<!--
 * @Author: wanxin
 * @LastEditTime: 2022-03-07 14:16:22
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\projectDetail\dailyManage\EngineeringInspection.vue
-->
<template>
  <div class="people-box">
    <div class="people-right">
      <el-tabs v-model="selectTab">
        <el-tab-pane v-for="(item, index) in tabList" :label="item.name" :name="item.name" :key="index">
        </el-tab-pane>
      </el-tabs>
      <project-patrol v-if="selectTab == '工程巡查'" />
      <flood-inspection v-if="selectTab == '汛前检查'" />
      <annual-inspection v-if="selectTab == '年度检查'" />
      <!-- <special-inspection v-if="selectTab == '特别检查'" /> -->
      <termite-inspection v-if="selectTab == '白蚁检查'" />
    </div>
  </div>
</template>

<script>
import ProjectPatrol from './components/ProjectPatrol.vue'
import FloodInspection from './components/FloodInspection.vue'
import AnnualInspection from './components/AnnualInspection.vue'
import SpecialInspection from './components/SpecialInspection.vue'
import TermiteInspection from './components/TermiteInspection.vue'
export default {
  name: 'Maintenance',
  data() {
    return {
      tabList: [
        {
          name: '工程巡查',
        },
        {
          name: '汛前检查',
        },
        {
          name: '年度检查',
        },
        // {
        //   name: '特别检查',
        // },
        {
          name: '白蚁检查',
        },
      ],
      selectTab: '工程巡查',
    };
  },
  mounted() {
  },
  components: { ProjectPatrol, FloodInspection, AnnualInspection, SpecialInspection, TermiteInspection },
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