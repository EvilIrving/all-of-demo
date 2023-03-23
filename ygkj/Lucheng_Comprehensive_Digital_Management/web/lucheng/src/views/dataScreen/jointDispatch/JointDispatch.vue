<!--
 * @Author: hanyu
 * @LastEditTime: 2021-02-05 11:12:49
 * @Description: 联合调度
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\jointDispatch\JointDispatch.vue
-->
<template>
  <div class="bigScreen">
    <div class="bigScreen_map">
      <screen-map
        ref="map"
        class="screen_map"
        :pageType="pageType"
        @modelHandle="modelHandle"
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
        @click="bigScreenGo('/floodJudge')"
      ></i>
      <i
        class="bigScreen_next icon-screen-next"
        @click="bigScreenGo('/rescueSupport')"
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
    <div class="bigScreen_left">
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
    </div>
    <!-- 左侧菜单 -->
    <div class="bigScreen_left" v-if="!noSideLeft">
      <transition name="fade" v-on:after-leave="transitionDoneLeft">
        <div class="sidebar" :style="{ width: pageType == 'flood' ? '280px' : '380px' }" v-show="barshowLeft">
          <!-- 左侧表格 -->
            <el-table
              ref="multipleTable"
              :data="leftTableList"
              style="width: 100%;"
              height="370px"
              @selection-change="handleSelectionChange" >
            <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column type="index" label width="55">
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="projectName" label="水库名称"></el-table-column>
            </el-table>
            <el-button type="primary" style="float:right;margin:10px;" @click="lookDetails">查看</el-button>
          <div class="switch-btn" @click="toggleSideBarLeft()"></div>
        </div>
      </transition>
      <div class="switch-btn show-btn" v-show="showBtnLeft" @click="toggleSideBarLeft()"></div>
    </div>
    <!-- 右侧菜单 -->
    <div class="bigScreen_right" v-if="!noSide">
      <transition name="fade" v-on:after-leave="transitionDone">
        <div class="sidebar" v-show="barshow">
          <div
            class="right-tab"
            :style="{
              top:
                stInfo && stInfo.projectCode == '330327027000071'
                  ? '-90px'
                  : '-67px',
            }"
          >
            <template v-if="modelShow">
              <span
                v-for="(item, index) in sidebarTabArr"
                :key="index"
                @click="sidebarTabChange(item)"
                :class="activeSidebar == item.name ? 'active' : ''"
                >{{ item.name }}</span
              >
            </template>
            <template v-else>
              <span
                v-for="(item, index) in sidebarTabArrOther"
                :key="index"
                @click="sidebarTabChangeOther(item)"
                :class="activeSidebarOther == item.name ? 'active' : ''"
                >{{ item.name }}</span
              >
            </template>
            <p
              class="font-orange"
              v-if="stInfo && stInfo.projectCode == '330327027000071'"
            >
              <i class="icon-warn-tips"></i>
              当前位置发生预警，前往
              <span class="go-btn" @click="bigScreenGo('/rescueSupport')"
                >抢险</span
              >
            </p>
          </div>
          <!-- <router-view></router-view> -->
          <div class="side-wrap" v-if="modelShow">
            <model-analysis
              ref="ModelAnalysis"
              v-if="modelName == 'ModelAnalysis'"
              :info="stInfo"
            ></model-analysis>
            <real-time-monitor
              v-if="modelName == 'RealTimeMonitor'"
            ></real-time-monitor>
            <real-time-communicate
              v-if="modelName == 'RealTimeCommunicate'"
            ></real-time-communicate>
          </div>
          <div class="side-wrap" v-if="!modelShow">
            <water-storage-ability
              v-if="modelName == 'waterStorageAbility'"
            ></water-storage-ability>
            <water-storage v-if="modelName == 'waterStorage'"></water-storage>
            <max-water-storage
              v-if="modelName == 'maxWaterStorage'"
            ></max-water-storage>
          </div>
          <div class="switch-btn" @click="toggleSideBar()"></div>
        </div>
      </transition>
      <div
        class="switch-btn show-btn"
        v-show="showBtn"
        @click="toggleSideBar()"
      ></div>
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
      <div class="legend_item">
        <img src="../../../assets/images/legend/map_normal.png" />
        <span>正常水位</span>
      </div>
      <div class="legend_item">
        <img src="../../../assets/images/legend/map_unnormal.png" />
        <span>超警水位</span>
      </div>
    </div>
    <!-- 查看详情弹窗 -->
    <details-dialog :dialogVisible="detailsShow" :lookDetailsData="lookDetailsData" v-if="detailsShow" @handleClose="dispatchClose" />
    <!-- 底部菜单模块 -->
    <div class="bigScreen_bottom">
      <div class="btn_box">
        <div v-for="(item, index) in bottomBtnList"
          :key="index"
          :class="['btn_item', item.selected ? 'active' : '']"
          @click="clickBottomBtn(index,item.path)">
          <i :class="item.icon1"></i>
          <span>{{ item.name }}</span>
        </div>
      </div>
    </div>
    <div class="bigScreen_count">
      <div class="box">
        <div>
          <p>报警数量</p>
          <p class="num" :style="{ color: countData.one === -1 ? '#ff3525' : 'red' }">{{ countData.one + 1 }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { dispatchApi } from "@/api/dataScreen/jointDispatch/JointDispatch";

import ScreenMap from "./components/ScreenMap";
import WeatherData from "../components/WeatherData";
import WaterStorageAbility from "./components/sidebar/WaterStorageAbility";
import WaterStorage from "./components/sidebar/WaterStorage";
import MaxWaterStorage from "./components/sidebar/MaxWaterStorage";
import ModelAnalysis from "./components/sidebar/ModelAnalysis";
import RealTimeMonitor from "./components/sidebar/RealTimeMonitor";
import RealTimeCommunicate from "./components/sidebar/RealTimeCommunicate";
//详情展示
import detailsDialog from "./components/detailsDialog";
export default {
  name: "RescueSupport",
  props: {},
  components: {
    ScreenMap,
    WeatherData,
    WaterStorageAbility,
    WaterStorage,
    MaxWaterStorage,
    ModelAnalysis,
    RealTimeMonitor,
    RealTimeCommunicate,
    detailsDialog
  },
  data() {
    return {
      currentPage: "/jointDispatch",
      pageOptions: [
        { value: "/floodJudge", label: "防汛研判" },
        { value: "/jointDispatch", label: "联合调度" },
        { value: "/rescueSupport", label: "抢险支持" },
        // { value: "/floodAnalysis", label: "洪涝分析" },
      ],
      barshow: true,
      showBtn: false,
      bottomBtnList: [
        {
          selected: false,
          name: "洪水分析",
          icon1: "icon-btn_rain1",
          icon2: "icon-btn_rain2",
          type: "rain",
          path: '/inundationFlooding'
        },
        {
          selected: true,
          name: "调度模型",
          icon1: "icon-btn_water1",
          icon2: "icon-btn_water2",
          type: "water",
          path: '/JointDispatch'
        }
      ],
      sidebarTabArr: [
        {
          name: "模型分析",
          url: "/modelAnalysis",
          routerName: "ModelAnalysis",
        },
        {
          name: "实时监控",
          url: "/realTimeMonitor",
          routerName: "RealTimeMonitor",
        },
        {
          name: "实时通讯",
          url: "/realTimeCommunicate",
          routerName: "RealTimeCommunicate",
        },
      ],
      sidebarTabArrOther: [
        {
          name: "可蓄水量",
          url: "/jointWaterStorageAbility",
          routerName: "waterStorageAbility",
        },
        {
          name: "纳蓄能力",
          url: "/jointWaterStorage",
          routerName: "waterStorage",
        },
      ],
      modelName: "waterStorageAbility",
      activeSidebar: "模型分析",
      activeSidebarOther: "可蓄水量",
      timer: null,
      nowDate: {
        date: new Date().format("hh:mm:ss"),
        year: new Date().format("yyyy"),
        month: new Date().format("MM"),
        day: new Date().format("dd"),
      },
      topMenu: [],
      bottomMenu: [],
      pageType: "waterStorageAbility",
      mapDetail: false,
      countData: {
        one: "",
      },
      modelShow: false,
      dailogShow: false,
      stInfo: {},
      noSide: false,
      noSideLeft: false,
      barshowLeft: true,
      showBtnLeft: false,
      leftTableList: [],
      detailsShow: false,
      currentRow: [],
      lookDetailsData: []
    };
  },
  mounted() {
    this.getNowTime();
    this.topMenu[0].tag = true;
    this.$router.push(this.topMenu[0].url);
    this.pageType = this.topMenu[0].value;
    this.getCount();
    this.getList();
  },
  methods: {
    //查看详情
    lookDetails() {
      if( 0 < this.currentRow.length) {
        this.detailsShow = true;
        this.lookDetailsData = this.currentRow;
      }else {
        this.$message({ type: 'warning', message:"请选择至少一条数据！" });
      }
    },
    //关闭查看
    dispatchClose() {
      this.detailsShow = false;
    },
    //多选
    handleSelectionChange(val) {
      if(val.length > 6) {
        this.$message({ type: 'warning', message:"最多只能选中六条数据！" });
          this.$refs.multipleTable.toggleRowSelection(val.pop());  //pop：最后一个
      } else {
          this.currentRow = val;         //multipleSelection：当前选择的数组
      }
    },
    //水库信息查询
    async getList() {
      let res = await dispatchApi.mapPoint();
      if (res.code == 0) {
        this.leftTableList = res.data;
      }
    },
    //左侧菜单
    toggleSideBarLeft() {
      this.showBtnLeft = false;
      this.barshowLeft = !this.barshowLeft;
    },
    transitionDoneLeft(el, done) {
      this.showBtnLeft = true;
    },
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
    // 底部按钮点击事件
    clickBottomBtn(index,val) {
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
      this.$router.push(val);
    },
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
      this.getList();
      this.timer = setInterval(() => {
        this.nowDate.date = new Date().format("hh:mm:ss");
      }, 1000);
    },
    sidebarTabChange(item) {
      this.activeSidebar = item.name;
      this.modelName = item.routerName;
      if (this.modelName == "ModelAnalysis") {
        this.$nextTick(() => {
          this.$refs.ModelAnalysis.getDetail();
        });
      }
    },
    sidebarTabChangeOther(item) {
      this.activeSidebarOther = item.name;
      this.modelName = item.routerName;
    },
    changeMenu(info) {
      this.topMenu.map((item) => {
        item.tag = false;
      });
      info.tag = true;
      if (info.value == "maxWaterStorage") {
        this.noSide = true;
      } else {
        this.noSide = false;
        this.modelShow = false;
        this.$router.push(info.url);
        this.modelName = info.value;
        this.pageType = info.value;
      }
    },
    modelHandle(item) {
      this.noSide = false;
      this.modelShow = true;
      this.activeSidebar = "模型分析";
      this.modelName = this.sidebarTabArr[0].routerName;
      this.stInfo = item;
      this.$nextTick(() => {
        this.$refs.ModelAnalysis.getDetail();
      });
      // this.$router.push({
      //   name: this.sidebarTabArr[0].routerName,
      //   params: item,
      // });
    },
    showMap() {
    },
    async getCount() {
      let res = await dispatchApi.warnNum();
      if (res) {
        this.countData.one = res.data.limitNum;
      }
    },
  },
  beforeDestroy() {
    this.timer = null;
  },
};
</script>

<style lang='scss' scoped>
$sidebarHeight: 424px;
.font-orange {
  color: #fa6400;
  height: 32px;
  font-size: 14px;
  padding-left: 16px;
  margin: 10px 0;
  .go-btn {
    display: inline-block;
    height: 32px;
    padding: 0 20px;
    line-height: 32px;
    text-align: center;
    color: #fff;
    font-size: 14px;
    cursor: pointer;
    background: url(../../../assets/images/bigScreen-mini-tab-warn.png)
      no-repeat;
    background-size: 100% 100%;
  }
  .icon-warn-tips {
    vertical-align: middle;
  }
}
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
  left: 30px;
  color: #fff;
  ::v-deep .el-table th {
      background: none;
  }
  ::v-deep .el-table th, ::v-deep.el-table tr {
    color: #4898fb;
  }
  ::v-deep.el-checkbox__inner {
    border: 0.0625rem solid #4898fb;
    background-color: #4898fb;
  }
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
      vertical-align: middle;
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
    background: url("../../../assets/images/bs-warn-bg.png") no-repeat center;
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
.side-wrap {
  height: 100%;
  width: 100%;
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