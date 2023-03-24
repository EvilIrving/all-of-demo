// 报错 CleanWebpackPlugin is not a constructor 
// const  CleanWebpackPlugin  = require("clean-webpack-plugin")

const { CleanWebpackPlugin } = require("clean-webpack-plugin")
const HTMLWebpackPlugin = require("html-webpack-plugin")
// const eslintPlugin = require('eslint-webpack-plugin')
const minicss = require('mini-css-extract-plugin')
const minimizer = require("css-minimizer-webpack-plugin")
// const myLoader = require('./mycssloader/index.js')

let pluginArr = [
    // new eslintPlugin(),
    new minimizer(),
    new CleanWebpackPlugin(),
    new HTMLWebpackPlugin({
        template: "./index.html",
        filename: 'index[hash:6].html',
        chunks: ["menu"],
        minify: {
            collapseWhitespace: false,
            removeComments: false,
            removeAttributeQuotes: true
        },
        // inject: body|true,head|false
    }),
    new HTMLWebpackPlugin({
        template: "./index.html",
        filename: 'index2[hash:6].html',
        chunks: ["start"],
        minify: {
            collapseWhitespace: false,
            removeComments: false,
            removeAttributeQuotes: true
        }
    }),
]

function hasMinicss() {
    if (process.env.NODE_ENV === "production") {
        pluginArr.push(new minicss({
            filename: './css/test.bundle.css'
        }),)
    } else {

    }
}
hasMinicss()

module.exports = {
    entry: {
        menu: './app.js',
        // start: './app2.js'
        // menu: './app2.js',
    },
    output: {
        publicPath: 'http://cdn.xxx.com/', // 默认在打包后的文件，加上引用前缀
        path: __dirname + '/a_dist',
        filename: "[name].[hash:6].bundles.js"
    },
    cache: false,
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
    module: {
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
                }
            },
            {
                test: /\.tsx?$/,
                use: {
                    loader: 'ts-loader'
                }
            },

            {
                // 使用MiniCssExtractPlugin报ReferenceError: document is not defined错误，是因为和style-loader冲突
                test: /\.css$/i,
                use: [process.env.NODE_ENV === "production" ? minicss.loader : "style-loader", 'css-loader', "./mycssloader/index.js"]
            },
            // {
            //     test: /\.scss$/,
            //     use: [minicss.loader,'style-loader', 'css-loader', 'sass-loader']
            // }, // 处理 scss 文件的 loader
            {
                // 也可以处理mp3等 
                test: /\.(jpg|jpeg|png|gif|svg)$/,
                loader: 'url-loader',
                options: {
                    limit: 10,
                    name: "./pic/[name].[hash:6].[ext]"
                }
            },
            {
                // wp5默认方式
                test: /\.(jpg|jpeg|png|gif|svg)$/,
                type: "asset",// asset/inline asset/resource
                // 限制写在parser中
                parser: {
                    dataUrlCondition: {
                        maxSize: 10
                    }
                },
                // 针对每种匹配做不同的设置
                generator: {
                    filename: "[name].[hash][ext]"
                }
            },

        ]
    },
    plugins: pluginArr,
}

