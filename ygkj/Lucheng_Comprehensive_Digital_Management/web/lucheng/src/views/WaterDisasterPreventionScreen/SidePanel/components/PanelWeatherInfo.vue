<script>
import PanelItem from "../../components/PanelItem.vue";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
import { mapGetters, mapActions } from "vuex";

export default {
  name: "PanelWeatherInfo",
  props: {},
  data() {
    return {
      actionPointBtnActive: false,
      currentCarouselIndex: 0,
      //气象预警数据
      weatherWarningList: [],
      weatherInfoList: [],
      weatherInfo: {
        airPressure: "1003.7",
        rain: "0.1",
        sunrise: "5:05",
        sunset: "18:41 ",
        visibility: "723",
        windPower: "0级",
        humidity: "",
        currentWindSpeed: "",
        currentTemperature: 21.8,
        winds: "",
      },
    };
  },
  computed: { ...mapGetters(["ScreenMap", "wenzhouWeatherForecast", "weatherForecast"]) },
  mounted() {
    this.getWeatherWarning();
    // this.getWeatherInfo();
    this.getWeatherForecast();
  },
  render() {
    let weatherInfo = this.renderWeatherInfo();
    let weatherWarning = this.renderWeatherWarning();
    let titleList = ["气象预警", "天气实况"];
    let yinzhangClass = [
      this.actionPointBtnActive ? "icon-yinzhang-active" : "icon-yinzhang-1",
      "icon-sty",
    ];
    return (
      <PanelItem
        title={
          this.weatherWarningList.length > 0
            ? titleList[this.currentCarouselIndex]
            : "天气实况"
        }
        hasSwitch
        class="panel-meteorological"
      >
        <template slot="title-switch">
          <i
            class={yinzhangClass}
            onClick={() => {
              this.handlePointButtonClick();
            }}
          />
        </template>

        <el-carousel
          trigger="click"
          arrow="never"
          height="200px"
          autoplay={false}
          onChange={(index) => {
            this.currentCarouselIndex = index;
            this.drawWeatherWarningOverlay();
            this.drawWeatherOverlay();
          }}
        >
          {this.weatherWarningList.length > 0 ? weatherWarning : null}
          <el-carousel-item>
            <div style="display: flex; justify-content: center;margin-top:15px;">
              <p class="storage-capacity"></p>
              <el-tooltip effect="dark" content=" 温州国家气象站 （58659）" placement="top">
              <p style=" padding: 0 6px; color: #1cfff5; font-size: 20px; font-weight: 500;" > 温州市区 </p>
              </el-tooltip>
              <p class="storage-capacity-sty"></p>
            </div>
            {weatherInfo}
          </el-carousel-item>
        </el-carousel>
      </PanelItem>
    );
  },
  methods: {
    ...mapActions(["getWeatherForecast"]),
    /**
     * 预警页
     */
    renderWeatherWarning() {
      return (
        <el-carousel-item>
          <el-carousel height="200px" indicator-position="none" interval={3000}>
            {this.weatherWarningList.map((item) => {
              return (
                <el-carousel-item>
                  <el-row>
                    <el-col span={5}>
                      <img
                        src={item.imgSrc}
                        style="width: 80px; height: 72px;margin-top:16px"
                      />
                    </el-col>
                    <el-col span={19}>
                      <p class="warning-style" title={item.WARN_CONTENT}>
                        {item.WARN_CONTENT}
                      </p>
                    </el-col>
                  </el-row>
                </el-carousel-item>
              );
            })}
          </el-carousel>
        </el-carousel-item>
      );
    },
    /**
     * 天气实况页
     */
    renderWeatherInfo() {
      let thermometer = this.renderThermometer();
      let weather = this.renderWeatherDetail();
      let sunAndWind = this.renderSunAndWind();
      return (
        <div class="weather-info">
          {thermometer}
          <div class="weather-info__content">
            <div class="weather-info__left">{weather}</div>
            <div class="weather-info__right">{sunAndWind}</div>
          </div>
        </div>
      );
    },
    renderThermometer() {
      const maxTemperature = 50;
      let height = Math.floor(
        (this.weatherInfo.currentTemperature / maxTemperature) * 100
      );
      let tempStyle = { height: `${height}px`, marginTop: `${100 - height}px` };
      return (
        <div class="thermometer pl-4">
          <div class="thermometer-wrapper">
            <div class="thermometer-label">CF</div>
            <div class="thermometer-bar">
              <div class="thermometer-bar__content" style={tempStyle} />
            </div>
            <div class="thermometer-bottom" />
          </div>
        </div>
      );
    },
    renderWeatherDetail() {
      let weatherInfo = this.wenzhouWeatherForecast;
      return (
        <div class="pa-8 pr-0 flexbox flex-wrap">
          <el-tooltip effect="dark" content="当前温度" placement="top" offset={80}>
            <div class="w-100 ft-s-22 my-6">{weatherInfo.currentTemperature || "-"}℃</div>
          </el-tooltip>
          <el-tooltip effect="dark" content="相对湿度" placement="top">
            <div class="w-50 my-4 flexbox">
              <i class="icon-waether-pre-pro mr-2" style="vertical-align: middle" />
              {weatherInfo.humidity || "-"}%
            </div>
          </el-tooltip>
          <el-tooltip effect="dark" content="过去1小时降水" placement="top">
            <div class="w-50 my-4 flexbox">
              <i class="icon-weather-pre mr-2" style="vertical-align: middle" />
              {weatherInfo.rain || "-"}mm
            </div>
          </el-tooltip>
          <el-tooltip effect="dark" content="气压" placement="top">
            <div class="w-50 my-4 flexbox">
              <i class="icon-weather-air-pre mr-2" style="vertical-align: middle" />
              {weatherInfo.airPressure || "-"}hPa
            </div>
          </el-tooltip>
          <el-tooltip effect="dark" content="能见度" placement="top">
            <div class="w-50 my-4 flexbox">
              <i class="icon-weather-visibility mr-2" style="vertical-align: middle" />
              {weatherInfo.visibility || "-"}m
            </div>
          </el-tooltip>
        </div>
      );
    },
    renderSunAndWind() {
      let weatherInfo = this.wenzhouWeatherForecast;
      return (
        <div class="sun-wind">
          <div class="semicircle">
            <div class="wind-speed">{weatherInfo.currentWindSpeed || 0}m/s</div>
            <div>{weatherInfo.winds || ""}</div>
          </div>
          <div class="sun-info">
            <el-tooltip effect="dark" content="日出时间" placement="bottom">
              <span>{weatherInfo.sunrise || ""}</span>
            </el-tooltip>
            <el-tooltip effect="dark" content="日落时间" placement="bottom">
              <span>{weatherInfo.sunset || ""}</span>
            </el-tooltip>
          </div>
          <div class="decoration">
            <div class="windmill-1" />
            <div class="windmill-2" />
          </div>
        </div>
      );
    },
    // 非渲染方法 ↓↓↓↓↓
    /** 气象预警 */
    handlePointButtonClick() {
      this.actionPointBtnActive = !this.actionPointBtnActive;
      this.drawWeatherWarningOverlay();
      this.drawWeatherOverlay();
    },
    getWeatherWarning() {
      disasterPreventionApi.weatherWarning().then((res) => {
        if (res.code == 0) {
          this.weatherWarningList = res.data;
          this.weatherWarningList.map((item) => {
            let type = item.WARN_TYPE + item.WARN_LEVEL;
            item.imgSrc = this.getWeatherWarningIcon(type);
          });
        }
      });
    },
    getWeatherInfo() {
      disasterPreventionApi.wholeWeather().then((res) => {
        this.weatherInfoList = res.data;
        let wenzhouInfo = res.data.find((item) => item.areaName == "温州市");
        if (wenzhouInfo) {
          this.weatherInfo = wenzhouInfo;
        }
      });
    },
    getWeatherWarningIcon(name) {
      let imgSrc = "";
      switch (name) {
        case "暴雨橙色":
          {
            imgSrc = require("@/assets/images/warns/heavy-rain-orange.png");
          }
          break;
        case "暴雨红色":
          {
            imgSrc = require("@/assets/images/warns/heavy-rain-red.png");
          }
          break;
        case "暴雨黄色":
          {
            imgSrc = require("@/assets/images/warns/heavy-rain-yellow.png");
          }
          break;
        case "暴雨蓝色":
          {
            imgSrc = require("@/assets/images/warns/heavy-rains-blue.png");
          }
          break;
        case "大风橙色":
          {
            imgSrc = require("@/assets/images/warns/the-wind-orange.png");
          }
          break;
        case "大风黄色":
          {
            imgSrc = require("@/assets/images/warns/the-wind-yellow.png");
          }
          break;
        case "大风蓝色":
          {
            imgSrc = require("@/assets/images/warns/the-wind-blue.png");
          }
          break;
        case "干旱橙色":
          {
            imgSrc = require("@/assets/images/warns/dry-orange.png");
          }
          break;
        case "雷电橙色":
          {
            imgSrc = require("@/assets/images/warns/lightning-orange.png");
          }
          break;
        case "雷电黄色":
          {
            imgSrc = require("@/assets/images/warns/lightning-yellow.png");
          }
          break;
        case "雷雨大风橙色":
          {
            imgSrc = require("@/assets/images/warns/thunderstorm-gale-orange.png");
          }
          break;
        case "沙尘暴橙色":
          {
            imgSrc = require("@/assets/images/warns/dust-storm-orange.png");
          }
          break;
        default: {
          imgSrc = require("@/assets/images/warns/warning-remind.png");
        }
      }
      return imgSrc;
    },
    getWeatherIcon(weatherName) {
      let icon = "";
      let qingRegex = /^晴$|转晴/;
      let yinRegex = /^多云$|^阴$|转多云|转阴/;
      let yuRegex = /雨$|/;
      if (qingRegex.exec(weatherName)) {
        icon = "icon-weather-sunny";
      } else if (yinRegex.exec(weatherName)) {
        icon = "icon-weather-cloudy";
      } else if (yuRegex.exec(weatherName)) {
        icon = "icon-weather-rain";
      }
      return icon;
    },
    drawWeatherOverlay() {
      if (
        !this.actionPointBtnActive ||
        (this.weatherWarningList.length > 0 && this.currentCarouselIndex == 0)
      ) {
        this.ScreenMap.addWeatherOverlay([]);
        return;
      }
      /** 区县中心点经纬度 */
      const centerPointMap = {
        鹿城区: [120.55389798474596, 28.068232997940203],
        龙湾区: [120.81894315076158, 27.89931820301833],
        瓯海区: [120.54703152966783, 27.963862880752703],
        洞头区: [121.03317654919908, 27.919917568252703],
        永嘉县: [120.66376126599596, 28.337398037002703],
        平阳县: [120.36143482403382, 27.64214415802697],
        苍南县: [120.44959860797451, 27.440954047283753],
        文成县: [120.00699559080968, 27.82237370619345],
        泰顺县: [119.85989517628137, 27.524267556397135],
        瑞安市: [120.43658087217555, 27.839296762732104],
        乐清市: [121.01717188880941, 28.276692685577352],
        温州市: [120.54195024899947, 28.00796028614328],
      };
      let weatherInfoList = this.weatherForecast.map((item) => {
        let ret = {
          areaName: item.areaName == "温州市" ? "温州市区" : item.areaName,
          temperature: item.currentTemperature,
          weather: item.currentWeather || "小雨",
          icon: this.getWeatherIcon(item.currentWeather),
          coodinate: centerPointMap[item.areaName],
          humidity: item.humidity,
          rain: item.rain,
          airPressure: item.airPressure,
          visibility: item.visibility,
          currentWindSpeed: item.currentWindSpeed,
          winds: item.winds,
        };
        return ret;
      });
      this.ScreenMap.addWeatherOverlay(weatherInfoList);
    },
    drawWeatherWarningOverlay() {
      if (!this.actionPointBtnActive || this.currentCarouselIndex == 1) {
        this.ScreenMap.drawPoint([], {});
        return;
      }
      let weatherWarningList = this.weatherWarningList.map((item, index) => {
        let type = item.WARN_TYPE + item.WARN_LEVEL;
        return {
          lat: item.LAT,
          lng: item.LON,
          name: `${item.WARN_TYPE}${item.WARN_LEVEL}预警`,
          label: item.WARN_CONTENT,
          src: this.getWeatherWarningIcon(type),
          // no_pointermove: true,
          type: "气象预警",
        };
      });
      let style = { scale: 0.7 };
      this.ScreenMap.drawPoint(weatherWarningList, style);
    },
  },
};
</script>

