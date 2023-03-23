<template>
  <div ref="dragCard" class="drag-card collapse-panel__border" :class="{ 'drag-card-big': isHigh}">
    <div class="drag-crad__content" :style="cardStyle">
      <slot></slot>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
export default {
  name: 'DragCard',
  props: {
    isHigh: {
      type: Boolean,
      default: false
    },
    cardName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      hoverShow: false,
      indexStyle: {}
    };
  },
  mounted() {
    this.SetTempZindex();
    this.$nextTick(() => {
      this.$refs.dragCard.style.zIndex = this.TempZIndex;
    });
  },
  computed: {
    ...mapGetters(['CardHeight', 'HighCardHeight', 'CheckedArr', 'TempZIndex']),
    cardStyle() {
      return {
        height: this.isHigh ? this.HighCardHeight : this.CardHeight
      };
    }
  },
  methods: {
    ...mapActions(['SetCheckedArr', 'SetTempZindex']),
    closeCard() {
      const layerName = this.$slots.default[0]?.componentOptions?.Ctor?.extendOptions?.layerName;
      const legendName = this.$slots.default[0]?.componentOptions?.Ctor?.extendOptions?.legendName;
      const previewModule = this.$slots.default[0]?.componentOptions?.Ctor?.extendOptions?.previewModule;
      this.$EventBus.$emit('clearLayer', { layerName, legendName, previewModule });
      if (this.cardName) {
        let arr = Array.from(this.CheckedArr);
        let idx = arr.findIndex((item) => {
          return item == this.cardName;
        });
        arr.splice(idx, 1);
        this.SetCheckedArr(arr);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.drag-crad__content {
  // box-shadow: inset 0 4px 30px 0 rgba(2, 51, 72, 0.88);
  // background-color: rgba(2, 51, 72, 0.88);
  > div {
    height: 100%;
  }
  width: 100%;
  height: 33.3%;
  display: flex;
  flex-direction: column;
  overflow: scroll;
  padding: 0 16px;
  ::v-deep .panel-item__content {
    display: flex;
    flex-direction: column;
    &::-webkit-scrollbar {
      display: none;
    }
  }

  &::-webkit-scrollbar {
    display: none;
  }
}

.drag-card {
  z-index: 1000;
  width: 400px;
  color: #fff;
  position: relative;
  background: url(~assets/images/ScreenAsset/drag-card-icon.png) no-repeat;
  background-size: 100% 100%;
  background-color: rgba($color: #031a2b, $alpha: 0.9);
  .close-box {
    position: absolute;
    width: 20px;
    height: 20px;
    right: 4px;
    top: 8px;
    cursor: pointer;
  }
}
.drag-card-big {
  background: url(~assets/images/ScreenAsset/drag-big-card-icon.png) no-repeat;
  background-size: 100% 100%;
  background-color: rgba($color: #031a2b, $alpha: 0.9);
}
</style>
