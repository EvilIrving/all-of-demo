<!--
 * @Date: 2022-10-12 15:25:02
 * @Author: 
 * @Description: 除险加固弹窗
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2023-01-05 15:25:48
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\seawallScreenMap\popups\components\CxjgPopup.vue
-->

<template>
  <div class="popup_map">
    <div class="dialog_title">
      <span style="cursor:pointer;" @click="lookDetail(popupData)">{{ popupData.name }}</span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)"> {{popupData.scale}}</span>
      <i class="el-icon-close" @click="closePopup()"></i>
    </div>
    <div class="dialog_body">
      <div class="body_content">

      </div>
      <div class="body_left">
        <!-- 海塘名称、工程等级、所在行政区划、海塘长度、所属岸段、最近一次安全鉴定时间、鉴定结论、除险加固开工时间、除险加固完工时间、除险加固竣工时间、 -->
        <p class="small_title">基本信息</p>
        <div class="left_item bg">
          <p>海塘名称：</p>
          <span>{{popupData.name}}</span>
        </div>
        <div class="left_item">
          <p>工程等级：</p>
          <span>{{popupData.scale}}</span>
        </div>
        <div class="left_item ">
          <p>所在行政区划：</p>
          <span>{{popupData.cityADNM}} - {{popupData.countryADNM}}</span>
        </div>
        <div class="left_item bg">
          <p>海塘长度：</p>
          <span>{{popupData.seawall_length + 'km'}}</span>
        </div>
        <div class="left_item">
          <p>所属岸段：</p>
          <span>{{popupData.COASTAL_NAME}}</span>
        </div>
        <p class="small_title">除险加固</p>
        <div class="left_item">
          <p>最近一次安全鉴定时间：</p>
          <span>{{popupData.SA_APP_TM?popupData.SA_APP_TM:'-'}}</span>
        </div>
        <div class="left_item bg">
          <p>鉴定结论：</p>
          <span>{{popupData.SA_APP_RST?popupData.SA_APP_RST:'-'}}</span>
        </div>
        <div class="left_item">
          <p>除险加固开工时间：</p>
          <span>{{popupData.R_REF_ST_TM?popupData.R_REF_ST_TM:'-'}}</span>
        </div>
        <div class="left_item bg">
          <p>除险加固完工时间：</p>
          <span>{{popupData.R_REF_COM_TM?popupData.R_REF_COM_TM:'-'}}</span>
        </div>
        <div class="left_item">
          <p>除险加固竣工时间：</p>
          <span>{{popupData.C_REF_FIN_TM?popupData.C_REF_FIN_TM:'-'}}</span>
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
  // background: #fff;
  padding: 30px 40px 40px 40px;
  @include shadow(#ccc, 0px, 0px, 2px);
  background: url($imageUrl + "sk-popup-bg.png") no-repeat center;
  background-size: 100% 100%;
  width: 730px;
  height: 480px;
  border-radius: 4px;
  .dialog_title {
    background: transparent;
    background: url($imageUrl + "sk-popup-title.png") no-repeat bottom;
    background-size: 100% auto;
    height: 40px;
    line-height: 40px;
    padding-left: 16px;
    font-weight: bold;
    width: 100%;
    box-sizing: border-box;
    span {
      font-size: 24px;
      font-family: youshe;
      color: #fff;
    }
    & > i {
      position: absolute;
      top: 40px;
      right: 40px;
      color: #999;
      font-size: 20px;
      cursor: pointer;
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
        font-size: 20px;
        font-weight: 500;
        color: #ffffff;
        background: url($imageUrl + "small_title.png") no-repeat left 25px;
        background-size: 8px 8px;
        padding: 17px 0 10px 20px;
      }
      .left_item {
        height: 38px;
        line-height: 38px;
        @include flexbox;
        padding-left: 5px;
        box-sizing: border-box;
       p {
          min-width: 70px;
          color: #fff;
          font-size: 14px;
        }
        span {
          color: #fff;
          font-size: 14px;
        }
        img {
          width: 80px;
          height: 71px;
          margin-top: 13px;
        }
        &.bg {
          background: #03338d;
          box-sizing: border-box;
          border: 1px solid #0051b5;
        }
      }
    }
    .body_right {
      width: calc(100% - 270px);
      height: 100%;
      margin-top: 10px;
      box-sizing: border-box;
      .small_title {
        font-size: 20px;
        font-weight: 500;
        color: #ffffff;
        background: url($imageUrl + 'small_title.png') no-repeat left
          25px;
        background-size: 8px 8px;
        padding: 17px 0 10px 20px;
      }

      .body_right_text {
        padding: 0 16px;
        border: 1px solid #0051b5;
        background: #03338d;
        font-size: 16px;
        color: #ffffff;
        height: 240px;
        position: relative;
        width: 100%;
        box-sizing: border-box;
        .text_box{
          height: 180px;
          overflow: auto;
          span{
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