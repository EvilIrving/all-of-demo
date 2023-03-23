<!--
 * @Date: 2022-04-02 13:35:34
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-05-23 13:58:29
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\SluiceDialogForSchedule\index.vue
-->
<template>
  <div>
    <sluice v-if="pointData.projectType === 'sluice'" :pointData="pointData" />
    <reservoir v-if="pointData.projectType === 'reservoir'" :pointData="pointData" />
    <embankment v-if="pointData.projectType === 'dike'" :pointData="pointData" />
    <seawall v-if="pointData.projectType === 'seawall'" :seawallData="pointData" />
  </div>
</template>

<script>
/** 自定义iframe */
import sluice from './views/sluice.vue'
import reservoir from './views/reservoir.vue'
import embankment from './views/embankment.vue'
import Seawall from '../../dialog/DetailsCommonDialog/components/cardListDetail/seawallCardList/Index.vue'
export default {
  name:'scheduleDialog',
  props: {
    pointData: {
      type: Object,
      default: {}
    }
  },
  components: {
    sluice,
    reservoir,
    embankment,
    Seawall
  },
  mounted() {
    console.log(this.pointData,'弹窗',this.pointData.projectType);
  },
};
</script>