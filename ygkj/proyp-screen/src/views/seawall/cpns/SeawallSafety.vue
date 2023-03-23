<!--
 * @Author: wqn
 * @Date: 2023-02-14 11:42:01
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-20 16:27:04
 * @Description: 海塘安全鉴定
-->
<template>
  <ModuleLayout title="安全鉴定" disabled>
    <div class='seawall-safety'>
      <section @click='clickItem("2")' :class="{active:selectModuleName =='安全鉴定-2'}">
        <p class="ttitle">{{ safeData.eltNum }}<span class="unit">条</span></p>
        <div> <img :src="$config.ypImgUrl + 'safe_base.png'" alt=""> </div>
        <p>二类塘</p>
      </section>
      <section @click='clickItem("3")' :class="{active:selectModuleName =='安全鉴定-3'}">
        <p class="ttitle">{{ safeData.sltNum }}<span class="unit">条</span></p>
        <div> <img :src="$config.ypImgUrl + 'safe_base.png'" alt=""> </div>
        <p>三类塘</p>
      </section>
    </div>
    <div class="point-popup" ref="seawall-popup" v-show="clickFeatVal && clickFeatVal.layerName === '安全鉴定'">
      <seawall-popup v-if="clickFeatVal && clickFeatVal.layerName === '安全鉴定'" :map='map' :popupData="clickFeatVal?.data ?? {}" />
    </div>
    <MapLegend v-show="selectModuleName.includes('安全鉴定')">
      <el-radio-group class="map-legend-radio" v-model="checkValue">
        <el-radio :label="2"><img src="@/assets/images/safe-2.png" /><span>二类塘</span></el-radio>
        <el-radio :label="3"><img src="@/assets/images/safe-3.png" /><span>三类塘</span></el-radio>
      </el-radio-group>
    </MapLegend>
  </ModuleLayout>
</template>

<script>
import SeawallPopup from "../popups/SeawallPopup.vue";
import { zhSeawall } from "@/api/api_seawall";
export default {
  name: "SeawallSafety",
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
      safeData: {},
      pointsArr: [],
      linesArr: [],
      checkValue: "",
    };
  },
  computed: {
    selectAdcds() {
      return this.$store.state.selectAdcd;
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
      this.getSafeData();
    },
    checkValue(val) {
      if (val === 2 || val === 3) {
        this.$store.commit("setModuleName", "安全鉴定-" + val);
      }
    },
    selectModuleName(val) {
      if (val.includes("安全鉴定")) {
        this.loadSafeList(this.checkValue);
      } else {
        this.checkValue = "";
        this.$olMapUtil.removeLayer(this.map, "安全鉴定");
        this.$olMapUtil.removeLayer(this.map, "安全鉴定岸线");
      }
    },
  },
  created() {},
  mounted() {
    this.getSafeData();
  },
  destroyed() {},
  methods: {
    clickItem(item) {
      this.checkValue = Number(item);
    },
    // 安全鉴定
    async getSafeData() {
      let res = await zhSeawall.safeStaNum({
        interType: 1,
        adcd: this.selectAdcds,
      });
      if (res.success) {
        this.safeData = res.data;
      }
    },
    async loadSafeList(type) {
      let opt = {
        adcd: this.selectAdcds,
        dam: type, // 类型
      };
      let res = await zhSeawall.seawallProIndo(opt);
      let pointArr = [];
      let lineArr = [];
      if (res.success && res.data.length) {
        pointArr = res.data.map((v) => {
          let srcs = require(`images/safe-${type}-group.png`);
          let src = require(`images/safe-${type}.png`);
          let lineColor = null;
          let mapLegendFilterFlag = null;
          switch (type) {
            case 2:
              lineColor = "#ff9019";
              mapLegendFilterFlag = 2;
              break;
            case 3:
              lineColor = "rgb(250, 68, 68)";
              mapLegendFilterFlag = 3;
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
      this.$olMapUtil.drawLines(this.map, lineArr, "安全鉴定岸线");
      this.$olMapUtil.drawClusterPoint(this.map, pointArr, "安全鉴定");
    },
  },
};
</script>
<style lang='scss' scoped>
.seawall-safety {
  width: 100%;
  margin: 16px auto;
  height: 100%;
  @include flexbox();
  @include flexAC();
  @include flexJC(center);
  section {
    width: 121px;
    height: 153px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    margin: 0 30px;
    &.active {
      background: url("~@/assets/images/item-active-border-121_153.png")
        no-repeat center;
      background-size: cover;
    }
    p {
      font-size: 16px;
      &.ttitle {
        flex: 0.8;
        font-family: "agencyb";
        font-size: 26px;
        letter-spacing: 2px;
        height: 0px;
        span.unit {
          font-family: "fzzyjw";
          font-size: 18px;
          color: #b1dcff;
        }
      }
    }
  }
}
</style>