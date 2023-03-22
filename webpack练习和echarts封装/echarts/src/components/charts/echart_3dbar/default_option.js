// 这里的数据会被深度合并
const BASIC_OPTION = {
    grid: {
        top: 30,
        left: 28,
        right: 10,
        bottom: 45
    },
    xAxis: {
        type: 'category',
        data: ['2017', '2018', '2019', '2020', '2021', '2022', '2023'],
        axisLabel: {
            interval: 0,
            fontSize: "16",
            color: "#fff",
        },
        axisLine: {
            color: "#fff",
            lineStyle: {
                color: "#fff",
                opacity: 0.5,
            },
        }
    },
    yAxis: {
        type: 'value',
        axisLine: {
            show: false,
        },
        axisLabel: {
            color: "#fff",
            fontSize: "16",
        },
        splitLine: {
            show: false,
            lineStyle: {
                color: "#fff",
                opacity: 0.5,
            },
        },
    },
    legend: {
        show: true,
        right: 0,
        top: 0,
        textStyle:{
            color:"#fff"
        }
    },
    series: []
};

const COLOR_ARRAY = ["#2886c6", "#50bfda", "#89e3ec"];

export { BASIC_OPTION, COLOR_ARRAY };