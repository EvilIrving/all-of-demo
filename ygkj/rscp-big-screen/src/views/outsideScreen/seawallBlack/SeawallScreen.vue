<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-15 10:09:04
 * @Description: 
 * @FilePath: /rscp-big-screen/src/views/bigScreen/seawallBlack/SeawallScreen.vue
-->
<template>
  <div class="screen_wrap">
    <section class="drawer_left" :class="{ showLeft: drawerShowLeft }">
      <seawall-left></seawall-left>
      <img
        class="drawer_left_btn"
        v-show="drawerShowLeft"
        @click="close('Left')"
        src="../../../assets/images/black_left1.png"
        alt=""
      />
      <img
        class="drawer_left_btn"
        v-show="!drawerShowLeft"
        @click="open('Left')"
        src="../../../assets/images/black_left2.png"
        alt=""
      />
      <div class="map_legend">
        <div class="map_legend_title">
          <div class="chart_title_black" @click="showLegend = !showLegend">
            <!-- <img src="../../../assets/images/legend_icon.png" alt="" /> -->
            <span>图例</span>
          </div>
          <div class="map_legend_title_right">
            <span>图标聚合</span>
            <el-switch
              @click.stop
              @change="changeSwitch"
              v-model="jhFlag"
              active-color="#193CC5"
              inactive-color="#999999"
            >
            </el-switch>
            <i
              v-show="!showLegend"
              @click="showLegend = !showLegend"
              class="el-icon-caret-top"
            ></i>
            <i
              v-show="showLegend"
              @click="showLegend = !showLegend"
              class="el-icon-caret-bottom"
            ></i>
          </div>
        </div>
        <transition name="fade">
          <div v-show="showLegend" class="map_legend_list">
            <div
              class="map_legend_item"
              v-for="(item, index) in legendList"
              :key="index"
            >
              <p :style="{ backgroundColor: item.color }"></p>
              <span>{{ item.name }}</span>
            </div>
          </div>
        </transition>
      </div>
    </section>
    <seawall-map ref="seawallMap"></seawall-map>
    <section class="drawer_right" :class="{ showRight: drawerShowRight }">
      <!-- <keep-alive> -->
      <seawall-right-default v-if="rightIndex === 0"></seawall-right-default>
      <seawall-right-aljs v-if="rightIndex === 1"></seawall-right-aljs>
      <!-- <seawall-right-tflj v-if="rightIndex === 3"></seawall-right-tflj> -->
      <!-- <seawall-right-photo ref="seawallRightPhoto" v-if="rightIndex === 2"></seawall-right-photo> -->
      <seawall-right-video
        ref="seawallRightVideo"
        v-if="rightIndex === 2"
      ></seawall-right-video>
      <seawall-right-sscw v-if="rightIndex === 3"></seawall-right-sscw>
      <seawall-right-cwyj
        v-if="rightIndex === 4"
        @openFanganManage="openFanganManage"
        :rightSelectList2.sync="rightSelectList2"
      ></seawall-right-cwyj>

      <!-- </keep-alive> -->
      <img
        class="drawer_right_btn"
        v-show="drawerShowRight"
        @click="close('Right')"
        src="../../../assets/images/black_right1.png"
        alt=""
      />
      <img
        class="drawer_right_btn"
        v-show="!drawerShowRight"
        @click="open('Right')"
        src="../../../assets/images/black_right2.png"
        alt=""
      />
      <!-- <img class="drawer_right_home" @click="goHome()" src="../../../assets/images/home.png" alt=""> -->
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
          <!-- <img src="../../../assets/images/title_ht.png" alt="" /> -->
          海塘浏览
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
              <i class="zuo">(条)</i>
            </span>
            <!-- <span v-else>暂无数据</span> -->
          </span>
        </div>
        <div class="right_select_title">
          <!-- <img src="../../../assets/images/title_yj.png" alt="" /> -->
          海塘预警
        </div>
        <div
          class="right_select_item"
          :class="idx + 3 === rightIndex ? 'active' : ''"
          v-for="(ite, idx) in rightSelectList2"
          :key="ite.name"
          @click="rightSelectClick(idx + 3)"
        >
          <p>{{ ite.name }}</p>
          <span>
            <span>
              {{ ite.num ? ite.num : 0 }}
              <i class="zuo">(条)</i>
            </span>
            <!-- <span v-else>暂无数据</span> -->
          </span>
        </div>
        <div class="right_select_close" @click="showHideLayer()">
          收起
          <!-- <img src="../../../assets/images/up.png" alt="" /> -->
        </div>
      </div>
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
    </section>
    <preview-photo
      v-if="showLookPhotosPopup"
      :loading="lookPhotosLoading"
      :photoList="photosList"
      :seawallName="seawallName"
      @closeLookPhotosPopup="closeLookPhotosPopup"
    ></preview-photo>
    <preview-video
      v-if="showLookVideoPopup"
      :loading="lookVideoLoading"
      :videoUrl="videoUrl"
      @closeLookVideoPopup="closeLookVideoPopup"
    ></preview-video>
    <el-dialog
      title="方案管理"
      custom-class="fangan_dialog"
      :visible.sync="showFanganDialog"
      width="1200px"
    >
      <iframe class="fangan_iframe" :src="fanganUrl" frameborder="0"></iframe>
    </el-dialog>
    <el-dialog
      title="海塘工程详情"
      custom-class="seawall_dialog"
      :visible.sync="showDetailDialog"
      fullscreen
    >
      <iframe
        class="seawall_dialog_iframe"
        :src="detailUrl"
        frameborder="0"
      ></iframe>
    </el-dialog>
  </div>
