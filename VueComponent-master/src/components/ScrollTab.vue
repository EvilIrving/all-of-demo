<template>
  <div
    id="scrollTab"
    ref="tab"
    @touchstart="startFn"
    @touchmove.prevent="moveFn"
    @touchend="endFn"
  >
    <div
      class="srcoll-tab"
      @transitionend="transEndFn"
      :style="{
        transform: `translateX(${transX}px)`,
        transition: transState ? `transform .35s ease-in-out` : '',
      }"
    >
      <div
        class="item-wrap"
        ref="itemWrap"
        v-for="(item, index) in tabList"
        :key="index"
        @click.stop="onClick(index)"
        :style="{
          width: `${wrapWidth}%`,
          paddingRight: `${wrapPaddingRight}px`,
        }"
      >
        <slot :item="item" :index="index" :activite-index="activiteIndex" />
      </div>
    </div>
  </div>
</template>

<script>
import {isArray} from 'lodash'
export default {
  name: "ScrollTab",
  props: {
    tabList: {
      type: Array,
      default: () => [],
    },
    // 起始tab索引
    startIndex: {
      type: Number,
      default: () => 0,
    },
    // 动画持续时间  ms
    duration: {
      type: Number,
      default: () => 400,
    },
    // 单个tabItem的宽度 %
    wrapWidth: {
      type: Number,
      default: () => 25,
    },
    // 相邻tabItem的间距 px
    wrapPaddingRight: {
      type: Number,
      default: () => 10,
    },
  },
  data() {
    return {
      activiteIndex: 0, // 激活的tabItem索引值
      tabWidth: 0, // scrollTab宽度，px 精准值
      tabItemWrapWidth: 0, //tabWrapItem宽度，px 精准值
      minTranX: 0, // 最小滚动距离，px
      startX: 0, // 起始滑动值,px
      diffX: 0, // 滚动距离差值,px
      preX: 0, // 是否在滑动中
      transX: 0, // 滑动距离,px
      transState: false, // 滑动状态
      timeStamp:0, // 时间戳
    };
  },
  computed: {
    // tabItem数
    tabListLen() {
      return isArray(this.tabList);
    },
    // 是否允许滚动
    isTrans() {
      return this.transState || (this.tabItemWrapWidth * (this.tabListLen - 1) > this.tabWidth);
    },
  },
  created() {
    this.activiteIndex = this.startIndex;
    this.$emit("click", this.activiteIndex);
  },
  mounted() {
    this.init();
    // 根据startIndex值初始化位置
    if (this.isTrans) {
      this.transX = this.preX = this.calcTransX(this.activiteIndex);
    }
    window.addEventListener("resize", this.resetInit);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.resetInit);
  },
  methods: {
    resetInit() {
      this.$nextTick(this.init);
    },
    // 初始化
    init() {
      this.tabWidth = this.$refs.tab.getBoundingClientRect().width;
      this.tabItemWrapWidth = this.$refs.itemWrap[0].getBoundingClientRect().width;
      this.minTranX = this.calcMinTransX(this.tabListLen - 1);
      if (this.isTrans) {
        this.transX = this.preX = this.calcTransX(this.activiteIndex);
      }
    },
    // 滑动开始 - start
    startFn(e) {
      if (!this.isTrans) return;
      this.timeStamp = e.timeStamp;
      this.diffX = 0;
      this.startX = this.getTransX(e);
    },
    // 滑动中 - move
    moveFn(e) {
      if (!this.isTrans) return;
      this.diffX = this.getTransX(e) - this.startX;
      this.transX = this.diffX + this.preX;
    },
    // 滑动结束 - end
    endFn() {
      if (!this.isTrans) return;
      // 修正滑动位置
      this.transX = this.preX  = this.fixTransX(this.transX)
    },
    // 动画结束
    transEndFn() {
      this.transState = false;
    },
      // 点击tab
    onClick(i) {
      this.transState = true;
      this.activiteIndex = i;
      this.transX = this.preX =  this.calcTransX(i);
      this.$emit("click", this.activiteIndex);
    },
    // 修正滑动位置
    fixTransX(transX) {
      if (transX > 0) {
        this.transState = true;
        return 0;
      } else if (transX < this.minTranX) {
        this.transState = true;
        return this.minTranX;
      } else {
        return transX;
      }
    },
    // 获取位置
    getTransX(e) {
      return e.touches[e.touches.length - 1].clientX;
    },
    // 计算点击tabItem滚动到中间的位置
    calcTransX(i) {
      return this.fixTransX(-(
        this.$refs.itemWrap[i].offsetLeft -
        this.tabWidth / 2 -
        this.wrapPaddingRight / 2 +
        this.tabItemWrapWidth / 2
      ))
    },
    // 计算最小滚动位置
    calcMinTransX(i) {
      return -(
        this.$refs.itemWrap[i].offsetLeft +
        this.tabItemWrapWidth -
        this.tabWidth
      );
    },
  },
};
</script>

<style scoped lang="scss">
#scrollTab {
  overflow: hidden;
  .srcoll-tab {
    width: 100%;
    position: relative;
    display: flex;

    .item-wrap {
      flex: none;
      box-sizing: border-box;
      // 修复移动端点击该元素出现黑色闪动
      -webkit-tap-highlight-color: transparent;
    }
  }
}
</style>