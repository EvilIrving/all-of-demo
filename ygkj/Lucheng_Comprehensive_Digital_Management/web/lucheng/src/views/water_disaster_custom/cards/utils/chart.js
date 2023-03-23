/**
 * 生成两端线性渐变色
 * @param {String|any} color1
 * @param {String|any} color2
 * @param {{x:number;y:number;x2:number;y2:number}} option 渐变方向配置
 * @returns {Object}
 */
export function genNormalLinearGradients(color1, color2, option = { x: 0, x2: 1, y: 0, y2: 1 }) {
	const { x, x2, y, y2 } = option;
	let colorStops = [
		{ offset: 0, color: color1 },
		{ offset: 1, color: color2 }
	];
	let type = "linear";
	return { type, x, x2, y, y2, colorStops };
}

/**
 * 生成柱状用渐变色
 * @param {String|any} color1
 * @param {String|any} color2
 * @returns {Object}
 */
export function genBodyLinearGradients(color1, color2) {
	let colorStops = [
		{ offset: 0, color: color1 },
		{ offset: 0.5, color: color1 },
		{ offset: 0.5, color: color2 },
		{ offset: 1, color: color2 }
	];
	let type = "linear";
	return { type, x: 0, x2: 1, y: 0, y2: 0, colorStops };
}

/**
 * 生成柱状图的系列
 * @desc 如果需要特殊的颜色可以将参数color传Function
 * @param {{data:Array,barWidth:number,color:any,topBgColor:string,labelColor:string}} param0 data:数据数组，barWidth：柱状图宽度，color：柱状图渐变颜色，topBgColor：顶部颜色，labelColor：标签颜色
 * @param {boolean|number} needFixed 是否需要保留小数
 * @returns {Object}
 */
export function generateSeries(
	{ data, barWidth = 30, color, topBgColor = "#89e3ec", labelColor = "#00C1FF" },
	needFix = true,
	seriesName = "数据"
) {
	const itemStyle = {
		normal: {
			borderColor: "rgba(0,0,0,0)",
			borderWidth: 2,
			color: "rgba(0,0,0,0)"
		}
	};
	const staticData = data.map(item => (item > 0 ? 1 : 0));
	const showData = data.map(item => (item > 0 ? item : { value: 1, itemStyle }));

	const seriesZ1 = {
		z: 1,
		name: seriesName,
		type: "bar",
		barWidth,
		barGap: "0%",
		data: data,
		itemStyle: { normal: { color } },
		label: {
			show: true,
			fontSize: 18,
			fontWeight: "bold",
			position: "top",
			color: labelColor,
			formatter: ({ value }) => {
				if (needFix == false) {
					return value;
				} else if (typeof needFix == "boolean" && needFix) {
					return Number(value).toFixed(1);
				} else if (typeof needFix == "number" && needFix > 0) {
					return Number(value).toFixed(needFix);
				}
			}
		}
	};
	const seriesZ2 = {
		z: 2,
		name: seriesName,
		type: "pictorialBar",
		data: staticData,
		symbol: "diamond",
		symbolOffset: ["0%", "50%"],
		symbolSize: [barWidth, 10],
		itemStyle: { normal: { color } },
		tooltip: {
			show: false
		}
	};
	const seriesZ3 = {
		z: 3,
		name: seriesName,
		type: "pictorialBar",
		symbolPosition: "end",
		data: showData,
		symbol: "diamond",
		symbolOffset: ["0%", "-50%"],
		symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
		itemStyle: {
			normal: {
				borderColor: topBgColor,
				borderWidth: 2,
				color: topBgColor
			}
		},
		tooltip: { show: false }
	};
	return [seriesZ1, seriesZ2, seriesZ3];
}

/**
 * 生成DataZoom
 * @param {Number} count 需要显示的图像数量
 * @param {Number} length 总数
 * @param {{axis:'x'|'y',index:number|number[]}} setting 显示指定轴配置,more to do
 * @returns {Object}
 */
export function generateDataZoom(count, length, setting = { axis: "x", index: [0] }) {
	let ret = {
		type: "slider",
		start: 0, //默认为0,
		end: (count / length) * 100, //默认为100
		show: length > count,
		handleSize: 0, //滑动条的 左右2个滑动条的大小
		fillerColor: "#1EF8F2",
		backgroundColor: "#0000",
		borderColor: "#0000",
		showDetail: false,
		showDataShadow: false, //是否显示数据阴影 默认auto
		realtime: true, //是否实时更新
		filterMode: "filter"
	};
	ret[setting.axis == "x" ? "height" : "width"] = 5;
	ret[setting.axis == "x" ? "bottom" : "top"] = 0;
	ret[`${setting.axis}AxisIndex`] = setting.index;
	return ret;
}

/**
 * 生成DataZoom startVal endVal
 * @param {{axis<String>,index<Number|Number[]>}} setting 显示指定轴配置,more to do
 * @returns {Object}
 */
export function generateStartDataZoom(startValue = 0, endValue = 5, setting = { axix: "x", index: [0] }) {
	let ret = {
		type: "slider",
		startValue: startValue,
		endValue: endValue,
		show: true,
		xAxisIndex: [0],
		handleSize: 0, //滑动条的 左右2个滑动条的大小
		height: 5, //组件高度
		bottom: 0,
		fillerColor: "#1EF8F2",
		backgroundColor: "#0000",
		borderColor: "#0000",
		showDetail: false,
		showDataShadow: false, //是否显示数据阴影 默认auto
		realtime: true, //是否实时更新
		filterMode: "filter"
	};
	ret[`${setting.axis}AxisIndex`] = setting.index;
	return ret;
}

/**
 * 字数过多换行formatter
 * @param {string} param
 * @param {number} length 分割的长度
 */
export function wrapFormatter(param, length = 5) {
	const regexp = new RegExp(`.{1,${length}}`, "g");

	return param
		.replace(/(^\s*)|(\s*$)/g, "")
		.match(regexp)
		.join("\n");
}
