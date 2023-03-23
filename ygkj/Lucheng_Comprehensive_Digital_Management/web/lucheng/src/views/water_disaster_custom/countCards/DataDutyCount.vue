<template>
	<count-wrap title="数据责任" class="short-count">
		<div class="has-count">
			<el-col :span="12" class="fs24">
				<p>
					数据责任人确认率<span class="green fs36"> {{ dutyData.tableDutyManCount }} </span> |<span class="green fs36">
						{{ dutyData.rate * 100 }}%
					</span>
				</p>
			</el-col>
			<el-col :span="12" class="fs24">
				<p>
					无责任人表数量 <span class="green fs36"> {{ dutyData.tableUnDutyCount }} </span> 个
				</p>
			</el-col>
		</div>
	</count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { getDuty4C } from "@/api/micro-service/data-management/";
export default {
	name: "DataDutyCount",
	data() {
		return {
			dutyData: { rate: 0, tableDutyManCount: 0, tableUnDutyCount: 0 }
		};
	},
	components: { CountWrap },
	mounted() {
		this.getDuty4C();
	},
	methods: {
		getDuty4C() {
			getDuty4C().then(res => {
				if (res.code == 0) {
					this.dutyData = res.data;
				}
			});
		}
	}
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.short-count .has-count {
	height: 100%;
	display: flex;
	justify-content: space-between;
	> div {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		height: 100%;
	}
}
</style>
