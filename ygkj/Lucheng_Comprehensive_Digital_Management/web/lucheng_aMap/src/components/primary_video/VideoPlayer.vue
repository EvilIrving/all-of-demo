<template>
  <div class="video-player">
    <div id="playWnd" class="playWnd" ref="playWnd" v-if="plugin"></div>
    <div class="down" v-else>
      <a
        href="http://jbfsys.oss-cn-beijing.aliyuncs.com/download/VideoWebPlugin-1564128302790.exe"
        >下载</a
      >
    </div>
  </div>
</template>
<script>
// import { jquery } from "@/plugins/webControl/jquery-1.11.3";
import { JSEncrypt } from "@/plugins/webControl/jsencrypt.min";
import { WebControl } from "@/plugins/webControl/jsWebControl-1.0.0.min";
var oWebControl = null;
//声明公用变量
var initCount = 0;
var pubKey = "";
var that;
export default {
  name: "VideoPlayer",
  props: {
    code: {
      type: String,
      default: "33038200581314000135",
    },
  },
  data() {
    return {
      plugin: true,
    };
  },
  created() {},
  mounted() {
    // console.log("init");
    this.initPlugin();
    this.setWndCover();
    that = this;
  },
  destroyed() {
    if (oWebControl != null) {
      oWebControl.JS_Disconnect().then(
        function () {},
        function () {}
      );
    }
  },
  methods: {
    //   插件初始化
    initPlugin() {
      let _this = this;
      oWebControl = new WebControl({
        szPluginContainer: "playWnd", // 指定容器id
        iServicePortStart: 15900, // 指定起止端口号，建议使用该值
        iServicePortEnd: 15909,
        szClassId: "23BF3B0A-2C56-4D97-9C03-0CB103AA8F11", // 用于IE10使用ActiveX的clsid
        cbConnectSuccess: () => {
          console.log("initPlugin---sucess");
          // 创建WebControl实例成功
          oWebControl
            .JS_StartService("window", {
              // WebControl实例创建成功后需要启动服务
              dllPath: "./VideoPluginConnect.dll", // 值"./VideoPluginConnect.dll"写死
            })
            .then(
              function () {
                // 启动插件服务成功
                oWebControl.JS_SetWindowControlCallback({
                  // 设置消息回调
                  cbIntegrationCallBack: _this.cbIntegrationCallBack,
                });
                var playWndWidth = _this.$refs.playWnd.offsetWidth;
                var playWndHeight = _this.$refs.playWnd.offsetHeight;
                oWebControl
                  .JS_CreateWnd("playWnd", playWndWidth, playWndHeight)
                  .then(function () {
                    //JS_CreateWnd创建视频播放窗口，宽高可设定
                    _this.init(); // 创建播放实例成功后初始化
                  });
              },
              function () {
                // 启动插件服务失败
                oWebControl = null;
              }
            );
        },
        cbConnectError: () => {
          console.log("initPlugin---error");
          // 创建WebControl实例失败
          oWebControl = null;
          this.$message("插件未启动，正在尝试启动，请稍候...");
          WebControl.JS_WakeUp("VideoWebPlugin://"); // 程序未启动时执行error函数，采用wakeup来启动程序
          initCount++;
          if (initCount < 3) {
            setTimeout(function () {
              this.initPlugin();
            }, 3000);
          } else {
            this.$message("插件启动失败，请检查插件是否安装！");
          }
        },
        cbConnectClose: () => {
          console.log("initPlugin---close");
          oWebControl = null;
        },
      });
    },
    // 设置窗口控制回调
    setCallbacks() {
      oWebControl.JS_SetWindowControlCallback({
        cbIntegrationCallBack: this.cbIntegrationCallBack,
      });
    },
    // 推送消息
    cbIntegrationCallBack(oData) {
      // this.$message.warning(JSON.stringify(oData.responseMsg));
    },
    //初始化
    init() {
      // console.log("chishuhua");
      this.getPubKey(() => {
        ////////////////////////////////// 请自行修改以下变量值	////////////////////////////////////
        var appkey = "26996628"; //综合安防管理平台提供的appkey，必填
        var secret = this.setEncrypt("oo2xAQ3uCNZhkM6KFDfg"); //综合安防管理平台提供的secret，必填
        var ip = "122.228.182.206"; //综合安防管理平台IP地址，必填
        var playMode = 0; //初始播放模式：0-预览，1-回放
        var port = 446; //综合安防管理平台端口，若启用HTTPS协议，默认443
        var snapDir = "D:\\SnapDir"; //抓图存储路径
        var videoDir = "D:\\VideoDir"; //紧急录像或录像剪辑存储路径
        var layout = "1x1"; //playMode指定模式的布局
        var enableHTTPS = 1; //是否启用HTTPS协议与综合安防管理平台交互，是为1，否为0
        var encryptedFields = "secret"; //加密字段，默认加密领域为secret
        var showToolbar = 1; //是否显示工具栏，0-不显示，非0-显示
        var showSmart = 1; //是否显示智能信息（如配置移动侦测后画面上的线框），0-不显示，非0-显示
        var buttonIDs =
          "0,16,256,257,258,259,260,512,513,514,515,516,517,768,769"; //自定义工具条按钮
        ////////////////////////////////// 请自行修改以上变量值	////////////////////////////////////
        var _this = this;
        oWebControl
          .JS_RequestInterface({
            funcName: "init",
            argument: JSON.stringify({
              appkey: appkey, //API网关提供的appkey
              secret: secret, //API网关提供的secret
              ip: ip, //API网关IP地址
              playMode: playMode, //播放模式（决定显示预览还是回放界面）
              port: port, //端口
              snapDir: snapDir, //抓图存储路径
              videoDir: videoDir, //紧急录像或录像剪辑存储路径
              layout: layout, //布局
              enableHTTPS: enableHTTPS, //是否启用HTTPS协议
              encryptedFields: encryptedFields, //加密字段
              showToolbar: showToolbar, //是否显示工具栏
              showSmart: showSmart, //是否显示智能信息
              buttonIDs: buttonIDs, //自定义工具条按钮
            }),
          })
          .then(function (oData) {
            // console.log("111111111111");
            var playWndWidth = _this.$refs.playWnd.offsetWidth;
            var playWndHeight = _this.$refs.playWnd.offsetHeight + 20;
            oWebControl.JS_Resize(playWndWidth, playWndHeight); // 初始化后resize一次，规避firefox下首次显示窗口后插件窗口未与DIV窗口重合问题
            // $(".playWnd").css({
            //     "left": 0,
            //     "top": 0
            // });
            // console.log("22222222");
            that.startPreview(that.code);
            // console.log("3333333");
          });
      });
    },
    // 获取公钥
    getPubKey(callback) {
      // console.log(oWebControl,'oWebControloWebControl')
      oWebControl
        .JS_RequestInterface({
          funcName: "getRSAPubKey",
          argument: JSON.stringify({
            keyLength: 1024,
          }),
        })
        .then((oData) => {
          if (oData.responseMsg.data) {
            pubKey = oData.responseMsg.data;
            console.log(pubKey, "-------------getPubKey()----pubKey-----");
            callback();
          }
        });
    },
    // RSA加密
    setEncrypt(value) {
      let encrypt = new JSEncrypt();
      encrypt.setPublicKey(pubKey);
      return encrypt.encrypt(value);
    },
    // 视频预览
    startPreview(IndexCode) {
      console.log("------开始播放-------");
      let cameraIndexCode = IndexCode;
      var streamMode = 0; //主子码流标识：0-主码流，1-子码流
      var transMode = 1; //传输协议：0-UDP，1-TCP
      var gpuMode = 0; //是否启用GPU硬解，0-不启用，1-启用
      var wndId = 0; //播放窗口序号（在2x2以上布局下可指定播放窗口）

      cameraIndexCode = cameraIndexCode.replace(/(^\s*)/g, "");
      cameraIndexCode = cameraIndexCode.replace(/(\s*$)/g, "");
      console.log(cameraIndexCode, "+++");
      oWebControl
        .JS_RequestInterface({
          funcName: "startPreview",
          argument: JSON.stringify({
            cameraIndexCode: cameraIndexCode,
            streamMode: streamMode,
            transMode: transMode,
            gpuMode: gpuMode,
            wndId: wndId,
          }),
        })
        .then(function (oData) {
          console.log("err:", oData);
        });
    },
    setWndCover() {
      let iWidth = document.body.clientWidth;
      let iHeight = document.body.clientHeight;
      let oDivRect = document.getElementById("playWnd").getBoundingClientRect();
      let iCoverLeft = oDivRect.left < 0 ? Math.abs(oDivRect.left) : 0;
      let iCoverTop = oDivRect.top < 0 ? Math.abs(oDivRect.top) : 0;
      let iCoverRight =
        oDivRect.right - iWidth > 0 ? Math.round(oDivRect.right - iWidth) : 0;
      let iCoverBottom =
        oDivRect.bottom - iHeight > 0
          ? Math.round(oDivRect.bottom - iHeight)
          : 0;
      iCoverLeft = iCoverLeft > 700 ? 700 : iCoverLeft;
      iCoverTop = iCoverTop > 400 ? 400 : iCoverTop;
      iCoverRight = iCoverRight > 700 ? 700 : iCoverRight;
      iCoverBottom = iCoverBottom > 400 ? 400 : iCoverBottom;
      if (this.iLastCoverLeft != iCoverLeft) {
        console.log("iCoverLeft: " + iCoverLeft);
        this.iLastCoverLeft = iCoverLeft;
        oWebControl.JS_SetWndCover("left", iCoverLeft);
      }
      if (this.iLastCoverTop != iCoverTop) {
        console.log("iCoverTop: " + iCoverTop);
        this.iLastCoverTop = iCoverTop;
        oWebControl.JS_SetWndCover("top", iCoverTop);
      }
      if (this.iLastCoverRight != iCoverRight) {
        console.log("iCoverRight: " + iCoverRight);
        this.iLastCoverRight = iCoverRight;
        oWebControl.JS_SetWndCover("right", iCoverRight);
      }
      if (this.iLastCoverBottom != iCoverBottom) {
        console.log("iCoverBottom: " + iCoverBottom);
        this.iLastCoverBottom = iCoverBottom;
        oWebControl.JS_SetWndCover("bottom", iCoverBottom);
      }
    },
    hideCamera() {
      if (oWebControl != null) {
        oWebControl.JS_HideWnd();
      }
    },
    showCamera() {
      if (oWebControl != null) {
        oWebControl.JS_ShowWnd();
      }
    },
    destroyCamera() {
      oWebControl.JS_DestroyWnd().then(
        function () {
          // oWebControl 为 WebControl 的对象
          // 销毁插件窗口成功
          // console.log("destory success!");
        },
        function () {
          // 销毁插件窗口失败
        }
      );
    },
  },
  watch: {},
};
</script>
<style lang="scss" scoped>
.playWnd {
  width: 100%;
  height: 100%;
}
.video-player {
  width: 100%;
  height: 100%;
  .divPlugin {
    width: 100%;
    height: 100%;
    color: red;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 18px;
  }
  .down {
    width: 100%;
    height: 100%;
    color: red;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 18px;
    a {
      color: red;
    }
  }
}
</style>
