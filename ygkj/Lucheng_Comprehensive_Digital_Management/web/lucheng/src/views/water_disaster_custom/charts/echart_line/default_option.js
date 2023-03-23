// 这里的数据会被深度合并
const BASIC_OPTION = {
    grid: {
        top: 30,
        left: 28,
        right: 10,
        bottom: 45
    },
    xAxis: {
        boundaryGap: true,
        type: 'category',
        axisLabel: {
            show: true,
            textStyle: {
                color: "#fff",
            },
        },
        data: ['05-06 00:00', '05-07 00:00', '05-08 00:00', '05-09 00:00'],
        axisTick: {
            show: false
        },
        axisLine: {
            lineStyle: {
                color: "#fff",
                fontSize: "18",
            }
        },
    },
    yAxis: {
        type: 'value',
        axisTick: {
            show: false
        },
        axisLine: {
            show: false
        },
        axisLabel: {
            color: '#00C1FF'
        },
        splitLine: {
            lineStyle: {
                color: "#00C1FF",
                opacity: 0.3
            }
        },
        name: 'm',
        nameTextStyle: {
            color: "#fff",
            fontSize: "20"
        },
        axisLabel: {
            fontSize: "20",
            color: "#fff",
        }
    },
    tooltip: {
        show: true,
        trigger: 'axis'
    },
    legend: {
        show: true,
        top: '2',
        right: '10',
        orient: 'horizontal',
        textStyle: {
            color: "#fff",
            fontSize: "20"
        },
        icon: 'circle',
        itemWidth: 12,
        nameTextStyle: {
            color: "#00C1FF",
            fontSize: "20"
        }
    },
    series: [],
    color: ['#33FFFF', '#5DFF30', '#FF0000']
};

const COLOR_ARRAY = ['#33FFFF', '#5DFF30', '#FF0000'];

export { BASIC_OPTION, COLOR_ARRAY };