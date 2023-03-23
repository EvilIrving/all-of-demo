/*
 * @Author: wqn
 * @Date: 2022-11-22 15:08:54
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-20 13:49:58
 */
const path = require("path");
function resolve(dir) {
  return path.join(__dirname, dir);
}
module.exports = {
  publicPath: "/rscpscreen/",
  outputDir: "rscp-big-screen",
  // CSS 相关选项
  css: {
    extract: false,
    sourceMap: false,
    loaderOptions: {
      sass: {
        prependData: `@import "style/mixin.scss";`,
      },
    },
    requireModuleExtension: true,
  },
  chainWebpack: config => {
    config.resolve.alias
      .set("@", resolve("src"))
      .set("public", resolve("public"))
      .set("src", resolve("src"))
      .set("api", resolve("src/api"))
      .set("fonts", resolve("src/assets/fonts"))
      .set("images", resolve("src/assets/images"))
      .set("geoJson", resolve("src/assets/geoJson"))
      .set("components", resolve("src/components"))
      .set("common", resolve("src/components/common"))
      .set("page", resolve("src/components/page"))
      .set("config", resolve("src/config"))
      .set("style", resolve("src/style"))
      .set("utils", resolve("src/utils"))
      .set("views", resolve("src/views"));
  },
  devServer: {
    watchOptions: {
      ignored: /node_modules/,
    },
    proxy: {
      "/baseApi": {
        //  target: "https://sk.zjwater.com/",
        // target: "http://192.168.2.97:8082/", //张经理
        // target: 'http://192.168.2.111:8385/', //路亚峰
        // target: 'http://192.168.2.57:8080/', //李汪洋
        target: 'http://192.168.2.123:7070/', //吴高峰
        // target: 'http://192.168.2.188:8081/', //王鼎
        changeOrigin: true,
        pathRewrite: {
          "^/baseApi": "",
        },
      },
    },
  },
}