<template>
  <div class="rain-forecast-card">
    <panel-item title="工情" has-switch tc @tclick="tclick">
      <template #title-switch>
        <i v-if="!showDetail" title="地图落点" @click.stop="handleWorkConditionChange('szbtn')" :class="[szbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']" />
        <i title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog('工情')" />
      </template>
      <!-- 工情 -->
      <div class="bar_box" v-if="!showDetail">
        <el-row v-for="(item, index) in rankingData" :key="index" :gutter="14" class="bar_line" @click.native="typeTabChange(item)">
          <el-col :span="21">{{ `${item.name}` }}</el-col>
          <el-col :span="3">
            <span :style="{ color: '#47F5A7', fontSize: '16px' }">{{ item.value }}</span>
            <span>/{{ item.total }}</span>
          </el-col>
          <el-col :span="24">
            <el-progress :show-text="false" :stroke-width="4" :percentage="getPercent(item)" :color="'#47F5A7'" />
          </el-col>
        </el-row>
      </div>
      <div class="water-gate-sty" v-if="showDetail">
        <div class="flex-box">
          <CapsuleTabs :tabs="['返回']" @change="handleTabChange" />
          <div>
            <el-dropdown placement="bottom-start" trigger="click">
              <span class="el-dropdown-link" :class="yearDropdownVisible ? 'dropdown-active' : ''">
                <span style="font-size: 1rem">{{ statusLabel }}</span>
                <i class="el-icon-caret-bottom"></i>
              </span>
              <el-dropdown-menu slot="dropdown" class="year-dropdown">
                <el-dropdown-item v-for="(item, index) in statusOption" :key="index">
                  <el-radio v-model="status" :label="item.value" @change="statusChange">{{ item.label }}</el-radio>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
        <div class="table-list">
          <primary-map-table :columnList="tableColumns" :dataList="dataList" @rowClick="rowClick" :paginationFlag="false"></primary-map-table>
        </div>
      </div>
    </panel-item>
  </div>
</template>

