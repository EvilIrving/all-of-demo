<template>
	<div class="primary-dialog sluice-detail-table">
		<DataDetailDialog
			:visible.sync="visible"
			:fullscreen="fullscreen"
			:before-close="handleClose"
			title="数据汇聚"
			@changeFullScreen="changeFullscreen"
		>
			<template #stat-tabs>
				<StatisticsTabs :active.sync="activeTab" :tabs="['水情表', '雨情表', '工情表', '视频表']" @change="getList" />
			</template>
			<template #stat>
				<StatisticsBox title="站点总数" icon="icon-hewang-shuliang-tongji"> <span> 0 </span> 个 </StatisticsBox>
				<StatisticsBox title="在线数量" icon="icon-chao-xunxian"> <span>0 </span> 个 </StatisticsBox>
				<StatisticsBox title="离线数量" icon="icon-chao-xunxian"> <span>0 </span> 个 </StatisticsBox>
			</template>
			<template #query>
				<LabelFormItem label="行政区划:">
					<el-select v-model="areaCode" size="small" clearable placeholder="全部" @change="getList">
						<el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adcd" />
					</el-select>
				</LabelFormItem>
				<LabelFormItem label="站点状态:">
					<el-select v-model="status" size="small" clearable @change="getList">
						<el-option label="全部" value="全部" />
						<el-option label="在线" value="在线" />
						<el-option label="离线" value="离线" />
					</el-select>
				</LabelFormItem>
				<LabelFormItem label="关键字：">
					<el-input v-model="searchVal" placeholder="请输入内容" @input="getList" style="width:180px" />
				</LabelFormItem>
				<DataDetailButton right @click="getList">搜 索</DataDetailButton>
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
				<el-table-column type="index" label="序号" :index="indexMethod" width="55" />
				<el-table-column v-if="activeTab != '视频表'" show-overflow-tooltip prop="stName" label="名称" />
				<el-table-column v-else show-overflow-tooltip prop="cameraName" label="名称" />
				<el-table-column show-overflow-tooltip label="行政区划">
					<template #default="{row}">
						{{ getAreaName(row) }}
					</template>
				</el-table-column>
				<el-table-column show-overflow-tooltip prop="managementLevel" label="状态">
					<template #default="{row}">
						<span :style="{ color: row.online || row.status == 1 ? '#00c1ff' : '#be8609' }">{{
							row.online || row.status == 1 ? "在线" : "离线"
						}}</span>
					</template>
				</el-table-column>
				<el-table-column v-if="activeTab == '水情表'" show-overflow-tooltip prop="waterLevel" label="水位(m)" />
				<el-table-column v-if="activeTab == '雨情表'" show-overflow-tooltip prop="rainfall" label="最新雨量" />
				<template v-if="activeTab == '工情表'">
					<el-table-column show-overflow-tooltip prop="upz" label="闸上水位" />
					<el-table-column show-overflow-tooltip prop="dwz" label="闸下水位" />
					<el-table-column show-overflow-tooltip prop="flow" label="过闸流量" />
					<el-table-column show-overflow-tooltip prop="openGate" label="开度" />
				</template>
			</el-table>
			<template #bottom>
				<div class="bottom__pagination" v-if="activeTab == '视频表'">
					<el-pagination
						:current-page.sync="pageNum"
						:page-sizes="[10, 20, 30, 50]"
						:page-size.sync="pageSize"
						layout="total, sizes, prev, pager, next"
						:total="total"
						@size-change="getList"
						@current-change="getList"
					/>
				</div>
			</template>
		</DataDetailDialog>
	</div>
</template>

<script>
import { getWaterStations } from "@/api/micro-service/graph";
import {
	DataDetailDialog,
	StatisticsBox,
	StatisticsTabs,
	LabelFormItem,
	DataDetailButton
} from "../../dialog/DetailsCommonDialog/components";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { listCamera } from "@/api/micro-service/project-manage";
import { pageMixin } from "./mixins.js";

export default {
	name: "SluiceStationDetail",
	props: {
		visible: { type: Boolean, default: false }
	},
	mixins: [pageMixin],
	components: { DataDetailDialog, StatisticsBox, StatisticsTabs, LabelFormItem, DataDetailButton },
	data() {
		return {
			activeTab: "水情表",
			areaCode: "",
			status: "",
			searchVal: "",

			//行政区划
			administration: [],
			loading: false,
			tableData: [],
			fullscreen: false
		};
	},
	mounted() {
		this.getArea();
		this.getList();
	},
	methods: {
		changeFullscreen() {
			this.fullscreen = !this.fullscreen;
			this.$nextTick(() => {
				this.$refs.multipleTable.doLayout();
			});
		},
		getAreaName(row) {
			return row.adnm || this.administration.find(item => item.adcd == row.areaCode)?.adnm || "-";
		},
		/**
		 * 行政区划
		 */
		async getArea() {
			const obj = { areaCode: 3303, level: 3 };
			await disasterPreventionApi.getArea(obj).then(res => {
				if (res.code == 0) {
					this.administration = res.data;
				}
			});
		},
		async getList() {
			this.loading = true;
			const paramMap = {
				水情表: { stationType: "RR,ZZ", areaCode: this.areaCode },
				雨情表: { stationType: "PP", areaCode: this.areaCode },
				工情表: { stationType: "DD", areaCode: this.areaCode }
			};
			if (this.activeTab in paramMap) {
				await getWaterStations(paramMap[this.activeTab]).then(res => {
					if (res.code == 0) {
						this.tableData = res.data;
					}
				});
			} else if (this.activeTab == "视频表") {
				const { pageSize, pageNum } = this;
				await listCamera({ pageSize, pageNum }).then(res => {
					if (res.code == 0) {
						this.total = res.data.totalRows;
						this.tableData = res.data.list;
					}
				});
			}
			this.loading = false;
		},
		handleClose() {
			this.$emit("close");
			this.$emit("update:visible", false);
		}
	}
};
</script>
