<!--
 * @Date: 2022-07-27 10:57:08
 * @Author: 
 * @Description: 概化图 水闸
 * @LastEditors: dtb
 * @LastEditTime: 2022-07-28 00:18:29
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\MotionSluiceDialog.vue
-->
<template>
  <div class="detail-table">
    <!-- <DataDetailDialog :visible.sync="visible" :before-close="handleClose" class="detail-table" :title="pointData.name"> -->
      <!-- 概化图 -->
      <div class="new_box">
        <div class="sluice_flex">
        <!-- <div class="sluice_flex" v-if="pointData.stType == 'DD'"> -->
          <div class="danger-tap">
            <div @click="checkTab(index)" :class="{ active: index == tabAction }" v-for="(item, index) in tabList" :key="item">
              {{ item }}
            </div>
          </div>
          <ProjectInfo v-if="tabAction===1" :responsibleList='contactList' :propList='propList' :type='pointData.type' />
          <ProjectVideo v-if="tabAction===2" :code='pointData.code' />
          <CustomIframe :src="frameUrl" v-if="tabAction===3" />,
          <!-- <RunningInfo v-if="tabAction===3" :type='pointData.type' :stationCode='pointData.stCode' /> -->
          <div class="new_box_item" v-if="tabAction===0">
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
        </div>
      </div>
    <!-- </DataDetailDialog> -->
  </div>
</template>

