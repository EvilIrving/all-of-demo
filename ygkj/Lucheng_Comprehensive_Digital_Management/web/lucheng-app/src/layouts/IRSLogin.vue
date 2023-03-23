 <template>
  <div></div>
</template>

<script>
import { localData, sessionData } from "@/plugins/storage";
import { getQueryString } from "@/utils/utils";
import { zwwLoginApi, zlbLoginApi } from "@/api/login";
import zwUtil, { initZwlog } from "../utils/zwUtil";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      go: 0,
      mobileAppHref:
        "https://puser.zjzwfw.gov.cn/sso/mobile.do?action=oauth&scope=1",
      mobileAlipay:
        "https://puser.zjzwfw.gov.cn/sso/alipay.do?action=ssoLogin&citycode=",
      servicecode: "BCDSGA_35a21689670e02112cc3608cf7acc04b",
    };
  },
  async mounted() {
    this.setViewType("irs");
    const go = getQueryString("go");
    if (go) {
      this.go = go;
    }
    this.excuteBridge()
  },
  methods: {
    //初始化jsbridge 设置窗口标题
    excuteBridge() {
      const _this = this;
      ZWJSBridge.onReady(() => {
        ZWJSBridge.setTitle({
          title: "鹿城水利",
        })
          .then((result) => {
            _this.login()
          })
          .catch((error) => {
          });
      });
    },
    login() {
      const userInfo = sessionData("get", "userInfo");
      if (userInfo && userInfo.accessToken) {
        this.$router.replace("/IRSAppIndex");
      } else {
        const sUserAgent = window.navigator.userAgent.toLowerCase();
        const ticketId = getQueryString("ticketId");
        const mobileAlipay =
          sUserAgent.indexOf("miniprogram") > -1 &&
          sUserAgent.indexOf("alipay") > -1;
        if (ticketId) {
          this.zwwLogin(ticketId);
        } else {
          const ticket = getQueryString("ticket");
          if (!ticket) {
            let gotoUrl = encodeURIComponent(
              "https://mapi.zjzwfw.gov.cn/web/mgop/gov-open/zj/2002276620/reserved/index.html"
            );
            if (mobileAlipay) {
              window.location.href = `${this.mobileAlipay}&servicecode=${this.servicecode}&goto=${gotoUrl}&redirectUrl=`;
            } else {
              window.location.href = `${this.mobileAppHref}&servicecode=${this.servicecode}&goto=${gotoUrl}&redirectUrl=`;
            }
          } else {
            this.zwwLogin(ticket, 1);
          }
        }
      }
    },
    ...mapActions(["addRoutesType", "setViewType"]),
    //保存登录信息，跳转到首页
    async setTokenInfo(res) {
      const opt = res.data;
      opt.timestamp = new Date().getTime();
      zwUtil.initZwlog(opt.id);
      await localData("set", "userInfo", opt);
      await sessionData("set", "userInfo", opt);
      const _this = this;
      if (this.go == 1) {
        this.$router.go(-1);
      } else {
        ZWJSBridge.getUiStyle().then(async (res) => {
          if (res.uiStyle === "elder") {
            await _this.addRoutesType("older");
            await _this.$router.replace("/appIndex");
          } else {
            await _this.addRoutesType("normal");
            await _this.$router.replace("/IRSAppIndex");
          }
        });
      }
    },
    async zwwLogin(ticket, type) {
      let res = null;
      if (type == 1) {
        res = await zwwLoginApi({ ticketId: ticket });
      } else {
        res = await zlbLoginApi({ ticketId: ticket });
      }
      if (res.code == 0) {
        this.setTokenInfo(res);
      } else {
        this.$toast.fail(res.message);
        this.$router.replace("/noPermissions");
      }
    },
  },
};
</script>

<style>
</style>