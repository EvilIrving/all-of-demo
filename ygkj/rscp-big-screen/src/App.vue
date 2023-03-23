<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { getQueryString } from './utils/utils'
export default {
  created() {
    // window.__wpk.setConfig({ uid: JSON.parse(localStorage.getItem('userInfo_obj')).id }) // 设置uid
    // 获取url中的token 用于数据请求
    if (getQueryString('token')) {
      this.$localData('set', 'token', getQueryString('token'))
      window.location.href = window.location.origin + window.location.pathname + '?type=' + getQueryString('type') + '&whiteLeft=' +  getQueryString('whiteLeft')
    }
    if (getQueryString('isSgzg') == '1') {
      this.$localData('set', 'token', 'f725d2871e1412b720882dcb6b78c813')
    }

    // //在页面刷新时将vuex里的信息保存到localStorage里
    // localStorage.getItem("userMsg") && this.$store.replaceState(Object.assign(this.$store.state,JSON.parse(localStorage.getItem("userMsg"))));
    // window.addEventListener("beforeunload",()=>{
    //   localStorage.setItem("userMsg",JSON.stringify(this.$store.state))
    // })
  }
}
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  height: 100%;
}
</style>
