<template>
	<count-wrap :title="title" class="planning-count">
		<el-row class="has-count">
			<el-col :span="6" class="fs24">
				<p>总投资</p>
				<p>
					<span class="blue fs36">{{ statData.total }}</span
					>亿元
				</p>
			</el-col>
			<el-col :span="6" class="fs24">
				<p>{{ title }}投资</p>
				<p>
					<span class="blue fs36">{{ statData.invest }}</span
					>亿元
				</p>
			</el-col>
			<el-col :span="6" class="fs24">
				<p>已完成投资</p>
				<p>
					<span class="blue fs36">{{ statData.finish }}</span
					>亿元
				</p>
			</el-col>
			<el-col :span="6" class="fs24">
				<p>结转投资</p>
				<p>
					<span class="blue fs36">{{ statData.carryOver }}</span
					>亿元
				</p>
			</el-col>
		</el-row>
	</count-wrap>
</template>
<script>
import CountWrap from '../components/CountWrap.vue';
import { planDigitizationApi } from '@/api/plan_digitization/index.js';
import { mapGetters } from 'vuex';
/**
 * @param {Number|String} val
 */
function formatterVal(val) {
	if (!val) return '-';
	else {
		if (typeof val == 'string') return parseFloat(val).toFixed(2);
		else if (typeof val == 'number') return val.toFixed(2);
	}
}
export default {
	name: 'PlanningCount',
	components: { CountWrap },
	data() {
		return {
			title: '-',
			id: '',
			statData: { total: 0, invest: 0, finish: 0, carryOver: 0 }
		};
	},

	computed: {
		...mapGetters(['currentPlanning'])
	},
	watch: {
		currentPlanning: {
			immediate: true,
			handler({ id, title }) {
				this.title = title;
				this.id = id;
				this.getStat();
			}
		}
	},
	methods: {
		getStat() {
			planDigitizationApi.getStatsCwindowOfDevPlan({ ptId: this.id }).then(res => {
				this.statData.total = formatterVal(res.data.amountInvestTotal / 10000);
				this.statData.invest = formatterVal(res.data.amountInvestDevelop / 10000);
				this.statData.finish = formatterVal(res.data.amountInvested / 10000);
				this.statData.carryOver = formatterVal(res.data.amountInvestIndirect / 10000);
			});
		}
	}
};
</script>
<style lang="scss" scoped>
@import './style/CountCard';
.river-count .has-count {
	height: 100%;
	.el-col {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		height: 100%;
	}

	p {
		text-align: center;
	}
}
</style>
