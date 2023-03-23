<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-12-27 14:35:13
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\components\flow.vue
-->
<template>
  <div class="water-box">
    <div class="water-from-box">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="站点名称：">
          <el-select size="small" v-model="form.region" placeholder="请选择">
            <el-option :label="item.stnm" :value="item.stcd" v-for="item in options" :key="item.stcd"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div class="water-content-box">
      <div class="water-left">
        <div class="water-content-title">
          <img src="~assets/images/diamonds.png" alt="" />
          <span>流量曲线</span>
        </div>
        <div class="curve_line">
          <ve-line v-if="chartWaterData.rows.length > 0" :extend="chartWaterExtend" :colors="waterColors" :data="chartWaterData" :settings="chartWaterSettings" :height="'100%'" width="100%" :grid="waterGrid" :mark-line="waterMarkLine" />
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
import { getSeawallListSt } from '@/api/seawall.js';
import { hydraulicApi } from '@/api/hydraulic_amap/index.js';
export default {
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  components: { PrimaryMapTable },
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
          label: '时间',
          width: 160
        },
        {
          prop: 'z',
          label: '水位(m)'
        },
        {
          prop: 'flow',
          label: '流量(m³/s)'
        }
      ],
      waterColors: ['#32BCB4', '#19af47', 'red'],
      chartWaterData: {
        columns: ['时间', '流量', '水位'],
        rows: []
      },
      dataList: [],
      options: [],
      form: {
        region: ''
      }
    };
  },
  mounted() {
    this.getListSt();
  },
  methods: {
    async getListSt() {
      let res = await getSeawallListSt({
        projectCode: this.code,
        stType: 3,
        projectType: 1
      });
      console.log(res);
      if (res.code == 0) {
        this.options = res.data || [];
        if (this.options.length > 0) {
          this.form.region = this.options[0].stcd;
          this.getFlowDetail();
        }
      }
    },
    async getFlowDetail() {
      let data = this.options.find((item) => item.stcd == this.form.region);
      this.subhead = '流量列表';
      const opt = {
        hours: 24,
        stCode: data.stcd
      };
      this.chartWaterData.columns = ['时间', '流量', '水位'];
      hydraulicApi.getMrwlCurve(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          let reverseList = res.data.map((item) => {
            item.tm = new Date(item.tm).format('yyyy-MM-dd hh:mm:ss');
            return item;
          });
          this.dataList = JSON.parse(JSON.stringify(reverseList)).reverse()
          // let info = JSON.parse(JSON.stringify(res.data)).reverse();
          info.map((item) => {
            item['时间'] = new Date(item.tm).format('yyyy-MM-dd hh:mm:ss');
            item['流量'] = item.flow;
            item['水位'] = item.z;
            return item;
          });
          this.chartWaterData.rows = info;
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
    height: 400px;
  }
  .water-left {
    width: 804px;
    .curve_line {
      height: calc(100% - 60px);
    }
  }
  .water-right {
    width: calc(100% - 804px);
  }
}
</style>