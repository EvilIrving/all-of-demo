<template>
  <div class="big-card">
    <panel-item title="灌区" hasSwitch tc @tclick="tclick">
      <template slot="title-switch">
        <i
          title="地图落点"
          @click.stop="handleReservoirChange('skbtn')"
          :class="[
            skbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          title="查看详表"
          @click.stop="openDetailsDialog()"
          class="icon-liebiao icon-sty"
        />
        <i
          @click="goToAmap('灌区')"
          title="一张图"
          class="icon-yizhangtu icon-sty"
        />
      </template>
      <!-- 灌区类型统计-->
      <div class="tab-type">
        <card-tabs
          ref="typeList"
          v-model="typeSelect"
          :list="typeList"
          :decimalsFlag="false"
          :col="8"
          @change="tabTypeClick"
        />
      </div>
      <div class="area-box">
        <ve-histogram
          ref="chart"
          height="100%"
          width="100%"
          :data="barData"
          :settings="barOption.setting"
          :extend="barOption.extend"
          :grid="barOption.grid"
          :data-zoom="barOption.dataZoom"
          :tooltip-visible="false"
          :legend-visible="false"
          :after-config="barConfig"
        />
      </div>
    </panel-item>
    <IrrigateDetail
      v-if="detailVisible"
      type="灌区"
      :visible="detailVisible"
      @close="closeDetail"
    ></IrrigateDetail>
  </div>
</template>

