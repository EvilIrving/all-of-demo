import { ref } from '@vue/composition-api';

export default function usePagination(defaultSize = 10) {
  const pageSize = ref(defaultSize);
  const pageNum = ref(1);
  const total = ref(0);
  return { pageNum, pageSize, total };
}
