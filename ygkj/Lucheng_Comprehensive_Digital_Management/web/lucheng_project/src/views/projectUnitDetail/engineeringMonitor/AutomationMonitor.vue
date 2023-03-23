<!--
 * @Author: wanxin
 * @LastEditTime: 2022-05-25 09:14:36
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\engineeringMonitor\AutomationMonitor.vue
-->
<template>
  <div class="wrap">
    <div class="tabs_box">
      <el-tabs v-model="selectTab" >
        <el-tab-pane v-for="(item, index) in tabList" :label="item.name" :name="item.name" :key="index">
        </el-tab-pane>
      </el-tabs>
      <seepage-monitoring v-if="selectTab == '渗流监测'" />
      <settlement-monitoring v-if="selectTab == '沉降监测'" />
    </div>
  </div>
</template>

<script>
import SeepageMonitoring from './components/SeepageMonitoring.vue'
import SettlementMonitoring from './components/SettlementMonitoring.vue'
export default {
  data() {
    return {
      tabList: [
        {
          name: '渗流监测',
        },
        {
          name: '沉降监测',
        },
      ],
      selectTab: '渗流监测',
    }
  },
  components: { SeepageMonitoring, SettlementMonitoring },

}
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .tabs_box {
    height: 100%;
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