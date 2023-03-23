<template>
	<div class="primary-dialog sluice-detail-table">
		<DataDetailDialog
			:visible.sync="visible"
			:loading="exportLoading"
			:fullscreen="fullscreen"
			:before-close="handleClose"
			title="闸站详表"
			@changeFullScreen="changeFullscreen"
		>
			<template #stat>
				<StatisticsBox title="闸站总数" icon="icon-hewang-shuliang-tongji">
					<span> {{ statisticObj.gateNum }} </span> 个
				</StatisticsBox>
				<StatisticsBox title="总装机流量" icon="icon-chao-xunxian">
					<span>{{ statisticObj.totalInsPow }} </span> m³/s
				</StatisticsBox>
			</template>
			<template #query>
				<LabelFormItem label="行政区划:">
					<el-select v-model="areaVal" size="small" clearable placeholder="全部" @change="getList">
						<el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adcd" />
					</el-select>
				</LabelFormItem>
				<LabelFormItem label="规模：">
					<el-select @change="getList" v-model="engScal" size="small" clearable>
						<el-option label="中型" value="中型" />
						<el-option label="小(1)型" value="小（Ⅰ）型" />
					</el-select>
				</LabelFormItem>
				<LabelFormItem label="关键字：">
					<el-input v-model="searchVal" placeholder="请输入内容" @input="getList" style="width:180px" />
				</LabelFormItem>
				<DataDetailButton @click="exportExcel" style="margin-left: auto">导 出</DataDetailButton>
			</template>

			<el-table
				ref="multipleTable"
				:data="tableData"
				stripe
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
				<el-table-column :show-overflow-tooltip="true" sortable prop="gateName" label="闸站名称" />
				<el-table-column :show-overflow-tooltip="true" prop="managementLevel" label="管理层级" />
				<el-table-column :show-overflow-tooltip="true" prop="locCity" label="所在市" />
				<el-table-column :show-overflow-tooltip="true" prop="locCounty" label="所在县" />
				<el-table-column :show-overflow-tooltip="true" sortable prop="engScal" label="闸站规模" />
				<el-table-column :show-overflow-tooltip="true" sortable prop="maxFlow" label="过闸流量(m³/s)" />
				<el-table-column :show-overflow-tooltip="true" sortable prop="singleInsPow" label="装机功率(kW)" />
			</el-table>
		</DataDetailDialog>
	</div>
</template>

<script>
import DataDetailDialog from "../../dialog/DetailsCommonDialog/components/DataDetailDialog.vue";
import StatisticsBox from "../../dialog/DetailsCommonDialog/components/StatisticsBox.vue";
import StatisticsTabs from "../../dialog/DetailsCommonDialog/components/StatisticsTab.vue";
import LabelFormItem from "../../dialog/DetailsCommonDialog/components/LabelFormItem.vue";
import DataDetailButton from "../../dialog/DetailsCommonDialog/components/DataDetailButton.vue";
import { exportGateProject } from "@/api/micro-service/project-manage/export";
import { hydraulicApi } from "@/api/hydraulic_amap";
import { disasterPreventionApi } from "@/api/RightSildePanel";
export default {
	name: "SluiceStationDetail",
	props: {
		visible: {
			type: Boolean,
			default: false
		},
		type: {
			type: String,
			default: "闸站"
		}
	},
	components: { DataDetailDialog, StatisticsBox, StatisticsTabs, LabelFormItem, DataDetailButton },
	data() {
		return {
			// 导出Loading
			exportLoading: false,
			statisticObj: {
				gateNum: "",
				totalInsPow: ""
			},
			areaVal: "",
			//行政区划
			administration: [],
			engScal: "",
			searchVal: "",
			tableData: [],
			loading: false,
			orderColumn: "",
			ascOrDesc: "",
			fullscreen: false
		};
	},
	mounted() {
		this.getArea();
		this.getList();
		this.getStats();
	},
	methods: {
		changeFullscreen() {
			this.fullscreen = !this.fullscreen;
			this.$nextTick(() => {
				this.$refs.multipleTable.doLayout();
			});
		},
		// 导出
		async exportExcel() {
			this.exportLoading = true;
			const obj = {
				type: "闸站",
				areaCode: this.areaVal,
				projectName: this.searchVal,
				level: this.engScal
			};
			exportGateProject(obj).finally(() => {
				this.exportLoading = false;
			});
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
		},
		async getList() {
			const opt = {
				type: "闸站",
				areaCode: this.areaVal,
				projectName: this.searchVal,
				level: this.engScal
			};
			await hydraulicApi.projectsDetailList(opt).then(res => {
				if (res.code == 0) {
					this.tableData = res.data;
				}
			});
		},
		handleClose() {
			this.$emit("close");
		},
		async getStats() {
			const opt = { type: "闸站" };
			await hydraulicApi.stats4ProjectList(opt).then(res => {
				if (res.code == 0) {
					this.statisticObj = res.data;
				}
			});
		}
	}
};
</script>
