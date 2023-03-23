<script>
import { factoryCardComponent, factoryStatistics, getChildComponent } from './utils.js';
import {
  DragCard,
  PrimaryDialog,
  PrimaryLegend,
  ProjectDialog,
  SilderTimelineBar,
  PlayableTimelineBar
} from '@/components';
import ScreenMap from './components/ScreenMap';
// import RenderTabControl from './components/RenderTabControl';
import SeawallRangDialog from './dialog/seawallRangDialog.vue';
import simpleRangDialog from './dialog/simpleRangDialog.vue';
// 约定式引入组件 见readme.md
//极简模式组件
import PotentialRisks from './components/simple/PotentialRisks.vue';
import PotentialRiskList from './components/simple/PotentialRiskList.vue';
import SimpleCount from './components/simple/SimpleCount.vue';
import SimpleSluiceDialog from './dialog/SimpleSluice.vue';
//  落点弹窗
import WorkWaterDialog from './dialog/teamPointDialog.vue';
import ComponentList, { ComponentList4kebabCase } from './cards';
import StatComponentList from './countCards';
import SelectCards from './components/SelectCards.vue';
import { mapActions, mapGetters } from 'vuex';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index.js';
import DetailsCommonDialog from './dialog/DetailsCommonDialog/index.vue';
import RainDialogContent from '../hydraulic_amap/components/dialog/StationDialog';
//山洪弹框
import FloodDetailContent from './dialog/FloodDetailContent.vue';
//避灾点弹框
import DisasterSiteDialog from './dialog/DisasterSiteDialog.vue';
//风险点弹框
import RiskDialogContent from '../WaterDisasterPreventionScreen/components/RiskPointContent.vue';
//洪水预报弹窗
import FloodForecastingDialog from './dialog/FloodForecastingDialog ';
//  水研究详情弹框
import WaterStudyDetails from './dialog/WaterStudyDetails';

// 趋势分析
import TrendAnalysisDialog from './cards/TrendAnalysisDialog';
import PreviewCard from '../WaterDisasterPreventionScreen/components/PreviewCard.vue';
// 大中型水库
import BigReservoirDialog from './dialog/BigReservoirDialog';
//选点提示
import SelectionTipsDialog from './dialog/SelectionTipsDialog';
// 调度方案弹框
import EmergencySchemeDialog from './dialog/EmergencySchemeDialog';
import ForeCastDialog from './dialog/ForeCastDialog';
//发送抢险信息
import SendMsgDialog from './dialog/SendMsgDialog';
import ModuleAnalyDialog from './dialog/ModuleAnalyDialog';
// 水库蓄水量落点弹窗
import ReservoirDialog from './dialog/ReservoirDialog';
// 天气实况风力分布弹窗
import WeatherDialog from './dialog/WeatherDialog';
// 水源供水弹窗
import waterSourDialog from './dialog/waterSourDialog';
// 珊溪总览 落点弹窗
import shanXiZLDialog from './dialog/ShanXiZLDialog';
// 珊溪巡查 落点弹窗
import shanXiXcDialog from './dialog/ShanXiXcDialog';
// 取水监管 落点弹窗
import waterRegulationDialog from './dialog/WaterRegulationDialog';
// 水政务协同
import governmentDialog from './dialog/governmentDialog';
//堤防详表
import dikeDialog from './dialog/dikeDialog';
//堤防落点弹窗
import dikePointDialog from './dialog/dikePointDialog';

// 河道详表
import riverwayDialog from './dialog/riverwayDialog';
//泵站详表
import pumpStationDialog from './dialog/pumpStationDialog';
//泵站落点弹窗
import pumpPointDialog from './dialog/pumpPointDialog';
//山塘详表
import MountainPondDialog from './dialog/MountainPondDialog';
// 清四乱
import FourChaosDialog from './dialog/FourChaosDialog';
// 美丽河湖
import beautifulRiverDialog from './dialog/beautifulRiverDialog';
//
import WaterIntakeDialog from './dialog/WaterIntakeDialog';
import SluiceDialogForSchedule from './dialog/SluiceDialogForSchedule/index.vue';
// 水电站
import HydropowerStationDialog from './dialog/HydropowerStationDialog';
// 山塘
import MountainPondPointDialog from './dialog/MountainPondPointDialog.vue';
import TyphoonReservoirDialog from './dialog/TyphoonReservoirDialog.vue';
import TyphoonSluiceDialog from './dialog/TyphoonSluiceDialog.vue';

