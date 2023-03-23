<template>
  <div class="map-wrap">
    <div class="map" id="mainView"></div>
    <coverage-dialog v-if="dispatchVisible" :dialogVisible="dispatchVisible" @handleClose="dispatchClose"></coverage-dialog>
    <div
      class="ol-popup-water"
      v-for="(item, index) in popupArr"
      :key="index"
      :id="`popup${item.projectCode}`"
      @click="modelHandle(item)"
    >
      <div class="content-wrap">
        <p class="title">{{ item.projectName }}</p>
        <div class="flex-box">
          <div>
            <p>
              <img src="../../../../assets/images/popup-icon.png" alt />
              当前水位
            </p>
            <p>{{ item.z == null ? "-" : item.z }}m</p>
          </div>
          <div>
            <p>
              <img src="../../../../assets/images/popup-icon.png" alt />
              汛限水位
            </p>
            <p>{{ item.limitLevel == null ? "-" : item.limitLevel }}m</p>
          </div>
          <div>
            <p>
              <img src="../../../../assets/images/popup-icon.png" alt />
              时间
            </p>
            <p>{{ item.forecastTime == null ? "-" : item.forecastTime }}</p>
          </div>
        </div>
      </div>
    </div>
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
import coverageDialog from "./coverageDialog";

import BarChart from "./charts/BarChart";
import lineChart from "./charts/LineChart";
export default {
  name: "ScreenMap",
  components: {
      BarChart, 
      lineChart,
      coverageDialog
  },
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
      waterLayer: null, //水情分析图层
      map: null,
      popupData: {},
      popupArr: [],
      //图层
      overlay_water: {},
      tabArr: [
        {
          tit: "历史极值",
          id: "1",
        },
        {
          tit: "实时降雨",
          id: "2",
        },
        {
          tit: "2h降雨预报",
          id: "3",
        },
        {
          tit: "72h降雨预报",
          id: "4",
        },
      ],
      tabActive: "2",
      //图表
      resize: false, //解决第一次点卡宽度不能自适应问题
      chartData: { rows: [], columns: [] },
      setting: { labelMap: {} },
      dispatchVisible: false
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
    modelHandle(item) {
      this.$store.commit("SET_MODEL_INFO",item);
      this.$emit("modelHandle", item);
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
          zoom: 9.5,
        }),
      });
      // this.addOverlay(); // 添加overlay
      this.mapClick(); // 初始化地图成功后，给地图添加点击事件
    },
    //地图点击事件
    mapClick() {
      this.map.on("singleclick", (evt) => {
        this.dispatchVisible = true;
      })
    },
    //关闭图层
    dispatchClose() {
      this.dispatchVisible = false;
    },
    // 添加overlay
    addOverlay(data) {
      this.popupArr = data;
      this.$nextTick(() => {
        for (let i in this.popupArr) {
          if (
            this.popupArr[i].projectName == "金溪水库" ||
            this.popupArr[i].projectName == "桥墩水库" ||
            this.popupArr[i].projectName == "仙居水库" ||
            this.popupArr[i].projectName == "三插溪水库"
          ) {
            let pop_container = document.getElementById(
              `popup${this.popupArr[i].projectCode}`
            );
            this.overlay_water[this.popupArr[i].projectCode] = new Overlay({
              element: pop_container,
              autoPan: true,
              autoPanAnimation: {
                duration: 250,
              },
              positioning: "bottom-center",
              offset: [5, 0],
            });
            this.map.addOverlay(
              this.overlay_water[this.popupArr[i].projectCode]
            );
            this.overlay_water[this.popupArr[i].projectCode].setPosition([
              this.popupArr[i].lng,
              this.popupArr[i].lat,
            ]);
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
        zoom: 14,
      });
    },
    //降雨分析-popup-菜单
    handleClick(id) {
      this.tabActive = id;
    },
    async waterPoint() {
      this.cleanAllLayer();
      // this.closeOverlay();
      let res = await dispatchApi.mapPoint({});
      let index = res.data.findIndex((item) => item.projectName == "桥墩水库");
      res.data[index].warnStatus = 1;
      this.waterLayer = drawLhddPoint(res.data, "water");
      this.addOverlay(res.data);
      this.map.addLayer(this.waterLayer);
    },
    //清理所有图层
    cleanAllLayer() {
      this.map.removeLayer(this.waterLayer);
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
    // 水情分析
    .ol-popup-water {
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
        padding: 20px 10px;
        .title {
          font-size: 22px;
          color: #50bcea;
          padding-left: 10px;
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
    //工程分析
    .ol-popup-project {
      width: 447px;
      height: 284px;
      background: url("../../../../assets/images/popup-project-bg.png")
        no-repeat center;
      background-size: 100% 100%;
      .title {
        height: 46px;
        line-height: 46px;
        padding-left: 30px;
        font-size: 18px;
        color: #50bcea;
        i {
          position: absolute;
          top: 20px;
          right: 30px;
          cursor: pointer;
        }
      }
      .content {
        width: 436px;
        height: 230px;
      }
    }
    //降雨分析
    .ol-popup-rain {
      width: 447px;
      height: 344px;
      background: url("../../../../assets/images/popup-rain-bg.png") no-repeat
        center;
      background-size: 100% 100%;
      .title {
        height: 46px;
        line-height: 46px;
        padding-left: 30px;
        font-size: 18px;
        color: #50bcea;
        i {
          position: absolute;
          top: 21px;
          right: 30px;
          cursor: pointer;
        }
      }
      .content {
        position: relative;
        width: 436px;
        height: 230px;
      }
    }

    //tab
    .sidebar-tab {
      width: 438px;
      margin-top: 20px;
      padding: 0 40px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      > span {
        display: inline-block;
        height: 32px;
        padding: 0 5px;
        line-height: 32px;
        text-align: center;
        color: #2c5a90;
        font-size: 14px;
        cursor: pointer;
        background: url(../../../../assets/images/bigScreen-mini-tab.png)
          no-repeat;
        background-size: 100% 100%;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        word-break: break-all;
        &:last-child {
          margin-right: 0;
        }
        &.active {
          background: url(../../../../assets/images/bigScreen-mini-tab-check.png)
            no-repeat;
          color: #a6dff6;
          background-size: 100% 100%;
        }
      }
    }
  }
}
</style>
