<template>
  <div
    :class="['primary-dialog', leftSide == true ? 'project-dialog-wrap' : '']"
  >
    <el-dialog
      :visible.sync="visible"
      :width="width"
      :before-close="handleClose"
      destroy-on-close
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      :fullscreen="fullscreen"
    >
      <!-- 标题 -->
      <div
        class="primary-dialog__title"
        slot="title"
        :style="minimizeStatus ? 'justify-content: space-between;' : ''"
      >
        <p class="primary-dialog__title--label" style="color: #fff">
          水研究详情
        </p>
        <i
          @click="changeFullscreen"
          :class="['el-icon-full-screen', 'full-screen-icon']"
        ></i>
      </div>
      <!-- 内容 -->
      <div class="primary-dialog__content">
        <el-row style="height:100%">
          <el-col class="content-box" :span="24" style="height:100%">
            <div class="results-show-sty">
              <div class="card-sty">
                <p style="margin-right: 15px">
                  <i class="icon-cheng-guo-shu" />
                </p>
                <div style="margin-right: 20px">
                  <p>研究成果总数</p>
                  <p style="margin-top: 10px">
                    <span style="font-size: 24px; color: #a80bee">
                      {{ overviewList.achievementNums }}
                    </span>
                    个
                  </p>
                </div>
                <p style="margin-right: 15px">
                  <i class="icon-yajiu-jingfei" />
                </p>
                <div>
                  <p>研究经费总计</p>
                  <p style="margin-top: 10px">
                    <span style="font-size: 24px; color: #56fefe">
                      {{ overviewList.totalResearchFund }}
                    </span>
                    万元
                  </p>
                </div>
              </div>
            </div>
            <!-- 中间卡片 -->
            <el-row style="margin: 15px 0">
              <el-col :span="6">
                <span style="color: #ffffff">研究类型：</span>
                <el-select
                  v-model="researchTypeId"
                  placeholder="请选择"
                  @change="researchChange(researchTypeId)"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </el-col>
              <el-col :span="6">
                <div class="demo-input-suffix">
                  <p style="width: 80px">关键字：</p>
                  <el-input
                    style="width: 160px"
                    v-model="searchVal"
                    placeholder="请输入内容"
                    @input="busSubLoadPage"
                  ></el-input>
                  <div
                    class="derive-sty mg-l16"
                    style="width: 80px"
                    @click="busSubLoadPage"
                  >
                    查 询
                  </div>
                </div>
              </el-col>
            </el-row>
            <!-- 列表 -->
            <div class="table-box">
              <el-table
                ref="multipleTable"
                :data="realTimeData"
                height="100%"
                stripe
                width="100%"
              >
                <el-table-column prop="index" label="序号" width="55">
                  <template slot-scope="scope">
                    <span class="table-order">{{ scope.$index + 1 }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="researchName"
                  label="研究名称"
                />
                <el-table-column
                  prop="particularYear"
                  label="年份"
                  width="100"
                />
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="personLiable"
                  label="负责人"
                  width="100"
                />
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="undertakUnit"
                  label="承担单位"
                  width="160"
                />
                <el-table-column prop="photoUrl" label="成果图片">
                  <template slot-scope="scope">
                    <i
                      class="el-icon-picture-outline"
                      style="color: #00c1ff; font-size: 20px"
                      @click="photograph(scope.row)"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="fileUrl" label="研究报告">
                  <template slot-scope="scope">
                    <i
                      class="el-icon-paperclip"
                      style="color: #00c1ff; font-size: 20px"
                      @click="fileDialogShow(scope.row.fileUrl)"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="researchResult" label="研究成果转化">
                  <template slot-scope="scope">
                    <el-popover
                      placement="top-start"
                      width="270"
                      trigger="hover"
                    >
                      <template>
                        <div>
                          <div
                            style="
                              padding: 10px 5px;
                              background: #00c1ff;
                              display: flex;
                              justify-content: left;
                            "
                          >
                            <p style="margin-right: 10px; width: 30px">序号</p>
                            <p style="margin-right: 10px; width: 30px">类型</p>
                            <p style="margin-right: 10px; width: 120px">名称</p>
                            <p style="width: 30px">附件</p>
                          </div>
                          <div>
                            <div
                              style="
                                padding: 10px;
                                display: flex;
                                justify-content: left;
                              "
                              v-for="(item, index) in scope.row.achRespVoList"
                              :key="index"
                            >
                              <p style="margin-right: 10px; width: 30px">
                                {{ index + 1 }}
                              </p>
                              <p style="margin-right: 10px; width: 30px">
                                {{ item.type }}
                              </p>
                              <p style="margin-right: 10px; width: 120px">
                                {{ item.name }}
                              </p>
                              <p>
                                <i
                                  class="el-icon-paperclip"
                                  style="color: #00c1ff; font-size: 20px"
                                  @click="fileDialog(item.url)"
                                />
                              </p>
                            </div>
                          </div>
                        </div>
                      </template>
                      <span slot="reference" style="color: #00c1ff"
                        >转化列表</span
                      >
                    </el-popover>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <!-- 分页 -->
            <div class="pagination-area" style="padding-bottom:12px">
              <el-pagination
                class="right"
                @current-change="pageChange"
                background
                layout="total, prev, pager, next"
                :total="total"
              />
            </div>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    <!-- 图片弹窗 -->
    <PictureBouncedDialog
      v-if="photographShow"
      :visible="photographShow"
      :photoUrl="photoUrl"
      @close="closePhotograph"
    />
    <!-- 文件预览 -->
    <FilePreviewDialog
      v-if="filePrevieShow"
      :visible="filePrevieShow"
      :fileList="fileList"
      @close="closeFilePreview"
    />
  </div>
</template>

<script>
import { programmeApi } from "@/api/programme_api/index.js";
import PictureBouncedDialog from "./PictureBouncedDialog.vue";
import FilePreviewDialog from "./FilePreviewDialog.vue";
export default {
  name: "PrimaryDialog",
  components: {
    PictureBouncedDialog,
    FilePreviewDialog
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    width: {
      type: String,
      default: "1600px",
    },
    leftSide: {
      type: Boolean,
      default: false,
    },
    minimizeStatus: {
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      total: 0,
      pageNum: 1,
      pageSize: 10,
      realTimeData: [],
      photographShow: false,
      filePrevieShow: false,
      photoUrl: '',
      fileList: '',
      researchTypeId: '',
      options: [],
      overviewList: {},
      searchVal: "",
      fullscreen: false,
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.busSubLoadPage();
      this.loadPage();
      this.trendOverviewAchievements();     //成果总览
    })
  },
  methods: {
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      this.$nextTick(() => {
        this.$refs.multipleTable.doLayout();
      })
    },
    fileDialogShow(val) {
      if (val != '[]') {
        this.fileList = JSON.parse(val)[0].url;
        this.filePrevieShow = true;
      } else {
        this.filePrevieShow = false;
        this.$message.error("暂无附件信息！");
      }
    },
    // 文件预览
    fileDialog(val) {
      if (val) {
        this.fileList = val;
        this.filePrevieShow = true;
      } else {
        this.filePrevieShow = false;
        this.$message.error("暂无附件信息！");
      }
    },
    //弹窗关闭
    closeFilePreview() {
      this.filePrevieShow = false;
    },
    // 图片弹框
    photograph(val) {
      if (val) {
        this.photoUrl = val.photoUrl;
        this.photographShow = true;
      } else {
        this.photographShow = false;
        this.$message.error("暂无图片信息！");
      }
    },
    // 关闭图片弹窗
    closePhotograph() {
      this.photographShow = false;
    },
    // 研究类别
    researchChange(val) {
      this.researchTypeId = val;
      this.busSubLoadPage();
    },
    // 分页
    pageChange(page) {
      this.pageNum = page;
      this.busSubLoadPage();
    },
    //项目列表
    async busSubLoadPage() {
      let obj = {
        researchType: this.researchTypeId,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        researchName: this.searchVal,            //规划信息名称
      }
      await programmeApi.busSubLoadPage(obj).then((res) => {
        if (res.code == 0) {
          this.total = res.data.totalRows;
          this.realTimeData = res.data.list;
        }
      })
    },
    // 趋势分析 -- 成果分析
    async trendOverviewAchievements() {
      this.overviewList = {};
      await programmeApi.trendOverviewAchievements().then((res) => {
        if (res.code == 0) {
          this.overviewList = res.data;
        }
      })
    },
    // 研究类别
    async loadPage() {
      await programmeApi.loadPage().then((res) => {
        if (res.code == 0) {
          let str = {
            id: '',
            name: '全部'
          }
          res.data.list.unshift(str);
          this.options = res.data.list;
        }
      })
    },
    handleClose() {
      this.$emit("close");
    }
  }
}
</script>

