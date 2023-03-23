<!--
 * @Author: hanyu
 * @LastEditTime: 2023-01-06 10:56:39
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\seawallScreenLegend\components\SafeAppraisal.vue
-->
<template>
  <div class='wrap'>
    <el-radio-group v-model="checkedScale" @change="changeCheckedScale">
      <el-radio :disabled='apiOnce' v-for="item in skScale" :label="item.scale" :key="item.scale" :checked="item.checked">
        <p class="legend_label_radio">
          <!-- <img :src="item.icon" alt=""> -->
        <section class="color_block" :style="{'background-color':item.color}"></section>
        <span>{{item.scale}}</span>
        <i v-show="checkedScale == item.scale">({{num}})</i>
        </p>
      </el-radio>
    </el-radio-group>
  </div>
</template>

<script>
import { seawallProIndo } from "@/api/api_seawall";
export default {
  name: "SafeAppraisal",
  props: {
    defultSelected: {
      type: String,
      default: "",
    },
  },
  components: {},
  data() {
    return {
      // 一类塘、二类塘、三类塘、一年度到期
      checkedScale: "",
      skScale: [
        {
          icon: this.$config.imgUrl + "aqjd-3.png",
          scale: "一类塘",
          num: 0,
          dam: 1,
          color: "#1BC57E",
        },
        {
          icon: this.$config.imgUrl + "aqjd-2.png",
          scale: "二类塘",
          num: 0,
          dam: 2,
          color: "#FFA01E",
        },
        {
          icon: this.$config.imgUrl + "aqjd-1.png",
          scale: "三类塘",
          num: 0,
          dam: 3,
          color: "#FA4444",
        },
        {
          icon: this.$config.imgUrl + "aqjd-1.png",
          scale: "一年内到期",
          dam: 4,
          num: 0,
          color: "#3886FF",
        },
      ],
      num: 0,
      pointArr: {
        一类塘: [],
        二类塘: [],
        三类塘: [],
        一年内到期: [],
      },
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: true,
    };
  },
  computed: {
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd;
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
    changeScale(value) {
      this.showPoints();
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
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        dam: damData.dam,
      };

      this.$parent.$parent.$refs.seawallMap.removeAllLayer();
      let res = await seawallProIndo(opt);
      if (res.success) {
        this.apiOnce = false;
        let pointArr = [];
        let lineArr = [];
        this.num = res.data.length;
        if (res.data.length) {
          res.data.map((v) => {
            pointArr.push({
              lat: v.lttd,
              lng: v.lgtd,
              mapType: "aqjdPopup",
              ...v,
            });
            lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              type: 'seawallPopup',
              level: v.projectScale,
            });
            v.lineArr = [];
            v.lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
                : "",
              type: 'seawallPopup',
              level: v.projectScale,
            });
          });
        }
        let styleObj = {};
        switch (this.checkedScale) {
          case "一类塘":
            styleObj["jhImg"] = require("@/assets/images/map/seawall-green-group.png");
            styleObj["img"] = require("@/assets/images/map/seawall-green.png");
            break;
          case "二类塘":
            styleObj["jhImg"] = require("@/assets/images/map/seawall-yellow-group.png");
            styleObj["img"] = require("@/assets/images/map/seawall-yellow.png");
            break;
          case "三类塘":
            styleObj["jhImg"] = require("@/assets/images/map/seawall-red-group.png");
            styleObj["img"] = require("@/assets/images/map/seawall-red.png");
            break;
          case "一年内到期":
            styleObj["jhImg"] = require("@/assets/images/map/yndq-jh.png");
            styleObj["img"] = require("@/assets/images/map/yndq.png");
            break;
        }
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