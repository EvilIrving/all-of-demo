//@ts-check
import Vue from "vue";
import { localData } from '../plugins/storage'
Vue.directive("drag", {
	bind: function(el, binding, vnode, oldVnode) {
		/** @type {HTMLElement?} */
		const headerElement = el.querySelector(".drag");
		if (!headerElement) return;
		headerElement.style.cursor = "move";

		// 获取原有CSS属性
		const elementStyle = window.getComputedStyle(el);
		let clickFlag = true; //判断点击事件标记

		headerElement.onmousedown = e => {
			clickFlag = true;
			// @ts-ignore
			vnode?.children?.[0].$store.commit("SET_TEMP_ZINDEX");
			// @ts-ignore
			const index = vnode?.children?.[0].$store.state.custom.tempZIndex;
			el.style.zIndex = index;
			// 鼠标按下，计算当前元素距离可视区的距离
			const disX = e.clientX - headerElement.offsetLeft + 16;
			const disY = e.clientY - headerElement.offsetTop + 7;

			// 获取left和top的值
			const leftValue = parseFloat(elementStyle.left);
			const topValue = parseFloat(elementStyle.top);
      const { clientWidth, clientHeight } = document.body;
			el.setAttribute("data-flag", "false");
			const firstTime = new Date().getTime();
			document.onmousemove = function(e) {
				// 通过事件委托，计算移动的距离
				const moveLeft = e.clientX - disX;
				const moveTop = e.clientY - disY;
				const [minLeft, maxLeft] = [0, clientWidth - parseFloat(elementStyle.width) / 2];
				const [minTop, maxTop] = [0, clientHeight - parseFloat(elementStyle.height)];
				const resultLeft = leftValue + moveLeft > maxLeft ? maxLeft : Math.max(...[minLeft, leftValue + moveLeft]);
				const resultTop = topValue + moveTop > maxTop ? maxTop : Math.max(...[minTop, topValue + moveTop]);
				// 移动当前元素
				if (clickFlag && (moveLeft < 17 || moveLeft > 20)) {
					el.style.left = `${(resultLeft / window.innerWidth) * 100}%`;
          el.style.top = `${(resultTop / window.innerHeight) * 100}%`;
          // el.style.left = `${Math.ceil(resultLeft)}px`;
					// el.style.top = `${Math.ceil(resultTop)}px`;
				}
			};

			document.onmouseup = function(e) {
				clickFlag = false; 
				document.onmousemove = null;
				document.onmouseup = null;
				//判断元素是否为点击事件
				const lastTime = new Date().getTime();
				if (lastTime - firstTime < 20) {
					el.setAttribute("data-flag", "true");
				}
			};
		};
	}
});
   
Vue.directive("permission", {
  inserted: (el, bind) => {
    let per = bind.value
    let arr = localData('get', 'permission')
    if (!arr.includes(per)) {
      console.log(el.parentNode, 'dasdasdadsadsadsaddsas')
      el.parentNode.removeChild(el)
      // el.parentNode.removeChild(el);
    }
  }
})
