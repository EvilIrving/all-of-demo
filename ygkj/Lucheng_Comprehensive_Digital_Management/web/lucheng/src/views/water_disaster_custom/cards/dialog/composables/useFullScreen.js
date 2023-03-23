import { useToggle } from '@/composables';
import { nextTick, ref } from '@vue/composition-api';

/**
 * 用于切换详表窗口全屏与否的VCA
 * @returns
 */
export default function useFullScreen() {
  /** @type {import('@vue/composition-api').Ref<import('element-ui').Table>} */
  const tableRef = ref();
  const [fullscreen, toggleFullScreen] = useToggle(false);
  const changeFullScreen = () => {
    toggleFullScreen();
    nextTick(() => {
      if (tableRef.value) tableRef.value.doLayout();
    });
  };
  return { tableRef, fullscreen, changeFullScreen };
}
