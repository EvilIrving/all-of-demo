<!--
 * @Author: dyn
 * @Date: 2021-07-21 15:51:37
 * @LastEditTime: 2022-04-18 17:22:21
 * @LastEditors: Please set LastEditors
 * @Description: 洪水风险图小组件
 * @FilePath: \lucheng\src\views\water_disaster_custom\cards\FloodRiskCard.vue
-->
<template>
  <div class="flood-risk-card">
    <panel-item
      title="洪水风险图"
      has-switch
      tc
      @tclick="tclick"
      :action-tabs="['淹没面积', '影响人口', '淹没区洪水损失']"
      @change="handleTabChange"
    >
      <template #title-switch>
        <RadioDropdown v-model="checkYear" :options="yearOption" label-suffix="年一遇" @change="handleYear" />
        <el-popover
          placement="bottom-start"
          width="140"
          trigger="hover"
          @show="basinDropdownShow"
          @hide="basinDropdownHide"
          popper-class="basin-popper"
        >
          <el-checkbox-group v-model="checkBasin" >
            <el-checkbox label="瓯江干流鹿城片"></el-checkbox>
            <el-checkbox label="戍浦江片"></el-checkbox>
            <el-checkbox label="温瑞平原片"></el-checkbox>
          </el-checkbox-group>
          <span class="dropdown-wrap" :class="basinDropdownVisible ? 'dropdown-active' : ''" slot="reference">
            <i
              style="vertical-align: text-bottom; margin-left: 6px"
              :class="basinDropdownVisible ? 'icon-yinzhang-active' : 'icon-yinzhang-1'"
            ></i>
            <i class="el-icon-caret-bottom"></i>
          </span>
        </el-popover>
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('洪水风险图')"
          style="margin-left: 10px"
        />
      </template>
      <div class="area-box" v-if="activeTab == 0">
        <div class="area-box-title">
          淹没总面积<span>{{ floodTotal.floodedAreaTotal }}</span
          >km²
        </div>
        <div class="pie-chart-panel">
          <div id="waterUseEchart" ref="areaPieA"></div>
          <div class="legend-panel">
            <div class="legend-item" v-for="(item, index) in areaPieData" :key="index">
              <i :class="['icon-sty', 'icon-circle', 'icon-year-' + index]" />
              <div class="text">
                <p>{{ item.name }}</p>
                <p>
                  <span style="color: #23e3fb; font-size: 1rem">{{ item.value }}</span>
                  km²
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="area-box" v-if="activeTab == 1">
        <div class="area-box-title">
          影响总人口<span>{{ floodTotal.submergedPopulationTotal }}</span
          >万人
        </div>
        <div class="pie-chart-panel">
          <div id="waterUseEchart" ref="areaPieB"></div>
          <div class="legend-panel">
            <div class="legend-item" v-for="(item, index) in peoplePieData" :key="index">
              <i :class="['icon-sty', 'icon-circle', 'icon-year-' + (index + 3)]" />
              <div class="text">
                <p>{{ item.name }}</p>
                <p>
                  <span style="color: #23e3fb; font-size: 1rem">{{ item.value }}</span>
                  万人
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="area-box" v-if="activeTab == 2">
        <div class="area-box-title">
          淹没区洪水总损失<span>{{ floodTotal.floodLossTotal }}</span
          >万元
        </div>
        <div class="pie-chart-panel">
          <div id="waterUseEchart" ref="areaPieC"></div>
          <div class="legend-panel">
            <div class="legend-item" v-for="(item, index) in moneyPieData" :key="index">
              <i :class="['icon-sty', 'icon-circle', 'icon-year-' + (index + 6)]" />
              <div class="text">
                <p>{{ item.name }}</p>
                <p>
                  <span style="color: #23e3fb; font-size: 1rem">{{ item.value }}</span>
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
import { mapGetters, mapActions } from 'vuex';
import PanelItem from '../components/PanelItem.vue';
import { disasterPreventionApi } from '@/api/RightSildePanel';

import * as echarts from 'echarts'; // echart插件
import RadioDropdown from '../components/RadioDropdown.vue';

