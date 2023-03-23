<template>
  <div class="rain-forecast-card">
    <panel-item title="短临预报" hasSwitch tc @tclick="tclick">
      <!-- 短临预报降雨 标题右侧图标点击事件 -->
      <template slot="title-switch">
        <!-- <i class="icon-yinzhang-1 icon-sty" /> -->
        <i
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('短临预报')"
        />
        <!-- <i class="icon-yizhangtu icon-sty" /> -->
      </template>
      <!-- 1h, 3h, 6h 降雨量数据 -->
      <CardTabs
        :tabs-active="cardSelect == 'forecast'"
        v-model="forecastPreCardSelect"
        :list="forecastPreCardList"
        @change="precipitationCardClick"
      />
      <div class="sidebar-list switch-list">
        <el-carousel
          trigger="click"
          arrow="never"
          :autoplay="true"
          :interval="5000"
        >
          <el-carousel-item>
            <!-- 短临预报降雨极值列表 -->
            <precipition-max-list
              :active="maxSelect == 'forecast'"
              :max-list="forecastMaxList"
              @change="forecastMaxListChange"
              @active="forecastMaxListActive"
            />
          </el-carousel-item>
          <el-carousel-item ref="rainChart">
            <!-- 一小时面雨量 -->
            <div class="areal-rainfall-sty">
              <div>
                <i class="icon-diamond icon-sty" />
                <span>{{
                  `${forecastPreCardList[forecastPreCardSelect].label}面雨量`
                }}</span>
              </div>
              <capsule-tabs
                v-model="forecastPreChartTab"
                :tabs="['行政区划', '流域']"
                @change="forecastChartTabChange"
              />
            </div>
            <!-- 图表信息 -->
            <ve-histogram
              :loading="forecastChartLoading"
              :judge-width="true"
              height="120px"
              :data="forecastChartData"
              :settings="forecastChartOption.setting"
              :extend="forecastChartOption.extend"
              :grid="forecastChartOption.grid"
              :after-config="forecastConfig"
            />
          </el-carousel-item>
        </el-carousel>
      </div>
    </panel-item>
  </div>
</template>

