<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2022-03-29 15:47:34
 * @LastEditors: Please set LastEditors
 * @Description: 水电站 C位
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\HydropowerStationCount.vue
-->
<template>
  <count-wrap title="防汛风险" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>水库风险</p>
        <p><span>{{count.skRisk}}</span>个</p>
      </div>
      <div class="has-count-item">
        <p>水闸风险</p>
        <p><span>{{count.szRisk}}</span>个</p>
      </div>
      <div class="has-count-item">
        <p>海塘风险</p>
        <p><span>{{count.htRisk}}</span>个</p>
      </div>
      <div class="has-count-item">
        <p>堤防风险</p>
        <p><span>{{count.dfRisk}}</span>个</p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from '../components/CountWrap.vue';
import { waterResourceApi } from '@/api/waterResourcesGuarantee';

export default {
  name: 'HydropowerStationCount',
  data() {
    return {
      count: {}
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
      await waterResourceApi.floodRiskCenter().then((res) => {
        this.count = res.data;
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.stagnant-water-count {
  .has-count {
    display: flex;
    justify-content: space-around;
    width: 100%;
    height: 100%;
    .has-count-item {
      padding-top: 13px;
      > p:first-child {
        font-size: 16px;
        text-align: center;
        padding-bottom: 10px;
      }
      > p:last-child {
        font-size: 20px;
        font-weight: 600;
        display: flex;
        align-items: flex-end;
        span {
          font-size: 32px;
          color: #FCEE27;
          padding-right: 5px;
          position: relative;
          top: 5px;
        }
      }
    }
  }
}
</style>
