<template>
  <div class="typhoon-effect">
    <!-- <div>
      <span>工程类型:</span>
      <el-select
        remote
        v-model="projectType"
        :loading="typeOptionLoading"
        style="width: 8.75rem; margin-bottom: 0.5rem"
        @change="getProjectList"
      >
        <el-option
          v-for="(item, index) in typeOptions"
          :key="index"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </div> -->

    <table-title title="关联工程">
      <template #append>
        <i v-if="chartType == 'card'" :class="[
            pointActive ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'mr-12',
          ]" @click="onPointActiveClick" />
        <i :class="[
            chartType == 'card' ? 'icon-card-chart-active' : 'icon-card-chart','mr-12',
          ]" @click="doChartChange('card')" />
        <i :class="[
            chartType == 'bar' ? 'icon-line-chart-active' : 'icon-line-chart',
            'mr-12',
          ]" @click="doChartChange('bar')" />
        <!-- <i :class="[
            chartType == 'pie' ? 'icon-pie-chart-active' : 'icon-pie-chart',
          ]" @click="doChartChange('pie')" /> -->
      </template>
    </table-title>

    <div class="chart-type-box">
      <!-- 柱状图 -->
      <ve-histogram v-if="chartType == 'bar'" ref="chart" height="100%" width="100%" :data="barData" :settings="barOption.setting" :extend="barOption.extend" :grid="barOption.grid" :data-zoom="barOption.dataZoom" :tooltip-visible="false" :legend-visible="false" :after-config="barConfig" />
      <!-- 饼图 -->
      <div class="pie-chart" v-if="chartType == 'pie'">
        <div class="pie-box">
          <EchartPie ref="pie" :seriesData="pieData" :extraOption="pieOption"></EchartPie>
        </div>
        <div class="pie-legend">
          <div v-for="(item, index) in pieData" :key="index">
            <template v-if="item.name">
              <span>
                <i class="legend-circle" :style="{ background: item.color }"></i>
                {{ item.name }}
              </span>
              <span>
                <span class="legend-num">{{ item.value }}</span>
                个
              </span>
            </template>
          </div>
        </div>
      </div>
      <!-- 卡片 -->
      <div class="cards-chart" v-show="chartType == 'card'">
        <div :class="['card-item', projectType == item.name ? 'active' : '']" @click="doTypeClick(item.name)" :style="
            (index + 1) % 2 == 0
              ? {
                  'border-left': '1px solid rgba(0,193,255,0.2)',
                  'border-right': '1px solid rgba(0,193,255,0.2)',
                }
              : {}
          " :key="item.name" v-for="(item, index) in barData.rows">
          <div>
            <p>
              <b>{{ item.value }}</b>座
            </p>
            <p>
              <span>{{ item.name }}</span>
            </p>
          </div>
        </div>
      </div>
    </div>
    <div class="sub-title"><b>工程列表</b></div>
    <div ref="typhoonTable" style="flex: 1">
      <el-table v-loading="projectListLoading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)" :data="projectList" :height="projectListHeight">
        <el-table-column type="index" label="序号" />
        <el-table-column label="工程类型" prop="projectType" />
        <el-table-column label="名称" prop="projectName" />
      </el-table>
    </div>
    <table-title title="历史相似灾情"> </table-title>
    <div class="influence-list">
      <div class="no-data" v-if="influenceList.length == 0">暂无数据</div>
      <div class="list-item" v-for="(item, index) in influenceList" :key="index">
        <el-row class="item-line line-title">
          <!-- <el-col :span="6">
              <span>{{ item.adnm }}</span>
            </el-col>
            <el-col :span="6">
              <span>{{ item.year }}</span>
            </el-col> -->
          <el-col :span="24">
            <span>{{ item.typhoonName }} ( {{ item.year }} ) </span>
          </el-col>
          <!-- <el-col :span="6">
              <span>{{ item.community }}</span>
            </el-col> -->
        </el-row>
        <el-row class="item-line">
          <div>
            <i class="icon-tfyx-xzqh"></i>
            影响行政区划：{{ item.adnm }}
          </div>
        </el-row>
        <el-row class="item-line">
          <div>
            <i class="icon-tfyx-ryss"></i>
            <span>人员损失: {{ item.personLoss }}</span>
          </div>
        </el-row>
        <el-row class="item-line">
          <div>
            <i class="icon-tfyx-wzss"></i>
            <span>物资损失: {{ item.materialLoss }}</span>
          </div>
        </el-row>
        <el-row class="item-line line"></el-row>
      </div>
    </div>
  </div>
