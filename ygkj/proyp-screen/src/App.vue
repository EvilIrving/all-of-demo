<!--
 * @Author: wqn
 * @Date: 2022-11-14 09:45:28
 * @LastEditors: dtb
 * @LastEditTime: 2023-02-14 15:26:48
-->
<template>
  <div id="app">
    <router-view />
  </div>
</template>
<script>
import { getQueryString } from 'utils/utils.js'
export default {
  name: "App",
  created() {
    let token = getQueryString('token')
    if (token) {
      this.$localData('set', 'token', getQueryString('token'))
      window.location.href = window.location.origin + window.location.pathname + '?type=' + getQueryString('type')
    }

  },
};
</script>
<style lang="scss" scope>
#app {
  overflow: hidden;
  width: 100%;
  height: 100%;
  background-image: url("~@/assets/images/system-bg.png");
  background-repeat: no-repeat;
  background-size: 100% 100%;
}
</style>
