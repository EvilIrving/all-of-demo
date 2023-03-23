<!--
 * @Author: wqn
 * @Date: 2023-02-15 14:41:24
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-15 14:41:24
 * @Description: 管控清单弹窗内的一键导出按钮
-->
<template>
  <div class='list-dialog-export-button' @click="click">
    <div class="button-inner">
      <slot>一键导出</slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "ListDialogExportButton",
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
    click() {
      this.$emit("click");
    },
  },
};
</script>
<style lang='scss' scoped>
.list-dialog-export-button {
  position: relative;
  width: 83px;
  height: 32px;
  border: 2px solid #013a62;
  border-radius: 16px;
  cursor: pointer;
  .button-inner {
    position: absolute;
    inset: 3px;
    border-radius: 13px;
    background-image: linear-gradient(to right, #06295b, #3e668f);
    line-height: 24px;
    text-align: center;
    font-weight: bold;
    font-size: 14px;
    color: #b1dcff;
  }
  &:hover {
    border-color: rgba(140, 203, 255, 0.75);
  }
}
</style>