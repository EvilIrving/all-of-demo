<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-07-27 21:30:11
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\MotionDialog\components\components\water.vue
-->
<template>
  <div class="water-box">
    <div class="query-box">
      <span style="color: white">站点名称：</span>
      <el-select size="mini" v-model="region" placeholder="请选择" @change="getWaterLevels">
        <el-option :label="item.stnm" :value="item.stcd" v-for="item in options" :key="item.stcd"></el-option>
      </el-select>
    </div>
    <div class="water-content-box">
      <div class="water-left">
        <div class="water-content-title">
          <img src="~assets/images/diamonds.png" alt="" />
          <span>水位曲线</span>
        </div>
        <div class="curve_line">
          <ve-line
            v-if="chartWaterData.rows.length > 0"
            :extend="chartWaterExtend"
            :colors="waterColors"
            :data="chartWaterData"
            :settings="chartWaterSettings"
            height="100%"
            width="100%"
            :grid="waterGrid"
            :mark-line="waterMarkLine"
          />
          <div v-else style="color: rgba(255, 255, 255, 0.8); text-align: center; margin-top: 150px">暂无数据</div>
        </div>
      </div>
      <div class="water-right">
        <div class="water-content-title">
          <img src="~assets/images/diamonds.png" alt="" />
          <span>列表</span>
        </div>
        <div class="table-box water-table-box">
          <primary-map-table :columnList="columnList" :dataList="dataList"></primary-map-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { PrimaryMapTable } from '@/components';
import { waterAndRain } from '@/api/bigScreenPart';
import DataDetailButton from '../../common/DataDetailButton.vue';
import { getSeawallListSt } from '@/api/seawall.js';
export default {
  props: {
    pointData: {
      type: Object,
      default: {}
    },
    type: {
      type: String,
      default: '水闸'
    }
  },
  components: { PrimaryMapTable, DataDetailButton },
  data() {
    this.chartWaterExtend = {
      legend: {
        icon: 'rect',
        itemWidth: 8,
        itemHeight: 4,
        right: 0,
        bottom: 0,
        textStyle: {
          //图例文字的样式
          color: '#FFF',
          fontSize: 12
        }
      },
      'xAxis.0.axisLabel.color': '#fff',
      'yAxis.0.axisLabel.color': '#fff'
    };
    this.chartWaterSettings = {
      showLine: ['水位曲线']
    };
    this.waterGrid = {
      top: 20,
      bottom: 30,
      left: 0,
      right: 0
    };
    this.waterMarkLine = {
      data: [
        {
          yAxis: 30
        }
      ],
      lineStyle: {
        normal: {
          color: 'red' // 这儿设置安全基线颜色
        }
      },
      label: {
        normal: {
          position: 'middle',
          formatter: '超汛限水位' // 这儿设置安全基线
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
          prop: 'upz',
          label: '闸上水位(m)'
        }
      ],
      waterColors: ['#00BAFF', '#00BAFF', 'red'],
      chartWaterData: {
        columns: ['时间', '闸上水位'],
        rows: []
      },
      dataList: [],
      options: [],
      region: '',
      typeList: {
        水闸: {
          stType: 6,
          projectType: 6
        },
        水库: {
          stType: 7,
          projectType: 2
        },
        堤防: {
          stType: 7,
          projectType: 5
        }
      }
    };
  },
  mounted() {
    if (this.type == '水库') {
      this.columnList =[
        {
          prop: 'tm',
          label: '时间'
        },
        {
          prop: 'rz',
          label: '水位(m)'
        }
      ]
    }
    this.getListSt();
  },
  methods: {
    async getListSt() {
      let res = await getSeawallListSt({
        projectCode: this.pointData.code,
        stType: this.typeList[this.type].stType,
        projectType: this.typeList[this.type].projectType
      });
      if (res.code == 0) {
        this.options = res.data || [];
        if (this.options.length > 0) {
          this.region = this.options[0].stcd;
          this.getWaterLevels();
        }
      }
    },
    getWaterLevels() {
      let data = this.options.find((item) => item.stcd == this.region);
      waterAndRain
        .waterLevels({
          stationCode: data.stcd,
          stationType: data.stType,
          days: 1
        })
        .then((res) => {
          this.waterMarkLine = {}
          this.dataList = JSON.parse(JSON.stringify(res.data.data)).reverse();
          let info = JSON.parse(JSON.stringify(res.data.data));
          this.chartWaterData.columns = ['时间', '实时水位'];
          info.map((item) => {
            item['时间'] = item.tm;
            item['实时水位'] = item.upz || item.rz || 0;
            // item['保证水位'] = res.data.guaranteeLevel;
            // item['警戒水位'] = res.data.warningLevel;
            return item;
          });
          this.chartWaterData.rows = info;
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
.danger-box {
  padding-top: 22px;
  .danger-tap {
    display: flex;
    > div {
      width: 114px;
      height: 32px;
      border: 1px solid rgba(255, 255, 255, 0.6);
      line-height: 32px;
      text-align: center;
      margin-right: 16px;
      color: rgba(255, 255, 255, 0.8);
      cursor: pointer;
      &.active {
        color: #00baff;
        background: rgba(72, 152, 251, 0.1);
        box-shadow: 0px 0px 5px 0px #00baff inset;
        border-color: rgba(0, 186, 255, 0.6);
      }
    }
  }
}
</style>