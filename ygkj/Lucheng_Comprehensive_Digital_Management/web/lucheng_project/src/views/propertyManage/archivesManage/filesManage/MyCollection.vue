<template>
  <div class="fileWrap_box">
    <div class="file_wrap" v-if="condition == 'fileManage'">
      <div class="right_top">
        <div class="table_input">
          <div style="float: right">
            <primary-table
              ref="fileTable"
              title=""
              :hideAdd="true"
              @doSearch="doSearch"
            >
            </primary-table>
          </div>
        </div>
      </div>
      <div class="right_mid">
        <el-collapse-transition>
          <div class="filter_box" v-show="filterShow">
            <div
              class="filter_item"
              :class="[
                tagHeight > 42 && tagShow ? 'filterMore' : '',
                tagHeight > 42 ? 'filterPadding' : '',
              ]"
            >
              <span>相关标签:</span>
              <el-checkbox-group
                v-model="checkboxGroup1"
                ref="tagTypeRef"
                @change="getFileList"
              >
                <el-checkbox-button
                  v-for="tag in tagOptions"
                  :label="tag.id"
                  :key="tag.id"
                  >{{ tag.name }}</el-checkbox-button
                >
              </el-checkbox-group>
              <i
                v-show="tagHeight > 42"
                :class="tagShow ? 'active' : ''"
                @click="tagShow = !tagShow"
                ><b v-if="tagShow">收起</b><b v-if="!tagShow">展开</b></i
              >
            </div>
            <div class="filter_item">
              <span>文件类型:</span>
              <el-checkbox-group v-model="checkboxGroup2" @change="getFileList">
                <el-checkbox-button
                  v-for="fileType in fileTypeOptions"
                  :label="fileType"
                  :key="fileType"
                  >{{ fileType }}</el-checkbox-button
                >
              </el-checkbox-group>
            </div>
            <div class="filter_item">
              <span>时间选择:</span>
              <el-checkbox-group v-model="checkboxGroup3" @change="getFileList">
                <el-checkbox-button
                  v-for="year in yearOptions"
                  :label="year"
                  :key="year"
                  >{{ year }}</el-checkbox-button
                >
              </el-checkbox-group>
            </div>
          </div>
        </el-collapse-transition>
        <div class="filter_arrow">
          <span @click="filterShow = !filterShow">
            <i :class="!filterShow ? 'active' : ''"></i
            ><b v-if="filterShow">收起</b><b v-if="!filterShow">展开</b>
          </span>
        </div>
      </div>
      <div class="table_wrap" :class="!filterShow ? 'active' : ''">
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
          <el-table-column prop="collectionFlag" label="收藏状态">
            <template slot-scope="scope">
              <span
                class="unfavorite"
                @click="unfavorite(scope.row.id, scope.row.collectionId)"
                ><i></i>已收藏</span
              >
            </template>
          </el-table-column>
          <el-table-column prop="id" label="归档编号"></el-table-column>
          <el-table-column prop="fileName" label="文件名称">
            <template slot-scope="scope">
              <span
                v-if="
                  scope.row.fileType == 'gif' ||
                  scope.row.fileType == 'jpeg' ||
                  scope.row.fileType == 'jpg' ||
                  scope.row.fileType == 'png' ||
                  scope.row.fileType == 'pdf'
                "
                style="color: #0392fe; cursor: pointer"
                @click="lookFile(scope.row.fileUrl)"
                >{{ scope.row.fileName }}</span
              >
              <span v-else>{{ scope.row.fileName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="fileType" label="文件类型"></el-table-column>
          <el-table-column
            prop="achievementName"
            label="目录归属"
          ></el-table-column>
          <el-table-column prop="createTime" label="上传时间"></el-table-column>
          <el-table-column prop="browseNum" label="访问记录">
            <template slot-scope="scope">
              <span
                style="color: #0392fe; cursor: pointer"
                @click="accessRecords(scope.row.id, scope.row.fileName)"
                >{{ scope.row.browseNum }}</span
              >
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <span class="edBtn" @click="editFile(scope.row.id)" v-permission="'myCollection:edit'">编辑</span>
              <span
                class="edBtn"
                v-if="scope.row.downloadFlag == '1'"
                @click="uploadFile(scope.row.id, scope.row.fileUrl)"
                v-permission="'myCollection:down'"
                >下载</span
              >
              <span
                class="edBtn"
                v-if="scope.row.deleteFlag == '1'"
                @click="deleFile(scope.row.id)"
                v-permission="'myCollection:del'"
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

      <!-- 文件编辑dialog -->
      <el-dialog
        class="list-dialog"
        :visible.sync="fileEditDialog"
        width="50%"
        :before-close="editClose"
        :close-on-click-modal="false"
      >
        <div slot="title" class="el-dialog__title">
          <strong>文件编辑</strong>
        </div>
        <fileManage-edit
          ref="fileManageEdit"
          @cancelDialog="editClose"
        ></fileManage-edit>
      </el-dialog>
    </div>
    <template v-if="condition == 'fileLog'">
      <file-log ref="fileLog" @cancelFileLog="cancelFileLog"></file-log>
    </template>
  </div>
</template>

<script>
import PrimaryTable from "../primary_table/index";
import config from "@/config/config";
import FileManageEdit from "./components/FileManageEdit";
import FileLog from "./FileLog";
import {
  getLabelDataApi,
  getFileTypeDataApi,
  getCollectFileListApi,
  unCollectApi,
  downloadFileApi,
  deleteUpFileApi,
} from "@/api/propertyManage";
export default {
  name: "MyCollection",
  props: {},
  watch: {
    tagOptions: function () {
      this.$nextTick(function () {
        let cur = this.$refs["tagTypeRef"];
        if (cur) {
          this.tagHeight = cur.clientHeight;
        }
      });
    },
  },
  components: {
    PrimaryTable,
    FileManageEdit,
    FileLog,
  },
  data() {
    return {
      loading: false,
      condition: "fileManage",
      checkboxGroup1: [],
      checkboxGroup2: [],
      checkboxGroup3: [],
      tagOptions: [],
      fileTypeOptions: [],
      yearOptions: [],
      filterShow: true,
      tagShow: false,
      tagHeight: 50,
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      fileEditDialog: false,
    };
  },
  mounted() {
    this.getFileList();
    this.getTagLabelData();
    this.getFileTypeData();
  },
  methods: {
    accessRecords(fileLogId, fileName) {
      this.condition = "fileLog";
      this.$nextTick().then(() => {
        this.$refs.fileLog.getFileLogId(fileLogId, fileName);
      });
    },
    cancelFileLog() {
      this.condition = "fileManage";
    },
    //获取标签
    async getTagLabelData() {
      await getLabelDataApi({ fetchAll: true }).then((res) => {
        if (res.code == 0) {
          this.tagOptions = res.data.list;
        }
      });
    },
    //获取文件类型
    async getFileTypeData() {
      await getFileTypeDataApi({ fetchAll: true }).then((res) => {
        if (res.code == 0) {
          this.fileTypeOptions = res.data.types;
          this.yearOptions = res.data.times;
        }
      });
    },
    //获取文件列表
    async getFileList() {
      const name = this.$refs.fileTable.searchName;
      await getCollectFileListApi({
        fileName: name,
        labelIds: this.checkboxGroup1.join("|"),
        fileType: this.checkboxGroup2.join(","),
        year: this.checkboxGroup3.join(","),
        pageSize: this.pageSize,
        pageNum: this.pageNum,
      }).then((res) => {
        if (res.code == 0) {
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      });
    },

    handleSizeChange(val) {
      this.pageSize = val;
      this.getFileList();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getFileList();
    },
    doSearch() {
      this.getFileList();
    },

    editFile(id) {
      //文件编辑
      this.fileEditDialog = true;
      this.$nextTick().then(() => {
        this.$refs.fileManageEdit.getFormData(id);
      });
    },
    editClose() {
      this.$refs.fileManageEdit.resetForm();
      this.fileEditDialog = false;
      this.getFileList();
    },
    uploadFile(id, fileUrl) {
      window.open(
        config.baseUrl +
          "/authentication-system/ums/sysFile/downloadFile?id=" +
          id
      );
      //await downloadFileApi({id});
    },
    lookFile(fileUrl) {
      window.open(fileUrl);
    },
    //取消收藏
    async unfavorite(id, collectionId) {
      await unCollectApi({
        id: collectionId,
      }).then((res) => {
        if (res.code == 0) {
          this.$message({
            message: "取消收藏成功",
            type: "success",
          });
          this.getFileList();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    deleFile(id) {
      //删除
      this.$confirm("此操作将删除该文件, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          await deleteUpFileApi({ id }).then((res) => {
            if (res.code == 0) {
              this.$message({
                message: "删除成功",
                type: "success",
              });
              this.getFileList();
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
.fileWrap_box {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}
.file_wrap {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  padding: 16px;
  .right_top {
    background: #fff;
    .table_input {
      height: 36px;
      background: #fff;
      border: 1px solid #e6e6e6;
      padding: 12px 16px;
      .wrap {
        padding: 0;
      }
    }
  }
  .right_mid {
    border: 1px solid #e6e6e6;
    background: #fff;
    margin-top: 8px;
    .filter_box {
      overflow-y: auto;
      .filter_item {
        height: 50px;
        overflow: hidden;
        position: relative;
        color: #858585;
        padding: 4px 16px;
        border-bottom: 1px dashed #f4f4f4;
        span {
          font-size: 14px;
          line-height: 40px;
          float: left;
        }
        .el-checkbox-group {
          padding: 0 0 0 72px;
          .el-checkbox-button {
            margin: 4px 6px;
          }
        }
        i {
          position: absolute;
          right: 8px;
          top: 18px;
          font-size: 14px;
          font-style: normal;
          color: #09a8a0;
          padding-right: 20px;
          background: url("../../../../assets/images/tag_arrow1.png") no-repeat
            right center;
          cursor: pointer;
          b {
            font-weight: normal;
          }
        }
        i.active {
          background: url("../../../../assets/images/tag_arrow2.png") no-repeat
            right center;
        }
      }
      .filter_item.filterMore {
        height: auto;
      }
      .filter_item.filterPadding {
        .el-checkbox-group {
          padding: 0 72px;
        }
      }
    }
    .filter_arrow {
      text-align: center;
      span {
        display: inline-block;
        font-size: 14px;
        color: #858585;
        line-height: 32px;
        cursor: pointer;
        i {
          display: inline-block;
          width: 24px;
          height: 24px;
          vertical-align: middle;
          margin-top: -2px;
          margin-right: 3px;
          background: url("../../../../assets/images/menu_arrow.png") no-repeat
            center;
          background-size: 100% 100%;
        }
        b {
          font-weight: normal;
        }
        i.active {
          transform: rotate(180deg);
          -ms-transform: rotate(180deg); /* IE 9 */
          -moz-transform: rotate(180deg); /* Firefox */
          -webkit-transform: rotate(180deg); /* Safari 和 Chrome */
          -o-transform: rotate(180deg);
        }
      }
    }
  }
  .table_wrap {
    width: 100%;
    height: calc(100% - 274px);
    border: 1px solid #e6e6e6;
    background: #fff;
    margin-top: 8px;
    padding: 8px;
    overflow-y: auto;
    .unfavorite {
      font-size: 14px;
      color: #f7b500;
      padding: 0 6px;
      border: 1px solid #f7b500;
      border-radius: 4px;
      cursor: pointer;
      i {
        display: inline-block;
        width: 16px;
        height: 16px;
        vertical-align: middle;
        margin-top: -2px;
        margin-right: 3px;
        background: url("../../../../assets/images/unfavorite_icon.png") no-repeat
          center;
        background-size: 100% 100%;
      }
    }
    .collectFile {
      font-size: 14px;
      color: #8c8c8c;
      padding: 0 6px;
      border: 1px solid #8c8c8c;
      border-radius: 4px;
      cursor: pointer;
      i {
        display: inline-block;
        width: 16px;
        height: 16px;
        vertical-align: middle;
        margin-top: -2px;
        margin-right: 3px;
        background: url("../../../../assets/images/collectFile_icon.png") no-repeat
          center;
        background-size: 100% 100%;
      }
    }
  }
  .table_wrap.active {
    height: calc(100% - 116px);
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
</style>
<style lang='scss'>
.file_wrap .table_input .el-button {
  margin-right: -20px;
}
.file_wrap .table_input .el-input {
  margin-right: 0;
}
.file_wrap .tabBlock {
  border: none;
  padding: 0 16px;
}
.file_wrap .tabBlock .tabContent::before {
  height: 0;
}
.file_wrap .tabBlock .tab {
  height: 48px;
}
.file_wrap .el-checkbox-button__inner {
  min-width: 60px;
  border-radius: 4px;
  padding: 10px 12px;
  border-left: 1px solid #dcdfe6;
}
.file_wrap .el-checkbox-button:first-child .el-checkbox-button__inner {
  border-radius: 4px;
}
.file_wrap .el-checkbox-button:last-child .el-checkbox-button__inner {
  border-radius: 4px;
}
.file_wrap .el-checkbox-button.is-checked .el-checkbox-button__inner {
  background: #01a59c;
}
.edBtn {
  color: #01a59c;
  margin: 0 5px;
  cursor: pointer;
}
</style>
