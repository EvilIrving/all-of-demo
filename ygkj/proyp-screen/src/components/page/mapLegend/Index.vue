<!--
 * @Author: wqn
 * @Date: 2023-02-16 16:48:48
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-16 16:48:48
 * @Description: 地图图例容器
-->
<template>
  <div class='map-legend' v-show="!reservoirStorage">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: "MapLegend",
  props: {},
  components: {},
  data() {
    return {};
  },
  computed: {
    reservoirStorage () {
      return this.$store.state.reservoir.reservoirStorage
    }
  },
  watch: {},
  created() {},
  mounted() {},
  destroyed() {},
  methods: {},
};
</script>
<style lang='scss' scoped>
.map-legend {
  position: fixed;
  left: 500px;
  bottom: 40px;
  padding: 15px 15px;
  background-image: linear-gradient(
      0deg,
      rgba(0, 26, 85, 0.3) 0%,
      rgba(8, 77, 152, 0.3) 0%,
      rgba(16, 127, 219, 0.3) 0%,
      rgba(17, 89, 171, 0.3) 0%,
      rgba(18, 50, 122, 0.3) 0%,
      rgba(16, 127, 219, 0.3) 100%
    ),
    linear-gradient(#00184d, #00184d);
  background-blend-mode: normal, normal;
  border-style: solid;
  border-width: 1px;
  border-image-source: linear-gradient(
    218deg,
    #2593ee 0%,
    #2593ee 50%,
    #2593ee 100%
  );
  border-image-slice: 1;
}
</style>