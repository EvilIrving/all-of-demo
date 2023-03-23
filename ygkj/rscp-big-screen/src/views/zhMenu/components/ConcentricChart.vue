<!--
 * @Date: 2022-01-12 15:01:12
 * @Author: dtb
 * @Description: 同心圆
 * @LastEditors: dtb
 * @LastEditTime: 2022-02-28 10:01:53
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\components\ConcentricChart.vue
-->

<template>
  <div class="chart_wrap">
    <div v-if="chartData.id !== ''" class="myChart" :id="chartData.id" v-on-echart-resize></div>
    <div v-else class="noData">
      <img :src='imgUrl' alt="" />
    </div>
    <ul>
      <li>
        <span>可纳雨量</span>
      </li>
      <li>
        <span class="green"></span><span>200mm以上</span>
      </li>
      <li>
        <span class="blue"></span><span>100-200mm</span>
      </li>
      <li>
        <span class="orange"></span><span>50-100mm</span>
      </li>
      <li>
        <span class="red"></span><span>50mm以下</span>
      </li>
    </ul>

  </div>
</template>

<script>
export default {
  name: "",
  props: {
    chartData: {
      type: Object,
      required: true
    }
  },
  components: {},
  data() {
    return {
      imgUrl: 'https://cdn.ygwjg.com/rscp/screen/1.0/skzg/noData-black.png'
    };
  },
  computed: {

  },
  watch: {
    chartData: {
      handler(newVal, oldVal) {
        this.$nextTick(() => {
          this.initChart();
        })
      },
      deep: true,
      immediate: false
    }
  },
  mounted() {},
  methods: {
    initChart() {
      let myChart = this.$echarts.init(document.getElementById(this.chartData.id));
      let option = {
        color: [
          'rgba(91, 190, 88, 0.7)',
          'rgba(56, 134, 255, 0.7)',
          'rgba(247, 181, 0, 0.7)',
          'rgba(250, 100, 0, 0.8)',
          'rgba(250, 100, 0, 0.8)',
        ],
        title: {
          text: ''
        },
        grid: {
          left: '0',
          right: '0',
          top: '50',
          bottom: '50',
          containLabel: true
        },
        tooltip: {
          trigger: 'item',//数据项图形触发
          //{a}: 系列名称，就是 series 中的 name
          //{b}: 类目值， 就是 x 轴的分类名
          //{c}: 数值， 当前分类对应的数值
          //<br/>换行
          formatter: '{a} {b}'//回调函数
        },
        toolbox: {//工具栏
          show: false,
        },
        legend: {//图例
          show: false,
          right: '10%',//图例组件离容器左侧的距离：靠左
          orient: 'vertical',//图例列表的布局朝向：垂直
          top: 'center',//图例组件离容器左侧的距离：居中
          textStyle: { //图例文字的样式
            color: '#fff',
            fontSize: 15
          },
          formatter: (name) => {
            let target = null;
            let l = this.chartData.data.length
            for (let i = 0; i < l; i++) {
              if (this.chartData.data[i].name == name) {
                target = this.chartData.data[i].fz;
              }
            }
            return target;
          }
        },
        series: [{
          name: '',
          type: 'pie',
          center: ['35%', '50%'],
          radius: [+this.chartData.data[1].value, +this.chartData.data[0].value],
          label: { normal: { position: 'inside', color: '#FFF' } },
          labelLine: { normal: { show: false } },
          data: [{ value: this.chartData.data[0].num, name: this.chartData.data[0].name }]
        }, {
          name: '',
          type: 'pie',
          center: ['35%', '50%'],
          radius: [+this.chartData.data[2].value, +this.chartData.data[1].value],
          label: { normal: { position: 'inside', color: '#FFF' } },
          labelLine: { normal: { show: false } },
          data: [{ value: this.chartData.data[1].num, name: this.chartData.data[1].name }]
        }, {
          name: '',
          type: 'pie',
          selectmode: 'single',
          center: ['35%', '50%'],
          radius: [+this.chartData.data[3].value, +this.chartData.data[2].value],
          label: { normal: { position: 'inside', color: '#FFF' } },
          labelLine: { normal: { show: false } },
          data: [{ value: this.chartData.data[2].num, name: this.chartData.data[2].name }]
        }, {
          name: '',
          type: 'pie',
          selectmode: 'single',
          center: ['35%', '50%'],
          radius: [0, +this.chartData.data[3].value],
          label: { position: 'center', color: '#FFF' },
          labelLine: { show: false },
          data: [{ value: this.chartData.data[3].num, name: this.chartData.data[3].name }]
        },
        ]
      };
      myChart.setOption(option);
    },
  }
};
</script>

<style lang='scss' scoped>
.chart_wrap {
  width: 100%;
  height: 100%;
  position: relative;
  ul {
    width: 30%;
    position: absolute;
    top: 15%;
    right: 5%;
    li {
      user-select: none;
      -webkit-user-select: none;
      width: 100%;
      font-size: 16px;
      line-height: 24px;
      @include flexbox();
      @include flexAC();
      @include flexJC(flex-start);
      span.green {
        width: 24px;
        height: 14px;
        margin-right: 5px;
        border-radius: 2px;
        background: rgba(91, 190, 88, 0.7);
      }
      span.blue {
        width: 24px;
        height: 14px;
        margin-right: 5px;
        border-radius: 2px;
        background: rgba(56, 134, 255, 0.7);
      }
      span.orange {
        width: 24px;
        height: 14px;
        margin-right: 5px;
        border-radius: 2px;
        background: rgba(247, 181, 0, 0.7);
      }
      span.red {
        width: 24px;
        height: 14px;
        margin-right: 5px;
        border-radius: 2px;
        background: rgba(250, 100, 0, 0.8);
      }
    }
  }
  .myChart {
    width: 100%;
    height: 100%;
  }
  .noData {
    width: 100%;
    height: 100%;
    @include flexbox();
    @include flexAC();
    @include flexJC(center);
    img {
      width: 35%;
    }
  }
}
</style>