<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 水电站 C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="水电站" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>水电站总数</p>
        <p>
          <span class="num">
            {{ count.totalHyst || "-" }}
          </span>
          <span class="unit">座</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>最多数量</p>
        <p>
          <span class="num">
            {{ count.maxAreaName || "-" }} | {{ count.maxNum }}座 ({{
              count.totalHyst
                ? ((count.maxNum / count.totalHyst) * 100).toFixed(2)
                : "-"
            }}%)
          </span>
        </p>
      </div>
      <div class="has-count-item">
        <p>生态流量达标率</p>
        <p>
          <span class="num">
            {{ count.standardArg ? (count.standardArg * 100).toFixed(2) : "-" }}
          </span>
          <span class="unit">%</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>设备在线率</p>
        <p>
          <span class="num">
            {{ count.onlineArg ? (count.onlineArg * 100).toFixed(2) : "-" }}
          </span>
          <span class="unit">%</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

export default {
  name: "HydropowerStationCount",
  data() {
    return {
      count: {},
    };
  },
  components: { CountWrap },
  computed: {},
  created() {},
  mounted() {
    this.mainTotalNum();
  },
  methods: {
    async mainTotalNum() {
      await waterResourceApi.mainTotalNum().then((res) => {
        console.log(111, this.count);
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
        font-size: 25px;
        color: #1ef8f2;
      }
    }
  }
}
</style>
