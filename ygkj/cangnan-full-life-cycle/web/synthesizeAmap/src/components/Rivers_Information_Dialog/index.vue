<template>
  <div :class="[leftSide == true ? 'project-dialog-wrap' : '']">
    <el-dialog
      :modal="modal"
      :visible.sync="visible"
      :width="width"
      top="10vh"
      :before-close="handleClose"
      :append-to-body="appendToBody"
      :fullscreen="fullscreen"
      :close-on-click-modal="false"
      class="primary-dialog"
    >
      <div
        class="primary-dialog__title"
        slot="title"
        :style="minimizeStatus ? 'justify-content: space-between;' : ''"
      >
        <p class="primary-dialog__title--label">{{ title }}</p>
        <i
          class="minimize el-icon-minus"
          @click="minimize"
          v-if="minimizeStatus"
        />
      </div>
      <div
        class="primary-dialog__content"
        style="overflow-x: clip; overflow-y: auto; height: 620px"
      >
      <div v-if="ProgrammeData.type == '地下水位站' || ProgrammeData.type == '地下机井'">
        <el-row>
          <el-col :span="12" v-for="(item, i) in PublicHeaderList" :key="i" style="margin:10px 0;">
            <span style="color:#fff;width:145px;display: inline-block;text-align: right;">{{item.label}}</span>
            <el-input style="width:300px;margin-left:10px;" :placeholder="form[item.prop]" />
          </el-col>
        </el-row>
      </div>
        <!-- 河景全景图 -->
        <iframe
          v-if="ProgrammeData.type == '河景'"
          :src="panoramagram"
          frameborder="0"
          style="width: 100%; height: 680px"
        />
        <div v-if="ProgrammeData.type == '取水口' || ProgrammeData.type == '河道'">
          <!-- tab切换 -->
          <div>
            <span
              v-if="ProgrammeData.type == '取水口'"
              :class="tabSwitch == '日取水量' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('日取水量')"
              >日取水量</span
            >
            <span
              v-if="ProgrammeData.type == '取水口'"
              :class="tabSwitch == '月取水量' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('月取水量')"
              >月取水量</span
            >
            <!-- 河道 -->
            <span
              v-if="ProgrammeData.type == '河道'"
              :class="tabSwitch == '基础信息' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('基础信息')"
              >基础信息</span
            >
            <span
              v-if="ProgrammeData.type == '河道'"
              :class="tabSwitch == '健康信息' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('健康信息')"
              >健康信息</span
            >
            <span
              v-if="ProgrammeData.type == '河道'"
              :class="
                tabSwitch == '巡查信息' ? 'tab-switch' : 'tab-switch-sty'
              "
              @click="tabSwitchClick('巡查信息')"
              >巡查信息</span
            >
            <i v-if="backData" style="float:right;" class="icon-get-back" @click="backtrack" />
          </div>
          <!-- 河道基础信息 -->
          <div v-if="ProgrammeData.type == '河道' && !backData" style="margin-top:15px;">
            <!-- 基础信息 -->
            <el-row v-if="tabSwitch == '基础信息'">
              <el-col :span="24">
                <ProjectItem title="河道基础信息" />
                <el-col  :span="11" class="riverway-sty" v-for="(item, i) in channelLnformationList" :key="i">
                  <span>{{item.label}}</span>
                  <span>{{rivSegInfoList[item.prop]}}</span>
                </el-col>
              </el-col>
              <el-col :span="24">
                <ProjectItem title="河长基础信息">
                <span v-for="(item, i) in riverChiefName" :key="i" v-show="riverChief" :class="riverChief == item ? 'tab-switch' : 'tab-switch-sty'" @click="riverChiefClick(item)">{{item}}</span>
                </ProjectItem>
                <el-col v-show="riverChief" style="margin:10px 38px 10px 0;" :span="11" class="riverway-sty" v-for="(item, i) in riverChiefList" :key="i">
                  <span>{{item.label}}</span>
                  <span>{{riverChiefData[item.prop]}}</span>
                </el-col>
              </el-col>
            </el-row>
            <!-- 健康信息 -->
            <el-row v-if="tabSwitch == '健康信息'" style="margin-top:10px;">
              <ProjectItem title="健康信息" />
              <el-col :span="13" style="margin-top:20px;display: flex;justify-content: space-between;">
                <div style="width:95%;">
                  <div class="health-code-sty">
                    <i v-if="rivSegInfoList.healthState == 1" class="icon-the-green-code" />
                    <i v-if="rivSegInfoList.healthState == 2" class="icon-the-yellow-code" />
                    <i v-if="rivSegInfoList.healthState == 3" class="icon-the-red-code" />
                  </div>
                  <div class="health-sty">
                    <p>河湖“四乱”清零<span>{{rivSegInfoList.noneFourChaos == 1 ? '是' : '否'}}</span></p>
                    <p>河湖管护到位 <span>{{rivSegInfoList.riverManageWell == 1 ? '是' : '否'}}</span></p>
                  </div>
                  <div class="health-sty">
                    <p>无乱点河道创建 <span>{{rivSegInfoList.noneIllegalRiverCreate == 1 ? '是' : '否'}}</span></p>
                    <p>水域岸线占补平衡 <span>{{rivSegInfoList.shorelineOccupyBalance == 1 ? '是' : '否'}}</span></p>
                  </div>
                  <div class="health-sty">
                    <p>涉河建设项目批建相符 <span>{{rivSegInfoList.riverProjTallied == 1 ? '是' : '否'}}</span></p>
                    <p>公众满意 <span>{{rivSegInfoList.publicSatisfy == 1 ? '是' : '否'}}</span></p>
                  </div>
                </div>
                <div class="boundary-sty"></div>
              </el-col>
              <el-col :span="11">
                <div id="radarMap" style="width:450px;height:500px;"></div>
              </el-col>
            </el-row>
            <!-- 巡查信息 -->
            <div v-if="tabSwitch == '巡查信息'" style="margin-top:10px;">
              <el-row class="under-frame-sty">
                <el-col :span="6" class="inspect-number-sty">
                    <p><i class="icon-amount" /></p>
                    <div>
                      <p>本月巡查总次数</p>
                      <p><span>{{stats4LoadPageList.times}}</span>次</p>
                    </div>
                </el-col>
                <el-col :span="6" class="inspect-number-sty">
                  <p><i class="icon-amount" /></p>
                  <div>
                    <p>本月巡查总长度</p>
                    <p><span>{{stats4LoadPageList.inspectLength}}</span>km</p>
                  </div>
                </el-col>
                <el-col :span="6" class="inspect-number-sty">
                  <p><i class="icon-amount" /></p>
                  <div>
                    <p>本月巡查总时长</p>
                    <p><span>{{stats4LoadPageList.inspectTime}}</span>h</p>
                  </div>
                </el-col>
                <el-col :span="6" class="inspect-number-sty">
                  <p><i class="icon-hong-ma" /></p>
                  <div>
                    <p>本月巡查问题总数</p>
                    <p><span style="color:red;">{{stats4LoadPageList.problemNum}}</span>个</p>
                  </div>
                </el-col>
              </el-row>
              <el-row style="margin:15px 0;">
                <el-col :span="5">
                  <p style="font-size:16px;color:#fff;margin-bottom:10px;">所属单位</p>
                  <el-select v-model="affiliatedUnitData" placeholder="请选择" style="width: 150px;">
                    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"/>
                  </el-select>
                </el-col>
                <el-col :span="5">
                  <p style="font-size:16px;color:#fff;margin-bottom:10px;">巡查开始时间</p>
                  <el-date-picker style="width: 150px;" value-format="yyyy-MM-dd HH:mm" format="yyyy-MM-dd HH:mm" v-model="startTime" type="datetime" placeholder="巡查时间"/>
                </el-col>
                <el-col :span="5">
                  <p style="font-size:16px;color:#fff;margin-bottom:10px;">巡查结束时间</p>
                  <el-date-picker style="width: 150px;" value-format="yyyy-MM-dd HH:mm" format="yyyy-MM-dd HH:mm" v-model="endTime" type="datetime" placeholder="巡查时间"/>
                </el-col>
                <el-col :span="5">
                  <p style="font-size:16px;color:#fff;margin-bottom:10px;">巡查人员</p>
                  <el-input v-model="inspectorsData" placeholder="请输入" style="width: 150px;" />
                </el-col>
                <el-col :span="4">
                  <span class="btn-sty" @click="inquire">查询</span>
                  <span class="btn-sty" @click="empty">清空</span>
                </el-col>
              </el-row>
              <!-- 巡查列表 -->
              <div class="table-box" style="height: 380px">
                <primary-map-table :columnList="columnLists" @rowClick="rowClick" :dataList="loadPageList" />
              </div>
            </div>
          </div>
          <!-- 巡查路劲 -->
          <div v-if="backData" style="margin-top:15px; height:550px;overflow-y: auto;">
            <ProjectItem title="巡查路径" />
            <div style="padding-bottom: 10px;height: 470px">
              <ComparativeHistoryMap v-if="visible" :mapShowsList="mapShowsList" />
            </div>
            <!-- 巡查情况 -->
            <ProjectItem title="巡查情况" />
            <el-row>
              <el-col :span="11" class="riverway-sty" v-for="(item, i) in columnLists" :key="i" v-show="item.label != '巡查问题(个)'">
                <span>{{item.label}}</span>
                <span>{{inspectorsList[item.prop]}}</span>
              </el-col>
            </el-row>
            <!-- 问题记录 -->
            <div>
              <ProjectItem title="问题记录" />
              <el-collapse v-model="activeName" accordion v-if="inspectorsList.patrolProblemRespVos">
                <el-collapse-item v-for="(item, i) in inspectorsList.patrolProblemRespVos" :key="i" :title="'问题点' + `${i+1}` + ':' + item.problemDescription" :name="i">
                  <el-row style="padding:20px 0 0 20px;">
                    <el-col :span="11" class="riverway-sty">
                      <span>详细地址</span>
                      <span>{{item.address}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>所在乡镇</span>
                      <span>{{item.town}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>所在街道</span>
                      <span>{{item.street}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>地理坐标</span>
                      <span>{{item.lng}}-{{item.lat}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>问题来源</span>
                      <span>{{item.problemSource}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>所在河湖</span>
                      <span>{{item.riverName}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>河道等级</span>
                      <span>{{item.riverLevel}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>河长姓名</span>
                      <span>{{item.riverManagerName}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>四乱类型</span>
                      <span>{{item.fourType}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>严重程度</span>
                      <span>{{item.severity}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>备注说明</span>
                      <span>{{item.remark}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>占地面积</span>
                      <span>{{item.occupySpace}}</span>
                    </el-col>
                    <el-col :span="23" class="riverway-sty">
                      <span>整改要求</span>
                      <span>{{item.rectificationRequirement}}</span>
                    </el-col>
                   <el-col :span="11" class="riverway-sty">
                      <span>整改前图像</span>
                      <span>{{item.rectImgbeforeList}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>整改后图像</span>
                      <span>{{item.rectImgAfter}}</span>
                    </el-col>
                  </el-row>
                </el-collapse-item>
              </el-collapse>
              <div v-else style="text-align: center;">
                <i class="icon-no-record"></i>
              </div>
            </div>
          </div>
          <!-- 图表，列表 -->
          <el-row v-if="ProgrammeData.type == '取水口'">
            <el-col :span="16">
              <!-- 图表 -->
              <div id="histogramChart" style="width: 100%; height: 560px" />
            </el-col>
            <!-- 列表 -->
            <el-col :span="8">
              <div class="table-box" style="height: 500px">
                <primary-map-table :columnList="columnList" :dataList="dataList"/>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as echarts from "echarts";
  import ComparativeHistoryMap from "../comparative_history_map/";
  import ProjectItem from "../screen_project_dialog/components/ProjectItem";
  import  PrimaryMapTable  from "../primary_map_table/";
  import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
  export default {
    name: "RiversInformationDialog",
    props: {
      ProgrammeData: {
        type: Object,
        default: null,
      },
      visible: {
        type: Boolean,
        default: false,
      },
      appendToBody: {
        type: Boolean,
        default: false,
      },
      width: {
        type: String,
        default: "1000px",
      },
      mainTitle: {
        type: String,
        default: "测试",
      },
      fullscreen: {
        type: Boolean,
        default: false,
      },
      leftSide: {
        type: Boolean,
        default: false,
      },
      minimizeStatus: {
        type: Boolean,
        default: false,
      },
      hasAnalysis: { type: Boolean, default: true },
      modal: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        mapShowsList: [],
        // 巡查人员表字段
        inspectorsList: {},
        // 河长
        riverChief: null,
        activeName: '1',
        riverChiefName: [],
        riverChiefData: {},
        riverChiefList: [
          {
            label: "河长等级",
            prop: "levelName",
          },
          {
            label: "河长身份",
            prop: "rpostName",
          },
          {
            label: "河长姓名",
            prop: "realName",
          },
          {
            label: "河长职务",
            prop: "post",
          },
          {
            label: "联系方式",
            prop: "phone",
          }
        ],
        channelLnformationList: [
          {
            label: "行政区划",
            prop: "areaName",
          },
          {
            label: "所属流域",
            prop: "watershedName",
          },
          {
            label: "河道等级",
            prop: "levelName",
          },
          {
            label: "起点名称",
            prop: "startName",
          },
          {
            label: "终点名称",
            prop: "endName",
          },
          {
            label: "河道长度",
            prop: "length",
          }
        ],
        panoramagram: "",
        title: "",
        // 巡查列表表头
        columnLists: [
          {
            width: 110,
            prop: "patrolUserName",
            label: "巡查人员",
          },
          {
            width: 160,
            prop: "deptName",
            label: "所属单位",
          },
          {
            width: 160,
            prop: "startTime",
            label: "开始时间",
          },
          {
            width: 160,
            prop: "endTime",
            label: "结束时间",
          },
          {
            width: 160,
            prop: "inspectTime",
            label: "巡查时长(h)",
            sortable: true,
          },
          {
            width: 160,
            prop: "inspectLength",
            label: "巡查长度(km)",
            sortable: true,
          },
          {
            width: 160,
            prop: "problemNum",
            label: "巡查问题(个)",
            sortable: true,
          }
        ],
        // 表头
        columnList: [
          {
            width: 160,
            prop: "tm",
            label: "时间",
          },
          {
            width: 110,
            prop: "intake",
            label: "取水量（m³）",
          },
        ],
        dataList: [],
        tabSwitch: "日取水量",
        yearData: [],
        overviewData: [],
        legendName: ["取水量", "取水累积量"],
        waterLevelArr: [
          {
            label: "编号",
            prop: "stationCode",
          },
          {
            label: "水位站名称",
            prop: "stationName",
          },
          {
            label: "sim卡卡号",
            prop: "sim",
          },
          {
            label: "表具号",
            prop: "meterNumber",
          },
          {
            label: "每日最大值",
            prop: "maxValue",
          },
          {
            label: "行政区划",
            prop: "areaName",
          },
          {
            label: "取水地点",
            prop: "waterPlace",
          },
          {
            label: "管理单位",
            prop: "manageUnit",
          },
          {
            label: "负责人",
            prop: "chargePerson",
          },
          {
            label: "负责人电话",
            prop: "chargePersonPhone",
          },
          {
            label: "日常管理单位",
            prop: "dailyChargeUnit",
          },
        ],
        pumpWellArr: [
          {
            label: "取水井代码",
            prop: "code",
          },
          {
            label: "取水井名称",
            prop: "name",
          },
          {
            label: "取水井所在位置",
            prop: "address",
          },
          {
            label: "井深",
            prop: "deep",
          },
          {
            label: "井口井管内径",
            prop: "internalDiameter",
          },
          {
            label: "井壁管材料",
            prop: "meterials",
          },
          {
            label: "成井年份",
            prop: "buildYear",
          },
          {
            label: "设计年取水量（万m³）",
            prop: "yearUseWater",
          },
          {
            label: "水源类型",
            prop: "waterResourceType",
          },
          {
            label: "备注",
            prop: "remark",
          },
        ],
        form:{},
        affiliatedUniData: null,
        endTime: null,
        startTime: null,
        inspectorsData: null,
        backData: false,
        rivSegInfoList: {},
        stats4LoadPageList: {},
        loadPageList: [],
        affiliatedUnitData:null,
        options: []
      };
    },
    components:{
      ProjectItem,
      PrimaryMapTable,
      ComparativeHistoryMap
    },
    mounted() {
      this.$nextTick(() => {
        this.title = this.ProgrammeData.name || this.ProgrammeData.wiuName || this.ProgrammeData.stationName;
        if (this.ProgrammeData.type == '地下水位站') {
          this.PublicHeaderList = this.waterLevelArr;
          this.form = this.ProgrammeData;
        }else if (this.ProgrammeData.type == '地下机井') {
          this.PublicHeaderList = this.pumpWellArr;
          this.form = this.ProgrammeData;
        }
        if(this.ProgrammeData.type == '河道') {
          this.tabSwitch = '基础信息';
          this.rivSegInfoById(this.ProgrammeData.id);
          this.stats4LoadPage();
          this.loadPage();
        }
        this.panoramagram = this.ProgrammeData.browseUrl;
        this.intakeCurve("1", this.ProgrammeData.wainCode);
      });
    },
    methods: {
      // 分页查找上面的统计
      async stats4LoadPage() {
        let opt = {
          patrolUserName: this.inspectorsData, //巡查人员姓名
          deptName: this.affiliatedUnitData,   //所属单位
          startTime: this.startTime,           //巡查开始时间
          endTime: this.endTime,               //巡查开始时间
           id: this.ProgrammeData.id
         }
         await hydraulicApi.stats4LoadPage(opt).then((res) => {
          if (res.code == 0) {
            this.stats4LoadPageList = res.data;
          }
        });
      },
      // 巡查列表接口
       async loadPage() {
         let opt = {
          patrolUserName: this.inspectorsData, //巡查人员姓名
          deptName: this.affiliatedUnitData,   //所属单位
          startTime: this.startTime,           //巡查开始时间
          endTime: this.endTime,               //巡查开始时间
          //  id: this.ProgrammeData.id
         }
         await hydraulicApi.loadPage(opt).then((res) => {
          if (res.code == 0) {
            this.loadPageList = res.data.list;
          }
        });
       },
      // 河长选择
      riverChiefClick(val) {
        this.riverChief = val;
        let str = this.rivSegInfoList.riverChiefList.find((item) => {
          if (item.realName == val) {
            return item;
          }
        });
        this.riverChiefData = str;
      },
      // 河道健康码，基础信息，健康信息接口
      async rivSegInfoById(val) {
        this.riverChiefName = [];
        await hydraulicApi.rivSegInfoById({id: val,withChiefInfo:true}).then((res) => {
          if (res.code == 0) {
            this.rivSegInfoList = res.data;
            this.riverChiefData = res.data.riverChiefList[0];
            this.riverChief = res.data.riverChiefList[0].realName
            res.data.riverChiefList.forEach((item)=> {
              this.riverChiefName.push(item.realName);
            })
          }
        })
      },
      // 巡查路径返回
      backtrack() {
        this.backData = false;
      },
      // 巡查列表点击某一行事件
      rowClick(val) {
        this.inspectorsList = val;
        this.backData = true;
      },
      // 巡查查询按钮
      inquire() {
        this.loadPage();
        this.stats4LoadPage();
      },
      // 巡查筛选清楚按钮
      empty() {
        this.affiliatedUnitData = null;
        this.inspectorsData = null;
        this.startTime = null;             //巡查开始时间
        this.endTime = null;               //巡查开始时间
      },
      // 健康信息雷达图
      radarMapApi() {
        var chartDom = document.getElementById('radarMap');
        var myChart = echarts.init(chartDom);
        var option = {
            // title: {
            //     text: '基础雷达图'
            // },
            tooltip: {
                trigger: 'axis'
            },
            // legend: {
            //     data: ['无乱点河道创建', '河湖管护到位']
            // },
            radar: {
                // shape: 'circle',
                indicator: [
                    { name: '河湖“四乱”清零'},
                    { name: '无乱点河道创建'},
                    { name: '水域岸线占补平衡'},
                    { name: '涉河建设项目批建相符'},
                    { name: '公众满意'},
                    { name: '河湖管护到位'}
                ],
                // axisLine: {            // 坐标轴线
                //     show: false        // 默认显示，属性show控制显示与否
                // },
                name:{
                  textStyle:{color:'#fff'}
                 },
                splitArea : {
                    show : true,   
                    areaStyle : {
                      opacity: 0.2,
                      color: ["#050E30"]  // 图表背景网格的颜色
                    }
                },
                splitLine : {
                    show : true,
                    lineStyle : {
                        width : 1,
                        color : '#fff' // 图表背景网格线的颜色
                    }
                },
                radius: 130,
            },
            series: [{
                // name: '预算 vs 开销（Budget vs spending）',
                //  tooltip: {
                //     trigger: 'item'
                // },
                symbol: "none", // 去掉图表中各个图区域的边框线拐点
                type: 'radar',
                itemStyle: {
                    normal: {
                        color : "#33FFFF", // 图表中各个图区域的边框线拐点颜色
                        // lineStyle: {
                        //     color:"#33FFFF" // 图表中各个图区域的边框线颜色
                        // },
                        areaStyle: {
                            type: 'default'
                        }
                    }
                },
                data: [
                    {
                        value: [1, 1, 0, 0, 1, 1],
                        itemStyle: {
                            normal: {
                                areaStyle: {
                                    type: 'default',
                                    opacity: 0.8,
                                    color: "#33FFFF" // 图表中各个图区域的颜色
                                }
                            }
                        },
                        name: '无乱点河道创建'
                    },
                    {
                        value: [1, 1, 0, 0, 1, 1],
                        itemStyle: {
                            normal: {
                                areaStyle: {
                                    type: 'default',
                                    opacity: 0.8,
                                    color: "red" // 图表中各个图区域的颜色
                                }
                            }
                        },
                        name: '河湖管护到位'
                    }
                ]
            }]
        };
        option && myChart.setOption(option);
      },
      // tab切换
      tabSwitchClick(val) {
        this.tabSwitch = val;
        if (val == "日取水量") {
          this.intakeCurve("1", this.ProgrammeData.wainCode);
        } else if (val == "月取水量") {
          this.intakeCurve("2", this.ProgrammeData.wainCode);
        } else if (val == '健康信息') {
          this.$nextTick(()=>{
            this.radarMapApi();
          });
        }
      },
      // 取水口曲线接口数据
      async intakeCurve(val, type) {
        let obj = {
          wainCode: type, //取水口编码
          type: val, //1，日取水量；2，月取水量
        };
        await hydraulicApi.intakeCurve(obj).then((res) => {
          if (res.code == 0) {
            this.overviewData = [];
            this.yearData = [];
            this.dataList = res.data;
            let arr = [];
            let str = [];
            res.data.forEach((val) => {
              arr.push(val.intake);
              str.push(val.intakeSum);
              this.yearData.push(val.tm2Str);
            });
            this.overviewData.push(
              {
                name: "取水量",
                type: "bar",
                barWidth: "25",
                color: "#56FEFE",
                data: arr,
              },
              {
                name: "取水累积量",
                type: "line",
                color: "#56FEFE",
                yAxisIndex: 1,
                data: str,
              }
            );
            this.histogram();
          }
        })
      },
      // 柱状，折线图
      histogram() {
        var chartDom = document.getElementById("histogramChart");
        var myChart = echarts.init(chartDom);
        var option;
        option = {
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "cross",
              crossStyle: {
                color: "#999",
              },
            },
          },
          legend: {
            top: "0",
            right: "20",
            data: this.legendName,
            textStyle: {
              color: "#fff", //这里用参数代替了
            },
          },
          grid: {
            top: "60",
            left: "10",
            right: "30",
            bottom: "10",
            containLabel: true,
          },
          xAxis: [
            {
              type: "category",
              data: this.yearData,
              axisPointer: {
                type: "shadow",
              },
              axisLine: {
                show: false,
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                show: true,
                textStyle: {
                  color: "#fff", //这里用参数代替了
                },
              },
            },
          ],
          yAxis: [
            {
              type: "value",
              name: "取水量（m³）",
              nameTextStyle: {
                color: "#fff",
                fontSize: 12,
              },
              axisLabel: {
                formatter: "{value}",
                show: true,
                textStyle: {
                  color: "#fff", //这里用参数代替了
                },
              },
            },
            {
              type: "value",
              name: "取水累积量（m³）",
              nameTextStyle: {
                color: "#fff",
                fontSize: 12,
              },
              axisLabel: {
                formatter: "{value}",
                show: true,
                textStyle: {
                  color: "#fff", //这里用参数代替了
                },
              },
            },
          ],
          series: this.overviewData,
        };
        option && myChart.setOption(option, true);
      },
      // 查询一项完整的规划项目
      async loadFullPlanProj(val) {
        await hydraulicApi.loadFullPlanProj({ id: val }).then((res) => {
          if (res.code == 0) {
            this.form = res.data;
          }
        });
      },
      handleClose() {
        this.$emit("handleClose");
      },
      peripheralAnalysis() {
        this.$emit("analysis");
      },
      minimize() {
        this.$emit("minimize");
      },
    },
  }
</script>

<style lang="scss">
.primary-dialog {
  .el-dialog {
    background-image: linear-gradient(
      180deg,
      rgba(8, 42, 53, 1) 2%,
      rgba(5, 14, 48, 1) 100%
    );
    .el-dialog__header {
      height: 54px;
      padding: 18px;
      border: 1px solid #00c1ff;
      // box-shadow: inset 0 4px 30px 0 #2351ef40;
      background: transparent;
      border-bottom: 0;
      margin: 2px 2px 0;
      .primary-dialog__title {
        height: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 40px;
        .primary-dialog__title--label {
          background: url(~assets/images/WaterDisastersImage/title-bg.png)
            no-repeat;
          // background-size: 100% 100%;
          font-size: 16px;
          color: #fff;
          letter-spacing: 0;
          height: 32px;
          width: 80%;
          // margin-left: 16px;
          padding-left: 56px;
          font-weight: bold;
          line-height: 32px;
        }
        .primary-dialog__title--minimize {
          cursor: pointer;
          margin-right: 48px;
        }
      }
      .el-dialog__headerbtn {
        .el-dialog__close {
          color: #00c1ff;
          font-weight: bold;
        }
      }
    }
    .el-dialog__body {
      min-height: 100px;
      padding: 0 18px;
      background: transparent;
      border: 1px solid #00c1ff;
      border-top: 0;
      border-bottom: 0;
      margin: 0 2px;
    }
    .health-code-sty {
      text-align: center;
      margin-bottom:50px;
    }
    .health-sty {
      color: #fff;
      display: flex;
      justify-content: space-between;
      padding:15px 0;
      >p {
        font-size: 16px;
        font-weight: 600;
        >span {
          color:#33FFFF;
          font-size: 18px;
          padding-left:20px;
        }
      }
    }
    .el-collapse-item__header {
      background-color: rgba(8,42,53,0.2);
      color: #fff;
      border-bottom: 0 solid #00C1FF;
    }
    .el-collapse-item__content {
      color: #fff;
      padding-bottom: 0;
      background: rgba(1, 9, 14, 0.9);
    }
    .el-collapse {
      border-top: 0 solid #EBEEF5;
      border-bottom: 0 solid #EBEEF5;
    }
    .under-frame-sty {
      border-bottom: 1px solid rgba($color: #00C1FF, $alpha: 0.2) ;
    }
    .btn-sty {
      background-image: linear-gradient(180deg, #36C0E4 20%, #0639FF 100%);
      border-radius: 4px;
      padding: 13px 15px;
      font-size:16px;
      display: inline-block;
      color:#fff;
      margin-right: 10px;
      margin-top: 25px;
    }
    .inspect-number-sty {
      padding-bottom: 15px;
      padding-right: 50px;
      font-size: 16px;
      display: flex;
      justify-content: space-between;
      > div {
        color: #fff;
        >p:nth-child(2) {
          margin-top: 10px;
          > span {
            margin-right:10px;
            font-size: 22px;
            color: #33FFFF;
          }
        }
      }
    }
    .boundary-sty {
      width: 2px;
      height: 460px;
      float: right;
      background-image: linear-gradient(0deg, rgba(12,61,54,0.02) 0%, rgba(28,255,245,0.50) 54%, rgba(12,61,54,0.50) 100%);
    }
    .riverway-sty {
      height: 50px;
      margin: 0 38px 20px 0;
      border: 1px solid #00C1FF;
      >span:nth-child(1) {
        color: #56FEFE;
        text-align: center;
        padding: 0 15px;
        height: 48px;
        line-height: 48px;
        display: inline-block;
        border-right: 1px solid #00C1FF;
        background-image: linear-gradient(180deg, rgba(8,42,53,0.90) 2%, rgba(5,14,48,0.90) 100%);
        box-shadow: inset 0 4px 30px 0 rgba(35,81,239,0.25);
      }
      >span:nth-child(2) {
        color: #fff;
        padding-left:15px;
      }
    }
    .el-dialog__footer {
      padding: 0 18px 24px;
      background: transparent;
      border: 1px solid #00c1ff;
      border-top: 0;
      margin: 0 2px 4px;
      .dialog-footer {
        .el-button {
          padding: 6px 16px;
          &.cancel {
            color: #1ef8f2;
            border: 1px solid #1ef8f2;
            background-color: transparent;
            border-radius: 4px;
          }
          &.confirm {
            color: #fff;
            border: 0;
            background-image: linear-gradient(
              180deg,
              #36c0e4 20%,
              #0639ff 100%
            );
          }
        }
      }
    }
    .tab-switch {
      padding: 5px 10px;
      margin-right: 15px;
      color: #56fefe;
      width: 150px;
      height: 30px;
      line-height: 30px;
      border: 1px solid #56fefe;
      box-shadow: inset 0 0 5px 0 #56FEFE;
    }
    .tab-switch-sty {
      padding: 5px 10px;
      margin-right: 15px;
      color: #fff;
      width: 160px;
      height: 30px;
      line-height: 30px;
      border: 1px solid #fff;
    }
    .el-table {
      .cell {
        height: 37px;
        line-height: 37px;
      }
      .el-checkbox__inner {
        border: 0.0625rem solid #00c1ff;
        background-color: #00c1ff;
        background: rgba($color: #000000, $alpha: 0);
      }
      .el-table__body tr.current-row > td {
        background-color: rgba(41, 147, 180, 0.3);
      }
      th,
      td {
        padding: 0;
        font-size: 14px;
        color: #fff;
      }
      th {
        padding: 3px 0;
        font-size: 14px;
        background-color: rgba(41, 147, 180, 0.1);
        font-weight: bold;
        color: #00c1ff;
      }
    }
    &:after {
      content: "";
      position: absolute;
      height: calc(100% + 4px);
      width: 100%;
      top: -2px;
      left: 0;
      background: linear-gradient(to bottom, #35b9e3, #5398f6) left top
          no-repeat,
        linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
        linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
        linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat,
        linear-gradient(to bottom, #5398f6, #35b9e3) left bottom no-repeat,
        linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
        linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
        linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
      /*设置大小*/
      background-size: 18px 2px, 2px 18px;
      z-index: -1;
    }
  }
  &.project-dialog-wrap {
    ::v-deep .el-dialog {
      margin-left: 20px;
      margin-top: 10vh !important;
    }
  }
}
.peripheral-analysis {
  display: inline-block;
  margin-left: 10px;
  cursor: pointer;
  background: url(~assets/images/peripheral_analysis.png) no-repeat;
  background-size: 100% 100%;
  width: 15px;
  height: 15px;
}
.minimize {
  color: #00c1ff;
  font-weight: bold;
  cursor: pointer;
}
</style>
