<template>
  <div class="work-waterlogging">
    <!-- 地图区域 -->
    <screen-map ref="map" class="screen_map"></screen-map>
    <!-- 左侧菜单 -->
    <div class="bigScreen-left">
      <transition name="fade" v-on:after-leave="transitionDoneLeft">
        <div class="sidebar" :style="{ width: '380px' }" v-show="barshowLeft">
          <div class="screen-title">
            <div class="title-left">
              <i class="icon-judge-icon"></i>
              <span
                :style="{
                  marginRight: '15px',
                  paddingBottom: '10px',
                  borderBottom:
                    switchoverShow == '4' ? '2px solid #23E3FB' : '2px',
                  color: switchoverShow == '4' ? '#23E3FB' : '#728691',
                }"
                @click="switchover('4')"
                >河道水位</span
              >
              <span
                :style="{
                  marginRight: '15px',
                  paddingBottom: '10px',
                  borderBottom:
                    switchoverShow == '3' ? '2px solid #23E3FB' : '2px',
                  color: switchoverShow == '3' ? '#23E3FB' : '#728691',
                }"
                @click="switchover('3')"
                >潮位</span
              >
              <span
                :style="{
                  marginRight: '15px',
                  paddingBottom: '10px',
                  borderBottom:
                    switchoverShow == '2' ? '2px solid #23E3FB' : '2px',
                  color: switchoverShow == '2' ? '#23E3FB' : '#728691',
                }"
                @click="switchover('2')"
                >水库水位</span
              >
              <span
                :style="{
                  paddingBottom: '10px',
                  borderBottom:
                    switchoverShow == '1' ? '2px solid #23E3FB' : '2px',
                  color: switchoverShow == '1' ? '#23E3FB' : '#728691',
                }"
                @click="switchover('1')"
                >水闸水位</span
              >
            </div>
          </div>
          <!-- 所有tab列表水位 -->
          <!-- 河道水位 -->
          <div class="sidebar-list" v-if="switchoverShow == '4'">
            <el-table
              ref="table4"
              :data="riverNetworkData"
              :header-cell-style="{ 'text-align': 'left' }"
              :cell-style="{ 'text-align': 'left' }"
              style="width: 100%"
              height="790"
              highlight-current-row
              @row-click="rowClick"
            >
              <el-table-column type="index" label="序号" width="50">
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="stName"
                label="名称"
                width="150"
              >
                <template slot-scope="scope">
                  <span v-if="scope.row.warning" class="hot-sty">超</span>
                  <span class="table-order">{{ scope.row.stName }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="waterLevel" label="水位 (m)" width="80" />
              <el-table-column
                prop="warningLevel"
                label="警戒水位 (m)"
                width="120"
              />
              <el-table-column
                prop="guaranteeLevel"
                label="保证水位 (m)"
                width="120"
              />
            </el-table>
          </div>
          <!-- 潮位 -->
          <div class="sidebar-list" v-if="switchoverShow == '3'">
            <el-table
              ref="table3"
              :data="waterStationsData"
              :header-cell-style="{ 'text-align': 'left' }"
              :cell-style="{ 'text-align': 'left' }"
              style="width: 100%"
              height="790"
              highlight-current-row
              @row-click="rowClick"
            >
              <el-table-column type="index" label="序号" width="50">
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="stName"
                label="名称"
                width="150"
              />
              <el-table-column prop="tdz" label="水位 (m)" width="80" />
              <el-table-column
                prop="warningLevel"
                label="警戒水位 (m)"
                width="120"
              />
            </el-table>
          </div>
          <!-- 水库水位 -->
          <div class="sidebar-list" v-if="switchoverShow == '2'">
            <el-table
              ref="table2"
              :data="sluiceArr"
              :header-cell-style="{ 'text-align': 'left' }"
              :cell-style="{ 'text-align': 'left' }"
              style="width: 100%"
              height="790"
              highlight-current-row
              @row-click="rowClick"
            >
              <el-table-column type="index" label="序号" width="50">
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="reservoirName"
                label="名称"
                width="150"
              >
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.row.reservoirName }}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="currentWaterLevel"
                label="水位 (m)"
                width="80"
              />
              <el-table-column
                prop="limitLevel"
                label="汛限水位 (m)"
                width="120"
              />
            </el-table>
          </div>
          <!-- 水闸水位 -->
          <div class="sidebar-list" v-if="switchoverShow == '1'">
            <el-table
              ref="table1"
              :data="strobeData"
              :header-cell-style="{ 'text-align': 'left' }"
              :cell-style="{ 'text-align': 'left' }"
              style="width: 100%"
              height="790"
              highlight-current-row
              @row-click="rowClick"
            >
              <el-table-column type="index" label="序号" width="50">
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="stName" label="名称" width="150">
                <template slot-scope="scope">
                  <span v-if="scope.row.warning" class="hot-sty">超</span>
                  <span class="table-order">{{ scope.row.stName }}</span>
                </template>
              </el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="upz"
                label="水位 (m)"
                width="80"
              />
              <el-table-column
                prop="warningLevel"
                label="超警水位 (m)"
                width="120"
              />
            </el-table>
          </div>
          <!-- 左侧边栏切换按钮 -->
          <div class="switch-btn" @click="toggleSideBarLeft()"></div>
        </div>
      </transition>
      <!-- 左侧边栏切换按钮 -->
      <div
        class="switch-btn show-btn"
        v-show="showBtnLeft"
        @click="toggleSideBarLeft()"
      ></div>
    </div>
    <!-- 右侧菜单 -->
    <div class="bigScreen-right">
      <transition name="fade" v-on:after-leave="transitionDone">
        <div class="sidebar" v-show="barshow">
          <!-- 水闸水位 -->
          <div class="sidebar-list" v-if="switchoverShow == '1'">
            <!-- 水闸水位标题 -->
            <div class="screen-title">
              <div class="title-left">
                <i class="icon-judge-icon"></i><span>{{ rightTitleName }}</span>
              </div>
              <div>
                <i
                  class="icon-video-action-btn"
                  style="margin-right: 10px"
                  @click="videoDialogOpen"
                ></i>
                <i
                  class="icon-shipintonghua"
                  style="margin-right: 10px"
                  @click="callDialogOpen"
                ></i>
                <i
                  class="icon-wenjian"
                  style="margin-right: 10px"
                  @click="engineeringOpen"
                ></i>
              </div>
            </div>
            <!-- 水库图表容器 -->
            <echart-api
              ref="echartApi"
              :dialogVisible="waterloggingDialogShow"
              :num="num"
              :echartName="echartName"
              :strobeOvs="strobeOvs"
              :LevelsDataList="LevelsDataList"
              :YechartDataList="YechartDataList"
              :XechartDataList="XechartDataList"
              v-if="YechartDataList"
            />
          </div>
          <!-- 水库水位 -->
          <div class="sidebar-list" v-if="switchoverShow == '2'">
            <!-- 水库水位标题 -->
            <div class="screen-title">
              <div class="title-left">
                <i class="icon-judge-icon"></i><span>{{ rightTitleName }}</span>
              </div>
              <div>
                <i
                  class="icon-video-action-btn"
                  style="margin-right: 10px"
                  @click="videoDialogOpen"
                ></i>
                <i
                  class="icon-shipintonghua"
                  style="margin-right: 10px"
                  @click="callDialogOpen"
                ></i>
                <i
                  class="icon-wenjian"
                  style="margin-right: 10px"
                  @click="engineeringOpen"
                ></i>
                <i
                  class="icon-gongcheng"
                  style="margin-right: 10px"
                  @click="indeterminateOpen"
                ></i>
              </div>
            </div>
            <!-- 水库图表容器 -->
            <echart-api
              ref="echartApi"
              :dialogVisible="waterloggingDialogShow"
              :modelInfo="modelInfo"
              :num="num"
              :echartName="echartName"
              :LevelsDataList="LevelsDataList"
              :YechartDataList="YechartDataList"
              :XechartDataList="XechartDataList"
              v-if="YechartDataList"
            />
          </div>
          <!-- 潮位 -->
          <div class="sidebar-list" v-if="switchoverShow == '3'">
            <div class="screen-title">
              <div class="title-left">
                <i class="icon-judge-icon"></i><span>{{ rightTitleName }}</span>
              </div>
            </div>
            <!-- 潮位图表容器 -->
            <echart-api
              ref="echartApi"
              :dialogVisible="waterloggingDialogShow"
              :echartName="echartName"
              :LevelsDataList="LevelsDataList"
              :YechartDataList="YechartDataList"
              :XechartDataList="XechartDataList"
              v-if="YechartDataList"
            />
          </div>
          <!-- 河道水位 -->
          <div class="sidebar-list" v-if="switchoverShow == '4'">
            <div class="screen-title">
              <div class="title-left">
                <i class="icon-judge-icon"></i><span>{{ rightTitleName }}</span>
              </div>
            </div>
            <!-- 河道图表容器 -->
            <echart-api
              ref="echartApi"
              :dialogVisible="waterloggingDialogShow"
              :echartName="echartName"
              :LevelsDataList="LevelsDataList"
              :YechartDataList="YechartDataList"
              :XechartDataList="XechartDataList"
              v-if="YechartDataList"
            />
          </div>
          <!-- 右侧边栏切换按钮 -->
          <div class="switch-btn" @click="toggleSideBar()"></div>
        </div>
      </transition>
      <!-- 右侧边栏切换按钮 -->
      <div
        class="switch-btn show-btn"
        v-show="showBtn"
        @click="toggleSideBar()"
      ></div>
    </div>
    <!-- 右下侧标识 地图图例 -->
    <div class="map-legend">
      <div class="map-legend-item">
        <div class="legend-title">水闸水位</div>
        <div
          class="legend-detail"
          :key="index"
          v-for="(item, index) in WaterPointsLegendList"
        >
          <i :class="item.type"></i> <span>{{ item.label }}</span>
        </div>
      </div>
      <div class="map-legend-item">
        <div class="legend-title">水库水位</div>
        <div
          class="legend-detail"
          :key="index"
          v-for="(item, index) in waterwayLegendList"
        >
          <i :class="item.type"></i> <span>{{ item.label }}</span>
        </div>
      </div>
      <div class="map-legend-item">
        <div class="legend-title">潮位</div>
        <div
          class="legend-detail"
          :key="index"
          v-for="(item, index) in seaLevelLegendList"
        >
          <i :class="item.type"></i> <span>{{ item.label }}</span>
        </div>
      </div>
      <div class="map-legend-item">
        <div class="legend-title">河道水位</div>
        <div
          class="legend-detail"
          :key="index"
          v-for="(item, index) in strobeLegendList"
        >
          <i :class="item.type"></i> <span>{{ item.label }}</span>
        </div>
      </div>
    </div>
    <!-- 视频弹框 -->
    <video-dialog
      v-if="videoDialogShow"
      :rightTitleName="rightTitleName"
      :reservoirCode="reservoirCode"
      :visible="videoDialogShow"
      @close="videoDialogClose"
    />
    <!-- 可视电话 -->
    <call-dialog
      v-if="callDialogShow"
      :visible="callDialogShow"
      :code="reservoirCode"
      @close="callDialogClose"
    />
    <!-- 待定 -->
    <indeterminate-dialog
      v-if="indeterminate"
      :visible="indeterminate"
      @close="indeterminateDialogClose"
    />
    <!-- 工程特性 -->
    <engineering-dialog
      v-if="engineering"
      :visible="engineering"
      @close="engineeringDialogClose"
    />
  </div>
