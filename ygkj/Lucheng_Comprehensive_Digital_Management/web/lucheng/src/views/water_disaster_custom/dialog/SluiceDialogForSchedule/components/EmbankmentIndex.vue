<!--
 * @Date: 2022-04-02 13:35:34
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-05-23 14:02:15
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\SluiceDialogForSchedule\components\EmbankmentIndex.vue
-->
<template>
  <div class="seawall">
    <div class="basic-info flex-box">
      <div>
        <div class="seawall-title">
          <span class="img"></span>
          <span>基本信息</span>
        </div>
        <div class="basic-info-box">
          <BasicInfo :propList="propList" />
        </div>
      </div>
      <div class="flex-box">
        <div class="duty">
          <div class="seawall-title">
            <span class="img"></span>
            <span>六个责任人</span>
          </div>
          <div class="duty-box flex-box">
            <template v-if="contactList.length">
              <div v-for="(item, index) in contactList" :key="item.id">
                <responsible-list-item :data="item" :flag="index" />
              </div>
            </template>
            <div v-else class="no-responsible">
              <div></div>
              <span>暂无责任人</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="flex-box duty-alarm">
      <div class="alarm">
        <div class="seawall-title">
          <span class="img"></span>
          <span>重大报警</span>
        </div>
        <div class="alarm-box flex-box">
          <div class="alarm-box-one">
            <div class="alarm-box-title">堤防超警</div>
            <div class="alarm-box-04">
              <div>
                <span>堤防超警</span>
                <span class="no-alarm-span" :class="{ 'red-alarm-span': waterLevelWarnType.warning }">{{
                  !waterLevelWarnType.warning ? '正常' : '超警'
                }}</span>
              </div>
            </div>
          </div>
          <div class="alarm-box-one">
            <div class="alarm-box-title">台风影响</div>
            <div class="alarm-box-03 flex-box">
              <template v-if="typhoonData.length > 0">
                <div>
                  <div>台风名称</div>
                  <div>
                    <span>{{ typhoonData[0].name }}</span>
                  </div>
                </div>
                <div>
                  <div>台风距离</div>
                  <div>
                    <span>{{ typhoonData[0].distance }}km</span>
                  </div>
                </div>
              </template>
              <template v-else>
                <div class="typhoon-no"></div>
              </template>
            </div>
          </div>
          <div class="alarm-box-one">
            <div class="alarm-box-title">运行报警</div>
            <div class="alarm-box-04">
              <div>
                <span>安全鉴定</span>
                <span
                  class="no-alarm-span"
                  @click="openSupervise('运行报警', RuntimeWarn.safety)"
                  :class="{ 'red-alarm-span': RuntimeWarn.safety == 1 }"
                  >{{ RuntimeWarn.safety == 1 ? '已超期' : '未超期' }}</span
                >
              </div>
              <div>
                <span>汛前检查</span>
                <span
                  class="no-alarm-span"
                  @click="openSupervise('运行报警', RuntimeWarn.flood)"
                  :class="{ 'red-alarm-span': RuntimeWarn.flood == 1 }"
                  >{{ RuntimeWarn.flood == 1 ? '已超期' : '未超期' }}</span
                >
              </div>
              <div>
                <span>年度总结</span>
                <span
                  class="no-alarm-span"
                  @click="openSupervise('运行报警', RuntimeWarn.yearReport)"
                  :class="{ 'red-alarm-span': RuntimeWarn.yearReport == 1 }"
                  >{{ RuntimeWarn.yearReport == 1 ? '已超期' : '未超期' }}</span
                >
              </div>
              <div>
                <span>巡查信息</span>
                <span
                  class="no-alarm-span"
                  @click="openSupervise('运行报警', RuntimeWarn.patrol)"
                  :class="{ 'red-alarm-span': RuntimeWarn.patrol == 1 }"
                  >{{ RuntimeWarn.patrol == 1 ? '异常' : '正常' }}</span
                >
              </div>
              <div>
                <span>工程隐患</span>
                <span
                  class="no-alarm-span"
                  @click="openSupervise('运行报警', RuntimeWarn.danger)"
                  :class="{ 'red-alarm-span': RuntimeWarn.danger == 1 }"
                  >{{ RuntimeWarn.danger == 1 ? '有隐患' : '无隐患' }}</span
                >
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="primary-dialog" v-show="visible">
      <el-dialog
        top="30vh"
        width="600px"
        destroy-on-close
        :before-close="() => (visible = false)"
        :modal="false"
        :visible.sync="visible"
      >
        <!-- 弹窗标题 -->
        <div class="primary-dialog__title" slot="title">
          <p class="primary-dialog__title--label">发送信息</p>
        </div>
        <!-- 内容区域 -->
        <div class="primary-dialog__content">
          <el-row :gutter="14">
            <el-col :span="4" style="line-height: 2.75rem; color: #fff">预警内容：</el-col>
            <el-col :span="20">
              <el-input type="textarea" v-model="content" />
            </el-col>
          </el-row>
          <el-row :gutter="14">
            <el-col :span="4" style="line-height: 2.75rem; color: #fff">发送类型：</el-col>
            <el-col :span="20" style="line-height: 2.75rem">
              <el-radio-group v-model="sendType">
                <el-radio :label="1">短信</el-radio>
                <el-radio :label="2">浙政钉</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row type="flex" style="justify-content: flex-end">
            <div class="send_btn" @click="sendContent">发送</div>
          </el-row>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
