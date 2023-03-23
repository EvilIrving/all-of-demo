

<template>
  <div ref="wrapper" class="wrapper">
    <div ref="query" class="query_wrapper whitestyle">
      <!-- 查询区域 -->
      <template>
        <el-select popper-class="query_select whitestyle" clearable v-model="selectAdcd" placeholder="请选择市县" @change="getTableListByQuery">
          <el-option v-for="item in adcdList" :key="item.adcd" :label="item.label" :value="item.adcd">
          </el-option>
        </el-select>
        <el-select popper-class="query_select whitestyle" clearable v-model="scaleSelect" placeholder="请选择规模" @change="getTableListByQuery">
          <el-option v-for="item in scaleList" :key="item.value" :label="item.scale" :value="item.value">
          </el-option>
        </el-select>
        <!-- <el-select popper-class="query_select whitestyle" clearable v-model="codeSelect" placeholder="请选择三色码" @change="getTableListByQuery">
          <el-option v-for="item in codeList" :key="item.value" :label="item.scale" :value="item.value">
          </el-option>
        </el-select>
        <el-select v-show="codeReasonList.length !== 0" popper-class="query_select whitestyle" clearable v-model="codeReasonSelect" placeholder="请选择赋码原因" @change="getTableListByQuery">
          <el-option v-for="item in codeReasonList" :key="item.code" :label="item.reason" :value="item.code">
          </el-option>
        </el-select> -->

        <el-input v-model="inputName" placeholder="请输入名称" clearable @input="getTableList"></el-input>
        <el-button plain key="querySearch" v-show="isFirstEntry" @click="getTableListByQuery">查询</el-button>
        <el-button plain key="nullSearch" v-show="!isFirstEntry" @click="getTableList">查询</el-button>
        <el-button plain key="queryExport" v-show="isFirstEntry" @click="exportListByQuery">导出</el-button>
        <el-button plain key="nullExport" v-show="!isFirstEntry" @click="exportList">导出</el-button>
      </template>
    </div>
    <!-- 表格 -->
    <div class="sk_table_wrapper whitestyle">
      <!-- 监测分析 -->
      <el-table ref="tb_wrappers" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="name" :show-overflow-tooltip="true" label="工程名称" align="center"></el-table-column>
        <el-table-column prop="cityADNM" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryADNM" label="所在县" align="center"></el-table-column>
        <el-table-column prop="scale" label="规模" align="center"></el-table-column>
        <el-table-column prop="threeYards" :label="'三色码状态'" align="center" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.threeYards === '红色'" class="red">红码</span>
            <span v-if="scope.row.threeYards === '黄色'" class="yellow">黄码</span>
            <span v-if="scope.row.threeYards === '绿色'" class="green">绿码</span>
          </template>
        </el-table-column>

        <el-table-column show-overflow-tooltip="" v-if="codeSelect == '黄色' || codeSelect == '红色'" prop="tyCause" :label="'红黄码原因'" align="center" width="100px">
        </el-table-column>

        <el-table-column label="行政责任人" align="center">
          <el-table-column prop="executive_gov_name" label="姓名" align="center"></el-table-column>
          <el-table-column prop="executive_gov_position" label="职务" align="center"></el-table-column>
        </el-table-column>
        <el-table-column label="技术责任人" align="center">
          <el-table-column prop="technical_name" label="姓名" align="center"></el-table-column>
          <el-table-column prop="techinical_position" label="职务" align="center"></el-table-column>
        </el-table-column>
        <el-table-column label="巡查责任人" align="center">
          <el-table-column prop="patrol_name" label="姓名" align="center"></el-table-column>
          <el-table-column prop="patrol_position" label="职务" align="center"></el-table-column>
        </el-table-column>
      </el-table>

    </div>
    <div class="project_popup_wrap" v-if="showProjectPopup">
      <ProjectPopup :popupData="popupData"></ProjectPopup>
    </div>
  </div>
</template>

