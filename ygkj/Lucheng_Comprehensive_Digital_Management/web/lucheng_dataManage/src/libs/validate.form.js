import Vue from 'vue'

/**
 * 生成rule
 * @param validatorName 验证名字
 * @param trigger 类型 blur || change
 * @param required 是否必填
 * @returns {{trigger: *, message: string, required: *}}
 */
Vue.prototype.$ruler = (validatorName, trigger = 'blur', required = true) => {
  return { message: `请${trigger === 'blur' ? '输入' : '选择'}${validatorName}`, trigger, required }
}
