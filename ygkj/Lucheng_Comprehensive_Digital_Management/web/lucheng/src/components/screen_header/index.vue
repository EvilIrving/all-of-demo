<template>
  <div class="mian-header" v-show="!qiankunModule">
    <div class="main-header__content">
      <div class="main-header__content--menu">
        <div class="main-header-left">
          <img src="~assets/images/ScreenAsset/header-address.png" alt="" />
          <span class="address-name">鹿城区</span>
          <span class="address-weather">{{ weather.wpname }}</span>
          <span>{{ weather.minTemp }}～{{ weather.maxTemp }}</span>
          <!-- <HeaderInfo /> -->
          <span class="address-line"></span>
          <!-- <HeaderDate /> -->
        </div>
        <div class="mian-header-center">
          <img src="~assets/images/ScreenAsset/header-txt.png" alt="" />
        </div>
        <div class="main-header-right">
          <div class="main-header-date">
            <HeaderDate />
          </div>
          <!-- <el-popover
            class="mian-popover"
            popper-class="mian-popover-box"
            v-model="actionPointBtnActive"
            placement="bottom-end"
            trigger="hover"
          >
            <div class="mian-header-list">
              <div @click="switchBigScene('waterDisasterCustom')">海塘防潮评估</div>
              <div @click="switchBigScene('scheduleMapProject')">灾害防御调度</div>
              <div @click="switchBigScene('ourPujiang')">戍浦江防洪调度</div>
            </div>
            <div class="mian-header-item" slot="reference" @click="handlePointBtnActive = true">
              <span>{{ BigSceneName }}</span>
              <span class="down-icon"></span>
            </div>
          </el-popover> -->
          <div class="go-home" @click="goHome"></div>
          <el-badge
            v-if="warnShow"
            :hidden="currentWarningCount <= 0"
            :value="currentWarningCount"
            :max="99"
            class="warning-badge"
          >
            <div class="notification" />
          </el-badge>
        </div>
      </div>
    </div>
    <police-dialog :dialogVisible="dialogVisible" @handleClose="handleClose" />
    <WorkplaceDialog :visiable="workplaceVisible" @close="workplaceVisible = false" />
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import policeDialog from './components/policeDialog.vue';
import HeaderDate from './components/HeaderDate.vue';
import WorkplaceDialog from '../screen_workplace/index.vue';
import actions from '../../actions';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention';

