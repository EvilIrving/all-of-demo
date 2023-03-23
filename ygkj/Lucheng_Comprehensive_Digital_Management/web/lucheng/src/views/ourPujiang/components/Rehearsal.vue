<!--
 * @Date: 2022-05-06 17:08:31
 * @Author: 
 * @Description: 戍浦江防洪调度 预演
 * @LastEditors: dtb
 * @LastEditTime: 2022-05-13 14:11:14
 * @FilePath: \lucheng\src\views\ourPujiang\components\Rehearsal.vue
-->
<template>
  <div class="rehearsal-card" ref="">
    <panel-item title="预演">
      <div class="query_wrapper">
        <LabelFormItem>
          <el-select v-model="rainfall" @change="rainSelectVal" placeholder="实时降雨">
            <el-option v-for="item in rainfallOption" :key="item.name" :label="item.name" :value="item.value" />
          </el-select>
        </LabelFormItem>
        <LabelFormItem>
          <el-select v-model="rainChosedValue" placeholder="24h">
            <el-option v-for="item in rainChosedOption" :key="item.name" :label="item.name" :value="item.value" />
          </el-select>
        </LabelFormItem>
        <div v-for="(item, index) in tabButtons" :key="index" :class="[index == activeTab && 'active', 'tab-button']" @click="handleTabChange(index)">
          {{ item }}
        </div>
      </div>
      <div class="cahrt_wrapper">
        <v-line-and-high-chart height="100%" width="100%" ref="rehearsalChart" :barWidth='20' :barData="barData" :colorArr="ranksColor" :labelColor="ranksLabelColor" :barName="ranksBarName" unit="mm" @barClick="clickBarData"></v-line-and-high-chart>
      </div>
    </panel-item>
  </div>
</template>

<script>
import VLineAndHighChart from '../components/vchart_3dbar/VchartBar.vue';
import { PanelItem } from '@/components';
import LabelFormItem from './LabelFormItem.vue';
import { hourlyRainfall } from '@/api/bigScreenPart'
export default {
  name: 'Rehearsal',
  props: {

  },
  components: {
    PanelItem,
    LabelFormItem,
    VLineAndHighChart
  },
  data() {
    return {
      rainfall: 0,
      rainfallOption: [{ name: ' 实时降雨', value: 0 }, { name: '预报降雨', value: 1 }, { name: '典型暴雨', value: 2 },],
      rainChosedValue: 24,
      rainChosedOption: [{ name: '24h', value: 24 }, { name: '36h', value: 36 }, { name: '72h', value: 72 }],
      realOrPrecastOption: [{ name: '24h', value: 24 }, { name: '36h', value: 36 }, { name: '72h', value: 72 },],
      stormChosedOption: [{ name: '潘多拉', value: 0 }, { name: '哥斯拉', value: 1 }, { name: '毕达哥斯拉', value: 2 },],
      activeTab: 0,// 模拟计算 0 方案对比 1 
      tabButtons: ['模拟计算', '方案对比'],
      ranksLabelColor: '#1EF8F2',
      ranksBarName: '',
      ranksColor: ['#15E2DB', '#9DFDFB', '#A6FFFF'],
      barData: {
        columns: ['name', 'data'],
        rows: []
      },
      rankUnit: 'mm',
      scheduleList: [
        { active: 0, name: '控运分析', imgSrc: require('@/assets/images/distributionMap/control.png') },
        { active: 1, name: '负荷分析', imgSrc: require('@/assets/images/distributionMap/load.png') },
        { active: 2, name: '排水分析', imgSrc: require('@/assets/images/distributionMap/drain.png') }
      ],
    };
  },
  computed: {

  },
  watch: {
    rainfall(val) {
      this.getChartData()
    },
    rainChosedValue(val) {
      this.getChartData()
    },
  },
  created() {

  },
  mounted() {
    this.getChartData()
  },
  methods: {
    clickBarData() { },
    async getChartData() {
      // hours使用rainChosedValue，此时默认先传720小时，有数据
      let res = await hourlyRainfall({ hours: 720 })
      res.data.forEach(element => {
        element.data = element.drp
        element.name = element.tm
      });
      this.barData.rows = res.data
      this.$nextTick(() => {
        this.$refs['rehearsalChart'].resize();
      });
    },
    rainSelectVal(val) {
      switch (val) {
        case 2:
          this.rainChosedOption = this.stormChosedOption
          this.rainChosedValue = '潘多拉'
          break;
        default:
          this.rainChosedOption = this.realOrPrecastOption
          this.rainChosedValue = '24h'
          break;
      }
    },
    handleTabChange(val) {
      this.activeTab = val;
      if (val == 1) {
        this.$emit('changeMapType',false)
      }
    },
  },
};
</script>

<style scoped lang="scss">
.rehearsal-card {
  height: 100%;
  .cahrt_wrapper {
    height: 75%;
  }
}
.query_wrapper {
  margin: 5px 0px;
}
.tab-button {
  display: inline-block;
  cursor: pointer;
  user-select: none;
  height: 28px;
  padding: 6px;
  border-radius: 3px;
  color: #fff;
  font-size: 16px;
  &.active {
    background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
  }
}
</style>