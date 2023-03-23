/*
 * @Author: wqn
 * @Date: 2022-11-09 14:43:48
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-17 09:55:22
 */
import Vue from "vue"
import 'utils/directive.js'
import 'utils/prototype.js'
import utils from 'utils/utils.js'
import config from "config/config";
import olMapUtil from 'utils/olMapUtils.js'
import { localData, sessionData } from "utils/storage";

Vue.prototype.$util = utils;
Vue.prototype.$config = config;
Vue.prototype.$olMapUtil = olMapUtil;
Vue.prototype.$localData = localData;
Vue.prototype.$sessionData = sessionData;