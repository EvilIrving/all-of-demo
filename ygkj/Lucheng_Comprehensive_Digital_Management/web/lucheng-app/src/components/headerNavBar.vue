<template>
  <van-nav-bar
    class="old-van-nav-bar"
    :class="{ 'app-index': $route.meta.title == '鹿城水利' }"
    :border="false"
    safe-area-inset-top
    :title="title"
    right-text=""
    @click-left="clickLeftArrow"
  >
  </van-nav-bar>
</template>

<script>
export default {
  computed: {
    leftArrowType() {
      return this.$route.meta.title != "鹿城水利";
    },
    title(){
      return this.$route.meta.title
    }
  },
  methods: {
    clickLeftArrow() {
      if (this.$route.meta.title != "鹿城水利") {
        this.$router.go(-1);
      }
    },
  },
};
</script>

<style lang='scss'>
.old-van-nav-bar {
  box-shadow: 0px 1px 1px 1px rgba(0, 0, 0, 0.02),
    0px 0px 1px 0px rgba(0, 0, 0, 0.06), 0px 1px 1px 0px rgba(0, 0, 0, 0.06);
  &.van-hairline--bottom::after {
    border-bottom-width: 0px;
  }
  .van-nav-bar__content {
    .van-nav-bar__title {
      font-size: 20px;
      font-weight: 500;
    }
    .van-nav-bar__left {
      .van-icon {
        color: #000000;
      }
    }
  }
}
.app-index {
  box-shadow: none;
  .van-nav-bar__content {
    background-color: #276af0;
    .van-nav-bar__title {
      color: #ffffff;
      margin-left: 12px;
    }
  }
}
</style>