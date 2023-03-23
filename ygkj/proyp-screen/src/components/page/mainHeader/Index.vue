<!--
 * @Author: wqn
 * @Date: 2022-11-22 14:53:23
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-20 14:19:48
 * @Description: 大屏头部区域
-->
<template>
  <div class="main-header">
    <div class="title vertical-horizontal-center__unknown-size">
      <slot></slot>
    </div>
    <div class="back-btn" @click="back">
      <i class="icon-content-cion-huitui"></i>
      浙水安全
    </div>
  </div>
</template>

<script>
export default {
  name: "MainHeader",
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
  methods: {
    /**
     * @description 返回浙水安全
     */
    back() {
      // todo
    },
  },
};
</script>
<style lang='scss' scoped>
.main-header {
  position: relative;
  width: 100%;
  height: 107px;
  background-image: url("./img/header.png");
  background-size: 100% 100%;
  .title {
    font-family: fzzyjw;
    font-size: 50px;
    letter-spacing: 5.4px;
    top: 43%;
    user-select:none
  }
  .back-btn {
    position: absolute;
    left: 30px;
    top: 10px;
    width: 100px;
    height: 30px;
    line-height: 30px;
    text-align: center;
    font-family: fzzyjw;
    font-size: 16px;
    background-color: #0256ff;
    border-radius: 4px;
    cursor: pointer;
    i {
      font-size: 14px;
    }
    &:hover {
      background-color: #1e69ff;
    }
  }
}
</style>