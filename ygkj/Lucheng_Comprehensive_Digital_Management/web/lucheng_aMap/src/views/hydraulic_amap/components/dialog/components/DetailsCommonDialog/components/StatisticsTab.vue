<template>
	<div class="statistics-tabs">
		<div
			v-for="tab in tabs"
			:key="tab"
			:class="['statistics-tab__item', active == tab && 'active']"
			@click="onTabItemClick(tab)"
		>
			{{ tab }}
		</div>
	</div>
</template>

<script>
export default {
	name: "StatisticsTab",
	props: { active: String, tabs: { type: Array } },
	data() {
		return {};
	},
	methods: {
		onTabItemClick(tab) {
			this.$emit("update:active", tab);
			this.$emit("change", tab);
		}
	}
};
</script>

<style lang="scss">
.statistics-tabs {
	display: flex;
	margin-bottom: 20px;
}
.statistics-tab__item {
	padding: 10px 20px;
	margin-right: 20px;
	color: #fff;
	border: 1px solid rgba(255, 255, 255, 0.6);
	cursor: pointer;
	&.active {
		color: #56fefe;
		background: rgba(72, 152, 251, 0.1);
		border: 1px solid #56fefe;
		box-shadow: inset 0 0 5px 0 #56fefe;
	}
}
</style>
