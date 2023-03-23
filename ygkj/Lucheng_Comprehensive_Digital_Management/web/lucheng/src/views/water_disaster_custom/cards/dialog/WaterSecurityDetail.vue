<template>
	<div class="primary-dialog water-security-table">
		<DataDetailDialog
			:loading="exportLoading"
			:visible.sync="visible"
			:fullscreen="fullscreen"
			:before-close="handleClose"
			title="水源保障"
			@changeFullScreen="changeFullScreen"
		>
			<template #stat>
				<StatisticsBox icon="icon-section-flow-normal" title="最大供水能力">
					<span>{{ statistic.maxSupply.name }}</span
					>&nbsp;&nbsp;{{ statistic.maxSupply.supplyAbility }}天
				</StatisticsBox>
				<StatisticsBox icon="icon-section-flow-normal" title="最小供水能力">
					<span>{{ statistic.minSupply.name }}</span
					>&nbsp;&nbsp;{{ statistic.minSupply.supplyAbility }}天
				</StatisticsBox>
			</template>
			<template #query>
				<LabelFormItem label="关键词">
					<el-input v-model="watSrcName" placeholder="请输入水源地内容" style="width:180px" @input="getList" />
				</LabelFormItem>
				<DataDetailButton right @click="exportTable"> 导 出</DataDetailButton>
			</template>
			<el-table
				ref="travelTable"
				:data="tableData"
				height="100%"
				stripe
				v-loading="loading"
				element-loading-text="拼命加载中"
				element-loading-spinner="el-icon-loading"
				element-loading-background="rgba(0, 0, 0, 0.8)"
				@sort-change="sortChange($event, getList)"
			>
				<el-table-column type="index" label="序号" width="55" />
				<el-table-column :formatter="columnFormatter" prop="name" label="水源地名称" />
				<el-table-column :formatter="columnFormatter" :sortable="'custom'" prop="supportWater" label="水源可用水量" />
				<el-table-column :formatter="columnFormatter" :sortable="'custom'" prop="dayWatSupply" label="日平均供水量" />
				<el-table-column
					:formatter="columnFormatter"
					sortable="custom"
					prop="supplyAbility"
					width="160px"
					label="供水能力(天)"
				/>
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
import { tableMixin } from "./mixins";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { exportRsvrWatSrc } from "@/api/micro-service/project-manage/export";
import { getStats4rsvrWatSrcTable } from "@/api/micro-service/project-manage";

export default {
	name: "WaterSecurityDetail",
	props: { visible: { type: Boolean, default: false } },
	components: { DataDetailDialog, StatisticsBox, LabelFormItem, DataDetailButton },
	mixins: [tableMixin],
	data() {
		return {
			watSrcName: "",
			tableData: [],
			statistic: { maxSupply: {}, minSupply: {} },
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
			const opt = { watSrcName: this.watSrcName };
			await exportRsvrWatSrc(opt);
			this.exportLoading = false;
		},
		async getList() {
			this.loading = true;
			const opt = { watSrcName: this.watSrcName };
			await waterResourceApi.waterGuaranteeRsvrWatSrcTable(opt).then(res => {
				res.code == 0 ? (this.tableData = res.data) : [];
			});
			this.getStat(opt);
			this.loading = false;
		},
		async getStat(opt) {
			await getStats4rsvrWatSrcTable(opt).then(res => {
				if (res.code == 0) {
					const { maxSupply = { name: "", supplyAbility: "" }, minSupply = { name: "", supplyAbility: "" } } = res.data;
					this.statistic = { maxSupply, minSupply };
				}
			});
		}
	}
};
</script>
