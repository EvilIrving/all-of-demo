<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 数据珊溪 C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="数字珊溪" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>班次统计/行动次数</p>
        <p>
          <span class="num">
            {{ count.groupTotal || "-" }} /
            {{ count.dispatchNum || "-" }}
          </span>
        </p>
      </div>
      <div class="has-count-item">
        <p>出动人次</p>
        <p>
          <span class="num">
            {{ count.patrolPeoNum || "-" }}
          </span>
        </p>
      </div>
      <div class="has-count-item">
        <p>巡查里程</p>
        <p>
          <span class="num">
            {{ count.mileage || "-" }}
          </span>
          <span class="unit">km</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>巡查时长</p>
        <p>
          <span class="num">
            {{ count.totalTime || "-" }}
          </span>
          <span class="unit">h</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

export default {
  name: "ShanXiCount",
  data() {
    return {
      count: {},
    };
  },
  components: { CountWrap },
  computed: {},
  created() {},
  mounted() {
    this.processStatisticss();
  },
  methods: {
    async processStatisticss() {
      await waterResourceApi.processStatisticss().then((res) => {
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
        color: #1ef8f2;
      }
    }
  }
}
</style>
