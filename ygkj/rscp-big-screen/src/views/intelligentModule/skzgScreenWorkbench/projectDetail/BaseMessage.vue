<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-11 11:20:32
 * @Description: 工作台-基础信息
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenWorkbench\projectDetail\BaseMessage.vue
-->
<template>
  <div class="wrap">
    <div class="proj_content">
      <!-- 上部分 -->
      <div class="proj_top">
        <!-- 上部分-左侧 -->
        <div class="proj_left">
          <!-- 内容 -->
          <div class="proj_title" @click="lookDetail(popupData)">
            <img class="title_img" v-if="!popupData.status || popupData.status == '一类坝'" :src="$config.imgUrl+ 'status-green.png'" alt="" />
            <img class="title_img" v-if="popupData.status && popupData.status == '二类坝'" :src="$config.imgUrl+ 'status-yellow.png'" alt="" />
            <img class="title_img" v-if="
                popupData.status && popupData.status.slice(0, 3) == '三类坝'
              " :src="$config.imgUrl+ 'status-red.png'" alt="" />
            <!-- <p>行政区划：</p> -->
            <span>{{ popupData.NAME ? popupData.NAME : '-'}}</span>
            <span v-if="popupData.project_scale == '1'" v-html="'&nbsp[大(1)型]'"></span>
            <span v-if="popupData.project_scale == '2'" v-html="'&nbsp[大(2)型]'"></span>
            <span v-if="popupData.project_scale == '3'" v-html="'&nbsp[中型]'"></span>
            <span v-if="popupData.project_scale == '4'" v-html="'&nbsp [小(1)型]'"></span>
            <span v-if="popupData.project_scale == '5'" v-html="'&nbsp [小(2)型]'"></span>
          </div>
          <div class="proj_detail">
            <!-- <img class="qrCode" :src="$config.imgUrl+ 'qrcode_green.png'" alt=""> -->
            <div class="popup_item">
              <p>水位：</p>
              <span>{{
                  popupData.waterLevel
                    ? popupData.waterLevel.toFixed(2) + 'm'
                    : '- '
                }}
              </span>
              <!-- 括号填写是否上升及数字 -->
              <!-- <div v-if="parseFloat(popupData.waterChange)">
                <span v-html="'&nbsp'"></span>(
                <span>{{
                  popupData.waterChange
                    ? popupData.waterChange.toFixed(2)
                    : popupData.waterChange
                }}</span>
                <span v-if="popupData.waterChange"
                  ><i
                    class="el-icon-top"
                    style="color: green;font-size:16px"
                    v-if="
                      popupData.waterChange > 0 ||
                        popupData.waterChange == null
                    "
                  ></i>
                  <i
                    class="el-icon-bottom"
                    style="color: red;font-size:16px"
                    v-else
                  ></i></span
                >)
              </div> -->
              <!-- 加个箭头 -->
            </div>
            <div class="popup_item">
              <p>时间：</p>
              <span>{{
                popupData.waterleveltm
                  ? popupData.waterleveltm.slice(5, 16)
                  : '-'
              }}</span>
            </div>
            <!-- <div class="popup_item">
              <p>规模：</p>
              <span v-if="popupData.project_scale == '1'">大(1)型</span>
              <span v-if="popupData.project_scale == '2'">大(2)型</span>
              <span v-if="popupData.project_scale == '3'">中型</span>
              <span v-if="popupData.project_scale == '4'">小(1)型</span>
              <span v-if="popupData.project_scale == '5'">小(2)型</span>
            </div> -->
            <div class="popup_item">
              <p>总库容：</p>
              <span>{{
                popupData.TATOL_STORAGE ? popupData.TATOL_STORAGE + '万m³' : '-'
              }}</span>
            </div>
            <div class="popup_item">
              <p>集雨面积：</p>
              <span>{{ popupData.RCA ? popupData.RCA + 'km²' : '-' }}</span>
            </div>
            <div class="popup_item">
              <p>坝型：</p>
              <span>{{ popupData.DTBS ? popupData.DTBS : '-' }}</span>
            </div>

            <!-- <div class="popup_item">
              <p>所在市：</p>
              <span>{{ popupData.cityADNM ? popupData.cityADNM : "-" }}</span>
            </div>
            <div class="popup_item">
              <p>所在县：</p>
              <span>{{
                popupData.countryADNM ? popupData.countryADNM : "-"
              }}</span>
            </div> -->
            <div class="popup_item">
              <p>安全鉴定：</p>
              <span v-if="
                  popupData.status && popupData.status.slice(0, 3) == '三类坝'
                ">{{ popupData.status.slice(0, 3) }}</span>
              <span v-else>{{
                popupData.status ? popupData.status : '-'
              }}</span>
            </div>
            <div class="popup_item" v-if="
                popupData.status && popupData.status.slice(0, 3) == '三类坝'
              ">
              <p>除险加固：</p>
              <span>{{
                popupData.status ? popupData.status.slice(3) : '-'
              }}</span>
            </div>
            <div class="popup_item" v-if="popupData.rightPopType && popupData.rightPopType == 'xsl'">
              <p>蓄水率：</p>
              <span>{{ popupData.xsl ? popupData.xsl : '-' }}</span>
            </div>
            <div class="popup_item" v-if="popupData.rightPopType && popupData.rightPopType == 'kgkn'">
              <p>可纳雨：</p>
              <span>{{
                popupData.rainContains
                  ? popupData.rainContains.toFixed(2) + 'mm'
                  : '0'
              }}</span>
            </div>
            <div class="popup_item" v-if="popupData.rightPopType && popupData.rightPopType == 'kgkn'">
              <p>可纳洪：</p>
              <span>{{
                popupData.knx ? popupData.knx + popupData.knxUnit : '0'
              }}</span>
            </div>

            <div class="popup_item" v-if="popupData.rightPopType && popupData.rightPopType == 'kgkn'">
              <p>可供水：</p>
              <span>{{
                popupData.kgs ? popupData.kgs + popupData.kgsUnit : '0'
              }}</span>
            </div>
            <!-- 预测预警 -->
            <div class="popup_item" v-if="popupData.isYcyj">
              <p>{{ popupData.overName }}：</p>
              <span>{{
                popupData.overValue ? popupData.overValue.toFixed(2) + 'm' : '0'
              }}</span>
            </div>
            <div class="popup_item" v-if="popupData.isYcyj">
              <p>预报水位：</p>
              <span style="color:#ff0000">{{
                popupData.ybWater ? popupData.ybWater.toFixed(2) + 'm' : '0'
              }}</span>
            </div>
          </div>
        </div>
        <!-- 上部分-右侧 -->
        <div class="proj_right">
          <div class="proj_tabs">
            <div :class="popupTabsIndex == 0 ? 'active' : ''" @click="clickTabs(0, 1)">
              <div>
                <img v-if="popupTabsIndex == 0" src="@/assets/images/image0.png" alt="" />
                <img v-else :src="$config.imgUrl+ 'image1.png'" alt="" />
              </div>
              照片
            </div>
            <div :class="[
                popupTabsIndex == 1 ? 'active' : '',
                videoList.length ? '' : 'notClick',
              ]" @click="clickTabs(1, videoList.length)">
              <div>
                <img v-if="popupTabsIndex == 1" :src="$config.imgUrl+ 'yx0.png'" alt="" />
                <img v-else :src="$config.imgUrl+ 'yx1.png'" alt="" />
              </div>
              影像
            </div>
            <div :class="[
                popupTabsIndex == 2 ? 'active' : '',
                cameraList.length ? '' : 'notClick',
              ]" @click="clickTabs(2, cameraList.length)">
              <div>
                <img v-if="popupTabsIndex == 2" src="@/assets/images/video0.png" alt="" />
                <img v-else :src="$config.imgUrl+ 'video1.png'" alt="" />
              </div>
              视频
            </div>
          </div>
          <!-- 照片 -->
          <div v-show="popupTabsIndex == 0" style="cursor: pointer" v-loading="loadingImg" @click="lookPhotos(popupData)">
            <div v-if="photoList.length" class="img_box">
              <el-carousel ref="carousel" class="carousel_box" indicator-position="none" arrow="always" :autoplay="false" @change="changeItem">
                <el-carousel-item class="carousel_item" v-for="(item, index) in photoList" :key="index">
                  <div class="swiper_img" :style="{ backgroundImage: 'url(' + item.smallUrl + ')' }" :title="item.name"></div>
                </el-carousel-item>
              </el-carousel>
              <div class="preview_words">
                <span>{{ imgIndex + 1 }}</span> / {{ photoList.length }}
              </div>
            </div>
            <div v-if="!loadingImg && !photoList.length" class="img_nodata">
              <img :src="$config.imgUrl+ 'noData.png'" alt="" />
              <p>暂无图片</p>
            </div>
            <div v-if="loadingImg" class="img_loading">
              <p>图片加载中...</p>
            </div>
          </div>
          <!-- 影像 -->
          <div v-show="popupTabsIndex == 1" v-loading="loadingVideo">
            <div v-if="videoList.length" class="img_box">
              <el-carousel ref="carousel" class="carousel_box" indicator-position="none" arrow="always" :autoplay="false" @change="changeVideoItem">
                <el-carousel-item class="carousel_item" v-for="(item, index) in videoList" :key="index">
                  <video class="swiper_video" :src="item.url" controls></video>
                </el-carousel-item>
              </el-carousel>
              <div class="preview_words">
                <span>{{ videoIndex + 1 }}</span> / {{ videoList.length }}
              </div>
            </div>
            <div v-if="!loadingVideo && !videoList.length" class="img_nodata">
              <img :src="$config.imgUrl+ 'noData.png'" alt="" />
              <p>暂无视频影像</p>
            </div>
            <div v-if="loadingVideo" class="img_loading">
              <p>视频影像加载中...</p>
            </div>
          </div>
          <!-- 视频 -->
          <div v-show="popupTabsIndex == 2" style="cursor: pointer">
            <div class="camera_box" @click="lookCamera(popupData)">
              <img :src="$config.imgUrl+ 'sk-camera.png'" alt="" />
            </div>
          </div>
        </div>
      </div>
      <!-- 下部分 -->
      <div class="proj_bottom">
        <div class="person_list" v-loading="!loadingPerson">
          <div class="person_item">
            <div class="person_item_top">
              <h4>政府责任人</h4>
              <img :src="$config.imgUrl+ 'sk-person.png'" alt="" />
            </div>
            <p>{{ personData.executiveGovName }}</p>
            <span>{{ personData.executiveGovPhone }}</span>
          </div>
          <div class="person_item">
            <div class="person_item_top">
              <h4>水行政主管部门负责人</h4>
              <img :src="$config.imgUrl+ 'sk-person.png'" alt="" />
            </div>
            <p>{{ personData.executiveWaterName }}</p>
            <span>{{ personData.executiveWaterPhone }}</span>
          </div>
          <div class="person_item">
            <div class="person_item_top">
              <h4>主管部门产权责任人</h4>
              <img :src="$config.imgUrl+ 'sk-person.png'" alt="" />
            </div>
            <p>{{ personData.executiveDepartName }}</p>
            <span>{{ personData.executiveDepartPhone }}</span>
          </div>
          <div class="person_item" v-if="personData.ifDepartment == '有'">
            <!-- <div class="person_item"> -->
            <div class="person_item_top">
              <h4>管理单位责任人</h4>
              <img :src="$config.imgUrl+ 'sk-person.png'" alt="" />
            </div>
            <p>{{ personData.executiveManageName }}</p>
            <span>{{ personData.executiveManagePhone }}</span>
          </div>
          <div class="person_item">
            <div class="person_item_top">
              <h4>技术负责人</h4>
              <img :src="$config.imgUrl+ 'sk-person.png'" alt="" />
            </div>
            <p>{{ personData.technicalName }}</p>
            <span>{{ personData.techinicalPhone }}</span>
          </div>
          <div class="person_item">
            <div class="person_item_top">
              <h4>巡查负责人</h4>
              <img :src="$config.imgUrl+ 'sk-person.png'" alt="" />
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
  reservoirLeftStatisticApi
} from "@/api/api_reservoir";
export default {
  name: '',
  props: {
    reservoirDetail: {
      type: Object,

    }
  },
  components: {},
  data() {
    return {
      popupData: {},
      //轮播图弹窗数据
      photoList: [],
      imgIndex: 0,
      loadingImg: true,
      videoList: [],
      videoIndex: 0,
      loadingVideo: true,
      popupTabsIndex: 0,
      cameraList: [],
      personData: {},
      loadingPerson: true,
    }
  },
  mounted() {
    let PRCD = this.reservoirDetail.prcd
    let NAME = this.reservoirDetail.NAME
    this.getPhotoDetailList(PRCD, NAME);
    this.getVideoDetailList(PRCD);
    this.getCameraList(PRCD);
    this.getPersonList(PRCD);
    this.getNewPopupData()
  },
  methods: {
    async getNewPopupData() {
      let res = await reservoirLeftStatisticApi({
        statistic: "mapList",
        itemCategory: "",
        itemValue: "",
        prcd: this.reservoirDetail.prcd,
        adcd: this.$localData("get", "userInfo").adcd,
      });
      if (res.success && res.data.length !== 0) {
        this.popupData = res.data[0]
      }
    },
    // 弹窗内打开查看图片
    lookPhotos(row) {
      this.$parent.$parent.$parent.$parent.openLookPhotosPopup();
      this.$parent.$parent.$parent.$parent.showPhotosList(this.photoList, row.NAME);
    },

    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$parent.$parent.$parent.$parent.openDetailPopup(row);
    },

    // 轮播图弹窗方法
    changeItem(index) {
      this.imgIndex = index;
    },
    // 轮播图弹窗方法
    changeVideoItem(index) {
      this.videoIndex = index;
    },

    //获取工程图片
    async getPhotoDetailList(prcd, reservoirName) {
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
            smallUrl: `${res.rows[0].damBehindUrl ? res.rows[0].damBehindUrl : res.rows[0].damBehindFullurl}`,
          },
          {
            name: `${res.rows[0].damCrest}`,
            url: `${res.rows[0].damCrestFullurl}`,
            smallUrl: `${res.rows[0].damCrestUrl ? res.rows[0].damCrestUrl : res.rows[0].damCrestFullurl}`,
          },
          {
            name: `${res.rows[0].damFront}`,
            url: `${res.rows[0].damFrontFullurl}`,
            smallUrl: `${res.rows[0].damFrontUrl ? res.rows[0].damFrontUrl : res.rows[0].damFrontFullurl}`,
          },
          {
            name: `${res.rows[0].damToe}`,
            url: `${res.rows[0].damToeFullurl}`,
            smallUrl: `${res.rows[0].damToeUrl ? res.rows[0].damToeUrl : res.rows[0].damToeFullurl}`,
          },
          {
            name: `${res.rows[0].extphoto5}`,
            url: `${res.rows[0].extphoto5Fullurl}`,
            smallUrl: `${res.rows[0].extphoto5Url ? res.rows[0].extphoto5Url : res.rows[0].extphoto5Fullurl}`,
          },
          {
            name: `${res.rows[0].extphoto6}`,
            url: `${res.rows[0].extphoto6Fullurl}`,
            smallUrl: `${res.rows[0].extphoto6Url ? res.rows[0].extphoto6Url : res.rows[0].extphoto6Fullurl}`,
          },
          {
            name: `${res.rows[0].outletDrainageFacilities}`,
            url: `${res.rows[0].outletDrainageFacilitiesFullurl}`,
            smallUrl: `${res.rows[0].outletDrainageFacilitiesUrl ? res.rows[0].outletDrainageFacilitiesUrl : res.rows[0].outletDrainageFacilitiesFullurl}`,
          },
          {
            name: `${res.rows[0].spillway}`,
            url: `${res.rows[0].spillwayFullurl}`,
            smallUrl: `${res.rows[0].spillwayUrl ? res.rows[0].spillwayUrl : res.rows[0].spillwayFullurl}`,
          },
          {
            name: `${res.rows[0].spillwayInletSection}`,
            url: `${res.rows[0].spillwayInletSectionFullurl}`,
            smallUrl: `${res.rows[0].spillwayInletSectionUrl ? res.rows[0].spillwayInletSectionUrl : res.rows[0].spillwayInletSectionFullurl}`,
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
    lookCamera(popupData) {
      this.$parent.$parent.$parent.$parent.openLookCameraPopup(popupData.PRCD, popupData.NAME);
    },

    // 获取人员信息
    async getPersonList(prcd) {
      let res = await reservoirPersonListApi({
        prcd: prcd,
        year: new Date().format("yyyy"),
      });
      if (res.success) {
        this.personData = res.rows[0];
      }
    },

    //弹窗tabs点击事件
    clickTabs(index, length) {
      if (length) {
        this.popupTabsIndex = index;
      }
    },
  }
}
</script>

