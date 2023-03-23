<template>
  <div class="wrap">
    <van-nav-bar
      fixed
      placeholder
      safe-area-inset-top
      :title="title"
      left-text=""
      :left-arrow="$store.state.viewType !== 'irs'"
      @click-left="$router.go(-1)"
    />
    <div class="frame_box">
      <iframe width="100%" height="100%" :src="link" frameborder="0"></iframe>
    </div>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      link: "",
    };
  },
  computed: {
    title() {
      return this.$route.query.type == 1 ? "实时台风" : "雷达卫星";
    },
  },
  created() {
    this.link =
      this.$route.query.type == 1
        ? "https://typhoon.slt.zj.gov.cn/wap.htm"
        : "https://smart.zj121.com/radarNew/index.html";

  },
  mounted() {
    this.addZwlog(this)
  }
};
</script>

<style scoped lang="scss">
.wrap {
  width: 100%;
  height: 100%;
}
.frame_box {
  width: 100%;
  height: calc(100% - 46px);
}
</style>
