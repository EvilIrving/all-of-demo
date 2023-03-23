<template>
  <div class="popup_map" ref="pointDialog" v-show="pointDialogType">
    <!-- 工程气泡 有照片视频 -->
    <div class="proj_content">
      <div class="dialog_title">
        <!-- 水库信息 -->
        <span @click="lookDetail(popupData)">{{ popupData.NAME ? popupData.NAME : "-" }}</span> -
        <span @click="lookDetail(popupData)" v-html="projectScale"></span>
        <i class="el-icon-close" @click="closePopup()"></i>
      </div>
      <!-- 上部分 -->
      <div class="proj_top">
        <!-- 上部分-左侧 -->
        <div class="proj_left">
          <!-- 内容 -->
          <div class="proj_title">
            <p><span>{{ popupData.cityADNM }} - {{ popupData.countryADNM }}</span></p>
          </div>
          <div class="proj_detail">
            <div class="popup_item">
              <p>水位：</p>
              <span>{{
                popupData.waterLevel
                ? popupData.waterLevel.toFixed(2) + "m"
                : "- "
              }}
              </span>
              <!-- 括号填写是否上升及数字 -->
              <div v-if="parseFloat(popupData.waterChange)">
                <span v-html="'&nbsp'"></span>(
                <span>{{
                  popupData.waterChange
                  ? popupData.waterChange.toFixed(2)
                  : popupData.waterChange
                }}</span>
                <span v-if="popupData.waterChange"><i class="el-icon-top" style="color: green; font-size: 16px" v-if="
                  popupData.waterChange > 0 || popupData.waterChange == null
                "></i>
                  <i class="el-icon-bottom" style="color: red; font-size: 16px" v-else></i></span>)
              </div>
              <!-- 加个箭头 -->
            </div>
            <div class="popup_item">
              <p>时间：</p>
              <span>{{
                popupData.waterleveltm
                ? popupData.waterleveltm.slice(5, 16)
                : "-"
              }}</span>
            </div>
            <div class="popup_item">
              <p>总库容：</p>
              <span>{{
                popupData.TATOL_STORAGE ? popupData.TATOL_STORAGE + "万m³" : "-"
              }}</span>
            </div>
            <div class="popup_item">
              <p>集雨面积：</p>
              <span>{{ popupData.RCA ? popupData.RCA + "km²" : "-" }}</span>
            </div>
            <div class="popup_item">
              <p>坝型：</p>
              <span>{{ popupData.DTBS ? popupData.DTBS : "-" }}</span>
            </div>
            <div class="popup_item">
              <p>安全鉴定：</p>
              <span v-if="
                popupData.status && popupData.status.slice(0, 3) == '三类坝'
              ">{{ popupData.status.slice(0, 3) }}</span>
              <span v-else>{{
                popupData.status ? popupData.status : "-"
              }}</span>
            </div>
            <div class="popup_item" v-if="
              popupData.status && popupData.status.slice(0, 3) == '三类坝'
            ">
              <p>除险加固：</p>
              <span>{{
                popupData.status ? popupData.status.slice(3) : "-"
              }}</span>
            </div>
            <div class="popup_item" v-if="popupData.rightPopType && popupData.rightPopType == 'xsl'">
              <p>蓄水率：</p>
              <span>{{ popupData.xsl ? popupData.xsl : "-" }}</span>
            </div>
            <div class="popup_item" v-if="popupData.rightPopType && popupData.rightPopType == 'kgkn'">
              <p>可纳雨：</p>
              <span>{{
                popupData.rainContains
                ? popupData.rainContains.toFixed(2) + "mm"
                : "0"
              }}</span>
            </div>
            <div class="popup_item" v-if="popupData.rightPopType && popupData.rightPopType == 'kgkn'">
              <p>可纳洪：</p>
              <span>{{
                popupData.knx ? popupData.knx + popupData.knxUnit : "0"
              }}</span>
            </div>

            <div class="popup_item" v-if="popupData.rightPopType && popupData.rightPopType == 'kgkn'">
              <p>可供水：</p>
              <span>{{
                popupData.kgs ? popupData.kgs + popupData.kgsUnit : "0"
              }}</span>
            </div>
            <!-- 预测预警 -->
            <div class="popup_item" v-if="popupData.isYcyj">
              <p>{{ popupData.overName }}：</p>
              <span>{{
                popupData.overValue ? popupData.overValue.toFixed(2) + "m" : "0"
              }}</span>
            </div>
            <div class="popup_item" v-if="popupData.isYcyj">
              <p>预报水位：</p>
              <span style="color: #ff0000">{{
                popupData.ybWater ? popupData.ybWater.toFixed(2) + "m" : "0"
              }}</span>
            </div>
          </div>
        </div>
        <!-- 上部分-右侧 -->
        <div class="proj_right">
          <!-- 照片 -->
          <div style="cursor: pointer" v-loading="loadingImg" @click="lookPhotos(popupData)">
            <div v-if="photoList.length" class="img_box">
              <el-carousel ref="carousel" class="carousel_box" indicator-position="none" arrow="always" :autoplay="false"
                @change="changeItem">
                <el-carousel-item class="carousel_item" v-for="(item, index) in photoList" :key="index">
                  <div class="swiper_img" :style="{ backgroundImage: 'url(' + item.smallUrl + ')' }" :title="item.name">
                  </div>
                </el-carousel-item>
              </el-carousel>
              <div class="preview_words">
                <span>{{ imgIndex + 1 }}</span> / {{ photoList.length }}
              </div>
            </div>
            <div v-if="!loadingImg && !photoList.length" class="img_nodata">
              <img src="@/assets/images/noData.png" alt="" />
              <p>暂无图片</p>
            </div>
            <div v-if="loadingImg" class="img_loading">
              <p>图片加载中...</p>
            </div>
          </div>
        </div>
      </div>
      <!-- 下部分 -->
      <div class="proj_bottom">
        <div class="person_list" v-loading="loadingPerson">
          <div class="person_item">
            <div class="person_item_top">
              <h4>政府责任人</h4>
              <img :src="$config.imgUrl + 'personnal-white.png'" alt="" />
            </div>
            <p>{{ personData.executiveGovName }}</p>
            <span>{{ personData.executiveGovPhone }}</span>
          </div>
          <div class="person_item">
            <div class="person_item_top">
              <h4>水行政主管部门负责人</h4>
              <img :src="$config.imgUrl + 'personnal-white.png'" alt="" />
            </div>
            <p>{{ personData.executiveWaterName }}</p>
            <span>{{ personData.executiveWaterPhone }}</span>
          </div>
          <div class="person_item">
            <div class="person_item_top">
              <h4>主管部门产权责任人</h4>
              <img :src="$config.imgUrl + 'personnal-white.png'" alt="" />
            </div>
            <p>{{ personData.executiveDepartName }}</p>
            <span>{{ personData.executiveDepartPhone }}</span>
          </div>
          <div class="person_item" v-if="personData.ifDepartment == '有'">
            <div class="person_item_top">
              <h4>管理单位责任人</h4>
              <img :src="$config.imgUrl + 'personnal-white.png'" alt="" />
            </div>
            <p>{{ personData.executiveManageName }}</p>
            <span>{{ personData.executiveManagePhone }}</span>
          </div>
          <div class="person_item">
            <div class="person_item_top">
              <h4>技术负责人</h4>
              <img :src="$config.imgUrl + 'personnal-white.png'" alt="" />
            </div>
            <p>{{ personData.technicalName }}</p>
            <span>{{ personData.techinicalPhone }}</span>
          </div>
          <div class="person_item">
            <div class="person_item_top">
              <h4>巡查负责人</h4>
              <img :src="$config.imgUrl + 'personnal-white.png'" alt="" />
            </div>
            <p>{{ personData.patrolName }}</p>
            <span>{{ personData.patrolPhone }}</span>
          </div>
        </div>
      </div>
    </div>
