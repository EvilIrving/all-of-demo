<!--
 * @Author: wpf
 * @Date: 2021-07-15 09:47:17
 * @LastEditTime: 2022-09-22 15:19:11
 * @LastEditors: Please set LastEditors
 * @Description: 防汛风险 卡片
 * @FilePath: \lucheng\src\views\water_disaster_custom\cards\HydropowerStationCard.vue
-->
<template>
  <div class="water-quality-card">
    <panel-item title="防汛风险" has-switch tc @tclick="tclick('防汛风险')">
      <template #title-switch>
        <i
          title="地图落点"
          @click="drawPoint('ldbtn')"
          style="margin-right: 5px"
          :class="[ldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
        />
      </template>
      <div class="water-content-box">
        <div class="water-content-item">
          <div class="water-content-title">
            <span v-if="showDetail_a">防汛超警统计</span>
            <span v-else>{{ superTitle }}</span>
          </div>
          <div class="histogram-box">
            <div class="makeapie-box" v-if="showDetail_a">
              <div>
                <makeapie />
              </div>
              <div class="makeapie-length">
                <div class="suiku" @click="checktable('水库超汛数量', '水库')">
                  <div></div>
                  <div>
                    水库超汛数量<span>{{ waterCount.skCount }}</span
                    >个
                  </div>
                </div>
                <div class="suizha" @click="checktable('水闸超警数量', '水闸')">
                  <div></div>
                  <div>
                    水闸超警数量<span>{{ waterCount.szCount }}</span
                    >个
                  </div>
                </div>
                <div class="difang" @click="checktable('堤防超警数量', '堤防')">
                  <div></div>
                  <div>
                    堤防超警数量<span>{{ waterCount.dfCount }}</span
                    >个
                  </div>
                </div>
              </div>
            </div>
            <div class="primary-map-table" v-else>
              <CapsuleTabs :tabs="['返回']" @change="showDetail_a = true" />
              <div class="is-water-box">
                <primary-map-table
                  :columnList="tableColumns"
                  :dataList="dataSeawallList"
                  :paginationFlag="false"
                ></primary-map-table>
              </div>
            </div>
          </div>
        </div>
        <div class="water-content-item">
          <div class="water-content-title">
            <span>防汛报警统计</span>
          </div>
          <div class="police-statistics">
            <div class="police-box" v-if="showDetail_b">
              <div @click="checkPolice('海塘报警')">
                <div class="police-icon warning-icon-01"></div>
                <div class="police-statistics-box">
                  <div>
                    <span class="statistics-a">{{ alarmCount.bj }}</span
                    >个
                  </div>
                  <div>海塘报警</div>
                </div>
              </div>
              <div @click="checkPolice('海塘预警')">
                <div class="police-icon warning-icon-02"></div>
                <div class="police-statistics-box">
                  <div>
                    <span class="statistics-b">{{ warnCount.bj }}</span
                    >个
                  </div>
                  <div>海塘预警</div>
                </div>
              </div>
              <div @click="checkPolice('位移报警')">
                <div class="police-icon warning-icon-03"></div>
                <div class="police-statistics-box">
                  <div>
                    <span>{{ displacementCount.length }}</span
                    >个
                  </div>
                  <div>位移报警</div>
                </div>
              </div>
              <div @click="checkPolice('沉降报警')">
                <div class="police-icon warning-icon-04"></div>
                <div class="police-statistics-box">
                  <div>
                    <span>{{ sedimentationCount.length }}</span
                    >个
                  </div>
                  <div>沉降报警</div>
                </div>
              </div>
            </div>
            <div class="primary-map-table" v-else>
              <CapsuleTabs :tabs="['返回']" @change="showDetail_b = true" />
              <div class="is-water-box">
                <primary-map-table
                  :columnList="tableColumns_b"
                  :dataList="dataSeawallList_b"
                  :paginationFlag="false"
                ></primary-map-table>
              </div>
            </div>
          </div>
        </div>
        <div class="water-content-item">
          <div class="water-content-title">
            <span>工程运行统计</span>
          </div>
          <div class="histogram-box">
            <vchart-bar
              v-if="showDetail_c"
              height="100%"
              width="100%"
              ref="ranksChart"
              :barData="barData"
              :barWidth="20"
              barName=""
              @barClick="clickBarData"
            ></vchart-bar>
            <div class="primary-map-table" v-else>
              <div class="flex-box">
                <CapsuleTabs :tabs="['返回']" @change="barCapsuClick" />
                <div>
                  <el-dropdown placement="bottom-start" trigger="click">
                    <span class="el-dropdown-link">
                      <span style="font-size: 1rem">{{ statusLabel }}</span>
                      <i class="el-icon-caret-bottom"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown" class="year-dropdown">
                      <el-dropdown-item v-for="(item, index) in statusOption" :key="index">
                        <el-radio v-model="status" :label="item.value" @change="statusChange">{{
                          item.label
                        }}</el-radio>
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
              </div>
              <div class="is-water-box">
                <primary-map-table
                  :columnList="tableColumns_c"
                  :dataList="dataSeawallList_c"
                  :paginationFlag="false"
                ></primary-map-table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </panel-item>
  </div>
