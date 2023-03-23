import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
//水利工程相关操作module
export const projectModule = {
    //获取工程点位
    async getProjectPoint(type) {
        // console.log(type,3333)
        const projectObj = {
            '水库':'reservoir',
            '山塘':'pool',
            '泵站':'pump',
            '水闸':'sluice',
            '闸站':'sluicestation',
            '灌区':'irrigated',
            '农饮水':'farmwater',
            '水电站':'hydropower'
        }
        let opt = {
            type,
            withWaterLevel:true
        };
        let res = await hydraulicApi.getProjects(opt);
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
        let srcKey = projectObj[type];
        let list = res.data;
        list.forEach(item=>{
            let obj = {
              ...item,
              latitude:item.lowLeftLat || item.dtMidLat || item.pustLat || item.startLat || item.lowLeftLat || item.cwsLat || item.hystLat || item.latitude,
              longitude:item.lowLeftLong || item.dtMidLong || item.pustLong || item.startLong || item.lowLeftLong || item.cwsLong || item.hystLong || item.longitude,
              type:type,
              props:this.getPropByType(type,item),
            }
            normalArr.push(obj);
        })
        //堤防海塘没有线数据先画点
        if(type == '堤防' || type == '海塘'){
            this.showProjectPoint(list,type);
        }else{
            normalStyle = {
                ...normalStyle,
                src:require(`@/assets/images/map_legends/map-${srcKey}.png`),
                srcs:require(`@/assets/images/map_legends/map-${srcKey}-cluster.png`),
            }
            this.MapComponent.drawClusterPoint(normalArr,normalStyle,srcKey+'normal');
        }
        // this.showProjectPoint(list, type);
    },
    getPropByType(type,data){
        let obj = {};
        switch(type){
            case '水库':{
                obj = {
                  "名称":data.resName,
                  "实时水位":(data.waterLevel || "-") + 'm' ,
                  "汛限水位":(data.currentLimitLevel || "-") + 'm' || "-",
                  "更新时间":(data.dataTime) || "-",
                  "鉴定结论":(data.safetyConclusion) || "-"
                }
            }break;
            case '山塘':{
                obj = {
                  "名称":data.mnpdName|| "-" ,
                  "山塘类型":data.mnpdCate || "-",
                  "总容积(万m³)": data.totalVol || "-",
                }
            }break;
            case '泵站':{
                obj = {
                  "名称":data.pustName || "-",
                  "泵站规模":data.engScal || "-",
                  "装机流量(m³/s)":data.insFlow || "-",
                }
            }break;
            case '水闸':{
                obj = {
                  "名称":data.wagaName || "-",
                  "水闸规模":data.engScal || "-",
                  "最大过闸流量(m³/s)":data.desLockDisc || "-",
                }
            }break;
            case '闸站':{
                obj = {
                  "名称":data.gateName || "-",
                  "闸站规模":data.engScal || "-",
                  "过闸流量(m³/s)":data.maxFlow || "-",
                }
            }break;
            case '水电站':{
                obj = {
                  "名称":data.hystName || "-",
                  "水电站规模":data.engScal || "-",
                  "总装机容量(kW)":data.totInsCap || "-"
                }
            }break;
            case '农饮水':{
                obj = {
                    "名称":data.cwsName || "-",
                  }
                
            }break;
            default:{
                obj = {
                    "名称":data.name || "-"
                }
            }
        }
        return obj;
    },
    //展示工程点位
    showProjectPoint(list, type) {
        // console.log(list,222);
        this.MapComponent.showProjectPoint(list, type);
    },
    //工程弹框
    showProjectDialog(data, feature) {
        switch (feature.type) {
            case '山塘':
                {
                    this.removeTabs = ['运行管理'];
                }
                break;
            case '闸站':
            case '灌区':
                {
                    this.removeTabs = ['运行管理'];
                }
                break;
            case '农饮水':
                {
                    this.removeTabs = ['运行管理'];
                }
                break;
            case '水电站':
                {
                    this.removeTabs = ['运行管理'];
                }
                break;
            default:
                {
                    this.removeTabs = [];
                }
        }
        this.dialogTitle = data.resName || data.seawallName || data.dikeName;
        this.projectDetailData = data;
        this.projectDetailData.name = data.resName || data.seawallName || data.dikeName;
        this.projectDetailData.code = data.resCode || data.mnpdCode || data.seawallCode || data.dikeCode || data.wagaCode || data.hystCode || data.pustCode || data.irrCode || data.cwsCode || data.gateCode;
        this.projectDetailData.type = data.type;
        //周边分析统一经纬度字段
        this.projectDetailData.latitude = feature.lat;
        this.projectDetailData.longitude = feature.lng;
        this.projectDetailDialogShow = true;
    },
    //关闭工程弹框
    handleProjectDetailClose() {
        this.projectDetailDialogShow = false;
        this.projectDetailData = {};
    },
}