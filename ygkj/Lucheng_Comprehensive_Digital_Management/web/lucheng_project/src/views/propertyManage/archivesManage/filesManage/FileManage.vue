<template>
  <div class="fileManage_box">
    <div class="file_wrap" v-if="condition == 'fileManage'">
      <div class="file_wrap_left">
        <FileTree ref="fileTree" @nodeClick="getFileNodeClick" @getTableData="getFileNodeClick"></FileTree>
      </div>
      <div class="file_wrap_right">
        <div class="right_top">
          <div class="table_input">
            <!-- <span
              class="file_upload"
              v-if="nodeData.uploadFlag == '1'"
              @click="fileUpload()"
              ><i></i>文件上传</span
            > -->
            <span class="file_upload" v-permission="'filesIntegration:upload'" @click="fileUpload()"
              ><i></i>文件上传</span
            >
            <span class="global_search" @click="globalSearch"><i></i>全局搜索</span>
            <div style="float: right">
              <primary-table ref="fileTable" title="" :hideAdd="true" @doSearch="doSearch"> </primary-table>
            </div>
          </div>
          <!-- <map-tab :list="tabList" v-model="tabActive" @input="handleMapTab"></map-tab> -->
        </div>
        <div class="right_mid">
          <el-collapse-transition>
            <div class="filter_box" v-show="filterShow">
              <div
                class="filter_item"
                :class="[tagHeight > 42 && tagShow ? 'filterMore' : '', tagHeight > 42 ? 'filterPadding' : '']"
              >
                <span>相关标签:</span>
                <el-checkbox-group v-model="checkboxGroup1" ref="tagTypeRef" @change="getFileList">
                  <el-checkbox-button v-for="tag in tagOptions" :label="tag.id" :key="tag.id">{{
                    tag.name
                  }}</el-checkbox-button>
                </el-checkbox-group>
                <i v-show="tagHeight > 42" :class="tagShow ? 'active' : ''" @click="tagShow = !tagShow"
                  ><b v-if="tagShow">收起</b><b v-if="!tagShow">展开</b></i
                >
              </div>
              <div class="filter_item">
                <span>文件类型:</span>
                <el-checkbox-group v-model="checkboxGroup2" @change="getFileList">
                  <el-checkbox-button v-for="item in fileTypeOptions" :label="item.value" :key="item.value">{{
                    item.name
                  }}</el-checkbox-button>
                </el-checkbox-group>
              </div>
              <div class="filter_item">
                <span>时间选择:</span>
                <el-checkbox-group v-model="checkboxGroup3" @change="getFileList">
                  <el-checkbox-button v-for="year in yearOptions" :label="year" :key="year">{{
                    year
                  }}</el-checkbox-button>
                </el-checkbox-group>
              </div>
            </div>
          </el-collapse-transition>
          <div class="filter_arrow">
            <span @click="filterShow = !filterShow">
              <i :class="[!filterShow && 'active']"></i>
              <b v-if="filterShow">收起</b>
              <b v-if="!filterShow">展开</b>
            </span>
          </div>
        </div>
        <!-- <div class="bulk_operat">
        <span class="bulk_del"><i></i>删除</span>
        <span class="bulk_down"><i></i>下载</span>
      </div> -->
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
            <el-table-column prop="collectionFlag" label="收藏状态" width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.collectionFlag == 0" class="unfavorite" @click="unfavorite(scope.row.id)"
                  ><i></i>已收藏</span
                >
                <span v-if="scope.row.collectionFlag == 1" class="collectFile" @click="collectFile(scope.row.id)"
                  ><i></i>收藏</span
                >
              </template>
            </el-table-column>
            <el-table-column prop="fileNo" label="归档编号"></el-table-column>
            <el-table-column prop="fileName" label="文件名称">
              <template slot-scope="scope">
                <span
                  v-if="['gif', 'jpeg', 'jpg', 'png', 'pdf'].includes(scope.row.fileType)"
                  style="color: #0392fe; cursor: pointer"
                  @click="lookFile(scope.row.fileUrl, scope.row.id)"
                  >{{ scope.row.fileName }}</span
                >
                <span v-else>{{ scope.row.fileName }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="fileType" label="文件类型" width="80"></el-table-column>
            <el-table-column prop="achievementName" label="目录归属"></el-table-column>
            <el-table-column prop="createTime" label="上传时间"></el-table-column>
            <el-table-column prop="browseNum" label="访问记录" width="80">
              <template slot-scope="scope">
                <span
                  style="color: #0392fe; cursor: pointer"
                  @click="accessRecords(scope.row.id, scope.row.fileName)"
                  >{{ scope.row.browseNum }}</span
                >
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template slot-scope="scope">
                <span class="edBtn" v-permission="'filesIntegration:edit'" @click="editFile(scope.row.id)">编辑</span>
                <!-- <span
                  class="edBtn"
                  v-if="nodeData.downloadFlag == '1'"
                  @click="uploadFile(scope.row.id, scope.row.fileUrl)"
                  >下载</span
                >
                <span
                  class="edBtn"
                  v-if="nodeData.deleteFlag == '1'"
                  @click="deleFile(scope.row.id)"
                  >删除</span
                > -->
                <span
                  v-show="['pdf', 'png', 'jpg'].includes(scope.row.fileType)"
                  class="edBtn"
                  @click="uploadFile(scope.row.id, scope.row.fileUrl)"
                  v-permission="'filesIntegration:look'"
                  >查看</span
                >
                <span
                  v-show="!['pdf', 'png', 'jpg'].includes(scope.row.fileType)"
                  class="edBtn"
                  @click="uploadFile(scope.row.id, scope.row.fileUrl)"
                  v-permission="'filesIntegration:down'"
                  >下载</span
                >
                <span class="edBtn" @click="deleFile(scope.row.id)" v-permission="'filesIntegration:del'">删除</span>
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
            />
          </div>
        </div>
      </div>

      <!-- 上传文件dialog -->
      <el-dialog
        class="list-dialog"
        :visible.sync="fileUploadDialog"
        width="50%"
        :before-close="handleClose"
        :close-on-click-modal="false"
      >
        <div slot="title" class="el-dialog__title">
          <strong>上传文件</strong>
        </div>
        <FileManageForm ref="fileManageForm" @cancelDialog="handleClose" />
      </el-dialog>

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
        <FileManageEdit
          v-if="fileEditDialog"
          ref="fileManageEdit"
          :achievementId="achievementId"
          @cancelDialog="editClose"
        />
      </el-dialog>
    </div>
    <template v-if="condition == 'fileLog'">
      <FileLog ref="fileLog" @cancelFileLog="cancelFileLog" />
    </template>
  </div>
</template>

<script>
  import FileTree from './components/FileTree';
  import PrimaryTable from '../primary_table/index';
  import FileManageForm from './components/FileManageForm';
  import FileManageEdit from './components/FileManageEdit';
  import FileLog from './FileLog';
  import {
    lookFileLogApi,
    getLabelDataApi,
    getFileTypeDataApi,
    getUpFileListApi,
    collectFileApi,
    unfavoriteApi,
    deleteUpFileApi
  } from '@/api/propertyManage';

  export default {
    name: 'FileManage',
    props: {},
    watch: {
      tagOptions: function () {
        this.$nextTick(function () {
          let cur = this.$refs['tagTypeRef'];
          if (cur) {
            this.tagHeight = cur.clientHeight;
          }
        });
      }
    },
    components: { FileTree, PrimaryTable, FileManageForm, FileManageEdit, FileLog },
    data() {
      return {
        loading: false,
        // tabActive: 0,
        // tabList: [
        //   { title: "所有文件", value: '1' },
        //   { title: "文本文件", value: '2' },
        //   { title: "图像文件", value: '3' },
        //   { title: "视频影像文件", value: '4' },
        //   { title: "zip压缩文件", value: '5' },
        //   { title: "cad文件", value: '6' },
        //   { title: "其他文件", value: '7' }
        // ],
        // condition: '1',
        condition: 'fileManage',
        checkboxGroup1: [],
        checkboxGroup2: [],
        checkboxGroup3: [],
        tagOptions: [],
        fileTypeOptions: [
          {
            name: '档案文件',
            value: 'pdf,xls,xlsx,docx,doc,txt'
          },
          {
            name: '图像文件',
            value: 'gif,jpg,jpeg,png'
          },
          {
            name: '视频影像文件',
            value: 'mp4,avi'
          },
          {
            name: 'zip文件',
            value: 'zip,rar'
          },
          {
            name: 'cad文件',
            value: 'cad'
          },
          {
            name: '其他文件',
            value: 'other'
          }
        ],
        yearOptions: [],
        filterShow: true,
        tagShow: false,
        tagHeight: 50,
        tableData: [],
        pageNum: 1,
        pageSize: 10,
        total: 0,
        fileUploadDialog: false,
        fileEditDialog: false,
        achievementId: '',
        nodeData: ''
      };
    },
    mounted() {
      this.getTagLabelData();
      this.getFileTypeData();
    },
    methods: {
      accessRecords(fileLogId, fileName) {
        this.condition = 'fileLog';
        this.$nextTick().then(() => {
          this.$refs.fileLog.getFileLogId(fileLogId, fileName);
        });
      },
      cancelFileLog() {
        this.condition = 'fileManage';
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
            // this.fileTypeOptions = res.data.types;
            this.yearOptions = res.data.times;
          }
        });
      },
      //获取文件列表
      async getFileList() {
        const name = this.$refs.fileTable.searchName;
        let otherType = this.checkboxGroup2.includes('other') ? true : false;

        await getUpFileListApi({
          achievementId: this.achievementId == 0 ? '' : this.achievementId,
          fileName: name,
          labelIds: this.checkboxGroup1.join('|'),
          fileType: this.checkboxGroup2.join(','),
          year: this.checkboxGroup3.join(','),
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          otherType: otherType
        }).then((res) => {
          if (res.code == 0) {
            if (res.data.list.length > 0) {
              res.data.list.map((item) => {
                if (item.achievementName) {
                  let achievementName = item.achievementName.split('/');
                  if (achievementName.length == 1) {
                    item.achievementName = achievementName.join('');
                  } else if (!achievementName[achievementName.length - 1]) {
                    achievementName.pop();
                    console.log(achievementName);
                    item.achievementName = achievementName.join('/');
                  } else {
                    item.achievementName = achievementName.join('/');
                  }
                }
              });
            }
            this.tableData = res.data.list;
            this.total = res.data.total;
          }
        });
      },
      //获取选中树节点
      getFileNodeClick(id, data) {
        this.achievementId = id;
        this.nodeData = data;
        console.log('this.nodeData: ', this.nodeData);
        //console.log(this.nodeData);
        this.getFileList();
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
      globalSearch() {
        this.$router.push('/propertyManage/globalSearch');
      },
      fileUpload() {
        //文件上传
        this.fileUploadDialog = true;
        this.$nextTick().then(() => {
          this.$refs.fileManageForm.getFormData(this.achievementId);
        });
      },
      handleClose() {
        this.$refs.fileManageForm.resetForm();
        this.fileUploadDialog = false;
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
      async uploadFile(id, fileUrl) {
        console.log(fileUrl, 'fileUrl');
        window.open(fileUrl);
        // window.open(
        //   config.baseUrl +
        //     "/authentication-system/ums/sysFile/downloadFile?id=" +
        //     id
        // );
        // await downloadFileApi({id});
      },
      lookFile(fileUrl, id) {
        lookFileLogApi({
          id: id
        });
        window.open(fileUrl);
      },
      //收藏
      async collectFile(id) {
        await collectFileApi({
          achievementId: this.achievementId,
          fileId: id
        }).then((res) => {
          if (res.code == 0) {
            this.$message({
              message: '收藏成功',
              type: 'success'
            });
            this.getFileList();
          } else {
            this.$message.error(res.msg);
          }
        });
      },
      //取消收藏
      async unfavorite(id) {
        await unfavoriteApi({
          achievementId: this.achievementId,
          fileId: id
        }).then((res) => {
          if (res.code == 0) {
            this.$message({
              message: '取消收藏成功',
              type: 'success'
            });
            this.getFileList();
          } else {
            this.$message.error(res.msg);
          }
        });
      },
      deleFile(id) {
        //删除
        this.$confirm('此操作将删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(async () => {
            await deleteUpFileApi({ id }).then((res) => {
              if (res.code == 0) {
                this.$message({
                  message: '删除成功',
                  type: 'success'
                });
                this.getFileList();
              } else {
                this.$message.error(res.msg);
              }
            });
          })
          .catch(() => {});
      }
    }
  };
