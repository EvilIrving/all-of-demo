<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-03 09:23:12
 * @Description: 数据监测
 * @FilePath: /lucheng_project/src/views/projectDetail/monitoring/DataMonitor.vue
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
    this.iframeUrl = this.$config.aqjcHost + '/#/dataMonitoring?from=anji&seawallCode=' + this.seawallDetail.seawallCode
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