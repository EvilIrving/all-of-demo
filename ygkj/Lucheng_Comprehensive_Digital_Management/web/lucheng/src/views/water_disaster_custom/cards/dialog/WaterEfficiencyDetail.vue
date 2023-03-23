<template>
	<div class="primary-dialog">
		<DataDetailDialog
			:visible.sync="visible"
			:loading="exportLoading"
			:fullscreen="fullscreen"
			:before-close="handleClose"
			title="用水效率"
			@changeFullScreen="changeFullscreen"
		>
			<template #stat>
				<StatisticsBox v-if="statData.wpgBest" icon="icon-water-efficiency-gdp" title="万元GDP用水量最优年份">
					<span>{{ statData.wpgBest.year }}年</span>&nbsp;|&nbsp;<span>{{ statData.wpgBest.watPerGdp.toFixed(2) }}</span
					>m³/万元
				</StatisticsBox>
				<StatisticsBox v-if="statData.wpgBest" icon="icon-water-efficiency-iav" title="万元工业增加值用水量最优年份">
					<span>{{ statData.wpgidBest.year }}年</span>&nbsp;|&nbsp;<span>{{
						statData.wpgidBest.wpgInduDelta.toFixed(2)
					}}</span
					>m³/万元
				</StatisticsBox>
			</template>
			<template #query>
				<DataDetailButton right @click="exportTable">导 出</DataDetailButton>
			</template>
			<el-table
				ref="multipleTable"
				:data="tableData"
				stripe
				height="100%"
				v-loading="loading"
				element-loading-text="拼命加载中"
				element-loading-spinner="el-icon-loading"
				element-loading-background="rgba(0, 0, 0, 0.8)"
			>
				<el-table-column prop="index" label="序号" width="55">
					<template slot-scope="scope">
						<span class="table-order">{{ scope.$index + 1 }}</span>
					</template>
				</el-table-column>
				<el-table-column v-bind="commonColumnProp" prop="year" label="年份" />
				<el-table-column v-bind="commonColumnProp" prop="adnm" label="行政区划" />
				<el-table-column v-bind="commonColumnProp" prop="watPerGdp" width="200" label="万元GDP用水量(m³/万元)" />
				<el-table-column v-bind="commonColumnProp" prop="wpgCompareZjHighestRate" width="120" label="相较浙江最高值" />
				<el-table-column v-bind="commonColumnProp" prop="wpgCompareZjLowestRate" width="120" label="相较浙江最低值" />
				<el-table-column v-bind="commonColumnProp" prop="wpgCompareZjAvgRate" width="120" label="相较浙江平均值" />
				<el-table-column v-bind="commonColumnProp" prop="wpgInduDelta" width="200" label="万元工业增加值(m³/万元)" />
				<el-table-column v-bind="commonColumnProp" prop="wpgidCompareZjRate" width="120" label="相较浙江省" />
				<el-table-column v-bind="commonColumnProp" prop="wpgidCompareCnAvgRate" width="120" label="相较全国平均值" />
				<el-table-column
					v-bind="commonColumnProp"
					prop="wpgidCompareCnHighestRate"
					width="120"
					label="相较全国最高值"
				/>
				<el-table-column v-bind="commonColumnProp" prop="wpgidCompareCnLowestRate" width="120" label="相较全国最低值" />
			</el-table>
		</DataDetailDialog>
	</div>
</template>

<script>
import { DataDetailDialog, StatisticsBox, DataDetailButton } from "../../dialog/DetailsCommonDialog/components";
import { getWatConEffectList, getStats4WatConEffectList } from "@/api/micro-service/wra";
import { exportWatConEffectList } from "@/api/micro-service/project-manage/export";
export default {
	name: "WaterEfficiencyDetail",
	props: { visible: { type: Boolean, default: false } },
	components: { DataDetailDialog, StatisticsBox, DataDetailButton },
	data() {
		this.commonColumnProp = {
			formatter: this.columnFormatter,
			"show-overflow-tooltip": true
		};
		return {
			statData: {},
			exportLoading: false,
			loading: false,
			tableData: [],
			fullscreen: false
		};
	},
	watch: {
		visible(val) {
			if (val) this.getList();
		}
	},
	methods: {
		/**
		 * 表格列数据格式化
		 * @param {number|string} value
		 */
		columnFormatter(row, column, value, index) {
			if (value) {
				if (typeof value == "number" && parseFloat(value) != parseInt(value)) {
					return value.toFixed(2);
				}
				if (typeof value == "string" && value.split(",").length > 1) {
					return parseInt(value.split(",")[0]).toFixed(1);
				}
				return value;
			} else {
				return "-";
			}
		},
		changeFullscreen() {
			this.fullscreen = !this.fullscreen;
			this.$nextTick(() => {
				this.$refs.multipleTable.doLayout();
			});
		},
		async exportTable() {
			this.exportLoading = true;
			await exportWatConEffectList();
			this.exportLoading = false;
		},
		handleClose() {
			this.$emit("update:visible", false);
		},
		async getList() {
			this.loading = true;
			await getWatConEffectList().then(res => {
				if (res.code == 0) {
					this.tableData = res.data;
				}
			});
			await getStats4WatConEffectList().then(res => {
				if (res.code == 0) {
					this.statData = res.data;
				}
			});
			this.loading = false;
		}
	}
};
</script>

<style></style>
