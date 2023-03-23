# Vuex 获得完整的代码提示

## First

新建一个`./modules/*.ts` 定义模块中`state`的类型并导出
如下

```ts
export interface User {
  accessToken: string;
  id: string;
  phone?: string;
  refreshToken: string;
  timestamp: number;
  username?: string;
}
```

随之引入声明

```ts
import { Module } from 'vuex';
import { RootStateTypes } from '../index';
```

并以`Module<user,RootStateTypes>`声明模块变量

# Second

将声明的类型引入并修改`./index.ts`以下接口定义与对应的导出保持一致，在 createStore 的参数中增加 modules 的属性

```ts
export interface AllStateTypes extends RootStateTypes {
  user: User;
}
//...
export default createStore<RootStateTypes>({
  modules: { UserModule }
});
```
