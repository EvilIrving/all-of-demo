<template>
	<div class="primary-dialog section-flow-detail">
		<DataDetailDialog
			:loading="exportLoading"
			:visible.sync="visible"
			:fullscreen="fullscreen"
			:before-close="handleClose"
			title="流域生态流量"
			@changeFullScreen="changeFullScreen"
		>
			<template #stat>
				<StatisticsBox icon="icon-section-flow-normal" title="正常断面数量">
					<span style="color:#19AF47">{{ statistic.normNum }}</span
					>个
				</StatisticsBox>
				<StatisticsBox icon="icon-section-flow-orange" title="橙色预警数量">
					<span style="color:#FF551F">{{ statistic.orangeWarnNum }}</span
					>个
				</StatisticsBox>
				<StatisticsBox icon="icon-section-flow-red" title="红色预警数量">
					<span style="color:#E80000">{{ statistic.redWarnNum }}</span
					>个
				</StatisticsBox>
			</template>
			<template #query>
				<LabelFormItem label="流域:">
					<el-select v-model="basin" size="small" clearable @change="getList">
						<el-option v-for="item in basinList" :key="item" :label="item" :value="item"></el-option>
					</el-select>
				</LabelFormItem>
				<LabelFormItem label="预警:">
					<el-select v-model="warnType" size="small" clearable @change="getList">
						<el-option label="橙色预警" value="1" />
						<el-option label="红色预警" value="2" />
					</el-select>
				</LabelFormItem>
				<LabelFormItem label="关键字:">
					<el-input v-model="sectName" placeholder="请输入断面名称" clearable @blur="getList" />
				</LabelFormItem>
				<DataDetailButton style="margin-left: auto" @click="exportTable">导 出</DataDetailButton>
			</template>
			<el-table
				v-loading="loading"
				ref="multipleTable"
				:data="tableData"
				height="100%"
				stripe
				element-loading-text="拼命加载中"
				element-loading-spinner="el-icon-loading"
				element-loading-background="rgba(0, 0, 0, 0.8)"
			>
				<el-table-column type="index" label="序号" width="55" />
				<!-- 暂时这样吧 懒得写好看了 -->
				<el-table-column v-bind="{ prop: 'sectName', label: '断面名称' }" />
				<el-table-column v-bind="{ prop: 'bas', label: '流域', sortable: true }" />
				<el-table-column v-bind="{ prop: 'flow', label: '实时流量(m³/s)', sortable: true }" />
				<el-table-column v-bind="{ prop: 'orangeFlow', label: '橙色预警指标(m³/s)', sortable: true }" />
				<el-table-column v-bind="{ prop: 'redFlow', label: '红色预警指标(m³/s)', sortable: true }" />
			</el-table>
		</DataDetailDialog>
	</div>
</template>

<script>
import {
	DataDetailDialog,
	StatisticsBox,
	LabelFormItem,
	DataDetailButton
} from "../../dialog/DetailsCommonDialog/components";
import { filterMixin } from "./mixins.js";
import { getSectFlowList, getStats4SectFlowList } from "@/api/micro-service/hydrology-digitization";
import { exportSectFlowList } from "@/api/micro-service/project-manage/export";
export default {
	name: "SectionFlowDetail",
	props: { visible: { type: Boolean, default: false } },
	components: { DataDetailDialog, StatisticsBox, LabelFormItem, DataDetailButton },
	mixins: [filterMixin],
	data() {
		return {
			warnType: "",
			sectName: "",
			tableData: [],
			statistic: {},
			exportLoading: false,
			loading: false,
			fullscreen: false
		};
	},
	watch: {
		visible(val) {
			if (val) this.getList();
		}
	},
	methods: {
		changeFullScreen() {
			this.fullscreen = !this.fullscreen;
			this.$refs.multipleTable.doLayout();
		},
		handleClose() {
			this.$emit("close");
			this.$emit("update:visible", false);
		},
		async exportTable() {
			this.exportLoading = true;
			const { basin, warnType, sectName } = this;
			const opt = { basin, warnType, sectName };
			await exportSectFlowList(opt);
			this.exportLoading = false;
		},
		async getList() {
			this.loading = true;
			const { basin, warnType, sectName } = this;
			const opt = { basin, warnType, sectName };
			await getSectFlowList(opt).then(res => {
				this.tableData = res.data;
			});
			this.getStat(opt);
			this.loading = false;
		},
		async getStat(opt) {
			getStats4SectFlowList(opt).then(res => {
				if (res.code == 0) {
					this.statistic = res.data;
				}
			});
		}
	}
};
</script>
