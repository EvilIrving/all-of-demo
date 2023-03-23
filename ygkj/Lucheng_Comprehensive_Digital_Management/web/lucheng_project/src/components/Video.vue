<!--
 * @Date: 2021-12-09 17:37:38
 * @Author: 
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2021-12-09 17:40:10
 * @FilePath: \lucheng_project\src\components\Video.vue
-->
<template>
  <div class="video_box">
    <video id="myVideo" class="video-js">
      <source :src="src" type="video/mp4">
    </video>
  </div>
</template>

<script>
export default {
  props: {
    src: {
      type: String,
      require: true,
      default: '//vjs.zencdn.net/v/oceans.mp4'
    }
  },
  data() {
    return {

    };
  },
  computed: {

  },
  created() {

  },
  mounted() {
    this.initVideo();
  },
  watch: {

  },
  methods: {
    initVideo() {
      //初始化视频方法
      let myPlayer = this.$video(myVideo, {
        //确定播放器是否具有用户可以与之交互的控件。没有控件，启动视频播放的唯一方法是使用autoplay属性或通过Player API。
        controls: true,
        //自动播放属性,muted:静音播放
        autoplay: "muted",
        //建议浏览器是否应在<video>加载元素后立即开始下载视频数据。
        preload: "auto",
        //设置视频播放器的显示宽度（以像素为单位）
        width: "800px",
        //设置视频播放器的显示高度（以像素为单位）
        height: "400px"
      });
    }
  },
  components: {

  },
};
</script>

<style scoped lang="scss">
.video_box {
  width: 100%;
  height: 100%;
}
</style>
