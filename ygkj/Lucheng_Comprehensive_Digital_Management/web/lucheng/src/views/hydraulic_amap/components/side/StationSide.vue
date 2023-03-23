<template>
  <div class="station-side" ref="stationSide">
    <!-- 统计分析 -->
    <div class="statistics-chart" ref="statisticsChart">
      <div class="chart-tabs">
        <span
          v-if="stationType == '雨情'"
          :class="[typeTab == '条件选择' ? 'active' : '']"
          @click="typeTabChange('条件选择')"
          >条件选择
        </span>
        <span
          v-if="stationType == '水库水位'"
          :class="[typeTab == '类别' ? 'active' : '']"
          @click="typeTabChange('类别')"
          >按类型</span
        >
        <span
          :class="[typeTab == '行政区域' ? 'active' : '']"
          @click="typeTabChange('行政区域')"
          >按区域</span
        >
        <span
          v-if="stationType == '水库水位'"
          :class="[typeTab == '流域' ? 'active' : '']"
          @click="typeTabChange('流域')"
          >按流域</span
        >
        <div class="title-right">
          <i @click="resetParam()" class="icon-btn-reset"></i>
        </div>
      </div>
      <div class="chart-box" :style="chartStyle">
        <echart-capsule
          v-if="typeTab == '行政区域'"
          ref="chartCapsule"
          :series-data="statisticData"
          :extra-option="extraOption"
          @clickItem="getCountryData"
        >
        </echart-capsule>

        <div class="type-chart" v-if="typeTab == '流域'">
          <!-- 总数 -->
          <div class="type-chart-item">
            <div class="chart-item-title cursor" @click="showBas('瓯江流域')">
              瓯<br /><br />江
            </div>
            <div class="chart-item-content">
              <div
                :class="[
                  selectItem == '瓯江正常' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.ojNormalRemark, '瓯江正常')
                "
              >
                <div class="param-item-icon icon-type-two"></div>
                <div class="param-item-data">
                  <p>
                    <strong>{{ normalText }}</strong>
                  </p>
                  <p>
                    <span>{{ statisticObj.ojNormalNum }}</span
                    >个
                  </p>
                </div>
              </div>
              <div
                :class="[
                  selectItem == '瓯江超汛限' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.ojOverRemark, '瓯江超汛限')
                "
              >
                <div class="param-item-icon icon-type-three"></div>
                <div class="param-item-data">
                  <p>
                    <strong>{{ abnormalText }}</strong>
                  </p>
                  <p>
                    <span
                      :style="{
                        color:
                          statisticObj.ojOverNum > 0 ? '#FBE400' : '#54FEF4',
                      }"
                      >{{ statisticObj.ojOverNum }}</span
                    >个
                  </p>
                </div>
              </div>
            </div>
          </div>
          <!-- 飞云江 -->
          <div class="type-chart-item">
            <div class="chart-item-title cursor" @click="showBas('飞云江流域')">
              飞云江
            </div>
            <div class="chart-item-content">
              <div
                :class="[
                  selectItem == '飞云江正常' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(
                    statisticObj.fyjNormalRemark,
                    '飞云江正常'
                  )
                "
              >
                <div class="param-item-icon icon-type-two"></div>
                <div class="param-item-data">
                  <p>
                    <strong>{{ normalText }}</strong>
                  </p>
                  <p>
                    <span>{{ statisticObj.fyjNormalNum }}</span
                    >个
                  </p>
                </div>
              </div>
              <div
                :class="[
                  selectItem == '飞云江超汛限' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(
                    statisticObj.fyjOverRemark,
                    '飞云江超汛限'
                  )
                "
              >
                <div class="param-item-icon icon-type-three"></div>
                <div class="param-item-data">
                  <p>
                    <strong>{{ abnormalText }}</strong>
                  </p>
                  <p>
                    <span
                      :style="{
                        color:
                          statisticObj.fyjOverNum > 0 ? '#FBE400' : '#54FEF4',
                      }"
                      >{{ statisticObj.fyjOverNum }}</span
                    >个
                  </p>
                </div>
              </div>
            </div>
          </div>
          <!-- 鳌江 -->
          <div class="type-chart-item">
            <div class="chart-item-title cursor" @click="showBas('鳌江流域')">
              鳌<br /><br />江
            </div>
            <div class="chart-item-content">
              <div
                :class="[
                  selectItem == '鳌江正常' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.ajNormalRemark, '鳌江正常')
                "
              >
                <div class="param-item-icon icon-type-two"></div>
                <div class="param-item-data">
                  <p>
                    <strong>{{ normalText }}</strong>
                  </p>
                  <p>
                    <span>{{ statisticObj.ajNormalNum }}</span
                    >个
                  </p>
                </div>
              </div>
              <div
                :class="[
                  selectItem == '鳌江超汛限' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.ajOverRemark, '鳌江超汛限')
                "
              >
                <div class="param-item-icon icon-type-three"></div>
                <div class="param-item-data">
                  <p>
                    <strong>{{ abnormalText }}</strong>
                  </p>
                  <p>
                    <span
                      :style="{
                        color:
                          statisticObj.ajOverNum > 0 ? '#FBE400' : '#54FEF4',
                      }"
                      >{{ statisticObj.ajOverNum }}</span
                    >个
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="type-chart" v-if="typeTab == '类别'">
          <!-- 总数 -->
          <div class="type-chart-item">
            <div class="chart-item-title">总<br /><br />数</div>
            <div class="chart-item-content">
              <div
                :class="[
                  selectItem == '水位站总数' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  waterStationsBody(
                    statisticObj.stationRemark,
                    'RR',
                    '水位站总数'
                  )
                "
              >
                <div class="param-item-icon icon-type-two"></div>
                <div class="param-item-data">
                  <p><strong>水位站</strong></p>
                  <p>
                    <span>{{ statisticObj.stationNum }}</span
                    >个
                  </p>
                </div>
              </div>
              <div
                :class="[
                  selectItem == '水库总数' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.rsvrNumRemark, '水库总数')
                "
              >
                <div class="param-item-icon icon-type-one"></div>
                <div class="param-item-data">
                  <p><strong>水库</strong></p>
                  <p>
                    <span>{{ statisticObj.rsvrNumNum }}</span
                    >个
                  </p>
                </div>
              </div>
            </div>
          </div>
          <!-- 大中型 -->
          <div class="type-chart-item">
            <div class="chart-item-title">大中型</div>
            <div class="chart-item-content">
              <div
                :class="[
                  selectItem == '大中型正常' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(
                    statisticObj.lmNormalRemark,
                    '大中型正常'
                  )
                "
              >
                <div class="param-item-icon icon-type-four"></div>
                <div class="param-item-data">
                  <p><strong>正常</strong></p>
                  <p>
                    <span>{{ statisticObj.lmNormalNum }}</span
                    >个
                  </p>
                </div>
              </div>
              <div
                :class="[
                  selectItem == '大中型超汛限' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(
                    statisticObj.lmOverRemark,
                    '大中型超汛限'
                  )
                "
              >
                <div class="param-item-icon icon-type-three"></div>
                <div class="param-item-data">
                  <p><strong>超汛限数</strong></p>
                  <p>
                    <span
                      :style="{
                        color:
                          statisticObj.lmOverNum > 0 ? '#FBE400' : '#54FEF4',
                      }"
                      >{{ statisticObj.lmOverNum }}</span
                    >个
                  </p>
                </div>
              </div>
            </div>
          </div>
          <!-- 小型 -->
          <div class="type-chart-item">
            <div class="chart-item-title">小<br /><br />型</div>
            <div class="chart-item-content">
              <div
                :class="[
                  selectItem == '小型正常' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.smNormalRemark, '小型正常')
                "
              >
                <div class="param-item-icon icon-type-four"></div>
                <div class="param-item-data">
                  <p><strong>正常</strong></p>
                  <p>
                    <span>{{ statisticObj.smNormalNum }}</span
                    >个
                  </p>
                </div>
              </div>
              <div
                :class="[
                  selectItem == '小型超汛限' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.smOverRemark, '小型超汛限')
                "
              >
                <div class="param-item-icon icon-type-three"></div>
                <div class="param-item-data">
                  <p><strong>超汛限数</strong></p>
                  <p>
                    <span
                      :style="{
                        color:
                          statisticObj.smOverNum > 0 ? '#FBE400' : '#54FEF4',
                      }"
                      >{{ statisticObj.smOverNum }}</span
                    >个
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="type-chart" v-if="typeTab == '条件选择'">
          <div class="custom-filter">
            <el-radio-group v-model="radioVal" @change="getRainStations">
              <el-radio :label="60">前1小时</el-radio>
              <el-radio :label="180">前3小时</el-radio>
              <el-radio :label="360">前6小时</el-radio>
              <el-radio :label="720">前12小时</el-radio>
              <el-radio :label="1440">前24小时</el-radio>
              <el-radio :label="2880">前48小时</el-radio>
            </el-radio-group>
            <!-- <div class="sub-title">降雨历时</div>
            <el-row>
              <el-col :span="24" class="filter-time">
                <span style="color: #fff">从</span>
                <el-date-picker
                  v-model="customFilter.beginDate"
                  type="date"
                  placeholder="日期"
                  :clearable="false"
                />
                <el-time-select
                  v-model="customFilter.beginTime"
                  :picker-options="{
                    start: '00:00',
                    step: '01:00',
                    end: '23:00',
                  }"
                  value-format="HH[时]"
                  placeholder="时间"
                  :clearable="false"
                />
                <el-button class="screen-button">实时</el-button>
              </el-col>
              <el-col :span="24" class="filter-time">
                <span style="color: #fff">到</span>
                <el-date-picker
                  v-model="customFilter.endDate"
                  type="date"
                  placeholder="日期"
                  :clearable="false"
                />
                <el-time-select
                  v-model="customFilter.endTime"
                  :picker-options="{
                    start: '00:00',
                    step: '01:00',
                    end: '23:00',
                  }"
                  value-format="HH[时]"
                  placeholder="时间"
                  :clearable="false"
                />
                <el-button class="screen-button">确定</el-button>
              </el-col>
            </el-row> -->
            <div class="sub-title">雨量范围</div>
            <el-row>
              <el-col class="rainfall-range" :span="24">
                <el-select
                  v-model="rainfallRange"
                  placeholder="请选择"
                  @change="getRainStations"
                >
                  <el-option label="<10" :value="[0, 10]"></el-option>
                  <el-option label="10-25" :value="[10, 25]"></el-option>
                  <el-option label="25-50" :value="[25, 50]"></el-option>
                  <el-option label="50-100" :value="[50, 100]"></el-option>
                  <el-option label="100-250" :value="[100, 250]"></el-option>
                  <el-option label=" > 250" :value="[250, 999]"></el-option>
                  <el-option label="自定义" :value="'0'"></el-option>
                </el-select>
              </el-col>
              <el-col class="rainfall-range" :span="24">
                <el-input
                  placeholder="请输入"
                  v-model="rainfallRangeMin"
                  :disabled="rainfallRange != '0'"
                ></el-input>
                <span style="margin: 0 10px; line-height: 30px">-</span>
                <el-input
                  placeholder="请输入"
                  v-model="rainfallRangMax"
                  :disabled="rainfallRange != '0'"
                ></el-input>
                <el-button
                  class="btn-sty"
                  :disabled="rainfallRange != '0'"
                  size="small"
                  @click="getRainStations"
                  >确认</el-button
                >
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </div>
    <i class="split-line icon-split-line"></i>

    <!-- 统计列表 -->
    <div class="statistics-list" ref="statisticsList" :style="listStyle">
      <div class="search-area" ref="searchArea">
        <div class="left-area">
          <el-input
            @input="getStationListByType"
            v-model="searchVal"
            placeholder="请输入关键字查询"
          >
            <template #prepend>
              <el-button slot="append" icon="el-icon-search"></el-button>
            </template>
          </el-input>
        </div>
      </div>
      <div class="table-list" :style="tableListStyle">
        <screen-table
          :columnList="tableColumns"
          :dataList="dataList"
          @rowClick="rowClick"
          :tableHeight="tableHeight"
        ></screen-table>
        <!-- <primary-map-table 
          :columnList="tableColumns"
          :dataList="dataList"
          @rowClick="rowClick"
          :paginationFlag="true"
        ></primary-map-table> -->
      </div>
    </div>
  </div>
