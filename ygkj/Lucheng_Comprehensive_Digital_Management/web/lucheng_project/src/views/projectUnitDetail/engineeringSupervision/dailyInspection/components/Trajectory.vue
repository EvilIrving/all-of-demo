<!--
 * @Author: wanxin
 * @LastEditTime: 2022-06-27 14:27:21
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\engineeringSupervision\dailyInspection\components\Trajectory.vue
-->
<template>
  <div class="map_wrap">
    <div class="map" id="mainView"></div>
  </div>
</template>

<script>
import "ol/ol.css";
import { Map, View } from "ol";
import XYZ from "ol/source/XYZ";
import Tile from "ol/layer/Tile";
import { defaults as defaultControls } from "ol/control";
import { DoubleClickZoom } from "ol/interaction";
import { drawPoint, drawLine } from "@/utils/mapUtil";
import { checkTrack } from "@/api/projectUnitDetail.js";
export default {
  name: "Trajectory",
  props: {
    id: {
      type: String,
      default: "",
    },
  },
  components: {},
  data() {
    return {
      map: null,
      pointLayer: null,
      linesLayer: null,
      pointPolygonLayer: null,
      polygonLayer: null,
      defaultCenter: {
        // lnglat: [119.555, 30.676],
        lnglat: [119.755, 30.676],
        zoom: 14,
      },
      pointData_a: {},
      pointData_b: {},
    };
  },
  computed: {
    seawallDetail() {
      return this.$store.state.seawallDetail;
    },
  },
  async mounted() {
    console.log(this.id);
    console.log(this.seawallDetail, "====");
    await this.mapInit();
    await this.getCheckTrack();
  },
  methods: {
    async getCheckTrack() {
      let res = await checkTrack({
        id: this.id,
      });
      if (res.code == 0) {
        console.log(res.data);
        if (res.data.start) {
          let location = res.data.start;
          this._setView(location.lgtd, location.lttd);
        }
        let list = res.data.position;
        if (list && list.length > 0) {
          list.map((item) => {
            item.lng = item.lgtd;
            item.lat = item.lttd;
            item.coordinates = [item.lng, item.lat];
            item.coordinates.push();
            // item.stnm = item.projectPosition
          });
          this._drawLines(list);
        }
        if (res.data.list && res.data.list.length > 0) {
          let coordinates = res.data.list;
          coordinates.map((item) => {
            item.lng = item.lgtd;
            item.lat = item.lttd;
            item.stnm = item.projectPosition;
          });
          this._drawPoints(coordinates);
        }
      }
    },
    //初始化地图
    mapInit() {
      // 地图
      this.map = new Map({
        target: "mainView",
        //地图控件
        // controls: defaultControls({ attribution: true, zoom: true }),
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          new Tile({
            source: new XYZ({
              // url: "https://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              url: "https://t0.tianditu.gov.cn/DataServer?T=img_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              projection: "EPSG:4326",
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),

          // //绿色水利地图
          // new Tile({
          //   source: new XYZ({
          //     url: "http://223.4.72.162:16080/arcgis/rest/services/basemap/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}",
          //     projection: "EPSG:4326",
          //     crossOrigin: "anonymous",
          //   }),
          //   preload: 1,
          //   visible: true,
          // })
        ],
        view: new View({
          projection: "EPSG:4326", //使用这个坐标系
          center: this.defaultCenter.lnglat,
          zoom: this.defaultCenter.zoom,
          maxZoom: 18,
          minZoom: 0,
        }),
      });

      // 禁用地图双击缩放
      const dblClickInteraction = this.map
        .getInteractions()
        .getArray()
        .find((interaction) => {
          return interaction instanceof DoubleClickZoom;
        });
      this.map.removeInteraction(dblClickInteraction);

      // 监听事件
      this.mapClick(); // 初始化地图成功后，给地图添加点击事件

      // 鼠标移入
      this.map.on("pointermove", (e) => {
        var pixel = this.map.getEventPixel(e.originalEvent);
        var hit = this.map.hasFeatureAtPixel(pixel);
        if (hit) {
          this.map.getTargetElement().style.cursor = "pointer";
        } else {
          this.map.getTargetElement().style.cursor = "";
        }
      });

      // this._setView(this.seawallDetail.longitude, this.seawallDetail.latitude);
    },
    // 地图点击事件
    mapClick() {
      this.map.on("singleclick", (evt) => {
        var pixel = this.map.getEventPixel(evt.originalEvent);
        var feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          return feature;
        });
        // this.closePopup()
        if (
          feature &&
          feature.values_.features &&
          feature.values_.features.length > 0
        ) {
          if (feature.values_.features.length == 1) {
            //点击单个图标时
            // let pointData = feature.values_.features[0].values_.data;
            // const coordinate = evt.coordinate; // 获取坐标
            // const hdms = toStringHDMS(toLonLat([pointData.lng,pointData.lat])); // 转换坐标格式
            // this.currentCoordinate = hdms; // 保存坐标点
            // this.popupData = pointData
            // setTimeout(() => {
            //   this.overlay.setPosition([pointData.lng,pointData.lat]);
            // });
          } else {
            //点击聚合图标时
            this.map.getView().fit(feature.get("features")[0].getGeometry(), {
              maxZoom: this.map.getView().getZoom() + 1,
              duration: 250,
            });
          }
        } else {
          // let pointData = feature.values_.data;
          // this.popupData = pointData
          // const hdms = toStringHDMS(toLonLat([pointData.lng,pointData.lat])); // 转换坐标格式
          // this.currentCoordinate = hdms; // 保存坐标点
          // setTimeout(() => {
          //   this.overlay.setPosition([pointData.lng,pointData.lat]);
          // });
        }
      });
    },
    //打点不聚合
    _drawPoints(coordinates) {
      // 点
      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: require("@/assets/images/xc-normal.png"), //图片路径
        title: "",
        scale: 1,
      };
      this.pointLayer = drawPoint(coordinates, styleObj);
      this.map.addLayer(this.pointLayer);
    },

    // 画线
    _drawLines(list) {
      console.log(list);
      this.map.removeLayer(this.linesLayer);
      // let arr = []
      // let coordinateObj = {}
      let coordinates = [];
      for (let i = 0; i < list.length; i++) {
        // list[i].coordinates = [];
        // list[i].coordinates.push([list[i].lgtd,list[i].lttd])
        coordinates.push([list[i].lgtd, list[i].lttd]);
      }
      // coordinateObj.coordinates = coordinates
      // arr.push(coordinateObj)
      // // console.log(list,'list');
      // console.log(arr,'coordinates');
      this.linesLayer = drawLine(coordinates);
      this.map.addLayer(this.linesLayer);
    },

    _setView(lng, lat) {
      const to = [lng, lat];
      const view = this.map.getView();
      let obj = {
        center: to,
        duration: 0,
      };
      view.animate(obj);
    },
  },
};
</script>

<style lang='scss' scoped>
.map_wrap {
  //   background: #fdfdf1;
  position: relative;
  width: 100%;
  height: 500px;
  .map {
    width: 100%;
    height: 100%;
  }
}
</style>