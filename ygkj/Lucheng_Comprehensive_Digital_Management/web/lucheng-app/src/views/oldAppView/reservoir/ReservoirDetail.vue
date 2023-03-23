<!--
 * @Date: 2022-06-01 14:51:14
 * @Author: 
 * @Description: {type}工程首页的列表模式
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-22 18:12:21
 * @FilePath: \lucheng-app\src\views\appView\reservoir\ReservoirDetail.vue
-->
<template>
  <div class="project-wrap">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="$localData('get', 'engineeringInfo').name" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <project-map-single v-if="showModel=='big'" class="single_map" ref="projectMap"> </project-map-single>
    <transition name="fade">
      <div class="project-detail" v-if="showModel == 'big'">
        <div class="detail-title" v-if="type !== '山塘'">
          <div class="title-name">
            <h3>{{ projectDetail.name }}</h3>
            <span>{{ projectDetail.engScal || projectDetail.dikeGrad || projectDetail.seawallLevel}}{{type}}</span>
          </div>
          <div class="title-dec">
            <span>{{projectDetail.safetyConclusion || projectDetail.wagaType ||projectDetail.dikeType }}</span>
            <span>{{projectDetail.areaName || projectDetail.village  || projectDetail.startLocation|| projectDetail.startLoc || '温州市鹿城区' }}</span>
          </div>
        </div>
        <div class="detail-title" v-if="type === '山塘'">
          <div class="title-name">
            <h3>{{ projectDetail.name }}</h3>
          </div>
          <div class="title-dec">
            <span>总容积：</span>
            <span>{{projectDetail.jhhsRj}}万m3</span>
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
import ProjectMapSingle from "./ProjectMapSingle.vue";
import { homeIndex } from "@/api/projectDetail";
export default {
  name: 'ReservoirDetail',
  props: {

  },
  components: {
    ProjectMapSingle,
  },
  data() {
    return {
      menuIndex: 0,
      transitionName: "slide-right",
      skPointLayer: null,
      showModel: "big",
      projectDetail: {},
      personList: [],
      name: '',
    };
  },
  computed: {
    routes() {
      var routes = {
        children: this.$router.options.routes,
      };
      var route = this.$route.matched;
      for (var i = 0; i < route.length - 1; i++) {
        routes = routes.children.find((e) => e.name == route[i].name);
      }
      let temp = []
      if (this.type == '水闸') {
        temp = routes.children.slice(0, 3)
      } else if (this.type == '山塘') {
        temp = routes.children.slice(0, 1)
      } else {
        temp = routes.children.slice(0, 2)
      }
      return temp;
    },
    routeDeep() {
      let routeDeep = [];
      for (var i = 0; i < this.routes.length; i++) {
        routeDeep.push(this.routes[i].path);
      }
      return routeDeep;
    },
    type() {
      return this.$localData('get', 'engineeringInfo').type;
    },
    code() {
      return this.$localData('get', 'engineeringInfo').code;
    },
  },
  watch: {
    $route(to, from) {
      const routeDeep = this.routeDeep;
      const toDepth = routeDeep.indexOf(to.path);
      this.menuIndex = toDepth;
      const fromDepth = routeDeep.indexOf(from.path);
      console.log(fromDepth,toDepth,'more-depth');
      this.transitionName = toDepth > fromDepth ? "slide-left" : "slide-right";
    },
  },
  mounted() {
    this.getData();
    this.responsible();
  },
  methods: {
    async getData() {
      let res = await homeIndex.getProjectList({
        code: this.code,
        type: this.type,
        withWaterLevel: true,
      });
      if (res.code == 0) {

        // this.$store.commit('SET_ENFINEERINGINFO', this.projectDetail)
        // this.$localData("set", "projectDetail", this.projectDetail);
        const configObj = {
          src: require("@/assets/images/my-position.png"),
          scale: 0.4,
        };
        let data = []
        res.data.forEach((item) => {
          item.lat = item.lowLeftLat || item.stLat || item.latitude || item.startLat || item.dtMidLat
          item.lng = item.lowLeftLong || item.stLong || item.longitude || item.endLong || item.dtMidLong
          item.type = this.type;
          item.name = item.mnpdName || item.dikeName || item.wagaName || item.seawallName || item.resName
          item.code = item.mnpdCode || item.dikeCode || item.wagaCode || item.seawallCode || item.resCode
          data.push({
            point: [item.lng, item.lat],
            ...item
          })
        })
        this.projectDetail = data[0];
        this.skPointLayer = this.$refs.projectMap._drawPoint(
          data,
          configObj,
          this.skPointLayer
        );
        this.$refs.projectMap.setView(data[0].lng, data[0].lat, 15);
      }
    },
    // 责任人
    async responsible() {
      let res = await homeIndex.getChargePerson({
        projectCode: this.code,
      });
      if (res.code == 0) {
        res.data.forEach((element, index) => {
          if (index < 3) {
            element.phone = element.phone.substring(0, 3) + '****' + element.phone.substring(7)
          }
        });
        this.personList = res.data;
      }
    },
    clickMenu(index) {
      this.menuIndex = index;
      this.$router.push(this.routes[index].path);
    },
  },
};
</script>

<style scoped lang="scss">
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
  .single_map {
    width: 100%;
    height: 25%;
  }
  .project-detail {
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 75%;
    background: #fff;
    box-shadow: 0 1px 10px 0 rgba(0, 0, 0, 0.15);
    .detail-title {
      padding: 10px 0;
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
        span {
          color: #276af0;
          font-weight: 600;
          // @include tipsBg(120px, 32px, 14px);
        }
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
