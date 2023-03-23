<template>
  <div class="main-header" >
    <div class="left-part">
      <i class="title-icon icon-wenzhou-logo"></i>
      <span class="title">温州市水平台</span>
      <!-- <div class="sub-title">{{ systemSelect }}</div> -->
      <el-select
        popper-class="header-select-poper"
        v-model="systemSelect"
        @change="systemChange"
      >
        <el-option
          v-for="(item, index) in systemList"
          :key="index"
          :label="item.name"
          :value="item.value"
        >
        </el-option>
      </el-select>
    </div>
    <div class="main-menu">
      <ul>
        <li
          :class="[index == menuSelect ? 'icon-menu-active' : '']"
          :key="index"
          v-for="(item, index) in mainMenuList"
          @click="mainMenuClick(index)"
        >
          <i
            :class="index == menuSelect ? item.iconb : item.icon"
            style="vertical-align: middle"
          ></i>
          {{ item.label }}
        </li>
      </ul>
    </div>
    <div class="right-part">
      <i @click="logout" class="logout icon-header-logout"></i>
      <i class="user-icon icon-default-user"></i>
      <span>{{ userName }}</span>
    </div>
  </div>
</template>

<script>
import MENULIST from "@/config/menus.js";
import { logout } from "@/api/login.js";
export default {
  name: "MainHeader",
  data() {
    return {
      systemSelect: "",
      systemList: [
        {
          name: "防洪减灾调度支撑系统",
          value: "http://117.149.228.207:8081/FloodDisasterDispatch/#/",
        },
        {
          name: "水文数字化系统",
          value: "http://117.149.228.207:8081/hydrology/#/",
        },
        {
          name: "水旱灾害监测预报",
          value: "http://117.149.228.207:8081/floodDrought/#/",
        },
      ],
      menuSelect: 0,
      mainMenuList: [],
      userName: "",
    };
  },
  watch: {
    /* systemSelect: function (val) {
      this.systemList.map((item) => {
        if (item.name === this.systemSelect) {
          this.mainMenuList = item.children;
        }
      });
      this.$store.commit("setLayerList", this.mainMenuList[0].layerList);
    }, */
  },
  mounted() {
    this.mainMenuList = MENULIST[0].children;
    console.log(this.mainMenuList,111)
    this.systemSelect = this.systemList[0].value;
    this.$nextTick(() => {
      this.$emit("menuClick", this.mainMenuList[0]);
    });
    this.userName = this.$localData("get", "userInfo").username;
  },
  methods: {
    systemChange(item) {
      window.location.href = item;
    },
    mainMenuClick(index) {
      this.menuSelect = index;
      this.$store.commit("setMapMenu", index);
      this.$emit("menuClick", this.mainMenuList[index]);
      this.$store.commit("setLayerList", this.mainMenuList[index].layerList);
    },
    async logout() {
      const userInfo = this.$localData("get", "userInfo");
      const opt = {
        accessToken: userInfo.accessToken,
        refreshToken: userInfo.refreshToken,
      };
      await logout(opt).then((res) => {
        if (res.code === 0) {
          this.$message({
            message: "退出成功",
            type: "success",
          });
          this.$localData("clean", "userInfo");
          this.$router.push("/login");
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./style.scss";
</style>

<style lang="scss">
@import "@/style/theme.scss";
.header-select-poper {
  .el-scrollbar {
    .el-select-dropdown__wrap.el-scrollbar__wrap {
      overflow: hidden;
      margin-bottom: 0px !important;
    }
  }
  .el-select-dropdown__item {
    span {
      font-size: 22px;
      letter-spacing: 1.3px;
    }
    &.selected {
      color: $primaryBorderColor;
    }
  }
}
</style>