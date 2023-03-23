<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-05 10:33:28
 * @Description: 海塘详情页面框架
 * @FilePath: \lucheng_project\src\views\projectDetail\ProjectMenu.vue
-->
<template>
  <div class="home">
    <Header defaultActive="/seawallPrevent" />
    <!-- <div class="layout_top">
      <div class="layout_top-title">鹿城区水利全域数字化管护平台</div>
      <div class="layout_top-first_menu">
        <el-menu
          :default-active="firstDefaultActive"
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
        >
          <el-menu-item
            :index="item.path"
            v-for="(item, index) in firstMenuList"
            :key="index"
            >{{ item.title }}</el-menu-item
          >
        </el-menu>
      </div>

      <div class="layout_top-person">
        <div class="line"></div>
        <div class="layout_top-img">
          <img src="@/assets/images/open_msg.png" alt="" />
        </div>
        <el-popover placement="bottom" width="60" trigger="hover">
          <div class="userMenu">
            <p @click="logout()">退出登录</p>
          </div>
          <div class="person_head" slot="reference">
            <span :title="userName">{{ userName }}</span>
          </div>
        </el-popover>
      </div>
    </div> -->
    <div class="second_menu_nb">
      <div class="menu">
        <div class="project_msg">
          <div>
            <img src="../../assets/images/seawall_menu.png" alt="" />
            <span>{{ seawallDetail.seawallName }}</span>
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
            <!-- <i
              v-if="item.children && item.children.length"
              class="el-icon-plus arrow"
              
              :class="isShowMenu == item.path ? 'close' : ''"
              @click.stop="actionMenu(item)"
            ></i> -->
          </div>
          <template v-if="item.children && item.children.length">
            <div v-for="itemChild in item.children" :key="itemChild.name">
              <div
                class="menu_item"
                @click="selectThirdMenu(itemChild)"
                v-if="!itemChild.hide"
                :class="itemDefaultActive == itemChild.path ? 'active' : ''"
              >
                <img
                  v-if="itemDefaultActive == itemChild.path"
                  src="../../assets/images/menu_style1.png"
                  alt=""
                />
                <img
                  v-if="itemDefaultActive != itemChild.path"
                  src="../../assets/images/menu_style0.png"
                  alt=""
                />
                <span class="menu_item_word" :title="itemChild.title">{{
                  itemChild.title
                }}</span>
              </div>
            </div>
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
          </el-breadcrumb>
        </div>
        <div
          class="page_box2"
          v-if="
            pageTitle1 == '测点管理' ||
            pageTitle1 == '数据监测' ||
            pageTitle1 == '数据分析'
          "
        >
          <router-view style="width: 100%; height: 100%" />
        </div>
        <div class="page_box" v-else>
          <router-view style="width: 100%; height: 100%" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { logout } from "@/api/index";
