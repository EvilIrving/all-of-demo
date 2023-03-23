<template>
  <div class="bigScreen">
    <div class="bigScreen_map">
      <router-view></router-view>
      <!-- <screen-map
        ref="map"
        class="screen_map"
      ></screen-map> -->
    </div>
    <div class="bigScreen_title">
      <span>防汛减灾调度支撑系统</span>
    </div>

    <i class="bigScreen_back icon-back-home" @click="bigScreenGo('/')"></i>
    <div class="bigScreen_time">
      <div class="date_left">{{ nowDate.date }}</div>
      <div class="date_right">
        <div>{{ nowDate.year }}年</div>
        {{ nowDate.month }}月{{ nowDate.day }}日
      </div>
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
  </div>
</template>

<script>
export default {
  name: "FloodJudgeNewest",
  props: {},
  components: {},
  data() {
    return {
      day: "",
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
          name: "水情雨情",
          icon1: "icon-btn_rain1",
          icon2: "icon-btn_rain2",
          type: "rain",
        },
        {
          selected: false,
          name: "工情内涝",
          icon1: "icon-btn_water1",
          icon2: "icon-btn_water2",
          type: "water",
        },
        {
          selected: false,
          name: "三江流域",
          icon1: "icon-btn_else1",
          icon2: "icon-btn_else2",
          type: "work",
        },
        {
          selected: false,
          name: "水库调度",
          icon1: "icon-btn_else1",
          icon2: "icon-btn_else2",
          type: "risk",
        },
      ],
    };
  },
  mounted() {
    this.getNowTime();
    this.day = this.$moment().format("YYYY年MM月DD日");
  },
  methods: {
    bigScreenGo(path) {
      this.$router.push(path);
    },
    // 获取当前时间
    getNowTime() {
      this.timer = setInterval(() => {
        this.nowDate.date = new Date().format("hh:mm:ss");
      }, 1000);
    },
    // 底部按钮点击事件
    clickBottomBtn(index) {
      //关闭所有菜单选中状态
      this.bottomBtnList.map((item) => {
        item.selected = false;
      });
      //选中当前点击菜单项目
      this.bottomBtnList[index].selected = true;
      //菜单点击的操作
      let url = "";
      if (this.bottomBtnList[index].type == "rain") {
        url = "/waterRainAnalysis";
      } else if (this.bottomBtnList[index].type == "water") {
        url = "/workWaterlogging";
      } else if (this.bottomBtnList[index].type == "work") {
        url = "/workDrainageBasin";
      } else if (this.bottomBtnList[index].type == "risk") {
        url = "/reservoirRegulation";
      } else {
        url = "/riskAnalysis";
      }
      this.$router.push(url);
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

.screen_map {
  width: 100%;
  height: 100%;
}
</style>
<style lang="scss" scoped>
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