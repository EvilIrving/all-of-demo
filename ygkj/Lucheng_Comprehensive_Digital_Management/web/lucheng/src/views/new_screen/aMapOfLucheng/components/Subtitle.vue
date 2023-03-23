<!--
 * @Author: wqn
 * @Date: 2022-09-13 14:28:40
 * @LastEditors: wqn
 * @LastEditTime: 2022-09-13 14:28:40
 * @Description: 子标题栏组件
-->
<template>
  <div class='popUpSubtitle'>
    <div class="subtitle">
      <img src="@/assets/new_images/a_map_subtitle_icon.png">
      <div>
        <slot></slot>
      </div>
    </div>
    <div class="split-line"></div>
  </div>
</template>

<script>
export default {
  name: 'subtitle',
  props: {},
  components: {},
  data() {
    return {};
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  destroyed() {},
  methods: {}
};
</script>
<style lang='scss' scoped>
.popUpSubtitle {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
  height: 26px;
  margin-bottom: 10px;
  .subtitle {
    display: flex;
    align-items: center;
    height: 20px;
    line-height: 20px;
    font-size: 18px;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #ffffff;
    > img {
      margin-right: 7px;
    }
    > div {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: calc(100% - 34px);
    }
  }
  .split-line {
    position: relative;
    width: calc(100% - 44px);
    height: 2px;
    transform: translateX(44px);
    background-image: linear-gradient(90deg, #047faf 50%, transparent 100%);
    &::before,
    &::after {
      content: '';
      position: absolute;
      width: 22px;
      height: 2px;
      top: 0;
      background-image: linear-gradient(90deg, #047faf 25%, transparent 75%);
    }
    &::before {
      left: -44px;
    }
    &::after {
      left: -22px;
    }
  }
}
</style>