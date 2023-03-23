<template>
  <!-- 
    created by niujinxi at 20220317
    In your page,you set a container,give an id and set height when you use this component.
    And if you want to change scrollbar's style,just change .scrollContainer's style in your page.That's all..
-->
  <vue-seamless-scroll style="height: 100%" :data="list" :class-option="defaultOption">
    <slot />
  </vue-seamless-scroll>
</template>

<script>
  import vueSeamlessScroll from "vue-seamless-scroll";
  export default {
    name: "ScrollList",
    props: {
      list: {
        type: Array,
        default: () => {
          return [];
        },
      },
      className: {
        type: String,
        default: "",
      },
    },
    components: {
      vueSeamlessScroll,
    },
    computed: {
      defaultOption() {
        return {
          step: 0.8, // 数值越大速度滚动越快
          limitMoveNum: this.list.length, // 开始无缝滚动的数据量 this.dataList.length
          hoverStop: true, // 是否开启鼠标悬停stop
          direction: 1, // 0向下 1向上 2向左 3向右
          openWatch: true, // 开启数据实时监控刷新dom
          singleHeight: 48, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
          singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
          waitTime: 500, // 单步运动停止的时间(默认值1000ms)
        };
      },
    },
    mounted() {
      let _this = this
      this.$nextTick(() => {
        setTimeout(() => {
          let dom = document.getElementsByClassName(_this.className);
          let arr = Array.from(dom);
          arr.forEach((item, index) => {
            if (index % 2 === 0) {
              item.style.background = "rgba(0,186,255,0.2)";
            }
          });
        }, 0);
      });
    },
  };
</script>

