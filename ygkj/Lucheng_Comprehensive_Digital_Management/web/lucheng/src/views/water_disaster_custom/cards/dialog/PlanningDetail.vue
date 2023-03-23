<template>
	<div class="primary-dialog sluice-detail-table">
		<DataDetailDialog
			:visible.sync="visible"
			:fullscreen="fullscreen"
			:before-close="handleClose"
			:title="title"
			@changeFullScreen="changeFullscreen"
		>
			<template #stat>
				<StatisticsBox v-for="(item, index) in stats" :key="index" :icon="item.icon" :title="item.label">
					<span>{{ item.num }}</span
					>{{ item.unit }}
				</StatisticsBox>
			</template>
			<template #query>
				<LabelFormItem label="行政区划:">
					<el-select v-model="areaCode" size="small" clearable placeholder="全部" @change="getList">
						<el-option v-for="item in areaList" :key="item.value" :label="item.adnm" :value="item.adcd" />
					</el-select>
				</LabelFormItem>
				<LabelFormItem label="起止年份:">
					<el-date-picker
						v-model="startYear"
						format="yyyy"
						value-format="yyyy"
						type="year"
						placeholder="开始"
						clearable
						:picker-options="{
							disabledDate: time => (endYear ? time.getFullYear() > endYear : null)
						}"
						@change="getList"
					/>
					至
					<el-date-picker
						v-model="endYear"
						format="yyyy"
						value-format="yyyy"
						type="year"
						placeholder="结束"
						clearable
						:picker-options="{
							disabledDate: time => (startYear ? time.getFullYear() < startYear : null)
						}"
						@change="getList"
					/>
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
				<el-table-column prop="projName" label="项目名称" show-overflow-tooltip />
				<el-table-column prop="projDuration" label="起止年份" width="120" />
				<el-table-column prop="constructContent" label="主要建设内容" show-overflow-tooltip />
				<el-table-column prop="amountInvestTotal" label="总投资(万元)" width="150" />
				<el-table-column prop="amountInvestDevelop" :label="`${title || ''}投资(万元)`" width="160" />
				<el-table-column prop="amountInvested" label="已完成投资(万元)" width="150" />
				<el-table-column prop="amountInvestIndirect" label="结转总投资(万元)" width="150" />
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
import { tableMixin, filterMixin } from "./mixins.js";
import { getPageDevPlanProj, getDevPlanProjStats } from "@/api/micro-service/plan-digitization";
import { exportDdvPlanProj } from "@/api/micro-service/project-manage/export";

export default {
	name: "PlanningDetail",
	props: {
		visible: {
			type: Boolean,
			default: false
		},
		type: {
			type: String,
			default: "闸站"
		},
		title: { type: String, default: "规划专题" },
		ptId: { type: String }
	},
	mixins: [tableMixin, filterMixin],
	components: { DataDetailDialog, StatisticsBox, LabelFormItem, DataDetailButton },
	data() {
		return {
			stats: [
				{
					label: "项目条数",
					num: "",
					icon: "icon-planning-total",
					unit: "条"
				},
				{ label: "总投资", num: "", icon: "icon-invest-total", unit: "万元" },
				{
					label: "",
					num: "",
					icon: "icon-current-invest",
					auto: true,
					unit: "万元"
				},
				{
					label: "已完成投资",
					num: "",
					icon: "icon-finish-invest",
					unit: "万元"
				},
				{
					label: "结转投资",
					num: "",
					icon: "icon-indirect-invest",
					unit: "万元"
				}
			],
			startYear: "",
			endYear: "",
			projName: "",
			tableData: [],
			fullscreen: false,
			exportLoading: false,
			loading: false
		};
	},
	watch: {
		visible(val) {
			if (val) this.getList();
		}
	},
	methods: {
		changeFullscreen() {
			this.fullscreen = !this.fullscreen;
			this.$nextTick(() => {
				this.$refs.multipleTable.doLayout();
			});
		},
		async exportTable() {
			this.exportLoading = true;
			const { title, ptId, areaCode, startYear, endYear, projName } = this;
			const opt = { title, ptId, areaCode, startYear, endYear, projName };
			await exportDdvPlanProj(opt);
			this.exportLoading = false;
		},
		async getList() {
			this.loading = true;
			const { title, ptId, areaCode, startYear, endYear, projName } = this;
			const opt = { title, ptId, areaCode, startYear, endYear, projName };
			await getPageDevPlanProj(opt).then(res => {
				if (res.code == 0) {
					this.tableData = res.data.list;
				}
			});
			this.getStat(opt);
			this.loading = false;
		},
		async getStat(opt) {
			await getDevPlanProjStats(opt).then(res => {
				const data = res.data;
				this.stats = this.stats.map(item => {
					if (item.auto) {
						return {
							...item,
							label: `${opt.title}投资`,
							num: data.amountInvestDevelop | "-"
						};
					}
					if (item.label == "总投资") {
						return { ...item, num: data.amountInvestTotal | "-" };
					}
					if (item.label == "项目条数") {
						return { ...item, num: data.projectNum | "-" };
					}
					if (item.label == "已完成投资") {
						return { ...item, num: data.amountInvested | "-" };
					}
					if (item.label == "结转投资") {
						return { ...item, num: data.amountInvestIndirect | "-" };
					}
					return item;
				});
			});
		},
		handleClose() {
			this.$emit("close");
			this.$emit("update:visible", false);
		}
	}
};
</script>
