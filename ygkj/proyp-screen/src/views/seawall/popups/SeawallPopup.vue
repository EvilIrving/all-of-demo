
<template>
  <div class="proj_content">
    <div class="dialog_title">
      <!-- 海塘信息 -->
      <span style="cursor: pointer" @click="lookDetail(seawallBaseInfo)">{{popupData.name}}</span>
      <span style="font-size: 16px" @click="lookDetail(seawallBaseInfo)">{{ popupData.scale }}</span>
      <i class="dialog__close" @click="closePopup()"></i>
    </div>
    <!-- 上部分 -->
    <div class="proj_top">
      <!-- 上部分-左侧 -->
      <div class="proj_left">
        <!-- 内容 -->
        <div class="proj_title">
          <p><span>{{ seawallBaseInfo.cityADNM }} - {{ seawallBaseInfo.countryADNM }}</span></p>
        </div>
        <div class="proj_detail">
          <div class="popup_item">
            <p>海塘类型：</p>
            <span>{{seawallBaseInfo.seawall_ct_type ?? "-"}}</span>
          </div>
          <div class="popup_item">
            <p>海塘形式：</p>
            <span>{{seawallBaseInfo.seawall_pattern ?? "-"}}</span>
          </div>
          <div class="popup_item">
            <p>海塘长度：</p>
            <span>{{seawallBaseInfo.seawall_length ? seawallBaseInfo.seawall_length + "m" : "-"}}</span>
          </div>
          <div class="popup_item">
            <p>安全管理状态：</p>
            <span>{{ seawallBaseInfo.status ?? "-" }}</span>
          </div>
          <div class="popup_item">
            <p>设计潮位：</p>
            <span>{{ seawallBaseInfo.designTideLevel ?? "-" }}</span>
          </div>
          <div class="popup_item">
            <p>实时潮位：</p>
            <span>{{ seawallBaseInfo.realTimeTide ?? "-" }}</span>
          </div>
          <div class="popup_item">
            <p>更新时间：</p>
            <span>{{ seawallBaseInfo.realTime ? seawallBaseInfo.realTime.substring(10,16) : "-" }}</span>
          </div>
        </div>
      </div>
      <!-- 上部分-右侧 -->
      <div class="proj_right">
        <div style="cursor: pointer" v-loading="loadingImg" @click="lookPhotos(photoList)">
          <div v-if="photoList.length" class="img_box">
            <el-carousel ref="carousel" class="carousel_box" indicator-position="none" arrow="always" :autoplay="false" @change="changeItem">
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
      <div class="person_list" v-loading="!loadingPerson">
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
</template>

<script>







import {
  reservoirPhotoListApi,
  reservoirPersonListApi,
} from "api/api_reservoir.js";
import { seawallProIndo } from "api/api_seawall.js";
export default {
  name: "SeawallPopup",
  inject: ["lookPhotos", "lookDetail"],
  props: {
    popupData: {
      type: Object,
      required: true,
    },
    map: {
      requird: true,
      type: Object,
      default: null,
    },
  },
  components: {},
  data() {
    return {
      seawallBaseInfo: {},
      photoList: [],
      imgIndex: 0,
      loadingImg: true,
      personData: {},
      loadingPerson: true,
    };
  },
  computed: {},
  watch: {},
  mounted() {
    if (this.popupData && JSON.stringify(this.popupData) !== "{}") {
      let prcd = this.popupData.prcd ?? this.popupData.PRCD ?? "";
      this.$olMapUtil.flyPoint(
        this.map,
        [Number(this.popupData.longitude), Number(this.popupData.latitude) + 0.01],
        15
      );
      this.getSeawallBaseInfo(prcd);
      this.getPhotoDetailList(prcd);
      this.getPersonList(prcd);
    }
  },
  methods: {
    // 关闭弹窗
    closePopup() {
      this.$store.commit("setClickFeatVal", null);
    },
    // 轮播图弹窗方法
    changeItem(index) {
      this.imgIndex = index;
    },
    // 轮播图弹窗方法
    changeVideoItem(index) {
      this.videoIndex = index;
    },
    // 获取海塘基本信息
    getSeawallBaseInfo(prcd) {
      console.log(prcd);
      seawallProIndo({ prcd }).then(({ data }) => {
        this.seawallBaseInfo = data || {};
      });
    },
    // 获取工程图片
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
  },
};
</script>


<style lang='scss' scoped>
.mask {
  width: 100%;
  background: rgba($color: #0431a0, $alpha: 0.3);
  z-index: 100;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
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
    & > i {
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
        & > div {
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
          & > div {
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
        & > div.notClick {
          cursor: not-allowed;
        }
        & > div.active {
          color: rgba(41, 82, 208, 1);
          font-weight: bold;
          & > div {
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

.left_menu {
  width: 18px;
  position: absolute;
  right: 42.5%;
  top: 44%;
  z-index: 100;
}
.dialog-content {
  height: 650px;
  ::v-deep .primary-dialog {
    .el-dialog__body {
      padding: 0;
    }
  }
}
</style>
<style lang="scss">
.color_detail_popper {
  background: #fff;
  // box-shadow: inset 0px 0px 16px 0px rgba(46, 228, 255, 0.5);
  border: 1px solid #ccc;
  color: #333;
  font-family: none;
  .color_detail_box {
    p {
      padding: 5px 10px;
    }
  }
}
.color_detail_popper[x-placement^="bottom"] .popper__arrow {
  border-bottom-color: #ccc;
}
.color_detail_popper[x-placement^="bottom"] .popper__arrow::after {
  border-bottom-color: #ccc;
}
</style>