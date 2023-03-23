<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-12 17:31:00
 * @Description: 
 * @FilePath: \big_screen\src\views\property\propertyRight\RightIndex.vue
-->
<template>
  <div class="property-box">
    <div class="warp-box property-left-first">
      <div class="warp-title">工程巡查</div>
      <div class="line-box">
        <div class="radar-title">汛期非汛期巡查频次</div>
        <RadarIndex />
      </div>
    </div>
    <div class="warp-box property-left-two">
      <div class="warp-title">异常信息展示</div>
      <div class="bar-box">
        <AberrantIndex />
      </div>
    </div>
    <div class="warp-box property-left-three">
      <div class="warp-title">维修养护</div>
      <div class="bar-box">
        <MaintainIndex />
      </div>
    </div>
  </div>
</template>

<script>
import AberrantIndex from "./aberrant/AberrantIndex.vue";
import MaintainIndex from "./maintain/MaintainIndex.vue";
import RadarIndex from "./EchartsRadar/RadarIndex.vue";
export default {
  components: {
    AberrantIndex,
    MaintainIndex,
    RadarIndex,
  },
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
    // height: 245px;
    .line-box {
      width: 100%;
      height: 245px;
      position: relative;
      .radar-title {
        position: absolute;
        top: 12px;
        padding: 5px 0;
        color: #fff;
        font-size: 14px;
        width: 134px;
        box-sizing: border-box;
        margin-left: 20px;
        text-align: center;
        background: rgba(255, 255, 255, 0.06);
        border: 1px solid;
        border-image: linear-gradient(
            225deg,
            rgba(50, 197, 255, 0),
            rgba(50, 197, 255, 1),
            rgba(0, 251, 255, 0)
          )
          1 1;
      }
    }
  }
  .property-left-two {
    margin-bottom: 10px;
    .bar-box {
      width: 100%;
      padding-bottom: 15px;
    }
  }
  .property-left-three {
    .bar-box {
      width: 100%;
      padding-bottom: 15px;
    }
  }
}
</style>