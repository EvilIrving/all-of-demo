/*
 * @Author: hanyu
 * @LastEditTime: 2021-06-16 20:57:06
 * @Description: 动态设置图表大小
 * @FilePath: /rscp-big-screen/src/plugins/echarts-resize.js
 */
import echarts from 'echarts';
import Vue from 'vue';

let HANDLER = "_vue_echarts_resize_handler";

function bind(el) {
  unbind(el);
  el[HANDLER] = function () {
    let chart = echarts.getInstanceByDom(el);
    if(!chart){
      return;
    }
    setTimeout(()=>{
      chart.resize()
    },500)
    
  };
  window.addEventListener('resize', el[HANDLER]);
};

function unbind(el) {
  window.removeEventListener('resize', el[HANDLER]);
  delete el[HANDLER];
};

var directive = {
  bind: bind,
  unbind: unbind
};

const onEchartResize = Vue.directive('onEchartResize', directive);
export { onEchartResize };