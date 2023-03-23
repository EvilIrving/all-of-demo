module.exports = {
  // 项目部署的基础路径
  // 我们默认假设你的应用将会部署在域名的根部，
  // 比如 https://www.my-app.com/
  // 如果你的应用时部署在一个子路径下，那么你需要在这里
  // 指定子路径。比如，如果你的应用部署在
  // https://www.foobar.com/my-app/
  // 那么将这个值改为 `/my-app/`
  publicPath: "/screen/",

  // 将构建好的文件输出到哪里
  outputDir: "rscp-big-screen",

  // 放置静态资源的地方 (js/css/img/font/...)
  assetsDir: "",

  // 用于多页配置，默认是 undefined
  //   pages: {
  //     index: {
  //       // 入口文件
  //       entry: 'src/index/main.js',
  //       // 模板文件
  //       template: 'public/index.html',
  //       // 输出文件
  //       filename: 'index.html',
  //       // 页面title
  //       title: 'Index Page'
  //     },
  //     // 简写格式
  //     // 模板文件默认是 `public/subpage.html`
  //     // 如果不存在，就是 `public/index.html`.
  //     // 输出文件默认是 `subpage.html`.
  //     subpage: 'src/subpage/main.js'
  //   },

  // 是否在保存的时候使用 `eslint-loader` 进行检查。
  // 有效的值：`ture` | `false` | `"error"`
  // 当设置为 `"error"` 时，检查出的错误会触发编译失败。
  lintOnSave: false,

  // 使用带有浏览器内编译器的完整构建版本
  // 查阅 https://cn.vuejs.org/v2/guide/installation.html#运行时-编译器-vs-只包含运行时
  runtimeCompiler: false,

  // babel-loader 默认会跳过 node_modules 依赖。
  // 通过这个选项可以显式转译一个依赖。
  transpileDependencies: [
    /* string or regex */
  ],

  // 是否为生产环境构建生成 source map？
  productionSourceMap: false,

  // 调整内部的 webpack 配置。
  // 查阅 https://github.com/vuejs/vue-docs-zh-cn/blob/master/vue-cli/webpack.md
  chainWebpack: () => { },
  configureWebpack: () => { },

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
        prependData: `@import "@/style/mixin.scss";`,
      },
      postcss: {
        plugins: [
          require("postcss-px2rem")({
            // 以设计稿750为例， 750 / 10 = 75
            remUnit: 192,
          }),
        ],
      },
    },

    // 为所有的 CSS 及其预处理文件开启 CSS Modules。
    // 这个选项不会影响 `*.vue` 文件。
    requireModuleExtension: true,
  },

  // 在生产环境下为 Babel 和 TypeScript 使用 `thread-loader`
  // 在多核机器下会默认开启。
  parallel: require("os").cpus().length > 1,

  // PWA 插件的选项。
  // 查阅 https://github.com/vuejs/vue-docs-zh-cn/blob/master/vue-cli-plugin-pwa/README.md
  pwa: {},

  // 配置 webpack-dev-server 行为。
  devServer: {
    watchOptions: {
      ignored: /node_modules/,
    },
    // open: process.platform === 'darwin',
    // host: '0.0.0.0',
    // port: 8080,
    // https: false,
    // hotOnly: false,
    // // 查阅 https://github.com/vuejs/vue-docs-zh-cn/blob/master/vue-cli/cli-service.md#配置代理
    // proxy: null, // string | Object
    // before: app => {}
    proxy: {
      "/baseApi": {
        // target: "https://sk.zjwater.com/",
        // target: "http://192.168.2.210:8080/rscp_war_exploded/", //潘麒帆
        // target: 'http://112.17.127.75:58105/',
        // target: "http://192.168.2.97:8082/", //张经理
        target: 'http://192.168.2.111:8385/', //路亚峰
        // target: 'http://192.168.2.57:8080/', //李汪洋
        // target: 'http://192.168.2.123:7070/', //吴高峰
        // target: 'http://192.168.2.188:8081/', //王鼎
        changeOrigin: true,
        pathRewrite: {
          "^/baseApi": "",
        },
      },
    },
  },
  // 三方插件的选项
  pluginOptions: {},
};
