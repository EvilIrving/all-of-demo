<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-25 10:09:55
 * @Description: 水库弹窗
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenMap\popups\components\AqjdPopup.vue
-->
<template>
  <div class="popup_map">
    <div class="dialog_title">
      <span style="cursor:pointer;" @click="lookDetail(popupData)">{{ popupData.name }}</span>
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
          <span>{{popupData.cityADNM}} - {{popupData.countryADNM}}</span>
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
          <span v-if="popupData.project_scale == '1'">大(1)型</span>
          <span v-if="popupData.project_scale == '2'">大(2)型</span>
          <span v-if="popupData.project_scale == '3'">中型</span>
          <span v-if="popupData.project_scale == '4'">小(1)型</span>
          <span v-if="popupData.project_scale == '5'">小(2)型</span>
        </div> -->
        <p class="small_title">安全鉴定内容</p>
        <div class="left_item bg">
          <p>工程竣工时间：</p>
          <span>{{popupData.COMPLETE_TIME}}</span>
        </div>
        <div class="left_item">
          <p>最后一次安全鉴定时间：</p>
          <span>{{popupData.SA_APP_TM}}</span>
        </div>
        <div class="left_item bg">
          <p>鉴定承担单位：</p>
          <span>{{popupData.appraisal_Unit}}</span>
        </div>
        <div class="left_item">
          <p>鉴定审定单位：</p>
          <span>{{popupData.appraisal_Unit}}</span>
        </div>
         
        <div class="left_item">
          <p>结论：</p>
          <img v-if="popupData.SA_APP_RST == '一类坝'" :src="$config.imgUrl +'1lei.png'" alt="">
          <img v-if="popupData.SA_APP_RST == '二类坝'" :src="$config.imgUrl +'2lei.png'" alt="">
          <img v-if="popupData.SA_APP_RST == '三类坝'" :src="$config.imgUrl +'3lei.png'" alt="">
        </div>
      </div>
      <!-- <div class="body_right">
        <iframe src="https://www.baidu.com" frameborder="0"></iframe>
      </div> -->
    </div>
  </div>
</template>

<script>
export default {
  name: 'AqjdPopup',
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
      //轮播图弹窗数据

    };
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
  // background: url($imageUrl + "sk-popup-bg.png") no-repeat
  //   center;
  // background-size: 100% 100%;
  height: 480px;
  border-radius: 4px;
  .dialog_title {
    background: transparent;
    // background: url($imageUrl + "sk-popup-title.png")
    //   no-repeat bottom;
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
          background: #f4f5f8;
          box-sizing: border-box;
        }
      }
    }
    .body_right {
      width: calc(100% - 270px);
      padding-top: 5px;
      iframe {
        width: 100%;
        height: 100%;
      }
    }
  }
}
</style>