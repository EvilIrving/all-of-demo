<template>
  <!-- 容器 -->
  <div
    class="disaster-prevention"
    element-loading-background="rgba(0, 0, 0, 0.8)"
    v-loading="screenLoading"
    ref="bigScreen"
  >
    <!-- <audio ref="audio" class="audio-content" loop src="~assets/audio/12310.mp3">
      您的浏览器不支持 audio 标签。
    </audio> -->
    <div class="test" @click="getBack" v-if="typhoonShow"></div>

    <!-- 图层控件 -->
    <screen-map
      v-if="mapChangeModule == 'map'"
      @point-click="showDialog"
      ref="map"
      @typhoonPointClick="typhoonPointClick"
      @rainfullClick="rainfullClick"
    />

    <!-- 演练统计卡片 -->
    <div
      class="preview-module-card"
      v-if="previewModule && currentScene == '防御态势'"
    >
      <preview-card />
    </div>

    <generalization-map
      v-if="mapChangeModule == 'generalization'"
    ></generalization-map>

    <!-- 地图、拓扑图切换区域 -->
    <div
      class="change-area"
      :class="!collapseRight ? 'area-right' : ''"
      v-if="currentScene == '防洪调度'"
    >
      <div
        @click="moduleChange('map')"
        :class="[
          'change-item',
          mapChangeModule == 'map'
            ? 'icon-map-module-active'
            : 'icon-map-module',
        ]"
      ></div>
      <div
        @click="moduleChange('generalization')"
        :class="[
          'change-item',
          mapChangeModule == 'generalization'
            ? 'icon-generalization-module-active'
            : 'icon-generalization-module',
        ]"
      ></div>

      <!-- 拓扑图流域筛选 -->
      <div style="width: 110px" v-if="mapChangeModule == 'generalization'">
        <el-select v-model="basinArea" placeholder="请选择">
          <el-option
            v-for="item in basinAreaList"
            :key="item"
            :label="item"
            :value="item"
          >
          </el-option>
        </el-select>
      </div>

      <!-- 拓扑图信息多选 -->
      <div class="general-info-list" v-if="mapChangeModule == 'generalization'">
        <div
          :class="generalInfoArr.indexOf('名称') != -1 ? 'active' : ''"
          class="general-info-item"
          @click="checkedInfo('名称')"
        >
          <p><i class="icon-general-name"></i></p>
          <p><span>名称</span></p>
        </div>
        <div
          class="general-info-item"
          :class="generalInfoArr.indexOf('水位') != -1 ? 'active' : ''"
          @click="checkedInfo('水位')"
        >
          <p><i class="icon-general-waterlevel"></i></p>
          <p><span>水位</span></p>
        </div>
        <div
          class="general-info-item"
          :class="generalInfoArr.indexOf('流量') != -1 ? 'active' : ''"
          @click="checkedInfo('流量')"
        >
          <p><i class="icon-general-flow"></i></p>
          <p><span>流量</span></p>
        </div>
      </div>
    </div>

    <!-- 左侧边栏控件 -->
    <left-panel
      ref="LeftPanel"
      :typhoonShow="typhoonShow"
      :height="sideHeight"
      :typhoonData="typhoonData"
      @open-analyse-dialog="openAnalyseDialog"
      @legend-change="handleLegendChange"
      @clickRealtimeRainfull="rainfullClick"
    />

    <!-- 右侧边栏控件 -->
    <right-panel
      ref="RightPanel"
      :height="sideHeight"
      @FloodForecasting="FloodForecasting"
      @legend-change="handleLegendChange"
      @open-analyse-dialog="openAnalyseDialog"
    />
    <el-select
      v-if="false"
      v-model="value"
      class="float-select"
      placeholder="请选择"
    >
      <el-option value="等值面">等值面</el-option>
      <el-option value="网格">网格</el-option>
    </el-select>

    <!-- 大中型水库弹窗 -->
    <big-reservoir-dialog
      v-if="BigReservoirShow"
      :visible.sync="BigReservoirShow"
      @sendValue="sendValue"
      @closeDialog="closeBigReservoirShow"
    />

    <!-- 洪水预报弹窗 -->
    <flood-forecasting-dialog
      v-if="FloodForecastingShow"
      :lookDetailsData="FloodForecastingVal"
      :visible.sync="FloodForecastingShow"
      @closeDialog="closeFloodDialogShow"
    />

    <!-- 详情公共弹窗 -->
    <details-common-dialog
      :code="codeTpye"
      v-if="destroy"
      :mediumSized="mediumSized"
      :visible="analyseDialogShow"
      @close="closeAnalyseDialogShow"
    />

    <!-- 图例 -->
    <div
      v-show="showLegend"
      :class="['map-legend', isBigScreen && 'is-big-screen']"
      :style="{ ...legendStyle }"
    >
      <primary-legend title="图例" ref="mapLegend">
        <ul>
          <li v-for="(subLegends, subIndex) in legends" :key="subIndex">
            <div class="legend-left">{{ subLegends.label }}</div>
            <div class="legend-right">
              <div
                v-for="(item, index) in subLegends.children"
                class="legend-item"
                :key="index"
              >
                <i :class="item.icon" />{{ item.label || "" }}
              </div>
            </div>
          </li>
        </ul>
      </primary-legend>
    </div>

    <!-- 小场景切换按钮 -->
    <!-- <div class="change-small" :style="{ ...changeStyle }" @click="goToSmall">
      <span>小切口</span>
      <i class="el-icon-right"></i>
    </div> -->

    <project-dialog
      v-if="projectDialogVisible"
      :remove-tabs="projectDialogRemoveTabs"
      :visible="projectDialogVisible"
      :point-data="pointData"
      @close="handleProjectDialogClose"
    />

    <!-- 雨情弹框 -->
    <primary-dialog
      :mainTitle="dialogTitle"
      :hasAnalysis="false"
      :visible="waterRainDialog"
      @handleClose="handleWaterRainClose"
      width="1129px"
    >
      <div class="dialog-content">
        <rain-dialog-content
          :dialogType="dialogType"
          :stationData="pointData"
          ref="rainDialog"
        />
      </div>
    </primary-dialog>

    <!-- 风险隐患弹框 -->
    <primary-dialog
      mainTitle="风险详情"
      :hasAnalysis="false"
      v-if="riskDialog"
      :visible="riskDialog"
      @handleClose="handleRiskClose"
      width="920px"
    >
      <div class="dialog-content">
        <RiskDialogContent :pointData="pointData"></RiskDialogContent>
      </div>
    </primary-dialog>

    <!-- 山洪弹窗 -->
    <primary-dialog
      mainTitle="山洪点详情"
      :hasAnalysis="false"
      v-if="floodDetailDialog"
      :visible="floodDetailDialog"
      @handleClose="handleFloodDetailClose"
      width="920px"
    >
      <div class="dialog-content">
        <FloodDetailContent :pointData="pointData"></FloodDetailContent>
      </div>
    </primary-dialog>

    <!-- 堤防保险点位弹窗 -->
    <primary-dialog
      mainTitle="保险理赔详情"
      :hasAnalysis="false"
      v-if="dikeInsuranceDialog"
      :visible="dikeInsuranceDialog"
      @handleClose="handleDikeInsuranceClose"
      width="920px"
    >
      <div class="dialog-content">
        <DikeDisasterContent></DikeDisasterContent>
      </div>
    </primary-dialog>

    <WarningWidget v-if="warningListShow" :style="warningWidgetStyle" />

    <SilderTimelineBar
      :autoPlay="autoPlayFlag"
      ref="SilderTimelineBar"
      v-if="
        currentScene == '防御复盘' ||
        (currentScene == '防御态势' && previewModule)
      "
    />

    <!-- <div
      v-if="currentScene == '防御复盘'"
      :style="rainfullControlStyle"
      class="rainfull-control is-big-screen"
    >
      <div @click="rainfullClick" :class="rainfullControlClass" />
    </div> -->
  </div>
