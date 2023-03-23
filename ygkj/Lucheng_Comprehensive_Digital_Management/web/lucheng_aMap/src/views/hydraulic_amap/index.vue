<template>
  <div class="bigScreen" ref="bigScreen">
    <!-- 图层组件信息 -->
    <div class="bigScreen_map">
      <screen-map ref="map" class="screen_map" @showDialog="showDialog" @showRiversDialog="showRiversDialog" @programmeShowDialog="programmeShowDialog" @showJcBuildingDialog="showJcBuildingDialog" @showZhuanghaoDialog="showZhuanghaoDialog" @showProjectDialog="showProjectDialog" @showInScopeDialog="showInScopeDialog" @showRescueDialog="showRescueDialog" @showProductionProjDialog="showProductionProjDialog" @showWaterIntakeDialog="showWaterIntakeDialog" />
    </div>
    <!-- 左侧边栏 -->
    <screen-silde-panel position="left" @change="leftSildeChange" :height="sideHeight">
      <div class="sidebar" :style="{ width: '350px' }">
        <div class="catalog-title">
          <div class="catalog-title-primary">
            <div class="panel-item__title--light" />
            <span style="margin-left: 6px">图层目录</span>
            <el-input clearable v-model="layerSearch" prefix-icon="el-icon-search" placeholder="输入关键词查询" class="layer-search myClass" @input="handleLayerSearch" />
          </div>
        </div>

        <div class="layer-tree">
          <custom-tree v-if="!isRefresh" :default-expand-all="true" v-show="treeLoading" icon-class="el-icon-arrow-right" ref="layerTree" @check-change="checkChange" :data="layerData" show-checkbox :default-expanded-keys="defaultExpandedNode" :default-checked-keys="defaultCheckedNode" node-key="label" :props="{
              children: 'children',
              label: 'label',
            }">
            <span :class="[
                'custom-tree-node__label',
                node.disabled && node.isLeaf && 'disabled',
              ]" slot-scope="{ node, data }">
              <div v-if="node.level == 1" class="tree-icon">
                <i :class="data.icon" />
              </div>
              <div v-if="data.iconClass" class="layer-icon">
                <i :class="data.iconClass" />
              </div>
              {{ node.label }}
              <span v-if="data.hasOwnProperty('count')" class="tree-count">({{ data.count }})</span>
            </span>
          </custom-tree>
        </div>
      </div>
    </screen-silde-panel>

    <!-- 右侧边栏 -->
    <screen-silde-panel :width="458" position="right" :height="sideHeight" @change="rightSildeChange">
      <div class="sidebar" ref="rightPanel">
        <div class="side-tab">
          <primary-tabs v-model="tabActive" :list="tabList" ref="primaryTab" @input="changeView" @closeTab="closeTabHandle" />
        </div>
        <div class="side-content">
          <custom-router-view @typhoonDetail="handleShowTyphoonDetail" @closeTyphoonDetail="handleTyphoonDetailClose" />
          <!--   <router-view /> -->
        </div>
      </div>
    </screen-silde-panel>

    <!-- 水雨情弹窗 -->
    <primary-dialog :mainTitle="dialogTitle" :visible="waterRainDialog" @handleClose="handleWaterRainClose" @analysis="showAnalysis" width="1129px">
      <div class="dialog-content">
        <rain-dialog-content v-if="waterRainDialog" :dialogType="dialogType" :stationData="stationData" ref="rainDialog" />
      </div>
    </primary-dialog>

    <!-- 规划详情弹窗 -->
    <ProgrammeDialog :mainTitle="programmeTitle" :visible="programmeDialogShow" v-if="programmeDialogShow" @handleClose="closeProgrammeDialog" :ProgrammeData="ProgrammeData" width="1129px" />
    <!-- 河湖点位弹窗 -->
    <RiversInformationDialog ref="RiversInformation" :mainTitle="RiversTitle" v-if="RiversDialogShow" :visible="RiversDialogShow" @analysis="showAnalysis" @handleClose="CloseRiversDialogShow" :ProgrammeData="RiversDetailData" />

    <!-- 生产建设项目弹窗 -->
    <ProductionProjDialog ref="ProductionProjDialog" v-if="ProductionProjDialogShow" :visible="ProductionProjDialogShow" @handleClose="CloseTestDiaolog" />

    <!-- 取水户点位弹窗 -->
    <WaterIntakeDialog v-if="WaterIntakeShow" :visible="WaterIntakeShow" :WaterIntakeData="WaterIntakeData" @handleClose="CloseWaterIntakeDiaolog" />
    <project-dialog v-if="projectDetailDialogShow" :visible="projectDetailDialogShow" :point-data="projectDetailData" @close="handleProjectDetailClose" :removeTabs="removeTabs" @analysis="showAnalysis" :analysisFlag="true" />

    <!-- 交叉建筑物弹窗 -->
    <primary-dialog :mainTitle="dialogTitle" :visible="buildingDetailDialogShow" @handleClose="handleBuildingDetailClose" @analysis="showAnalysis" width="400px">
      <div class="dialog-content build_dialog" v-if="buildingDetailData.type !== '断面'">
        <div class="build_img">
          <img v-if="buildingDetailData && buildingDetailData.imgFileURL" :src="buildingDetailData.imgFileURL" alt="">
        </div>
        <div class="build_item">
          <span>名称：</span>
          {{buildingDetailData ? buildingDetailData.proName : '-'}}
        </div>
        <div class="build_item">
          <span>主体工程：</span>
          {{buildingDetailData ? buildingDetailData.prnm : '-'}}
        </div>
        <div class="build_item">
          <span>类型：</span>
          {{buildingDetailData ? buildingDetailData.proType : '-'}}
        </div>
        <div class="build_item">
          <span>面积：</span>
          {{buildingDetailData ? buildingDetailData.proArea : '-'}}
        </div>
        <div class="build_item">
          <span>简介：</span>
          {{buildingDetailData ? buildingDetailData.proInfo : '-'}}
        </div>

      </div>
      <div class="dialog-content build_dialog" v-if="buildingDetailData.type === '断面'">
        <div class="build_img">
          <img v-if="buildingDetailData && buildingDetailData.imgFileURL" :src="buildingDetailData.imgFileURL" alt="">
        </div>
        <div class="build_item">
          <span>断面名称：</span>
          {{buildingDetailData.name ? buildingDetailData.name : '-'}}
        </div>
        <div class="build_item">
          <span>堤顶高程</span>
          {{buildingDetailData.crestElevatio ? buildingDetailData.crestElevatio : '-'}}
        </div>
        <div class="build_item">
          <span>潮位：</span>
          {{buildingDetailData.tdz ? buildingDetailData.tdz : '-'}}
        </div>
        <div class="build_item">
          <span>H2：</span>
          {{buildingDetailData.H2 ? buildingDetailData.H2 : '-'}}
        </div>

      </div>

    </primary-dialog>

    <!-- 桩号弹窗 -->
    <primary-dialog :mainTitle="dialogTitle" :visible="zhuanghaoDetailDialogShow" @handleClose="handleZhuanghaoDetailClose" @analysis="showAnalysis" width="600px">
      <div class="dialog-content build_dialog">
        <div class="build_line">
          <div class="build_item">
            <span>桩号：</span>
            {{zhuanghaoDetailData ? zhuanghaoDetailData.stake : '-'}}
          </div>
          <div class="build_item">
            <span>断面类型：</span>
            {{zhuanghaoDetailData ? zhuanghaoDetailData.sectionType : '-'}}
          </div>
        </div>
        <div class="build_line">
          <div class="build_item">
            <span>所属标段：</span>
            {{zhuanghaoDetailData ? zhuanghaoDetailData.bidSection : '-'}}
          </div>
          <div class="build_item">
            <span>主体工程：</span>
            {{zhuanghaoDetailData ? zhuanghaoDetailData.prnm : '-'}}
          </div>
        </div>
        <div class="build_item">
          <span>断面图：</span>
        </div>
        <div class="build_img_big">
          <img v-if="zhuanghaoDetailData && zhuanghaoDetailData.stakeFileURL" :src="zhuanghaoDetailData.stakeFileURL" alt="">
        </div>
      </div>
    </primary-dialog>

    <!-- 周边分析弹框 -->
    <primary-dialog :mainTitle="dialogTitle" :visible="inScopeDialogShow" @handleClose="handleInScopeClose" @minimize="minimize" :hasAnalysis="false" :minimizeStatus="true" width="60%">
      <div class="dialog-content scope-content">
        <scope-dialog-content :scopeTreeData="scopeTreeData" :rangeShow="rangeShow" @setscopeTreeData="setscopeTreeData" @changeRange="changeRange" ref="scopeDialog" />
      </div>
      <div slot="footer">
        <el-button type="primary" @click="minimize">确 定</el-button>
        <el-button @click="handleInScopeClose">取 消</el-button>
      </div>
    </primary-dialog>

    <!-- 应急救援弹框 -->
    <primary-dialog :mainTitle="dialogTitle" :visible="rescueDialogShow" @handleClose="handleRescueClose" :hasAnalysis="false" width="920px">
      <div class="dialog-content">
        <rescue-dialog v-if="rescueDialogShow" :dialogType="dialogType" :info="rescueDialogInfo" ref="rescueDialog" />
      </div>
    </primary-dialog>

    <!-- 最小化弹窗 -->
    <el-button class="minimizeBtn" :style="{ ...minimizeStyle }" v-show="minimizeStatus" @click="maximize">周边分析</el-button>

    <playable-timeline-bar v-if="timelinebarShow" dark :timeline-list="timelineList" :before-play-next="displayRainFallForecast" @change="handleTimelineChange" />
    <!-- 只看在线摄像头 -->
    <div v-if="checkShow" class="map-legend" :style="{ ...leftLegendStyle }">
      <div :class="checked ? 'checked-style' : 'checked-sty'">
        <el-checkbox v-model="checked" @change="checkedData">只看在线摄像头</el-checkbox>
      </div>
    </div>
    <!-- 扰乱图斑 -->
    <div v-if="patternShow" class="map-legend" :style="{ ...leftLegendStyle }">
      <div class="pattern-spot-sty">
        <el-select v-model="yearPattern" clearable placeholder="历史对比" @change="changePatternYaer">
          <el-option label="2018" value="2018" />
          <el-option label="2019" value="2019" />
          <el-option label="2020" value="2020" />
        </el-select>
      </div>
    </div>
    <!-- 数字珊溪弹框 -->
    <primary-dialog class="shanxiDialog" mainTitle="数字珊溪" :visible="shanxiDialogShow" @handleClose="
        () => {
          this.shanxiDialogShow = false;
          this.$refs.layerTree.setChecked('数字珊溪', false);
        }
      " @changeFullscreen="() => (fullscreenStatus = !fullscreenStatus)" :hasAnalysis="false" :fullscreenFlag="true" :fullscreen="fullscreenStatus" width="1200px">
      <div class="dialog-content" style="height: 100%">
        <iframe class="common-item" src="https://zhsx.wzsl.com/skip?pg=map/mapIndex&token=eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.d5E_lKEs4bb0ON4q7zpyyGarlDmDdEQlpkQtNnOb5lbIOzcdKD6_kbAAQ2vNv1DYfI8sTy5_HCqEfrkOQO_DIbNVM0sP5phQ9noiVNdBGul2wWT9xrbZXqmZWTwcn-PHwr7gv_4vUOmHEm-48rlzvQ87AcUhFar5Bnfa-bxYBZs.hxDJAQq4eAFurxth.xzrHYRbARg5OwIyx8Vvtp930zgUUOKlIJS64ov5eq7HFuSo2TEzPgv51jiwBKoEv6S1h61V8nw5Egg4Rs1yCj7P9fz1J4R4Yvm9IBg9wrrJJWoY0gQrrbPii8tlhOSocw1SF3jEaimTZbW9QPN5EmI-dD0VTtDlEysBi.JSz7ocbMEqiesvRFmMeAEA" frameborder="0" width="100%"></iframe>
      </div>
    </primary-dialog>
    <!-- 图例 -->
    <div v-if="legends.length > 0" class="map-legend" :style="{ ...legendStyle }">
      <primary-legend title="图例" ref="mapLegend">
        <ul>
          <li v-for="(subitem, subindex) in legends" :key="subindex">
            <div class="legend-name">
              {{ subitem.label }}
              <i :ref="subitem.clusterLayer ? subitem.clusterLayer[0] : ''" :class="
                  subitem.checked
                    ? 'el-icon-location'
                    : 'el-icon-location-outline'
                " title="聚合" @click="doClusterAction(subitem)"></i>
            </div>
            <div class="legend-list">
              <span v-for="(item, index) in subitem.legends" :key="index" class="rainLegend">
                <i :class="item.icon" />
                {{ item.label }}
              </span>
            </div>
          </li>
        </ul>
      </primary-legend>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import ScreenMap from "./components/ScreenMap";
