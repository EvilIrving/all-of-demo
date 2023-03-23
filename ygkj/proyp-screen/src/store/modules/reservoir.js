export default {
  state: {
    reservoirScale: '', // 储存水库查询时的规模
    projectScale: '', // 储存水库规模查询时 具体是哪个规模，用来联动分布情况数据
    reservoirAdcd: '', // 储存水库查询时的行政区划编码
    reservoirStorage: '', // 储存水库点位弹框是否打开
    reservoirRiskType: '', // 储存水库查询时的风险研判类型
    reservoirWarnType: '', // 储存水库查询时的告警类型
  },
  mutations: {
    setReservoirScale(state, { label = '', scale = '' }) {
      if (state.reservoirRiskType) {
        state.reservoirRiskType = ''
      }
      if (state.reservoirWarnType) {
        state.reservoirWarnType = ''
      }
      let value = ''
      switch (label) {
        case '大（I）型水库':
          value = 1
          break;
        case '大（II）型水库':
          value = 2
          break;
        case '中型水库':
          value = 3
          break;
        case '小（I）型水库':
          value = 4
          break;
        case '小（II）型水库':
          value = 5
          break;
        default:
          value = 'all'
          break

      }
      state.projectScale = value
      state.reservoirScale = scale
    },
    setReservoirAdcd(state, adcd) {
      if (adcd && adcd !== state.reservoirAdcd) {
        state.reservoirAdcd = adcd
      } else {
        state.reservoirAdcd = ''
      }
    },
    setReservoirStorage(state, storage) {
      state.reservoirStorage = storage
    },
    setReservoirRiskType(state, type) {
      state.reservoirScale = ''
      state.reservoirRiskType = type
    },
    setReservoirWarnType(state, type) {
      state.reservoirScale = ''
      if (type && type !== state.reservoirWarnType) {
        state.reservoirWarnType = type
      } else {
        state.reservoirWarnType = ''
      }
    }
  },
}