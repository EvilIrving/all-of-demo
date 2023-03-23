<!--
 * @Autor: ysl
 * @Date: 2021-08-23 20:16:53
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-26 17:20:50
 * @Description: 会议培训C位
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/conferenceTrainingCount.vue
-->
<template>
  <count-wrap title="会议培训" class="conference-training-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>会议培训次数</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ conferenceTrainingCount.numberTrain || 0 }}
          </span>
          <span class="unit">次</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>报销次数</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ conferenceTrainingCount.expenseAccountNumber || 0 }}
          </span>
          <span class="unit">次</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>报销金额</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ conferenceTrainingCount.reimbursementAmount || 0 }}
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
  name: "conferenceTrainingCount",
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["conferenceTrainingCount"]),
  },
  components: { CountWrap },
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.conference-training-count {
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
