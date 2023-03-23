import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
//河湖信息相关操作module
export const InformationOfLakesModule = {
  // 取水户落点信息
  async waterDoorList(val) {
    let opt = {
      areaCode: '',
      wiuType: '',
      intakeState: ''
    }
    let res = await hydraulicApi.listWiu(opt);
    if(res.code == 0) {
      this.dataList = res.data;
      let list = res.data;
      // 正在取水
      let isWaterList = [];
      let isWaterStyle = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        src: require(`@/assets/images/normal-water.png`),
        srcs: require(`@/assets/images/normal-water-cluster.png`)
      };
      // 停止取水
      let stopTheWaterList = [];
      let stopTheWaterStyle = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        src: require(`@/assets/images/license-for-water.png`),
        srcs: require(`@/assets/images/license-for-water-cluster.png`)
      };
      // 设备掉线
      let equipmentDropsList = [];
      let equipmentDropsStyle = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        src: require(`@/assets/images/super-license.png`),
        srcs: require(`@/assets/images/super-license-cluster.png`)
      };
      list.forEach(item=>{
        let obj = {
          ...item,
          latitude: parseFloat(item.wiuLat),
          longitude: parseFloat(item.wiuLong),
          type:val,
          props:{
            '取水户名称': item.wiuName,
            '当年取水量（万m³）': item.intake,
            '许可取水量（万m³）': item.intakePermit
          },
        }
        if (item.intakeState == "1") {
          isWaterList.push(obj);
        }else if(item.intakeState == "2") {
          stopTheWaterList.push(obj);
        }else {
          equipmentDropsList.push(obj);
        }
      })
      this.MapComponent._drawClusterPoint(isWaterList, isWaterStyle, 'waterSampling');
      this.MapComponent._drawClusterPoint(stopTheWaterList, stopTheWaterStyle, 'waterSampling1');
      this.MapComponent._drawClusterPoint(equipmentDropsList, equipmentDropsStyle, 'waterSampling2');
    }
  },
  // 扰动图斑
  patternSpot() {
    this.MapComponent.showPatternLayer('2018');
  },
  // 遥感影像
  remoteSensing() {
    this.MapComponent.showSpecklesLayer('2020');
  },
  // 地下机井列表
  async waterWellsList(val) {
    const opt = {
      name: '',
      areaCode: '',
    };
    await hydraulicApi.waterWellsList(opt).then((res) => {
      if (res.code == 0) {
        let list = res.data;
        let motorPumpedList = [];
        let motorPumpedList1 = [];
        let motorPumpedList2 = [];
        let motorPumpedList3 = [];
        let motorPumpedStyle = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/being-water-icon.png`),
          srcs: require(`@/assets/images/being-water-icon-cluster.png`)
        };
        let motorPumpedStyle1 = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/stop-water-icon.png`),
          srcs: require(`@/assets/images/stop-water-icon-cluster.png`)
        };
        let motorPumpedStyle2 = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/equipment-drops-icon.png`),
          srcs: require(`@/assets/images/equipment-drops-icon-cluster.png`)
        };
        let motorPumpedStyle3 = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/early-warning-icon.png`),
          srcs: require(`@/assets/images/early-warning-icon-cluster.png`)
        };
        list.forEach(item=>{
          let obj = {
            ...item,
            latitude: parseFloat(item.latitude),
            longitude: parseFloat(item.longitude),
            type:val,
            props: {
              '名称': item.name,
              '设计年取水量（万m³）': item.yearUseWater,
              '当前取水量（万m³）': item.realWaterUsage,
            }
          }
          if (item.realWaterUsage > item.yearUseWater) {
            motorPumpedList.push(obj);
          } else {
            motorPumpedList1.push(obj);
          }
            
        });
        this.MapComponent._drawClusterPoint(motorPumpedList, motorPumpedStyle, 'motorPumpedPoint');
        this.MapComponent._drawClusterPoint(motorPumpedList1, motorPumpedStyle1, 'motorPumpedPoint1');
        this.MapComponent._drawClusterPoint(motorPumpedList2, motorPumpedStyle2, 'motorPumpedPoint2');
        this.MapComponent._drawClusterPoint(motorPumpedList3, motorPumpedStyle3, 'motorPumpedPoint3');
      }
    })
  },
  // 地下水位站列表
  async waterStationsList(val) {
    const opt = {
      stationName: '',
      areaCode: '',
    };
    await hydraulicApi.waterStationsList(opt).then((res) => {
      if (res.code == 0) {
        let list = res.data;
        let undergroundWaterStationList = [];
        let undergroundWaterStationList1 = [];
        let undergroundWaterStationStyle = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/on-line-icon.png`),
          srcs: require(`@/assets/images/on-line-icon-cluster.png`)
        };
        let undergroundWaterStationStyle1 = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/off-line-icon.png`),
          srcs: require(`@/assets/images/off-line-icon-cluster.png`)
        };
        list.forEach(item=>{
          let obj = {
              ...item,
              latitude: parseFloat(item.latitude),
              longitude: parseFloat(item.longitude),
              type:val,
              props: {
                '名称': item.stationName,
                '负责人': item.chargePerson,
              }
            }
            undergroundWaterStationList.push(obj);
        });
        this.MapComponent._drawClusterPoint(undergroundWaterStationList, undergroundWaterStationStyle, 'undergroundWaterStation');
        this.MapComponent._drawClusterPoint(undergroundWaterStationList1, undergroundWaterStationStyle1, 'undergroundWaterStation1');
      }
    })
  },
  // 河道落点接口pageRivSegBaseInfo
  async pageRivSegBaseInfo(val) {
    let obj = {
      riverName: '',              //河道名称
      areaCode: '',               //行政区划
      healthState: '',            //健康等级
      level: '',                  //河道等级
      withPathStr: true,
      pageSize: 1000,
      pageNum: 1
    };
    await hydraulicApi.pageRivSegBaseInfo(obj).then((res) => {
      if (res.code == 0) {
        let list = res.data.list;
        let isWaterList = [];
        list.forEach((item) => {
          let obj = {};
          if (item.riverPath) {
            let str = JSON.parse(item.riverPath);
            if (item.healthState == '1') {
              obj = {
                ...item,
                name:item.riverName,
                coordinates: str,
                fillColor: "#19AF47",
                lineColor: "#19AF47",
                lineWidth: 5,
                type: val,
                props: {
                  '河道等级': item.levelName,
                  '河道长度': item.length,
                  '河长姓名': item.levelName,
                  '联系方式': item.levelName,
                }
              }
            }else if (item.healthState == '2'){
              obj = {
                coordinates: str,
                fillColor: "#EEC80B",
                lineColor: "#EEC80B",
                lineWidth: 5,
                type: val,
                props: {
                  '河道等级': item.levelName,
                  '河道长度': item.length,
                  '河长姓名': item.levelName,
                  '联系方式': item.levelName,
                }
              }
            }else if (item.healthState == '3'){
              obj = {
                '河道等级':item.levelName,
                coordinates: str,
                fillColor: "#EF1212",
                lineColor: "#EF1212",
                lineWidth: 5,
                type: val,
                props: {
                  '河道等级': item.levelName,
                  '河道长度': item.length,
                  '河长姓名': item.levelName,
                  '联系方式': item.levelName,
                }
              }
            }
            isWaterList.push(obj);
          }
        })
        this.MapComponent._riversLrawLines(isWaterList, {}, 'riverwayLines');
      }
    })
  },
  // 雨水口
  async rivInlet(val) {
    let str = val == '雨水口' ? '1' : '2';
    let obj = {
      areaCode: '',
      recordType: str
    }
    await hydraulicApi.rivInlet(obj).then((res) => {
      if (res.code == 0) {
        let list = res.data;
        let isWaterList = [];
        let isWaterStyle = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/drain-outlet.png`),
          srcs: require(`@/assets/images/drain-outlet-cluster.png`)
        };
        list.forEach(item=>{
          let obj = {
              ...item,
              latitude: parseFloat(item.latitude),
              longitude: parseFloat(item.longitude),
              type:val,
              props: {
                '雨水口编码': item.expNo,
                '道路名称': item.road
              }
            }
          isWaterList.push(obj);
        });
        this.MapComponent._drawClusterPoint(isWaterList, isWaterStyle, 'drainOutlet');
      }
    })
  },
  // 排污口点位信息
  async rivInlets(val) {
    let str = val == '雨水口' ? '1' : '2';
    let obj = {
      areaCode: '',
      recordType: str
    }
    await hydraulicApi.rivInlet(obj).then((res) => {
      if (res.code == 0) {
        let list = res.data;
        let isWaterListS = [];
        let isWaterStyleS = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/Gully.png`),
          srcs: require(`@/assets/images/Gully-cluster.png`)
        };
        list.forEach(item=>{
          let obj = {
            ...item,
            latitude: parseFloat(item.latitude),
            longitude: parseFloat(item.longitude),
            type:val,
            props: {
              '排污口编码': item.expNo,
              '道路名称': item.road
            }
          }
          isWaterListS.push(obj);
        });
        this.MapComponent._drawClusterPoint(isWaterListS, isWaterStyleS, 'drainOutletS');
      }
    })
  },
  // 取水口点位接口
  async intakePoint(val) {
    let obj = {
      areaCode: this.areaData,          //行政区划    
      status: this.runningStatusData,   //运行状态
    };
    await hydraulicApi.wainMonitor(obj).then((res) => {
      if (res.code == 0) {
        let list = res.data;
        // 正在取水
        let isWaterList = [];
        let isWaterStyle = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/Is-water.png`),
          srcs: require(`@/assets/images/Is-water-cluster.png`)
        };
        // 停止取水
        let stopTheWaterList = [];
        let stopTheWaterStyle = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/Stop-the-water.png`),
          srcs: require(`@/assets/images/Stop-the-water-cluster.png`)
        };
        // 设备掉线
        let equipmentDropsList = [];
        let equipmentDropsStyle = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/Equipment-docking.png`),
          srcs: require(`@/assets/images/Equipment-docking-cluster.png`)
        };
        list.forEach(item=>{
          let obj = {
            ...item,
            latitude: parseFloat(item.wainLat),
            longitude: parseFloat(item.wainLong),
            type:val,
            props:{
              '取水口名称': item.wainName,
              '实时取水量（m³）': item.intake,
              '更新时间': item.tm
            },
          }
          if (item.status == "正在取水") {
            isWaterList.push(obj);
          }else if(item.status == "停止取水") {
            stopTheWaterList.push(obj);
          }else {
            equipmentDropsList.push(obj);
          }
        })
        this.MapComponent._drawClusterPoint(isWaterList, isWaterStyle, 'isWater');
        this.MapComponent._drawClusterPoint(stopTheWaterList, stopTheWaterStyle, 'stopTheWater');
        this.MapComponent._drawClusterPoint(equipmentDropsList, equipmentDropsStyle, 'equipmentDrops');
      }
    })
  },
  // 点位接口
  async riverUnionInfo(val) {
    let obj = {
      pageNum: '1',        //页码
      pageSize: '999',      //页数
      areaCode: '',      //行政区划    
      name: '',          //名称 
      type: val == '河景' ? '河道河景': '', //类型：河道河景/雨水口/入河排污口
    };
    await hydraulicApi.riverUnionInfo(obj).then((res) => { 
      if (res.code == 0) {
        let list = res.data.list;
        let riverSceneryList = [];
        let riverSceneryStyle = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          src: require(`@/assets/images/river-scenery.png`),
          srcs: require(`@/assets/images/river-scenery-cluster.png`)
        };
        list.forEach(item=>{
          let obj = {
            ...item,
            latitude: parseFloat(item.latitude),
            longitude: parseFloat(item.longitude),
            type:val,
            props:{
              '名称': item.name
            },
          }
          riverSceneryList.push(obj);
      })
      this.MapComponent._drawClusterPoint(riverSceneryList, riverSceneryStyle, 'riverScenery');
      }
    })
  },
  // 关闭弹窗
  closeProgrammeDialog() {
    this.programmeDialogShow = false;
  },
  //打开测站弹框
  programmeShowDialog(data) {
    this.ProgrammeData = data;
    this.programmeTitle = data.name;
    this.programmeDialogShow = true;
  }
}