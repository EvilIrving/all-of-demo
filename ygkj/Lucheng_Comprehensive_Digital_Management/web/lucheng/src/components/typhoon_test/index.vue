<template>
  <div class="typhoon-test">
    <primary-map-typhoon key="typhoonMap"></primary-map-typhoon>
  </div>
</template>

<script>
import { PrimaryMapTyphoon } from "@/components";
export default {
  name: "TyphoonTest",
  data() {
    return {};
  },
  components: {
    PrimaryMapTyphoon,
  },
  methods: {},
};
</script>

<style lang='scss' scoped>
.typhoon-test {
  width: 100%;
  height: 100%;
}
</style>