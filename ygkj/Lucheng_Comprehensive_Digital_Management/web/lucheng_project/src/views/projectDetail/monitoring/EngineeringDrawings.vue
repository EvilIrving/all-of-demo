<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-25 18:41:00
 * @Description: 工程图纸
 * @FilePath: /lucheng_project/src/views/projectDetail/monitoring/EngineeringDrawings.vue
-->
<template>
  <div class='wrap'>
    <iframe :src="iframeUrl" frameborder="0" width="100%" height="100%"></iframe>
  </div>
</template>

<script>
export default {
  name: 'EngineeringDrawings',
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
    this.iframeUrl = this.$config.aqjcHost + '/#/engineeringDrawings?from=anji&seawallCode=' + this.seawallDetail.seawallCode
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