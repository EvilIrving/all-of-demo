<!--
 * @Date: 2022-05-11 10:46:51
 * @Author: wqn
 * @Description: 水位图
 * @LastEditors: wqn
 * @LastEditTime: 2023-01-10 14:56:32
 * @FilePath: \lucheng\src\views\new_screen\projectScheduling\controlFloodDispatch\waterChart.vue
-->
<template>
  <div class="water-level-chart-wrapper">
    <el-select class="reach-select" v-model="reachSelectValue" placeholder="请选择">
      <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
    </el-select>
    <div class="water-level-chart-inner" ref="waterChart"></div>
    <div class="water-level-info">
      <div class="title">
        <div class="hight-water-level">最高水位</div>
        <div class="discovery-time">发现时间</div>
      </div>
      <ul class="info">
        <!-- <li v-for="(item,index) in highWaterSurfaceData" :style="{left: distanceData[index]/distanceData[distanceData.length - 1]*100 + '%'}" :key="`${item.hightWaterLevel}-${index}`"> -->
        <li v-for="(item, index) in highWaterSurfaceData" :style="{ left: (index / (highWaterSurfaceData.length - 1)) * 100 + '%' }" :key="`${item.hightWaterLevel}-${index}`">
          <div class="hight-water-level">{{ item.hightWaterLevel || '--' }}</div>
          <div class="discovery-time">
            <div>
              {{ item.discoveryTime ? item.discoveryTime.split(' ')[0].substring(5).replace(/-/g, '.') : '--' }}
            </div>
            <div>
              {{ item.discoveryTime ? item.discoveryTime.replace(/-/g, '.').split(' ')[1].substring(0, 5) : '--' }}
            </div>
          </div>
        </li>
      </ul>
    </div>
    <div class="timeline-box">
      <div class="start date">{{ startDateTime }}</div>
      <div class="timeline-slider">
        <el-slider v-model="dateValue" :min="dateMin" :max="dateMax" :format-tooltip="formatTooltip"></el-slider>
      </div>
      <div class="end date">{{ endDateTime }}</div>
    </div>
    <div class="control-box">
      <div class="play-speed">
        播放速度：
        <span class="speed-item" :style="{ backgroundColor: speed === 0.5 ? '#026FE0' : 'transparent' }" @click="speed = 0.5">×0.5</span>
        <span class="speed-item" :style="{ backgroundColor: speed === 1 ? '#026FE0' : 'transparent' }" @click="speed = 1">×1</span>
        <span class="speed-item" :style="{ backgroundColor: speed === 2 ? '#026FE0' : 'transparent' }" @click="speed = 2">×2</span>
        <span class="speed-item" :style="{ backgroundColor: speed === 4 ? '#026FE0' : 'transparent' }" @click="speed = 4">×4</span>
      </div>
      <div class="start-pause-btn" @click="isStart = !isStart">
        <i v-show="!isStart" class="start-icon"></i>
        <i v-show="isStart" class="pause-icon"></i>
      </div>
      <el-button class="reduction-btn" type="primary" round icon="el-icon-refresh-right" size="mini" @click="reduction">还原</el-button>
      <el-button class="set-time-btn" type="primary" round icon="el-icon-time" size="mini" @click="setTime">设置时间</el-button>
      <el-date-picker class="date-picker" ref="datePicker" v-model="datePickerValue" type="datetimerange" @change="changeDatePickerValue"></el-date-picker>
    </div>
    <!-- <div class="footer-icon"></div> -->
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { sectionInformationHydrograph } from '@/api/ourPujiang';

