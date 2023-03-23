<!--
 * @Author: lsh
 * @Date: 2021-02-02 09:49:53
 * @LastEditors: lsh
 * @LastEditTime: 2021-02-02 14:07:42
 * @Descripttion: 
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\jointDispatch\components\ModelDialog1.vue
-->
<template>
  <el-dialog
    :visible.sync="dailogShow"
    :before-close="handleClose"
    top="30vh"
    title="模型调度"
    class="model-dialog"
    :close-on-click-modal="false"
    append-to-body
  >
    <div class="dialog-content">
      <el-row class="form-row">
        <el-col :span="5" class="form-label">调度规则：</el-col>
        <el-col :span="18">
          <el-select
            popper-class="blue-popper"
            v-model="keyword1"
            style="width: 100%"
            placeholder="请选择"
          >
            <el-option
              v-for="(item, index) in goodList"
              :key="index"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row class="form-row">
        <el-col :span="5" class="form-label">设置流量：</el-col>
        <el-col :span="18">
          <el-input v-model="keyword2" type="number"></el-input>
        </el-col>
      </el-row>
    </div>
    <div slot="footer">
      <el-row>
        <div class="form-footer">
          <el-button class="form-btn btn-cancel" @click="handleClose"
            >取消</el-button
          >
          <el-button class="form-btn btn-save" @click="analysis"
            >保存</el-button
          >
        </div>
      </el-row>
    </div>
  </el-dialog>
</template>

<script>
// import { xxx } from "@/api/xxxx";
export default {
  name: "",
  props: {
    dailogShow: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      keyword1: "",
      keyword2: "",
      goodList: [],
    };
  },
  mounted() {},
  methods: {
    handleClose() {
      this.$emit("handleClose");
    },
    analysis() {},
  },
};
</script>

<style lang="scss">
@import "@/style/blueDialog.scss";
.model-dialog {
  margin: 0 auto;
  user-select: none;
  .el-dialog {
    width: 600px;
    height: auto;
    background: rgba(0, 0, 0, 0) url(../../../../assets/images/bs-dialog-bg.png)
      no-repeat center center;
    background-size: 100% 100%;
    .el-dialog__footer .form-btn {
      background: rgba(0, 0, 0, 0);
      color: #2c72be;
      border: none;
      border-radius: 0;
      &.btn-save {
        color: #fff;
        background: #239def;
      }
    }
    .el-dialog__header {
      background: rgba(0, 0, 0, 0);
      padding: 20px 20px 12px;
      .el-dialog__title {
        color: #4db6e4;
      }
      .el-dialog__headerbtn {
        top: 26px;
        right: 28px;
      }
    }
    .dialog-content {
      overflow: auto;
      .form-row {
        margin-bottom: 16px;
        .el-button {
          padding: 6px 18px;
          &.el-button--primary {
            background: #239def;
            border-color: #239def;
          }
        }
      }
      .form-label {
        text-align: right;
        height: 24px;
        line-height: 24px;
        color: #2c72be;
      }
      .el-input {
        .el-input__inner {
          height: 28px;
          border: 1px solid #2c72be;
          background: rgba(0, 0, 0, 0);
        }
      }
      .table-box {
        width: calc(100% - 10px);
        height: 400px;
        padding: 0 16px;
        .el-input__icon {
          line-height: normal;
        }
        .el-table th {
          background: #239def;
        }
      }
      .chart-box {
        width: calc(100% - 10px);
        height: 300px;
      }
    }
  }
}
.blue-popper {
  .el-select-dropdown__item.selected {
    color: #2c72be;
  }
}
.el-input__icon {
  line-height: 28px;
}
.check-box {
  color: #2c72be;
  padding: 0 26px 16px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  & > div {
    span {
      cursor: pointer;
      margin-left: 10px;
      padding: 0 10px;
      display: inline-block;
      color: #fff;
      background: #239def;
      height: 30px;
      line-height: 30px;
      border-radius: 5px;
    }
  }
}
</style>