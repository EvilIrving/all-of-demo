
<template>
  <div class="screen_wrap">
    <div class="screen_title">
      <div class="screen_title_box">
        <!-- <div class="screen_title_box_left"> -->
        <img class="logo" src="../../../assets/images/waterIcon-white.png" alt="" />
        <p class="title">海塘防潮智能模块</p>
        <div class="top-right-icon">
          <img @click="showFanganDialog = true" class="ppt-look-icon" src="../../../assets/images/ppt-look-icon1.png" alt="" />
        </div>
        <!-- </div> -->
        <section class="ht_tabs">
          <div class="tabs_item" :class="{active:index === tabsIndex,haveTips:showTips && index !== tabsIndex}" v-for="(item, index) in tabsList" :key="index" tips='数据正在加载...请稍后' @click="tabsClick(index)">
            <p>{{ item.name }}</p>
          </div>
        </section>
        <p class="time">{{ nowTime }}</p>
        <div class="person" v-if="userPhone != '15958118384'">
          <div class="person_right">
            <img :src="$config.imgUrl + 'man.png'" alt="" />
            <span>{{ userName }}</span>
          </div>
        </div>
      </div>
    </div>

    <seawall-map ref="seawallMap" :activeName='activeName' @removeCity="removeCityType = false"></seawall-map>
    <img class="seawall_right_back" src="../../../assets/images/right-bg_pic.png" alt="">
    <section class="right_box" :class="{ showRight: drawerShowRight }">
      <seawall-screen-legend v-if="sgzgMapType !== '潮位预报' && sgzgMapType !== '闭环管控'" :sgzgMapType="sgzgMapType" />

      <div class="seawall-right">
        <seawall-left v-if="activeName === 'first'"></seawall-left>
        <seawall-right-typhoon v-if="activeName === 'second'"></seawall-right-typhoon>
        <seawall-right-sscw ref="childProp" v-if="activeName === 'third'"></seawall-right-sscw>
        <seawall-right-aljs ref="childProp" v-if="activeName === 'four'"></seawall-right-aljs>
      </div>

      <div class="map_btns">
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
      </div>
    </section>
    <preview-photo v-if="showLookPhotosPopup" :loading="lookPhotosLoading" :photoList="photosList" :seawallName="seawallName" @closeLookPhotosPopup="closeLookPhotosPopup"></preview-photo>
    <preview-video v-if="showLookVideoPopup" :loading="lookVideoLoading" :videoUrl="videoUrl" @closeLookVideoPopup="closeLookVideoPopup"></preview-video>

    <PrimaryDialog mainTitle=" " class="seawall_primary_dialog" :visible="showFanganDialog" width="1200px" :showFooter="false" top="5vh" :appendToBody="true" :fullscreen="fullscreen" @changeFullscreen="fullscreen = !fullscreen" @handleClose="showFanganDialog = false">
      <div class="fangan_dialog" :class="{'fullscreen-box': fullscreen}">
        <img class="img-small img-big" src="../../../assets/images/ppt_pic.png" alt="" />
      </div>
    </PrimaryDialog>

    <!-- <el-dialog :title="projectName" class="seawall_primary_dialog" :visible.sync="showDetailDialog" fullscreen>
      <iframe class="seawall_dialog_iframe" :src="detailUrl" frameborder="0"></iframe>
    </el-dialog> -->

    <div class="reservoir_dialog" v-show="showDetailDialog">
      <div class="reservoir_title">
        <img @click="showDetailDialog = false" src="@/assets/images/back2.png" alt="" />
        <p>{{ projectTitle }}</p>
      </div>
      <iframe class="reservoir_dialog_iframe" :src="detailUrl" frameborder="0"></iframe>
    </div>

    <primary-dialog class="seawall_primary_dialog" :showFooter="false" :mainTitle="tabsDialogTitle" :modal="modal" :visible="dialogVisible" @handleClose="handleClose" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" width="85%" top="5vh">
      <div class="dialog-content" :class="{ 'fullscreen-box': fullscreen }">
        <generation ref="generation" :fullscreen="fullscreen" v-if="
            dialogVisible &&  sgzgMapType !== '潮位预报' 
          " :sgzgMapType="sgzgMapType" :planId="planId" :checkDataType='checkDataType' />
        <iframe class="seawall_dialog_iframe" v-else :src="exportUrl" frameborder="0"></iframe>
      </div>
    </primary-dialog>

    <!--概况-->
    <section class="general-situation" :class="{showLeft: drawerShowLeft}" v-if="removeCityType">
      <div class="general-title">潮位实况</div>
      <!-- <div class="general-time">更新时间：2022-10-19</div> -->
      <div class="general-detail">
        <!-- <div class="head-detail">
          <div>站点</div>
          <div>潮位</div>
          <div>时间</div>
        </div> -->
        <div class="service-wrap">
          <vue-seamless-scroll style="height: 100%" :data="realTimeList" :class-option="defaultOption" ref="serviceReal">
            <div class="service_real">
              <div class="service_real_item scroll_item_realtime" v-for="(item, index) in realTimeList" :key="index">
                <div>{{ item.stnm }}</div>
                <div>{{ item.realTimeTide.toFixed(2) }}m</div>
                <div>{{ item.realTime.substring(5, 16) }}</div>
              </div>
            </div>
          </vue-seamless-scroll>
        </div>

       
      </div>
      <img class="left_menu cursor" v-show="drawerShowLeft" @click="close('Left')" :src="$config.zhImgUrl + 'left_packup.png'" alt="">
      <img class="left_menu cursor" v-show="!drawerShowLeft" @click="open('Left')" :src="$config.zhImgUrl + 'left_unbold.png'" alt="">

    </section>

    <div class="mask" v-show="showFanganDialog">
      <div :class="['report-dialog',{'full-screen':fullscreen}]">
        <!-- <div class="report-dialog-close">
          <i class="el-icon-close" style="z-index: 2" @click="closeTitlePhotosPopup()"></i>
        </div> -->
        <div class="dialog-icon-group">
          <i v-if="!fullscreen" class="dialog-icon dialog-zoom-in" @click="fullscreen = true"></i>
          <i v-else class="dialog-icon dialog-zoom-out" @click="fullscreen = false"></i>
          <i class="dialog-icon dialog-close" @click="showFanganDialog = false"></i>
        </div>
        <div :class="['report-dialog-inner',{'full-screen':fullscreen}]">
          <!-- arrow="always" -->
          <el-carousel ref="carousel" class="carousel_box ppt-carousel" indicator-position="inside" :autoplay="false">
            <el-carousel-item class="carousel_item" v-for="(item, index) in titlePhotosList" :key="index">
              <!-- <div class="swiper_img" :style="{backgroundImage:'url('+item.url+')'}" :title="item.name"></div> -->
              <!-- <el-image style="width:100%;height: 85.5vh;" :src="item.url" :fit="fit"></el-image> -->
              <img :src="item.url" alt="" :class="[{'full-box':fullscreen}]">
            </el-carousel-item>

          </el-carousel>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SeawallMap from "./seawallMap/SeawallMap";
