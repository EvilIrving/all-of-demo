<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-25 10:10:55
 * @Description: 纳蓄弹窗 
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenMap\popups\components\IrrigationPopup.vue
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
      <div class="body_content">
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
          <p>水库规模：</p>
          <span v-if="popupData.project_scale == '1'">大(1)型</span>
          <span v-if="popupData.project_scale == '2'">大(2)型</span>
          <span v-if="popupData.project_scale == '3'">中型</span>
          <span v-if="popupData.project_scale == '4'">小(1)型</span>
          <span v-if="popupData.project_scale == '5'">小(2)型</span>
        </div> -->
        <div class="left_item ">
          <p>水库库容(万m³)：</p>
          <span>{{popupData.TATOL_STORAGE}}</span>
        </div>
        <div class="left_item bg">
          <p>当前供水量(万m³)：</p>
          <span>{{popupData.supply_total}}</span>
        </div>
        <div class="left_item ">
          <p>灌溉面积(万亩)：</p>
          <span>{{popupData.DIA}}</span>
        </div>
        <div class="left_item bg">
          <p>是否是粮食功能区水库：</p>
          <span>{{popupData.grain_zone === 1? '是' :'否'}}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NxypPopup',
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
  padding: 30px 20px 40px 20px;
  // @include shadow(#ccc, 0px, 0px, 2px);
  // background: url($imageUrl + "sk-popup-bg.png") no-repeat center;
  // background-size: 100% 100%;
  width: 280px;
  // height: 440px;
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
    .body_content {
      width: 100%;
      .small_title {
        font-size: 20px;
        font-weight: 500;
        color: #333;
        // background: url($imageUrl + "small_title.png") no-repeat left 25px;
        // background-size: 8px 8px;
        padding: 17px 0 10px 20px;
        position: relative;
        &:before {
        top: 22px;
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
  }
}
</style>