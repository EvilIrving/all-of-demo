<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2021-07-15 18:12:26
 * @LastEditors: Please set LastEditors
 * @Description: 珊溪巡查落点弹窗
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCard.vue
-->
<template>
  <div class="detail-table">
    <el-dialog
      width="60%"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">{{ name }}</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div class="content-panel">
          <div class="tb-row">
            <span class="label">乱点名称</span>
            <span class="value">XXXXX</span>
          </div>
          <div class="tb-row">
            <span class="label">乱点类型</span>
            <span class="value">XXXXX</span>
          </div>
          <div class="tb-row">
            <span class="label">乱点状态</span>
            <span class="value">XXXXX</span>
          </div>
          <div class="tb-row">
            <span class="label">发现时间</span>
            <span class="value">XXXXX</span>
          </div>
          <div class="tb-row">
            <span class="label">所属县/市</span>
            <span class="value">XXXXX</span>
          </div>
          <div class="tb-row">
            <span class="label">所在区域</span>
            <span class="value">XXXXX</span>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { generateDataZoom } from "../cards/utils/chart.js";
import * as echarts from "echarts"; // echart插件
import { weatherApi } from "@/api/weatherApi";

export default {
  name: "ShanXiXcDialog",
  data() {
    return {
      name: "",

      loading: false,
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  components: {},
  created() {},
  mounted() {},
  computed: {},
  methods: {
    init(val) {
      this.name = val.name;
      this.$nextTick(() => {});
    },

    handleClose() {
      this.$emit("closeDialog");
    },
  },
};
</script>
<style lang="scss" scoped>
.primary-dialog {
  .tb-row {
    color: #fff;
    border: 1px solid #00c1ff;
    font-size: 16px;
    line-height: 40px;
    border-bottom: none;
    .label {
      display: inline-block;
      height: 100%;
      width: 110px;
      color: #56fefe;
      text-align: center;
      border-right: 1px solid #00c1ff;
      background-image: linear-gradient(
        90deg,
        rgba(44, 158, 191, 0.3) 3%,
        rgba(41, 147, 180, 0.1) 100%
      );
    }
    .value {
      padding: 0 20px;
    }
    &:last-child {
      border-bottom: 1px solid #00c1ff;
    }
  }

  ::v-deep .el-dialog {
    .el-dialog__header {
      padding: 2px;
      .primary-dialog__title {
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
      }
    }
    .el-dialog__body {
      // height: 300px;
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        height: 100%;
        padding: 20px;
      }
    }
  }
}
</style>