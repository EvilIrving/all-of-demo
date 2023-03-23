<!--
 * @Date: 2022-06-09 11:09:05
 * @Author: 
 * @Description: 实时雨情 详情
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-10-26 16:45:22
 * @FilePath: \lucheng-older\src\views\appView\rainfall\RealTimeRainfall.vue
-->
<template>
  <view-page>
    <div class="wrap-page">
      <mapViewPoint
        :point="[{ point: [siteInfo.longitude, siteInfo.latitude] }]"
      />
      <!--此处样式已被提取成公共样式 在style/util.css  start-->
      <div class="detail-page-title">
        <div class="page-title">
          <div class="title-text">{{ siteInfo.stationName }}</div>
        </div>
        <div class="detail-page">
          雨量：<span>{{ siteInfo.drp }}mm</span>
        </div>
      </div>
      <!------end------->
      <div class="page-detail-box">
        <wrapTitle title="雨情信息" />
        <div class="picker-box">
          <div>
            <span @click="showPicker = true">{{ columns[defaultIndex] }}</span>
            <span class="el-icon-caret-bottom"></span>
          </div>
        </div>
        <div class="van-grid-box">
          <div class="van-grid-item">
            <div>累计雨量</div>
            <div class="green-color">
              <span>{{ infoData.rainAll }}</span>
              <span>mm</span>
            </div>
          </div>
        </div>
        <div class="echarts-box" v-if="infoData.hasRain">
          <div class="echarts-bar-box">
            <EchartsBar :extraOption="extraOption" :widthIndex="defaultIndex" />
          </div>
        </div>
        <wrapTitle title="雨量列表" />
        <div class="table_wrap">
          <tableList :theadList="theadList" :list="list">
            <template v-slot:date="item">
              <span>{{ item.data.date }}</span>
            </template>
          </tableList>
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
  name: "RealTimeRainfall",
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
          label: "雨量（mm）",
          prop: "rainValue",
          slot: false,
        },
      ],
      list: [],
      infoData: {},
      extraOption: {},
    };
  },
  created() {},
  mounted() {
    this.addZwlog(this)
    this.getDateList(1);
  },
  methods: {
    async getDateList(days) {
      // RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道，SS墒情,PT泵站
      let opt = {
        stationCode: this.siteInfo.mgstcd,
        stationType: "PP",
        days,
      };
      let res = await realTimeMonitoring.waterRainHours(opt);
      if (res.code === 0) {
        let xAxisData = [];
        let rainData = {
          data: [],
        };
        res.data.list.forEach((item, index) => {
          let times = item.date.split("-");
          let hours = times[2].split(":");
          xAxisData.push(`${times[1]}-${[hours[0], hours[1]].join(":")}`);
          rainData.data.push(item.rainValue);
        });
        this.extraOption = {
          xAxis: {
            data: xAxisData,
            axisLabel: {
              show: true,
            },
          },
          series: [rainData],
        };
        this.infoData = res.data;
        this.list = res.data.list.reverse();
      }
    },
    onConfirm(data, index) {
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
    border-top: 1px solid #e6e6e6;
    border-bottom: 1px solid #e6e6e6;
    .van-grid-item {
      width: 93px;
      box-sizing: border-box;
      padding: 11px 0;
      text-align: center;
      border-right: 1px solid #e6e6e6;
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