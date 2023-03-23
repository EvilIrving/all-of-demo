<!--
 * @Author: your name
 * @Date: 2021-06-30 18:18:20
 * @LastEditTime: 2021-06-30 20:59:08
 * @LastEditors: Please set LastEditors
 * @Description: 病险小组件
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\DangerousCard.vue
-->
<template>
  <div class="dangerous-card">
    <panel-item title="病险工程" has-switch>
      <template slot="title-switch">
        <i
          title="地图落点"
          @click.stop="weakProjectChange('1')"
          :class="[count % 2 ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
        />
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('病险工程')"
        />
      </template>
      <el-row
        class="risk-container"
        style="letter-spacing: 2px; padding: 0 16px; height: 100%"
      >
        <el-col
          :span="12"
          v-for="(item, index) in dilapData"
          :key="index"
          :class="['weakness-sty', dangerouslyShow == item.key ? 'background-sty' : '']"
        >
          <div @click="dangerously(item)">
            <p>
              <i
                :class="[
                  'icon-sty',
                  item.key == '病险水库'
                    ? 'icon-bx-shui-ku'
                    : item.key == '病险海塘'
                    ? 'icon-bx-hai-tang'
                    : item.key == '病险水闸'
                    ? 'icon-bx-shui-zha'
                    : 'icon-bx-shan-tang',
                ]"
              />
            </p>
            <div style="font-size: 20px">
              <p>{{ item.key }}</p>
              <p>
                <span> {{ item.value }} </span>
                {{ item.key == "病险海塘" ? "段" : "座" }}
              </p>
            </div>
          </div>
        </el-col>
      </el-row>
    </panel-item>
  </div>
</template>

