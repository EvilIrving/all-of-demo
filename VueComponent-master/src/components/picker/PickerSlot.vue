<template>
  <div id="pickerSlot">
    <div
      class="slot-content"
      :style="{
        transform: `translate3d(0,${transY}px,0)`,
        transition:
          !transState && isTrans
            ? `transform 800ms cubic-bezier(0,0,0.25,1)`
            : '',
      }"
      @transitionend="transEnd"
    >
      <div
        v-for="(sItem, index) in slotList"
        :key="index"
        class="slot-item"
        :class="{ 'active-slot-item': index === activeIndex }"
        :style="{ height: `${itemHeight}px`, lineHeight: `${itemHeight}px` }"
      >
        {{ sItem.name }}
      </div>
    </div>
    <div
      class="slot-area"
      @touchstart="startFn"
      @touchmove="moveFn"
      @touchend="endFn"
    ></div>
  </div>
</template>
<script>
export default {
  name: "PickerSolt",
  props: {
    value: {
      type: Object,
      default: () => {},
    },
    slotMap: {
      type: Map,
      default: () => new Map(),
    },
    itemHeight: {
      type: Number,
      default: () => 34,
    },
    rows: {
      type: Number,
      default: () => 7,
    },
    isLink: {
      type: Boolean,
      default: () => false,
    },
  },
  data() {
    return {
      preY: 0, // 初始化位置，px
      transY: 0, // 目的位置，px
      startY: 0, // 起始滑动位置，px
      diffY: 0, // 手指滑动距离，px
      activeIndex: 0, // 激活索引
      criticalVal: 0.3, // 临界比
      transState: false, // 滑动动画状态
      isTrans: false, // 是否在滑动中
      lastTimeStamp: 0, // 时间戳
    };
  },
  watch: {
    "value.parent": {
      handler(newVal, oldVal) {
        if (!this.isLink) return;
        if (newVal !== oldVal) {
          this.activeIndex = 0;
          this.isTrans = true;
        }
        this.transY = this.initY = this.calcTransY();
      },
      deep: true,
    },
  },
  computed: {
    slotList() {
      return this.slotMap.get(this.value.parent);
    },
    slotListLen() {
      return this.slotList.length;
    },
    // 滑动初始值 - init
    initTransY() {
      return Math.trunc(this.rows / 2) * this.itemHeight;
    },
    // 滑动临界值 - top
    topTransY() {
      return this.initTransY + this.criticalVal * this.itemHeight;
    },
    // 滑动临界值 - bottom
    bottomTransY() {
      return (
        this.initTransY -
        (this.slotListLen - (1 - this.criticalVal)) * this.itemHeight
      );
    },
  },
  created() {
    this.activeIndex = this.slotList.findIndex((item) => {
      return item.value === this.value.value;
    });
    this.transY = this.preY = this.calcTransY();
  },
  methods: {
    // 滑动开始 - start
    startFn(e) {
      this.diffY = 0;
      this.lastTimeStamp = e.timeStamp;
      this.transState = true;
      this.startY = this.calcClientY(e);
    },
    // 滑动 - move
    moveFn(e) {
      e.preventDefault();
      if (!this.transState) return;
      this.diffY = this.calcClientY(e) - this.startY;
      // 滑动临界值处理 - top
      if (this.transY >= this.topTransY) {
        this.handleCricitalTransY(this.topTransY, 0);
      } else if (this.transY <= this.bottomTransY) {
        // 滑动临界值处理 - bottom
        this.handleCricitalTransY(this.bottomTransY, this.slotListLen - 1);
      } else {
        // 倍速滑动
        this.diffY *= 1.3;
        this.transY = this.diffY + this.preY;
      }
    },
    // 滑动 - end
    endFn(e) {
      this.isTrans = true;
      this.transState = false;
      let diffYAbs = Math.abs(this.diffY);
      if (e.timeStamp - this.lastTimeStamp <= 150) diffYAbs *= 4;
      const itemNum = Math.trunc(diffYAbs / this.itemHeight);
      const criticalVal = diffYAbs / this.itemHeight - itemNum;
      if (this.diffY > 0) {
        this.activeIndex -= itemNum;
        if (criticalVal >= this.criticalVal) {
          this.activeIndex -= 1;
        }
        this.activeIndex = this.fixIndex(this.activeIndex, true);
      } else if (this.diffY < 0) {
        this.activeIndex += itemNum;
        if (criticalVal >= this.criticalVal) {
          this.activeIndex += 1;
        }
        this.activeIndex = this.fixIndex(this.activeIndex, false);
      }
      this.transY = this.preY = this.calcTransY();
      this.$emit("input", this.slotList[this.activeIndex]);
    },
    // 动画结束 - end
    transEnd() {
      this.isTrans = false;
    },
    // 鼠标进入 - enter
    enterFn() {
      this.transState = false;
    },
    // 鼠标离开 - leave
    leaveFn(e) {
      this.transState && this.endFn(e);
    },
    // 计算滑动值
    calcTransY() {
      return this.initTransY - this.activeIndex * this.itemHeight;
    },
    // 修复索引
    fixIndex(i, isTop) {
      if (isTop) {
        return i < 0 ? 0 : i;
      } else {
        return i > this.slotListLen - 1 ? this.slotListLen - 1 : i;
      }
    },
    // 处理临界滑动值
    handleCricitalTransY(criticalTransY, activeIndex) {
      this.diffY = 0;
      this.activeIndex = activeIndex;
      this.transY = criticalTransY;
    },
    calcClientY(e) {
      if (e.type === "mousedown" || e.type === "mousemove") {
        return e.clientY;
      } else {
        return e.touches[e.touches.length - 1].clientY;
      }
    },
  },
};
</script>
<style lang="scss" scoped>
@mixin common-style {
  top: 0;
  right: auto;
  bottom: auto;
  left: 0;
  width: 100%;
  height: 100%;
}
#pickerSlot {
  position: relative;
  @include common-style;
  display: flex;
  overflow: hidden;
  .slot-content {
    position: absolute;
    top: 0;
    right: auto;
    bottom: auto;
    left: 0;
    width: 100%;
    .slot-item {
      font-size: $px-16;
      text-align: center;
      box-sizing: border-box;

      &.active-slot-item {
        font-size: $px-18;
      }
    }
  }
  .slot-area {
    position: absolute;
    @include common-style;
    z-index: $z-index-lg;
    background-color: transparent;
  }
}
</style>