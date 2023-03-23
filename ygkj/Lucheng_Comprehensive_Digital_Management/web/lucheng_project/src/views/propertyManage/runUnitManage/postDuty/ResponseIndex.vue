<template>
  <div class='wrap'>
    <div class="tabs_box">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane v-for="item in pageList" :key="item.name" :label="item.name" :name="item.name"></el-tab-pane>
      </el-tabs>
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'ResponseIndex',
  props: {
    
  },
  components: {
    
  },
  data () {
    return {
      activeName: '单位负责人',
      pageList: [
        {
          name: '单位负责人',
          path: '/propertyManage/runUnitResponse'
        },
        {
          name: '技术负责人',
          path: '/propertyManage/runTechResponse'
        },
        {
          name: '巡查责任人',
          path: '/propertyManage/runMoneyResponse'
        }
      ]
    };
  },
  mounted() {
    
  },
  methods: {
    handleClick(val){
      this.$router.push(this.pageList[val.index].path)
    }
  }
}
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