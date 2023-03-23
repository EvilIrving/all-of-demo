
const BASIC_OPTION = {
    grid:{
        top:0
    },
    tooltip: {
      show: true,
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c} ({d}%)',
      confine:true
    },
    color: ["#19AF47", "#FF551F", "#E80000"],
    series: [{
        name: '断面个数',
        type: 'pie',
        radius: [0, '80%'],
        center: ['50%', '50%'],
        roseType: 'area',
        label: {
          formatter: [
            '{a|{b}}',
            '{b|{c}}{c|个}'
          ].join('\n'),

          rich: {
            a: {
              color: '#fff',
              fontSize: 16,
              lineHeight: 10,
            },
            b: {
              color: '#1EF8F2',
              fontSize: 24,
              padding:[6,0],
            },
            c: {
              padding:[6,0],
              fontSize: 18,
              color: "#fff",
              fontFamily: 'Microsoft YaHei',
              borderColor: '#fff',
            },
          },
        },
        itemStyle: {
          borderRadius: 5
        },
        data: [
          { value: 100, name: '正常断面' },
          { value: 200, name: '橙色预警断面' },
          { value: 300, name: '红色预警断面' },
        ]
      }],
    
};


export { BASIC_OPTION };