<!--
 * @Date: 2022-01-12 15:01:12
 * @Author: dtb
 * @Description: 漏斗图
 * @LastEditors: dtb
 * @LastEditTime: 2022-02-28 09:49:59
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\components\FunnelChart.vue
-->

<template>
  <div class="chart_wrap">
    <div v-if="chartData.id !== ''" class="myChart" :id="chartData.id" v-on-echart-resize></div>
    <div v-else class="noData">
      <img :src="this.$config.imgUrl + 'noData-black.png'" alt="" />
    </div>
    <ul>
      <li class="title">
        <span>可纳雨量</span>
        <span class="nx_btn" @click="openDeduction('推演')">推演</span>
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
  mounted() { },
  methods: {
    openDeduction(deducing){
      this.$emit('openDeduction',deducing)
    },
    initChart() {
      let myChart = this.$echarts.init(document.getElementById(this.chartData.id));
      let option = {
        color: [
          'rgba(91, 190, 88, 0.7)',
          'rgba(56, 134, 255, 0.7)',
          'rgba(247, 181, 0, 0.7)',
          'rgba(250, 100, 0, 0.8)',
          '#73c0de',
          '#3ba272',
          '#fc8452',
          '#9a60b4',
          '#ea7ccc',
        ],
        title: {
          text: ''
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
          type: 'funnel',//类型：漏斗图
          left: '5%',
          top: 10,
          bottom: 10,
          width: '55%',//漏斗图组件的宽度
          min: 0,//指定的数据最小值
          max: 100,//指定的数据最大值
          sort: 'descending',////漏斗数据降序排列
          gap: 0,//数据图形间距
          label: {//漏斗图图形上的文本标签
            show: true,//显示文本标签
            position: 'inside'//标签的位置：漏斗图梯形内部
          },
          labelLine: {//标签的视觉引导线样式
            length: 10,//视觉引导线的长度
            lineStyle: {//线的样式
              width: 1,//宽度
              type: 'solid'
            }
          },
          itemStyle: {//图形样式
            borderColor: 'none',//图形的描边颜色
            borderWidth: 0//图形描边宽度
          },
          emphasis: {//高亮的标签和图形样式
            label: {
              fontSize: 20//当鼠标悬停在漏斗某部分上，重点突出文字大小
            }
          },
          data: this.chartData.data
        }
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
      &.title{
        height: 30px;
        .nx_btn{
          font-size: 28px;
          font-family: 'youshe';
          color: rgb(0, 230, 241);
          cursor: pointer;
          padding-left: 10px;
          position: relative;
          top: -3px;
        }
      }
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