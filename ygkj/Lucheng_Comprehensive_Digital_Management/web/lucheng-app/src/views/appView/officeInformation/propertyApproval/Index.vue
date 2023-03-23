<template>
  <!--物业审批-->
  <div class="property_wrap">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="'物业审批'"  :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <div class="maintenance" style="padding-top: 21px">
      <div class="title">
        <span>维修养护</span>
      </div>
      <div class="content">
        <van-grid direction="horizontal" :border="false" center>
          <van-grid-item @click="goLink('/relayTask')">
            <div class="content_left">
              <p class="left_title">任务下达</p>
              <span class="num">{{ form.approveTotal }}个</span>
            </div>
            <img :src="imgsrc.relayTask" />
          </van-grid-item>
          <van-grid-item @click="goLink('/businessApproval')">
            <div class="content_left">
              <p class="left_title">业务审批</p>
              <span class="num">{{ form.pendTotal }}个</span>
            </div>
            <img :src="imgsrc.businessApproval" />
          </van-grid-item>
          <van-grid-item
            style="margin-top: 15px"
            @click="goLink('/progressQuery')"
          >
            <div class="content_left">
              <p class="left_title">进度查询</p>
              <span class="num">{{ form.progess }}个</span>
            </div>
            <img :src="imgsrc.progressQuery" />
          </van-grid-item>
          <van-grid-item
            style="margin-top: 15px"
            @click="goLink('/notifications')"
          >
            <div class="content_left">
              <p class="left_title">消息提醒</p>
              <span class="num">{{ form.msgTotal }}个</span>
            </div>
            <img :src="imgsrc.message" />
          </van-grid-item>
        </van-grid>
      </div>
    </div>
    <div class="contract_management" style="margin-top: 26px">
      <div class="title">
        <span>合同管理</span>
      </div>
      <div class="content">
        <van-grid direction="horizontal" :border="false" center>
          <van-grid-item
            @click="goLink('/acceptance?type=1')"
            v-if="userInfo.userType == 3"
          >
            <div class="content_left">
              <p class="left_title">验收申请</p>
              <span class="num"
                >{{ appApplicationCount.applicationNum }}个</span
              >
            </div>
            <img :src="imgsrc.acceptanceApplication" />
          </van-grid-item>
          <van-grid-item
            @click="goLink('/acceptance?type=2')"
            v-if="userInfo.userType != 3"
          >
            <div class="content_left">
              <p class="left_title">验收审批</p>
              <span class="num">{{ appApplicationCount.approvalNum }}个</span>
            </div>
            <img :src="imgsrc.acceptanceApproval" />
          </van-grid-item>
          <van-grid-item @click="goLink('/acceptanceProgress')">
            <div class="content_left">
              <p class="left_title">进度查询</p>
              <span class="num">{{ appApplicationCount.progressNum }}个</span>
            </div>
            <img :src="imgsrc.progressQuery" />
          </van-grid-item>
          <van-grid-item
            style="margin-top: 15px"
            @click="goLink('/acceptanceMessage')"
            v-if="userInfo.userType != 3"
          >
            <div class="content_left">
              <p class="left_title">消息提醒</p>
              <span class="num">{{ appApplicationCount.messageNum }}个</span>
            </div>
            <img :src="imgsrc.message" />
          </van-grid-item>
        </van-grid>
      </div>
    </div>
  </div>
</template>

<script>
import { localData } from "@/plugins/storage";
import { appApplicationCountApi } from "@/api/rainfall.js";
import { propertyMt } from "@/api/appView";
export default {
  name: "Index",
  props: {},
  components: {},
  computed: {
    userInfo() {
      return localData("get", "userInfo");
    },
  },
  data() {
    return {
      appApplicationCount: {},
      imgsrc: {
        relayTask: require("@/assets/images/officeInformation/relay_task.png"),
        businessApproval: require("@/assets/images/officeInformation/business_approval.png"),
        progressQuery: require("@/assets/images/officeInformation/progress_qiery.png"),
        message: require("@/assets/images/officeInformation/message.png"),
        acceptanceApplication: require("@/assets/images/officeInformation/acceptanceApplication.png"),
        acceptanceApproval: require("@/assets/images/officeInformation/acception_approval.png"),
      },
      form: {},
    };
  },
  mounted() {
    this.addZwlog(this)
    this.getAppApplicationCount();
    this.getList();
  },
  methods: {
    goLink(path) {
      this.$router.push(path);
    },
    async getList() {
      let res = await propertyMt.getHiddenAppTotal({});
      if (res.code === 0) {
        this.form = res.data;
      }
    },
    async getAppApplicationCount() {
      let res = await appApplicationCountApi();
      if (res.code == 0) {
        this.appApplicationCount = res.data;
      }
    },
  },
};
</script>

<style scoped lang="scss">
.property_wrap {
  height: 100%;
  width: 100%;
  background: #ffffff;
  .title {
    width: 90px;
    font-size: 15px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 250;
    color: #000000;
    line-height: 21px;
    margin-left: 1.5px;
    //margin-top: 21px;
    text-align: center;
  }
  .content {
    margin-top: 18px;
    padding: 0 15px;
    .content_left {
      width: calc(100% - 43px);
      .left_title {
        font-size: 15px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 250;
        color: #333333;
        line-height: 21px;
      }
      .num {
        font-size: 13px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 200;
        color: #666666;
        line-height: 18px;
        margin-top: 4.5px;
      }
    }
    img {
      width: 33px;
      height: 33px;
      margin-left: 10px;
    }
  }
}
</style>
<style lang="scss">
.property_wrap {
  .content {
    .van-grid {
      justify-content: space-between;
      .van-grid-item {
        padding-right: 0 !important;
      }
    }
    .van-grid-item__content {
      width: 165px;
      background: #f8f8f7;
      border-radius: 5px;
    }
  }
}
</style>
