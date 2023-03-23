<template>
  <div class="bigScreen" ref="bigScreen">
    <div class="bigScreen_map">
      <screen-map
        ref="map"
        class="screen_map"
        @showDialog="showDialog"
        @showProjectDialog="showProjectDialog"
        @showInScopeDialog="showInScopeDialog"
      ></screen-map>
    </div>

    <!-- 左侧边栏 -->
    <screen-silde-panel
      position="left"
      @change="leftSildeChange"
      :height="sideHeight"
    >
      <div class="sidebar" :style="{ width: '350px' }">
        <div class="catalog-title">
          <div class="catalog-title-primary">
            <div class="panel-item__title--light" />
            <span style="margin-left: 6px">图层目录</span>
            <el-input
              v-model="layerSearch"
              prefix-icon="el-icon-search"
              placeholder="输入关键词查询"
              class="layer-search myClass"
              clearable
              @input="handleLayerSearch"
            />
          </div>
        </div>
        <div class="layer-tree">
          <custom-tree
            v-if="treeLoading"
            icon-class="el-icon-arrow-right"
            ref="layerTree"
            @check-change="checkChange"
            :data="layerData"
            show-checkbox
            :default-expanded-keys="defaultExpandedNode"
            :default-checked-keys="defaultCheckedNode"
            node-key="label"
            :props="{
              children: 'children',
              label: 'label',
            }"
          >
            <span
              :class="[
                'custom-tree-node__label',
                node.disabled && node.isLeaf && 'disabled',
              ]"
              slot-scope="{ node, data }"
            >
              <div v-if="node.level == 1" class="tree-icon">
                <i :class="data.icon" />
              </div>
              {{ node.label }}
              <span v-if="data.hasOwnProperty('count')" class="tree-count"
                >({{ data.count }})</span
              >
            </span>
          </custom-tree>
        </div>
      </div>
    </screen-silde-panel>

    <!-- 右侧边栏 -->
    <screen-silde-panel
      :width="458"
      position="right"
      :height="sideHeight"
      @change="rightSildeChange"
    >
      <div class="sidebar" ref="rightPanel">
        <div class="side-tab">
          <primary-tabs
            v-model="tabActive"
            :list="tabList"
            ref="primaryTab"
            @input="changeView"
          />
        </div>
        <div class="side-content">
          <custom-router-view
            @typhoonDetail="handleShowTyphoonDetail"
            @closeTyphoonDetail="handleTyphoonDetailClose"
          />
          <!--   <router-view /> -->
        </div>
      </div>
    </screen-silde-panel>

    <!-- 水雨情弹窗 -->
    <primary-dialog
      :mainTitle="dialogTitle"
      :visible="waterRainDialog"
      @handleClose="handleWaterRainClose"
      @analysis="showAnalysis"
      width="1129px"
    >
      <div class="dialog-content">
        <rain-dialog-content
          v-if="waterRainDialog"
          :dialogType="dialogType"
          :stationData="stationData"
          ref="rainDialog"
        />
      </div>
    </primary-dialog>

    <!-- 水利工程弹框 -->
    <!-- <primary-dialog
      :mainTitle="dialogTitle"
      :visible="projectDetailDialogShow"
      v-if="projectDetailDialogShow"
      @handleClose="handleProjectDetailClose"
      @analysis="showAnalysis"
      width="76%"
      custom-class="project-dialog-wrap"
      ref="project-dialog-wrap"
      :leftSide="true"
    >
      <div class="dialog-content">
        <project-detail-dialog-content
          :projectDetailData="projectDetailData"
          ref="projectDetailDialog"
        />
      </div>
    </primary-dialog> -->

    <project-dialog
      v-if="projectDetailDialogShow"
      :visible="projectDetailDialogShow"
      :point-data="projectDetailData"
      @close="handleProjectDetailClose"
      :removeTabs="removeTabs"
      @analysis="showAnalysis"
      :analysisFlag="true"
    />

    <!-- 周边分析弹框 -->
    <primary-dialog
      :mainTitle="dialogTitle"
      :visible="inScopeDialogShow"
      @handleClose="handleInScopeClose"
      @minimize="minimize"
      :hasAnalysis="false"
      :minimizeStatus="true"
      width="60%"
    >
      <div class="dialog-content scope-content">
        <scope-dialog-content
          :scopeTreeData="scopeTreeData"
          :rangeShow="rangeShow"
          @showScopePoint="showScopePoint"
          @setscopeTreeData="setscopeTreeData"
          ref="scopeDialog"
        />
      </div>
      <div slot="footer">
        <el-button type="primary" @click="minimize">确 定</el-button>
        <el-button @click="handleInScopeClose">取 消</el-button>
      </div>
    </primary-dialog>

    <!-- 最小化弹窗 -->
    <el-button
      class="minimizeBtn"
      :style="{ ...minimizeStyle }"
      v-show="minimizeStatus"
      @click="maximize"
      >{{ dialogTitle }}</el-button
    >

    <playable-timeline-bar
      v-if="timelinebarShow"
      dark
      :timeline-list="timelineList"
      :before-play-next="displayRainFallForecast"
      @change="handleTimelineChange"
    />

    <!-- 图例 -->
    <div
      v-if="legends.length > 0"
      class="map-legend is-big-screen"
      :style="{ ...legendStyle }"
    >
      <primary-legend title="图例" ref="mapLegend">
        <ul>
          <li v-for="(subitem, subindex) in legends" :key="subindex">
            <div class="legend-left">
              {{ subitem.label }}
            </div>
            <div class="legend-right">
              <span
                v-for="(item, index) in subitem.legends"
                :key="index"
                class="rainLegend"
              >
                <i :class="item.icon" />{{ item.label }}</span
              >
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
import {
  PrimaryTabs,
  PrimaryDialog,
  PrimaryLegend,
  PrimaryMapTable,
  PlayableTimelineBar,
  CustomTree,
  ProjectDialog,
} from "@/components";
import RainDialogContent from "./components/dialog/StationDialog";
import ScopeDialogContent from "./components/dialog/ScopeDialog";
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import { rainApi } from "@/api/dataScreen/floodJudge/index";
import { ScreenSildePanel } from "@/components";
import ProjectDetailDialogContent from "./components/dialog/projectDetailDialog";
import Measure from "@/utils/measure";
import { mapActions } from "vuex";
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
    PrimaryMapTable,
    RainDialogContent,
    ProjectDetailDialogContent,
    ScopeDialogContent,
    ScreenSildePanel,
    CustomRouterView,
    PlayableTimelineBar,
    CustomTree,
    ProjectDialog,
  },
  watch: {},
  data() {
    return {
      timelinebarShow: false,
      rainfallType: "",
      //图层树
      layerData: [
        {
          id: 20,
          label: "水利工程",
          icon: "icon-tree-project",
          disabled: true,
          children: [
            {
              id: 21,
              label: "水库",
              url: "/projectSide?type=水库",
            },
            {
              id: 22,
              label: "山塘",
              url: "/projectSide?type=山塘",
            },
            {
              id: 23,
              label: "堤防",
              url: "/projectSide?type=堤防",
            },
            {
              id: 24,
              label: "海塘",
              url: "/projectSide?type=海塘",
            },
            {
              id: 25,
              label: "泵站",
              url: "/projectSide?type=泵站",
            },
            {
              id: 26,
              label: "水闸",
              url: "/projectSide?type=水闸",
            },
            {
              id: 27,
              label: "闸站",
              url: "/projectSide?type=闸站",
            },
            {
              id: 28,
              label: "灌区",
              url: "/projectSide?type=灌区",
            },
            {
              id: 29,
              label: "农饮水",
              url: "/projectSide?type=农饮水",
            },
            {
              id: 30,
              label: "水电站",
              url: "/projectSide?type=水电站",
            },
          ],
        },
        {
          id: 1,
          label: "水利监控",
          icon: "icon-tree-monitor",
          disabled: true,
          children: [
            {
              id: 4,
              label: "水情",
              disabled: true,
              children: [
                {
                  id: 9,
                  label: "水库水位",
                  url: "/stationSide?type=水库水位",
                },
                {
                  id: 10,
                  label: "河道水位",
                  url: "/stationSide?type=河道水位",
                },
                {
                  id: 11,
                  label: "堰闸水位",
                  url: "/stationSide?type=堰闸水位",
                },
                {
                  id: 12,
                  label: "潮位",
                  url: "/stationSide?type=潮位",
                },
              ],
            },
            {
              id: 2,
              label: "雨情",
              children: [],
              url: "/stationSide?type=雨情",
            },
            {
              id: 3,
              label: "工情",
              disabled: true,
              children: [
                {
                  id: 7,
                  // label: "水闸开度",
                  label: "水闸工情",
                  url: "/stationSide?type=水闸工情",
                },
              ],
            },
            {
              id: 13,
              label: "流量",
              disabled: true,
              url: "/stationSide?type=流量",
            },
            {
              id: 14,
              label: "水质",
              disabled: true,
              children: [
                {
                  id: 15,
                  label: "国控断面(开发中)",
                  disabled: true,
                  url: "/stationSide",
                },
                {
                  id: 16,
                  label: "省控断面(开发中)",
                  disabled: true,
                  url: "/stationSide",
                },
                {
                  id: 17,
                  label: "水文站水质",
                  url: "/stationSide?type=水文站水质",
                },
              ],
            },
            {
              id: 18,
              label: "积水",
              url: "/stationSide?type=积水",
            },
            {
              id: 19,
              label: "视频",
              url: "/stationSide?type=视频",
            },
          ],
        },
        {
          id: 31,
          label: "应急救援(开发中)",
          disabled: true,
          icon: "icon-tree-rescue",
          children: [
            {
              id: 31,
              label: "防汛仓库",
              disabled: true,
            },
            {
              id: 32,
              label: "救援队伍",
              disabled: true,
            },
          ],
        },
        {
          id: 33,
          label: "台风分布",
          icon: "icon-tree-typhoon",
          url: "/typhoonSearch",
        },
        {
          id: 34,
          label: "气象预报(开发中)",
          icon: "icon-tree-weather",
          disabled: true,
        },
        {
          id: 35,
          label: "降雨分布(开发中)",
          icon: "icon-tree-realtime",
          disabled: true,
        },
        {
          id: 36,
          label: "降雨预报(开发中)",
          icon: "icon-tree-forecast",
          disabled: true,
        },
        {
          id: 37,
          label: "水域调查(开发中)",
          icon: "icon-tree-survey",
          disabled: true,
        },
      ],
      defaultExpandedNode: [], //树默认展开结点
      defaultCheckedNode: [], //树默认选中结点
      tabList: [], //右侧tab列表
      tabActive: 0,
      waterRainDialog: false,
      dialogTitle: "",
      stationData: {}, //测站弹框-测站信息
      dialogType: "RR",
      checkedList: [],
      projectDetailDialogShow: false, //水利工程弹框开关flag
      projectDetailData: null,
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
    };
  },
  mounted() {
    const selectType = this.$route.query.selectType;
    this.changeDefault(selectType);
    this.getProjectCount();
    this.handleWindowResize();
    this.$store.commit("SetMapComponent", this.$refs.map);
    window.addEventListener("resize", this.handleWindowResize);
    this.$nextTick(() => {
      this.addLegend("水库");
    });
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  computed: {
    legends() {
      return this.$store.getters.displayLegends;
    },
    legendStyle() {
      return { right: this.rightCollapse == false ? "3.125rem" : "29.375rem" };
    },
    minimizeStyle() {
      return { left: this.leftCollapse == false ? "42px" : "350px" };
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
    ...mapActions(["addLegend", "removeLegend"]),
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
    /**
     * 获取工程数量统计
     */
    getProjectCount() {
      this.treeLoading = false;
      hydraulicApi.countProjectNumber().then((res) => {
        const countData = {};
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
              if (child.hasOwnProperty("children")) {
                scan(child);
              }
            }
          }
        };
        this.layerData.forEach((item) => {
          scan(item);
        });
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
      let routeMap = {
        台风分布: {},
        潮位: {
          layerName: "潮位",
          url: "/stationSide?type=潮位",
          parent: "水情",
        },
        水闸: {
          layerName: "水闸",
          url: "/projectSide?type=水闸",
          parent: "工情",
        },
        水库: {
          layerName: "水库",
          url: "/projectSide?type=水库",
          parent: "水利工程",
        },
        水闸工情: {
          layerName: "水闸工情",
          url: "/stationSide?type=水闸工情",
          parent: "工情",
        },
        水库水位: {
          layerName: "水库水位",
          url: "/stationSide?type=水库水位",
          parent: "水情",
        },
        河道水位: {
          layerName: "河道水位",
          url: "/stationSide?type=河道水位",
          parent: "水情",
        },
        海塘: {
          layerName: "海塘",
          url: "/projectSide?type=海塘",
          parent: "水利工程",
        },
        雨量: {
          layerName: "雨情",
          url: "/stationSide?type=雨情",
          parent: "水利监控",
        },
      };
      let routeObj = routeMap[selectType] || routeMap["水库"];
      // console.log(routeMap[selectType],1)
      this.addSideTab(routeObj.layerName, routeObj.url);
      switch (selectType) {
        case "台风分布":
          {
            this.defaultCheckedNode = [];
            this.defaultCheckedNode.push("台风分布");
          }
          break;
        case "潮位":
          {
            this.defaultCheckedNode = [];
            this.defaultExpandedNode = ["水情"];
            this.defaultCheckedNode.push("潮位");
            this.getWaterPoint(this.defaultCheckedNode[0]);
          }
          break;
        case "水闸":
          {
            this.defaultCheckedNode = [];
            this.defaultExpandedNode = ["水利工程"];
            this.defaultCheckedNode.push("水闸");
          }
          break;
        case "水闸工情":
          {
            this.defaultCheckedNode = [];
            this.defaultExpandedNode = ["工情"];
            // this.defaultCheckedNode.push("水闸开度");
            this.defaultCheckedNode.push("水闸工情");
            this.getWaterPoint(this.defaultCheckedNode[0]);
          }
          break;
        case "河道水位":
          {
            this.defaultCheckedNode = [];
            this.defaultExpandedNode = ["水情"];
            this.defaultCheckedNode.push("河道水位");
            this.getWaterPoint(this.defaultCheckedNode[0]);
          }
          break;
        case "海塘":
          {
            this.defaultCheckedNode = [];
            this.defaultExpandedNode = ["水利工程"];
            this.defaultCheckedNode.push("海塘");
            this.getWaterPoint(this.defaultCheckedNode[0]);
          }
          break;
        case "雨量":
          {
            this.defaultCheckedNode = [];
            this.defaultExpandedNode = ["水利监控"];
            this.defaultCheckedNode.push("雨情");
            this.getRainPoint(this.defaultCheckedNode[0]);
          }
          break;
        case "水库水位":
          {
            this.defaultCheckedNode = [];
            this.defaultExpandedNode = ["水情"];
            this.defaultCheckedNode.push("水库水位");
            this.getWaterPoint(this.defaultCheckedNode[0]);
          }
          break;
        default: {
          this.defaultExpandedNode = ["水利工程"];
          this.defaultCheckedNode = ["水库"];
        }
      }
    },
    handleWindowResize() {
      this.sideHeight = this.$refs.bigScreen.offsetHeight - 160;
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
    //图层数选中改变方法
    checkChange(data, status) {
      const checkedList = this.$refs.layerTree.getCheckedNodes();
      let layerName = data.name;
      // console.log(layerName, 123);
      if (checkedList.length == 0) {
        this.$refs.layerTree.setChecked(layerName, true);
      }
      if (status) {
        this.addLegend(layerName);
        switch (layerName) {
          case "水库水位":
          case "河道水位":
          case "堰闸水位":
          case "潮位":
          case "水闸工情":
            {
              this.getWaterPoint(layerName);
            }
            break;
          case "雨情":
            {
              this.getRainPoint(layerName);
            }
            break;
          case "国控断面":
          case "省控断面":
          case "水文站水质":
            {
              this.getWaterQualityPoint(layerName);
            }
            break;
          case "积水":
            {
              this.getLoggingPoint(layerName);
              this.$refs.map.showCityAreaLine();
              this.$refs.map.clearAreaLayer();
            }
            break;
          case "视频":
            {
              this.getCameraOpsPoint(layerName);
            }
            break;
          case "水库":
          case "山塘":
          case "堤防":
          case "海塘":
          case "泵站":
          case "水闸":
          case "闸站":
          case "灌区":
          case "农饮水":
          case "水电站":
            {
              // this.getProjectPoint(layerName);
            }
            break;
          case "降雨分布":
            {
              this.timelinebarShow = true;
              this.rainfallType = "past";
              this.$set(this.layerData[5], "disabled", true);
            }
            break;
          case "降雨预报":
            {
              this.timelinebarShow = true;
              this.rainfallType = "forecast";
              this.$set(this.layerData[4], "disabled", true);
            }
            break;
        }
        if (data.url) {
          this.addSideTab(layerName, data.url);
        }
      } else {
        //取消选中时
        this.removeLegend(layerName);
        this.removeSideTab(layerName);
        this.$refs.map.clearCityLayer();
        if (layerName == "海塘") {
          this.$refs.map.removeLineLayer();
        } else {
          this.$refs.map.clearLayerByName(layerName);
        }
        if (["降雨分布", "降雨预报"].includes(layerName)) {
          this.timelinebarShow = false;
          this.$refs.map.clearIsohyetLayer();
          this.$set(this.layerData[4], "disabled", false);
          this.$set(this.layerData[5], "disabled", false);
        }
      }
    },
    /**
     * 添加侧边栏
     * @description 此方法会跳转至增加的视图
     */
    addSideTab(layerName, url) {
      const idx = this.tabList.length;
      let obj = {
        value: idx,
        name: layerName,
        url: url,
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
    //获取水情点位
    async getWaterPoint(type) {
      let opt = {};
      switch (type) {
        case "水库水位":
          {
            opt.stationType = "RR";
            opt.resGrade = "";
          }
          break;
        case "河道水位":
          {
            opt.stationType = "ZZ";
          }
          break;
        case "堰闸水位":
        case "水闸":
        case "水闸工情":
          {
            opt.stationType = "DD";
            opt.isWrp = true;
          }
          break;
        case "潮位":
          {
            opt.stationType = "TT";
          }
          break;
      }
      let res = await hydraulicApi.waterStations(opt);
      let list = res.data;
      list.map((item) => {
        item.latitude = item.stLat;
        item.longitude = item.stLong;
        let src = "";
        switch (item.stType) {
          case "RR":
            {
              item.stationType = "水库";
              src = require("@/assets/images/legend/zhengchangshuiku.png");
            }
            break;
          case "ZZ":
            {
              item.stationType = "河道";
              src = require("@/assets/images/legend/legend-stagnant-normal.png");
            }
            break;
          case "DD":
            {
              item.stationType = "堰闸";
              src = require("@/assets/images/legend/zhamen1.png");
            }
            break;
          case "TT": {
            item.stationType = "潮位";
            src = require("@/assets/images/legend/choawei.png");
          }
        }
        item.src = src;
        return item;
      });
      this.$refs.map.showWaterPoint(list, type);
    },
    //获取雨情点位
    async getRainPoint(type) {
      let opt = {
        min: -10,
        max: 10000,
        minutes: 60,
      };
      let res = await hydraulicApi.rainStations(opt);
      let list = res.data;
      list.map((item) => {
        let src = "";
        if (item.drp <= 0) {
          src = require("@/assets/images/legend/rain_0.png");
        } else if (item.drp > 0 && item.drp < 10) {
          src = require("@/assets/images/legend/rain_10.png");
        } else if (item.drp >= 10 && item.drp < 25) {
          src = require("@/assets/images/legend/rain_10_25.png");
        } else if (item.drp >= 25 && item.drp < 50) {
          src = require("@/assets/images/legend/rain_25_50.png");
        } else if (item.drp >= 50 && item.drp < 100) {
          src = require("@/assets/images/legend/rain_50_100.png");
        } else if (item.drp >= 100 && item.drp < 250) {
          src = require("@/assets/images/legend/rain_100_250.png");
        } else {
          src = require("@/assets/images/legend/rain_250.png");
        }
        item.src = src;
        return item;
      });
      if (type) {
        this.$refs.map.showRainPoint(list, type);
      }
    },
    //获取水质点位
    async getWaterQualityPoint(type) {
      let opt = {
        name: "",
        adcd: "",
      };
      let res = await hydraulicApi.waterQualityStation(opt);
      let list = res.data;
      this.$refs.map.showWaterPoint(list, type);
    },
    //获取积水点位
    async getLoggingPoint(type) {
      let opt = {};
      let res = await hydraulicApi.getLogging(opt);
      let list = res.data;
      list.map((item) => {
        let src = "";
        item.ponding
          ? (src = require("@/assets/images/legend/jishuidian.png"))
          : (src = require("@/assets/images/legend/wujishui.png"));
        item.src = src;
        return item;
      });
      this.$refs.map.showWaterPoint(list, type);
    },
    //获取视频点位
    async getCameraOpsPoint(type) {
      let opt = {
        cameraName: "",
        pageNum: 0,
        pageSize: 0,
        adcd: "",
      };
      let res = await hydraulicApi.getCameraOps(opt);
      let list = res.data.list;
      list.map((item) => {
        if (item.status == 0) {
          item.status = "离线";
          item.src = require("@/assets/images/MapPoint/video-offline.png");
        } else {
          item.status = "在线";
          item.src = require("@/assets/images/MapPoint/video-online.png");
        }
      });
      this.$refs.map.showVideoPoint(list, type);
    },
    //获取工程点位
    async getProjectPoint(type) {
      let opt = {
        type,
      };
      let res = await hydraulicApi.getProjects(opt);
      let list = res.data;
      this.$refs.map.showProjectPoint(list, type);
    },
    //展示工程点位
    showProjectPoint(list, type) {
      this.$refs.map.showProjectPoint(list, type);
    },
    //关闭水雨情弹窗
    handleWaterRainClose() {
      this.waterRainDialog = false;
      this.stationData = {};
    },
    //最小化弹窗
    minimize() {
      debugger
      this.inScopeDialogShow = false;
      this.minimizeStatus = true;
      let list = this.$refs.scopeDialog.dataList;
      const selectNode = this.$refs.scopeDialog.selectNode;
      this.$refs.map.showAnalysisPoints(list, selectNode);
      if (this.centerPoint.length > 0) {
        this.$refs.map.flyPoint(this.centerPoint, 14);
      }
    },
    //还原弹窗
    maximize() {
      this.inScopeDialogShow = true;
      this.minimizeStatus = false;
    },
    showDialog(data) {
      this.stationData = data;
      this.dialogType = data.stType;
      this.dialogTitle =
        data.stName || data.stationName || data.cameraName || data.name;
      this.waterRainDialog = true;
      data.cameraName ? (this.dialogType = "camera") : this.dialogType;
      this.$nextTick(() => {
        switch (this.dialogType) {
          case "PP":
            this.$refs.rainDialog.getRainStationInfo();
            break;
          case "RR":
          case "DD":
          case "ZZ":
          case "TT":
            this.$refs.rainDialog.getWaterStationInfo();
            break;
          case "camera":
            this.$refs.rainDialog.getCameraStationInfo();
            break;
          case "FP":
            this.$refs.rainDialog.getLogStaitonInfo();
            break;
          default:
            this.$refs.rainDialog.getWaterStationInfo();
            break;
        }
      });
    },
    //工程弹框
    showProjectDialog(data, feature) {
      switch (feature.type) {
        case "闸站":
        case "灌区":
        case "农饮水":
        case "水电站":
          {
            this.removeTabs = ["运行管理"];
          }
          break;
        default: {
          this.removeTabs = [];
        }
      }
      this.dialogTitle = data.resName;
      this.projectDetailData = data;
      this.projectDetailData.name = data.resName;
      this.projectDetailData.code =
        data.resCode ||
        data.mnpdCode ||
        data.seawallCode ||
        data.dikeCode ||
        data.wagaCode ||
        data.hystCode ||
        data.pustCode ||
        data.irrCode ||
        data.cwsCode ||
        data.gateCode;
      this.projectDetailData.type = feature.type;
      //周边分析统一经纬度字段
      this.projectDetailData.latitude = feature.lat;
      this.projectDetailData.longitude = feature.lng;
      this.projectDetailDialogShow = true;

      this.removeTabs;
      // console.log(data);
    },
    handleProjectDetailClose() {
      this.projectDetailDialogShow = false;
      this.projectDetailData = {};
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
          this.$refs.scopeDialog.handleNodeClick(
            this.scopeTreeData[0].children[0]
          );
        });
      }
    },
    //测面点位
    showScopePoint(data) {
      data ? this.$refs.map.clearSearchData(data) : "";
      // this.$refs.map.cleanAllLayer()
      val.data.map((item) => {
        item.latitude = item.stLat;
        item.longitude = item.stLong;
        return item;
      });
      this.$refs.map.showProjectPoint(val.data, val.name || val.label);
    },
    //周边分析弹框
    showAnalysis() {
      this.dialogTitle = this.dialogTitle + " — 周边分析";
      this.inScopeDialogShow = true;
      this.rangeShow = true;
      this.waterRainDialog = false;
      this.projectDetailDialogShow = false;
      this.scopeTreeData = [];
      this.$nextTick(() => {
        // console.log(this.stationData.longitude,66);
        if (this.stationData.longitude) {
          const center = [
            this.stationData.longitude,
            this.stationData.latitude,
          ];
          this.$refs.scopeDialog.getAnalysisTreeData(this.stationData);
          this.$refs.map._drawCircleInMeter(center, 3000);
          this.centerPoint = center;
        } else {
          const center = [
            this.projectDetailData.longitude,
            this.projectDetailData.latitude,
          ];
          this.$refs.scopeDialog.getAnalysisTreeData(this.projectDetailData);
          this.$refs.map._drawCircleInMeter(center, 3000);
          this.centerPoint = center;
        }
      });
    },
    //周边分析数据
    setscopeTreeData(val) {
      // this.scopeTreeData = [{
      //   name:"全部",
      // }];
      this.scopeTreeData = [];
      val.map((item) => {
        this.scopeTreeData.push(item);
      });
      this.$nextTick(() => {
        if (this.scopeTreeData.length > 0) {
          this.$refs.scopeDialog.handleNodeClick(this.scopeTreeData[0]);
          for (let i = 0; i < this.scopeTreeData.length; i++) {
            if (this.scopeTreeData[i].children.length > 0) {
              this.$refs.scopeDialog.handleNodeClick(
                this.scopeTreeData[i].children[0]
              );
              break;
            }
          }
        }
      });
    },
    handleInScopeClose() {
      this.inScopeDialogShow = false;
      this.$refs.map.clearCirlceLayer();
      this.$refs.map.clearAnalysisPointsLayer();
      this.centerPoint = [];
      this.clearMeasure();
      this.$refs.map.resetScopeArr();
    },
    //展示台风
    showTyphoon(data) {
      this.$refs.map.showTyphoon(data);
    },
    clearTyphoonLayer() {
      this.$refs.map.clearTyphoonLayer();
      this.$refs.map.resetMap();
    },
    async displayRainFallForecast() {
      this.$refs.map.clearIsohyetLayer();
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
        this.$refs.map.showIsohyetLayer(res.data);
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
  background: url("~assets/images/da-ping-bg.png") no-repeat center;
  background-size: 100% 100%;
  overflow: hidden;
  .is-big-screen {
    transform: translateX(30%) scaleX(50%);
  }
  .layer-tree {
    overflow: auto;
    // height: 620px;
    height: calc(100% - 12px);
    padding-top: 16px;
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
  }
  .sidebar {
    height: 100%;
    padding: 0;
    .catalog-title {
      position: relative;
      left: -12px;
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
}
::v-deep.myClass .el-input__suffix {
  margin-top: -4px;
  padding: 0;
}

::v-deep .collapse-panel {
  top: 9%;
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
</style>
