<!--
 * @Author: wqn
 * @Date: 2023-02-14 09:30:16
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-20 14:17:58
 * @Description: 大屏-水库风险管控清单
-->
<template>
  <div class="reservoir">
    <MainHeader>水库风险管控清单</MainHeader>
    <div class="screen-container">
      <div class="map-container">
        <OlMap @initMapFinished="initMapFinished" />
      </div>
      <div class="left side-container">
        <div>
          <div class="left-box-1">
            <ReservoirScale :map="map" />
          </div>
          <div class="left-box-2">
            <ReservoirNumber :map="map" />
          </div>
          <div class="left-box-3">
            <ReservoirStorage :map="map" />
          </div>
        </div>
      </div>
      <div class="right side-container">
        <div class="right-box-1">
          <ReservoirRisk :map="map" />
        </div>
        <div class="right-box-2">
          <ReservoirSize :map="map" />
        </div>
        <div class="right-box-3">
          <ReservoirDanger :map="map" />
        </div>
        <div class="right-box-4">
          <ReservoirDetailedList />
        </div>
      </div>
      <div class="admn-basin-select-box">
        <admn-basin-select projectCategory="水库" v-model="admnInfo" />
      </div>
    </div>
    <PointDialog
      :map="map"
      @lookCamera="lookCamera"
      @lookVideo="lookVideo"
      @lookDetail="lookDetail"
      @lookPhotos="lookPhotos"
    />
    <ProjectDetail v-model="showDetailDialog" :projectDetail="projectDetail" />
    <PreviewPhoto
      v-if="showLookPhotosPopup"
      :loading="lookPhotosLoading"
      :photoList="photosList"
      :reservoirName="reservoirName"
      @closePhotos="closePhotos"
    />
    <PreviewVideo
      v-if="showLookVideoPopup"
      :loading="lookVideoLoading"
      :videoUrl="videoUrl"
      @closeLookVideoPopup="closeLookVideoPopup"
    />
    <PreviewCamera
      v-if="showLookCameraPopup"
      :prcd="prcd"
      :name="skName"
      @closeLookCameraPopup="closeLookCameraPopup"
    />
  </div>
</template>

<script>
import ReservoirScale from "./cpns/ReservoirScale.vue";
import ReservoirNumber from "./cpns/ReservoirNumber.vue";
import ReservoirStorage from "./cpns/ReservoirStorage.vue";
import ReservoirRisk from "./cpns/ReservoirRisk.vue";
import ReservoirSize from "./cpns/ReservoirSize.vue";
import ReservoirDanger from "./cpns/ReservoirDanger.vue";
import ReservoirDetailedList from "./cpns/ReservoirDetailedList.vue";
import PointDialog from "./cpns/PointDialog.vue";
import PreviewPhoto from "components/common/PreviewPhoto";
import PreviewVideo from "components/common/PreviewVideo";
import PreviewCamera from "components/common/PreviewCamera";

export default {
  name: "Reservoir",
  props: {},
  components: {
    ReservoirScale,
    ReservoirNumber,
    ReservoirStorage,
    ReservoirRisk,
    ReservoirSize,
    ReservoirDanger,
    ReservoirDetailedList,
    PointDialog,
    PreviewPhoto,
    PreviewVideo,
    PreviewCamera,
  },
  data() {
    return {
      map: null,
      admnInfo: {},
      showDetailDialog: false,
      projectDetail: {},
      showLookPhotosPopup: false,
      lookPhotosLoading: true,
      lookVideoLoading: false,
      showLookVideoPopup: false,
      photosList: [],
      reservoirName: "",
      prcd: "",
      skName: "",
      showLookCameraPopup: false,
    };
  },
  watch: {
    admnInfo(value) {
      if (this.map) {
        this.relationPolygon = value.geometry;
        this.initMapViewPosition();
      }
    },
  },
  destroyed() {
    this.$store.commit("setReservoirScale", "");
  },
  methods: {
    // 弹窗内打开查看工程详情
    lookDetail(row) {
      let prcd = row.PRCD;
      let name = row.NAME;
      let token = this.$localData("get", "token");
      name = encodeURI(encodeURI(name));
      const projectTitle = `${row.cityADNM} - ${row.countryADNM} - ${row.NAME}`;
      const detailUrl = `https://sk.zjwater.com/cp/skip/reservoirDetailSingle?prcd=${prcd}&name=${name}&fromMap=&pName=&cName=&token=${token}`;
      this.projectDetail = {
        projectTitle,
        detailUrl,
      };
      this.showDetailDialog = true;
    },
    //弹窗内打开查看图片
    lookPhotos(arr, reservoirName) {
      this.photosList = arr;
      this.reservoirName = reservoirName;
      this.showLookPhotosPopup = true;
      this.lookPhotosLoading = false;
    },
    //弹窗内打开查看视频
    lookVideo() {
      // this.showLookVideoPopup = true;
      // this.lookVideoLoading = false;
    },
    //弹窗内打开查看监控
    lookCamera(prcd, skName) {
      this.prcd = prcd;
      this.skName = skName;
      this.showLookCameraPopup = true;
    },
    //关闭查看图片
    closePhotos() {
      this.showLookPhotosPopup = false;
      this.lookPhotosLoading = true;
    },
    //关闭查看监控
    closeLookCameraPopup(){
      this.showLookCameraPopup = false
    },
    initMapFinished(map, polygon) {
      this.map = map;
      this.relationPolygon = polygon;
      this.initMapViewPosition();
    },
    initMapViewPosition() {
      this.$olMapUtil.changeViewByPolygon(
        this.map,
        this.relationPolygon,
        [0, 500, 0, 500]
      );
    },
  },
};
</script>
<style lang='scss' scoped>
.reservoir {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  .screen-container {
    position: relative;
    flex: 1;
    width: 100%;
    height: 100%;
    .map-container {
      position: absolute;
      inset: 0;
      width: 100%;
      height: 100%;
    }
    .side-container {
      position: absolute;
      width: 460px;
      top: 0px;
      bottom: 10px;
      height: 100%;
      z-index: 1;
      padding-top: 10px;
      &.left {
        left: 30px;
        &::after {
          content: "";
          background: url("~@/assets/images/left-bg.png") no-repeat;
          background-size: 100% 100%;
          position: absolute;
          left: -30px;
          top: 0;
          width: 100%;
          height: 100%;
        }
        > div {
          width: 100%;
          height: 100%;
          position: relative;
          z-index: 2;
          .left-box-1 {
            height: 275px;
          }
          .left-box-2 {
            height: 30%;
          }
          .left-box-3 {
            height: calc(100% - 30% - 280px);
          }
        }
      }
      &.right {
        padding-right: 30px;
        box-sizing: content-box;
        right: 0;
        background: url("~@/assets/images/right-bg.png") no-repeat;
        .right-box-1 {
          height: 220px;
        }
        .right-box-2 {
          height: 129px;
        }
        .right-box-3 {
          height: 129px;
        }
        .right-box-4 {
          height: calc(100% - 498px);
        }
      }
    }
    .admn-basin-select-box {
      position: absolute;
      left: 500px;
      top: 50px;  
    }
  }
}
</style>