<!--
 * @Author: wqn
 * @Date: 2022-09-05 16:41:28
 * @LastEditors: wqn
 * @LastEditTime: 2022-09-05 16:41:28
 * @Description: 预警
-->
<template>
  <div class='chart_early_warn'>
    <ul class="tabbar">
      <li :class="activeItem==='实时超警'?'active':''" @click="activeItem='实时超警'">实时超警 ({{realTimeOverAlertTotal}})</li>
      <li :class="activeItem==='预测超警'?'active':''" @click="activeItem='预测超警'">预测超警 ({{forecastDataOverAlertTotal}})</li>
    </ul>
    <ul class="table">
      <li class="header">
        <span>序号</span>
        <span>断面名称</span>
        <span>水位(m)</span>
        <span>警戒水位(m)</span>
        <span v-show="activeItem === '实时超警'">{{activeItem === '实时超警' ? '更新时间': '超警时长'}}</span>
      </li>
      <li class="row" v-for="(item, index) in tableData" :key="activeItem + index">
        <span>{{index + 1}}</span>
        <span>{{item.sectionName || '-'}}</span>
        <span>{{item.waterLevel || '-'}}</span>
        <span>{{item.warningLevel || '-'}}</span>
        <span v-show="activeItem === '实时超警'">{{activeItem === '实时超警' ? item.updateTime || '-' : item.warnTime || '-'}}</span>
      </li>
    </ul>
  </div>
</template>

<script>
import { WorkingConditionsApi } from '@/api/bigScreenPart';
import { forecastAlert } from '@/api/new_screen_api';
export default {
  name: 'controlFloodDispatchChartEarlyWarn',
  props: {},
  components: {},
  data() {
    return {
      activeItem: '实时超警',
      realTimeData: [],
      realTimeOverAlertTotal: 0,
      forecastData: [],
      forecastDataOverAlertTotal: 0
    };
  },
  computed: {
    tableData() {
      return this.activeItem === '实时超警' ? this.realTimeData : this.forecastData;
    }
  },
  watch: {},
  created() {
    this.getRealTimeData();
    this.getForecastData();
  },
  mounted() {},
  destroyed() {},
  methods: {
    /**
     * @description 获取实时超警数据
     */
    getRealTimeData() {
      WorkingConditionsApi.overAlert({ type: '实时超警' }).then((res) => {
        if (res.code === 0) {
          this.realTimeData = res.data || [];
          this.realTimeOverAlertTotal = this.realTimeData.filter((e) => {
            return e.waterLevel && e.warningLevel && e.waterLevel >= e.warningLevel;
          }).length;
        }
      });
    },
    /**
     * @description 获取预测超警数据
     */
    getForecastData() {
      forecastAlert().then((res) => {
        if (res.code === 0) {
          this.forecastData = res.data || [];
          this.forecastDataOverAlertTotal = this.forecastData.filter((e) => {
            return e.waterLevel && e.warningLevel && e.waterLevel >= e.warningLevel;
          }).length;
        }
      });
    }
  }
};
</script>
<style lang='scss' scoped>
.chart_early_warn {
  width: 100%;
  height: 100%;
  ul.tabbar {
    display: flex;
    margin-bottom: 10px;
    li {
      height: 22px;
      border-radius: 10px;
      padding: 0 12px;
      font-size: 13px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #ffffff;
      line-height: 22px;
      cursor: pointer;
      &.active {
        background: linear-gradient(180deg, #36c0e4 0%, #0639ff 100%);
      }
      & + li {
        margin-left: 30px;
      }
    }
  }
  ul.table {
    overflow: auto;
    height: calc(100% - 32px);
    li {
      box-sizing: border-box;
      height: 35px;
      font-size: 15px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ffffff;
      line-height: 35px;
      &:nth-child(odd) {
        background-image: linear-gradient(to right, rgba(255, 255, 255, 0.2), rgba(249, 249, 249, 0));
      }
      &.header {
        background: linear-gradient(90deg, rgba(44, 158, 191, 0.3) 0%, rgba(41, 147, 180, 0.1) 100%);
        font-weight: 400;
        color: #00baff;
      }
      span {
        display: inline-block;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        &:nth-child(1) {
          padding-left: 16px;
          width: 57px;
        }
        &:nth-child(2) {
          padding-left: 15px;
          width: 79px;
        }
        &:nth-child(3) {
          padding-left: 15px;
          width: 77px;
        }
        &:nth-child(4) {
          padding-left: 15px;
          width: 100px;
        }
        &:nth-child(5) {
          padding-left: 15px;
          width: 110px;
        }
      }
    }
  }
}
</style>