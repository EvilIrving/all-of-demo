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

      <div ref="tableBox" class="table-box">
        <!-- 表格  headerCellClassName="myNowrap" -->
        <table-page
          ref="tablePage"
          dataLoaded
          :manualLoad="true"
          :columns="columns"
          :checkBox="false"
          :height="tableHeight"
          :pageIngType="false"
          @tableLoaded="tableLoaded"
        >
          <template slot-scope="{ index }" slot="modifyStatus">
            <span v-if="index === 0">原数据</span>
            <span v-else-if="index === 1">修改后数据</span>
          </template>
        </table-page>
      </div>
    </primary-table>
    <!-- 自定义按钮组 -->
    <div slot="footer" class="dialog-footer" v-if="!isLook">
      <el-button type="primary" @click="auditPass()">通过</el-button>
      <el-button type="warning" @click="openRemarkDialog()">驳回</el-button>
    </div>

    <!-- 驳回备注弹窗 -->
    <el-dialog
      title="驳回备注"
      :visible.sync="remarkDialogShow"
      :close-on-click-modal="false"
      :append-to-body="true"
      @close="closeRemarkDialog"
    >
      <RejectRemark
        ref="remarkForm"
        @close="closeRemarkDialog"
        @closeDialogAndRefresh="closeRemarkDialogAndRefresh"
      ></RejectRemark>
    </el-dialog>
  </div>
</template>

<script>
import { getTableColumn } from "_a/dataManage/dataSheetManage/dataSheetManage";
import { auditDataApply } from "_a/dataManage/dataAudit/dataAudit";
import { ConfirmCustom, MessageError, MessageSuccess } from "_l/message";
import PrimaryTable from "_c/primary_table/index";
import TablePage from "_c/CommonForm/table-page";
import RejectRemark from "./rejectRemark";
export default {
  name: "columnDetails",
  props: {
    isLook: {
      type: Boolean,
      default: true,
    },
  },
  components: { PrimaryTable, TablePage, RejectRemark },
  data() {
    return {
      columns: [],
      tableHeight: 0,
      dataSourceId: "",
      tableName: "",
      headerList: [],
      selectTableData: [],
      remarkDialogShow: false,
      type: null,
      form: {},
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
      this.form = JSON.parse(JSON.stringify(row));
      this.type = row.applyType;
      this.columns = [];
      this.headerList = [];
      this.dataSourceId = row.dataSourceId;
      this.tableName = row.tableName;
      let dataObj = JSON.parse(row.dataObject);
      let sameKey = [];
      getTableColumn({
        dataSourceId: this.dataSourceId,
        tableName: this.tableName,
      }).then((res) => {
        if (res.data) {
          if (res.data.length > 0) {
            this.headerList = res.data;
            if (this.type == 3) {
              //如果是数据修改，显示原数据和修改后数据对比
              this.columns.push({
                label: "",
                slot: "modifyStatus",
                width: 100,
              });
              sameKey = this.ifCompare(dataObj.oldData, dataObj.data);
            }
            //console.log(sameKey);
            res.data.map((item) => {
              let className = "";
              if (!sameKey.includes(item.columnName) && this.type == 3) {
                className = "diffValue";
              }
              if (item.dataType == -7) {
                this.columns.push({
                  label: item.displayName,
                  prop: item.columnName,
                  minWidth: 160,
                  className: className,
                  // renderHeader: this.renderHeader,
                  formatter: this.formatBoolean,
                });
              } else {
                this.columns.push({
                  label: item.displayName,
                  prop: item.columnName,
                  minWidth: 160,
                  className: className,
                  // renderHeader: this.renderHeader,
                });
              }
            });

            let oldData = {};
            let newData = {};
            let dataList = [];
            if (dataObj.oldData) {
              oldData = dataObj.oldData;
              dataList.push(oldData);
            }
            if (dataObj.data) {
              newData = dataObj.data;
              dataList.push(newData);
            }
            this.$refs.tablePage.getTableData(dataList);
          }
        }
      });
    },

    // 列出所有的键，接着遍历数组
    ifCompare(object1, object2) {
      let o1keys = Object.keys(object1);
      let o2keys = Object.keys(object2);
      let arr = [];
      if (o1keys.length > 0) {
        for (let i = 0; i < o1keys.length; i++) {
          let key = o1keys[i];
          if (o2keys.includes(key)) {
            if (object2[key] === object1[key]) {
              arr.push(key);
            }
          }
        }
      }
      return arr;
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
     * 审核驳回弹窗
     */
    openRemarkDialog() {
      this.remarkDialogShow = true;
    },

    closeRemarkDialog() {
      this.remarkDialogShow = false;
    },
    /**
     * 关闭审核驳回弹窗
     */
    closeRemarkDialogAndRefresh(remark) {
      this.closeRemarkDialog();
      this.auditReject(remark);
    },

    /**
     * 审核通过
     */
    auditPass() {
      this.$confirm("确定审核通过该条数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.form.applyStatus = 1;
        auditDataApply(this.form).then((res) => {
          if (res.data) {
            this.$message({ message: "通过成功！", type: "success" });
            this.$emit("closeDialogAndRefresh");
          } else {
            this.$message({ message: "通过失败！", type: "error" });
          }
        });
      });
    },

    /**
     * 审核驳回
     */
    auditReject(remark) {
      this.form.applyStatus = -1;
      this.form.remark = remark;
      auditDataApply(this.form).then((res) => {
        if (res.data) {
          this.$message({ message: "驳回成功！", type: "success" });
          this.$emit("closeDialogAndRefresh");
        } else {
          this.$message({ message: "驳回失败！", type: "error" });
        }
      });
    },

    formatBoolean(row, column, cellValue) {
      if (cellValue) {
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
  width: 100%;
  height: 500px;
  .wrap {
    width: 100%;
    height: calc(100% - 72px);
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
  text-align: center;
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
::v-deep .el-table td.diffValue {
  color: red;
}
</style>
