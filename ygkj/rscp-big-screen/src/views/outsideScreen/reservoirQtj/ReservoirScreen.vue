<!--
 * @Author: hanyu
 * @LastEditTime: 2021-08-13 18:03:31
 * @Description: 
 * @FilePath: /rscp-big-screen/src/views/bigScreen/reservoirQtj/ReservoirScreen.vue
-->
<template>
  <div class="screen_wrap">
    <reservoir-map ref="reservoirMap"></reservoir-map>
    <section
      class="drawer_right_qtj"
      :class="{ showRightQtj: drawerShowRight }"
    >
      <!-- 三类坝 -->
      <reservoir-right-bxsk></reservoir-right-bxsk>
      <div
        class="drawer_right_btn"
        v-show="drawerShowRight"
        @click="close('Right')"
      >
        <i class="el-icon-caret-right" style="color:#5782ef"></i>
      </div>
      <div
        class="drawer_right_btn"
        v-show="!drawerShowRight"
        @click="open('Right')"
      >
        <i class="el-icon-caret-left" style="color:#5782ef"></i>
      </div>
      <!-- <img class="drawer_right_btn" v-show="drawerShowRight" @click="close('Right')" src="../../../assets/images/drawer_right1.png" alt="">
      <img class="drawer_right_btn" v-show="!drawerShowRight" @click="open('Right')" src="../../../assets/images/drawer_right2.png" alt=""> -->

      <!-- <div class="map_btns">
        <div class="map_btns_tool">
          <div class="tool_list" v-show="showTool">
            <div class="tool_item" @click="selectTool('length')">长度测量</div>
            <div class="tool_item" @click="selectTool('area')">面积测量</div>
            <div class="tool_item" @click="selectTool('none')">清除测量</div>
          </div>
          <div class="tool_title" @click="showTool = !showTool">
            <i class="el-icon-suitcase-1"></i>
            <span>工具箱</span>
            <i v-show="!showTool" class="el-icon-caret-top"></i>
            <i v-show="showTool" class="el-icon-caret-bottom"></i>
          </div>
        </div>
        <div class="map_btns_zoom">
          <div @click="setZoom('plus')"><i class="el-icon-plus"></i></div>
          <div @click="setZoom('minus')"><i class="el-icon-minus"></i></div>
        </div>
      </div> -->
      <div class="map_legend">
        <div class="map_legend_title">
          <div class="map_legend_title_left">
            <img src="../../../assets/images/legend_icon.png" alt="" />
            <span>图例</span>
          </div>
        </div>
        <div class="map_legend_list">
          <div
            class="map_legend_item"
            v-for="(item, index) in legendList"
            :key="index"
          >
            <i :style="{ backgroundColor: item.color }"></i>
            <span>{{ item.name }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 弹出层 -->
    <preview-photo
      v-if="showLookPhotosPopup"
      :loading="lookPhotosLoading"
      :photoList="photosList"
      :reservoirName="reservoirName"
      @closeLookPhotosPopup="closeLookPhotosPopup"
    ></preview-photo>
    <preview-video
      v-if="showLookVideoPopup"
      :loading="lookVideoLoading"
      :videoUrl="videoUrl"
      @closeLookVideoPopup="closeLookVideoPopup"
    ></preview-video>
    <preview-camera
      v-if="showLookCameraPopup"
      :prcd="prcd"
      :name="skName"
      @closeLookCameraPopup="closeLookCameraPopup"
    ></preview-camera>
    <preview-brief
      v-if="showLookBriefPopup"
      @closeLookBriefPopup="closeLookBriefPopup"
    ></preview-brief>
    <div class="reservoir_dialog" v-show="showDetailDialog">
      <div class="reservoir_title">
        <img
          @click="showDetailDialog = false"
          src="../../../assets/images/back2.png"
          alt=""
        />
        <p>{{ projectTitle }}</p>
      </div>
      <!-- <img class="back" @click="showDetailDialog = false" src="../../../assets/images/back2.png" alt=""> -->
      <iframe
        class="reservoir_dialog_iframe"
        :src="detailUrl"
        frameborder="0"
      ></iframe>
    </div>
  </div>
</template>

<script>
import ReservoirMap from "./ReservoirMap";
import ReservoirRightBxsk from "./ReservoirRightBxsk";
import PreviewPhoto from "@/components/PreviewPhoto";
import PreviewVideo from "@/components/PreviewVideo";
import PreviewCamera from "@/components/PreviewCamera";
import PreviewBrief from "@/components/PreviewBrief";

import { reservoirLeftStatisticApi } from "@/api/api_reservoir";

export default {
  name: "ReservoirScreen",
  props: {},
  components: {
    ReservoirMap,
    ReservoirRightBxsk,
    PreviewPhoto,
    PreviewVideo,
    PreviewCamera,
    PreviewBrief,
  },
  computed: {},
  watch: {},
  data() {
    return {
      drawerShowRight: true,
      showLookPhotosPopup: false,
      lookPhotosLoading: true,
      photosList: [],
      reservoirName: "",
      showLookVideoPopup: false,
      lookVideoLoading: true,
      videoUrl: "",
      showLookBriefPopup: false,
      showTool: false,
      jhFlag: true,
      legendList: [
        {
          name: "空库运行",
          color: "#F7B500",
        },
        // {
        //   name: '超死水位1米',
        //   color: '#F7B500'
        // },
        {
          name: "超限制水位",
          color: "#FA6400",
        },
        {
          name: "超正常水位",
          color: "#E02020",
        },
      ],
      opt: {
        statistic: "mapTabs",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
      showLookMsgPopup: false,
      fanganUrl: "",
      showDetailDialog: false,
      detailUrl: "",
      projectTitle: "详情",
      prcd: "",
      skName: "",
      showLookCameraPopup: false,
    };
  },
  mounted() {
    this.getRightSelectList();
  },
  methods: {
    open(type) {
      this["drawerShow" + type] = true;
    },
    close(type) {
      this["drawerShow" + type] = false;
    },

    // 画点 聚合
    showMapPointJh(pointArr, type) {
      this.$nextTick(() => {
        this.$refs.reservoirMap.newMapJh(pointArr, type);
      });
    },
    // 画点 不聚合
    showMapPoints(pointArr) {
      this.$nextTick(() => {
        this.$refs.reservoirMap.newMapPoints(pointArr);
      });
    },

    //显示弹窗
    showOverlays(pointData) {
      this.$nextTick(() => {
        this.$refs.reservoirMap._drawOverlays(pointData);
      });
    },

    // 查看图片
    openLookPhotosPopup() {
      this.showLookPhotosPopup = false;
      this.$nextTick(() => {
        this.lookPhotosLoading = true;
        this.showLookPhotosPopup = true;
      });
    },
    closeLookPhotosPopup() {
      this.showLookPhotosPopup = false;
    },
    showPhotosList(arr, reservoirName) {
      setTimeout(() => {
        this.lookPhotosLoading = false;
        this.photosList = arr;
        this.reservoirName = reservoirName;
      });
    },

    // 查看视频
    openLookVideoPopup() {
      this.showLookVideoPopup = false;
      this.$nextTick(() => {
        this.lookVideoLoading = true;
        this.showLookVideoPopup = true;
      });
    },
    closeLookVideoPopup() {
      this.showLookVideoPopup = false;
    },
    showVideo(url) {
      this.lookVideoLoading = false;
      this.videoUrl = url;
    },

    // 查看带列表的视频
    openLookCameraPopup(prcd, skName) {
      this.prcd = prcd;
      this.skName = skName;
      this.showLookCameraPopup = false;
      this.$nextTick(() => {
        this.showLookCameraPopup = true;
      });
    },
    closeLookCameraPopup() {
      this.showLookCameraPopup = false;
    },

    // 查看工程详情
    openDetailPopup(row) {
      console.log(row, "--");
      let prcd = row.PRCD;
      let name = row.NAME;
      let token = this.$localData("get", "token");
      name = encodeURI(encodeURI(name));
      this.projectTitle = `${row.cityADNM} - ${row.countryADNM} - ${row.NAME}`;
      this.detailUrl = `https://sk.zjwater.com/cp/skip/reservoirDetailSingle?prcd=${prcd}&name=${name}&fromMap=&pName=&cName=&token=${token}`;
      // this.detailUrl = `http://www.baidu.com`
      this.showDetailDialog = true;
    },

    // 查看简报
    openLookBriefPopup() {
      this.showLookBriefPopup = false;
      this.$nextTick(() => {
        this.showLookBriefPopup = true;
      });
    },
    closeLookBriefPopup() {
      this.showLookBriefPopup = false;
    },

    // 预警短信
    openMassageDialog() {
      this.showLookMsgPopup = false;
      this.$nextTick(() => {
        this.showLookMsgPopup = true;
      });
    },
    closeLookMessagePopup() {
      this.showLookMsgPopup = false;
    },

    // 地图右下角操作
    setZoom(type) {
      this.$refs.reservoirMap._setZoom(type);
    },
    selectTool(type) {
      this.showTool = false;
      this.$refs.reservoirMap.mapToolBar(type);
    },
    changeSwitch(val) {
      if (val) {
        this.setCluster(100);
      } else {
        this.setCluster(0);
      }
    },
    setCluster(val) {
      this.$refs.reservoirMap.setCluster(val);
    },

    // 查看点位
    setView(lng, lat) {
      this.$refs.reservoirMap._setView(lng, lat);
    },

    // 设置弹窗
    setPopup(pointData) {
      this.$refs.reservoirMap._setPopup(pointData);
    },

    // 设置选中点位
    showOneSelectPoint(points) {
      this.$refs.reservoirMap._drawPoints(points);
    },

    // 返回首页
    goHome() {
      this.$router.push("/menu");
    },
  },
};
</script>
<style lang="scss" scoped>
.fangan_iframe {
  width: 100%;
  height: 600px;
}
.reservoir_dialog {
  background: #fff;
  z-index: 999999;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  // .back{
  //   cursor: pointer;
  //   position: absolute;
  //   top: 4px;
  //   left: 0px;
  //   width: 30px;
  //   height: 30px;
  // }
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

.map_btns {
  left: -200px !important;
  width: 194px !important;
}

.drawer_right_qtj {
  position: fixed;
  z-index: 1000;
  top: 0px;
  bottom: 0px;
  width: 400px;
  height: 90%;
  transform: translateY(-50%);
  top: 50%;
  background: hsla(0, 0%, 100%, 0.9);
  @include transition(all, 0.3s, ease);
  right: -401px;
  .drawer_right_btn {
    width: 24px;
    height: 58px;
    line-height: 58px;
    text-align: center;
    background: hsla(0, 0%, 100%, 0.75);
    position: absolute;
    font-size: 22px;
    left: -22px;
    top: 80px;
    margin-top: -38px;
    cursor: pointer;
  }
  .map_legend {
    position: absolute;
    bottom: 16px;
    left: -140px;
    background: #fff;
    width: 120px;
    border-radius: 4px;
    .map_legend_list {
      @include flexbox();
      @include flexJC(space-between);
      @include flexAC();
      @include flexflow(row wrap);
      padding: 8px 0;
      .map_legend_item {
        width: 100%;
        padding: 8px 0;
        @include flexbox();
        // @include flexJC(center);
        @include flexAC();
        & > p {
          width: 30px;
          height: 3px;
          margin-right: 8px;
        }
        & > i {
          width: 12px;
          height: 12px;
          border-radius: 6px;
          margin-right: 8px;
          margin-left: 16px;
        }
      }
    }
    .map_legend_title {
      height: 40px;
      @include flexbox();
      @include flexJC(space-between);
      @include flexAC();
      background: #f9f9f9;
      .map_legend_title_left {
        @include flexbox();
        @include flexAC();
        img {
          margin: 0 6px 0 12px;
        }
      }
      .map_legend_title_right {
        @include flexbox();
        @include flexAC();
        .el-switch {
          width: 36px;
        }
        & > span {
          margin-right: 8px;
        }
        i {
          margin: 0 12px 0 8px;
          color: #999;
          cursor: pointer;
        }
      }
    }
  }
}
.showRightQtj {
  right: 20px;
}
</style>

<style lang="scss">
.fade-enter-active {
  transition: all 0.6s ease;
}
.fade-leave-active {
  transition: all 0.2s ease;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.fangan_dialog .el-dialog__header {
  padding-bottom: 0;
}
.fangan_dialog .el-dialog__body {
  padding-top: 0;
}
</style>
