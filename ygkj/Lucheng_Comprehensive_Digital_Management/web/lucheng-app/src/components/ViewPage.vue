<template>
  <div class="view-page">
    <header-nav-bar></header-nav-bar>
    <div class="view-box">
      <slot></slot>
    </div>
  </div>  
</template>

<script>
import headerNavBar from './headerNavBar.vue'
export default {
  components:{headerNavBar}
}
</script>

<style lang='scss' scoped>
.view-page{
  height: 100%;
  .view-box{
    height: calc(100% - 46px);
  }
}
</style>