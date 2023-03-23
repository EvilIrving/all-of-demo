<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2022-09-23 13:57:21
 * @LastEditors: Please set LastEditors
 * @Description: 积水卡片
 * @FilePath: \lucheng\src\views\water_disaster_custom\cards\StagnantWaterCard.vue
-->
<template>
  <div class="stagnant-water-card">
    <panel-item title="救援仓库" has-switch tc>
      <template #title-switch>
        <i title="地图落点" style="margin-right: 5px" @click="handleStagnantWater('jsbtn')" :class="[jsbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']" />
        <i v-show="!previewModule" title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog('救援仓库')" style="margin-right: 5px" />
        <!-- <i
          v-show="!previewModule"
          @click="goToAMap('积水')"
          style="margin-left: 10px"
          title="一张图"
          class="icon-yizhangtu icon-sty"
        /> -->
      </template>

      <div class="area-box" v-show="activeTab == 0">
        <ve-histogram ref="chart" height="100%" width="100%" :data="barData" :settings="barOption.setting" :extend="barOption.extend" :grid="barOption.grid" :data-zoom="barOption.dataZoom" :tooltip-visible="false" :legend-visible="false" :after-config="barConfig" :events='chartEvents' />
      </div>
      <div class="is-water-box" v-show="activeTab == 1">
        <CapsuleTabs :tabs="['返回']" @change="handleTabChange" />
        <div class="table-list">
          <primary-map-table :columnList="tableColumns" :dataList="dataList" :paginationFlag="false"></primary-map-table>
        </div>
      </div>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from 'vuex';
import { getWaterWarehouses, getWaterWarehousesCount } from '@/api/flow';
import CapsuleTabs from '@/views/WaterDisasterPreventionScreen/components/CapsuleTabs.vue';
import { PrimaryMapTable } from '@/components/';
import PanelItem from '../components/PanelItem.vue';
import { goAMap } from './utils/common';
export default {
  name: 'StagnantWaterCard',
  layerName: ['pondingLayer', 'nopondingLayer'],
  legendName: '积水',
  components: {
    PanelItem,
    PrimaryMapTable,
    CapsuleTabs
  },
  computed: {
    ...mapGetters(['previewModule']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  data() {
    let that = this
    this.chartEvents = {
      click: function (params) {
        console.log(params)
        that.getWaterLoggingStats(params.name)
      }
    }
    return {
      jsbtn: false, // 落点
      activeTab: 0, // 0按行政区划 1按是否有积水
      pointList: [],
      barData: {
        columns: ['name', 'type'],
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
          right: 10,
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
          },
        }
      },
      timer: null,
      tableColumns: [
        {
          prop: 'name',
          label: '仓库名称'
        },
        {
          prop: 'materialPrice',
          label: '仓库总价值'
        }
      ],
      dataList: [],
      name: ''
    };
  },
  mounted() {
    let that = this;
    this.$nextTick(() => {
      this.getDeviceType();
      this.getWaterLoggingList();
      this.timer = setInterval(() => {
        this.getWaterLoggingList();
        this.getDeviceType();
      }, 300000);
      // this.$refs.chart.echarts.on('click', function (params) {
      //   that.getWaterLoggingStats(params.name)
      // });
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.map.drawClusterPoint([], {}, 'pondingLayer');
  },
  methods: {
    ...mapActions(['SetDetailDialogParam']),
    setType(value) {
      switch (value) {
        case 'sgxs':
          return '省供销社';
        case 'shcj':
          return '社会厂家';
        case 'xjck':
          return '县级仓库';
        case 'sjck1':
          return '市级仓库';
        default:
          return '省级仓库';
      }
    },
    async getDeviceType() {
      let res = await getWaterWarehousesCount();
      if (res.code == 0) {
        let rows = [];
        for (let index in res.data.totalNumByType) {
          rows.push({
            name: this.setType(res.data.totalNumByType[index].key),
            type: res.data.totalNumByType[index].value
          });
        }
        this.barData.rows = rows;
        this.$refs['chart'].echarts.resize();
      }
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
    // 地图点位
    handleStagnantWater(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/new_images/point/icon-warehouse-juhe.png'),
        src: require('@/assets/new_images/point/icon-warehouse.png')
      };
      let pondingList = [];
      for (let point of this.pointList) {
        let obj = {
          name: point.name,
          type: '救援仓库',
          lng: +point.longitude,
          lat: +point.latitude,
          code: point.id,
          stationCode: point.id || '',
          props: {
            仓库面积: `${point.acreage}㎡`,
            仓库价值: `${point.materialPrice !== null ? point.materialPrice : '-'}元`,
            联系人: `${point.chargePerson}`,
            联系方式: `${point.chargePhone}`
          }
        };
        pondingList.push(obj);
      }
      if (this[btnType]) {
        this.map.drawClusterPoint(pondingList, style, 'pondingLayer');
      } else {
        this.map.drawClusterPoint([], {}, 'pondingLayer');
      }
    },
    goToAMap(type) {
      goAMap(type);
    },
    handleTabChange(val) {
      this.activeTab = val;
    },
    // 获取点位
    async getWaterLoggingList() {
      let res = await getWaterWarehouses();
      this.pointList = res.data.list;
    },
    // 获取积水统计数据
    async getWaterLoggingStats(opt) {
      let res = await getWaterWarehouses({
        type: opt
      });
      if (res.code == 0) {
        this.dataList = res.data.list
        this.activeTab = 1
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
      let barWidth = 20;
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
      ];
      return options;
    }
  }
};
</script>
<style lang="scss" scoped>
.stagnant-water-card {
  width: 100%;
  height: 100%;
  .area-box,
  .is-water-box {
    width: 100%;
    height: calc(100% - 14px);
  }
  .is-water-box {
    padding-top: 10px;
    .table-list {
      padding-top: 10px;
      height: calc(100% - 24px);
    }
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
.is-water-box .table-wrap .el-table th {
  background-color: rgba(41, 147, 180, 0.1);
}
</style>