<template>
  <div class="primary-dialog waterIntake-detail-table">
    <el-dialog
      top="10vh"
      width="1200px"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="waterIntake-detail-table"
      append-to-body
      :fullscreen="fullscreen"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">取水户详表</p>
        <i @click="changeFullscreen" :class="['el-icon-full-screen','full-screen-icon']"></i>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div class="statistics-sty">
          <div>
            <p><i class="icon-shuiyuandi qushuihu-dialog-count-1"></i></p>
            <div>
              <p>取水户总数</p>
              <p>
                <span>
                  {{
                    wtaerIntakeCount.intakeSum &&
                    wtaerIntakeCount.intakeSum.toFixed(0)
                  }}
                </span>
                户
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi qushuihu-dialog-count-2"></i></p>
            <div>
              <p>正常</p>
              <p>
                <span style="color: #19af47">
                  {{ wtaerIntakeCount.intakeNorm }}
                </span>
                户
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi qushuihu-dialog-count-3"></i></p>
            <div>
              <p>接近许可</p>
              <p>
                <span style="color: #008fe8">
                  {{ wtaerIntakeCount.intakeNearPermit }}
                </span>
                户
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi qushuihu-dialog-count-4"></i></p>
            <div>
              <p>超许可</p>
              <p>
                <span style="color: #e89100">
                  {{ wtaerIntakeCount.intakeOverPermit }}
                </span>
                户
              </p>
            </div>
          </div>
        </div>
        <div class="query-sty">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-select v-model="typeValue" size="small" @input="changeType">
                <el-option label="取水户" value="取水户" />
                <el-option label="取水量统计" value="取水量统计" />
              </el-select>
            </el-col>
            <el-col :span="6" v-show="typeValue == '取水户'">
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="administrationValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
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
            <el-col :span="6" v-show="typeValue == '取水户'">
              <div class="demo-input-suffix">
                <p style="width: 100px">取水类型：</p>
                <el-select
                  v-model="wtaerIntakeType"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="getWaterIntakeList"
                >
                  <el-option
                    v-for="item in wtaerIntakeTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6" v-show="typeValue == '取水户'">
              <div class="demo-input-suffix" style="line-height: 32px">
                <p style="width: 60px">报警：</p>
                <el-radio-group
                  v-model="wtaerIntakeWarnType"
                  @change="getWaterIntakeList"
                >
                  <el-radio class="el-radio" :label="true">是</el-radio>
                  <el-radio class="el-radio" :label="false">否</el-radio>
                </el-radio-group>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="tab-list-sty">
          <el-table
            ref="multipleTable"
            :data="wtaerIntakeData"
            height="100%"
            stripe
            class="abc"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <template v-if="typeValue == '取水户'">
              <el-table-column
                :show-overflow-tooltip="true"
                prop="wiuName"
                label="名称"
                width="180"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="toFixedFormatter"
                prop="areaName"
                label="行政区划"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="toFixedFormatter"
                prop="wiuType"
                label="取水类型"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="toFixedFormatter"
                prop="intake"
                label="当年取水量(万m³)"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="toFixedFormatter"
                prop="intakePermit"
                label="许可取水量(万m³)"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="toFixedFormatter"
                prop="legRepr"
                label="法人代表"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="toFixedFormatter"
                prop="legPhone"
                label="联系电话"
              />
            </template>
            <template v-if="typeValue == '取水量统计'">
              
              <!-- <el-table-column
                :show-overflow-tooltip="true"
                prop="wiuName"
                label="年份"
              /> -->
              
              <el-table-column
                :show-overflow-tooltip="true"
                prop="wiuName"
                label="许可取水量"
                :formatter="Formatter"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="intakeState"
                label="状态"
              >
                <template slot-scope="scope">
                  <span v-if="scope.row.intakeState == 1">正常</span>
                  <span v-if="scope.row.intakeState == 2">接近许可</span>
                  <span v-if="scope.row.intakeState == 3">超许可</span>
                </template>
              </el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="intakePermit"
                label="当前取水量"
                :formatter="Formatter"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="adnm"
                label="行政区划"
                width="180"
              />
            </template>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { disasterPreventionApi } from "@/api/RightSildePanel";
