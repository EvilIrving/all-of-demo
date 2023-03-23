<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-12-31 10:30:05
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\simpleRangDialog.vue
-->
<template>
  <div class="range-timeline-bar" :class="{ 'is-big-screen': isBigScreen }">
    <div class="timeline-action" @click="inPlay = !inPlay">
      <i :class="[inPlay ? 'el-icon-video-pause' : 'el-icon-video-play']" />
    </div>
    <el-tooltip effect="dark" content="切换上一段时间" placement="top">
      <div class="prev-button" @click="handlePrevButton" :disabled="!position >= 1" />
    </el-tooltip>
    <div class="range-timeline-bar__wrapper">
      <div class="range-timeline-bar__scale">
        <a-slider v-if="timelists.length > 0" :tipFormatter="formatTooltip" :value="value" tooltipPlacement="top" dots :min="1" :max="maxValue" :marks="getMark()" @change="changeValue" />
      </div>
    </div>
    <el-tooltip effect="dark" content="切换下一段时间" placement="top">
      <div class="next-button" @click="handleNextButton" :disabled="!isBefore" />
    </el-tooltip>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { debounce } from '@/utils/utils';
import moment from 'moment';
export default {
  props: {
    autoPlay: { type: Boolean, default: false }
  },
  data() {
    return {
      value: 1,
      position: 0,
      inPlay: false,
      preTimeList: [],
      timelists: [],
      playTimer: null
    };
  },
  computed: {
    ...mapGetters(['seawallData', 'simpleTimes']),
    isBigScreen() {
      return this.$route.query.large == 'true';
    },
    timeRanges() {
      return this.timelists[this.position];
    },
    timeList() {
      let startTime = this.timeRanges[0];
      let endTime = this.timeRanges[this.timeRanges.length - 1];
      let obj = { 1: startTime };
      if (moment(startTime).format('YYYY-MM-DD') == moment(endTime).format('YYYY-MM-DD')) {
        obj[this.timeRanges.length] = endTime;
        return obj;
      } else {
        let times = moment(startTime).add(1, 'd').format('YYYY-MM-DD');
        this.timeRanges.forEach((item, index) => {
          let time = item;
          if (moment(time).format('YYYY-MM-DD') == times) {
            times = moment(times).add(1, 'd').format('YYYY-MM-DD');
            obj[index + 1] = time;
          }
        });
        obj[this.timeRanges.length] = endTime;
        let arr = Object.keys(obj);
        if (arr[arr.length - 1] - arr[arr.length - 2] < 10) {
          delete obj[arr[arr.length - 2]];
        }
        return obj;
      }
    },
    maxValue() {
      return this.timeRanges.length;
    },
    isBefore() {
      return this.position < this.timelists.length - 1;
    }
  },
  watch: {
    inPlay: {
      deep: true,
      immediate: true,
      handler: function (val, oldVal) {
        console.log(val);
        if (val) {
          this.startPlayTimer();
        } else {
          this.setBarTimer(null);
        }
      }
    },
    value: debounce(function (val) {
      this.setSeawallDrill(val);
    }, 1000),
    seawallRangModule: function (val) {
      if (!val) {
        this.setBarTimer();
      }
    },
    simpleTimes: async function (val) {
      this.position = 0
      this.value = 1
      this.preTimeList = []
      let startTimes = moment(this.simpleTimes[0]).format('YYYY-MM-DD HH:mm:ss');
      let endTimes = moment(this.simpleTimes[1]).add(1, 'd').format('YYYY-MM-DD HH:mm:ss');
      await this.setTimes(startTimes, endTimes, this.preTimeList);
      await this.getSliderMark();
      await this.setSeawallDrill(this.value);
    }
  },
  async mounted() {
    let startTimes = moment(this.simpleTimes[0]).format('YYYY-MM-DD HH:mm:ss');
    let endTimes = moment(this.simpleTimes[1]).add(1, 'd').format('YYYY-MM-DD HH:mm:ss');
    await this.setTimes(startTimes, endTimes, this.preTimeList);
    await this.getSliderMark();
    await this.setSeawallDrill(this.value);
  },
  methods: {
    ...mapActions(['setSimpleRangTime', 'setPreviewTime']),
    setTimes(start, end, arr) {
      let startTime = new Date(start).getTime();
      let endTime = new Date(end).getTime();
      if (startTime < endTime) {
        arr.push(start);
        let times = moment(start).add(1, 'h').format('YYYY-MM-DD HH:mm:ss');
        this.setTimes(times, end, arr);
      }
    },
    setSeawallDrill(num) {
      let data = this.timeRanges[num - 1];
      this.setSimpleRangTime(data);
    },
    changeValue(value) {
      this.inPlay = false;
      this.value = value;
    },
    formatTooltip(value) {
      let data = this.timeRanges[value - 1];
      return data;
    },
    getMark() {
      //演练模式时时间label要改成当前时间开始的判断
      let timelist = JSON.parse(JSON.stringify(this.timeList));
      for (let item in this.timeList) {
        let datetime = moment(this.timeList[item]);
        let date = datetime.format('MoDo');
        let time = datetime.format('HH时');
        const VNode = (
          <div class="mark-label">
            <div class="date">{date}</div>
            <div class="time">{time}</div>
          </div>
        );
        timelist[item] = { label: VNode };
      }
      return timelist;
    },
    handleNextButton() {
      if (this.isBefore) {
        this.position++;
        this.value = 1;
        let tempArr = this.timelists[this.position]
        this.setPreviewTime([tempArr[0], tempArr[tempArr.length - 1]])
        this.setSeawallDrill(this.value);
      }
    },
    handlePrevButton() {
      if (this.position >= 1) {
        this.position--;
        this.value = 1;
        let tempArr = this.timelists[this.position]
        this.setPreviewTime([tempArr[0], tempArr[tempArr.length - 1]])
        this.setSeawallDrill(this.value);
      }
    },
    setBarTimer() {
      this.inPlay = false;
      clearInterval(this.playTimer);
    },
    startPlayTimer() {
      if (this.value < this.maxValue) {
        this.playTimer = setInterval(() => {
          this.value++;
          if (this.value == this.timeRanges.length) {
            this.setBarTimer();
          }
        }, 5000);
      } else {
        this.setBarTimer();
      }
    },
    getSliderMark() {
      let arr = this.preTimeList;
      let num = Math.ceil(arr.length / 100);
      let arr_pre = [];
      for (let i = 0; i < num; i++) {
        let start = i * 100;
        let end = (i + 1) * 100;
        let data = arr.slice(start, end);
        arr_pre.push(data);
      }
      this.timelists = arr_pre;
      let tempArr = this.timelists[0]
      this.setPreviewTime([tempArr[0], tempArr[tempArr.length - 1]])
    }
  },
  destroyed() {
    this.setBarTimer();
  }
};
</script>

