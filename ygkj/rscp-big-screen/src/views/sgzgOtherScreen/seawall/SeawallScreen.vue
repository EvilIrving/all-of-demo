<!--
 * @Author: hanyu
 * @LastEditTime: 2022-09-02 11:30:36
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\sgzgOtherScreen\seawall\SeawallScreen.vue
-->
<template>
  <div class='screen_wrap'>
    <section class="drawer_left" :class="{showLeft: drawerShowLeft}">
      <seawall-left></seawall-left>
    </section>
    <seawall-map ref="seawallMap"></seawall-map>
    <section class="drawer_right" :class="{showRight: drawerShowRight}">
      <seawall-right-default v-if="rightIndex === 0"></seawall-right-default>
      <seawall-right-aljs v-if="rightIndex === 1"></seawall-right-aljs>
      <seawall-right-sscw v-if="rightIndex === 2"></seawall-right-sscw>
      <seawall-right-cwyj v-if="rightIndex === 3" @openFanganManage="openFanganManage" :rightSelectList2.sync="rightSelectList2"></seawall-right-cwyj>
      <img class="right_select_open" v-if="!isShowHideLayer" :src="$config.swImgUrl + 'expand-icon.png'" @click="showHideLayer()">
      <div class="right_select" v-if="isShowHideLayer" v-loading="selectLoading" element-loading-background="rgba(0, 0, 0, 0.2)" v-show="drawerShowRight">
        <div class="right_select_title">
          <img src="@/assets/images/point_icon.png" alt="">
          海塘浏览
        </div>
        <div class="right_select_item" :class="index === rightIndex ? 'active' :  ''" v-for="(item, index) in rightSelectList1" :key="index" @click="rightSelectClick(index)">
          <p>{{item.name}}</p>
          <span>
            <span>
              {{item.num ? item.num : 0}}
              <i class="zuo">(条)</i>
            </span>
            <!-- <span v-else>暂无数据</span> -->
          </span>
        </div>
        <div class="right_select_title">
          <img src="@/assets/images/point_icon.png" alt="">
          海塘预警
        </div>
        <div class="right_select_item" :class="idx + 2 === rightIndex ? 'active' :  ''" v-for="(ite, idx) in rightSelectList2" :key="ite.name" @click="rightSelectClick(idx + 2)">
          <p>{{ite.name}}</p>
          <span>
            <span>
              {{ite.num ? ite.num : 0}}
              <i class="zuo">(条)</i>
            </span>
            <!-- <span v-else>暂无数据</span> -->
          </span>
        </div>
        <div class="right_select_close" @click="showHideLayer()">
          收起
          <img src="@/assets/images/upbtn.png" alt="">
        </div>
      </div>
      <div class="map_legend">
        <div class="map_legend_title">
          <div class="map_legend_title_left">
            <span>图例</span>
          </div>
          <div class="map_legend_title_right">
            <img src="@/assets/images/upbtn.png" :class="{flip: showLegend}" @click="showLegend = !showLegend" alt="">
          </div>
        </div>
        <div v-show="showLegend" class="map_legend_line"></div>
        <transition name="fade">
          <div v-show="showLegend">
            <div class="map_legend_list">
              <div class="map_legend_item" v-for="(item, index) in legendList" :key="index">
                <p :style="{backgroundColor: item.color}"></p>
                <span>{{item.name}}</span>
              </div>
            </div>
            <div class="switch-box">
              <el-switch style="margin: 10px 18px 17px 6px;height: 16px;" @click.stop @change="changeSwitch" v-model="jhFlag" :width="28" active-color="#46FDFF" inactive-color="#999999"></el-switch>
              <span>图标聚合</span>
            </div>
          </div>
        </transition>
      </div>
    </section>
    <preview-photo v-if="showLookPhotosPopup" :loading="lookPhotosLoading" :photoList="photosList" :seawallName="seawallName" @closeLookPhotosPopup="closeLookPhotosPopup"></preview-photo>
    <preview-video v-if="showLookVideoPopup" :loading="lookVideoLoading" :videoUrl="videoUrl" @closeLookVideoPopup="closeLookVideoPopup"></preview-video>
    <el-dialog title="方案管理" custom-class="fangan_dialog" :visible.sync="showFanganDialog" width="1200px">
      <iframe class="fangan_iframe" :src="fanganUrl" frameborder="0"></iframe>
    </el-dialog>
    <el-dialog title="海塘工程详情" custom-class="seawall_dialog" :visible.sync="showDetailDialog" fullscreen>
      <iframe class="seawall_dialog_iframe" :src="detailUrl" frameborder="0"></iframe>
    </el-dialog>
  </div>
</template>

