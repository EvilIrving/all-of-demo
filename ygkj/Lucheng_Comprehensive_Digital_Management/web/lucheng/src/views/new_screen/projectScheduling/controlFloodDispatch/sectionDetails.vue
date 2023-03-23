<!--
 * @Author: wqn
 * @Date: 2023-01-03 14:53:15
 * @LastEditors: wqn
 * @LastEditTime: 2023-01-10 11:48:55
 * @Description: 断面详情
-->
<template>
  <div class='section_details'>
    <ul class="tabbar">
      <li :class="{active: activeTab === '基本信息'}" @click="activeTab = '基本信息'">基本信息</li>
      <li :class="{active: activeTab === '水位过程线'}" @click="activeTab = '水位过程线'">水位过程线</li>
      <li :class="{active: activeTab === '流量过程线'}" @click="activeTab = '流量过程线'">流量过程线</li>
    </ul>
    <div class="details_box" v-if="activeTab === '基本信息'" key="基本信息">
      <div class="base_info_wrapper">
        <div class="base_data">
          <div class="item"><label>断面名称：</label><span>{{sectionName}}</span></div>
          <div class="item"><label>左岸高程（m）：</label><span>{{leftBankElevation}}</span></div>
          <div class="item"><label>右岸高程（m）：</label><span>{{rightBankElevation}}</span></div>
          <div class="item"><label>预报最高水位（m）：</label><span>{{forecastMaxWaterLevel}}</span></div>
          <div class="item"><label>历史最高水位（m）：</label><span>{{historyMaxWaterLevel}}</span></div>
          <div class="item"><label>出现时间：</label><span>{{appearTime}}</span></div>
        </div>
        <div class="base_chart" ref="baseChart"></div>
      </div>
    </div>
    <div class="details_box" v-else-if="activeTab === '水位过程线'" key="水位过程线">
      <div class="water_level_wrapper">
        <div class="chart_wrapper">
          <subtitle>水位过程线</subtitle>
          <div class="chart_box" ref="waterLevelChart"></div>
          <div class="data_box">
            <label>预报最高水位（m）：</label><span>{{forecastMaxWaterLevel2}}</span>
            <label>出现时间：</label><span>{{appearTime2}}</span>
            <label>预报说明：</label><span>{{forecastInfo}}</span>
          </div>
        </div>
        <div class="table_wrapper">
          <subtitle>列表</subtitle>
          <div class="table_inner">
            <div class="table_header">
              <span>序号</span><span>时间</span><span>实时水位(m)</span><span>预报水位(m)</span>
            </div>
            <ul class="table_bodyer">
              <li v-for="(item, index) in waterLevelList" :key="'waterLevelTableRow'+index">
                <span>{{index+1}}</span><span>{{item.time}}</span><span>{{item.realtimeWaterlevel}}</span><span>{{item.forecastWaterlevel}}</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="details_box" v-else-if="activeTab === '流量过程线'" key="流量过程线">
      <div class="flow_wrapper">
        <div class="chart_wrapper">
          <subtitle>流量过程线</subtitle>
          <div class="chart_box" ref="flowChart"></div>
          <div class="data_box">
            <label>预报洪峰流量（m3/s）：</label><span>27.32</span>
            <label>出现时间：</label><span>08-17 17:00</span>
          </div>
        </div>
        <div class="table_wrapper">
          <subtitle>列表</subtitle>
          <div class="table_inner">
            <div class="table_header">
              <span>序号</span><span>时间</span><span>实时流量(m³/s)</span><span>预报流量(m³/s)</span>
            </div>
            <ul class="table_bodyer">
              <li v-for="(item, index) in flowList" :key="'flowTableRow'+index">
                <span>{{index+1}}</span><span>{{item.time}}</span><span>{{item.realtimeFlow}}</span><span>{{item.forecastFlow}}</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import subtitle from './dialogSubtitle.vue';
