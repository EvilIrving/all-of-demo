  <template>
  <div class="detail-table">
    <el-dialog
      width="850px;"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">堤防详情</p>
      </div>
      <!-- 内容区域 -->

      <!-- 内容区域 -->

      <div class="primary-dialog__content">
        <!-- <div class="content-panel"> -->
        <div class="left-content">
          <div class="top-panel">
            <div class="chart-panel">
              <div id="barChart" style="width: 100%; height: 100%"></div>
            </div>
          </div>
          <div class="right-content">
            <div class="tab-panel">
              <div
                :class="['tab-item', activeTab == '基本信息' ? 'active' : '']"
                @click="changeActiveTab('基本信息')"
              >
                基本信息
              </div>
              <div
                :class="['tab-item', activeTab == '位置信息' ? 'active' : '']"
                @click="changeActiveTab('位置信息')"
              >
                位置信息
              </div>
              <div
                :class="['tab-item', activeTab == '建管信息' ? 'active' : '']"
                @click="changeActiveTab('建管信息')"
              >
                建管信息
              </div>
              <div
                :class="['tab-item', activeTab == '工程特性' ? 'active' : '']"
                @click="changeActiveTab('工程特性')"
              >
                工程特性
              </div>
              <div
                :class="['tab-item', activeTab == '视频监控' ? 'active' : '']"
                @click="changeActiveTab('视频监控')"
              >
                视频监控
              </div>
            </div>
          </div>
          <!-- 基本信息 -->
          <div
            class="list-top-panel"
            v-if="
              activeTab == '基本信息' ||
              activeTab == '位置信息' ||
              activeTab == '建管信息' ||
              activeTab == '工程特性'
            "
          >
            <div class="info-top-panel">
              <img :src="img" alt="" />
              <div class="info-top-try">
                <div style="padding-top: 15px; font-size: 20px">
                  <p>营前河右堤防工程</p>
                  <p />
                </div>
                <el-row :gutter="20">
                  <div style="padding-top: 20px; font-size: 15px">
                    <el-col :span="10">
                      <span class="name">行政区划</span>
                      <span class="value">{{ obj.cityAdnm || "-" }}</span>
                    </el-col>
                    <el-col :span="6">
                      <span class="name">管理层级</span>
                      <span class="value">{{ obj.managementLevel || "-" }}</span>
                    </el-col>
                    <el-col :span="8" style="text-align: center">
                      <span class="name">岸别</span>
                      <span class="value">{{ obj.rvBank || "-" }}</span>
                    </el-col>
                  </div>
                </el-row>
                <el-row :gutter="20">
                  <div style="padding-top: 1rem; font-size: 16px">
                    <el-col :span="10">
                      <span class="name">堤防长（m）</span>
                      <span class="value">{{ obj.dikeLen || "-" }}</span>
                    </el-col>
                    <el-col :span="6">
                      <span class="name">标绘长（m）</span>
                      <span class="value">{{ obj.flStaLen || "-" }}</span>
                    </el-col>
                  </div>
                </el-row>
              </div>
            </div>
          </div>
          <div class="list-top-panel" v-if="activeTab == '基本信息'">
            <div class="info-down-panel">
              <el-row :gutter="20">
                <el-col :span="8">
                  <span class="name">所在水资源三级区编码</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">所在乡镇名称</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">所在村名称</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">所在河道等级</span>
                  <span class="value">{{ obj.riverLevel || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">完工时间（非加固）</span>
                  <span class="value">{{ obj.finshTime || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">是否五水共治任务</span>
                  <span class="value">{{ obj.isWsgz || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">是否竣工验收（非加固）</span>
                  <span class="value">{{ obj.isComplete || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">竣工验收时间（非加固）</span>
                  <span class="value">{{ obj.compDate || "-" }}</span>
                </el-col>
              </el-row>
            </div>
          </div>
          <!-- 位置信息 -->
          <div class="list-top-panel" v-if="activeTab == '位置信息'">
            <div class="info-down-panel" style="margin-top: -1.2rem">
              <el-row :gutter="20">
                <el-col :span="8">
                  <span class="name">所在河流（湖泊、海岸）名称</span>
                  <span class="value">{{ obj.bas || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">河流级别</span>
                  <span class="value">{{ obj.riverLevel || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">岸别</span>
                  <span class="value">{{ obj.rvBank || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">起点经度坐标</span>
                  <span class="value">{{ obj.startLong || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">起点纬度坐标</span>
                  <span class="value">{{ obj.startLat || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">起点位置</span>
                  <span class="value">{{ obj.startLoc || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">终点经度坐标</span>
                  <span class="value">{{ obj.endLong || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">终点纬度坐标</span>
                  <span class="value">{{ obj.endLat || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">终点位置</span>
                  <span class="value">{{ obj.endLoc || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">堤防类型</span>
                  <span class="value">{{ obj.dikeType || "-" }}</span>
                </el-col>
              </el-row>
            </div>
          </div>
          <!-- 建管信息 -->
          <div class="list-top-panel" v-if="activeTab == '建管信息'">
            <div class="info-down-panel" style="margin-top: -1.2rem">
              <el-row :gutter="20">
                <el-col :span="8">
                  <span class="name">归口管理部门</span>
                  <span class="value">{{ obj.admDep || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">工程建设情况</span>
                  <span class="value">{{ obj.endStar || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">开工时间</span>
                  <span class="value">{{ obj.startDate || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">建成时间</span>
                  <span class="value">{{ obj.compDate || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">是否完成划界</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">是否完成确权</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">是否纳入创标名录</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">是否完成创标</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">堤防跨界情况</span>
                  <span class="value">{{ obj.dikeCorBoun || "-" }}</span>
                </el-col>
              </el-row>
            </div>
          </div>
          <!-- 工程特性信息 -->
          <div class="list-top-panel" v-if="activeTab == '工程特性'">
            <div class="info-down-panel" style="margin-top: -1.2rem">
              <el-row :gutter="20">
                <el-col :span="8">
                  <span class="name">工程任务</span>
                  <span class="value">{{ obj.endTask || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">堤防类型</span>
                  <span class="value">{{ obj.dikeType || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">堤防形式</span>
                  <span class="value">{{ obj.dikePatt || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">堤防长度（m）</span>
                  <span class="value">{{ obj.dikeLen || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">堤顶宽度（m）（最小值）</span>
                  <span class="value">{{ obj.dikeTopWidMin || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">堤顶宽度（m）（最大值）</span>
                  <span class="value">{{ obj.dikeTopWidMax || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">堤防高度（m）（最小值）</span>
                  <span class="value">{{ obj.dikeHigMax || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">堤防高度（m）（最大值）</span>
                  <span class="value">{{ obj.dikeHigMin || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">堤顶高程（起点高程（m））</span>
                  <span class="value">{{ obj.startDikeTopEl || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">堤顶高程（终点高程（m））</span>
                  <span class="value">{{ obj.endDikeTopEl || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">高程系统</span>
                  <span class="value">{{ obj.elevSys || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">水闸数量（个）</span>
                  <span class="value">{{ obj.sluiceCount || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">管涵数量（个）</span>
                  <span class="value">{{ obj.culvertCount || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">泵站数量（处）</span>
                  <span class="value">{{ obj.pumpCount || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">设计水位（m）</span>
                  <span class="value">{{ obj.desStag || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">代表水位站名称</span>
                  <span class="value">{{ obj.relativeStationName || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">代表水位站编码</span>
                  <span class="value">{{ obj.relativeStationCode || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">警戒水位（m）</span>
                  <span class="value">{{ obj.warnWaterLevel || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">保证水位（m）</span>
                  <span class="value">{{ obj.dangerWaterLevel || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">规划防洪标准[重现期（年）]</span>
                  <span class="value">{{ obj.planFlSta || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">达到规划防洪标准的长度（m）</span>
                  <span class="value">{{}}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">堤防最低高程</span>
                  <span class="value">{{ obj.dikeLowEl || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">参照水位站危机水位</span>
                  <span class="value">{{ obj.warnWaterLevel || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">薄弱堤段名称</span>
                  <span class="value">{{ obj.weakDikeName || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">薄弱堤段起始经度</span>
                  <span class="value">{{ obj.weakDikeStartLongitude || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">薄弱堤段起始纬度</span>
                  <span class="value">{{ obj.weakDikeStartLatitude || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">薄弱堤段终止经度</span>
                  <span class="value">{{ obj.weakDikeEndLongitude || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">薄弱堤段终止纬度</span>
                  <span class="value">{{ obj.weakDikeEndLatitude || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">薄弱情况描述</span>
                  <span class="value">{{ obj.weakSituationDescription || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">保护范围</span>
                  <span class="value">{{ obj.protectionArea || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">倒虹吸数量（处）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">中间堤顶高程</span>
                  <span class="value">{{ obj.middleElevation || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">其他穿堤建筑物数量</span>
                  <span class="value">{{ obj.otherCrossBuildCount || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">道口数量</span>
                  <span class="value">{{ obj.levelCrossingCount || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">其他涉堤建筑物数量</span>
                  <span class="value">{{ obj.otherBuildingCount || "-" }}</span>
                </el-col>
              </el-row>
            </div>
          </div>

          <!-- 视频监控 -->
          <div
            class="tab-content-panel video-panel"
            v-if="activeTab == '视频监控'"
          >
            <div class="left-panel">
              <div
                class="camera-item"
                :class="activeCamera == item.code ? 'active' : ''"
                v-for="(item, i) in cameraList"
                :key="i"
                @click="cameraChange(item.code)"
              >
                {{ item.name }}
              </div>
            </div>
            <div class="right-panel">
              <primary-video
                ref="videoDialog"
                :code="videoData.cameraIndexCode"
              ></primary-video>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { PrimaryVideo } from "@/components";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { disasterPreventionApi } from "@/api/RightSildePanel";
// import { waterResourceApi } from "@/api/waterResourcesGuarantee";
// import { generateDataZoom } from "../cards/utils/chart.js";

export default {
  name: "dikePointDialog",
  data() {
    return {
      obj: {},
      dikeName: "",
      cityAdnm: "",
      test: "",
      activeTab: "基本信息", //默认
      // listActive: "建设档案",
      videoLoading: false,
      // obj: {},
      img: require("@/assets/images/shuiku-linshi.png"),
      name: "",
      scale: "",
      type: "",
      area: "",
      isP: "",
      target: "",
      warn: "",
      // activeTab: 0,
      ctiveCamera: "",
      cameraList: [
        { name: "摄像头1", code: "1" },
        { name: "摄像头2", code: "2" },
        { name: "摄像头3", code: "3" },
      ],
      videoData: {},
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  components: { PrimaryVideo },
  // components: {},
  created() {
    this.dikeproject();
  },
  mounted() {},
  computed: {
    // setWaterWarnColor() {
    //   switch (this.obj.warn) {
    //     case "暂无风险":
    //       return "#19AF47";
    //     case "黄色预警":
    //       return "#e89100";
    //     case "橙色预警":
    //       return "#ff551f";
    //     case "红色预警":
    //       return "#e80000";
    //   }
    // },
  },
  methods: {
    init(val) {
      // this.name = val.name;
      this.dikeName = val.name;
      // console.log(this.dikeName,"??????")
      // this.obj = val;
      this.$nextTick(() => {
        this.dikeproject();
      });
    },
    async dikeproject() {
      // console.log(this.dikeName,2223348990)
      await disasterPreventionApi
        .dikeproject({ dikeName: this.dikeName })
        .then((res) => {
          if (res.code == 0) {
            this.obj = res.data.list;
            this.obj = this.obj[0];
            // this.totalNum = res.data.totalNum;
            // this.dikeLen = res.data.dikeLen;
            // this.highestNumber = res.data.highestNumber;
            // this.miniNumber = res.data.miniNumber;
          }
          //  console.log(this.obj,666677777)
        });
    },
    handleClose() {
      this.$emit("closeDialog");
    },
    // 监控切换
    cameraChange(val) {
      this.activeCamera = val;
    },
    //tab切换
    changeActiveTab(val) {
      this.activeTab = val;
      switch (val) {
        case "基本信息":
          // this.listUseByMonth();
          break;
        case "位置信息":
          // this.setBaseInfoList();
          break;
        case "建管信息":
          // this.currentWptOfWiu();
          break;
        case "工程特性":
          // this.wiuDocList4Config();
          break;
        case "视频监控":
          this.cameraList = [
            { name: "摄像头1", code: "1" },
            { name: "摄像头2", code: "2" },
            { name: "摄像头3", code: "3" },
          ];
          break;
        // this.listUseByMonth();
      }
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    handleTabChange(val) {
      this.activeTab = val;
    },
    // 曲线（模仿婷婷姐姐）
    async getFlowCurve() {
      this.lineData.name = [];
      this.lineData.value = [];
      let opt = {
        hystCode: this.hystCode,
        startTime: this.startDate,
        endTime: this.endDate,
      };
      this.loading = true;
      await waterResourceApi.getFlowCurve(opt).then((res) => {
        console.log("ressss line: ", res);
        res.data.forEach((item) => {
          this.lineData.name.push(item.recTime);
          this.lineData.value.push(item.maxFlow);
        });
        this.tbList = res.data;
        this.$nextTick(() => {
          this.lineChartOpt();
          this.loading = false;
        });
      });
    },
    //获取堤防信息
    getDikeInfo() {
      this.loading = true;
      this.subhead = "堤防";
      // console.log(this.stationData);
      const opt = {
        name: this.stationData.name,
        hours: this.time * 24,
      };
      // hydraulicApi.floodPointHistoricCurve(opt).then((res) => {
      //   this.loading = false;
      //   if (res.code == 0) {
      //     this.dataList = res.data;
      //     this.columnList = [
      //       {
      //         prop: "key",
      //         label: "时间",
      //         width: 200,
      //       },
      //       {
      //         prop: "value",
      //         label: "水深(cm)",
      //       },
      //     ];
      //     this.chartWaterData.columns = ["时间", "水深"];
      //     let info = [];
      //     res.data.map((item) => {
      //       info.push({ 时间: item.key, 水深: item.value });
      //       return item;
      //     });
      //     this.chartWaterData.rows = info;
      //     console.log(this.chartWaterData);
      //   }
      // });
    },

    //     init(val) {
    //       this.obj = val;
    //       this.$nextTick(() => {});
    //     },
    //     handleClose() {
    //       this.$emit("closeDialog");
    //     },
  },
};
</script>
<style lang="scss" scoped>
::v-deep.primary-dialog {
  .el-dialog {
    width: 55vw;
  }
  .tab-content-panel {
    display: flex;
    .left-panel {
      width: 60%;
      margin-right: 20px;
      .top-panel {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .title {
          font-size: 18px;
          i {
            vertical-align: middle;
            margin-right: 10px;
          }
        }
        .time {
          ::v-deep.el-date-editor {
            width: 140px;
          }
          ::v-deep.el-input__inner {
            padding-left: 1.875rem;
            background: none;
            border: 1px solid rgba(255, 255, 255, 0.5);
            height: 32px;
          }
          ::v-deep.el-input__icon {
            line-height: 1;
          }
        }
      }
      .chart-panel {
        height: calc(100% - 30px);
      }
    }
    .right-panel {
      flex: 1;
    }
  }

  .video-panel {
    height: 30rem;
    display: flex;
    justify-content: space-between;
    .left-panel {
      width: 30%;
      margin-right: 20px;
      overflow-y: scroll;
      .camera-item {
        color: #fff;
        cursor: pointer;
        margin: 8px 0;
        margin-right: 0.375rem;
        border: 1px #285c9e solid;
        padding: 6px;
        &:hover,
        &.active {
          border: 1px solid #168aa7;
          box-shadow: 0 0 20px #168aa746 inset;
        }
      }
    }
    .right-panel {
      flex: 1;
      margin-bottom: 20px;
    }
  }
  .top-panel {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .content-panel {
    height: 500px;
    display: flex;
    justify-content: space-between;
    color: #fff;
    .left-content {
      width: 60%;
      margin-right: 20px;
      .title {
        margin-bottom: 15px;
        span {
          margin-left: 10px;
          font-size: 18px;
          vertical-align: top;
        }
      }
    }
    .chart-panel {
      width: 100%;
      height: calc(100% - 40px);
    }
    .right-content {
      width: 110%;
    }
  }

  .tab-panel {
    display: flex;
    margin-bottom: 20px;
  }
  .tab-item {
    padding: 8px 10px;
    margin-right: 20px;
    color: #fff;
    border: 1px solid rgba(255, 255, 255, 0.6);
    cursor: pointer;
    &.active {
      color: #56fefe;
      background: rgba(72, 152, 251, 0.1);
      border: 1px solid #56fefe;
      box-shadow: inset 0 0 5px 0 #56fefe;
    }
  }
  .list-top-panel {
    padding: 16px;
    .list-item {
      color: #fff;
      font-size: 14px;
      margin-bottom: 20px;
      .value {
        // font-size: 14px;
        padding: 10px 10px;
        margin-top: 8px;
        border-radius: 5px;
        background-color: rgba(0, 0, 0, 0.2);
      }
    }
  }
  .list-down-panel {
    height: 300px;
    // height: calc(100% - 42px);

    .list-item {
      color: #fff;
      font-size: 14px;
      margin-bottom: 20px;
      .value {
        padding: 10px 10px;
        margin-top: 8px;
        border-radius: 5px;
        background-color: rgba(0, 0, 0, 0.2);
      }
    }
  }
  .abc ::v-deep .el-table__body tr:hover {
    background-color: rgba($color: #56fefe, $alpha: 0.4);
  }
  ::v-deep .el-dialog {
    // width:850px;
    // width:850px;
    // height: 200px;
    .el-dialog__header {
      padding: 2px;
      .primary-dialog__title {
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
      }
    }
    .el-dialog__body {
      // height: 200px;
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        height: 100%;
        padding: 20px;
      }
    }
  }
  .img-panel {
    height: 200px;
    width: 200px;
    // margin-bottom: 20px;
    img {
      width: 200px;
      height: 200px;
    }
  }
  .info-top-panel {
    border-bottom: 1px solid rgba($color: #409eff, $alpha: 0.4);
    // margin-right: 50px;
    // padding-right: 50px;
    height: 200px;
    color: #fff;
    font-size: 14px;
    line-height: 32px;
    .info-top-try {
      width: 700px;
      height: 180px;
      margin-top: -11.5rem;
      margin-left: 300px;
      //        padding-left: 204px;
      // margin-top: -84px;
    }
    img {
      width: 270px;
      height: 172px;
    }
    span.name {
      font-size: 15px;
    }
    span.value {
      font-size: 16px;
      margin-left: 10px;
      color: #33ffff;
    }
  }
  .info-down-panel {
    color: #fff;
    font-size: 16px;
    line-height: 32px;
    span.name {
      font-size: 15px;
    }
    span.value {
      margin-left: 10px;
      color: #33ffff;
    }
  }

  ::v-deep .el-radio__input.is-checked .el-radio__inner {
    border-color: #1ef8f2;
    background-color: #1ef8f2;
  }

  ::v-deep .el-dialog {
    // padding-left:20px;
    // width:200%;
    // width: 855px;
    // height: 458px;
    .el-dialog__header {
      // width:150%;
      padding: 2px;
      .primary-dialog__title {
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
      }
    }
    .el-dialog__body {
      // width:150%;
      // height: 500px;
      height: 25.25rem;
      // width: 861px;
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        height: 100%;
        padding: 20px;
      }
    }
  }
}
</style>
<style lang="scss" scoped>
.basic-info {
  ::v-deep .basic-info-item {
    margin-bottom: 16px;
    &:last-child {
      margin-bottom: 0;
    }
    .basic-info-item__title {
      border-left: 2px solid #1e8bd6;
      padding-left: 6px;
      color: #44a0cd;
      font-size: 15px;
    }
    .basic-info-item__content {
      display: flex;
      margin-top: 16px;
      justify-content: space-between;
      padding-left: 6px;
      .figure-box {
        width: 70%;
      }
      .basic-info-item__detail {
        width: 25%;
        display: flex;
        flex-direction: column;
        .detail-item {
          display: flex;
          flex-direction: column;
          height: 33%;
          justify-content: space-between;
          padding: 14px 0;
        }
      }
    }
    .info-list {
      .el-col {
        margin: 8px 0;
      }
      .item-title {
        margin-right: 16px;
      }
    }
  }
  .item-title {
    color: #3f86df;
  }
  .item-label {
    color: #fff;
  }
}
.figure-box {
  height: 12rem;
  padding-left: 1px;
  position: relative;
  overflow: hidden;
  .figure-box-staff {
    height: 100%;
    width: 1rem;
    color: #fff;
    border-right: 1px solid #ffffff;
    position: absolute;
    top: 0;
    left: 0;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .current-water-level {
    width: 50%;
    padding: 0.5rem;
    border-bottom: 2px dashed #32bbb3;
    color: #ffffff;
    position: absolute;
    top: 18%; //需要动态修改
    left: 1rem;
  }
  @keyframes wave {
    0% {
      background-position: 0px bottom;
    }
    100% {
      background-position: 252px bottom;
    }
  }
  .green-water {
    position: absolute;
    width: 182px;
    height: 140px;
    bottom: 0;
    left: 1rem;
    background: url(~assets/images/project-map-greenwater.png) left bottom;
    background-size: cover;
    animation: wave 10s linear infinite;
  }

  .gray-dam {
    width: calc(60% - 0.5rem);
    height: 100%;
    background: url(~assets/images/project-map-dam.png) left bottom;
    background-size: 100% 100%;
    position: absolute;
    top: 0px;
    left: calc(25% + 1rem);
  }

  .over-flood {
    width: calc(100% - 1rem);
    padding: 0.5rem;
    border-bottom: 2px solid #ff0000;
    color: #ff0000;
    text-align: right;
    position: absolute;
    top: calc(50% - 0.5rem);
    left: 1rem;
  }

  .small-green-water {
    width: calc(15% - 0.5rem);
    height: 100%;
    background: url(~assets/images/project-map-greenwater-s.png) left bottom
      no-repeat;
    background-size: 100% 9%;
    position: absolute;
    top: 0rem;
    left: calc(85% + 0.5rem);
  }
}
</style>
<style lang="scss" scoped>
.project-item {
  .project-item-title {
    font-size: 18px;
    display: flex;
    align-items: center;
    margin-bottom: 8px;
    span {
      color: #20d3ec;
      font-weight: bold;
      margin-left: 8px;
    }
  }
  .split-line {
    width: 100%;
    margin-bottom: 16px;
  }
}
</style>