<script>
import SeawallMap from "./seawallMap/SeawallMap";
import SeawallLeft from "./seawallLeft/SeawallLeft";
import SeawallRightDefault from "./seawallRight/SeawallRightDefault";
import SeawallRightCwyj from "./seawallRight/SeawallRightCwyj";
import SeawallRightSscw from "./seawallRight/SeawallRightSscw";
import SeawallRightAljs from "./seawallRight/SeawallRightAljs";
import PreviewPhoto from "@/components/PreviewPhoto";
import PreviewVideo from "@/components/PreviewVideo";

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
    PreviewPhoto,
    PreviewVideo
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
    }
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
    }
  },
  data() {
    return {
      drawerShowLeft: true,
      drawerShowRight: true,
      rightIndex: 0,
      rightSelectList1: [
        {
          name: "海塘分布",
          num: 0
        },
        {
          name: "安澜建设",
          num: 0
        }
        // {
        //   name: '台风路径',
        //   num: 0
        // },
        // {
        //   name: '工程照片',
        //   num: 0
        // },
        // {
        //   name: '工程视频',
        //   num: 0
        // }
      ],
      rightSelectList2: [
        {
          name: "实时报警",
          num: 0
        },
        {
          name: "预测预警",
          num: 0
        }
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
          color: "#E02021"
        },
        {
          name: "二级海塘",
          color: "#EB7E63"
        },
        {
          name: "三级海塘",
          color: "#F7C53A"
        },
        {
          name: "四级海塘",
          color: "#3154EB"
        },
        {
          name: "五级海塘",
          color: "#1DC47E"
        },
        {
          name: "规模以下",
          color: "#C2C3C2"
        }
      ],
      opt: {
        statistic: "mapTabs",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd
      },
      showFanganDialog: false,
      fanganUrl: "",
      showDetailDialog: false,
      detailUrl: "",
      isShowHideLayer: true
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
            num: res.data[0].proCnt
          },
          {
            name: "安澜建设",
            num: res.data[0].constructionCnt
          }
          // {
          //   name: '工程照片',
          //   num: res.data[0].photoCnt
          // },
          // {
          //   name: '工程视频',
          //   num: res.data[0].cameraCnt
          // }
        ];

        this.rightSelectList2 = [
          {
            name: "实时报警",
            num: res.data[0].overCnt
          },
          {
            name: "预测预警",
            num: res.data[0].forecast
          }
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
      this.$router.push("/zhMenu");
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../style/common.scss";
.drawer_left {
  @include drawerLayout();
  @include drawerLeft();
}
.drawer_right {
  @include drawerLayout();
  @include drawerRight();
  .right_select_open {
    @include rightSelectOpen();
  }
  .right_select {
    @include rightSelect();
    .right_select_title {
      @include rightSelectTitle();
    }
    .right_select_item {
      @include rightSelectItem();
      &.active {
        @include rightSelectActive();
      }
    }
    .right_select_close {
      @include rightSelectClose();
    }
  }
  .map_legend {
    position: absolute;
    right: 100%;
    bottom: 32px;
    width: 166px;
    box-sizing: border-box;
    padding: 0 12px;
    transform: translateX(-6px);
    background-image: url($swImgUrl + "legend-bgi-haitang.png");
    .map_legend_title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 32px;
      padding: 0 5px 0 4px;
      .map_legend_title_left {
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
        line-height: 32px;
      }
      .map_legend_title_right {
        img {
          width: 13px;
          height: 12px;
        }
        .flip {
          transform: rotateZ(180deg);
        }
      }
    }
    .map_legend_line {
      height: 1px;
      background-image: url($swImgUrl + "legend-line.png");
    }
    .map_legend_list {
      padding: 12px 0 16px 6px;

      .map_legend_item {
        display: flex;
        align-items: center;
        height: 22px;
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
        line-height: 22px;
        & + .map_legend_item {
          margin-top: 8px;
        }
        p {
          width: 18px;
          height: 4px;
          margin-right: 8px;
        }
      }
    }
    .switch-box {
      display: flex;
      ::v-deep .el-switch {
        .el-switch__core {
          height: 18px;
          &:after {
            top: 2px;
            width: 12px;
            height: 12px;
            background-color: #0431a0;
          }
        }
        &.is-checked {
          .el-switch__core:after {
            margin-left: -14px;
          }
        }
      }
      span {
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
        line-height: 34px;
      }
    }
  }
}
.fangan_iframe {
  width: 100%;
  height: 600px;
}
.seawall_dialog_iframe {
  height: 100%;
  width: 100%;
}
.drawer_right_home {
  position: absolute;
  top: 6px;
  left: -85px;
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