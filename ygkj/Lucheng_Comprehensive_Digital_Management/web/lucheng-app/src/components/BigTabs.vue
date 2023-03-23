<!--
 * @Author: dyn
 * @Date: 2021-11-15 14:56:16
 * @LastEditTime: 2021-11-15 16:59:29
 * @LastEditors: Please set LastEditors
 * @Description: 大的tab切换
 * @FilePath: \cangnan-app\src\components\BigTabs.vue
-->
<template>
  <div class="big-tabs">
    <van-tabs
      v-model="activeTab"
      line-width="0"
      line-height="0"
      swipe-threshold="3"
      @click="handleTab"
    >
      <van-tab
        v-for="item in tabs"
        :key="item.value"
        :title="item.label"
        :name="item.value"
      ></van-tab>
    </van-tabs>
  </div>
</template>

<script>
export default {
  name: "BigTabs",
  props: {
    defaultActive: String | Number,
    tabs: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      activeTab: this.defaultActive,
    };
  },
  components: {},
  created() {},
  mounted() {},
  methods: {
    handleTab(val) {
      this.$emit("handleTab", val);
    },
  },
};
</script>
<style lang="scss" scoped>
.big-tabs {
  width: 100%;
  margin-top: 16px;
  ::v-deep .van-tab {
    min-width: 90px;
    height: 34px;
    background: #fbfbfb;
    border: 1px solid rgba(0, 0, 0, 0.16);
    border-radius: 4px;
    font-family: PingFangSC-Regular;
    font-size: 16px;
    color: #1e1e1f;
    text-align: center;
    font-weight: 400;
    margin-right: 24px;
    &:last-child {
      margin-right: 0;
    }
    &.van-tab--active {
      background: #036eff;
      border: 1px solid rgba(0, 0, 0, 0.16);
      color: #fff;
    }
  }
}
</style>