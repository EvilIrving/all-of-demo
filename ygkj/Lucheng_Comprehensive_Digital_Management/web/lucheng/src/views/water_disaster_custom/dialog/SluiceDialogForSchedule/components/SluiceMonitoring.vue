<!--
 * @Date: 2022-04-02 13:35:34
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-05-23 14:01:44
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\SluiceDialogForSchedule\components\SluiceMonitoring.vue
-->
<template>
  <div class="danger-box">
    <div class="danger-tap">
      <div
        :class="{ set_cursor: Boolean(item.cursor), active: index == tabAction }"
        v-for="(item, index) in tabList"
        :key="item.name"
      >
        <div @click="checkTab(index)">{{ item.name }}</div>
      </div>
    </div>
    <div class="table-box">
      <water v-if="tabAction == 0" :pointData="pointData" />
      <hyetal v-if="tabAction == 1" :pointData="pointData" />
      <flow v-if="tabAction == 2" :pointData="pointData" />
      <ProjectVideo v-if="tabAction == 3" :code="pointData.projectCode" />
    </div>
  </div>
</template>

<script>
import VTable from '../common/VTable.vue';
import ProjectVideo from '@/components/screen_project_dialog/components/ProjectVideo.vue';
import water from './components/water.vue';
import flow from './components/flow.vue';
import hyetal from './components/hyetal.vue';
import waterQuality from './components/waterQuality.vue';
import displacement from './components/displacement.vue';
import sedimentation from './components/sedimentation.vue';
export default {
  name:'SluiceMonitoring',
  props: {
    pointData: {
      type: Object,
      default: {}
    }
  },
  components: { VTable, ProjectVideo, water, waterQuality, hyetal, flow, displacement, sedimentation },
  data() {
    return {
      tabList: [
        { name: '闸上水位', cursor: false },
        { name: '闸下水位', cursor: false },
        { name: '流量', cursor: false },
        { name: '视频', cursor: false },
      ],
      tabAction: 0
    };
  },
  mounted() {
    
  },
  methods: {
    checkTab(index) {
      this.tabAction = index;
    },
  }
};
</script>

<style lang="scss" scoped>
.danger-box {
  padding-top: 16px;
  .danger-tap {
    display: flex;
    > div {
      width: 114px;
      height: 32px;
      border: 1px solid rgba(255, 255, 255, 0.6);
      line-height: 32px;
      text-align: center;
      margin-right: 16px;
      color: rgba(255, 255, 255, 0.8);
      cursor: pointer;
      &.active {
        color: #00baff;
        background: rgba(72, 152, 251, 0.1);
        box-shadow: 0px 0px 5px 0px #00baff inset;
        border-color: rgba(0, 186, 255, 0.6);
      }
      div {
        width: 100%;
        height: 100%;
      }
      &.set_cursor {
        cursor: not-allowed;
      }
      &.set_cursor div {
        pointer-events: none;
      }
    }
  }
}
.table-box {
  margin-top: 16px;
  height: 530px;
}
</style>