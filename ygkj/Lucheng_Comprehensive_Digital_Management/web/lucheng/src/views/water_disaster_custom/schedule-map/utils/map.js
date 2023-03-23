import { View, Feature } from 'ol';
import ImageLayer from 'ol/layer/Image';
import ImageStatic from 'ol/source/ImageStatic';
import Point from 'ol/geom/Point';
import GeoJSON from 'ol/format/GeoJSON';
import { Style, Fill, Stroke, Icon, Text, Circle } from 'ol/style';
import { Select } from 'ol/interaction';
import { singleClick } from 'ol/events/condition';
import { LineString } from 'ol/geom';
import { Vector as VectorLayer } from 'ol/layer';
import { Vector as VectorSource } from 'ol/source';
import { defaults as defaultControls } from 'ol/control';
import { cloneObj } from '@/utils/utils'
import fyjLines from '@/assets/generate_point/feiyunRiver_line_Project.json';
import fyjPoints from '@/assets/generate_point/feiyunRiver_point_Project.json';
import ojLines from '@/assets/generate_point/lucheng_line_Project.json';
import ojPoints from '@/assets/generate_point/lucheng_point_Project.json';
import ajLines from '@/assets/generate_point/aojiang_line_Project.json';
import ajPoints from '@/assets/generate_point/aojiang_point_Project.json';

const GeoJSONBasMap = {
  瓯江: { line: ojLines, points: ojPoints },
  鳌江: { line: ajLines, points: ajPoints },
  飞云江: { line: fyjLines, points: fyjPoints }
};

const scheduleBgLayer = new ImageLayer({
  name: 'scheduleBg',
  source: new ImageStatic({
    url: require('@/assets/images/testbg.png'),
    projection: 'EPSG:4326',
    imageExtent: [119.54142526282753, 28.525295081408228, 122.3166735752165, 26.91993693683649] //WGS-84 经纬度数据范围
  }),
  opacity: 0.5,
  zIndex: 10
});

const ViewSetting = new View({
  projection: 'EPSG:4326',
  center: [120.56371244684031, 28.059903839075673],
  zoom: 16.8,
  maxZoom: 18,
  minZoom: 11,
  extent: [118.245, 26.51, 122.752, 29.45]
});

const isEmpty = value => {
  value = !Number.isNaN(Number(value)) ? Number(value).toFixed(1) : '-';
  return value ? value : '-';
};

function addLayerClick(clickLayer) {
  return new Select({
    condition: singleClick,
    style: null,
    filter: (feature, layer) => layer === clickLayer // 需要添加事件的layer
  });
}
/**
 * 处理接口数据
 * @desc 将接口返回数据与静态数据合并，将点位状态按normal、warning、error分类。 水库对应正常、泄洪、超汛限；水闸对应正常、开启、无数据；潮位、水文对应正常、超警、无数据
 * @param {Array<Object>} data 接口数据
 * @param {Array<>} geoData 从json生成的features
 */
function MergeData(data, geoData, type) {
  let geoDataCopy = cloneObj(geoData)
  let point = geoDataCopy.points
  let dataObj = cloneObj(data)
  if (type) {
    let arr = []
    let obj = {}
    data.forEach(item => {
      let name = item.codeName
      obj[name] = item
      name = name.replace('（', '(')
      name = name.replace('）',')')
      let data = point.features.find(ite => ite.properties.name == name)
      arr.push(data)
    })
    point.features = arr
    dataObj = obj
  }
  point.features = point.features.map(feature => {
    let { properties, geometry } = feature;
    let { name, type } = properties;
    name = name.replace('(', '（')
    name = name.replace(')', '）')
    // properties[name] = data[name]
    // 如果找到合并的数据则merge
    if (name && dataObj.hasOwnProperty(name)) {
      const _data = dataObj[name];
      let status = _data.codeType.indexOf('工程')
      properties.code = status == -1 ? _data.code : _data.extraInfo?.mainStcd;
      properties.code = properties.code || _data.code
      properties.detail = _data
      properties.mapType = '概化图'
      if (_data.noData) {
				properties.label = '暂无数据';
			}
      if (['小型水库', '中型水库'].includes(type)) {
        // TODO
        if (_data.overWarnLimit) properties.status = 'error';
        properties.props = {
          实时水位: isEmpty(_data.waterLevel) + 'm',
          汛限水位: isEmpty(_data.warnLimitLevel) + 'm',
        };
      } else if (type == '水闸') {
        if (!_data.ovs) properties.status = 'warning';
        if (_data.overWarnLimit) properties.status = 'error';
        properties.props = {
          闸上水位: isEmpty(_data.upz) + 'm',
          闸下水位: isEmpty(_data.dwz) + 'm',
          过闸流量: isEmpty(_data.flow) + 'm³/s',
          闸门开度: isEmpty(_data.ovs)
        };
      } else {
        properties.props = {
          水位: isEmpty(_data.waterLevel) + 'm',
        };
      }
    } else {
      properties.label = '暂无数据';
    }
    return { properties, geometry, type: 'Feature' };
  });
  return geoDataCopy;
}
/**
 * 设置动态线段样式
 * @param {*} feature
 * @returns
 */
