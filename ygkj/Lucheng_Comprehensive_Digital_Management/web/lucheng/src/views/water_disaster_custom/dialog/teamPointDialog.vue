<template>
  <div class="detail-table">
    <DataDetailDialog :visible.sync="visible" :before-close="handleClose" class="detail-table" :title="pointData.name">
      <!-- 水情 -->
      <div v-if="title.indexOf('水情') !== -1" class="new_box">
        <div class="left">
          <div>
            <img src="../../../assets/images/diamonds.png" alt="" />
            <span style="color: #fff">24小时水位曲线</span>
          </div>
          <img style="width: 100%" src="../../../assets/images/long_line.png" alt="" />
          <div class="curve_line">
            <span v-show="showGateFlowText" style="color: #fff; float:right; white-space: nowrap">过闸流量 m³/s</span>
            <ve-line v-if="chartWaterData.rows.length > 0" :extend="chartWaterExtend" :colors="waterColors" :data="chartWaterData" :settings="chartWaterSettings" :height="'100%'" width="100%" :grid="waterGrid" :mark-line="waterMarkLine" />
            <div v-else style="color: rgba(255, 255, 255, 0.8); text-align: center; margin-top: 150px">暂无数据</div>
          </div>
        </div>
        <div class="right">
          <div>
            <img src="../../../assets/images/diamonds.png" alt="" />
            <span style="color: #fff">列表</span>
          </div>
          <img style="width: 100%" src="../../../assets/images/short_line.png" alt="" />
          <div class="table-box">
            <primary-map-table :columnList="watercolumnList" :dataList="dataList"></primary-map-table>
          </div>
        </div>
      </div>
      <!-- 工情 -->
      <div v-if="title.indexOf('工情') !== -1" class="new_box">
        <div class="left">
          <div>
            <img src="../../../assets/images/diamonds.png" alt="" />
            <span style="color: #fff">24小时水位曲线</span>
          </div>
          <img src="../../../assets/images/long_line.png" alt="" />
          <div class="curve_line">
            <span v-show="showGateFlowText" style="color: #fff; float:right; white-space: nowrap">过闸流量 m³/s</span>
            <ve-line v-if="chartWorkData.rows.length > 0" :extend="chartWaterExtend" :colors="waterColors" :data="chartWorkData" :settings="chartWaterSettings" :height="'100%'" width="100%" :grid="waterGrid" :mark-line="waterMarkLine" />
            <div v-else style="color: rgba(255, 255, 255, 0.8); text-align: center; margin-top: 150px">暂无数据</div>
          </div>
        </div>
        <div class="right">
          <div>
            <img src="../../../assets/images/diamonds.png" alt="" />
            <span style="color: #fff">列表</span>
          </div>
          <img src="../../../assets/images/short_line.png" alt="" />
          <div class="table-box">
            <primary-map-table :columnList="workcolumnList" :dataList="dataList"></primary-map-table>
          </div>
        </div>
      </div>
      <!-- 报警 -->
      <div v-if="title == '报警'" class="new_box">
        <div class="left">
          <div>
            <img src="../../../assets/images/diamonds.png" alt="" />
            <span>24小时水位曲线</span>
          </div>
          <img src="../../../assets/images/long_line.png" alt="" />
          <div class="bj_left_tooltip">
            <img src="../../../assets/images/blue_tool2.png" alt="" /><span class="one">实时水位</span>
            <img src="../../../assets/images/red_tool.png" alt="" /><span class="two">警戒线水位</span>
          </div>
          <div class="curve_line">
            <ve-line :extend="chartWaterExtend" :colors="waterColors" :data="chartWaterData" :settings="chartWaterSettings" :height="'100%'" width="100%" :grid="waterGrid" :mark-line="waterMarkLine" />
          </div>
        </div>
        <div class="right">
          <div class="">
            <img src="../../../assets/images/diamonds.png" alt="" />
            <span>六个责任人</span>
          </div>
          <img src="../../../assets/images/long_line.png" alt="" />
          <div class="duty-box flex-box">
            <template v-if="contactList.length">
              <div v-for="item in contactList" :key="item.id">
                <responsible-list-item :data="item" />
              </div>
            </template>
            <div v-else class="no-responsible">
              <div></div>
              <span>暂无责任人</span>
            </div>
          </div>
        </div>
      </div>
    </DataDetailDialog>
  </div>
