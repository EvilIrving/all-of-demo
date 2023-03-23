<template>
  <div class="map-wrap">
    <div class="map" id="mainView"></div>
    <!-- 水情弹框 -->
    <!-- <div id="popup" class="ol-popup">
      <div class="popup-wrap" style="padding: 10px">
        <div class="content">
          <p class="title">{{ popupData.stName }}</p>
          <div class="line" />
          <p>当前水位：{{ popupData.z }}m</p>
          <p>汛限水位：{{ popupData.limitLevel }}m</p>
          <p>
            防洪高水位：{{
              popupData.floodLevel == null ? "-" : popupData.floodLevel
            }}m
          </p>
        </div>
        <div class="video" />
        <div @click="openDiaspatchSchema" class="btn" />
      </div>
      <div class="line-tip" />
    </div> -->
    <!-- 调度方案弹框 -->
    <dispatch-dialog
      :lat="dispatchLat"
      :lng="dispatchLng"
      v-if="dispatchVisible"
      :dialogVisible="dispatchVisible"
      @handleClose="dispatchClose"
      @dispatchInfo="dispatchInfo"
    ></dispatch-dialog>
    <!-- 点选框2 -->
    <div id="popup2" class="ol-popup2">
      <div class="content-wrap">
        <p class="title">{{ featureInfo.title }}</p>
        <div class="flex-box">
          <div v-for="(value, key) in featureInfo.data" :key="key">
            <p>{{ key }}</p>
            <p>{{ value }}</p>
          </div>
          <!-- <div>
            <p>当前水位</p>
            <p>20m</p>
          </div>
          <div>
            <p>当前水位</p>
            <p>20m</p>
          </div> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import { waterDisPreApi } from "@/api/disPrvtApi";

