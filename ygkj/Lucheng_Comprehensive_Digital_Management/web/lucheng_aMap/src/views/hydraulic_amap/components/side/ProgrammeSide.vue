<template>
  <div class="project-side" ref="stationSide">
    <!-- 条件查询及搜索 -->
    <div class="statistics-chart" ref="statisticsChart">
      <span class="active">条件选择</span>
      <el-row :gutter="20" class="split-row" v-if="projectType == '发展规划' || projectType == '综合规划' || projectType == '专业规划'|| projectType == '专项规划'">
        <el-col :span="24">
          <span style="color:#fff;">规划类型：</span>
          <el-select v-model="planningTypeVal" placeholder="规划类型" @change="programme">
            <el-option v-for="item in planningTypeList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-col>
        <el-col :span="24" style="margin: 15px 0;">
          <span style="color:#fff;">工程名称：</span>
          <el-select v-model="projectTypeVal" placeholder="工程名称" @change="planningName">
            <el-option v-for="item in projectTypeList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-col>
        <el-col :span="24">
          <span style="color:#fff;">建设性质：</span>
          <el-select v-model="constructionVal" placeholder="建设性质" @change="planningName">
            <el-option v-for="item in constructionList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="split-row" v-if="projectType == '谋划项目'">
        <el-col :span="24">
          <span style="color:#fff;">项目分类：</span>
          <el-select v-model="projectClassificationVal" placeholder="项目分类" @change="commonGetList(projectType)">
            <el-option v-for="item in projectClassificationList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="24" style="margin: 15px 0;">
          <span style="color:#fff;">行政区划：</span>
          <el-select v-model="countryAdcd" placeholder="行政区划" @change="commonGetList(projectType)">
            <el-option v-for="item in areaTab" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="24">
          <span style="color:#fff;">项目类型：</span>
          <el-select v-model="projectTypeValue" placeholder="项目类型" @change="commonGetList(projectType)">
            <el-option v-for="item in projectTypeData" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        
      </el-row>
      <el-row :gutter="20" class="split-row" v-if="projectType == '计划项目'">
        <el-col :span="24">
          <span style="color:#fff;">行政区划：</span>
          <el-select v-model="countryAdcd" placeholder="行政区划" @change="commonGetList(projectType)">
            <el-option v-for="item in areaTab" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="24" style="margin: 15px 0;">
          <span style="color:#fff;">项目类型：</span>
          <el-select clearable v-model="projectVal" placeholder="项目类型" @change="commonGetList(projectType)">
            <el-option-group v-for="group in countryAdcdList" :key="group.label" :label="group.label">
              <el-option v-for="item in group.options" :key="item.dictCode" :label="item.dictName" :value="item.dictCode" />
            </el-option-group>
          </el-select>
        </el-col>
        <el-col :span="24">
          <span style="color:#fff;">所属流域：</span>
          <el-select v-model="projectBasin" placeholder="所属流域" @change="commonGetList(projectType)">
            <el-option v-for="item in projectBasinList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="24" style="margin-top: 15px;">
          <span style="color:#fff;">项目状态：</span>
          <el-select v-model="projectState" placeholder="项目状态" @change="commonGetList(projectType)">
            <el-option v-for="item in projectStateList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
      </el-row>
      <i class="icon-split-line1" />
    </div>
    <!-- 统计列表 -->
    <div class="statistics-list" ref="statisticsList">
      <div class="catalog-title" ref="catalogTitle">
        <div class="search-area">
          <span class="amount-sty" style="width:60px;">总数<br/>{{inventoryList.length}}</span>
          <span class="amount-sty" style="margin-right: 20px;width:130px;">投资(万元)<br/>{{ amountInvestTotal }}</span>
          <div class="left-area">
            <el-input @input="commonGetList(projectType)" v-model="projName" placeholder="请输入关键字查询">
              <template slot="prepend">
                <el-button slot="append" icon="el-icon-search"></el-button>
              </template>
            </el-input>
          </div>
        </div>
      </div>
      <!-- 简表 -->
      <div class="table-list" :style="tableListStyle">
        <screen-table :columnList="tableColumns" :dataList="inventoryList" :loading="tbLoading" :tableHeight="tableHeight" />
      </div>
    </div>
  </div>
