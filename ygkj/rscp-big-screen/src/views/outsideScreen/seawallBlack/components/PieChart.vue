<!--
 * @Author: hanyu
 * @LastEditTime: 2021-08-13 17:47:29
 * @Description: 饼图
 * @FilePath: /rscp-big-screen/src/components/PieChart.vue
-->
<template>
  <div class="chart_wrap">
    <div class="chart_title_black" v-if="!chartData.noTitle">
      <!-- <img src="@/assets//images/chart_item_icon.png" alt=""> -->
      {{ chartData.title }}
      <span
        v-if="chartData.id == 'htsjfhbzChart' && leftSelectPreventLevel != ''"
        class="clean_btn"
        @click="cleanClickStatus(1)"
        >清除</span
      >
      <span
        v-if="
          (chartData.id == 'htjbtjChart' || chartData.id == 'skgmChart') &&
            leftSelectProjectScale != ''
        "
        class="clean_btn"
        @click="cleanClickStatus(2)"
        >清除</span
      >
    </div>
    <div
      v-if="chartData.series.length"
      class="myChart"
      :id="chartData.id"
      v-on-echart-resize
    ></div>
    <div v-else class="noData">
      <img src="@/assets/images/noData-black.png" alt="" />
    </div>
  </div>
</template>

<script>
export default {
  name: "",
  props: {
    chartData: {
      type: Object,
      required: true,
    },
    // 示例
    // htsjfhbzData: {
    //   title: "海塘设计防洪标准统计",
    //   id: 'htsjfhbzChart',
    //   unit: '',
    // legend: {
    //   orient: 'vertical',
    //   left: '60%',
    //   height: '70%'
    // },
    //   color: ['#1BC57E', '#6F9DF2', '#5D7092', '#F7C739', '#EB7E65', '#E02020'],
    //   series: [],
    // },
  },
  components: {},
  computed: {
    leftSelectPreventLevel() {
      return this.$store.state.leftSelectPreventLevel;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    },
  },
  data() {
    return {};
  },
  watch: {
    chartData: {
      handler(newVal, oldVal) {
        this.$nextTick(() => {
          this.initChart();
        });
      },
      deep: true,
      immediate: false,
    },
    leftSelectPreventLevel(val) {
      if (val == "") {
        if (this.chartData.id == "htsjfhbzChart") {
          this.initChart();
        }
      }
    },
    leftSelectProjectScale(val) {
      if (val == "") {
        if (
          this.chartData.id == "htjbtjChart" ||
          this.chartData.id == "skgmChart"
        ) {
          this.initChart();
        }
      }
    },
  },
  mounted() {},
  methods: {
    // 查询元素在数组中的索引值

    initChart() {
      console.log(this.chartData,'图');
      let legend = [];
      if (this.chartData.series.length) {
        this.chartData.series.map((v) => {
          legend.push(v.name);
        });
      }
      let myChart = this.$echarts.init(
        document.getElementById(this.chartData.id)
      );
      myChart.setOption({
        tooltip: {
          trigger: "item",
          formatter: "{b}: {c}" + this.chartData.unit + " ({d}%)",
        },
        // color: [
        //   "#5B8FF9",
        //   "#5AD8A6",
        //   "#A1ABD7",
        //   "#64D9F5",
        //   "#E4B297",
        //   "#8059ED",
        //   "#6DC8EC",
        //   "#9270CA",
        // ],
        legend: {
          show: true,
          // type: "scroll",
          orient: "vertical",
          left: "55%",
          top: "center",
          icon: "circle",
          data: legend,
          itemGap: 10,
          textStyle: {
            color:'#fff',
            fontSize: 14,
          },
          // itemStyle: {
          //   color: [
          //     "#5B8FF9",
          //     "#5AD8A6",
          //     "#A1ABD7",
          //     "#64D9F5",
          //     "#E4B297",
          //     "#8059ED",
          //     "#6DC8EC",
          //     "#9270CA",
          //   ],
          // },
          formatter: (name) => {
            let data =  this.chartData.series.find(item=>item.name == name)
            console.log(data,'数据');
            return `${name} ${data.value}`;
          },
        },
        // series: [
        //   {
        //     name: '类型',
        //     type: 'pie',
        //     radius: ['0', '70%'],
        //     center: ["30%", "50%"],
        //     avoidLabelOverlap: false,
        //     itemStyle: {
        //       borderColor: '#fff',
        //       borderWidth: 1
        //     },
        //     label: {
        //         show: false,
        //         position: 'center'
        //     },
        //     labelLine: {
        //         show: false
        //     },
        //     color: this.chartData.color,
        //     data: this.chartData.series
        //   }
        // ],
        series: [
          {
            name: "区域分布",
            type: "pie",
            radius: ["20%", "50%"],
            center: ["35%", "50%"],
            data: this.chartData.series,
            zlevel: 1,
            selectedMode: "single",
            // itemStyle: {
            //   normal: {
            //     color: function(params) {
            //       return "#fff";
            //     },
            //     label: {
            //       show: false,
            //     },
            //   },
            // },
                 label: {
                  show: false,
                },
                labelLine:{
                  show:false
                }
          },
          {
            name: "区域分布",
            type: "pie",
            radius: ["20%", "50%"],
            center: ["35%", "50%"],
            data: this.chartData.series,
            zlevel: 2,
            selectedMode: "single",
            color: this.chartData.color,
            // itemStyle: {
            //   normal: {
            //     color: function(params) {
            //       //自定义颜色
            //       var colorList = [
            //         "#5B8FF9",
            //         "#5AD8A6",
            //         "#A1ABD7",
            //         "#64D9F5",
            //         "#E4B297",
            //         "#8059ED",
            //         "#6DC8EC",
            //         "#9270CA",
            //         "#DD62A9",
            //         "#269A99",
            //         "#FF99C3",
            //         "#DFC972",
            //         "#857BDA",
            //         "#70A4E9",
            //         "#99C046",
            //         "#5DC4AE",
            //         "#59842D",
            //         "#97F7E3",
            //         "#E4F3A5",
            //         "#70C4D1",
            //       ];
            //       return colorList[params.dataIndex];
            //     },
            //   },
            // },
            label: {
              show: false,
            },
            labelLine: {
              show: false,
            },
          },
        ],
      });
      myChart.on("click", this.clickChart);
    },
    clickChart(param) {
      this.$emit("pieClick", param.data);
    },
    cleanClickStatus(type) {
      if (type == 1) {
        this.$store.commit("SET_LEFT_SELECT_PREVENT_LEVEL", "");
      } else {
        this.$store.commit("SET_LEFT_SELECT_PROJECT_SCALE", "");
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.chart_wrap {
  width: 100%;
  height: 100%;
  .clean_btn {
    float: right;
    color: #789fec;
    cursor: pointer;
    font-size: 14px;
  }
  .myChart {
    width: 100%;
    height: calc(100% - 40px);
  }
  .noData {
    width: 100%;
    height: calc(100% - 40px);
    @include flexbox();
    @include flexAC();
    @include flexJC(center);
    img {
      width: 30%;
    }
  }
}
</style>
