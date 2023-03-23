import { usePagination, useToggle } from '@/composables';
import { ref } from '@vue/composition-api';
/**
 * 异步的详表封装VCA
 * @param {()=>Promise<any[]>} fetchCallback 获取数据的异步方法
 * @param {()=>Promise} [exportCallback] 导出数据的异步方法
 * @description 拥有导出状态，加载状态，分页状态的用于详表的自定义VCA，参数传入两个Promise，导出可选
 */
export default function useAsyncTable(fetchCallback, exportCallback) {
  const [exporting, setExporting] = useToggle(false);
  const [loading, setLoading] = useToggle(false);
  /**
   * 数据表数据 通过el-table展示
   * @type {import('@vue/composition-api').Ref<any[]>}
   */
  const tableData = ref([]);
  const { pageNum, pageSize, total } = usePagination();
  /**
   * 获取数据
   * @param {()=>void} [cb] 回调 同时执行的函数 同步或异步
   */
  async function getList(cb) {
    tableData.value = [];
    setLoading(true);
    tableData.value = await fetchCallback();
    cb && cb();
    setLoading(false);
  }

  async function exportTable() {
    setExporting(true);
    await exportCallback();
    setExporting(false);
  }
  return { exporting, setExporting, loading, setLoading, pageNum, pageSize, total, tableData, getList, exportTable };
}
