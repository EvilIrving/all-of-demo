<template>
  <div class="legend-wrap">
    <p class="legend-title" @click="handleActive" :style="active ? 'border-bottom: none;' : ''">
      <span>{{ title }}</span>
      <i :class="active ? 'el-icon-d-arrow-left' : 'el-icon-d-arrow-right'" style="transform: rotate(-90deg)" />
    </p>
    <div v-show="active">
      <slot></slot>
    </div>
  </div>
</template>
<script>
export default {
  name: 'PrimaryLegend',
  props: {
    title: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      active: true
    };
  },
  methods: {
    handleActive() {
      this.active = !this.active;
    },
    setClose() {
      this.active = false;
    },
    setOpen() {
      this.active = true;
    }
  }
};
</script>

<style lang="scss" scoped>
$titleHeight: 52px;
$itemHeight: 30px;
.legend-wrap {
  width: 100%;
  margin-top: 20px;
  background: url(~assets/images/ScreenAsset/drag-card-icon.png) no-repeat;
  background-size: 100% 100%;
  background-color: rgba($color: #031a2b, $alpha: 0.9);
  .legend-panel {
    width: 100%;
    padding: 8px 20px;
    // background: rgba(2, 22, 47, 0.65);
    // border: 1px solid #239def;
    border-top: none;
    border-top: 0;
    // box-shadow: inset 0px -5px 20px 0px rgba(72, 152, 251, 0.74);
    color: #fff;
    line-height: $itemHeight;
    // max-height: 138px;
    overflow-y: auto;
    border-radius: 0 0 4px 4px;
    .legend-item-box {
      font-size: 14px;
      .legend-list {
        .rainLegend {
          width: 100px;
        }
        span {
          margin-right: 8px;
          display: inline-block;
          width: 80px;
        }
      }
      i {
        margin-right: 11px;
        vertical-align: middle;
        display: inline-block;
        width: 20px;
        height: 20px;
        border-radius: 50%;
      }
    }
  }
  .legend-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    height: $titleHeight;
    color: rgba(255, 255, 255, 0.88);
    font-size: 20px;
    padding: 0 18px;
    // background: linear-gradient(180deg, rgba(2, 111, 224, 0.49) 0%, rgba(2, 80, 224, 0.49) 100%);
    // box-shadow: inset 0px 5px 24px 0px rgba(72, 152, 251, 0.74);
    // border-radius: 4px 4px 0 0;
    // border: 1px solid #239def;
    cursor: pointer;
  }
}
</style>