</template>

<script>
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import { ScreenTable, PrimaryMapTable } from "@/components/";
import EchartCapsule from "../charts/echart_capsule/EchartCapsule";
export default {
  name: "station-side",
  data() {
    return {
      //条件选择
      radioVal: 60,
      customFilter: {
        beginDate: "",
        endDate: "",
        beginTime: "",
        endTime: "",
      },
      rainfallRange: [0, 10],
      rainfallRangeMin: "",
      rainfallRangMax: "",
      statisticData: [], //统计图数据
      extraOption: {},
      searchVal: "",
      tableHeight: 0,
      tableColumns: [
        {
          prop: "stName",
          label: "名称",
        },
      ],
      dataList: [],
      tableListStyle: {}, //表格样式
      listStyle: {}, //表格父容器样式
      typeTab: "行政区域",
      stationType: "水库水位",
      stationObj: {
        水库水位: "RR",
        河道水位: "ZZ",
        水闸工情: "DD",
        堰闸水位: "DD",
        潮位: "TT",
        雨情:'PP'
      },
      basinName: "", //选中流域字段
      areaCode: "", //选中行政区划
      statisticObj: {},
      selectItem: "水位站总数",
      areaCodeObj: {}, //行政区划和编码对应对象 用在按区域查询水库
      normalText: "正常",
      abnormalText: "超汛限",
    };
  },
  components: {
    ScreenTable,
    // PrimaryMapTable,
    EchartCapsule,
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
        if (this.stationType == "水库水位") {
          obj = {
            height: "264px",
          };
        } else {
          obj = {};
        }
      } else {
        obj = {
          height: "264px",
        };
      }
      return obj;
    },
    listTitle() {
      let text = "统计列表";
      if (this.areaCode) {
        text = text + "(" + this.areaCode + ")";
      } else {
        text = "统计列表";
      }
      return text;
    },
    mapObj() {
      return this.$store.state.hydraulicAmap.mapComponent;
    },
  },
  watch: {
    $route: {
      handler: function (val, oldVal) {
        this.calcDomHeight();
        this.calcTableHeight();
        let type = val.query.type ? val.query.type : "";
        this.stationType = type;
        this.initSideStation(type);
      },
      // 深度观察监听
      deep: true,
    },
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  mounted() {
    this.calcDomHeight();
    this.calcTableHeight();

    this.stationType = this.$route.query.type;
    // console.log(this.stationType, 111);
    this.initSideStation(this.stationType);
    window.addEventListener("resize", this.handleWindowResize);
  },
  methods: {
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
            this.$refs.searchArea.offsetHeight -
            10;
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
    //重置筛选条件
    resetParam() {
      this.basinName = "";
      this.areaCode = "";
      this.mapObj.resetMap();
      this.mapObj.clearAreaLayer();
      this.mapObj.clearBasinLayer();
      // this.getProjectList();
    },
    rowClick(data) {
      // console.log(data);
      this.$parent.$emit("showDialog", data);
    },
    //获取水库工程
    async getProjectList(areaCode) {
      const opt = {
        type: "水库",
        stationName: this.searchVal,
        areaCode: areaCode,
      };
      this.tableColumns = [
        {
          prop: "resName",
          label: "名称",
        },
      ];
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
      arr.forEach((item) => {
        this.tableColumns.push(item);
      });
      let res = await hydraulicApi.getProjects(opt);
      res.data.map((item) => {
        item.totalVol = item.totalVol || "-";
      });
      this.dataList = res.data;
      // console.log( this.$parent.showProjectPoint)
      // if(!unloadPoint){
      //   this.mapObj.showProjectPoint(this.dataList,this.projectType);
      // }
    },
    //统计分析tab改变
    typeTabChange(type) {
      this.resetParam();
      this.typeTab = type;
      // console.log(this.stationType);
      let stType = this.stationObj[this.stationType];
      // console.log(stType);
      this.abnormalText = "超汛限";
      this.normalText = "正常";
      if (stType) {
        if (this.stationType == "水库水位") {
          if (type == "行政区域") {
            this.projStatStatistic("水库", "区域");
          } else if (type == "流域") {
            this.selectItem = "瓯江正常";
            this.projStatStatistic("水库", "流域");
          } else {
            this.selectItem = "水位站总数";
            this.projStatStatistic("水库", "类型");
          }
        } else {
          this.attStBaseCountByType(type, stType);
        }
      }

      if (this.stationType == "视频") {
        if (type == "行政区域") {
          this.statsCameraByAdcd();
        } else {
          this.abnormalText = "离线";
          this.normalText = "在线";
          this.statsCameraByBasin();
        }
      }
    },
    initSideStation(type) {
      switch (type) {
        case "水库水位":
          {
            this.stationType = type;
            this.getWaterStations(type);
            this.typeTab = "类别";
            this.projStatStatistic("水库", "类型");
          }
          break;
        case "河道水位":
        case "堰闸水位":
        case "潮位":
        case "水闸工情":
          {
            // this.getWaterStations(type);
            this.stationType = type;
            this.typeTab = "行政区域";
            this.getWaterStations(type);
            this.attStBaseCountByType("行政区域", this.stationObj[type]);
          }
          break;
        case "国控断面":
        case "省控断面":
        case "水文站水质":
          {
            this.waterQualityStatistic();
            this.getWaterQualityStations(type);
          }
          break;
        case "雨情":
          {
            this.getRainStations(type);
            this.attStBaseCountByType("行政区域", "PP");
          }
          break;
        case "积水":
          {
            this.floodPointStatistic();
            this.getWaterLoggingStations(type);
          }
          break;
        case "视频":
          {
            this.getCameraOpsStations(type);
            this.statsCameraByAdcd();
          }
          break;
      }
    },
    //根据类型获取测站列表
    getStationListByType() {
      switch (this.stationType) {
        case "水库水位":
          {
            this.getWaterStations(this.stationType);
          }
          break;
        case "河道水位":
        case "堰闸水位":
        case "潮位":
        case "水闸工情":
          {
            this.getWaterStations(this.stationType);
          }
          break;
        case "国控断面":
        case "省控断面":
        case "水文站水质":
          {
            this.getWaterQualityStations(this.stationType);
          }
          break;
        case "雨情":
          {
            this.getRainStations(this.stationType);
          }
          break;
        case "积水":
          {
            this.getWaterLoggingStations(this.stationType);
          }
          break;
        case "视频":
          {
            this.getCameraOpsStations(this.stationType);
          }
          break;
      }
    },
    //水库测站统计
    async projStatStatistic(type, dimension) {
      let opt = {
        type: type,
        dimension: dimension,
      };
      let res = await hydraulicApi.projStatStatistic(opt);
      if (dimension == "类型") {
        this.statisticObj = res.data;
        this.waterStationsBody(
          this.statisticObj.stationRemark,
          "RR",
          "水位站总数"
        );
      } else if (dimension == "流域") {
        this.statisticObj = res.data;
        this.rsvrNewestWaterLevel(this.statisticObj.ojNormalRemark, "瓯江正常");
      } else {
        this.statisticData = [];
        let xArr = [];
        let seriesArr = [];
        let overNumArr = []; //超汛限数目
        let normalNumArr = []; //正常数目
        res.data.forEach((item) => {
          const x = item.countKey || item.key;
          xArr.push(x);
          const y = item.countNum || item.value;
          this.statisticData.unshift(y);
          //暂存totalRemark  点击柱子查列表时要用到
          this.areaCodeObj[x] = item.value.totalRemark;
          overNumArr.push(item.value.overNum);
          normalNumArr.push(item.value.normalNum);
        });
        xArr = xArr.reverse();
        overNumArr = overNumArr.reverse();
        normalNumArr = normalNumArr.reverse();
        seriesArr = [
          {
            name: "超汛限",
            type: "bar",
            stack: "total",
            label: {
              show: false,
            },
            emphasis: {
              focus: "series",
            },
            data: overNumArr,
            itemStyle: {
              barBorderRadius: [10, 10, 10, 10],
              borderWidth: 2,
              borderColor: "#ccc",
            },
          },
          {
            name: "正常",
            type: "bar",
            stack: "total",
            label: {
              show: false,
            },
            emphasis: {
              focus: "series",
            },
            data: normalNumArr,
            itemStyle: {
              barBorderRadius: [10, 10, 10, 10],
              borderWidth: 2,
              borderColor: "#ccc",
            },
          },
        ];
        let obj = {
          yAxis: {
            data: xArr,
          },
          series: seriesArr,
          color: ["#F53526", "#37A2DA"],
        };
        this.extraOption = { ...obj };
        this.$refs.chartCapsule.updateChartView();
        this.rsvrNewestWaterLevel("", "");
        this.$nextTick(() => {
          this.calcTableHeight();
        });
      }
    },
    //监测站分类 统计
    async attStBaseCountByType(type, stType) {
      let opt = {
        type,
        stType,
      };
      if(stType == 'DD'){
        if(this.stationType == '堰闸水位'){
          opt.isWrp = false;
        }else{
          opt.isWrp = true;
        }
      }
      let res = await hydraulicApi.attStBaseCountByType(opt);
      if (res.code == 0) {
        this.statisticData = [];
        let xArr = [];
        res.data.forEach((item) => {
          const x = item.countKey || item.key;
          xArr.push(x);
          const y = item.countNum || item.value;
          this.statisticData.unshift(y);
        });
        xArr = xArr.reverse();
        let obj = {
          yAxis: {
            data: xArr,
          },
        };
        this.extraOption = { ...obj };

        this.$refs.chartCapsule.updateChartView();
        this.$nextTick(() => {
          this.calcTableHeight();
        });
      }
      // console.log(res);
    },
    //按类型水库列表
    async rsvrNewestWaterLevel(resCodes, activeType) {
      if (resCodes) {
        this.selectItem = activeType;
        this.tableColumns = [];
        const arr = [
          {
            prop: "resName",
            label: "名称",
          },
          {
            prop: "waterLevel",
            label: "水位(m)",
            sortable: true,
          },
          {
            prop: "meiFloodLimitWaterLevel",
            label: "汛限水位(m)",
          },
        ];
        arr.map((item) => {
          this.tableColumns.push(item);
        });
        if (resCodes == "") {
          this.dataList = [];
        } else {
          let opt = {
            resCodes: resCodes,
          };
          let res = await hydraulicApi.rsvrNewestWaterLevel(opt);
          this.dataList = res.data;
        }
      }
      // console.log(res.data);
    },
    //获取水位站列表
    async waterStationsBody(stCodes, stationType, activeType) {
      this.selectItem = activeType;
      this.tableColumns = [];
      const arr = [
        {
          prop: "stName",
          label: "名称",
        },
        {
          prop: "waterLevel",
          label: "水位(m)",
          sortable: true,
        },
        {
          prop: "limitLevel",
          label: "警戒水位(m)",
          sortable: true,
        },
      ];
      arr.map((item) => {
        this.tableColumns.push(item);
      });
      let opt = { stCodes, stationType };
      let res = await hydraulicApi.waterStationsBody(opt);
      this.dataList = res.data;
      // console.log(res);
    },
    //水情
    async getWaterStations(type) {
      // console.log(type,88)
      const opt = {
        stationType: "RR",
        resGrade: "",
        stationName: this.searchVal,
        basinCode: this.basinName,
        areaCode: this.areaCode,
      };
      this.tableColumns = [
        {
          prop: "stName",
          label: "名称",
        },
      ];
      let arr = [
        {
          prop: "waterLevel",
          label: "水位(m)",
          sortable: true,
        },
      ];

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
          {
            opt.stationType = "DD";
            opt.isWrp = false;
          }
          break;
        case "水闸工情":
          {
            opt.stationType = "DD";
            opt.isWrp = true;
            arr = [
              {
                prop: "upz",
                label: "闸上水位(m)",
                sortable: true,
              },
              {
                prop: "dwz",
                label: "闸下水位(m)",
                sortable: true,
              },
              {
                prop: "tdz",
                label: "闸门开度",
                sortable: true,
              },
              {
                prop: "flow",
                label: "过闸流量(m³/s))",
                sortable: true,
              },
            ];
          }
          break;
        case "潮位":
          {
            opt.stationType = "TT";
          }
          break;
      }
      arr.forEach((item) => {
        this.tableColumns.push(item);
      });
      let res = await hydraulicApi.waterStations(opt);
      this.dataList = res.data;
    },
    //按流域统计摄像头数量
    async statsCameraByBasin() {
      let res = await hydraulicApi.statsCameraByBasin();
      if (res.code == 0) {
        const data = res.data;
        this.statisticObj = {
          ojNormalNum:
            data.onlineNum[0].value >= 0 ? data.onlineNum[0].value : "-",
          ojOverNum:
            data.offlineNum[0].value >= 0 ? data.offlineNum[0].value : "-",
          fyjNormalNum:
            data.onlineNum[2].value >= 0 ? data.onlineNum[2].value : "-",
          fyjOverNum:
            data.offlineNum[2].value >= 0 ? data.offlineNum[2].value : "-",
          ajNormalNum:
            data.onlineNum[1].value >= 0 ? data.onlineNum[1].value : "-",
          ajOverNum:
            data.offlineNum[1].value >= 0 ? data.offlineNum[1].value : "-",
        };
      } else {
        this.statisticObj = {
          ojNormalNum: "-",
          ojOverNum: "-",
          fyjNormalNum: "-",
          fyjOverNum: "-",
          ajNormalNum: "-",
          ajOverNum: "-",
        };
      }
    },
    //按行政区划统计摄像头数量
    async statsCameraByAdcd() {
      let res = await hydraulicApi.statsCameraByAdcd();
      this.statisticData = [];
      let xArr = [];
      let seriesArr = [];
      let overNumArr = []; //离线数目
      let normalNumArr = []; //在线数目
      res.data.offlineNum.forEach((item) => {
        const x = item.countKey || item.key;
        xArr.push(x);
        overNumArr.push(item.value);
      });
      res.data.onlineNum.forEach((item) => {
        normalNumArr.push(item.value);
      });
      xArr = xArr.reverse();
      overNumArr = overNumArr.reverse();
      normalNumArr = normalNumArr.reverse();
      seriesArr = [
        {
          name: "离线",
          type: "bar",
          stack: "total",
          label: {
            show: false,
          },
          emphasis: {
            focus: "series",
          },
          data: overNumArr,
          itemStyle: {
            barBorderRadius: [10, 10, 10, 10],
            borderWidth: 2,
            borderColor: "#ccc",
          },
        },
        {
          name: "在线",
          type: "bar",
          stack: "total",
          label: {
            show: false,
          },
          emphasis: {
            focus: "series",
          },
          data: normalNumArr,
          itemStyle: {
            barBorderRadius: [10, 10, 10, 10],
            borderWidth: 2,
            borderColor: "#ccc",
          },
        },
      ];
      let obj = {
        yAxis: {
          data: xArr,
        },
        series: seriesArr,
        color: ["#F53526", "#37A2DA"],
      };
      this.extraOption = { ...obj };
      this.$refs.chartCapsule.updateChartView();
      this.$nextTick(() => {
        this.calcTableHeight();
      });
    },
    //雨情
    async getRainStations(type) {
      if (this.rainfallRange != "0") {
        this.rainfallRangeMin = "";
        this.rainfallRangMax = "";
      }
      const opt = {
        min:
          this.rainfallRange == "0"
            ? this.rainfallRangeMin
            : this.rainfallRange[0],
        max:
          this.rainfallRange == "0"
            ? this.rainfallRangMax
            : this.rainfallRange[1],
        minutes: this.radioVal,
        stationName: this.searchVal,
        areaCode: this.areaCode,
      };
      this.tableColumns = [
        {
          prop: "stationName",
          label: "名称",
        },
        {
          prop: "drp",
          label: "雨量(mm)",
          sortable: true,
        },
      ];
      let res = await hydraulicApi.rainStations(opt);
      this.dataList = res.data;
    },
    //水质统计
    async waterQualityStatistic(){
      let res = await hydraulicApi.bswqStatistic();
      if(res.code == 0){
        this.statisticData = [];
        let xArr = [];
        res.data.forEach((item) => {
          const x = item.countKey || item.key;
          xArr.push(x);
          const y = item.countNum || item.value;
          this.statisticData.unshift(y);
        });
        xArr = xArr.reverse();
        let obj = {
          yAxis: {
            data: xArr,
          },
        };
        this.extraOption = { ...obj };

        this.$refs.chartCapsule.updateChartView();
        this.$nextTick(() => {
          this.calcTableHeight();
        });
      }
    },
    //积水统计
    async floodPointStatistic(){
      let res = await hydraulicApi.floodPointStatistic();
      if(res.code == 0){
        this.statisticData = [];
        let xArr = [];
        res.data.forEach((item) => {
          const x = item.countKey || item.key;
          xArr.push(x);
          const y = item.countNum || item.value;
          this.statisticData.unshift(y);
        });
        xArr = xArr.reverse();
        let obj = {
          yAxis: {
            data: xArr,
          },
        };
        this.extraOption = { ...obj };

        this.$refs.chartCapsule.updateChartView();
        this.$nextTick(() => {
          this.calcTableHeight();
        });
      }
    },
    //水质
    async getWaterQualityStations(type) {
      let opt = {
        name: this.searchVal,
        adcd: this.areaCode,
      };
      this.tableColumns = [
        {
          prop: "name",
          label: "名称",
        },
        {
          prop: "drp",
          label: "水质等别",
          sortable: true,
        },
      ];
      let res = await hydraulicApi.waterQualityStation(opt);
      this.dataList = res.data;
    },
    //积水
    async getWaterLoggingStations(type) {
      let opt = {
        name: this.searchVal,
        areaCode: this.areaCode,
      };
      this.tableColumns = [
        {
          prop: "name",
          label: "名称",
        },
        {
          prop: "waterLevel",
          label: "水深（cm）",
          sortable: true,
        },
      ];
      let res = await hydraulicApi.getLogging(opt);
      this.dataList = res.data;
    },
    //视频
    async getCameraOpsStations(type) {
      let opt = {
        name: "",
        adcd: this.areaCode,
        pageNum: 0,
        pageSize: 0,
      };
      this.tableColumns = [
        {
          prop: "cameraName",
          label: "名称",
        },
        {
          prop: "status",
          label: "状态",
          sortable: true,
        },
      ];
      let res = await hydraulicApi.getCameraOps(opt);
      this.dataList = res.data.list.map((item) => {
        item.status = item.status == 0 ? "离线" : "在线";
        item.stType = "camera";
        return item;
      });
    },
    //统计图单项点击
    async getCountryData(data) {
      this.areaCode = data.name;
      this.mapObj.showAreaLine(this.areaCode);
      switch (this.stationType) {
        case "水库水位":
          {
            let remark = this.areaCodeObj[this.areaCode];
            this.rsvrNewestWaterLevel(remark, "");
          }
          break;
        case "河道水位":
        case "堰闸水位":
        case "潮位":
        case "水闸工情":
          {
            this.getWaterStations(this.stationType);
          }
          break;
        case "国控断面":
        case "省控断面":
        case "水文站水质":
          {
            this.getWaterQualityStations(this.stationType);
          }
          break;
        case "雨情":
          {
            this.getRainStations(this.stationType);
          }
          break;
        case "积水":
          {
            this.getWaterLoggingStations(this.stationType);
          }
          break;
        case "视频":
          {
            this.getCameraOpsStations(this.stationType);
          }
          break;
      }
    },
    //展示流域
    async showBas(basName) {
      this.basinName = basName;
      this.mapObj.basinCommand(this.basinName);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
.station-side {
  width: 100%;
  height: 100%;
  @include flexbox;
  @include flexflow(column, nowrap);
  .statistics-chart {
    position: relative;
    flex: 1;
    .title-right {
      position: absolute;
      right: 0;
      .icon-btn-reset {
        cursor: pointer;
        height: 30px;
      }
    }
    .chart-tabs {
      margin-top: 10px;
      @include flexbox;
      @include flexAC;
      span {
        display: inline-block;
        padding: 4px 8px;
        width: 80px;
        color: #4898fb;
        border: 1px solid #4898fb;
        text-align: center;
        line-height: 16px;
        font-size: 12px;
        margin-right: 16px;
        cursor: pointer;
        &.active {
          color: #a6e9fb;
          background: rgba(72, 152, 251, 0.1);
          border: 1px solid #23e3fb;
        }
      }
    }
    .chart-box {
      padding-top: 8px;
      .split-line {
        display: inline-block;
        width: 100%;
        height: 4px;
      }
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
            &.cursor {
              cursor: pointer;
            }
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
              padding: 6px 12px;
              &.active {
                // border: 1px dashed #4CE3FB;
                background: url(~assets/images/param-item-bg.png) no-repeat
                  center center;
                background-size: 100% 100%;
              }
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
    .table-list {
      height: 100%;
      width: 100%;
    }
  }
}
</style>
<style lang="scss">
.custom-filter {
  .sub-title {
    width: 64px;
    height: 20px;
    font-size: 14px;
    color: #23e3fb;
    position: relative;
    padding-left: 8px;
    &:before {
      content: "";
      position: absolute;
      left: 0;
      width: 3px;
      height: 14px;
      background: #23e3fb;
      border-radius: 15px;
    }
  }
  .filter-time {
    height: 48px;
    display: flex;
    align-items: center;
    > * {
      margin: 0 3px;
    }
    .el-button {
      margin-left: 16px;
    }
  }
  .el-radio {
    margin: 8px 0;
    width: 90px;
    .el-radio__inner {
      background-color: transparent;
      border: 1px solid #4694f6;
    }
    .el-radio__input.is-checked {
      .el-radio__inner {
        background: transparent;
        &:after {
          background-color: #1ef8f2;
          border-color: #1ef8f2;
          transform: translate(-50%, -50%) scale(1.8);
        }
      }
    }
    .el-radio__label {
      color: #fff;
    }
  }
  .el-date-editor {
    width: 139px;
    .el-input__prefix {
      color: #3f9bfc;
      left: unset;
      right: 8px;
      .el-input__icon {
        line-height: 1;
      }
    }
    .el-input__suffix {
      .el-input__icon {
        line-height: 1;
      }
    }
    .el-input__inner {
      padding-left: 8px;
      width: 139px;
      height: 32px;
      border: 1px solid #3f9bfc;
      border-radius: 2px;
    }
    &.el-date-editor--time-select {
      width: 88px;
      .el-input__inner {
        width: 88px;
      }
    }
  }
  .rainfall-range {
    display: flex;
    color: #fff;
    justify-content: left;
    // align-items: center;
    margin-top: 8px;
    margin-bottom: 24px;
    .btn-sty {
      padding: 5px 10px;
      font-size: 14px;
      margin-left: 10px;
      line-height: 20px;
      color: #c0c4cc;
      background: rgba(0, 0, 0, 0.4);
      border: 1px solid #3f9bfc;
    }
    .el-select {
      width: auto;
      .el-input__inner {
        // width: 88px;
        height: 32px;
        border: 1px solid #3f9bfc;
        border-radius: 2px;
      }
      .el-input__suffix {
        .el-input__icon {
          line-height: 1;
        }
      }
    }
    > .el-input {
      width: auto;
      .el-input__inner {
        width: 98px;
        height: 32px;
        border: 1px solid #3f9bfc;
        border-radius: 2px;
      }
    }
  }
  .screen-button {
    width: 72px;
    height: 32px;
    border: 1px solid #4898fb;
    border-radius: 2px;
    background: #0000;
    color: #4898fb;
    padding: 0;
  }
}
</style>
