<template>
	<div class="hydrological-forecast-card">
		<page-pane title="水文预报">
			<template #append>
				<i :class="point ? 'icon-yinzhang-active' : 'icon-yinzhang-1'" @click="onPointClick" />
				<i v-if="typeTab == 0" class="icon-liebiao" @click="onDialogShow" />
			</template>
			<div style="height:100%;margin-top:0.5rem">
				<CapsuleTabs v-model="typeTab" :tabs="['河道', '潮位']" @change="onTabChange" />
				<el-table
					v-loading="dataLoading"
					:data="displayData"
					stripe
					element-loading-text="拼命加载中"
					element-loading-spinner="el-icon-loading"
					element-loading-background="rgba(0, 0, 0, 0.8)"
					:cell-style="{ fontSize: '15px' }"
					height="calc(100% - 0.5rem - 24px)"
					:header-cell-style="{ color: '#1cfff5', fontSize: '18px' }"
				>
					<el-table-column label="序号" type="index" width="60" />
					<el-table-column label="站点名称" prop="name" />
					<el-table-column label="时间" prop="releaseTime" />
					<el-table-column label="状态">
						<template #default="{row}">
							<span :style="{ color: row.status[1] }">{{ row.status[0] }}</span>
						</template>
					</el-table-column>
				</el-table>
			</div>
		</page-pane>
		<el-dialog
			top="10vh"
			width="1200px"
			destroy-on-close
			append-to-body
			:visible.sync="visible"
			:close-on-click-modal="false"
			class="primary-dialog detail-table"
		>
			<div class="primary-dialog__title" slot="title">
				<p class="primary-dialog__title--label">详表</p>
			</div>
			<div class="primary--dialog__content">
				<el-row :gutter="20" class="query-sty">
					<el-col :span="6">
						<div class="demo-input-suffix">
							<span style="width:80px">类型:</span>
							<el-select v-model="type" size="small">
								<el-option label="全部" value=""></el-option>
							</el-select>
						</div>
					</el-col>
					<el-col :span="6">
						<div class="demo-input-suffix">
							<span style="width:80px">关键字:</span>
							<el-input v-model="keyword" size="small" @input="getList" />
						</div>
					</el-col>
				</el-row>
				<div class="tab-list-sty" style="padding-bottom: 20px">
					<el-table
						v-loading="loading"
						element-loading-text="拼命加载中"
						element-loading-spinner="el-icon-loading"
						element-loading-background="rgba(0, 0, 0, 0.8)"
						ref="travelTable"
						:data="tableData"
						:height="500"
						stripe
					>
						<el-table-column label="序号" type="index" />
						<el-table-column label="测站" prop="stName" />
						<el-table-column label="预报发布时间" prop="releaseTime" />
						<el-table-column label="洪峰水位(m)" prop="peakLevel" />
						<el-table-column label="洪峰水位出现时间" prop="peakLevelTime" />
						<el-table-column label="洪峰流量" prop="peakFlow" />
						<el-table-column label="洪峰流量出现时间" prop="peakFlowTime" />
					</el-table>
				</div>
			</div>
		</el-dialog>
	</div>
</template>

<script>
import { getHydrologyData, getTideData, getHydrologyForecast } from "@/api/micro-service/hydrology-digitization/";
import { PagePane } from "../components/PagePane";
import CapsuleTabs from "../components/CapsuleTabs.vue";
import { mapGetters } from "vuex";
export default {
	name: "HydrologicalForecastCard",
	components: { PagePane, CapsuleTabs },
	data() {
		return {
			dataLoading: false,
			point: false,
			typeTab: 0,
			hydrologyData: [],
			tideData: [],
			displayData: [],

			keyword: "",
			type: "",
			tableData: [],
			visible: false,
			loading: false
		};
	},
	mounted() {
		this.getData(0);
	},
	computed: {
		...mapGetters(["ScreenMap"])
	},
	methods: {
		/**
		 * tab change event
		 */
		onTabChange(tab) {
			if (this[["hydrologyData", "tideData"][tab]].length == 0) {
				this.getData(tab);
			} else {
				this.displayData = this[["hydrologyData", "tideData"][tab]];
			}
		},
		/**
		 * request both hydrology and tide data and store to their variables for display
		 */
		getData(tab) {
			this.dataLoading = true;
			const fetchAllData = Promise.all([getHydrologyData(), getTideData()]);
			fetchAllData.then(([res1, res2]) => {
				this.dataLoading = false;
				if (res1.code == 0 && res2.code == 0) {
					this.hydrologyData = res1.data.map(item => ({
						...item,
						name: item.stName,
						releaseTime: item.releaseTime,
						status: [
							{
								RR: ["正常", "超警戒", "超保证"],
								ZZ: ["正常", "超汛限", "超防洪高"]
							}[item.stType][item.warnState],
							["#fff", "#d88802", "#da2502"][item.warnState]
						],
						lat: item.stLat,
						lng: item.stLong
					}));
					this.tideData = res2.data.manualValueList.map(item => ({
						...item,
						name: item.stName,
						releaseTime: res2.data.releaseTime,
						status: [
							["正常", "蓝色预报", "黄色预报", "橙色预报", "红色预报"][item.warnState],
							["#fff", "#12a4ff", "#d88802", "#e84100", "#da2502"][item.warnState]
						],
						lat: item.stLat,
						lng: item.stLong
					}));
					this.displayData = this[["hydrologyData", "tideData"][tab]];
				}
			});
		},
		/**
		 * on point icon click
		 */
		onPointClick() {
			this.point = !this.point;
			const style = {
				scale: 0.7,
				anchor: [0.5, 1],
				anchorYUnits: "fraction",
				src: require("@/assets/images/hewang-zheng-chang.png")
			};
			if (this.point) {
				const pointList = this.hydrologyData.concat(this.tideData).map(item => ({
					lng: item.lng,
					lat: item.lat,
					name: item.stName,
					props: { 时间: item.releaseTime }
				}));
				this.ScreenMap.drawClusterPoint(pointList, style, "hydrologyForecastLayer");
				this.$emit("legend-change", "潮位", {});
				this.$emit("legend-change", "河道", {});
			} else {
				this.ScreenMap.drawClusterPoint([], {}, "hydrologyForecastLayer");
				this.$emit("legend-change", "潮位", {});
				this.$emit("legend-change", "河道", {});
			}
		},
		/**
		 * on dialog icon click
		 */
		onDialogShow() {
			this.visible = true;
			this.getList();
		},
		getList() {
			this.loading = true;
			getHydrologyForecast({}).then(res => {
				this.loading = false;
				if (res.code == 0) {
					this.tableData = res.data.list;
				}
			});
		}
	}
};
</script>

<style></style>