<script>
import ProjectPopup from "./ProjectPopup";
import { listADCDtreeApi } from "@/api/api_utils";
import {
  safeAppraisalApi,
  cyjgNumApi,
} from "@/api/api_reservoir";
import { seawallProIndo, dealProblemInfo } from "@/api/api_seawall";
export default {
  components: {
    ProjectPopup,
  },
  props: {
    projectScale: {
      type: String,
      default: "",
    },
    sgzgMapType: {
      type: String,
      required: true,
      default: "",
    },
    dam: {
      type: Number,
      default: 2,
    },
    fullscreen: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      adcdList: [],
      scaleList: [
        { scale: "一级海塘", value: 1 },
        { scale: "二级海塘", value: 2 },
        { scale: "三级海塘", value: 3 },
        { scale: "四级海塘", value: 4 },
        { scale: "非标海塘", value: 5 },
      ],
      scaleSelect: "",
      codeList: [
        { scale: "红码", value: '红色' },
        { scale: "黄码", value: '黄色' },
        { scale: "绿码", value: '绿色' },
      ],
      codeSelect: "",
      codeReasonList: [],
      codeReasonSelect: "",
      securityList: [],
      securityValue: "",
      selectAdcd: "",
      inputName: "",
      tableHeight: 0,
      isFirstEntry: false, // 刚进来的时候是假，搜索之后变为真
      loading: false,
      siteList: [],
      showProjectPopup: false,
      popupData: {},
      colorCode: '',
      gpType:'',
    };
  },
  computed: {
    htColorCode() {
      return this.$store.state.htColorCode;
    },
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale ? this.$store.state.leftSelectProjectScale : '';
    },
  },
  watch: {
    codeSelect: {
      handler(newVal, oldVal) {
        this.codeReasonSelect = "";
        if (newVal === '红色') {
          // 红
          this.codeReasonList = [
            { code: "R1", reason: "超期未鉴定" },
            { code: "R2", reason: "三类塘未开工" },
            { code: "R3", reason: "预报潮位超设计" },
            { code: "R4", reason: "实时潮位超设计" },
            { code: "R5", reason: "严重隐患未整改" },
          ];
        } else if (newVal == '黄色') {
          // 黄
          this.codeReasonList = [
            { code: "Y1", reason: "一年内到期" },
            { code: "Y2", reason: "二类塘未开工" },
            { code: "Y3", reason: "较重隐患未整改" },
          ];
        } else {
          this.codeReasonList = [];
        }
        return newVal;
      },
      deep: true,
      immediate: true,
    },
    htColorCode: {
      handler(newVal, oldVal) {
        switch (newVal) {
          case 'yellow':
            this.colorCode = '黄色'
            break;
          case 'red':
            this.colorCode = '红色'
            break;
          case 'green':
            this.colorCode = '绿色'
            break;
          default:
            this.colorCode = ''
            break;
        }
        this.getTableList()
      },
      deep: true,
      immediate: true,
    },
    fullscreen(val) {
      this.resizeTable();
    },
  },
  mounted() {
    this.resizeTable();
    this.selectAdcd = this.leftSelectAdcd
      ? this.leftSelectAdcd
      : this.$localData("get", "userInfo").adcd;
    this.getAdcdOptions();
    this.getTableList();
  },
  methods: {
    resizeTable() {
      this.$nextTick(() => {
        const height =
          this.$refs.wrapper.clientHeight - this.$refs.query.clientHeight;
        this.tableHeight = height;
      });
    },
    async getAdcdOptions() {
      let res = await listADCDtreeApi({
        parentadcd: this.$localData("get", "userInfo").adcd,
        fetchAll: true,
      });
      if (res.success) {
        res.listTree.map((v, i) => {
          this.adcdList.push({
            label: v.ADNM,
            adcd: v.ADCD,
            value: i,
          });
        });
      }
    },
    // 初始查询列表
    async getTableList() {
      console.log("无条件查询");
      this.loading = true;
      let opt = {
        adcd: this.selectAdcd,
        name: this.inputName,
        projectScale: this.leftSelectProjectScale,
      };

      switch (this.sgzgMapType) {
        case "监测分析":
          opt.extractPram = this.colorCode;
          break;
        case "安全鉴定":
          opt.dam = this.dam;
          break;
        default:
          break;
      }
      let res = {};
      res = await seawallProIndo(opt);
      if (res.success) {
        this.loading = false;
        // 时间显示条件
        // 1、时间精确到日
        // 2、未加固：不显示时间
        // 3、加固中：开工时间
        // 4、已完工：开工 + 完工（除竣工外）
        // 5、已竣工：全部时间

        // 字段名称
        // 开工时间 R_REF_ST_TM
        // 完工时间 C_REF_FIN_TM
        // 蓄水验收时间 R_REF_WATER_TM
        // 竣工验收时间 R_REF_FIN_TM
        res.data.map((item) => {
          if (
            item.status.indexOf("未加固") !== -1 ||
            item.status.indexOf("未开工") !== -1 ||
            item.status == ""
          ) {
            item.R_REF_ST_TM = "-";
            item.C_REF_FIN_TM = "-";
            item.R_REF_WATER_TM = "-";
            item.R_REF_FIN_TM = "-";
          } else if (
            item.status.indexOf("加固中") !== -1 ||
            item.status.indexOf("已加固") !== -1 ||
            item.status.indexOf("已开工") !== -1
          ) {
            item.R_REF_ST_TM = item.R_REF_ST_TM?.slice(0, 11) || "-";
            item.C_REF_FIN_TM = "-";
            item.R_REF_WATER_TM = "-";
            item.R_REF_FIN_TM = "-";
          } else if (item.status.indexOf("已完工") !== -1) {
            item.R_REF_ST_TM = item.R_REF_ST_TM?.slice(0, 11) || "-";
            item.C_REF_FIN_TM = item.C_REF_FIN_TM?.slice(0, 11) || "-";
            item.R_REF_WATER_TM = item.R_REF_WATER_TM?.slice(0, 11) || "-";
            item.R_REF_FIN_TM = "-";
          } else if (item.status.indexOf("已竣工") !== -1) {
            item.R_REF_ST_TM = item.R_REF_ST_TM?.slice(0, 11) || "-";
            item.C_REF_FIN_TM = item.C_REF_FIN_TM?.slice(0, 11) || "-";
            item.R_REF_WATER_TM = item.R_REF_WATER_TM?.slice(0, 11) || "-";
            item.R_REF_FIN_TM = item.R_REF_FIN_TM?.slice(0, 11) || "-";
          }
        });
        this.siteList = res.data;
      }
    },
    // 根据查询条件查询列表
    async getTableListByQuery() {
      console.log("条件查询");
      this.loading = true;
      if (!this.isFirstEntry) this.isFirstEntry = true;
      let opt = {
        adcd: this.selectAdcd,
        name: this.inputName,
        gpType:this.codeReasonSelect
      };
      switch (this.sgzgMapType) {
        case "监测分析":
          opt.projectScale = this.scaleSelect;
          opt.extractPram = this.codeSelect;
          break;
        case "安全鉴定":
          opt.projectScale = this.scaleSelect;
          opt.extractPram = this.codeSelect;
          opt.dam = this.dam;
          break;
        default:
          break;
      }
      let res = {};
      res = await seawallProIndo(opt);
      if (res.success) {
        this.loading = false;
        // 时间显示条件
        // 1、时间精确到日
        // 2、未加固：不显示时间
        // 3、加固中：开工时间
        // 4、已完工：开工 + 完工（除竣工外）
        // 5、已竣工：全部时间

        // 字段名称
        // 开工时间 R_REF_ST_TM
        // 完工时间 C_REF_FIN_TM
        // 蓄水验收时间 R_REF_WATER_TM
        // 竣工验收时间 R_REF_FIN_TM
        res.data.map((item) => {
          switch (item.reinforcement) {
            case "未加固":
            case "未开工":
            case "":
              item.R_REF_ST_TM = "-";
              item.C_REF_FIN_TM = "-";
              item.R_REF_WATER_TM = "-";
              item.R_REF_FIN_TM = "-";
              break;
            case "加固中":
            case "已加固":
            case "已开工":
              item.R_REF_ST_TM = item.R_REF_ST_TM?.slice(0, 11) || "-";
              item.C_REF_FIN_TM = "-";
              item.R_REF_WATER_TM = "-";
              item.R_REF_FIN_TM = "-";
              break;
            case "已完工":
              item.R_REF_ST_TM = item.R_REF_ST_TM?.slice(0, 11) || "-";
              item.C_REF_FIN_TM = item.C_REF_FIN_TM?.slice(0, 11) || "-";
              item.R_REF_WATER_TM = item.R_REF_WATER_TM?.slice(0, 11) || "-";
              item.R_REF_FIN_TM = "-";
              break;
            case "已竣工":
              item.R_REF_ST_TM = item.R_REF_ST_TM?.slice(0, 11) || "-";
              item.C_REF_FIN_TM = item.C_REF_FIN_TM?.slice(0, 11) || "-";
              item.R_REF_WATER_TM = item.R_REF_WATER_TM?.slice(0, 11) || "-";
              item.R_REF_FIN_TM = item.R_REF_FIN_TM?.slice(0, 11) || "-";
              break;
          }
        });
        this.siteList = res.data;
      }
    },
    exportList() {
      console.log("无条件导出");
      let token = this.$localData("get", "token");
      switch (this.sgzgMapType) {
        case "监测分析":
          window.open(
            this.$config.host +
            `/mgt/bm/watertube/seawallProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&extractPram=${this.codeSelect}` +
            `&token=${token}`
          );
          break;
        default:
          window.open(
            this.$config.host +
            `/mgt/bm/watertube/seawallProjectToExcel` +
            `?extractPram=${this.codeSelect}` +
            `&adcd=${this.selectAdcd}` +
            `&dam=${this.dam}` +
            `&token=${token}`
          );
          break;
      }
    },
    exportListByQuery() {
      console.log("条件导出");
      let token = this.$localData("get", "token");
      switch (this.sgzgMapType) {
        case "监测分析":
          window.open(
            this.$config.host +
            `/mgt/bm/watertube/seawallProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&projectScale=${this.scaleSelect}` +
            `&extractPram=${this.codeSelect}` +
            `&gpType=${this.codeReasonSelect}` +
            `&token=${token}`
          );
          break;
        default:
          window.open(
            this.$config.host +
            `/mgt/bm/watertube/seawallProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&projectScale=${this.scaleSelect}` +
            `&extractPram=${this.codeSelect}` +
            `&gpType=${this.codeReasonSelect}` +
            `&dam=${this.dam}` +
            `&token=${token}`
          );
          break;
      }
    },
    sortBy(attr, rev) {
      rev = rev ? 1 : -1;
      return function (a, b) {
        a = a[attr];
        b = b[attr];
        if (a < b) {
          return rev * -1;
        }
        if (a > b) {
          return rev * 1;
        }
        return 0;
      };
    },
    clickProject(row) {
      this.popupData = row;
      this.popupData.name = row.name || row.name;
      this.popupData.prcd = row.prcd || row.prcd;
      this.showProjectPopup = true;
    },
    closeProjectPopup() {
      this.showProjectPopup = false;
    },
  },

};
</script>
<style  scoped lang="scss">
.green {
  color: #58c056;
}
.red {
  color: #ff3153;
}
.yellow {
  color: #f7b500;
}

.wrapper {
  .project_popup_wrap {
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    background: rgba(0, 0, 0, 0.5);
    @include flexbox();
    @include flexAI(center);
    @include flexJC(center);
  }
}
</style>
<style lang="scss">
.el-table--striped .el-table__body tr.el-table__row--striped td {
  background-color: #fff !important;
}
.wrapper {
  // 查询样式
  font-size: 16px;
  width: 100%;
  height: 100%;
  .query_select {
    margin: 0 10px;
  }
}
</style>
