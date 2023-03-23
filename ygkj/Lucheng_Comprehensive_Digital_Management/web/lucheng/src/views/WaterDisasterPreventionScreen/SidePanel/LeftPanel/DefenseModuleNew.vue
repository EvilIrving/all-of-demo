<template>
  <div>
    <!-- 防御态势 -->
    <div class="collapse-panel__border" style="width: 490px">
      <div class="collapse-panel__content">
        <!-- 气象预警 六天预报 -->
        <panel-weather-meteor />
        <DefenseModuleForecast
          :card-selection="cardSelect == 'forecast'"
          @change="forecastCardClick"
        />
        <!-- 山洪 -->
        <panel-mountain-torrents />
      </div>
    </div>
  </div>
</template>

<script>
import "@/style/v-charts/style.css";
import PrecipitionMaxList from "../../components/PrecipitionMaxList.vue";
import PanelWeatherMeteor from "../components/PanelWeatherInfo.vue";
import PanelMountainTorrents from "../components/PanelMountainTorrents.vue";
import DefenseModuleForecast from "./components/DefenseModuleForecast.vue";

export default {
  name: "LeftSildePanelDefenseModule",
  components: {
    PrecipitionMaxList,
    PanelWeatherMeteor,
    PanelMountainTorrents,
    DefenseModuleForecast,
  },
  data() {
    return {
      //两个卡片选择中
      cardSelect: "",
    };
  },
  methods: {
    forecastCardClick() {
      this.cardSelect = "forecast";
    },
  },
};
</script>
<style lang="scss" scoped>
@import "../../style/SildePanel.scss";
::v-deep .panel-item__title--switch {
  i {
    cursor: pointer;
  }
}
.typhoon-search {
  width: 360px;
  height: 100%;
  @include flexbox;
  @include flexflow(column, nowrap);
  > p {
    padding: 8px 0 12px 0;
    color: #fff;
  }
  .search-row {
    margin-bottom: 12px;
    line-height: 44px;
    .search-label {
      color: #fff;
      height: 100%;
      @include flexbox;
      @include flexAC;
      @include flexJC(center);
    }
  }
  .primary-btn {
    color: #fff;
    margin: 6px 0;
  }
  .typhoon-list {
    height: 270px;
  }
}

.prediction-sty {
  text-align: center;
  color: #ffffff;
  padding-top: 15px;
  padding-bottom: 15px;
  .border-sty {
    border-left: 1px solid rgba(35, 140, 239, 0.4);
    border-right: 1px solid rgba(35, 140, 239, 0.4);
  }
  .rainfall-sty {
    font-size: 20px;
    color: #1cfff5;
  }
}

.areal-rainfall-sty {
  display: flex;
  justify-content: space-between; /* 横向中间自动空间 */
  > div:nth-child(1) {
    vertical-align: middle;
    > i {
      vertical-align: middle;
    }
    > span {
      color: #fff;
    }
  }
  > div:nth-child(2) {
    line-height: 21px;
    .tab-switch-sty {
      padding: 2px 10px;
      border-radius: 15px;
      background: #00c1ff;
    }
    > span {
      color: #fff;
      margin-left: 15px;
      font-size: 15px;
    }
  }
}

.early-warning-sty {
  font-size: 20px;
  font-weight: bold;
  padding: 15px 20px;
  background-image: linear-gradient(to right, rgba(117, 124, 52, 0.3), rgba(0, 0, 0, 0));
  > p {
    color: #eec80b;
  }
  > p:nth-child(2) {
    margin-top: 16px;
    > span {
      color: #eec80b;
      > span {
        color: #fff;
        font-size: 14px;
      }
    }
  }
}
.early-warning-style {
  font-size: 20px;
  font-weight: bold;
  padding: 15px 20px;
  background-image: linear-gradient(to right, rgba(192, 110, 63, 0.3), rgba(0, 0, 0, 0));
  > p {
    color: #ff7144;
  }
  > p:nth-child(2) {
    margin-top: 16px;
    > span {
      color: #ff7144;

      > span {
        color: #fff;
        font-size: 14px;
      }
    }
  }
}

.switch-list {
  ::v-deep.el-carousel--horizontal {
    overflow-x: hidden;
    overflow-y: hidden;
  }
}

.sidebar-list {
  .warning-style {
    font-size: 16px;
    color: #ffffff;
    line-height: 24px;
    font-weight: 400;
  }
  ::v-deep.el-carousel--horizontal {
    overflow-x: hidden;
    overflow-y: hidden;
    height: unset;
  }
}
</style>
<style lang="scss">
.station-list {
  display: flex;
  .statistic {
    width: 50%;
    margin-right: 16px;
    margin-top: 8px;
    padding: 8px 0;
    color: #fff;
    font-size: 18px;
    display: flex;
    justify-content: center;
    align-items: baseline;
    background: url(~assets/images/WaterDisastersImage/rainfall-bg.png) no-repeat top
      center;
    background-size: 100% 100%;
    .number {
      margin: 0 3px;
      font-size: 28px;
      color: #1ef8f2;
      line-height: 24px;
    }
    &:last-child {
      margin-right: 0;
    }
  }
}
</style>
