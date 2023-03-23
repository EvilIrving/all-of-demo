<!--
 * @Autor: ysl
 * @Date: 2021-08-23 20:16:53
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-26 16:35:32
 * @Description: 公务用车C位
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/officialCarCount.vue
-->
<template>
  <count-wrap title="公务用车" class="official-car-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>用车次数</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ officialVehicleManagementCount.carNumber || 0 }}
          </span>
          <span class="unit">次</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>报销次数</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ officialVehicleManagementCount.expenseNumber || 0 }}
          </span>
          <span class="unit">次</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>报销金额</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ officialVehicleManagementCount.expenseMoney || 0 }}
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
  name: "officialCarCount",
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["officialVehicleManagementCount"]),
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
