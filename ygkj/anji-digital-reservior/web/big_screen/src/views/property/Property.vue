<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-12 14:33:43
 * @Description: 
 * @FilePath: \big_screen\src\views\property\Property.vue
-->
<template>
  <div class="warp flex-box">
    <section class="property-left">
      <PropertyLeft />
    </section>
    <PropertyMap />
    <section class="property-right">
      <PropertyRight />
    </section>
  </div>
</template>

<script>
import PropertyMap from "./propertyMap/PropertyMap.vue";
import PropertyLeft from "./propertyLeft/LeftIndex.vue";
import PropertyRight from "./propertyRight/RightIndex.vue";
export default {
  name: "property",
  components: {
    PropertyMap,
    PropertyRight,
    PropertyLeft,
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