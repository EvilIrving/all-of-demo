<template>
  <div>
    <!-- 水库统计 -->
    <div class="statistics-sty" v-if="projType == '水库'">
      <!-- 水库总数统计 -->
      <div>
        <p><i class="icon-shui-ku-zong-shu"></i></p>
        <div>
          <p>水库总数</p>
          <p>
            <span>{{ dataCount.total }} </span> 个
          </p>
        </div>
      </div>
      <!-- 病险数量统计 -->
      <div>
        <p><i class="icon-shui-ku-bing-xian"></i></p>
        <div>
          <p>病险数量</p>
          <p>
            <span style="color: #ff7144">{{ dataCount.dilapProj }} </span> 个
          </p>
        </div>
      </div>
    </div>
    <!-- 山塘统计 -->
    <div class="statistics-sty" v-if="projType == '山塘'">
      <!-- 山塘总数统计 -->
      <div>
        <p><i class="icon-shan-tang-zong-shu"></i></p>
        <div>
          <p>山塘总数</p>
          <p>
            <span>{{ dataCount.total }} </span> 个
          </p>
        </div>
      </div>
      <!-- 病险数量统计 -->
      <div>
        <p><i class="icon-shan-tang-bing-xian"></i></p>
        <div>
          <p>病险数量</p>
          <p>
            <span>{{ dataCount.dilapProj }} </span> 个
          </p>
        </div>
      </div>
    </div>
    <!-- 堤防统计 -->
    <div class="statistics-sty" v-if="projType == '堤防'">
      <!-- 堤防总数统计 -->
      <div>
        <p><i class="icon-di-fang-zong-shu"></i></p>
        <div>
          <p>堤防总数</p>
          <p>
            <span>{{ dataCount.total }} </span> 个
          </p>
        </div>
      </div>
      <!-- 堤防总长度统计 -->
      <div>
        <p><i class="icon-di-fang-zong-chang"></i></p>
        <div>
          <p>堤防总长度</p>
          <p>
            <span>{{ dataCount.dikeLen }} </span> km
          </p>
        </div>
      </div>
    </div>
    <!-- 海塘统计 -->
    <div class="statistics-sty" v-if="projType == '海塘'">
      <!-- 海塘总数统计 -->
      <div>
        <p><i class="icon-hai-tang-zong-shu"></i></p>
        <div>
          <p>海塘总数</p>
          <p>
            <span>{{ dataCount.total }} </span> 个
          </p>
        </div>
      </div>
      <!-- 病险数量统计 -->
      <div>
        <p><i class="icon-hai-tang-bing-xian"></i></p>
        <div>
          <p>病险数量</p>
          <p>
            <span>{{ dataCount.dilapProj }} </span> 个
          </p>
        </div>
      </div>
      <!-- 海塘总长度统计 -->
      <div>
        <p><i class="icon-hai-tang-zong-chang"></i></p>
        <div>
          <p>海塘总长度</p>
          <p>
            <span>{{ dataCount.seawallLen }} </span> km
          </p>
        </div>
      </div>
    </div>
    <!-- 水闸统计 -->
    <div class="statistics-sty" v-if="projType == '水闸'">
      <!-- 水闸总数统计 -->
      <div>
        <p><i class="icon-shui-zha-zong-shu"></i></p>
        <div>
          <p>水闸总数</p>
          <p>
            <span>{{ dataCount.total }} </span> 个
          </p>
        </div>
      </div>
      <!-- 病险数量统计 -->
      <div>
        <p><i class="icon-shui-zha-bing-xian"></i></p>
        <div>
          <p>病险数量</p>
          <p>
            <span>{{ dataCount.dilapProj }} </span> 个
          </p>
        </div>
      </div>
    </div>
    <!-- 泵站统计 -->
    <div class="statistics-sty" v-if="projType == '泵站'">
      <!-- 泵站总数统计 -->
      <div>
        <p><i class="icon-bang-zhan-zong-shu"></i></p>
        <div>
          <p>泵站总数</p>
          <p>
            <span>{{ dataCount.total }} </span> 个
          </p>
        </div>
      </div>
      <!-- 总装机流量统计 -->
      <div>
        <p><i class="icon-bang-zhan-zong-zhaung-ji"></i></p>
        <div>
          <p>总装机流量</p>
          <p>
            <span>{{ dataCount.insFlow }} </span> m³/s
          </p>
        </div>
      </div>
    </div>
    <!-- 闸站统计 -->
    <div class="statistics-sty" v-if="projType == '闸站'">
      <!-- 闸站总数统计 -->
      <div>
        <p><i class="icon-zha-zhan-zong-shu"></i></p>
        <div>
          <p>闸站总数</p>
          <p>
            <span>{{ dataCount.total }} </span> 个
          </p>
        </div>
      </div>
    </div>
    <!-- 灌区统计 -->
    <div class="statistics-sty" v-if="projType == '灌区'">
      <!-- 灌区总数统计 -->
      <div>
        <p><i class="icon-guan-qu-zong-shu"></i></p>
        <div>
          <p>灌区总数</p>
          <p>
            <span>{{ dataCount.total }} </span> 个
          </p>
        </div>
      </div>
      <!-- 有效灌溉总面积统计 -->
      <div>
        <p><i class="icon-you-xiao-zong-mian-ji"></i></p>
        <div>
          <p>有效灌溉总面积</p>
          <p>
            <span style="color: #19af47">{{ dataCount.effIrrArea }} </span> km²
          </p>
        </div>
      </div>
    </div>
    <!-- 水电站统计 -->
    <div class="statistics-sty" v-if="projType == '水电站'">
      <!-- 水电站总数统计 -->
      <div>
        <p><i class="icon-shui-dian-zhan-zong-shu"></i></p>
        <div>
          <p>水电站总数</p>
          <p>
            <span>{{ dataCount.total }} </span> 个
          </p>
        </div>
      </div>
      <!-- 总装机容量统计 -->
      <div>
        <p><i class="icon-shui-dian-zhan-zong-zhuang-ji"></i></p>
        <div>
          <p>总装机容量</p>
          <p>
            <span>{{ dataCount.totInsCap }} </span> KW
          </p>
        </div>
      </div>
    </div>
    <!-- 防汛仓库统计 -->
    <div class="statistics-sty" v-if="projType == '防汛仓库'">
      <div>
        <p><i class="icon-warehouse-count"></i></p>
        <div>
          <p>仓库总数</p>
          <p>
            <span>{{ dataCount.total }} </span> 个
          </p>
        </div>
      </div>
      <div>
        <p><i class="icon-warehouse-count"></i></p>
        <div>
          <p>仓库总面积</p>
          <p>
            <span>{{ dataCount.totalArea }} </span> m²
          </p>
        </div>
      </div>
      <div>
        <p><i class="icon-warehouse-count"></i></p>
        <div>
          <p>物资总价值</p>
          <p>
            <span>{{ dataCount.materialPrice.toFixed(2) }} </span>万元
          </p>
        </div>
      </div>
    </div>
    <!-- 救援队伍统计 -->
    <div class="statistics-sty" v-if="projType == '救援队伍'">
      <div>
        <p><i class="icon-ranks-count"></i></p>
        <div>
          <p>队伍总数</p>
          <p>
            <span>{{ dataCount.total }} </span> 个
          </p>
        </div>
      </div>
      <div>
        <p><i class="icon-ranks-count"></i></p>
        <div>
          <p>队伍总人数</p>
          <p>
            <span>{{ dataCount.existingNumber }} </span> 人
          </p>
        </div>
      </div>
    </div>
    <div class="statistics-sty" v-if="projType == '避灾点'">
      <div>
        <p><i class="icon-ranks-count"></i></p>
        <div>
          <p>避灾点总数</p>
          <p>
            <span>{{ dataCount.total }} </span> 个
          </p>
        </div>
      </div>
      <div>
        <p><i class="icon-ranks-count"></i></p>
        <div>
          <p>避灾点总人数</p>
          <p>
            <span>{{ dataCount.countPerson }} </span> 人
          </p>
        </div>
      </div>
    </div>
    <div class="scope-content">
      <div class="table-box">
        <div class="select-group">
          <div class="select-box" v-if="
              projType == '水库' ||
              projType == '水闸' ||
              projType == '泵站' ||
              projType == '闸站' ||
              projType == '水电站'
            ">
            工程规模:
            <el-select v-model="engScalVal" class="primary-select" @change="projectsDetailList">
              <el-option v-for="item in engScalOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '山塘'">
            山塘类型:
            <el-select v-model="mnpdCateVal" class="primary-select" @change="projectsDetailList">
              <el-option v-for="item in mnpdCateOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '堤防'">
            堤防级别:
            <el-select v-model="gradeVal" class="primary-select" @change="projectsDetailList">
              <el-option v-for="item in gradeOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '海塘'">
            防潮标准:
            <el-select v-model="designVal" class="primary-select" @change="projectsDetailList">
              <el-option v-for="item in designOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="select-box" v-if="projType == '海塘'">
            海塘级别:
            <el-select v-model="gradeVal" class="primary-select" @change="projectsDetailList">
              <el-option v-for="item in gradeOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <!-- <div class="select-box" style="width: 200px" v-if="projType == '防汛仓库'">
            仓库类型:
            <el-select v-model="warehouseVal" class="primary-select" @change="projectsDetailList">
              <el-option v-for="item in warehouseOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div> -->
          <div class="select-box" v-if="projType == '救援队伍'">
            队伍类型:
            <el-select v-model="ranksVal" class="primary-select" @change="projectsDetailList">
              <el-option v-for="item in ranksOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="select-box search-area" style="width: 250px">
            工程名称:
            <el-input v-model="searchName" placeholder="请输入工程名称" @input="projectsDetailList"></el-input>
          </div>
          <div class="reset" @click="resetParam">重置</div>
        </div>
        <primary-map-table :columnList="columnList" :tableIndex="tableIndex" :dataList="dataList" :tableLoading="tableLoading"></primary-map-table>
      </div>
    </div>
  </div>
