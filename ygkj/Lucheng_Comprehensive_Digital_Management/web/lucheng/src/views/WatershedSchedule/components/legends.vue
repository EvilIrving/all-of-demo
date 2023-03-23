<template>
	<div class="schedule-legend">
		<div class="schedule-legend__wrap">
			<div @click="handleActive" class="schedule-legend__title" :style="active ? 'border-bottom: none;' : ''">
				<span>图例</span>
				<i :class="active ? 'el-icon-d-arrow-left' : 'el-icon-d-arrow-right'" style="transform: rotate(-90deg)" />
			</div>
			<div v-show="active" :style="active ? 'border-bottom: none;' : ''">
				<slot>
					<el-row class="schedule-legend__content">
						<el-col v-for="(item, index) in legendData" :key="index" :span="item.span" class="schedule-legend__item">
							<p class="title">
								<span>{{ item.title }}</span>
								<i v-if="item.icon" :class="`icon-${item.icon}`" />
							</p>
							<div v-if="item.children" class="item-status__list">
								<div v-for="(status, sIndex) in item.children" :key="sIndex" class="item-status">
									<i :class="`icon-${status.icon}`" />
									<span>{{ status.label }}</span>
								</div>
							</div>
						</el-col>
					</el-row>
				</slot>
			</div>
		</div>
	</div>
</template>

<script>
export default {
	name: 'ScheduleLegend',
	data() {
		return {
			active: true,
			legendData: [
				{
					title: '大型水库',
					span: 12,
					children: [
						{ icon: 'rs-big-normal', label: '正常' },
						{ icon: 'rs-big-warning', label: '泄洪' },
						{ icon: 'rs-big-error', label: '超汛限' },
						{ icon: 'gate-nodata', label: '无数据' }
					]
				},
				{
					title: '水闸',
					span: 12,
					children: [
						{ icon: 'gate-normal', label: '正常' },
						{ icon: 'gate-open', label: '开启' },
						{ icon: 'gate-nodata', label: '无数据' }
					]
				},
				{
					title: '中型水库',
					span: 12,
					children: [
						{ icon: 'rs-mid-normal', label: '正常' },
						{ icon: 'rs-mid-warning', label: '泄洪' },
						{ icon: 'rs-mid-error', label: '超汛限' },
						{ icon: 'gate-nodata', label: '无数据' }
					]
				},
				{
					title: '潮位',
					span: 12,
					children: [
						{ icon: 'tide-normal', label: '正常' },
						{ icon: 'tide-warning', label: '超警' },
						{ icon: 'tide-nodata', label: '无数据' }
					]
				},
				{
					title: '小型水库',
					span: 12,
					children: [
						{ icon: 'rs-small-normal', label: '正常' },
						{ icon: 'rs-small-warning', label: '泄洪' },
						{ icon: 'rs-small-error', label: '超汛限' },
						{ icon: 'gate-nodata', label: '无数据' }
					]
				},
				{
					title: '水文',
					span: 12,
					children: [
						{ icon: 'sw-normal', label: '正常' },
						{ icon: 'sw-warning', label: '超警' },
						{ icon: 'sw-nodata', label: '无数据' }
					]
				},
				{
					title: '排涝工程',
					icon: 'general-drain'
				}
			]
		};
	},
	methods: {
		handleActive() {
			this.active = !this.active;
		}
	}
};
</script>

<style lang="scss">
.schedule-legend {
	position: absolute;
	right: 1%;
	bottom: 9%;
	width: 600px;
	.schedule-legend__wrap {
		position: relative;
		opacity: 0.8;
		&::after {
			content: '';
			position: absolute;
			height: calc(100% + 4px);
			width: calc(100% + 4px);
			top: -2px;
			left: -2px;
			background: linear-gradient(to bottom, #35b9e3, #5398f6) left top no-repeat,
				linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
				linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
				linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat,
				linear-gradient(to bottom, #5398f6, #35b9e3) left bottom no-repeat,
				linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
				linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
				linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
			/*设置大小*/
			background-size: 18px 2px, 2px 18px;
			z-index: -1;
		}
		.schedule-legend__title {
			cursor: pointer;
			font-size: 18px;
			color: #fff;
			padding: 16px;
			background-color: #041c36;
			box-shadow: 0 0 20px 20px inset #1f5183;
			border: 1px solid #00c1ff;
			border-bottom: none;
		}
		.schedule-legend__content {
			padding: 20px;
			background-color: #041c36;
			border: 1px solid #00c1ff;
			border-top: none;
			.schedule-legend__item {
				padding-right: 32px;
				.title {
					font-size: 18px;
					color: #1bf6ee;
					font-weight: bold;
					margin-bottom: 8px;
					> i {
						vertical-align: middle;
						width: 18px;
						height: 18px;
						margin-left: 3px;
					}
				}
				.item-status__list {
					display: flex;
					align-items: center;
					justify-content: space-between;
					margin-bottom: 24px;
					.item-status {
						color: #fff;
						font-size: 14px;
						display: flex;
						align-items: center;
						> i {
							width: 18px;
							height: 18px;
							margin-right: 3px;
						}
					}
				}
			}
		}
	}
}
</style>
