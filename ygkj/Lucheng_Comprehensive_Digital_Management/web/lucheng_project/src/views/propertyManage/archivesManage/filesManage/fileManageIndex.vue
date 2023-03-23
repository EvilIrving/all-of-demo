<template>
  <div class="primary-view file_manage_index-page">
    <el-tabs v-model="selectTab" @tab-click="tabClick">
      <el-tab-pane
        v-for="(item, index) in tabList"
        :label="item.title"
        :name="item.title"
        :key="index"
      >
      </el-tab-pane>
    </el-tabs>
    <div class="view-content with_tab">
      <router-view />
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "FileManageIndex",
  components: {},
  computed: {
    ...mapGetters(["routesList"]),
    tabList: function () {
      let homeList = this.routesList.find((item) => item.name == "home");
      let propertyManage = homeList.children.find(
        (item) => item.name == "propertyManage"
      );
      let archivesManage = propertyManage.children.find(
        (item) => item.name == "archivesManage"
      );
      let filesManage = archivesManage.children.find(
        (item) => item.name == "filesManage"
      );
      return filesManage.children;
    },
  },
  data() {
    return {
      selectTab: "",
    };
  },
  created() {},
  mounted() {
    this.selectTab = this.tabList[0].title;
    this.tabClick(this.tabList[0]);
  },
  methods: {
    tabClick(val) {
      let index = this.tabList.findIndex((item) => item.title == this.selectTab);
      this.$router.push(this.tabList[index].path);
    },
  },
};
</script>
<style lang="scss" scoped>
@import "@/style/theme.scss";
.primary-view {
  width: 100%;
  height: 100%;
  position: relative;
  @include contentBox();
  &.bg_white .view-content,
  &.bg_white .el-tabs {
    background: #fff;
    box-shadow: 0 0 0.125rem 0 rgb(14 31 53 / 12%),
      0 0.0625rem 0.25rem 0 rgb(14 31 53 / 6%);
  }
  .no_padding {
    padding: 0;
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
  .view-content {
    width: 100%;
    height: 100%;
    &.with_tab {
      height: calc(100% - 50px);
    }
  }
  .tab-list {
    font-weight: bold;
    display: flex;
    padding: 6px 22px 26px 0;
    font-size: 24px;
    color: rgba(102, 102, 102, 0.7);
    span {
      display: inline-block;
      margin-right: 24px;
      cursor: pointer;
      &:hover,
      &.active {
        color: #1492ff;
      }
    }
  }
  // .technical-support {
  //   position: absolute;
  //   bottom: 16px;
  //   width: calc(100% - 32px);
  //   font-size: 14px;
  //   color: #ffffff;
  //   text-align: center;
  // }
}
</style>