</template>


<script>
import {
  PrimaryDialog,
  PrimaryLegend,
  ProjectDialog,
  PlayableTimelineBar,
  SilderTimelineBar,
} from "@/components";
import RainDialogContent from "../hydraulic_amap/components/dialog/StationDialog";
//风险点弹框
import RiskDialogContent from "./components/RiskPointContent.vue";
import DikeDisasterContent from "./components/DikeDisasterContent.vue";
//山洪弹框
import FloodDetailContent from "./components/FloodDetailContent.vue";
// 图层控件
import ScreenMap from "./components/ScreenMap";
// 左侧边栏控件
import leftPanel from "./SidePanel/LeftPanel/";
// 右侧边栏控件
import RightPanel from "./SidePanel/RightPanel/";
// 大中型水库
import BigReservoirDialog from "./dialog/BigReservoirDialog";
//洪水预报弹窗
import FloodForecastingDialog from "./dialog/FloodForecastingDialog ";
// 详情公共弹窗
import DetailsCommonDialog from "./dialog/DetailsCommonDialog.vue";
import { mapActions, mapGetters } from "vuex";
import WarningWidget from "./components/WarningWidget.vue";
//概化图组件
import GeneralizationMap from "./components/GeneralizationMap";
import PreviewCard from "./components/PreviewCard.vue";

