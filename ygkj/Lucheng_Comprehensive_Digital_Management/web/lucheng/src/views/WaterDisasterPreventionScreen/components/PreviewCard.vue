<template>
  <div :class="[isBigScrren && 'is-big-screen', 'preview-card']">
    <div class="card-top">
      <div class="card-title">
        {{ SeawallDrillData.label }}演练中
        <span>{{ SeawallDrillData.showTime }}</span>
        <div @click="quitPreview" class="btn-area">
          <i class="el-icon-close"></i>
          <span>取消</span>
        </div>
      </div>
    </div>
    <div class="card-bottom">
      <div class="card-item">
        <div class="item-title">
          <i></i>
          <span>蓝色</span>
        </div>
        <div class="item-content">
          <strong style="color:#008FE8">{{ SeawallDrillData.blueLength }}</strong>km
        </div>
      </div>
      <div class="card-item">
        <div class="item-title">
          <i></i>
          <span>黄色</span>
        </div>
        <div class="item-content">
          <strong style="color:#E99100">{{ SeawallDrillData.yellowLength }}</strong>km
        </div>
      </div>
      <div class="card-item">
        <div class="item-title">
          <i></i>
          <span>橙色</span>
        </div>
        <div class="item-content">
          <strong style="color:#FF551F">{{ SeawallDrillData.orangeLength }}</strong>km
        </div>
      </div>
      <div class="card-item">
        <div class="item-title">
          <i></i>
          <span>红色</span>
        </div>
        <div class="item-content">
          <strong style="color:#E80000">{{ SeawallDrillData.redLength }}</strong>km
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention';
export default {
  name: 'preview-card',
  data() {
    return {
      statisticsObj: {},
      showTime: '',
      top: '40px',
      isSelect: ''
    };
  },
  computed: {
    ...mapGetters(['previewData', 'formatterCurrentTime', 'formatterStartTime', 'AffairId', 'SeawallDrillData']),
    currentTime() {
      let time = this.formatterCurrentTime();
      return time != 'Invalid date' ? time : '请选择时间';
    },
    isBigScrren() {
      return this.$route.query.large == 'true';
    }
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
            });
          }
        });
      }
    }
  },
  methods: {
    // 村落落点
    droppoint(val) {
      this.isSelect = val;
      console.log(this.statisticsObj, 2222);
      this.$emit('showPoint', val, this.statisticsObj);
      this.rainstormDrillStatistics();
    },
    ...mapActions(['setPreviewData', 'changePreviewModule', 'setCurrentTimeatmap', 'changeSeawallRangModule','setTyphoonVal']),
    quitPreview() {
      this.setTyphoonVal('')
      this.changeSeawallRangModule(false);

    },
    async clearFloodWarning() {
      await disasterPreventionApi.clearFloodWarning();
    },
    async rainstormDrillStatistics() {
      const preTime = new Date(this.formatterStartTime()).format('yyyy-MM-dd hh:mm:ss');
      let opt = {
        endTime: new Date(this.currentTime).format('yyyy-MM-dd hh:mm:ss'),
        startTime: preTime,
        // endTime: "2020-08-04 08:00:00",
        drillAffairId: this.AffairId
      };
      let res = await disasterPreventionApi.rainstormDrillStatistics(opt);
      if (res.code == 0) {
        this.statisticsObj = res.data;
        this.showTime = res.data.endTime;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.preview-card {
  width: 792px;
  height: 152px;
  background: url('../../../assets/images/count-card-bg.png') no-repeat center center;
  background-size: 100% 100%;
  padding: 0 39px;
  box-sizing: border-box;
  color: #ffffff;
  font-size: 16px;
  .card-top {
    position: relative;
    width: 100%;
    height: 55px;
    font-size: 32px;
    color: #1ef8f2;
    border-bottom: 1px solid rgba(255, 255, 255, 0.4);
    text-align: center;
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
        line-height: 55px;
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
      font-size: 24px;
      color: #ffffff;
      letter-spacing: 0;
      line-height: 32px;
      padding-bottom: 10px;
    }
    .item-content {
      @include flexbox;
      @include flexJC(center);
      @include flexAC;
      font-size: 24px;
      strong {
        font-size: 36px;
        letter-spacing: 0;
        line-height: 36px;
        font-weight: 600;
      }
    }
  }
}
</style>