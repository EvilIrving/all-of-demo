<template>
  <Empty v-if="isSeriesEmpty" class="custom-image" description="暂无数据" />
  <div v-else class="chart"></div>
</template>

<script>
import { isEmpty } from "lodash";
import { Empty } from "vant";
import { merge } from "lodash";
import { BASIC_OPTION, COLOR_ARRAY } from "./default_option";
import ResizeListener from "element-resize-detector";

export default {
  name: "ChartCapsule",
  props: {
    // 正常的业务数据，对应echarts饼图配置中series[0].data
    seriesData: {
      type: Array,
      required: true,
      default: () => [],
    },
    // 表示需要特殊定制的配置
    // 一般UI会规定一个统一的设计规范（比如颜色，字体，图例格式，位置等）
    // 但不排除某个图标会和设计规范不同，需要特殊定制样式，所以开放这个配置，增强灵活性
    extraOption: {
      type: Object,
      default: () => ({}),
    },
  },
  components: { Empty },
  computed: {
    // 针对饼图数据是不是无效的判断
    isSeriesEmpty() {
      return (
        isEmpty(this.seriesData) || this.seriesData.every((item) => !item.value)
      );
    },
  },
  data() {
    return {
      chart: null,
      checkName: "",
    };
  },
  watch: {
    seriesData: {
      deep: true,
      handler() {
        // console.log(this.seriesData, 666);
        this.checkName = "";
        this.updateChartView();
      },
    },
  },
  mounted() {
    this.chart = this.$echarts.init(this.$el);
    this.updateChartView();
    window.addEventListener("resize", this.handleWindowResize);
    this.addChartResizeListener();
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  methods: {
    /**
     * 将业务数据加入到基础样式配置中
     * @returns {Object} 完整的echart配置
     */
    assembleDataToOption() {
      // 这部分的图例formatter取决于UI要求，如果你的项目中不需要，就可以不写formatter
      // 由于echarts版本的迭代，这里的写法也有稍许改变
      const formatter = (name) => {
        const total = this.seriesData.reduce((acc, cur) => acc + cur.value, 0);
        const data = this.seriesData.find((item) => item.name === name) || {};
        const percent = data.value
          ? `${Math.round((data.value / total) * 100)}%`
          : "0%";

        return `${name} ${percent}`;
      };
      return merge(
        {},
        BASIC_OPTION,
        {
          legend: { formatter },
          series: [
            {
              data: this.seriesData,
              itemStyle: {
                barBorderRadius: [10, 10, 10, 10],
                borderWidth: 0,
                color: (params) => {
                  if (this.checkName === params.name) {
                    return "#19AF47"; //点击后的颜色
                  } else {
                    return COLOR_ARRAY; //默认颜色
                  }
                },
                backgroundColor: (params) => {
                  if (this.checkName === params.name) {
                    return "#062E41"; //点击后的颜色
                  } else {
                    return ""; //默认颜色
                  }
                },
              },
            },
          ],
        },
        this.extraOption
      );
    },

    /**
     * 对chart元素尺寸进行监听，当发生变化时同步更新echart视图
     */
    addChartResizeListener() {
      const instance = ResizeListener({
        strategy: "scroll",
        callOnAdd: true,
      });

      instance.listenTo(this.$el, () => {
        if (!this.chart) return;
        this.chart.resize();
      });
    },

    /**
     * 更新echart视图
     */
    updateChartView() {
      if (!this.chart) return;
      const fullOption = this.assembleDataToOption();
      this.chart.setOption(fullOption, true);
      this.chart.on("click", this.clickItem);
    },
    //图标点击事件
    clickItem(param) {
      this.checkName = this.checkName == param.name ? "" : param.name;
      this.chart.setOption(this.assembleDataToOption(), true);
      this.$emit("clickItem", param, this.checkName);
    },

    /**
     * 当窗口缩放时，echart动态调整自身大小
     */
    handleWindowResize() {
      if (!this.chart) return;
      this.chart.resize();
    },
  },
};
</script>

<style lang="scss" scoped>
.custom-image {
  width: 100%;
  height: 100%;
  color: #fff;
  ::v-deep .van-empty__image {
    width: 100%;
    @include flexbox;
    @include flexJC(center);
    img {
      width: 200px;
      height: 200px;
    }
  }
  ::v-deep .van-empty__description {
    text-align: center;
  }
}
.chart {
  width: 100%;
  height: 100%;
}
</style>
