import { Message, MessageBox } from 'element-ui'

const ADD_SUCCESS_NOTICE = '添加成功'
const UPDATE_SUCCESS_NOTICE = '修改成功'
const DELETE_SUCCESS_NOTICE = '删除成功'
const SAVE_SUCCESS_NOTICE = '保存成功'
const SAVE_ERROR_NOTICE = '保存失败'

const ADD_ERROR_NOTICE = '添加失败'
const UPDATE_ERROR_NOTICE = '修改失败'
const DELETE_ERROR_NOTICE = '删除失败'

const HANDLE_SUCCESS_NOTICE = '处理成功'
const HANDLE_ERROR_NOTICE = '处理失败'

/**
 * 成功通知
 * @param type
 */
export const MessageSuccess = (type) => {
  switch (type) {
    case 'add':
      Message.success({ message: ADD_SUCCESS_NOTICE })
      break
    case 'upd':
      Message.success({ message: UPDATE_SUCCESS_NOTICE })
      break
    case 'del':
      Message.success({ message: DELETE_SUCCESS_NOTICE })
      break
    case 'save':
      Message.success({ message: SAVE_SUCCESS_NOTICE })
      break
    case 'handle':
      Message.success({ message: HANDLE_SUCCESS_NOTICE })
      break
  }
}

/**
 * 失败通知
 * @param type
 */
export const MessageError = (type) => {
  switch (type) {
    case 'add':
      Message.error({ message: ADD_ERROR_NOTICE })
      break
    case 'upd':
      Message.error({ message: UPDATE_ERROR_NOTICE })
      break
    case 'del':
      Message.error({ message: DELETE_ERROR_NOTICE })
      break
    case 'save':
      Message.error({ message: SAVE_ERROR_NOTICE })
      break
    case 'handle':
      Message.error({ message: HANDLE_ERROR_NOTICE })
      break
  }
}

/**
 *  自定义通知
 * @param type
 * @param message
 * @constructor
 */
export const MessageCustom = (type = 'info', message) => {
  switch (type) {
    case 'success':
      Message.success({ message: message })
      break
    case 'warning':
      Message.warning({ message: message })
      break
    case 'info':
      Message.info({ message: message })
      break
    case 'error':
      Message.error({ message: message })
      break
  }
}

export const ConfirmCustom = (options = { type: 'info' }, message = '您确认删除所选内容吗？', title = '提示') => {
  return MessageBox.confirm(message, title, options)
}
