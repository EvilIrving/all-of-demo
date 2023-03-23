<!--
 * @Autor: ysl
 * @Date: 2021-08-23 20:16:53
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-24 09:01:49
 * @Description: 资产管理C位
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/assetsCount.vue
-->
<template>
  <count-wrap title="资产管理" class="travel-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>资产总数</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ governmentAssetsCount.totalNum }}
          </span>
          <span class="unit">件</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>资产总额</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ governmentAssetsCount.totalAmount }}
          </span>
          <span class="unit">万元</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>报废件数</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ governmentAssetsCount.scrapNum }}
          </span>
          <span class="unit">件</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>领用件数</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ governmentAssetsCount.collectNum }}
          </span>
          <span class="unit">件</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { mapGetters } from "vuex";

export default {
  name: "assetsCount",
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["governmentAssetsCount"]),
  },
  components: { CountWrap },
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.travel-count {
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
      .numPeople {
        font-size: 32px;
        color: #1df8f3;
      }
      .num {
        font-size: 32px;
        color: #eec80b;
      }
    }
  }
}
</style>
