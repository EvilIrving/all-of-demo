<!--
 * @Author: your name
 * @Date: 2021-07-12 15:32:29
 * @LastEditTime: 2021-07-16 19:03:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\EmergencySupportCard.vue
-->
<template>
  <div class="emergency-support-card">
    <panel-item title="抢险支持" has-switch>
      <template slot="title-switch">
        <i
          :class="
            IsAction ? 'icon-rescue-immediately' : 'icon-rescue-stateless'
          "
          style="cursor: pointer"
          @click="SelectionTips"
        ></i>
        
      </template>
      <el-carousel
        ref="carousel"
        trigger="click"
        arrow="never"
        @change="carouselChange"
        :autoplay="autoplay"
      >
        <el-carousel-item class="carousel-item-content">
          <sub-title-card
            ref="warehouseCard"
            title="防汛仓库"
            :tabs="warehouseTab"
            :dropdown="warehouseDropdown"
            @handleTabChange="handleWarehouseTab"
            @handleDropdownChange="handleWarehouseDropdown"
          >
            <div slot="title-operate">
              <i
                title="地图落点"
                @click="handleWarehouse('warehouseBtn')"
                :class="[
                  warehouseBtn == true
                    ? 'icon-yinzhang-active'
                    : 'icon-yinzhang-1',
                  'icon-sty',
                ]"
              />
              <i
                v-show="!previewModule"
                title="查看详表"
                class="icon-liebiao icon-sty"
                @click.stop="openDetailsDialog('防汛仓库')"
                style="margin-left: 10px"
              />
              <i 
                @click="goToAmap('防汛仓库')" 
                title="一张图" 
                class="icon-yizhangtu icon-sty" 
                style="margin-left: 10px"
              />
            </div>
            <vchart-bar
              ref="warehouseChart"
              :barData="warehouseBarData"
              :colorArr="warehouseColor"
              :labelColor="warehouseLabelColor"
              :barName="warehouseBarName"
              :unit="warehouseUnit"
            ></vchart-bar>
          </sub-title-card>
        </el-carousel-item>
        <el-carousel-item class="carousel-item-content">
          <sub-title-card
            ref="ranksCard"
            title="防汛队伍"
            :tabs="ranksTab"
            :dropdown="ranksDropdown"
            @handleTabChange="handleRanksTab"
            @handleDropdownChange="handleRanksDropdown"
          >
            <div slot="title-operate">
              <i
                title="地图落点"
                @click="handleRanks('ranksBtn')"
                :class="[
                  ranksBtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
                  'icon-sty',
                ]"
              />
              <i
                v-show="!previewModule"
                title="查看详表"
                class="icon-liebiao icon-sty"
                @click.stop="openDetailsDialog('防汛队伍')"
                style="margin-left: 10px"
              />
              <i 
                @click="goToAmap('救援队伍')" 
                title="一张图" 
                class="icon-yizhangtu icon-sty" 
                style="margin-left: 10px"
              />
            </div>
            <vchart-bar
              ref="ranksChart"
              :barData="ranksBarData"
              :colorArr="ranksColor"
              :labelColor="ranksLabelColor"
              :barName="ranksBarName"
              :unit="rankUnit"
            ></vchart-bar>
          </sub-title-card>
        </el-carousel-item>
        <el-carousel-item class="carousel-item-content" v-if="DispatchCarousel">
          <sub-title-card ref="ranksCard" title="抢险结果">
            <div slot="title-operate">
              <i class="icon-send" style="cursor: pointer" @click="SendMsg" />
            </div>
            <div class="route-head">
              <span class="route-point-name" :title="TeamInfo.name">{{
                TeamInfo.name
              }}</span>
              <span class="route-tag">{{ TeamInfo.areaName }}</span>
              <span class="route-tag">{{ TeamInfo.existingNumber }}人</span>
              <span class="route-tag">{{ TeamInfo.distance }}km</span>
              <i class="icon-clear" @click="clearDispatch"></i>
            </div>
            <p class="route-info">
              <i class="icon-perfect-route"></i>
              <span>{{ teamDispatchInfo.duration }}</span>
              <span class="info-divide">|</span>
              <span>{{ teamDispatchInfo.distance }}公里</span>
              <span class="info-divide">|</span>
              <span>{{ teamDispatchInfo.traffic_lights }}个红绿灯 </span>
            </p>
            <p class="route-info">
              <i class="icon-charge-person"></i>
              {{ TeamInfo.chargePerson }}
              {{ TeamInfo.chargePhone }}
            </p>
            <div class="route-head warehouse-route-head">
              <span class="route-point-name" :title="WarehouseInfo.name">{{
                WarehouseInfo.name
              }}</span>
            </div>
            <p class="route-info">
              <i class="icon-perfect-route"></i>
              <span>{{ warehouseDispatchInfo.duration }}</span>
              <span class="info-divide">|</span>
              <span>{{ warehouseDispatchInfo.distance }}公里</span>
              <span class="info-divide">|</span>
              <span>{{ warehouseDispatchInfo.traffic_lights }}个红绿灯 </span>
            </p>
            <p class="route-info">
              <i class="icon-charge-person"></i>
              {{ WarehouseInfo.chargePerson }}
              {{ WarehouseInfo.chargePhone }}
            </p>
          </sub-title-card>
        </el-carousel-item>
      </el-carousel>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import SubTitleCard from "../components/SubTitleCard.vue";
