<!--
 * @Date: 2021-12-23 14:55:58
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2021-12-24 09:51:08
 * @FilePath: \rscp-big-screen\src\views\sgzgScreenNew\zhekuHealthy\ZhekuHealthyScreen.vue
-->
<template>
  <div class="screen_wrap">
    <section class="drawer_left" :class="{ showLeft: drawerShowLeft,hasTop: hasTop}">
      <zheku-healthy-left></zheku-healthy-left>
      <img class="drawer_left_btn" v-show="drawerShowLeft" @click="close('Left')" src="../../../assets/images/drawer_left1.png" alt="" />
      <img class="drawer_left_btn" v-show="!drawerShowLeft" @click="open('Left')" src="../../../assets/images/drawer_left2.png" alt="" />
    </section>
    <main-map ref="reservoirMap"></main-map>
    <section class="drawer_right" :class="{ showRight: drawerShowRight,hasTop: hasTop  }">
      <zheku-healthy-right v-if="rightIndex === 0"></zheku-healthy-right>
      <img class="drawer_right_btn" v-show="drawerShowRight" @click="close('Right')" src="../../../assets/images/drawer_right1.png" alt="" />
      <img class="drawer_right_btn" v-show="!drawerShowRight" @click="open('Right')" src="../../../assets/images/drawer_right2.png" alt="" />
    </section>
    <!-- 弹出层 -->
    <preview-photo v-if="showLookPhotosPopup" :loading="lookPhotosLoading" :photoList="photosList" :reservoirName="reservoirName" @closeLookPhotosPopup="closeLookPhotosPopup"></preview-photo>
    <preview-video v-if="showLookVideoPopup" :loading="lookVideoLoading" :videoUrl="videoUrl" @closeLookVideoPopup="closeLookVideoPopup"></preview-video>
    <preview-camera v-if="showLookCameraPopup" :prcd="prcd" :name="skName" @closeLookCameraPopup="closeLookCameraPopup"></preview-camera>
    <preview-brief v-if="showLookBriefPopup" @closeLookBriefPopup="closeLookBriefPopup"></preview-brief>
    <preview-message v-if="showLookMsgPopup" @closeLookMessagePopup="closeLookMessagePopup"></preview-message>
    <div class="reservoir_dialog" v-show="showDetailDialog">
      <div class="reservoir_title">
        <img @click="showDetailDialog = false" src="../../../assets/images/back2.png" alt="" />
        <p>{{ projectTitle }}</p>
      </div>
      <iframe class="reservoir_dialog_iframe" :src="detailUrl" frameborder="0"></iframe>
    </div>
  </div>
