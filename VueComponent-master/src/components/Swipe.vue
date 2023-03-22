<template>
  <div
    id="swipe"
    @touchstart.prevent="startFn"
    @touchmove.prevent="moveFn"
    @touchend.prevent="endFn"
  >
    <div
      class="swipe"
      ref="swipe"
      :style="{
        transform: `translateX(${transX}px)`,
        transition: isTrans ? `transform .35s ease-out` : '',
      }"
      @transitionend="transEndFn"
    >
      <div
        class="swipe-item-wrapper"
        v-for="(item, index) in currentList"
        :key="index"
        :style="{ padding: `0 ${wrapperPadding}px` }"
      >
        <slot :item="item" :id="index"></slot>
      </div>
    </div>
    <div
      v-if="showIndicators && itemCount > 1"
      class="indicator"
      :class="`indicator-${indicatorPosition}`"
    >
      <div
        class="indicator-item"
        v-for="(item, index) in list"
        :key="index"
        :class="{ 'indicator-item-active': index === actuallyActiveIndex }"
      ></div>
    </div>
  </div>
</template>

<script>
import {isArray,cloneDeep} from 'lodash'
export default {
  name: "Swipe",
  props: {
    // 图片列表
    list: {
      type: Array,
      default: () => [],
    },
    // 起始索引
    startIndex: {
      type: Number,
      default: () => 0,
    },
    // 临界比，0-1之间小数
    cirticalVal: {
      type: Number,
      default: () => 1 / 5,
    },
    // 是否开启自动轮播
    autoPlay: {
      tpye: Boolean,
      default: () => false,
    },
    // 是否为连续滑动 ,true
    continuous: {
      type: Boolean,
      default: () => true,
    },
    // 自动轮播时间，ms
    autoPlayTime: {
      type: Number,
      default: () => 3000,
    },
    // 是否显示指示器
    showIndicators: {
      type: Boolean,
      default: () => false,
    },
    // 指示器位置，可选值为：'top-left' 'top-center' 'top-right' 'bottom-left' 'bottom-center' 'bottom-right'
    indicatorPosition: {
      type: String,
      default: () => "bottom-right",
    },
    // swipeItem包裹容器内边距,px
    wrapperPadding: {
      type: Number,
      default: () => 0,
    },
  },
  data() {
    return {
      currentList: null, // 处理后的list
      activeIndex: 0, // 索引值
      actuallyActive: 0, // 实际索引值
      itemCount: 0, // 处理后item数量
      actuallyItemCount: 0, // 实际item数量
      transX: 0, // 滑动距离,px
      preX: 0, // 初始滑动距离,px
      startX: 0, // 起始滑动值,px
      diffX: 0, // 滚动距离差值,px
      minTransX: 0, // 最小滚动临界值,px
      isTrans: false, // 是否在滑动中
      swipeWidth: 0, // 单个swipeItem的宽度,px(精确值)
      timeId: null, // timeId
    };
  },
  computed: {
    // 是否忽略滑动 - 正在滑动中或只有一个item
    ignoreTrans() {
      return this.isTrans || this.actuallyItemCount === 1;
    },
    // 是否开启自动轮播
    isAutoPlay() {
      return this.autoPlay && this.continuous && this.actuallyItemCount > 1;
    },
    // 是否是临界距离
    isCritical() {
      return this.continuous && (this.transX >= 0 || this.transX <= this.minTransX);
    },
  },
  created() {
    // 数据处理
    this.actuallyItemCount = isArray(this.list);
    if (this.actuallyItemCount === 0) return;
    if (this.continuous && this.actuallyItemCount > 1) {
      this.currentList = cloneDeep(this.list.slice(-1).concat(this.list).concat(this.list.slice(0, 1)));
    } else {
      this.currentList = cloneDeep(this.list);
    }
    this.itemCount = this.currentList.length;
    this.activeIndex = this.actuallyActiveIndex = this.startIndex;
    this.activeIndex = this.continuous ? this.activeIndex + 1 : this.activeIndex;
    this.$emit("change", this.actuallyActiveIndex);
  },
  mounted() {
    // 当轮播图数量只有一张时候
    if (this.actuallyItemCount === 1) return;
    this.init();
    // 监听页面大小发生变化，重新初始化参数
    window.addEventListener("resize", this.resetInit);
    this.isAutoPlay && this.autoPlayFn();
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.resetInit);
    clearInterval(this.timeId);
  },
  methods: {
    resetInit(){
      this.$nextTick(this.init)
    },
    // 初始化
    init() {
      this.swipeWidth = this.$refs.swipe && this.$refs.swipe.getBoundingClientRect().width;
      this.minTransX = -(this.itemCount - 1) * this.swipeWidth;
      this.transX = this.preX = this.calcTrans();
    },
    // 滑动开始 - start
    startFn(e) {
      this.isAutoPlay && clearInterval(this.timeId);
      if (this.ignoreTrans) return;
      this.diffX = 0;
      this.startX = this.getTransX(e);
    },
    // 滑动中 - move
    moveFn(e) {
      // 临界值处理 - 滑动到一张
      if (this.ignoreTrans || this.isCritical) return;
      // 时时滑动
      this.diffX = this.getTransX(e) - this.startX;
      this.transX = this.diffX + this.preX;
    },
    // 获取触控X轴位置
    getTransX(e) {
      return e.touches[e.touches.length - 1].clientX;
    },
    // 滑动结束 - end
    endFn() {
      if (this.ignoreTrans) return;
      const diffXAbs = Math.abs(this.diffX); // 滑动距离绝对值
      const itemCount = Math.trunc(diffXAbs / this.swipeWidth); // 滑过轮播图个数
      const critialVal = diffXAbs / this.swipeWidth - itemCount; // 滑动临界值比例
      // 左滑
      if (this.diffX > 0) {
        this.slideLeft(itemCount, critialVal);
      } else {
        // 右滑
        this.slideRight(itemCount, critialVal);
      }
      // 获取实际的索引值
      this.actuallyActiveIndex = this.continuous
        ? this.getIndex(this.activeIndex) - 1
        : this.activeIndex;
      // 当手指滑动距离正好等于整数个item时候
      if (critialVal === 0) {
        this.transEndFn();
      } else {
        this.isTrans = true;
        this.transX = this.preX = this.calcTrans();
      }
    },
    // 左滑
    slideLeft(itemCount, critialVal) {
      this.activeIndex -= itemCount;
      if (critialVal >= this.cirticalVal) {
        this.activeIndex -= 1;
      }
      this.activeIndex = this.activeIndex < 0 ? 0 : this.activeIndex;
    },
    // 右滑
    slideRight(itemCount, critialVal) {
      this.activeIndex += itemCount;
      if (critialVal >= this.cirticalVal) {
        this.activeIndex += 1;
      }
      this.activeIndex = this.activeIndex > this.itemCount - 1 ? this.itemCount - 1 : this.activeIndex;
    },
    // 动画结束 - end
    transEndFn() {
      this.isTrans = false;
      // 当滑动到最后或者第一张时候，瞬间拉回回当前轮播图实际位置
      if (this.continuous) {
        this.activeIndex = this.getIndex(this.activeIndex);
        this.transX = this.preX = this.calcTrans();
      }
      this.$emit("change", this.actuallyActiveIndex);
      this.isAutoPlay && this.autoPlayFn();
    },
    // 计算滑动位置
    calcTrans() {
      return -this.activeIndex * this.swipeWidth;
    },
    // 计算索引位置
    getIndex(i) {
      switch(i) {
        case 0:
          return this.itemCount - 2;
        case this.itemCount - 1:
          return 1;
        default:
          return i;
      }
    },
    // 自动轮播
    autoPlayFn() {
      clearInterval(this.timeId);
      this.timeId = setInterval(() => {
        this.slideRight(1, 0);
        this.actuallyActiveIndex = this.getIndex(this.activeIndex) - 1;
        this.isTrans = true;
        this.transX = this.preX = this.calcTrans();
      }, this.autoPlayTime);
    },
  },
};
</script>
<style lang="scss" scoped>
#swipe {
  position: relative;
  width: 100%;
  font-size: 0;
  overflow: hidden;
  touch-action: none;
  @include ios-fix;
  .swipe {
    display: flex;
    @include ios-fix;
    .swipe-item-wrapper {
      flex: none;
      width: 100%;
      box-sizing: border-box;
      @include ios-fix;
    }
  }
  .indicator {
    position: absolute;
    display: flex;

    // 指示器位置
    $indicator-position-list: (top-left, $px-8, auto, auto, $px-8, 0),
      (top-center, $px-8, auto, auto, 50%, -50%),
      (top-right, $px-8, $px-8, auto, auto, 0),
      (bottom-left, auto, auto, $px-8, $px-8, 0),
      (bottom-center, auto, auto, $px-8, 50%, -50%),
      (bottom-right, auto, $px-8, $px-8, auto, 0);

    @each $position, $top, $right, $bottom, $left,
      $trans-x in $indicator-position-list
    {
      &.indicator-#{$position} {
        top: $top;
        right: $right;
        bottom: $bottom;
        left: $left;
        transform: translate($trans-x, 0);
      }
    }
    .indicator-item {
      width: $px-8;
      height: $px-8;
      border-radius: 100%;
      background-color: $c-white;
      opacity: 0.6;
      margin-right: $px-4;

      &.indicator-item-active {
        opacity: 1;
      }
    }
  }
}
</style>