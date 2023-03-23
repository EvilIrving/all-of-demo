//@ts-check
import GeoJSON from 'ol/format/GeoJSON';
import LayerGroup from 'ol/layer/Group';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Style, Fill, Stroke, Text } from 'ol/style';
import { dispatchApi } from '../../../../api/dataScreen/jointDispatch/JointDispatch'

const SURFACE_FILL_COLOR = [
  'rgba(0, 160, 255, 0.5)',
  'rgba(75, 199, 4, 0.5)',
  'rgba(254, 203, 110, 0.5)',
  'rgba(255, 138, 33, 0.5)',
  'rgba(254, 50, 58, 0.5)',
  'rgba(243, 47, 223, 0.5)'
];
/**
 * 获取面雨量
 * @returns
 */
export async function getEquivalentSurface({ startTime, endTime }) {
  console.log(startTime, endTime, 'dsadasdasdasdasas');
  const dataInterval = [10, 25, 50, 100, 250, 100000];
  const inRange = (value) => {
    return dataInterval.findIndex((item) => {
      return value < item;
    });
  };
  const res = await dispatchApi.rainfallSurface({
    startTime,
    endTime,
    dataInterval: [-5, 0, ...dataInterval].join()
  });
  if (res.code == 0) {
    const geojson = new GeoJSON();
    const featuresPolygon = geojson.readFeatures(res.data.geoJsonPogyLon);
    const featuresPolyline = res.data.geoJsonPolyLine ? geojson.readFeatures(res.data.geoJsonPolyLine) : undefined;
    const source = new VectorSource({ features: featuresPolygon });
    featuresPolygon.forEach((feature) => {
      const { hvalue, lvalue } = feature.getProperties();
      if (lvalue < 0) {
        source.removeFeature(feature);
      }
    });
    const polygonLayer = new VectorLayer({
      source,
      style: (f) => {
        const { hvalue, lvalue } = f.getProperties();

        return new Style({
          fill: new Fill({ color: SURFACE_FILL_COLOR[inRange(lvalue)] })
        });
      },
      zIndex: 10
    });
    const polylineLayer = new VectorLayer({
      source: new VectorSource({ features: featuresPolyline }),
      style: (f) => {
        const { value } = f.getProperties();

        return new Style({
          stroke: new Stroke({
            color: 'red',
            width: 2
          }),
          text: new Text({
            text: `${value}`,
            font: '12px bold Calibri,sans-serif',
            fill: new Fill({ color: '#000' })
          })
        });
      },
      zIndex: 10
    });
    const layer = new LayerGroup({
      layers: [polygonLayer, polylineLayer]
    });
    layer.set('name', 'isolineLayer');
    return layer;
  }
}