import SeawallLeft from "./seawallLeft/SeawallLeft";
import SeawallRightDefault from "./seawallRight/SeawallRightDefault";
import SeawallRightSscw from "./seawallRight/SeawallRightSscw";
import SeawallRightAljs from "./seawallRight/SeawallRightAljs";
import SeawallRightTyphoon from "./seawallRight/SeawallRightTyphoon";
import SeawallScreenLegend from "./seawallLegend/SeawallScreenLegend.vue";
import PreviewPhoto from "@/components/PreviewPhoto";
import PreviewVideo from "@/components/PreviewVideo";
import PrimaryDialog from "./components/PrimaryDialog.vue";
import Generation from "./components/Generation.vue";
import { seawallLeftStatisticApi, getRealTimeData } from "@/api/api_seawall";
import vueSeamlessScroll from "vue-seamless-scroll";
export default {
  name: "SeawallScreen",
  props: {},
  components: {
    SeawallMap,
    SeawallLeft,
    SeawallRightDefault,
    SeawallRightSscw,
    SeawallRightAljs,
    SeawallRightTyphoon,
    PreviewPhoto,
    PreviewVideo,
    SeawallScreenLegend,
    PrimaryDialog,
    Generation,
    vueSeamlessScroll,
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd || this.$localData("get", "userInfo").adcd;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    },
    defaultOption() {
      return {
        step: 0.8, // 数值越大速度滚动越快
        limitMoveNum: this.realTimeList.length, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 48, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 500, // 单步运动停止的时间(默认值1000ms)
      };
    },
  },
  watch: {
    leftSelectAdcd(val) {
      if (val == "33") {
      }

    },
    tabsIndex(val) {
      this._getRealTimeData();
    },
    leftSelectProjectScale(val) {


    },
  },
  data() {
    return {
      tabsIndex: 0,
      exportUrl: "",
      tabsDialogTitle: "海塘概况",
      projectTitle: "海塘概况",
      sgzgMapType: "海塘概况",
      dialogVisible: false,
      modal: true,
      fullscreen: false,
      activeName: "first",
      drawerShowLeft: true,// 左侧收起
      drawerShowRight: true,
      titlePhotosList: [{ url: this.$config.zhImgUrl + "ppt_pic.png", name: "1" }], //新增标题轮播图弹窗
      tabsList: [
        {
          name: "海塘概况",
          sort: 0,
        },
        {
          name: "潮位预报",
          sort: 1,
        },
        {
          name: "风险研判",
          sort: 2,
        },
        {
          name: "闭环管控",
          sort: 3,
        },
      ],
      showLookPhotosPopup: false,
      lookPhotosLoading: true,
      photosList: [],
      seawallName: "",
      showLookVideoPopup: false,
      lookVideoLoading: true,
      videoUrl: "",
      showLegend: false,
      showTool: false,
      jhFlag: true,
      legendList: [
        {
          name: "一级海塘",
          color: "#E02020",
        },
        {
          name: "二级海塘",
          color: "#EB7E65",
        },
        {
          name: "三级海塘",
          color: "#F7C739",
        },
        {
          name: "四级海塘",
          color: "#193CC5",
        },
        {
          name: "五级海塘",
          color: "#1BC57E",
        },
        {
          name: "规模以下",
          color: "#C2C2C2",
        },
      ],
      showFanganDialog: false,
      fanganUrl: "",
      showDetailDialog: false,
      detailUrl: "",
      isShowHideLayer: false,
      projectName: "",
      planId: "", // 风险研判中 计划id
      checkDataType: true,  // 闭环管控中 是否复盘的状态 false为复盘
      userName: "",
      userPhone: "",
      nowTime: new Date().format("hh:mm:ss"),
      dateTimer: null,
      removeCityType: true,
      showTips: false,
      clickDisabled: false,
      realTimeList: [],
    };
  },
  mounted() {
    this.userName = this.$localData("get", "userInfo").psnm;
    this.userPhone = this.$localData("get", "userInfo").phone;
    this.dateTimer = setInterval(() => {
      this.nowTime = new Date().format("hh:mm:ss");
    }, 1000);
    this._getRealTimeData();
  },
  beforeDestroy() { },
  methods: {
    // 弹窗内打开 智慧水库标题轮播图
    lookPhotos() {
      this.showFanganDialog = true;
      this.titlePhotosList = [
        { url: this.$config.zhImgUrl + "ppt_pic.png", name: "1" },
      ];
    },
    // 获取实况数据
    async _getRealTimeData() {
      this.realTimeList = []
      let res = await getRealTimeData({
        interType: 1,
        extractPram: this.tabsIndex === 1 ? '2022-09-13 19:00:00' : ''
      });
      if (res.data.length > 0) {
        this.realTimeList = res.data;
        this.$nextTick(() => {
          setTimeout(() => {
            let dom = document.getElementsByClassName("service_real_item");
            let arr = Array.from(dom);
            arr.forEach((item, index) => {
              if (index % 2 === 0) {
                item.style.background = "rgba(0,186,255,0.2)";
              }
            });
          }, 0);
        });
      }
    },
    setRemoveCityType(type) {
      // this.removeCityType = type;
    },
    showHideLayer() {
      this.isShowHideLayer = !this.isShowHideLayer;
    },
    /** 
     * @function: 
     * @description: 
     * @param {*} payload
     * @param {*} checkDataType 闭环管控中 是否复盘的状态 false为复盘
     * @return {*}
     */
    openGernaretion(payload, checkDataType) {
      if (this.sgzgMapType == "风险研判") {
        this.planId = this.$refs.childProp.planId;
      }
      if (this.sgzgMapType == "闭环管控") {
        this.checkDataType = checkDataType;
        checkDataType ? this.tabsDialogTitle = "当前的闭环管控" : this.tabsDialogTitle = "复盘的闭环管控";
      }
      this.fullscreen = false;
      this.dialogVisible = true;
    },
    open(type) {
      this["drawerShow" + type] = true;
    },
    close(type) {
      this["drawerShow" + type] = false;
    },
    handleClose() {
      this.fullscreen = false;
      this.dialogVisible = false;
    },
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      this.$nextTick(() => {
        // this.$refs.generation.$refs.tb_wrappers.doLayout();
        this.$refs.generation.getTableList();
      });
    },
    //显示台风
    showTyphoon(tfid, type) {
      this.$nextTick(() => {
        this.$refs.seawallMap.showTyphoon(tfid, type);
      });
    },
    //移动到台风位置
    showTf() {
      this.$nextTick(() => {
        this.$refs.seawallMap.showTf();
      });
    },
    //清除台风
    removeTyphoonLayer() {
      this.$nextTick(() => {
        this.$refs.seawallMap.removeTyphoonLayer();
      });
    },
    // 画点 聚合
    showMapPointJh(pointArr, type) {
      this.$nextTick(() => {
        this.$refs.seawallMap.newMapJh(pointArr, type);
      });
    },

    // 海塘点位
    getHtList(pointArr) {
      this.$nextTick(() => {
        this.$refs.seawallMap.getHtList(pointArr);
      });
    },
    // 画点 不聚合
    showMapPoints(pointArr, type) {
      this.$nextTick(() => {
        this.$refs.seawallMap.newMapPoints(pointArr, type);
      });
    },

    // 画线
    showMapLines(lineArr) {
      this.$nextTick(() => {
        this.$refs.seawallMap._drawLines(lineArr);
      });
    },

    //显示弹窗
    showOverlays(pointData) {
      this.$nextTick(() => {
        this.$refs.seawallMap._drawOverlays(pointData);
      });
    },
    setCityfillColor() {
      this.$nextTick(() => {
        this.$refs.seawallMap.setCityfillColor();
      });
    },
    removeCityLayer() {
      this.$nextTick(() => {
        this.$refs.seawallMap.removeCityLayer();
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
    showPhotosList(arr, seawallName) {
      setTimeout(() => {
        this.lookPhotosLoading = false;
        this.photosList = arr;
        this.seawallName = seawallName;
      });
    },
    open(type) {
      this['drawerShow' + type] = true
    },
    close(type) {
      this['drawerShow' + type] = false
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

    selectTool(type) {
      this.showTool = false;
      this.$refs.seawallMap.mapToolBar(type);
    },
    tabsClick(index) {
      // 点击时 clickDisabled 是 true 就不可点击  showTips 设置为true 3s后设置clickDisabled为false showTips设置为false
      if (!this.clickDisabled) {
        // 不可重复点击
        if (index !== this.tabsIndex) {
          // this.showTips = true
          this.tabsIndex = index;
          this.removeCityType = false;
          switch (index) {
            case 0:
              this.activeName = "first";
              this.removeCityType = true;
              this.$store.commit('SET_TOUBUTABSACTIVE', 'first')
              this.sgzgMapType = "海塘概况";
              this.tabsDialogTitle = "海塘概况";
              break;
            case 1:
              this.activeName = "second";
              this.removeCityType = true;
              this.$store.commit('SET_TOUBUTABSACTIVE', 'second')
              this.exportUrl = this.$config.zhImgUrl + "风险隐患清单.pdf";
              this.sgzgMapType = "潮位预报";
              this.tabsDialogTitle = "潮位预报";
              break;
            case 2:
              this.activeName = "third";
              this.$store.commit('SET_TOUBUTABSACTIVE', 'third')
              this.sgzgMapType = "风险研判";
              this.tabsDialogTitle = "风险海塘清单";
              break;
            case 3:
              this.activeName = "four";
              this.$store.commit('SET_TOUBUTABSACTIVE', 'four')
              this.sgzgMapType = "闭环管控";
              this.checkDataType ? this.tabsDialogTitle = "当前的闭环管控" : this.tabsDialogTitle = "复盘的闭环管控";
              this.exportUrl = this.$config.zhImgUrl + "风险提示单.pdf";
              break;
          }
          this.$store.commit('SET_CHOOSED_ITEM', '分布')
          this.$store.commit('SET_LEFT_SELECT_ADCD', this.$localData("get", "userInfo").adcd)
          this.$refs.seawallMap.removeAllLayer();
          this.$refs.seawallMap.closePopup();

        }
      }

      this.clickDisabled = true
      this.showTips = true
      //  3s后开启点击
      setTimeout(() => {
        this.showTips = false
        this.clickDisabled = false
      }, 1500);
    },
    changeSwitch(val) {
      if (val) {
        this.setCluster(100);
      } else {
        this.setCluster(0);
      }
    },
    setCluster(val) {
      this.$refs.seawallMap.setCluster(val);
    },

    // 地图右下角操作
    setZoom(type) {
      this.$refs.seawallMap._setZoom(type);
    },

    // 查看点位
    setHtView(lnglat, zoom) {
      this.$refs.seawallMap._setHtView(lnglat, zoom);
    },

    // 查看点位
    setView(lng, lat) {
      this.$refs.seawallMap._setView(lng, lat);
    },

    // 设置弹窗
    setPopup(pointData) {
      this.$refs.seawallMap._setPopup(pointData);
    },

    // 设置选中点位
    showOneSelectPoint(points, flag, type) {
      this.$refs.seawallMap._drawPoints(points, flag, type);
    },

    // 返回首页
    goHome() {
      this.$router.push("/menu");
    },
  },
  beforeDestroy() {
    this.dateTimer = null;
  },
};
</script>
<style lang="scss" scoped>
.general-situation {
  position: absolute;
  top: 120px;
  left: -326px;
  background: rgba(0, 56, 160, 0.8);
  box-shadow: inset 0px 0px 16px 0px rgba(46, 228, 255, 0.5);
  border-radius: 4px;
  border: 1px solid #448ef7;
  transition: all 1s ease;
  &.showLeft {
    left: 16px;
    transition: all 1s ease;
  }
  .general-title {
    width: 324px;
    height: 46px;
    background: url("../../../assets/images/general-title.png") no-repeat;
    background-size: 100% 100%;
    margin: 0 auto;
    text-align: center;
    line-height: 46px;
    color: #fff;
    font-family: "hanyi";
    font-size: 24px;
  }
  .general-time {
    padding: 12px 16px 12px 0;
    text-align: right;
    color: #43abe9;
    font-size: 14px;
  }
  .general-detail {
    padding: 0 16px 15px;
    .service-wrap {
      position: relative;
      width: 100%;
      height: 284px;
      overflow-y: hidden;
    }

    .service_real > div,
    .head-detail {
      display: flex;
      flex-shrink: 0;
      height: 48px;
      line-height: 48px;
      justify-content: space-between;
      align-items: center;
      font-size: 18px;
      color: #ffffff;
      padding: 0 12px;
      text-align: center;
      &.active {
        background: rgba(0, 186, 255, 0.2);
      }
      &:last-child {
        margin-bottom: 0;
      }
      > div {
        &:nth-child(1) {
          width: 30%;
        }
        &:nth-child(2) {
          width: 30%;
        }
        &:nth-child(3) {
          width: 45%;
        }
      }
    }
    .head-detail {
      font-weight: bold;
      color: #032476;
      background: #00cfff;
    }
  }
  .left_menu {
    width: 18px;
    position: absolute;
    top: 145px;
    left: 324px;
    z-index: 12;
    transition: all 1s ease;
  }
}
.mask {
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  z-index: 3000;

  .report-dialog {
    position: absolute;
    left: 50%;
    top: 50%;
    padding: 40px 20px 20px 20px;
    transform: translate(-50%, -50%);
    background-color: #fff;
    &.full-screen {
      width: 98%;
      height: 94%;
    }
  }
  .report-dialog-close {
    position: absolute;
    top: -16px;
    right: 19px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    color: #fff;
    z-index: 3001;
    cursor: pointer;
  }

  .dialog-icon-group {
    display: flex;
    position: absolute;
    right: 0;
    top: 0;
    padding: 10px 20px;
  }
  .dialog-icon {
    width: 20px;
    height: 20px;
    background-size: 100% 100%;
    cursor: pointer;
  }
  .dialog-zoom-in {
    background: url($imageUrl + "maxscreen.png") no-repeat center;
    background-size: 100%;
  }
  .dialog-zoom-out {
    background: url($imageUrl + "minscreen.png") no-repeat center;
    background-size: 100%;
  }
  .dialog-close {
    background: url($imageUrl + "closeImg.png") no-repeat center;
    background-size: 100%;
  }
  .dialog-icon + .dialog-icon {
    margin-left: 20px;
  }
  .report-dialog-close:before {
    box-sizing: border-box;
    position: absolute;
    left: -21px;
    top: -44px;
    content: "";
    width: 60px;
    height: 60px;
    border-top: 0;
    border-bottom: 20px solid rgba(0, 103, 223, 0.42);
    border-left: 2px solid transparent;
    border-right: 3px solid transparent;
  }
  .report-dialog-inner {
    width: 152vh;
    height: 85.5vh;
    &.full-screen {
      width: 100%;
      height: 100%;
    }
    .ppt-carousel {
      height: 100%;
      img {
        width: 100%;
        height: 100%;
        &.full-box {
          width: 100%;
          height: 100%;
        }
      }
    }
  }
}
.screen_wrap {
  background: url("../../../assets/images/page_bg.png") no-repeat center;
  background-size: 100% 100%;
  position: relative;
}
.seawall_right_back {
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  width: 500px;
  height: 100%;
  z-index: 1;
}
.fangan_iframe {
  width: 100%;
  height: 600px;
}
.screen_title {
  user-select: none;
  width: 100%;
  position: absolute;
  top: -2px;
  left: 0;
  text-align: center;
  z-index: 12;
  .screen_title_box {
    letter-spacing: 3px;
    width: 100%;
    height: 103px;
    background: url("../../../assets/images/screen_title_bg3.png") no-repeat
      center;
    background-size: 100% 100%;
    background-position-x: -6px;
    color: #ffffff;
    font-size: 56px;
    font-weight: 600;
    display: flex;
    font-family: youshe;
    align-items: center;
    justify-content: flex-start;
    // padding-left: 30px;
    // .screen_title_box_left {
    //   width: 654px;
    //   height: 100%;
    //   background: url("../../../assets/images/screen_title_bg2.png") no-repeat
    //     center;
    //   background-size: 100% 100%;
    // }
    .title {
      line-height: 72px;
      font-family: "Yahei";
      font-size: 46px;
      font-weight: 600;
    }
    img.logo {
      display: inline-block;
      width: 36px;
      height: 45px;
      margin-right: 36px;
      margin-left: 30px;
    }
    .top-right-icon {
      position: absolute;
      left: 535px;
      top: 38px;
      width: 28px;
      height: 28px;
      margin-right: 0;
      margin-left: 10px;
      background: url($imageUrl + "outCircle.png") no-repeat center;
      background-size: 120% 120%;
      background-position: 50%;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-left: 10px;
      cursor: pointer;
    }
    .ht_tabs {
      position: absolute;
      left: 40%;
      top: 10px;
      // width: 680px;
      height: 50px;
      display: flex;
      align-items: center;
      justify-content: center;
      .tabs_item {
        width: 180px;
        height: 30px;
        padding: 13px 0;
        cursor: pointer;
        // margin-right: 10px;
        margin-left: -6px;
        color: rgba(255, 255, 255, 0.5);
        // background: url("../../../assets/images/ht-tabs.png") no-repeat center;
        // background-size: 100% 100%;
        &.active {
          background: url("../../../assets/images/ht-tabs-active1.png")
            no-repeat center;
          background-size: 100% 100%;
          color: #1efdff;
          font-size: 32px;
        }
        &.haveTips {
          cursor: not-allowed;
        }

        &.haveTips::after {
          content: attr(tips);
          padding: 5px;
          background-color: #b4a078;
          box-shadow: 0 0 5px #b4a078;
          color: white;
          min-width: 150px;
          max-width: 100%;
          font-size: 16px;
          text-align: center;
          border-radius: 5px;
          position: absolute;
          bottom: -100%;
          left: 40%;
          transform: translate(-50%, -10px);
          opacity: 0;
          transition: all 0.5s;
        }

        &.haveTips:hover::after {
          opacity: 1;
        }

        &.haveTips:hover::before {
          opacity: 1;
        }
        p {
          line-height: 1;
          font-size: 27px;
          font-family: hanyi;
        }
      }
    }
    .time {
      font-family: "Microsoft YaHei";
      font-size: 30px;
      position: absolute;
      top: 18px;
      right: 8.6%;
      font-weight: 400;
    }
    .person {
      position: absolute;
      right: 20px;
      top: 10px;
      height: 36px;
      background: #204cc0;
      box-sizing: border-box;
      border-radius: 18px;
      border: 2px solid #5a89f1;
      margin-top: 5px;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);
      .person_left {
        box-sizing: border-box;
        height: 36px;
        cursor: pointer;
        width: 120px;
        text-align: center;
        border-right: 1px solid #5c8ddc;
        span {
          line-height: 36px;
          font-size: 20px;
        }
      }
      .person_right {
        width: 120px;
        box-sizing: border-box;
        height: 36px;
        @include flexbox();
        @include flexAI(center);
        @include flexJC(center);
        img {
          width: 16px;
          height: 16px;
          margin-right: 5px;
        }
        span {
          line-height: 36px;
          font-size: 20px;
          font-family: "Microsoft YaHei";
          font-weight: bold;
        }
      }
    }
  }

  .haveTF {
    position: absolute;
    right: 490px;
    top: 8px;
    width: 68px;
    height: 68px;
    cursor: pointer;
  }
  .noTF {
    position: absolute;
    right: 500px;
    top: 12px;
    width: 62px;
    height: 62px;
  }
}
.seawall_dialog_iframe {
  height: 100%;
  width: 100%;
}
.dialog-content {
  height: 700px;
  padding-bottom: 20px;
  box-sizing: border-box;
  // height: 80vh;
  ::v-deep .primary-dialog {
    .el-dialog__body {
      padding: 0;
    }
  }
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
.right_box {
  position: fixed;
  z-index: 17;
  top: 70px;
  bottom: 0px;
  width: 500px;
  height: 100%;
  @include transition(all, 0.3s, ease);
  right: 0px;

  // padding: 0px 0px 0px 20px;
  .map_left {
    z-index: 1002;
  }
  .drawer_right_btn {
    width: 24px;
    height: 68px;
    position: absolute;
    left: -22px;
    top: 50%;
    margin-top: -38px;
    cursor: pointer;
  }
  .right_select {
    position: absolute;
    top: 85px;
    left: -178px;
    width: 158px;
    // height: 383px;
    background: #fff;
    box-shadow: 0 1px 1px 0 rgba(0, 0, 0, 0.06), 0 0 1px 0 rgba(0, 0, 0, 0.06),
      0 2px 4px 1px rgba(0, 0, 0, 0.02);
    .right_select_item {
      text-align: center;
      padding: 16px 0 11px 0;
      cursor: pointer;
      box-sizing: border-box;
      & > p {
        font-size: 14px;
        color: #8c8c8c;
        margin-bottom: 10px;
      }
      & > span {
        & > span {
          font-size: 24px;
          color: #000000;
          font-weight: 500;
          .zuo {
            font-size: 16px;
            font-weight: 500;
          }
        }
      }
      &:hover {
        background: rgba(25, 59, 196, 0.15);
        @include transition(all, 0.5s, ease);
      }
      &.active {
        background: rgba(25, 59, 196, 0.15);
        border: 1px solid #193bc4;
        p {
          color: #193cc5;
        }
        span {
          color: #193cc5;
        }
      }
    }
  }
  .map_btns {
    position: absolute;
    bottom: 16px;
    left: -208px;
    width: 200px;
    @include flexbox();
    @include flexJC(space-between);
    @include flexAI(flex-end);
    .map_btns_ztjb {
      background: rgba(0, 0, 0, 0.5);
      color: #fff;
      border-radius: 2px;
      width: 104px;
      height: 32px;
      text-align: center;
      font-size: 14px;
      line-height: 32px;
      cursor: pointer;
      i {
        font-size: 18px;
        vertical-align: text-top;
        margin-right: 4px;
      }
    }
    .map_btns_tool {
      width: 104px;
      .tool_title {
        background: rgba(0, 0, 0, 0.5);
        color: #fff;
        height: 32px;
        font-size: 14px;
        box-sizing: border-box;
        width: 100%;
        padding: 0 8px;
        cursor: pointer;
        @include flexbox();
        @include flexJC(space-between);
        @include flexAC();
        .el-icon-suitcase-1 {
          font-size: 18px;
        }
      }
      .tool_list {
        .tool_item {
          cursor: pointer;
          background: rgba(0, 0, 0, 0.8);
          color: #fff;
          height: 32px;
          line-height: 32px;
          font-size: 14px;
          text-align: center;
          &:hover {
            background: rgba(0, 0, 0, 0.65);
          }
        }
      }
    }
    .map_btns_zoom {
      & > div {
        cursor: pointer;
        display: inline-block;
        width: 32px;
        height: 32px;
        background: rgba(0, 0, 0, 0.5);
        color: #fff;
        font-size: 18px;
        text-align: center;
        line-height: 32px;
        margin-right: 8px;
      }
    }
  }
}

.seawall-right {
  height: calc(100% - 72px);
  // background: url("../../../assets/images/right-bg_pic.png") no-repeat center;
  // background-size: 100% 100%;
  // overflow: hidden;
  position: relative;

  // &::before {
  //   content: "";
  //   position: absolute;
  //   top: 0;
  //   left: 0;
  //   width: 600px;
  //   height: 300px;
  //   filter: blur(5px);
  //   z-index: -1;
  //   background-size: cover;
  // }
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
.fangan_dialog {
  z-index: 999;
  overflow: auto;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 10+ */
  &::-webkit-scrollbar {
    display: none;
  }
  width: 100%;
  img.img-small {
    width: 100%;
  }
  &.fullscreen-box {
    .img-big {
      display: block;
      margin: 0 auto;
      width: 80%;
    }
  }
}
</style>
<style lang="scss">
.switich_tab .el-tabs__nav-wrap {
  padding: 0 16px;
}
.switich_tab .el-tabs__nav-scroll {
  width: 100%;
}
.switich_tab .el-tabs__nav {
  width: 100%;
}
.switich_tab .el-tabs__item.is-active {
  color: #193bc4;
}
.switich_tab .el-tabs__active-bar {
  background-color: #193bc4;
  width: 25% !important;
}
.switich_tab .el-tabs__item {
  font-size: 20px;
  cursor: pointer;
}
.switich_tab .el-tabs__item:hover {
  color: #193bc4a1;
  cursor: pointer;
}
.switich_tab .el-tabs__header {
  margin-bottom: 6px;
}
.switich_tab .el-tabs__item {
  font-size: 18px;
  font-weight: bold;
  width: 25%;
  text-align: center;
  height: 50px;
  line-height: 50px;
  padding: 0;
}
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
.seawall_dialog .el-dialog__header {
  padding: 16px;
  background: #2478f0;
  .el-dialog__title {
    color: #fff;
  }
  .el-dialog__close {
    color: #fff;
  }
}
.seawall_dialog .el-dialog__body {
  height: calc(100% - 120px);
  overflow: hidden;
}
.fangan_dialog .el-dialog__header {
  padding-bottom: 0;
}
.fangan_dialog .el-dialog__body {
  padding-top: 0;
}
</style>