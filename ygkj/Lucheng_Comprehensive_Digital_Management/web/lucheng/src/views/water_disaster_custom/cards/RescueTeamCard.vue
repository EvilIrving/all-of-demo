<template>
  <div class="rain-forecast-card">
    <panel-item title="救援队伍" :inSimulation="previewModule" has-switch tc @tclick="tclick">
      <template #title-switch>
        <i
          title="地图落点"
          @click.stop="handleReservoirChange('rescuebtn')"
          :class="[rescuebtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
        />
        <i v-show="!previewModule" title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog" />
      </template>
      <div class="table-list">
        <v-table :tableData="dataList" :operation="false" :dynamicColumnSetting="tableColumns">
          <template v-slot:name="row">
            <span>{{ row.data.name }}</span>
          </template>
        </v-table>
      </div>
    </panel-item>
    <RescueTeamDetail v-if="detailVisible" :visible.sync="detailVisible" type="救援队伍" />
  </div>
</template>

<script>
import PanelItem from '../components/PanelItem.vue';
import { disasterPreventionApi } from '@/api/RightSildePanel';
import { mapGetters, mapActions } from 'vuex';
import RescueTeamDetail from './dialog/RescueTeamDetail.vue';
import { cloneObj } from '@/utils/utils';
import VTable from '../dialog/DetailsCommonDialog/components/table/VTable.vue';
export default {
  name: 'RescueTeamCard',
  layerName: 'runRescueteamLayer',
  legendName: '救援队伍',
  components: {
    PanelItem,
    VTable,
    RescueTeamDetail
  },
  mounted() {
    this.getRightMapPoints(this.rescuebtn);
  },
  data() {
    return {
      rescuebtn: false, //救援队伍点位展示标记
      //救援队伍数据
      detailVisible: false,
      //救援队伍列表
      dataList: [],
      tableColumns: [],
      legendOptions: [
        {
          type: 1,
          value: 'rescueteam',
          name: '救援队伍',
          mark: 'jy'
        }
      ]
    };
  },
  beforeDestroy() {
    this.setLegend(this.legendOptions[0].name);
    this.setPoint([], {}, projectType);
  },
  computed: {
    ...mapGetters(['ScreenMap', 'currentSelectArea', 'previewModule', 'formatterCurrentTime', 'AffairId']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  watch: {},
  methods: {
    ...mapActions(['changeScreenLoading', 'getForecastRainfallLayer', 'SetDetailDialogParam']),
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
    },
    //  打开详情弹框点击事件
    openDetailsDialog(val) {
      this.detailVisible = true;
    },
    // 表格点击  单个项目
    rowClick(data) {
      console.log(data, '单击表格数据');
      //   this.$parent.$emit('showDialog', data);
    },
    // 获取点位数据
    async getRightMapPoints(flag) {
      this.tableColumns = [
        {
          prop: 'resName',
          label: '救援队伍名称'
        },
        {
          prop: 'waterLevel',
          label: '现有人数',
          sortable: true
        }
      ];
      let opt = { type: '救援队伍' };
      //   let res = await projectApi.loadPage({});
      let res = {
        code: 0,
        data: [
          {
            resName: 'XXXXX',
            waterLevel: '15m',
            meiFloodLimitWaterLevel: '20m',
            projectName: '啊大大发',
            type: 'rescueteam',
            lat: 27.384784,
            lng: 120.386814,
            projectType: 'rescueteam'
          },
          {
            resName: 'XXXXX',
            waterLevel: '15m',
            meiFloodLimitWaterLevel: '20m',
            projectName: '范德萨发的说法辅导费',
            type: 'rescueteam',
            lat: 27.382736,
            lng: 120.410339,
            projectType: 'rescueteam'
          },
          {
            resName: 'XXXXX',
            waterLevel: '15m',
            lat: 27.448118,
            lng: 120.656576,
            meiFloodLimitWaterLevel: '20m',
            projectName: '啊啊啊啊啊fdaefa啊啊啊啊啊啊',
            type: 'rescueteam',
            projectType: 'rescueteam'
          },
          {
            resName: 'XXXXX',
            waterLevel: '15m',
            lat: 27.392998,
            lng: 120.533896,
            meiFloodLimitWaterLevel: '20m',
            projectName: '范德萨发大水',
            type: 'rescueteam',
            projectType: 'rescueteam'
          },
          {
            resName: 'XXXXX',
            waterLevel: '15m',
            lat: 27.571349,
            lng: 120.472791,
            meiFloodLimitWaterLevel: '20m',
            projectName: 'eeeeeeee',
            type: 'rescueteam',
            projectType: 'rescueteam'
          },
          {
            resName: 'XXXXX',
            waterLevel: '15m',
            lat: 27.204187,
            lng: 120.471922,
            meiFloodLimitWaterLevel: '20m',
            projectName: 'dfsafeewq',
            type: 'rescueteam',
            projectType: 'rescueteam'
          },
          {
            resName: 'XXXXX',
            waterLevel: '15m',
            lat: 27.503693,
            lng: 120.409137,
            meiFloodLimitWaterLevel: '20m',
            projectName: 'fdfafsaf ',
            type: 'rescueteam',
            projectType: 'rescueteam'
          }
        ]
      };
      if (res.code == 0) {
        let resArr = (this.dataList = res.data);
        let redList = [];
        let projectType = this.legendOptions[0].value;
        resArr.map((item) => {
          let obj = {
            name: item.pjnm,
            type: '救援队伍',
            lng: item.lng,
            lat: item.lat,
            code: item.pjcd,
            projectType: projectType,
            depId: item.projectType,
            props: {
              救援队伍名称: item.resName,
              现有人数: item.waterLevel,
              联系方式: item.meiFloodLimitWaterLevel
            }
          };
          redList.push(obj);
        });
        if (flag) {
          this.setPoint(redList, this.getPointStyle(projectType), projectType);
        }
      }
    },
    /**
     * 点位开关
     * @param {number} index 开关的序号
     */
    handleReservoirChange(btnType) {
      this[btnType] = !this[btnType];
      if (this[btnType]) {
        this.setLegend(this.legendOptions[0].name, this.legendOptions[0].value);
        this.getRightMapPoints(btnType);
      } else {
        this.rescuebtn = false;
        let projectType = this.legendOptions[0].value;
        this.setLegend(this.legendOptions[0].name);
        this.setPoint([], {}, projectType);
      }
    },
    /**
     * @description: 获取点位样式对象
     * @param {String} projectType 工程类别en 可根据tabCorrType与当前选中activeTab获取
     * @return {Object}
     */
    getPointStyle(projectType) {
      let defaultStyle = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        srcs: null,
        src: null
      };
      let styleObj = cloneObj(defaultStyle);
      styleObj.src = require(`@/assets/images/MapPoint/point-${projectType}.png`);
      styleObj.srcs = require(`@/assets/images/MapPoint/group-${projectType}.png`);
      return styleObj;
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
              label: '点位',
              icon: `icon-legend-${value}-point`
            },
            {
              label: '聚合',
              icon: `icon-legend-${value}-group`
            }
          ]
        });
      } else {
        this.$emit('legend-change', name, {});
      }
    },
    /**
     * @description: 设置点位
     * @param {Array} pointList 点位数组
     * @param {Object} pointStyle 点位样式
     * @param {String} projectType 工程类别en
     * @return {*}
     */
    setPoint(pointList, pointStyle, projectType) {
      let typeUpper = projectType.charAt(0).toUpperCase() + projectType.slice(1);
      this.map.drawClusterPoint(pointList, pointStyle, `run${typeUpper}Layer`);
    }
  }
};
</script>

