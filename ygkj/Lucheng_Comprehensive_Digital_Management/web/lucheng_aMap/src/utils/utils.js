/*
 * @Author: hanyu
 * @LastEditTime: 2020-11-18 14:52:17
 * @Description: 存放常用方法
 * @FilePath: /pingHuWaterPlatform/src/utils/utils.js
 */

import axios from "axios";
import { localData } from "../../src/plugins/storage";

/**
 * @description: URL编码
 * @param { Object } 参数json对象
 * @return { String }
 * @author: hanyu
 */
export function toParams(param) {
	let result = "";
	for (let name in param) {
		if (typeof param[name] != "function") {
			if (param[name] === null) {
				result += "&" + name + "=";
			} else {
				result += "&" + name + "=" + encodeURI(param[name]);
			}
		}
	}
	return result.substring(1);
}
/**
 * @desc  函数防抖
 * @param  func 需要执行的函数
 * @param  wait 延迟执行时间（毫秒）
 * @param  immediate---true 表立即执行，false 表非立即执行
 **/
export function debounce(func, wait, immediate = false) {
	let timer;

	return function() {
		let context = this;
		let args = arguments;

		if (timer) clearTimeout(timer);
		if (immediate) {
			var callNow = !timer;
			timer = setTimeout(() => {
				timer = null;
			}, wait);
			if (callNow) func.apply(context, args);
		} else {
			timer = setTimeout(function() {
				func.apply(context, args);
			}, wait);
		}
	};
}

/**
 * @description: 对象深拷贝
 * @param { Object } obj 对象
 * @returns { Object }
 * @author: hanyu
 */
export function cloneObj(obj) {
	// console.log(obj,666)
	let str;
	let newObj = obj.constructor === Array ? [] : {};
	if (Object.prototype.toString.call(obj) !== "[object Object]" && Object.prototype.toString.call(obj) !== "[object Array]") {
		return;
	} else if (window.JSON) {
		str = JSON.stringify(obj); // 系列化对象
		newObj = JSON.parse(str); // 还原
	} else {
		for (let i in obj) {
			newObj[i] = typeof obj[i] === "object" ? this.cloneObj(obj[i]) : obj[i];
		}
	}
	return newObj;
}
/**
 * @description: 格式化数字 每隔3位添加一个逗号
 * @param { Number } value
 * @param { Boolean } int 是否返回整数 默认false
 * @return { String }
 * @author: dyn
 */
export function numFormat(value, int) {
	if (!value) return "0.00";

	/*原来用的是Number(value).toFixed(0)，这样取整时有问题，例如0.51取整之后为1，感谢Nils指正*/
	/*后来改成了 Number(value)|0,但是输入超过十一位就为负数了，具体见评论 */
	var intPart = Number(value) - (Number(value) % 1); //获取整数部分（这里是windy93的方法）
	var intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, "$1,"); //将整数部分逢三一断

	var floatPart = ".00"; //预定义小数部分
	var value2Array = value.toString().split(".");

	//=2表示数据有小数位
	if (value2Array.length == 2) {
		floatPart = value2Array[1].toString(); //拿到小数部分

		if (floatPart.length == 1) {
			//补0,实际上用不着
			return intPartFormat + "." + floatPart + "0";
		} else {
			return intPartFormat + "." + floatPart;
		}
	} else {
		if (int) return intPartFormat;
		return intPartFormat + floatPart;
	}
}

/**
 * @description: 根据日期获取星期几
 * @param { String } dateString 日期字符串（如：2020-05-02）
 * @return { String }
 * @author: hanyu
 */
export function getWeek(dateString) {
	let dateArray = dateString.split("-");
	let date = new Date(dateArray[0], parseInt(dateArray[1] - 1), dateArray[2]);
	return "周" + "日一二三四五六".charAt(date.getDay());
}

export function isEmpty(val) {
	if (val == "" || val == undefined || val == null || val == "null") {
		return "-";
	} else {
		return val;
	}
}
