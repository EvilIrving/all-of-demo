<!--
 * @Date: 2022-03-31 09:53:12
 * @Author: 
 * @Description: 水灾害防御 流域工程调度 水闸弹窗
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-15 14:51:27
 * @FilePath: \lucheng\src\views\WatershedSchedule\components\WaterDisasterDistributionMapDialog.vue
-->
<template>
  <div>
    <div v-if="pointData.stcd">
      <!-- 曲线 -->
      <div class="curve_line">
        <ve-line
          v-if="chartWaterData.rows.length > 0"
          :extend="chartWaterExtend"
          :colors="waterColors"
          :data="chartWaterData"
          :settings="chartWaterSettings"
          :height="'100%'"
          width="95%"
          :grid="waterGrid"
          :mark-line="waterMarkLine"
        />
        <div v-else style="width: 95%; color: rgba(255, 255, 255, 0.8); text-align: center; margin-top: 100px">
          暂无数据
        </div>
        <div class="curve_name">
          <div>
            <p>内江水位(m)</p>
            <p>{{ currData.upz || '-' }}</p>
          </div>
          <div>
            <p>外江水位(m)</p>
            <p>{{ currData.dwz || '-' }}</p>
          </div>
        </div>
      </div>
      <!-- 动态水闸门 -->
      <div class="sluice_dynamic_gate">
        <img v-if="pointData.gaorNum === 1" src="../../../assets/images/distributionMap/onegate.png" alt="" />
        <img v-if="pointData.gaorNum === 2" src="../../../assets/images/distributionMap/twogate.png" alt="" />
        <img v-if="pointData.gaorNum === 3" src="../../../assets/images/distributionMap/threegate.png" alt="" />
        <img v-if="pointData.gaorNum === 4" src="../../../assets/images/distributionMap/fourgate.png" alt="" />
        <img v-if="pointData.gaorNum === 5" src="../../../assets/images/distributionMap/fivegate.png" alt="" />
        <ul>
          <li v-for="(item, index) in opendingList" :key="index">
            <p>{{ index + 1 }}#液压闸门</p>
            <p>开度(m):{{ item }}</p>
          </li>
        </ul>
      </div>
    </div>
    <div v-else class="no-data">暂无数据~</div>
  </div>
</template>

<script>
import { waterAndRain, newGateOvs } from '@/api/bigScreenPart';
export default {
  name: 'WaterDisasterDistributionMapDialog',
  props: {
    pointData: Object
  },
  components: {},
  data() {
    this.chartWaterExtend = {
      legend: {
        show: true,
        icon: 'rect',
        itemWidth: 8,
        itemHeight: 4,
        right: 0,
        top: 0,
        textStyle: {
          //图例文字的样式
          color: '#FFF',
          fontSize: 12
        }
      },
      grid: {
        top: 40,
        left: 5
      },
      xAxis: {
        boundaryGap: false
      },
      series: [
        {
          type: 'line',
          symbol: 'circle',
          symbolSize: 1
        }
      ],
      'xAxis.0.axisLabel.color': '#fff',
      'yAxis.0.axisLabel.color': '#fff',
      'yAxis.1.axisLabel.color': '#fff'
    };
    this.chartWaterSettings = {
      showLine: ['水位曲线']
    };
    this.waterGrid = {
      top: 10,
      bottom: 10,
      left: 0,
      right: 0
    };
    return {
      waterColors: ['#19AF47', '#008FE8', '#E89100', '#A80BEE', '#FF4A1A', '#00BAFF', '#23E2FB'],
      waterMarkLine: {
        data: [
          {
            yAxis: 0
          }
        ],
        lineStyle: {
          normal: {
            type: 'solid',
            color: '#EECB01' // 这儿设置安全基线颜色
          }
        },
        label: {
          normal: {
            position: 'middle',
            formatter: '警戒线水位' // 这儿设置安全基线
          }
        }
      },
      chartWaterData: {
        columns: ['时间', '实时水位'],
        rows: []
      },
      opendingList: [],
      currData: {}
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getNewGateOvs();
    this.getWaterLevels();
  },
  methods: {
    async getNewGateOvs() {
      let res = await newGateOvs({ stcd: this.pointData.stcd });
      console.log(res, 'dasss');
      if (res.code == 0) {
        this.opendingList = res.data;
        // this.opendingList = [0.05, 0.05, 0.05, 0.05, 0.05];
      }
    },
    getWaterLevels() {
      waterAndRain
        .waterLevels({
          stationCode: this.pointData.stcd,
          stationType: 'DD',
          days: 1
        })
        .then((res) => {
          this.waterMarkLine = {};
          // this.dataList = JSON.parse(JSON.stringify(res.data.data)).reverse();
          let info = JSON.parse(JSON.stringify(res.data.data));
          this.chartWaterData.columns = ['时间', '内江水位', '外江水位'];
          info.map((item) => {
            item['时间'] = item.tm;
            item['内江水位'] = item.upz;
            item['外江水位'] = item.dwz;
            return item;
          });
          this.chartWaterData.rows = info;
          if (info.length > 0) {
            this.currData = info[info.length - 1];
          }

          // this.chartWaterData.rows = [];
        });
    }
  }
};
</script>

<style scoped lang="scss">
.curve_line {
  color: #fff;
  width: 100%;
  height: 350px;
  padding-top: 16px;
  display: flex;
  position: relative;
  .curve_name {
    margin-top: 50px;
    width: 125px;
    // height: 55px;

    div {
      width: 100%;
      background: center url('../../../assets/images/distributionMap/gateinfo.png') no-repeat;
      border: 1px solid #00c7c7;
      border-left: none;
      border-right: none;
      background-size: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      padding: 10px;
      margin-bottom: 28px;
      > p {
        padding-bottom: 5px;
        &:last-child {
          padding-bottom: 0px;
        }
      }
    }
  }
}
.sluice_dynamic_gate > img {
  width: 100%;
}
.sluice_dynamic_gate {
  height: calc(100% - 350px);
  position: relative;
  ul {
    position: absolute;
    bottom: 0px;
    color: #fff;
    display: flex;
    justify-content: center;
    width: 100%;
    li {
      background: center url('../../../assets/images/distributionMap/gateinfo.png') no-repeat;
      background-size: 100%;
      padding: 15px 0;
      margin-right: 15px;
      line-height: 18px;
      width: 104px;
      box-sizing: border-box;
      text-align: center;
      &:last-child{
        margin-right: 0;
      }
    }
  }
}
.no-data {
  height: 500px;
  text-align: center;
  padding-top: 200px;
  box-sizing: border-box;
}
</style>
