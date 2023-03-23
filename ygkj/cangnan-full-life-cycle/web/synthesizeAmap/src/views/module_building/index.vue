<template>
  <div class="module-building">
    <div class="pic-area"></div>
    <p>当前模块建设中</p>
  </div>
</template>

<script>
export default {
  name: 'ModuleBuilding',
  data() { 
    return {

    }
  }
 }
</script>

<style lang="scss" scoped>
  .module-building{
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0) url(~assets/images/bigScreenBg.jpg) no-repeat center center;
    @include flexbox;
    @include flexAC;
    @include flexJC(center);
    @include flexflow(column,nowrap);
    .pic-area{
      width: 700px;
      height: 550px;
      background: url(~assets/images/model-building.png) no-repeat center center;
    }
    p{
      opacity: 0.7;
      font-family: PingFangSC-Medium;
      font-size: 28px;
      color: #FFFFFF;
      line-height: 35px;
      font-weight: 500;
      position: relative;
      top: -90px;
    }
  }
</style>