</div>
</template>

<script>
import {
  reservoirPhotoListApi,
  reservoirVideoListApi,
  reservoirCameraListApi,
  reservoirPersonListApi,
  getFile,
  reservoirLeftStatisticApi,
} from "@/api/api_reservoir";
export default {
  name: "PointDialog",
  props: {
    map: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      pointDialogType: false,
      loadingPerson: false,
      loadingImg: false,
      loadingVideo: false,
      popupData: {},
      photoList: {},
      videoList: [],
      cameraList: [],
      personData: {},
      popupTabsIndex: 0,
    };
  },
  computed: {
    clickFeatVal() {
      return this.$store.state.map.clickFeatVal;
    },
    projectScale() {
      let text = ''
      switch (this.popupData.project_scale) {
        case '1':
          text = '&nbsp[大(1)型]';
          break;
        case '2':
          text = '&nbsp[大(2)型]';
          break;
        case '3':
          text = '&nbsp[中型]';
          break;
        case '4':
          text = '&nbsp [小(1)型]';
          break;
        default:
          text = '&nbsp [小(2)型]';
          break;
      }
      return text
    }
  },
  watch: {
    clickFeatVal: {
      handler: function (value) {
        if (value) {
          this.popupTabsIndex = 0
          this.mapClick(value);
        }
      },
      deep: true,
    },
  },
  destroyed() {
    this.$store.commit("setReservoirStorage", false);
    this.$store.commit("setClickFeatVal", "");
  },
  methods: {
    closePopup() {
      this.$olMapUtil.removeHoverFrame(this.map, "reservoirPointDialog");
      this.$olMapUtil.flyPoint(this.map, [120.6, 28.8], 8);
      this.$store.commit("setReservoirStorage", false);
      this.$store.commit("setClickFeatVal", "");
      this.pointDialogType = false;
    },
    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$emit("lookDetail", row);
    },
    // 弹窗内打开查看图片
    lookPhotos(row) {
      this.$emit("lookPhotos", this.photoList, row.NAME);
    },
    // 弹窗内打开查看视频
    lookVideo(row) {
      this.$emit("lookVideo", row.cameraid);
    },
    // 弹窗内打开查看监控
    lookCamera(popupData) {
      this.$emit("lookCamera", popupData.PRCD, popupData.NAME);
    },
    // 轮播图弹窗方法
    changeItem(index) {
      this.imgIndex = index;
    },
    // 轮播图弹窗方法
    changeVideoItem(index) {
      this.videoIndex = index;
    },
    //获取工程详情
    async getProjectDetail(prcd) {
      let res = await reservoirLeftStatisticApi({
        statistic: "mapList",
        prcd,
      });
      console.log(res);
      if (res.success) {
        this.popupData = res.data[0];
      }
    },
    //获取工程图片
    async getPhotoDetailList(prcd) {
      this.imgIndex = 0;
      this.photoList = [];
      this.loadingImg = true;
      let res = await reservoirPhotoListApi({
        viewType: "latestAll",
        prcd: prcd,
        fetchAll: true,
      });
      if (res.success) {
        let options = [
          {
            name: `${res.rows[0].damBehind}`,
            url: `${res.rows[0].damBehindFullurl}`,
            smallUrl: `${res.rows[0].damBehindUrl
              ? res.rows[0].damBehindUrl
              : res.rows[0].damBehindFullurl
              }`,
          },
          {
            name: `${res.rows[0].damCrest}`,
            url: `${res.rows[0].damCrestFullurl}`,
            smallUrl: `${res.rows[0].damCrestUrl
              ? res.rows[0].damCrestUrl
              : res.rows[0].damCrestFullurl
              }`,
          },
          {
            name: `${res.rows[0].damFront}`,
            url: `${res.rows[0].damFrontFullurl}`,
            smallUrl: `${res.rows[0].damFrontUrl
              ? res.rows[0].damFrontUrl
              : res.rows[0].damFrontFullurl
              }`,
          },
          {
            name: `${res.rows[0].damToe}`,
            url: `${res.rows[0].damToeFullurl}`,
            smallUrl: `${res.rows[0].damToeUrl
              ? res.rows[0].damToeUrl
              : res.rows[0].damToeFullurl
              }`,
          },
          {
            name: `${res.rows[0].extphoto5}`,
            url: `${res.rows[0].extphoto5Fullurl}`,
            smallUrl: `${res.rows[0].extphoto5Url
              ? res.rows[0].extphoto5Url
              : res.rows[0].extphoto5Fullurl
              }`,
          },
          {
            name: `${res.rows[0].extphoto6}`,
            url: `${res.rows[0].extphoto6Fullurl}`,
            smallUrl: `${res.rows[0].extphoto6Url
              ? res.rows[0].extphoto6Url
              : res.rows[0].extphoto6Fullurl
              }`,
          },
          {
            name: `${res.rows[0].outletDrainageFacilities}`,
            url: `${res.rows[0].outletDrainageFacilitiesFullurl}`,
            smallUrl: `${res.rows[0].outletDrainageFacilitiesUrl
              ? res.rows[0].outletDrainageFacilitiesUrl
              : res.rows[0].outletDrainageFacilitiesFullurl
              }`,
          },
          {
            name: `${res.rows[0].spillway}`,
            url: `${res.rows[0].spillwayFullurl}`,
            smallUrl: `${res.rows[0].spillwayUrl
              ? res.rows[0].spillwayUrl
              : res.rows[0].spillwayFullurl
              }`,
          },
          {
            name: `${res.rows[0].spillwayInletSection}`,
            url: `${res.rows[0].spillwayInletSectionFullurl}`,
            smallUrl: `${res.rows[0].spillwayInletSectionUrl
              ? res.rows[0].spillwayInletSectionUrl
              : res.rows[0].spillwayInletSectionFullurl
              }`,
          },
        ];
        options.map((v) => {
          // 缩略图
          if (v.smallUrl && v.smallUrl != "null") {
            if (v.smallUrl.indexOf("https://file.ygwjg.com/") != -1) {
              v.smallUrl = v.smallUrl + "?imageView2/2/w/300/h/200";
            } else if (v.smallUrl.indexOf("fileOss") !== -1) {
              v.smallUrl = "http://sk.zjwater.com/" + v.smallUrl;
            } else {
              v.smallUrl = "http://sk.zjwater.com/fileUpds/" + v.smallUrl;
            }
          }

          if (v.url.indexOf("fileOss") !== -1) {
            v.url = "http://sk.zjwater.com/" + v.url;
          } else if (v.url && v.url.indexOf("https://file.ygwjg.com/") == -1) {
            v.url = "http://sk.zjwater.com/fileUpds/" + v.url;
          }
        });
        this.photoList = options;
        this.loadingImg = false;
      } else {
        this.loadingImg = true;
      }
    },
    async getFileList(ids) {
      let res = await getFile({
        dataId: ids,
      });
      let photoList = [];
      if (res.list.length) {
        res.list.map((v) => {
          let url = v.filurl;
          let smallUrl = v.filurl;

          if (v.smallPicurl && v.smallPicurl != "null") {
            smallUrl = "http://sk.zjwater.com/fileUpds/" + v.smallPicurl;
          } else if (v.filurl && v.filurl != "null") {
            if (v.filurl.indexOf("https://file.ygwjg.com/") != -1) {
              smallUrl = v.filurl + "?imageView2/2/w/300/h/200";
            } else {
              smallUrl = "http://sk.zjwater.com/fileUpds/" + v.filurl;
            }
          }

          if (
            v.filurl &&
            v.filurl.indexOf("https://file.ygwjg.com/") == -1 &&
            v.filurl.indexOf("/fileUpds/") != -1
          ) {
            url = "https://sk.zjwater.com" + v.filurl;
          }
          photoList.push({
            name: v.filnm,
            url: url,
            smallUrl: smallUrl,
          });
        });
        this.photoList = photoList;
      }
      this.loadingImg = false;
    },

    // 获取工程视频影像
    async getVideoDetailList(prcd) {
      this.videoIndex = 0;
      this.videoList = [];
      this.loadingVideo = true;
      let res = await reservoirVideoListApi({
        prcd: prcd,
        fetchAll: true,
      });
      if (res.success) {
        let arr = [];
        if (res.rows.length) {
          res.rows.map((v) => {
            arr.push(v.videouri);
          });
          this.getVideoFileList(arr.join(","));
        } else {
          this.loadingVideo = false;
        }
      } else {
        this.loadingVideo = false;
      }
    },
    async getVideoFileList(ids) {
      let res = await getFile({
        // dataId: '9D06FE1A368848B6A7E097DC30F32FDE',
        dataId: ids,
      });
      let videoList = [];
      if (res.list.length) {
        res.list.map((v) => {
          let url = v.filurl;
          if (
            v.filurl &&
            v.filurl.indexOf("https://file.ygwjg.com/") == -1 &&
            v.filurl.indexOf("/fileUpds/") != -1
          ) {
            url = "https://sk.zjwater.com" + v.filurl;
          }
          videoList.push({
            name: v.filnm,
            url: url,
          });
        });
        this.videoList = videoList;
      }
      this.loadingVideo = false;
    },

    // 获取摄像头数据
    async getCameraList(prcd) {
      let res = await reservoirCameraListApi({
        prcd: prcd,
        fetchAll: true,
      });
      if (res.success) {
        this.cameraList = res.rows;
      }
    },

    // 获取人员信息
    async getPersonList(prcd) {
      this.loadingPerson = true;
      let res = await reservoirPersonListApi({
        prcd: prcd,
        year: new Date().format("yyyy"),
      });
      if (res.success) {
        this.personData = res.rows[0];
        this.loadingPerson = false;
      } else {
        this.loadingPerson = true;
      }
    },

    //弹窗tabs点击事件
    clickTabs(index, length) {
      if (length) {
        this.popupTabsIndex = index;
      }
    },
    mapClick(value) {
      console.log(this.map, "dadsasdasdas");
      const popupData = value.data.data;
      this.getProjectDetail(popupData.PRCD);
      this.getPhotoDetailList(popupData.PRCD, popupData.NAME);
      this.getVideoDetailList(popupData.PRCD);
      this.getCameraList(popupData.PRCD);
      this.getPersonList(popupData.PRCD);
      this.$olMapUtil.drawHoverFrame(
        this.map,
        value.data,
        this.$refs.pointDialog,
        48,
        "reservoirPointDialog"
      );
      this.$olMapUtil.flyPoint(
        this.map,
        [
          Number(value.data.longitude),
          Number(value.data.latitude) + 0.01,
        ],
        15
      );
      this.pointDialogType = true;
      this.$store.commit("setReservoirStorage", true);
    },
  },
};
</script>

