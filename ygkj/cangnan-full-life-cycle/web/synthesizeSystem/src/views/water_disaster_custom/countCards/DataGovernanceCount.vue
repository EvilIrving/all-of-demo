<template>
	<count-wrap title="数据治理" class="short-count">
		<div class="has-count">
			<div class="fs24">
				<p>治理规则数量</p>
				<p>
					<span class="green fs32">{{ ruleCount }}</span
					>mm
				</p>
			</div>
			<div class="fs24">
				<p>外部数据来源</p>
				<p>
					<span class="green fs32"> {{ externalDataCount }} </span>mm
				</p>
			</div>
			<div class="fs24">
				<p>经治理的数据量</p>
				<p>
					<span class="green fs32">{{ persistenceCount }} </span>mm
				</p>
			</div>
		</div>
	</count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { getGovernance4C } from "@/api/data-management/index.js";
export default {
	name: "DataGovernanceCount",
	data() {
		return {
			ruleCount: 9,
			externalDataCount: 54,
			persistenceCount: 14
		};
	},
	components: { CountWrap },
	mounted() {
		this.getGovernance4C();
	},
	methods: {
		getGovernance4C() {
			getGovernance4C().then(res => {
				if (res.code == 0) {
					const { ruleCount, externalDataCount, persistenceCount } = res.data;
					this.ruleCount = ruleCount;
					this.externalDataCount = externalDataCount;
					this.persistenceCount = persistenceCount;
				}
			});
		}
	}
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.short-count .has-count {
	height: 100%;
	display: flex;
	justify-content: space-between;
	> div {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		height: 100%;
	}
}
</style>
