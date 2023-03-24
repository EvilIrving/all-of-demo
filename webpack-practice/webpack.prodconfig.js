const base = require('./webpack.baseconfig')
const merge = require("webpack-merge").merge
const webpack = require('webpack')
module.exports = merge(base, {
    mode: 'production',
    // devtool: "none",
    plugins: [
        new webpack.DefinePlugin({
            baseURL: 'www.xxx.com'
        })
    ]
})

// 另外一种设置环境变量的方法
// "dev": "cross-env NODE_ENV webpack-dev-server --config ./webpack.devconfig.js --env development",
// "build": "webpack --config ./webpack.prodconfig.js --env production"

// module.exports = function (env) {
//     return merge(base(env), {
//         mode: 'production',
//         devtool: "none",
//     })
// }