<template>
  <div class="skzg_wrap">
    <div class="skzg_top">
      <p class="time">{{ nowTime }}</p>
      <p class="title">水网智管</p>
      <div class="person" v-if="userPhone != '15958118384'">
        <div v-if="showWork" class="person_left">
          <span @click="showMyWorkbench = true">我的任务</span>
        </div>
        <div class="person_right">
          <img :src="$config.imgUrl + 'man.png'" alt="" />
          <span>{{ userName }}</span>
        </div>
      </div>
    </div>

    <!-- <section class="topinfo_wrapper">
      <div class="btns_item">引调水</div>
      <div class="item_wrap">
        <ul class="gs_list">
          <li :class="['gs_item',{'pl':index === 0 }]" v-for="(item, index) in ydsItemList" :key="index">
            <div>
              <h3>{{ item.name }}</h3>
              <p>
                <span class="num">{{ item.num }}</span>
                <span>{{ item.unit }}</span>
                <span v-show="index === 0" class="l_title">跨市 <span class="l_num">2</span>条 / 跨县<span class="l_num">8</span> 条</span>
              </p>
            </div>
          </li>
        </ul>
      </div>
    </section> -->

    <div class="map_legend">
      <div class="map_legend_title">
      </div>
      <transition name="fade">
        <div class="switch-box">
          <el-switch style="margin: 10px 18px 17px 6px;height: 16px;" @click.stop @change="changeSwitch" v-model="jhFlag" :width="28" active-color="#46FDFF" inactive-color="#999999"></el-switch>
          <span>图标聚合</span>
        </div>
      </transition>
    </div>

    <div class="bottom_btns">
      <div class="btns_item" @click="openUrl('https://sk.zjwater.com/')">进入平台</div>
    </div>

    <!-- 左侧的背景图 -->
    <img class="left_bg" :src="$config.swImgUrl + 'left_pingbg.png'" alt="">
    <!-- 右侧的背景图 -->

    <img class="right_bg" :src="$config.swImgUrl + 'right_pingbg.png'" alt="">
    <div class="skzg_content">
      <section class="skzg_left">
        <index-left ref="skzgLeft" />
      </section>
      <section class="skzg_middle">
        <index-map ref="skzgMap" />
      </section>
      <section class="skzg_right">
        <index-right />
      </section>
    </div>

    <primary-dialog :mainTitle="sgzgMapType" :modal="modal" :visible="dialogVisible" @handleClose="handleClose" @changeFullscreen="changeFullscreen" :fullscreen='fullscreen' width="1400px" top="5vh">
      <div class="dialog-content" :class="{'fullscreen-box': fullscreen}">
        <generation ref="generation" :fullscreen='fullscreen' v-if="dialogVisible" :sgzgSelectBasin="sgzgSelectBasin" :sgzgSelectAdcd="sgzgSelectAdcd" :checkedArr="checkedArr" :sgzgMapType="sgzgMapType" :workStatus="workStatus" />
      </div>
    </primary-dialog>

    <preview-photo v-if="showLookPhotosPopup" :loading="lookPhotosLoading" :photoList="photosList" :reservoirName="reservoirName" @closeLookPhotosPopup="closeLookPhotosPopup"></preview-photo>

    <preview-video v-if="showLookVideoPopup" :loading="lookVideoLoading" :videoUrl="videoUrl" @closeLookVideoPopup="closeLookVideoPopup"></preview-video>

    <preview-camera v-if="showLookCameraPopup" :prcd="prcd" :name="skName" @closeLookCameraPopup="closeLookCameraPopup"></preview-camera>
    <div class="reservoir_dialog" v-show="showDetailDialog">
      <div class="reservoir_title">
        <img @click="showDetailDialog = false" src="@/assets/images/back2.png" alt="" />
        <p>{{ projectTitle }}</p>
      </div>
      <iframe class="reservoir_dialog_iframe" :src="detailUrl" frameborder="0"></iframe>
    </div>

    <!-- 我的任务 -->
    <primary-dialog mainTitle="我的任务" :visible="showMyWorkbench" @handleClose="closeMyWorkbench" width="75%" top="10vh">
      <div class="dialog-content">
        <Workbench v-if="showMyWorkbench" />
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import { selectGzt } from "@/api/api_reservoir";
import IndexLeft from "./IndexLeft.vue";
import IndexRight from "./IndexRight.vue";
import IndexMap from "./indexMap/IndexMap.vue";
import Workbench from "./Workbench/Workbench.vue";
import PrimaryDialog from "./components/PrimaryDialog.vue";
import PreviewPhoto from "@/components/PreviewPhoto";
import PreviewVideo from "@/components/PreviewVideo";
import PreviewCamera from "@/components/PreviewCamera";
export default {
  name: "MenuIndex",
  props: {},
  components: {
    IndexLeft,
    IndexRight,
    IndexMap,
    Workbench,
    PrimaryDialog,
    PreviewPhoto,
    PreviewVideo,
    PreviewCamera,
  },
  data() {
    return {
      nowTime: new Date().format("yyyy年MM月dd日 hh:mm:ss"),
      dateTimer: null,
      dialogVisible: false,
      checkedArr: [],
      workStatus: [],
      sgzgMapType: "",
      userName: "",
      userPhone: "",
      showLookPhotosPopup: false,
      lookPhotosLoading: true,
      photosList: [],
      reservoirName: "",
      showLookVideoPopup: false,
      lookVideoLoading: true,
      videoUrl: "",
      showDetailDialog: false,
      detailUrl: "",
      projectTitle: "详情",
      prcd: "",
      skName: "",
      showLookCameraPopup: false,
      fullscreen: false,
      showSelectBox: false,
      showMyWorkbench: false,
      showWork: false,
      modal: true,
      jhFlag: true, // 聚合开关
      ydsItemList: [
        {
          name: "数量",
          num: 10,
          unit: "条",
        },
        {
          name: "总长度",
          num: 4277,
          unit: "km",
        },
        {
          name: "设计年引水总量",
          num: 19,
          unit: "万方",
        },
      ],
    };
  },
  computed: {
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd;
    },
    sgzgSelectBasin() {
      return this.$store.state.sgzgSelectBasin;
    },
  },
  mounted() {
    this.getWorkNumber();
    this.userName = this.$localData("get", "userInfo").psnm;
    this.userPhone = this.$localData("get", "userInfo").phone;
    this.dateTimer = setInterval(() => {
      this.nowTime = new Date().format("yyyy年MM月dd日 hh:mm:ss");
    }, 1000);
  },
  methods: {
    changeSwitch(val) {
      if (val) {
        this.$refs.skzgMap.setCluster(100,'PointLayer')
      } else {
        this.$refs.skzgMap.setCluster(0,'PointLayer')
      }
    },
    // 画点 聚合
    showMapPointsJh(pointArr, layerName, mapType) {
      this.$nextTick(() => {
        this.$refs.skzgMap.drawMapPointsJh(pointArr, layerName, mapType);
      });
    },
    // 画点 不聚合
    showMapPoints(pointArr, layerName, mapType) {
      this.$nextTick(() => {
        this.$refs.skzgMap.drawMapPoints(pointArr, layerName, mapType);
      });
    },

    async getWorkNumber() {
      let res = await selectGzt();
      if (res.success) {
        if (res.data.skCount == 0) {
          this.showWork = false;
        } else {
          this.showWork = true;
        }
      }
    },
    openUrl(index) {
      window.open("https://sk.zjwater.com/newIndex");
    },
    handleClose() {
      this.fullscreen = false
      this.dialogVisible = false;
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

    // 查看图片
    openLookPhotosPopup() {
      this.showLookPhotosPopup = false;
      this.$nextTick(() => {
        this.lookPhotosLoading = true;
        this.showLookPhotosPopup = true;
      });
    },

    closeLookPhotosPopup() {
      console.log(3);
      this.showLookPhotosPopup = false;
    },
    showPhotosList(arr, reservoirName) {
      setTimeout(() => {
        this.lookPhotosLoading = false;
        this.photosList = arr;
        this.reservoirName = reservoirName;
      });
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
      // this.detailUrl = `http://www.baidu.com`
      this.showDetailDialog = true;
    },

    closeMyWorkbench() {
      this.showMyWorkbench = false;
    },
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      this.$nextTick(() => {
        // this.$refs.generation.$refs.tb_wrappers.doLayout();
        this.$refs.generation.getTableList();
      });
    },
  },

  beforeDestroy() {
    this.dateTimer = null;
  },
};
</script>

