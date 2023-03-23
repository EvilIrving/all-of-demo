<!--
 * @Author: lsh
 * @Date: 2021-02-02 09:49:59
 * @LastEditors: lsh
 * @LastEditTime: 2021-02-02 11:33:28
 * @Descripttion: 
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\jointDispatch\components\ModelDialog2.vue
-->
<template>
  <el-dialog
    :visible.sync="dailogShow"
    :before-close="handleClose"
    top="16vh"
    width="50%"
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
            @change="handleRule"
          >
            <el-option
              v-for="(item, index) in goodList"
              :key="index"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row class="form-row">
        <el-col :span="5" class="form-label">设置{{ checkRule }}：</el-col>
        <el-col :span="18">
          <el-input
            v-model="keyword2"
            type="number"
            :disabled="!checkRule"
          ></el-input>
        </el-col>
      </el-row>
      <div class="check-box" v-if="analysisShow">
        <span>{{ checkRule }}列表</span>
        <!-- <div>
          <span>模板下载</span>
          <span>导入</span>
        </div> -->
      </div>
      <div class="table-box" v-if="analysisShow">
        <el-table
          ref="table"
          :data="tableData"
          style="width: 100%"
          stripe
          height="100%"
        >
          <!-- <el-table-column type="selection" width="55"></el-table-column> -->
          <el-table-column type="index" label="序号">
            <template slot-scope="scope">{{ scope.$index + 1 }}</template>
          </el-table-column>
          <el-table-column prop="time" label="时间"></el-table-column>
          <el-table-column prop="in" label="入库流量(m³/s)"></el-table-column>
          <el-table-column prop="out" label="出库流量(m³/s)"></el-table-column>
          <el-table-column prop="except" label="预报水位(m)"></el-table-column>
          <el-table-column prop="real" label="实测水位(m)"></el-table-column>
          <el-table-column prop="rain" label="雨量(mm)"></el-table-column>
          <el-table-column prop="capacity" label="库容(万m³)"></el-table-column>
        </el-table>
      </div>
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
      goodList: [
        {
          label: "控制水位调度",
          value: 0,
        },
        {
          label: "控制雨量调度",
          value: 1,
        },
        {
          label: "控制流量调度",
          value: 2,
        },
      ],
      checkRule: "",
      tableData: [
        {
          time: "2021/2/8 9:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.01,
        },
        {
          time: "2021/2/8 10:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.01,
        },
        {
          time: "2021/2/8 11:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.01,
        },
        {
          time: "2021/2/8 12:00",
          in: 0.01,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.01,
        },
        {
          time: "2021/2/8 13:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.01,
        },
        {
          time: "2021/2/8 14:00",
          in: 0.01,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.01,
        },
        {
          time: "2021/2/8 15:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.01,
        },
        {
          time: "2021/2/8 16:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.01,
        },
        {
          time: "2021/2/8 17:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.01,
        },
        {
          time: "2021/2/8 18:00",
          in: 0.01,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/8 19:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/8 20:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/8 21:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/8 22:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/8 23:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/9 0:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/9 1:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/9 2:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/9 3:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/9 4:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/9 5:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/9 6:00",
          in: 0.01,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.02,
        },
        {
          time: "2021/2/9 7:00",
          in: 1.18,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.23,
        },
        {
          time: "2021/2/9 8:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.45,
        },
        {
          time: "2021/2/9 9:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.45,
        },
        {
          time: "2021/2/9 10:00",
          in: 0.01,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.45,
        },
        {
          time: "2021/2/9 11:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.45,
        },
        {
          time: "2021/2/9 12:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.45,
        },
        {
          time: "2021/2/9 13:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.45,
        },
        {
          time: "2021/2/9 14:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.45,
        },
        {
          time: "2021/2/9 15:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.45,
        },
        {
          time: "2021/2/9 16:00",
          in: 0,
          out: 0,
          except: 30.19,
          real: "-",
          rain: "-",
          capacity: 1003.45,
        },
        {
          time: "2021/2/9 17:00",
          in: 1.18,
          out: 0,
          except: 30.2,
          real: "-",
          rain: "-",
          capacity: 1003.66,
        },
        {
          time: "2021/2/9 18:00",
          in: 0,
          out: 0,
          except: 30.2,
          real: "-",
          rain: "-",
          capacity: 1003.87,
        },
        {
          time: "2021/2/9 19:00",
          in: 0,
          out: 0,
          except: 30.2,
          real: "-",
          rain: "-",
          capacity: 1003.87,
        },
        {
          time: "2021/2/9 20:00",
          in: 0,
          out: 0,
          except: 30.2,
          real: "-",
          rain: "-",
          capacity: 1003.87,
        },
        {
          time: "2021/2/9 21:00",
          in: 0,
          out: 0,
          except: 30.2,
          real: "-",
          rain: "-",
          capacity: 1003.87,
        },
      ],
      keyword1: "",
      keyword2: "",
      analysisShow: false,
    };
  },
  mounted() {},
  methods: {
    handleRule() {
      this.keyword2 = "";
      if (this.keyword1 == 0) {
        this.checkRule = "水位";
      } else if (this.keyword1 == 1) {
        this.checkRule = "雨量";
      } else {
        this.checkRule = "流量";
      }
    },
    analysis() {
      if (this.keyword2 >= 10000) {
        if (this.keyword1 == 0) {
          //水位
          this.$message({
            message: `您输入的水位值为${this.keyword2}，已超过水库坝顶高程，请确认后重新输入`,
            type: "warning",
          });
        } else if (this.keyword1 == 2) {
          // 流量
          this.$message({
            message: `您输入的流量值为${this.keyword2}，已超过最大过闸流量，请确认后重新输入`,
            type: "warning",
          });
        }
        this.analysisShow = false;
        return;
      } else if (!this.keyword2) {
        this.$message({
          message: `请设置值`,
          type: "warning",
        });
        this.analysisShow = false;
        return;
      }
      this.analysisShow = true;
    },
    handleClose() {
      this.$emit("handleClose");
    },
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
    background: rgba(0, 0, 0, 0)
      url(../../../../assets/images/blue-dialog-bg.png) no-repeat center center;
    background-size: 100% 100%;
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
  .form-btn {
    background: rgba(0, 0, 0, 0);
    color: #2c72be;
    border: none;
    border-radius: 0;
    &.btn-save {
      color: #fff;
      background: #239def;
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