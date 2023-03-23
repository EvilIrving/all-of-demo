<!--
 * @Date: 2022-06-13 18:59:23
 * @Author: 
 * @Description: 报警
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-08-12 17:32:55
 * @FilePath: \lucheng-app\src\views\appView\workbench\PoliceMessage.vue
-->
<template>
  <div class="notification_wrap">
    <van-nav-bar
      fixed
      placeholder
      safe-area-inset-top
      :title="title"
      :left-arrow="$store.state.viewType !== 'irs'"
      @click-left="$router.go(-1)"
    />
    <!--  报警-->
    <div class="info_list" v-if="list.length > 0">
      <van-list offset="100">
        <div
          class="card-list"
          v-for="(item, index) in list"
          :key="index"
          @click="goLink(item)"
        >
          <div class="card-box">
            <div class="item-top">
              <!-- readStatus 1-未读  2-已读 -->
              <img :src="imgSrc" />
              <img
                v-if="item.readStatus == '1'"
                class="redpoint"
                :src="redpoint"
              />
              <!-- （1.安全鉴定超期，2.汛前检查超期,3.年度总结超期,4.巡查信息异常,5.工程隐患,6.海塘实时报警,7.海塘预报预警,8.时段雨量报警,9.河道水情报警,10.水库水情报警,11.潮位报警）', -->
              <span class="item_title" v-if="item.warningrule === 1"
                >安全鉴定超期</span
              >
              <span class="item_title" v-if="item.warningrule === 2"
                >汛前检查超期</span
              >
              <span class="item_title" v-if="item.warningrule === 3"
                >年度总结超期</span
              >
              <span class="item_title" v-if="item.warningrule === 4"
                >巡查信息异常</span
              >
              <span class="item_title" v-if="item.warningrule === 5"
                >工程隐患</span
              >
              <span class="item_title" v-if="item.warningrule === 6"
                >海塘实时报警</span
              >
              <span class="item_title" v-if="item.warningrule === 7"
                >海塘预报预警</span
              >
              <span class="item_title" v-if="item.warningrule === 8"
                >时段雨量报警</span
              >
              <span class="item_title" v-if="item.warningrule === 9"
                >河道水情报警</span
              >
              <span class="item_title" v-if="item.warningrule === 10"
                >水库水情报警</span
              >
              <span class="item_title" v-if="item.warningrule === 11"
                >潮位报警</span
              >
              <span class="item_title" v-if="item.warningrule === 12"
                >测站关联报警</span
              >
              <span class="item_title" v-if="item.warningrule === 13"
                >测站限值报警</span
              >
              <span class="item_title" v-if="item.warningrule === 14"
                >预案资料提醒</span
              >
              <span class="item_title" v-if="item.warningrule === 15"
                >日常检查提醒</span
              >
              <span class="item_title" v-if="item.warningrule === 16"
                >汛后检查提醒</span
              >
              <span class="item_title" v-if="item.warningrule === 17"
                >年度检查提醒</span
              >
              <span class="item_title" v-if="item.warningrule === 18"
                >汛前检查提醒</span
              >
              <span class="info_time">{{ item.warningtime }}</span>
            </div>
            <div class="item-content">
              <span>您有一条新的消息，请您及时查看！</span>
            </div>
            <div class="item-foot">
              <span class="detail">查看详情</span>
            </div>
          </div>
        </div>
      </van-list>
      <div class="bottom_btn" @click="readAll">全部已读</div>
    </div>
    <div class="no-data-box" v-else>
      <noData />
    </div>
  </div>
</template>

<script>
import { policeMessage } from "@/api/projectDetail";
import { localData } from "@/plugins/storage";
import noData from "@/components/noData.vue";
export default {
  name: "PoliceMessage",
  props: {},
  components: { noData },
  computed: {
    userInfo() {
      return localData("get", "userInfo");
    },
  },
  data() {
    return {
      list: [],
      type: 1,
      imgSrc: require("@/assets/images/officeInformation/info_icon.png"),
      redpoint: require("@/assets/red_point.png"),
    };
  },
  computed: {
    title() {
      let wz = "";
      this.$route.query.id == 1 ? (wz = "报警") : (wz = "提醒");
      return wz;
    },
  },
  watch: {
    imgSrc: function (newVal, oldVal) {
      if (this.id === 1) {
        this.imgSrc = require("@/assets/images/officeInformation/info_icon.png");
      } else {
        this.imgSrc = require("@/assets/tixing.png");
      }
    },
  },
  mounted() {
    this.addZwlog(this)
    this.type = this.$route.query.type;
    this.getList();
  },

  methods: {
    async goLink(data, index) {
      this.$router.push("/policeDetail?id=" + data.id);
    },
    async getList() {
      let res = await policeMessage.list({ ruletype: this.type });
      if (res.code === 0) {
        this.list = res.data.list;
      }
    },
    async readAll() {
      let res = await policeMessage.readAll();
      if (res.code === 0) {
        this.$toast(res.data);
        this.getList();
      }
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
          position: relative;
          img {
            width: 30px;
            height: 30px;
          }
          .redpoint {
            position: absolute;
            top: 10px;
            left: 30px;
            width: 5px;
            height: 5px;
          }
          .item_title {
            width: 165px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 200;
            color: #333333;
            margin: 0px 17px 0px 12px;
            line-height: 16px;
          }

          .info_time {
            width: 114px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 200;
            color: #666666;
            margin-top: 0px;
          }
          .card-item-label {
            color: #666666;
            font-size: 13px;
            position: relative;
            padding-left: 13px;
            &::after {
              position: absolute;
              content: "";
              width: 6px;
              height: 6px;
              background-color: #999999;
              border-radius: 50%;
              left: 0;
              top: 50%;
              margin-top: -4px;
            }
          }
          .card-item-value {
            color: #333333;
            font-size: 13px;
            max-width: 75%;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
          }
        }
        .item-content {
          width: 335px;
          height: 18px;
          font-size: 12px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 200;
          color: #999999;
          line-height: 18px;
          margin-top: 12px;
          padding-bottom: 7px;
          border-bottom: 1px solid #e6e6e6;
        }
        .item-foot {
          margin-top: 8px;
          .detail {
            width: 56px;
            height: 18px;
            font-size: 16px;
            font-weight: 600;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 200;
            color: #333333;
            line-height: 18px;
            margin-right: 238px;
          }
          .state {
            width: 45px;
            height: 20px;
            background: rgba(18, 143, 255, 0.09);
            border-radius: 4px;
            font-size: 11px;
            font-family: SourceHanSansCN-Normal, SourceHanSansCN;
            font-weight: 200;
            color: #0faafc;
            line-height: 16px;
            padding: 4px 6px;
          }
        }
      }
    }
  }
  .no-data-box {
    padding-top: 50px;
  }
}
</style>