<template>
  <div>
    <div class="scope-content">
      <div class="table-box">
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
          <div class="select-box">
            行政区划:
            <el-select v-model="areaData" @change="projectsDetailList">
              <el-option v-for="item in areaoOptions" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
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
        this.resetParam(); 
        this.DetailsInterface();      //详情接口调用
      });
    },
    methods: {
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
      border: 1px solid rgba(255, 255, 255, 0.6);
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
