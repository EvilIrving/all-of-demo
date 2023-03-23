import { hydraulicApi } from '@/api/hydraulic_amap/index.js';
//水利工程相关操作module
export const projectModule = {
	//获取工程点位
	async getProjectPoint(type) {
		const projectObj = {
			水库: 'reservoir',
			山塘: 'pool',
			泵站: 'pump',
			水闸: 'sluice',
			闸站: 'sluicestation',
			灌区: 'irrigated',
			农饮水: 'farmwater',
			水电站: 'hydropower',
			码头: 'matou',
			桥梁: 'qiaoliang',
			海塘道路交叉出入口: 'jccrk',
			旱闸: 'hanzha',
			箱涵: 'xianghan',
			管理房: 'guanli',
			规模以下水闸: 'watergate',
			测站平台: 'cezhan',
			桩号: 'zhuanghao',
			避灾点: 'bizaidian',
			断面: 'duanmian'
		};
		let opt = {
			type,
			withWaterLevel: true
		};
		let res = {}
		let list = []
		if (type === '断面') {
			res = await hydraulicApi.sectionList(opt);
			list = res.data.list;
		} else {
			res = await hydraulicApi.getProjects(opt);
			list = res.data
		}
		let normalArr = [];
		let abnormalArr = [];
		let normalStyle = {
			scale: 0.7,
			anchor: [27, 30],
			anchorXUnits: 'pixels'
		};
		let abnormalStyle = {
			scale: 0.7,
			anchor: [27, 30],
			anchorXUnits: 'pixels'
		};
		let srcKey = projectObj[type];
		list.forEach(item => {
			let obj = {
				...item,
				latitude:
					Number(item.lowLeftLat ||
						item.dtMidLat ||
						item.pustLat ||
						item.startLat ||
						item.cwsLat ||
						item.hystLat ||
						item.latitude ||
						item.lat ||
						item.stakeLatitude),
				longitude:
					Number(item.lowLeftLong ||
						item.dtMidLong ||
						item.pustLong ||
						item.startLong ||
						item.cwsLong ||
						item.hystLong ||
						item.longitude ||
						item.lng ||
						item.stakeLongitude),
				type: type,
				props: this.getPropByType(type, item),
				text: this.getTextByType(type, item)
			};

			normalArr.push(obj);
		});
		//堤防没有线数据先画点
		if (type == '堤防') {
			if (type == '海塘') this.MapComponent.handleSeawallChange(list);
			if (type == '堤防') this.MapComponent.drawCoorpotLine(list, '堤防');
			this.showProjectPoint(list, type);
		} else if (type == '海塘') { // 海塘画线
			let htRes = await hydraulicApi.rightMapPoints({ type: "海塘" })
			this.drwaLine(htRes.data)
		}
		else if (type == '码头' || type == '桥梁' || type == '海塘道路交叉出入口' || type == '旱闸' || type == '桩号' || type == '箱涵' || type == '管理房' || type == '测站平台' || type == '规模以下水闸' || type == '断面') {
			normalStyle = {
				...normalStyle,
				src: require(`@/assets/images/map_legends/map-${srcKey}.png`),
				srcs: require(`@/assets/images/map_legends/map-${srcKey}.png`)
			};
			this.MapComponent.drawClusterPoint(normalArr, normalStyle, srcKey + 'normal');
		}
		else {
			normalStyle = {
				...normalStyle,
				src: require(`@/assets/images/map_legends/map-${srcKey}.png`),
				srcs: require(`@/assets/images/map_legends/map-${srcKey}-cluster.png`)
			};
			this.MapComponent.drawClusterPoint(normalArr, normalStyle, srcKey + 'normal');
		}
		// this.showProjectPoint(list, type);
	},
	drwaLine(list) {
		for (let seawall of list) {
			let color = "#E80000";
			if (seawall.designTideStandard < 10) {
				color = "#E80000";
			} else if (seawall.designTideStandard == 10) {
				color = "#DD00DB";
			} else if (seawall.designTideStandard == 20) {
				color = "#E89100";
			} else if (seawall.designTideStandard == 50) {
				color = "#EEC80B";
			} else if (seawall.designTideStandard == 100) {
				color = "#19AF47";
			} else if (seawall.designTideStandard > 100) {
				color = "#4CEBC7";
			} else if (seawall.designTideStandard == null) {
				color = "#8D0A0A";
			}
			let style = {
				lineWidth: 4,
				lineColor: color,
			};
			let lineString =
				(seawall.coorpot && seawall.coorpot.replaceAll("&quot;", "'")) || "[]";
			let linelist = lineString && eval(lineString);
			linelist = linelist.map((line) => {
				line = line.map((item, index) => {
					return [item.lng, item.lat];
				});
				return {
					lineList: line,
					data: {
						type: "海塘",
						name: seawall.seawallName,
						code: seawall.seawallCode,
						props: {
							海塘长度: seawall.seawallLength + "m",
							防潮标准: seawall.designTideStandard + "年",
							所在位置: seawall.village,
						},
						...seawall,
					},
				};
			});
			this.MapComponent.drawLineEx(linelist, style);
		}
	},
	getPropByType(type, data) {
		let obj = {};
		switch (type) {
			case '水库':
				{
					obj = {
						名称: data.resName,
						实时水位: (data.waterLevel || '-') + 'm',
						汛限水位: (data.currentLimitLevel || '-') + 'm' || '-',
						更新时间: data.dataTime || '-',
						鉴定结论: data.safetyConclusion || '-'
					};
				}
				break;
			case '山塘':
				{
					obj = {
						名称: data.mnpdName || '-',
						山塘类型: data.mnpdCate || '-',
						'总容积(万m³)': data.jhhsRj || '-'
					};
				}
				break;
			case '泵站':
				{
					obj = {
						名称: data.pustName || '-',
						泵站规模: data.engScal || '-',
						'装机流量(m³/s)': data.insFlow || '-'
					};
				}
				break;
			case '水闸':
				{
					obj = {
						名称: data.wagaName || '-',
						水闸规模: data.engScal || '-',
						'最大过闸流量(m³/s)': data.desLockDisc || '-'
					};
				}
				break;
			case '闸站':
				{
					obj = {
						名称: data.gateName || '-',
						闸站规模: data.engScal || '-',
						'过闸流量(m³/s)': data.maxFlow || '-'
					};
				}
				break;
			case '水电站':
				{
					obj = {
						名称: data.hystName || '-',
						水电站规模: data.engScal || '-',
						'总装机容量(kW)': data.totInsCap || '-'
					};
				}
				break;
			case '农饮水':
				{
					obj = {
						名称: data.cwsName || '-'
					};
				}
				break;
			case "码头":
			case "桥梁":
			case "海塘道路交叉出入口":
			case "旱闸":
			case "箱涵":
			case "管理房":
			case "规模以下水闸":
			case "测站平台":
				{
					obj = {
						名称: data.proName || '-',
						主体工程: data.prnm || '-',
						类型: data.proType || '-',
					};
				}
				break;
			case "桩号":
				{
					obj = {
						桩号: data.stake || '-'
					};
				}
				break;
			case "断面":
				{
					obj = {
						名称: data.name || '-'
					};
				}
				break;
			default: {
				obj = {
					名称: data.name || '-'
				};
			}
		}
		return obj;
	},
	getTextByType(type, data) {
		let text = "";
		switch (type) {
			case '水库':
				{
					text = `${data.resName} 实时:${data.waterLevel || '-'}m,汛限:${data.currentLimitLevel || '-'}m`;
				}
				break;
			case '山塘':
				{
					text = `${data.mnpdName} 总容积:${data.jhhsRj || '-'}万m³`;
				}
				break;
			case '泵站':
				{
					text = `${data.pustName} 装机流量:${data.insFlow || '-'}m³/s`;
				}
				break;
			case '水闸':
				{
					text = `${data.wagaName} 最大过闸流量:${data.desLockDisc || '-'}m³/s`;
				}
				break;
			case '闸站':
				{
					text = `${data.gateName} 过闸流量:${data.maxFlow || '-'}m³/s`;
				}
				break;
			case '水电站':
				{
					text = `${data.hystName} 总装机容量:${data.totInsCap || '-'}kW`;
				}
				break;
			case '农饮水':
				{
					text = `${data.cwsName}`;
				}
				break;
			case "码头":
			case "桥梁":
			case "海塘道路交叉出入口":
			case "旱闸":
			case "箱涵":
			case "测站平台":
			case "规模以下水闸":
			case "管理房":
				{
					text = `${data.proName || '-'}`;
				}
				break;
			case "桩号":
				{
					text = `${data.stake || '-'}`;
				}
				break;
			default: {
				text = `${data.name || '-'}`;
			}
		}
		return text;
	},
	//展示工程点位
	showProjectPoint(list, type) {
		this.MapComponent.showProjectPoint(list, type);
	},
	//工程弹框
	showProjectDialog(data, feature) {
		console.log(data,'data.type');
    if (data.type == '堤防' && !data.dikeCode) {
      return false
    }
		switch (data.type) {
			case '山塘':
			case '闸站':
			case '灌区':
			case '农饮水':
			case '水电站':
				{
					this.removeTabs = ['运行管理'];
				}
				break;
			default: {
				this.removeTabs = [];
			}
		}
		this.dialogTitle = data.projectName || data.resName || data.seawallName || data.dikeName || data.mnpdName || data.pointName;
		this.projectDetailData = data;
		this.projectDetailData.name = data.projectName || data.resName || data.seawallName || data.dikeName || data.mnpdName || data.pointName;
		this.projectDetailData.code =
			data.code ||
			data.resCode ||
			data.mnpdCode ||
			data.seawallCode ||
			data.dikeCode ||
			data.wagaCode ||
			data.hystCode ||
			data.pustCode ||
			data.irrCode ||
			data.cwsCode ||
			data.id ||
			data.gateCode;
		this.projectDetailData.type = data.type;
		//周边分析统一经纬度字段
		this.projectDetailDialogShow = true;
	},
	//关闭工程弹框
	handleProjectDetailClose() {
		this.projectDetailDialogShow = false;
		this.projectDetailData = {};
	},

	//交叉建筑物弹窗 以及 断面
	async showJcBuildingDialog(data) {
		this.buildingDetailDialogShow = true;
		this.dialogTitle = data.name || data.proName;
		let res = {}
		if (data.type === '断面') {
			res = await hydraulicApi.sectionWindows({
				id: data.id
			})
			res.data.type = '断面'
		}
		this.buildingDetailData = data.type == '断面' ? res.data : data;
	},
	handleBuildingDetailClose() {
		this.buildingDetailDialogShow = false;
		this.buildingDetailData = {};
	},

	//桩号弹窗
	showZhuanghaoDialog(data) {
		this.zhuanghaoDetailDialogShow = true;
		this.dialogTitle = data.stake ? data.stake : '-';
		this.zhuanghaoDetailData = data;
	},
	handleZhuanghaoDetailClose() {
		this.zhuanghaoDetailDialogShow = false;
		this.zhuanghaoDetailData = {};
	},
};
