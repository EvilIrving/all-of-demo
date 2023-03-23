<template>
  <div class="legend-wrap">
    <p
      class="legend-title"
      @click="handleActive"
      :style="active ? 'border-bottom: none;' : ''"
    >
      <span>{{ title }}</span>
      <i
        :class="active ? 'el-icon-d-arrow-left' : 'el-icon-d-arrow-right'"
        style="transform: rotate(-90deg)"
      />
    </p>
    <div v-show="active">
      <slot></slot>
    </div>
  </div>
</template>
<script>
export default {
  name: "PrimaryLegend",
  props: {
    title: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      active: true,
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
    },
  },
};
</script>

<style lang="scss" scoped>
$titleHeight: 52px;
$itemHeight: 30px;
.legend-wrap {
  width: 100%;
  // margin-top: 20px;
  ul {
    width: 100%;
    max-height: 268px;
    overflow-y: auto;
    padding: 8px 20px;
    background: linear-gradient(180deg, rgba(6, 29, 54, 0.6) 0%, rgba(5, 24, 50, 0.7) 100%);
    border: 1px solid #00C1FF;
    border-top: none;
    border-top: 0;
    box-shadow: inset 0px -5px 20px 0px rgba(72, 152, 251, 0.74);
    color: #fff;
    line-height: $itemHeight;
    // max-height: 138px;
    overflow-y: auto;
    border-radius: 0 0 4px 4px;
    li {
      font-size: 14px;
      padding-bottom: 15px;
      margin-bottom: 15px;
      border-bottom: 1px solid #687586;

      &:last-child {
        margin-bottom: 0;
        border-bottom: none;
      }
      .legend-name {
        // width: 56px;
        margin-right: 15px;
        color: #1df8f3;
      }
      .legend-list {
        .rainLegend {
          display: inline-block;
          min-width: 50%;
        }
      }
      i {
        margin-right: 11px;
        vertical-align: middle;
        display: inline-block;
        width: 20px;
        height: 20px;
      }

      //海塘图例
      .seawall-type-1 {
        background-color: #e80000;
        @include icon();
      }
      .seawall-type-10 {
        background-color: #dd00db;
        @include icon();
      }
      .seawall-type-20 {
        background-color: #e89100;
        @include icon();
      }
      .seawall-type-50 {
        background-color: #eec80b;
        @include icon();
      }
      .seawall-type-100 {
        background-color: #19af47;
        @include icon();
      }
      .seawall-type-101 {
        background-color: #4cebc7;
        @include icon();
      }
      .seawall-type-none {
        background-color: #8d0a0a;
        @include icon();
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
    background: linear-gradient(180deg, rgba(6, 29, 54, 0.6) 0%, rgba(5, 24, 50, 0.7) 100%);
    border: 1px solid #00C1FF;
    border-radius: 4px 4px 0 0;
    cursor: pointer;
  }
}
</style>
