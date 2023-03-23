import "./style.scss";
import { debounce } from "@/utils/utils";
import { mapActions, mapGetters } from "vuex";
import moment from "moment";

export default {
	name: "PrimaryTimelineBar",
	props: {
		autoPlay: { type: Boolean, default: false },
		beginTime: { type: String, default: new Date().format("yyyy-MM-dd hh:mm") },
		finishTime: { type: String, default: moment(new Date().format("yyyy-MM-dd hh:mm"), "x").add(4, "d") }
	},
	data() {
		return {
			value: 1,
			position: 0,
			inPlay: false,
			currentTime: "",
		};
	},
	computed: {
		...mapGetters(["formatterCurrentTime", "formatterEndTime", "ScreenMap", "barTimer"]),
		isBigScreen() {
			return this.$route.query.large == "true";
		},
		startTime() {
			return this.beginTime;
		},
		timeRange() {
			let startMomentObj = moment(this.beginTime, "x").add(this.position, "d");
			let endMomentObj = moment(this.finishTime, "x");
			return [startMomentObj, endMomentObj];
		},
		timeList() {
			const time1 = this.timeRange[0];
			const time2 = moment(time1).add(1, "d");
			const time3 = moment(time2).add(1, "d");
			const time4 = moment(time3).add(1, "d");
			const time5 = moment(time4).add(1, "d");
			return { 1: time1, 24: time2, 48: time3, 72: time4, 96: time5 };
		},
		isBefore() {
			let endMomentObj = moment(this.beginTime, "x").add(this.position + 4, "d");
			return endMomentObj.isBefore(moment(this.finishTime, "x"));
		},
		maxValue() {
			let start = moment(this.beginTime, "x");
			let end = moment(this.finishTime, "x");
			let diffDay = end.diff(start, "d");
			return diffDay < 5 ? diffDay * 24 - 1 : 119;
		},
		playTimer() {
			return this.barTimer;
		}
	},
	watch: {
		startTime: {
			handler(val, oldVal) { },
		},
		inPlay: {
			deep: true,
			immediate: true,
			handler: function (val, oldVal) {
				if (val) {
					this.startPlayTimer();
				} else {
					this.setBarTimer(null);
				}
			},
		},

	},
	methods: {
		...mapActions(["setCurrentTimeatmap", "changeScreenLoading", "setBarTimer"]),
		formatTooltip(value) {
			let time = moment(this.timeRange[0])
				.add(value, "h")
				.format("YYYY年MoDo HH时");

			return `${time != "Invalid date" ? time : "-"}`;
		},
		renderActionPlay() {
			return (
				<div
					class="timeline-action"
					onClick={() => {
						this.inPlay = !this.inPlay;
					}}
				>
					<i class={this.inPlay ? "el-icon-video-pause" : "el-icon-video-play"} />
				</div>
			);
		},
		handleSliderChange: debounce(function (value) {
			// console.log(value,222)
			const oldtime = moment(this.currentTime, "x");
			const time = moment(this.timeRange[0]).add(value, "h");
			// console.log(time,2333);
			if ((oldtime, time, time.diff(oldtime) === 0)) return;
			this.doChangeAction(time);
		}, 1000),
		doChangeAction(time) {
			this.currentTime = time;
		},
		getSliderMark() {
			let timelist = JSON.parse(JSON.stringify(this.timeList));
			for (let item in this.timeList) {
				let datetime = moment(timelist[item]);
				let date = datetime.format("MoDo");
				let time = datetime.format("HH时");
				const VNode = (
					<div class="mark-label">
						<div class="date">{date == "Invalid date" ? "-" : date}</div>
						<div class="time">{time == "Invalid date" ? "-" : time}</div>
					</div>
				);
				timelist[item] = { label: VNode };
			}
			return timelist;
		},
		handlePrevButton() {
			if (this.position >= 1) {
				this.position--;
				this.handleSliderChange(this.value);
			}
		},
		handleNextButton() {
			if (this.isBefore) {
				this.position++;
				this.handleSliderChange(this.value);
			}
		},
		startPlayTimer() {
			if (this.value < this.maxValue) {
				const playTimer = setInterval(() => {
					this.value++;
					const time = moment(this.timeRange[0]).add(this.value, "h");
					this.doChangeAction(time);
				}, 5000);
				this.setBarTimer(playTimer);
			} else {
				this.setBarTimer(null);
			}
		}
	},
	render() {
		let barPlay = this.renderActionPlay();
		return (
			<div class={["range-timeline-bar", this.isBigScreen && "is-big-screen"]}>
				{this.autoPlay && barPlay}
				<el-tooltip effect="dark" content="切换上一段时间" placement="top">
					<div class="prev-button" onClick={this.handlePrevButton} disabled={!this.position >= 1} />
				</el-tooltip>
				<div class="range-timeline-bar__wrapper">
					<div class="range-timeline-bar__scale">
						<a-slider
							value={this.value}
							tipFormatter={this.formatTooltip}
							tooltipPlacement="top"
							onChange={(value) => {
								this.value = value;
							}}
							onAfterChange={this.handleSliderChange}
							marks={this.getSliderMark()}
							min={1}
							max={this.maxValue}
							dots
						/>
					</div>
				</div>
				<el-tooltip effect="dark" content="切换下一段时间" placement="top">
					<div class="next-button" onClick={this.handleNextButton} disabled={!this.isBefore} />
				</el-tooltip>
			</div>
		);
	},
};
