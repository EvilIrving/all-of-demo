<!--
 * @Author: your name
 * @Date: 2021-06-30 18:16:57
 * @LastEditTime: 2021-07-06 22:05:21
 * @LastEditors: Please set LastEditors
 * @Description: 潮位小组件
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\TideCard.vue
-->
<template>
  <div class="tide-card">
    <panel-item
      :inSimulation="previewModule"
      title="潮位"
      has-switch
      :action-tabs="['温州', '灵昆', '瑞安', '鳌江']"
      @change="handleEngineeringChange"
      tc
      @tclick="tclick"
    >
      <template slot="title-switch">
        <i
          title="地图落点"
          @click.stop="handleTideChange('cwbtn')"
          :class="[
            cwbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('潮位')"
        />
        <i
          v-show="!previewModule"
          @click="goToAmap('潮位')"
          title="一张图"
          class="icon-yizhangtu icon-sty"
        />
      </template>
      <!-- tab切换 -->
      <div class="sidebar-list">
        <el-carousel trigger="click" arrow="never" :autoplay="true" :interval="interval">
          <el-carousel-item>
            <echartLine ref="lineChart" :extraOption="extraOption"></echartLine>
          </el-carousel-item>
          <el-carousel-item>
            <el-row>
              <el-col
                :span="8"
                v-for="(item, index) in astroTideData"
                class="sea-level-sty"
                :class="index == 1 ? 'sea-left-level-style' : ''"
                :key="index"
              >
                <div class="level-sty">
                  <p>{{ item.date && item.date.slice(5, 10) }}</p>
                  <p>{{ item.lunar }}</p>
                </div>
              </el-col>
            </el-row>
            <el-row class="sea-level-style">
              <el-col :span="8" class="sea-level-sty">
                <div 
                  class="sea-sty"
                  v-if="astroTideData[0] &&
                      astroTideData[0].amMax.tm"
                >
                  <p>
                    {{
                      astroTideData[0] &&
                      astroTideData[0].amMax.tm &&
                      astroTideData[0].amMax.tm.slice(11, 16)
                    }}
                    <i class="icon-shang-sheng icon-sty" />{{
                      astroTideData[0] && astroTideData[0].amMax.tdz
                    }}
                    m
                  </p>
                </div>
                <div 
                  class="sea-sty"
                  v-if="astroTideData[0] &&
                      astroTideData[0].amMin.tm"
                >
                  <p>
                    {{
                      astroTideData[0] &&
                      astroTideData[0].amMin.tm &&
                      astroTideData[0].amMin.tm.slice(11, 16)
                    }}
                    <i class="icon-chaowei-xiajiang icon-sty" />{{
                      astroTideData[0] && astroTideData[0].amMin.tdz
                    }}
                    m
                  </p>
                </div>
              </el-col>
              <el-col :span="8" class="sea-level-sty sea-left-level-style">
                <div 
                  class="sea-sty"
                  v-if="astroTideData[1] &&
                      astroTideData[1].amMax.tm"
                >
                  <p>
                    {{
                      astroTideData[1] &&
                      astroTideData[1].amMax.tm &&
                      astroTideData[1].amMax.tm.slice(11, 16)
                    }}
                    <i class="icon-shang-sheng icon-sty" />{{
                      astroTideData[1] && astroTideData[1].amMax.tdz
                    }}
                    m
                  </p>
                </div>
                <div 
                  class="sea-sty"
                  v-if="astroTideData[1] &&
                      astroTideData[1].amMin.tm"
                >
                  <p>
                    {{
                      astroTideData[1] &&
                      astroTideData[1].amMin.tm &&
                      astroTideData[1].amMin.tm.slice(11, 16)
                    }}
                    <i class="icon-chaowei-xiajiang icon-sty" />{{
                      astroTideData[1] && astroTideData[1].amMin.tdz
                    }}
                    m
                  </p>
                </div>
              </el-col>
              <el-col :span="8" class="sea-level-sty">
                <div 
                  class="sea-sty"
                  v-if="astroTideData[2] &&
                      astroTideData[2].amMax.tm"
                >
                  <p>
                    {{
                      astroTideData[2] &&
                      astroTideData[2].amMax.tm &&
                      astroTideData[2].amMax.tm.slice(11, 16)
                    }}
                    <i class="icon-shang-sheng icon-sty" />{{
                      astroTideData[2] && astroTideData[2].amMax.tdz
                    }}
                    m
                  </p>
                </div>
                <div 
                  class="sea-sty"
                  v-if="astroTideData[2] &&
                      astroTideData[2].amMin.tm"
                >
                  <p>
                    {{
                      astroTideData[2] &&
                      astroTideData[2].amMin.tm &&
                      astroTideData[2].amMin.tm.slice(11, 16)
                    }}
                    <i class="icon-chaowei-xiajiang icon-sty" />{{
                      astroTideData[2] && astroTideData[2].amMin.tdz
                    }}
                    m
                  </p>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8" class="sea-level-sty">
                <div 
                  class="sea-sty"
                  v-if="astroTideData[0] &&
                      astroTideData[0].pmMax.tm"
                >
                  <p>
                    {{
                      astroTideData[0] &&
                      astroTideData[0].pmMax.tm &&
                      astroTideData[0].pmMax.tm.slice(11, 16)
                    }}
                    <i class="icon-shang-sheng icon-sty" />{{
                      astroTideData[0] && astroTideData[0].pmMax.tdz
                    }}
                    m
                  </p>
                </div>
                <div 
                  class="sea-sty"
                  v-if="astroTideData[0] &&
                      astroTideData[0].pmMin.tm"
                >
                  <p>
                    {{
                      astroTideData[0] &&
                      astroTideData[0].pmMin.tm &&
                      astroTideData[0].pmMin.tm.slice(11, 16)
                    }}
                    <i class="icon-chaowei-xiajiang icon-sty" />{{
                      astroTideData[0] && astroTideData[0].pmMin.tdz
                    }}
                    m
                  </p>
                </div>
              </el-col>
              <el-col :span="8" class="sea-level-sty sea-left-level-style">
                <div 
                  class="sea-sty"
                  v-if="astroTideData[1] &&
                      astroTideData[1].pmMax.tm"
                >
                  <p>
                    {{
                      astroTideData[1] &&
                      astroTideData[1].pmMax.tm &&
                      astroTideData[1].pmMax.tm.slice(11, 16)
                    }}
                    <i class="icon-shang-sheng icon-sty" />{{
                      astroTideData[1] && astroTideData[1].pmMax.tdz
                    }}
                    m
                  </p>
                </div>
                <div 
                  class="sea-sty" 
                  v-if="astroTideData[1] &&
                      astroTideData[1].pmMin.tm"
                >
                  <p>
                    {{
                      astroTideData[1] &&
                      astroTideData[1].pmMin.tm &&
                      astroTideData[1].pmMin.tm.slice(11, 16)
                    }}
                    <i class="icon-chaowei-xiajiang icon-sty" />{{
                      astroTideData[1] && astroTideData[1].pmMin.tdz
                    }}
                    m
                  </p>
                </div>
              </el-col>
              <el-col :span="8" class="sea-level-sty">
                <div 
                  class="sea-sty" 
                  v-if="astroTideData[2] && astroTideData[2].pmMax.tm"
                >
                  <p>
                    {{
                      astroTideData[2] &&
                      astroTideData[2].pmMax.tm &&
                      astroTideData[2].pmMax.tm.slice(11, 16)
                    }}
                    <i class="icon-shang-sheng icon-sty" />{{
                      astroTideData[2] && astroTideData[2].pmMax.tdz
                    }}
                    m
                  </p>
                </div>
                <div class="sea-sty" v-if="astroTideData[2]">
                  <p>
                    {{
                      astroTideData[2] &&
                      astroTideData[2].pmMin.tm &&
                      astroTideData[2].pmMin.tm.slice(11, 16)
                    }}
                    <i class="icon-chaowei-xiajiang icon-sty" />
                    <span>
                      {{ astroTideData[2] && astroTideData[2].pmMin.tdz }}
                    </span>
                    m
                  </p>
                </div>
              </el-col>
            </el-row>
          </el-carousel-item>
        </el-carousel>
      </div>
    </panel-item>
  </div>
