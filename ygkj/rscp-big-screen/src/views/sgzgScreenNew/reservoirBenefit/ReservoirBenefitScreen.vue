<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-25 16:58:11
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\sgzgScreenNew\reservoirBenefit\ReservoirBenefitScreen.vue
-->
<template>
  <div class="screen_wrap">
    <section class="datetime" :class="{ hasTop: hasTop}">
      <el-date-picker v-model="benefitDate" value-format="yyyy-MM-dd" format="yyyy-MM-dd" type="daterange" align="right" unlink-panels range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions">
      </el-date-picker>
      <el-button class="cal_btn" type="primary" @click="compute">计算</el-button>
    </section>
    <section class="drawer_left" :class="{ showLeft: drawerShowLeft,hasTop: hasTop}">
      <reservoir-benefit-left />
      <img class="drawer_left_btn" v-show="drawerShowLeft" @click="close('Left')" src="../../../assets/images/drawer_left1.png" alt="" />
      <img class="drawer_left_btn" v-show="!drawerShowLeft" @click="open('Left')" src="../../../assets/images/drawer_left2.png" alt="" />
    </section>
    <main-map ref="reservoirStorageMap" />
    <section class="drawer_right" :class="{ showRight: drawerShowRight,hasTop: hasTop  }">
      <reservoir-benefit-right />
      <img class="drawer_right_btn" v-show="drawerShowRight" @click="close('Right')" src="../../../assets/images/drawer_right1.png" alt="" />
      <img class="drawer_right_btn" v-show="!drawerShowRight" @click="open('Right')" src="../../../assets/images/drawer_right2.png" alt="" />
    </section>
    <!-- 弹出层 -->
    <preview-photo v-if="showLookPhotosPopup" :loading="lookPhotosLoading" :photoList="photosList" :reservoirName="reservoirName" @closeLookPhotosPopup="closeLookPhotosPopup"></preview-photo>
    <preview-video v-if="showLookVideoPopup" :loading="lookVideoLoading" :videoUrl="videoUrl" @closeLookVideoPopup="closeLookVideoPopup"></preview-video>
    <preview-camera v-if="showLookCameraPopup" :prcd="prcd" :name="skName" @closeLookCameraPopup="closeLookCameraPopup"></preview-camera>
    <div class="reservoir_dialog" v-show="showDetailDialog">
      <div class="reservoir_title">
        <img @click="showDetailDialog = false" src="../../../assets/images/back2.png" alt="" />
        <p>{{ projectTitle }}</p>
      </div>
      <!-- <img class="back" @click="showDetailDialog = false" src="../../../assets/images/back2.png" alt=""> -->
      <iframe class="reservoir_dialog_iframe" :src="detailUrl" frameborder="0"></iframe>
    </div>
  </div>
</template>

