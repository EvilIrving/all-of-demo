<!--
 * @Date: 2022-01-13 14:16:12
 * @Author: dtb
 * @Description:除险加固图例
 * @LastEditors: dtb
 * @LastEditTime: 2023-01-06 10:56:14
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\seawallScreenLegend\components\Reinforce.vue
-->
<template>
  <div class='wrap'>
    <el-checkbox-group v-model="checkedStatus" @change="getList">
      <el-checkbox :disabled='apiOnce' v-for="item in skStatus" :label="item.scale" :key="item.scale">
        <p class="legend_label">
          <!-- <img :src="item.icon" alt=""> -->
        <section class="color_block" :style="{'background-color':item.color}"></section>
        <span>{{item.scale}}</span>
        <i v-show="checkedStatus.includes(item.scale)">({{pointArr[item.scale].length}})</i>
        </p>
      </el-checkbox>
    </el-checkbox-group>
    <div class="line"></div>
    <el-checkbox-group v-model="checkedScale" @change="getList">
      <el-checkbox :disabled='apiOnce' v-for="item in skScale" :label="item.scale" :key="item.scale">
        <p class="legend_label">
          <!-- <img :src="item.icon" alt=""> -->
          <span>{{item.scale}}</span>
          <i v-show="checkedScale.includes(item.scale)">({{pointArr[item.scale].length}})</i>
        </p>
      </el-checkbox>
    </el-checkbox-group>
  </div>
</template>

