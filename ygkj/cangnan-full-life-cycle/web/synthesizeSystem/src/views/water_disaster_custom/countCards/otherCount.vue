<!--
 * @Autor: ysl
 * @Date: 2021-08-23 20:16:53
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-26 16:57:34
 * @Description: 其他报销C位
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/otherCount.vue
-->
<template>
  <count-wrap title="其他报销" class="official-car-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>报销次数</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ otherReimbursementCount.expenseNumber || 0 }}
          </span>
          <span class="unit">次</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>报销金额</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ otherReimbursementCount.expenseMoney || 0 }}
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
  name: "otherCount",
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["otherReimbursementCount"]),
  },
  components: { CountWrap },
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.official-car-count {
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
