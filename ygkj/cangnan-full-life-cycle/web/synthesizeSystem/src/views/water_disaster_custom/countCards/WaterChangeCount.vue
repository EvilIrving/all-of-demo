<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 水域变化 C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="水域变化" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>水域面积</p>
        <p>
          <span class="num">
            {{ count.waterArea }}
          </span>
          <span class="unit">k㎡</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>占用面积</p>
        <p>
          <span class="num">
            {{ count.occupiedArea }}
          </span>
          <span class="unit">k㎡</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>补偿面积</p>
        <p>
          <span class="num">
            {{ count.compensationArea }}
          </span>
          <span class="unit">k㎡</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

export default {
  name: "WaterChangeCount",
  data() {
    return {
      count: {},
    };
  },
  components: { CountWrap },
  computed: {},
  created() {},
  mounted() {
    this.cPositionStatisticsWaterArea();
  },
  methods: {
    async cPositionStatisticsWaterArea() {
      await waterResourceApi.cPositionStatisticsWaterArea().then((res) => {
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
