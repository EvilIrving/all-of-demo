<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-02 13:50:57
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\SluiceDialogForSchedule\components\components\PagePatrol.vue
-->
<template>
  <div class="page-patrol">
    <div class="water-from-box">
      <span style="color:white">是否有隐患：</span>
      <el-select size="mini" v-model="hiddenTroubleCount" placeholder="请选择" @change="getPagePatrol">
        <el-option v-for="item in options" :key="item.value" :label="item.name" :value="item.value" placeholder="请选择">
        </el-option>
      </el-select>
      <span style="color:white;margin-left: 10px;">时间：</span>
      <el-date-picker size="mini" v-model="timeChangeValue" type="datetimerange" :picker-options="pickerOptions" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" align="right" clearable @change="getPagePatrol">
      </el-date-picker>
      <DataDetailButton class="data-detail-button" style="margin-left: auto" @click="exportExcel">导 出</DataDetailButton>
    </div>
    <v-table :tableData="tableData" :dynamicColumnSetting="dynamicColumnSetting">
      <template v-slot:hiddenTroubleCount="row">
        <span>{{ row.data.hiddenTroubleCount >= 1 ? '是' : '否'}}</span>
      </template>
    </v-table>
    <div class="bottom__pagination">
      <el-pagination :current-page.sync="pageNum" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize" layout="total, sizes, prev, pager, next" :total="totalPage" @size-change="onSizeChange" @current-change="onCurrentChange" />
    </div>
  </div>
</template>

<script>

import { getSeawallPagePatrol } from '@/api/seawall.js';
import VTable from '../../common/VTable';
import DataDetailButton from "../../common/DataDetailButton";
import { mapGetters } from 'vuex';
import { exportPagePatrol } from "@/api/micro-service/project-manage/export";
export default {
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      dynamicColumnSetting: [
        {
          prop: 'realName',
          label: '巡查人'
        },
        {
          prop: 'patrolBegintime',
          label: '巡查开始时间'
        },
        {
          prop: 'patrolEndtime',
          label: '巡查结束时间'
        },
        {
          prop: 'hiddenTroubleCount',
          label: '是否有隐患',
          slot: true
        }
      ],
      tableData: [],
      pageSize: 10,
      totalPage: 1,
      pageNum: 1,
      timeChangeValue: [],
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      options: [{ name: '有', value: 1 }, { name: '没有', value: 0 }],
      hiddenTroubleCount: ''
    };
  },
  components: {
    VTable, DataDetailButton
  },
  computed: {
    ...mapGetters(['SeawallActive']),
  },
  mounted() {
    this.getPagePatrol();
  },
  methods: {
    async getPagePatrol() {
      let res = await getSeawallPagePatrol({
        projectCode: this.code,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        startTime: this.timeChangeValue ? this.timeChangeValue[0] : '',
        endTime: this.timeChangeValue ? this.timeChangeValue[1] : '',
        seawallName: this.SeawallActive.name,
        hiddenTroubleCount: this.hiddenTroubleCount
      });
      if (res.code == 0 && res.data) {
        this.tableData = res.data.list;
        this.totalPage = res.data.totalRows;
      }
    },
    onSizeChange(size) {
      this.pageSize = size;
      this.pageNum = 1;
      this.getPagePatrol();
    },
    onCurrentChange(page) {
      this.pageNum = page;
      this.getPagePatrol();
    },
    // 导出
		async exportExcel() {
			const obj = {
				projectCode: this.code,
        startTime: this.timeChangeValue ? this.timeChangeValue[0] : '',
        endTime: this.timeChangeValue ? this.timeChangeValue[1] : '',
        seawallName: this.SeawallActive.name,
        hiddenTroubleCount: this.hiddenTroubleCount
			};
			exportPagePatrol(obj);
		},
  }
};
</script>

<style lang="scss" scoped>
.page-patrol {
  .bottom__pagination {
    padding-top: 10px;
    text-align: right;
  }
}
</style>
