const color1 = {
    type: 'linear',
    x: 0,
    y: 0,
    x2: 1,
    y2: 1,
    colorStops: [{
        offset: 0,
        color: '#19AF47' // 0% 处的颜色
    }, {
        offset: 1,
        color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
    }],
    global: false // 缺省为 false
}
const color2 = {
    type: 'linear',
    x: 0,
    y: 0,
    x2: 1,
    y2: 1,
    colorStops: [{
        offset: 0,
        color: '#EEC80B' // 0% 处的颜色
    }, {
        offset: 1,
        color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
    }],
    global: false // 缺省为 false
}
const color3 = {
    type: 'linear',
    x: 0,
    y: 0,
    x2: 1,
    y2: 1,
    colorStops: [{
        offset: 0,
        color: '#E89100' // 0% 处的颜色
    }, {
        offset: 1,
        color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
    }],
    global: false // 缺省为 false
}
const color4 = {
    type: 'linear',
    x: 0,
    y: 0,
    x2: 1,
    y2: 1,
    colorStops: [{
        offset: 0,
        color: '#DD00DB' // 0% 处的颜色
    }, {
        offset: 1,
        color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
    }],
    global: false // 缺省为 false
}
const color5 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [{
            offset: 0,
            color: '#E80000' // 0% 处的颜色
        }, {
            offset: 1,
            color: 'rgba(0,0,0,0.7)' // 100% 处的颜色
        }],
        global: false // 缺省为 false
    }
    // 这里的数据会被深度合并
const BASIC_OPTION = {
    tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c}'
    },
    legend: {
        show: false
    },
    series: [{
        name: '风险隐患',
        type: 'pie',
        // center: ['50%', '50%'], // 饼图的圆心坐标
        radius: ['60%', '90%'],
        avoidLabelOverlap: false,
        label: {
            show: false,
            position: 'center',
            normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: "center",
                color: "#fff",
                fontSize: 24,
                formatter: function(data) {
                    return (
                        data.data.total
                    );
                },
            },
        },
        emphasis: {
            label: {
                show: true,
                fontSize: '20',
                fontWeight: 'bold',
                color: "#fff",
            }
        },
        labelLine: {
            show: false
        },
        data: [
            // { value: 335, name: '鹿城区' },
            { value: 310, name: '瓯海区' },
            { value: 234, name: '洞头区' },
            { value: 135, name: '龙湾区' },
        ]
    }],
    color: [color1, color2, color3, color4, color5]
};

const COLOR_ARRAY = [color1, color2, color3, color4, color5];

export { BASIC_OPTION, COLOR_ARRAY };