export default {
  components: {
    ScreenMap,
    leftPanel,
    RightPanel,
    DetailsCommonDialog,
    PrimaryDialog,
    PrimaryLegend,
    ProjectDialog,
    BigReservoirDialog,
    FloodForecastingDialog,
    WarningWidget,
    GeneralizationMap,
    RainDialogContent,
    SilderTimelineBar,
    RiskDialogContent,
    DikeDisasterContent,
    PreviewCard,
    FloodDetailContent,
  },
  data() {
    return {
      typhoonData: {},
      // mapChangeModule:'map',
      value: "等值面",
      sideHeight: 0, //侧边栏自适应高度
      analyseDialogShow: false, //详情公共弹窗
      legends: {},
      showLegend: false,
      codeTpye: "",
      typhoonShow: false,
      destroy: false,
      BigReservoirShow: false,
      //洪水预报
      FloodForecastingShow: false,
      FloodForecastingVal: [],
      projectDialogVisible: false,
      pointData: {},
      rightPanelWidth: 0,
      leftPanelWidth: 0,
      warningList: [
        { type: "暴雨", count: 1 },
        { type: "水库", count: 1 },
        { type: "潮位", count: 1 },
        { type: "河网", count: 1 },
        { type: "山洪", count: 1 },
      ],
      waterRainDialog: false,
      riskDialog: false,
      dialogTitle: "",
      mediumSized: "",
      basinArea: "瓯江",
      basinAreaList: ["瓯江", "飞云江", "鳌江"],
      generalInfoArr: ["名称"],
      dialogType: "PP",
      /** 工程弹框动态移除tab */
      projectDialogRemoveTabs: [
        // "工程巡查",
        // "规划档案",
        // "建设档案",
        "运行管理",
        // "洪水预测",
      ],
      dikeInsuranceDialog: false,
      floodTitle: "",
      floodDetailDialog: false,
    };
  },
  mounted() {
    this.$nextTick(() => {
      //存入$store
      this.SetMapComponent(this.$refs.map);
      //
      this.hideWarningList();
      // this.$refs.LeftPanel.map = this.$refs.map; //左侧边栏图层控件
      this.$refs.RightPanel.map = this.$refs.map; //右侧边栏图层控件
      this.handleWindowResize(); //侧边栏高度自适应
      window.addEventListener("resize", this.handleWindowResize);
      this.rightPanelWidth = this.$refs.RightPanel.$el.clientWidth;
      this.leftPanelWidth = this.$refs.LeftPanel.$el.clientWidth;
      console.log('131321313131313131313213')
      let sceneType = this.$route.query.sceneType;
      if(sceneType=="防御复盘"){
        this.changeScene(sceneType);
        this.moduleChange('map');
      }
      // else if(sceneType=="三江调度"){
      //   this.moduleChange('generalization');
      // }
    });
  },
  beforeDestroy() {
    //钩子函数销毁前调用
    window.removeEventListener("resize", this.handleWindowResize);
  },
  computed: {
    ...mapGetters([
      "warningListShow",
      "collapseLeft",
      "collapseRight",
      "screenLoading",
      "currentScene",
      "mapChangeModule",
      "analyseData",
      "rainfullActive",
      "previewModule",
      "formatterStartTime",
      "formatterEndTime",
    ]),
    rainfullControlStyle() {
      return this.analyseData == "typhoon"
        ? { left: "520px" }
        : { left: "260px" };
    },
    rainfullControlClass() {
      return this.rainfullActive == true
        ? "icon-dzm-normal-active"
        : "icon-dzm-normal";
    },
    isBigScreen() {
      return this.$route.query.large == "true";
    },
    warningWidgetStyle() {
      let left = "0";
      if (this.collapseLeft) {
        left = `${this.leftPanelWidth - 15}px`;
        if (this.isBigScreen) left = `${this.leftPanelWidth / 2 - 15}px`;
      }
      return { left };
    },
    legendStyle() {
      let right = "35.5rem";
      if (this.collapseRight) {
        right = this.rightPanelWidth * 0.5 + "px";
      } else {
        right = "1rem";
      }
      return {
        right: right,
      };
    },
    changeStyle() {
      let left = "35.5rem";
      if (this.collapseLeft) {
        left = this.rightPanelWidth * 0.5 + "px";
      } else {
        left = "1rem";
      }
      return {
        left: left,
      };
    },
    autoPlayFlag() {
      return this.currentScene == "防御态势";
    },
  },
  watch: {
    "$store.state.typhoonShowVal": function (val, oldval) {
      this.typhoonShow = val;
    },
    generalInfoArr: function (val) {
      this.$store.commit("SET_GENERAL_INFOARR", val);
    },
    basinArea: function (val) {
      this.$store.commit("SET_BASIN_AREA", val);
    },
    mapChangeModule(val) {
      if (val == "map") {
        this.$nextTick(() => {
          // console.log(this.$refs.map);
          this.SetMapComponent(this.$refs.map);
        });
      }
    },
    warningListShow(val) {
      let audioPlay = this.$refs.audio;
      if (val) {
        this.$nextTick(() => {
          audioPlay.play();
        });
      } else {
        audioPlay.pause();
        audioPlay.currentTime = 0;
      }
    },
  },
  methods: {
    ...mapActions(["changeRainfullActive","changeScene"]),
    rainfullClick() {
      this.changeRainfullActive();
      this.$refs.SilderTimelineBar.loadRainFull(
        this.$refs.SilderTimelineBar.value
      );
    },
    //拓扑图信息选中
    checkedInfo(type) {
      let idx = this.generalInfoArr.indexOf(type);
      if (idx != -1) {
        this.generalInfoArr.splice(idx, 1);
      } else {
        this.generalInfoArr.push(type);
      }
    },
    displayRainFallForecast() {
      // console.log(444);
    },
    moduleChange(type) {
      if (type == "map") {
        this.$store.commit("SET_RIGHT_COLLAPSE", true);
        this.$store.commit("SET_LEFT_COLLAPSE", true);
      } else {
        this.$store.commit("SET_RIGHT_COLLAPSE", false);
        this.$store.commit("SET_LEFT_COLLAPSE", false);
      }
      this.$store.commit("SetMapChangeModule", type);
      // let panelFlag = false;
      // if(type == 'map'){
      //   panelFlag = true;
      // }
      // if(this.$refs.LeftPanel.handleLeftPanelchange){
      //   this.$refs.LeftPanel.handleLeftPanelchange(panelFlag);
      //   this.$refs.RightPanel.handleLeftPanelchange(panelFlag);
      // }
    },
    sendValue(val) {
      if (val.length > 0) {
        this.FloodForecastingVal = val;
        this.FloodForecastingShow = true;
      } else {
        this.FloodForecastingShow = false;
      }
    },
    /**
     * 返回
     */
    getBack() {
      this.typhoonShow = false;
      this.$store.commit("typhoonShowList", false);
    },
    ...mapActions(["hideWarningList", "SetMapComponent"]),
    /**
     * 洪水预报
     */
    FloodForecasting() {
      this.BigReservoirShow = true;
    },
    closeBigReservoirShow() {
      this.BigReservoirShow = false;
    },
    /**
     * 关闭洪水预报弹窗
     */
    closeFloodDialogShow() {
      this.FloodForecastingShow = false;
    },
    handleLegendChange(name, legend) {
      if (JSON.stringify(legend) == "{}") {
        delete this.legends[name];
      } else {
        this.legends[name] = legend;
      }
      this.showLegend = JSON.stringify(this.legends) != "{}";
      this.$forceUpdate();
    },
    /**
     * 侧边栏高度自适应方法
     */
    handleWindowResize() {
      this.sideHeight = this.$refs.bigScreen.offsetHeight - 160;
    },
    /**
     * 图层点击事件
     */
    showDialog(val) {
      let dialogList = [
        "水库",
        "山塘",
        "堤防",
        "海塘",
        "泵站",
        "水闸",
        "闸站",
        "灌区",
        "农饮水",
        "水电站",
        "雨量",
        "复盘雨量",
        "河网",
        "潮位",
        "风险隐患",
        "堤防保险",
        "山洪",
      ];
      if (!val.type || !dialogList.includes(val.type)) return;
      this.pointData = val;
      // console.log(val,987)
      if (val.type == "水库") {
        this.pointData.start = new Date(this.formatterStartTime()).format(
          "yyyy-MM-dd hh:mm:ss"
        );
        this.pointData.end = new Date(this.formatterEndTime()).format(
          "yyyy-MM-dd hh:mm:ss"
        );
      }

      // console.log(this.pointData);
      switch (val.type) {
        case "雨量":
          {
            this.dialogType = "PP";
            this.dialogTitle = val.name;
            this.$nextTick(() => {
              if (this.previewModule) {
                this.$refs.rainDialog.getPreviewRainStationInfo();
                // "formatterStartTime", "currentScene","formatterCurrentTime"
              } else {
                this.$refs.rainDialog.getRainStationInfo();
              }
            });
            this.waterRainDialog = true;
          }
          break;
        case "河网":
          {
            this.dialogType = "ZZ";
            this.dialogTitle = val.name;
            this.$nextTick(() => {
              this.$refs.rainDialog.getWaterStationInfo();
            });
            this.waterRainDialog = true;
          }
          break;
        case "复盘雨量":
          {
            this.dialogType = "PP";
            this.dialogTitle = val.name;
            this.$nextTick(() => {
              this.$refs.rainDialog.getRainStationInfo("复盘雨量");
            });
            this.waterRainDialog = true;
          }
          break;
        case "潮位":
          {
            this.dialogType = "TT";
            this.dialogTitle = val.name;
            this.$nextTick(() => {
              this.$refs.rainDialog.getWaterStationInfo();
            });
            this.waterRainDialog = true;
          }
          break;
        case "风险隐患":
          {
            this.riskDialog = true;
          }
          break;
        case "堤防保险":
          {
            this.dikeInsuranceDialog = true;
          }
          break;
        case "山洪":
          {
            this.floodDetailDialog = true;
          }
          break;
        case "水库":
          {
            if (this.previewModule) {
              this.dialogType = "RR";
              this.dialogTitle = val.name;
              this.$nextTick(() => {
                this.$refs.rainDialog.getPreWaterStationInfo();
              });
              this.waterRainDialog = true;
            } else {
              this.projectDialogVisible = true;
            }
          }
          break;
        default:
          {
            this.projectDialogVisible = true;
          }
          break;
      }
    },
    handleRiskClose() {
      this.riskDialog = false;
    },
    handleDikeInsuranceClose() {
      this.dikeInsuranceDialog = false;
    },
    handleProjectDialogClose() {
      this.projectDialogVisible = false;
    },
    handleFloodDetailClose() {
      this.floodDetailDialog = false;
    },
    /**
     * 打开详情公共弹窗
     */
    openAnalyseDialog(code, data) {
      this.mediumSized = data;
      this.analyseDialogShow = true;
      this.codeTpye = code;
      this.destroy = true;
    },
    /**
     * 关闭详情公共弹窗
     */
    closeAnalyseDialogShow() {
      this.analyseDialogShow = false;
      this.destroy = false;
    },
    /**
     * 改变面雨量loading
     */
    changeRainfallLoading(type) {
      this.mapRainfallLoading = type;
    },
    typhoonPointClick(data) {
      this.typhoonData = {};
      this.typhoonData = data;
    },
    //关闭水雨情弹窗
    handleWaterRainClose() {
      this.waterRainDialog = false;
      this.pointData = {};
    },
    goToSmall() {
      this.$router.push("/waterDisasterCustom?large=true");
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
@import "@/style/sidebarStyle.scss";
.rainfull-control {
  position: absolute;
  width: 100px;
  height: 100px;
  top: 80px;
  z-index: 100;
  cursor: pointer;
}
.test {
  z-index: 1000;
  width: 60px;
  height: 60px;
  position: absolute;
  top: 6rem;
  right: 32rem;
  background: url(~assets/images/WaterDisastersImage/get-back.png) no-repeat top
    center;
  background-size: 100% 100%;
}
.change-area {
  position: absolute;
  top: 11%;
  right: 566px;
  z-index: 1000;
  color: #fff;
  @include flexbox;
  @include flexflow(column, nowrap);
  align-items: flex-end;
  &.area-right {
    right: 24px;
  }
  .change-item {
    padding: 6px 0;
    cursor: pointer;
    text-align: right;
    margin-bottom: 12px;
    &.active {
      color: #00a0ff;
    }
  }

  .general-info-list {
    margin-top: 28px;
    .general-info-item {
      width: 60px;
      height: 60px;
      border-radius: 10px;
      cursor: pointer;
      margin-bottom: 10px;
      @include flexbox;
      @include flexflow(column, nowrap);
      @include flexJC(center);
      @include flexAC;
      p {
        margin-bottom: 4px;
        i {
          display: inline-block;
        }
        font-size: 14px;
        &:last-child {
          margin-bottom: 0px;
        }
      }
      &.active {
        background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
      }
    }
  }
}
.map-legend {
  bottom: 7%;
  width: 260px;
  &.is-big-screen {
    transform: scaleX(0.5) translate(50%);
  }
  ul {
    max-height: 200px;
    overflow-y: auto;
    li {
      align-items: center;
      border-bottom: 1px solid #00c1ff;
      &:last-child {
        border-bottom: none;
      }
      .legend-left {
        padding: 10px 20px;
        margin-right: 0;
        color: #fff;
        > p:nth-child(1) {
          font-family: PingFangSC-Semibold;
          font-size: 16px;
          color: #00c1ff;
          letter-spacing: 0;
          line-height: 32px;
          font-weight: 600;
          opacity: 1;
        }
        > p {
          padding: 5px 0;
          opacity: 0.6;
          color: #a6dff6;
          > i {
            margin-right: 10px;
          }
        }
      }
      .legend-right {
        border-left: 1px solid #00c1ff;
        padding-left: 16px;
        flex-direction: column;
        span {
          width: 100%;
        }
        .legend-item {
          .icon-sluice,
          .icon-sluice-warn {
            width: 24px;
            height: 24px;
          }
        }
      }
      .item-color-block {
        width: 25px;
        height: 25px;
      }
    }
  }
}
.change-small {
  width: 148px;
  height: 48px;
  position: absolute;
  bottom: 84px;
  background: url(~assets/images/custom-button-bg.png) no-repeat center center;
  background-size: 100% 100%;
  color: #fff;
  @include flexbox;
  @include flexflow(row, nowrap);
  @include flexJC(center);
  @include flexAC;
  cursor: pointer;
  transition: all 0.5s ease-in-out;
  span {
    display: inline-block;
    margin-right: 6px;
  }
}
.disaster-prevention {
  width: 100%;
  height: 100%;
  background: url(~assets/images/da-ping-bg.png) no-repeat top center;
  background-size: 100% 100%;
  .audio-content {
    position: absolute;
    z-index: 1001;
    top: 150px;
    left: 50%;
  }
  .preview-module-card {
    position: absolute;
    top: 140px;
    left: 50%;
    transform: translateX(-50%);
  }
}
// 去掉table表格纵向滚动条
::v-deep .el-table__body-wrapper::-webkit-scrollbar {
  height: 6px; // 纵向滚动条 必写
  width: 0px;
}
//设置table表格字体样式
::v-deep.sidebar-list .el-table .cell {
  font-size: 12px;
  opacity: 0.8;
}
//设置table表格 列宽，字间距样式
::v-deep .el-table {
  .cell {
    height: $tdHeight;
    line-height: $tdHeight;
  }
  // 设置勾选框样式
  .el-checkbox__inner {
    border: 0.0625rem solid #00c1ff;
    background-color: #00c1ff;
    background: rgba($color: #000000, $alpha: 0);
  }
  // 设置选中行样式颜色
  .el-table__body tr.current-row > td {
    background-color: rgba(41, 147, 180, 0.3);
  }
  th,
  td {
    padding: 0;
    color: #fff;
    font-size: 12px;
  }
  th {
    font-size: 14px;
    color: #00c1ff;
    background-color: rgba(41, 147, 180, 0.1);
  }
}
</style>
<style lang="scss" scoped>
.float-select {
  position: absolute;
  top: 10%;
  left: 480px;
  z-index: 101;
  ::v-deep.el-select .el-input__inner {
    border: 1px solid #36bee4;
  }
}
</style>
<style lang="scss">
.realtime-250 {
  background-color: #f32fdf;
  @include icon();
}
.realtime-100 {
  background-color: #fe323a;
  @include icon();
}
.realtime-50 {
  background-color: #ff8a21;
  @include icon();
}
.realtime-25 {
  background-color: #fecb6e;
  @include icon();
}
.realtime-10 {
  background-color: #4bc704;
  @include icon();
}
.realtime-0_1 {
  background-color: #00a0ff;
  @include icon();
}
.forecast-100 {
  background-color: #7f0140;
  @include icon();
}
.forecast-70 {
  background-color: #f800fb;
  @include icon();
}
.forecast-10 {
  background-color: #3cba3e;
  @include icon();
}
.forecast-0 {
  background-color: #fff;
  @include icon();
}
.forecast-50 {
  background-color: #0001e1;
  @include icon();
}
.forecast-25 {
  background-color: #60b8ff;
  @include icon();
}
.forecast-10 {
  background-color: #48b940;
  @include icon();
}
.forecast-1 {
  background-color: #92ff74;
  @include icon();
}
</style>