import CardTabs from "./components/CardTabs.vue";
import {
  PrimaryTabs,
  PrimaryDialog,
  PrimaryLegend,
  PlayableTimelineBar,
  CustomTree,
  ProjectDialog,
  ProgrammeDialog,
  WaterIntakeDialog,
  RiversInformationDialog,
  BuildDialog
} from "@/components";
import { layersData } from "./assets/layers";
import RainDialogContent from "./components/dialog/StationDialog";
import ScopeDialogContent from "./components/dialog/ScopeDialog";
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import { rainApi } from "@/api/dataScreen/floodJudge/index";
import { ScreenSildePanel } from "@/components";
import Measure from "@/utils/measure";
import { mapActions, mapGetters } from "vuex";
import { layerPointModule } from "./modules/layerPoint";
import { projectModule } from "./modules/projectModule";
import { stationModule } from "./modules/stationModule";
import { weatherModule } from "./modules/weatherModule";
import { ProgrammeModule } from "./modules/ProgrammeModule";
import { InformationOfLakesModule } from "./modules/InformationOfLakesModule";

import { rescueModule } from "./modules/rescueModule";
import RescueDialog from "./components/dialog/RescueDialog";
import ProductionProjDialog from "./components/dialog/ProductionProjDialog";
/**
 * @description 简单封装用于自定义事件
 */