</script>

<style lang="scss" scoped>
  .fileManage_box {
    width: 100%;
    height: 100%;
    box-sizing: border-box;
  }
  .file_wrap {
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    padding: 16px;
    @include flexbox();
    @include flexJC(space-between);
    > .file_wrap_left {
      width: 256px;
      height: auto;
      border: 1px solid #e6e6e6;
      background: #fff;
      box-sizing: border-box;
      padding: 16px;
    }
    > .file_wrap_right {
      width: calc(100% - 264px);
      height: 100%;
      box-sizing: border-box;
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
          span.global_search i {
            background: url('../../../../assets/images/ss.png') no-repeat center;
            background-size: 100% 100%;
          }
          span.file_upload i {
            background: url('../../../../assets/images/add.png') no-repeat center;
            background-size: 100% 100%;
          }
        }
      }
      .right_mid {
        border: 1px solid #e6e6e6;
        background: #fff;
        margin-top: 8px;
        .filter_box {
          // height: 158px;
          overflow-y: auto;
          .filter_item {
            height: 50px;
            overflow: hidden;
            position: relative;
            color: #858585;
            padding: 4px 12px;
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
              background: url('../../../../assets/images/tag_arrow1.png') no-repeat right center;
              cursor: pointer;
              b {
                font-weight: normal;
              }
            }
            i.active {
              background: url('../../../../assets/images/tag_arrow2.png') no-repeat right center;
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
              background: url('../../../../assets/images/menu_arrow.png') no-repeat center;
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
      .bulk_operat {
        height: 56px;
        border: 1px solid #e6e6e6;
        background: #fff;
        margin-top: 8px;
        padding: 10px 16px;
        span {
          font-size: 14px;
          height: 36px;
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
        span.bulk_down i {
          background: url('../../../../assets/images/bulk_down.png') no-repeat center;
          background-size: 100% 100%;
        }
        span.bulk_del i {
          background: url('../../../../assets/images/bulk_del.png') no-repeat center;
          background-size: 100% 100%;
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
        box-sizing: border-box;
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
            background: url('../../../../assets/images/unfavorite_icon.png') no-repeat center;
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
            background: url('../../../../assets/images/collectFile_icon.png') no-repeat center;
            background-size: 100% 100%;
          }
        }
      }
      .table_wrap.active {
        height: calc(100% - 116px);
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
      ::v-deep span:not([class*='suffix']) {
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
<style lang="scss">
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
