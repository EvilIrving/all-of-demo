# 温州市水平台项目 组件化大屏 源代码

# 依赖

见 `./package.json`，部分依赖因为遗留问题不清楚是否在项目中使用，一些需要解释的如下

- [v-chart](https://github.com/linjhon/v-charts):【[文档](https://www.linjhon.com/v-charts/)】 - echart 的 Vue 封装,linjhon 维护版本,出于迭代考虑 没有使用[vue-echarts](https://github.com/ecomfe/vue-echarts)
- @vue/composition-api - 增强部分逻辑复用性 `composition-api` 语法与 Vue3 的语法基本一致
- vue-seamless-scroll - 无限滚动组件

# 目录结构

```plain
/
  /dist
  /public
  /src
    /api - 对请求接口的封装
    /assets - 静态资源
    /components - 公共组件
    /composables - 自定义VCA (vue-composition-api)
    /config - 一些配置
    /layouts - 抽离的页面布局
    /plugins - 一些Vue插件
    /router - Vue-router配置
    /store - Vuex配置
    /style - 公共样式
    /utils - 工具方法
    /views - 页面
```

# @vue/composition-api 一些用法

## mapActions、mapGetter

```js
//...
method: {
  ...mapActions(['KeyName'])
}
```

这种解构用法在`@vue/composition-api`中需要如下使用

```js
setup(){
  const { KeyName1, KeyName2 } = mapActions(['KeyName1','KeyName2'])
  return { KeyName1, KeyName2 }
}
```

`mapGetter`同理，因为这两个函数返回了一个包含传的参数的 dispatch 和 getter 响应式包装为键的对象，只需要解构这个对象来用就好了，然后 return 暴露出来就好了

# VSCode 配置

> 部分常用插件 .vscode 中已添加工作区推荐

## Volar

使用 [Volar](https://marketplace.visualstudio.com/items?itemName=johnsoncodehk.volar) 配合获得更完善的开发体

- 提供模板中的完整的代码补全以及类型提示
- 对 Vue 文件的完善代码着色

### 缺陷

无法关闭 eslint 中对 Vue 废弃 Api 的警告

## Prettier

[Prettier](https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode) 对获得统一的代码美化格式

## eslint

不用解释吧

# 历史遗留问题

## 复用逻辑使用 Mixin

原先没有引入`@vue/composition-api`只能使用 mixin，但为了更好的复用逻辑和开发体验最后还是使用了

> 业务系统 `dataManagement`、`fourChaos`、`mobilSupervision`中已经使用`composition-api`

## 多套版本的组件在页面中使用

`./views`目录下的文件夹有很大一部分是冗余的，是经过一版又一版、很长时间的积累才形成这个局面，试图优化也只能做到现在这样。😅

要使得项目更适合多人协作开发，有以下几点目标

- [ ] 完全分离原一张图的代码
- [ ] 完全解决 lint 和美化格式问题然后开启强制性的 eslint
- [ ] 切换 gitlab 并使用 code review
