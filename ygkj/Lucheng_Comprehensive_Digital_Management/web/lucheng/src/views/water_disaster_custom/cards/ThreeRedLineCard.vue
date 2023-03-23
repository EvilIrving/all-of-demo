<!--
 * @Autor: ysl
 * @Date: 2021-08-12 19:39:57
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-14 13:26:15
 * @Description: 三条红线
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/ThreeRedLineCard.vue
-->
<template>
  <div class="three-red-line-card">
    <panel-item
      title="三条红线"
      has-switch
      tc
      @tclick="tclick('三条红线')"
      :action-tabs="['用水总量', '用水效率', '限制纳污']"
      @change="handleTabChange"
    >
      <template #title-switch>
        <RadioDropdown
          v-model="checkYear"
          :options="yearOption"
          :keyOption="{ labelProp: 'label', valueProp: 'value' }"
          @change="handleYear"
        />
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('三条红线')"
          style="margin-left: 10px"
        />
      </template>

      <div class="area-box">
        <div class="show_message">
          <template v-if="activeTab == 0"
            >{{ checkYear }}年全市用水量红线{{ messageInfo.controlWatCsm }}亿m³，实际用水量{{
              messageInfo.totalWatCsm
            }}亿m³</template
          >
          <template v-if="activeTab == 1">{{ checkYear }}年全市万元GDP增加用水量降幅</template>
          <template v-if="activeTab == 2">{{ checkYear }}年全省重要水功能区水质达标评价达标率</template>
        </div>
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
  </div>
</template>
<script>
import { mapGetters, mapActions } from 'vuex';
import PanelItem from '../components/PanelItem.vue';
import { waterResourceApi } from '@/api/waterResourcesGuarantee';
import { generateDataZoom } from './utils/chart.js';
import RadioDropdown from '../components/RadioDropdown.vue';

