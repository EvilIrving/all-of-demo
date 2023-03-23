<template>
	<count-wrap title="数据汇聚" class="data-gather">
		<div class="has-count">
			<div class="fs24">
				<p>雨情在线率</p>
				<p><span class="green fs32">25</span>%</p>
			</div>
			<div class="fs24">
				<p>水清在线率</p>
				<p><span class="green fs32"> 100 </span>%</p>
			</div>
			<div class="fs24">
				<p>工情在线率</p>
				<p><span class="green fs32">95 </span>%</p>
			</div>
			<div class="fs24">
				<p>视频在线率</p>
				<p><span class="green fs32">76 </span>%</p>
			</div>
		</div>
	</count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
export default {
	name: "DataGovernanceCount",
	data() {
		return {};
	},
	components: { CountWrap },
	mounted() {},
	methods: {}
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.data-gather .has-count {
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
