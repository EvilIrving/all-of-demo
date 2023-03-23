<!--
 * @Author: wqn
 * @Date: 2023-02-14 11:42:01
 * @LastEditors: dtb
 * @LastEditTime: 2023-03-08 09:34:04
 * @Description: 分布情况
-->
<template>
  <ModuleLayout title="分布情况" unit="" disabled>
    <div slot="extra">
      <span class="extra-clear" @click="clearEcharts" v-show="chartIndex !== ''">还原</span>
    </div>
    <div class="reservoir-number">
      <div ref="reservoirNumber" v-on-echart-resize></div>
    </div>
    <MapLegend v-if="reservoirScale === '水库数量'">
      <el-checkbox-group class="map-legend-checkbox" v-model="checkList" @change="changeCheckList">
        <el-checkbox :label="1"><img src="@/assets/images/map_points/reservoir-lv1-green.png" /><span>大（I）型水库</span></el-checkbox>
        <el-checkbox :label="2"><img src="@/assets/images/map_points/reservoir-lv2-green.png" /><span>大（II）型水库</span></el-checkbox>
        <el-checkbox :label="3"><img src="@/assets/images/map_points/reservoir-lv3-green.png" /><span>中型水库</span></el-checkbox>
        <el-checkbox :label="4"><img src="@/assets/images/map_points/reservoir-lv4-green.png" /><span>小（I）型水库</span></el-checkbox>
        <el-checkbox :label="5"><img src="@/assets/images/map_points/reservoir-lv5-green.png" /><span>小（II）型水库</span></el-checkbox>
      </el-checkbox-group>
    </MapLegend>
  </ModuleLayout>
</template>

