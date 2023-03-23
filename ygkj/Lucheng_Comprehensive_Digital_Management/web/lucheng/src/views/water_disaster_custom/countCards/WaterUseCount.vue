<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2022-03-16 14:24:00
 * @LastEditors: Please set LastEditors
 * @Description: 用水量  C位 
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\WaterUseCount.vue
-->
<template>
  <count-wrap title="洪水风险图" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <div>淹没面积</div>
        <div><span>446.12</span>km²</div>
      </div>
      <div class="has-count-item">
        <div>影响人口</div>
        <div><span class="red-span">74.51</span>万人</div>
      </div>
      <div class="has-count-item">
        <div>淹没区洪水损失</div>
        <div><span class="red-span">10087</span>万元</div>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from '../components/CountWrap.vue';
import { mapGetters } from 'vuex';

export default {
  name: 'WaterUseCount',
  data() {
    return {};
  },
  components: { CountWrap },
  computed: {
    ...mapGetters(['WaterUseCount'])
  },
  created() {},
  mounted() {},
  methods: {}
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
      text-align: center;
      flex: 1;
      > div:nth-child(1) {
        padding: 13px 0 10px;
        font-size: 16px;
        color: #fff;
      }
      > div:nth-child(2) {
        font-size: 20px;
        color: #fff;
        font-weight: 600;
        display: flex;
        align-items: center;
        justify-content: center;
        span{
          font-size: 32px;
          color: #FCEE27;
          padding-right: 5px;
          &.red-span{
            color: #FF0000;
          }
        }
      }
      &:first-child{
        >div:nth-child(2){
          display: block;
        }
      }
    }
  }
}
</style>
