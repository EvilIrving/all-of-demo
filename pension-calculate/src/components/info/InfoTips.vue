<template>
  <div class="el-badge">
    <slot></slot>
    <transition name="el-zoom-in-center">
      <sup v-show="!hidden && (content || content === 0 || isDot)" v-text="content" class="el-badge__content" :class="[
      type? 'el-badge__content--' + type :null,
      {
        'is-fixed':$slots.default,
        'is-dot':isDot
      }
      ]"></sup>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'InfoTips',
  props: {
    value: [String, Number],
    max: Number,
    isDot: Boolean,
    hidden: Boolean,
    type: {
      type: String,
      validator: (val) => {
        return ['primary', 'success', 'warning', 'info', 'danger'].includes(val)
      }
    }
  },
  computed: {
    content() {
      if (this.isDot) return

      const value = this.value, max = this.max

      if (typeof value === 'number' && typeof max === 'number') {
        return max < value ? `${max}+` : value
      }

      return value
    }
  },
};
</script>

<style scoped lang="scss">
$colors: #f56c6c, #67c23a, #e6a23c, #909399, #409eff;
$types: "danger", "success", "warning", "info", "primary";
.el-badge {
  position: relative;
  vertical-align: middle;
  display: inline-block;
}
.el-badge__content {
  background-color: #f56c6c;
  border-radius: 10px;
  color: #fff;
  display: inline-block;
  font-size: 12px;
  height: 18px;
  line-height: 18px;
  padding: 0 6px;
  text-align: center;
  white-space: normal;
  border: 1px solid #fff;
  &.is-dot {
    height: 8px;
    width: 8px;
    padding: 0;
    right: 0;
    border-radius: 50%;
  }
  &.is-fixed {
    position: absolute;
    top: 0;
    right: 10px;
    transform: translateY(-50%) translateX(100%);
    &.is-dot {
      right: 5px;
    }
  }

  @each $color in $colors {
    $i: index(
      $list: $colors,
      $value: $color,
    );
    $type: nth(
      $list: $types,
      $n: $i,
    );
    &--#{$type} {
      background-color: $color;
    }
  }
}
.el-zoom-in-center-enter-active,
.el-zoom-in-center-leave-active {
  transition: all 0.3s cubic-bezier(0.55, 0, 0.1, 1);
}
.el-zoom-in-center-enter,
.el-zoom-in-center-leave-active {
  opacity: 0;
  transform: scaleX(0);
}
</style>
