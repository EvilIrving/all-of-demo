<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2021-07-15 18:12:26
 * @LastEditors: Please set LastEditors
 * @Description: 清四乱落点弹窗
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCard.vue
-->
<template>
	<div class="detail-table">
		<el-dialog
			width="60%"
			destroy-on-close
			:visible.sync="visible"
			:before-close="handleClose"
			append-to-body
			:modal-append-to-body="true"
			:close-on-click-modal="false"
			class="primary-dialog"
		>
			<!-- 弹窗标题 -->
			<div class="primary-dialog__title" slot="title">
				<p class="primary-dialog__title--label">{{ name }}</p>
			</div>
			<!-- 内容区域 -->
			<div class="primary-dialog__content">
				<div class="content-panel-item">
					<div class="title-box">
						<div class="title-panel">
							<i class="icon-diamonds" style="margin-right: 10px; cursor: pointer"></i>
							<span>整改前图像</span>
						</div>
						<i class="qsl-ld-dialog-line"></i>
					</div>
					<div class="content-box">
						<div class="img-panel" v-if="list.rectImgbeforeList && list.rectImgbeforeList.length > 0">
							<el-image
								v-for="(item, i) in list.rectImgbeforeList"
								:key="i"
								:src="item.fileUrl"
								:preview-src-list="list.rectImgbeforeList.map(i => i.fileUrl)"
							/>
						</div>
						<p v-else style="color: #a6a6a6">暂无图片</p>
					</div>
				</div>

				<div class="content-panel-item">
					<div class="title-box">
						<div class="title-panel">
							<i class="icon-diamonds" style="margin-right: 10px; cursor: pointer"></i>
							<span>整改后图像</span>
						</div>
						<i class="qsl-ld-dialog-line"></i>
					</div>
					<div class="content-box">
						<div class="img-panel" v-if="list.rectImgAfterList && list.rectImgAfterList.length > 0">
							<el-image
								v-for="(item, i) in list.rectImgAfterList"
								:key="i"
								:src="item.fileUrl"
								:preview-src-list="list.rectImgAfterList.map(i => i.fileUrl)"
							/>
						</div>
						<p v-else style="color: #a6a6a6">暂无图片</p>
					</div>
				</div>

				<div class="content-panel-item">
					<div class="title-box">
						<div class="title-panel">
							<i class="icon-diamonds" style="margin-right: 10px; cursor: pointer"></i>
							<span>详细信息</span>
						</div>
						<i class="qsl-ld-dialog-line"></i>
					</div>
					<div class="content-box">
						<el-row :gutter="20">
							<el-col :span="8">
								<span class="label">详细地址</span>
								<span class="value">{{ list.address || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">所在乡镇</span>
								<span class="value">{{ list.town || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">所在街道</span>
								<span class="value">{{ list.street || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">地理坐标</span>
								<span class="value"
									>{{ Number(list.lng).toFixed(2) || '-' }}，{{ Number(list.lat).toFixed(2) || '-' }}</span
								>
							</el-col>
							<el-col :span="8">
								<span class="label">问题来源</span>
								<span class="value">{{ list.problemSource || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">河道等级</span>
								<span class="value">{{ list.riverLevel || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">关联河湖</span>
								<span class="value">{{ list.riverName || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">河长姓名</span>
								<span class="value">{{ list.riverManagerName || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">四乱类型</span>
								<span class="value">{{ list.fourType || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">严重程度</span>
								<span class="value">{{ list.severity || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">问题描述</span>
								<span class="value">{{ list.problemDescription || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">占地面积(k㎡)</span>
								<span class="value">{{ list.occupySpace || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">备注说明</span>
								<span class="value">{{ list.remark || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">整改要求</span>
								<span class="value">{{ list.rectificationRequirement || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">通知单位</span>
								<span class="value">{{ list.deptName || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">通知人员</span>
								<span class="value">{{ list.patrolUserName || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">联系方式</span>
								<span class="value">{{ list.patrolUserPhone || '-' }}</span>
							</el-col>
							<el-col :span="8">
								<span class="label">通知方式</span>
								<span class="value">{{ list.sendType == '1' ? '短信' : '浙政钉' }}</span>
							</el-col>
						</el-row>
					</div>
				</div>
			</div>
		</el-dialog>
	</div>
</template>

<script>
export default {
	name: 'FourChaosDialog',
	data() {
		return {
			name: '',

			loading: false,
			list: {}
		};
	},
	props: {
		visible: {
			type: Boolean,
			default: false
		}
	},
	components: {},
	created() {},
	mounted() {},
	computed: {},
	methods: {
		init(val) {
			this.name = val.name;
			this.list = val.obj;
		},

		handleClose() {
			this.$emit('closeDialog');
		}
	}
};
</script>
<style lang="scss" scoped>
.primary-dialog {
	.content-panel-item {
		margin-bottom: 25px;
		&:last-child {
			margin-bottom: 0;
		}
	}
	.title-panel {
		color: #fff;
		font-size: 18px;
		display: flex;
		align-items: center;
	}
	.content-box {
		margin-top: 15px;
	}

	.img-panel {
		img {
			display: inline-block;
			width: 150px;
			height: 100px;
			margin-right: 10px;
		}
	}

	.el-col {
		color: #fff;
		font-size: 15px;
		line-height: 38px;
		&:last-child {
			margin-bottom: 0;
		}
		.value {
			color: #33ffff;
			font-size: 16px;
			margin-left: 20px;
		}
	}

	.qsl-ld-dialog-line {
		display: inline-block;
		height: 3px;
		width: 100%;
		background-image: url(~assets/images/qsl-ld-dialog-line.png);
		background-size: 100% 100%;
	}

	::v-deep .el-dialog {
		.el-dialog__header {
			padding: 2px;
			.primary-dialog__title {
				.primary-dialog__title--label {
					width: 80%;
					height: 32px;
					color: #fff;
					font-size: 16px;
					letter-spacing: 0;
					margin-left: 15px;
					font-weight: bold;
					line-height: 35px;
					padding-left: 56px;
				}
			}
		}
		.el-dialog__body {
			// height: 300px;
			padding: 2px;
			padding-top: 0;
			border-bottom: 0.0625rem solid #00c1ff;
			position: relative;
			.primary-dialog__content {
				height: 100%;
				padding: 20px;
			}
		}
	}
}
</style>
