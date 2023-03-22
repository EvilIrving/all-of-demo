<template>
  <svg class="icon" aria-hidden="true" :style="styles">
    <use :xlink:href="iconName"></use>
  </svg>
</template>
<script>
export default {
  name: "icon",
  props: {
    name: {
      type: String,
      default: () => "",
    },
    size: {
      type: Number,
      default: () => 14,
    },
    color: {
      type: String,
      default: () => "title",
    },
  },
  computed: {
    iconName() {
      return `#icon-${this.name}`;
    },
    styles() {
      const styles = {};
      styles.width = `${this.size}px`;
      styles.height = `${this.size}px`;
      styles.color = this.$handleColor(this.color);
      return styles;
    },
  }
};
</script>
<style lang="scss" scoped>
.icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}
</style>