---
title: 简要约定式提交
date: 2023-01-5 00:27:00
tags:
- JavaScript
- 开发规范
categories:
- JavaScript
---

[简要约定式提交](https://juejin.cn/post/6877462747631026190)

## 规范化

- [前端规范文档](<https://juejin.cn/post/6844903897610321934#heading-4>)  
- [commitizen + husky + commitlint](<https://github.com/zuoxiaobai/commitizen-practice-demo>)
- [约定式提交](<https://www.conventionalcommits.org/zh-hans/v1.0.0/>)
- [element plus](<https://github.com/element-plus/element-plus>)
- [Vue github](<https://github.com/vuejs/vue>)
- [Prettier Options](<https://prettier.io/docs/en/options.html>)
- [ESLint Rules](<https://eslint.org/docs/rules/>)
- [Airbnb Style Guide](<https://github.com/airbnb/javascript>)
- [commitizen](<https://cz-git.qbb.sh/zh/>)
- [husky](<https://typicode.github.io/husky/#/>)
- [commitlint](https://commitlint.js.org/#/)

`npx install-peerdeps --dev eslint-config-airbnb` | npm[下载airbnb](https://www.npmjs.com/package/eslint-config-airbnb)

1. Install ESLint & Prettier extensions for VSCode  
Optional - Set format on save and any global prettier options

2. Install Packages  
`npm i -D eslint prettier eslint-plugin-prettier eslint-config-prettier eslint-plugin-node eslint-config-node`  
`npx install-peerdeps --dev eslint-config-airbnb`
3. Create `.prettierrc` for any prettier rules (semicolons, quotes, etc)  
4. Create `.eslintrc.json` file  
You can generate with `eslint --init` if you install eslint globally

```json
{
  "extends": ["airbnb", "prettier", "plugin:node/recommended"],
  "plugins": ["prettier"],
  "rules": {
    "prettier/prettier": "error",
    "no-unused-vars": "warn",
    "no-console": "off",
    "func-names": "off",
    "no-process-exit": "off",
    "object-shorthand": "off",
    "class-methods-use-this": "off"
  }
}
```
