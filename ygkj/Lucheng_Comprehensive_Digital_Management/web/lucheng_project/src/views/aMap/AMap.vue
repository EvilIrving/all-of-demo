<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-03 19:45:46
 * @Description: 
 * @FilePath: /lucheng_project/src/views/aMap/AMap.vue
-->
<template>
  <div class='wrap'>
    <iframe :src="url" frameborder="0" width="100%" height="100%"></iframe>
  </div>
</template>

<script>
export default {
  name: '',
  props: {
    
  },
  components: {
    
  },
  data () {
    return {
      url:'https://slgh.lucheng.gov.cn/aMap/projectSide?type=%E6%B5%B7%E5%A1%98,%E6%B0%B4%E9%97%B8,%E6%BD%AE%E4%BD%8D', //线上
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
    width: 100%;
    height: calc(100% - 85px);
  }
</style>