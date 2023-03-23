<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-02 15:47:22
 * @Description: 
 * @FilePath: /rscp-big-screen/src/views/bigScreen/basin/BasinScreen.vue
-->
<template>
  <div class="screen_wrap">
    <section class="drawer_left" :class="{ showLeft: drawerShowLeft }">
      <basin-left></basin-left>
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
      <div class="map_legend" v-if="rightShowLegend">
        <div class="map_legend_title" >
          <div class="map_legend_title_left" @click="showLegend = !showLegend">
            <img src="../../../assets/images/legend_icon.png" alt="">
            <span>图例</span>
          </div>
          <div class="map_legend_title_right">
            <i v-show="!showLegend" @click="showLegend = !showLegend" class="el-icon-caret-top"></i>
            <i v-show="showLegend" @click="showLegend = !showLegend" class="el-icon-caret-bottom"></i>
          </div>
        </div>
        <transition name="fade">
          <div v-show="showLegend" class="map_legend_list">
            <div class="map_legend_item" v-for="(item, index) in legendList" :key="index">
              <p :style="{backgroundColor: item.color}"></p>
              <span>{{item.name}}</span>
            </div>
          </div>
        </transition>
      </div>
    </section>
    <basin-map ref="reservoirMap"></basin-map>
    <section class="drawer_right" :class="{ showRight: drawerShowRight }">
      <!-- <keep-alive> -->
      <basin-right-reservoir v-if="rightIndex === 0"></basin-right-reservoir>
      <basin-right-dike v-if="rightIndex === 1"></basin-right-dike>
      <basin-right-sluice v-if="rightIndex === 2"></basin-right-sluice>
      <basin-right-pump v-if="rightIndex === 3"></basin-right-pump>
      <basin-right-gate v-if="rightIndex === 4"></basin-right-gate>
      
      <reservoir-right-xsl v-if="rightIndex === 5"></reservoir-right-xsl>
      <reservoir-right-kgkn v-if="rightIndex === 6"></reservoir-right-kgkn>
      <reservoir-right-cwyj v-if="rightIndex === 7" @openMassageDialog="openMassageDialog" :rightSelectList2.sync="rightSelectList2"></reservoir-right-cwyj>
      <!-- </keep-alive> -->
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
        class="drawer_right_home"
        @click="goHome()"
        src="../../../assets/images/home.png"
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
        v-if="isShowHideLayer"
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
        <div class="right_select_title" v-if="rightIndex == 0 || rightIndex > 4">
          <img src="../../../assets/images/title_yj.png" alt="" />
          运行研判
        </div>
        <div v-if="rightIndex == 0 || rightIndex > 4">
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
        </div>
        
        <div class="right_select_close" @click="showHideLayer()">
          收起
          <img src="../../../assets/images/up.png" alt="" />
        </div>
      </div>
      <div :class="['map_btns', provincialLevel ? 'map_btns1' : 'map_btns2']">
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
import BasinMap from "./basinMap/BasinMap";
import BasinLeft from "./basinLeft/BasinLeft";
import BasinRightReservoir from "./basinRight/BasinRightReservoir";
import BasinRightDike from "./basinRight/BasinRightDike";
import BasinRightSluice from "./basinRight/BasinRightSluice";
import BasinRightPump from "./basinRight/BasinRightPump";
import BasinRightGate from "./basinRight/BasinRightGate";

import PreviewPhoto from "@/components/PreviewPhoto";
import PreviewVideo from "@/components/PreviewVideo";
import PreviewCamera from "@/components/PreviewCamera";

// 水库组件
import ReservoirRightXsl from "../reservoir/reservoirRight/ReservoirRightXsl";
import ReservoirRightKgkn from "../reservoir/reservoirRight/ReservoirRightKgkn";
import ReservoirRightCwyj from "../reservoir/reservoirRight/ReservoirRightCwyj";

import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
import { basinStatisticApi } from "@/api/api_basin";

export default {
  name: "BasinScreen",
  props: {},
  components: {
    BasinMap,
    BasinLeft,
    BasinRightReservoir,
    BasinRightDike,
    BasinRightSluice,
    BasinRightPump,
    BasinRightGate,
    PreviewPhoto,
    PreviewVideo,
    PreviewCamera,
    // 水库组件
    ReservoirRightXsl,
    ReservoirRightKgkn,
    ReservoirRightCwyj
  },
  computed: {
    basinSelectFlag() {
      return this.$store.state.basinSelectFlag;
    },
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectBasin() {
      return this.$store.state.leftSelectBasin;
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
    leftSelectBasin(val) {
      // this.setOptions("scale", val);
      this.getRightSelectList();
    },
  },
  data() {
    return {
      rightShowLegend: false,
      drawerShowLeft: true,
      drawerShowRight: true,
      rightIndex: 0,
      rightSelectList1: [
        {
          name: "水库",
          num: 0,
          unit: "座",
        },
        {
          name: "堤防",
          num: 0,
          unit: "条",
        },
        {
          name: "水闸",
          num: 0,
          unit: "座",
        },
        {
          name: "泵站",
          num: 0,
          unit: "座",
        },
        {
          name: "闸站",
          num: 0,
          unit: "座",
        }
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
        {
          name: "预估超限",
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
      showLegend: true,
      legendList: [
        {
          name: '一级堤防',
          color: '#E02020'
        },
        {
          name: '二级堤防',
          color: '#EB7E65'
        },
        {
          name: '三级堤防',
          color: '#F7C739'
        },
        {
          name: '四级堤防',
          color: '#193CC5'
        },
        {
          name: '五级堤防',
          color: '#1BC57E'
        }
      ],
      showTool: false,
      
      opt: {
        statistic: "mapTabs",
        basin: '',
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
    };
  },
  mounted() {
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
      this.opt.basin = this.leftSelectBasin
      let res = await reservoirLeftStatisticApi(this.opt);
      let resTop = await basinStatisticApi(this.opt);
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
          },
          {
            name: "预估超限",
            num: res.data[0].forecast ? res.data[0].forecast : 0,
            // num: 0,
            unit: "座",
          },
        ];
      }
      if (resTop.success){
        this.rightSelectList1 = [
          {
            name: "水库",
            num: resTop.data[0].num,
            unit: "座",
          },
          {
            name: "堤防",
            num: resTop.data[1].num,
            unit: "条",
          },
          {
            name: "水闸",
            num: resTop.data[2].num,
            unit: "座",
          },
          {
            name: "泵站",
            num: resTop.data[3].num,
            unit: "座",
          },
          {
            name: "闸站",
            num: resTop.data[4].num,
            unit: "座",
          }
        ];
      }
      console.log(resTop,'resTop');
      this.selectLoading = false;
    },
    rightSelectClick(index) {
      // if(this.basinSelectFlag && index != 1){
      if (this.basinSelectFlag) {
        this.rightIndex = index;
        if(this.rightIndex == 1){ //堤防显示图例
          this.rightShowLegend = true
        }else{
          this.rightShowLegend = false
        }
      }
    },
    open(type) {
      this["drawerShow" + type] = true;
    },
    close(type) {
      this["drawerShow" + type] = false;
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


    // 返回首页
    goHome() {
      this.$store.commit("SET_LEFT_SELECT_BASIN", "");
      this.$router.push("/menu");
    },
  },
};
</script>
<style lang="scss" scoped>
.drawer_left {
  width: 380px;
  left: -381px;
}
.showLeft {
  left: 0;
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
      @include flexJC(center)
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
  width: 200px !important;
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
