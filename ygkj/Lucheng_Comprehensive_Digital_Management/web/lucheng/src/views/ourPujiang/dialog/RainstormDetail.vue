<template>
  <div class="water-box">
    <div class="water-left">
      <div class="water-content-title">
        <img src="~assets/images/diamonds.png" alt="" />
        <span>降雨过程曲线</span>
      </div>
      <div class="curve_line">
        <bar-chart v-if="chartData.rows.length > 0" :chartData="chartData" :setting="setting" />
        <div v-else style="color: rgba(255, 255, 255, 0.8); text-align: center; margin-top: 150px">暂无数据</div>
      </div>
    </div>
    <div class="water-right">
      <!-- <div class="water-content-title">
        <img src="~assets/images/diamonds.png" alt="" />
        <span>列表</span>
      </div> -->
      <primary-map-table :columnList="columnList" :dataList="dataList"></primary-map-table>
    </div>
  </div>
</template>

<script>
import { PrimaryMapTable } from '@/components';
import { getSeawallListSt, rainfall24 } from '@/api/seawall.js';
import BarChart from '@/views/dataScreen/floodJudge/components/charts/BarChart';
import { HeavyRainsArchive } from "@/api/bigScreenPart";
export default {
  name: 'RainstormDetail',
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  components: { PrimaryMapTable, BarChart },
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
      columnList: [
        {
          prop: 'tm',
          label: '时间'
        },
        {
          prop: 'drp',
          label: '降雨量(mm)'
        }
      ],
      chartData: { rows: [], columns: [] },
      setting: { labelMap: {} },
      dataList: [{ tm: '5254235' }],
      options: [],
      region: '',
      realtimeList: {}
    };
  },
  computed: {
  },
  async mounted() {
    await this.getRain();
  },
  methods: {
    getRain() {
      HeavyRainsArchive.hourlyRainfallvalue({ id: this.code, })
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
  }
};
</script>

<style lang="scss" scoped>
.water-box {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
  .water-left {
    width: 600px;
    .curve_line {
      height: calc(100% - 60px);
    }
  }
  .water-right {
    width: calc(100% - 620px);
  }
}
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
</style>

