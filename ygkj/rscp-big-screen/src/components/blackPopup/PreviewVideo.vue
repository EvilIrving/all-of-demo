<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-15 10:14:18
 * @Description: 查看视频监控
 * @FilePath: /rscp-big-screen/src/components/blackPopup/PreviewVideo.vue
-->
<template>
  <div class='preview_bg' v-loading="loading" element-loading-background="rgba(0, 0, 0, .4)">
    <i class="el-icon-close" @click="closeLookVideoPopup()"></i>
    <div class="preview_box" v-if="!loading" @click.stop>
      <live-player id="player" v-if="videoUrl" :video-url="videoUrl" live="true" muted="true" stretch="false"></live-player>
    </div>
  </div>
</template>

<script>

export default {
  name: 'PreviewVideo',
  props: {
    loading: {
      type: Boolean,
      default: true
    },
    videoUrl: {
      type: String,
      required: true
    }
  },
  components: {
    
  },
  data () {
    return {
    };
  },
  watch: {
    videoUrl(val){
      let that = this
      setTimeout(()=>{
        that.playVideo()
      })
    }
  },
  mounted() {
    
  },
  methods: {
    playVideo(){
      let that = this
      let player = document.getElementById('player');
      player.getVueInstance().play();
    },
    closeLookVideoPopup(){
      this.$emit('closeLookVideoPopup')
    },
  }
}
</script>

<style lang='scss' scoped>
  .preview_bg{
    position: absolute;
    top: 145px;
    left: 0;
    z-index: 9999;
    width: 100%;
    height: calc(100% - 145px);
    background: rgba(0,0,0,.6);
    .el-icon-close{
      position: absolute;
      top: 20px;
      right: 20px;
      color: #fff;
      font-size: 40px;
      cursor: pointer;
      z-index: 2001;
    }
    .preview_box{
      position: relative;
      width: 80%;
      height: 80%;
      margin: 5% auto;
      border-radius: 4px;
      overflow: hidden;
      background: rgba(0,0,0,.6);
      .loading{
        text-align: center;
        position:absolute;
        left:0;
        top:0;
        color:#FFF;
        background:#000;
        width: 100%;
        height: 100%;
        z-index: 9;
        img{
          position: absolute;
          left:50%;
          top:50%;
          margin-left: -14px;
          margin-top: -14px;
        }
      }
    }
  }
</style>