export default {
  name: "WaterIntakeDetail",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      wtaerIntakeType: "",
      wtaerIntakeTypeOption: [
        { label: "全部", value: "" },
        { label: "既用又取", value: "既用又取" },
        { label: "只取不用", value: "只取不用" },
        { label: "只用不取", value: "只用不取" },
      ],
      wtaerIntakeCount: {},
      wtaerIntakeData: [],
      wtaerIntakeWarnType: "",
      //行政区划选中值
      administrationValue: "",
      administration: [],
      loading: false,
      typeValue: "取水户",
      fullscreen:false,
    }
  },
  computed: {
  },
  mounted() {
    this.getArea();
    this.getWaterIntakeList();
    this.getStats();
  },
  methods: {
    changeFullscreen(){
      this.fullscreen=!this.fullscreen;
      this.$refs.multipleTable.doLayout();
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
    handleClose() {
      this.$emit("close");
    },
    async getStats() {
      await waterResourceApi.stat4ListWiu().then((res) => {
        this.wtaerIntakeCount = res.data;
      });
    },
    selectVal() {
      this.getWaterIntakeList();
    },
    // 取水户
    async getWaterIntakeList() {
      let opt = {
        areaCode: this.administrationValue,
        warning: this.wtaerIntakeWarnType,
        wiuType: this.wtaerIntakeType,
      };
      this.loading = true;
      await waterResourceApi.listWiu(opt).then((res) => {
        this.wtaerIntakeData = res.data;
        this.loading = false;
      });
    },
    /**
     * 水库字段排序
     */
    sortChange(val) {
      if (val.order) {
        if (val.order == "ascending") {
          this.orderColumn = val.prop;
          this.ascOrDesc = false;
        } else {
          this.orderColumn = val.prop;
          this.ascOrDesc = true;
        }
      } else {
        this.orderColumn = "";
        this.ascOrDesc = "";
      }
    },
    /**
     * 表格数据格式化
     */
    toFixedFormatter(row, column, value, index) {
      if (value != null) {
        return value;
      } else {
        return "-";
      }
    },
    async wiuWarnByAdcd() {
      this.loading = true;
      await waterResourceApi.wiuWarnByAdcd().then(res => {
        if (res.code == 0) {
          this.wtaerIntakeData = res.data;
        }
        this.loading = false;
      });
    },
    changeType() {
      if (this.typeValue == '取水户') {
        this.getWaterIntakeList();
      } else {
        this.wiuWarnByAdcd();
      }
    },
    Formatter(row, column, value, index) {
      // if (!value || value == "") return "-";
      if (value != null) {
        let valueFixed = String(value);
        if (valueFixed.split(",").length > 1) {
          valueFixed = valueFixed.split(",")[0];
        }
        valueFixed = Number(valueFixed).toFixed(1);
        return valueFixed;
      } else {
        return "-"
      }
    },
  }
}
</script>

<style lang="scss">
.mg-l16 {
  margin-left: 16px;
}
.fr {
  float: right;
}
.waterIntake-detail-table {
  .icon-shuiyuandi {
    background-size: 100% 100%;
    display: inline-block;
    width: 48px;
    height: 48px;
  }
  .icon-shuiyuandi-1 {
    background-image: url(~assets/images/shuiyuandi-1.png);
  }
  .icon-shuiyuandi-2 {
    background-image: url(~assets/images/shuiyuandi-2.png);
  }
  .icon-shuiyuandi-3 {
    background-image: url(~assets/images/shuiyuandi-3.png);
  }
  .icon-shuiyuandi-4 {
    background-image: url(~assets/images/shuiyuandi-4.png);
  }

  .qushuihu-dialog-count-1 {
    background-image: url(~assets/images/qushuihu-dialog-count-1.png);
  }
  .qushuihu-dialog-count-2 {
    background-image: url(~assets/images/qushuihu-dialog-count-2.png);
  }
  .qushuihu-dialog-count-3 {
    background-image: url(~assets/images/qushuihu-dialog-count-3.png);
  }
  .qushuihu-dialog-count-4 {
    background-image: url(~assets/images/qushuihu-dialog-count-4.png);
  }

  .el-dialog {
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
      // min-height: 6.25rem;
      background-image: linear-gradient(180deg, #1c3357 0%, #0b2045 100%);
      border: 0.0625rem solid #00c1ff;
      border-top: 0;
      margin: 0 0.125rem;
      @include flex(1);

      .primary-dialog__content {
        @include flexbox;
        @include flexflow(column, nowrap);
        height:100%;
        // height: 680px;
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