<template>
  <div class="rain-forecast-card">
    <panel-item title="水闸" has-switch tc @tclick="tclick">
      <template slot="title-switch">
        <i
          title="地图落点"
          @click="handleSluiceChange('szbtn')"
          :class="[
            szbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('水闸')"
        />
        <i @click="goToAmap('水闸')" title="一张图" class="icon-yizhangtu icon-sty" />
      </template>
      <!-- 水闸 -->
      <div class="rainfall-style">
        <p>
          总数 <span> {{ wagaData.wagaProjNum }} </span> 个
        </p>
      </div>
      <div class="water-gate-sty">
        <!-- 大中型水库 -->
        <div class="area-style">
          <p>温瑞平</p>
          <p>
            总数 <span> {{ wagaData.wrpNum }} </span> 个
          </p>
          <p>
            <i style="margin-right: 10px" class="icon-kai-qi-shu"></i>已开启
            <span class="yellow"> {{ wagaData.wrpOpenNum }} </span> 个
          </p>
        </div>
        <div class="area-style">
          <p>
            <i class="el-icon-time icon-sty" style="margin-right: 4px"></i
            ><span style="font-size: 14px; color: #fff; line-height: 2.125rem"
              >累计:</span
            >
            <el-input-number
              v-model="num"
              size="small"
              controls-position="right"
              :min="1"
              :max="72"
              @input="changeHours"
            />
            <span style="font-size: 14px; color: #fff; line-height: 2.125rem">小时</span>
          </p>
          <p style="font-size: 14px">
            泄洪总量 <span> {{ wagaData.drainage }} </span> 万m³
          </p>
        </div>
      </div>
    </panel-item>
  </div>
</template>

