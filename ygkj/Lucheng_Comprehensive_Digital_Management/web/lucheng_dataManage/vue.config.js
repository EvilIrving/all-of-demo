'use strict'
const path = require('path')
const SpritesmithPlugin = require("webpack-spritesmith");
const defaultSettings = require('./src/settings.js')

// 引入等比适配插件
const px2rem = require('postcss-px2rem');
// 配置基本大小
const postcss = px2rem({
  // 基准大小 baseSize, 需要和rem.js中相同
  remUnit: 16
})

function resolve(dir) {
  return path.join(__dirname, dir)
}

const name = defaultSettings.title || '数据管理系统' // page title

// If your port is set to 80,
// use administrator privileges to execute the command line.
// For example, Mac: sudo npm run
// You can change the port by the following method:
// port = 9527 npm run dev OR npm run dev --port = 9527
const port = process.env.port || process.env.npm_config_port || 9527 // dev port

// All configuration item explanations can be find in https://cli.vuejs.org/config/
module.exports = {
  /**
   * You will need to set publicPath if you plan to deploy your site under a sub path,
   * for example GitHub Pages. If you plan to deploy your site to https://foo.github.io/bar/,
   * then publicPath should be set to "/bar/".
   * In most cases please use '/' !!!
   * Detail: https://cli.vuejs.org/config/#publicpath
   */
  publicPath: '/dataManage/',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: process.env.NODE_ENV === 'development',
  productionSourceMap: false,
  devServer: {
    port: port,
    // open: true,
    disableHostCheck: true,// 暂时使用
    watchOptions: {
      ignored: /node_modules/
    },
    // overlay: {
    //   warnings: false,
    //   errors: false
    // },
    // proxy: {
    //   [process.env.VUE_APP_BASE_API]: {
    //     target: process.env.VUE_APP_PROXY_TARGET,
    //     changeOrigin: true,
    //     ws: true,
    //     pathRewrite: {
    //       ['^' + process.env.VUE_APP_BASE_API]: ''
    //     }
    //   }
    // }
  },
  configureWebpack: {
    // provide the app's title in webpack's name field, so that
    // it can be accessed in index.html to inject the correct title.
    name: name,
    resolve: {
      alias: {
        '@': resolve('src'),
        '_c': resolve('src/components'),
        '_a': resolve('src/api'),
        '_l': resolve('src/libs')
      }
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
              "src/styles/sprite.less",
              {
                format: "base_template"
              },
            ],
            [
              "src/styles/spriteConfig.less",
              {
                format: 'config_template'
              }
            ]
          ],
        },
        apiOptions: {
          cssImageRef: "../assets/images/sprite.png",
        },
        spritesmithOptions: {
          padding: 2,
        },
        customTemplates: {
          base_template: function (data) {
            var r = '@import "./spriteConfig.less";\n';
            r += data.sprites.map(s => {
              return `.icon-${s.name}{ .icon-${s.name}() }`
            }).join('\n');
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
                            ${(sprite.x / (s_w - width)) *
                  100 || 0}% 
                            ${(sprite.y / (s_h - height)) *
                  100 || 0}%; 
                        background-size: 
                            ${(s_w / width) * 100}% 
                            ${(s_h / height) * 100}%; 
                    }
                `;
              })
              .join("\n");
            return perSprite;
          }
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
        additionalData: `@import "@/styles/mixin.scss";`
      },
      postcss: {
        plugins: [
          postcss
        ]
      }
    },

    // 为所有的 CSS 及其预处理文件开启 CSS Modules。
    // 这个选项不会影响 `*.vue` 文件。
    modules: false
  },
  chainWebpack(config) {
    config.plugins.delete('preload') // TODO: need test
    config.plugins.delete('prefetch') // TODO: need test

    // set svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()

    // set preserveWhitespace
    config.module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap(options => {
        options.compilerOptions.preserveWhitespace = true
        return options
      })
      .end()

    config
      // https://webpack.js.org/configuration/devtool/#development
      .when(process.env.NODE_ENV === 'development',
        config => config.devtool('cheap-source-map')
      )

    config
      .when(process.env.NODE_ENV !== 'development',
        config => {
          config
            .plugin('ScriptExtHtmlWebpackPlugin')
            .after('html')
            .use('script-ext-html-webpack-plugin', [{
              // `runtime` must same as runtimeChunk name. default is `runtime`
              inline: /runtime\..*\.js$/
            }])
            .end()
          config
            .optimization.splitChunks({
              chunks: 'all',
              cacheGroups: {
                libs: {
                  name: 'chunk-libs',
                  test: /[\\/]node_modules[\\/]/,
                  priority: 10,
                  chunks: 'initial' // only package third parties that are initially dependent
                },
                elementUI: {
                  name: 'chunk-elementUI', // split elementUI into a single package
                  priority: 20, // the weight needs to be larger than libs and app or it will be packaged into libs or app
                  test: /[\\/]node_modules[\\/]_?element-ui(.*)/ // in order to adapt to cnpm
                },
                commons: {
                  name: 'chunk-commons',
                  test: resolve('src/components'), // can customize your rules
                  minChunks: 3, //  minimum common number
                  priority: 5,
                  reuseExistingChunk: true
                }
              }
            })
          config.optimization.runtimeChunk('single')
        }
      )
  }
}
