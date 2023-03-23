<!--
 * @Author: lsh
 * @Date: 2021-02-02 09:50:10
 * @LastEditors: lsh
 * @LastEditTime: 2021-02-02 11:32:43
 * @Descripttion: 
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\jointDispatch\components\ModelDialog3.vue
-->
<template>
  <el-dialog
    :visible.sync="dailogShow"
    :before-close="handleClose"
    top="10vh"
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
        <el-col :span="5" class="form-label">预报时间：</el-col>
        <el-col :span="18">
          <el-date-picker
            v-model="value1"
            type="datetime"
            placeholder="选择日期时间"
            style="width: 100%"
          ></el-date-picker>
        </el-col>
      </el-row>
      <el-row class="form-row">
        <el-col :span="5" class="form-label">预报降雨总量：</el-col>
        <el-col :span="7">
          <el-input v-model="keyword3" type="number"></el-input>
        </el-col>
        <el-col :span="4" class="form-label">典型暴雨：</el-col>
        <el-col :span="7">
          <el-select
            popper-class="blue-popper"
            v-model="keyword4"
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
      <div class="check-box">
        <span>数据列表</span>
        <div>
          <span
            class="icon-list"
            :class="{ active: menuActive == 1 }"
            @click="handleMenuClick(1)"
          ></span>
          <span
            class="icon-chart"
            :class="{ active: menuActive == 2 }"
            @click="handleMenuClick(2)"
          ></span>
        </div>
      </div>
      <div class="table-box" v-show="menuActive == 1">
        <el-table
          ref="table"
          :data="tableData"
          style="width: 100%"
          stripe
          height="100%"
        >
          <!-- <el-table-column type="selection" width="55"></el-table-column> -->
          <el-table-column type="index" label="序号" width="80">
            <template slot-scope="scope">{{ scope.$index + 1 }}</template>
          </el-table-column>
          <el-table-column prop="time" label="时刻"></el-table-column>
          <el-table-column prop="rain" label="降雨量(mm)"></el-table-column>
        </el-table>
      </div>
      <div class="chart-box" v-show="menuActive == 2">
        <bar-chart :chartData="chartData" :setting="setting" v-if="resize" />
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
import BarChart from "./charts/BarChart";
export default {
  name: "",
  props: {
    dailogShow: {
      type: Boolean,
      default: false,
    },
  },
  components: { BarChart },
  data() {
    return {
      tableData: [
        {
          time: "2020-10-11",
          rain: 21,
        },
        {
          time: "2020-10-11",
          rain: 21,
        },
        {
          time: "2020-10-11",
          rain: 21,
        },
        {
          time: "2020-10-11",
          rain: 21,
        },
        {
          time: "2020-10-11",
          rain: 21,
        },
        {
          time: "2020-10-11",
          rain: 21,
        },
        {
          time: "2020-10-11",
          rain: 21,
        },
        {
          time: "2020-10-11",
          rain: 21,
        },
        {
          time: "2020-10-11",
          rain: 21,
        },
      ],
      keyword1: "",
      keyword2: "",
      keyword3: "",
      keyword4: "",
      value1: "",
      menuActive: 1,
      //图表
      resize: false, //解决第一次点卡宽度不能自适应问题
      chartData: { rows: [], columns: [] },
      setting: { labelMap: {} },
    };
  },
  mounted() {},
  methods: {
    handleClose() {
      this.$emit("handleClose");
    },
    //图标菜单
    handleMenuClick(type) {
      if (type == 1) {
        this.menuActive = 1;
        //展示列表
      } else {
        this.menuActive = 2;
        //展示图表
        //初始化数据
        this.chartData = { rows: [], columns: [] };
        this.setting = { labelMap: {} };
        //数据组装
        this.chartData.rows = [
          {
            tm: "2021",
            drp: "3",
          },
          {
            tm: "2020",
            drp: "6",
          },
          {
            tm: "2019",
            drp: "11",
          },
        ];
        this.setting.labelMap = {
          drp: "降雨量",
          tm: "时间",
        };
        this.chartData.columns = ["tm", "drp"];
        //展示
        this.resize = false;
        this.$nextTick(() => {
          this.resize = true;
        });
      }
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
}
.icon-list {
  cursor: pointer;
  margin-right: 10px;
  display: inline-block;
  width: 21px;
  height: 21px;
  background: url("../../../../assets/images/bs-list.png") no-repeat center;
  background-size: 100% 100%;
  &.active {
    background: url("../../../../assets/images/bs-list-active.png") no-repeat
      center;
  }
}
.icon-chart {
  cursor: pointer;
  display: inline-block;
  width: 21px;
  height: 21px;
  background: url("../../../../assets/images/bs-chart.png") no-repeat center;
  background-size: 100% 100%;
  &.active {
    background: url("../../../../assets/images/bs-chart-active.png") no-repeat
      center;
  }
}
</style>