<script>
import axios from "axios";
import PanelItem from "../components/PanelItem.vue";
import CardTabs from "../../WaterDisasterPreventionScreen/components/CardTabs.vue";
import CapsuleTabs from "../../WaterDisasterPreventionScreen/components/CapsuleTabs.vue";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
import PrecipitionMaxList from "../../WaterDisasterPreventionScreen/components/PrecipitionMaxList.vue";
import { mapGetters, mapActions } from "vuex";
import mapConfig from "@/config/mapConfig";
export default {
  name: "RainForecastCard",
  layerName: "maxPointLayer",
  legendName: ["实时降雨量", "预报降雨量"],
  components: {
    CardTabs,
    PrecipitionMaxList,
    CapsuleTabs,
    PanelItem,
  },
  mounted() {
    this.getRainfallData();
    this.timer = setInterval(() => {
      this.getRainfallData();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  data() {
    return {
      timer: null,
      cardSelect: "",
      //短临预报降雨图表tab
      forecastPreChartTabList: ["行政区划", "流域"],
      forecastPreChartTab: 0,
      //短临预报降雨卡片
      forecastPreCardSelect: 0,
      //短临预报降雨数据
      forecastPreList: {},
      //短临预报降雨极值
      forecastPreMaxList: {},
      forecastChartLoading: false,
      forecastChartData: {
        columns: ["name", "min", "max", "avg"],
        rows: [
          { name: "永嘉县", min: 136, max: 178 },
          { name: "乐清市", min: 136, max: 178 },
          { name: "瓯海区", min: 136, max: 178 },
          { name: "鹿城区", min: 136, max: 178 },
          { name: "洞头区", min: 136, max: 178 },
        ],
      },
      forecastChartOption: {
        grid: {
          top: "30%",
          bottom: "0%",
          containLabel: true,
        },
        setting: {
          yAxisName: ["mm", ""],
          labelMap: { min: "最小值", max: "最大值", avg: "平均值" },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            position: "top",
            color: "#00C1FF",
            formatter: ({ value }) => Number(value).toFixed(1),
          },
        },
        extend: {
          legend: {
            right: "0%",
            icon: "circle",
            itemWidth: 10,
            textStyle: { color: "#FFF" },
          },
          "textStyle.color": "#fff",
          barWidth: 15,
          yAxis: {
            nameTextStyle: { color: "#ffffff99" },
            max: (value) => {
              return value.max > 1 ? null : 1;
            },
            axisLabel: {
              color: "#fff",
            },
            splitLine: {
              lineStyle: {
                color: "#00C1FF",
                opacity: 0.5,
              },
            },
          },
          "series.0.itemStyle": { color: "#00C1FF" },
          "series.1.itemStyle": { color: "#EEC80B" },
        },
      },
      forecastAreaRainfallChart: [],
      forecastBasinRainfallChart: [],
      maxSelect: "",
    };
  },
  computed: {
    ...mapGetters(["ScreenMap", "currentSelectArea"]),
    forecastMaxList() {
      let time = this.forecastPreCardList[this.forecastPreCardSelect].label;
      return [
        {
          label: `预计${time}最大降雨`,
          data: this.forecastPreMaxStation,
        },
        {
          label: `预计${time}最大降雨区域`,
          data: this.forecastPreMaxArea,
        },
        {
          label: `预计${time}最大降雨流域`,
          data: this.forecastPreMaxBasin,
        },
      ];
    },
    chartHeight() {
      // const height = this.$refs.rainChart.$el.offsetHeight;
      // return height;
    },
    /** 短临预报卡片数据 */
    forecastPreCardList() {
      let list = [
        this.forecastPreList.rainfall_1,
        this.forecastPreList.rainfall_3,
        this.forecastPreList.rainfall_6,
      ];
      return [
        {
          label: "1小时",
          data: { number: list[0], unit: "mm" },
        },
        {
          label: "3小时",
          data: { number: list[1], unit: "mm" },
        },
        {
          label: "6小时",
          data: { number: list[2], unit: "mm" },
        },
      ];
    },
    /** 预报极值列表数据 */
    forecastPreMaxStation() {
      let name = this.forecastPreMaxList.maxRainfallAddress;
      let max = Number(this.forecastPreMaxList.maxRainfall).toFixed(1) || "0";
      let min = Number(this.forecastPreMaxList.minRainfall).toFixed(1) || "0";
      let lng = this.forecastPreMaxList.maxRainfallLongitude;
      let lat = this.forecastPreMaxList.maxRainfallLatitude;

      return {
        name: "",
        address: name || "",
        value: `${max}` || "0",
        type: "station",
        lng,
        lat,
      };
    },
    forecastPreMaxArea() {
      let name = this.forecastPreMaxList.maxRainfallAreaName;
      let max =
        Number(this.forecastPreMaxList.areaMaxRainfall).toFixed(1) || "0";
      let min =
        Number(this.forecastPreMaxList.areaMinRainfall).toFixed(1) || "0";
      return {
        name: name || "",
        value: `${max}` || "0",
        type: "area",
      };
    },
    forecastPreMaxBasin() {
      let name = this.forecastPreMaxList.maxRainfallBasinName;
      let max =
        Number(this.forecastPreMaxList.maxBasinRainfall).toFixed(1) || "0";
      let min =
        Number(this.forecastPreMaxList.minBasinRainfall).toFixed(1) || "0";
      return {
        name: name || "",
        value: `${max}` || "0",
        type: "basin",
      };
    },
  },
  methods: {
    ...mapActions([
      "changeScreenLoading",
      "getForecastRainfallLayer",
      "SetDetailDialogParam",
    ]),
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    /** 初始化雨量数据 */
    getRainfallData(areaName) {
      let opt = {
        areaName: "",
      };
      if (areaName && areaName != "温州市") {
        opt.areaName = areaName;
      } else {
        opt.areaName = "";
      }
      disasterPreventionApi.predictRainfallByAreaName(opt).then((res) => {
        if (res.code == 0) {
          this.forecastPreList = res.data;
        }
      });

      if (!areaName) {
        this.getForecastPreMaxValue(1);
      }
    },
    /** 选中1，3，6小时降雨卡片降切换 */
    precipitationCardClick(val) {
      if (val == null) {
        this.ScreenMap.removeLayerByName("rainfall");
        this.ScreenMap.clearTextLayer();
        this.$emit("legend-change", "实时降雨量", {});
        this.$emit("legend-change", "预报降雨量", {});
        return;
      }
      let valMap = [1, 3, 6];
      this.cardSelect = "forecast";
      let data = this.forecastPreList[`rainfall_${valMap[val]}_data`];
      if (data) {
        let arr = [];
        for (let item of data) {
          if (item.rainfall > 0) {
            arr.push(item);
          }
        }
        if (arr.length > 0) {
          this.changeScreenLoading(true);
          let params = this.formatRainfallParam(arr, "rainfall");
          this.ScreenMap._showTestPoint(params.features);
          this.getForecastRainfallLayer({
            opt: params,
            type: false,
            area: this.currentSelectArea,
            hour: valMap[val],
          }).then((result) => {
            let mapImage = result.mapImage;
            this.changeScreenLoading(false);
            this.getIsohyetOpacity(
              mapImage.xmin,
              mapImage.ymin,
              mapImage.xmax,
              mapImage.ymax,
              result.jobId,
              "forecast"
            );
          });
        } else {
          this.changeScreenLoading(false);
          this.ScreenMap.removeLayerByName("rainfall");
          this.ScreenMap.clearTextLayer();
        }
        // console.log(params,3)
        /* this.getIsohyetArea(params); */
      }
      this.getForecastPreMaxValue(valMap[val]);
      this.$emit("legend-change", "实时降雨量", {});
      this.$emit("legend-change", "预报降雨量", {
        label: "预报降雨量",
        children: [
          {
            label: ">70",
            icon: "forecast-100",
          },
          { label: "50~70", icon: "forecast-70" },
          { label: "20~50", icon: "forecast-50" },
          { label: "10~20", icon: "forecast-25" },
          { label: "3~10", icon: "forecast-10" },
          { label: "1~3", icon: "forecast-1" },
          { label: "<1", icon: "forecast-0" },
        ],
      });
    },

    /** 短临预报极值点击 */
    forecastMaxListActive() {
      this.maxSelect = "forecast";
      this.ScreenMap.drawPoint([], {}, "maxPointLayer");
      this.ScreenMap.fullMap();
    },

    /** 雨量数据转换成等雨量面参数格式 */
    formatRainfallParam(list, paramName) {
      let geoJson = {
        features: [],
      };
      let featuresArr = [];
      list.forEach((item) => {
        // let drp = item.drp > 0 ? item.drp : 0;
        // if (item[paramName] != "-" && item[paramName] > 0) {
        let obj = {
          attributes: {
            x: Number(item.longitude),
            y: Number(item.latitude),
            value: item[paramName],
          },
          geometry: {
            x: Number(item.longitude),
            y: Number(item.latitude),
          },
        };
        featuresArr.push(obj);
        // }
      });
      geoJson.features = featuresArr;
      return geoJson;
    },
    async getIsohyetOpacity(xmin, ymin, xmax, ymax, jobId, type) {
      //type用来区分预报和实时等值面服务 forecast为预报 预报请求112 实时请求117
      let bbox = xmin + "," + ymin + "," + xmax + "," + ymax;
      let params = new URLSearchParams();
      params.append("bbox", bbox);
      params.append("f", "pjson");
      params.append("transparent", true);
      let url = `${mapConfig.mapServer}gps/dengzhiPIDWReal0605/MapServer/jobs/${jobId}/export`;
      if (type == "forecast") {
        url = `${mapConfig.mapServer}gps/PointTRaster/MapServer/jobs/${jobId}/export`;
      }
      await axios({
        url: url,
        method: "post",
        headers: { "content-type": "application/x-www-form-urlencoded" },
        data: params,
      }).then((res) => {
        this.changeScreenLoading(false);
        this.ScreenMap.showIsohyetLayer(res.data);
      });
    },
    /** 获得预报极值数据 */
    getForecastPreMaxValue(hour) {
      this.forecastChartLoading = true;
      disasterPreventionApi.rainfallMaxValuePredict({ hour }).then((res) => {
        this.forecastPreMaxList = { ...res.data };
      });
      if (
        this.forecastAreaRainfallChart.length == 0 &&
        this.forecastBasinRainfallChart.length == 0
      ) {
        disasterPreventionApi
          .rainfallPredictGrid({ type: "流域" })
          .then((res) => {
            this.forecastBasinRainfallChart = res.data;
          });
        disasterPreventionApi
          .rainfallPredictGrid({ type: "行政区划" })
          .then((res) => {
            this.forecastAreaRainfallChart = res.data;
            this.forecastChartTabChange(this.forecastPreChartTab, hour);
          });
      }
      this.forecastChartTabChange(this.forecastPreChartTab, hour);
      this.forecastChartLoading = false;
    },
    /** 短临预报降雨图表tabs切换 */
    forecastChartTabChange(index, hour) {
      hour = hour || [1, 3, 6][this.forecastPreCardSelect];
      let tab = this.forecastPreChartTabList[index];
      let data = {
        行政区划: this.forecastAreaRainfallChart || [],
        流域: this.forecastBasinRainfallChart || [],
      };
      if (!data[tab]) return;
      this.forecastChartData.rows = [];
      let rows = [];
      const sortData = data[tab].sort(
        (a, b) => b[`rainfall_${hour}`] - a[`rainfall_${hour}`]
      );
      for (let item of sortData) {
        if (rows.length < 5) {
          rows.push({
            name: item.key,
            max: item[`${hour}hourMaxRainfall`],
            min: item[`${hour}hourMinRainfall`],
            avg: item[`rainfall_${hour}`],
          });
        } else break;
      }
      this.forecastChartData.rows = rows;
    },
    forecastMaxListChange(status, data) {
      let type = data.type;
      if (type == "station") {
        let point = [
          {
            lng: data.lng,
            lat: data.lat,
            no_pointermove: false,
            // name: data.address,
            props: {
              /* 测站: `${(data.name, data.value)}mm` */
            },
          },
        ];
        point[0].props[`${data.address}`] = `${data.value}mm`;
        let style = {
          src: require("@/assets/images/WaterDisastersImage/torrent-flag.png"),
          anchor: [0.5, 1],
          anchorXUnits: "fraction",
          anchorYUnits: "fraction",
        };
        if (status) {
          this.ScreenMap.drawPoint(point, style, "maxPointLayer");
          this.ScreenMap.flyPoint([data.lng, data.lat], 12);
        } else {
          this.ScreenMap.drawPoint([], {}, "maxPointLayer");
          this.ScreenMap.fullMap();
        }
      } else if (type == "area") {
        status
          ? this.ScreenMap.showAreaLine(data.name, true)
          : this.ScreenMap.clearAreaLayer();
      } else if (type == "basin") {
        status
          ? this.ScreenMap.basinCommand(`${data.name}流域`)
          : this.ScreenMap.basinCommand("全流域");
      }
    },
    forecastConfig(options) {
      const minData = options.series[0].data;
      const maxData = options.series[1].data;
      const avgData = options.series[2].data;
      let colorArr = ["#15ddd7", "#7dfefa", "#9dfffc"];
      let colorArr2 = ["#2886c6", "#50bfda", "#89e3ec"];

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
      let color2 = {
        type: "linear",
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr2[0],
          },
          {
            offset: 0.5,
            color: colorArr2[0],
          },
          {
            offset: 0.5,
            color: colorArr2[1],
          },
          {
            offset: 1,
            color: colorArr2[1],
          },
        ],
      };
      let barWidth = 20;
      let constData = [];
      let showData = [];

      /** 最小柱形 */
      constData = minData.map((item) => (item ? 1 : 0));
      showData = minData.map((item) =>
        item
          ? item
          : {
              value: 1,
              itemStyle: {
                normal: {
                  borderColor: "rgba(0,0,0,0)",
                  borderWidth: 2,
                  color: "rgba(0,0,0,0)",
                },
              },
            }
      );
      const minSeries = [
        {
          z: 1,
          name: "最小值",
          type: "bar",
          barWidth: barWidth,
          barGap: "0%",
          data: minData,
          itemStyle: {
            normal: {
              color: color,
            },
          },
          label: {
            show: true,
            fontSize: 18,
            fontWeight: "bold",
            position: "top",
            color: "#00C1FF",
            formatter: ({ value }) => Number(value).toFixed(1),
          },
        },
        {
          z: 2,
          name: "最小值",
          type: "pictorialBar",
          data: constData,
          symbol: "diamond",
          symbolOffset: [(-1 * barWidth) / 2, "50%"],
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
          name: "最小值",
          type: "pictorialBar",
          symbolPosition: "end",
          data: showData,
          symbol: "diamond",
          symbolOffset: [(-1 * barWidth) / 2, "-50%"],
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
      /** 最大柱形 */
      constData = maxData.map((item) => (item ? 1 : 0));
      showData = maxData.map((item) =>
        item
          ? item
          : {
              value: 1,
              itemStyle: {
                normal: {
                  borderColor: "rgba(0,0,0,0)",
                  borderWidth: 2,
                  color: "rgba(0,0,0,0)",
                },
              },
            }
      );
      const maxSeries = [
        {
          z: 1,
          name: "最大值",
          type: "bar",
          barWidth: barWidth,
          barGap: "0%",
          data: maxData,
          itemStyle: {
            normal: {
              color: color2,
            },
          },
          label: {
            show: true,
            fontSize: 18,
            fontWeight: "bold",
            position: "top",
            color: "#00C1FF",
            formatter: ({ value }) => Number(value).toFixed(1),
          },
        },
        {
          z: 2,
          name: "最大值",
          type: "pictorialBar",
          data: constData,
          symbol: "diamond",
          symbolOffset: [barWidth / 2, "50%"],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color2,
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          z: 3,
          name: "最大值",
          type: "pictorialBar",
          symbolPosition: "end",
          data: showData,
          symbol: "diamond",
          symbolOffset: [barWidth / 2, "-50%"],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr2[2],
              borderWidth: 2,
              color: colorArr2[2],
            },
          },
          tooltip: {
            show: false,
          },
        },
      ];
      /** 平均 */
      options.series = [
        ...minSeries,
        ...maxSeries,
        {
          name: "平均值",
          smooth: false,
          type: "line",
          data: avgData,
        },
      ];

      options.legend.data = [...options.legend.data, "平均值"];
      options.legend.selectedMode = false;
      return options;
    },
    openDetailsDialog(code) {
      this.SetDetailDialogParam({
        mediumSized: "",
        codeTpye: code,
        analyseDialogShow: true,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.rain-forecast-card {
  height: 100%;
  // ::v-deep .panel-item__content{
  //   padding: 0 16px;
  // }
  // padding-bottom: 12px;`
  .drag {
    padding: 8px 16px;
  }
  .switch-list {
    ::v-deep.el-carousel--horizontal {
      overflow-x: hidden;
      overflow-y: hidden;
    }
  }
  .water-gate-sty {
    margin-bottom: 0;
  }

  .sidebar-list {
    height: calc(100% - 102px);
    // margin-top: 12px;
    margin-top: 0px;
    .precipition-max-list {
      @include flexbox;
      @include flexflow(column, nowrap);
      @include flexJC(space-between);
      height: 100%;
      ::v-deep .precipition-max-item {
        // margin-bottom: 3%;
        font-size: 16px;
        margin-bottom: 0;
      }
    }
    .areal-rainfall-sty {
      @include flexbox;
      @include flexflow(row, nowrap);
      @include flexAC;
      @include flexJC(space-between);
    }
    ::v-deep .ve-histogram {
      height: 100%;
    }

    .warning-style {
      font-size: 16px;
      color: #ffffff;
      line-height: 24px;
      font-weight: 400;
    }
    ::v-deep.el-carousel--horizontal {
      overflow-x: hidden;
      overflow-y: auto;
      height: 100%;
      min-height: 150px;
      .el-carousel__container {
        height: 100%;
      }
    }
  }
}
</style>
