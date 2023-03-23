<!--
 * @Autor: ysl
 * @Date: 2021-08-13 09:02:17
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-18 15:59:03
 * @Description: 合同管理C位
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/contractCount.vue
-->
<template>
  <count-wrap title="合同管理" class="contract-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>合同总数</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ governmentContractCount.num || 0 }}
          </span>
          <span class="unit">份</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>合同总额</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ governmentContractCount.contractAmount || 0 }}
          </span>
          <span class="unit">万元</span>
        </p>
      </div>
			<div class="has-count-item">
        <p>合同报销金额</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ governmentContractCount.expenseAmount || 0 }}
          </span>
          <span class="unit">万元</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>合同剩余金额</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ governmentContractCount.remainExpenseAmount || 0 }}
          </span>
          <span class="unit">万元</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { mapGetters } from "vuex";

export default {
  name: "travelCount",
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["governmentContractCount"]),
  },
  components: { CountWrap },
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.contract-count {
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
