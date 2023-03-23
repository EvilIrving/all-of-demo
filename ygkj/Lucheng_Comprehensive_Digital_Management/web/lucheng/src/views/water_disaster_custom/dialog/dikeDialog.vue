<template>
	<div class="primary-dialog emergency-scheme dikeDialog">
		<el-dialog
			top="10vh"
			width="1200px"
			destroy-on-close
			:visible.sync="visible"
			v-loading="exportLoading"
			element-loading-text="拼命导出中。。。"
			element-loading-spinner="el-icon-loading"
			element-loading-background="rgba(0, 0, 0, 0.8)"
			:before-close="handleClose"
			:modal-append-to-body="true"
			:close-on-click-modal="false"
			:fullscreen="fullscreen"
		>
			<!-- 弹窗标题 -->
			<div class="primary-dialog__title" slot="title">
				<p class="primary-dialog__title--label">堤防详表</p>
				<i @click="changeFullscreen" :class="['el-icon-full-screen','full-screen-icon']"></i>
			</div>
			<!-- 内容区域 -->
			<div class="primary-dialog__content">
				<div class="statistics-sty">
					<!-- 图标 -->
					<div>
						<p><i class="icon-dikedialog" /></p>
						<div>
							<p>数量统计</p>
							<p>
								<span> {{ totalNum }} </span>
                <span style="color: #fff; font-size: 16px">条</span>
							</p>
						</div>
					</div>
					<!-- start -->
					<div>
						<p><i class="icon-dikedialog" /></p>
						<div>
							<p>总长度</p>
							<p>
								<span> {{ dikeLen }} </span>
                <span style="color: #fff; font-size: 16px">m</span>
							</p>
						</div>
					</div>
					<!-- 1 -->
					<div>
						<p><i class="icon-dikedialog" /></p>
						<div>
							<p>最多数量</p>
							<p>
								<span> {{ highestNumber }} </span>
							</p>
						</div>
					</div>
					<!-- 2 -->
					<div>
						<p><i class="icon-dikedialog" /></p>
						<div>
							<p>最少数量</p>
							<p>
								<span> {{ miniNumber }} </span>
							</p>
						</div>
					</div>
					<!-- end -->
				</div>
				<!-- 行政区划筛选-->
				<div class="query-sty">
					<el-col :span="6" class="bb">
						<div class="demo-input-suffix">
							<p style="width: 70px">行政区划：</p>
							<el-select
								v-model="administrationValue"
								style="width: 180px; height: 32px"
								size="small"
								clearable
								placeholder="全部"
								@change="handleadcd"
							>
								<el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adcd" />
							</el-select>
						</div>
					</el-col>
					<!-- 堤防规模选择 -->
					<el-col class="bb" :span="6" style="width: 250px">
						<div class="demo-input-suffix">
							<p style="width: 70px">堤防规模：</p>
							<el-select
								v-model="dikeOptionsType"
								size="small"
								style="width: 160px; height: 32px"
								clearable
								placeholder="全部"
								@change="handledike"
							>
								<el-option v-for="item in dikeOptions" :key="item.value" :label="item.label" :value="item.value" />
							</el-select>
						</div>
					</el-col>
					<el-col :span="6" class="bb" style="margin-left: 15px">
						<div class="demo-input-suffix" style="margin-left: 0px">
							<p style="width: 100px; padding-right: 0px">关键字：</p>
							<el-input placeholder="请输入内容" v-model="NameVal" class="mineClass" clearable @blur="queryVal" />
						</div>
					</el-col>
					<el-col :span="6">
            <div class="search-sty" style="margin-left: 10px;" @click="exportExcel">导 出</div>
            <!-- <div class="search-sty" @click="queryVal" >搜 索</div> -->
          </el-col>
				</div>
				<!-- tab展示区域 -->
				<!-- 堤防规模 -->
				<div class="tab-list-sty">
					<el-table
						ref="travelTable"
						:data="tableData"
						height="90%"
						stripe
						class="bcd"
						v-loading="loading"
						element-loading-text="拼命加载中"
						element-loading-spinner="el-icon-loading"
						element-loading-background="rgba(0, 0, 0, 0.8)"
						@sort-change="sortChange"
					>
						<el-table-column prop="index" label="序号" width="55">
							<template slot-scope="scope">
								<span class="table-order">{{
									pageNum == 1 ? scope.$index + 1 : (pageNum - 1) * pageSize + scope.$index + 1
								}}</span>
							</template>
						</el-table-column>
						<el-table-column
							:show-overflow-tooltip="true"
							:formatter="toFixedFormatter"
							prop="dikeName"
							label="工程名称"
						/>
						<el-table-column
							:show-overflow-tooltip="true"
							:formatter="toFixedFormatter"
							prop="managementLevel"
							label="管理层级"
						/>
						<el-table-column
							:show-overflow-tooltip="true"
							:formatter="toFixedFormatter"
							prop="countryAdnm"
							label="行政区划"
						/>
						<el-table-column
							:show-overflow-tooltip="true"
							:formatter="toFixedFormatter"
							prop="dikeGrad"
							label="堤防规模(级)"
						/>
						<el-table-column :show-overflow-tooltip="true" :formatter="toFixedFormatter" prop="bas" label="所在河流" />
						<el-table-column
							:show-overflow-tooltip="true"
							:formatter="toFixedFormatter"
							prop="riverLevel"
							label="河道级别"
						/>
						<el-table-column :show-overflow-tooltip="true" :formatter="toFixedFormatter" prop="rvBank" label="岸别" />
						<el-table-column
							:show-overflow-tooltip="true"
							:formatter="toFixedFormatter"
							prop="dikeType"
							label="堤防类型"
						/>
						<el-table-column
							:show-overflow-tooltip="true"
							:formatter="toFixedFormatter"
							:sortable="'custom'"
							prop="dikeLen"
							label="堤防长(m)"
						/>
						<el-table-column
							:show-overflow-tooltip="true"
							:formatter="toFixedFormatter"
							:sortable="'custom'"
							prop="flStaLen"
							label="标绘长(m)"
						/>
						<el-table-column
							:show-overflow-tooltip="true"
							:formatter="toFixedFormatter"
							prop="planFlSta"
							width="160px"
							label="防洪标准(年)"
						/>
					</el-table>
					<!-- 分页 -->
          <div class="pagination-area" style="padding-top:15px;">
            <el-pagination
              class="right"
              @size-change="handleSizeChange"
              @current-change="dikeSrcTable"
              :current-page.sync="pageNum"
              :page-sizes="[10, 20, 30, 50]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next"
              :total="total"
            />
          </div>
				</div>
			</div>
		</el-dialog>
	</div>
