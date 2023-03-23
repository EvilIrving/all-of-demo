//@ts-check
import { ref } from '@vue/composition-api';

/**
 * 创建一个切换属性的VCA
 * @param {boolean} [defaultState]
 * @returns {[import('@vue/composition-api').Ref<boolean>,(val:boolean)=>void]}
 */
export default function useToggle(defaultState = true) {
  const state = ref(defaultState);
  /**
   * @param {boolean} [val]
   */
  function setState(val) {
    state.value = val ? val : !state.value;
  }
  return [state, setState];
}
