<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-06 14:36:51
 * @Description: 
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\rescueTeamManage\RescueTeamManageIndex.vue
-->
<template>
  <div class="wrap">
    <div class="tabs_box">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane
          v-for="item in pageList"
          :key="item.title"
          :label="item.title"
          :name="item.title"
        ></el-tab-pane>
      </el-tabs>
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "RescueTeamManageIndex",
  props: {},
  computed: {
    ...mapGetters(["routesList"]),
    pageList: function () {
      let homeList = this.routesList.find((item) => item.name == "home");
      let seawallPreventList = homeList.children.find(
        (item) => item.name == "seawallPrevent"
      );
      let refugeManageList = seawallPreventList.children.find(
        (item) => item.name == "refugeManage"
      );
      let rescueTeamManage = refugeManageList.children.find(
        (item) => item.name == "rescueTeamManage"
      );
      return rescueTeamManage.children
    },
  },
  mounted() {
    console.log(this.pageList, 'pageListpageListpageList')
  },
  data() {
    return {
      activeName: "救援队伍",
      // pageList: [
      //   {
      //     name: "救援队伍",
      //     path: "/seawallPrevent/rescueTeamManage/team",
      //   },
      //   {
      //     name: "队伍人员管理",
      //     path: "/seawallPrevent/rescueTeamManage/people",
      //   },
      //   {
      //     name: "队伍设备管理",
      //     path: "/seawallPrevent/rescueTeamManage/equip",
      //   },
      // ],
    };
  },
  methods: {
    handleClick(val) {
      this.$router.push(this.pageList[val.index].path);
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  @include contentBox();
  height: 100%;
}
.el-tabs {
  width: 100%;
  padding-top: 10px;
  font-size: 14px;
  ::v-deep .el-tabs__header {
    padding: 0 26px;
    margin: 0;
  }
  ::v-deep .el-tabs__item {
    color: rgba(48, 49, 51, 0.5);
    &.is-active {
      color: #1492ff;
    }
  }
  .el-tabs__active-bar {
    background: #1492ff;
  }
  ::v-deep .el-tabs__content {
    width: 100%;
    .el-tab-pane {
      width: 100%;
      height: 100%;
    }
  }
}
</style>