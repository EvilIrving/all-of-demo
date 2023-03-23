/*
 * @Author: dengzhao
 * @LastEditTime: 2022-04-18 16:37:34
 * @Description: 
 * @FilePath: \lucheng_project\src\views\propertyManage\propertyUnitMatter\projectTypeTable.js
 */
const reservoir = [
  {
    label: '水库规模',
    prop: 'scale',
  },
  {
    label: '当前水位（米）',
    prop: 'waterLevel',
  },
  {
    label: '当前库容（万方）',
    prop: 'currenty',
  },
  {
    label: '总库容（万方）',
    prop: 'tatolStorage',
  },
  {
    label: '死库容（万方）',
    prop: 'ddcp',
  },
]
const sluice = [
  {
    label: '水闸规模',
    prop: 'scale',
  },
  {
    label: '所在流域',
    prop: 'basinName',
  },
  {
    label: '最大过闸流量（m³/s）',
    prop: 'maxFlow',
  },
]
const seawall = [
  {
    label: '海塘级别',
    prop: 'scale',
  },
  {
    label: '堤防跨界情况',
    prop: 'dts',
  },
  {
    label: '所属岸段',
    prop: 'coastalName',
  },
  {
    label: '海塘长（m）',
    prop: 'seawallLength',
  },
  {
    label: '标绘长（m）',
    prop: 'drawLength',
  },
  {
    label: '起点位置',
    prop: 'startLocation',
  },
  {
    label: '终点位置',
    prop: 'finalLocation',
  },
]
const dike = [
  {
    label: '堤防规模',
    prop: 'scale',
  },
  {
    label: '所在流域',
    prop: 'basinName',
  },
  {
    label: '河道级别',
    prop: 'riverLevel',
  },
  {
    label: '岸别',
    prop: 'riverBank',
  },
  {
    label: '堤防类型',
    prop: 'dikePattern',
  },
  {
    label: '堤防长（m）',
    prop: 'dikeLength',
  },
  {
    label: '标绘长（m）',
    prop: 'drawLength',
  },
  {
    label: '防洪标准（年）',
    prop: 'designedStandard',
  },
]

export default {
  reservoir,
  dike,
  seawall,
  sluice
}