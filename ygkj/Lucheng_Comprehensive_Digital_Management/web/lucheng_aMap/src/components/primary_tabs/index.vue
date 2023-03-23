<template>
  <div v-if="list.length > 0" class="primary-tabs-wrapper">
    <div @scroll="scroll" class="tab-content">
      <div ref="tabContentScroll" class="tab-content-scroll">
        <div class="tab">
          <div
            ref="TabItem"
            v-for="(item, index) in list"
            :key="index"
            @mouseout="hoverShow = -1"
            @mouseover="hoverShow = index"
            :class="['tab__item', currentIndex === index && 'active']"
            @click="select(item, index)"
          >
            <div class="tab__item-title">
              <slot :title="item" name="title"></slot>
            </div>
            <div v-if="!showTitleSlot" class="tab__item-title">
              {{ item.name }}
            </div>
            <span
              v-show="list.length > 1 && hoverShow == index"
              class="tab-close"
              @click.stop="closeTabHandle(item)"
              >×</span
            >
          </div>
        </div>
      </div>
      <el-button
        v-if="showReturnButton"
        class="tab-return-button"
        @click="handleReturnButton"
      >
        返回
      </el-button>
    </div>

    <i class="split-line icon-split-line1"></i>
  </div>
</template>

<script>
export default {
  name: "PrimaryTabs",
  props: {
    value: {
      type: [Number, String],
      default: null,
    },
    list: {
      type: Array,
      default: () => {
        return [];
      },
    },
    showReturnButton: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      currentIndex: 0,
      currentItem: {},
      scrollLeft: 0,
      hoverShow:-1,
    };
  },
  computed: {
    showTitleSlot() {
      return this.$scopedSlots.title;
    },
  },
  watch: {
    list() {
      this.setTabList();
    },
    value(newVal) {
      this.currentIndex = newVal;
      this.setTabList();
    },
  },
  mounted() {
    this.currentIndex = this.value;
  },
  methods: {
    select(item, index) {
      this.$emit("input", index);
      this.currentIndex = index;
      this.currentItem = item;
      this.scroll();
    },
    setTabList() {
      this.$nextTick(() => {
        if (this.list.length > 0) {
          this.scrollIntodiv();
        }
      });
    },
    // item滚动计算
    scrollIntodiv() {
      this.getElementData((data) => {
        const item = data[this.currentIndex];
        // 滚动父级元素宽度
        // const tabContentWidth = this.$refs.tabContentScroll.scrollWidth
        // 父级元素可视区域宽度
        const screenWdith = this.$refs.tabContentScroll.offsetWidth;
        // item的宽度
        const itemWidth = item.width;
        // item距离最左侧的距离
        const itemOffsetLeft = item.left;
        // item中间值 = (父级可视区域宽度 - item的宽度) / 2
        const itemCenter = (screenWdith - itemWidth) / 2;
        // item目标值 = 当前item左移值 - 中间值
        const itemTarget = itemOffsetLeft - itemCenter;

        // 目标值小于0时，滚动值设置为0
        if (itemTarget < 0) {
          this.$refs.tabContentScroll.scrollLeft = 0;
          return;
        }
        /* // 屏幕宽度
        const winWidth = window.innerWidth;
        // 目标值小于屏幕宽减去父级可是区域宽时，滚动值设置为 父级可是区域宽-屏幕宽+元素宽
        if (itemTarget < winWidth - screenWdith) {
          this.$refs.tabContentScroll.scrollLeft = screenWdith - winWidth + itemWidth
          return
        } */
        this.$refs.tabContentScroll.scrollLeft = itemTarget;
      });
    },
    getElementData(callback) {
      const domArr = this.$refs.TabItem;
      const arr = [];
      domArr.forEach((e) => {
        arr.push({
          id: "TabItem",
          left: e.offsetLeft,
          width: e.offsetWidth,
          text: e.innerText,
        });
      });
      callback(arr);
    },
    scroll() {
      this.scrollLeft = this.$refs.tabContentScroll.scrollLeft;
    },
    handleReturnButton() {
      this.$emit("return");
    },
    closeTabHandle(val) {
      this.$emit("closeTab", val);
    },
  },
};
</script>
<style lang="scss">
@import "@/style/theme.scss";
.primary-tabs-wrapper {
  position: relative;
  font-size: 14px;
  height: 100%;
  // border: 1px solid #dfe0e0;
  border-bottom: 0;
  width: 100%;
  .split-line {
    display: inline-block;
    width: 100%;
    height: 4px;
  }
  .tab-content {
    display: flex;
    justify-content: space-between;
    .tab-content-scroll {
      transition: all 0.5s linear;
      overflow: auto;
      scrollbar-width: none; /* Firefox */
      -ms-overflow-style: none; /* IE 10+ */
    }
    .tab-content-scroll::-webkit-scrollbar {
      display: none; /* Chrome Safari */
    }
    .tab-return-button {
      width: 88px;
      height: 32px;
      line-height: 32px;
      color: #fff;
      padding: 0;
      background: #239def;
      border: 0;
      border-radius: 4px;
    }
  }
  .tab {
    position: relative;
    display: flex;
    height: 100%;
    white-space: nowrap;
    &__item {
      line-height: 90px;
      position: relative;
      padding: 5px 14px 5px 12px;
      margin-right: 16px;
      cursor: pointer;
      color: #fff;
      background: url(~assets/images/tab-title-unselect1.png) no-repeat center
        center;
      background-size: 100% 80%;
      &.active {
        color: #56fefe;
        font-weight: bold;
        background: url(~assets/images/tab-title-select1.png) no-repeat center
          center;
        background-size: 100% 80%;
      }
      &-title {
        letter-spacing: 1px;
        font-size: 16px;
        line-height: 28px;
        font-weight: normal;
      }
    }
    &-close {
      position: absolute;
      top: 4px;
      right: 3px;
      line-height: normal;
      &:hover {
        color: #56fefe;
      }
    }
  }
  .tab_line {
    display: block;
    height: 4px;
    position: absolute;
    bottom: 0px;
    left: 0;
    z-index: 1;
    position: relative;
    background: $primaryColor;
  }
}
</style>