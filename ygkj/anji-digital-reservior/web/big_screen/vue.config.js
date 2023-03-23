/*
 * @Author: dengzhao
 * @LastEditTime: 2021-10-11 16:46:10
 * @Description: 
 * @FilePath: \big_screen\vue.config.js
 */
module.exports = {
  publicPath: './',
  outputDir: 'dist',
  assetsDir: '',
  lintOnSave: false,
  runtimeCompiler: false,
  productionSourceMap: false,
  css: {
    //extract: true,
    sourceMap: false,
    loaderOptions: {
      sass: {
        prependData: `@import "@/style/mixin.scss";`
      },
      postcss: {
        plugins: [
          require("postcss-px2rem")({
            // 以设计稿750为例， 750 / 10 = 75
            remUnit: 144,
          }),
        ],
      },
    },
    requireModuleExtension: true
  },
  parallel: require('os').cpus().length > 1,
  devServer: {
    watchOptions: {
      ignored: /node_modules/
    }
  },
}