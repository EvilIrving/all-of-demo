<!--
 * @Author: your name
 * @Date: 2021-06-30 18:17:27
 * @LastEditTime: 2022-09-23 14:04:08
 * @LastEditors: Please set LastEditors
 * @Description: 水库蓄水量
 * @FilePath: \lucheng\src\views\water_disaster_custom\cards\ReservoirStorageCard.vue
-->
<template>
  <div class="water-card">
    <panel-item title="救援队伍" has-switch tc @tclick="tclick">
      <template #title-switch>
        <i title="地图落点" @click="handleMapNet('ldbtn')" :class="[ldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']" />
        <i title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog('救援队伍')" />
      </template>

      <div class="area-box" style="height: 100%; margin-top: 0.5rem">
        <el-table v-loading="reservoiDataLoading" :data="reservoiList" stripe :cell-style="{ fontSize: '15px' }" height="calc(100% - 0.5rem)" :header-cell-style="{ color: '#00BAFF', fontSize: '16px' }">
          <el-table-column label="序号" type="index" width="60"></el-table-column>
          <el-table-column label="救援队伍名称" prop="name"></el-table-column>
          <el-table-column label="现有人数" prop="existingNumber">
            <template slot-scope="scope">
              {{ scope.row.existingNumber || '-' }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </panel-item>
  </div>
</template>

<script>
import PanelItem from '../components/PanelItem.vue';
import { mapGetters, mapActions } from 'vuex';
import { getTeamList } from '@/api/flow';
export default {
  name: 'ReservoirStorageCard',
  layerName: 'digReservoir1Layer',
  legendName: '水库蓄水量',
  components: { PanelItem },
  data() {
    return {
      ldbtn: false,
      reservoiData: {},
      reservoiList: [],
      reservoiDataLoading: true,
      timer: null,
    };
  },
  computed: {
    ...mapGetters(['ScreenMap', 'currentSelectArea', 'previewModule', 'formatterCurrentTime']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  mounted() {
    this.getList();
    this.$nextTick(() => {
      this.timer = setInterval(() => {
        this.getList();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(['SetDetailDialogParam', 'SetReserStorageCount']),
    async getList() {
      let res = await getTeamList({
        pageNum: 1,
        pageSize: 9999
      })
      this.reservoiDataLoading = false
      if (res.code == 0) {
        this.reservoiList = res.data.list
      }
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
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
    // 地图落点
    handleMapNet(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require('@/assets/new_images/point/rescue-team-icon.png'),
        srcs: require('@/assets/new_images/point/rescue-team-juhe.png')
      };
      let pointList = this.reservoiList.map((point) => {
        let imgSrc = require('@/assets/new_images/point/rescue-team-icon.png');
        return {
          name: point.name,
          type: '救援队伍',
          lng: Number(point.longitude),
          lat: Number(point.latitude),
          code: point.id,
          src: imgSrc,
          props: { 现有人数: `${point.existingNumber}人`, 联系方式: `${point.chargePhone}` }
        };
      });
      if (this[btnType]) {
        this.map.drawClusterPoint(pointList, style, 'digReservoir1Layer');
      } else {
        this.map.drawClusterPoint([], {}, 'digReservoir1Layer');
      }
    },
  }
};
</script>

<style lang="scss" scoped>
@import './style/SildePanel.scss';
.water-card {
  height: 100%;

  .icon-sty {
    margin-right: 10px;
  }

  .area-box {
    width: 100%;
    height: 100%;
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
</style>

<style lang="scss">
.water-card .panel-item .panel-item__content--tabs .tab-button,
.water-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.water-card .panel-item .panel-item__content--tabs .tab-button.active,
.water-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
