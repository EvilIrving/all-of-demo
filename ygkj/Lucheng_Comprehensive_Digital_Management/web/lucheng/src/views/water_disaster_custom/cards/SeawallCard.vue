<!--
 * @Author: your name
 * @Date: 2021-06-30 18:17:27
 * @LastEditTime: 2022-09-21 15:14:28
 * @LastEditors: Please set LastEditors
 * @Description: 海塘小组件
 * @FilePath: \lucheng\src\views\water_disaster_custom\cards\SeawallCard.vue
-->
<template>
  <div class="seawall-card">
    <panel-item title="海塘" has-switch tc @tclick="tclick">
      <template #title-switch>
        <i
          title="模拟演练"
          :class="[seawallRangModule ? 'icon-moniyanlian-active' : 'icon-moniyanlian', 'icon-sty']"
          @click.stop="doDrill()"
        >
        </i>
        <i
          title="地图落点"
          @click.stop="handleSeawallChange('htbtn')"
          :class="[htbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
        />
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('海塘')"
        />
      </template>
      <el-carousel trigger="click" arrow="never" :autoplay="true" :interval="interval">
        <el-carousel-item class="carousel-item-content" v-if="!seawallRangModule">
          <div>
            <el-row>
              <el-col :span="24" class="hydrops-sty">
                <div id="seawallEchart" style="width: 232px; height: 100%"></div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" class="hydrops-legend">
                <div>
                  <div
                    v-for="(item, index) in seawallDataList"
                    :key="index"
                    style="cursor: pointer"
                    @click="focuSeawall(index)"
                  >
                    <p>
                      <i
                        style="vertical-align: middle"
                        :class="[
                          'icon-sty',
                          item.name == '100年以上'
                            ? 'icon-year-101'
                            : item.name == '100年一遇'
                            ? 'icon-year-100'
                            : item.name == '50年一遇'
                            ? 'icon-year-50'
                            : item.name == '20年一遇'
                            ? 'icon-year-20'
                            : item.name == '10年一遇'
                            ? 'icon-year-10'
                            : item.name == '非标准'
                            ? 'icon-fei-biao-zhun'
                            : 'icon-year-5'
                        ]"
                      />
                      <span>{{ item.name }}</span>
                    </p>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-carousel-item>

        <el-carousel-item class="carousel-item-content carousel-padding">
          <div class="seawall-count">
            <div class="seawall-count-tap">
              <div :class="{ active: activeTab == 0 }" @click="handleTabChange(0)">预报</div>
              <div :class="{ active: activeTab == 1 }" @click="handleTabChange(1)">监测</div>
            </div>
            <template v-if="activeTab == 0 || activeTab == 1">
              <div class="seawall-count-number">
                <div class="count-box">
                  <span class="font-middle">正常</span>
                  <span class="font-big">{{ normLength }}</span>
                  <span class="font-small">km</span>
                  <span class="font-big">{{ normNum }}</span>
                  <span class="font-small">段</span>
                </div>
                <div class="count-sub count-bottom">
                  <div v-for="(item, index) in countArr" :key="index">
                    <p>{{ item.color }}</p>
                    <p>
                      <span class="font-big">{{ item.long }}</span
                      >km
                    </p>
                    <p>
                      <span class="font-big">{{ item.part }}</span>
                      段
                    </p>
                  </div>
                </div>
              </div>
            </template>
          </div>
        </el-carousel-item>
      </el-carousel>
    </panel-item>
    <!-- 预演弹框 -->
    <primary-dialog
      mainTitle="预演"
      :hasAnalysis="false"
      :visible="previewDialog"
      @handleClose="handlePreviewClose"
      :appendToBody="true"
      width="400px"
    >
      <div class="dialog-content">
        <div class="content-box">
          <div class="search-row">
            <span>年份</span>
            <el-select size="small" v-model="typhoonYear" placeholder="年份" @change="typhoonTime">
              <el-option v-for="item in typhoonYears" :key="item.value" :label="item.value" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="search-row">
            <span>方案名称:</span>
            <el-select
              size="small"
              @change="typhoonChange"
              v-model="typhoonValue"
              placeholder="方案选择"
              style="color: #fff; font-size: 16px"
            >
              <el-option v-for="item in caseList" :key="item.id" :label="item.releaseTime" :value="item.id"></el-option>
            </el-select>
          </div>
          <div class="search-row">
            <span>方案描述:</span>
            <el-input disabled type="textarea" v-model="caseDescription"></el-input>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button class="cancel" @click="handlePreviewClose"> 取消 </el-button>
        <el-button class="confirm" @click="doPreview"> 确定 </el-button>
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'; // echart插件
import { PrimaryDialog } from '@/components';
import PanelItem from '../components/PanelItem.vue';
import { disasterPreventionApi } from '@/api/RightSildePanel';
import { pageTideFcst, listTileFcst } from '@/api/seawall';
import { alarmPeople } from '@/api/bigScreenPart';
import { mapGetters, mapActions } from 'vuex';
import { goAMap } from './utils/common';
export default {
  name: 'ReservoirCard',
  layerName: 'focuLineLayer',
  legendName: '海塘',
  components: {
    PanelItem,
    PrimaryDialog
  },
  mounted() {
    this.$nextTick(() => {
      this.seawallProjStats(); //海塘数据接口
      this.getRightMapPoints('海塘');
      this._seawallWarnStatistic();
      this.timer = setInterval(() => {
        this.seawallProjStats(); //海塘数据接口
        this.getRightMapPoints('海塘');
        this._seawallWarnStatistic();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.map.removeLineLayer();
    this.map.removeFocusLineLayer();
    this.$emit('legend-change', '海塘', {});
    this.map.drawClusterPoint([], {}, `dryBrakeLayer`);
    this.map.drawClusterPoint([], {}, `wharfLayer`);
    this.map.drawClusterPoint([], {}, `intersectLayer`);
    this.map.drawClusterPoint([], {}, `bridgeLayer`);
    this.map.drawClusterPoint([], {}, `stakeLayer`);
    this.map.drawClusterPoint([], {}, `culvertLayer`);
    this.map.drawClusterPoint([], {}, `sluiceLayer`);
    this.map.drawClusterPoint([], {}, `pumpLayer`);
    // this.map.drawClusterPoint([], {}, `pipeLayer`);
    this.map.drawClusterPoint([], {}, `rainbowLayer`);
    this.map.drawClusterPoint([], {}, `roomLayer`);
    this.map.drawClusterPoint([], {}, `stationLayer`);
    this.map.drawClusterPoint([], {}, `watergateLayer`);
  },
  data() {
    return {
      timer: null,
      interval: 5000,
      //海塘数据
      seawallData: {
        totalLength: '477.2'
      },
      //海塘列表
      seawallList: [],
      htbtn: false, //海塘点位展示标记
      //海塘曲线图数据
      seawallDataList: [
        { value: 2, name: '100年以上' },
        { value: 13, name: '100年一遇' },
        { value: 54, name: '50年一遇' },
        { value: 42, name: '20年一遇' },
        { value: 19, name: '10年一遇' },
        { value: 0, name: '10年以下' }
        // { value: 0, name: "非标准" },
      ],
      activeTab: 0,
      normLength: 0,
      normNum: 0,
      countArr: [
        {
          color: '蓝色',
          long: 0,
          part: 0
        },
        {
          color: '黄色',
          long: 0,
          part: 0
        },
        {
          color: '橙色',
          long: 0,
          part: 0
        },
        {
          color: '红色',
          long: 0,
          part: 0
        }
      ],
      previewDialog: false,
      typhoonYears: [
        {
          value: '2021'
        },
        {
          value: '2020'
        },
        {
          value: '2019'
        },
        {
          value: '2016'
        },
        {
          value: '2013'
        },
        {
          value: '2009'
        },
        {
          value: '2008'
        },
        {
          value: '2007'
        },
        {
          value: '2006'
        },
        {
          value: '2005'
        },
        {
          value: '2004'
        },
        {
          value: '2003'
        }
      ],
      typhoonValue: '',
      typhoonYear: '',
      caseList: [],
      caseDescription: '',
      typhoonData: {}
    };
  },
  computed: {
    ...mapGetters([
      'ScreenMap',
      'currentSelectArea',
      'previewModule',
      'formatterCurrentTime',
      'SeawallDrillData',
      'typhoonVal',
      'seawallRangModule'
    ]),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  watch: {
    SeawallDrillData: {
      handler: function (val) {
        this.map.removeLineLayer();
        this.map.removeFocusLineLayer();
        this.$emit('legend-change', '海塘', {});
        let info = val;
        let arr = [
          {
            color: '蓝色',
            long: 0,
            part: 0
          },
          {
            color: '黄色',
            long: 0,
            part: 0
          },
          {
            color: '橙色',
            long: 0,
            part: 0
          },
          {
            color: '红色',
            long: 0,
            part: 0
          }
        ];
        arr[0].long = info.blueLength;
        arr[0].part = info.blueNum;
        arr[1].long = info.yellowLength;
        arr[1].part = info.yellowNum;
        arr[2].long = info.orangeLength;
        arr[2].part = info.orangeNum;
        arr[3].long = info.redLength;
        arr[3].part = info.redNum;
        this.countArr = arr;
        this.normLength = info.normLength;
        this.normNum = info.normNum;
        this.seawallHand(info.points);
      },
      deep: true
    },
    seawallRangModule: function (val) {
      if (!val) {
        this.map.removeLineLayer();
        this.map.removeFocusLineLayer();
        this.$emit('legend-change', '海塘', {});
        this.$nextTick(() => {
          this.seawallEchart();
        });
      } else {
        this.htbtn = false;
      }
    }
  },
  methods: {
    ...mapActions(['setTyphoonVal']),
    seawallHand(dataList) {
      for (let i = 0; i < dataList.length; i++) {
        let seawall = dataList[i];
        let color = '#1CFFF5';
        if (seawall.tdz) {
          if (seawall.tdz < seawall.tideStationBlueLevel) {
            color = '#1CFFF5';
          } else if (seawall.tdz >= seawall.tideStationBlueLevel && seawall.tdz < seawall.tideStationYellowLevel) {
            color = '#008FE8';
          } else if (seawall.tdz >= seawall.tideStationYellowLevel && seawall.tdz < seawall.tideStationOrangeLevel) {
            color = '#E99100';
          } else if (seawall.tdz >= seawall.tideStationOrangeLevel && seawall.tdz < seawall.tideStationRedLevel) {
            color = '#FF551F';
          } else if (seawall.tdz >= seawall.tideStationRedLevel) {
            color = '#E80000';
          }
        }
        let style = {
          lineWidth: 4,
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
              name: seawall.seawallName,
              code: seawall.seawallCode,
              props: {
                海塘长度: seawall.seawallLength + 'm',
                防潮标准: seawall.preventTideStandard + '年',
                所在位置: seawall.village
              },
              ...seawall
            }
          };
        });
        // if (this.activeTab != 2) {
        //   this.$emit('legend-change', '海塘', {
        //     label: '海塘',
        //     children: [
        //       {
        //         label: '正常',
        //         icon: 'seawall-type-101'
        //       },
        //       {
        //         label: '蓝色警戒',
        //         icon: 'seawall-type-102'
        //       },
        //       {
        //         label: '黄色警戒',
        //         icon: 'seawall-type-20'
        //       },
        //       {
        //         label: '橙色警戒',
        //         icon: 'seawall-type-103'
        //       },
        //       {
        //         label: '红色警戒',
        //         icon: 'seawall-type-1'
        //       }
        //     ]
        //   });
        // } else {
        //   this.$emit('legend-change', '海塘', {
        //     label: '海塘',
        //     children: [
        //       {
        //         label: '正常',
        //         icon: 'seawall-type-101'
        //       },
        //       {
        //         label: '报警',
        //         icon: 'seawall-type-20'
        //       }
        //     ]
        //   });
        // }
        this.map.drawLineEx(lineList, style);
      }
    },
    typhoonTime() {
      this.getPageTideFcst();
    },
    typhoonChange(val) {
      const arr = this.caseList.filter((item) => {
        return item.id == val;
      });
      this.caseDescription = arr[0].caseDescription || '';
      this.typhoonData = arr.length > 0 ? arr[0] : {};
    },
    async startDrillAffair(id) {
      return new Promise(async (resolve) => {
        let res = await listTileFcst(id);
        this.setSeawallData(res.data);
        resolve(res.data);
      });
      // console.log(this.AffairId,222)
      // console.log(res);
    },
    doPreview() {
      if (this.typhoonValue) {
        this.setTyphoonVal(this.typhoonValue);
        this.startDrillAffair(this.typhoonValue).then((res) => {
          this.changeSeawallRangModule(true);
          this.handlePreviewClose();
        });
      } else {
        this.$message({
          type: 'warning',
          message: '请选择要预演的方案'
        });
      }
    },
    async getPageTideFcst() {
      let res = await pageTideFcst({
        year: this.typhoonYear
      });
      if (res.code == 0) {
        this.caseList = res.data.list;
      }
    },
    // 模拟演练
    doDrill() {
      //全降雨演练分析开的时候 点击无效
      if (this.seawallRangModule) {
        this.changeSeawallRangModule(false);
      } else {
        this.previewDialog = true;
      }
    },
    handlePreviewClose() {
      this.previewDialog = false;
      this.typhoonYear = '';
      this.typhoonValue = '';
    },
    ...mapActions(['SetDetailDialogParam', 'changeSeawallRangModule', 'setSeawallData']),
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
    },
    // 获取第二页海塘数据
    async _seawallWarnStatistic() {
      await alarmPeople.seawallWarning({ drill: false, formList: null }).then((res) => {
        if (res.code == 0) {
          let info = res.data;
          this.countArr[0].long = info.blue;
          this.countArr[0].part = info.blueLen;
          this.countArr[1].long = info.yellow;
          this.countArr[1].part = info.yellowLen;
          this.countArr[2].long = info.orange;
          this.countArr[2].part = info.orangeLen;
          this.countArr[3].long = info.red;
          this.countArr[3].part = info.red;
          this.normLength = info.zcLen ? info.zcLen : 44.3;
          this.normNum = info.zc;
        }
      });
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
      this.SetDetailDialogParam({
        mediumSized: '',
        codeTpye: val,
        analyseDialogShow: true
      });
    },
    /**
     * 跳转一张图
     */
    goToAMap(type) {
      goAMap(type);
    },
    /**
     * 点位list
     */
    async getRightMapPoints(type) {
      let opt = {
        type
      };
      let res = await disasterPreventionApi.rightMapPoints(opt);
      switch (type) {
        case '海塘':
          {
            this.seawallList = res.data;
          }
          break;
      }
    },
    handleTabChange(val) {
      this.activeTab = val;
      this._seawallWarnStatistic();
    },
    /**
     * 海塘数据接口请求
     */
    async seawallProjStats() {
      const typeMap = {
        notStandard: '非标准',
        lt10Year: '10年以下',
        eq10Year: '10年一遇',
        eq20Year: '20年一遇',
        eq50Year: '50年一遇',
        eq100Year: '100年一遇',
        gt100Year: '100年以上'
      };
      await disasterPreventionApi.seawallProjStats().then((res) => {
        if (res.code == 0) {
          this.seawallData = res.data;
          const arr = res.data.list.map((val) => {
            let obj = {
              value: val.partNum,
              partLength: val.partLength,
              lengthPercent: val.lengthPercent,
              name: typeMap[val.type]
            };
            return obj;
          });
          let data = arr.find((item) => item.name == '非标准');
          arr.unshift(data);
          arr.pop();
          this.seawallDataList = arr
          this.$nextTick(() => {
            this.seawallEchart();
          });
        }
      });
    },
    focuSeawall(index) {
      if (this.focuIndex == index) {
        this.focuIndex = -1;
        this.map.removeFocusLineLayer();
        return;
      }
      this.focuIndex = index;
      const typeMap = [
        (val) => val > 100,
        (val) => val == 100,
        (val) => val == 50,
        (val) => val == 20,
        (val) => val == 10,
        (val) => val < 10,
        (val) => val == null
      ];
      const colorMap = ['#32FFE0', '#2EE266', '#FFDF42', '#FFB335', '#FF4CFE', '#FF6A6A', '#BD0000'];
      let seawallList = this.seawallList.filter((item) => {
        return typeMap[index](item.designTideStandard);
      });
      let style = {
        lineWidth: 6,
        lineColor: colorMap[index]
      };
      this.map.removeFocusLineLayer();
      for (let seawall of seawallList) {
        let lineString = (seawall.coorpot && seawall.coorpot.replaceAll('&quot;', "'")) || '[]';
        let lineList = lineString && eval(lineString);
        lineList = lineList.map((line) => {
          line = line.map((item) => {
            return [item.lng, item.lat];
          });
          return {
            lineList: line,
            data: {
              type: '海塘',
              name: seawall.seawallName,
              props: {
                海塘长度: seawall.seawallLength + 'm',
                防潮标准: seawall.preventTideStandard + '年',
                所在位置: seawall.village
              }
            }
          };
        });
        this.map.drawLineFocus(lineList, style, 'focuLineLayer');
      }
    },
    /**
     * 海塘点位开关
     */
    handleSeawallChange(btnType) {
      if (this.seawallRangModule) {
        this.$message({
          type: 'warning',
          message: '请先关闭海塘预演'
        });
        return;
      }
      this[btnType] = !this[btnType];
      if (this[btnType]) {
        for (let i = 0; i < this.seawallList.length; i++) {
          let seawall = this.seawallList[i];
          let color = '#E80000';
          if (seawall.designTideStandard < 10) {
            color = '#E80000';
          } else if (seawall.designTideStandard == 10) {
            color = '#DD00DB';
          } else if (seawall.designTideStandard == 20) {
            color = '#E89100';
          } else if (seawall.designTideStandard == 50) {
            color = '#EEC80B';
          } else if (seawall.designTideStandard == 100) {
            color = '#19AF47';
          } else if (seawall.designTideStandard > 100) {
            color = '#4CEBC7';
          } else if (seawall.designTideStandard == null) {
            color = '#8D0A0A';
          }
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
                name: seawall.seawallName,
                code: seawall.seawallCode,
                props: {
                  海塘长度: seawall.seawallLength + 'm',
                  防潮标准: seawall.preventTideStandard + '年',
                  所在位置: seawall.village
                },
                ...seawall
              }
            };
          });
          this.$emit('legend-change', '海塘', {
            label: '海塘',
            children: [
              {
                label: '非标准',
                icon: 'seawall-type-none'
              },
              {
                label: '10年以下',
                icon: 'seawall-type-1'
              },
              {
                label: '10年一遇',
                icon: 'seawall-type-10'
              },
              {
                label: '20年一遇',
                icon: 'seawall-type-20'
              },
              {
                label: '50年一遇',
                icon: 'seawall-type-50'
              },
              {
                label: '100年一遇',
                icon: 'seawall-type-100'
              },
              {
                label: '100年以上',
                icon: 'seawall-type-101'
              }
            ]
          });
          this.map.drawLineEx(lineList, style);
        }
      } else {
        this.map.drawPoint([], {}, `dryBrakeLayer`);
        this.map.drawPoint([], {}, `wharfLayer`);
        this.map.drawPoint([], {}, `intersectLayer`);
        this.map.drawPoint([], {}, `bridgeLayer`);
        this.map.drawPoint([], {}, `stakeLayer`);
        this.map.drawClusterPoint([], {}, `culvertLayer`);
        this.map.drawClusterPoint([], {}, `sluiceLayer`);
        this.map.drawClusterPoint([], {}, `pumpLayer`);
        // this.map.drawClusterPoint([], {}, `pipeLayer`);
        this.map.drawClusterPoint([], {}, `rainbowLayer`);
        this.map.drawClusterPoint([], {}, `roomLayer`);
        this.map.drawClusterPoint([], {}, `stationLayer`);
        this.map.drawClusterPoint([], {}, `watergateLayer`);
        this.map.removeLineLayer();
        this.map.removeFocusLineLayer();
        this.$emit('legend-change', '海塘', {});
      }
    },
    /**
     * 海塘图表
     */
    seawallEchart() {
      var chartDom = document.getElementById('seawallEchart');
      var myChart = echarts.init(chartDom);
      var option;
      const color0 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          { offset: 0, color: '#8D0A0A' },
          { offset: 1, color: 'rgba(0,0,0,0.7)' }
        ],
        global: false
      };
      const color1 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          { offset: 0, color: '#E80000' },
          { offset: 1, color: 'rgba(0,0,0,0.7)' }
        ],
        global: false
      };
      const color2 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          { offset: 0, color: '#DE01DB' },
          { offset: 1, color: 'rgba(0,0,0,0.7)' }
        ],
        global: false
      };
      const color3 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          { offset: 0, color: '#E99100' },
          { offset: 1, color: 'rgba(0,0,0,0.7)' }
        ],
        global: false // 缺省为 false
      };
      const color4 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          { offset: 0, color: '#CCB227' },
          { offset: 1, color: 'rgba(0,0,0,0.7)' }
        ],
        global: false
      };
      const color5 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          { offset: 0, color: '#1AAF47' },
          { offset: 1, color: 'rgba(0,0,0,0.7)' }
        ],
        global: false
      };
      const color6 = {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          { offset: 0, color: '#4CECC7' },
          { offset: 1, color: 'rgba(0,0,0,0.7)' }
        ],
        global: false
      };
      option = {
        tooltip: {
          show: true,
          trigger: 'item',
          formatter: function (data) {
            return (
              data.name +
              "<br/><span style='font-size: 20px;color: #1CFFF5;line-height: 26px;'>" +
              data.data.partLength +
              "</span> <span  style='font-size: 14px;'> km</span>"
            );
          },
          borderWidth: 0,
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          confine: true,
          extraCssText: 'width:160px;font-size:16px; color: #ffffff'
        },
        series: [
          {
            type: 'pie',
            radius: ['60%', '90%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center',
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: 'center',
                color: '#fff',
                fontSize: 18,
                formatter: this.seawallData.totalLength + 'km\n' + '总长度' // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
              }
            },
            labelLine: {
              show: false
            },
            data: this.seawallDataList,
            color: [color0, color1, color2, color3, color4, color5, color6]
          }
        ]
      };
      option && myChart.setOption(option);
    }
  }
};
</script>