</template>

<script>

import { PrimaryMapTable } from '@/components';
import { DataDetailDialog } from './DetailsCommonDialog/components';
import ResponsibleListItem from '@/components/screen_project_dialog/components/ResponsibleListItem.vue';
import { reservoirRegulationApi } from '@/api/dataScreen/ReservoirRegulation';
// import { WorkingConditionsApi } from '@/api/waterResourcesGuarantee';
import { waterAndRain } from '@/api/bigScreenPart';
import { log } from 'video.js';
const DEFAULT_COLOR = [
  "#00EBC7",
  "#E89100",
  "#FFEC36",
  "#008FE8",
  "#FF7144",
  "#2DDF3B",
  "#004FE3",
  "#DBA380",
  "#FF7D7D",
  "#A80BEE",
  "#EE0B6B",
  "#89BA8B",
  "#8A8AE1",
  "#28C9F6",
  "#19AF47",
  "#f1939c",
  "#e2e1e4",
  "#5cb3cc",
  "#2c9678",
];
const LESS_COLOR = ["#19AF47", "#008FE8", "#E89100", "#A80BEE", "#FFEC36"];
export default {
  name: 'dikePointDialog',
  data() {
    this.chartWaterExtend = {
      legend: {
        show: true,
        icon: 'rect',
        itemWidth: 8,
        itemHeight: 4,
        right: 0,
        top: 0,
        textStyle: {
          //图例文字的样式
          color: '#FFF',
          fontSize: 12
        }
      },
      grid: {
        top: 40,
        left: 5,
      },
      xAxis: {
        boundaryGap: false
      },
      series: [
        {
          type: 'line',
          symbol: 'circle',
          symbolSize: 1
        }
      ],
      'xAxis.0.axisLabel.color': '#fff',
      'yAxis.0.axisLabel.color': '#fff',
      'yAxis.1.axisLabel.color': '#fff',
    };
    this.chartWaterSettings = {
      showLine: ['水位曲线'],
      axisSite: { right: ['过闸流量'] },
      yAxisType: ['KMB', 'KMB'],
    };
    this.waterGrid = {
      top: 20,
      bottom: 30,
      left: 0,
      right: 0
    };
    return {
      showGateFlowText: false,
      contactList: [],
      waterColors: ["#19AF47", "#008FE8", "#E89100", "#A80BEE", "#FF4A1A"],
      watercolumnList: [
        {
          prop: 'tm',
          label: '时间',
          width: '160'
        },
        {
          prop: 'rz',
          label: '水位(m)'
        },
      ],
      workcolumnList: [],
      chartWaterData: {
        columns: ['时间', '实时水位'],
        rows: []
      },
      chartWorkData: {
        columns: ['时间', '进水池水位', '出水池水位', '流量'],
        rows: []
      },
      dataList: [],
      waterMarkLine: {
        data: [
          {
            yAxis: 0
          }
        ],
        lineStyle: {
          normal: {
            type: 'solid',
            color: '#FF4A1A' // 这儿设置安全基线颜色
          }
        },
        label: {
          normal: {
            position: 'middle',
            formatter: '警戒线水位' // 这儿设置安全基线
          }
        }
      },
      loading: false
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    pointData: Object,
    title: {
      type: String,
      default: '水情'
    }
  },
  components: {
    DataDetailDialog,
    ResponsibleListItem,
    PrimaryMapTable
  },
  created() { },
  mounted() {
    this.getRainStationInfo();
    this.getContactList();
  },
  computed: {},
  methods: {
    async getContactList() {
      let contactList = [];
      reservoirRegulationApi.getThreeCharge(this.pointData.code).then((res) => {
        if (res.data.length > 0) {
          for (let charge of res.data) {
            let data = {
              title: charge.type,
              name: charge.username,
              position: charge.job,
              phone: charge.phone
            };
            contactList.push(data);
          }
        }
        this.contactList = contactList;
      });
    },
    // 水情获取曲线数据和列表数据
    async getRainStationInfo() {
      this.loading = true;
      if (this.pointData.name === '龙湾水闸') {
        return;
      }
      let opt = {
        stationCode: this.pointData.code,
        stationType: this.pointData.stType,
        days: 1
      };
      waterAndRain.waterLevels(opt).then((res) => {
        
        if (res.code == 0) {
          this.loading = false;
          let dataArr = []
          let info = []
          if (this.title.indexOf('水情') !== -1) {
            dataArr = res.data.data.map((item) => {
              item.rz = item.rz || item.z || item.tdz;
              return item;
            });
          } else if (this.title.indexOf('工情') !== -1) {
            if (this.pointData.stType == 'DD') {
              this.workcolumnList = [
                {
                  prop: 'tm',
                  label: '时间'
                },
                {
                  prop: 'ovs',
                  label: '开度(m)'
                },
                {
                  prop: 'upz',
                  label: '闸上水位(m)'
                },
                {
                  prop: 'dwz',
                  label: '闸下水位(m)'
                },
                {
                  prop: 'll',
                  label: '过闸流量(m³/s)'
                }
              ];
            } else if (this.pointData.stType == 'RR') {
              this.workcolumnList = [
                {
                  prop: 'tm',
                  label: '时间',
                  width: '160'
                },
                {
                  prop: 'rz',
                  label: '水位(m)'
                },
              ];
            }
            dataArr = res.data.data
          }
          
          let arrs = JSON.parse(JSON.stringify(res.data.data)).reverse();
          if (res.data.astroTdz && res.data.astroTdz.length > 0) {
            let astroTdz = JSON.parse(JSON.stringify(res.data.astroTdz)).reverse();
            const arr = arrs.map((item) => {
              const str = astroTdz.find((i) => {
                if (item.tm == i.tm) {
                  return i;
                }
              });
              item.twc = str.tdz;
              return item;
            });
            info = arr;
          } else {
            if (this.title.indexOf('水情') !== -1) {
              info = res.data.data
            } else {
              info = res.data.data
            }
          }
          this.chartWaterData.columns = ['时间', '潮位', '天文潮', '警戒水位'];
          if (this.pointData.stType == 'DD') {
            this.showGateFlowText = info.some((v) => v.ll !== null)
            this.chartWorkData.columns = ['时间', '闸门开度', '闸上水位', '闸下水位', '过闸流量', '警戒水位'];
            this.chartWaterData.columns = ['时间', '闸门开度', '闸上水位', '闸下水位', '过闸流量', '警戒水位'];
            this.watercolumnList = [
              {
                prop: 'tm',
                label: '时间'
              },
              {
                prop: 'upz',
                label: '闸上水位(m)'
              },
              // {
              //   prop: 'dwz',
              //   label: '闸下水位(m)'
              // },
              // {
              //   prop: 'll',
              //   label: '过闸流量(m³/s)'
              // }
            ];
          } else if (this.pointData.stType == 'RR') {
            this.chartWorkData.columns = ['时间', '水位'];
          }
          info.map((item) => {
            item['时间'] = item.tm;
            item['潮位'] = item.rz || item.z || item.tdz;
            item['水位'] = item.rz || item.z || item.tdz;
            item['警戒水位'] = res.data.warningLevel;
            item['天文潮'] = item.twc;
            item['闸上水位'] = item.upz;
            item['保证水位'] = res.data.guaranteeLevel;
            item['闸下水位'] = item.dwz;
            item["闸门开度"] = item.ovs;
            item['过闸流量'] = item.ll;
            return item;
          });
          if (this.title.indexOf('水情') !== -1) {
            this.dataList = dataArr.reverse()
            this.chartWaterData.rows = info;
          } else if (this.title.indexOf('工情') !== -1) {
            this.dataList = dataArr
            this.chartWorkData.rows = info;
          }
          this.waterMarkLine.data[0].yAxis = res.data.warningLevel;
        }
      });
    },
    handleClose() {
      this.$emit('closeDialog');
    }
  }
};
</script>
<style lang="scss" scoped>
.new_box {
  width: 100%;
  height: 100%;
  display: flex;
  & > img {
    width: 90%;
  }
  justify-content: space-between;
  .left > div:nth-child(1),
  .right > div:nth-child(1) {
    margin-top: 20px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
  .left {
    width: 760px;
    padding: 10px;
    box-sizing: border-box;
    .bj_left_tooltip,
    .left_tooltip {
      margin-top: 10px;
      display: flex;
      align-items: center;
    }
    .bj_left_tooltip {
      justify-content: flex-start;
    }
    .left_tooltip {
      justify-content: flex-start;
    }
    .bj_left_tooltip,
    .left_tooltip {
      span {
        font-size: 20px;
        font-weight: 400;
        line-height: 28px;
        padding-left: 15px;
        &.one {
          color: #48fff4;
          padding-right: 15px;
        }
        &.two {
          color: #ff4a1a;
        }
        &.three {
          color: #00baff;
          padding-right: 15px;
        }
        &.four {
          color: #fcee27;
          padding-right: 15px;
        }
        &.five {
          color: #48fff4;
          padding-right: 15px;
        }
      }
    }
    .curve_line {
      width: 100%;
      height: calc(100% - 100px);
      padding-top: 16px;
      position: relative;
      span {
        position: absolute;
        right: -5px;
        top: 40px;
      }
    }
  }
  .right {
    width: calc(100% - 760px);
    padding: 10px;
    box-sizing: border-box;
    > img {
      width: 90%;
    }
    .table-box {
      margin-top: 10px;
      width: 100%;
      height: calc(100% - 100px);
    }
    ul {
      padding: 5px 0;
      li {
        & > h4 {
          font-size: 14px;
          font-weight: 600;
          color: #00baff;
          line-height: 20px;
        }
        .contact_box {
          width: 244px;
          height: 94px;
          border-radius: 4px;
          opacity: 0.2;
          border: 1px solid #00c1ff;
          display: flex;
          justify-content: space-around;
          align-items: center;
          img {
            flex: 2;
            width: 100%;
          }
          .contact_right {
            flex: 3;
            h4,
            p {
              font-weight: 500;
              color: #ffffff;
              line-height: 22px;
            }
            h4 {
              font-size: 16px;
            }
            p {
              padding-top: 5px;
              font-size: 14px;
            }
            & > div {
              display: flex;
              justify-content: space-between;
              align-items: center;
              img {
                width: 10px;
              }
            }
          }
        }
      }
    }
  }
}
</style>
<style lang="scss" scoped>
.water-supply-select-panel {
  display: flex;
  margin-bottom: 20px;
  margin-top: 20px;
}
.tab-list-sty {
  @include flex(1);
  padding-bottom: 20px;
  overflow: auto;
}
.water-supply-select-btn {
  padding: 10px 20px;
  margin-right: 20px;
  color: #c9c8c8;
  border: 1px solid rgba(255, 255, 255, 0.6);
  cursor: pointer;
  &.active {
    color: #56fefe;
    background: rgba(72, 152, 251, 0.1);
    border: 1px solid #56fefe;
    box-shadow: inset 0 0 5px 0 #56fefe;
  }
}
.primary-dialog {
  ::v-deep .el-dialog {
    @include flexbox;
    @include flexflow(column, nowrap);
    height: 680px;
    &.is-fullscreen {
      height: 100%;
      overflow: hidden;
    }
    .el-dialog__header {
      padding: 2px;
      .primary-dialog__title {
        position: relative;
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
        .full-screen-icon {
          position: absolute;
          right: 40px;
          color: #00c1ff;
          cursor: pointer;
          top: 15px;
        }
      }
    }
    .el-dialog__body {
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      @include flex(1);
      .primary-dialog__content {
        @include flexbox;
        @include flexflow(column, nowrap);
        height: 100%;
        // height: 680px;
        border-top: 0;
        padding: 0 16px;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);

        // 统计样式
        .statistics-sty {
          padding: 10px 0;
          width: 100%;
          display: flex;
          justify-content: left;
          border-bottom: 1px solid rgba($color: #409eff, $alpha: 0.4);
          > div {
            // margin-right: 50px;
            padding: 10px 0;
            margin-right: 20px;
            display: flex;
            justify-content: space-between;
            > p {
              padding-top: 5px;
              margin-right: 15px;
            }
            > div {
              > p:nth-child(1) {
                font-family: PingFangSC-Regular;
                font-size: 15px;
                color: #ffffff;
                letter-spacing: 0;
                line-height: 24px;
                font-weight: 400;
                margin-bottom: 5px;
              }
              > p:nth-child(2) {
                font-family: PingFangSC-Medium;
                font-size: 14px;
                color: #fff;
                letter-spacing: 0;
                line-height: 24px;
                font-weight: 500;
                > span {
                  font-size: 24px;
                  color: #56fefe;
                }
              }
            }
          }
        }
        // 查询样式
        .query-sty {
          padding: 15px 0;
          color: #fff;
          .derive-sty {
            padding: 0 10px;
            text-align: center;
            border-radius: 4px;
            line-height: 30px;
            float: right;
            color: #fff;
            cursor: pointer;
            border-color: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%) !important;
            background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
          }
          .aa {
            margin-right: 20px;
          }
          .demo-input-suffix {
            display: flex;
            justify-content: left;
            align-items: center;
          }
        }
      }
    }
  }
  ::v-deep.el-input-number {
    width: 5.25rem;
  }
  ::v-deep.el-input__inner:hover,
  .el-select:hover .el-input__inner {
    border: 1px solid #fff;
  }
  ::v-deep.el-input__inner {
    height: 2rem;
    background: rgba(24, 47, 90, 0.1);
    color: #fff;
    border: 1px solid #fff;
  }
  ::v-deep.el-select .el-input .el-select__caret {
    color: #fff;
  }
  ::v-deep input::-webkit-input-placeholder {
    color: #fff;
  }
  .dialog-footer {
    .el-button--primary.is-plain {
      color: #fff;
      border-color: #0bbbff;
      background-color: rgba($color: #0bbbff, $alpha: 0.1);
    }
  }
  ::v-deep.el-table .warm-row .cell {
    color: #eec80b !important;
  }
  .abc ::v-deep .el-table__body tr:hover {
    background-color: rgba($color: #56fefe, $alpha: 0.4);
  }
  .gates-open-sty {
    background: url(~assets/images/WaterDisastersImage/zhamen-kaiqi.png) no-repeat center center;
    background-size: 100% 100%;
  }
  .water-gate-sty {
    > .area-style {
      border-left: 1px solid rgba($color: #0bbbff, $alpha: 0.5);
      background: none;
      display: flex;
      justify-content: flex-start;
      > p:nth-child(1) {
        padding: 15px 15px 0 40px;
        > span {
          font-family: PingFangSC-Regular;
          font-size: 16px;
          color: #ffffff;
          font-weight: 400;
          margin-right: 10px;
        }
        > i {
          line-height: 30px;
          margin: 0 10px 0 0;
          color: #fff;
        }
        ::v-deep.el-input__inner {
          background: none;
          border: 0.0625rem solid #fff;
        }
        ::v-deep.el-input-number__decrease {
          border-left: 0.0625rem solid #fff;
          background: none;
          > i {
            color: #fff;
          }
        }
        ::v-deep.el-input-number__increase {
          height: 16px;
          border-left: 0.0625rem solid #fff;
          background: none;
          > i {
            color: #fff;
          }
        }
      }
      > div:nth-child(2) {
        padding: 15px 15px 0 40px;
        font-size: 16px;
        color: #fff;
        > span {
          font-family: PingFangSC-Medium;
          font-size: 24px;
          color: #1cfff5;
          letter-spacing: 0;
          text-align: right;
          line-height: 24px;
          font-weight: 500;
          padding: 0;
          margin: 0;
        }
      }
    }
  }
}
::v-deep.primary-dialog {
  .el-dialog {
    width: 55vw;
  }
  .tab-content-panel {
    display: flex;
    .left-panel {
      width: 60%;
      margin-right: 20px;
      .top-panel {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .title {
          font-size: 18px;
          i {
            vertical-align: middle;
            margin-right: 10px;
          }
        }
        .time {
          ::v-deep.el-date-editor {
            width: 140px;
          }
          ::v-deep.el-input__inner {
            padding-left: 1.875rem;
            background: none;
            border: 1px solid rgba(255, 255, 255, 0.5);
            height: 32px;
          }
          ::v-deep.el-input__icon {
            line-height: 1;
          }
        }
      }
      .chart-panel {
        height: calc(100% - 30px);
      }
    }
    .right-panel {
      flex: 1;
    }
  }

  .video-panel {
    height: 30rem;
    display: flex;
    justify-content: space-between;
    .left-panel {
      width: 30%;
      margin-right: 20px;
      overflow-y: scroll;
      .camera-item {
        color: #fff;
        cursor: pointer;
        margin: 8px 0;
        margin-right: 0.375rem;
        border: 1px #285c9e solid;
        padding: 6px;
        &:hover,
        &.active {
          border: 1px solid #168aa7;
          box-shadow: 0 0 20px #168aa746 inset;
        }
      }
    }
    .right-panel {
      flex: 1;
      margin-bottom: 20px;
    }
  }
  .top-panel {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .content-panel {
    height: 500px;
    display: flex;
    justify-content: space-between;
    color: #fff;
    .left-content {
      width: 60%;
      margin-right: 20px;
      .title {
        margin-bottom: 15px;
        span {
          margin-left: 10px;
          font-size: 18px;
          vertical-align: top;
        }
      }
    }
    .chart-panel {
      width: 100%;
      height: calc(100% - 40px);
    }
    .right-content {
      width: 110%;
    }
  }

  .tab-panel {
    display: flex;
    margin-bottom: 20px;
  }
  .tab-item {
    padding: 8px 10px;
    margin-right: 20px;
    color: #fff;
    border: 1px solid rgba(255, 255, 255, 0.6);
    cursor: pointer;
    &.active {
      color: #56fefe;
      background: rgba(72, 152, 251, 0.1);
      border: 1px solid #56fefe;
      box-shadow: inset 0 0 5px 0 #56fefe;
    }
  }
  .list-top-panel {
    padding: 16px;
    .list-item {
      color: #fff;
      font-size: 14px;
      margin-bottom: 20px;
      .value {
        // font-size: 14px;
        padding: 10px 10px;
        margin-top: 8px;
        border-radius: 5px;
        background-color: rgba(0, 0, 0, 0.2);
      }
    }
  }
  .list-down-panel {
    height: 300px;
    // height: calc(100% - 42px);

    .list-item {
      color: #fff;
      font-size: 14px;
      margin-bottom: 20px;
      .value {
        padding: 10px 10px;
        margin-top: 8px;
        border-radius: 5px;
        background-color: rgba(0, 0, 0, 0.2);
      }
    }
  }
  .abc ::v-deep .el-table__body tr:hover {
    background-color: rgba($color: #56fefe, $alpha: 0.4);
  }
  ::v-deep .el-dialog {
    // width:850px;
    // width:850px;
    // height: 200px;
    .el-dialog__header {
      padding: 2px;
      .primary-dialog__title {
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
      }
    }
    .el-dialog__body {
      // height: 200px;
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        height: 100%;
        padding: 20px;
      }
    }
  }
  .img-panel {
    height: 200px;
    width: 200px;
    // margin-bottom: 20px;
    img {
      width: 200px;
      height: 200px;
    }
  }
  .info-top-panel {
    border-bottom: 1px solid rgba($color: #409eff, $alpha: 0.4);
    // margin-right: 50px;
    // padding-right: 50px;
    height: 200px;
    color: #fff;
    font-size: 14px;
    line-height: 32px;
    .info-top-try {
      width: 700px;
      height: 180px;
      margin-top: -11.5rem;
      margin-left: 300px;
      //        padding-left: 204px;
      // margin-top: -84px;
    }
    img {
      width: 270px;
      height: 172px;
    }
    span.name {
      font-size: 15px;
    }
    span.value {
      font-size: 16px;
      margin-left: 10px;
      color: #33ffff;
    }
  }
  .info-down-panel {
    color: #fff;
    font-size: 16px;
    line-height: 32px;
    span.name {
      font-size: 15px;
    }
    span.value {
      margin-left: 10px;
      color: #33ffff;
    }
  }

  ::v-deep .el-radio__input.is-checked .el-radio__inner {
    border-color: #1ef8f2;
    background-color: #1ef8f2;
  }

  ::v-deep .el-dialog {
    // padding-left:20px;
    // width:200%;
    // width: 855px;
    // height: 458px;
    .el-dialog__header {
      // width:150%;
      padding: 2px;
      .primary-dialog__title {
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
      }
    }
    .el-dialog__body {
      // width:150%;
      // height: 500px;
      height: 25.25rem;
      // width: 861px;
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        height: 100%;
        padding: 20px;
      }
    }
  }
}
.abc ::v-deep .el-table__body tr:hover {
  background-color: rgba($color: #56fefe, $alpha: 0.4);
}
::v-deep .el-table {
  &.government_table {
    tr {
      td {
        &:nth-child(2),
        &:nth-child(3) {
          .cell {
            overflow: initial;
            height: 2.9125rem;
            line-height: 1.5rem;
            display: flex;
            align-items: center;
            justify-content: center;
          }
        }
      }
    }
    .el-table__body-wrapper.is-scrolling-none {
      height: calc(100% - 124px) !important;
    }
  }
}
.duty-box {
  margin-top: 24px;
  flex-wrap: wrap;
  .duty-user-box {
    margin-bottom: 16px;
  }
}
.alarm {
  .alarm-box {
    margin-top: 25px;
    > div {
      .alarm-box-title {
        padding: 8px 0 0 12px;
        font-size: 20px;
        font-family: 'YouSheBiaoTiHei';
        color: rgba(255, 255, 255, 0.88);
      }
      width: 148px;
      &.alarm-box-one {
        background: url(~assets/images/WaterDisastersImage/seawall-alarm-01.png) no-repeat;
        background-size: 100% 100%;
        height: 224px;
      }
      &.alarm-box-two {
        > div {
          height: 104px;
          background: url(~assets/images/WaterDisastersImage/seawall-alarm-02.png) no-repeat;
          background-size: 100% 100%;
          &:last-child {
            margin-top: 16px;
          }
        }
      }
      .alarm-box-01 {
        height: 174px;
        width: 136px;
        margin: 0 auto;
        margin-top: 12px;
        padding: 6px 10px;
        box-sizing: border-box;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        .box {
          > div {
            &:nth-child(1) {
              width: 48px;
              height: 43px;
              img {
                width: 100%;
                height: 100%;
              }
            }
            &:nth-child(2) {
              flex-direction: column;
              font-size: 14px;
              color: #ffffff;
              padding: 3px 0;
            }
          }
        }
      }
      .alarm-box-02 {
        width: 136px;
        height: 59px;
        margin: 0 auto;
        margin-top: 12px;
        padding: 8px 0 0;
        > div {
          color: #ffffff;
          span {
            font-size: 14px;
            &.no-alarm {
              color: #00baff;
            }
            &.red-alarm {
              color: #ff0000;
            }
            &:last-child {
              padding-left: 6px;
            }
          }
          &:last-child {
            margin-top: 12px;
          }
          &.alarm-box-02-div {
            color: #ff0000;
            margin-top: 10px;
            span {
              font-style: 16px;
              &:first-child {
                font-size: 20px;
                margin-right: 5px;
              }
            }
          }
        }
      }
      .alarm-box-03 {
        flex-direction: column;
        justify-content: flex-start;
        width: 136px;
        margin: 0 auto;
        margin-top: 12px;
        padding: 7px 0 0 7px;
        > div {
          color: #ffffff;
          font-size: 14px;
          margin-bottom: 16px;
          > div {
            &:last-child {
              color: #00baff;
              padding-top: 7px;
              span {
                font-size: 20px;
                padding-right: 5px;
              }
            }
          }
          &:last-child {
            margin-bottom: 0;
          }
        }
      }
      .alarm-box-04 {
        width: 136px;
        margin: 0 auto;
        margin-top: 12px;
        padding: 10px 8px 0 13px;
        > div {
          margin-bottom: 18px;
          font-size: 14px;
          color: #ffffff;
          span {
            &:last-child {
              padding-left: 17px;
            }
            &.no-alarm-span {
              color: #00baff;
            }
            &.red-alarm-span {
              color: #ff0000;
            }
          }
        }
      }
    }
  }
}
.no-responsible {
  width: 147px;
  text-align: center;
  margin: 0 auto;
  margin-top: 30px;
  > div {
    user-select: none;
    width: 100%;
    height: 87px;
    background-image: url(~assets/images/no-responsible.png);
    background-repeat: no-repeat;
    background-size: 100% 100%;
    margin-bottom: 16px;
  }
  > span {
    color: #fff;
  }
}
</style>
