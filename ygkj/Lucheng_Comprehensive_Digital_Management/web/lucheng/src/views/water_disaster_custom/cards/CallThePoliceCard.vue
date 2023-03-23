<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2022-09-22 15:31:06
 * @LastEditors: Please set LastEditors
 * @Description: 报警卡片
 * @FilePath: \lucheng\src\views\water_disaster_custom\cards\CallThePoliceCard.vue
-->
<template>
  <div class="stagnant-water-card">
    <panel-item title="海塘报警" has-switch tc @tclick="tclick('报警')">
      <template #title-switch>
        <i title="地图落点" @click="handleSeawallChange('policebtn')" :class="[policebtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']" style="margin-right: 5px" />
        <i title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog('报警', '1')" style="margin-right: 5px" />
      </template>
      <div class="area-box" v-show="!showSeawallDetail">
        <CapsuleTabs :tabs="actionTabList_a" @change="switchTabs" />
        <div class="chart-box">
          <vchart-bar v-if="
              this.actionTabList_a[this.activeTab] == '实时报警' || this.actionTabList_a[this.activeTab] == '预报预警'
            " height="100%" width="100%" ref="ranksChart" :barWidth='30' :barData="barData" :colorArr="ranksColor" :labelColor="ranksLabelColor" :barName="ranksBarName" :unit="rankUnit" @barClick="clickBarData"></vchart-bar>
          <EchartPie v-if="this.actionTabList_a[this.activeTab] == '运行报警'" width="80%" height="80%" ref="sub_pie" :extraOption="pieOption" :seriesData="pieData" @clickItem="clickSeawallPieData"></EchartPie>
        </div>
      </div>
      <CapsuleTabs v-show="showSeawallDetail" :tabs="['返回']" @change="showSeawallDetail = false" />
      <div class="is-water-box" v-if="showSeawallDetail">
        <primary-map-table :columnList="tableColumns" :dataList="dataSeawallList" :paginationFlag="false"></primary-map-table>
      </div>
    </panel-item>
    <div>
      <panel-item title="水雨情报警" has-switch tc @tclick="tclick('报警')">
        <template #title-switch>
          <i title="地图落点" @click="handleStagnantWatchRain('watchRain')" :class="[watchRain == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']" style="margin-right: 5px" />
          <i title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog('报警', '2')" style="margin-right: 5px" />
        </template>
        <div class="area-box" v-show="!showDetail">
          <CapsuleTabs :tabs="actionTabList_b" @change="switchWatchTabs" />
          <div class="chart-box">
            <EchartPie width="80%" height="80%" ref="sub_pie" :extraOption="pieWaterRainOption" :seriesData="pieWaterRainData" @clickItem="clickPieData"></EchartPie>
          </div>
        </div>
        <CapsuleTabs v-if="showDetail" :tabs="['返回']" @change="BackHistogram" />
        <div class="is-water-box" v-if="showDetail">
          <primary-map-table :columnList="waterRainTableColumns" :dataList="waterRainDataType" :paginationFlag="false"></primary-map-table>
        </div>
      </panel-item>
    </div>
    <div>
      <panel-item title="台风报警" has-switch tc @tclick="tclick('报警')">
        <template #title-switch>
          <i title="地图落点" @click="handleStagnantTyphoon('typhoon')" :class="[typhoon == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']" style="margin-right: 5px" />
          <i title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog('报警', '3')" style="margin-right: 5px" />
        </template>
        <div class="is-water-box typhoon-table">
          <primary-map-table :columnList="typhoonTableColumns" :dataList="typhoonData" :paginationFlag="false" @rowClick="rowClickTyphoon"></primary-map-table>
        </div>
      </panel-item>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import PanelItem from '../components/PanelItem.vue';