<script>
import PanelItem from "../components/PanelItem.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { mapGetters, mapActions } from "vuex";
import { goAmap } from "./utils/common";
export default {
  name: "SluiceCard",
  layerName: "sluiceLayer",
  legendName: "水闸",
  components: {
    PanelItem,
  },
  mounted() {
    this.$nextTick(() => {
      this.getRightMapPoints("水闸");
      this.timer = setInterval(()=>{
        this.getRightMapPoints("水闸");
      },300000);
    });
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  data() {
    return {
      //水闸统计数据
      wagaData: {},
      //累计泄洪时间
      num: 1,
      //水闸列表
      wageStList: [],
      szbtn: false, //水闸点位展示标记
      timer:null
    };
  },
  computed: {
    ...mapGetters([
      "ScreenMap",
      "currentSelectArea",
      "previewModule",
      "formatterCurrentTime",
    ]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  methods: {
    ...mapActions([
      "changeScreenLoading",
      "getForecastRainfallLayer",
      "SetDetailDialogParam",
    ]),
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
      this.SetDetailDialogParam({
        mediumSized: "",
        codeTpye: val,
        analyseDialogShow: true,
      });
    },
    /**
     * 跳转一张图
     */
    goToAmap(type) {
      goAmap(type);
      // this.$router.push(`/hydraulicAmap?selectType=${type}`);
    },
    /**
     * 点位list
     */
    async getRightMapPoints(type) {
      let opt = {
        type,
      };
      let res = await disasterPreventionApi.rightMapPoints(opt);
      this.wageStList = res.data;
    },
    /**
     * 水闸点位开关
     */
    handleSluiceChange(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.7,
        anchor: [24, 16],
        anchorXUnits: "pixels",
        src: require("@/assets/images/MapPoint/sluice.png"),
        // srcs:require("@/assets/images/MapPoint/sluice.png")
      };
      let pointList = [];
      for (let point of this.wageStList) {
        let imgSrc = require("@/assets/images/MapPoint/sluice.png");
        // let imgSrcs = require("@/assets/images/shuizha-dianwei-kaiqi.png");
        if (!point.gateOpened) {
          imgSrc = require("@/assets/images/MapPoint/sluice-warn.png");
          // imgSrcs = require("@/assets/images/MapPoint/sluice.png");
        }
        pointList.push({
          lng: point.startLong,
          lat: point.startLat,
          type: "水闸",
          src: imgSrc,
          code: point.wagaCode,
          name: point.wagaName,
          stationCode: point.stCode || "",
          props: {
            闸上水位: `${point.upz || "-"}m`,
            闸下水位: `${point.dwz || "-"}m`,
          },
        });
      }
      if (this[btnType]) {
        this.map.drawPoint(pointList, style, "sluiceLayer");
        this.$emit("legend-change", "水闸", {
          label: "水闸",
          children: [
            {
              label: "开启",
              icon: "icon-shui-zha-kaiqi",
            },
            {
              label: "关闭",
              icon: "icon-shui-zha-guanbi",
            },
          ],
        });
      } else {
        this.map.drawPoint([], {}, "sluiceLayer");
        this.$emit("legend-change", "水闸", {});
      }
    },
    /**
     * 累计泄洪时间
     */
    changeHours() {
      this.wagaProjStats(this.num);
    },
    /**
     * 水闸统计数据
     */
    async wagaProjStats(val) {
      let obj = {
        hours: val,
      };
      await disasterPreventionApi.wagaProjStats(obj).then((res) => {
        if (res.code == 0) {
          this.wagaData = res.data;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.rain-forecast-card {
  height: 100%;
  // padding: 0 16px;
  .rainfall-style {
    @include flexbox;
    @include flexJC(center);
    margin-top: 16px;
  }
  ::v-deep .panel-item {
    .panel-item__content {
      @include flexbox;
      @include flexflow(column, nowrap);
      @include flexJC(space-around);
    }
  }
}
::v-deep .rivertooltip__item {
  padding: 4px 8px;
  font-size: 18px;
  &.rivertooltip__item-error {
    color: #ff7144;
  }
  &.rivertooltip__item-warn {
    color: #eec80b;
  }
}
::v-deep .panel-item__title--switch {
  i {
    cursor: pointer;
  }
}

.water-gate-sty {
  padding: 10px;
  margin-bottom: 10px;
  background: url(~assets/images/WaterDisastersImage/shui-zha.png) no-repeat top center;
  background-size: 100% 100%;
  > .area-style {
    background: none;
    p {
      color: #fff;
      font-size: 18px;
      vertical-align: middle;
      > span {
        font-size: 26px;
        color: #1cfff5;
        margin: 0 3px;
      }
    }
    > p:last-child {
      span.yellow {
        color: #eec80b;
      }
    }
  }
  > .area-style:nth-child(1) {
    align-items: center;
  }

  > div:nth-child(1) {
    border-bottom: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
  }
  > div:nth-child(2) {
    > p:nth-child(1) {
      > i {
        font-size: 17px;
        line-height: 30px;
        margin-top: 2px;
        color: #fff;
      }
      font-size: 16px;
      color: #fff;
      display: flex;
      justify-content: left;
      ::v-deep.el-input {
        width: 32px;
        height: 32px;
      }
      ::v-deep.el-input-number {
        position: relative;
        display: inline-block;
        width: 60px;
        border: 1px solid #ffffff;
        border-radius: 4px;
      }
      ::v-deep.el-input__inner {
        background: none;
        padding: 0;
      }
      ::v-deep.el-input-number__decrease {
        width: 24px;
        border-left: 0.0625rem solid #fff;
        background: none;
        > i {
          color: #fff;
        }
      }
      ::v-deep.el-input-number__increase {
        width: 24px;
        height: 16px;
        border-left: 0.0625rem solid #fff;
        background: none;
        > i {
          color: #fff;
        }
      }
    }
    > p:nth-child(2) {
      font-size: 16px;
      > span {
        padding: 0;
        margin: 0;
      }
    }
  }
}
</style>
