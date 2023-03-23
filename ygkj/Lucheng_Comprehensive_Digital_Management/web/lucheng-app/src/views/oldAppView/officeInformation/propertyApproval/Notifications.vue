<template>
  <view-page>
    <div class="notification_wrap">
      <!--  消息提醒-->
      <div class="info_list" v-if="list.length > 0">
        <van-list v-model="loading" :finished="finished">
          <div class="card-list" v-for="(item, index) in list" :key="index">
            <div class="card-box">
              <div class="item-top">
                <img :src="imgSrc" />
                <span class="item_title">{{ item.message }}</span>
                <span class="info_time">{{ item.reminderTime }}</span>
              </div>
              <div class="item-content">
                <span>您有一条新的消息，请您及时查看！</span>
              </div>
              <div class="item-foot">
                <span class="detail" @click="getDetail(item)">查看详情</span>
                <span class="state" v-if="item.status === '1'">未处理</span>
                <span
                  class="state"
                  v-if="item.status === '2'"
                  style="background: rgba(18, 255, 162, 0.09); color: #44d7b6"
                  >已处理</span
                >
              </div>
            </div>
          </div>
        </van-list>
      </div>
      <div class="no-data-box" v-else>
        <noData />
      </div>
    </div>
  </view-page>
</template>

<script>
import { propertyMt } from "@/api/appView";
import noData from "@/components/noData.vue";
export default {
  name: "Notifications",
  props: {},
  components: { noData },
  data() {
    return {
      finished: false,
      loading: false,
      list: [],
      imgSrc: require("@/assets/images/officeInformation/info_icon.png"),
    };
  },
  mounted() {
    this.addZwlog(this)
    this.getList();
  },
  methods: {
    async getList() {
      let res = await propertyMt.getMsgList({});
      if (res.code === 0) {
        this.list = res.data;
        this.loading = this.finished = true;
      }
    },
    getDetail(item) {
      this.$router.push("/businessApprovalInfo?id=" + item.associationId);
      propertyMt
        .getMsgRead({
          id: item.associationId,
        })
        .then((res) => {});
    },
  },
};
</script>

<style scoped lang="scss">
.notification_wrap {
  width: 100%;
  height: 100%;
  .info_list {
    height: calc(100% - 44px);
    background-color: #f2f6f9;
    overflow: auto;
    .card-list {
      background-color: #ffffff;
      padding: 0 12px 8px 11px;
      margin-bottom: 6px;
      .card-box {
        .item-top {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding-top: 12px;
          img {
            width: 30px;
            height: 30px;
          }
          .item_title {
            width: 185px;
            font-size: 20px;
            font-weight: 200;
            color: #333333;
            margin: 0px 20px 0px 12px;
            line-height: 24px;
          }
          .info_time {
            width: 110px;
            font-size: 18px;
            font-weight: 200;
            color: #666666;
            line-height: 22px;
            //margin-top: 8px;
          }
        }
        .item-content {
          width: 335px;
          font-size: 18px;
          font-weight: 200;
          color: #999999;
          line-height: 18px;
          margin-top: 18px;
          padding-bottom: 12px;
          border-bottom: 1px solid #e6e6e6;
        }
        .item-foot {
          margin-top: 9px;
          display: flex;
          justify-content: space-between;
          align-content: center;
          .detail {
            font-size: 18px;
            color: #333333;
          }
          .state {
            background: rgba(18, 143, 255, 0.09);
            border-radius: 4px;
            font-size: 20px;
            color: #0faafc;
            line-height: 16px;
            padding: 4px 6px;
          }
        }
      }
    }
  }
}
</style>