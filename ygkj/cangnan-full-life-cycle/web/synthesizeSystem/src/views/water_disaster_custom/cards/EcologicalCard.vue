<!--
 * @Author: your name
 * @Date: 2021-06-30 18:17:27
 * @LastEditTime: 2021-07-06 22:04:35
 * @LastEditors: Please set LastEditors
 * @Description: 流域生态流量小组件
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\SeawallCard.vue
-->
<template>
  <div class="ecological-card">
    <panel-item title="流域生态流量" has-switch tc @tclick="tclick">
      <template slot="title-switch">
        <i
          title="地图落点"
          @click.stop="handleEcologicalChange('htbtn')"
          :class="[
            htbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('流域生态流量')"
        />
      </template>
      <div class="ecological-card-content">
        <div class="tab-type">
          <card-tabs
            ref="typeList"
            v-model="ecologicalSelect"
            :list="typeList"
            :decimalsFlag="false"
            :col="6"
            @change="tabTypeClick"
          />
        </div>
        <div class="chart-box">
          <EchartPie
            ref="chartCapsule"
            :seriesData="statisticData"
          ></EchartPie>
        </div>
      </div>
    </panel-item>
  </div>
</template>

<script>
import CardTabs from "../../WaterDisasterPreventionScreen/components/CardTabs.vue";
import EchartPie from "../charts/echart_solidPie/EchartPie.vue";
import PanelItem from "../components/PanelItem.vue";
import { mapGetters, mapActions } from "vuex";
import { waterResourceApi } from "@/api/waterResourcesGuarantee/index";
export default {
  name: "EcologicalCard",
  layerName: ["warnfractureLayer","normalfractureLayer","errorfractureLayer"],
  legendName: "断面",
  components: {
    PanelItem,
    CardTabs,
    EchartPie
  },
  created() {
    this.getSectFlowList();
  },
  mounted() {
    this.$nextTick(() => {
      this.stats4SectFlow();
      // this.getChartData();
    });
  },
  beforeDestroy() {
    this.$nextTick(() => {
    })
    // clearInterval(this.timer);
  },
  data() {
    return {
      timer: null,
      interval: 5000,
      htbtn: false, //流域生态流量点位展示标记
      ecologicalSelect: 0,
      statisticObj: null,
      statisticData: [],
      pointList: []
    };
  },
  computed: {
    ...mapGetters([
      "ScreenMap",
    ]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
    typeList() {
      let list = [{
        label: "断面总数",
        data: { number: 0, unit: null },
      }, {
        label: "瓯江流域",
        data: { number: 0, unit: null },
      }, {
        label: "飞云江流域",
        data: { number: 0, unit: null },
      }, {
        label: "鳌江总数",
        data: { number: 0, unit: null },
      }];
      if (this.statisticObj) {
        list.map(item => {
          switch (item.label) {
            case '断面总数': {
              item.data.number = this.statisticObj.totalNum;
            } break;
            case '瓯江流域': {
              item.data.number = this.statisticObj.ojNum;
            } break;
            case '飞云江流域': {
              item.data.number = this.statisticObj.fyjNum;
            } break;
            case '鳌江总数': {
              item.data.number = this.statisticObj.ajNum;
            } break;
          }
        })
      }
      return list;
    }
  },
  methods: {
    ...mapActions(["SetEmergencyCount", "SetDetailDialogParam"]),
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    handleTabChange(val) {
      this.activeTab = val;
    },
    tabTypeClick(val) {
      this.ecologicalSelect = val;
    },
    async stats4SectFlow() {
      // console.log(122);
      let res = await waterResourceApi.stats4SectFlow();
      this.statisticObj = res.data;
      this.SetEmergencyCount(res.data);
      this.typeList.map(item => {
        switch (item.label) {
          case '断面总数': {
            item.number = res.data.totalNum;
          } break;
          case '瓯江流域': {
            item.number = res.data.ojNum;
          } break;
          case '飞云江流域': {
            item.number = res.data.fyjNum;
          } break;
          case '鳌江总数': {
            item.number = res.data.ajNum;
          } break;
        }
      })
      this.getChartData();
      // this.$refs.typeList.$forceUpdate();
      // console.log(res,123);
    },
    getChartData() {
      // console.log(this.statisticObj, 333)
      this.statisticData = [{
        data: [
          { value: this.statisticObj.normNum, name: '正常断面' },
          { value: this.statisticObj.orangeWarnNum, name: '橙色预警断面' },
          { value: this.statisticObj.redWarnNum, name: '红色预警断面' }
        ]
      }];
      this.$refs.chartCapsule.updateChartView();
    },
    async getSectFlowList() {
      let res = await waterResourceApi.getSectFlowList();
      this.pointList = res.data;
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
      this.SetDetailDialogParam({
        mediumSized: "",
        codeTpye: val,
        analyseDialogShow: true,
      });
    },
    handleEcologicalChange(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        distance: parseInt(10, 10),
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/MapPoint/fracture-normal-group.png"),
        src: require("@/assets/images/MapPoint/fracture-normal.png"),
      };
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        distance: parseInt(10, 10),
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/MapPoint/fracture-warn-group.png"),
        src: require("@/assets/images/MapPoint/fracture-warn.png"),
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        distance: parseInt(10, 10),
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/MapPoint/fracture-error-group.png"),
        src: require("@/assets/images/MapPoint/fracture-error.png"),
      }
      let normalList = [];
      let warnList = [];
      let errorList = [];
      for (let point of this.pointList) {
        let obj = {
          name: point.mainStName,
          type: "断面",
          lng: Number(point.stLong),
          lat: Number(point.stLat),
          code: point.mainStCode,
          data:point,
          props:{
            '实时流量':(point.flow || '-') + 'm³/s',
            '橙色预警指标':(point.orangeFlow || '-') + 'm³/s',
            '红色预警指标':(point.redFlow || '-') + 'm³/s'
          }
        };
        if (point.warnType == 1) {
          warnList.push(obj);
        } else if(point.warnType == 2){
          errorList.push(obj);
        }else{
          normalList.push(obj);
        }
      }
      if (this[btnType]) {
        this.map.drawClusterPoint(warnList, style1, "warnfractureLayer");
        this.map.drawClusterPoint(normalList, style, "normalfractureLayer");
        this.map.drawClusterPoint(errorList, style1, "errorfractureLayer");
        this.$emit("legend-change", "断面", {
          label: "断面",
          children: [
            {
              label: "正常断面",
              icon: "icon-legend-fracture-normal",
            },
            {
              label: "橙色预警断面",
              icon: "icon-legend-fracture-warn",
            },{
              label: "红色预警断面",
              icon: "icon-legend-fracture-error",
            },
          ],
        });
      } else {
        this.map.drawClusterPoint([], style1, "warnfractureLayer");
        this.map.drawClusterPoint([], style, "normalfractureLayer");
        this.map.drawClusterPoint([], style2, "errorfractureLayer");
        this.$emit("legend-change", "断面", {});
      }
    }
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.ecological-card {
  height: 100%;
  .ecological-card-content {
    width: 100%;
    height: 100%;
    @include flexbox;
    @include flexflow(column, nowrap);
    .tab-type {
      padding-top: 8px;
      ::v-deep .card-tabs-list {
        .tabs-list-item {
          .tabs-list-item__content {
            .item-data {
              margin-bottom: 8px;
              .data-number {
                font-size: 24px;
                font-weight: bold;
              }
            }
            .item-label {
              font-size: 16px;
            }
          }
        }
      }
    }
    .chart-box {
      width: 100%;
      @include flex(1);
    }
  }
  // padding: 0 16px;
  ::v-deep.el-carousel {
    height: 100%;
    .el-carousel__container {
      height: 100%;
    }
  }
  .font-small {
    font-size: 14px;
  }
  .font-big {
    font-size: 28px;
  }
  .count-box {
    width: 100%;
    height: 48px;
    line-height: 48px;
    background: rgba(28, 255, 245, 0.1);
    border: 1px solid rgba(28, 255, 245, 0.5);
    border-radius: 2px;
    font-size: 16px;
    text-align: center;
    margin-top: 14px;
    margin-bottom: 12px;
    .font-big {
      margin: 0 6px;
      color: #1cfff5;
      font-size: 24px;
    }
    .font-middle {
      margin-right: 16px;
    }
  }
  .count-sub {
    display: flex;
    justify-content: space-between;
    height: calc(100% - 110px);
    text-align: center;
    > div {
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      width: calc((100% - 24px) / 4);
      margin-right: 8px;
      text-align: center;
      &:nth-child(1) {
        background: rgba(0, 143, 232, 0.1);
        border: 1px solid rgba(0, 143, 232, 0.5);
        border-radius: 2px;
        .font-big {
          color: #008fe8;
        }
      }
      &:nth-child(2) {
        background: rgba(238, 200, 11, 0.1);
        border: 1px solid rgba(238, 200, 11, 0.5);
        border-radius: 2px;
        .font-big {
          color: #eec80b;
        }
      }
      &:nth-child(3) {
        background: rgba(255, 85, 31, 0.1);
        border: 1px solid rgba(255, 85, 31, 0.5);
        border-radius: 2px;
        .font-big {
          color: #ff551f;
        }
      }
      &:nth-child(4) {
        background: rgba(232, 0, 0, 0.1);
        border: 1px solid rgba(232, 0, 0, 0.5);
        border-radius: 2px;
        margin-right: 0px;
        .font-big {
          color: #e80000;
        }
      }
    }
  }
  .hydrops-sty {
    display: flex;
    justify-content: center;
    height: 150px;
    color: #fff;
    font-size: 16px;
    font-weight: 400;
    > div {
      padding: 0 10px;
      > div {
        display: flex;
        padding: 5px;
        justify-content: space-between;
        > p:nth-child(2) {
          > span {
            color: #1cfff5;
          }
        }
      }
    }
  }
  .hydrops-legend {
    width: 100%;
    > div {
      display: flex;
      flex-wrap: wrap;
      width: 100%;
      padding-left: 20px;
      > div {
        width: 33.33%;
        line-height: 30px;
        font-size: 16px;
      }
    }
  }
  .icon-sty {
    margin-right: 10px;
    &.icon-year-100 {
      @include chart-icon();
      background: #19af47;
    }
    &.icon-year-50 {
      @include chart-icon();
      background: #eec80b;
    }
    &.icon-year-20 {
      @include chart-icon();
      background: #e89100;
    }
    &.icon-year-10 {
      @include chart-icon();
      background: #dd00db;
    }
    &.icon-year-5 {
      @include chart-icon();
      background: #e80000;
    }
    &.risk-legend {
      width: 16px;
      height: 16px;
      border-radius: 50%;
      display: inline-block;
      @include chart-icon();
    }
  }
  .background-sty {
    background: url(~assets/images/WaterDisastersImage/bai-jing-style.png)
      no-repeat top center;
    background-size: 100% 100%;
  }
  .sea-level-style {
    border-top: 1px solid rgba($color: #00c1ff, $alpha: 0.5) !important;
    border-bottom: 1px solid rgba($color: #00c1ff, $alpha: 0.5) !important;
  }
  .sea-left-level-style {
    border-left: 1px solid rgba($color: #00c1ff, $alpha: 0.5) !important;
    border-right: 1px solid rgba($color: #00c1ff, $alpha: 0.5) !important;
  }
}
</style>
<style lang="scss">
.ecological-card .panel-item .panel-item__content--tabs .tab-button,
.ecological-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.ecological-card .panel-item .panel-item__content--tabs .tab-button.active,
.ecological-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