<script>
import MainMap from "../components/MainMap";
import ReservoirBenefitLeft from "./reservoirBenefitLeft/ReservoirBenefitLeft.vue";
import ReservoirBenefitRight from "./reservoirBenefitRight/ReservoirBenefitRight.vue";
import PreviewPhoto from "@/components/PreviewPhoto";
import PreviewVideo from "@/components/PreviewVideo";
import PreviewCamera from "@/components/PreviewCamera";
import { getQueryString } from '@/utils/utils'
export default {
  name: "ReservoirScreen",
  props: {},
  components: {
    MainMap,
    ReservoirBenefitLeft,
    ReservoirBenefitRight,
    PreviewPhoto,
    PreviewVideo,
    PreviewCamera,
  },
  computed: {

  },
  watch: {

  },
  data() {
    return {
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      hasTop: true,
      drawerShowLeft: true,
      drawerShowRight: true,
      showLookPhotosPopup: false,
      lookPhotosLoading: true,
      photosList: [],
      reservoirName: "",
      showLookVideoPopup: false,
      lookVideoLoading: true,
      videoUrl: "",
      jhFlag: true,
      showDetailDialog: false,
      detailUrl: "",
      projectTitle: "详情",
      prcd: "",
      skName: "",
      showLookCameraPopup: false,
      provincialLevel: false,
    };
  },
  computed: {
    benefitDate(newVal, oldVal) {
      let arr = ['2021-01-01']
      let now = this.getCurrentTime()
      arr.push(now)
      return arr;
    }
  },
  mounted() {
    //http://192.168.2.212:8080/screen?isSgzg=1&hasTop=1&type=zhekuHealthy
    if (getQueryString('hasTop') == '1') {
      this.hasTop = true
    } else {
      this.hasTop = false
    }
    if (this.$localData("get", "userInfo").adcd == "330000000000") {
      this.provincialLevel = true;
    }
  },
  methods: {
    compute() {
    },
    getCurrentTime() {
      //创建对象
      let date = new Date();
      //获取年份
      let y = date.getFullYear();
      //获取月份  返回0-11
      let m = date.getMonth() + 1;
      // 获取日
      let d = date.getDate();
      //获取星期几  返回0-6   (0=星期天)
      let w = date.getDay();
      //星期几
      let ww = ' 星期' + '日一二三四五六'.charAt(date.getDay());
      //时
      let h = date.getHours();
      //分
      let minute = date.getMinutes()
      //秒
      let s = date.getSeconds();
      //毫秒
      let sss = date.getMilliseconds();

      if (m < 10) {
        m = "0" + m;
      }
      if (d < 10) {
        d = "0" + d;
      }
      if (h < 10) {
        h = "0" + h;
      }
      if (minute < 10) {
        minute = "0" + minute;
      }
      if (s < 10) {
        s = "0" + s;
      }
      if (sss < 10) {
        sss = "00" + sss;
      } else if (sss < 100) {
        sss = "0" + sss;
      }
      return y + "-" + m + "-" + d + " " + h + ":" + minute + ":" + s;
    },
    open(type) {
      this["drawerShow" + type] = true;
    },
    close(type) {
      this["drawerShow" + type] = false;
    },


    // 画点 聚合
    showMapPointJh(pointArr, type) {
      this.$nextTick(() => {
        this.$refs.reservoirStorageMap.newMapJh(pointArr, type);
      });
    },
    // 画点 不聚合
    showMapPoints(pointArr) {
      this.$nextTick(() => {
        this.$refs.reservoirStorageMap.newMapPoints(pointArr);
      });
    },

    //显示弹窗
    showOverlays(pointData) {
      this.$nextTick(() => {
        this.$refs.reservoirStorageMap._drawOverlays(pointData);
      });
    },

    // 查看图片
    openLookPhotosPopup() {
      this.showLookPhotosPopup = false;
      this.$nextTick(() => {
        this.lookPhotosLoading = true;
        this.showLookPhotosPopup = true;
      });
    },
    closeLookPhotosPopup() {
      this.showLookPhotosPopup = false;
    },
    showPhotosList(arr, reservoirName) {
      setTimeout(() => {
        this.lookPhotosLoading = false;
        this.photosList = arr;
        this.reservoirName = reservoirName;
      });
    },

    // 查看视频
    openLookVideoPopup() {
      this.showLookVideoPopup = false;
      this.$nextTick(() => {
        this.lookVideoLoading = true;
        this.showLookVideoPopup = true;
      });
    },
    closeLookVideoPopup() {
      this.showLookVideoPopup = false;
    },
    showVideo(url) {
      this.lookVideoLoading = false;
      this.videoUrl = url;
    },

    // 查看带列表的视频
    openLookCameraPopup(prcd, skName) {
      this.prcd = prcd;
      this.skName = skName;
      this.showLookCameraPopup = false;
      this.$nextTick(() => {
        this.showLookCameraPopup = true;
      });
    },
    closeLookCameraPopup() {
      this.showLookCameraPopup = false;
    },

    // 查看工程详情
    openDetailPopup(row) {
      let prcd = row.PRCD;
      let name = row.NAME;
      let token = this.$localData("get", "token");
      name = encodeURI(encodeURI(name));
      this.projectTitle = `${row.cityADNM} - ${row.countryADNM} - ${row.NAME}`;
      this.detailUrl = `https://sk.zjwater.com/cp/skip/reservoirDetailSingle?prcd=${prcd}&name=${name}&fromMap=&pName=&cName=&token=${token}`;
      this.showDetailDialog = true;
    },


    // 地图右下角操作
    setZoom(type) {
      this.$refs.reservoirStorageMap._setZoom(type);
    },

    setCluster(val) {
      this.$refs.reservoirStorageMap.setCluster(val);
    },

    // 查看点位
    setView(lng, lat) {
      this.$refs.reservoirStorageMap._setView(lng, lat);
    },

    // 设置弹窗
    setPopup(pointData) {
      this.$refs.reservoirStorageMap._setPopup(pointData);
    },

    // 设置选中点位
    showOneSelectPoint(points) {
      this.$refs.reservoirStorageMap._drawPoints(points);
    },
  },
};
</script>
<style lang="scss" scoped>
.datetime {
  z-index: 33;
  width: 25%;
  padding: 10px;
  height: 50px;
  background: #f4faff;
  text-align: center;
  border-radius: 19px;
  color: #3b59cd;
  font-size: 20px;
  border: 4px solid #194ac573;
  cursor: pointer;
  @include flexbox();
  @include flexJC(center);
  @include flexAI(center);
  position: absolute;
  top: 20px;
  // top: 50%;
  &.hasTop {
    top: 126px !important;
  }
  left: 36%;
  // transform: translate(-50% -50%);
  .cal_btn {
    margin-left: 20px;
    // width: calc(100% - 16px);
    background: #2d58ff;
  }
}
.drawer_left {
  width: 484px;
  left: -485px;
  transition: none;
  &.hasTop {
    top: 126px !important;
    height: calc(100% - 126px);
  }
}

