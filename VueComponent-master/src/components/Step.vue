<template>
  <div :class="classes">
    <div :class="circleContainerClass" @click.stop="parentClickStep">
      <div :class="lineBeforeClass" :style="bgStyle"></div>
      <template v-if="active">
        <slot name="active-icon">
          <div :class="circleClass" :style="bgStyle">
            <icon
              v-if="parentActiveIcon"
              :name="parentActiveIcon"
              color="white"
              :size="14"
            ></icon>
          </div>
        </slot>
      </template>
      <template v-else-if="finish">
        <slot name="finish-icon">
          <div :class="circleClass" :style="bgStyle">
            <icon
              v-if="parentFinishIcon"
              :name="parentFinishIcon"
              color="white"
              :size="14"
            ></icon>
          </div>
        </slot>
      </template>
      <template v-else>
        <slot name="inactive-icon">
          <div :class="circleClass" :style="bgStyle">
            <icon
              v-if="parentInactiveIcon"
              :name="parentInactiveIcon"
              color="white"
              :size="14"
            ></icon>
          </div>
        </slot>
      </template>
      <div :class="lineAfterClass" :style="bgStyle"></div>
    </div>
    <div :class="titleClass" :style="textStyle" @click.stop="parentClickStep">
      <slot></slot>
    </div>
  </div>
</template>

<script>
import Icon from "@/components/Icon";
const STEP_STATUS = {
  FINISH: "finish",
  PROCESS: "process",
};
export default {
  name: "step",
  inject: {
    parent: {
      from: "steps",
      default: null,
    },
  },
  components: {
    Icon,
  },
  computed: {
    // 注入属性
    parentActive() {
      return (this.parent && this.parent.active) || 0;
    },
    parentDirection() {
      return (this.parent && this.parent.direction) || "horizontal";
    },
    parentActiveColor() {
      return (this.parent && this.parent.activeColor) || "primary";
    },
    parentInactiveColor() {
      return (this.parent && this.parent.inactiveColor) || "weak";
    },
    parentFinishIcon() {
      return (this.parent && this.parent.finishIcon) || "";
    },
    parentActiveIcon() {
      return (this.parent && this.parent.activeIcon) || "select_line";
    },
    parentInactiveIcon() {
      return (this.parent && this.parent.inactiveIcon) || "";
    },
    // 样式
    name() {
      return `${this.$options.name}`;
    },
    classes() {
      const base = this.name;
      const classes = [base];
      classes.push(`${base}-${this.parent.direction}`);
      return classes;
    },
    titleClass() {
      const base = `${this.name}-title`;
      const classes = [base];
      classes.push(`${base}-${this.parentDirection}`);
      return classes;
    },
    circleContainerClass() {
      return `${this.name}-circle-container`;
    },
    circleClass() {
      return `${this.name}-circle`;
    },
    lineBeforeClass() {
      const base = `${this.name}-line`;
      const classes = [base];
      classes.push(`${base}-before`);
      return classes;
    },
    lineAfterClass() {
      const base = `${this.name}-line`;
      const classes = [base];
      classes.push(`${base}-after`);
      return classes;
    },
    bgStyle() {
      const styles = {};
      if (this.active || this.finish) {
        styles.backgroundColor = this.$handleColor(this.parentActiveColor);
      } else {
        styles.backgroundColor = this.$handleColor(this.parentInactiveColor);
      }
      return styles;
    },
    textStyle() {
      const styles = {};
      if (this.active) styles.color = this.$handleColor(this.parentActiveColor);
      if (this.finish) styles.color = "var(--title)";
      return styles;
    },
    // 索引
    index() {
      if (!this.parent) return -1;
      return this.parent.$slots["default"].indexOf(this.$vnode);
    },
    // 当前状态
    status() {
      let status = "";
      if (this.index === this.parentActive) status = STEP_STATUS.PROCESS;
      if (this.index < this.parentActive) status = STEP_STATUS.FINISH;
      return status;
    },
    // 是否激活
    active() {
      return this.status === STEP_STATUS.PROCESS;
    },
    // 是否完成
    finish() {
      return this.status === STEP_STATUS.FINISH;
    },
    // 未激活
    inActive() {
      return this.status === "";
    },
  },
  methods: {
    parentClickStep() {
      return this.parent && this.parent.clickStep(this.index);
    },
  },
};
</script>

<style lang="scss" scoped>
$step: "step";

.step {
  flex: 1;
  color: $c-weak;
  &-circle-container {
    @extend %f-xy;
    align-self: stretch;
    .#{step}-circle {
      @extend %f-xy;
      min-width: 16px;
      min-height: 16px;
      background-color: $c-weak;
      border-radius: 50%;
    }
    .#{step}-line {
      flex: 1;
    }
  }
  &-horizontal {
    @extend %f-xy;
    flex-direction: column;
    .#{step}-circle-container {
      flex-direction: row;
      .#{step}-line {
        height: 1px;
      }
    }
  }
  &-vertical {
    @extend %f-y;
    flex-direction: row;
    .#{step}-circle-container {
      flex-direction: column;
      .#{step}-line {
        width: 1px;
      }
    }
  }
  &-title-horizontal {
    margin-top: 12px;
    padding: 0 24px;
  }
  &-title-vertical {
    margin-left: 12px;
    padding: 24px 0;
  }
  &:first-child .#{step}-line-before {
    visibility: hidden;
  }
  &:last-child .#{step}-line-after {
    visibility: hidden;
  }
}
</style>