<script>
import PanelItem from '../components/PanelItem.vue';
import CapsuleTabs from '@/views/WaterDisasterPreventionScreen/components/CapsuleTabs.vue';
import { WorkingConditionsApi } from '@/api/bigScreenPart';
import { PrimaryMapTable } from '@/components/';
import { mapGetters, mapActions } from 'vuex';
import { cloneObj } from '@/utils/utils';
export default {
  name: 'WorkConditionCard',
  layerName: ['workSluiceLayer', 'workReservoirLayer', 'workPumpLayer'],
  legendName: ['水库', '水闸', '泵站'],
  components: {
    PanelItem,
    PrimaryMapTable,
    CapsuleTabs
  },
  mounted() {
    this.getOverView();
    this.timer = setInterval(() => {
      this.getOverView();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.legendOptions.map((item) => {
      this.setHealthCodeLegend(item.name);
    });
    this.setPoint([], 'reservoir', 'normal', false);
    this.setPoint([], 'reservoir', 'warn', false);
    this.setPoint([], 'pump', 'normal', false);
    this.setPoint([], 'pump', 'warn', false);
    this.setPoint([], 'sluice', 'normal', false);
    this.setPoint([], 'sluice', 'warn', false);
  },
  data() {
    return {
      // 工情 列表 类型
      workConditionType: '',
      timer: null,
      showDetail: false,
      szbtn: false, //工情点位展示标记
      tableColumns: [
        {
          prop: 'stName',
          label: '名称'
        },
        {
          prop: 'gateOpened',
          label: '启闭',
          width: 60
        },
        {
          prop: 'll',
          label: '流量(m³/s)'
        }
      ],
      dataList: [],
      legendOptions: [
        {
          type: 1,
          value: 'work-condition-sluice',
          name: '水闸',
          isShow: false,
          mark: 'wr'
        },
        {
          type: 2,
          value: 'work-condition-reservoir',
          name: '水库',
          isShow: false,
          mark: 'wr'
        },
        {
          type: 3,
          value: 'work-condition-pump',
          name: '泵站',
          isShow: false,
          mark: 'wr'
        }
      ],
      activeTab: 0,
      rankingData: [
        {
          name: '水闸开启数量',
          label: '水闸',
          value: 0,
          total: 0
        },
        {
          name: '水库开启数量',
          label: '水库',
          value: 0,
          total: 0
        },
        {
          name: '泵站开启数量',
          label: '泵站',
          value: 0,
          total: 0
        }
      ],
      yearDropdownVisible: false,
      statusOption: [
        { value: '1', label: '开启' },
        { value: '2', label: '关闭' }
      ],
      status: '1',
      statusLabel: '开启',
      reservoirList: [],
      sluiceList: [],
      pumpList: []
    };
  },
  computed: {
    ...mapGetters(['currentSelectArea', 'previewModule', 'formatterCurrentTime']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  methods: {
    ...mapActions(['changeScreenLoading', 'getForecastRainfallLayer', 'SetDetailDialogParam']),
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
    },
    // 获取工情统计数据
    async getOverView() {
      this.$forceUpdate();
      await WorkingConditionsApi.workingConditionsTotal().then((res) => {
        if (res.code == 0) {
          this.rankingData[0].value = res.data.wagaOpenNum;
          this.rankingData[0].total = res.data.wagaNum;
          this.rankingData[1].value = res.data.rsvrOpenNum;
          this.rankingData[1].total = res.data.rsvrNum;
          this.rankingData[2].value = res.data.pustOpenNum;
          this.rankingData[2].total = res.data.pustNum;
          this.$forceUpdate();
        }
      });
    },
    // 点击进度条 进入类型列表
    async typeTabChange(item) {
      this.showDetail = true;
      this.workConditionType = item.label;
      this.getRightMapList(item.label, '1');
    },
    // 返回进度条信息
    handleTabChange(val) {
      this.showDetail = false;
      this.status = '1';
      this.statusLabel = '开启';
      // this.getOverView();
    },
    // 在第二级列表 选择 选择开启关闭
    statusChange(val) {
      let data = this.statusOption.find((item) => item.value == val);
      this.statusLabel = data.label;
      this.getRightMapList(this.workConditionType, val);
    },
    // 点击列表 同 落点
    rowClick(data) {
      this.legendOptions.map((item) => {
        this.setHealthCodeLegend(item.name);
      });
      this.setPoint([], 'reservoir', 'normal', false);
      this.setPoint([], 'reservoir', 'warn', false);
      this.setPoint([], 'pump', 'normal', false);
      this.setPoint([], 'pump', 'warn', false);
      this.setPoint([], 'sluice', 'normal', false);
      this.setPoint([], 'sluice', 'warn', false);
      this.setMapPlace([data.data], this.workConditionType);
    },
    // 打开详情弹框点击事件
    openDetailsDialog(val) {
      this.SetDetailDialogParam({
        mediumSized: '',
        codeTpye: val,
        analyseDialogShow: true
      });
    },
    setMapPlace(arr, type) {
      let sluiceList = [];
      let reservoirList = [];
      let pumpList = [];
      arr.map((item) => {
        let obj;
        if (type == '水闸') {
          obj = {
            name: item.wagaName,
            lat: item.stLat || item.startLat,
            lng: item.stLong || item.startLong,
            type: '工情',
            code: item.stCode ? item.stCode : item.wagaCode,
            stCode: item.stCode,
            isOpened: item.gateOpened,
            stType: 'DD',
            projectType: 'sluice',
            props: {
              水闸名称: item.wagaName,
              闸上水位: item.upz,
              闸下水位: item.dwz,
              过闸流量: item.ll,
              闸门开度: item.ovs
            }
          };
          sluiceList.push(obj);
        } else if (type == '水库') {
          obj = {
            name: item.resName,
            lng: item.lowLeftLong,
            lat: item.lowLeftLat,
            isOpened:item.rsvrOpened,
            type: '工情',
            code: item.mainStcd,
            stType: 'RR',
            projectType: 'reservoir',
            props: {
              水库名称: item.resName,
              水位: item.waterLevel,
              下泄流量: item.xxll
            }
          };
          reservoirList.push(obj);
        } else {
          obj = {
            name: item.projectName,
            isOpened:item.pustOpened,
            lat: item.latitude ,
            lng: item.longitude ,
            type: '工情',
            code: item.projectId,
            stType: 'PT',
            projectType: 'pump',
            props: {
              泵站名称: item.wagaName,
              进水池水位: item.jscZ,
              出水池水位: item.cscZ,
              流量: item.sum,
              水泵启闭: item.projectName
            }
          };
          pumpList.push(obj);
        }
      });
      if (type == '水闸') {
        let openlist = []
        let closedlist = []
        sluiceList.forEach(i => {
          // 开启用绿色 关闭用黄色
          if (i.isOpened) {
            openlist.push(i)
          } else {
            closedlist.push(i)
          }
        })
        this.setPoint(openlist, 'sluice', 'normal', true);
        this.setPoint(closedlist, 'sluice', 'warn', true);
      } else if (type == '水库') {
        let openlist = []
        let closedlist = []
        reservoirList.forEach(i => {
          if (i.isOpened) {
            openlist.push(i)
            } else {
            closedlist.push(i)
          }
        })
        this.setPoint(openlist, 'reservoir', 'normal', true);
        this.setPoint(closedlist, 'reservoir', 'warn', true);
      } else {
        let openlist = []
        let closedlist = []
        pumpList.forEach(i => {
          if (i.isOpened) {
            openlist.push(i)
            } else {
            closedlist.push(i)
          }
        })
        this.setPoint(openlist, 'pump', 'normal', true);
        this.setPoint(warnList, 'pump', 'warn', true);
      }
    },
    /**
     * 工情list  根据type获取不同类型
     */
    async getRightMapList(type, status) {
      let opt = { pjtp: type, openStatus: status };
      let res = await WorkingConditionsApi.workingConditionsList(opt);
      if (res.code == 0) {
        if (res.data && res.data.length > 0) {
          this.dataList = res.data.map((item) => {
            return {
              stName: item.wagaName || item.resName,
              gateOpenedText: item.gateOpened ? '开启' : '关闭',
              ll: item.ll || item.xxll,
              data: item
            };
          });
        } else {
          this.dataList = [];
        }
        if (!status && res.data && res.data.length > 0) {
          let arr = res.data;
          this.setMapPlace(arr, type);
        }
      }
    },

    /**
     * 工情点位开关
     */
    handleWorkConditionChange(btnType) {
      this[btnType] = !this[btnType];
      this.reservoirList = [];
      this.sluiceList = [];
      this.pumpList = [];
      if (this[btnType]) {
        this.getRightMapList('水库');
        this.getRightMapList('水闸');
        this.getRightMapList('泵站');
        this.legendOptions.map((item) => {
          this.setHealthCodeLegend(item.name, item.mark);
        });
      } else {
        this.legendOptions.map((item) => {
          this.setHealthCodeLegend(item.name);
        });
        this.setPoint([], 'reservoir', 'normal', false);
        this.setPoint([], 'reservoir', 'warn', false);
        this.setPoint([], 'pump', 'normal', false);
        this.setPoint([], 'pump', 'warn', false);
        this.setPoint([], 'sluice', 'normal', false);
        this.setPoint([], 'sluice', 'warn', false);
      }
    },
    /**
     * @description: 设置工情图例
     * @param {String} name 显示图例名称
     * @param {String} value 工情标记 'wc' 避免冲突
     * @return {*}
     */
    setHealthCodeLegend(name, value) {
      if (value) {
        this.$emit('legend-change', name, {
          label: name,
          children: [
            {
              label: '开启',
              icon: 'icon-kaiqi'
            },
            {
              label: '关闭',
              icon: 'icon-guanbi'
            }
          ]
        });
      } else {
        this.$emit('legend-change', name, {});
      }
    },
    /**
     * @description: 获取点位样式对象
     * @param {String} projectType 工程类别en 可根据tabCorrType与当前选中activeTab获取
     * @param {String} color 可传点位颜色 green,red,yellow
     * @return {Object}
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
         * @param {*} status 开启状态
         * @param {*} flag 是否设置样式
         * @return {*}
       */
    setPoint(pointList, type, status, flag) {
      let pointStyle = flag ? this.getPointStyle(type, status) : {};
      let typeUpper = type.charAt(0).toUpperCase() + type.slice(1);
      // let statusUpper = type.charAt(0).toUpperCase() + type.slice(1);
      let layer = typeUpper + status;
      this.map.drawClusterPoint(pointList, pointStyle, `work${layer}Layer`);
    },

    // 计算百分比
    getPercent(item) {
      return item.value != 0 ? Number((item.value / item.total) * 100) : 0;
    }
  }
};
</script>

<style lang="scss" scoped>
@import './style/SildePanel.scss';
.rain-forecast-card {
  height: 100%;
  .bar_box {
    position: absolute;
    // overflow: hidden scroll;
    overflow: auto;
    height: calc(100% - 3.75rem);
    width: 100%;
    // 隐藏滚动条  overflow显示设置为auto或者scroll保证内容是可滚动的。
    scrollbar-width: none; /* firefox */
    -ms-overflow-style: none; /* IE 10+ */
    &::-webkit-scrollbar {
      display: none; /* Chrome Safari */
    }

    .bar_line {
      width: 95%;
      margin: 18px 10px;
      user-select: none;
      cursor: pointer;
      .el-col {
        margin-top: 4px;
        padding-top: 12px;
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
  }
  .table-list {
    height: calc(100% - 55px);
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
