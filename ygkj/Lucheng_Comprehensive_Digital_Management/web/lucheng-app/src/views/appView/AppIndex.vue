<template>
  <div class="appindex">
    <van-nav-bar
      placeholder
      :border="false"
      safe-area-inset-top
      :title="$route.meta.title"
      right-text="按钮"
      @click-right="$router.go(-1)"
      
    >
      <template #right>
        <van-icon name="cross" size="18" color="#fff" />
      </template>
    </van-nav-bar>
    <div class="head_box">
      <div class="head_info">
        <span class="temperature"
          >{{ weather.wd}}</span
        >
        <div>
          <span>{{ dateInfo.date }}</span>
          <span style="margin-left: 3px">{{ dateInfo.week }}</span>
        </div>
      </div>
      <div class="box_content">
        <div class="box_item" @click="goLink('/rainfall?type=warning')">
          <van-badge :content="alarm.rainAlarmNum">
            <img :src="imgsrc.rainfall" />
          </van-badge>
          <span>雨量超警</span>
        </div>
        <div class="box_item" @click="goLink('/waterRegimenList?type=warning')">
          <van-badge :content="alarm.waterAlarmNum">
            <img :src="imgsrc.waterLevel" />
          </van-badge>
          <span>水位超警</span>
        </div>
        <div class="box_item">
          <van-badge :content="alarm.hydrologyAlarmNum">
            <img :src="imgsrc.hydrology" />
          </van-badge>
          <span>水文预警</span>
        </div>
        <div class="box_item">
          <van-badge :content="alarm.tideAlarmNum">
            <img
              :src="imgsrc.seaLevel"
              @click="goLink('/tidalLevelForecast?type=warning')"
            />
          </van-badge>
          <span>潮位超警</span>
        </div>
        <div class="box_item">
          <van-badge :content="alarm.torrentAlarmNum">
            <img
              :src="imgsrc.torrent"
              @click="goLink('/flashFloodRisk?type=warning')"
            />
          </van-badge>
          <span>山洪超警</span>
        </div>
      </div>
    </div>
    <div class="flood_control_info">
      <wrap-title title="防汛信息" />
      <p class="line"></p>
      <div class="content">
        <van-grid :column-num="5" :border="false">
          <van-grid-item @click="goLink('/FloodPaper')">
            <img :src="imgsrc.floodPaper" />
            <span>汛情摘要</span>
          </van-grid-item>
          <van-grid-item @click="goLink('/rainfall')">
            <img :src="imgsrc.realRainfall" />
            <span>实时雨情</span>
          </van-grid-item>
          <van-grid-item @click="goLink('/waterRegimenList')">
            <img :src="imgsrc.realHydrology" />
            <span>实时水情</span>
          </van-grid-item>
          <van-grid-item @click="goLink('/tidalLevelForecast')">
            <img :src="imgsrc.tideForecast2" />
            <span>潮位预报</span>
          </van-grid-item>
          <van-grid-item>
            <img :src="imgsrc.waterForecast" />
            <span>水文预报</span>
          </van-grid-item>
          <van-grid-item>
            <img
              :src="imgsrc.rainForecast"
              @click="goLink('/rainfallForecast')"
            />
            <span>降雨预报</span>
          </van-grid-item>
          <van-grid-item @click="goProjectFile(1, 'climate')">
            <img :src="imgsrc.realTyphoon" />
            <span>实时台风</span>
          </van-grid-item>
          <van-grid-item @click="goProjectFile(2, 'climate')">
            <img :src="imgsrc.satellitePicture" />
            <span>雷达卫星</span>
          </van-grid-item>
          <van-grid-item @click="goLink('/flashFloodRisk')">
            <img :src="imgsrc.tideForecast" />
            <span>山洪风险</span>
          </van-grid-item>
        </van-grid>
      </div>
    </div>
    <div class="project_info">
      <wrap-title title="工程信息" />
      <p class="line"></p>
      <div class="content">
        <van-grid :column-num="5" :border="false" style="width: 100%">
          <van-grid-item>
            <img :src="imgsrc.reservoir" @click="gotoProject('水库')" />
            <span>水库</span>
          </van-grid-item>
          <van-grid-item>
            <img :src="imgsrc.sluice" @click="gotoProject('水闸')" />
            <span>水闸</span>
          </van-grid-item>
          <van-grid-item>
            <img :src="imgsrc.seawall" @click="gotoProject('海塘')" />
            <span>海塘</span>
          </van-grid-item>
          <van-grid-item>
            <img :src="imgsrc.dike" @click="gotoProject('堤防')" />
            <span>堤防</span>
          </van-grid-item>
          <van-grid-item>
            <img :src="imgsrc.floodPaper" @click="gotoProject('山塘')" />
            <span>山塘</span>
          </van-grid-item>
        </van-grid>
      </div>
    </div>
    <div class="work_info">
      <wrap-title title="办公信息" />
      <p class="line"></p>
      <div class="content">
        <van-grid :column-num="5" :border="false">
          <van-grid-item @click="openApproval('涉水审批')">
            <img :src="imgsrc.waterApproval" />
            <span>涉水审批</span>
          </van-grid-item>
          <van-grid-item @click="goLink('/propertyApproval')">
            <img :src="imgsrc.projectApproval" />
            <span>物业审批</span>
          </van-grid-item>
          <van-grid-item @click="goLink('/propertySupervision')">
            <img :src="imgsrc.projectSupervision" />
            <span>物业督办</span>
          </van-grid-item>
          <van-grid-item @click="goProjectFile('工程')">
            <img :src="imgsrc.projectFiles" />
            <span>工程档案</span>
          </van-grid-item>
          <van-grid-item @click="goProjectFile('物业')">
            <img :src="imgsrc.propertyFiles" />
            <span>物业档案</span> </van-grid-item
          ><!--  -->
          <van-grid-item @click="goLink('/personnelAttendance')">
            <img :src="imgsrc.peopleAssess" />
            <span>人员考核</span>
          </van-grid-item>
          <van-grid-item @click="goLink('/workbench')">
            <img :src="imgsrc.workbench" />
            <span>工作台</span>
          </van-grid-item>
          <!-- <van-grid-item></van-grid-item>
          <van-grid-item></van-grid-item>
          <van-grid-item></van-grid-item> -->
        </van-grid>
      </div>
    </div>
  </div>
