<!--
 * @Author: zhongg
 * @LastEditTime: 2022-07-06 15:31:56
 * @Description: 标签管理
 * @FilePath: \lucheng_project\src\views\propertyManage\archivesManage\authorityManage\LabelManage.vue
-->
<template>
  <div class="wrap label_manage">
    <div class="table_input">
      <span
        class="label_add"
        @click="addLabelAction"
        v-permission="'LabelManage:add'"
        ><i></i>新增标签</span
      >
      <div style="float: right">
        <primary-table
          ref="labelTable"
          title=""
          :hideAdd="true"
          @doSearch="doSearch"
        >
        </primary-table>
      </div>
    </div>
    <div ref="tableBox" class="table-box table_wrap">
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        style="width: 100%"
        class="deep-table"
        border
        :header-row-style="{ height: '43px' }"
      >
        <el-table-column type="index" label="序号" width="50"></el-table-column>
        <el-table-column prop="name" label="标签名称"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <span
              class="edBtn"
              @click="operateLabel(scope.row.id)"
              v-permission="'LabelManage:edit'"
              >编辑</span
            >
            <span
              class="edBtn"
              @click="deleLabel(scope.row.id)"
              v-permission="'LabelManage:del'"
              >删除</span
            >
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination_area">
        <el-pagination
          class="right"
          background
          layout="total,prev, pager, next"
          :total="total"
          :page-sizes="[10, 20, 30, 40]"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </div>

    <!-- 新增标签表单dialog -->
    <el-dialog
      class="list-dialog"
      :visible.sync="labelDialog"
      width="50%"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <div slot="title" class="el-dialog__title">
        <strong>{{ dialogTitle }}</strong>
      </div>
      <labelManage-form
        ref="labelManageForm"
        @addLabel="addLabel"
        @cancelDialog="handleClose"
      ></labelManage-form>
    </el-dialog>
  </div>
</template>

<script>
import PrimaryTable from "../primary_table/index";
import LabelManageForm from "./components/LabelManageForm";
import {
  getLabelDataApi,
  addLabelApi,
  deleteLabelApi,
} from "@/api/propertyManage";
export default {
  name: "LabelManage",
  props: {},
  components: {
    PrimaryTable,
    LabelManageForm,
  },
  data() {
    return {
      loading: false,
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      dialogTitle: "新增标签",
      labelDialog: false,
    };
  },
  mounted() {
    this.getLabelData();
  },
  methods: {
    async getLabelData() {
      const name = this.$refs.labelTable.searchName;
      let res = await getLabelDataApi({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: name,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.total = res.data.total;
      }
    },

    doSearch() {
      this.getLabelData();
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getLabelData();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getLabelData();
    },
    handleClose() {
      this.$refs.labelManageForm.resetForm();
      this.labelDialog = false;
    },
    addLabelAction() {
      //新增
      this.dialogTitle = "新增标签";
      this.labelDialog = true;
    },
    async addLabel(data) {
      //保存新增或修改
      await addLabelApi(data).then((res) => {
        if (res.code == 0) {
          this.$message({
            message: "保存成功",
            type: "success",
          });
          this.handleClose();
          this.getLabelData();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    operateLabel(id) {
      //修改
      this.dialogTitle = "编辑标签";
      this.labelDialog = true;
      this.$nextTick().then(() => {
        this.$refs.labelManageForm.getFormData(id);
      });
    },
    deleLabel(id) {
      //删除
      this.$confirm("此操作将删除该标签, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          await deleteLabelApi({ id }).then((res) => {
            if (res.code == 0) {
              this.$message({
                message: "删除成功",
                type: "success",
              });
              this.getLabelData();
            } else {
              this.$message.error(res.msg);
            }
          });
        })
        .catch(() => {});
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  .table_input {
    height: 36px;
    background: #fff;
    border: 1px solid #e6e6e6;
    padding: 12px 16px;
    .wrap {
      padding: 0;
    }
    span {
      font-size: 14px;
      line-height: 24px;
      padding: 6px 12px;
      margin-left: 15px;
      background: #f5f5f5;
      border-radius: 4px;
      float: right;
      cursor: pointer;
      i {
        width: 24px;
        display: inline-block;
        height: 24px;
        margin-top: -2px;
        margin-right: 8px;
        vertical-align: middle;
      }
    }
    span.label_add i {
      background: url("../../../../assets/images/add.png") no-repeat center;
      background-size: 100% 100%;
    }
  }
  .table_wrap {
    width: 100%;
    height: calc(100% - 72px);
    background: #fff;
    border: 1px solid #e6e6e6;
    margin-top: 8px;
    padding: 12px;
    overflow-y: auto;
    box-sizing: border-box;
  }
}
.pagination_area {
  width: 100%;
  height: 32px;
  background: #f8f8f8;
  border: 1px solid #d6d6d6;
  border-top: 0;
  .el-pagination {
    height: 100%;
    padding: 0;
    ::v-deep button,
    ::v-deep ul {
      height: 30px;
      line-height: 30px;
    }
    ::v-deep .el-pager {
      margin-top: 3px;
      li {
        height: 24px;
        min-width: 24px;
        line-height: 24px;
        border: 1px solid #d6d6d6;
        border-radius: 4px;
        margin: 0 4px 0 0;
      }
    }
    ::v-deep span:not([class*="suffix"]) {
      margin-top: 2px;
      vertical-align: middle;
    }
    ::v-deep .el-input__inner {
      background: transparent;
      border: 1px solid #1492ff;
      border-radius: 4px;
      font-size: 12px;
      color: rgba(0, 0, 0, 0.88);
    }
    ::v-deep .el-select .el-input {
      width: 80px;
      // width: 200px;
    }
    ::v-deep .btn-prev i,
    ::v-deep .btn-next i {
      width: 24px;
      line-height: 22px;
      border: 1px solid #d6d6d6;
      border-radius: 4px;
      margin: 0;
    }
    .el-pagination__sizes {
      margin: 0;
    }
  }
}
.list-dialog {
  ::v-deep .el-dialog__header {
    background: #1492ff;
    padding: 15px 20px;
    .el-dialog__title {
      color: #fff;
    }
    .el-dialog__headerbtn {
      top: 5px;
      right: 5px;
      padding-top: 10px;
      .el-icon-close {
        color: #fff;
        height: 30px;
        width: 35px;
      }
    }
  }
  ::v-deep .el-dialog__body {
    padding: 15px 20px;
    .el-input__inner {
      background-color: #fff;
      border: 0.0625rem solid #dcdfe6;
      color: #606266;
    }
  }
  .el-select {
    width: 100%;
  }
  .el-date-editor.el-input,
  .el-date-editor.el-input__inner {
    width: 100%;
  }
}
</style>
<style lang='scss'>
.label_manage .table_input .el-button {
  margin-right: -20px;
}
.label_manage .table_input .el-input {
  margin-right: 0;
}
.label_manage .table_input .wrap .table-view-head {
  height: 38px;
}
.edBtn {
  color: #01a59c;
  margin: 0 5px;
  cursor: pointer;
}
</style>

