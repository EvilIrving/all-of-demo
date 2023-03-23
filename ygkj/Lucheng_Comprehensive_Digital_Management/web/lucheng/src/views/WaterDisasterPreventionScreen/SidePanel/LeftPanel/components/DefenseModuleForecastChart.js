const forecastChartOption = {
	grid: {
		top: "30%",
		bottom: "0%",
		containLabel: true,
	},
	settings: {
		yAxisName: ["mm", ""],
		labelMap: { min: "最小值", max: "最大值" },
		label: {
			show: true,
			fontSize: 14,
			position: "top",
			color: "#00C1FF",
			formatter: ({ value }) => Number(value).toFixed(1),
		},
	},
	extend: {
		legend: {
			right: "0%",
			icon: "circle",
			itemWidth: 10,
			textStyle: { color: "#FFF" },
		},
		"textStyle.color": "#fff",
		barWidth: 15,
		yAxis: {
			nameTextStyle: { color: "#ffffff99" },
			max: (value) => {
				return value.max > 1 ? null : 1;
			},
			axisLabel: {
				color: "#fff",
			},
			splitLine: {
				lineStyle: {
					color: "#00C1FF",
					opacity: 0.5,
				},
			},
		},
		"series.0.itemStyle": { color: "#00C1FF" },
		"series.1.itemStyle": { color: "#EEC80B" },
	},
};
const forecastConfig = (options) => {
	const minData = options.series[0].data;
	const maxData = options.series[1].data;
	let colorArr = ["#15ddd7", "#7dfefa", "#9dfffc"];
	let colorArr2 = ["#2886c6", "#50bfda", "#89e3ec"];

	let color = {
		type: "linear",
		x: 0,
		x2: 1,
		y: 0,
		y2: 0,
		colorStops: [
			{
				offset: 0,
				color: colorArr[0],
			},
			{
				offset: 0.5,
				color: colorArr[0],
			},
			{
				offset: 0.5,
				color: colorArr[1],
			},
			{
				offset: 1,
				color: colorArr[1],
			},
		],
	};
	let color2 = {
		type: "linear",
		x: 0,
		x2: 1,
		y: 0,
		y2: 0,
		colorStops: [
			{
				offset: 0,
				color: colorArr2[0],
			},
			{
				offset: 0.5,
				color: colorArr2[0],
			},
			{
				offset: 0.5,
				color: colorArr2[1],
			},
			{
				offset: 1,
				color: colorArr2[1],
			},
		],
	};
	let barWidth = 20;
	let constData = [];
	let showData = [];

	/** 最小柱形 */
	constData = minData.map((item) => (item ? 1 : 0));
	showData = minData.map((item) =>
		item
			? item
			: {
					value: 1,
					itemStyle: {
						normal: {
							borderColor: "rgba(0,0,0,0)",
							borderWidth: 2,
							color: "rgba(0,0,0,0)",
						},
					},
			  }
	);
	const minSeries = [
		{
			z: 1,
			name: "最小值",
			type: "bar",
			barWidth: barWidth,
			barGap: "0%",
			data: minData,
			itemStyle: {
				normal: {
					color: color,
				},
			},
			label: {
				show: true,
				fontSize: 18,
				fontWeight: "bold",
				position: "top",
				color: "#00C1FF",
				formatter: ({ value }) => Number(value).toFixed(1),
			},
		},
		{
			z: 2,
			name: "最小值",
			type: "pictorialBar",
			data: constData,
			symbol: "diamond",
			symbolOffset: [(-1 * barWidth) / 2, "50%"],
			symbolSize: [barWidth, 10],
			itemStyle: {
				normal: {
					color: color,
				},
			},
			tooltip: {
				show: false,
			},
		},
		{
			z: 3,
			name: "最小值",
			type: "pictorialBar",
			symbolPosition: "end",
			data: showData,
			symbol: "diamond",
			symbolOffset: [(-1 * barWidth) / 2, "-50%"],
			symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
			itemStyle: {
				normal: {
					borderColor: colorArr[2],
					borderWidth: 2,
					color: colorArr[2],
				},
			},
			tooltip: {
				show: false,
			},
		},
	];
	/** 最大柱形 */
	constData = maxData.map((item) => (item ? 1 : 0));
	showData = maxData.map((item) =>
		item
			? item
			: {
					value: 1,
					itemStyle: {
						normal: {
							borderColor: "rgba(0,0,0,0)",
							borderWidth: 2,
							color: "rgba(0,0,0,0)",
						},
					},
			  }
	);
	const maxSeries = [
		{
			z: 1,
			name: "最大值",
			type: "bar",
			barWidth: barWidth,
			barGap: "0%",
			data: maxData,
			itemStyle: {
				normal: {
					color: color2,
				},
			},
			label: {
				show: true,
				fontSize: 18,
				fontWeight: "bold",
				position: "top",
				color: "#00C1FF",
				formatter: ({ value }) => Number(value).toFixed(1),
			},
		},
		{
			z: 2,
			name: "最大值",
			type: "pictorialBar",
			data: constData,
			symbol: "diamond",
			symbolOffset: [barWidth / 2, "50%"],
			symbolSize: [barWidth, 10],
			itemStyle: {
				normal: {
					color: color2,
				},
			},
			tooltip: {
				show: false,
			},
		},
		{
			z: 3,
			name: "最大值",
			type: "pictorialBar",
			symbolPosition: "end",
			data: showData,
			symbol: "diamond",
			symbolOffset: [barWidth / 2, "-50%"],
			symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
			itemStyle: {
				normal: {
					borderColor: colorArr2[2],
					borderWidth: 2,
					color: colorArr2[2],
				},
			},
			tooltip: {
				show: false,
			},
		},
	];
	/** 平均 */
	let len = options.series[0].data.length;
	let data = new Array(len).fill(null).map((item, index) => {
		return Number((minData[index] + maxData[index]) / 2).toFixed(1);
	});
	let series = options.series;
	options.series = [
		...minSeries,
		...maxSeries,
		{
			name: "平均值",
			smooth: false,
			type: "line",
			data: data,
		},
	];

	options.legend.data = [...options.legend.data, "平均值"];
	options.legend.selectedMode = false;
	return options;
};
export { forecastChartOption, forecastConfig };