<style lang="scss" scoped>
@import './style/SildePanel.scss';
.rain-forecast-card {
  height: 100%;
  .flood-discharge {
    user-select: none;
    color: #fff;
    width: 100%;
    overflow-y: scroll;
    &::-webkit-scrollbar {
      display: none;
    }
    .flood-discharge-item {
      padding: 10px 0;
      border-right: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
      position: relative;
      &.white-bg {
        &:after {
          content: '';
          position: absolute;
          height: 100%;
          width: 200%;
          left: 0;
          top: 0;
          opacity: 0.5;
          background-image: linear-gradient(90deg, #ffffff33 0%, #f9f9f900 100%);
        }
      }
      &:nth-child(even) {
        border-right: 0;
      }
      .flood-discharge-item__content {
        .item-title {
          font-size: 16px;
          text-align: center;
          padding-top: 5px;
        }
        .item-text {
          display: flex;
          padding-top: 5px;
          font-size: 18px;
          justify-content: center;
          > i {
            margin: 5px 10px 0 0;
          }
          .item-label {
            font-size: 14px;
            .number {
              margin: 0 5px;
              font-size: 24px;
              color: #1cfff5;
            }
          }
        }
      }
    }
  }
}
::v-deep .rivertooltip__item {
  padding: 4px 8px;
  font-size: 18px;
  &.rivertooltip__item-error {
    color: #ff7144;
  }
  &.rivertooltip__item-warn {
    color: #eec80b;
  }
}
::v-deep .panel-item__title--switch {
  i {
    cursor: pointer;
  }
}
.reservoir-sty {
  > p {
    @include flexbox;
    @include flexflow(row, nowrap);
    line-height: 32px;
    padding: 4px 0;
    span {
      color: #fff;
      font-size: 24px;
    }
    display: flex;
    // flex-direction: column;
    align-items: center;
  }
  // > p:nth-child(1) {
  //   margin-right: 16px;
  //   > span {
  //     color: #1cfff5;
  //   }
  // }
  > p:nth-child(2) {
    background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png) no-repeat top center;
    background-size: 100% 100%;
  }
}
.reservoir-style {
  $color: #1cfff485;
  // position: absolute;
  content: '';
  width: 100%;
  // height: 2.5rem;
  left: 0;
  background: linear-gradient(to bottom, $color, $color) left top no-repeat,
    linear-gradient(to bottom, $color, $color) left top no-repeat,
    linear-gradient(to bottom, $color, $color) right top no-repeat,
    linear-gradient(to bottom, $color, $color) right top no-repeat,
    linear-gradient(to bottom, $color, $color) left bottom no-repeat,
    linear-gradient(to bottom, $color, $color) left bottom no-repeat,
    linear-gradient(to bottom, $color, $color) right bottom no-repeat,
    linear-gradient(to bottom, $color, $color) right bottom no-repeat;
  /*设置大小*/
  background-size: 12px 2px, 2px 12px;
  border-radius: 3px;
}
::v-deep .el-carousel {
  .el-carousel__container {
    height: 100%;
  }
}

