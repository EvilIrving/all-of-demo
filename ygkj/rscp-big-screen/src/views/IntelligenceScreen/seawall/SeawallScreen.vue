
<template>
  <div class="screen_wrap">
    <section class="drawer_left" :class="{ showLeft: drawerShowLeft }">
      <el-tabs
        ref="switich_tab"
        class="switich_tab"
        v-model="activeTab"
        @tab-click="handleClick"
      >
        <el-tab-pane label="海塘概况" name="first"></el-tab-pane>
        <el-tab-pane label="潮位预报" name="second"></el-tab-pane>
        <el-tab-pane label="风险研判" name="third"></el-tab-pane>
        <el-tab-pane label="闭环管控" name="four"></el-tab-pane>
      </el-tabs>
    </section>

    <seawall-map ref="seawallMap"></seawall-map>
    <section class="drawer_right" :class="{ showRight: drawerShowRight }">
      <seawall-screen-legend :sgzgMapType="sgzgMapType" />

      <div class="seawall-right">
        <seawall-left v-if="activeTab === 'first'"></seawall-left>
        <seawall-right-typhoon
          v-if="activeTab === 'second'"
        ></seawall-right-typhoon>
        <seawall-right-sscw v-if="activeTab === 'third'"></seawall-right-sscw>
        <seawall-right-aljs v-if="activeTab === 'four'"></seawall-right-aljs>
      </div>

      <!-- <img class="drawer_right_home" @click="goHome()" src="../../../assets/images/home.png" alt=""> -->
      <!-- <img
        class="_open"
        v-if="!isShowHideLayer"
        src="../../../assets/images/right_select.png"
        @click="showHideLayer()"
      /> -->
      <!-- <div
        class="right_select"
        v-if="isShowHideLayer"
        v-loading="selectLoading"
        v-show="drawerShowRight"
      >
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
          </span>
        </div>

        <div
          class="right_select_item"
          :class="idx + 2 === rightIndex ? 'active' : ''"
          v-for="(ite, idx) in rightSelectList2"
          :key="ite.name"
          @click="rightSelectClick(idx + 2)"
        >
          <p>{{ ite.name }}</p>
          <span>
            <span>
              {{ ite.num ? ite.num : 0 }}
              <i class="zuo">(条)</i>
            </span>
          </span>
        </div>
        <div class="right_select_close" @click="showHideLayer()">
          收起
          <img src="../../../assets/images/up.png" alt="" />
        </div>
      </div> -->
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
      :title="projectName"
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

    <primary-dialog
      :showFooter="false"
      :mainTitle="sgzgMapType"
      :modal="modal"
      :visible="dialogVisible"
      @handleClose="handleClose"
      @changeFullscreen="changeFullscreen"
      :fullscreen="fullscreen"
      width="1400px"
      top="5vh"
    >
      <div class="dialog-content" :class="{ 'fullscreen-box': fullscreen }">
        <generation
          ref="generation"
          :fullscreen="fullscreen"
          v-if="
            dialogVisible &&
            (sgzgMapType == '监测分析' || sgzgMapType == '安全鉴定')
          "
          :dam="dam"
          :sgzgMapType="sgzgMapType"
        />
        <iframe
          class="seawall_dialog_iframe"
          v-else
          :src="exportUrl"
          frameborder="0"
        ></iframe>
      </div>
    </primary-dialog>
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

import { seawallLeftStatisticApi } from "@/api/api_seawall";

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
  },
  computed: {
    seawallSelectFlag() {
      return this.$store.state.seawallSelectFlag;
    },
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd || this.$localData("get", "userInfo").adcd;
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
    activeTab(val) {
      switch (val) {
        case "first":
          this.sgzgMapType = "监测分析";
          break;
        case "second":
          this.exportUrl = this.$config.zhImgUrl + "风险隐患清单.pdf";
          this.sgzgMapType = "风险研判";
          break;
        default:
          this.sgzgMapType = "闭环管控";
          this.exportUrl = this.$config.zhImgUrl + "风险提示单.pdf";
          break;
      }
    },
    leftSelectProjectScale(val) {
      this.setOptions("scale", val);
      this.getRightSelectList();
    },
  },
  data() {
    return {
      exportUrl: "",
      sgzgMapType: "监测分析",
      dialogVisible: false,
      modal: true,
      fullscreen: false,
      activeTab: "second",
      drawerShowLeft: true,
      drawerShowRight: true,
      rightIndex: 1,
      rightSelectList1: [
        {
          name: "监测分析",
          num: 0,
        },
        {
          name: "潮位预报",
          num: 0,
        },
      ],
      rightSelectList2: [
        {
          name: "风险研判",
          num: 0,
        },
        {
          name: "风险管控",
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
      opt: {
        statistic: "mapTabs",
        adcd: this.leftSelectAdcd,
      },
      showFanganDialog: false,
      fanganUrl: "",
      showDetailDialog: false,
      detailUrl: "",
      isShowHideLayer: false,
      projectName: "",
      dam: "",
    };
  },
  mounted() {
    this.getRightSelectList();
  },
  methods: {
    checkSgzgMapType(payload) {
      this.sgzgMapType = payload.name;
      this.dam = payload.type || "";
    },
    handleClick(tab, event) {
      console.log(tab, event);
      this.activeTab = tab.name;
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
      let res = await seawallLeftStatisticApi(this.opt);
      if (res.success) {
        this.rightSelectList1 = [
          {
            name: "监测分析",
            num: 0,
          },
          {
            name: "潮位预报",
            num: 0,
          },
        ];

        this.rightSelectList2 = [
          {
            name: "风险研判",
            num: 0,
          },
          {
            name: "风险管控",
            num: 0,
          },
        ];
      }
      this.selectLoading = false;
    },
    rightSelectClick(index) {
      if (this.seawallSelectFlag) {
        this.rightIndex = index;
      } else {
        return false;
      }
    },
    openGernaretion() {
      this.fullscreen = false;
      this.dialogVisible = true;
    },
    open(type) {
      this["drawerShow" + type] = true;
    },
    close(type) {
      this["drawerShow" + type] = false;
    },
    // /**
    //  * @function:
    //  * @description: 一键生成弹窗
    //  * @param {*} checkedArr
    //  * @param {*} sgzgMapType
    //  * @param {*} workStatus
    //  * @param {*} xzScale  限制运用新增带入 规模参数
    //  * @return {*}
    //  */
    // open(sgzgMapType) {
    //   this.activeTab = sgzgMapType;
    //   this.dialogVisible = true;
    // },
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
      this.projectName = name;
      let token = this.$localData("get", "token");
      name = encodeURI(encodeURI(name));
      this.detailUrl = `https://sk.zjwater.com/cp/skip/reservoirDetailSingle?prcd=${prcd}&name=${name}&fromMap=&pName=&cName=&token=${token}`;
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
.seawall-right {
  height: 100%;
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