<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-11-14 16:56:58
 * @Description: 
 * @FilePath: \lucheng-app\src\views\appView\lookFile\lookFile.vue
-->
<template>
  <div class="files-box project_wrap">
    <van-nav-bar
      fixed
      placeholder
      safe-area-inset-top
      title="文件预览"
      :left-arrow="$store.state.viewType !== 'irs'"
      @click-left="onBackClick"
    />
    <iframe :src="filesSrc" frameborder="0"></iframe>
    <div class="go-back" @click="goBack">返回</div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  computed: {
    ...mapGetters(["filesSrc"]),
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    onBackClick() {
      this.loading = true;
      this.$router.go(-1);
      this.$nextTick(() => {
        this.loading = false;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.files-box {
  width: 100%;
  height: 100%;
  padding-bottom: 15px;
  box-sizing: border-box;
  iframe {
    width: 100%;
    height: 85%;
  }
  .go-back {
    width: 90%;
    margin: 15px auto 0;
    padding: 13px 0;
    background-color: #3a8ee6;
    color: #fff;
    text-align: center;
    border-radius: 4px;
  }
}
</style>

<style lang="scss">
.project_wrap {
  .van-dropdown-menu__bar {
    height: 44px;
    box-shadow: none;
  }
}
</style>