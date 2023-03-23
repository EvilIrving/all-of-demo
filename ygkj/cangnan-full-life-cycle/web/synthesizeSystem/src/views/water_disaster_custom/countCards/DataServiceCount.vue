<template>
	<count-wrap title="数据服务" class="short-count">
		<div class="has-count">
			<el-col :span="12" class="fs24">
				<p>
					本月数据API调用次数<span class="green fs36"> {{ apiMonthTotalInvokeNumber }} </span> 个
				</p>
			</el-col>
			<el-col :span="12" class="fs24">
				<p>
					最多访问接口{{ apiInvokeMaxNumber }}<span class="green fs36"> {{ apiInvokeMax }} </span> km
				</p>
			</el-col>
		</div>
	</count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { getDataService4C } from "@/api/data-management/index.js";
export default {
	name: "DataServiceCount",
	data() {
		return {
			apiMonthTotalInvokeNumber: 9,
			apiInvokeMax: 100,
			apiInvokeMaxNumber: "业务数据查询"
		};
	},
	components: { CountWrap },
	mounted() {},
	methods: {
		getDataService4C() {
			getDataService4C().then(res => {
				if (res.code == 0) {
					const { apiMonthTotalInvokeNumber, apiInvokeMax, apiInvokeMaxNumber } = res.data;
					this.apiMonthTotalInvokeNumber = apiMonthTotalInvokeNumber;
					this.apiInvokeMax = apiInvokeMax;
					this.apiInvokeMaxNumber = apiInvokeMaxNumber;
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
