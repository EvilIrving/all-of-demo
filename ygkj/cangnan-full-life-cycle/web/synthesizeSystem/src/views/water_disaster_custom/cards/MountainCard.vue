<!--
 * @Author: your name
 * @Date: 2021-07-04 10:31:18
 * @LastEditTime: 2021-07-07 11:00:06
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\MountainCard.vue
-->
<script>
import PanelItem from "../components/PanelItem.vue";
import { mapActions, mapGetters } from "vuex";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
import EchartPie from "../../WaterDisasterPreventionScreen/charts/echart_pie/EchartPie.vue"; // 积水饼图
export default {
  name: "MountainCard",
  layerName: ["normalPointLayer","importPointLayer","areaWKTLayer"],
  legendName: ["村落","危险区"],
  prop: {},
  data() {
    return {
      ylbtn: false,
      tabActive: "预报",
      //风险隐患饼图数据
      projRiskSeriesData: [
        {
          value: 2002,
          scale: 66.13,
          name: "危险区",
          total: "3027",
          color: "#1CFFF5",
        },
        {
          value: 709,
          scale: 23.42,
          name: "高危险区",
          total: "3027",
          color: "#EEC80B",
        },
        {
          value: 316,
          scale: 10.45,
          name: "极高危险区",
          total: "3027",
          color: "#FF7144",
        },
      ],
      projRiskExtraOption: {
        tooltip: {
          show: true,
          trigger: "item",
          formatter: function (data) {
            return (
              data.data.name +
              ":" +
              "  " +
              data.data.value +
              "个" +
              "  " +
              "(" +
              data.data.scale +
              "%" +
              ")"
            );
          },
          confine: true,
          extraCssText: "width:160px;height:50px;font-size:18px;",
        },
        series: [
          {
            type: "pie",
            radius: ["60%", "90%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: "center",
                color: "#fff",
                fontSize: 18,
                formatter: 3027 + "\n" + "总数", // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
              },
            },
            labelLine: {
              show: false,
            },
          },
        ],
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
                color: "#1CFFF5", // 0% 处的颜色
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
                color: "#EEC80B", // 0% 处的颜色
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
                color: "#FF7144", // 0% 处的颜色
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
      "torrentsWarning",
      "effectTown",
      "previewModule",
      "formatterCurrentTime",
      "AffairId"
    ]),
    currentTime() {
      let time = this.formatterCurrentTime();
      return time != "Invalid date" ? time : "请选择时间";
    },
  },
  watch: {
    currentTime: {
      immediate: true,
      handler(val) {
        this.$nextTick(() => {
          if (this.currentTime != "请选择时间") {
            this.tabChange("监测");
          } else {
            this.getTorrentsWarning();
          }
        });
      },
    },
    previewModule(val){
      if(val){
        clearInterval(this.timer);
      }else{
        this.timer = setInterval(()=>{
          let time = new Date().format("yyyy-MM-dd hh:mm:ss");
          this.getTorrentsWarning(time);
        },300000);
      }
    }
  },
  mounted() {
    let time = new Date().format("yyyy-MM-dd hh:mm:ss");
    this.getTorrentsWarning(time);
    this.timer = setInterval(()=>{
      let time = new Date().format("yyyy-MM-dd hh:mm:ss");
      this.getTorrentsWarning(time);
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["getTorrentsWarning", "SetActiveCount", "SetMountainCardTab"]),
    // 标题点击打开统计卡片
    tclick() {
      this.$emit("tclick", "山洪预警");
    },
    renderPanelLeft() {
      return (
        <div class="torrent-total-info__page bottom-torrent-total-info__page">
          <div class="page-item">
            <i class="icon-card-bg5" />
            <div class="page-item__label">涉及县市区</div>
            <div class="page-item__data">
              <span class="number">12</span>
              <span class="unit">个</span>
            </div>
          </div>
          <div class="page-item">
            <i class="icon-card-bg7" />
            <div class="page-item__label">防治乡镇</div>
            <div class="page-item__data">
              <span class="number">184</span>
              <span class="unit">个</span>
            </div>
          </div>
          <div class="page-item">
            <i class="icon-card-bg6" />
            <div class="page-item__label">防治村落</div>
            <div class="page-item__data">
              <span class="number">7200</span>
              <span class="unit">个</span>
            </div>
          </div>
          <div class="page-item">
            <i class="icon-card-bg8" />
            <div class="page-item__label">影响人口</div>
            <div class="page-item__data">
              <span class="number">12.98</span>
              <span class="unit">万人</span>
            </div>
          </div>
        </div>
      );
    },
    renderPanelRight() {
      return (
        <el-carousel-item class="torrent-total-info__page">
          <div class="page-item">
            <i class="icon-card-bg1" />
            <div class="page-item__label">总数</div>
            <div class="page-item__data">
              <span class="number">3027</span>
              <span class="unit">个</span>
            </div>
          </div>
          <div class="page-item">
            <i class="icon-card-bg2" />
            <div class="page-item__label">危险区</div>
            <div class="page-item__data">
              <span class="number">2002</span>
              <span class="unit">个</span>
            </div>
          </div>
          <div class="page-item">
            <i class="icon-card-bg3" />
            <div class="page-item__label">高危险区</div>
            <div class="page-item__data">
              <span class="number">709</span>
              <span class="unit">个</span>
            </div>
          </div>
          <div class="page-item">
            <i class="icon-card-bg4" />
            <div class="page-item__label">极高危险区</div>
            <div class="page-item__data">
              <span class="number">316</span>
              <span class="unit">个</span>
            </div>
          </div>
        </el-carousel-item>
      );
    },
    renderTwoCard() {
      return (
        // <el-carousel-item class="card-capsule-content">
        <div class="card-capsule-content">
          <div class="capsule-tabs-top">
            <div class="capsule-tabs">
              <div
                onClick={() => {
                  this.tabChange("预报");
                }}
                class={["capsule-tabs__item", this.tabActive == "预报" ? "active" : ""]}
              >
                预报
              </div>
              <div
                onClick={() => {
                  this.tabChange("监测");
                }}
                class={["capsule-tabs__item", this.tabActive == "监测" ? "active" : ""]}
              >
                监测
              </div>
            </div>
          </div>
          <div class="torrent-total-info__page">
            <div class="possibility-card yellow">
              <div class="card-title">
                {this.tabActive == "监测" ? "准备转移" : "有可能发生山洪"}
              </div>
              <div class="card-content">
                <div class="card-content-item">
                  <div class="content-label">县区</div>
                  <div>
                    <span class="count-number">
                      {this.torrentsWarning.grade4County || 0}
                    </span>
                    <span class="unit">个</span>
                  </div>
                </div>
                <div class="card-content-item">
                  <div class="content-label">乡镇</div>
                  <div>
                    <span class="count-number">
                      {this.torrentsWarning.grade4street || 0}
                    </span>
                    <span class="unit">个</span>
                  </div>
                </div>
                <div class="card-content-item">
                  <div class="content-label">村落</div>
                  <div>
                    <span class="count-number">
                      {this.torrentsWarning.grade4villages || 0}
                    </span>
                    <span class="unit">个</span>
                  </div>
                </div>
                <div class="card-content-item">
                  <div class="content-label">人口</div>
                  <div>
                    <span class="count-number">
                      {this.torrentsWarning.grade4Person || 0}
                    </span>
                    <span class="unit">人</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="possibility-card">
              <div class="card-title">
                {this.tabActive == "监测" ? "立即转移" : "可能性很大"}
              </div>
              <div class="card-content">
                <div class="card-content-item">
                  <div class="content-label">县区</div>
                  <div>
                    <span class="count-number">
                      {this.torrentsWarning.grade5County || 0}
                    </span>
                    <span class="unit">个</span>
                  </div>
                </div>
                <div class="card-content-item">
                  <div class="content-label">乡镇</div>
                  <div>
                    <span class="count-number">
                      {this.torrentsWarning.grade5street || 0}
                    </span>
                    <span class="unit">个</span>
                  </div>
                </div>
                <div class="card-content-item">
                  <div class="content-label">村落</div>
                  <div>
                    <span class="count-number">
                      {this.torrentsWarning.grade5villages || 0}
                    </span>
                    <span class="unit">个</span>
                  </div>
                </div>
                <div class="card-content-item">
                  <div class="content-label">人口</div>
                  <div>
                    <span class="count-number">
                      {this.torrentsWarning.grade5Person || 0}
                    </span>
                    <span class="unit">人</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        // </el-carousel-item>
      );
    },
    renderChart() {
      return (
        <div class="chart-box">
          <el-row style="margin-top: 6px">
            <el-col span={11} class="hydrops-sty">
              <EchartPie
                seriesData={this.projRiskSeriesData}
                extraOption={this.projRiskExtraOption}
              ></EchartPie>
            </el-col>
            <el-col span={13} class="hydrops-sty" style="overflow-y: auto">
              <div>
                {this.projRiskSeriesData.map((item, index) => {
                  let style = {
                    "vertical-align": "baseline",
                    background: item.color,
                  };
                  return (
                    <div>
                      <p>
                        <i style={style} class="icon-sty  risk-legend" />
                        {item.name}
                      </p>
                      <p>
                        <span> {item.value} </span> 个
                      </p>
                    </div>
                  );
                })}
              </div>
            </el-col>
          </el-row>
        </div>
      );
    },
    // 地图落点
    drawFloodWarningPoint() {
      if (!this.ylbtn) {
        this.ScreenMap.drawPoint([], {}, "floodWarningLayer");
        this.ScreenMap.clearAreaWKTLayer();
        this.$emit("legend-change", "村落", {});
        this.$emit("legend-change", "危险区", {});
        return;
      }
      let style = {
        src: require("@/assets/images/MapPoint/torrents.png"),
        anchor: [27, 30],
        anchorXUnits: "pixels",
        scale: 0.7,
      };
      let pointList = [];
      let towns = this.effectTown || [];
      for (let town of towns) {
        let list = town.villages.map((item) => {
          return {
            name: item.adnm,
            lng: item.lng,
            lat: item.lat,
            type: "山洪预警",
            label: item.desc,
          };
        });
        pointList = pointList.concat(list);
      }
      this.ScreenMap.drawPoint(pointList, style, "floodWarningLayer");
      this.$emit("legend-change", "村落", {
        label: "村落",
        children: [
          {
            label: "一般村落",
            icon: "icon-normal-village",
          },
          {
            label: "重要村落",
            icon: "icon-important-village",
          },
        ],
      });
      this.$emit("legend-change", "危险区", {
        label: "危险区",
        children: [
          {
            label: "危险区",
            icon: "icon-danger-area",
          },
          {
            label: "高危险区",
            icon: "icon-moredanger-village",
          },
          {
            label: "极高危险区",
            icon: "icon-mostdanger-village",
          },
        ],
      });
    },
    async listPrevTfInfo() {
      if (!this.ylbtn) {
        this.ScreenMap.drawClusterPoint([], {}, "normalPointLayer");
        this.ScreenMap.drawClusterPoint([], {}, "importPointLayer");
        return;
      }
      let opt = {};
      let res = await disasterPreventionApi.listPrevTfInfo(opt);
      let normalArr = [];
      let importArr = [];
      let areaPolyArr = [];
      for (let i = 0; i < res.data.length; i++) {
        if (res.data[i].recType == "一般村落") {
          let item = res.data[i];
          item.src = require("@/assets/images/MapPoint/normal-village.png");
          item.lng = item.lon;
          item.name = item.avi;
          item.type = "山洪";
          item.props = {
            总户数: item.tho + "户",
            总人口: item.tpo + "人",
          };
          normalArr.push(item);
        } else if (res.data[i].recType == "重要村落") {
          let item = res.data[i];
          item.lng = item.lon;
          item.type = "山洪";
          item.src = require("@/assets/images/MapPoint/important-village.png");
          item.name = item.avi;
          item.props = {
            总户数: item.tho + "户",
            总人口: item.tpo + "人",
          };
          importArr.push(item);
        } else {
          let item = res.data[i];
          item.type = "山洪";
          item.coordinates = item.areaPoints;
          areaPolyArr.push(item);
        }
      }
      const normalStyle = {
        src: require("@/assets/images/MapPoint/normal-village.png"),
        srcs: require("@/assets/images/MapPoint/normal-village-group.png"),
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
      };
      const importStyle = {
        src: require("@/assets/images/MapPoint/important-village.png"),
        srcs: require("@/assets/images/MapPoint/important-village-group.png"),
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
      };
      this.ScreenMap.drawClusterPoint(normalArr, normalStyle, "normalPointLayer");
      this.ScreenMap.drawClusterPoint(importArr, importStyle, "importPointLayer");
      this.ScreenMap.drawAreaWKT(areaPolyArr);
    },
    tabChange(type) {
      if (this.previewModule && type == "预报") return;
      this.SetMountainCardTab(type);
      this.SetActiveCount("山洪");
      this.tabActive = type;
      if (type == "预报") {
        this.getTorrentsWarning();
      } else {
        let time = new Date(this.formatterCurrentTime()).format("yyyy-MM-dd hh:mm:ss");
        let opt;
        if (this.previewModule) {
          opt = {
            currentFlag: false,
            time,
            drillAffairId:this.AffairId
          };
        } else {
          time = new Date().format("yyyy-MM-dd hh:mm:ss");
          opt = {
            currentFlag: true,
            time,
          };
        }
        this.getTorrentsWarning(opt);
      }
    },
  },

  render() {
    let leftPart = this.renderPanelLeft();
    let rightPart = this.renderPanelRight();
    let twoCard = this.renderTwoCard();
    let chart = this.renderChart();
    let yinzhangClass = [
      this.ylbtn ? "icon-yinzhang-active" : "icon-yinzhang-1",
      "icon-sty",
    ];
    return (
      <PanelItem
        inSimulation={this.previewModule}
        title="山洪预警"
        hasSwitch
        class="panel-torrent"
        tc
        onTclick={this.tclick}
      >
        <template slot="title-switch">
          <i
            title="地图落点"
            onClick={() => {
              this.ylbtn = !this.ylbtn;
              this.drawFloodWarningPoint();
              this.listPrevTfInfo();
            }}
            class={yinzhangClass}
          />
        </template>
        <div class="torrent-info">
          <div class="torrent-total-info">
            {twoCard}
            {chart}
            {leftPart}
          </div>
        </div>
      </PanelItem>
    );
  },
};
// {leftPart}
// {rightPart}
// <el-carousel
//   trigger="click"
//   arrow="never"
//   interval={5000}
//   autoplay={false}
// >
// </el-carousel>
</script>
<style lang="scss" scoped>
.panel-torrent {
  color: #fff;
  ::v-deep.el-carousel__indicators {
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
  .chart-box {
    height: 170px;
    width: 100%;
  }

  .hydrops-sty {
    height: 170px;
    color: #fff;
    font-size: 16px;
    font-weight: 400;
    &:last-child {
      > div {
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        padding: 30px 10px;
      }
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
  .bottom-torrent-total-info__page.torrent-total-info__page {
    height: calc(62% - 186px);
    width: 100%;
  }
  .torrent-info {
    padding-top: 8px;
    height: 100%;
    .torrent-total-info {
      // background-image: url(~assets/images/ScreenAsset/torrents-bg1.png);
      // background-repeat: no-repeat;
      // background-size: 100% 100%;
      height: 100%;
      ::v-deep.el-carousel {
        height: 100%;
        .el-carousel__container {
          height: 100%;
        }
      }
      .torrent-total-info__page {
        padding: 0 16px;
        @include flexbox;
        @include flexflow(row, wrap);
        // @include flexAC;
        @include flexJC(space-around);

        .page-item {
          height: 50%;
          width: 50%;
          //   background-image: linear-gradient(
          //     90deg,
          //     rgba(28, 255, 245, 0.2) 0%,
          //     rgba(249, 249, 249, 0) 50%
          //   );
          position: relative;
          // background-image: url(~assets/images/ScreenAsset/torrents-bg1.png);
          // background-repeat: no-repeat;
          // background-size: 95% 100%;
          @include flexbox;
          @include flexflow(column, nowrap);
          @include flexJC(space-around);
          @include flexAC;
          &:nth-child(odd) {
            border-right: 1px solid #00bfff73;
          }
          &:nth-child(1),
          &:nth-child(2) {
            border-bottom: 1px solid #00bfff73;
          }
          padding: 10px 0;
          .page-item__label {
            // display: flex;
            text-align: center;
            i {
              position: absolute;
              right: 0;
              bottom: 0;
              margin-right: 8px;
            }
            .title {
              font-size: 18px;
              padding-left: 8px;
            }
            .number {
              font-size: 44px;
              color: #17cfcb;
              margin-left: 8px;
            }
            &.baseline {
              align-items: flex-end;
            }
          }
          .page-item__data {
            .number {
              font-size: 28px;
              color: #1cfff5;
            }
          }
        }
      }
    }
    .torrent-possibility {
      .capsule-tabs {
        @include flexJC(flex-end);
        width: 100%;
      }
      .possibility-card {
        height: 160px;
        display: flex;
        flex-direction: column;
        align-items: center;
        .card-title {
          font-family: PingFangSC-Semibold;
          font-size: 20px;
          color: #ff7144;
          margin: 16px auto;
        }
        .card-content {
          display: flex;
          flex-wrap: wrap;
          width: 100%;
          .card-content-item {
            width: 50%;
            text-align: center;
            margin-bottom: 16px;
            .content-label {
              margin-bottom: 6px;
              color: #fff;
            }
            .count-number {
              font-size: 20px;
              color: #ff7144;
            }
          }
        }
        &.yellow {
          .card-title {
            color: #e2c417;
          }
          .card-content {
            .card-content-item {
              .content-label {
                color: #fff;
              }
              .count-number {
                color: #e2c417;
              }
              .unit{
                color: #fff;
              }
            }
          }
        }

        &:first-child {
          background-image: url(~assets/images/ScreenAsset/torrents-bg3.png);
          background-repeat: no-repeat;
          background-size: 100%;
          margin-bottom: 10px;
        }
        &:last-child {
          background-image: url(~assets/images/ScreenAsset/torrents-bg2.png);
          background-repeat: no-repeat;
          background-size: 100%;
        }
      }
    }
  }
  .card-capsule-content {
    // height: 100%;
    // overflow-y: auto;
    height: 38%;
    @include flexbox;
    @include flexflow(column, nowrap);
    .capsule-tabs-top {
      padding: 0 16px;
      margin-bottom: 16px;
    }
    .torrent-total-info__page {
      padding: 16px 0;
      @include flex(1);
      @include flexbox;
      .possibility-card {
        @include flex(1);
        @include flexbox;
        flex-direction: column;
        // @include flexflow(column, nowrap);
        height: 100%;
        padding: 12px 0;
        color: #ff7144;
        background: url(~assets/images/custom_card/flood-card-bg2.png) no-repeat bottom
          right;
        background-size: 100% 100%;
        &:first-child {
          margin-right: 16px;
        }
        .count-number {
          color: #ff7144;
        }
        &.yellow {
          color: #e2c417;
          background: url(~assets/images/custom_card/flood-card-bg1.png) no-repeat bottom
            right;

          .count-number {
            color: #e2c417;
          }
          .content-label {
            color: #fff;
          }
          .unit{
            color: #fff;
          }
        }
        .card-title {
          font-size: 20px;
          width: 100%;
          @include flexbox;
          @include flexJC(center);
        }
        .card-content {
          @include flexbox;
          @include flexJC(space-around);
          @include flex(1);
          @include flexflow(row, wrap);
          padding: 0 16px;
          .card-content-item {
            width: 100%;
            @include flexbox;
            // @include flexflow(column, nowrap);
            @include flexJC(space-between);
            @include flexAC;
            font-size: 20px;
            color: #fff;
            .unit {
              font-size: 14px;
            }
            .count-number {
              font-size: 40px;
            }
          }
        }
      }
    }
  }
}
</style>
