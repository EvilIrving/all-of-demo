<template>
  <div class="map-wrap">
    <div class="map" id="mainView"></div>
  </div>
</template>

<script>
import { dispatchApi } from "@/api/dataScreen/jointDispatch/JointDispatch";
import "ol/ol.css";
import { Map, View, Overlay } from "ol";
import XYZ from "ol/source/XYZ";
import TileLayer from "ol/layer/Tile";
import { defaults as defaultControls } from "ol/control";

import { drawLhddPoint } from "@/utils/mapUtil.js";

export default {
  name: "ScreenMap",
  components: {},
  props: {
    pageType: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      //地图
      serviceLink: "http://112.17.127.75:8008/arcgis/rest/services/",
      reserPointLayer: null, //水情分析图层
      map: null,
    };
  },
  created() {},
  mounted() {
    this.mapInit();
    this.waterPoint();
  },
  watch: {
    pageType: function (val) {
      if (val == "water") {
        this.waterPoint();
      }
    },
  },
  methods: {
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
          zoom: 9.5,
        }),
      });
      this.mapClick(); // 初始化地图成功后，给地图添加点击事件
    },
    //地图点击事件
    mapClick() {
      this.map.on("singleclick", (e) => {
        //点击事件
        //to do
      });
    },
    //关闭图层
    dispatchClose() {},

    // 设置地图位置
    setView(lng, lat) {
      const to = [lng, lat];
      const view = this.map.getView();
      view.animate({
        center: to,
        duration: 200,
        zoom: 14,
      });
    },
    /**
     * 显示点位
     */
    async waterPoint() {
      this.cleanAllLayer();
      dispatchApi.mapPoint({}).then((res) => {
        let index = res.data.findIndex(
          (item) => item.projectName == "桥墩水库"
        );
        res.data[index].warnStatus = 1;
        this.reserPointLayer = drawLhddPoint(res.data, "reservoir");
        this.map.addLayer(this.reserPointLayer);
      });
    },
    //清理所有图层
    cleanAllLayer() {
      this.map.removeLayer(this.reserPointLayer);
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
  }
}
</style>
