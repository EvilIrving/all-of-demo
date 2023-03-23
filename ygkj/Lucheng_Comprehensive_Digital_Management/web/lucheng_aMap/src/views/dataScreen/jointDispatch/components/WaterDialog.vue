<template>
  <el-dialog
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose"
    top="24vh"
    :title="name"
    class="water-dialog"
    :close-on-click-modal="false"
    append-to-body
  >
    <div class="dialog-content">
      <i class="icon-simulation-setting" @click="mode = !mode"></i>
      <div class="sidebar-tab">
        <span
          v-for="(item, index) in tabArr"
          :key="index"
          :class="{ active: tabActive == item.value }"
          @click="handleClick(item.value)"
          :title="item.label"
          >{{ item.label }}</span
        >
      </div>
      <div class="content">
        <div class="count-box" v-if="mode">
          <line-chart :chartData="chartData" :setting="setting" />
        </div>
        <div class="table-box" v-else>
          <el-table :data="tableData" style="width: 100%" stripe height="280px">
            <el-table-column prop="time" label="时间"></el-table-column>
            <el-table-column prop="level" label="水位(m)"></el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import LineChart from "./charts/LineChart";
import { dispatchApi } from "@/api/dataScreen/rescueSupport/dispatch.js";
export default {
  name: "RescueSupport",
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
    name: {
      type: String,
      default: "",
    },
    info: {
      type: Object,
    },
  },
  components: { LineChart },
  data() {
    return {
      goodNum: 0,
      form: {
        stName: "",
        lat: "",
        lng: "",
      },
      selectGood: "", //下拉框选中物资
      teamData: [], //测站最近的3个队伍列表
      goodsData: [], //添加的物资列表
      goodList: [], //下拉框物资列表
      selectTeam: {}, //选中team列表
      chartData: {
        rows: [
          {
            tm: "2020-09-09",
            upz: 12,
          },
          {
            tm: "2020-01-09",
            upz: 13,
          },
          {
            tm: "2020-02-09",
            upz: 14,
          },
        ],
        columns: ["tm", "upz"],
      },
      setting: {
        labelMap: {
          upz: "水位",
          tm: "时间",
        },
      },
      tabArr: [
        {
          label: "前一天",
          value: "1",
        },
        {
          label: "前二天",
          value: "二",
        },
        {
          label: "前三天",
          value: "3",
        },
        {
          label: "前四天",
          value: "4",
        },
        {
          label: "前一周",
          value: "7",
        },
        {
          label: "前二周",
          value: "14",
        },
      ],
      tabActive: 1,
      mode: true,
      tableData: [
        {
          time: "2020-09-09",
          level: 30,
        },
        {
          time: "2020-09-09",
          level: 30,
        },
        {
          time: "2020-09-09",
          level: 30,
        },
        {
          time: "2020-09-09",
          level: 30,
        },
        {
          time: "2020-09-09",
          level: 30,
        },
        {
          time: "2020-09-09",
          level: 30,
        },
        {
          time: "2020-09-09",
          level: 30,
        },
        {
          time: "2020-09-09",
          level: 30,
        },
        {
          time: "2020-09-09",
          level: 30,
        },
        {
          time: "2020-09-09",
          level: 30,
        },
        {
          time: "2020-09-09",
          level: 30,
        },
        {
          time: "2020-09-09",
          level: 30,
        },
      ],
    };
  },
  mounted() {},
  methods: {
    handleClose() {
      this.$emit("handleClose");
    },
    handleClick(value) {
      this.tabActive = value;
    },
  },
};
</script>

<style lang="scss">
@import "@/style/blueDialog.scss";
.water-dialog {
  user-select: none;
  .icon-simulation-setting {
    position: absolute;
    right: 50px;
    top: 24px;
    cursor: pointer;
  }
  .sidebar-tab {
    margin-left: 20px;
    > span {
      display: inline-block;
      height: 32px;
      width: 76px;
      margin-right: 16px;
      line-height: 32px;
      text-align: center;
      color: #2c5a90;
      font-size: 14px;
      cursor: pointer;
      background: url(../../../../assets/images/bigScreen-mini-tab.png)
        no-repeat;
      background-size: 100% 100%;

      &:last-child {
        margin-right: 0;
      }

      &.active {
        background: url(../../../../assets/images/bigScreen-mini-tab-check.png)
          no-repeat;
        background-size: 100% 100%;
        color: #a6dff6;
      }
    }
  }
  .el-dialog {
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
    .content {
      width: calc(100% - 20px);
      height: 290px;
      .count-box {
        height: 100%;
        width: 100%;
      }
      .table-box {
        margin-top: 16px;
      }
    }
    .dialog-content {
      height: 344px;
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
        text-align: center;
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
      .num-area {
        height: 28px;
        width: 100%;
        span {
          display: inline-block;
          height: 100%;
          text-align: center;
          line-height: 26px;
          color: #2c72be;
          border: 1px solid #2c72be;
        }
        .left-part {
          width: 32px;
          cursor: pointer;
          border-radius: 4px 0 0 4px;
        }
        .right-part {
          width: 32px;
          cursor: pointer;
          border-radius: 0 4px 4px 0;
        }
        .num-part {
          width: calc(100% - 64px);
        }
      }
      .table-box {
        padding: 0 16px;
        margin-bottom: 16px;
        .el-input__icon {
          line-height: normal;
        }

        p {
          color: #2c72be;
          padding: 0 0 12px;
        }
        .el-table th {
          background: #239def;
        }
      }
    }
  }
}
</style>