import "ol/ol.css";
import { Map, View, Overlay } from "ol";
import XYZ from "ol/source/XYZ";
import TileLayer from "ol/layer/Tile";
import { defaults as defaultControls } from "ol/control";
import {
  drawMapPoint,
  drawLine,
  drawJgRainPoint,
  drawJgWaterPoint,
  drawQxWaterPoint,
  drawRoutePoint,
} from "@/utils/mapUtil.js";
import DispatchDialog from "./DispatchDialog";
import { drawLhddPoint } from "@/utils/mapUtil.js";
import axios from "axios";
export default {
  name: "ScreenMap",
  components: {
    DispatchDialog,
  },
  props: {
    typeList: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      //地图
      serviceLink: "http://112.17.127.75:8008/arcgis/rest/services/",
      pointLayer_warehouse: null, //仓库点位图层
      pointLayer_team: null, //队伍点位图层
      pointLayer_rain: null, //雨情点位图层
      pointLayer_water: null, //水情点位图层
      map: null,
      popupData: {},
      dispatchVisible: false,
      routeLayer: null, //路线图层
      lineLayer_team: null, //人员调度路线图层
      lineLayer_warehouse: null, //仓库调度路线图层
      pointLayer_team_start: null, //队伍起点
      pointLayer_warehouse_start: null, //仓库起点
      pointLayer_station_end: null, //测站终点
      checkedList: [], //选中的点位类型
      featureInfo: {
        title: "",
        data: {},
      },
      stCode: "", //打开调度方案的测站编码
      qdLayer: null,
      dispatchLat: "",
      dispatchLng: "",
    };
  },
  created() { },
  computed: {},
  watch: {
    typeList(val) {
      this.checkPointType(val);
    },
    checkedList(newVal, oldVal) {
      // 判断多选状态
      this.checkChange(newVal, oldVal);
    },
  },
  mounted() {
    this.checkPointType(this.typeList);
    this.mapInit();
    this.qdPoint();
    // this.getRouterLine(
    //   [119.88841967773439, 27.636412231445316],
    //   [120.68904833984377, 28.22280749511719]
    // );
  },
  methods: {
    async qdPoint() {
      let qiaoDun = [
        {
          drp: 200,
          flag: "rain",
          intv: null,
          lat: 27.510837,
          lng: 120.298002,
          // lat: 27.475837,
          // lng: 120.303302,
          mgstcd: "SWZ_05821",
          stType: "PP",
          stationName: "桥墩(北斗)",
          stcd: "SWZ_05821",
          tm: "2021-02-04 15:05:59",
          uploadTime: null,
          warning: false,
          warnStatus: 1,
        },
      ];
      this.qdLayer = drawLhddPoint(qiaoDun, "water");
      this.map.addLayer(this.qdLayer);
    },
    // 判断多选状态 增加或减去图层
    checkChange(newVal, oldVal) {
      if (newVal.length < oldVal.length) {
        let newArr = oldVal.filter((v) => {
          return newVal.indexOf(v) == -1;
        });
        switch (newArr[0]) {
          case "防汛仓库":
            this.map.removeLayer(this.pointLayer_warehouse);
            break;
          case "抢险队伍":
            this.map.removeLayer(this.pointLayer_team);
            break;
          case "雨情":
            this.map.removeLayer(this.pointLayer_rain);
            break;
          case "水情":
            this.map.removeLayer(this.pointLayer_water);
            break;
        }
      }
    },
    //初始化地图
    mapInit() {
      this.map = new Map({
        target: "mainView",
        //地图控件
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          new TileLayer(
            {
              source: new XYZ({
                url:
                  "http://117.149.227.112:6080/arcgis/rest/services/mapservice/wenzhou_fangxun/MapServer/tile/{z}/{y}/{x}",
                projection: "EPSG:4326",
              }),
            },
            { zoomOffset: 1 }
          ),
        ],
        view: new View({
          projection: "EPSG:4326", //使用这个坐标系
          center: [120.527, 27.86],
          zoom: 9.5,
          maxZoom: 18,
          minZoom: 9.5,
        }),
      });
      this.mapClick(); // 初始化地图成功后，给地图添加点击事件
    },
    // 地图点击事件
    mapClick() {
      this.map.on("singleclick", (evt) => {
        let pixel = this.map.getEventPixel(evt.originalEvent);
        let feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          return feature;
        });

        //设置overlay
        // let pop_container = document.getElementById("popup");
        // let overlay = new Overlay({
        //   element: pop_container,
        //   autoPan: true,
        //   autoPanAnimation: {
        //     duration: 250,
        //   },
        //   positioning: "bottom-center",
        //   offset: [-5, -105],
        // });
        // this.map.addOverlay(overlay);

        let pop_container2 = document.getElementById("popup2");
        let overlay2 = new Overlay({
          element: pop_container2,
          autoPan: true,
          autoPanAnimation: {
            duration: 250,
          },
          positioning: "bottom-center",
          offset: [5, 0],
        });
        this.map.addOverlay(overlay2);

        //点位信息
        console.log("feature: ", feature);
        //是否存在坐标
        if (feature == undefined) {
          // overlay.setPosition(undefined);
          this.dispatchVisible = true;
          this.dispatchLat = evt.coordinate[1].toFixed(3);
          this.dispatchLng = evt.coordinate[0].toFixed(3);
          return false;
        } else {
          let lng = feature.values_.featureProperties.lng;
          let lat = feature.values_.featureProperties.lat;
          if (lng && lat) {
            this.popupData = feature.values_.featureProperties.data;
            let popType = feature.values_.featureProperties.type;
            this.featureInfo.data = {};
            switch (popType) {
              //仓库
              case "1":
                this.featureInfo.title = this.popupData.name;
                this.featureInfo.data[
                  "仓库联系人"
                ] = this.popupData.chargePerson;
                this.featureInfo.data["联系方式"] = this.popupData.chargePhone;
                this.featureInfo.data[
                  "物资总价值"
                ] = this.popupData.materialPrice;
                overlay2.setPosition([lng, lat]);
                break;
              //队伍
              case "2":
                this.featureInfo.title = this.popupData.name;
                this.featureInfo.data[
                  "救援队人数"
                ] = this.popupData.existingNumber;
                this.featureInfo.data["联系人"] = this.popupData.chargePerson;
                this.featureInfo.data["联系方式"] = this.popupData.chargePhone;
                // overlay2.setPosition([lng, lat]);
                overlay2.setPosition([lng, lat]);
                break;
              case "stationPoint":
                this.stCode = this.popupData.stCode;
                // overlay.setPosition([lng, lat]);
                break;
            }
          }
        }
      });
    },
    // 设置地图位置
    setView(lng, lat) {
      const to = [lng, lat];
      const view = this.map.getView();
      view.animate({
        center: to,
        duration: 200,
        zoom: 15,
      });
    },

    // 地图打点
    showPoint(type, arr) {
      // this.map.removeLayer(this.pointLayer);
      let data = [];
      switch (type) {
        case "warehouse":
          this.map.removeLayer(this.pointLayer_warehouse);
          this.pointLayer_warehouse = drawQxWaterPoint(arr, "1");
          this.map.addLayer(this.pointLayer_warehouse);
          break;
        case "team":
          this.map.removeLayer(this.pointLayer_team);
          this.pointLayer_team = drawQxWaterPoint(arr, "2");
          this.map.addLayer(this.pointLayer_team);
          break;
        case "water":
          this.map.removeLayer(this.pointLayer_water);
          this.pointLayer_water = drawJgWaterPoint(arr);
          this.map.addLayer(this.pointLayer_water);
          break;
      }
    },
    // 雨情打点
    showRainPoint(data) {
      console.log(data, 888);
      this.map.removeLayer(this.pointLayer_rain);
      this.pointLayer_rain = drawJgRainPoint(data);
      this.map.addLayer(this.pointLayer_rain);
    },

    //调度方案打开
    openDiaspatchSchema() {
      this.dispatchVisible = true;
    },
    //调度方案关闭
    dispatchClose() {
      this.dispatchVisible = false;
    },
    //获取路线的方法
    async getRouterLine(startPoint, endPoint, type) {
      await axios({
        url: `https://restapi.amap.com/v3/direction/driving?origin=${startPoint[0]},${startPoint[1]}&destination=${endPoint[0]},${endPoint[1]}&output=xml&key=${this.$config.gaoDeKey}&output=json`,
        method: "get",
      }).then((res) => {
        // console.log(res);
        if (res.data.route.paths.length > 0) {
          const route = res.data.route;
          const steps = route.paths[0].steps;
          let routeLine = [];
          steps.map((item) => {
            const tempArr = item.polyline.split(";");
            tempArr.map((child) => {
              let tempPoint = child.split(",");
              tempPoint[0] = Number(tempPoint[0]);
              tempPoint[1] = Number(tempPoint[1]);
              routeLine.push(tempPoint);
            });
          });
          if (type == "1") {
            //队伍展示路线
            this.map.removeLayer(this.lineLayer_team);
            this.lineLayer_team = drawLine(routeLine, { lineColor: "#3CC5FF" });
            this.map.addLayer(this.lineLayer_team);
            //存储队伍调度路线详情
            const teamInfoList = [];
            for (let i = 0; i < route.paths.length; i++) {
              const distance = Number(route.paths[i].distance) / 1000;
              const hour = Math.floor(Number(route.paths[i].duration) / 3600);
              const minute = Math.floor(
                (Number(route.paths[i].duration) % 3600) / 60
              );
              const duration = hour + "小时" + +minute + "分钟";
              const routeInfoItem = {
                taxi_cost: route.taxi_cost,
                distance: distance,
                traffic_lights: route.paths[i].traffic_lights,
                duration: duration,
              };
              teamInfoList.push(routeInfoItem);
            }
            this.$store.commit("TEAM_DISPATCH_LIST", teamInfoList);
          } else {
            //仓库展示路线
            this.map.removeLayer(this.lineLayer_warehouse);
            this.lineLayer_warehouse = drawLine(routeLine, {
              lineColor: "#FFD13C",
            });
            this.map.addLayer(this.lineLayer_warehouse);
            //存储仓库调度路线详情
            const warehoureInfoList = [];
            for (let i = 0; i < route.paths.length; i++) {
              const distance = Number(route.paths[i].distance) / 1000;
              const hour = Math.floor(Number(route.paths[i].duration) / 3600);
              const minute = Math.floor(
                (Number(route.paths[i].duration) % 3600) / 60
              );
              const duration = hour + "小时" + +minute + "分钟";
              const routeInfoItem = {
                taxi_cost: route.taxi_cost,
                distance: distance,
                traffic_lights: route.paths[i].traffic_lights,
                duration: duration,
              };
              warehoureInfoList.push(routeInfoItem);
            }
            this.$store.commit("WAREHOUSE_DISPATCH_LIST", warehoureInfoList);
          }
        }
      });
    },
    // 判断显示点类型
    checkPointType(typeList) {
      this.checkedList = [];
      this.typeList.map((v) => {
        this.checkedList.push(v.name);
      });
    },
    //获取调度信息
    dispatchInfo(data, stationInfo) {
      //清空仓库队伍测站图层
      this.map.removeLayer(this.pointLayer_warehouse);
      this.map.removeLayer(this.pointLayer_team);
      this.map.removeLayer(this.pointLayer_water);
      this.checkedList = [];

      const stationPos = [stationInfo.lng, stationInfo.lat];
      //人员调度
      const peoplePos = [data.teamInfo.longitude, data.teamInfo.latitude];
      //设置队伍起点
      this.pointLayer_team_start &&
        this.map.removeLayer(this.pointLayer_team_start);
      this.pointLayer_team_start = drawRoutePoint(peoplePos, 1);
      this.map.addLayer(this.pointLayer_team_start);
      //绘制队伍路线
      this.getRouterLine(peoplePos, stationPos, 1);
      let wareFlag = true;
      if (data.warehouses.length > 0) {
        //物资调度
        const wareHousePos = [
          data.warehouses[0].longitude,
          data.warehouses[0].latitude,
        ];

        //设置仓库起点
        this.pointLayer_warehouse_start &&
          this.map.removeLayer(this.pointLayer_warehouse_end);
        this.pointLayer_warehouse_start = drawRoutePoint(wareHousePos, 2);
        this.map.addLayer(this.pointLayer_warehouse_start);
        //绘制仓库路线
        this.getRouterLine(wareHousePos, stationPos, 2);
      } else {
        wareFlag = false;
      }

      //设置测站终点
      this.pointLayer_station_end &&
        this.map.removeLayer(this.pointLayer_station_end);
      this.pointLayer_station_end = drawRoutePoint(stationPos, 3);
      this.map.addLayer(this.pointLayer_station_end);
      //侧边栏增加调度tab
      this.$emit("showDispatchTab", wareFlag);
    },
  },
};
</script>
<style lang='scss' scoped>
.map-wrap {
  width: 100%;
  height: 100%;
  .map {
    width: 100%;
    height: 100%;
    .ol-popup {
      position: relative;
      width: 490px;
      height: 210px;
      .line-tip {
        position: absolute;
        bottom: -140px;
        left: 213px;
        width: 62px;
        height: 132px;
        background: url("../../../../assets/images/popup-line.png") no-repeat
          center;
        background-size: auto 132px;
      }
      .popup-wrap {
        position: relative;
        width: 490px;
        height: 210px;
        background: url("../../../../assets/images/popup-bg.png") no-repeat
          center;
        background-size: 100% 100%;
        .content {
          position: relative;
          float: right;
          width: 250px;
          height: 100%;
          display: flex;
          display: flex;
          flex-direction: column;
          flex-wrap: nowrap;
          justify-content: space-around;
          .title {
            font-size: 32px;
            color: #50bcea;
          }
          .line {
            background: #d8d8d8;
            height: 1px;
            width: 230px;
            background: url("../../../../assets/images/popup-line2.png")
              no-repeat center;
          }
          p {
            width: 100%;
            font-size: 14px;
            color: #a6dff6;
          }
        }
        .video {
          position: absolute;
          top: -26px;
          left: 0;
          width: 192px;
          height: 198px;
          background: url("../../../../assets/images/popup-video.png") no-repeat
            center;
          background-size: 100% 100%;
          cursor: pointer;
        }
        .btn {
          position: absolute;
          bottom: -42px;
          right: -42px;
          width: 84px;
          height: 84px;
          background: url("../../../../assets/images/popup-btn.png") no-repeat
            center;
          background-size: 100% 100%;
        }
      }
    }
    .ol-popup2 {
      position: relative;
      height: 79px;
      width: 1px;
      background: #239def;
      .content-wrap {
        position: absolute;
        bottom: 79px;
        left: 0;
        width: 320px;
        height: 130px;
        background: url("../../../../assets/images/popup-bg2.png") no-repeat
          left top;
        background-size: 100% 100%;
        color: #239def;
        padding: 20px;
        .title {
          font-size: 22px;
          color: #50bcea;
        }
        .flex-box {
          width: 100%;
          height: 60px;
          margin-top: 10px;
          display: flex;
          & > div {
            flex: 1;
            text-align: center;
            display: flex;
            justify-content: space-around;
            flex-direction: column;
          }
        }
      }
    }
  }
}
</style>
