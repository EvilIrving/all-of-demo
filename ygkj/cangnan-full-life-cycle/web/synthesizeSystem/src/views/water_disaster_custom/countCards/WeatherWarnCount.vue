<!--
 * @Author: your name
 * @Date: 2021-07-01 16:29:21
 * @LastEditTime: 2021-09-07 17:26:34
 * @LastEditors: ysl
 * @Description: 气象预警统计卡片
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/WeatherWarnCount.vue
-->
<template>
  <count-wrap title="气象预警" class="weather-warn-count">
    <div class="has-count">
      <!-- 预警数量
      <span class="fs36 ml38 mr12 yellow">{{ this.WeatherWarnNumber }}</span
      >个 -->
      <!-- 暂时无法处理，需要有报警信息存在的时候才能修改 -->
      <div class="has-count-item">
        <p>蓝色预警</p>
        <p>
          <span class="numPeople" style="cursor: pointer">
            {{ WeatherWarnNumber.blue }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>黄色预警</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ WeatherWarnNumber.yellow }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>橙色预警</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ WeatherWarnNumber.orange }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>红色预警</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ WeatherWarnNumber.red }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import { mapGetters } from "vuex";
import CountWrap from "../components/CountWrap.vue";
export default {
  name: "WeatherWarnCount",
  data() {
    return {
      warnNum: 1,
    };
  },
  components: { CountWrap },
  computed: {
    ...mapGetters(["WeatherWarnNumber"]),
  },
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.weather-warn-count .has-count {
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
</style>
