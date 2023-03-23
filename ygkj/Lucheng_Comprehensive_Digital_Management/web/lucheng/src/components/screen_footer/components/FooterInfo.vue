<script>
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
import { PrimaryWindow } from "@/components/";
import { mapActions, mapGetters } from "vuex";

export default {
  name: "FooterInfo",
  props: { popover: { type: Boolean, default: false } },
  data() {
    return {
      weatherInfo: {},
      forecastInfo: {},
      currentAreaWeather: {},
      currentAreaForecast: [],
      areaList: [
        "温州市",
        "鹿城区",
        "瓯海区",
        "龙湾区",
        "洞头区",
        "瑞安市",
        "乐清市",
        "龙港市",
        "泰顺县",
        "文成县",
        "永嘉县",
        "苍南县",
        "平阳县",
        "温州市区",
      ],
    };
  },
  mounted() {
    this.getWeatherInfo().then(() => {
      this.areaList.forEach(async (item) => {
        await this.getWeatherInfo(item);
      });
    });
  },
  computed: { ...mapGetters(["currentSelectArea", "weatherPopover"]) },
  watch: {
    currentSelectArea: {
      immediate: true,
      handler: function (val, oldVal) {
        this.currentAreaWeather = this.weatherInfo[val];
        this.currentAreaForecast = this.forecastInfo[val];
      },
    },
  },
  methods: {
    ...mapActions(["setWeatherPopover"]),
    /**
     * 获取天气信息
     */
    async getWeatherInfo(areaName = "温州市") {
      disasterPreventionApi.weatherForecast({ areaName }).then((res) => {
        let [weatherInfo, ...forecastInfo] = res.data;
        let outTime = this.getDays(7);
        this.weatherInfo[areaName] = weatherInfo;
        this.forecastInfo[areaName] = forecastInfo.map((item, i) => {
          let ret = {};
          ret.maxTemp = Math.floor(item.maxTemp);
          ret.minTemp = Math.floor(item.minTemp);
          ret.day = outTime[i];
          ret.icon = this.getWeatherIcon(item.wpname || item.wpName);
          return ret;
        });
        if (areaName == "温州市") {
          this.currentAreaWeather = this.weatherInfo["温州市"] || {};
          this.currentAreaForecast = this.forecastInfo["温州市"] || [];
        }
      });
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
    getDays(num) {
      //获取系统当前时间
      var leng = num;
      var now = new Date();
      var nowTime = now.getTime();
      var oneDayTime = 24 * 60 * 60 * 1000;
      var timeArr = [];
      for (var i = 0; i < leng; i++) {
        //显示星期
        var ShowTime = nowTime + i * oneDayTime;
        //初始化日期时间
        var myDate = new Date(ShowTime);
        var str = "周" + "一二三四五六日".charAt(myDate.getDay());
        timeArr.push(str);
      }
      return timeArr;
    },
    renderPopoverContent() {
      let forecastInfo = this.currentAreaForecast || new Array(5);
      return (
        <PrimaryWindow>
          <div class="weather-forecast">
            <div class="weather-forecast__title">
              <span class="weather-forecast__title--label" style="color:#fff;">
                {this.currentSelectArea}气象预报
              </span>
            </div>
            <el-row type="flex" class="weather-forecast__content">
              {forecastInfo.map((item) => {
                return (
                  <el-col span={4} class="forecast-item">
                    <div class="item-weekday">{item.day}</div>
                    <div class="item-icon">
                      <i class={item.icon} />
                    </div>
                    <div class="item-temp">
                      {item.minTemp}~{item.maxTemp}℃
                    </div>
                  </el-col>
                );
              })}
            </el-row>
          </div>
        </PrimaryWindow>
      );
    },
  },
  render() {
    let weatherInfo = this.currentAreaWeather || [];
    let currentTemper = () => {
      let temperture = (Number(weatherInfo.maxTemp) + Number(weatherInfo.minTemp)) / 2;
      return temperture.toFixed(1);
    };
    let weatherIcon = this.getWeatherIcon(weatherInfo.wpname);
    let popoverContent = this.renderPopoverContent();
    let windDir =
      (weatherInfo.windDirections != "--风" && weatherInfo.windDirections) ||
      (weatherInfo.nWindDirect != "--风" && weatherInfo.nWindDirect) ||
      (weatherInfo.dWindDirect != "--风" && weatherInfo.dWindDirect) ||
      "无持续风向";
    return (
      <div class="map-footer__info">
        <el-popover
          value={this.weatherPopover}
          onInput={(value) => {
            this.setWeatherPopover(value);
          }}
          popperClass="weather-popover"
          placement="top"
          trigger="click"
          close-delay={0}
          visibleArrow={false}
        >
          {popoverContent}
          <template slot="reference">
            <div class="map-footer__info--weather">
              <i class={[weatherIcon, "icon-weather"]} />
              {this.currentSelectArea}&nbsp;
              {weatherInfo.wpname}&nbsp;
              {weatherInfo.minTemp}℃~{weatherInfo.maxTemp}℃
            </div>
          </template>
        </el-popover>

        <div class="map-footer__info--temp">{currentTemper}℃</div>
        <div class="map-fotter__info--wind">
          <i class="icon-footer-wind" />
          {windDir} {weatherInfo.windPower || "--"}
        </div>
      </div>
    );
  },
};
</script>
<style lang="scss" scoped>
.map-footer__info {
  user-select: none;
  flex: 1;
  height: 50px;
  display: flex;
  align-items: center;
  font-size: 18px;
  color: #fff;
  margin-top: 36px;
  z-index: 1;
  .map-footer__info--weather {
    cursor: pointer;
    margin-left: 32px;
    display: flex;
    align-items: center;
    .icon-weather {
      width: 24px;
      height: 24px;
      margin-right: 12px;
    }
  }
  .map-footer__info--temp {
    margin-left: 13px;
  }
  .map-fotter__info--wind {
    margin-left: 48px;
    display: flex;
    align-items: center;
    .icon-footer-wind {
      margin: 0 8px;
    }
  }
}
</style>
<style lang="scss">
.weather-popover {
  padding: 0;
  border: 0;
  background: unset;
  .weather-forecast {
    padding-top: 16px;
    .weather-forecast__title {
      width: 420px;
      height: 30px;
      background: url(~assets/images/WaterDisastersImage/title-bg.png) no-repeat;
      background-size: 100% 100%;
      display: flex;
      padding-left: 48px;
      align-items: center;
      color: #00c1ff;
      .weather-forecast__title--label {
        user-select: none;
        font-size: 18px;
        font-weight: bold;
        margin-right: 10px;
      }
    }
    .weather-forecast__content {
      .forecast-item {
        user-select: none;
        display: flex;
        flex-direction: column;
        align-items: center;
        margin: 8px 8px;
        .item-weekday {
          margin-bottom: 12px;
        }
        .item-icon {
          width: 24px;
          height: 24px;
          i {
            width: 100%;
            height: 100%;
          }
        }
        .item-temp {
          margin-top: 12px;
        }
      }
    }
  }
}
</style>
