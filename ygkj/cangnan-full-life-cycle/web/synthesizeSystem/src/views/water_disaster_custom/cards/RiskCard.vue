<!--
 * @Author: your name
 * @Date: 2021-06-30 18:18:43
 * @LastEditTime: 2021-07-04 10:51:20
 * @LastEditors: Please set LastEditors
 * @Description: 风险小组件
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\RiskCard.vue
-->
<template>
  <div class="risk-card">
    <panel-item
      title="风险隐患"
      has-switch
      :action-tabs="['风险类型', '隐患点销号情况', '薄弱点销号情况']"
      @change="potentialRisksChange"
      tc
      @tclick="tclick"
    >
      <!-- 积水 标题右侧图标点击事件 -->
      <template slot="title-switch">
        <i
          title="地图落点"
          @click.stop="handleRiskChange('fxyhBtn')"
          :class="[
            fxyhBtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i class="icon-liebiao icon-sty" @click.stop="openDetailsDialog('风险隐患')" />
        <!-- <i class="icon-yizhangtu icon-sty" /> -->
      </template>
      <!-- 图标详情 -->
      <el-row class="card-container" style="margin-top: 6px">
        <el-col :span="10" class="hydrops-sty">
          <echartPie
            :seriesData="projRiskSeriesData"
            :extraOption="projRiskExtraOption"
          ></echartPie>
        </el-col>
        <el-col
          :span="14"
          class="hydrops-sty"
          style="padding-top: 15px; overflow-y: auto"
        >
          <div>
            <div v-for="(item, index) in projRiskSeriesData" :key="index">
              <el-tooltip class="item" effect="dark" :content="item.name" placement="left">
                <p style="width: 120px; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
                  <i :style="{ 'overflow': 'hidden', background: item.color, }" class="icon-sty risk-legend" /> {{ item.name }}
                </p>
              </el-tooltip>
              <p>
                <span> {{ item.scale }} </span> %
              </p>
            </div>
          </div>
        </el-col>
      </el-row>
    </panel-item>
  </div>
</template>

