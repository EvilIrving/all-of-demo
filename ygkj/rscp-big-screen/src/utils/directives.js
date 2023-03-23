/*
 * @Author: hanyu
 * @LastEditTime: 2022-02-18 15:48:42
 * @Description: 存放自定义指令
 * @FilePath: /rscp-big-screen/src/utils/directives.js
 */
import Vue from "vue";
import { localData } from "../plugins/storage";

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
