<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 水源供水C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="水源供水" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>可供水量</p>
        <p>
          <span class="num">
            {{ count.total || "-"  }}
          </span>
          <span class="unit">万m³</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>红色预警</p>
        <p>
          <span class="num" style="color: #ef1313">
            {{ count.redNum || "-"  }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>橙色预警</p>
        <p>
          <span class="num" style="color: #ff551f">
            {{ count.orangeNum || "-" }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>黄色预警</p>
        <p>
          <span class="num" style="color: #e89100">
            {{ count.yellowNum || "-" }}
          </span>
          <span class="unit">个</span>
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
      await waterResourceApi.statisC().then((res) => {
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
