<!--
 * @Author: dyn
 * @Date: 2021-10-09 10:23:14
 * @LastEditTime: 2021-10-09 17:52:56
 * @LastEditors: Please set LastEditors
 * @Description: 诊新版本
 * @FilePath: \cangnan-app\src\views\projectDetail\diagnosis\DiagnosisNew.vue
-->
<template>
  <div class="wrap">
    <div class="detail-menu">
      <div
        :class="['detail-menu-item', menuIndex === index ? 'active' : '']"
        v-for="(item, index) in tabs"
        :key="item.name"
        @click="clickMenu(item, index)"
      >
        {{ item.title }}
      </div>
    </div>
    <component :is="componentName"> </component>
  </div>
</template>

<script>
import Governor from "../governor/Governor";
import Warn from "../warn/Warn";
import Diagnosis from "./Diagnosis";
import Know from "../know/Know";
import Experience from "./components/Experience";

export default {
  name: "",
  props: {},
  components: { Governor, Warn, Diagnosis, Know, Experience },
  data() {
    return {
      menuIndex: 0,
      tabs: [
        {
          title: "问题督查",
          name: "Governor",
        },
        {
          title: "三码督查",
          name: "Warn",
        },
        {
          title: "专家库",
          name: "Diagnosis",
        },
        {
          title: "知识库",
          name: "Know",
        },
        {
          title: "经验库",
          name: "Experience",
        },
      ],
      type: 1,
      componentName: "",
    };
  },
  mounted() {
    this.componentName = this.tabs[0].name;
  },
  methods: {
    clickMenu(item, index) {
      this.menuIndex = index;
      this.componentName = item.name;
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  width: 100%;
  .detail-menu {
    @include flexbox();
    overflow-x: auto;
    overflow-y: hidden;
    padding: 12px 24px;
    box-sizing: border-box;
    &::-webkit-scrollbar {
      display: none;
    }
    .detail-menu-item {
      white-space: nowrap;
      min-width: 96px;
      height: 34px;
      font-size: 16px;
      color: #74767a;
      text-align: center;
      line-height: 32px;
      font-weight: 500;
      background: #eff3fe;
      border-radius: 4px;
      margin-right: 16px;
      &.active {
        background: #eff3fe;
        color: #036eff;
      }

      .detail-menu-item {
        white-space: nowrap;
        min-width: 96px;
        height: 34px;
        font-size: 16px;
        color: #74767a;
        text-align: center;
        line-height: 32px;
        font-weight: 500;
        background: #f6f6f6;
        border-radius: 4px;
        margin-right: 16px;

        &.active {
          background: #eff3fe;
          color: #036eff;
        }

        &:last-child {
          margin-right: 0;
        }
      }
    }
  }
}
</style>
