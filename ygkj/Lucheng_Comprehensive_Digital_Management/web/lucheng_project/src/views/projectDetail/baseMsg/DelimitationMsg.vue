<!--
 * @Author: hanyu
 * @LastEditTime: 2022-11-21 11:22:07
 * @Description: 划界信息
 * @FilePath: \lucheng_project\src\views\projectDetail\baseMsg\DelimitationMsg.vue
-->
<template>
  <div class="map_wrap">
    <div class="map" id="mainView"></div>
    <div class="map_line">
      <div>
        <el-button style="width: 100%" plain @click="pointMap(1)">保护范围线</el-button>
      </div>
      <div>
        <el-button style="width: 100%" plain @click="pointMap(2)">管保区域</el-button>
      </div>
      <div>
        <el-button style="width: 100%" plain @click="pointMap(3)">社会资源区域</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import "ol/ol.css";
import { Map, View, Feature } from "ol";
import XYZ from "ol/source/XYZ";
import Tile from "ol/layer/Tile";
import { defaults as defaultControls } from "ol/control";
import Point from "ol/geom/Point";
import Overlay from "ol/Overlay";
import { toStringHDMS } from "ol/coordinate";
import { toLonLat } from "ol/proj";
import {
  Style,
  Fill,
  Stroke,
  Icon,
  Text,
  Circle as CircleStyle
} from "ol/style";
import { Tile as TileLayer, Vector as VectorLayer } from "ol/layer";
import { Vector as VectorSource, Cluster as ClusterSource } from "ol/source";
import { Draw, DoubleClickZoom } from "ol/interaction";
import { drawPoint, drawLines, drawPolygon, drawLine } from "@/utils/mapUtil";
import Measure from "@/utils/measure";
import { perviewApi } from "@/api/projectDetail.js";
export default {
  name: "DelimitationMsg",
  props: {},
  components: {},
  data() {
    return {
      map: null,
      pointLayer: null,
      linesLayer: null,
      pointPolygonLayer: null,
      polygonLayer: null,
      polygonLayer_: null,
      defaultCenter: {
        // lnglat: [119.555, 30.676],
        lnglat: [119.755, 30.676],
        zoom: 14
      },
      pointData_a: {},
      pointData_b: {},
      pointData_c: {}
    };
  },
  computed: {
    seawallDetail() {
      return this.$store.state.seawallDetail;
    }
  },
  async mounted() {
    console.log(this.seawallDetail, "====");
    await this.mapInit();
    await this.getRangebyCode(1);
    await this.getRangebyCode(2);
    await this.getRangebyCode(3);
  },
  methods: {
    pointMap(type) {
      if (type == 1) {
        if (this.pointPolygonLayer) {
          this.$confirm("是否重新编辑?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消"
          })
            .then(() => {
              this.map.removeLayer(this.pointPolygonLayer);
              Measure.measure(this.map, "area", 1, this.setPoint);
            })
            .catch(() => {
              this.$message({
                type: "info",
                message: "已取消"
              });
            });
        } else {
          Measure.measure(this.map, "area", 1, this.setPoint);
        }
      } else if (type == 2) {
        if (this.polygonLayer) {
          this.$confirm("是否重新编辑?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消"
          })
            .then(() => {
              this.map.removeLayer(this.polygonLayer);
              Measure.measure(this.map, "area", 2, this.setPoint);
            })
            .catch(() => {
              this.$message({
                type: "info",
                message: "已取消"
              });
            });
        } else {
          Measure.measure(this.map, "area", 2, this.setPoint);
        }
      } else if (type == 3) {
        if (this.polygonLayer_) {
          this.$confirm("是否重新编辑?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消"
          })
            .then(() => {
              this.map.removeLayer(this.polygonLayer_);
              Measure.measure(this.map, "area", 3, this.setPoint);
            })
            .catch(() => {
              this.$message({
                type: "info",
                message: "已取消"
              });
            });
        } else {
          Measure.measure(this.map, "area", 3, this.setPoint);
        }
      }
    },
    setPoint(point, type) {
      if (point.length > 0) {
        let num = point.length / 2;
        let opintList = [];
        for (let i = 0; i < num; i++) {
          let start = i * 2;
          let end = (i + 1) * 2;
          let arr = point.slice(start, end);
          opintList.push(arr);
        }
        if (type == 1) {
          this.savePoint(opintList, type);
        } else if (type == 2) {
          let arr = [opintList[0], opintList[opintList.length - 2]];
          this.savePoint(arr, type);
        } else if (type == 3) {
          this.savePoint(opintList, type);
        }
      }
    },
    savePoint(arr, type) {
      this.$confirm("是否保存?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      })
        .then(() => {
          let obj = {
            prcd: this.seawallDetail.seawallCode,
            point: JSON.stringify(arr),
            type: type
          };
          if (type == 1) {
            obj.id = this.pointData_a.id || "";
          } else if (type == 2) {
            obj.id = this.pointData_b.id || "";
          } else if (type === 3) {
            obj.id = this.pointData_c.id || "";
          }
          perviewApi.saveOrUpdate(obj).then(res => {
            if (res.code == 0) {
              this.$message.success("保存成功");
            } else {
              this.$message.error(res.msg);
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消"
          });
          Measure.meatureFeaturesClear();
        });
    },
    async getRangebyCode(type) {
      let res = await perviewApi.getRangebyCode({
        prcd: this.seawallDetail.seawallCode,
        type: type
      });
      if (res.code == 0) {
        if (res.data.length > 0) {
          let point = JSON.parse(res.data[0].point);
          if (type == 1 && res.data.length > 0) {
            this.pointData_a = res.data[0];
            this.pointPolygonLayer = drawPolygon([point]);
            this.map.addLayer(this.pointPolygonLayer);
          } else if (type == 2 && res.data.length > 0) {
            this.pointData_b = res.data[0];
            this.polygonLayer = drawLine(point);
            this.map.addLayer(this.polygonLayer);
          } else if (type == 3 && res.data.length > 0) {
            this.pointData_c = res.data[0];
            this.polygonLayer_ = drawLine(point);
            this.map.addLayer(this.polygonLayer_);
          }
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
              url:
                "https://t0.tianditu.gov.cn/DataServer?T=img_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              projection: "EPSG:4326",
              crossOrigin: "anonymous"
            }),
            preload: 1,
            visible: true
          })

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
          minZoom: 0
        })
      });

      // 禁用地图双击缩放
      const dblClickInteraction = this.map
        .getInteractions()
        .getArray()
        .find(interaction => {
          return interaction instanceof DoubleClickZoom;
        });
      this.map.removeInteraction(dblClickInteraction);

      // 监听事件
      this.mapClick(); // 初始化地图成功后，给地图添加点击事件

      // 鼠标移入
      this.map.on("pointermove", e => {
        var pixel = this.map.getEventPixel(e.originalEvent);
        var hit = this.map.hasFeatureAtPixel(pixel);
        if (hit) {
          this.map.getTargetElement().style.cursor = "pointer";
        } else {
          this.map.getTargetElement().style.cursor = "";
        }
      });

      this._setView(this.seawallDetail.longitude, this.seawallDetail.latitude);
      this._drawLines([
        {
          line: this.seawallDetail.coorpot
            ? JSON.parse(
                this.seawallDetail.coorpot.replace(
                  new RegExp("&quot;", "gm"),
                  '"'
                )
              )[0]
            : "",
          level: this.seawallDetail.seawallLevel
        }
      ]);
    },
    // 地图点击事件
    mapClick() {
      this.map.on("singleclick", evt => {
        var pixel = this.map.getEventPixel(evt.originalEvent);
        var feature = this.map.forEachFeatureAtPixel(pixel, function(feature) {
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
              duration: 250
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
        src: require("@/assets/images/reservoir.png"), //图片路径
        title: "",
        scale: 1
      };
      this.pointLayer = drawPoint(coordinates, styleObj);
      this.map.addLayer(this.pointLayer);
    },

    // 画线
    _drawLines(list) {
      this.map.removeLayer(this.linesLayer);
      for (let i = 0; i < list.length; i++) {
        list[i].coordinates = [];
        if (list[i].line && list[i].line.length) {
          for (let j = 0; j < list[i].line.length; j++) {
            list[i].coordinates.push([
              list[i].line[j].lng,
              list[i].line[j].lat
            ]);
          }
          switch (list[i].level) {
            case "1":
              list[i].lineColor = "#E02020";
              break;
            case "2":
              list[i].lineColor = "#EB7E65";
              break;
            case "3":
              list[i].lineColor = "#F7C739";
              break;
            case "4":
              list[i].lineColor = "#193CC5";
              break;
            case "5":
              list[i].lineColor = "#1BC57E";
              break;
            default:
              list[i].lineColor = "#C2C2C2";
              break;
          }
        }
      }
      console.log(list, "45465");
      this.linesLayer = drawLines(list);
      this.map.addLayer(this.linesLayer);
    },

    _setView(lng, lat) {
      const to = [lng, lat];
      const view = this.map.getView();
      let obj = {
        center: to,
        duration: 0
      };
      view.animate(obj);
    }
  }
};
</script>

<style lang='scss' scoped>
.map_wrap {
  background: #fdfdf1;
  position: relative;
  width: 100%;
  height: 100%;
  .map {
    width: 100%;
    height: 100%;
  }
  .map_line {
    position: absolute;
    z-index: 999;
    top: 20px;
    left: 20px;
    > div {
      margin-bottom: 15px;
    }
  }
}
</style>