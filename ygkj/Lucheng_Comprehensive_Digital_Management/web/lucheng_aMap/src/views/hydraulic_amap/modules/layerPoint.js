export const layerPointModule = {
    //根据图层名称加载点位
    loadPointByName(layerName, data) {
        switch (layerName) {
            case "水库水位":
            case "河道水位":
            case "堰闸水位":
            case "潮位":
            case "水闸工情":
                {
                    this.getWaterPoint(layerName);
                }
                break;
            case "泵站工情":
                {
                    this.getPumpStationPoint(layerName);
                }
                break;
            case "雨情":
                {
                    this.getRainPoint(layerName);
                }
                break;
            case "流量":
                {
                    this.mountainRiverWaterLevel(layerName);
                }
                break;
            // case "国控断面":
            // case "省控断面":
            case "水质监测":
                {
                    this.getWaterQualityPoint(layerName);
                }
                break;
            case "清四乱":
                {
                    this.WsPatrolProblem(layerName);
                }
                break;
            case "积水":
                {
                    this.getLoggingPoint(layerName);
                    // this.MapComponent.showCityAreaLine();
                    // this.MapComponent.clearAreaLayer();
                }
                break;
            case "发展规划":
                {
                    this.getGlanningLevelPoint(layerName);
                }
                break;
            case "综合规划":
                {
                    this.getGlanningLevelPoint(layerName);
                }
                break;
            case "专业规划":
                {
                    this.getGlanningLevelPoint(layerName);
                }
                break;
            case "专项规划":
                {
                    this.getGlanningLevelPoint(layerName);
                }
                break;
            case "视频":
                {
                    this.getCameraOpsPoint(layerName);
                }
                break;
            case "墒情":
                {
                    this.getMoistureContentPoint(layerName);
                }
                break;
            case "水库":
            case "山塘":
            case "堤防":
            case "海塘":
            case "泵站":
            case "水闸":
            case "闸站":
            case "灌区":
            case "农饮水":
            case "水电站":
            case "码头":
            case "桥梁":
            case "旱闸":
            case "海塘道路交叉出入口":
            case "避灾点":
            case "箱涵":
            case "管理房":
            case "规模以下水闸":
            case "测站平台":
            case "桩号":
                {
                    this.getProjectPoint(layerName);
                }
                break;
            case "断面":
                {
                    this.getProjectPoint(layerName);
                }
                break;
            case "降雨分布":
                {
                    this.timelinebarShow = true;
                    this.rainfallType = "past";
                    this.$set(this.layerData[5], "disabled", true);
                }
                break;
            case "降雨预报":
                {
                    this.timelinebarShow = true;
                    this.rainfallType = "forecast";
                    this.$set(this.layerData[4], "disabled", true);
                }
                break;
            case "风场预报":
                {
                    this.controlWind(true);
                }
                break;
            case "浪场预报":
                {
                    this.controlWave(true);
                }
                break;
            case "卫星云图":
                {
                    this.loadClound(true);
                }
                break;
            case '雷达降雨':
                {
                    this.radarRain(true);
                }
                break;
            // 河湖信息开始
            case '河道':
                {
                    this.pageRivSegBaseInfo(layerName);
                }
                break;
            case '河景':
                {
                    this.riverUnionInfo(layerName);
                }
                break;
            case '取水口':
                {
                    this.intakePoint(layerName);
                }
                break;
            case '雨水口':
                {
                    this.rivInlet(layerName);
                }
                break;
            case '入河排污口':
                {
                    this.rivInlets(layerName);
                }
                break;
            case '取水户':
                {
                    this.waterDoorList(layerName);
                }
                break;
            case '在建工程':
                {
                    this.searchProjectList(layerName);
                }
                break;
            case '项目':
                {
                    this.getProjectList(layerName);
                }
                break;
            case '扰动图斑':
                {
                    this.patternSpot(layerName);
                }
                break;
            case '遥感影像':
                {
                    this.remoteSensing(layerName);
                }
                break;
            case '地下机井':
                {
                    this.waterWellsList(layerName);
                }
                break;
            case '地下水位站':
                {
                    this.waterStationsList(layerName);
                }
                break;
            case '气象预报':
                {
                    window.open("https://embed.windy.com/embed2.html?lat=53.278&lon=-161.719&detailLat=37.020&detailLon=156.270&width=1920&height=950&zoom=3&level=surface&overlay=wind&product=ecmwf&menu=&message=true&marker=&calendar=now&pressure=&type=map&location=coordinates&detail=&metricWind=default&metricTemp=default&radarRange=-1");
                }
                break;
            case "人工水道临水线点图层":
            case "人工水道临水线图层":
            case "人工水道中心线图层":
            case "人工水道水面线图层":
            case "人工水道管理范围线图层":
            case "人工水道水域面图层":
            case "山塘临水线点图层":
            case "山塘临水线图层":
            case "山塘管理范围线图层":
            case "山塘水面线图层":
            case "山塘水域面图层":
            case "拦水坝（堰）图层":
            case "船闸图层":
            case "其他工程图层":
            case "泵站图层":
            case "水闸图层":
            case "码头图层":
            case "桥梁图层":
            case "箱涵图层":
            case "管理房图层":
            case "规模以下水闸图层":
            case "断面图层":
            case "测站平台图层":
            case "提防图层":
            case "其他水域临水线点图层":
            case "其他水域临水线图层":
            case "其他水域管理范围线图层":
            case "其他水域水面线图层":
            case "其他水域水域面图层":
            case "水库临水线点图层":
            case "水库临水线图层":
            case "水库管理范围线图层":
            case "水库水面线图层":
            case "水库水域面图层":
            case "河道临水线点图层":
            case "河道临水线图层":
            case "河道水面线图层":
            case "河道管理范围线图层":
            case "河道中心线图层":
            case "暗河图层":
            case "其他沟渠图层":
            case "河道水域面图层":
            case "水域断面点图层":
            case "水域断面线图层":
                {
                    this.MapComponent.lodeRiverFeatureLayers(data);
                }
                break;
            case "河道蓝线": {
                this.MapComponent.loadRiverLine(true);
            } break;
        }
    },
    unloadPointByName(layerName) {
        console.log(layerName, 999);
        switch (layerName) {
            case "水库水位":
                {
                    this.MapComponent.drawClusterPoint([], {}, 'RRnormal');
                    this.MapComponent.drawClusterPoint([], {}, 'RRabnormal');
                }
                break;
            case "河道水位":
                {
                    this.MapComponent.drawClusterPoint([], {}, 'ZZnormal');
                    this.MapComponent.drawClusterPoint([], {}, 'ZZabnormal');
                }
                break;
            case "堰闸水位":
                {
                    this.MapComponent.drawClusterPoint([], {}, 'DDnormal');
                    this.MapComponent.drawClusterPoint([], {}, 'DDabnormal');
                }
                break;
            case "潮位":
                {
                    this.MapComponent.drawClusterPoint([], {}, 'TTnormal');
                    this.MapComponent.drawClusterPoint([], {}, 'TTabnormal');
                }
                break;
            case '项目':
                {
                    this.MapComponent._drawClusterPoint([], {}, 'wisdomSite');
                    this.MapComponent._drawClusterPoint([], {}, 'wisdomSite1');
                    this.MapComponent._drawClusterPoint([], {}, 'wisdomSite2');
                }
                break;
            case "发展规划":
                {
                    this.MapComponent._riversLrawLines([], {}, "line");
                    this.MapComponent.drawClusterPoint([], {}, "pointLayer");
                    this.MapComponent.drawClusterPoint([], {}, "pointLayer0");
                    this.MapComponent.drawClusterPoint([], {}, "pointLayer1");
                    this.MapComponent.drawClusterPoint([], {}, "pointLayer2");
                    this.MapComponent.drawClusterPoint([], {}, "pointLayer3");
                    this.MapComponent.drawClusterPoint([], {}, "pointLayer4");
                    this.MapComponent.drawClusterPoint([], {}, "pointLayer5");
                }
                break;
            case "综合规划":
                {
                    // this.MapComponent._riversLrawLines([], {}, "line");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer0");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer1");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer2");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer3");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer4");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer5");
                }
                break;
            case "专业规划":
                {
                    // this.MapComponent._riversLrawLines([], {}, "line");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer0");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer1");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer2");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer3");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer4");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer5");
                }
                break;
            case "专项规划":
                {
                    // this.MapComponent._riversLrawLines([], {}, "line");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer0");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer1");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer2");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer3");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer4");
                    // this.MapComponent.drawClusterPoint([], {}, "pointLayer5");
                }
                break;
            case "清四乱":
                {
                    this.MapComponent._drawClusterPoint([], {}, 'QingFourDisorderly');
                    this.MapComponent._drawClusterPoint([], {}, 'QingFourDisorderly1');
                    this.MapComponent._drawClusterPoint([], {}, 'QingFourDisorderly2');
                    this.MapComponent._drawClusterPoint([], {}, 'QingFourDisorderly3');
                    this.MapComponent._drawClusterPoint([], {}, 'QingFourDisorderly4');
                }
                break;
            case "水质监测":
                {
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal1');
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal2');
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal3');
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal4');
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal5');
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal6');
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal7');
                }
                break;
            case "在建工程":
                {
                    this.MapComponent._drawClusterPoint([], {}, 'engineeringPoint');
                    this.MapComponent._drawClusterPoint([], {}, 'engineeringPoint1');
                    this.MapComponent._drawClusterPoint([], {}, 'engineeringPoint2');
                    this.MapComponent._drawClusterPoint([], {}, 'engineeringPoint3');
                }
                break;
            case '积水':
                {
                    this.MapComponent.drawClusterPoint([], {}, 'hydropsNormal');
                    this.MapComponent.drawClusterPoint([], {}, 'hydropsAbnormal');
                }
                break;
            case '海塘':
                {
                    this.MapComponent.removeLineLayer();
                    this.MapComponent.showProjectPoint([], '海塘');
                }
                break;
            case "墒情":
                {
                    this.MapComponent.drawClusterPoint([], {}, 'moisturePoint');
                }
                break;
            case "流量":
                {
                    this.MapComponent.drawClusterPoint([], {}, "flownormal");
                }
                break;
            case "堤防":
                {
                    this.MapComponent.showProjectPoint([], '堤防');
                    this.MapComponent.removeLineLayer()
                }
                break;
            case "水库":
            case "山塘":
            case "泵站":
            case "水闸":
            case "闸站":
            case "灌区":
            case "农饮水":
            case "水电站":
            case "码头":
            case "桥梁":
            case "箱涵":
            case "管理房":
            case "测站平台":
            case "规模以下水闸":
            case "断面":
            case "海塘道路交叉出入口":
            case "旱闸":
            case "桩号":
                {
                    const projectObj = {
                        '水库': 'reservoir',
                        '山塘': 'pool',
                        '泵站': 'pump',
                        '水闸': 'sluice',
                        '闸站': 'sluicestation',
                        '灌区': 'irrigated',
                        '农饮水': 'farmwater',
                        '水电站': 'hydropower',
                        '码头': 'matou',
                        '桥梁': 'qiaoliang',
                        '断面': 'duanmian',
                        '海塘道路交叉出入口': 'jccrk',
                        '避灾点': 'bizaidian',
                        '箱涵': 'xianghan',
                        '管理房': 'guanli',
                        '规模以下水闸': 'watergate',
                        '旱闸': 'hanzha',
                        '测站平台': 'cezhan',
                        '桩号': 'zhuanghao'
                    }
                    this.MapComponent.drawClusterPoint([], {}, projectObj[layerName] + 'normal');
                }
                break;
            case '台风分布':
                {
                    this.MapComponent.clearAllTyphoon();
                }
                break;
            case '风场预报':
                {
                    this.MapComponent.closeWeatherLayers("wind");
                }
                break;
            case '浪场预报':
                {
                    this.MapComponent.closeWeatherLayers("wave");
                }
                break;
            case "卫星云图":
                {
                    this.loadClound(false);
                }
                break;
            case '雷达降雨':
                {
                    this.radarRain(false);
                }
                break;
            // 河湖信息开始
            case '河道':
                {
                    this.MapComponent._riversLrawLines([], {}, 'riverwayLines');
                }
                break;
            case '湖泊':
                {
                    // this.radarRain(false);
                }
                break;
            case "水闸工情":
                {
                    this.MapComponent.drawClusterPoint([], {}, 'DDnormal');
                    this.MapComponent.drawClusterPoint([], {}, 'DDabnormal');
                }
                break;
            case "泵站工情":
                {
                    this.MapComponent.drawClusterPoint([], {}, 'PumpStation');
                }
                break;
            case '扰动图斑':
                {
                    this.MapComponent.showPatternLayer();
                }
                break;
            case '遥感影像':
                {
                    this.MapComponent.showSpecklesLayer();
                }
                break;
            case '河景':
                {
                    this.MapComponent._drawClusterPoint([], {}, 'riverScenery');
                }
                break;
            case '取水口':
                {
                    this.MapComponent._drawClusterPoint([], {}, 'isWater');
                    this.MapComponent._drawClusterPoint([], {}, 'stopTheWater');
                    this.MapComponent._drawClusterPoint([], {}, 'equipmentDrops');
                }
                break;
            case '雨水口':
                {
                    this.MapComponent._drawClusterPoint([], {}, 'drainOutlet');
                }
                break;
            case '入河排污口':
                {
                    this.MapComponent._drawClusterPoint([], {}, 'drainOutletS');
                }
                break;
            // 河湖信息结束
            case '取水户':
                {
                    this.MapComponent._drawClusterPoint([], {}, 'waterSampling');
                    this.MapComponent._drawClusterPoint([], {}, 'waterSampling1');
                    this.MapComponent._drawClusterPoint([], {}, 'waterSampling2');
                }
                break;
            case '地下机井':
                {
                    this.MapComponent._drawClusterPoint([], {}, 'motorPumpedPoint');
                    this.MapComponent._drawClusterPoint([], {}, 'motorPumpedPoint1');
                    this.MapComponent._drawClusterPoint([], {}, 'motorPumpedPoint2');
                    this.MapComponent._drawClusterPoint([], {}, 'motorPumpedPoint3');
                }
                break;
            case '地下水位站':
                {
                    this.MapComponent._drawClusterPoint([], {}, 'undergroundWaterStation');
                    this.MapComponent._drawClusterPoint([], {}, 'undergroundWaterStation1');
                }
                break;
            case '生产建设项目':
                {
                    this.MapComponent._drawPoints([], {}, 'scjsxmPointLayer');
                }
                break;
            case "河道蓝线":
                {
                    this.MapComponent.loadRiverLine(false);
                } break;
            default:
                {
                    this.MapComponent.clearLayerByName(layerName);
                }
        }
    }

}