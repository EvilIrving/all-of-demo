<template>
  <div id="app">
    <div class="header-box">
      <div class="flex-box header-title">
        <div class="title">孝源数字水库</div>
        <div class="line"></div>
        <div class="select">请选择</div>
      </div>
      <div class="tab-box flex-box">
        <div class="active">物业化成效</div>
        <div>防洪调度</div>
        <div>抗旱灌溉</div>
        <div>大坝研判</div>
        <div>巡检安防</div>
        <div>应急联动</div>
      </div>
      <div class="flex-box header-user">
        <div class="header-msg">
          <img src="./assets/image/message-icon.png" alt="" />
        </div>
        <div class="header-work">我的工作台</div>
        <div class="header-name">张瑞林</div>
      </div>
    </div>
    <router-view class="router-box"></router-view>
  </div>
</template>

<script>
export default {
  name: "App",
};
</script>

<style lang="scss" scoped>
#app {
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
}
.header-box {
  position: fixed;
  top: 0;
  left: 0;
  height: 61px;
  width: 100%;
  background: url("./assets/image/header-icon.png") no-repeat;
  background-size: 100% 100%;
  display: flex;
  color: #ffffff;
  z-index: 22;
  .header-title {
    width: 312px;
    height: 45px;
    align-items: center;
    background: url("./assets/image/title-icon.png") no-repeat;
    background-size: 100% 100%;
    .title {
      padding-left: 13px;
      font-size: 28px;
      font-weight: 500;
    }
    .line {
      width: 1px;
      height: 31px;
      background: #ffffff;
      margin: 0 16px;
    }
    .select {
      font-size: 14px;
      font-weight: 500;
      color: #bfc4cc;
    }
  }
  .tab-box {
    width: calc(100% - 590px);
    margin-top: 12px;
    justify-content: space-between;
    > div {
      width: 138px;
      height: 35px;
      line-height: 35px;
      padding-left: 50px;
      background: url("./assets/image/tab-icon.png") no-repeat;
      background-size: 100% 100%;
      box-sizing: border-box;
      font-size: 14px;
      font-weight: 500;
      &.active {
        background: url("./assets/image/tab-active-icon.png") no-repeat;
        background-size: 100% 100%;
        color: #000;
      }
    }
  }
  .header-user {
    width: 278px;
    .header-msg {
      width: 39px;
      height: 25px;
      margin: 13px 0 0 12px;
      img {
        width: 100%;
      }
    }
    .header-work {
      width: 130px;
      height: 45px;
      margin: 1px 0 0 12px;
      box-sizing: border-box;
      background: url("./assets/image/work-icon.png") no-repeat;
      background-size: 100% 100%;
      font-size: 12px;
      line-height: 48px;
      text-align: right;
      padding-right: 25px;
    }
    .header-name {
      font-size: 18px;
      font-weight: 500;
      margin-top: 16px;
      margin-left: 12px;
    }
  }
}
.router-box {
  height: 100%;
}
</style>
