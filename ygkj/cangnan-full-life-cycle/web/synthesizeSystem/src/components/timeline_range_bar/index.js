import "./style.scss";
import { debounce } from "@/utils/utils";
import areaWzBorder from "@/assets/water_data/areaWzBorder.json";
import axios from "axios";
import { mapActions, mapGetters } from "vuex";
import moment from "moment";
import mapConfig from "../../config/mapConfig";

export default {
	name: "SilderTimelineBar",
	props: {
		autoPlay: { type: Boolean, default: false },
	},
	data() {
		return { 
			value: 1, 
			position: 0,
			inPlay: false, 
		};
	},
	computed: {
		...mapGetters(["formatterStartTime","formatterPreStartTime", "currentScene","formatterCurrentTime", "formatterEndTime","formatterPreEndTime", "ScreenMap", "stationPoint", "rainfullActive","previewModule","barTimer"]),
		isBigScreen() {
			return this.$route.query.large == "true";
		},
		startTime() {
			return this.formatterStartTime();
		},
		timeRange() {
			const time = this.formatterStartTime("x");
			let startMomentObj = moment(time, "x").add(this.position, "d");
			let endMomentObj = moment(time, "x").add(this.position + 4, "d");
			return [startMomentObj, endMomentObj];
		},
		preTimeRange(){
			const time = this.formatterPreStartTime("x");
			let startMomentObj = moment(time, "x").add(this.position, "d");
			let endMomentObj = moment(time, "x").add(this.position + 4, "d");
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
		preTimeList(){
			const time1 = this.preTimeRange[0];
			const time2 = moment(time1).add(1, "d");
			const time3 = moment(time2).add(1, "d");
			const time4 = moment(time3).add(1, "d");
			const time5 = moment(time4).add(1, "d");
			return { 1: time1, 24: time2, 48: time3, 72: time4, 96: time5 };
		},
		isBefore() {
			let endMomentObj = moment(this.formatterStartTime("x"), "x").add(this.position + 4, "d");
			return endMomentObj.isBefore(moment(this.formatterEndTime("x"), "x"));
		},
		maxValue() {
			let start = moment(this.formatterStartTime("x"), "x");
			let end = moment(this.formatterEndTime("x"), "x");
			let diffDay = end.diff(start, "d");
			return diffDay < 5 ? diffDay * 24 - 1 : 119;
		},
		playTimer(){
			return this.barTimer;
		}
	},
	watch: {
		startTime: {
			handler(val, oldVal) {
				if (val != oldVal) {
					// console.log(this.currentScene(),2222)
					if(this.currentScene == '防御复盘'){
						this.changeScreenLoading(true);
						this.getStationData().then(() => {
							this.drawStationPoint();
							this.changeScreenLoading(false);
						});
					}
				}
			},
		},
		inPlay: {
			deep: true,
			immediate: true,
			handler: function(val, oldVal) {
				if (val) {
					this.startPlayTimer();
				} else {
					this.setBarTimer(null);
				}
			},
		},
		previewModule(val){
			if(!val){
				this.setBarTimer(null);
			}
		}
	},
	methods: {
		...mapActions(["setCurrentTimeatmap", "changeScreenLoading", "rainfullActive", "getStationData" ,"setBarTimer",""]),
		formatTooltip(value) {
			let time = moment(this.timeRange[0])
				.add(value, "h")
				.format("YYYY年MoDo HH时");
			if(this.previewModule){
				time = moment(this.preTimeRange[0])
				.add(value, "h")
				.format("YYYY年MoDo HH时");
			}
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
		drawStationPoint() {
			let style = {
				anchor: [5, 5],
				anchorXUnits: "pixels",
			};
			let imgSrc = require("@/assets/images/legend/rain_0.png");
			let pointList = this.stationPoint.map((point) => {
				if (point.drp > 0) {
					imgSrc = require("@/assets/images/legend/rain_10.png");
				}
				if (point.drp > 10) {
					imgSrc = require("@/assets/images/legend/rain_10_25.png");
				}
				if (point.drp > 25) {
					imgSrc = require("@/assets/images/legend/rain_25_50.png");
				}
				if (point.drp > 50) {
					imgSrc = require("@/assets/images/legend/rain_50_100.png");
				}
				if (point.drp > 100) {
					imgSrc = require("@/assets/images/legend/rain_100_250.png");
				}
				if (point.drp >= 250) {
					imgSrc = require("@/assets/images/legend/rain_250.png");
				}
				return { ...point, src: imgSrc ,type:'复盘雨量',};
			});
			this.ScreenMap.drawPoint(pointList, style, "stationLayer");
		},
		handleSliderChange: debounce(function(value) {
			// console.log(value,222)
			const oldtime = moment(this.formatterCurrentTime("x"), "x");
			const time = moment(this.timeRange[0]).add(value, "h");
			// console.log(time,2333);
			if ((oldtime, time, time.diff(oldtime) === 0)) return;
			this.doChangeAction(time);
		}, 1000),
		doChangeAction(time){
			this.setCurrentTimeatmap(time.valueOf());
			if(this.currentScene == '防御复盘'){
				this.changeScreenLoading(true);
				this.getStationData().then(() => {
					this.drawStationPoint();
					if (this.rainfullActive) {
						const params = this.formatRainfallParam(this.stationPoint, "drp");
						if (params.features.length > 0) {
							this.changeScreenLoading(true);
							this.getIsohyetArea(params, true);
						} else {
							this.changeScreenLoading(false);
						}
					} else {
						this.changeScreenLoading(false);
					}
				});
			}
		},
		loadRainFull(data) {
			if (this.rainfullActive) {
				const params = this.formatRainfallParam(this.stationPoint, "drp");
				if (params.features.length > 0) {
					this.changeScreenLoading(true);
					this.getIsohyetArea(params, true);
				}
			} else {
				this.ScreenMap.removeLayerByName("rainfall");
			}
		},
		getSliderMark() {
			let timelist = JSON.parse(JSON.stringify(this.timeList));
			//演练模式时时间label要改成当前时间开始的判断
			if(this.previewModule){
				timelist = JSON.parse(JSON.stringify(this.preTimeList));
				for (let item in this.preTimeList) {
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
			}else{
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
		/** 雨量数据转换成等雨量面参数格式 */
		formatRainfallParam(list, paramName) {
			let geoJson = {
				features: [],
			};
			let featuresArr = [];
			list.forEach((item) => {
				// let drp = item.drp > 0 ? item.drp : 0;
				// if (item[paramName] != "-" && item[paramName] > 0) {
				let obj = {
					attributes: {
						x: Number(item.lng),
						y: Number(item.lat),
						value: item[paramName],
					},
					geometry: {
						x: Number(item.lng),
						y: Number(item.lat),
					},
				};
				featuresArr.push(obj);
				// }
			});
			geoJson.features = featuresArr;
			return geoJson;
		},
		/** 获取等雨量面 type为true时 实时等雨量面 展示数字 */
		async getIsohyetArea(opt, type) {
			this.ScreenMap.clearTextLayer();
			// if(!type){
			//  this.ScreenMap._showTestPoint(opt.features);
			// }
			let params = new URLSearchParams();
			params.append("station", JSON.stringify(opt));
			params.append("f", "json");
			params.append("Extent", JSON.stringify(areaWzBorder));
			let _this = this;
			let areaUrl = "http://117.149.227.112:6080/arcgis/rest/services/gps/PointTRaster/GPServer/PointTRaster/submitJob";
			if (type) {
				areaUrl = mapConfig.mapServer + "gps/dengzhiPIDWReal0605/GPServer/dengzhiPIDWReal0605/submitJob";
			}
			await axios({
				url: areaUrl,
				method: "post",
				headers: { "content-type": "application/x-www-form-urlencoded" },
				data: params,
			}).then(async (res) => {
				let interVal = setInterval(async () => {
					let url = "http://117.149.227.112:6080/arcgis/rest/services/gps/PointTRaster/GPServer/PointTRaster/jobs/" + res.data.jobId + "/results/Idwres?f=pjson";
					if (type) {
						url = mapConfig.mapServer + "gps/dengzhiPIDWReal0605/GPServer/dengzhiPIDWReal0605/jobs/" + res.data.jobId + "/results/Idwres?f=pjson";
					}
					await axios({
						url: url,
						method: "get",
					}).then(async (result) => {
						if (!result.data.value.mapImage.error) {
							clearInterval(interVal);
							//加载透明等雨量面
							let mapImage = result.data.value.mapImage.extent;
							_this.getIsohyetOpacity(mapImage.xmin, mapImage.ymin, mapImage.xmax, mapImage.ymax, res.data.jobId, type);
						}
					});
				}, 1000);
			});
		},
		async getIsohyetOpacity(xmin, ymin, xmax, ymax, jobId, type) {
			//type用来区分预报和实时等值面服务 forecast为预报 预报请求112 实时请求117
			let bbox = xmin + "," + ymin + "," + xmax + "," + ymax;
			let params = new URLSearchParams();
			params.append("bbox", bbox);
			params.append("f", "pjson");
			params.append("transparent", true);
			let url = `${mapConfig.mapServer}gps/dengzhiPIDWReal0605/MapServer/jobs/${jobId}/export`;
			if (type == "forecast") {
				url = `${mapConfig.mapServer}gps/PointTRaster/MapServer/jobs/${jobId}/export`;
			}
			await axios({
				url: url,
				method: "post",
				headers: { "content-type": "application/x-www-form-urlencoded" },
				data: params,
			}).then((res) => {
				this.changeScreenLoading(false);
				this.ScreenMap.showIsohyetLayer(res.data);
			});
		},
		startPlayTimer(){
			if(this.value < this.maxValue){
				const playTimer = setInterval(()=>{
					this.value++;
					const time = moment(this.timeRange[0]).add(this.value, "h");
					this.doChangeAction(time);
				},5000);
				this.setBarTimer(playTimer);
			}else{
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
						{/* <el-slider
							value={this.value}
							showTooltip={true}
							formatTooltip={this.formatTooltip}
							showStops
							onInput={(value) => {
								this.value = value;
							}}
							onChange={this.handleSliderChange}
							min={1}
							max={this.maxValue}
							marks={mark}
						/> */}
					</div>
				</div>
				<el-tooltip effect="dark" content="切换下一段时间" placement="top">
					<div class="next-button" onClick={this.handleNextButton} disabled={!this.isBefore} />
				</el-tooltip>
			</div>
		);
	},
};
