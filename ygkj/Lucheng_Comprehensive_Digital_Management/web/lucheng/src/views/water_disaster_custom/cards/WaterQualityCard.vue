<!--
 * @Author: wpf
 * @Date: 2021-07-15 09:47:17
 * @LastEditTime: 2021-10-13 19:06:47
 * @LastEditors: ysl
 * @Description: 水质监控卡片
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/WaterQualityCard.vue
-->
<template>
  <div class="water-quality-card">
    <panel-item
      title="水质监控"
      has-switch
      tc
      @tclick="tclick('水质监控')"
      :action-tabs="[]"
      @change="handleTabChange"
    >
      <template #title-switch>
        <i
          title="地图落点"
          @click="getWaterQualityPoint(jsbtn)"
          :class="[
            jsbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('水质')"
          style="margin-left: 10px"
        />
      </template>
       <div class="chart-panel pie-box">
        <div id="waterQualityPie" style="width:50%;height:100%" ref="waterQualityPie"></div>
				<div class="legend-panel bar_box">
					<div
						class="legend-item"
						v-for="(item, index) in barPieData"
						:key="index"
					>
						<div>
							<i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
							<span class="text">{{ item.label }}</span>
						</div>
						<span class="num">{{ item.value }}</span>个
					</div>
      	</div>
      </div>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import { hydraulicApi } from "@/api/hydraulic_amap/index";