export default {
  name: 'ScreenHeader',
  props: {},
  components: { policeDialog, WorkplaceDialog, HeaderDate },
  inject: ['reload'],
  data() {
    return {
      actionPointBtnActive: false,
      warnShow: true,
      collapse: false,
      workplaceVisible: false,
      dialogVisible: false,
      menuList: [
        { title: '水灾害防御', path: '/waterDisasterCustom?large=true&isBigScreen=true&bsType=waterDisaster' },
        { title: '水资源保障', path: '/waterDisasterCustom?large=true&isBigScreen=true&bsType=waterResource' },
        { title: '河湖库保护', path: '/waterDisasterCustom?large=true&isBigScreen=true&bsType=riverProtect' },
        { title: '水发展规划', path: '/waterDisasterCustom?large=true&isBigScreen=true&bsType=waterDevelop' },
        { title: '水事务监管', path: '/waterDisasterCustom?large=true&isBigScreen=true&bsType=waterAffair' },
        { title: '水政务协同', path: '/waterDisasterCustom?large=true&isBigScreen=true&bsType=waterGovernment' },
        { title: '综合一张图', path: '/hydraulicAmap' }
      ],
      timer: null,
      weather: {},
      handlePointBtnActive: false
    };
  },
  created() {
    let bsType = this.$route.query.bsType;
    this.SetBsName(this.BsNameObj[bsType]);
  },
  mounted() {
    // this.getWarningList();
    this.timer = setInterval(() => {
      // this.getWarningList();
    }, 15000);
    //微应用时监听主应用发布的消息
    this.getWeatherInfo();
    if (this.qiankunModule) {
      this.SetCollapse(true);
      actions.onGlobalStateChange((state) => {});
    }
    // if (this.$route.name == 'waterDisasterCustom') {
    //   this.SetBsName('海塘防潮评估');
    //   this.setBigSceneName('海塘防潮评估');
    //   this.setSimple(true);
    // } else if(this.$route.name == 'ourPujiang') {
    //   this.SetBsName('戍浦江防洪调度');
    //   this.setBigSceneName('戍浦江防洪调度');
    //   this.setSimple(true);
    // } else {
    //   this.SetBsName('灾害防御调度');
    //   this.setBigSceneName('灾害防御调度');
    //   this.setSimple(false);
    // }
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  watch: {
    '$store.state.warnShow': function (val, oldval) {
      this.warnShow = val;
    }
  },
  computed: {
    ...mapGetters([
      'warningListShow',
      'currentWarningList',
      'currentWarningCount',
      'previewModule',
      'formatterCurrentTime',
      'formatterPreStartTime',
      'AffairId',
      'BigSceneName',
      'BsNameObj'
    ]),
    /**
     * 当前路由位于菜单配置的项数
     * @description 路由配置未使用二级嵌套路由故如此
     */
    currentRoute() {
      let currentRoute = null,
        currentIndex = 0;
      const bsType = this.$route.query.bsType;
      //对应路由的菜单 要和菜单列表对应
      const arr = ['waterDisaster', 'waterResource', 'riverProtect', 'waterDevelop', 'waterAffair', 'waterGovernment'];
      if (bsType && arr.includes(bsType)) {
        currentIndex = arr.indexOf(bsType);
      } else {
        this.menuList.forEach((route, index) => {
          let find = this.$route.matched.find((item) => item.path == route.path);
          if (find) {
            currentRoute = find;
            currentIndex = index;
          }
        });
      }
      return currentIndex;
    },
    currentTime() {
      let time = this.formatterCurrentTime();
      return time != 'Invalid date' ? time : '请选择时间';
    },
    //判断微应用模式还是直接访问模式
    qiankunModule() {
      return window.__POWERED_BY_QIANKUN__;
    }
  },
  methods: {
    ...mapActions([
      'showWarningList',
      'hideWarningList',
      'getWarningList',
      'SetComponentLoading',
      'SetCollapse',
      'SetCheckedArr',
      'SetBsName',
      'setSimple',
      'setBigSceneName'
    ]),
    switchBigScene(sceneName) {
      switch (sceneName) {
        case 'scheduleMapProject':
          this.SetBsName('灾害防御调度');
          this.setBigSceneName('灾害防御调度');
          this.setSimple(false);
          break;
        case 'ourPujiang':
          this.SetBsName('戍浦江防洪调度');
          this.setBigSceneName('戍浦江防洪调度');
          break;
        default:
          this.SetBsName('海塘防潮评估');
          this.setBigSceneName('海塘防潮评估');
          this.setSimple(true);
          break;
      }
      this.$router.push(sceneName);
    },
    async getWeatherInfo(areaName = '鹿城区') {
      disasterPreventionApi.weatherForecast({ areaName }).then((res) => {
        if (res && res.code === 0) {
          this.weather = {
            wpname: res.data[0].type,
            minTemp: res.data[0].wd.split("-")[0],
            maxTemp: res.data[0].wd.split("-")[1],
          }
        }
      });
    },
    goHome() {
      window.open('https://slgh.lucheng.gov.cn/lucheng/');
    },
    handleAlertCilck() {
      if (this.warnShow) {
        if (this.warningListShow) {
          this.hideWarningList();
        } else {
          this.showWarningList();
        }
      }
    },
    handleClose() {
      this.dialogVisible = false;
    },
    handleCollapseClick() {
      this.collapse = !this.collapse;
      this.SetCollapse(this.collapse);
      this.$emit('change', this.collapse);
    },
    hanleMenuClick(index) {
      this.SetComponentLoading(true);
      this.SetCheckedArr([]);
      this.$router.push(this.menuList[index].path);
      const bsType = this.$route.query.bsType;
      this.SetBsName(this.BsNameObj[bsType]);
      this.reload();
    },
    handleMenuArrowClick(next = 1) {
      this.$router.push(this.menuList[this.currentRoute + next].path);
    }
  }
};
</script>

<style lang="scss">
@import './style';
.warning-badge {
  .el-badge__content {
    top: 10px;
    right: 44%;
    background-color: red;
  }
}
.mian-popover-box.el-popover {
  background-color: rgba($color: #031a2b, $alpha: 0.9);
  box-shadow: none;
  border-color: transparent;
  width: 190px !important;
  border-image: linear-gradient(180deg, rgba(35, 227, 251, 1), rgba(35, 157, 239, 1)) 1 1;
  position: relative;
  .popper__arrow {
    left: 140px !important;
  }
}
</style>

<style lang="scss" scoped>
.mian-header-list {
  > div {
    font-size: 24px;
    background: linear-gradient(180deg, #5de6fb 0%, #02a4ed 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    font-family: 'YouYaBiaoTiHei';
    text-align: center;
    cursor: pointer;
    margin: 5px 0;
  }
}
.mian-header-item {
  margin-top: 8px;
  margin-right: 30px;
  font-size: 36px;
  width: 347px;
  height: 62px;
  background: url(~assets/images/ScreenAsset/reference-icon.png) no-repeat;
  background-size: 100% 100%;
  outline: 0;
  line-height: 62px;
  text-align: center;
  position: relative;
  > span:first-child {
    background: linear-gradient(180deg, #5de6fb 0%, #02a4ed 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    font-family: 'YouYaBiaoTiHei';
  }
  .down-icon {
    width: 28px;
    height: 28px;
    background: url(~assets/images/ScreenAsset/down-icon.png) no-repeat;
    background-size: 100% 100%;
    position: absolute;
    right: 20px;
    top: 50%;
    margin-top: -12px;
  }
}
.go-home {
  width: 50px;
  height: 50px;
  background: url(~assets/images/ScreenAsset/go-home-icon.png) no-repeat;
  background-size: 100% 100%;
  margin-right: 20px;
  // position: relative;
  // top: 6px;
  cursor: pointer;
}
</style>
