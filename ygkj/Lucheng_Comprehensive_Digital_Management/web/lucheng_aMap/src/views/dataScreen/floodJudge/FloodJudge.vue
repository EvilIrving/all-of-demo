<!--
 * @Author: hanyu
 * @LastEditTime: 2021-02-06 17:20:20
 * @Description: 防汛研判大屏
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\floodJudge\FloodJudge.vue
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
      <i class="bigScreen_prev icon-screen-prev-dis"></i>
      <i
        class="bigScreen_next icon-screen-next"
        @click="bigScreenGo('/jointDispatch')"
      ></i>
    </div>
    <i class="bigScreen_back icon-back-home" @click="bigScreenGo('/')"></i>
    <div class="bigScreen_time">
      <div class="date_left">{{ nowDate.date }}</div>
      <div class="date_right">
        <div>{{ nowDate.year }}年</div>
        {{ nowDate.month }}月{{ nowDate.day }}日
      </div>
    </div>

    <!-- 左侧选中菜单 -->
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

    <!-- 右侧菜单 -->
    <div class="bigScreen_right" v-if="!noSide">
      <transition name="fade" v-on:after-leave="transitionDone">
        <div class="sidebar" v-show="barshow">
          <div class="right-tab">
            <!-- <template v-if="pageType == 'water'"> -->
            <template>
              <span
                v-for="(item, index) in sidebarTabArr"
                :key="index"
                @click="sidebarTabChange(item)"
                :class="activeSidebar == item.name ? 'active' : ''"
                >{{ item.name }}</span
              >
            </template>
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

    <!-- 底部菜单模块 -->
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
        <span>数据统计</span>
      </div>
      <div class="img"></div>
    </div>

    <div class="bigScreen_legend">
      <div v-show="bottomBtnList[0].selected">
        <!-- <div class="title">雨情</div> -->
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_0.png" />
          <span>无降雨</span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_10.png" />
          <span>0-10</span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_10_25.png" />
          <span>10-25</span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_25_50.png" />
          <span>25-50</span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_50_100.png" />
          <span>50-100</span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_100_250.png" />
          <span>100-250</span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/rain_250.png" />
          <span>>250</span>
        </div>
      </div>
      <div v-show="bottomBtnList[1].selected || bottomBtnList[2].selected">
        <!-- <div class="title">水位</div> -->
        <div class="legend_item">
          <img src="../../../assets/images/legend/map_unnormal.png" />
          <span>超警水位</span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/map_normal.png" />
          <span>正常水位</span>
        </div>
      </div>
    </div>

    <div class="bigScreen_count">
      <div class="box" v-show="bottomBtnList[0].selected">
        <div>
          <p>1h降雨量报警</p>
          <p class="num">{{ countData.one + 1 }}</p>
        </div>
      </div>
      <div class="box" v-show="bottomBtnList[0].selected">
        <div>
          <p>3h降雨量报警</p>
          <p class="num">{{ countData.three + 1 }}</p>
        </div>
      </div>
      <div class="box" v-show="bottomBtnList[1].selected">
        <div>
          <p>超警戒</p>
          <p class="num" style="color: #ff3525">{{ countData.cjj }}</p>
        </div>
      </div>
      <div class="box" v-show="bottomBtnList[1].selected">
        <div>
          <p>超保证</p>
          <p class="num" style="color: #ff3525">{{ countData.cbz }}</p>
        </div>
      </div>
      <div class="box" v-show="bottomBtnList[1].selected">
        <div>
          <p>超汛限</p>
          <p class="num" style="color: #ff3525">{{ countData.cxx }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { rainApi, waterApi } from "@/api/dataScreen/floodJudge/index";

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
      currentPage: "/floodJudge",
      pageOptions: [
        { value: "/floodJudge", label: "防汛研判" },
        { value: "/jointDispatch", label: "联合调度" },
        { value: "/rescueSupport", label: "抢险支持" },
        // { value: "/floodAnalysis", label: "洪涝分析" },
      ],
      barshow: true,
      showBtn: false,
      // sidebarTabArr: [
      //   {
      //     name: '大中型水库',
      //     url: '/waterHistory',
      //     id: 4,
      //     pointId: 1
      //   },
      //   {
      //     name: '流域水位',
      //     url: '/waterHistory',
      //     id: 5,
      //     pointId: 2
      //   },
      //   {
      //     name: '平原水位',
      //     url: '/waterHistory',
      //     id: 6,
      //     pointId: 3
      //   }
      // ],
      sidebarTabArr: [
        {
          name: "实时雨量",
          url: "/realRain",
        },
        {
          name: "预报雨量",
          url: "/realRainfall",
        },
        {
          name: "历史极值",
          url: "/rainHistory",
        },
        {
          name: "等面雨量",
          url: "/realRain?dmyl=true",
        },
      ],
      waterLevelTab: [
        {
          name: "大中型水库",
          url: "/waterHistory",
          id: 4,
          pointId: 1,
        },
        {
          name: "流域水位",
          url: "/waterHistory",
          id: 5,
          pointId: 2,
        },
        {
          name: "平原水位",
          url: "/waterHistory",
          id: 6,
          pointId: 3,
        },
      ],
      waterLevelHistoryTab: [
        {
          name: "大中型水库",
          url: "/waterHistory",
          id: 1,
          pointId: 1,
        },
        {
          name: "流域水位",
          url: "/waterHistory",
          id: 2,
          pointId: 2,
        },
        {
          name: "平原水位",
          url: "/waterHistory",
          id: 3,
          pointId: 3,
        },
      ],
      activeSidebar: "实时雨量",
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
          name: "降雨分析",
          icon1: "icon-btn_rain1",
          icon2: "icon-btn_rain2",
          type: "rain",
        },
        {
          selected: false,
          name: "水情分析",
          icon1: "icon-btn_water1",
          icon2: "icon-btn_water2",
          type: "water",
        },
        {
          selected: false,
          name: "工程分析",
          icon1: "icon-btn_else1",
          icon2: "icon-btn_else2",
          type: "work",
        },
      ],
      rainTopMenu: [
        {
          icon: "",
          name: "实时雨情",
          selected: false,
          icon: "menu-ssyl",
          url: "/realRain",
          tag: false,
        },
        {
          icon: "",
          name: "预报雨量",
          selected: false,
          icon: "menu-ybyl",
          url: "/forecastRain",
          tag: false,
        },
        {
          icon: "",
          name: "历史极值",
          selected: false,
          icon: "menu-lsjz",
          url: "/rainHistory",
          tag: false,
        },
      ],
      rainBottomMenu: [
        {
          icon: "",
          name: "未知",
          selected: false,
          icon: "menu-yqjb",
          tag: false,
        },
        {
          icon: "",
          name: "实时雨量",
          selected: false,
          icon: "menu-ssyq",
          url: "/realRainfall",
          tag: false,
        },
        {
          icon: "",
          name: "等面雨量分析",
          selected: false,
          icon: "menu-dmyl",
          // url: "/arealRainfall",
          url: "/realRain?dmyl=true",
          tag: false,
        },
        {
          icon: "",
          name: "态势分析",
          selected: false,
          icon: "menu-tsfx",
          // url: "/situationAnalysis",
          url: "/realRain",
          tag: false,
        },
      ],
      waterTopMenu: [
        {
          icon: "",
          name: "历史极值",
          selected: false,
          icon: "menu-lsjz",
          tag: false,
          url: "/waterHistory",
        },
        {
          icon: "",
          name: "未知",
          selected: false,
          tag: false,
          icon: "menu-yqjb",
        },
        {
          icon: "",
          name: "未知",
          selected: false,
          tag: false,
          icon: "menu-ssyq",
        },
      ],
      workBottomMenu: [
        {
          icon: "",
          name: "实时工情",
          selected: false,
          tag: false,
          icon: "menu-ssyl",
          url: "/realWork",
        },
        {
          icon: "",
          name: "可蓄水量",
          selected: false,
          tag: false,
          icon: "menu-ssyq",
          url: "/waterStorageAbility",
        },
        {
          icon: "",
          name: "纳蓄能力",
          selected: false,
          tag: false,
          icon: "menu-dmyl",
          url: "/waterStorage",
        },
        {
          icon: "",
          name: "未知",
          selected: false,
          tag: false,
          icon: "menu-yqjb",
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
    this.bottomMenu = this.rainBottomMenu;
    this.topMenu[0].tag = true;
    this.$router.push(this.topMenu[0].url);
    this.getCount();
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
    // 底部按钮点击事件
    clickBottomBtn(index) {
      this.noSide = false;
      //清空所有图层
      this.$refs.map.cleanAllLayer();
      //关闭所有菜单选中状态
      this.bottomBtnList.map((item) => {
        item.selected = false;
      });
      //选中当前点击菜单项目
      this.bottomBtnList[index].selected = true;
      this.pageType = this.bottomBtnList[index].type;

      //菜单点击的操作
      if (this.bottomBtnList[index].type == "rain") {
        this.topMenu = this.rainTopMenu;
        this.bottomMenu = this.rainBottomMenu;

        //添加右侧顶部菜单
        this.sidebarTabArr = [
          {
            name: "实时雨量",
            url: "/realRain",
          },
          {
            name: "预报雨量",
            url: "/realRainfall",
          },
          {
            name: "历史极值",
            url: "/rainHistory",
          },
          {
            name: "等面雨量",
            url: "/realRain?dmyl=true",
          },
        ];
        this.activeSidebar = "实时雨量";
      } else if (this.bottomBtnList[index].type == "water") {
        this.topMenu = this.waterTopMenu;
        this.bottomMenu = [];
        this.sidebarTabArr = [
          {
            name: "大中型水库",
            url: "/waterHistory?id=4&pointId=1",
            id: 4,
            pointId: 1,
          },
          {
            name: "流域水位",
            url: "/waterHistory?id=5&pointId=2",
            id: 5,
            pointId: 2,
          },
          {
            name: "平原水位",
            url: "/waterHistory?id=6&pointId=3",
            id: 6,
            pointId: 3,
          },
        ];
        this.activeSidebar = "大中型水库";
      } else if (this.bottomBtnList[index].type == "work") {
        this.topMenu = [];
        this.bottomMenu = this.workBottomMenu;
        //添加右侧顶部菜单
        this.sidebarTabArr = [
          {
            name: "实时工情",
            url: "/realWork",
          },
          {
            name: "可蓄水量",
            url: "/waterStorageAbility",
          },
          {
            name: "纳蓄能力",
            url: "/waterStorage",
          },
        ];
        this.activeSidebar = "实时工情";
      }
      this.sidebarTabChange(this.sidebarTabArr[0]);

      // //循环关闭所有标签
      // if (this.pageType == 'rain' || this.pageType == 'water') {
      //   this.topMenu.map(item => {
      //     item.tag = false
      //   })
      //   this.bottomMenu.map(item => {
      //     item.tag = false
      //   })
      //   if (this.pageType == 'rain') {
      //     this.$router.push(this.topMenu[0].url)
      //     this.topMenu[0].tag = true
      //   } else {
      //     this.sidebarTabArr = this.waterLevelTab
      //     this.sidebarTabChange(this.sidebarTabArr[0])
      //   }
      // } else {
      //   this.$router.push(this.bottomMenu[0].url)
      //   this.bottomMenu.map(item => {
      //     item.tag = false
      //   })
      //   this.bottomMenu[0].tag = true
      // }
    },
    sidebarTabChange(item) {
      console.log("item: ", item);
      this.activeSidebar = item.name;
      this.$router.push(item.url);
      if (item.name == "等面雨量") {
        this.$refs.map.showConstantRainfall();
      } else {
        this.$refs.map.clearRainfallLayer();
      }
      if (
        item.name == "大中型水库" ||
        item.name == "流域水位" ||
        item.name == "平原水位"
      ) {
        this.$refs.map.waterPoint(item.pointId);
      }
    },
    // 左侧点击事件
    changeMenu(info) {
      // if (info.url == '/waterHistory') {
      //   this.noSide = false
      //   this.sidebarTabArr = this.waterLevelHistoryTab
      //   this.sidebarTabChange(this.sidebarTabArr[0])
      //   this.activeSidebar = '大中型水库'
      // } else {
      //   if (info.name == '未知') {
      //     this.noSide = true
      //   } else {
      //     this.$router.push(info.url)
      //     this.noSide = false
      //   }
      // }
      // this.topMenu.map(item => {
      //   item.tag = false
      // })
      // this.bottomMenu.map(item => {
      //   item.tag = false
      // })
      // info.tag = info.name != '未知'
      // //降雨分析-实时雨情-地图打点
      // if (info.name == '等面雨量分析') {
      //   this.$refs.map.showConstantRainfall()
      // } else {
      //   this.$refs.map.clearRainfallLayer()
      // }
    },
    showMap() {
      alert(1);
    },
    async getCount() {
      let res = await rainApi.count({
        hours: 1,
      });
      if (res) {
        this.countData.one = res.data;
      }
      let res2 = await rainApi.count({
        hours: 3,
      });
      if (res2) {
        this.countData.three = res2.data;
      }
      let res3 = await waterApi.count();
      if (res3) {
        this.countData.cjj = res3.data.warnNum;
        this.countData.cbz = res3.data.guaranteeNum;
        this.countData.cxx = res3.data.limitNum;
      }
    },
  },
  beforeDestroy() {
    this.timer = null;
  },
};
</script>

<style lang="scss" scoped>
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
    height: 182px;
    width: 60px;
    background: url("../../../assets/images/menu-top.png") no-repeat center;
    background-size: 100% 100%;
    padding-top: 4px;
  }
  .bottom-menu {
    height: 246px;
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
      width: 89px;
      height: 40px;
      line-height: 40px;
      text-align: center;
      cursor: pointer;
      color: #4898fb;
      background: url(../../../assets/images/bigScreen-big-tab.png) no-repeat;
      background-size: 100% 100%;
      margin-right: 6px;
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
      @include flexAC;
      margin-right: 126px;
      width: 112px;
      height: 36px;
      background: url("../../../assets/images/btn_bottom.png");
      background-size: 100% 100%;
      cursor: pointer;
      font-size: 16px;
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
    background: url("../../../assets/images/screen_bottom_right_fxyp.png")
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