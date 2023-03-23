<!--
 * @Date: 2022-03-10 15:04:25
 * @Author: hanyu
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-04-19 18:12:11
 * @FilePath: \lucheng_project\src\views\defenseDispatch\DefenseDispatch.vue
-->
<template>
  <div class='building_wrap'>
    <iframe :src="url" class="iframe" frameborder="0" width="100%" height="100%"></iframe>
    <!-- <div class="building">
      <div>
        <img src="@/assets/images/no-responsible.png" alt="">
        <p>建设中...</p>
      </div>
    </div> -->
  </div>
</template>

<script>
export default {
  name: '',
  props: {

  },
  components: {

  },
  data() {
    return {
      url: 'https://slgh.lucheng.gov.cn/login/'
    };
  },
  mounted() {
  },
  methods: {

  }
}
</script>

<style lang='scss' scoped>
.wrap {
}
</style>