<script>
import CardTabs from "../../WaterDisasterPreventionScreen/components/CardTabs.vue";
import PanelItem from "../components/PanelItem.vue";
import { hydraulicApi } from "@/api/hydraulic_amap";
import { mapGetters, mapActions } from "vuex";
import IrrigateDetail from "./dialog/IrrigateDetail.vue";
export default {
  name: "IrrigateCard",
  layerName: "IrrigateLayer",
  legendName: "灌区",
  components: {
    PanelItem,
    IrrigateDetail,
    CardTabs
  },
  mounted() {
    this.$nextTick(() => {
      this.rsvrProjDrainageStats();
      this.getStats().then(()=>{
        this.getList(this.typeList[0].levels);
      });
    });
  },
  data() {
    return {
      skbtn: false,
      barData: {
        columns: ["name", "data"],
        rows: [],
      },
      barOption: {
        grid: {
          top: 40,
          bottom: "1%",
          left: 10,
          containLabel: true,
        },
        setting: {
          yAxisName: ["k㎡"],
          itemStyle: { color: "#fff" },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            fontWeight: "bold",
            position: "top",
            color: "#fff",
          },
        },
        extend: {
          "textStyle.color": "#fff",
          barWidth: 15,
          xAxis: {
            axisLabel: {
              interval: 0,
              fontSize: this.$commonSetting.chartFontSize,
            },
          },
          yAxis: {
            nameTextStyle: {
              color: "#ffffff99",
              fontSize: this.$commonSetting.chartFontSize,
            },
            axisLine: {
              show: false,
            },
            axisLabel: {
              color: "#fff",
              fontSize: this.$commonSetting.chartFontSize,
            },
            splitLine: {
              lineStyle: {
                color: "#fff",
                opacity: 0.5,
              },
            },
            max: (value) => {
              return value.max > 1 ? null : 1;
            },
          },
        },
      },
      resData: [],
      detailVisible: false,
      typeSelect: 0,
      statisticObj: null,
    };
  },
  computed: {
    ...mapGetters([
      "ScreenMap",
      "currentSelectArea",
      "previewModule",
      "formatterCurrentTime",
    ]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
    typeList() {
      let list = [{
        label: "大型灌区",
        data: { number: 0, unit: '个' },
        levels:""
      }, {
        label: "中型灌区",
        data: { number: 0, unit: '个' },
        levels:""
      }, {
        label: "小型灌区",
        data: { number: 0, unit: '个' },
        levels:""
      }];
      if(this.statisticObj){
        list.map(item => {
          switch (item.label) {
            case '大型灌区': {
              item.data.number = this.statisticObj.bigNum;
              item.levels = this.statisticObj.bigLevels;
            } break;
            case '中型灌区': {
              item.data.number = this.statisticObj.midNum;
              item.levels = this.statisticObj.midLevels;
            } break;
            case '小型灌区': {
              item.data.number = this.statisticObj.smallNum;
              item.levels = this.statisticObj.smallLevels;
            } break;
          }
        })
      }
      return list;
    }
  },
  methods: {
    ...mapActions(["SetDetailDialogParam"]),
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val, data) {
      this.detailVisible = true;
      // this.SetDetailDialogParam({
      //   mediumSized: data,
      //   codeTpye: val,
      //   analyseDialogShow: true,
      // });
    },
    /**
     * 跳转一张图
     */
    goToAmap(type) {
      this.$router.push(`/hydraulicAmap?selectType=灌区`);
    },
    tabTypeClick(val) {
      let levels = this.typeList[val].levels;
      this.getList(levels);
    },
    async rsvrProjDrainageStats() {
      const opt = {
        type: "灌区"
      }
      await hydraulicApi.projectsDetailList(opt).then((res) => {
        if (res.code == 0) {
          this.resData = res.data;
        }
      });
    },
    barConfig(options) {
      let data = options.series[0].data;
      let colorArr = [];
      colorArr = ["#15E2DB", "#9DFDFB", "#A6FFFF"];
      let color = {
        type: "linear",
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr[0],
          },
          {
            offset: 0.5,
            color: colorArr[0],
          },
          {
            offset: 0.5,
            color: colorArr[1],
          },
          {
            offset: 1,
            color: colorArr[1],
          },
        ],
      };
      let barWidth = 30;
      let constData = [];
      let showData = [];
      data.filter(function (item) {
        if (item) {
          constData.push(1);
          showData.push(item);
        } else {
          constData.push(0);
          showData.push({
            value: 1,
            itemStyle: {
              normal: {
                borderColor: "rgba(0,0,0,0)",
                borderWidth: 2,
                color: "rgba(0,0,0,0)",
              },
            },
          });
        }
      });

      options.series = [
        {
          z: 1,
          name: "数据",
          type: "bar",
          barWidth: barWidth,
          barGap: "0%",
          data: data,
          itemStyle: {
            normal: {
              color: color,
            },
          },
          label: {
            show: true,
            fontSize: 18,
            position: "top",
            color: "#1EF8F2",
          },
        },
        {
          z: 2,
          name: "数据",
          type: "pictorialBar",
          data: constData,
          symbol: "diamond",
          symbolOffset: ["0%", "50%"],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color,
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          z: 3,
          name: "数据",
          type: "pictorialBar",
          symbolPosition: "end",
          data: showData,
          symbol: "diamond",
          symbolOffset: ["0%", "-50%"],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr[2],
              borderWidth: 2,
              color: colorArr[2],
            },
          },
          tooltip: {
            show: false,
          },
        },
      ];
      return options;
    },
    async getStats() {
      const opt = {
        type: 1
      }
      await hydraulicApi.irrStats4BigScreen(opt).then(res => {
        if (res.code == 0 && res.data.length > 0) {
          this.statisticObj = {
            'bigNum': res.data[0].value,
            'bigLevels':res.data[0].remark,
            'midNum': res.data[1].value,
            'midLevels':res.data[1].remark,
            'smallNum': res.data[2].value,
            'smallLevels':res.data[2].remark
          }
        }
      })
    },
    async getList(level) {
      const opt = {
        type: "灌区",
        levels: level
      }
      await hydraulicApi.projectsDetailList(opt).then((res) => {
        if (res.code == 0) {
          let rows = [];
          for (let item of res.data) {
            rows.push({ name: item.irrName, data: item.effIrrArea });
          }
          this.barData.rows = rows;
          this.$refs["chart"].echarts.resize();
        }
      });
    },

    handleReservoirChange(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.7,
        anchor: [24, 16],
        anchorXUnits: "pixels",
        src: require("@/assets/images/MapPoint/irrigated.png"),
      };
      let pointList = this.resData.map((point) => {
        let imgSrc = require("@/assets/images/MapPoint/irrigated.png");
        return {
          name: point.irrName,
          type: "",
          lng: point.lowLeftLong,
          lat: point.lowLeftLat,
          code: point.irrCode,
          src: imgSrc,
          props: {
            行政区划: `${point.adnm || '-'}`,
            流域: `${point.bas || '-'}`,
            有效灌溉面积: `${point.effIrrArea || '-'} ㎡`,
          },
        };
      });
      if (this[btnType]) {
        this.ScreenMap.drawPoint(pointList, style, "IrrigateLayer");
        this.$emit("legend-change", "灌区", {
          label: "灌区",
          children: [
            {
              label: "灌区",
              icon: "icon-legend-irrigated",
            },
          ],
        });
      } else {
        this.ScreenMap.drawPoint([], {}, "IrrigateLayer");
        this.$emit("legend-change", "灌区", {});
      }
    },
    closeDetail() {
      this.detailVisible = false;
    }
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.big-card {
  height: 100%;
  // padding: 0 16px;
  ::v-deep.el-carousel {
    height: 100%;
    .el-carousel__container {
      height: 100%;
    }
  }
  .tab-type {
    padding: 8px 0 0;
  }
  .area-box {
    width: 100%;
    height: calc(100% - 100px);
  }
}
</style>