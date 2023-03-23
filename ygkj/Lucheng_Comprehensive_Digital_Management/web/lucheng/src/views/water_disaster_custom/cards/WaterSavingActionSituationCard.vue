<template>
  <div class="water-saving-action-situation-card">
    <panel-item :title="'节水行动态势'" has-switch tc @tclick="tclick('节水行动态势')">
      <template #title-switch>
        <i
          title="地图落点"
          @click="situationDrawMapHandle('sldbtn')"
          :class="[sldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
        />
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog"
          style="margin-left: 10px"
        />
      </template>

      <div class="charts-panel">
        <!-- 水量/水质评价 -->
        <sub-title-click-card :title="['节水指标', '取水监管']" @tabChange="tabChangeHandle">
          <div slot="title-operate">
            <el-dropdown placement="bottom-start" trigger="click" v-if="activeTab == '节水指标'">
              <span class="el-dropdown-link" :class="dropdownVisible ? 'dropdown-active' : ''" style="max-width: 9rem">
                <span style="font-size: 1rem">{{ filterTypeOptions[checkType].label }}</span>
                <i class="el-icon-caret-bottom"></i>
              </span>
              <el-dropdown-menu slot="dropdown" class="year-dropdown">
                <el-dropdown-item v-for="(item, index) in filterTypeOptions" :key="index">
                  <el-radio v-model="checkType" :label="item.value" @change="handleType">{{ item.label }}</el-radio>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div v-show="activeTab == '节水指标'" id="savingLineChart" style="width: 100%; height: 100%"></div>
          <template v-if="activeTab == '取水监管'">
            <div v-if="actionTabs" class="panel-item__content--tabs">
              <span
                v-for="(item, index) in actionTabs"
                :key="index"
                :class="[index == activeActionTab && 'active', 'tab-button']"
                @click="handleActionTabChange(index)"
              >
                {{ item }}
              </span>
            </div>
            <div class="is-water-box" v-if="activeActionTab == 0">
              <ve-histogram
                ref="chart"
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
            <div class="chart-panel pie-box" v-if="activeActionTab == 1">
              <div id="typePie" style="width: 50%; height: 100%" ref="typePie"></div>
              <div class="legend-panel">
                <div class="legend-item" v-for="(item, index) in typeOptions" :key="index">
                  <div>
                    <i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
                    <span class="text">{{ item.label }}</span>
                  </div>
                  <span class="num">{{ item.value }}个</span>
                </div>
              </div>
            </div>
          </template>
        </sub-title-click-card>
      </div>
    </panel-item>
    <WaterSavingActionDetail :visible.sync="detailVisible" :type="detailType" />
  </div>
</template>
<script>
import { mapGetters, mapActions } from 'vuex';
import PanelItem from '../components/PanelItem.vue';
import SubTitleClickCard from '../components/SubTitleClickCard.vue';
import { waterResourceApi } from '@/api/waterResourcesGuarantee';
import { generateDataZoom } from './utils/chart.js';

import * as echarts from 'echarts'; // echart插件
import WaterSavingActionDetail from './dialog/WaterSavingActionDetail.vue';

