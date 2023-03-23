<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 水资源量（公报）C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="水资源量(公报)" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>水资源总量</p>
        <p>
          <span class="num">
            {{ WaterResourceCount.totalWaterResource }}
          </span>
          <span class="unit">亿m³</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>水资源利用率</p>
        <p>
          <span class="num">
            {{ WaterResourceCount.avgWrUseRate }}
          </span>
          <span class="unit">%</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>人均水资源量</p>
        <p>
          <span class="num">
            {{ WaterResourceCount.avgWrUse }}
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
  name: "WaterResourceCount",
  data() {
    return {};
  },
  components: { CountWrap },
  computed: {
    ...mapGetters(["WaterResourceCount"]),
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
