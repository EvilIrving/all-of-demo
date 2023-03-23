<template>
  <div class="sidebar-box">
    <div class="sidebar-list">
      <div
        class="dispatch-item"
        v-for="(item, index) in routeListInfo"
        :key="index"
      >
        <div class="item-title"><span>推荐路线</span></div>
        <p>
          <span>{{ item.duration }}</span>
          <span> | </span>
          <span>{{ item.distance }}公里</span>
          <span> | </span>
          <span> {{ item.traffic_lights }}个红绿灯 </span>
        </p>
        <p>
          <span>打车约{{ item.taxi_cost }}元</span>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "WarehouseDispatch",
  props: {},
  data() {
    return {};
  },
  computed: {
    routeListInfo() {
      return this.$store.state.warehouseDispatchList;
    },
  },
  mounted() {},
  methods: {},
};
</script>

<style lang='scss' scoped>
@import "@/style/bigScreenSidebar.scss";
.sidebar-box {
  .sidebar-list {
    .dispatch-item {
      background: rgba(63, 155, 252, 0.4);
      padding-bottom: 12px;
      .item-title {
        span {
          display: inline-block;
          padding: 8px 24px;
          background: rgba(63, 155, 252, 0.8);
        }
      }
      p {
        color: #fff;
        padding: 16px 24px 0 24px;
      }
    }
  }
}
</style>