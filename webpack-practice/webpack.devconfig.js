const base = require('./webpack.baseconfig')
const merge = require("webpack-merge").merge
const webpack = require('webpack')
module.exports = merge(base, {
    mode: 'development', // production none
    devtool: "eval-cheap-source-map", //线上环境  (开发环境：eval-)cheap-module-source-map 
    devServer: {
        watchOptions: {
            ignored: /node_modules/,
        },
        // contentBase: "./dist",
        open: true,
        hot: true,
        port: 3000,
        proxy: {
            '/api': {
                'target': 'http://localhost:3000',
                'changeOrigin': true,
                'pathRewrite': { '^/api': '' },
            },
        }
    },
    plugins: [
        new webpack.DefinePlugin({
            baseURL: 'www.vvv.com'
        })
    ]
})