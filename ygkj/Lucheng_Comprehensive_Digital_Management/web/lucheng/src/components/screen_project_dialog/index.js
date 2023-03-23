import { reservoirRegulationApi } from '@/api/dataScreen/ReservoirRegulation';
import { hydraulicApi } from '@/api/hydraulic_amap';
import PrimaryDialog from '../primary_dialog';
import ResponsibleList from './components/ResponsibleList.vue';
import BasicInfo from './components/BasicInfo.vue';
import FloodForecast from './components/FloodForecast';
import RunningInfo from './components/RunningInfo.vue';
import ProjectVideo from './components/ProjectVideo.vue';
import './style.scss';

/** 自定义iframe */
let CustomIframe = {
  props: {},
  render() {
    return <iframe style={{ width: '100%', height: '100%', 'min-height': '450px' }} />;
  }
};
/** 工程信息 用了俩组件 简单二次封装下*/
let ProjectInfo = {
  props: { responsibleList: Array, type: String, propList: Array },
  render() {
    return (
      <div class="project-info">
        <el-row gutter={20}>
          <el-col span={12}>
            <BasicInfo type={this.type} propList={this.propList} />
          </el-col>
          <el-col span={12}>
            <ResponsibleList responsibleList={this.responsibleList} />
          </el-col>
        </el-row>
      </div>
    );
  }
};

