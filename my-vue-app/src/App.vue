<script setup lang="ts">
// import HelloWorld from "./components/HelloWorld.vue";
// import Hello from "./components/Hello.vue";
import LifeCycle from "./components/LifeCycle.vue";
import SendValue from "./components/SendValue.tsx";
// import Test from "./components/test.jsx";
</script>

<template>
  <div>
    <a href="https://vitejs.dev" target="_blank">
      <img src="/vite.svg" class="logo" alt="Vite logo" />
    </a>
    <a href="https://vuejs.org/"  target="_blank">
      <img src="./assets/vue.svg" class="logo vue" alt="Vue logo" />
    </a>
  </div>
  <SendValue  />
</template>

<style scoped>
/* @import "assets/style/global.scss"; */
@import "normalize.css";
@import "animate.css";

.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
/*设置进度条颜色  不配置时为默认颜色*/
#nprogress .bar {
   background: red !important;
}
</style>
