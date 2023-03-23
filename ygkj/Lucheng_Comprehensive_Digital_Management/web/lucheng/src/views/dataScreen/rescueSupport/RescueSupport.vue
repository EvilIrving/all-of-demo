<!--
 * @Author: hanyu
 * @LastEditTime: 2021-01-27 15:46:00
 * @Description: 抢险支持大屏
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\rescueSupport\RescueSupport.vue
-->
<template>
  <div class="bigScreen">
    <div class="bigScreen_map">
      <screen-map
        ref="map"
        class="screen_map"
        :typeList="rightTabList"
        @showDispatchTab="showDispatchTab"
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
        @click="bigScreenGo('/jointDispatch')"
      ></i>
      <!-- <i
        class="bigScreen_next icon-screen-next"
        @click="bigScreenGo('/floodAnalysis')"
      ></i> -->
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
    <div class="bigScreen_left">
      <div
        v-for="(item, index) in leftBtnList"
        :key="index"
        :class="['btn_item', item.selected ? 'active' : '']"
        @click="clickLeftBtn(index)"
      >
        <span>{{ item.name }}</span>
      </div>
    </div>
    <div class="bigScreen_right">
      <transition name="fade" v-on:after-leave="transitionDone">
        <div class="sidebar" v-show="barshow">
          <div class="right-tab">
            <span v-for="(item, index) in rightTabList" :key="index">
              <span
                @click="sidebarTabChange(index)"
                :class="activeSidebar == item.name ? 'active' : ''"
                >{{ item.name }}</span
              >
            </span>
          </div>
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
    <!-- <div class="bigScreen_bottom">
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
    </div> -->
    <div class="bigScreen_bottom-left">
      <weather-data class="weather_data"></weather-data>
    </div>
    <div class="bigScreen_bottom-right">
      <div class="btn_box">
        <i class="icon-screen-br-btn"></i>
        <span>仓库统计</span>
      </div>
      <div class="img"></div>
    </div>
    <div class="bigScreen_legend">
      <div v-show="bottomBtnList[0].selected">
        <div class="title">雨情</div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_0.png" />
          <span> 无降雨 </span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_10.png" />
          <span> 0-10 </span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_10_25.png" />
          <span> 10-25 </span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_25_50.png" />
          <span> 25-50 </span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_50_100.png" />
          <span> 50-100 </span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_100_250.png" />
          <span> 100-250 </span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_250.png" />
          <span> >250 </span>
        </div>
      </div>
      <div v-show="bottomBtnList[1].selected">
        <div class="title">水位</div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/map_unnormal.png" />
          <span> 正常水位 </span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/map_normal.png" />
          <span> 超警水位 </span>
        </div>
      </div>
      <div>
        <div class="title">抢险</div>
        <div class="legend_item">
          <img src="../../../assets/images/map_fxck_big.png" />
          <span> 防汛仓库 </span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/map_qxdu_big.png" />
          <span> 抢险队伍 </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
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
      currentPage: "/rescueSupport",
      pageOptions: [
        { value: "/floodJudge", label: "防汛研判" },
        { value: "/jointDispatch", label: "联合调度" },
        { value: "/rescueSupport", label: "抢险支持" },
        // { value: "/floodAnalysis", label: "洪涝分析" },
      ],
      barshow: true,
      showBtn: false,
      sidebarTabArr: [
        {
          label: "防汛仓库",
          value: 0,
        },
        {
          label: "防汛队伍",
          value: 1,
        },
      ],
      activeSidebar: "防汛仓库",
      timer: null,
      nowDate: {
        date: new Date().format("hh:mm:ss"),
        year: new Date().format("yyyy"),
        month: new Date().format("MM"),
        day: new Date().format("dd"),
      },
      bottomBtnList: [
        {
          selected: false,
          name: "雨情",
          icon1: "icon-btn_rain1",
          icon2: "icon-btn_rain2",
          url: "/rainSituation",
        },
        {
          selected: false,
          name: "水情",
          icon1: "icon-btn_water1",
          icon2: "icon-btn_water2",
          url: "/waterSituation",
        },
        {
          selected: false,
          name: "其他",
          icon1: "icon-btn_else1",
          icon2: "icon-btn_else2",
        },
      ],
      leftBtnList: [
        {
          selected: true,
          name: "防汛仓库",
          url: "/floodWarehouse",
        },
        {
          selected: false,
          name: "抢险队伍",
          url: "/floodTeam",
        },
      ],
      dispathSideList: [
        {
          selected: true,
          name: "人员调度",
          url: "/teamDispatch",
        },
        {
          selected: false,
          name: "物资调度",
          url: "/warehouseDispatch",
        },
      ],
      rightTabList: [],
    };
  },
  mounted() {
    this.getNowTime();
    this.rightTabList.push(this.leftBtnList[0]);
    this.sidebarTabChange(0);
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
      if (this.bottomBtnList[index].name == "其他") return;
      this.bottomBtnList[index].selected = !this.bottomBtnList[index].selected;
      if (this.checkTab(this.bottomBtnList[index])) {
        this.rightTabList.push(this.bottomBtnList[index]);
        this.sidebarTabChange(this.rightTabList.length - 1);
      }
    },
    // 左侧按钮
    clickLeftBtn(index) {
      // if (this.leftBtnList[index].name == "防汛仓库") return;
      this.leftBtnList[index].selected = !this.leftBtnList[index].selected;
      if (this.checkTab(this.leftBtnList[index])) {
        this.rightTabList.push(this.leftBtnList[index]);
        this.sidebarTabChange(this.rightTabList.length - 1);
      }
    },
    checkTab(item) {
      for (let i = 0; i < this.rightTabList.length; i++) {
        if (item.name == this.rightTabList[i].name) {
          this.rightTabList.splice(i, 1);
          this.sidebarTabChange(this.rightTabList.length - 1);
          return false;
        }
      }
      return true;
    },
    sidebarTabChange(index) {
      this.activeSidebar = this.rightTabList[index].name;
      this.$router.push(this.rightTabList[index].url);
    },
    showDispatchTab(flag) {
      this.rightTabList = [];
      this.rightTabList.push(this.dispathSideList[0]);
      if (flag) {
        this.rightTabList.push(this.dispathSideList[1]);
      }
      this.leftBtnList.map((item) => {
        item.selected = false;
      });
      this.$router.push(this.rightTabList[0].url);
    },
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
  .bigScreen_map {
    width: 100%;
    height: 100%;
    .screen_map {
      width: 100%;
      height: 100%;
    }
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
    top: 30%;
    left: 30px;
    color: #fff;
    .btn_item {
      height: 75px;
      width: 155px;
      background: url("../../../assets/images/btn_left1.png") no-repeat center;
      background-size: 100% 100%;
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
      white-space: nowrap;
      overflow: auto;
      width: 100%;
      @include flexbox();
      &::-webkit-scrollbar {
        display: none;
      }
      > span > span {
        display: inline-block;
        width: 120px;
        height: 40px;
        line-height: 40px;
        text-align: center;
        cursor: pointer;
        color: #4898fb;
        background: url(../../../assets/images/bigScreen-big-tab.png) no-repeat;
        background-size: 100% 100%;
        margin-right: 16px;
        &.active {
          color: #a6e9fb;
          background: url(../../../assets/images/bigScreen-big-tab-check.png)
            no-repeat;
          background-size: 100% 100%;
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
      background: url("../../../assets/images/screen_bottom_right.png")
        no-repeat center;
      background-size: 100% 100%;
    }
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
        vertical-align: middle;
        margin-right: 5px;
      }
    }
  }
}
</style>
<style lang="scss" scoped>
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