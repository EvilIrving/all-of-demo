<!--
 * @Description: 洪涝分析大屏
-->
<template>
  <div class="bigScreen">
    <div class="bigScreen_map">
      <screen-map
        ref="map"
        class="screen_map"
        :pageType="pageType"
      ></screen-map>
    </div>
    <div class="bigScreen_title">
      <span>防汛减灾调度系统·</span>
      <el-select
        v-model="currentPage"
        placeholder="请选择"
        class="page-select"
        popper-class="page-select-popper"
        @change="bigScreenGo"
      >
        <el-option
          v-for="item in pageOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
    </div>
    <div class="bigScreen_switch">
      <i
        class="bigScreen_prev icon-screen-prev"
        @click="bigScreenGo('/rescueSupport')"
      ></i>
      <i class="bigScreen_next icon-screen-next-dis"></i>
    </div>
    <i class="bigScreen_back icon-back-home" @click="bigScreenGo('/')"></i>
    <div class="bigScreen_time">
      <div class="date_left">{{ nowDate.date }}</div>
      <div class="date_right">
        <div>{{ nowDate.year }}年</div>
        {{ nowDate.month }}月{{ nowDate.day }}日
      </div>
    </div>
    <!-- <div class="bigScreen_left">
      <div class="top-menu" v-if="topMenu.length > 0">
        <div
          v-for="(item, index) in topMenu"
          :key="index"
          :class="['menu-icon', item.icon]"
          @click="changeMenu(item)"
        >
          <span class="name-tag" v-if="item.tag">{{ item.name }}</span>
        </div>
      </div>
      <div class="bottom-menu" v-if="bottomMenu.length > 0">
        <div
          v-for="(item, index) in bottomMenu"
          :key="index"
          :class="['menu-icon', item.icon]"
          @click="changeMenu(item)"
        >
          <span class="name-tag" v-if="item.tag">{{ item.name }}</span>
        </div>
      </div>
    </div> -->
    <div class="bigScreen_right" v-if="!noSide">
      <transition name="fade" v-on:after-leave="transitionDone">
        <div class="sidebar" v-show="barshow">
          <!-- <div class="right-tab">
            <template v-if="pageType == 'water'">
              <span
                v-for="(item, index) in sidebarTabArr"
                :key="index"
                @click="sidebarTabChange(item)"
                :class="activeSidebar == item.name ? 'active' : ''"
              >{{ item.name }}</span>
            </template>
          </div> -->
          <router-view></router-view>
          <div class="switch-btn" @click="toggleSideBar()"></div>
        </div>
      </transition>
      <div
        class="switch-btn show-btn"
        v-show="showBtn"
        @click="toggleSideBar()"
      ></div>
    </div>
    <div class="bigScreen_bottom">
      <div class="btn_box">
        <div
          v-for="(item, index) in bottomBtnList"
          :key="index"
          :class="['btn_item', item.selected ? 'active' : '']"
          @click="clickBottomBtn(index)"
        >
          <i v-show="!item.selected" :class="item.icon1"></i>
          <i v-show="item.selected" :class="item.icon2"></i>
          <span>{{ item.name }}</span>
        </div>
      </div>
    </div>
    <div class="bigScreen_bottom-left">
      <weather-data class="weather_data"></weather-data>
    </div>
    <div class="bigScreen_bottom-right">
      <div class="btn_box">
        <i class="icon-screen-br-btn"></i>
        <span>洪涝影响</span>
      </div>
      <div class="img"></div>
    </div>

    <div class="bigScreen_legend">
      <div v-show="bottomBtnList[0].selected">
        <div class="title">积水点</div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/legend-stagnant-normal.png" />
          <span>正常</span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/legend-stagnant-warn.png" />
          <span>预警</span>
        </div>
      </div>
      <div v-show="bottomBtnList[0].selected">
        <div class="title">水位</div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_10_25.png" />
          <span>正常水位</span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_50_100.png" />
          <span>超汛限水位</span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_250.png" />
          <span>超警水位</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import { rainApi, waterApi } from "@/api/dataScreen/floodJudge/index";

