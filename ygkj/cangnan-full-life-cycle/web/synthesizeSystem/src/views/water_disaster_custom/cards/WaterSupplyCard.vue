<!--
 * @Author: your name
 * @Date: 2021-06-30 18:17:27
 * @LastEditTime: 2021-07-06 22:04:35
 * @LastEditors: Please set LastEditors
 * @Description: 供水量（公报）
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\WaterSupplyCard.vue
-->
<template>
  <div class="water-card">
    <panel-item
      title="供水量"
      has-switch
      tc
      @tclick="tclick"
      :action-tabs="['供水量', '供水工程']"
      @change="handleTabChange"
    >
      <template slot="label-description">（公报）</template>
      <template slot="title-switch">
        <el-dropdown placement="bottom-start" trigger="click">
          <span
            class="el-dropdown-link"
            :class="yearDropdownVisible ? 'dropdown-active' : ''"
          >
            <span style="font-size: 1rem">{{ checkYear }}</span>
            <i class="el-icon-caret-bottom"></i>
          </span>
          <el-dropdown-menu slot="dropdown" class="year-dropdown">
            <el-dropdown-item v-for="(item, index) in yearOption" :key="index">
              <el-radio
                v-model="checkYear"
                :label="item"
                @change="handleYear"
                >{{ item }}</el-radio
              >
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <!-- <i
          title="地图落点"
          @click.stop="drawPointHandle('htbtn')"
          :class="[
            htbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        /> -->
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('供水量（公报）')"
        />
      </template>

      <div class="area-box" v-if="activeTab == 0">
        <div class="pie-chart-panel">
          <div id="waterSupplyEchart" style="width: 100%; height: 100%"></div>
          <div class="legend-panel">
            <div
              class="legend-item"
              v-for="(item, index) in waterSupplyData.rows"
              :key="index"
            >
              <i
                style="vertical-align: middle"
                :class="[
                  'icon-sty',
                  'icon-circle',
                  item.name == '蓄水'
                    ? 'icon-year-100'
                    : item.name == '引水'
                    ? 'icon-year-5'
                    : item.name == '提水'
                    ? 'icon-year-10'
                    : '',
                ]"
              />
              <div class="text">
                <p>{{ item.name }}</p>
                <p>
                  <span style="color: #1cfff5; font-size: 1rem">{{
                    item.value
                  }}</span>
                  亿m³
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="area-box" v-if="activeTab == 1">
        <div class="pie-chart-panel">
          <div
            id="waterSupplyProjEchart"
            style="width: 100%; height: 100%"
          ></div>
          <div class="legend-panel">
            <div
              class="legend-item"
              v-for="(item, index) in waterSupplyProjData.rows"
              :key="index"
            >
              <i
                style="vertical-align: middle"
                :class="[
                  'icon-sty',
                  item.name == '蓄水'
                    ? 'icon-year-100'
                    : item.name == '引水'
                    ? 'icon-year-5'
                    : item.name == '提水'
                    ? 'icon-year-10'
                    : '',
                ]"
              />
              <div class="text">
                <p>{{ item.name }}</p>
                <p>
                  <span style="color: #1cfff5; font-size: 1rem">{{
                    item.value
                  }}</span>
                  个
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </panel-item>
  </div>
</template>

<script>
import * as echarts from "echarts"; // echart插件
import PanelItem from "../components/PanelItem.vue";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

