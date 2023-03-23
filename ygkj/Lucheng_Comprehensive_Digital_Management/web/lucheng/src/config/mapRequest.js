import originAxios from 'axios'

export default function mapAxios(option,wzGis) {
  return new Promise((resolve,reject) => {
    let mapServer = process.env.NODE_ENV === "production" ? "/xglptMpServer/arcgis/rest/services/" : "http://117.149.229.28:6080/arcgis/rest/services/";
    //一期库
    if(wzGis){
      mapServer = process.env.NODE_ENV === "production" ? "https://webgis.ygwjg.com/arcgis/rest/services/" : "http://112.17.127.75:8008/arcgis/rest/services/";
    }
    // 1. 创建axios实例
    const instance = originAxios.create({
      baseURL: mapServer,
      timeout: 15000
    });

    // 2. 配置请求和响应拦截
    instance.interceptors.request.use(config => {
      // console.log('来到了request拦截success中');
			// 2.1 当发送网络请求时, 在页面中添加一个loading组件, 作为动画

      // 2.2 某些请求要求用户必须登录, 判断用户是否有token, 如果没有token跳转到login页面
      
			// 2.3 对请求的参数进行序列化(看服务器是否需要序列化)
      
      return config
    },err => {
      // console.log('来到了request拦截failure中');
      return err
    })

    instance.interceptors.response.use(response => {
      // console.log('来到了response拦截success中');
      return response.data
    },err => {
      // console.log('来到了response拦截failure中');
      // console.log(err);
      if (err && err.response) {
        switch (err.response.status) {
          case 400:
            err.message = '请求错误'
            break;
          case 401:
            err.message = '未授权的访问'
            break;
        }
      }
      return err
    })

    //2.传入对象进行网络请求
    instance(option).then(res => {
      // console.log(option)
      resolve(res)
    }).catch(err => {
      reject(err)
    })
  })
}