</template>
<script>
import { getTyphoonEffectedProject, getTyphoonAffects, listTyphoonAllRealProjectNewestVersion } from "@/api/typhoon";
import { TableTitle } from "../../components.js";
import { generateDataZoom } from "../../../utils/chart.js";
import EchartPie from "../../../../charts/echart_pie/EchartPie.vue";
export default {
  name: "TyphoonEffect",
  layerName: "effectLayer",
  props: { pointData: Object, typhoonName: String },
  components: { TableTitle, EchartPie },
  data() {
    return {
      projectType: "",
      typeOptionLoading: false,
      typeOptions: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "水库",
          value: "水库",
        },
        {
          label: "山塘",
          value: "山塘",
        },
        {
          label: "堤防",
          value: "堤防",
        },
        {
          label: "海塘",
          value: "海塘",
        },
        {
          label: "泵站",
          value: "泵站",
        },
        {
          label: "水闸",
          value: "水闸",
        },
        {
          label: "闸站",
          value: "闸站",
        },
        {
          label: "灌区",
          value: "灌区",
        },
        {
          label: "农饮水",
          value: "农饮水",
        },
        {
          label: "水电站",
          value: "水电站",
        },
      ],

      projectListLoading: false,
      projectList: [],
      projectListHeight: 0,
      pointActive: false,
      typePoints: {},
      influenceList: [],
      chartType: 'card',
      barData: {
        columns: ["name", "data"],
        rows: [],
      },
      barOption: {
        dataZoom: {
          type: "slider",
          startValue: 0,
          endValue: 4,
        },
        grid: {
          top: 30,
          bottom: 20,
          containLabel: true,
        },
        setting: {
          yAxisName: ["个"],
          itemStyle: { color: "#fff" },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            fontWeight: "bold",
            position: "top",
            color: "#fff",
          },
        },
        extend: {
          "textStyle.color": "#fff",
          barWidth: 15,
          xAxis: {
            axisLabel: {
              interval: 0,
              fontSize: this.$commonSetting.chartFontSize,
            },
          },
          yAxis: {
            nameTextStyle: {
              color: "#ffffff99",
              fontSize: this.$commonSetting.chartFontSize,
            },
            axisLine: {
              show: false,
            },
            axisLabel: {
              color: "#fff",
              fontSize: this.$commonSetting.chartFontSize,
            },
            splitLine: {
              lineStyle: {
                color: "#fff",
                opacity: 0.5,
              },
            },
            max: (value) => {
              return value.max > 1 ? null : 1;
            },
          },
        },
      },

      pieData: [],
      pieLegend: [],
      pieOption: {
        tooltip: {
          show: true,
          trigger: "item",
          formatter: function (data) {
            return data.data.name + ":" + "  " + data.data.value + "个";
          },
          confine: true,
          extraCssText: "font-size:18px;",
        },
        series: [
          {
            type: "pie",
            radius: ["60%", "90%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: false,
                position: "center",
                color: "#fff",
                fontSize: 18,
              },
            },
            labelLine: {
              show: false,
            },
          },
        ],
        // color: [
        //   {
        //     type: "linear",
        //     x: 0,
        //     y: 0,
        //     x2: 1,
        //     y2: 1,
        //     colorStops: [
        //       {
        //         offset: 0,
        //         color: "#1CFFF5", // 0% 处的颜色
        //       },
        //       {
        //         offset: 1,
        //         color: "rgba(0,0,0,0.7)", // 100% 处的颜色
        //       },
        //     ],
        //     global: false, // 缺省为 false
        //   },
        //   {
        //     type: "linear",
        //     x: 0,
        //     y: 0,
        //     x2: 1,
        //     y2: 1,
        //     colorStops: [
        //       {
        //         offset: 0,
        //         color: "#FF7144", // 0% 处的颜色
        //       },
        //       {
        //         offset: 1,
        //         color: "rgba(0,0,0,0.7)", // 100% 处的颜色
        //       },
        //     ],
        //     global: false, // 缺省为 false
        //   },
        // ],
      },

      selectType: '',

    };
  },
  watch: {
    pointData: {
      immediate: true,
      handler(val, oldVal) {
        if (val) {
          this.fitTableHeight();
          this.getProjectList();
          this.getTyphoonProjectStatistic();
          this.getTyphoonAffects();
        }
      },
    },
  },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.fitTableHeight();
      this.getTyphoonAffects();
      this.getTyphoonProjectStatistic();
    })
  },
  updated() {
    this.fitTableHeight();
  },
  beforeDestroy() {
    this.clearFeature();
  },
  methods: {
    doChartChange(type) {
      this.chartType = type;
      if (this.chartType != 'card') {
        this.pointActive = false;
        this.clearFeature();
      }
    },
    fitTableHeight() {
      this.$nextTick(() => {
        this.projectListHeight = this.$refs.typhoonTable.offsetHeight;
      });
    },
    //清除地图要素
    clearFeature() {
      for (let type in this.typePoints) {
        if (type == '堤防' || type == '海塘') {
          this.map.removeLineLayer();
        } else {
          this.map.drawClusterPoint([], {}, `effect${type}Layer`);
        }
      }
    },
    onPointActiveClick() {
      this.pointActive = !this.pointActive;
      if (this.pointActive) {
        this.pointDownMap();
      } else {
        this.clearFeature();
      }
    },
    pointDownMap() {
      let styleMap = {
        水库: {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/dianwei-juhe.png"),
          src: require("@/assets/images/legend/shuiku-juhe.png"),
        },
        山塘: {
          scale: 0.7,
          anchorXUnits: "pixels",
          anchor: [27, 30],
          src: require("@/assets/images/pond1big.png"),
          srcs: require("@/assets/images/pond1big-juhe.png"),
        },
        堤防: {
          scale: 0.7,
          anchorXUnits: "pixels",
          anchor: [27, 30],
          src: require("@/assets/images/MapPoint/seawall.png"),
          srcs: require("@/assets/images/MapPoint/seawall.png"),
        },
        海塘: {
          scale: 0.7,
          anchorXUnits: "pixels",
          anchor: [27, 30],
          src: require("@/assets/images/MapPoint/seawall.png"),
          srcs: require("@/assets/images/MapPoint/seawall.png"),
        },
        泵站: {
          scale: 0.7,
          anchorXUnits: "pixels",
          anchor: [27, 30],
          src: require("@/assets/images/pump3big.png"),
          srcs: require("@/assets/images/pump3big-juhe.png"),
        },
        水闸: {
          scale: 0.7,
          anchorXUnits: "pixels",
          anchor: [27, 30],
          src: require("@/assets/images/MapPoint/point-sluice-normal.png"),
          srcs: require("@/assets/images/MapPoint/group-sluice-normal.png"),
        },
        闸站: {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/MapPoint/map-sluicestation-cluster.png"),
          src: require("@/assets/images/MapPoint/map-sluicestation.png"),
        },
        灌区: {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/dianwei-juhe.png"),
          src: require("@/assets/images/legend/shuiku-juhe.png"),
        },
        农饮水: {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/MapPoint/map-farmwater-cluster.png"),
          src: require("@/assets/images/MapPoint/map-farmwater.png"),
        },
        水电站: {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/dianwei-juhe.png"),
          src: require("@/assets/images/legend/shuiku-juhe.png"),
        },
        在建工程: {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/MapPoint/advance-works-icons-cluster.png"),
          src: require("@/assets/images/MapPoint/advance-works-icons.png"),
        }
      };
      let pointList = this.projectList.map((item) => {
        let obj = {
          name: item.projectName,
          lng: item.longitude,
          lat: item.latitude,
          type: item.projectType,
          code: item.projectCode,

        }
        // if (item.projectType === '山塘') {
        //   let props = {
        //     行政区划: `${item.countryAdnm || "-"}`,
        //     山塘类别: `${item.mnpdCate || "-"}`,
        //     主要功能: `${item.zygn || "-"}`,
        //   }
        //   obj.props = props
        // }
        if (item.projectType == '海塘') {
          obj.info = item.seawallInfo;
        } else if (item.projectType == '堤防') {
          obj.info = item.dikeInfo;
        }
        return obj;
      });
      let typePoints = {};
      pointList.forEach((item, index) => {
        if (typePoints.hasOwnProperty(item.type)) {
          typePoints[item.type].push(item);
        } else {
          typePoints[item.type] = [item];
        }
      });
      this.typePoints = typePoints;
      for (let type in typePoints) {
        if (type == '堤防') {
          typePoints[type].forEach(dike => {
            let color = "#f40305";
            if (dike.dikeGrad == '4') {
              color = "#fbda17";
            } else if (dike.dikeGrad == '5') {
              color = "#4fff01";
            }
            let style = {
              lineWidth: 4,
              lineColor: color,
            };
            let lineString = dike.info.coorpot && dike.info.coorpot.replaceAll('&quot;', "'") || "[]";
            let coordinates = lineString && eval(lineString);
            try {
              coordinates = coordinates.map(line => {
                line = line.map(item => {
                  return [item.lng, item.lat];
                });
                return {
                  lineList: line,
                  data: {
                    type: "堤防",
                    name: dike.name,
                    code: dike.code,
                    props: {
                      '堤防类型': dike.info.dikeType,
                      '防洪标准(年)': dike.info.planFlSta || '-',
                      '堤防长(米)': dike.info.dikeLen || '-'
                    },
                    ...dike
                  }
                };
              });
              this.map.drawLineEx(coordinates, style, 'dikeLineLayer');
            } catch (error) {
              console.log('该条数据有误', dike);
            }
          });
        } else if (type == "海塘") {
          typePoints[type].forEach(seawall => {
            let color = "#E80000";
            if (seawall.info.designTideStandard < 10) {
              color = "#E80000";
            } else if (seawall.info.designTideStandard == 10) {
              color = "#DD00DB";
            } else if (seawall.info.designTideStandard == 20) {
              color = "#E89100";
            } else if (seawall.info.designTideStandard == 50) {
              color = "#EEC80B";
            } else if (seawall.info.designTideStandard == 100) {
              color = "#19AF47";
            } else if (seawall.info.designTideStandard > 100) {
              color = "#4CEBC7";
            } else if (seawall.info.designTideStandard == null) {
              color = "#8D0A0A";
            }
            let style = {
              lineWidth: 4,
              lineColor: color,
            };
            let lineString = seawall.info.coorpot && seawall.info.coorpot.replaceAll('&quot;', "'") || "[]";
            let coordinates = lineString && eval(lineString);
            try {
              coordinates = coordinates.map(line => {
                line = line.map(item => {
                  return [item.lng, item.lat];
                });
                return {
                  lineList: line,
                  data: {
                    type: "海塘",
                    name: seawall.name,
                    code: seawall.code,
                    props: {
                      海塘长度: seawall.info.seawallLength + "m",
                      防潮标准: seawall.info.designTideStandard + "m",
                      所在位置: seawall.info.village,
                    },
                    ...seawall
                  }
                };
              });
              this.map.drawLineEx(coordinates, style, 'seawallLineLayer');
            } catch (error) {
              console.log('该条数据有误', seawall);
            }
          });
        } else {
          this.map.drawClusterPoint(typePoints[type], styleMap[type], `effect${type}Layer`);
        }
      }
    },
    getProjectList() {
      const { pointData } = this;
      if (pointData && Object.keys(pointData).length == 0) return;
      this.projectListLoading = true;
      let { lng, lat, se, sw, ne, nw } = pointData;
      let opt = { projectType: this.projectType, type: 2, lng, lat, se, sw, ne, nw, withProjInfo: true };
      getTyphoonEffectedProject(opt).then((res) => {
        this.projectList = res.data;
        this.projectListLoading = false;
        if (this.pointActive) {
          this.clearFeature();
          this.pointDownMap();
        }
      });
    },
    async getTyphoonProjectStatistic() {
      let { lng, lat, se, sw, ne, nw } = this.pointData;
      let opt = { lng, lat, se, sw, ne, nw, withProjInfo: true, type: 2 };
      listTyphoonAllRealProjectNewestVersion(opt).then(res => {
        let rows = [];
        let projectTypeRows = [];
        const colorArr = ['#00EBC7', '#E89100', '#008FE8', '#FF7144', '#19AF47', '#004FE3', '#FF9595', '#A80BEE', '#89BA8B', '#8A8AE1', '#28C9F6', '#FFEC36', '#2DDF3B', '#DBA380'];
        let chartColorArr = [];
        for (let i = 0; i < res.data.groupByAdcd.length; i++) {
          rows.push({
            name: res.data.groupByAdcd[i].areaName,
            data: res.data.groupByAdcd[i].projects.length,
            value: res.data.groupByAdcd[i].projects.length,
            color: colorArr[i]
          });
        }
        for (let item of res.data.groupByProjectType) {
          projectTypeRows.push({ name: item.projectType, data: item.projects.length, value: item.projects.length })
        }
        this.barData.rows = projectTypeRows;
        this.pieData = rows;
        for (let item of colorArr) {
          let colorObj =
          {
            type: "linear",
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: item, // 0% 处的颜色
              },
              {
                offset: 1,
                color: "rgba(0,0,0,0.5)", // 100% 处的颜色
              },
            ],
            global: false, // 缺省为 false
          };
          chartColorArr.push(colorObj);
        }
        this.pieOption.color = chartColorArr;
        this.$refs["chart"].echarts.resize();
      })
    },
    async getTyphoonAffects() {
      let opt = {
        name: this.typhoonName,
        fetchAll: true,
      }
      let res = await getTyphoonAffects(opt);
      if (res.data.list == null) {
        this.influenceList = [];
      } else {
        this.influenceList = res.data.list;
      }
    },
    barConfig(options) {
      let data = options.series[0].data;
      let colorArr = [];
      colorArr = ["#15E2DB", "#9DFDFB", "#A6FFFF"];
      let color = {
        type: "linear",
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr[0],
          },
          {
            offset: 0.5,
            color: colorArr[0],
          },
          {
            offset: 0.5,
            color: colorArr[1],
          },
          {
            offset: 1,
            color: colorArr[1],
          },
        ],
      };
      let barWidth = 30;
      let constData = [];
      let showData = [];
      data.filter(function (item) {
        if (item) {
          constData.push(1);
          showData.push(item);
        } else {
          constData.push(0);
          showData.push({
            value: 1,
            itemStyle: {
              normal: {
                borderColor: "rgba(0,0,0,0)",
                borderWidth: 2,
                color: "rgba(0,0,0,0)",
              },
            },
          });
        }
      });

      options.series = [
        {
          z: 1,
          name: "数据",
          type: "bar",
          barWidth: barWidth,
          barGap: "0%",
          data: data,
          itemStyle: {
            normal: {
              color: color,
            },
          },
          label: {
            show: true,
            fontSize: 18,
            position: "top",
            color: "#1EF8F2",
          },
        },
        {
          z: 2,
          name: "数据",
          type: "pictorialBar",
          data: constData,
          symbol: "diamond",
          symbolOffset: ["0%", "50%"],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color,
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          z: 3,
          name: "数据",
          type: "pictorialBar",
          symbolPosition: "end",
          data: showData,
          symbol: "diamond",
          symbolOffset: ["0%", "-50%"],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr[2],
              borderWidth: 2,
              color: colorArr[2],
            },
          },
          tooltip: {
            show: false,
          },
        },
      ];
      options.dataZoom = generateDataZoom(4, this.barData.rows.length);
      return options;
    },
    doTypeClick(type) {
      if (type == this.projectType) {
        this.projectType = "";
      } else {
        this.projectType = type;
      }
      this.getProjectList();
    }
  },
};
</script>
<style lang="scss">
.typhoon-effect {
  height: 100%;
  display: flex;
  flex-direction: column;
  .mr-12 {
    margin-right: 8px;
  }
  .sub-title {
    @include flexbox;
    @include flexJC(space-between);
    @include flexAC;
    font-size: 14px;
    color: #ffffff;
    padding-bottom: 12px;
  }
  .chart-type-box {
    height: 200px;
    margin-bottom: 12px;
    .pie-chart {
      width: 100%;
      height: 100%;
      @include flexbox;
      @include flexflow(row, nowrap);
      .pie-box {
        width: 45%;
        height: 100%;
      }
      .pie-legend {
        // display: flex;
        // flex-direction: column;
        // justify-content: center;
        width: 55%;
        height: 100%;
        font-size: 16px;
        padding-left: 30px;
        overflow: auto;
        > div {
          display: flex;
          justify-content: space-between;
          line-height: 30px;

          &:last-child .legend-circle {
            background: #ff7144;
          }
          .legend-circle {
            display: inline-block;
            height: 14px;
            width: 14px;
            border-radius: 50%;
            background: #1cfff5;
            vertical-align: middle;
            margin-right: 10px;
          }
          .legend-num {
            font-size: 20px;
            color: #1cfff5;
            margin-right: 7px;
          }
        }
      }
    }

    .cards-chart {
      width: 100%;
      height: 100%;
      overflow: auto;
      @include flexbox;
      @include flexflow(row, wrap);
      .card-item {
        width: 33.3%;
        height: 75px;
        padding: 0 16px;
        margin-bottom: 16px;
        cursor: pointer;

        > div {
          width: 100%;
          height: 100%;
          @include flexbox;
          @include flexflow(column, nowrap);
          @include flexJC(space-around);

          p {
            color: #fff;
            font-size: 16px;
            text-align: center;
            b {
              font-size: 30px;
              color: #1cfff5;
              padding: 0 6px;
            }
            span {
              font-size: 20px;
            }
          }
        }

        &.active > div {
          $color: #1cfff485;
          background: linear-gradient(to bottom, $color, $color) left top no-repeat,
            linear-gradient(to bottom, $color, $color) left top no-repeat,
            linear-gradient(to bottom, $color, $color) right top no-repeat,
            linear-gradient(to bottom, $color, $color) right top no-repeat,
            linear-gradient(to bottom, $color, $color) left bottom no-repeat,
            linear-gradient(to bottom, $color, $color) left bottom no-repeat,
            linear-gradient(to bottom, $color, $color) right bottom no-repeat,
            linear-gradient(to bottom, $color, $color) right bottom no-repeat;
          background-color: rgba(28, 255, 245, 0.1);
          /*设置大小*/
          background-size: 12px 2px, 2px 12px;
          border-radius: 3px;
        }
      }
    }
  }
  .influence-list {
    // @include flex(1);
    overflow: auto;
    .no-data {
      width: 100%;
      height: 100%;
      color: #fff;
      text-align: center;
      @include flexbox;
      @include flexflow(wrap, nowrap);
      @include flexJC(center);
      @include flexAC;
    }
    .list-item {
      color: #fff;
      margin-bottom: 16px;
      font-size: 14px;
      .item-line {
        margin-top: 16px;
        line-height: 24px;
        i {
          display: inline-block;
          width: 16px;
          height: 16px;
          margin-right: 8px;
          vertical-align: middle;
        }
        span {
          vertical-align: middle;
        }
        &.line-title {
          font-size: 20px;
        }
        &.line {
          width: 100%;
          height: 1px;
          background-color: rgba(255, 255, 255, 0.3);
        }
      }
    }
  }
}
</style>
