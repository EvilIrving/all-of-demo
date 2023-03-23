<template>
  <view-page>
    <div class="appindex">
      <div class="head_box">
        <div class="head_info">
          <span class="temperature">{{ weather.wd }}</span>
          <div class="head_info_times">
            <span>{{ dateInfo.date }}</span>
            <span>{{ dateInfo.week }}</span>
          </div>
          <!-- <div class="old-tip">
            <img src="../../assets/old-tip-02.png" alt="" />
          </div> -->
        </div>
      </div>
      <div class="content-box">
        <div class="flood_control_info">
          <wrap-title title="防汛信息" />
          <div class="content">
            <div @click="goLink('/rainfall')">
              <img :src="imgsrc.realRainfall" />
              <div>实时雨情</div>
            </div>
            <div @click="goLink('/waterRegimenList')">
              <img :src="imgsrc.realHydrology" />
              <div>实时水情</div>
            </div>
          </div>
        </div>
        <div class="work_info">
          <wrap-title title="办公信息" />
          <div class="content">
            <!-- <div @click="openApproval('涉水审批')">
              <img :src="imgsrc.waterApproval" />
              <div>涉水审批</div>
            </div> -->
            <div @click="goLink('/propertyApproval')">
              <img :src="imgsrc.projectApproval" />
              <div>物业审批</div>
            </div>
            <div @click="goLink('/propertySupervision')">
              <img :src="imgsrc.projectSupervision" />
              <div>物业督办</div>
            </div>
            <div @click="goProjectFile('工程')">
              <img :src="imgsrc.projectFiles" />
              <div>工程档案</div>
            </div>
            <div @click="goProjectFile('物业')">
              <img :src="imgsrc.propertyFiles" />
              <div>物业档案</div>
            </div>
            <div @click="goLink('/personnelAttendance')">
              <img :src="imgsrc.peopleAssess" />
              <div>人员考核</div>
            </div>
            <!-- <div @click="goLink('/workbench')">
              <img :src="imgsrc.workbench" />
              <div>工作台</div>
            </div> -->
          </div>
        </div>
        <div class="service-delivery">
          <p>本服务由浙江政府服务网、温州市鹿城区农业农村局提供</p>
          <p>服务热线：<span @click="phoneCall">0577-88281163</span></p>
        </div>
      </div>
    </div>
  </view-page>
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
        hydrologyAlarmNum: 0, // 水文
        tideAlarmNum: 0, // 潮位
        waterAlarmNum: 0, // 水位
        torrentAlarmNum: 0, // 山洪
        rainAlarmNum: 0, // 雨量
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
    phoneCall() {
      ZWJSBridge.onReady(() => {
        ZWJSBridge.phoneCall({
          corpId: "0577-88281163",
        })
          .then((result) => {
            console.log(result);
          })
          .catch((error) => {
            console.log(error);
          });
      });
    },
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
  position: relative;
  .head_box {
    height: 152px;
    background: url("~@/assets/images/pageIndex/index_bg.png") no-repeat;
    background-size: 100% 100%;
    // padding-top: 30px;

    .head_info {
      font-family: D-DINExp, D;
      font-weight: normal;
      color: #ffffff;
      margin-left: 10px;
      position: relative;
      .temperature {
        font-size: 32px;
        line-height: 48px;
        margin-right: 5px;
      }
      .head_info_times {
        font-size: 18px;
        line-height: 20px;
      }
      .old-tip {
        position: absolute;
        right: 12px;
        bottom: -5px;
        width: 102px;
        img {
          width: 100%;
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
  .content-box {
    top: 76px;
    left: 0;
    position: absolute;
    border-radius: 8px 8px 0px 0px;
    background-color: #fff;
    overflow: hidden;
    padding-top: 12px;
  }
  .content {
    color: #666666;
    line-height: 16px;
    display: flex;
    flex-wrap: wrap;
    padding: 16px 17px;
    > div {
      margin-right: 16px;
      padding: 20px 0 23px;
      width: 103px;
      text-align: center;
      background: url("../../assets/content-div-bg.png") no-repeat;
      background-size: 100% 100%;
      margin-bottom: 10px;
      img {
        width: 30px;
        height: 30px;
        // margin-left: 10px;
        margin-bottom: 16px;
      }
      div {
        font-size: 20px;
      }
      &:nth-child(3n) {
        margin-right: 0;
      }
    }
  }
  .service-delivery {
    text-align: center;
    padding: 75px 16px 20px;
    font-size: 18px;
    line-height: 22px;
    span {
      color: #2c3e50;
    }
  }
}
</style>

<style>
.appindex .van-nav-bar {
  background-color: #276af0;
}
.appindex .van-nav-bar--fixed,
.appindex .van-nav-bar__placeholder,
.appindex .van-nav-bar__content {
  background-color: #276af0;
}
.appindex .van-nav-bar__title {
  margin-left: 12px;
  color: #fff;
  font-size: 20px;
  font-weight: 500;
}
</style>