</template>
<script>
  import axios from "axios";
  import config from "@/config/config";
  import { localData } from "@/plugins/storage";
	import { disasterPreventionApi } from '@/api/RightSildePanel';
	export default {
		name: 'dikeDialog',
		props: {
			visible: {
				type: Boolean,
				default: false
			},
			code: {
				type: String,
				default: ''
			}
		},
		data() {
			return {
				// 导出Loading
				exportLoading: false,
				totalNum: '',
				dikeLen: '',
				highestNumber: '',
				miniNumber: '',
				selectVal: '',
				NameVal: '',
				tableData: [],
				dikeOptionsType: '', //堤防规模
				dikeOptions: [
					// {
					//    value:1,
					//    label:"1级"
					//  },
					{
						value: '2',
						label: '2级'
					},
					//  {
					//    value:3,
					//    label:"3级"
					//  },
					{
						value: '4',
						label: '4级'
					},
					{
						value: '5',
						label: '5级'
					}
				], //堤防规模数组
				administration: '', //行政区划
				administrationValue: [], //行政区划数组
				waterLoggingType: null,
				selectValue: '', //下拉值
				loading: false,
				pageNum: 1,
				pageSize: 10,
				total: 0,
				fullscreen:false,
			};
		},
		created() {
			this.getcAStatisticalProject(); //c位标题需要
			this.getArea(); //行政区划
		},
		methods: {
			changeFullscreen(){
				this.fullscreen=!this.fullscreen;
				this.$refs.travelTable.doLayout();
			},
			init() {
				this.getcAStatisticalProject();
				this.dikeSrcTable();
			},
			getcAStatisticalProject() {
				disasterPreventionApi.getcAStatisticalProject({ type: '堤防' }).then(res => {
					if (res.code == 0) {
						this.totalNum = res.data.totalNum;
						this.dikeLen = res.data.dikeLen;
						this.highestNumber = res.data.highestNumber;
						this.miniNumber = res.data.miniNumber;
					}
				});
			},
			// 分页条数
      handleSizeChange(val) {
        this.pageSize = val;
        this.dikeSrcTable();
      },
			// 导出
			async exportExcel() {
				this.exportLoading = true;
				let obj = {
					pageNum: this.pageNum,
					pageSize: this.pageSize,
					dikeName: this.NameVal,
					exportStrategy: 0,
					dikeDrad: this.dikeOptionsType,
					areaCode: this.administrationValue
				};
				let userInfo = localData("get", "userInfo");
				let { accessToken } = userInfo ? userInfo : { accessToken: "" };
				axios({
					url: `${config.baseUrl}/project-manage/newProjectManage/exportDikeProject`,
					method: "post",
					data: obj,
					responseType: "blob",
					headers: {
						authorization: accessToken,
					},
				}).then((res) => {
					const content = res.data;
					const blob = new Blob([content], { type: "application/msword" });
					const link = document.createElement("a"); // 创建a标签
					let filename = res.headers["content-disposition"];
					filename = filename ? filename.substr(filename.indexOf("=") + 1) : "";
					filename = decodeURIComponent(filename);
					link.download = filename;
					link.style.display = "none";
					link.href = URL.createObjectURL(blob);
					document.body.appendChild(link);
					link.click(); // 执行下载
					URL.revokeObjectURL(link.href); // 释放url
					document.body.removeChild(link); // 释放标签
					this.exportLoading = false;
				}).catch(error => {
					this.exportLoading = false;
				});
			},
			//搜索
			queryVal() {
				this.dikeSrcTable();
			},
			indexMethod(index) {
				return index + 1;
			},

			/**
			 * 弹窗关闭
			 */
			handleClose() {
				this.$emit('closeDialog');
			},
			// 下拉行政规划选择后
			handleadcd() {
				this.getArea();
			},
			// 下拉堤防规模选择后
			handledike() {
				this.dikeSrcTable();
			},
			/**
			 * 表格数据格式化
			 */
			toFixedFormatter(row, column, value, index) {
				if (value != null) {
					return value;
				} else {
					return '-';
				}
			},

			// 搜索
			async dikeSrcTable() {
				this.loading = true;
				let opt = {
					pageNum: this.pageNum,
					pageSize: this.pageSize,
					dikeName: this.NameVal,
					dikeDrad: this.dikeOptionsType,
					areaCode: this.administrationValue
				};
				await disasterPreventionApi.dikeproject(opt).then(res => {
					if (res.code == 0) {
						this.tableData = res.data.list;
						this.total = res.data.totalRows;
					}
				});

				this.loading = false;
			},
			/**
			 * 行政区划
			 */
			async getArea() {
				let obj = {
					areaCode: 3303,
					level: 3
				};
				await disasterPreventionApi.getArea(obj).then(res => {
					if (res.code == 0) {
						this.administration = res.data;
					}
				});
				this.dikeSrcTable();
			},
			//排序
			sortChange(val) {
				if (val.order) {
					if (val.order == 'ascending') {
						this.tableData.sort(this.sortBy(val.prop, false));
					} else {
						this.tableData.sort(this.sortBy(val.prop, true));
					}
				} else {
					this.tableData.sort(this.sortBy(val.prop, false));
				}
			},
			sortBy(attr, rev) {
				rev = rev ? 1 : -1;
				return function(a, b) {
					a = a[attr];
					b = b[attr];
					if (a < b) {
						return rev * -1;
					}
					if (a > b) {
						return rev * 1;
					}
					return 0;
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
@import '@/style/bigScreen.scss';
@import '@/style/sidebarStyle.scss';
// 去掉table表格纵向滚动条
.dikeDialog ::v-deep .el-table__body-wrapper::-webkit-scrollbar {
	height: 6px; // 纵向滚动条 必写
	width: 6px;
}
.dikeDialog {
	::v-deep .el-table__empty-text,
	::v-deep.el-loading-text {
		text-align: center;
	}
}
//设置table表格字体样式
::v-deep.sidebar-list .el-table .cell {
	font-size: 12px;
	opacity: 0.8;
}
//设置table表格 列宽，字间距样式
::v-deep .el-table {
	.cell {
		height: $tdHeight;
		line-height: $tdHeight;
	}
	// 设置勾选框样式
	.el-checkbox__inner {
		border: 0.0625rem solid #00c1ff;
		background-color: #00c1ff;
		background: rgba($color: #000000, $alpha: 0);
	}
	// 设置选中行样式颜色
	.el-table__body tr.current-row > td {
		background-color: rgba(41, 147, 180, 0.3);
	}
	th,
	td {
		padding: 0;
		color: #fff;
		font-size: 12px;
	}
	th {
		font-size: 14px;
		color: #00c1ff;
		background-color: rgba(41, 147, 180, 0.1);
	}
}
</style>
<style lang="scss">
.realtime-250 {
	background-color: #f32fdf;
	@include icon();
}
.realtime-100 {
	background-color: #fe323a;
	@include icon();
}
.realtime-50 {
	background-color: #ff8a21;
	@include icon();
}
.realtime-25 {
	background-color: #fecb6e;
	@include icon();
}
.realtime-10 {
	background-color: #4bc704;
	@include icon();
}
.realtime-0_1 {
	background-color: #00a0ff;
	@include icon();
}
.forecast-100 {
	background-color: #7f0140;
	@include icon();
}
.forecast-70 {
	background-color: #f800fb;
	@include icon();
}
.forecast-10 {
	background-color: #3cba3e;
	@include icon();
}
.forecast-0 {
	background-color: #fff;
	@include icon();
}
.forecast-50 {
	background-color: #0001e1;
	@include icon();
}
.forecast-25 {
	background-color: #60b8ff;
	@include icon();
}
.forecast-10 {
	background-color: #48b940;
	@include icon();
}
.forecast-1 {
	background-color: #92ff74;
	@include icon();
}
// 供水量图例图标
.legend-icon-xushui {
	background-image: url(~assets/images/gongshui.png);
	background-repeat: no-repeat;
}
.legend-icon-tishui {
	background-image: url(~assets/images/tishui.png);
	background-repeat: no-repeat;
}
.legend-icon-yinshui {
	background-image: url(~assets/images/yinshui.png);
	background-repeat: no-repeat;
}

// 水资源承载能力 水量水质评价图例
.water-buchaozai {
	background-color: #19af47;
	@include icon();
}
.water-linjie {
	background-color: #008fe8;
	@include icon();
}
.water-chaozai {
	background-color: #e89100;
	@include icon();
}
.water-yanzhong {
	background-color: #e80000;
	@include icon();
}

// 水资源承载能力 经济人口评价图例
.water-max {
	background-color: #00c1ff;
	@include icon();
}
.water-min {
	background-color: #19af47;
	@include icon();
}
.water-normal {
	background-color: #33ffff;
	@include icon();
}
</style>

<style lang="scss" scoped>
.pagination-area {
    padding: 0 0 16px 0;
    color: #fff;
    ::v-deep .el-pagination {
      .el-pagination__total{
        color:#fff;
      }
      .el-pagination__jump{
        color:#fff;
      }
      .el-pager {
        >li {
          background: #0000;
          color: #fff;
          &.active {
            background-image: linear-gradient(
              180deg,
              #36c0e4 20%,
              #0639ff 100%
            );
            border-radius: 2px;
          }
        }
      }
    }
  }
.mg-l16 {
	margin-left: 16px;
}
.fr {
	float: right;
}
.primary-dialog {
	::v-deep .el-dialog {
		@include flexbox;
		@include flexflow(column,nowrap);
		height: 680px;
		&.is-fullscreen{
			height:100%;
			overflow: hidden;
		}
		.el-dialog__header {
			padding: 2px;
			.primary-dialog__title {
				position: relative;
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
				.full-screen-icon{
					position:absolute;
					right:40px;
					color:#00c1ff;
          cursor: pointer;
          top:15px;
				}
			}
		}
		.el-dialog__body {
			padding: 2px;
			padding-top: 0;
			border-bottom: 0.0625rem solid #00c1ff;
			@include flex(1);
			.primary-dialog__content {
				@include flexbox;
				@include flexflow(column, nowrap);
				height:100%;
				// height: 680px;
				border-top: 0;
				padding: 0 16px;
				box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
				background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);

				// 统计样式
				.statistics-sty {
					padding: 10px 0;
					width: 100%;
					display: flex;
					justify-content: left;
					border-bottom: 1px solid rgba($color: #409eff, $alpha: 0.4);
					> div {
						// margin-right: 50px;
						padding: 10px 0;
						margin-right: 20px;
						display: flex;
						justify-content: space-between;
						> p {
							padding-top: 5px;
							margin-right: 15px;
						}
						> div {
							> p:nth-child(1) {
								font-family: PingFangSC-Regular;
								font-size: 16px;
								color: #ffffff;
								letter-spacing: 0;
								line-height: 24px;
								font-weight: 400;
								margin-bottom: 5px;
							}
							> p:nth-child(2) {
								font-family: PingFangSC-Medium;
								font-size: 20px;
								// color: #fff;
								letter-spacing: 0;
								line-height: 24px;
								font-weight: 500;

								color: #56fefe;

								> span {
									font-size: 20px;
									color: #56fefe;
								}
							}
						}
						// 弹窗底部按钮样式
						.dialog-footer {
							position: absolute;
							right: 20px;
							bottom: 15px;
							.el-button--primary.is-plain {
								color: #fff;
								border-color: #0bbbff;
								background-color: rgba($color: #0bbbff, $alpha: 0.1);
							}
						}
						::v-deep.primary-dialog .el-dialog .el-table th {
							color: #1cfff5;
						}
						::v-deep.el-table .ascending .sort-caret.ascending {
							border-bottom-color: #1cfff5;
						}
						::v-deep.el-table .descending .sort-caret.descending {
							border-top-color: #1cfff5;
						}
						::v-deep.mineClass .el-input__suffix {
							opacity: 0.7;
							font-family: PingFangSC-Regular;
							font-size: 14px;
							color: #ffffff;
							font-weight: 400;
						}
						::v-deep.el-date-editor .el-range-input {
							background: none;
							color: #fff;
						}
						::v-deep.el-date-editor .el-range-separator {
							color: #fff;
							line-height: 1.5rem;
						}
						::v-deep.el-date-editor .el-range__icon {
							color: #fff;
							line-height: 1.4rem;
						}
						::v-deep.el-date-editor .el-range__close-icon {
							color: #fff;
							line-height: 1.4rem;
						}
						::v-deep.el-date-editor .el-icon-date {
							line-height: 1;
						}
						.detail-table {
							::v-deep .el-table__body-wrapper::-webkit-scrollbar {
								height: 6px; // 纵向滚动条 必写
								width: 6px;
							}
						}
					}
				}

				// 查询样式
				.query-sty {
					padding: 15px 0;
					color: #fff;
					.search-sty {
						padding: 0px;
						margin-top: 10px;
						text-align: center;
						border-radius: 4px;
						width:60px;
						line-height: 30px;
						float: right;
						color: #fff;
						cursor: pointer;
						border-color: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%) !important;
						background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
					}

					.derive-sty {
						padding: 0 10px;
						text-align: center;
						border-radius: 4px;
						line-height: 30px;
						float: right;
						color: #fff;
						cursor: pointer;
						border-color: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%) !important;
						background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
					}
					.bb {
						font-family: PingFangSC-Regular;
						font-size: 14px;
						color: #ffffff;
						text-align: right;
						font-weight: 400;
					}
					.aa {
						margin-right: 20px;
					}
					.demo-input-suffix {
						margin-top: 10px;
						display: flex;
						justify-content: left;
						align-items: center;
					}
				}

				.tab-list-sty {
					@include flex(1);
					padding-bottom: 20px;
					overflow: auto;
				}
			}
		}
	}
	::v-deep.el-input-number {
		width: 5.25rem;
	}
	::v-deep.el-input__inner:hover,
	.el-select:hover .el-input__inner {
		border: 1px solid #fff;
	}
	::v-deep.el-input__inner {
		height: 2rem;
		background: rgba(24, 47, 90, 0.1);
		color: #fff;
		border: 1px solid #fff;
	}
	::v-deep.el-select .el-input .el-select__caret {
		color: #fff;
	}
	::v-deep input::-webkit-input-placeholder {
		color: #fff;
	}
	.dialog-footer {
		.el-button--primary.is-plain {
			color: #fff;
			border-color: #0bbbff;
			background-color: rgba($color: #0bbbff, $alpha: 0.1);
		}
	}
	::v-deep.el-table .warm-row .cell {
		color: #eec80b !important;
	}
	.bcd ::v-deep .el-table__body tr:hover {
		font-family: PingFangSC-Medium;
		font-size: 14px;
		color: #1ef8f2;
		line-height: 16px;
		font-weight: 500;
	}
	.gates-open-sty {
		background: url(~assets/images/WaterDisastersImage/zhamen-kaiqi.png) no-repeat center center;
		background-size: 100% 100%;
	}
	.water-gate-sty {
		> .area-style {
			border-left: 1px solid rgba($color: #0bbbff, $alpha: 0.5);
			background: none;
			display: flex;
			justify-content: flex-start;
			> p:nth-child(1) {
				padding: 15px 15px 0 40px;
				> span {
					font-family: PingFangSC-Regular;
					font-size: 16px;
					color: #ffffff;
					font-weight: 400;
					margin-right: 10px;
				}
				> i {
					line-height: 30px;
					margin: 0 10px 0 0;
					color: #fff;
				}
				::v-deep.el-input__inner {
					background: none;
					border: 0.0625rem solid #fff;
				}
				::v-deep.el-input-number__decrease {
					border-left: 0.0625rem solid #fff;
					background: none;
					> i {
						color: #fff;
					}
				}
				::v-deep.el-input-number__increase {
					height: 16px;
					border-left: 0.0625rem solid #fff;
					background: none;
					> i {
						color: #fff;
					}
				}
			}
			> div:nth-child(2) {
				padding: 15px 15px 0 40px;
				font-size: 16px;
				color: #fff;
				> span {
					font-family: PingFangSC-Medium;
					font-size: 24px;
					color: #1cfff5;
					letter-spacing: 0;
					text-align: right;
					line-height: 24px;
					font-weight: 500;
					padding: 0;
					margin: 0;
				}
			}
		}
	}
}
// 弹窗底部按钮样式
.dialog-footer {
	position: absolute;
	right: 20px;
	bottom: 15px;
	.el-button--primary.is-plain {
		color: #fff;
		border-color: #0bbbff;
		background-color: rgba($color: #0bbbff, $alpha: 0.1);
	}
}
::v-deep.primary-dialog .el-dialog .el-table th {
	color: #1cfff5;
}
::v-deep.el-table .ascending .sort-caret.ascending {
	border-bottom-color: #1cfff5;
}
::v-deep.el-table .descending .sort-caret.descending {
	border-top-color: #1cfff5;
}
::v-deep.mineClass .el-input__suffix {
	opacity: 0.7;
	font-family: PingFangSC-Regular;
	font-size: 14px;
	color: #ffffff;
	font-weight: 400;
}
::v-deep.el-date-editor .el-range-input {
	background: none;
	color: #fff;
}
::v-deep.el-date-editor .el-range-separator {
	color: #fff;
	line-height: 1.5rem;
}
::v-deep.el-date-editor .el-range__icon {
	color: #fff;
	line-height: 1.4rem;
}
::v-deep.el-date-editor .el-range__close-icon {
	color: #fff;
	line-height: 1.4rem;
}
::v-deep.el-date-editor .el-icon-date {
	line-height: 1;
}
.detail-table {
	::v-deep .el-table__body-wrapper::-webkit-scrollbar {
		height: 6px; // 纵向滚动条 必写
		width: 6px;
	}
}
.tb-top-panel {
	margin-top: 10px;
	color: #fff;
	display: flex;
	align-items: center;
	i {
		float: left;
	}
	p {
		flex: 1;
		font-size: 15px;
		margin-left: 10px;
		background-color: rgba(255, 255, 255, 0.1);
		padding: 8px 10px;
		border-radius: 4px;
	}
	span {
		color: #7ccfff;
		font-size: 20px;
	}
	.laba {
		width: 24px;
		height: 24px;
		background: url(~assets/images/laba.png) no-repeat;
		background-size: 100%;
	}
}

::v-deep .is-checked .el-checkbox__label {
	color: #70adf8;
}
::v-deep .el-radio {
	margin-right: 15px;
}
::v-deep .el-radio__label {
	color: #fff;
}
::v-deep .is-checked .el-checkbox__inner {
	border-color: #70adf8;
	color: #70adf8;
	&::after {
		border-color: #70adf8;
	}
}
</style>

<style lang="scss" scoped>
.water-supply-select-panel {
	display: flex;
	margin-bottom: 20px;
}
.water-supply-select-btn {
	padding: 10px 20px;
	margin-right: 20px;
	color: #c9c8c8;
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