.drawer_right {
  width: 484px;
  right: -485px;
  transition: none;
  &.hasTop {
    top: 126px !important;
    height: calc(100% - 126px);
  }
}
.showLeft {
  left: 0;
}
.showRight {
  right: 0;
}
.fangan_iframe {
  width: 100%;
  height: 600px;
}
.reservoir_dialog {
  background: #fff;
  z-index: 999999;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  // .back{
  //   cursor: pointer;
  //   position: absolute;
  //   top: 4px;
  //   left: 0px;
  //   width: 30px;
  //   height: 30px;
  // }
  .reservoir_title {
    padding: 16px;
    background: #2478f0;
    // padding-left: 16px;
    color: #fff;
    font-size: 20px;
    height: 40px;
    line-height: 40px;
    @include flexbox();
    @include flexAC();
    // @include flexJC(center);
    img {
      margin-right: 10px;
      cursor: pointer;
    }
  }
  .reservoir_dialog_iframe {
    height: calc(100% - 120px);
    width: 100%;
    overflow: hidden;
  }
}

.map_legend {
  width: 150px !important;
  right: -180px !important;
  .map_legend_title {
    height: 28px !important;
  }
  .map_legend_list {
    .map_legend_item {
      width: 100% !important;
    }
  }
}

.right_select_title {
  @include flexbox();
  @include flexAC();
  padding: 8px 16px;
  color: #193cc5;
  font-size: 14px;
  line-height: 16px;
  background: -webkit-linear-gradient(
    90deg,
    #e4f3fc 13%,
    rgba(201, 219, 250, 0) 98%
  );
  background: -o-linear-gradient(
    90deg,
    #e4f3fc 13%,
    rgba(201, 219, 250, 0) 98%
  );
  background: -moz-linear-gradient(
    90deg,
    #e4f3fc 13%,
    rgba(201, 219, 250, 0) 98%
  );
  background: linear-gradient(90deg, #e4f3fc 13%, rgba(201, 219, 250, 0) 98%);
  img {
    margin-right: 8px;
  }
}
.right_select_close {
  cursor: pointer;
  @include flexbox();
  @include flexAC();
  @include flexJC(center);
  padding: 8px 0;
  border-top: 1px solid #d6d6d6;
  color: #333;
  background: #f9f9f9;
  font-size: 14px;
  line-height: 16px;
  img {
    margin-left: 6px;
  }
}
.drawer_right_home {
  position: absolute;
  top: 6px;
  left: -85px;
  cursor: pointer;
}
.right_select_open {
  position: absolute;
  top: 75px;
  left: -78px;
  cursor: pointer;
}
.map_btns {
}
.map_btns1 {
  left: -300px !important;
  width: 300px !important;
}
.map_btns2 {
  left: -200px !important;
  width: 195px !important;
}
</style>

<style lang="scss">
.fade-enter-active {
  transition: all 0.6s ease;
}
.fade-leave-active {
  transition: all 0.2s ease;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.fangan_dialog .el-dialog__header {
  padding-bottom: 0;
}
.fangan_dialog .el-dialog__body {
  padding-top: 0;
}
</style>
