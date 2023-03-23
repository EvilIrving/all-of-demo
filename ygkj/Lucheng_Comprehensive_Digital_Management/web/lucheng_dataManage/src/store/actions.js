import {getDictDataByDictType} from "_a/common";

const actions = {
  /**
   * 根据dictType查询list，如果有直接查缓存，没有查询数据库
   * @param commit
   * @param state
   * @param dictType
   * @returns {Promise<unknown>}
   */
  getDictListByType({commit, state}, dictType) {
    return new Promise((resolve, reject) => {
      let dictList = state.dict[dictType];
      if (dictList) {
        resolve(dictList)
        return;
      }
      getDictDataByDictType(dictType).then(response => {
        const {data} = response
        commit('SET_DICT', {type: dictType, data: data});
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  }
}
export default actions
