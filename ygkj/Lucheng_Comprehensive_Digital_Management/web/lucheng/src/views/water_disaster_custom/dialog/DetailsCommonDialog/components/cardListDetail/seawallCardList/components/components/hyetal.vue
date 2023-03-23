<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-05-11 10:23:25
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\components\hyetal.vue
-->
<template>
  <div class="water-box">
    <div class="query-box">
      <span style="color: white">站点名称：</span>
      <el-select size="mini" v-model="region" placeholder="请选择" @change="changeValue">
        <el-option :label="item.stnm" :value="item.stcd" v-for="item in options" :key="item.stcd"></el-option>
      </el-select>
      <span v-show="TyphoonVal === '' || PreviewTime.length === 0" style="color: white; margin-left: 10px">时间：</span>
      <el-date-picker
        v-show="TyphoonVal === '' || PreviewTime.length === 0"
        size="mini"
        v-model="timeChangeValue"
        type="datetimerange"
        :picker-options="pickerOptions"
        format="yyyy-MM-dd HH:mm:ss"
        value-format="yyyy-MM-dd HH:mm:ss"
        range-separator="至"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        align="right"
        clearable
        @change="changeValue"
      >
      </el-date-picker>
      <data-detail-button class="data-detail-button" @click="exportExcel(code)" style="margin-left: auto"
        >导 出</data-detail-button
      >
    </div>
    <div class="water-content-box">
      <div class="water-left">
        <div class="water-content-title">
          <img src="~assets/images/diamonds.png" alt="" />
          <span>降雨图表</span>
        </div>
        <div class="curve_line">
          <bar-chart v-if="chartData.rows.length > 0" :chartData="chartData" :setting="setting" />
          <div v-else style="color: rgba(255, 255, 255, 0.8); text-align: center; margin-top: 150px">暂无数据</div>
        </div>
      </div>
      <div class="water-right">
        <div class="water-content-title">
          <img src="~assets/images/diamonds.png" alt="" />
          <span style="cursor: pointer" @click="checkTab(true)">列表</span>
          <span
            style="cursor: pointer; margin-left: 20px"
            @click="
              checkTab(false);
              getRainfall24();
            "
            >统计</span
          >
        </div>
        <div class="table-box water-table-box">
          <primary-map-table v-if="tabAction" :columnList="columnList" :dataList="dataList"></primary-map-table>
          <card-tabs v-else ref="typeList" v-model="typeSelect" :list="typeList" :decimalsFlag="false" :col="8" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DataDetailButton from '../../../../DataDetailButton.vue';
