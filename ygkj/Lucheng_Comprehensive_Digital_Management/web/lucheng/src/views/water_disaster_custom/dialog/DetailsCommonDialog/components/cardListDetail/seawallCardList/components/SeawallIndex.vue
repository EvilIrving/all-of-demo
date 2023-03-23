<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-07 15:42:13
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\SeawallIndex.vue
-->
<template>
  <div class="seawall">
    <div class="flex-box">
      <div class="overview">
        <div class="seawall-title">
          <span class="img"></span>
          <span>工程概述</span>
        </div>
        <div class="overview-box flex-box">
          <div class="danger-tap">
            <div @click="checkTab(index)" :class="{ active: index == tabAction }" v-for="(item, index) in tabList" :key="item">
              {{ item }}
            </div>
          </div>
          <i v-show="tabAction === 1" class="setting_btn" @click="weightsConfig(code)" style="cursor:pointer"></i>
          <div class="overview-img">
            <div v-if="tabAction === 0" class="overview-img-box"></div>
            <div v-else class="qrcode_box">
              <img :src="qrImg" alt="" />
            </div>
          </div>
          <div class="basic-info">
            <div class="seawall-top-title">基本信息</div>
            <div class="flex-box basic-info-box">
              <div class="flex-box">
                <div>
                  <span>海塘类型：</span>
                  <span>{{ detail.safetyConclusion || '-' }}</span>
                </div>
                <div>
                  <span>设计防潮标准(年)：</span>
                  <span>{{ detail.designTideStandard || '-' }}</span>
                </div>
                <div>
                  <span>设计潮位(m)：</span>
                  <span>{{ detail.designTiddeStandard || '-' }}</span>
                </div>
                <div>
                  <span>海塘长度(m)：</span>
                  <span>{{ detail.seawallLength || '-' }}</span>
                </div>
                <div>
                  <span>海塘高度(最大值m)：</span>
                  <span>{{ detail.maxHeight || '-' }}</span>
                </div>
                <div>
                  <span>海塘高度(最小值m)：</span>
                  <span>{{ detail.minHeight || '-' }}</span>
                </div>
                <div>
                  <span>塘顶高程(起点m)：</span>
                  <span>{{ detail.startCrestAltitude || '-' }}</span>
                </div>
                <div>
                  <span>海塘平均高程(m)：</span>
                  <span>{{ detail.averageAltitude || '-' }}</span>
                </div>
              </div>
              <div class="flex-box">
                <div>
                  <span>海塘形式：</span>
                  <span>{{ detail.seawallType || '-' }}</span>
                </div>
                <div>
                  <span>实际防潮标准(年)：</span>
                  <span>{{ detail.designTideStandard || '-' }}</span>
                </div>
                <div>
                  <span>水基准面：</span>
                  <span>{{ detail.levelDatum || '-' }}</span>
                </div>
                <div>
                  <span>达到规划防洪(潮)标准的长度(m)：</span>
                  <span>{{ detail.reachStandardLength || '-' }}</span>
                </div>
                <div>
                  <span>塘顶宽度(最大值m)：</span>
                  <span>{{ detail.maxWidth || '-' }}</span>
                </div>
                <div>
                  <span>塘顶宽度(最小值m)：</span>
                  <span>{{ detail.minWidth || '-' }}</span>
                </div>
                <div>
                  <span>塘顶高程(终点m)：</span>
                  <span>{{ detail.finalCrestAltitude || '-' }}</span>
                </div>
                <div>
                  <span>海塘最低高程(m)：</span>
                  <span>{{ detail.lowestElevation || '-' }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="operation">
        <div class="seawall-title">
          <span class="img"></span>
          <span>运行管理</span>
        </div>
        <div class="operation-box flex-box">
          <div class="operation-left">
            <div class="seawall-top-title">值班信息</div>
            <div class="operation-left-content">
              <div class="user-box">
                <div>值班负责人</div>
                <div>
                  <div class="user-img">
                    <img :src="userImg" alt="" />
                  </div>
                  <div class="user-info">
                    <div class="user-name">
                      <span>{{ dutyDetail.leader ? dutyDetail.leader.name : '-' }}</span>
                      <span class="user-note" v-if="dutyDetail.leader && dutyDetail.leader.name" @click="visible = true"></span>
                    </div>
                    <div class="user-jop" :title="dutyDetail.leader ? dutyDetail.leader.duty : ''">
                      {{ dutyDetail.leader ? dutyDetail.leader.duty : '-' }}
                    </div>
                    <div>{{ dutyDetail.leader ? dutyDetail.leader.telphone : '-' }}</div>
                  </div>
                </div>
              </div>
              <div class="user-box">
                <div>值班人员</div>
                <div>
                  <div class="user-img">
                    <img :src="userImg" alt="" />
                  </div>
                  <div class="user-info">
                    <div class="user-name">
                      <span>{{
                        dutyDetail.worker && dutyDetail.worker[0] && dutyDetail.worker.length > 0 ? dutyDetail.worker[0].name : '-'
                      }}</span>
                      <span class="user-note" v-show="dutyDetail.worker && dutyDetail.worker[0]!==null&& dutyDetail.worker.length > 0 " @click="visible = true"></span>
                    </div>
                    <div class="user-jop" :title="dutyDetail.worker && dutyDetail.worker[0]!==null&& dutyDetail.worker.length > 0 ? dutyDetail.worker[0].duty : ''">
                      {{ dutyDetail.worker && dutyDetail.worker[0] !==null&& dutyDetail.worker.length > 0 ? dutyDetail.worker[0].duty : '-' }}
                    </div>
                    <div>
                      {{ dutyDetail.worker && dutyDetail.worker[0] !==null&& dutyDetail.worker.length > 0 ? dutyDetail.worker[0].telphone : '-' }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="operation-right">
            <div class="seawall-top-title">管理信息</div>
            <div class="operation-right-content">
              <div class="right-content-box">
                <div class="flex-box">
                  <span>安全鉴定</span>
                  <span>{{ manageInfo.safetyConclusion }}</span>
                </div>
                <div class="flex-box">
                  <span>除险加固</span>
                  <span>{{ manageInfo.reinforce || '-' }}</span>
                </div>
                <div class="flex-box">
                  <span>险情处置</span>
                  <span>{{
                    manageInfo.greatDanger == 0 ? '无险情 ' : manageInfo.greatDanger == 1 ? '已处置' : '未处置'
                  }}</span>
                </div>
                <div class="flex-box">
                  <span>防汛预案</span>
                  <span>{{ manageInfo.floodPlan == 1 ? '已上传' : '未上传' }}</span>
                </div>
                <div class="flex-box">
                  <span>防汛演练</span>
                  <span>{{ manageInfo.floodDrill == 1 ? '已演练' : '未演练' }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="flex-box duty-alarm">
      <div class="duty">
        <div class="seawall-title">
          <span class="img"></span>
          <span>六个责任人</span>
        </div>
        <div class="duty-box flex-box">
          <template v-if="contactList.length">
            <div v-for="(item,index) in contactList" :key="item.id">
              <responsible-list-item :data="item" :flag='index' />
            </div>
          </template>
          <div v-else class="no-responsible">
            <div></div>
            <span>暂无责任人</span>
          </div>
          <!-- <div class="user-box duty-user-box" v-for="item in 6" :key="item">
            <div>值班人员</div>
            <div>
              <div class="user-img">
                <img :src="userImg" alt="" />
              </div>
              <div class="user-info">
                <div class="user-name">
                  <span>姚高员</span>
                  <span></span>
                </div>
                <div class="user-jop">市长</div>
                <div>055755558888</div>
              </div>
            </div>
          </div> -->
        </div>
      </div>
      <div class="alarm">
        <div class="seawall-title">
          <span class="img"></span>
          <span>重大报警</span>
        </div>
        <div class="alarm-box flex-box">
          <div class="alarm-box-one">
            <div class="alarm-box-title">气象预警</div>
            <div class="alarm-box-01" v-if="weatherWarningList.length > 0">
              <div class="box flex-box" v-for="(item, index) in weatherWarningList" :key="index">
                <div>
                  <img :src="item.imgSrc" alt="" />
                </div>
                <div class="flex-box">
                  <div>{{ item.WARN_TYPE + item.WARN_LEVEL }}</div>
                  <div>{{ item.WARN_LEVEL }}预警</div>
                </div>
              </div>
            </div>
            <div class="alarm-box-01" v-else>
              <div class="box" style="text-align: center; padding-top: 50px">暂无数据</div>
            </div>
          </div>
          <div class="alarm-box-two">
            <div>
              <div class="alarm-box-title">海塘预警</div>
              <div class="alarm-box-02">
                <div>
                  <span>海塘报警</span>
                  <span class="no-alarm" @click="openSupervise('海塘预警', seawallWarn.warn)" :class="{ 'red-alarm': seawallWarn.warn != '正常' }">{{ seawallWarn.warn }}</span>
                </div>
                <div>
                  <span>海塘预警</span>
                  <span class="no-alarm" @click="openSupervise('海塘预警', seawallWarn.earlyWarn)" :class="{ 'red-alarm': seawallWarn.earlyWarn != '正常' }">{{ seawallWarn.earlyWarn }}</span>
                </div>
              </div>
            </div>
            <div>
              <div class="alarm-box-title">安全监测</div>
              <div class="alarm-box-02">
                <div>
                  <span>沉降</span>
                  <span class="no-alarm" :class="{ 'red-alarm': security.zj == 1 }">{{security.zj == 0 ?"正常":"异常"}}</span>
                </div>
                <div>
                  <span>位移</span>
                  <span class="no-alarm" :class="{ 'red-alarm': security.wy == 1 }">{{security.wy == 0 ?"正常":"异常"}}</span>
                </div>
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
                <span class="no-alarm-span" @click="openSupervise('运行报警', RuntimeWarn.safety)" :class="{ 'red-alarm-span': RuntimeWarn.safety == 1 }">{{ RuntimeWarn.safety == 1 ? '已超期' : '未超期' }}</span>
              </div>
              <div>
                <span>汛前检查</span>
                <span class="no-alarm-span" @click="openSupervise('运行报警', RuntimeWarn.flood)" :class="{ 'red-alarm-span': RuntimeWarn.flood == 1 }">{{ RuntimeWarn.flood == 1 ? '已超期' : '未超期' }}</span>
              </div>
              <div>
                <span>年度总结</span>
                <span class="no-alarm-span" @click="openSupervise('运行报警', RuntimeWarn.yearReport)" :class="{ 'red-alarm-span': RuntimeWarn.yearReport == 1 }">{{ RuntimeWarn.yearReport == 1 ? '已超期' : '未超期' }}</span>
              </div>
              <div>
                <span>巡查信息</span>
                <span class="no-alarm-span" @click="openSupervise('运行报警', RuntimeWarn.patrol)" :class="{ 'red-alarm-span': RuntimeWarn.patrol == 1 }">{{ RuntimeWarn.patrol == 1 ? '异常' : '正常' }}</span>
              </div>
              <div>
                <span>工程隐患</span>
                <span class="no-alarm-span" @click="openSupervise('运行报警', RuntimeWarn.danger)" :class="{ 'red-alarm-span': RuntimeWarn.danger == 1 }">{{ RuntimeWarn.danger == 1 ? '有隐患' : '无隐患' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="primary-dialog" v-show="visible">
      <el-dialog top="30vh" width="600px" destroy-on-close :before-close="() => (visible = false)" :modal="false" :visible.sync="visible">
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
    <PrimaryDialog :visible="visibleDialog" :appendToBody="true" @analysis="this.$emit('analysis')" main-title="督办中心" width="1200px" @handleClose="visibleDialog = false">
      <superviseIndex :detail="detailData" v-if="visibleDialog" :code="code" />
    </PrimaryDialog>
    <!-- 权重配置 -->
    <PrimaryDialog :visible="healthDialogVisible" @handleClose="healthDialogVisible = false" main-title="健康码赋分设置" width="1200px">
      <base-form v-if="healthDialogVisible" :info="healthCode" @close="close">
      </base-form>
    </PrimaryDialog>
  </div>
</template>

<script>
import {
  getSeawallProjects,
  getSeawallSelectDuty,
  getSeawallSelectManageInfo,
  getSeawallselectRuntimeWarn,
  getSeawallSelectWarn,
  seawallTyphoonDif,
  getAqjcWarn,
  getSeawallQr
} from '@/api/seawall.js';
import BaseForm from "./codeForm/BaseForm";
import { PrimaryDialog } from '@/components';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention';
import { reservoirRegulationApi } from '@/api/dataScreen/ReservoirRegulation';
import ResponsibleListItem from '@/components/screen_project_dialog/components/ResponsibleListItem.vue';
import superviseIndex from './components/supervise/superviseIndex.vue';
import { mapGetters, mapActions } from 'vuex';
superviseIndex;
export default {
  name: 'SeawallIndex',
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
    detailData: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      tabList: ['概化图', '二维码'],
      tabAction: 0,
      qrImg: '',
      visibleDialog: false,
      detail: {},
      contactList: [],
      dutyDetail: {},
      visible: false,
      content: null,
      sendType: 2,
      userImg: require('@/assets/images/WaterDisastersImage/seawall-user-icon.png'),
      manageInfo: {
        floodDrill: 0,
        floodPlan: 0,
        greatDanger: 0,
        reinforce: '',
        safetyConclusion: ''
      },
      RuntimeWarn: {
        danger: 0,
        flood: 0,
        patrol: 0,
        safety: 0,
        yearReport: 0
      },
      seawallWarn: {
        warn: '-',
        earlyWarn: '-'
      },
      security: { zj: 0, wy: 0 },
      weatherWarningList: [],
      rainfall_1_num: 0,
      forecastPreList: {},
      previewDate: "",
      typhoonData: [],
      healthCode: {},
      healthDialogVisible: false,
    };
  },
  components: {
    ResponsibleListItem,
    PrimaryDialog,
    superviseIndex,
    BaseForm
  },
  computed: {
    ...mapGetters(['SeawallDrillData', 'TyphoonVal']),
  },
  mounted() {
    this.getSeawallDetail();
    this.getContactList();
    this.getSelectDuty();
    this.getSelectManageInfo();
    this.getSelectRuntimeWarn();
    this.getSelectWarn();
    this.getWeatherWarning();
    this.getRainfallData();
    this.getRainfallForecastData();
    this.getSeawallTyphoonDif();
    this.getAqjcWarning()
    this.setSeawallActive(this.detailData)
  },
  methods: {
    ...mapActions(["setSeawallActive"]),
    weightsConfig(code) {
      this.healthDialogVisible = true;
      this.healthCode = { code: code };
    },
    //关闭弹窗
    close() {
      this.healthDialogVisible = false;
    },
    // 安全监测
    async getAqjcWarning() {
      let opt = {
        date: this.SeawallDrillData.showTime,
        seawallCode: this.code,
        fcstId: this.TyphoonVal ? this.TyphoonVal : ''
      }
      let res = await getAqjcWarn(opt);
      if (res.code === 0) {
        this.security = res.data;
      }
    },
    // 海塘预警
    async getSelectWarn() {
      let opt = {
        time: this.SeawallDrillData.showTime,
        fcstId: this.TyphoonVal ? this.TyphoonVal : ''
      }
      let res = await getSeawallSelectWarn(opt, this.code);
      if (res.code == 0) {
        if (res.data && res.data.warn) {
          this.seawallWarn.warn = this.disposeWarn(res.data.warn);
        }
        if (res.data && res.data.earlyWarn) {
          this.seawallWarn.earlyWarn = this.disposeWarn(res.data.earlyWarn);
        }
      }
    },
    // 新增切换二维码扫描
    async checkTab(index) {
      this.tabAction = index;
      if (index === 1) {
        let opt = {
          seawallCode: this.code,
          fcstId: this.TyphoonVal ? this.TyphoonVal : '',
          time: this.SeawallDrillData.showTime ? this.SeawallDrillData.showTime : "",
        }
        let res = await getSeawallQr(opt);
        if (res.code === 0) {
          this.qrImg = 'data:image/png;base64,' + res.data;
        }
      }
    },
    async getSeawallTyphoonDif() {
      let res = await seawallTyphoonDif({ lat: this.lat, lng: this.lng });

      if (res.code == 0) {
        this.typhoonData = res.data.map((item) => {
          let points = JSON.parse(item.points);
          item['distance'] = points[points.length - 1].distance;
          item['power'] = points[points.length - 1].power;
          return item;
        });
      }
    },
    openSupervise(str, data) {
      if (str == '运行报警' && data == 1) {
        this.visibleDialog = true;
        return false;
      }
      if (str == '海塘预警' && data != '正常') {
        this.visibleDialog = true;
        return false;
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
    async getSeawallDetail() {
      let res = await getSeawallProjects({
        code: this.code,
        type: '海塘'
      });
      if (res.code == 0) {
        this.detail = res.data[0];
      }
    },
    async getContactList() {
      let contactList = [];
      reservoirRegulationApi.getThreeCharge(this.code).then((res) => {
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
    async getSelectDuty() {
      let res = await getSeawallSelectDuty(this.code);
      if (res.code == 0) {
        this.dutyDetail = res.data;
      }
    },
    async getSelectManageInfo() {
      let res = await getSeawallSelectManageInfo({
        projectCode: this.code
      });
      if (res.code == 0) {
        this.manageInfo = res.data;
      }
    },
    async getSelectRuntimeWarn() {
      let res = await getSeawallselectRuntimeWarn({
        projectCode: this.code
      });
      if (res.code == 0) {
        this.RuntimeWarn = res.data[0];
      }
    },
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
    getRainfallData() {
      disasterPreventionApi.realtimeRainfallByAreaName({ areaName: '鹿城区' }).then((res) => {
        if (res.code == 0) {
          this.rainfall_1_num = res.data.rainfall_1;
        }
      });
    },
    getRainfallForecastData() {
      disasterPreventionApi.predictRainfallByAreaName({ areaName: '鹿城区' }).then((res) => {
        if (res.code == 0) {
          this.forecastPreList = res.data;
        }
      });
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
}
.user-box {
  > div:nth-child(1) {
    padding-bottom: 10px;
    font-size: 14px;
    color: #00baff;
  }
  > div:nth-child(2) {
    width: 186px;
    border-radius: 4px;
    padding: 6px;
    border: 1px solid rgba(0, 193, 255, 0.2);
    display: flex;
    justify-content: space-between;
    .user-img {
      width: 64px;
      height: 64px;
      img {
        width: 100%;
      }
    }
    .user-info {
      width: calc(100% - 70px);
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      color: rgba(255, 255, 255, 0.2);
      font-size: 14px;
      .user-name {
        color: #ffffff;
        font-size: 16px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        span {
          &.user-note {
            background: url(~assets/images/WaterDisastersImage/seawall-user-lx.png) no-repeat;
            background-size: 100% 100%;
            width: 14px;
            height: 13px;
            cursor: pointer;
          }
        }
      }
      .user-jop {
        color: rgba(255, 255, 255, 0.6);
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}
.seawall-top-title {
  position: relative;
  color: #00baff;
  font-size: 16px;
  padding-left: 13px;
  &::after {
    content: '';
    position: absolute;
    width: 2px;
    height: 13px;
    background: #00baff;
    border-radius: 1px;
    left: 5px;
    top: 1px;
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
.overview {
  width: 863px;
  .overview-box {
    padding-top: 16px;
    .overview-img {
      position: absolute;
      top: 240px;
      .overview-img-box {
        background: url(~assets/images/WaterDisastersImage/seawall-overview-icon.png) no-repeat;
        background-size: 100% 100%;
        width: 350px;
        height: 220px;
        margin-top: 16px;
      }
      .qrcode_box {
        width: 350px;
        height: 220px;
        margin-top: 16px;
        img {
          margin-left: 20px;
          width: 220px;
          height: 220px;
        }
      }
    }
    .basic-info {
      width: calc(100% - 35px - 378px);
      .basic-info-box {
        margin-top: 16px;
        height: 236px;
        > div {
          flex-direction: column;
          > div {
            span {
              font-size: 16px;
              &:first-child {
                color: #00baff;
              }
              &:last-child {
                color: #ffffff;
              }
            }
          }
          &:first-child {
            width: 180px;
          }
          &:last-child {
            width: calc(100% - 185px);
          }
        }
      }
    }
    .danger-tap {
      display: flex;
      height: 32px;
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
  }
}
.operation {
  width: 359px;
  .operation-box {
    padding-top: 16px;
    .operation-left {
      width: 186px;
      .operation-left-content {
        height: 226px;
        margin-top: 21px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
      }
    }
    .operation-right {
      width: 160px;
      .operation-right-content {
        margin-top: 45px;
        height: 200px;
        padding: 10px;
        box-sizing: border-box;
        border-radius: 4px;
        border: 1px solid rgba(0, 193, 255, 0.2);
        .right-content-box {
          background-color: rgba(11, 16, 22, 0.69);
          height: 100%;
          padding: 12px 8px 8px;
          box-sizing: border-box;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          > div {
            span {
              &:first-child {
                color: rgba(255, 255, 255, 0.78);
              }
              &:last-child {
                color: rgba(0, 186, 255, 1);
              }
            }
          }
        }
      }
    }
  }
}
.duty-alarm {
  margin-top: 32px;
  .duty {
    width: 585px;
    .duty-box {
      margin-top: 24px;
      flex-wrap: wrap;
      .duty-user-box {
        margin-bottom: 16px;
      }
    }
  }
  .alarm {
    width: 640px;
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
  background: center/100% url('../../../../../../../../assets/images/setting-btn.png') no-repeat;
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
<style lang="less">
#qrcode {
  display: inline-block;
  img {
    width: 132px;
    height: 132px;
    background-color: #fff; //设置白色背景色
    padding: 6px; // 利用padding的特性，挤出白边
  }
}
</style>
