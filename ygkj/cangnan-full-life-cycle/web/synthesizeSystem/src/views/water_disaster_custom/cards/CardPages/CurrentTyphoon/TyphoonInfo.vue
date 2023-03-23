<script>
export default {
  name: "TyphoonInfo",
  props: { data: Object },
  methods: {
    /**
     * 渲染一个区块
     * @param {Function} labelEl 渲染函数
     * @param {String} title 名称
     * @param {String} imgCls 图片类名
     * @returns
     */
    renderBlock(labelEl, title, imgCls, clickable = false) {
      return (
        <div
          class="typhoon-item"
          style={clickable && "cursor: pointer;"}
          onClick={() => {
            this.$emit("change", title);
          }}
        >
          <div class="typhoon-item__label">{labelEl}</div>
          <div class="typhoon-item__title">{title}</div>
          <div class="typhoon-item__line">
            <div class="line-before" />
            <div class="line-after" />
          </div>
          <div class={["typhoon-item__image", imgCls]}></div>
        </div>
      );
    },
    renderUnit(unit) {
      return <span class="unit">{unit}</span>;
    },
    renderLocation() {
      let { renderUnit, data } = this;

      const el = (
        <span>
          {data.lng || "-"}
          {renderUnit("°E")}
          {data.lat || "-"}
          {renderUnit("°N")}
        </span>
      );
      return this.renderBlock(el, "中心位置", "typhoon-information-style");
    },
    renderCenterPressure() {
      let { renderUnit, data } = this;
      const el = (
        <span>
          {data.pressure || "-"}
          {renderUnit("hPa")}
        </span>
      );
      return this.renderBlock(el, "中心气压", "typhoon-pressure-style", true);
    },
    renderWindPowerSpeed() {
      let { renderUnit, data } = this;
      const el = (
        <span>
          {data.power || "-"}
          {renderUnit("级")}
          {data.speed || "-"}
          {renderUnit("米/秒")}
        </span>
      );
      return this.renderBlock(el, "风力风速", "wind-speed-style", true);
    },
    renderMoveSpeed() {
      let { renderUnit, data } = this;
      const el = (
        <span>
          {data.move_speed || "-"}
          {renderUnit("km/h")}
        </span>
      );
      return this.renderBlock(el, "移动速度", "typhoon-movement-style", true);
    },
  },
  render() {
    const lcation = this.renderLocation();
    const centerPre = this.renderCenterPressure();
    const windPower = this.renderWindPowerSpeed();
    const moveSpeed = this.renderMoveSpeed();
    return (
      <div class="typhoon-info-list">
        {lcation}
        {centerPre}
        {windPower}
        {moveSpeed}
      </div>
    );
  },
};
</script>
<style lang="scss">
.typhoon-info-list {
  border-bottom: 1px solid #00bfff3b;
  .typhoon-item {
    height: 100px;
    position: relative;
    .typhoon-item__label {
      position: absolute;
      left: 14px;
      top: 24px;
      font-size: 24px;
      color: #1ff1f0;
      .unit {
        font-size: 14px;
        color: #ffffff;
        line-height: 24px;
        margin-right: 12px;
      }
    }
    .typhoon-item__title {
      position: absolute;
      left: 14px;
      top: 52px;
      font-size: 14px;
      color: #ffffff;
    }
    .typhoon-item__line {
      position: relative;
      top: 60px;
      left: 80px;
      .line-before {
        width: 140px;
        border-bottom: 1px solid #1ff1f0;
        position: relative;
        &::before {
          content: "";
          position: absolute;
          width: 5px;
          height: 5px;
          border-radius: 50%;
          background-color: #1ff1f0;
          top: -2px;
        }
      }
      .line-after {
        width: 33px;
        border-bottom: 1px solid #1ff1f0;
        position: absolute;
        left: 140px;
        top: 0;
        transform: rotate(-45deg) translate(5px, -12px);
        position: relative;
        &::before {
          content: "";
          position: absolute;
          width: 5px;
          height: 5px;
          border-radius: 50%;
          background-color: #1ff1f0;
          right: 0;
          top: -2px;
        }
      }
    }
    .typhoon-item__image {
      position: absolute;
      right: 25px;
      &.typhoon-information-style {
        height: 80px;
        width: 100px;
        float: right;
        margin: 10px 0;
        color: #00f5ff;
        background: url(~assets/images/WaterDisastersImage/zhong-xin.png) no-repeat top
          center;
        background-size: 100% 100%;
      }
      &.typhoon-pressure-style {
        height: 80px;
        width: 100px;
        float: right;
        color: #00f5ff;
        background: url(~assets/images/WaterDisastersImage/zhong-xin-qi-ya.png) no-repeat
          top center;
        background-size: 100% 100%;
      }
      &.wind-speed-style {
        height: 80px;
        width: 100px;
        float: right;
        color: #00f5ff;
        background: url(~assets/images/WaterDisastersImage/fengshu-fengli.png) no-repeat
          top center;
        background-size: 100% 100%;
      }
      &.typhoon-movement-style {
        height: 80px;
        width: 100px;
        float: right;
        color: #00f5ff;
        background: url(~assets/images/WaterDisastersImage/yi-dong-shu-du.png) no-repeat
          top center;
        background-size: 100% 100%;
      }
    }
  }
}
</style>