import { PrimaryMapTable, LabelFormItem } from '@/components';
import { getSeawallListSt, rainfall24 } from '@/api/seawall.js';
import { rainApi } from '@/api/dataScreen/floodJudge/index';
import BarChart from '@/views/dataScreen/floodJudge/components/charts/BarChart';
import CardTabs from './CardTabs.vue';
import { mapGetters } from 'vuex';
import { exportHourRainfall } from '@/api/micro-service/project-manage/export';
export default {
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  components: { PrimaryMapTable, BarChart, CardTabs, LabelFormItem, DataDetailButton },
  data() {
    this.chartRainSettings = {
      showLine: ['累计雨量'],
      itemStyle: {
        //配置一些样式
        color: {
          type: 'linear',
          x: 0, //X轴起始坐标
          y: 0, //Y轴起始坐标
          x2: 1, //X轴终点坐标
          y2: 1, //Y轴终点坐标
          colorStops: [
            {
              offset: 0,
              color: '#9DE16C' // 0% 处的颜色
            },
            {
              offset: 1,
              color: '#07BBB9' // 100% 处的颜色
            }
          ],
          globalCoord: false // 缺省为 false
        }
      }
      //   labelMap: {
      //       tm: '时间',
      //       level: '水位'
      //   }
    };
    this.rainGrid = {
      top: 20,
      bottom: 30,
      left: 0,
      right: 0
    };
    this.chartRainExtend = {
      legend: {
        icon: 'rect',
        itemWidth: 8,
        itemHeight: 4,
        right: 0,
        bottom: 0,
        textStyle: {
          //图例文字的样式
          color: '#fff',
          fontSize: 12
        }
      },
      'xAxis.0.axisLabel.color': '#0b2045',
      series(v) {
        v && v.forEach((i) => (i.barGap = '0%'));
        return v;
      }
    };
    this.yAxis = {
      inverse: false,
      max: 20,
      textStyle: {
        color: '#fff' //这里是改变字体颜色
      },
      axisLine: {
        show: false,
        lineStyle: {
          color: '#fff'
        }
      }
    };
    return {
      timeChangeValue: [],
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }
        ]
      },
      tabAction: 1, //切换列表 和 统计
      realtimeList: [],
      typeSelect: 0,
      columnList: [
        {
          prop: 'tm',
          label: '时间'
        },
        {
          prop: 'drp',
          label: '雨量(mm)'
        }
      ],
      chartData: { rows: [], columns: [] },
      setting: { labelMap: {} },
      dataList: [],
      options: [],
      region: '',
      realtimeList: {}
    };
  },
  computed: {
    ...mapGetters(['TyphoonVal', 'PreviewTime']),
    typeList() {
      let list = [this.realtimeList.r3, this.realtimeList.r6, this.realtimeList.r24];
      return [
        {
          label: '前3小时',
          data: { number: list[0], unit: 'mm' }
        },
        {
          label: '前6小时',
          data: { number: list[1], unit: 'mm' }
        },
        {
          label: '前24小时',
          data: { number: list[2], unit: 'mm' }
        }
      ];
    }
  },
  async mounted() {
    await this.getListSt();
    await this.getRainfall24();
  },
  methods: {
    /**
     * 导出
     * @param {string} val
     */
    async exportExcel() {
      let data = this.options.find((item) => item.stcd == this.region);
      let obj = {
        stationCode: data.stcd,
        days: 1,
        fcstId: this.TyphoonVal,
        startTime: this.PreviewTime[0] ? this.PreviewTime[0] : this.timeChangeValue[0],
        endTime: this.PreviewTime[1] ? this.PreviewTime[1] : this.timeChangeValue[1]
      };
      exportHourRainfall(obj);
    },
    checkTab(index) {
      this.tabAction = index;
    },
    async getListSt() {
      let res = await getSeawallListSt({
        projectCode: this.code,
        stType: 2,
        projectType: 1
      });
      if (res.code == 0) {
        this.options = res.data || [];
        if (this.options.length > 0) {
          this.region = this.options[0].stcd;
          this.getRainStationInfo();
        }
      }
    },
    changeValue() {
      this.getRainStationInfo();
      this.getRainfall24();
    },
    getRainStationInfo() {
      let data = this.options.find((item) => item.stcd == this.region);
      rainApi
        .rainPop({
          stationCode: data.stcd,
          days: 1,
          fcstId: this.TyphoonVal,
          startTime: this.PreviewTime[0] ? this.PreviewTime[0] : this.timeChangeValue[0],
          endTime: this.PreviewTime[1] ? this.PreviewTime[1] : this.timeChangeValue[1]
        })
        .then((res) => {
          if (res.code == 0) {
            this.chartData = { rows: [], columns: [] };
            this.setting = { labelMap: {} };
            this.chartData.rows = JSON.parse(JSON.stringify(res.data)).reverse();
            this.chartData.columns = ['tm', 'drp'];
            this.setting.labelMap = {
              drp: '降雨量',
              tm: '时间'
            };
            this.dataList = res.data;
          }
        });
    },

    getRainfall24() {
      let data = this.options.find((item) => item.stcd == this.region);
      rainfall24(data.stcd).then((res) => {
        if (res.code === 0) {
          this.realtimeList = res.data;
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.water-content-box {
  display: flex;
  justify-content: space-between;
  .water-content-title {
    display: flex;
    align-items: center;
    position: relative;
    margin-bottom: 32px;
    span {
      color: #fff;
    }
    img {
      margin-right: 10px;
    }
    &::after {
      content: '';
      position: absolute;
      bottom: -8px;
      left: 0;
      width: 100%;
      height: 4px;
      background: url(~assets/images/long_line.png) no-repeat;
      background-size: 100% 100%;
    }
  }
  .table-box {
    height: 380px;
  }
  .water-left {
    width: 804px;
    .curve_line {
      height: calc(100% - 60px);
    }
  }
  .water-right {
    width: calc(100% - 820px);
  }
}
</style>

