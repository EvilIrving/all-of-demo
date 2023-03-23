<template>
  <div style="width:100%;height: 100%">
    <WatershedScheduleMap ref="WatershedScheduleMap" @point-click="onPointClick" @set-basin="setBasin" @handle-active="onHandleActive">
      <screen-footer />
    </WatershedScheduleMap>
    <PrimaryDialog ref="PrimaryDialog" :visible='motionDialogShow' :main-title='pointData.name' width="1200px" @handleClose='closeMotionDialogShow'>
      <MotionDialog :colseDialog='colseDialog'  :pointData='pointData' />
    </PrimaryDialog>
  </div>
</template>
<script>
import WatershedScheduleMap from './components/watershedScheduleMap.vue';
import { ProjectDialog, PrimaryDialog, ScreenFooter } from '@/components/';
import RainDialogContent from '@/views/water_disaster_custom/components/StationDialog.vue';
import ScheduleLegend from './components/legends.vue';
import ScheduleCount from './components/scheduleCount.vue';
import MotionDialog from '../dialog/MotionDialog/index.vue'; // 概化图tab页面

import { mapGetters, mapActions } from 'vuex';
export default {
  name: 'WatershedSchedule',
  components: {
    WatershedScheduleMap,
    ProjectDialog,
    RainDialogContent,
    PrimaryDialog,
    ScheduleLegend,
    ScheduleCount,
    ScreenFooter,
    MotionDialog,
  },
  data() {
    return {
      projectDialogVisible: false,
      pointData: {},
      waterRainDialog: false,
      dialogType: '',
      dialogTitle: '',
      basin: '瓯江',
      motionDialogShow: false, // 概化图 水闸弹窗
      colseDialog: false
    };
  },
  computed: {
    ...mapGetters(['IsCollapse', 'BsName'])
  },
  mounted() {
    this.GetSceneList({ bsName: this.BsName, mode: window.__POWERED_BY_QIANKUN__ ? 2 : 1 }).then((res) => {
      if (res) {
        this.SetActiveScene({ id: 'gcght', name: '工程概化图' });
      }
    });
  },
  methods: {
    ...mapActions(['GetSceneList', 'SetActiveScene']),
    setBasin(val) {
      this.basin = val;
    },
    onHandleActive(val) {
      this.$refs.WatershedScheduleMap.drawPointByCount(val);
    },
    //  概化图
    closeMotionDialogShow() {
      this.motionDialogShow = false;
        this.colseDialog = false
    },
    onPointClick(payload) {
      if (payload.label) {
        return false
      }
      this.pointData = payload.detail;
      this.colseDialog = true
      // switch (projectType) {
      //   case 'reservoir':

      //     break;
      //   case 'sluice':
      //     this.pointData = payload[name];
      //     break;
      //   case '测站':
      //     this.pointData = payload[name];
      //     break;
      //   default:
      //     break;
      // }
      this.motionDialogShow = true;

    },
  }
};
</script>
<style>
.project-schedule-map {
  width: 100%;
  height: 100%;
  background: url(~assets/images/schedule-map-bg.png) no-repeat 100%;
}
.project-schedule-map .map {
  width: 100%;
  height: 100%;
}
.schedule-count {
  position: absolute;
  top: 13%;
  left: 50%;
  transform: translateX(-50%);
}
</style>
<style lang="scss" scoped>
.schedule-dialog {
  .primary-dialog {
    ::v-deep .el-dialog {
      position: absolute;
    }
  }
}
</style>