<script>
  import PanelItem from "../components/PanelItem.vue";
  import { disasterPreventionApi } from "@/api/RightSildePanel";
  import { mapGetters, mapActions } from "vuex";
  import EchartPie from "../../WaterDisasterPreventionScreen/charts/echart_pie/EchartPie.vue"; // 积水饼图
  export default {
    name: "RiskCard",
    layerName: ["riskLayer", "dangerLayer", "weakLayer"],
    legendName: "风险隐患",
    components: {
      PanelItem,
      EchartPie,
    },
    mounted() {
      this.$nextTick(() => {
        this.projRiskStats("风险类型"); //分线隐患接口
        this.getbusRiskListPage();
        this.timer = setInterval(()=>{
          this.projRiskStats("风险类型"); //分线隐患接口
          this.getbusRiskListPage();
        },300000);
      });
    },
    beforeDestroy(){
      clearInterval(this.timer);
    },
    data() {
      return {
        fxyhBtn: false, //风险隐患点位展示标记
        //风险隐患
        projRiskData: {},
        //风险隐患饼图数据
        projRiskSeriesData: [
          {
            value: 173,
            scale: 96.95,
            name: "隐患点",
            type: "风险类型",
            total: "179",
          },
          {
            value: 0,
            scale: 0,
            name: "高风险点",
            type: "风险类型",
            total: "179",
          },
          {
            value: 6,
            scale: 3.35,
            name: "薄弱点",
            type: "风险类型",
            total: "179",
          },
        ],
        riskPointList: [], //风险点集合
        projRiskExtraOption: {
          tooltip: {
            show: true,
            trigger: "item",
            formatter: function (data) {
              return (
                data.data.type +
                "<br/>" +
                data.data.name +
                ":" +
                "  " +
                data.data.value +
                "个" +
                "  " +
                "(" +
                data.data.scale +
                "%" +
                ")" +
                "<br/>" +
                "总计 : " +
                data.data.total
              );
            },
            confine: true,
            extraCssText: "width:160px;height:50px;font-size:18px;",
          },
          color: [
            {
              type: "linear",
              x: 0,
              y: 0,
              x2: 1,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: "#E89100", // 0% 处的颜色
                },
                {
                  offset: 1,
                  color: "rgba(0,0,0,0.7)", // 100% 处的颜色
                },
              ],
              global: false, // 缺省为 false
            },
            {
              type: "linear",
              x: 0,
              y: 0,
              x2: 1,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: "#008FE8", // 0% 处的颜色
                },
                {
                  offset: 1,
                  color: "rgba(0,0,0,0.7)", // 100% 处的颜色
                },
              ],
              global: false, // 缺省为 false
            },
            {
              type: "linear",
              x: 0,
              y: 0,
              x2: 1,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: "#19AF47", // 0% 处的颜色
                },
                {
                  offset: 1,
                  color: "rgba(0,0,0,0.7)", // 100% 处的颜色
                },
              ],
              global: false, // 缺省为 false
            },
            {
              type: "linear",
              x: 0,
              y: 0,
              x2: 1,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: "#1CFFF5", // 0% 处的颜色
                },
                {
                  offset: 1,
                  color: "rgba(0,0,0,0.7)", // 100% 处的颜色
                },
              ],
              global: false, // 缺省为 false
            },
          ],
        },
        timer:null,
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
      openDetailsDialog(val) {
        this.SetDetailDialogParam({
          mediumSized: "",
          codeTpye: val,
          analyseDialogShow: true,
        });
      },
      /**
       * 跳转一张图
       */
      goToAmap(type) {
        this.$router.push(`/hydraulicAmap?selectType=${type}`);
      },

      // handleRiskChange(btnType) {
      //   this[btnType] = !this[btnType];
      //   let style = {
      //     scale: 0.7,
      //     anchor: [27, 30],
      //     anchorXUnits: "pixels",
      //     srcs: require("@/assets/images/MapPoint/risk-group.png"),
      //     src: require("@/assets/images/MapPoint/risk-point.png"),
      //   };
      //   let style1 = {
      //     scale: 0.7,
      //     anchor: [27, 30],
      //     anchorXUnits: "pixels",
      //     srcs: require("@/assets/images/MapPoint/hiddentrouble-group.png"),
      //     src: require("@/assets/images/MapPoint/hiddentrouble-point.png"),
      //   };
      //   let style2 = {
      //     scale: 0.7,
      //     anchor: [27, 30],
      //     anchorXUnits: "pixels",
      //     srcs: require("@/assets/images/MapPoint/weakness-group.png"),
      //     src: require("@/assets/images/MapPoint/weakness-point.png"),
      //   };
      //   let pointList = []; //高风险点列表
      //   let dangerList = []; //隐患点列表
      //   let weakList = []; //薄弱点列表
      //   for (let point of this.riskPointList) {
      //     if (point.lat && point.lng) {
      //       let obj = {
      //         整改情况: point.recCondition,
      //         工程类型: point.projType,
      //         行政区划: point.county,
      //         整改责任单位: point.dutyUnit,
      //         上报人员: point.reportPerson,
      //         完成时间: point.reportTime,
      //       };
      //       if (point.sbSign == "252") {
      //         //高风险点
      //         pointList.push({
      //           name: point.projName,
      //           type: "风险隐患",
      //           sbSign: point.sbSign,
      //           lng: Number(point.lng),
      //           lat: Number(point.lat),
      //           code: point.sbId,
      //           props: obj,
      //         });
      //       } else if (point.sbSign == "251") {
      //         //隐患点
      //         obj = {
      //           ...obj,
      //           隐患类型: point.dangerType,
      //         };
      //         dangerList.push({
      //           name: point.projName,
      //           type: "风险隐患",
      //           sbSign: point.sbSign,
      //           lng: Number(point.lng),
      //           lat: Number(point.lat),
      //           code: point.sbId,
      //           props: obj,
      //         });
      //       } else {
      //         //薄弱点
      //         weakList.push({
      //           name: point.projName,
      //           type: "风险隐患",
      //           sbSign: point.sbSign,
      //           lng: Number(point.lng),
      //           lat: Number(point.lat),
      //           code: point.sbId,
      //           props: obj,
      //         });
      //       }
      //     }
      //   }
      //   // console.log(pointList);
      //   if (this[btnType]) {
      //     this.map.drawClusterPoint(pointList, style, "riskLayer");
      //     this.map.drawClusterPoint(dangerList, style1, "dangerLayer");
      //     this.map.drawClusterPoint(weakList, style2, "weakLayer");
      //     this.$emit("legend-change", "风险隐患", {
      //       label: "风险隐患",
      //       children: [
      //         {
      //           label: "隐患点",
      //           icon: "icon-hiddentrouble-point",
      //         },
      //         {
      //           label: "高风险点",
      //           icon: "icon-risk-point",
      //         },
      //         {
      //           label: "薄弱点",
      //           icon: "icon-weakness-point",
      //         },
      //       ],
      //     });
      //   } else {
      //     this.map.drawClusterPoint([], {}, "riskLayer");
      //     this.map.drawClusterPoint([], {}, "dangerLayer");
      //     this.map.drawClusterPoint([], {}, "weakLayer");
      //     this.$emit("legend-change", "风险隐患", {});
      //   }
      // },
      /**
       * 风险隐患tab切换
       */
      potentialRisksChange(index, type) {
        this.projRiskStats(type);
      },
      /**
       * 风险隐患数据
       */
      async projRiskStats(val) {
        if (val == "风险类型") {
          this.riskTypeStatistics();
        } else if (val == "隐患点销号情况") {
          this.cancellationHiddenDangerStatic();
        } else {
          this.cancellationWeakPointsStatic();
          this.projRiskSeriesData = [
            {
              value: 0,
              scale: 0,
              name: "未整改",
              type: "薄弱点销号情况",
              total: "6",
            },
            {
              value: 0,
              scale: 0,
              name: "整改中",
              type: "薄弱点销号情况",
              total: "6",
            },
            {
              value: 3,
              scale: 50,
              name: "已落实措施",
              type: "薄弱点销号情况",
              total: "6",
            },
            {
              value: 3,
              scale: 50,
              name: "完成整改",
              type: "薄弱点销号情况",
              total: "6",
            },
          ];
        }
      },

      /**
       * 风险类型统计
       */
      async riskTypeStatistics() {
        let res = await disasterPreventionApi.riskTypeStatistics();
        if (res.code == 0) {
          this.projRiskSeriesData = [];
          let colorObj = {
            隐患点: "#E89100",
            高风险点: "#338FE8",
            薄弱点: "#35AF47",
          };
          res.data.forEach((item) => {
            let opt = {
              value: item.num,
              scale: item.rate,
              name: item.sbSignName,
              type: "风险类型",
              total: item.total,
              color: colorObj[item.sbSignName],
            };
            this.projRiskSeriesData.push(opt);
          });
        }
        // console.log(res);
      },
      /**
       * 风险清单list
       */
      async getbusRiskListPage() {
        let res = await disasterPreventionApi.busRiskListPage({
          pageNum: 1,
          pageSize: 6000,
        });
        if (res.code == 0) {
          this.riskPointList = res.data.list;
        }
      },
      /**
       * 隐患点销号情况
       */
      async cancellationHiddenDangerStatic() {
        let res = await disasterPreventionApi.cancellationHiddenDangerStatic();
        if (res.code == 0) {
          let colorObj = {
            完成整改: "#E89100",
            已落实措施: "#008FE8",
            已落实管控措施: "#19AF47",
            已落实管控措施并进行整改: "#1CFFF5",
            未整改: "#E80000",
          };
          this.projRiskSeriesData = [];
          res.data.forEach((item) => {
            let opt = {
              value: item.num,
              scale: item.rate,
              name: item.reccondition,
              type: "隐患点销号情况",
              total: item.total,
              color: colorObj[item.reccondition],
            };
            this.projRiskSeriesData.push(opt);
          });
        }
        // console.log(res);
      },
      /**
       * 薄弱点销号情况统计
       */
      async cancellationWeakPointsStatic() {
        let res = await disasterPreventionApi.cancellationWeakPointsStatic();
        if (res.code == 0) {
          let colorObj = {
            完成整改: "#E89100",
            已落实措施: "#008FE8",
            未整改: "#19AF47",
            整改中: "#1CFFF5",
          };
          this.projRiskSeriesData = [];
          res.data.forEach((item) => {
            let opt = {
              value: item.num,
              scale: item.rate,
              name: item.reccondition,
              type: "薄弱点销号情况",
              total: item.total,
              color: colorObj[item.reccondition],
            };
            this.projRiskSeriesData.push(opt);
          });
        }
      },
      handleRiskChange(btnType) {
        this[btnType] = !this[btnType];
        let style = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/MapPoint/risk-group.png"),
          src: require("@/assets/images/MapPoint/risk-point.png"),
        };
        let style1 = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/MapPoint/hiddentrouble-group.png"),
          src: require("@/assets/images/MapPoint/hiddentrouble-point.png"),
        };
        let style2 = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/MapPoint/weakness-group.png"),
          src: require("@/assets/images/MapPoint/weakness-point.png"),
        };
        let pointList = []; //高风险点列表
        let dangerList = []; //隐患点列表
        let weakList = []; //薄弱点列表
        for (let point of this.riskPointList) {
          if (point.lat && point.lng) {
            let obj = {
              整改情况: point.recCondition,
              工程类型: point.projType,
              行政区划: point.county,
              整改责任单位: point.dutyUnit,
              上报人员: point.reportPerson,
              完成时间: point.reportTime,
            };
            if (point.sbSign == "252") {
              //高风险点
              pointList.push({
                name: point.projName,
                type: "风险隐患",
                sbSign: point.sbSign,
                lng: Number(point.lng),
                lat: Number(point.lat),
                code: point.sbId,
                props: obj,
              });
            } else if (point.sbSign == "251") {
              //隐患点
              obj = {
                ...obj,
                隐患类型: point.dangerType,
              };
              dangerList.push({
                name: point.projName,
                type: "风险隐患",
                sbSign: point.sbSign,
                lng: Number(point.lng),
                lat: Number(point.lat),
                code: point.sbId,
                props: obj,
              });
            } else {
              //薄弱点
              weakList.push({
                name: point.projName,
                type: "风险隐患",
                sbSign: point.sbSign,
                lng: Number(point.lng),
                lat: Number(point.lat),
                code: point.sbId,
                props: obj,
              });
            }
          }
        }
        // console.log(pointList);
        if (this[btnType]) {
          this.map.drawClusterPoint(pointList, style, "riskLayer");
          this.map.drawClusterPoint(dangerList, style1, "dangerLayer");
          this.map.drawClusterPoint(weakList, style2, "weakLayer");
          this.$emit("legend-change", "风险隐患", {
            label: "风险隐患",
            clusterLayer:['riskLayer','dangerLayer','weakLayer'],
            children: [
              {
                label: "隐患点",
                icon: "icon-hiddentrouble-point",
              },
              {
                label: "高风险点",
                icon: "icon-risk-point",
              },
              {
                label: "薄弱点",
                icon: "icon-weakness-point",
              },
            ],
          });
        } else {
          this.map.drawClusterPoint([], {}, "riskLayer");
          this.map.drawClusterPoint([], {}, "dangerLayer");
          this.map.drawClusterPoint([], {}, "weakLayer");
          this.$emit("legend-change", "风险隐患", {});
        }
      },
    },
  };
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.risk-card {
  height: 100%;
  // padding: 0 16px;
  .card-container {
    height: calc(100% - 30px);
  }
  ::v-deep.el-carousel {
    height: 100%;
    .el-carousel__container {
      height: 100%;
    }
  }

  .hydrops-sty {
    height: calc(100% - 30px);
    color: #fff;
    font-size: 16px;
    font-weight: 400;
    @include flexbox;
    @include flexAC;
    &::-webkit-scrollbar {
      display: none;
    }
    > div {
      padding: 10px;
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
}
</style>
<style lang="scss">
.risk-card .panel-item .panel-item__content--tabs .tab-button,
.risk-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.risk-card .panel-item .panel-item__content--tabs .tab-button.active,
.risk-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
