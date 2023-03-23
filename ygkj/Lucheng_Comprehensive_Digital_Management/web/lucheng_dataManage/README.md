# dcy-fast-cloud-vue


## 平台简介

由于一直使用微服务体现架构开发，但是查看了很多的开源架构，都不是自己心目中想要的效果，而且别人写的代码，封装比较深，对于个人来说不是很好维护。
所以自己打算开源一套微服务架构，使用技术包含技术前沿架构spring cloud alibaba+dubbo远程调用，开源的架构中很少使用dubbo的架构体系，都是采用Feign注解调用方式。
dubbo和Feign对比速度来看，RPC要比http更快，虽然底层都是TCP，但是http协议的信息往往比较臃肿，不过可以采用gzip压缩。难度来看，RPC实现较为复杂，http相对比较简单，
灵活性来看，http更胜一筹，因为它不关心实现细节，跨平台、跨语言。所以我这里才有dubbo远程通信体系，速度够快的，写法相对来说简单，一个service就可以了。
平台名称 dcy-fast-cloud 和dcy-fast 差不多，对于表结构也是一样的，里面有操作权限，数据权限，统一错误处理，网关统一权限拦截。  
让大家拿来即用，快速上手。她可以用于所有的Web应用程序，如网站管理后台，网站会员中心，CMS，CRM，OA。所有前端后台代码封装过后十分精简易上手，出错概率低。同时支持移动客户端访问。系统会陆续更新一些实用功能。


dcy-fast-cloud是一套全部开源的快速开发平台，毫无保留给个人及企业免费使用。

* 感谢**若依**，请移步[RuoYi](https://gitee.com/y_project/RuoYi)
* 感谢 [vue-element-admin](https://github.com/PanJiaChen/vue-element-admin) 后台主题 UI 框架。
* 接口访问地址：[http://localhost:9527/](http://localhost:9527/)
  * 用户名：admin；密码：123456

## dcy-fast生态

* **dcy-fast单体**，请移步[dcy-fast](https://gitee.com/dcy421/dcy-fast)
* **dcy-fast-vue前端**，请移步[dcy-fast-vue](https://gitee.com/dcy421/dcy-fast-vue)
* **dubbo 2.7.8 微服务版本dcy-fast-cloud**，请移步[dcy-fast-cloud](https://gitee.com/dcy421/dcy-fast-cloud)
* **dubbo 2.7.8 微服务版本dcy-fast-cloud-vue前端**，请移步[dcy-fast-cloud-vue](https://gitee.com/dcy421/dcy-fast-cloud-vue)

## 技术介绍

|  技术   | 版本  |作用  |
|  ----  | ----  |----  |
| vue  | 2.6.10 | 渐进式JavaScript框架 |
| element-ui  | 2.13.0 | 前端组件 |
| axios  | 0.18.1 | 网络请求 |
| vue-router  | 3.1.3 | 路由管理 |
| vuex  | 3.1.2 | 状态管理 |
| js-cookie  | 2.2.0 | cookie工具 |