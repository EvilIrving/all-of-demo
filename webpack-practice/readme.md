# webpack

`npm install webpack webpack-cli webpack-dev-server -g`

`npm install babel-loader @babel/core @babel/preset-env --save-dev`

`npm install eslint eslint-webpack-plugin --save-dev`

`npm install eslint eslint-webpack-plugin  --save-dev`

`eslint-config-standard eslint-config-airbnb eslint-config-vue`

`npm install css-loader style-loader file-loader mini-css-extra-plugin --save-dev`

`npm install postcss-loader autoprefixer -D`

`npm i sass-loader node-sass -D`

`npm i node-sass --sass_binary_site=https://npm.taobao.org/mirrors/node-sass/`

`npm install css-minimizer-webpack-plugin`

`npm install typescript typescript-loader --save-dev`

`npm install html-webpack-plugin clean-webpack-plugin --save-dev`

`npm install cross-env --save-dev`

打包分析: webpack-analysis-plugin

## rollup

```js
module.exports={
    input:'', // 必须
    output:{ 
        dir, // 输出目录
        file,
        format // 必须 输出的模块化
    },
    external, 
    plugins,
}
```

`npm install @rollup/plugin-node-resolve --save-dev`
`npm install @rollup/plugin-terser --save`

### rollup-plugin-node-resolve

Rollup 无法处理对node_modules里的包的依赖，增加此配置，可以让 Rollup 具有对node_modules的依赖处理的能力，并将其打包到输出文件里。

### rollup-plugin-commonjs

一些库导出成你可以正常导入的ES6模块，但是目前 NPM 中的大多数包都是以 CommonJS 模块的形式出现的。在它们更改之前，我们需要将 CommonJS 模块转换为 ES2015 供 Rollup 处理。该插件就是用来将 CommonJS 模块转换成 ES2015 模块的。

### @rollup/plugin-terser

压缩代码

## vite

`npm init vite@latest system-vue -- --template vue-ts`
