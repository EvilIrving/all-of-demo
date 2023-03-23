<!--
 * @Autor: ly
 * @Date: 2021-08-271 09:55:26
 * @LastEditors: dtb
 * @LastEditTime: 2022-05-20 17:02:33
 * @Description: 河流分布
 * @FilePath: \lucheng\src\views\water_disaster_custom\cards\DistributionRiverCard.vue
-->
<template>
  <div class="water-security-card">
    <panel-item title="河流分布" has-switch tc @tclick="tclick" :action-tabs="['行政区划', '河段等级', '健康分析']" @change="handleEngineeringChange">
      <template #title-switch>
        <!-- tab切换 -->
        <el-dropdown placement="bottom-start" trigger="click">
          <span class="el-dropdown-link" :class="yearDropdownVisible ? 'dropdown-active' : ''">
            <span style="font-size: 1rem">{{typeData}}</span>
            <i class="el-icon-caret-bottom" />
          </span>
          <el-dropdown-menu slot="dropdown" class="year-dropdown">
            <el-dropdown-item v-for="(item, index) in typeOption" :key="index">
              <el-radio v-model="typeData" :label="item" @change="typeChange">{{ item }}</el-radio>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <!-- 地图落点 -->
        <i title="地图落点" @click="handleMapNet('sldbtn')" :class="[sldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']" />
        <!-- 详情 -->
        <i title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog"/>
      </template>
      <!-- 图表内容 -->
      <div v-if="riverTabData == '行政区划'" class="area-box">
        <ve-histogram
          ref="chart1"
          height="100%"
          width="100%"
          :data="barData"
          :settings="barOption.setting"
          :extend="barOption.extend"
          :grid="barOption.grid"
          :tooltip-visible="false"
          :legend-visible="false"
          :after-config="barConfig"
        />
      </div>
      <!-- 饼状图 -->
      <div v-else class="area-box project-type">
        <ve-ring
          height="160px"
          width="160px"
          ref="chart2"
          v-bind="prjectOption"
          :data="barData"
          :tooltip-visible="false"
          :legend-visible="false"
          :after-config="afterProjectConfig"
        />
        <!-- 右侧统计详情 -->
        <div class="project-type-legend">
          <ul class="list">
            <li v-for="(item, index) in barData.rows" :key="index" class="list-item">
              <i class="dot" :style="{'backgroundColor': item.color}" />
              {{ item.adnm }}
              <span style="float:right;"> 
                <span style="color:#1CFFF5;">{{ item.num }}</span>{{ units }}
              </span>
            </li>
          </ul>
        </div>
      </div>
    </panel-item>
  </div>
</template>

