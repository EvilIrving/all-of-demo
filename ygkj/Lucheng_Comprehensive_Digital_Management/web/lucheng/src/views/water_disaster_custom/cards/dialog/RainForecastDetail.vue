vue<template>
  <div class="primary-dialog reservoir-detail-table">
    <DataDetailDialog
      :visible.sync="visible"
      :loading="exportLoading"
      :fullscreen="fullscreen"
      :before-close="handleClose"
      title="短临预报"
      @changeFullScreen="changeFullscreen"
    >
      <template #stat>
        <StatisticsBox :title="`1小时`" icon="icon-rainfall_1">
          <span> {{ statisticsData.rainfall_1_total.toFixed(2) }} </span> mm
          <div class="box-max">最大降雨:{{ statisticsData.rainfall_1_max }}</div>
        </StatisticsBox>
        <StatisticsBox :title="`3小时`" icon="icon-rainfall_3">
          <span>{{ statisticsData.rainfall_3_total.toFixed(2) }} </span> mm
          <div class="box-max">最大降雨:{{ statisticsData.rainfall_3_max }}</div>
        </StatisticsBox>
        <StatisticsBox :title="`6小时`" icon="icon-rainfall_6">
          <span>{{ statisticsData.rainfall_6_total.toFixed(2) }} </span> mm
          <div class="box-max">最大降雨:{{ statisticsData.rainfall_6_max }}</div>
        </StatisticsBox>
      </template>
      <template #query>
        <LabelFormItem label="统计类型:">
          <el-select v-model="rainfallType" size="small" clearable placeholder="全部" @change="getList">
            <el-option v-for="item in rainfallTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </LabelFormItem>
        <DataDetailButton @click="exportExcel" style="margin-left: auto">
          导 出
        </DataDetailButton>
      </template>
      <el-table
        v-loading="loading"
        ref="multipleTable"
        :data="tableData"
        height="100%"
        stripe
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        @sort-change="sortChange($event, getList)"
      >
        <el-table-column :formatter="columnFormatter" type="index" label="序号" width="55" />
        <el-table-column prop="key" label="名称" />
        <el-table-column :formatter="columnFormatter" sortable prop="rainfall_1" label="1小时(mm)" />
        <el-table-column :formatter="columnFormatter" sortable prop="rainfall_3" label="3小时(mm)" />
        <el-table-column :formatter="columnFormatter" sortable prop="rainfall_6" label="6小时(mm)" />
      </el-table>
    </DataDetailDialog>
  </div>
</template>

<script>
import {
  DataDetailDialog,
  StatisticsBox,
  LabelFormItem,
  DataDetailButton
} from '../../dialog/DetailsCommonDialog/components';
import { tableMixin } from './mixins.js';
import { disasterPreventionApi } from '@/api/RightSildePanel';
import { exportRainfallPredictGrid } from '@/api/micro-service/project-manage/export';

export default {
  name: 'RainForestDetail',
  props: {
    visible: { type: Boolean, default: false }
  },
  components: {
    DataDetailDialog,
    StatisticsBox,
    LabelFormItem,
    DataDetailButton
  },
  mixins: [tableMixin],
  data() {
    this.rainfallTypeList = ['行政区划', '流域'];
    return {
      exportLoading: false,
      loading: false,
      rainfallType: '行政区划',

      /** 统计数据 */
      statisticsData: {
        rainfall_1_total: 0,
        rainfall_1_max: '',
        rainfall_3_total: 0,
        rainfall_3_max: '',
        rainfall_6_total: 0,
        rainfall_6_max: ''
      },
      tableData: [],
      fullscreen: false
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      this.$nextTick(() => {
        this.$refs.multipleTable.doLayout();
      });
    },
    async exportExcel() {
      this.exportLoading = true;
      const obj = { type: this.rainfallType };
      await exportRainfallPredictGrid(obj);
      this.exportLoading = false;
    },
    async getList() {
      this.loading = true;
      await disasterPreventionApi.rainfallPredictGrid({ type: this.rainfallType }).then((res) => {
        if (res.code == 0) {
          this.tableData = res.data;
          ['rainfall_1', 'rainfall_3', 'rainfall_6'].forEach((type) => {
            const rainfallList = this.tableData.map((item) => item[type]);
            this.statisticsData[`${type}_total`] = rainfallList.reduce((total, cur) => total + cur);
            this.statisticsData[`${type}_max`] = this.tableData.sort((a, b) => b[type] - a[type])[0].key;
          });
        }
      });

      this.loading = false;
    },
    handleClose() {
      this.$emit('close');
      this.$emit('update:visible', false);
    }
  }
};
</script>
<style lang="scss" scoped>
.box-max {
  display: inline-block;
  background: #e89100;
  padding: 2px 5px;
  border-radius: 3px;
  font-size: 12px;
}
</style>
