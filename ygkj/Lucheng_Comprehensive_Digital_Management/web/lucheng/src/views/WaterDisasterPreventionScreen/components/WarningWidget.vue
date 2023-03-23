<script>
import { mapActions, mapGetters } from "vuex";
export default {
  name: "WarningWidget",
  data() {
    return {
      pointActive: false,
    };
  },
  watch: {
    currentWarningListPoint: {
      deep: true,
      handler(val) {
        this.$nextTick(() => {
          if (this.pointActive) {
            this.ScreenMap.clearDynamicPoint();
            this.showWarnPoints();
          }
        });
      },
    },
  },
  computed: {
    ...mapGetters([
      "currentWarningList",
      "currentWarningListPoint",
      "ScreenMap",
    ]),
    isBigScrren() {
      return this.$route.query.large == "true";
    },
  },
  methods: {
    ...mapActions(["hideWarningList"]),
    /** 落点按钮点击 */
    handlePointClick(e, hideType = "") {
      if (hideType == "") this.pointActive = !this.pointActive;
      if (this.pointActive) {
        this.showWarnPoints();
      } else {
        this.ScreenMap.clearDynamicPoint();
      }
    },
    showWarnPoints() {
      let typePointList = {};
      let typeMap = {
        暴雨: {
          src: require("@/assets/images/MapPoint/risk-rain.gif"),
          lat: "latitude",
          lng: "longitude",
          type: "雨量",
          code: "stcd",
          name: "stationName",
          props: {
            雨量: {
              name: "drp",
              unit: "mm",
            },
          },
        },
        山洪: {
          src: require("@/assets/images/MapPoint/torrents_higher.png"),
          name: "avi",
          type: "山洪",
          code: "stationCode",
          lng: "lon",
          lat: "lat",
          props: {
            总户数: { name: "tho", unit: "户" },
            总人口: { name: "tpo", unit: "人" },
            防汛责任人: { name: "chargerName", unit: "" },
            防汛责任人电话: { name: "chargerPhone", unit: "" },
          },
        },
        水库: {
          name: "resName",
          type: "水库",
          props: {
            汛限水位: { name: "currentLimitLevel", unit: "m" },
            实时水位: { name: "waterLevel", unit: "m" },
          },
          code: "resCode",
          lng: "lowLeftLong",
          lat: "lowLeftLat",
          src: require("@/assets/images/MapPoint/reservoir-fault.png"),
        },
        河网: {
          name: "rnName",
          type: "河网",
          props: {
            警戒水位: { name: "warningLevel", unit: "m" },
            实时水位: { name: "waterLevel", unit: "m" },
          },
          lng: "stLong",
          lat: "stLat",
          src: require("@/assets/images/MapPoint/rivernet-warn.png"),
        },
        潮位: {
          name: "stName",
          type: "潮位",
          props: {
            警戒水位: { name: "warningLevel", unit: "m" },
            实时水位: { name: "waterLevel", unit: "m" },
          },
          lng: "stLong",
          lat: "stLat",
          src: require("@/assets/images/MapPoint/tide.png"),
        },
      };
      let typeLayerMap = {
        水库: "reservoir-dynamic",
        暴雨: "rainrisk-dynamic",
        山洪: "flood-dynamic",
      };
      this.currentWarningListPoint.forEach((item) => {
        let type = item.type;
        let list = item.list.map((point) => {
          let props = {};
          let propsList = Object.getOwnPropertyNames(
            typeMap[type]?.props || {}
          );
          propsList.forEach((prop) => {
            props[prop] =
              point[typeMap[type].props[prop].name] +
              typeMap[type].props[prop].unit;
          });
          let result = {
            lng: point[typeMap[type].lng],
            lat: point[typeMap[type].lat],
            name: point[typeMap[type].name],
            code: point[typeMap[type].code],
            type: typeMap[type].type,
            src: typeMap[type].src,
            props,
            ...point,
          };
          return result;
        });
        typePointList[type] = list;
        // if (hideType == "") {
        if (list.length > 0) {
          this.ScreenMap.drawDynamicPoint(list, `${typeLayerMap[type]}`);
        }
        // } else if (hideType == type) {
        //   if (this.pointActive) {
        //     this.ScreenMap.drawDynamicPoint(list,`${typeLayerMap[type]}`);
        //   } else {
        //     this.ScreenMap.clearDynamicPoint(`${typeLayerMap[type]}`);
        //   }
        // }
      });
    },
    handleWarningListHide(e) {
      this.hideWarningList();
      this.pointActive = true;
      this.handlePointClick();
    },
  },
  render() {
    const pointBtnCls = [
      "acition-button",
      this.pointActive
        ? "icon-warning-widget-point-active"
        : "icon-warning-widget-point",
    ];
    const editBtnCls = ["acition-button", "icon-warning-widget-edit"];
    const hideBtnCls = ["acition-button", "icon-warning-widget-hide"];
    let hiddenList = [];
    return (
      <div class={["warning-widget"]}>
        <div class="warning-action-bar drag">
          <span class="bar-label">
            <i class="icon-warning-widget-alert" />
            <span>报警</span>
          </span>
          <span class="bar-action">
            <i class={pointBtnCls} onClick={this.handlePointClick} />
            <i class={hideBtnCls} onClick={this.handleWarningListHide} />
          </span>
        </div>
        <div class="warning-list">
          {this.currentWarningList.map((item, index) => {
            if (item.number == 0) return null;
            return (
              <div
                class="warning-list__item"
                onClick={(e) => {
                  if (hiddenList.indexOf(index) != -1) hiddenList.push(index);
                  this.handlePointClick(e, item.title);
                }}
              >
                <div class="item-content">
                  <i class={`icon-warning-widget-${item.icon}`} />
                  <span class="warning-title">{item.title}</span>
                  <span class="warning-number">{item.number}</span>
                  <span class="warning-unit">{item.unit}</span>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    );
  },
};
</script>

<style lang="scss" scoped>
.warning-widget {
  user-select: none;
  position: absolute;
  top: 8%;
  z-index: 100;
  margin-left: 15px;
  // transition: all 0.5s ease-in-out;
  &.is-big-screen {
    transform: scaleX(0.5) translateX(-50%);
  }
  &.is-big-screen.fsl {
    transition: unset;
  }
  .warning-action-bar {
    height: 42px;
    width: 200px;
    background-image: linear-gradient(
      90deg,
      #cc0205 0%,
      #000000 50%,
      #cc0205 99%
    );
    border-radius: 2px;
    border-bottom-left-radius: 0;
    border-bottom-right-radius: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #fff;
    font-size: 20px;
    .bar-label {
      display: flex;
      align-items: baseline;
      i {
        margin-right: 6px;
      }
    }
    .bar-action {
      margin-left: 20px;
      display: flex;
      align-items: baseline;
      .acition-button {
        margin: 0 5px;
        cursor: pointer;
      }
    }
  }
  .warning-list__item {
    // cursor: pointer;
    border-radius: 2px;
    border-top-left-radius: 0;
    border-top-right-radius: 0;
    margin-bottom: 8px;
    position: relative;
    background: linear-gradient(90deg, #ca0205 0%, #000000 50%, #cc0205 100%)
        left top no-repeat,
      linear-gradient(90deg, #cc0205 0%, #cc0205 100%) right top no-repeat,
      linear-gradient(90deg, #ca0205 0%, #000000 50%, #cc0205 100%) left bottom
        no-repeat,
      linear-gradient(90deg, #ca0205 0%, #cc0205 100%) left bottom no-repeat;

    /*设置大小*/
    background-size: 100% 2px, 2px 100%;
    padding: 2px;
    .item-content {
      color: #fff;
      height: 38px;
      width: 196px;
      background-image: linear-gradient(
        90deg,
        #360001 0%,
        #000000 50%,
        #360001 99%
      );
      border-radius: 2px;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 20px;
      .warning-title {
        margin-left: 12px;
      }
      .warning-number {
        width: 48px;
        text-align: right;
        font-size: 24px;
      }
      .warning-unit {
        margin-left: 6px;
      }
    }
  }
}
</style>
<style lang="less" scoped>
/* @import "../../../style/spriteConfig";
.icon-warning-widget-point {
  &:hover {
    .icon-warning-widget-point-active();
  }
} */
</style>
