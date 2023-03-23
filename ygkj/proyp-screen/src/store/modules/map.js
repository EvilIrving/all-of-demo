/*
 * @Author: wqn
 * @Date: 2023-02-15 13:44:36
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-15 13:58:42
 */
export default {
  state: {
    clickLngLat: [], // 储存点击地图的经纬度
    clickFeatVal: null, // 储存点击的要素中携带的数据
    clickAdmnFeat: null, // 储存点击的区域要素
    dbClickLngLat: [], // 储存双击地图的经纬度
    hoverLngLat: [], // 储存地图上鼠标悬浮位置的经纬度
    hoverFeatVal: null, // 储存鼠标悬浮在对应要素上时要素携带的数据
    hoverAdmnFeat: null, // 储存鼠标悬浮在对应区域上时的区域要素
    mapZoom: null, // 储存地图缩放大小
  },
  mutations: {
    setClickLngLat (state, coordinate) {
      if (coordinate && (coordinate[0] !== state.clickLngLat[0] || coordinate[1] !== state.clickLngLat[1])) {
        state.clickLngLat = coordinate
      } else {
        state.clickLngLat = []
      }
    },
    setClickFeatVal (state, featureValue_) {
      if (featureValue_ && featureValue_ !== state.clickFeatVal) {
        state.clickFeatVal = featureValue_
      } else {
        state.clickFeatVal = null
      }
    },
    setClickAdmnFeat (state, admnFeature) {
      if (admnFeature && admnFeature !== state.clickAdmnFeat) {
        state.clickAdmnFeat = admnFeature
      } else {
        state.clickAdmnFeat = null
      }
    },
    setDbClickLngLat (state, coordinate) {
      if (coordinate && (coordinate[0] !== state.clickLngLat[0] || coordinate[1] !== state.clickLngLat[1])) {
        state.dbClickLngLat = coordinate
      } else {
        state.dbClickLngLat = []
      }
    },
    setHoverLngLat (state, coordinate) {
      state.hoverLngLat = coordinate ? coordinate : []
    },
    setHoverFeatVal (state, featureValue_) {
      state.hoverFeatVal = featureValue_ ? featureValue_ : null
    },
    setHoverAdmnVal (state, admnFeature) {
      state.hoverAdmnFeat = admnFeature ? admnFeature : null
    },
    setMapZoom (state, zoom) {
      state.mapZoom = zoom ? zoom : null
    }
  }
}