<style lang="scss" scoped>
.primary-dialog {
  ::v-deep .el-dialog {
    background: transparent;
    @include flexbox;
    @include flexflow(column, nowrap);
    height: 680px;
    &.is-fullscreen {
      height: 100%;
      overflow: hidden;
    }
    .el-dialog__header {
      // background: linear-gradient(to bottom, #35b9e3, #5398f6) left top
      //     no-repeat,
      //   linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
      //   linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
      //   linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat;
      background-size: 18px 2px, 2px 18px;
      padding: 2px;
      // padding-bottom: 0;
      height: 54px;
      .primary-dialog__title {
        // border: 1px solid #00c1ff;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1f375f 0%, #1b3047 100%);
        // border-bottom: 0;
        height: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 40px;
        position: relative;
        .primary-dialog__title--label {
          background: url(~assets/images/WaterDisastersImage/title-bg.png)
            no-repeat;
          // background-size: 100% 100%;
          font-size: 16px;
          color: #00c1ff;
          letter-spacing: 0;
          height: 32px;
          width: 80%;
          margin-left: 16px;
          padding-left: 56px;
          font-weight: bold;
          line-height: 32px;
        }
        .full-screen-icon {
          position: absolute;
          right: 40px;
          color: #00c1ff;
          cursor: pointer;
          top: 15px;
        }
        .primary-dialog__title--minimize {
          cursor: pointer;
          margin-right: 48px;
        }
      }
      .el-dialog__headerbtn {
        .el-dialog__close {
          color: #00c1ff;
          font-weight: bold;
        }
      }
    }
    .el-dialog__body {
      // background: linear-gradient(to bottom, #5398f6, #35b9e3) left bottom
      //     no-repeat,
      //   linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
      //   linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
      //   linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
      background-size: 18px 2px, 2px 18px;
      border-bottom: 0.0625rem solid #00c1ff;
      padding: 2px;
      padding-top: 0;
      @include flex(1);
      .primary-dialog__content {
        padding: 0 15px;
        // height: 630px;
        @include flexbox;
        @include flexflow(column, nowrap);
        height: 100%;
        overflow-y: auto;
        overflow-x: hidden;
        // border: 1px solid #00c1ff;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
        border-top: 0;
        .el-date-table td.current:not(.disabled) span,
        .el-pagination.is-background .el-pager li:not(.disabled).active,
        .el-date-table td.end-date span,
        .el-date-table td.start-date span {
          background-color: #00c1ff;
        }
        .content-box{
          @include flexbox;
          @include flexflow(column,nowrap);
        }
        .demo-input-suffix {
          @include flexbox;
          @include flexAC;
          color: #fff;
          .derive-sty {
            padding: 0 10px;
            text-align: center;
            border-radius: 4px;
            line-height: 30px;
            float: right;
            color: #fff;
            cursor: pointer;
            border-color: linear-gradient(
              180deg,
              #36c0e4 20%,
              #0639ff 100%
            ) !important;
            background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
          }
        }
        .results-show-sty {
          margin-top: 10px;
          display: flex;
          padding-bottom: 10px;
          border-bottom: 1px solid rgba($color: #00c1ff, $alpha: 0.2);
          justify-content: space-between;
          > p:nth-child(1) {
            margin-top: 10px;
            width: 200px;
            height: 26px;
            font-family: PingFangSC-Semibold;
            font-size: 24px;
            color: #328173;
            letter-spacing: 0;
            font-weight: 600;
            padding-left: 10px;
            border-left: 4px solid #328173;
          }
          > div {
            display: flex;
            justify-content: space-between;
            > p {
              display: flex;
              margin-left: 10px;
              justify-content: space-between;
              > span {
                width: 100px;
                height: 44px;
                line-height: 44px;
              }
            }
            .el-input__inner {
              color: #333333;
              border: 0;
              height: 40px;
              background: #dee3e4;
              border-radius: 4px;
            }
          }
        }
        .orientation {
          position: absolute;
          right: 20px;
          top: 145px;
        }
        .card-sty {
          > div {
            color: #fff;
          }
          // padding: 20px 10px 0 10px;
          // display: flex;
          // justify-content: space-between;
          // >div:nth-child(1) {
          //   // width: 475px;
          //   // height: 151.56px;
          //   // background: url(../../../../assets/images/research-result.png) no-repeat;
          //   background-size: 100% 100%;
          //   padding: 45px 0 0 150px;
          //   > p {
          //     height: 28px;
          //     font-family: PingFangSC-Regular;
          //     font-size: 20px;
          //     color: #FFFFFF;
          //     letter-spacing: 0;
          //     font-weight: 400;
          //   }
          //   > p:nth-child(2) {
          //     > span {
          //       font-size: 40px;
          //     }
          //   }
          // }
          // >div:nth-child(2) {
          //   width: 475px;
          //   height: 151.56px;
          //   // background: url(../../../../assets/images/the-document.png) no-repeat;
          //   background-size: 100% 100%;
          //   padding: 45px 0 0 150px;
          //   > p {
          //     height: 28px;
          //     font-family: PingFangSC-Regular;
          //     font-size: 20px;
          //     color: #FFFFFF;
          //     letter-spacing: 0;
          //     font-weight: 400;
          //   }
          //   > p:nth-child(2) {
          //     > span {
          //       font-size: 40px;
          //     }
          //   }
          // }
        }
        .tab-sty {
          color: #fff;
          height: 26px;
          font-weight: 500;
          margin-right: 20px;
          padding-bottom: 5px;
        }
        .switch-sty {
          height: 26px;
          font-weight: 500;
          margin-right: 20px;
          padding-bottom: 5px;
          color: #56fefe;
          border-bottom: 2px solid #56fefe;
        }
        .test-sty {
          width: 20px;
          height: 10px;
          display: inline-block;
          margin-right: 10px;
        }
        .table-box{
          @include flex(1);
          overflow: auto;
        }
      }
    }
  }
  &.project-dialog-wrap {
    ::v-deep .el-dialog {
      margin-left: 20px;
      margin-top: 10vh !important;
    }
  }
}
.peripheral-analysis {
  display: inline-block;
  margin-left: 10px;
  cursor: pointer;
  // background: url(~assets/images/peripheral_analysis.png) no-repeat;
  background-size: 100% 100%;
  width: 15px;
  height: 15px;
}
.minimize {
  color: #00c1ff;
  font-weight: bold;
  cursor: pointer;
}
</style>