import { mapGetters, mapActions } from "vuex";
export default {
  name: "WaterSupplyCard",
  components: {
    PanelItem,
  },
  data() {
    return {
      timer: null,
      interval: 5000,
      waterSupplyData: {
        totalLength: 0,
        rows: [],
      },
      waterSupplyProjData: {
        totalLength: 0,
        rows: [],
      },

      seawallList: [],
      htbtn: false,

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

      yearOption: [],
      yearDropdownVisible: false,
      checkYear: "",
    };
  },
  created() {
    this.getYearOption();
  },
  mounted() {
    this.stats4WrbWaterSupply();
    this.getCenterData();
    this.$nextTick(() => {
      this.timer = setInterval(() => {
        this.stats4WrbWaterSupply();
        this.getCenterData();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
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
    ...mapActions(["SetDetailDialogParam", "SetWaterSupplyCount"]),
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
    handleTabChange(val) {
      this.activeTab = val;
      this.stats4WrbWaterSupply();
    },
    /**
     * 供水量数据接口请求
     */
    async stats4WrbWaterSupply() {
      let opt = {
        type: this.activeTab == 0 ? "1" : "2",
        year: this.checkYear ? Number(this.checkYear.substring(0, 4)) : "",
      };
      let data = [];
      let count = 0;
      await waterResourceApi.stats4WrbWaterSupply(opt).then((res) => {
        this.checkYear = res.data[0].year + " 年";
        res.data.forEach((item) => {
          let obj = {
            name: item.classifyName,
            value: item.classifyValue,
          };
          count = count + item.classifyValue;
          data.push(obj);
        });
        if (this.activeTab == 0) {
          this.waterSupplyData.rows = data;
          this.waterSupplyData.totalLength = count;
          this.$nextTick(() => {
            this.waterSupplyEchart();
          });
        } else {
          this.waterSupplyProjData.rows = data;
          this.waterSupplyProjData.totalLength = count;
          this.$nextTick(() => {
            this.waterSupplyProjEchart();
          });
        }
      });
    },
    // 获取C位数据
    async getCenterData() {
      let opt = {
        year: this.checkYear ? Number(this.checkYear.substring(0, 4)) : "",
      };
      await waterResourceApi.statsCwindow4WrbWaterSupply(opt).then((res) => {
        this.SetWaterSupplyCount(res.data);
      });
    },
    /**
     * 点击点位
     */
    drawPointHandle(btnType) {
      this[btnType] = !this[btnType];
      if (this[btnType]) {
        this.$emit("legend-change", "供水工程", {
          label: "供水工程",
          children: [
            {
              label: "蓄水",
              icon: "legend-icon-xushui",
            },
            {
              label: "提水",
              icon: "legend-icon-tishui",
            },
            {
              label: "引水",
              icon: "legend-icon-yinshui",
            },
          ],
        });
      } else {
        this.map.removeLineLayer();
        this.map.removeFocusLineLayer();
        this.$emit("legend-change", "供水工程", {});
      }
    },
    /**
     * 图表 供水量
     */
    waterSupplyEchart() {
      var chartDom = document.getElementById("waterSupplyEchart");
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
            color: "#19AF47", // 0% 处的颜色
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
            color: "#A000C8", // 0% 处的颜色
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
            color: "#009AFF", // 0% 处的颜色
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
              data.value +
              "</span> <span  style='font-size: 14px;'> 亿m³</span>"
            );
          },
          confine: true,
          extraCssText: "width:160px;height:50px;font-size:18px;",
        },
        series: [
          {
            type: "pie",
            radius: ["50%", "80%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: "center",
                color: "#fff",
                fontSize: 14,
                formatter: this.waterSupplyData.totalLength + "亿m³\n" + "总量", // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
              },
            },
            labelLine: {
              show: false,
            },
            data: this.waterSupplyData.rows,
            color: [color0, color1, color2],
          },
        ],
      };
      option && myChart.setOption(option);
    },

    /**
     * 图表 供水工程
     */
    waterSupplyProjEchart() {
      var chartDom = document.getElementById("waterSupplyProjEchart");
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
            color: "#19AF47", // 0% 处的颜色
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
            color: "#A000C8", // 0% 处的颜色
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
            color: "#009AFF", // 0% 处的颜色
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
              data.value +
              "</span> <span  style='font-size: 14px;'> 亿m³</span>"
            );
          },
          confine: true,
          extraCssText: "width:160px;height:50px;font-size:18px;",
        },
        series: [
          {
            type: "pie",
            radius: ["50%", "80%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: "center",
                color: "#fff",
                fontSize: 14,
                formatter:
                  this.waterSupplyProjData.totalLength + "个\n" + "总量", // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
              },
            },
            labelLine: {
              show: false,
            },
            data: this.waterSupplyProjData.rows,
            color: [color0, color1, color2],
          },
        ],
      };
      console.log(9000, option);
      option && myChart.setOption(option);
    },

    // 获取年份选择下拉内容
    getYearOption() {
      let date = new Date();
      let y = date.getFullYear();
      for (let i = y; i >= 1990; i--) {
        this.yearOption.push(i + " 年");
      }
      // this.checkYear = y + " 年";
    },
    // 下拉年份选择后
    handleYear() {
      this.stats4WrbWaterSupply();
      this.getCenterData();
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.water-card {
  height: 100%;
  .icon-sty {
    margin-right: 10px;
  }

  .icon-circle {
    width: 12px !important;
    height: 12px !important;
  }

  .icon-year-100 {
    @include chart-icon();
    background: #19af47;
  }
  .icon-year-10 {
    @include chart-icon();
    background: #dd00db;
  }
  .icon-year-5 {
    @include chart-icon();
    background: #008fe8;
  }

  .area-box {
    width: 100%;
    height: calc(100% - 34px);
  }
  .pie-chart-panel {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    .legend-panel {
      width: 100%;
      display: flex;
      justify-content: space-around;
      .legend-item {
        display: flex;
        .text > p {
          font-size: 12px;
          margin-bottom: 6px;
          &:last-child {
            margin-bottom: 0;
          }
        }
      }
    }
  }
  .el-dropdown {
    cursor: pointer;
    top: -2px;
    right: 10px;
    .el-dropdown-link {
      display: flex;
      align-items: center;
      &.dropdown-active {
        span,
        i {
          color: #1cfff5;
        }
      }
    }
  }
  .el-icon-caret-bottom {
    margin-left: 3px;
  }
}
</style>

<style lang="scss">
.water-card .panel-item .panel-item__content--tabs .tab-button,
.water-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.water-card .panel-item .panel-item__content--tabs .tab-button.active,
.water-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
