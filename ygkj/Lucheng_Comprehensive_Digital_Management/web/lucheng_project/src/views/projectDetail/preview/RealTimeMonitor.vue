<template>
  <div id="dialog_diy">
    <el-dialog :visible.sync="visible" width="70%" :before-close="close" top="15vh" title="实时监测" :close-on-click-modal="false" class="list-dialog" append-to-body>
      <div class="monitor_tabs">
        <div v-for="(item, index) in tabList" :key="item.name" @click="switchTab(index, item)" :class="[item.select ? 'active' : '']">
          <i :class="[item.select ? 'active' : '', item.name]"></i>
          <span :class="[item.select ? 'active' : '']">{{ item.label }}</span>
        </div>
      </div>
      <div class="divide"></div>
      <div class="title-flex"></div>
      <div v-if="stType !== 5 && stType !== 6" class="monitor_content">
        <div v-if="stType == 1" class="c_left content_box">
          <div class="select_box">
            <span>潮位:</span>
            <el-select size="mini" v-model="stationName" placeholder="请选择" @change="getTideCharts">
              <el-option v-for="item in stationOption" :label="item.name" :value="item.value" :key="item.value">
              </el-option>
            </el-select>
            <!-- <el-select style="margin-left:16px" size="mini" v-model="stationName" placeholder="请选择" @change="getTideCharts">
              <el-option v-for="item in stationOption" :label="item.name" :value="item.value" :key="item.value">
              </el-option>
            </el-select> -->
          </div>
          <p>潮位</p>

          <!--潮位需要显示实时潮位曲线、天文潮曲线、风暴潮曲线、蓝色警戒水位、
              黄色警戒水位、橙色警戒水位、红色警戒水位；
              沉降需要显示沉降曲线+限值（10cm）；
              位移需要显示位移曲线+正限值（5mm）+负限值（5mm） -->
          <ve-line :extend="chartWaterExtend" :colors="waterColors" :data="chartWaterData" :settings="chartWaterSettings" height="90%" width="100%" :grid="waterGrid" :mark-line="waterMarkLine" :yAxis="yAxis" />
        </div>
        <div v-if="stType == 2" class="c_left content_box">
          <div class="select_box">
            <span>雨量:</span>
            <el-select size="mini" v-model="rainName" placeholder="请选择" @change="getRainCharts">
              <el-option v-for="item in rainOption" :label="item.name" :value="item.value" :key="item.value">
              </el-option>
            </el-select>
          </div>
          <p>雨量</p>
          <ve-histogram :data="charRaintData" :extend="chartRainExtend" height="90%" width="100%" :grid="rainGrid" :settings="chartRainSettings" :yAxis="yAxis"></ve-histogram>
        </div>
        <div v-if="stType == 3" class="c_left content_box">
          <div class="select_box">
            <span>沉降:</span>
            <el-select size="mini" v-model="optionsRegion" placeholder="请选择" @change="getWaterLevels">
              <el-option :label="item.stnm" :value="item.stcd" v-for="item in options" :key="item.stcd"></el-option>
            </el-select>
          </div>
          <p>沉降</p>
          <ve-line class="chart_wrap" :extend="chartWaterExtend" :colors="waterColors" :data="chartData" :settings="chartWaterSettings" :grid="waterGrid" :mark-line="markLine" :yAxis="yAxis" />
        </div>
        <div v-if="stType == 4" class="c_left content_box">
          <div class="select_box">
            <span>位移:</span>
            <el-select size="mini" v-model="optionsRegion" placeholder="请选择" @change="getWaterLevels">
              <el-option :label="item.stnm" :value="item.stcd" v-for="item in options" :key="item.stcd"></el-option>
            </el-select>
          </div>
          <p>位移</p>
          <ve-line class="chart_wrap" :extend="chartWaterExtend" :colors="waterColors" :data="chartData" :settings="chartWaterSettings" :grid="waterGrid" :mark-line="markLine" :yAxis="yAxis" />
        </div>
        <div class="c_right content_box">
          <p>列表</p>
          <div class="tb_wrapper">
            <el-table :data="tableData" stripe border style="width: 100%">
              <el-table-column type="index" align="center" width="50" label=""></el-table-column>
              <el-table-column v-for="(item, index) in columnList" :key="index" :prop="item.prop" :label="item.label" align="center"></el-table-column>
            </el-table>
          </div>
        </div>
      </div>
      <!-- 视频 -->
      <div v-if="stType == 5" class="video_content">
        <div class="c_left content_box">
          <p>摄像头列表</p>
          <ul>
            <li v-for="(item, index) in videoList" :key="item.index" @click="switchVideo(index, item)" :class="{ active: index == activeIndex }">
              {{ item.cameraName }}
            </li>
          </ul>
        </div>
        <div class="c_right content_box">
          <!-- 正常视频播放 -->
          <primary-video v-if="currentCameraReload" ref="videoDialog" :code="videoData.cameraIndexCode" />
        </div>
      </div>
      <!-- 全景图 -->
      <div v-if="stType == 6" class="video_content"></div>
    </el-dialog>
  </div>
