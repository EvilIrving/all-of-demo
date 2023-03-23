<template>
  <div class="drag-crad__content">
    <panel-item title="预警" :tabs='tabs' @change="handleTabChange">
      <div class='table_box'>
        <div class="table-list" v-show='value==0'>
          <PrimaryMapTable :dataList="dataList" :columnList="columnList" @rowClick="rowClick">
          </PrimaryMapTable>
        </div>
        <div class="table-list" v-show='value==1'>
          <PrimaryMapTable :dataList="dataList" :columnList="forecastList" @rowClick="rowClick">
          </PrimaryMapTable>
        </div>
      </div>
    </panel-item>
  </div>
</template>

<script>
import { PrimaryMapTable } from '@/components/';
import { WorkingConditionsApi } from '@/api/bigScreenPart';
import PanelItem from '@/views/ourPujiang/components/PanelItem';
export default {
  name: 'WarningTable',
  props: {},
  components: {
    PanelItem,
    PrimaryMapTable
  },
  data() {
    return {
      tabs: [
        '实时超警', '预测超警'
      ],
      dataList: [],
      columnList: [
        {
          prop: 'sectionName',
          label: '断面名称'
        },
        {
          prop: 'waterLevel',
          label: '水位(m)'
        },
        {
          prop: 'warningLevel',
          label: '警戒水位(m)',
        },
        {
          prop: 'updateTime',
          label: '更新时间'
        }
      ],
      forecastList: [
        {
          prop: 'sectionName',
          label: '断面名称'
        },
        {
          prop: 'waterLevel',
          label: '水位(m)'
        },
        {
          prop: 'warningLevel',
          label: '警戒水位(m)',
        },
        {
          prop: 'warnTime',
          label: '超警时长'
        }
      ],
      value: '0'
    }
  },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  mounted() {
    this.getList('实时超警')
  },
  methods: {
    async getList(type) {
      let res = await WorkingConditionsApi.overAlert({type});
      if (res.code == 0) {
        if (res.data && res.data.length > 0) {
          this.dataList = res.data.map((item) => {
            if (type === '实时超警') {
              return {
                 sectionName: item.sectionName,
                waterLevel: item.waterLevel,
                warningLevel: item.warningLevel,
                updateTime: item.updateTime,
              };
            } else {
              return {
                sectionName: item.sectionName,
                waterLevel: item.waterLevel,
                warningLevel: item.warningLevel,
                warnTime: item.warnTime,
              };
            }
          });
        } else {
          this.dataList = [];
        }

      }
    },
    rowClick(section) {
      // if (this.isLayerExist(layerName)) this.removeLayerByName('lineLayer');
      // let tempLayer = drawLines(section);
      // tempLayer.set('name', 'lineLayer');
      // tempLayer.setZIndex(99);
      // this.map.addLayer(tempLayer);
      // const selectClick = addLayerClick(tempLayer);
      // selectClick.on('select', function (e) {
      //   var features = e.target.getFeatures().getArray();
      //   if (features.length > 0) {
      //     _this.$emit('point-click', features[0].values_.data);
      //   }
      // });
      // this.map.addInteraction(selectClick);
    },
    handleTabChange(val) {
      this.value = val
    }
  }
};
</script>

<style scoped lang='scss'>
.table-list {
  height: 265px;
}
.drag-crad__content {
  > div {
    height: 100%;
  }
  height: 310px;
  width: 100%;
  display: flex;
  flex-direction: column;
  overflow: scroll;
  // padding: 0 16px;
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
.capsule_tabs {
  margin-left: 15px;
  display: flex;
  flex-direction: column;
}

.drag-card {
  width: 500px;
  color: #fff;
  position: relative;
  background: url(~assets/images/ScreenAsset/drag-big-card-icon.png) no-repeat;
  background-size: 100% 100%;
  background-color: rgba($color: #031a2b, $alpha: 0.9);
  height: 100%;
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
</style>