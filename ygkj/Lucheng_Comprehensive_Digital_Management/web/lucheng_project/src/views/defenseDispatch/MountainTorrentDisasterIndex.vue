<!--
 * @Author: wanxin
 * @LastEditTime: 2022-07-06 14:58:23
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\defenseDispatch\MountainTorrentDisasterIndex.vue
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
  name: "MountainTorrentDisasterIndex",
  props: {},
  computed: {
    ...mapGetters(["routesList"]),
    pageList: function () {
      let homeList = this.routesList.find((item) => item.name == "home");
      let defenseDispatch = homeList.children.find(
        (item) => item.name == "defenseDispatch"
      );
      
      let comprehensive = defenseDispatch.children.find(
        (item) => item.name == "comprehensive"
      );
      let dataSheet = comprehensive.children.find(
        (item) => item.name == "dataSheet"
      );
      return dataSheet.children;
    },
  },
  data() {
    return {
      activeName: "山洪灾害责任人表",
      // pageList: [
      //   {
      //     name: "山洪灾害责任人表",
      //     path: "/defenseDispatch/personLiable",
      //   },
      //   {
      //     name: "渔船名单",
      //     path: "/defenseDispatch/fishingBoatNameList",
      //   },
      //   {
      //     name: "种粮大户名单",
      //     path: "/defenseDispatch/largeGrainGrowersNameList",
      //   },
      //   {
      //     name: "山洪灾害防御对象",
      //     path: "/defenseDispatch/defenseObject",
      //   },
      // ],
    };
  },
  mounted() {},
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