</template>

<script>
import PanelItem from "../components/PanelItem.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { mapGetters, mapActions } from "vuex";
import EchartLine from "../../WaterDisasterPreventionScreen/charts/echart_line/EchartLine.vue"; // 折线图
import { goAmap } from "./utils/common";
export default {
  name: "TideCard",
  layerName: "tideLayer",
  legendName: "潮位",
  components: {
    PanelItem,
    EchartLine,
  },
  mounted() {
    this.$nextTick(() => {
      this.tideCurve("温州"); //潮位数据接口
      this.getRightMapPoints("潮位");
      this.astroTideTable("温州"); //未来三天潮位数据接口
      this.timer = setInterval(()=>{
        this.tideCurve("温州");
        this.getRightMapPoints("潮位");
        this.astroTideTable("温州"); 
      },300000);
    });
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  data() {
    return {
      interval: 5000,
      //潮位图表数据
      seriesLineData: [],
      //潮位图表X轴数据
      extraOption: {},
      //未来三天潮位预测数据
      astroTideData: [],
      //潮位列表
      tideStList: [],
      cwbtn: false, //潮位点位展示标记
      timer:null
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
        case "潮位":
          {
            this.tideStList = res.data;
          }
          break;
      }
    },
    /**
     * 处理潮位change事件
     */
    handleEngineeringChange(index, type) {
      this.tideCurve(type);
      this.astroTideTable(type);
    },

    /**
     * 潮位点位开关
     */
    handleTideChange(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.7,
        anchor: [27,30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/legend/juhe-tide-legend-icon.png"),
        src: require("@/assets/images/legend/juhe-tide-legend.png"),
      };
      let pointList = [];
      for (let point of this.tideStList) {
        let imgSrc = require("@/assets/images/legend/juhe-tide-legend.png");
        if (point.warning != false) {
          imgSrc = require("@/assets/images/legend/juhe-tide-legends.png");
        }
        pointList.push({
          stCode: point.stCode,
          stType: point.stType,
          lng: point.stLong,
          lat: point.stLat,
          type: "潮位",
          src: imgSrc,
          name: point.stName,
          props: { 潮位: `${point.tdz || "-"}m` },
        });
      } 
      if (this[btnType]) {
        this.$emit("legend-change", "潮位", {
          label: "潮位",
          children: [
            { 
              label: "正常", 
              icon: "icon-tide-legend-icon" 
            },
            {
              label: "超警",
              icon: "icon-tide-legend-icons",
            }
          ],
        });
        this.map.drawClusterPoint(pointList, style, "tideLayer");
      } else {
        this.map.drawClusterPoint([], {}, "tideLayer");
        this.$emit("legend-change", "潮位", {});
      }
    },
    /**
     * 潮位接口
     */
    async tideCurve(val) {
      await disasterPreventionApi.tideCurve({ tideStaName: val }).then((res) => {
        if (res.code == 0) {
          let arr = [];
          let obj = [];
          let warnTide = [];
          let xAxisArr = [];
          let series = [];
          this.extraOption = {
            yAxis: {
              axisLabel: {
                fontSize: this.$commonSetting.chartFontSize,
              },
            },
            legend: {
              textStyle: {
                fontSize: this.$commonSetting.chartFontSize,
              },
            },
          };
          if (res.data.astroTide.length > 0) {
            res.data.astroTide.forEach((val) => {
              if (val.tdz && val.tm) {
                arr.push(val.tdz);
                xAxisArr.push(val.tm.slice(5, 16));
              }
              warnTide.push(res.data.warnTide);
            });
            res.data.realTide.forEach((val) => {
              obj.push(val.tdz);
            });
            let xAxis = [
              {
                axisLabel: {
                  show: true,
                  textStyle: {
                    color: "#fff",
                    fontSize: this.$commonSetting.chartFontSize,
                  },
                  formatter: function (value) {
                    var ret = ""; //拼接加\n返回的类目项
                    var maxLength = 6; //每项显示文字个数
                    var valLength = value.length; //X轴类目项的文字个数
                    var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
                    if (rowN > 1) {
                      //如果类目项的文字大于3,
                      for (var i = 0; i < rowN; i++) {
                        var temp = ""; //每次截取的字符串
                        var start = i * maxLength; //开始截取的位置
                        var end = start + maxLength; //结束截取的位置
                        //这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
                        temp = value.substring(start, end) + "\n";
                        ret += temp; //凭借最终的字符串
                      }
                      return ret;
                    } else {
                      return value;
                    }
                  },
                },
                data: xAxisArr,
              },
            ];
            this.extraOption.xAxis = xAxis;
          }
          series.push(
            {
              data: arr,
              type: "line",
              smooth: true,
              name: "预报值",
              smooth: false, //关键点，为true是不支持虚线的，实线就用true
              itemStyle: {
                normal: {
                  lineStyle: {
                    width: 2,
                    type: "dashed",
                  },
                },
              },
            },
            {
              data: obj,
              type: "line",
              smooth: true,
              name: "实时值",
            },
            {
              data: warnTide,
              type: "line",
              smooth: true,
              name: "警戒值",
              lineStyle: {
                color: "#FF0000",
                type: "dashed",
              },
              itemStyle: {
                opacity: 0,
              },
            }
          );
          this.extraOption.series = series;
        }
      });
    },
    /**
     * 未来三天文潮表格
     */
    async astroTideTable(val) {
      await disasterPreventionApi.astroTideTable({ tideStaName: val }).then((res) => {
        if (res.code == 0) {
          this.astroTideData = res.data;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.tide-card {
  height: 100%;
  // padding: 0 6px;
  ::v-deep .panel-item {
    .panel-item__content {
      .el-carousel {
        overflow-y: hidden;
      }
    }
  }
  ::v-deep.el-carousel {
    height: 100%;
    .el-carousel__container {
      height: 100%;
    }
  }
  .sidebar-list {
    height: calc(100% - 42px);
    overflow: hidden;
  }
  .level-style {
    border-left: 1px solid #00c1ff;
    border-right: 1px solid #00c1ff;
  }
  .sea-level-sty {
    font-size: 16px;
    text-align: center;
    padding: 7px;
    > div:nth-child(2) {
      > p:nth-child(2) {
        color: #5dff30;
      }
    }
    .level-sty {
      font-family: PingFangSC-Regular;
      letter-spacing: 0;
      text-align: center;
      line-height: 24px;
      > p:nth-child(1) {
        font-size: 24px;
        color: #f4f8fd;
        font-weight: 500;
      }
      > p:nth-child(2) {
        font-size: 16px;
        color: #00fff4;
        font-weight: 400;
      }
    }
    .sea-sty {
      padding: 5px 0;
      font-size: 14px;
      display: flex;
      justify-content: space-between;
      .icon-sty{
        margin-left: 4px;
        margin-right: 0;
      }
      >p{
        display: flex;
        justify-content: space-between;
        align-content: center;
      }
      > p:nth-child(2) {
        font-size: 14px;
        > i {
          vertical-align: baseline;
        }
        color: #00fff4;
      }
    }
  }
  .river-sty {
    > p {
      text-align: right;
      color: #00c1ff;
      font-size: 16px;
    }
  }
  .river-style {
    margin: 13px 0 0 0;
    font-size: 18px;
  }
  .seawall-sty {
    margin: 10px 0;
    color: #fff;
    font-size: 18px;
    > div {
      padding: 0 10px;
      display: flex;
      line-height: 30px;
      justify-content: center;
      justify-content: space-between;
    }
    > div:nth-child(1) {
      > p:nth-child(2) {
        > span {
          color: #00c1ff;
          font-size: 24px;
        }
      }
    }
    > div:nth-child(2) {
      > p:nth-child(2) {
        > span {
          color: #eec80b;
          font-size: 24px;
        }
      }
    }
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
.tide-card .panel-item .panel-item__content--tabs .tab-button,
.tide-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.tide-card .panel-item .panel-item__content--tabs .tab-button.active,
.tide-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
