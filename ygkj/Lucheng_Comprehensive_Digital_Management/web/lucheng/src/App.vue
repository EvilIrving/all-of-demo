<template>
  <div id="app">
    <router-view v-if="isRouterAlive" />
  </div>
</template>
<script>
import { login, permissionListApi, publicKey } from '@/api/login';
import { sessionData, localData } from './plugins/storage';
import { Base64 } from "js-base64";
import { JSEncrypt } from "jsencrypt";
export default {
  async created() { 
    let userInfo = sessionData('get', 'userInfo');
    if (userInfo && userInfo.accessToken) {
      await localData('set', 'userInfo', userInfo);
      await this.getPermissionList(userInfo.jobId);
    } else {
      if (process.env.NODE_ENV !== 'production') {
        await this.getPublicKey();
        await this.doLogin();
      }
    }
  },
  provide() {
    return {
      reload: this.reload
    };
  },
  data() {
    return {
      isRouterAlive: true,
      key: ''
    };
  },
  methods: {
    reload() {
      this.isRouterAlive = false;
      this.$nextTick(function () {
        this.isRouterAlive = true;
      });
    },
    async getPublicKey() {
      let res = await publicKey();
      if (res.success) {
        this.key = res.key;
      }
    },
    async doLogin() {
      let password = 'ygkj0818';
      if (this.key) {
        let encryptor = new JSEncrypt();
        encryptor.setPublicKey(this.key);
        password = encryptor.encrypt(password);
      }
      let opt = {
        username: Base64.encode('13858860396'),
        password: password,
        isEncryption: true
      };
      await login(opt).then((res) => {
        if (res.code == 0) {
          let opt = res.data;
          opt.timestamp = new Date().getTime();
          this.getPermissionList(res.data.jobId);
          this.$localData('set', 'userInfo', opt);
        } else {
          this.$message.error(res.message);
        }
      });
    },
    async getPermissionList(jobId) {
      let res = await permissionListApi({
        id: jobId
      });
      if (res.code == 0) {
        this.$localData('set', 'permission', res.data);
      } else {
        this.$message.error('获取功能权限失败');
      }
      // this.loading = false
    }
  }
};
</script>

<style lang="scss">
* {
  user-select: none;
}
body {
  overflow-x: hidden;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif, Microsoft YaHei;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #333;
  height: 100%;
}
//适配拉伸大屏
.is-big-screen {
  transform: scaleX(0.5);
}
</style>
