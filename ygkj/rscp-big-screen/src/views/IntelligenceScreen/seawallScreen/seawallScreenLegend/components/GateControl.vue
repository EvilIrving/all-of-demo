<!--
 * @Author: wqn
 * @Date: 2023-02-23 17:19:47
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-24 17:35:03
 * @Description: 
-->
<template>
  <div class='wrap'>
     <el-radio-group v-model="checkedScale" @change="changeCheckedScale">
      <el-radio v-for="item in skScale" :label="item.scale" :key="item.scale">
        <p class="legend_label_radio">
          <img :src="item.icon" alt="">
          <span>{{item.scale}}</span>
        </p>
      </el-radio>
    </el-radio-group>
    <div class="line"></div>
    <div class="item">
      <i class="open"></i>
      <span>开启</span>
    </div>
    <div class="item">
      <i class="close"></i>
      <span>关闭</span>
    </div>
  </div>
</template>

<script>
import { seawallDoorList } from "@/api/api_seawall";
export default {
  name: 'GateControl',
  props: {
    defultSelected: {
      type: String,
      default: "",
    },
  },
  components:{},
  data() {
    return {
      checkedScale: "旱闸",
      skScale: [
        {
          icon: this.$config.imgUrl + "map-icon-dry-sluice.png",
          scale: "旱闸",
        },
        {
          icon: this.$config.imgUrl + "map-icon-road-junction.png",
          scale: "道口",
        },
        {
          icon: this.$config.imgUrl + "map-icon-breach.png",
          scale: "缺口",
        },
        {
          icon: this.$config.imgUrl + "map-icon-dock-entrance.png",
          scale: "船坞口"
        },
      ],
    }
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
      // this.getList();
    },
    defultSelected(val) {
      this.setSelect();
      this.getList();
    },
  },
  created() {},
  mounted() {
    if (this.defultSelected !== "") {
      this.setSelect();
    }
    this.getList();
  },
  destroyed() {},
  methods: {
    setSelect() {
      this.checkedScale = this.defultSelected;
    },
    changeCheckedScale() {
      this.$store.commit("SET_NUMBER_SELECT", this.checkedScale);
      this.getList();
    },
    getList() {
      this.$parent.$parent.$refs.seawallMap.removeAllLayer();
      seawallDoorList({
        extractPram: this.checkedScale,
        adcd: this.sgzgSelectAdcd,
        fetchAll: true
      }).then(res => {
        if (res.success) {
          let pointArr = [];
          let lineArr = [];
          if (res.rows.length) {
            res.rows.map((v) => {
              let src = ''
              if (this.checkedScale === '旱闸') {
                if (v.kstatus == 1) {
                  src = this.$config.imgUrl + "map-icon-dry-sluice.png"
                } else {
                  src = this.$config.imgUrl + "map-icon-dry-sluice-yellow.png"
                }
              } else if (this.checkedScale === '道口') {
                if (v.kstatus == 1) {
                  src = this.$config.imgUrl + "map-icon-road-junction.png"
                } else {
                  src = this.$config.imgUrl + "map-icon-road-junction-yellow.png"
                }
              } else if (this.checkedScale === '缺口') {
                if (v.kstatus == 1) {
                  src = this.$config.imgUrl + "map-icon-breach.png"
                } else {
                  src = this.$config.imgUrl + "map-icon-breach-yellow.png"
                }
              } else if (this.checkedScale === '船坞口') {
                if (v.kstatus == 1) {
                  src = this.$config.imgUrl + "map-icon-dock-entrance.png"
                } else {
                  src = this.$config.imgUrl + "map-icon-dock-entrance-yellow.png"
                }
              }
              pointArr.push({
                lat: + v.latd,
                lng: + v.lgtd,
                mapType: "gateControlPopup",
                ...v,
                styleObj: {
                  src,
                  anchor: [0.5, 0.5],
                  anchorXUnits: 'fraction',
                  anchorYUnits: 'fraction',
                  scale: 0.8
                }
              });
            });
          }
          if (res.coorpotList.length) {
            res.coorpotList.map((v) => {
              lineArr.push({
                line: v.coorpot ? JSON.parse(v.coorpot.replace(new RegExp("&quot;", "gm"), '"'))[0] : "",
                type: 'seawallPopup',
                level: v.project_scale ?? '',
              });
            })
          }
          this.$parent.$parent.$refs.seawallMap.removeAllLayer()
          this.$parent.$parent.$refs.seawallMap.drawMapPoints(pointArr, "seawallPopup");
          this.$parent.$parent.$refs.seawallMap._drawLines(lineArr);
        }
      })
    }
  },
}

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
    .legend_label_radio {
      @include flexbox();
      @include flexAI(center);
      span {
        margin-left: 5px;
        color: #fff;
      }
    }
  }
  .line {
    width: 90%;
    height: 2px;
    margin: 5px auto;
    background: #979797;
  }
  .item {
    display: flex;
    align-items: center;
    padding-left: 15px;
    height: 30px;
    > i {
      display: inline-block;
      width: 15px;
      height: 15px;
      border-radius: 50%;
      margin-right: 10px;
    }
    .open {
      background-color: #3886ff;
    }
    .close {
      background-color: #f6bd16;
    }
  }
}
</style>