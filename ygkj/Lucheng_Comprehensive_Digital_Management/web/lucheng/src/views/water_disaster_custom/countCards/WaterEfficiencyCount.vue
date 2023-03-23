<template>
	<count-wrap title="用水效率" class="travel-count">
		<div class="has-count">
			<div class="has-count-item">
				<p>万元GDP用水量省内排名</p>
				<p>
					<span class="num" style="cursor: pointer">
						{{ waterEfficiencyCount.wpgRank ? `第${waterEfficiencyCount.wpgRank}名` : "未知" }}
						{{ waterEfficiencyCount.wzWpg.watPerGdp ? waterEfficiencyCount.wzWpg.watPerGdp.toFixed(2) : "-" }}
					</span>
					<span class="unit">m³/万元</span>
				</p>
			</div>
			<div class="has-count-item">
				<p>万元工业增加值用水量全国排名</p>
				<p>
					<span class="num" style="cursor: pointer">
						第{{ waterEfficiencyCount.wpgidRank || 1 }}名
						{{ waterEfficiencyCount.wzWpgid.wpgInduDelta ? waterEfficiencyCount.wzWpgid.wpgInduDelta.toFixed(2) : "-" }}
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
		...mapGetters(["waterEfficiencyCount"])
	},
	components: { CountWrap },
	created() {},
	mounted() {},
	methods: {}
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