const CustomRouterView = {
  name: "CustomRouterView",
  render() {
    return <router-view />;
  },
  methods: {
    showTyphoonDetail(code) {
      this.$emit("typhoonDetail", code);
    },
    closeTyphoonDetail() {
      this.$emit("closeTyphoonDetail");
    },
  },
};

export default {
  name: "HydraulicAmap",
  props: {},
  components: {
    ScreenMap,
    PrimaryTabs,
    PrimaryDialog,
    PrimaryLegend,
    RainDialogContent,
    ScopeDialogContent,
    ScreenSildePanel,
    CustomRouterView,
    PlayableTimelineBar,
    RiversInformationDialog,
    WaterIntakeDialog,
    CustomTree,
    ProjectDialog,
    ProgrammeDialog,
    CardTabs,
    RescueDialog,
    BuildDialog,
    ProductionProjDialog,
  },
  data() {
    return {
      ProductionProjDialogShow: false, // 生产建设项目
      RiversDialogShow: false, //河湖信息
      RiversDetailData: {}, //河湖信息数据
      WaterIntakeShow: false,
      WaterIntakeData: {},
      RiversTitle: "",
      checkShow: false,
      patternShow: false,
      checked: false,
      timelinebarShow: false,
      rainfallType: "",
      //图层树
      layerData: layersData,
      defaultExpandedNode: [], //树默认展开结点
      defaultCheckedNode: [], //树默认选中结点
      tabList: [], //右侧tab列表
      tabActive: 0,
      waterRainDialog: false,
      dialogTitle: "",
      // 规划
      ProgrammeData: {},
      programmeTitle: "规划",
      programmeDialogShow: false,
      stationData: {}, //测站弹框-测站信息
      dialogType: "RR",
      checkedList: [],
      projectDetailDialogShow: false, //水利工程弹框开关flag
      projectDetailData: null,
      buildingDetailDialogShow: false, //交叉建筑物弹窗
      buildingDetailData: {
        type:'非断面'
      },
      zhuanghaoDetailDialogShow: false, //桩号弹窗
      zhuanghaoDetailData: null,
      inScopeDialogShow: false, // 测面弹框开关
      rightCollapse: true,
      leftCollapse: true,
      scopeTreeData: [
        // 测面结果数据
        {
          name: "水利工程",
          children: [],
        },
      ],
      minimizeStatus: false, //最小化状态
      rangeShow: false, //距离范围状态
      sideHeight: 0,
      centerPoint: [],
      treeLoading: true,
      layerSearch: "",
      removeTabs: [],
      rescueDialogShow: false, // 应急救援弹框
      rescueDialogInfo: {},
      windTimes: [],
      windDatas: null,
      waveTimes: [],
      waveDatas: null,
      checkedShow: "",
      yearPattern: "2018",
      moudleType: "水灾害防御",
      isRefresh: false,
      shanxiDialogShow: false, // 数字珊溪弹窗
      fullscreenStatus: false, // 珊溪图层全屏
    };
  },
  watch: {
    buildingDetailData: {
      immediate: true,
      deep:true,
      handler: function (val) {
        if (val) {
          console.log(val, 'val');
        }
      }
    }
  },
  mounted() {
    const arr = this.$route.query.type;
    let selectType = [];
    if (arr) {
      selectType = arr.split(",");
    } else {
      selectType = ["水库"];
    }
    this.changeDefault(selectType);
    this.clearLegends();
    // this.addLegend(selectType);
    this.handleWindowResize();
    this.SetMapComponent(this.$refs.map);
    this.getWindTimes();
    this.getWaveTimes();
    this.getProjectCount();
    window.addEventListener("resize", this.handleWindowResize);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  computed: {
    ...mapGetters(["MapComponent"]),
    legends() {
      return this.$store.getters.displayLegends;
    },
    legendStyle() {
      return { right: this.rightCollapse == false ? "3.125rem" : "29.375rem" };
    },
    leftLegendStyle() {
      return { left: this.leftCollapse == false ? "42px" : "430px" };
    },
    minimizeStyle() {
      return { left: "500px" };
    },
    timelineList() {
      let listMap = {
        past: [
          { label: "1小时", value: 60 },
          { label: "3小时", value: 180 },
          { label: "6小时", value: 360 },
          { label: "12小时", value: 720 },
          { label: "24小时", value: 1440 },
          { label: "72小时", value: 4320 },
        ],
        forecast: [
          { label: "1小时", value: 1 },
          { label: "3小时", value: 3 },
          { label: "6小时", value: 6 },
        ],
      };
      return listMap[this.rainfallType];
    },
  },
  methods: {
    /**
     * via @/store/modules/legends.js
     */
    ...mapActions([
      "addLegend",
      "removeLegend",
      "clearLegends",
      "SetMapComponent",
    ]),
    //图层点位相关module
    ...layerPointModule,
    //水利工程相关操作module
    ...projectModule,
    //水利监控相关操作module
    ...stationModule,
    //气象相关操作module
    ...weatherModule,
    // 规划 点位展示
    ...ProgrammeModule,
    // 河湖信息点位
    ...InformationOfLakesModule,
    // 应急救援项管操作module
    ...rescueModule,
    doClusterAction(subitem) {
      if (subitem.checked) {
        this.MapComponent.map.getLayers().forEach((element) => {
          if (
            subitem.clusterLayer &&
            subitem.clusterLayer.indexOf(element.get("name")) != -1
          ) {
            element.getSource().setDistance(parseInt(30, 20));
          }
        });
        subitem.checked = false;
      } else {
        subitem.checked = true;
        this.MapComponent.map.getLayers().forEach((element) => {
          if (
            subitem.clusterLayer &&
            subitem.clusterLayer.indexOf(element.get("name")) != -1
          ) {
            element.getSource().setDistance(0);
          }
        });
      }
    },
    //判断侧边栏标签是否存在
    judgeSideExist(side) {
      let arr = this.tabList.map((item) => {
        return item.name;
      });
      if (arr.includes(side)) return false;
      return true;
    },
    //年份改变加载对应年份的扰动图斑
    changePatternYaer(val) {
      this.MapComponent.showPatternLayer(val);
    },
    //根据图层名称搜索图层
    handleLayerSearch(value) {
      let nodes = this.$refs.layerTree.store.nodesMap;
      if (this.layerSearch == "") return;
      let closeAllNode = () => {
        for (let i of Object.keys(nodes)) {
          nodes[i].expanded = false;
        }
      };
      let openNode = (node) => {
        node.expanded = true;
        if (node.parent) openNode(node.parent);
      };
      for (let i of Object.keys(nodes)) {
        const node = nodes[i];
        if (node.data.label.indexOf(this.layerSearch) != -1 && node.isLeaf) {
          closeAllNode();
          openNode(node.parent);
        }
      }
    },
    CloseRiversDialogShow() {
      this.RiversDialogShow = false;
    },
    checkedData(val) {
      this.checkedShow = val;
      let obj = {
        count: 3105,
        iconClass: "icon-catalog-video",
        id: 19,
        label: "视频",
        name: "视频",
        type: "水灾害防御",
        url: "/stationSide?type=视频",
      };
      this.checkChange(obj, true);
    },
    /**
     * 获取工程数量统计
     */
    getProjectCount() {
      this.treeLoading = false;
      this.isRefresh = true;
      hydraulicApi.countProjectNumber().then((res) => {
        const countData = {};
        console.log(res.data,'data');
        res.data.forEach((item) => {
          countData[item.type] = item.num;
        });
        let scan = (node) => {
          if (node.children) {
            for (let child of node.children) {
              child.name = child.label;
              if (countData.hasOwnProperty(child.label)) {
                child.count = countData[child.label];
              }
              // else{
              //   if (!child["children"] || child["children"].length == 0) {
              //     child.count = 0
              //   }
              // }
              if (child.hasOwnProperty("children")) {
                scan(child);
              }
            }
          }
        };
        this.layerData.forEach((item) => {
          scan(item);
        });
        this.isRefresh = false;
        this.treeLoading = true;
      });
    },
    handleShowTyphoonDetail(code) {
      this.removeSideTab("台风信息");
      this.addSideTab("台风信息", `/typhoonSearchInfo?code=${code}`);
    },
    handleTyphoonDetailClose() {
      this.removeSideTab("台风信息");
      this.clearTyphoonLayer();
    },
    /**
     * 设定默认类型
     * @description 根据跳转的路由query的type进行默认操作 无query.type则默认为水库
     */
    changeDefault(selectType) {
      const ignoreArr = ["台风分布"];
      let nodes = this.$refs.layerTree.store.nodesMap;
      this.defaultCheckedNode = [];
      selectType.map((item) => {
        if (!ignoreArr.includes(item)) {
          let url = this.findUrlInLayerTreeByName(item);
          if (url && this.judgeSideExist(item)) {
            this.addSideTab(item, url);
          }
        }
        if (nodes[item].parent) {
          this.defaultCheckedNode.push(item);
          // this.defaultExpandedNode = [nodes[selectType].parent.label];
        }
        this.addLegend(item);
        //根据图层名称加载点位
        this.loadPointByName(item);
      });
      // console.log();
      //默认展开父节点

      // console.log(nodes,12333)
    },
    //图层树中根据名称查找侧边栏路由
    findUrlInLayerTreeByName(selectType) {
      let url = "";
      let recursionSearch = (arr) => {
        if (arr.length == 0) return;
        for (let i = 0; i < arr.length; i++) {
          if (selectType == arr[i].label) {
            url = arr[i].url;
            break;
          }
          if (arr[i].children) {
            recursionSearch(arr[i].children);
          }
        }
      };
      recursionSearch(layersData);
      return url;
    },
    handleWindowResize() {
      this.sideHeight = this.$refs.bigScreen.offsetHeight;
    },
    rightSildeChange(collapse) {
      this.rightCollapse = collapse;
    },
    leftSildeChange(collapse) {
      this.leftCollapse = collapse;
    },
    /**
     * 右侧tab切换视图
     */
    changeView(value) {
      const type = this.tabList[value].name;
      this.$router.push({ path: this.tabList[value].url, query: { type } });
    },
    // 点击右侧tab上的×关闭图层
    closeTabHandle(val) {
      this.$refs.layerTree.setChecked(val.name, false);
    },
    //图层数选中改变方法
    checkChange(data, status) {
      const checkedList = this.$refs.layerTree.getCheckedNodes();
      let layerName = data.label;
      let currentLayer = null;
      if (layerName == "视频") {
        this.removeLegend(layerName);
        this.removeSideTab(layerName);
      }
      if (data.name == "视频" && status == true) {
        this.checkShow = true;
      } else if (data.name == "视频" && status == false) {
        this.checkShow = false;
      }
      if (checkedList.length == 0) {
        this.$refs.layerTree.setChecked(layerName, true);
      }
      if (status) {
        this.addLegend(layerName);
        // this.loadPointByName(layerName,data,status);
        if (data.url && this.judgeSideExist(layerName)) {
          this.addSideTab(layerName, data.url, data);
        }
        this.MapComponent.map.getLayers().forEach((element) => {
          if (element.get("id") == data.id) {
            currentLayer = element;
            element.setVisible(1);
            return;
          }
        });
        if (currentLayer == null) {
          this.loadPointByName(layerName, data);
        }
      } else {
        //取消选中时
        console.log(layerName)
        this.removeLegend(layerName);
        this.removeSideTab(layerName);
        this.MapComponent.clearCityLayer();
        this.unloadPointByName(layerName);
        this.MapComponent.map.getLayers().forEach((element) => {
          if (element.get("id") == data.id) {
            element.setVisible(0);
          }
        });
        if (["降雨分布", "降雨预报"].includes(layerName)) {
          this.timelinebarShow = false;
          this.MapComponent.clearIsohyetLayer();
          this.$set(this.layerData[4], "disabled", false);
          this.$set(this.layerData[5], "disabled", false);
        }
      }
    },
    /**
     * 添加侧边栏
     * @description 此方法会跳转至增加的视图
     */
    addSideTab(layerName, url, data = {}) {
      const idx = this.tabList.length;
      let obj = {
        value: idx,
        name: layerName,
        url: url,
        data: data,
      };
      this.tabList.push(obj);
      this.$router.push(url);
      this.tabActive = idx;
    },
    /**
     * 移除侧边栏
     */
    removeSideTab(layerName) {
      let idx = this.tabList.findIndex((item) => {
        return item.name == layerName;
      });
      if (idx != -1) {
        this.tabList.splice(idx, 1);
        this.tabActive = this.tabList.length - 1;
        this.$router.push(
          this.tabList[this.tabList.length - 1]
            ? this.tabList[this.tabList.length - 1].url
            : ""
        );
      }
    },
    //最小化弹窗
    minimize() {
      this.inScopeDialogShow = false;
      this.minimizeStatus = true;
      let list = this.$refs.scopeDialog.dataList;
      const selectNode = this.$refs.scopeDialog.selectNode;
      this.MapComponent.showAnalysisPoints(list, selectNode);
      if (this.centerPoint.length > 0) {
        this.MapComponent.flyPoint(this.centerPoint, 14);
      }
    },
    //还原弹窗
    maximize() {
      this.inScopeDialogShow = true;
      this.minimizeStatus = false;
    },
    //测面弹框
    showInScopeDialog(data, type) {
      if (type == "setVisible") {
        this.inScopeDialogShow = true;
      } else {
        this.dialogTitle = "周边分析";
        this.scopeTreeData[0].children = [];
        for (let key in data) {
          this.scopeTreeData[0].children.push({
            name: key,
            data: data[key],
          });
        }
        this.scopeTreeData[0].children = [
          ...new Set(this.scopeTreeData[0].children),
        ];
        this.inScopeDialogShow = true;
        this.$nextTick(() => {
          this.$refs.scopeDialog.doFirstNodeClick();
        });
      }
    },
    /**
     *河湖信息
     */
    showRiversDialog(val) {
      this.RiversTitle = val.projectName || val.outletName;
      if (val.type == "雨水口") {
        this.RiversDialogShow = false;
      } else {
        this.RiversDialogShow = true;
      }
      this.RiversDetailData = val;
      if (val.type == "在建工程") {
        this.$refs.RiversInformation.init(val);
      }
    },
    // 取水户
    showWaterIntakeDialog(val) {
      this.WaterIntakeShow = true;
      this.WaterIntakeData = val;
    },
    // 关闭取水户点位弹窗
    CloseWaterIntakeDiaolog() {
      this.WaterIntakeShow = false;
    },
    // 关闭河湖信息弹窗
    CloseRiversDiaolog() {
      this.RiversDialogShow = false;
    },
    // 生产建设项目弹窗 关闭
    CloseTestDiaolog() {
      this.ProductionProjDialogShow = false;
    },
    // 生产建设项目弹窗
    showProductionProjDialog(val) {
      this.ProductionProjDialogShow = true;
      this.$nextTick(() => {
        this.$refs.ProductionProjDialog.init(val);
      });
    },
    //周边分析弹框
    showAnalysis(type) {
      this.dialogTitle =
        (this.dialogTitle || this.RiversDetailData.name) + " — 周边分析";
      this.inScopeDialogShow = true;
      //周边分析标记
      this.rangeShow = true;
      this.waterRainDialog = false;
      this.RiversDialogShow = false;
      this.projectDetailDialogShow = false;
      this.scopeTreeData = [];
      if (type == "河道") {
        this.$nextTick(() => {
          this.$refs.scopeDialog.getRiverAnalysisTreeData(
            this.RiversDetailData
          );
          this.MapComponent._drawLineInMeter(
            this.RiversDetailData.coordinates,
            "3"
          );
        });
        return false;
      }
      this.$nextTick(() => {
        const pointData = this.stationData.longitude
          ? this.stationData
          : this.projectDetailData;
        const center = [pointData.longitude, pointData.latitude];
        this.$refs.scopeDialog.getAnalysisTreeData(pointData);
        this.MapComponent._drawCircleInMeter(center, 3000);
        this.centerPoint = center;
      });
    },
    //周边分析数据
    setscopeTreeData(val) {
      this.scopeTreeData = [];
      val.map((item) => {
        this.scopeTreeData.push(item);
      });
      this.$nextTick(() => {
        if (this.scopeTreeData.length > 0) {
          this.$refs.scopeDialog.doFirstNodeClick();
        }
      });
    },
    // 变更范围
    changeRange(val, type) {
      this.$nextTick(() => {
        // this.$refs.scopeDialog.getAnalysisTreeData(this.RiversDetailData);
        this.MapComponent._drawLineInMeter(
          this.RiversDetailData.coordinates,
          val
        );
      });
    },
    handleInScopeClose() {
      this.inScopeDialogShow = false;
      this.MapComponent.clearCirlceLayer();
      this.MapComponent.clearLineLayer();
      this.MapComponent.clearAnalysisPointsLayer();
      this.centerPoint = [];
      this.clearMeasure();
      this.MapComponent.resetScopeArr();
    },
    //展示台风
    showTyphoon(data) {
      this.MapComponent.showTyphoon(data);
    },
    clearTyphoonLayer() {
      this.MapComponent.clearTyphoonLayer();
      this.MapComponent.resetMap();
    },
    async displayRainFallForecast() {
      this.MapComponent.clearIsohyetLayer();
      let res = await rainApi.rainfallPredict({ hour: 1 });
      let params = this.formatRainfallParam(res.data, "drp");
      return this.getIsohyetArea(params);
    },
    async displayRainFallPast() {
      let opt = {
        minutes: val,
        min: "-1",
        max: "10000",
      };
      let res = await rainApi.realRain(opt);
      let params = this.formatRainfallParam(res.data, "drp");
      return this.getIsohyetArea(params);
    },
    handleTimelineChange(e) {
      let val = this.timelineList[e].value;
      if (this.rainfallType == "forecast") {
        this.getRainfallPredict(val);
      } else {
        this.getRealRainPredict(val);
      }
    },
    //获取预报雨量
    async getRainfallPredict(val) {
      let res = await rainApi.rainfallPredict({ hour: val });
      let params = this.formatRainfallParam(res.data, "drp");
      this.getIsohyetArea(params);
    },
    //获取实时雨量
    async getRealRainPredict(val) {
      let opt = {
        minutes: val,
        min: "-1",
        max: "10000",
      };
      let res = await rainApi.realRain(opt);
      let params = this.formatRainfallParam(res.data, "drp");
      this.getIsohyetArea(params);
    },
    //获取等雨量面
    async getIsohyetArea(opt) {
      let pollingTimer = null;
      let params = new URLSearchParams();
      params.append("station", JSON.stringify(opt));
      params.append("f", "json");
      let submitJobUrl =
        "http://117.149.227.112:6080/arcgis/rest/services/gps/dengzhiPolygonIDW/GPServer/dengzhiPolygonIDW/submitJob";
      let getJobUrl = (id) =>
        `http://117.149.227.112:6080/arcgis/rest/services/gps/dengzhiPolygonIDW/GPServer/dengzhiPolygonIDW/jobs/${id}/results/Idwres?f=pjson`;
      return new Promise((resolve) => {
        axios({
          url: submitJobUrl,
          method: "post",
          headers: { "content-type": "application/x-www-form-urlencoded" },
          data: params,
        }).then((res) => {
          pollingTimer = setInterval(() => {
            let url = getJobUrl(res.data.jobId);
            axios({
              url: url,
              method: "get",
            }).then((result) => {
              if (!result.data.value.mapImage.error) {
                clearInterval(pollingTimer);
                //加载透明等雨量面
                let mapImage = result.data.value.mapImage.extent;
                this.getIsohyetOpacity(
                  mapImage.xmin,
                  mapImage.ymin,
                  mapImage.xmax,
                  mapImage.ymax,
                  res.data.jobId
                );
                resolve();
              }
            });
          }, 1000);
        });
      });
    },
    //获取透明背景等雨量面
    async getIsohyetOpacity(xmin, ymin, xmax, ymax, jobId) {
      let bbox = xmin + "," + ymin + "," + xmax + "," + ymax;
      let params = new URLSearchParams();
      params.append("bbox", bbox);
      params.append("f", "pjson");
      params.append("transparent", true);
      let url = (id) =>
        `http://117.149.227.112:6080/arcgis/rest/services/gps/dengzhiPolygonIDW/MapServer/jobs/${id}/export`;
      await axios({
        url: url(jobId),
        method: "post",
        headers: { "content-type": "application/x-www-form-urlencoded" },
        data: params,
      }).then((res) => {
        this.MapComponent.showIsohyetLayer(res.data);
        this.mapRainfallLoading = false;
      });
    },
    //雨量数据转换成等雨量面参数格式
    formatRainfallParam(list, paramName) {
      let geoJson = {
        features: [],
      };
      let featuresArr = [];
      list.forEach((item) => {
        // let drp = item.drp > 0 ? item.drp : 0;
        if (item[paramName] != "-") {
          let obj = {
            attributes: {
              x: item.longitude,
              y: item.latitude,
              value: item[paramName],
            },
            geometry: {
              x: item.longitude,
              y: item.latitude,
            },
          };
          featuresArr.push(obj);
        }
      });
      geoJson.features = featuresArr;
      return geoJson;
    },
    //清除测量图层
    clearMeasure() {
      Measure.meatureFeaturesClear();
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
$sidebarHeight: 650px;
.bigScreen {
  position: relative;
  height: 100%;
  width: 100%;
  // background: url("~assets/images/da-ping-bg.png") no-repeat center;
  background-size: 100% 100%;
  overflow: hidden;
  .is-big-screen {
    transform: translateX(30%) scaleX(50%);
  }
  .type-area {
    height: 32px;
    margin-top: 12px;
    @include flexbox;
    @include flexflow(row, nowrap);
    @include flexAC;
    > div {
      @include flex(1);
      @include flexbox;
      @include flexAC;
      @include flexJC(center);
      color: #fff;
      height: 100%;
      border: 1px solid #ffffff;
      cursor: pointer;
      &.active {
        border: 1px solid #56fefe;
        color: #56fefe;
      }
    }
  }
  .layer-tree {
    overflow-y: auto;
    overflow-x: hidden;
    // height: 620px;
    height: calc(100% - 84px);
    margin: 12px 0;
    position: relative;
    ::v-deep .el-tree {
      background: rgba(0, 0, 0, 0);
      color: #fff;
      .el-tree-node:focus > .el-tree-node__content {
        background: rgba(0, 0, 0, 0.5);
      }
      .el-checkbox__inner {
        background: rgba(0, 0, 0, 0.1);
        border: 1px solid #308dcc;
      }
      .el-tree-node__content {
        &:hover {
          background: rgba(0, 0, 0, 0.5);
        }
      }
      .el-tree-node {
        margin: 6px 0;
      }
    }
    .module-type {
      height: 100%;
      @include flexbox;
      @include flexflow(column, nowrap);
      .module-layers {
        @include flex(1);
        overflow-y: auto;
        > ul {
          height: 100%;
          li {
            margin-left: 12px;
            display: flex;
            align-items: center;
            font-size: 16px;
            color: #ffffff;
            letter-spacing: 0;
            line-height: 24px;
            font-weight: 500;
            height: 54px;
            .el-checkbox {
              margin-right: 16px;
              ::v-deep .el-checkbox__input {
                &.is-checked {
                  .el-checkbox__inner {
                    color: #1cfff5;
                    border: 1px solid #1cfff5;
                    &::after {
                      border: 1px solid #1cfff5;
                      border-left: 0;
                      border-top: 0;
                    }
                  }
                }
              }
            }
            .tree-icon {
              display: inline-block;
              height: 28px;
              width: 28px;
              margin-right: 16px;
            }

            .tree-count {
              margin-left: 3px;
              color: #1cfff5;
            }
          }
        }
      }
    }
  }
  .sidebar {
    height: 100%;
    padding: 0;
    .catalog-title {
      position: relative;
    }
    .catalog-title-primary {
      height: 35px;
      width: 100%;
      background: url(~assets/images/WaterDisastersImage/title-bg_lightless.png)
        no-repeat;
      background-size: 100% 100%;
      background-position-x: 18px;
      align-items: center;
      color: #fff;
      @include flexbox;
      .panel-item__title--light {
        height: 32px;
        width: 40px;
        background: url(~assets/images/WaterDisastersImage/title-bg_light.gif)
          no-repeat;
        background-size: 100% 100%;
      }
    }
  }
  .side-tab {
    height: 64px;
  }
  .side-content {
    height: calc(100% - 64px);
    // padding-top: 16px;
  }
}
.bigScreen_map {
  width: 100%;
  height: 100%;
  .map-operate {
    width: auto;
    height: 40px;
    line-height: 40px;
    position: absolute;
    left: 36px;
    bottom: 40px;
    color: #fff;
    z-index: 1900;
  }
}
.legend-name {
  > i {
    margin-left: 8px;
    vertical-align: middle;
    cursor: pointer;
    padding-top: 2px;
  }
}
::v-deep.myClass .el-input__suffix {
  margin-top: -4px;
  padding: 0;
}

::v-deep .collapse-panel {
  &.right {
    // width: 400px;
    .sidebar {
      width: 380px;
      .side-tab {
        width: 100%;
      }
    }
  }
}

.screen_map {
  width: 100%;
  height: 100%;
}
.icon-legend-zhengchangshuiku {
  background: url("~assets/images/legend/zhengchangshuiku.png") no-repeat;
  display: inline-block;
  width: 16px;
  height: 16px;
}
.icon-legend-stagnant-normal {
  background: url("~assets/images/legend/legend-stagnant-normal.png") no-repeat;
  display: inline-block;
  width: 16px;
  height: 16px;
}
.icon-legend-chaojinshuiku {
  background: url("~assets/images/legend/chaojinshuiku.png") no-repeat;
  display: inline-block;
  width: 16px;
  height: 16px;
}
.icon-legend-stagnant-warn {
  background: url("~assets/images/legend/legend-stagnant-warn.png") no-repeat;
  display: inline-block;
  width: 16px;
  height: 16px;
}
.icon-legend-jishuidian {
  background: url("~assets/images/legend/jishuidian.png") no-repeat;
  display: inline-block;
  width: 16px;
  height: 16px;
}
.icon-legend-wujishui {
  background: url("~assets/images/legend/wujishui.png") no-repeat;
  display: inline-block;
  width: 16px;
  height: 16px;
}

.minimizeBtn {
  position: absolute;
  top: 140px;
  left: 350px;
  transition: all 0.5s ease-in-out;
  padding: 4px 12px;
  font-size: 14px;
  color: #4898fb;
  border: 1px solid #4898fb;
  background: transparent;
  border-radius: 4px;
  cursor: pointer;
}
</style>
<style lang="scss" scoped>
::v-deep .el-select-dropdown.el-popper.page-select-popper {
  border: 0;
  background-color: rgba(0, 0, 0, 0.4);
  .el-select-dropdown__item.selected {
    color: #409eff;
  }
  .el-select-dropdown__item {
    color: #fff;
  }
  .el-select-dropdown__item.hover,
  .el-select-dropdown__item:hover {
    background-color: transparent;
  }
}
::v-deep .el-popper[x-placement^="bottom"].page-select-popper .popper__arrow {
  border-bottom-color: transparent;
}
::v-deep
  .el-popper[x-placement^="bottom"].page-select-popper
  .popper__arrow::after {
  border-bottom-color: rgba(0, 0, 0, 0.4);
}
</style>
<style lang="scss">
.layer-search {
  width: 167px;
  margin-left: auto;
  .el-input__inner {
    width: 167px;
    height: 32px;
    background: none;
    border: 1px solid #ffffff;
    border-radius: 4px;
  }
  .el-input__prefix {
    .el-input__icon {
      line-height: 32px;
    }
  }
}
.checked-style {
  color: #1cfff5;
  width: 160px;
  padding: 5px 10px;
  background-color: rgba(0, 0, 0, 0.2);
  .el-checkbox__input.is-focus .el-checkbox__inner,
  .el-checkbox__input.is-checked + .el-checkbox__label {
    color: #1cfff5;
  }
  .el-checkbox__input.is-checked .el-checkbox__inner,
  .el-checkbox__input.is-indeterminate .el-checkbox__inner {
    background-color: transparent;
    border-color: #1cfff5;
    color: #1cfff5;
  }
  .el-checkbox__inner::after {
    -webkit-box-sizing: content-box;
    box-sizing: content-box;
    content: "";
    border: 0.0625rem solid #1cfff5;
    border-left: 0;
    border-top: 0;
    height: 0.4375rem;
    left: 0.25rem;
    position: absolute;
    top: 0.0625rem;
    -webkit-transform: rotate(45deg) scaleY(0);
    transform: rotate(45deg) scaleY(0);
    width: 0.1875rem;
    -webkit-transition: -webkit-transform 0.15s ease-in 0.05s;
    transition: -webkit-transform 0.15s ease-in 0.05s;
    transition: transform 0.15s ease-in 0.05s;
    transition: transform 0.15s ease-in 0.05s,
      -webkit-transform 0.15s ease-in 0.05s;
    -webkit-transform-origin: center;
    transform-origin: center;
  }
}
.checked-sty {
  width: 160px;
  padding: 5px 10px;
  background-color: rgba(0, 0, 0, 0.2);
  .el-checkbox {
    color: #fff;
    background: rgba(0, 0, 0, 0);
  }
  .el-checkbox__inner:hover {
    border-color: #fff;
  }
}
.layer-tree {
  .custom-tree {
    .custom-tree-node {
      color: #fff;
      .custom-tree-node__content {
        display: flex;
        align-items: center;
        height: 36px;
        padding-right: 24px;
        .custom-tree-node__expand-icon {
          transition: all 0.3s ease-out;
          &.expanded {
            transform: rotate(90deg);
          }
          &.is-leaf {
            display: none;
          }
        }
        .custom-tree-node__label {
          margin-left: 12px;
          display: flex;
          align-items: center;
          font-size: 16px;
          color: #ffffff;
          letter-spacing: 0;
          line-height: 24px;
          font-weight: 500;
          .tree-icon {
            display: inline-block;
            height: 28px;
            width: 28px;
            margin-right: 16px;
          }
          .layer-icon {
            display: inline-block;
            height: 20px;
            width: 20px;
            @include flexbox;
            @include flexAC;
            i {
              display: inline-block;
            }
          }
          .tree-count {
            margin-left: 3px;
            color: #1cfff5;
          }
          &.disabled {
            color: #909090;
          }
        }
        .el-checkbox {
          .el-checkbox__inner {
            background-color: transparent;
          }
          &.is-checked {
            .el-checkbox__inner {
              border: 1px solid #1cfff5;
              &:after {
                border: 1px solid #1cfff5;
                border-left: 0;
                border-top: 0;
              }
            }
          }
        }
        &.is-top {
          // width: 371px;
          height: 48px;
          background: rgba(7, 157, 254, 0.1);
          border-radius: 2px;
          justify-content: space-between;
          .custom-tree-node__label {
            font-size: 18px;
            color: #ffffff;
            line-height: 24px;
            font-weight: 500;
          }
          .custom-tree-node__expand-icon {
            display: none;
          }
          .el-checkbox {
            order: 2;
          }
          &.has-child {
            .custom-tree-node__expand-icon {
              display: inline-block;
              order: 2;
            }
          }
        }
        &.has-child {
          .el-checkbox {
            display: none;
          }
        }
      }
    }
  }
}
.shanxiDialog {
  .common-item {
    height: calc(100vh - 320px);
    min-height: 480px;
  }
  .is-fullscreen {
    .common-item {
      height: calc(100vh - 80px);
    }
  }
}

.build_dialog {
  padding: 16px 30px;
  .build_line {
    width: 100%;
    @include flexbox();
    & > .build_item {
      width: 50%;
    }
  }
  .build_img {
    width: 300px;
    img {
      width: 100%;
    }
    margin-bottom: 20px;
  }
  .build_img_big {
    width: 500px;
    img {
      width: 100%;
    }
    margin-top: 10px;
  }
  .build_item {
    color: #1cfff5;
    padding: 10px 0;
    font-weight: bold;
    span {
      color: #ccc;
    }
  }
}
</style>
