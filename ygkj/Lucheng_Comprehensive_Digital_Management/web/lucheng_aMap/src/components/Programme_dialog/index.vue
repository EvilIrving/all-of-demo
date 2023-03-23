<template>
  <div :class="[leftSide == true ? 'project-dialog-wrap' : '']">
    <el-dialog
      :visible.sync="visible"
      :width="width"
      :before-close="handleClose"
      :modal="modal"
      :append-to-body="appendToBody"
      :fullscreen="fullscreen"
      :close-on-click-modal="false"
      class="primary-dialog"
    >
      <div
        class="primary-dialog__title"
        slot="title"
        :style="minimizeStatus ? 'justify-content: space-between;' : ''"
      >
        <p class="primary-dialog__title--label">{{ mainTitle }}</p>
        <i
          class="minimize el-icon-minus"
          @click="minimize"
          v-if="minimizeStatus"
        ></i>
      </div>
      <div
        class="primary-dialog__content"
        style="overflow-x: clip; overflow-y: auto"
      >
        <el-form ref="templateForm" :model="form">
          <!-- <p class="title-sty">基本资料</p> -->
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item prop="projName">
                <p class="item-label">项目名称</p>
                <el-input v-model="form.projName" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="itemType">
                <p class="item-label">工程类型</p>
                <el-input v-model="form.itemType" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="constructContent">
                <p class="item-label">主要建设内容</p>
                <el-input v-model="form.constructContent" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="constructProperty">
                <p class="item-label">建设性质</p>
                <el-input v-model="form.constructProperty" disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin: 0">
            <el-col
              :span="6"
              v-if="
                form.itemType == '堤防' ||
                form.itemType == '灌区' ||
                form.itemType == '保护区' ||
                form.itemType == '河道' ||
                form.itemType == '水库'
              "
            >
              <el-form-item prop="bas">
                <p class="item-label">流域</p>
                <el-input v-model="form.bas" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="areaCode">
                <p class="item-label">行政区划</p>
                <el-input v-model="form.areaCode" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="startYear">
                <p class="item-label">项目起始年份</p>
                <el-input v-model="form.startYear" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="endYear">
                <p class="item-label">项目截止年份</p>
                <el-input v-model="form.endYear" disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <p class="title-sty">特征信息</p>
          <el-row :gutter="20">
            <el-col :span="6" v-if="form.itemType == '防洪区'">
              <el-form-item prop="acreage">
                <p class="item-label">面积（万㎡）</p>
                <el-input v-model="form.acreage" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="flLowLimLev">
                <p class="item-label">防洪限制水位（m）</p>
                <el-input v-model="form.flLowLimLev" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="flow">
                <p class="item-label">流量</p>
                <el-input v-model="form.flow" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="fpType">
                <p class="item-label">防洪区类型</p>
                <el-input v-model="form.fpType" disabled />
              </el-form-item>
            </el-col>
            <el-col
              :span="6"
              v-if="form.itemType == '堤防' || form.itemType == '保护区'"
            >
              <el-form-item prop="planFloodStandard">
                <p class="item-label">规划防洪标准(年一遇)</p>
                <el-input v-model="form.planFloodStandard" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6" v-if="form.itemType == '河道'">
              <el-form-item prop="planRiverWidth">
                <p class="item-label">规划河宽（m）</p>
                <el-input v-model="form.planRiverWidth" disabled />
              </el-form-item>
            </el-col>
            <el-col
              :span="6"
              v-if="form.itemType == '堤防' || form.itemType == '保护区'"
            >
              <el-form-item prop="realFloodStandard">
                <p class="item-label">现状防洪标准</p>
                <el-input v-model="form.realFloodStandard" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6" v-if="form.itemType == '河道'">
              <el-form-item prop="renovateLength">
                <p class="item-label">整治长度（km）</p>
                <el-input v-model="form.renovateLength" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6" v-if="form.itemType == '水库'">
              <el-form-item prop="totCap">
                <p class="item-label">总库容（万m³）</p>
                <el-input v-model="form.totCap" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6" v-if="form.itemType == '灌区'">
              <el-form-item prop="transformAcreage">
                <p class="item-label">改造面积（万㎡）</p>
                <el-input v-model="form.transformAcreage" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6" v-if="form.itemType == '水库'">
              <el-form-item prop="uppLevFlco">
                <p class="item-label">防洪高水位（m）</p>
                <el-input v-model="form.uppLevFlco" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6" v-if="form.itemType == '防洪区'">
              <el-form-item prop="volume">
                <p class="item-label">容积（万m³）</p>
                <el-input v-model="form.volume" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6" v-if="form.itemType == '水库'">
              <el-form-item prop="watShedArea">
                <p class="item-label">集雨面积（k㎡）</p>
                <el-input v-model="form.watShedArea" disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <p class="title-sty">投资信息</p>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item prop="amountInvestCapitalize">
                <p class="item-label">核定投资金额(万元)</p>
                <el-input v-model="form.amountInvestCapitalize" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="amountInvestDevelop">
                <p class="item-label">发展投资金额(万元)</p>
                <el-input v-model="form.amountInvestDevelop" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="amountInvestIndirect">
                <p class="item-label">结转投资金额(万元)</p>
                <el-input v-model="form.amountInvestIndirect" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item prop="amountInvestTotal">
                <p class="item-label">总投资金额(万元)</p>
                <el-input v-model="form.amountInvestTotal" disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin: 0">
            <el-col :span="6">
              <el-form-item prop="amountInvested">
                <p class="item-label">已完成投资金额(万元)</p>
                <el-input v-model="form.amountInvested" disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <p class="title-sty">年度投资金额(万元)</p>
          <el-row :gutter="20">
            <div v-for="(item, index) in form.amounts" :key="index">
              <el-col :span="6">
                <p class="item-label">金额(万元)</p>
                <el-input v-model="item.amount" disabled />
              </el-col>
              <el-col :span="6">
                <p class="item-label">年份</p>
                <el-input v-model="item.year" disabled />
              </el-col>
            </div>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <slot name="footer"></slot>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
