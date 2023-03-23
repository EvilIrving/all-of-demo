<!--
 * @Date: 2022-06-08 14:22:55
 * @Author: 
 * @Description: 督办首页tab切换
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-11-09 11:07:03
 * @FilePath: \lucheng-older\src\views\appView\propertysupervision\Index.vue
-->

<template>
  <view-page>
    <div class="wuye_wrap">
      <div class="maintenance" style="padding-top: 21px">
        <div class="title">
          <span>物业督办</span>
        </div>
        <div class="content">
          <van-grid :gutter="2" direction="horizontal" center>
            <van-grid-item @click="gotoPgae('/propertySupervision/inspection')">
              <div class="content_left">
                <div class="left_title">督查检查</div>
                <div class="num">{{ countInfo.inspectNum }}个</div>
              </div>
              <img :src="imgsrc.relayTask" />
            </van-grid-item>
            <van-grid-item @click="gotoPgae('/propertySupervision/supervise')">
              <div class="content_left">
                <div class="left_title">督查督办</div>
                <div class="num">{{ countInfo.superviseNum }}个</div>
              </div>
              <img :src="imgsrc.businessApproval" />
            </van-grid-item>
            <van-grid-item @click="gotoPgae('/propertySupervision/schedule')">
              <div class="content_left">
                <div class="left_title">进度查询</div>
                <div class="num">{{ countInfo.progressNum }}个</div>
              </div>
              <img :src="imgsrc.progressQuery" />
            </van-grid-item>
            <van-grid-item @click="gotoPgae('/propertySupervision/archive')">
              <div class="content_left">
                <div class="left_title">归档成果</div>
                <div class="num">{{ countInfo.filedNum }}个</div>
              </div>
              <img :src="imgsrc.acceptanceApproval" />
            </van-grid-item>
          </van-grid>
        </div>
      </div>
    </div>
  </view-page>
</template>

<script>
import { superviseApi } from "@/api/rainfall";
export default {
  name: "Index",
  props: {},
  components: {},
  data() {
    return {
      imgsrc: {
        relayTask: require("@/assets/images/officeInformation/relay_task.png"),
        businessApproval: require("@/assets/images/officeInformation/business_approval.png"),
        progressQuery: require("@/assets/images/officeInformation/progress_qiery.png"),
        acceptanceApplication: require("@/assets/images/officeInformation/acceptanceApplication.png"),
        acceptanceApproval: require("@/assets/images/officeInformation/acception_approval.png"),
      },
      countInfo: {},
    };
  },
  mounted() {
    this.addZwlog(this)
    this.getCounts();
  },
  methods: {
    gotoPgae(path, type) {
      this.$router.push(path);
    },
    async getCounts() {
      let res = await superviseApi.counts();
      if (res.code == 0) {
        this.countInfo = res.data;
      }
    },
  },
};
</script>

<style scoped lang="scss">
.wuye_wrap {
  height: 80%;
  width: 100%;
  background-color: #ffffff;
  .title {
    font-size: 22px;
    color: #000000;
    padding-left: 16px;
  }
  .content {
    margin-top: 18px;
    padding: 0 16px;
    .content_left {
      .left_title {
        font-size: 20px;
        color: #333333;
      }
      .num {
        font-size: 20px;
        color: #666666;
        margin-top: 10px;
      }
    }
    img {
      width: 33px;
      height: 33px;
    }
  }
}
</style>
<style lang="scss">
.wuye_wrap {
  .content {
    .van-grid {
      justify-content: space-between;
    }
    .van-grid-item {
      margin-bottom: 15px;
    }
    .van-grid-item__content {
      width: 163px;
      background: #f8f8f7;
      border-radius: 5px;
      justify-content: space-between;
    }
  }
}
</style>
