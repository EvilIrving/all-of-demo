<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-17 15:43:55
 * @Description: 大屏入口页面
 * @FilePath: \rscp-big-screen\src\views\Home.vue
-->
<template>
  <div class="wrap">
    <!-- <div class="ScaleBox" ref="ScaleBox" :style="{ width: width + 'px', height: height + 'px' }"> -->
    <router-view />
    <!-- </div> -->
  </div>
</template>

<script>
export default {
  name: "ScaleBox",
  props: {
    // width: {
    //   type: Number,
    //   default: 1920
    // },
    // height: {
    //   type: Number,
    //   default: 944
    // }
  },
  data() {
    return {
      // scale: null
    };
  },
  mounted() {
    // console.log(window.innerWidth, window.innerHeight);
    // this.setScale();
    // window.addEventListener("resize", this.debounce(this.setScale, 500));
  },
  methods: {
    // getScale() {
    //   const { width, height } = this;
    //   let ww = window.innerWidth / width;
    //   let wh = window.innerHeight / height;
    //   // alert(window.innerWidth)
    //   // alert(window.innerHeight)
    //   return ww < wh ? ww : wh;
    // },
    // setScale() {
    //   this.scale = this.getScale();
    //   console.log(this.scale);
    //   this.$refs.ScaleBox.style.setProperty("--scale", this.scale);
    // },
    // debounce(fn, delay) {
    //   let delays = delay || 500;
    //   let timer;
    //   return function() {
    //     let th = this;
    //     let args = arguments;
    //     if (timer) {
    //       clearTimeout(timer);
    //     }
    //     timer = setTimeout(function() {
    //       timer = null;
    //       fn.apply(th, args);
    //     }, delays);
    //   };
    // }
  },
};
</script>

<style lang="scss" scoped>
// #ScaleBox {
//   --scale: 1;
// }
.wrap {
  background: #eee;
  width: 100%;
  height: 100%;
}
// .ScaleBox {
//   transform: scale(var(--scale)) translate(-50%, -50%);
//   // transform: scale(var(--scale));
//   display: flex;
//   width: 100%;
//   height: 100%;
//   flex-direction: column;
//   transform-origin: 0 0;
//   position: absolute;
//   left: 50%;
//   top: 50%;
//   transition: 0.3s;
//   z-index: 999;
// }
</style>

