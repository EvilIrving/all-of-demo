<!--
 * @Author: wqn
 * @Date: 2023-02-15 10:03:41
 * @LastEditors: dtb
 * @LastEditTime: 2023-02-16 13:12:49
 * @Description: 弹窗（管控清单详情）
-->
<template>
  <div class='list-details-dialog wrapper' v-if="value">
    <div class="inner">
      <div class="head">
        <i></i><span>{{title}}</span>
      </div>
      <div class="body">
        <slot></slot>
      </div>
      <i class="el-icon-close" @click="close"></i>
    </div>
  </div>
</template>

<script>
export default {
  name: "ListDetailsDialog",
  props: {
    value: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: "详情",
    },
  },
  components: {},
  data() {
    return {};
  },
  computed: {},
  watch: {},
  created() { },
  mounted() { },
  destroyed() { },
  methods: {
    close() {
      this.$emit("input", false);
    },
  },
};
</script>
<style lang='scss' scoped>
.list-details-dialog.wrapper {
  position: fixed;
  inset: 0;
  z-index: 99;
  background-color: rgba(3, 16, 43, 0.35);
  .inner {
    position: absolute;
    width: 1592px;
    left: 0;
    right: 0;
    top: 89px;
    bottom: 71px;
    margin: auto;
    background-color: #03102b;
    .head {
      position: relative;
      display: flex;
      align-items: center;
      width: 100%;
      height: 47px;
      background-image: linear-gradient(to right, #06254d, transparent);
      > i {
        width: 2px;
        height: 14px;
        margin-left: 19px;
        background-color: #147fd8;
      }
      > span {
        font-size: 18px;
        font-weight: bold;
        margin-left: 10px;
      }
      &::before {
        content: "";
        position: absolute;
        display: inline-block;
        width: 100%;
        height: 2px;
        top: 0;
        background-image: linear-gradient(
          to left,
          #107fdb 50%,
          transparent 100%
        );
      }
    }
    .body {
      width: 100%;
      height: calc(100% - 47px);
    }
    .el-icon-close {
      position: absolute;
      top: 10px;
      right: 15px;
      font-size: 20px;
      cursor: pointer;
    }
  }
}
</style>