<script>
export default {
  name: "PagePaneItem",
  props: {
    name: { type: String, require: true },
    label: { type: String, require: true },
    /** 控制切换是否重新渲染触发所有生命周期 */
    pageRerender: { type: Boolean, default: false },
  },
  inject: ["page"],
  computed: {
    active() {
      const active = this.page.activeName === this.name;
      return active;
    },
  },
  render() {
    let { $slots, pageRerender } = this;
    let render = (
      <div class="page-pane-item" style={!this.active && { display: "none" }}>
        <div class="page-pane-item__content">{$slots.default}</div>
      </div>
    );
    if (pageRerender) return active && render;
    return render;
  },
};
</script>
<style>
.page-pane-item {
  height: 100%;
}
.page-pane-item .page-pane-item__content {
  height: 100%;
}
</style>