export default {
  name: 'controlFloodDispatchWaterChart',
  props: {},
  components: {},
  data() {
    return {
      reachSelectValue: '1',
      options: [
        {
          value: '1',
          label: '戍浦江河段'
        }
      ],
      waterChart: null,
      colors: ['#94FEFE', '#56FEFE', '#FD7C00', '#F11616', '#1CFFF5', '#47F5A7'],
      xAxisData: [
        '常山',
        '衢州',
        '龙游',
        '洋港',
        '兰溪',
        '三河',
        '富春江',
        '桐庐',
        '富阳',
        '闻家堰',
        '闸口',
        '三堡',
        '七堡',
        '仓前'
      ],
      distanceData: [0, 39, 76, 93, 114, 135, 184, 196, 235, 259, 270, 283, 290, 306],
      groundData: [76.31, 54.36, 30.68, 27.17, 17.48, 11.62, 2.63, -10.87, -9.23, -7.21, -7.88, -5.85, -5.58, -3.21],
      waterSurfaceData: [78.05, 59.65, 40.07, 34.1, 23.49, 23.28, 22.94, 4.99, 4.77, 4.78, 4.72, 4.53, 4.42, 3.92],
      alertData: [82.0, 61.2, 42.7, 35.0, 28.0, 25.5, 25.0, 8.0, 8.0, 7.3, 7.0, 7.0, 6.7, 6.5],
      ensureData: [84, 63.7, 44.5, 36.5, 31.0, 26.5, 26.0, 9.0, 9.0, 8.0, 7.8, 7.5, 7.5, 7.0],
      leftBankData: [90.01, 66.39, 46.19, 40.21, 35.05, 26.93, 34.5, 10.35, 10.35, 10.35, 10.47, 9.35, 9.35, 9.83],
      rightBankData: [87.53, 66.04, 46.39, 40.87, 34.0, 28.7, 30.26, 15.35, 15.35, 10.52, 10.35, 6.63, 6.63, 7.39],
      isIncreaseDecrease: [1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1],
      highWaterSurfaceData: [
        // {
        //   hightWaterLevel: 78.05,
        //   discoveryTime: '05.16 03:00'
        // },
        // {
        //   hightWaterLevel: 59.67,
        //   discoveryTime: '05.18 06:45'
        // },
        // {
        //   hightWaterLevel: 40.13,
        //   discoveryTime: '05.16 12:00'
        // },
        // {
        //   hightWaterLevel: 34.15,
        //   discoveryTime: '05.17 09:30'
        // },
        // {
        //   hightWaterLevel: 23.35,
        //   discoveryTime: '05.15 19:00'
        // },
        // {
        //   hightWaterLevel: 23.24,
        //   discoveryTime: '05.16 08:00'
        // },
        // {
        //   hightWaterLevel: 23.12,
        //   discoveryTime: '05.17 06:35'
        // },
        // {
        //   hightWaterLevel: 5.81,
        //   discoveryTime: '05.17 19:55'
        // },
        // {
        //   hightWaterLevel: 5.37,
        //   discoveryTime: '05.17 18:35'
        // },
        // {
        //   hightWaterLevel: 5.57,
        //   discoveryTime: '05.18 04:55'
        // },
        // {
        //   hightWaterLevel: 5.75,
        //   discoveryTime: '05.18 04:30'
        // },
        // {
        //   hightWaterLevel: 5.78,
        //   discoveryTime: '05.18 03:55'
        // },
        // {
        //   hightWaterLevel: 5.83,
        //   discoveryTime: '05.18 03:30'
        // },
        // {
        //   hightWaterLevel: 6.01,
        //   discoveryTime: '05.18 03:10'
        // }
      ],
      dateMin: 0,
      dateValue: 24,
      dateMax: 24,
      datePickerValue: '',
      startDateTime: null,
      endDateTime: null,
      speed: 1,
      isStart: false,
      timer: null
    };
  },
  computed: {},
  watch: {
    isStart(val) {
      clearInterval(this.timer);
      if (val) {
        this.dateValue === this.dateMax ? (this.dateValue = 0) : this.dateValue++;
        this.timer = setInterval(() => {
          this.dateValue === this.dateMax ? (this.dateValue = 0) : this.dateValue++;
        }, 3000 / this.speed);
      }
    },
    speed(val) {
      clearInterval(this.timer);
      this.timer = setInterval(() => {
        this.dateValue === this.dateMax ? (this.dateValue = 0) : this.dateValue++;
      }, 3000 / val);
    },
    dateValue(val) {
      const time = this.getDateTimeStr(new Date(Date.parse(this.startDateTime) + val * 60 * 60 * 1000));
      this.getEChartsData(time.replace(/\./g, '-') + ':00');
    }
  },
  mounted() {
    this.waterChart = echarts.init(this.$refs.waterChart);
    // 自适应窗口大小变化
    window.onresize = () => {
      this.waterChart.resize();
    };
    const d = new Date();
    this.endDateTime = this.getDateTimeStr(d);
    this.startDateTime = this.getDateTimeStr(new Date(Date.parse(this.endDateTime) - this.dateMax * 60 * 60 * 1000));
    this.getEChartsData(this.endDateTime.replace(/\./g, '-') + ':00');
  },
  destroyed() {
    clearInterval(this.timer);
  },
  methods: {
    /**
     * @description 获取水位图数据
     */
    getEChartsData(time) {
      sectionInformationHydrograph({
        time: time,
        startTime: this.startDateTime.replace(/\./g, '-') + ':00',
        endTime: this.endDateTime.replace(/\./g, '-') + ':00'
      }).then((res) => {
        this.xAxisData = [];
        this.distanceData = [];
        this.groundData = [];
        this.waterSurfaceData = [];
        this.alertData = [];
        this.ensureData = [];
        this.leftBankData = [];
        this.rightBankData = [];
        this.isIncreaseDecrease = [];
        this.highWaterSurfaceData = [];
        if (res.code == 0) {
          const data = res.data || [];
          data.forEach((e) => {
            this.xAxisData.push(e.name);
            // this.distanceData.push(e.);
            this.groundData.push(e.lowest_elevation);
            this.waterSurfaceData.push(e.waterLevel);
            this.alertData.push(e.warning_level);
            this.ensureData.push(e.guarantee_level);
            this.leftBankData.push(e.left_elevation);
            this.rightBankData.push(e.right_elevation);
            this.isIncreaseDecrease.push(
              e.waterLevel && e.waterLevel2
                ? e.waterLevel > e.waterLevel2
                  ? 1
                  : e.waterLevel === e.waterLevel2
                  ? 0
                  : -1
                : 0
            );
            this.highWaterSurfaceData.push({
              hightWaterLevel: e.highest_level,
              discoveryTime: e.appear_time
            });
          });
          console.log(this.highWaterSurfaceData);
        }
        this.updateECharts();
      });
    },
    /**
     * @description 绘制水位图
     */
    updateECharts() {
      // 配置图表
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'none',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          data: [
            {
              name: '实时水位',
              itemStyle: {
                color: '#000',
                borderColor: this.colors[1]
              },
              lineStyle: {
                color: this.colors[1],
                opacity: 1
              }
            },
            {
              name: '警戒水位',
              itemStyle: {
                color: '#000',
                borderColor: this.colors[2]
              },
              lineStyle: {
                color: this.colors[2],
                opacity: 1
              }
            },
            {
              name: '保证水位',
              itemStyle: {
                color: '#000',
                borderColor: this.colors[3]
              },
              lineStyle: {
                color: this.colors[3],
                opacity: 1
              }
            },
            {
              name: '左岸高程',
              itemStyle: {
                color: this.colors[4],
                borderColor: this.colors[4]
              },
              lineStyle: {
                color: this.colors[4],
                opacity: 1
              }
            },
            {
              name: '右岸高程',
              itemStyle: {
                color: this.colors[5],
                borderColor: this.colors[5]
              },
              lineStyle: {
                color: this.colors[5],
                opacity: 1
              }
            }
          ],
          right: 0,
          padding: [16, 18, 0, 0],
          itemGap: 13,
          itemWidth: 34,
          itemHeight: 16,
          itemStyle: {
            borderWidth: 1
          },
          lineStyle: {
            width: 1
          },
          textStyle: {
            color: '#FFFFFF',
            fontWeight: 500,
            fontFamily: 'PingFangSC-Medium, PingFang SC',
            fontSize: 14,
            lineHeight: 20
          }
        },
        grid: {
          left: 79,
          right: 32,
          bottom: 190,
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            // data: [...new Array(this.distanceData[this.distanceData.length - 1] + 1).keys()].map((e) => {
            //   if (this.distanceData.includes(e)) {
            //     return this.xAxisData[this.distanceData.indexOf(e)];
            //   } else {
            //     return e;
            //   }
            // }),
            data: this.xAxisData,
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              interval: (index, value) => this.xAxisData.includes(value),
              margin: 22,
              color: '#FFFFFF',
              fontWeight: 600,
              fontFamily: 'PingFangSC-Semibold, PingFang SC',
              fontSize: 16,
              lineHeight: 22
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: '#6b7077',
                type: 'dashed'
              }
            },
            triggerEvent: true
          }
        ],
        yAxis: [
          {
            type: 'value',
            min: this.groundData.sort((a, b) => a - b)[0],
            // max: this.highWaterSurfaceData.sort((a, b) => b.hightWaterLevel - a.hightWaterLevel)[0].hightWaterLevel,
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              show: false
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: '#6b7077',
                type: 'dashed'
              }
            }
          }
        ],
        series: [
          {
            name: '河底高程',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: this.groundData.map((e, i) => [this.xAxisData[i], e]),
            lineStyle: {
              color: this.colors[0],
              opacity: 0
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: this.colors[0]
                  },
                  {
                    offset: 0.75,
                    color: this.colors[0]
                  },
                  {
                    offset: 1,
                    color: '#7d9aff'
                  }
                ]
              },
              origin: 'start',
              opacity: 1
            }
          },
          {
            name: '实时水位',
            type: 'line',
            smooth: true,
            symbol: 'triangle',
            symbolSize: (value, params) => (this.isIncreaseDecrease[params.dataIndex] === 0 ? 0 : [12, 21]),
            // symbolOffset: [0, -6],
            symbolRotate: (value, params) => (this.isIncreaseDecrease[params.dataIndex] > 0 ? 0 : 180),
            data: this.waterSurfaceData.map((e, i) => [this.xAxisData[i], e]),
            label: {
              show: true,
              distance: 5,
              color: '#FFFFFF',
              fontWeight: 400,
              fontFamily: 'PingFangSC-Regular, PingFang SC',
              fontSize: 14,
              lineHeight: 25,
              width: 42,
              height: 30,
              borderRadius: 8,
              backgroundColor: 'rgba(71, 245, 167, 0.8)'
            },
            itemStyle: {
              color: (params) => (this.isIncreaseDecrease[params.dataIndex] > 0 ? '#FF0000' : '#39C68B')
            },
            lineStyle: {
              color: this.colors[1],
              opacity: 0
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: this.colors[1]
                  },
                  {
                    offset: 0.75,
                    color: this.colors[1]
                  },
                  {
                    offset: 1,
                    color: 'rgba(6, 57, 255, 0.52)'
                  }
                ]
              },
              origin: 'start',
              opacity: 0.5
            }
          },
          {
            name: '警戒水位',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: this.alertData.map((e, i) => [this.xAxisData[i], e]),
            lineStyle: {
              color: this.colors[2]
            }
          },
          {
            name: '保证水位',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: this.ensureData.map((e, i) => [this.xAxisData[i], e]),
            lineStyle: {
              color: this.colors[3]
            }
          },
          {
            name: '左岸高程',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: this.leftBankData.map((e, i) => [this.xAxisData[i], e]),
            lineStyle: {
              color: this.colors[4],
              type: 'dotted'
            }
          },
          {
            name: '右岸高程',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: this.rightBankData.map((e, i) => [this.xAxisData[i], e]),
            lineStyle: {
              color: this.colors[5],
              type: 'dotted'
            }
          }
        ]
      };
      this.waterChart.setOption(option);
    },
    /**
     * @description 还原按钮点击触发
     */
    reduction() {
      this.dateValue = 0;
      this.isStart = false;
    },
    /**
     * @description 获取MM-dd HH:00格式的时间字符串
     */
    getDateTimeStr(d) {
      return `${d.getFullYear()}.${handleSingleDigit(d.getMonth() + 1)}.${handleSingleDigit(
        d.getDate()
      )} ${handleSingleDigit(d.getHours())}:00`;
      function handleSingleDigit(value) {
        return value < 10 ? '0' + value : value;
      }
    },
    /**
     * @description 设置时间按钮点击事件
     */
    setTime() {
      this.$refs.datePicker.focus();
    },
    /**
     * @description 设置时间改变时触发
     */
    changeDatePickerValue(val) {
      if (val) {
        this.startDateTime = this.getDateTimeStr(val[0]);
        this.endDateTime = this.getDateTimeStr(val[1]);
        this.dateMax = (Date.parse(this.endDateTime) - Date.parse(this.startDateTime)) / (60 * 60 * 1000);
        this.dateValue = this.dateMax;
        this.getEChartsData(this.endDateTime.replace(/\./g, '-') + ':00');
      }
    },
    /**
     * @description 格式化滑块的Tooltip
     */
    formatTooltip(val) {
      return this.getDateTimeStr(new Date(Date.parse(this.startDateTime) + val * 60 * 60 * 1000));
    }
  }
};
</script>

