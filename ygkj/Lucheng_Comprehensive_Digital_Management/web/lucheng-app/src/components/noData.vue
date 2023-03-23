<template>
  <div class="no-data" :class="{ 'old-wrap-data': addRouteType === 'older' }">
    <img src="../assets/nodata.png" alt="" />
    <p>暂无数据</p>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  computed: {
    ...mapGetters(["addRouteType"]),
  },
};
</script>

<style lang="scss" scoped>
.no-data {
  padding-top: 20px;
  text-align: center;
  img {
    width: 117px;
  }
  p {
    color: #999999;
    font-size: 12px;
    padding-top: 28px;
  }
  &.old-wrap-data {
    p {
      font-size: 18px;
    }
  }
}
</style>