.carousel-item-content {
  // padding: 0 16px;
  @include flexbox;
  @include flexflow(column, nowrap);
  @include flexJC(space-around);
}
.reservoir-sty1 {
  margin: 0;
  @include flexbox;
  @include flexJC(space-between);
  p {
    margin: 0;
    line-height: 32px;
    width: 50%;
    margin: 12px 0;
    @include flexbox;
    @include flexflow(column, nowrap);
    @include flexAC;
    @include flexJC(center);
    background: url(~assets/images/WaterDisastersImage/rainfall-bg.png) no-repeat;
    background-size: 100% 100%;
  }
  > p:nth-child(1) {
    margin-right: 16px;
    > span {
      color: #1cfff5;
      font-size: 16px;
    }
  }
  > p:nth-child(2) {
    background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png) no-repeat top center;
    background-size: 100% 100%;
    > span {
      font-size: 16px;
      > span {
        color: #eec80b;
      }
    }
  }
}
.area-style {
  padding: 5px !important;
  // margin: 0;
  margin: 0;
  height: 40px;
  p {
    @include flexbox;
    @include flexflow(row, nowrap);
    @include flexAC;
    span {
      margin: 0;
    }
  }

  > p:nth-child(1) {
    line-height: 1.8rem;
    color: #1cfff5;
    font-weight: 500;
    @include flexbox;
    > i {
      vertical-align: text-top;
      margin-top: 2px;
      margin-right: 0;
    }
    > span {
      padding-left: 2px;
      color: #1cfff5;
    }
  }
  > p:nth-child(2) {
    line-height: 25px;
    color: #fff;
    > span {
      // font-size: 28px;
      color: #1ff1f0;
      padding: 0 6px;
    }
  }
  > p:nth-child(3) {
    line-height: 25px;
    color: #fff;
    > i {
      vertical-align: text-top;
      margin-top: 7px;
      margin-right: 0;
    }
    > span {
      font-size: 28px;
      color: #eec80b;
      padding: 0 6px;
    }
  }
}
.diagram-sty {
  // margin: 15px 0;
  color: #fff;
  height: calc(100% - 60px);
  .diagram-sty-content {
    height: 100%;
  }
  .border-L {
    height: 90%;
    border-right: 1px solid rgba($color: #00c1ff, $alpha: 0.4);
  }
  ::v-deep.border-R::-webkit-scrollbar {
    /*滚动条整体样式*/
    width: 0; /*高宽分别对应横竖滚动条的尺寸*/
  }
  .border-R {
    padding-left: 10px;
    height: 90%;
    overflow-y: auto;
    font-size: 16px;
    color: #1cfff5;
    .classOption-sty {
      padding: 5px 0;
      display: flex;
      justify-content: space-between;
      > p:nth-child(1) {
        // width: 100px;
        overflow-x: auto;
        overflow: hidden;
      }
    }
  }
}
.storage-capacity {
  margin-top: 9px;
  background-image: linear-gradient(64deg, rgba(0, 193, 255, 0) 5%, #00c1ff 100%);
  width: 15%;
  height: 2px;
}
.storage-capacity-sty {
  background-image: linear-gradient(64deg, #00c1ff 5%, rgba(0, 193, 255, 0) 100%);
  margin-top: 9px;
  width: 20%;
  height: 2px;
}

.icon-sty {
  margin-right: 10px;
  &.icon-year-100 {
    @include chart-icon();
    background: #19af47;
  }
  &.icon-year-50 {
    @include chart-icon();
    background: #eec80b;
  }
  &.icon-year-20 {
    @include chart-icon();
    background: #e89100;
  }
  &.icon-year-10 {
    @include chart-icon();
    background: #dd00db;
  }
  &.icon-year-5 {
    @include chart-icon();
    background: #e80000;
  }
  &.risk-legend {
    width: 16px;
    height: 16px;
    border-radius: 50%;
    display: inline-block;
    @include chart-icon();
  }
}
</style>
