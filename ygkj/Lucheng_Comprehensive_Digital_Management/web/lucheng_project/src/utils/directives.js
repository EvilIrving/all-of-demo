/*
 * @Author: hanyu
 * @LastEditTime: 2022-07-06 14:09:41
 * @Description: 存放自定义指令
 * @FilePath: \lucheng_project\src\utils\directives.js
 */
import Vue from 'vue'
import { localData } from '../plugins/storage'
// 按钮权限
Vue.directive("permission", {
  inserted: (el, bind) => {
    let per = bind.value
    let arr = localData('get', 'permission')
    if(!arr.includes(per)){
      el.parentNode.removeChild(el);
    }
  }
})
//自定义指令v-conceal，根据登录账号隐藏按钮
Vue.directive('conceal', {
  inserted: function (el) {
    let info = localStorage.getItem('userInfo_obj')
    if (info) {
      info = JSON.parse(info)
      if (info.unitAttibute != 1) {
        el.remove()
      }
    }
  }
})