<!--
 * @Author: wqn
 * @Date: 2023-02-14 11:42:01
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-20 16:15:15
 * @Description: 海塘等级
-->
<template>
  <ModuleLayout title="海塘等级" disabled>
    <div class='seawall-level'>
      <div :id="id" style="height: 100%; width: 100%"></div>
      <img class="base" :src="$config.ypImgUrl+ 'sealevel-base.png'" alt="base">
      <ul class="legend">
        <li v-for="(item,index) in dataList" :key="'海塘等级' + index" @click="checkIt(index)">
          <img v-show="selectModuleName === '海塘等级' + index && checked === index" class="selected rotate" :src="$config.imgUrl + 'arr.png'" alt="" />
          <i :class="item.colorClass"></i>
          <span :class="selectModuleName === '海塘等级' + index && checked===index?'check':''">{{item.name}}</span>
          <span :class="item.colorClass">{{item.y}}</span>个
        </li>
      </ul>
    </div>

    <div class="point-popup" ref="seawall-popup" v-show="clickFeatVal && clickFeatVal.layerName === '海塘等级'">
      <seawall-popup v-if="clickFeatVal && clickFeatVal.layerName === '海塘等级'" :map='map' :popupData="clickFeatVal?.data ?? {}" />
    </div>

    <MapLegend v-show="selectModuleName === '海塘等级'+checked">
      <el-checkbox-group class="map-legend-checkbox" v-model="checkList" @change="changeCheckList">
        <el-checkbox :label="1"><img src="@/assets/images/map_points/seawall-lv1-bjh.png" /><span>1级海塘</span></el-checkbox>
        <el-checkbox :label="2"><img src="@/assets/images/map_points/seawall-lv2-bjh.png" /><span>2级海塘</span></el-checkbox>
        <el-checkbox :label="3"><img src="@/assets/images/map_points/seawall-lv3-bjh.png" /><span>2级海塘</span></el-checkbox>
        <el-checkbox :label="4"><img src="@/assets/images/map_points/seawall-lv4-bjh.png" /><span>4级海塘</span></el-checkbox>
        <el-checkbox :label="5"><img src="@/assets/images/map_points/seawall-lv5-bjh.png" /><span>非标海塘</span></el-checkbox>
      </el-checkbox-group>
    </MapLegend>
  </ModuleLayout>
</template>

