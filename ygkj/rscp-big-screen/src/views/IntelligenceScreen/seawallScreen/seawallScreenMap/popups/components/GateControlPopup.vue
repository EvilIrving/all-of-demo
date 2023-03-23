<!--
 * @Author: wqn
 * @Date: 2023-02-24 11:13:35
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-24 11:28:32
 * @Description: 
-->
<template>
  <div class="popup_map">
    <div class="dialog_title">
      <span>{{ popupData.name}}</span>
      <i class="el-icon-close" @click="closePopup()"></i>
    </div>
    <div class="dialog_body">
      <div class="body_content">
        <div class="body_left">
          <!-- <p class="small_title">基本信息</p> -->
          <div class="left_item ">
            <p>所属海塘：</p>
            <span>{{ popupData.seawallName}}</span>
          </div>
          <div class="left_item bg">
            <p>类型：</p>
            <span>{{ popupData.type}}</span>
          </div>
          <div class="left_item">
            <p>责任人：</p>
            <span>{{ popupData.person_liable }}</span>
          </div>
          <div class="left_item bg">
            <p>联系电话：</p>
            <span>{{ popupData.phone }}</span>
          </div>
          <div class="left_item">
            <p>封堵方式：</p>
            <span>{{ popupData.block_mode }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GateControlPopup',
  props: {
    popupData: {
      type: Object,
      required: true
    }
  },
  components:{},
  data() {
    return {
      
    }
  },
  computed: {},
  watch: {},
  created() {
    
  },
  mounted() {
    
  },
  destroyed() {
    
  },
  methods: {
    closePopup() {
      this.$parent.$parent.closeAllOverly()
    },
  },
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