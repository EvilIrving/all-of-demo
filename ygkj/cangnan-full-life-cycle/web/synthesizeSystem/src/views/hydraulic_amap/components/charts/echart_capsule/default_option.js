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
      show:false
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
      splitLine:{
          show:false
      },
      axisTick:{
          show:false,
      },
      axisLine:{
          show:false,
      },
      axisLabel:{
          color:'#fff'
      },
      name:'数目',
      nameTextStyle:{
          color:"#fff"
      }
  },
  yAxis: {
      type: 'category',
      data: [],
      splitLine:{
          show:false
      },
      axisTick:{
          show:false,
      },
      axisLine:{
          show:false,
      },
      axisLabel:{
          color:'#fff'
      }
  },
  series: [
      {
          name: '数目',
          type: 'bar',
          data: [],
          barWidth:8,
          label:{
              show:true,
              position:'right'
          },
          itemStyle:{
              barBorderRadius:[10,10,10,10],
              borderWidth:2,
              borderColor:'#ccc'
          }
      },
  ]
};

const COLOR_ARRAY = ["#37A2DA"];

export { BASIC_OPTION, COLOR_ARRAY};
