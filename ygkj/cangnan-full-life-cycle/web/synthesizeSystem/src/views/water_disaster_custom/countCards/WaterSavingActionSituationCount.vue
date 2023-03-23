<!--
 * @Autor: ysl
 * @Date: 2021-08-12 16:33:53
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-14 11:41:33
 * @Description: 节水行动态势
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/WaterSavingActionSituationCount.vue
-->
<template>
  <count-wrap
    :title="waterSavingActionSituationCount.type"
    class="water-saving-action-situation-count"
  >
    <!-- 节水指标 -->
    <div v-if="waterSavingActionSituationCount.type == '节水指标'" style="height: 100%">
      <el-carousel
        trigger="click"
        arrow="never"
        :autoplay="true"
        :interval="5000"
      >
        <el-carousel-item class="carousel-item-content">
          <div class="has-count">
            <div class="has-count-item">
              <p>年用水总量</p>
              <p>
                <span class="numPeople" style="cursor: pointer">
                  {{ waterSavingActionSituationCount.totalWatCsm || 0 | toFixed }}
                </span>
                <span class="unit">亿m³</span>
              </p>
            </div>
            <div class="has-count-item">
              <p>万元GDP用水量下降率</p>
              <p>
                <span class="numPeople" style="cursor: pointer">
                  {{ waterSavingActionSituationCount.wpgDecrRate || 0 | toFixed }}
                </span>
                <span class="unit">%</span>
              </p>
            </div>
            <div class="has-count-item">
              <p>万元工业增加值用水量下降率</p>
              <p>
                <span class="numPeople" style="cursor: pointer">
                  {{ waterSavingActionSituationCount.induWpgDecrRate || 0 | toFixed }}
                </span>
                <span class="unit">%</span>
              </p>
            </div>
          </div>
        </el-carousel-item>
        <el-carousel-item class="carousel-item-content">
          <div class="has-count">
            <div class="has-count-item">
              <p>农业灌溉水有效利用系数</p>
              <p>
                <span class="numPeople" style="cursor: pointer">
                  {{ waterSavingActionSituationCount.agIrrEffectFactor || 0 | toFixed }}
                </span>
              </p>
            </div>
            <div class="has-count-item">
              <p>县域节水型社会创建达标率</p>
              <p>
                <span class="numPeople" style="cursor: pointer">
                  {{ waterSavingActionSituationCount.watSavRate || 0 | toFixed }}
                </span>
                <span class="unit">%</span>
              </p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
    <!-- 取水监管 -->
    <div class="has-count" v-if="waterSavingActionSituationCount.type == '取水监管'">
      <div class="has-count-item">
        <p>过期数量</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ waterSavingActionSituationCount.outOfDate || 0 | toFixed }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>超许可数量</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ waterSavingActionSituationCount.overPermit || 0 | toFixed }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>有证无点数量</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ waterSavingActionSituationCount.withoutWain || 0 | toFixed }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>取水偏快数量</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ waterSavingActionSituationCount.intakeTooFast || 0 | toFixed }}
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
  name: "waterSavingActionSituationCount",
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["waterSavingActionSituationCount"]),
  },
	filters: {
		toFixed(val) {
			let num = val ? val.toString().indexOf('.') != -1 ? Number(val).toFixed(2) : val : 0
			return num
		}
	},
  components: { CountWrap },
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.water-saving-action-situation-count {
  .el-carousel,
  ::v-deep .el-carousel__container {
    height: 100%;
  }
  ::v-deep .el-carousel {
		.el-carousel__indicators--horizontal {
			.el-carousel__indicator {
				padding: 0 8px;
			}
		}
    .el-carousel__indicator {
      .el-carousel__button {
        padding: 0;
        width: 0.5625rem;
        height: 0.5625rem;
        border-radius: 50%;
      }
      &.is-active {
        button {
          background-color: #1ef8f2;
        }
      }
    }
  }
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