</template>

<script>
import SeawallMap from "./seawallMap/SeawallMap.vue";
import SeawallLeft from "./seawallLeft/SeawallLeft.vue";
import SeawallRightDefault from "./seawallRight/SeawallRightDefault";
import SeawallRightCwyj from "./seawallRight/SeawallRightCwyj";
import SeawallRightSscw from "./seawallRight/SeawallRightSscw";
import SeawallRightAljs from "./seawallRight/SeawallRightAljs";
import SeawallRightVideo from "./seawallRight/SeawallRightVideo";
import PreviewPhoto from "@/components/blackPopup/PreviewPhoto";
import PreviewVideo from "@/components/blackPopup/PreviewVideo";

import { seawallLeftStatisticApi } from "@/api/api_seawall";

export default {
  name: "SeawallScreen",
  props: {},
  components: {
    SeawallMap,
    SeawallLeft,
    SeawallRightDefault,
    SeawallRightCwyj,
    SeawallRightSscw,
    SeawallRightAljs,
    SeawallRightVideo,
    PreviewPhoto,
    PreviewVideo,
  },
  computed: {
    seawallSelectFlag() {
      return this.$store.state.seawallSelectFlag;
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
      rightSelectList1: [
        {
          name: "海塘分布",
          num: 0,
        },
        {
          name: "安澜建设",
          num: 0,
        },
        // {
        //   name: '台风路径',
        //   num: 0
        // },
        // {
        //   name: '工程照片',
        //   num: 0
        // },
        {
          name: "工程视频",
          num: 0,
        },
      ],
      rightSelectList2: [
        {
          name: "实时报警",
          num: 0,
        },
        {
          name: "预测预警",
          num: 0,
        },
      ],
      selectLoading: true,
      showLookPhotosPopup: false,
      lookPhotosLoading: true,
      photosList: [],
      seawallName: "",
      showLookVideoPopup: false,
      lookVideoLoading: true,
      videoUrl: "",
      showLegend: true,
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
      opt: {
        statistic: "mapTabs",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
      showFanganDialog: false,
      fanganUrl: "",
      showDetailDialog: false,
      detailUrl: "",
      isShowHideLayer: true,
    };
  },
  mounted() {
    this.getRightSelectList();
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
      let res = await seawallLeftStatisticApi(this.opt);
      if (res.success) {
        this.rightSelectList1 = [
          {
            name: "海塘分布",
            num: res.data[0].proCnt,
          },
          {
            name: "安澜建设",
            num: res.data[0].constructionCnt,
          },
          // {
          //   name: '工程照片',
          //   num: res.data[0].photoCnt
          // },
          {
            name: "工程视频",
            num: res.data[0].cameraCnt,
          },
        ];

        this.rightSelectList2 = [
          {
            name: "实时报警",
            num: res.data[0].overCnt,
          },
          {
            name: "预测预警",
            num: res.data[0].forecast,
          },
        ];
        // this.rightSelectList.splice(3,0,{
        //   name: '台风路径',
        //   num: 0
        // })
      }
      this.selectLoading = false;
    },
    rightSelectClick(index) {
      console.log(index, "---");
      if (this.seawallSelectFlag) {
        this.rightIndex = index;
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
        this.$refs.seawallMap.newMapJh(pointArr, type);
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
    openDetailPopup(prcd, name) {
      let token = this.$localData("get", "token");
      name = encodeURI(encodeURI(name));
      this.detailUrl = `https://sk.zjwater.com/cp/skip/reservoirDetailSingle?prcd=${prcd}&name=${name}&fromMap=&pName=&cName=&token=${token}`;
      console.log(this.detailUrl);
      this.showDetailDialog = true;
    },

    // 方案管理
    openFanganManage(url) {
      this.fanganUrl = url;
      console.log(this.fanganUrl);
      this.showFanganDialog = true;
    },

    // 地图右下角操作
    setZoom(type) {
      this.$refs.seawallMap._setZoom(type);
    },
    selectTool(type) {
      this.showTool = false;
      this.$refs.seawallMap.mapToolBar(type);
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
};
</script>
<style lang="scss" scoped>
.fangan_iframe {
  width: 100%;
  height: 600px;
}
.seawall_dialog_iframe {
  height: 100%;
  width: 100%;
}

.drawer_right_btn{
  left: -24px!important;
}
.drawer_left_btn{
  right: -24px!important;
}
.right_select {
  background: rgba($color: #152528, $alpha: 0.8) !important;
  // background: #152528!important;
  border: 1px solid #75e4e2 !important;
  // border-right: 1px solid #75e4e2 !important;
  top: 20px!important;
  left: -188px!important;
  .right_select_item {
    &:hover {
      background: rgb(42, 85, 84) !important;
    }
    p {
      color: #7acdcb !important;
    }
    span {
      color: #7acdcb !important;
    }
    &.active {
      background: #4edad0 !important;
      border: none !important;
      p {
        color: #fff !important;
      }
      span {
        color: #fff !important;
      }
    }
  }
}
.right_select_title {
  @include flexbox();
  @include flexAC();
  padding: 8px 16px;
  color: #798283;
  font-size: 14px;
  line-height: 16px;
  img {
    margin-right: 8px;
  }
}
// .right_select_close {
//   cursor: pointer;
//   @include flexbox();
//   @include flexAC();
//   @include flexJC(center);
//   padding: 8px 0;
//   border-top: 1px solid #d6d6d6;
//   color: #333;
//   background: #f9f9f9;
//   font-size: 14px;
//   line-height: 16px;
//   img {
//     margin-left: 6px;
//   }
// }
.right_select_close {
  border-top: 1px solid rgb(32, 80, 77);
  border-bottom: 1px solid rgb(32, 80, 77);
  cursor: pointer;
  @include flexbox();
  @include flexAC();
  @include flexJC(center);
  padding: 8px 0;
  color: #fff;
  // background: #f9f9f9;
  font-size: 14px;
  line-height: 16px;
  img {
    margin-left: 6px;
  }
}
.drawer_left,
.drawer_right {
  background: rgba($color: #11171b, $alpha: 0.6);
   top: 145px;
  height: calc(100% - 145px)!important;
  // background: rgba(0, 0, 0, 0.6);
  // background: #FF11171B;
}

.drawer_right{
  border-left: none;
}
.drawer_left {
  border-right: none;

  .map_legend {
    .map_legend_title {
      font-size: 20px;
      line-height: 40px;
      height: 40px;
      color: #fff;
      background: linear-gradient(
        270deg,
        rgba(86, 255, 255, 0) 0%,
        rgba(86, 255, 255, 0.24) 100%
      );
      position: relative;
      .chart_title_black {
        font-size: 20px;
        line-height: 40px;
        padding: 0 16px;
        height: 40px;
        color: #fff;
        background: linear-gradient(
          270deg,
          rgba(86, 255, 255, 0) 0%,
          rgba(86, 255, 255, 0.24) 100%
        );
        position: relative;
        .el-switch {
          position: absolute;
          right: 8px;
          top: 3px;
          background: #355c60;
          border-color: #355c60;
          color: #fff;
        }
      }
    }
    background: rgba($color: #11171b, $alpha: 0.6);
    span {
      color: #fff;
    }
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
  top: 20px;
  left: -78px;
  cursor: pointer;
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
