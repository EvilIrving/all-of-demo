<script>
import PanelItem from '../components/PanelItem.vue';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention';
import { weatherApi } from '@/api/weatherApi';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'PanelWeatherInfo',
  layerName: ['weatherInfoLayer', 'windLayer', 'weatherLayer', 'maxPointLayer'],
  legendName: ['实时降雨量', '预报降雨量'],
  props: {},
  data() {
    return {
      forecastPreCardSelect: 0,
      forecastPreChartTabList: ['行政区划', '流域'],
      forecastChartData: {
        columns: ['name', 'min', 'max', 'avg'],
        rows: [
          { name: '永嘉县', min: 136, max: 178 },
          { name: '乐清市', min: 136, max: 178 },
          { name: '瓯海区', min: 136, max: 178 },
          { name: '鹿城区', min: 136, max: 178 },
          { name: '洞头区', min: 136, max: 178 }
        ]
      },
      forecastAreaRainfallChart: [],
      forecastBasinRainfallChart: [],
      forecastChartLoading: false,
      actionPointBtnActive: false,
      currentCarouselIndex: 0,
      //气象预警数据
      forecastPreList: {},
      weatherWarningList: [],
      weatherInfoList: [],
      weatherInfo: {
        airPressure: '1003.7',
        rain: '0.1',
        sunrise: '5:05',
        sunset: '18:41 ',
        visibility: '723',
        windPower: '0级',
        humidity: '',
        currentWindSpeed: '',
        currentTemperature: 21.8,
        winds: ''
      },
      timer: null,
      type: '',
      pointList: [], // 气象落点
      tabsIndex: -1,
      forecastPreMaxList: {},
      stormTideData: {},
      forecastRainfallList: [],
    };
  },
  computed: {
    weatherOption() {
      let list = [this.forecastPreList.rainfall_1, this.forecastPreList.rainfall_3, this.forecastPreList.rainfall_6];
      return [
        // { label: '天气实况', index: 0 },
        { label: '潮位预警', index: 1 },
        { label: '气象监测', index: 2 },
        { label: '风力分布', index: 3 },
        {
          label: '降雨1小时',
          index: 4,
          data: { number: list[0], unit: 'mm' }
        },
        {
          label: '降雨3小时',
          index: 5,
          data: { number: list[1], unit: 'mm' }
        },
        {
          label: '降雨6小时',
          index: 6,
          data: { number: list[2], unit: 'mm' }
        }
      ];
    },
    ...mapGetters(['ScreenMap', 'wenzhouWeatherForecast', 'weatherForecast'])
  },
  watch: {
    // weatherWarningList(val) {
    //   this.SetWeatherWarnNumber(val.length);
    // },
  },
  mounted() {
    this.getWeatherWarning();
    this.getRainfallData();
    this.getWeatherForecast();
    this.stormTideSelect();
    this.getForecastPreMaxValue();
    this.timer = setInterval(() => {
      this.getWeatherWarning();
      this.getWeatherForecast();
      this.getRainfallData();
      this.stormTideSelect();
      this.getForecastPreMaxValue();
    }, 300000);
  },
  beforeDestroy() {
    this.ScreenMap.removeOverlayByName('weather-popup-');
    clearInterval(this.timer);
    this.removelayer();
  },
  render() {
    let weatherInfo = this.renderWeatherInfo();
    let weatherWarning = this.renderWeatherWarning();
    let titleList = ['潮位气象预警', '天气实况'];
    let interval = 5000;
    let yinzhangClass = [this.actionPointBtnActive ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty'];
    return (
      <PanelItem
        title={this.weatherWarningList.length > 0 ? titleList[this.currentCarouselIndex] : '天气实况'}
        hasSwitch
        tc
        onTclick={this.tclick}
        class="panel-meteorological"
      >
        <template slot="title-switch">
          <el-popover append-to-body={false} placement="bottom" width="60" trigger="manual" v-model={this.actionPointBtnActive}>
            <ul>
              {this.weatherOption.map((item, index) => {
                return (
                  <li
                    style="cursor: pointer"
                    class={index == this.tabsIndex ? 'actives' : ''}
                    onClick={(e) => this.handleYear(item, index, e)}
                  >
                    {item.label}
                  </li>
                );
              })}
            </ul>
            <i slot="reference" onClick={(e) => this.handlePointBtnActive(e)} class={yinzhangClass} />
          </el-popover>
        </template>

        <el-carousel
          trigger="click"
          arrow="never"
          height="100%"
          autoplay={false}
          onChange={(index) => {
            this.currentCarouselIndex = index;
            this.drawWeatherWarningOverlay();
            this.drawWeatherOverlay();
          }}
        >
          {this.weatherWarningList.length > 0 ? weatherWarning : null}
          <el-carousel-item class="city-weather">
            <div style="display: flex; justify-content: center;margin-top:12px;">
              <p class="storage-capacity"></p>
              <el-tooltip effect="dark" content=" 温州国家气象站 （58659）" placement="top">
                <p style=" padding: 0 6px; color: #1cfff5; font-size: 20px; font-weight: 500;"> 温州市区 </p>
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
    ...mapActions([
      'getWeatherForecast',
      'SetWeatherWarnNumber',
      'changeScreenLoading',
      'getForecastRainfallLayer',
      'SetDetailDialogParam',
      'SetForecastPointFlag',
      'SetRangeBar',
      'SetTimelineList'
    ]),
    async stormTideSelect() {
      let res = await weatherApi.stormTideSelect();
      console.log(res);
      if (res.code == 0) {
        this.stormTideData = res.data;
      }
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
    },
    handlePointBtnActive(event) {
      event.stopPropagation();
      this.actionPointBtnActive = !this.actionPointBtnActive;
      if (!this.actionPointBtnActive) {
        this.tabsIndex = -1;
        this.removelayer();
      }
      // this.drawWeatherWarningOverlay();
      // this.drawWeatherOverlay();
    },
    /** 初始化雨量数据 */
    getRainfallData() {
      disasterPreventionApi.predictRainfallByAreaName().then((res) => {
        if (res.code == 0) {
          this.forecastPreList = res.data;
        }
      });
    },
    /**
     * 预警页
     */
    renderWeatherWarning() {
      return (
        <el-carousel-item>
          <el-carousel height="200px" indicator-position="none">
            {this.weatherWarningList.map((item) => {
              return (
                <el-carousel-item>
                  <el-row>
                    <el-col span={5}>
                      <img src={item.imgSrc} style="width: 80px; height: 72px;margin-top:16px" />
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
      let weatherInfo = this.wenzhouWeatherForecast;
      let thermometer = this.renderThermometer();
      let weather = this.renderWeatherDetail();
      let sunAndWind = this.renderSunAndWind();

      return (
        <div>
          <div class="weather-info">
            {thermometer}
            <div class="weather-info__content">
              <div class="weather-info__left">{weather}</div>
            </div>
          </div>
          <div class="weather-info__right">
            <div class="wind-info">
              <div class="wind-type">
                {weatherInfo.winds || ''}({weatherInfo.currentWindSpeed || 0}m/s)
              </div>
              <div class="wind-speed">
                <span>{weatherInfo.sunrise || ''}am </span>
                <span>{weatherInfo.sunset || ''}pm</span>
              </div>
            </div>
            {sunAndWind}
          </div>
        </div>
      );
    },
    renderThermometer() {
      const maxTemperature = 50;
      let height = Math.floor((this.weatherInfo.currentTemperature / maxTemperature) * 100);
      let tempStyle = { height: `${height}px`, marginTop: `${68 - height}px` };
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
            <div style="color:#1EF8F2;" class="w-100 ft-s-22">
              {weatherInfo.currentTemperature || '-'}℃
            </div>
          </el-tooltip>
          <el-tooltip effect="dark" content="相对湿度" placement="top">
            <div class="w-50 my-4 flexbox">
              <i class="icon-waether-pre-pro mr-2" style="vertical-align: middle" />
              {weatherInfo.humidity || '-'}%
            </div>
          </el-tooltip>
          <el-tooltip effect="dark" content="过去1小时降水" placement="top">
            <div class="w-50 my-4 flexbox">
              <i class="icon-weather-pre mr-2" style="vertical-align: middle" />
              {weatherInfo.rain || '-'}mm
            </div>
          </el-tooltip>
          <el-tooltip effect="dark" content="气压" placement="top">
            <div class="w-50 my-4 flexbox">
              <i class="icon-weather-air-pre mr-2" style="vertical-align: middle" />
              {weatherInfo.airPressure || '-'}hPa
            </div>
          </el-tooltip>
          <el-tooltip effect="dark" content="能见度" placement="top">
            <div class="w-50 my-4 flexbox">
              <i class="icon-weather-visibility mr-2" style="vertical-align: middle" />
              {weatherInfo.visibility || '-'}m
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
            <div class="wind-type">{weatherInfo.winds || ''}</div>
            <div class="wind-speed">{weatherInfo.currentWindSpeed || 0}m/s</div>
          </div>
          <div class="sun-info">
            <el-tooltip effect="dark" content="日出时间" placement="bottom">
              <span>{weatherInfo.sunrise || ''}</span>
            </el-tooltip>
            <el-tooltip effect="dark" content="日落时间" placement="bottom">
              <span>{weatherInfo.sunset || ''}</span>
            </el-tooltip>
          </div>
          <div class="decoration">
            <div class="windmill-1" />
            <div class="windmill-2" />
          </div>
        </div>
      );
    },
    removelayer() {
      this.ScreenMap.removeOverlayByName('weather-popup-');
      this.ScreenMap.drawPoint([], {}, 'weatherTyphoonInfoLayer');
      this.ScreenMap.drawPoint([], {}, 'weatherTemperateInfoLayer');
      // this.ScreenMap.drawPoint([], {}, 'weatherInfoLayer');
      this.ScreenMap.drawPoint([], {}, 'weatherLayer');
      this.ScreenMap.drawPoint([], {}, 'windLayer');
      this.ScreenMap.loadRainfallPoint([], false, true);
      this.$emit('legend-change', '实时降雨量', {});
      this.$emit('legend-change', '预报降雨量', {});
    },
    // 非渲染方法 ↓↓↓↓↓
    // 下拉选择 气象预警筛选项
    handleYear(item, index, event) {
      event.stopPropagation();
      this.tabsIndex = index;
      let name = item.label;
      this.removelayer();
      switch (name) {
        case '天气实况':
          this.drawWeatherOverlay();
          break;
        case '潮位预警':
          this.drawWeatherWarningOverlay();
          break;
        case '气象监测':
          this.radioChange('气象监测');
          break;
        case '风力分布':
          this.radioChange('风力分布');
          break;
        case '降雨1小时':
          this.precipitationCardClick(1); // “短临预报”组件落点
          break;
        case '降雨3小时':
          this.precipitationCardClick(3);
          break;
        case '降雨6小时':
          this.precipitationCardClick(6);
          break;
        default:
          break;
      }
    },
    /** 选中1，3，6小时降雨卡片降切换 */
    precipitationCardClick(val) {
      console.log(val, '5265+65+65655+');
      if (val == null) {
        this.ScreenMap.removeLayerByName('rainfall');
        this.ScreenMap.clearTextLayer();
        this.$emit('legend-change', '实时降雨量', {});
        this.$emit('legend-change', '预报降雨量', {});
        return;
      }
      // let valMap = [1, 3, 6];
      this.cardSelect = 'forecast';
      let data = this.forecastRainfallList.map(item => {
        if(val == 1) {
          item['drp'] = item.data060
        } else if(val == 3) {
          item['drp'] = item.data180
        } else {
          item['drp'] = item.data360
        }
        return item
      });
      if (data) {
        this.ScreenMap.loadRainfallPoint(data, true, true);
      }
      this.$emit('legend-change', '实时降雨量', {});
      this.$emit('legend-change', '预报降雨量', {
        label: '预报降雨量',
        children: [
          {
            label: '>70',
            icon: 'forecast-100'
          },
          { label: '50~70', icon: 'forecast-70' },
          { label: '20~50', icon: 'forecast-50' },
          { label: '10~20', icon: 'forecast-25' },
          { label: '3~10', icon: 'forecast-10' },
          { label: '1~3', icon: 'forecast-1' },
          { label: '<1', icon: 'forecast-0' }
        ]
      });
    },
    /** 获得预报极值数据 */
    getForecastPreMaxValue() {
      disasterPreventionApi.forecastRainfall().then((res) => {
        this.forecastRainfallList = res.data;
      });
    },
    /** 短临预报降雨图表tabs切换 */
    forecastChartTabChange(index, hour) {
      hour = hour || [1, 3, 6][this.forecastPreCardSelect];
      let tab = this.forecastPreChartTabList[index];
      let data = {
        行政区划: this.forecastAreaRainfallChart || [],
        流域: this.forecastBasinRainfallChart || []
      };
      if (!data[tab]) return;
      this.forecastChartData.rows = [];
      let rows = [];
      const sortData = data[tab].sort((a, b) => b[`rainfall_${hour}`] - a[`rainfall_${hour}`]);
      for (let item of sortData) {
        if (rows.length < 5) {
          rows.push({
            name: item.key,
            max: item[`${hour}hourMaxRainfall`],
            min: item[`${hour}hourMinRainfall`],
            avg: item[`rainfall_${hour}`]
          });
        } else break;
      }
      this.forecastChartData.rows = rows;
    },

    /** 气象预警 */
    // handlePointButtonClick() {
    //   this.actionPointBtnActive = !this.actionPointBtnActive;
    //   if(!this.actionPointBtnActive){
    //
    //   }
    //   this.drawWeatherOverlay();
    //   // this.drawWeatherWarningOverlay();
    // },
    getWeatherWarning() {
      disasterPreventionApi.weatherWarning().then((res) => {
        if (res.code == 0) {
          let arr = {
            blue: 0,
            yellow: 0,
            orange: 0,
            red: 0
          };
          this.weatherWarningList = res.data;
          this.weatherWarningList.map((item) => {
            let type = item.WARN_TYPE + item.WARN_LEVEL;
            item.imgSrc = this.getWeatherWarningIcon(type);
            arr.blue += item.WARN_LEVEL == '蓝色' ? 1 : 0;
            arr.yellow += item.WARN_LEVEL == '黄色' ? 1 : 0;
            arr.orange += item.WARN_LEVEL == '橙色' ? 1 : 0;
            arr.red += item.WARN_LEVEL == '红色' ? 1 : 0;
          });
          this.SetWeatherWarnNumber(arr);
        }
      });
    },
    getWeatherInfo() {
      disasterPreventionApi.wholeWeather().then((res) => {
        this.weatherInfoList = res.data;
        let wenzhouInfo = res.data.find((item) => item.areaName == '温州市');
        if (wenzhouInfo) {
          this.weatherInfo = wenzhouInfo;
        }
      });
    },
    getWeatherWarningIcon(name) {
      let imgSrc = '';
      switch (name) {
        case '暴雨橙色':
          {
            imgSrc = require('@/assets/images/warns/heavy-rain-orange.png');
          }
          break;
        case '暴雨红色':
          {
            imgSrc = require('@/assets/images/warns/heavy-rain-red.png');
          }
          break;
        case '暴雨黄色':
          {
            imgSrc = require('@/assets/images/warns/heavy-rain-yellow.png');
          }
          break;
        case '暴雨蓝色':
          {
            imgSrc = require('@/assets/images/warns/heavy-rains-blue.png');
          }
          break;
        case '大风橙色':
          {
            imgSrc = require('@/assets/images/warns/the-wind-orange.png');
          }
          break;
        case '大风黄色':
          {
            imgSrc = require('@/assets/images/warns/the-wind-yellow.png');
          }
          break;
        case '大风蓝色':
          {
            imgSrc = require('@/assets/images/warns/the-wind-blue.png');
          }
          break;
        case '干旱橙色':
          {
            imgSrc = require('@/assets/images/warns/dry-orange.png');
          }
          break;
        case '雷电橙色':
          {
            imgSrc = require('@/assets/images/warns/lightning-orange.png');
          }
          break;
        case '雷电黄色':
          {
            imgSrc = require('@/assets/images/warns/lightning-yellow.png');
          }
          break;
        case '雷雨大风橙色':
          {
            imgSrc = require('@/assets/images/warns/thunderstorm-gale-orange.png');
          }
          break;
        case '沙尘暴橙色':
          {
            imgSrc = require('@/assets/images/warns/dust-storm-orange.png');
          }
          break;
        default: {
          imgSrc = require('@/assets/images/warns/warning-remind.png');
        }
      }
      return imgSrc;
    },
    getWeatherIcon(weatherName) {
      let icon = '';
      let qingRegex = /^晴$|转晴/;
      let yinRegex = /^多云$|^阴$|转多云|转阴/;
      let yuRegex = /雨$|/;
      if (qingRegex.exec(weatherName)) {
        icon = 'icon-weather-sunny';
      } else if (yinRegex.exec(weatherName)) {
        icon = 'icon-weather-cloudy';
      } else if (yuRegex.exec(weatherName)) {
        icon = 'icon-weather-rain';
      }
      return icon;
    },
    drawWeatherOverlay() {
      if (!this.actionPointBtnActive || this.weatherWarningList.length) {
        this.ScreenMap.removeOverlayByName('weather-popup-');
        // this.ScreenMap.addWeatherOverlay([]);
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
        温州市: [120.54195024899947, 28.00796028614328]
      };
      let weatherInfoList = this.weatherForecast
        .map((item) => {
          let ret = {
            areaName: item.areaName,
            temperature: item.currentTemperature,
            weather: item.currentWeather || '小雨',
            icon: this.getWeatherIcon(item.currentWeather),
            coodinate: centerPointMap[item.areaName],
            humidity: item.humidity,
            rain: item.rain,
            airPressure: item.airPressure,
            visibility: item.visibility,
            currentWindSpeed: item.currentWindSpeed,
            winds: item.winds
          };
          return ret;
        })
        .filter((item) => {
          return item.areaName != '温州市区';
        });
      // this.ScreenMap.addWeatherOverlay(weatherInfoList);
      this.ScreenMap.addWeatherWarnOverlay(weatherInfoList, 'weather-popup-');
    },
    drawWeatherWarningOverlay() {
      if (!this.actionPointBtnActive) {
        this.ScreenMap.drawPoint([], {}, 'weatherTyphoonInfoLayer');
        this.ScreenMap.drawPoint([], {}, 'weatherTemperateInfoLayer');
        return;
      }
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        src: require('@/assets/images/legend/temperate-zone-icon-01.png')
      };
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        src: require('@/assets/images/legend/typhoon-icon-01.png')
      };
      if (this.stormTideData.typhoon) {
        if (this.stormTideData.typhoon.result == '蓝色') {
          style1.src = require('@/assets/images/legend/typhoon-icon-01.png');
        } else if (this.stormTideData.typhoon.result == '黄色') {
          style1.src = require('@/assets/images/legend/typhoon-icon-02.png');
        } else if (this.stormTideData.typhoon.result == '橙色') {
          style1.src = require('@/assets/images/legend/typhoon-icon-03.png');
        } else {
          style1.src = require('@/assets/images/legend/typhoon-icon-04.png');
        }
        let obj = {
          name: '台风风暴潮预警',
          type: '风暴潮预警',
          lng: 120.68561641357425,
          lat: 28.04623106285519,
          code: this.stormTideData.typhoon.urlId,
          stationCode: '1',
          props: {}
        };
        this.ScreenMap.drawPoint([obj], style1, 'weatherTyphoonInfoLayer');
      }
      if (this.stormTideData.temperate) {
        if (this.stormTideData.temperate.result == '蓝色') {
          style.src = require('@/assets/images/legend/temperate-zone-icon-01.png');
        } else if (this.stormTideData.temperate.result == '黄色') {
          style.src = require('@/assets/images/legend/temperate-zone-icon-02.png');
        } else if (this.stormTideData.temperate.result == '橙色') {
          style.src = require('@/assets/images/legend/temperate-zone-icon-03.png');
        } else {
          style.src = require('@/assets/images/legend/temperate-zone-icon-04.png');
        }
        let obj = {
          name: '温带风暴潮预警',
          type: '风暴潮预警',
          lng: 120.55016743896488,
          lat: 28.106206981284012,
          code: this.stormTideData.temperate.urlId,
          stationCode: '2',
          props: {}
        };
        this.ScreenMap.drawPoint([obj], style, 'weatherTemperateInfoLayer');
      }
      // let pondingList = [];
      // for (let point of this.pointList) {
      //   pondingList.push(obj);
      // }

      // let weatherWarningList = this.weatherWarningList.map((item, index) => {
      //   let type = item.WARN_TYPE + item.WARN_LEVEL;
      //   return {
      //     lat: item.LAT,
      //     lng: item.LON,
      //     name: `${item.WARN_TYPE}${item.WARN_LEVEL}预警`,
      //     label: item.WARN_CONTENT,
      //     src: this.getWeatherWarningIcon(type),
      //     type: '气象预警'
      //   };
      // });
      // console.log(weatherWarningList, 990);
      // let style = { scale: 0.7 };
      // this.ScreenMap.drawPoint(weatherWarningList, style, 'weatherInfoLayer');
    },
    weatherWindChange(val) {
      this.meteorological(); //onChange={this.weatherWindChange}
    },
    radioChange(data) {
      if (data == this.type) {
        this.type = '';
      } else {
        this.type = data;
      }
      // this.$nextTick(() => {
      //   this.$forceUpdate();
      // })
      if (this.type) {
        this.weatherWindChange();
      } else {
        this.ScreenMap.drawPoint([], {}, 'weatherLayer');
        this.ScreenMap.drawPoint([], {}, 'windLayer');
      }
    },
    // 气象监测落点
    getWeatherList() {
      this.ScreenMap.drawPoint([], {}, 'windLayer');
      let style = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require('@/assets/new_images/point/weather.png')
      };
      let pointList = this.pointList.map((point) => {
        let imgSrc = require('@/assets/new_images/point/weather.png');
        let obj = point.meteorological || {};
        if (obj.precipition == 'undefined') {
          obj.precipition = '-';
        }
        return {
          stationCode: point.stationCode,
          name: point.stationName,
          type: '气象监测',
          lng: point.longitude,
          lat: point.latitude,
          src: imgSrc,
          props: {
            风速: `${obj.windVelocity || '-'}m/s`,
            风向: `${point.windDirect || '-'}`,
            相对湿度: `${obj.relhumidity || '-'}%`,
            降水量: `${obj.precipition}mm`,
            气压: `${obj.stationPress || '-'}hPa`
          }
        };
      });
      this.ScreenMap.drawPoint(pointList, style, 'weatherLayer');
    },
    // 风力分布落点
    getWindList() {
      this.ScreenMap.drawPoint([], {}, 'weatherLayer');
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        src: require('@/assets/images/fengxiangbiao-1.png')
      };
      let pointList = this.pointList.map((point) => {
        let imgSrc = require('@/assets/images/fengxiangbiao-1.png');
        let rotation = 0;

        let obj = point.meteorological || {};

        if (obj.windVelocity < 0.2) {
          imgSrc = require('@/assets/images/fengxiangbiao-1.png');
        } else if (obj.windVelocity >= 0.3 && obj.windVelocity <= 1.5) {
          imgSrc = require('@/assets/images/fengxiangbiao-2.png');
        } else if (obj.windVelocity >= 1.6 && obj.windVelocity <= 3.3) {
          imgSrc = require('@/assets/images/fengxiangbiao-3.png');
        } else if (obj.windVelocity >= 3.4 && obj.windVelocity <= 5.4) {
          imgSrc = require('@/assets/images/fengxiangbiao-4.png');
        } else if (obj.windVelocity >= 5.5 && obj.windVelocity <= 7.9) {
          imgSrc = require('@/assets/images/fengxiangbiao-5.png');
        } else if (obj.windVelocity >= 8.0 && obj.windVelocity <= 10.7) {
          imgSrc = require('@/assets/images/fengxiangbiao-6.png');
        } else if (obj.windVelocity >= 10.8 && obj.windVelocity <= 13.8) {
          imgSrc = require('@/assets/images/fengxiangbiao-7.png');
        } else if (obj.windVelocity >= 13.9 && obj.windVelocity <= 17.1) {
          imgSrc = require('@/assets/images/fengxiangbiao-8.png');
        } else if (obj.windVelocity >= 17.2 && obj.windVelocity <= 20.7) {
          imgSrc = require('@/assets/images/fengxiangbiao-9.png');
        } else if (obj.windVelocity >= 20.8 && obj.windVelocity <= 24.4) {
          imgSrc = require('@/assets/images/fengxiangbiao-10.png');
        } else if (obj.windVelocity >= 24.5 && obj.windVelocity <= 28.4) {
          imgSrc = require('@/assets/images/fengxiangbiao-11.png');
        } else if (obj.windVelocity >= 28.5 && obj.windVelocity <= 32.6) {
          imgSrc = require('@/assets/images/fengxiangbiao-12.png');
        } else if (obj.windVelocity >= 32.7 && obj.windVelocity <= 36.9) {
          imgSrc = require('@/assets/images/fengxiangbiao-13.png');
        }

        switch (point.windDirect) {
          case '东风':
            rotation = 145;
            break;
          case '东北风':
            rotation = 270;
            break;
          case '北风':
            rotation = 200;
            break;
          case '西北风':
            rotation = 300;
            break;
          case '西风':
            rotation = 10;
            break;
          case '西南风':
            rotation = 185;
            break;
          case '南风':
            rotation = 178;
            break;
          case '东南风':
            rotation = 20;
            break;
        }

        return {
          name: point.stationName,
          lng: point.longitude,
          lat: point.latitude,
          src: imgSrc,
          rotation: rotation,
          props: {
            风速: `${obj.windVelocity || '-'}m/s`,
            风向: `${point.windDirect || '-'}`
          }
        };
      });
      this.ScreenMap.drawPoint(pointList, style, 'windLayer');
    },
    // 获取风力气象落点
    async meteorological() {
      await weatherApi.meteorological().then((res) => {
        this.pointList = res.data;
      });
      if (this.type == '气象监测') {
        this.getWeatherList();
      } else {
        this.getWindList();
      }
    }
  }
};
</script>
<style lang="scss">
.el-popover {
  background: #244778;
  // background: linear-gradient(180deg, rgba(53, 185, 227, 0.1) 0%, rgba(25, 60, 197, 0) 100%);
  box-shadow: 0px 4px 30px 0px rgba(35, 81, 239, 0.25);
  border: 2px solid #244778;
  color: #fff;
  min-width: 120px;
  font-size: 14px;
  ul > li {
    font-size: 14px;
    padding-top: 5px;
    color: #00baff;
    &.actives {
      color: #fff;
    }
  }
}
.el-popper[x-placement^='bottom'] .popper__arrow::after {
  top: 0.0625rem;
  margin-left: -0.375rem;
  border-top-width: 0;
  border-bottom-color: #244778;
}
</style>
<style lang="scss" scoped>
$color: #73808d;
$repeatingLinearGrd1: repeating-linear-gradient(180deg, $color 0, $color 1px, transparent 0, transparent 10px);
$repeatingLinearGrd2: repeating-linear-gradient(180deg, $color 0, $color 1px, transparent 0, transparent 5px);
::v-deep .el-carousel {
  height: 100%;
}
.city-weather {
  height: 100%;
  overflow: auto;
  @include flexbox;
  @include flexflow(column, nowrap);
  &::-webkit-scrollbar {
    display: none;
  }
  // @include flexJC(space-around);
}
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
  background-image: linear-gradient(64deg, rgba(0, 193, 255, 0) 5%, #00c1ff 100%);
  width: 35%;
  height: 2px;
}
.storage-capacity-sty {
  background-image: linear-gradient(64deg, #00c1ff 5%, rgba(0, 193, 255, 0) 100%);
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
  width: 100%;
  padding: 8px;
  // margin-top: 0.5rem;
  .weather-info__content {
    display: flex;
    flex-wrap: wrap;
    width: 100%;
    padding: 0 16px;
    .weather-info__title {
      text-align: center;
      width: 100%;
      color: #1cfff5;
      font-size: 20px;
      font-weight: 500;
      position: relative;
      &:before {
        content: '';
        position: absolute;
        height: 2px;
        width: 30%;
        left: 10%;
        top: 50%;
        background-image: linear-gradient(to left, #00c1ff, #00c1ff00);
      }
      &:after {
        content: '';
        position: absolute;
        height: 2px;
        width: 30%;
        right: 10%;
        top: 50%;
        background-image: linear-gradient(to right, #00c1ff, #00c1ff00);
      }
    }
    .weather-info__left {
      width: 100%;
      display: flex;
    }
  }
}
.weather-info__right {
  display: flex;
  position: relative;
  @include flexflow(row, nowrap);
  &::before {
    content: '';
    position: absolute;
    left: 0;
    top: 5%;
    height: 90%;
    // border-left: 1px solid #00c1ff;
  }
  .wind-info {
    width: 50%;
    height: 100%;
    padding: 0 20px;
    .wind-type {
      font-size: 24px;
      color: #1ef8f2;
      letter-spacing: 0;
      line-height: 28px;
      padding-bottom: 8px;
    }
    ::v-deep .el-radio__label {
      color: #fff;
      font-size: 18px;
      line-height: 28px;
    }
    ::v-deep .is-checked .el-radio__label {
      color: #1ef8f2;
    }
    ::v-deep .is-checked .el-radio__inner {
      border: 1px solid #1ef8f2;
      background: none;
    }
    ::v-deep .el-radio__inner {
      border: 1px solid #fff;
      background: none;
    }
    ::v-deep .is-checked .el-radio__inner::after {
      width: 8px;
      height: 8px;
      border-radius: 100%;
      background-color: #1ef8f2;
    }
  }
}
.thermometer {
  user-select: none;
  display: inline;
  .thermometer-wrapper {
    margin-top: 20px;
    width: 32px;
    height: 78px;
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
      height: 68px;
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
.icon-sty {
  outline: 0 !important;
  cursor: pointer;
}
.sun-wind {
  // margin-top: 48px;
  user-select: none;
  position: relative;
  width: 50%;
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
    margin: 0 auto;
    padding-top: 8px;

    .wind-speed {
      font-size: 20px;
    }
  }
  .sun-info {
    margin-top: 8px;
    margin-left: 36px;
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
  padding: 8px;
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
