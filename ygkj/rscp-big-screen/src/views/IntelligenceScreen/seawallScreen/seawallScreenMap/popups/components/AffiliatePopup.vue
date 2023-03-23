<!--
 * @Date: 2023-01-05 13:56:58
 * @Author: 
 * @Description: 附属建筑物弹窗
 * @LastEditors: dtb
 * @LastEditTime: 2023-01-05 14:13:06
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\seawallScreenMap\popups\components\AffiliatePopup.vue
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
        <div class="body_left">
          <!-- 工程类型、工程规模、所属海塘 -->
          <p class="small_title">基本信息</p>
          <div class="left_item ">
            <p>工程类型：</p>
            <span>{{popupData.project_category}}</span>
          </div>
          <div class="left_item bg">
            <p>工程规模：</p>
            <span>{{popupData.project_scale}}级{{popupData.project_category}}</span>
          </div>
          <div class="left_item">
            <p>所属海塘：</p>
            <span>{{popupData.htName }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FirstLinePopup',
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
  // background: #fff;
  padding: 30px 20px 40px 20px;
  @include shadow(#ccc, 0px, 0px, 2px);
  background: url($imageUrl + "sk-popup-bg.png") no-repeat center;
  background-size: 100% 100%;
  width: 220px;
  height: 200px;
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
    .body_content {
      width: 100%;
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
  }
}
</style>