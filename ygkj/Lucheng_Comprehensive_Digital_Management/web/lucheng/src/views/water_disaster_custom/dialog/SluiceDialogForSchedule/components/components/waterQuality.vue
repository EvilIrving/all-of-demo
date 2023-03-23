<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-01-10 14:27:42
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\components\waterQuality.vue
-->
<template>
  <div class="water-box">
    <div class="water-from-box">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="站点名称：">
          <el-select size="small" v-model="form.region" placeholder="请选择">
            <el-option label="区域一" value="shanghai"></el-option>
            <el-option label="区域二" value="beijing"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div class="water-content-box">
      <div class="water-left">
        <div class="water-content-title">
          <img src="~assets/images/diamonds.png" alt="" />
          <span>水质曲线</span>
        </div>
        <div class="curve_line">
          <!-- <ve-line
          :extend="chartWaterExtend"
          :colors="waterColors"
          :data="chartWaterData"
          :settings="chartWaterSettings"
          :height="'100%'"
          width="100%"
          :grid="waterGrid"
          :mark-line="waterMarkLine"
        /> -->
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
export default {
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  components: { PrimaryMapTable },
  data() {
    return {
      columnList: [
        {
          prop: 'tm',
          label: '时间'
        },
        {
          prop: 'drp',
          label: '水位(m)'
        }
      ],
      dataList: [],
      form: {
        region: ''
      }
    };
  },
  mounted(){
    this.getListSt()
  },
  methods:{
    async getListSt(){
      let res = await getSeawallListSt({
        projectCode: this.code,
        stType: 1,
        projectType: 1
      })
      console.log(res)
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
    span{
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
  }
  .water-right {
    width: calc(100% - 804px);
  }
}
</style>