import { mapGetters } from "vuex";
import Header from "@/components/Header.vue";
export default {
  name: "Home",
  props: {},
  data() {
    return {
      firstDefaultActive: "/seawallPrevent",
      firstMenuList: [],
      showSecond: false,
      secondMenuList: [],
      pageTitle0: "",
      pageTitle1: "",
      menuSearch: "",
      userName: "",
      isShowMenu: "",
      defaultActive: "",
      itemDefaultActive: "",
      pageUrl: "",
    };
  },
  components: {Header},
  computed: {
    seawallDetail() {
      return this.$store.state.seawallDetail;
    },
    ...mapGetters(["allRoutesList"]),
  },
  beforeCreate() {},
  created() {},
  watch: {},
  mounted() {
    this.userName = this.$sessionData("get", "userInfo").username || "游客";
    this.userType = this.$sessionData("get", "userInfo").userType;
    let routers = this.allRoutesList.filter((item) => item.name == "home")[0];
    this.firstMenuList = routers.children;
    // this.firstDefaultActive = "/" + this.$route.path.split("/")[1];
    // for(let i = 0; i < this.$router.options.routes.length; i++){
    //   if(this.$router.options.routes[i].name == 'home'){
    //     if(this.userType == '3'){
    //       for(let j = 0; j < this.$router.options.routes[i].children.length; j++){
    //         if(
    //           this.$router.options.routes[i].children[j].title !== '水域占用审批' &&
    //           this.$router.options.routes[i].children[j].title !== '灾害防御调度' &&
    //           this.$router.options.routes[i].children[j].title !== '浙政钉应用' &&
    //           this.$router.options.routes[i].children[j].title !== '数字大屏'
    //         ){
    //           if(this.$router.options.routes[i].children[j].title == '工程物业管理' || this.$router.options.routes[i].children[j].title == '海塘防潮评估'){
    //             this.$router.options.routes[i].children[j].children = this.$router.options.routes[i].children[j].children.slice(0,1)
    //           }
    //           this.firstMenuList.push(this.$router.options.routes[i].children[j])
    //         }
    //       }
    //     }else{
    //       if(this.userName !== '游客'){
    //         this.firstMenuList = this.$router.options.routes[i].children
    //       }else{
    //         for(let j = 0; j < this.$router.options.routes[i].children.length; j++){
    //           if(
    //             this.$router.options.routes[i].children[j].title !== '工程物业管理' &&
    //             this.$router.options.routes[i].children[j].title !== '水域占用审批' &&
    //             this.$router.options.routes[i].children[j].title !== '灾害防御调度' &&
    //             this.$router.options.routes[i].children[j].title !== '浙政钉应用'
    //           ){
    //             this.firstMenuList.push(this.$router.options.routes[i].children[j])
    //           }
    //         }
    //       }
    //     }
    //   }
    // }
    // this.firstDefaultActive = "/" + this.$route.path.split("/")[1];
    // for(let i = 0; i < this.$router.options.routes.length; i++){
    //   if(this.$router.options.routes[i].name == 'home'){
    //     this.firstMenuList = this.$router.options.routes[i].children
    //   }
    // }
    console.log(this.$router.options.routes)
    for (let i = 0; i < this.allRoutesList.length; i++) {
      if (this.allRoutesList[i].name == "projectMenu") {
        this.secondMenuList = this.allRoutesList[i].children;
      }
    }
    this.secondMenuList.map((v, i) => {
      v.icon = require("@/assets/images/detail0.png");
      v.icon_selected = require("@/assets/images/detail1.png");
      this.$set(this.secondMenuList, i, v);
    });
    this.pageTitle0 = this.secondMenuList.length
      ? this.secondMenuList[0].title
      : "";
    this.defaultActive = this.secondMenuList.length
      ? this.secondMenuList[0].path
      : "";
    this.isShowMenu = this.secondMenuList.length
      ? this.secondMenuList[0].path
      : "";
  },
  methods: {
    handleSelect(key, keyPath) {
      this.$router.push(keyPath[0]);
    },

    selectSecondMenu(item) {
      this.defaultActive = item.path;
      this.isShowMenu = item.path;
      this.$router.push(item.path);
      this.pageTitle0 = item.title;
      if (item.children && item.children.length) {
        this.pageTitle1 = item.children[0].title;
        this.itemDefaultActive = item.redirect;
      } else {
        this.pageTitle1 = "";
      }
    },

    selectThirdMenu(item) {
      this.itemDefaultActive = item.path;
      this.$router.push(item.path);
      this.pageTitle1 = item.title;
    },

    actionMenu(item) {
      if (this.isShowMenu == item.path) {
        //如果已展开那么收缩
        this.isShowMenu = "";
      } else {
        this.isShowMenu = item.path;
      }
    },

    goBlack() {
      this.$router.push("/seawallPrevent/projectList");
    },
    goScreen() {
      window.open("https://slgh.lucheng.gov.cn/synthesizeSystem/");
    },
    async logout() {
      let res = await logout({
        accessToken: this.$sessionData("get", "userInfo").accessToken,
        refreshToken: this.$sessionData("get", "userInfo").refreshToken,
      });
      if (res.code == 0) {
        this.$sessionData("clean", "userInfo");
        this.$router.push("/login");
        window.location.reload();
      }
    },
  },
};
</script>

<style lang='scss' scoped>
.home {
  width: 100%;
  height: 100%;
}
</style>