<script>
import SeawallPopup from "../popups/SeawallPopup.vue";
import highcharts from "highcharts";
import { fontChart } from "utils/utils";
import { zhSeawall } from "api/api_seawall";
export default {
  name: "SeawallLevel",
  props: {
    map: {
      requird: true,
      type: Object,
      default: null,
    },
  },
  components: {
    SeawallPopup,
  },
  data() {
    return {
      checked: null,
      id: "demo3d",
      dataList: [
        {
          name: "1级海塘",
          y: 0,
          colorClass: "blue",
          h: 0,
          bfb: 0,
        },
        {
          name: "2级海塘",
          y: 0,
          colorClass: "yellow",
          h: 0,
          bfb: 0,
        },
        {
          name: "3级海塘",
          y: 0,
          h: 0,
          colorClass: "babyblue",
          bfb: 0,
        },
        {
          name: "4级海塘",
          y: 0,
          h: 0,
          colorClass: "green",
          bfb: 0,
        },
        {
          name: "非标海塘",
          y: 0,
          h: 0,
          colorClass: "orange",
          bfb: 0,
        },
      ],
      pointsArr: [],
      linesArr: [],
      checkList: [],
    };
  },
  computed: {
    selectAdcds() {
      return this.checkAdcd || this.$store.state.selectAdcd;
    },
    selectModuleName() {
      return this.$store.state.selectModuleName;
    },
    clickFeatVal() {
      return this.$store.state.map.clickFeatVal;
    },
  },
  watch: {
    selectAdcds() {
      this.getLevelData();
      this.getSeawallList();
    },
    selectModuleName(val) {
      if (val === "海塘等级" + this.checked) {
        this.checkList = [this.checked + 1];
        this.changeCheckList();
      } else {
        this.$olMapUtil.removeLayer(this.map, "海塘等级");
        this.$olMapUtil.removeLayer(this.map, "海塘等级岸线");
      }
    },
    clickFeatVal(value) {
      if (value && value.layerName === "海塘等级") {
        this.$olMapUtil.drawHoverFrame(
          this.map,
          value.data || {},
          this.$refs["seawall-popup"],
          72,
          "海塘等级"
        );
      } else {
        this.$olMapUtil.removeHoverFrame(this.map, "海塘等级");
      }
    },
  },
  created() {},
  mounted() {
    this.getSeawallList();
    this.getLevelData();
    window.addEventListener("resize", this.initOption);
  },
  destroyed() {
    window.removeEventListener("resize", this.initOption);
  },
  methods: {
    checkIt(index) {
      this.checked = index;
      this.$store.commit("setModuleName", "海塘等级" + index);
    },
    async getLevelData() {
      this.dataList = [];
      let res = await zhSeawall.seawallAline({
        adcd: this.selectAdcds,
        extractPram: "一线海塘",
      });
      if (res.success && res.rows.length) {
        let testArr = ["1", "2", "3", "4", "5"];
        let arr = [];
        for (let i = 0; i < res.rows.length; i++) {
          let sacle = "";
          arr.push(res.rows[i].project_scale);
          switch (res.rows[i].project_scale) {
            case "1":
              sacle = "1级";
              break;
            case "2":
              sacle = "2级";
              break;
            case "3":
              sacle = "3级";
              break;
            case "4":
              sacle = "4级";
              break;
            default:
              sacle = "非标";
              break;
          }
          let htName = sacle + "海塘";
          if (res.rows[i].project_scale != 6)
            this.dataList.push({
              name: htName,
              y: res.rows[i].count,
              h: 0,
              colorClass: this.getColor(res.rows[i].project_scale),
              bfb: 0,
              level: res.rows[i].project_scale,
            });
        }
        testArr.filter(
          (item) => !arr.includes(item) && this.dataList.splice(item - 1, 1)
        );
        this.initOption();
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
    },
    getColor(project_scale) {
      let colorClass = "";
      switch (project_scale) {
        case "1":
          colorClass = "blue";
          break;
        case "2":
          colorClass = "yellow";
          break;
        case "3":
          colorClass = "babyblue";
          break;
        case "4":
          colorClass = "green";
          break;
        default:
          colorClass = "orange";
          break;
      }
      return colorClass;
    },
    async getSeawallList() {
      let opt = {
        adcd: this.selectAdcds,
        dam: 16, // ；类型
        projectScale: "1,2,3,4,5" || this.checkList.toString(),
      };
      let res = await zhSeawall.seawallProIndo(opt);
      let pointArr = [];
      let lineArr = [];
      if (res.success && res.data.length) {
        pointArr = res.data.map((v) => {
          let srcs = null;
          let src = null;
          let lineColor = null;
          let mapLegendFilterFlag = null;
          switch (v.project_scale) {
            case "1":
              srcs = require("images/map_points/seawall-lv1-jh.png");
              src = require("images/map_points/seawall-lv1-bujh.png");
              lineColor = "#19b1ff";
              mapLegendFilterFlag = 1;
              break;
            case "2":
              srcs = require("images/map_points/seawall-lv2-jh.png");
              src = require("images/map_points/seawall-lv2-bujh.png");
              lineColor = "#ffcd19";
              mapLegendFilterFlag = 2;
              break;
            case "3":
              srcs = require("images/map_points/seawall-lv3-jh.png");
              src = require("images/map_points/seawall-lv3-bujh.png");
              lineColor = "#00f5ff";
              mapLegendFilterFlag = 3;

              break;
            case "4":
              srcs = require("images/map_points/seawall-lv4-jh.png");
              src = require("images/map_points/seawall-lv4-bujh.png");
              lineColor = "#07dda3";
              mapLegendFilterFlag = 4;
              break;
            case "5":
              srcs = require("images/map_points/seawall-lv5-jh.png");
              src = require("images/map_points/seawall-lv5-bujh.png");
              lineColor = "#ff9019";
              mapLegendFilterFlag = 5;
              break;
          }
          lineArr.push({
            line: v.coorpot
              ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
              : "",
            level: v.project_scale,
            lineColor,
            mapLegendFilterFlag,
          });
          return {
            latitude: v.lttd,
            longitude: v.lgtd,
            name: v.name,
            prcd: v.prcd,
            level: v.project_scale,
            mapLegendFilterFlag,
            styleObj: {
              src: src,
              srcs: srcs,
              scale: 0.5,
              color: "#fff",
              font: "normal 16px 微软雅黑",
              textOffsetX: 16,
              textOffsetY: 0,
            },
          };
        });
      }
      for (let i = 0; i < lineArr.length; i++) {
        lineArr[i].coordinates = [];
        if (lineArr[i].line && lineArr[i].line.length) {
          for (let j = 0; j < lineArr[i].line.length; j++) {
            lineArr[i].coordinates.push([
              lineArr[i].line[j].lng,
              lineArr[i].line[j].lat,
            ]);
          }
        }
      }

      this.pointsArr = pointArr;
      this.linesArr = lineArr;
      // this.$olMapUtil.drawLines(this.map, this.linesArr, "海塘等级岸线");
      // this.$olMapUtil.drawClusterPoint(this.map, this.pointsArr, "海塘等级");
    },
    // 初始化3d图
    initOption() {
      this.dataList.forEach((item) => {
        item.bfb = parseInt(15);
        item.h = item.bfb * 1.5 >= 70 ? 70 : item.bfb * 1.5;
      });
      // 修改3d饼图绘制过程
      var each = highcharts.each,
        round = Math.round,
        cos = Math.cos,
        sin = Math.sin,
        deg2rad = Math.deg2rad;
      highcharts.wrap(
        highcharts.seriesTypes.pie.prototype,
        "translate",
        function (proceed) {
          proceed.apply(this, [].slice.call(arguments, 1));
          // Do not do this if the chart is not 3D
          if (!this.chart.is3d()) return;
          var series = this,
            chart = series.chart,
            options = chart.options,
            seriesOptions = series.options,
            depth = seriesOptions.depth || 0,
            options3d = options.chart.options3d,
            alpha = options3d.alpha,
            beta = options3d.beta,
            z = seriesOptions.stacking
              ? (seriesOptions.stack || 0) * depth
              : series._i * depth;
          z += depth / 2;
          if (seriesOptions.grouping !== false) {
            z = 0;
          }
          each(series.data, function (point) {
            var shapeArgs = point.shapeArgs,
              angle;
            point.shapeType = "arc3d";
            var ran = point.options.h;
            shapeArgs.z = z;
            shapeArgs.depth = depth * 0.75 + ran;
            shapeArgs.alpha = alpha;
            shapeArgs.beta = beta;
            shapeArgs.center = series.center;
            shapeArgs.ran = ran;
            angle = (shapeArgs.end + shapeArgs.start) / 2;
            point.slicedTranslation = {
              translateX: round(
                cos(angle) * seriesOptions.slicedOffset * cos(alpha * deg2rad)
              ),
              translateY: round(
                sin(angle) * seriesOptions.slicedOffset * cos(alpha * deg2rad)
              ),
            };
          });
        }
      );
      (function (H) {
        H.wrap(
          highcharts.SVGRenderer.prototype,
          "arc3dPath",
          function (proceed) {
            // Run original proceed method
            var ret = proceed.apply(this, [].slice.call(arguments, 1));
            ret.zTop = (ret.zOut + 0.5) / 100;
            return ret;
          }
        );
      })(highcharts);
      highcharts.chart(this.id, {
        chart: {
          animation: false,
          backgroundColor: "none",
          type: "pie", //饼图
          margin: [0, 0, 0, 0],
          options3d: {
            enabled: true, //使用3d功能
            alpha: 70, //延y轴向内的倾斜角度
            beta: 0,
            depth: 50,
          },
          events: {
            load: function () {
              var each = highcharts.each,
                points = this.series[0].points;
              each(points, function (p) {
                p.graphic.attr({
                  translateY: -p.shapeArgs.ran,
                });
                p.graphic.side1.attr({
                  translateY: -p.shapeArgs.ran,
                });
                p.graphic.side2.attr({
                  translateY: -p.shapeArgs.ran,
                });
              });
            },
            redraw: function () {
              var each = highcharts.each,
                points = this.series[0].points;
              each(points, function (p) {
                p.graphic.attr({
                  translateY: -p.shapeArgs.ran,
                });
                p.graphic.side1.attr({
                  translateY: -p.shapeArgs.ran,
                });
                p.graphic.side2.attr({
                  translateY: -p.shapeArgs.ran,
                });
              });
            },
          },
        },
        legend: {
          enabled: false, // 关闭图例
        },
        title: {
          text: null,
        },
        subtitle: {
          text: "",
        },
        plotOptions: {
          pie: {
            allowPointSelect: false, // 禁用点击
            cursor: "pointer",
            depth: fontChart(20),
            showInLegend: true,
            size: "100%", // 外圈直径大小
            innerSize: fontChart(70), // 内圈直径大小
            center: ["25%", "60%"],
            colors: ["#19b1ff", "#ffcd19", "#00f5ff", "#07dda3", "#ff9019"],
            dataLabels: {
              useHTML: false,
              enabled: false, //是否显示饼图的线形tip
              distance: -10,
              borderColor: "#007acc",
              align: "center",
              // verticalAlign: 'top',
              position: "right",
              format: "{point.bfb}%",
              // formatter: (point,b) => {
              //   console.log(point,'ponit-->>')
              //   console.log(b,'ponit-->>')
              // },
              color: "#ffffff",
              style: {
                textOutline: "none",
                fontSize: fontChart(13),
              },
            },
          },
        },
        credits: {
          enabled: false, // 禁用版权信息
        },
        series: [
          {
            type: "pie",
            name: "数量",
            data: this.dataList,
          },
        ],
      });
    },
    /**
     * @description 图例筛选
     */
    changeCheckList() {
      this.$olMapUtil.removeLayer(this.map, "海塘等级岸线");
      this.$olMapUtil.removeLayer(this.map, "海塘等级");
      setTimeout(() => {
        this.$olMapUtil.drawLines(
          this.map,
          this.linesArr.filter((e) =>
            this.checkList.includes(e.mapLegendFilterFlag)
          ),
          "海塘等级岸线"
        );
        this.$olMapUtil.drawClusterPoint(
          this.map,
          this.pointsArr.filter((e) =>
            this.checkList.includes(e.mapLegendFilterFlag)
          ),
          "海塘等级"
        );
      }, 0);
    },
  },
};
</script>
<style lang='scss' scoped>
.seawall-level {
  width: 96%;
  margin: 0 auto;
  height: 100%;
  position: relative;
  img.base {
    display: block;
    position: absolute;
    width: 50%;
    height: 50%;
    top: 50%;
    left: 2%;
  }
  ul.legend {
    position: absolute;
    top: 10%;
    right: 5%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: start;
    li {
      padding: 4px 2px;
      cursor: pointer;
      position: relative;
      i {
        display: inline-block;
        width: 10px;
        height: 10px;
        border-radius: 50%;
        margin-right: 8px;
        &.blue {
          background-color: #19b1ff;
        }
        &.yellow {
          background-color: #ffcd19;
        }
        &.babyblue {
          background-color: #00f5ff;
        }
        &.green {
          background-color: #07dda3;
        }
        &.orange {
          background-color: #ff9019;
        }
      }
      .selected {
        position: absolute;
        left: -26px;
        top: 9px;
        width: 20px;
        height: 10px;
        margin-right: 40px;
      }
      .rotate {
        transform: rotate(180deg);
      }
      span {
        color: #d9f0fe;
        font-size: 16px;
        &.blue {
          color: #19b1ff;
          padding: 16px;
        }
        &.yellow {
          color: #ffcd19;
          padding: 16px;
        }
        &.babyblue {
          color: #00f5ff;
          padding: 16px;
        }
        &.green {
          color: #07dda3;
          padding: 16px;
        }
        &.orange {
          color: #ff9019;
          padding: 16px;
        }
        &.check {
          color: #00f5ff;
        }
      }
    }
  }
}
</style>