<script>
  import PanelItem from "../components/PanelItem.vue";
  import { mapGetters, mapActions } from "vuex";
  import { generateDataZoom, genNormalLinearGradients } from "./utils/chart.js";
  import { waterResourceApi } from "@/api/waterResourcesGuarantee";

  export default {
    name: "distributionRiverCard",
    layerName: "focuLineLayer",
    components: { PanelItem },
    data() {
      return {
        units: '个',
        typeData: '数量',
        yearDropdownVisible: false,
        typeOption: ['数量', '长度'],
        riverTabData: '行政区划',
        prjectOption: {
          grid: {
            top: 40,
            bottom: "2%",
            containLabel: true,
          },
          settings: { radius: [40, 75] },
          extend: {
            "textStyle.color": "#fff",
            series: {
              center: [80, "50%"],
              hoverAnimation: false,
              label: { show: false },
            },
          },
        },
        timer: null,
        barData: {
          columns: ["adnm", "num"],
          rows: [],
        },
        barOption: {
          grid: {
            top: 40,
            bottom: "5%",
            containLabel: true,
          },
          setting: {
            yAxisName: ["个"],
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
        sldbtn: false, //水源地点位展示标记
        reservoiList: [],
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
    created() {
    },
    mounted() {
      this.$nextTick(() => {
        this.rivSegStaticByArea();
        this.waterGuaranteeRsvrWatSrcTable();
        this.getCenterData();
        this.timer = setInterval(() => {
          this.waterGuaranteeRsvrWatSrcTable();
          this.getCenterData();
        }, 300000);
      });
    },
    beforeDestroy() {
      clearInterval(this.timer);
    },
    methods: {
      ...mapActions(["SetDetailDialogParam", "SetDistributionRiverCount", "SetRiverway"]),
      /*
      * 类型切换
      */ 
      typeChange() {
        if (this.riverTabData == '行政区划') {
          this.rivSegStaticByArea();
        }else if(this.riverTabData == '河段等级') {
          this.rivSegStaticByAreaLevel();
        }
      },
      /**
       * 处理河流分布change事件
       */
      handleEngineeringChange(index, type) {
        this.riverTabData = type;
        if (type == '行政区划') {
          this.rivSegStaticByArea();
        }else if(type == '河段等级') {
          this.rivSegStaticByAreaLevel();
        }else {
          this.rivSegStaticByHealthState();
        }
      },
      // 环形图方法
      afterProjectConfig(options) {
        let data = options.series[0].data;
        let count = data.map((i) => i.value).reduce((total, cur) => total + cur);
        options.title = {
          show: true,
          text: `{count|${count}}个\n总数`,
          textStyle: { color: "#fff", rich: { count: { fontSize: 22 } } },
          textAlign: "center",
          left: 75,
          top: "40%",
        };
        let colorArr = ['#19AF47', '#008FE8', '#E89100', '#FF551F', '#EF1212'];
        options.color = colorArr.map((item) => {
          return genNormalLinearGradients(item, "#000");
        });
        return options;
      },
      // 河流分析按行政区划统计
      async rivSegStaticByArea() {
        this.barData.rows = [];
        let opt = {
          type: this.typeData == '数量' ? '1' : '2'
        }
        let res = await waterResourceApi.rivSegStaticByArea(opt);
        if (res.code == 0) {
          this.barData.rows = res.data;
          if (this.typeData == '数量') {
            this.barOption.setting.yAxisName = ["个"];
          }else {
            this.barOption.setting.yAxisName = ["km"];
          }
          this.$refs["chart1"].echarts.resize();
        }
      },
      // 河流分析按河段等级统计
      async rivSegStaticByAreaLevel() {
        this.barData.rows = [];
        let opt = {
          type: this.typeData == '数量' ? '1' : '2'
        }
        let res = await waterResourceApi.rivSegStaticByAreaLevel(opt);
        if (res.code == 0) {
          let color = ['#19AF47', '#008FE8', '#E89100', '#FF551F', '#EF1212'];
          this.barData.rows = res.data;
          res.data.forEach((val, i)=>{
            this.barData.rows[i].color = color[i];
            this.barData.rows[i].adnm = val.areaLevel;
          });
          if (this.typeData == '数量') {
            this.units = "个";
          }else {
            this.units = "km";
          }
          this.$refs["chart2"].echarts.resize();
        }
      },
      // 河流分析按健康分析统计
      async rivSegStaticByHealthState() {
        this.barData.rows = [];
        let res = await waterResourceApi.rivSegStaticByHealthState();
        if (res.code == 0) {
          this.barData.rows = res.data.slice(0,-1);
          this.units = "个";
          let color = ['#19AF47', '#EEC80B', '#EF1212'];
          res.data.forEach((val, i)=>{
            this.barData.rows[i].color = color[i];
            this.barData.rows[i].adnm = val.healthState;
          })
          this.$refs["chart2"].echarts.resize();
        }
      },
      // 获取河道地图落点数据
      async waterGuaranteeRsvrWatSrcTable() {
        await waterResourceApi.pageRivSegBaseInfo({withPathStr: true}).then((res) => {
            this.reservoiList = res.data.list;
        });
      },
      // 获取C位数据
      async getCenterData() {
        let opt = {};
        await waterResourceApi.rivSegCStatic(opt).then((res) => {
          this.SetDistributionRiverCount(res.data);
        });
      },
      // 标题点击打开统计卡片
      tclick(title) {
        this.$emit("tclick", title);
      },
      /**
       * 打开详情弹框点击事件
       */
      openDetailsDialog() {
        this.SetRiverway(true);
      },
      // 地图落点
      handleMapNet(btnType) {
        this[btnType] = !this[btnType];
        let isWaterList = [];
        this.reservoiList.forEach((item) => {
          let obj = {};
          if (item.riverPath) {
            let str = JSON.parse(item.riverPath);
            if (item.healthState == '1') {
              obj = {
                ...item,
                name:item.riverName,
                coordinates: str,
                fillColor: "#19AF47",
                lineColor: "#19AF47",
                lineWidth: 5,
                type: '河道',
                props: {
                  '河道等级': item.levelName,
                  '河道长度': item.length,
                  '河长姓名': item.levelName,
                  '联系方式': item.levelName,
                }
              }
            }else if (item.healthState == '2'){
              obj = {
                coordinates: str,
                fillColor: "#EEC80B",
                lineColor: "#EEC80B",
                lineWidth: 5,
                type: '河道',
                props: {
                  '河道等级': item.levelName,
                  '河道长度': item.length,
                  '河长姓名': item.levelName,
                  '联系方式': item.levelName,
                }
              }
            }else if (item.healthState == '3'){
              obj = {
                '河道等级':item.levelName,
                coordinates: str,
                fillColor: "#EF1212",
                lineColor: "#EF1212",
                lineWidth: 5,
                type: '河道',
                props: {
                  '河道等级': item.levelName,
                  '河道长度': item.length,
                  '河长姓名': item.levelName,
                  '联系方式': item.levelName,
                }
              }
            }
            isWaterList.push(obj);
          }
        })
        if (this[btnType]) {
          this.ScreenMap._riversLrawLines(isWaterList, {}, "areaPointLayer5");
        } else {
          this.ScreenMap._riversLrawLines([], {}, "areaPointLayer5");
        }
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
              fontSize: this.$commonSetting.chartFontSize,
              position: "top",
              color: "#1EF8F2",
            },
            markPoint: { // 设置最大值和最小值
              symbol: "circle",
              symbolSize: 1,
              data: [
                {
                  type: "max",
                  // name: "最大",
                },
                {
                  type: "min",
                  // name: "最小",
                },
              ],
              label: {
                show: true,
                formatter: '{b}\n{c}',
                color: "#1EF8F2",
                fontSize: this.$commonSetting.chartFontSize,
                position: "top"
              }
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
        options.dataZoom = generateDataZoom(4, this.barData.rows.length);
        return options;
      }
    }
  }
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.water-security-card {
  height: 100%;
  .project-type {
    margin-top: 10px;
    display: flex;
    flex-direction: row;
  }
  .project-type-legend {
    margin-left: 20px;
    width: 100%;
    height: 160px;
    overflow-y: auto;
    &::-webkit-scrollbar-thumb:vertical {
      background-color: #1ef8f2;
    }

    .list {
      .list-item {
        padding: 5px 6px;
        font-size: 16px;
        color: #fff;
        font-weight: 500;
        position: relative;
        padding-left: 20px;
        .dot {
          content: "";
          position: absolute;
          left: 0;
          top: 50%;
          width: 12px;
          height: 12px;
          border-radius: 50%;
          transform: translateY(-50%);
        }
      }
    }
  }





  .icon-sty {
    margin-right: 10px;
  }

  .area-box {
    width: 100%;
    height: 100%;
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
.water-security-card .panel-item .panel-item__content--tabs .tab-button,
.water-security-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.water-security-card .panel-item .panel-item__content--tabs .tab-button.active,
.water-security-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
