<!--
 * @Author: hanyu
 * @LastEditTime: 2021-08-05 16:27:49
 * @Description: 台风路径
 * @FilePath: /rscp-big-screen/src/views/bigScreen/reservoir/reservoirRight/ReservoirRightTflj.vue
-->
<template>
  <div class='wrap'>
    ReservoirRightTflj
  </div>
</template>

<script>
export default {
  name: 'ReservoirRightTflj',
  props: {
    
  },
  components: {
    
  },
  data () {
    return {
      
    };
  },
  mounted() {
    this.$parent.$refs.reservoirMap.removeAllLayer();
    this.$parent.$refs.reservoirMap.closePopup();
    this.$parent.$refs.reservoirMap.initView();
  },
  methods: {
    
  }
}
</script>

<style lang='scss' scoped>
  .wrap{
    
  }
</style>