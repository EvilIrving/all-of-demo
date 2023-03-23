import { reactive } from 'vue';

/**
 * 请求接口获取的list
 */
 export function useList<T = any>() {
  const data = reactive<{ list: T[]}>({
    list: [],
  });
  return { data };
}