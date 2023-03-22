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

function hasMinicss(params) {
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
        path: __dirname + '/a_dist',
        filename: "[name].[hash:6].bundles.js"
    },
    resolve: {},
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: {
                    loader: 'ts-loader'
                }
            },
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
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
                parser: {
                    dataUrlCondition: {
                        maxSize: 10
                    }
                },
                generator: {
                    filename: "[name].[hash][ext]"
                }
            },

        ]
    },

    optimization: {
        // splitChunks: {
        //     cacheGroups: {
        //         commons: {
        //             name: "commons",
        //             chunks: "initial"
        //         }
        //     }
        // }
    },
    plugins: pluginArr,
}

