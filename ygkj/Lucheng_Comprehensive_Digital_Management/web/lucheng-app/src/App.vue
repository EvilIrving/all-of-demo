<template>
  <div
    id="app"
    :class="{ 'dd-class': type == 'dd' || viewType === 'irs', 'old-class': addRouteType === 'older' }"
  >
    <transition :name="transitionName">
      <router-view v-if="$route.meta.keepAlive" />
    </transition>
    <transition :name="transitionName">
      <router-view v-if="!$route.meta.keepAlive" />
    </transition>
  </div>
</template>
<script>
import { sessionData, localData } from "./plugins/storage";
import { getQueryString } from "./utils/utils";
import VConsole from "vconsole";
import zwUtil, { initZwlog } from "./utils/zwUtil";
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      transitionName: "",
      seawallCode: "",
      type: "",
    };
  },
  computed: {
    ...mapGetters(["addRouteType", "viewType"]),
  },
  async created() {
    this.seawallCode = this.$route.query.seawallCode;
    // let userInfo = sessionData("get", "userInfo");
    // if (userInfo?.accessToken) {
    //   await localData("set", "userInfo", userInfo);
    //   await sessionData("set", "userInfo", userInfo);
    // }
    let type = getQueryString("type");
    if (type) {
      this.type = type;
    }
    // let vconsole = getQueryString("vconsole");
    // if (vconsole) {
    //   new VConsole();
    // }
    // let ticketId = getQueryString("ticketId");
    // if (ticketId) {
    //   console.log(ticketId);
    // }

    // else {
    //   window.location.href =
    //     "https://puser.zjzwfw.gov.cn/sso/alipay.do?action=ssoLogin&citycode=&servicecode=BCDSGA_35a21689670e02112cc3608cf7acc04b&goto=https%3A%2F%2Fmapi.zjzwfw.gov.cn%2Fweb%2Fmgop%2Fgov-open%2Fzj%2F2002276620%2F1.0.1%2Findex.html%3Fdebug%3Dtrue%26vconsole%3Dtrue";
    // }
  },
  provide() {
    return {
      reload: this.reload,
    };
  },
  watch: {
    $route(to, from) {
      let excludePage = ["/appIndex"];
      const fromDepth = from.meta.posIndex;
      const toDepth = to.meta.posIndex;
      // scrollTo(0,800)
      console.log(window.location.href, "this.$routethis.$routethis.$route");
      this.transitionName = toDepth > fromDepth ? "slide-left" : "slide-right";
      //进入时间
      if (window.location.href.indexOf("slgh.lucheng.gov.cn") === -1) {
        zwUtil.zwlogPvGlobal({
          pagePath: to.path,
          enterPageTime: new Date(),
        });
        //离开时间
        zwUtil.zwlogPvGlobal({
          pagePath: from.path,
          leavePageTime: new Date(),
        });
      }
    },
  },
  methods: {
    reload() {
      this.$route.meta.keepAlive = false;
      this.$nextTick(function () {
        this.$route.meta.keepAlive = true;
      });
    },
  },
};
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  background-color: #fff;
  width: 100%;
  height: 100%;
  overflow: auto;
  position: relative;
  font-size: 16px;
  // margin-top: 30px;
  .slide-right-enter-active,
  .slide-right-leave-active,
  .slide-left-enter-active,
  .slide-left-leave-active {
    will-change: transform;
    transition: all 500ms;
    // position: absolute;
  }
  .slide-right-enter {
    opacity: 0;
    transform: translateX(-100%);
  }
  .slide-right-leave-active {
    opacity: 0;
    transform: translateX(100%);
  }
  .slide-left-enter {
    opacity: 0;
    transform: translateX(100%);
  }
  .slide-left-leave-active {
    opacity: 0;
    transform: translateX(-100%);
  }
}
.dd-class {
  .van-nav-bar {
    padding-top: 0px !important;
    // display: none;
  }
  .van-nav-bar__placeholder {
    padding-top: 0px !important;
  }
  .van-nav-bar--fixed {
    top: 0;
  }
}
</style>

