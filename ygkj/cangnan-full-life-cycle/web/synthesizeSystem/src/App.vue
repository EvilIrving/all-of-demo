<template>
  <div id="app">
    <router-view v-if="isRouterAlive" />
  </div>
</template>
<script>
import { login } from "@/api/login";
export default {
  created() {
    if (!window.__POWERED_BY_QIANKUN__) {
      this.doLogin();
    }
  },
  provide() {
    return {
      reload: this.reload
    };
  },
  data() {
    return {
      isRouterAlive: true
    };
  },
  methods: {
    reload() {
      this.isRouterAlive = false;
      this.$nextTick(function () {
        this.isRouterAlive = true;
      });
    },
    async doLogin() {
      let opt = {
        username: 15858500288,
        password: "ygkj0818"
      }
      await login(opt).then((res) => {
        if (res.code == 0) {
          let opt = res.data;
          opt.timestamp = new Date().getTime();
          this.$localData("set", "userInfo", opt);
        } else {
          this.$message.error(res.message);
        }
      });
    },
  }
}
</script>

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
