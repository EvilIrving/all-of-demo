<!--
 * @Author: dyn
 * @Date: 2021-02-27 18:21:25
 * @LastEditTime: 2021-02-28 16:19:59
 * @LastEditors: Please set LastEditors
 * @Description: 洪涝分析-洪涝分析-右侧
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\floodJudgeNew\components\sidebar\FloodAnalysisRight.vue
-->
<template>
  <div class="sidebar-box">
    <p class="sidebar-title">
      <span><i class="icon-judge-icon"></i> 洪涝分析</span>
      <span>
          <a href="http://122.224.94.107:8011/Home/Index" target="_blank">
            <span>洪涝分析</span>
          </a>
          <i class="icon-animation-arrow" />
        </span>
    </p>
    <div class="right-tab">
      <template>
        <span
          v-for="(name, index) in sidebarTabArr"
          :key="index"
          @click="sidebarTabChange(name)"
          :class="activeSidebar == name ? 'active' : ''"
          >{{ name }}</span
        >
      </template>
    </div>
    <div class="flood-wrap">
      <div class="flood-menu">
        <div
          v-for="(item, index) in menuTab"
          :key="index"
          :class="index == activeTab ? 'active' : ''"
          @click="activeTab = index"
        >
          {{ item }}
        </div>
      </div>
      <p>说明</p>
      <div class="explain" style="text-indent: 25px; line-height: 18px;">
          采用水力学方法模拟温瑞平原遭遇1987年雨型5年一遇洪水，河道洪水漫溢淹没风险。该方案三日雨量为255mm。瓯江选用1971年9月潮型，飞云江选用1989年10月潮型, 东部的瓯飞围区采用瓯飞一期围垦工程设计潮型。计算范围主要瓯江干流以南、飞云江干流以北、西部到瓯海区、瑞安市西部山区，东部到围垦区的平原。计算时长为72h，计算步长为20s。
      </div>
      <p>风险信息统计</p>
      <div class="info-count">
        <div class="info-item" v-for="(item, index) in infoArr" :key="index">
          <span class="info-attribute">{{ item.attribute }}</span>
          <span class="info-num">{{ item.num }}</span>
          <span class="info-unit">{{ item.unit }}</span>
          <div class="line"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { waterApi } from "@/api/dataScreen/floodJudge/index";

export default {
  name: "Sidebar",
  props: {},
  data() {
    return {
      sidebarTabArr: ["淹没水深图", "淹没历时图"],
      activeSidebar: "淹没水深图",
      menuTab: ["灾害统计", "过程线"],
      activeTab: 0,
      infoArr: [
        {
          attribute: "淹没面积",
          num: 45612,
          unit: "万平方千米",
        },
        {
          attribute: "淹没耕地面积",
          num: 45612,
          unit: "万平方千米",
        },
        {
          attribute: "影响人口",
          num: 45612,
          unit: "人",
        },
        {
          attribute: "淹没区GDP",
          num: 45612,
          unit: "万元",
        },
      ],
    };
  },
  watch: {
    $route: {
      handler(nv, ov) { },
    },
    deep: true,
  },
  mounted() { },
  methods: {
    sidebarTabChange(name) {
      this.activeSidebar = name;
    },
  },
};
</script>

<style lang='scss' scoped>
@import "@/style/bigScreenSidebar.scss";
</style>
<style lang="scss">
$searchInpHeight: 25px;
$mainColor: #3f9bfc;
$tdHeight: 37px;
.sidebar-box {
  .right-tab {
    > span {
      display: inline-block;
      width: 120px;
      height: 40px;
      line-height: 40px;
      text-align: center;
      cursor: pointer;
      color: #4898fb;
      background: url(../../../../../assets/images/bigScreen-big-tab.png)
        no-repeat;
      background-size: 100% 100%;
      margin-right: 6px;
      &.active {
        color: #a6e9fb;
        background: url(../../../../../assets/images/bigScreen-big-tab-check.png)
          no-repeat;
        background-size: 100% 100%;
      }
    }
  }
  .sidebar-title {
    line-height: 50px;
    i {
      vertical-align: middle;
    }
  }
  .flood-wrap {
    width: 100%;
    
    height: 544px;
    margin-top: 16px;
    background: url(../../../../../assets/images/flood-bg.png) no-repeat;
    background-size: 100% 100%;
    padding: 10px 26px;
    .flood-menu {
      display: flex;
      height: 40px;
      line-height: 40px;
      text-align: center;
      border-bottom: 2px solid #0f3355;
      position: relative;
      > div {
        position: absolute;
        top: -1px;
        width: 70px;
        color: #f3fcff;
        cursor: pointer;
        &:last-child {
          left: 70px;
        }
        &.active {
          border-bottom: 2px solid #3c93f8;
        }
      }
    }
    > p {
      line-height: 50px;
    }
    .explain,
    .info-count {
      width: 100%;
      height: 160px;
      background: rgba(20, 59, 100, 0.11);
      border: 1px solid rgba(151, 151, 151, 0.11);
      padding: 10px;
      font-size: 14px;
    }
    .info-count {
      height: 210px;
      padding: 10px 26px;
      padding-top: 2px;
      .info-item {
        position: relative;
        height: 40px;
        line-height: 40px;
        width: 100%;
        margin-top: 6px;
        > span {
          display: inline-block;
        }
        .info-attribute {
          width: 48%;
          font-size: 14px;
          color: #ffffff;
        }
        .info-unit {
          width: 60px;
          text-align: left;
          font-size: 10px;
          color: #a6dff6;
          margin-left: 12px;
        }
        .info-num {
          width: calc(48% - 72px);
          text-align: left;
          font-size: 16px;
          color: #fef568;
        }
        .line {
          position: absolute;
          bottom: 0;
          left: 0;
          height: 2px;
          width: 100%;
          background: url(../../../../../assets/images/divide-line.png)
            no-repeat;
          background-size: 100% 100%;
        }
      }
    }
  }
}
</style>