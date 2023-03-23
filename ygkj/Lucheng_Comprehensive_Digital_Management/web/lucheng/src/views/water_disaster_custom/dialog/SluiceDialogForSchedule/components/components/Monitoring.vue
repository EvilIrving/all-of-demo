<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-08 14:07:11
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\SluiceDialogForSchedule\components\components\Monitoring.vue
-->
<!--
 * @Date: 2022-04-02 13:35:34
 * @Author: dtb
 * @Description: 
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-07 16:48:24
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
      <water v-if="tabAction == 0" :type="type" :pointData="pointData" />
      <rainfall v-if="tabAction == 1" :type="type" :pointData="pointData" />
      <ProjectVideo v-if="tabAction == 2" :code="pointData.projectCode" />
    </div>
  </div>
</template>

<script>
import VTable from '../../common/VTable.vue';
import ProjectVideo from '@/components/screen_project_dialog/components/ProjectVideo.vue';
import water from './water.vue';
import rainfall from './rainfall.vue';
export default {
  props: {
    pointData: {
      type: Object,
      default: {}
    },
    type: {
      type: String,
      default: ''
    }
  },
  components: { VTable, ProjectVideo, water, rainfall, },
  data() {
    return {
      tabList: [
        { name: '水位', cursor: false },
        { name: '雨量', cursor: false },
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