<script>
import PanelItem from "../components/PanelItem.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { mapGetters, mapActions } from "vuex";
export default {
  name: "ReservoirCard",
  layerName: "dangerousLayer",
  legendName:["病险工程"],
  components: {
    PanelItem,
  },
  mounted() {
    this.$nextTick(() => {
      this.dilapProjStats(); //病险工程
      this.timer = setInterval(() => {
        this.dilapProjStats();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  data() {
    return {
      //病险分析
      dilapData: [],
      //病险工程列表
      weakProjectList: [],
      count: 2,
      //病险工程
      dangerouslyShow: "",
      timer: null,
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
    ...mapActions(["SetDetailDialogParam"]),
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
      this.$router.push(`/hydraulicAmap?selectType=${type}`);
    },

    /**
     * 病险工程
     */
    async dilapProjStats() {
      await disasterPreventionApi.dilapProjStats().then((res) => {
        if (res.code == 0) {
          this.dilapData = res.data;
        }
      });
    },
    //病险工程
    dangerously(val) {
      this.count = 2;
      if (val.key == this.dangerouslyShow) {
        this.dangerouslyShow = "";
        this.dangerouslyMapPoints({ type: "病险工程", dilapProjType: val.key });
        this.$emit("legend-change", "病险工程", {});
      } else {
        this.dangerouslyShow = val.key;
        this.dangerouslyMapPoints({ type: "病险工程", dilapProjType: val.key });
      }
    },
    /**
     * 病险工程全部点位
     */
    async weakProjectChange(btnType) {
      this.count++;
      if (this.count % 2) {
        this.dangerouslyShow = "病险工程";
        this.dangerouslyMapPoints({ type: "病险工程" });
        this.$emit("legend-change", "病险工程", {
          label: "病险工程",
          children: [
            { label: "病险水库", icon: "icon-legend-bxsk" },
            { label: "病险海塘", icon: "icon-haitang-bing-xian" },
            { label: "病险水闸", icon: "icon-legend-bxs" },
            { label: "病险山塘", icon: "icon-legend-bxst" },
          ],
        });
      } else {
        this.dangerouslyShow = "";
        this.dangerouslyMapPoints({ type: "病险工程" });
        this.$emit("legend-change", "病险工程", {});
      }
    },
    /**
     * 病险工程单独点位处理
     */
    async dangerouslyMapPoints(type) {
      let res = await disasterPreventionApi.rightMapPoints(type);
      if (res.code == 0) {
        this.weakProjectList = res.data;
        this.handleWeakProjectChange("病险工程");
      }
    },

    /**
     * 病险点位开关
     */
    handleWeakProjectChange(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.5,
        anchor: [49, 16],
        anchorXUnits: "pixels",
        src: "",
      };
      let pointList = [];
      let lineArr = [];
      for (let point of this.weakProjectList) {
        let imgSrc = require("@/assets/images/MapPoint/reservoir-fault.png");
        let props = {};
        if (point.projectType == "水库") {
          imgSrc = require("@/assets/images/MapPoint/reservoir-fault.png");
          let waterLevel = point.waterLevel ? point.waterLevel + "m" : "-";
          let currentLimitLevel = point.rsvrInfo.currentLimitLevel
            ? point.rsvrInfo?.currentLimitLevel + "m"
            : "-";
          props = {
            水位: waterLevel,
            汛限水位: currentLimitLevel,
          };
        } else if (point.projectType == "海塘") {
          imgSrc = "@/assets/images/legend-dike-normal.png";
          this.map.drawPoint([], {}, "dangerousLayer");
          let obj = {};
          let color = "#E80000";
          if (!point.seawallInfo) continue;
          let seawall = point.seawallInfo;
          if (seawall < 10) {
            color = "#E80000";
          } else if (seawall.designTideStandard == 10) {
            color = "#DD00DB";
          } else if (seawall.designTideStandard == 20) {
            color = "#E89100";
          } else if (seawall.designTideStandard == 50) {
            color = "#EEC80B";
          } else if (seawall.designTideStandard == 100) {
            color = "#19AF47";
          } else if (seawall.designTideStandard > 100) {
            color = "#4CEBC7";
          }
          let style = {
            lineWidth: 4,
            lineColor: color,
          };
          let lineString =
            (seawall.coorpot && seawall.coorpot.replaceAll("&quot;", "'")) || "[]";
          let linelist = lineString && eval(lineString);
          linelist = linelist.map((line) => {
            line = line.map((item, index) => {
              return [item.lng, item.lat];
            });
            return {
              lineList: line,
              data: {
                type: "海塘",
                name: seawall.seawallName,
                props: {
                  海塘长度: seawall.seawallLength + "m",
                  防潮标准: seawall.designTideStandard + "m",
                  所在位置: seawall.village,
                },
              },
            };
          });
          obj.coordinates = linelist;
          obj.styleObj = style;
          lineArr.push(obj);
        } else if (point.projectType == "山塘") {
          imgSrc = require("@/assets/images/MapPoint/shantang-fault.png");
        } else {
          imgSrc = require("@/assets/images/MapPoint/sluice-fault.png");
        }
        pointList.push({
          lng: point.longitude,
          lat: point.latitude,
          type: point.projectType,
          name: point.projectName,
          src: imgSrc,
          code: point.projectCode,
          props: props,
        });
      }
      if (this.dangerouslyShow) {
        // this.$emit("legend-change", "病险工程", {
        //   label: "病险工程",
        //   children: [
        //     { label: "病险水库", icon: "icon-legend-bxsk" },
        //     { label: "病险海塘", icon: "icon-haitang-bing-xian" },
        //     { label: "病险水闸", icon: "icon-legend-bxs" },
        //     { label: "病险山塘", icon: "icon-legend-bxst" },
        //   ],
        // });
        if (lineArr.length > 0) {
          console.log(lineArr.map((item) => item.coordinates));
          for (let item of lineArr) {
            this.map.drawLineEx(item.coordinates, item.styleObj);
          }
          this.map.drawPoint(pointList, style, "dangerousLayer");
        } else {
          this.map.removeLineLayer();
          this.map.drawPoint(pointList, style, "dangerousLayer");
        }
      } else {
        if (lineArr.length > 0) {
          this.map.removeLineLayer();
        } else {
          this.map.drawPoint([], {}, "dangerousLayer");
        }
        this.$emit("legend-change", "河网", {});
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.dangerous-card {
  height: 100%;
  // padding: 0 16px;
  ::v-deep.el-carousel {
    height: 100%;
    .el-carousel__container {
      height: 100%;
    }
  }
  .risk-container {
    @include flexbox;
    @include flexflow(row, wrap);
  }
  .weakness-sty {
    padding: 16px 0;
    margin: 10px 0 0 0;
    > div:nth-child(1) {
      font-size: 16px;
      color: #ffffff;
      line-height: 24px;
      display: flex;
      justify-content: center;
      > div {
        > p {
          font-size: 16px;
          > span {
            font-size: 32px;
            color: #eec80b;
            font-weight: 600;
            line-height: 40px;
          }
        }
      }
      > p {
        font-size: 16px;
        > i {
          height: 52px;
          width: 52px;
          margin-top: 5px;
          margin-right: 10px;
        }
      }
    }
  }
  .background-sty {
    background: url(~assets/images/WaterDisastersImage/bai-jing-style.png) no-repeat top
      center;
    background-size: 100% 100%;
  }
}
</style>