export default {
  name: 'WaterSavingActionSituationCard',
  components: { PanelItem, SubTitleClickCard, WaterSavingActionDetail },
  computed: {
    ...mapGetters(['previewModule']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },

  data() {
    return {
      detailVisible: false,
      detailType: 'norm',
      activeTab: '节水指标',
      sldbtn: false,
      actionTabs: ['按区域', '按取水许可'],
      activeActionTab: 0,
      waterUseData: {},
      barData: {
        columns: ['areaName', 'totalWra'],
        rows: []
      },
      showLine: true,
      checkType: 0,
      dropdownVisible: false,
      filterTypeOptions: [
        { label: '年用水总量', value: 0, unit: '亿m³', type: 'totalWatCsm' },
        { label: '万元GDP用水量', value: 1, unit: '%', type: 'wpg' },
        { label: '万元工业增加值用水量', value: 2, unit: '%', type: 'induWpg' },
        {
          label: '农业灌溉水有效利用系数',
          value: 3,
          unit: '',
          type: 'agIrrEffectFactor'
        },
        {
          label: '县域节水型社会创建达标率',
          value: 4,
          unit: '%',
          type: 'watSavRate'
        }
      ],
      typeOptions: [
        { label: '过期', name: 'areaInNumTotal1', value: 0 },
        { label: '超许可', name: 'areaOutNumTotal2', value: 0 },
        { label: '有证无点', name: 'areaInNumTotal3', value: 0 },
        { label: '取水偏快', name: 'areaOutNumTotal4', value: 0 }
      ],
      totalLength: 0,
      barOption: {
        grid: {
          top: '15%',
          bottom: '0%',
          containLabel: true
        },
        setting: {
          yAxisName: ['个'],
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
      }
    };
  },
  created() {},
  mounted() {
    window.addEventListener('resize', this.handleWindowResize);
    this.waterUseDataList();
    this.getCenterData();
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleWindowResize);
  },
  methods: {
    ...mapActions(['SetDetailDialogParam', 'SetWaterSavingActionSituationCount']),

    // 获取取水监管-按区域数据
    async stats4WrbWra() {
      //type： 1.根据行政区划统计，2.按取水许可统计（过期、超许可、有证无点、取水偏快）,3，取水用途统计：城市生活、农村生活、工业、农业、水力发电、其他、非水力发电;4，统计总许可取水量，总许可证次数
      let opt = {
        type: this.activeActionTab + 1
      };
      this.totalLength = 0;
      await waterResourceApi.intakeSuperviseStats(opt).then((res) => {
        opt.type == 1
          ? (this.barData.rows = res.data.map((item) => {
              item.areaName = item.key;
              item.totalWra = item.value;
              return item;
            }))
          : this.typeOptions.map((item) => {
              item.value = res.data.filter((sItem) => sItem.key == item.label)[0].value;
              this.totalLength += item.value;
              return item;
            });
      });
      this.$nextTick(() => {
        opt.type == 1 ? this.$refs['chart'].echarts.resize() : this.setTypePie();
      });
    },
    // 获取C位数据
    async getCenterData() {
      let opt = {};
      let str = this.activeTab == '节水指标' ? 'watSavNorm4CWindow' : 'intakeSupervise4CWindow';
      await waterResourceApi[str](opt).then((res) => {
        res.data.type = this.activeTab;
        this.SetWaterSavingActionSituationCount(res.data);
      });
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog() {
      this.detailType = this.activeTab == '节水指标' ? 'norm' : 'supervision';
      this.detailVisible = true;
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
    },
    // 节水指标切换
    handleType() {
      this.waterUseDataList();
      if (this.sldbtn) this.situationDrawMapHandle('sldbtn');
    },
    // 一级页签切换
    tabChangeHandle(val) {
      this.activeTab = val;
      this.activeActionTab = 0;
      val == '节水指标' ? this.waterUseDataList() : this.stats4WrbWra();
      this.getCenterData();
      if (this.sldbtn) this.situationDrawMapHandle('sldbtn');
    },
    // 取水监管-二级页签切换
    handleActionTabChange(val) {
      this.activeActionTab = val;
      this.$nextTick(() => {
        this.stats4WrbWra();
      });
    },
    // 取水监管 按区域 柱状图
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
      let barWidth = 18;
      let constData = [];
      let showData = [];
      data.filter(function(item) {
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
            fontSize: 12,
            position: 'top',
            color: '#1EF8F2'
          },
          markPoint: {
            // 设置最大值和最小值
            symbol: 'circle',
            symbolSize: 1,
            data: [
              {
                type: 'max',
                name: '最大'
              },
              {
                type: 'min',
                name: '最小'
              }
            ],
            label: {
              show: true,
              formatter: '{b}\n{c}',
              color: '#1EF8F2',
              fontSize: 12,
              position: 'top'
            }
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
      options.dataZoom = generateDataZoom(4, this.barData.rows.length);
      return options;
    },

    // 节水指标 数据
    async waterUseDataList() {
      this.waterUseData = {
        name: [],
        now: [],
        watConControlNorm: 0
      };
      let opt = {};
      this.showLine = false;
      let type = this.filterTypeOptions[this.checkType].type;
      await waterResourceApi.watSavNormByYearInterval(opt).then((res) => {
        this.showLine = true;
        this.waterUseData.watConControlNorm = res.data.norm[type] || 0;
        res.data.list.forEach((item) => {
          this.waterUseData.name.push(item.year);
          this.waterUseData.now.push(item[type] ? item[type].toFixed(2) : 0);
        });
      });
      this.$nextTick(() => {
        this.waterUseConfig();
      });
    },
    // 节水指标 线图
    waterUseConfig() {
      const el = document.getElementById('savingLineChart');
      const charts = echarts.init(el);
      let iconPath1 = require('@/assets/images/map-realigning-3.png');
      let iconPath2 = require('@/assets/images/map-realigning-2.png');
      let options = {
        dataZoom: generateDataZoom(6, this.waterUseData.name.length),
        grid: {
          top: '20%',
          bottom: '5%',
          left: '0%',
          right: '0%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.waterUseData.name,
          axisLabel: {
            show: true,
            textStyle: {
              color: '#fff'
            }
          },
          axisTick: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: '#fff',
              fontSize: this.$commonSetting.chartFontSize
            }
          }
        },
        yAxis: {
          type: 'value',
          name: this.filterTypeOptions[this.checkType].unit,
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
            lineStyle: { show: false, color: '#fff', opacity: 0.2 }
          },
          minInterval: 1
        },
        legend: {
          right: '10',
          selectedMode: false,
          textStyle: { color: '#fff', fontSize: 12 },
          data: [
            {
              name: this.filterTypeOptions[this.checkType].label,
              itemStyle: { opacity: 0 },
              lineStyle: { color: '#1ef8f2' }
            },
            {
              name: '十四五控制指标',
              itemStyle: { opacity: 0 },
              lineStyle: { type: 2, color: '#e89100' }
            }
          ]
        },
        tooltip: {
          trigger: 'axis',
          formatter: (data) => {
            return (
              data[0].name +
              '<br/><span>' +
              this.filterTypeOptions[this.checkType].label +
              '：</span>' +
              data[0].value +
              '<span>' +
              this.filterTypeOptions[this.checkType].unit +
              '</span>' +
              '<br/>十四五控制指标：' +
              this.waterUseData.watConControlNorm +
              '<span>' +
              this.filterTypeOptions[this.checkType].unit +
              '</span>'
            );
          },
          confine: true
        },
        series: [
          {
            name: this.filterTypeOptions[this.checkType].label,
            type: 'line',
            smooth: true,
            data: this.waterUseData.now,
            symbol: 'none',
            lineStyle: {
              color: '#1EF8F2'
            },
            markLine: {
              symbol: 'none',
              label: {
                color: '#E89100',
                fontWeight: 'bold',
                position: 'middle',
                formatter: ' ' // 十四五用水总量控制指标
              },
              lineStyle: {
                normal: {
                  type: 'dotted',
                  color: '#E89100',
                  width: 2
                }
              },
              data: [{ yAxis: this.waterUseData.watConControlNorm, name: 'max' }]
            }
          },
          {
            name: '十四五控制指标',
            type: 'line',
            smooth: true,
            symbol: 'none',
            lineStyle: {
              color: '#1EF8F2'
              // type: "dotted", //'dotted'虚线 'solid'实线
            },
            data: []
          }
        ]
      };
      options && charts.setOption(options);
    },
    // 按取水许可 饼图
    setTypePie() {
      var chartDom = document.getElementById('typePie');
      var myChart = echarts.init(chartDom);
      var option;
      const color0 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: '#328fe8' // 0% 处的颜色
          },
          {
            offset: 1,
            color: 'rgba(0,0,0)' // 100% 处的颜色
          }
        ],
        global: false // 缺省为 false
      };
      const color1 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: '#e89100' // 0% 处的颜色
          },
          {
            offset: 1,
            color: 'rgba(0,0,0)' // 100% 处的颜色
          }
        ],
        global: false // 缺省为 false
      };
      const color2 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: '#8a8ae1' // 0% 处的颜色
          },
          {
            offset: 1,
            color: 'rgba(0,0,0)' // 100% 处的颜色
          }
        ],
        global: false // 缺省为 false
      };
      const color3 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: '#a80bee' // 0% 处的颜色
          },
          {
            offset: 1,
            color: 'rgba(0,0,0)' // 100% 处的颜色
          }
        ],
        global: false // 缺省为 false
      };
      option = {
        tooltip: {
          show: true,
          trigger: 'item',
          formatter: function(data) {
            return (
              data.data.label +
              "<br/><span style='font-size: 20px;color: #1CFFF5;line-height: 26px;'>" +
              data.value +
              "</span> <span  style='font-size: 14px;'> 个</span>"
            );
          },
          confine: true,
          extraCssText: 'width:160px;height:50px;font-size:18px;'
        },
        series: [
          {
            type: 'pie',
            radius: ['50%', '80%'],
            centen: [0, 0],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center',
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: 'center',
                color: '#fff',
                fontSize: 18,
                formatter: `${this.totalLength}个\n 总数`
              }
            },
            labelLine: {
              show: false
            },
            data: this.typeOptions,
            color: [color0, color1, color2, color3]
          }
        ]
      };
      option && myChart.setOption(option);
    },

    // 节水指标点位数据
    async watSavNormByYear() {
      let opt = { compareType: this.checkType + 1 };
      let result = [];
      await waterResourceApi.watSavNormByYear(opt).then((res) => {
        result = res.data.map((item) => {
          item.value = item[this.filterTypeOptions[this.checkType].type]
            ? item[this.filterTypeOptions[this.checkType].type].toFixed(2)
            : 0;
          item.type = this.filterTypeOptions[this.checkType].label;
          item.unit = this.filterTypeOptions[this.checkType].unit;
          return item;
        });
      });
      return result;
    },
    // 落点按钮
    situationDrawMapHandle(btnType) {
      this[btnType] = !this[btnType];
      // legend-change
      this.$emit('legend-change', '节水指标', {});
      this.$emit('legend-change', '取水监管', {});
      if (this[btnType]) {
        if (this.activeTab == '节水指标') {
          this.setMapOverLay();
          this.$emit('legend-change', this.activeTab, {
            label: this.activeTab,
            children: [
              {
                label: '最大值',
                icon: 'water-max'
              },
              {
                label: '最小值',
                icon: 'water-min'
              },
              {
                label: '正常值',
                icon: 'water-normal'
              }
            ]
          });
        }
        if (this.activeTab == '取水监管') {
          this.setWaterOverLay();
          this.$emit('legend-change', '取水监管', {
            label: '取水监管',
            children: [
              {
                label: '正在取水',
                icon: 'icon-being-icon'
              },
              {
                label: '停止取水',
                icon: 'icon-stop-icon'
              },
              {
                label: '设备掉线',
                icon: 'icon-drop-icon'
              }
            ]
          });
        }
      } else {
        this.map.removeOverlayByName('targetOverlay');
        this.map.drawClusterPoint([], {}, 'isWater');
        this.map.drawClusterPoint([], {}, 'stopTheWater');
        this.map.drawClusterPoint([], {}, 'equipmentDrops');
        this.$emit('legend-change', this.activeTab, {});
      }
    },
    //节水指标落点
    setMapOverLay() {
      if (this.sldbtn) {
        this.watSavNormByYear().then((res) => {
          this.map.addSavingActionFixedOverlay(res, 'targetOverlay', this.activeTab);
        });
      }
    },
    // 取水监管落点
    async setWaterOverLay() {
      if (this.sldbtn) {
        let obj = {
          areaCode: '', //行政区划
          status: '' //运行状态
        };
        await waterResourceApi.wainMonitor(obj).then((res) => {
          if (res.code == 0) {
            let list = res.data;
            // 正在取水
            let isWaterList = [];
            let isWaterStyle = {
              scale: 0.7,
              anchor: [27, 30],
              anchorXUnits: 'pixels',
              src: require(`@/assets/images/Is-water.png`),
              srcs: require(`@/assets/images/Is-water-cluster.png`)
            };
            // 停止取水
            let stopTheWaterList = [];
            let stopTheWaterStyle = {
              scale: 0.7,
              anchor: [27, 30],
              anchorXUnits: 'pixels',
              src: require(`@/assets/images/Stop-the-water.png`),
              srcs: require(`@/assets/images/Stop-the-water-cluster.png`)
            };
            // 设备掉线
            let equipmentDropsList = [];
            let equipmentDropsStyle = {
              scale: 0.7,
              anchor: [27, 30],
              anchorXUnits: 'pixels',
              src: require(`@/assets/images/Equipment-docking.png`),
              srcs: require(`@/assets/images/Equipment-docking-cluster.png`)
            };
            list.forEach((item) => {
              let obj = {
                ...item,
                lat: Number(item.wainLat),
                lng: Number(item.wainLong),
                type: '取水监管',
                props: {
                  取水口名称: item.wainName,
                  '实时取水量（m³）': item.intake,
                  更新时间: item.tm
                }
              };
              if (item.status == '正在取水') {
                isWaterList.push(obj);
              } else if (item.status == '停止取水') {
                stopTheWaterList.push(obj);
              } else {
                equipmentDropsList.push(obj);
              }
            });
            this.map.drawClusterPoint(isWaterList, isWaterStyle, 'isWater');
            this.map.drawClusterPoint(stopTheWaterList, stopTheWaterStyle, 'stopTheWater');
            this.map.drawClusterPoint(equipmentDropsList, equipmentDropsStyle, 'equipmentDrops');
          }
        });
      }
    },
    // 地图落点
    handleMapNet(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        src: require('@/assets/images/shuiyuandi-ld-5.png'),
        srcs: require('@/assets/images/shuiyuandi-ld-juhe-5.png')
      };
      let pointList = this.reservoiList.map((point) => {
        return {
          name: point.resName,
          type: '水源保障',
          lng: point.lowLeftLong,
          lat: point.lowLeftLat,
          code: point.resCode,
          props: {}
        };
      });
      if (this[btnType]) {
        this.ScreenMap.drawClusterPoint(pointList, style, 'areaPointLayer5');
      } else {
        this.ScreenMap.drawClusterPoint([], {}, 'areaPointLayer5');
      }
    },
    // 当窗口缩放时，echart动态调整自身大小
    handleWindowResize() {
      this.waterUseConfig();
    }
  }
};
</script>
<style lang="scss" scoped>
@import './style/SildePanel.scss';
.water-saving-action-situation-card {
  width: 100%;
  height: 100%;
  .area-box,
  .is-water-box {
    width: 100%;
    height: 100%;
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
.charts-panel {
  height: 100%;
}
.panel-item__content--tabs,
.panel-item__title--tabs {
  .tab-button {
    cursor: pointer;
    user-select: none;
    height: 24px;
    line-height: 24px;
    padding: 0 5px;
    border-radius: 13px;
    color: #fff;
    font-size: 20px;
    &.active {
      padding: 2px 7px;
      // background: #00c1ff;
      background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
    }
  }
}
.panel-item__content--tabs {
  margin-top: 0px;
  margin-bottom: 6px;
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
</style>

<style lang="scss">
.water-saving-action-situation-card .panel-item .panel-item__content--tabs .tab-button,
.water-saving-action-situation-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.water-saving-action-situation-card .panel-item .panel-item__content--tabs .tab-button.active,
.water-saving-action-situation-card .panel-item .panel-item__title--tabs .tab-button.active {
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
  background: #328fe8;
}
.icon-color-2 {
  @include chart-icon();
  background: #e89100;
}
.icon-color-3 {
  @include chart-icon();
  background: #8a8ae1;
}
.icon-color-4 {
  @include chart-icon();
  background: #a80bee;
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
