import { sessionData } from "../plugins/storage"

export const toDo = (thingName, userId) => {
  try {
    const wpk = new Wpk({ appKey: 'a06930f5', appSecret: 'f3f9199358ea42489ba1fdc747c72b51' })
    wpk.toDo(thingName, userId)
  } catch (error) {
    console.log('错误：获取埋点文件失败')
  }
}

export const metaInfo = function () {
  let userInfo = sessionData('get', 'userInfo')
  aplus_queue.push({
    action: 'aplus.setMetaInfo',
    arguments: ['_hold', 'BLOCK']
  });
  //设置会员ID
  aplus_queue.push({
    action: "aplus.setMetaInfo",
    arguments: ["_user_id", userInfo.id]
  });
  // aplus_queue.push({
  //   action: "aplus.setMetaInfo",
  //   arguments: ["_dev_id", "yourDeviceId"]
  // });
  //如采集用户信息是异步行为，需要先设置完用户信息后再执行这个START埋点
  //此时被block住的日志会携带上用户信息逐条发出
  aplus_queue.push({
    action: 'aplus.setMetaInfo',
    arguments: ['_hold', 'START']
  });
}



export const sendPV = function (page_id, page_name, page_url) {
  let userInfo = sessionData('get', 'userInfo')
  aplus_queue.push({
    action: 'aplus.setMetaInfo',
    arguments: ['aplus-waiting', 'MAN']
  });//
  //单页应用路由切换后或在异步获取到pv日志所需的参数后再执行sendPV：
  aplus_queue.push({
    'action': 'aplus.sendPV',
    'arguments': [{
      is_auto: false
    }, {
      //当前你的应用信息，此两行按应用实际参数修改，不可自定义。
      sapp_id: '29902',
      sapp_name: 'slgc',
      _user_id: userInfo.id,
      _user_nick: userInfo.username,
      //自定义PV参数key-value键值对（只能是这种平铺的json，不能做多层嵌套），
      page_id,
      page_name,
      page_url,
    }]
  })
}