<template>
	<div class="primary-dialog irrigate-table">
		<DataDetailDialog
			:loading="exportLoading"
			:visible.sync="visible"
			:fullscreen="fullscreen"
			:before-close="handleClose"
			title="灌区详表"
			@changeFullScreen="changeFullscreen"
		>
			<template #stat>
				<StatisticsBox title="灌区总数" icon="icon-hewang-shuliang-tongji">
					<span> {{ statisticObj.irrNum }} </span> 个
				</StatisticsBox>
				<StatisticsBox title="设计灌溉面积" icon="icon-chao-xunxian">
					<span>{{ statisticObj.desIrrArea }} </span> k㎡
				</StatisticsBox>
				<StatisticsBox title="有效灌溉面积" icon="icon-chao-xunxian">
					<span>{{ statisticObj.effIrrArea }} </span> k㎡
				</StatisticsBox>
			</template>
			<!-- 内容区域 -->
			<template #query>
				<LabelFormItem label="行政区划:">
					<el-select v-model="areaVal" size="small" clearable placeholder="全部" @change="getList">
						<el-option v-for="item in areaList" :key="item.value" :label="item.adnm" :value="item.adcd"></el-option>
					</el-select>
				</LabelFormItem>
				<LabelFormItem label="工程规模:">
					<el-select @change="getList" v-model="engScal" size="small" clearable>
						<el-option label="大型" value="1,2" />
						<el-option label="中型" value="3" />
						<el-option label="小型" value="4,5" />
					</el-select>
				</LabelFormItem>
				<LabelFormItem label="流域:">
					<el-select @change="getList" v-model="basVal" size="small" clearable>
						<el-option label="飞云江" value="飞云江" />
						<el-option label="瓯江" value="瓯江" />
						<el-option label="鳌江" value="鳌江" />
					</el-select>
				</LabelFormItem>
				<DataDetailButton right @click="exportExcel">导 出</DataDetailButton>
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
				<el-table-column :show-overflow-tooltip="true" sortable prop="irrName" label="灌区名称" width="160" />
				<el-table-column :show-overflow-tooltip="true" prop="engScal" label="工程规模" width="100" />
				<el-table-column
					:show-overflow-tooltip="true"
					prop="desIrrArea"
					label="设计灌溉面积(k㎡)"
					width="180"
					sortable
				/>
				<el-table-column
					:show-overflow-tooltip="true"
					prop="effIrrArea"
					label="有效灌溉面积(k㎡)"
					width="180"
					sortable
				/>
				<el-table-column :show-overflow-tooltip="true" prop="manUnit" label="管理单位" width="160" />
				<el-table-column :show-overflow-tooltip="true" prop="adnm" label="行政区划" width="100" />
				<el-table-column :show-overflow-tooltip="true" prop="bas" label="流域" width="80" />
				<el-table-column :show-overflow-tooltip="true" prop="managementLevel" label="管理层级" />
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
import { hydraulicApi } from "@/api/hydraulic_amap";
import { filterMixin } from "./mixins.js";
import { exportIrrProject } from "@/api/micro-service/project-manage/export";
export default {
	name: "IrrigateDetail",
	props: {
		visible: { type: Boolean, default: false }
	},
	components: { DataDetailDialog, StatisticsBox, LabelFormItem, DataDetailButton },
	mixins: [filterMixin],
	data() {
		return {
			// 导出Loading
			exportLoading: false,
			statisticObj: {
				irrNum: "",
				effIrrArea: "",
				desIrrArea: ""
			},
			areaVal: "",
			//行政区划
			engScal: "",
			searchVal: "",
			tableData: [],
			loading: false,
			orderColumn: "",
			ascOrDesc: "",
			basVal: "",
			fullscreen: false
		};
	},
	computed: {},
	mounted() {
		this.getList();
		this.getStats();
	},
	methods: {
		changeFullscreen() {
			this.fullscreen = !this.fullscreen;
			this.$refs.multipleTable.doLayout();
		},
		// 导出
		async exportExcel() {
			this.exportLoading = true;
			const obj = {
				type: "灌区",
				areaCode: this.areaVal,
				projectName: this.searchVal,
				bas: this.basVal,
				levels: this.engScal
			};
			exportIrrProject(obj);
			this.exportLoading = false;
		},
		async getList() {
			const opt = {
				type: "灌区",
				areaCode: this.areaVal,
				projectName: this.searchVal,
				bas: this.basVal,
				levels: this.engScal
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
			const opt = { type: "灌区" };
			await hydraulicApi.stats4ProjectList(opt).then(res => {
				if (res.code == 0) {
					this.statisticObj = res.data;
				}
			});
		}
	}
};
</script>
