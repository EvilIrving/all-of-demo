<!--
 * @Date: 2022-06-14 15:59:17
 * @Author: 
 * @Description: 审批
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-08-15 10:16:03
 * @FilePath: \lucheng-app\src\views\appView\workbench\Approve.vue
-->

<template>
  <div class="notification_wrap">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="$route.meta.title" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <!--  审批-->
    <div class="info_list" v-if="list.length > 0">
      <van-list offset="100">
        <div class="card-list" v-for="(item, index) in list" :key="index" @click="goLink(item)">
          <div class="card-box">
            <div class="item-top">
              <!-- 未读1 已读2 -->
              <img :src="imgSrc" />
              <img v-if="item.status == 1" class="redpoint" :src="redpoint" />
              <span class="item_title">{{item.type}}</span>
              <span class="info_time">{{item.reminderTime}}</span>
            </div>
            <div class="item-content">
              <span>{{item.message}}</span>
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
  name: "Approve",
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
      imgSrc: require("@/assets/shenpi.png"),
      redpoint: require("@/assets/red_point.png"),
    };
  },
  mounted() {
    this.addZwlog(this)
    this.type = this.$route.query.type
    this.getList();
  },

  methods: {
    async goLink({ type, associationId, id }) {
      await policeMessage.readMessage({id})
      if (type == '合同管理') {
        this.$router.push("/acceptanceParticulars?id=" + associationId);
      } else {
        this.$router.push("/businessApprovalInfo?id=" + associationId);
      }
    },
    async getList() {
      let res = await policeMessage.getApproval();
      if (res.code === 0) {
        this.list = res.data;
      }
    },
    async readAll() {
      let res = await policeMessage.readAllMessage({type:'合同管理,维修养护'});
      if (res.code === 0) {
        this.$toast(res.data || '无未读消息');
        this.getList()
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