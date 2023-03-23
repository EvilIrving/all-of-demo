<!--
 * @Author: your name
 * @Date: 2021-06-30 18:17:27
 * @LastEditTime: 2022-03-15 16:57:49
 * @LastEditors: Please set LastEditors
 * @Description: 用水量 
 * @FilePath: \lucheng\src\views\water_disaster_custom\cards\WaterUseCard.vue
-->
<template>
  <div class="water-card">
    <panel-item
      title="洪水风险图"
      has-switch
      tc
      @tclick="tclick"
      :action-tabs="['淹没面积', '影响人口', '淹没区洪水损失']"
      @change="handleTabChange"
      class="water-card-panel"
    >
      <!-- <template #label-description></template> -->
      <template #title-switch>
        <div class="water-title-switch">
          <div>
            <el-select class="select-box" size="small" v-model="titleSwitchValue" placeholder="请选择">
              <el-option label="5年一遇" value="1"> </el-option>
              <el-option label="10年一遇" value="2"> </el-option>
              <el-option label="20年一遇" value="3"> </el-option>
              <el-option label="50年一遇" value="4"> </el-option>
            </el-select>
          </div>
          <div>
            <el-popover placement="bottom" width="120" trigger="manual" v-model="actionPointBtnActive">
              <div class="manual-div">
                <div style="cursor: pointer" @click="activeCheckMap(0)" :class="{ avtive: activesIndex == 0 }">
                  瓯江干流鹿城片
                </div>
                <div style="cursor: pointer" @click="activeCheckMap(1)" :class="{ avtive: activesIndex == 1 }">
                  戍浦江片
                </div>
                <div style="cursor: pointer" @click="activeCheckMap(2)" :class="{ avtive: activesIndex == 2 }">
                  温瑞平原片
                </div>
              </div>
              <i
                @click="actionPointBtnActive = !actionPointBtnActive"
                slot="reference"
                title="地图落点"
                :class="[ldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
              />
            </el-popover>
            <i title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog('用水量')" />
          </div>
        </div>
      </template>

      <div class="area-box" v-if="activeTab == 0">
        <div class="area-box-title">
          淹没总面积<span>{{ waterUseJobData.totalLength }}</span
          >km²
        </div>
        <div class="pie-chart-panel">
          <div id="waterUseEchart" ref="areaPieA"></div>
          <div class="legend-panel">
            <div class="legend-item" v-for="(item, index) in waterUseJobData.rows" :key="index">
              <i :class="['icon-sty', 'icon-circle', 'icon-year-' + index]" />
              <div class="text">
                <p>{{ item.name }}</p>
                <p>
                  <span style="color: #1cfff5; font-size: 1rem">{{ item.value }}</span>
                  km²
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="area-box" v-if="activeTab == 1">
        <div class="area-box-title">
          影响总人口<span>{{ waterUseJobData.totalLength }}</span
          >万人
        </div>
        <div class="pie-chart-panel">
          <div id="waterUseEchart" ref="areaPieB"></div>
          <div class="legend-panel">
            <div class="legend-item" v-for="(item, index) in waterUseJobData.rows" :key="index">
              <i :class="['icon-sty', 'icon-circle', 'icon-year-' + (index + 3)]" />
              <div class="text">
                <p>{{ item.name }}</p>
                <p>
                  <span style="color: #1cfff5; font-size: 1rem">{{ item.value }}</span>
                  万人
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="area-box" v-if="activeTab == 2">
        <div class="area-box-title">
          淹没区洪水总损失<span>{{ waterUseJobData.totalLength }}</span
          >万元
        </div>
        <div class="pie-chart-panel">
          <div id="waterUseEchart" ref="areaPieC"></div>
          <div class="legend-panel">
            <div class="legend-item" v-for="(item, index) in waterUseJobData.rows" :key="index">
              <i :class="['icon-sty', 'icon-circle', 'icon-year-' + (index + 6)]" />
              <div class="text">
                <p>{{ item.name }}</p>
                <p>
                  <span style="color: #1cfff5; font-size: 1rem">{{ item.value }}</span>
                  万元
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
import * as echarts from 'echarts'; // echart插件
import PanelItem from '../components/PanelItem.vue';
import { waterResourceApi } from '@/api/waterResourcesGuarantee';
import { mapGetters, mapActions } from 'vuex';
import { generateDataZoom } from './utils/chart.js';
import mapConfig from '@/config/mapConfig';
import { queryMapserver } from '@/api/loadMapServe';
export default {
  name: 'WaterUseCard',
  layerName: 'focuLineLayer',
  components: {
    PanelItem
  },
  data() {
    return {
      titleSwitchValue: '1',
      actionPointBtnActive: false,
      ldbtn: false, // 落点
      timer: null,
      interval: 5000,
      activeTab: 0,
      activesIndex: '-1',
      waterUseJobData: {
        totalLength: '477.2',
        rows: [
          {
            name: '瓯江干流鹿城片',
            value: 0.69
          },
          {
            name: '戌浦江片',
            value: 0.85
          },
          {
            name: '温瑞平原片',
            value: 0.85
          }
        ]
      },
      waterUseAreaData: {
        columns: ['adnm', 'totalConsume'],
        rows: []
      },
      waterUseAreaOption: {
        grid: {
          top: 40,
          bottom: '5%',
          containLabel: true
        },
        setting: {
          yAxisName: ['亿m³'],
          itemStyle: { color: '#fff' },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            fontWeight: 'bold',
            position: 'top',
            color: '#fff'
          }
        },
        extend: {
          'textStyle.color': '#fff',
          barWidth: 15,
          xAxis: {
            axisLabel: {
              interval: 0,
              fontSize: this.$commonSetting.chartFontSize
            }
          },
          yAxis: {
            nameTextStyle: {
              color: '#ffffff99',
              fontSize: this.$commonSetting.chartFontSize
            },
            axisLine: {
              show: false
            },
            axisLabel: {
              color: '#fff',
              fontSize: this.$commonSetting.chartFontSize
            },
            splitLine: {
              lineStyle: {
                color: '#fff',
                opacity: 0.5
              }
            },
            max: (value) => {
              return value.max > 1 ? null : 1;
            }
          }
        }
      },

      yearOption: [],
      yearDropdownVisible: false,
      checkYear: '',
      realdzmbtn: false,
      areaGeometryArr: []
    };
  },
  computed: {
    ...mapGetters(['ScreenMap', 'currentSelectArea', 'previewModule', 'formatterCurrentTime']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  created() {
    // this.getYearOption();
    // this.getAreaGeometry();
  },
  mounted() {
    // this.stats4WrbWaterConsume();
    // this.getCenterData();
    this.$nextTick(() => {
      this.setAreaPieA(this.waterUseJobData.rows);
      this.timer = setInterval(() => {
        // this.stats4WrbWaterConsume();
        // this.getCenterData();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(['SetDetailDialogParam', 'SetWaterUseCount']),
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
      this.SetDetailDialogParam({
        mediumSized: '',
        codeTpye: val,
        analyseDialogShow: true
      });
    },
    //获取行政区划边界
    getAreaGeometry() {
      const searchUrl = mapConfig.mapServer + 'wenzhou/wenzhou_fangxunend/MapServer/3/query';
      const opt = {
        where: '1=1',
        outFields: '*',
        f: 'pjson'
      };
      queryMapserver(searchUrl, opt).then((res) => {
        if (res.features) {
          this.areaGeometryArr = res.features;
        }
      });
    },
    activeCheckMap(index) {
      if (this.activesIndex == index) {
        this.activesIndex = '-1';
      } else {
        this.activesIndex = index;
      }
    },
    handleTabChange(val) {
      this.activeTab = val;
      console.log(val);
      this.stats4WrbWaterConsume();
      switch (this.activeTab) {
        case 1:
          this.$nextTick(() => {
            this.setAreaPieB(this.waterUseJobData.rows);
          });
          break;
        case 2:
          this.$nextTick(() => {
            this.setAreaPieC(this.waterUseJobData.rows);
          });
          break;
        default:
          this.$nextTick(() => {
            this.setAreaPieA(this.waterUseJobData.rows);
          });
          break;
      }
    },
    /**
     * 用水量行业类别数据接口请求
     */
    async stats4WrbWaterConsume() {
      let opt = {
        type: this.activeTab == 0 ? '1' : this.activeTab == 1 ? '2' : '3',
        year: this.checkYear ? Number(this.checkYear.substring(0, 4)) : ''
      };
      let data = [];
      let count = 0;
      await waterResourceApi.stats4WrbWaterConsume(opt).then((res) => {
        this.checkYear = res.data[0].year + ' 年';
        res.data.forEach((item) => {
          let value = item.classifyValue || item.totalConsume;
          let obj = {
            name: item.classifyName,
            value: value.toFixed(2)
          };
          count = count + item.classifyValue;
          data.push(obj);
        });
        if (this.activeTab == 0) {
          this.waterUseJobData.rows = data;
          this.waterUseJobData.totalLength = count.toFixed(2);
        } else if (this.activeTab == 1) {
          this.waterUseAreaData.columns = ['adnm', 'totalConsume'];
          this.waterUseAreaOption.setting.yAxisName = ['亿m³'];
          this.waterUseAreaData.rows = res.data;
          this.$nextTick(() => {
            this.$refs['chart'].echarts.resize();
          });
        } else {
          this.waterUseAreaData.columns = ['adnm', 'watConPerCapita'];
          this.waterUseAreaData.rows = res.data;
          this.waterUseAreaOption.setting.yAxisName = ['m³'];
          this.$nextTick(() => {
            this.$refs['chart'].echarts.resize();
          });
        }
      });
    },
    // 获取C位数据
    async getCenterData() {
      let opt = {
        year: this.checkYear ? Number(this.checkYear.substring(0, 4)) : ''
      };
      await waterResourceApi.statsCwindow4WrbWaterConsume(opt).then((res) => {
        this.SetWaterUseCount(res.data);
      });
    },
    /**
     * 图表 行业类别
     */
    setAreaPieA(data) {
      let color0 = this.setEchartsColor('rgba(60, 255, 251, 0)', '#1CFFF5');
      let color1 = this.setEchartsColor('#EEC80B', 'rgba(248, 230, 26, 0)');
      let color2 = this.setEchartsColor('#FF7144', 'rgba(255, 170, 121, 0)');
      this.waterUseEchart(this.$refs.areaPieA, color0, color1, color2, data);
    },
    setAreaPieB(data) {
      let color4 = this.setEchartsColor('rgba(168, 11, 238, 0)', '#A80BEE');
      let color5 = this.setEchartsColor('#19AF47', 'rgba(25, 175, 71, 0)');
      let color6 = this.setEchartsColor('#24D4FF', 'rgba(0, 143, 232, 0)');
      this.waterUseEchart(this.$refs.areaPieB, color4, color5, color6, data);
    },
    setAreaPieC(data) {
      let color7 = this.setEchartsColor('rgba(81, 250, 193, 0)', '#51FAC1');
      let color8 = this.setEchartsColor('#FBE057', 'rgba(252, 224, 87, 0)');
      let color9 = this.setEchartsColor('#24D4FF', 'rgba(0, 143, 232, 0)');
      this.waterUseEchart(this.$refs.areaPieC, color7, color8, color9, data);
    },
    setEchartsColor(color0, color1) {
      return {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: color0 // 0% 处的颜色
          },
          {
            offset: 1,
            color: color1 // 100% 处的颜色
          }
        ]
      };
    },
    waterUseEchart(chartDom, color0, color1, color2, data) {
      var myChart = echarts.init(chartDom);
      var option;
      option = {
        // tooltip: {
        //   show: true,
        //   trigger: 'item',
        //   formatter: function (data) {
        //     return (
        //       data.name +
        //       "<br/><span style='font-size: 20px;color: #1CFFF5;line-height: 26px;'>" +
        //       data.value +
        //       "</span> <span  style='font-size: 14px;'> 亿m³</span>"
        //     );
        //   },
        //   confine: true,
        //   extraCssText: 'width:160px;height:50px;font-size:18px;'
        // },
        series: [
          {
            type: 'pie',
            radius: ['50%', '80%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            labelLine: {
              show: false
            },
            data,
            color: [color0, color1, color2]
          }
        ]
      };
      option && myChart.setOption(option);
    },

    // 获取年份选择下拉内容
    getYearOption() {
      let date = new Date();
      let y = date.getFullYear();
      for (let i = y; i >= 1990; i--) {
        this.yearOption.push(i + ' 年');
      }
      // this.checkYear = y + " 年";
    },
    // 下拉年份选择后
    handleYear() {
      this.stats4WrbWaterConsume().then(() => {
        if (this.realdzmbtn) {
          this.showAreaPolygon();
        }
      });
      this.getCenterData();
    },
    // 行政区划柱状图设置
    barConfig(options) {
      let data = options.series[0].data;
      let colorArr = [];
      colorArr = ['#15E2DB', '#9DFDFB', '#A6FFFF'];
      let color = {
        type: 'linear',
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr[0]
          },
          {
            offset: 0.5,
            color: colorArr[0]
          },
          {
            offset: 0.5,
            color: colorArr[1]
          },
          {
            offset: 1,
            color: colorArr[1]
          }
        ]
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
                borderColor: 'rgba(0,0,0,0)',
                borderWidth: 2,
                color: 'rgba(0,0,0,0)'
              }
            }
          });
        }
      });

      options.series = [
        {
          z: 1,
          name: '数据',
          type: 'bar',
          barWidth: barWidth,
          barGap: '0%',
          data: data,
          itemStyle: {
            normal: {
              color: color
            }
          },
          label: {
            show: true,
            fontSize: 18,
            position: 'top',
            color: '#1EF8F2'
          }
        },
        {
          z: 2,
          name: '数据',
          type: 'pictorialBar',
          data: constData,
          symbol: 'diamond',
          symbolOffset: ['0%', '50%'],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color
            }
          },
          tooltip: {
            show: false
          }
        },
        {
          z: 3,
          name: '数据',
          type: 'pictorialBar',
          symbolPosition: 'end',
          data: showData,
          symbol: 'diamond',
          symbolOffset: ['0%', '-50%'],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr[2],
              borderWidth: 2,
              color: colorArr[2]
            }
          },
          tooltip: {
            show: false
          }
        }
      ];
      options.tooltip = {
        show: true,
        trigger: 'item',
        confine: true,
        formatter: (val) => {
          let str = this.waterUseAreaData.rows.find((item) => {
            if (item.adnm == val.name) {
              return item;
            }
          });
          if (this.activeTab == 1) {
            let htmlText =
              `<p class="rivertooltip__item rivertooltip__item-error"><span>${str.adnm}</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>用水量: </span><span>${str.totalConsume}亿m³</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>较上一年: </span><span>${str.tcCompareLastYearRate}%</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>占全市水资源总量: </span><span>${str.tcOfCityRate}%</span></p>`;
            return htmlText;
          } else if (this.activeTab == 2) {
            let htmlText =
              `<p class="rivertooltip__item rivertooltip__item-error"><span>${str.adnm}</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>人均综合用水量: </span><span>${str.watConPerCapita}m³</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>较上一年: </span><span>${str.wcpcCompareLastYearRate}%</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>相对于温州: </span><span>${
                str.wzWcpc
              }m³ </span> ${str.wcpcSubWzPosNeg == true ? '多' : '少'} <span>${str.wcpcSubWzAbsVal}m³</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>相对于浙江省: </span><span>${
                str.zjWcpc
              }m³</span> ${str.wcpcSubZjPosNeg == true ? '多' : '少'} <span>${str.wcpcSubZjAbsVal}m³</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>相对于全国: </span><span>${
                str.cnWcpc
              }m³</span> ${str.wcpcSubCnPosNeg == true ? '多' : '少'} <span>${str.wcpcSubCnAbsVal}m³</span></p>`;
            return htmlText;
          }
        }
      };
      options.dataZoom = generateDataZoom(4, this.waterUseAreaData.rows.length);
      return options;
    },
    clickRealtimeRainfull() {
      this.realdzmbtn = !this.realdzmbtn;
      if (this.realdzmbtn) {
        this.showAreaPolygon();
      } else {
        this.map.drawAreaPolygon([], 'waterUseAreaLayer');
        this.$emit('legend-change', '用水量', {});
      }
    },
    showAreaPolygon() {
      let rows = this.waterUseAreaData.rows;
      let arr = this.waterUseAreaData.rows.map((item) => {
        return item.totalConsume;
      });
      if (this.activeTab == 1) {
        arr = this.waterUseAreaData.rows.map((item) => {
          return item.totalConsume;
        });
      } else if (this.activeTab == 2) {
        arr = this.waterUseAreaData.rows.map((item) => {
          return item.watConPerCapita;
        });
      }
      const max = Math.max(...arr);
      const min = Math.min(...arr);
      const onePercent = (max - min) / 5;
      rows.map((item) => {
        item.geometry = this.getGeometryByName(item.adnm);
        item.attr = item;
        item.type = '用水量';
        item.active = this.activeTab;
        if (this.activeTab == 1) {
          item.style = this.getColor(item.totalConsume, max, min);
        } else if (this.activeTab == 2) {
          item.style = this.getColor(item.watConPerCapita, max, min);
        }
      });
      this.map.drawAreaPolygon(rows, 'waterUseAreaLayer');
      this.$emit('legend-change', '用水量', {
        label: '用水量',
        children: [
          {
            label: `${parseFloat(onePercent * 4).toFixed(1)} - ${parseFloat(onePercent * 5).toFixed(1)}`,
            icon: 'evaluate-type5'
          },
          {
            label: `${parseFloat(onePercent * 3).toFixed(1)} - ${parseFloat(onePercent * 4).toFixed(1)}`,
            icon: 'evaluate-type4'
          },
          {
            label: `${parseFloat(onePercent * 2).toFixed(1)} - ${parseFloat(onePercent * 3).toFixed(1)}`,
            icon: 'evaluate-type3'
          },
          {
            label: `${parseFloat(onePercent).toFixed(1)} - ${parseFloat(onePercent * 2).toFixed(1)}`,
            icon: 'evaluate-type2'
          },
          { label: `${parseFloat(min).toFixed(1)}-${parseFloat(onePercent).toFixed(1)}`, icon: 'evaluate-type1' }
        ]
      });
    },
    getGeometryByName(name) {
      let data = this.areaGeometryArr.filter((item) => {
        return item.attributes.NAME == name;
      });
      return data[0].geometry.rings;
    },
    getColor(data, max, min) {
      const normalizationRatio = this.normalization(data, max, min);
      const lineColors = ['#6D6DF0', '#3797F7', '#42CC71', '#FEB722', '#F7413F'];
      const fillColors = [
        'rgba(109,109,240,0.5)',
        'rgba(55,151,247,0.5)',
        'rgba(66,204,113,0.5)',
        'rgba(254,183,34,0.5)',
        'rgba(247,65,63,0.5)'
      ];
      let colorObj = {
        fillColor: '',
        lineColor: ''
      };
      if (normalizationRatio <= 0.2) {
        colorObj.fillColor = fillColors[0];
        colorObj.lineColor = lineColors[0];
      } else if (normalizationRatio <= 0.4) {
        colorObj.fillColor = fillColors[1];
        colorObj.lineColor = lineColors[1];
      } else if (normalizationRatio <= 0.6) {
        colorObj.fillColor = fillColors[2];
        colorObj.lineColor = lineColors[2];
      } else if (normalizationRatio <= 0.8) {
        colorObj.fillColor = fillColors[3];
        colorObj.lineColor = lineColors[3];
      } else {
        colorObj.fillColor = fillColors[4];
        colorObj.lineColor = lineColors[4];
      }
      return colorObj;
    },
    normalization(value, max, min) {
      let normalizationRatio = (value - min) / (max - min);
      return normalizationRatio;
    }
  }
};
</script>

