<!--
 * @Author: hanyu
 * @LastEditTime: 2022-06-14 18:14:48
 * @Description: 
 * @FilePath: \lucheng-app\src\views\projectIndex\ProjectIndex.vue
-->
<template>
  <div class="project-wrap">
    <project-map ref="projectMap"> </project-map>
    <transition name="fade">
      <div class="project-detail project-detail-small" v-if="showModel == 'small'">
        <div class="detail-title" @click="showModel = 'big'">
          <div style="text-align: center">
            <img src="@/assets/images/up-arr.png" alt="" />
          </div>
          <div class="title-name">
            <h3>{{ projectDetail.seawallName }}</h3>
            <span>{{ projectDetail.safetyConclusion || "-" }}</span>
          </div>
          <div class="title-dec">
            <span>距你 - km</span>
            <span>{{ projectDetail.startLocation }}</span>
          </div>
        </div>
      </div>
    </transition>
    <transition name="fade">
      <div class="project-detail" v-if="showModel == 'big'">
        <div class="detail-title" @click="showModel = 'small'">
          <div style="text-align: center">
            <img src="@/assets/images/down-arr.svg" alt="" />
          </div>
          <div class="title-name">
            <h3>{{ projectDetail.seawallName }}</h3>
            <span>{{ projectDetail.safetyConclusion || "-" }}</span>
          </div>
          <div class="title-dec">
            <span>距你 - km</span>
            <span>{{ projectDetail.startLocation }}</span>
          </div>
        </div>
        <div class="detail-line"></div>
        <div class="detail-menu">
          <div :class="['detail-menu-item', menuIndex === index ? 'active' : '']" v-for="(item, index) in routes" :key="item.name" @click="clickMenu(index)">
            {{ item.title }}
          </div>
        </div>
        <div class="scroll-box">
          <transition :name="transitionName">
            <router-view class="page-view" />
          </transition>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import ProjectMap from "./components/ProjectMap";
import { homeIndex } from "@/api/projectDetail";
export default {
  name: "ProjectIndex",
  props: {},
  components: {
    ProjectMap,
  },
  data() {
    return {
      menuIndex: 0,
      transitionName: "slide-right",
      projectDetail: {},
      myPointLayer: null,
      showModel: "small",
      seawallCode: "",
    };
  },
  computed: {
    routes() {
      // if (this.$route.query.seawallCode) {
      //   this.showModel = this.$route.query.seawallCode;
      //   this.$router.push(this.$route.query.pagePath);
      // }
      var routes = {
        children: this.$router.options.routes,
      };
      var route = this.$route.matched;
      for (var i = 0; i < route.length - 1; i++) {
        routes = routes.children.find((e) => e.name == route[i].name);
      }
      return routes.children;
    },
    routeDeep() {
      let routeDeep = [];
      for (var i = 0; i < this.routes.length; i++) {
        routeDeep.push(this.routes[i].path);
      }
      return routeDeep;
    },
  },
  watch: {
    $route(to, from) {
      const routeDeep = this.routeDeep;
      const toDepth = routeDeep.indexOf(to.path);
      const fromDepth = routeDeep.indexOf(from.path);
      this.transitionName = toDepth > fromDepth ? "slide-left" : "slide-right";
    },
  },
  mounted() {
    this.seawallCode = this.$route.query.seawallCode;
    this.$store.commit('SET_SEAWALLLCODE', this.seawallCode)
    this.menuIndex = this.routeDeep.indexOf(this.$route.path);
    setTimeout(() => {
      this.getData();
    }, 500)
  },
  methods: {
    async getData() {
      let res = await homeIndex.getProjectList({
        code: this.seawallCode,
        type: "海塘",
      });
      if (res.code == 0) {
        this.projectDetail = res.data[0];
        this.$store.commit('SET_PROJECTDETAIL', res.data[0])
        const configObj = {
          src: require("@/assets/images/my-position.png"),
          scale: 0.4,
        };
        this.$localData("set", "projectDetail", this.projectDetail);
        const data = [
          {
            point: [
              +this.projectDetail.longitude,
              +this.projectDetail.latitude,
            ],
            // name: '我的位置'
          },
        ];
        this.myPointLayer = this.$refs.projectMap._drawPoint(
          data,
          configObj,
          this.myPointLayer
        );
        this.$refs.projectMap.setView(
          +this.projectDetail.longitude,
          +this.projectDetail.latitude - 0.09
        );
      }
    },
    clickMenu(index) {
      this.menuIndex = index;
      this.$router.push(this.routes[index].path);
    },
  },
};
</script>

