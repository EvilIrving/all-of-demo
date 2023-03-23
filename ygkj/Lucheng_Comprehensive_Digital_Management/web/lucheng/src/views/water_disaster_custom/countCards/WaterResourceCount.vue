<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 水资源量C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="水资源量" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>水资源总量</p>
        <p style=" display: flex; justify-content: space-around; align-items: center;">
          <span class="num" v-if="WaterResourceCount.wzCityData != null">
            {{ WaterResourceCount.wzCityData.totalWaterResource }}
            <span class="unit">亿m³</span>
          </span>
          <span class="cut-off-rule" />
          <span>
            <p class="water-resource-sty" v-if="WaterResourceCount.maxTwrCountyData != null">
              <span>最大</span>
              <span>{{ WaterResourceCount.maxTwrCountyData.adnm }}</span>
              <span>{{ WaterResourceCount.maxTwrCountyData.totalWaterResource }}</span>
              <span>亿m³</span>
            </p>
            <p class="water-resource-sty" v-if="WaterResourceCount.minTwrCountyData != null">
              <span style="background: #0639ff;margin-left:-0.325rem;">最小</span>
              <span>{{ WaterResourceCount.minTwrCountyData.adnm }}</span>
              <span>{{ WaterResourceCount.minTwrCountyData.totalWaterResource }}</span>
              <span>亿m³</span>
            </p>
          </span>
        </p>
      </div>
      <div class="has-count-item">
        <p>水资源利用率</p>
        <p style=" display: flex; justify-content: space-around; align-items: center;">
          <span class="num" v-if="WaterResourceCount.wzCityData != null">
            {{ WaterResourceCount.wzCityData.avgWrUseRate }}
            <span class="unit">%</span>
          </span>
          <span class="cut-off-rule" />
          <span>
            <p class="water-resource-sty" v-if="WaterResourceCount.maxWrurCountyData != null">
              <span>最大</span>
              <span>{{ WaterResourceCount.maxWrurCountyData.adnm }}</span>
              <span>{{ WaterResourceCount.maxWrurCountyData.avgWrUseRate }}</span>
              <span>%</span>
            </p>
            <p class="water-resource-sty" v-if="WaterResourceCount.minWrurCountyData != null">
              <span style="background: #0639ff;margin-left:-0.325rem;">最小</span>
              <span>{{ WaterResourceCount.minWrurCountyData.adnm }}</span>
              <span>{{ WaterResourceCount.minWrurCountyData.avgWrUseRate }}</span>
              <span>%</span>
            </p>
          </span>
        </p>
      </div>
      <div class="has-count-item">
        <p>人均水资源量</p>
        <p style=" display: flex; justify-content: space-around; align-items: center;">
          <span class="num" v-if="WaterResourceCount.wzCityData != null">
            {{ WaterResourceCount.wzCityData.watResPerCapita }}
            <span class="unit">m³</span>
          </span>
          <span class="cut-off-rule" />
          <span>
            <p class="water-resource-sty" v-if="WaterResourceCount.maxWrpcCountyData != null">
              <span>最大</span>
              <span>{{ WaterResourceCount.maxWrpcCountyData.adnm}}</span>
              <span>{{ WaterResourceCount.maxWrpcCountyData.watResPerCapita}}</span>
              <span>m³</span>
            </p>
            <p class="water-resource-sty" v-if="WaterResourceCount.minWrpcCountyData != null">
              <span style="background: #0639ff;margin-left:-0.325rem;">最小</span>
              <span>{{ WaterResourceCount.minWrpcCountyData.adnm }}</span>
              <span>{{ WaterResourceCount.minWrpcCountyData.watResPerCapita }}</span>
              <span>m³</span>
            </p>
          </span>
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
      .unit {
        color: #fff;
        font-size: 18px;
      }
      .cut-off-rule {
        height: 2.2rem;
        margin-top: 10px;
        margin: 0 0.4rem;
        border-right: 2px solid #fff;
      }
      .water-resource-sty {
        line-height: 13px;
        >span:nth-child(1) {
          background: #e89100;
          padding: 0 2px;
          font-size: 14px;
          margin: 0 3px;
        }
        >span:nth-child(2) {
          padding: 2px 2px;
          font-size: 14px;
        }
        >span:nth-child(3) {
          padding: 2px 2px;
          font-size: 14px;
          color: #1df8f3;
        }
        >span:nth-child(4) {
          font-size: 14px;
          color: #fff;
        }
      }
    }
  }
}
</style>