</template>
<script>
import MainMap from "../components/MainMap";
import zhekuHealthyRight from "./zhekuHealthyRight/zhekuHealthyRight";
import PreviewPhoto from "@/components/PreviewPhoto";
import PreviewVideo from "@/components/PreviewVideo";
import PreviewCamera from "@/components/PreviewCamera";
import PreviewBrief from "@/components/PreviewBrief";
import PreviewMessage from "@/components/PreviewMessage";
import eventBus from "@/utils/eventBus";
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
import ZhekuHealthyLeft from './zhekuHealthyLeft/zhekuHealthyLeft.vue';
import { getQueryString } from '@/utils/utils'
export default {
  name: "HealthyOnlineScreen",
  props: {},
  components: {
    MainMap,
    ZhekuHealthyLeft,
    zhekuHealthyRight,
    PreviewPhoto,
    PreviewVideo,
    PreviewCamera,
    PreviewBrief,
    PreviewMessage
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
      hasTop: true,
      drawerShowLeft: true,
      drawerShowRight: true,
      rightIndex: 0,
      rightSelectList1: [
        {
          name: "全部水库",
          num: 0,
          unit: "座",
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
      showLookBriefPopup: false,
      showLegend: true,
      showTool: false,
      jhFlag: true,
      legendList: [
        {
          name: "空库运行",
          color: "#1341c5",
        },
        {
          name: "超汛限水位",
          color: "#f8b400",
        },
        {
          name: "超正常水位",
          color: "#fb6e13",
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
      isShowHideLayer: true,
      projectTitle: "详情",
      prcd: "",
      skName: "",
      showLookCameraPopup: false,
      provincialLevel: false,
    };
  },
  mounted() {
    //http://192.168.2.212:8080/screen?isSgzg=1&hasTop=1&type=zhekuHealthy
    if (getQueryString('hasTop') == '1') {
      this.hasTop = true
    } else {
      this.hasTop = false
    }
    if (this.$localData("get", "userInfo").adcd == "330000000000") {
      this.provincialLevel = true;
    }
    eventBus.$on("custom-event", (msg) => {
      this.rightSelectClick(msg);
    }); // 当 ChildComponent 被挂载，控制台中将显示一条消息

    this.rightSelectClick(0);
    this.getRightSelectList();
    if (this.$localData("get", "userInfo").adcd == "330000000000") {
      this.provincialLevel = true;
    }
  },
  methods: {
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
        kgs = res.data[0].capable
          ? (res.data[0].capable / 10000).toFixed(0)
          : 0;
        knx = res.data[0].addtion
          ? (res.data[0].addtion / 10000).toFixed(0)
          : 0;
        this.rightSelectList1 = [
          {
            name: "全部水库",
            num: res.data[0].totalCnt ? res.data[0].totalCnt : 0,
            unit: "座",
          },
        ];

        this.$store.commit("SET_RESERVOIR_TABS", [
          ...this.rightSelectList1,
        ]);
      }
      this.selectLoading = false;
    },
    rightSelectClick(index) {
      // if(this.reservoirSelectFlag && index != 1){
      if (index === 20) {
        this.legendList = [
          {
            name: "超防洪高水位",
            color: "#fb6e13",
          },

          {
            name: "超正常水位",
            color: "#f8b400",
          },
          {
            name: "超汛限水位",
            color: "#1341c5",
          },
          {
            name: "低于汛限水位",
            color: "#59be56",
          },
        ];
      }
      if (this.reservoirSelectFlag) {
        this.rightIndex = index;
        switch (index) {
          case 0:
            this.legendList = [
              {
                name: "超正常水位",
                color: "#fb6e13",
              },
              {
                name: "超汛限水位",
                color: "#f8b400",
              },
              {
                name: "空库运行",
                color: "#1341c5",
              },
            ];
            break;
          case 1:
            this.legendList = [
              {
                name: "超防洪高水位",
                color: "#fb6e13",
              },
              {
                name: "超汛限水位",
                color: "#f8b400",
              },
              {
                name: "低于汛限水位",
                color: "#1341c5",
              },
            ];
            break;
          case 3:
            this.legendList = [
              {
                name: "超防洪高水位",
                color: "#fb6e13",
              },

              {
                name: "超正常水位",
                color: "#f8b400",
              },
              {
                name: "超汛限水位",
                color: "#1341c5",
              },
            ];
            break;
          case 2:
            this.legendList = [
              {
                name: "超正常水位",
                color: "#fb6e13",
              },
              {
                name: "超汛限水位",
                color: "#f8b400",
              },
              {
                name: "空库运行",
                color: "#1341c5",
              },
            ];
            break;
          case 4:
            this.legendList = [
              {
                name: "超防洪高水位",
                color: "#fb6e13",
              },

              {
                name: "超正常水位",
                color: "#f8b400",
              },
              {
                name: "超汛限水位",
                color: "#1341c5",
              },
            ];
            break;
          case 20:
            this.legendList = [
              {
                name: "超防洪高水位",
                color: "#fb6e13",
              },

              {
                name: "超正常水位",
                color: "#f8b400",
              },
              {
                name: "超汛限水位",
                color: "#1341c5",
              },
              {
                name: "低于汛限水位",
                color: "#59be56",
              },
            ];
            break;
        }
      } else {
        return false;
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
.drawer_left {
  width: 484px;
  left: -485px;
  &.hasTop {
    top: 126px !important;
    height: calc(100% - 126px);
  }
}
.drawer_right {
  width: 484px;
  right: -485px;
  &.hasTop {
    top: 126px !important;
    height: calc(100% - 126px);
  }
}
.showLeft {
  left: 0;
}
.showRight {
  right: 0;
}
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