const displayPropsMap = {
  堤防: [
    { name: '堤防级别', prop: 'dikeGrad' },
    { name: '堤防类型', prop: 'dikeType' },
    { name: '堤防型式', prop: 'dikePatt' },
    { name: '防洪标准(年)', prop: 'planFlSta' },
    { name: '堤防长度(m)', prop: 'dikeLen' },
    { name: '堤顶最小宽度(m)', prop: 'dikeTopWidMin' },
    { name: '堤顶最大宽度(m)', prop: 'dikeTopWidMax' },
    { name: '保护范围(km²)', prop: 'protectionArea' },
    { name: '警戒水位(m)', prop: 'warnWaterLevel' }
  ]
};
import { seawallTyphoonDif } from '@/api/seawall.js';
import ResponsibleListItem from '@/components/screen_project_dialog/components/ResponsibleListItem.vue';
import BasicInfo from './BasicInfo.vue';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention';
import { reservoirRegulationApi } from '@/api/dataScreen/ReservoirRegulation';
import { hydraulicApi } from '@/api/hydraulic_amap';
import { waterResourceApi, riskMonitoring } from '@/api/waterResourcesGuarantee';
export default {
  name: 'EmbankmentIndex',
  props: {
    code: {
      type: String,
      default: ''
    },
    lat: {
      type: String,
      default: ''
    },
    lng: {
      type: String,
      default: ''
    },
    pointData: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      contactList: [],
      visible: false,
      content: null,
      sendType: 2,
      RuntimeWarn: {
        danger: 0,
        flood: 0,
        patrol: 0,
        safety: 0,
        yearReport: 0
      },
      weatherWarningList: [],
      typhoonData: [],
      propList: [],
      waterLevelWarnType: {}
    };
  },
  components: {
    BasicInfo,
    ResponsibleListItem
  },
  computed: {},
  mounted() {
    this.getContactList();
    this.getProjectInfo();
    this.getWeatherWarning();
    this.getTyphoonDif();
    this.listRuntimeWarn();
    this.waterLevelWarn();
  },
  methods: {
    getContactList() {
      let contactList = [];
      reservoirRegulationApi.getThreeCharge(this.pointData.projectCode).then((res) => {
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
      hydraulicApi
        .getProjects({ withWaterLevel: false, type: '堤防', code: this.pointData.projectCode || '' })
        .then((res) => {
          let resProp = res.data[0],
            list = [];
          for (let item of displayPropsMap['堤防']) {
            list.push({ name: item.name, prop: item.prop, data: (resProp && resProp[item.prop]) || '-' });
          }
          this.propList = list;
        });
    },
    async getTyphoonDif() {
      let res = await seawallTyphoonDif({ lat: this.pointData.lat, lng: this.pointData.lng });
      if (res.code == 0) {
        this.typhoonData = res.data.map((item) => {
          let points = JSON.parse(item.points);
          item['distance'] = points[points.length - 1].distance;
          item['power'] = points[points.length - 1].power;
          return item;
        });
      }
    },
    sendContent() {
      let obj = {
        context: this.content,
        phone: this.data.phone,
        type: this.sendType
      };
      reservoirRegulationApi.sendMsg(obj).then((res) => {
        if (res.code == 0 && res.data.result === 0) {
          this.content = '';
          this.sendType = 2;
          this.visible = false;
          this.$message({
            type: 'success',
            message: res.message
          });
        } else {
          this.$message({
            type: 'error',
            message: `发送失败: ${res.data.errMsg}`
          });
        }
      });
    },
    // 天气
    disposeWarn(type) {
      switch (type) {
        case 1:
          return '正常';
        case 2:
          return '超蓝色';
        case 3:
          return '超黄色';
        case 4:
          return '超橙色';
        default:
          return '超红色';
      }
    },
    getWeatherWarningIcon(name) {
      let imgSrc = '';
      switch (name) {
        case '暴雨橙色':
          {
            imgSrc = require('@/assets/images/warns/heavy-rain-orange.png');
          }
          break;
        case '暴雨红色':
          {
            imgSrc = require('@/assets/images/warns/heavy-rain-red.png');
          }
          break;
        case '暴雨黄色':
          {
            imgSrc = require('@/assets/images/warns/heavy-rain-yellow.png');
          }
          break;
        case '暴雨蓝色':
          {
            imgSrc = require('@/assets/images/warns/heavy-rains-blue.png');
          }
          break;
        case '大风橙色':
          {
            imgSrc = require('@/assets/images/warns/the-wind-orange.png');
          }
          break;
        case '大风黄色':
          {
            imgSrc = require('@/assets/images/warns/the-wind-yellow.png');
          }
          break;
        case '大风蓝色':
          {
            imgSrc = require('@/assets/images/warns/the-wind-blue.png');
          }
          break;
        case '干旱橙色':
          {
            imgSrc = require('@/assets/images/warns/dry-orange.png');
          }
          break;
        case '雷电橙色':
          {
            imgSrc = require('@/assets/images/warns/lightning-orange.png');
          }
          break;
        case '雷电黄色':
          {
            imgSrc = require('@/assets/images/warns/lightning-yellow.png');
          }
          break;
        case '雷雨大风橙色':
          {
            imgSrc = require('@/assets/images/warns/thunderstorm-gale-orange.png');
          }
          break;
        case '沙尘暴橙色':
          {
            imgSrc = require('@/assets/images/warns/dust-storm-orange.png');
          }
          break;
        default: {
          imgSrc = require('@/assets/images/warns/warning-remind.png');
        }
      }
      return imgSrc;
    },
    getWeatherWarning() {
      disasterPreventionApi.weatherWarning().then((res) => {
        if (res.code == 0) {
          let arr = {
            blue: 0,
            yellow: 0,
            orange: 0,
            red: 0
          };
          this.weatherWarningList = res.data;
          this.weatherWarningList.map((item) => {
            let type = item.WARN_TYPE + item.WARN_LEVEL;
            item.imgSrc = this.getWeatherWarningIcon(type);
            arr.blue += item.WARN_LEVEL == '蓝色' ? 1 : 0;
            arr.yellow += item.WARN_LEVEL == '黄色' ? 1 : 0;
            arr.orange += item.WARN_LEVEL == '橙色' ? 1 : 0;
            arr.red += item.WARN_LEVEL == '红色' ? 1 : 0;
          });
        }
      });
    },
    async listRuntimeWarn() {
      let res = await waterResourceApi.listRuntimeWarn({
        projectType: '堤防',
        projectCode: this.pointData.projectCode
      });
      if (res.code == 0) {
        this.RuntimeWarn.safety = res.data.filter((item) => item.safety == 1).length;
        this.RuntimeWarn.flood = res.data.filter((item) => item.flood == 1).length;
        this.RuntimeWarn.yearReport = res.data.filter((item) => item.yearReport == 1).length;
        this.RuntimeWarn.patrol = res.data.filter((item) => item.patrol == 1).length;
        this.RuntimeWarn.danger = res.data.filter((item) => item.danger == 1).length;
      }
    },
    async waterLevelWarn() {
      let res = await riskMonitoring.waterLevelWarn({
        projectType: '堤防',
        projectCode: this.pointData.projectCode
      });
      console.log(res);
      if (res.code == 0) {
        this.waterLevelWarnType = res.data.list.length > 0 ? res.data.list[0] : {};
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.flex-box {
  display: flex;
  justify-content: space-between;
}
.seawall {
  padding-top: 12px;
  .basic-info {
    width: 100%;
    > div:nth-child(1) {
      width: calc(100% - 600px);
      .basic-info-box {
        padding-top: 24px;
      }
    }
    > div:nth-child(2) {
      width: 585px;
      .duty {
        width: 100%;
        .duty-box {
          margin-top: 24px;
          flex-wrap: wrap;
          .duty-user-box {
            margin-bottom: 16px;
          }
        }
      }
    }
  }
}
.seawall-title {
  display: flex;
  align-items: center;
  padding-bottom: 10px;
  position: relative;
  color: #ffffff;
  .img {
    background: url(~assets/images/WaterDisastersImage/seawall-title-icon.png) no-repeat;
    background-size: 100% 100%;
    width: 20px;
    height: 20px;
    margin-right: 6px;
  }
  &::after {
    content: '';
    position: absolute;
    width: 100%;
    height: 4px;
    background: url(~assets/images/WaterDisastersImage/seawall-title-icon-overview.png) no-repeat;
    background-size: 100% 100%;
    bottom: 0;
  }
}

.duty-alarm {
  .alarm {
    width: 560px;
    .alarm-box {
      margin-top: 25px;
      > div {
        .alarm-box-title {
          padding: 8px 0 0 12px;
          font-size: 20px;
          font-family: 'YouSheBiaoTiHei';
          color: rgba(255, 255, 255, 0.88);
        }
        width: 160px;
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
            margin-bottom: 30px;
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
            &:first-child {
              margin-top: 12px;
            }
            &:last-child {
              margin-bottom: 0;
            }
          }
          .typhoon-no {
            margin: 5px 0 0 0;
            width: 120px;
            height: 146px;
            background: url(~assets/images/ScreenAsset/typhoon-no-icon.png) no-repeat;
            background-size: 100% 100%;
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
                cursor: pointer;
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
}
.send_btn {
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
.setting_btn {
  margin-top: 5px;
  width: 20px;
  height: 20px;
  font-size: 20px;
  background: center/100% url(~assets/images/setting-btn.png) no-repeat;
}
::v-deep .primary-dialog .el-dialog .el-dialog__body {
  padding: 1.125rem;
}
::v-deep .el-textarea .el-textarea__inner {
  background-color: transparent;
  color: #fff;
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
