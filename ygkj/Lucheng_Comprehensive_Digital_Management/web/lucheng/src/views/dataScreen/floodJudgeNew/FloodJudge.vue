<!--
 * @Author: hanyu
 * @LastEditTime: 2021-03-01 17:06:50
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
    <!-- 左侧菜单 -->
    <div class="bigScreen_left" v-if="!noSideLeft">
      <transition name="fade" v-on:after-leave="transitionDoneLeft">
        <div
          class="sidebar"
          :style="{ width: pageType == 'flood' ? '280px' : '380px' }"
          v-show="barshowLeft"
        >
          <!-- <div class="right-tab"> -->
          <!-- <template v-if="pageType == 'water'"> -->
          <!-- <template>
              <span
                v-for="(item, index) in sidebarTabArr"
                :key="index"
                @click="sidebarTabChange(item)"
                :class="activeSidebar == item.name ? 'active' : ''"
                >{{ item.name }}</span
              >
            </template>
          </div> -->
          <router-view name="left"></router-view>
          <div class="switch-btn" @click="toggleSideBarLeft()"></div>
        </div>
      </transition>
      <div
        class="switch-btn show-btn"
        v-show="showBtnLeft"
        @click="toggleSideBarLeft()"
      ></div>
    </div>
    <!-- 右侧菜单 -->
    <div class="bigScreen_right" v-if="!noSide">
      <transition name="fade" v-on:after-leave="transitionDone">
        <div class="sidebar" v-show="barshow">
          <!-- <div class="right-tab"> -->
          <!-- <template v-if="pageType == 'water'"> -->
          <!-- <template>
              <span
                v-for="(item, index) in sidebarTabArr"
                :key="index"
                @click="sidebarTabChange(item)"
                :class="activeSidebar == item.name ? 'active' : ''"
                >{{ item.name }}</span
              >
            </template> -->
          <!-- </div> -->
          <router-view name="right"></router-view>
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
          <i :class="item.icon1"></i>
          <!-- <i v-show="item.selected" :class="item.icon2"></i> -->
          <span>{{ item.name }}</span>
        </div>
      </div>
    </div>
    <!-- <div class="bigScreen_bottom-left">
      <weather-data class="weather_data"></weather-data>
    </div> -->
    <!-- <div class="bigScreen_bottom-right">
      <div class="btn_box">
        <i class="icon-screen-br-btn"></i>
        <span>数据统计</span>
      </div>
      <div class="img"></div>
    </div> -->

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

      <!-- <div v-show="bottomBtnList[3].selected"> -->
        <!-- <div class="title">水位</div> -->
        <!-- <div class="legend_item">
          <img src="../../../assets/images/legend-water-general-station.png" />
          <span>积水点</span>
        </div>
      </div> -->

      <div v-show="bottomBtnList[3].selected">
        <!-- <div class="title">水位</div> -->
        <div class="legend_item">
          <img src="../../../assets/images/legend/risk-legend-danger.png" />
          <span>高风险</span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/risk-legend-warn.png" />
          <span>中风险</span>
        </div>
        <div class="legend_item">
          <img src="../../../assets/images/legend/risk-legend-normal.png" />
          <span>低风险</span>
        </div>
      </div>
    </div>

    <div class="bigScreen_count">
      <div class="box" v-show="bottomBtnList[0].selected">
        <div>
          <p>1h降雨量</p>
          <p
            class="num"
            :style="{ color: countData.one === -1 ? '#ff3525' : 'red' }"
          >
            {{ countData.one + 1 }}
          </p>
        </div>
      </div>
      <!-- <div class="box" v-show="bottomBtnList[0].selected">
        <div>
          <p>3h降雨量报警</p>
          <p
            class="num"
            :style="{ color: countData.one === -1 ? '#ff3525' : 'unset' }"
          >
            {{ countData.three + 1 }}
          </p>
        </div>
      </div> -->
      <div class="box" v-show="bottomBtnList[1].selected || pageType == 'work'">
        <div>
          <p>超警戒</p>
          <p class="num" :style="{ color: countData.cjj === 0 ? '' : '#ff3525' }">{{ countData.cjj }}</p>
        </div>
      </div>
      <div class="box" v-show="bottomBtnList[1].selected || pageType == 'work'">
        <div>
          <p>超保证</p>
          <p class="num" :style="{ color: countData.cbz === 0 ? '' : '#ff3525' }">{{ countData.cbz }}</p>
        </div>
      </div>
      <div class="box" v-show="bottomBtnList[1].selected || pageType == 'work'">
        <div>
          <p>超汛限</p>
          <p class="num" :style="{ color: countData.cxx === 0 ? '' : '#ff3525' }">{{ countData.cxx }}</p>
        </div>
      </div>
    </div>
    <div class="meteorological-warn">
      <span class="yellow">{{ day }}</span>
      暂无气象预警
    </div>

    <div
      class="layer-control"
      v-if="pageType == 'rain'"
      :style="{ left: barshowLeft ? '490px' : '80px' }"
    >
      <!-- <div class="one-key" v-if="pageType == 'rain'"></div> -->
      <div class="layer-control-item">
        <p>实时雨量</p>
        <el-checkbox-group
          class="layer-control-box"
          v-model="checkedRealLayer"
          @change="handleCheckedRealLayerChange"
        >
          <el-checkbox
            v-for="(item, index) in realLayer"
            :label="item.label"
            :key="index"
            >{{ item.label }}</el-checkbox
          >
        </el-checkbox-group>
      </div>

      <div class="layer-control-item">
        <p>预报雨量</p>
        <el-checkbox-group
          class="layer-control-box"
          v-model="checkedExpectLayer"
          @change="handleCheckedExpectLayerChange"
        >
          <el-checkbox
            v-for="(item, index) in expectLayer"
            :label="item.label"
            :key="index"
            >{{ item.label }}</el-checkbox
          >
        </el-checkbox-group>
      </div>

      <div class="layer-control-item">
        <p>实时+预报雨量</p>
        <el-checkbox-group
          class="layer-control-box"
          v-model="checkedMixLayer"
          @change="handleCheckedMixChange"
        >
          <el-checkbox
            v-for="(item, index) in mixLayer"
            :label="item.label"
            :key="index"
            >{{ item.label }}</el-checkbox
          >
        </el-checkbox-group>
      </div>
    </div>
  </div>