import ScreenMap from "./components/ScreenMap";
import WeatherData from "../components/WeatherData";
export default {
  name: "RescueSupport",
  props: {},
  components: {
    ScreenMap,
    WeatherData,
  },
  data() {
    return {
      currentPage: "/floodAnalysis",
      pageOptions: [
        { value: "/floodJudge", label: "防汛研判" },
        { value: "/jointDispatch", label: "联合调度" },
        { value: "/rescueSupport", label: "抢险支持" },
        // { value: "/floodAnalysis", label: "洪涝分析" },
      ],
      loading: false,
      barshow: true,
      showBtn: false,
      timer: null,
      nowDate: {
        date: new Date().format("hh:mm:ss"),
        year: new Date().format("yyyy"),
        month: new Date().format("MM"),
        day: new Date().format("dd"),
      },
      bottomBtnList: [
        {
          selected: true,
          name: "积水分析",
          icon1: "icon-btn_rain1",
          icon2: "icon-btn_rain2",
          type: "rain",
        },
        {
          selected: false,
          name: "影响分析",
          icon1: "icon-btn_water1",
          icon2: "icon-btn_water2",
          type: "water",
        },
      ],
      rainTopMenu: [
        {
          icon: "",
          name: "积水点",
          selected: false,
          icon: "menu-ssyq",
          url: "/stagnantPoint",
          tag: false,
        },
        {
          icon: "",
          name: "未知",
          selected: false,
          tag: false,
          icon: "menu-ssyq",
        },
      ],

      waterbottomMenu: [
        {
          icon: "",
          name: "未知",
          selected: false,
          tag: false,
          icon: "menu-ssyq",
        },
        {
          icon: "",
          name: "未知",
          selected: false,
          tag: false,
          icon: "menu-ssyq",
        },
      ],

      topMenu: [],
      bottomMenu: [],
      pageType: "rain",
      countData: {
        cjj: "-",
        cbz: "-",
        cxx: "-",
        one: "-",
        three: "-",
      },
      noSide: false,
    };
  },
  mounted() {
    this.getNowTime();
    this.topMenu = this.rainTopMenu;
    this.topMenu[0].tag = true;
    this.$router.push(this.topMenu[0].url);
    //this.getCount();
  },
  methods: {
    bigScreenGo(path) {
      this.$router.push(path);
    },
    toggleSideBar() {
      this.showBtn = false;
      this.barshow = !this.barshow;
    },
    transitionDone(el, done) {
      this.showBtn = true;
    },
    // 获取当前时间
    getNowTime() {
      this.timer = setInterval(() => {
        this.nowDate.date = new Date().format("hh:mm:ss");
      }, 1000);
    },
    // 底部按钮
    clickBottomBtn(index) {
      this.noSide = false;
      this.bottomBtnList.map((item) => {
        item.selected = false;
      });
      this.bottomBtnList[index].selected = true;

      this.pageType = this.bottomBtnList[index].type;

      if (this.bottomBtnList[index].type == "rain") {
        this.topMenu = this.rainTopMenu;
        this.bottomMenu = [];
      } else if (this.bottomBtnList[index].type == "water") {
        this.topMenu = [];
        this.bottomMenu = this.waterbottomMenu;
      }
    },
    sidebarTabChange(item) {
      this.activeSidebar = item.name;
      this.$router.push({
        path: item.url,
        query: {
          id: item.id,
        },
      });
      this.$refs.map.waterPoint(item.pointId);
    },
    changeMenu(info) {
      if (info.url == "/waterHistory") {
        this.noSide = false;
        this.sidebarTabArr = this.waterLevelHistoryTab;
        this.sidebarTabChange(this.sidebarTabArr[0]);
        this.activeSidebar = "大中型水库";
      } else {
        if (info.name == "未知") {
          this.noSide = true;
        } else {
          this.$router.push(info.url);
          this.noSide = false;
        }
      }
      this.topMenu.map((item) => {
        item.tag = false;
      });
      this.bottomMenu.map((item) => {
        item.tag = false;
      });
      info.tag = info.name != "未知";
      //降雨分析-实时雨情-地图打点
    },
    showMap() {
      alert(1);
    },
    // async getCount() {
    //   let res = await rainApi.count({
    //     hours: 1
    //   });
    //   if (res) {
    //     this.countData.one = res.data;
    //   }
    //   let res2 = await rainApi.count({
    //     hours: 3
    //   });
    //   if (res2) {
    //     this.countData.three = res2.data;
    //   }
    //   let res3 = await waterApi.count();
    //   if (res3) {
    //     this.countData.cjj = res3.data.warnNum;
    //     this.countData.cbz = res3.data.guaranteeNum;
    //     this.countData.cxx = res3.data.limitNum;
    //   }
    // }
  },
  beforeDestroy() {
    this.timer = null;
  },
};
</script>

<style lang='scss' scoped>
$sidebarHeight: 424px;
.bigScreen {
  position: relative;
  height: 100%;
  width: 100%;
  background: url("../../../assets/images/bigScreen_bg.gif") no-repeat center;
  background-size: 100% 100%;
  overflow: hidden;
}
.bigScreen_map {
  width: 100%;
  height: 100%;
}
.bigScreen_title {
  position: absolute;
  top: 24px;
  left: 30px;
  font-size: 32px;
  color: #ffffff;
  text-shadow: 0 0 6px #a6dff6;
}