import VchartBar from "../charts/vchart_3dbar/VchartBar.vue";
import EmergencyScheme from "../dialog/EmergencySchemeDialog";
import { goAmap } from "./utils/common";
export default {
  layerName: ["warehouseLayer", "ranksLayer", "emergencySupport"],
  legendName: "仓库",
  name: "EmergencySupportCard",
  components: {
    PanelItem,
    SubTitleCard,
    VchartBar,
    EmergencyScheme,
  },
  computed: {
    ...mapGetters([
      "previewModule",
      "TeamDispatchList",
      "WarehouseDispatchList",
      "DispatchCarousel",
      "TeamInfo",
      "WarehouseInfo",
      "IsAction",
    ]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  watch: {
    DispatchCarousel(val) {
      if (val) {
        this.$nextTick(() => {
          this.$refs.carousel.setActiveItem(2);
        });
      } else {
        this.$nextTick(() => {
          this.$refs.carousel.setActiveItem(0);
        });
      }
    },
    WarehouseDispatchList(val) {
      this.warehouseDispatchInfo = val && val.length > 0 ? val[0] : [];
    },
    TeamDispatchList(val) {
      this.teamDispatchInfo = val && val.length > 0 ? val[0] : [];
    },
  },
  data() {
    return {
      warehouseDispatchInfo: {},
      teamDispatchInfo: {},
      warehouseBtn: false, // 仓库落点
      ranksBtn: false, // 队伍落点
      autoplay: false,
      jsbtn: false, // 落点
      warehouseDropdown: ["仓库数量", "仓库面积", "物资价值"],
      ranksDropdown: ["队伍", "人员"],
      warehouseTab: ["按行政区划", "按仓库类型"],
      ranksTab: ["按行政区划", "按队伍类型"],
      activeWarehouseTab: 0,
      activeRanksTab: 0,
      ranksLabelColor: "#1EF8F2",
      ranksBarName: "队伍",
      ranksColor: ["#15E2DB", "#9DFDFB", "#A6FFFF"],
      ranksBarData: {
        columns: ["name", "data"],
        rows: [],
      },
      rankUnit: "z支",
      warehouseBarName: "仓库数量",
      warehouseLabelColor: "#1EF8F2",
      warehouseColor: ["#15E2DB", "#9DFDFB", "#A6FFFF"],
      warehouseBarData: {
        columns: ["name", "data"],
        rows: [],
      },
      warehouseUnit: "个",
      warehouseList: [],
      ranksList: [],
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.ranksBarName = this.ranksDropdown[0];
      this.warehouseBarName = this.warehouseDropdown[0];
      this.$refs.ranksCard.selected = this.ranksDropdown[0];
      this.$refs.warehouseCard.selected = this.warehouseDropdown[0];
      this.getWarehouseBar();
      this.getRanksBar();
      this._warehouses();
      this._teams();
    });
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetIsAction", "SetSpotsFlag"]),
    goToAmap(type){
      goAmap(type);
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
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    carouselChange() { },
    // 地图点位
    async _warehouses() {
      let res = await disasterPreventionApi.warehouses();
      this.warehouseList = res.data.list;
    },
    async _teams() {
      let res = await disasterPreventionApi.teams();
      this.ranksList = res.data.list;
    },
    // 地图落点
    handleWarehouse(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/MapPoint/warehouse-group.png"),
        src: require("@/assets/images/MapPoint/warehouse-point.png"),
      };
      let pointList = [];
      for (let point of this.warehouseList) {
        let obj = {
          name: point.name,
          type: "仓库",
          lng: Number(point.longitude),
          lat: Number(point.latitude),
          code: point.id,
          props: {
            仓库类型: `${point.type || "-"}`,
            物资价值: `${point.materialPrice || "-"}万元`,
            负责人: `${point.chargePerson || "-"}`,
            联系电话: `${point.chargePhone || "-"}`,
          },
        };
        if (point.status == 1) {
          pointList.push(obj);
        } else {
          pointList.push(obj);
        }
      }
      if (this[btnType]) {
        this.map.drawClusterPoint(pointList, style, "warehouseLayer");
        this.$emit("legend-change", "仓库", {
          label: "仓库",
          children: [
            {
              label: "仓库",
              icon: "icon-legend-warehouse",
            },
          ],
        });
      } else {
        this.map.drawClusterPoint([], {}, "warehouseLayer");
        this.$emit("legend-change", "仓库", {});
      }
    },
    handleRanks(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/MapPoint/ranks-group.png"),
        src: require("@/assets/images/MapPoint/ranks-point.png"),
      };
      let pointList = [];
      for (let point of this.ranksList) {
        let obj = {
          name: point.name,
          type: "队伍",
          lng: Number(point.longitude),
          lat: Number(point.latitude),
          code: point.id,
          props: {
            队伍类型: `${point.typeName || "-"}`,
            负责人: `${point.chargePerson || "-"}`,
            联系电话: `${point.chargePhone || "-"}`,
            人数: `${point.existingNumber || "-"}`,
          },
        };
        if (point.status == 1) {
          pointList.push(obj);
        } else {
          pointList.push(obj);
        }
      }
      if (this[btnType]) {
        this.map.drawClusterPoint(pointList, style, "ranksLayer");
        this.$emit("legend-change", "队伍", {
          label: "队伍",
          children: [
            {
              label: "队伍",
              icon: "icon-legend-ranks",
            },
          ],
        });
      } else {
        this.map.drawClusterPoint([], {}, "ranksLayer");
        this.$emit("legend-change", "队伍", {});
      }
    },
    // 仓库
    handleWarehouseTab(index) {
      this.activeWarehouseTab = index;
      this.setWarehouseColor();
      this.getWarehouseBar();
    },
    setWarehouseColor() {
      if (this.warehouseBarName == "仓库数量") {
        this.warehouseColor = ["#15E2DB", "#9DFDFB", "#A6FFFF"];
        this.warehouseLabelColor = "#1EF8F2";
        this.warehouseUnit = "个";
      } else if (this.warehouseBarName == "仓库面积") {
        this.warehouseColor = ["#2886c6", "#50bfda", "#89e3ec"];
        this.warehouseLabelColor = "#009AFF";
        this.warehouseUnit = "km²";
      } else {
        this.warehouseColor = ["#1360DA", "#598FFE", "#5691EC"];
        this.warehouseLabelColor = "#6A72FF";
        this.warehouseUnit = "万元";
      }
    },
    handleWarehouseDropdown(val) {
      this.warehouseBarName = val;
      this.setWarehouseColor();
      this.getWarehouseBar();
    },
    async getWarehouseBar() {
      let res = {};
      let rows = [];
      this.warehouseBarData.rows = rows;
      if (this.activeWarehouseTab == 0) {
        // 按行政区划
        if (this.warehouseBarName == "仓库数量") {
          res = await disasterPreventionApi.warehousesGroupByArea();
          for (let item of res.data) {
            rows.push({
              name: item.areaName,
              data: item.number,
            });
          }
        } else if (this.warehouseBarName == "仓库面积") {
          res = await disasterPreventionApi.staticAcreageByArea();
          for (let item of res.data) {
            rows.push({
              name: item.areaName,
              data: item.acreageData ? Number(item.acreageData).toFixed(1) : 0,
            });
          }
        } else {
          res = await disasterPreventionApi.staticMaterialPriceByArea();
          for (let item of res.data) {
            rows.push({
              name: item.areaName,
              data: item.materialPriceData
                ? Number(item.materialPriceData).toFixed(1)
                : 0,
            });
          }
        }
      } else {
        // 按类型
        if (this.warehouseBarName == "仓库数量") {
          res = await disasterPreventionApi.warehousesGroupByType();
          for (let item of res.data) {
            rows.push({
              name: item.type,
              data: item.number,
            });
          }
        } else if (this.warehouseBarName == "仓库面积") {
          res = await disasterPreventionApi.staticAcreageByType();
          for (let item of res.data) {
            rows.push({
              name: item.type,
              data: item.acreageData ? Number(item.acreageData).toFixed(1) : 0,
            });
          }
        } else {
          res = await disasterPreventionApi.staticMaterialPriceByType();
          for (let item of res.data) {
            rows.push({
              name: item.type,
              data: item.materialPriceData
                ? Number(item.materialPriceData).toFixed(1)
                : 0,
            });
          }
        }
      }
      this.warehouseBarData.rows = rows;
      this.$nextTick(() => {
        this.$refs["warehouseChart"].resize();
      });
    },
    // 队伍
    handleRanksTab(index) {
      this.activeRanksTab = index;
      this.setRanksColor();
      this.getRanksBar();
    },
    setRanksColor() {
      if (this.ranksBarName == "队伍") {
        this.ranksColor = ["#15E2DB", "#9DFDFB", "#A6FFFF"];
        this.ranksLabelColor = "#1EF8F2";
        this.rankUnit = "支";
      } else if (this.ranksBarName == "人员") {
        this.ranksColor = ["#2886c6", "#50bfda", "#89e3ec"];
        this.ranksLabelColor = "#009AFF";
        this.rankUnit = "人";
      }
    },
    handleRanksDropdown(val) {
      this.ranksBarName = val;
      this.setRanksColor();
      this.getRanksBar();
    },
    async getRanksBar() {
      let rows = [];
      let res = {};
      this.ranksBarData.rows = [];
      if (this.activeRanksTab == 0) {
        // 按行政区划
        if (this.ranksBarName == "人员") {
          res = await disasterPreventionApi.staticPeopleNumByArea();
          for (let item of res.data) {
            rows.push({
              name: item.areaName,
              data: item.peopleNum,
            });
          }
        } else {
          res = await disasterPreventionApi.teamGroupByArea();
          for (let item of res.data) {
            rows.push({
              name: item.areaName,
              data: item.number,
            });
          }
        }
      } else {
        // 按类型
        if (this.ranksBarName == "人员") {
          res = await disasterPreventionApi.staticPeopleNumByType();
          for (let item of res.data) {
            rows.push({
              name: item.type_name,
              data: item.peopleNum,
            });
          }
        } else {
          res = await disasterPreventionApi.teamGroupByType();
          for (let item of res.data) {
            rows.push({
              name: item.type,
              data: item.number,
            });
          }
        }
      }
      this.ranksBarData.rows = rows;
      this.$nextTick(() => {
        this.$refs["ranksChart"].resize();
      });
    },
    // 清除调度路线
    clearDispatch() {
      this.map.clearDispatch();
    },
    // 立即抢险
    SelectionTips() {
      this.SetIsAction(!this.IsAction);
      if (this.IsAction == true) {
        this.$emit("SelectionTips");
      } else {
        this.SetSpotsFlag(false);
      }
    },
    //发送抢险结果
    SendMsg() {
      this.$emit("SendMsg");
    },
  },
};
</script>
<style lang="scss" scoped>
.emergency-support-card {
  width: 100%;
  height: 100%;
  ::v-deep .el-carousel .el-carousel__container {
    height: 100%;
  }

  .route-info {
    font-size: 16px;
    color: #ffffff;
    line-height: 26px;
    i {
      vertical-align: middle;
      margin-right: 4px;
    }
    .info-divide {
      margin: 0 2px;
    }
  }
  .route-head {
    display: flex;
    align-items: center;
    height: 24px;
    margin-bottom: 6px;
    > span {
      display: inline-block;
    }
    &.warehouse-route-head .route-point-name {
      width: 100%;
    }
    .route-point-name {
      width: 124px;
      font-size: 18px;
      color: #33ffff;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .route-tag {
      height: 24px;
      line-height: 24px;
      padding: 0 8px;
      background: rgba(51, 255, 255, 0.2);
      border: 1px solid #33ffff;
      border-radius: 4px;
      margin-left: 8px;
      font-size: 14px;
      color: #ffffff;
    }
    i {
      margin-left: 8px;
      vertical-align: middle;
      cursor: pointer;
    }
  }
}
</style>
<style lang="scss">
.el-popover.el-popper.send-popper {
  width: 214px;
  background-image: linear-gradient(
    180deg,
    rgba(6, 29, 54, 1) 2%,
    rgba(5, 24, 50, 1) 100%
  );
  border: 1px solid #00c1ff;
  box-shadow: inset 0 4px 20px 0 rgba(35, 81, 239, 0.25);
  color: #fff;
  font-size: 14px;
  .el-button--text,
  .el-button--text:focus,
  .el-button--text:hover {
    color: #fff;
  }
  .el-button--primary,
  .el-button--primary:focus,
  .el-button--primary:hover {
    background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
    border: 0;
  }
  .popper__arrow {
    border-bottom-color: #00c1ff;
  }
  .popper__arrow::after {
    border-bottom-color: rgba(5, 24, 50, 1);
  }
}
</style>