export default {
  name: "PrimaryDialog",
  props: {
    ProgrammeData: {
      type: Object,
      default: null,
    },
    visible: {
      type: Boolean,
      default: false,
    },
    appendToBody: {
      type: Boolean,
      default: false,
    },
    width: {
      type: String,
      default: "900px",
    },
    mainTitle: {
      type: String,
      default: "测试",
    },
    fullscreen: {
      type: Boolean,
      default: false,
    },
    leftSide: {
      type: Boolean,
      default: false,
    },
    minimizeStatus: {
      type: Boolean,
      default: false,
    },
    hasAnalysis: { type: Boolean, default: true },
    modal: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      form: {},
    };
  },
  components: {},
  mounted() {
    this.$nextTick(() => {
      this.loadFullPlanProj(this.ProgrammeData.id);
    });
  },
  methods: {
    // 查询一项完整的规划项目
    async loadFullPlanProj(val) {
      await hydraulicApi.loadFullPlanProj({ id: val }).then((res) => {
        if (res.code == 0) {
          console.log(441, res.data);
          this.form = res.data;
        }
      });
    },
    handleClose() {
      this.$emit("handleClose");
    },
    peripheralAnalysis() {
      this.$emit("analysis");
    },
    minimize() {
      this.$emit("minimize");
    },
  },
};
</script>

<style lang="scss">
.primary-dialog {
  .el-dialog {
    background-image: linear-gradient(
      180deg,
      rgba(8, 42, 53, 1) 2%,
      rgba(5, 14, 48, 1) 100%
    );
    .el-dialog__header {
      height: 54px;
      padding: 18px;
      border: 1px solid #00c1ff;
      // box-shadow: inset 0 4px 30px 0 #2351ef40;
      background: transparent;
      border-bottom: 0;
      margin: 2px 2px 0;
      .primary-dialog__title {
        height: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 40px;
        .primary-dialog__title--label {
          background: url(~assets/images/WaterDisastersImage/title-bg.png)
            no-repeat;
          // background-size: 100% 100%;
          font-size: 16px;
          color: #fff;
          letter-spacing: 0;
          height: 32px;
          width: 80%;
          // margin-left: 16px;
          padding-left: 56px;
          font-weight: bold;
          line-height: 32px;
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
      min-height: 100px;
      padding: 0 18px;
      background: transparent;
      border: 1px solid #00c1ff;
      border-top: 0;
      border-bottom: 0;
      margin: 0 2px;
    }
    .el-dialog__footer {
      padding: 0 18px 24px;
      background: transparent;
      border: 1px solid #00c1ff;
      border-top: 0;
      margin: 0 2px 4px;
      .dialog-footer {
        .el-button {
          padding: 6px 16px;
          &.cancel {
            color: #1ef8f2;
            border: 1px solid #1ef8f2;
            background-color: transparent;
            border-radius: 4px;
          }
          &.confirm {
            color: #fff;
            border: 0;
            background-image: linear-gradient(
              180deg,
              #36c0e4 20%,
              #0639ff 100%
            );
          }
        }
      }
    }
    .el-table {
      .cell {
        height: 37px;
        line-height: 37px;
      }
      .el-checkbox__inner {
        border: 0.0625rem solid #00c1ff;
        background-color: #00c1ff;
        // opacity: 0;
        background: rgba($color: #000000, $alpha: 0);
      }
      .el-table__body tr.current-row > td {
        background-color: rgba(41, 147, 180, 0.3);
      }
      th,
      td {
        padding: 0;
        font-size: 14px;
        color: #fff;
      }
      th {
        padding: 3px 0;
        font-size: 14px;
        background-color: rgba(41, 147, 180, 0.1);
        font-weight: bold;
        color: #00c1ff;
      }
    }
    &:after {
      content: "";
      position: absolute;
      height: calc(100% + 4px);
      width: 100%;
      top: -2px;
      left: 0;
      background: linear-gradient(to bottom, #35b9e3, #5398f6) left top
          no-repeat,
        linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
        linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
        linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat,
        linear-gradient(to bottom, #5398f6, #35b9e3) left bottom no-repeat,
        linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
        linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
        linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
      /*设置大小*/
      background-size: 18px 2px, 2px 18px;
      z-index: -1;
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
  background: url(~assets/images/peripheral_analysis.png) no-repeat;
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