</template>

<script>
import PrimaryVideo from "@/components/VideoPlayer.vue";
// import LineChart from "@/components/LineChart";
import { perviewApi } from "@/api/projectDetail";
export default {
  props: {
    visible: Boolean,
    info: Object,
  },
  components: { PrimaryVideo },
  data() {
    this.chartWaterExtend = {
      legend: {
        icon: "rect",
        itemWidth: 8,
        itemHeight: 4,
        right: 0,
        bottom: 0,
        textStyle: {
          //图例文字的样式
          fontSize: 12,
        },
      },
    };
    this.chartWaterSettings = {
      showLine: ["水位曲线"],
    };
    this.waterGrid = {
      top: 20,
      bottom: 30,
      left: 0,
      right: 0,
    };
    this.waterMarkLine = {
      data: [
        {
          yAxis: 30,
        },
      ],
      lineStyle: {
        normal: {
          color: "red", // 这儿设置安全基线颜色
        },
      },
      label: {
        normal: {
          position: "middle",
          formatter: "警戒潮位", // 这儿设置安全基线
        },
      },
    };
    this.chartRainSettings = {
      showLine: ["累计雨量"],
      itemStyle: {
        //配置一些样式
        color: {
          type: "linear",
          x: 0, //X轴起始坐标
          y: 0, //Y轴起始坐标
          x2: 1, //X轴终点坐标
          y2: 1, //Y轴终点坐标
          colorStops: [
            {
              offset: 0,
              color: "#84C6FF", // 0% 处的颜色
            },
            {
              offset: 1,
              color: "#62A3FF", // 100% 处的颜色
            },
          ],
          globalCoord: false, // 缺省为 false
        },
      },
      // labelMap: {
      //   tm: "时间",
      //   drp: '降雨量',
      // },
    };
    this.chartRainExtend = {
      legend: {
        icon: "rect",
        itemWidth: 8,
        itemHeight: 4,
        right: 0,
        bottom: 0,
        textStyle: {
          //图例文字的样式
          fontSize: 12,
        },
      },
      // "xAxis.0.axisLabel.color": "#0b2045",
      // series(v) {
      //   v && v.forEach((i) => (i.barGap = "0%"));
      //   return v;
      // },
    };
    this.rainGrid = {
      top: 20,
      bottom: 30,
      left: 0,
      right: 0,
    };
    this.yAxis = {
      // inverse: false,
      // textStyle: {
      // },
      // axisLine: {
      //   show: true,
      //   lineStyle: {
      //   },
      // },
    };
    this.markLine = {
      data: [
        {
          yAxis: 0,
          label: {
            position: "middle",
            formatter: "警戒上限值", // 这儿设置安全基线
          },
          lineStyle: {
            color: "red", // 这儿设置安全基线颜色
          },
        },
        {
          yAxis: 0,
          label: {
            position: "middle",
            formatter: "警戒下限值", // 这儿设置安全基线
          },
          lineStyle: {
            color: "red", // 这儿设置安全基线颜色
          },
        },
      ],
    };
    return {
      stationOption: [],
      stationName: 1,
      rainOption: [],
      rainName: 1,
      playerOptions: {
        playbackRates: [0.7, 1.0, 1.5, 2.0], //播放速度、
        techOrder: ["flash"],
        autoplay: true,
        controls: true,
        preload: "auto", //视频预加载
        muted: false, //默认情况下将会消除任何音频。
        loop: false, //导致视频一结束就重新开始
        language: "zh-CN",
        aspectRatio: "16:9", // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        sources: [
          {
            type: "rtmp/mp4",
            //   src: 'rtmp://122.228.154.149:1935/service/PuId-ChannelNo=122000000109597567-01&PlayMethod=0&StreamingType=1'
            src: "rtmp://58.200.131.2:1935/livetv/hunantv",
          },
        ],
        width: document.documentElement.clientWidth,
        notSupportedMessage: "此视频暂无法播放，请稍后再试", //允许覆盖Video.js无法播放媒体源时显示的默认信息。
      },
      // tide rain security panorama video
      tabList: [
        { name: "tide", select: true, value: 1, label: "潮位" },
        { name: "rain", select: false, value: 2, label: "雨量" },
        { name: "settlement", select: false, value: 3, label: "沉降" },
        { name: "displacement", select: false, value: 4, label: "位移" },
        { name: "video", select: false, value: 5, label: "视频监控" },
        { name: "panorama", select: false, value: 6, label: "全景图" },
      ],
      videoList: [],
      currentCameraReload: false,
      activeIndex: 0,
      videoData: {},
      stType: 1,
      tableData: [],
      chartDataTT: {
        id: "linecharttt",
        unit: "座",
        xData: [],
        yData: [],
      },
      charRaintData: {
        columns: ["时间", "雨量"],
        rows: [],
      },
      chartWaterData: {
        columns: ["时间", "潮位", '预测潮位'],
        rows: [],
      },
      chartData: {
        columns: ["时间", "观测值"],
        rows: [],
      },
      waterColors: ["#32BCB4", "#19af47", "rgb(255, 0, 0)"],
      chartDataDD: {
        id: "linechartdd",
        unit: "座",
        xData: [2020, 2021, 2022, 2023, 2024],
        yData: [20, 12, 15, 16, 18],
      },
      options: [],
      optionsRegion: [],
      columnList: [
        {
          prop: "tm",
          label: "时间",
        },
        {
          prop: "tdz",
          label: "潮位(m)",
        },
      ],
    };
  },
  watch: {},
  created() { },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let opt = {
        projectCode: this.info.projectCode,
        stType: this.stType,
        projectType: this.info.projectType,
      };
      if (this.stType == 3 || this.stType == 4) {
        opt.stType = 5;
      }
      this.stationOption = [];
      let res = await perviewApi.getSeawallListSt(opt);
      if (res.code === 0) {
        switch (this.stType) {
          case 1:
            this.columnList = [
              {
                prop: "tm",
                label: "时间",
              },
              {
                prop: "tdz",
                label: "潮位(m)",
              },
            ];
            if (this.info.projectName == '江心屿景区防洪堤塘' || this.info.projectName == '江心屿西园防洪堤塘'||this.info.projectName == '七都标准堤西北堤' || this.info.projectName == '七都标准堤东堤南堤吟州堤') {
              this.columnList.push({
                prop: "h2",
                label: "预测潮位(m)",
              })
            }
            res.data.map((i, index) => {
              this.stationOption.push({
                stType: i.stType,
                stcd: i.stcd,
                name: i.stnm,
                value: index + 1,
              });
            });
            this.getTideCharts(this.stationName);
            break;
          case 2:
            this.columnList = [
              {
                prop: "tm",
                label: "时间",
              },
              {
                prop: "drp",
                label: "雨量(mm)",
              },
            ];
            if (res.data.length !== 0) {
              res.data.map((i, index) => {
                this.rainOption.push({
                  stType: i.stType,
                  stcd: i.stcd,
                  name: i.stnm,
                  value: index + 1,
                });
              });
              this.getRainCharts(this.rainName);
            }
            break;
          case 3:
            this.columnList = [
              {
                prop: "gcsj",
                label: "时间",
              },
              {
                prop: "zzjg",
                label: "观测值(mm)",
              },
            ];
            if (res.data.length !== 0) {
              this.options =
                res.data.filter((item) => item.stcd.indexOf("DI_") != -1) || [];
              if (this.options.length > 0) {
                this.optionsRegion = this.options[0].stcd;
                this.getWaterLevels();
              } else {
                this.optionsRegion = "";
              }
            }
            break;
          case 4:
            this.columnList = [
              {
                prop: "gcsj",
                label: "时间",
              },
              {
                prop: "zzjg",
                label: "观测值(mm)",
              },
            ];
            if (res.data.length !== 0) {
              this.options =
                res.data.filter((item) => item.stcd.indexOf("ES_") != -1) || [];
              if (this.options.length > 0) {
                this.optionsRegion = this.options[0].stcd;
                this.getWaterLevels();
              } else {
                this.optionsRegion = "";
              }
            }
            break;
        }
      }
    },
    async getTideCharts() {
      let opt = {
        stationCode: this.stationOption[this.stationName - 1].stcd,
        stationType: this.stationOption[this.stationName - 1].stType,
        days: 1,
        dmId:this.info.sectionId
      };
      let res = await perviewApi.getSeawallWaterLevels(opt);
      if (res.code === 0) {
        this.tableData = JSON.parse(JSON.stringify(res.data.data)).reverse();
        res.data.data.map((i) => {
          this.chartDataTT.xData.push(i.tm);
          this.chartDataTT.yData.push(i.tdz);
          this.chartDataTT.yData.push(i.h2);
        });
        let obj = res.data.data.map((item, index) => {
          item["tm"] = item.tm;
          item["tdz"] = item.tdz;
          item["h2"] = item.h2;
          return { ...item };
        });
        let info = JSON.parse(JSON.stringify(obj));
        let max = 0;
        info.map((item) => {
          item["时间"] = item.tm;
          item["潮位"] = item.tdz;
          item["预测潮位"] = item.h2;
          max = item.tdz > max ? item.tdz : max;
          return item;
        });
        this.yAxis.max = max * 1.5;
        this.waterMarkLine.data[0].yAxis = res.data.warningLevel;
        if (this.info.projectName == '江心屿景区防洪堤塘' || this.info.projectName == '江心屿西园防洪堤塘'||this.info.projectName == '七都标准堤西北堤' || this.info.projectName == '七都标准堤东堤南堤吟州堤') {
          this.chartWaterData.columns = ["时间", "潮位", '预测潮位']
        } else {
          this.chartWaterData.columns = ["时间", "潮位"]
        }
        this.chartWaterData.rows = info;
      }
    },
    async getRainCharts() {
      let opt = {
        stationCode: this.rainOption[this.rainName - 1].stcd,
        days: 1,
      };
      let res = await perviewApi.rainPop(opt);
      if (res.code == 0) {
        // res.data.map((item) => {
        //   item.drp = Math.floor(Math.random() * 100).toFixed(2);
        //   return item;
        // });
        let obj = res.data.map((item, index) => {
          item["tm"] = item.tm;
          item["tdz"] = item.tdz;
          return { ...item };
        });
        let info = JSON.parse(JSON.stringify(obj));
        let max = 0;
        info.map((item) => {
          item["时间"] = item.tm;
          item["雨量"] = item.drp;
          max = item.drp > max ? item.drp : max;
          return item;
        });
        this.yAxis.max = max * 1.5;
        this.charRaintData.rows = info;
        this.tableData = res.data;
      }
    },
    getWaterLevels() {
      let data = this.options.find((item) => item.stcd == this.optionsRegion);
      perviewApi
        .getAqjcDatas({
          stationCode: data.stcd,
          days: 1,
        })
        .then((res) => {
          let info = JSON.parse(JSON.stringify(res.data));
          let max = 0;
          let maxValue = 0;
          let minValue = 0;
          info.map((item) => {
            item["时间"] = item.gcsj;
            // item['警戒上限值'] = 0;
            // item['警戒下限值'] = 0;
            item["观测值"] = item.zzjg;
            max = item.zzjg > max ? item.zzjg : max;
            maxValue = item.maxValue;
            minValue = item.minValue;
            return item;
          });
          this.markLine.data[0].yAxis = maxValue;
          this.markLine.data[1].yAxis = minValue;
          this.yAxis.max = (max * 2).toFixed(2);
          this.chartData.rows = info;
          this.tableData = JSON.parse(JSON.stringify(res.data)).reverse();
        });
    },
    async getVideoList() {
      let res = await perviewApi.getVideoList(this.info.projectCode);
      if (res.code === 0) {
        this.videoList = res.data;
        this.switchVideo(0, res.data[0]);
      }
    },
    getPhotoList() { },
    switchTab(index, row) {
      this.tabList.map((v) => {
        v.select = false;
      });
      this.tabList[index].select = true;
      this.stType = index + 1;
      switch (index) {
        case 4:
          this.getVideoList();
          break;
        case 5:
          this.getPhotoList();
          break;
        default:
          this.getList();
          break;
      }
    },
    switchVideo(index, row) {
      this.activeIndex = index;
      this.videoData = row;
      this.currentCameraReload = false
      setTimeout(() => {
        this.currentCameraReload = true;
      }, 200);
    },
    // 关闭
    close() {
      this.$emit("close");
    },
  },
};
</script>