<style lang="scss" scoped>
$color: #73808d;
$repeatingLinearGrd1: repeating-linear-gradient(
  180deg,
  $color 0,
  $color 1px,
  transparent 0,
  transparent 10px
);
$repeatingLinearGrd2: repeating-linear-gradient(
  180deg,
  $color 0,
  $color 1px,
  transparent 0,
  transparent 5px
);
.panel-meteorological {
  ::v-deep .el-carousel__indicators--horizontal {
    .el-carousel__indicator {
      &.is-active button {
        background-color: #1ef8f2;
      }
    }
  }
  ::v-deep.el-carousel__button {
    padding: 0;
    width: 9px;
    height: 9px;
    border-radius: 50%;
  }
}
.storage-capacity {
  margin-top: 9px;
  background-image: linear-gradient(
    64deg,
    rgba(0, 193, 255, 0) 5%,
    #00c1ff 100%
  );
  width: 35%;
  height: 2px;
}
.storage-capacity-sty {
  background-image: linear-gradient(
    64deg,
    #00c1ff 5%,
    rgba(0, 193, 255, 0) 100%
  );
  margin-top: 9px;
  width: 35%;
  height: 2px;
}
.warning-style {
  font-size: 18px;
  color: #ffffff;
  padding: 0 8px;
  padding-top: 16px;
  line-height: 24px;
  font-weight: 400;
  height: 160px;
  overflow: hidden;
  text-overflow: ellipsis;
}
.rainfall-style {
    color: #fff;
    width: 100%;
    margin: 12px 0;
    display: flex;
    font-size: 18px;
    padding: 10px;
    justify-content: center;
    background: url(~assets/images/WaterDisastersImage/rainfall-bg.png) no-repeat top center;
    background-size: 100% 100%;
    > p:nth-child(1) {
      margin-right: 20px;
      color: #1cfff5;
    }
  }