<style lang='scss' scoped>
.skzg_wrap {
  width: 100%;
  height: 100%;
  background: url($imageUrl + "skzg_bg.png") no-repeat center;
  background-size: 100% 100%;
  color: #fff;
  position: relative;
  .topinfo_wrapper {
    position: absolute;
    top: 105px;
    left: 25%;
    width: 50%;
    box-sizing: border-box;
    z-index: 99;
    @include flexbox();
    @include flexJC(center);
    @include flexAI(center);
    @include flexflow(column wrap);
    .btns_item {
      cursor: pointer;
      text-align: center;
      box-sizing: border-box;
      font-size: 26px;
      font-family: youshe;
      width: 500px;
      height: 40px;
      line-height: 40px;
      background: url($swImgUrl + "shuazi.png") no-repeat center;
      background-size: 100% 100%;
      position: relative;
    }
    .item_wrap {
      width: 720px;
      height: calc(50% - 50px);
      margin: 2% 0;
      .gs_list {
        @include flexbox();
        @include flexAI(center);
        @include flexflow(row wrap);
        width: 100%;
        height: calc(100% - 50px);
        box-sizing: border-box;
        // padding-left: 16px;
        .gs_item {
          width: 30%;
          height: 50%;
          @include flexbox();
          @include flexAI(center);
          box-sizing: border-box;
          &.pl {
            width: 40%;
          }
          div {
            h3 {
              font-size: 20px;
              padding-top: 4px;
              padding-bottom: 8px;
              color: #fff;
            }
            p {
              font-size: 16px;
              span:nth-child(1),
              span:nth-child(2) {
                color: rgb(0, 232, 255);
                user-select: none;
                vertical-align: text-bottom;
                font-size: 28px;
                &.num {
                  font-family: pangmen;
                  // box-shadow: 10px 5px 5px red;
                  margin-right: 4px;
                  text-shadow: 0 0 10px rgba(0, 232, 255, 0.4),
                    0 0 20px rgba(0, 232, 255, 0.4),
                    0 0 30px rgba(0, 232, 255, 0.4),
                    0 0 40px rgba(0, 232, 255, 0.4);
                }
              }
              span:nth-child(3) {
                color: #fff;
                user-select: none;
                vertical-align: text-bottom;
                font-size: 16px;
                &.l_title {
                  margin-left: 4px;
                }
                .l_num {
                  font-size: 16px;
                  font-family: pangmen;
                  padding: 0 2px;
                }
              }
            }
          }
        }
      }
    }
  }
  .bottom_btns {
    position: absolute;
    bottom: 36px;
    width: 100%;
    box-sizing: border-box;
    z-index: 99;
    @include flexbox();
    @include flexJC(center);
    .btns_item {
      cursor: pointer;
      text-align: center;
      box-sizing: border-box;
      font-size: 45px;
      font-family: youshe;
      width: 500px;
      height: 60px;
      line-height: 60px;
      background: url($swImgUrl + "bottom_btn_bg.png") no-repeat center;
      background-size: 100% 100%;
      position: relative;
    }
  }
  .map_legend {
    position: absolute;
    bottom: 12%;
    right: 30%;
    width: 166px;
    box-sizing: border-box;
    padding: 0 12px;
    z-index: 101;
    transform: translateX(-6px);
    background-image: url($swImgUrl + "legend-bgi-haitang.png");
    .map_legend_title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 10px;
      padding: 0 5px 0 4px;
    }
    .switch-box {
      cursor: pointer;
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

  .left_bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 32%;
    height: 100%;
    opacity: 0.4;
    z-index: 1;
  }
  .right_bg {
    position: absolute;
    top: 0;
    right: 0;
    width: 32%;
    height: 100%;
    opacity: 0.4;
    z-index: 1;
  }
  .skzg_top {
    height: 96px;
    position: relative;
    box-sizing: border-box;
    background: url($imageUrl + "skzg_title.png") no-repeat center;
    background-size: 100% 100%;
    z-index: 2;
    .lightning {
      z-index: 1;
      position: absolute;
      top: 38px;
      width: 500px;
      &.l-left {
        left: 13%;
      }
      &.l-right {
        right: 13%;
      }
    }
    .time {
      font-size: 24px;
      position: absolute;
      top: 22px;
      left: 25px;
      font-weight: 500;
    }
    .title {
      font-family: "youshe";
      text-align: center;
      font-size: 60px;
      color: #fff;
      padding-top: 10px;
      letter-spacing: 3px;
      span {
        font-size: 24px;
        font-weight: bold;
        color: #bdddf9;
        &.report_text {
          font-family: "pangmen";
        }
      }
    }
    .top-right-icon {
      position: absolute;
      left: 56%;
      top: 25px;
      width: 5vh;
      height: 5vh;
      background: url($imageUrl + "outCircle.png") no-repeat center;
      background-size: 120% 120%;
      background-position: 50%;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-left: 10px;
      cursor: pointer;
    }
    .person {
      position: absolute;
      right: 20px;
      top: 20px;
      height: 36px;
      background: #204cc0;
      box-sizing: border-box;
      border-radius: 18px;
      border: 2px solid #5a89f1;
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
        }
      }
    }
  }
  .skzg_content {
    height: calc(100% - 100px);
    @include flexbox();
    .skzg_left {
      z-index: 2;
      width: 440px;
      height: 100%;
      margin-left: 40px;
    }
    .skzg_middle {
      // z-index: 2;
      @include flex(1);
      height: 100%;
      background: transparent;
      position: relative;
      .map_wrap {
        // height: calc(100% - 160px);
        height: calc(100% - 40px);
        width: 100%;
      }
      .legend_wrap {
        width: 170px;
        position: absolute;
        bottom: 60px;
        right: 10px;
      }
    }
    .skzg_right {
      z-index: 2;
      width: 440px;
      height: 100%;
      margin-right: 40px;
    }
  }
  .dialog-content {
    height: 550px;
    // height: 80vh;
    ::v-deep .primary-dialog {
      .el-dialog__body {
        padding: 0;
      }
    }
    &.brief-report {
      height: 660px;
      overflow-y: scroll;
      scrollbar-width: none; /* Firefox */
      -ms-overflow-style: none; /* IE 10+ */
      &::-webkit-scrollbar {
        display: none;
      }
    }
  }
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
</style>

<style>
.el-icon-loading {
  font-size: 25px;
}
.report-dialog-inner .el-carousel__button {
  width: 12px;
  height: 12px;
  background-color: #443d3d;
  border-radius: 50%;
}
</style>