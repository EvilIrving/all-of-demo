const mutations = {
  SET_DICT: (state, dict) => {
    state.dict[dict.type] = dict.data
  },
}
export default mutations
