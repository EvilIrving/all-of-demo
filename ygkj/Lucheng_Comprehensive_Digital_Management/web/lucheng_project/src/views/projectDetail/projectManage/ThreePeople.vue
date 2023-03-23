<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-20 15:19:10
 * @Description: 安全责任制
 * @FilePath: /lucheng_project/src/views/projectDetail/projectManage/ThreePeople.vue
-->
<template>
  <div class='wrap'>
    <iframe :src="iframeUrl" frameborder="0" width="100%" height="100%"></iframe>
  </div>
</template>

<script>
export default {
  name: 'ThreePeople',
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
    this.iframeUrl = this.$config.rscpHost + '/mgt/lucheng/threepeople/form?outsideToken=' + this.seawallDetail.outsideToken + '&year=' + new Date().format('yyyy') + '&subType=1'
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