<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-02 20:02:59
 * @Description: 测点管理
 * @FilePath: /lucheng_project/src/views/projectDetail/monitoring/PointManage.vue
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
    this.iframeUrl = this.$config.aqjcHost + '/#/measuringPointConfiguration?from=anji&seawallCode=' + this.seawallDetail.seawallCode
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