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

import fyjLines from '@/assets/generate_point/feiyunRiver_line_Project.json';
import fyjPoints from '@/assets/generate_point/feiyunRiver_point_Project.json';
import ojLines from '@/assets/generate_point/ouRiver_line_Project.json';
import ojPoints from '@/assets/generate_point/ouRiver_point_Project.json';
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
	center: [120.54361652790057, 27.686108565213664],
	zoom: 11.4,
	maxZoom: 14,
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
function MergeData(data, geoData) {
	let point = geoData.points;
	point.features = point.features.map(feature => {
		let { properties, geometry } = feature;
		let { name, type } = properties;

		// 如果找到合并的数据则merge
		if (name && data.hasOwnProperty(name)) {
			const _data = data[name];
			properties.code = _data.code;
			if (_data.noData) {
				properties.label = '暂无数据';
			}
			if (['小型水库', '中型水库', '大型水库', '水库'].includes(type)) {
				// TODO
				if (false) properties.status = 'error';
				else if (false) properties.status = 'warning';
				properties.props = {
					水位: isEmpty(_data.waterLevel) + 'm',
					流量: isEmpty(_data.flow) + 'm³/s' + '（模拟计算）',
					入库流量: isEmpty(_data.inFlow) + 'm³/s' + _data.reportTime,
					出库流量: isEmpty(_data.outFlow) + 'm³/s' + _data.reportTime
				};
			} else if (type == '潮位') {
				if (_data.noData) properties.status = 'error';
				else if (_data.overWarnLimit) properties.status = 'warning';
				properties.props = {
					水位: isEmpty(_data.waterLevel) + 'm'
				};
			} else if (type == '水闸') {
				if (_data.noData) properties.status = 'error';
				else if (_data.geteOpened) properties.status = 'warning';
				properties.props = {
					闸上水位: isEmpty(_data.upz) + 'm',
					闸下水位: isEmpty(_data.dwz) + 'm',
					过闸流量: isEmpty(_data.flow) + 'm³/s'
				};
			} else if (type == '水文') {
				if (_data.noData) properties.status = 'error';
				else if (_data.overWarnLimit) properties.status = 'warning';
				properties.props = {
					水位: isEmpty(_data.waterLevel) + 'm',
					流量: isEmpty(_data.flow) + 'm³/s'
				};
			}
		}
		return { properties, geometry, type: 'Feature' };
	});
	return geoData;
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
		})
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
 * @returns Feature with Icon Style
 */
function getIconFeature(feature) {
	const imgSrcMap = {
		潮位: {
			normal: require('@/assets/images/generalPoints/tide-normal.png'),
			warning: require('@/assets/images/generalPoints/tide-warning.png'),
			error: require('@/assets/images/generalPoints/tide-nodata.png')
		},
		小型水库: {
			normal: require('@/assets/images/generalPoints/rs-small-normal.png'),
			warning: require('@/assets/images/generalPoints/rs-small-warning.png'),
			error: require('@/assets/images/generalPoints/rs-small-error.png')
		},
		中型水库: {
			normal: require('@/assets/images/generalPoints/rs-mid-normal.png'),
			warning: require('@/assets/images/generalPoints/rs-mid-warning.png'),
			error: require('@/assets/images/generalPoints/rs-mid-error.png')
		},
		大型水库: {
			normal: require('@/assets/images/generalPoints/rs-big-normal.png'),
			warning: require('@/assets/images/generalPoints/rs-big-warning.png'),
			error: require('@/assets/images/generalPoints/rs-big-error.png')
		},
		水库: {
			normal: require('@/assets/images/generalPoints/rs-mid-normal.png'),
			warning: require('@/assets/images/generalPoints/rs-mid-warning.png'),
			error: require('@/assets/images/generalPoints/rs-mid-error.png')
		},
		水闸: {
			normal: require('@/assets/images/generalPoints/gate-normal.png'),
			warning: require('@/assets/images/generalPoints/gate-open.png'),
			error: require('@/assets/images/generalPoints/gate-nodata.png')
		},
		水文: {
			normal: require('@/assets/images/generalPoints/sw-normal.png'),
			warning: require('@/assets/images/generalPoints/sw-warning.png'),
			error: require('@/assets/images/generalPoints/sw-nodata.png')
		},
		排涝工程: {
			normal: require('@/assets/images/generalPoints/pailao.png'),
			warning: require('@/assets/images/generalPoints/pailao.png'),
			error: require('@/assets/images/generalPoints/pailao.png')
		}
	};
	let coordinates = feature.values_.geometry.flatCoordinates;
	let [lng, lat] = coordinates;
	let type = feature.get('type');
	let status = feature.get('status') || 'normal';
	let name = feature.get('name') || '';
	let text = feature.get('text');
	const iconFeature = new Feature({
		geometry: new Point(coordinates),
		lng,
		lat,
		name,
		type: 'generalPoint',
		aaaaa: 'aaaaaaaa',
		data: feature
	});
	let position = feature.getGeometry().flatCoordinates[1] >= 27.6468505859375 ? 'top' : 'bottom';
	const imgSrc = imgSrcMap[type][status] || imgSrcMap[type].normal;
	const iconStyle = new Style({
		image: new Icon({
			anchor: [16, 16],
			anchorXUnits: 'pixels', //anchorX单位
			anchorYUnits: 'pixels', //anchorY单位
			src: imgSrc, //图片路径
			scale: 0.7
		}),
		text: new Text({
			textAlign: 'center',
			textBaseline: 'middle',
			font: 'normal 14px 微软雅黑',
			text,
			fill: new Fill({ color: '#fff' }),
			radius: 10,
			offsetY: position == 'top' ? -28 : 28,
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
