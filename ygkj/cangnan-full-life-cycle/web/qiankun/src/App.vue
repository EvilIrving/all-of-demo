<template>
  <div id="root-app">
    <MainHeader @changeSystem="changeSystem" @hanleMenuClick="hanleMenuClick" />
    <MainContent :systemName="systemName" :sideHide="sideHide" />
    <!-- <div class="root-view" id="root-view" /> -->
  </div>
</template>

<script>
import MainHeader from "@/components/screen_header";
import MainContent from "@/components/screen_content";
export default {
  name: "app",
  components: { MainHeader, MainContent },
  data() {
    return {
      sideHide: true,
      systemName: "水文数字化"
    }
  },
  methods: {
    hanleMenuClick(title) {
      if (title == "日常工作") {
        this.sideHide = false;
      } else {
        this.sideHide = true;
      }
    },
    changeSystem(val) {
      this.systemName = val;
    }
  }

};
</script>

<style lang="scss">
#root-app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  position: relative;
  height: 100%;
  width: 100%;
}
.root-view {
  height: 100%;

  > div {
    height: 100%;
  }
}
.main-nav {
  position: absolute;
  z-index: 99999;
  top: 0%;
  width: 100%;
}
</style>
