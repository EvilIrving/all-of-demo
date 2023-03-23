<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-20 15:20:54
 * @Description: 大事记管理
 * @FilePath: /lucheng_project/src/views/projectDetail/projectManage/BigThing.vue
-->
<template>
  <div class='wrap'>
    <iframe :src="iframeUrl" frameborder="0" width="100%" height="100%"></iframe>
  </div>
</template>

<script>
export default {
  name: 'BigThing',
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
    this.iframeUrl = this.$config.rscpHost + '/mgt/lucheng/emerm?outsideToken=' + this.seawallDetail.outsideToken
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