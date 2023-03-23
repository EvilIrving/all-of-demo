<!--
 * @Date: 2022-01-11 10:14:06
 * @Author: dtb
 * @Description: 监测点管理
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-26 14:42:57
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\MonitoringPointManage\MonitoringPointManage.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>监测点管理</p>
    </div>
    <div class="info_option" style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box">
      <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input placeholder="请输入名称" prefix-icon="el-icon-search" @change="getList()" v-model="NameVal" style="width: 240px; margin-right: 15px" clearable />
      <span style="font-size: 14px; margin-left: 15px">状态：</span>
      <el-select v-model="stationStatus" placeholder="请选择" @change="getList" clearable style="width: 180px; margin: 0 20px 0 15px">
        <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>

      <span style="font-size: 14px; margin-left: 15px">工程类型：</span>
      <el-select v-model="stationType" placeholder="请选择" @change="getList" clearable style="width: 180px; margin: 0 20px 0 15px">
        <el-option v-for="item in typeOptions" :key="item.name" :label="item.name" :value="item.name">
        </el-option>
      </el-select>
      <!-- <el-button type="primary" class="right" @click="exportExcel">导出</el-button> -->
    </div>
    <div class="tb_wrapper">
      <el-table v-bind="commonTableProps" v-loading="loading" :data="dynamicTableData">
        <el-table-column type="index" label="序号" width="55" align="center" :index="indexMethod" />
        <el-table-column align="center" v-for="column in dynamicColumnSetting" show-overflow-tooltip :formatter="columnFormatter" :key="column.prop" :prop="column.prop" :label="dynamicColumnLabel(column.label)" :width="column.width" :sortable="column.sortable || false">
          <template v-if="column.slot" #default="{ row }">
            <span v-bind="column.slot(row).props">
              {{ column.slot(row).label || column.slot(row) }}
            </span>
          </template>
        </el-table-column>
        <!-- <el-table-column align="center" :key="'tableHandle'" :label="tableHandle.label" :min-width="tableHandle.minWidth">
          <template v-slot="scope">
            <template v-for="(item, index) in tableHandle.btList">
              <span style="color:#1492FF;cursor:pointer" :key="index" @click="editTable(item.event, scope.row)">{{ item.label }}</span>
            </template>
          </template>
        </el-table-column> -->
      </el-table>
      <pagination :total="totalPage" :page.sync="pageNum" :limit.sync="pageSize" :page-sizes="[10, 20, 50, 100]" @pagination="getList" />
    </div>
    <el-dialog :title="title" :visible.sync="showEditDialog" width="35%">
      <monitoring-point-manage-form  :id="id" @closeEditDialog="closeEditDialog"></monitoring-point-manage-form>
    </el-dialog>
  </div>
</template>

<script>
import { disasterPreventionApi } from '@/api/seawallPrevent'
// import { exportPageCamera } from "@/utils/export.js";
import MonitoringPointManageForm from './MonitoringPointManageForm.vue'
import { tableSettings, columnSettings, commonTableProps } from '../index';
export default {
  name: 'MonitoringPointManage',
  props: {},
  components: {
    MonitoringPointManageForm,
  },
  data() {
    this.commonTableProps = commonTableProps;
    return {
      code: '视频监控',
      NameVal: '',
      stationStatus: '',
      statusOptions: [
        {
          label: '在线',
          value: 1,
        },
        {
          label: '离线',
          value: 0,
        },
      ],
      stationType: '',
      typeOptions: [
        {
          name: '水库'
        },
        {
          name: '山塘'
        },
        {
          name: '海塘'
        },
        {
          name: '水闸'
        },
        {
          name: '其他'
        }
      ],
      totalPage: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: '',
      title: '新增',
      editDialog: false,
      showEditDialog: false,
      loading: false,
      videoData: [],
      tableHandle: {
        label: "操作",
        minWidth: "60",
        btList: [
          { label: "编辑", event: "编辑" },
        ]
      },
    }
  },
  computed: {
    /**
     * 动态表格数据
     * @returns {any[]}
     */
    dynamicTableData() {
      if (Array.isArray(tableSettings[this.code])) {
        let ret;
        tableSettings[this.code].forEach((item, index) => {
          ret = index == 0 ? this[item] : ret[item];
        });
        return ret;
      } else return this[tableSettings[this.code]];
    },
    /**
     * 动态列配置
     * @returns {import('./tableSettings/index.js').ColumnProp[]}
     */
    dynamicColumnSetting() {
      return columnSettings[this.code].filter((column) => {
        if (column.conditions) {
          const conditions = column.conditions;
          let flag = false;
          for (let i = 0; i < conditions.length; i += 3) {
            if (
              (conditions[i + 1] == 'eq' && this[conditions[i]] == conditions[i + 2]) ||
              (conditions[i + 1] == 'neq' && this[conditions[i]] != conditions[i + 2])
            ) {
              flag = true;
            }
          }
          return flag;
        } else return true;
      });
    },
  },
  mounted() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true;
      let obj = {
        projectType: this.stationType,
        cameraName: this.NameVal,
        status: this.stationStatus,
        pageSize: this.pageSize,
        pageNum: this.pageNum
      };
      await disasterPreventionApi
        .listCamera(obj)
        .then((res) => {
          if (res.code == 0) {
            this.videoData = res.data.list;
            this.totalPage = res.data.totalRows;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    editTable(title, row) {
      console.log(row,'row');
      this.id = row.cameraIndexCode
      this.title = title
      this.showEditDialog = true
      this.editDialog = false
      this.$nextTick(() => {
        this.editDialog = true
      })
    },
    delTable(id) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        basMsgZjkApi.del(id).then((res) => {
          if (res.code == 0) {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.getList()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败！',
            })
          }
        })
      })
    },
    closeEditDialog() {
      this.showEditDialog = false
      this.getList()
    },
    async exportExcel() {
      let obj = {
        projectType: this.stationType,
        cameraName: this.NameVal,
        status: this.stationStatus,
      };
      await exportPageCamera(obj);
    },
    /**
    * 动态label
    * @param {string|()=>string} label
    */
    dynamicColumnLabel(label) {
      /** @description 使用call转换作用域*/
      if (typeof label == 'function') return label(this);
      return label;
    },
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
    /**
     * 表格序号项编号
     * @param {number} index
     * @returns {number}
     */
    indexMethod(index) {
      return this.pageSize * (this.pageNum - 1) + index + 1;
    },
  },
}
</script>

<style lang='scss' scoped>
.wrap {
  @include contentBox();
  height: 100%;
}
</style>