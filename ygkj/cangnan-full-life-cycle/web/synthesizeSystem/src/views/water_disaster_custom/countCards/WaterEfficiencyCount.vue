<!--
 * @Autor: ysl
 * @Date: 2021-08-11 20:29:02
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-13 14:55:58
 * @Description: 用水效率C位
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/waterEfficiencyCount.vue
-->
<template>
  <count-wrap title="用水效率" class="travel-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>万元GDP用水量省内排名</p>
        <p>
          <span class="num" style="cursor: pointer">
            第{{ waterEfficiencyCount.wpgIndex || 1 }}名 {{ waterEfficiencyCount.data ? waterEfficiencyCount.data.wpg : 0}}
          </span>
          <span class="unit">m³/万元</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>万元工业增加值用水量省内排名</p>
        <p>
          <span class="num" style="cursor: pointer">
            第{{ waterEfficiencyCount.induWpgIndex || 1 }}名 {{ waterEfficiencyCount.data ? waterEfficiencyCount.data.induWpg : 0 }}
          </span>
          <span class="unit">m³/万元</span>
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
    ...mapGetters(["waterEfficiencyCount"]),
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