<script>
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "ReservoirNumber",
  props: {
    map: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      pointsList: [],
      checkList: [1, 2, 3, 4, 5],
      chartIndex: "",
      chartData: {
        title: "水库数量（座）",
        xShupai: true,
        id: "skfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        adnmList: [],
        unit: "座",
      },
    };
  },
  computed: {
    selectAdcd() {
      return this.$store.state.selectAdcd;
    },
    reservoirScale() {
      return this.$store.state.reservoir.reservoirScale;
    },
    projectScale() {
      return this.$store.state.reservoir.projectScale;
    },
  },
  watch: {
    selectAdcd() {
      this.getSkfbData();
    },
    projectScale() {
      this.getSkfbData();
    },
    reservoirScale(value) {
      if (value !== "水库数量") {
        this.clearEcharts();
      }
    },
  },
  created() { },
  mounted() {
    this.getSkfbData();
  },
  destroyed() { },
  methods: {
    //点位图例
    changeCheckList() {
      this.$olMapUtil.drawPoint(
        this.map,
        this.pointsList.filter((e) =>
          this.checkList.includes(Number(e.data.project_scale))
        ),
        "水库数量落点"
      );
    },
    //获取水库数据
    async getSkfbData() {
      let opt = {
        statistic: "adcd",
        adcd: this.selectAdcd,
        itemValue: this.projectScale,
      };
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        const xData = [];
        const yData = [];
        const adcdList = [];
        const adnmList = [];
        for (let i = 0; i < res.data.length; i++) {
          adnmList.push(res.data[i].adnm);
          if (res.data[i].adnm.length > 2) {
            xData.push(res.data[i].adnm.replace(/市/, ""));
          } else {
            xData.push(res.data[i].adnm);
          }
          yData.push({ value: res.data[i].num });
          adcdList.push(res.data[i].adcd);
        }
        this.chartData.xData = xData;
        this.chartData.yData = yData;
        this.chartData.adcdList = adcdList;
        this.chartData.adnmList = adnmList;
        this.clearEcharts();
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.skfbLoading = false;
    },
    //清楚选中状态
    clearEcharts() {
      this.chartData.yData.forEach((item) => {
        if (item.itemStyle) delete item.itemStyle;
      });
      this.chartData.xData.forEach((item) => {
        if (item.textStyle) delete item.textStyle;
      });
      if (this.reservoirScale === "水库数量") {
        this.$store.commit("setReservoirScale", "");
      }
      this.chartIndex = "";
      this.removeDangerPoint();
      this.initChart();
    },
    //初始化echarts
    initChart() {
      let myChart = this.$echarts.init(this.$refs.reservoirNumber);
      let option = {
        // tooltip: {
        //   trigger: "axis",
        //   triggerOn: 'click',
        //   alwaysShowContent: true,
        //   hideDelay: 999999999,
        //   axisPointer: {
        //     type: "shadow",
        //   },
        //   formatter: this.chartData.noWords
        //     ? "{b}"
        //     : "{b}: {c} " + this.chartData.unit,
        // },
        legend: {
          top: "15",
          textStyle: {
            color: "#fff",
            fontSize: 14,
          },
        },
        grid: {
          top: "60",
          left: "4%",
          right: "3%",
          bottom: "5%",
          containLabel: true,
        },
        xAxis: {
          data: this.chartData.xData,
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#b2d9ee",
            },
            formatter: (data) => {
              if (data.value) return data.value;
              return data;
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
        },
        yAxis: {
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            textStyle: {
              color: "#5b8da8",
              fontSize: 12,
            },
          },
          splitLine: {
            lineStyle: {
              color: "#658598",
              opacity: 0.2,
            },
          },
        },
        series: [
          {
            name: "水库数量（座）",
            type: "bar",
            barWidth: 10,
            itemStyle: {
              color: {
                type: "linear",
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: "#22dffa", // 0% 处的颜色
                  },
                  {
                    offset: 1,
                    color: "#183b89", // 100% 处的颜色
                  },
                ],
                global: false, // 缺省为 false
              },
            },
            data: this.chartData.yData,
          },
        ],
      };

      if (this.chartData.noWords) {
        delete option.series[0].label;
      }
      myChart.getZr().off("click");
      myChart.setOption(option);
      myChart.getZr().on("click", (params) => {
        let pointInPixel = [params.offsetX, params.offsetY];
        let zIndex = 0;
        if (myChart.containPixel("grid", pointInPixel)) {
          zIndex = myChart.convertFromPixel({ seriesIndex: 0 }, [
            params.offsetX,
            params.offsetY,
          ])[0];
        }
        if (this.chartIndex !== zIndex) {
          this.chartIndex = zIndex;
          this.chartData.yData.forEach((item, index) => {
            if (item.itemStyle) delete item.itemStyle;
            if (index !== zIndex) {
              item["itemStyle"] = {
                color: "#ccc",
              };
            }
          });
          this.chartData.xData.forEach((item) => {
            if (item.textStyle) delete item.textStyle;
          });
          this.chartData.xData[zIndex] = {
            value:
              this.chartData.xData[zIndex].value ||
              this.chartData.xData[zIndex],
            textStyle: {
              color: "#ffffff",
            },
          };
          const activeAdcd = this.chartData.adcdList[this.chartIndex];
          myChart.setOption(option);
          this.$store.commit("setReservoirScale", "水库数量");
          this.getMapPointList(activeAdcd);
        }
      });
    },
    //设置水库不同状态的点位图标
    setPointIcon(data) {
      if (data.waterLevel > data.DSFLZ) {
        return require(`@/assets/images/map_points/reservoir-lv${data.project_scale}-red.png`);
      } else if (data.waterLevel > data.limitWaterLevel) {
        return require(`@/assets/images/map_points/reservoir-lv${data.project_scale}-orange.png`);
      } else {
        return require(`@/assets/images/map_points/reservoir-lv${data.project_scale}-green.png`);
      }
    },
    //获取水库点位
    async getMapPointList(activeAdcd) {
      let res = await reservoirLeftStatisticApi({
        statistic: "mapList",
        adcd: activeAdcd,
        itemValue: this.projectScale,
      });
      if (res.success) {
        this.pointsList = res.data.map((item) => {
          return {
            longitude: item.LGTD,
            latitude: item.LTTD,
            styleObj: {
              src: require(`@/assets/images/map_points/reservoir-lv${item.project_scale}-green.png`),
              scale: 0.7,
            },
            data: item,
          };
        });
        this.$olMapUtil.drawPoint(this.map, this.pointsList, "水库数量落点");
      }
    },
    //移除图层
    removeDangerPoint() {
      this.$olMapUtil.removeLayer(this.map, "水库数量落点");
    },
  },
};
</script>
<style lang='scss' scoped>
.reservoir-number {
  width: 415px;
  height: 100%;
  > div {
    width: 100%;
    height: 100%;
  }
}
.extra-clear {
  display: inline-block;
  padding: 3px 6px;
  background: #19549b;
  border-radius: 3px;
  margin-right: 45px;
  cursor: pointer;
}
</style>