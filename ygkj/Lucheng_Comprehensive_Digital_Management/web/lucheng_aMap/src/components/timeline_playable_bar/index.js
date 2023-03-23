import "./style.scss";
export default {
	name: "PlayableTimelineBar",
	props: {
		//
		defaultIndex: { type: Number, default: 0 },
		dark: { type: Boolean, default: false },
		autoPlay: { type: Boolean, default: false },
		hideArrow: { type: Boolean, default: false },
		hidePlay: { type: Boolean, default: false },
		delay: { type: [Number, String], default: 2000 },
		timelineList: {
			type: Array,
			default: () => [
				//
				{ label: "1小时" },
				{ label: "3小时" },
				{ label: "6小时" },
				{ label: "12小时" },
				{ label: "24小时" },
				{ label: "72小时" },
			],
		},
		beforePlayNext: {
			type: Function,
			default: () => {
				return new Promise((resolve) => {
					setTimeout(() => {
						resolve();
					}, 1000);
				});
			},
		},
	},
	data() {
		return {
			playTimer: null,
			activeIndex: 0,
			inPlay: false,
		};
	},
	mounted() {
		this.activeIndex = this.defaultIndex;
		this.inPlay = this.autoPlay;
	},
	beforeDestroy() {
		clearTimeout(this.playTimer);
		this.playTimer = null;
	},
	watch: {
		inPlay: {
			deep: true,
			immediate: true,
			handler: function(val, oldVal) {
				if (val) {
					this.startPlayTimer();
				} else {
					clearTimeout(this.playTimer);
					this.playTimer = null;
				}
			},
		},
		activeIndex: {
			deep: true,
			immediate: true,
			handler: function(val, oldVal) {
				let marginLeft = 0;
				if (this.timelineList.length - val > 2) {
					marginLeft = val * 100 - 300;
				} else {
					marginLeft = (this.timelineList.length - 3) * 100 - 300;
				}
				if (val == 0) {
					marginLeft = 0;
				}
				this.$emit("change", val);
				this.$nextTick(() => {
					this.$refs.TimelineList.style.marginLeft = `-${marginLeft}px`;
				});
			},
		},
	},
	render() {
		let wrapperWidth = (this.timelineList.length * 100 < 600 ? this.timelineList.length * 100 : 600) + 150;

		let barContent = this.renderbarContent();
		let barPlay = this.renderActionPlay();
		let listArrowLeft = this.renderArrowLeft();
		let listArrowRight = this.renderArrowRight();
		/** filtered function components list*/
		let componentsList = [!this.hidePlay && barPlay, !this.showArrow && listArrowLeft, barContent, !this.showArrow && listArrowRight];
		return (
			<div class="timeline-bar">
				<div class="timeline-bar-wrapper" style={{ width: `${wrapperWidth}px`, marginLeft: "auto" }}>
					<div class="timeline-bar__content">
						<div class={["timeline", this.dark && "dark"]}>{componentsList}</div>
					</div>
				</div>
			</div>
		);
	},
	methods: {
		renderbarContent() {
			let contentWidth = this.timelineList.length * 100 < 600 ? this.timelineList.length * 100 : 600;
			return (
				<div class="timeline-content" style={{ width: `${contentWidth}px` }}>
					<ul class="timeline-list" ref="TimelineList">
						{this.timelineList.map((item, index) => (
							<li class={["timeline-item", index == this.activeIndex && "active"]}>
								<div class="timeline-item__line" />
								<div
									class="timeline-item__symbol"
									onClick={() => {
										this.activeIndex = index;
									}}
								/>
								<div class="timeline-item__wrapper">{item.label}</div>
							</li>
						))}
					</ul>
				</div>
			);
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
		renderArrowLeft() {
			let clickEvent = () => {
				this.activeIndex > 0 && this.activeIndex--;
			};
			return (
				<div class="timeline-action__arrow-left" onClick={clickEvent}>
					<i class="el-icon-arrow-left" />
				</div>
			);
		},
		renderArrowRight() {
			let clickEvent = () => {
				this.activeIndex < this.timelineList.length - 1 && this.activeIndex++;
			};
			return (
				<div class="timeline-action__arrow-right" onClick={clickEvent}>
					<i class="el-icon-arrow-right" />
				</div>
			);
		},
		/**
		 * 开始定时器
		 * @description 用递归setTimeout调用一个Promise实现阻断的定时器
		 */
		startPlayTimer() {
			let delay = Number(this.delay);
			if (this.activeIndex < this.timelineList.length - 1) {
				this.beforePlayNext().then(() => {
					this.playTimer = setTimeout(() => {
						this.activeIndex++;
						this.startPlayTimer();
					}, delay);
				});
			} else {
				this.beforePlayNext().then(() => {
					this.playTimer = setTimeout(() => {
						this.activeIndex = 0;
						this.startPlayTimer();
					}, delay);
				});
			}
		},
	},
};
