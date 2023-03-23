<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-23 17:11:52
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\components\panoramagram.vue
-->
<template>
  <div class="iframe-box">
    <iframe :src="src" frameborder="0"></iframe>
  </div>
</template>

<script>
export default {
  data(){
    return{
      src: ''
    }
  }
}
</script>

<style lang="scss" scoped>
.iframe-box{
  width: 100%;
  height: 100%;
  iframe{
    width: 100%;
    height: 100%;
  }
}
</style>