<template>
  <div class="project-side" ref="stationSide">
    <div class="statistics-chart" ref="statisticsChart">
      <!-- 统计项 -->
      <div class="chart-tabs" style="margin-bottom:10px;">
        <span :class="[typeTab == '条件筛选' ? 'active' : '']" @click="typeTabChange('条件筛选')">条件筛选</span>
        <span v-if="projectType == '取水口' || projectType == '河道' || projectType == '取水户'" :class="[typeTab == '区域统计' ? 'active' : '']" @click="typeTabChange('区域统计')">区域统计</span>
        <span v-if="projectType == '取水口'" :class="[typeTab == '运行状态' ? 'active' : '']" @click="typeTabChange('运行状态')">运行状态</span>
        <!-- <span :class="[typeTab == '设备掉线' ? 'active' : '']" @click="typeTabChange('设备掉线')">设备掉线</span> -->
        <!-- <span :class="[typeTab == '运行状态' ? 'active' : '']" @click="typeTabChange('运行状态')">运行状态</span> -->
        <!-- <span v-if="projectType == '取水口'" :class="[typeTab == '运行状态' ? 'active' : '']" @click="typeTabChange('运行状态')">运行状态</span> -->
        <span v-if="projectType == '河道'" :class="[typeTab == '等级' ? 'active' : '']" @click="typeTabChange('等级')">等级</span>
        <span v-if="projectType == '河道'" :class="[typeTab == '健康分析' ? 'active' : '']" @click="typeTabChange('健康分析')">健康分析</span>
        <span v-if="projectType == '取水户'" :class="[typeTab == '超许可统计' ? 'active' : '']" @click="typeTabChange('超许可统计')">超许可统计</span>
      </div>
      <!-- 条件查询及搜索 -->
      <el-row :gutter="20" class="split-row" v-if="typeTab == '条件筛选'">
        <!-- 行政区划 -->
         <el-col :span="24" style="margin-bottom: 10px;">
           <span style="color: #fff;">行政区划：</span>
          <el-select v-model="areaData" placeholder="行政区划" @change="planningName">
            <el-option v-for="item in areaoOptions" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-col>
        <!-- 报警 -->
        <el-col v-if="projectType == '取水户'" :span="24" style="margin-bottom: 10px;">
           <span style="color: #fff;">报警类型：</span>
          <el-select v-model="alarmData" placeholder="报警" @change="planningName">
            <el-option v-for="item in alarmList" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-col>
        <!-- 取水类型 -->
        <el-col v-if="projectType == '取水户'" :span="24" style="margin-bottom: 10px;">
           <span style="color: #fff;">取水类型：</span>
          <el-select v-model="waterIntakingData" placeholder="取水类型" @change="planningName">
            <el-option v-for="item in waterIntakingList" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-col>
        <!-- 河道等级 -->
        <el-col :span="24" v-if="projectType == '河道'" style="margin-bottom: 10px;">
          <span style="color: #fff;">河道等级：</span>
          <el-select v-model="level" placeholder="河道等级" @change="planningName">
            <el-option v-for="item in levelList" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-col>
        <!-- 健康分析 -->
        <el-col :span="24" v-if="projectType == '河道'" style="margin-bottom: 10px;">
          <span style="color: #fff;">健康分析：</span>
          <el-select v-model="healthState" placeholder="健康分析" @change="planningName">
            <el-option v-for="item in healthStateList" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-col>
        <!-- 运行状态 -->
        <el-col :span="24" v-if="projectType == '取水口'" style="margin-bottom: 10px;">
          <span style="color: #fff;">运行状态：</span>
          <el-select v-model="runningStatusData" placeholder="运行状态" @change="planningName">
            <el-option v-for="item in runningStatusList" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-col>
        <!-- 健康分布 -->
        <el-col :span="24" v-if="projectType == '河道'" style="margin-bottom: 10px;">
          <span style="color: #fff;">健康分布：</span>
          <el-checkbox v-model="checked" />
        </el-col>
      </el-row>
      
      <!-- 统计图表 -->
      <EchartCapsule
        v-if="typeTab == '区域统计' || typeTab == '运行状态' || typeTab == '等级' || typeTab == '健康分析' || typeTab == '超许可统计'"
        ref="chartCapsule" 
        :series-data="statisticData"
        :extra-option="extraOption"
        @clickItem="getCountryData"
      />
    </div>
    <!-- 统计列表 -->
    <div class="statistics-list" ref="statisticsList" :style="listStyle">
      <i class="split-line icon-split-line"></i>
      <!-- 关键字查询 -->
      <div class="search-area" ref="catalogTitle">
        <span class="amount-sty">总数<br/>{{inventoryList.length}}</span>
        <div class="left-area">
          <el-input @input="planningName" v-model="projName" placeholder="请输入关键字查询">
            <template slot="prepend">
              <el-button slot="append" icon="el-icon-search" />
            </template>
          </el-input>
        </div>
        <!-- 详表按钮 -->
        <div class="right-area" @click="lookDetail" v-if="projectType == '取水口' || projectType == '河道' || projectType == '取水户'">
          <i class="icon-liebiao-detail"></i>
        </div>
      </div>
      <!-- 简表 -->
      <div class="table-list" :style="tableListStyle">
        <screen-table :columnList="tableColumns" :dataList="inventoryList" :projectType="projectType" :loading="tbLoading" @rowClick="rowClick" :tableHeight="tableHeight"/>
      </div>
    </div>
    <!-- 详情弹框 -->
    <primary-dialog :mainTitle="projectType + '信息详表'" :visible="detailDialog" @handleClose="handleDetailClose" width="50%" height="600" :hasAnalysis="false">
      <div class="dialog-content scope-content">
        <RiversDetailDialog v-if="detailDialog" :projType="projectType" />
      </div>
    </primary-dialog>
  </div>
