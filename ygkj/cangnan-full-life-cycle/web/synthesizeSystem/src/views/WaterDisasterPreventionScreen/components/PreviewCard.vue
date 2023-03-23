<template>
  <div :class="[isBigScrren && 'is-big-screen', 'preview-card']">
    <div class="card-top">
      <div class="card-title">
        {{ previewData.label }}演练中
        <span>{{ this.showTime }}</span>
        <div @click="quitPreview" class="btn-area">
          <i class="el-icon-close"></i>
          <span>取消</span>
        </div>
      </div>
      <!-- <div class="card-top-btn">
        <div>
          <i class="icon-moniyanlian"></i>
          <span>模拟</span>
        </div>
        <div>
          <i class="el-icon-star-off"></i>
          <span>收藏</span>
        </div>
        <div @click="quitPreview">
          <i class="el-icon-close"></i>
          <span>取消</span>
        </div>
      </div> -->
    </div>
    <div class="card-bottom">
      <div
        :class="['card-item', isSelect == '影响村落' ? 'active' : '']"
        style="cursor: pointer"
        @click="droppoint('影响村落')"
      >
        <div class="item-title">
          <i></i>
          <span>影响村落</span>
        </div>
        <div class="item-content">
          <strong>{{ statisticsObj.gradevillages }}</strong
          >个
        </div>
      </div>
      <div class="card-item">
        <div class="item-title">
          <i></i>
          <span>准备转移人口</span>
        </div>
        <div class="item-content">
          <strong>{{ statisticsObj.grade4Person }}</strong
          >人
        </div>
      </div>
      <div class="card-item">
        <div class="item-title">
          <i></i>
          <span>立即转移人口</span>
        </div>
        <div class="item-content">
          <strong style="color: #ff7144">{{
            statisticsObj.grade5Person
          }}</strong
          >人
        </div>
      </div>
      <div
        :class="['card-item', isSelect == '水库' ? 'active' : '']"
        style="cursor: pointer"
        @click="droppoint('水库')"
      >
        <div class="item-title">
          <i></i>
          <span>超汛限大中型水库</span>
        </div>
        <div class="item-content">
          <strong>{{ statisticsObj.overLimitNum }}</strong
          >座
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
export default {
  name: 'preview-card',
  data() {
    return {
      statisticsObj: {},
      showTime: "",
      top: "40px",
      isSelect: "",
    }
  },
  computed: {
    ...mapGetters(["previewData", "formatterCurrentTime", "formatterStartTime", "AffairId"]),
    currentTime() {
      let time = this.formatterCurrentTime();
      return time != "Invalid date" ? time : "请选择时间";
    },
    isBigScrren() {
      return this.$route.query.large == "true";
    },
  },
  watch: {
    currentTime: {
      immediate: true,
      handler(val) {
        this.$nextTick(() => {
          if (this.currentTime != '请选择时间') {
            this.rainstormDrillStatistics().then(() => {
              if (this.isSelect) {
                this.droppoint(this.isSelect);
              }
            })
          }
        })
      }
    },
  },
  methods: {
    // 村落落点
    droppoint(val) {
      this.isSelect = val;
      console.log(this.statisticsObj,2222);
      this.$emit('showPoint', val, this.statisticsObj);
      this.rainstormDrillStatistics();
    },
    ...mapActions(['setPreviewData', 'changePreviewModule', 'setCurrentTimeatmap']),
    quitPreview() {
      this.changePreviewModule(false);
      this.setCurrentTimeatmap('');
      this.clearFloodWarning();
      if (this.isSelect) {
        this.$emit('showPoint', this.isSelect, {mountainWarnPoint:[],rsvrWarnPoint:[]});
      }
    },
    async clearFloodWarning() {
      await disasterPreventionApi.clearFloodWarning();
    },
    async rainstormDrillStatistics() {
      const preTime = new Date(this.formatterStartTime()).format('yyyy-MM-dd hh:mm:ss');
      let opt = {
        endTime: new Date(this.currentTime).format("yyyy-MM-dd hh:mm:ss"),
        startTime: preTime,
        // endTime: "2020-08-04 08:00:00",
        drillAffairId: this.AffairId
      }
      let res = await disasterPreventionApi.rainstormDrillStatistics(opt);
      if (res.code == 0) {
        this.statisticsObj = res.data;
        this.showTime = res.data.endTime;
      }
    },

  }
}
</script>

<style lang="scss" scoped>
.preview-card {
  width: 870px;
  padding: 0px 24px;
  margin-top: 22px;
  background: url(~assets/images/preview-card-bg.png) no-repeat center center;
  background-size: 100% 100%;
  box-shadow: 0 0 5px 0 #289dfb;
  border-radius: 45px;
  -webkit-animation: shadowOut 4s infinite ease-in-out;
  animation: shadowOut 4s infinite ease-in-out;
  .card-top {
    padding: 12px 0;
    border-bottom: 1px solid rgba(255, 255, 255, 0.4);
    @include flexbox;
    @include flexflow(row, nowrap);
    > div {
      width: 100%;
      // &:first-child{
      //   width: 60%;
      // }
      &.card-title {
        @include flexbox;
        @include flexJC(center);
        font-size: 24px;
        color: #1ef8f2;
        letter-spacing: 0;
        line-height: 24px;
        font-weight: 500;
        position: relative;
        > span {
          color: #fff;
          font-size: 20px;
          display: inline-block;
          padding-left: 10px;
        }
        .btn-area {
          color: #fff;
          align-self: flex-end;
          position: absolute;
          right: 0;
          top: 4px;
          font-size: 18px;
          cursor: pointer;
          i {
            display: inline-block;
            margin-right: 6px;
            font-size: 20px;
          }
        }
      }
      &.card-top-btn {
        @include flexbox;
        @include flexflow(row, nowrap);
        @include flexAC;
        @include flexJC(space-around);
        cursor: pointer;
        i {
          display: inline-block;
          width: 17px;
          height: 17px;
          margin-right: 4px;
          color: #fff;
        }
        span {
          font-size: 16px;
          color: #ffffff;
        }
      }
    }
  }
  .card-bottom {
    padding: 8px 0;
    color: #fff;
    @include flexbox;
    @include flexflow(row, nowrap);
    @include flexAC;
    @include flexJC(space-between);
    .card-item {
      &.active {
        span {
          color: #1ef8f2;
        }
      }
    }
    .item-title {
      font-size: 20px;
      color: #ffffff;
      letter-spacing: 0;
      line-height: 32px;
      font-weight: 600;
    }
    .item-content {
      @include flexbox;
      @include flexJC(center);
      @include flexAC;
      strong {
        font-size: 30px;
        color: #eec80b;
        letter-spacing: 0;
        line-height: 32px;
        font-weight: 600;
      }
    }
  }
}
@-webkit-keyframes shadowOut {
  0% {
    -webkit-box-shadow: 0 0 50px 0 #289dfb;
  }
  50% {
    -webkit-box-shadow: 0 0 5px 0 #289dfb;
  }
  100% {
    -webkit-box-shadow: 0 0 50px 0 #289dfb;
  }
}
@keyframes shadowOut {
  0% {
    box-shadow: 0 0 50px 0 #289dfb;
  }
  50% {
    box-shadow: 0 0 5px 0 #289dfb;
  }
  100% {
    box-shadow: 0 0 50px 0 #289dfb;
  }
}
</style>