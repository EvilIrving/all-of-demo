<script>
import { factoryCardComponent, factoryStatistics, getChildComponent } from './utils.js';
import { DragCard, PrimaryDialog, PrimaryLegend, ProjectDialog } from '@/components';
import ScreenBackMap from './components/ScreenBackMap.vue';
import RenderRightTabControl from './components/RenderRightTabControl.vue';
import ComponentList from './cards';
import StatComponentList from './countCards';
import SelectCards from './components/SelectCards.vue';
import { mapActions, mapGetters } from 'vuex';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index.js';
import DetailsCommonDialog from './dialog/DetailsCommonDialog/index.vue';
import lcwxqyGeoJson from '@/assets/geojson/lc_weixiequyu.json';
// 抗旱监管

import DataDetailDialog from '../water_disaster_custom/dialog/DetailsCommonDialog/components/DataDetailDialog.vue'
import WaterIntakeDialog from './dialog/WaterIntakeDialog';
import SluiceDialogForSchedule from './dialog/SluiceDialogForSchedule/index.vue';
import MotionDialog from './dialog/MotionDialog/index.vue'; // 概化图tab页面
import MotionSluiceDialog from './dialog/MotionSluiceDialog.vue'; // 概化图水闸
import WatershedDisasterRiskAssessment from './dialog/watershedDisasterRiskAssessment/WatershedDisasterRiskAssessment.vue';
import Seawall from './dialog/DetailsCommonDialog/components/cardListDetail/seawallCardList/Index'
export default {
  name: 'disasterPreventionScheduling',
  inject: ['reload'],
  components: {
    DragCard,
    ProjectDialog,
    DataDetailDialog,
    Seawall,
    PrimaryDialog, PrimaryLegend,
    WaterIntakeDialog,
    MotionSluiceDialog,
    MotionDialog,
    SluiceDialogForSchedule,
    WatershedDisasterRiskAssessment,
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
      floodDetailDialog: false,
      disasterDialog: false,
      projectDialogVisible: false,
      BigReservoirShow: false,
      SelectionTipsShow: false,
      SendMsgShow: false,
      WaterIntakeShow: false, // 抗旱监管
      sluiceDialogForSchedule: false, // 防汛风险
      motionSluiceDialogShow: false, // 概化图 水闸弹窗
      motionDialogShow: false, // 概化图 水闸弹窗
      watershedDisasterRiskAssessment: false, // 山洪灾害短信功能
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
      realtimePreList: {}, //实时降雨数据对象
      forecastPreList: {} //预报降雨数据对象
    };
  },
  created() { },
  beforeMount() {
    this.SetCheckedArr([]);
  },
  mounted() {
    this.createEventHandler();
    // this.listPrevTfInfo();
    this.listPrevObj();
    // //设置大屏标记
    let bigScreenflag = this.$route.query.isBigScreen == 'true';
    this.SetBigScreen(bigScreenflag);
    // //获取实时雨量数据
    // // this.getRealRainfallData();
    // //获取预报降雨数据
    // // this.getPreRainData();
    // if (this.qiankunModule) {
    //   this.SetBsName(this.$localData('get', 'systemType'));
    // }
    this.$nextTick(() => {
      this.setSimple(false);
      this.SetMapComponent(this.$refs.mapsche);
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
      //this.previewActive = this.warningListShow ? '预演' : '预演';
    });
  },
  beforeDestroy() {
    // window.removeEventListener('resize', this.handleWindowResize);
  },
  computed: {
    ...mapGetters([
      'CheckedArr',
      'SceneList',
      'BsName',
      'DetailDialogParam',
      'sluiceDistributionMap',
      'CheckedNoBxArr',
      'CardHeight',
      'AddCardDialog',
      'SaveAsDialog',
      'ActiveScene',
      'isBigScreen',
      'SceneComponentList',
      // 'WaterStudyDetailsShow',
      'StatisticFlag',
      'ActiveCount',
      // 'IsCollapse',
      // 'formatterStartTime',
      // 'formatterEndTime',
      // 'warningListShow',
      'ComponentLoading'
      // 'previewModule',
      // 'TempZIndex',
      // 'riverwayShow',
      // 'trendAnalysisShow',
      // 'RangeBar',
      // 'TimelineList',
      // 'seawallRangModule',
      // 'Simple',
      // 'simpleRightType',
      // 'simpleTimes'
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
      return {
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
        防汛风险: <hydropower-station-count />
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
    StatisticFlag(val) {
      if (val) {
        !this.ActiveCount && this.SetActiveCount(this.CheckedNoBxArr[0]);
      }
    },
    CheckedNoBxArr(val) {
      const index = val.findIndex((item) => item == this.ActiveCount);
      if (index == -1) this.SetActiveCount(val[0]);
    },
    // CheckedArr(components) {
    //   const listCurrentCardName = Object.keys(this.cardMap).filter((item) => components.includes(item));
    //   const listCurrentScene = listCurrentCardName.map((item) => this.cardMap[item].props.key);
    //   const listNeedClear = Object.keys(ComponentList4kebabCase)
    //     .filter((key) => !listCurrentScene.includes(key))
    //     .map((comp) => ComponentList4kebabCase[comp]);
    //   listNeedClear.forEach((item) => {
    //     if (item.layerName) this.removeLayers(item.layerName);
    //     if (item.legendName) this.removeLegends(item.legendName);
    //   });
    // },
    warningListShow(val) {
      let data = Array.from(this.CheckedNoBxArr);
      this.SetActiveCount(data[0]);
    }
  },
  methods: {
    ...mapActions([
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
      'showWarningList',
      'hideWarningList',
      'setDistributionMap',
      'setSimple'
    ]),
    createEventHandler() {
      // 关闭卡片
      this.$EventBus.$on('clearLayer', (payload) => {
        let { layerName, legendName, previewModule } = payload;
        if (layerName) {
          if (layerName == 'designFloodLayer') {
            //清除洪水风险图
            this.$refs.mapsche.clearDesignFlood();
          } else if (layerName instanceof Array && layerName.includes('emergencySupport')) {
            //清除抢险支持路线
            this.$refs.mapsche.clearDispatch();
          } else if (legendName == '水资源承载能力') {
            this.$nextTick(() => {
              this.removeLayers(layerName[0]);
            });
            this.$refs.mapsche.removeOverlayByName(layerName[1]);
          } else if (layerName == 'waterChangeOverlay') {
            this.$refs.mapsche.removeOverlayByName(layerName);
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
    async listPrevTfInfo() {
      // if (!this.ylbtn) {
      //   this.ScreenMap.drawClusterPoint([], {}, "normalPointLayer");
      //   this.ScreenMap.drawClusterPoint([], {}, "importPointLayer");
      //   return;
      // }
      let opt = {};
      let res = await disasterPreventionApi.listPrevTfInfo(opt);
      console.log(res, 'listPrevTfInfo');
      // let normalArr = [];
      // let importArr = [];
      let areaPolyArr = [];
      for (let i = 0; i < res.data.length; i++) {
        let item = res.data[i];
        item.type = '山洪';
        item.coordinates = item.areaPoints;
        areaPolyArr.push(item);
        // if (res.data[i].recType == "一般村落") {
        //   let item = res.data[i];
        //   item.src = require("@/assets/images/MapPoint/normal_village.png");
        //   item.lng = item.lon;
        //   item.name = item.recType;
        //   item.type = "山洪";
        //   item.modular = "流域灾害风险评估";
        //   item.props = {
        //     // 总户数: item.tho + "户",
        //     行政村: item.avi,
        //     自然村: item.nvi,
        //     人数: item.tpo + "人",
        //     转移责任人: item.chargerName,
        //     电话: item.chargerPhone
        //   };
        //   normalArr.push(item);
        // } else if (res.data[i].recType == "重要村落") {
        //   let item = res.data[i];
        //   item.lng = item.lon;
        //   item.type = "山洪";
        //   item.modular = "流域灾害风险评估";
        //   item.src = require("@/assets/images/MapPoint/important_village.png");
        //   item.name = item.recType;
        //   item.props = {
        //     // 总户数: item.tho + "户",
        //     行政村: item.avi,
        //     自然村: item.nvi,
        //     人数: item.tpo + "人",
        //     转移责任人: item.chargerName,
        //     电话: item.chargerPhone
        //   };
        //   importArr.push(item);
        // } else {
        //   let item = res.data[i];
        //   item.type = "山洪";
        //   item.coordinates = item.areaPoints;
        //   areaPolyArr.push(item);
        // }
      }
      // const normalStyle = {
      //   src: require("@/assets/images/MapPoint/normal_village.png"),
      //   // srcs: require("@/assets/images/MapPoint/normal-village-group.png"),
      //   scale: 0.7,
      //   anchor: [10, 12],
      //   anchorXUnits: "pixels",
      // };
      // const importStyle = {
      //   src: require("@/assets/images/MapPoint/important_village.png"),
      //   // srcs: require("@/assets/images/MapPoint/important-village-group.png"),
      //   scale: 0.7,
      //   anchor: [10, 12],
      //   anchorXUnits: "pixels",
      // };
      // this.$refs.mapsche.drawPoint(normalArr, normalStyle, 'normalPointLayer');
      // this.$refs.mapsche.drawPoint(importArr, importStyle, 'importPointLayer');
      this.$refs.mapsche.drawAreaWKT(areaPolyArr);
    },
    async listPrevObj() {
      // if (!this.ylbtn) {
      //   this.ScreenMap.drawClusterPoint([], {}, "normalPointLayer");
      //   this.ScreenMap.drawClusterPoint([], {}, "importPointLayer");
      //   return;
      // }
      let opt = {};
      let res = await disasterPreventionApi.listPrevObj(opt);
      // console.log(res, 'listPrevObj');
      let normalArr = [];
      let importArr = [];
      for (let i = 0; i < res.data.length; i++) {
        if (res.data[i].recType == '一般村落') {
          let item = res.data[i];
          item.src = require('@/assets/images/MapPoint/normal_village.png');
          item.lng = item.lon;
          item.name = item.recType;
          item.type = '山洪';
          item.modular = '流域灾害风险评估';
          item.props = {
            // 总户数: item.tho + "户",
            行政村: item.avi,
            自然村: item.nvi,
            人数: item.tpo + '人',
            总人口: item.chargerName,
            电话: item.chargerPhone
          };
          normalArr.push(item);
        } else {
          let item = res.data[i];
          item.lng = item.lon;
          item.type = '山洪';
          item.modular = '流域灾害风险评估';
          item.src = require('@/assets/images/MapPoint/important_village.png');
          item.name = item.recType;
          item.props = {
            // 总户数: item.tho + "户",
            行政村: item.avi,
            自然村: item.nvi,
            总人口: item.tpo + '人',
            转移责任人: item.chargerName,
            电话: item.chargerPhone
          };
          importArr.push(item);
        }
      }
      const normalStyle = {
        src: require('@/assets/images/MapPoint/normal_village.png'),
        // srcs: require("@/assets/images/MapPoint/normal-village-group.png"),
        scale: 1,
        anchor: [10, 12],
        anchorXUnits: 'pixels'
      };
      const importStyle = {
        src: require('@/assets/images/MapPoint/important_village.png'),
        // srcs: require("@/assets/images/MapPoint/important-village-group.png"),
        scale: 1,
        anchor: [10, 12],
        anchorXUnits: 'pixels'
      };
      this.$refs.mapsche.drawPoint(normalArr, normalStyle, 'normalPointLayer');
      this.$refs.mapsche.drawPoint(importArr, importStyle, 'importPointLayer');
      this.getLcwxqyGeoJsonLayer();
    },
    //获取鹿城威胁区域
    getLcwxqyGeoJsonLayer() {
      this.$refs.mapsche.addLcwxqyGeoJsonLayer(lcwxqyGeoJson);
    },
    removeLayerAction(layerName) {
      if (layerName == '台风') {
        this.$refs.mapsche.clearAllTyphoon();
      } else {
        this.$refs.mapsche.removeLayerByName(layerName);
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
      let highHeight = document.body.clientHeight - 220 + 'px';
      let height = parseInt(highHeight) / 3 + 'px';
      this.SetHighCardHeight(highHeight);
      this.SetCardHeight(height);
    },
    handleCardClose() {
      this.SetAddcardDialog(false);
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
        const opt = {
          bsName: this.BsName,
          mode: this.qiankunModule ? 2 : 1
        };
        this.GetSceneList(opt).then((res) => {
          //获取当前场景组件信息列表
          let param = {
            bsName: this.BsName,
            id: this.ActiveScene.id,
            mode: this.qiankunModule ? 2 : 1
          };
          if (!id) {
            param.id = res[res.length - 1].id;
          }
          this.GetSceneComponentInfo(param).then((result) => {
            this.SetActiveCount(result[0]);
          });
          let defaultScene = {
            name: res[res.length - 1].sceneName,
            id,
            editable: this.ActiveScene.editable
          };
          if (!id) {
            defaultScene.id = res[res.length - 1].id;
            defaultScene.editable = res[res.length - 1].editable;
          }
          this.SetActiveScene(defaultScene);
        });
        this.$message({
          type: 'success',
          message: '保存成功'
        });
        this.SetSaveAsDialog(false);
        this.sceneName = '';
      }
    },
    // sendMessage() {
    //   this.$refs.mapsche.openMessage();
    // },
    // selectResponseLevel() {
    //   console.log('jjxy');
    // },
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
                    this.$refs.mapsche.map.getLayers().forEach((element) => {
                      if (element && this.legends[legend].clusterLayer.indexOf(element.get('name')) != -1) {
                        element.getSource().setDistance(parseInt(30, 20));
                      }
                    });
                    this.$refs[this.legends[legend].clusterLayer[0]].setAttribute('class', 'el-icon-location');
                  } else {
                    this.$refs.mapsche.map.getLayers().forEach((element) => {
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
          style={{ right: '30.5rem', bottom: '153px' }}
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
        <ScreenBackMap
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
          ref="mapsche"
        />
      );
    },
    // 右上角功能区
    renderShortMessagebox() {
      // console.log('右上角发送短信区');
      return (
        <RenderRightTabControl onSelectTips={() => { }}></RenderRightTabControl>
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
            width="1200px"
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
    closeWaterIntakeShow() {
      this.WaterIntakeShow = false;
    },
    closeSluiceDialogForSchedule() {
      this.sluiceDialogForSchedule = false;
    },
    closeWatershedDisasterRiskAssessment() {
      this.watershedDisasterRiskAssessment = false;
    },
    //  概化图 水闸弹窗
    closeMotionSluiceDialogShow() {
      this.motionSluiceDialogShow = false;
    },
    //  概化图
    closeMotionDialogShow() {
      this.motionDialogShow = false;
    },
    sendValue(val) {
      if (val.length > 0) {
        this.FloodForecastingVal = val;
        this.FloodForecastingShow = true;
      } else {
        this.FloodForecastingShow = false;
      }
    },

    // 概化图水闸
    renderMotionSluiceDialog() {
      return (
        this.motionSluiceDialogShow && (
          <MotionSluiceDialog
            ref="motionSluiceDialogShow"
            visible={this.motionSluiceDialogShow}
            title={'概化图水闸'}
            pointData={this.pointData}
            onCloseDialog={this.closeMotionSluiceDialogShow}
          />
        )
      );
    },
    // 概化图
    renderMotionDialog() {
      return (
        // this.motionDialogShow && (
        //   <MotionDialog
        //     ref="motionDialogShow"
        //     visible={this.motionDialogShow}
        //     title={'概化图'}
        //     pointData={this.pointData}
        //     onCloseDialog={this.closeMotionDialogShow}
        //   />
        // )
        this.motionDialogShow && (
          <PrimaryDialog
            visible={this.motionDialogShow}
            main-title={this.pointData.name}
            width="1200px"
            onHandleClose={() => this.closeMotionDialogShow()}
          >
            <MotionDialog pointData={this.pointData} />
          </PrimaryDialog>
        )
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
    renderWatershedDisasterRiskAssessment() {
      return (
        this.watershedDisasterRiskAssessment && (
          <PrimaryDialog
            visible={this.watershedDisasterRiskAssessment}
            main-title={this.pointData.nvi}
            width="1000px"
            top="20vh"
            onHandleClose={() => this.closeWatershedDisasterRiskAssessment()}
          >
            <WatershedDisasterRiskAssessment pointData={this.pointData} />
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
    renderProjectDialog() {
      return (
        this.projectDialogVisible && <ProjectDialog
          visible={this.projectDialogVisible}
          point-data={this.pointData}
          onClose={this.handleProjectDialogClose}
          ref="projectDialog"
        />
      );
    },
    handleProjectDialogClose() {
      this.projectDialogVisible = false;
      this.pointData = {};
    },
    // 抗旱监管
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
      this.dispatchLat = lat;
      this.dispatchLng = lng;
      this.foreCastShow = true;
    },
    closeForecastDialog() {
      this.foreCastShow = false;
    },
    //获取调度信息
    dispatchInfo(data, stationInfo) {
      this.$refs.mapsche.dispatchInfo(data, stationInfo);
      this.SendMsg();
    },
    /**
     * 图层点击事件
     */
    showDialog(val) {
      console.log(val, 'schedule 图层点击事件');
      this.cardInitFlag = false;
      let dialogList = ['抗旱监管', '防汛风险', '洪水风险图', '山洪', '概化图水闸', '海塘', '概化图'];
      if (!val.type || !dialogList.includes(val.type)) return;
      this.pointData = val;
      const methodsMap = {
        抗旱监管: () => {
          if (val.projectType !== 'sluice' && val.projectType !== 'reservoir') {
            this.WaterIntakeShow = true;
          } else {
            this.sluiceDialogForSchedule = true;
          }
        },
        防汛风险: () => {
          this.sluiceDialogForSchedule = true;
        },
        概化图水闸: () => {
          this.motionSluiceDialogShow = true;
        },
        概化图: () => {
          this.motionDialogShow = true;
        },
        山洪: () => {
          this.watershedDisasterRiskAssessment = true;
        }
      };
      if (Object.keys(methodsMap).includes(val.type)) methodsMap[val.type]();
      else {
        this.projectDialogVisible = true;
        this.$nextTick(() => {
          this.$refs.projectDialog.getProjectInfo();
          this.$refs.projectDialog.getContactList();
        });
      }
    },
    handleLegendChange(name, legend = {}) {
      if (Object.keys(legend).length == 0) delete this.legends[name];
      else this.legends[name] = legend;
      this.showLegend = Object.keys(legend).length > 0 || Object.keys(this.legends).length > 0;
      this.$forceUpdate();
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
      return this.CheckedArr.map((item) => {
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
      // this.$refs.mapsche.drawClusterPoint([], {}, 'digReservoir1Layer');
      let index = this.CheckedNoBxArr.findIndex((item) => item == this.ActiveCount);
      const targetIndex = index == this.CheckedNoBxArr.length - 1 ? 0 : index + 1;
      this.SetActiveCount(this.CheckedNoBxArr[targetIndex]);
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
        this.$refs.mapsche.removeLayerByName('rainfall');
        this.$refs.mapsche.clearTextLayer();
        //加载实时数据
        const rainList = this.realtimePreList[mapArray[val]];
        this.$refs.mapsche.loadRainfallPoint(rainList, true);
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
        this.$refs.mapsche.loadRainfallPoint([], false);
        //加载预报数据
        let data = this.forecastPreList[`rainfall_${ValMap[val]}_data`];
        if (data) {
          this.$refs.mapsche.showPredictRain(data, { hour: ValMap[val] });
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
    // const { renderMap, renderLegend, renderFactoryCard, renderPreviewCard, renderStatCard } = this;
    const { renderMap, renderShortMessagebox, renderLegend, renderStatCard, renderFactoryCard } = this;
    const detailDialog = this.renderDetailDialog();
    const addDialog = this.renderAddDialog();
    const saveAsDialog = this.renderSaveAsDialog();
    const WaterIntakeDialog = this.renderWaterIntakeDialog(); // 抗旱监管
    const SluiceDialogForSchedule = this.renderSluiceDialogForSchedule(); // 防汛风险
    const WatershedDisasterRiskAssessment = this.renderWatershedDisasterRiskAssessment(); // 山洪灾害短信功能
    const projectDialog = this.renderProjectDialog();
    const RenderMotionSluiceDialog = this.renderMotionSluiceDialog();
    const RenderMotionDialog = this.renderMotionDialog();
    return (
      <div
        v-loading={this.ComponentLoading}
        class="disaster-prevention"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        ref="bigScreen"
      >
        {[renderMap(), renderShortMessagebox(), renderLegend(), renderStatCard(), renderFactoryCard()]}
        {addDialog}
        {saveAsDialog}
        {detailDialog}
        {WaterIntakeDialog}
        {SluiceDialogForSchedule}
        {WatershedDisasterRiskAssessment}
        {projectDialog}
        {RenderMotionSluiceDialog}
        {RenderMotionDialog}
      </div>
    );
  }
};
</script>

<style lang="scss" scoped>
@import '@/style/bigScreen.scss';
@import '@/style/sidebarStyle.scss';
.disaster-prevention {
  width: 100%;
  height: 100%;
  background: url(~assets/images/da-ping-bg.png) no-repeat top center;
  background-size: 100% 100%;
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
  .tab-box {
    position: absolute;
    right: 20%;
    top: 16%;
    z-index: 100;
    .tab-control {
      width: 128px;
      height: 43px;
      background: url(~assets/images/ScreenAsset/tab-control-icon.png) no-repeat;
      background-size: 100% 100%;
      padding-left: 16px;
      box-sizing: border-box;
      line-height: 43px;
      position: relative;
      font-size: 16px;
      color: #ffffff;
      margin-bottom: 24px;
      cursor: pointer;
      &::after {
        content: '';
        position: absolute;
        width: 60px;
        height: 60px;
        right: -10px;
        top: 50%;
        margin-top: -30px;
      }
      &.tab-control-00 {
        &::after {
          background: url(~assets/images/scheduling/shortMessage.png) no-repeat;
          background-size: 100% 100%;
        }
      }
    }
    .responseLevel {
      background: #0f1b2590;
      margin-top: 5px;
      width: 128px;
      position: absolute;
      right: 0;
      text-align: center;
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
    top: 140px;
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
  bottom: 7%;
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

//抗旱监管预警图例
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
