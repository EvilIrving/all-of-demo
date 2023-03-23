<!--
 * @Date: 2023-01-05 12:25:29
 * @Author: 
 * @Description: 附属建筑物
 * @LastEditors: dtb
 * @LastEditTime: 2023-01-06 11:14:43
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\seawallScreenLegend\components\Affiliate.vue
-->

<template>
  <div class='wrap'>
    <el-radio-group v-model="checkedScale" @change="changeCheckedScale">
      <el-radio :disabled='apiOnce' v-for="item in skScale" :label="item.scale" :key="item.scale" :checked="item.checked">
        <p class="legend_label_radio">
          <img :src="item.icon" alt="">
          <!-- <section class="color_block" :style="{'background-color':item.color}"></section> -->
          <span>{{item.scale}}</span>
          <i v-show="checkedScale == item.scale">({{num}})</i>
        </p>
      </el-radio>
    </el-radio-group>
  </div>
</template>

<script>
import { zhhtFacility } from "@/api/api_seawall";
export default {
  name: "Affiliate",
  props: {
    defultSelected: {
      type: String,
      default: "",
    },
  },
  components: {},
  data() {
    return {
      checkedScale: "大中型闸站",
      skScale: [
        // ["#FF0000", "#C447D6", " #00CFFF", "#00FD6D", "#EFC30A"],
        {
          icon: this.$config.imgUrl + "_DZsluice.png",
          scale: "大中型闸站",
          num: 0,
          dam: 15,
          color: "#1BC57E",
        },
        {
          icon: this.$config.imgUrl + "_sluice.png",
          scale: "水闸",
          num: 0,
          dam: 13,
          color: "#FFA01E",
        },
        {
          icon: this.$config.imgUrl + "_pump.png",
          scale: "泵站",
          num: 0,
          dam: 14,
          color: "#FA4444",
        },
        {
          icon: this.$config.imgUrl + "_damGate.png",
          scale: "闸站",
          dam: 16,
          num: 0,
          color: "#3886FF",
        },
      ],
      num: 0,
      pointArr: {
        大中型闸站: [],
        水闸: [],
        泵站: [],
        闸站: [],
      },
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: true,
    };
  },
  computed: {
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd || this.$localData("get", "userInfo").adcd;
    },
    sgzgSelectBasin() {
      return this.$store.state.sgzgSelectBasin;
    },
  },
  watch: {
    sgzgSelectAdcd(val) {
      this.getList();
    },
    sgzgSelectBasin(val) {
      this.getList();
    },
    defultSelected(val) {
      this.setSelect();
      this.getList();
    },
  },
  mounted() {
    if (this.defultSelected !== "") {
      this.setSelect();
    }
    this.getList();
  },
  methods: {
    setSelect() {
      this.checkedScale = this.defultSelected;
    },

    changeCheckedScale() {
      this.$store.commit("SET_NUMBER_SELECT", this.checkedScale);
      this.getList();
    },
    async getList() {
      this.num = 0;
      this.apiOnce = true;
      let damData = this.skScale.find(
        (item) => item.scale === this.checkedScale
      );
      let opt = {
        adcd: this.sgzgSelectAdcd,
        basinCode: this.sgzgSelectBasin,
        dam: damData.dam,
      };

      this.$parent.$parent.$refs.seawallMap.removeAllLayer();
      let res = await zhhtFacility(opt);
      if (res.success) {
        this.apiOnce = false;
        let pointArr = [];
        let lineArr = [];
        this.num = res.data.length;
        if (res.data.length) {
          res.data.map((v) => {
            pointArr.push({
              lat: + v.LTTD,
              lng: + v.LGTD,
              mapType: "affiliatePopup",
              ...v,
            });
          });
        }

        if (res.lines.length) {
          res.lines.map((v) => {
            lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              type: 'seawallPopup',
              level: v.project_scale,
            });
          })
        }
        let styleObj = {};

        switch (this.checkedScale) {
          case "大中型闸站":
            styleObj["jhImg"] = require("@/assets/images/map/_DZsluice-jh.png");
            styleObj["img"] = require("@/assets/images/map/_DZsluice.png");
            break;
          case "水闸":
            styleObj["jhImg"] = require("@/assets/images/map/_sluice-jh.png");
            styleObj["img"] = require("@/assets/images/map/_sluice.png");
            break;
          case "泵站":
            styleObj["jhImg"] = require("@/assets/images/map/_pump-jh.png");
            styleObj["img"] = require("@/assets/images/map/_pump.png");
            break;
          case "闸站":
            styleObj["jhImg"] = require("@/assets/images/map/_dryGate-jh.png");
            styleObj["img"] = require("@/assets/images/map/_dryGate.png");
            break;
        }
        this.$parent.$parent.$refs.seawallMap.removeAllLayer()
        this.$parent.$parent.$refs.seawallMap.newMapJh(
          pointArr,
          "seawallPopup",
          styleObj
        );
        this.$parent.$parent.$refs.seawallMap._drawLines(lineArr);
      }
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
    img {
      width: 15px;
      height: 15px;
    }
  }
}
</style>

<style lang='scss'  >
.wrap {
  .legend_label_radio {
    @include flexbox();
    @include flexAI(center);
    span {
      margin-left: 5px;
      color: #fff;
    }
  }
}
</style>