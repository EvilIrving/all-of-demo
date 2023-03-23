<template>
  <div class='building_wrap'>
    <!-- <iframe :src="url" class="iframe" frameborder="0" width="100%" height="100%"></iframe> -->
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
  name: 'SpeWarningPlatform',
  props: {

  },
  components: {

  },
  data() {
    return {
      url: 'https://slgh.lucheng.gov.cn/warn/index.do'
    };
  },
  mounted() {
    window.open('https://slgh.lucheng.gov.cn/warn/index.do')
  },
  methods: {}
}
</script>

<style lang='scss' scoped>
.building_wrap {
  width:100%
}
</style>