function getFeatureStyle(feature) {
  const defaultStyle = new Style({
    stroke: new Stroke({
      color: '#41556499',
      width: 5
    })
  });
  const lineStyle = new Style({
    stroke: new Stroke({
      color: '#00c1ff', // [204, 204, 255, 1],
      width: 3,
      lineDash: [16, 16],
      lineDashOffset: feature.get('dashOffset')
    }),
    // text: new Text({
    //   textAlign: 'center',
    //   textBaseline: 'middle',
    //   font: 'normal 16px 微软雅黑',
    //   text: feature.get('name'),
    //   fill: new Fill({ color: '#fff' }),
    //   radius: 10,
    //   // offsetY: position == 'top' ? -offsetY + 10 : offsetY + (index >= 3 ? 0 : 10),
    //   offsetY: offsetY,
    //   offsetX: offsetX,
    //   backgroundFill: new Fill({
    //     color: 'rgba(0,0,0,0)'
    //   }),
    //   padding: [3, 6, 1, 6]
    // })
  });
  return [defaultStyle, lineStyle];
}

/**
 * 将GeoJson数据转化为Feature对象数组
 * @param {Object} data
 * @return {Array} Feature[]
 */
function getFeaturesFromSuorce(data) {
  const geoJson = new GeoJSON();
  let features = geoJson.readFeatures(data);
  return features;
}
/**
 * 获得IconStyle
 * @param {*} feature
 * @param {number} index
 * @returns Feature with Icon Style
 */
function getIconFeature(feature, index) {
  const imgSrcMap = {
    小型水库: {
      normal: require('@/assets/images/generalPoints/point-icon-01.png'),
      error: require('@/assets/images/generalPoints/point-icon-02.png'),
    },
    中型水库: {
      normal: require('@/assets/images/generalPoints/point-icon-06.png'),
      error: require('@/assets/images/generalPoints/point-icon-07.png'),
    },
    水闸: {
      normal: require('@/assets/images/generalPoints/point-icon-03.png'),
      warning: require('@/assets/images/generalPoints/point-icon-04.png'),
      error: require('@/assets/images/generalPoints/point-icon-05.png'),
    },
    测站: {
      normal: require('@/assets/images/generalPoints/point-icon-08.png'),
    }
  };
  let coordinates = feature.values_.geometry.flatCoordinates;
  let [lng, lat] = coordinates;
  let type = feature.get('type');
  let status = feature.get('status') || 'normal';
  let name = feature.get('name') || '';
  let text = feature.get('text');
  let statusType = feature.get('statusType');
  const iconFeature = new Feature({
    geometry: new Point(coordinates),
    lng,
    lat,
    name,
    type: 'generalPoint',
    data: feature
  });
  const imgSrc = imgSrcMap[type][status] || imgSrcMap[type].normal;
  const offsetY = (10 * index) + 8
  const offsetX = (40 * index) + 8
  let offsetY_Num = 0
  let offsetX_Num = 0
  if (statusType == '上') {
    offsetY_Num = -offsetY - 10
  } else if (statusType == '下') {
    offsetY_Num = offsetY + 10
  } else if (statusType == '左') {
    if (name.length == 2) {
      offsetX_Num = -offsetX + 20
    } else if (name.length == 4) {
      offsetX_Num = -offsetX + 5
    } else if (name.length == 5 || name == '上戍(21年)') {
      offsetX_Num = -offsetX
    } else if (name.length == 6) {
      offsetX_Num = -offsetX - 10
    } else {
      offsetX_Num = -offsetX - 15
    }

  } else if (statusType == '右') {
    if (name.length == 2) {
      offsetX_Num = offsetX - 20
    } else if (name.length == 3) {
      offsetX_Num = offsetX - 10
    } else if (name.length == 4) {
      offsetX_Num = offsetX - 5
    } else if (name.length == 5) {
      offsetX_Num = offsetX
    } else if (name.length == 6 || name == '樟村(21年建)' || name == '藤桥(21年建)') {
      offsetX_Num = offsetX + 10
    } else if (name == '七都瓯江边(21年建)') {
      offsetX_Num = offsetX + 28
    } else if (name == '西坑溪水电站(压力式)') {
      offsetX_Num = offsetX + 36
    } else {
      offsetX_Num = offsetX + 42
    }
  }
  const iconStyle = new Style({
    image: new Icon({
      anchor: [25, 25],
      anchorXUnits: 'pixels', //anchorX单位
      anchorYUnits: 'pixels', //anchorY单位
      offset: [0, 0],
      src: imgSrc, //图片路径
      scale: 0.4
    }),
    text: new Text({
      textAlign: 'center',
      textBaseline: 'middle',
      font: 'normal 14px 微软雅黑',
      text,
      fill: new Fill({ color: '#fff' }),
      radius: 10,
      offsetY: offsetY_Num,
      offsetX: offsetX_Num,
      backgroundFill: new Fill({
        color: 'rgba(0,0,0,0)'
      }),
      padding: [3, 6, 1, 6]
    })
  });
  iconFeature.setStyle(iconStyle);
  return iconFeature;
}

export {
  scheduleBgLayer,
  ViewSetting,
  GeoJSONBasMap,
  addLayerClick,
  getFeaturesFromSuorce,
  MergeData,
  getFeatureStyle,
  getIconFeature
};