</template>

<script>
import { PrimaryMapTable } from "@/components";
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import { getDict } from "@/api/system";

export default {
  name: "DetailTable",
  props: {
    projType: {
      type: String,
      default: "水库",
    },
  },
  data() {
    return {
      areaVal: "", // 行政区划
      areaoOptions: [
        {
          value: "",
          label: "全部",
        },
      ],
      engScalVal: "", // 工程规模
      engScalOptions: [
        {
          value: "",
          label: "全部",
        },
        {
          value: "大（1）型",
          label: "大型",
        },
        {
          value: "中型",
          label: "中型",
        },
        {
          value: "小（1）型",
          label: "小（1）型",
        },
        {
          value: "小（2）型",
          label: "小（2）型",
        },
        {
          value: "其他",
          label: "其他",
        },
      ],
      mnpdCateVal: "", //山塘类型
      mnpdCateOptions: [
        {
          value: "",
          label: "全部",
        },
        {
          value: "高坝山塘",
          label: "高坝山塘",
        },
        {
          value: "屋顶山塘",
          label: "屋顶山塘",
        },
        {
          value: "普通山塘",
          label: "普通山塘",
        },
        {
          value: "其他",
          label: "其他",
        },
      ],
      gradeVal: "", //堤防级别||海塘级别
      gradeOptions: [
        {
          value: "",
          label: "全部",
        },
        {
          value: "1级",
          label: "1级",
        },
        {
          value: "2级",
          label: "2级",
        },
        {
          value: "3级",
          label: "3级",
        },
        {
          value: "4级",
          label: "4级",
        },
        {
          value: "5级",
          label: "5级",
        },
        {
          value: "其他",
          label: "其他",
        },
      ],
      designVal: "", //防潮标准
      designOptions: [
        {
          value: "",
          label: "全部",
        },
        {
          value: "0-10",
          label: "10年以下",
        },
        {
          value: "10-10",
          label: "10年",
        },
        {
          value: "20-20",
          label: "20年",
        },
        {
          value: "50-50",
          label: "50年",
        },
        {
          value: "100-100",
          label: "100年",
        },
        {
          value: "100-9999",
          label: "100年以上",
        },
      ],
      tableIndex: 0,
      columnList: [
        {
          prop: "resName",
          label: "名称",
        },
        {
          prop: "resLoc",
          label: "所在位置",
        },
        {
          prop: "engScal",
          label: "工程规模",
        },
        {
          prop: "watShedArea",
          label: "坝址控制流域面积",
        },
        {
          prop: "resType",
          label: "类型",
        },
        {
          prop: "engGrad",
          label: "工程等别",
        },
        {
          prop: "normPoolStagCap",
          label: "正常蓄水位相应库容",
        },
        {
          prop: "totCap",
          label: "总库容",
        },
        {
          prop: "admDep",
          label: "归口管理部门",
        },
        {
          prop: "damSizeLen",
          label: "主坝坝长",
        },
        {
          prop: "damSizeHig",
          label: "主坝坝高",
        },
        {
          prop: "designFloodStandard",
          label: "设计洪水标准(年)",
        },
        {
          prop: "checkFloodStandard",
          label: "校核洪水标准(年)",
        },
      ],
      dataList: [],
      stationData: [],
      tableLoading: false, // 表格loading
      searchName: "",
      dataCount: [],
      warehouseOptions: [
        { label: '省级仓库', value: '省级仓库' },
        { label: '市级仓库', value: '市级仓库' },
        { label: '县级仓库', value: '县级仓库' },
        { label: '省供销社', value: '省供销社' },
        { label: '社会厂家', value: '社会厂家' },
      ],
      warehouseVal: "",
      ranksOptions: [],
      ranksVal: "",
    };
  },
  computed: {},
  components: {
    PrimaryMapTable,
  },
  created() {
    this.setColumnList(this.projType);
  },
  mounted() {
    if (this.projType == "防汛仓库" || this.projType == "救援队伍") {
      this.ranksOptions = [
        {
          label: "全部",
          value: "",
        },
      ];
      // this._getDict("warehouseType");
      // this._getDict("teamType");
      if (this.projType == "防汛仓库") {
        this.getWarehouseCount();
      } else {
        this.getRanksCount();
      }
    } else {
      this.countList();
    }
    this.projectsDetailList();
    this.getAdcdList();
    this.$nextTick(() => {
      this.resetParam();
    });
  },
  watch: {},
  methods: {
    async _getDict(dictCode) {
      let res = await getDict(dictCode);
      let arr = res.data;
      if (dictCode == "warehouseType") {

      } else {
        arr.map((item) => {
          this.ranksOptions.push({
            label: item.dictName,
            value: item.id,
          });
        });
      }
    },
    async getAdcdList() {
      let opt = {
        areaCode: "3303",
        level: 3,
      };
      let res = await hydraulicApi.getAdcdList(opt);
      if (res.code == 0) {
        this.areaoOptions = [
          {
            value: "",
            label: "全部",
          },
        ];
        res.data.forEach((item) => {
          let obj = {
            value: item.adcd,
            label: item.adnm,
          };
          this.areaoOptions.push(obj);
        });
      }
    },
    //设置表头
    setColumnList(val) {
      let columnArr = [];
      // this.columnList = [];
      switch (val) {
        case "水库":
          columnArr = [
            {
              prop: "resName",
              label: "名称",
            },
            {
              prop: "resLoc",
              label: "所在位置",
            },
            {
              prop: "engScal",
              label: "工程规模",
            },
            {
              prop: "watShedArea",
              label: "坝址控制流域面积",
            },
            {
              prop: "resType",
              label: "类型",
            },
            {
              prop: "engGrad",
              label: "工程等别",
            },
            {
              prop: "normPoolStagCap",
              label: "正常蓄水位相应库容",
            },
            {
              prop: "totCap",
              label: "总库容",
            },
            {
              prop: "admDep",
              label: "归口管理部门",
            },
            {
              prop: "damSizeLen",
              label: "主坝坝长",
            },
            {
              prop: "damSizeHig",
              label: "主坝坝高",
            },
            {
              prop: "designFloodStandard",
              label: "设计洪水标准(年)",
            },
            {
              prop: "checkFloodStandard",
              label: "校核洪水标准(年)",
            },
          ];
          break;
        case "堤防":
          columnArr = [
            {
              prop: "dikeName",
              label: "名称",
            },
            {
              prop: "dikeGrad",
              label: "级别",
            },
            {
              prop: "dikeType",
              label: "堤防类型",
            },
            {
              prop: "dikePatt",
              label: "形式",
            },
            {
              prop: "dikeLen",
              label: "长度",
            },
            {
              prop: "flStaLen",
              label: "规划防洪(潮)标准[重现期](年)",
            },
          ];
          break;
        case "水电站":
          columnArr = [
            {
              prop: "hystName",
              label: "名称",
            },
            {
              prop: "bas",
              label: "所属流域",
            },
            {
              prop: "hystLoc",
              label: "所在位置",
            },
            {
              prop: "hystType",
              label: "类型",
            },
            {
              prop: "totInsCap",
              label: "总装机容量",
            },
            {
              prop: "engGrad",
              label: "工程等别",
            },
            {
              prop: "engScal",
              label: "工程规模",
            },
            {
              prop: "mainBuildGrad",
              label: "主要建筑物级别",
            },
            {
              prop: "engStat",
              label: "工程建设情况",
            },
            {
              prop: "startDate",
              label: "开工时间",
            },
            {
              prop: "compDate",
              label: "竣工验收时间",
            },
            {
              prop: "amdDep",
              label: "归口管理部门",
            },
            {
              prop: "generatorsCount",
              label: "机组台数(台)",
            },
            {
              prop: "isUseResvoirWater",
              label: "是否利用水库发电",
            },
            {
              prop: "localWaterCollectionArea",
              label: "本流域集雨面积(平方千米)",
            },
            {
              prop: "damMaxHeight",
              label: "最大坝高(米)",
            },
          ];
          break;
        case "山塘":
          columnArr = [
            {
              prop: "mnpdName",
              label: "名称",
            },
            {
              prop: "adnm",
              label: "所在县",
            },
            {
              prop: "strName",
              label: "所在乡镇(街道)名称",
            },
            {
              prop: "vilName",
              label: "所在村",
            },
            {
              prop: "zygn",
              label: "主要功能",
            },
            {
              prop: "syqr",
              label: "所有权人(管理责任主体)",
            },
            {
              prop: "syqrdh",
              label: "所有权人电话",
            },
            {
              prop: "xcyxm",
              label: "巡查员姓名",
            },
            {
              prop: "xcydh",
              label: "巡查员电话",
            },
            {
              prop: "zgbm",
              label: "主管部门",
            },
            {
              prop: "jhhsP",
              label: "校核洪水(水位)",
            },
            {
              prop: "zcsxswSw",
              label: "正常蓄水位(水位)",
            },
            {
              prop: "rcdsArea",
              label: "坝址以上集雨面积",
            },
            {
              prop: "jhhsRj",
              label: "总容积",
            },
            {
              prop: "mnpdCate",
              label: "山塘类别",
            },
            {
              prop: "mnpdFunc",
              label: "山塘功能",
            },
            {
              prop: "dmtpEle",
              label: "坝顶高程",
            },
            {
              prop: "madmHeight",
              label: "最大坝高",
            },
            {
              prop: "madmWidth",
              label: "坝顶宽度",
            },
            {
              prop: "damLength",
              label: "坝长",
            },
            {
              prop: "dscncltp",
              label: "主要泄洪建筑物型式",
            },
          ];
          break;
        case "农村供水工程":
          columnArr = [
            {
              prop: "cwsName",
              label: "名称",
            },
            {
              prop: "cwsLoc",
              label: "所在位置",
            },
            {
              prop: "engType",
              label: "工程类型",
            },
            {
              prop: "wasuType",
              label: "供水方式",
            },
            {
              prop: "wasuRang",
              label: "供水范围",
            },
            {
              prop: "desWasuScal",
              label: "设计供水规模",
            },
            {
              prop: "desWasuPop",
              label: "设计供水入口",
            },
            {
              prop: "engStat",
              label: "工程建设情况",
            },
            {
              prop: "wtPro",
              label: "水处理工艺",
            },
            {
              prop: "dfType",
              label: "消毒设施设备",
            },
            {
              prop: "watersupplytesttype",
              label: "水源类型",
            },
            {
              prop: "watervillages",
              label: "供水村庄",
            },
            {
              prop: "managerunitname",
              label: "管理单位名称",
            },
          ];
          break;
        case "泵站":
          columnArr = [
            {
              prop: "pustName",
              label: "名称",
            },
            {
              prop: "pustLoc",
              label: "泵站位置",
            },
            {
              prop: "bas",
              label: "所属流域",
            },
            {
              prop: "insFlow",
              label: "装机流量",
            },
            {
              prop: "insPow",
              label: "装机功率",
            },
            {
              prop: "engGrad",
              label: "工程等别",
            },
            {
              prop: "engScal",
              label: "工程规模",
            },
          ];
          break;
        case "水闸":
          columnArr = [
            {
              prop: "wagaName",
              label: "名称",
            },
            {
              prop: "wagaLoc",
              label: "所在位置",
            },
            {
              prop: "wagaType",
              label: "水闸类型",
            },
            {
              prop: "engGrad",
              label: "工程等别",
            },
            {
              prop: "engScal",
              label: "工程规模",
            },
            {
              prop: "desLockDisc",
              label: "设计最大过闸流量(m³/s)",
            },
            {
              prop: "gaorNum",
              label: "闸口数量",
            },
            {
              prop: "mainBuildGrad",
              label: "主要建筑物级别",
            },
          ];
          break;
        case "海塘":
          columnArr = [
            {
              prop: "seawallName",
              label: "名称",
            },
            {
              prop: "startLocation",
              label: "所在乡镇(街道)名称",
            },
            {
              prop: "seawallLength",
              label: "海塘长度",
            },
            {
              prop: "designTideStandard",
              label: "设计防潮标准",
            },
          ];
          break;
        case "灌区":
          columnArr = [
            {
              prop: "irrName",
              label: "名称",
            },
            {
              prop: "city",
              label: "所在市",
            },
            {
              prop: "county",
              label: "所在县",
            },
            {
              prop: "engScal",
              label: "工程规模",
            },
            {
              prop: "desIrrArea",
              label: "设计灌溉面积",
            },
            {
              prop: "effIrrArea",
              label: "有效灌溉面积",
            },
            {
              prop: "manUnit",
              label: "管理单位",
            },
            {
              prop: "benRan",
              label: "受益范围",
            },
          ];
          break;
        case "闸站":
          columnArr = [
            {
              prop: "gateName",
              label: "名称",
            },
            {
              prop: "locCity",
              label: "所在市",
            },
            {
              prop: "locCounty",
              label: "所在县",
            },
            {
              prop: "locStreet",
              label: "所在街道",
            },
            {
              prop: "bas",
              label: "所属流域",
            },
            {
              prop: "river",
              label: "所在河流",
            },
            {
              prop: "engScal",
              label: "工程规模",
            },
            {
              prop: "singleInsFlow",
              label: "单台装机流量",
            },
            {
              prop: "singleInsPow",
              label: "单台装机功率",
            },
            {
              prop: "maxFlow",
              label: "最大过闸流量",
            },
          ];
          break;
        case "水库水位":
          columnArr = [
            {
              prop: "gateName",
              label: "名称",
            },
            {
              prop: "gateName",
              label: "流域",
            },
            {
              prop: "gateName",
              label: "行政区划",
            },
            {
              prop: "gateName",
              label: "水位(m)",
            },
            {
              prop: "gateName",
              label: "汛限水位(m)",
            },
            {
              prop: "gateName",
              label: "防洪高水位(m)",
            },
            {
              prop: "gateName",
              label: "蓄水率(%)",
            },
            {
              prop: "gateName",
              label: "至汛限纳蓄能力(mm)",
            },
          ];
          break;
        case "断面":
          columnArr = [
            {
              prop: "name",
              label: "名称",
            },
            {
              prop: "fetchLength",
              label: "风区长度",
            },
            {
              prop: "windDirection",
              label: "风向",
            },
            {
              prop: "crestElevatio",
              label: "堤顶高程",
            },
          ];
          break;
        case "防汛仓库":
          columnArr = [
            {
              prop: "name",
              label: "物资点名称",
            },
            {
              prop: "type",
              label: "仓库类型",
            },
            {
              prop: "areaName",
              label: "行政区划",
            },
            {
              prop: "address",
              label: "地址",
            },
            {
              prop: "acreage",
              label: "仓库面积(km²)",
              sortable: true,
              width: 140,
            },
            {
              prop: "materialPrice",
              label: "物资价值(万元)",
              sortable: true,
              width: 140,
            },
            {
              prop: "chargePerson",
              label: "负责人",
            },
            {
              prop: "chargePhone",
              label: "联系电话",
            },
          ];
          break;
        case "救援队伍":
          columnArr = [
            {
              prop: "name",
              label: "抢险队伍名称",
            },
            {
              prop: "typeName",
              label: "队伍类型",
            },
            {
              prop: "areaName",
              label: "行政区划",
            },
            {
              prop: "address",
              label: "地址",
            },
            {
              prop: "existingNumber",
              label: "人数(人)",
              sortable: true,
            },
            {
              prop: "chargePerson",
              label: "负责人",
            },
            {
              prop: "chargePhone",
              label: "联系电话",
            },
          ];
          break;
        case '避灾点':
          columnArr = [
            {
              prop: "pointName",
              label: "避灾点名称",
            },
            {
              prop: "pointCapacity",
              label: "可容纳人数",
            },
          ];
          break;
        // case '潮位':
        //   break;
        // case '雨情':
        //   break;
        // case '水闸工情':
        //   break;
        // case '积水':
        //   break;
        // case '流量':
        //   break;
      }
      this.columnList = columnArr;
      this.tableIndex = Math.random(10000);
      // columnArr.forEach(item=>{
      //   this.columnList.push(item);
      // })
    },
    // select选择
    areaTypeHandle() { },
    //详情列表
    async projectsDetailList() {
      let opt = {
        type: this.projType !== '防汛仓库' ? this.projType : '',
        level: this.engScalVal,
        areaCode: this.areaVal,
        mnpdCate: this.mnpdCateVal,
        grade: this.gradeVal,
        projectName:this.searchName,
        designTideStandardStart: this.designVal.split("-")[0],
        designTideStandardEnd: this.designVal.split("-")[1],
      };
      if (this.projType == "救援队伍" || this.projType == "防汛仓库") {
        opt.name = this.searchName;
        opt.typeId =
          this.projType == "救援队伍" ? this.ranksVal : this.warehouseVal;
      }
      if (this.projType == "断面") {
        opt.name = this.searchName;
      }
      let res = {};
      switch (this.projType) {
        case '防汛仓库':
          res = await hydraulicApi.warehouses(opt);
          break;
        case '救援队伍':
          res = await hydraulicApi.teams(opt);
          break;
        case '断面':
          res = await hydraulicApi.sectionList(opt);
          break;
        case '避灾点':
          res = await hydraulicApi.avoidancePoint({pointName : this.searchName, pageNum: 1, pageSize: 99999 });
          break;
        default:
          res = await hydraulicApi.projectsDetailList(opt);
          break;
      }

      if (res.code === 0) {
        if (this.projType == "防汛仓库" || this.projType == "救援队伍" || this.projType == "避灾点" || this.projType == "断面") {
          this.dataList = res.data.list;
        } else {
          this.dataList = res.data;
        }
      } else {
        this.dataList = [];
      }
      // console.log(this.dataList);
    },
    //统计
    async countList() {
      let opt = {
        type: this.projType,
      };
      let res = await hydraulicApi.countProjectNum(opt);
      if (res.code == 0) {
        this.dataCount = res.data;
      } else {
        this.dataCount = [];
      }
    },
    //重置筛选条件
    resetParam() {
      this.engScalVal = "";
      this.areaVal = "";
      this.searchName = "";
      this.mnpdCateVal = "";
      this.gradeVal = "";
      this.designVal = "";
      this.ranksVal = "";
      this.warehouseVal = "";
      this.projectsDetailList();
    },
    // 仓库统计
    async getWarehouseCount() {
      let res = await hydraulicApi.warehousesCount();
      if (res.code == 0) {
        this.dataCount = res.data;
      }
    },
    // 队伍统计
    async getRanksCount() {
      let res = await hydraulicApi.teamCount();
      if (res.code == 0) {
        this.dataCount = res.data;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.scope-content {
  display: flex;
  height: 568px;
  padding: 0 22px;
  // 统计样式
  .statistics-sty {
    padding: 0 0 10px 0;
    width: 100%;
    display: flex;
    justify-content: left;
    border-bottom: 1px solid rgba($color: #409eff, $alpha: 0.4);
    > div {
      // margin-right: 50px;
      padding: 10px 0;
      margin-right: 30px;
      display: flex;
      justify-content: space-between;
      > p {
        padding-top: 5px;
        margin-right: 15px;
      }
      > div {
        > p:nth-child(1) {
          font-family: PingFangSC-Regular;
          font-size: 15px;
          color: #ffffff;
          letter-spacing: 0;
          line-height: 24px;
          font-weight: 400;
          margin-bottom: 5px;
        }
        > p:nth-child(2) {
          font-family: PingFangSC-Medium;
          font-size: 14px;
          color: #fff;
          letter-spacing: 0;
          line-height: 24px;
          font-weight: 500;
          > span {
            font-size: 24px;
            color: #56fefe;
          }
        }
      }
    }
  }
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

  .table-wrap {
    height: 474px;
  }

  ::v-deep .el-table {
    tr {
      background-color: transparent;
    }
  }
  .table-box {
    color: #fff;
    width: 100%;
    .select-group {
      padding: 16px 0 24px;
      display: flex;
      .select-box {
        width: 171px;
        margin-right: 11px;
        &.search-area {
          ::v-deep .el-input {
            width: fit-content;
            .el-input__inner {
              width: 180px;
            }
          }
        }
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
