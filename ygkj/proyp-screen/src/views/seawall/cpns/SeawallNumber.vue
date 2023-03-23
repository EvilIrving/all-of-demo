<!--
 * @Author: wqn
 * @Date: 2023-02-14 11:42:01
 * @LastEditors: dtb
 * @LastEditTime: 2023-02-17 17:22:22
 * @Description: 海塘数量
-->
<template>
  <ModuleLayout title="海塘数量" unit="条" disabled>
    <div class='seawall-number'>
      <single-date-bar-chart v-loading="htLoading" class="leftChart" :chartData="htData" @barClick="barClickAdcd" />
    </div>
    <div class="point-popup" ref="seawall-popup" v-show="clickFeatVal && clickFeatVal.layerName === '海塘数量'">
      <seawall-popup v-if="clickFeatVal && clickFeatVal.layerName === '海塘数量'" :map='map' :popupData="clickFeatVal?.data ?? {}" />
    </div>
    <MapLegend v-show="selectModuleName === '海塘数量'">
      <!-- <el-checkbox-group class="map-legend-checkbox" v-model="checkList" @change="changeCheckList"> -->
      <el-checkbox-group class="map-legend-checkbox" v-model="checkList">
        <el-checkbox :label="1"><span>1级海塘</span></el-checkbox>
        <el-checkbox :label="2"><span>2级海塘</span></el-checkbox>
        <el-checkbox :label="3"><span>2级海塘</span></el-checkbox>
        <el-checkbox :label="4"><span>4级海塘</span></el-checkbox>
        <el-checkbox :label="5"><span>非标海塘</span></el-checkbox>
      </el-checkbox-group>
    </MapLegend>
  </ModuleLayout>

</template>

<script>

import SeawallPopup from "../popups/SeawallPopup.vue";
import { seawallLeftStatisticApi, zhSeawall } from "api/api_seawall";
import SingleDateBarChart from "../components/SingleDateBarChart.vue";
export default {
  name: "SeawallNumber",
  props: {
    map: {
      requird: true,
      type: Object,
      default: null,
    },
  },
  components: {
    SingleDateBarChart,
    SeawallPopup,

  },
  data() {
    return {
      htData: {
        title: "海塘数量",
        id: "htChart",
        xData: [],
        yData: [],
        adcdList: [],
        allowClick: true,
        isReset: false,
        unit: "条",
      },
      htLoading: true,
      checkAdcd: '',
      pointsArr: [],
      linesArr: [],
      checkList: [1],
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
      this.gethtData();
      this.getSeawallList();
      setTimeout(() => {
        this.changeCheckList()
      }, 2000);
    },
    checkList() {
      this.changeCheckList()
    },
    selectModuleName(val) {
      if (val === '海塘数量') {
        console.log('-----');
      } else {
        this.$olMapUtil.removeLayer(this.map, "海塘数量");
        this.$olMapUtil.removeLayer(this.map, "海塘岸线");
      }
    },
    clickFeatVal(value) {
      if (value && value.layerName && value.layerName === "海塘数量") {
        this.$olMapUtil.drawHoverFrame(
          this.map,
          value.data || {},
          this.$refs["seawall-popup"],
          72,
          "海塘弹窗"
        );
      } else {
        this.$olMapUtil.removeHoverFrame(this.map, '海塘弹窗');
      }
    },
  },
  created() { },
  async mounted() {
    await this.getSeawallList();
    await this.changeCheckList()
    await this.gethtData();
    this.$store.commit('setModuleName', '海塘数量')
  },
  destroyed() { },
  methods: {
    // 海塘分布
    async gethtData() {
      let xData = [];
      let yData = [];
      let adcdList = [];
      let res = await seawallLeftStatisticApi({
        statistic: "adcd",
        adcd: this.selectAdcds,
      });
      if (res.success && res.data.length) {
        for (let i = 0; i < res.data.length; i++) {
          if (res.data[i].adnm.length > 2) {
            xData.push(
              res.data[i].adnm.substring(0, res.data[i].adnm.length - 1)
            );
          } else {
            xData.push(res.data[i].adnm);
          }
          yData.push(res.data[i].num);
          adcdList.push(res.data[i].adcd);
        }
        this.htData.isReset = !this.selectAdcds.endsWith("0000000000");
        this.htData.xData = xData;
        this.htData.yData = yData;
        this.htData.adcdList = adcdList;
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.htLoading = false;
    },
    async getSeawallList() {
      let res = await zhSeawall.seawallProIndo({
        adcd: this.selectAdcds,
      });
      let pointArr = [];
      let lineArr = [];
      if (res.success && res.data.length) {
        pointArr = res.data.map((v) => {
          let srcs = require("images/seawall-jh.png");
          let src = require("images/seawall-bjh.png");
          let lineColor = null
          let mapLegendFilterFlag = null
          switch (v.project_scale) {
            case '1':
              lineColor = "#19b1ff";
              mapLegendFilterFlag = 1;
              break;
            case '2':
              lineColor = "#ffcd19";
              mapLegendFilterFlag = 2
              break;
            case '3':
              lineColor = "#00f5ff";
              mapLegendFilterFlag = 3;
              break;
            case '4':
              lineColor = "#07dda3";
              mapLegendFilterFlag = 4;
              break;
            case '5':
              lineColor = "#ff9019";
              mapLegendFilterFlag = 5;
              break;
          }

          lineArr.push({
            line: v.COORPOT
              ? JSON.parse(
                v.COORPOT.replace(new RegExp("&quot;", "gm"), '"')
              )[0]
              : "",
            level: v.project_scale,
            lineColor,
            mapLegendFilterFlag,
          })
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
            ...v
          };
        });

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
        this.pointsArr = pointArr
        this.linesArr = lineArr
        // this.$olMapUtil.drawLines(this.map, lineArr, "海塘岸线");
        // this.$olMapUtil.drawClusterPoint(this.map, pointArr, "海塘数量");
        console.log(new Date())
      }
    },

    // 海塘柱状图点击事件
    barClickAdcd(index, reset = false) {
      if (reset) {
        this.checkAdcd = this.$localData("get", "userInfo").adcd
        this.$store.commit('setSelectAdcd', '330000000000')
      } else {
        this.checkAdcd = this.htData.adcdList[index]
      }
    },
    /**
     * @description 图例筛选
     */
    changeCheckList() {
      this.$nextTick(() => {
        this.$olMapUtil.drawLines(this.map, this.linesArr.filter((e) =>
          this.checkList.includes(e.mapLegendFilterFlag)
        ), "海塘岸线")
        this.$olMapUtil.drawClusterPoint(
          this.map,
          this.pointsArr.filter(e =>
            this.checkList.includes(e.mapLegendFilterFlag)
          ),
          "海塘数量"
        )
      })
    },
  },
};
</script>
<style lang='scss' scoped>
.seawall-number {
  width: 96%;
  margin: 0 auto;
  height: 100%;
}
.point-popup {
  position: relative;
  z-index: 99;
}
</style>