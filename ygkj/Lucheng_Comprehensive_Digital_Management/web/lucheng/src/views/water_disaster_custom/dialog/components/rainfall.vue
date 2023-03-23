<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-11-08 16:47:11
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\components\rainfall.vue
-->
<template>
  <div class="rainfall">
    <div class="rainfall-title">
      <div>
        <span>{{rainfallInfo.currentTemperature || '-'}}</span>
        <span>℃</span>
      </div>
      <div>
        <div>
          <span class="icon wind-power-icon"></span>
          <span>风力：{{rainfallInfo.windPower || '-'}}</span>
        </div>
        <div>
          <span class="icon humidness-icon"></span>
          <span>湿度：{{rainfallInfo.humidity || '-'}}%</span>
        </div>
      </div>
    </div>
    <!-- <div>雨渐小，40分钟转为小雨，55分钟转为中雨</div> -->
    <div class="line-box">
      <ve-line
        :extend="chartWaterExtend"
        :colors="waterColors"
        :data="chartWaterData"
        :settings="chartWaterSettings"
        height="100%"
        width="100%"
        :grid="waterGrid"
      />
    </div>
    <div class="rainfall-tip">注：未来六小时降水预报是由雷达外推所得，仅供参考</div>
  </div>
</template>

<script>
import { weatherApi } from '@/api/weatherApi/';
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
export default {
  props: {
    lat: {
      type: String | Number,
      default: ''
    },
    lng: {
      type: String | Number,
      default: ''
    }
  },
  data() {
    return {
      rainfallData: {},
      rainfallInfo: {},
      title: '',
      waterColors: ['#32BCB4', '#19af47', 'red'],
      chartWaterData: {
        columns: ['时间', '雨量'],
        rows: []
      },
      chartWaterSettings: {
        showLine: ['雨量曲线'],
        area: true
      },
      waterGrid: {
        top: 30,
        bottom: 30,
        left: 0,
        right: 0
      },
      chartWaterExtend: {
        legend: {
          icon: 'rect',
          itemWidth: 8,
          itemHeight: 4,
          right: 0,
          top: 0,
          textStyle: {
            //图例文字的样式
            color: '#FFF',
            fontSize: 12
          }
        },
        title: {
          show: true,
          text: 'mm',
          textStyle: {
            color: '#fff',
            fontSize: 14,
            fontWeight: 'normal'
          },
          padding: [0, 0, 0, 0]
        },
        xAxis: {
          boundaryGap: 0
        },
        'xAxis.0.axisLabel.color': '#fff',
        'yAxis.0.axisLabel.color': '#fff'
      }
    };
  },
  async mounted() {
    await this.getMapPointDetail(this.lat, this.lng);
    await this.getWeatherForecast();
  },
  methods: {
    async getWeatherForecast() {
      let res = await disasterPreventionApi.wholeWeather();
      console.log(res);
      if(res.code == 0) {
        if(res.data.length > 0){
          this.rainfallInfo = res.data.find((item) => item.areaName == "鹿城区")
        }
      }
    },
    async getMapPointDetail(latitude, longitude) {
      const opt = {
        latitude,
        longitude
      };
      await weatherApi.getMapPointDetail(opt).then((res) => {
        this.title = res.data.address;
        res.data.hourlyRainfallList.map((item) => {
          item['时间'] = item.datetime;
          item['雨量'] = item.value;
          return item;
        });
        this.chartWaterData.rows = res.data.hourlyRainfallList;
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.rainfall {
  .rainfall-title {
    display: flex;
    padding-bottom: 30px;
    border-bottom: 1px solid rgba(0, 186, 255, 0.4);
    > div {
      &:first-child {
        font-size: 48px;
        font-weight: 500;
        color: #00baff;
        > span:first-child {
          font-size: 72px;
          padding-right: 10px;
        }
      }
      &:last-child {
        padding-left: 60px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        font-weight: 500;
        color: #ffffff;
        font-size: 24px;
        > div {
          display: flex;
          align-items: center;
          .icon {
            width: 24px;
            height: 24px;
            margin-right: 10px;
            &.wind-power-icon {
              background-size: 100% 100%;
              background: url(~assets/images/ScreenAsset/wind-power-icon.png) no-repeat;
            }
            &.humidness-icon {
              background-size: 100% 100%;
              background: url(~assets/images/ScreenAsset/humidness-icon.png) no-repeat;
            }
          }
        }
      }
    }
  }
  .line-box {
    padding-top: 30px;
    height: 350px;
  }
  .rainfall-tip {
    color: rgba(255, 255, 255, 0.88);
    font-size: 16px;
  }
}
</style>