<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 流域生态流量 C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="流域生态流量" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>红色预警数量</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ EmergencyCount && EmergencyCount.redWarnNum }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>橙色预警数量</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ EmergencyCount && EmergencyCount.orangeWarnNum }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { mapGetters } from "vuex";

export default {
  name: "EmergencySupportCount",
  data() {
    return {};
  },
  components: { CountWrap },
  computed: {
    ...mapGetters(["EmergencyCount"]),
  },
  created() {},
  mounted() {
    console.log(this.EmergencyCount,333);
  },
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
