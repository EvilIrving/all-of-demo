<template>
	<div class="schedule-legend">
		<div class="schedule-legend__wrap">
			<div @click="handleActive" class="schedule-legend__title" :style="active ? 'border-bottom: none;' : ''" v-if="showTitle">
				<span>图例</span>
				<i :class="active ? 'el-icon-d-arrow-left' : 'el-icon-d-arrow-right'" style="transform: rotate(-90deg)" />
			</div>
			<div v-show="active" :style="active ? 'border-bottom: none;' : ''">
				<slot>
					<el-row class="schedule-legend__content" :gutter="showTitle ? 0 : 14">
						<el-col v-for="(item, index) in legendData" :key="index" :span="item.span" class="schedule-legend__item">
							<p class="title">
								<span>{{ item.title }}</span>
								<i v-if="item.icon" :class="`icon-${item.icon}`" />
							</p>
							<div v-if="item.children" class="item-status__list">
								<div v-for="(status, sIndex) in item.children" :key="sIndex" class="item-status">
									<img :src="setImgSrc(status.icon)"/>
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
	props: { showTitle: { type: Boolean, default: true } },
	data() {
		return {
			active: true,
			legendData: [
				{
					title: '中型水库',
					span: 11,
					children: [
						{ icon: 'shuiku-02', label: '正常' },
						{ icon: 'shuiku-01', label: '超汛限' },
					]
				},
				{
					title: '水闸',
					span: 13,
          children: [
            { icon: 'shuizha-01', label: '开启' },
						{ icon: 'shuizha-02', label: '关闭' },
						{ icon: 'shuizha-03', label: '超警戒' }
					]
				},
				{
					title: '小型水库',
					span: 11,
					children: [
						{ icon: 'xiaoxingshuiku-02', label: '正常' },
						{ icon: 'xiaoxingshuiku-01', label: '超汛限' },
					]
				},
				{
					title: '其他',
					span: 13,
					children: [
						{ icon: 'cezhan', label: '测站' },
					]
				},
			]
		};
	},
	methods: {
		handleActive() {
			this.active = !this.active;
    },
    setImgSrc(icon) {
      return require(`@/assets/images/generalPoints/${icon}.png`)
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
					font-size: 16px;
					color: #23E3FB;
					font-weight: bold;
					margin-bottom: 12px;
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
					// justify-content: space-between;
					margin-bottom: 24px;
					.item-status {
						color: #fff;
						font-size: 14px;
						display: flex;
						align-items: center;
            margin-right: 16px;
            &:last-child{
              margin-right: 0;
            }
						> img {
							width: 16px;
							height: 16px;
							margin-right: 5px;
              position: relative;
              top: -2px;

						}
					}
				}
			}
		}
	}
}
</style>
