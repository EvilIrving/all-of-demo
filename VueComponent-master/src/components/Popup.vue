<template>
  <div id="popup" @touchmove.prevent="()=>{}">
    <transition name="mask">
      <div v-if="value" class="mask" @click.self="close"></div>
    </transition>
    <transition :name="`popup-${popupPosition}`">
      <div v-if="value" :class="['popup',`popup-${popupPosition}`]" :style="{ height: `${popupHeight}px` }">
        <slot/>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: "Popup",
  props: {
    // 控制弹框显示与隐藏
    value: {
      type: Boolean,
      default: () => false,
    },
    // 弹框方向 - 可选值： "top","bottom","left","right","center"
    popupPosition: {
      type: String,
      default: () => "bottom",
    },
    // 弹框高度 - px
    popupHeight: {
      type: Number,
      default: () => 260,
    },
    // 关闭弹框函数
    close: {
      type: Function,
      default: () => {},
    },
  }
};
</script>
<style scoped lang="scss">
#popup {
  .mask {
    position: fixed;
    width: 100%;
    height: 100vh;
    top: 0;
    left: 0;
    opacity: 0.4;
    transition: opacity .2s;
    background-color: $c-black;
  }
  .mask-enter,
  .mask-leave-to {
    opacity: 0;
  }

  .popup {
    position: fixed;
    width: 100%;
    background-color: $c-white;
    backface-visibility: hidden;
    transition: transform .2s ease-in-out;
  }

  $popup-list: (top, 0, 0, auto, auto, 0, 0, 0, -100%, 1, 1),
    (bottom, auto, 0, auto, 0, 0, 0, 0, 100%, 1, 1),
    (left, 50%, 0, auto, auto, 0, -50%, -50%, -50%, 1, 1),
    (right, 50%, auto, 0, auto, 0, -50%, 50%, -50%, 1, 1),
    (center, 50%, 50%, auto, auto, -50%, -50%, -50%, -50%, 1, 0);
  @each $postion, $top, $left, $right, $bottom, $transX, $transY, $transActiveX,
    $transActiveY, $scale, $scaleActive in $popup-list
  {
    .popup-#{$postion} {
      top: $top;
      left: $left;
      right: $right;
      bottom: $bottom;
      transform: translate($transX, $transY) scale($scale);
    }
    .popup-#{$postion}-enter,
    .popup-#{$postion}-leave-to {
      transform: translate($transActiveX, $transActiveY) scale($scaleActive);
    }
  }
}
</style>