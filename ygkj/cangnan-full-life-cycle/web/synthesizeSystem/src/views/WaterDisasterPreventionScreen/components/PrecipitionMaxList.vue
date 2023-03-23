<script>
export default {
  name: "PrecipitionMaxList",
  props: { maxList: { type: Array }, active: { type: Boolean, default: true } },
  data() {
    return {
      activeList: [],
    };
  },
  watch: {
    maxList: {
      immediate: true,
      handler(val) {
        this.activeList.forEach((item, index) => {
          // console.log(this.maxList, this.maxList[index].data, this.activeList, item);
          this.$emit("change", true, this.maxList[index].data);
        });
      },
    },
  },
  methods: {
    renderItem(item, index) {
      let cls = [
        "precipition-max-item",
        !this.maxList[index].disabled &&
          this.activeList.includes(index) &&
          this.active &&
          "active",
      ];
      return (
        <div
          class={cls}
          onClick={() => {
            if (!this.active) {
              this.$emit("active");
              this.activeList = [];
            }
            this.handleClick(index);
          }}
          style={{ cursor: this.maxList[index].disabled && this.active && "unset" }}
        >
          <span class="label">
            <i class="icon-diamond icon-sty" />
            {item.label}
          </span>
          {item.data.name || ""}&nbsp;&nbsp;
          <span class="number">
            {!Number.isNaN(item.data.value) ? item.data.value : "0"}
            <span class="label">mm</span>
          </span>
        </div>
      );
    },
    handleClick(index) {
      const find = this.activeList.indexOf(index);
      if (find != -1) this.activeList.splice(find, 1);
      else this.activeList.push(index);
      this.$emit("change", this.activeList.includes(index), this.maxList[index].data);
    },
  },
  render() {
    return (
      <div class="precipition-max-list">
        {this.maxList.map((item, index) => {
          return this.renderItem(item, index);
        })}
      </div>
    );
  },
};
</script>
<style lang="scss" scoped>
.precipition-max-list {
  .precipition-max-item {
    user-select: none;
    cursor: pointer;
    position: relative;
    width: 100%;
    padding: 10px;
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
    background-image: linear-gradient(90deg, #00c1ff33 0%, #ffffff00 100%);
    align-items: center;
    font-size: 18px;
    color: #1ef8f2;
    font-weight: bold;
    .label {
      color: #fff;
      font-weight: normal;
      flex: 2;
      margin-left: 3px;
    }
    .icon-diamond {
      display: inline-block;
      height: 24px;
      width: 24px;
      background-image: url(~assets/images/WaterDisastersImage/diamond.gif);
    }
    .number {
      margin-left: 6px;
    }
    i {
      vertical-align: middle;
    }
    &.active {
      &:after {
        $color: #1cfff485;
        position: absolute;
        content: "";
        width: 100%;
        height: 100%;
        left: 0;
        background: linear-gradient(to bottom, $color, $color) left top no-repeat,
          linear-gradient(to bottom, $color, $color) left top no-repeat,
          linear-gradient(to bottom, $color, $color) right top no-repeat,
          linear-gradient(to bottom, $color, $color) right top no-repeat,
          linear-gradient(to bottom, $color, $color) left bottom no-repeat,
          linear-gradient(to bottom, $color, $color) left bottom no-repeat,
          linear-gradient(to bottom, $color, $color) right bottom no-repeat,
          linear-gradient(to bottom, $color, $color) right bottom no-repeat;
        /*设置大小*/
        background-size: 12px 2px, 2px 12px;
        border-radius: 3px;
      }
    }
  }
}
</style>
