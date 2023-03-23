<template>
  <div class="amap-login">
    <p>登陆中.....</p>
  </div>
</template>

<script>
import { validateTicket } from "@/api/login.js";
export default {
  name: "AmapLogin",
  async created() {
    let ticket = this.getQueryString("ticket");
    let option = {
      ticket: ticket,
      type: 5,
    };
    await validateTicket(option).then((res) => {
      let opt = res;
      opt.timestamp = new Date().getTime();
      this.$localData("set", "userInfo", res);
      this.$router.replace("/realRain");
      // this.$message({
      //   message: res.message,
      //   type: "success",
      //   duration: 1000,
      // });
    });
  },
  methods: {
    getQueryString(name) {
      return (
        decodeURIComponent(
          (new RegExp("[?|&]" + name + "=" + "([^&;]+?)(&|#|;|$)").exec(
            location.href
          ) || [, ""])[1].replace(/\+/g, "%20")
        ) || null
      );
    },
  },
};
</script>

<style lang="scss" scoped>
.amap-login {
  width: 100%;
  height: 100%;
  padding: 16px;
}
</style>