<!--
 * @Author: dyn
 * @Date: 2021-07-13 09:23:38
 * @LastEditTime: 2021-07-14 13:56:29
 * @LastEditors: Please set LastEditors
 * @Description: 子标题为可选择tab
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\components\SubTitleCard.vue
-->
<template>
	<div class="sub-title-card">
		<div v-if="title">
			<div class="title-box">
				<div class="title-panel">
					<i class="icon-diamonds" style="margin-right: 10px; cursor: pointer"></i>

					<div class="tab-panel" v-if="type == 'tab'">
						<div v-for="(tab, i) in title" :key="i">
							<span :class="['title-label', activeTab == i ? 'active' : '']" @click="tabChange(i)">
								{{ tab }}
							</span>
							<i class="icon-line" v-if="i < title.length - 1"></i>
						</div>
					</div>

					<div class="tab-panel" v-if="type == 'radio'">
						<span class="radio-title">{{ title }}</span>
						<div class="radio-box">
							<el-radio-group v-model="activeRadio" @change="radioChange">
								<el-radio v-for="(item, ind) in radioList" :key="ind" :label="ind">
									{{ item }}
								</el-radio>
							</el-radio-group>
						</div>
					</div>
				</div>
				<slot name="title-operate"> </slot>
			</div>
			<i class="icon-card-title-divide"></i>
		</div>
		<div class="card-content">
			<slot></slot>
		</div>
	</div>
</template>

<script>
export default {
	name: "",
	props: {
		title: {
			type: Array | String,
			default: () => []
		},
		type: {
			type: String,
			default: "tab"
		},
		radioList: {
			type: Array,
			default: () => []
		}
	},
	data() {
		return {
			activeTab: 0, // 当前tab
			activeRadio: 0
		};
	},
	components: {},
	created() {},
	mounted() {},
	methods: {
		// 点击标题切换
		tabChange(val) {
			this.activeTab = val;
			this.$emit("tabChange", this.title[val]);
		},
		// 点击标题切换
		radioChange(val) {
			this.activeRadio = val;
			this.$emit("radioChange", this.radioList[val]);
		}
	}
};
</script>
<style lang="scss" scoped>
.sub-title-card {
	width: 100%;
	height: 100%;
	display: flex;
	flex-direction: column;
	.title-box {
		display: flex;
		justify-content: space-between;
		align-items: center;
		font-size: 18px;
		color: #ffffff;
		margin-top: 12px;
		.title-panel,
		.tab-panel {
			display: flex;
			align-items: center;
		}
		.radio-box {
			margin-left: 20px;
			::v-deep .el-radio__label {
				color: #fff;
				font-size: 16px;
				line-height: 1;
				padding-left: 5px;
			}
			::v-deep .is-checked .el-radio__label {
				color: #1ef8f2;
			}
			::v-deep .is-checked .el-radio__inner {
				border: 1px solid #1ef8f2;
				background: none;
			}
			::v-deep .el-radio__inner {
				border: 1px solid #fff;
				background: none;
			}
			::v-deep .is-checked .el-radio__inner::after {
				width: 8px;
				height: 8px;
				border-radius: 100%;
				background-color: #1ef8f2;
			}
		}
		.active {
			color: #1ef8f2;
		}
		.title-label {
			cursor: pointer;
		}
		.icon-card-title-divide {
			cursor: unset;
			margin-bottom: 4px;
		}
		.icon-line {
			width: 1px;
			height: 15px;
			border: 1px solid rgba(255, 255, 255, 0.2);
			margin: 0 15px;
		}
	}
	.card-content {
		width: 100%;
		height: 100%;
		display: flex;
		flex-direction: column;
		margin-top: 10px;
	}
}
</style>
