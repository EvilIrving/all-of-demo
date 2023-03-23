<script>
export default {
  name: "CapsuleTabs",
  props: {
    value: { type: Number, default: 0 },
    tabs: { type: Array, default: () => [] },
  },
  data() {
    return {
      activeIndex: 0,
    };
  },
  watch: {
    value: {
      deep: true,
      immediate: true,
      handler: function (val, oldVal) {
        this.activeIndex = val;
      },
    },
  },
  render() {
    let tabList = this.tabs.map((item, index) => {
      let tabClass = ["capsule-tabs__item", index == this.activeIndex && "active"];
      return (
        <div
          class={tabClass}
          onClick={() => {
            if (item.disabled) return;
            this.activeIndex = index;
            this.$emit("input", index);
            this.$emit("change", index);
          }}
          style={{ cursor: item.disabled && "not-allowed" }}
        >
          <span>{item.label || item}</span>
        </div>
      );
    });
    return <div class="capsule-tabs">{tabList}</div>;
  },
};
</script>

<style lang="scss" scpoed>
.capsule-tabs {
  display: inline-flex;
  .capsule-tabs__item {
    height: 24px;
    line-height: 24px;
    cursor: pointer;
    user-select: none;
    padding: 0 12px;
    // margin-right: 4px;
    border-radius: 16px;
    color: #fff;
    &.active {
      background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
    }
  }
}
</style>