<style lang="scss" scoped>
$color: #12b5ff;
$repeatingLinearGrd1: repeating-linear-gradient(180deg, $color 0, $color 1px, transparent 0, transparent 10px);
$repeatingLinearGrd2: repeating-linear-gradient(180deg, $color 0, $color 1px, transparent 0, transparent 5px);
.range-timeline-bar {
  overflow-x: hidden;
  position: absolute;
  // width: 1050px;
  padding: 0 16px;
  height: 70px;
  left: 50%;
  bottom: 112px;
  transform: translateX(-50%);
  background-image: linear-gradient(209deg, #1868d4c9 0%, #1d7fffb3 100%);
  border-radius: 35px;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: center;
  .timeline-action {
    color: #fff;
    background-image: linear-gradient(90deg, #166ef900 0%, #2077ff 56%, #166ef900 100%);
    border-radius: 50%;
    margin-right: 16px;
    .el-icon-video-pause,
    .el-icon-video-play {
      cursor: pointer;
      font-size: 22px;
    }
  }
  .range-timeline-bar__wrapper {
    width: 903px;
    height: 30px;
    opacity: 0.7;
    background-image: linear-gradient(90deg, #166ef900 0%, #2077ff 56%, #166ef900 100%);
    margin-left: 35px;
    margin-right: 20px;
    position: relative;
    .el-slider {
      transform: translateY(-50%);
      top: 50%;
      position: absolute;
      width: 100%;
      .el-slider__runway {
        margin: 0;
        background-color: transparent;
        .el-slider__bar {
          background-color: #ffda0000;
          background-image: linear-gradient(270deg, #ffda00 0%, #ffda0000 100%);
        }
        .el-slider__button-wrapper {
          .el-slider__button {
            border: 0;
            background-color: #ffda00;
            box-shadow: 0 0 10px 0 #ffdb00;
          }
        }
        .el-slider__stop {
          height: 8px;
          width: 2px;
          background-color: #12b5ff;
          &.el-slider__marks-stop {
            height: 12px;
          }
        }
        .el-slider__marks {
          .el-slider__marks-text {
            margin-top: -19px;
            color: #fff;
            white-space: nowrap;
          }
        }
      }
      .range-timeline-bar__scale {
        width: 100%;
        margin-top: 2px;
        height: 8px;
        background-image: $repeatingLinearGrd1, $repeatingLinearGrd2;
        background-position: 0 0, 0 2px;
        background-size: 100% 100%, 100% 100%;
        background-repeat: no-repeat;
      }
    }
    .ant-slider {
      margin: 0;
      padding: 0;
      transform: translateY(-50%);
      top: 50%;
      position: absolute;
      width: 100%;
      .ant-slider-rail {
        background-color: transparent;
      }
      .ant-slider-track {
        height: 6px;
        background-color: #ffda0000;
        background-image: linear-gradient(270deg, #ffda00 0%, #ffda0022 100%);
      }
      .ant-slider-step {
        .ant-slider-dot {
          height: 8px;
          width: 2px;
          background-color: #12b5ff;
          border: 0;
          top: 0;
          margin-left: 0;
          &.ant-slider-dot-active {
            display: none;
          }
        }
      }
      .ant-slider-handle {
        height: 16px;
        width: 16px;
        border: 0;
        background-color: #ffda00;
        box-shadow: 0 0 10px 0 #ffdb00;
        z-index: 1;
      }
      .ant-slider-mark {
        top: -28px;
        z-index: -1;
        .ant-slider-mark-text {
          color: #fff;
          font-weight: bold;
          font-size: 18px;
          .mark-label {
            .time {
              font-size: 16px;
              font-weight: normal;
              margin-top: 14px;
            }
          }
        }
      }
    }
  }
  .next-button {
    width: 31px;
    height: 26px;
    background-image: linear-gradient(to bottom, #52a5fd 0%, #0e4db3 100%);
    border-radius: 16px;
    position: relative;
    cursor: pointer;
    box-shadow: 2px 2px 10px #313131;
    &:before {
      box-sizing: border-box;
      content: '';
      height: 14px;
      width: 8px;
      top: 50%;
      left: 8px;
      transform: translateY(-50%);
      border-left: #fff solid 8px;
      border-top: transparent solid 7px;
      border-bottom: transparent solid 7px;
      position: absolute;
    }
    &:after {
      box-sizing: border-box;
      content: '';
      height: 14px;
      width: 8px;
      top: 50%;
      right: 7px;
      transform: translateY(-50%);
      border-left: #fff solid 8px;
      border-top: transparent solid 7px;
      border-bottom: transparent solid 7px;
      position: absolute;
    }
    &[disabled] {
      &:before,
      &:after {
        border-left: #aaa solid 8px;
        border-top: transparent solid 7px;
        border-bottom: transparent solid 7px;
        cursor: not-allowed;
      }
    }
  }
  .prev-button {
    @extend .next-button;
    transform: scaleX(-1);
  }
  &.is-big-screen {
    transform: translateX(-50%) scaleX(0.5);
  }
}
</style>