<!--
 * @Author: your name
 * @Date: 2021-06-30 18:17:27
 * @LastEditTime: 2021-09-18 09:54:01
 * @LastEditors: ysl
 * @Description: 海塘小组件
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/SeawallCard.vue
-->
<template>
  <div class="seawall-card">
    <panel-item title="海塘" has-switch tc @tclick="tclick">
      <template slot="title-switch">
        <i
          title="地图落点"
          @click.stop="handleSeawallChange('htbtn')"
          :class="[
            htbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('海塘')"
        />
        <i
          v-show="!previewModule"
          @click="goToAmap('海塘')"
          title="一张图"
          class="icon-yizhangtu icon-sty"
        />
      </template>

      <el-carousel trigger="click" arrow="never" :autoplay="true" :interval="interval">
        <el-carousel-item class="carousel-item-content">
          <div>
            <el-row>
              <el-col :span="24" class="hydrops-sty" style="padding: 0; height: 170px">
                <!-- <echartPie :seriesData="seawallDataList"></echartPie> -->
                <div id="seawallEchart" style="width: 232px; height: 170px"></div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" class="hydrops-legend">
                <div>
                  <div
                    v-for="(item, index) in seawallDataList"
                    :key="index"
                    style="cursor: pointer"
                    @click="focuSeawall(index)"
                  >
                    <p>
                      <i
                        style="vertical-align: middle"
                        :class="[
                          'icon-sty',
                          item.name == '100年以上'
                            ? 'icon-year-101'
                            : item.name == '100年一遇'
                            ? 'icon-year-100'
                            : item.name == '50年一遇'
                            ? 'icon-year-50'
                            : item.name == '20年一遇'
                            ? 'icon-year-20'
                            : item.name == '10年一遇'
                            ? 'icon-year-10'
                            : item.name == '非标准'
                            ? 'icon-fei-biao-zhun'
                            : 'icon-year-5',
                        ]"
                      />
                      <span>{{ item.name }}</span>
                    </p>
                    <!-- <p>
                      <span> {{ item.partLength }} </span>km
                    </p> -->
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-carousel-item>

        <el-carousel-item class="carousel-item-content carousel-padding">
          <panel-item title="" :action-tabs="[ '监测','预报']" @change="handleTabChange">
            <template v-if="activeTab == 0 || activeTab == 1">
              <div class="count-box">
                <span class="font-middle">正常</span>
                <span class="font-big">{{ normLength }}</span>
                <span class="font-small">km</span>
                <span class="font-big">{{ normNum }}</span>
                <span class="font-small">段</span>
              </div>
              <div class="count-sub">
                <div v-for="(item, index) in countArr" :key="index">
                  <p>{{ item.color }}</p>
                  <p>
                    <span class="font-big">{{ item.long }}</span
                    >km
                  </p>
                  <p>
                    <span class="font-big">{{ item.part }}</span>
                    段
                  </p>
                </div>
              </div>
            </template>
          </panel-item>
        </el-carousel-item>
      </el-carousel>
    </panel-item>
  </div>
</template>

