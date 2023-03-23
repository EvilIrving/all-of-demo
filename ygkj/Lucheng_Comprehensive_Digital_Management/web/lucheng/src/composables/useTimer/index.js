//@ts-check
import { ref, onMounted, onUnmounted } from '@vue/composition-api';
/**
 * 随生命周期自动销毁的
 * @param {()=>void} callback
 * @param {number} [timeout] 默认时延
 */
export default function useTimer(callback, timeout = 1000) {
  const timer = ref();
  onMounted(() => {
    if (callback) timer.value = setInterval(callback, timeout);
  });
  onUnmounted(() => {
    if (timer.value) clearInterval(timer.value);
  });
}
