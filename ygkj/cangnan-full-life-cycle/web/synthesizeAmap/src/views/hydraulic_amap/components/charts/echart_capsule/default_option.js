// 这里的数据会被深度合并
const BASIC_OPTION = {
    title: {
        text: '',
        subtext: ''
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    legend: {
        show: false
    },
    grid: {
        top: 10,
        left: '3%',
        right: 50,
        bottom: 16,
        containLabel: true,
    },
    xAxis: {
        type: 'value',
        boundaryGap: [0, 0.01],
        splitLine: {
            show: false
        },
        axisTick: {
            show: false,
        },
        axisLine: {
            show: false,
        },
        axisLabel: {
            color: '#fff'
        },
        name: '个数',
        nameTextStyle: {
            color: "#fff"
        }
    },
    yAxis: {
        type: 'category',
        data: [],
        splitLine: {
            show: false
        },
        axisTick: {
            show: false,
        },
        axisLine: {
            show: false,
        },
        axisLabel: {
            color: '#fff'
        }
    },
    series: [{
        name: '个数',
        type: 'bar',
        data: [],
        barWidth: 8,
        label: {
            show: true,
            position: 'right'
        },
    }, ]
};

const COLOR_ARRAY = ["#56FEFE"];

export { BASIC_OPTION, COLOR_ARRAY };