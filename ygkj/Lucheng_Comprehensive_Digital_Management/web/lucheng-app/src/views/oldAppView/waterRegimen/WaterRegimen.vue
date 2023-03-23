<template>
  <view-page>
    <div class="wrap-page">
      <mapViewPoint :point="[{ point: [siteInfo.stLong, siteInfo.stLat] }]" />
      <!--此处样式已被提取成公共样式 在style/util.css  start-->
      <div class="detail-page-title">
        <div class="page-title">
          <div class="title-text">{{ siteInfo.stName }}</div>
          <div class="tip-text">{{ siteInfo | siteInfoType }}</div>
        </div>
        <div class="detail-page">
          水位：<span>{{ siteInfo | waterType }}m</span>
        </div>
      </div>
      <!------end------->
      <div class="page-detail-box">
        <wrapTitle title="水情信息" />
        <div class="picker-box">
          <div>
            <span @click="showPicker = true">{{ columns[defaultIndex] }}</span>
            <span class="el-icon-caret-bottom"></span>
          </div>
        </div>
        <div
          class="van-grid-box"
          :class="{ 'five-van-grid-box': infoData.hasRain }"
        >
          <div class="van-grid-item">
            <div>当前水位</div>
            <div class="green-color">
              <span>{{ siteInfo | waterType }}</span>
              <span>m</span>
            </div>
          </div>
          <div class="van-grid-item" v-if="infoData.hasRain">
            <div>累计雨量</div>
            <div class="green-color">
              <span>{{ infoData.rainAll || "-" }}</span>
              <span>mm</span>
            </div>
          </div>
          <div class="van-grid-item">
            <div>最低水位</div>
            <div class="orange-color">
              <span>{{ infoData.minLevel || "-" }}</span>
              <span>m</span>
            </div>
          </div>
          <div class="van-grid-item">
            <div>最高水位</div>
            <div class="blue-color">
              <span>{{ infoData.maxLevel || "-" }}</span>
              <span>m</span>
            </div>
          </div>
          <div class="van-grid-item">
            <div>出现时间</div>
            <div class="blue-color">
              <span>{{
                infoData.maxLevelTime
                  ? infoData.maxLevelTime.split(" ")[1]
                  : "-"
              }}</span>
              <span></span>
            </div>
          </div>
        </div>
        <div class="echarts-box" v-if="infoData.hasWater || infoData.hasRain">
          <div
            class="echarts-bar-box"
            v-if="infoData.hasRain"
            :inverse="infoData.hasWater"
          >
            <EchartsBar
              :extraOption="rainOption"
              :inverse="infoData.hasWater"
              :widthIndex="defaultIndex"
            />
          </div>
          <div class="echarts-bar-box" v-if="infoData.hasWater">
            <EchartsLine :extraOption="waterOption" />
          </div>
        </div>
        <div>
          <wrapTitle title="水位列表" />
          <div class="table_wrap">
            <tableList :theadList="theadList" :list="list">
              <template v-slot:date="item">
                <span>{{ item.data.date }}</span>
              </template>
            </tableList>
          </div>
        </div>
      </div>
      <!--选择器-->
      <vantPicker
        @onConfirm="onConfirm"
        @onCancel="onCancel"
        :defaultIndex="defaultIndex"
        :showPicker="showPicker"
        :columns="columns"
      />
    </div>
  </view-page>
</template>

