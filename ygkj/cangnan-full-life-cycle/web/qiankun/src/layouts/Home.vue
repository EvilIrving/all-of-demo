<template>
  <div
    class="home"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)"
    v-loading="FullLoading"
  >
    <MainHeader @hanleMenuClick="hanleMenuClick" />
    <MainContent ref="mainContent" />
  </div>
</template>

<script>
import MainHeader from "@/components/screen_header";
import MainContent from "@/components/screen_content";
import { mapGetters } from "vuex";
export default {
  name: 'home',
  components: { MainHeader, MainContent },
  computed: {
    ...mapGetters(["SystemType", "FullLoading"]),
  },
  data() {
    return {

    }
  },
  created() {
  },
  methods: {

  }
}
</script>

<style lang="scss" scoped>
.home {
  height: 100%;

  > div {
    height: 100%;
  }
  .main-nav {
    position: absolute;
    z-index: 99999;
    top: 0%;
    width: 100%;
  }
}
</style>