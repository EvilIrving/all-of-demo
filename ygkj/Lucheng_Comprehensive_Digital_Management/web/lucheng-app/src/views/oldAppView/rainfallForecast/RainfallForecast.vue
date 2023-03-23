<template>
  <div class="tidalLevelForecast_box">
    <van-nav-bar
      fixed
      placeholder
      safe-area-inset-top
      :title="'降雨预报'"
      :left-arrow="$store.state.viewType !== 'irs'"
      @click-left="$router.go(-1)"
    />
    <div class="title-box">
      <span>{{ nowDate }}</span> 至 <span>{{ newDate }}</span>
    </div>
    <div class="map_box">
      <MainMap ref="mainMap" />
    </div>
    <div class="content_box">
      <div class="content_details" v-for="(item, i) in areaList" :key="i">
        <div class="content_detail">
          <div>{{ i + 1 }}</div>
          <div>{{ item.key }}</div>
          <div>{{ item.value }}mm</div>
        </div>
      </div>
    </div>
    <div class="legend_box">
      <img src="@/assets/images/map/rainfallForecast_legend.png" alt="" />
    </div>
    <div class="select_box">
      <div
        :class="['btn_box', i == index ? 'active' : '']"
        v-for="(item, i) in timeList"
        :key="i"
        @click="selectTime(i)"
      >
        <div><img :src="item.imgSrc" alt="" /></div>
        <div>{{ item.name }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import { rainfallForecast } from "@/api/projectDetail";
import MainMap from "./components/MainMap.vue";
export default {
  components: {
    MainMap,
  },
  data() {
    return {
      nowDate: "",
      newDate: "",
      timeList: [
        { name: "1小时", imgSrc: require("@/assets/images/map/1hour.png") },
        { name: "3小时", imgSrc: require("@/assets/images/map/3hour.png") },
        { name: "1天", imgSrc: require("@/assets/images/map/1day.png") },
        { name: "2天", imgSrc: require("@/assets/images/map/2day.png") },
        { name: "3天", imgSrc: require("@/assets/images/map/3day.png") },
      ],
      index: 0,
      redPointLayer: null,
      bluePointLayer: null,
      singlePointLayer: null,
      option: [
        { text: "全部", value: "全部" },
        { text: "正常", value: "正常" },
        { text: "报警", value: "报警" },
      ],
      warning: "全部",
      name: "",
      visible: false,
      tableData: [],
      //短临预报降雨数据
      forecastPreList: {},
      areaList: [],
    };
  },
  computed: {
    routeDeep() {
      let routeDeep = [];
      for (var i = 0; i < this.routes.length; i++) {
        routeDeep.push(this.routes[i].path);
      }
      return routeDeep;
    },
  },
  watch: {
    $route(to, from) {
      const routeDeep = this.routeDeep;
      const toDepth = routeDeep.indexOf(to.path);
      const fromDepth = routeDeep.indexOf(from.path);
      this.transitionName = toDepth > fromDepth ? "slide-left" : "slide-right";
    },
  },
  async mounted() {
    // this.getList()
    await this.getRainfallData();
    await this.selectTime(0);
    // setTimeout(() => {

    // }, 1000);
  },
  methods: {
    async rainFallGrid(time) {
      //   this.tableData = []
      let res = await rainfallForecast.rainFallGrid({ hour: time });
      this.areaList = res.data;
      for(var item of this.areaList){
        if(item.value!==null){
          item.value = item.value.toFixed(3)
        }else{
          item.value = '0.000'
        }
      }

    },
    /** 初始化雨量数据 */
    async getRainfallData(areaName) {
      //   console.log('15151515pppp')
      let opt = {
        areaName: "",
      };
      if (areaName && areaName != "温州市") {
        opt.areaName = areaName;
      } else {
        opt.areaName = "";
      }
      //   rainfallForecast.list(opt).then((res) => {
      //     if (res.code == 0) {
      //       this.forecastPreList = res.data
      //     //   console.log(this.forecastPreList, 'this.forecastPreList')
      //     }
      //   })

      let res = await rainfallForecast.list({ areaName: "鹿城区" });
      if (res.code == 0) {
        this.forecastPreList = res.data;
        //   console.log(this.forecastPreList, 'this.forecastPreList')
      }
      if (!areaName) {
        // this.getForecastPreMaxValue(1)
      }
    },
    selectTime(i) {
      this.index = i;
      let times = [1, 3, 24, 48, 72];
      let nowtime = new Date();
      let selectTime = times[i];
      this.rainFallGrid(selectTime);
      //   console.log(nowtime.format('MM'))
      //   console.log(new Date(nowtime.getTime() + 24 * 60 * 60 * 1000))
      //   console.log(times[i])
      let nowDate =
        nowtime.format("yyyy") +
        "年" +
        nowtime.format("MM") +
        "月" +
        nowtime.format("dd") +
        "日 " +
        nowtime.format("hh") +
        " 时";
      //   console.log(nowDate)
      let newTime = new Date(nowtime.getTime() + selectTime * 60 * 60 * 1000);
      let newDate =
        newTime.format("yyyy") +
        "年" +
        newTime.format("MM") +
        "月" +
        newTime.format("dd") +
        "日 " +
        newTime.format("hh") +
        " 时";
      //   console.log(newDate)
      this.nowDate = nowDate;
      this.newDate = newDate;

      //全降雨演练分析开的时候 点击无效
      //   if (this.RangeBar) {
      //     this.$message({ type: 'warn', message: '请先关闭全时序降雨分析' })
      //     return
      //   }
      if (i == null) {
        this.clearAnalysisInfo();
        return;
      }
      let valMap = [1, 3, 6, null, null];
      //   this.cardSelect = true
      let data = this.forecastPreList[`rainfall_${valMap[i]}_data`];
     // console.log(data, "data");
      if (data) {
        this.$refs.mainMap.showPredictRain(data, { hour: valMap[i] });
      }
      //   this.getForecastPreMaxValue(valMap[i])
    },
  },
};
</script>

<style lang="scss" scoped>
.tidalLevelForecast_box {
  height: 100%;
  width: 100%;
  .title-box {
    width: 100%;
    height: 44px;
    background: #ffffff;
    text-align: center;
    line-height: 44px;
    font-size: 14px;
    color: #666666;
  }
  .map_box {
    width: 100%;
    // height: calc(100% - 204px);
    height: 280px;
    position: relative;
  }
  .content_box {
    width: 100%;
    height: 216px;
    overflow: auto;
    .content_details {
      display: inline-block;
      width: 184px;
      height: 30px;
      background-color: rgba(233, 240, 253, 0.28);
      border: 1px solid rgba(39, 106, 240, 0.28);
      margin-bottom: 1px;
      .content_detail {
        width: 100%;
        display: flex;
        justify-content: space-between;
        padding: 0 11px;
        box-sizing: border-box;
        color: #666666;
        font-size: 12px;
        line-height: 30px;
      }
    }
    .content_details:nth-child(odd) {
      margin-right: 3px;
    }
  }
  .legend_box {
    margin-top: 18px;
    width: 100%;
    > img {
      width: 100%;
    }
  }
  .select_box {
    display: flex;
    .btn_box {
      width: 20%;
      height: 78px;
      border: 1px solid #e9f0fd;
      border-right: none;
      background: url("../../../assets/images/map/btn_unchecked.png") no-repeat
        center;
      background-size: 100% 100%;
      text-align: center;
      img {
        margin: 13px 0;
        width: 100%;
      }
      &.active {
        background: url("../../../assets/images/map/btn_checked.png") no-repeat
          center;
        background-size: 100% 100%;
        color: #ffffff;
      }
    }
    .btn_box:last-child {
      border-right: 1px solid #e9f0fd;
    }
  }
}
</style>
