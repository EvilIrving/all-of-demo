<template>
  <div class="primary-dialog sluice-detail-table">
    <el-dialog
      top="10vh"
      width="1200px"
      destroy-on-close
      v-loading="exportLoading"
			element-loading-text="拼命导出中。。。"
			element-loading-spinner="el-icon-loading"
			element-loading-background="rgba(0, 0, 0, 0.8)"
      :visible.sync="visible"
      :before-close="handleClose"
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="sluice-detail-table"
      append-to-body
      :fullscreen="fullscreen"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">灌区详表</p>
        <i @click="changeFullscreen" :class="['el-icon-full-screen','full-screen-icon']"></i>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div class="statistics-sty">
          <div>
            <p><i class="icon-hewang-shuliang-tongji"></i></p>
            <div>
              <p>灌区总数</p>
              <p>
                <span> {{ statisticObj.irrNum }} </span> 个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-chao-xunxian"></i></p>
            <div>
              <p>设计灌溉面积</p>
              <p>
                <span>{{ statisticObj.desIrrArea }} </span> k㎡
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-chao-xunxian"></i></p>
            <div>
              <p>有效灌溉面积</p>
              <p>
                <span>{{ statisticObj.effIrrArea }} </span> k㎡
              </p>
            </div>
          </div>
        </div>
        <div class="query-sty">
          <el-row>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 70px">行政区划：</p>
                <el-select
                  v-model="areaVal"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="getList"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adcd"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 70px">工程规模：</p>
                <el-select
                  @change="getList"
                  v-model="engScal"
                  size="small"
                  clearable
                >
                  <el-option label="大型" value="1,2" />
                  <el-option label="中型" value="3" />
                  <el-option label="小型" value="4,5" />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 50px">流域：</p>
                <el-select
                  @change="getList"
                  v-model="basVal"
                  size="small"
                  clearable
                >
                  <el-option label="飞云江" value="飞云江" />
                  <el-option label="瓯江" value="瓯江" />
                  <el-option label="鳌江" value="鳌江" />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 80px">关键字：</p>
                <el-input v-model="searchVal" style="width: 150px" placeholder="请输入内容" @input="getList" />
                <div class="search-sty" style="margin-left: 10px; width: 74px" @click="getList"> 搜 索 </div>
                <div class="search-sty" style="margin-left: 10px; width: 74px" @click="exportExcel">导 出</div>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="tab-list-sty">
          <el-table
            ref="multipleTable"
            :data="tableData"
            height="100%"
            stripe
            class="abc"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="irrName"
              label="灌区名称"
              width="160"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="engScal"
              label="工程规模"
              width="100"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="desIrrArea"
              label="设计灌溉面积(k㎡)"
              width="180"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="effIrrArea"
              label="有效灌溉面积(k㎡)"
              width="180"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="manUnit"
              label="管理单位"
              width="160"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="行政区划"
              width="100"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="bas"
              label="流域"
              width="80"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="managementLevel"
              label="管理层级"
            />
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import axios from "axios";
  import config from "@/config/config";
  import { localData } from "@/plugins/storage";
  import { hydraulicApi } from "@/api/hydraulic_amap";
  import { disasterPreventionApi } from "@/api/RightSildePanel";
  export default {
    name: "IrrigateDetail",
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
      type: {
        type: String,
        default: "灌区",
      },
    },
    data() {
      return {
        // 导出Loading
			  exportLoading: false,
        statisticObj: {
          irrNum: "",
          effIrrArea: "",
          desIrrArea: ""
        },
        areaVal: "",
        //行政区划
        administration: [],
        engScal: "",
        searchVal: "",
        tableData: [],
        loading: false,
        orderColumn: "",
        ascOrDesc: "",
        basVal: "",
        fullscreen:false,
      }
    },
    computed: {
    },
    mounted() {
      this.getArea();
      this.getList();
      this.getStats();
    },
    methods: {
      changeFullscreen(){
        this.fullscreen=!this.fullscreen;
        this.$refs.multipleTable.doLayout();
      },
      // 导出
        async exportExcel() {
          this.exportLoading = true;
          let obj = {
            type: "灌区",
            areaCode: this.areaVal,
            projectName: this.searchVal,
            bas: this.basVal,
            levels: this.engScal
          };
          let userInfo = localData("get", "userInfo");
          let { accessToken } = userInfo ? userInfo : { accessToken: "" };
          axios({
            url: `${config.baseUrl}/project-manage/newProjectManage/exportIrrProject`,
            method: "post",
            data: obj,
            responseType: "blob",
            headers: {
              authorization: accessToken,
            },
          }).then((res) => {
            const content = res.data;
            const blob = new Blob([content], { type: "application/msword" });
            const link = document.createElement("a"); // 创建a标签
            let filename = res.headers["content-disposition"];
            filename = filename ? filename.substr(filename.indexOf("=") + 1) : "";
            filename = decodeURIComponent(filename);
            link.download = filename;
            link.style.display = "none";
            link.href = URL.createObjectURL(blob);
            document.body.appendChild(link);
            link.click(); // 执行下载
            URL.revokeObjectURL(link.href); // 释放url
            document.body.removeChild(link); // 释放标签
            this.exportLoading = false;
          }).catch(error => {
            this.exportLoading = false;
          });
        },
      /**
       * 行政区划
       */
      async getArea() {
        let obj = {
          areaCode: 3303,
          level: 3,
        };
        await disasterPreventionApi.getArea(obj).then((res) => {
          if (res.code == 0) {
            this.administration = res.data;
          }
        });
      },
      async getList() {
        const opt = {
          type: "灌区",
          areaCode: this.areaVal,
          projectName: this.searchVal,
          bas: this.basVal,
          levels: this.engScal
        }
        await hydraulicApi.projectsDetailList(opt).then((res) => {
          if (res.code == 0) {
            this.tableData = res.data;
          }
        });
      },
      handleClose() {
        this.$emit("close");
      },
      async getStats() {
        const opt = {
          type: '灌区'
        }
        await hydraulicApi.stats4ProjectList(opt).then(res => {
          if (res.code == 0) {
            this.statisticObj = res.data;
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
.mg-l16 {
  margin-left: 16px;
}
.fr {
  float: right;
}
.sluice-detail-table {
  ::v-deep .el-dialog {
    background: transparent;
    @include flexbox;
		@include flexflow(column,nowrap);
    height: 680px;
    &.is-fullscreen{
			height:100%;
			overflow: hidden;
		}
    .el-dialog__header {
      padding: 2px;
      height: 54px;
      border: 1px solid #00c1ff;
      box-shadow: inset 0 0.25rem 1.875rem 0 #2351ef40;
      background-image: linear-gradient(180deg, #1f375f 0%, #1b3047 100%);
      border-bottom: 0;
      margin: 0.125rem 0.125rem 0;
      .primary-dialog__title {
        position: relative;
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
        .full-screen-icon{
					position:absolute;
					right:40px;
					color:#00c1ff;
          cursor: pointer;
          top:15px;
				}
      }
    }
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
      .primary-dialog__title--label {
        background: url(~assets/images/WaterDisastersImage/title-bg.png)
          no-repeat;
        // background-size: 100% 100%;
        width: 80%;
        height: 32px;
        color: #fff;
        font-size: 16px;
        letter-spacing: 0;
        margin-left: 15px;
        font-weight: bold;
        line-height: 35px;
        padding-left: 56px;
      }
      .primary-dialog__title--minimize {
        cursor: pointer;
        margin-right: 48px;
      }
    }
    .el-dialog__body {
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      min-height: 6.25rem;
      background-image: linear-gradient(180deg, #1c3357 0%, #0b2045 100%);
      border: 0.0625rem solid #00c1ff;
      border-top: 0;
      margin: 0 0.125rem;
      @include flex(1);

      .primary-dialog__content {
        @include flexbox;
        @include flexflow(column, nowrap);
        // height: 680px;
        height:100%;
        border-top: 0;
        padding: 0 16px;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);

        // 统计样式
        .statistics-sty {
          padding: 10px 0;
          width: 100%;
          display: flex;
          justify-content: left;
          border-bottom: 1px solid rgba($color: #409eff, $alpha: 0.4);
          > div {
            // margin-right: 50px;
            padding: 10px 0;
            margin-right: 20px;
            display: flex;
            justify-content: space-between;
            > p {
              padding-top: 5px;
              margin-right: 15px;
            }
            > div {
              > p:nth-child(1) {
                font-family: PingFangSC-Regular;
                font-size: 15px;
                color: #ffffff;
                letter-spacing: 0;
                line-height: 24px;
                font-weight: 400;
                margin-bottom: 5px;
              }
              > p:nth-child(2) {
                font-family: PingFangSC-Medium;
                font-size: 14px;
                color: #fff;
                letter-spacing: 0;
                line-height: 24px;
                font-weight: 500;
                > span {
                  font-size: 24px;
                  color: #56fefe;
                }
              }
            }
          }
        }
        // 查询样式
        .query-sty {
          // padding: 15px 0;
          margin: 10px 0;
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
          .aa {
            margin-right: 20px;
            margin-bottom: 10px;
          }
          .demo-input-suffix {
            display: flex;
            justify-content: left;
            align-items: center;
          }
          .search-sty {
            padding: 0px;
            text-align: center;
            border-radius: 4px;
            width: 100px;
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

        .tab-list-sty {
          @include flex(1);
          padding-bottom: 20px;
          overflow: auto;
        }
      }
    }
  }
  .el-input-number {
    width: 5.25rem;
  }
  .el-input__inner:hover,
  .el-select:hover .el-input__inner {
    border: 1px solid #fff;
  }
  .el-input__inner {
    height: 2rem;
    background: rgba(24, 47, 90, 0.1);
    color: #fff;
    border: 1px solid #fff;
  }
  .el-select .el-input .el-select__caret {
    color: #fff;
  }
  input::-webkit-input-placeholder {
    color: #fff;
  }
  .dialog-footer {
    .el-button--primary.is-plain {
      color: #fff;
      border-color: #0bbbff;
      background-color: rgba($color: #0bbbff, $alpha: 0.1);
    }
  }
  .el-table .warm-row .cell {
    color: #eec80b !important;
  }
  .abc .el-table__body tr:hover {
    background-color: rgba($color: #56fefe, $alpha: 0.4);
  }
  // 弹窗底部按钮样式
  .dialog-footer {
    position: absolute;
    right: 20px;
    bottom: 15px;
    .el-button--primary.is-plain {
      color: #fff;
      border-color: #0bbbff;
      background-color: rgba($color: #0bbbff, $alpha: 0.1);
    }
  }
  .el-dialog .el-table {
    th {
      color: #1cfff5;
      padding: 3px 0;
      font-size: 14px;
      background-color: rgba(41, 147, 180, 0.1);
      font-weight: bold;
      .cell {
        height: 37px;
        line-height: 37px;
      }
    }
  }

  .el-table .ascending .sort-caret.ascending {
    border-bottom-color: #1cfff5;
  }
  .el-table .descending .sort-caret.descending {
    border-top-color: #1cfff5;
  }
  .myClass .el-input__suffix {
    margin-top: -3px;
    padding: 0;
  }
  .el-date-editor .el-range-input {
    background: none;
    color: #fff;
  }
  .el-date-editor .el-range-separator {
    color: #fff;
    line-height: 1.5rem;
  }
  .el-date-editor .el-range__icon {
    color: #fff;
    line-height: 1.4rem;
  }
  .el-date-editor .el-range__close-icon {
    color: #fff;
    line-height: 1.4rem;
  }
  .el-date-editor .el-icon-date {
    line-height: 1;
  }
  .detail-table {
    .el-table__body-wrapper::-webkit-scrollbar {
      height: 6px; // 纵向滚动条 必写
      width: 6px;
    }
  }
  .tb-top-panel {
    margin-top: 10px;
    color: #fff;
    display: flex;
    align-items: center;
    i {
      float: left;
    }
    p {
      flex: 1;
      font-size: 15px;
      margin-left: 10px;
      background-color: rgba(255, 255, 255, 0.1);
      padding: 8px 10px;
      border-radius: 4px;
    }
    span {
      color: #7ccfff;
      font-size: 20px;
    }
    .laba {
      width: 24px;
      height: 24px;
      background: url(~assets/images/laba.png) no-repeat;
      background-size: 100%;
    }
  }

  .is-checked .el-checkbox__label {
    color: #70adf8;
  }
  .el-radio {
    margin-right: 15px;
  }
  .el-radio__label {
    color: #fff;
  }
  .is-checked .el-checkbox__inner {
    border-color: #70adf8;
    color: #70adf8;
    &::after {
      border-color: #70adf8;
    }
  }
  .water-supply-select-panel,
  .beauty-tab-panel {
    display: flex;
    margin-bottom: 20px;
  }
  .beauty-tab-panel {
    margin-bottom: 0;
  }
  .water-supply-select-btn,
  .beauty-tab-item {
    padding: 10px 20px;
    margin-right: 20px;
    color: #fff;
    border: 1px solid rgba(255, 255, 255, 0.6);
    cursor: pointer;
    &.active {
      color: #56fefe;
      background: rgba(72, 152, 251, 0.1);
      border: 1px solid #56fefe;
      box-shadow: inset 0 0 5px 0 #56fefe;
    }
  }

  .pagination {
    margin-top: 15px;
    text-align: right;
  }
  .el-pagination {
    .el-pagination__total {
      color: #fff;
    }
    .el-pager {
      li {
        color: #fff;
        background: rgba(0, 0, 0, 0);
        &.active {
          background-color: #409eff !important;
        }
      }
    }
    .btn-prev,
    .btn-next {
      background: rgba(0, 0, 0, 0);
    }
  }
}
</style>