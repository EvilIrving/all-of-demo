<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-20 14:54:15
 * @Description: 除险加固
 * @FilePath: /lucheng_project/src/views/projectDetail/safeManage/DerInfo.vue
-->
<template>
  <div class='wrap'>
    <iframe :src="iframeUrl" frameborder="0" width="100%" height="100%"></iframe>
  </div>
</template>

<script>
export default {
  name: 'SafeInfo',
  props: {
    
  },
  components: {
    
  },
  data () {
    return {
      iframeUrl: ''
    };
  },
  computed: {
    seawallDetail(){
      return this.$store.state.seawallDetail
    }
  },
  mounted() {
    this.iframeUrl = this.$config.rscpHost + '/mgt/lucheng/derinfo?outsideToken=' + this.seawallDetail.outsideToken
  },
  methods: {
    
  }
}
</script>

<style lang='scss' scoped>
  .wrap{
    width: 100%;
    height: 100%;
  }
</style>