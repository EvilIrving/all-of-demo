<template>
  <div id="app">
    <router-view v-if="isRouterAlive"></router-view>
  </div>
</template>
<script>
import qs from "qs";
import { getQueryString } from "@/utils/utils";
import { sendPV } from "@/utils/Wpk.js";
export default {
  provide() {
    return {
      reload: this.reload,
    };
  },
  data() {
    return {
      isRouterAlive: true,
    };
  },
  watch: {
    $route(val) {
      if (val.path != "/login") {
        sendPV(val.meta.id, val.meta.title, val.path)
      }
    },
  },
  methods: {
    reload() {
      this.isRouterAlive = false;
      this.$nextTick(function () {
        this.isRouterAlive = true;
      });
    },
  },
  created() {
    // localStorage.getItem("userMsg") && this.$store.replaceState(Object.assign(this.$store.state, JSON.parse(localStorage.getItem("userMsg"))));
    // //在页面刷新时将vuex里的信息保存到localStorage里
    // window.addEventListener("beforeunload", () => {
    //   localStorage.setItem("userMsg", JSON.stringify(this.$store.state))
    // })
    let type = getQueryString("type");
    this.$sessionData("set", "type", type);
    if (type === "dd") {
      aplus_queue.push({
        action: "aplus.setMetaInfo",
        arguments: ["aplus-rhost-v", "alog.zjzwfw.gov.cn"],
      });
      aplus_queue.push({
        action: "aplus.setMetaInfo",
        arguments: ["aplus-rhost-g", "alog.zjzwfw.gov.cn"],
      });
      console.log(aplus_queue, "aplus_queueaplus_queueaplus_queue");
      const u = navigator.userAgent;
      const isAndroid = u.indexOf("Android") > -1;
      const isIOS = !!u.match(/\(i[^;]+;(U;)?CPU.+MacOSX/);
      aplus_queue.push({
        action: "aplus.setMetaInfo",
        arguments: [
          "appId",
          isAndroid ? "28302650" : isIOS ? "28328447" : "47130293",
        ],
      });
    }
  },
};
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #333;
  height: 100%;
}
</style>
