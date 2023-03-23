<template>
  <div class="generalization-map">
    <div class="map" id="GeneralzationView" />

    <!-- 图例 -->
    <div class="map-legend" :style="{ right: '1rem' }">
      <primary-legend title="图例" ref="mapLegend">
        <ul>
          <li v-for="(subLegends, subIndex) in legends" :key="subIndex">
            <div class="legend-left">{{ subLegends.label }}</div>
            <div class="legend-right">
              <div
                v-for="(item, index) in subLegends.children"
                class="legend-item"
                :key="index"
              >
                <i :class="item.icon" />{{ item.label || "" }}
              </div>
            </div>
          </li>
        </ul>
      </primary-legend>
    </div>

    <screen-footer :map-bind="map" />

    <div v-show="popupShow" id="popup" class="ol-popup">
      <div class="ol-popup__wrapper">
        <div class="ol-popup__title">{{ featureInfo.name }}</div>
        <div class="ol-popup__content">
          <div class="ol-popup__content--props">
            <div :key="key" v-for="(prop, key) of featureInfo.props" class="props-item">
              <span class="item-label">{{ key }}</span>
              <span class="item-data">
                {{ prop }}
              </span>
            </div>
            <div v-if="featureInfo.label" class="props-label">
              <span> {{ featureInfo.label }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "ol/ol.css";

import XYZ from "ol/source/XYZ";
import TileLayer from "ol/layer/Tile";
import { Map, View, Overlay, Feature } from "ol";
import { LineString } from "ol/geom";
import { Vector as VectorLayer } from "ol/layer";
import ImageLayer from "ol/layer/Image";
import ImageStatic from "ol/source/ImageStatic";
import { Vector as VectorSource } from "ol/source";
import { defaults as defaultControls } from "ol/control";
import { Style, Stroke } from "ol/style";

import { ScreenFooter, PrimaryLegend } from "@/components";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention/index";
import {
  drawPoint,
  addLayerClick,
  drawLines,
  showGeneralizaPoint,
} from "@/utils/mapUtil.js";
import { mapGetters } from "vuex";
import fyjLines from "@/assets/generate_point/feiyunRiver_line_Project.json";
import fyjPoints from "@/assets/generate_point/feiyunRiver_point_Project.json";
import ojLines from "@/assets/generate_point/ouRiver_line_Project.json";
import ojPoints from "@/assets/generate_point/ouRiver_point_Project.json";
import ajLines from "@/assets/generate_point/aojiang_line_Project.json";
import ajPoints from "@/assets/generate_point/aojiang_point_Project.json";

export default {
  name: "ScreenMap",
  components: { ScreenFooter, PrimaryLegend },
  props: {},
  data() {
    return {
      //地图
      map: null,
      //点位信息
      featureInfo: {},
      popupShow: false,
      overlay: null,
      baseLineLayer: null,
      linePointArr: [
        //最长的横线
        {
          coordinates: [
            [120.07558194383863, 27.66076640751137],
            [121.66987877721849, 27.66076640751137],
          ],
        },
        //左侧第一段向下折线
        {
          coordinates: [
            [120.26382140099008, 27.66076640751137],
            [120.26382140099008, 27.51049221138277],
            [120.08640798317978, 27.51049221138277],
          ],
        },
        // 和一水闸
        {
          coordinates: [
            [120.35354189340357, 27.66076640751137],
            [120.35354189340357, 27.692467027909938],
          ],
        },
        //岩门水闸
        {
          coordinates: [
            [120.42282965858381, 27.66076640751137],
            [120.42282965858381, 27.60426902278397],
          ],
        },
        //卧旗水闸
        {
          coordinates: [
            [120.46733521349103, 27.66076640751137],
            [120.46733521349103, 27.60426902278397],
          ],
        },
        //温州潮位站
        {
          coordinates: [
            [120.683076132255, 27.66076640751137],
            [120.683076132255, 27.68405697423059],
          ],
        },
        //上下线(江心屿)
        {
          coordinates: [
            [120.73634389951702, 27.64323715455258],
            [120.73732761683944, 27.73277369569227],
          ],
        },
        //罗浦水闸
        {
          coordinates: [
            [120.84978538370362, 27.66076640751137],
            [120.84978538370362, 27.689492297669005],
          ],
        },
        //中间向上长段(罗溪)
        {
          coordinates: [
            [120.8891395350689, 27.66076640751137],
            [120.8901133057618, 28.083330865036572],
          ],
        },
        {
          coordinates: [
            [120.66540197788227, 28.082518332025675],
            [120.88919215291311, 28.082518332025675],
          ],
        },
        {
          coordinates: [
            [120.55218257534983, 28.01140174664565],
            [120.88957409000984, 28.012274794310215],
          ],
        },
        {
          coordinates: [
            [120.70908773829642, 27.938275931958913],
            [120.8897514105115, 27.93900326516535],
          ],
        },
        {
          coordinates: [
            [120.77310113388712, 27.862187759814212],
            [120.88934551510773, 27.862804990839336],
          ],
        },
        //中间向下5段
        {
          coordinates: [
            [120.92764229668623, 27.66076640751137],
            [120.92764229668623, 27.57461785579188],
          ],
        },
        {
          coordinates: [
            [120.97452092669322, 27.66076640751137],
            [120.97452092669322, 27.574859504528426],
          ],
        },
        {
          coordinates: [
            [121.04702051455844, 27.66076640751137],
            [121.04702051455844, 27.621773045380586],
          ],
        },
        {
          coordinates: [
            [121.08973320665443, 27.66076640751137],
            [121.08973320665443, 27.621930554562287],
          ],
        },
        {
          coordinates: [
            [121.11819302372028, 27.66076640751137],
            [121.1178546422469, 27.621559926732196],
          ],
        },

        //右侧向上折线
        {
          coordinates: [
            [121.13624019467842, 27.66076640751137],
            [121.13624019467842, 27.77217053118829],
            [121.36130022534535, 27.77217053118829],
          ],
        },
        //折线中向上
        {
          coordinates: [
            [121.1555212887081, 27.77217053118829],
            [121.1555212887081, 27.798987556742322],
          ],
        },
        {
          coordinates: [
            [121.17458223890392, 27.77217053118829],
            [121.17458223890392, 27.798987556742322],
          ],
        },
        {
          coordinates: [
            [121.24369059386181, 27.77217053118829],
            [121.24369059386181, 27.812138916509205],
          ],
        },
        {
          coordinates: [
            [121.26985652252054, 27.77217053118829],
            [121.26985652252054, 27.812138916509205],
          ],
        },
        {
          coordinates: [
            [121.32464334844714, 27.77217053118829],
            [121.32464334844714, 27.869446531987403],
          ],
        },
        //折线中向下
        {
          coordinates: [
            [121.19930309150972, 27.77217053118829],
            [121.19930309150972, 27.748873814804224],
          ],
        },
        {
          coordinates: [
            [121.29706756948515, 27.77217053118829],
            [121.29706756948515, 27.748873814804224],
          ],
        },

        //蓝田
        {
          coordinates: [
            [121.21500690469371, 27.66076640751137],
            [121.21500690469371, 27.625225345064173],
          ],
        },
        //永强塘
        {
          coordinates: [
            [121.23082805454214, 27.66076640751137],
            [121.23082805454214, 27.58003272026099],
          ],
        },
        //海思
        {
          coordinates: [
            [121.27972198587952, 27.66076640751137],
            [121.27972198587952, 27.68280341850075],
          ],
        },
        //最右边向下竖线
        {
          coordinates: [
            [121.47739273181193, 27.66076640751137],
            [121.47739273181193, 27.409140010550363],
          ],
        },
        {
          coordinates: [
            [121.47739273181193, 27.6181791299812],
            [121.50584091266657, 27.6181791299812],
          ],
        },
        {
          coordinates: [
            [121.47739273181193, 27.592913819685528],
            [121.50643366199276, 27.592913819685528],
          ],
        },
        {
          coordinates: [
            [121.47739273181193, 27.536595728858305],
            [121.50584091266657, 27.536595728858305],
          ],
        },
        {
          coordinates: [
            [121.47739273181193, 27.472810928690663],
            [121.5112132331519, 27.472810928690663],
          ],
        },
        {
          coordinates: [
            [121.47739273181193, 27.435673977280583],
            [121.5423095138614, 27.435673977280583],
          ],
        },
      ],
      legends: {
        水库: {
          label: "水库",
          children: [
            {
              icon: "icon-general-sk",
              label: "正常",
            },
            {
              icon: "icon-general-sk-flood",
              label: "泄洪",
            },
            {
              icon: "icon-general-sk-abnormal",
              label: "超汛限",
            },
          ],
        },
        水闸: {
          label: "水闸",
          children: [
            {
              icon: "icon-general-gate",
              label: "正常",
            },
            {
              icon: "icon-general-sk-open",
              label: "开启",
            },
          ],
        },
        潮位: {
          label: "潮位",
          children: [
            {
              icon: "icon-general-chaowei",
              label: "正常",
            },
            {
              icon: "icon-general-chaowei-abnormal",
              label: "超警",
            },
          ],
        },
        水文: {
          label: "水文",
          children: [
            {
              icon: "icon-general-hydrology",
              label: "正常",
            },
            {
              icon: "icon-general-hydrology-abnormal",
              label: "超警",
            },
          ],
        },
      },
      pointArr: [],
      generalPointLayer: null,
    };
  },
  created() {},
  mounted() {
    this.mapInit();
    this.$nextTick(() => {
      this.formateLine(ojLines);
      this.formatePoints(ojPoints);
      this.getHydrProjStaDiagram("瓯江");

    });
  },
  computed: {
    ...mapGetters(["basinArea", "generalInfoArr"]),
    isBigScrren() {
      return this.$route.query.large == "true";
    },
  },
  watch: {
    basinArea(val) {
      this.getHydrProjStaDiagram(val);
      switch (val) {
        case "瓯江":
          {
            this.formateLine(ojLines);
            this.formatePoints(ojPoints);
            this.flyPoint([120.54361652790057, 27.686108565213664], 11);
          }
          break;
        case "鳌江":
          {
            this.formateLine(ajLines);
            this.formatePoints(ajPoints);
            this.flyPoint([120.44361652790057, 27.636108565213664], 11.5);
          }
          break;
        case "飞云江":
          {
            this.formateLine(fyjLines);
            this.formatePoints(fyjPoints);
            this.flyPoint([120.44361652790057, 27.636108565213664], 11.5);
          }
          break;
      }
    },
    generalInfoArr(val) {
      this.showGeneralPoint();
      // console.log(val,2333)
    },
  },
  methods: {
    /**
     * 初始化地图
     */
    mapInit() {
      //地图主图层
      let mainLayer = new ImageLayer({
        name: "rainfall",
        source: new ImageStatic({
          url: require("@/assets/images/testbg.png"), //这里添加静态图片的地址
          projection: "EPSG:4326",
          imageExtent: [
            119.54142526282753,
            28.525295081408228,
            122.3166735752165,
            26.91993693683649,
          ], //WGS-84 经纬度数据范围
        }),
        opacity: 0.5,
        zIndex: 10,
      });
      // view设置
      let viewSetting = new View({
        projection: "EPSG:4326",
        center: [120.54361652790057, 27.686108565213664],
        zoom: 11.4,
        maxZoom: 14,
        minZoom: 11,
        extent: [118.245, 26.51, 122.752, 29.45],
      });
      this.map = new Map({
        target: "GeneralzationView",
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [mainLayer],
        view: viewSetting,
      });
      // this.drawPoint();
      this.registeredEvent(); //注册事件
      this.addStationOverlay();
    },
    //定位到对应位置和层级
    flyPoint(center, zoom) {
      const view = this.map.getView();
      view.animate({
        center: center,
        duration: 300,
        zoom: zoom,
      });
    },
    /**
     * 设置摄像机位置
     */
    setView(lng, lat) {
      const view = this.map.getView();
      view.animate({ center: [lng, lat], duration: 200, zoom: 14 });
    },
    addStationOverlay() {
      this.overlay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector("#popup"),
        autoPan: true,
        autoPanAnimation: {
          duration: 250,
        },
      });
      this.map.addOverlay(this.overlay);
    },
    /**
     * 注册事件
     */
    registeredEvent() {
      this.map.on("click", (event) => {
        // console.log(2222, event);
      });
      this.map.on("pointermove", (event) => {
        let pixel = this.map.getEventPixel(event.originalEvent);
        let flag = false;
        this.map.getTargetElement().style.cursor = "auto";
        this.map.forEachFeatureAtPixel(pixel, (feature) => {
          let coodinate = event.coordinate;
          let properties = feature.getProperties();
          if (feature && properties && properties.featureProperties.data) {
            let data = properties.featureProperties.data;
            this.map.getTargetElement().style.cursor = "pointer";
            this.featureInfo = {
              name: data.name,
              props: data.props,
              label: data.label || false,
            };
            if (this.featureInfo.name) {
              this.overlay.setPosition(coodinate);
              flag = true;
            }
          }
        });
        if (flag) {
          this.popupShow = true;
        } else {
          this.popupShow = false;
        }
      });
    },
    /**
     * 画点并将图层点位绑定事件
     * @param {Array} coordinate 点位列表
     * @param {Object} styleObj 样式对象
     * @param {String} layerName 点位图层名称，用于分辨
     */
    drawPoint(coordinates, styleObj, layerName = "pointLayer") {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);

      let pointLayer = drawPoint(coordinates, styleObj);
      pointLayer.set("name", layerName);
      pointLayer.setZIndex(10);
      this.map.addLayer(pointLayer);
      let selectClick = addLayerClick(pointLayer);
      selectClick.on("select", (e) => {
        // console.log(e);
        let features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          this.$emit("point-click", features[0].values_.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    drawBaseLine(arr) {
      if (this.baseLineLayer != null) this.map.removeLayer(this.baseLineLayer);
      this.baseLineLayer = drawLines(arr);
      this.map.addLayer(this.baseLineLayer);
    },
    drawFlowLine(arr) {
      if (this.baseLineLayer != null) this.map.removeLayer(this.baseLineLayer);

      let source = new VectorSource({ wrapX: false });
      this.baseLineLayer = new VectorLayer({
        source: source,
      });
      let outlineStroke = new Style({
        stroke: new Stroke({
          color: [25, 25, 255, 1],
          width: 5,
        }),
      });

      function getAnimationStrokeStyle(featureLine) {
        return new Style({
          stroke: new Stroke({
            color: [204, 204, 255, 1],
            width: 3,
            lineDash: [2, 7],
            lineDashOffset: featureLine.get("dashOffset"),
          }),
        });
      }

      function getStyle(featureLine) {
        return [outlineStroke, getAnimationStrokeStyle(featureLine)];
      }
      for (let i = 0; i < arr.length; i++) {
        let featureLine = new Feature({
          geometry: new LineString(arr[i].coordinates),
          dashOffset: 0,
        });
        featureLine.setStyle(getStyle);
        setInterval(function () {
          let offset = featureLine.get("dashOffset");
          offset = offset == 8 ? 0 : offset + 1;
          featureLine.set("dashOffset", offset);
        }, 100);
        source.addFeature(featureLine);
      }
      this.map.addLayer(this.baseLineLayer);
    },
    //将线geoJson数据转换格式
    formateLine(data) {
      let features = data.features;
      this.linePointArr = [];
      for (let i = 0; i < features.length; i++) {
        let obj = {
          coordinates: features[i].geometry.coordinates,
        };
        this.linePointArr.push(obj);
      }
      this.drawFlowLine(this.linePointArr);
    },
    formatePoints(data) {
      // console.log(data,333)
      // type:"闸门",
      // name:"灵昆",
      // coordinates:[121.19898850759421,27.745695514372578]
      let features = data.features;
      this.pointArr = [];
      for (let i = 0; i < features.length; i++) {
        let obj = {
          coordinates: features[i].geometry.coordinates,
          name: features[i].properties.name,
          pointType: features[i].properties.type,
          type: '概化图',
        };
        this.pointArr.push(obj);
      }
      this.showGeneralPoint();
    },
    showGeneralPoint() {
      this.pointArr.map((item) => {
        // console.log(item)
        let text = "";
        if (item.data) {
          if (this.generalInfoArr.indexOf("名称") != -1) {
            text += item.name;
          }
          if (this.generalInfoArr.indexOf("水位") != -1) {
            text = text + " " + this.isEmpty(item.data.waterLevel) + "m";
          }
          if (this.generalInfoArr.indexOf("流量") != -1) {
            text = text + " " + this.isEmpty(item.data.flow) + "m³/s";
          }
          item.text = text;
        } else {
          item.text = item.name;
        }
        return item;
      });
      // console.log(this.pointArr)
      if (this.generalPointLayer != null) this.map.removeLayer(this.generalPointLayer);
      this.generalPointLayer = showGeneralizaPoint(this.pointArr);
      this.map.addLayer(this.generalPointLayer);
    },
    isEmpty(value) {
      if (value) {
        return value;
      } else {
        return "-";
      }
    },
    async getHydrProjStaDiagram(data) {
      let opt = {
        bas: data,
      };
      let res = await disasterPreventionApi.getHydrProjStaDiagram(opt);
      const pointObj = res.data;
      this.pointArr.map((item) => {
        if (pointObj[item.name]) {
          // console.log(item.name,)
          item.data = pointObj[item.name];
          switch (item.type) {
            case "小型水库":
            case "中型水库":
            case "大型水库":
            case "水库":
              {
                item.props = {
                  水位: this.isEmpty(pointObj[item.name].waterLevel) + "m",
                  流量: this.isEmpty(pointObj[item.name].flow) + "m³/s",
                  入库流量: this.isEmpty(pointObj[item.inFlow]) + "m³/s",
                  出库流量: this.isEmpty(pointObj[item.outFlow]) + "m³/s",
                };
              }
              break;
            case "潮位":
              {
                item.props = {
                  水位: this.isEmpty(pointObj[item.name].waterLevel) + "m",
                };
              }
              break;
            case "水闸":
              {
                item.props = {
                  闸上水位: this.isEmpty(pointObj[item.name].upz) + "m",
                  闸下水位: this.isEmpty(pointObj[item.name].dwz) + "m",
                  过闸流量: this.isEmpty(pointObj[item.name].flow) + "m³/s",
                };
              }
              break;
            case "水文":
              {
                item.props = {
                  水位: this.isEmpty(pointObj[item.name].waterLevel) + "m",
                  流量: this.isEmpty(pointObj[item.name].flow) + "m³/s",
                };
              }
              break;
          }
        }
      });
      this.showGeneralPoint();
    },
  },
};
</script>
<style lang="scss" scoped>
.generalization-map {
  width: 100%;
  height: 100%;
  position: relative;
  .map {
    width: 100%;
    height: 100%;
  }
  .map-legend {
    position: absolute;
    bottom: 7%;
    width: 200px;
    z-index: 1000;
    ::v-deep .legend-item {
      i {
        display: inline-block;
        width: 24px;
        height: 24px;
      }
    }
    ul {
      overflow-y: auto;
      padding: 6px 12px;
      li {
        // align-items: center;
        @include flexbox;
        @include flexflow(column, nowrap);
        // border-bottom: 1px solid #00c1ff;
        &:last-child {
          border-bottom: none;
        }
        .legend-left {
          // padding: 10px 20px;
          // text-align: center;
          margin-left: 12px;
          font-size: 16px;
          color: #1cfff5;
          > p:nth-child(1) {
            font-family: PingFangSC-Semibold;
            font-size: 16px;
            color: #00c1ff;
            letter-spacing: 0;
            line-height: 32px;
            font-weight: 600;
            opacity: 1;
          }
          > p {
            padding: 5px 0;
            opacity: 0.6;
            color: #a6dff6;
            > i {
              margin-right: 10px;
            }
          }
        }
        .legend-right {
          // border-left: 1px solid #00c1ff;
          padding-left: 16px;
          flex-direction: row;
          span {
            width: 100%;
          }
          .legend-item {
            margin-right: 12px;
            .icon-sluice,
            .icon-sluice-warn {
              width: 24px;
              height: 24px;
            }
          }
        }
        .item-color-block {
          width: 25px;
          height: 25px;
        }
      }
    }
  }

  .ol-popup {
    position: absolute;
    bottom: 15px;
    left: -50%;
    transform: translateX(-50%);
    border-radius: 3px;
    min-width: 150px;
    .ol-popup__wrapper {
      background-image: linear-gradient(180deg, #0094c3 2%, #103567 100%);
      border: 2px solid #00c1ff;
      box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
      border-radius: 3px;
      height: 100%;
      width: 100%;
      padding: 16px;
      position: relative;
      opacity: 0.9;
      &:before {
        content: "";
        position: absolute;
        display: block;
        box-sizing: content-box;
        width: 0px;
        height: 0px;
        border: 7px solid transparent;
        border-top: 7px solid #103567;
        border-bottom: 0;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%) translateY(100%);
        z-index: 100;
      }
      &:after {
        content: "";
        position: absolute;
        display: block;
        box-sizing: content-box;
        width: 0px;
        height: 0px;
        border: 10px solid transparent;
        border-top: 10px solid #00c1ff;
        border-bottom: 0;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%) translateY(100%);
      }
      .ol-popup__title {
        font-size: 18px;
        color: #fff;
      }
      .ol-popup__content {
        margin-top: 8px;
        color: #fff;
        .ol-popup__content--props {
          display: flex;
          .props-item {
            padding: 0 12px;
            position: relative;
            display: flex;
            flex-direction: column;
            align-items: center;
            min-width: 100px;
            &:after {
              content: "";
              position: absolute;
              height: 80%;
              right: 0;
              top: 10%;
              border-right: 1px solid #00c1ff4d;
            }
            &:first-child {
              padding-left: 0;
            }
            &:last-child {
              padding-right: 0;
              &:after {
                border-right: 0;
              }
            }
            .item-label {
              font-size: 16px;
            }
            .item-data {
              font-size: 18px;
              color: #33ffff;
              line-height: 25px;
            }
          }
          .props-label {
            line-height: 20px;
            width: 350px;
          }
        }
      }
    }
  }
  .ol-weather-popup {
    user-select: none;
    cursor: pointer;
    width: 200px;
    height: 70px;
    background-image: linear-gradient(180deg, #0096c3 2%, #103567 100%);
    border: 2px solid #00c1ff;
    box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
    border-radius: 3px;
    position: relative;
    opacity: 0.9;
    &:before {
      content: "";
      position: absolute;
      display: block;
      box-sizing: content-box;

      width: 0px;
      height: 0px;
      border: 7px solid transparent;
      border-top: 7px solid #103567;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%) translateY(100%);
      z-index: 100;
    }
    &:after {
      content: "";
      position: absolute;
      display: block;
      box-sizing: content-box;

      width: 0px;
      height: 0px;
      border: 10px solid transparent;
      border-top: 10px solid #00c1ff;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%) translateY(100%);
    }
    .ol-weather-popup__content {
      height: 100%;
      width: 100%;
      color: #fff;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .weather-icon {
        margin-left: 16px;
        width: 48px;
        i {
          width: 32px;
          height: 32px;
        }
      }
      .weather-label {
        flex: 2;
        text-align: left;

        display: flex;
        flex-direction: column;
        line-height: 24px;
        font-size: 16px;
        margin-right: 8px;
        .label-name {
          display: flex;
          justify-content: space-between;
        }
        .weather {
          font-size: 18px;
        }
      }
    }
  }
}
</style>
