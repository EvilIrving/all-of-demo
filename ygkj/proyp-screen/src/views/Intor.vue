<!--
 * @Author: dtb
 * @LastEditTime: 2023-02-14 15:57:21
 * @Description: 进入大屏过渡页面
 * @FilePath: \proyp-screen\src\views\Intor.vue
-->
<template>
  <div class="loading_bg">
    <p>正在加载中...</p>
  </div>
</template>

<script>
import { getUserInfoApi } from "api/api_utils";
import { getQueryString } from 'utils/utils.js'
export default {
  name: "Intor",
  props: {},
  components: {},
  data() {
    return {
      phone: ''
    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    async getUserInfo() {
      let res = await getUserInfoApi({
        token: getQueryString('token'),
      });
      if (res.success) {
        this.$localData("set", "userInfo", res.user);
        this.$store.commit("setSelectAdnm", res.user.adnm);
        this.$store.commit("setSelectAdcd", res.user.adcd);
        let type = this.$route.query.type
        switch (type) {
          case 'reservoir':
          case 'seawall':
            this.$router.push("/" + type);
            break;
          default:
            this.$router.push("/");
            break;
        }
      }
    },

  },
};
</script>

<style lang="scss" scoped>
.loading_bg {
  width: 100%;
  height: 100%;
  background: url("../assets/images/loading_bg.png") no-repeat center;
  background-size: cover;
  position: relative;
  p {
    position: absolute;
    bottom: 200px;
    width: 100%;
    text-align: center;
    color: #fff;
    font-size: 22px;
  }
}
</style>
