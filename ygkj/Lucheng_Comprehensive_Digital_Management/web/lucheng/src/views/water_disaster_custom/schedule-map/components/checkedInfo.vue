<template>
	<div class="checked-info">
		<div
			v-for="(item, index) in checkedList"
			:key="index"
			:class="['info-item', checked.includes(item) && 'active']"
			@click="onItemClick(item)"
		>
			<p><i :class="iconMap[item]" /></p>
			<p>
				<span>{{ item }}</span>
			</p>
		</div>
	</div>
</template>
<script>
export default {
	name: 'CheckedInfo',
	model: { prop: 'checked', event: 'change' },
	props: { checked: Array },
	data() {
		return {
			iconMap: {
				名称: 'icon-general-name',
				水位: 'icon-general-waterlevel',
				流量: 'icon-general-flow'
			},
			checkedList: ['名称', '水位', '流量']
		};
	},
	methods: {
		onItemClick(item) {
			let ret = [];
			if (this.checked.includes(item)) {
				ret = this.checked.filter(value => value != item);
			} else {
				ret = this.checked.concat([item]);
			}
			this.$emit('change', ret);
		}
	}
};
</script>
<style lang="scss" scoped>
.checked-info {
	margin-top: 28px;
	color: #eee;

	.info-item {
		width: 60px;
		height: 60px;
		border-radius: 10px;
		cursor: pointer;
		margin-bottom: 10px;
		@include flexbox;
		@include flexflow(column, nowrap);
		@include flexJC(center);
		@include flexAC;
		p {
			margin-bottom: 4px;
			i {
				display: inline-block;
			}
			font-size: 14px;
			&:last-child {
				margin-bottom: 0px;
			}
		}
		&.active {
			background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
		}
	}
}
</style>
