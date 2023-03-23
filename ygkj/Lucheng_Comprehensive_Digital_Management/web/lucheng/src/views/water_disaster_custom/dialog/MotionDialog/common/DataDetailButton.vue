<template>
	<div class="data-detail-button" :style="{ width: '65px', marginLeft: right && 'auto' }" @click="onClick">
		<slot></slot>
	</div>
</template>

<script>
export default {
	name: "DataDetailButton",
	props: {
		right: { type: Boolean, default: false }
	},
	methods: {
		onClick() {
			this.$emit("click");
		}
	}
};
</script>

<style>
.data-detail-button {
	white-space: nowrap;
	padding: 0 15px;
	text-align: center;
	border-radius: 4px;
	line-height: 30px;
	float: right;
	color: #fff;
	cursor: pointer;
	border-color: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%) !important;
	background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
}
.data-detail-button + .data-detail-button {
	margin-left: 8px;
}
</style>
