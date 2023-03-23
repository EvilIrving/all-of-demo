/*
 * @Author: wqn
 * @Date: 2023-01-18 10:11:49
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-14 09:40:44
 */
import Vue from "vue";
import * as echarts from "echarts";

// 自定义指令-点击目标元素之外触发事件
Vue.directive("clickOutside", {
  // 初始化指令
  bind(el, binding) {
    function clickHandler(e) {
      // 这里判断点击的元素是否是本身，是本身，则返回
      if (el.contains(e.target)) {
        return false;
      }
      // 判断指令中是否绑定了函数
      if (binding.expression) {
        // 如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
        binding.value(e);
      }
    }
    // 给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
    el.__vueClickOutside__ = clickHandler;
    document.addEventListener("click", clickHandler);
  },
  update() { },
  unbind(el) {
    // 解除事件监听
    document.removeEventListener("click", el.__vueClickOutside__);
    delete el.__vueClickOutside__;
  },
});

//echarts动态设置图表大小
Vue.directive('onEchartResize', {
  bind(el) {
    el['_changeEchartsSize'] = () => {
      let chart = echarts.getInstanceByDom(el);
      if (!chart) {
        return;
      }
      setTimeout(() => {
        chart.resize()
      }, 0)
    }
    window.addEventListener('resize', el['_changeEchartsSize']);
  },
  unbind(el) {
    // 解除事件监听
    document.removeEventListener("click", el['_changeEchartsSize']);
    delete el['_changeEchartsSize'];
  },
})
