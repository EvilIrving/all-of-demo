<template>
  <div v-show="visible" class="viedo-call">
    <div class="video-call-content">
      <video id="remoteVideo" class="remote-video" autoPlay playsinline>
        您的浏览器不支持。
      </video>
      <!-- <video id="localVideo" class="local-video" autoPlay playsinline>
        您的浏览器不支持。
      </video> -->
    </div>
    <div class="video-call-action">
      <i class="el-icon-phone" @click="termCall"></i>
    </div>
  </div>
</template>
<script>
import websdk from "easemob-websdk";
import webrtc from "easemob-webrtc";
import config from "@/utils/WebIM/config.js";
let WebIM = {};

export default {
  name: "VideoCall",
  props: {
    visible: { type: Boolean, default: false },
  },
  data() {
    return {
      user: { user: "", pwd: "" },
      inCalling: false,
      inReceiveCall: false,
      inWaitCallAccept: false,

      rtcCall: {},
      caller: "",
      callee: "",
    };
  },
  components: {},
  mounted() {
    this.initWebIM();
    this.initWebRTC();
  },
  methods: {
    /**
     * 登录
     * todo:改成vuex形式
     */
    login(loginu) {
      console.log(loginu);
      this.user.user = loginu.user;
      this.user.pwd = loginu.pwd;
      //回调形式改写Promise
      return new Promise((resolve) => {
        let option = {
          ...this.user,
          apiUrl: config.apiURL,
          appKey: config.appkey,
        };
        WebIM.conn.open(option);
        WebIM.conn.listen({
          onOpened: function (message) {
            //连接成功回调 此处回调只会调用一次
            console.log("登录成功", message);
            resolve({ code: 200, data: { user: loginu } });
          },
        });
      });
    },
    /**
     * 初始化WebIM供WebRTC使用
     * todo:注册成全局
     */
    initWebIM() {
      WebIM = window.WebIM = websdk;
      WebIM.config = config;
      WebIM.conn = new WebIM.connection({
        appKey: WebIM.config.appkey,
        isHttpDNS: WebIM.config.isHttpDNS,
        isMultiLoginSessions: WebIM.config.isMultiLoginSessions,
        https: WebIM.config.https,
        url: WebIM.config.xmppURL,
        apiUrl: WebIM.config.apiURL,
        isAutoLogin: true,
        heartBeatWait: WebIM.config.heartBeatWait,
        autoReconnectNumMax: WebIM.config.autoReconnectNumMax,
        autoReconnectInterval: WebIM.config.autoReconnectInterval,
        isStropheLog: WebIM.config.isStropheLog,
        delivery: WebIM.config.delivery,
      });
      if (!WebIM.conn.apiUrl) {
        WebIM.conn.apiUrl = WebIM.config.apiURL;
      }
      // 注册监听回调
      WebIM.conn.listen({
        // 本机网络连接成功
        onOnline: function () {
          console.log("onOnline 网络已连接");
        },
        // 本机网络掉线
        onOffline: function () {
          console.log("onOffline 网络已断开");
        },
        // 失败回调
        onError: function (message) {
          if (message.type == 0) {
            console.log("请输入账号密码");
          } else if (message.type == 28) {
            console.log("未登陆");
          } else if (
            JSON.parse(message.data.data).error_description == "user not found"
          ) {
            this.$message.error("用户名不存在！");
          } else if (
            JSON.parse(message.data.data).error_description ==
            "invalid password"
          ) {
            console.log("密码无效！");
          } else if (
            JSON.parse(message.data.data).error_description ==
            "user not activated"
          ) {
            this.$message.error("用户已被封禁！");
          }
        },
      });
      WebIM.WebRTC = webrtc;
    },
    /**
     * 初始化WebRTC
     */
    initWebRTC() {
      this.rtcCall = new webrtc.Call({
        connection: WebIM.conn, // WebIM 的链接信息
        mediaStreamConstaints: {
          audio: true,
          video: {
            facingMode: "user",
            /* // 视频分辨率 宽度和高度
            width: {
              exact: 1280,
            },
            height: {
              exact: 720,
            }, */
          },
          /**
           * 修改默认摄像头，可以按照以下设置，不支持视频过程中切换
           * video:{ 'facingMode': "user" } 调用前置摄像头
           * video: { facingMode: { exact: "environment" } } 后置
           */
        },
        //事件监听
        listener: {
          onAcceptCall: (from, options) => {
            console.log("onAcceptCall::", "from: ", from, "options: ", options);
            this.inCalling = true;
            this.inReceiveCall = false;
            this.inWaitCallAccept = false;
          },
          //通过streamType区分视频流和音频流，streamType: 'VOICE'(音频流)，'VIDEO'(视频流)
          onGotRemoteStream: (stream, streamType) => {
            console.log(
              "onGotRemoteStream::",
              "stream: ",
              stream,
              "streamType: ",
              streamType
            );
            let video = document.getElementById("remoteVideo");
            video.srcObject = stream;
          },
          onGotLocalStream: (stream, streamType) => {
            console.log(
              "onGotLocalStream::",
              "stream:",
              stream,
              "streamType: ",
              streamType
            );
            /* let video = document.getElementById("localVideo");
            video.srcObject = stream; */
          },
          /**
           * 收到呼叫监听
           * 暂时直接接通
           */
          onRinging: (caller, streamType) => {
            console.log("onRinging", caller, streamType);
            this.caller = caller;
            this.showCall();
          },
          /**
           * 中断监听
           * @param {String} reason [中断原因]
           */
          onTermCall: (reason) => {
            // "ok"      -> 'HANGUP'     "success" -> 'HANGUP'   "timeout"          -> 'NORESPONSE'
            // "decline" -> 'REJECT'     "busy"    -> 'BUSY'     "failed-transport" -> 'FAIL'
            console.log("onTermCall::", reason);
            if (reason == "ok" || reason == "success" || reason == "hangup") {
              this.close("对方已挂断！");
            } else if (reason == "busy" || reason == "BUSY") {
              this.close("对方繁忙！");
            } else if (reason == "failed-transport" || reason == "fail") {
              this.close("呼叫失败");
            } else if (reason == "decline" || reason == "reject") {
              this.close("对方已拒绝");
            } else {
              this.close("");
            }
          },
          onIceConnectionStateChange: (iceState) => {
            console.log("onIceConnectionStateChange::", "iceState:", iceState);
          },
          /**
           * 通话断网监听
           */
          onNetWorkDisconnect: (endType) => {
            // endType: local || remote, 哪一端断网
            console.log("1v1 onNetWorkDisconnect", endType);
          },
          /**
           * 错误监听
           */
          onError: (e) => {
            console.log(e);
          },
        },
      });
    },
    /**
     * 呼出
     * @param {String} callee 呼叫方名字
     */
    call(callee) {
      console.log("[Make call:]", callee);
      if (callee) {
        this.callee = callee;
        this.rtcCall.caller = this.user.user;
        this.rtcCall.makeVideoCall(callee);
        this.inCalling = false;
        this.inReceiveCall = false;
        this.inWaitCallAccept = true;
      }
    },
    /**
     * 接受呼叫
     */
    accept() {
      this.inCalling = true;
      this.inReceiveCall = false;
      this.inWaitCallAccept = false;
      this.rtcCall.acceptCall();
    },
    /**
     * 拒绝呼叫
     */
    refuse() {
      //复用 相当于别名
      this.termCall();
    },
    /**
     * 处理关闭事件
     */
    close(msg) {
      if (msg != "") {
        console.log(msg);
      }
      this.$emit("close", msg);
      this.inCalling = false;
    },
    /**
     * 收到呼叫显示
     */
    showCall() {
      this.inReceiveCall = true;
      this.$emit("showCall");
    },
    /**
     * 中断呼叫
     */
    termCall() {
      try {
        this.$message("挂断通话");
        this.inCalling = false;
        this.inReceiveCall = false;
        this.inWaitCallAccept = false;
        this.$emit("close");
        this.rtcCall.endCall();
      } catch (e) {
        console.log("endCall error:", e);
      }
    },
  },
};
</script>

<style lang="scss">
.viedo-call {
  width: 100%;
  height: 100%;
  .video-call-content {
    width: 100%;
    height: 100%;
    video {
      width: auto;
      height: 100%;
      display: block;
      margin: 0 auto;
    }
  }
  .video-call-action {
    position: absolute;

    bottom: 5%;
    left: 50%;
    border: 1px solid #f00;
    background: #f00;
    border-radius: 50%;
    padding: 5px;
    font-size: 64px;
    height: 78px;
    width: 78px;
    padding: 6px;
    .el-icon-phone {
      transform: rotate(135deg);
      color: #fff;
    }
  }
}
</style>