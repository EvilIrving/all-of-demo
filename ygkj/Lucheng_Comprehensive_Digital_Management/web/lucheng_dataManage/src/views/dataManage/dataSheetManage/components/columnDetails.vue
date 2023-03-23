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
    <primary-table :hideInput="true" :hideAdd="true">
      <!-- 操作按钮 -->
      <template slot="operateBtn">
        <span class="_btn" @click="addColumnData()">新增</span>
        <span class="_btn" @click="updateColumnAlias()">更新列别名</span>
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
          :checkBox="false"
          :height="tableHeight"
          @tableLoaded="tableLoaded"
        >
          <template slot-scope="{ index }" slot="order">{{
            index + 1
          }}</template>
          <template slot-scope="{ row }" slot="action">
            <el-button @click="updateColumnData(row)" type="text"
              >修改</el-button
            >
            <el-button @click="deleteColumnData(row)" type="text"
              >删除</el-button
            >
          </template>
        </table-page>
      </div>
    </primary-table>

    <!-- 数据表单 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogShow"
      :close-on-click-modal="false"
      :append-to-body="true"
      @close="closeDialog"
      class="columnDataDialog"
      top="10vh"
    >
      <Form
        ref="form"
        :dialogTitle="dialogTitle"
        :headerList="headerList"
        :dataSourceId="dataSourceId"
        :tableName="tableName"
        @close="closeDialog"
        @closeDialogAndRefresh="closeDialogRefresh"
      ></Form>
    </el-dialog>

    <!-- 更新列别名 -->
    <el-dialog
      title="更新列别名"
      :visible.sync="columnAliasDialog"
      :close-on-click-modal="false"
      :append-to-body="true"
      @close="closeColumnAliasDialog"
      class="columnDataDialog"
      top="10vh"
    >
      <ColumnAlias
        ref="columnAlias"
        :headerList="headerList"
        :dataSourceId="dataSourceId"
        :tableName="tableName"
        @close="closeColumnAliasDialog"
        @closeDialogAndRefresh="closeColumnAliasDialogRefresh"
      ></ColumnAlias>
    </el-dialog>
  </div>
</template>

<script>
import {
  getTableColumn,
  deleteColumnDataById,
} from "_a/dataManage/dataSheetManage/dataSheetManage";
import { ConfirmCustom, MessageError, MessageSuccess } from "_l/message";
import PrimaryTable from "_c/primary_table/index";
import TablePage from "_c/CommonForm/table-page";
import Form from "./form";
import ColumnAlias from "./columAlias";
export default {
  name: "columnDetails",
  props: {},
  components: { PrimaryTable, TablePage, Form, ColumnAlias },
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
      dialogTitle: "新增数据",
      dialogShow: false,
      columnAliasDialog: false,
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
      this.columns = [];
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
              label: "所属部门",
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
     * 刷新
     */
    refresh() {
      this.$refs.tablePage.refresh();
    },

    // 数据新增
    addColumnData() {
      if (this.headerList.length == 0) {
        this.$message({ message: "数据列为空！", type: "error" });
      } else {
        this.dialogTitle = "新增数据";
        this.dialogShow = true;
      }
    },
    // 数据修改
    updateColumnData(data) {
      this.dialogTitle = "编辑数据";
      this.dialogShow = true;
      this.$nextTick(() => {
        this.$refs.form.reset();
        this.$refs.form.setForm({ ...data });
      });
    },
    // 数据删除
    deleteColumnData(data) {
      ConfirmCustom({ type: "warning" }).then(() => {
        deleteColumnDataById({
          // colList: this.headerList,
          data: data,
          dataSourceId: this.dataSourceId,
          tableName: this.tableName,
        }).then((res) => {
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
     * 关闭数据弹框
     */
    closeDialog() {
      this.$refs.form.reset();
      this.dialogShow = false;
    },
    /**
     * 关闭数据弹出框和刷新
     */
    closeDialogRefresh() {
      this.closeDialog();
      this.refresh();
    },
    /**
     * 更新列数据
     */
    updateColumnAlias() {
      this.columnAliasDialog = true;
      this.$nextTick(() => {
        this.$refs.columnAlias.reset();
        this.$refs.columnAlias.setForm(this.headerList);
      });
    },
    /**
     * 关闭更新列别名弹框
     */
    closeColumnAliasDialog() {
      this.$refs.columnAlias.reset();
      this.columnAliasDialog = false;
    },
    /**
     * 关闭更新列别名弹出框和刷新
     */
    closeColumnAliasDialogRefresh() {
      this.closeColumnAliasDialog();
      this.updateColumn();
    },
    updateColumn() {
      getTableColumn({
        dataSourceId: this.dataSourceId,
        tableName: this.tableName,
      }).then((res) => {
        if (res.data) {
          if (res.data.length > 0) {
            this.columns = [];
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
              label: "所属部门",
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
          }
        }
      });
    },

    // renderHeader(h, { column }) {
    //   return [
    //     h(
    //       "el-tooltip",
    //       {
    //         props: {
    //           effect: "dark",
    //           content: column.property + "（" + column.label + "）",
    //           placement: "top",
    //         },
    //       },
    //       [h("span", column.property)]
    //     ),
    //   ];
    // },
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
.column_details {
  height: 650px;
  .wrap {
    width: 100%;
    height: 100%;
    ::v-deep .table_head {
      justify-content: flex-end;
    }
    ::v-deep .table_content {
      width: 100%;
      height: calc(100% - 72px);
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
