<template>
  <keep-alive>
    <el-container class="home">
      <!-- <screen-header /> -->
      <el-main class="main-container">
        <router-view />
      </el-main>
    </el-container>
  </keep-alive>
</template>

<script>
import { ScreenHeader } from "@/components";
export default {
  name: "ScreenLayout",
  props: {},
  data() {
    return {};
  },
  components: { ScreenHeader },
  mounted() {},
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "@/style/theme.scss";
.home {
  height: 100%;
  .main-container {
    overflow-x: hidden;
    padding: 0;
    height: 100%;
    transition: all 0.2s ease-in;
  }
}
</style>
