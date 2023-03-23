<!--
 * @Autor: ysl
 * @Date: 2021-08-11 16:23:03
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-13 09:40:01
 * @Description: 水源保障C位
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/waterSecurityCount.vue
-->
<template>
  <count-wrap title="水源保障" class="travel-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>综合供水能力</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ waterSecurityCount.compositeSupplyAbility || 0 }}
          </span>
          <span class="unit">天</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>最小供水能力</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ waterSecurityCount.min && waterSecurityCount.min.name ? waterSecurityCount.min.name : "-" }}
          </span>
          <span class="num" style="cursor: pointer">
            {{ waterSecurityCount.min && waterSecurityCount.min.supplyAbility ? waterSecurityCount.min.supplyAbility : 0 }}
          </span>
          <span class="unit">天</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { mapGetters } from "vuex";

export default {
  name: "waterSecurityCount",
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["waterSecurityCount"]),
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
      .num {
        font-size: 32px;
        color: #1df8f3;
      }
    //   .num {
    //     font-size: 32px;
    //     color: #eec80b;
    //   }
    }
  }
}
</style>
