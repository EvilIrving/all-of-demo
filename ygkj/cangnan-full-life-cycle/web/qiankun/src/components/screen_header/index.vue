<template>
  <el-header class="main-header" style="padding: 0">
    <div class="main-header__float">
      <div class="main-header__float--logo">
        <div class="logo">温州市水平台</div>
        <el-collapse-transition>
          <div
            class="system-name"
            style="border-left: 2px solid #fff; padding-left: 15px"
          >
            <el-select
              @change="changeSystem"
              v-model="systemType"
              style="width: 165px"
            >
              <el-option
                :key="index"
                v-for="(item, index) in systemList"
                :label="item.value"
                :value="item.name"
              ></el-option>
              <!-- <el-option label="数据管理系统" value="数据管理系统"
                >数据管理系统</el-option  
              > -->
            </el-select>
          </div>
        </el-collapse-transition>
      </div>
      <div class="main-header__float--action">
        <el-badge
          v-show="MenuName == '业务场景'"
          :hidden="currentWarningCount <= 0"
          :value="currentWarningCount"
          :max="99"
          class="warning-badge"
        >
          <div
            :class="
              currentWarningCount > 0 ? 'notification-warning' : 'notification'
            "
          />
        </el-badge>
        <div
          class="analysis"
          slot="reference"
          @click="SetWorkPlaceVisible(true)"
        >
          我的工作台
        </div>
        <div class="user-info">
          <i class="user-icon"></i>
          <span>{{ userName }}</span>
        </div>
      </div>
    </div>
    <div class="main-header__content">
      <el-collapse-transition>
        <div v-show="collapse" class="main-header__content--menu">
          <div class="menu__list">
            <div
              v-for="(item, index) in menuList"
              :key="index"
              :class="['menu__list--item', SelectMenu == index ? 'active' : '']"
              @click="hanleMenuClick(index)"
            >
              <router-link :to="item.path" cutsom>
                <i :class="['menu-icon', item.iconClass]"></i>
                {{ item.title }}
              </router-link>
            </div>
          </div>
        </div>
      </el-collapse-transition>
      <!-- <div class="main-header__content--collaspe-btn" v-show="MenuName == '业务场景'">
        <div
          :class="['collapse-btn', collapse ? 'up' : 'down']"
          @click="handleCollapseClick"
        />
      </div> -->
    </div>
    <police-dialog :dialogVisible="dialogVisible" @handleClose="handleClose" />
    <WorkplaceDialog
      :visiable="WorkplaceVisible"
      @close="SetWorkPlaceVisible(false)"
    />
  </el-header>
</template>

<script>
import policeDialog from "./components/policeDialog.vue";
import WorkplaceDialog from "../screen_workplace/index.vue";
import actions from "./actions";
import { menuApi } from "@/api/menus";
import { mapActions, mapGetters } from "vuex";
import { login } from "@/api/login";
export default {
  name: "ScreenHeader",
  props: {},
  components: { policeDialog, WorkplaceDialog },
  data() {
    return {
      collapse: true,
      workplaceVisible: false,
      dialogVisible: false,
      menuList: [
        {
          title: "业务场景",
          path: "/water-disaster-custom/waterDisasterCustom?large=true&isBigScreen=false&bsType=waterDisaster",
          iconClass: "business-type"
        },
        {
          title: "水利地图",
          path: "/synthesize-amap/projectSide?selectType=水库",
          iconClass: "map-type"
        },
        {
          title: "日常工作",
          path: "/",
          iconClass: "work-type"
        },
        {
          title: "数据报表",
          path: "/datamanagement/statement",
          iconClass: "sheet-type"
        },
        {
          title: "知识档案",
          path: "/hydrolo-gy/knowledgeRecord",
          iconClass: "sheet-type"
        },
      ],
      systemList: [],
      currentWarningCount: 0,
      selectMenu: 0,
      systemType: "",
      userName: "",
    };
  },
  created() {
    this.doLogin();
    this.getMenus().then(() => {
    });
  },
  mounted() {
    this.$nextTick(() => {
      this.$router.push('/water-disaster-custom/waterDisasterCustom?large=true&isBigScreen=false&bsType=waterDisaster');
    })
    // console.log(this.$route,333);
    // 注册一个观察者函数
    // actions.onGlobalStateChange((state, prevState) => {
    //   // state: 变更后的状态; prevState: 变更前的状态
    //   console.log("主应用观察者：collapse 改变前的值为 ", prevState.collapse);
    //   console.log("主应用观察者：登录状态发生改变，改变后的 collapse 的值为 ", state.collapse);
    // });
  },
  beforeDestroy() {
  },
  watch: {
    SystemType(val){
      this.systemType = val;
    }
  },
  computed: {
    ...mapGetters(["SystemType", "MenuName", "WorkplaceVisible", "SelectMenu"]),
  },
  methods: {
    ...mapActions(["SetSystemType", "SetMenuName", "SetWorkPlaceVisible", "SetSelectMenu"]),
    async doLogin() {
      let opt = {
        username: 15858500288,
        password: "ygkj0818"
      }
      await login(opt).then((res) => {
        if (res.code == 0) {
          let opt = res.data;
          this.userName = opt.username;
          opt.timestamp = new Date().getTime();
          this.$localData("set", "userInfo", opt);
        } else {
          this.$message.error(res.message);
        }
      });
    },
    changeSystem(val) {
      this.$emit("changeSystem", val)
      this.SetSystemType(val);
      actions.setGlobalState({ bsName: val });
    },
    handleClose() {
      this.dialogVisible = false;
    },
    handleCollapseClick() {
      this.collapse = !this.collapse;
      actions.setGlobalState({ collapse: this.collapse });
      // this.$emit("change", this.collapse);
    },
    hanleMenuClick(index) {
      // this.selectMenu = index;
      // this.$emit("hanleMenuClick", this.menuList[index].title)
      this.$nextTick(() => {
        this.SetSelectMenu(index);
        this.SetMenuName(this.menuList[index].title);
      })
      // if (index != this.currentRoute){
      // this.SetComponentLoading(true);
      // this.SetCheckedArr([]);
      // this.$router.push(this.menuList[index].path);
      // const bsType = this.$route.query.bsType;
      // this.SetBsName(this.BsNameObj[bsType]);
      // this.reload();
      // }
    },
    async getMenus() {
      await menuApi.getMenus({}).then(res => {
        this.systemList = res.data.menus;
        this.systemType = this.systemList[0].name;
        this.SetSystemType(this.systemType);
      })
    }
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
.system-name {
  .el-input--suffix {
    .el-select {
      -moz-user-select: none;
      -webkit-user-select: none;
      user-select: none;
    }
    .el-input__inner {
      background: none;
      border: none;
      font-size: 24px;
      padding: 0;
      color: #fff;
      -moz-user-select: none;
      -webkit-user-select: none;
      user-select: none;
      font-family: "YouSheBiaoTiHei";
    }
  }
}
</style>

