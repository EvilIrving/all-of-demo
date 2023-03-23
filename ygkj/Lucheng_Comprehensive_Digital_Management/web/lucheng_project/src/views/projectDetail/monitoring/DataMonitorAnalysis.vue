<!--
 * @Author: hanyu
 * @LastEditTime: 2022-03-11 18:28:23
 * @Description: 数据分析
 * @FilePath: \lucheng_project\src\views\projectDetail\monitoring\DataMonitorAnalysis.vue
-->
<template>
  <div class='wrap'>
    <iframe :src="iframeUrl" frameborder="0" width="100%" height="100%"></iframe>
  </div>
</template>

<script>
export default {
  name: 'GoodsManage',
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
    this.iframeUrl = this.$config.aqjcHost + '/#/externalDataAnalysisDetaile?from=anji&seawallCode=' + this.seawallDetail.seawallCode
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