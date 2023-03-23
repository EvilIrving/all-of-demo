<template>
  <div class="map-view-point">
    <project-map-single class="single_map" ref="projectMapSinglePoint">
    </project-map-single>
  </div>
</template>

<script>
import ProjectMapSingle from "./ProjectMapSingle.vue";
export default {
  components: { ProjectMapSingle },
  props: {
    point: {
      type: Array,
      default: () => {
        return [{ point: [] }];
      },
    },
  },
  watch: {
    point: {
      handler: function (val) {
        if (val) {
          this.setPoint();
        }
      },
      deep: true,
    },
  },
  mounted() {
    if (this.point.length > 0) {
      this.setPoint();
    }
  },
  methods: {
    setPoint() {
      this.$nextTick(() => {
        this.$refs.projectMapSinglePoint._drawPoint(this.point);
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.map-view-point {
  width: 100%;
  height: 150px;
}
</style>