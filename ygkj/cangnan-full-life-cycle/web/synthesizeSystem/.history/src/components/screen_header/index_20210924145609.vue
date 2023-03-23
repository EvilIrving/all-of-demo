<template>
  <el-header class="mian-header" v-show="!qiankunModule">
    <div class="main-header__float">
      <div class="main-header__float--logo">
        <div class="logo">温州市水平台</div>
        <el-collapse-transition>
          <div
            class="system-name"
            style="border-left: 2px solid #fff; padding-left: 15px"
          >
            {{ menuList[currentRoute].title }}
          </div>
        </el-collapse-transition>
      </div>
      <!-- <div class="main-header__float--center" v-show="!collapse"></div> -->
      <div class="main-header__float--action">
        <!-- <div class="workplace" @click="handleWorkplace">我的工作台</div> -->
        <!-- <el-popover placement="bottom" trigger="hover" popper-class="popover-menu">
          <ul class="menu-list">
            <li><i class="icon-analysis-count" /><span>消息列表</span></li>
            <li><i class="icon-analysis-count" /><span>我的数据</span></li>
            <li><i class="icon-analysis-count" /><span>我的收藏</span></li>
            <li><i class="icon-analysis-count" /><span>个人信息</span></li>
            <li><i class="icon-analysis-count" /><span>角色清单</span></li>
            <li><i class="icon-analysis-count" /><span>修改密码</span></li>
          </ul>
        </el-popover> -->
        <div class="analysis" slot="reference" @click="workplaceVisible = true">
          我的工作台
        </div>
        <el-badge
          v-if="warnShow"
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
          <!-- <div
            :class="['menu__arrow', 'menu__arrow--left']"
            @click="handleMenuArrowClick(-1)"
          /> -->
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
          <!-- <div
            :class="['menu__arrow', 'menu__arrow--right']"
            @click="handleMenuArrowClick()"
          /> -->
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
    <WorkplaceDialog
      :visiable="workplaceVisible"
      @close="workplaceVisible = false"
    />
  </el-header>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import policeDialog from "./components/policeDialog.vue";
import WorkplaceDialog from "../screen_workplace/index.vue";
import actions from "../../actions";

export default {
  name: "ScreenHeader",
  props: {},
  components: { policeDialog, WorkplaceDialog },
  inject: ["reload"],
  data() {
    return {
      warnShow: true,
      collapse: false,
      workplaceVisible: false,
      dialogVisible: false,
      menuList: [
        { title: "水灾害防御", path: "/waterDisasterCustom?bsType=waterDisaster" },
        { title: "水资源保障", path: "/waterDisasterCustom?bsType=waterResource" },
        { title: "河湖库保护", path: "/waterDisasterCustom?bsType=riverProtect" },
        { title: "水发展规划", path: "/waterDisasterCustom?bsType=waterDevelop" },
        { title: "水事务监管", path: "/waterDisasterCustom?bsType=waterAffair" },
        { title: "水政务协同", path: "/waterDisasterCustom?bsType=waterGovernment" },
        { title: "综合一张图", path: "/hydraulicAmap" },
      ],
      timer: null,
    };
  },
  created() {
    let bsType = this.$route.query.bsType;
    this.SetBsName(this.BsNameObj[bsType]);
  },
  mounted() {
    // console.log(window.__POWERED_BY_QIANKUN__,8888);
    this.getWarningList();
    this.timer = setInterval(() => {
      this.getWarningList();
    }, 15000);
    //微应用时监听主应用发布的消息
    if (this.qiankunModule) {
      this.SetCollapse(true);
      actions.onGlobalStateChange((state) => {
        console.log(state, 222);
      })
    }
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  watch: {
    "$store.state.warnShow": function (val, oldval) {
      this.warnShow = val;
    },
  },
  computed: {
    ...mapGetters([
      "warningListShow",
      "currentWarningList",
      "currentWarningCount",
      "previewModule",
      "formatterCurrentTime",
      "formatterPreStartTime",
      "AffairId",
      "BsNameObj",
    ]),
    /**
     * 当前路由位于菜单配置的项数
     * @description 路由配置未使用二级嵌套路由故如此
     */
    currentRoute() {
      let currentRoute = null,
        currentIndex = 0;
      const bsType = this.$route.query.bsType;
      //对应路由的菜单 要和菜单列表对应
      const arr = ["waterDisaster", "waterResource", "riverProtect", "waterDevelop", "waterAffair", "waterGovernment"];
      if (bsType && arr.includes(bsType)) {
        currentIndex = arr.indexOf(bsType);
      } else {
        this.menuList.forEach((route, index) => {
          let find = this.$route.matched.find(
            (item) => item.path == route.path
          );
          if (find) {
            currentRoute = find;
            currentIndex = index;
          }
        });
      }
      return currentIndex;
    },
    currentTime() {
      let time = this.formatterCurrentTime();
      return time != "Invalid date" ? time : "请选择时间";
    },
    //判断微应用模式还是直接访问模式
    qiankunModule() {
      return window.__POWERED_BY_QIANKUN__;
    },
  },
  methods: {
    ...mapActions([
      "showWarningList",
      "hideWarningList",
      "getWarningList",
      "SetComponentLoading",
      "SetCollapse",
      "SetCheckedArr",
      "SetBsName",
    ]),
    handleAlertCilck() {
      if (this.warnShow) {
        if (this.warningListShow) {
          this.hideWarningList();
        } else {
          this.showWarningList();
        }
      }
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
      this.SetComponentLoading(true);
      this.SetCheckedArr([]);
      this.$router.push(this.menuList[index].path);
      const bsType = this.$route.query.bsType;
      this.SetBsName(this.BsNameObj[bsType]);
      this.reload();
    },
    handleMenuArrowClick(next = 1) {
      this.$router.push(this.menuList[this.currentRoute + next].path);
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