<style lang="scss" scoped>
@import './style/SildePanel.scss';
.manual-div {
  > div {
    color: #ffffff;
    padding-bottom: 10px;
    &:last-child{
      padding-bottom: 0;
    }
    &.avtive {
      color: #00baff;
    }
  }
}
.water-card {
  height: 100%;
  .water-title-switch {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 7px;
    > div:nth-child(1) {
      // padding-bottom: 5px;
      .select-box {
        width: 100px;
        margin-right: 40px;
        ::v-deep .el-input__inner {
          height: 28px;
          line-height: 28px;
          background: transparent;
          font-size: 12px;
        }
        ::v-deep .el-input--small .el-input__icon {
          line-height: 28px;
        }
      }
    }
    > div:nth-child(2) {
      i {
        cursor: pointer;
        margin-right: 5px;
      }
    }
  }
  .icon-sty {
    margin-right: 10px;
  }

  .icon-circle {
    width: 12px !important;
    height: 12px !important;
  }

  .icon-year-0 {
    @include chart-icon();
    background: #1cf0e6;
  }
  .icon-year-1 {
    @include chart-icon();
    background: #debc0b;
  }
  .icon-year-2 {
    @include chart-icon();
    background: #e06942;
  }
  .icon-year-3 {
    @include chart-icon();
    background: #930ad1;
  }
  .icon-year-4 {
    @include chart-icon();
    background: #17a442;
  }
  .icon-year-5 {
    @include chart-icon();
    background: #1ebce6;
  }
  .icon-year-6 {
    @include chart-icon();
    background: #1cf0e6;
  }
  .icon-year-7 {
    @include chart-icon();
    background: #f9df57;
  }
  .icon-year-8 {
    @include chart-icon();
    background: #1fbee9;
  }
  .area-box {
    width: 100%;
    height: calc(100% - 34px);
    padding-top: 14px;
    .area-box-title {
      height: 34px;
      background: rgba(0, 186, 255, 0.1);
      border-radius: 2px;
      border: 1px solid rgba(0, 186, 255, 0.54);
      text-align: center;
      line-height: 30px;
      font-size: 14px;
      span {
        font-size: 20px;
        padding-left: 16px;
        color: #00baff;
      }
    }
  }
  .pie-chart-panel {
    height: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-around;
    #waterUseEchart {
      width: 160px;
      height: 100%;
    }
    .legend-panel {
      width: calc(100% - 160px);
      .legend-item {
        display: flex;
        align-items: center;
        padding-bottom: 16px;
        &:last-child {
          padding-bottom: 0;
        }
        > i {
          width: 14px !important;
          height: 14px !important;
          position: relative;
          top: -3px;
        }
        .text {
          display: flex;
          justify-content: space-between;
          width: calc(100% - 20px);
          p {
            font-size: 14px;
            &:last-child {
              margin-bottom: 0;
            }
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
.water-card .water-card-panel .panel-item__title {
  justify-content: space-between;
}
.water-card .water-card-panel .panel-item__title .panel-item__title--switch {
  margin-left: 0;
  display: flex;
}
</style>
