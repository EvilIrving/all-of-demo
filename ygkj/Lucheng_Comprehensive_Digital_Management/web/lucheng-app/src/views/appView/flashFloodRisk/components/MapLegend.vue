<!--
 * @Author: wanxin
 * @LastEditTime: 2022-06-22 18:00:02
 * @Descripttion: 
 * @FilePath: \lucheng-app\src\views\appView\flashFloodRisk\components\MapLegend.vue
-->
<template>
  <div>
    <div class="legend_btn" v-show="isShowLegendbtn" @click="showLegend">
      <img src="@/assets/images/map/legend_close.png" alt="" />
    </div>
    <div class="legend_box" v-show="isShowLegend">
      <div class="legend_title" @click="showLegend">
        <img src="@/assets/images/map/legend_open.png" alt="" />
        图例
        <img
          style="margin-left: 4pt"
          src="@/assets/images/map/close_btn.png"
          alt=""
        />
      </div>
      <div class="legend_content">
        <div>
          <div class="dot"></div>
          <span>正常</span>
        </div>
        <div>
          <div class="dot" style="background-color: #ff0000"></div>
          <span>报警</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isShowLegend: true,
      isShowLegendbtn: false,
    }
  },
  mounted() {},
  methods: {
    showLegend() {
      this.isShowLegend = !this.isShowLegend
      this.isShowLegendbtn = !this.isShowLegendbtn
    },
  },
}
</script>

<style lang="scss" scoped>
.legend_box,
.legend_btn {
  position: absolute;
  left: 12px;
  bottom: 100px;
}
.legend_box {
  width: 90px;
  height: 80px;
  border-radius: 2px;
  .legend_title {
    width: 100%;
    height: 30px;
    // font-size: 10px;
    line-height: 30px;
    background-color: #f9f9f9;
    > img {
      vertical-align: middle;
      margin-left: 8px;
    }
  }
  .legend_content {
    height: calc(100% - 30px);
    padding: 5.5px 8px;
    background-color: #FFFFFF;
    box-sizing: border-box;
    > div {
        margin-bottom: 5px;
    }
    .dot {
      width: 7px;
      height: 7px;
      background-color: #276af0;
      display: inline-block;
      border-radius: 50%;
      margin-right: 6px;
    }
  }
}
</style>