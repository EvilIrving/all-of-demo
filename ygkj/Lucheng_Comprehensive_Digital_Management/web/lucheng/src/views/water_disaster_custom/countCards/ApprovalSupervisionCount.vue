<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 批后监管 C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="批后监管" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>项目数量</p>
        <p>
          <span class="num">
            {{ count.projectNum }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>监管次数</p>
        <p>
          <span class="num" style="color: #e80000">
            {{ count.inspectionNum }}
          </span>
          <span class="unit">次</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>发现问题数</p>
        <p>
          <span class="num" style="color: #ff551f">
            {{ count.findProblem }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>移交执法大队数</p>
        <p>
          <span class="num" style="color: #e89100">
            {{ count.hiddenDangerNum }}
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
  name: "ApprovalSupervisionCount",
  data() {
    return {
      count: {},
    };
  },
  components: { CountWrap },
  computed: {},
  created() {},
  mounted() {
    this.homeOverView();
  },
  methods: {
    async homeOverView() {
      await waterResourceApi.homeOverView().then((res) => {
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
