<!--
 * @Author: dyn
 * @Date: 2021-07-12 11:43:29
 * @LastEditTime: 2022-09-23 14:10:24
 * @LastEditors: Please set LastEditors
 * @Description: 视频监控卡片
 * @FilePath: \lucheng\src\views\water_disaster_custom\cards\VideoCard.vue
-->
<template>
  <div class="video-card">
    <panel-item
      title="视频监控"
      has-switch
      tc
      @tclick="tclick"
      :action-tabs="['按工程类型', '按视频状态']"
      @change="handleTabChange"
    >
      <template #title-switch>
        <i
          title="地图落点"
          @click="handleVideo('spbtn')"
          :class="[spbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
        />
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('视频监控')"
          style="margin-left: 10px"
        />
        <!-- <i
					v-show="!previewModule"
					@click="goToAMap('视频')"
					style="margin-left: 10px"
					title="一张图"
					class="icon-yizhangtu icon-sty"
				/> -->
      </template>

      <!-- <div class="area-box" v-if="activeTab == 0">
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
					:data-zoom="barOption.dataZoom"
				/>
			</div> -->
      <div class="type-box" v-if="activeTab == 0">
        <div class="pie-box">
          <EchartPie :seriesData="typePieData" :extraOption="typePieOption"></EchartPie>
        </div>
        <div class="pie-legend">
          <div v-for="(item, index) in typePieLegend" :key="index">
            <span>
              <i class="legend-circle" :style="{ background: item.color }"></i>
              {{ item.label }}
            </span>
          </div>
        </div>
      </div>
      <div class="status-box" v-if="activeTab == 1">
        <div class="pie-box">
          <EchartPie :seriesData="pieData" :extraOption="pieOption"></EchartPie>
        </div>
        <div class="pie-legend">
          <div v-for="(item, index) in pieData" :key="index">
            <template v-if="item.name">
              <span>
                <i class="legend-circle" :style="{ background: item.color }"></i>
                {{ item.name }}
              </span>
              <span>
                <span class="legend-num">{{ item.value }}</span>
                个
              </span>
            </template>
          </div>
        </div>
      </div>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from 'vuex';
