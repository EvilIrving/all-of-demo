<template>
  <el-header class="mian-header">
    <div class="main-header__float">
      <div class="main-header__float--logo">
        <div class="logo">温州市水平台</div>
        <el-collapse-transition>
          <div
            v-show="!collapse"
            class="system-name"
            style="border-left: 2px solid #fff; padding-left: 15px"
          >
            {{ menuList[currentRoute].title }}
          </div>
        </el-collapse-transition>
      </div>
      <!-- <div class="main-header__float--center" v-show="!collapse"></div> -->
      <div class="main-header__float--action">
        <div class="workplace" @click="handleWorkplace">我的工作台</div>
        <div class="analysis" @click="handleAnalysis">我的数据</div>
        <el-badge
          :hidden="currentWarningCount <= 0"
          :value="currentWarningCount"
          :max="99"
          class="warning-badge"
        >
          <div
            :class="
              currentWarningCount > 0 ? 'notification-warning' : 'notification'
            "
            @click="handleAlertCilck"
          />
        </el-badge>
      </div>
    </div>
    <div class="main-header__content">
      <el-collapse-transition>
        <div v-show="collapse" class="main-header__content--menu">
          <div
            :class="['menu__arrow', 'menu__arrow--left']"
            @click="handleMenuArrowClick(-1)"
          />
          <div class="menu__list">
            <div
              v-for="(item, index) in menuList"
              :key="index"
              :class="['menu__list--item', index == currentRoute && 'active']"
              @click="hanleMenuClick(index)"
            >
              {{ item.title }}
            </div>
          </div>
          <div
            :class="['menu__arrow', 'menu__arrow--right']"
            @click="handleMenuArrowClick()"
          />
        </div>
      </el-collapse-transition>
      <div class="main-header__content--collaspe-btn">
        <div
          :class="['collapse-btn', collapse ? 'up' : 'down']"
          @click="handleCollapseClick"
        />
      </div>
    </div>
    <police-dialog :dialogVisible="dialogVisible" @handleClose="handleClose" />
  </el-header>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import policeDialog from "./components/policeDialog.vue";

export default {
  name: "ScreenHeader",
  props: {},
  components: { policeDialog },
  data() {
    return {
      collapse: true,
      dialogVisible: false,
      menuList: [
        { title: "水灾害防御", path: "/index?large=true" },
        { title: "水资源保障", path: "/waterResource" },
        { title: "河湖库保护", path: "/riverlakeProtect" },
        { title: "水发展规划", path: "/waterDevelop" },
        { title: "水事务监管", path: "/wateraffairWatch" },
        { title: "水政务协同", path: "/governmentSynergy" },
        { title: "综合一张图", path: "/hydraulicAmap" },
      ],
    };
  },
  mounted() {
    this.getWarningList();
  },
  watch: {
    currentTime: {
      immediate: true,
      handler(val) {
        this.$nextTick(() => {
          if (this.currentTime != "请选择时间") {
            const time = new Date(this.formatterCurrentTime()).format(
              "yyyy-MM-dd hh:mm:ss"
            );
            const param = {
              time,
            };
            this.getWarningList(param);
          } else {
            this.getWarningList();
          }
        });
      },
    },
  },
  computed: {
    ...mapGetters([
      "currentWarningList",
      "currentWarningCount",
      "previewModule",
      "formatterCurrentTime",
      "formatterPreStartTime",
    ]),
    /**
     * 当前路由位于菜单配置的项数
     * @description 路由配置未使用二级嵌套路由故如此
     */
    currentRoute() {
      let currentRoute = null,
        currentIndex = 0;
      this.menuList.forEach((route, index) => {
        let find = this.$route.matched.find((item) => item.path == route.path);
        if (find) {
          currentRoute = find;
          currentIndex = index;
        }
      });
      return currentIndex;
    },
    currentTime() {
      let time = this.formatterCurrentTime();
      return time != "Invalid date" ? time : "请选择时间";
    },
  },
  methods: {
    ...mapActions([
      "showWarningList",
      "hideWarningList",
      "getWarningList",
      "SetCollapse",
    ]),
    handleAlertCilck() {
      this.showWarningList();
      // this.dialogVisible = true;
    },
    handleClose() {
      this.dialogVisible = false;
    },
    handleCollapseClick() {
      this.collapse = !this.collapse;
      this.SetCollapse(this.collapse);
      this.$emit("change", this.collapse);
    },
    hanleMenuClick(index) {
      if (index != this.currentRoute);
      this.$router.push(this.menuList[index].path);
    },
    handleMenuArrowClick(next = 1) {
      this.$router.push(this.menuList[this.currentRoute + next].path);
    },
    handleWorkplace() {
      window.open("http://117.149.228.207:8081/floodDrought/#");
    },
    handleAnalysis() {
      window.open("http://117.149.228.207:8081/digitalManage/#/");
    },
  },
};
</script>

<style lang="scss">
@import "./style";
.warning-badge {
  .el-badge__content {
    top: 20px;
    right: 44%;
    background-color: red;
  }
}
</style>
