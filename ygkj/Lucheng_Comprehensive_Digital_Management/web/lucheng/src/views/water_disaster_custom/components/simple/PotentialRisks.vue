<template>
  <div ref="dragCard" class="drag-card collapse-panel__border drag-box" :class="{ 'is-big-screen': isBigScreen, 'drag-risk-card': riskIcon }">
    <div class="drag-crad__content">
      <panel-item title="工程列表">
        <div class="risks-box">
          <!-- <primary-map-table :columnList="columnList" :tableIndex="tableIndex" :dataList="dataList" :tableLoading="tableLoading"></primary-map-table> -->
          <v-table :tableData="dataList" :dynamicColumnSetting="columnList" @rowClick="seawallClick">
          </v-table>
        </div>
      </panel-item>
    </div>
    <div class="risk-left-icon" :class="{ 'risk-icon': riskIcon }" @click="riskChange()"></div>
    <MapPoint ref="point" />
  </div>
</template>

<script>
import { PrimaryMapTable } from "@/components/";
import VTable from '../../dialog/DetailsCommonDialog/components/table/VTable'
import PanelItem from '../PanelItem.vue';
import MapPoint from './components/MapPoint.vue';
import { mapActions, mapGetters } from 'vuex';
import { alarmPeople } from '@/api/bigScreenPart';
export default {
  name: 'DragCard',
  components: {
    PanelItem,
    PrimaryMapTable,
    VTable,
    MapPoint
  },
  data() {
    return {
      alarmTimes: '',
      riskIcon: false,
      alarmChecked_a: false,
      alarmChecked_b: false,
      alarmChecked_c: false,
      alarmChecked_d: false,
      alarmChecked_e: false,
      seawallAlarm: {},
      seawallWarningData: {},
      displacementList: [],
      sedimentationList: [],
      typhoonData: [],
      columnList: [
        {
          prop: "seawallName",
          label: "工程名称",
        },
      ],
      tableIndex: 0,
      dataList: [],
      tableLoading: false, // 表格loading
    };
  },
  mounted() {
    this.getSeaWallList()
    // this.SetTempZindex();
    // this.seawallGiveAnAlarm();
    // this.seawallWarning();
    // this.typhoonInfluence();
    // this.displacement(1);
    // this.displacement(2);
    // this.$nextTick(() => {
    //   this.$refs.dragCard.style.zIndex = this.TempZIndex;
    // });
  },
  beforeDestroy() {
    this.map.removeLineLayer();
    this.map.removeFocusLineLayer();
  },
  computed: {
    ...mapGetters(['TempZIndex', 'isBigScreen', 'simpleItem', 'simpleRangTime']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  watch: {
    simpleItem: function (val) {
      if (!val) {
        this.alarmChecked_a = false;
        this.alarmChecked_b = false;
        this.alarmChecked_c = false;
        this.alarmChecked_d = false;
        this.alarmChecked_e = false;
      }
    },
    simpleRangTime: async function (val) {
      if (this.alarmChecked_a) {
        await this.seawallGiveAnAlarm();
        await this.seawallHand(this.seawallAlarm.list);
        await this.setSimplePotenData(this.seawallAlarm);
      } else if (this.alarmChecked_b) {
        await this.seawallWarning();
        await this.seawallHand(this.seawallWarningData.list);
        await this.setSimplePotenData(this.seawallWarningData);
      } else if (this.alarmChecked_c) {
        await this.displacement(1, true);
        await this.setSimplePotenData(this.displacementList);
      } else if (this.alarmChecked_d) {
        await this.displacement(2, true);
        await this.setSimplePotenData(this.sedimentationList);
      } else {
        await this.typhoonInfluence();
        await this.setSimplePotenData(this.typhoonData);
      }
    }
  },
  methods: {
    ...mapActions(['SetTempZindex', 'setSimpleRightType', 'setSimpleItem', 'setSimpleTimes', 'setSimplePotenData']),
    riskChange() {
      this.riskIcon = !this.riskIcon;
    },
    async getSeaWallList() {
      let res = await alarmPeople.seawallGiveAnAlarm();
      if (res.code === 0) {
        res.data.list.map(i => {
          i.type = '海塘'
          i.name = i.seawallName
          i.code = i.seawallCode
          i.leftClick = true
        })
        this.dataList = res.data.list;
      }
    },
    seawallClick(row) {
      this.$parent.showDialog(row)
    }
  }
};
</script>

<style lang="scss" scoped>

.drag-crad__content {
  > div {
    height: 100%;
  }
  width: 100%;
  display: flex;
  flex-direction: column;
  overflow: scroll;
  padding: 0 16px;
  ::v-deep .panel-item__content {
    display: flex;
    flex-direction: column;
    &::-webkit-scrollbar {
      display: none;
    }
  }

  &::-webkit-scrollbar {
    display: none;
  }
}

.drag-card {
  z-index: 1000;
  width: 400px;
  color: #fff;
  position: relative;
  background: url(~assets/images/ScreenAsset/drag-big-card-icon.png) no-repeat;
  background-size: 100% 100%;
  background-color: rgba($color: #031a2b, $alpha: 0.9);
  top: 134px;
  height: 75%;
  .risk-left-icon {
    position: absolute;
    width: 37px;
    height: 69px;
    background: url(~assets/images/ScreenAsset/risk-left-icon.png) no-repeat;
    background-size: 100% 100%;
    right: -37px;
    top: 50%;
    margin-top: -35px;
    cursor: pointer;
    &::after {
      content: '';
      width: 10px;
      height: 18px;
      background: url(~assets/images/ScreenAsset/risk-left-array-icon.png) no-repeat;
      background-size: 100% 100%;
      position: absolute;
      top: 50%;
      left: 50%;
      margin: -9px 0 0 -8px;
    }
    &.risk-icon {
      &::after {
        content: '';
        background: url(~assets/images/ScreenAsset/risk-right-array-icon.png) no-repeat;
        background-size: 100% 100%;
      }
    }
  }
}
.drag-risk-card {
  width: 0;
}

.risks-box {
  padding-top: 20px;
  .risk-top {
    display: flex;
    color: #ffffff;
    font-size: 14px;
    align-items: center;
    justify-content: space-between;
    .risk-daterange {
      width: 252px;
      height: 32px;
      border: 1px solid #ffffff;
      background-color: transparent;
    }
    .reset-btn {
      width: 60px;
      height: 32px;
      border-radius: 4px;
      border: 1px solid #00baff;
      line-height: 32px;
      text-align: center;
      color: #00baff;
      cursor: pointer;
    }
  }
  .risk-content {
    > div {
      margin-top: 24px;
      background-color: rgba(7, 157, 254, 0.06);
      display: flex;
      align-items: center;
      padding-left: 20px;
      height: 64px;
      font-size: 32px;
      cursor: pointer;
      &.active {
        background-color: rgba(7, 157, 254, 0.28);
        border: 2px solid rgba(72, 152, 251, 0.41);
      }
      .checkbox {
        width: 24px;
        height: 24px;
      }
      img {
        width: 32px;
        height: 32px;
        margin: 0 19px 0 12px;
      }
      .risk-num {
        padding-left: 20px;
        color: #ff0000;
      }
    }
  }
}
</style>

<style lang="scss">
.risk-daterange {
  .el-icon-date {
    color: #fff;
    line-height: 27px;
  }
  .el-range-separator {
    color: #fff;
    line-height: 24px;
    padding: 0;
  }
  .el-range-input {
    background-color: transparent;
    color: #fff;
  }
}
.risk-content {
  > div {
    .checkbox {
      .el-checkbox__inner {
        width: 24px;
        height: 24px;
        background-color: transparent;
      }
      .el-checkbox__inner::after {
        width: 8px;
        height: 13px;
        border-color: #00baff;
        left: 50%;
        margin-left: -4px;
      }
    }
  }
}
</style>