<script>
import * as echarts from "echarts"; // echart插件
import PanelItem from "../components/PanelItem.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { disasterPreventionApi as disasterPreventionApi1 } from "@/api/waterDisasterPrevention";
import { mapGetters, mapActions } from "vuex";
import { goAmap } from "./utils/common";
export default {
  name: "ReservoirCard",
  layerName:"focuLineLayer",
  legendName:"海塘",
  components: {
    PanelItem,
  },
  mounted() {
    this.$nextTick(() => {
      this.seawallProjStats(); //海塘数据接口
      this.getRightMapPoints("海塘");
      this._seawallWarnStatistic();
      this.timer = setInterval(()=>{
        this.seawallProjStats(); //海塘数据接口
        this.getRightMapPoints("海塘");
        this._seawallWarnStatistic();
      },300000);
    });
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  data() {
    return {
      timer:null,
      interval: 5000,
      //海塘数据
      seawallData: {
        totalLength: "477.2",
      },
      //海塘列表
      seawallList: [],
      htbtn: false, //海塘点位展示标记
      //海塘曲线图数据
      seawallDataList: [
        { value: 2, name: "100年以上" },
        { value: 13, name: "100年一遇" },
        { value: 54, name: "50年一遇" },
        { value: 42, name: "20年一遇" },
        { value: 19, name: "10年一遇" },
        { value: 0, name: "10年以下" },
        // { value: 0, name: "非标准" },
      ],
      activeTab: 0,
      normLength: 0,
      normNum: 0,
      countArr: [
        {
          color: "蓝色",
          long: 0,
          part: 0,
        },
        {
          color: "黄色",
          long: 0,
          part: 0,
        },
        {
          color: "橙色",
          long: 0,
          part: 0,
        },
        {
          color: "红色",
          long: 0,
          part: 0,
        },
      ],
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
    // 获取第二页海塘数据
    async _seawallWarnStatistic() {
      await disasterPreventionApi1.seawallWarnStatistic({ drill: false,formList:null }).then((res) => {
        if (res.code == 0) {
          let info = res.data;
          this.countArr[0].long = info.blueLength;
          this.countArr[0].part = info.blueNum;
          this.countArr[1].long = info.yellowLength;
          this.countArr[1].part = info.yellowNum;
          this.countArr[2].long = info.orangeLength;
          this.countArr[2].part = info.orangeNum;
          this.countArr[3].long = info.redLength;
          this.countArr[3].part = info.redNum;
          this.normLength = info.normLength;
          this.normNum = info.normNum;
        }
      });
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
      goAmap(type);
      // this.$router.push(`/hydraulicAmap?selectType=${type}`);
    },
    /**
     * 点位list
     */
    async getRightMapPoints(type) {
      let opt = {
        type,
      };
      let res = await disasterPreventionApi.rightMapPoints(opt);
      switch (type) {
        case "海塘":
          {
            this.seawallList = res.data;
          }
          break;
      }
    },
    handleTabChange(val) {
      this.activeTab = val;
    },
    /**
     * 海塘数据接口请求
     */
    async seawallProjStats() {
      const typeMap = {
        notStandard: "非标准",
        lt10Year: "10年以下",
        eq10Year: "10年一遇",
        eq20Year: "20年一遇",
        eq50Year: "50年一遇",
        eq100Year: "100年一遇",
        gt100Year: "100年以上",
      };
      await disasterPreventionApi.seawallProjStats().then((res) => {
        if (res.code == 0) {
          this.seawallData = res.data;

          let arr = res.data.list.map((val) => {
            let obj = {
              value: val.partNum,
              partLength: val.partLength,
              lengthPercent: val.lengthPercent,
              name: typeMap[val.type],
            };
            return obj;
          });
          let pop = arr.pop();
          //添加非标准
          // arr.unshift(pop);
          this.seawallDataList = arr.reverse();
          this.$nextTick(() => {
            this.seawallEchart();
          });
        }
      });
    },
    focuSeawall(index) {
      if (this.focuIndex == index) {
        this.focuIndex = -1;
        this.map.removeFocusLineLayer();
        return;
      }
      this.focuIndex = index;
      const typeMap = [
        (val) => val > 100,
        (val) => val == 100,
        (val) => val == 50,
        (val) => val == 20,
        (val) => val == 10,
        (val) => val < 10,
        (val) => val == null,
      ];
      const colorMap = [
        "#32FFE0",
        "#2EE266",
        "#FFDF42",
        "#FFB335",
        "#FF4CFE",
        "#FF6A6A",
        "#BD0000",
      ];
      let seawallList = this.seawallList.filter((item) => {
        return typeMap[index](item.designTideStandard);
      });
      let style = {
        lineWidth: 6,
        lineColor: colorMap[index],
      };
      this.map.removeFocusLineLayer();
      for (let seawall of seawallList) {
        let lineString =
          (seawall.coorpot && seawall.coorpot.replaceAll("&quot;", "'")) || "[]";
        let linelist = lineString && eval(lineString);
        linelist = linelist.map((line) => {
          line = line.map((item, index) => {
            return [item.lng, item.lat];
          });
          return {
            lineList: line,
            data: {
              type: "海塘",
              name: seawall.seawallName,
              props: {
                海塘长度: seawall.seawallLength + "m",
                防潮标准: seawall.designTideStandard + "m",
                所在位置: seawall.village,
              },
            },
          };
        });
        this.map.drawLineFocus(linelist, style, "focuLineLayer");
      }
    },
    /**
     * 海塘点位开关
     */
    handleSeawallChange(btnType) {
      this[btnType] = !this[btnType];
      if (this[btnType]) {
        for (let seawall of this.seawallList) {
          let color = "#E80000";
          if (seawall.designTideStandard < 10) {
            color = "#E80000";
          } else if (seawall.designTideStandard == 10) {
            color = "#DD00DB";
          } else if (seawall.designTideStandard == 20) {
            color = "#E89100";
          } else if (seawall.designTideStandard == 50) {
            color = "#EEC80B";
          } else if (seawall.designTideStandard == 100) {
            color = "#19AF47";
          } else if (seawall.designTideStandard > 100) {
            color = "#4CEBC7";
          } else if (seawall.designTideStandard == null) {
            color = "#8D0A0A";
          }
          let style = {
            lineWidth: 4,
            lineColor: color,
          };
          let lineString =
            (seawall.coorpot && seawall.coorpot.replaceAll("&quot;", "'")) || "[]";
          let linelist = lineString && eval(lineString);
          linelist = linelist.map((line) => {
            line = line.map((item, index) => {
              return [item.lng, item.lat];
            });
            return {
              lineList: line,
              data: {
                type: "海塘",
                name: seawall.seawallName,
                code: seawall.seawallCode,
                props: {
                  海塘长度: seawall.seawallLength + "m",
                  防潮标准: seawall.designTideStandard + "m",
                  所在位置: seawall.village,
                },
                ...seawall,
              },
            };
          });
          this.$emit("legend-change", "海塘", {
            label: "海塘",
            children: [
              {
                label: "非标准",
                icon: "seawall-type-none",
              },
              {
                label: "10年以下",
                icon: "seawall-type-1",
              },
              {
                label: "10年一遇",
                icon: "seawall-type-10",
              },
              {
                label: "20年一遇",
                icon: "seawall-type-20",
              },
              {
                label: "50年一遇",
                icon: "seawall-type-50",
              },
              {
                label: "100年一遇",
                icon: "seawall-type-100",
              },
              {
                label: "100年以上",
                icon: "seawall-type-101",
              }
            ],
          });
          this.map.drawLineEx(linelist, style);
        }
      } else {
        this.map.removeLineLayer();
        this.map.removeFocusLineLayer();
        this.$emit("legend-change", "海塘", {});
      }
    },
    /**
     * 海塘图表
     */
    seawallEchart() {
      var chartDom = document.getElementById("seawallEchart");
      var myChart = echarts.init(chartDom);
      var option;
      const color0 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#00EBC7", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color1 = {
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
      };
      const color2 = {
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
      };
      const color3 = {
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
      };
      const color4 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#DD00DB", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color5 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#E80000", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color6 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#8D0A0A", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      option = {
        tooltip: {
          show: true,
          trigger: "item",
          formatter: function (data) {
            return (
              data.name +
              "<br/><span style='font-size: 20px;color: #1CFFF5;line-height: 26px;'>" +
              data.data.partLength +
              "</span> <span  style='font-size: 14px;'> km</span>"
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
                formatter: this.seawallData.totalLength + "km\n" + "总长度", // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
              },
            },
            labelLine: {
              show: false,
            },
            data: this.seawallDataList,
            color: [color0, color1, color2, color3, color4, color5, color6],
          },
        ],
      };
      option && myChart.setOption(option);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.seawall-card {
  height: 100%;
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
    background: url(~assets/images/WaterDisastersImage/bai-jing-style.png) no-repeat top
      center;
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
.seawall-card .panel-item .panel-item__content--tabs .tab-button,
.seawall-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.seawall-card .panel-item .panel-item__content--tabs .tab-button.active,
.seawall-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
