<template>
  <div class="rain-forecast-card">
    <panel-item title="水情" has-switch tc @tclick="tclick">
      <template #title-switch>
        <i
          v-if="!showDetail"
          title="地图落点"
          @click.stop="handleWorkConditionChange('waterbtn')"
          :class="[waterbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
        />
        <i title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog('水情')" />
      </template>
      <!-- 水情 -->
      <div class="bar_box" v-if="!showDetail">
        <el-row
          v-for="(item, index) in rankingData"
          :key="index"
          :gutter="14"
          class="bar_line"
          @click.native="typeTabChange(item)"
        >
          <el-col :span="21">{{ `${item.name}报警数量` }}</el-col>
          <el-col :span="3">
            <span :style="{ color: '#FCEE27', fontSize: '16px' }">{{ item.value }}</span>
            <span>/{{ item.total }}</span>
          </el-col>
          <el-col :span="24">
            <el-progress :show-text="false" :stroke-width="4" :percentage="getPercent(item)" :color="'#FCEE27'" />
          </el-col>
        </el-row>
      </div>
      <div class="water-gate-sty" v-else>
        <div class="flex-box">
          <CapsuleTabs :tabs="['返回']" @change="handleTabChange" />
          <div class="right-box">
            <span>正常/报警</span>
            <el-dropdown placement="bottom-start" trigger="click">
              <span class="el-dropdown-link" :class="yearDropdownVisible ? 'dropdown-active' : ''">
                <span style="font-size: 1rem">{{ status }}</span>
                <i class="el-icon-caret-bottom"></i>
              </span>
              <el-dropdown-menu slot="dropdown" class="year-dropdown">
                <el-dropdown-item v-for="(item, index) in statusOption" :key="index">
                  <el-radio v-model="status" :label="item" @change="statusChange">{{ item }}</el-radio>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
        <div class="table-list">
          <primary-map-table
            :columnList="tableColumns"
            :dataList="dataList"
            @rowClick="rowClick"
            :paginationFlag="false"
          ></primary-map-table>
        </div>
      </div>
    </panel-item>
  </div>
</template>

