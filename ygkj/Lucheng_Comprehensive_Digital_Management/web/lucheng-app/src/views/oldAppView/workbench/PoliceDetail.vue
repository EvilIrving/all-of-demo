<template>
  <view-page>
    <div class="notification_wrap">
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
                <img :src="imgSrc" />
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
              <div class="item-foot">
                <span class="detail">{{ item.warningcontent }}</span>
              </div>
            </div>
          </div>
        </van-list>
        <div
            :class="['bottom_btn', { white: list[0].status == '1' }]"
            @click="goLink"
        >
          {{ list[0].status == "1" ? "已转发" : "转发" }}
        </div>
      </div>
      <div class="no-data-box" v-else>
        <noData />
      </div>
    </div>
  </view-page>
</template>

<script>
import { policeMessage } from "@/api/projectDetail";
import noData from "@/components/noData.vue";
export default {
  name: "policeDetail",
  props: {},
  components: { noData },
  data() {
    return {
      list: [],
      imgSrc: require("@/assets/images/officeInformation/info_icon.png"),
      id: "",
    };
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
    this.id = this.$route.query.id;
    this.getById(this.id);
  },
  methods: {
    async goLink() {
      if (this.list[0].status == "1") {
        // this.$router.push("/transinfo?id=" + this.id);
      } else {
        this.$router.push("/transinfo?id=" + this.id);
      }
    },
    async getById(id) {
      let res = await policeMessage.getById({ id });
      if (res.code === 0) {
        this.list = [res.data] || [];
        res.data.readStatus == "1" ? this.readById(this.id) : "";
      }
    },
    async readById(id) {
      await policeMessage.readById({ warningId: id });
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
          padding-bottom: 7px;
          border-bottom: 1px solid #e6e6e6;
          img {
            width: 30px;
            height: 30px;
          }
          .item_title {
            width: 165px;
            font-size: 20px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 200;
            color: #333333;
            margin: 0px 17px 0px 12px;
            line-height: 16px;
          }

          .info_time {
            width: 114px;
            font-size: 18px;
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
          font-size: 18px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 200;
          color: #999999;
          line-height: 18px;
          margin-top: 12px;
        }
        .item-foot {
          margin-top: 8px;
          .detail {
            width: 56px;
            height: 18px;
            font-size: 18px;
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
    .bottom_btn{
      font-size: 18px;
    }
  }
  .no-data-box {
    padding-top: 50px;
  }
}
</style>