.weather-info {
  display: flex;
  color: #fff;
  margin-top: 0.5rem;
  .weather-info__content {
    display: flex;
    flex-wrap: wrap;
    .weather-info__title {
      text-align: center;
      width: 100%;
      color: #1cfff5;
      font-size: 20px;
      font-weight: 500;
      position: relative;
      &:before {
        content: "";
        position: absolute;
        height: 2px;
        width: 30%;
        left: 10%;
        top: 50%;
        background-image: linear-gradient(to left, #00c1ff, #00c1ff00);
      }
      &:after {
        content: "";
        position: absolute;
        height: 2px;
        width: 30%;
        right: 10%;
        top: 50%;
        background-image: linear-gradient(to right, #00c1ff, #00c1ff00);
      }
    }
    .weather-info__left {
      width: 60%;
      display: flex;
    }
    .weather-info__right {
      width: 40%;
      display: flex;
      justify-content: center;
      position: relative;
      &::before {
        content: "";
        position: absolute;
        left: 0;
        top: 5%;
        height: 90%;
        border-left: 1px solid #00c1ff;
      }
    }
  }
}
.thermometer {
  user-select: none;
  display: inline;
  .thermometer-wrapper {
    margin-top: 20px;
    width: 32px;
    height: 110px;
    background-image: $repeatingLinearGrd1, $repeatingLinearGrd2;
    background-position: 0 6px, 50% 6px;
    background-size: 100% 65%, 50% 65%;
    background-repeat: no-repeat;
    position: relative;
    .thermometer-label {
      position: absolute;
      top: -15px;
      font-size: 12px;
      letter-spacing: 18px;
      color: $color;
    }
    .thermometer-bar {
      height: 100px;
      width: 8px;
      left: 12px;
      border-radius: 10px;
      position: absolute;
      background: #fff;
      .thermometer-bar__content {
        box-sizing: border-box;
        border: 1px solid #fff;
        width: 100%;
        background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
        border-radius: 8px;
      }
    }
    .thermometer-bottom {
      position: absolute;
      bottom: 3px;
      width: 18px;
      height: 18px;
      left: 7px;
      border-radius: 50%;
      border: 1px solid #fff;
      background-image: linear-gradient(180deg, #ffffff 0%, #36c0e4 49%, #083dfe 99%);
      border: 1px solid #ffffff;
      box-shadow: inset 0 2px 3px 0 #002531;
    }
  }
}
.sun-wind {
  margin-top: 48px;
  user-select: none;
  position: relative;
  .semicircle {
    width: 117px;
    height: 47px;
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    background-image: url(~assets/images/WaterDisastersImage/semicircle.png);
    background-size: 100%;
    background-repeat: no-repeat;
    margin: 0 15px;
    padding-top: 8px;
    .wind-speed {
      font-size: 20px;
    }
  }
  .sun-info {
    margin-top: 8px;
    font-size: 14px;
    width: 135px;
    display: flex;
    justify-content: space-between;
  }
  .decoration {
    position: absolute;
    display: flex;
    top: 0;
    top: -25px;
    align-items: flex-end;
    .windmill-1 {
      width: 18px;
      height: 48px;
      background-image: url(~assets/images/WaterDisastersImage/windmill-1.gif);
      background-size: 100%;
      background-repeat: no-repeat;
    }
    .windmill-2 {
      width: 24px;
      height: 74px;
      background-image: url(~assets/images/WaterDisastersImage/windmill-2.gif);
      background-size: 100%;
      background-repeat: no-repeat;
    }
    .cloud {
      width: 24px;
      height: 74px;
      background-image: url(~assets/images/WaterDisastersImage/cloud.png);
      background-size: 100%;
      background-repeat: no-repeat;
      margin-left: 60px;
    }
  }
}
</style>
<style lang="scss" scoped>
// 原子类 懒得整合取名
.pa-8 {
  padding: 16px;
}
.pl-4 {
  padding-left: 8px;
}
.pr-0 {
  padding-right: 0;
}
.my-4 {
  margin-top: 8px;
  margin-bottom: 8px;
}
.my-6 {
  margin-top: 12px;
  margin-bottom: 12px;
}
.mr-2 {
  margin-right: 4px;
}
.flexbox {
  display: flex;
}
.flex-wrap {
  flex-wrap: wrap;
}
.ft-s-22 {
  font-size: 22px;
}
.w-100 {
  width: 100%;
}
.w-50 {
  width: 50%;
}
</style>
