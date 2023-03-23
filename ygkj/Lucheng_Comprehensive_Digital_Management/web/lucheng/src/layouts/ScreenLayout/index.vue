<template>
  <keep-alive>
    <el-container class="home">
      <screen-header />
      <el-main class="main-container">
        <router-view />
      </el-main>
      <screen-footer />
    </el-container>
  </keep-alive>
</template>

<script>
import { ScreenHeader } from '@/components';
import { ScreenFooter } from '@/components';
export default {
  name: 'ScreenLayout',
  props: {},
  data() {
    return {};
  },
  components: { ScreenHeader, ScreenFooter },
  mounted() {
    // console.log(window)
  },
  methods: {}
};
</script>

<style lang="scss" scoped>
@import '@/style/theme.scss';
.home {
  height: 100%;
  width: 100%;
  .main-container {
    overflow-x: hidden;
    box-sizing: border-box;
    padding: 140px 16px 18px;
    height: 100%;
    width: 100%;
    transition: all 0.2s ease-in;
    background-image: url(~assets/new_images/bg.png);
    background-size: 100% 100%;
    background-repeat: no-repeat;
  }
}
</style>
