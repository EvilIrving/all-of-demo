<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 水资源承载能力C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="水资源承载能力" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>水量评价超载数量</p>
        <p>
          <span class="num">
            {{ count.consumeOverLoadNum }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>水质评价超载数量</p>
        <p>
          <span class="num">
            {{ count.qualityOverLoadNum }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>用水饱和年份</p>
        <p>
          <span class="num">
            {{ count.consumeSatYear || "-" }}
          </span>
          <span class="unit">年</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

export default {
  name: "WaterResCarryCount",
  data() {
    return {
      count: {},
    };
  },
  components: { CountWrap },
  computed: {},
  created() {},
  mounted() {
    this.statsCwindow4WraUnit();
  },
  methods: {
    async statsCwindow4WraUnit() {
      await waterResourceApi.statsCwindow4WraUnit().then((res) => {
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
        color: #eec80b;
      }
    }
  }
}
</style>
