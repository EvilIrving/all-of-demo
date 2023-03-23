<template>
  <div>
    <DistributionMap />
    <screen-footer />
  </div>
</template>
<script>
import DistributionMap from './DistributionMap.vue';
import { ProjectDialog, PrimaryDialog, ScreenFooter } from '@/components/';
import RainDialogContent from '@/views/hydraulic_amap/components/dialog/StationDialog.vue';
import { mapGetters, mapActions } from 'vuex';
export default {
  name: 'WatershedSchedule',
  components: {
    DistributionMap,
    ProjectDialog,
    RainDialogContent,
    PrimaryDialog,
    ScreenFooter
  },
  data() {
    return {
      projectDialogVisible: false,
      pointData: {},
      waterRainDialog: false,
      dialogType: '',
      dialogTitile: ''
    };
  },
  computed: {
    ...mapGetters(['IsCollapse', 'BsName'])
  },
  mounted() {
    // this.GetSceneList({ bsName: this.BsName, mode: window.__POWERED_BY_QIANKUN__ ? 2 : 1 }).then((res) => {
    //   if (res.code == 0) {
    //     this.SetActiveScene({ id: 'lygcdd', name: '流域工程调度' });
    //   }
    // });
    this.$nextTick(() => {
      this.GetSceneList({ bsName: this.BsName, mode: window.__POWERED_BY_QIANKUN__ ? 2 : 1 }).then((res) => {
        this.SetActiveScene({ id: 'lygcdd', name: '流域工程调度' });
      });
    });
  },
  methods: {
    ...mapActions(['GetSceneList', 'SetActiveScene', 'SetActiveCount', 'GetSceneComponentInfo']),
    onPointClick(payload) {
      console.log(payload);
      if (payload.type && payload.code) {
        let { type } = payload;
        if (type == '水闸' || type.indexOf('水库') != -1) {
          this.pointData = payload;
          this.projectDialogVisible = true;
        } else if (type == '潮位') {
          this.dialogType = 'TT';
          this.pointData = { stCode: payload.code, stType: 'TT', ...payload };
          this.dialogTitile = payload.name;
          this.waterRainDialog = true;
          this.$nextTick(() => {
            this.$refs.rainDialog.getWaterStationInfo();
          });
        } else if (type == '水文') {
          this.dialogType = 'ZZ';
          this.pointData = { stCode: payload.code, stType: 'ZZ', ...payload };
          this.dialogTitile = payload.name;
          this.waterRainDialog = true;
          this.$nextTick(() => {
            this.$refs.rainDialog.getWaterStationInfo();
          });
        }
      }
    },
    handleProjectDialogClose() {
      this.projectDialogVisible = false;
    },
    handleWaterRainClose() {
      this.waterRainDialog = false;
    }
  }
};
</script>
<style>
.watershed-schedule {
  width: 100%;
  height: 100%;
  background: #000;
}
.watershed-schedule .map {
  width: 100%;
  height: 100%;
}
.schedult-count {
  position: absolute;
  top: 13%;
  left: 50%;
  transform: translateX(-50%);
}
</style>
