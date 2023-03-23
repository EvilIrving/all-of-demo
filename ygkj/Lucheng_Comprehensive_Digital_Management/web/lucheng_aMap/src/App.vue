<template>
  <div id="app">
     
    <router-view />
  </div>
</template>

<style lang="scss">
* {
  user-select: none;
}
body {
  overflow-x: hidden;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif, Microsoft YaHei;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #333;
  height: 100%;
}
//适配拉伸大屏
.is-big-screen {
  transform: scaleX(0.5);
}
</style>
