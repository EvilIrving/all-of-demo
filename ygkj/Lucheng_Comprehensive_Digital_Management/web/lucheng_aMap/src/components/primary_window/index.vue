<script>
export default {
  name: "PrimayrWindow",
  props: {
    /** 透明度百分比 */
    opacity: { type: Number, default: 50 },
  },
  render() {
    let style = { backgroundColor: `rgba(0, 0, 0, ${this.opacity / 100})` };
    return (
      <div class="primary-window">
        <div class="primary-window__wrapper" style={style}>
          {this.$slots.default}
        </div>
      </div>
    );
  },
};
</script>
<style lang="scss" scoped>
.primary-window {
  margin-left: auto;
  margin-right: auto;
  background: linear-gradient(to bottom, #35b9e3, #5398f6) left top no-repeat,
    linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
    linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
    linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat,
    linear-gradient(to bottom, #5398f6, #35b9e3) left bottom no-repeat,
    linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
    linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
    linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
  /*设置大小*/
  background-size: 18px 2px, 2px 18px;
  padding: 2px;
  .primary-window__wrapper {
    color: #fff;
    border: 1px solid #00c1ff;
    min-height: 40px;
    box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
    background-image: linear-gradient(
      180deg,
      rgba(53, 186, 227, 0.2) 0%,
      rgba(25, 59, 197, 0.2) 100%
    );

    padding: 0 16px;
  }
}
</style>