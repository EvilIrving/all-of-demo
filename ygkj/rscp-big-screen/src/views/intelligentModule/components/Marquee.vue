<!--
 * @Date: 2022-09-02 09:30:05
 * @Author: 
 * @Description: 滚动公告条
 * @LastEditors: dtb
 * @LastEditTime: 2022-09-02 19:21:13
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\components\Marquee.vue
-->
<template>
  <div class="wrap">
    <div ref="box" class="box">
      <div class="marquee" style="background: #12277454;">
        <span style="color: #EFC30A;font-family: youshe;">{{name}}</span> {{detail}}
        <i class="tf_icon"></i>
      </div>
      <div ref="copy" class="copy">
        <div class="marquee" style="background: #12277454;">
          <span style="color: #EFC30A;font-family: youshe;">{{name}}</span> {{detail}}
          <i class="tf_icon"></i>
        </div>
      </div>
    </div>
    <div ref="node" class="node">
      <div class="marquee" style="background: #12277454;">
        <span style="color: #EFC30A;font-family: youshe;">{{name}}</span> {{detail}}
        <i class="tf_icon"></i>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Marquee',
  props: {
    name: {
      type: String,
      default: "",
    },
    detail: {
      type: String,
      default: "",
    },
  },
  data() {
    return {

    }
  },
  methods: {
    move() {
      // 获取文字text 的计算后宽度  （由于overflow的存在，直接获取不到，需要独立的node计算）
      let width = this.$refs.node.getBoundingClientRect().width
      let distance = 0 // 位移距离
      // 设置位移
      this.timer =  setInterval(() => {
        distance = distance - 1
        // 如果位移超过文字宽度，则回到起点
        if (-distance >= width) {
          distance = 16
        }
        this.$refs.box.style.transform = 'translateX(' + distance + 'px)'
      }, 40)
    }
  },
  created() {

  },
  mounted() {
    this.move()
  },
}
</script>
<style scoped lang="scss">
.wrap {
  overflow: hidden;
  position: absolute;
  margin: 0 2%;
  top: 7px;
  left: 130px;
  z-index: 99999;
}

.box {
  background: #123881b8;
  width: 80000%;
}

.box div {
  float: left;
}

.node {
  position: absolute;
  z-index: -999;
  top: -999999px;
}

.marquee {
  
  height: 30px;
  //   overflow: hidden;
  line-height: 30px;
  // background: url($imageUrl + "typhoon_bg.png") no-repeat center;
  // background-size: cover;
  // background: #123881b8;
  //   position: absolute;
  //   margin: 0 auto;
  //   top: 0px;
  //   left: 160px;
  z-index: 100;
  padding-left: 16px;
  font-size: 20px;
  .tfinfo {
    // background: #123881b8;
  }
  .tf_icon {
    width: 30px;
    height: 15px;
    line-height: 30px;
    background: center/80% url($imageUrl + "typhoone_rightbg.png") no-repeat;
    display: inline-block;
    margin-left: 46%;
  }
}
</style>