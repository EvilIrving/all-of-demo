<!--
 * @Author: hanyu
 * @LastEditTime: 2022-08-26 10:43:57
 * @Description: 弹窗
 * @FilePath: \rscp-big-screen\src\views\sgzgOtherScreen\index\indexMap\popups\Popups.vue
-->
<template>
  <div class='popups_wrap'>
    <!-- 风险研判弹窗 -->
    <div ref="reservoirPopup">
      <!-- <ReservoirPopup :popupData="pointData" /> -->
    </div>
  </div>
</template>

<script>


export default {
  name: 'Popups',
  props: {
    pointData: {
      type: Object,
      required: true
    }
  },
  components: {

  },
  data() {
    return {

    };
  },

  mounted() {
  },
  methods: {

  }
}
</script>

<style lang='scss' scoped>
.popups_wrap {
  .reservoirPopup {
    width: 300px;
    height: 300px;
    background: #fff;
  }
}
</style>