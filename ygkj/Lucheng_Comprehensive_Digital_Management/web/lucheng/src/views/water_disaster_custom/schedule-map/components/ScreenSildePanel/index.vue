<template>
  <div
    :class="['collapse-panel', position]"
    :style="{
      top: type(top) == 'number' ? `${top}px` : top,
      height: type(height) == 'number' ? `${height}px` : height
    }"
  >
    <div :class="['collapse-panel__button', collapse ? 'open' : 'close']" @click="handleCollapse" />

    <div ref="wrapper" :style="collapseStyle" :class="['collapse-panel__wrapper']">
      <slot name="slot-panel">
        <div>
          <div class="">
            <div class="collapse-panel__content">
              <slot></slot>
            </div>
          </div>
        </div>
      </slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ScreenSildePanel',
  props: {
    value: { type: Boolean, default: true },
    top: { type: [Number, String] },
    height: { type: [Number, String] },
    position: { type: String, default: 'right' }
  },
  data() {
    return {
      collapse: true,
      offsetWidth: 0
    };
  },
  watch: {
    value: {
      immediate: true,
      handler(val) {
        this.collapse = val;
        this.$emit('input', this.collapse);
        this.$emit('change', this.collapse);
      }
    }
  },
  computed: {
    collapseStyle() {
      let style = {};
      if (this.position == 'left') {
        style.marginLeft = this.collapse ? '0' : `-${this.offsetWidth}px`;
        style.paddingLeft = '0px';
      } else {
        style.marginRight = this.collapse ? '0' : `-${this.offsetWidth}px`;
        style.paddingRight = '20px';
      }
      return style;
    }
  },
  mounted() {
    const resizeObserver = new ResizeObserver((entries) => {
      for (let entry of entries) {
        this.offsetWidth = entry.target.offsetWidth;
      }
    });
    resizeObserver.observe(this.$refs.wrapper);
  },
  methods: {
    type(param) {
      return typeof param;
    },
    /**
     * 处理collapse改变并将事件冒泡
     */
    handleCollapse() {
      this.collapse = !this.collapse;
      this.$emit('input', this.collapse);
      this.$emit('change', this.collapse);
    }
  }
};
</script>

<style lang="scss">
.collapse-panel {
  top: 140px;
  position: absolute;
  z-index: 100;
  display: flex;
  align-items: center;
  // transform: scaleX(0.5);
  &.left {
    flex-direction: row-reverse;
    transform-origin: left;
    .collapse-panel__button {
      margin-left: -3px;
    }
    .collapse-panel__wrapper {
      float: left;
    }
  }
  &.right {
    right: 0;
    transform-origin: right;
    .collapse-panel__button {
      margin-right: -3px;
      transform: rotate(180deg);
    }
    .collapse-panel__wrapper {
      float: right;
    }
  }
  .collapse-panel__button {
    cursor: pointer;
    height: 69px;
    width: 37px;
    &.open {
      background-image: url(~assets/images/ScreenAsset/collapse-close-new.png);
      background-repeat: no-repeat;
      background-size: 100% 100%;
    }
    &.close {
      background-image: url(~assets/images/ScreenAsset/collapse-open-new.png);
      background-repeat: no-repeat;
      background-size: 100% 100%;
    }
  }
  .collapse-panel__wrapper {
    height: 100%;
    transition: all 0.5s ease-in-out;
    display: flex;
    background: url(~assets/images/ScreenAsset/panel-info-icon.png) no-repeat;
    background-size: 100% 100%;
    > div {
      display: flex;
    }
    .collapse-panel__content {
      // border: 1px solid #00c1ff;
      border-radius: 6px;
      // box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
      // background-color: rgba(0, 0, 0, 0.8);
      // background-image: linear-gradient(180deg, rgba(2, 51, 72, 0.88) 2%, rgba(2, 51, 72, 1));
      height: 100%;
      width: 100%;
      padding: 20px 24px;
      display: flex;
      flex-direction: column;
      overflow: scroll;
      border: none;
      box-shadow: none;
      background-color: transparent;
      background-image: none;
      &::-webkit-scrollbar {
        display: none;
      }
    }
    .collapse-panel__border {
      height: 100%;
      width: 100%;
      // background: linear-gradient(to bottom, #35b9e3, #5398f6) left top no-repeat,
      //   linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
      //   linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
      //   linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat,
      //   linear-gradient(to bottom, #5398f6, #35b9e3) left bottom no-repeat,
      //   linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
      //   linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
      //   linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
      /*设置大小*/
      background-size: 18px 2px, 2px 18px;
      padding: 2px;
    }
  }
}
</style>
