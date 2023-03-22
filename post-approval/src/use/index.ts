/**
 * 存放composition api 的自定义hooks
 */

import { onUnmounted, reactive, ref } from 'vue';

/**
 * 随实例自动销毁的定时器
 * @param callback 定时器执行内容
 * @param timeout 时延
 */
export function useTimer(callback: () => void, timeout = 1000) {
  const timer = setInterval(callback, timeout);
  onUnmounted(() => {
    clearInterval(timer);
  });
}
/**
 * 请求接口获取的list
 */
export function useList<T = any>() {
  const data = reactive<{ list: T[]; loading: boolean; finished: boolean; error: boolean }>({
    list: [],
    loading: false,
    finished: false,
    error: false
  });
  const resetData = () => {
    data.list = [];
    data.loading = false;
    data.finished = false;
    data.error = false;
  };
  return { data, resetData };
}
/**
 * 辅助滑动刷新的翻页器形式
 */
export function usePagination() {
  const pageNum = ref<number>(1);
  const pageSize = ref<number>(10);
  const pageAdd = (size = 1) => {
    pageNum.value += size;
  };
  const pageSizeChange = (size: number) => {
    pageSize.value = size;
  };
  const resetPage = (size = 10) => {
    pageNum.value = 1;
    pageSize.value = size;
  };
  return { pageNum, pageSize, pageAdd, pageSizeChange, resetPage };
}
