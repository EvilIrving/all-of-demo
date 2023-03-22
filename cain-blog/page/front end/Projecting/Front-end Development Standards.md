---
title: 前端规范
date: 2023-01-5 00:27:00
tags:
- JavaScript
- 开发规范
categories:
- JavaScript
---

## 划分

* 开发流程规范
* 代码规范
* git commit规范
* 项目文件结构规范
* UI设计规范

## 开发流程规范

![开发流程](../../../assets/%E5%BC%80%E5%8F%91%E8%A7%84%E8%8C%83/IMG_20230220.webp)  

## 格式化规范

1. 安装依赖：[`prettier`](https://prettier.io/)

    ```linux
    npm install --save-dev --save-exact prettier 
    // or
    yarn add --dev --exact prettier
    ```

2. 创建一个空配置文件,让编辑器和其他工具知道你正在使用 Prettier

    ```linux
    echo {}> .prettierrc.json
    ```

3. 创建一个.prettierignore文件，让 Prettier CLI 和编辑器知道哪些文件不能格式化，example

    ```txt
    # Ignore artifacts:
    dist
    build
    coverage
    ```

4. 配置编辑器（VScode为例）

    IDE中安装 [`Prettier-Code Formater`](https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode) 插件：
    ![图 13](../../../assets/2.webp)  
    找到IDE中设置模块，搜索 format On Save，勾上这个就可以了。
    ![图 13](../../../assets/1.webp)  

5. git commit 的阶段自动将提交的代码进行格式化

    借助工具 [`husky`](https://github.com/typicode/husky)，它主要可以帮助我们在 git 阶段检查提交消息、运行测试、检查代码等。

    * 安装 husky 和 ​​lint-staged：

    ```js
    npm install --save-dev husky lint-staged
    npx husky install
    npm set-script prepare "husky install"
    npx husky add .husky/pre-commit "npx lint-staged"
    // or
    yarn add --dev husky lint-staged
    npx husky install
    npm set-script prepare "husky install"
    npx husky add .husky/pre-commit "npx lint-staged"
    ```

    * 然后将以下内容添加到package.json中:

    ```js
    //当执行 git commit 阶段前，先执行lint-staged，lint-staged中的内容就是对暂存区的文件执行格式化的命令。
    {
      "lint-staged": {
        "**/*": "prettier --write --ignore-unknown"
      }
    }
    ```

    * 其他：若使用的是脚手架工具搭建的项目，会自带eslint配置（eslintConfig）。prettier 和 eslint 会有一些配置上的冲突，这个时候需要安装eslint-config-prettier 以使 ESLint 和 Prettier 相互配合，安装完后在.eslintrc中配置（以Create-React-App为例）：

    ```js
    # 用"prettier"的部分规则覆盖前面的规则
      "eslintConfig": {
        "extends": [
          "react-app",
          "react-app/jest",
          "prettier"
        ]
      },
    ```

## JS/TS规范

### 常见规范

* [Airbnb JavaScript Style Guide](-)
* [Google JavaScript Style Guide](-)
* [Idiomatic JavaScript Style Guide](-)
* [JavaScript Standard Style Guide](-)
* [jQuery JavaScript Style Guide](-)

### 安装

1. 安装依赖 [eslint](https://eslint.org/docs/latest/user-guide/getting-started%E3%80%82)

    ```txt
    npm install eslint --save-dev
    // or
    yarn add eslint --dev
    ```

2. 生成配置文件

    ```txt
    npm init @eslint/config
    // or
    yarn create @eslint/config
    ```

3. 通过在`git commit`阶段校验，若不通过则取消提交。  
配置（在 `package.json` 中的 `lint-staged` ）：

    ```txt
      "lint-staged": {
        "**/*": "prettier --write --ignore-unknown", //格式化
        "src/*": "eslint --ext .js,.ts,.tsx"  //进行eslint校验
      },
    ```

    >注意： 这里如果选用的`Typescript`，则会默认使用`@typescript-eslint/parser`解析器，官方为了追求更快的解析速度，并不会对.ts文件中的类型进行检查，只会做语法检测。

    >如果需要对类型也进行检测，需要在`extends`中加上`plugin:@typescript-eslint/recommended-requiring-type-checking`。

    >但是在笔者的使用中发现效果并不好，一些基本的类型依然检测不出来，索性这里换了另一种方式：在`pre commit`中执行`yarn run tsc`，这里的意思是对项目中ts文件进行类型检测，默认会读取根目录中的`tsconfig.jso`n配置。

    >这种方式并不完美，它的弊端就在于**全量检测**，如果项目不大还好，若是项目代码量够多，检测10-20s也是常有的事。

## CSS规范

使用 `stylelint` 插件，规范则推荐使用 [`stylelint-config-standard`](https://github.com/stylelint/stylelint-config-standard)

1. 安装
2. 在项目的根目录中创建一个配置文件`.stylelintrc.json`

    ```js
    {
      "extends": "stylelint-config-standard"
    }
    ```

3. 解决与 `prettier` 配置的冲突：

    ```txt
    npm install --save-dev stylelint-config-prettier
    ```

4. 将下面配置复制到 `.stylelintrc.json` 中：

    ```js
    {
      "extends": ["stylelint-config-standard", "stylelint-config-prettier"]
    }
    ```

5. 在 `git commit` 阶段进行检测：

    ```js
    "lint-staged": {
        "**/*": "prettier --write --ignore-unknown", //格式化
        "src/**.{js,jsx,ts,tsx}": "eslint --ext .js,.jsx,.ts,.tsx", //对js文件检测
        "**/*.{less,css}": "stylelint --fix" //对css文件进行检测
      },
    ```

## 自定义其他规范

```js
// 👍 明确函数意图，对于返回true or false的函数，最好以should/is/can/has开头
function shouldShowFriendsList() {...}
function isEmpty() {...}
function canCreateDocuments() {...}
function hasLicense() {...}
function sendEmailToUser(user) {.... } //动词开头，函数意图就很明显

// 👍 文件的顶部明确说明该组件做什么，有没有业务理解难点等等，对业务特殊函数/变量也需要写注释

// 👍 在文件头部导入，顺序依次为: 第三方库 > 公共组件/方法 > 非公共部分组件/方法
import React from 'react'
import _ from 'loadsh'
import Header from '@components/header'
import Content from './Content'

// 👍 在底部导出
export { Content, Header }
export default Component
```

## 项目文件结构规范

文件夹名称全部采用小写 + "-" 来隔开，index.ts用来做导出，通过对工具函数、hooks、api 等加上后缀，更加容易区分引入的文件。

```txt
   - src 开发目录
      - pages 视图
          - module-a 模块A
            - components 私有组件
              - ComA.tsx
              - ComB.tsx
            - index.module.less
            - index.tsx
            - Content.tsx
          - module-b 模块B
      - components 公共组件
        - index.ts 导出所有组件
        - header
          - index.tsx
          - index.module.less
          - User.tsx
          - useGetBaseInfo.hooks.ts
      - routers 路由文件
      - store redux中的数据
      - utils 这里是以utils为后缀
        - index.ts
        - a.utils.ts
        - b.utils.ts
      - hooks 这里是以hooks为后缀
        - index.ts
        - a.hooks.ts
        - b.hooks.ts
      - styles 静态资源文件
      - service api请求,这里是以api为后缀
        - a.api.ts 按照后端微服务进行划分
        - b.api.ts
      - constans 常量
```

## Git commit规范

 message 格式如下

 ```text
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
 ```

每个提交消息都包含一个subject、一个body和一个footer (中间使用空行分割)，提交信息的任何一行不能超过 100 个字符。  
👉 type主要有以下几种类型：

* feat: 一个新特性
* fix: 修复bug
* docs: 文档修改
* style: 不影响代码含义的更改（空格、格式、缺少分号等）
* refactor: 代码重构
* perf: 优化性能
* test: 测试用例修改
* chore: 对构建过程或辅助工具和库的更改，例如文档生成
👉 scope：可以是影响范围的任何内容。

👉 subject：包含对更改的简洁描述，规则：

* 使用陈述语句
* 第一个字母不要大写
* 末尾没有点 (.)

👉 body：commit 具体修改内容, 可以分为多行，应该包括改变的动机，并与以前的行为进行对比。

👉 footer: 一些备注, 通常是修复的 bug 的链接。  
👉 [示例](https://github.com/nrwl/nx)：
      ![提交示例](../../../assets/3.webp)

约束提交：[`commitlint`](https://github.com/conventional-changelog/commitlint)。它要做的就是在我们每次提交 `git commit` 的时候，都会帮我们检查 `commit message` 是否符合一定的规范，如果不符合，就让这次提交失败。  
具体配置：

```txt
# 安装 commitlint cli 和 conventional config
npm install --save-dev @commitlint/{config-conventional,cli}
# Windows:
npm install --save-dev @commitlint/config-conventional @commitlint/cli

配置要使用的 commitlint 规则
echo "module.exports = {extends: ['@commitlint/config-conventional']}" > commitlint.config.js

加入到husky中：
npx husky add .husky/commit-msg 'npx --no -- commitlint --edit "$1"'
or
yarn husky add .husky/commit-msg 'yarn commitlint --edit $1'
```

## 资源

1. [Antd设计规范](https://ant.design/docs/spec/introduce-cn)
2. [Clarity Design设计规范](https://design.teambition.com/doc/introduction)

## 推荐阅读

1. [建立前端构建知识体系](https://juejin.cn/post/7145855619096903717)
2. [Webpack Plugin](https://juejin.cn/post/7160467329334607908)
3. [前端工程化基石 `AST`（抽象语法树）以及`AST`的广泛应用](https://juejin.cn/post/7155151377013047304)
4. [自定义hooks](https://juejin.cn/post/7095396322643017742)
5. [浅析前端异常及降级处理](https://juejin.cn/post/6979564690787532814)
6. [前端重新部署](https://juejin.cn/post/6981718762483679239)
7. [前端场景下的搜索框](https://juejin.cn/post/7042332309449605127)
8. [React数据持久化机制](https://juejin.cn/post/7072761358277672974)
9. [多窗口之间sessionStorage共享状态](https://juejin.cn/post/7076767687828832286)

来源 [Demo](https://github.com/noBaldAaa/my-hooks)  

## 如何使用 ESLint

* 安装 Node.JS。
* 使用 `npm install -g eslint` 命令（npm 是一个 JavaScript 包安装工具）安装 ESLint。
* 在你的 JavaScript 项目的根目录（包含该项目的所有文件的那个文件夹）创建一个名为`.eslintrc` 的配置文件。
* 在集成了 ESLint 的编辑器中安装/启用插件。大多数编辑器都有这个选项。

剩下的就是根据[官网](https://eslint.org/docs/latest/user-guide/getting-started)文档自定义规则集

或者使用知名的开源规则如  

* [vue 官方](https://github.com/vuejs/eslint-config-vue)
* [airbnb](https://github.com/airbnb/javascript)

安装pnpm和yarn
`$ npm i pnpm -g --register=<https://registry.npmmirror.com/>`
`$ npm install --global yarn`

## 验证 pnpm 是否安装成功

* `$ pnpm -v`
* `$ yarn --version`
