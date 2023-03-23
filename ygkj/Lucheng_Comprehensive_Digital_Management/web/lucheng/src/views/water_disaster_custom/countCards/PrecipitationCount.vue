<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 降水量C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="降水量" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>全市距平值
          <span class="num" style="cursor: pointer">
            {{ PrecipitationCount.anomaly }} %
          </span>
        </p>
        <!-- <p>
          <span class="num" style="cursor: pointer">
            {{ PrecipitationCount.anomaly }} %
          </span>
          <span class="unit">%</span>
        </p> -->
      </div>
      <div class="has-count-item">
        <p>水资源划分
          <span class="num" style="cursor: pointer">
            {{ PrecipitationCount.watResState == 0 ? '平水年' : PrecipitationCount.watResState > 0 ? '丰水年' : '枯水年' }}
          </span>
        </p>
        <!-- <p>
          <span class="num" style="cursor: pointer">
            {{ PrecipitationCount.watResState == 0 ? '平水年' : PrecipitationCount.watResState > 0 ? '丰水年' : '枯水年' }}
          </span>
        </p> -->
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