<script>
import mapViewPoint from "@/components/mapViewPoint.vue";
import wrapTitle from "@/components/wrapTitle.vue";
import vantPicker from "@/components/vantPicker.vue";
import EchartsBar from "@/components/EchartsBar.vue";
import EchartsLine from "@/components/EchartsLine.vue";
import tableList from "@/components/tableList.vue";
import { realTimeMonitoring } from "@/api/projectDetail.js";
import { mapGetters } from "vuex";
export default {
  components: {
    mapViewPoint,
    wrapTitle,
    vantPicker,
    EchartsBar,
    EchartsLine,
    tableList,
  },
  computed: {
    ...mapGetters(["siteInfo"]),
  },
  filters: {
    waterType: (item) => {
      switch (item.stType) {
        case "RR":
          return item.waterLevel;
        case "ZZ":
          return item.waterLevel;
        case "DD":
          return item.upz;
        default:
          return item.tdz;
      }
    },
    siteInfoType: (item) => {
      switch (item.stType) {
        case "RR":
          if (item.waterLevel > item.limitLevel && item.limitLevel > 0) {
            return "超汛限";
          } else {
            return "";
          }
        case "ZZ":
          if (
            item.waterLevel > item.guaranteeLevel &&
            item.guaranteeLevel > 0
          ) {
            return "超保证";
          } else if (
            item.waterLevel > item.warningLevel &&
            item.warningLevel > 0
          ) {
            return "超警戒";
          } else {
            return "";
          }
        case "DD":
          if (item.upz > item.guaranteeLevel && item.guaranteeLevel > 0) {
            return "超保证";
          } else if (item.upz > item.warningLevel && item.warningLevel > 0) {
            return "超警戒";
          } else {
            return "";
          }
        default:
          if (item.tdz > item.warningLevel && item.warningLevel > 0) {
            return "超警戒";
          } else {
            return "";
          }
      }
    },
  },
  data() {
    return {
      columns: ["前一天", "前二天", "前三天", "前四天", "前一周", "前二周"],
      defaultIndex: 0,
      showPicker: false,
      theadList: [
        {
          label: "时间",
          prop: "date",
          slot: true,
        },
        {
          label: "水位（m）",
          prop: "waterLevel",
          slot: false,
          width: '120px'
        },
      ],
      list: [],
      rainOption: {},
      waterOption: {},
      infoData: {},
    };
  },
  mounted() {
    this.addZwlog(this)
    this.getDateList(1);
  },
  methods: {
    async getDateList(days) {
      // RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道，SS墒情,PT泵站
      let opt = {
        stationCode: this.siteInfo.stCode,
        stationType: this.siteInfo.stType,
        days,
      };
      let res = await realTimeMonitoring.waterRainHours(opt);
      if (res.code === 0) {
        let xAxisData = [];
        let waterData = {
          data: [],
          markLine: {
            data: [
              {
                yAxis: 100,
              },
            ],
          },
        };
        let rainData = {
          data: [],
        };
        res.data.list.forEach((item, index) => {
          let times = item.date.split("-");
          let hours = times[2].split(":");
          xAxisData.push(`${times[1]}-${[hours[0], hours[1]].join(":")}`);
          waterData.data.push(item.waterLevel);
          if (res.data.hasRain) {
            rainData.data.push(item.rainValue);
          }
        });
        this.waterOption = {
          xAxis: {
            data: xAxisData,
          },
          series: [waterData],
        };
        if (res.data.hasRain) {
          let obj = {
            label: "雨量（mm）",
            prop: "rainValue",
            slot: false,
            width: '120px'
          };
          this.theadList.push(obj);
          this.rainOption = {
            xAxis: {
              data: xAxisData,
            },
            series: [rainData],
          };
        }
        this.infoData = res.data;
        this.list = res.data.list.reverse();
      }
    },
    onConfirm(data, index) {
      console.log(data, index);
      this.defaultIndex = index;
      this.showPicker = false;
      let days = 0;
      switch (index) {
        case 1:
          days = 2;
          break;
        case 2:
          days = 3;
          break;
        case 3:
          days = 4;
          break;
        case 4:
          days = 7;
          break;
        case 5:
          days = 14;
          break;
        default:
          days = 1;
          break;
      }
      this.getDateList(days);
    },
    onCancel() {
      this.showPicker = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.table_wrap {
  max-height: 300px;
  overflow: auto;
}
.page-detail-box {
  margin-top: 6px;
  background-color: #fff;
  .picker-box {
    padding: 0 8px;
    > div {
      border-top: 1px solid #e9f0fd;
      padding: 10px 0 13px;
      span {
        color: #666666;
        font-size: 20px;
        &:first-child {
          padding-right: 10px;
        }
        &:last-child {
          font-size: 14px;
          color: #aaaaaa;
        }
      }
    }
  }
  .van-grid-box {
    display: flex;
    flex-wrap: wrap;
    border-top: 1px solid #e6e6e6;
    .van-grid-item {
      flex: 1;
      padding: 11px 0;
      text-align: center;
      border-right: 1px solid #e6e6e6;
      box-sizing: border-box;
      border-bottom: 1px solid #e6e6e6;
      &:last-child {
        border-right: none;
      }
      > div:first-child {
        font-size: 20px;
        color: #666666;
        font-weight: 600;
      }
      > div:last-child {
        padding-top: 5px;
        > span {
          &:first-child {
            font-size: 20px;
            font-weight: 500;
            line-height: 25px;
          }
          &:last-child {
            font-size: 18px;
          }
        }
      }
    }
    &.five-van-grid-box {
      .van-grid-item {
        flex: none;
        min-width: 95px;
        &:last-child {
          border-right: 1px solid #e6e6e6;
        }
      }
    }
  }
  .echarts-box {
    padding-top: 13px;
    .echarts-bar-box {
      height: 180px;
      width: 100%;
      padding: 0 12px;
      box-sizing: border-box;
    }
  }
}
</style>