<template>
  <div>
    <div class="scope-content">
      <div class="table-box">
        <!-- 河道统计 -->
        <div class="collect-sty" v-if="projType == '入河排污口'">
          <el-row>
            <el-col :span="5" style="display: flex;justify-content: space-between;margin-right:15px;">
              <i class="icon-above-icon" />
              <div>
                <p style="margin-bottom:10px;">入河排污口总数</p>
                <span style="font-size: 24px;color: #56FEFE;">{{collectList.totalNum}}</span> 个
              </div>
            </el-col>
            <el-col :span="5" style="display: flex;justify-content: space-between;margin-right:15px;">
              <i class="icon-above-icon" />
              <div>
                <p style="margin-bottom:10px;">规模以上数量</p>
                <span style="font-size: 24px;color: #56FEFE;">{{collectList.gtGm}}</span> 个
              </div>
            </el-col>
            <el-col :span="5" style="display: flex;justify-content: space-between;margin-right:15px;">
              <i class="icon-under-icon" />
              <div>
                <p style="margin-bottom:10px;">规模以下数量</p>
                <span style="font-size: 24px;color: #56FEFE;">{{collectList.ltGm}}</span> 个
              </div>
            </el-col>
          </el-row>
        </div>
        <!-- 河道统计 -->
        <div class="collect-sty" v-if="projType == '河道'">
          <el-row>
            <el-col :span="4" style="display: flex;justify-content: space-between;margin-right:15px;">
              <i class="icon-amount"></i>
              <div>
                <p style="margin-bottom:10px;">河道总数</p>
                <span style="font-size: 24px;color: #56FEFE;">{{collectList.rivGreenNum}}</span> 个
              </div>
            </el-col>
            <el-col :span="4" style="display: flex;justify-content: space-between;margin-right:15px;">
              <i class="icon-green-ma"></i>
              <div>
                <p style="margin-bottom:10px;">绿码</p>
                <span style="font-size: 24px;color: #56FEFE;">{{collectList.rivSegNum}}</span> 个
              </div>
            </el-col>
            <el-col :span="4" style="display: flex;justify-content: space-between;margin-right:15px;">
              <i class="icon-huang-ma"></i>
              <div>
                <p style="margin-bottom:10px;">黄码</p>
                <span style="font-size: 24px;color: #56FEFE;">{{collectList.rivYellowNum}}</span> 个
              </div>
            </el-col>
            <el-col :span="4" style="display: flex;justify-content: space-between;">
              <i class="icon-hong-ma"></i>
              <div>
                <p style="margin-bottom:10px;">红码</p>
                <span style="font-size: 24px;color: #56FEFE;">{{collectList.rivRedNum}}</span> 个
              </div>
            </el-col>
          </el-row>
        </div>
        <!-- 筛选项 -->
        <div class="select-group">
          <!-- 清四乱条件选择 -->
          <div class="select-box" v-if="projType == '清四乱'">
            <!-- 四乱类型： -->
            <el-select v-model="fourType" placeholder="四乱类型" @change="getList" clearable>
              <el-option v-for="item in fourOptions" :key="item.label" :label="item.label" :value="item.label"/>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '清四乱'">
            <!-- 严重程度： -->
            <el-select v-model="severity" placeholder="严重程度" @change="getList" clearable>
              <el-option v-for="item in orderSeverity" :key="item.label" :label="item.label" :value="item.label"/>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '清四乱'">
            <!-- 问题状态： -->
            <el-select v-model="status" placeholder="问题状态" @change="getList" clearable>
              <el-option v-for="item in checkOptions" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '清四乱'">
            <!-- 问题来源： -->
            <el-select v-model="problemSource" placeholder="问题来源" @change="getList" clearable>
              <el-option v-for="item in problemSourceList" :key="item.label" :label="item.label" :value="item.label"/>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '清四乱'">
            <!-- 关键字查询： -->
            <el-input v-model="problemDescription" placeholder="请输入关键字查询"  @change="getList" />
          </div>
          <div class="select-box" v-if="projType != '清四乱'">
            行政区划:
            <el-select v-model="areaData" @change="projectsDetailList">
              <el-option v-for="item in areaoOptions" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </div>
          <!-- 入河排污口筛选项 -->
          <div class="select-box" v-if="projType == '入河排污口'">
            排污规模:
            <el-select v-model="scaleScreening" placeholder="规模筛选" @change="projectsDetailList">
              <el-option v-for="item in scaleScreeningList" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '入河排污口'">
            排放方式:
            <el-select v-model="emissionsWay" placeholder="排放方式" @change="projectsDetailList">
              <el-option v-for="item in emissionsWayList" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '入河排污口'">
            关键字:
            <el-input style="width:100px;" v-model="projName" placeholder="请输入关键字查询" @change="projectsDetailList" />
          </div>
          
          <div class="select-box" v-if="projType == '取水户'">
            报警类型:
            <el-select v-model="alarmData" @change="projectsDetailList">
              <el-option v-for="item in alarmList" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '取水户'">
            取水类型:
            <el-select v-model="waterIntakingData" @change="projectsDetailList">
              <el-option v-for="item in waterIntakingList" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '河道'">
            河道等级:
            <el-select v-model="level" placeholder="河道等级" @change="projectsDetailList">
              <el-option v-for="item in levelList" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '河道'">
            健康分析:
            <el-select v-model="healthState" placeholder="健康分析" @change="projectsDetailList">
              <el-option v-for="item in healthStateList" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '河道'">
            河道名称:
            <el-input style="width:100px;" v-model="projName" placeholder="请输入" @change="projectsDetailList" />
          </div>
          <!-- 运行状态 -->
          <div class="select-box" v-if="projType == '河道'">
            运行状态:
            <el-select style="width:105px;" v-model="runningStatusData" placeholder="请选择" @change="projectsDetailList">
              <el-option v-for="item in runningStatusList" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </div>
          <div class="reset" @click="resetParam">重置</div>
        </div>
        <!-- 详情列表 -->
        <primary-map-table :columnList="columnList" :projType="projType" :tableIndex="tableIndex" :dataList="dataList" :tableLoading="tableLoading"/>
      </div>
    </div>
  </div>
