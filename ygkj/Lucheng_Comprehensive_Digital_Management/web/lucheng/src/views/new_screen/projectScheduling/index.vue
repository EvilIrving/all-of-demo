<template>
  <div class="scheduling-box">
    <div class="scheduling-header">
      <div class="scheduling-header-list">
        <div class="scheduling-header-item" :class="{ active_item: activeItem === index }" v-for="(item, index) in headerList" :key="index"
          @click="checkHeaderItem(item.componentName, item.componentInfoName, index)">
          {{ item.title }}
        </div>
      </div>
      <div class="scheduling-header-info">
        <component ref="headerInfo" v-bind:is="currentTabComponent" @dispatchAnalysisChange="controlFloodDispatchTabChange" @getAllProject="(e) => (allProject = e)" @selectedProject="(e) => (selectedProject = e)">
        </component>
      </div>
    </div>
    <div class="scheduling-content">
      <component v-bind:is="contentComponent" :pointData="allProject" :overlayData="selectedProject"></component>
    </div>
  </div>
</template>

<script>
import conceptualizationInfo from './conceptualization/info.vue'; //工程概化图tab选项右边
import conceptualization from './conceptualization/index.vue'; //工程概化图
import controlFloodDispatchInfo from './controlFloodDispatch/info.vue'; //防洪调度图tab选项右边
import controlFloodDispatch from './controlFloodDispatch/index.vue'; //防洪调度图
import sluicesDistributionInfo from './sluicesDistribution/info.vue'; //水闸分布图tab选项右边
import sluicesDistribution from './sluicesDistribution/index.vue'; //水闸分布图
import waterChart from './controlFloodDispatch/waterChart.vue';
import floodRiskMap from './controlFloodDispatch/floodRiskMap.vue';
export default {
  components: {
    conceptualizationInfo,
    conceptualization,
    controlFloodDispatchInfo,
    controlFloodDispatch,
    sluicesDistributionInfo,
    sluicesDistribution,
    waterChart,
    floodRiskMap
  },
  data() {
    return {
      currentTabComponent: 'conceptualizationInfo',
      contentComponent: 'conceptualization',
      headerList: [
        {
          title: '工程概化图',
          componentName: 'conceptualization',
          componentInfoName: 'conceptualizationInfo'
        },
        {
          title: '水闸分布图',
          componentName: 'sluicesDistribution',
          componentInfoName: 'sluicesDistributionInfo'
        },
        {
          title: '防洪调度图',
          componentName: 'controlFloodDispatch',
          componentInfoName: 'controlFloodDispatchInfo'
        }
      ],
      allProject: [],
      selectedProject: [],
      activeItem: 0,
    };
  },
  mounted() {
    let type = this.$route.query.type;
    if (type) {
      this.activeItem = Number(type)
      this.contentComponent = this.headerList[type].componentName;
      this.currentTabComponent = this.headerList[type].componentInfoName;
    }
  },
  methods: {
    checkHeaderItem(componentName, componentInfoName, index) {
      this.contentComponent = componentName;
      this.currentTabComponent = componentInfoName;
      let route = '/projectScheduling?type=' + index;
      this.activeItem = index
      this.$router.push(route);
    },
    /**
     * @description 防洪调度图-调度分析-类型切换时调用
     * @param {String} type - 'baseMap'：地图；'waterLevelChart': 水位图；'riskMap'：风险图
     */
    controlFloodDispatchTabChange(type) {
      switch (type) {
        case 'baseMap':
          this.contentComponent = 'controlFloodDispatch';
          this.selectedProject = [];
          this.allProject = [];
          this.$nextTick(() => {
            this.selectedProject = this.$refs.headerInfo.projectCatalogueList.filter((e) => e.checked);
            this.allProject = this.$refs.headerInfo.projectCatalogueList;
          });
          break;
        case 'waterLevelChart':
          this.contentComponent = 'waterChart';
          break;
        case 'riskMap':
          this.contentComponent = 'floodRiskMap';
          break;
        default:
          break;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.scheduling-box {
  width: 100%;
  height: 100%;
  background-image: url(~assets/new_images/kuang.png);
  background-size: 100% 100%;
  background-repeat: no-repeat;
  box-sizing: border-box;
  padding: 2px;
  .scheduling-header {
    height: 106px;
    border-bottom: 1px solid rgba(0, 186, 255, 0.39);
    display: flex;
    .scheduling-header-list {
      width: 588px;
      height: 100%;
      border-right: 1px solid rgba(0, 186, 255, 0.39);
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 32px 0 30px;
      .scheduling-header-item {
        width: 156px;
        height: 47px;
        background-image: url(~assets/new_images/projectScheduling/scheduling-icon.png);
        background-size: 100% 100%;
        background-repeat: no-repeat;
        line-height: 46px;
        text-align: center;
        cursor: pointer;
        color: #ffffff;
        font-size: 20px;
        &.active_item {
          color: #23e3fb;
          background-image: url(~assets/new_images/projectScheduling/scheduling-active-icon.png);
        }
      }
    }
    .scheduling-header-info {
      width: calc(100% - 589px);
      // background-color: red;
      height: 100%;
    }
  }
  .scheduling-content {
    height: calc(100% - 106px);
  }
}
</style>

<style lang="scss">
.point-sluice-popover {
  background-image: linear-gradient(180deg, #0094c3 2%, #103567 100%);
  color: #ffffff;
  border: 2px solid #00c1ff;
  width: 200px;
  padding: 16px;
  box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
  &.el-popper .popper__arrow {
    border-top-color: #00c1ff;
    &::after {
      border-top-color: #00c1ff;
      bottom: -1px;
    }
  }
}
</style>