<!--
 * @Autor: ysl
 * @Date: 2021-08-24 10:37:23
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-24 10:38:51
 * @Description: 政务内部职能管理
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/insideCount.vue
-->
<template>
  <count-wrap title="政务内部职能管理" class="travel-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>建议提案文件个数</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ governmentInsideCount.proposalNum }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>领导批示个数</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ governmentInsideCount.instruFileNum }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>重要文件个数</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ governmentInsideCount.importFileNum }}
          </span>
          <span class="unit">个</span>
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
    ...mapGetters(["governmentInsideCount"]),
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
