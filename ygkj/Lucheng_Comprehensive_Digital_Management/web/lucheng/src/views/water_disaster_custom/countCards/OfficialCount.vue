<!--
 * @Autor: ysl
 * @Date: 2021-08-10 19:26:43
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-18 16:23:55
 * @Description: 公务接待
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/officialCount.vue
-->
<template>
  <count-wrap title="公务接待" class="travel-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>接待人次</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ governmentOfficialCount.visitNum || 0 }}
          </span>
          <span class="unit">人</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>陪同人次</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ governmentOfficialCount.escortsNumber || 0 }}
          </span>
          <span class="unit">人</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>报销金额</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ governmentOfficialCount.actualAmount || 0 }}
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
  name: "officialCount",
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["governmentOfficialCount"]),
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
