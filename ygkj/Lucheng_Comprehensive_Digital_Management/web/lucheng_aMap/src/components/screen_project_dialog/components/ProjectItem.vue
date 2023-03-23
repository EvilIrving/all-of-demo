<template>
  <div class="project-item">
    <div class="project-item-title">
      <i class="icon-judge-icon"></i>
      <span>
        {{ title }}
      </span>
    </div>
    <i v-if="!hideLine" class="split-line icon-split-line"></i>
    <div class="project-item-content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProjectItem",
  props: { title: String, hideLine: { type: Boolean, default: () => false } },
};
</script>

<style lang="scss" scoped>
.project-item {
  .project-item-title {
    font-size: 18px;
    display: flex;
    align-items: center;
    margin-bottom: 8px;
    span {
      color: #20d3ec;
      font-weight: bold;
      margin-left: 8px;
    }
  }
  .split-line {
    width: 100%;
    margin-bottom: 16px;
  }
}
</style>