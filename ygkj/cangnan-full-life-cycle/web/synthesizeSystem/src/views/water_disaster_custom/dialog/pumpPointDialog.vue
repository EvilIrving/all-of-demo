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
        <p class="primary-dialog__title--label">泵站详情</p>
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
            v-if="activeTab == '基本信息' || activeTab == '工程特性'"
          >
            <div class="info-top-panel">
              <img :src="img" alt="" />
              <div class="info-top-try">
                <div style="padding-top: 15px; font-size: 20px">
                  <p>{{ obj.pustName }}</p>
                  <p />
                </div>
                <el-row :gutter="20">
                  <div style="padding-top: 20px; font-size: 15px">
                    <el-col :span="10">
                      <span class="name">行政区划</span>
                      <span class="value">{{ obj.cityAdnm }}</span>
                    </el-col>
                    <el-col :span="6">
                      <span class="name">管理层级</span>
                      <span class="value">{{ obj.managementLevel || "-" }}</span>
                    </el-col>
                    <el-col :span="8" style="text-align: right">
                      <span class="name">所在河流</span>
                      <span class="value">{{ obj.bas || "-" }}</span>
                    </el-col>
                  </div>
                </el-row>
                <el-row :gutter="20">
                  <div style="padding-top: 1rem; font-size: 16px">
                    <el-col :span="10">
                      <span class="name">装机流量（m³/s）</span>
                      <span class="value">{{ obj.insFlow || "-" }}</span>
                    </el-col>
                    <el-col :span="6">
                      <span class="name">装机功率（KW）</span>
                      <span class="value">{{ obj.insPow || "-" }}</span>
                    </el-col>
                  </div>
                </el-row>
                <el-row :gutter="20">
                  <div style="padding-top: 1rem; font-size: 16px">
                    <el-col :span="5">
                      <span class="name">机组1状态</span>
                      <span class="value">{{ obj.num1 || "-" }}</span>
                    </el-col>
                    <el-col :span="5">
                      <span class="name">机组2状态</span>
                      <span class="value">{{ obj.num2 || "-" }}</span>
                    </el-col>
                    <el-col :span="5">
                      <span class="name">机组3状态</span>
                      <span class="value">{{ obj.num3 || "-" }}</span>
                    </el-col>
                    <el-col :span="5">
                      <span class="name">机组4状态</span>
                      <span class="value">{{ obj.num4 || "-" }}</span>
                    </el-col>
                    <el-col :span="4">
                      <span class="name">机组5状态</span>
                      <span class="value">{{ obj.num5 || "-" }}</span>
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
                  <span class="name">所在水资源三级区名称</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">所在灌区名称</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">所在河流（湖、库、渠、海堤）名称</span>
                  <span class="value">{{ obj.bas || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">所在流域</span>
                  <span class="value">{{ obj.bas || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">是否竣工验收</span>
                  <span class="value">{{ obj.compDate || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">是否为闸站工程</span>
                  <span class="value">{{ obj.isGateStationProject || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">闸站参照水位站名称</span>
                  <span class="value">{{
                    obj.pumpReferenceWlStationName || "-"
                  }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">闸站参照水位站编码</span>
                  <span class="value">{{
                    obj.pumpReferenceWlStationCode || "-"
                  }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">闸站参照水位站警戒水位（m）</span>
                  <span class="value">{{
                    obj.pumpReferenceWlStationWarningLevel || "-"
                  }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">闸站参照水位站危机水位（m）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">工程任务</span>
                  <span class="value">{{ obj.engTask || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">开工时间</span>
                  <span class="value">{{ obj.startDate || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">完工时间</span>
                  <span class="value">{{ obj.compDate || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">竣工验收时间（非加固）</span>
                  <span class="value">{{ obj.isComplete || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">泵站类型</span>
                  <span class="value">{{ obj.pustType || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">所在村名称</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">工程建设情况</span>
                  <span class="value">{{ obj.engStart || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">归口管理部门</span>
                  <span class="value">{{ obj.admDep || "-" }}</span>
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
                  <span class="name">内河常水位（m）</span>
                  <span class="value">{{ obj.riverWaterLevel || "-" }}</span>
                </el-col>
              </el-row>
            </div>
          </div>

          <!-- 工程特性信息 -->
          <div class="list-top-panel" v-if="activeTab == '工程特性'">
            <div class="info-down-panel">
              <el-row :gutter="20">
                <el-col :span="8">
                  <span class="name">流域面积</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">主要建筑物级别</span>
                  <span class="value">{{ obj.mainBuildGrad || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">内河常水位（m）</span>
                  <span class="value">{{ obj.riverWaterLevel || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">前池排涝期设计运行水位（m）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">前池排涝期最低运行水位（m）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">出水池排涝期最高运行水位（m）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">出水池排涝期设计运行水位（m）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">出水池排涝期最低运行水位（m）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">外江设计高潮位（m）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">外江设计低潮位（m）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">外江多年平均高潮位（m）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">外江多年平均低潮位（m）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">防洪排涝流量（m³/s）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">设计饮水流量（m³/s）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">历史饮水保证率</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">年饮水流量（m³/s）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">多年平均年饮水流量（m³/s）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">水泵台数</span>
                  <span class="value">{{ obj.pumpNum || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">电动机台数</span>
                  <span class="value">{{ obj.motorNumber || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">输电线路电压（KV）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">是否为闸站工程</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">是否为引泉泵站</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">典型横断面图</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">总平面图布置图</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">单台设计流量（m³/s）</span>
                  <span class="value">-</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">装机流量（m³/s）</span>
                  <span class="value">{{ obj.insFlow || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">水泵设计扬程</span>
                  <span class="value">{{ obj.desHead || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="name">电动机装机功率（KW）</span>
                  <span class="value">{{ obj.insPow || "-" }}</span>
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
      test: "",
      activeTab: "基本信息", //默认
      // listActive: "建设档案",
      videoLoading: false,
      obj: {},
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
  created() {
    this.pustProject();
  },
  methods: {
    init(val) {
      this.pustName = val.name;
      console.log(this.pustName, "??????");

      this.$nextTick(() => {
        this.pustproject();
      });
    },
    async pustProject() {
      await disasterPreventionApi
        .pustProject({ pustName: this.pustName })
        .then((res) => {
          if (res.code == 0) {
            let str = res.data.list.find((val)=>{
              if (val.pustName == this.pustName) {
                return val;
              }
            });
            this.obj = str;
          }
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
     
    },

 
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
      height: 30rem;
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
    
    // height: 13.25rem;
    // overflow: auto;
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
    padding: 16px;
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
    width: 855px;
    height: 458px;
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
