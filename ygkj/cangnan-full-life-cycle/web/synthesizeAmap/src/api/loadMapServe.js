import mapAxios from '../config/mapRequest'

export function queryMapserver(url, params) {
    return mapAxios({
        url,
        params,
        // : {
        //   f: "pjson",
        //   where: "1=1",
        //   outSR:4490,
        //   inSR:4490,
        //   maxAllowableOffset:0,
        //   returnGeometry:true,
        //   outFields:"*",
        //   spatialRel:"esriSpatialRelIntersects"
        // }
    })
}