<style lang="scss">
.screen_wrap {
  background: #ffffff;
  width: 100%;
  height: 100%;
  .drawer_left {
    position: fixed;
    z-index: 10;
    top: 0px;
    // left: 0px;
    width: 400px;
    height: 100%;
    background: #ffffff;
    border-right: 1px solid #cfd8dc;
    box-shadow: 0px 1px 5px rgba(0, 0, 0, 0.1);
    @include transition(all, 0.3s, ease);
    left: -401px;
    // padding: 0px 0px 0px 20px;
    .drawer_left_btn {
      width: 24px;
      height: 68px;
      position: absolute;
      right: -22px;
      top: 50%;
      margin-top: -38px;
      cursor: pointer;
    }
    .map_legend {
      position: absolute;
      bottom: 16px;
      right: -320px;
      background: #fff;
      width: 300px;
      border-radius: 4px;
      .map_legend_list {
        @include flexbox();
        @include flexJC(space-around);
        @include flexAC();
        @include flexflow(row wrap);
        padding: 8px 0;
        .map_legend_item {
          width: 45%;
          padding: 8px 0;
          @include flexbox();
          // @include flexJC(center);
          @include flexAC();
          & > p {
            width: 30px;
            height: 3px;
            margin-right: 8px;
          }
          & > i {
            width: 12px;
            height: 12px;
            border-radius: 6px;
            margin-right: 8px;
            margin-left: 16px;
          }
        }
      }
      .map_legend_title {
        height: 40px;
        @include flexbox();
        @include flexJC(space-between);
        @include flexAC();
        background: #f9f9f9;
        .map_legend_title_left {
          @include flexbox();
          @include flexAC();
          cursor: pointer;
          img {
            margin: 0 6px 0 12px;
          }
        }
        .map_legend_title_right {
          @include flexbox();
          @include flexAC();
          .el-switch {
            width: 36px;
          }
          & > span {
            margin-right: 8px;
          }
          i {
            margin: 0 12px 0 8px;
            color: #999;
            cursor: pointer;
          }
        }
      }
    }
  }
  .drawer_right {
    position: fixed;
    z-index: 10;
    top: 0px;
    bottom: 0px;
    width: 400px;
    height: 100%;
    background: #ffffff;
    border-left: 1px solid #cfd8dc;
    box-shadow: 0px 1px 5px rgba(0, 0, 0, 0.1);
    @include transition(all, 0.3s, ease);
    right: -401px;
    // padding: 0px 0px 0px 20px;
    .map_left {
      z-index: 1002;
    }
    .drawer_right_btn {
      width: 24px;
      height: 68px;
      position: absolute;
      left: -22px;
      top: 50%;
      margin-top: -38px;
      cursor: pointer;
    }
    .right_select {
      position: absolute;
      top: 85px;
      left: -178px;
      width: 158px;
      // height: 383px;
      background: #fff;
      box-shadow: 0 1px 1px 0 rgba(0, 0, 0, 0.06), 0 0 1px 0 rgba(0, 0, 0, 0.06),
        0 2px 4px 1px rgba(0, 0, 0, 0.02);
      .right_select_item {
        text-align: center;
        padding: 16px 0 11px 0;
        cursor: pointer;
        box-sizing: border-box;
        & > p {
          font-size: 14px;
          color: #8c8c8c;
          margin-bottom: 10px;
        }
        & > span {
          & > span {
            font-size: 24px;
            color: #000000;
            font-weight: 500;
            .zuo {
              font-size: 16px;
              font-weight: 500;
            }
          }
        }
        &:hover {
          background: rgba(25, 59, 196, 0.15);
          @include transition(all, 0.5s, ease);
        }
        &.active {
          background: rgba(25, 59, 196, 0.15);
          border: 1px solid #193bc4;
          p {
            color: #193cc5;
          }
          span {
            color: #193cc5;
          }
        }
      }
    }
    .map_btns {
      position: absolute;
      bottom: 16px;
      left: -208px;
      width: 200px;
      @include flexbox();
      @include flexJC(space-between);
      @include flexAI(flex-end);
      .map_btns_ztjb {
        background: rgba(0, 0, 0, 0.5);
        color: #fff;
        border-radius: 2px;
        width: 104px;
        height: 32px;
        text-align: center;
        font-size: 14px;
        line-height: 32px;
        cursor: pointer;
        i {
          font-size: 18px;
          vertical-align: text-top;
          margin-right: 4px;
        }
      }
      .map_btns_tool {
        width: 104px;
        .tool_title {
          background: rgba(0, 0, 0, 0.5);
          color: #fff;
          height: 32px;
          font-size: 14px;
          box-sizing: border-box;
          width: 100%;
          padding: 0 8px;
          cursor: pointer;
          @include flexbox();
          @include flexJC(space-between);
          @include flexAC();
          .el-icon-suitcase-1 {
            font-size: 18px;
          }
        }
        .tool_list {
          .tool_item {
            cursor: pointer;
            background: rgba(0, 0, 0, 0.8);
            color: #fff;
            height: 32px;
            line-height: 32px;
            font-size: 14px;
            text-align: center;
            &:hover {
              background: rgba(0, 0, 0, 0.65);
            }
          }
        }
      }
      .map_btns_zoom {
        & > div {
          cursor: pointer;
          display: inline-block;
          width: 32px;
          height: 32px;
          background: rgba(0, 0, 0, 0.5);
          color: #fff;
          font-size: 18px;
          text-align: center;
          line-height: 32px;
          margin-right: 8px;
        }
      }
    }
  }
  .showRight {
    right: 0;
  }
  .showLeft {
    left: 0;
  }
}
</style>