<style scoped lang="scss">
.monitor_tabs {
  margin: 0 20%;
  @include flexbox();
  @include flexAI(center);
  @include flexJC(space-around);
  flex-wrap: nowrap;
  & > div {
    @include flexbox();
    @include flexAI(center);
    @include flexJC(space-around);
    flex-direction: column;
    width: 100px;
    height: 56px;
    background: #f3f4f4;
    border-radius: 4px;
    &.active {
      background: #1492ff;
    }
    span {
      color: rgba(0, 0, 0, 0.88);
      font-size: 14px;
    }
    span.active {
      color: rgba(255, 255, 255, 0.88);
    }
    i {
      width: 20px;
      height: 20px;
    }
    i.tide {
      background: url("../../../assets/images/tide-b.png") no-repeat center;
      &.active {
        background: url("../../../assets/images/tide.png") no-repeat center;
      }
    }
    i.rain {
      background: url("../../../assets/images/rain-b.png") no-repeat center;
      &.active {
        background: url("../../../assets/images/rain.png") no-repeat center;
      }
    }
    i.security {
      background: url("../../../assets/images/security-b.png") no-repeat center;
      &.active {
        background: url("../../../assets/images/security.png") no-repeat center;
      }
    }
    i.panorama {
      background: url("../../../assets/images/security-b.png") no-repeat center;
      &.active {
        background: url("../../../assets/images/security.png") no-repeat center;
      }
    }
    i.video {
      background: url("../../../assets/images/video-b.png") no-repeat center;
      &.active {
        background: url("../../../assets/images/video.png") no-repeat center;
      }
    }
    i.displacement {
      background: url("../../../assets/images/displacement-b.png") no-repeat
        center;
      &.active {
        background: url("../../../assets/images/displacement.png") no-repeat
          center;
      }
    }
    i.settlement {
      background: url("../../../assets/images/settlement-b.png") no-repeat
        center;
      &.active {
        background: url("../../../assets/images/settlement.png") no-repeat
          center;
      }
    }
  }
}

