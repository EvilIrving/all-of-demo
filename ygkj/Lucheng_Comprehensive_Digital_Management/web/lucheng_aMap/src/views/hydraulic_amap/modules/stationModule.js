import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
//水利监控相关操作module
export const stationModule = {
  // 发展规划，综合规划，专项规划，专业规划点位
  async getGlanningLevelPoint(type) {
    await hydraulicApi.getAllProjByPlanClassify({ planClassify: type }).then((res) => {
      if (res.code == 0) {
        let lineList = [];
        // 河道
        let pointList = [];
        let programmeStyle = {
          scale: 1,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/agricultural-water.png`),
          srcs: require(`@/assets/images/agricultural-water.png`)
        };
        // 灌区
        let pointList0 = [];
        let programmeStyle0 = {
          scale: 1,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/flood-control-areas.png`),
          srcs: require(`@/assets/images/flood-control-areas.png`)
        };
        // 分洪工程
        let pointList1 = [];
        let programmeStyle1 = {
          scale: 1,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/irrigated-area.png`),
          srcs: require(`@/assets/images/irrigated-area.png`)
        };
        // 防洪区
        let pointList2 = [];
        let programmeStyle2 = {
          scale: 1,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/pump-station.png`),
          srcs: require(`@/assets/images/pump-station.png`)
        };
        // 堤防
        let pointList3 = [];
        let programmeStyle3 = {
          scale: 1,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/water-gate.png`),
          srcs: require(`@/assets/images/water-gate.png`)
        };
        // 水库
        let pointList4 = [];
        let programmeStyle4 = {
          scale: 1,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/reservoir.png`),
          srcs: require(`@/assets/images/reservoir.png`)
        };
        // 保护区
        let pointList5 = [];
        let programmeStyle5 = {
          scale: 1,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/protection-zone.png`),
          srcs: require(`@/assets/images/protection-zone.png`)
        };
        res.data.forEach((data) => {
          if (data.mapCoordinate != "") {
            let str = JSON.parse(data.mapCoordinate);
            if (str.line.length > 0) {
              str.line.forEach((item) => {
                let result = [];
                for (var i = 0; i < item.coodinate.length; i += 2) {
                  result.push(item.coodinate.slice(i, i + 3));
                }
                lineList.push({
                  name: item.projectName,
                  coordinates: result,
                  fillColor: "#1cfff5",
                  lineColor: "#1cfff5",
                  lineWidth: 4,
                  id: data.id,
                  type: '规划'
                });
              });
              this.MapComponent._riversLrawLines(lineList, {}, "line");
            } else if (str.point.length > 0) {
              str.point.forEach((item) => {
                if (item.projectType == "河道") {
                  let obj = {
                    ...item,
                    latitude: item.lng,
                    longitude: item.lat,
                    name: item.projectName,
                    type: '规划',
                    id: data.id,
                    props: { 名称: item.projectName || "-" }
                  }
                  pointList.push(obj);
                } else if (item.projectType == "灌区") {
                  let obj = {
                    ...item,
                    latitude: item.lng,
                    longitude: item.lat,
                    name: item.projectName,
                    type: '规划',
                    id: data.id,
                    props: { 名称: item.projectName || "-" }
                  }
                  pointList0.push(obj);
                } else if (item.projectType == "分洪工程") {
                  let obj = {
                    ...item,
                    latitude: item.lng,
                    longitude: item.lat,
                    name: item.projectName,
                    type: '规划',
                    id: data.id,
                    props: { 名称: item.projectName || "-" }
                  }
                  pointList1.push(obj);
                } else if (item.projectType == "防洪区") {
                  let obj = {
                    ...item,
                    latitude: item.lng,
                    longitude: item.lat,
                    name: item.projectName,
                    type: '规划',
                    id: data.id,
                    props: { 名称: item.projectName || "-" }
                  }
                  pointList2.push(obj);
                } else if (item.projectType == "堤防") {
                  let obj = {
                    ...item,
                    latitude: item.lng,
                    longitude: item.lat,
                    name: item.projectName,
                    type: '规划',
                    id: data.id,
                    props: { 名称: item.projectName || "-" }
                  }
                  pointList3.push(obj);
                } else if (item.projectType == "水库") {
                  let obj = {
                    ...item,
                    latitude: item.lng,
                    longitude: item.lat,
                    name: item.projectName,
                    type: '规划',
                    id: data.id,
                    props: { 名称: item.projectName || "-" }
                  }
                  pointList4.push(obj);
                } else if (item.projectType == "保护区") {
                  let obj = {
                    ...item,
                    latitude: item.lng,
                    longitude: item.lat,
                    name: item.projectName,
                    type: '规划',
                    id: data.id,
                    props: { 名称: item.projectName || "-" }
                  }
                  pointList5.push(obj);
                }
              });
              this.MapComponent.drawClusterPoint(pointList, programmeStyle, "pointLayer");
              this.MapComponent.drawClusterPoint(pointList0, programmeStyle0, "pointLayer0");
              this.MapComponent.drawClusterPoint(pointList1, programmeStyle1, "pointLayer1");
              this.MapComponent.drawClusterPoint(pointList2, programmeStyle2, "pointLayer2");
              this.MapComponent.drawClusterPoint(pointList3, programmeStyle3, "pointLayer3");
              this.MapComponent.drawClusterPoint(pointList4, programmeStyle4, "pointLayer4");
              this.MapComponent.drawClusterPoint(pointList5, programmeStyle5, "pointLayer5");
            }
          }
        })
      }
    })
  },
  // 流量点位
  async mountainRiverWaterLevel(type) {
    let opt = {
      dataTime: '',
      areaCode: '',
      bas: ''
    }
    let res = await hydraulicApi.mountainRiverWaterLevel(opt);
    let normalArr = [];
    let normalStyle = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
      src: require(`@/assets/images/map_legends/map-flow.png`),
      srcs: require(`@/assets/images/map_legends/map-flow-cluster.png`),
    };
    res.data.forEach((item) => {
      let obj = {
        ...item,
        stType: "flow",
        latitude: item.stLat,
        longitude: item.stLong,
        type: "流量",
        props: {
          站点名称: item.stName || "-",
          实时流量: (item.flow || "-") + "m³/s",
        },
        text: `${item.stName || "-"} 实时流量:${item.flow || "-"}m³/s`
      };
      normalArr.push(obj);
    });
    this.MapComponent.drawClusterPoint(normalArr, normalStyle, "flownormal");
  },
  //获取水情点位
  async getWaterPoint(type) {
    let opt = {};
    switch (type) {
      case "水库水位":
        {
          opt.stationType = "RR";
          opt.resGrade = "";
        }
        break;
      case "河道水位":
        {
          opt.stationType = "ZZ";
        }
        break;
      case "堰闸水位":
        {
          opt.stationType = "DD";
        }
        break;
      case "水闸":
      case "水闸工情":
        {
          opt.stationType = "DD";
          opt.isWrp = true;
        }
        break;
      case "潮位":
        {
          opt.stationType = "TT";
        }
        break;
    }
    let res = await hydraulicApi.waterStations(opt);
    let normalArr = [];
    let abnormalArr = [];
    let normalStyle = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
    };
    let abnormalStyle = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
    };
    let stType = res.data.length > 0 ? res.data[0].stType : '';
    let srcKey = "";
    res.data.forEach(item => {
      let obj = {
        ...item,
        latitude: item.stLat,
        longitude: item.stLong,
        type: type
      }
      switch (item.stType) {
        case "RR":
          {
            obj.stationType = "水库";
            srcKey = "reservoirwater";
            obj.props = {
              名称: item.stName,
              水位: item.waterLevel + "m",
            }
            obj.text = `${item.stName || "-"} 水位:${item.waterLevel || "-"}m`;
          }
          break;
        case "ZZ":
          {
            obj.stationType = "河道";
            srcKey = "riverwater";
            obj.props = {
              名称: item.stName,
              水位: item.waterLevel + "m",
            }
            obj.text = `${item.stName || "-"} 水位:${item.waterLevel || "-"}m`;
          }
          break;
        case "DD":
          {
            obj.stationType = "堰闸";
            srcKey = "checkgate";
            if (type == '堰闸水位') {
              obj.props = {
                名称: item.stName,
                水位: item.upz + "m",
              }
              obj.text = `${item.stName || "-"} 水位:${item.upz || "-"}m`;
            } else {
              obj.props = {
                名称: item.stName,
                水位: item.waterLevel + "m",
              }
              obj.text = `${item.stName || "-"} 水位:${item.waterLevel || "-"}m`;
            }

          }
          break;
        case "TT":
          {
            obj.stationType = "潮位";
            srcKey = "sealevel";
            obj.props = {
              名称: item.stName,
              潮位: item.tdz + "m",
            }
            obj.text = `${item.stName || "-"} 潮位:${item.tdz || "-"}m`;
          }
          break;
      }
      if (item.warning) {
        abnormalArr.push(obj);
      } else {
        normalArr.push(obj);
      }
    })
    normalStyle = {
      ...normalStyle,
      src: require(`@/assets/images/map_legends/map-${srcKey}.png`),
      srcs: require(`@/assets/images/map_legends/map-${srcKey}-cluster.png`),
    }
    abnormalStyle = {
      ...abnormalStyle,
      src: require(`@/assets/images/map_legends/map-${srcKey}-abnormal.png`),
      srcs: require(`@/assets/images/map_legends/map-${srcKey}-abnormal-cluster.png`),
    }
    this.MapComponent.drawClusterPoint(normalArr, normalStyle, stType + 'normal');
    this.MapComponent.drawClusterPoint(abnormalArr, abnormalStyle, stType + 'abnormal');
  },
  //获取雨情点位
  async getRainPoint(type) {
    let opt = {
      min: -10,
      max: 10000,
      minutes: 60,
    };
    let res = await hydraulicApi.rainStations(opt);
    let list = res.data;
    list.map((item) => {
      item.drp = item.drp == 0 ? item.drp : Number(item.drp).toFixed(1)
      let src = "";
      if (item.drp <= 0) {
        src = require("@/assets/images/legend/rain_0.png");
      } else if (item.drp > 0 && item.drp < 10) {
        src = require("@/assets/images/legend/rain_10.png");
      } else if (item.drp >= 10 && item.drp < 25) {
        src = require("@/assets/images/legend/rain_10_25.png");
      } else if (item.drp >= 25 && item.drp < 50) {
        src = require("@/assets/images/legend/rain_25_50.png");
      } else if (item.drp >= 50 && item.drp < 100) {
        src = require("@/assets/images/legend/rain_50_100.png");
      } else if (item.drp >= 100 && item.drp < 250) {
        src = require("@/assets/images/legend/rain_100_250.png");
      } else {
        src = require("@/assets/images/legend/rain_250.png");
      }
      item.src = src;
      return item;
    });
    if (type) {
      this.MapComponent.showRainPoint(list, type);
    }
  },
  //获取水质点位
  async getWaterQualityPoint(type) {
    let abnormalArr1 = [];
    let abnormalArr2 = [];
    let abnormalArr3 = [];
    let abnormalArr4 = [];
    let abnormalArr5 = [];
    let abnormalArr6 = [];
    let abnormalArr7 = [];
    let abnormalStyle1 = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
      src: require(`@/assets/images/map_legends/one-class.png`),
      srcs: require(`@/assets/images/map_legends/one-class-cluster.png`)
    };
    let abnormalStyle2 = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
      src: require(`@/assets/images/map_legends/two-class.png`),
      srcs: require(`@/assets/images/map_legends/two-class-cluster.png`)
    };
    let abnormalStyle3 = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
      src: require(`@/assets/images/map_legends/three-class.png`),
      srcs: require(`@/assets/images/map_legends/three-class-cluster.png`)
    };
    let abnormalStyle4 = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
      src: require(`@/assets/images/map_legends/four-class.png`),
      srcs: require(`@/assets/images/map_legends/four-class-cluster.png`)
    };
    let abnormalStyle5 = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
      src: require(`@/assets/images/map_legends/five-class.png`),
      srcs: require(`@/assets/images/map_legends/five-class-cluster.png`)
    };
    let abnormalStyle6 = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
      src: require(`@/assets/images/map_legends/six-class.png`),
      srcs: require(`@/assets/images/map_legends/six-class-cluster.png`)
    };
    let abnormalStyle7 = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
      src: require(`@/assets/images/map_legends/seven-class.png`),
      srcs: require(`@/assets/images/map_legends/seven-class-cluster.png`)
    };
    let opt = {
      areaCode: "",
      siteName: "",
      qualityLevel: "",
      level: ""
    };
    let res = await hydraulicApi.waterQualityV2(opt);
    res.data.forEach(item => {
      let obj = {
        ...item,
        type: type,
        latitude: parseFloat(item.latitude),
        longitude: parseFloat(item.longitude),
        props: {
          站点名称: item.siteName,
          水质等级: item.qualityLevel == 1 && item.qualityWorse == false ? 'Ⅰ类'
            : item.qualityLevel == 2 && item.qualityWorse == false ? 'Ⅱ类'
              : item.qualityLevel == 3 && item.qualityWorse == false ? 'Ⅲ类'
                : item.qualityLevel == 4 && item.qualityWorse == false ? 'Ⅳ类'
                  : item.qualityLevel == 5 && item.qualityWorse == false ? 'Ⅴ类'
                    : item.qualityLevel == 6 && item.qualityWorse == false ? '劣Ⅴ类'
                      : '恶化',
          更新时间: item.tm
        },
        text: `${item.siteName || "-"} 水质等级:${item.qualityLevel == 1 && item.qualityWorse == false ? 'Ⅰ类'
            : item.qualityLevel == 2 && item.qualityWorse == false ? 'Ⅱ类'
              : item.qualityLevel == 3 && item.qualityWorse == false ? 'Ⅲ类'
                : item.qualityLevel == 4 && item.qualityWorse == false ? 'Ⅳ类'
                  : item.qualityLevel == 5 && item.qualityWorse == false ? 'Ⅴ类'
                    : item.qualityLevel == 6 && item.qualityWorse == false ? '劣Ⅴ类'
                      : '恶化'}`
      }
      if (item.qualityLevel == 1 && item.qualityWorse == false) {
        abnormalArr1.push(obj);
      } else if (item.qualityLevel == 2 && item.qualityWorse == false) {
        abnormalArr2.push(obj);
      } else if (item.qualityLevel == 3 && item.qualityWorse == false) {
        abnormalArr3.push(obj);
      } else if (item.qualityLevel == 4 && item.qualityWorse == false) {
        abnormalArr4.push(obj);
      } else if (item.qualityLevel == 5 && item.qualityWorse == false) {
        abnormalArr5.push(obj);
      } else if (item.qualityLevel == 6 && item.qualityWorse == false) {
        abnormalArr6.push(obj);
      } else {
        abnormalArr7.push(obj);
      }
    });
    this.MapComponent.drawClusterPoint(abnormalArr1, abnormalStyle1, 'waterQualityAbnormal1');
    this.MapComponent.drawClusterPoint(abnormalArr2, abnormalStyle2, 'waterQualityAbnormal2');
    this.MapComponent.drawClusterPoint(abnormalArr3, abnormalStyle3, 'waterQualityAbnormal3');
    this.MapComponent.drawClusterPoint(abnormalArr4, abnormalStyle4, 'waterQualityAbnormal4');
    this.MapComponent.drawClusterPoint(abnormalArr5, abnormalStyle5, 'waterQualityAbnormal5');
    this.MapComponent.drawClusterPoint(abnormalArr6, abnormalStyle6, 'waterQualityAbnormal6');
    this.MapComponent.drawClusterPoint(abnormalArr7, abnormalStyle7, 'waterQualityAbnormal7');
  },
  //获取积水点位
  async getLoggingPoint(type) {
    let res = await hydraulicApi.getLogging({});
    let normalArr = [];
    let abnormalArr = [];
    let normalStyle = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
      src: require(`@/assets/images/map_legends/map-hydrops.png`),
      srcs: require(`@/assets/images/map_legends/map-hydrops-cluster.png`)
    };
    let abnormalStyle = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
      src: require(`@/assets/images/map_legends/map-hydrops-abnormal.png`),
      srcs: require(`@/assets/images/map_legends/map-hydrops-abnormal-cluster.png`)
    };
    res.data.forEach(item => {
      let obj = {
        ...item,
        latitude: parseFloat(item.latitude),
        longitude: parseFloat(item.longitude),
        type: type,
        props: {
          名称: item.name,
          积水: item.waterLevel + item.waterLevelUnit
        },
        text: `${item.name || "-"} 积水:${item.waterLevel || "-"}${item.waterLevelUnit}`
      }
      if (item.ponding) {
        normalArr.push(obj);
      } else {
        abnormalArr.push(obj);
      }
    });
    this.MapComponent.drawClusterPoint(normalArr, normalStyle, 'hydropsNormal');
    this.MapComponent.drawClusterPoint(abnormalArr, abnormalStyle, 'hydropsAbnormal');
  },
  //获取视频点位
  async getCameraOpsPoint(type) {
    let opt = {
      cameraName: "",
      pageNum: 0,
      pageSize: 0,
      adcd: "",
    };
    let res = await hydraulicApi.getCameraOps(opt);
    let list = [];
    if (this.checkedShow == true) {
      res.data.list.forEach((val) => {
        if (val.status == 1) {
          list.push(val);
        }
      })
    } else {
      list = res.data.list;
    }
    list.map((item) => {
      if (item.status == 0) {
        item.status = "离线";
        item.src = require("@/assets/images/MapPoint/ditulixian.png");
      } else {
        item.status = "在线";
        item.src = require("@/assets/images/MapPoint/dituzaixian.png");
      }
    });
    this.MapComponent.showVideoPoint(list, type);
  },
  // 墒情
  async getMoistureContentPoint(type) {
    let opt = {
      cameraName: "",
      adcd: "",
      pageNum: 1,
      pageSize: 9999,
      stationType: 'SS',
      dataTime: ""
    };
    let res = await hydraulicApi.soilMoistureMonitorStations(opt);
    let list = [];
    let abnormalStyle = {
      scale: 0.7,
      anchor: [27, 30],
      anchorXUnits: "pixels",
      src: require(`@/assets/images/shang-qing.png`),
    };
    if (res.code == 0) {
      res.data.forEach(item => {
        if (item.stSoliR) {
          let obj = {
            ...item,
            latitude: parseFloat(item.stLat),
            longitude: parseFloat(item.stLong),
            type: type,
            props: {
              '名称': item.stName,
              '10cm土壤含水率(%)': item.stSoliR.slm10,
              '20cm土壤含水率(%)': item.stSoliR.slm20,
              '30cm土壤含水率(%)': item.stSoliR.slm30,
            },
            text: `${item.stName || "-"}`
          }
          list.push(obj);
        }
      });
      this.MapComponent.drawClusterPoint(list, abnormalStyle, 'moisturePoint');
    }
  },
  //关闭水雨情弹窗
  handleWaterRainClose() {
    this.waterRainDialog = false;
    this.stationData = {};
  },
  //打开测站弹框
  showDialog(data) {
    this.stationData = data;
    this.dialogType = data.stType || data.type;
    this.dialogTitle = data.stName || data.stationName || data.cameraName || data.name || data.siteName;
    this.waterRainDialog = true;
    data.cameraName ? (this.dialogType = "camera") : this.dialogType;
    this.$nextTick(() => {
      console.log(this.dialogType, 'dialogType');
      switch (this.dialogType) {
        case "PP":
          this.$refs.rainDialog.getRainStationInfo();
          break;
        case "RR":
        case "DD":
        case "ZZ":
        case "TT":
          this.$refs.rainDialog.getWaterStationInfo();
          break;
        case "camera":
          this.$refs.rainDialog.getCameraStationInfo();
          break;
        case "flow":
          this.$refs.rainDialog.getFlowDetail();
          break;
        case "FP":
          this.$refs.rainDialog.getLogStaitonInfo();
          break;
        default:
          this.$refs.rainDialog.getWaterStationInfo();
          break;
      }
    });
  },
}