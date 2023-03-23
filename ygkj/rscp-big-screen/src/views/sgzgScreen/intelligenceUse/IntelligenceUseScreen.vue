<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-17 14:37:29
 * @Description: 智慧运用
 * @FilePath: /rscp-big-screen/src/views/sgzgScreen/intelligenceUse/IntelligenceUseScreen.vue
-->
<template>
  <div class="screen_wrap">
    <section class="drawer_left" :class="{ showLeft: drawerShowLeft }">
      <reservoir-left @selectProjectType="selectProjectType"></reservoir-left>
      <img
        class="drawer_left_btn"
        v-show="drawerShowLeft"
        @click="close('Left')"
        src="../../../assets/images/drawer_left1.png"
        alt=""
      />
      <img
        class="drawer_left_btn"
        v-show="!drawerShowLeft"
        @click="open('Left')"
        src="../../../assets/images/drawer_left2.png"
        alt=""
      />
    </section>
    <reservoir-map ref="reservoirMap"></reservoir-map>
    <section class="drawer_right" :class="{ showRight: drawerShowRight }">
      
      <!-- 水库 -->
      <reservoir-right-bxsk v-if="projectType == '水库' && rightIndex === 0"></reservoir-right-bxsk>
      <reservoir-right-two v-if="projectType == '水库' && rightIndex === 1"></reservoir-right-two>
      <reservoir-right-sthe v-if="projectType == '水库' && rightIndex === 2"></reservoir-right-sthe>
      <reservoir-right-one v-if="projectType == '水库' && rightIndex === 3"></reservoir-right-one>
      <reservoir-right-all v-if="projectType == '水库' && rightIndex === 4"></reservoir-right-all>
      <reservoir-right-xsl v-if="projectType == '水库' && rightIndex === 5"></reservoir-right-xsl>
      <reservoir-right-kgkn v-if="projectType == '水库' && rightIndex === 6"></reservoir-right-kgkn>
      <reservoir-right-cwyj v-if="projectType == '水库' && rightIndex ===7" :rightSelectList2.sync="rightSelectList2"></reservoir-right-cwyj>
      <!-- 堤防 -->
      <dike-right v-if="projectType == '堤防'" :safeStatus="safeStatus"></dike-right>
      <!-- 水闸 -->
      <sluice-right v-if="projectType == '水闸'" :safeStatus="safeStatus"></sluice-right>
      <!-- 泵站 -->
      <pump-right v-if="projectType == '泵站'" :safeStatus="safeStatus"></pump-right>
      <!-- 闸站 -->
      <gate-right v-if="projectType == '闸站'" :safeStatus="safeStatus"></gate-right>



      <img
        class="drawer_right_btn"
        v-show="drawerShowRight"
        @click="close('Right')"
        src="../../../assets/images/drawer_right1.png"
        alt=""
      />
      <img
        class="drawer_right_btn"
        v-show="!drawerShowRight"
        @click="open('Right')"
        src="../../../assets/images/drawer_right2.png"
        alt=""
      />
      <img
        class="right_select_open"
        v-if="!isShowHideLayer"
        src="../../../assets/images/right_select.png"
        @click="showHideLayer()"
      />
      <div
        class="right_select"
        v-if="isShowHideLayer && projectType == '水库'"
        v-loading="selectLoading"
        v-show="drawerShowRight"
      >
        <div class="right_select_title">
          <img src="../../../assets/images/title_ht.png" alt="" />
          安全状态
        </div>
        <div
          class="right_select_item"
          :class="index === rightIndex ? 'active' : ''"
          v-for="(item, index) in rightSelectList1"
          :key="index"
          @click="rightSelectClick(index)"
        >
          <p>{{ item.name }}</p>
          <span>
            <span>
              {{ item.num ? item.num : 0 }}
              <i class="zuo">({{ item.unit }})</i>
            </span>
            <!-- <span v-else>暂无数据</span> -->
          </span>
        </div>
        <div class="right_select_title">
          <img src="../../../assets/images/title_yj.png" alt="" />
          运行研判
        </div>
        <div
          class="right_select_item"
          :class="idx + 5 === rightIndex ? 'active' : ''"
          v-for="(ite, idx) in rightSelectList2"
          :key="ite.name"
          @click="rightSelectClick(idx + 5)"
        >
          <p>{{ ite.name }}</p>
          <span>
            <span>
              {{ ite.num }}
              <i class="zuo">({{ ite.unit }})</i>
            </span>
            <!-- <span v-else>暂无数据</span> -->
          </span>
        </div>
        <div class="right_select_close" @click="showHideLayer()">
          收起
          <img src="../../../assets/images/up.png" alt="" />
        </div>
      </div>

      <div
        class="right_select"
        v-if="isShowHideLayer && projectType != '水库'"
        v-loading="selectLoading"
        v-show="drawerShowRight"
      >
        <div class="right_select_title">
          <img src="../../../assets/images/title_ht.png" alt="" />
          安全状态
        </div>
        <div
          class="right_select_item"
          :class="index === rightIndex ? 'active' : ''"
          v-for="(item, index) in rightSelectList"
          :key="index"
          @click="rightSelectClickOther(index)"
        >
          <p>{{ item.name }}</p>
          <span>
            <span>
              {{ item.num ? item.num : 0 }}
              <i class="zuo">({{ item.unit }})</i>
            </span>
            <!-- <span v-else>暂无数据</span> -->
          </span>
        </div>
        <div class="right_select_close" @click="showHideLayer()">
          收起
          <img src="../../../assets/images/up.png" alt="" />
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
import ReservoirMap from "./intelligenceUseMap/IntelligenceUseMap";
import ReservoirLeft from "./intelligenceUseLeft/IntelligenceUseLeft";
import ReservoirRightCwyj from "../../rscpDriveScreen/reservoir/reservoirRight/ReservoirRightCwyj";
import ReservoirRightBxsk from "../../rscpDriveScreen/reservoir/reservoirRight/ReservoirRightBxsk";
import ReservoirRightSthe from "../../rscpDriveScreen/reservoir/reservoirRight/ReservoirRightSthe";
import ReservoirRightTwo from "../../rscpDriveScreen/reservoir/reservoirRight/ReservoirRightTwo";
import ReservoirRightOne from "../../rscpDriveScreen/reservoir/reservoirRight/ReservoirRightOne";
import ReservoirRightAll from "../../rscpDriveScreen/reservoir/reservoirRight/ReservoirRightAll";
import ReservoirRightXsl from "../../rscpDriveScreen/reservoir/reservoirRight/ReservoirRightXsl";
import ReservoirRightKgkn from "../../rscpDriveScreen/reservoir/reservoirRight/ReservoirRightKgkn";

