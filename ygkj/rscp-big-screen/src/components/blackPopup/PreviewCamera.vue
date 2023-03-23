<!--
 * @Author: hanyu
 * @LastEditTime: 2022-09-08 10:13:44
 * @Description: 查看视频监控
 * @FilePath: \rscp-big-screen\src\components\blackPopup\PreviewCamera.vue
-->
<template>
  <div class='preview_bg' element-loading-background="rgba(0, 0, 0, .4)">
    <i class="el-icon-close" @click="closeLookCameraPopup()"></i>
    <p class="title">{{name}}</p>
    <div class="preview_box" @click.stop>
      <div class="camera_list">
        <div :class="['camera_item', index == cameraIndex ? 'active' : '']" v-for="(item, index) in cameraList" :key="index" @click="clickCamera(index)">
          <span>{{item.cameraname}}</span>
          <p class="on" v-if="item.state == 'ON'">在线</p>
          <p class="off" v-else>离线</p>
        </div>
      </div>
      <div class="player_box">
        <live-player id="player" :video-url="cameraUrl" live="true" muted="true" stretch="false"></live-player>
      </div>
    </div>
  </div>
</template>

<script>
import { reservoirCameraListApi } from '@/api/api_reservoir'
import { videoSelectApi, getVideoUrlApi } from '@/api/api_seawall'
export default {
  name: 'PreviewCamera',
  props: {
    name: {
      type: String,
      required: true
    },
    prcd: {
      type: String,
      required: true
    }
  },
  components: {
    
  },
  data () {
    return {
      cameraList: [],
      cameraIndex: 0,
      cameraUrl: ''
    };
  },
  mounted() {
    this.getCameraList()
  },
  methods: {
    async getCameraList(prcd){
      let res = await reservoirCameraListApi({
        prcd: this.prcd,
        fetchAll: true
      })
      if(res.success){
        this.cameraList = res.rows
        this.getCameraToken(this.cameraList[0].cameraid)
      }else{
        this.$message.error('获取摄像头数据失败！')
      }
    },
    async getCameraToken(cameraid) {
      let res = await videoSelectApi({
        cameraid: cameraid,
      });
      if (res.success) {
        let liveAddress = res.data.liveaddress;
        let apiUrlType = 3;
        if (liveAddress == "211.lives9.top:30443") {
          apiUrlType = 1;
        } else if (liveAddress == "170.lives9.top:30443") {
          apiUrlType = 2;
        }
        this.getCameraUrl(
          apiUrlType,
          res.data.serial,
          res.data.code,
          res.data.token
        );
      } else {
        this.$message.error("获取摄像头数据失败！");
      }
    },
    async getCameraUrl(type, serial, code, token) {
      let res = await getVideoUrlApi["url" + type]({
        serial: serial,
        code: code,
        token: token,
      });
      console.log(res, "------");
      if (res && res.WS_FLV) {
       this.cameraUrl = res.WS_FLV
        setTimeout(() => {
          this.playVideo()
        })
      } else {
        this.$message.error("加载视频失败,请稍后再试！");
        this.$parent.closeLookVideoPopup();
      }
    },
    clickCamera(index){
      this.cameraIndex = index
      this.getCameraToken(this.cameraList[index].cameraid)
    },
    playCamera(){
      let that = this
      let player = document.getElementById('player');
      player.getVueInstance().play();
    },
    closeLookCameraPopup(){
      this.$emit('closeLookCameraPopup')
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
    .title{
      height: 60px;
      line-height: 60px;
      font-size: 30px;
      letter-spacing: 3px;
      color: #fff;
      text-align: center;
      padding-top: 15px;
    }
    .preview_box{
      // position: relative;
      width: 80%;
      height: 80%;
      position: absolute;
      top:50%;
      left:50%;
      transform: translate(-50%,-50%);
      border-radius: 4px;
      overflow: hidden;
      background: rgba(0,0,0,.6);
      @include flexbox();
      .camera_list{
        width: 350px;
        height: 100%;
        background: rgba(0,0,0,.2);
        // background: rgb(146, 146, 146);
        .camera_item{
          font-size: 16px;
          color: #ccc;
          padding: 24px 16px;
          cursor: pointer;
          border-radius: 4px;
          &.active{
            color: #666;
            background: #fff;
            // border: 1px solid #999;
          }
          &>span{
            
          }
          &>p{
            float: right;
            &.on{
              color: #41b883;
            }
            &.off{
              color: #f41e2e;
            }
          }
        }
      }
      .player_box{
        width: calc(100% - 350px);
        position: relative;
      }
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