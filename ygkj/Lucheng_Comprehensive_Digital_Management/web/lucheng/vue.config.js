const path = require("path");
const SpritesmithPlugin = require("webpack-spritesmith");
const isProduction = process.env.NODE_ENV === "production";
// 引入等比适配插件
const px2rem = require('postcss-px2rem');
// 配置基本大小
const postcss = px2rem({
  // 基准大小 baseSize，需要和rem.js中相同
  remUnit: 16
})
const { name } = require("./package");
module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? '/synthesizeSystem/' : '/',
  outputDir: 'dist-synthesizeSystem',
  assetsDir: '',
  lintOnSave: true,
  runtimeCompiler: false,
  transpileDependencies: [
    /* string or regex */
  ],
  productionSourceMap: true,
  chainWebpack(config) {
    config.optimization.minimizer('terser').tap((args) => {
      args[0].terserOptions.compress.drop_console = true
      return args
    })
    config.module
      .rule('swf')
      .test(/\.swf$/)
      .use('url-loader')
      .loader('url-loader')
      .options({
        limit: 1024,
        name: 'file/[path][name].[hash:7].[ext]'
      })
  },
  configureWebpack: {
    devtool: isProduction ? '' : 'eval',
    resolve: {
      extensions: ['.js', '.less', '.vue'],
      alias: {
        style: path.resolve(__dirname, 'src/style'),
        assets: path.resolve(__dirname, 'src/assets')
      }
    },
    plugins: [
      // new SpritesmithPlugin({
      //   src: {
      //     cwd: "public/images/sprites",
      //     glob: "*.png",
      //   },
      //   target: {
      //     image: "src/assets/images/sprite.png",
      //     css: [
      //       [
      //         "src/style/sprite.less",
      //         {
      //           format: "base_template"
      //         },
      //       ],
      //       [
      //         "src/style/spriteConfig.less",
      //         {
      //           format: 'config_template'
      //         }
      //       ]
      //     ],
      //   },
      //   apiOptions: {
      //     cssImageRef: "~assets/images/sprite.png",
      //   },
      //   spritesmithOptions: {
      //     padding: 2,
      //   },
      //   customTemplates: {
      //     base_template: function (data) {
      //       var r = '@import "./spriteConfig.less";\n';
      //       r += data.sprites.map(s => {
      //         return `.icon-${s.name}{ .icon-${s.name}() }`
      //       }).join('\n');
      //       return r;
      //     },
      //     config_template: function (data) {
      //       var s_h = data.spritesheet.height;
      //       var s_w = data.spritesheet.width;
      //       var perSprite = data.sprites
      //         .map(function (sprite) {
      //           var width = sprite.width;
      //           var height = sprite.height;
      //           return `
      //               .icon-${sprite.name}() {
      //                   background-image: url(${data.sprites[0].image});
      //                   display: inline-block;
      //                   width: ${width}px;
      //                   height: ${height}px;
      //                   background-position:
      //                       ${(sprite.x / (s_w - width)) *
      //             100 || 0}%
      //                       ${(sprite.y / (s_h - height)) *
      //             100 || 0}%;
      //                   background-size:
      //                       ${(s_w / width) * 100}%
      //                       ${(s_h / height) * 100}%;
      //               }
      //           `;
      //         })
      //         .join("\n");
      //       return perSprite;
      //     }
      //   },
      // }),
    ],
    output: {
      // 把子应用打包成 umd 库格式
      library: `${name}-[name]`,
      libraryTarget: 'umd',
      jsonpFunction: `webpackJsonp_${name}`
    }
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
        prependData: `@import "@/style/mixin.scss";`
      },
      postcss: {
        plugins: [postcss]
      }
    },

    // 为所有的 CSS 及其预处理文件开启 CSS Modules。
    // 这个选项不会影响 `*.vue` 文件。
    modules: false
  },

  // 在生产环境下为 Babel 和 TypeScript 使用 `thread-loader`
  // 在多核机器下会默认开启。
  parallel: require('os').cpus().length > 1,

  // PWA 插件的选项。
  // 查阅 https://github.com/vuejs/vue-docs-zh-cn/blob/master/vue-cli-plugin-pwa/README.md
  pwa: {},

  // 配置 webpack-dev-server 行为。
  devServer: {
    port: 8092,
    disableHostCheck: true,
    overlay: {
      warnings: false,
      errors: true
    },
    headers: {
      'Access-Control-Allow-Origin': '*'
    }
    // proxy: {
    //   '/proxy': {
    //     'target': 'https://117.149.228.59',
    //     'secure': true, // false为http访问，true为https访问
    //     //'changeOrigin': true, // 跨域访问设置，true代表跨域
    //     'pathRewrite': { // 路径改写规则
    //       '^/proxy': '' // 以/proxy/为开头的改写为''
    //     }
    //   }
    // }
  },
  // 三方插件的选项
  pluginOptions: {}
};
