import Draw from 'ol/interaction/Draw';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import { fromLonLat, transform, toLonLat } from "ol/proj";
import {
    unByKey
} from 'ol/Observable.js';
import Overlay from 'ol/Overlay';

import {
    getArea,
    getLength
} from 'ol/sphere.js';
import View from 'ol/View';
import {
    LineString,
    Polygon
} from 'ol/geom.js';
import {
    Circle as CircleStyle,
    Fill,
    Stroke,
    Style
} from 'ol/style.js';
// import { map } from 'core-js/fn/array';
//
var meatureFeature;
var mapObj;

export default {
    measure(map, measureType) {
        /**
         * Currently drawn feature.
         * @type {module:ol/Feature~Feature}
         */
        var sketch;


        /**
         * The help tooltip element.
         * @type {Element}
         */
        var helpTooltipElement;


        /**
         * Overlay to show the help messages.
         * @type {module:ol/Overlay}
         */
        var helpTooltip;


        /**
         * The measure tooltip element.
         * @type {Element}
         */
        var measureTooltipElement;


        /**
         * Overlay to show the measurement.
         * @type {module:ol/Overlay}
         */
        var measureTooltip;


        /**
         * Message to show when the user is drawing a polygon.
         * @type {string}
         */
        var continuePolygonMsg = '继续点击绘制多边形';


        /**
         * Message to show when the user is drawing a line.
         * @type {string}
         */
        var continueLineMsg = '继续点击绘制线';

        createMeasureTooltip();
        createHelpTooltip();

        // if(measureType=="none"){
        //     // alert("ssss")
        //    meatureFeature.getSource().clear();
        // }else{

        // }

        if (!meatureFeature) {
            meatureFeature = new VectorLayer({
                source: new VectorSource()
            });
            map.addLayer(meatureFeature)
        }
        // window['mapObj'].addLayer(meatureFeature);

        /**
         * Handle pointer move.
         * @param {module:ol/MapBrowserEvent~MapBrowserEvent} evt The event.
         */
        var pointerMoveHandler = function(evt) {
            if (evt.dragging) {
                return;
            }
            /** @type {string} */
            var helpMsg = '请点击开始绘制';

            if (sketch) {
                var geom = (sketch.getGeometry());
                if (geom instanceof Polygon) {
                    helpMsg = continuePolygonMsg;
                } else if (geom instanceof LineString) {
                    helpMsg = continueLineMsg;
                }
            }

            helpTooltipElement.innerHTML = helpMsg;
            helpTooltip.setPosition(evt.coordinate);

            helpTooltipElement.classList.remove('hidden');
        };

        map.on('pointermove', pointerMoveHandler);

        map.getViewport().addEventListener('mouseout', function() {
            helpTooltipElement.classList.add('hidden');
        });

        var draw;
        var formatLength = function(line) {
            var sourceProj = map.getView().getProjection(); //获取投影坐标系
            var length = getLength(line, { projection: sourceProj });
            // console.log('line: ', line.extent_);
            // console.log(length)
            var output;
            if (length > 100) {
                output = (Math.round(length / 1000 * 100) / 100) +
                    ' ' + '千米';
            } else {
                output = (Math.round(length * 100) / 100) +
                    ' ' + '米';
            }
            return output;
            // return length;
        };
        var formatArea = function(polygon) {
            // console.log('polygon: ', polygon);
            var sourceProj = map.getView().getProjection(); //获取投影坐标系
            var area = getArea(polygon, { projection: sourceProj });
            // console.log(area)
            var output;
            if (area > 10000) {
                output = '<p style="line-height:50px">'+(Math.round(area / 1000000 * 100) / 100) +
                    // ' ' + 'km<sup>2</sup></p>';
                    ' ' + '平方千米';
            } else {
                output = (Math.round(area * 100) / 100) +
                    // ' ' + 'm<sup>2</sup>';
                    ' ' + '平方米';
            }
            return output;
        };
        var source;
        // var layer ;
        // 获取存放feature的vectorlayer层。map初始化的时候可以添加好了
        for (let layerTmp of map.getLayers().getArray()) {
            if (layerTmp.get("name") == "feature") {
                // layer = layerTmp;
                // layerTmp.setSource(null)
                source = layerTmp.getSource();
            }
        }

        function addInteraction() {
            var type = (measureType == 'area' ? 'Polygon' : 'LineString');
            draw = new Draw({
                source: source,
                type: type,
                style: new Style({
                    fill: new Fill({
                        color: 'rgba(0, 0, 255, 0.2)'
                    }),
                    stroke: new Stroke({
                        color: 'rgba(0, 0, 255, 0.8)',
                        lineDash: [10, 10],
                        width: 2
                    }),
                    image: new CircleStyle({
                        radius: 5,
                        stroke: new Stroke({
                            color: 'rgba(0, 0, 0, 0.7)'
                        }),
                        fill: new Fill({
                            color: 'rgba(255, 255, 255, 0.2)'
                        })
                    })
                })
            });
            map.addInteraction(draw);

            var listener;
            draw.on('drawstart',
                function(evt) {
                    // set sketch
                    sketch = evt.feature;

                    /** @type {module:ol/coordinate~Coordinate|undefined} */
                    var tooltipCoord = evt.coordinate;

                    listener = sketch.getGeometry().on('change', function(evt) {
                        var geom = evt.target;
                        var output;
                        if (geom instanceof Polygon) {
                            output = formatArea(geom);
                            tooltipCoord = geom.getInteriorPoint().getCoordinates();
                        } else if (geom instanceof LineString) {
                            output = formatLength(geom);
                            tooltipCoord = geom.getLastCoordinate();
                        }
                        measureTooltipElement.innerHTML = output;
                        measureTooltip.setPosition(tooltipCoord);
                    });
                }, this);

            draw.on('drawend',
                function(e) {
                    meatureFeature.getSource().addFeature(e.feature)
                    measureTooltipElement.className = 'tooltip tooltip-static';
                    measureTooltip.setOffset([0, -7]);
                    // unset sketch
                    sketch = null;
                    // unset tooltip so that a new one can be created
                    measureTooltipElement = null;
                    createMeasureTooltip();
                    unByKey(listener);
                    map.un('pointermove', pointerMoveHandler);
                    map.removeInteraction(draw);
                    // helpTooltipElement.classList.add('hidden');
                    helpTooltipElement.innerHTML = "";
                    // setTimeout(() => {
                    //     meatureFeature.getSource().clear();
                    // }, 2000);
                }, this);
        }

        function createHelpTooltip() {
            if (helpTooltipElement) {
                helpTooltipElement.parentNode.removeChild(helpTooltipElement);
            }
            helpTooltipElement = document.createElement('div');
            helpTooltipElement.className = 'tooltip hidden';
            helpTooltip = new Overlay({
                id: "sbxmjzlId",
                element: helpTooltipElement,
                offset: [15, 0],
                positioning: 'center-left'
            });
            map.addOverlay(helpTooltip);
        }

        function createMeasureTooltip() {
            if (measureTooltipElement) {
                measureTooltipElement.parentNode.removeChild(measureTooltipElement);
            }
            measureTooltipElement = document.createElement('div');
            measureTooltipElement.className = 'tooltip tooltip-measure';
            measureTooltip = new Overlay({
                id: "sbxmjlId",
                element: measureTooltipElement,
                offset: [0, -15],
                positioning: 'bottom-center'
            });
            map.addOverlay(measureTooltip);
        }
        // 量测调用
        addInteraction();
    },
    meatureFeaturesClear(map) {
        if (meatureFeature.getSource()) {
            meatureFeature.getSource().clear();
            console.log(map.getOverlayById("sbxmjlId"));
            // map.getOverlayById("sbxmjlId").getElement(undefined)
            // map.getOverlayById("sbxmjzlId").setPosition(undefined)
            map.getOverlays().clear();

        }
    }
}