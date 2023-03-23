<template>
	<count-wrap class="water-schedule-count">
		<div class="has-count flex">
			<CountItem title="水库报警" :count="countData.rsvrWarnNum" unit="座" color="yellow" />
			<CountItem title="水闸开启" :count="countData.wagaOpenNum" unit="个" color="blue" />
			<CountItem title="水文报警" :count="countData.riverWarnNum" unit="个" color="yellow" />
			<CountItem title="潮位报警" :count="countData.tideWarnNum" unit="个" color="yellow" />
			<CountItem float title="水库出库流量" :count="countData.rsvrOutflow" unit="万m³" color="blue" />
			<div class="setting">
				<span>过去:</span>
				<el-input-number v-model="hour" controls-position="right" :max="24" :min="0" @change="getData" />
				<span>小时</span>
			</div>
			<CountItem float title="水闸泄洪流量" :count="countData.wagaDrainage" unit="万m³" color="blue" />
		</div>
	</count-wrap>
</template>
<script>
const CountItem = {
	name: 'CountItem',
	functional: true,
	props: { title: String, count: Number, unit: String, color: String, float: Boolean },
	render(h, context) {
		let { props } = context;
		let count = props.count;
		if (props.float) count = Number.isNaN(Number(props.count)) ? '-' : Number(props.count).toFixed(1);
		return (
			<div class="count-item">
				<p>{props.title}</p>
				<p>
					<span class={['fs36', props.color]}>{count}</span>
					{props.unit}
				</p>
			</div>
		);
	}
};
import CountWrap from '../../water_disaster_custom/components/CountWrap.vue';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index.js';
import debounce from 'lodash/debounce';
import { mapGetters } from 'vuex';
export default {
	name: 'ScheduleCount',
	components: { CountWrap, CountItem },
	data() {
		return {
			hour: 0,
			countData: {}
		};
	},
	mounted() {
		this.getData();
	},
	computed: {
		...mapGetters(['IsCollapse'])
	},
	methods: {
		getData: debounce(function() {
			let { hour } = this;
			disasterPreventionApi.getWaterSchedule4C({ hour }).then(res => {
				if (res.code == 0) {
					this.countData = res.data;
				}
			});
		}, 500)
	}
};
</script>
<style lang="scss" scoped>
@import '../../water_disaster_custom/countCards/style/CountCard.scss';
.water-schedule-count {
	width: 1200px;
	.has-count {
		height: 100%;
		display: flex;
		align-items: center;
		.setting {
			height: 100%;
			margin: 0 16px;
			border-left: 1px solid #fff;
			display: flex;
			align-items: center;
			padding-left: 16px;
			::v-deep.el-input-number {
				width: 72px;

				.el-input-number__decrease,
				.el-input-number__increase {
					background: transparent;
					width: 32px;
					> i {
						color: #fff;
					}
				}
				.el-input {
					.el-input__inner {
						border: 1px solid #fff;
						padding-left: 5px;
						padding-right: 36px;
					}
				}
			}
		}
		.count-item {
			display: flex;
			flex-direction: column;
			justify-content: space-around;
			height: 100%;
			margin: 0 6px;
		}
		p {
			text-align: center;
		}
	}
}
</style>
