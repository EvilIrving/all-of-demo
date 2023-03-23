<template>
  <div class="notification_wrap">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="$route.meta.title" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <!--  消息提醒-->
    <div class="info_list">
      <van-list v-model="loading" :finished="finished" @load="onLoad">
        <div class="card-list" v-for="item in list" :key="item">
          <div class="card-box">
            <div class="item-top">
              <img :src="imgSrc" />
              <span class="item_title">XXXXXXXXXXXXXX</span>
              <span class="info_time">2022.06.01 12:00:01</span>
            </div>
            <div class="item-content">
              <span>您有一条新的消息，请您及时查看！</span>
            </div>
            <div class="item-foot">
              <span class="detail">查看详情</span>
              <span class="state">未处理</span>
            </div>
          </div>
        </div>
      </van-list>
    </div>
  </div>
</template>

<script>
export default {
  name: "MessageReminder",
  props: {},
  components: {},
  data() {
    return {
      finished: false,
      loading: false,
      list: [
        {
          name: 'XXXXXX工程名称'
        }
      ],
      imgSrc: require("@/assets/images/officeInformation/info_icon.png")
    }
  },
  mounted() {
    this.addZwlog(this)
  },
  methods: {
    onLoad() {
      // 异步更新数据
      // setTimeout 仅做示例，真实场景中一般为 ajax 请求
      setTimeout(() => {
        for (let i = 0; i < 10; i++) {
          this.list.push(this.list.length + 1);
        }

        // 加载状态结束
        this.loading = false;

        // 数据全部加载完成
        if (this.list.length >= 40) {
          this.finished = true;
        }
      }, 1000);
    },

  }
}
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
          line-height: 26px;

          padding-top: 12px;
          img {
            width: 30px;
            height: 30px;
          }
          .item_title {
            width: 125px;
            height: 14px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 200;
            color: #333333;
            line-height: 14px;
            margin: 8px 57px 0px 12px;
          }

          .info_time {
            width: 114px;
            height: 14px;
            font-size: 12px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 200;
            color: #666666;
            line-height: 14px;
            margin-top: 8px;
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
            font-size: 14px;
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
}
</style>