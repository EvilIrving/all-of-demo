/*
 * @Author: wupengfei
 * @LastEditTime: 2020-09-25 11:42:34
 * @LastEditors: wupengfei
 * @Descripttion: 
 * @FilePath: \ruiAnWaterPlatform\src\config\mapConfig.js
 */
import { Tile as TileLayer } from 'ol/layer'
import XYZ from 'ol/source/XYZ'

const MAPSERVER = "http://112.17.127.75:8008/arcgis/rest/services/"

// const BASEMAPSERVER = 'https://sldtpt.zjwater.com:6443/wzslyzt/PBS/rest/services/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}'
const BASEMAPSERVER = 'http://t0.tianditu.gov.cn/DataServer?T=img_c&X={x}&Y={y}&L={z}&tk=2eeb2384fab6fcc8f96762c31bd0563b'

const BASEMAKSERVER = MAPSERVER + 'wzbap/MapServer/tile/{z}/{y}/{x}'

const BASEBOUNDARYSERVER = MAPSERVER + '%E6%B8%A9%E5%B7%9E%E5%B8%82%E7%95%8C/MapServer/tile/{z}/{y}/{x}'

const XJBOUNDARYSERVER = MAPSERVER + '2017%E6%B8%A9%E5%B7%9E%E5%8E%BF%E7%95%8C/MapServer/tile/{z}/{y}/{x}';

const PROJECTION = "EPSG:4326" //坐标系

const CENTERLATLNG = [27.817, 120.527] //中心点经度和纬度

const setBaseLayer = function (type) {
    let layers = [];
    switch (type) {
        case 1: {
            const layer = new TileLayer({
                // zIndex: 1,
                source: new XYZ({
                    url: BASEMAPSERVER,
                    projection: PROJECTION
                }),
            }, { zoomOffset: 1 })
            layers.push(layer);
        } break;

        case 'mask': {
            const layer = new TileLayer({
                source: new XYZ({
                    url: BASEMAKSERVER,
                    projection: PROJECTION
                }),
            }, { zoomOffset: 1 })
            layers.push(layer);
        } break;

        case 'boundary': {
            const layer = new TileLayer({
                source: new XYZ({
                    url: BASEBOUNDARYSERVER,
                    projection: PROJECTION
                }),
            }, { zoomOffset: 1 })
            layers.push(layer);
        } break;
        case 'xjBoundary': {
            const layer = new TileLayer({
                source: new XYZ({
                    url: XJBOUNDARYSERVER,
                    projection: PROJECTION
                }),
            }, { zoomOffset: 1 })
            layers.push(layer);
        }
    }
    return layers;
}

const mapconfig = {
    mapServer: MAPSERVER,
    lng: CENTERLATLNG[1],
    lat: CENTERLATLNG[0],
    zoom: 9.5,
    minZoom: 9,
    maxZoom: 18,
    defaultLayer: setBaseLayer(1),
    maskServer: BASEMAKSERVER,
    maskLayer: setBaseLayer('mask'),
    boundaryServer: BASEBOUNDARYSERVER,
    boundaryLayer: setBaseLayer('boundary'),
    xjBoundary: setBaseLayer('xjBoundary')
};

export default mapconfig