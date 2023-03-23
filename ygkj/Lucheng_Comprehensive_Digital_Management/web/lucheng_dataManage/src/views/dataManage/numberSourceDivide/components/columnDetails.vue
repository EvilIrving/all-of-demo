<!--
 * @Author: zg
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: zg
 * @Description: 详情
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div class="column_details">
    <primary-table title="数据列" :hideInput="true" :hideAdd="true">
      <!-- 操作按钮 -->
      <template slot="operateBtn">
        <span class="_btn" @click="addDepart()">批量添加归属部门</span>
        <span class="_btn" @click="goBack()">返回</span>
      </template>
      <div ref="tableBox" class="table-box">
        <!-- 表格  headerCellClassName="myNowrap" -->
        <table-page
          ref="tablePage"
          dataLoaded
          :manualLoad="true"
          :url="tableUrl"
          :columns="columns"
          :query-params="queryParams"
          :checkBox="true"
          :height="tableHeight"
          @tableLoaded="tableLoaded"
          @table-select-val="selectVal"
        >
          <template slot-scope="{ index }" slot="order">{{
            index + 1
          }}</template>
          <template slot-scope="{ row }" slot="action">
            <el-button
              v-if="row.deptList && row.deptList.length > 0"
              @click="deleteDepart(row)"
              type="text"
              >删除归属部门</el-button
            >
          </template>
        </table-page>
      </div>
    </primary-table>

    <!-- 归属部门表单 -->
    <el-dialog
      title="归属部门"
      :visible.sync="departDialogShow"
      :close-on-click-modal="false"
      @close="closeDepartDialog"
    >
      <DepartForm
        ref="departForm"
        :selectRows="selectTableData"
        :dataSourceId="dataSourceId"
        :tableName="tableName"
        @close="closeDepartDialog"
        @closeDialogAndRefresh="closeDepartDialogAndRefresh"
      ></DepartForm>
    </el-dialog>
  </div>
</template>

<script>
import {
  getTableColumn,
  deleteAssignedData,
} from "_a/dataManage/dataSheetManage/dataSheetManage";
import { ConfirmCustom, MessageError, MessageSuccess } from "_l/message";
import PrimaryTable from "_c/primary_table/index";
import TablePage from "_c/CommonForm/table-page";
import DepartForm from "./departForm";
export default {
  name: "columnDetails",
  props: {},
  components: { PrimaryTable, TablePage, DepartForm },
  data() {
    return {
      tableUrl: "/data-manage/data/page",
      columns: [],
      queryParams: {
        page: true,
      },
      tableHeight: 0,
      dataSourceId: "",
      tableName: "",
      headerList: [],
      selectTableData: [],
      departDialogShow: false,
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = this.$refs.tableBox.offsetHeight - 42;
    });
  },
  methods: {
    // 获取列数据
    getColumnData(row) {
      //this.columns = [];
      this.headerList = [];
      this.dataSourceId = row.dataSourceId;
      this.tableName = row.tableName;
      this.queryParams.dataSourceId = this.dataSourceId;
      this.queryParams.tableName = this.tableName;
      getTableColumn({
        dataSourceId: this.dataSourceId,
        tableName: this.tableName,
      }).then((res) => {
        if (res.data) {
          if (res.data.length > 0) {
            this.headerList = res.data;
            this.columns.push({ label: "序号", slot: "order", width: 50 });
            res.data.map((item) => {
              if (item.dataType == -7) {
                this.columns.push({
                  label: item.displayName,
                  prop: item.columnName,
                  minWidth: 160,
                  // renderHeader: this.renderHeader,
                  formatter: this.formatBoolean,
                });
              } else {
                this.columns.push({
                  label: item.displayName,
                  prop: item.columnName,
                  minWidth: 160,
                  // renderHeader: this.renderHeader,
                  formatter: this.formatData,
                });
              }
            });
            this.columns.push({
              label: "归属部门",
              // prop: "myDepartment",
              minWidth: 160,
              fixed: "right",
              formatter: this.formatDept,
            });
            this.columns.push({
              label: "操作",
              slot: "action",
              width: 120,
              fixed: "right",
            });

            this.refresh();
          }
        }
      });
    },

    tableLoaded(data) {
      this.$nextTick(() => {
        this.$refs.tablePage.doLayout();
      });
    },

    /**
     * 处理表格归属部门数据
     */
    formatDept(row, column, cellValue) {
      let deptText = "";
      if (row.deptList && row.deptList.length > 0) {
        let deptArr = [];
        row.deptList.map((item) => {
          deptArr.push(item.deptName);
        });
        deptText = deptArr.join(",");
      }
      return deptText;
    },

    /**
     * 点击每一行的checkbox
     */
    selectVal(data) {
      this.selectTableData = data;
    },

    /**
     * 打开列数据归属部门弹窗
     */
    addDepart() {
      if (this.selectTableData.length == 0) {
        this.$message({ message: "请至少选择一条数据！", type: "error" });
      } else {
        this.departDialogShow = true;
        this.$nextTick(() => {
          this.$refs.departForm.reset();
        });
      }
    },

    closeDepartDialog() {
      this.$refs.departForm.reset();
      this.departDialogShow = false;
    },
    /**
     * 关闭列数据归属部门弹出框和刷新
     */
    closeDepartDialogAndRefresh() {
      this.closeDepartDialog();
      this.refresh();
    },

    /**
     * 删除列数据归属部门
     */
    deleteDepart(data) {
      let idList = [];
      if (data.deptList && data.deptList.length > 0) {
        data.deptList.map((item) => {
          idList.push(item.id);
        });
      } else {
        return false;
      }
      ConfirmCustom({ type: "warning" }).then(() => {
        deleteAssignedData(idList).then((res) => {
          if (res.code) {
            MessageSuccess("del");
          } else {
            MessageError("del");
          }
          // 刷新表格
          this.refresh();
        });
      });
    },

    /**
     * 刷新
     */
    refresh() {
      this.$refs.tablePage.refresh();
    },

    goBack() {
      this.$emit("close");
    },

    formatData(row, column, cellValue) {
      return row.data[column.property];
    },
    formatBoolean(row, column, cellValue) {
      //console.log(11);
      if (row.data[column.property]) {
        return "true";
      } else {
        return "false";
      }
    },
  },
};
</script> 

<style lang='scss' scoped>
@import "@/styles/customizeHandle.scss";
.column_details {
  width: 100%;
  height: 100%;
  padding: 16px;
  @include themeify {
    background-color: themed("background_color1");
  }
  .wrap {
    width: 100%;
    height: 100%;
    ::v-deep .table_head {
      justify-content: flex-end;
    }
    ::v-deep .table_content {
      width: 100%;
      height: calc(100% - 128px);
      padding-bottom: 16px;
      .table-box {
        height: 100%;
      }
    }
  }
}
.dialog-footer {
  text-align: right;
}
::v-deep .myNowrap .cell span {
  display: block;
  width: 100%;
  height: 23px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.columnDataDialog {
  ::v-deep .el-dialog__body {
    padding: 0;
  }
}
</style>
