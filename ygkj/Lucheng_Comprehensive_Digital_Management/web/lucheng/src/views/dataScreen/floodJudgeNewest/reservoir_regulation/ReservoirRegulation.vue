<template>
  <div class="reservoir-regulation">
    <!-- 地图区域 -->
    <screen-map ref="map" class="screen_map"></screen-map>

    <div class="bigScreen-left">
      <transition name="fade" @after-leave="transitionDoneLeft">
        <div
          v-show="barshowLeft"
          class="sidebar higher"
          :style="{ width: '400px' }"
        >
          <div class="screen-title">
            <div class="title-left">
              <i class="icon-judge-icon"></i>
              <span>大中型水库</span>
              <span style="font-size: 16px; color: #ffffff; font-weight: 400">
                （最多可以选择6个水库）
              </span>
            </div>
          </div>
          <div class="sidebar-list">
            <el-table
              ref="multipleTable"
              :data="leftTableList"
              style="width: 100%"
              height="765px"
              @selection-change="handleSelectionChange"
            >
              <el-table-column
                label-class-name="disabled-selection"
                type="selection"
                width="35"
              />
              <el-table-column
                align="left"
                type="index"
                label="序号"
                width="35"
              >
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="left"
                prop="projectName"
                label="水库名称"
                show-overflow-tooltip
                min-width="75"
              />
              <el-table-column
                align="right"
                prop="z"
                label="库容（万m³）"
                min-width="100"
              />
              <el-table-column
                align="right"
                prop="limitLevel"
                label="可纳蓄容量（万m³）"
                min-width="100"
              />
            </el-table>
            <el-row type="flex" justify="space-between">
              <el-col>
                <div>
                  已选
                  <span :style="{ fontSize: '24px', color: '#23E3FB' }">
                    {{ leftTableSelected.length }}
                  </span>
                  个水库
                  <el-divider direction="vertical"></el-divider>
                  <el-button
                    type="text"
                    :style="{ color: '#239DEF' }"
                    @click="clearSelection"
                  >
                    清空
                  </el-button>
                </div>
              </el-col>
              <el-col
                style="margin-left: auto; width: unset; margin-right: 8px"
              >
                <el-button type="primary" class="thin-btn" @click="lookDetails"
                  >确定</el-button
                >
              </el-col>
            </el-row>
          </div>
          <div class="switch-btn higher" @click="toggleSideBarLeft()"></div>
        </div>
      </transition>
      <div
        v-show="showBtnLeft"
        class="switch-btn show-btn"
        @click="toggleSideBarLeft"
      />
    </div>
    <div class="bigScreen-right">
      <transition name="fade" @after-leave="transitionDone">
        <div v-show="barshow" class="sidebar">
          <div class="screen-title">
            <div class="title-left">
              <i class="icon-judge-icon"></i>
              <span>最接近超警水库</span>
            </div>
          </div>
          <div class="sidebar-list">
            <el-table
              ref="dataTable"
              :data="rightTableList"
              style="width: 100%"
              height="600px"
            >
              <el-table-column
                align="left"
                type="index"
                label="序号"
                width="45"
              >
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="left"
                prop="reservoirName"
                label="名称"
                width="100"
              />
              <el-table-column
                align="right"
                prop="currentWaterLevel"
                label="水位(m)"
                width="70"
              />
              <el-table-column
                align="right"
                prop="limitLevel"
                label="警戒水位(m)"
                width="100"
              />
              <el-table-column label="操作">
                <template slot-scope="{ row }">
                  <i
                    class="icon-video-action-btn action-btn"
                    @click="videoDialogOpen(row)"
                  />
                  <i
                    class="icon-call-action-btn action-btn"
                    @click="callDialogOpen(row.reservoirCode)"
                  />
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="switch-btn" @click="toggleSideBar()"></div>
        </div>
      </transition>
      <div
        v-show="showBtn"
        class="switch-btn show-btn"
        @click="toggleSideBar()"
      />
    </div>
    <!-- 左侧按钮 -->
    <div
      class="layer-control"
      :style="{ left: barshowLeft ? '490px' : '80px' }"
    >
      <!-- 一键研判 -->
      <div class="one-key" @click="Judged"></div>
      <!-- 今日值班 -->
      <div class="one-key one-today" @click="onDutyToday"></div>
      <!-- 防汛预案 -->
      <div class="one-key control-plan" @click="floodControlPlan"></div>
    </div>
    <call-dialog
      v-if="callDialogShow"
      :code="callDialogCode"
      :visible="callDialogShow"
      @close="callDialogClose"
    />
    <video-dialog
      v-if="videoDialogShow"
      :reservoirName="reservoirName"
      :reservoirCode="reservoirCode"
      :visible="videoDialogShow"
      @close="videoDialogClose"
    />
    <!-- 一键研判弹窗 -->
    <judged-dialog
      v-if="dispatchVisible"
      :dialogVisible="dispatchVisible"
      @handleClose="dispatchClose"
    />
    <!-- 今日值班 -->
    <on-duty-today-dialog
      v-if="todayVisibleShow"
      :dialogVisible="todayVisibleShow"
      @handleClose="todayCloseShow"
    />
    <!-- 防汛预案 -->
    <flood-control-plan-dialog
      v-if="floodVisibleShow"
      :dialogVisible="floodVisibleShow"
      @handleClose="floodCloseShow"
    />
    <!-- 查看详情弹窗 -->
    <flood-prediction-dialog
      :dialogVisible="detailsShow"
      :lookDetailsData="lookDetailsData"
      v-if="detailsShow"
      @handleClose="floodPredictionClose"
    />
  </div>
</template>

