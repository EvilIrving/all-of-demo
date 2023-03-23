<template>
  <div
    class="wrap-title"
    :class="{ 'old-wrap-title': addRouteType === 'older' }"
  >
    <span class="icon-wrap-title"></span>
    <span class="txt-wrap-title">{{ title }}</span>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  props: {
    title: String,
  },
  computed: {
    ...mapGetters(["addRouteType"]),
  },
};
</script>

<style lang="scss" scoped>
.wrap-title {
  padding: 20px 0 10px 8px;
  display: flex;
  align-items: center;
  background: #ffffff;
  .icon-wrap-title {
    width: 6px;
    height: 16px;
    background: url(~assets/decorate.png) no-repeat;
    background-size: 100% 100%;
  }
  .txt-wrap-title {
    font-weight: 500;
    color: #1f1f1f;
    font-size: 18px;
    padding-left: 5px;
  }
  &.old-wrap-title {
    padding: 9px 0 9px 8px;
    .txt-wrap-title {
      font-size: 24px;
      padding-left: 10px;
    }
  }
}
</style>