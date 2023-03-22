const path = require('path');
const resolve = (dir) => path.join(__dirname, '.', dir);
module.exports = {
  chainWebpack: config => {
    config
      .plugin('html')
      .tap(args => {
        args[0].title = 'VueComponents'
        return args
      })
    config.resolve.alias.set('@', resolve('src'))
  },
  css: {
    loaderOptions: {
      scss: {
        prependData: `@import "~@/assets/css/index.scss";`
      }
    },
  },
  devServer: {
    hot: true,
    open: true,
    port: 8848,
    // host: "192.168.31.23"
  }
}