</template>

<script>
import { rainApi, waterApi } from "@/api/dataScreen/floodJudge/index";
import ScreenMap from "./components/ScreenMap";
import axios from "axios";
export default {
  name: "RescueSupport",
  props: {},
  components: {
    ScreenMap,
  },
  data() {
    return {
      day: "",
      currentPage: "/floodJudge",
      pageOptions: [
        { value: "/floodJudge", label: "防汛研判" },
        { value: "/jointDispatch", label: "联合调度" },
        { value: "/rescueSupport", label: "抢险支持" },
        // { value: "/floodAnalysis", label: "洪涝分析" },
      ],
      barshow: true,
      showBtn: false,
      barshowLeft: true,
      showBtnLeft: false,
      checkedRealLayer: [],
      checkedExpectLayer: [],
      checkedMixLayer: [],
      realLayer: [
        {
          label: "1h",
          value: 1,
        },
        {
          label: "3h",
          value: 3,
        },
        {
          label: "6h",
          value: 6,
        }
        // ,
        // {
        //   label: "前72h",
        //   value: 72,
        // },
      ],
      expectLayer: [
        {
          label: "1h",
          value: 1,
        },
        {
          label: "3h",
          value: 3,
        },
        {
          label: "6h",
          value: 6,
        },
      ],
      mixLayer: [
        {
          label: "前1h+预报1h",
          value: 1,
        },
        {
          label: "前3h+预报3h",
          value: 2,
        },
        {
          label: "前6h+预报6h",
          value: 3,
        }
        // ,
        // {
        //   label: "前3h+预报1h",
        //   value: 4,
        // },
        // {
        //   label: "前3h+预报3h",
        //   value: 5,
        // },
        // {
        //   label: "前3h+预报24h",
        //   value: 6,
        // },
        // {
        //   label: "前24h+预报1h",
        //   value: 7,
        // },
        // {
        //   label: "前24h+预报3h",
        //   value: 8,
        // },
        // {
        //   label: "前24h+预报24h",
        //   value: 9,
        // },
      ],
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
          name: "水位分析",
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
        // {
        //   selected: false,
        //   name: "洪涝分析",
        //   icon1: "icon-btn_else1",
        //   icon2: "icon-btn_else2",
        //   type: "flood",
        // },
        {
          selected: false,
          name: "风险分析",
          icon1: "icon-btn_else1",
          icon2: "icon-btn_else2",
          type: "risk",
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
      noSideLeft: false,
    };
  },
  mounted() {
    this.getNowTime();
    this.topMenu = this.rainTopMenu;
    this.bottomMenu = this.rainBottomMenu;
    this.topMenu[0].tag = true;
    this.$router.replace("/rainfallAnalysis");
    this.getCount();
    this.day = this.$moment().format("YYYY年MM月DD日");
    this.checkedRealLayer.push("1");
    this.getRainData();
  },
  methods: {
    handleCheckedRealLayerChange(val) {
      if (val.length > 1) {
        this.checkedRealLayer.shift();
      }
      this.rainfallPredict(val[0])
      // console.log(val);
      this.getRainData();
    },
    handleCheckedExpectLayerChange() {},
    handleCheckedMixChange() {},
    bigScreenGo(path) {
      this.$router.push(path);
    },
    toggleSideBar() {
      this.showBtn = false;
      this.barshow = !this.barshow;
    },
    toggleSideBarLeft() {
      this.showBtnLeft = false;
      this.barshowLeft = !this.barshowLeft;
    },
    transitionDone(el, done) {
      this.showBtn = true;
    },
    transitionDoneLeft(el, done) {
      this.showBtnLeft = true;
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
      this.noSideLeft = false;
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
      let url = "";
      if (this.bottomBtnList[index].type == "rain") {
        url = "/rainfallAnalysis";
      } else if (this.bottomBtnList[index].type == "water") {
        url = "/waterAnalysis";
      } else if (this.bottomBtnList[index].type == "work") {
        url = "/workAnalysis";
      // } else if (this.bottomBtnList[index].type == "flood") {
      //   url = "/floodAnalysisChild";
      } else {
        url = "/riskAnalysis";
      }
      this.$router.push(url);
      // this.sidebarTabChange(this.sidebarTabArr[0]);

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
    //获取雨量测站
    async getRainData() {
      const obj = {
        前1h: 60,
        前3h: 180,
        前24h: 1440,
        前72h: 4320,
      };
      const opt = {
        minutes: obj[this.checkedRealLayer[0]],
        min: -1,
        max: 2000,
      };
      await rainApi.realRain(opt).then((res) => {
        this.$refs.map.rainPoint(res.data);
        // console.log(res);
      });
    },
    showStagnantPoint(data) {
      this.$refs.map._drawStagnantPoint(data);
    },
    showRiskPoint() {
      this.$refs.map._drawRiskPoint(this.$store.state.riskPointList);
    },
    sidebarTabChange(item) {
      // console.log("item: ", item);
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
    showMapPoint(data) {
      data.map((item) => {
        console.log(item);
      });
      this.$refs.map.workPoint(data);
    },
    //雨量预测（1/3/6小时）
    async rainfallPredict(param){
      let hour = "";
      this.realLayer.forEach(item => {
        if(item.label == param){
          hour = item.value;
        }
      })
      let opt = {
        hour:hour
      }
      let res = await rainApi.rainfallPredict(opt);
      let params = this.formatRainfallParam(res.data);
      this.getIsohyetArea(params);
      // console.log(res);
    },
    formatRainfallParam(list){
      let geoJson = {
        "features":[],
      }
      let featuresArr = [];
      list.forEach(item=>{
        // let drp = item.drp > 0 ? item.drp : 0;
        let obj = {
          "attributes":{
            "x":item.longitude,
            "y":item.latitude,
            "value":item.drp
          },
          "geometry":{
            "x":item.longitude,
            "y":item.latitude,
          }
        }
        featuresArr.push(obj);
      });
      geoJson.features = featuresArr;
      return geoJson;
    },
    //获取等雨量面
    async getIsohyetArea(opt){
      let params = new URLSearchParams();
      params.append("station", JSON.stringify(opt));
      params.append("f", "json");
      let _this = this;
      await axios({
        url:
          "http://117.149.227.112:6080/arcgis/rest/services/gps/dengzhiPolygonIDW/GPServer/dengzhiPolygonIDW/submitJob",
        method: "post",
        headers: { "content-type": "application/x-www-form-urlencoded" },
        data: params,
      }).then(async (res) => {
        let interVal = setInterval(async ()=>{
          let url = "http://117.149.227.112:6080/arcgis/rest/services/gps/dengzhiPolygonIDW/GPServer/dengzhiPolygonIDW/jobs/"+res.data.jobId+"/results/Idwres?f=pjson";
          await axios({
            url:url,
            method: "get",
          }).then(async (result) => {
            if(!result.data.value.mapImage.error){
              clearInterval(interVal);
              //加载透明等雨量面
              let mapImage = result.data.value.mapImage.extent;
              _this.getIsohyetOpacity(mapImage.xmin,mapImage.ymin,mapImage.xmax,mapImage.ymax,res.data.jobId);
              // _this.$refs.waterWorkMap.showIsohyetLayer(result.data.value.mapImage);
            }
          })
          // console.log(res.data);
        },1000);
      })
    },
    //获取透明背景等雨量面
    async getIsohyetOpacity(xmin,ymin,xmax,ymax,jobId){
      let bbox = xmin + ',' + ymin + ',' + xmax + ',' + ymax;
      let params = new URLSearchParams();
      params.append("bbox", bbox);
      params.append("f", "pjson");
      params.append("transparent", true);
      let _this = this;
      let url = "http://117.149.227.112:6080/arcgis/rest/services/gps/dengzhiPolygonIDW/MapServer/jobs/"+jobId+"/export";
      await axios({
        url:url,
        method: "post",
        headers: { "content-type": "application/x-www-form-urlencoded" },
        data: params,
      }).then(res=>{
        _this.$refs.map.showIsohyetLayer(res.data);
        this.loading = false;
        if(this.autoTimePlayFlag){
          this.autoTimePlay();
        }
      })
    },
  },
  beforeDestroy() {
    this.timer = null;
  },
};
</script>

<style lang="scss" scoped>
$sidebarHeight: 620px;
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
// .bigScreen_left {
//   position: absolute;
//   top: 20%;
//   left: 0;
//   color: #fff;
//   .btn_item {
//     height: 75px;
//     width: 155px;
//     background: url("../../../assets/images/btn_left1.png") no-repeat center;
//     position: relative;
//     cursor: pointer;
//     margin-bottom: 40px;
//     span {
//       position: absolute;
//       font-size: 14px;
//       font-weight: 500;
//       background: linear-gradient(to right, #6ae1ff, #4297ff);
//       -webkit-background-clip: text;
//       color: transparent;
//       top: 17px;
//       right: 27px;
//     }
//     &.active {
//       background: url("../../../assets/images/btn_left2.png") no-repeat center;
//       background-size: 100% 100%;
//     }
//   }
//   .top-menu {
//     height: 182px;
//     width: 60px;
//     background: url("../../../assets/images/menu-top.png") no-repeat center;
//     background-size: 100% 100%;
//     padding-top: 4px;
//   }
//   .bottom-menu {
//     height: 246px;
//     width: 60px;
//     background: url("../../../assets/images/menu-bottom.png") no-repeat center;
//     background-size: 100% 100%;
//   }
//   .menu-icon {
//     position: relative;
//     height: 58px;
//     width: 58px;
//     cursor: pointer;
//   }
//   .name-tag {
//     position: absolute;
//     left: 50px;
//     top: 12px;
//     display: inline-block;
//     width: 85px;
//     height: 40px;
//     line-height: 40px;
//     text-align: center;
//     background: url("../../../assets/images/menu-tag.png") no-repeat center;
//     background-size: 100% 100%;
//     color: #64a6f6;
//     font-size: 14px;
//   }
//   .menu-ssyl {
//     background: url("../../../assets/images/ssyl.png") no-repeat center;
//     background-size: 100% 100%;
//   }
//   .menu-ssyq {
//     background: url("../../../assets/images/ssyq.png") no-repeat center;
//     background-size: 100% 100%;
//   }
//   .menu-dmyl {
//     background: url("../../../assets/images/dmyl.png") no-repeat center;
//     background-size: 100% 100%;
//   }
//   .menu-yqjb {
//     background: url("../../../assets/images/yqjb.png") no-repeat center;
//     background-size: 100% 100%;
//   }
//   .menu-tsfx {
//     background: url("../../../assets/images/tsfx.png") no-repeat center;
//     background-size: 100% 100%;
//   }
//   .menu-lsjz {
//     background: url("../../../assets/images/lsjz.png") no-repeat center;
//     background-size: 100% 100%;
//   }
//   .menu-ybyl {
//     background: url("../../../assets/images/ybyl.png") no-repeat center;
//     background-size: 100% 100%;
//   }
// }
.bigScreen_left {
  position: absolute;
  top: 25%;
  left: 30px;
  color: #fff;
  // 侧边栏
  .sidebar {
    width: 380px;
    height: $sidebarHeight;
    position: absolute;
    left: 0;
    top: 0;
    z-index: 1900;
    .sidebar-box {
      height: calc(#{$sidebarHeight} + 40px);
      width: 100%;
      position: absolute;
      right: 0;
      top: -40px;
    }
    .switch-btn {
      width: 45px;
      height: $sidebarHeight;
      position: absolute;
      top: 0;
      right: -38px;
      background: url(../../../assets/images/sidebar-left.png) no-repeat;
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
    background: url(../../../assets/images/sidebar-left.png) no-repeat;
    background-size: 100% 100%;
    // transform: translateY(50%);
    z-index: 1900;
    cursor: pointer;
  }

  .fade-enter,
  .fade-leave-to {
    transform: translateX(-100%);
  }
  .fade-enter-active,
  .fade-leave-active {
    transition: all 0.5s linear;
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
    top: 0px;
    z-index: 1900;
    .sidebar-box {
      height: calc(#{$sidebarHeight} + 40px);
      width: 100%;
      position: absolute;
      right: 0;
      top: -40px;
    }
    .switch-btn {
      width: 45px;
      height: $sidebarHeight;
      position: absolute;
      top: 0;
      left: -38px;
      background: url(../../../assets/images/sidebar-right.png) no-repeat;
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
    background: url(../../../assets/images/sidebar-right.png) no-repeat;
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
  bottom: 0;
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
      margin-right: 36px;
      width: 122px;
      height: 62px;
      cursor: pointer;
      font-size: 16px;
      color: #a6dff6;
      line-height: 66px;
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
        color: #fff;
        background: url("../../../assets/images/btn_bottom.png");
        background-size: 100% 100%;
        background-position: 0 10px;
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
        color: #fff;
      }
      .num {
        font-size: 22px;
        color: #bafcfe;
      }
    }
  }
}
// 一键研判
.one-key {
  width: 102px;
  height: 26px;
  position: relative;
  top: -20px;
  //left: 490px;
  z-index: 9999;
  color: #3895f4;
  cursor: pointer;
  background: url("../../../assets/images/one-key.png") no-repeat center;
  background-size: 100% 100%;
}
// 气象预警
.meteorological-warn {
  position: absolute;
  top: 60px;
  left: 470px;
  z-index: 9999;
  color: #fff;
  height: 54px;
  width: 460px;
  line-height: 54px;
  // background: rgba(21, 59, 100.5);
  // border: 2px solid #3f8ae7;
  // box-shadow: 0 0 4px 0 rgba(63, 138, 231, 0.8);
  // border-radius: 1px;
  // border-radius: 1px;
  font-size: 14px;
  padding: 0 20px;
  .yellow {
    color: #fef568;
    margin-right: 16px;
  }
}
.layer-control {
  transition: left 0.6s ease-in-out;

  position: absolute;
  bottom: 80px;
  left: 490px;
  z-index: 9999;
  &-item {
    > p {
      line-height: 28px;
    }
    color: #f3faff;
    background-color: #060e36;
    padding: 4px 10px 10px 10px;
    margin-bottom: 12px;
  }
  .layer-control-box {
    display: flex;
    flex-direction: column;
  }
}
</style>
<style lang="scss" scoped>
.layer-control .layer-control-box {
  .el-checkbox {
    margin-right: 0;
  }
  .el-checkbox__label {
    color: #f3faff;
  }
  .el-checkbox__input.is-checked + .el-checkbox__label {
    color: #409eff;
  }
  .el-checkbox__inner {
    border: 0.0625rem solid #146d84;
    background: #103464;
  }
}
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