const path = require("path");
const isProduction = process.env.NODE_ENV === "production";
module.exports = {
    publicPath: process.env.NODE_ENV === 'production' ? '/synthesizeSystem/' : '/',
    outputDir: 'dist-synthesizeSystem',
    assetsDir: '',
    configureWebpack: {
        devtool: isProduction ? '' : 'eval',
        resolve: {
            extensions: ['.js', '.vue'],
            alias: {
                style: path.resolve(__dirname, 'src/style'),
                assets: path.resolve(__dirname, 'src/assets')
            }
        },
    },
};
