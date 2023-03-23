<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 清四乱 C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="清“四乱”" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>“四乱”问题总数</p>
        <p>
          <span class="num">
            {{ count.total }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>最多问题地区</p>
        <p>
          <span class="num">
            {{ count.maxRate || "-" }}
          </span>
          <!-- |
          <span class="num"> {{ 12 || "-" }}个({{ 5 || "-" }}%) </span> -->
        </p>
      </div>
      <div class="has-count-item">
        <p>最少问题地区</p>
        <p>
          <span class="num">
            {{ count.maxRate || "-" }}
          </span>
          <!-- |
          <span class="num">
            {{ count.qualityOverLoadNum || "-" }}个({{
              count.qualityOverLoadNum || "-"
            }}%)
          </span> -->
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

export default {
  name: "FourChaosCount",
  data() {
    return {
      count: {},
    };
  },
  components: { CountWrap },
  computed: {},
  created() {},
  mounted() {
    this.statisProblemC();
  },
  methods: {
    async statisProblemC() {
      await waterResourceApi.statisProblemC().then((res) => {
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
      p:nth-child(2) {
        font-size: 32px;
        margin-top: 10px;
      }
      .num {
        font-size: 26px;
        // font-weight: bold;
        color: #1ef8f2;
      }
      .unit {
        font-size: 20px;
      }
    }
  }
}
</style>
