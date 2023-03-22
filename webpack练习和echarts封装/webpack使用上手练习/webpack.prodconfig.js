const path = require("path")
const HtmlWebpackPlugin = require("html-webpack-plugin")
const { CleanWebpackPlugin } = require("clean-webpack-plugin")

module.exports = {
    mode: 'development',
    devtool: 'inline-cheap-source-map', //线上环境  (开发环境：eval-)cheap-module-source-map 
    cache: false,
    // entry: './src/index.js',
    entry: {
        test: './src/index.js',
        demo: './src/index.js',
    },
    output: {
        publicPath: 'http://cdn.xxx.com/', // 默认在打包后的文件，加上引用前缀
        path: path.resolve(__dirname, 'bundles.js'),
        filename: '[name].js',
        // assetModuleFilename: "img/[name].[hash:6][ext]"  // webpack 5中 设置 模块类型
        // chunkFilename: '[name].[chunkhash].js',
        // crossOriginLoading: 'anonymous'
    },
    resolve: {}, // 提供简化功能
    optimization: { // 优化相关
        splitChunks: {
            cacheGroups: {
                commons: {
                    name: "commons",
                    chunks: "initial"
                }
            }
        }
    },

    module: { // loader
        rules: [
            // file-loader 打包图片
            {
                test: /.(jpg|png|gif)$/,
                // use: 'file-loader',
                use: {
                    loader: 'file-loader',
                    options: {
                        name: '[name]_[hash].[ext]',
                        outputPath: 'images/'
                    }
                },
            },
            // url-loader 将图片打包在文件中
            {
                test: /.(jpg|png|gif)$/,
                // use: 'file-loader',
                use: {
                    loader: 'url-loader',
                    options: {
                        //placeholders: 图片资源放在 images 文件下，name 表示图片原本的名字，[hash:6] 表示取 6 位hash值作为图片名字的一部分，ext 表示后缀名
                        name: 'images/[name]_[hash:6].[ext]',
                        outputPath: 'images/',
                        limit: 2048
                    }
                },
            },
            // 'style-loader', 'css-loader' 打包css文件  sass-loader 处理sass格式样式，可增加兼容性前缀

            {
                test: /.css$/,
                use: [
                    'style-loader',
                    // 'css-loader',
                    {
                        // 使用css-laoder 启用 CSS-Module 
                        loader: 'css-loader',
                        option: {
                            modules: true
                        }
                    },
                    'postcss-loader',
                    'sass-loader'
                ]
            },
            // file-loader 打包字体
            {
                test: /.(eot|svg|ttf|woff)/,
                use: 'file-loader'
            },
            {
                test: /\.js$/,
                exclude: /node-modules/,
                use: {
                    // babel-loader 只是识别js文件需要用loader加载
                    loader: 'babel-loader',
                    options: {
                        // @babel/preset-env ES6到ES5的代码转换
                        presets: [
                            ['@babel/preset-env', {
                                // 仅polyfill使用过的ES6语法 缩小打包体积
                                useBuiltIns: 'usage'
                            }],

                        ]
                    }
                }
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: './src/index.html'
        }),
        new CleanWebpackPlugin()
    ],
    devServer: {
        watchOptions: {
            ignored: /node_modules/,
        },
        proxy: {
            "/baseApi": {
                target: "https://sk.zjwater.com/",
                changeOrigin: true,
                pathRewrite: {
                    "^/baseApi": "",
                },
            },
        },
    },
}