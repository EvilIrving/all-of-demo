<template>
  <div class="el-card" :class="shadow?'is-'+shadow+'-shadow':'is-always-shadow'">
    <div class="el-card__header" v-if="$slots.header||header">
      <slot name='header'>{{header}}</slot>
    </div>
    <div class="el-card__body" :style="bodyStyle">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Card',
  props: {
    header: {},
    bodyStyle: {},
    shadow: {
      type: String,
      default:""
    }
  },
};
</script>

<style scoped lang="scss">
.el-card {
  border-radius: 4px;
  border: 1px solid #ebeef5;
  background-color: #fff;
  overflow: hidden;
  color: #303133;
  transition: 0.3s;
  &.is-always-shadow,
  &.is-hover-shadow:focus,
  &.is-hover-shadow:hover {
    box-shadow: 0 2px 12px 0 rgba($color: #000000, $alpha: 0.1);
  }
  .el-card__header {
    padding: 18px;
    border-bottom: 1px solid #ebeef5;
    box-sizing: border-box;
  }
  .el-card__body {
    padding: 20px;
  }
}
</style>