import DikeRight from "./intelligenceUseRight/DikeRight";
import SluiceRight from "./intelligenceUseRight/SluiceRight";
import PumpRight from "./intelligenceUseRight/PumpRight";
import GateRight from "./intelligenceUseRight/GateRight";

import PreviewPhoto from "@/components/PreviewPhoto";
import PreviewVideo from "@/components/PreviewVideo";
import PreviewCamera from "@/components/PreviewCamera";
import eventBus from "@/utils/eventBus";

import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
import { dikeStatisticApi } from "@/api/api_dike";
import { sluiceStatisticApi } from "@/api/api_sluice";
import { pumpStatisticApi } from "@/api/api_pump";
import { gateStatisticApi } from "@/api/api_gate";

export default {
  name: "ReservoirScreen",
  props: {},
  components: {
    ReservoirMap,
    ReservoirLeft,
    ReservoirRightCwyj,
    ReservoirRightBxsk,
    ReservoirRightSthe,
    ReservoirRightTwo,
    ReservoirRightOne,
    ReservoirRightAll,
    ReservoirRightXsl,
    ReservoirRightKgkn,
    DikeRight,
    SluiceRight,
    PumpRight,
    GateRight,
    PreviewPhoto,
    PreviewVideo,
    PreviewCamera,
  },
  computed: {
    reservoirSelectFlag() {
      return this.$store.state.reservoirSelectFlag;
    },
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.opt.adcd = val ? val : this.$localData("get", "userInfo").adcd;
      if (val == "33") {
        this.opt.superviseLevel = 1;
      } else {
        this.opt.superviseLevel = "";
      }
      this.getRightSelectList();
    },
    leftSelectProjectScale(val) {
      this.setOptions("scale", val);
      this.getRightSelectList();
    },
  },
  data() {
    return {
      drawerShowLeft: true,
      drawerShowRight: true,
      rightIndex: 0,
      rightSelectList: [
        {
          name: "全部堤防",
          num: 0,
          unit: "条",
          safeStatus: 'totalCnt'
        },
        {
          name: "未加固三类堤",
          num: 0,
          unit: "条",
          safeStatus: 'thirdCnt'
        },
        {
          name: "未加固二类堤",
          num: 0,
          unit: "条",
          safeStatus: 'secondCnt'
        },
        {
          name: "加固中堤防",
          num: 0,
          unit: "条",
          safeStatus: 'processCnt'
        },
        {
          name: "一类堤",
          num: 0,
          unit: "条",
          safeStatus: 'firstCnt'
        },
      ],
      rightSelectList1: [
        {
          name: "未加固三类坝",
          num: 0,
          unit: "座",
        },
        {
          name: "未加固二类坝",
          num: 0,
          unit: "座",
        },
        {
          name: "加固中水库",
          num: 0,
          unit: "座",
        },
        {
          name: "一类坝水库",
          num: 0,
          unit: "座",
        },
        {
          name: "全部水库",
          num: 0,
          unit: "座",
        },
      ],
      rightSelectList2: [
        {
          name: "蓄水率",
          num: 0,
          unit: "%",
        },
        {
          name: "可纳洪/可供水",
          num: 0,
          unit: "亿方",
        },
      ],
      selectLoading: true,
      showLookPhotosPopup: false,
      lookPhotosLoading: true,
      photosList: [],
      reservoirName: "",
      showLookVideoPopup: false,
      lookVideoLoading: true,
      videoUrl: "",
      opt: {
        statistic: "mapTabs",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
      showDetailDialog: false,
      detailUrl: "",
      isShowHideLayer: true,
      projectTitle: "详情",
      prcd: "",
      skName: "",
      showLookCameraPopup: false,
      provincialLevel: false,
      projectType: '水库',
      safeStatus: 'totalCnt',
    };
  },
  mounted() {
    eventBus.$on("custom-event", (msg) => {
      this.rightSelectClick(msg);
    }); // 当 ChildComponent 被挂载，控制台中将显示一条消息

    this.getRightSelectList();
    if (this.$localData("get", "userInfo").adcd == "330000000000") {
      this.provincialLevel = true;
    }
  },
  methods: {
    selectProjectType(projectType){
      this.rightIndex = 0
      this.safeStatus = 'totalCnt',
      this.projectType = projectType
      this.getRightSelectListOther(projectType)
    },
    showHideLayer() {
      this.isShowHideLayer = !this.isShowHideLayer;
    },
    setOptions(type, val) {
      if (val) {
        // val不为空
        if (this.opt.itemCategory) {
          if (this.opt.itemCategory.indexOf(type) == -1) {
            // 不存在该参数
            this.opt.itemCategory = this.opt.itemCategory + "," + type;
            this.opt.itemValue = this.opt.itemValue + "," + val;
          } else {
            // 已存在该参数 更新参数
            let itemArr = this.opt.itemCategory.split(",");
            let index = itemArr.indexValue(type);
            let valueArr = this.opt.itemValue.toString().split(",");
            valueArr[index] = val;
            this.opt.itemValue = valueArr.join(",");
          }
        } else {
          // 没有参数
          this.opt.itemCategory = type;
          this.opt.itemValue = val;
        }
      } else {
        // 为空
        if (this.opt.itemCategory) {
          if (this.opt.itemCategory.indexOf(type) != -1) {
            // 存在该参数 清空参数
            let itemArr = this.opt.itemCategory.split(",");
            let valueArr = this.opt.itemValue.toString().split(",");
            let index = itemArr.indexValue(type);
            itemArr.splice(index, 1);
            valueArr.splice(index, 1);
            this.opt.itemCategory = itemArr.join(",");
            this.opt.itemValue = valueArr.join(",");
          }
        }
      }
    },
    async getRightSelectList() {
      this.selectLoading = true;
      let res = await reservoirLeftStatisticApi(this.opt);
      if (res.success) {
        let knx = ""; //addtion 可纳蓄
        let kgs = ""; //capable 供水
        let unit = "亿m³";
        // knx = res.data[0].capable > 10000 ? (res.data[0].capable / 10000).toFixed(0) : res.data[0].capable.toFixed(0)
        // kgs = res.data[0].addtion > 10000 ? (res.data[0].addtion / 10000).toFixed(0) : res.data[0].addtion.toFixed(0)

        kgs = res.data[0].capable
          ? (res.data[0].capable / 10000).toFixed(0)
          : 0;
        knx = res.data[0].addtion
          ? (res.data[0].addtion / 10000).toFixed(0)
          : 0;
        this.rightSelectList1 = [
          {
            name: "未加固三类坝",
            num: res.data[0].thirdClass ? res.data[0].thirdClass : 0,
            unit: "座",
          },
          {
            name: "未加固二类坝",
            num: res.data[0].secondClass ? res.data[0].secondClass : 0,
            unit: "座",
          },
          {
            name: "加固中水库",
            num: res.data[0].processCnt ? res.data[0].processCnt : 0,
            unit: "座",
          },
          {
            name: "一类坝水库",
            num: res.data[0].firstClass ? res.data[0].firstClass : 0,
            unit: "座",
          },
          {
            name: "全部水库",
            num: res.data[0].totalCnt ? res.data[0].totalCnt : 0,
            unit: "座",
          },
        ];
        this.rightSelectList2 = [
          {
            name: "蓄水率",
            num: res.data[0].storagePerc ? res.data[0].storagePerc : 0,
            unit: "%",
          },
          {
            name: "可纳洪/可供水",
            num: knx + "/" + kgs,
            unit: unit,
          }
        ];
        this.$store.commit("SET_RESERVOIR_TABS", [
          ...this.rightSelectList1,
          ...this.rightSelectList2,
        ]);
      }
      this.selectLoading = false;
    },
    async getRightSelectListOther(type) {
      this.selectLoading = true;
      let opt = {
        statistic: "mapTabs",
        adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData("get", "userInfo").adcd
      }
      let res = {}
      let text1 = '堤防'
      let text2 = '堤'
      let unit = '条'
      switch (type){
        case '堤防':
          res = await dikeStatisticApi(opt)
          text1 = '堤防'
          text2 = '堤'
          unit = '条'
          break;
        case '水闸':
          res = await sluiceStatisticApi(opt)
          text1 = '水闸'
          text2 = '闸'
          unit = '座'
          break;
        case '泵站':
          res = await pumpStatisticApi(opt)
          text1 = '泵站'
          text2 = '泵'
          unit = '座'
          break;
        case '闸站':
          res = await gateStatisticApi(opt)
          text1 = '闸站'
          text2 = '闸站'
          unit = '座'
          break;
      }
      if (res.success){
        this.rightSelectList = [
          {
            name: "全部" + text1,
            num: res.data[0].totalCnt,
            unit: unit,
            safeStatus: 'totalCnt'
          },
          {
            name: "未加固三类" + text2,
            num: res.data[0].thirdCnt,
            unit: unit,
            safeStatus: 'thirdCnt'
          },
          {
            name: "未加固二类" + text2,
            num: res.data[0].secondCnt,
            unit: unit,
            safeStatus: 'secondCnt'
          },
          {
            name: "加固中" + text1,
            num: res.data[0].processCnt,
            unit: unit,
            safeStatus: 'processCnt'
          },
          {
            name: "一类" + text2,
            num: res.data[0].firstCnt,
            unit: unit,
            safeStatus: 'firstCnt'
          },
        ];
      }
      this.selectLoading = false;
    },

    rightSelectClick(index) {
      if (this.reservoirSelectFlag) {
        this.rightIndex = index;
      } else {
        return false;
      }
    },
    rightSelectClickOther(index){
      if (this.reservoirSelectFlag) {
        this.safeStatus = this.rightSelectList[index].safeStatus
        this.rightIndex = index;
      }
    },
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
    showMapPoints(pointArr, type) {
      this.$nextTick(() => {
        this.$refs.reservoirMap.newMapPoints(pointArr, type);
      });
    },

    // 画线
    showMapLines(lineArr){
      this.$nextTick(() => {
        this.$refs.reservoirMap._drawLines(lineArr);
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

    // 地图右下角操作
    setZoom(type) {
      this.$refs.reservoirMap._setZoom(type);
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

.map_legend {
  width: 150px !important;
  right: -180px !important;
  .map_legend_title {
    height: 28px !important;
  }
  .map_legend_list {
    .map_legend_item {
      width: 100% !important;
    }
  }
}
.right_select{
  top: 135px!important;
}
.right_select_title {
  @include flexbox();
  @include flexAC();
  padding: 8px 16px;
  color: #193cc5;
  font-size: 14px;
  line-height: 16px;
  background: -webkit-linear-gradient(
    90deg,
    #e4f3fc 13%,
    rgba(201, 219, 250, 0) 98%
  );
  background: -o-linear-gradient(
    90deg,
    #e4f3fc 13%,
    rgba(201, 219, 250, 0) 98%
  );
  background: -moz-linear-gradient(
    90deg,
    #e4f3fc 13%,
    rgba(201, 219, 250, 0) 98%
  );
  background: linear-gradient(90deg, #e4f3fc 13%, rgba(201, 219, 250, 0) 98%);
  img {
    margin-right: 8px;
  }
}
.right_select_close {
  cursor: pointer;
  @include flexbox();
  @include flexAC();
  @include flexJC(center);
  padding: 8px 0;
  border-top: 1px solid #d6d6d6;
  color: #333;
  background: #f9f9f9;
  font-size: 14px;
  line-height: 16px;
  img {
    margin-left: 6px;
  }
}
.drawer_right_home {
  position: absolute;
  top: 6px;
  left: -85px;
  cursor: pointer;
}
.right_select_open {
  position: absolute;
  top: 75px;
  left: -78px;
  cursor: pointer;
}
.map_btns {
}
.map_btns1 {
  left: -300px !important;
  width: 300px !important;
}
.map_btns2 {
  left: -200px !important;
  width: 195px !important;
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
