<template>
  <div class="primary-dialog reservoir-detail-table">
    <DataDetailDialog
      :visible.sync="visible"
      :loading="exportLoading"
      :fullscreen="fullscreen"
      :before-close="handleClose"
      title="救援队伍"
      @changeFullScreen="changeFullscreen"
    >
      <template #stat>
        <StatisticsBox title="救援队伍总数" icon="icon-jiuyuanduiwu">
          <span>{{ statisticsData.total || '-' }} </span> 座
        </StatisticsBox>
        <StatisticsBox title="XXXXXXXX队(最多)" icon="icon-jiuyuanduiwu">
          <span>{{ statisticsData.avgStorageRatio || '-' }} </span> %
        </StatisticsBox>
        <StatisticsBox title="XXXXXXXX队(最少)" icon="icon-jiuyuanduiwu">
          <span>{{ statisticsData.avgLimitStorageAbility || '-' }} </span> mm
        </StatisticsBox>
      </template>
      <template #query>
        <LabelFormItem label="抢险专业：">
          <el-select v-model="professionalValue" size="small" clearable placeholder="全部" @change="getList">
            <el-option
              v-for="item in professionalList"
              :key="item.name"
              :label="item.name"
              :value="item.name"
            ></el-option>
          </el-select>
        </LabelFormItem>
        <LabelFormItem label="单位性质：">
          <el-select v-model="basin" size="small" clearable placeholder="全部" @change="getList">
            <el-option v-for="item in basinList" :key="item.name" :label="item.name" :value="item.name" />
          </el-select>
        </LabelFormItem>
        <LabelFormItem label="关键字:">
          <el-input v-model="resName" placeholder="请输入救援队伍名称" style="width: 180px" />
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
        <el-table-column :formatter="columnFormatter" type="index" label="序号" :index="indexMethod" />
        <el-table-column :formatter="columnFormatter" prop="resName" sortable="custom" label="名称" />
        <el-table-column :formatter="columnFormatter" prop="bas" label="抢险专业" />
        <el-table-column :formatter="columnFormatter" prop="areaName" label="单位性质" />
        <el-table-column
          :formatter="columnFormatter"
          sortable="custom"
          prop="waterLevel"
          width="100"
          label="编制人数"
        />
        <el-table-column
          :formatter="columnFormatter"
          sortable="custom"
          prop="waterLevel"
          width="100"
          label="现有人数"
        />
        <el-table-column
          :formatter="columnFormatter"
          sortable="custom"
          prop="waterLevel"
          width="100"
          label="详细地址"
        />
        <el-table-column
          :formatter="columnFormatter"
          sortable="custom"
          prop="waterLevel"
          width="100"
          label="联系方式"
        />
      </el-table>
      <template #bottom>
        <div class="bottom__pagination">
          <el-pagination
            :current-page.sync="pageNum"
            :page-sizes="[10, 20, 30, 50]"
            :page-size.sync="pageSize"
            layout="total, sizes, prev, pager, next"
            :total="total"
            @size-change="getList"
            @current-change="getList"
          />
        </div>
      </template>
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
import { pageMixin, filterMixin, tableMixin } from './mixins.js';
import { disasterPreventionApi } from '@/api/RightSildePanel';
import { exportRsvr } from '@/api/micro-service/project-manage/export';

export default {
  name: 'RescueTeamDetail',
  props: {
    visible: { type: Boolean, default: false }
  },
  components: { DataDetailDialog, StatisticsBox, LabelFormItem, DataDetailButton },
  mixins: [pageMixin, filterMixin, tableMixin],
  data() {
    return {
      exportLoading: false,
      loading: false,
      resName: '',
      /** 水库统计数据 */
      statisticsData: {},
      tableData: [],
      professionalValue: '',
      professionalList: [
        { name: '防汛救灾', value: 1 },
        { name: '水利抢险', value: 1 },
        { name: '工程救援', value: 1 },
        { name: '水利专业', value: 1 },
        { name: '水上救援', value: 1 },
        { name: '水利工程抢险', value: 1 },
        { name: '水上抢险', value: 1 },
        { name: '其他应急抢险', value: 1 },
        { name: '堤防抢险', value: 1 }
      ],
      basin: '',
      basinList: [
        { name: '水利局机关单位抢险组织', value: 1 },
        { name: '社会救援机构', value: 1 },
        { name: '自收自支事业单位', value: 1 },
        { name: '差额事业单位', value: 1 },
        { name: '全额事业单位', value: 1 }
      ],

      fullscreen: false
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    /**
     * 表格列数据格式化
     * @param {number|string} value
     */
    columnFormatter(row, column, value, index) {
      if (value) {
        if (typeof value == 'number' && parseFloat(value) != parseInt(value)) {
          return value.toFixed(2);
        }
        if (typeof value == 'string' && value.split(',').length > 1) {
          return parseInt(value.split(',')[0]).toFixed(1);
        }
        return value;
      } else {
        return '-';
      }
    },
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      this.$nextTick(() => {
        this.$refs.multipleTable.doLayout();
      });
    },
    async exportExcel() {
      this.exportLoading = true;
      const {} = this;
      const opt = {};
      await exportRsvr(opt);
      this.exportLoading = false;
    },
    async getList() {
      this.loading = true;
      const {} = this;
      const opt = {};
      await disasterPreventionApi.rsvrUnitList(opt).then((res) => {
        if (res.code == 0) {
          this.tableData = res.data.list;
          this.total = res.data.totalRows;
        }
      });
      this.$refs.multipleTable.doLayout();
      this.loading = false;
    },
    handleClose() {
      this.$emit('close');
      this.$emit('update:visible', false);
    }
  }
};
</script>