</template>
<script>
import PanelItem from '../components/PanelItem.vue';
import SubTitleClickCard from '../components/SubTitleClickCard.vue';
import EchartInstrument from '../charts/echart_instrument/EchartInstrument.vue'; // 仪表盘图
import { waterResourceApi } from '@/api/waterResourcesGuarantee';
import VchartBar from '../charts/vchart_3dbar/VchartBar.vue';
import makeapie from '../charts/makeapie/makeapie.vue';
import CapsuleTabs from '@/views/WaterDisasterPreventionScreen/components/CapsuleTabs.vue';
import { PrimaryMapTable } from '@/components/';
import { cloneObj } from '@/utils/utils';
export default {
  name: 'HydropowerStationCard',
  layerName: ['workSluiceLayer', 'workReservoirLayer', 'workPumpLayer'],
  legendName: ['水库', '水闸', '堤防'],
  components: {
    PanelItem,
    SubTitleClickCard,
    EchartInstrument,
    VchartBar,
    makeapie,
    CapsuleTabs,
    PrimaryMapTable
  },
  data() {
    return {
      ldbtn: false, // 落点
      barData: {
        columns: ['name', 'data'],
        rows: [
          { name: '安全鉴\n定超期', data: 80 },
          { name: '汛前检\n查超期', data: 60 },
          { name: '年度总\n结超期', data: 50 },
          { name: '巡查问题', data: 70 },
          { name: '工程隐患', data: 90 }
        ]
      },
      tableColumns: [
        {
          prop: 'projectName',
          label: '名称'
        },
        {
          prop: 'waterLevel',
          label: '水位(m)'
        },
        {
          prop: 'limitLevel',
          label: '汛限水位(m)'
        }
      ],
      dataSeawallList: [],
      showDetail_a: true,
      superTitle: '',
      showDetail_b: true,
      tableColumns_b: [],
      dataSeawallList_b: [],
      showDetail_c: true,
      tableColumns_c: [
        {
          prop: 'projectName',
          label: '名称'
        },
        {
          prop: 'projectType',
          label: '工程类型'
        }
      ],
      dataSeawallList_c: [],
      status: '',
      statusOption: [
        { value: '', label: '全部类型' },
        { value: '海塘', label: '海塘' },
        { value: '水库', label: '水库' },
        { value: '水闸', label: '水闸' },
        { value: '堤防', label: '堤防' }
      ],
      statusLabel: '全部类型',
      projectType: '',
      waterCount: {},
      alarmCount: {},
      warnCount: {},
      displacementCount: [],
      sedimentationCount: [],
      proType: 0,
      sluiceList: [],
      reservoirList: [],
      dikeList: [],
      seawallList: [],
      seawallListData: []
    };
  },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  mounted() {
    this.waterLevelWarnCount();
    this.seawallGiveAnAlarm(1);
    this.seawallWarning();
    this.displacemen(1);
    this.displacemen(2);
    this.listRuntimeWarn(1);
    this.seawallGiveAnAlarm(2);
  },
  methods: {
    getList() {
      let typeList = ['水闸', '水库', '堤防'];
      typeList.forEach(async (type) => {
        let res = await waterResourceApi.waterLevelWarn({
          projectType: type
        });
        if (res.code === 0) {
          // RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道，SS墒情,PT泵站
          switch (type) {
            case '水闸':
              this.sluiceList = res.data.list.map((item) => {
                return {
                  name: item.projectName,
                  lat: parseFloat(item.latitude),
                  lng: parseFloat(item.longitude),
                  type: '防汛风险',
                  stCode: item.projectCode,
                  stcd: item.stcd,
                  stType: 'DD',
                  projectType: 'sluice',
                  props: {
                    水闸名称: item.projectName,
                    闸上水位: item.upz,
                    闸下水位: item.dwz,
                    过闸流量: item.ll,
                    闸门开度: item.ovs
                  },
                  ...item
                };
              });
              break;
            case '水库':
              this.reservoirList = res.data.list.map((item) => {
                return {
                  name: item.projectName,
                  lat: parseFloat(item.latitude),
                  lng: parseFloat(item.longitude),
                  type: '防汛风险',
                  stCode: item.projectCode,
                  stType: 'RR',
                  projectType: 'reservoir',
                  props: {
                    水库名称: item.projectName,
                    水位: item.waterLevel,
                    下泄流量: item.xxll
                  },
                  ...item
                };
              });
              break;
            default:
              console.log(res.data.list, 'dikeres.data.listres.data.list');
              this.dikeList = res.data.list.map((item) => {
                return {
                  name: item.projectName,
                  lat: parseFloat(item.latitude),
                  lng: parseFloat(item.longitude),
                  type: '防汛风险',
                  stCode: item.projectCode,
                  stType: 'DK',
                  projectType: 'dike',
                  props: {
                    堤防名称: item.projectName,
                    堤防规模: '',
                    '防洪标准(年)': '',
                    '长度(m)': ''
                  },
                  ...item
                };
              });
              break;
          }
        }
      });
    },
    checktable(title, projectType) {
      this.projectType = projectType;
      if (this.projectType == '水库') {
        this.tableColumns = [
          {
            prop: 'projectName',
            label: '名称'
          },
          {
            prop: 'waterLevel',
            label: '水位(m)'
          },
          {
            prop: 'limitLevel',
            label: '汛限水位(m)'
          }
        ];
      } else if (this.projectType == '水闸') {
        this.tableColumns = [
          {
            prop: 'projectName',
            label: '名称'
          },
          {
            prop: 'waterLevel',
            label: '闸上水位(m)'
          },
          {
            prop: 'controlTransportLevel',
            label: '控运水位(m)'
          }
        ];
      } else {
        this.tableColumns = [
          {
            prop: 'projectName',
            label: '名称'
          },
          {
            prop: 'waterLevel',
            label: '水位(m)'
          },
          {
            prop: 'limitLevel',
            label: '警戒水位(m)'
          }
        ];
      }
      this.superTitle = title;
      this.showDetail_a = false;
      this.waterLevelWarn();
    },
    checkPolice(type) {
      if (type == '海塘报警' || type == '海塘预警') {
        this.tableColumns_b = [
          {
            prop: 'seawallName',
            label: '海塘名称',
            width: 200
          },
          {
            prop: 'tdz',
            label: '潮位(m)'
          },
          {
            prop: 'tideStationBlueLevel',
            label: '蓝色警戒潮位(m)',
            width: 130
          },
          {
            prop: 'tm',
            label: '时间'
          }
        ];
        if (type == '海塘报警') {
          this.dataSeawallList_b = this.alarmCount.list;
          if(this.dataSeawallList_b.length > 0){
            this.seawallHand(this.dataSeawallList_b, false, '#FF9500');
          }
        } else {
          this.dataSeawallList_b = this.warnCount.list;
          if(this.dataSeawallList_b.length > 0){
            this.seawallHand(this.dataSeawallList_b, false, '#F3E627');
          }
        }
      } else if (type == '位移报警') {
        this.tableColumns_b = [
          {
            prop: 'gcmc',
            label: '海塘名称'
          },
          {
            prop: 'zzjg',
            label: '位移(mm)',
            width: 130
          },
          {
            prop: 'maxValue',
            label: '警戒上限位移(mm)',
            width: 130
          },
          {
            prop: 'minValue',
            label: '警戒下限位移(mm)',
            width: 130
          }
        ];
        this.dataSeawallList_b = this.displacementCount;
        let data = this.displacementCount.map((item) => {
          let itemData = this.seawallListData.find((element) => element.seawallCode == item.gcbm);
          return itemData;
        });
        this.seawallHand(data, false, '#E80000')
      } else {
        this.tableColumns_b = [
          {
            prop: 'gcmc',
            label: '海塘名称',
            width: 90
          },
          {
            prop: 'zzjg',
            label: '沉降(mm)',
            width: 130
          },
          {
            prop: 'maxValue',
            label: '警戒上限沉降(mm)',
            width: 130
          },
          {
            prop: 'minValue',
            label: '警戒下限沉降(mm)',
            width: 130
          }
        ];
        this.dataSeawallList_b = this.sedimentationCount;
        let data = this.sedimentationCount.map((item) => {
          let itemData = this.seawallListData.find((element) => element.seawallCode == item.gcbm);
          return itemData;
        });
        this.seawallHand(data, false, '#E80000')
      }
      this.showDetail_b = false;
    },
    clickBarData(data) {
      this.proType = data;
      this.showDetail_c = false;
      this.listRuntimeWarn();
    },
    statusChange(val) {
      let data = this.statusOption.find((item) => item.value == val);
      this.statusLabel = data.label;
      this.listRuntimeWarn();
    },
    barCapsuClick() {
      this.status = '';
      this.statusLabel = '全部类型';
      this.listRuntimeWarn();
      this.showDetail_c = true;
    },
    async waterLevelWarnCount() {
      let res = await waterResourceApi.waterLevelWarnCount();
      if (res.code == 0) {
        this.waterCount = res.data;
      }
    },
    async waterLevelWarn() {
      let res = await waterResourceApi.waterLevelWarn({
        projectType: this.projectType,
        warning: true
      });
      if (res.code == 0) {
        this.dataSeawallList = res.data.list;
        if (this.dataSeawallList.length > 0) {
          this.drawPointSuper(this.projectType, this.dataSeawallList);
        }
      }
    },
    async seawallGiveAnAlarm(type) {
      let res = await waterResourceApi.seawallGiveAnAlarm({ warning: type == 1 ? true : false });
      if (res.code == 0) {
        if(type == 1){
          this.alarmCount = res.data;
        }else{
          this.seawallListData = res.data.list
        }
      }
    },
    async seawallWarning() {
      let res = await waterResourceApi.seawallWarning({ warning: true });
      if (res.code == 0) {
        this.warnCount = res.data;
      }
    },
    async displacemen(jclx) {
      let res = await waterResourceApi.displacemen({
        jclx
      });
      if (res.code == 0) {
        if (jclx == 1) {
          this.displacementCount = res.data;
        } else {
          this.sedimentationCount = res.data;
        }
      }
    },
    async listRuntimeWarn() {
      let res = await waterResourceApi.listRuntimeWarn({
        projectType: this.status,
        isProjInfo: true
      });
      if (res.code == 0) {
        let safety = res.data.filter((item) => item.safety == 1);
        let flood = res.data.filter((item) => item.flood == 1);
        let yearReport = res.data.filter((item) => item.yearReport == 1);
        let patrol = res.data.filter((item) => item.patrol == 1);
        let danger = res.data.filter((item) => item.danger == 1);
        if (this.showDetail_c) {
          this.barData.rows = [
            { name: '安全鉴\n定超期', data: safety.length },
            { name: '汛前检\n查超期', data: flood.length },
            { name: '年度总\n结超期', data: yearReport.length },
            { name: '巡查问题', data: patrol.length },
            { name: '工程隐患', data: danger.length }
          ];
        } else {
          switch (this.proType) {
            case 0:
              this.dataSeawallList_c = safety;
              break;
            case 1:
              this.dataSeawallList_c = flood;
              break;
            case 2:
              this.dataSeawallList_c = yearReport;
              break;
            case 3:
              this.dataSeawallList_c = patrol;
              break;
            default:
              this.dataSeawallList_c = danger;
              break;
          }
          if (this.dataSeawallList_c.length > 0) {
            this.removeAllLayer();
            let reservoir = this.dataSeawallList_c.filter((item) => item.projectType == '水库');
            let sluice = this.dataSeawallList_c.filter((item) => item.projectType == '水闸');
            let dike = this.dataSeawallList_c.filter((item) => item.projectType == '堤防');
            let seawall = this.dataSeawallList_c.filter((item) => item.projectType == '海塘');
            switch (this.status) {
              case '水库':
                this.drawPointSuper('水库', reservoir, true, 'reservoirHydLayer');
                break;
              case '水闸':
                this.drawPointSuper('水闸', sluice, true, 'sluiceHydLayer');
                break;
              case '堤防':
                this.drawPointSuper('堤防', dike, true, 'dikeHydLayer');
                break;
              case '海塘':
                this.seawallHand(seawall, true);
                break;
              default:
                this.drawPointSuper('水库', reservoir, true, 'reservoirHydLayer');
                this.drawPointSuper('水闸', sluice, true, 'sluiceHydLayer');
                this.drawPointSuper('堤防', dike, true, 'dikeHydLayer');
                this.seawallHand(seawall, true);
                break;
            }
          }
        }
      }
    },
    // 落点
    drawPoint(btn) {
      if(this[btn]){
        this[btn] = false
        this.removeAllLayer()
      }
    },
    //水库落点
    drawPointSuper(type, arrList, flag, layerName = 'reservoirWarnLayer') {
      this.ldbtn = true
      if (!flag) {
        this.removeAllLayer();
      }
      let style = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: null,
        srcs: null
      };
      let projectType = '';
      if (type == '水库') {
        style.src = require(`@/assets/new_images/point/point-reservoir-warn.png`);
        style.srcs = require(`@/assets/new_images/point/group-reservoir-warn.png`);
        projectType = 'reservoir';
      } else if (type == '水闸') {
        style.src = require(`@/assets/new_images/point/point-sluice-warn.png`);
        style.srcs = require(`@/assets/new_images/point/group-sluice-warn.png`);
        projectType = 'sluice';
      } else {
        style.src = require(`@/assets/new_images/point/point-dike-warn.png`);
        style.srcs = require(`@/assets/new_images/point/group-dike-warn.png`);
        projectType = 'dike';
      }
      let list = arrList.map((item) => {
        let obj = {};
        if (type == '水库') {
          obj = {
            水库名称: item.projectName,
            '实时水位(m)': item.waterLevel,
            '汛限水位(m)': item.limitLevel,
            更新时间: item.tm,
            鉴定结论: item.safetyConclusion
          };
        } else if (type == '水闸') {
          obj = {
            水闸名称: item.projectName,
            '闸上水位(m)': item.waterLevel,
            '闸下水位(m)': item.dwz,
            '过闸流量(m³/s)': item.mxgtq,
            '闸门开度(m)': item.ovs
          };
        } else {
          obj = {
            堤防名称: item.projectName,
            堤防规模: item.waterLevel,
            '防洪标准(年)': item.dwz,
            '长度(m)': item.mxgtq
          };
        }
        return {
          ...item,
          // type: '概化图',
          type: '防汛风险',
          name: item.projectName,
          lng: parseFloat(item.longitude),
          lat: parseFloat(item.latitude),
          stType: 'RR',
          // stType: 'DD',
          stCode: item.stCode || '',
          projectType: projectType,
          projectCode: item.projectCode,
          props: obj
        };
      });
      this.map.drawClusterPoint(list, style, layerName);
    },
    seawallHand(list, flag, color = '#F3E627') {
      this.ldbtn = true
      if (!flag) {
        this.removeAllLayer();
      }
      for (let i = 0; i < list.length; i++) {
        let seawall = list[i];
        let style = {
          lineWidth: 6,
          lineColor: color
        };
        let lineString = (seawall.coorpot && seawall.coorpot.replace(/&quot;/g, "'")) || '[]';
        let lineList = lineString && eval(lineString);
        lineList = lineList.map((line) => {
          line = line.map((item) => {
            return [item.lng, item.lat];
          });
          return {
            lineList: line,
            data: {
              type: '海塘',
              name: seawall.seawallName || seawall.projectName,
              code: seawall.seawallCode || seawall.projectCode,
              projectType: 'seawall',
              isFxfx: true,
              props: {
                海塘长度: seawall.seawallLength ?seawall.seawallLength +'m':'-' ,
                防潮标准: seawall.preventTideStandard? seawall.preventTideStandard +'年':'-',
                所在位置: seawall.village
              },
              ...seawall
            }
          };
        });
        this.map.drawLineEx(lineList, style);
      }
    },
    removeAllLayer() {
      this.map.drawClusterPoint([], {}, `reservoirWarnLayer`);
      this.map.drawClusterPoint([], {}, `reservoirHydLayer`);
      this.map.drawClusterPoint([], {}, `sluiceHydLayer`);
      this.map.drawClusterPoint([], {}, `dikeHydLayer`);
      this.map.removeLineLayer();
      this.map.removeFocusLineLayer();
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
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
    },
    /**
     * @function: 设置点位样式
     * @description:
     * @param {*} projectType 点位类型
     * @param {*} value 点位图标后缀
     * @return {*}
     */
    getPointStyle(projectType, value) {
      let defaultStyle = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        srcs: null,
        src: null
      };
      let styleObj = cloneObj(defaultStyle);
      styleObj.src = require(`@/assets/images/MapPoint/point-${projectType}-${value}.png`);
      styleObj.srcs = require(`@/assets/images/MapPoint/group-${projectType}-${value}.png`);
      return styleObj;
    },
    /**
     * @function:
     * @description: 设置点位
     * @param {*} pointList 点位列表
     * @param {*} type  类型
     * @param {*} status  状态，配合类型用以设置图层名
     * @param {*} flag 是否设置样式
     * @return {*}
     */
    setPoint(pointList, type, status, flag) {
      let pointStyle = flag ? this.getPointStyle(type, status) : {};
      let layer = type + status;
      this.map.drawClusterPoint(pointList, pointStyle, `${layer}Layer`);
      // console.log(this.map,'map');
      // console.log(pointList, 'pointStyle');
    }
  },
  beforeDestroy(){
    this.removeAllLayer()
  }
};
</script>
<style lang="scss" scoped>
.water-content-box {
  margin-bottom: 20px;
  .water-content-item {
    padding-top: 14px;
    height: 235px;
    box-sizing: border-box;
    &:last-child {
      height: 240px;
    }
    .water-content-title {
      width: 100%;
      height: 50px;
      background: url(~assets/images/WaterDisastersImage/flood-title-icon.png) no-repeat;
      background-size: 100% 100%;
      box-sizing: border-box;
      padding: 7px 0 0 40px;
      span {
        font-size: 16px;
      }
    }
    .police-statistics {
      height: calc(100% - 50px);
      .police-box {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        padding: 17px 16px 0 7px;
        > div {
          display: flex;
          align-items: center;
          margin-bottom: 17px;
          &:last-of-type(2n) {
            margin-bottom: 0;
          }
          .police-icon {
            width: 64px;
            height: 64px;
            &.warning-icon-01 {
              background: url(~assets/images/WaterDisastersImage/warning-icon-01.png) no-repeat;
              background-size: 100% 100%;
            }
            &.warning-icon-02 {
              background: url(~assets/images/WaterDisastersImage/warning-icon-02.png) no-repeat;
              background-size: 100% 100%;
            }
            &.warning-icon-03 {
              background: url(~assets/images/WaterDisastersImage/warning-icon-03.png) no-repeat;
              background-size: 100% 100%;
            }
            &.warning-icon-04 {
              background: url(~assets/images/WaterDisastersImage/warning-icon-04.png) no-repeat;
              background-size: 100% 100%;
            }
          }
          .police-statistics-box {
            padding: 5px 0 0 8px;
            > div {
              font-size: 17px;
              color: rgba(255, 255, 255, 0.6);
              line-height: 24px;
              span {
                font-size: 29px;
                color: #ffffff;
                line-height: 31px;
                padding-right: 8px;
                &.statistics-a {
                  color: #ff9500;
                }
                &.statistics-b {
                  color: #f3e627;
                }
              }
            }
          }
        }
      }
    }
    .histogram-box {
      height: calc(100% - 50px);
      width: 100%;
      .makeapie-box {
        display: flex;
        position: relative;
        height: 100%;
        width: 100%;
        > div {
          &:first-child {
            width: 60%;
          }
          &.makeapie-length {
            width: 52%;
            position: absolute;
            top: 0;
            right: 0;
            padding-top: 30px;
            > div {
              display: flex;
              align-items: center;
              margin-bottom: 25px;
              cursor: pointer;
              > div:first-child {
                width: 14px;
                height: 14px;
                border-radius: 50%;
                margin-right: 10px;
              }
              > div:last-child {
                color: #ffffff;
                font-size: 14px;
                span {
                  font-size: 20px;
                  font-weight: 500;
                  padding: 0 5px 0 13px;
                }
              }
              &.suiku {
                > div:first-child {
                  background: #50f9c0;
                }
                > div:last-child {
                  span {
                    color: #50f9c0;
                  }
                }
              }
              &.suizha {
                > div:first-child {
                  background: #fbe057;
                }
                > div:last-child {
                  span {
                    color: #fbe057;
                  }
                }
              }
              &.difang {
                > div:first-child {
                  background: #45caff;
                }
                > div:last-child {
                  span {
                    color: #45caff;
                  }
                }
              }
            }
          }
        }
      }
    }
    .primary-map-table {
      width: 100%;
      height: 100%;
      .is-water-box {
        width: 100%;
        height: calc(100% - 45px);
      }
      .flex-box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .capsule-tabs {
          width: 50%;
        }
      }
    }
  }
}
.rank-1 {
  background-image: url(~assets/images/rank-1.png);
}
.rank-2 {
  background-image: url(~assets/images/rank-2.png);
}
.rank-3 {
  background-image: url(~assets/images/rank-3.png);
}

.icon-circle {
  width: 12px !important;
  height: 12px !important;
  border-radius: 50%;
}
.icon-color-1 {
  @include chart-icon();
  background: #008fe8;
}
.icon-color-2 {
  @include chart-icon();
  background: #00ebc7;
}
.icon-color-3 {
  @include chart-icon();
  background: #19af47;
}
.icon-color-4 {
  @include chart-icon();
  background: #eec80b;
}
.icon-color-5 {
  @include chart-icon();
  background: #e89100;
}
</style>

<style lang="scss">
.water-quality-card .panel-item .panel-item__content--tabs .tab-button,
.water-quality-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.water-quality-card .panel-item .panel-item__content--tabs .tab-button.active,
.water-quality-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>