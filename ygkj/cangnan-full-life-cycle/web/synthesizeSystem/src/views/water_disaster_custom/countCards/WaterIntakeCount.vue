<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 取水户 C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="取水户" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>取水总量</p>
        <p>
          <span class="num">
            {{ count.intakeSum }}
          </span>
          <span class="unit">亿m³</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>接近许可数量</p>
        <p>
          <span class="num">
            {{ count.intakeNearPermit }}
          </span>
          <span class="unit">户</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>超过许可数量</p>
        <p>
          <span class="num" style="color: #e89100">
            {{ count.intakeOverPermit }}
          </span>
          <span class="unit">户</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

export default {
  name: "WaterIntakeCount",
  data() {
    return {
      count: {},
    };
  },
  components: { CountWrap },
  computed: {},
  created() {},
  mounted() {
    this.wiuUnit4CWindow();
  },
  methods: {
    async wiuUnit4CWindow() {
      await waterResourceApi.wiuUnit4CWindow().then((res) => {
        this.count = res.data;
      });
    },
  },
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
        font-weight: bold;
        color: #1ef8f2;
      }
    }
  }
}
</style>
