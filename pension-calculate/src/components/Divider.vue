<template functional>
  <div v-bind="data.attrs" v-on="listeners" :class="[data.staticClass,'el-divider',`el-divider--${props.direction}`]">
    <div :class="['el-divider__text',`is-${props.contentPosition}`]">
      <slot />
    </div>
  </div>
</template>

<script>
export default {
  name: 'Divider',
  props: {
    direction: {
      type: String,
      default: 'horizontal',
      validator: (val) => {
        return ['horizontal', 'vertical'].indexOf(val) !== -1
      }
    },
    contentPosition: {
      type: String,
      default: 'center',
      validator: (val) => {
        return ['left', 'center', 'right'].indexOf(val) !== -1
      }
    }
  },
};
</script>

<style scoped lang="scss">
.el-divider {
  background-color: #dcdfe6;
  position: relative;
  &.el-divider--horizontal {
    display: block;
    height: 1px;
    width: 100%;
    margin: 24px 0;
  }
  &.el-divider--vertical {
    display: block;
    width: 1px;
    height: 1em;
    margin: 0 8px;
    vertical-align: middle;
    position: relative;
  }
  .el-divider__text {
    position: absolute;
    background-color: #fff;
    padding: 0 20px;
    font-weight: 500;
    color: #303133;

    font-size: 14px;
    &.is-left {
      left: 20%;
      transform: translateX(-50%);
    }
    &.is-center {
      left: 50%;
      transform: translateY(-50%) translateX(-50%);
    }
    &.is-right {
      right: 20px;
      transform: translateY(-50%);
    }
  }
}
</style>
