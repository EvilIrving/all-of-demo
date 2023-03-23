<template>
  <div class="project-side" ref="stationSide">
    <!-- 统计分析 -->
    <div class="statistics-chart" ref="statisticsChart" v-show="showTj">
      <div class="chart-tabs">
        <!-- <span :class="[typeTab=='类别'?'active':'']" @click="typeTabChange('类别')">类别</span> -->
        <span :class="[typeTab == '条件选择' ? 'active' : '']" @click="typeTabChange('条件选择')">条件选择</span>
        <!-- <span
          :class="[typeTab == '行政区划' ? 'active' : '']"
          @click="typeTabChange('行政区划')"
          >区域统计</span
        >
        <span
          :class="[typeTab == '流域' ? 'active' : '']"
          @click="typeTabChange('流域')"
          >流域统计</span
        > -->
        <div class="title-right">
          <i @click="resetParam()" class="icon-btn-reset1"></i>
        </div>
      </div>
      <div class="chart-box" :style="chartStyle">
        <echart-capsule v-if="typeTab == '行政区划' || typeTab == '流域'" ref="chartCapsule" :series-data="statisticData" :extra-option="extraOption" @clickItem="getCountryData">
        </echart-capsule>
        <div class="type-chart" v-if="typeTab == '条件选择'">
          <div class="custom-filter" v-if="projectType == '水库'">
            <div class="filter-item">
              <span class="filter-label">工程规模：</span>
              <div>
                <el-radio-group v-model="radioVal" @change="getProjectList">
                  <el-row>
                    <el-col :span="8">
                      <el-radio :label="''">全部</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'1'">大型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'3'">中型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'4'">小(1)型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'5'">小(2)型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'其他'">其他</el-radio>
                    </el-col>
                  </el-row>
                </el-radio-group>
              </div>
            </div>
          </div>

          <div class="custom-filter" v-if="projectType == '山塘'">
            <div class="filter-item">
              <span class="filter-label">山塘类型：</span>
              <div>
                <el-radio-group v-model="radioVal" @change="getProjectList">
                  <el-row>
                    <el-col :span="8">
                      <el-radio :label="''">全部</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'高坝山塘'">高坝山塘</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'屋顶山塘'">屋顶山塘</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'普通山塘'">普通山塘</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'其他'">其他</el-radio>
                    </el-col>
                  </el-row>
                </el-radio-group>
              </div>
            </div>
          </div>

          <div class="custom-filter" v-if="projectType == '堤防'">
            <div class="filter-item">
              <span class="filter-label">堤防级别：</span>
              <div>
                <el-radio-group v-model="radioVal" @change="getProjectList">
                  <el-row>
                    <el-col :span="6">
                      <el-radio :label="''">全部</el-radio>
                    </el-col>
                    <el-col :span="6">
                      <el-radio :label="'1级'">1级</el-radio>
                    </el-col>
                    <el-col :span="6">
                      <el-radio :label="'2级'">2级</el-radio>
                    </el-col>
                    <el-col :span="6">
                      <el-radio :label="'3级'">3级</el-radio>
                    </el-col>
                    <el-col :span="6">
                      <el-radio :label="'4级'">4级</el-radio>
                    </el-col>
                    <el-col :span="6">
                      <el-radio :label="'5级'">5级</el-radio>
                    </el-col>
                    <el-col :span="6">
                      <el-radio :label="'其他'">其他</el-radio>
                    </el-col>
                  </el-row>
                </el-radio-group>
              </div>
            </div>
          </div>

          <div class="custom-filter" v-if="projectType == '海塘'">
            <div class="filter-item">
              <span class="filter-label">防潮标准：</span>
              <div>
                <el-radio-group v-model="radioVal" @change="getProjectList">
                  <el-row>
                    <el-col :span="8">
                      <el-radio :label="''">全部</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'0-10'">10年以下</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'10-10'">10年</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'20-20'">20年</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'50-50'">50年</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'100-100'">100年</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'100-9999'">100年以上</el-radio>
                    </el-col>
                  </el-row>
                </el-radio-group>
              </div>
            </div>
            <div class="filter-item">
              <span class="filter-label">海塘级别：</span>
              <div>
                <el-radio-group v-model="radioVal1" @change="getProjectList">
                  <el-row>
                    <el-col :span="6">
                      <el-radio :label="''">全部</el-radio>
                    </el-col>
                    <el-col :span="6">
                      <el-radio :label="'1级'">1级</el-radio>
                    </el-col>
                    <el-col :span="6">
                      <el-radio :label="'2级'">2级</el-radio>
                    </el-col>
                    <el-col :span="6">
                      <el-radio :label="'3级'">3级</el-radio>
                    </el-col>
                    <el-col :span="6">
                      <el-radio :label="'4级'">4级</el-radio>
                    </el-col>
                    <el-col :span="6">
                      <el-radio :label="'5级'">5级</el-radio>
                    </el-col>
                    <el-col :span="6">
                      <el-radio :label="'其他'">其他</el-radio>
                    </el-col>
                  </el-row>
                </el-radio-group>
              </div>
            </div>
          </div>

          <div class="custom-filter" v-if="projectType == '水闸'">
            <div class="filter-item">
              <span class="filter-label">工程规模：</span>
              <div>
                <el-radio-group v-model="radioVal" @change="getProjectList">
                  <el-row>
                    <el-col :span="8">
                      <el-radio :label="''">全部</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'1'">大型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'3'">中型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'4'">小(1)型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'5'">小(2)型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'其他'">其他</el-radio>
                    </el-col>
                  </el-row>
                </el-radio-group>
              </div>
            </div>
          </div>

          <div class="custom-filter" v-if="projectType == '泵站'">
            <div class="filter-item">
              <span class="filter-label">工程规模：</span>
              <div>
                <el-radio-group v-model="radioVal" @change="getProjectList">
                  <el-row>
                    <el-col :span="8">
                      <el-radio :label="''">全部</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'1'">大型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'3'">中型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'4'">小(1)型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'5'">小(2)型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'其他'">其他</el-radio>
                    </el-col>
                  </el-row>
                </el-radio-group>
              </div>
            </div>
          </div>

          <div class="custom-filter" v-if="projectType == '闸站'">
            <div class="filter-item">
              <span class="filter-label">工程规模：</span>
              <div>
                <el-radio-group v-model="radioVal" @change="getProjectList">
                  <el-row>
                    <el-col :span="8">
                      <el-radio :label="''">全部</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'1'">大型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'3'">中型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'4'">小(1)型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'5'">小(2)型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'其他'">其他</el-radio>
                    </el-col>
                  </el-row>
                </el-radio-group>
              </div>
            </div>
          </div>

          <div class="custom-filter" v-if="projectType == '农饮水'">
            <div class="filter-item">
              <span class="filter-label">类型：</span>
              <div>
                <el-radio-group v-model="radioVal" @change="getProjectList">
                  <el-row>
                    <el-col :span="8">
                      <el-radio :label="''">全部</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'城'">城市</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'乡'">乡镇</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'联'">联村</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'单'">单村</el-radio>
                    </el-col>
                  </el-row>
                </el-radio-group>
              </div>
            </div>
          </div>

          <div class="custom-filter" v-if="projectType == '水电站'">
            <div class="filter-item">
              <span class="filter-label">工程规模：</span>
              <div>
                <el-radio-group v-model="radioVal" @change="getProjectList">
                  <el-row>
                    <el-col :span="8">
                      <el-radio :label="''">全部</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'1'">大型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'3'">中型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'4'">小(1)型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'5'">小(2)型</el-radio>
                    </el-col>
                    <el-col :span="8">
                      <el-radio :label="'其他'">其他</el-radio>
                    </el-col>
                  </el-row>
                </el-radio-group>
              </div>
            </div>
          </div>
          <!-- <div class="custom-filter" style="margin-top: 10px">
            <div class="filter-item filter-item-nolabel" gutter="10">
              <primary-select @selectHandle="selectHandle"></primary-select>
            </div>
          </div> -->
        </div>
      </div>
      <i class="split-line icon-split-line1"></i>
    </div>

    <!-- 统计列表 -->
    <div class="statistics-list" ref="statisticsList" :style="listStyle">
      <div class="catalog-title" ref="catalogTitle">
        <div class="type-area">
          <div class="tab-area">
            <div :class="tabType == '总数' ? 'tab-active' : ''" @click="changeLayerType('总数')" class="tabList">
              <div>
                总数<span>{{ listTitleTotal }}</span>
              </div>
            </div>
            <div :class="tabType == '病险' ? 'tab-active' : ''" @click="changeLayerType('病险')" v-if="
                projectType == '水库' ||
                projectType == '海塘' ||
                projectType == '水闸' ||
                projectType == '山塘'
              " class="tabList">
              <div>
                病险<span>{{ listTitleDang }}</span>
              </div>
            </div>
          </div>
          <div class="search-area" ref="searchArea">
            <div class="left-area">
              <el-input @input="getProjectList" v-model="searchVal" placeholder="请输入关键字查询">
                <template slot="prepend">
                  <el-button slot="append" icon="el-icon-search" class="keyword-search"></el-button>
                </template>
              </el-input>
            </div>
            <div class="right-area" @click="lookDetail" v-if="projectType != '农饮水'">
              <i class="icon-liebiao-detail"></i>
            </div>
          </div>
        </div>
      </div>

      <div class="table-list" :style="tableListStyle">
        <screen-table :columnList="tableColumns" :dataList="dataList" :loading="tbLoading" @rowClick="rowClick" :tableHeight="tableHeight">
        </screen-table>
        <!-- <primary-map-table 
          :columnList="tableColumns"
          :dataList="dataList"
          @rowClick="rowClick"
          :paginationFlag="true"
        ></primary-map-table> -->
      </div>
    </div>

    <!-- 详情弹框 -->
    <primary-dialog mainTitle="详表" :visible="detailDialog" @handleClose="handleDetailClose" width="50%" :hasAnalysis="false">
      <div class="dialog-content scope-content">
        <detail-table v-if="detailDialog" :projType="projectType"></detail-table>
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import {
  ScreenTable,
  PrimaryMapTable,
  PrimaryDialog,
  PrimarySelect,
} from "@/components/";
import EchartCapsule from "../charts/echart_capsule/EchartCapsule";
import DetailTable from "../dialog/DetailTable";
export default {
  name: "project-side",
  data() {
    return {
      statisticData: [], //统计图数据
      extraOption: {},
      projectType: "水库", //工程类型
      searchVal: "",
      tableHeight: 0,
      tableColumns: [],
      dataList: [],
      //病险数据
      dataListDang: [],
      //总数数据
      dataListTotal: [],
      tableListStyle: {}, //表格样式
      listStyle: {}, //表格父容器样式
      typeTab: "条件选择",
      basinName: "", //选中流域字段
      areaCode: "", //选中行政区划
      detailDialog: false,
      //条件选择
      radioVal: "",
      radioVal1: "",
      tabType: "总数",
      flag: false,
      areaCodeVal: "",
      areaCodeList: [],
      conditionAreaCode: "",
      conditionAreaName: "",
      conditionBasin: "",
      tbLoading: false,
      showTj: true
    };
  },
  computed: {
    chartStyle() {
      let obj = {
        height: "280px",
      };
      if (this.typeTab == "行政区划") {
        obj = {
          height: "280px",
        };
      } else if (this.typeTab == "流域") {
        obj = {
          height: "180px",
        };
      } else if (this.typeTab == "条件选择") {
        obj =
          this.projectType == "海塘"
            ? {
              height: "170px",
            }
            : {
              height: "90px",
            };
      }
      return obj;
    },
    listTitleTotal() {
      let text = this.projectTotal;
      // if (this.areaCode) {
      //   text = text + "(" + this.areaCode + ")";
      // } else {
      //   text = this.projectTotal;
      // }
      return text;
    },
    //病险数量
    listTitleDang() {
      let text = this.projectDang;
      // if (this.areaCode) {
      //   text = text + "(" + this.areaCode + ")";
      // } else {
      //   text = this.projectDang;
      // }
      return text;
    },
    mapObj() {
      return this.$store.state.hydraulicAmap.mapComponent;
    },
    projectTotal() {
      return this.dataListTotal.length;
    },
    //病险数量
    projectDang() {
      return this.dataListDang.length;
    },
  },
  components: {
    ScreenTable,
    PrimaryMapTable,
    EchartCapsule,
    PrimaryDialog,
    DetailTable,
    PrimarySelect,
  },
  watch: {
    $route: {
      handler: function (val, oldVal) {
        this.calcDomHeight();
        this.calcTableHeight();
        this.projectType = val.query.type ? val.query.type : "";
        this.getProjectList();
        switch (this.projectType) {
          case '码头':
          case '桥梁':
          case '海塘道路交叉出入口':
          case '码头':
          case '旱闸':
          case '桩号':
          case '箱涵':
          case '管理房':
          case '测站平台':
          case '规模以下水闸':
          case '避灾点':
            this.showTj = false
            this.typeTab = "无";
            break;
          default:
            this.projectCount("条件选择", this.projectType);
            this.typeTab = "条件选择";
            this.showTj = true
            break;
        }
        this.$nextTick(() => {
          this.resetParam();
        });
      },
      // 深度观察监听
      deep: true,
    },
  },
  mounted() {
    this.calcDomHeight();
    this.calcTableHeight();
    this.projectType = this.$route.query.type || "";
    this.projectCount("条件选择", this.projectType);
    if (this.projectType !== '码头' && this.projectType !== '桥梁' && this.projectType !== '海塘道路交叉出入口' && this.projectType !== '旱闸' && this.projectType !== '桩号' && this.projectType !== '箱涵' && this.projectType !== '管理房' && this.projectType !== '测站平台' && this.projectType !== '规模以下水闸' && this.projectType !== '断面' && this.projectType !== '避灾点') {
      this.projectCount("条件选择", this.projectType);
      this.showTj = true
    } else {
      this.showTj = false
    }
    window.addEventListener("resize", this.handleWindowResize);
    this.$nextTick(() => {
      this.resetParam();
    });
    this.changeLayerType("总数");
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  methods: {
    selectHandle(value, type, label) {
      this.mapObj.clearAreaLayer();
      this.mapObj.clearBasinLayer();
      if (type == "area") {
        this.conditionBasin = "";
        this.conditionAreaCode = value;
        this.conditionAreaName = label;
        if (label) this.mapObj.showAreaLine(label);
      } else {
        this.conditionAreaCode = "";
        this.conditionAreaName = "";
        this.conditionBasin = value == "全部" ? "" : value;
        if (this.conditionBasin)
          this.mapObj.basinCommand(this.conditionBasin + "流域");
      }
      this.getProjectList();
    },
    async changeLayerType(type) {
      this.tabType = type;
      this.flag = this.tabType == "病险" ? true : false;
      if (this.flag) {
        this.dataList = this.dataListDang;
      } else {
        this.dataList = this.dataListTotal;
      }
      // if (type == "海塘") {
      //   this.mapObj.handleSeawallChange(this.dataList);
      // } else {
      //    this.mapObj.showProjectPoint(this.dataList, this.projectType);
      // }
    },
    //获取病险列表
    async getProDangList(opt) {
      Object.assign(opt, { dilapProj: "true" });
      this.tbLoading = true;
      let res = await hydraulicApi.getProjects(opt);
      res.data.map((item) => {
        item.resName =
          item.resName ||
          item.mnpdName ||
          item.dikeName ||
          item.seawallName ||
          item.pustName ||
          item.wagaName ||
          item.gateName ||
          item.irrName ||
          item.cwsName ||
          item.hystName;
        item.totalVol = item.totalVol || "-";
      });
      if (res.code == 0) {
        this.dataListDang = res.data;
        this.tbLoading = false;
      } else {
        this.dataListDang = [];
      }
      if (this.flag) {
        this.dataList = this.dataListDang;
      }
    },
    //计算表格父容器高度
    calcDomHeight() {
      this.$nextTick(() => {
        const height =
          this.$refs.stationSide.offsetHeight -
          this.$refs.statisticsChart.offsetHeight +
          "px";
        this.listStyle = {
          height,
        };
      });
    },
    //计算表格高度
    calcTableHeight() {
      this.$nextTick(() => {
        setTimeout(() => {
          this.tableHeight =
            this.$refs.statisticsList.offsetHeight -
            this.$refs.catalogTitle.offsetHeight -
            this.$refs.searchArea.offsetHeight +
            36;
          this.tableListStyle = {
            height: this.tableHeight + "px",
          };
        }, 100);
      });
    },
    handleWindowResize() {
      this.calcDomHeight();
      this.calcTableHeight();
    },
    rowClick(data) {
      let lng =
        data.longitude ||
        data.lng ||
        data.lowLeftLong ||
        data.dtMidLong ||
        data.startLong ||
        data.pustLong ||
        data.startLong ||
        data.hystLong;
      let lat =
        data.latitude ||
        data.lat ||
        data.lowLeftLat ||
        data.dtMidLat ||
        data.startLat ||
        data.pustLat ||
        data.startLat ||
        data.hystLat;
      if (lng && lat) this.mapObj.setView(lng, lat);

      if (this.projectType == '码头' || this.projectType == '桥梁' || this.projectType == '海塘道路交叉出入口' || this.projectType == '旱闸' && this.projectType !== '箱涵' && this.projectType !== '管理房' && this.projectType !== '测站平台' && this.projectType !== '规模以下水闸' && this.projectType !== '断面') {
        this.$parent.$parent.$parent.$refs.map.$emit("showJcBuildingDialog", data);
      }

      if (this.projectType == '桩号') {
        this.$parent.$parent.$parent.$refs.map.$emit("showZhuanghaoDialog", data);
      }


      // 有问题
      // this.$parent.showDialog(data);
    },
    //重置筛选条件
    resetParam() {
      this.basinName = "";
      this.areaCode = "";
      this.searchVal = "";
      this.areaCodeVal = "";
      this.conditionAreaCode = "";
      this.conditionAreaName = "";
      this.conditionBasin = "";
      (this.radioVal = ""), (this.radioVal1 = ""), this.mapObj.resetMap();
      this.mapObj.clearAreaLayer();
      this.mapObj.clearBasinLayer();
      this.getProjectList();
    },
    //统计分析tab改变
    typeTabChange(type) {
      console.log(type,'type');
      this.resetParam();
      this.typeTab = type;
      this.projectCount(type, this.projectType);
      this.handleWindowResize();
    },
    async getProjectList() {
      let opt = {
        type: this.$route.query.type || "水库",
        projectName: this.searchVal,
        areaCode:
          this.typeTab == "条件选择"
            ? this.conditionAreaCode
            : this.areaCodeVal,
        bas: this.typeTab == "条件选择" ? this.conditionBasin : this.basinName,
      };
      switch (this.projectType) {
        case '断面':
          opt.name = this.searchVal;
          break;
        case "避灾点":
          opt.pointName = this.searchName;
          break;
        case "水库":
        case "水闸":
          opt.pointName = this.searchName;
          opt.withWaterLevel = true;
          opt.levels = this.radioVal;
          break;
        case "泵站":
        case "闸站":
        case "水电站":
          opt.levels = this.radioVal;
          break;
        case "山塘":
          opt.mnpdCate = this.radioVal;
          break;
        case "堤防":
          opt.grade = this.radioVal;
          break;
        case "农饮水":
          opt.engType = this.radioVal;
          break;
        case "海塘":
          opt.grade = this.radioVal1;
          opt.designTideStandardStart = this.radioVal.split("-")[0];
          opt.designTideStandardEnd = this.radioVal.split("-")[1];
          break;
        default:

          break;
      }

      if (this.projectType == '桩号') {
        this.tableColumns = [
          {
            prop: "stake",
            label: "桩号",
          },
        ];
      } else if (this.projectType == '断面') {
        this.tableColumns = [
          {
            prop: "name",
            label: "断面名称",
          },
        ];
      } else {
        this.tableColumns = [
          {
            prop: "resName",
            label: "名称",
          },
        ];
      }

      let arr = [
        {
          prop: "engScal",
          label: "工程规模",
          sortable: true,
        },
        {
          prop: "totCap",
          label: "总库容(万m³)",
          sortable: true,
        },
      ];
      switch (this.projectType) {
        case "水库":
          {
            arr = [
              {
                prop: "engScal",
                label: "工程规模",
                sortable: true,
              },
              {
                prop: "totCap",
                label: "总库容(万m³)",
                sortable: true,
              },
            ];
          }
          break;
        case "山塘":
          {
            arr = [
              {
                prop: "mnpdCate",
                label: "山塘类型",
                sortable: true,
              },
              {
                prop: "jhhsRj",
                label: "总容积(万m³)",
                sortable: true,
              },
            ];
          }
          break;
        case "堤防":
          {
            arr = [
              {
                prop: "dikeType",
                label: "堤防类别",
              },
              {
                prop: "dikeLen",
                label: "堤防长度(m)",
                sortable: true,
              },
            ];
          }
          break;
        case "海塘":
          {
            arr = [
              {
                prop: "seawallLevel",
                label: "海塘级别",
                sortable: true,
              },
              {
                prop: "seawallLength",
                label: "海塘长度(m)",
                sortable: true,
              },
            ];
          }
          break;
        case "泵站":
          {
            arr = [
              {
                prop: "engScal",
                label: "泵站规模",
                sortable: true,
              },
              {
                prop: "insFlow",
                label: "装机流量(m³/s)",
                sortable: true,
              },
            ];
          }
          break;
        case "水闸":
          {
            arr = [
              {
                prop: "engScal",
                label: "水闸规模",
                sortable: true,
              },
              {
                prop: "desLockDisc",
                label: "最大过闸流量(m³/s)",
                sortable: true,
              },
            ];
          }
          break;
        case "闸站":
          {
            arr = [
              {
                prop: "engScal",
                label: "闸站规模",
                sortable: true,
              },
              {
                prop: "maxFlow",
                label: "过闸流量(m³/s)",
                sortable: true,
              },
            ];
          }
          break;
        case "灌区":
          {
            arr = [
              {
                prop: "effIrrArea",
                label: "有效灌溉面积",
                sortable: true,
              },
            ];
          }
          break;
        case "农饮水":
          {
            arr = [];
          }
          break;
        case "水电站":
          {
            arr = [
              {
                prop: "engScal",
                label: "水电站规模",
                sortable: true,
              },
              {
                prop: "totInsCap",
                label: "总装机容量(kW)",
                sortable: true,
              },
            ];
          }
          break;
        case "码头":
        case "桥梁":
        case "海塘道路交叉出入口":
        case "旱闸":
        case "箱涵":
        case "测站平台":
        case "规模以下水闸":
        case "管理房":
          {
            arr = [
              {
                prop: "prnm",
                label: "主体工程",
              },
              {
                prop: "proType",
                label: "类型",
              },
              {
                prop: "proArea",
                label: "面积",
                sortable: true,
              },
            ];
          }
          break;
        case "桩号":
          {
            arr = [
              {
                prop: "sectionType",
                label: "断面类型",
              },
              {
                prop: "bidSection",
                label: "所属标段",
              },
              {
                prop: "prnm",
                label: "主体工程",
              }
            ];
          }
          break;
        case "断面":
          {
            arr = [
              {
                prop: "fetchLength",
                label: "风区长度",
              },
              {
                prop: "windDirection",
                label: "风向",
              },
              {
                prop: "crestElevatio",
                label: "堤顶高程",
              },
            ];
          }
          break;
        case "避灾点":
          {
            arr = [
              {
                prop: "pointName",
                label: "避灾点名称",
              },
              {
                prop: "pointCapacity",
                label: "可容纳人数",
              },
            ];
          }
          break;
      }
      arr.forEach((item) => {
        this.tableColumns.push(item);
      });
      this.tbLoading = true;
      let res = {}
      if (this.projectType == '断面') {
        res = await hydraulicApi.sectionList(opt);
        res.data.list.map((item) => {
          item.name = item.name;
          item.fetchLength = item.fetchLength || '-';
          item.windDirection = item.windDirection || '-';
          item.crestElevatio = item.crestElevatio || '-';
        });
      } else {
        res = await hydraulicApi.getProjects(opt);
        res.data.map((item) => {
          item.resName =
            item.resName ||
            item.mnpdName ||
            item.dikeName ||
            item.seawallName ||
            item.pustName ||
            item.wagaName ||
            item.gateName ||
            item.irrName ||
            item.cwsName ||
            item.hystName ||
            item.proName;
          item.totalVol = item.totalVol || "-";
        });
      }

      if (res.code == 0) {
        this.dataListTotal = this.projectType === '断面' ? res.data.list : res.data;
        this.tbLoading = false;
      } else {
        this.dataListTotal = [];
      }
      // this.getProDangList(opt);
      if (!this.flag) {
        this.dataList = this.dataListTotal;
      }
      // if (!unloadPoint) {
      // if (type == "海塘") {
      //   this.mapObj.handleSeawallChange(this.dataListTotal);
      // } else {
      //   this.mapObj.showProjectPoint(this.dataListTotal, this.projectType);
      // }
      // }
      // this.changeLayerType("病险");
      // this.changeLayerType("总数");
    },
    async getCountryData(data, checkName) {
      if (this.typeTab == "行政区划") {
        if (checkName) {
          this.areaCode = data.name;
          this.mapObj.showAreaLine(this.areaCode);
          this.areaCodeList.some((item) => {
            if (data.name == item.areaName) {
              this.areaCodeVal = item.areaCode;
              return true;
            }
          });
        } else {
          this.areaCode = "";
          this.areaCodeVal = "";
          this.conditionAreaCode = "";
          this.conditionAreaName = "";
          this.mapObj.clearAreaLayer();
        }
      } else if (this.typeTab == "流域") {
        if (checkName) {
          this.basinName = data.name;
          const bsName = this.basinName + "流域";
          this.mapObj.basinCommand(bsName);
        } else {
          this.basinName = "";
          this.conditionBasin = "";
          this.mapObj.clearBasinLayer();
        }
      }
      this.getProjectList();
    },
    //工程分类 统计
    async projectCount(type, stType) {
      let opt = {
        type,
        stType,
      };
      let res = await hydraulicApi.projectCount(opt);
      if (res.code == 0) {
        this.extraOption = {};
        this.statisticData = [];
        let xArr = [];
        this.areaCodeList = res.data;
        res.data.forEach((item) => {
          const x = item.areaName || item.key;
          xArr.push(x);
          const y = item.number || item.value;
          this.statisticData.unshift(y);
        });
        xArr = xArr.reverse();
        let obj = {
          yAxis: {
            data: xArr,
          },
        };
        this.extraOption = {
          ...obj,
        };

        this.$refs.chartCapsule.updateChartView();
        this.$nextTick(() => {
          this.calcTableHeight();
        });
      }
    },
    //详表弹窗关闭
    handleDetailClose() {
      this.detailDialog = false;
    },
    //查看详表
    lookDetail() {
      this.detailDialog = true;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
@import "./customFilter.scss";

.project-side {
  width: 100%;
  height: 100%;
  @include flexbox;
  @include flexflow(column, nowrap);

  .statistics-chart {
    position: relative;

    // flex: 1;
    .title-right {
      position: absolute;
      right: 0;

      .icon-btn-reset1 {
        cursor: pointer;
        height: 32px;
      }
    }

    .split-line {
      display: inline-block;
      width: 100%;
      height: 4px;
      margin-bottom: 16px;
    }

    .chart-tabs {
      @include flexbox;
      @include flexAC;

      span {
        box-sizing: border-box;
        display: inline-block;
        padding: 4px 8px;
        width: 80px;
        height: 32px;
        color: #ffffff;
        border: 1px solid rgba(255, 255, 255, 0.6);
        text-align: center;
        line-height: 20px;
        font-size: 14px;
        margin-right: 16px;
        cursor: pointer;

        &.active {
          color: #56fefe;
          background: rgba(72, 152, 251, 0.1);
          border: 1px solid #56fefe;
          box-shadow: inset 0 0 5px 0 #56fefe;
        }
      }
    }

    .chart-box {
      padding-top: 8px;

      ::v-deep .dv-capsule-chart {
        height: 100%;
      }

      .type-chart {
        padding: 16px 0;

        .type-chart-item {
          @include flexbox;
          @include flexflow(row, nowrap);
          color: #00c1ff;
          margin-bottom: 20px;

          .chart-item-title {
            width: 25px;
            letter-spacing: 10px;
            border-right: 1px solid #00c1ff;
          }

          .chart-item-content {
            @include flex(1);
            @include flexbox;
            @include flexflow(row, nowrap);
            padding-left: 18px;

            .param-item {
              width: 49%;
              @include flexbox;
              @include flexAC;
              @include flexflow(row, nowrap);
              cursor: pointer;

              .param-item-icon {
                width: 43px;
                height: 43px;
              }

              .param-item-data {
                margin-left: 10px;

                p {
                  line-height: 24px;

                  span {
                    font-weight: bold;
                    color: #1cfef4;
                    margin-right: 6px;
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  .statistics-list {
    @include flex(2);

    .type-area {
      width: 100%;
      margin-top: 12px;
      @include flexbox;
      @include flexflow(row, nowrap);
      @include flexAC;
      justify-content: space-between;

      .tab-area {
        @include flexbox;
        @include flexflow(row, nowrap);
      }

      .search-area {
        @include flex(1);
        margin-left: 10px;

        .left-area {
          @include flex(2);

          ::v-deep .el-input {
            background-color: rgba(0, 0, 0, 0);
            border: 1px solid rgba(255, 255, 255, 0.6);
            border-radius: 2px;

            .keyword-search {
              color: #ffffff;
            }

            .el-input__inner {
              background-color: rgba(0, 0, 0, 0);
            }
          }
        }
      }

      .right-area {
        margin-left: 10px;
        cursor: pointer;
        float: right;
      }

      .tabList {
        @include flex(1);
        @include flexbox;
        @include flexflow(row, nowrap);
        @include flexAC;
        border: 1px solid #ffffff;
        color: #fff;

        &.tab-active {
          background: rgba(72, 152, 251, 0.1);
          border: 1px solid #56fefe;
          box-shadow: inset 0 0 5px 0 #56fefe;
          color: #56fefe;
        }

        > div {
          @include flex(1);
          @include flexbox;
          @include flexAC;
          @include flexJC(center);
          flex-flow: column;
          height: 50px;
          text-align: center;
          line-height: 18px;
          font-size: 14px;
          cursor: pointer;
          padding: 0 10px;

          > span {
            font-size: 16px;
          }
        }
      }

      .right-area {
        cursor: pointer;
        margin-left: 12px;
      }
    }
  }
}
</style>
<style lang="scss" scoped>
</style>