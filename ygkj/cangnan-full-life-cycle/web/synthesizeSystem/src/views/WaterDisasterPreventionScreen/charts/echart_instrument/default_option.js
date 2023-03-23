const color1 = {
    type: 'linear',
    x: 0,
    y: 0,
    x2: 0,
    y2: 1,
    colorStops: [{
        offset: 0,
        color: '#F7C739' // 0% 处的颜色
    }, {
        offset: 1,
        color: '#E02020' // 100% 处的颜色
    }],
    global: false // 缺省为 false
}
const color2 = {
    type: 'linear',
    x: 0,
    y: 0,
    x2: 1,
    y2: 0,
    colorStops: [{
        offset: 0,
        color: '#193CC5' // 0% 处的颜色
    }, {
        offset: 1,
        color: '#F7C739' // 100% 处的颜色
    }],
    global: false // 缺省为 false
}
const color3 = {
    type: 'linear',
    x: 0,
    y: 0,
    x2: 1,
    y2: 0,
    colorStops: [{
        offset: 0,
        color: '#6F9DF2' // 0% 处的颜色
    }, {
        offset: 1,
        color: '#193CC5' // 100% 处的颜色
    }],
    global: false // 缺省为 false
}
const color4 = {
    type: 'linear',
    x: 0,
    y: 0,
    x2: 0,
    y2: 1,
    colorStops: [{
        offset: 0,
        color: '#6F9DF2' // 0% 处的颜色
    }, {
        offset: 1,
        color: '#19C57D' // 100% 处的颜色
    }],
    global: false // 缺省为 false
}
const color5 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 0,
        y2: 1,
        colorStops: [{
            offset: 0,
            color: 'red' // 0% 处的颜色
        }, {
            offset: 1,
            color: 'blue' // 100% 处的颜色
        }],
        global: false // 缺省为 false
    }
    // 这里的数据会被深度合并
const BASIC_OPTION = {
    // title: {
    //     show: true,
    //     text: "平均蓄水率",
    //     bottom: '0',
    //     left: 'center',
    //     textStyle: {
    //         color: '#fff',
    //         fontSize: 14
    //     }
    // },
    tooltip: {
        formatter: '{a} <br/>{b} : {c}%'
    },
    // toolbox: {
    //     feature: {
    //         restore: {},
    //         saveAsImage: {}
    //     }
    // },
    grid: {
        top: -5,
        left: 0,
        right: 0,
        bottom: 20
    },
    series: [{
        name: '',
        type: 'gauge',
        radius: '90%',
        detail: { formatter: '{value}%', color: "#fff", fontSize: 16, offsetCenter: [0, '70%'] },
        data: [{ value: 0 }, ],
        axisTick: {
            show: false,
        },
        title: {
            offsetCenter: [0, '80%'],
            color: 'rgba(255,255,255)',
            fontSize: 18
        },
        axisLine: {
            lineStyle: {
                fontSize: 18,
                color: [
                    [0.2, color4],
                    [0.4, color3],
                    [0.6, color2],
                    [1, color1],
                ],
                width: 12
            }
        },
        splitLine: {
            distance: -30,
            length: 15,
            show: false,
        },
        min: 20,
        max: 120,
        splitNumber: 5, // 分隔线之间分割的刻度数,默认 5。
        axisLabel: {
            color: "#fff",
            distance: 0,
            fontSize: 17
        }
    }],
};

const COLOR_ARRAY = ["#37A2DA"];

export { BASIC_OPTION, COLOR_ARRAY };