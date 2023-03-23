<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-13 09:44:15
 * @Description: 
 * @FilePath: \big_screen\src\views\checkingSecurity\left\Index.vue
-->
<template>
  <div class="property-box">
    <div class="warp-box property-left-first">
      <div class="warp-title">水情相关</div>
      <div class="line-box">
        <EchartsLine />
      </div>
    </div>
    <div class="warp-box property-left-two">
      <div class="warp-title">暴雨相关</div>
      <div class="bar-box">
        <EchartsBar />
      </div>
    </div>
  </div>
</template>

<script>
import EchartsLine from "./components/EchartsLine.vue";
import EchartsBar from "./components/EchartsBar.vue";
export default {
  components: { EchartsLine, EchartsBar },
};
</script>

<style lang="scss" scoped>
.property-box {
  width: 301px;
  > div {
    width: 100%;
    padding-top: 20px;
  }
  .property-left-first {
    margin-bottom: 10px;
    .grade{
      color: #FCEE27;
      padding-right: 18px;
      font-weight: normal;
      font-size: 18px;
    }
    .line-box {
      width: 100%;
      height: 265px;
    }
  }
  .property-left-two {
    .bar-box {
      width: 100%;
      height: 265px;
    }
  }
}
</style>