<template>
  <keep-alive>
    <div class="home">
      <div
        :class="[
          'page-content',
          'clearfix',
          menuFlag ? 'has-menu' : '',
        ]"
      >
        <transition name="fade">
          <div
            class="expand-expand-area"
            v-show="menuFlag && !menuShow"
          >
            <i class="icon-expand-menu" @click="menuControl"></i>
          </div>
        </transition>
        <transition name="fade" v-on:after-leave="transitionDone">
          <div class="second-menu left" v-if="menuShow" >
            <div
              class="expand-shrink-area"
              v-if="MenuName == '日常工作' || MenuName =='数据报表'"
            >
              <i class="icon-shrink-menu" @click="menuControl"></i>
            </div>
            <el-collapse-transition>
              <MenuTree
                key="dailyWorkTree"
                ref="dailyWorkTree"
                @nodeClick="nodeClick"
                v-if="MenuName == '日常工作'"
                v-show="menuShow"
                :treeData="sideMenu"
              ></MenuTree>
              <MenuTree
                key="dataSheetTree"
                ref="dataSheetTree"
                @nodeClick="nodeClick"
                v-if="MenuName == '数据报表'"
                v-show="menuShow"
                :treeData="dataMenus"
              ></MenuTree>
            </el-collapse-transition>
          </div>
        </transition>
        <div class="main-router-content left">
          <div class="root-view" id="root-view" />
        </div>
      </div>
    </div>
  </keep-alive>
</template>

<script>
import { menuApi } from "@/api/menus";
import { mapGetters } from "vuex";
import MenuTree from "./components/MenuTree.vue";

