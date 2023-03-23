<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-19 10:13:17
 * @Description: 形象面貌
 * @FilePath: /lucheng_project/src/views/projectDetail/baseMsg/ImageMsg.vue
-->
<template>
  <div class='wrap'>
    <iframe :src="iframeUrl" frameborder="0" width="100%" height="100%"></iframe>
  </div>
</template>

<script>
export default {
  name: 'ImageMsg',
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
    this.iframeUrl = this.$config.rscpHost + '/mgt/lucheng/projectappearance?outsideToken=' + this.seawallDetail.outsideToken + '&viewType=latestAll&proType=res'
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