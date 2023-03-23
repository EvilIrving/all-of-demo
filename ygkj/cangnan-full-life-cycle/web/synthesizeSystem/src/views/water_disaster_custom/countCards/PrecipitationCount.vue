<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 降水量（公报）C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="降水量(公报)" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>平均降水量</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ PrecipitationCount.averageRainfall }}
          </span>
          <span class="unit">mm</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>折合水量</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ PrecipitationCount.totalReducedWater }}
          </span>
          <span class="unit">亿m³</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { mapGetters } from "vuex";

export default {
  name: "PrecipitationCount",
  data() {
    return {};
  },
  components: { CountWrap },
  computed: {
    ...mapGetters(["PrecipitationCount"]),
  },
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.stagnant-water-count {
  .has-count {
    display: flex;
    justify-content: space-around;
    align-items: center;
    width: 100%;
    height: 100%;
    .has-count-item {
      & > p {
        font-size: 20px;
        text-align: center;
        margin-bottom: 3px;
      }
      .num {
        font-size: 32px;
        color: #1df8f3;
      }
    }
  }
}
</style>