<script>
import PanelItem from '../components/PanelItem.vue';
import CapsuleTabs from '@/views/WaterDisasterPreventionScreen/components/CapsuleTabs.vue';
import { PrimaryMapTable } from '@/components/';
import { waterAndRain } from '@/api/bigScreenPart';
import { mapGetters, mapActions } from 'vuex';
import { cloneObj } from '@/utils/utils';
export default {
  name: 'SluiceCard',
  layerName: ['reservoirLayer', 'weirgateLayer', 'riverLayer', 'tideLayer'],
  legendName: ['堰闸', '水库', '潮位', '河道'],
  components: {
    PanelItem,
    PrimaryMapTable,
    CapsuleTabs
  },
  async mounted() {
    // RR ZZ  DD TT
    // ['水库','河道', '堰闸','潮位',],
    await this.getOverView('RR');
    await this.getOverView('ZZ');
    await this.getOverView('TT');
    await this.getOverView('DD');
    this.timer = setInterval(async () => {
      this.rankingData = [];
      await this.getOverView('RR');
      await this.getOverView('ZZ');
      await this.getOverView('TT');
      await this.getOverView('DD');
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.ScreenMap.removeLayerByName('reservoirLayer');
    this.ScreenMap.removeLayerByName('weirgateLayer');
    this.ScreenMap.removeLayerByName('riverLayer');
    this.ScreenMap.removeLayerByName('tideLayer');
    this.legendOptions.map((item) => {
      this.setHealthCodeLegend(item.name);
      this.setPoint([], item.value, 'normal', false);
      this.setPoint([], item.value, 'warn', false);
    });
  },
  data() {
    return {
      //水情列表
      wageStList: [],
      // 水情列表类型
      workConditionType: '',
      waterbtn: false, //水情点位展示标记
      timer: null,
      selectItem: '水位站总数',
      tableColumns: [
        {
          prop: 'stName',
          label: '名称',
          width: '60'
        },
        {
          prop: 'waterLevel',
          label: '水位(m)',
          width: '80'
        },
        {
          prop: 'limitLevel',
          label: '汛限水位(m)'
        }
      ],
      dataList: [],
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
      ],
      showDetail: false,
      activeTab: 0,
      rankingData: [],
      yearDropdownVisible: false,
      statusOption: ['正常', '报警'],
      status: '正常',
      reservoirList: [],
      riverList: [],
      weirgateList: [],
      tideList: []
    };
  },
  computed: {
    ...mapGetters(['ScreenMap', 'currentSelectArea', 'previewModule', 'formatterCurrentTime']),
    map() {
      return this.$store.state.screen.mapComponent;
    },
    restData() {
      const [...rest] = this.rankingData;
      return /** @type {any[]} */ (rest);
    },
    sumData() {
      if (this.rankingData.length == 0) return 0;
      return this.rankingData.map((item) => item.value).reduce((a, b) => a + b);
    }
  },
  methods: {
    ...mapActions(['changeScreenLoading', 'getForecastRainfallLayer', 'SetDetailDialogParam']),
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
    },
    // 获取水情 所有数据 统计数据自行计算
    async getOverView(stationType) {
      // RR ZZ  DD TT
      // ['水库','河道', '堰闸','潮位',],
      const opt = { stationType };
      await waterAndRain.waterStationsList(opt).then((res) => {
        if (res.code == 0) {
          let data = res.data.filter((item) => item.warning == true);
          let LevelLimit = data.length;
          let levelNumber = res.data.length;
          if (stationType == 'RR') {
            this.rankingData.push({ name: '水库', value: LevelLimit, total: levelNumber, code: 'RR' });
          } else if (stationType == 'ZZ') {
            this.rankingData.push({ name: '河道', value: LevelLimit, total: levelNumber, code: 'ZZ' });
          } else if (stationType == 'DD') {
            this.rankingData.push({ name: '堰闸', value: LevelLimit, total: levelNumber, code: 'DD' });
          } else if (stationType == 'TT') {
            this.rankingData.push({ name: '潮位', value: LevelLimit, total: levelNumber, code: 'TT' });
          }
          this.$forceUpdate();
        }
      });
    },
    // 进度条点 改变类型水库列表
    async typeTabChange(item) {
      if (this.workConditionType == '水库') {
        this.tableColumns = [
          {
            prop: 'stName',
            label: '名称',
            width: '60'
          },
          {
            prop: 'waterLevel',
            label: '水位(m)',
            width: '80'
          },
          {
            prop: 'limitLevel',
            label: '汛限水位(m)'
          }
        ];
      } else {
        this.tableColumns = [
          {
            prop: 'stName',
            label: '名称',
            width: '60'
          },
          {
            prop: 'waterLevel',
            label: '水位(m)',
            width: '80'
          },
          {
            prop: 'warningLevel',
            label: '警戒水位(m)'
          }
        ];
      }
      this.workConditionType = item.name;
      this.showDetail = true;
      let type = '';
      switch (this.workConditionType) {
        case '潮位':
          type = 'TT';
          break;
        case '水库':
          type = 'RR';
          break;
        case '堰闸':
          type = 'DD';
          break;
        default:
          type = 'ZZ';
          break;
      }
      this.getList(type);
    },
    // 返回进度条信息系
    handleTabChange(val) {
      this.status = '正常';
      this.showDetail = false;
      this.legendOptions.map((item) => {
        this.setHealthCodeLegend(item.name);
        this.setPoint([], item.value, 'normal', false);
        this.setPoint([], item.value, 'warn', false);
      });
      // this.getOverView();
    },
    statusChange() {
      let type = '';
      switch (this.workConditionType) {
        case '潮位':
          type = 'TT';
          break;
        case '水库':
          type = 'RR';
          break;
        case '堰闸':
          type = 'DD';
          break;
        default:
          type = 'ZZ';
          break;
      }
      this.getList(type);
    },
    async getList(type) {
      let opt = { stationType: type };
      if (this.status) {
        opt['warning'] = this.status == '正常' ? false : true;
      }
      let res = await waterAndRain.waterStationsList(opt);
      if (res.code == 0) {
        this.dataList = res.data;
      }
    },
    // 表格点击  单个项目
    rowClick(data) {
      console.log(data);
      if (!this.waterbtn) {
        this.legendOptions.map((item) => {
          this.setHealthCodeLegend(item.name);
          this.setPoint([], item.value, 'normal', false);
          this.setPoint([], item.value, 'warn', false);
        });
        this.setMapPlace([data], data.stType);
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
    /**
     * 计算百分比
     */
    getPercent(item) {
      return item.value != 0 ? Number((item.value / item.total) * 100) : 0;
    },
    /**
     * 点位list
     */
    async getRightMapList(type) {
      let opt = { stationType: type, warning: this.status == '正常' ? false : true };
      let res = await waterAndRain.waterStationsList(opt);
      if (res.code == 0) {
        let arr = res.data;
        this.setMapPlace(arr, type);
      }
    },
    setMapPlace(arr, type) {
      arr.map((item) => {
        let obj = {
          name: item.stName,
          lat: item.stLat,
          lng: item.stLong,
          code: item.stCode,
          stType: item.stType,
          warning: item.warning,
          type: '水情',
          props: {
            名称: item.stName,
            水位: item.waterLevel,
            更新时间: item.time
          }
        };
        if (type == 'RR') {
          this.reservoirList.push(obj);
        } else if (type == 'ZZ') {
          this.riverList.push(obj);
        } else if (type == 'TT') {
          this.tideList.push(obj);
        } else {
          this.weirgateList.push(obj);
        }
      });
      // ['水库','河道', '堰闸','潮位',],
      // RR ZZ  DD TT
      switch (type) {
        case 'RR':
          let warnList = [];
          let normalList = [];
          this.reservoirList.map((i) => {
            if (i.warning == false) {
              normalList.push(i);
            } else {
              warnList.push(i);
            }
          });
          this.setPoint(warnList, 'reservoir', 'warn', true);
          this.setPoint(normalList, 'reservoir', 'normal', true);
          break;
        case 'ZZ':
          let normalRiverList = [];
          let warnRiverList = [];
          this.riverList.map((i) => {
            if (i.warning == false) {
              normalRiverList.push(i);
            } else {
              warnRiverList.push(i);
            }
          });
          this.setPoint(normalRiverList, 'river', 'normal', true);
          this.setPoint(warnRiverList, 'river', 'warn', true);
          break;
        case 'DD':
          let warnWeirgateList = [];
          let normalWeirgateList = [];
          this.weirgateList.map((i) => {
            if (i.warning == false) {
              normalWeirgateList.push(i);
            } else {
              warnWeirgateList.push(i);
            }
          });
          this.setPoint(normalWeirgateList, 'weirgate', 'normal', true);
          this.setPoint(warnWeirgateList, 'weirgate', 'warn', true);
          break;
        default:
          let warnTideList = [];
          let normalTideList = [];
          this.tideList.map((i) => {
            if (i.warning == false) {
              normalTideList.push(i);
            } else {
              warnTideList.push(i);
            }
          });
          this.setPoint(normalTideList, 'tide', 'normal', true);
          this.setPoint(warnTideList, 'tide', 'warn', true);
          break;
      }
    },
    /**
     * 水情点位开关
     */
    handleWorkConditionChange(btnType) {
      this[btnType] = !this[btnType];
      this.reservoirList = [];
      this.riverList = [];
      this.weirgateList = [];
      this.tideList = [];
      if (this[btnType]) {
        this.getRightMapList('RR');
        this.getRightMapList('ZZ');
        this.getRightMapList('DD');
        this.getRightMapList('TT');
        this.legendOptions.map((item) => {
          this.setHealthCodeLegend(item.name, item.mark);
        });
      } else {
        this.waterbtn = false;
        this.legendOptions.map((item) => {
          this.setHealthCodeLegend(item.name);
          this.setPoint([], item.value, 'normal', false);
          this.setPoint([], item.value, 'warn', false);
        });
      }
    },
    /**
     * @description: 设置水情图例
     * @param {String} name 显示图例名称
     * @param {String} value
     * @return {*}
     */
    setHealthCodeLegend(name, value) {
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
@import './style/SildePanel.scss';
.rain-forecast-card {
  height: 100%;
  .bar_box {
    position: absolute;
    overflow: auto;
    // 隐藏滚动条  overflow显示设置为auto或者scroll保证内容是可滚动的。
    scrollbar-width: none; /* firefox */
    -ms-overflow-style: none; /* IE 10+ */
    &::-webkit-scrollbar {
      display: none; /* Chrome Safari */
    }
    height: calc(100% - 3.75rem);
    width: 100%;
    .bar_line {
      width: 95%;
      margin: 14px 10px;
      user-select: none;
      cursor: pointer;
      .el-col {
        padding-top: 10px;
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

.water-gate-sty {
  padding-top: 10px;
  margin-bottom: 10px;
  height: 100%;
  box-sizing: border-box;
  .flex-box {
    display: flex;
    align-items: center;
    justify-content: space-between;
    > div > span {
      margin-right: 10px;
      font-size: 16px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #fcee27;
      line-height: 22px;
    }
    .right-box {
      display: flex;
      align-items: center;
    }
  }
  .table-list {
    height: calc(100% - 50px);
  }
  // background: url(~assets/images/WaterDisastersImage/shui-zha.png) no-repeat top center;
  // background-size: 100% 100%;
  > .area-style {
    background: none;
    p {
      color: #fff;
      font-size: 18px;
      vertical-align: middle;
      > span {
        font-size: 26px;
        color: #1cfff5;
        margin: 0 3px;
      }
    }
    > p:last-child {
      span.yellow {
        color: #eec80b;
      }
    }
  }
  > .area-style:nth-child(1) {
    align-items: center;
  }

  > div:nth-child(1) {
    // border-bottom: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
  }
  > div:nth-child(2) {
    > p:nth-child(1) {
      > i {
        font-size: 17px;
        line-height: 30px;
        margin-top: 2px;
        color: #fff;
      }
      font-size: 16px;
      color: #fff;
      display: flex;
      justify-content: left;
      ::v-deep.el-input {
        width: 32px;
        height: 32px;
      }
      ::v-deep.el-input-number {
        position: relative;
        display: inline-block;
        width: 60px;
        border: 1px solid #ffffff;
        border-radius: 4px;
      }
      ::v-deep.el-input__inner {
        background: none;
        padding: 0;
      }
      ::v-deep.el-input-number__decrease {
        width: 24px;
        border-left: 0.0625rem solid #fff;
        background: none;
        > i {
          color: #fff;
        }
      }
      ::v-deep.el-input-number__increase {
        width: 24px;
        height: 16px;
        border-left: 0.0625rem solid #fff;
        background: none;
        > i {
          color: #fff;
        }
      }
    }
    > p:nth-child(2) {
      font-size: 16px;
      > span {
        padding: 0;
        margin: 0;
      }
    }
  }
}
</style>

<style lang="scss">
.water-gate-sty .capsule-tabs {
  width: 100px;
}
.el-dropdown-menu__item:not(.is-disabled):hover,
.el-dropdown-menu__item:focus {
  background-color: rgba(35, 157, 239, 0.23);
  color: #568ecb;
}
</style>
