/*
 * @Author: wupengfei
 * @LastEditTime: 2020-12-01 15:47:44
 * @LastEditors: Please set LastEditors
 * @Descripttion:
 * @FilePath: \ruiAnWaterPlatform\src\utils\filter.js
 */
import Vue from 'vue'

Vue.filter('dateTimeFormat', function (value) {
    if (!value) return ''
    return new Date(value).format("yyyy-MM-dd hh:mm:ss")
})

Vue.filter('EmptyFormat', function(value){
    if(value == null || value == undefined || value == 'null' || value == ''){
        return '-';
    }else{
        return value;
    }
})

