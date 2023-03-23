<template>
  <div class="seawall_wrap">
    <div class="seawall_top">
      <p class="time">{{ nowTime }}</p>
      <p class="title">智慧海塘</p>
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
    <div class="seawall_content">
      <section class="seawall_left">
        <SeawallScreenLeft ref="seawallLeft" />
      </section>
      <section class="seawall_middle">
        <SeawallScreenMap ref="seawallMap" />
        <SeawallScreenLegend />
        <div class="bottom_btns">
          <div
            class="btns_item"
            v-for="(item, index) in bottomBtnList"
            :key="index"
            @click="openUrl(index)"
          >
            {{ item.name }}
            <div
              v-if="showSelectBox && item.selectType"
              class="btns_select_box"
            >
              <div
                class="btns_select_item"
                v-for="item in btnSelectList"
                :key="item.name"
                @click="clickBtnSelect(item.url)"
              >
                {{ item.name }}
              </div>
            </div>
          </div>
        </div>
      </section>
      <section class="seawall_right">
        <SeawallScreenRight />
      </section>
    </div>
    <primary-dialog
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
          v-if="dialogVisible"
          :sgzgSelectBasin="sgzgSelectBasin"
          :sgzgSelectAdcd="sgzgSelectAdcd"
          :checkedArr="checkedArr"
          :sgzgMapType="sgzgMapType"
          :workStatus="workStatus"
          :xzScale="xzScale"
        />
      </div>
    </primary-dialog>

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
          src="@/assets/images/back2.png"
          alt=""
        />
        <p>{{ projectTitle }}</p>
      </div>
      <iframe
        class="reservoir_dialog_iframe"
        :src="detailUrl"
        frameborder="0"
      ></iframe>
    </div>

    <!-- 我的任务 -->
    <primary-dialog
      mainTitle="我的任务"
      :visible="showMyWorkbench"
      @handleClose="closeMyWorkbench"
      width="75%"
      top="10vh"
    >
      <div class="dialog-content">
        <SeawallScreenWorkbench v-if="showMyWorkbench" />
      </div>
    </primary-dialog>
    <primary-dialog
      mainTitle=""
      @handleClose="closeBriefReport"
      @changeFullscreen="changeFullscreen"
      :fullscreen="fullscreen"
      :visible="showBriefReport"
      width="90%"
      top="3vh"
    >
      <div
        class="dialog-content brief-report"
        :class="{ 'fullscreen-box': fullscreen }"
        v-loading="briefReportLoading"
        element-loading-text="简报生成中,请稍等...."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
      >
        <BriefReport v-if="briefReportType" @clearLoading="clearLoading" />
      </div>
    </primary-dialog>

    <div class="mask" v-show="showWisdomPhotoList">
      <div :class="['report-dialog', { 'full-screen': fullscreen }]">
        <!-- <div class="report-dialog-close">
          <i class="el-icon-close" style="z-index: 2" @click="closeTitlePhotosPopup()"></i>
        </div> -->
        <div class="dialog-icon-group">
          <i
            v-if="!fullscreen"
            class="dialog-icon dialog-zoom-in"
            @click="fullscreen = true"
          ></i>
          <i
            v-else
            class="dialog-icon dialog-zoom-out"
            @click="fullscreen = false"
          ></i>
          <i
            class="dialog-icon dialog-close"
            @click="showWisdomPhotoList = false"
          ></i>
        </div>
        <div :class="['report-dialog-inner', { 'full-screen': fullscreen }]">
          <!-- arrow="always" -->
          <el-carousel
            ref="carousel"
            class="carousel_box ppt-carousel"
            indicator-position="inside"
            :autoplay="false"
          >
            <el-carousel-item
              class="carousel_item"
              v-for="(item, index) in titlePhotosList"
              :key="index"
            >
              <!-- <div class="swiper_img" :style="{backgroundImage:'url('+item.url+')'}" :title="item.name"></div> -->
              <!-- <el-image style="width:100%;height: 85.5vh;" :src="item.url" :fit="fit"></el-image> -->
              <img
                :src="item.url"
                alt=""
                :class="[{ 'full-box': fullscreen }]"
              />
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { selectGzt } from "@/api/api_reservoir";
import SeawallScreenLeft from "./SeawallScreenLeft.vue";
import SeawallScreenRight from "./SeawallScreenRight.vue";
import SeawallScreenMap from "./seawallScreenMap/SeawallScreenMap.vue";
import SeawallScreenLegend from "./seawallScreenLegend/SeawallScreenLegend";
import SeawallScreenWorkbench from "./seawallScreenWorkbench/SeawallScreenWorkbench";
import Generation from "./components/Generation.vue";
import BriefReport from "./components/BriefReport.vue";
import PrimaryDialog from "./components/PrimaryDialog.vue";
import CarouselPhoto from "./components/CarouselPhoto.vue";
import PreviewPhoto from "@/components/PreviewPhoto";
import PreviewVideo from "@/components/PreviewVideo";
import PreviewCamera from "@/components/PreviewCamera";
import { clickLog } from "@/api/api_reservoir";
export default {
  name: "SeawallScreenIndex",
  props: {},
  components: {
    SeawallScreenLeft,
    SeawallScreenRight,
    SeawallScreenMap,
    SeawallScreenLegend,
    SeawallScreenWorkbench,
    Generation,
    PrimaryDialog,
    SeawallScreenLegend,
    PreviewPhoto,
    CarouselPhoto,
    PreviewVideo,
    PreviewCamera,
    BriefReport,
  },
  data() {
    return {
      showWisdomPhotoList: false,
      wisdomPhotoListLoading: false,
      titlePhotosList: [], //新增标题轮播图弹窗
      nowTime: new Date().format("yyyy年MM月dd日 hh:mm:ss"),
      dateTimer: null,
      dialogVisible: false,
      checkedArr: [],
      workStatus: [],
      sgzgMapType: "",
      xzScale: "",
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
      bottomBtnList: [
        // {
        //   name: "综合报告",
        //   permission: "jlzs:jianbao",
        // },
        // {
        //   name: "智慧海塘",
        //   permission: "jlzs:futurers",
        //   selectType: true
        // },
        {
          name: "工作平台",
          permission: "jlzs:business",
        },
      ],
      showSelectBox: false,
      btnSelectList: [
        {
          name: "鹿城区海塘",
          url: "https://slgh.lucheng.gov.cn/synthesizeSystem/waterDisasterCustom?userCode=121",
        },
        {
          name: "海盐段海塘",
          url: "https://web.dcyun.com:48443/htfc/#/basicMessage?userCode=121",
        },
      ],
      showMyWorkbench: false,
      showWork: false,
      modal: true,
      briefReportLoading: true,
      showBriefReport: false,
      briefReportType: false,
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
    if (window.performance.navigation.type === 1) {
      clickLog({
        title: "智慧海塘-重加载页面",
      });
    } else {
      clickLog({
        title: "智慧海塘-首次进入",
      });
    }
    this.getWorkNumber();
    this.userName = this.$localData("get", "userInfo").psnm;
    this.userPhone = this.$localData("get", "userInfo").phone;
    this.dateTimer = setInterval(() => {
      this.nowTime = new Date().format("yyyy年MM月dd日 hh:mm:ss");
    }, 1000);
  },
  methods: {
    // 画点 不聚合
    showMapPoints(pointArr, type, stypeObj) {
      this.$nextTick(() => {
        this.$refs.seawallMap.newMapJh(pointArr, type, stypeObj);
      });
    },

    // 画线
    showMapLines(lineArr) {
      this.$nextTick(() => {
        this.$refs.seawallMap._drawLines(lineArr);
      });
    },
    clearLoading() {
      this.briefReportLoading = false;
    },
    closeBriefReport() {
      this.showBriefReport = false;
      this.briefReportType = false;
      this.briefReportLoading = true;
      this.fullscreen = false;
    },
    clickBriefReport() {
      this.briefReportType = true;
      this.showBriefReport = true;
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
    /**
     * @function:
     * @description:
     * @param {*} checkedArr
     * @param {*} sgzgMapType
     * @param {*} workStatus
     * @param {*} xzScale  限制运用新增带入 规模参数
     * @return {*}
     */
    open(checkedArr, sgzgMapType, workStatus, xzScale) {
      if (sgzgMapType === "风险研判") {
        let color = this.$store.state.colorCode;
        switch (color) {
          case "green":
            this.workStatus = ["绿码"];
            break;
          case "yellow":
            this.workStatus = ["黄码"];
            break;
          case "red":
            this.workStatus = ["红码"];
            break;
          case "all":
            this.workStatus = ["红码", "黄码", "绿码"];
            break;
        }
      }
      if (sgzgMapType === "安全鉴定") {
        switch (checkedArr) {
          case "一类塘":
            this.checkedArr = [1];
            break;
          case "二类塘":
            this.checkedArr = [2];
            break;
          case "三类塘":
            this.checkedArr = [3];
            break;
          case "一年内到期":
            this.checkedArr = [4];
            break;
        }
      } else {
        this.checkedArr = checkedArr || [];
      }
      if (sgzgMapType === "限制运用") {
        this.xzScale = xzScale.join(",");
      }
      this.workStatus = workStatus;
      this.sgzgMapType = sgzgMapType;
      this.dialogVisible = true;
    },
    handleClose() {
      this.fullscreen = false;
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

    // 弹窗内打开 智慧水库标题轮播图
    lookPhotos() {
      clickLog({
        title: "智慧水库-标题轮播",
      });
      this.showWisdomPhotoList = true;
      this.titlePhotosList = [
        // { url: this.$config.zhImgUrl + "diyizhnag.png", name: "1" },
        { url: this.$config.zhImgUrl + "disanzhang.png", name: "2" },
      ];
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
    openUrl(index) {
      if (index == 0) {
        this.showSelectBox = false;
        window.open("https://sk.zjwater.com/newIndex");
      }

      // else if (index == 0) {
      //   this.showSelectBox = !this.showSelectBox;
      // } else {
      //   this.clickBriefReport()
      // }
    },
    clickBtnSelect(url) {
      window.open(url);
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
.seawall_wrap {
  width: 100%;
  height: 100%;
  background: url($imageUrl + "skzg_bg.png") no-repeat center;
  background-size: 100% 100%;
  color: #fff;
  .seawall_top {
    height: 96px;
    position: relative;
    box-sizing: border-box;
    background: url($imageUrl + "skzg_title.png") no-repeat center;
    background-size: 100% 100%;

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
  .seawall_content {
    height: calc(100% - 100px);
    @include flexbox();
    .seawall_left {
      z-index: 2;
      width: 440px;
      height: 100%;
      margin-left: 40px;
    }
    .seawall_middle {
      z-index: 2;
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
        // background: rgb(38 106 169 / 30%);
      }
      .bottom_btns {
        position: absolute;
        bottom: 10px;
        width: 100%;
        box-sizing: border-box;
        // padding: 0 0px;
        @include flexbox();
        @include flexJC(center);
        .btns_item {
          cursor: pointer;
          text-align: center;
          box-sizing: border-box;
          padding-top: 14px;
          font-size: 20px;
          font-family: youshe;
          width: 169px;
          height: 75px;
          background: url($imageUrl + "btns_bg.png") no-repeat center;
          background-size: 100% 100%;
          // &:first-child {
          //   margin-right: 30px;
          // }
          position: relative;
          .btns_select_box {
            position: absolute;
            left: 20px;
            bottom: 75px;
            .btns_select_item {
              width: 130px;
              height: 35px;
              line-height: 35px;
              color: 16px;
              background: rgba(2, 86, 241, 0.3);
              border: 1px solid #5c8ddc;
              margin-bottom: 3px;
              cursor: pointer;
              box-sizing: border-box;
              &:hover {
                color: #69e3ee;
                border: none;
                background: url($imageUrl + "select_btns_bg.png") no-repeat
                  center;
                background-size: 100% 100%;
              }
            }
          }
        }
      }
    }
    .seawall_right {
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