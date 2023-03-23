/*
 * @Author: wupengfei
 * @LastEditTime: 2020-11-09 13:48:04
 * @LastEditors: wupengfei
 * @Descripttion: 
 */
import { Tile as TileLayer } from 'ol/layer'
import XYZ from 'ol/source/XYZ'

const MAPSERVER = "http://112.17.127.75:8008/arcgis/rest/services/"

//天地图交通底图
const skpMapVec = "https://t{0-7}.tianditu.gov.cn/DataServer?T=vec_c&x={x}&y={y}&l={z}&tk="

//天地图影像底图
const skpMapImg = "https://t{0-7}.tianditu.gov.cn/DataServer?T=img_c&x={x}&y={y}&l={z}&tk="

//天地图标注图层
const skpMapCva = "https://t{0-7}.tianditu.gov.cn/DataServer?T=cva_c&x={x}&y={y}&l={z}&tk="; //天地图影像注记

//天地图密钥
const skpMapKey = "434967d31e3d3f9d396b2f4c4250b023"

const BASEMAPSERVER = 'https://sldtptgis.zjwater.com/arcgis/rest/services/basemap/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}'

const BASEMAPSERVER2 = 'http://223.4.72.162:18099/zjsslgcyxglpt/arcgis/rest/services/basemap/WYX1029/MapServer/tile/{z}/{y}/{x}?blankTile=false'

const PROJECTION = "EPSG:4326" //坐标系

const CENTERLATLNG = [27.817, 120.527] //中心点经度和纬度

const setBaseLayer = function (type) {
    let layers = [];
    switch (type) {
        case 1: {//影像底图
            const layer = new TileLayer({
                source: new XYZ({
                    url: skpMapImg + skpMapKey,
                    projection: PROJECTION
                }),
            }, { zoomOffset: 1 })
            const skpMapCvaLayer = new TileLayer({
                source: new XYZ({
                    url: skpMapCva + skpMapKey,
                    projection: PROJECTION
                }),
            }, { zoomOffset: 1 })
            layers.push(layer);
            layers.push(skpMapCvaLayer);
        } break;
        case 2: {//交通底图
            const layer = new TileLayer({
                source: new XYZ({
                    url: skpMapVec + skpMapKey,
                    projection: PROJECTION
                }),
            }, { zoomOffset: 1 })
            layers.push(layer);
        } break;
    }
    return layers;
}

const mapconfig = {
    mapServer: MAPSERVER,
    lng: CENTERLATLNG[1],
    lat: CENTERLATLNG[0],
    zoom: 9.5,
    minZoom: 8,
    maxZoom: 18,
    vecLayer: setBaseLayer(2),
    defaultLayer: setBaseLayer(1),
};

export default mapconfig