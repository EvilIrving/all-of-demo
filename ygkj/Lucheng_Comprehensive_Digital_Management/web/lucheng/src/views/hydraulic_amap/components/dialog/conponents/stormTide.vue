<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-11-03 19:17:17
 * @Description: 
 * @FilePath: \lucheng\src\views\hydraulic_amap\components\dialog\conponents\stormTide.vue
-->
<template>
  <div class="iframe-box" style="">
    <iframe style="width: 100%; height: 100%" :src="src" frameborder="0"></iframe>
  </div>
</template>

<script>
export default {
  props: {
    disasterData: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      typhoon: 'https://slgh.lucheng.gov.cn/storm/chanpin/hyzh/surge/c-surge/',
      temperate: 'https://slgh.lucheng.gov.cn/storm/chanpin/hyzh/surge/c-surge1/',
      src: ''
    };
  },
  mounted() {
    if (this.disasterData.stationCode == 1) {
      this.src = this.typhoon + this.disasterData.code;
    } else {
      this.src = this.temperate + this.disasterData.code;
    }
  }
};
</script>

<style lang="scss" scoped>
.iframe-box {
  width: 100%;
  height: 100%;
  padding-top: 16px;
  background-color: #fff;
}
</style>