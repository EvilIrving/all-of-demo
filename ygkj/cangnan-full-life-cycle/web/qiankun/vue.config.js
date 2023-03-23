const path = require('path');
const SpritesmithPlugin = require('webpack-spritesmith');
const isProduction = process.env.NODE_ENV === 'production';
// 引入等比适配插件
const px2rem = require('postcss-px2rem');
// 配置基本大小
const postcss = px2rem({
	// 基准大小 baseSize，需要和rem.js中相同
	remUnit: 16
});

module.exports = {
	publicPath: process.env.NODE_ENV === 'production' ? '/qiankun/' : '/',
	devServer: {
		port: 8090
	},
	lintOnSave: false,
	configureWebpack: {
		devtool: isProduction ? '' : 'eval',
		resolve: {
			extensions: ['.js', '.less', '.vue', '.scss'],
			alias: {
				style: path.resolve(__dirname, 'src/style'),
				assets: path.resolve(__dirname, 'src/assets')
			}
		},
		plugins: [
			new SpritesmithPlugin({
				src: {
					cwd: 'public/images/sprites',
					glob: '*.png'
				},
				target: {
					image: 'src/assets/images/sprite.png',
					css: [
						['src/style/sprite.less', { format: 'base_template' }],
						['src/style/spriteConfig.less', { format: 'config_template' }]
					]
				},
				apiOptions: {
					cssImageRef: '~assets/images/sprite.png'
				},
				spritesmithOptions: {
					padding: 2
				},
				customTemplates: {
					base_template: function(data) {
						var r = '@import "./spriteConfig.less";\n';
						r += data.sprites
							.map(s => {
								return `.icon-${s.name}{ .icon-${s.name}() }`;
							})
							.join('\n');
						return r;
					},
					config_template: function(data) {
						var s_h = data.spritesheet.height;
						var s_w = data.spritesheet.width;
						var perSprite = data.sprites
							.map(function(sprite) {
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
							.join('\n');
						return perSprite;
					}
				}
			})
		]
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
	}
};