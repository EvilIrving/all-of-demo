<!--
 * @Autor: ysl
 * @Date: 2021-08-23 20:16:53
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-26 15:41:12
 * @Description: 出租车C位
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/taxiCount.vue
-->
<template>
  <count-wrap title="市内出租车" class="travel-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>报销次数</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ citytaxiCount.expenseNumber }}
          </span>
          <span class="unit">次</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>报销金额</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ citytaxiCount.expenseMoney }}
          </span>
          <span class="unit">元</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { mapGetters } from "vuex";

export default {
  name: "taxiCount",
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["citytaxiCount"]),
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
