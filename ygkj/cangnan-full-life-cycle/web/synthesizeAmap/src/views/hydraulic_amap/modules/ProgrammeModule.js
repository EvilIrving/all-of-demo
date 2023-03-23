import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
//水利监控相关操作module
export const ProgrammeModule = {
    // 点位接口
    async listAllProjByPlanClassify(val) {
        // console.log(444, val);
        // this.projectType = [];
        let hash = [];
        let pointList = [];
        let lineList = [];
        let mapCoordinate = null;
        let mapCoordinatePoint = [];
        let mapCoordinateLine = [];
        await hydraulicApi.listAllProjByPlanClassify({ planClassify: val }).then((res) => {
            if (res.data.length > 0) {
                // console.log(555, res.data)
                res.data.forEach((data) => {
                        if (data.mapCoordinate) {
                            mapCoordinate = JSON.parse(data.mapCoordinate);
                            if (mapCoordinate.point.length > 0) {
                                mapCoordinate.point[0].id = data.id
                                mapCoordinatePoint.push(...mapCoordinate.point)
                            }
                            if (mapCoordinate.line.length > 0) {
                                mapCoordinate.line[0].id = data.id
                                mapCoordinateLine.push(...mapCoordinate.line)
                            }
                        }
                    })
                    // 点位信息
                if (mapCoordinatePoint.length > 0) {
                    mapCoordinatePoint.forEach((item) => {
                        hash.push(item.projectType);
                        let imgSrc = require("@/assets/images/reservoir.png");
                        if (item.projectType == "河道") {
                            imgSrc = require("@/assets/images/agricultural-water.png");
                        } else if (item.projectType == "灌区") {
                            imgSrc = require("@/assets/images/flood-control-areas.png");
                        } else if (item.projectType == "分洪工程") {
                            imgSrc = require("@/assets/images/irrigated-area.png");
                        } else if (item.projectType == "防洪区") {
                            imgSrc = require("@/assets/images/pump-station.png");
                        } else if (item.projectType == "堤防") {
                            imgSrc = require("@/assets/images/water-gate.png");
                        } else if (item.projectType == "水库") {
                            imgSrc = require("@/assets/images/reservoir.png");
                        } else if (item.projectType == "保护区") {
                            imgSrc = require("@/assets/images/protection-zone.png");
                        }
                        pointList.push({ lng: item.lat, lat: item.lng, name: item.projectName, id: item.id, src: imgSrc });
                    });
                    // this.projectType = this.unique1(hash);
                    this.MapComponent._drawPoint(pointList, {}, "reservoirLayer");
                }
                // 画线方法
                if (mapCoordinateLine.length > 0) {
                    mapCoordinateLine.forEach((item) => {
                        console.log(22,item);
                        let len = item.coodinate.length;
                        let n = 2;
                        let res = [];
                        let lineNum = len % 2 === 0 ? len / 2 : Math.floor((len / 2) + 1);
                        for (let i = 0; i < lineNum; i++) {
                            let temp = item.coodinate.slice(i * n, i * n + n);
                            res.push(temp);
                        }
                        lineList.push({
                            name: item.projectName,
                            coordinates: res,
                            fillColor: "#A923D2",
                            lineColor: "#A923D2",
                            lineWidth: 5,
                            id: item.id,
                        })
                    })
                    this.MapComponent._drawLines(lineList, {}, "lineLayer")
                }
            } else {
                this.MapComponent._drawPoint([], {}, "reservoirLayer");
                this.MapComponent._drawLines([], {}, "lineLayer");
            }
        })
    },
    // 关闭弹窗
    closeProgrammeDialog() {
        this.programmeDialogShow = false;
    },
    //打开测站弹框
    programmeShowDialog(data) {
        console.log(103, data);
        this.programmeTitle = data.name;
        this.ProgrammeData = data;
        this.programmeDialogShow = true;

        console.log(902, data)

    }
}