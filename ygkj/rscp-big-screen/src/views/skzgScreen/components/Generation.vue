

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
        <el-select v-if="sgzgMapType === '风险研判'" popper-class="query_select whitestyle" clearable v-model="codeSelect" placeholder="请选择三色码" @change="getTableListByQuery">
          <el-option v-for="item in codeList" :key="item.value" :label="item.scale" :value="item.value">
          </el-option>
        </el-select>
        <el-select v-show="codeReasonList.length !== 0" popper-class="query_select whitestyle" clearable v-model="codeReasonSelect" placeholder="请选择赋码原因" @change="getTableListByQuery">
          <el-option v-for="item in codeReasonList" :key="item.code" :label="item.reason" :value="item.code">
          </el-option>
        </el-select>
        <!-- <el-select popper-class="query_select" clearable v-model="securityValue" placeholder="请选择安全状态" @change="getTableListByQuery">
          <el-option v-for="item in securityList" :key="item.adcd" :label="item.label" :value="item.adcd">
          </el-option>
        </el-select> -->
        <el-input v-model="inputName" placeholder="请输入名称" clearable @input="getTableList"></el-input>
        <el-button plain key="querySearch" v-show="isFirstEntry" @click="getTableListByQuery">查询</el-button>
        <el-button plain key="nullSearch" v-show="!isFirstEntry" @click="getTableList">查询</el-button>
        <el-button v-permission="'jlzs:export'" plain key="queryExport" v-show="isFirstEntry" @click="exportListByQuery">导出</el-button>
        <el-button v-permission="'jlzs:export'" plain key="nullExport" v-show="!isFirstEntry" @click="exportList">导出</el-button>
      </template>
    </div>
    <!-- 表格 -->
    <div class="sk_table_wrapper whitestyle">
      <!-- 风险研判 -->
      <el-table v-if="sgzgMapType === '风险研判'" ref="tb_wrappers" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="NAME" :show-overflow-tooltip="true" label="工程名称" align="center"></el-table-column>
        <el-table-column prop="adcdctiy" label="所在市" align="center"></el-table-column>
        <el-table-column prop="adcdcountry" label="所在县" align="center"></el-table-column>
        <el-table-column prop="scale" label="规模" align="center"></el-table-column>
        <el-table-column prop="CODE" :label="'三色码状态'" align="center" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.CODE === '红色'" class="red">红码</span>
            <span v-if="scope.row.CODE === '黄色'" class="yellow">黄码</span>
            <span v-if="scope.row.CODE === '绿色'" class="green">绿码</span>
          </template>
        </el-table-column>

        <el-table-column v-if="colorCode == 'yellow' || colorCode == 'red'" prop="tyCause" :label="'红黄码原因'" align="center" width="500px"></el-table-column>
        <!-- <el-table-column prop="SA_APP_RST" label="安全状况" align="center"></el-table-column>
        <el-table-column prop="if_department" align="center">
          <template slot="header">
            <div>有无</div>
            <div>管理单位</div>
          </template>
          <template slot-scope="scope">
            <span>{{ scope.row.if_department }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="R_REF_ST_TM" label="除险加固开工时间" align="center"></el-table-column>
        <el-table-column prop="R_REF_COM_TM" label="除险加固完工时间" align="center"></el-table-column> -->
        <el-table-column v-if="colorCode == 'all' || colorCode == 'green'" label="行政责任人" align="center">
          <el-table-column prop="executive_gov_name" label="姓名" align="center"></el-table-column>
          <el-table-column prop="executive_gov_position" label="职务" align="center"></el-table-column>
        </el-table-column>
        <el-table-column v-if="colorCode == 'all' || colorCode == 'green'" label="技术责任人" align="center">
          <el-table-column prop="technical_name" label="姓名" align="center"></el-table-column>
          <el-table-column prop="techinical_position" label="职务" align="center"></el-table-column>
        </el-table-column>
        <el-table-column v-if="colorCode == 'all' || colorCode == 'green'" label="巡查责任人" align="center">
          <el-table-column prop="patrol_name" label="姓名" align="center"></el-table-column>
          <el-table-column prop="patrol_position" label="职务" align="center"></el-table-column>
        </el-table-column>
      </el-table>

      <!-- 除险加固 -->
      <el-table v-if="sgzgMapType === '除险加固' || sgzgMapType === '存量清零'" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="adcdctiy" label="所在市" align="center"></el-table-column>
        <el-table-column prop="adcdcountry" label="所在县" align="center"></el-table-column>
        <el-table-column prop="NAME" label="工程名称" align="center"></el-table-column>
        <el-table-column prop="scale" label="规模" align="center"></el-table-column>
        <el-table-column prop="status" label="安全管理状态" align="center"></el-table-column>
        <el-table-column prop="SA_APP_RST" label="最新一次安全鉴定结果" align="center"></el-table-column>
        <el-table-column prop="R_REF_ST_TM" label="除险加固开工时间" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.R_REF_ST_TM }}</span>
          </template>
        </el-table-column>
        <el-table-column label="除险加固完工时间" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.C_REF_FIN_TM }}</span>
          </template>
        </el-table-column>
        <el-table-column label="蓄水验收时间" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.R_REF_WATER_TM }}</span>
          </template>
        </el-table-column>
        <el-table-column label="竣工验收时间" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.R_REF_FIN_TM }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="备注" align="center"></el-table-column>
      </el-table>

      <!-- 限制运用 -->
      <el-table v-if="sgzgMapType === '限制运用'" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="cityADNM" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryADNM" label="所在县" align="center"></el-table-column>
        <el-table-column prop="name" label="工程名称" align="center"></el-table-column>
        <el-table-column prop="eng_scal" label="规模" align="center"></el-table-column>
        <el-table-column prop="safetyIdentification" label="安全管理状态" align="center"></el-table-column>
        <!-- <el-table-column prop="safetyIdentification" label="坝型" align="center"></el-table-column> -->
        <el-table-column prop="waterLevel" label="当前水位(m)" align="center"></el-table-column>
        <el-table-column prop="limitWaterLevel" label="限制水位(m)" align="center"></el-table-column>
        <el-table-column prop="DSFLZ" label="设计洪水位(m)" align="center"></el-table-column>
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
        <el-table-col umn prop="LtstatusLevel" label="超限高度(m)" align="center"></el-table-col>
        <el-table-column prop="ltHours" label="持续时长(h)" align="center"></el-table-column>
      </el-table>

      <!-- 三类坝 -->
      <el-table v-if="sgzgMapType === '二类坝' || sgzgMapType === '三类坝'" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="cityADNM" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryADNM" label="所在县" align="center"></el-table-column>
        <el-table-column prop="name" label="工程名称" align="center"></el-table-column>
        <el-table-column prop="eng_scal" label="规模" align="center"></el-table-column>
        <!-- <el-table-column prop="safeState" label="安全鉴定" align="center"></el-table-column>
        <el-table-column prop="reinforceState" label="除险加固状态" align="center"></el-table-column> -->
        <el-table-column prop="safetyIdentification" label="安全管理状态" align="center"></el-table-column>
        <el-table-column prop="waterLevel" label="当前水位(m)" align="center"></el-table-column>
        <el-table-column prop="DDZ" label="死水位(m)" align="center"></el-table-column>
        <el-table-column prop="limitWaterLevel" label="限制水位(m)" align="center"></el-table-column>
        <el-table-column prop="DSFLZ" label="设计洪水位(m)" align="center"></el-table-column>
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
        <el-table-col umn prop="LtstatusLevel" label="超限高度(m)" align="center"></el-table-col>
        <el-table-column prop="ltHours" label="持续时长(h)" align="center"></el-table-column>
      </el-table>

      <!-- 安全鉴定 -->
      <el-table v-if="sgzgMapType === '安全鉴定'" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="name" label="工程名称" align="center" min-width="100"></el-table-column>
        <el-table-column prop="cityADNM" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryADNM" label="所在县" align="center"></el-table-column>
        <el-table-column prop="project_scalestr" label="工程规模" align="center" min-width="100"></el-table-column>
        <el-table-column prop="MD_DAM_MAX_HEIGHT" label="最大坝高(m)" align="center"></el-table-column>
        <!-- <el-table-column label="蓄水验收时间" align="center" min-width="110">
          <template slot-scope="scope">
            <span>{{scope.row.R_REF_WATER_TM ? scope.row.R_REF_WATER_TM.slice(0,11) : '-'}}</span>
          </template>
        </el-table-column>
        <el-table-column label="完工时间" align="center" min-width="100">
          <template slot-scope="scope">
            <span>{{scope.row.FINISH_TIME ? scope.row.FINISH_TIME.slice(0,11) : '-'}}</span>
          </template>
        </el-table-column>
        <el-table-column label="竣工时间" align="center" min-width="100">
          <template slot-scope="scope">
            <span>{{scope.row.COMPLETE_TIME ? scope.row.COMPLETE_TIME.slice(0,11) : '-'}}</span>
          </template>
        </el-table-column> -->
        <el-table-column label="始建时间" align="center" min-width="100">
          <template slot-scope="scope">
            <span>{{
              scope.row.FINISH_TIME ? scope.row.FINISH_TIME.slice(0, 11) : "-"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="R_REF_WATER_TM" label="最近一次安全鉴定" align="center">
          <!-- <el-table-column prop="APPRAISAL" label="是否开展安全鉴定" align="center"></el-table-column> -->
          <el-table-column label="鉴定时间" align="center" min-width="100">
            <template slot-scope="scope">
              <span>{{
                scope.row.SA_APP_TM ? scope.row.SA_APP_TM.slice(0, 11) : "-"
              }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="SA_APP_RST" label="鉴定结论" align="center"></el-table-column>
          <!-- <el-table-column prop="reinforcement" label="有无加固" align="center"></el-table-column> -->
        </el-table-column>
        <!-- <el-table-column prop="R_REF_WATER_TM" label="加固计划" align="center">
          <el-table-column label="开工时间" align="center" min-width="100">
            <template slot-scope="scope">
              <span>{{scope.row.P_ST_TM ? scope.row.P_ST_TM.slice(0,11) : '-'}}</span>
            </template>
          </el-table-column>
          <el-table-column label="除险加固完工时间" align="center" min-width="100">
            <template slot-scope="scope">
              <span>{{scope.row.P_ED_TM ? scope.row.P_ED_TM.slice(0,11) : '-'}}</span>
            </template>
          </el-table-column>
        </el-table-column> -->
        <el-table-column prop="R_REF_WATER_TM" label="最近一次除险加固情况" align="center">
          <el-table-column label="开工时间" align="center" min-width="100">
            <template slot-scope="scope">
              <span>{{ scope.row.R_REF_ST_TM }}</span>
            </template>
          </el-table-column>
          <el-table-column label="完工时间" align="center" min-width="100">
            <template slot-scope="scope">
              <span>{{ scope.row.C_REF_FIN_TM }}</span>
            </template>
          </el-table-column>
          <el-table-column label="蓄水验收时间" align="center" min-width="100">
            <template slot-scope="scope">
              <span>{{ scope.row.R_REF_WATER_TM }}</span>
            </template>
          </el-table-column>
          <el-table-column label="竣工验收时间" align="center" min-width="100">
            <template slot-scope="scope">
              <span>{{ scope.row.R_REF_FIN_TM }}</span>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>

      <!-- 问题处置 -->
      <!-- 整改进展（待开展、开展中、已完成）-->
      <el-table v-if="sgzgMapType === '问题处置'" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="cityADNM" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryADNM" label="所在县" align="center"></el-table-column>
        <el-table-column prop="proName" label="工程名称" align="center"></el-table-column>
        <el-table-column prop="scale" label="规模" align="center"></el-table-column>
        <!-- <el-table-column prop="damTypeStructure" label="坝型" align="center"></el-table-column> -->
        <el-table-column prop="wtly" label="问题来源" align="center"></el-table-column>
        <el-table-column prop="CHECKLEVELNAME" label="检查主体" align="center"></el-table-column>
        <el-table-column prop="TM" label="发生时间" align="center"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" label="问题描述" align="center">
          <template slot-scope="scope">
            <div :title="scope.row.QUESTION">
              {{ scope.row.QUESTION ? scope.row.QUESTION : "-" }}
            </div>
          </template>
        </el-table-column>

        <el-table-column :show-overflow-tooltip="true" label="整改措施" align="center">
          <template slot-scope="scope">
            <div :title="scope.row.RESPONSEMETHOD">
              {{ scope.row.RESPONSEMETHOD ? scope.row.RESPONSEMETHOD : "-" }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="statusName" label="整改进展" align="center"></el-table-column>
        <el-table-column prop="" label="计划完成时间" align="center">
          -
        </el-table-column>
        <el-table-column label="实际完成时间" align="center">
          <template slot-scope="scope">
            <div>
              {{ scope.row.examineTm ? scope.row.examineTm : "-" }}
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 纳蓄 -->
      <el-table v-if="
          sgzgMapType === '纳蓄' ||
          sgzgMapType === '蓄水率' ||
          sgzgMapType === '蓄水量'
        " v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="adcdctiy" label="所在市" align="center"></el-table-column>
        <el-table-column prop="adcdcountry" label="所在县" align="center"></el-table-column>
        <el-table-column prop="NAME" label="名称" align="center"></el-table-column>
        <el-table-column prop="scale" label="规模" align="center"></el-table-column>
        <el-table-column prop="TATOL_STORAGE" label="总库容（万方）" align="center"></el-table-column>
        <el-table-column prop="CSCNWL" label="正常库容（万方）" align="center"></el-table-column>
        <el-table-column label="当前库容（万方）" align="center">
          <template slot-scope="scope">
            <div>
              {{ scope.row.currenty > 0 ? scope.row.currenty : 0 }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="可纳库容(万方)/雨量(mm)" align="center">
          <el-table-column label="至防洪高水位" align="center">
            <template slot-scope="scope">
              <div>
                {{ scope.row.fsrCapable > 0 ? scope.row.fsrCapable : 0 }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="至防洪高雨量" align="center">
            <template slot-scope="scope">
              <div>
                {{ scope.row.fsrRainfall > 0 ? scope.row.fsrRainfall : 0 }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="至正常水位" align="center">
            <template slot-scope="scope">
              <div>
                {{ scope.row.cscCapable > 0 ? scope.row.cscCapable : 0 }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="至正常雨量" align="center">
            <template slot-scope="scope">
              <div>
                {{ scope.row.cscRainfall > 0 ? scope.row.cscRainfall : 0 }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="至汛限水位" align="center">
            <template slot-scope="scope">
              <div>
                {{ scope.row.limitCapable > 0 ? scope.row.limitCapable : 0 }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="至汛限雨量" align="center">
            <template slot-scope="scope">
              <div>
                {{ scope.row.limitRainfall > 0 ? scope.row.limitRainfall : 0 }}
              </div>
            </template>
          </el-table-column>
        </el-table-column>
        <!-- <el-table-column prop="capable" label="蓄水量（万方）" align="center"></el-table-column>
        <el-table-column prop="storagePerc" label="蓄水率（%）" align="center"></el-table-column>
        <el-table-column prop="DDCP" label="死库容（万方）" align="center"></el-table-column> -->
      </el-table>

      <!-- 防洪-泄洪 -->
      <el-table v-if="sgzgMapType === '防洪' && numberSelected == '泄洪'" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="basin" label="所在水系" align="center"></el-table-column>
        <el-table-column prop="cityadnm" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryadnm" label="所在县" align="center"></el-table-column>
        <el-table-column prop="prnm" label="水库名称" align="center"></el-table-column>
        <el-table-column label="工程规模" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.prlv === 'D6F910F800B7438FAE33F4E5EB080ADA'">大（I）型</span>
            <span v-if="scope.row.prlv === 'F5415448206A4E8595451AB8FAC4771E'">大（II）型</span>
            <span v-if="scope.row.prlv === 'C7EBDBA0437E4EF6B390DF660FA80158'">中型</span>
          </template>
        </el-table-column>
        <el-table-column prop="dispatchtime" min-width="150" label="时间" align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.dispatchtime
                ? scope.row.dispatchtime.slice(0, 13) + "时"
                : "-"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="gateflow" label="闸门泄洪（m³/s）" align="center"></el-table-column>
        <el-table-column prop="overflow" label="溢流（m³/s）" align="center"></el-table-column>
        <el-table-column prop="electricityflow" label="发电流量（m³/s）" align="center"></el-table-column>
        <el-table-column prop="watersupplyflow" label="供水流量（m³/s）" align="center"></el-table-column>
        <el-table-column prop="ecoflow" label="生态流量（m³/s）" align="center"></el-table-column>
        <el-table-column prop="totalflow" show-overflow-tooltip label="合计出库流量（m³/s）" align="center"></el-table-column>
        <el-table-column prop="inputflow" label="入库流量（m³/s）" align="center"></el-table-column>
        <el-table-column prop="remark" show-overflow-tooltip label="调度事项记录" align="center"></el-table-column>
      </el-table>

      <!-- 防洪-溢洪 -->
      <el-table v-if="sgzgMapType === '防洪' && numberSelected == '溢洪'" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="cityADNM" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryADNM" label="所在县" align="center"></el-table-column>
        <el-table-column prop="NAME" label="水库名称" align="center"></el-table-column>
        <el-table-column prop="scale" label="规模" align="center"></el-table-column>
        <el-table-column label="开始时间" align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.lttm ? scope.row.lttm.slice(0, 16) : "-"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="wtele" label="溢洪道堰顶高程(m)" align="center"></el-table-column>
        <el-table-column prop="waterLevel" label="当前水位(m)" align="center"></el-table-column>
        <el-table-column label="当前时间" align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.waterLevelTM ? scope.row.waterLevelTM.slice(0, 16) : "-"
            }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="ltHours" label="持续时间(h)" align="center"></el-table-column>
      </el-table>

      <!-- 拦洪座次 -->
      <el-table v-if="sgzgMapType === '拦洪座次'" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="cityADNM" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryADNM" label="所在县" align="center"></el-table-column>
        <el-table-column prop="NAME" label="水库名称" align="center"></el-table-column>
        <el-table-column prop="project_scale_str" label="工程规模" align="center"></el-table-column>
        <el-table-column prop="feature" label="水库功能" align="center"></el-table-column>
        <el-table-column prop="count" label="本年度拦洪次数" align="center"></el-table-column>
        <el-table-column prop="difflow" label="拦洪水量(万m³)" align="center"></el-table-column>
      </el-table>

      <!-- 灌溉 -->
      <el-table v-if="
          sgzgMapType === '灌溉' ||
          sgzgMapType === '灌溉面积' ||
          sgzgMapType === '灌溉水量'
        " v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="cityADNM" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryADNM" label="所在县" align="center"></el-table-column>
        <el-table-column prop="name" label="工程名称" align="center"></el-table-column>
        <el-table-column label="工程规模" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.project_scale == '1'">大(1)型</span>
            <span v-if="scope.row.project_scale == '2'">大(2)型</span>
            <span v-if="scope.row.project_scale == '3'">中型</span>
            <span v-if="scope.row.project_scale == '4'">小(1)型</span>
            <span v-if="scope.row.project_scale == '5'">小(2)型</span>
          </template>
        </el-table-column>
        <el-table-column label="水库功能" align="center">
          <template slot-scope="scope">
            <span>{{
              sgzgMapType === "灌溉水量"
                ? scope.row.feature
                : scope.row.ACTFUNCMC
            }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="
            sgzgMapType === '灌溉水量'
              ? '本年度灌溉水量（万方）'
              : '灌溉面积（万亩）'
          " align="center">
          <template slot-scope="scope">
            <span>{{
              sgzgMapType === "灌溉水量"
                ? scope.row.supply_total
                : scope.row.DIA
            }}</span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 生态 -->
      <el-table v-if="sgzgMapType === '生态' || sgzgMapType === '风景区'" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="cityADNM" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryADNM" label="所在县" align="center"></el-table-column>
        <el-table-column prop="name" label="工程名称" align="center"></el-table-column>
        <el-table-column label="工程规模" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.project_scale == '1'">大(1)型</span>
            <span v-if="scope.row.project_scale == '2'">大(2)型</span>
            <span v-if="scope.row.project_scale == '3'">中型</span>
            <span v-if="scope.row.project_scale == '4'">小(1)型</span>
            <span v-if="scope.row.project_scale == '5'">小(2)型</span>
          </template>
        </el-table-column>
        <el-table-column label="水库功能" align="center">
          <template slot-scope="scope">
            <span>{{
              sgzgMapType === "风景区" ? scope.row.feature : scope.row.ACTFUNCMC
            }}</span>
          </template>
        </el-table-column>
      </el-table>
      <!-- 发电 -->
      <el-table v-if="
          sgzgMapType === '发电' ||
          sgzgMapType === '装机容量' ||
          sgzgMapType === '当年发电量'
        " v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="cityADNM" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryADNM" label="所在县" align="center"></el-table-column>
        <el-table-column prop="name" label="工程名称" align="center"></el-table-column>
        <el-table-column label="工程规模" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.project_scale == '1'">大(1)型</span>
            <span v-if="scope.row.project_scale == '2'">大(2)型</span>
            <span v-if="scope.row.project_scale == '3'">中型</span>
            <span v-if="scope.row.project_scale == '4'">小(1)型</span>
            <span v-if="scope.row.project_scale == '5'">小(2)型</span>
          </template>
        </el-table-column>
        <el-table-column label="水库功能" align="center">
          <template slot-scope="scope">
            <span>{{
              sgzgMapType === "当年发电量"
                ? scope.row.feature
                : scope.row.ACTFUNCMC
            }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="
            sgzgMapType === '当年发电量'
              ? '本年度发电量(千瓦时)'
              : '装机容量(Kw)'
          " align="center">
          <template slot-scope="scope">
            <span>{{
              sgzgMapType === "当年发电量"
                ? scope.row.generating_capacity
                : scope.row.TOTAL_INSTALLED_CAPACITYH
            }}</span>
          </template>
        </el-table-column>
      </el-table>
      <!-- 供水 -->
      <el-table v-if="
          sgzgMapType === '供水' ||
          sgzgMapType === '可供水量' ||
          sgzgMapType === '供水水量'
        " v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%" @row-click="clickProject">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="cityADNM" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryADNM" label="所在县" align="center"></el-table-column>
        <el-table-column prop="name" label="工程名称" align="center"></el-table-column>
        <el-table-column label="工程规模" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.project_scale == '1'">大(1)型</span>
            <span v-if="scope.row.project_scale == '2'">大(2)型</span>
            <span v-if="scope.row.project_scale == '3'">中型</span>
            <span v-if="scope.row.project_scale == '4'">小(1)型</span>
            <span v-if="scope.row.project_scale == '5'">小(2)型</span>
          </template>
        </el-table-column>
        <el-table-column label="水库功能" align="center">
          <template slot-scope="scope">
            <span>{{
              sgzgMapType === "供水水量"
                ? scope.row.feature
                : scope.row.ACTFUNCMC
            }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="
            sgzgMapType === '供水水量'
              ? '本年度供水水量（万方）'
              : '可供水量（万方）'
          " align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.supply_total ? scope.row.supply_total : "-"
            }}</span>
          </template>
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
  generateProject,
  floodListJson,
  safeAppraisalApi,
  cyjgNumApi,
  scenicSpotApi,
  floodExportListApi,
  floodListYihong,
} from "@/api/api_reservoir";
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
      default: false
    }
  },
  data() {
    return {
      adcdList: [],
      scaleList: [
        { scale: "大(1)型", value: 1 },
        { scale: "大(2)型", value: 2 },
        { scale: "中型", value: 3 },
        { scale: "小(1)型", value: 4 },
        { scale: "小(2)型", value: 5 },
      ],
      scaleSelect: "",
      codeList: [
        { scale: "红码", value: 19 },
        { scale: "黄码", value: 18 },
        { scale: "绿码", value: 17 },
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
    };
  },
  computed: {
    gpType() {
      switch (this.sgzgMapType) {
        case "风险研判":
          return 1;
          break;
        case "除险加固":
          return 3;
          break;
        case "限制运用":
        case "二类坝":
        case "三类坝":
          return 4;
          break;
        case "问题处置":
          return 5;
          break;
        case "纳蓄":
        case "蓄水率":
        case "蓄水量":
          return 6;
          break;
        case "供水":
        case "可供水量":
        case "供水水量":
        case "灌溉":
        case "灌溉面积":
        case "灌溉水量":
        case "生态":
        case "发电":
        case "装机容量":
        case "当年发电量":
          return 8;
          break;
        case "存量清零":
          return 9;
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
        case "纳蓄":
        case "蓄水率":
        case "蓄水量":
        case "供水":
        case "可供水量":
        case "供水水量":
        case "灌溉":
        case "灌溉面积":
        case "灌溉水量":
        case "发电":
        case "装机容量":
        case "当年发电量":
        case "生态":
        case "风景区":
        case "拦洪座次":
        case "防洪":
          this.checkedArr.forEach((element) => {
            switch (element) {
              case "大(1)型":
                tempArr.push(1);
                break;
              case "大(2)型":
                tempArr.push(2);
                break;
              case "中型":
                tempArr.push(3);
                break;
              case "小(1)型":
                tempArr.push(4);
                break;
              case "小(2)型":
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
              case "三类坝":
                tempArr.push("三类坝");
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
        case "三类坝":
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
        case "除险加固":
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
              case "三类坝":
                tempArr.push(10);
                break;
            }
          });
          return tempArr.join(",");
          break;
        case "存量清零":
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
              this.codeSelect = 19;
              tempArr.push(19);
              break;
            case "green":
              this.codeSelect = 17;
              tempArr.push(17);
              break;
            case "yellow":
              this.codeSelect = 18;
              tempArr.push(18);
              break;
            default:
              this.codeSelect = "";
              tempArr = [17, 18, 19];
              break;
          }
          // this.workStatus.forEach((element) => {
          //   switch (element) {
          //     // 17.绿色,18.黄色，19.红色
          //     case '红码':
          //       tempArr.push(19)
          //       break;
          //     case '黄码':
          //       tempArr.push(18)
          //       break;
          //     case '绿码':
          //       tempArr.push(17)
          //       break;
          //   }
          // })

          // this.workStatus.length > 1 ? this.codeSelect = '' : this.codeSelect = tempArr[0]
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
              case "三类坝":
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
        case "三类坝":
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
  },
  watch: {
    codeSelect: {
      handler(newVal, oldVal) {
        this.codeReasonSelect = "";
        if (newVal === 19) {
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
        } else if (newVal == 18) {
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
        return newVal;
      },
      deep: true,
      immediate: true,
    },
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
      let fdOpt = {
        adcd: this.selectAdcd,
        basin: this.sgzgSelectBasin,
        queryStr: this.inputName,
        year: new Date().getFullYear(),
        fetchAll: true,
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
          opt.extractPram = this.codeReasonSelect;
          opt.screen = this.screenType;
          break;
        case "纳蓄":
        case "蓄水率":
        case "蓄水量":
          opt.projectScale = this.projectType;
          switch (this.rain) {
            case "50,100":
            case "100,200":
              opt.rainScope = this.rain;
              break;
            case "50,1":
              opt.ifUnder = 1;
              opt.rain = 50;
              break;
            case "200,2":
              opt.ifUnder = 2;
              opt.rain = 200;
              break;
            default:
              break;
          }
          break;
        case "限制运用":
          opt.projectScale = this.xzScale;
          opt.screen = this.projectType;
          opt.dam = this.screenType;
          break;
        case "二类坝":
          opt.screen = this.projectType;
          opt.dam = 2;
          opt.extractPram = "1";
          break;
        case "三类坝":
          opt.screen = this.projectType;
          opt.dam = 3;
          opt.extractPram = "1";
          break;
        case "防洪":
          fdOpt.projectScale = this.projectType;
          break;
        case "安全鉴定":
          safeOpt.dam = this.projectType;
          break;
        case "除险加固":
          opt.screen = this.projectType;
          // opt.extractPram = this.sgzgCxjgCl
          break;
        case "存量清零":
          safeOpt.extractPram = this.projectType;
          break;
        case "问题处置":
          opt.screen = this.projectType;
          opt.extractPram = this.screenType;
          break;
        case "供水":
        case "可供水量":
          opt.extractPram = "供水";
          opt.gpType = this.gpType;
          opt.projectScale = this.projectType;
          break;
        case "供水水量":
          opt.extractPram = "供水";
          opt.gpType = this.gpType;
          opt.projectScale = this.projectType;
          opt.interType = 2;
          break;
        case "灌溉":
        case "灌溉面积":
          opt.extractPram = "灌溉";
          opt.gpType = this.gpType;
          opt.projectScale = this.projectType;
          break;
        case "灌溉水量":
          opt.extractPram = "灌溉";
          opt.gpType = this.gpType;
          opt.projectScale = this.projectType;
          opt.interType = 2;
          break;
        case "生态":
          opt.extractPram = "生态";
          opt.gpType = this.gpType;
          opt.projectScale = this.projectType;
          break;
        case "发电":
        case "装机容量":
          opt.extractPram = "发电";
          opt.gpType = this.gpType;
          opt.projectScale = this.projectType;
          break;
        case "当年发电量":
          opt.extractPram = "发电";
          opt.gpType = this.gpType;
          opt.projectScale = this.projectType;
          opt.interType = 2;
          break;
        default:
          break;
      }
      let res = {};
      if (this.sgzgMapType === "安全鉴定") {
        res = await safeAppraisalApi(safeOpt);
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
      if (this.sgzgMapType === "防洪") {
        if (this.numberSelected && this.numberSelected == "溢洪") {
          res = await floodListYihong({
            adcd: this.selectAdcd,
            basinCode: this.sgzgSelectBasin,
            projectScale: this.projectType,
            name: this.inputName,
          });
          if (res.success) {
            this.loading = false;
            this.siteList = res.rows;
          }
          return;
        }
        res = await floodListJson(fdOpt);
        if (res.success) {
          this.loading = false;
          this.siteList = res.rows;
        }

        return;
      }
      if (this.sgzgMapType === "存量清零") {
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
      if (this.sgzgMapType === "风景区") {
        res = await scenicSpotApi({
          adcd: this.selectAdcd,
          basinCode: this.sgzgSelectBasin,
          projectScale: this.projectType,
          name: this.inputName,
          fetchAll: true,
        });
        if (res.success) {
          this.loading = false;
          this.siteList = res.rows;
        }
        return;
      }
      if (this.sgzgMapType === "拦洪座次") {
        res = await floodExportListApi({
          adcd: this.selectAdcd,
          basinCode: this.sgzgSelectBasin,
          projectScale: this.projectType,
          name: this.inputName,
        });
        if (res.success) {
          this.loading = false;
          this.siteList = res.rows;
        }
        return;
      }
      res = await generateProject(opt);
      if (res.success) {
        this.loading = false;
        if (this.sgzgMapType === "除险加固") {
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
        } else {
          this.siteList = res.data;
        }
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
      let fdOpt = {
        adcd: this.selectAdcd,
        year: new Date().getFullYear(),
        fetchAll: true,
        basin: this.sgzgSelectBasin,
        queryStr: this.inputName,
      };
      let safeOpt = {
        adcd: this.selectAdcd,
        basinCode: this.sgzgSelectBasin,
        name: this.inputName,
        interType: 2,
      };
      let clqlOpt = {
        adcd: this.selectAdcd,
        basinCode: this.sgzgSelectBasin,
        name: this.inputName,
        interType: 2,
      };
      switch (this.sgzgMapType) {
        case "风险研判":
          queryOpt.projectScale = this.scaleSelect;
          queryOpt.screen = this.codeSelect;
          queryOpt.extractPram = this.codeReasonSelect;
          break;
        case "纳蓄":
        case "蓄水率":
        case "蓄水量":
          queryOpt.projectScale = this.scaleSelect;
          switch (this.rain) {
            case "50,100":
            case "100,200":
              queryOpt.rainScope = this.rain;
              break;
            case "50,1":
              queryOpt.ifUnder = 1;
              queryOpt.rain = 50;
              break;
            case "200,2":
              queryOpt.ifUnder = 2;
              queryOpt.rain = 200;
              break;
            default:
              break;
          }
          break;
        case "供水":
        case "可供水量":
          queryOpt.extractPram = "供水";
          queryOpt.gpType = this.gpType;
          queryOpt.projectScale = this.scaleSelect;
          break;
        case "供水水量":
          queryOpt.extractPram = "供水";
          queryOpt.gpType = this.gpType;
          queryOpt.projectScale = this.scaleSelect;
          queryOpt.interType = 2;
          break;
        case "灌溉":
        case "灌溉面积":
          queryOpt.extractPram = "灌溉";
          queryOpt.gpType = this.gpType;
          queryOpt.projectScale = this.scaleSelect;
          break;
        case "灌溉水量":
          queryOpt.extractPram = "灌溉";
          queryOpt.gpType = this.gpType;
          queryOpt.projectScale = this.scaleSelect;
          queryOpt.interType = 2;
          break;
        case "生态":
          queryOpt.extractPram = "生态";
          queryOpt.gpType = this.gpType;
          queryOpt.projectScale = this.scaleSelect;
          break;
        case "发电":
        case "装机容量":
          queryOpt.extractPram = "发电";
          queryOpt.gpType = this.gpType;
          queryOpt.projectScale = this.scaleSelect;
          break;
        case "当年发电量":
          queryOpt.extractPram = "发电";
          queryOpt.gpType = this.gpType;
          queryOpt.projectScale = this.projectType;
          queryOpt.interType = 2;
        case "限制运用":
          queryOpt.projectScale = this.scaleSelect;
          queryOpt.screen = this.projectType;
          queryOpt.dam = this.screenType;
          break;
        case "二类坝":
          queryOpt.projectScale = this.scaleSelect;
          queryOpt.screen = this.projectType;
          queryOpt.dam = this.screenType;
          queryOpt.extractPram = "1";
          break;
        case "三类坝":
          queryOpt.projectScale = this.scaleSelect;
          queryOpt.screen = this.projectType;
          queryOpt.dam = this.screenType;
          queryOpt.extractPram = "1";
          break;
        case "防洪":
          fdOpt.projectScale = this.scaleSelect;
          break;
        case "安全鉴定":
          safeOpt.projectScale = this.scaleSelect;
          safeOpt.dam = this.projectType;
          break;
        case "除险加固":
          queryOpt.projectScale = this.scaleSelect;
          queryOpt.screen = this.projectType;
          break;
        case "存量清零":
          safeOpt.projectScale = this.scaleSelect;
          safeOpt.extractPram = this.projectType;
          break;
        case "问题处置":
          queryOpt.projectScale = this.scaleSelect;
          queryOpt.screen = this.projectType;
          opt.extractPram = this.screenType;
          break;
        default:
          break;
      }
      let res = {};
      if (this.sgzgMapType === "安全鉴定") {
        res = await safeAppraisalApi(safeOpt);
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
      if (this.sgzgMapType === "防洪") {
        if (this.numberSelected && this.numberSelected == "溢洪") {
          res = await floodListYihong({
            adcd: this.selectAdcd,
            basinCode: this.sgzgSelectBasin,
            projectScale: this.scaleSelect,
            name: this.inputName,
          });
          if (res.success) {
            this.loading = false;
            this.siteList = res.rows;
          }
          return;
        }
        res = await floodListJson(fdOpt);
        if (res.success) {
          this.loading = false;
          this.siteList = res.rows;
        }
        return;
      }
      if (this.sgzgMapType === "存量清零") {
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
      if (this.sgzgMapType === "风景区") {
        res = await scenicSpotApi({
          adcd: this.selectAdcd,
          basinCode: this.sgzgSelectBasin,
          projectScale: this.scaleSelect,
          name: this.inputName,
          fetchAll: true,
        });
        if (res.success) {
          this.loading = false;
          this.siteList = res.rows;
        }
        return;
      }
      if (this.sgzgMapType === "拦洪座次") {
        res = await floodExportListApi({
          adcd: this.selectAdcd,
          basinCode: this.sgzgSelectBasin,
          projectScale: this.scaleSelect,
          name: this.inputName,
        });
        if (res.success) {
          this.loading = false;
          this.siteList = res.rows;
        }
        return;
      }
      res = await generateProject(queryOpt);
      if (res.success) {
        this.loading = false;
        if (this.sgzgMapType === "除险加固") {
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
        } else {
          this.siteList = res.data;
        }
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
        case "除险加固":
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
        case "存量清零":
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
        case "三类坝":
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
        case "防洪":
          if (this.numberSelected && this.numberSelected == "溢洪") {
            window.open(
              this.$config.host +
              `/mgt/bm/ctroChcuDif/toExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basin=${this.sgzgSelectBasin}` +
              `&projectScale=${this.projectType}` +
              `&token=${token}`
            );
            return;
          }
          window.open(
            this.$config.host +
            `/mgt/temp/dispatchrecord/toExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.projectType}` +
            `&year=2022` +
            `&fetchAll=true` +
            `&token=${token}`
          );
          break;
        case "拦洪座次":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.projectType}` +
            `&gpType=11` +
            `&token=${token}`
          );
          break;
        case "供水":
        case "可供水量":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.projectType}` +
            `&extractPram=供水` +
            `&gpType=${this.gpType}` +
            `&token=${token}`
          );
          break;
        case "供水水量":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.projectType}` +
            `&extractPram=供水` +
            `&gpType=10` +
            `&interType=2` +
            `&token=${token}`
          );
          break;
        case "灌溉":
        case "灌溉面积":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.projectType}` +
            `&extractPram=灌溉` +
            `&gpType=${this.gpType}` +
            `&token=${token}`
          );
          break;
        case "灌溉水量":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.projectType}` +
            `&extractPram=灌溉` +
            `&gpType=10` +
            `&interType=2` +
            `&token=${token}`
          );
          break;
        case "生态":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.projectType}` +
            `&extractPram=生态` +
            `&gpType=${this.gpType}` +
            `&token=${token}`
          );
          break;
        case "风景区":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.projectType}` +
            `&extractPram=生态` +
            `&gpType=10` +
            `&token=${token}`
          );
          break;
        case "发电":
        case "装机容量":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.projectType}` +
            `&extractPram=发电` +
            `&gpType=${this.gpType}` +
            `&token=${token}`
          );
          break;
        case "当年发电量":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.projectType}` +
            `&extractPram=发电` +
            `&gpType=10` +
            `&interType=2` +
            `&token=${token}`
          );
          break;
        case "纳蓄":
        case "蓄水率":
        case "蓄水量":
          switch (this.rain) {
            case "50,1":
              window.open(
                this.$config.host +
                `/mgt/bm/reservoirWT/generateProjectToExcel` +
                `?adcd=${this.selectAdcd}` +
                `&basinCode=${this.sgzgSelectBasin}` +
                `&projectScale=${this.projectType}` +
                `&rain=50` +
                `&ifUnder=1` +
                `&gpType=${this.gpType}` +
                `&token=${token}`
              );
              break;
            case "200,2":
              window.open(
                this.$config.host +
                `/mgt/bm/reservoirWT/generateProjectToExcel` +
                `?adcd=${this.selectAdcd}` +
                `&basinCode=${this.sgzgSelectBasin}` +
                `&projectScale=${this.projectType}` +
                `&rain=200` +
                `&ifUnder=2` +
                `&gpType=${this.gpType}` +
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
                `&rainScope=${this.rain}` +
                `&gpType=${this.gpType}` +
                `&token=${token}`
              );
              break;
          }
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
      console.log("条件导出");
      let token = this.$localData("get", "token");
      switch (this.sgzgMapType) {
        case "风险研判":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basinCode=${this.sgzgSelectBasin}` +
            `&projectScale=${this.scaleSelect}` +
            `&screen=${this.codeSelect}` +
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
        case "除险加固":
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
        case "存量清零":
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
        case "三类坝":
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
        case "防洪":
          if (this.numberSelected && this.numberSelected == "溢洪") {
            window.open(
              this.$config.host +
              `/mgt/bm/ctroChcuDif/toExcel` +
              `?adcd=${this.selectAdcd}` +
              `&basin=${this.sgzgSelectBasin}` +
              `&projectScale=${this.scaleSelect}` +
              `&token=${token}`
            );
            return;
          }
          window.open(
            this.$config.host +
            `/mgt/temp/dispatchrecord/toExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.scaleSelect}` +
            `&year=2022` +
            `&fetchAll=true` +
            `&token=${token}`
          );
          break;
        case "拦洪座次":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.scaleSelect}` +
            `&gpType=11` +
            `&token=${token}`
          );
          break;
        case "供水":
        case "可供水量":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.scaleSelect}` +
            `&extractPram=供水` +
            `&gpType=${this.gpType}` +
            `&token=${token}`
          );
          break;
        case "供水水量":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.scaleSelect}` +
            `&extractPram=供水` +
            `&gpType=10` +
            `&interType=2` +
            `&token=${token}`
          );
          break;
        case "灌溉":
        case "灌溉面积":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.scaleSelect}` +
            `&extractPram=灌溉` +
            `&gpType=${this.gpType}` +
            `&token=${token}`
          );
          break;
        case "灌溉水量":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.scaleSelect}` +
            `&extractPram=灌溉` +
            `&gpType=10` +
            `&interType=2` +
            `&token=${token}`
          );
          break;
        case "生态":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.scaleSelect}` +
            `&extractPram=生态` +
            `&gpType=${this.gpType}` +
            `&token=${token}`
          );
          break;
        case "风景区":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.scaleSelect}` +
            `&extractPram=生态` +
            `&gpType=10` +
            `&token=${token}`
          );
          break;
        case "发电":
        case "装机容量":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.scaleSelect}` +
            `&extractPram=发电` +
            `&gpType=${this.gpType}` +
            `&token=${token}`
          );
          break;
        case "当年发电量":
          window.open(
            this.$config.host +
            `/mgt/bm/reservoirWT/generateProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&basin=${this.sgzgSelectBasin}` +
            `&projectScale=${this.scaleSelect}` +
            `&extractPram=发电` +
            `&gpType=10` +
            `&interType=2` +
            `&token=${token}`
          );
          break;
        case "纳蓄":
        case "蓄水率":
        case "蓄水量":
          switch (this.rain) {
            case "50,1":
              window.open(
                this.$config.host +
                `/mgt/bm/reservoirWT/generateProjectToExcel` +
                `?adcd=${this.selectAdcd}` +
                `&basinCode=${this.sgzgSelectBasin}` +
                `&projectScale=${this.scaleSelect}` +
                `&rain=50` +
                `&ifUnder=1` +
                `&gpType=${this.gpType}` +
                `&token=${token}`
              );
              break;
            case "200,2":
              window.open(
                this.$config.host +
                `/mgt/bm/reservoirWT/generateProjectToExcel` +
                `?adcd=${this.selectAdcd}` +
                `&basinCode=${this.sgzgSelectBasin}` +
                `&projectScale=${this.scaleSelect}` +
                `&rain=200` +
                `&ifUnder=2` +
                `&gpType=${this.gpType}` +
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
                `&rainScope=${this.rain}` +
                `&gpType=${this.gpType}` +
                `&token=${token}`
              );
              break;
          }
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
      this.popupData.NAME = row.NAME || row.name;
      this.popupData.PRCD = row.PRCD || row.prcd;
      this.showProjectPopup = true;
    },
    closeProjectPopup() {
      this.showProjectPopup = false;
    },
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
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
