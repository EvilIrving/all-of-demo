<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-13 09:35:02
 * @Description: 
 * @FilePath: \big_screen\src\views\checkingSecurity\CheckingSecurity.vue
-->
<template>
  <div class="warp flex-box">
    <section class="property-left">
      <LeftIndex />
    </section>
    <Map />
    <section class="property-right">

    </section>
  </div>
</template>

<script>
import Map from "./map/MapIndex.vue"
import LeftIndex from './left/Index.vue'
export default {
  name: "property",
  components: {
    Map,
    LeftIndex,
  },
};
</script>

<style lang="scss" scoped>
.warp {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  position: relative;
  .property-left {
    position: absolute;
    left: 0;
    top: 56px;
    z-index: 22;
  }
  .property-right{
    position: absolute;
    right: 0;
    top: 56px;
    z-index: 22;
  }
}
</style>