<style lang="scss" scoped>
.wrap {
  height: calc(100% - 60px);
  width: 100%;
  .proj_content {
    font-size: 14px;
    .proj_top {
      width: 100%;
      @include flexbox;
      @include flexJC(space-between);
      margin-top: 16px;
      .proj_left {
        width: 262px;
        .proj_title {
          min-height: 48px;
          box-sizing: border-box;
          padding-top: 5px;
          cursor: pointer;
          font-weight: bold;
          font-size: 18px;
          color: #69e3ee;
          line-height: 24px;
          // @include flexbox();
          // @include flexAI(center);
          .title_img {
            width: 24px;
            height: 24px;
          }
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
            height: 40px;
            line-height: 40px;
            @include flexbox;
            padding-left: 5px;
            box-sizing: border-box;
            p {
              color: #fff;
            }
            span {
              color: #fff;
            }
            &:nth-child(odd) {
              background: #03338d;
              box-sizing: border-box;
              border: 1px solid #0051b5;
            }
          }
        }
      }
      .proj_right {
        width: calc(100% - 270px);
        .carousel_box {
          height: 300px;
          .carousel_item {
            height: 100%;
            .swiper_img {
              background: #03338d;
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
            color: #fff;
          }
        }
        .proj_tabs {
          width: 100%;
          @include flexbox;
          @include flexAC;
          @include flexJC(space-between);
          & > div {
            width: 33.33%;
            @include flexbox;
            @include flexAC;
            background: #0d2c82;
            border: 1px solid #5081ca;
            height: 48px;
            box-sizing: border-box;
            // border-radius: 20px;
            cursor: pointer;
            color: #fff;
            @include flexJC(center);
            // margin-right: 15px;
            & > div {
              width: 30px;
              height: 30px;
              text-align: center;
              background: #1641ab;
              border: 1px solid #1990ff;
              box-sizing: border-box;
              border-radius: 15px;

              margin-right: 8px;
              img {
                width: 50%;
                margin-top: 7px;
              }
            }
          }
          & > div.notClick {
            cursor: not-allowed;
          }
          & > div.active {
            color: #69e3ee;
            font-weight: bold;
            background: rgba(105, 227, 238, 0.2);
            box-shadow: inset 0px 1px 10px 0px #69e3ee;
            border: 1px solid #69e3ee;
            & > div {
              background: #89e0ec;
              border: 1px solid #89e0ec;
              box-sizing: border-box;
            }
          }
        }
      }
    }
    .proj_bottom {
      height: 100px;
      .person_list {
        height: 100%;
        @include flexbox;
        margin-top: 10px;
        .person_item {
          // width: 120px;
          @include flex(1);
          box-sizing: border-box;
          padding: 0 5px;
          background: linear-gradient(152deg, #0068d4 0%, #03338d 100%);
          border: 1px solid #0051b5;
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
              color: #fff;
            }
            img {
              padding-left: 5px;
              width: 26px;
              height: 26px;
            }
          }
          p {
            color: #fff;
            font-size: 14px;
            font-weight: 500;
            margin-top: 10px;
          }
          span {
            color: #ccc;
            display: block;
            font-size: 14px;
            padding: 4px 0;
          }
        }
      }
    }
  }
}
</style>