<style lang="scss" scoped>
@import './style/SildePanel.scss';
.seawall-card {
  height: 100%;
  // padding: 0 16px;
  ::v-deep.el-carousel {
    height: 100%;
    .el-carousel__container {
      height: 100%;
    }
  }
  .carousel-item-content {
    overflow-y: auto;
    &::-webkit-scrollbar {
      display: none;
    }
  }
  .seawall-count {
    padding-top: 12px;
    .seawall-count-tap {
      display: flex;
      margin-bottom: 12px;
      > div {
        font-weight: 400;
        font-size: 16px;
        width: 64px;
        height: 22px;
        line-height: 24px;
        text-align: center;
        border-radius: 22px;
        cursor: pointer;
        &.active {
          background: linear-gradient(180deg, #36c0e4 0%, #0639ff 100%);
        }
      }
    }
    .seawall-count-number {
      .count-top {
        width: 100%;
        height: 48px;
        text-align: center;
        line-height: 48px;
        font-size: 16px;
        color: #ffffff;
        background: rgba(0, 186, 255, 0.1);
        border-radius: 2px;
        border: 1px solid rgba(0, 186, 255, 0.54);
        span {
          color: #00baff;
          font-size: 20px;
          padding-left: 15px;
          padding-right: 6px;
          &:last-child {
            padding-left: 10px;
          }
        }
      }
    }
    .count-bottom {
      padding-bottom: 20px;
      display: flex;
      justify-content: space-between;
      > div {
        width: 84px;
        height: 140px;
        box-sizing: border-box;
        display: flex;
        flex-direction: column;
        text-align: center;
        justify-content: space-between;
        padding: 16px 0;
        span {
          font-size: 20px;
          padding: 0 12px 0 0;
        }
        &.blue {
          background: rgba(0, 143, 232, 0.1);
          border-radius: 2px;
          border: 1px solid rgba(0, 143, 232, 0.5);
          span {
            color: #008fe8;
          }
        }
        &.yellow {
          background: rgba(238, 200, 11, 0.1);
          border-radius: 2px;
          border: 1px solid rgba(238, 200, 11, 0.5);
          span {
            color: #eec80b;
          }
        }
        &.orange {
          background: rgba(255, 85, 31, 0.1);
          border-radius: 2px;
          border: 1px solid rgba(255, 85, 31, 0.5);
          span {
            color: #ff551f;
          }
        }
        &.red {
          background: rgba(232, 0, 0, 0.1);
          border-radius: 2px;
          border: 1px solid rgba(232, 0, 0, 0.5);
          span {
            color: #e80000;
          }
        }
      }
    }
  }
  .font-small {
    font-size: 14px;
  }
  .font-big {
    font-size: 28px;
  }
  .count-box {
    width: 100%;
    height: 48px;
    line-height: 48px;
    background: rgba(28, 255, 245, 0.1);
    border: 1px solid rgba(28, 255, 245, 0.5);
    border-radius: 2px;
    font-size: 16px;
    text-align: center;
    margin-top: 14px;
    margin-bottom: 12px;
    .font-big {
      margin: 0 6px;
      color: #1cfff5;
      font-size: 24px;
    }
    .font-middle {
      margin-right: 16px;
    }
  }
  .count-sub {
    display: flex;
    justify-content: space-between;
    height: calc(100% - 110px);
    text-align: center;
    > div {
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      width: calc((100% - 24px) / 4);
      margin-right: 8px;
      text-align: center;
      &:nth-child(1) {
        background: rgba(0, 143, 232, 0.1);
        border: 1px solid rgba(0, 143, 232, 0.5);
        border-radius: 2px;
        .font-big {
          color: #008fe8;
        }
      }
      &:nth-child(2) {
        background: rgba(238, 200, 11, 0.1);
        border: 1px solid rgba(238, 200, 11, 0.5);
        border-radius: 2px;
        .font-big {
          color: #eec80b;
        }
      }
      &:nth-child(3) {
        background: rgba(255, 85, 31, 0.1);
        border: 1px solid rgba(255, 85, 31, 0.5);
        border-radius: 2px;
        .font-big {
          color: #ff551f;
        }
      }
      &:nth-child(4) {
        background: rgba(232, 0, 0, 0.1);
        border: 1px solid rgba(232, 0, 0, 0.5);
        border-radius: 2px;
        margin-right: 0px;
        .font-big {
          color: #e80000;
        }
      }
    }
  }
  .hydrops-sty {
    display: flex;
    justify-content: center;
    height: 150px;
    color: #fff;
    font-size: 16px;
    font-weight: 400;
    > div {
      padding: 0 10px;
      > div {
        display: flex;
        padding: 5px;
        justify-content: space-between;
        > p:nth-child(2) {
          > span {
            color: #1cfff5;
          }
        }
      }
    }
  }
  .hydrops-legend {
    width: 100%;
    padding-bottom: 10px;
    > div {
      display: flex;
      flex-wrap: wrap;
      width: 100%;
      padding-left: 20px;
      > div {
        width: 33.33%;
        line-height: 25px;
        font-size: 16px;
        P {
          display: flex;
          align-items: center;
        }
      }
    }
  }
  .icon-sty {
    margin-right: 10px;
    &.icon-year-100 {
      @include chart-icon();
      background: #19af47;
    }
    &.icon-year-50 {
      @include chart-icon();
      background: #eec80b;
    }
    &.icon-year-20 {
      @include chart-icon();
      background: #e89100;
    }
    &.icon-year-10 {
      @include chart-icon();
      background: #dd00db;
    }
    &.icon-year-5 {
      @include chart-icon();
      background: #e80000;
    }
    &.risk-legend {
      width: 16px;
      height: 16px;
      border-radius: 50%;
      display: inline-block;
      @include chart-icon();
    }
  }
  .background-sty {
    background: url(~assets/images/WaterDisastersImage/bai-jing-style.png) no-repeat top center;
    background-size: 100% 100%;
  }
  .sea-level-style {
    border-top: 1px solid rgba($color: #00c1ff, $alpha: 0.5) !important;
    border-bottom: 1px solid rgba($color: #00c1ff, $alpha: 0.5) !important;
  }
  .sea-left-level-style {
    border-left: 1px solid rgba($color: #00c1ff, $alpha: 0.5) !important;
    border-right: 1px solid rgba($color: #00c1ff, $alpha: 0.5) !important;
  }
}
.dialog-content {
  padding: 8px 0 16px;
  .content-box {
    // padding-top: 8px;
    .search-row {
      padding-top: 12px;
      @include flexbox;
      @include flexAC;
      @include flexflow(row, nowrap);
      span {
        display: inline-block;
        width: 100px;
        text-align: right;
        padding-right: 12px;
        color: #fff;
      }
      ::v-deep .el-select {
        width: 100%;
        border-radius: 4px;
        border: 1px solid #ffffff;
      }
      ::v-deep .el-textarea {
        .el-textarea__inner {
          background: rgba(0, 0, 0, 0);
          border: 1px solid #fff;
          border-radius: 4px;
          color: #fff;
        }
      }
    }
  }
}
</style>
<style lang="scss">
.seawall-card .panel-item .panel-item__content--tabs .tab-button,
.seawall-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.seawall-card .panel-item .panel-item__content--tabs .tab-button.active,
.seawall-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