<style lang="scss" scoped>
.popup_map {
  background: rgba($color: #0431a0, $alpha: 0.3);
  @include shadow(#ccc, 0px, 0px, 2px);
  border-radius: 4px;
  position: relative;
  z-index: 99;

  .popup_content {
    color: #333;

    .popup_title {
      color: #000;
      font-size: 18px;
      font-weight: 500;
      padding-bottom: 4px;
    }

    .popup_item {
      padding-top: 16px;
      @include flexbox;
      @include flexAC;
      font-size: 16px;

      p {
        color: #666;
      }

      span {
        color: #000;
      }
    }
  }

  .proj_content {
    width: 770px;
    font-size: 14px;
    border-radius: 4px;
    position: relative;
    background: #fff;
    padding: 12px;
    @include shadow(#ccc, 0px, 0px, 2px);

    .dialog_title {
      background: transparent;
      height: 40px;
      line-height: 40px;
      // padding-left: 16px;
      font-weight: bold;
      width: 100%;
      box-sizing: border-box;

      .title_img_box {
        display: inline-block;
        margin-left: 16px;
        width: 26px;
        height: 26px;
        cursor: pointer;

        .title_img {
          width: 26px;
          height: 26px;
        }
      }

      .dialog__close {
        width: 20px;
        height: 20px;
        background-size: 100% 100%;
        cursor: pointer;
        background: url($imageUrl + "closeImg.png") no-repeat center;
        background-size: 100%;
      }

      span {
        font-size: 24px;
        font-family: youshe;
        color: #333;
      }

      &>i {
        position: absolute;
        top: 25px;
        right: 40px;
        color: #999;
        font-size: 20px;
        cursor: pointer;
        z-index: 10;
        background: url($imageUrl + "closeImg.png") no-repeat center;
        background-size: 100%;
      }
    }

    .proj_top {
      width: 100%;
      @include flexbox;
      @include flexJC(space-between);
      margin-top: 16px;

      .proj_left {
        width: 240px;

        .proj_title {
          min-height: 44px;
          box-sizing: border-box;
          padding-top: 5px;
          // cursor: pointer;
          font-weight: bold;
          font-size: 18px;
          color: rgba(41, 82, 208, 1);
          line-height: 24px;
          // @include flexbox();
          // @include flexAI(center);
        }

        .proj_detail {
          // @include flexbox;
          // @include flexJC(space-between);
          // @include flexflow(row wrap);
          position: relative;

          .qrCode {
            position: absolute;
            top: 0;
            right: 0;
            width: 63px;
            height: 63px;
          }

          .popup_item {
            height: 38px;
            line-height: 38px;
            @include flexbox;
            padding-left: 5px;
            box-sizing: border-box;

            p {
              color: #333;
              font-weight: 600;
            }

            span {
              color: #333;
            }

            &:nth-child(odd) {
              // background: #03338d;
              background: #f4f5f8;
              box-sizing: border-box;
              // border: 1px solid #ccc;
            }
          }
        }
      }

      .proj_right {
        width: calc(100% - 250px);

        .carousel_box {
          height: 300px;

          .carousel_item {
            height: 100%;

            .swiper_img {
              // background: #03338d;
              width: 100%;
              height: 100%;
              background-repeat: no-repeat;
              background-size: auto 100%;
              background-position: center;
              border-radius: 4px;
            }

            .swiper_video {
              width: 100%;
              height: 100%;
              border-radius: 4px;
            }
          }
        }

        .camera_box {
          height: 300px;
          width: 100%;

          img {
            width: 100%;
          }
        }

        .img_nodata {
          height: 300px;
          text-align: center;
          width: 100%;

          img {
            width: 35%;
            margin-top: 20px;
          }

          p {
            font-size: 16px;
            color: #999;
            padding-top: 15px;
          }
        }

        .img_loading {
          height: 300px;
          width: 100%;
          text-align: center;
          box-sizing: border-box;
          padding-top: 90px;

          p {
            font-size: 16px;
            color: #999;
          }
        }

        .img_box {
          position: relative;

          .preview_words {
            text-align: center;
            position: absolute;
            width: 100%;
            bottom: 10px;
            z-index: 9;
            color: #333;
          }
        }

        .proj_tabs {
          width: 100%;
          @include flexbox;
          @include flexAC;
          @include flexJC(center);
          padding-bottom: 10px;

          &>div {
            // width: 80px;
            padding-right: 10px;
            @include flexbox;
            @include flexAC;
            // background: #1641ab;
            border: 1px solid #ccc;
            height: 30px;
            border-radius: 20px;
            cursor: pointer;
            color: #333;
            margin-right: 15px;

            &>div {
              width: 30px;
              height: 30px;
              border-radius: 20px;
              text-align: center;
              // background: #1641ab;
              border: 1px solid #ccc;
              box-sizing: border-box;
              margin-right: 8px;

              img {
                width: 50%;
                margin-top: 7px;
              }
            }
          }

          &>div.notClick {
            cursor: not-allowed;
          }

          &>div.active {
            color: rgba(41, 82, 208, 1);
            font-weight: bold;

            &>div {
              background: rgba(41, 82, 208, 1);
              border: 1px solid rgba(41, 82, 208, 1);
              box-sizing: border-box;
            }
          }
        }
      }
    }

    .proj_bottom {
      .person_list {
        @include flexbox;
        margin-top: 10px;

        .person_item {
          width: 120px;
          box-sizing: border-box;
          padding: 0 5px;
          background: #2952d026;
          border: 1px solid #ccc;
          border-radius: 4px;
          margin-right: 10px;

          &:last-child {
            margin-right: 0;
          }

          .person_item_top {
            @include flexbox;
            padding-top: 6px;
            height: 33px;

            h3 {
              font-size: 14px;
              line-height: 18px;
            }

            h4 {
              font-size: 13px;
              line-height: 16px;
              color: rgba(51, 51, 51, 1);
            }

            img {
              padding-left: 5px;
              width: 26px;
              height: 26px;
            }
          }

          p {
            color: rgba(51, 51, 51, 1);
            font-size: 14px;
            font-weight: 600;
            margin-top: 10px;
          }

          span {
            color: rgba(51, 51, 51, 1);
            display: block;
            font-weight: 600;
            font-size: 14px;
            padding: 4px 0;
          }
        }
      }
    }
  }
}
</style>