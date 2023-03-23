<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-17 11:08:54
 * @Description: 查看附件
 * @FilePath: /lucheng_project/src/components/LookFile.vue
-->
<template>
  <div class='wrap'>
    <iframe :src="url" class="iframe" frameborder="0" width="100%" height="100vh"></iframe>
  </div>
</template>

<script>
export default {
  name: '',
  props: {
    url: {
      type: String,
      required: true
    }
  },
  components: {
    
  },
  data () {
    return {
      
    };
  },
  mounted() {
    
  },
  methods: {
    
  }
}
</script>

<style lang='scss' scoped>
  .wrap{
    
  }
  .iframe{
    text-align: center;
    height: calc(100vh - 117px);
  }
  
  img{
    width: 100%;
  }
</style>