.title-flex {
  margin: 20px 0;
  display: flex;
  align-items: center;
  font-size: 0.4rem;
  color: #eee;
}
.title-flex::before,
.title-flex::after {
  content: "";
  flex: 1;
  height: 1px;
  background: #eee;
}
.monitor_content {
  width: 100%;
  height: 100%;
  @include flexbox();
  .content_box {
    padding-right: 16px;
    p {
      font-size: 14px;
      font-weight: 600;
      color: #333333;
      height: 15x;
      line-height: 15x;
      border-left: 3px solid #1492ff;
      padding-left: 16px;
    }
  }
  .c_left {
    width: 60%;
    // width: 300px;
    height: 500px;
    .chart_wrap {
      width: 100%;
      height: calc(100% - 25px);
    }
    .select_box {
      margin: 10px;
      span {
        padding-right: 10px;
      }
    }
  }
  .c_right {
    width: 40%;
    height: 440px;
    .tb_wrapper {
      height: 100%;
      overflow-y: auto;
    }
  }
}
.video_content {
  width: 100%;
  height: 500px;
  @include flexbox();
  .content_box {
    padding-right: 16px;
    height: 100%;
    p {
      font-size: 14px;
      font-weight: 600;
      color: #333333;
      height: 15x;
      line-height: 15x;
      border-left: 3px solid #1492ff;
      padding-left: 16px;
    }
  }
  .c_left {
    width: 20%;
    // width: 300px;
    height: 400px;
    ul {
      width: 100%;
      height: 100%;
      overflow-y: auto;
      scrollbar-width: none; /* Firefox */
      -ms-overflow-style: none; /* IE 10+ */
      padding: 5px 5px 5px 0;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(flex-start);
      flex-direction: column;
      &::-webkit-scrollbar {
        display: none; /* Chrome Safari */
      }
      li {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(space-around);
        margin: 5px 10px 5px 0;
        width: 199px;
        height: 20px;
        padding: 10px;
        cursor: pointer;
        border: 1px solid #e6e6e6;
        &.active {
          border: 1px solid #1492ff;
        }
      }
    }
  }
  .c_right {
    width: 80%;
    height: 100%;
    border: 2px solid black;
  }
}
</style>
