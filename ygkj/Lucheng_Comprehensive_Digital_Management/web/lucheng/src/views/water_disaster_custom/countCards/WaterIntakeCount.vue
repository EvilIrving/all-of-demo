<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2022-05-10 15:45:46
 * @LastEditors: dtb
 * @Description: 取水户 C位
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\WaterIntakeCount.vue
-->
<template>
  <count-wrap title="抗旱监管" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>当前无风险</p>
        <p>
          <span class="num" style="color: #47F5A7">
            0
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>蓝色风险</p>
        <p>
          <span class="num" style="color: #00BAFF">
            0
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>黄色风险</p>
        <p>
          <span class="num" style="color: #FCEE27;">
            0
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>橙色风险</p>
        <p>
          <span class="num" style="color: #FC8D27">
            0
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>红色风险</p>
        <p>
          <span class="num" style="color: #FF0000">
            0
          </span>
          <span class="unit">个</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from '../components/CountWrap.vue';
import { waterResourceApi } from '@/api/waterResourcesGuarantee';

export default {
  name: 'WaterIntakeCount',
  data() {
    return {
      count: {}
    };
  },
  components: { CountWrap },
  computed: {},
  created() {},
  mounted() {
    this.wiuUnit4CWindow();
  },
  methods: {
    async wiuUnit4CWindow() {
      await waterResourceApi.wiuUnit4CWindow().then((res) => {
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
