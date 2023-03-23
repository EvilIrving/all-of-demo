<!--
 * @Author: wupengfei
 * @LastEditTime: 2020-09-10 16:34:01
 * @LastEditors: wupengfei
 * @Descripttion: 
 * @FilePath: \ruiAnWaterPlatform\src\components\primary_view\index.vue
-->
<template>
  <div class="primary-view" :class="[pageName ? pageName + '-page' : '']">
    <p v-if="title" class="title">
      {{ title }}
      <span class="right-area">
        <slot name="desc"></slot>
      </span>
    </p>
    <p class="tab-list" v-if="typeChange">
      <span
        @click="tabClick(item)"
        :class="selectTab == item ? 'active' : ''"
        v-for="(item, index) in tabList"
        :key="index"
        >{{ item }}</span
      >
    </p>
    <div class="view-content">
      <slot></slot>
    </div>
    <p class="technical-support">技术支持©浙江禹贡信息科技有限公司</p>
  </div>
</template>

<script>
export default {
  name: "PrimaryView",
  props: {
    title: String,
    pageName: String,
    typeChange: {
      type: Boolean,
      default: false,
    },
    tabList: Array,
  },
  data() {
    return {
      typeSelect: "pic",
      selectTab: "",
    };
  },
  components: {},
  mounted() {
    if (this.typeChange) {
      this.selectTab = this.tabList[0];
      this.$emit("tabChange", this.selectTab);
    }
  },
  methods: {
    changeModule(module) {
      this.typeSelect = module;
      this.$emit("changeModule", module);
    },
    tabClick(item) {
      this.selectTab = item;
      this.$emit("tabChange", this.selectTab);
    },
  },
};
</script>

<style lang='scss' scoped>
@import "@/style/theme.scss";
$primaryview-titleHeight: 24px;
.primary-view {
  padding: 16px 16px 16px 0;
  width: 100%;
  height: 100%;
  font-weight: bold;
  position: relative;
  .title {
    font-weight: bold;
    display: flex;
    justify-content: space-between;
    padding: 6px 22px 46px 0;
    font-size: 24px;
    color: #fff;
  }
  .tab-list {
    font-weight: bold;
    display: flex;
    padding: 6px 22px 46px 0;
    font-size: 24px;
    color: #fff;
    span {
      display: inline-block;
      margin-right: 24px;
      opacity: 0.64;
      cursor: pointer;
      &:hover,
      &.active {
        opacity: 1;
      }
    }
  }
  .type-change {
    @include flexbox;
    @include flexJC(flex-end);
    i {
      display: block;
      width: 14px;
      height: 14px;
      margin-right: 8px;
      cursor: pointer;
    }
  }
  .view-content {
    height: calc(100% - #{$primaryview-titleHeight} - 82px);
    opacity: 0.8;
    background: #f7f8f8;
    border-radius: 20px;
    border-radius: 20px;
    padding: 12px;
  }
  .technical-support {
    position: absolute;
    bottom: 16px;
    width: calc(100% - 32px);
    font-size: 14px;
    color: #ffffff;
    text-align: center;
  }
}
</style>