</template>

<script>
  import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
  import { ScreenTable } from "@/components/";

  export default {
    name: "ProgrammeSide",
    components: { ScreenTable },
    data() {
      return {
        planningTypeVal: "",    //规划类型
        planningTypeList: [],   //规划类型数据
        projectTypeVal: "",     //工程名称
        projectTypeList: [],    //工程名称数据
        constructionVal: "",    //建设性质
        constructionList: [],    //建设性质数据
        projectName: '',
        tableColumns: [{
          prop: "projName",
          label: "名称",
        },
        {
          prop: "constructProperty",
          label: "建设性质",
          width: '80px'
        },
        {
          prop: "amountInvestTotal",
          label: "总投资(万元)",
          width: '90px'
        }],
        tableListStyle: {}, //表格样式
        inventoryList: [],  //简表数据
        tableHeight: 0,     //简表高度
        projName: "",       //关键字查询
        amountInvestTotal: 0,//总投资金额
        projectType: "发展规划", //工程类型
        tbLoading: false,     //简表load
        areaTab: [],          //行政区划list
        countryAdcd: '',      //行政区划
        projectTypeValue: '', //谋划项目
        projectClassificationVal: '重大项目',
        projectClassificationList: [
          {
            label: "重大项目",
            value: "重大项目",
          },
          {
            label: "面上项目",
            value: "面上项目",
          }
        ],
        projectTypeData: [{
          label: "全部",
          value: "",
        },
        {
          label: "海塘安澜千亿",
          value: "海塘安澜千亿",
        },
        {
          label: "新建大中型水库工程",
          value: "新建大中型水库工程",
        },
        {
          label: "五大平原骨干排涝工程",
          value: "五大平原骨干排涝工程",
        },
        {
          label: "大中型病险水库水闸除险加固",
          value: "大中型病险水库水闸除险加固",
        },
        {
          label: "五大江河干堤加固工程",
          value: "五大江河干堤加固工程",
        },
        {
          label: "区域引调水工程",
          value: "区域引调水工程",
        },
        {
          label: "重点中小河流综合治理项目",
          value: "重点中小河流综合治理项目",
        },
        {
          label: "大型灌区加固改造",
          value: "大型灌区加固改造",
        },
        {
          label: "其他防洪排涝",
          value: "其他防洪排涝",
        }],
        projectState:'1',
        // 项目状态
        projectStateList: [
          {
            label: '规划',
            value: '1',
          },{
            label: '拟建',
            value: '2',
          },{
            label: '在建',
            value: '3',
          },{
            label: '完工',
            value: '4',
          }
        ],
        projectBasin: '',
        // 所属流域
        projectBasinList: [
          {
            label: '瓯江流域',
            value: '1',
          },{
            label: '飞云江流域',
            value: '2',
          },{
            label: '鳌江流域',
            value: '3',
          }
        ],
        projectVal: '',
        countryAdcdList: [],
      }
    },
    watch: {
      projectClassificationVal(val) {
        this.projectTypeValue = '';
        if (val == '面上项目') {
          this.projectTypeData = [
            {
              label: "全部",
              value: "",
            },
            {
              label: "中小流域综合治理",
              value: "中小流域综合治理",
            },
            {
              label: "小型水库除险加固",
              value: "小型水库除险加固",
            },
            {
              label: "圩区整治",
              value: "圩区整治",
            },
            {
              label: "美丽河湖建设",
              value: "美丽河湖建设",
            },
            {
              label: "水电生态治理",
              value: "水电生态治理",
            },
            {
              label: "农村水系治理",
              value: "农村水系治理",
            },
            {
              label: "重要山塘整治",
              value: "重要山塘整治",
            },
            {
              label: "新建小型水库",
              value: "新建小型水库",
            },{
              label: "水土流失综合治理",
              value: "水土流失综合治理",
            },
            {
              label: "其他项目",
              value: "其他项目",
            }
          ]
        } else {
          this.projectTypeData = [
            {
              label: "全部",
              value: "",
            },
            {
              label: "海塘安澜千亿",
              value: "海塘安澜千亿",
            },
            {
              label: "新建大中型水库工程",
              value: "新建大中型水库工程",
            },
            {
              label: "五大平原骨干排涝工程",
              value: "五大平原骨干排涝工程",
            },
            {
              label: "大中型病险水库水闸除险加固",
              value: "大中型病险水库水闸除险加固",
            },
            {
              label: "五大江河干堤加固工程",
              value: "五大江河干堤加固工程",
            },
            {
              label: "区域引调水工程",
              value: "区域引调水工程",
            },
            {
              label: "重点中小河流综合治理项目",
              value: "重点中小河流综合治理项目",
            },
            {
              label: "大型灌区加固改造",
              value: "大型灌区加固改造",
            },
            {
              label: "其他防洪排涝",
              value: "其他防洪排涝",
            }
          ]
        }
      },
      $route: {
        handler: function (val) {
          this.projectType = val.query.type ? val.query.type : "";
          this.commonGetList(this.projectType);
        },
        deep: true,     // 深度观察监听
      },
    },
    mounted() {
      this.projectType = this.$route.query.type ? this.$route.query.type : "";
      this.getAreaList();     //行政区划
      this.groupJson();       //数据字典
      this.commonGetList(this.projectType);
      window.addEventListener("resize", this.handleWindowResize);     // 计算表格高度尺寸
    },
    // 销毁函数
    beforeDestroy() {
      window.removeEventListener("resize", this.handleWindowResize);
    },
    methods: {
      // 公共调用接口
      commonGetList(val) {
        this.handleWindowResize();
        switch (val) {
          case "发展规划":
            this.getPlanTypeByClassify({ classify: this.projectType });     // 规划类型查询
            break;
          case "综合规划": 
            this.getPlanTypeByClassify({ classify: this.projectType });     // 规划类型查询
            break;
          case "专业规划":
            this.getPlanTypeByClassify({ classify: this.projectType });     // 规划类型查询
            break;
          case "专项规划": 
            this.getPlanTypeByClassify({ classify: this.projectType });     // 规划类型查询
            break;
          case "谋划项目":
            this.tableColumns = [{
              prop: "projectName",
              label: "项目名称",
            },
            {
              prop: "totalInvestment",
              label: "总投资(万元)",
            }];
            this.gequeryLibrarytList();       //谋划项目
            break;
          case "计划项目": 
            this.tableColumns = [{
              prop: "projectName",
              label: "项目名称",
            },
            {
              prop: "projectMoney",
              label: "总投资(万元)",
            }];
            this.projectLoadPage();           //计划项目
            break;
        }
      },
      //获取行政列表
      async getAreaList() {
        this.areaTab = [
          {
            label: "全部",
            value: "",
          },
        ];
        const opt = {
          level: "3",
          areaCode: "3303",
        };
        await hydraulicApi.getArea(opt).then((res) => {
          res.data.map((item) => {
            const opt = {
              label: item.adnm,
              value: item.adcd,
            };
            this.areaTab.push(opt);
          })
        })
      },
      // 谋划项目列表
      async gequeryLibrarytList() {
        this.tbLoading = true;
        let opt = {
          level: 0,
          projectName: this.projName,
          countryAdcd: this.countryAdcd,
          projectType: this.projectTypeValue,
          type: this.projectClassificationVal
        };
        this.amountInvestTotal = 0;
        await hydraulicApi.queryLibrary(opt).then((res) => {
          if (res.code == 0) {
            this.inventoryList = res.data.list;
            res.data.list.forEach((item) => {
              this.amountInvestTotal = this.amountInvestTotal + parseInt(item.totalInvestment);
            });
          }
          this.tbLoading = false;
        })
      },
      //计划项目获取列表
      async projectLoadPage() {
        this.tbLoading = true;
        let opt = {
          projectBasin: this.projectBasin,
          projectType: this.projectVal,
          projectName: this.projName,
          projectState: this.projectState,
          type: this.projectClassificationVal,
        };
        this.amountInvestTotal = 0;
        await hydraulicApi.projectLoadPage(opt).then((res) => {
          this.tbLoading = false;
          if (res.code == 0) {
            this.inventoryList = res.data.list;
            res.data.list.forEach((item) => {
              this.amountInvestTotal = this.amountInvestTotal + parseInt(item.projectMoney);
            });
          }
        });
      },
      // 数据字典
      async groupJson() {
        await hydraulicApi.groupJson({parentCode: 'projectType'}).then((res) => {
          if (res.code == 0) {
            this.countryAdcdList.push(
              {
                label: '建设类工程',
                options: res.data['建设类工程']
              },
              {
                label: '无项目类别',
                options: res.data['无项目类别']
              },
              {
                label: '管理类工程',
                options: res.data['管理类工程']
              },
              {
                label: '管理类项目',
                options: res.data['管理类项目']
              }
            )
          }
        })
      },
      // 规划类型数据接口
      async getPlanTypeByClassify(val) {
        this.planningTypeList = [];
        await hydraulicApi.getPlanTypeByClassify(val).then((res) => {
          if (res.code == 0) {
            this.planningTypeVal = res.data[1].ptName;
            this.pagePlanRec({ ptId: res.data[1].id });
            res.data.forEach((item) => {
              this.planningTypeList.push({
                name: item.ptName,
                id: item.id
              })
            })
          }
        })
      },
      //工程名称列表
      async pagePlanRec(val) {
        this.projectTypeList = [];
        await hydraulicApi.pagePlanRec(val).then((res) => {
          if (res.code == 0) {
            this.projectTypeVal = res.data.list[0].id;
            this.planningName();
            res.data.list.forEach((item) => {
              this.projectTypeList.push({
                name: item.planName,
                id: item.id
              })
            })
          }
        })
      },
      // 查询规划名称
      programme(val) {
        let str = this.planningTypeList.find((item) => item.id == val);
        this.projectName = str.name;
        this.pagePlanRec({ ptId: val });
      },
      // 查询项目清单
      planningName() {
        this.getAllProjByPrId({ prId: this.projectTypeVal, projName: this.projName });
      },
      // 项目清单
      async getAllProjByPrId(val) {
        this.tbLoading = true;
        this.amountInvestTotal = 0;
        await hydraulicApi.getAllProjByPrId(val).then((res) => {
          if (res.code == 0) {
            this.inventoryList = res.data;
            res.data.forEach((item) => {
              this.amountInvestTotal = this.amountInvestTotal + item.amountInvestTotal;
            });
            this.tbLoading = false;
            
          }
        })
      },
      // 计算表格高度尺寸
      handleWindowResize() {
        this.$nextTick(() => {
          setTimeout(() => {
            this.tableHeight =
              this.$refs.stationSide.offsetHeight -
              this.$refs.statisticsChart.offsetHeight -
              this.$refs.catalogTitle.offsetHeight -
              16;
            this.tableListStyle = {
              height: this.tableHeight + 'px'
            };
          }, 100)
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
    .statistics-chart {
      position: relative;
      .active {
        color: #56fefe;
        background: rgba(72, 152, 251, 0.1);
        border: 0.0625rem solid #56fefe;
        box-shadow: inset 0 0 0.3125rem 0 #56fefe;
        box-sizing: border-box;
        display: inline-block;
        padding: 0.25rem 0.5rem;
        width: 5rem;
        height: 2rem;
        text-align: center;
        line-height: 1.25rem;
        font-size: 0.875rem;
        margin-right: 1rem;
        cursor: pointer;
        margin-bottom:15px;
      }
      .icon-split-line1 {
        background-image: url(/img/sprite.3056d035.png);
        display: inline-block;
        width: 22.875rem;
        height: 0.25rem;
        margin-top: 20px;
        background-position: 17.20779221% 60.20833333%;
        background-size: 268.30601093% 32100%;
      }
    }
    .statistics-list {
      @include flex(2);
      .table-list {
        height: 100%;
        width: 100%;
      }
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
    }
  }
</style>