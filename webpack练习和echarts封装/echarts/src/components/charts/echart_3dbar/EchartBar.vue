<template>
  <div class="chart"></div>
</template>

<script>
import { merge } from 'lodash';
import { BASIC_OPTION, COLOR_ARRAY } from './default_option';
import ResizeListener from 'element-resize-detector';
import * as echarts from 'echarts';
export default {
  name: 'ChartLine',
  props: {
    // 正常的业务数据，对应echarts饼图配置中series[0].data
    seriesData: {
      type: Array,
      default: () => []
    },
    // 表示需要特殊定制的配置
    // 一般UI会规定一个统一的设计规范（比如颜色，字体，图例格式，位置等）
    // 但不排除某个图标会和设计规范不同，需要特殊定制样式，所以开放这个配置，增强灵活性
    extraOption: {
      type: Object,
      default: () => ({})
    }
  },
  components: {},
  computed: {},
  data() {
    return {
      chart: null
    };
  },
  watch: {
    seriesData: {
      deep: true,
      handler() {
        this.updateChartView();
      }
    },
    extraOption: {
      deep: true,
      handler() {
        this.chart.resize();
        this.updateChartView();
      }
    }
  },
  mounted() {
    this.chart = echarts.init(this.$el);
    this.updateChartView();
    window.addEventListener('resize', this.handleWindowResize);
    this.addChartResizeListener();
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleWindowResize);
  },
  methods: {
    /**
     * 将业务数据加入到基础样式配置中
     * @returns {Object} 完整的echart配置
     */
    assembleDataToOption() {
      return merge(
        {},
        BASIC_OPTION,
        { color: COLOR_ARRAY },
        {
          series: this.seriesData
        },
        this.extraOption
      );
    },

    /**
     * 对chart元素尺寸进行监听，当发生变化时同步更新echart视图
     */
    addChartResizeListener() {
      const instance = ResizeListener({
        strategy: 'scroll',
        callOnAdd: true
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
      this.chart.on('click', this.clickItem);
    },
    //图标点击事件
    clickItem(param) {
      // console.log(param);
      this.$emit('clickItem', param);
    },

    /**
     * 当窗口缩放时，echart动态调整自身大小
     */
    handleWindowResize() {
      if (!this.chart) return;
      this.chart.resize();
    }
  }
};
</script>

<style lang="scss" scoped>
.custom-image {
  width: 100%;
  height: 100%;
  color: #fff;
  ::v-deep .van-empty__image {
    width: 100%;
    display: flex;
    justify-content: center;
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