export default {
  name: "Home",
  props: {
  },
  data() {
    return {
      menus: [],
      sideMenu: [
        {
          path: "/dailyBusiness/dutyManage/",
          name: "值班管理",
          iconClass: "icon-menu-case",
          children: [
            //排班管理
            {
              path: "/dailyBusiness/dutyManage/schedulManage",
              name: "排班管理",
            },
            //值班人员管理
            {
              path: "/dailyBusiness/dutyManage/dutyPersonManage",
              name: "值班人员管理",
            },
            //值班常用电话
            {
              path: "/dailyBusiness/dutyManage/dutyPhone",
              name: "值班常用电话",
            }],
        },
        //通讯录管理
        {
          path: "/dailyBusiness/contactsManage/",
          children: [],
          name: "通讯录管理",
          iconClass: "icon-menu-law"
        },
        //日常管理
        {
          path: "/dailyBusiness/dailyManagement/",
          children: [],
          name: "日常管理",
          iconClass: "icon-menu-law"
        },
        //业务管理
        {
          path: "/dailyBusiness/businessManagement/",
          children: [],
          name: "业务管理",
          iconClass: "icon-menu-law"
        },
        {
          path: "/resources/base/",
          name: "基础数据",
          iconClass: "icon-menu-law",
          redirect: "/resources/base/manage",
          children: [
            {
              name: "数据管理",
              path: "/resources/base/manage",
              iconClass: null,
            },
            {
              path: "/resources/base/approve",
              name: "数据审批",
              iconClass: null,
            },
            {
              path: "/resources/base/duty-manage",
              name: "责任管理",
              iconClass: null,
            },
            {
              path: "/resources/base/trace-source",
              name: "溯源管理",
              iconClass: null,
            },
            {
              path: "/resources/base/setting",
              name: "设置管理",
              iconClass: null,
            },
          ],
        },
      ],
      activeName: "",
      sideHide: true,//控制侧边栏的显隐
      dataMenus: [
        {
          id: 1,
          path: "/datamanagement/statement",
          name: "基础数据报表",
          iconClass: "icon-menu-case",
          children: [],
        }, {
          id: 2,
          name: "水文报表",
          iconClass: "icon-menu-case",
          children: [{
            id: 20,
            path: "/hydrolo-gy/dataAnalyse/rainStatement/",
            name: "雨情报表",
            children: [{
              id: 201,
              path: "/hydrolo-gy/dataAnalyse/rainStatement/rainSheet",
              name: "分区雨量",
              children: [],
            }, {
              id: 202,
              path: "/hydrolo-gy/dataAnalyse/rainStatement/hourMax",
              name: "时段极值",
              children: [],
            }, {
              id: 203,
              path: "/hydrolo-gy/dataAnalyse/rainStatement/daily",
              name: "日雨量",
              children: [],
            }, {
              id: 204,
              path: "/hydrolo-gy/dataAnalyse/rainStatement/rainstorm",
              name: "暴雨加报",
              children: [],
            }]
          }, {
            id: 3,
            path: "/hydrolo-gy/dataAnalyse/waterStatement/",
            name: "水情报表",
            iconClass: "icon-menu-case",
            children: [{
              id: 301,
              path: "/hydrolo-gy/dataAnalyse/waterStatement/riverSheet",
              name: "河网水位",
              children: [],
            }, {
              id: 302,
              path: "/hydrolo-gy/dataAnalyse/waterStatement/rivernet",
              name: "河道水位统计表",
              children: [],
            }, {
              id: 303,
              path: "/hydrolo-gy/dataAnalyse/waterStatement/mountain",
              name: "山区河道水位",
              children: [],
            }, {
              id: 304,
              path: "/hydrolo-gy/dataAnalyse/waterStatement/riverDayAvg",
              name: "河道日平均水位",
              children: [],
            }, {
              id: 305,
              path: "/hydrolo-gy/dataAnalyse/waterStatement/riverWarning",
              name: "河道水位超警表",
              children: [],
            }, {
              id: 306,
              path: "/hydrolo-gy/dataAnalyse/waterStatement/tideHourGrowth",
              name: "逐时潮位增长",
              children: [],
            }, {
              id: 307,
              path: "/hydrolo-gy/dataAnalyse/waterStatement/tideHighGrowth",
              name: "高潮位增长表",
              children: [],
            }, {
              id: 308,
              path: "/hydrolo-gy/dataAnalyse/waterStatement/reservoirStorage",
              name: "水库蓄水统计表",
              children: [],
            }, {
              id: 309,
              path: "/hydrolo-gy/dataAnalyse/waterStatement/reservoirLevel",
              name: "水库水位统计表",
              children: [],
            }, {
              id: 310,
              path: "/hydrolo-gy/dataAnalyse/waterStatement/reservoirHour",
              name: "水库水位时段表",
              children: [],
            }, {
              id: 311,
              path: "/hydrolo-gy/dataAnalyse/waterStatement/reservoirOverlimit",
              name: "水库水位超汛表",
              children: [],
            }]
          }]
        },
      ],
      menuShow: true,
    };
  },
  components: {
    MenuTree
  },
  watch: {
    SystemType(val) {
      if (this.MenuName == '日常工作') {
        this.getMenus(val);
      }
    },
    MenuName(val) {
      this.hanleMenuClick(val);
    },
  },
  computed: {
    ...mapGetters(["SystemType", "MenuName"]),
    menuFlag(){
      return (this.MenuName == '日常工作' || this.MenuName == '数据报表');
    },
  },
  created() {
  },
  mounted() {
  },
  methods: {
    transitionDone(el, done) {
      this.showBtn = true;
    },
    nodeClick(data) {
      if (data.children.length == 0) {
        this.$router.push(data.path);
      }
    },
    async getMenus(system) {
      await menuApi.getMenus({ system }).then(res => {
        this.sideMenu = res.data.menus[0].children;
        this.$nextTick(() => {
          this.$refs.dailyWorkTree.handleNodeClick(this.sideMenu[0]);
          // if (!this.sideHide) {
          //   if (this.sideMenu[0].redirect) {
          //     this.$router.push(this.sideMenu[0].redirect);
          //   } else {
          //     this.$router.push(this.sideMenu[0].path);
          //   }
          // }
        })
      })
    },
    setActive(name) {
      this.activeName = name;
    },
    handleRouteChange(route) {
      const find = this.menus.find((item) => item.path === route.matched[1].path);
      this.sideMenu = find?.children || [];
    },
    hasChildren(item) {
      return item.children && item.children.length > 0;
    },
    caretIcon(isActive) {
      return ["menu-sub", isActive ? "el-icon-caret-top" : "el-icon-caret-bottom"];
    },
    hanleMenuClick(title) {
      if (title == '日常工作') {
        this.sideHide = false;
        this.getMenus(this.SystemType).then(() => {
          // console.log(this.sideMenu,222)
          // this.activeName = this.getChildName(this.sideMenu[0]);
        });
      } else if (title == "数据报表") {
        this.$nextTick(() => {
          this.$refs.dataSheetTree.handleNodeClick(this.dataMenus[0]);
        })
        // this.activeName = "基础数据报表";
      } else {
        this.sideHide = true;
      }
    },
    getChildName(list) {
      let childName = "";
      let recursion = (data) => {
        if (data.children && data.children.length > 0) {
          recursion(data.children[0]);
        } else {
          childName = data.name;
        }
      }
      recursion(list);
      return childName;
    },
    menuControl() {
      this.menuShow = !this.menuShow;
    }
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/theme.scss";
$mainMenuHeight: 60px;
$secondMenuWidth: 255px;
$menuActiveBorderColor: yellow;
$headerHeight: 90px;
.home {
  width: 100%;
  height: 100%;
  position: relative;
  .root-view {
    height: 100%;
    > div {
      height: 100%;
    }
  }
  .main-router-content {
    width: 100%;
    height: 100%;
    position: relative;
    .no-router {
      width: calc(100% - 255px);
      height: 100%;
      left: 255px;
      top: 0;
      position: absolute;
      background: url(~assets/images/no-page.png) no-repeat center center;
    }
  }

  .fade-enter,
  .fade-leave-to {
    transform: translateX(-100%);
  }
  .fade-enter-active,
  .fade-leave-active {
    transition: all 0.5s linear;
  }

  .page-content {
    width: 100%;
    height: 100%;
    // padding-top: 80px;
    background: url("../../assets/images/list-bg.png") no-repeat;
    background-size: 100% 100%;
    display: flex;
    position: relative;
    .expand-expand-area,
    .expand-shrink-area {
      position: absolute;
      top: 0px;
      left: 12px;
      z-index: 1000;
      cursor: pointer;
      i {
        display: inline-block;
        width: 54px;
        height: 40px;
      }
    }
    .expand-expand-area {
      top: 90px;
    }
    .second-menu {
    }
    &.has-menu {
      .main-router-content,
      .second-menu {
        height: calc(100% - #{$headerHeight});
      }
      .second-menu {
        position: relative;
        width: $secondMenuWidth;
        min-width: $secondMenuWidth;
        margin-top: $headerHeight;
        height: calc(100% - 100px);
        overflow: auto;
        padding-top: 30px;
        .menu-item {
          cursor: pointer;
          padding: 24px 0 24px 50px;
          display: flex;
          align-items: center;
          justify-content: space-between;
          opacity: 0.64;
          font-size: 18px;
          color: #ffffff;
          i {
            width: 16px;
            height: 16px;
            margin-right: 16px;
            &.menu-sub {
              color: #ffffff;
            }
          }
          &.active {
            color: $primaryListColor;
            span {
              font-weight: bold;
            }
          }
          &:hover {
            color: $primaryListColor;
          }
          // &:last-child {
          //   padding-bottom: 0;
          // }
        }
        .sub-menu-list {
          overflow: hidden;
          .sub-menu-item {
            display: block;
            padding: 20px 0 20px 90px;
            font-family: PingFangSC-Regular;
            font-size: 16px;
            color: #ffffff;
            text-align: left;
            &.router-link-exact-active,
            &.router-link-active {
              color: $primaryListColor;
            }
            .child-menu-item {
              color: #fff;
              opacity: 0.8;
              &.active {
                color: $primaryListColor;
                span {
                  font-weight: bold;
                  color: $primaryListColor;
                }
              }
              &:hover {
                color: $primaryListColor;
              }
            }
          }
          .sup-menu-list {
            overflow: hidden;
            padding: 12px 0;
            &:last-child {
              padding-bottom: 0;
            }
            .sup-menu-item {
              display: block;
              opacity: 0.64;
              font-family: PingFangSC-Regular;
              font-size: 16px;
              color: #ffffff;
              text-align: left;

              span {
                padding: 20px 0 20px 20px;
                width: 100%;
                display: inline-block;
                &.active {
                  color: $primaryListColor;
                }
                &:hover {
                  color: $primaryListColor;
                }
              }
              &.router-link-exact-active,
              &.router-link-active {
                color: #fff;
              }
              &:last-child {
                padding-bottom: 0;
              }
            }
          }
        }
      }
      .main-router-content {
        flex: 1;
        height: 100%;
        overflow: auto;
        // padding-left: $secondMenuWidth;
      }
    }
  }
}
</style>