<script>
import { seawallProIndo } from "@/api/api_seawall";
export default {
  name: "KeyProblem",
  props: {
    defultSelected: {
      type: String,
      default: "",
    },
  },
  components: {},
  data() {
    return {
      checkedScale: [],
      skScale: [
        {
          scale: "二类塘",
          icon: this.$config.imgUrl + "classDam.png",
        },
        {
          scale: "三类塘",
          icon: this.$config.imgUrl + "classDam.png",
        },
      ],
      checkedStatus: ["未开工"],
      skStatus: [
        {
          icon: this.$config.imgUrl + "notStart.png",
          scale: "未开工",
          color: "#E8684A",
          num: 0,
        },
        {
          icon: this.$config.imgUrl + "started.png",
          scale: "已开工",
          color: "#F6BD16",
          num: 0,
        },
        {
          icon: this.$config.imgUrl + "currentYear.png",
          scale: "当年完工",
          color: "#1BC57E",
          num: 0,
        },
      ],
      pointArr: {
        未开工: [],
        已开工: [],
        当年完工: [],
        二类塘: [],
        三类塘: [],
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
    sgzgCxjgCl() {
      return this.$store.state.sgzgCxjgCl;
    },
    projectType() {
      let tempArr = [];
      let checkedArr = [...this.checkedStatus, ...this.checkedScale];
      checkedArr.forEach((element) => {
        switch (element) {
          case "未开工":
            tempArr.push(6);
            break;
          case "已开工":
            tempArr.push(7);
            break;
          case "当年完工":
            tempArr.push(8);
            break;
          case "二类塘":
            tempArr.push(9);
            break;
          case "三类塘":
            tempArr.push(10);
            break;
        }
      });
      return tempArr.join(",");
    },
    dam() {
      const damArr = [];
      const one_index = this.checkedScale.findIndex(
        (item) => item === "二类塘"
      );
      const two_index = this.checkedScale.findIndex(
        (item) => item === "三类塘"
      );
      const a_index = this.checkedStatus.findIndex((item) => item === "未开工");
      const b_index = this.checkedStatus.findIndex((item) => item === "已开工");
      const c_index = this.checkedStatus.findIndex(
        (item) => item === "当年完工"
      );
      if (a_index != -1) {
        if (one_index != -1 && two_index == -1) {
          damArr.push(6);
        } else if (one_index == -1 && two_index != -1) {
          damArr.push(9);
        } else {
          damArr.push(6);
          damArr.push(9);
        }
      }
      if (b_index != -1) {
        if (one_index != -1 && two_index == -1) {
          damArr.push(7);
        } else if (one_index == -1 && two_index != -1) {
          damArr.push(10);
        } else {
          damArr.push(7);
          damArr.push(10);
        }
      }
      if (c_index != -1) {
        if (one_index != -1 && two_index == -1) {
          damArr.push(8);
        } else if (one_index == -1 && two_index != -1) {
          damArr.push(11);
        } else {
          damArr.push(8);
          damArr.push(11);
        }
      }
      return damArr.join(",");
    },
  },
  watch: {
    sgzgSelectAdcd(val) {
      this.getList();
    },
    sgzgSelectBasin(val) {
      this.getList();
    },
    sgzgCxjgCl(val) {
      this.getList();
    },
    defultSelected(val) {
      console.log(val, "ssadasdasdsad");
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
      let defultSelected1 = this.defultSelected.split(",")[1];
      let defultSelected2 = this.defultSelected.split(",")[0];
      this.checkedScale = [defultSelected1];
      if (defultSelected2 == "总数") {
        this.checkedStatus = ["未开工", "已开工", "当年完工"];
      } else {
        this.checkedStatus = [defultSelected2];
      }
    },
    changeScale(value) {
      this.showPoints();
    },
    seiPoint(arr, type, styleObj) {
      let pointArr = [];
      let lineArr = [];
      arr.map((v) => {
        pointArr.push({
          lat: v.lttd,
          lng: v.lgtd,
          mapType: "cxjgPopup",
          ...v,
        });
        lineArr.push({
          line: v.coorpot
            ? JSON.parse(v.coorpot.replace(new RegExp("&quot;", "gm"), '"'))[0]
            : "",
              type: 'seawallPopup',
          level: v.projectScale,
        });
        v.lineArr = [];
        v.lineArr.push({
          line: v.coorpot
            ? JSON.parse(v.coorpot.replace(new RegExp("&quot;", "gm"), '"'))[0]
            : "",
          type: 'seawallPopup',
          level: v.projectScale,
        });
      });
      if (type) {
        this.$parent.$parent.$refs.seawallMap.newMapJh(
          pointArr,
          type,
          styleObj
        );
      } else {
        this.$parent.$parent.$refs.seawallMap._drawLines(lineArr);
      }
    },
    async getList() {
      this.skStatus.map((v) => (v.num = 0));
      this.apiOnce = true;
      this.pointArr = {
        未开工: [],
        已开工: [],
        当年完工: [],
        二类塘: [],
        三类塘: [],
      };
      let opt = {
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        dam: this.dam,
      };
      this.$parent.$parent.$refs.seawallMap.removeAllLayer();
      let res = await seawallProIndo(opt);
      if (res.success) {
        this.apiOnce = false;
        if (res.data.length) {
          this.seiPoint(res.data);
          const notArr = res.data.filter((item) => item.type == 4) || [];
          const alreadyArr = res.data.filter((item) => item.type == 5) || [];
          const properArr = res.data.filter((item) => item.type == 6) || [];
          const twoArr = res.data.filter((item) => item.typet == 2) || [];
          const threeArr = res.data.filter((item) => item.typet == 3) || [];
          this.pointArr = {
            未开工: notArr,
            已开工: alreadyArr,
            当年完工: properArr,
            二类塘: twoArr,
            三类塘: threeArr,
          };
          if (notArr.length > 0) {
            const styleObj = {
              jhImg: require("@/assets/images/map/project-red-jh1.png"),
              img: require("@/assets/images/map/project-red1.png"),
            };
            this.seiPoint(notArr, "notArr", styleObj);
          }
          if (alreadyArr.length > 0) {
            const styleObj = {
              jhImg: require("@/assets/images/map/project-yellow-jh1.png"),
              img: require("@/assets/images/map/project-yellow1.png"),
            };
            this.seiPoint(alreadyArr, "alreadyArr", styleObj);
          }
          if (properArr.length > 0) {
            const styleObj = {
              jhImg: require("@/assets/images/map/project-green-jh1.png"),
              img: require("@/assets/images/map/project-green1.png"),
            };
            this.seiPoint(properArr, "properArr", styleObj);
          }
        }
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

<style lang='scss'  >
.wrap {
  .el-radio__label {
    @include flexbox();
    @include flexAI(center);
  }
}
</style>