</template>

<script>
  import EchartCapsule from "../charts/echart_capsule/EchartCapsule";
  import RiversDetailDialog from "../dialog/RiversDetailDialog";
  import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
  import { ScreenTable, PrimaryDialog } from "@/components/";

  export default {
    name: "InformationOfLakes",
    data() {
      return {
        // 报警
        alarmData: null,
        // 报警数据
        alarmList: [
          {
            label: "全部",
            value: null
          },{
            label: "正常",
            value: "1"
          },{
            label: "接近许可",
            value: "2"
          },{
            label: "超许可",
            value: "3"
          }
        ],
        // 取水类型
        waterIntakingData: null,
        // 取水类型数据
        waterIntakingList: [
          {
            label: "全部",
            value: null
          },{
            label: "既取又用",
            value: "1"
          },{
            label: "只取不用",
            value: "2"
          },{
            label: "只用不取",
            value: "3"
          }
        ],
        // 河道等级数据
        levelList: [
          {
            label: "全部",
            value: null
          },{
            label: "省级",
            value: "2"
          },{
            label: "市级",
            value: "3"
          },{
            label: "县级",
            value: "4"
          },{
            label: "镇级",
            value: "5"
          },{
            label: "村级",
            value: "6"
          }
        ],
        // 河道等级
        level: null,
        // 健康等级数据
        healthStateList: [
          {
            label: "全部",
            value: null
          },{
            label: "绿码",
            value: "1"
          },{
            label: "黄码",
            value: "2"
          },{
            label: "红码",
            value: "3"
          }
        ],
        // 健康等级
        healthState: null,
        // 健康分布
        checked: true,
        //页码
        pageNum: "1",        
        //页数
        pageSize: "9999",      
        //行政区划
        areaoOptions: [],         
        //行政区划选中值
        areaData: '',             
        //运行状态
        runningStatusList:[
          {
            label: "全部",
            value: null
          },{
            label: "正在取水",
            value: "正在取水"
          },{
            label: "停止取水",
            value: "停止取水"
          },{
            label: "设备掉线",
            value: "设备掉线"
          }
        ],                          
        // 运行状态选中值
        runningStatusData: '',
        // 统计项切换
        typeTab: '条件筛选',
        //统计图数据
        statisticData: [],
        extraOption: {},
        // 详表弹窗
        detailDialog: false,
        // 模糊查询
        projName: null,
        //简表数据
        inventoryList: [],
        // 详表参数
        projectType: "",
        // 简表高度
        tableHeight: 0,
        // 简表表头
        tableColumns: [
          {
            prop: "projName",
            label: "名称"
          },
          {
            prop: "constructProperty",
            label: "更新时间"
          },
          {
            prop: "amountInvestTotal",
            label: "实时取水量（m³）"
          },{
            prop: "amountInvestTotal",
            label: "运行状态"
          }
        ],
        //简表样式
        tableListStyle: {},
        // 统计列表  //表格父容器样式
        listStyle: {}, 
        // 表格Loading
        tbLoading: false,
      }
    },
    computed: {
      mapObj() {
        return this.$store.state.hydraulicAmap.mapComponent;
      }
    },
    components: {
      ScreenTable,
      EchartCapsule,
      PrimaryDialog,
      RiversDetailDialog,
    },
    watch: {
      $route: {
        handler: function (val) {
          this.projectType = val.query.type ? val.query.type : "";
          this.typeTab = "条件筛选";
          this.handleWindowResize();    //计算简表高度，及其容器高度
          this.getStationListByType();  //根据type类型，调用对应的接口
        },
        // 深度观察监听
        deep: true,
      },
    },
    mounted() {
      this.projectType = this.$route.query.type ? this.$route.query.type : "";
      window.addEventListener("resize", this.handleWindowResize);
      this.$nextTick(()=>{
        this.getAreaList();           //行政区划
        this.handleWindowResize();    //计算简表高度，及其容器高度
        this.getStationListByType();  //根据type类型，调用对应的接口
      });
    },
    // 销毁组件
    beforeDestroy() {
      window.removeEventListener("resize", this.handleWindowResize);
    },
    methods: {
      // 行政区划
      async getAreaList(){
        let res = await hydraulicApi.getArea({ areaCode:'3303',level:3 });
        if(res.code == 0) {
          this.areaoOptions.unshift({
            label: "全部",
            value: ""
          });
          for(let item of res.data) {
            this.areaoOptions.push({
              label:item.adnm,
              value:item.adcd
            })
          }
        }
      },
      //查看详表
      lookDetail() {
        this.detailDialog = true;
      },
      //详表弹窗关闭
      handleDetailClose() {
        this.detailDialog = false;
      },
      // 计算高度
      handleWindowResize() {
        this.calcDomHeight();
        this.calcTableHeight();
      },
      //计算表格父容器高度
      calcDomHeight() {
        this.$nextTick(() => {
          const height = this.$refs.stationSide.offsetHeight - this.$refs.statisticsChart.offsetHeight + "px";
          this.listStyle = { height }
        })
      },
      //计算表格高度
      calcTableHeight() {
        this.$nextTick(() => {
          setTimeout(() => {
            this.tableHeight = this.$refs.statisticsList.offsetHeight - this.$refs.catalogTitle.offsetHeight - 50;
            this.tableListStyle = { height: this.tableHeight + "px" }
          }, 100)
        })
      },
      // 点击简表数据查找对应点位
      rowClick(data) {
        console.log(102, data);
      },
      //重置筛选条件
      resetParam() {
        // this.basinName = "";
        // this.areaCode = "";
        // this.searchVal = "";
        // this.mapObj.resetMap();
        // this.mapObj.clearAreaLayer();
        // this.mapObj.clearBasinLayer();
        // this.getProjectList(this.projectType);
      },
      //统计分析tab改变
      typeTabChange(type) {
        this.getStationListByType(type);
        this.typeTab = type;
      },
      // 详表点击事件
      async getCountryData(data) {
        if (this.typeTab == "行政区划") {
          this.areaCode = data.name;
          this.mapObj.showAreaLine(this.areaCode);
        } else if (this.typeTab == "流域") {
          this.basinName = data.name;
          const bsName = this.basinName + "流域";
          this.mapObj.basinCommand(bsName);
        }
      },
      // 查询项目清单
      planningName() {
        this.getStationListByType();
      },
      //根据类型获取测站列表 搜索
      getStationListByType(type) {
        switch (this.projectType) {
          case "河道":
            {
              this.tableColumns = [
                {
                  prop: "riverName",
                  label: "名称"
                },
                {
                  prop: "levelName",
                  label: "等级"
                },
                {
                  prop: "length",
                  label: "河道长度(km)",
                  sortable: true
                }
              ],
              this.riverStats(type);
              this.pageRivSegBaseInfo('河道');
            }
            break;
          case "河景":
            {
              this.tableColumns = [{
                prop: "name",
                label: "名称"
              }];
              this.riverUnionInfo("河道河景");
            }
            break;
          case "取水口":
            {
              this.tableColumns = [{
                prop: "wainName",
                label: "名称"
              },
              {
                prop: "tm",
                label: "更新时间"
              },
              {
                prop: "intake",
                label: "实时取水量（m³）"
              },{
                prop: "status",
                label: "运行状态"
              }];
              this.wainStats(type);
              this.wainMonitor("取水口");
            }
            break;
          case "雨水口":
            {
              this.tableColumns = [{
                prop: "expNo",
                label: "雨水口编码"
              },
              {
                prop: "road",
                label: "道路名称"
              }];
              this.rivInlet('1');
            }
            break;
          case "入河排污口":
            {
              this.tableColumns = [{
                prop: "expNo",
                label: "排污口编码"
              },
              {
                prop: "road",
                label: "道路名称"
              }];
              this.rivInlet('2');
            }
            break;
          case "取水户":
            {
              this.tableColumns = [{
                width: 90,
                prop: "wiuName",
                label: "名称",
              },
              {
                prop: "intake",
                label: "当年取水量（万m³）",
              },
              {
                prop: "intakePermit",
                label: "许可取水量（万m³）",
              }];
              this.wiuStats(type);
              this.waterDoorList();
            }
            break;
          case "地下机井":
            {
              this.tableColumns = [{
                width: 90,
                prop: "name",
                label: "名称",
              },
              {
                prop: "yearUseWater",
                label: "设计年取水量（万m³）",
                sortable:true
              },
              {
                prop: "realWaterUsage",
                label: "当前取水量（万m³）",
                sortable:true
              }];
              this.waterWellsList();
            }
            break;
          case "地下水位站":
            {
              this.tableColumns = [{
                prop: "stationName",
                label: "名称"
              },
              {
                prop: "chargePerson",
                label: "负责人"
              }];
              this.waterStationsList();
            }
            break;
        }
      },
      // 取水户
      async waterDoorList() {
        this.tbLoading = true;
        let opt = {
          areaCode: this.areaData,
          wiuName: this.projName,
          wiuType: this.waterIntakingData,
          intakeState: this.alarmData,
        }
        let res = await hydraulicApi.listWiu(opt);
        if(res.code == 0) {
          this.inventoryList = res.data;
        }
        this.tbLoading = false;
      },
      // 取水户统计
      async wiuStats(val) {
        let str = val == '区域统计' ? '1' : '2'
        let res = await hydraulicApi.wiuStats({type: str});
        if(res.code == 0) {
          this.statisticData = [];
          let xArr = [];
          res.data.forEach((item) => {
            const x = item.countKey || item.key;
            xArr.push(x);
            const y = item.countNum != undefined ? item.countNum : item.value;
            this.statisticData.unshift(y);
          });
          xArr = xArr.reverse();
          let obj = {
            yAxis: {
              data: xArr,
            },
          };
          this.extraOption = { ...obj };
        }
      },
      // 地下机井列表
      async waterWellsList() {
        this.tbLoading = true;
        const opt = {
          name: this.projName,
          areaCode: this.areaData,
        };
        await hydraulicApi.waterWellsList(opt).then((res) => {
          this.tbLoading = false;
          if (res.code == 0) {
            this.inventoryList = res.data;
          }
        }).catch((err)=>{
          this.$message.error(err);
          this.tbLoading = false;
        })
      },
      // 地下水位站列表
      async waterStationsList() {
        this.tbLoading = true;
        const opt = {
          stationName: this.projName,
          areaCode: this.areaData,
        };
        await hydraulicApi.waterStationsList(opt).then((res) => {
          this.tbLoading = false;
          if (res.code == 0) {
            this.inventoryList = res.data;
          }
        }).catch((err)=>{
          this.$message.error(err);
          this.tbLoading = false;
        })
      },
      // 河道图表统计
      async riverStats(val) {
        let str = val == '等级' ? '2' : val == '健康分析' ? '3' : '1'
        await hydraulicApi.riverStats({statsType: str}).then((res) => {
          if (res.code == 0) {
            this.statisticData = [];
            let xArr = [];
            res.data.forEach((item) => {
              const x = item.countKey || item.key;
              xArr.push(x);
              const y = item.countNum != undefined ? item.countNum : item.value;
              this.statisticData.unshift(y);
            });
            xArr = xArr.reverse();
            let obj = {
              yAxis: {
                data: xArr,
              },
            };
            this.extraOption = { ...obj };
          }
        })
      },
      // 河道详情统计接口
      async pageRivSegBaseInfo() {
        this.tbLoading = true;
        let obj = {
          riverName: this.projName,           //河道名称
          areaCode: this.areaData,            //行政区划
          healthState: this.healthState,      //健康等级
          level: this.level                   //河道等级
        };
        await hydraulicApi.pageRivSegBaseInfo(obj).then((res) => {
          if (res.code == 0) {
            this.inventoryList = res.data.list;
            this.tbLoading = false;
          }
        }).catch((err)=>{
          this.$message.error(err);
          this.tbLoading = false;
        })
      },

      // 雨水口，排污口详情接口
      async rivInlet(val) {
        this.tbLoading = true;
        let obj = {
          road: this.projName,
          areaCode: this.areaData,
          recordType: val
        };
        await hydraulicApi.rivInlet(obj).then((res) => {
          if (res.code == 0) {
            this.inventoryList = res.data;
            this.tbLoading = false;
          }
        }).catch((err)=>{
          this.$message.error(err);
          this.tbLoading = false;
        })
      },
      // 河景接口
      async riverUnionInfo(val) {
        this.tbLoading = true;
        let obj = {
          pageNum: this.pageNum,        //页码
          pageSize: this.pageSize,      //页数
          areaCode: this.areaData,      //行政区划    
          name: this.projName,          //名称 
          type: val,                    //类型：河道河景/雨水口/入河排污口

        };
        await hydraulicApi.riverUnionInfo(obj).then((res) => {
          if (res.code == 0) {
            this.inventoryList = res.data.list;
            this.tbLoading = false;
          }
        }).catch((err)=>{
          this.$message.error(err);
          this.tbLoading = false;
        })
      },
      // 取水口监测列表接口
      async wainMonitor() {
        this.tbLoading = true;
        let obj = {
          areaCode: this.areaData,          //行政区划    
          wainName: this.projName,          //名称 
          status: this.runningStatusData,   //运行状态
        };
        await hydraulicApi.wainMonitor(obj).then((res) => {
          if (res.code == 0) {
            this.inventoryList = res.data;
            this.tbLoading = false;
          }
        }).catch((err)=>{
          this.$message.error(err);
          this.tbLoading = false;
        })
      },
      // 取水口统计接口
      async wainStats(type) {
        let obj = {
          type: type == '运行状态' ? '2' : '1',   //运行状态
        };
        await hydraulicApi.wainStats(obj).then((res) => {
          if (res.code == 0) {
            this.statisticData = [];
            let xArr = [];
            res.data.forEach((item) => {
              const x = item.countKey || item.key;
              xArr.push(x);
              const y = item.countNum != undefined ? item.countNum : item.value;
              this.statisticData.unshift(y);
            });
            xArr = xArr.reverse();
            let obj = {
              yAxis: {
                data: xArr,
              },
            };
            this.extraOption = { ...obj };
            // this.$refs.chartCapsule.updateChartView();
          }
        })
      },



















      // 列表数据
      async getAllProjByPrId(val) {
        this.tbLoading = true;
        this.amountInvestTotal = 0;
        this.inventoryList = [];
        await hydraulicApi.getAllProjByPrId(val).then((res) => {
          if (res.code == 0) {
            res.data.forEach((item) => {
              this.inventoryList.push(item);
              this.amountInvestTotal =
                this.amountInvestTotal + item.amountInvestTotal;
            });
            this.tbLoading = false;
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "@/style/bigScreen.scss";
  .project-side {
    width: 100%;
    height: 100%;
    @include flexbox;
    @include flexflow(column, nowrap);
    .amount-sty {
      background: rgba(72,152,251,0.10);
      border: 0 solid #56FEFE;
      box-shadow: inset 0 0 5px 0 #56FEFE;
      padding: 10px;
      font-size: 16px;
      color: #56FEFE;
      text-align: center;
      line-height: 18px;
      font-weight: 400;
    }
    .statistics-chart {
      position: relative;
      flex: 1;
      .title-right {
        position: absolute;
        right: 0;
        .icon-btn-reset {
          cursor: pointer;
          height: 30px;
        }
      }
      .chart-tabs {
        margin-top: 10px;
        @include flexbox;
        @include flexAC;
        span {
          display: inline-block;
          padding: 4px 8px;
          width: 80px;
          color: #4898fb;
          border: 1px solid #4898fb;
          text-align: center;
          line-height: 16px;
          font-size: 12px;
          margin-right: 16px;
          cursor: pointer;
          &.active {
            color: #a6e9fb;
            background: rgba(72, 152, 251, 0.1);
            border: 1px solid #23e3fb;
          }
        }
      }
    }
    .statistics-list {
      @include flex(2);
      .table-list {
        height: 100%;
        width: 100%;
      }
    }
  }
</style>