</template>

<script>
import WrapTitle from "@/components/wrapTitle";
import { homeIndex } from "@/api/projectDetail";
import { alarmStatic } from "@/api/appView";
export default {
  name: "",
  props: {},
  components: {
    WrapTitle,
  },
  data() {
    return {
      imgsrc: {
        rainfall: require("@/assets/images/pageIndex/rainfall.png"),
        waterLevel: require("@/assets/images/pageIndex/water_level.png"),
        hydrology: require("@/assets/images/pageIndex/hydrology.png"),
        seaLevel: require("@/assets/images/pageIndex/sea_level.png"),
        torrent: require("@/assets/images/pageIndex/torrent.png"),
        floodPaper: require("@/assets/images/pageIndex/flood_paper.png"),
        realRainfall: require("@/assets/images/pageIndex/real_rainfall.png"),
        realHydrology: require("@/assets/images/pageIndex/real_hydrology.png"),
        tideForecast: require("@/assets/images/pageIndex/tide_forecast.png"),
        waterForecast: require("@/assets/images/pageIndex/water_forecast.png"),
        rainForecast: require("@/assets/images/pageIndex/rain_forecast.png"),
        realTyphoon: require("@/assets/images/pageIndex/real_typhoon.png"),
        satellitePicture: require("@/assets/images/pageIndex/satellite_picture.png"),
        weatherRadar: require("@/assets/images/pageIndex/weather_radar.png"),
        tideForecast2: require("@/assets/images/pageIndex/tide_forecast2.png"),
        reservoir: require("@/assets/images/pageIndex/reservoir.png"),
        sluice: require("@/assets/images/pageIndex/sluice.png"),
        seawall: require("@/assets/images/pageIndex/seawall.png"),
        dike: require("@/assets/images/pageIndex/dike.png"),
        pool: require("@/assets/images/pageIndex/pool.png"),
        waterApproval: require("@/assets/images/pageIndex/water_approval.png"),
        projectApproval: require("@/assets/images/pageIndex/project_approval.png"),
        projectSupervision: require("@/assets/images/pageIndex/project_supervision.png"),
        projectFiles: require("@/assets/images/pageIndex/project_files.png"),
        propertyFiles: require("@/assets/images/pageIndex/property_files.png"),
        peopleAssess: require("@/assets/images/pageIndex/people_asssess.png"),
        workbench: require("@/assets/images/pageIndex/workbench.png"),
      },

      dateInfo: {
        date: "2021.07.25",
        week: "星期日",
        time: "19:00:29",
      },
      timer: null,
      weather: {},
      alarm: {
        hydrologyAlarmNum: null, // 水文
        tideAlarmNum: null, // 潮位
        waterAlarmNum: null, // 水位
        torrentAlarmNum: null, // 山洪
        rainAlarmNum: null, // 雨量
      },
    };
  },
  created() {
    this.getDateInfo();
    this.getWeatherInfo();
    this.getAlarmStatic();
  },
  mounted() {},
  methods: {
    openApproval() {
      window.location.href = "https://slgh.lucheng.gov.cn/approval/";
    },
    gotoProject(type) {
      this.$router.push("/reservoir?type=" + type);
    },
    goLink(path) {
      this.$router.push(path);
    },
    goProjectFile(type, path) {
      if (type === 1 || type === 2 || type === 3) {
        this.$router.push(`${path}?type=` + type);
      } else {
        this.$router.push("/projectFiles?type=" + type);
      }
    },
    async getAlarmStatic() {
      alarmStatic().then((res) => {
        if (res && res.code === 0) {
          this.alarm = this.filterParams(res.data);
        }
      });
    },
    filterParams(obj) {
      let _newPar = {};
      for (let key in obj) {
        if (obj[key]) {
          _newPar[key] = obj[key];
        } else {
          _newPar[key] = null;
        }
      }
      return _newPar;
    },
    async getWeatherInfo() {
      homeIndex.weatherForecast().then((res) => {
        if (res && res.code === 0) {
          this.weather = res.data[0];
        }
      });
    },
    getDateInfo() {
      const d = new Date();
      this.dateInfo.date = d.getMonth() + 1 + "月" + d.getDate() + "日";

      /* this.dateInfo.date = `${d.getFullYear()}.${handleSingleDigit(
           d.getMonth() + 1
       )}.${handleSingleDigit(d.getDate())}`;*/
      this.dateInfo.week =
        "星期" + ["日", "一", "二", "三", "四", "五", "六"][d.getDay()];
      this.dateInfo.time = `${handleSingleDigit(
        d.getHours()
      )}:${handleSingleDigit(d.getMinutes())}:${handleSingleDigit(
        d.getSeconds()
      )}`;
      this.year1 = `${d.getFullYear()}`;
      this.year2 = `${d.getFullYear()}`;
      function handleSingleDigit(value) {
        return value < 10 ? "0" + value : value;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.appindex {
  width: 100%;
  height: 100%;
  .head_box {
    height: 152px;
    background: url("~@/assets/images/pageIndex/index_bg.png") no-repeat;
    background-size: 100% 100%;
    border-radius: 0px 0px 8px 8px;
    // padding-top: 30px;
    .head_info {
      font-size: 12px;
      font-family: D-DINExp, D;
      font-weight: normal;
      color: #ffffff;
      margin-left: 10px;
      .temperature {
        font-size: 24px;
        line-height: 48px;
        margin-right: 5px;
      }
    }

    .box_content {
      display: flex;
      justify-content: space-around;
      width: 360px;
      height: 77px;
      background: rgba(255, 255, 255, 0.8);
      border-radius: 5px;
      // border: 1px solid;
      margin-top: 10px;
      border-image: linear-gradient(
          135deg,
          rgba(255, 255, 255, 0.87),
          rgba(255, 255, 255, 0.25),
          rgba(255, 255, 255, 1)
        )
        1 1;
      backdrop-filter: blur(16px);
      margin-left: 8px;
      .box_item {
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        margin: 10px 5px;
        display: flex;
        justify-content: space-around;
        align-items: center;
        flex-direction: column;
        img {
          width: 30px;
          height: 30px;
        }
      }
    }
  }
  .title {
    height: 32px;
    border-bottom: 1px solid #e9f0fd;
    padding-top: 20px;
    img {
      margin-left: 8px;
      margin-right: 5px;
    }
  }
  .content {
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 200;
    color: #666666;
    line-height: 16px;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    background-color: #fff;
    padding: 0 5px;
    img {
      width: 30px;
      height: 30px;
      // margin-left: 10px;
      margin-bottom: 8px;
    }
    span {
      // margin-left: 11px;
    }
  }
  .line {
    width: 359px;
    height: 1px;
    background: #e9f0fd;
    margin-left: 8px;
  }
}
</style>
<style>
/* vant 导航栏定位 全局样式 */
.van-nav-bar--fixed {
  position: fixed;
  top: 30px;
  left: 0;
  width: 100%;
}
.appindex .van-nav-bar {
  padding-top: 30px;
  background-color: #276af0;
}
.van-nav-bar__placeholder {
  padding-top: 30px;
  z-index: 9;
}
.appindex .van-nav-bar--fixed,
.appindex .van-nav-bar__placeholder,
.appindex .van-nav-bar__content {
  background-color: #276af0;
}
.appindex .van-nav-bar__title {
  margin-left: 20px;
  color: #fff;
}
</style>