import PanelItem from '../components/PanelItem.vue';
import { disasterPreventionApi } from '@/api/RightSildePanel';
import EchartPie from '../../WaterDisasterPreventionScreen/charts/echart_pie/EchartPie.vue'; // 积水饼图
import { goAMap } from './utils/common';
export default {
  name: 'StagnantWaterCard',
  layerName: ['onlineLayer', 'outlineLayer'],
  legendName: '视频',
  components: {
    PanelItem,
    EchartPie
  },
  computed: {
    ...mapGetters(['previewModule']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  data() {
    return {
      spbtn: false, // 落点
      activeTab: 0,
      barData: {
        columns: ['name', 'total', 'online', 'outline'],
        rows: []
      },
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
            fontSize: this.$commonSetting.chartFontSize,
            fontWeight: 'bold',
            position: 'top',
            color: '#fff'
          }
        },
        extend: {
          'textStyle.color': '#fff',
          barWidth: 15,
          legend: {
            show: true,
            right: 0,
            top: 10,
            icon: 'circle',
            itemWidth: 8,
            itemHeight: 8,
            data: [
              {
                name: '在线',
                icon: 'circle',
                textStyle: {
                  color: '#1CFFF5' // 图例文字颜色
                }
              },
              {
                name: '离线',
                icon: 'circle',
                textStyle: {
                  color: '#009AFF' // 图例文字颜色
                }
              }
            ]
          },
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
      pieData: [],
      pieLegend: [
        {
          label: '在线',
          num: 96
        },
        {
          label: '离线',
          num: 3
        }
      ],
      pieOption: {
        tooltip: {
          show: true,
          trigger: 'item',
          formatter: function (data) {
            return data.data.name + ':' + '  ' + data.data.value + '个' + '  ' + '(' + data.data.scale + '%' + ')';
          },
          confine: true,
          extraCssText: 'font-size:18px;'
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
                show: true,
                position: 'center',
                color: '#fff',
                fontSize: 18,
                formatter: () => {
                  return this.videoTotal;
                }
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
                color: '#31B5FF' // 0% 处的颜色
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
                color: '#C6613F' // 0% 处的颜色
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
      videoTotal: 0,
      typeTotal: 0,
      typePieLegend: [
        {
          color: '#31B5FF',
          label: '水库'
        },
        {
          color: '#31FDFF',
          label: '水闸'
        },
        {
          color: '#64E14C',
          label: '山塘'
        },
        {
          color: '#DEFA50',
          label: '海塘'
        },
        {
          color: '#FFC518',
          label: '水电站'
        },
        {
          color: '#FF7618',
          label: '堤防'
        },
        {
          color: '#FF4018',
          label: '河道'
        },
        {
          color: '#FF1818',
          label: '泵站'
        },
        {
          color: '#E605B9',
          label: '灌区'
        },
        {
          color: '#A839FF',
          label: '农饮水'
        },
        {
          color: '#6740FF',
          label: '其他'
        }
      ],
      typePieData: [],
      typePieOption: {
        tooltip: {
          show: true,
          trigger: 'item',
          formatter: function (data) {
            return data.data.name + ':' + '  ' + data.data.value + '个' + '  ' + '(' + data.data.scale + '%' + ')';
          },
          confine: true,
          extraCssText: 'font-size:18px;'
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
                formatter: () => {
                  return this.typeTotal;
                }
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
                color: '#31B5FF' // 0% 处的颜色
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
      timer: null
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.setTypePieColor();
      // this._statsCameraByAdcd();
      this._statsCameraByProject();
      this._countCameraStatus();
      this._selectCamera();
      // this.timer = setInterval(()=>{
      //   this._statsCameraByAdcd();
      //   this._statsCameraByProject();
      //   this._countCameraStatus();
      // },300000);
    });
  },
  beforeDestroy() {
    // clearInterval(this.timer);
    this.map.drawClusterPoint([], {}, 'onlineLayer');
    this.map.drawClusterPoint([], {}, 'outlineLayer');
    this.$emit('legend-change', '视频', {});
  },
  methods: {
    ...mapActions(['SetDetailDialogParam']),
    goToAMap(type) {
      goAMap(type);
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
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
    },
    // 地图落点开关
    handleVideo(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/new_images/point/group-video-online.png'),
        src: require('@/assets/new_images/point/point-video-online.png')
      };
      let style1 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/new_images/point/group-video-offline.png'),
        src: require('@/assets/new_images/point/point-video-offline.png')
      };
      let onlineList = [];
      let outlineList = [];
      for (let point of this.pointList) {
        let obj = {
          name: point.cameraName,
          type: '视频',
          lng: Number(point.longitude),
          lat: Number(point.latitude),
          code: point.cameraIndexCode,
          previewUrl: point.previewUrl || '',
          cameraIndexCode: point.cameraIndexCode,
          cameraSource: point.cameraSource || '',
          stationCode: point.cameraIndexCode || ''
        };
        if (point.status == 1) {
          onlineList.push(obj);
        } else {
          outlineList.push(obj);
        }
      }
      if (this[btnType]) {
        this.map.drawClusterPoint(outlineList, style1, 'onlineLayer');
        this.map.drawClusterPoint(onlineList, style, 'outlineLayer');
        this.$emit('legend-change', '视频', {
          label: '视频',
          clusterLayer: ['onlineLayer', 'outlineLayer'],
          children: [
            {
              label: '在线',
              icon: 'icon-legend-video-online'
            },
            {
              label: '离线',
              icon: 'icon-legend-video-outline'
            }
          ]
        });
      } else {
        this.map.drawClusterPoint([], {}, 'onlineLayer');
        this.map.drawClusterPoint([], {}, 'outlineLayer');
        this.$emit('legend-change', '视频', {});
      }
    },
    handleTabChange(val) {
      this.activeTab = val;
    },
    // 按行政区划统计
    async _statsCameraByAdcd() {
      let res = await disasterPreventionApi.statsCameraByAdcd();
      let rows = [];
      for (let index in res.data.totalNum) {
        rows.push({
          name: res.data.totalNum[index].key,
          total: res.data.totalNum[index].value,
          online: res.data.onlineNum[index].value,
          outline: res.data.offlineNum[index].value
        });
      }
      this.barData.rows = rows;
      this.$refs['chart'].echarts.resize();
    },
    // 按工程统计摄像头
    async _statsCameraByProject() {
      let res = await disasterPreventionApi.statsCameraByProject();
      if (res.code == 0) {
        let rows = [];
        let total = res.data.totalNum.reduce((prev, cur) => {
          return prev + cur.value;
        }, 0);
        this.typePieLegend.forEach((element) => {
          let data = res.data.totalNum.find((item) => item.key == element.label);
          if (data) {
            rows.push({
              name: data.key,
              value: data.value,
              total: total,
              scale: data.value ? ((data.value / total) * 100).toFixed(2) : 0
            });
          } else {
            rows.push({
              name: element.label,
              value: 0,
              total: total,
              scale: 0
            });
          }
        });
        // for (let item of res.data.totalNum) {
        //   rows.push({
        //     name: item.key,
        //     value: item.value,
        //     total: total,
        //     scale: item.value ? ((item.value / total) * 100).toFixed(2) : 0
        //   });
        // }
        this.typePieData = rows;
        this.typeTotal = total;
      }
    },
    // 统计摄像头在线离线状态
    async _countCameraStatus() {
      let res = await disasterPreventionApi.countCameraStatus();
      let rows = [];
      for (let item of res.data) {
        rows.push({
          name: item.status == 0 ? '离线' : '在线',
          value: item.num,
          total: item.total,
          scale: item.num ? ((item.num / item.total) * 100).toFixed(2) : 0,
          color: item.status == 0 ? '#C6613F' : '#31B5FF'
        });
      }
      rows.push({
        name: '',
        value: 0
      });
      this.pieData = rows;
      this.videoTotal = res.data[0].total;
    },
    // 查询所有摄像头
    async _selectCamera() {
      let res = await disasterPreventionApi.selectCamera();
      this.map.drawClusterPoint([], {}, 'onlineLayer');
      this.map.drawClusterPoint([], {}, 'outlineLayer');
      this.pointList = res.data;
    },

    setTypePieColor() {
      let arr = [];
      this.typePieLegend.map((item) => {
        arr.push({
          type: 'linear',
          x: 0,
          y: 0,
          x2: 1,
          y2: 1,
          colorStops: [
            {
              offset: 0,
              color: item.color // 0% 处的颜色
            },
            {
              offset: 1,
              color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
            }
          ],
          global: false // 缺省为 false
        });
      });
      this.typePieOption.color = arr;
    },
    barConfig(options) {
      const onlineData = options.series[1].data;
      const outlineData = options.series[2].data;
      let colorArr = ['#15ddd7', '#7dfefa', '#9dfffc'];
      let colorArr2 = ['#2886c6', '#50bfda', '#89e3ec'];
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
      let barWidth = 20;
      let constData = [];
      let showData = [];
      // 在线柱状图
      constData = onlineData.map((item) => (item ? 1 : 0));
      showData = onlineData.map((item) =>
        item
          ? item
          : {
              value: 1,
              itemStyle: {
                normal: {
                  borderColor: 'rgba(0,0,0,0)',
                  borderWidth: 2,
                  color: 'rgba(0,0,0,0)'
                }
              }
            }
      );
      const onlineSeries = [
        {
          z: 1,
          name: '在线',
          type: 'bar',
          barWidth: barWidth,
          barGap: '0%',
          data: onlineData,
          itemStyle: {
            normal: {
              color: color
            }
          },
          label: {
            show: true,
            fontSize: 18,
            position: 'top',
            color: '#1EF8F2 '
          }
        },
        {
          z: 2,
          name: '在线',
          type: 'pictorialBar',
          data: constData,
          symbol: 'diamond',
          symbolOffset: [(-1 * barWidth) / 2, '50%'],
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
          name: '在线',
          type: 'pictorialBar',
          symbolPosition: 'end',
          data: showData,
          symbol: 'diamond',
          symbolOffset: [(-1 * barWidth) / 2, '-50%'],
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
      // 离线柱状图
      constData = outlineData.map((item) => (item ? 1 : 0));
      showData = outlineData.map((item) =>
        item
          ? item
          : {
              value: 1,
              itemStyle: {
                normal: {
                  borderColor: 'rgba(0,0,0,0)',
                  borderWidth: 2,
                  color: 'rgba(0,0,0,0)'
                }
              }
            }
      );
      const outlineSeries = [
        {
          z: 1,
          name: '离线',
          type: 'bar',
          barWidth: barWidth,
          barGap: '0%',
          data: outlineData,
          itemStyle: {
            normal: {
              color: color2
            }
          },
          label: {
            show: true,
            fontSize: 18,
            position: 'top',
            color: '#00C1FF'
          }
        },
        {
          z: 2,
          name: '离线',
          type: 'pictorialBar',
          data: constData,
          symbol: 'diamond',
          symbolOffset: [barWidth / 2, '50%'],
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
          name: '离线',
          type: 'pictorialBar',
          symbolPosition: 'end',
          data: showData,
          symbol: 'diamond',
          symbolOffset: [barWidth / 2, '-50%'],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: color2[2],
              borderWidth: 2,
              color: color2[2]
            }
          },
          tooltip: {
            show: false
          }
        }
      ];
      options.series = [...onlineSeries, ...outlineSeries];
      return options;
    }
  }
};
</script>
<style lang="scss" scoped>
.video-card {
  width: 100%;
  height: 100%;
  .area-box,
  .type-box,
  .status-box {
    width: 100%;
    height: calc(100% - 34px);
  }
  .type-box,
  .status-box {
    display: flex;
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
    .legend-circle {
      display: inline-block;
      height: 14px;
      width: 14px;
      border-radius: 50%;
      background: #1cfff5;
      vertical-align: middle;
      margin-right: 10px;
    }
  }
  .type-box {
    .pie-box {
      width: 40%;
      height: 100%;
    }
    .pie-legend {
      flex-wrap: wrap;
      justify-content: flex-start;
      > div {
        line-height: 26px;
      }
    }
  }
  .status-box {
    .pie-legend {
      > div {
        display: flex;
        justify-content: space-between;
        line-height: 30px;

        &:last-child .legend-circle {
          background: #ff7144;
        }
        .legend-num {
          font-size: 20px;
          color: #1cfff5;
          margin-right: 7px;
        }
      }
    }
  }
}
</style>

<style lang="scss">
.video-card .panel-item .panel-item__content--tabs .tab-button,
.video-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.video-card .panel-item .panel-item__content--tabs .tab-button.active,
.video-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