<script>
import { rainApi } from "@/api/dataScreen/floodJudge/index";
import { dispatchApi } from "@/api/dataScreen/jointDispatch/JointDispatch";
import ScreenMap from "./components/ScreenMap";
import CallDialog from "./components/CallDialog";
import VideoDialog from "./components/VideoDialog";
//一键研判
import JudgedDialog from "./components/JudgedDialog";
//今日值班
import OnDutyTodayDialog from "./components/OnDutyTodayDialog";
//防汛预案
import FloodControlPlanDialog from "./components/FloodControlPlanDialog";
//详情展示
import FloodPredictionDialog from "./components/FloodPredictionDialog";
export default {
  name: "ReservoirRegulation",
  components: {
    ScreenMap,
    CallDialog,
    VideoDialog,
    JudgedDialog,
    FloodPredictionDialog,
    OnDutyTodayDialog,
    FloodControlPlanDialog,
  },
  data() {
    return {
      dispatchVisible: false, //一键研判弹窗
      todayVisibleShow: false, //今日值班弹窗
      floodVisibleShow: false, //防汛预案弹窗

      showBtnLeft: false,
      barshowLeft: true,
      showBtn: false,
      barshow: true,

      leftTableList: [],
      leftTableSelected: [],

      rightTableList: [],

      callDialogShow: false,
      videoDialogShow: false,
      detailsShow: false,
      lookDetailsData: [],
      reservoirName: '',
      reservoirCode: ''
    };
  },
  mounted() {
    this.getLeftList();
    this.getRightList();
  },
  methods: {
    //查看详情
    lookDetails() {
      if (0 < this.leftTableSelected.length) {
        this.detailsShow = true;
        this.lookDetailsData = this.leftTableSelected;
      } else {
        this.$message({ type: "warning", message: "请选择至少一条数据！" });
      }
    },
    //关闭查看
    floodPredictionClose() {
      this.detailsShow = false;
      this.clearSelection();
    },
    //一键研判打开弹窗
    Judged() {
      this.dispatchVisible = true;
    },
    //关闭一键研判弹窗
    dispatchClose() {
      this.dispatchVisible = false;
    },
    //今日值班打开弹窗
    onDutyToday() {
      this.todayVisibleShow = true;
    },
    //今日值班关闭弹窗
    todayCloseShow() {
      this.todayVisibleShow = false;
    },
    //防汛预案打开弹窗
    floodControlPlan() {
      this.floodVisibleShow = true;
    },
    //防汛预案关闭弹窗
    floodCloseShow() {
      this.floodVisibleShow = false;
    },

    toggleSideBarLeft() {
      this.showBtnLeft = false;
      this.barshowLeft = !this.barshowLeft;
    },
    transitionDoneLeft() {
      this.showBtnLeft = true;
    },
    transitionDone() {
      this.showBtn = true;
    },
    toggleSideBar() {
      this.showBtn = false;
      this.barshow = !this.barshow;
    },
    /** 多选 */
    handleSelectionChange(val) {
      if (val.length > 6) {
        this.$message({ type: "warning", message: "最多只能选中六条数据！" });
        this.$refs.multipleTable.toggleRowSelection(val.pop());
      } else {
        this.leftTableSelected = val;
      }
    },
    /** 清空选择 */
    clearSelection() {
      this.$refs.multipleTable.clearSelection();
    },
    /** 打开视频通话对话框 */
    callDialogOpen(code) {
      this.callDialogCode = code;
      this.callDialogShow = true;
    },
    callDialogClose() {
      this.callDialogShow = false;
    },
    /** 打开视频对话框 */
    videoDialogOpen(val) {
      this.reservoirName = val.reservoirName;
      this.reservoirCode = val.reservoirCode;
      this.videoDialogShow = true;
    },
    videoDialogClose() {
      this.videoDialogShow = false;
    },
    getLeftList() {
      dispatchApi.mapPoint().then((res) => {
        if (res.code == 0) {
          this.leftTableList = res.data;
        }
      });
    },
    getRightList() {
      rainApi.getTopNRes(10).then((res) => {
        this.rightTableList = res.data;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
@import "./ReservoirRegulation.scss";
.reservoir-regulation {
  width: 100%;
  height: 100%;
  .screen-title {
    margin-bottom: 0;
  }
  .sidebar-list {
    margin-top: 0;
  }
  .bigScreen-left {
    top: 5rem;
    .sidebar.higher {
      height: 864px;
    }
    .switch-btn {
      height: 864px;
    }
  }
  ::v-deep .el-table {
    .cell {
      padding: 0;
    }
    .el-table__header-wrapper {
      .disabled-selection {
        .cell {
          .el-checkbox__inner {
            display: none;
          }
        }
      }
      th {
        .cell {
          font-weight: normal;
          font-size: 16px;
        }
      }
    }
  }

  .thin-btn {
    border-radius: 0;
    line-height: 0.5;
  }
  .action-btn {
    cursor: pointer;
    margin-left: 3px;
  }
  ::v-deep .is-left {
    text-align: left !important;
  }
  .layer-control {
    transition: left 0.6s ease-in-out;
    position: absolute;
    top: 300px;
    bottom: 80px;
    left: 490px;
    z-index: 100;
    // 一键研判
    .one-key {
      width: 102px;
      height: 26px;
      position: relative;
      top: -20px;
      margin-bottom: 20px;
      color: #3895f4;
      cursor: pointer;
      background: url("../../../../assets/images/one-key.png") no-repeat center;
      background-size: 100% 100%;
    }
    .one-today {
      width: 102px;
      height: 26px;
      background: url("../../../../assets/images/on-duty-today.png") no-repeat
        center;
      background-size: 100% 100%;
    }
    .control-plan {
      width: 102px;
      height: 26px;
      background: url("../../../../assets/images/flood-control-plan.png")
        no-repeat center;
      background-size: 100% 100%;
    }
  }
}
</style>