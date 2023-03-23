<template>
  <div class="subhead-wrap">
    <div class="subhead">{{ text }}</div>
    <slot></slot>
  </div>
</template>
<script>
export default {
  name: "Subhead",
  props: {
    text: String,
  },
  data() {
    return {};
  },
};
</script>
<style lang="scss">
.subhead-wrap {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 16px 0;
  font-size: 16px;
  .subhead {
    height: 16px;
    padding-left: 8px;
    border-left: 4px solid #1492ff;
    color: #fff;
  }
  .subhead-operate {
    > div {
      display: inline-block;
      padding: 8px 12px;
      background: rgba(0, 0, 0, 0.4);
      color: #fff;
      cursor: pointer;
      &:first-child {
        border-radius: 6px 0 0 6px;
      }
      &:last-child {
        border-radius: 0 6px 6px 0;
      }
      &.active {
        background-color: #1492ff;
      }
    }
  }
}
</style>