---
title: WebPack使用方式
date: 2022-05-31 17:49:03
tags:
- WebPack
- spritesmith
categories:
- WebPack
---


```js
// 5之前：raw-loader、url-loader、file-loader
// 之后：asset/resource、asset/inline、asset/source、asset
// assetModuleFilename: "img/[name].[hash:6][ext]" =>将所有针对 assetModule 打包的文件放在一起，推荐更换一下方式：
{
    test: /\.(jpe?g|png|gif|svg)$/,
    type: "asset/resource",
    generator: {
      // 针对每种匹配做不同的设置
      filename: "imgs/[name].[hash:6][ext]"
    },
    // 限制写在parser中
        parser: {
          dataUrlCondition: {
            maxSize: 100 * 1024
          }
        }
}
```