import SeawallCardList from '@/views/water_disaster_custom/dialog/DetailsCommonDialog/components/cardListDetail/seawallCardList/Index.vue';
export default {
  name: 'WaterDisasterCustom',
  components: {
    SeawallCardList,
    PotentialRiskList,
    SimpleCount,
    PotentialRisks,
    DragCard,
    DisasterSiteDialog,
    // RenderTabControl,
    TrendAnalysisDialog,
    riverwayDialog,
    FloodForecastingDialog,
    BigReservoirDialog,
    EmergencySchemeDialog,
    SelectionTipsDialog,
    ModuleAnalyDialog,
    SendMsgDialog,
    ForeCastDialog,
    ReservoirDialog,
    WeatherDialog,
    WaterStudyDetails,
    waterSourDialog,
    shanXiZLDialog,
    shanXiXcDialog,
    waterRegulationDialog,
    governmentDialog,
    FourChaosDialog,
    beautifulRiverDialog,
    WaterIntakeDialog,
    HydropowerStationDialog,
    MountainPondPointDialog,
    TyphoonReservoirDialog,
    TyphoonSluiceDialog,
    dikeDialog,
    dikePointDialog,
    pumpStationDialog,
    pumpPointDialog,
    MountainPondDialog,
    PlayableTimelineBar,
    WorkWaterDialog,
    SimpleSluiceDialog,
    SeawallRangDialog,
    simpleRangDialog,
    ...ComponentList,
    ...StatComponentList
  },
  data() {
    return {
      sceneName: '',
      typhoonData: {},
      showLegend: false,
      legends: {},
      waterRainDialog: false,
      // WaterStudyDetailsShow: false,
      floodDetailDialog: false,
      disasterDialog: false,
      projectDialogVisible: false,
      BigReservoirShow: false,
      SelectionTipsShow: false,
      SendMsgShow: false,
      ReservoirShow: false, // 水库蓄水量弹窗
      WeatherShow: false, // 气象监测
      waterSourShow: false, // 水源供水
      shanXiZLShow: false, // 珊溪总览
      shanXiXcShow: false, // 珊溪巡查
      fourChaosShow: false, // 清四乱
      beautifulRiverShow: false, // 美丽河湖
      hydropowerStationShow: false, // 水电站
      mountainPondPointDialog: false, //山塘落点
      TyphoonReservoirShow: false, //台风水库
      TyphoonSluiceShow: false, // 台风水闸
      droughtResistingShow: false, //抗旱监管
      WaterIntakeShow: false, // 抗旱监管
      sluiceDialogForSchedule: false, // 防汛风险
      //洪水预报
      FloodForecastingShow: false,
      emergencySchemeShow: false,
      foreCastShow: false,
      FloodForecastingVal: [],
      riskDialog: false,
      pointData: {},
      dialogType: 'PP',
      /** 工程弹框动态移除tab */
      projectDialogRemoveTabs: [
        /*'运行管理'  "工程巡查","规划档案", "建设档案", "洪水预测", */
      ],
      cardInitFlag: true,
      dispatchLat: '',
      dispatchLng: '',
      previewActive: '预演',
      moduleAnalyShow: false,
      waterRegulationShow: false, // 取水监管
      governmentDialogShow: false, // 水政务协同弹窗
      dikeDialogShow: false, //堤防弹窗
      dikePointDialogShow: false, //堤防落点弹窗
      citytaxiDialogShow: false, //市内出租车
      pumpStationDialogShow: false, //泵站弹窗
      pumpPointDialogShow: false, //泵站落点弹窗
      MountainPondDialogShow: false, //山塘详情弹窗
      governmentDialogCode: '出差管理',
      realtimePreList: {}, //实时降雨数据对象
      forecastPreList: {}, //预报降雨数据对象
      waterPointDialogShow: false,
      simpleSluiceDialogShow: false,
      workPointDialogShow: false,
      teamPointDialogShow: false,
      callThePoliceDialogShow: false
    };
  },
  mounted() {
    this.changePreviewModule(false);
    this.createEventHandler();
    //设置大屏标记
    let bigScreenflag = this.$route.query.isBigScreen == 'true';
    this.SetBigScreen(bigScreenflag);
    //获取实时雨量数据
    this.getRealRainfallData();
    //获取预报降雨数据
    this.getPreRainData();
    if (this.qiankunModule) {
      this.SetBsName(this.$localData('get', 'systemType'));
    }
    this.$nextTick(() => {
      // this.$route.query.large
      //存入$store
      this.SetMapComponent(this.$refs.map);
      //设置卡片高度
      this.handleWindowResize();
      const bsName = this.BsName;
      const mode = this.qiankunModule ? 2 : 1;
      const opt = { bsName, mode };
      //获取当前大屏场景列表
      this.GetSceneList(opt).then((res) => {
        let defaultScene = {
          name: res[0].sceneName,
          id: res[0].id,
          editable: res[0].editable
        };
        res.forEach((item) => {
          //PC模式下默认加载个人设置的场景
          if (item.userDefaultScene) {
            defaultScene = {
              id: item.id,
              name: item.sceneName,
              editable: item.editable
            };
          }
        });
        this.SetActiveScene(defaultScene);
        //获取当前场景组件信息列表
        const param = {
          bsName: this.BsName,
          id: defaultScene.id,
          mode
        };
        this.GetSceneComponentInfo(param).then((result) => {
          this.SetActiveCount(result[0]);
        });
      });
      this.previewActive = this.warningListShow ? '预演' : '预演';
    });
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleWindowResize);
  },
  computed: {
    ...mapGetters([
      'DoSceneSaveData',
      'CheckedArr',
      'CheckedNoBxArr',
      'CardHeight',
      'AddCardDialog',
      'SaveAsDialog',
      'ActiveScene',
      'SceneList',
      'WaterStudyDetailsShow',
      'SceneComponentList',
      'DetailDialogParam',
      'StatisticFlag',
      'ActiveCount',
      'IsCollapse',
      'isBigScreen',
      'formatterStartTime',
      'formatterEndTime',
      'warningListShow',
      'ComponentLoading',
      'previewModule',
      'TempZIndex',
      'BsName',
      'BsNameObj',
      'riverwayShow',
      'trendAnalysisShow',
      'RangeBar',
      'TimelineList',
      'seawallRangModule',
      'Simple',
      'simpleRightType',
      'sluiceDistributionMap',
      'simpleTimes',
      'Aligning'
    ]),
    cardPos() {
      const top1 = '124px';
      const top2 = parseInt(this.CardHeight) + 124 + 6 + 'px';
      const top3 = parseInt(top2) + parseInt(this.CardHeight) + 6 + 'px';
      return {
        leftpos1: { left: '0px', top: top1 },
        leftpos2: { left: '0px', top: top2 },
        leftpos3: { left: '0px', top: top3 },
        leftpos4: { left: '410px', top: top1 },
        leftpos5: { left: '410px', top: top2 },
        leftpos6: { left: '410px', top: top3 }
      };
    },
    /**
     * 卡片组件配置
     * @type {[key:string]:{component:VNode|string;props:any;cardProps:{isHight?:boolean}}}
     */
    cardMap() {
      // 水利数据资产大屏所属组件
      const DataAsset = {
        数据治理: { component: <data-governance-card />, cardProps: { isHigh: true } },
        数据责任: { component: <data-duty-card />, cardProps: { isHigh: true } },
        数据分布: { component: <data-distributed-card />, cardProps: { isHigh: true } },
        数据服务: { component: <data-service-card />, cardProps: { isHigh: true } },
        数据汇聚: {
          component: <data-gather-card />,
          props: { on: { 'legend-change': this.handleLegendChange } },
          cardProps: { isHigh: true }
        }
      };
      // 水灾害防御大屏所属组件
      const DisasterPrevention = {
        // 鹿城新增卡片
        水情: {
          component: <sluice-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        工情: {
          component: <work-conditions-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        // 救援队伍: {
        //   component: <rescue-team-card />,
        //   props: {
        //     on: { 'legend-change': this.handleLegendChange }
        //   }
        // },
        报警: {
          component: <call-the-police-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        },
        // 原有卡片

        短临预报: {
          component: <rain-forecast-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        山洪预警: {
          component: <MountainTorrentsCard />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        天气实况: {
          component: <panel-weather-info />,
          props: { key: 'weather-info', on: { 'legend-change': this.handleLegendChange } }
        },
        水库: {
          component: <reservoir-card />,
          props: {
            on: {
              'legend-change': this.handleLegendChange,
              FloodForecasting: this.FloodForecasting
            }
          }
        },

        河网: {
          component: <river-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        潮位: {
          component: <tide-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        病险工程: {
          component: <dangerous-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        风险隐患: {
          component: <risk-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        大中型水库: {
          component: <big-reservoir-card />,
          props: {
            on: {
              'legend-change': this.handleLegendChange,
              FloodForecasting: this.FloodForecasting
            }
          }
        },
        海塘: {
          component: <seawall-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        水利设施保险: {
          component: <Water-insurance-facility-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        实时降雨: {
          component: <real-rain-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        山洪: {
          component: <mountain-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        },
        救援仓库: {
          component: <stagnant-water-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        视频监控: {
          component: <video-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        台风: {
          component: <typhoon-card />,
          props: {
            on: {
              showTyphoon: this.showTyphoon,
              'legend-change': this.handleLegendChange
            }
          },
          cardProps: { isHigh: true }
        },
        抢险支持: {
          component: <emergency-support-card />,
          props: {
            on: {
              'legend-change': this.handleLegendChange,
              SelectionTips: this.SelectionTips,
              SendMsg: this.SendMsg
            }
          }
        },
        实时台风: {
          component: <real-typhoon-card />,
          props: { typhoonList: this.typhoonData },
          cardProps: { isHigh: true }
        },

        避灾点: {
          component: <flow-monitor-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        // 洪水风险图: {
        //   component: <flood-risk-card />,
        //   props: { on: { 'legend-change': this.handleLegendChange } }
        // },

        预警信息: {
          component: <infor-card />,
          props: { on: { 'legend-change': this.handleLegendChange } }
        },
        前期影响雨量: {
          component: <prophase-influenced-rainfall-card />,
          props: {
            on: {
              openDialog: this.openGovernmentDialogShow,
              'legend-change': this.handleLegendChange
            }
          }
        }
      };
      return {
        ...DisasterPrevention,
        ...DataAsset,

        堤防: {
          component: <dike-card />,
          props: {
            on: {
              openDialog: this.openDikeDialogShow,
              'legend-change': this.handleLegendChange
            }
          }
        },
        泵站: {
          component: <pump-Station-card />,
          props: {
            on: {
              openDialog: this.openPumpStationDialogShow,
              'legend-change': this.handleLegendChange
            }
          }
        },
        山塘: {
          component: <Mountain-Pond-card />,
          props: {
            on: {
              openDialog: this.openMountainPondDialogShow,
              'legend-change': this.handleLegendChange
            }
          }
        },

        降水量: {
          component: <precipitation-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        救援队伍: {
          component: <reservoir-storage-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        水资源承载能力: {
          component: <water-res-carry-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        },
        水资源量: {
          component: <water-resource-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        供水量: {
          component: <water-supply-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        用水量: {
          component: <water-use-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        流域生态流量: {
          component: <ecological-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        水研究专题: {
          component: <water-research-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        },
        水文报警: {
          component: <hydrological-alarm-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        },
        水文预报: {
          component: <hydrological-forecast-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        河流分布: {
          component: <distribution-river-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: false }
        },
        水源供水: {
          component: <water-sour-supply-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        },
        规划专题: {
          component: <planning-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        },
        // 水资源保障
        水源保障: {
          component: <water-security-card />,
          props: {
            on: { openDialog: this.openGovernmentDialogShow }
          }
        },
        用水效率: {
          component: <water-efficiency-card />,
          props: {
            on: { openDialog: this.openGovernmentDialogShow }
          }
        },
        节水行动态势: {
          component: <water-saving-action-situation-card />,
          props: {
            on: {
              openDialog: this.openGovernmentDialogShow,
              'legend-change': this.handleLegendChange
            }
          }
        },
        //
        三条红线: {
          component: <three-red-line-card />,
          props: {
            on: {
              openDialog: this.openGovernmentDialogShow
            }
          }
        },
        //oa
        出差管理: {
          component: <government-travel-card />,
          props: {
            on: { openDialog: this.openGovernmentDialogShow }
          }
        },
        公务接待: {
          component: <government-official-card />,
          props: {
            on: { openDialog: this.openGovernmentDialogShow }
          }
        },
        合同管理: {
          component: <government-contract-card />,
          props: {
            on: { openDialog: this.openGovernmentDialogShow }
          }
        },
        资产管理: {
          component: <government-assets-card />,
          props: {
            on: { openDialog: this.openGovernmentDialogShow }
          },
          cardProps: { isHigh: true }
        },
        政务内部职能管理: {
          component: <government-inside-card />,
          props: {
            on: { openDialog: this.openGovernmentDialogShow }
          }
        },
        公务用车: {
          component: <government-official-car-card />,
          props: {
            on: { openDialog: this.openGovernmentDialogShow }
          }
        },
        其他报销: {
          component: <government-other-card />,
          props: {
            on: { openDialog: this.openGovernmentDialogShow }
          }
        },
        会议培训: {
          component: <conference-training-card />,
          props: {
            on: { openDialog: this.openGovernmentDialogShow }
          }
        },
        水域变化: {
          component: <water-change-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        美丽河湖: {
          component: <beautiful-river-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        批后监管: {
          component: <approval-supervision-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        清四乱: {
          component: <four-chaos-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: false }
        },
        取水户: {
          component: <water-intake-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        },
        水电站: {
          component: <hydropower-station-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        },
        市内出租车: {
          component: <city-taxi-card />,
          props: {
            on: { openDialog: this.openGovernmentDialogShow }
          }
        },
        河长巡查: {
          component: <river-patrol-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        数字珊溪: {
          component: <shan-xi-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        },
        闸站: {
          component: <sluice-station-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        灌区: {
          component: <irrigate-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          }
        },
        水厂供水: {
          component: <waterworks-supply-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        },
        水质监控: {
          component: <water-quality-card />,
          props: {
            on: {
              openDialog: this.openGovernmentDialogShow,
              'legend-change': this.handleLegendChange
            }
          }
        },
        洪水风险图: {
          component: <flood-risk-card />,
          props: { on: { 'legend-change': this.handleLegendChange } }
        },
        抗旱监管: {
          component: <water-intake-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        },
        防汛风险: {
          component: <hydropower-station-card />,
          props: {
            on: { 'legend-change': this.handleLegendChange }
          },
          cardProps: { isHigh: true }
        }
      };
    },
    statMap() {
      return {
        洪水风险图: <flood-risk-count />,
        抗旱监管: <water-intake-count />,
        防汛风险: <hydropower-station-count />,
        // 鹿城新增count
        水情: <sluice-count />, // 水闸修改
        工情: <work-conditions-count />,
        极简模式: <simple-count />,
        报警: <call-the-police-count />,
        // 原有count
        短临预报: <short-count />,
        天气实况: <weather-warn-count />,
        山洪预警: <mountain-warn-count key="mountainSmall" />,
        山洪: <mountain-warn-count onLegendChange={this.handleLegendChange} key="mountainBig" />,
        水库: <reservoir-count />,
        河网: <river-count />,
        潮位: <tide-count />,
        堤防: <dike-count />,
        泵站: <pump-station-count />,
        山塘: <mountain-pond-count />,
        海塘: <seawall-count />,
        风险隐患: <risk-count />,
        大中型水库: <big-reservoir-count />,
        实时降雨: <real-rain-count />,
        救援仓库: <stagnant-water-count />,
        视频监控: <video-count />,
        台风: <typhoon-count />,
        避灾点: <flow-monitor-count />,
        //报警: <warn-count />,
        洪水风险图: <flood-risk-count />,
        降水量: <precipitation-count />,
        救援队伍: <reservoir-storage-count />,
        水资源承载能力: <water-res-carry-count />,
        水资源量: <water-resource-count />,
        供水量: <water-supply-count />,
        用水量: <water-use-count />,
        水源供水: <water-sour-supply-count />,
        水厂供水: <waterworks-water-supply-count />,
        河流分布: <distribution-river-count />,
        水研究专题: <water-research-topics />,
        流域生态流量: <emergency-support-count />,
        规划专题: <planning-count />,
        // 水资源保障
        水源保障: <water-security-count />,
        用水效率: <water-efficiency-count />,
        节水行动态势: <water-saving-action-situation-count />,
        三条红线: <three-red-line-count />,
        // 水政务协同
        出差管理: <travel-count />,
        公务接待: <official-count />,
        合同管理: <contract-count />,
        资产管理: <assets-count />,
        政务内部职能管理: <inside-count />,
        公务用车: <official-car-count />,
        其他报销: <other-count />,
        会议培训: <conference-training-count />,
        水域变化: <water-change-count />,
        美丽河湖: <beautiful-river-count />,
        批后监管: <approval-supervision-count />,
        清四乱: <four-chaos-count />,
        取水户: <water-intake-count />,
        水电站: <hydropower-station-count />,
        市内出租车: <taxi-count />,
        河湖库: <river-patrol-count />,
        数字珊溪: <shan-xi-count />,
        闸站: <sluice-station-count />,
        灌区: <irrigate-count />,
        预警信息: <infor-count />,
        前期影响雨量: <prophase-influenced-rainfall-count />,
        // 数据资产
        数据治理: <data-governance-count />,
        数据责任: <data-duty-count />,
        数据服务: <data-service-count />,
        数据汇聚: <data-gather-count />,
        水质监控: <water-quality-count />
      };
    },
    cardStyle() {
      let obj = new Map();
      this.SceneComponentList.forEach((item) => {
        obj.set(item.unitName, item.unitSceneProperty);
      });
      return obj;
    },
    //判断微应用模式还是直接访问模式
    qiankunModule() {
      return window.__POWERED_BY_QIANKUN__;
    }
  },
  watch: {
    Aligning(val) {
      if (val) {
        this.setAligning();
      }
    },
    async DoSceneSaveData(val) {
      if (val) {
        await this.doSaveAs(val);
        await this.SetDoSceneSave('');
      }
    },
    StatisticFlag(val) {
      if (val) {
        !this.ActiveCount && this.SetActiveCount(this.CheckedNoBxArr[0]);
      }
    },
    CheckedNoBxArr(val) {
      const index = val.findIndex((item) => item == this.ActiveCount);
      if (index == -1) this.SetActiveCount(val[0]);
    },
    CheckedArr(components) {
      const listCurrentCardName = Object.keys(this.cardMap).filter((item) => components.includes(item));
      const listCurrentScene = listCurrentCardName.map((item) => this.cardMap[item].props.key);
      const listNeedClear = Object.keys(ComponentList4kebabCase)
        .filter((key) => !listCurrentScene.includes(key))
        .map((comp) => ComponentList4kebabCase[comp]);
      listNeedClear.forEach((item) => {
        if (item.layerName) this.removeLayers(item.layerName);
        if (item.legendName) this.removeLegends(item.legendName);
      });
    },
    warningListShow(val) {
      let data = Array.from(this.CheckedNoBxArr);
      if (val) {
        data.unshift('报警');
        this.PushCheckedNobxArr(data);
      } else {
        const idx = data.indexOf('报警');
        if (idx != -1) {
          data.splice(idx, 1);
          this.PushCheckedNobxArr(data);
        }
      }
      this.SetActiveCount(data[0]);
    }
  },
  methods: {
    ...mapActions([
      'SetAligning',
      'SetMapComponent',
      'SetCheckedArr',
      'SetCardHeight',
      'SetHighCardHeight',
      'SetAddcardDialog',
      'GetSceneList',
      'SetSaveAsDialog',
      'GetSceneComponentInfo',
      'SetActiveScene',
      'SetTrendAnalysis',
      'SetWaterResearchShow',
      'SetDetailDialogParam',
      'SetStatisticFlag',
      'SetActiveCount',
      'SetBigScreen',
      'PushCheckedNobxArr',
      'SetTempZindex',
      'setCurrentTimeatmap',
      'changePreviewModule',
      'SetBsName',
      'SetRiverway',
      'showWarningList',
      'hideWarningList',
      'setSimpleData',
      'setDistributionMap',
      'setSimpleRightType',
      'SetDoSceneSave'
    ]),
    setAligning() {
      this.SetAligning(false);
      console.log(this.SceneComponentList, 'dsadasd');
      this.SceneComponentList.forEach((item) => {
        let { component } = this.cardMap[item.unitName];
        let vueInstance = component.context;
        const componentTag = component.componentOptions.tag;
        let doc = getChildComponent(vueInstance, componentTag).$parent.$el;
        if (item.unitSceneProperty.left > '50%') {
          doc.style.left = '78.3323%';
        } else {
          doc.style.left = '0.833333%';
        }
        if (item.unitSceneProperty.top < '15%') {
          doc.style.top = '12.9948%';
        } else if (item.unitSceneProperty.top >= '15%' && item.unitSceneProperty.top < '60%') {
          doc.style.top = '40.8449%';
        } else {
          doc.style.top = '68.6936%';
        }
        doc.style.zIndex = 1020;
      });
    },
    createEventHandler() {
      // 关闭卡片
      this.$EventBus.$on('clearLayer', (payload) => {
        let { layerName, legendName, previewModule } = payload;
        if (layerName) {
          if (layerName == 'designFloodLayer') {
            //清除洪水风险图
            this.$refs.map.clearDesignFlood();
          } else if (layerName instanceof Array && layerName.includes('emergencySupport')) {
            //清除抢险支持路线
            this.$refs.map.clearDispatch();
          } else if (legendName == '水资源承载能力') {
            this.$nextTick(() => {
              this.removeLayers(layerName[0]);
            });
            this.$refs.map.removeOverlayByName(layerName[1]);
          } else if (layerName == 'waterChangeOverlay') {
            this.$refs.map.removeOverlayByName(layerName);
          } else {
            this.removeLayers(layerName);
          }
        }
        if (previewModule) {
          this.changePreviewModule(false);
          this.setCurrentTimeatmap('');
          this.clearFloodWarning();
        }
        if (legendName) this.removeLegends(legendName);
      });
      window.addEventListener('resize', this.handleWindowResize);
    },
    async clearFloodWarning() {
      await disasterPreventionApi.clearFloodWarning();
    },
    removeLayerAction(layerName) {
      if (layerName == '台风') {
        this.$refs.map.clearAllTyphoon();
      } else {
        this.$refs.map.removeLayerByName(layerName);
      }
    },
    /**
     * @param {String|Array} layerName
     */
    removeLayers(layerName) {
      if (Array.isArray(layerName)) {
        for (let name of layerName) {
          this.removeLayerAction(name);
        }
      } else {
        this.removeLayerAction(layerName);
      }
    },
    /**
     * @param {String|Array} legendName
     */
    removeLegends(legendName) {
      if (Array.isArray(legendName)) for (let name of legendName) this.handleLegendChange(name, {});
      else this.handleLegendChange(legendName, {});
    },
    handleWindowResize() {
      let highHeight = document.body.clientHeight - 190 + 'px';
      let height = parseInt(highHeight) / 3 + 'px';
      highHeight = parseInt(highHeight) + 10 + 'px';
      this.SetHighCardHeight(highHeight);
      this.SetCardHeight(height);
    },
    openAddDialog() {
      this.SetAddcardDialog(true);
    },
    handleCardClose() {
      this.SetAddcardDialog(false);
    },
    openSaveAs() {
      this.SetSaveAsDialog(true);
    },
    handleSaveAsClose() {
      this.SetSaveAsDialog(false);
    },
    //选择卡片组件确定事件
    doAddCard() {
      this.SetCheckedArr(this.$refs.SelectCards.checkList);
      this.handleCardClose();
    },
    //另存为
    async doSaveAs(id) {
      const unitList = this.getCardInfo();
      let opt = {
        bsName: this.BsName,
        unitList,
        mode: this.qiankunModule ? 2 : 1
      };
      if (id) {
        opt.id = id;
        opt.sceneName = this.ActiveScene.name;
      } else {
        if (!this.sceneName) {
          this.$message({
            type: 'warning',
            message: '请输入场景名称'
          });
          return;
        }
        opt.sceneName = this.sceneName;
      }
      let res = await disasterPreventionApi.addOrUpdateScene(opt);
      if (res.code == 0) {
        this.$message({
          type: 'success',
          message: '保存成功'
        });
        this.SetSaveAsDialog(false);
        this.sceneName = '';
      }
    },
    backBigScreen() {
      this.$router.go(-1);
    },
    //图例
    renderLegend() {
      let legends = Object.keys(this.legends)
        .reverse()
        .map((legend) => {
          let clusterBtn = '';
          if (this.legends[legend].clusterLayer) {
            let btnFlag = true;
            clusterBtn = (
              <i
                ref={this.legends[legend].clusterLayer[0]}
                class={['el-icon-location']}
                title="聚合开关"
                onClick={() => {
                  btnFlag = !btnFlag;
                  if (btnFlag) {
                    this.$refs.map.map.getLayers().forEach((element) => {
                      if (element && this.legends[legend].clusterLayer.indexOf(element.get('name')) != -1) {
                        element.getSource().setDistance(parseInt(30, 20));
                      }
                    });
                    this.$refs[this.legends[legend].clusterLayer[0]].setAttribute('class', 'el-icon-location');
                  } else {
                    this.$refs.map.map.getLayers().forEach((element) => {
                      if (element && this.legends[legend].clusterLayer.indexOf(element.get('name')) != -1) {
                        element.getSource().setDistance(0);
                      }
                    });
                    this.$refs[this.legends[legend].clusterLayer[0]].setAttribute('class', 'el-icon-location-outline');
                  }
                }}
              />
            );
          }
          return (
            <div class="legend-item-box">
              <div class="legend-name">
                <span>{legend}</span>
                {clusterBtn}
              </div>
              <div class="legend-list">
                {this.legends[legend].children.map((item) => (
                  <div class="legend-item">
                    <i class={item.icon} />
                    {item.label || ''}
                  </div>
                ))}
              </div>
            </div>
          );
        });
      return (
        <div
          v-show={this.showLegend}
          class={['map-legend', this.isBigScreen && 'is-big-screen']}
          style={{ right: '28 .5rem', bottom: '108px' }}
        >
          <PrimaryLegend title="图例" ref="mapLegend">
            <div class="legend-panel"> {legends} </div>
          </PrimaryLegend>
        </div>
      );
    },
    // 地图组件
    renderMap() {
      return (
        <ScreenMap
          onDoSceneSave={() => {
            if (this.ActiveScene.editable) {
              let sceneId = this.ActiveScene.id;
              this.doSaveAs(sceneId);
            } else {
              return;
            }
          }}
          onTyphoonPointClick={(val) => {
            this.typhoonData = {};
            this.typhoonData = val;
          }}
          onPoint-click={this.showDialog}
          onShowScheduleScheme={this.showScheduleScheme}
          onShowForecastDialog={this.showForecastDialog}
          ref="map"
        />
      );
    },
    // 右上角功能区
    renderFuncbox() {
      console.log('右上角功能区');
      return (
        <div class="func-box">
          <div class="func-item" onClick={this.backBigScreen}>
            <i class="el-icon-back"></i>
            <span>大场景</span>
          </div>
        </div>
      );
    },
    //公共详表弹窗
    renderDetailDialog() {
      return (
        this.DetailDialogParam.analyseDialogShow && (
          <DetailsCommonDialog
            code={this.DetailDialogParam.codeTpye}
            mediumSized={this.DetailDialogParam.mediumSized}
            visible={this.DetailDialogParam.analyseDialogShow}
            onClose={this.closeAnalyseDialogShow}
          />
        )
      );
    },
    closeAnalyseDialogShow() {
      this.SetDetailDialogParam({
        codeTpye: '',
        mediumSized: '',
        analyseDialogShow: false
      });
    },
    //水研究专题详表弹窗
    renderWaterStudyDetails() {
      return (
        this.WaterStudyDetailsShow && (
          <WaterStudyDetails visible={this.WaterStudyDetailsShow} onclose={this.closeWaterResearchDialogShow} />
        )
      );
    },
    // 关闭水研究详情弹窗
    closeWaterResearchDialogShow() {
      this.SetWaterResearchShow(false);
    },

    // 趋势分析
    renderTrendAnalysisDialog() {
      return (
        this.trendAnalysisShow && (
          <TrendAnalysisDialog visible={this.trendAnalysisShow} onclose={this.closeTrendAnalysisDialogShow} />
        )
      );
    },
    closeTrendAnalysisDialogShow() {
      this.SetTrendAnalysis(false);
    },
    // 河道详情弹窗
    renderRiverwayDialog() {
      return (
        this.riverwayShow && (
          <riverwayDialog ref="riverway" visible={this.riverwayShow} onCloseDialog={this.closeriverwayDialogShow} />
        )
      );
    },
    closeriverwayDialogShow() {
      this.SetRiverway(false);
    },
    // 抗旱监管
    renderWaterIntakeDialog() {
      return (
        <PrimaryDialog
          visible={this.droughtResistingShow}
          main-title={this.pointData.name}
          width="1200px"
          top="12vh"
          onHandleClose={() => this.closeWaterIntakeShow()}
        >
          <WaterIntakeDialog ref="WaterIntakeDialog" pointData={this.pointData} />
        </PrimaryDialog>

        // this.WaterIntakeShow && (
        //   <WaterIntakeDialog
        //     ref="WaterIntakeDialog"
        //     visible={this.WaterIntakeShow}
        //     onCloseDialog={this.closeWaterIntakeShow}
        //   />
        // )
      );
    },
    renderSluiceDialogForSchedule() {
      return (
        this.sluiceDialogForSchedule && (
          <PrimaryDialog
            visible={this.sluiceDialogForSchedule}
            main-title={this.pointData.name}
            width="1200px"
            onHandleClose={() => this.closeSluiceDialogForSchedule()}
          >
            <SluiceDialogForSchedule pointData={this.pointData} />
          </PrimaryDialog>

          // <SluiceDialogForSchedule
          //   ref="SluiceDialogForSchedule"
          //   visible={this.sluiceDialogForSchedule}
          //   pointData={this.pointData}
          //   onCloseDialog={this.closeSluiceDialogForSchedule}
          // />
        )
      );
    },
    //添加小切口
    renderAddDialog() {
      return (
        this.AddCardDialog && (
          <PrimaryDialog
            mainTitle="添加组件"
            hasAnalysis={false}
            visible={this.AddCardDialog}
            onHandleClose={this.handleCardClose}
            appendToBody={true}
            modal={true}
            width="1310px"
          >
            <div class="dialog-content">
              <SelectCards ref="SelectCards" />
            </div>
            <div slot="footer" class="dialog-footer">
              <el-button class="cancel" onClick={this.handleCardClose}>
                取消
              </el-button>
              <el-button class="confirm" onClick={this.doAddCard}>
                确定
              </el-button>
            </div>
          </PrimaryDialog>
        )
      );
    },
    //另存为弹框
    renderSaveAsDialog() {
      return (
        <PrimaryDialog
          mainTitle="另存为"
          hasAnalysis={false}
          visible={this.SaveAsDialog}
          onHandleClose={this.handleSaveAsClose}
          appendToBody={true}
          width="500px"
          modal={true}
        >
          <div class="dialog-content">
            <div class="scene-saveas">
              <span>场景名称:</span>
              <el-input v-model={this.sceneName}></el-input>
            </div>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button class="cancel" onClick={this.handleSaveAsClose}>
              取消
            </el-button>
            <el-button
              class="confirm"
              onClick={() => {
                this.doSaveAs('');
              }}
            >
              确定
            </el-button>
          </div>
        </PrimaryDialog>
      );
    },
    // 暴雨演练 影响村落，超汛限大中型水库落点
    showPoints(val, data) {
      if (val == '影响村落') {
        this.$refs.map.drawPoint([], {}, 'digReservoirLayers');
        let normalArr = [];
        let importArr = [];
        for (let i = 0; i < data.mountainWarnPoint.length; i++) {
          if (data.mountainWarnPoint[i].recType == '一般村落') {
            let item = data.mountainWarnPoint[i];
            item.src = require('@/assets/images/MapPoint/normal-village.png');
            item.lng = item.lon;
            item.name = item.avi;
            item.type = '山洪';
            item.props = {
              总户数: item.tho + '户',
              总人口: item.tpo + '人'
            };
            normalArr.push(item);
          } else if (data.mountainWarnPoint[i].recType == '重要村落') {
            let item = data.mountainWarnPoint[i];
            item.lng = item.lon;
            item.type = '山洪';
            item.src = require('@/assets/images/MapPoint/important-village.png');
            item.name = item.avi;
            item.props = {
              总户数: item.tho + '户',
              总人口: item.tpo + '人'
            };
            importArr.push(item);
          }
          const normalStyle = {
            src: require('@/assets/images/MapPoint/normal-village.png'),
            srcs: require('@/assets/images/MapPoint/normal-village-group.png'),
            scale: 0.7,
            anchor: [27, 30],
            anchorXUnits: 'pixels'
          };
          const importStyle = {
            src: require('@/assets/images/MapPoint/important-village.png'),
            srcs: require('@/assets/images/MapPoint/important-village-group.png'),
            scale: 0.7,
            anchor: [27, 30],
            anchorXUnits: 'pixels'
          };
          this.$refs.map.drawClusterPoint(normalArr, normalStyle, 'normalPointLayers');
          this.$refs.map.drawClusterPoint(importArr, importStyle, 'importPointLayers');
        }
      } else {
        this.$refs.map.drawClusterPoint([], {}, 'normalPointLayers');
        this.$refs.map.drawClusterPoint([], {}, 'importPointLayers');
        let style = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: 'pixels',
          src: require('@/assets/images/legend/shuiku-juhe.png')
        };
        let pointList = data.rsvrWarnPoint.map((point) => {
          let imgSrc = require('@/assets/images/legend/shuiku-juhe.png');
          // if (point.overLimitVal != null && point.overLimitVal > 0) {
          //   imgSrc = require("@/assets/images/dianwei-juhe-chaoji.png");
          // }
          return {
            name: point.resName,
            type: '水库',
            lng: point.lowLeftLong,
            lat: point.lowLeftLat,
            code: point.resCode,
            src: imgSrc,
            props: {
              实时水位: `${point.waterLevel} m`,
              汛限水位: `${point.currentLimitLevel} m`,
              更新时间: `${point.dataTime}`,
              鉴定结论: `${point.safetyConclusion}`
            }
          };
        });
        this.$refs.map.drawPoint(pointList, style, 'digReservoirLayers');
      }
    },
    //演练统计卡片
    renderPreviewCard() {
      // const style = { top: this.IsCollapse ? '120px' : '40px' };
      const arr = [
        { component: <PreviewCard onshowPoint={this.showPoints} />, type: '预演' }
        // {
        // 	component: <warn-count class={this.isBigScreen && 'is-big-screen'} />,
        // 	type: '报警'
        // }
      ];
      // {<i v-show={this.warningListShow} class={getCls('left', item.type)} onClick={this.changePreview} />}
      // {<i v-show={this.warningListShow} class={getCls('right', item.type)} onClick={this.changePreview} />}
      return (
        <div class={['center-list', 'preview-module-card']}>
          {arr.map((item) => (
            <div>{item.component}</div>
          ))}
        </div>
      );
    },
    changePreview() {
      // if (this.previewActive == '报警') {
      //   this.previewActive = '预演';
      // } else {
      //   this.previewActive = '报警';
      // }
    },
    showTyphoon(data) {
      this.$nextTick(() => {
        this.$refs.map.showTyphoon(data);
      });
    },
    /**
     * 洪水预报
     */
    FloodForecasting() {
      this.BigReservoirShow = true;
    },
    SelectionTips() {
      this.SelectionTipsShow = true;
    },
    SendMsg() {
      this.SendMsgShow = true;
    },
    closeSendMsgShow() {
      this.SendMsgShow = false;
    },
    closeReservoirShow() {
      this.ReservoirShow = false;
    },
    closeWeatherShow() {
      this.WeatherShow = false;
    },
    closeWaterSourShow() {
      this.waterSourShow = false;
    },
    closeShanXiZLShow() {
      this.shanXiZLShow = false;
    },
    closeShanXiXcShow() {
      this.shanXiXcShow = false;
    },
    closeFourChaosShow() {
      this.fourChaosShow = false;
    },
    closeBeautifulRiverShow() {
      this.beautifulRiverShow = false;
    },
    closeWaterIntakeShow() {
      this.WaterIntakeShow = false;
    },
    closeHydropowerStationShow() {
      this.hydropowerStationShow = false;
    },
    // 山塘落点弹窗
    closeMountainPondPointShow() {
      this.mountainPondPointDialog = false;
    },
    closeTyphoonReservoirShow() {
      this.TyphoonReservoirShow = false;
    },
    closeTyphoonSluiceShow() {
      this.TyphoonSluiceShow = false;
    },
    closeSelectionTipsShow() {
      this.SelectionTipsShow = false;
    },
    closeBigReservoirShow() {
      this.BigReservoirShow = false;
    },
    closeWaterRegulationShow() {
      this.waterRegulationShow = false;
    },
    closeDikePointDialogShow() {
      this.dikePointDialogShow = false;
    },
    closePumpPointDialogShow() {
      this.pumpPointDialogShow = false;
    },
    // 水情弹窗
    closeWaterPointDialogShow() {
      this.waterPointDialogShow = false;
    },
    //  工情弹窗
    closeWorkPointDialogShow() {
      this.workPointDialogShow = false;
    },
    //  极简模式水闸弹窗
    closesimpleSluiceDialogShow() {
      this.simpleSluiceDialogShow = false;
    },

    closeTeamPointDialogShow() {
      this.teamPointDialogShow = false;
    },
    //  报警弹窗
    closePoliceDialogShow() {
      this.callThePoliceDialogShow = false;
    },
    sendValue(val) {
      if (val.length > 0) {
        this.FloodForecastingVal = val;
        this.FloodForecastingShow = true;
      } else {
        this.FloodForecastingShow = false;
      }
    },
    /**
     * 关闭洪水预报弹窗
     */
    closeFloodDialogShow() {
      this.FloodForecastingShow = false;
    },
    // 水雨情弹框
    renderWaterRainDialog() {
      return (
        this.waterRainDialog && (
          <PrimaryDialog
            mainTitle={this.dialogTitle}
            hasAnalysis={false}
            visible={this.waterRainDialog}
            onHandleClose={this.handleWaterRainClose}
            width="1129px"
          >
            <div class="dialog-content">
              <RainDialogContent dialogType={this.dialogType} stationData={this.pointData} ref="rainDialog" />
            </div>
          </PrimaryDialog>
        )
      );
    },
    // 山洪弹框
    renderFloodDialog() {
      return (
        this.floodDetailDialog && (
          <PrimaryDialog
            mainTitle="山洪点详情"
            hasAnalysis={false}
            visible={this.floodDetailDialog}
            onHandleClose={this.handleFloodDetailClose}
            width="920px"
          >
            <div class="dialog-content">
              <FloodDetailContent pointData={this.pointData} />
            </div>
          </PrimaryDialog>
        )
      );
    },
    //洪水预报弹窗
    renderFloodForecastDialog() {
      return (
        this.FloodForecastingShow && (
          <flood-forecasting-dialog
            lookDetailsData={this.FloodForecastingVal}
            visible={this.FloodForecastingShow}
            onCloseDialog={this.closeFloodDialogShow}
          />
        )
      );
    },
    //大中型水库弹窗
    renderBigReservoirDialog() {
      return (
        this.BigReservoirShow && (
          <big-reservoir-dialog
            visible={this.BigReservoirShow}
            onSendValue={this.sendValue}
            onCloseDialog={this.closeBigReservoirShow}
          />
        )
      );
    },
    closeModuleAnalyShow() {
      this.moduleAnalyShow = false;
    },
    //模型分析弹框
    renderModuleAnalyDialog() {
      return (
        this.moduleAnalyShow && (
          <module-analy-dialog
            visible={this.moduleAnalyShow}
            onCloseDialog={this.closeModuleAnalyShow}
          ></module-analy-dialog>
        )
      );
    },
    //选点提示弹窗
    renderSelectionTipsDialog() {
      return (
        this.SelectionTipsShow && (
          <selection-tips-dialog visible={this.SelectionTipsShow} onCloseDialog={this.closeSelectionTipsShow} />
        )
      );
    },
    renderProjectDialog() {
      // let ignoreArr = ['闸站', '农饮水', '灌区'];
      // if (ignoreArr.indexOf(this.pointData.type) != -1) {
      //   this.projectDialogRemoveTabs.push('运行管理');
      // } else {

      // }

      this.projectDialogRemoveTabs = [];
      return (
        this.projectDialogVisible && (
          <PrimaryDialog
            visible={this.projectDialogVisible}
            main-title={this.pointData.name}
            width="1300px"
            onHandleClose={() => this.handleProjectDialogClose()}
          >
            <SeawallCardList seawallData={this.pointData} />
          </PrimaryDialog>
        )
      );
      // return (
      //   this.projectDialogVisible && (
      //     <ProjectDialog
      //       remove-tabs={this.projectDialogRemoveTabs}
      //       visible={this.projectDialogVisible}
      //       point-data={this.pointData}
      
      //       onClose={this.handleProjectDialogClose}
      //       ref="projectDialog"
      //     />
      //   )
      // );
    },
    renderRiskDialog() {
      return (
        <PrimaryDialog
          mainTitle="风险详情"
          hasAnalysis={false}
          visible={this.riskDialog}
          onHandleClose={this.handleRiskClose}
          width="920px"
        >
          <div class="dialog-content">
            <RiskDialogContent ref="riskDialog" pointData={this.pointData} />
          </div>
        </PrimaryDialog>
      );
    },
    renderDisasterSiteDialog() {
      return (
        <PrimaryDialog
          mainTitle="避灾点详情"
          hasAnalysis={false}
          visible={this.disasterDialog}
          onHandleClose={this.handleDisasterClose}
          width="1296px"
        >
          <div class="dialog-content">
            <DisasterSiteDialog ref="isasterDialog" pointData={this.pointData} />
          </div>
        </PrimaryDialog>
      );
    },
    // 调度方案弹框
    renderEmergencySchemeDialog() {
      return (
        this.emergencySchemeShow && (
          <emergency-scheme-dialog
            visible={this.emergencySchemeShow}
            onCloseDialog={this.closeEmergencySchemeShow}
            onDispatchInfo={this.dispatchInfo}
            lat={this.dispatchLat}
            lng={this.dispatchLng}
          />
        )
      );
    },

    //短临预报弹框
    renderForeCastDialog() {
      return (
        this.foreCastShow && (
          <fore-cast-dialog
            visible={this.foreCastShow}
            onCloseDialog={this.closeForecastDialog}
            lat={this.dispatchLat}
            lng={this.dispatchLng}
          />
        )
      );
    },

    // 发送抢险信息弹框
    renderSendMsgDialog() {
      return this.SendMsgShow && <send-msg-dialog visible={this.SendMsgShow} onCloseDialog={this.closeSendMsgShow} />;
    },

    // 水库蓄水量落点弹窗
    renderReservoirDialog() {
      return (
        this.ReservoirShow && (
          <reservoir-dialog
            ref="ReservoirDialog"
            visible={this.ReservoirShow}
            pointData={this.pointData}
            onCloseDialog={this.closeReservoirShow}
          />
        )
      );
    },

    // 气象监测
    renderWeatherDialog() {
      return (
        this.WeatherShow && (
          <weather-dialog ref="WeatherDialog" visible={this.WeatherShow} onCloseDialog={this.closeWeatherShow} />
        )
      );
    },

    // 水源供水
    renderWaterSourDialog() {
      return (
        this.waterSourShow && (
          <waterSourDialog ref="waterSourDialog" visible={this.waterSourShow} onCloseDialog={this.closeWaterSourShow} />
        )
      );
    },

    // 珊溪总览
    renderShanXiZLDialog() {
      return (
        this.shanXiZLShow && (
          <shanXiZLDialog ref="shanXiZLDialog" visible={this.shanXiZLShow} onCloseDialog={this.closeShanXiZLShow} />
        )
      );
    },

    // 珊溪巡查
    renderShanXiXcDialog() {
      return (
        this.shanXiXcShow && (
          <shanXiXcDialog ref="shanXiXcDialog" visible={this.shanXiXcShow} onCloseDialog={this.closeShanXiXcShow} />
        )
      );
    },
    renderWaterRegulationDialog() {
      return (
        this.waterRegulationShow && (
          <waterRegulationDialog
            ref="waterRegulationDialog"
            visible={this.waterRegulationShow}
            onCloseDialog={this.closeWaterRegulationShow}
          />
        )
      );
    },
    renderDikePointDialog() {
      return (
        this.dikePointDialogShow && (
          <dikePointDialog
            ref="dikePointDialog"
            visible={this.dikePointDialogShow}
            onCloseDialog={this.closeDikePointDialogShow}
          />
        )
      );
    },
    renderPumpPointDialog() {
      return (
        this.pumpPointDialogShow && (
          <pumpPointDialog
            ref="pumpPointDialog"
            visible={this.pumpPointDialogShow}
            onCloseDialog={this.closePumpPointDialogShow}
          />
        )
      );
    },

    // 清四乱
    renderFourChaosDialog() {
      return (
        this.fourChaosShow && (
          <FourChaosDialog ref="FourChaosDialog" visible={this.fourChaosShow} onCloseDialog={this.closeFourChaosShow} />
        )
      );
    },

    // 美丽河湖
    renderBeautifulRiverDialog() {
      return (
        this.beautifulRiverShow && (
          <beautifulRiverDialog
            ref="beautifulRiverDialog"
            visible={this.beautifulRiverShow}
            onCloseDialog={this.closeBeautifulRiverShow}
          />
        )
      );
    },

    // 取水户
    renderWaterIntakeDialog() {
      return (
        this.WaterIntakeShow && (
          <WaterIntakeDialog
            ref="WaterIntakeDialog"
            visible={this.WaterIntakeShow}
            onCloseDialog={this.closeWaterIntakeShow}
          />
        )
      );
    },

    // 水电站
    renderHydropowerStationDialog() {
      return (
        this.hydropowerStationShow && (
          <HydropowerStationDialog
            ref="HydropowerStationDialog"
            visible={this.hydropowerStationShow}
            pointData={this.pointData}
            onCloseDialog={this.closeHydropowerStationShow}
          />
        )
      );
    },
    // 山塘
    renderMountainPondPointDialog() {
      return (
        this.mountainPondPointDialog && (
          <MountainPondPointDialog
            ref="mountainPondPointDialog"
            visible={this.mountainPondPointDialog}
            point-data={this.pointData}
            onCloseDialog={this.closeMountainPondPointShow}
          />
        )
      );
    },
    // 台风 水库 落点弹窗
    renderTyphoonReservoirDialog() {
      return (
        this.TyphoonReservoirShow && (
          <typhoon-reservoir-dialog
            ref="TyphoonReservoirShow"
            visible={this.TyphoonReservoirShow}
            pointData={this.pointData}
            onCloseDialog={this.closeTyphoonReservoirShow}
          />
        )
      );
    },
    // 台风 水闸 落点弹窗
    renderTyphoonSluiceDialog() {
      return (
        this.TyphoonSluiceShow && (
          <typhoon-sluice-dialog
            ref="TyphoonSluiceShow"
            visible={this.TyphoonSluiceShow}
            pointData={this.pointData}
            onCloseDialog={this.closeTyphoonSluiceShow}
          />
        )
      );
    },
    // 水情
    renderWaterPointDialog() {
      return (
        this.waterPointDialogShow && (
          <WorkWaterDialog
            ref="waterPointDialog"
            visible={this.waterPointDialogShow}
            title={'水情'}
            pointData={this.pointData}
            onCloseDialog={this.closeWaterPointDialogShow}
          />
        )
      );
    },
    // 极简模式水闸
    renderSimpleSluiceDialog() {
      return (
        this.simpleSluiceDialogShow && (
          <SimpleSluiceDialog
            ref="simpleSluiceDialog"
            visible={this.simpleSluiceDialogShow}
            title={'极简模式'}
            pointData={this.pointData}
            onCloseDialog={this.closesimpleSluiceDialogShow}
          />
        )
      );
    },
    // 工情
    renderWorkPointDialog() {
      return (
        this.workPointDialogShow && (
          <WorkWaterDialog
            ref="workPointDialog"
            visible={this.workPointDialogShow}
            title={'工情'}
            pointData={this.pointData}
            onCloseDialog={this.closeWorkPointDialogShow}
          />
        )
      );
    },

    // 报警
    renderPoliceDialog() {
      return (
        this.callThePoliceDialogShow && (
          <WorkWaterDialog
            ref="policeDialog"
            visible={this.callThePoliceDialogShow}
            title={'报警'}
            onCloseDialog={this.closePoliceDialogShow}
          />
        )
      );
    },

    showScheduleScheme(lat, lng) {
      this.dispatchLat = lat;
      this.dispatchLng = lng;
      this.emergencySchemeShow = true;
    },
    closeEmergencySchemeShow() {
      this.emergencySchemeShow = false;
    },
    //短临预报点位点击弹框
    showForecastDialog(lat, lng) {
      // console.log(lat, lng, 111);
      this.dispatchLat = lat;
      this.dispatchLng = lng;
      this.foreCastShow = true;
    },
    closeForecastDialog() {
      this.foreCastShow = false;
    },
    //获取调度信息
    dispatchInfo(data, stationInfo) {
      this.$refs.map.dispatchInfo(data, stationInfo);
      this.SendMsg();
    },
    /**
     * 图层点击事件
     */
    showDialog(val) {
      console.log(val, '图层点击事件');
      if (val.pointType) {
        return false;
      }
      // if (this.Simple && val.type == '海塘' && !val.leftClick) {
      //   this.setSimpleRightType(true);
      //   this.setSimpleData(val);
      //   return false;
      // }
      if (val.type == '河道') {
        this.$store.commit('riverwayData', val);
        this.SetRiverway(true);
      }
      this.cardInitFlag = false;
      let dialogList = [
        '水情',
        '工情',
        '极简模式',
        '报警',
        '救援仓库',
        '风暴潮预警',
        '避灾点',
        '视频',
        '救援队伍',
        '附属工程',
        '水库',
        '水闸',
        '山塘',
        '堤防',
        '海塘',
        '泵站',
        '闸站',
        '灌区',
        '农饮水',
        '水电站',
        '雨量',
        '在建工程',
        '复盘雨量',
        '河网',
        '潮位',
        '风险隐患',
        '堤防保险',
        '山洪',
        '山洪预警',
        '断面',
        '气象监测',
        '水厂',
        '水厂运行状态',
        '水源供水',
        '珊溪总览',
        '珊溪巡查',
        '清四乱',
        '美丽河湖',
        '水源保障',
        '取水监管',
        '取水户',
        '水电站',
        '河长巡查',
        '水质监控',
        '抗旱监管',
        '防汛风险',
        '洪水风险图'
      ];
      if (!val.type || !dialogList.includes(val.type)) return;
      this.pointData = val;
      if (val.type == '水库') {
        this.pointData.start = this.formatterStartTime('yyyy-MM-DD hh:mm:ss');
        this.pointData.end = this.formatterEndTime('yyyy-MM-DD hh:mm:ss');
      }
      const methodsMap = {
        雨量: () => {
          this.dialogType = 'PP';
          this.dialogTitle = val.name;
          this.$nextTick(() => {
            if (this.previewModule) {
              this.$refs.rainDialog.getPreviewRainStationInfo();
            } else {
              this.$refs.rainDialog.getRainStationInfo();
            }
          });
          this.waterRainDialog = true;
        },
        河网: () => {
          this.dialogType = 'ZZ';
          this.dialogTitle = val.name;
          this.$nextTick(() => {
            this.$refs.rainDialog.getWaterStationInfo();
          });
          // this.moduleAnalyShow = true;
          this.waterRainDialog = true;
        },
        潮位: () => {
          this.dialogType = 'TT';
          this.dialogTitle = val.name;
          this.$nextTick(() => {
            this.$refs.rainDialog.getWaterStationInfo();
          });
          this.waterRainDialog = true;
        },
        断面: () => {
          this.dialogType = '断面';
          this.dialogTitle = val.name;
          this.$nextTick(() => {
            this.$refs.rainDialog.sectFlowCurve(val);
          });
          this.waterRainDialog = true;
        },
        风暴潮预警: () => {
          this.dialogType = 'disaster';
          this.dialogTitle = val.name;
          this.$nextTick(() => {
            this.$refs.rainDialog.sectFlowCurve(val);
          });
          this.waterRainDialog = true;
        },
        风险隐患: () => {
          this.riskDialog = true;
          this.$nextTick(() => {
            this.$refs.riskDialog.init();
          });
        },
        山洪: () => {
          this.floodDetailDialog = true;
        },
        山洪预警: () => {
          this.floodDetailDialog = true;
        },
        // 在建工程: () => {
        //   this.projectDialogVisible = true;
        //   this.$nextTick(() => {
        //     this.$refs.projectDialog.getProjectInfo();
        //     this.$refs.projectDialog.getContactList();
        //   });
        // },
        水库: () => {
          if (this.previewModule) {
            this.dialogType = 'RR';
            this.dialogTitle = val.name;
            this.$nextTick(() => {
              this.$refs.rainDialog.getPreWaterStationInfo();
            });
            this.waterRainDialog = true;
          } else {
            // this.projectDialogVisible = true;
            // this.$nextTick(() => {
            //   this.$refs.projectDialog.getProjectInfo();
            //   this.$refs.projectDialog.getContactList();
            // });
            this.TyphoonReservoirShow = true;
            this.$nextTick(() => {
              this.$refs.TyphoonReservoirShow.Init();
              // this.$refs.ReservoirDialog.getContactList();
            });
          }
        },
        水闸: () => {
          this.TyphoonSluiceShow = true;
          this.$nextTick(() => {
            this.$refs.TyphoonSluiceShow.Init();
            // this.$refs.ReservoirDialog.getProjectInfo();
            // this.$refs.ReservoirDialog.getContactList();
          });
        },
        救援仓库: () => {
          this.dialogType = 'disaster';
          this.dialogTitle = val.name;
          this.waterRainDialog = true;
          this.$nextTick(() => {
            this.$refs.rainDialog.getLogStaitonInfo();
          });
        },
        水质监控: () => {
          this.dialogType = val.type;
          this.dialogTitle = val.name;
          this.waterRainDialog = true;
          this.$nextTick(() => {
            this.$refs.rainDialog.qualityCurve(val.siteNo);
          });
        },
        水情: () => {
          this.dialogType = 'waterqing';
          this.dialogTitle = val.name;
          this.waterPointDialogShow = true;
          this.$nextTick(() => {
            this.$refs.waterPointDialog.getRainStationInfo();
          });
        },
        工情: () => {
          this.dialogType = 'FP';
          this.dialogTitle = val.name;
          this.workPointDialogShow = true;
          this.$nextTick(() => {
            this.$refs.workPointDialog.getRainStationInfo();
          });
        },
        极简模式: () => {
          this.dialogType = 'FP';
          this.dialogTitle = val.name;
          this.simpleSluiceDialogShow = true;
          // this.$nextTick(() => {
          //   this.$refs.simpleSluiceDialog.getContactList();
          // });
        },
        // 救援仓库: () => {
        //   this.dialogType = 'CK';
        //   this.dialogTitle = val.name;
        //   this.waterRainDialog = true;
        //   this.$nextTick(() => {
        //     this.$refs.pumpPointDialog.getDikeInfo();
        //   });
        // },
        报警: () => {
          this.dialogType = 'BJ';
          this.dialogTitle = val.name;
          this.callThePoliceDialogShow = true;
          this.$nextTick(() => {
            this.$refs.policeDialog.getRainStationInfo();
          });
        },
        避灾点: () => {
          this.dialogType = 'disaster';
          this.dialogTitle = val.name;
          this.waterRainDialog = true;
          this.$nextTick(() => {
            this.$refs.rainDialog.getFlowDetail();
          });
        },
        视频: () => {
          this.dialogType = 'camera';
          this.dialogTitle = val.name;
          this.waterRainDialog = true;
          this.$nextTick(() => {
            this.$refs.rainDialog.getCameraStationInfo();
          });
        },
        救援队伍: () => {
          this.ReservoirShow = true;
          let opt = {
            name: val.name,
            resCode: val.code
          };
        },
        附属工程: () => {
          this.ReservoirShow = true;
        },
        气象监测: () => {
          this.WeatherShow = true;
          let opt = {
            name: val.name,
            stationCode: val.stationCode
          };
          this.$nextTick(() => {
            this.$refs.WeatherDialog.init(opt);
          });
        },
        水源供水: () => {
          this.waterSourShow = true;
          this.$nextTick(() => {
            this.$refs.waterSourDialog.init(val);
          });
        },
        水厂: () => {
          this.waterSourShow = true;
          this.$nextTick(() => {
            this.$refs.waterSourDialog.init(val);
          });
        },
        水厂运行状态: () => {
          this.waterSourShow = true;
          this.$nextTick(() => {
            this.$refs.waterSourDialog.init(val);
          });
        },
        珊溪总览: () => {
          this.shanXiZLShow = true;
          this.$nextTick(() => {
            this.$refs.shanXiZLDialog.init(val);
          });
        },
        珊溪巡查: () => {
          this.shanXiXcShow = true;
          this.$nextTick(() => {
            this.$refs.shanXiXcDialog.init(val);
          });
        },
        清四乱: () => {
          this.fourChaosShow = true;
          this.$nextTick(() => {
            this.$refs.FourChaosDialog.init(val);
          });
        },
        美丽河湖: () => {
          this.beautifulRiverShow = true;
          this.$nextTick(() => {
            this.$refs.beautifulRiverDialog.init(val);
          });
        },
        水源保障: () => {
          this.waterSourShow = true;
          this.$nextTick(() => {
            this.$refs.waterSourDialog.init(val);
          });
        },
        取水监管: () => {
          this.waterRegulationShow = true;
          this.$nextTick(() => {
            this.$refs.waterRegulationDialog.init(val);
          });
        },
        堤防: () => {
          this.dikePointDialogShow = true;
          let opt = {
            name: val.name,
            resCode: val.code
          };
          this.$nextTick(() => {
            this.$refs.dikePointDialog.init(opt);
          });
        },
        泵站: () => {
          this.pumpPointDialogShow = true;
          let opt = {
            name: val.name,
            resCode: val.code
          };
          this.$nextTick(() => {
            this.$refs.pumpPointDialog.init(opt);
          });
        },
        取水户: () => {
          this.WaterIntakeShow = true;
          this.$nextTick(() => {
            this.$refs.WaterIntakeDialog.init(val);
          });
        },
        水电站: () => {
          this.hydropowerStationShow = true;
          this.$nextTick(() => {
            this.$refs.HydropowerStationDialog.init(val);
          });
        },
        山塘: () => {
          this.mountainPondPointDialog = true;
        },
        河长巡查: () => {
          this.beautifulRiverShow = true;
          this.$nextTick(() => {
            this.$refs.beautifulRiverDialog.init(val);
          });
        },
        抗旱监管: () => {
          if (val.projectType !== 'sluice' && val.projectType !== 'reservoir') {
            this.WaterIntakeShow = true;
          } else {
            this.sluiceDialogForSchedule = true;
          }
        },
        防汛风险: () => {
          this.sluiceDialogForSchedule = true;
        }
      };
      if (Object.keys(methodsMap).includes(val.type)) {
        methodsMap[val.type]();
      } else {
        this.projectDialogVisible = true;
        // this.$nextTick(() => {
        //   this.$refs.projectDialog.getProjectInfo();
        //   this.$refs.projectDialog.getContactList();
        // });
      }
    },
    handleLegendChange(name, legend = {}) {
      if (Object.keys(legend).length == 0) delete this.legends[name];
      else this.legends[name] = legend;
      this.showLegend = Object.keys(legend).length > 0 || Object.keys(this.legends).length > 0;
      this.$forceUpdate();
    },
    closeWaterIntakeShow() {
      this.WaterIntakeShow = false;
    },
    closeSluiceDialogForSchedule() {
      this.sluiceDialogForSchedule = false;
    },
    //关闭水雨情弹窗
    handleWaterRainClose() {
      this.waterRainDialog = false;
      this.pointData = {};
    },
    //关闭山洪弹窗
    handleFloodDetailClose() {
      this.floodDetailDialog = false;
      this.pointData = {};
    },
    handleProjectDialogClose() {
      this.projectDialogVisible = false;
      this.pointData = {};
    },
    // 关闭风险弹框
    handleRiskClose() {
      this.riskDialog = false;
      this.pointData = {};
    },
    // 关闭避灾点
    handleDisasterClose() {
      this.disasterDialog = false;
      this.pointData = {};
    },
    renderWaterIntakeDialog() {
      return (
        <PrimaryDialog
          visible={this.WaterIntakeShow}
          main-title={this.pointData.name}
          width="1200px"
          top="12vh"
          onHandleClose={() => this.closeWaterIntakeShow()}
        >
          <WaterIntakeDialog ref="WaterIntakeDialog" pointData={this.pointData} />
        </PrimaryDialog>

        // this.WaterIntakeShow && (
        //   <WaterIntakeDialog
        //     ref="WaterIntakeDialog"
        //     visible={this.WaterIntakeShow}
        //     onCloseDialog={this.closeWaterIntakeShow}
        //   />
        // )
      );
    },
    /**
     * 渲染配置过的统计卡片组件
     * @see factoryStatistics <./utils.js> 内
     */
    renderFactoryStat() {
      const h = this.$createElement;
      /**
       * 临时C位
       * @desc 这个变量里增加 开发环境会把对应的配置 统计卡片渲染
       * @type {string[]}
       */
      const tempCheckList = [];
      const renderList =
        process.env.NODE_ENV === 'development' ? this.CheckedNoBxArr.concat(tempCheckList) : this.CheckedNoBxArr;
      return renderList.map((item) => {
        let component = this.statMap[item];
        let props = {
          class: [this.isBigScreen && 'is-big-screen', 'center-item'],
          on: { 'legend-change': this.handleLegendChange }
        };
        return {
          type: item,
          component: factoryStatistics(h, { component, props })
        };
      });
    },
    /**
     * 渲染配置过的卡片组件  其配置=>this.cardMap
     * @desc 核心函数factoryCardComponent在 <./utils.js> 内
     */
    renderFactoryCard() {
      const h = this.$createElement;
      console.log(this.CheckedArr);
      return this.CheckedArr.map((item) => {
        // if (item == '报警') return;
        console.log(this.cardMap[item]);
        let { component, props = {}, cardProps = {} } = this.cardMap[item];
        const param = {
          component,
          componentProp: props,
          cardProp: {
            style: this.getCardPos(item),
            props: { cardName: item, ...cardProps },
            class: ['drag-box', this.isBigScreen && 'is-big-screen'],
            key: props.key || component.componentOptions.tag
          }
        };
        return factoryCardComponent(h, param);
      });
    },
    /**
     * 获取卡片位置
     */
    getCardPos(cardName) {
      return this.cardStyle.has(cardName) ? this.cardStyle.get(cardName) : this.cardPos['leftpos1'];
    },
    /**
     * 获取页面中卡片组件信息
     */
    getCardInfo() {
      let res = [];
      let ignoreArr = [];
      this.CheckedArr.map((item) => {
        if (ignoreArr.indexOf(item) == -1) {
          let { component } = this.cardMap[item];
          let vueInstance = component.context;
          const componentTag = component.componentOptions.tag;
          let styleObj = getChildComponent(vueInstance, componentTag).$parent.$el.style;
          let unitSceneProperty = JSON.stringify({
            left: styleObj.left,
            top: styleObj.top,
            right: styleObj.right,
            bottom: styleObj.bottom
          });
          res.push({
            unitId: item,
            unitSceneProperty
          });
        }
      });
      return res;
    },

    /**
     * 设置统计卡片
     */
    renderStatCard() {
      const countsArr = this.renderFactoryStat();
      // const style = { top: this.IsCollapse ? '120px' : '40px' };
      const getCls = (pos, name) => [this.isBigScreen && 'big-screen', name == `icon-${pos}-count`];
      return (
        this.StatisticFlag && (
          <div class="center-list">
            {countsArr.map((item) => (
              <div v-show={this.ActiveCount == item.type}>
                {this.ActiveCount == item.type && <i class="icon-left-count" onClick={this.countsPrev} />}
                {item.component}
                {this.ActiveCount == item.type && <i class="icon-right-count" onClick={this.countsNext} />}
              </div>
            ))}
          </div>
        )
      );
    },

    renderStatCardTest() {
      const countsArr = this.renderFactoryStat();
      const style = { top: this.IsCollapse ? '120px' : '40px' };
      return (
        <div class="center-list" style={style}>
          {countsArr.map((item) => (
            <div v-show={this.ActiveCount == item.type}>
              <RiverPatrolCount />
            </div>
          ))}
        </div>
      );
    },
    countsPrev() {
      let index = this.CheckedNoBxArr.findIndex((item) => item == this.ActiveCount);
      const targetIndex = index == 0 ? this.CheckedNoBxArr.length - 1 : index - 1;
      this.SetActiveCount(this.CheckedNoBxArr[targetIndex]);
    },
    countsNext() {
      // 若要切换count时消除落点，需要把图层置空
      // this.$refs.map.drawClusterPoint([], {}, 'digReservoir1Layer');
      let index = this.CheckedNoBxArr.findIndex((item) => item == this.ActiveCount);
      const targetIndex = index == this.CheckedNoBxArr.length - 1 ? 0 : index + 1;
      this.SetActiveCount(this.CheckedNoBxArr[targetIndex]);
    },
    // 水政务协同-出差管理
    renderGovernmentDialog() {
      return (
        this.governmentDialogShow && (
          <government-dialog
            ref="governmentDialog"
            code={this.governmentDialogCode}
            visible={this.governmentDialogShow}
            onCloseDialog={this.closeGovernmentDialogShow}
          />
        )
      );
    },
    openGovernmentDialogShow(code) {
      this.governmentDialogShow = true;
      this.governmentDialogCode = code;
      this.$nextTick(() => {
        this.$refs.governmentDialog.init();
      });
    },
    closeGovernmentDialogShow() {
      this.governmentDialogShow = false;
    },
    //模仿堤防
    renderDikeDialog() {
      return (
        this.dikeDialogShow && (
          <dike-dialog
            ref="dikeDialog"
            code={this.dikeDialogCode}
            visible={this.dikeDialogShow}
            onCloseDialog={this.closeDikeDialogShow}
          />
        )
      );
    },
    openDikeDialogShow(code) {
      this.dikeDialogShow = true;
      this.dikeDialogCode = code;

      this.$nextTick(() => {
        this.$refs.dikeDialog.init();
        // console.log("看看");
      });
    },
    closeDikeDialogShow() {
      this.dikeDialogShow = false;
    },
    //模仿泵站
    renderPumpStationDialog() {
      return (
        this.pumpStationDialogShow && (
          <pump-station-dialog
            ref="pumpStationDialog"
            code={this.pumpStationDialogCode}
            visible={this.pumpStationDialogShow}
            onCloseDialog={this.closePumpStationDialogShow}
          />
        )
      );
    },
    openPumpStationDialogShow(code) {
      this.pumpStationDialogShow = true;
      this.pumpStationDialogCode = code;
      this.$nextTick(() => {
        this.$refs.pumpStationDialog.init();
      });
    },
    closePumpStationDialogShow() {
      this.pumpStationDialogShow = false;
    },
    //模仿山塘
    renderMountainPondDialog() {
      return (
        this.MountainPondDialogShow && (
          <mountain-pond-dialog
            ref="MountainPondDialog"
            code={this.MountainPondDialogCode}
            visible={this.MountainPondDialogShow}
            point-data={this.pointData}
            onCloseDialog={this.closeMountainPondDialogShow}
          />
        )
      );
    },
    openMountainPondDialogShow(code) {
      this.MountainPondDialogShow = true;
      this.MountainPondDialogCode = code;
      this.$nextTick(() => {
        this.$refs.MountainPondDialog.init();
      });
    },
    closeMountainPondDialogShow() {
      this.MountainPondDialogShow = false;
    },
    showRainInfo(val) {
      const ValMap = [72, 24, 6, 3, 1, 0, 1, 3, 6];
      const mapArray = [
        'rainfall_72_data',
        'rainfall_24_data',
        'rainfall_6_data',
        'rainfall_3_data',
        'rainfall_1_data',
        'rainfall_newsest_1_data'
      ];
      if (val < 6) {
        //实时降雨量
        this.$refs.map.removeLayerByName('rainfall');
        this.$refs.map.clearTextLayer();
        //加载实时数据
        const rainList = this.realtimePreList[mapArray[val]];
        this.$refs.map.loadRainfallPoint(rainList, true);
        //加载图例
        this.handleLegendChange('预报降雨量', {});
        this.handleLegendChange('实时降雨量', {
          label: '实时降雨量',
          children: [
            { label: '>250', icon: 'realtime-250' },
            { label: '100~250', icon: 'realtime-100' },
            { label: '50~100', icon: 'realtime-50' },
            { label: '25~50', icon: 'realtime-25' },
            { label: '10~25', icon: 'realtime-10' },
            { label: '0.1~10', icon: 'realtime-0_1' }
          ]
        });
      } else {
        //预报降雨量
        //清除实时点位
        this.$refs.map.loadRainfallPoint([], false);
        //加载预报数据
        let data = this.forecastPreList[`rainfall_${ValMap[val]}_data`];
        if (data) {
          this.$refs.map.showPredictRain(data, { hour: ValMap[val] });
          this.handleLegendChange('实时降雨量', {});
          this.handleLegendChange('预报降雨量', {
            label: '预报降雨量',
            children: [
              {
                label: '>70',
                icon: 'forecast-100'
              },
              { label: '50~70', icon: 'forecast-70' },
              { label: '20~50', icon: 'forecast-50' },
              { label: '10~20', icon: 'forecast-25' },
              { label: '3~10', icon: 'forecast-10' },
              { label: '1~3', icon: 'forecast-1' },
              { label: '<1', icon: 'forecast-0' }
            ]
          });
        }
      }
    },
    //获取实时降雨数据
    getRealRainfallData() {
      disasterPreventionApi.realtimeRainfallByAreaName({}).then((res) => {
        if (res.code == 0) {
          this.realtimePreList = res.data;
        }
      });
    },
    //获取预报降雨数据
    getPreRainData() {
      disasterPreventionApi.predictRainfallByAreaName({}).then((res) => {
        if (res.code == 0) {
          this.forecastPreList = res.data;
        }
      });
    }
  },
  render() {
    const { renderMap, renderLegend, renderFactoryCard, renderPreviewCard, renderStatCard } = this;
    const TrendAnalysisDialog = this.renderTrendAnalysisDialog();
    const riverwayDialog = this.renderRiverwayDialog();
    const WaterStudyDetails = this.renderWaterStudyDetails();
    const detailDialog = this.renderDetailDialog();
    const waterRainDialog = this.renderWaterRainDialog();
    const floodDialog = this.renderFloodDialog();
    const projectDialog = this.renderProjectDialog();
    const riskDialog = this.renderRiskDialog();
    const FloodForecastDialog = this.renderFloodForecastDialog();
    const BigReservoirDialog = this.renderBigReservoirDialog();
    const ModuleAnalyDialog = this.renderModuleAnalyDialog();
    const addDialog = this.renderAddDialog();
    const saveAsDialog = this.renderSaveAsDialog();
    const emergencySchemeDialog = this.renderEmergencySchemeDialog();
    const forecastDialog = this.renderForeCastDialog();
    const SelectionTipsDialog = this.renderSelectionTipsDialog();
    const SendMsgDialog = this.renderSendMsgDialog(0);
    const ReservoirDialog = this.renderReservoirDialog();
    const WeatherDialog = this.renderWeatherDialog();
    const waterSourDialog = this.renderWaterSourDialog();
    const shanXiZLDialog = this.renderShanXiZLDialog();
    const shanXiXcDialog = this.renderShanXiXcDialog();
    const FourChaosDialog = this.renderFourChaosDialog();
    const beautifulRiverDialog = this.renderBeautifulRiverDialog();
    const WaterIntakeDialog = this.renderWaterIntakeDialog();
    const SluiceDialogForSchedule = this.renderSluiceDialogForSchedule(); // 防汛风险
    const HydropowerStationDialog = this.renderHydropowerStationDialog();
    const MountainPondPointDialog = this.renderMountainPondPointDialog();
    const TyphoonReservoirDialog = this.renderTyphoonReservoirDialog();
    const TyphoonSluiceDialog = this.renderTyphoonSluiceDialog();
    const waterRegulationDialog = this.renderWaterRegulationDialog();
    const governmentDialog = this.renderGovernmentDialog();
    const dikeDialog = this.renderDikeDialog();
    const dikePointDialog = this.renderDikePointDialog();
    const pumpPointDialog = this.renderPumpPointDialog();
    const pumpStationDialog = this.renderPumpStationDialog();
    const MountainPondDialog = this.renderMountainPondDialog();
    const DisasterSiteDialog = this.renderDisasterSiteDialog();
    const PoliceDialog = this.renderPoliceDialog();
    const WaterPointDialog = this.renderWaterPointDialog();
    const WorkPointDialog = this.renderWorkPointDialog();
    return (
      <div class="disaster-prevention" ref="bigScreen">
        {[
          renderMap(),
          // renderWarningWidget(),
          renderLegend(),
          renderFactoryCard()
        ]}
        {this.RangeBar && (
          <PlayableTimelineBar
            ref="PlayableTimelineBar"
            onChange={this.showRainInfo}
            delay={30000}
            timelineList={this.TimelineList}
          ></PlayableTimelineBar>
        )}

        {this.previewModule && <SilderTimelineBar auto-play ref="SilderTimelineBar" />}
        {this.seawallRangModule && <SeawallRangDialog auto-play ref="SeawallRangDialog" />}
        {this.previewModule || this.seawallRangModule ? renderPreviewCard() : renderStatCard()}
        {addDialog}
        {saveAsDialog}
        {detailDialog}
        {waterRainDialog}
        {floodDialog}
        {projectDialog}
        {riskDialog}
        {FloodForecastDialog}
        {BigReservoirDialog}
        {emergencySchemeDialog}
        {forecastDialog}
        {SelectionTipsDialog}
        {SendMsgDialog}
        {ModuleAnalyDialog}
        {ReservoirDialog}
        {riverwayDialog}
        {WeatherDialog}
        {TrendAnalysisDialog}
        {WaterStudyDetails}
        {waterSourDialog}
        {shanXiZLDialog}
        {shanXiXcDialog}
        {dikeDialog}
        {dikePointDialog}
        {pumpPointDialog}
        {pumpStationDialog}
        {MountainPondDialog}
        {waterRegulationDialog}
        {governmentDialog}
        {FourChaosDialog}
        {beautifulRiverDialog}
        {WaterIntakeDialog}
        {SluiceDialogForSchedule}
        {HydropowerStationDialog}
        {DisasterSiteDialog}
        {PoliceDialog}
        {WaterPointDialog}
        {WorkPointDialog}
        {MountainPondPointDialog}
        {TyphoonReservoirDialog}
        {TyphoonSluiceDialog}
      </div>
    );
    // if (!this.Simple) {

    // } else {
    //   return (
    //     <div
    //       v-loading={this.ComponentLoading}
    //       class="disaster-prevention"
    //       element-loading-background="rgba(0, 0, 0, 0.8)"
    //       ref="bigScreen"
    //     >
    //       {[
    //         renderMap()
    //         // renderWarningWidget(),
    //         // renderLegend(),
    //         // renderFactoryCard()
    //       ]}

    //       {this.sluiceDistributionMap ? '' : <SimpleCount />}
    //       {this.sluiceDistributionMap ? '' : <PotentialRisks />}
    //       {this.simpleRightType ? <PotentialRiskList /> : ''}
    //       {<RenderTabControl onSelectTips={() => { }}></RenderTabControl>}
    //       {this.RangeBar && (
    //         <PlayableTimelineBar
    //           ref="PlayableTimelineBar"
    //           onChange={this.showRainInfo}
    //           delay={30000}
    //           timelineList={this.TimelineList}
    //         ></PlayableTimelineBar>
    //       )}
    //       {this.simpleTimes ? <simpleRangDialog ref="SeawallRangDialog" /> : ''}
    //       {/* process.env.NODE_ENV == "development" && (
    // 			<DragCard
    // 				v-drag
    // 				class="drag-box"
    // 				style="top: 8.16937%;left: 21.4193%;"
    // 				key="stagnant-water-card"
    // 				cardName="asdad"
    // 				isHigh
    // 			>
    // 				<DataGatherCard />
    // 			</DragCard>
    // 		) */}
    //       {addDialog}
    //       {saveAsDialog}
    //       {detailDialog}
    //       {waterRainDialog}
    //       {floodDialog}
    //       {projectDialog}
    //       {riskDialog}
    //       {FloodForecastDialog}
    //       {BigReservoirDialog}
    //       {emergencySchemeDialog}
    //       {forecastDialog}
    //       {SelectionTipsDialog}
    //       {SendMsgDialog}
    //       {ModuleAnalyDialog}
    //       {ReservoirDialog}
    //       {riverwayDialog}
    //       {WeatherDialog}
    //       {TrendAnalysisDialog}
    //       {WaterStudyDetails}
    //       {waterSourDialog}
    //       {shanXiZLDialog}
    //       {shanXiXcDialog}
    //       {dikeDialog}
    //       {dikePointDialog}
    //       {pumpPointDialog}
    //       {pumpStationDialog}
    //       {MountainPondDialog}
    //       {waterRegulationDialog}
    //       {governmentDialog}
    //       {FourChaosDialog}
    //       {beautifulRiverDialog}
    //       {WaterIntakeDialog}
    //       {HydropowerStationDialog}
    //       {DisasterSiteDialog}
    //       {PoliceDialog}
    //       {WaterPointDialog}
    //       {WorkPointDialog}
    //       {SimpleSluiceDialog}
    //       {MountainPondPointDialog}
    //       {TyphoonReservoirDialog}
    //       {TyphoonSluiceDialog}
    //     </div>
    //   );
    // }
  }
};
</script>

<style lang="scss" scoped>
@import '@/style/bigScreen.scss';
@import '@/style/sidebarStyle.scss';
.disaster-prevention {
  width: 100%;
  height: 100%;
  background-image: url(~assets/new_images/kuang.png);
  background-size: 100% 100%;
  background-repeat: no-repeat;
  box-sizing: border-box;
  padding: 2px;
  .func-box {
    position: absolute;
    left: 22%;
    bottom: 6%;
    z-index: 11;
    .func-item {
      width: 148px;
      height: 48px;
      background: url(~assets/images/custom-button-bg.png) no-repeat center center;
      background-size: 100% 100%;
      color: #fff;
      cursor: pointer;
      margin-bottom: 16px;
      @include flexbox;
      @include flexflow(row, nowrap);
      @include flexJC(center);
      @include flexAC;
      i {
        display: inline-block;
        margin-right: 6px;
      }
    }
  }
  .drag-box {
    position: absolute;
    &.is-big-screen {
      transform: translateX(-25%) scaleX(0.5);
    }
    &.default-pos {
      top: 80px;
      left: 0px;
    }
    &.mountain-warn {
      top: 480px;
      left: 0px;
    }
  }
  .center-list {
    position: absolute;
    top: 144px;
    left: calc((100% - 1000px) / 2);
    width: 1000px;
    overflow: hidden;
    display: flex;
    align-items: center;
    z-index: 11;
    justify-content: center;
    &:hover {
      > div {
        i.icon-left-count,
        i.icon-right-count,
        i.icon-left-count-red,
        i.icon-right-count-red {
          visibility: visible;
        }
      }
    }
    > div {
      position: relative;
      i {
        position: absolute;
        &.icon-left-count {
          left: -70px;
          top: 50%;
          transform: translateY(-50%);
          visibility: hidden;
          &.big-screen {
            left: 128px;
          }
        }
        &.icon-left-count-red {
          left: -50px;
          top: 50%;
          transform: translateY(-50%);
          visibility: hidden;
          &.big-screen {
            left: 128px;
          }
        }
        &.icon-right-count {
          right: -70px;
          top: 50%;
          transform: translateY(-50%);
          visibility: hidden;
          &.big-screen {
            right: 128px;
          }
        }
        &.icon-right-count-red {
          right: -50px;
          top: 50%;
          transform: translateY(-50%);
          visibility: hidden;
          &.big-screen {
            right: 128px;
          }
        }
      }
    }
    .icon-left-count,
    .icon-right-count {
      cursor: pointer;
    }
  }
}
.map-legend {
  bottom: 5%;
  width: 280px;
  &.is-big-screen {
    transform: scaleX(0.5) translate(50%);
  }
  .legend-panel {
    max-height: 268px;
    overflow-y: auto;
    .legend-item-box {
      // align-items: center;
      padding-bottom: 15px;
      margin-bottom: 15px;
      border-bottom: 1px solid #687586;
      &:last-child {
        border-bottom: none;
        margin-bottom: 0;
      }
      .legend-name {
        // padding: 10px 20px;
        margin-right: 0;
        color: #1df8f3;
        > i {
          margin-left: 8px;
          vertical-align: middle;
          cursor: pointer;
          padding-top: 1px;
        }
        > p:nth-child(1) {
          font-family: PingFangSC-Semibold;
          font-size: 16px;
          color: #00c1ff;
          letter-spacing: 0;
          line-height: 32px;
          font-weight: 600;
          opacity: 1;
        }
        > p {
          padding: 5px 0;
          opacity: 0.6;
          color: #a6dff6;
          > i {
            margin-right: 10px;
          }
        }
      }
      .legend-list {
        // border-left: 1px solid #00c1ff;
        padding-left: 16px;
        // flex-direction: column;
        span {
          width: 100%;
        }
        .legend-item {
          display: inline-block;
          min-width: 50%;
          .icon-sluice,
          .icon-sluice-warn {
            width: 24px;
            height: 24px;
          }
        }
      }
      .item-color-block {
        width: 25px;
        height: 25px;
      }
    }
  }
}
// 去掉table表格纵向滚动条
::v-deep .el-table__body-wrapper::-webkit-scrollbar {
  height: 6px; // 纵向滚动条 必写
  width: 0px;
}
//设置table表格字体样式
::v-deep.sidebar-list .el-table .cell {
  font-size: 12px;
  opacity: 0.8;
}
//设置table表格 列宽，字间距样式
::v-deep .el-table {
  .cell {
    height: $tdHeight;
    line-height: $tdHeight;
  }
  // 设置勾选框样式
  .el-checkbox__inner {
    border: 0.0625rem solid #00c1ff;
    background-color: #00c1ff;
    background: rgba($color: #000000, $alpha: 0);
  }
  // 设置选中行样式颜色
  .el-table__body tr.current-row > td {
    background-color: rgba(41, 147, 180, 0.3);
  }
  th,
  td {
    padding: 0;
    color: #fff;
    font-size: 12px;
  }
  th {
    font-size: 14px;
    color: #00c1ff;
    background-color: rgba(41, 147, 180, 0.1);
  }
}
.scene-saveas {
  @include flexbox;
  @include flexAC;
  padding-top: 24px;
  span {
    display: inline-block;
    width: 100px;
    color: #fff;
  }
}
</style>
<style lang="scss">
.evaluate-type1 {
  background-color: #6d6df0;
  @include icon();
}
.evaluate-type2 {
  background-color: #3797f7;
  @include icon();
}
.evaluate-type3 {
  background-color: #42cc71;
  @include icon();
}
.evaluate-type4 {
  background-color: #feb722;
  @include icon();
}
.evaluate-type5 {
  background-color: #f7413f;
  @include icon();
}
.evaluate-type6 {
  background-color: #fcff24;
  @include icon();
}
.realtime-250 {
  background-color: #f32fdf;
  @include icon();
}
.realtime-100 {
  background-color: #fe323a;
  @include icon();
}
.realtime-50 {
  background-color: #ff8a21;
  @include icon();
}
.realtime-25 {
  background-color: #fecb6e;
  @include icon();
}
.realtime-10 {
  background-color: #4bc704;
  @include icon();
}
.realtime-0_1 {
  background-color: #00a0ff;
  @include icon();
}
.forecast-100 {
  background-color: #7f0140;
  @include icon();
}
.forecast-70 {
  background-color: #f800fb;
  @include icon();
}
.forecast-10 {
  background-color: #3cba3e;
  @include icon();
}
.forecast-0 {
  background-color: #fff;
  @include icon();
}
.forecast-50 {
  background-color: #0001e1;
  @include icon();
}
.forecast-25 {
  background-color: #60b8ff;
  @include icon();
}
.forecast-10 {
  background-color: #48b940;
  @include icon();
}
.forecast-1 {
  background-color: #92ff74;
  @include icon();
}

// 供水量图例图标
.legend-icon-xushui {
  background-image: url(~assets/images/gongshui.png);
  background-repeat: no-repeat;
}
.legend-icon-tishui {
  background-image: url(~assets/images/tishui.png);
  background-repeat: no-repeat;
}
.legend-icon-yinshui {
  background-image: url(~assets/images/yinshui.png);
  background-repeat: no-repeat;
}

// 水资源承载能力 水量水质评价图例
.water-buchaozai {
  background-color: #19af47;
  @include icon();
}
.water-linjie {
  background-color: #008fe8;
  @include icon();
}
.water-chaozai {
  background-color: #e89100;
  @include icon();
}
.water-yanzhong {
  background-color: #e80000;
  @include icon();
}

// 水资源承载能力 经济人口评价图例
.water-max {
  background-color: #00c1ff;
  @include icon();
}
.water-min {
  background-color: #19af47;
  @include icon();
}
.water-normal {
  background-color: #33ffff;
  @include icon();
}

//取水户预警图例
.qushuihu-area-legend-1 {
  background-color: #19af47;
  @include icon();
}
.qushuihu-area-legend-2 {
  background-color: #008fe8;
  @include icon();
}
.qushuihu-area-legend-3 {
  background-color: #e89100;
  @include icon();
}

//台风静态风险图例
.typhoon-type5 {
  background-color: #00dc00;
  @include icon();
}
.typhoon-type4 {
  background-color: #00a0ff;
  @include icon();
}
.typhoon-type3 {
  background-color: #ff994a;
  @include icon();
}
.typhoon-type2 {
  background-color: #ff0120;
  @include icon();
}

//海塘图例
.seawall-type-1 {
  background-color: #e80000;
  @include icon();
}
.seawall-type-10 {
  background-color: #dd00db;
  @include icon();
}
.seawall-type-20 {
  background-color: #e89100;
  @include icon();
}
.seawall-type-50 {
  background-color: #eec80b;
  @include icon();
}
.seawall-type-100 {
  background-color: #19af47;
  @include icon();
}
.seawall-type-101 {
  background-color: #4cebc7;
  @include icon();
}
.seawall-type-none {
  background-color: #8d0a0a;
  @include icon();
}
.seawall-type-103 {
  background-color: #ff551f;
  @include icon();
}
.seawall-type-102 {
  background-color: #008fe8;
  @include icon();
}
//水厂
.water-works-sygs-5 {
  background-image: url(~assets/new_images/point/city-icon.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  width: 12px !important;
  height: 12px !important;
}
.water-works-sygs-1 {
  background-image: url(~assets/new_images/point/town-icon.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  width: 12px !important;
  height: 12px !important;
}
.water-works-sygs-3 {
  background-image: url(~assets/new_images/point/union-icon.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  width: 12px !important;
  height: 12px !important;
}
.water-works-sygs-4 {
  background-image: url(~assets/new_images/point/single-icon.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  width: 12px !important;
  height: 12px !important;
}
// 水源供水图例
.legend-sygs-1 {
  background-image: url(~assets/images/legend-sygs-1.png);
  background-repeat: no-repeat;
}
.legend-sygs-2 {
  background-image: url(~assets/images/legend-sygs-2.png);
  background-repeat: no-repeat;
}
.legend-sygs-3 {
  background-image: url(~assets/images/legend-sygs-3.png);
  background-repeat: no-repeat;
}
.legend-sygs-4 {
  background-image: url(~assets/images/legend-sygs-4.png);
  background-repeat: no-repeat;
}
.legend-sygs-5 {
  background-image: url(~assets/images/legend-sygs-5.png);
  background-repeat: no-repeat;
}
.legend-sygs-6 {
  background-image: url(~assets/images/legend-sygs-6.png);
  background-repeat: no-repeat;
}
.legend-sygs-7 {
  background-image: url(~assets/images/legend-sygs-7.png);
  background-repeat: no-repeat;
}
.legend-sygs-8 {
  background-image: url(~assets/images/legend-sygs-8.png);
  background-repeat: no-repeat;
}
// 珊溪图例
.legend-sx-zl-1 {
  background-image: url(~assets/images/legend-sx-zl-1.png);
  background-repeat: no-repeat;
}
.legend-sx-zl-2 {
  background-image: url(~assets/images/legend-sx-zl-2.png);
  background-repeat: no-repeat;
}
.legend-sx-zl-3 {
  background-image: url(~assets/images/legend-sx-zl-3.png);
  background-repeat: no-repeat;
}
.legend-sx-zl-4 {
  background-image: url(~assets/images/legend-sx-zl-4.png);
  background-repeat: no-repeat;
}
.legend-sx-zl-5 {
  background-image: url(~assets/images/legend-sx-zl-5.png);
  background-repeat: no-repeat;
}

.legend-sx-ld-mywr-1 {
  background-image: url(~assets/images/legend-sx-ld-mywr-1.png);
  background-repeat: no-repeat;
}
.legend-sx-ld-mywr-2 {
  background-image: url(~assets/images/legend-sx-ld-mywr-2.png);
  background-repeat: no-repeat;
}
.legend-sx-ld-mywr-3 {
  background-image: url(~assets/images/legend-sx-ld-mywr-3.png);
  background-repeat: no-repeat;
}

.legend-sx-ld-stls-1 {
  background-image: url(~assets/images/legend-sx-ld-stls-1.png);
  background-repeat: no-repeat;
}
.legend-sx-ld-stls-2 {
  background-image: url(~assets/images/legend-sx-ld-stls-2.png);
  background-repeat: no-repeat;
}
.legend-sx-ld-stls-3 {
  background-image: url(~assets/images/legend-sx-ld-stls-3.png);
  background-repeat: no-repeat;
}
.legend-sx-ld-stls-4 {
  background-image: url(~assets/images/legend-sx-ld-stls-4.png);
  background-repeat: no-repeat;
}
.legend-sx-ld-stls-5 {
  background-image: url(~assets/images/legend-sx-ld-stls-5.png);
  background-repeat: no-repeat;
}

.legend-sx-ld-1 {
  background-image: url(~assets/images/legend-sx-ld-1.png);
  background-repeat: no-repeat;
}
.legend-sx-ld-2 {
  background-image: url(~assets/images/legend-sx-ld-2.png);
  background-repeat: no-repeat;
}
.legend-sx-ld-3 {
  background-image: url(~assets/images/legend-sx-ld-3.png);
  background-repeat: no-repeat;
}
.legend-sx-ld-4 {
  background-image: url(~assets/images/legend-sx-ld-4.png);
  background-repeat: no-repeat;
}
.legend-sx-ld-5 {
  background-image: url(~assets/images/legend-sx-ld-5.png);
  background-repeat: no-repeat;
}
.legend-sx-ld-6 {
  background-image: url(~assets/images/legend-sx-ld-6.png);
  background-repeat: no-repeat;
}
.legend-sx-ld-7 {
  background-image: url(~assets/images/legend-sx-ld-7.png);
  background-repeat: no-repeat;
}

.legend-sx-cz-1 {
  background-image: url(~assets/images/legend-sx-cz-1.png);
  background-repeat: no-repeat;
}
.legend-sx-cz-2 {
  background-image: url(~assets/images/legend-sx-cz-2.png);
  background-repeat: no-repeat;
}

.mlhh-legend-1 {
  background-image: url(~assets/images/mlhh-legend-1.png);
  background-repeat: no-repeat;
}
.mlhh-legend-2 {
  background-image: url(~assets/images/mlhh-legend-2.png);
  background-repeat: no-repeat;
}
.mlhh-legend-3 {
  background-image: url(~assets/images/mlhh-legend-3.png);
  background-repeat: no-repeat;
}
.mlhh-legend-4 {
  background-image: url(~assets/images/mlhh-legend-4.png);
  background-repeat: no-repeat;
}

.qushuihu-legend-1 {
  background-image: url(~assets/images/qushuihu-legend-1.png);
  background-repeat: no-repeat;
}
.qushuihu-legend-2 {
  background-image: url(~assets/images/qushuihu-legend-2.png);
  background-repeat: no-repeat;
}
.qushuihu-legend-3 {
  background-image: url(~assets/images/qushuihu-legend-3.png);
  background-repeat: no-repeat;
}
// 取水监管图例
.icon-being-icon {
  background-image: url(~assets/images/icon-being-icon.png);
  background-repeat: no-repeat;
  display: inline-block;
  width: 15px;
  height: 15px;
}
.icon-stop-icon {
  background-image: url(~assets/images/icon-stop-icon.png);
  background-repeat: no-repeat;
  display: inline-block;
  width: 15px;
  height: 15px;
}
.icon-drop-icon {
  background-image: url(~assets/images/icon-drop-icon.png);
  background-repeat: no-repeat;
  display: inline-block;
  width: 15px;
  height: 15px;
}
//堤防图例
.icon-two-legend {
  background-image: url(~assets/images/two-legend.png);
  background-repeat: no-repeat;
  display: inline-block;
  margin-top: 13px;
  width: 15px;
  height: 5px;
}
.icon-four-legend {
  background-image: url(~assets/images/four-legend.png);
  background-repeat: no-repeat;
  display: inline-block;
  margin-top: 13px;
  width: 15px;
  height: 5px;
}
.icon-five-legend {
  background-image: url(~assets/images/five-legend.png);
  background-repeat: no-repeat;
  display: inline-block;
  margin-top: 13px;
  width: 15px;
  height: 5px;
}

.legend-sdz-1 {
  background-image: url(~assets/images/legend-sdz-1.png);
  background-repeat: no-repeat;
}
.legend-sdz-2 {
  background-image: url(~assets/images/legend-sdz-2.png);
  background-repeat: no-repeat;
}

.legend-qsl-1 {
  background-image: url(~assets/images/legend-qsl-1.png);
  background-repeat: no-repeat;
}
.legend-qsl-2 {
  background-image: url(~assets/images/legend-qsl-2.png);
  background-repeat: no-repeat;
}
.legend-qsl-3 {
  background-image: url(~assets/images/legend-qsl-3.png);
  background-repeat: no-repeat;
}
.legend-qsl-4 {
  background-image: url(~assets/images/legend-qsl-4.png);
  background-repeat: no-repeat;
}
.legend-qsl-5 {
  background-image: url(~assets/images/legend-qsl-5.png);
  background-repeat: no-repeat;
}

.legend-hzxc-1 {
  background-image: url(~assets/images/legend-hzxc-1.png);
  background-repeat: no-repeat;
}
.legend-hzxc-2 {
  background-image: url(~assets/images/legend-hzxc-2.png);
  background-repeat: no-repeat;
}
.legend-hzxc-3 {
  background-image: url(~assets/images/legend-hzxc-3.png);
  background-repeat: no-repeat;
}
.legend-hzxc-4 {
  background-image: url(~assets/images/legend-hzxc-4.png);
  background-repeat: no-repeat;
}
.icon-rivernet-legend-normal,
.icon-rivernet-legend-abnormal {
  width: 20px !important;
  height: 10px !important;
}
.ranking-icon {
  width: 1.5rem;
  height: 1.5rem;
  background-size: 1rem 1.3rem;
  background-repeat: no-repeat;
  background-position: center;
  &.government-icon-top1 {
    background-image: url(~assets/images/top1.png);
  }
  &.government-icon-top2 {
    background-image: url(~assets/images/top2.png);
  }
  &.government-icon-top3 {
    background-image: url(~assets/images/top3.png);
  }
}
.rank-icon {
  position: absolute;
  display: inline-block;
  height: 1.5rem;
  width: 1.5rem;
  top: 0.3rem;
  left: -1.5875rem;
  background-repeat: no-repeat;
  &.top1 {
    background-image: url(~assets/images/topOne.png);
  }
  &.top2 {
    background-image: url(~assets/images/topTwo.png);
  }
  &.top3 {
    background-image: url(~assets/images/topThree.png);
  }
  &.rank-icon-up {
    left: 97%;
    top: 9%;
    background-position: right;
    background-image: url(~assets/images/rankRise.png);
  }
  &.rank-icon-down {
    left: 97%;
    top: 9%;
    background-position: right;
    background-image: url(~assets/images/rankDown.png);
  }
}

.legend-item {
  i {
    background-repeat: no-repeat;
    border-radius: 50%;
    &.icon-one-icon {
      background-image: url(~assets/images/icon-one-icon.png);
    }
    &.icon-two-icon {
      background-image: url(~assets/images/icon-two-icon.png);
    }
    &.icon-three-icon {
      background-image: url(~assets/images/icon-three-icon.png);
    }
    &.icon-four-icon {
      background-image: url(~assets/images/icon-four-icon.png);
    }
    &.icon-five-icon {
      background-image: url(~assets/images/icon-five-icon.png);
    }
    &.icon-six-icon {
      background-image: url(~assets/images/icon-six-icon.png);
    }
    &.icon-error-icon {
      background-image: url(~assets/images/icon-error-icon.png);
    }
    // 工情 图例图标
    &.icon-legend-sluice-wc {
      background: center url(~assets/images/legend-sluice-normal.png);
      background-size: 100%;
    }
    &.icon-legend-reservoir-wc {
      background-image: url(~assets/images/legend-water-general-reservoir.png);
      background-size: 100%;
    }
    &.icon-legend-pump-wc {
      background-image: url(~assets/images/pump3small.png);
      background-size: 100%;
    }
    // 水情图例图标
    &.icon-legend-seawall-wr {
      background: center url(~assets/images/legend-sluice-normal.png);
      background-size: 100%;
    }
    &.icon-legend-reservoir-wr {
      background-image: url(~assets/images/legend-water-general-reservoir.png);
      background-size: 100%;
    }
    &.icon-legend-tide-wr {
      background-image: url(~assets/images/pump3small.png);
      background-size: 100%;
    }
    &.icon-legend-typhoon-wr {
      background-image: url(~assets/images/pump3small.png);
      background-size: 100%;
    }
    &.icon-legend-river-wr {
      background-image: url(~assets/images/pump3small.png);
      background-size: 100%;
    }
    &.icon-legend-weirgate-wr {
      background-image: url(~assets/images/pump3small.png);
      background-size: 100%;
    }
    // 开启关闭 正常超警
    &.icon-kaiqi {
      background-image: url(~assets/images/legend-water-user-doing.png);
      background-size: 100%;
    }
    &.icon-guanbi {
      background-image: url(~assets/images/map-waterdoor-warn.png);
      background-size: 100%;
    }
    // 救援队伍 图例图标
    &.icon-legend-rescueteam-point {
      background-image: url(~assets/images/MapPoint/point-rescueteam.png);
      background-size: 100%;
    }
    &.icon-legend-rescueteam-group {
      background-image: url(~assets/images/MapPoint/group-rescueteam.png);
      background-size: 100%;
    }
  }
}
</style>
