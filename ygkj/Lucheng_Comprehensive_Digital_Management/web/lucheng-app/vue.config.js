const path = require("path");
const SpritesmithPlugin = require("webpack-spritesmith");
const isProduction = process.env.NODE_ENV === "production";
// // 引入等比适配插件
// const px2rem = require("postcss-px2rem");
// // 配置基本大小
// const postcss = px2rem({
//   // 基准大小 baseSize，需要和rem.js中相同
//   remUnit: 16,
// });
const autoprefixer = require("autoprefixer");
const pxtorem = require("postcss-pxtorem");
module.exports = {
  publicPath: "./",
  assetsDir: "",
  outputDir: "build",
  // publicPath: "/lucheng-mobile/",
  // assetsDir: "",
  // outputDir: "dist-lucheng-mobile",
  lintOnSave: false,
  runtimeCompiler: false,
  transpileDependencies: [],
  productionSourceMap: true,
  // 调整内部的 webpack 配置。
  // 查阅 https://github.com/vuejs/vue-docs-zh-cn/blob/master/vue-cli/webpack.md
  chainWebpack: config => {
    config.plugin('html').tap(args => {
      // args[0].title = '鹿城全域数字化系统';
      args[0].title = '鹿城水利';
      return args
    })
  },
  configureWebpack: {
    devtool: isProduction ? "" : "eval",
    resolve: {
      extensions: [".js", ".less", ".vue"],
      alias: {
        style: path.resolve(__dirname, "src/style"),
        assets: path.resolve(__dirname, "src/assets"),
      },
    },
    plugins: [
      new SpritesmithPlugin({
        src: {
          cwd: "public/images/sprites",
          glob: "*.png",
        },
        target: {
          image: "src/assets/images/sprite.png",
          css: [
            [
              "src/style/sprite.less",
              {
                format: "base_template",
              },
            ],
            [
              "src/style/spriteConfig.less",
              {
                format: "config_template",
              },
            ],
          ],
        },
        apiOptions: {
          cssImageRef: "~assets/images/sprite.png",
        },
        spritesmithOptions: {
          padding: 2,
        },
        customTemplates: {
          base_template: function (data) {
            var r = '@import "./spriteConfig.less";\n';
            r += data.sprites
              .map((s) => {
                return `.icon-${s.name}{ .icon-${s.name}() }`;
              })
              .join("\n");
            return r;
          },
          config_template: function (data) {
            var s_h = data.spritesheet.height;
            var s_w = data.spritesheet.width;
            var perSprite = data.sprites
              .map(function (sprite) {
                var width = sprite.width;
                var height = sprite.height;
                return `
                    .icon-${sprite.name}() { 
                        background-image: url(${data.sprites[0].image}); 
                        display: inline-block; 
                        width: ${width}px; 
                        height: ${height}px; 
                        background-position: 
                            ${(sprite.x / (s_w - width)) * 100 || 0}% 
                            ${(sprite.y / (s_h - height)) * 100 || 0}%; 
                        background-size: 
                            ${(s_w / width) * 100}% 
                            ${(s_h / height) * 100}%; 
                    }
                `;
              })
              .join("\n");
            return perSprite;
          },
        },
      }),
    ],
  },

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
          autoprefixer(),
          pxtorem({
            rootValue: 37.5,
            propList: ["*"],
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
    port: 8082,
    disableHostCheck: true,
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
    // proxy: {
    //   "/baseApi": {
    //     target: "xxx",
    //     changeOrigin: true,
    //     pathRewrite: {
    //       "^/baseApi": "",
    //     },
    //   },
    // },
  },

  // 三方插件的选项
  pluginOptions: {},
};
