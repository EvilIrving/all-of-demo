<template>
  <div class="label-form-item">
    <div v-if="label" class="label-form-item__Label">{{ label }}</div>
    <div class="label-form-item__content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "LabelFormItem",
  props: { label: { type: String, require: true } },
};
</script>

<style lang="scss">
.label-form-item {
  display: inline-flex;
  align-items: center;
  margin-right: 8px;
  line-height: 42px;
  .label-form-item__Label {
    display: inline-block;
    white-space: nowrap;
    min-width: 48px;
    margin-right: 4px;
  }
  .label-form-item__content {
    .el-input__inner {
      min-width: 120px;
    }
    .el-select {
      .el-input__inner {
        max-width: 150px;
      }
    }
  }
}
</style>
