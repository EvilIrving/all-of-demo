<!--
 * @Date: 2021-12-09 14:02:47
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-01-11 15:45:16
 * @FilePath: \lucheng_project\src\views\projectDetail\dailyManage\Maintenance.vue
-->
<template>
  <div class="people-box">
    <div class="people-right">
      <el-tabs v-model="selectTab">
        <el-tab-pane v-for="(item, index) in tabList" :label="item.name" :name="item.name" :key="index">
        </el-tab-pane>
      </el-tabs>
      <project-maintenance v-if="selectTab == '维修养护项目维护'" />
      <fund-declaration v-if="selectTab == '维修养护资金申报'" />
      <fund-uses v-if="selectTab == '维修养护资金使用'" />
    </div>
  </div>
</template>

<script>
import ProjectMaintenance from './components/ProjectMaintenance.vue'
import FundUses from './components/FundUses.vue'
import FundDeclaration from './components/FundDeclaration.vue'
export default {
  name: 'Maintenance',
  data() {
    return {
      tabList: [
        {
          name: '维修养护项目维护',
        },
        {
          name: '维修养护资金申报',
        },
        {
          name: '维修养护资金使用',
        },
      ],
      selectTab: '维修养护项目维护',
    };
  },
  mounted() {
  },
  components: { FundDeclaration, FundUses, ProjectMaintenance },
  methods: {

  },
};
</script>

<style lang="scss" scoped>
.people-box {
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  .people-right {
    @include contentBox();
    width: 100%;
  }
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