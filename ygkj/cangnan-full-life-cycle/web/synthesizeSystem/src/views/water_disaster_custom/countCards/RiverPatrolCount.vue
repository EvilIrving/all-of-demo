<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 河长巡查 C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="河长巡查" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>当年问题总数</p>
        <p>
          <span class="num" style="color: #e89100">
            {{ count.totalNum || "-" }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>当年已处理总数</p>
        <p>
          <span class="num" style="color: #39fcf8">
            {{ count.handledNum || "-" }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>当年未处理总数</p>
        <p>
          <span class="num" style="color: #e89100">
            {{ count.unHandledNum || "-" }}
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
  name: "RiverPatrolCount",
  data() {
    return {
      count: {},
    };
  },
  components: { CountWrap },
  computed: {},
  created() {},
  mounted() {
    this.chiefPatrolProblemStats();
  },
  methods: {
    async chiefPatrolProblemStats() {
      await waterResourceApi
        .chiefPatrolProblemStats({ type: "5" })
        .then((res) => {
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
