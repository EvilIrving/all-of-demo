<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-01 16:32:16
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\charts\makeapie\makeapie.vue
-->
<template>
  <div class="chart">
    <div ref="makeapie"></div>
    <div ref="makeapieB"></div>
  </div>
</template>
<script>
import * as echarts from 'echarts';

export default {
  name:'Makeapie',
  mounted() {
    this.$nextTick(() => {
      this.init_a();
      this.init_b();
    });
  },
  methods: {
    init_a() {
      this.chart = echarts.init(this.$refs.makeapie);
      let _this = this;
      var data = [],
        data2 = [];
      var trafficWay = [
        { name: '水库超汛数量', value: 128 },
        { name: '水闸超警数量', value: 128 },
        { name: '堤防超警数量', value: 128 },
      ];
      var color = [
        '#50F9C0',
        '#FBE057',
        '#45CAFF',
      ];
      for (var i = 0; i < trafficWay.length; i++) {
        data.push(
          {
            value: trafficWay[i].value,
            name: trafficWay[i].name,
            itemStyle: {
              normal: { borderWidth: 8, shadowBlur: 20, borderRadius: 20, borderColor: color[i], shadowColor: color[i] }
            }
          },
          {
            value: 5,
            name: '',
            itemStyle: {
              normal: {
                label: { show: false },
                labelLine: { show: false },
                color: 'rgba(0, 0, 0, 0)',
                borderColor: 'rgba(0, 0, 0, 0)',
                borderWidth: 0
              }
            }
          }
        );
        data2.push(
          {
            value: trafficWay[i].value,
            name: trafficWay[i].name
          },
          {
            value: 5,
            name: '',
            itemStyle: {
              normal: {
                label: { show: false },
                labelLine: { show: false },
                color: 'rgba(0, 0, 0, 0)',
                borderColor: 'rgba(0, 0, 0, 0)',
                borderWidth: 0,
                opacity: 0.2
              }
            }
          }
        );
      }
      let option = {
        color: color,
        series: [
          {
            name: '',
            type: 'pie',
            clockWise: false,
            radius: ['98%', '95%'],
            hoverAnimation: false,
            center: ['33.33%', '50%'],
            top: 'center',
            itemStyle: { normal: { label: { show: false } } },
            data: data
          },
          {
            type: 'pie',
            top: 'center',
            startAngle: 90,
            clockwise: false,
            center: ['33.33%', '50%'],
            legendHoverLink: false,
            hoverAnimation: false,
            radius: ['94%', '55%'],
            itemStyle: { opacity: 0.15 },
            label: { show: false, position: 'center' },
            labelLine: { show: false },
            data: data2
          },
          {
            name: '',
            type: 'pie',
            clockWise: false,
            center: ['33.33%', '50%'],
            radius: ['39%', '38%'],
            hoverAnimation: false,
            top: 'center',
            itemStyle: { normal: { label: { show: false } } },
            data: data
          }
        ]
      };
      //获取圆上面某点的坐标(x0,y0表示坐标，r半径，angle角度)
      _this.chart.setOption(option, true);
    },
    init_b() {
      this.chart = echarts.init(this.$refs.makeapieB);
      let _this = this;
      var color = [
        '#2A8BFD',
        '#BAFF7F',
        '#00FAC1',
        '#00CAFF',
        '#FDE056',
        '#4ED33C',
        '#FF8A26',
        '#FF5252',
        '#9689FF',
        '#CB00FF'
      ];
      let angle = 0;
      let option = {
        backgroundColor: 'transparent',
        color: color,
        series: [
          {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params, api) {
              return {
                type: 'arc',
                shape: {
                  cx: api.getWidth() / 3,
                  cy: api.getHeight() / 2,
                  r: (Math.min(api.getWidth(), api.getHeight()) / 1.8) * 0.6,
                  startAngle: ((0 + angle) * Math.PI) / 180,
                  endAngle: ((90 + angle) * Math.PI) / 180
                },
                style: { stroke: '#4EE9E6', fill: 'transparent', lineWidth: 1.5 },
                silent: true
              };
            },
            data: [0]
          },
          {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params, api) {
              return {
                type: 'arc',
                shape: {
                  cx: api.getWidth() / 3,
                  cy: api.getHeight() / 2,
                  r: (Math.min(api.getWidth(), api.getHeight()) / 1.8) * 0.6,
                  startAngle: ((180 + angle) * Math.PI) / 180,
                  endAngle: ((270 + angle) * Math.PI) / 180
                },
                style: { stroke: '#4EE9E6', fill: 'transparent', lineWidth: 1.5 },
                silent: true
              };
            },
            data: [0]
          },
          {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params, api) {
              return {
                type: 'arc',
                shape: {
                  cx: api.getWidth() / 3,
                  cy: api.getHeight() / 2,
                  r: (Math.min(api.getWidth(), api.getHeight()) / 1.5) * 0.65,
                  startAngle: ((270 + -angle) * Math.PI) / 180,
                  endAngle: ((40 + -angle) * Math.PI) / 180
                },
                style: { stroke: '#4EE9E6', fill: 'transparent', lineWidth: 1.5 },
                silent: true
              };
            },
            data: [0]
          },
          {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params, api) {
              return {
                type: 'arc',
                shape: {
                  cx: api.getWidth() / 3,
                  cy: api.getHeight() / 2,
                  r: (Math.min(api.getWidth(), api.getHeight()) / 1.5) * 0.65,
                  startAngle: ((90 + -angle) * Math.PI) / 180,
                  endAngle: ((220 + -angle) * Math.PI) / 180
                },
                style: { stroke: '#4EE9E6', fill: 'transparent', lineWidth: 1.5 },
                silent: true
              };
            },
            data: [0]
          },
          {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params, api) {
              let x0 = api.getWidth() / 3;
              let y0 = api.getHeight() / 2;
              let r = (Math.min(api.getWidth(), api.getHeight()) / 1.5) * 0.65;
              let point = getCirlPoint(x0, y0, r, 90 + -angle);
              return {
                type: 'circle',
                shape: { cx: point.x, cy: point.y, r: 4 },
                style: {
                  stroke: '#66FFFF',
                  fill: '#66FFFF'
                },
                silent: true
              };
            },
            data: [0]
          },
          {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params, api) {
              let x0 = api.getWidth() / 3;
              let y0 = api.getHeight() / 2;
              let r = (Math.min(api.getWidth(), api.getHeight()) / 1.5) * 0.65;
              let point = getCirlPoint(x0, y0, r, 270 + -angle);
              return {
                type: 'circle',
                shape: { cx: point.x, cy: point.y, r: 4 },
                style: { stroke: '#66FFFF', fill: '#66FFFF' },
                silent: true
              };
            },
            data: [0]
          },
          {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params, api) {
              let x0 = api.getWidth() / 3;
              let y0 = api.getHeight() / 2;
              let r = (Math.min(api.getWidth(), api.getHeight()) / 1.8) * 0.6;
              let point = getCirlPoint(x0, y0, r, 90 + angle);
              return {
                type: 'circle',
                shape: { cx: point.x, cy: point.y, r: 4 },
                style: { stroke: '#66FFFF', fill: '#66FFFF' },
                silent: true
              };
            },
            data: [0]
          },
          {
            name: 'ring5',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: function (params, api) {
              let x0 = api.getWidth() / 3;
              let y0 = api.getHeight() / 2;
              let r = (Math.min(api.getWidth(), api.getHeight()) / 1.8) * 0.6;
              let point = getCirlPoint(x0, y0, r, 270 + angle);
              return {
                type: 'circle',
                shape: { cx: point.x, cy: point.y, r: 4 },
                style: { stroke: '#66FFFF', fill: '#66FFFF' },
                silent: true
              };
            },
            data: [0]
          }
        ]
      };
      //获取圆上面某点的坐标(x0,y0表示坐标，r半径，angle角度)
      function getCirlPoint(x0, y0, r, angle) {
        let x1 = x0 + r * Math.cos((angle * Math.PI) / 180);
        let y1 = y0 + r * Math.sin((angle * Math.PI) / 180);
        return { x: x1, y: y1 };
      }
      function draw() {
        angle += 3;
        _this.chart.setOption(option, true);
      }
      window.requestAnimationFrame(draw);
      this.times = setInterval(function () {
        draw();
      }, 100);
    }
  }
};
</script>
<style lang="scss" scoped>
.chart {
  width: 100%;
  height: 100%;
  position: relative;
  > div {
    width: 100%;
    height: 100%;
    &:last-child {
      position: absolute;
      top: 0;
      left: 0;
      z-index: -1;
      background-color: transparent;
    }
  }
}
</style>