const displayPropsMap = {
  水库: [
    { name: '坝型', prop: 'mainWrType' },
    { name: '工程规模', prop: 'engScal' },
    { name: '行政区划', prop: 'areaName' },
    { name: '总库容（万m³）', prop: 'totCap' },
    { name: '校核洪水位(m)', prop: 'checkFloodLevel' },
    { name: '设计洪水位(m)', prop: 'desFlStag' },
    { name: '防洪高水位(m)', prop: 'uppLevFlco' },
    { name: '梅汛限水位(m)', prop: 'meiFloodLimitWaterLevel' },
    { name: '台汛限水位(m)', prop: 'yphoonFloodLlimitWaterLevel' }
  ],
  山塘: [
    { name: '所在乡镇名称', prop: 'strName' },
    { name: '所在村庄名称', prop: 'vilName' },
    { name: '山塘类型', prop: 'mnpdCate' },
    { name: '总容积(万m³)', prop: 'jhhsRj' },
    { name: '设计洪水位(m)', prop: 'sjhsSw' },
    { name: '校核洪水位(m)', prop: 'jhhsP' },
    { name: '正常蓄水位(m)', prop: 'zcsxswSw' },
    { name: '影响人口(人)', prop: 'yxrk' },
    { name: '灌溉面积(亩)', prop: 'ggmj' }
  ],
  农饮水: [
    { name: '供水工程所在位置', prop: 'cwsLoc' },
    { name: '工程类型', prop: 'engType' },
    { name: '供水方式', prop: 'wasuType' },
    { name: '供水范围', prop: 'wasuRang' },
    { name: '设计供水规模', prop: 'desWasuScal' },
    { name: '设计供水入口', prop: 'desWasuPop' },
    { name: '工程建设情况', prop: 'engStat' },
    { name: '水处理工艺', prop: 'wtPro' },
    { name: '消毒设施设备', prop: 'dfType' },
    { name: '水源类型', prop: 'watersupplytype' },
    { name: '供水村庄', prop: 'watervillages' }
  ],
  水闸: [
    { name: '名称', prop: 'engScal' },
    { name: '水位', prop: 'referenceWlStationCriticalLevel' },
    { name: '更新时间', prop: 'desLockDisc' }
    // { name: '闸孔数量', prop: 'gaorNum' },
    // { name: '闸孔总净宽', prop: 'gaorTotNetWid' },
    // { name: '闸门宽度', prop: 'gatewidth' },
    // { name: '闸底高程', prop: 'gatebottomelevation' },
    // { name: '闸门形式', prop: 'gateType' },
    // { name: '闸门高度', prop: 'gateSize' },
    // { name: '警戒水位', prop: 'referenceWlStationWarningLevel' },
    // { name: '危急水位', prop: 'referenceWlStationCriticalLevel' }
  ],
  潮位: [],
  泵站: [
    { name: '地址', prop: 'pustLoc' },
    { name: '工程规模', prop: 'engScal' },
    { name: '泵站类型', prop: 'pustType' },
    { name: '装机流量(m³/s)', prop: 'insFlow' },
    { name: '装机功率(kW)', prop: 'insPow' },
    { name: '水泵数量(台)', prop: 'pumpNum' },
    { name: '设计扬程(m)', prop: 'desHead' },
    { name: '警戒水位(m)', prop: 'pumpReferenceWlStationWarningLevel' },
    { name: '危急水位(m)', prop: 'pumpReferenceWlStationCriticalLevel' }
  ],
  堤防: [
    { name: '提防级别', prop: 'dikeGrad' },
    { name: '堤防类型', prop: 'dikeType' },
    { name: '堤防型式', prop: 'dikePatt' },
    { name: '防潮标准(年)', prop: 'planFlSta' },
    { name: '堤防长度(m)', prop: 'dikeLen' },
    { name: '堤顶最小宽度(m)', prop: 'dikeTopWidMin' },
    { name: '堤顶最大宽度(m)', prop: 'dikeTopWidMax' },
    { name: '保护范围(km²)', prop: 'protectionArea' },
    { name: '警戒水位(m)', prop: 'warnWaterLevel' }
  ],
  海塘: [
    { name: '海塘型式', prop: 'seawallType' },
    { name: '海塘级别', prop: 'seawallLevel' },
    { name: '防潮标准(年)', prop: 'designTideStandard' },
    { name: '海塘长度(m)', prop: 'seawallLength' },
    { name: '海塘平均高程(m)', prop: 'averageAltitude' },
    { name: '防护范围(km³)', prop: 'protectionArea' },
    { name: '保护人口(万人)', prop: 'protectionPopulation' }
    // { name: "蓝色警戒水位(m)", prop: "" },
    // { name: "黄色警戒水位(m)", prop: "" },
    // { name: "橙色警戒水位(m)", prop: "" },
    // { name: "红色警戒水位(m)", prop: "" },
  ],
  水电站: [
    { name: '地址', prop: 'hystLoc' },
    { name: '工程规模', prop: 'engScal' },
    { name: '水电站类型', prop: 'hystType' },
    { name: '总装机容量(kW)', prop: 'totInsCap' },
    { name: '保证出力(kW)', prop: 'firmPow' },
    { name: '额定水头(m)', prop: 'ratHead' },
    { name: '机组台数(台)', prop: 'generatorsCount' },
    { name: '多年平均发电量(万kW·h)', prop: 'yearsAveragePowerGeneration' },
    { name: '水轮机型式', prop: 'hydraulicTurbineMainType' }
  ],
  灌区: [
    { name: '工程规模', prop: 'engScal' },
    { name: '设计灌溉面积', prop: 'desIrrArea' },
    { name: '有效灌溉面积', prop: 'effIrrArea' },
    { name: '管理单位', prop: 'manUnit' },
    { name: '受益范围', prop: 'benRan' }
  ],
  闸站: [
    { name: '地址', prop: 'locStreet' },
    { name: '所属流域', prop: 'bas' },
    { name: '所在河流', prop: 'river' },
    { name: '工程规模', prop: 'engScal' },
    { name: '单台装机流量', prop: 'singleInsFlow' },
    { name: '单台装机功率', prop: 'singleInsPow' },
    { name: '最大过闸流量', prop: 'maxFlow' }
  ]
};
import SeawallCardList from '@/views/water_disaster_custom/dialog/DetailsCommonDialog/components/cardListDetail/seawallCardList/Index.vue';
export default {
  name: 'ProjectDialog',
  props: {
    visible: { type: Boolean, default: false },
    pointData: { type: Object },
    cardInitFlag: { type: Boolean, default: false },
    tabList: {
      type: Array,
      default: () => ['工程信息', '视频监控', '运行管理','运行曲线']
    },
    removeTabs: { type: Array, default: () => [] },
    analysisFlag: { type: Boolean, default: false }
  },
  data() {
    return {
      currentTab: 0,
      contactList: [],
      propList: [],
      frameUrl: '',
      token:
        '',
      fullscreen: false
    };
  },
  components: { SeawallCardList },
  mounted() {
    // this.getDynamicToken();
    // this.getContactList();
    // this.getProjectInfo();
  },
  render() {
    let title = this.pointData.name;
    // const projectTabs = this.renderTabs();
    // const projectContent = this.renderContent();
    return (
      <PrimaryDialog
        visible={this.visible}
        onAnalysis={() => this.$emit('analysis')}
        main-title={title}
        has-analysis={this.analysisFlag}
        width="1300px"
        onHandleClose={() => this.handleClose()}
        // fullscreenFlag={true}
        // fullscreen={this.fullscreen}
        // onChangeFullscreen={() => (this.fullscreen = !this.fullscreen)}
      >
        {this.pointData.type == '海塘' || this.pointData.type == '防汛风险' ? (
          <SeawallCardList seawallData={this.pointData} />
        ) : (
          <div class="project-dialog">
            <div class="project-tabs">
              {/* <div class="project-tabs-list">{projectTabs}</div> */}
            </div>
            {/* <div class="project-content">{projectContent}</div> */}
          </div>
        )}
      </PrimaryDialog>
    );
  },
  computed: {
    displayTabList() {
      let list = this.tabList.filter((item) => {
        return this.removeTabs.indexOf(item) == -1;
      });
      return list;
    },
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  methods: {
    handleClose() {
      if (this.pointData.isFxfx) {
        this.removeAllLayer()
      }
      this.$emit('close');
    },
    removeAllLayer() {
      this.map.drawClusterPoint([], {}, `dryBrakeLayer`);
      this.map.drawClusterPoint([], {}, `wharfLayer`);
      this.map.drawClusterPoint([], {}, `intersectLayer`);
      this.map.drawClusterPoint([], {}, `bridgeLayer`);
      this.map.drawClusterPoint([], {}, `stakeLayer`);
      this.map.drawClusterPoint([], {}, `culvertLayer`);
      this.map.drawClusterPoint([], {}, `sluiceLayer`);
      this.map.drawClusterPoint([], {}, `pumpLayer`);
      this.map.drawClusterPoint([], {}, `rainbowLayer`);
      this.map.drawClusterPoint([], {}, `roomLayer`);
      this.map.drawClusterPoint([], {}, `stationLayer`);
      this.map.drawClusterPoint([], {}, `watergateLayer`);
    },
    handleTabClick(index) {
      this.currentTab = index;
      if (this.tabList[this.currentTab] == '运行管理') {
        this.fullscreen = true;
      } else {
        this.fullscreen = false;
      }
    },
    renderTabs() {
      let jsx = this.displayTabList.map((item, index) => {
        let jsxStyle = ['project-tabs-item', index == this.currentTab && 'active'];
        return (
          <div class={jsxStyle} key={index} onClick={() => this.handleTabClick(index)}>
            {item}
          </div>
        );
      });
      return jsx;
    },
    renderContent() {
      const componentMap = {
        工程信息: (
          <ProjectInfo responsibleList={this.contactList} propList={this.propList} type={this.pointData.type} />
        ),
        视频监控: <ProjectVideo code={this.pointData.code} />,
        // 工程巡查: <CustomIframe />,
        // 规划档案: <CustomIframe />,
        // 建设档案: <CustomIframe />,
        运行管理: <CustomIframe src={this.frameUrl} />,
        // 洪水预测: <FloodForecast />,
        运行曲线: <RunningInfo type={this.pointData.type} stationCode={this.pointData.stationCode} />
      };
      if (this.displayTabList[this.currentTab] == '工程信息' && this.contactList == []) {
        this.getContactList();
        this.getProjectInfo();
      }
      if (this.displayTabList[this.currentTab] == '运行管理') {
        this.frameUrl = `https://sk.zjwater.com/cp/skip/reservoirDetailSingle?prcd=${
          this.pointData.code
        }&name=${encodeURI(encodeURI(this.pointData.name))}&fromMap=1&pName=&cName=&token=${this.token}`;
        // `https://sk.zjwater.com/mgt/main?prcd=${this.pointData.code}&suspension_btn=true&token=${this.token}`;
      }
      return componentMap[this.displayTabList[this.currentTab]];
    },
    getContactList() {
      if (this.cardInitFlag) return;
      let contactList = [];
      reservoirRegulationApi.getThreeCharge(this.pointData.code || '').then((res) => {
        for (let charge of res.data) {
          let data = {
            title: charge.type,
            name: charge.username,
            position: charge.job,
            phone: charge.phone
          };
          contactList.push(data);
        }
        this.contactList = contactList;
      });
    },

    getProjectInfo() {
      if (this.cardInitFlag) return;
      const { type, code } = { ...this.pointData };
      hydraulicApi.getProjects({ type, code: code || '' }).then((res) => {
        let resProp = res.data[0],
          list = [];
        for (let item of displayPropsMap[type]) {
          list.push({ name: item.name, prop: item.prop, data: (resProp && resProp[item.prop]) || '-' });
        }
        this.propList = list;
      });
    },
    getDynamicToken() {
      hydraulicApi.getDynamicToken().then((res) => {
        if (res.code == 0) {
          this.frameUrl = res.data;
        }
      });
    }
  }
};
