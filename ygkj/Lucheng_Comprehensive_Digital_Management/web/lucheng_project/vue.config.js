module.exports = {
  publicPath: '/lucheng/',
  outputDir: 'dist-lucheng',
  // 放置静态资源的地方 (js/css/img/font/...)
  assetsDir: '',
   // 是否在保存的时候使用 `eslint-loader` 进行检查。
  // 有效的值：`ture` | `false` | `"error"`
  // 当设置为 `"error"` 时，检查出的错误会触发编译失败。
  lintOnSave: false,
  runtimeCompiler: false,
  // babel-loader 默认会跳过 node_modules 依赖。
  // 通过这个选项可以显式转译一个依赖。
  transpileDependencies: [/* string or regex */],

  // 是否为生产环境构建生成 source map？
  productionSourceMap: false,
  // 调整内部的 webpack 配置。
  // 查阅 https://github.com/vuejs/vue-docs-zh-cn/blob/master/vue-cli/webpack.md
  // chainWebpack(config) {
  //   config.optimization.minimizer('terser').tap((args) => {
  //     args[0].terserOptions.compress.drop_console = true
  //     return args
  //   })
  // },
  configureWebpack: () => {},

  // CSS 相关选项
  css: {
    // 将组件内的 CSS 提取到一个单独的 CSS 文件 (只用在生产环境中)
    // 也可以是一个传递给 `extract-text-webpack-plugin` 的选项对象
    extract: false,

    // 是否开启 CSS source map？
    sourceMap: false,

    // 为预处理器的 loader 传递自定义选项。比如传递给
    // sass-loader 时，使用 `{ sass: { ... } }`。
    loaderOptions: {
        sass: {
            prependData: `@import "@/style/mixin.scss";`
        }
    },

    // 为所有的 CSS 及其预处理文件开启 CSS Modules。
    // 这个选项不会影响 `*.vue` 文件。
    requireModuleExtension: true
  },

  // 在生产环境下为 Babel 和 TypeScript 使用 `thread-loader`
  // 在多核机器下会默认开启。
  parallel: require('os').cpus().length > 1,

  
  // PWA 插件的选项。
  // 查阅 https://github.com/vuejs/vue-docs-zh-cn/blob/master/vue-cli-plugin-pwa/README.md
  pwa: {},

  // 配置 webpack-dev-server 行为。
  devServer: {
    port: 8083,
    disableHostCheck: true,
    watchOptions: {
      ignored: /node_modules/
    }
    // open: process.platform === 'darwin',
    // host: '0.0.0.0',
    // port: 8080,
    // https: false,
    // hotOnly: false,
    // // 查阅 https://github.com/vuejs/vue-docs-zh-cn/blob/master/vue-cli/cli-service.md#配置代理
    // proxy: null, // string | Object
    // before: app => {}
    // proxy: {}
  },

  // 三方插件的选项
  pluginOptions: {
  }
}
