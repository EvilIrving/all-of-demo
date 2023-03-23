import { ref, onMounted } from '@vue/composition-api';
import { disasterPreventionApi } from '@/api/RightSildePanel/index.js';

/**
 * 一些简单的复用筛选项自定义VCA
 */
export default function useFilterTable() {
  /** 行政区划筛选 */
  const areaCode = ref('');
  const areaList = ref([]);
  /** 流域筛选 */
  const basin = ref('');
  const basinList = ['飞云江', '瓯江', '鳌江'];
  /** 工程规模筛选 */
  const projectScaleValue = ref('');
  const projectScaleData = [
    { label: '全部', value: '' },
    { label: '大中型', value: '大中型' },
    { label: '小（Ⅰ）型', value: '小（Ⅰ）型' },
    { label: '小（Ⅱ）型', value: '小（Ⅱ）型' }
  ];
  async function getAreaList() {
    const res = await disasterPreventionApi.getArea({ areaCode: 3303, level: 3 });
    if (res.code == 0) {
      areaList.value = res.data;
    }
  }
  /** 开始年份筛选 */
  const startYear = ref('');
  /** 结束年份筛选 */
  const endYear = ref('');

  onMounted(() => {
    getAreaList();
  });

  return { areaCode, areaList, basin, basinList, projectScaleValue, projectScaleData, startYear, endYear };
}