</template>

<script>
  import { PrimaryMapTable } from "@/components";
  import { hydraulicApi } from "@/api/hydraulic_amap/index.js";

  export default {
    name: "RiversDetailDialog",
    props: {
      projType: {
        type: String,
        default: "水库",
      },
    },
    data() {
      return {
        fourType: '',
        severity: '',
        status: '',
        problemSource: '',
        problemDescription: '',
        // 规模筛选
        scaleScreening: null,
        scaleScreeningList:[{
          label: "全部",
          value: null
        },{
          label: "规模以上",
          value: "规模以上"
        },{
          label: "规模以下",
          value: "规模以下"
        }],
        // 排放方式
        emissionsWay: null,
        emissionsWayList:[{
          label: "全部",
          value: null
        },{
          label: "间歇式",
          value: "间歇式"
        },{
          label: "连续式",
          value: "连续式"
        }],
        fourOptions: [
          {
            label: "全部"
          },
          {
            label: "乱占"
          },
          {
            label: "乱采"
          },
          {
            label: "乱堆"
          },
          {
            label: "乱建"
          },
          {
            label: "其他"
          }
        ],
        problemSourceList: [
          {
            label: "全部"
          },
          {
            label: "市级督查"
          },
          {
            label: "县级自查"
          },
        ],
        orderSeverity: [
          {
            label: "全部"
          },
          {
            label: "轻微"
          },
          {
            label: "中度"
          },
          {
            label: "重大"
          },
        ],
        checkOptions: [
          {
            label: "未处理",
            value: 0,
          },
          {
            label: "已处理",
            value: 1,
          },
          {
            label: "逾期未处理",
            value: 2,
          },
          {
            label: "驳回",
            value: 3,
          }
        ],
        // 河道汇总
        collectList: {},
        // 河道名称
        projName: null,
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
        // 详表高度  
        tableIndex: '-300px',
        //行政区划选中值
        areaData: null,  
        // 行政区划
        areaoOptions: [],
        // 运行状态选中值
        runningStatusData: null, 
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
        // 列表表头
        columnList: [
          {
            prop: "wainName",
            label: "站点名称",
          },
          {
            prop: "wiuName",
            label: "取水户名称",
          },
          {
            prop: "areaName",
            label: "行政区划",
          },
          {
            prop: "tm",
            label: "更新时间",
          },
          {
            prop: "intake",
            label: "实时取水量（m³）",
          },
          {
            prop: "status",
            label: "运行状态",
          }
        ],
        // 详情列表数据
        dataList: [],
        // 表格loading
        tableLoading: false, 
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
      }
    },
    components: {
      PrimaryMapTable,
    },
    mounted() {
      this.getAdcdList();
      this.$nextTick(() => {
        if (this.projType == '清四乱') {
          this.getList();
          // 列表表头
          this.columnList = [
            {
              prop: "riverName",
              label: "问题河道",
            },
            {
              prop: "address",
              label: "详细地址",
            },
            {
              prop: "status",
              label: "问题状态",
            },
            {
              prop: "problemSource",
              label: "问题来源",
            },
            {
              prop: "fourType",
              label: "四乱类型",
            },
            {
              prop: "severity",
              label: "严重程度",
            },
            {
              prop: "problemDescription",
              label: "问题描述",
            },
            {
              prop: "createTime",
              label: "上传时间",
            },
            {
              prop: "rectificationStatus",
              label: "整改范围",
            }
          ]
        }
        this.resetParam(); 
        this.DetailsInterface();      //详情接口调用
      });
    },
    methods: {
      // 获取列表
      async getList() {
        this.dataList = [];
        const opt = {
          fourType: this.fourType,
          severity: this.severity,
          status: this.status,
          problemSource: this.problemSource,
          problemDescription: this.problemDescription
        };
        await hydraulicApi.WsPatrolProblem(opt).then((res) => {
          this.tableLoading = false;
          if (res.code == 0) {
            let list = res.data.list;
            list.map((val) => {
              if (val.status == '0') {
                val.status = '未处理';
              }else if(val.status == '1') {
                val.status = '已处理';
              }else if(val.status == '2') {
                val.status = '逾期未处理';
              }else if(val.status == '3') {
                val.status = '驳回';
              }
              if (val.rectificationStatus == '1') {
                val.rectificationStatus = '10天内已完成整改';
              }else if(val.rectificationStatus == '2') {
                val.rectificationStatus = '本季度已完成整改';
              }else if(val.rectificationStatus == '3') {
                val.rectificationStatus = '剩余10天';
              }else if(val.rectificationStatus == '4') {
                val.rectificationStatus = '剩余3天';
              }else if(val.rectificationStatus == '5') {
                val.rectificationStatus = '已逾期';
              }
              return val;
            });
            this.dataList = list;
          }
        })
      },
      //  行政区划
      async getAdcdList() {
        let res = await hydraulicApi.getAdcdList({areaCode: "3303",level: 3});
        if (res.code == 0) {
          this.areaoOptions = [{
            value: null,
            label: "全部"
          }];
          res.data.forEach((item) => {
            this.areaoOptions.push({
              value: item.adcd,
              label: item.adnm
            });
          });
        }
      },
      // 取水户
      async waterDoorList() {
        this.dataList = [];
        this.tableLoading = true;
        let opt = {
          areaCode: this.areaData,
          wiuType: this.waterIntakingData,
          intakeState: this.alarmData,
        }
        let res = await hydraulicApi.listWiu(opt);
        if(res.code == 0) {
          this.dataList = res.data;
        }
        this.tableLoading = false;
      },
      // 详情接口调用
      DetailsInterface() {
        switch (this.projType) {
          case "取水口":
            {
              this.wainMonitor();
            }
            break;
          case "入河排污口":
            {
              this.columnList = [
                {
                  prop: "outletName",
                  label: "入河排污口名称",
                },
                {
                  prop: "wrzGdthNm",
                  label: "所在水资源分区",
                },
                {
                  prop: "riverLakeRsvr",
                  label: "河湖名称",
                },
                {
                  prop: "importantWfzCode",
                  label: "水功能区编码",
                },
                {
                  prop: "wfzGdfiNm",
                  label: "水功能一级区",
                },
                {
                  prop: "outletType",
                  label: "入河排污口规模",
                },
                {
                  prop: "city",
                  label: "所在地",
                },
                {
                  prop: "intoRiverType",
                  label: "污水入河方式",
                },
                {
                  prop: "outletGrad",
                  label: "排放方式",
                }
              ];
              this.stats4PageList();
              this.riverOutlet();
            }
            break;
          case "取水户":
            {
              this.columnList = [
                {
                  prop: "wiuName",
                  label: "名称",
                },
                {
                  prop: "areaName",
                  label: "行政区划",
                },
                {
                  prop: "wiuType",
                  label: "取水类型",
                },
                {
                  prop: "intake",
                  label: "当年取水量（万m³）",
                },
                {
                  prop: "intakePermit",
                  label: "许可取水量（万m³）",
                },
                {
                  prop: "legRepr",
                  label: "法人代表",
                },
                {
                  prop: "legPhone",
                  label: "联系电话",
                }
              ];
              this.waterDoorList();
            }
            break;
          case "河道":
            {
              this.columnList = [
                {
                  prop: "riverName",
                  label: "河道名称",
                },
                {
                  prop: "areaName",
                  label: "行政区划",
                },
                {
                  prop: "levelName",
                  label: "河道等级",
                },
                {
                  prop: "startName",
                  label: "起点名称",
                },
                {
                  prop: "endName",
                  label: "终点名称",
                },
                {
                  prop: "length",
                  label: "河道长度(km)",
                  sortable: true
                }
              ];
              this.pageRivSegStats();
              this.pageRivSegBaseInfo();
            }
            break;
        }
      },
      // 入河排污口统计数据
      async stats4PageList() {
        let obj = {
          outletName: this.projName,
          areaCode: this.areaData,
          outletType: this.emissionsWay,
          outletGrad: this.scaleScreening
        };
        await hydraulicApi.stats4PageList(obj).then((res) => {
          if (res.code == 0) {
            this.collectList = res.data;
          }
        })
      },
      // 入河排污口详情接口
      async riverOutlet() {
        let obj = {
          outletName: this.projName,
          areaCode: this.areaData,
          outletType: this.emissionsWay,
          outletGrad: this.scaleScreening
        };
        await hydraulicApi.riverOutlet(obj).then((res) => {
          if (res.code == 0) {
            this.dataList = res.data.list;
          }
        })
      },
      // 河道统计
      async pageRivSegStats() {
        let obj = {
          riverName: this.projName,           //河道名称
          areaCode: this.areaData,            //行政区划
          healthState: this.healthState,      //健康等级
          level: this.level                   //河道等级
        };
        await hydraulicApi.pageRivSegStats(obj).then((res) => {
          if (res.code == 0) {
            this.collectList = res.data;
          }
        })
      },
      // 河道详情统计接口
      async pageRivSegBaseInfo() {
        this.tableLoading = true;
        let obj = {
          riverName: this.projName,           //河道名称
          areaCode: this.areaData,            //行政区划
          healthState: this.healthState,      //健康等级
          level: this.level                   //河道等级
        };
        await hydraulicApi.pageRivSegBaseInfo(obj).then((res) => {
          if (res.code == 0) {
            this.dataList = res.data.list;
            this.tableLoading = false;
          }
        }).catch((err)=>{
          this.$message.error(err);
          this.tableLoading = false;
        })
      },
      // 条件筛选列表
      projectsDetailList() {
        this.DetailsInterface();
      },
      // 取水口监测列表接口
      async wainMonitor() {
        this.tableLoading = true;
        let obj = {
          areaCode: this.areaData,          //行政区划    
          status: this.runningStatusData,   //运行状态
        };
        await hydraulicApi.wainMonitor(obj).then((res) => {
          if (res.code == 0) {
            this.dataList = res.data;
          }
        })
        this.tableLoading = false;
      },
      //重置筛选条件
      resetParam() {
        this.projName = null;
        this.areaData = null;
        this.healthState = null;
        this.level = null;
        this.alarmData = null;
        this.waterIntakingData = null;
        this.runningStatusData = null;
        this.fourType = null;
        this.severity = null;
        this.status = null;
        this.problemSource = null;
        this.problemDescription = null;
        this.DetailsInterface();
      }
    }
  }
</script>

<style lang="scss" scoped>
  .scope-content {
    display: flex;
    height: 620px;
    padding: 0 22px;
    ::v-deep .el-input__inner {
      width: 100px;
      height: 32px;
      // border: 1px solid rgba(255, 255, 255, 0.6);
      border-radius: 4px;
      background: transparent;
    }
    ::v-deep .el-input__icon {
      line-height: 24px;
    }
    ::v-deep .el-table {
      tr {
        background-color: transparent;
      }
    }
    .table-box {
      color: #fff;
      width: 100%;
      height: 480px;
      .collect-sty {
        padding-right: 20px;
        padding-bottom: 15px;
        border-bottom:1px solid rgba($color: #00C1FF, $alpha: 0.2);
      }
      .select-group {
        padding: 16px 0 24px;
        display: flex;
        .select-box {
          width: 170px;
          margin-right: 11px;
        }
        .reset {
          background-color: rgba(0, 0, 0, 0);
          border: 1px solid #56fefe;
          border-radius: 4px;
          font-size: 16px;
          color: #56fefe;
          width: 64px;
          height: 34px;
          line-height: 34px;
          text-align: center;
        }
      }
    }
    ::v-deep .el-loading-mask {
      background: transparent;
    }
  }
</style>
