<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:29:55
 * @LastEditTime: 2021-12-31 14:47:56
 * @LastEditors: dtb
 * @Description: 实时降雨统计卡片
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\RealRainCount.vue
-->
<template>
  <count-wrap title="实时降雨" class="rain-count">
    <el-row class="has-count">
      <el-col :span="12" class="fs24">
        <p style="cursor:pointer" @click="realRainClick('1hourBeyond30List')">
          1小时超30mm<span class="yellow ml12 mr12 fs36">{{
            realtimePreList["1hourBeyond30"] || "0"
          }}</span>个
        </p>
      </el-col>
      <el-col :span="12" class="fs24">
        <p style="cursor:pointer" @click="realRainClick('1hourBeyond60List')">
          3小时超60mm<span class="orange fs36 ml12 mr12">{{
            realtimePreList["3hourBeyond60"] || "0"
          }}</span>个
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { mapGetters } from 'vuex';
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
export default {
  name: "RealRainCount",
  data() {
    return {
      //短临预报降雨数据
      realtimePreList: {},
      timer: null,
    };
  },
  components: { CountWrap },
  computed: {
    ...mapGetters([
      'ScreenMap',
    ]),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  watch: {
    rainList: {
      deep: true,
      handler(val) {
        //预演雨量点位开关开 加载雨量点位
        if (val && this.ylbtn) {
          this.loadRainfallPoint();
        }
      }
    },
    realtimePreList: {
      deep: true,
      handler(val) {
        //预演等值面开关开 如果接口有等值面缓存 使用接口中的url
        if (val && this.rainfallArea) {
          const mapArray = [
            'rainfall_newsest_1_rcurl',
            'rainfall_1_rcurl',
            'rainfall_3_rcurl',
            'rainfall_6_rcurl',
            'rainfall_24_rcurl',
            'rainfall_72_rcurl'
          ];
          const href = val[mapArray[this.realtimePreCardSelect]];
          if (href) {
            const mapImage = {
              href
            };
            this.ScreenMap.showIsohyetLayer(mapImage);
          } else {
            this.showRealTimeRainfull();
          }
        }
      }
    }
  },
  mounted() {
    this.getRainfallData();
    this.timer = setInterval(() => {
      this.getRainfallData();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    /** 初始化雨量数据 */
    getRainfallData(areaName) {
      let opt = {
        areaName: "",
      };
      if (areaName && areaName != "温州市") {
        opt.areaName = areaName;
      } else {
        opt.areaName = "";
      }
      disasterPreventionApi.realtimeRainfallByAreaName(opt).then((res) => {
        if (res.code == 0) {
          this.realtimePreList = res.data;
        }
      });
    },
    realRainClick(type) {
      let rainlist = this.realtimePreList[type]
      this.ScreenMap.loadRainfallPoint(rainlist, true);
    },
  },
};
</script>
<style lang="scss" scoped>
@import './style/CountCard';
.rain-count .has-count {
  height: 100%;
  .el-col {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
}
</style>
