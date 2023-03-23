<!--
 * @Date: 2022-10-12 15:05:03
 * @Author: 
 * @Description: 闭环管控
 * @LastEditors: dtb
 * @LastEditTime: 2023-02-16 09:55:04
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\seawallScreenLegend\components\MoistureAnalysis.vue
-->

<template>
  <div class="wrap">
    <el-checkbox-group v-model="checkedScale" @change="getSeawallList">
      <el-checkbox v-for="item in skStatus" :label="item.scale" :key="item.scale">
        <p class="legend_label">
          <img :src="item.icon" alt="" />
          <span>{{ item.scale }}</span>
          <i v-show="checkedScale.includes(item.scale)">({{ item.num }})</i>
        </p>
      </el-checkbox>
    </el-checkbox-group>
  </div>
</template>

<script>
import { seawallLeftStatisticApi } from "@/api/api_seawall";
export default {
  name: "SecondLine",
  props: {},
  components: {},
  computed: {
    planId() {
      return this.$store.state.planId;
    },
    leftSelectAdcd() {
      return (
        this.$store.state.leftSelectAdcd ||
        this.$localData("get", "userInfo").adcd
      );
    },
  },
  watch: {
    planId(val) {
      if (val) {
        this.getSeawallList();
      }
    },
  },
  data() {
    return {
      checkedScale: ["超设计潮位", "超病险塘能力", "超塘顶高程"],
      skStatus: [
        {
          checked: true,
          icon: this.$config.zhImgUrl + "climaxRisk-nw.png",
          scale: "超设计潮位",
          num: 0,
          type: "overCnt",
        },
        {
          checked: true,
          icon: this.$config.zhImgUrl + "kouMenRisk-nw.png",
          scale: "超病险塘能力",
          num: 0,
          type: "sickCnt",
        },
        {
          checked: true,
          icon: this.$config.zhImgUrl + "shigong_risk-nw.png",
          scale: "超塘顶高程",
          num: 0,
          type: "overTop",
        },
      ],
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: false,
    };
  },
  mounted() {
    this.getSeawallList();
  },
  methods: {
    // 落点
    getSeawallList() {
      this.$parent.$parent.$refs.seawallMap.removeAllLayer();
      this.checkedScale.forEach(async (element) => {
        const skStatusData = this.skStatus.find(
          (item) => item.scale === element
        );
        let opt = (opt = {
          statistic: "mapList",
          itemCategory: "forecast,planId",
          itemValue: skStatusData.type + "," + this.planId,
          adcd: this.leftSelectAdcd,
        });
        let res = await seawallLeftStatisticApi(opt);
        this.tableLoading = false;
        if (res.success) {
          let pointArr = [];
          let lineArr = [];
          skStatusData.num = res.data.length;
          if (res.data.length) {
            res.data.map((v, i) => {
              let colorType = "";
              if (v.forecast > v.TIDE_ALERT_BLUE) {
                if (
                  v.forecast < v.TIDE_ALERT_YELLOW ||
                  v.forecast == v.TIDE_ALERT_YELLOW
                ) {
                  colorType = "blue";
                } else if (
                  v.forecast < v.TIDE_ALERT_ORANGE ||
                  v.forecast == v.TIDE_ALERT_ORANGE
                ) {
                  colorType = "yellow";
                } else if (
                  v.forecast < v.TIDE_ALERT_RED ||
                  v.forecast == v.TIDE_ALERT_RED
                ) {
                  colorType = "orange";
                } else {
                  colorType = "red";
                }
              } else {
                colorType = "default";
              }
              pointArr.push({
                lat: v.LTTD,
                lng: v.LGTD,
                NAME: v.NAME,
                PRCD: v.PRCD,
                rightType: "3",
                forecast_time: v.forecast_time,
                colorType: colorType,
                forecast: v.forecast,
                status: v.status,
                cityadnm: v.cityadnm,
                countryadnm: v.countryadnm,
                DESIGN_TIDE_STANDARD: v.DESIGN_TIDE_STANDARD,
                DESIGN_TIDE_LEVEL: v.DESIGN_TIDE_LEVEL,
                LOWEST_ELEVATION: v.LOWEST_ELEVATION,
                src: require("@/assets/images/map/gao_risk.png"),
              });
              lineArr.push({
                line: v.COORPOT
                  ? JSON.parse(
                    v.COORPOT.replace(new RegExp("&quot;", "gm"), '"')
                  )[0]
                  : "",
                type: 'seawallPopup',
                level: v.project_scale,
              });
              v.lineArr = [];
              v.lineArr.push({
                line: v.COORPOT
                  ? JSON.parse(
                    v.COORPOT.replace(new RegExp("&quot;", "gm"), '"')
                  )[0]
                  : "",
                type: 'seawallPopup',
                level: v.project_scale,
              });
            });
          }
          let styleObj = {};
          switch (skStatusData.type) {
            case "overCnt":
              styleObj[
                "jhImg"
              ] = require("@/assets/images/map/gao_risk_jh.png");
              styleObj[
                "img"
              ] = require("@/assets/images/map/gao_risk.png");
              this.$parent.$parent.showMapLines(lineArr);
              break;
            case "sickCnt":
              styleObj[
                "jhImg"
              ] = require("@/assets/images/map/koumen_risk_js.png");
              styleObj[
                "img"
              ] = require("@/assets/images/map/koumen_risk.png");
              break;
            case "overTop":
              styleObj[
                "jhImg"
              ] = require("@/assets/images/map/shigong_risk_jh.png");
              styleObj["img"] = require("@/assets/images/map/shigong_risk.png");
              break;
          }
          this.$parent.$parent.showMapPoints(pointArr, skStatusData.type, styleObj);
        }
      });
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  .el-radio {
    width: 100%;
    height: 30px;
    @include flexbox();
    @include flexAI(center);
    margin-left: 5px;
    .el-radio__label {
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);
    }
    .text {
      margin-left: 5px;
      color: #fff;
    }
    img {
      width: 20px;
    }
  }
  .line {
    width: 90%;
    margin: 5px;
    height: 2px;
    background: #979797;
  }
}
</style>
<style>
.el-checkbox-group {
  margin-left: 15px;
}
.el-radio-group {
  margin-left: 10px;
}
</style>