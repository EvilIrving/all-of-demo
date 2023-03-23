 <template>
  <div></div>
</template>

<script>
import { localData, sessionData } from "@/plugins/storage";
import { getQueryString } from "@/utils/utils";
import {
  login,
  loginTicketApi,
  loginZzdLoginApi,
  publicKey,
} from "@/api/login";
import { Base64 } from "js-base64";
import { JSEncrypt } from "jsencrypt";
import dd from "gdt-jsapi";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      go: 0,
    };
  },
  async mounted() {
    await this.addRoutesType("normal");
    // await this.addRoutesType("older");
    const go = getQueryString("go");
    if (go) {
      this.go = go;
    }
    const userInfo = sessionData("get", "userInfo");
    if (userInfo && userInfo.accessToken) {
      this.$router.replace("/appIndex");
    } else {
      const type = getQueryString("type");
      const ticket = getQueryString("ticket");
      if (type && type == "dd") {
        this.getAuthCode();
      } else {
        if (ticket) {
          this.loginTicket(ticket);
        } else {
          this.getPublicKey();
        }
      }
    }
  },
  methods: {
    ...mapActions(["addRoutesType"]),
    //保存登录信息，跳转到首页
    async setTokenInfo(res) {
      let opt = res.data;
      opt.timestamp = new Date().getTime();
      await localData("set", "userInfo", opt);
      await sessionData("set", "userInfo", opt);
      if (this.go == 1) {
        this.$router.go(-1);
      } else {
        this.$router.replace("/appIndex");
      }
    },
    //单ticket票据登陆
    async loginTicket(ticket) {
      let res = await loginTicketApi({
        ticket,
        type: routesType ? 2 : 3,
      });
      if (res.code == 0) {
        this.setTokenInfo(res);
      } else {
        this.$router.replace("/noPermissions");
        this.$toast.fail(res.message);
      }
    },
    //专有钉钉获取code
    getAuthCode() {
      let _this = this;
      dd.getAuthCode({})
        .then(async (result) => {
          if (result) {
            _this.loginZzdLogin(result.code);
          }
        })
        .catch((err) => {
          _this.$router.replace("/noPermissions");
        });
    },
    //专有钉钉登录
    async loginZzdLogin(authCode, callback) {
      let res = await loginZzdLoginApi({
        authCode,
      });
      if (res.code == 0) {
        if (res.data.isBind) {
          this.setTokenInfo(res);
        } else {
          this.$router.replace("/loginBind?openid=" + data.openid);
        }
      } else {
        this.$router.replace("/noPermissions");
      }
    },
    //获取加密key
    async getPublicKey() {
      let res = await publicKey();
      if (res.success) {
        this.doLogin(res.key);
      }
    },
    //默认账号密码登录
    async doLogin(key) {
      let password = "ygkj0818";
      if (key) {
        let encryptor = new JSEncrypt();
        encryptor.setPublicKey(key);
        password = encryptor.encrypt(password);
      }
      let opt = {
        username: Base64.encode("admin"),
        password: password,
        isEncryption: true,
      };
      let res = await login(opt);
      if (res.code == 0) {
        this.setTokenInfo(res);
      } else {
        this.$router.replace("/noPermissions");
        this.$toast.fail(res.message);
      }
    },
  },
};
</script>

<style>
</style>