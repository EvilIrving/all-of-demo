<script>
export default {
  name: "",
  props: {
    value: { type: Number, default: 0 },
    col: { type: Number, default: 8 },
    list: {
      type: Array,
      default: () => [],
    },
    tabsActive: { type: Boolean, default: true },
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
    return (
      <el-row class="card-tabs-list">
        {this.list.map((item, index) => this.renderCardTabsItem(item, index))}
      </el-row>
    );
  },
  methods: {
    /**
     * @param {Object} item 当前tab的对象 {label,data}
     */
    renderCardTabsItem(item, index) {
      let itemClass = [
        "tabs-list-item__wrapper",
        this.tabsActive && this.activeIndex == index && "active",
      ];
      let clickEvent = () => {
        if (this.activeIndex != index || !this.tabsActive)
          this.activeIndex = index;
        else this.activeIndex = null;
        this.$emit("change", this.activeIndex, this.list[index]);
        this.$emit("input", this.activeIndex);
      };
      let hasRightBorder = (index + 1) % 3 != 0;
      let hasTopBorder = index > 2;
      let singleLine = this.list.length * this.col <= 24;
      // <div class="item-data">
      //   <span class="data-number">
      //     {
      //       Number(item.num) > 0 ? Number(item.num) : "0"
      //     }
      //   </span>
      // </div>
      return (
        <el-col
          class={[
            "tabs-list-item",
            hasTopBorder && "top-border",
            hasRightBorder && "right-border",
            singleLine && "single-line",
          ]}
          span={this.col}
        >
          <div class={itemClass} onClick={clickEvent}>
            <div class="tabs-list-item__content">
              <div>
                <i class={item.icon}></i>
              </div>

              <div class="item-label">{item.label}</div>
            </div>
          </div>
        </el-col>
      );
    },
  },
};
</script>

<style lang="scss" scoped>
.card-tabs-list {
  .tabs-list-item {
    padding: 8px;
    position: relative;
    &.top-border {
      &::before {
        content: "";
        position: absolute;
        top: 0;
        width: 100%;
        border-top: 1px solid #238cef33;
      }
    }
    &.right-border {
      &::after {
        content: "";
        position: absolute;
        top: 0;
        right: 0;
        height: 100%;
        border-right: 1px solid #238cef33;
      }
      &.single-line {
        &::after {
          top: 10%;
          height: 70%;
        }
      }
    }
    .tabs-list-item__wrapper {
      cursor: pointer;
      user-select: none;
      .tabs-list-item__content {
        height: 100%;
        width: 100%;
        min-height: 60px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding-top: 6px;
        .item-data {
          margin: 4px 0;
          .data-number {
            color: #1cfff5;
            font-size: 24px;
            margin-right: 3px;
          }
          .data-unit {
            color: #fff;
            font-weight: bold;
          }
        }
        .item-label {
          font-size: 16px;
          color: #fff;
          margin: 8px 0;
        }
      }
      &.active {
        $color: #1cfff485;
        background: linear-gradient(to bottom, $color, $color) left top
            no-repeat,
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
        .tabs-list-item__content {
          background-color: rgba(28, 255, 245, 0.1);
        }
      }
    }
  }
}
</style>