import * as echarts from "echarts"; // echart插件
export default {
  name: "StagnantWaterCard",
  components: {
    PanelItem,
  },
  computed: {
    ...mapGetters(["previewModule", "ScreenMap"]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },

  data() {
    return {
      jsbtn: false, // 落点
      activeTab: 0, // 0按行政区划 1按是否有积水
      pointList: [],
      barPieData: [
        { label: "Ⅰ类", value: 0 },
        { label: "Ⅱ类", value: 0 },
        { label: "Ⅲ类", value: 0 },
        { label: "Ⅳ类", value: 0 },
        { label: "Ⅴ类", value: 0 },
        { label: "劣Ⅴ类", value: 0 }
      ],
      totalLength: 0,
      colorList: [
        "#00EBC7", "#008FE8", "#19AF47", "#FF7D7D", "#89BA8B", "#E89100", "#FF7144", "#004FE3", "#A80BEE",
        "#8A8AE1", "#EE0B6B", "#28C9F6", "#FFEC36", "#2DDF3B", "#DBA380", "#FF1111", "#5836FF", "#B05EFF"
      ],
    };
  },
  mounted() {
    this.getQualityStatsData()
  },
  methods: {
    ...mapActions(["SetDetailDialogParam"]),
    // 获取统计数据
    async getQualityStatsData() {
      await hydraulicApi.qualityStats({ type: 3 }).then(res => {
        this.barPieData.map((item, index) => {
          res.data.forEach(sItem => {
            if( sItem.key == item.label ) {
              item.index = index - 1
              item.value = sItem.value
            }
          })
          this.totalLength += item.value
          return item
        })
      })
      this.areaTypePie()
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
      this.$emit('openDialog', '水质监控');
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    setColor(color) {
      return {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: color, // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
    },
    // 饼图
    areaTypePie() {
      var chartDom = document.getElementById("waterQualityPie");
      var myChart = echarts.init(chartDom);
      var option;
      let totalLength = this.totalLength
      let color = []
      this.colorList.forEach((item, index) => {
        color.push(this.setColor(item))
      })
      option = {
        tooltip: {
          show: true,
          trigger: "item",
          formatter: function (data) {
            return (
              data.data.label +
              " <span style='font-size: 20px;color: #1CFFF5;line-height: 26px;'> " +
              data.value +
              "</span> <span  style='font-size: 14px;'> 个</span>"
            );
          },
          confine: true,
          extraCssText: "width:160px;height:50px;font-size:18px;",
        },
        series: [
          {
            type: "pie",
            radius: ["50%", "80%"],
            centen: [0, 0],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: "center",
                color: "#fff",
                fontSize: 16,
                formatter: `{a|${totalLength}}` + " {b|个\n站点总数}",
                rich: {
                  a: {
                    fontSize: 20,
                    color: "#fff",
                  },
                  b: {
                    fontSize: 12,
                    color: "#fff"
                  }
                }
              },
            },
            labelLine: {
              show: false,
            },
            data: this.barPieData,
            color: color,
          },
        ],
      };
      option && myChart.setOption(option);
    },
    //获取水质点位
    async getWaterQualityPoint(type) {
        this.jsbtn = !this.jsbtn;

        let abnormalArr1 = [];
        let abnormalArr2 = [];
        let abnormalArr3 = [];
        let abnormalArr4 = [];
        let abnormalArr5 = [];
        let abnormalArr6 = [];
        let abnormalArr7 = [];
        let abnormalStyle1 = {
            scale: 0.7,
            anchor: [27, 30],
            anchorXUnits: "pixels",
            src: require(`@/assets/images/map_legends/one-class.png`),
            srcs: require(`@/assets/images/map_legends/one-class-cluster.png`)
        };
        let abnormalStyle2 = {
            scale: 0.7,
            anchor: [27, 30],
            anchorXUnits: "pixels",
            src: require(`@/assets/images/map_legends/two-class.png`),
            srcs: require(`@/assets/images/map_legends/two-class-cluster.png`)
        };
        let abnormalStyle3 = {
            scale: 0.7,
            anchor: [27, 30],
            anchorXUnits: "pixels",
            src: require(`@/assets/images/map_legends/three-class.png`),
            srcs: require(`@/assets/images/map_legends/three-class-cluster.png`)
        };
        let abnormalStyle4 = {
            scale: 0.7,
            anchor: [27, 30],
            anchorXUnits: "pixels",
            src: require(`@/assets/images/map_legends/four-class.png`),
            srcs: require(`@/assets/images/map_legends/four-class-cluster.png`)
        };
        let abnormalStyle5 = {
            scale: 0.7,
            anchor: [27, 30],
            anchorXUnits: "pixels",
            src: require(`@/assets/images/map_legends/five-class.png`),
            srcs: require(`@/assets/images/map_legends/five-class-cluster.png`)
        };
        let abnormalStyle6 = {
            scale: 0.7,
            anchor: [27, 30],
            anchorXUnits: "pixels",
            src: require(`@/assets/images/map_legends/six-class.png`),
            srcs: require(`@/assets/images/map_legends/six-class-cluster.png`)
        };
        let abnormalStyle7 = {
            scale: 0.7,
            anchor: [27, 30],
            anchorXUnits: "pixels",
            src: require(`@/assets/images/map_legends/seven-class.png`),
            srcs: require(`@/assets/images/map_legends/seven-class-cluster.png`)
        };
        let opt = {
            areaCode: "",
            siteName: "",
            qualityLevel: "",
            level: ""
        };
        let res = await hydraulicApi.waterQualityV2(opt);
        res.data.forEach(item => {
            let obj = {
                ...item,
                type: "水质监控",
                name: item.siteName,
                lat: parseFloat(item.latitude),
                lng: parseFloat(item.longitude),
                props: {
                    站点名称: item.siteName,
                    水质等级: item.qualityLevel == 1 && item.qualityWorse == false ? 'Ⅰ类'
                             :item.qualityLevel == 2 && item.qualityWorse == false ? 'Ⅱ类' 
                             :item.qualityLevel == 3 && item.qualityWorse == false ? 'Ⅲ类' 
                             :item.qualityLevel == 4 && item.qualityWorse == false ? 'Ⅳ类' 
                             :item.qualityLevel == 5 && item.qualityWorse == false ? 'Ⅴ类' 
                             :item.qualityLevel == 6 && item.qualityWorse == false ? '劣Ⅴ类'
                             : '恶化',
                    更新时间: item.tm
                },
                text:`${item.siteName || "-"} 水质等级:${
                    item.qualityLevel == 1 && item.qualityWorse == false ? 'Ⅰ类'
                    :item.qualityLevel == 2 && item.qualityWorse == false ? 'Ⅱ类' 
                    :item.qualityLevel == 3 && item.qualityWorse == false ? 'Ⅲ类' 
                    :item.qualityLevel == 4 && item.qualityWorse == false ? 'Ⅳ类' 
                    :item.qualityLevel == 5 && item.qualityWorse == false ? 'Ⅴ类' 
                    :item.qualityLevel == 6 && item.qualityWorse == false ? '劣Ⅴ类'
                    : '恶化'}`
            }
            if (item.qualityLevel == 1 && item.qualityWorse == false) {
                abnormalArr1.push(obj);
            } else  if (item.qualityLevel == 2 && item.qualityWorse == false) {
                abnormalArr2.push(obj);
            } else  if (item.qualityLevel == 3 && item.qualityWorse == false) {
                abnormalArr3.push(obj);
            } else  if (item.qualityLevel == 4 && item.qualityWorse == false) {
                abnormalArr4.push(obj);
            } else  if (item.qualityLevel == 5 && item.qualityWorse == false) {
                abnormalArr5.push(obj);
            } else  if (item.qualityLevel == 6 && item.qualityWorse == false) {
                abnormalArr6.push(obj);
            } else {
                abnormalArr7.push(obj);
            }
        });
        
        if (!this.jsbtn) {
          this.ScreenMap.drawClusterPoint([], {}, 'waterQualityAbnormal1');
          this.ScreenMap.drawClusterPoint([], {}, 'waterQualityAbnormal2');
          this.ScreenMap.drawClusterPoint([], {}, 'waterQualityAbnormal3');
          this.ScreenMap.drawClusterPoint([], {}, 'waterQualityAbnormal4');
          this.ScreenMap.drawClusterPoint([], {}, 'waterQualityAbnormal5');
          this.ScreenMap.drawClusterPoint([], {}, 'waterQualityAbnormal6');
          this.ScreenMap.drawClusterPoint([], {}, 'waterQualityAbnormal7');
          this.$emit("legend-change", "水质监控", {});
          return;
        } else {
          this.ScreenMap.drawClusterPoint(abnormalArr1, abnormalStyle1, 'waterQualityAbnormal1');
          this.ScreenMap.drawClusterPoint(abnormalArr2, abnormalStyle2, 'waterQualityAbnormal2');
          this.ScreenMap.drawClusterPoint(abnormalArr3, abnormalStyle3, 'waterQualityAbnormal3');
          this.ScreenMap.drawClusterPoint(abnormalArr4, abnormalStyle4, 'waterQualityAbnormal4');
          this.ScreenMap.drawClusterPoint(abnormalArr5, abnormalStyle5, 'waterQualityAbnormal5');
          this.ScreenMap.drawClusterPoint(abnormalArr6, abnormalStyle6, 'waterQualityAbnormal6');
          this.ScreenMap.drawClusterPoint(abnormalArr7, abnormalStyle7, 'waterQualityAbnormal7');
          this.$emit("legend-change", "水质监控", {
            label: "水质监控",
            clusterLayer: ['waterQualityAbnormal1', 'waterQualityAbnormal2', 'waterQualityAbnormal3', 'waterQualityAbnormal4', 'waterQualityAbnormal5', 'waterQualityAbnormal6', 'waterQualityAbnormal7'],
            children: [
              { label: "I类", icon: "icon-one-icon" },
              { label: "II类", icon: "icon-two-icon" },
              { label: "III类", icon: "icon-three-icon" },
              { label: "IV类", icon: "icon-four-icon" },
              { label: "V类", icon: "icon-five-icon" },
              { label: "劣V类", icon: "icon-six-icon" },
              { label: "恶化", icon: "icon-error-icon" }
            ],
          });
        }
    },
    handleTabChange(val) {
      this.activeTab = val;
    },
    // 获取点位
    async getWaterLoggingList() {
      let res = await disasterPreventionApi.waterLoggingList();
      this.pointList = res.data;
    },
  },
};
</script>
<style lang="scss" scoped>
.water-quality-card{
  width: 100%;
  height: 100%;
  .area-box,
  .is-water-box {
    width: 100%;
    height: calc(100% - 34px);
  }
  .is-water-box {
    display: flex;
  }
  .pie-box {
    width: 100%;
    height: 100%;
    display: flex;
  }
  .pie-legend {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 60%;
    height: 100%;
    font-size: 16px;
    padding-left: 30px;
    > div {
      display: flex;
      justify-content: space-between;
      line-height: 30px;

      &:last-child .legend-circle {
        background: #ff7144;
      }
      .legend-circle {
        display: inline-block;
        height: 14px;
        width: 14px;
        border-radius: 50%;
        background: #1cfff5;
        vertical-align: middle;
        margin-right: 10px;
      }
      .legend-num {
        font-size: 20px;
        color: #1cfff5;
        margin-right: 7px;
      }
    }
  }
}
</style>

<style lang="scss">
.water-quality-card .panel-item .panel-item__content--tabs .tab-button,
.water-quality-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.water-quality-card .panel-item .panel-item__content--tabs .tab-button.active,
.water-quality-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
<style lang="scss" scoped>
.icon-sty {
  cursor: pointer;
  margin-right: 10px;
}
.icon-circle {
  width: 12px !important;
  height: 12px !important;
  border-radius: 50%;
  &.icon-color-1 {
    @include chart-icon();
    background: #00EBC7;
  }
  &.icon-color-2 {
    @include chart-icon();
    background: #008FE8;
  }
  &.icon-color-3 {
    @include chart-icon();
    background: #19AF47;
  }
  &.icon-color-4 {
    @include chart-icon();
    background: #FF7D7D;
  }
  &.icon-color-5 {
    @include chart-icon();
    background: #89BA8B;
  }
  &.icon-color-6 {
    @include chart-icon();
    background: #E89100;
  }
  &.icon-color-7 {
    @include chart-icon();
    background: #FF7144;
  }
  &.icon-color-8 {
    @include chart-icon();
    background: #004FE3;
  }
  &.icon-color-9 {
    @include chart-icon();
    background: #A80BEE;
  }
  &.icon-color-10 {
    @include chart-icon();
    background: #8A8AE1;
  }
  &.icon-color-11 {
    @include chart-icon();
    background: #EE0B6B;
  }
  &.icon-color-12 {
    @include chart-icon();
    background: #28C9F6;
  }
  &.icon-color-13 {
    @include chart-icon();
    background: #FFEC36;
  }
  &.icon-color-14 {
    @include chart-icon();
    background: #2DDF3B;
  }
  &.icon-color-15 {
    @include chart-icon();
    background: #DBA380;
  }
  &.icon-color-16 {
    @include chart-icon();
    background: #FF1111;
  }
  &.icon-color-17 {
    @include chart-icon();
    background: #5836FF;
  }
  &.icon-color-18 {
    @include chart-icon();
    background: #B05EFF;
  }
}
.chart-panel {
  height: 200px;
}
.pie-box {
  display: flex;
  justify-content: space-around;
  align-items: center;
	width: 100%;
  .legend-panel {
    width: 45%;
    height: 50%;
    overflow-y: auto;
    .legend-item {
      line-height: 25px;
      display: flex;
      justify-content: space-between;
      font-size: 16px;
      > div {
        width: 65%;
        display: flex;
        align-items: center;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      .text {
        font-size: 16px;
        width: 95%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        height: 1.5rem;
        line-height: 1.5rem;
        // word-break: keep-all;
      }
      .num {
        color: #1cfff5;
        font-size: 20px;

      }
    }
  }
  .bar_box {
		overflow: hidden auto;
  	height: 80%;
		width: 45%;
    padding: 8px;
		.bar_line {
			margin-top: 0;
			width: 98%;
      .text {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
		}
    &::-webkit-scrollbar-track-piece {
      background-color: transparent;
    }
    &::-webkit-scrollbar {
      width:3px;
    }
    &::-webkit-scrollbar-thumb {
      background-color: #15E2DB;
    }
	}
}
#waterQualityPie {
  div {
    height: 100% !important;
  }
}
</style>