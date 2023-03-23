

<template>
  <div ref="wrapper" class="wrapper">
    <div ref="query" class="query_wrapper">
      <!-- 查询区域 -->
      <template>
        <el-select
          popper-class="query_select"
          clearable
          v-model="selectAdcd"
          placeholder="请选择市县"
          @change="getTableListByQuery"
        >
          <el-option
            v-for="item in adcdList"
            :key="item.adcd"
            :label="item.label"
            :value="item.adcd"
          >
          </el-option>
        </el-select>
        <el-select
          popper-class="query_select"
          clearable
          v-model="scaleSelect"
          placeholder="请选择规模"
          @change="getTableListByQuery"
          v-if="sgzgMapType !== '防潮分析'"
        >
          <el-option
            v-for="item in scaleList"
            :key="item.value"
            :label="item.scale"
            :value="item.value"
          >
          </el-option>
        </el-select>
        <el-select
          v-if="sgzgMapType === '风险研判'"
          popper-class="query_select"
          clearable
          v-model="codeSelect"
          placeholder="请选择三色码"
          @change="changeCodeSelect"
        >
          <el-option
            v-for="item in codeList"
            :key="item.value"
            :label="item.scale"
            :value="item.value"
          >
          </el-option>
        </el-select>
        <el-select
          v-show="codeReasonList.length !== 0"
          popper-class="query_select"
          clearable
          v-model="codeReasonSelect"
          placeholder="请选择赋码原因"
          @change="getTableListByQuery"
        >
          <el-option
            v-for="item in codeReasonList"
            :key="item.code"
            :label="item.reason"
            :value="item.code"
          >
          </el-option>
        </el-select>
        <!-- <el-select popper-class="query_select" clearable v-model="securityValue" placeholder="请选择安全状态" @change="getTableListByQuery">
          <el-option v-for="item in securityList" :key="item.adcd" :label="item.label" :value="item.adcd">
          </el-option>
        </el-select> -->
        <el-input
          v-model="inputName"
          placeholder="请输入名称"
          clearable
          @input="getTableList"
        ></el-input>
        <el-button
          plain
          key="querySearch"
          v-show="isFirstEntry"
          @click="getTableListByQuery"
          >查询</el-button
        >
        <el-button
          plain
          key="nullSearch"
          v-show="!isFirstEntry"
          @click="getTableList"
          >查询</el-button
        >
        <el-button v-permission="'jlzs:export'" plain key="queryExport" v-show="isFirstEntry" @click="exportListByQuery">导出</el-button>
        <el-button v-permission="'jlzs:export'" plain key="nullExport" v-show="!isFirstEntry" @click="exportList">导出</el-button>
      </template>
    </div>
    <!-- 表格 -->
    <div class="sk_table_wrapper">
      <!-- 风险研判 -->
      <el-table
        v-if="sgzgMapType === '风险研判' && tableShowPeople"
        ref="tb_wrappers"
        v-loading="loading"
        :height="tableHeight"
        :data="siteList"
        stripe
        border
        style="width: 100%"
        @row-click="clickProject"
      >
        <el-table-column
          type="index"
          label="序号"
          align="center"
          width="60px"
        ></el-table-column>
        <el-table-column
          prop="name"
          :show-overflow-tooltip="true"
          label="工程名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="cityADNM"
          label="所在市"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="countryADNM"
          label="所在县"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="scale"
          label="规模"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="threeYards"
          label="三色码状态"
          align="center"
          width="120px"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.threeYards === '红色'" class="red">红码</span>
            <span v-if="scope.row.threeYards === '黄色'" class="yellow"
              >黄码</span
            >
            <span v-if="scope.row.threeYards === '绿色'" class="green"
              >绿码</span
            >
          </template>
        </el-table-column>
        <el-table-column
          v-if="codeSelectType"
          prop="tyCause"
          label="红黄码原因"
          align="center"
        ></el-table-column>
        <el-table-column
          v-if="!codeSelectType"
          label="行政责任人"
          align="center"
        >
          <el-table-column
            prop="executive_gov_name"
            label="姓名"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="executive_gov_position"
            label="职务"
            align="center"
          ></el-table-column>
        </el-table-column>
        <el-table-column
          v-if="!codeSelectType"
          label="技术责任人"
          align="center"
        >
          <el-table-column
            prop="technical_name"
            label="姓名"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="techinical_position"
            label="职务"
            align="center"
          ></el-table-column>
        </el-table-column>
        <el-table-column
          v-if="!codeSelectType"
          label="巡查责任人"
          align="center"
        >
          <el-table-column
            prop="patrol_name"
            label="姓名"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="patrol_position"
            label="职务"
            align="center"
          ></el-table-column>
        </el-table-column>
        <!-- 新增字段 “实际防潮等级”“海塘长度 -->
        <el-table-column
          prop="PREVENT_TIDE_STANDARD"
          label="实际防潮等级"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="seawall_length"
          label="海塘长度"
          align="center"
        ></el-table-column>
      </el-table>
      <!-- 一线海塘 -->
      <!-- 列表字段：工程名称，所在市，所在县，管理层级，工程规模、安全管理状态、海塘长度（米）、海塘型式、设计防潮标准（年）、实际防潮标准（年）、设计潮位（m）、塘顶平均高程（米）、海塘高度（最大值m）、海塘高度（最小值m）、塘顶宽度（最大值m）、塘顶宽度（最小值m） -->

      <el-table
        v-if="sgzgMapType === '一线海塘' || sgzgMapType === '非一线海塘'"
        v-loading="loading"
        :height="tableHeight"
        :data="siteList"
        stripe
        border
        style="width: 100%"
        @row-click="clickProject"
      >
        <el-table-column
          type="index"
          label="序号"
          align="center"
          width="60px"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="工程名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="cityADNM"
          label="所在市"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="countryADNM"
          label="所在县"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="superlevel"
          label="管理层级"
          align="center"
        ></el-table-column>

        <el-table-column
          prop="scale"
          label="工程规模"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="SA_APP_RST"
          label="安全管理状态"
          align="center"
        ></el-table-column>

        <el-table-column
          prop="seawall_ct_type"
          label="海塘型式"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="DESIGN_TIDE_STANDARD"
          label="设计防潮标准（年）"
          align="center"
        ></el-table-column>

        <el-table-column
          prop="PREVENT_TIDE_STANDARD"
          label="实际防潮标准（年）"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="designTideLevel"
          label="设计潮位（m）"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="AVERAGE_ALTITUDE"
          label="塘顶平均高程（米）"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="MAX_HEIGHT"
          label="海塘高度（最大值m）"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="MIN_HEIGHT"
          label="海塘高度（最小值m）"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="MAX_WIDTH"
          label="塘顶宽度（最大值m）"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="MIN_WIDTH"
          label="塘顶宽度（最小值m）"
          align="center"
        ></el-table-column>
        <!-- 新增字段 “实际防潮等级”“海塘长度 -->
        <el-table-column
          prop="PREVENT_TIDE_STANDARD"
          label="实际防潮等级"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="seawall_length"
          label="海塘长度(m)"
          align="center"
        ></el-table-column>
      </el-table>

      <!-- 限制运用 -->
      <el-table
        v-if="sgzgMapType === '限制运用'"
        v-loading="loading"
        :height="tableHeight"
        :data="siteList"
        stripe
        border
        style="width: 100%"
        @row-click="clickProject"
      >
        <el-table-column
          type="index"
          label="序号"
          align="center"
          width="60px"
        ></el-table-column>
        <el-table-column
          prop="cityADNM"
          label="所在市"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="countryADNM"
          label="所在县"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="工程名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="eng_scal"
          label="规模"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="safetyIdentification"
          label="安全管理状态"
          align="center"
        ></el-table-column>
        <!-- <el-table-column prop="safetyIdentification" label="坝型" align="center"></el-table-column> -->
        <el-table-column
          prop="waterLevel"
          label="当前水位(m)"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="limitWaterLevel"
          label="限制水位(m)"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="DSFLZ"
          label="设计洪水位(m)"
          align="center"
        ></el-table-column>
        <!-- <el-table-column prop="FHLV" label="防洪高水位(m)" align="center"></el-table-column> -->
        <!-- <el-table-column prop="NWL" label="正常蓄水位(m)" align="center"></el-table-column> -->
        <el-table-column label="更新时间" align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.waterleveltm
                ? scope.row.waterleveltm.slice(0, 13) + "时"
                : "-"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="Ltstatus" label="预警类型" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.Ltstatus === 11">超限制水位</span>
            <span v-if="scope.row.Ltstatus === 12">超设计洪水位</span>
            <span v-if="scope.row.Ltstatus === 13">超防洪高水位</span>
            <span v-if="scope.row.Ltstatus === 14">超设计洪水位</span>
          </template>
        </el-table-column>
        <el-table-col
          umn
          prop="LtstatusLevel"
          label="超限高度(m)"
          align="center"
        ></el-table-col>
        <el-table-column
          prop="ltHours"
          label="持续时长(h)"
          align="center"
        ></el-table-column>
        <!-- 新增字段 “实际防潮等级”“海塘长度 -->
        <el-table-column
          prop="PREVENT_TIDE_STANDARD"
          label="实际防潮等级"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="seawall_length"
          label="海塘长度(m)"
          align="center"
        ></el-table-column>
      </el-table>

      <!-- 安全鉴定 -->
      <!-- 工程名称、所在市、所在县、海塘级别、工程完工时间、工程竣工时间、最近一次安全鉴定时间、安全鉴定结论 -->
      <el-table
        v-if="sgzgMapType === '安全鉴定'"
        v-loading="loading"
        :height="tableHeight"
        :data="siteList"
        stripe
        border
        style="width: 100%"
        @row-click="clickProject"
      >
        <el-table-column
          type="index"
          label="序号"
          align="center"
          width="60px"
        ></el-table-column>
        <el-table-column
          prop="cityADNM"
          label="所在市"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="countryADNM"
          label="所在县"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="工程名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="scale"
          label="海塘级别"
          align="center"
        ></el-table-column>
        <el-table-column label="工程完工时间" align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.FINISH_TIME ? scope.row.FINISH_TIME.slice(0, 13) : "-"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="工程竣工时间" align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.COMPLETION_DATE
                ? scope.row.COMPLETION_DATE.slice(0, 13)
                : "-"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="最近一次安全鉴定时间" align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.SA_APP_TM ? scope.row.SA_APP_TM.slice(0, 13) : "-"
            }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-col
          umn
          prop="LtstatusLevel"
          label="安全鉴定结论"
          align="center"
        ></el-table-col> -->
      </el-table>

      <!-- 除险加固 -->
      <!-- 工程名称、所在市、所在县、海塘级别、安全管理状态、最近一次安全鉴定时间、安全鉴定结论、除险加固开工时间、除险加固完工时间、蓄水验收时间、除险加固竣工时间 -->
      <el-table
        v-if="sgzgMapType === '除险加固'"
        v-loading="loading"
        :height="tableHeight"
        :data="siteList"
        stripe
        border
        style="width: 100%"
        @row-click="clickProject"
      >
        <el-table-column
          type="index"
          label="序号"
          align="center"
          width="60px"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="工程名称"
          align="center"
          min-width="100"
        ></el-table-column>
        <el-table-column
          prop="cityADNM"
          label="所在市"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="countryADNM"
          label="所在县"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="project_scale"
          label="海塘级别"
          align="center"
          min-width="100"
        ></el-table-column>
        <el-table-column
          prop="safetyIdentification"
          label="安全管理状态"
          align="center"
        ></el-table-column>
        <el-table-column
          label="最近一次安全鉴定时间"
          align="center"
          min-width="100"
        >
          <template slot-scope="scope">
            <span>{{
              scope.row.SA_APP_TM ? scope.row.SA_APP_TM.slice(0, 11) : "-"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="SA_APP_RST"
          label="鉴定结论"
          align="center"
          min-width="100"
        ></el-table-column>
        <el-table-column
          label="除险加固开工时间"
          align="center"
          min-width="110"
        >
          <template slot-scope="scope">
            <span>{{
              scope.row.R_REF_ST_TM ? scope.row.R_REF_ST_TM.slice(0, 11) : "-"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="除险加固完工时间"
          align="center"
          min-width="100"
        >
          <template slot-scope="scope">
            <span>{{
              scope.row.R_REF_COM_TM ? scope.row.R_REF_COM_TM.slice(0, 11) : "-"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="蓄水验收时间" align="center" min-width="100">
          <template slot-scope="scope">
            <span>{{
              scope.row.R_REF_WATER_TM
                ? scope.row.R_REF_WATER_TM.slice(0, 11)
                : "-"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="除险加固竣工时间"
          align="center"
          min-width="100"
        >
          <template slot-scope="scope">
            <span>{{
              scope.row.C_REF_FIN_TM ? scope.row.C_REF_FIN_TM.slice(0, 11) : "-"
            }}</span>
          </template>
        </el-table-column>
        <!-- 新增字段 “实际防潮等级”“海塘长度 -->
        <el-table-column
          prop="PREVENT_TIDE_STANDARD"
          label="实际防潮等级"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="seawall_length"
          label="海塘长度(m)"
          align="center"
        ></el-table-column>
      </el-table>

      <!-- 问题处置 -->
      <!-- 工程名称、所在市、所在县、海塘级别、检查类型、检查主体、发生时间、问题描述、整改措施、整改进展、实际完成时间 -->
      <el-table
        v-if="sgzgMapType === '问题处置'"
        v-loading="loading"
        :height="tableHeight"
        :data="siteList"
        stripe
        border
        style="width: 100%"
        @row-click="clickProject"
      >
        <el-table-column
          type="index"
          label="序号"
          align="center"
          width="60px"
        ></el-table-column>
        <el-table-column
          prop="cityADNM"
          label="所在市"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="countryADNM"
          label="所在县"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="工程名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="scale"
          label="海塘级别"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="checktype"
          label="检查类型"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="CHECKMAIN"
          label="检查主体"
          align="center"
        ></el-table-column>
        <el-table-column prop="tm" label="发生时间" align="center">
          <template slot-scope="scope">
            <div :title="scope.row.adviceNote">
              {{ scope.row.tm ? scope.row.tm.substring(0, 11) : "-" }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          label="问题描述"
          align="center"
        >
          <template slot-scope="scope">
            <div :title="scope.row.adviceNote">
              {{ scope.row.adviceNote ? scope.row.adviceNote : "-" }}
            </div>
          </template>
        </el-table-column>

        <el-table-column
          :show-overflow-tooltip="true"
          label="整改措施"
          align="center"
        >
          <template slot-scope="scope">
            <div :title="scope.row.RESPONSEMETHOD">
              {{ scope.row.RESPONSEMETHOD ? scope.row.RESPONSEMETHOD : "-" }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="statusName"
          label="整改进展"
          align="center"
        ></el-table-column>
        <!-- <el-table-column prop="" label="计划完成时间" align="center">
          -
        </el-table-column> -->
        <el-table-column label="实际完成时间" align="center">
          <template slot-scope="scope">
            <div>
              {{
                scope.row.examineTm ? scope.row.examineTm.substring(0, 11) : "-"
              }}
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!-- 防潮分析 -->
      <el-table
        ref="tb_wrappers"
        v-if="sgzgMapType === '防潮分析'"
        v-loading="loading"
        :height="tableHeight"
        :data="siteList"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column
          type="index"
          label="序号"
          align="center"
          width="60px"
        ></el-table-column>
        <el-table-column
          prop="NAME"
          :show-overflow-tooltip="true"
          label="工程名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="cityadnm"
          label="所在市"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="countryadnm"
          label="所在县"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="DESIGN_TIDE_LEVEL"
          label="设计潮位(m)"
          align="center"
        >
          <template slot-scope="scope">
            <div>
              {{
                scope.row.DESIGN_TIDE_LEVEL
                  ? scope.row.DESIGN_TIDE_LEVEL.toFixed(2)
                  : 0
              }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="forecast" label="预报高潮位(m)" align="center">
          <template slot-scope="scope">
            <div>
              {{ scope.row.forecast ? scope.row.forecast.toFixed(2) : 0 }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="预报超设计(m)" align="center">
          <template slot-scope="scope">
            <div>
              {{
                scope.row.forecast - scope.row.DESIGN_TIDE_LEVEL
                  ? (scope.row.forecast - scope.row.DESIGN_TIDE_LEVEL).toFixed(
                      2
                    )
                  : 0
              }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="forecast_time" label="出现时间" align="center">
          <template slot-scope="scope">
            <div>
              {{
                scope.row.forecast_time
                  ? scope.row.forecast_time.substring(0, 16)
                  : ""
              }}
            </div>
          </template>
        </el-table-column>
        <!-- 新增字段 “实际防潮等级”“海塘长度 -->
        <el-table-column
          prop="PREVENT_TIDE_STANDARD"
          label="实际防潮等级"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="SEAWALL_LENGTH"
          label="海塘长度(m)"
          align="center"
        ></el-table-column>
      </el-table>
    </div>
    <div class="project_popup_wrap" v-if="showProjectPopup">
      <ProjectPopup :popupData="popupData"></ProjectPopup>
    </div>
  </div>
</template>

<script>
import ProjectPopup from "./SeawallPopup.vue";
import { listADCDtreeApi } from "@/api/api_utils";
import { cyjgNumApi } from "@/api/api_reservoir";
import {
  seawallProIndo,
  dealProblemInfo,
  seawallLeftStatisticApi,
} from "@/api/api_seawall";
export default {
  components: {
    ProjectPopup,
  },
  props: {
    checkedArr: {
      type: [Array, String],
      required: false,
      default: () => {
        return [];
      },
    },
    workStatus: {
      type: Array,
      required: false,
      default: () => {
        return [];
      },
    },
    projectScale: {
      type: String,
      default: "",
    },
    sgzgMapType: {
      type: String,
      required: true,
      default: "",
    },
    xzScale: {
      type: String,
      default: "",
    },
    sgzgSelectBasin: {
      type: String,
      required: true,
      default: "",
    },
    sgzgSelectAdcd: {
      type: String,
      required: true,
      default: "",
    },
    fullscreen: {
      type: Boolean,
      default: false,
    },
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
        { scale: "红码", value: "红色" },
        { scale: "黄码", value: "黄色" },
        { scale: "绿码", value: "绿色" },
      ],
      codeSelect: "",
      tableShowPeople: true,
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
    };
  },
  computed: {
    codeSelectType() {
      if (this.codeSelect === "红色" || this.codeSelect === "黄色") {
        return true;
      } else {
        return false;
      }
    },
    gpType() {
      switch (this.sgzgMapType) {
        case "风险研判":
          return 1;
          break;
        case "限制运用":
        case "二类坝":
        case "安全鉴定":
          return 4;
          break;
        case "问题处置":
          return 5;
          break;
        default:
          return 1;
          break;
      }
    },
    projectType() {
      let tempArr = [];
      switch (this.sgzgMapType) {
        case "风险研判":
          this.checkedArr.forEach((element) => {
            switch (element) {
              case "一级海塘":
                tempArr.push(1);
                break;
              case "二级海塘":
                tempArr.push(2);
                break;
              case "三级海塘":
                tempArr.push(3);
                break;
              case "四级海塘":
                tempArr.push(4);
                break;
              case "非标海塘":
                tempArr.push(5);
                break;
            }
          });
          // this.scaleSelect = tempArr
          return tempArr.join(",");
          break;
        case "安全鉴定":
          this.checkedArr.forEach((element) => {
            switch (element) {
              case "近一年到期":
                tempArr.push("近一年到期");
                break;
              case "二类坝":
                tempArr.push("二类坝");
                break;
              case "安全鉴定":
                tempArr.push("安全鉴定");
                break;
            }
          });
          return tempArr.join(",");
          break;
        case "限制运用":
          this.workStatus.forEach((element) => {
            switch (element) {
              case "超限制水位":
                tempArr.push(11);
                break;
              case "超设计洪水位":
                tempArr.push(14);
                break;
            }
          });
          return tempArr.join(",");
          break;
        case "二类坝":
        case "安全鉴定":
          this.workStatus.forEach((element) => {
            switch (element) {
              case "超限运行":
                tempArr.push(11);
                break;
              case "空库运行":
                tempArr.push(122);
                break;
              case "超设计洪水位":
                tempArr.push(14);
                break;
            }
          });
          return tempArr.join(",");
          break;
        case "问题处置":
          switch (this.checkedArr) {
            case "未处置":
              tempArr.push(16);
              break;
            case "处置中":
              tempArr.push(14);
              break;
            case "已处置":
              tempArr.push(15);
              break;
          }
          return tempArr.join(",");
          break;
        case "一线海塘":
          let checkedArr = [...this.workStatus, ...this.checkedArr];
          checkedArr.forEach((element) => {
            switch (element) {
              case "未开工":
                tempArr.push(6);
                break;
              case "已开工":
                tempArr.push(7);
                break;
              case "当年完工":
                tempArr.push(8);
                break;
              case "二类坝":
                tempArr.push(9);
                break;
              case "安全鉴定":
                tempArr.push(10);
                break;
            }
          });
          return tempArr.join(",");
          break;
        case "非一线海塘":
          this.workStatus.forEach((element) => {
            switch (element) {
              case "项目前期":
                tempArr.push(1);
                break;
              case "加固中":
                tempArr.push(2);
                break;
              case "已完工":
                tempArr.push(3);
                break;
              case "已竣工":
                tempArr.push(4);
                break;
            }
          });
          return tempArr.join(",");
          break;
        default:
          return this.checkedArr;
          break;
      }
    },
    screenType() {
      let tempArr = [];
      switch (this.sgzgMapType) {
        case "风险研判":
          switch (this.$store.state.colorCode) {
            case "red":
              this.codeSelect = "红色";
              tempArr.push("红色");
              break;
            case "green":
              this.codeSelect = "绿色";
              tempArr.push("绿色");
              break;
            case "yellow":
              this.codeSelect = "黄色";
              tempArr.push("黄色");
              break;
            default:
              this.codeSelect = "";
              tempArr = [];
              break;
          }
          return tempArr.join(",");
          break;
        case "限制运用":
          this.checkedArr.forEach((element) => {
            switch (element) {
              case "一类坝":
                tempArr.push(1);
                break;
              case "二类坝":
                tempArr.push(2);
                break;
              case "安全鉴定":
                tempArr.push(3);
                break;
            }
          });
          return tempArr.join(",");
          break;
        case "二类坝":
          tempArr.push(2);
          return tempArr.join(",");
          break;
        case "安全鉴定":
          tempArr.push(3);
          return tempArr.join(",");
          break;
        case "问题处置":
          this.workStatus.forEach((element) => {
            switch (element) {
              case "一般隐患":
                tempArr.push(1);
                break;
              case "较重隐患":
                tempArr.push(2);
                break;
              case "严重隐患":
                tempArr.push(3);
                break;
            }
          });
          return tempArr.join(",");
          break;
        default:
          return this.workStatus;
          break;
      }
    },
    rain() {
      return this.$store.state.rain;
    },
    colorCode() {
      return this.$store.state.colorCode;
    },
    numberSelected() {
      return this.$store.state.numberSelected;
    },
    planId() {
      return this.$store.state.planId;
    },
  },
  watch: {
    fullscreen(val) {
      this.resizeTable();
    },
  },
  mounted() {
    // console.log(this.colorCode, 'colorCode');
    this.resizeTable();
    this.selectAdcd = this.sgzgSelectAdcd
      ? this.sgzgSelectAdcd
      : this.$localData("get", "userInfo").adcd;
    this.getAdcdOptions();
    this.getTableList();
  },
  methods: {
    changeCodeSelect() {
      this.tableShowPeople = false;
      this.codeReasonSelect = "";
      if (this.codeSelect === "红色") {
        // 红
        this.codeReasonList = [
          { code: "R1", reason: "超期未鉴定" },
          { code: "R2", reason: "三类坝未开工" },
          { code: "R3", reason: "二类坝三年未开工" },
          { code: "R4", reason: "超设计洪水位" },
          { code: "R5", reason: "三类坝超限制水位（汛期中）" },
          { code: "R6", reason: "超限制运行5天及以上" },
          { code: "R7", reason: "严重隐患未整改" },
        ];
      } else if (this.codeSelect == "黄色") {
        // 黄
        this.codeReasonList = [
          { code: "Y1", reason: "一年内到期" },
          { code: "Y2", reason: "二类坝未开工" },
          { code: "Y3", reason: "超限制水位" },
          { code: "Y4", reason: "较重隐患未整改" },
        ];
      } else {
        this.codeReasonList = [];
      }
      this.$nextTick(() => {
        this.tableShowPeople = true;
      });
      this.getTableListByQuery()
    },
    resizeTable() {
      this.$nextTick(() => {
        const height =
          this.$refs.wrapper.clientHeight - this.$refs.query.clientHeight;
        this.tableHeight = height;
      });
    },
    resizeTable2() {
      let list = this.siteList;
      this.sitelist = [];
      setTimeout(() => {
        this.sitelist = list;
      }, 1000);
      // this.$nextTick(() => {
      //   const height = this.$refs.wrapper.clientHeight - this.$refs.query.clientHeight;
      //   this.tableHeight = height;
      //   this.sitelist = list
      // });
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
        basinCode: this.sgzgSelectBasin,
        name: this.inputName,
        gpType: this.gpType,
      };

      let safeOpt = {
        adcd: this.selectAdcd,
        basinCode: this.sgzgSelectBasin,
        name: this.inputName,
        interType: 2,
      };
      switch (this.sgzgMapType) {
        case "风险研判":
          opt.projectScale = this.projectType;
          opt.gpType = this.codeReasonSelect;
          opt.extractPram = this.screenType;
          break;
        case "限制运用":
          opt.projectScale = this.xzScale;
          opt.screen = this.projectType;
          opt.dam = this.screenType;
          break;
        case "安全鉴定":
          opt.dam = this.projectType;
          opt.extractPram = "1";
          break;
        case "一线海塘":
          opt.screen = this.projectType;
          opt.dam = 16;
          delete opt.gpType;
          // opt.extractPram = this.sgzgCxjgCl
          break;
        case "非一线海塘":
          safeOpt.extractPram = this.projectType;
          opt.dam = 17;
          delete opt.gpType;
          break;
        case "问题处置":
          opt.screen = this.projectType;
          opt.extractPram = this.screenType;
          break;
        default:
          break;
      }
      let res = {};
      if (this.sgzgMapType === "安全鉴定") {
        let safeOption = {
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basinCode: this.sgzgSelectBasin,
          dam: this.checkedArr.join(","),
        };
        res = await seawallProIndo(safeOption);
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
        return;
      }

      if (this.sgzgMapType === "问题处置") {
        res = await dealProblemInfo({
          adcd: this.selectAdcd,
          basinCode: this.sgzgSelectBasin,
          projectScale: this.scaleSelect,
          name: this.inputName,
          dealType: this.checkedArr,
        });
        if (res.success) {
          this.loading = false;
          this.siteList = res.data;
        }
        return;
      }
      if (this.sgzgMapType === "防潮分析") {
        let riskOpt = {
          statistic: "mapList",
          itemCategory: "forecast,planId",
          itemValue: "overCnt," + this.planId,
          name: this.inputName,
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
        };
        res = await seawallLeftStatisticApi(riskOpt);
        if (res.success) {
          this.siteList = res.data;
          this.loading = false;
        }
        return;
      }
      if (this.sgzgMapType === "除险加固") {
        let optObj = {
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basinCode: this.sgzgSelectBasin,
          dam: this.checkedArr,
        };
        res = await seawallProIndo(optObj);
        if (res.success) {
          this.loading = false;
          this.siteList = res.data;
        }
        return;
      }
      res = await seawallProIndo(opt);
      if (res.success) {
        this.loading = false;
        this.siteList = res.data;
      }
    },
    // 根据查询条件查询列表
    async getTableListByQuery() {
      console.log("条件查询");
      this.loading = true;
      if (!this.isFirstEntry) this.isFirstEntry = true;
      let queryOpt = {
        adcd: this.selectAdcd,
        basinCode: this.sgzgSelectBasin,
        name: this.inputName,
        gpType: this.gpType,
      };
      let opt = {
        adcd: this.selectAdcd,
        basinCode: this.sgzgSelectBasin,
        name: this.inputName,
        gpType: this.gpType,
      };
      let safeOpt = {
        adcd: this.selectAdcd,
        basinCode: this.sgzgSelectBasin,
        name: this.inputName,
        interType: 2,
      };
      switch (this.sgzgMapType) {
        case "风险研判":
          queryOpt.projectScale = this.scaleSelect;
          queryOpt.gpType = this.codeReasonSelect;
          queryOpt.extractPram = this.codeSelect;
          break;
        case "限制运用":
          opt.projectScale = this.scaleSelect;
          opt.screen = this.projectType;
          opt.dam = this.screenType;
          break;
        case "安全鉴定":
          opt.projectScale = this.scaleSelect;
          opt.screen = this.projectType;
          opt.dam = this.screenType;
          opt.extractPram = "1";
          break;
        case "一线海塘":
          opt.projectScale = this.scaleSelect;
          opt.screen = this.projectType;
          opt.dam = 16;
          break;
        case "非一线海塘":
          safeOpt.projectScale = this.scaleSelect;
          safeOpt.extractPram = this.projectType;
          opt.dam = 17;
          break;
        case "问题处置":
          opt.projectScale = this.scaleSelect;
          opt.screen = this.projectType;
          opt.extractPram = this.screenType;
          break;
        default:
          break;
      }
      let res = {};
      if (this.sgzgMapType === "安全鉴定") {
        let safeOption = {
          adcd: this.selectAdcd,
          basinCode: this.sgzgSelectBasin,
          projectScale: this.scaleSelect,
          name: this.inputName,
          dam: this.checkedArr.join(","),
        };
        res = await seawallProIndo(safeOption);
        if (res.success) {
          this.loading = false;
          // 时间显示条件
          // 1、时间精确到日
          // 2、未加固 未开工 或 空：不显示时间
          // 3、加固中 已加固 已开工：开工时间
          // 4、已完工：开工 + 完工（除竣工外）
          // 5、已竣工：全部时间

          // 字段名称
          // 开工时间 R_REF_ST_TM
          // 完工时间 C_REF_FIN_TM
          // 蓄水验收时间 R_REF_WATER_TM
          // 竣工验收时间 R_REF_FIN_TM
          res.data.map((item) => {
            switch (item.status) {
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
        return;
      }

      if (this.sgzgMapType === "非一线海塘") {
        res = await cyjgNumApi(safeOpt);
        if (res.success) {
          this.loading = false;
          // 字段名称
          // 开工时间 R_REF_ST_TM
          // 完工时间 C_REF_FIN_TM
          // 蓄水验收时间 R_REF_WATER_TM
          // 竣工验收时间 R_REF_FIN_TM
          res.data.map((item) => {
            item.R_REF_ST_TM = item.R_REF_ST_TM?.slice(0, 11) || "-";
            item.C_REF_FIN_TM = item.C_REF_FIN_TM?.slice(0, 11) || "-";
            item.R_REF_WATER_TM = item.R_REF_WATER_TM?.slice(0, 11) || "-";
            item.R_REF_FIN_TM = item.R_REF_FIN_TM?.slice(0, 11) || "-";
          });
          this.siteList = res.data;
        }
        return;
      }

      if (this.sgzgMapType === "问题处置") {
        res = await dealProblemInfo({
          adcd: this.selectAdcd,
          basinCode: this.sgzgSelectBasin,
          projectScale: this.scaleSelect,
          name: this.inputName,
          dealType: this.checkedArr,
        });
        if (res.success) {
          this.loading = false;
          this.siteList = res.data;
        }
        return;
      }

      if (this.sgzgMapType === "防潮分析") {
        let riskOpt = {
          statistic: "mapList",
          itemCategory: "forecast,planId",
          itemValue: "overCnt," + this.planId,
          name: this.inputName,
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
        };
        res = await seawallLeftStatisticApi(riskOpt);
        if (res.success) {
          this.siteList = res.data;
          this.loading = false;
        }
        return;
      }
      if (this.sgzgMapType === "除险加固") {
        let optObj = {
          adcd: this.sgzgSelectAdcd
            ? this.sgzgSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          basinCode: this.sgzgSelectBasin,
          projectScale: this.scaleSelect,
          name: this.inputName,
          dam: this.checkedArr,
        };
        res = await seawallProIndo(optObj);
        if (res.success) {
          this.loading = false;
          this.siteList = res.data;
        }
        return;
      }
      res = await seawallProIndo(queryOpt);
      if (res.success) {
        this.siteList = res.data;
        this.loading = false;
      }
    },
    exportList() {
      console.log("无条件导出");
      let token = this.$localData("get", "token");
      switch (this.sgzgMapType) {
        case "风险研判":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&projectScale=${this.projectType}` +
              `&screen=${this.screenType}` +
              `&extractPram=${this.codeReasonSelect}` +
              `&gpType=${this.gpType}` +
              `&token=${token}`
          );
          break;
        case "安全鉴定":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&dam=${this.projectType}` +
              `&interType=2` +
              `&gpType=2` +
              `&token=${token}`
          );
          break;
        case "一线海塘":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&screen=${this.projectType}` +
              `&gpType=${this.gpType}` +
              `&token=${token}`
          );
          break;
        case "非一线海塘":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&extractPram=${this.projectType}` +
              `&gpType=${this.gpType}` +
              `&token=${token}`
          );
          break;
        case "问题处置":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&screen=${this.projectType}` +
              `&extractPram =${this.screenType}` +
              `&gpType=${this.gpType}` +
              `&token=${token}`
          );
          break;
        case "限制运用":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&screen=${this.projectType}` +
              `&dam=${this.screenType}` +
              `&gpType=${this.gpType}` +
              `&token=${token}`
          );
          break;
        case "二类坝":
        case "安全鉴定":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&screen=${this.projectType}` +
              `&dam=${this.screenType}` +
              `&gpType=${this.gpType}` +
              `&extractPram=1` +
              `&token=${token}`
          );
          break;
        default:
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&projectScale=${this.projectType}` +
              `&gpType=${this.gpType}` +
              `&token=${token}`
          );
          break;
      }
    },
    exportListByQuery() {
      let token = this.$localData("get", "token");
      switch (this.sgzgMapType) {
        case "风险研判":
          const screen = this.codeSelect === '黄色' ? '18' : this.codeSelect === '红色' ? '19' : this.codeSelect === '绿色' ? '17' : ''
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&projectScale=${this.scaleSelect}` +
              `&screen=${screen}` +
              `&extractPram=${this.codeReasonSelect}` +
              `&gpType=${this.gpType}` +
              `&token=${token}`
          );
          break;
        case "安全鉴定":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&dam=${this.projectType}` +
              `&projectScale=${this.scaleSelect}` +
              `&interType=2` +
              `&gpType=2` +
              `&token=${token}`
          );
          break;
        case "一线海塘":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&screen=${this.projectType}` +
              // `&extractPram=${this.sgzgCxjgCl}` +
              `&gpType=${this.gpType}` +
              `&projectScale=${this.scaleSelect}` +
              `&token=${token}`
          );
          break;
        case "非一线海塘":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&extractPram=${this.projectType}` +
              `&gpType=${this.gpType}` +
              `&projectScale=${this.scaleSelect}` +
              `&token=${token}`
          );
          break;
        case "问题处置":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&screen=${this.projectType}` +
              `&gpType=${this.screenType}` +
              `&extractPram =${this.gpType}` +
              `&projectScale=${this.scaleSelect}` +
              `&token=${token}`
          );
          break;
        case "限制运用":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&screen=${this.projectType}` +
              `&projectScale=${this.scaleSelect}` +
              `&dam=${this.screenType}` +
              `&gpType=${this.gpType}` +
              `&token=${token}`
          );
          break;
        case "二类坝":
        case "安全鉴定":
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&screen=${this.projectType}` +
              `&projectScale=${this.scaleSelect}` +
              `&dam=${this.screenType}` +
              `&gpType=${this.gpType}` +
              `&extractPram=1` +
              `&token=${token}`
          );
          break;
        default:
          window.open(
            this.$config.host +
              `/mgt/bm/reservoirWT/generateProjectToExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basinCode=${this.sgzgSelectBasin}` +
              `&projectScale=${this.scaleSelect}` +
              `&gpType=${this.gpType}` +
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
      this.popupData.name = row.NAME || row.name;
      this.popupData.prcd = row.PRCD || row.prcd;
      this.showProjectPopup = true;
    },
    closeProjectPopup() {
      this.showProjectPopup = false;
    },
    beforeDestroy() {
      window.removeEventListener("resize", this.handleWindowResize);
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
  background-color: #052176 !important;
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
