<!--
 * @Author: wqn
 * @Date: 2022-09-05 16:41:28
 * @LastEditors: wqn
 * @LastEditTime: 2023-01-03 14:35:49
 * @Description: 预测
-->
<template>
  <div class='chart_forecast'>
    <el-select v-model="sectionSelected" multiple collapse-tags placeholder="断面选择" @change="sectionSelectedChanged">
      <el-option v-for="item in sectionList" :key="item.id + item.name" :label="item.name" :value="item.id"></el-option>
    </el-select>
    <div class="forecast" ref="forecast"></div>
    <ul class="choose_box">
      <li class="water_level" :class="{disabled: active !== '水位曲线'}" @click="active = '水位曲线'">水位曲线</li>
      <li class="flow_line" :class="{disabled: active !== '流量曲线'}" @click="active = '流量曲线'">流量曲线</li>
    </ul>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { chooseSection, forecastSection } from '@/api/new_screen_api';
export default {
  name: 'controlFloodDispatchChartForecast',
  props: {},
  components: {},
  data() {
    return {
      sectionSelected: [],
      sectionList: [],
      chart: null,
      xAxisData: [],
      sectionWaterLevelData: {},
      sectionFlowData: {},
      active: '水位曲线'
    };
  },
  computed: {},
  watch: {
    active() {
      this.drawSection();
    }
  },
  created() {},
  mounted() {
    this.getSectionList();
  },
  destroyed() {},
  methods: {
    /**
     * @description 获取断面下拉框数据
     */
    getSectionList() {
      chooseSection().then((res) => {
        if (res.code === 0) {
          this.sectionList = res.data || [];
          this.sectionSelected = this.sectionList.length > 0 ? [this.sectionList[0].id || ''] : [];
          this.getSectionData();
        }
      });
    },
    /**
     * @description 断面选择发生变化
     */
    sectionSelectedChanged() {
      this.getSectionData();
    },
    /**
     * @description 获取断面折线图数据
     */
    async getSectionData() {
      await forecastSection({
        sectionIds: this.sectionSelected.join(',')
      }).then((res) => {
        if (res.code === 0) {
          this.xAxisData = Object.keys(res.data || {});
          let sectionNameArr = this.sectionList.filter((e) => this.sectionSelected.includes(e.id)).map((e) => e.name);
          this.sectionWaterLevelData = {};
          this.sectionFlowData = {};
          sectionNameArr.forEach((e) => {
            this.sectionWaterLevelData[e] = [];
            this.sectionFlowData[e] = [];
          });
          this.xAxisData.forEach((e) => {
            let dataArr = res.data[e] || [];
            dataArr.forEach((m) => {
              sectionNameArr.forEach((n) => {
                if (m.name === n) {
                  this.sectionWaterLevelData[n].push(m.forestWaterLevel);
                  this.sectionFlowData[n].push(m.forestFlow);
                }
              });
            });
          });
        }
      });
      echarts.init(this.$refs.forecast).dispose();
      this.drawSection();
    },
    /**
     * @description 绘制断面折线图
     */
    drawSection() {
      this.chart = echarts.init(this.$refs.forecast);
      let option = {
        grid: {
          left: 0,
          top: 20,
          right: 45,
          bottom: 0,
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.xAxisData.map((e) =>
            e
              .slice(5, -3)
              .split(' ')
              .join('\n')
          ),
          axisLine: {
            lineStyle: {
              color: '#326876'
            }
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#537279',
            fontWeight: 400,
            fontSize: 13
          }
        },
        yAxis: {
          name: this.active === '水位曲线' ? 'm' : 'm³/s',
          nameGap: 5,
          nameTextStyle: {
            color: '#537279',
            fontWeight: 400,
            fontSize: 13
          },
          axisLabel: {
            color: '#537279',
            fontWeight: 400,
            fontSize: 13
          },
          splitLine: {
            show: false
          }
        },
        series: []
      };
      if (this.active === '水位曲线') {
        let sectionName = Object.keys(this.sectionWaterLevelData);
        sectionName.forEach((e) => {
          option.series.push({
            type: 'line',
            name: e,
            symbol: 'none',
            data: this.sectionWaterLevelData[e],
            endLabel: {
              show: true,
              offset: [0, 0],
              formatter: '{a}',
              color: '#FFFFFF',
              fontWeight: 400,
              fontSize: 14,
              textBorderWidth: 0
            },
            lineStyle: {
              color: '#00BAFF'
            },
            smooth: true
          });
        });
      } else {
        let sectionName_ = Object.keys(this.sectionFlowData);
        sectionName_.forEach((e) => {
          option.series.push({
            type: 'line',
            name: e,
            symbol: 'none',
            data: this.sectionFlowData[e],
            endLabel: {
              show: true,
              offset: [0, 0],
              formatter: '{a}',
              color: '#FFFFFF',
              fontWeight: 400,
              fontSize: 14,
              textBorderWidth: 0
            },
            lineStyle: {
              color: '#47f5a7'
            },
            smooth: true
          });
        });
      }
      this.chart.setOption(option);
    }
  }
};
</script>
<style lang='scss' scoped>
.chart_forecast {
  position: relative;
  width: 100%;
  height: 100%;
  ::v-deep .el-select {
    .el-select__tags {
      flex-wrap: nowrap;
      .el-tag.el-tag--info {
        padding: 0 2px;
        margin: 0 0 0 2px;
        background: linear-gradient(180deg, #36c0e4 0%, #0639ff 100%);
        border: none;
        color: #ffffff;
      }
      .el-tag--small {
        height: 18px;
        line-height: 18px;
      }
      .el-tag__close.el-icon-close {
        right: 0;
        background-color: #fff;
        color: #0639ff;
      }
    }
    .el-input {
      .el-input__inner {
        width: 105px;
        height: 22px;
        background: rgba(0, 186, 255, 0.1);
        border-radius: 2px;
        border: 1px solid rgba(0, 186, 255, 0.54);
        text-align: left;
        padding-left: 5px;
        padding-right: 22px;
      }
      .el-input__suffix {
        right: 0;
        .el-input__suffix-inner {
          .el-input__icon {
            line-height: 20px;
            &::before {
              content: '▲';
              font-size: 12px;
            }
          }
        }
      }
    }
  }
  .forecast {
    margin-top: 8px;
    height: calc(100% - 30px);
  }
  ul.choose_box {
    position: absolute;
    display: flex;
    top: 3px;
    right: 5px;
    li {
      display: flex;
      align-items: center;
      height: 18px;
      font-size: 13px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #ffffff;
      line-height: 18px;
      cursor: pointer;
      color: #ffffff;
      &::before {
        content: '';
        display: block;
        width: 20px;
        height: 3px;
        margin-right: 5px;
      }
      &.water_level::before {
        background-color: #00baff;
      }
      &.flow_line::before {
        background-color: #47f5a7;
      }
      &.disabled {
        color: #415161;
        &.water_level::before {
          background-color: #415161;
        }
        &.flow_line::before {
          background-color: #415161;
        }
      }
      + li {
        margin-left: 10px;
      }
    }
  }
}
</style>