<style lang="scss" scoped>
.fade-enter,
.fade-leave-to {
  transform: translateY(100%);
}
.fade-enter-active,
.fade-leave-active {
  transition: all 0.5s linear;
}
.project-wrap {
  position: relative;
  height: 100%;
  width: 100%;
  background: #fff;
  .score-text {
    font-size: 18px;
    font-weight: bold;
    &.red {
      color: #e02020;
    }
    &.yellow {
      color: #e6a23c;
    }
    &.green {
      color: #67c23a;
    }
    &.grey {
      color: #4e4c4c;
    }
  }
  .project-detail {
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 80%;
    background: #fff;
    border-top-left-radius: 20px;
    border-top-right-radius: 20px;
    box-shadow: 0 1px 10px 0 rgba(0, 0, 0, 0.15);
    &-small {
      height: 100px;
      .detail-title {
        .title-dec {
          position: relative;
          .code-text {
            line-height: 34px;
            color: #848484;
          }
          .qrcode {
            position: absolute;
            right: 18px;
            top: 30px;
            height: 90px;
            width: 90px;
            img {
              width: 100%;
              height: 100%;
            }
          }
        }
      }
      .btn-box {
        @include flexbox();
        @include flexJC(space-between);
        padding-top: 16px;
        border-top: 1px solid #f2f2f2;
        width: calc(100% - 32px);
        margin: 0 16px;
        > div {
          width: 46%;
          height: 40px;
          line-height: 40px;
          text-align: center;
          border-radius: 4px;
          font-size: 16px;
          &.route-btn {
            background: #fbfbfb;
            border: 1px solid rgba(0, 0, 0, 0.16);
            color: #1e1e1f;
          }
          &.nav-btn {
            background: #036eff;
            border: 1px solid rgba(0, 0, 0, 0.16);
            color: #fff;
          }
        }
      }
    }
    .detail-title {
      // text-align: center;
      padding: 5px 0;
      .title-name {
        @include flexbox();
        @include flexJC(space-between);
        @include flexAI(center);
        padding: 0 24px;
        h3 {
          font-size: 18px;
          color: #1e1e1f;
          font-weight: 600;
        }
        // span {
        //   @include tipsBg(120px, 32px, 14px);
        // }
      }
      .title-dec {
        padding-left: 24px;
        font-size: 12px;
        color: #b3b3b3;
        line-height: 24px;
        display: block;
        & > span:first-child {
          border-right: 1px solid #e5e5e5;
          padding-right: 10px;
          margin-right: 10px;
        }
        & > span:last-child {
          padding-left: 10px;
        }
      }
    }
    .detail-line {
      background: #f3f6f9;
      width: 100%;
      height: 8px;
    }
    .detail-menu {
      @include flexbox();
      overflow-x: auto;
      overflow-y: hidden;
      padding: 12px;
      box-sizing: border-box;
      &::-webkit-scrollbar {
        display: none;
      }
      .detail-menu-item {
        white-space: nowrap;
        min-width: 60px;
        padding: 0 10px;
        height: 30px;
        font-size: 16px;
        color: #74767a;
        text-align: center;
        line-height: 30px;
        font-weight: 500;
        background: #f6f6f6;
        border-radius: 4px;
        margin-right: 10px;
        &.active {
          background: #eff3fe;
          color: #036eff;
        }
        &:last-child {
          margin-right: 0;
        }
      }
    }
    .scroll-box {
      height: calc(100% - 155px);
      overflow: auto;
      .page-view {
      }
    }
    .slide-right-enter-active,
    .slide-right-leave-active,
    .slide-left-enter-active,
    .slide-left-leave-active {
      will-change: transform;
      transition: all 500ms;
      position: absolute;
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
}
</style>
