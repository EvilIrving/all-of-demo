<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-25 10:10:08
 * @Description: 除险加固 弹窗
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenMap\popups\components\CxjgPopup.vue
-->
<template>
  <div class="popup_map">
    <div class="dialog_title">
      <span style="cursor:pointer;" @click="lookDetail(popupData)">{{ popupData.NAME }}</span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '1'" v-html="'&nbsp[大(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '2'" v-html="'&nbsp[大(2)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '3'" v-html="'&nbsp[中型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '4'" v-html="'&nbsp[小(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '5'" v-html="'&nbsp[小(2)型]'"></span>
      <i class="el-dialog__close" @click="closePopup()"></i>
    </div>
    <div class="dialog_body">
      <div class="body_left">
        <p class="small_title">基本信息</p>
        <div class="left_item bg">
          <p>行政区划：</p>
          <span>{{popupData.adcdctiy}} - {{popupData.adcdnm}}</span>
        </div>
        <div class="left_item ">
          <p>集雨面积：</p>
          <span>{{ popupData.RCA ? popupData.RCA + "km²" : "-" }}</span>
        </div>
        <div class="left_item bg">
          <p>水库功能：</p>
          <span class="ellipsis">{{ popupData.ACTFUNCMC ? popupData.ACTFUNCMC : "-" }}</span>
        </div>
        <!-- <div class="left_item">
          <p>规模：</p>
          <span>{{popupData.scale}}</span>
        </div> -->
        <div class="left_item">
          <p>最后一次安全鉴定结论：</p>
          <span>{{popupData.SA_APP_RST?popupData.SA_APP_RST:'-'}}</span>
        </div>
        <p class="small_title">除险加固</p>
        <div class="left_item bg">
          <p>除险加固时间：</p>
          <span>{{popupData.R_REF_ST_TM?popupData.R_REF_ST_TM:'-'}}</span>
        </div>
        <div class="left_item">
          <p>完工时间：</p>
          <span>{{popupData.R_REF_COM_TM?popupData.R_REF_COM_TM:'-'}}</span>
        </div>
        <div class="left_item bg">
          <p>蓄水验收时间：</p>
          <span>{{popupData.R_REF_WATER_TM?popupData.R_REF_WATER_TM:'-'}}</span>
        </div>
        <div class="left_item">
          <p>竣工验收时间：</p>
          <span>{{popupData.R_REF_FIN_TM?popupData.R_REF_FIN_TM:'-'}}</span>
        </div>

      </div>
      <div class="body_right">
        <div class="body_right_text">
          <p class="small_title">主要建设内容</p>
          <div v-if="popupData.MCONT != ''" class="text_box">
            <span>{{popupData.MCONT}}</span>
          </div>
          <div v-else class="nodata">
            <img :src="$config.imgUrl + '/noData.png'" alt="">
          </div>
        </div>

        <div style="cursor:pointer;" class="body_bottom_img">
          <p class="small_title">相关照片</p>
          <div class="img_list" v-if="imgList.length" v-viewer>
            <img v-for="(item, index) in imgList" :key="index" :src="item" alt="">
            <!-- <img src="$config.imgUrl + '/000.jpg" alt="">
            <img src="$config.imgUrl + '/001.jpg" alt="">
            <img src="$config.imgUrl + '/002.jpg" alt=""> -->
          </div>
          <div v-else class="nodata">
            <img :src="$config.imgUrl + '/noImg.png'" alt="">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CxjgPopup',
  props: {
    popupData: {
      type: Object,
      required: true
    }
  },
  components: {

  },
  data() {
    return {
      imgList: []
    };
  },
  watch: {
    popupData(val) {
      if (val.mapType == "cxjgPopup") {
        if (val.picture) {
          this.imgList = val.picture.split(',')
        }
      }
    }
  },
  mounted() {
  },
  methods: {
    closePopup() {
      this.$parent.$parent.closeAllOverly()
    },
    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$parent.$parent.$parent.openDetailPopup(row);
    },
  }
}
</script>

<style lang='scss' scoped>
.popup_map {
  z-index: 99;
  background: #fff;
  padding: 30px 40px 40px 40px;
  // @include shadow(#ccc, 0px, 0px, 2px);
  // background: url($imageUrl + "sk-popup-bg.png") no-repeat center;
  // background-size: 100% 100%;
  width: 730px;
  height: 480px;
  border-radius: 4px;
  .dialog_title {
    background: transparent;
    // background: url($imageUrl + "sk-popup-title.png") no-repeat bottom;
    // background-size: 100% auto;
    height: 40px;
    line-height: 40px;
    padding-left: 16px;
    font-weight: bold;
    width: 100%;
    box-sizing: border-box;
    span {
      font-size: 24px;
      font-weight: bold;
      // font-family: youshe;
      color: #333;
    }
    & > i {
      position: absolute;
      top: 20px;
      right: 40px;
      width: 20px;
      height: 20px;
      color: #999;
      font-size: 20px;
      cursor: pointer;
      background: url($imageUrl + "closeImg.png") no-repeat center;
      background-size: 100%;
      z-index: 10;
    }
  }
  .dialog_body {
    width: 100%;
    height: calc(100% - 40px);
    @include flexbox();
    .body_left {
      width: 262px;
      margin-right: 8px;
      .small_title {
        font-size: 18px;
        font-weight: 600;
        color: #333;
        // background: url($imageUrl + 'small_title.png') no-repeat left
        //   25px;
        // background-size: 8px 8px;
        padding: 5px 0 6px 16px;
        position: relative;
        &:before {
          top: 8px;
          left: 0px;
          position: absolute;
          display: block;
          width: 10px;
          height: 10px;
          background: #03338d;
          content: "";
        }
      }

      .left_item {
        height: 38px;
        line-height: 38px;
        @include flexbox;
        padding-left: 5px;
        box-sizing: border-box;
        p {
          min-width: 70px;
          color: #333;
          font-size: 14px;
        }
        span {
          color: #333;
          font-size: 14px;
        }
        img {
          width: 80px;
          height: 71px;
          margin-top: 13px;
        }
        &.bg {
          // background: #03338d;
          box-sizing: border-box;
          background: #f4f5f8;
        }
      }
    }
    .body_right {
      width: calc(100% - 270px);
      height: 100%;
      margin-top: 10px;
      box-sizing: border-box;
      .small_title {
        font-size: 18px;
        font-weight: 600;
        color: #333;
        // background: url($imageUrl + 'small_title.png') no-repeat left
        //   25px;
        // background-size: 8px 8px;
        padding: 5px 0 6px 16px;
        position: relative;
        &:before {
          top: 8px;
          left: 0px;
          position: absolute;
          display: block;
          width: 10px;
          height: 10px;
          background: #03338d;
          content: "";
        }
      }

      .body_right_text {
        padding: 0 16px;
        font-size: 16px;
        color: #333;
        height: 240px;
        position: relative;
        width: 100%;
        box-sizing: border-box;
        .text_box {
          height: 180px;
          overflow: auto;
          background: #f4f5f8;
          span {
            line-height: 20px;
          }
        }
        .nodata {
          width: 100%;
          // height: calc(100% - 50px);
          display: flex;
          justify-content: center;
          align-items: center;
          img {
            margin: 0 auto;
            width: 40%;
          }
        }
      }
      .body_bottom_img {
        width: 100%;
        height: calc(100% - 250px);
        box-sizing: border-box;
        padding: 0 16px;
        .img_list {
          display: flex;
          justify-content: flex-start;
          align-items: center;
          overflow-x: auto;
          overflow-y: hidden;
          img {
            margin-right: 10px;
            width: 33%;
          }
        }
        .nodata {
          width: 100%;
          // height: calc(100% - 50px);
          display: flex;
          justify-content: center;
          align-items: center;
          img {
            margin: 0 auto;
            width: 20%;
          }
        }
      }
    }
  }
}
</style>