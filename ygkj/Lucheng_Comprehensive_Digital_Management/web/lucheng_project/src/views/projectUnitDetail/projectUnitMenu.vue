<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-05 10:32:51
 * @Description: 海塘详情页面框架
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\projectUnitMenu.vue
-->
<template>
  <div class="home">
    <Header defaultActive="/propertyManage" />
    <div class="second_menu_nb">
      <div class="menu">
        <div class="project_msg">
          <div>
            <img src="../../assets/images/seawall_menu.png" alt="" />
            <span>{{ projectDetail.name }}</span>
          </div>
          <img
            src="../../assets/images/menu_back.png"
            alt=""
            @click="goBlack()"
          />
        </div>
        <div
          class="menu_parent"
          v-for="item in secondMenuList"
          :key="item.name"
          :class="isShowMenu == item.path ? 'ddd' : 'hide'"
        >
          <div
            class="menu_root"
            :class="defaultActive == item.path ? 'active' : ''"
            :style="
              item.children && item.children.length ? 'margin-bottom:18px' : ''
            "
            @click="selectSecondMenu(item)"
          >
            <img
              class="left_ico"
              :src="defaultActive == item.path ? item.icon_selected : item.icon"
            />
            <span>{{ item.title }}</span>
            <i
              v-if="
                item.children && item.children.length && isShowMenu != item.path
              "
              class="el-icon-plus arrow"
              style="color: #999"
              @click.stop="actionMenu(item)"
            ></i>
            <i
              v-if="
                item.children && item.children.length && isShowMenu == item.path
              "
              class="el-icon-minus arrow"
              style="color: #999"
              @click.stop="actionMenu(item)"
            ></i>
          </div>
          <template v-if="item.children && item.children.length">
            <v-menu :menuList="item.children" :defaultPath="itemDefaultActive">
              <menu-item
                v-for="itemChild in item.children"
                :key="itemChild.name"
                :itemChild="itemChild"
                @setPageTitle="setPageTitle"
              >
                <v-menu
                  :menuList="itemChild.children"
                  :defaultPath="itemChildrenDefaultActive"
                >
                  <menu-item
                    v-for="ite in itemChild.children"
                    :key="ite.name"
                    :itemChild="ite"
                    :showIcon="false"
                    @setPageTitle="setPageChildTitle"
                  >
                  </menu-item>
                </v-menu>
              </menu-item>
            </v-menu>
          </template>
        </div>
      </div>
      <div class="page_wrap">
        <div class="page_title">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>{{ pageTitle0 }}</el-breadcrumb-item>
            <el-breadcrumb-item v-if="pageTitle1 !== ''">{{
              pageTitle1
            }}</el-breadcrumb-item>
            <el-breadcrumb-item v-if="pageTitle2 !== ''">{{
              pageTitle2
            }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="page_box">
          <router-view
            style="width: 100%; height: 100%"
            class="router-box-unit"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import VMenu from "./components/menu.vue";
import menuItem from "./components/menuItem.vue";
import { mapGetters } from "vuex";
export default {
  name: "Home",
  props: {},
  data() {
    return {
      firstDefaultActive: "/propertyManage",
      firstMenuList: [],
      showSecond: false,
      secondMenuList: [],
      pageTitle0: "",
      pageTitle1: "",
      pageTitle2: "",
      menuSearch: "",
      userName: "",
      isShowMenu: "",
      isShowChildMenu: "",
      defaultActive: "",
      itemDefaultActive: "",
      itemChildrenDefaultActive: "",
      pageUrl: "",
    };
  },
  components: { Header, VMenu, menuItem },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
    ...mapGetters(["allRoutesList"]),
  },
  beforeCreate() {},
  created() {},
  watch: {},
  mounted() {
    this.userName = this.$sessionData("get", "userInfo").username || "游客";
    this.userType = this.$sessionData("get", "userInfo").userType;
    
    for (let i = 0; i < this.allRoutesList.length; i++) {
      if (this.allRoutesList[i].name == "projectUnitMenu") {
        this.secondMenuList = this.allRoutesList[i].children;
      }
    }
    this.secondMenuList.map((v, i) => {
      v.icon = require("@/assets/images/detail0.png");
      v.icon_selected = require("@/assets/images/detail1.png");
      this.$set(this.secondMenuList, i, v);
    });
    this.setDefault();
  },
  methods: {
    handleSelect(key, keyPath) {
      this.$router.push(keyPath[0]);
    },
    setDefault() {
      if (this.$route.matched && this.$route.matched.length > 2) {
        this.defaultActive = this.$route.matched[1].path;
        this.isShowMenu = this.$route.matched[1].path;
        this.itemDefaultActive = this.$route.matched[2].path;
        let data = this.secondMenuList.find(
          (item) => item.path == this.defaultActive
        );
        this.pageTitle0 = data.title;
        if (data.children && data.children.length > 0) {
          let childrenData = data.children.find(
            (item) => item.path == this.itemDefaultActive
          );
          this.pageTitle1 = childrenData.title;
          if (childrenData.children && childrenData.children.length > 0) {
            this.itemChildrenDefaultActive = this.$route.path;
            let childrenItemData = childrenData.children.find(
              (item) => item.path == this.itemChildrenDefaultActive
            );
            this.pageTitle2 = childrenItemData.title;
          }
        }
      } else {
        this.defaultActive = this.$route.path;
        this.isShowMenu = this.$route.path;
        let data = this.secondMenuList.find(
          (item) => item.path == this.defaultActive
        );
        this.pageTitle0 = data.title;
      }
    },
    selectSecondMenu(item) {
      this.defaultActive = item.path;
      this.isShowMenu = item.path;
      this.pageTitle0 = item.title;
      this.$router.push(item.path);
      this.itemDefaultActive = "";
      this.itemChildrenDefaultActive = "";
      this.pageTitle1 = "";
      this.pageTitle2 = "";
      if (item.children && item.children.length) {
        this.itemDefaultActive = item.redirect;
        this.setPageTitle(item.children[0]);
      }
      // if (item.children && item.children.length) {
      //   console.log(item, '111111111111111')
      //   this.itemDefaultActive = item.redirect;
      //   console.log(this.itemDefaultActive, '111111111111111')
      //   this.setPageTitle(item.children[0]);
      // } else {
      //   this.pageTitle1 = "";
      //   this.pageTitle2 = "";
      // }
    },
    actionMenu(item) {
      if (this.isShowMenu == item.path) {
        this.isShowMenu = "";
      } else {
        this.isShowMenu = item.path;
        this.selectSecondMenu(item);
      }
    },
    setPageTitle(data) {
      this.pageTitle1 = data.title;
      if (data.children && data.children.length) {
        this.pageTitle2 = data.children[0].title;
        this.itemChildrenDefaultActive = data.redirect;
      } else {
        this.pageTitle2 = "";
        this.itemChildrenDefaultActive = "";
      }
    },
    setPageChildTitle(data) {
      this.pageTitle2 = data.title;
    },
    // actionChildMenu(item) {
    //   if (this.isShowChildMenu == item.path) {
    //     this.isShowMenu = "";
    //   } else {
    //     this.isShowChildMenu = item.path;
    //     this.selectThirdMenu(item);
    //   }
    // },
    goBlack() {
      this.$router.push("/propertyManage/propertyUnitMatter");
    },
    // goScreen() {
    //   window.open("https://slgh.lucheng.gov.cn/synthesizeSystem/");
    // },
    // async logout() {
    //   let res = await logout({
    //     accessToken: this.$sessionData("get", "userInfo").accessToken,
    //     refreshToken: this.$sessionData("get", "userInfo").refreshToken,
    //   });
    //   if (res.code == 0) {
    //     this.$sessionData("clean", "userInfo");
    //     this.$router.push("/login");
    //     window.location.reload();
    //   }
    // },
  },
};
</script>

<style lang='scss' scoped>
.home {
  width: 100%;
  height: 100%;
}
</style>

<style lang="scss">
.router-box-unit {
  > div {
    height: 100%;
  }
}
</style>