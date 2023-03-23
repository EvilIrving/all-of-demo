<template>
	<div :class="['count-card', !title && 'no-title']">
		<div v-if="title" :class="['drag', 'count-title']">
			<span class="title-name">
				<i :class="[title == '报警' ? 'icon-count-title-left-white' : 'icon-count-title-left']" />
				{{ title }}
				<i :class="[title == '报警' ? 'icon-count-title-right-white' : 'icon-count-title-right']" />
			</span>
			<span v-if="title == '报警'" class="title-btn" style="margin-right:170px;" @click="alarmShow">
				<i class="icon-liebiao" style="margin-right:10px;" />详表
			</span>
			<span class="title-right">
				<slot name="title-right"></slot>
			</span>
			<span class="title-btn" @click="hideCount"> <i class="icon-hide" />隐藏 </span>
		</div>
		<div class="count-content">
			<slot></slot>
		</div>
	</div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
export default {
	name: 'CountWrap',
	data() {
		return {};
	},
	props: {
		title: { type: String, default: '' },
		warn: { type: Boolean, default: false }
	},
	created() {},
	computed: {
		...mapGetters([])
	},
	methods: {
		...mapActions(['SetActiveCount', 'SetAlarmDialog','SetStatisticFlag']),
		alarmShow() {
			this.SetAlarmDialog(true);
		},
		hideCount() {
			this.SetStatisticFlag(false);
		}
	}
};
</script>

<style lang="scss" scoped>
	@import '../countCards/style/CountCard';
	.title-right {
		right: 80px;
		position: absolute;
		top: 0;
	}
</style>