</template>

<script>
import axios from "axios";
//地图组件引用
import ScreenMap from "../components/ScreenMap";
//右侧侧边栏组件
import echartApi from "./components/echartApi";
//视频弹框
import VideoDialog from "./components/VideoDialog";
//可是电话
import CallDialog from "./components/CallDialog";
//待定
import indeterminateDialog from "./components/indeterminateDialog";
//工程特性
import EngineeringDialog from "./components/EngineeringDialog";
//接口路径
import { rainApi } from "@/api/dataScreen/floodJudge/index";
import { dispatchApi } from "@/api/dataScreen/jointDispatch/JointDispatch";

export default {
  name: "WorkWaterlogging",
  components: {
    ScreenMap,
    echartApi,
    CallDialog,
    VideoDialog,
    EngineeringDialog,
    indeterminateDialog,
  },
  data() {
    return {
      videoDialogShow: false, //视频弹框
      callDialogShow: false, //可视电话
      engineering: false, //工程特性
      indeterminate: false, //待定
      showBtnLeft: false,
      barshowLeft: true,
      showBtn: false,
      barshow: true,
      stationData: "",
      stationType: "",
      //河道水位
      strobeLegendList: [
        {
          label: "正常河道水位",
          type: "icon-zhengchanghedao",
        },
        {
          label: "超警戒河道水位",
          type: "icon-chaojinhedao",
        },
        {
          label: "超保证河道水位",
          type: "icon-chaobaohedao",
        },
      ],
      //潮位
      seaLevelLegendList: [
        {
          label: "正常潮位",
          type: "icon-sea-level",
        },
        {
          label: "超警戒潮位",
          type: "icon-chaojin-chaowei",
        },
      ],
      //水库水位
      waterwayLegendList: [
        {
          label: "正常水库水位",
          type: "icon-zhengchang-shuiku",
        },
        {
          label: "超汛限水库水位",
          type: "icon-chaojinshuiku",
        },
      ],
      //水闸水位
      WaterPointsLegendList: [
        {
          label: "正常水闸水位",
          type: "icon-sluice-gate",
        },
        {
          label: "超警戒水闸水位",
          type: "icon-sluice-gate1",
        },
      ],
      waterloggingDialogShow: true, //内涝弹窗显示
      sluiceArr: [], //水库水位
      riverNetworkData: [], //河网水位
      waterStationsData: [], //潮位
      strobeData: [], //闸门
      switchoverShow: "",
      num: false,
      XechartDataList: [],
      YechartDataList: [],
      LevelsDataList: [],
      echartName: ["水位", "警戒水位"],
      modelInfo: {},
      strobeOvs: [],
      rightTitleName: "",
      reservoirCode: "",
      tokenStr:
        "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.gcGB1bClPatg2GZv2RR66iwdG6R5UsX0kmgkMHEf3wWVrKQIFmIA1Yg16xJgmeGfNbD3A9CTHXiRkVr8gXLtoRtlspBrhwL8OfhK_yAHnsCdaBTJDctWP_Ia7y9UnzmRMYMuTFzredeMIzQikxFSzAMWAyGN0WlHE2bMze_gNOQ.b0ffbanUZTskgR_2.eYyCDUOGbfsBThAJwASO6_wT2OC7vMYIWGaSGCuaNVJlAR0EXX0NDSZvGR8rfH-rUZyJyQgfqZTb80BRjRKX5N67su-1SVyVtu2-lvcfyyyVDtBiqtMKN7z9jDlL2Jvs-pQFBv5QbQpevUZaoSecDReuHovAleQ.ZMO_T18465VDneqtm4K1vw",
    };
  },
  watch: {
    //河网水位
    riverNetworkData(val) {
      this.$nextTick(function () {
        this.$refs.table4.setCurrentRow(this.riverNetworkData[0]);
        this.rightTitleName = this.riverNetworkData[0].stName;
        this.stationData = this.riverNetworkData[0].stCode;
        this.stationType = this.riverNetworkData[0].stType;
        this.waterStation();
      });
      this.$refs.map.drainagePoint(val, "1");
    },
    //潮位
    waterStationsData(val) {
      this.$refs.map.seaLevelPoint(val);
    },
    //闸门
    strobeData(val) {
      this.$refs.map.sluicePoint(val);
    },
  },
  mounted() {
    (this.switchoverShow = "4"),
      this.$nextTick(() => {
        this.wagaList();
        this.waterStations();
        this.riverWaterLevel();
        this.getReservoirWaterAndRain();
        this.sTime =
          this.$moment().subtract(3, "days").format("YYYY-MM-DD HH") + ":00:00";
        this.eTime =
          this.$moment().subtract(-2, "days").format("YYYY-MM-DD HH") +
          ":00:00";
      });
  },
  methods: {
    //左侧打开按钮
    transitionDoneLeft() {
      this.showBtnLeft = true;
    },
    // 左侧关闭按钮
    toggleSideBarLeft() {
      this.showBtnLeft = false;
      this.barshowLeft = !this.barshowLeft;
    },
    //右侧打开按钮
    transitionDone() {
      this.showBtn = true;
    },
    // 右侧关闭按钮
    toggleSideBar() {
      this.showBtn = false;
      this.barshow = !this.barshow;
    },
    // 打开视频对话框
    videoDialogOpen() {
      if (this.reservoirCode) {
        this.videoDialogShow = true;
      }
    },
    videoDialogClose() {
      this.videoDialogShow = false;
    },
    //打开视频通话对话框
    callDialogOpen() {
      if (this.reservoirCode) {
        this.callDialogShow = true;
      }
    },
    callDialogClose() {
      this.callDialogShow = false;
    },
    //工程特性
    engineeringOpen() {
      // this.engineering = true;
    },
    engineeringDialogClose() {
      this.engineering = false;
    },
    //待定
    indeterminateOpen() {
      // this.indeterminate = true;
    },
    indeterminateDialogClose() {
      this.indeterminate = false;
    },
    //右侧tab切换事件
    switchover(val) {
      this.reservoirCode = "";
      this.switchoverShow = val;
      if (val == "4") {
        this.echartName = ["水位", "警戒水位"];
        this.rightTitleName = this.riverNetworkData[0].stName;
        this.stationData = this.riverNetworkData[0].stCode;
        this.stationType = this.riverNetworkData[0].stType;
        this.waterStation();
      } else if (val == "3") {
        this.echartName = ["潮位", "警戒潮位"];
        this.rightTitleName = this.waterStationsData[0].stName;
        this.stationData = this.waterStationsData[0].stCode;
        this.stationType = this.waterStationsData[0].stType;
        this.waterStation();
      } else if (val == "2") {
        this.num = "2";
        this.echartName = ["水位", "汛限水位"];
        this.reservoirCode = this.sluiceArr[0].reservoirCode;
        this.rightTitleName = this.sluiceArr[0].reservoirName;
        this.stationData = this.sluiceArr[0].stCode;
        this.stationType = this.sluiceArr[0].stType;
        this.waterStation();
        this.getDetails(
          this.sTime,
          this.eTime,
          1.0,
          this.sluiceArr[0].limitLevel,
          1.0,
          this.sluiceArr[0].floodId
        );
      } else if (val == "1") {
        this.num = "1";
        this.echartName = ["水位", "警戒水位"];
        this.reservoirCode = this.strobeData[0].projCode;
        this.rightTitleName = this.strobeData[0].stName;
        this.stationData = this.strobeData[0].stCode;
        this.stationType = this.strobeData[0].stType;

        this.waterStation();
      }
    },
    //河网水位
    async riverWaterLevel() {
      await dispatchApi.riverWaterLevel().then((res) => {
        if (res.code == 0) {
          this.riverNetworkData = JSON.parse(JSON.stringify(res.data));
          this.$refs.map.drainagePoint(res.data, "1");
        }
      });
    },
    //潮位
    async waterStations() {
      let opt = {
        stationType: "TT",
      };
      await dispatchApi.waterStations(opt).then((res) => {
        if (res.code == 0) {
          this.waterStationsData = JSON.parse(JSON.stringify(res.data));
          this.$refs.map.seaLevelPoint(res.data);
        }
      });
    },
    //大中型水库水位
    async getReservoirWaterAndRain() {
      await rainApi.reservoirWaterAndRain().then((res) => {
        if (res.code == 0) {
          this.sluiceArr = JSON.parse(JSON.stringify(res.data));
          this.$refs.map.reservoirPoint();
        }
      });
    },
    //闸门
    async wagaList() {
      await dispatchApi.wagaList().then((res) => {
        if (res.code == 0) {
          this.strobeData = JSON.parse(JSON.stringify(res.data));
          this.$refs.map.sluicePoint(res.data);
        }
      });
    },
    //tab行点击事件
    rowClick(row) {
      this.rightTitleName = row.stName;
      this.stationData = row.stCode;
      this.stationType = row.stType;
      this.waterStation();
      if (this.switchoverShow == "2") {
        this.reservoirCode = row.reservoirCode;
        this.rightTitleName = row.reservoirName;
        this.getDetails(
          this.sTime,
          this.eTime,
          1.0,
          row.limitLevel,
          1.0,
          row.floodId
        );
      } else if (this.switchoverShow == "1") {
        this.reservoirCode = row.projCode;
      }
    },
    //图标数据获取
    async getDetails(
      sTime,
      eTime,
      flowZoom,
      limitWater,
      outflowZoom,
      projectId
    ) {
      await axios({
        url: `http://10.36.160.145:8888/mgt/bus/floodDispatch/dispatch?token=${this.tokenStr}&startTime=${sTime}&endTime=${eTime}&flowZoom=${flowZoom}&limitWater=${limitWater}&jsonFloodArray=[]&jsonWaveArray=[]&jsonSplitWaveArray=[]&outflowZoom=${outflowZoom}&outflowArray=[]&projectId=${projectId}`,
        method: "get",
      }).then((res) => {
        if (res.data.success) {
          this.modelInfo = res.data;
        }
      });
    },
    //曲线图接口数据
    async waterStation() {
      this.YechartDataList = [];
      this.XechartDataList = [];
      this.LevelsDataList = [];
      this.strobeOvs = [];
      let obj = {
        stationCode: this.stationData,
        stationType: this.stationType,
        days: this.$refs.echartApi.time,
      };
      await dispatchApi.waterStationInfo(obj).then((res) => {
        if (res.code == 0) {
          let arr = res.data.data.reverse();
          arr.forEach((val) => {
            if (this.switchoverShow == "1") {
              this.YechartDataList.push(val.upz);
              this.XechartDataList.push(val.tm.slice(5, 10));
              this.LevelsDataList.push(res.data.warningLevel);
              this.strobeOvs.push(val.ovs);
            } else if (this.switchoverShow == "2") {
              this.YechartDataList.push(val.rz);
              this.XechartDataList.push(val.tm.slice(5, 10));
              this.LevelsDataList.push(res.data.limitLevel);
            } else if (this.switchoverShow == "3") {
              this.YechartDataList.push(val.tdz);
              this.XechartDataList.push(val.tm.slice(5, 10));
              this.LevelsDataList.push(res.data.warningLevel);
            } else if (this.switchoverShow == "4") {
              this.YechartDataList.push(val.z);
              this.XechartDataList.push(val.tm.slice(5, 10));
              this.LevelsDataList.push(res.data.warningLevel);
            }
          });
          this.$nextTick(() => {
            this.$refs.echartApi.drawChart();
          });
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
.work-waterlogging {
  position: relative;
  width: 100%;
  height: 100%;
  .weather-forecast {
    width: 380px;
    height: 126px;
    position: absolute;
    left: 25px;
    @include flexbox;
    @include flexflow(row, nowrap);
    .weather-item {
      @include flex(1);
      font-size: 14px;
      color: #ffffff;
      div {
        text-align: center;
        &.weather-type {
          margin: 8px 0 16px;
          i {
            display: inline-block;
            width: 36px;
            height: 36px;
          }
        }
      }
    }
  }
  ::v-deep.sidebar-list .el-table .cell {
    font-size: 14px;
    opacity: 0.8;
  }
  //左侧导航栏样式
  .bigScreen-left {
    position: absolute;
    top: 80px;
    .switch-btn {
      height: 862px !important;
    }
    ::v-deep.el-table__body-wrapper::-webkit-scrollbar {
      width: 0px;
    }
    .hot-sty {
      background: red;
      color: #ffffff;
      border: 1px solid red;
      padding: 2px 3px;
      margin-right: 5px;
      font-size: 12px;
      border-radius: 12px;
    }
  }
  //右侧导航栏样式
  .bigScreen-right {
    .sidebar .sidebar-box {
      width: 104% !important;
      .tab-sty {
        border-bottom: 2px solid red !important;
      }
    }
    .switch-btn {
      height: 750px !important;
    }
  }
}
</style>