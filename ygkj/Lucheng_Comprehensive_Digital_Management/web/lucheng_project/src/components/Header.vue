<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-10-21 16:30:16
 * @Description: 
 * @FilePath: \lucheng_project\src\components\Header.vue
-->
<template>
  <div class="layout_top">
    <div class="layout_top-title">鹿城区水利全域数字化</div>
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
        <img
          @click="goDataManage"
          src="@/assets/images/data-manage.png"
          alt=""
        />
      </div>
      <div class="layout_top-img">
        <img src="@/assets/images/open_msg.png" alt="" />
      </div>
      <el-popover placement="bottom" width="60" trigger="hover">
        <div class="userMenu">
          <div class="out-info" @click="dialogPassVisible = true">修改密码</div>
          <div class="out-info" @click="logout()">退出登录</div>
        </div>
        <div class="person_head" slot="reference">
          <span :title="userName">{{ userName }}</span>
        </div>
      </el-popover>
    </div>
    <el-dialog
      title="修改密码"
      :visible.sync="dialogPassVisible"
      width="600px"
      custom-class="msg-dialog-box"
    >
      <password-form
        v-if="dialogPassVisible"
        @closeEditDialog="dialogPassVisible = false"
      ></password-form>
    </el-dialog>
  </div>
</template>

<script>
import { logout } from "@/api/index";
import { mapGetters } from "vuex";
import store from "@/store/index";
import PasswordForm from "@/components/PasswordForm";
export default {
  props: {
    defaultActive: {
      type: String,
      default: "",
    },
  },
  components: {
    PasswordForm,
  },
  data() {
    return {
      firstDefaultActive: "",
      userName: "",
      userType: "",
      firstMenuList: [],
      dialogPassVisible: false,
    };
  },
  computed: {
    ...mapGetters(["allRoutesList"]),
  },
  mounted() {
    console.log(this.$router.options.routes, "dasdasdsad");
    this.userName = this.$sessionData("get", "userInfo").username || "游客";
    this.userType = this.$sessionData("get", "userInfo").userType;
    let routers = this.allRoutesList.filter((item) => item.name == "home")[0];
    this.firstMenuList = routers.children;
    this.firstDefaultActive = this.defaultActive;
    //this.setRouteList();
  },
  methods: {
    async logout() {
      let res = await logout({
        accessToken: this.$sessionData("get", "userInfo").accessToken,
        refreshToken: this.$sessionData("get", "userInfo").refreshToken,
      });
      if (res.code == 0) {
        store.commit("ADD_ROUTES", []);
        this.$sessionData("clean", "userInfo");
        this.$router.push("/login");
        window.location.reload();
      }
    },
    goDataManage() {
      window.open("https://slgh.lucheng.gov.cn/dataManage/dataSheetManage");
    },
    handleSelect(key, keyPath) {
      console.log(keyPath, "dasdas");
      if (key == "/bigScreen") {
        window.open("https://slgh.lucheng.gov.cn/synthesizeSystem/");
        return false;
      }
      // 若点击数据管理系统 直接跳转
      if (key == "/dataManage") {
        window.open("https://slgh.lucheng.gov.cn/dataManage/dataSheetManage");
        return;
      }
      this.$router.push(keyPath[0]);
    },
    setRouteList() {
      for (let i = 0; i < this.$router.options.routes.length; i++) {
        if (this.$router.options.routes[i].name == "home") {
          if (this.userType == "3") {
            for (
              let j = 0;
              j < this.$router.options.routes[i].children.length;
              j++
            ) {
              if (
                this.$router.options.routes[i].children[j].title !==
                  "水域占用审批" &&
                this.$router.options.routes[i].children[j].title !==
                  "灾害防御调度" &&
                this.$router.options.routes[i].children[j].title !==
                  "浙政钉应用" &&
                this.$router.options.routes[i].children[j].title !== "数字大屏"
              ) {
                if (
                  this.$router.options.routes[i].children[j].title ==
                    "工程物业管理" ||
                  this.$router.options.routes[i].children[j].title ==
                    "海塘防潮评估"
                ) {
                  this.$router.options.routes[i].children[j].children =
                    this.$router.options.routes[i].children[j].children.slice(
                      0,
                      1
                    );
                }
                this.firstMenuList.push(
                  this.$router.options.routes[i].children[j]
                );
              }
            }
          } else {
            if (this.userName !== "游客") {
              this.firstMenuList = this.$router.options.routes[i].children;
            } else {
              for (
                let j = 0;
                j < this.$router.options.routes[i].children.length;
                j++
              ) {
                if (
                  this.$router.options.routes[i].children[j].title !==
                    "工程物业管理" &&
                  this.$router.options.routes[i].children[j].title !==
                    "水域占用审批" &&
                  this.$router.options.routes[i].children[j].title !==
                    "灾害防御调度" &&
                  this.$router.options.routes[i].children[j].title !==
                    "浙政钉应用"
                ) {
                  this.firstMenuList.push(
                    this.$router.options.routes[i].children[j]
                  );
                }
              }
            }
          }
        }
      }
    },
  },
};
</script>

<style lang="scss">
.out-info {
  padding: 0 10px 10px;
  cursor: pointer;
  &:last-child {
    padding-bottom: 0;
  }
}
</style>