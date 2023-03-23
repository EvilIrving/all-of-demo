<!--
 * @Autor: ysl
 * @Date: 2021-08-12 20:06:54
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-13 15:35:38
 * @Description: 三条红线C位
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/ThreeRedLineCount.vue
-->
<template>
	<count-wrap title="三条红线" class="water-saving-action-situation-count">
		<div class="has-count">
			<div class="has-count-item">
				<p>用水总量未达标数量</p>
				<p>
					<span class="num" style="cursor: pointer">
						{{ threeRedLineCount.watCsmReachNormNum || 0 }}
					</span>
					<span class="unit">个</span>
				</p>
			</div>
			<div class="has-count-item">
				<p>用水效率未达标数量</p>
				<p>
					<span class="num" style="cursor: pointer">
						{{ threeRedLineCount.wpgEffectReachNormNum || 0 }}
					</span>
					<span class="unit">个</span>
				</p>
			</div>
			<div class="has-count-item">
				<p>限制纳污未达标数量</p>
				<p>
					<span class="num" style="cursor: pointer">
						{{ threeRedLineCount.limitPollutantNum || 0 }}
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
  name: "threeRedLineCount",
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["threeRedLineCount"]),
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
      .num {
        font-size: 32px;
        color: #eec80b;
      }
    }
  }
}
</style>
