<!--
 * @Date: 2021-12-23 15:29:06
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2021-12-26 21:57:59
 * @FilePath: \rscp-big-screen\src\views\sgzgScreenNew\zhekuHealthy\zhekuHealthyRight\zhekuHealthyRight.vue
-->
<template>
  <div class="wrap">
    <div class="left_title">
      <span>视频监视</span>
    </div>
    <ul class="one">
      <li v-for="(item) in shipin" :key="item.name" @click="goShpin">
        <img :src="item.img" alt="">
        <div class="li_box">
          <div class="title">
            <!-- <p>{{item.name}}</p> -->
          </div>
          <p class="jike">点击立即前往</p>
        </div>
      </li>
    </ul>
    <div class="left_title">
      <span>工程照片</span>
    </div>
    <ul class="two">
      <li v-for="(item) in zhaopian" :key="item.name" @click="goZhaopian">
        <img :src="item.img" alt="">
        <div class="li_box">
          <div class="title">
            <!-- <p>{{item.name}}</p> -->
          </div>
          <p class="jike">点击立即前往</p>
        </div>

      </li>
    </ul>
    <div class="left_title">
      <span>数字孪生</span>
    </div>
    <ul class="three">
      <li v-for="(item,index) in reserviors" :key="item.name" @click="goGoal(index)">
        <img :src="item.img" alt="">
        <div class="li_box">
          <div class="title">
            <p>{{item.name}}</p>
          </div>
          <p class="text">点击立即前往</p>
        </div>

      </li>
    </ul>
  </div>
</template>

<script>
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {
  },
  data() {
    return {
      imgUrl: require('@/assets/images/liaowan.png'),
      reserviors: [{ name: '康门水库 》', img: require("@/assets/images/kangmen.png") }, { name: '九峰水库 》', img: require('@/assets/images/jiufeng.png') }, { name: '合溪水库 》', img: require('@/assets/images/hexi.png') },],
      shipin: [{ name: '康门水库 》', img: require("@/assets/images/kmshipin.png") },],
      zhaopian: [{ name: '康门水库 》', img: require("@/assets/images/kmgongcheng.png") },],
      opt: {
        statistic: "mapList",
        itemCategory: "keyStr",
        itemValue: "FCLWL_PREC",
        basin: "",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectAdnm() {
      return this.$store.state.leftSelectAdnm;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.opt.adcd = val ? val : this.$localData("get", "userInfo").adcd;
      if (val == "33") {
        this.opt.superviseLevel = 1;
      } else {
        this.opt.superviseLevel = "";
      }
      this.getReservoirList();
    }
  },
  mounted() {
    this.getReservoirList()
  },
  methods: {
    goShpin() {
      window.open('https://sk.zjwater.com/cp/skip/camera_view?cameraid=1125&cameraname=绍兴-辽湾水库大坝&token=')
    },
    goZhaopian() { window.open(this.imgUrl) },
    goGoal(index) {
      switch (index) {
        case 0:
          window.open('http://60.190.228.70:800/Home/stationinfopagesl?stationId=1033894&stationType=50')
          break;
        case 1:
          window.open('http://sk.vnet1000.net/project/login/auto/2')
          break;
        default:
          window.open('http://hxsk.zjszsk.com/unity/reservoirs/info/pages?reservoir_id=3')
          break;
      }
    },
    // 水库列表
    async getReservoirList() {
      this.$parent.$refs.reservoirMap.removeAllLayer();
      this.$parent.$refs.reservoirMap.closePopup();
      this.$parent.$refs.reservoirMap.initView();
      let res = await reservoirLeftStatisticApi(this.opt);
      if (res.success) {

        let pointArr = [];
        if (res.data.length) {
          res.data.map((v) => {
            v.lat = v.LTTD;
            v.lng = v.LGTD;
            v.rightType = "1";
            v.colorType = "blue";
            v.src = require("@/assets/images/map/reservoir.png");
            pointArr.push(v);
          });
        }
        this.$parent.showMapPoints(pointArr);
      }
      this.tableLoading = false;
      this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", true);
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  width: 100%;
  height: 100%;
  .left_title {
    width: 100%;
    height: 35px;
    span {
      font: bold 20px italic;
      vertical-align: center;
      margin-left: 30px;
      line-height: 35px;
    }
    background: left/96% no-repeat url("../../../../assets/images/menu-bg.png");
  }
  ul.one,
  ul.two {
    height: 17%;
    p {
      font-size: 25px;
      color: rgba(255, 255, 255, 0.9);
    }
  }
  .jike {
    font-size: 24px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 500;
    color: #ffffff;
    line-height: 33px;
  }
  ul.three {
    height: 50%;
  }
  ul {
    width: 100%;
    padding: 0 16px 16px 16px;
    @include flexbox();
    @include flexJC(space-between);
    @include flexflow(column nowrap);
    li {
      flex: 1;
      height: 33%;
      position: relative;
      box-sizing: border-box;
      text-align: center;
      border-radius: 2px;
      margin-top: 10px;
      cursor: pointer;
      .li_box {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        color: white;
        .title {
          p {
            font-style: italic;
            font-weight: bold;
            font-size: 24px;
          }
        }
        p.text {
          margin-top: 10px;
          font-size: 12px;
          color: rgba(255, 255, 255, 0.9);
        }
      }
      img {
        width: 100%;
        height: 100%;
      }
    }
  }
}
</style>
