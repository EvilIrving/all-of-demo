<!--
 * @Date: 2022-04-02 13:35:34
 * @Author: dtb
 * @Description: MotionMotion
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-09-05 10:58:12
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\MotionDialog\index.vue
-->
<template>
  <div>
    <!-- 水闸 -->
    <sluice v-if="pointData.codeType == '水闸工程' && colseDialog" :pointData="pointData" />
    <!-- 水库 -->
    <reservoir v-if="pointData.codeType == '水库工程' && colseDialog" :pointData="pointData" />
    <!-- 河道水位测站 -->
    <running-info
      v-if="(pointData.codeType == '河道测站' || pointData.codeType == '水库测站') && colseDialog"
      :pointData="pointData"
      :type="pointData.extraInfo.stType"
      :stationCode="pointData.code"
    />
  </div>
</template>

<script>
import sluice from '../MotionSluiceDialog.vue';
import reservoir from './views/reservoir.vue';
import RunningInfo from '@/components/screen_project_dialog/components/RunningInfo.vue';
export default {
  name: 'MotionDialog',
  data() {
    return {};
  },
  props: {
    pointData: {
      type: Object,
      default: {}
    },
    colseDialog: {
      type: Boolean,
      default: false
    }
  },
  components: {
    sluice,
    reservoir,
    RunningInfo
  },
  mounted() {}
};
</script>