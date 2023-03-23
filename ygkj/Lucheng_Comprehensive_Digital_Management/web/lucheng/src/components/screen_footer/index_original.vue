<template>
  <div class="map-footer">
    <!-- <footer-info /> -->
    <footer-action-bar
      @basinCommand="basinCommand"
      @mapTypeChange="mapTypeChange"
      @showAreaLine="showAreaLine"
      @doSceneSave="doSceneSave"
    />
    <!-- <footer-date /> -->
    <!-- <smart-compare v-if="boxFlag" @closeSmart="closeSmart" /> -->
  </div>
</template>

<script>
import esri2geo from 'esri2geo';
import { Fill, Stroke, Style, Circle } from 'ol/style';
import { Vector as VectorSource } from 'ol/source';
import { Vector as VectorLayer } from 'ol/layer';
import GeoJSON from 'ol/format/GeoJSON';
import Measure from '@/utils/measure';
import mapConfig from '@/config/mapConfig';
import { queryMapserver } from '@/api/loadMapServe';
import { XYZ, TileArcGISRest } from 'ol/source';
import TileLayer from 'ol/layer/Tile';
import { drawPolygon } from '@/utils/mapUtil.js';
import ImageLayer from 'ol/layer/Image';
import ImageArcGISRest from 'ol/source/ImageArcGISRest';
import FooterActionBar from './components/FooterActionBar.vue';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'ScreenMapFooter',
  props: { mapBind: { type: Object } },
  data() {
    return {
      basinLayer: null,
      mapTypeLayer: null,
      zhejiangSLMCLayer: null,
      zhejiangSLMCHSLayer: null,
      wenzhouTCLayer: null,
      areaLayer: null,
      isScreen: false,
      boxFlag: false
    };
  },
  components: { FooterActionBar},
  mounted() {},
  methods: {
    ...mapActions(['changeTimelinebarShow']),
    /** 测距 */
    distanceMeasure() {
      Measure.measure(this.mapBind, 'LineString');
    },
    /** 测面 */
    areaMeasure() {
      this.$emit('measure', 'area');
    },
    /** 清除测量 */
    clearMeasure() {
      this.$emit('measure', 'none');
      Measure.meatureFeaturesClear();
    },
    /** 清除图层 */
    clearLayer() {
      const ignoreList = ['SLTCLayer', 'zhejiangmainSLLayer', 'zhejiangmainSLMCLayer'];
      this.mapBind.getLayers().forEach((element) => {
        if (!element.get('id')) element.setVisible(0);
      });
      this.ScreenMap.clearDesignFlood();
    },
    /** 框选 */
    boxChoose() {
      this.$emit('measure', 'Box');
      // Measure.measure(this.mapBind, "Box");
    },
    /** 圆选 */
    circleChoose() {
      //to do
      this.$emit('measure', 'Circle');
      // Measure.measure(this.mapBind, "Circle");
    },
    /** 多边形选择 */
    polygonChoose() {
      this.$emit('measure', 'area');
      //to do
    },
    /** CADH智能对比 */
    smartContrast() {
      this.boxFlag = true;
    },
    closeSmart() {
      this.boxFlag = false;
    },
    /** 打印 */
    print() {
      this.ScreenMap.printMap();
    },
    /** 全屏 */
    fullScreen() {
      let element = document.documentElement;
      if (this.isScreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen();
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen();
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen();
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen();
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen();
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen();
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen();
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen();
        }
      }
      this.isScreen = !this.isScreen;
    },
    /** 全图 */
    fullMap() {
      let view = this.mapBind.getView();
      view.animate({
        center: [mapConfig.lng, mapConfig.lat],
        duration: 300,
        zoom: mapConfig.zoom
      });
    },
    /** 流域切换 */
    basinCommand(command) {
      if (command == '全流域') {
        if (this.basinLayer != null) {
          this.mapBind.removeLayer(this.basinLayer);
          this.basinLayer = null;
          this.fullMap();
        }
        return;
      }
      queryMapserver(
        '/温州流域/MapServer/0/query',
        {
          where: "WRNM='" + command + "'",
          f: 'json'
        },
        true
      ).then((res) => {
        if (res.features && res.features.length > 0) {
          esri2geo(res, (err, data) => {
            //加载温州流域图层
            let vectorSource = new VectorSource({
              features: new GeoJSON().readFeatures(data)
            });
            if (this.basinLayer != null) {
              this.mapBind.removeLayer(this.basinLayer);
              this.basinLayer = null;
            }
            this.basinLayer = new VectorLayer({
              source: vectorSource,
              style: new Style({
                stroke: new Stroke({
                  color: '#FF00E7',
                  lineDash: [4],
                  width: 2
                }),
                fill: new Fill({
                  color: 'rgba(0, 0, 255, 0.1)'
                })
              }),
              zIndex: 1000
            });
            this.mapBind.addLayer(this.basinLayer);
            this.mapBind.getView().fit(this.basinLayer.getSource().getFeatures()[0].getGeometry(), {
              padding: [100, 300, 0, 200]
            });
          });
        }
      });
    },
    /** 地图类型切换 */
    mapTypeChange(value) {
      // console.log(this.mapBind.getLayers().getArray());
      if (this.mapTypeLayer != null) {
        this.mapBind.removeLayer(this.mapTypeLayer);
        this.mapTypeLayer = null;
      }
      if (this.zhejiangSLMCLayer != null) {
        this.mapBind.removeLayer(this.zhejiangSLMCLayer);
        this.zhejiangSLMCLayer = null;
      }
      if (this.zhejiangSLMCHSLayer != null) {
        this.mapBind.removeLayer(this.zhejiangSLMCHSLayer);
        this.zhejiangSLMCHSLayer = null;
      }
      if (this.wenzhouTCLayer != null) {
        this.mapBind.removeLayer(this.wenzhouTCLayer);
        this.wenzhouTCLayer = null;
      }
      let boderList = [
        'tiandituVec',
        // "tiandituImg",
        'zhejiangSLLayer',
        'zhejiangHTLayer'
        // "wenzhouVecLinght",
        // "wenzhouVecDark",
        // "wenzhouVecWhite"
        // "wnzhouImgLayer",
      ];
      let boderListnew = [
        // "tiandituVec",
        'tiandituImg',
        // "zhejiangSLLayer",
        'wnzhouImgLayer'
      ];
      // //温州水系图
      // let wenzhouRivers = new TileLayer({
      //     id: "wenzhouRivers",
      //  zIndex: 0,
      //     source: new XYZ({
      //       url:
      //         "http://117.149.227.112:6080/arcgis/rest/services/mapservice/wenzhou_fangxun/MapServer/tile/{z}/{y}/{x}",
      //       projection: "EPSG:4326",
      //     })
      //   });
      //天地图路网图
      let tiandituVec = new TileLayer({
        id: 'tiandituVec',
        zIndex: 0,
        source: new XYZ({
          url: 'http://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=2eeb2384fab6fcc8f96762c31bd0563b',
          projection: 'EPSG:4326'
        })
      });
      //天地图影像图
      let tiandituImg = new TileLayer({
        id: 'tiandituImg',
        zIndex: 0,
        source: new XYZ({
          url: 'http://t0.tianditu.gov.cn/DataServer?T=img_c&X={x}&Y={y}&L={z}&tk=2eeb2384fab6fcc8f96762c31bd0563b',
          projection: 'EPSG:4326'
        })
      }); 
      //浙江水利底图
      let zhejiangSLLayer = new TileLayer(
        {
          id: 'zhejiangSLLayer',
          zIndex: 0.5,
          source: new XYZ({
            url: 'https://sldtpt.zjwater.com:6443/lcqslqyszhghpt/PBS/rest/services/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}',
            projection: 'EPSG:4326'
          })
        },
        { zoomOffset: 1 }
      );
      //浙江海塘底图
      let zhejiangHTLayer = new ImageLayer(
        {
          id: 'zhejiangHTLayer',
          zIndex: 0.5,
          source: new ImageArcGISRest({
            ratio: 1,
            params: {},
            url: 'https://sldtpt.zjwater.com:6443/lcqslqyszhghpt/arcgis/rest/services/zjsl/haitang/MapServer',
            projection: 'EPSG:4326'
          })
        },
        { zoomOffset: 1 }
      );

      // let zhejiangHTLayer = new TileLayer(
      //   {
      //     id: 'zhejiangHTLayer',
      //     zIndex: 1,
      //     source: new XYZ({
      //       url: 'https://sldtpt.zjwater.com:6443/lcqslqyszhghpt/arcgis/rest/services/zjsl/haitang/MapServer/tile/{z}/{y}/{x}',
      //       projection: 'EPSG:4326'
      //     })
      //   },
      //   { zoomOffset: 1 }
      // );
      // 温州市影像图
      let wnzhouImgLayer = new TileLayer(
        {
          id: 'wnzhouImgLayer',
          zIndex: 0,
          source: new TileArcGISRest({
            url: 'http://10.36.161.208:16080/wzgt22/563382047626443aba7e546b86c0cf4f/arcgis/rest/services/HISIMG/WZ2000_WORLDVIEW_2018/MapServer'
            // projection: "EPSG:4326",
          })
        },
        { zoomOffset: 1 }
      );
      //温州水行政区划矢量-亮色
      let wenzhouVecLinght = new TileLayer({
        id: 'wenzhouVecLinght',
        zIndex: 0,
        source: new XYZ({
          url: mapConfig.mapServer + 'wenzhou/wenzhou_bsend01/MapServer/tile/{z}/{y}/{x}',
          projection: 'EPSG:4326'
        })
      });
      //温州水行政区划矢量-黑色
      let wenzhouVecDark = new TileLayer({
        id: 'wenzhouVecDark',
        zIndex: 0,
        source: new XYZ({
          url: mapConfig.mapServer + 'wenzhou/wenzhou_fangxunend/MapServer/tile/{z}/{y}/{x}',
          projection: 'EPSG:4326'
        })
      });
      //温州水行政区划矢量-白色
      let wenzhouVecWhite = new TileLayer({
        id: 'wenzhouVecDark',
        zIndex: 0,
        source: new XYZ({
          url: mapConfig.mapServer + '/wenzhou/wenzhou_bsWhite/MapServer/tile/{z}/{y}/{x}',
          projection: 'EPSG:4326'
        })
      });
      //浙江水利底图-蒙层
      this.zhejiangSLMCLayer = new TileLayer({
        id: 'zhejiangSLMCLayer',
        zIndex: 2,
        source: new XYZ({
          url: 'https://webgis.ygwjg.com/arcgis/rest/services/wenzhou/wenzhou_luchengbs/MapServer/tile/{z}/{y}/{x}',
          projection: 'EPSG:4326'
        })
      });
      //浙江水利底图-蒙层黑色
      // this.zhejiangSLMCHSLayer = new TileLayer({
      //   id: 'zhejiangSLMCHSLayer',
      //   zIndex: 2,
      //   source: new XYZ({
      //     url: mapConfig.mapServer + 'wenzhou/wzmchdnew/MapServer/tile/{z}/{y}/{x}',
      //     projection: 'EPSG:4326'
      //   })
      // });
      //地图填充图层
      // this.wenzhouTCLayer = new TileLayer({
      //   id: 'wenzhouTCLayer',
      //   zIndex: 0,
      //   source: new XYZ({
      //     url: mapConfig.mapServer + 'wenzhou/wenzhouTC/MapServer/tile/{z}/{y}/{x}',
      //     projection: 'EPSG:4326'
      //   })
      // });
      let mapTypeMap = {
        tiandituVec,
        tiandituImg,
        zhejiangSLLayer,
        zhejiangHTLayer
      };
      // this.mapBind.removeLayer(this.zhejiangSLMCHSLayer);
      this.changeTimelinebarShow(false);
      this.removeYxLayers();
      this.mapTypeLayer = mapTypeMap[value];
      this.mapBind.addLayer(this.mapTypeLayer);
      // if (boderList.includes(value)) {
      //   this.mapBind.addLayer(this.zhejiangSLMCLayer);
      // }
      const list = ['zhejiangmainSLMCLayer', 'zhejiangmainSLLayer', 'SLTCLayer'];
      this.mapBind.getLayers().forEach((element) => {
        if (element) {
          if (list.includes(element.get('id'))) {
            this.mapBind.removeLayer(element);
          }
        }
      });
    },
    clearAreaLayer() {
      if (this.areaLayer != null) {
        this.mapBind.removeLayer(this.areaLayer);
      }
    },
    flyPoint(center, zoom) {
      const view = this.mapBind.getView();
      view.animate({
        center: center,
        duration: 300,
        zoom: zoom
      });
    },
    //根据行政区划名称查询行政区划边界
    showAreaLine(areaName) {
      this.clearAreaLayer();
      const searchAreaList = [
        '鹿城区',
        '龙湾区',
        '瓯海区',
        '洞头区',
        '永嘉县',
        '平阳县',
        '苍南县',
        '文成县',
        '泰顺县',
        '瑞安市',
        '乐清市'
      ];
      //对应searchAreaList 每个行政区划的定位中心点
      const centerPoint = [
        [120.55389798474596, 28.068232997940203],
        [120.81894315076158, 27.89931820301833],
        [120.54703152966783, 27.963862880752703],
        [121.03317654919908, 27.919917568252703],
        [120.66376126599596, 28.337398037002703],
        [120.36143482403382, 27.64214415802697],
        [120.44959860797451, 27.440954047283753],
        [120.00699559080968, 27.82237370619345],
        [119.85989517628137, 27.524267556397135],
        [120.43658087217555, 27.839296762732104],
        [121.01717188880941, 28.276692685577352]
      ];
      const areaIndex = searchAreaList.indexOf(areaName);
      if (areaIndex != -1) {
        const searchUrl = mapConfig.mapServer + 'wenzhou/wenzhou_fangxunend/MapServer/3/query';
        const opt = {
          where: "NAME='" + areaName + "'",
          inSR: 4490,
          outFields: '*',
          outSR: 4490,
          f: 'pjson'
        };
        queryMapserver(searchUrl, opt).then((res) => {
          let response = res.features;
          const coordinates = response[0].geometry.rings;
          this.areaLayer = drawPolygon(coordinates, {
            fillColor: 'rgba(0,0,0,0)',
            lineColor: '#FF551F',
            lineDash: [6],
            lineWidth: 4
          });
          this.mapBind.addLayer(this.areaLayer);
        });
        this.flyPoint(centerPoint[areaIndex], 10);
      }
    },
    removeYxLayers() {
      const yxArr = ['wz2010', 'wz2012', 'wz2014', 'wz2016', 'wz2017', 'wz2018', 'wz2019'];
      this.mapBind.getLayers().forEach((element) => {
        let layerId = element.get('id');

        if (yxArr.includes(layerId)) {
          this.mapBind.removeLayer(element);
        }
      });
    },
    doSceneSave() {
      this.$emit('doSceneSave');
    }
  },
  computed: {
    ...mapGetters(['mapChangeModule', 'ScreenMap',])
  }
};
</script>

<style lang="scss" scoped>
.map-footer {
  position: absolute;
  width: 100%;
  height: 95px;
  bottom: 0;
  display: flex;
  background-image: url(~assets/images/ScreenAsset/map-footer-bg.png);
  background-size: 100% 100%;
  background-repeat: no-repeat;
  justify-content: center;
  z-index: 2;
  ::v-deep .compare-detail-box {
    position: absolute;
    bottom: 82px;
    left: 50%;
    transform: translateX(-50%);
    border-radius: 4px;
    width: 300px;
    height: 338px;
    border: 1px solid #00c1ff;
    background-color: rgba(0, 0, 0, 0.8);
    background-image: linear-gradient(180deg, rgba(53, 149, 227, 0.4) 0%, rgba(25, 59, 197, 0.4) 100%);
    padding: 10px 16px 0 16px;
  }
}
</style>
