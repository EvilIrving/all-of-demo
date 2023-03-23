<script>
import ReplayModuleOption from "./ReplayModuleOption.vue";
import PanelItem from "../../../components/PanelItem.vue";
import Rainfall from "./ReplayModuleRainfall.vue";
import StationStat from "./ReplayModuleStation.vue";
import { mapActions, mapGetters } from "vuex";
export default {
  name: "ReplayModuleMain",
  data() {
    return {
      pointBtn: false,
      realdzmbtn:false,
    };
  },
  computed: {
    ...mapGetters([
      "formatterStartTime",
      "formatterEndTime",
      "formatterCurrentTime",
      "formatterTotalTime",
      "ScreenMap",
      "stationPoint",
      "currentScene"
    ]),
    startTime() {
      let time = this.formatterStartTime();
      return time != "Invalid date" ? time : "请选择时间";
    },
    currentTime() {
      let time = this.formatterCurrentTime();
      return time != "Invalid date" ? time : "请选择时间";
    },
  },
  watch: {
    currentTime: {
      immediate: true,
      handler(val) {
        this.$nextTick(() => {
          // this.getStationData();
          if(this.currentScene == '防御复盘'){
            this.getRainfallMaxData();
          }
        });
      },
    },
  },
  methods: {
    ...mapActions([
      "setStartTimeatmap",
      "setEndTimeatamp",
      "getStationData",
      "getRainfallMaxData",
    ]),
    renderOptionDisplay() {
      let startTime = this.startTime;
      let endTime = this.currentTime;
      let totalTime = !isNaN(this.formatterTotalTime) ? this.formatterTotalTime : "-";
      return (
        <div class="option-display">
          <div class="option-start-time display-item">
            <div class="date">{startTime}</div>
            <div>起始时间</div>
          </div>
          <div class="option-end-time display-item">
            <div class="date">{endTime}</div>
            <div>实时时间</div>
          </div>
          <div class="total-time display-item">
            <div class="date">{totalTime}h</div>
            <div>累计</div>
          </div>
        </div>
      );
    },
    selectRangeChange(value) {
      // console.log(value[0],value[1]);
      this.setStartTimeatmap(value[0] != "" && value[0]);
      this.setEndTimeatamp(value[1] != "" && value[1]);
    },
    drawStationPoint() {
      this.pointBtn = !this.pointBtn;
      if (this.pointBtn) {
        let style = {
          anchor: [5, 5],
          anchorXUnits: "pixels",
        };
        let imgSrc = require("@/assets/images/legend/rain_0.png");
        let pointList = this.stationPoint.map((point) => {
          if (point.drp > 0) {
            imgSrc = require("@/assets/images/legend/rain_10.png");
          }
          if (point.drp > 10) {
            imgSrc = require("@/assets/images/legend/rain_10_25.png");
          }
          if (point.drp > 25) {
            imgSrc = require("@/assets/images/legend/rain_25_50.png");
          }
          if (point.drp > 50) {
            imgSrc = require("@/assets/images/legend/rain_50_100.png");
          }
          if (point.drp > 100) {
            imgSrc = require("@/assets/images/legend/rain_100_250.png");
          }
          if (point.drp >= 250) {
            imgSrc = require("@/assets/images/legend/rain_250.png");
          }

          return { ...point, src: imgSrc ,type:'复盘雨量',name:point.stationName,code:point.stcd};
        });

        this.ScreenMap.drawPoint(pointList, style, "stationLayer");
      } else {
        this.ScreenMap.drawPoint([], {}, "stationLayer");
      }
    },
    clickRealtimeRainfull(type){
      this[type] = !this[type];
      this.$emit("clickRealtimeRainfull");
    }
  },
  render() {
    let optionDisplay = this.renderOptionDisplay();
    const scopedSlots = {
      "title-switch": () => {
        return (
          <i
            class={[this.pointBtn ? "icon-yinzhang" : "icon-yinzhang-1"]}
            style="cursor: pointer;"
            onClick={this.drawStationPoint}
          />
        );
      },
    };
    /* {...{ scopedSlots }} */
    return (
      <div class="collapse-panel__content replay-option-panel">
        <ReplayModuleOption onSelectRange={this.selectRangeChange} >
        </ReplayModuleOption>
        <PanelItem title="降雨分析" hasSwitch>
          <template slot="title-switch">
            <i
              title="等值面"
              style="height: 18px"
              onClick={()=>{
                this.clickRealtimeRainfull('realdzmbtn');
              }}
              class={[
                this.realdzmbtn == true ? 'icon-diagram-1' : 'icon-diagram',
                'icon-sty',
              ]}
            />
          </template>
          {optionDisplay}
          <StationStat />
          <Rainfall />
        </PanelItem>
      </div>
    );
  },
};
</script>
<style lang="scss">
.replay-option-panel {
  color: #fff;
  .option-display {
    margin-top: 8px;
    display: flex;
    height: 60px;
    background-image: linear-gradient(90deg, #ffffff22 0%, #f9f9f900 100%);
    .option-start-time,
    .option-end-time {
      width: 40%;
    }
    .total-time {
      width: 20%;
    }
    .display-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      position: relative;
      .date {
        font-size: 18px;
        color: #1ff1f0;
        line-height: 24px;
        margin-bottom: 5px;
      }
      &:after {
        content: "";
        position: absolute;
        right: 0;
        top: 15%;
        height: 70%;
        border-right: 1px #1ff1f0 solid;
        opacity: 0.2;
      }
      &:last-child {
        &:after {
          content: unset;
        }
      }
    }
  }
}
</style>
