<template>
  <div :class="className">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: "steps",
  props: {
    active: {
      type: Number,
      default: () => 0,
    },
    direction: {
      type: String,
      default: () => "horizontal",
    },
    activeColor: {
      type: String,
      default: () => "primary",
    },
    inactiveColor: {
      type: String,
      default: () => "weak",
    },
    activeIcon: {
      type: String,
      default: () => "select_line",
    },
    inactiveIcon: {
      type: String,
      default: () => "",
    },
    finishIcon: {
      type: String,
      default: () => "",
    },
  },
  provide() {
    return {
      [this.$options.name]: this,
    };
  },
  computed: {
    className() {
      const base = `${this.$options.name}`;
      const classes = [base];
      classes.push(`${base}-${this.direction}`);
      return classes;
    },
  },
  methods: {
    clickStep(index) {
      this.$emit("click-step", index);
    },
  },
};
</script>

<style lang="scss" scoped>
.steps {
  display: flex;
  justify-content: center;
  align-content: center;
  overflow: hidden;
  &-horizontal {
    justify-content: center;
    align-content: center;
    flex-direction: row;
  }
  &-vertical {
    flex-direction: column;
  }
}
</style>