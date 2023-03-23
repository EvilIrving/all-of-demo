<!--
 * @Author: wqn
 * @Date: 2023-02-14 09:30:16
 * @LastEditors: dtb
 * @LastEditTime: 2023-02-17 15:45:00
 * @Description: 大屏-海塘风险管控清单
-->
<template>
  <div class='seawall'>
    <img class="seawall_right_back" src="@/assets/images/right-bg.png" alt="">
    <img class="seawall_left_back" src="@/assets/images/left-bg.png" alt="">
    <div class="map-container">
      <OlMap ref="seawallMap" @initMapFinished="initMapFinished" />
    </div>
    <MainHeader style="z-index:5">海塘风险管控清单</MainHeader>
    <div class="left side-container">
      <div class="left-box-1">
        <SeawallNumber :map="map" />
      </div>
      <div class="left-box-2">
        <SeawallLevel :map="map" />
      </div>
      <div class="left-box-3">
        <SeawallLength />
      </div>
      <div class="left-box-4">
        <SeawallSafety :map="map" />
      </div>
    </div>
    <div class="right side-container">
      <div class="right-box-1">
        <SeawallMoistureproof :map="map" />
      </div>
      <div class="right-box-2">
        <SeawallControl />
      </div>
      <div class="right-box-3">
        <SeawallDetailedList />
      </div>
    </div>
    <div class="admn-basin-select-box">
      <admn-basin-select v-model="admnInfo" />
    </div>
    <preview-photo v-if="showLookPhotosPopup" :loading="lookPhotosLoading" :photoList="photosList" :seawallName="seawallName" @closePhotos="closePhotos"></preview-photo>
    <preview-video v-if="showLookVideoPopup" :loading="lookVideoLoading" :videoUrl="videoUrl" @closeLookVideoPopup="closeLookVideoPopup"></preview-video>

    <div class="reservoir_dialog" v-show="showDetailDialog">
      <div class="reservoir_title">
        <img @click="showDetailDialog = false" src="@/assets/images/back2.png" alt="" />
        <p>{{ projectTitle }}</p>
      </div>
      <iframe class="reservoir_dialog_iframe" :src="detailUrl" frameborder="0"></iframe>
    </div>
  </div>
</template>

<script>
import PreviewPhoto from "components/common/PreviewPhoto";
import PreviewVideo from "components/common/PreviewVideo";
import SeawallNumber from "./cpns/SeawallNumber.vue";
import SeawallLevel from "./cpns/SeawallLevel.vue";
import SeawallLength from "./cpns/SeawallLength.vue";
import SeawallSafety from "./cpns/SeawallSafety.vue";
import SeawallMoistureproof from "./cpns/SeawallMoistureproof.vue";
import SeawallControl from "./cpns/SeawallControl.vue";
import SeawallDetailedList from "./cpns/SeawallDetailedList.vue";
export default {
  name: "Seawall",
  props: {},
  provide() {
    return {
      lookPhotos: this.openPhotos,
      lookDetail: this.openDetailPopup,
      map: this.map,
    };
  },
  components: {
    SeawallNumber,
    SeawallLevel,
    SeawallLength,
    SeawallSafety,
    SeawallMoistureproof,
    SeawallControl,
    SeawallDetailedList,
    PreviewPhoto,
    PreviewVideo,
  },
  data() {
    return {
      map: null,
      admnInfo: {},
      showLookPhotosPopup: false,
      lookPhotosLoading: true,
      showLookVideoPopup: false,
      lookVideoLoading: true,
      photosList: [],
      seawallName: "",
      videoUrl: "",
      showDetailDialog: false,
      detailUrl: "",
      projectTitle: "海塘概况",

      relationPolygon: null,
    };
  },
  computed: {
    clickFeatVal() {
      return this.$store.state.map.clickFeatVal;
    },
  },
  watch: {
    admnInfo(value) {
      if (this.map) {
        this.relationPolygon = value.geometry;
        this.initMapViewPosition();
      }
    },
    clickFeatVal(value) {
      if (!value) {
        this.initMapViewPosition();
      }
    },
  },
  created() {},
  mounted() {},
  destroyed() {},
  methods: {
    initMapFinished(map, polygon) {
      this.map = map;
      this.relationPolygon = polygon;
      this.initMapViewPosition();
    },
    initMapViewPosition() {
      this.$olMapUtil.changeViewByPolygon(
        this.map,
        this.relationPolygon,
        [107, 500, 0, 500]
      );
    },
    openPhotos(payload) {
      this.photosList = payload;
      this.showLookPhotosPopup = false;
      this.$nextTick(() => {
        this.lookPhotosLoading = false;
        this.showLookPhotosPopup = true;
      });
    },
    closePhotos() {
      this.showLookPhotosPopup = false;
    },
    // 查看工程详情
    openDetailPopup(row) {
      let prcd = row.PRCD || row.prcd;
      let name = row.NAME || row.name || row.proName || row.prnm;
      let city = row.cityADNM || row.adcdctiy || row.cityadnm;
      let country = row.countryADNM || row.adcdcountry || row.countryadnm;
      let token = this.$localData("get", "token");
      let encodeName = encodeURI(encodeURI(name));
      this.projectTitle = `${city} - ${country} - ${name}`;
      this.detailUrl = `https://sk.zjwater.com/cp/skip/reservoirDetailSingle?prcd=${prcd}&name=${encodeName}&fromMap=&pName=&cName=&token=${token}`;
      this.showDetailDialog = true;
    },
  },
};
</script>
<style lang='scss' scoped>
.seawall {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  position: relative;
  .side-container {
    position: absolute;
    width: 460px;
    top: 107px;
    bottom: 10px;
    z-index: 5;
    &.left {
      left: 30px;
      .left-box-1 {
        height: 200px;
      }
      .left-box-2 {
        height: 200px;
      }
      .left-box-3 {
        height: 200px;
      }
      .left-box-4 {
        height: 200px;
      }
    }
    &.right {
      right: 30px;
      .right-box-1 {
        height: 200px;
      }
      .right-box-2 {
        height: 250px;
      }
      .right-box-3 {
        height: 350px;
      }
    }
  }
  .admn-basin-select-box {
    position: absolute;
    left: 500px;
    top: 150px;
    z-index: 4;
  }
  .seawall_right_back {
    position: absolute;
    top: 0;
    bottom: 0;
    right: 0px;
    width: 520px;
    height: 100%;
    z-index: 4;
    // box-shadow: 0px 0px 100px 100px #041846;
  }
  .seawall_left_back {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0px;
    width: 520px;
    height: 100%;
    z-index: 4;
    // box-shadow: 0px 0px 100px 100px #041846;
  }
  .map-container {
    position: absolute;
    inset: 0;
    z-index: 3;
    padding-top: 50px;
  }
  .reservoir_dialog {
    background: #fff;
    z-index: 999;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    .reservoir_title {
      padding: 16px;
      background: #2478f0;
      // padding-left: 16px;
      color: #fff;
      font-size: 20px;
      height: 40px;
      line-height: 40px;
      @include flexbox();
      @include flexAC();
      // @include flexJC(center);
      img {
        margin-right: 10px;
        cursor: pointer;
      }
    }
    .reservoir_dialog_iframe {
      height: calc(100% - 120px);
      width: 100%;
      overflow: hidden;
    }
  }
}
</style>