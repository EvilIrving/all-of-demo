<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 用水量（公报）C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="用水量(公报)" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>用水总量</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ WaterUseCount.totalConsume }}
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
  name: "WaterUseCount",
  data() {
    return {};
  },
  components: { CountWrap },
  computed: {
    ...mapGetters(["WaterUseCount"]),
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
      display: flex;
      align-items: baseline;
      & > p {
        font-size: 24px;
        text-align: center;
        margin-right: 10px;
      }
      .num {
        font-size: 32px;
        color: #1df8f3;
      }
    }
  }
}
</style>