import CapsuleTabs from '@/views/WaterDisasterPreventionScreen/components/CapsuleTabs.vue';
import { PrimaryMapTable } from '@/components/';
import { alarmPeople } from '@/api/bigScreenPart';
import { cloneObj } from '@/utils/utils';
import VchartBar from '../charts/vchart_3dbar/VchartBar.vue';
import EchartPie from '../charts/echart_pie/EchartPie.vue';
export default {
  name: 'CallThePoliceCard',
  layerName: [
    'runReservoirNormalLayer',
    'runSeawallNormalLayer',
    'runWeirgateNormalLayer',
    'runRiverNormalLayer',
    'runTyphoonNormalLayer',
    'runTideNormalLayer',
    'runTainfallNormalLayer',
    'runTeservoirWarnLayer',
    'runSeawallWarnLayer',
    'runWeirgateWarnLayer',
    'runRiverWarnLayer',
    'runTyphoonWarnLayer',
    'runRainfallWarnLayer',
    'runTideWarnLayer'
  ],
  legendName: ['河道', '堰闸', '台风', '水库', '潮位', '海塘', '降雨'],
  components: {
    PanelItem,
    CapsuleTabs,
    VchartBar,
    EchartPie,
    PrimaryMapTable
  },
  computed: {
    ...mapGetters(['previewModule']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  data() {
    return {
      multipleSelection: [],
      showSeawallDetail: false,
      policebtn: false, // 落点
      activeTab: 0, // ['海塘报警', '海塘预警', '降雨报警', '水情报警', '台风报警', '运行报警']"
      pointList: [],
      ranksLabelColor: '#1EF8F2',
      ranksBarName: '',
      ranksColor: ['#15E2DB', '#9DFDFB', '#A6FFFF'],
      barData: {
        columns: ['name', 'data'],
        rows: []
      },
      rankUnit: '个',
      actionTabList_a: ['实时报警', '预报预警', '运行报警'],
      actionTabList_b: ['水情报警', '降雨报警'],
      actionTabList_c: ['台风报警'],
      actionTabList: ['实时报警', '预报预警', '运行报警', '水情报警', '降雨报警', '台风报警'],
      barOption: {
        dataZoom: {
          type: 'inside',
          startValue: 0,
          endValue: 4
        },
        grid: {
          top: 40,
          bottom: '1%',
          containLabel: true
        },
        setting: {
          yAxisName: ['个'],
          itemStyle: { color: '#fff' },
          label: {
            show: true,
            fontSize: 12,
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
              fontSize: 12
            }
          },
          yAxis: {
            nameTextStyle: {
              color: '#ffffff99',
              fontSize: 12
            },
            axisLine: {
              show: false
            },
            axisLabel: {
              color: '#fff',
              fontSize: 12
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
      pieData: [],
      ExtraOption: {
        tooltip: {
          show: true,
          trigger: 'item',
          formatter: function (data) {
            return data.data.name + ':' + '  ' + data.data.value + '个' + '  ' + '(' + data.data.scale + '%' + ')';
          },
          confine: true,
          extraCssText: 'width:160px;height:50px;font-size:18px;'
        },
        series: [
          {
            type: 'pie',
            radius: ['60%', '90%'],
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
                formatter: 3027 + '\n' + '总数' // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
              }
            },
            labelLine: {
              show: false
            }
          }
        ],
        color: [
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: '#1CFFF5' // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          },
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: '#EEC80B' // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          },
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: '#FF7144' // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          }
        ]
      },
      //饼图
      pieOption: {
        title: {
          text: '0个\n总数',
          textStyle: {
            color: '#fff',
            fontSize: 16
          },
          left: 'center',
          top: 'middle'
        },
        tooltip: {
          show: true,
          trigger: 'item',
          formatter: function (data) {
            return data.data.name + ':' + '  ' + data.data.value + '个';
          },
          confine: true,
          extraCssText: 'font-size:18px;'
        },
        series: [
          {
            type: 'pie',
            radius: ['35%', '60%'],
            avoidLabelOverlap: false,
            label: {
              show: true,
              position: 'outside',
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: 'outside',
                color: '#fff',
                fontSize: 14,
                formatter: `{b}\n{c}个` // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
              }
            },
            labelLine: {
              show: true
            }
          }
        ],
        color: [
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: '#1CFFF5' // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          },
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: '#EEC80B' // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          },
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: '#A80BEE' // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          },
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: '#19AF47' // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          }
        ]
      },
      dataList: [],
      dataSeawallList: [],
      tableColumns: [],
      timer: null,
      showDetail: false,
      areaGeometryArr: [],
      typhoonTableColumns: [
        {
          prop: 'name',
          label: '名称'
        },
        {
          prop: 'waterLevel',
          label: '风力'
        },
        {
          prop: 'limitLevel',
          label: '距离鹿城(km)'
        }
      ], //台风
      typhoonData: [], //台风
      typhoon: false, //台风
      waterRainTableColumns: [], //水雨情
      waterRainData: [], //水雨情
      waterRainDataType: [],
      pieWaterRainData: [], //水雨情
      pieWaterRainOption: {
        title: {
          text: '0个\n总数',
          textStyle: {
            color: '#fff',
            fontSize: 16
          },
          left: 'center',
          top: 'middle'
        },
        tooltip: {
          show: true,
          trigger: 'item',
          formatter: function (data) {
            return data.data.name + ':' + '  ' + data.data.value + '个';
          },
          confine: true,
          extraCssText: 'font-size:18px;'
        },
        series: [
          {
            type: 'pie',
            radius: ['35%', '60%'],
            avoidLabelOverlap: false,
            label: {
              show: true,
              position: 'outside',
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: 'outside',
                color: '#fff',
                fontSize: 14,
                formatter: `{b}\n{c}个` // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
              }
            },
            labelLine: {
              show: true
            }
          }
        ],
        color: [
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: '#1CFFF5' // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          },
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: '#EEC80B' // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          },
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: '#A80BEE' // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          },
          {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: '#19AF47' // 0% 处的颜色
              },
              {
                offset: 1,
                color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          }
        ]
      }, //水雨情
      watchRain: false,
      watchRainTab: 0,
      legendOptions: [
        {
          value: 'reservoir',
          name: '水库',
          mark: 'wr'
        },
        {
          value: 'tide',
          name: '潮位',
          mark: 'wr'
        },
        {
          value: 'river',
          name: '河道',
          mark: 'wr'
        },
        {
          value: 'weirgate',
          name: '堰闸',
          mark: 'wr'
        }
      ]
    };
  },
  mounted() {
    this.getPoliceList('实时报警');
    this.typhoonList();
    this.waterRainfallRegime('水情报警');
    this.timer = setInterval(() => {
      this.getPoliceList('实时报警');
      this.typhoonList();
      this.waterRainfallRegime('水情报警');
    }, 300000);
    this.legendOptions.map((i) => {
      i.isShowPoint = false;
      if (i.name == this.legendOptions[this.activeTab].name) {
        i.isShowPoint = true;
      }
    });
    // this.$refs.chart.echarts.on('click', function(params) {
    //   this.histogram(params.name);
    // });
  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.legendOptions.map((item) => {
      this.setLegend(item.name);
      this.setPoint([], item.value, 'normal', false);
      this.setPoint([], item.value, 'warn', false);
    });
    this.map.clearAllTyphoon();
    this.map.removeLineLayer();
    this.map.removeFocusLineLayer();
    this.$emit('legend-change', '海塘', {});
    this.map.drawClusterPoint([], {}, `dryBrakeLayer`);
    this.map.drawClusterPoint([], {}, `wharfLayer`);
    this.map.drawClusterPoint([], {}, `intersectLayer`);
    this.map.drawClusterPoint([], {}, `bridgeLayer`);
    this.map.drawClusterPoint([], {}, `stakeLayer`);
    this.map.drawClusterPoint([], {}, `culvertLayer`);
    this.map.drawClusterPoint([], {}, `sluiceLayer`);
    this.map.drawClusterPoint([], {}, `pumpLayer`);
    // this.map.drawClusterPoint([], {}, `pipeLayer`);
    this.map.drawClusterPoint([], {}, `rainbowLayer`);
    this.map.drawClusterPoint([], {}, `roomLayer`);
    this.map.drawClusterPoint([], {}, `stationLayer`);
    this.map.drawClusterPoint([], {}, `watergateLayer`);
  },
  methods: {
    ...mapActions(['SetDetailDialogParam']),
    normalization(value, max, min) {
      let normalizationRatio = (value - min) / (max - min);
      return normalizationRatio;
    },
    //海塘tab切换
    async switchTabs(val) {
      this.activeTab = val;
      console.log(this.actionTabList_a[val]);
      await this.getPoliceList(this.actionTabList_a[val]);
      this.map.removeLineLayer();
      this.map.removeFocusLineLayer();
      this.$emit('legend-change', '海塘', {});
      if (this.policebtn) {
        this.seawallHand();
      }
    },
    //水雨情落点处理
    async getRightMapList(arr, type) {
      let warnList = [];
      let normalList = [];
      arr.map((i) => {
        if (i.warning == false) {
          normalList.push(i);
        } else {
          warnList.push(i);
        }
      });
      this.setPoint(warnList, type, 'warn', true);
      this.setPoint(normalList, type, 'normal', true);
    },
    //水情落点
    watchRainPoint() {
      if (this.watchRain) {
        let dataList_reservoir = []; //水库
        let dataList_riverway = []; //河道
        let dataList_weir_gate = []; //堰闸
        let dataList_sea_level = []; //潮位
        this.waterRainData.map((item) => {
          let obj = {
            name: item.stName,
            lat: item.stLat,
            lng: item.stLong,
            code: item.stCode,
            warning: item.warning,
            stType: item.stType,
            type: '水情',
            props: {
              名称: item.stName,
              水位: item.waterLevel,
              更新时间: item.time
            }
          };
          // ['水库','河道', '堰闸','潮位',],
          if (item.stType == 'RR') {
            dataList_reservoir.push(obj);
          } else if (item.stType == 'ZZ') {
            dataList_riverway.push(obj);
          } else if (item.stType == 'TT') {
            dataList_sea_level.push(obj);
          } else {
            dataList_weir_gate.push(obj);
          }
        });
        this.getRightMapList(dataList_reservoir, 'reservoir');
        this.getRightMapList(dataList_riverway, 'river');
        this.getRightMapList(dataList_weir_gate, 'weirgate');
        this.getRightMapList(dataList_sea_level, 'tide');
        this.legendOptions.map((item) => {
          this.setLegend(item.name, item.mark);
        });
      } else {
        this.legendOptions.map((item) => {
          this.setLegend(item.name);
          this.setPoint([], item.value, 'normal', false);
          this.setPoint([], item.value, 'warn', false);
        });
      }
    },
    //水雨情落点
    handleStagnantWatchRain() {
      this.watchRain = !this.watchRain;
      if (this.watchRain) {
        if (this.watchRainTab == 0) {
          this.watchRainPoint();
        } else {
          this.loadRainfallPoint();
        }
      } else {
        this.watchRainPoint();
        this.loadRainfallPoint();
      }
    },
    //降雨落点
    loadRainfallPoint() {
      if (this.watchRain) {
        let arrData = this.waterRainData.map((item) => {
          return {
            longitude: item.st_long,
            latitude: item.st_lat,
            stationName: item.stName,
            mgstcd: item.stcd,
            drp: item.drp == '-1' ? '-' : item.drp,
            stLoc: item.st_loc
          };
        });
        this.map.loadRainfallPoint(arrData, true);
        this.$emit('legend-change', '预报降雨量', {});
        this.$emit('legend-change', '实时降雨量', {
          label: '实时降雨量',
          children: [
            { label: '>250', icon: 'realtime-250' },
            { label: '100~250', icon: 'realtime-100' },
            { label: '50~100', icon: 'realtime-50' },
            { label: '25~50', icon: 'realtime-25' },
            { label: '10~25', icon: 'realtime-10' },
            { label: '0.1~10', icon: 'realtime-0_1' }
          ]
        });
      } else {
        this.map.loadRainfallPoint([], false);
        this.$emit('legend-change', '实时降雨量', {});
      }
    },
    //海塘地图点位
    handleSeawallChange() {
      this.policebtn = !this.policebtn;
      if (this.policebtn) {
        this.seawallHand();
      } else {
        this.map.removeLineLayer();
        this.map.removeFocusLineLayer();
        this.$emit('legend-change', '海塘', {});
        this.map.drawClusterPoint([], {}, `dryBrakeLayer`);
        this.map.drawClusterPoint([], {}, `wharfLayer`);
        this.map.drawClusterPoint([], {}, `intersectLayer`);
        this.map.drawClusterPoint([], {}, `bridgeLayer`);
        this.map.drawClusterPoint([], {}, `stakeLayer`);
        this.map.drawClusterPoint([], {}, `culvertLayer`);
        this.map.drawClusterPoint([], {}, `sluiceLayer`);
        this.map.drawClusterPoint([], {}, `pumpLayer`);
        // this.map.drawClusterPoint([], {}, `pipeLayer`);
        this.map.drawClusterPoint([], {}, `rainbowLayer`);
        this.map.drawClusterPoint([], {}, `roomLayer`);
        this.map.drawClusterPoint([], {}, `stationLayer`);
        this.map.drawClusterPoint([], {}, `watergateLayer`);
      }
    },
    seawallHand() {
      for (let i = 0; i < this.dataList.length; i++) {
        let seawall = this.dataList[i];
        let color = '#1CFFF5';
        if (this.activeTab != 2) {
          if (seawall.status == 1) {
            color = '#1CFFF5';
          } else if (seawall.status == 2) {
            color = '#008FE8';
          } else if (seawall.status == 3) {
            color = '#E99100';
          } else if (seawall.status == 4) {
            color = '#FF551F';
          } else if (seawall.status == 5) {
            color = '#E80000';
          }
        } else {
          if (seawall.context) {
            color = '#E99100';
          }
        }
        let style = {
          lineWidth: 6,
          lineColor: color
        };
        let lineString = (seawall.coorpot && seawall.coorpot.replace(/&quot;/g, "'")) || '[]';
        let lineList = lineString && eval(lineString);
        lineList = lineList.map((line) => {
          line = line.map((item) => {
            return [item.lng, item.lat];
          });
          return {
            lineList: line,
            data: {
              type: '海塘',
              name: seawall.seawallName,
              code: seawall.seawallCode,
              props: {
                海塘长度: seawall.seawallLength + 'm',
                防潮标准: `${seawall.preventTideStandard}年`,
                所在位置: seawall.village
              },
              ...seawall
            }
          };
        });
        if (this.activeTab != 2) {
          this.$emit('legend-change', '海塘', {
            label: '海塘',
            children: [
              {
                label: '正常',
                icon: 'seawall-type-101'
              },
              {
                label: '超蓝色警戒',
                icon: 'seawall-type-102'
              },
              {
                label: '超黄色警戒',
                icon: 'seawall-type-20'
              },
              {
                label: '超橙色警戒',
                icon: 'seawall-type-103'
              },
              {
                label: '超红色警戒',
                icon: 'seawall-type-1'
              }
            ]
          });
        } else {
          this.$emit('legend-change', '海塘', {
            label: '海塘',
            children: [
              {
                label: '正常',
                icon: 'seawall-type-101'
              },
              {
                label: '报警',
                icon: 'seawall-type-20'
              }
            ]
          });
        }
        this.map.drawLineEx(lineList, style);
      }
    },
    // 海塘
    async getPoliceList(type) {
      let res;
      if (type == '实时报警') {
        res = await alarmPeople.seawallGiveAnAlarm();
        this.tableColumns = [
          {
            prop: 'seawallName',
            label: '名称'
          },
          {
            prop: 'tdz',
            label: '水位',
            sortable: true
          }
        ];
        if (res.code == 0) {
          this.dataList = res.data.list;
          this.barData.rows = [
            { name: '正常', data: res.data.zc },
            { name: '超蓝色', data: res.data.blue },
            { name: '超黄色', data: res.data.yellow },
            { name: '超橙色', data: res.data.orange },
            { name: '超红色', data: res.data.red }
          ];
          this.$nextTick(() => {
            this.$refs['ranksChart'].resize();
          });
        }
      } else if (type == '预报预警') {
        res = await alarmPeople.seawallWarning();
        this.tableColumns = [
          {
            prop: 'seawallName',
            label: '名称',
            width: 100
          },
          {
            prop: 'tdz',
            label: '水位',
            sortable: true
          }
        ];
        if (res.code == 0) {
          this.dataList = res.data.list;
          this.barData.rows = [
            { name: '正常', data: res.data.zc },
            { name: '超蓝色', data: res.data.blue },
            { name: '超黄色', data: res.data.yellow },
            { name: '超橙色', data: res.data.orange },
            { name: '超红色', data: res.data.red }
          ];
          this.$nextTick(() => {
            this.$refs['ranksChart'].resize();
          });
        }
      } else {
        res = await alarmPeople.runGiveAnAlarm();
        this.tableColumns = [
          {
            prop: 'seawallName',
            label: '名称',
            width: 100
          },
          {
            prop: 'context',
            label: '报警内容',
            sortable: true
          }
        ];
        if (res.code == 0) {
          this.dataList = res.data.list;
          this.pieData = [
            { name: '正常', value: res.data.zc },
            { name: '报警', value: res.data.bj }
          ];
          let totalNum = res.data.zc + res.data.bj;
          this.pieOption.title.text = `${totalNum}个\n总数`;
        }
      }
    },
    //台风预警
    async typhoonList() {
      let res = await alarmPeople.typhoonGiveAnAlarm();
      if (res.code == 0) {
        if (res.data.list.length > 0) {
          this.typhoonData = res.data.list.map((item) => {
            return {
              name: item.name,
              limitLevel: item.points[item.points.length - 1].distance,
              waterLevel: item.points[item.points.length - 1].power,
              data: item
            };
          });
        }
        // this.typhoonData = res.data.list;
      } else {
        this.typhoonData = res.data.list;
      }
    },
    //水雨情
    async waterRainfallRegime(type) {
      let res;
      if (type == '水情报警') {
        res = await alarmPeople.waterGiveAnAlarm();
        this.waterRainTableColumns = [
          {
            prop: 'stName',
            label: '名称',
            width: 100
          },
          {
            prop: 'waterLevel',
            label: '水位',
            sortable: true
          },
          {
            prop: 'limitLevel',
            label: '汛限水位',
            sortable: true
          }
        ];
        if (res.code == 0) {
          this.waterRainData = res.data.list;
          this.pieWaterRainData = [
            { name: '水库', value: res.data.rrCount },
            { name: '河道', value: res.data.zzCount },
            { name: '堰闸', value: res.data.ddCount },
            { name: '潮位', value: res.data.ttCount }
          ];
          let totalNum = res.data.rrCount + res.data.zzCount + res.data.ddCount + res.data.ttCount;
          this.pieWaterRainOption.title.text = `${totalNum}个\n总数`;
        }
      } else {
        res = await alarmPeople.rainGiveAnAlarm();
        this.waterRainTableColumns = [
          {
            prop: 'stName',
            label: '名称',
            width: 100
          },
          {
            prop: 'drp',
            label: '雨量',
            sortable: true
          }
        ];
        if (res.code == 0) {
          this.waterRainData = res.data.list;
          this.pieWaterRainData = [
            { name: '正常', value: res.data.zc },
            { name: '1小时超30mm', value: res.data.oneHour },
            { name: '3小时超60mm', value: res.data.threeHours }
          ];
          let totalNum = res.data.zc + res.data.oneHour + res.data.threeHours;
          this.pieWaterRainOption.title.text = `${totalNum}个\n总数`;
        }
      }
    },
    //水雨情切换
    async switchWatchTabs(val) {
      this.watchRainTab = val;
      await this.waterRainfallRegime(this.actionTabList_b[val]);
      if (this.watchRain) {
        if (this.watchRainTab == 0) {
          this.watchRainPoint();
          this.map.loadRainfallPoint([], false);
          this.$emit('legend-change', '实时降雨量', {});
        } else {
          this.loadRainfallPoint();
          this.legendOptions.map((item) => {
            this.setLegend(item.name);
            this.setPoint([], item.value, 'normal', false);
            this.setPoint([], item.value, 'warn', false);
          });
        }
      }
    },
    //台风地图落点
    handleStagnantTyphoon(btnType) {
      this[btnType] = !this[btnType];
      this.map.clearAllTyphoon();
      if (this[btnType]) {
        this.typhoonData.map((item) => {
          let points = eval(item.data.points);
          const data = {
            ...item.data,
            points
          };
          this.map.showTyphoon(data);
        });
      } else {
        this.map.clearAllTyphoon();
      }
    },
    // pie图点击事件
    clickPieData(params) {
      this.showDetail = true;
      this.$nextTick(() => {
        switch (params.data.name) {
          case '水库':
            this.waterRainDataType = this.waterRainData.filter((i) => i.stType == 'RR');
            break;
          case '河道':
            this.waterRainDataType = this.waterRainData.filter((i) => i.stType == 'ZZ');
            break;
          case '堰闸':
            this.waterRainDataType = this.waterRainData.filter((i) => i.stType == 'DD');
            break;
          case '潮位':
            this.waterRainDataType = this.waterRainData.filter((i) => i.stType == 'TT');
            break;
          case '1小时超30mm':
            this.waterRainDataType = this.waterRainData.filter((i) => i.status == 1);
            break;
          case '3小时超60mm':
            this.waterRainDataType = this.waterRainData.filter((i) => i.status == 2);
            break;
          case '正常':
            this.waterRainDataType = this.waterRainData.filter((i) => i.status == 0);
            break;
        }
      });
    },
    clickSeawallPieData(params) {
      this.showSeawallDetail = true;
      this.dataSeawallList = [];
      if (params.data.name == '正常') {
        for (let i = 0; i < this.dataList.length; i++) {
          if (!this.dataList[i].context) {
            this.dataSeawallList.push(this.dataList[i]);
          }
        }
      } else {
        for (let i = 0; i < this.dataList.length; i++) {
          if (this.dataList[i].context) {
            this.dataSeawallList.push(this.dataList[i]);
          }
        }
      }
    },
    // 柱形图图点击事件
    clickBarData(params) {
      this.showSeawallDetail = true;
      this.dataSeawallList = [];
      let num = params + 1;
      for (let i = 0; i < this.dataList.length; i++) {
        if (num == this.dataList[i].status) {
          this.dataSeawallList.push(this.dataList[i]);
        }
      }
    },
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
      return options;
    },
    // 表格点击  单个项目
    rowClick(data) {
      this.$parent.$emit('showDialog', data);
    },
    histogram(name) {
      if (name) {
        this.showDetail = false;
        this.$nextTick(() => {
          this.showDetail = true;
          this.actionTabList = [];
        });
      }
    },
    // 返回柱形图
    BackHistogram(val) {
      this.showDetail = true;
      this.$nextTick(() => {
        this.showDetail = false;
      });
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val, num) {
      this.SetDetailDialogParam({
        mediumSized: num,
        codeTpye: val,
        analyseDialogShow: true
      });
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
    },
    rowClickTyphoon(row) {
      this.map.clearAllTyphoon();
      let points = eval(row.data.points);
      const data = {
        ...row.data,
        points
      };
      this.map.showTyphoon(data);
    },
    /**
     * @description: 设置图例
     * @param {String} name 显示图例名称cn
     * @param {String} value 工程类别en
     * @return {*}
     */
    setLegend(name, value) {
      if (value) {
        this.$emit('legend-change', name, {
          label: name,
          children: [
            {
              label: '正常',
              icon: 'icon-kaiqi'
            },
            {
              label: '超警',
              icon: 'icon-guanbi'
            }
          ]
        });
      } else {
        this.$emit('legend-change', name, {});
      }
    },
    /**
      * @function: 设置点位样式
      * @description: 
      * @param {*} projectType 点位类型
      * @param {*} value 点位图标后缀
      * @return {*}
    */
    getPointStyle(projectType, value) {
      let defaultStyle = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        srcs: null,
        src: null
      };
      let styleObj = cloneObj(defaultStyle);
      styleObj.src = require(`@/assets/new_images/point/point-${projectType}-${value}.png`);
      styleObj.srcs = require(`@/assets/new_images/point/group-${projectType}-${value}.png`);
      return styleObj;
    },
    /**
      * @function:  
      * @description: 设置点位
      * @param {*} pointList 点位列表
      * @param {*} type  类型
      * @param {*} status  状态，配合类型用以设置图层名
      * @param {*} flag 是否设置样式
      * @return {*}
    */
    setPoint(pointList, type, status, flag) {
      let pointStyle = flag ? this.getPointStyle(type, status) : {};
      let layer = type + status;
      this.map.drawClusterPoint(pointList, pointStyle, `${layer}Layer`);
    },
  }
};
</script>
<style lang="scss" scoped>
.stagnant-water-card {
  width: 100%;
  height: 100%;
  > div {
    height: 33.333%;
  }
  .area-box,
  .is-water-box {
    width: 100%;
    height: calc(100% - 34px);
  }
  .chart-box {
    height: calc(100% - 14px);
  }
  .is-water-box {
    display: flex;
  }
  .typhoon-table {
    margin-top: 10px;
  }
  .pie-box {
    width: 40%;
    height: 100%;
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
}
</style>

<style lang="scss">
.stagnant-water-card .panel-item .panel-item__content--tabs .tab-button,
.stagnant-water-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.stagnant-water-card .panel-item .panel-item__content--tabs .tab-button.active,
.stagnant-water-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