export default {
  name: 'governmentTravelCard',
  components: { PanelItem, RadioDropdown },
  computed: {
    ...mapGetters(['previewModule']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },

  data() {
    return {
      activeTab: 0,

      yearOption: [],
      yearDropdownVisible: false,
      checkYear: 2020, // new Date().getFullYear()
      barData: {
        columns: ['adnm', 'controlWatCsm', 'totalWatCsm'],
        rows: []
      },
      totalLength: 0,
      barOption: {
        grid: {
          top: '24%',
          bottom: '5%',
          left: '0%',
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
          legend: {
            right: '0%',
            icon: 'circle',
            itemWidth: 10,
            textStyle: { color: '#FFF' }
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
            }
          }
        }
      },
      messageInfo: {},
      testData: [
        { adnm: '鹿城区', wpgDecrRateNorm: '78', wpgDecrRate: '88' },
        { adnm: '龙湾区', wpgDecrRateNorm: '63', wpgDecrRate: '80' },
        { adnm: '瓯海区', wpgDecrRateNorm: '52', wpgDecrRate: '90' },
        { adnm: '洞头区', wpgDecrRateNorm: '31', wpgDecrRate: '37' },
        { adnm: '永嘉县', wpgDecrRateNorm: '37', wpgDecrRate: '41' }
      ]
    };
  },
  created() {
    this.getYearOption();
  },
  mounted() {
    this.$nextTick(() => {
      this.getRedLineData();
      this.getCenterData();
      this.timer = setInterval(() => {
        this.getRedLineData();
        this.getCenterData();
      }, 300000);
    });
  },
  methods: {
    ...mapActions(['SetDetailDialogParam', 'SetThreeRedLineCount']),

    // 获取三条红线数据
    async getRedLineData(type) {
      let opt = { year: this.checkYear };
      let str = this.activeTab == 0 ? 'redLineWatCsm' : 'redLineWpgEffect';
      this.barData.rows = [];
      this.activeTab == 2
        ? (this.barData.rows = this.testData)
        : await waterResourceApi[str](opt).then((res) => {
            this.messageInfo = res.data.wzInfo;
            this.messageInfo.totalWatCsm = this.messageInfo.totalWatCsm ? this.messageInfo.totalWatCsm.toFixed(2) : 0;
            this.messageInfo.controlWatCsm = this.messageInfo.controlWatCsm
              ? this.messageInfo.controlWatCsm.toFixed(2)
              : 0;

            res.data.list.forEach((item) => {
              let obj = {
                adnm: item.adnm,
                controlWatCsm: item.controlWatCsm && item.controlWatCsm.toFixed(2),
                totalWatCsm: item.totalWatCsm && item.totalWatCsm.toFixed(2),
                wpgDecrRateNorm: (item.wpgDecrRateNorm && item.wpgDecrRateNorm.toFixed(2)) || 0,
                wpgDecrRate: (item.wpgDecrRate && item.wpgDecrRate.toFixed(2)) || 0
              };
              this.barData.rows.push(obj);
            });
          });
      this.$nextTick(() => {
        this.$refs['chart'].echarts.resize();
      });
    },
    // 获取C位数据
    async getCenterData() {
      let opt = {};
      await waterResourceApi.redLine4CWindow(opt).then((res) => {
        this.SetThreeRedLineCount(res.data);
      });
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
      this.$emit('openDialog', '三条红线');
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
    },
    handleTabChange(val) {
      this.activeTab = val;
      val == 0
        ? (this.barData.columns = ['adnm', 'controlWatCsm', 'totalWatCsm'])
        : (this.barData.columns = ['adnm', 'wpgDecrRateNorm', 'wpgDecrRate']);
      this.getRedLineData();
    },
    barConfig(options) {
      let curr = options.series[0].data;
      let supp = options.series[1].data;
      let colorArr1 = ['#15E2DB', '#9DFDFB', '#A6FFFF'];
      let colorArr2 = ['#06a2d4', '#62d9ff', '#64d9ff'];
      let color1 = {
        type: 'linear',
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr1[0]
          },
          {
            offset: 0.5,
            color: colorArr1[0]
          },
          {
            offset: 0.5,
            color: colorArr1[1]
          },
          {
            offset: 1,
            color: colorArr1[1]
          }
        ]
      };
      let color2 = {
        type: 'linear',
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr2[0]
          },
          {
            offset: 0.5,
            color: colorArr2[0]
          },
          {
            offset: 0.5,
            color: colorArr2[1]
          },
          {
            offset: 1,
            color: colorArr2[1]
          }
        ]
      };
      let barWidth = 15;
      let constData1 = [];
      let showData1 = [];
      let constData2 = [];
      let showData2 = [];
      curr.filter(function(item) {
        if (item) {
          constData1.push(1);
          showData1.push(item);
        } else {
          constData1.push(0);
          showData1.push({
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
      supp.filter(function(item) {
        if (item) {
          constData2.push(1);
          showData2.push(item);
        } else {
          constData2.push(0);
          showData2.push({
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

      options.tooltip = {
        show: true,
        formatter: (data) => {
          return (
            data.name +
            "<br/><span style='font-size: 14px;'>控制指标：" +
            curr[data.dataIndex] +
            "</span> <br/><span style='font-size: 14px;'>完成情况：" +
            supp[data.dataIndex] +
            '</span>'
          );
        }
      };

      options.series = [
        {
          z: 1,
          name: '控制指标',
          type: 'bar',
          barWidth: barWidth,
          barGap: '30%',
          data: curr,
          itemStyle: {
            normal: {
              color: color1
            }
          },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            position: 'top',
            color: '#1EF8F2'
          }
        },
        {
          z: 2,
          name: '',
          type: 'pictorialBar',
          data: constData1,
          symbol: 'diamond',
          symbolOffset: ['-60%', '55%'],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color1
            }
          },
          tooltip: {
            show: false
          }
        },
        {
          z: 3,
          name: '',
          type: 'pictorialBar',
          symbolPosition: 'end',
          data: showData1,
          symbol: 'diamond',
          symbolOffset: ['-92%', '-50%'],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr1[2],
              borderWidth: 2,
              color: colorArr1[2]
            }
          },
          tooltip: {
            show: false
          }
        },
        {
          z: 1,
          name: '完成情况',
          type: 'bar',
          barWidth: barWidth,
          barGap: '30%',
          data: supp,
          itemStyle: {
            normal: {
              color: color2
            }
          },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            position: 'top',
            color: '#06a2d4'
          }
        },
        {
          z: 2,
          name: '',
          type: 'pictorialBar',
          data: constData2,
          symbol: 'diamond',
          symbolOffset: ['65%', '50%'],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color2
            }
          },
          tooltip: {
            show: false
          }
        },
        {
          z: 3,
          name: '',
          type: 'pictorialBar',
          symbolPosition: 'end',
          data: showData2,
          symbol: 'diamond',
          symbolOffset: ['90%', '-50%'],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr2[2],
              borderWidth: 2,
              color: colorArr2[2]
            }
          },
          tooltip: {
            show: false
          }
        }
      ];
      options.dataZoom = generateDataZoom(4, this.barData.rows.length);
      options.yAxis[0].name = this.activeTab == 0 ? '亿m³' : '%';
      return options;
    },

    // 获取年份选择下拉内容
    getYearOption() {
      let date = new Date();
      let y = date.getFullYear();
      this.yearOption = [{ label: '全部', value: '' }];
      for (let i = y; i >= 1990; i--) {
        this.yearOption.push({
          label: i + ' 年',
          value: i
        });
      }
    },
    // 下拉年份选择后
    handleYear() {
      this.getRedLineData();
    }
  }
};
</script>
<style lang="scss" scoped>
@import './style/SildePanel.scss';
.three-red-line-card {
  width: 100%;
  height: 100%;
  .area-box,
  .is-water-box {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
  }
  .is-water-box {
    display: flex;
  }
  .pie-box {
    width: 100%;
    height: calc(100% - 24px);
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
#barChart {
  width: 100%;
  height: 100%;
}
</style>

<style lang="scss">
.el-dropdown-menu.year-dropdown {
  max-height: 200px;
  overflow: auto;
  background: rgba(2, 10, 28, 0.7);
  //   background: #061639;
  border: 1px solid #1cfff5;
  .el-dropdown-menu__item {
    line-height: 26px;
  }
  .el-radio {
    color: #fff;
  }
  .el-radio__input.is-checked .el-radio__inner {
    border-color: #1cfff5;
    background: transparent;
    &::after {
      width: 8px;
      height: 8px;
      background: #1cfff5;
    }
  }
  .el-radio__input.is-checked + .el-radio__label {
    color: #1cfff5;
  }
  .el-radio__inner {
    background: transparent;
    border: 1px solid #fff;
  }
}
.el-dropdown-menu.year-dropdown.el-popper[x-placement^='bottom'] .popper__arrow::after {
  border-bottom-color: rgba(2, 10, 28, 0.7);
}
.show_message {
  padding: 8px 4px;
  margin-top: 6px;
  color: #1cfff5;
  font-size: 14px;
  display: flex;
  text-align: center;
  justify-content: space-around;
  align-items: center;
  background-color: rgba($color: #1cfff5, $alpha: 0.1);
}
</style>

<style lang="scss">
.three-red-line-card {
  .panel-item__content {
    height: 100%;
    display: flex;
    flex-direction: column;
  }
}
.three-red-line-card .panel-item .panel-item__content--tabs .tab-button,
.three-red-line-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.three-red-line-card .panel-item .panel-item__content--tabs .tab-button.active,
.three-red-line-card .panel-item .panel-item__title--tabs .tab-button.active {
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
}
.icon-color-1 {
  @include chart-icon();
  background: #15e2db;
}
.icon-color-2 {
  @include chart-icon();
  background: #008fe8;
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
    width: 40%;
    .legend-item {
      line-height: 25px;
      display: flex;
      justify-content: space-between;
      .text {
        font-size: 14px;
        margin-right: 10px;
      }
      .num {
        color: #1cfff5;
        font-size: 1rem;
      }
    }
  }
}
</style>