import * as echarts from 'echarts';
import { dmInfo, dmWaterLevel } from '@/api/bigScreenPart';
export default {
  name: 'sectionDetails',
  props: {
    id: { type: String, required: true }
  },
  components: { subtitle },
  data() {
    return {
      activeTab: '基本信息',
      sectionName: '', // 断面名称
      leftBankElevation: '', // 左岸高程
      rightBankElevation: '', // 右岸高程
      forecastMaxWaterLevel: '', // 预报最高水位
      historyMaxWaterLevel: '', // 历史最高水位
      appearTime: '', // 出现时间
      waterLevelLineData: [], // 基本信息中折线图数据
      guaranteeWaterLevel: null, // 保证水位
      warningWaterLevel: null, // 警戒水位
      baseChart: null,
      waterLevelList: [], // 水位数据列表
      waterLevelChart: null,
      forecastMaxWaterLevel2: '', // 预报最高水位
      appearTime2: '', // 出现时间
      forecastInfo: '', // 预报说明
      flowList: [], // 流量数据列表
      flowChart: null
    };
  },
  computed: {},
  watch: {
    id() {
      if (this.activeTab === '基本信息') {
        this.getBaseInfo();
      } else if (this.activeTab === '水位过程线') {
        this.getWaterLevel();
      } else if (this.activeTab === '流量过程线') {
        this.getFlow();
      }
    },
    activeTab(value) {
      if (value === '基本信息') {
        this.getBaseInfo();
      } else if (value === '水位过程线') {
        this.getWaterLevel();
      } else if (value === '流量过程线') {
        this.getFlow();
      }
    }
  },
  created() {
    if (this.activeTab === '基本信息') {
      this.getBaseInfo();
    } else if (this.activeTab === '水位过程线') {
      this.getWaterLevel();
    } else if (this.activeTab === '流量过程线') {
      this.getFlow();
    }
  },
  mounted() {},
  destroyed() {},
  methods: {
    getBaseInfo() {
      const now = new Date().getTime();
      dmInfo({
        id: this.id,
        startTime: new Date(now - 24 * 3600000).format('yyyy-MM-dd hh:mm:ss'),
        endTime: new Date(now + 24 * 3600000).format('yyyy-MM-dd hh:mm:ss')
      })
        .then((res) => {
          if (res.code === 0) {
            this.sectionName = res.data?.name ?? '-';
            this.leftBankElevation = res.data?.leftElevation ?? '-';
            this.rightBankElevation = res.data?.rightElevation ?? '-';
            this.forecastMaxWaterLevel = res.data?.forecastHighestLevel ?? '-';
            this.historyMaxWaterLevel = res.data?.highestLevel ?? '-';
            this.appearTime = res.data?.appearTime ?? '-';
            this.waterLevelLineData = res.data?.timeMap ?? [];
            this.guaranteeWaterLevel = res.data?.guaranteeLevel ?? null;
            this.warningWaterLevel = res.data?.warningLevel ?? null;
          }
        })
        .finally(() => {
          this.$nextTick(() => {
            this.drawBaseInfoEcharts();
          });
        });
    },
    drawBaseInfoEcharts() {
      this.baseChart = echarts.init(this.$refs.baseChart);
      const realtimeWaterLevel = [];
      const forecastWaterLevel = [];
      Object.keys(this.waterLevelLineData).forEach((e) => {
        if (this.waterLevelLineData[e].filter((ele) => ele.type === '实时水位').length > 0) {
          realtimeWaterLevel.push(this.waterLevelLineData[e].filter((ele) => ele.type === '实时水位')[0].waterLevel);
        } else {
          realtimeWaterLevel.push(null);
        }
        if (this.waterLevelLineData[e].filter((ele) => ele.type === '预报水位').length > 0) {
          forecastWaterLevel.push(this.waterLevelLineData[e].filter((ele) => ele.type === '预报水位')[0].waterLevel);
        } else {
          forecastWaterLevel.push(null);
        }
      });
      let option = {
        color: ['#00BAFF', '#1CFFF5', '#F11616', '#FD7C00'],
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: [
            {
              name: '实时水位',
              itemStyle: {
                color: '#000',
                borderColor: '#00BAFF'
              },
              lineStyle: {
                color: '#00BAFF',
                opacity: 1
              }
            },
            {
              name: '预报水位',
              itemStyle: {
                color: '#000',
                borderColor: '#1CFFF5'
              },
              lineStyle: {
                color: '#1CFFF5',
                opacity: 1
              }
            },
            {
              name: '保证水位',
              itemStyle: {
                color: '#000',
                borderColor: '#F11616'
              },
              lineStyle: {
                color: '#F11616',
                opacity: 1
              }
            },
            {
              name: '警戒水位',
              itemStyle: {
                color: '#000',
                borderColor: '#FD7C00'
              },
              lineStyle: {
                color: '#FD7C00',
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
          left: 20,
          right: 32,
          bottom: 0,
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: Object.keys(this.waterLevelLineData).map((e) => e.substring(5, 16).replace(' ', '\n')),
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            margin: 22,
            color: '#FFFFFF',
            fontWeight: 400,
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
        },
        yAxis: {
          name: 'm',
          nameGap: 30,
          nameTextStyle: {
            color: '#FFFFFF',
            fontWeight: 400,
            fontSize: 16
          },
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#FFFFFF',
            fontWeight: 400,
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
          }
        },
        series: [
          {
            name: '实时水位',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: realtimeWaterLevel,
            lineStyle: {
              color: '#00BAFF',
              type: 'dotted'
            }
          },
          {
            name: '预报水位',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: forecastWaterLevel,
            lineStyle: {
              color: '#1CFFF5',
              type: 'dotted'
            }
          },
          {
            name: '保证水位',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: Object.keys(this.waterLevelLineData).map(() => this.guaranteeWaterLevel),
            lineStyle: {
              color: '#F11616',
              type: 'dotted'
            }
          },
          {
            name: '警戒水位',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: Object.keys(this.waterLevelLineData).map(() => this.warningWaterLevel),
            lineStyle: {
              color: '#FD7C00',
              type: 'dotted'
            }
          }
        ]
      };
      this.baseChart.setOption(option);
    },
    getWaterLevel() {
      this.waterLevelList = [];
      const now = new Date().getTime();
      dmWaterLevel({
        id: this.id,
        startTime: new Date(now - 24 * 3600000).format('yyyy-MM-dd hh:mm:ss'),
        endTime: new Date(now + 24 * 3600000).format('yyyy-MM-dd hh:mm:ss')
      })
        .then((res) => {
          if (res.code === 0) {
            this.forecastMaxWaterLevel2 = res.data?.forecastHighestLevel ?? '-';
            this.appearTime2 = res.data?.appearTime ?? '-';
            this.forecastInfo = res.data?.description ?? '-';
            Object.keys(res.data?.timeMap ?? []).forEach((e) => {
              this.waterLevelList.push({
                time: e.substring(5, 16).replace(' ', '\n'),
                realtimeWaterlevel:
                  this.waterLevelLineData[e].filter((ele) => ele.type === '实时水位').length > 0
                    ? this.waterLevelLineData[e].filter((ele) => ele.type === '实时水位')[0].waterLevel
                    : null,
                forecastWaterlevel:
                  this.waterLevelLineData[e].filter((ele) => ele.type === '预报水位').length > 0
                    ? this.waterLevelLineData[e].filter((ele) => ele.type === '实时水位')[0].waterLevel
                    : null
              });
            });
          }
        })
        .finally(() => {
          this.$nextTick(() => {
            this.drawWaterLevelEcharts();
          });
        });
    },
    drawWaterLevelEcharts() {
      this.waterLevelChart = echarts.init(this.$refs.waterLevelChart);
      let option = {
        tooltip: { trigger: 'axis' },
        legend: {
          data: [
            {
              name: '实时水位',
              itemStyle: {
                color: '#23E3FB',
                borderColor: '#23E3FB'
              },
              lineStyle: {
                color: '#23E3FB',
                opacity: 1
              }
            },
            {
              name: '预报水位',
              itemStyle: {
                color: '#FCEE27',
                borderColor: '#FCEE27'
              },
              lineStyle: {
                color: '#FCEE27',
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
        grid: { left: 20, right: 32, bottom: 10, containLabel: true },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.waterLevelList.map((e) => e.time.replace(' ', '\n')),
          axisLine: {
            lineStyle: {
              color: '#0F3A82',
              width: 2
            }
          },
          axisTick: {
            inside: true,
            length: 15,
            lineStyle: {
              color: '#0F3A82',
              width: 2
            }
          },
          axisLabel: {
            margin: 10,
            color: '#FFFFFF',
            fontWeight: 400,
            fontFamily: 'PingFangSC-Semibold, PingFang SC',
            fontSize: 16,
            lineHeight: 22
          },
          splitLine: {
            show: false
          },
          triggerEvent: true
        },
        yAxis: {
          name: 'm',
          nameGap: 30,
          nameTextStyle: {
            color: '#FFFFFF',
            fontWeight: 400,
            fontSize: 16
          },
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#FFFFFF',
            fontWeight: 400,
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
          }
        },
        series: [
          {
            name: '实时水位',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: this.waterLevelList.map((e) => e.realtimeWaterlevel),
            lineStyle: {
              color: '#23E3FB'
            }
          },
          {
            name: '预报水位',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: this.waterLevelList.map((e) => e.forecastWaterlevel),
            lineStyle: {
              color: '#FCEE27'
            }
          }
        ]
      };
      this.waterLevelChart.setOption(option);
    },
    getFlow() {
      this.flowList = [
        { time: '08-17 00:00', realtimeFlow: 60, forecastFlow: 80 },
        { time: '08-17 01:00', realtimeFlow: 60, forecastFlow: 80 },
        { time: '08-17 02:00', realtimeFlow: 60, forecastFlow: 80 },
        { time: '08-17 03:00', realtimeFlow: 60, forecastFlow: 80 },
        { time: '08-17 04:00', realtimeFlow: 60, forecastFlow: 80 },
        { time: '08-17 05:00', realtimeFlow: 60, forecastFlow: 80 },
        { time: '08-17 06:00', realtimeFlow: 60, forecastFlow: 80 },
        { time: '08-17 07:00', realtimeFlow: 60, forecastFlow: 80 },
        { time: '08-17 08:00', realtimeFlow: 60, forecastFlow: 80 },
        { time: '08-17 09:00', realtimeFlow: 60, forecastFlow: 80 },
        { time: '08-17 10:00', realtimeFlow: 60, forecastFlow: 80 }
      ];
      this.$nextTick(() => {
        this.drawFlowEcharts();
      });
    },
    drawFlowEcharts() {
      this.flowChart = echarts.init(this.$refs.flowChart);
      let option = {
        tooltip: { trigger: 'axis' },
        legend: {
          data: [
            {
              name: '实时流量',
              itemStyle: {
                color: '#23E3FB',
                borderColor: '#23E3FB'
              },
              lineStyle: {
                color: '#23E3FB',
                opacity: 1
              }
            },
            {
              name: '预报流量',
              itemStyle: {
                color: '#FCEE27',
                borderColor: '#FCEE27'
              },
              lineStyle: {
                color: '#FCEE27',
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
        grid: { left: 20, right: 32, bottom: 10, containLabel: true },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.flowList.map((e) => e.time.replace(' ', '\n')),
          axisLine: {
            lineStyle: {
              color: '#0F3A82',
              width: 2
            }
          },
          axisTick: {
            inside: true,
            length: 15,
            lineStyle: {
              color: '#0F3A82',
              width: 2
            }
          },
          axisLabel: {
            margin: 10,
            color: '#FFFFFF',
            fontWeight: 400,
            fontFamily: 'PingFangSC-Semibold, PingFang SC',
            fontSize: 16,
            lineHeight: 22
          },
          splitLine: {
            show: false
          },
          triggerEvent: true
        },
        yAxis: {
          name: 'm',
          nameGap: 30,
          nameTextStyle: {
            color: '#FFFFFF',
            fontWeight: 400,
            fontSize: 16
          },
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#FFFFFF',
            fontWeight: 400,
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
          }
        },
        series: [
          {
            name: '实时流量',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: this.flowList.map((e) => e.realtimeFlow),
            lineStyle: {
              color: '#23E3FB'
            }
          },
          {
            name: '预报流量',
            type: 'line',
            smooth: true,
            symbol: 'none',
            data: this.flowList.map((e) => e.forecastFlow),
            lineStyle: {
              color: '#FCEE27'
            }
          }
        ]
      };
      this.flowChart.setOption(option);
    }
  }
};
</script>
<style lang='scss' scoped>
.section_details {
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  padding-top: 24px;
  ul.tabbar {
    display: flex;
    li {
      width: 114px;
      height: 32px;
      border: 1px solid rgba(255, 255, 255, 0.6);
      text-align: center;
      line-height: 30px;
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #ffffff;
      cursor: pointer;
      + li {
        margin-left: 16px;
      }
      &.active {
        background: rgba(72, 152, 251, 0.1);
        box-shadow: inset 0px 0px 5px 0px #00baff;
        border: 1px solid #00baff;
        color: #00baff;
      }
    }
  }
  .details_box {
    height: calc(100% - 32px);
    .base_info_wrapper {
      padding-top: 16px;
      height: 100%;
      .base_data {
        display: flex;
        justify-content: space-between;
        label {
          height: 20px;
          font-size: 14px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #00baff;
          line-height: 20px;
        }
        span {
          height: 20px;
          font-size: 14px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #ffffff;
          line-height: 20px;
        }
      }
      .base_chart {
        margin-top: 32px;
        height: calc(100% - 52px);
      }
    }
    .water_level_wrapper,
    .flow_wrapper {
      display: flex;
      justify-content: space-between;
      padding-top: 26px;
      height: 100%;
      .chart_wrapper {
        width: 804px;
        height: 100%;
        .chart_box {
          margin-top: 24px;
          height: calc(100% - 38px - 24px - 43px);
        }
        .data_box {
          width: 793px;
          height: 43px;
          margin-left: 11px;
          background-image: linear-gradient(180deg, rgba(53, 185, 227, 0.1) 0%, rgba(25, 60, 197, 0) 100%);
          box-shadow: inset 0px 4px 30px 0px rgba(35, 81, 239, 0.25);
          border: 2px solid #244778;
          line-height: 40px;
          font-family: PingFangSC-Regular, PingFang SC;
          label {
            font-size: 14px;
            font-weight: 400;
            color: #ffffff;
            margin-left: 20px;
          }
          span {
            font-size: 16px;
            font-weight: 500;
            color: #23e3fb;
          }
        }
      }
      .table_wrapper {
        width: 412px;
        height: 100%;
        .table_inner {
          margin-top: 24px;
          height: calc(100% - 38px - 24px);
          .table_header {
            display: flex;
            height: 44px;
            background-color: #239def;
          }
          ul.table_bodyer {
            overflow: auto;
            height: calc(100% - 44px);
            li {
              display: flex;
              height: 44px;
              + li {
                margin-top: 10px;
              }
              &:nth-child(even) {
                background-color: rgba(35, 157, 239, 0.23);
              }
            }
          }
          span {
            height: 22px;
            font-size: 16px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #ffffff;
            line-height: 44px;
            padding-left: 10px;
            &:nth-child(1) {
              width: 62px;
            }
            &:nth-child(2) {
              width: 118px;
            }
            &:nth-child(3) {
              white-space: nowrap;
              width: 116px;
            }
            &:nth-child(4) {
              white-space: nowrap;
              width: 110px;
            }
          }
        }
      }
    }
  }
}
</style>