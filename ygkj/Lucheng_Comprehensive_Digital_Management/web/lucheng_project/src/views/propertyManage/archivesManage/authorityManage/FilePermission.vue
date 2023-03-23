<!--
 * @Author: zhongg
 * @LastEditTime: 2022-07-06 15:31:16
 * @Description: 权限管理
 * @FilePath: \lucheng_project\src\views\propertyManage\archivesManage\authorityManage\FilePermission.vue
-->
<template>
  <div class="wrap file_permission">
    <div class="wrap_left">
      <FileTree
        ref="fileTree"
        @nodeClick="getDirectoryList"
        @getTableData="getDirectoryList"
      ></FileTree>
    </div>
    <div class="wrap_right">
      <div class="table_input">
        <!-- <span class="authority_manage" @click="addDirectoryAction"><i></i>权限管理</span> -->
        <span
          class="directory_add"
          @click="addDirectoryAction"
          v-permission="'FilePermission:add'"
          ><i></i>新增</span
        >
        <div style="float: right">
          <primary-table
            ref="directoryTable"
            title=""
            :hideAdd="true"
            @doSearch="doSearch"
          >
          </primary-table>
        </div>
      </div>
      <div class="table_wrap">
        <el-table
          v-loading="loading"
          :data="tableData"
          stripe
          style="width: 100%"
          class="deep-table"
          border
          :header-row-style="{ height: '43px' }"
        >
          <el-table-column
            type="index"
            label="序号"
            width="50"
          ></el-table-column>
          <el-table-column prop="name" label="名称"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <!-- <span class="edBtn" @click="operateConfig(scope.row.id)"
                >权限管理</span
              > -->
              <span
                class="edBtn"
                @click="operateDirectory(scope.row.id)"
                v-permission="'FilePermission:edit'"
                >编辑</span
              >
              <span
                class="edBtn"
                @click="deleDirectory(scope.row.id)"
                v-permission="'FilePermission:del'"
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
    </div>

    <!-- 新增标签表单dialog -->
    <el-dialog
      class="list-dialog"
      :visible.sync="directoryDialog"
      width="50%"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <div slot="title" class="el-dialog__title">
        <strong>{{ dialogTitle }}</strong>
      </div>
      <directory-form
        v-if="directoryDialog"
        :fileId="fileId"
        :projectCode="projectCode"
        :topId="topId"
        ref="directoryForm"
        @addDirectory="addDirectory"
        @cancelDialog="handleClose"
      ></directory-form>
    </el-dialog>

    <!-- 权限管理表单dialog -->
    <el-dialog
      class="list-dialog"
      :visible.sync="configDialog"
      width="50%"
      :before-close="resetForm"
      :close-on-click-modal="false"
    >
      <div slot="title" class="el-dialog__title">
        <strong>权限管理</strong>
      </div>
      <authority-tab
        ref="authorityTab"
        @cancelDialog="configClose"
      ></authority-tab>
    </el-dialog>
  </div>
</template>

<script>
import FileTree from "../filesManage/components/FileTree";
import PrimaryTable from "../primary_table/index";
import DirectoryForm from "./components/DirectoryForm";
import AuthorityTab from "./components/AuthorityTab";
import {
  getDirectoryListApi,
  addDirectoryApi,
  deleteDirectoryApi,
} from "@/api/propertyManage";
export default {
  name: "FilePermission",
  props: {},
  components: {
    FileTree,
    PrimaryTable,
    DirectoryForm,
    AuthorityTab,
  },
  data() {
    return {
      loading: false,
      fileId: "",
      projectCode: "",
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      dialogTitle: "新增文档目录",
      directoryDialog: false,
      configDialog: false,
      topId: "-1",
    };
  },
  mounted() {
    //this.getDirectoryList();
  },
  methods: {
    async getList() {
      const name = this.$refs.directoryTable.searchName;
      let res = await getDirectoryListApi({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: name,
        parentId: this.fileId,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.total = res.data.total;
      }
    },
    async getDirectoryList(selectFileId, selectFileName) {
      if (selectFileId) {
        this.fileId = selectFileId;
      }
      if (selectFileName.parentId == "-1") {
        this.topId = selectFileId;
      } else {
        this.topId = "-1";
      }
      this.projectCode = selectFileName.projectCode;
      this.getList();
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    doSearch() {
      this.getList();
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getList();
    },
    handleClose() {
      this.$refs.directoryForm.resetForm();
      this.directoryDialog = false;
    },

    configClose() {
      this.configDialog = false;
      this.getDirectoryList();
    },
    resetForm() {
      this.$refs.authorityTab.resetForm();
    },
    operateConfig(id) {
      //配置
      this.configDialog = true;
      this.$nextTick().then(() => {
        this.$refs.authorityTab.getFormId(id);
      });
    },
    addDirectoryAction() {
      //新增
      this.dialogTitle = "新增文档目录";
      this.directoryDialog = true;
      this.$nextTick().then(() => {
        this.$refs.directoryForm.getParentData(this.fileId);
      });
    },
    async addDirectory(data) {
      //保存新增或修改
      await addDirectoryApi(data).then((res) => {
        if (res.code == 0) {
          this.$message({
            message: "保存成功",
            type: "success",
          });
          this.handleClose();
          this.$refs.fileTree._getFileList();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    operateDirectory(id) {
      //修改
      this.dialogTitle = "编辑文档目录";
      this.directoryDialog = true;
      this.$nextTick().then(() => {
        this.$refs.directoryForm.getFormData(id, this.fileId);
      });
    },
    deleDirectory(id) {
      //删除
      this.$confirm("此操作将删除该文档目录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          await deleteDirectoryApi({ id }).then((res) => {
            if (res.code == 0) {
              this.$message({
                message: "删除成功",
                type: "success",
              });
              this.$refs.fileTree._getFileList();
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
.file_permission {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  @include flexbox();
  @include flexJC(space-between);
  > .wrap_left {
    width: 256px;
    height: auto;
    border: 1px solid #e6e6e6;
    background: #fff;
    box-sizing: border-box;
    padding: 16px;
  }
  > .wrap_right {
    box-sizing: border-box;
    width: calc(100% - 264px);
    height: 100%;
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
      span.directory_add i {
        background: url("../../../../assets/images/add.png") no-repeat center;
        background-size: 100% 100%;
      }
      span.authority_manage i {
        background: url("../../../../assets/images/authority_manage.png")
          no-repeat center;
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
.file_permission .table_input .el-button {
  margin-right: -20px;
}
.file_permission .table_input .el-input {
  margin-right: 0;
}
.file_permission .table_input .wrap .table-view-head {
  height: 38px;
}
.edBtn {
  color: #01a59c;
  margin: 0 5px;
  cursor: pointer;
}
</style>