export default {
  name: 'FloodRiskCard',
  layerName: 'designFloodLayer',
  legendName: '淹没深度',
  components: { PanelItem, RadioDropdown },
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
      videoTotal: 80,
      yearDropdownVisible: false,
      basinDropdownVisible: false,
      checkYear: '5',
      checkBasin: [],
      yearOption: ['5', '10', '20', '50'],
      pieData: [],
      designFloodName: {
        瓯江干流鹿城片: 'OJ_YMSSSJ',
        戍浦江片: 'YMSSsj',
        温瑞平原片: 'WRYMSSsj'
      },
      areaPieData: [],
      peoplePieData: [],
      moneyPieData: [],
      floodTotal: {},
      legendFlag: false,
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.floodRiskList();
    });
  },
  watch: {
    checkBasin: function (newVal, oldVal) {
      if (newVal.length > oldVal.length) {
        this.map.drawDesignFlood(this.designFloodName[newVal[newVal.length - 1]], this.checkYear);
      } else {
        for (let item of oldVal) {
          if (newVal.findIndex((nitem) => nitem == item) == -1) {
            this.map.clearDesignFlood(`${this.designFloodName[item]}${this.checkYear}`);
          }
        }
      }
      if (newVal.length == 0) {
        this.legendFlag = false;
        this.$emit('legend-change', '淹没深度', {});
      } else {
        if (!this.legendFlag) {
          this.$emit('legend-change', '淹没深度', {
            label: '淹没水深彩色',
            children: [
              {
                label: '0.2~0.5',
                icon: 'icon-flood02'
              },
              {
                label: '0.5~1',
                icon: 'icon-flood05'
              },
              {
                label: '1~1.5',
                icon: 'icon-flood1'
              },
              {
                label: '1.5~2',
                icon: 'icon-flood15'
              },
              {
                label: '2~3',
                icon: 'icon-flood2'
              },
              {
                label: '>3',
                icon: 'icon-flood3'
              }
            ]
          });
          this.legendFlag = true;
        }
      }
    }
  },
  methods: {
    ...mapActions(['SetDetailDialogParam', 'SetFloodRiskTotal']),
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
    handleTabChange(val) {
      this.activeTab = val;
      switch (this.activeTab) {
        case 1:
          this.$nextTick(() => {
            this.setAreaPieB(this.areaPieData);
          });
          break;
        case 2:
          this.$nextTick(() => {
            this.setAreaPieC(this.peoplePieData);
          });
          break;
        default:
          this.$nextTick(() => {
            this.setAreaPieA(this.moneyPieData);
          });
          break;
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
    // 流域下拉
    yearDropdownShow(val) {
      this.yearDropdownVisible = val;
    },
    handleYear() {
      this.map.clearDesignFlood();
      if (this.checkBasin.length > 0) {
        for (let i of this.checkBasin) {
          this.map.drawDesignFlood(this.designFloodName[i], this.checkYear);
        }
      }
      this.floodRiskList();
    },
    basinDropdownShow() {
      this.basinDropdownVisible = true;
    },
    basinDropdownHide() {
      this.basinDropdownVisible = false;
    },
    async floodRiskList() {
      this.peoplePieData = [];
      this.areaPieData = [];
      this.moneyPieData = [];
      this.pieData = [];
      let obj = {
        designStandard: `${this.checkYear}年一遇`
      };
      await disasterPreventionApi.floodRiskList(obj).then((res) => {
        if (res.code == 0) {
          let arr = res.data.list;
          for (let item of arr) {
            this.areaPieData.push({
              name: item.name,
              value: item.floodedArea || 0,
            });
            this.peoplePieData.push({
              name: item.name,
              value: item.submergedPopulation || 0,
            });
            this.moneyPieData.push({
              name: item.name,
              value: item.floodLoss || 0,
            });
          }
          let totalObj = {
            floodedAreaTotal: arr[0].floodedAreaTotal,
            submergedPopulationTotal: arr[0].submergedPopulationTotal,
            floodLossTotal: arr[0].floodLossTotal
          };
          this.floodTotal = totalObj;
          this.SetFloodRiskTotal(totalObj);
          this.handleTabChange(this.activeTab)
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
@import './style/SildePanel.scss';
.flood-risk-card {
  width: 100%;
  height: 100%;
  .el-dropdown {
    cursor: pointer;
    .el-dropdown-link {
      display: flex;
      align-items: center;
      outline: none;
      &.dropdown-active {
        span,
        i {
          color: #1cfff5;
        }
      }
    }
  }
  .dropdown-wrap {
    cursor: pointer;
    display: flex;
    align-items: center;
    outline: none;
    &.dropdown-active {
      i {
        color: #1cfff5;
      }
    }
  }
  .card-container {
    width: 100%;
    height: calc(100% - 100px);
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
          box-sizing: border-box;
          padding-right: 5px;
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
}
</style>

<style lang="scss">
.flood-risk-card .panel-item .panel-item__content--tabs .tab-button,
.flood-risk-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.flood-risk-card .panel-item .panel-item__content--tabs .tab-button.active,
.flood-risk-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
.flood-risk-card .panel-item .panel-item__title--switch {
  display: flex;
  align-items: center;
  padding-bottom: 4px;
  .el-dropdown-link {
    outline: none;
    &.dropdown-active {
      color: #00baff;
    }
  }
}
.basin-popper {
  .el-checkbox {
    padding-bottom: 10px;
    &:last-child {
      padding-bottom: 0;
    }
    &.is-checked {
      .el-checkbox__label {
        color: #00baff;
      }
    }
  }
  .el-checkbox__input.is-checked .el-checkbox__inner {
    background-color: #00baff;
    border-color: #00baff;
  }
  .el-checkbox__label {
    color: #fff;
  }
}
</style>
