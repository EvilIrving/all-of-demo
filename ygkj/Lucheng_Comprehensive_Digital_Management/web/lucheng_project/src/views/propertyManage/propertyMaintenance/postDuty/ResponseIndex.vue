<template>
  <div class="warp-page">
    <div class="warp-unit-list" v-if="userInfo.userType != 3">
      <unit-list @checkItem="checkItem" />
    </div>
    <div class="wrap-box" :class="{ 'wrap-type': userInfo.userType == 3 }">
      <div class="tabs_box">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane
            v-for="item in pageList"
            :key="item.name"
            :label="item.name"
            :name="item.name"
          ></el-tab-pane>
        </el-tabs>
      </div>
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import UnitList from "@/components/UnitList.vue";
export default {
  name: "ResponseIndexPro",
  props: {},
  components: { UnitList },
  data() {
    return {
      activeName: "单位负责人",
      pageList: [
        {
          name: "单位负责人",
          path: "/propertyManage/proUnitResponse",
        },
        {
          name: "技术负责人",
          path: "/propertyManage/proTechResponse",
        },
        {
          name: "巡查负责人",
          path: "/propertyManage/proMoneyResponse",
        },
      ],
      userInfo: {},
    };
  },
  mounted() {
    this.userInfo = this.$sessionData("get", "userInfo");
    if (this.userInfo.userType == 3) {
      this.deptId = this.userInfo.deptId;
      this.setDeptId(this.deptId);
    }
  },
  methods: {
    ...mapActions(["setDeptId"]),
    handleClick(val) {
      this.$router.push(this.pageList[val.index].path);
    },
    checkItem(item) {
      this.deptId = item.id;
      this.setDeptId(this.deptId);
    },
  },
};
</script>

<style lang='scss' scoped>
.warp-page {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
}
.warp-unit-list {
  @include contentBox();
  height: 100%;
  width: 255px;
}
.wrap-box {
  @include contentBox();
  height: 100%;
  width: calc(100% - 275px);
}
.wrap-type {
  width: 100%;
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