<style scoped lang="scss">
.water-level-chart-wrapper {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: calc(100% - 20px);
  padding: 0 14px;
  background: url(~assets/images/ourPujiang/our-pujiang-icon.png) no-repeat 100% 100%;
  overflow: hidden;
  .reach-select {
    position: absolute;
    width: 152px;
    height: 32px;
    left: 30px;
    top: 23px;
    background-color: rgba(0, 186, 255, 0.1);
    border: 1px solid rgba(0, 186, 255, 0.54);
    border-radius: 2px;
    z-index: 1;
    ::v-deep .el-input.el-input--suffix {
      height: 100%;
      .el-input__inner {
        height: 100%;
      }
      .el-input__suffix {
        .el-input__suffix-inner {
          .el-select__caret.el-input__icon.el-icon-arrow-up {
            line-height: 28px;
          }
        }
      }
    }
  }
  .water-level-chart-inner {
    width: calc(100% - 28px);
    height: calc(100% - 122px);
    margin: 14px 14px 78px;
    background-color: rgba(0, 1, 3, 0.6);
  }
  .water-level-info {
    position: absolute;
    display: flex;
    box-sizing: border-box;
    width: 100%;
    height: 96px;
    bottom: 143px;
    padding-right: 50px;
    .title {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
      width: 75px;
      height: 100%;
      .hight-water-level,
      .discovery-time {
        font-size: 16px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #ffffff;
        line-height: 22px;
      }
      .hight-water-level {
        height: 30px;
        line-height: 30px;
      }
      .discovery-time {
        height: 44px;
        line-height: 44px;
      }
    }
    .info {
      position: relative;
      display: flex;
      // justify-content: space-between;
      // align-items: center;
      width: calc(100% - 125px);
      height: 100%;
      li {
        position: absolute;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: center;
        height: 100%;
        .hight-water-level,
        .discovery-time {
          width: 50px;
        }
        .hight-water-level {
          height: 30px;
          line-height: 30px;
          text-align: center;
          font-size: 14px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #ffffff;
          background-color: rgba(71, 245, 167, 0.8);
          border-radius: 8px;
        }
        .discovery-time {
          height: 44px;
          > div {
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            align-items: center;
            height: 22px;
            line-height: 22px;
            font-size: 16px;
            font-family: PingFangSC-Semibold, PingFang SC;
            font-weight: 600;
            color: #ffffff;
          }
        }
      }
    }
  }
  .timeline-box {
    position: absolute;
    display: flex;
    box-sizing: border-box;
    width: 100%;
    height: 22px;
    bottom: 93px;
    padding: 0 50px 0 68px;
    .date {
      width: 150px;
      height: 22px;
      font-size: 16px;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #ffffff;
      line-height: 22px;
      &.end {
        text-align: right;
      }
    }
    .timeline-slider {
      width: calc(100% - 300px);
      ::v-deep .el-slider__runway {
        margin: 8.5px 0;
        background-color: rgba(252, 238, 39, 0.33);
        .el-slider__bar {
          background-color: transparent;
          background-image: linear-gradient(270deg, #ffda00 0%, rgba(255, 218, 0, 0) 100%);
        }
        .el-slider__button-wrapper {
          .el-slider__button {
            border: none;
            background-color: #ffda00;
            box-shadow: 0px 0px 10px 0px #ffdb00;
          }
        }
      }
    }
  }
  .control-box {
    position: absolute;
    display: flex;
    justify-content: center;
    align-items: center;
    width: calc(100% - 28px);
    height: 38px;
    bottom: 48px;
    .play-speed {
      font-size: 16px;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #ffffff;
      line-height: 38px;
      margin-right: 35px;
      .speed-item {
        display: inline-block;
        box-sizing: border-box;
        border: 1px solid #026fe0;
        padding: 0 16px;
        height: 30px;
        line-height: 30px;
        cursor: pointer;
        &:first-child {
          border-radius: 4px 0px 0px 4px;
        }
        &:last-child {
          border-radius: 0px 4px 4px 0px;
        }
        &:not(:first-child) {
          border-left: none;
        }
      }
    }
    .start-pause-btn {
      position: relative;
      width: 38px;
      height: 38px;
      background-color: #026fe0;
      border-radius: 50%;
      cursor: pointer;
      i {
        position: absolute;
        display: inline-block;
        left: 0;
        top: 0;
        right: 0;
        bottom: 0;
        margin: auto;
      }
      .start-icon {
        width: 0;
        height: 0;
        border-top: 7px solid transparent;
        border-bottom: 7px solid transparent;
        border-left: 12px solid #ffffff;
        transform: translateX(2px);
      }
      .pause-icon {
        width: 12px;
        height: 14px;
        border-top: none;
        border-bottom: none;
        border-left: 4px solid #ffffff;
        border-right: 4px solid #ffffff;
      }
    }
    .reduction-btn {
      margin-left: 35px;
    }
    .set-time-btn {
      margin-left: 25px;
    }
    ::v-deep .el-button--primary {
      background-color: #026fe0;
      border-color: #026fe0;
    }
    .date-picker {
      position: absolute;
      opacity: 0;
      width: 0;
      height: 0;
      padding: 0;
      left: 50%;
      transform: translate(215px, -9px);
    }
  }
  .footer-icon {
    position: absolute;
    width: 100%;
    height: 24px;
    bottom: 0;
    left: 0;
    background: url(~assets/images/ourPujiang/our-bottom-icon.png) no-repeat 100% 100%;
  }
}
</style>
