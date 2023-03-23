<!--
 * @Author: wqn
 * @Date: 2022-09-13 14:26:53
 * @LastEditors: wqn
 * @LastEditTime: 2022-09-13 14:26:53
 * @Description: 标题栏组件
-->
<template>
  <div class='chart_title'>
    <div class="title">
      <div class="pre">
        <div class="cube" :class="flag1 === 0 ? 'yellow' : ''"></div>
        <div class="cube" :class="flag1 === 1 ? 'yellow' : ''"></div>
        <div class="cube" :class="flag1 === 2 ? 'yellow' : ''"></div>
        <div class="cube" :class="flag1 === 3 ? 'yellow' : ''"></div>
      </div>
      <div class="main">
        <div class="icon"></div>
        <slot></slot>
      </div>
    </div>
    <div class="line">
      <div class="line_1"></div>
      <div class="line_2"></div>
      <div class="line_3"></div>
      <div class="line_4" :class="flag2 === 0 ? 'yellow' : ''"></div>
      <div class="line_4" :class="flag2 === 1 ? 'yellow' : ''"></div>
      <div class="line_4" :class="flag2 === 2 ? 'yellow' : ''"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'atitle',
  props: {},
  components: {},
  data() {
    return {
      timer: null,
      flag1: 0,
      flag2: 0
    };
  },
  computed: {},
  watch: {},
  created() {
    let d = Math.floor(new Date().getTime() / 500);
    this.flag1 = d % 4;
    this.flag2 = d % 3;
    this.timer = setInterval(() => {
      let d = Math.floor(new Date().getTime() / 500);
      this.flag1 = d % 4;
      this.flag2 = d % 3;
    }, 500);
  },
  mounted() {},
  destroyed() {
    clearInterval(this.timer);
  },
  methods: {}
};
</script>
<style lang='scss' scoped>
@keyframes iconBeat {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(0.8);
  }
  100% {
    transform: scale(1);
  }
}
.chart_title {
  position: relative;
  width: 100%;
  height: 30px;
  .title {
    display: flex;
    width: 100%;
    height: 23px;
    .pre {
      margin-right: 6px;
      .cube {
        width: 5px;
        height: 5px;
        background-color: #3598ff;
        & + .cube {
          margin-top: 1px;
        }
      }
    }
    .main {
      position: relative;
      box-sizing: border-box;
      flex: 1;
      height: 23px;
      background: linear-gradient(270deg, rgba(35, 227, 251, 0) 0%, rgba(35, 157, 239, 0.23) 100%);
      font-size: 18px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #ffffff;
      line-height: 23px;
      padding-left: 19px;
      .icon {
        position: absolute;
        left: 6px;
        top: 0;
        bottom: 0;
        margin: auto;
        width: 0;
        height: 0;
        border-left: 5px solid #fcee27;
        border-top: 5px solid transparent;
        border-bottom: 5px solid transparent;
        animation: iconBeat 1s infinite ease;
        &::after {
          content: '';
          position: absolute;
          display: block;
          top: -5px;
          left: -5px;
          width: 1px;
          height: 10px;
          box-shadow: 0px 0 8px 2px #ffee00;
        }
      }
    }
  }
  .line {
    display: flex;
    align-items: flex-end;
    margin-top: 4px;
    height: 3px;
    .line_1 {
      width: 51px;
      height: 3px;
      background: #026fe0;
      margin-right: 3px;
    }
    .line_2 {
      width: 6px;
      height: 3px;
      background: #ffffff;
      margin-right: 6px;
    }
    .line_3 {
      flex: 1;
      height: 1px;
      background: #026fe0;
      opacity: 0.5;
    }
    .line_4 {
      width: 17px;
      height: 3px;
      background: #026fe0;
      & + .line_4 {
        margin-left: 3px;
      }
    }
  }
  .yellow {
    background-color: #fcee27 !important;
  }
}
</style>