.bigScreen_switch {
  position: absolute;
  top: 76px;
  left: 30px;
  font-size: 32px;
  color: #ffffff;
  text-shadow: 0 0 6px #a6dff6;
  > i {
    cursor: pointer;
    margin-right: 16px;
  }
}
.bigScreen_back {
  position: absolute;
  top: 76px;
  right: 30px;
  cursor: pointer;
}
.bigScreen_time {
  position: absolute;
  top: 30px;
  right: 30px;
  color: #befcfd;
  @include flexbox();
  @include flexAC();
  .date_left {
    font-size: 32px;
    margin-right: 16px;
  }
  .date_right {
    font-size: 12px;
    & > div {
      padding-bottom: 5px;
    }
  }
}
.bigScreen_left {
  position: absolute;
  top: 20%;
  left: 0;
  color: #fff;
  .btn_item {
    height: 75px;
    width: 155px;
    background: url("../../../assets/images/btn_left1.png") no-repeat center;
    position: relative;
    cursor: pointer;
    margin-bottom: 40px;
    span {
      position: absolute;
      font-size: 14px;
      font-weight: 500;
      background: linear-gradient(to right, #6ae1ff, #4297ff);
      -webkit-background-clip: text;
      color: transparent;
      top: 17px;
      right: 27px;
    }
    &.active {
      background: url("../../../assets/images/btn_left2.png") no-repeat center;
      background-size: 100% 100%;
    }
  }
  .top-menu {
    height: 128px;
    width: 60px;
    background: url("../../../assets/images/menu-top.png") no-repeat center;
    background-size: 100% 100%;
    padding-top: 4px;
  }
  .bottom-menu {
    height: 128px;
    width: 60px;
    background: url("../../../assets/images/menu-bottom.png") no-repeat center;
    background-size: 100% 100%;
  }
  .menu-icon {
    position: relative;
    height: 58px;
    width: 58px;
    cursor: pointer;
  }
  .name-tag {
    position: absolute;
    left: 50px;
    top: 12px;
    display: inline-block;
    width: 85px;
    height: 40px;
    line-height: 40px;
    text-align: center;
    background: url("../../../assets/images/menu-tag.png") no-repeat center;
    background-size: 100% 100%;
    color: #64a6f6;
    font-size: 14px;
  }
  .menu-ssyl {
    background: url("../../../assets/images/ssyl.png") no-repeat center;
    background-size: 100% 100%;
  }
  .menu-ssyq {
    background: url("../../../assets/images/ssyq.png") no-repeat center;
    background-size: 100% 100%;
  }
  .menu-dmyl {
    background: url("../../../assets/images/dmyl.png") no-repeat center;
    background-size: 100% 100%;
  }
  .menu-yqjb {
    background: url("../../../assets/images/yqjb.png") no-repeat center;
    background-size: 100% 100%;
  }
  .menu-tsfx {
    background: url("../../../assets/images/tsfx.png") no-repeat center;
    background-size: 100% 100%;
  }
  .menu-lsjz {
    background: url("../../../assets/images/lsjz.png") no-repeat center;
    background-size: 100% 100%;
  }
  .menu-ybyl {
    background: url("../../../assets/images/ybyl.png") no-repeat center;
    background-size: 100% 100%;
  }
}
.bigScreen_right {
  position: absolute;
  top: 25%;
  right: 30px;
  color: #fff;
  // 侧边栏
  .sidebar {
    width: 380px;
    height: $sidebarHeight;
    position: absolute;
    right: 0;
    top: 0;
    z-index: 1900;
    .switch-btn {
      width: 45px;
      height: $sidebarHeight;
      position: absolute;
      top: 0;
      left: -38px;
      background: url(../../../assets/images/sidebar.png) no-repeat;
      background-size: 100% 100%;
      cursor: pointer;
    }
  }
  .right-tab {
    position: absolute;
    top: -67px;
    > span {
      display: inline-block;
      width: 120px;
      height: 40px;
      line-height: 40px;
      text-align: center;
      cursor: pointer;
      color: #4898fb;
      background: url(../../../assets/images/bigScreen-big-tab.png) no-repeat;
      margin-right: 6px;
      &.active {
        color: #a6e9fb;
        background: url(../../../assets/images/bigScreen-big-tab-check.png)
          no-repeat;
      }
    }
  }

  .show-btn {
    position: absolute;
    top: 0;
    right: -30px;
    width: 45px;
    height: $sidebarHeight;
    background: url(../../../assets/images/sidebar.png) no-repeat;
    background-size: 100% 100%;
    // transform: translateY(50%);
    z-index: 1900;
    cursor: pointer;
  }

  .fade-enter,
  .fade-leave-to {
    transform: translateX(100%);
  }
  .fade-enter-active,
  .fade-leave-active {
    transition: all 0.5s linear;
  }
}
.bigScreen_bottom {
  position: absolute;
  bottom: 12px;
  width: 100%;
  color: #fff;
  text-align: center;
  @include flexbox();
  @include flexJC(center);
  .btn_box {
    @include flexbox();
    .btn_item {
      @include flexbox();
      @include flexJC(center);
      margin-right: 126px;
      width: 92px;
      height: 30px;
      background: url("../../../assets/images/btn_bottom.png");
      background-size: 100% 100%;
      cursor: pointer;
      font-size: 14px;
      color: #a6dff6;
      padding-top: 8px;
      i {
        margin-right: 8px;
      }
      span {
        display: inline-block;
        padding-top: 3px;
      }
      &:last-child {
        margin-right: 0;
      }
      &.active {
        color: #fef568;
        // background: url('../../../assets/images/btn_bottom2.png');
      }
    }
  }
}
.bigScreen_bottom-left {
  position: absolute;
  bottom: 0;
  left: 15px;
  color: #fff;
  width: 430px;
  height: 220px;
  .weather_data {
  }
}
.bigScreen_bottom-right {
  position: absolute;
  bottom: 30px;
  right: 0;
  color: #fff;
  width: 430px;
  height: 180px;
  .btn_box {
    width: 101px;
    height: 26px;
    background: url("../../../assets/images/screen_bottom_right_btn.png")
      no-repeat center;
    background-size: 100% 100%;
    position: absolute;
    right: 0;
    top: 0;
    @include flexbox();
    @include flexJC(center);
    font-size: 14px;
    color: #3998f8;
    padding-top: 5px;
    span {
      display: inline-block;
      padding-top: 2px;
      padding-left: 4px;
    }
  }
  .img {
    position: absolute;
    right: 10px;
    top: 35px;
    width: 280px;
    height: 150px;
    background: url("../../../assets/images/screen_bottom_right_hlfx.png")
      no-repeat center;
    background-size: 100% 100%;
  }
}
.screen_map {
  width: 100%;
  height: 100%;
}
.bigScreen_legend {
  position: absolute;
  bottom: 150px;
  right: 480px;
  text-align: left;
  color: #a6dff6;
  font-size: 14px;
  font-weight: 500;
  width: 100px;
  // height: 0px;
  background: transparent;
  .title {
    font-weight: 500;
    color: #fff;
    padding-top: 20px;
  }
  .legend_item {
    padding-top: 10px;
    img {
      margin-right: 5px;
    }
  }
}
.bigScreen_count {
  position: absolute;
  top: 150px;
  left: 600px;
  text-align: left;
  color: #a6dff6;
  font-size: 14px;
  font-weight: 500;
  width: 207px;
  // height: 207px;
  background: transparent;
  .box {
    width: 207px;
    height: 66px;
    background: url("../../../assets/images/legend/count.png") no-repeat center;
    background-size: 100% 100%;
    padding-left: 93px;
    margin-bottom: 20px;
    & > div {
      width: 110px;
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      padding: 5px 0;
      p {
        width: 100%;
        font-size: 14px;
        color: #3a9eff;
      }
      .num {
        font-size: 22px;
        color: #bafcfe;
      }
    }
  }
}
</style>
<style lang="scss">
.bigScreen_title .page-select.el-select {
  width: 160px;
  .el-input.el-input--suffix .el-input__inner {
    font-size: 31px;
    color: #ffffff;
    text-shadow: 0 0 6px #a6dff6;
  }
  .el-input__inner {
    background-color: rgba(0, 0, 0, 0);
  }
  .el-input--suffix {
    .el-input__inner {
      padding: 0;
    }
    .el-select__caret.el-input__icon {
      color: #fff;
    }
  }
}
.el-select-dropdown.el-popper.page-select-popper {
  border: 0;
  background-color: rgba(0, 0, 0, 0.4);
  .el-select-dropdown__item.selected {
    color: #409eff;
  }
  .el-select-dropdown__item {
    color: #fff;
  }
  .el-select-dropdown__item.hover,
  .el-select-dropdown__item:hover {
    background-color: transparent;
  }
}
.el-popper[x-placement^="bottom"].page-select-popper .popper__arrow {
  border-bottom-color: transparent;
}
.el-popper[x-placement^="bottom"].page-select-popper .popper__arrow::after {
  border-bottom-color: rgba(0, 0, 0, 0.4);
}
</style>