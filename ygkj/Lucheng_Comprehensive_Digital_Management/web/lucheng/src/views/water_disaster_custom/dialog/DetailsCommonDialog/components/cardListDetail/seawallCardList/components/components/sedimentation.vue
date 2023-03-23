<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-03-07 13:41:45
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\components\sedimentation.vue
-->
<template>
  <div class="water-box">
    <div class="query-box">
      <span style="color: white">站点名称：</span>
      <el-select size="mini" v-model="region" placeholder="请选择" @change="getWaterLevels">
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
        @change="getWaterLevels"
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
          <span>沉降曲线</span>
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
import DataDetailButton from '../../../../DataDetailButton.vue';
import { mapGetters } from 'vuex';
import { PrimaryMapTable } from '@/components';
import { getSeawallListSt, getAqjcDatas } from '@/api/seawall.js';
import { exportGetAqjcData } from '@/api/micro-service/project-manage/export';
export default {
  props: {
    code: {
      type: String,
      default: ''
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
          yAxis: 0,
          label: {
            position: 'middle',
            formatter: '警戒上限值' // 这儿设置安全基线
          },
          lineStyle: {
            color: 'red' // 这儿设置安全基线颜色
          }
        },
        {
          yAxis: 0,
          label: {
            position: 'middle',
            formatter: '警戒下限值' // 这儿设置安全基线
          },
          lineStyle: {
            color: 'red' // 这儿设置安全基线颜色
          }
        }
      ]
    };
    return {
      columnList: [
        {
          prop: 'gcsj',
          label: '时间'
        },
        {
          prop: 'zzjg',
          label: '观测值(mm)'
        }
      ],
      waterColors: ['#32BCB4', 'rgb(240, 236, 12)', 'red'],
      chartWaterData: {
        columns: ['时间', '观测值'],
        rows: []
      },
      dataList: [],
      options: [],
      region: '',
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
      }
    };
  },
  computed: {
    ...mapGetters(['TyphoonVal', 'PreviewTime'])
  },
  mounted() {
    this.getListSt();
  },
  methods: {
    async exportExcel(val) {
      let data = this.options.find((item) => item.stcd == this.region);
      let obj = {
        stationCode: data.stcd,
        days: 1,
        fcstId: this.TyphoonVal,
        startTime: this.PreviewTime[0] ? this.PreviewTime[0] : this.timeChangeValue[0],
        endTime: this.PreviewTime[1] ? this.PreviewTime[1] : this.timeChangeValue[0]
      };
      exportGetAqjcData(obj);
    },
    async getListSt() {
      let res = await getSeawallListSt({
        projectCode: this.code,
        stType: 5,
        projectType: 1
      });
      if (res.code == 0) {
        this.options = res.data.filter((item) => item.stcd.indexOf('ES_') != -1) || [];
        if (this.options.length > 0) {
          this.region = this.options[0].stcd;
          this.getWaterLevels();
        }
      }
    },
    getWaterLevels() {
      let data = this.options.find((item) => item.stcd == this.region);
      getAqjcDatas({
        stationCode: data.stcd,
        days: 1,
        fcstId: this.TyphoonVal,
        startTime: this.PreviewTime[0] ? this.PreviewTime[0] : this.timeChangeValue[0],
        endTime: this.PreviewTime[1] ? this.PreviewTime[1] : this.timeChangeValue[0]
      }).then((res) => {
        this.dataList = JSON.parse(JSON.stringify(res.data)).reverse();
        let info = JSON.parse(JSON.stringify(res.data));
        this.chartWaterData.columns = ['时间', '观测值', '警戒上限值', '警戒下限值'];
        info.map((item) => {
          item['时间'] = item.gcsj;
          item['警戒上限值'] = item.maxValue;
          item['警戒下限值'] = item.minValue;
          this.waterMarkLine.data[0].yAxis = item.maxValue;
          this.waterMarkLine.data[1].yAxis = item.minValue;
          item['观测值'] = item.zzjg;
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
</style>