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
    //小数保留标记
    decimalsFlag: {
      type: Boolean,
      default: false,
    },
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
        console.log(this.activeIndex)
      },
    },
  },
  render() {
    // console.log(this.list,222);
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
          <div class={itemClass} onClick={clickEvent} >
            <div class="tabs-list-item__content">
              <i
                class={
                  item.label < "4级堤防"
                    ? "icon-two-dikes"
                    : item.label == "4级堤防"
                    ? "icon-four-dikes"
                    : "icon-five-dikes"
                }
              ></i>
              <div class="item-data" >
                <span class="data-number" style={{ color: item.data.color }}>
                  {this.decimalsFlag
                    ? Number(item.data.number).toFixed(1) > 0
                      ? Number(item.data.number).toFixed(1)
                      : "0"
                    : Number(item.data.number) > 0
                    ? Number(item.data.number)
                    : "0"}
                </span>
                <span class="data-unit">{item.data.unit}</span>
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
margin-top:-120px;
  .tabs-list-item {
    // padding: 8px;
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
      padding: 2px;
      .tabs-list-item__content {
        height: 100%;
        width: 100%;
        min-height: 60px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        .item-data {
          margin-top: 8px;
          margin-bottom: 16px;
          .data-number {
            color: #1cfff5;
            font-size: 32px;
            margin-right: 3px;
          }
          .data-unit {
            color: #fff;
            font-weight: bold;
          }
        }
        .item-label {
          font-size: 22px;
          color: #fff;
          margin-bottom: 8px;
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
<style lang="scss" scoped>
.icon-dike-sty {
  // cursor: pointer;
  margin-right: 10px;
}
.icon-circle {
  width: 12px !important;
  height: 12px !important;
  border-radius: 50%;
}
.icon-color-1 {
  @include chart-icon();
  background: #15e2db;
}
.icon-color-2 {
  @include chart-icon();
  background: #008fe8;
}
.chart-panel {
  height: 200px;
}
.pie-box {
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 100%;
  .legend-panel {
    width: 40%;
    .legend-item {
      line-height: 25px;
      display: flex;
      justify-content: space-between;
      .text {
        font-size: 14px;
        margin-right: 10px;
      }
      .num {
        color: #1cfff5;
        font-size: 1rem;
      }
    }
  }
}
</style>