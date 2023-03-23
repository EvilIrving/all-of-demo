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
            case "雨情":
                {
                    this.getRainPoint(layerName);
                }
                break;
            case "国控断面":
            case "省控断面":
            case "水文站水质":
                {
                    this.getWaterQualityPoint(layerName);
                }
                break;
            case "积水":
                {
                    this.getLoggingPoint(layerName);
                    // this.MapComponent.showCityAreaLine();
                    // this.MapComponent.clearAreaLayer();
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
        }
    },
    unloadPointByName(layerName) {
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
            case "水文站水质":
                {
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal1');
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal2');
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal3');
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal4');
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal5');
                    this.MapComponent.drawClusterPoint([], {}, 'waterQualityAbnormal6');
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
                }
                break;
            case "水库":
            case "山塘":
            case "堤防":
            case "泵站":
            case "水闸":
            case "闸站":
            case "灌区":
            case "农饮水":
            case "水电站":
                {
                    const projectObj = {
                        '水库': 'reservoir',
                        '山塘': 'pool',
                        '泵站': 'pump',
                        '水闸': 'sluice',
                        '闸站': 'sluicestation',
                        '灌区': 'irrigated',
                        '农饮水': 'farmwater',
                        '水电站': 'hydropower'
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
            default:
                {
                    this.MapComponent.clearLayerByName(layerName);
                }
        }
    }

}