<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2022-04-07 15:28:23
 * @LastEditors: Please set LastEditors
 * @Description: 取水户落点弹窗
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\WaterIntakeDialog.vue
-->
<template>
  <div class="detail-table">
    <div v-if="pointData.projectType === 'waterQualityAbnormal'">
      <span
        :class="{ 'tab-switch': tabSwitch == '高锰酸盐指数' }"
        class="tab-switch-sty"
        @click="tabSwitchClick('高锰酸盐指数')"
        >高锰酸盐指数</span
      >
      <span class="tab-switch-sty" :class="{ 'tab-switch': tabSwitch == '氨氮' }" @click="tabSwitchClick('氨氮')"
        >氨氮</span
      >
      <span class="tab-switch-sty" :class="{ 'tab-switch': tabSwitch == '总磷' }" @click="tabSwitchClick('总磷')"
        >总磷</span
      >
      <span class="tab-switch-sty" :class="{ 'tab-switch': tabSwitch == '溶解氧' }" @click="tabSwitchClick('溶解氧')"
        >溶解氧</span
      >
    </div>
    <div v-if="pointData.projectType === 'sourceOfWater'">
      <span
        :class="{ 'tab-switch': tabSwitch == '基本信息' }"
        class="tab-switch-sty"
        @click="tabSwitchClick('基本信息')"
        >基本信息</span
      >
      <span
        class="tab-switch-sty"
        :class="{ 'tab-switch': tabSwitch == '标识标牌' }"
        @click="tabSwitchClick('标识标牌')"
        >标识标牌</span
      >
    </div>
    <div
      class="water-content-box"
      v-if="
        pointData.projectType === 'waterQualityAbnormal' ||
        pointData.projectType === 'hydropowerStationLayer' ||
        pointData.projectType === 'regimenLayer'
      "
    >
      <div class="water-left">
        <div class="water-content-title">
          <img src="~assets/images/diamonds.png" alt="" />
          <span>{{ title }}</span>
        </div>
        <div class="curve_line">
          <ve-line
            v-if="chartWaterData.rows.length > 0"
            :extend="chartWaterExtend"
            :colors="waterColors"
            :data="chartWaterData"
            :settings="chartWaterSettings"
            height="100%"
            width="100%"
            :grid="waterGrid"
            :mark-line="waterMarkLine"
          />
          <div v-else style="color: rgba(255, 255, 255, 0.8); text-align: center; margin-top: 150px">暂无数据</div>
        </div>
      </div>
      <div class="water-right">
        <div class="water-content-title">
          <img src="~assets/images/diamonds.png" alt="" />
          <span>列表</span>
        </div>
        <div class="table-box water-table-box">
          <primary-map-table :columnList="columnList" :dataList="dataList"></primary-map-table>
        </div>
      </div>
    </div>
    <div class="sourceOfWater" v-if="pointData.projectType === 'sourceOfWater'">
      <div v-show="tabSwitch == '基本信息'">
        <div class="table-list-box">
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">水源地名称</div>
              <div class="value-item">{{ pointData.swhsName }}</div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">类型</div>
              <div class="value-item">{{ pointData.wainWasoTypeName }}</div>
            </div>
          </div>
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">规模</div>
              <div class="value-item">{{ pointData.supplyScaleName }}</div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">所在市县</div>
              <div class="value-item">{{ pointData.adnm }}</div>
            </div>
          </div>
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">是否划定保护区</div>
              <div class="value-item">{{ pointData.isPrtcAr }}</div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">目标水质</div>
              <div class="value-item">{{ pointData.waquGoal }}</div>
            </div>
          </div>
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">水量预警</div>
              <div class="value-item" :class="pointData.warnType != 1 ? `color_${pointData.warnType}` : ''">
                {{ pointData.warnType | warnType }}
              </div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">编码</div>
              <div class="value-item">{{ pointData.swhsLoc }}</div>
            </div>
          </div>
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">最大日供水能力</div>
              <div class="value-item">{{ pointData.dateMaxWat }}</div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">服务人口</div>
              <div class="value-item">{{ pointData.desAnnWasuPop }}</div>
            </div>
          </div>
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">负责人</div>
              <div class="value-item">{{ pointData.master }}</div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">电话</div>
              <div class="value-item">{{ pointData.masterPhone }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="source-of-water-box" v-show="tabSwitch == '标识标牌'">
        <primary-map-table :columnList="tableColumns" :dataList="pointData.singageList" :paginationFlag="false">
          <template v-slot:imgUrls>
            <span class="el-icon-paperclip el-span-box"></span>
          </template>
        </primary-map-table>
      </div>
    </div>
    <div v-if="pointData.projectType === 'waterWorks'">
      <div>
        <div class="water-content-title">
          <img src="~assets/images/diamonds.png" alt="" />
          <span>基本信息</span>
        </div>
        <div class="table-list-box">
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">水厂（站）名称</div>
              <div class="value-item">{{ pointData.cwsName }}</div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">所在市县</div>
              <div class="value-item">{{ pointData.adnm }}</div>
            </div>
          </div>
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">水厂（站）位置</div>
              <div class="value-item">{{ pointData.cwsLoc }}</div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">完工时间</div>
              <div class="value-item">{{ pointData.modifyTime }}</div>
            </div>
          </div>
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">设计供水规模(吨/天)</div>
              <div class="value-item">{{ pointData.desWasuScal }}</div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">覆盖人口（人）</div>
              <div class="value-item">{{ pointData.population }}</div>
            </div>
          </div>
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">实际供水规模(吨/天)</div>
              <div class="value-item">{{ pointData.actWasuScal }}</div>
            </div>
            <!-- <div class="label-value-item">
              <div class="lable-item">处理工艺</div>
              <div class="value-item">{{ pointData.treProc }}</div>
            </div> -->
          </div>
          <!-- <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">消毒工艺</div>
              <div class="value-item">{{ pointData.dfcProc }}</div>
            </div>
          </div> -->
        </div>
      </div>
      <div>
        <div class="water-content-title">
          <img src="~assets/images/diamonds.png" alt="" />
          <span>水源地信息</span>
        </div>
        <div class="table-list-box">
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">主水源名称</div>
              <div class="value-item">{{ pointData.waterSource }}</div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">水源类型</div>
              <div class="value-item">{{ pointData.watersupplytype }}</div>
            </div>
          </div>
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">水源地规模</div>
              <div class="value-item">{{ pointData.waterSourceScale }}</div>
            </div>
          </div>
        </div>
      </div>
      <div>
        <div class="water-content-title">
          <img src="~assets/images/diamonds.png" alt="" />
          <span>运行管理单位</span>
        </div>
        <div class="table-list-box">
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">名称</div>
              <div class="value-item">{{ pointData.managerunitname }}</div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">地址</div>
              <div class="value-item">{{ pointData.managerunitAddress }}</div>
            </div>
          </div>
          <div class="table-list-item">
            <div class="label-value-item">
              <div class="lable-item">责任人</div>
              <div class="value-item">{{ pointData.dutyPerson }}</div>
            </div>
            <div class="label-value-item">
              <div class="lable-item">责任人联系方式</div>
              <div class="value-item">{{ pointData.dutyPersonPhone }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { waterResourceApi } from '@/api/waterResourcesGuarantee';
import { PrimaryMapTable } from '@/components';
import { hydraulicApi } from '@/api/hydraulic_amap/index.js';
import { getSeawallWaterLevels } from '@/api/seawall.js';
export default {
  name: 'WaterIntakeDialog',
  data() {
    this.chartWaterExtend = {
      legend: {
        icon: 'rect',
        itemWidth: 8,
        itemHeight: 4,
        right: 0,
        bottom: 0,
        textStyle: {
          //图例文字的样式
          color: '#FFF',
          fontSize: 12
        }
      },
      'xAxis.0.axisLabel.color': '#fff',
      'yAxis.0.axisLabel.color': '#fff'
    };
    this.chartWaterSettings = {
      showLine: ['水位曲线']
    };
    this.waterGrid = {
      top: 20,
      bottom: 30,
      left: 0,
      right: 0
    };
    this.waterMarkLine = {
      data: [
        {
          yAxis: 30
        }
      ],
      lineStyle: {
        normal: {
          color: 'red' // 这儿设置安全基线颜色
        }
      },
      label: {
        normal: {
          position: 'middle',
          formatter: '超汛限水位' // 这儿设置安全基线
        }
      }
    };
    return {
      columnList: [
        {
          prop: 'tm',
          label: '时间'
        },
        {
          prop: 'rz',
          label: '水位(m³/s)'
        }
      ],
      dataList: [],
      waterColors: ['#32BCB4', '#19af47', 'red'],
      chartWaterData: {
        columns: ['时间', '潮位'],
        rows: []
      },
      title: '24小时流量曲线',
      tabSwitch: '高锰酸盐指数',
      qualityCurveData: [], //水质数据
      tableColumns: [
        {
          prop: 'signageName',
          label: '名称'
        },
        {
          prop: 'imgUrls',
          label: '相关图片',
          slot: true,
        }
      ],
      //////////////////////////////////
    };
  },
  filters: {
    warnType: function (val) {
      switch (val) {
        case '2':
          return '黄色预警';
        case '3':
          return '橙色预警';
        case '4':
          return '红色预警';
        default:
          return '暂无风险';
      }
    }
  },
  props: {
    pointData: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  watch: {
    pointData: {
      handler: function (val) {
        if (val) {
          this.init();
        }
      },
      deep: true
    }
  },
  components: {
    PrimaryMapTable
  },
  mounted() {
    this.init();
  },
  computed: {},
  methods: {
    tabSwitchClick(type) {
      this.tabSwitch = type;
      this.qualityCurve();
    },
    //水质数据接口
    async qualityCurveList() {
      let opt = {
        month: '12',
        siteNo: this.pointData.siteNo
      };
      let res = await hydraulicApi.qualityCurve(opt);
      if (res.code == 0) {
        this.qualityCurveData = res.data;
        this.qualityCurve();
      }
    },
    // 水质类型切换
    async qualityCurve() {
      this.waterMarkLine = {};
      this.dataList = [...this.qualityCurveData].reverse();
      this.waterColors = ['#47F5A7', '#FFFF00', '#f06058'];
      if (this.tabSwitch == '高锰酸盐指数') {
        this.columnList = [
          {
            prop: 'tm',
            label: '时间'
          },
          {
            prop: 'codmn',
            label: '高锰酸盐指数'
          }
        ];
        let info = this.qualityCurveData.map((item) => {
          item['时间'] = item.tm;
          item['高锰酸盐指数'] = item.codmn;
          return item;
        });
        this.chartWaterData.columns = ['时间', '高锰酸盐指数'];
        this.chartWaterData.rows = info;
      } else if (this.tabSwitch == '氨氮') {
        this.columnList = [
          {
            prop: 'tm',
            label: '时间'
          },
          {
            prop: 'nh3N',
            label: '氨氮'
          }
        ];
        let info = this.qualityCurveData.map((item) => {
          item['时间'] = item.tm;
          item['氨氮'] = item.nh3N;
          return item;
        });
        this.chartWaterData.columns = ['时间', '氨氮'];
        this.chartWaterData.rows = info;
      } else if (this.tabSwitch == '总磷') {
        this.columnList = [
          {
            prop: 'tm',
            label: '时间'
          },
          {
            prop: 'tp',
            label: '总磷'
          }
        ];
        let info = this.qualityCurveData.map((item) => {
          item['时间'] = item.tm;
          item['总磷'] = item.tp;
          return item;
        });
        this.chartWaterData.columns = ['时间', '总磷'];
        this.chartWaterData.rows = info;
      } else if (this.tabSwitch == '溶解氧') {
        this.columnList = [
          {
            prop: 'tm',
            label: '时间'
          },
          {
            prop: 'do',
            label: '溶解氧'
          }
        ];
        let info = this.qualityCurveData.map((item) => {
          item['时间'] = item.tm;
          item['溶解氧'] = item.do;
          return item;
        });
        this.chartWaterData.columns = ['时间', '溶解氧'];
        this.chartWaterData.rows = info;
      }
    },
    // 水电站
    async getFlowCurve() {
      let opt = {
        hystCode: this.pointData.hystCode
      };
      let res = await waterResourceApi.getFlowCurve(opt);
      if (res.code == 0) {
        this.waterColors = ['#23E3FB', '#FFFF00', '#f06058'];
        this.dataList = [...res.data].reverse();
        this.chartWaterData.columns = ['时间', '实时水位'];
        let info = res.data.map((item) => {
          item['时间'] = item.staTime.split(' ')[1];
          item['实时水位'] = item.maxFlow;
          return item;
        });
        this.chartWaterData.rows = info;
      }
    },
    // 水位
    getWaterLevels() {
      getSeawallWaterLevels({
        stationCode: this.pointData.stCode,
        stationType: this.pointData.stType,
        days: 1
      }).then((res) => {
        let reverseList = res.data.data.map((item) => {
          item.rz = item.rz || item.z || item.tdz || item.upz;
          return item;
        });
        this.waterColors = ['#00BAFF', '#FF4A1A', '#FF4A1A'];
        this.waterMarkLine = {};
        this.dataList = [...res.data.data].reverse();
        this.chartWaterData.columns = ['时间', '实时水位', '警戒水位'];
        let info = res.data.data.map((item) => {
          item['时间'] = item.tm.split(' ')[1];
          item['实时水位'] = item.rz || item.z || item.tdz || item.upz;
          item['警戒水位'] = res.data.warningLevel;
          return item;
        });
        this.chartWaterData.rows = info;
      });
    },
    init() {
      if (this.pointData.projectType === 'hydropowerStationLayer') {
        this.title = '24小时流量曲线';
        this.columnList = [
          {
            prop: 'staTime',
            label: '时间'
          },
          {
            prop: 'maxFlow',
            label: '水位(m³/s)'
          }
        ];
        this.chartWaterData.rows = [];
        this.getFlowCurve();
      } else if (this.pointData.projectType === 'regimenLayer') {
        this.title = '24小时水位曲线';
        this.columnList = [
          {
            prop: 'tm',
            label: '时间'
          },
          {
            prop: 'rz',
            label: '水位(m)'
          }
        ];
        this.chartWaterData.rows = [];
        this.getWaterLevels();
      } else if (this.pointData.projectType === 'waterQualityAbnormal') {
        this.title = '水质曲线';
        this.chartWaterData.rows = [];
        this.qualityCurveList();
        this.tabSwitch = '高锰酸盐指数';
      } else if (this.pointData.projectType === 'sourceOfWater') {
        this.tabSwitch = '基本信息';
        console.log(this.pointData, 'sadasdasdsadasdasdsadsadsad');
      }
    },
  }
};
</script>
<style lang="scss" scoped>
.detail-table {
  min-height: 500px;
  padding-top: 20px;
  box-sizing: border-box;
  .tab-switch-sty {
    width: 112px;
    margin: 0 15px 35px 0;
    color: #fff;
    height: 30px;
    line-height: 30px;
    border: 1px solid #fff;
    display: inline-block;
    vertical-align: top;
    text-align: center;
    cursor: pointer;
    &.tab-switch {
      color: #00baff;
      background: rgba(72, 152, 251, 0.1);
      box-shadow: inset 0px 0px 5px 0px #00baff;
      border: 1px solid #00baff;
    }
  }
  .water-content-title {
    display: flex;
    align-items: center;
    position: relative;
    margin-bottom: 32px;
    span {
      color: #fff;
    }
    img {
      margin-right: 10px;
    }
    &::after {
      content: '';
      position: absolute;
      bottom: -8px;
      left: 0;
      width: 100%;
      height: 4px;
      background: url(~assets/images/long_line.png) no-repeat;
      background-size: 100% 100%;
    }
  }
  .water-content-box {
    display: flex;
    justify-content: space-between;

    .table-box {
      height: 380px;
    }
    .water-left {
      width: 804px;
      .curve_line {
        height: calc(100% - 60px);
      }
    }
    .water-right {
      width: calc(100% - 820px);
    }
  }
  .sourceOfWater {
    > div {
      height: 380px;
      .el-span-box {
        color: #00baff;
      }
    }
  }
  .table-list-box {
    border: 1px solid rgba(0, 186, 255, 0.6);
    margin-bottom: 25px;
    .table-list-item {
      display: flex;
      border-bottom: 1px solid rgba(0, 186, 255, 0.6);
      .label-value-item {
        flex: 1;
        display: flex;
        border-right: 1px solid rgba(0, 186, 255, 0.6);
        .lable-item {
          width: 160px;
          color: #00baff;
          background: linear-gradient(90deg, rgba(0, 122, 255, 0.3) 0%, rgba(0, 122, 255, 0) 100%);
          text-align: center;
          padding: 14px 0;
          border-right: 1px solid rgba(0, 186, 255, 0.6);
        }
        .value-item {
          width: calc(100% - 161px);
          padding: 14px 16px;
          box-sizing: border-box;
          color: #fff;
          &.color_2 {
            color: #fcee27;
          }
          &.color_3 {
            color: orange;
          }
          &.color_4 {
            color: #ff0000;
          }
        }
        &:last-child {
          border-right: none;
        }
      }
      &:last-child {
        border-bottom: none;
      }
    }
  }
}
</style>

<style lang="scss">
.source-of-water-box .table-wrap .el-table .table-header th {
  background: rgba(8, 38, 57, 0.6);
  color: #00baff;
}
</style>