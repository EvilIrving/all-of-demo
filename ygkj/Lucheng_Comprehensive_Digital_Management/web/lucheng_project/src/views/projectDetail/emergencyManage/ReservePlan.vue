<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-20 16:35:05
 * @Description: 预案管理
 * @FilePath: /lucheng_project/src/views/projectDetail/emergencyManage/ReservePlan.vue
-->
<template>
  <div class='wrap'>
    <iframe :src="iframeUrl" frameborder="0" width="100%" height="100%"></iframe>
  </div>
</template>

<script>
export default {
  name: 'ReservePlan',
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
    this.iframeUrl = this.$config.rscpHost + '/mgt/lucheng/ataf?outsideToken=' + this.seawallDetail.outsideToken + '&dictCode=EM_EP_PPEX&showType=file&resourceid=437'
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