<script>
const displayPropsMap = {
  水闸: [
    { name: "工程规模", prop: "engScal" },
    { name: "水闸类型", prop: "wagaType" },
    { name: "设计最大过闸流量(m³/s)", prop: "desLockDisc" },
    { name: "闸孔数量", prop: "gaorNum" },
    { name: "闸孔总净宽", prop: "gaorTotNetWid" },
    { name: "闸门宽度", prop: "gatewidth" },
    { name: "闸底高程", prop: "gatebottomelevation" },
    { name: "闸门形式", prop: "gateType" },
    { name: "闸门高度", prop: "gateSize" },
    { name: "警戒水位", prop: "referenceWlStationWarningLevel" },
    { name: "危急水位", prop: "referenceWlStationCriticalLevel" },
  ],
};
/** 自定义iframe */
let CustomIframe = {
  props: {},
  render() {
    return <iframe style={{ width: '100%', height: 'calc(100% - 80px)', 'min-height': '450px' }} />;
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
import { PrimaryMapTable, ProjectDialog } from '@/components';
import { DataDetailDialog } from './DetailsCommonDialog/components';
import ResponsibleListItem from '@/components/screen_project_dialog/components/ResponsibleListItem.vue';
import BasicInfo from '@/components/screen_project_dialog/components/BasicInfo.vue';
import ProjectItem from '@/components/screen_project_dialog/components/ProjectItem.vue';
import ProjectVideo from '@/components/screen_project_dialog/components/ProjectVideo.vue';
import ResponsibleList from '@/components/screen_project_dialog/components/ResponsibleList.vue';
import RunningInfo from '@/components/screen_project_dialog/components/RunningInfo.vue';
import { reservoirRegulationApi } from '@/api/dataScreen/ReservoirRegulation';
import { waterAndRain } from '@/api/bigScreenPart';
import { hydraulicApi } from '@/api/hydraulic_amap';
export default {
  name: 'MotionSluiceDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    pointData: Object,
    title: {
      type: String,
      default: '概化图'
    }
  },
  components: {
    DataDetailDialog,
    ResponsibleListItem,
    ProjectDialog,
    BasicInfo,
    ProjectInfo, CustomIframe,
    ProjectItem,
    ProjectVideo,
    ResponsibleList,
    RunningInfo,
    PrimaryMapTable
  },
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
        top: 60
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
      "yAxis.1.axisLabel.color": "#fff",
    };
    this.chartWaterSettings = {
      showLine: ['水位曲线'],
      axisSite: { right: ['过闸流量'] },
      yAxisType: ['KMB', 'KMB'],
    };
    this.waterGrid = {
      top: 0,
      bottom: 0,
      left: 0,
      right: 0
    };
    return {
      showGateFlowText:false,
      contactList: [],
      propList: [],
      tabList: ['运行曲线','工程信息', '视频监控', '运行管理'],
      tabAction: 0,
      frameUrl: '',
      token: '',
      fullscreen: false,
      waterColors: ["#19AF47", "#008FE8", "#E89100", "#A80BEE", "#FF4A1A"],
      workcolumnList: [],
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
            color: '#FF4A1A ' // 这儿设置安全基线颜色
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
  computed: {},
  mounted() {
    this.getDynamicToken()
    this.getContactList();
    this.getProjectInfo()
    this.checkTab(0)
  },
  methods: {
    async checkTab(index) {
      this.tabAction = index;
      if (index === 1) {
        this.getContactList();
        this.getProjectInfo();
      }
      if (index === 3) {
        this.frameUrl = `https://sk.zjwater.com/cp/skip/reservoirDetailSingle?prcd=${this.pointData.code
          }&name=${encodeURI(encodeURI(this.pointData.name))}&fromMap=1&pName=&cName=&token=${this.token}`;
      }
      if (index === 0) {
        this.getRainStationInfo()
      }
    },

    getContactList() {
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
      const { code } = { ...this.pointData };
      hydraulicApi.getProjects({ withWaterLevel: false, type: '水闸', code: code || '' }).then((res) => {
        let resProp = res.data[0],
          list = [];
        for (let item of displayPropsMap['水闸']) {
          list.push({ name: item.name, prop: item.prop, data: (resProp && resProp[item.prop]) || '-' });
        }
        this.propList = list;
      });
    },
    getDynamicToken() {
      hydraulicApi.getDynamicToken().then((res) => {
        if (res.code == 0) {
          this.token = res.data;
        }
      });
    },
    //获取曲线数据和列表数据
    async getRainStationInfo() {
      this.loading = true;
      let opt = {
        stationCode: this.pointData.extraInfo.stCode ||this.pointData.extraInfo.wagaCode,
        stationType: this.pointData.extraInfo.stType ||this.pointData.extraInfo.wagaType,
        days: 1
      };
      waterAndRain.waterLevels(opt).then((res) => {
        if (res.code == 0) {
          this.loading = false;
          this.workcolumnList = [
            {
              prop: 'tm',
              label: '时间'
            },
            {
              prop: 'ovs',
              label: '闸门开度(m)'
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

          this.dataList = res.data.data;
          let info = JSON.parse(JSON.stringify(res.data.data));
          let arrs = JSON.parse(JSON.stringify(res.data.data)).reverse();
          if (res.data.astroTdz) {
            if (res.data.astroTdz.length > 0) {
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
              info = JSON.parse(JSON.stringify(res.data.data)).reverse();
            }
          } else {
            info = JSON.parse(JSON.stringify(res.data.data)).reverse();
          }
          this.chartWorkData.columns = ['时间', '闸门开度', '闸上水位', '闸下水位', '过闸流量', '警戒水位'];
          // info.map((v)=>v.ll = 300)
          this.showGateFlowText =  info.some((v)=>v.ll !== null)
          info.map((item) => {
            item['时间'] = item.tm;
            item['潮位'] = item.rz || item.z || item.tdz;
            item['水位'] = item.rz || item.z || item.tdz;
            item['天文潮'] = item.twc;
            item['闸上水位'] = item.upz;
            item['保证水位'] = res.data.guaranteeLevel;
            item['警戒水位'] = res.data.warningLevel;
            item['闸下水位'] = item.dwz;
            item["闸门开度"] = item.ovs;
            item['过闸流量'] = item.ll;
            return item;
          });
          this.chartWorkData.rows = info;
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
.new_box_item {
  width: 100%;
  height: 96%;
}
.new_box {
  width: 100%;
  // min-height: 600px;
}
.new_box,
.new_box_item {
  display: flex;
  & > img {
    width: 90%;
  }
  justify-content: space-between;
  .left > div:nth-child(1),
  .right > div:nth-child(1) {
    // margin-top: 20px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
  .left {
    width: 750px;
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
      height: 500px;
      padding-top: 16px;
      position: relative;
      span {
        position: absolute;
        right: -5px;
        top: 55px;
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
      height: 500px;
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
.sluice_flex {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.danger-tap {
  display: flex;
  height: 32px;
  margin: 15px;
  > div {
    width: 114px;
    height: 32px;
    border: 1px solid rgba(255, 255, 255, 0.6);
    line-height: 32px;
    text-align: center;
    margin-right: 16px;
    color: rgba(255, 255, 255, 0.8);
    cursor: pointer;
    &.active {
      color: #00baff;
      background: rgba(72, 152, 251, 0.1);
      box-shadow: 0px 0px 5px 0px #00baff inset;
      border-color: rgba(0, 186, 255, 0.6);
    }
  }
}
.project-info {
  width: 100%;
  height: calc(100% - 100px);
}
</style>
