<template>
  <div class="primary-dialog">
    <el-dialog
      top="15vh"
      width="1200px"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      :modal-append-to-body="true"
      :close-on-click-modal="false"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">{{ code == '水闸' ? '温瑞平水闸' : code}}</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
      <!-- 头部统计区域 -->
        <!-- 水利设施保险 -- 开始 -->
        <!-- <div class="statistics-sty"  v-if="mediumSized == '防御复盘' && code == '水利设施保险'"> -->
          <!-- 理赔合同 -->
          <!-- <div>
            <p><i class="icon-li-pei-he-tong"></i></p>
            <div>
              <p>理赔合同</p>
              <p><span>{{ statisticsData.total }} </span> 个</p>
            </div>
          </div> -->
          <!-- 理赔金额 -->
          <!-- <div>
            <p><i class="icon-li-pei-jin-e"></i></p>
            <div>
              <p>理赔金额</p>
              <p><span>{{ statisticsData.overLimitNum }} </span> 个</p>
            </div>
          </div> -->
          <!-- 江堤合同 -->
          <!-- <div>
            <p><i class="icon-jiang-di-li-pei"></i></p>
            <div>
              <p>江堤合同</p>
              <p><span>{{ statisticsData.avgStorageRatio }} </span> 个</p>
            </div>
          </div> -->
          <!-- 海堤单位 -->
          <!-- <div>
            <p><i class="icon-jiang-di-li-pei"></i></p>
            <div>
              <p>海堤单位</p>
              <p><span>{{ statisticsData.avgLimitStorageAbility }} </span> 个</p>
            </div>
          </div> -->
        <!-- </div> -->
        <!-- 水利设施保险 -- 结束 -->
        <!-- 水库分析 -- 开始 -->
        <div class="statistics-sty"  v-if="mediumSized == '防御复盘' && code == '水库分析'">
          <!-- 累计拦截蓄水量 -->
          <div>
            <p><i class="icon-lan-xushui-liang"></i></p>
            <div>
              <p>累计拦截蓄水量</p>
              <p><span>{{ reservoirAnalysisData.totalStorage }} </span> 万m³</p>
            </div>
          </div>
          <!-- 累计泄洪量 -->
          <div>
            <p><i class="icon-shuizha-paihong"></i></p>
            <div>
              <p>累计泄洪量</p>
              <p><span>{{ reservoirAnalysisData.totalDrainage }} </span> 万m³</p>
            </div>
          </div>
        </div>
        <!-- 水库分析 -- 结束 -->
        <!-- 水库统计 -->
        <div class="statistics-sty" v-if="code == '水库'">
          <!-- 数量统计 -->
          <div>
            <p><i class="icon-shu-liang-tong-ji"></i></p>
            <div>
              <p>数量统计</p>
              <p><span>{{ statisticsData.total }} </span> 座</p>
            </div>
          </div>
          <!-- 超汛限统计 -->
          <div>
            <p><i class="icon-chao-xunxian"></i></p>
            <div>
              <p>{{projectScaleValue == '大中型' ? '超汛限统计':'溢洪统计'}}</p>
              <p><span>{{ statisticsData.overLimitNum }} </span> 座</p>
            </div>
          </div>
          <!-- 平均蓄水率 -->
          <div>
            <p><i class="icon-xu-shui-lv"></i></p>
            <div>
              <p>平均蓄水率</p>
              <p><span>{{ statisticsData.avgStorageRatio }} </span> %</p>
            </div>
          </div>
          <!-- 至汛限平均纳蓄能力 -->
          <div v-if="projectScaleValue == '大中型'">
            <p><i class="icon-na-xu-nengli"></i></p>
            <div>
              <p>至汛限平均纳蓄能力</p>
              <p><span>{{ statisticsData.avgLimitStorageAbility }} </span> mm</p>
            </div>
          </div>
        </div>
        <!-- 水闸统计 -->
        <div class="statistics-sty" v-if="code == '水闸'">
          <!-- 数量统计 -->
          <div>
            <p><i class="icon-shuizha-shuliang-tongji"></i></p>
            <div>
              <p>数量统计</p>
              <p><span> {{ reservoirStatistical.totalNum }} </span> 个</p>
            </div>
          </div>
          <!-- 闸门开启数量统计 -->
          <div :class="count % 2 ? 'gates-open-sty' : ''" style="padding: 10px;" @click="openGates">
            <p><i class="icon-shuizha-zhamen-kaiqi-shuliang"></i></p>
            <div>
              <p>闸门开启数量</p>
              <p><span>{{ reservoirStatistical.openGateWagaNum }} </span> 个</p>
            </div>
          </div>
          <!-- 水闸泄洪量 -->
          <div class="water-gate-sty">
            <div class="area-style">
              <p>
                <i class="el-icon-time icon-sty"></i><span>累计:</span>
                <el-input-number
                  v-model="numTime"
                  size="small"
                  controls-position="right"
                  :min="1"
                  :max="72"
                  @input="changeHours"
                />
                <span style="margin-left:10px;font-size: 16px;">小时</span>
              </p>
              <div>
                泄洪总量 <span>{{ reservoirStatistical.drainageTotal }} </span> 万m³
              </div>
            </div>
          </div>
        </div>
        <!-- 河网统计 -->
        <div class="statistics-sty" v-if="code == '河网'">
          <!-- 中型水闸数量统计 -->
          <div>
            <p><i class="icon-hewang-shuliang-tongji"></i></p>
            <div>
              <p>数量统计</p>
              <p><span> {{ riverwayList.total }} </span> 个</p>
            </div>
          </div>
          <!-- 小型水闸数量统计 -->
          <div>
            <p><i class="icon-chao-xunxian"></i></p>
            <div>
              <p>超警戒统计</p>
              <p><span>{{ riverwayList.warn }} </span> 个</p>
            </div>
          </div>
        </div>
        <!-- 潮位统计 -->
        <div class="statistics-sty" v-if="code == '潮位'">
          <!-- 中型水闸数量统计 -->
          <div>
            <p><i class="icon-chaowei-tongji"></i></p>
            <div>
              <p>数量统计</p>
              <p><span> {{ seaLevelList.totalNum == null ? '-' : seaLevelList.totalNum }} </span> 个</p>
            </div>
          </div>
          <!-- 小型水闸数量统计 -->
          <div>
            <p><i class="icon-chao-xunxian"></i></p>
            <div>
              <p>超警戒统计</p>
              <p><span>{{ seaLevelList.overWarnNum == null ? '-' : seaLevelList.overWarnNum }} </span> 个</p>
            </div>
          </div>
        </div>
      <!-- 条件查询区域 -->
        <div class="query-sty">
          <!-- 风险隐患查询条件 开始 -->
          <el-row>
            <el-col :span="5" class="aa" v-if="code == '风险隐患'">
              <div class="demo-input-suffix">
                <p style="width: 90px;margin-right: 9px;">工程名称:</p>
                <el-input placeholder="工程名称" v-model="projectNameVal" class="myClass" clearable @change="selectVal" />
              </div>
            </el-col>
            <el-col :span="5" class="aa" v-if="code == '风险隐患'">
              <div class="demo-input-suffix">
                <p style="width: 90px">风险类型:</p>
                <el-select
                  v-model="riskTypeVal"
                  size="small"
                  clearable
                  @change="selectVal"
                  placeholder="全部"
                >
                  <el-option
                    v-for="item in riskTypeList"
                    :key="item.name"
                    :label="item.name"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="5" class="aa" v-if="code == '风险隐患'">
              <div class="demo-input-suffix">
                <p style="width: 90px">整改情况:</p>
                <el-select
                  v-model="reorganizeSituation"
                  size="small"
                  clearable
                  @change="selectVal"
                  placeholder="全部"
                >
                  <el-option
                    v-for="item in reorganizeSituationList"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="5" class="aa" v-if="code == '风险隐患'" style="padding-right: 0;">
              <div class="demo-input-suffix">
                <p style="width: 90px">发现渠道:</p>
                <el-select
                  v-model="discoveryChannel"
                  size="small"
                  clearable
                  @change="selectVal"
                  placeholder="全部"
                >
                  <el-option
                    v-for="item in discoveryChannelList"
                    :key="item.name"
                    :label="item.name"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
          </el-row>
          <el-row style="margin-top: 15px;">
            <el-col :span="5" class="aa" v-if="code == '风险隐患'">
              <div class="demo-input-suffix">
                <p style="width: 90px">工程类型:</p>
                <el-cascader v-model="projectTypeVal" :options="projectTypeList" clearable @change="selectVal" />
              </div>
            </el-col>
            <el-col :span="5" class="aa" v-if="code == '风险隐患'">
              <div class="demo-input-suffix">
                <p style="width: 90px;">行政区划:</p>
                <el-cascader v-model="district" clearable :options="districtList" @change="selectVal" />
              </div>
            </el-col>
             <el-col :span="8" class="aa" v-if="code == '风险隐患'" style="margin-top:3px;">
              <div class="demo-input-suffix">
                <p style="width: 80px;">时间范围:</p>
                  <el-date-picker
                    v-model="affiliatedUnit"
                    type="daterange"
                    align="right"
                    unlink-panels
                    value-format="yyyy-MM-dd"
                    range-separator="至"
                    @change="selectVal"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期">
                  </el-date-picker>
              </div>
            </el-col>
            <div class="derive-sty" v-if="code == '风险隐患'">导出Excel</div>
          </el-row>
          <!-- 风险隐患查询条件 结束 -->
          <el-row>
            <div class="derive-sty" v-if="code != '风险隐患'">导出Excel</div>
            <!-- 水利设施保险 -- 开始 -->
            <el-col :span="5" class="aa" v-if="mediumSized == '防御复盘' && code == '水利设施保险'">
              <div class="demo-input-suffix">
                <p style="width: 100px">提防名称：</p>
                <el-input placeholder="请输入" v-model="bewareName" class="myClass" clearable @input="queryVal" />
              </div>
              <!-- <div class="demo-input-suffix">
                <p style="width: 90px;margin-right: 9px;">堤防名称:</p>
                <el-input placeholder="请输入" v-model="projectNameVal" class="myClass" clearable @change="bewareName" />
              </div> -->
            </el-col>
            <el-col :span="5" class="aa" v-if="mediumSized == '防御复盘' && code == '水利设施保险'">
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="claimSchedule"
                  size="small"
                  clearable
                  @input="selectVal"
                  placeholder="全部"
                >
                  <el-option
                    v-for="item in claimScheduleList"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- <el-col :span="5" class="aa" v-if="mediumSized == '防御复盘' && code == '水利设施保险'">
              <div class="demo-input-suffix">
                <p style="width: 100px">所属合同：</p>
                <el-input placeholder="请输入" v-model="belongName" class="myClass" clearable @input="queryVal" />
              </div>
            </el-col> -->
            <!-- <el-col :span="5" class="aa" v-if="mediumSized == '防御复盘' && code == '水利设施保险'">
              <div class="demo-input-suffix">
                <p style="width: 100px">所属单位：</p>
                <el-input placeholder="请输入" v-model="affiliatedUnit" class="myClass" clearable @input="queryVal" />
              </div>
            </el-col> -->
            <!-- 水利设施保险 -- 结束 -->
            <!-- 潮位分析 -- 开始 -->
            <el-col :span="5" class="aa" v-if="mediumSized == '防御复盘' && code == '潮位分析'">
              <div class="demo-input-suffix">
                <p style="width: 100px">潮位名称：</p>
                <el-input placeholder="请输入" v-model="seaLevelName" class="myClass" clearable @input="queryVal" />
              </div>
            </el-col>
            <!-- 潮位分析 -- 结束 -->
            <!-- 河网分析 -- 开始 -->
            <el-col :span="5" class="aa" v-if="mediumSized == '防御复盘' && code == '河网分析'">
              <div class="demo-input-suffix">
                <p style="width: 100px">河网名称：</p>
                <el-input placeholder="请输入" v-model="riverName" class="myClass" clearable @input="queryVal" />
              </div>
            </el-col>
            <!-- 河网分析 -- 结束 -->
            <!-- 水闸分析 -- 开始 -->
            <el-col :span="5" class="aa" v-if="mediumSized == '防御复盘' && code == '水闸分析'">
              <div class="demo-input-suffix">
                <p style="width: 100px">水闸名称：</p>
                <el-input placeholder="请输入" v-model="sluiceName" class="myClass" clearable @input="queryVal" />
              </div>
            </el-col>
            <!-- 水闸分析 -- 结束 -->
            <!-- 水库分析 -- 开始 -->
            <el-col :span="5" class="aa" v-if="mediumSized == '防御复盘' && code == '水库分析'">
              <div class="demo-input-suffix">
                <p style="width: 100px">水库名称：</p>
                <el-input placeholder="请输入" v-model="reservoirName" class="myClass" clearable @input="queryVal" />
              </div>
            </el-col>
            <!-- 水库分析 -- 结束 -->
            <!-- 实时降雨 -- 开始 -->
            <el-col :span="5" class="aa" v-if="code == '实时降雨'">
              <div class="demo-input-suffix">
                <p style="width: 50px;">类型:</p>
                <el-select
                  v-model="realTimeType"
                  size="small"
                  @blur="changeVal"
                  placeholder="全部"
                  @change="selectVal"
                >
                  <el-option
                    v-for="item in realTimeStration"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col v-show="realTimeType == '点雨量'" :span="5" class="aa" v-if="code == '实时降雨'">
              <div class="demo-input-suffix">
                <p style="width: 90px">统计类型:</p>
                <el-select
                  v-model="surfaceType"
                  size="small"
                  @blur="changeVal"
                  @change="selectVal"
                  placeholder="全部"
                >
                  <el-option
                    v-for="item in surfaceTypeStration"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="5" class="aa" v-if="code == '实时降雨'">
              <div class="demo-input-suffix">
                <p style="width: 90px">降雨历时:</p>
                <el-select
                  v-model="durationRainfall"
                  size="small"
                  @change="selectVal"
                  placeholder="全部"
                >
                  <el-option
                    v-for="item in durationRainfallList"
                    :key="item.name"
                    :label="item.name"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col v-show="realTimeType == '点雨量'" :span="5" class="aa" v-if="code == '实时降雨'" style="margin-right:10px;">
              <div class="demo-input-suffix" style="height: 35px;">
                  <el-radio v-model="radio" class="el-radio" :label="true" style="color:#fff;" @change="changeVal" @input="selectVal">参与计算雨量站</el-radio>
                  <el-radio v-model="radio" class="el-radio" :label="false" style="color:#fff;" @change="changeVal" @input="selectVal">全部雨量站</el-radio>
              </div>
            </el-col>
            <el-col :style="{marginTop:(realTimeType == '点雨量' ? '10px' : '')}" v-show="realTimeType == '点雨量' && surfaceType == '流域'" :span="5" class="aa" v-if="code == '实时降雨'">
              <div class="demo-input-suffix">
                <p style="width: 50px">流域：</p>
                <el-select
                  v-model="drainageType"
                  size="small"
                  clearable
                  @blur="changeVal"
                  placeholder="全部"
                  @change="selectVal"
                >
                  <el-option
                    v-for="item in administrative"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :style="{marginTop:(realTimeType == '点雨量' ? '10px' : '')}" v-show="realTimeType == '面雨量' || surfaceType == '行政区划'" :span="5" class="aa" v-if="code == '实时降雨'">
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="administrativeType"
                  size="small"
                  clearable
                  @blur="changeVal"
                  placeholder="全部"
                  @change="selectVal"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adcd"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col v-show="realTimeType == '点雨量'" :span="5" class="aa" v-if="code == '实时降雨'" style="margin-top:10px;">
              <div class="demo-input-suffix">
                <el-input placeholder="名称" v-model="NameVal" class="myClass" clearable @blur="changeVal" />
                <div class="derive-sty" style="margin-left: 10px; width:65px;" @click="selectVal">搜 索</div>
              </div>
            </el-col>
            <!-- 实时降雨 -- 结束 -->
            <!-- 短临预报 -->
            <el-col :span="5" class="aa" v-if="code == '短临预报'">
              <div class="demo-input-suffix">
                <p style="width: 100px">统计类型：</p>
                <el-select
                  v-model="rainfallType"
                  size="small"
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in rainfalStration"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 设计防潮标准年限 -->
            <el-col :span="7" class="aa" v-if="code == '海塘'">
              <div class="demo-input-suffix">
                <p style="width: 140px">设计防潮标准年限：</p>
                <el-select
                  v-model="designTideStandard"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in designTideStandardData"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 行政区划 -->
            <el-col
              :span="5"
              class="aa"
              v-if="
              code != '短临预报' && 
              code != '实时降雨' && 
              code != '水闸'&& 
              code !== '水利设施保险' && 
              code != '水库分析' && 
              code !== '水闸分析' && 
              code != '河网分析' && 
              code !== '潮位分析'&&
              code !== '风险隐患'"
            >
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="administrationValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adcd"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 流域 -->
            <el-col
              :span="5"
              class="aa"
              v-if="
                code !== '山洪' &&
                code !== '短临预报' &&
                code !== '实时降雨' &&
                code !== '风险隐患' &&
                code !== '海塘' &&
                code !== '水闸'&&
                code !== '水利设施保险'&&
                code != '水库分析' && 
                code !== '水闸分析' && 
                code != '河网分析' && 
                code !== '潮位分析'"
              >
              <div class="demo-input-suffix">
                <p style="width: 50px">流域：</p>
                <el-select
                  v-model="administrativeValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in administrative"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 工程规模 -->
            <el-col :span="5" v-if="code == '水库'">
              <div class="demo-input-suffix">
                <p style="width: 100px">工程规模：</p>
                <el-select
                  v-model="projectScaleValue"
                  size="small"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in projectScaleData"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 水闸工程规模 -->
            <el-col :span="5" v-if="code == '水闸'">
              <div class="demo-input-suffix">
                <p style="width: 100px">工程规模：</p>
                <el-select
                  v-model="engScalesValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in engScalesData"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 海塘 -->
            <el-col :span="5" v-if="code == '海塘'">
              <div class="demo-input-suffix">
                <p style="width: 100px">工程等别：</p>
                <el-select
                  v-model="engineeringValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in EngineeringData"
                    :key="item.grade"
                    :label="item.grade"
                    :value="item.grade"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 工程类型 -->
            <el-col :span="5" v-if="code == '病险工程'">
              <div class="demo-input-suffix">
                <p style="width: 100px">工程类型：</p>
                <el-select
                  v-model="projType"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in projTypeData"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
          </el-row>
        </div>
      <!-- tab展示区域 -->
        <!-- 海塘详情列表 -->
        <div class="tab-list-sty" v-if="code == '海塘'">
          <el-table
            ref="multipleTable"
            :data="seawallProjData"
            height="525px"
            stripe
            class="abc"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="seawallName"
              label="海塘名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              label="行政区划"
            />
            <el-table-column
            :show-overflow-tooltip="true"
            :formatter="toFixedFormatter"
              prop="seawallLevel"
              label="海塘级别"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="village"
              label="所在乡镇"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="seawallLength"
              width="140px;"
              label="海塘长度(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="designTideStandard"
              label="设计防潮标准(年)"
            />
          </el-table>
        </div>
        <!-- 水库 tab列表 -->
        <div class="tab-list-sty" v-if="code == '水库'">
          <el-table
            ref="multipleTable"
            :data="WaterloggingData"
            height="425px"
            stripe
            class="abc"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :row-class-name="rowClassName"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="resName"
              width="100"
              :sortable="'custom'"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="bas"
              width="70"
              label="流域"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              width="80"
              label="行政区划"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="waterLevel"
              width="100"
              label="水位(m)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              prop="currentLimitLevel"
              :sortable="'custom'"
              width="130"
              label="汛限水位(m)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="uppLevFlco"
              width="150px;"
              label="防洪高水位(m)"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              :show-overflow-tooltip="true"
              :sortable="'custom'"
              prop="dataTime"
              width="100"
              label="更新时间"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              :show-overflow-tooltip="true"
              prop="safetyTime"
              width="110"
              label="鉴定时间"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="safetyConclusion"
              width="110"
              label="鉴定结论"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="reinforce"
              width="110"
              label="有无加固"
            />
            <el-table-column
              v-if="projectScaleValue != '大中型'"
              :formatter="toFixedFormatter"
              prop="normalSpillwayCrestElevation"
              label="溢洪道高程(m)"
            />
            <el-table-column
              v-if="projectScaleValue != '大中型'"
              :formatter="toFixedFormatter"
              prop="damSizeHig"
              label="坝高(m)"
            />
            <el-table-column
              v-if="projectScaleValue != '大中型'"
              :formatter="toFixedFormatter"
              prop="waterLevelCapacity"
              label="实时库容(万m³)"
            />
            <el-table-column
              v-if="projectScaleValue != '大中型'"
              :formatter="toFixedFormatter"
              prop="totCap"
              label="总库容(万m³)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型' && mediumSized == '大中型水库'"
              :formatter="toFixedFormatter"
              prop="drainage24H"
              width="150px;"
              label="24累计出库量(万m³)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型' && mediumSized == '大中型水库'"
              :formatter="toFixedFormatter"
              prop="drainage48H"
              width="150px;"
              label="48累计出库量(万m³)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型' && mediumSized == '大中型水库'"
              :formatter="toFixedFormatter"
              prop="drainage72H"
              width="150px;"
              label="72累计出库量(万m³)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="storageRatio"
              label="蓄水率(%)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              prop="waterLevelCapacity"
              width="160px"
              label="实时库容(万m³)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              prop="limitLevelCapacity"
              width="160px"
              label="汛限水位库容(万m³)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              prop="uppLevFlcoCapacity"
              width="170px"
              label="防洪高水位库容(万m³)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              prop="limitStorageAbility"
              width="160px"
              label="至汛限纳蓄能力(mm)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              prop="floodStorageAbility"
              width="175px"
              label="至防洪高纳蓄能力(mm)"
            />
          </el-table>
        </div>
        <!-- 水闸 tab列表 -->
        <div class="tab-list-sty" v-if="code == '水闸'">
          <el-table
            ref="multipleTable"
            :data="waterGateData"
            height="425px"
            stripe
            class="abc"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :sortable="'custom'"
              prop="wagaName"
              width="100"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              width="70"
              prop="bas"
              label="流域"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              width="80"
              prop="areaName"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="engScal"
              width="100"
              label="工程规模"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="upz"
              width="130"
              label="闸上水位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="dwz"
              width="130"
              label="闸下水位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="ovs"
              width="110"
              label="闸门开度"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              width="175"
              prop="mxgtq"
              label="最大过闸流量(㎡/s)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              width="175"
              prop="ll"
              label="实际过闸流量(㎡/s)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              width="200"
              prop="drainage"
              :label="'累计' + numTime + '小时泄洪量(万m³)'"
            />
          </el-table>
        </div>
        <!-- 河网 tab列表 -->
        <div class="tab-list-sty" v-if="code == '河网'">
          <el-table
            ref="multipleTable"
            :data="riverwayData"
            height="400px"
            stripe
            class="abc"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="rnName"
              width="80"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="bas"
              width="80"
              label="流域"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              width="100"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="waterLevel"
              label="水位（m）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="mainStName"
              label="水位站名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="warningLevel"
              label="警戒水位（m）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="overWarn"
              label="超警戒水位（m）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="guranteeLevel"
              label="保证水位（m）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="overGuarantee"
              label="超保证水位（m）"
            />
          </el-table>
        </div>
        <!-- 潮位 tab列表 -->
        <div class="tab-list-sty" v-if="code == '潮位'">
          <el-table
            ref="multipleTable"
            :data="seaLevelData"
            height="425px"
            stripe
            class="abc"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="stName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="bas"
              width="80"
              label="流域"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              width="80"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="tdz"
              width="150"
              label="实时潮位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="warningLevel"
              label="警戒水位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="overWarn"
              label="超警戒潮位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="astroTide"
              label="天文潮(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="subTide"
              label="增水(m)"
            />
          </el-table>
        </div>
        <!-- 病险工程 tab列表 -->
        <div class="tab-list-sty" v-if="code == '病险工程'">
          <el-table
            ref="multipleTable"
            :data="dangerouslyData"
            height="520px"
            stripe
            class="abc"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :sortable="'custom'"
              prop="projectName"
              width="100"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="bas"
              label="流域"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="engScal"
              label="工程规模"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projLocName"
              label="所在乡镇"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lastCheckTime"
              label="鉴定时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lastCheckResult"
              label="安全鉴定结论"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              v-if="projType == '水库'"
              prop="damType"
              width="100"
              label="坝型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              v-if="projType == '水库'"
              :formatter="toFixedFormatter"
              prop="totCap"
              width="120"
              label="总库容(万m³)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              v-if="projType == '水库'"
              :formatter="toFixedFormatter"
              prop="waterLevel"
              width="120"
              label="实时水位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              v-if="projType == '水库'"
              :formatter="toFixedFormatter"
              prop="limitLevel"
              width="120"
              label="汛限水位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              v-if="projType == '水库'"
              :formatter="toFixedFormatter"
              prop="floodLevel"
              width="140"
              label="防洪高水位(m)"
            />
          </el-table>
        </div>
        <!-- 风险隐患 tab列表 -->
        <div class="tab-list-sty" v-if="code == '风险隐患'">
          <el-table
            ref="multipleTable"
            :data="potentialRisksData"
            height="480px"
            class="abc"
            stripe
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="projName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projType"
              label="工程类型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="recType"
              label="风险类型"
            >
              <template slot-scope="scope">
                <span>
                {{
                  scope.row.sbSign == '251' ? '隐患点' : 
                  scope.row.sbSign == '252' ? '高风险点' :
                  scope.row.sbSign == '253' ? '薄弱点' : '-'
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="dangerType"
              label="隐患类型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="recCondition"
              label="整改情况"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="county"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="reportPerson"
              label="上报人员"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="dutyUnit"
              label="整改责任单位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="reportTime"
              label="发现时间"
            />
          </el-table>
        </div>
        <!-- 山洪 tab列表 -->
        <div class="tab-list-sty" v-if="code == '山洪'">
          <el-table
            ref="multipleTable"
            :data="dangerouslyData"
            height="440px"
            stripe
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projectName"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projectType"
              label="乡镇"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              label="可能性很大的村落"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="waterLevel"
              label="可能性较大的村落"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="warningLevel"
              label="影响人口"
            />
          </el-table>
        </div>
        <!-- 短临预报 tab列表 -->
        <div class="tab-list-sty" v-if="code == '短临预报'">
          <el-table
            ref="multipleTable"
            :data="inTheShortData"
            height="520px"
            stripe
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort = "{prop: 'date', order: 'descending'}"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="key"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="rainfall_1"
              label="1小时(mm)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="rainfall_3"
              label="3小时(mm)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="rainfall_6"
              label="6小时(mm)"
            />
          </el-table>
        </div>
        <!-- 预报 tab列表 -->
        <div class="tab-list-sty" v-if="code == '实时降雨'">
          <el-table
            ref="multipleTable"
            :data="realTimeData"
            :height="realTimeType == '点雨量' ? '480px' : '520px'"
            stripe
            class="abc"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort = "{prop: 'date', order: 'descending'}"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="name"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="newest1hour"
              label="近1小时(mm)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="1hour"
              label="前1小时(mm)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="3hour"
              label="前3小时(mm)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="6hour"
              label="前6小时(mm)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="前24小时(mm)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="72hour"
              label="前72小时(mm)"
            />
          </el-table>
        </div>
        <!-- 第三场景 水库 -->
        <div class="tab-list-sty" v-if="mediumSized == '防御复盘' && code == '水库分析'">
          <el-table
            ref="multipleTable"
            :data="reservoirAnalysisData.list"
            height="420px"
            stripe
            class="abc"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort = "{prop: 'date', order: 'descending'}"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="resName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="lowestWaterLevel"
              label="最低水位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lowestWaterLevelTm"
              width="150"
              label="最低水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="highestWaterLevel"
              label="最高水位(m)"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              :show-overflow-tooltip="true"
              :sortable="'custom'"
              prop="dataTime"
              width="100"
              label="更新时间"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              :show-overflow-tooltip="true"
              prop="safetyTime"
              width="110"
              label="鉴定时间"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="safetyConclusion"
              width="110"
              label="鉴定结论"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="reinforce"
              width="110"
              label="有无加固"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="highestWaterLevelTm"
              width="150"
              label="最高水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              width="150"
              prop="capacitySub"
              label="累计拦蓄水量(万m³)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              width="150"
              prop="drainage"
              label="累计泄洪量(万m³)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              width="150"
              prop="overWarnMinutes"
              label="累计超警时间(min)"
            />
          </el-table>
        </div>
        <!-- 第三场景 水闸 -->
        <div class="tab-list-sty" v-if="mediumSized == '防御复盘' && code == '水闸分析'">
          <el-table
            ref="multipleTable"
            :data="lockAnalysisData"
            height="520px"
            stripe
            class="abc"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort = "{prop: 'date', order: 'descending'}"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="wagaName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="lowestUpz"
              label="最小闸上水位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lowestUpzTm"
              label="最小闸上水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="highestUpz"
              label="最大闸上水位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="highestUpzTm"
              label="最大闸上水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="openGateMinutes"
              label="累计开闸时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="drainage"
              label="累计排洪量"
            />
          </el-table>
        </div>
        <!-- 第三场景 河网 -->
        <div class="tab-list-sty" v-if="mediumSized == '防御复盘' && code == '河网分析'">
          <el-table
            ref="multipleTable"
            :data="riverNetworkData"
            height="500px"
            stripe
            class="abc"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort = "{prop: 'date', order: 'descending'}"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="rnName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="mainStName"
              label="水位站名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="lowestWaterLevel"
              label="最低水位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lowestWaterLevelTm"
              label="最低水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="highestWaterLevel"
              label="最高水位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="highestWaterLevelTm"
              label="最高水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              prop="overWarnMinutes"
              label="累计超警时间"
            />
          </el-table>
        </div>
        <!-- 第三场景 潮位 -->
        <div class="tab-list-sty" v-if="mediumSized == '防御复盘' && code == '潮位分析'">
          <el-table
            ref="multipleTable"
            :data="waterAnalysisData"
            height="520px"
            stripe
            class="abc"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort = "{prop: 'date', order: 'descending'}"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="stName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="lowestRealTide"
              label="最低水位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lowestRealTideTm"
              label="最低水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="highestRealTide"
              label="最高水位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="highestRealTideTm"
              label="最高水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="highestSubTide"
              label="最大增水"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="overWarnMinutes"
              label="累计超警时间"
            />
          </el-table>
        </div>
        <!-- 第三场景 水利设施保险 -->
        <div class="tab-list-sty" v-if="mediumSized == '防御复盘' && code == '水利设施保险'">
          <el-table
            ref="multipleTable"
            :data="realTimeData"
            height="400px"
            stripe
            class="abc"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort = "{prop: 'date', order: 'descending'}"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="name"
              label="合同编号"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="name"
              label="合同名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="newest1hour"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="1hour"
              label="堤防名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="3hour"
              label="损失图片"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="6hour"
              label="相关文件"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="主管部门(甲)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="上传时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="现场视频"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="现场音频"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="现场图片"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="估评理赔金额(元)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="堪核时间"
            />
          </el-table>
        </div>
        <!-- <SilderTimelineBar v-if="mediumSized == '防御复盘'" /> -->
        <!-- 底部按钮 -->
        <!-- <div slot="footer" class="dialog-footer" v-show="mediumSized != '防御复盘'">
          <el-button type="primary" size="small" plain @click="handleClose">取 消</el-button>
        </div> -->
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { disasterPreventionApi } from "@/api/RightSildePanel";
  import { SilderTimelineBar } from "@/components";
  import { mapGetters } from "vuex";
  export default {
    name: "DetailsCommonDialog",
    components: {
      SilderTimelineBar,
    },
    props: {
      visible: {
        type: Boolean,
        default: false
      },
      code: {
        type: String,
        default: ''
      },
      mediumSized: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        // 病险工程
        NameVal: '',
        radio: true,
        durationRainfall: '',
        durationRainfallList: [
          {
            value: 1,
            name: '前1小时'
          },{
            value: 3,
            name: '前3小时'
          },{
            value: 6,
            name: '前6小时'
          },{
            value: 24,
            name: '前24小时'
          },{
            value: 72,
            name: '前72小时'
          }
        ],
        projectNameVal: '',     //工程名称
        projectTypeVal: [],     //工程类型
        projectTypeList: [
          {
            value: '水利工程',
            label: '水利工程',
            children: [{
              value: '水库',
              label: '水库'
            }, {
              value: '海塘',
              label: '海塘'
            },{
              value: '堤防',
              label: '堤防'
            },{
              value: '闸站',
              label: '闸站'
            },{
              value: '泵站',
              label: '泵站'
            },{
              value: '山塘',
              label: '山塘'
            }]
          }, {
            value: '在建水利工程',
            label: '在建水利工程',
           
          }, {
            value: '在建涉水工程',
            label: '在建涉水工程',
            
          }, {
            value: '水毁工程',
            label:'水毁工程',
            children: [{
              value: '水库',
              label: '水库'
            }, {
              value: '海塘',
              label: '海塘'
            }, {
              value: '堤防',
              label: '堤防'
            }, {
              value: '闸站',
              label: '闸站'
            }, {
              value: '泵站',
              label: '泵站'
            }, {
              value: '山塘',
              label: '山塘'
            }, {
              value: '渠道',
              label: '渠道'
            }, {
              value: '护岸',
              label: '护岸'
            }, {
              value: '水文测站',
              label: '水文测站'
            }]
          }, {
            value: '其他',
            label: '其他',
          }
        ],    //工程类型
        riskTypeVal: '',        //风险类型
        riskTypeList: [
          {
            name: '隐患点',
            value:'251'
          }, {
            name: '高风险点',
            value:'252'
          },{
            name: '薄弱点',
            value:'253'
          }
        ],       //风险类型
        district: [],           //区域
        districtList: [
          {
            value: '行政区划',
            label: '行政区划',
            children: [{
              value: '温州',
              label: '温州',
              children: [{
                value: '"330302000000"',
                label: '鹿城区',
              },{
                value: '"330303000000"',
                label: '龙湾区',
              },{
                value: '"330304000000"',
                label: '瓯海区',
              },{
                value: '"330322000000"',
                label: '洞头区',
              },{
                value: '"330324000000"',
                label: '永嘉县',
              },{
                value: '"330326000000"',
                label: '平阳县',
              },{
                value: '"330327000000"',
                label: '苍南县',
              },{
                value: '"330328000000"',
                label: '文成县',
              },{
                value: '"330329000000"',
                label: '泰顺县',
              },{
                value: '330381000000',
                label: '瑞安市',
              },{
                value: '"330382000000"',
                label: '乐清市',
              },{
                value: '"330383000000"',
                label: '龙港市',
              }]
            }]
          }, {
            value: '流域',
            label:'流域',
            children: [{
              value: '瓯江',
              label: '瓯江'
            }, {
              value: '飞云江',
              label: '飞云江'
            }, {
              value: '鳌江',
              label: '鳌江'
            }]
          }
        ],       //区域
        reorganizeSituation: '',      //整改情况
        reorganizeSituationList: [
          {
            name: '未整改'
          }, {
            name: '正在整改'
          },{
            name: '已落实措施'
          }, {
            name: '完成整改'
          }
        ],  //整改情况
        discoveryChannel: '',         //发现渠道
        discoveryChannelList: [
          {
            name: '县级检查',
            value: 'xjjc'
          }, {
            name: '工程自查',
            value: 'zczc'
          },{
            name: '市级抽查',
            value: 'sjcc'
          }, {
            name: '省级督查',
            value: 'sjdc'
          }
        ],     //发现渠道
        // 第三场景
        bewareName: '',         //提防名称
        belongName: '',         //所属合同
        affiliatedUnit: '',     //所属单位
        seaLevelName: '',       //潮位名称
        riverName: '',          //河网名称
        sluiceName: '',         //水闸名称
        reservoirName: '',       //水库名称
        claimSchedule: '',      //理赔进度
        reservoirAnalysisData: {},  //水库数据  
        lockAnalysisData: [],      //水闸数据
        riverNetworkData: [],      //河网数据
        waterAnalysisData: [],      //潮位数据
        tideStaStart: '',          //复盘起始时刻
        tideStaEnd: '',            //复盘结束时刻

        claimScheduleList: [
          {
            name: '已申请'
          },
          {
            name: '已理赔'
          },
          {
            name: '主管部门确认'
          }
        ],                      //理赔进度

        count: 0,
        numTime: '1',
        //表格loading
        loading: false,
        //行政区划
        administration: [],
        engineeringValue: '',
        filterOpenGate: false,
        //水库流域选中值
        administrativeValue: "",
        designTideStandard: '',
        //行政区划选中值
        administrationValue: "",
        designTideStandardData: [
          {
            name: "100年以上",
          }, {
            name: "100年一遇",
          }, {
            name: "50年一遇",
          }, {
            name: "20年一遇",
          }, {
            name: "10年一遇",
          }, {
            name: "10年以下",
          }
        ],
        //海塘工程等别
        EngineeringData: [
          {
            grade: "1级",
          }, {
            grade: "2级",
          }, {
            grade: "3级",
          }, {
            grade: "4级",
          }, {
            grade: "5级",
          }
        ],
        //水库流域
        administrative: [
          {
            name: "飞云江",
          }, {
            name: "瓯江",
          }, {
            name: "鳌江",
          }
        ],
        //工程规模
        projectScaleData: [
          {
            name: "大中型",
          }, {
            name: "小（Ⅰ）型",
          }, {
            name: "小（Ⅱ）型",
          }
        ],
        surfaceTypeStration: [
          {
            name: "行政区划",
          }, {
            name: "流域",
          },
        ],
        projTypeData: [
          {
            name: "水库",
          }, {
            name: "山塘",
          }, {
            name: "海塘",
          }, {
            name: "水闸",
          }
        ],
        //水库工程规模选中值
        projectScaleValue: "大中型",
        statisticsData: {},
        //水闸数据
        waterGateData: [],
        //水闸工程规模选中值
        engScalesValue:'',
        //水库工程规模下拉数据
        engScalesData: [
          {
            name: "大（Ⅰ）型",
          }, {
            name: "大（Ⅱ）型",
          }, {
            name: "中型",
          }, {
            name: "小（Ⅰ）型",
          }, {
            name: "小（Ⅱ）型",
          }
        ],
        //水库统计
        reservoirStatistical: {},
        riverwayData: [],
        riverwayList: {},
        seaLevelData: [],
        seaLevelList: {},
        dangerouslyData: [],
        projType: "",
        rainfallType: "行政区划",
        rainfalStration: [
          {
            name: "行政区划",
          }, {
            name: "流域",
          }
        ],
        inTheShortData: [],
        realTimeData: [],
        realTimeStration: [
          {
            name: "面雨量",
          }, {
            name: "点雨量",
          },
        ],
        realTimeType: "点雨量",
        administrativeType: "",
        drainageType: "",
        surfaceType: "",
        inputValue: "", //输入框
        selectValue: "", //下拉值
        potentialRisksData: [],
        seawallProjData: [],
        orderColumn: '',
        ascOrDesc: '',
        options: [
          {
            label: "测试",
            value: "测试",
          },
        ],
        WaterloggingData: [],
        num: 0,
        rainSearchList:[],
      }
    },
    computed: {
      ...mapGetters([ "formatterStartTime", "formatterCurrentTime" ]),
      startTime() {
        let time = this.formatterStartTime();
        return time != "Invalid date" ? time : "请选择时间";
      },
      currentTime() {
        let time = this.formatterCurrentTime();
        return time != "Invalid date" ? time : "请选择时间";
      },
    },
    watch: { 
      startTime: {
        immediate: true,
        handler(val) {
          if (val != '请选择时间') {
            this.tideStaStart = val + ':00';
          }
        },
      },
      currentTime: {
        immediate: true,
        handler(val) {
          if (val != '请选择时间') {
            this.tideStaEnd = val + ':00';
            this.$nextTick(() => {
                this.queryVal();
            })
          }
        }
      }
    },
    mounted() { 
      if (this.formatterCurrentTime() != 'Invalid date') {
        this.tideStaStart = this.formatterStartTime() + ":00";
        this.tideStaEnd = this.formatterCurrentTime() + ":00";
      }else {
        this.tideStaStart = this.$moment().subtract(3, "days").format("YYYY-MM-DD HH") + ":00:00";
        this.tideStaEnd = this.$moment().subtract(0, "days").format("YYYY-MM-DD HH") + ":00:00";
      }
      this.$nextTick(() => {
        this.getArea();
        if (this.code) {
          switch (this.code) {
            case "水库":
              this.rsvrProjList();
              break;
            case "水闸":
              this.wagaProjList();
              break;
            case "河网":
              this.riverNetworkList();
              break;
            case "潮位":
              this.tideList();
              break;
            case "病险工程":
              this.dilapProjList();
              break;
            case "风险隐患":
              this.projRiskList();
              break;
            case '海塘':
                this.seawallList();
            break;
            case "短临预报":
              this.rainfallPredictGrid();
              break;
            case "实时降雨":
              this.grandTotalRainfallGrid();
              break;
            case "水库分析":
              this.reservoirAnalysisInvoking();
              break;
            case "水闸分析":
              this.lockAnalysisInvoking();
              break;
            case "河网分析":
              this.riverNetworkAnalysis();
              break;
            case "潮位分析":
              this.waterAnalysisInvoking();
              break;
          }
        }
      })
    },
    methods: {
      changeVal() {
        this.durationRainfall = "";
        this.realTimeData = [];
      },
      /**
       * 第三场景 模糊查询
       */
      queryVal() {
        switch (this.code) {
          case "水库分析":
            this.reservoirAnalysisInvoking();
            break;
          case "水闸分析":
            this.lockAnalysisInvoking();
            break;
          case "河网分析":
            this.riverNetworkAnalysis();
            break;
          case "潮位分析":
            this.waterAnalysisInvoking();
            break;
        }
      },
      /**
       * 第三场景 水库接口调用
       */
      async reservoirAnalysisInvoking() {
        this.loading = true;
        let obj = {
          start: this.tideStaStart,  // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
          end: this.tideStaEnd,      // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
          resName: this.reservoirName
        };
        await disasterPreventionApi.rsvrReplayStats(obj).then((res) => {
          if (res.code == 0) {
            this.reservoirAnalysisData = res.data;
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        });
      },
      /**
       * 第三场景 水闸接口调用
       */
      async lockAnalysisInvoking() {
        this.loading = true;
        let obj = {
          start: this.tideStaStart,  // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
          end: this.tideStaEnd,      // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
          wagaName: this.sluiceName
        };
        await disasterPreventionApi.wrpWagaReplayStats(obj).then((res) => {
          if (res.code == 0) {
            this.lockAnalysisData = res.data.list;
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        });
      },
      /**
       * 第三场景 河网接口调用
       */
      async riverNetworkAnalysis() {
        this.loading = true;
        let obj = {
          start: this.tideStaStart,  // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
          end: this.tideStaEnd,      // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
          rnName: this.riverName
        };
        await disasterPreventionApi.rivernetReplayStats(obj).then((res) => {
          if (res.code == 0) {
            this.riverNetworkData = res.data.list;
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        });
      },
      /**
       * 第三场景 潮位接口调用
       */
      async waterAnalysisInvoking() {
        this.loading = true;
        let obj = {
          start: this.tideStaStart,  // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
          end: this.tideStaEnd,      // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
          stName: this.seaLevelName
        };
        await disasterPreventionApi.waterAnalysisList(obj).then((res) => {
          if (res.code == 0) {
            this.waterAnalysisData = res.data.data.list;
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        });
      },
      /**
       * 弹窗关闭
       */ 
      handleClose() {
        this.$emit("close");
      },
      /**
       * 开启数
       */
      openGates() {
        this.count++;
        this.numTime = '1';
        if (this.count % 2) {
          this.filterOpenGate = true;
          this.wagaProjList();
        } else {
          this.filterOpenGate = false;
          this.wagaProjList();
        }
      },
      /**
       * 累计泄洪时间
       */
      changeHours() {
        this.wagaProjList();
      },
      /**
       * 海塘接口调用
       */
      async seawallList() {
        this.loading = true;
        let obj = {
          adcd: this.administrationValue,  // 行政区划编码
          grade: this.engineeringValue,    // 1级、2级、3级、4级、5级
          designTideStandard: this.designTideStandard,
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc
        };
        await disasterPreventionApi.seawallProjList(obj).then((res) => {
          if (res.code == 0) {
            this.seawallProjData = res.data;
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        });
      },
      /**
       * 行的回调函数
       */
      rowClassName({row}) {
        if (row.overLimitVal > 0) {
          return 'warm-row';
        }else {
          return '';
        }
      },
      /**
       * 水库字段排序
       */
      sortChange(val) {
        if (val.order) {
          if (val.order == 'ascending') {
            this.orderColumn = val.prop;
            this.ascOrDesc = false;
          }else {
            this.orderColumn = val.prop;
            this.ascOrDesc = true;
          }
        }else {
          this.orderColumn = '';
          this.ascOrDesc = '';
        }
        if (this.code) {
          switch (this.code) {
            case "水库":
              this.rsvrProjList();
              break;
            case "水闸":
              this.wagaProjList();
              break;
            case "河网":
              this.riverNetworkList();
              break;
            case "潮位":
              this.tideList();
              break;
            case "病险工程":
              this.dilapProjList();
              break;
            case '海塘':
                this.seawallList();
            break;
          }
        }
      },
      /**
       * 风险隐患
       */
      async projRiskList() {
        let arr = '';
        let str = '';
        if (this.projectTypeVal.length == 2) {
          arr = this.projectTypeVal[0] + '-' + this.projectTypeVal[1];
        }else {
          arr = '';
        }
        if (this.district.length == 3) {
          str = this.district[2];
        }else if (this.district.length == 2) {
          str = this.district[1];
        }else {
          str = '';
        }
        this.loading = true;
        // let obj = {
        //   projName: this.projectNameVal, // 工程名称
        //   projType: arr, // 工程类型
        //   recType: this.riskTypeVal, // 风险类型
        //   areaCode: str, // 行政区划
        //   discoverChannel: this.discoveryChannel, // 发现渠道
        //   rectifyStatus: this.reorganizeSituation, // 整改情况
        //   startDate: this.affiliatedUnit[0], // 发现日期起始点
        //   endDate: this.affiliatedUnit[1], // 发现日期截止点
        // };
        let obj = {
          projName:this.projectNameVal,// 工程名称
          projType:arr,// 工程类型
          sbSign:this.riskTypeVal,// 风险类型
          adcdDuty:str?str.substring(1,7):'',// 行政区划
          property:this.discoveryChannel, // 发现渠道
          recCondition:this.reorganizeSituation,
          startTime:this.affiliatedUnit[0], // 发现日期起始点
          endTime:this.affiliatedUnit[1],
          pageNum:1,
          pageSize:5000
        }
        await disasterPreventionApi.busRiskListPage(obj).then((res) => {
          if (res.code == 0) {
            this.potentialRisksData = res.data.list;
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        });
      },
      /**
       * 实时降雨
       */
      async grandTotalRainfallGrid() {
        this.loading = true;
        let obj = {
          hour: this.durationRainfall,
          type: this.realTimeType, //只能是面雨量或者点雨量
          surfaceType: this.surfaceType, //在type选择面雨量之后，只能传流域或者行政区划
          areaCode: this.administrativeType, //行政区划编码，只有在type选择点雨量的时候才传
          bas: this.drainageType, //流域名称（瓯江/飞云江/鳌江），只有在type选择点雨量的时候才传
          stationName: this.NameVal,        // 搜索框
          isSurfaceRainStation: this.radio,   // 重点时传true 非重点传false
        };
        await disasterPreventionApi.grandTotalRainfallGrid(obj).then((res) => {
          if (res.code == 0) {
            if (this.durationRainfall == "") {
              this.realTimeData = res.data;
            }else {
              if (this.durationRainfall == 1) {
                let arr = [];
                this.realTimeData.forEach((item,index)=> {
                  item['1hour'] = res.data[index]['1hour'];
                  arr.push(item);
                });
                this.realTimeData = arr;
              }else if (this.durationRainfall == 3) {
                let arr = [];
                this.realTimeData.forEach((item,index)=> {
                  item['3hour'] = res.data[index]['3hour'];
                  arr.push(item);
                });
                this.realTimeData = arr;
              }else if (this.durationRainfall == 6) {
                let arr = [];
                this.realTimeData.forEach((item,index)=> {
                  item['6hour'] = res.data[index]['6hour'];
                  arr.push(item);
                });
                this.realTimeData = arr;
              }else if (this.durationRainfall == 24) {
                let arr = [];
                this.realTimeData.forEach((item,index)=> {
                  item['24hour'] = res.data[index]['24hour'];
                  arr.push(item);
                });
                this.realTimeData = arr;
              }else if (this.durationRainfall == 72) {
                let arr = [];
                this.realTimeData.forEach((item,index)=> {
                  item['72hour'] = res.data[index]['72hour'];
                  arr.push(item);
                });
                this.realTimeData = arr;
              }
            }
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        });
      },
      /**
       * 短临预报
       */
      async rainfallPredictGrid() {
        this.loading = true;
        let obj = {
          type: this.rainfallType,
        };
        await disasterPreventionApi.rainfallPredictGrid(obj).then((res) => {
          if (res.code == 0) {
            this.inTheShortData = res.data;
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        });
      },
      /**
       * 病险工程
       */
      async dilapProjList() {
        this.loading = true;
        let obj = {
          adcd: this.administrationValue, // 行政区划编码
          bas: this.administrativeValue, // 流域 飞云江、瓯江、鳌江
          projType: this.projType,
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc
        };
        await disasterPreventionApi.dilapProjList(obj).then((res) => {
          if (res.code == 0) {
            this.dangerouslyData = res.data.list;
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        });
      },
      /**
       * 潮位接口
       */
      async tideList() {
        this.loading = true;
        let obj = {
          adcd: this.administrationValue, // 行政区划编码
          bas: this.administrativeValue, // 流域 飞云江、瓯江、鳌江
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc
        }
        await disasterPreventionApi.tideList(obj).then((res) => {
          if (res.code == 0) {
            this.seaLevelData = res.data.list;
            this.seaLevelList = res.data;
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        });
      },
      /**
       * 河网接口
       */
      async riverNetworkList() {
        this.loading = true;
        let obj = {
          adcd: this.administrationValue, // 行政区划编码
          bas: this.administrativeValue, // 流域 飞云江、瓯江、鳌江
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc
        };
        await disasterPreventionApi.riverNetworkList(obj).then((res) => {
          if (res.code == 0) {
            this.riverwayData = res.data.list;
            this.riverwayList = res.data;
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        });
      },
      /**
       * 水闸接口
       */
      async wagaProjList() {
        this.loading = true;
        let obj = {
          filterOpenGate: this.filterOpenGate,
          engScal: this.engScalesValue,
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc,
          hours: this.numTime 
        };
        await disasterPreventionApi.wagaProjList(obj).then((res) => {
          if (res.code == 0) {
            this.waterGateData = res.data.list;
            this.reservoirStatistical = res.data;
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        });
      },
      /**
       * 选中事件
       */
      selectVal() {
        this.numTime = '1';
        switch (this.code) {
          case "水库":
            this.rsvrProjList();
            break;
          case "水闸":
            this.wagaProjList();
            break;
          case "河网":
            this.riverNetworkList();
            break;
          case "潮位":
            this.tideList();
            break;
          case "病险工程":
            this.dilapProjList();
            break;
          case "风险隐患":
            this.projRiskList();
            break;
          case '海塘':
                this.seawallList();
            break;
          case "短临预报":
            this.rainfallPredictGrid();
            break;
          case "实时降雨":
            this.grandTotalRainfallGrid();
            break;
        }
        if (this.code == "实时降雨") {
          if (this.realTimeType == "面雨量") {
            this.drainageType = "";
            this.surfaceType = "";
          } else {
            if (this.surfaceType == "流域") {
              this.administrativeType = "";
            }else {
              this.drainageType = "";
            }
          }
        }
      },
      /**
       * 短临降雨
       */
      Formatter(row, column, value, index) {
        // if (!value || value == "") return "-";
        if(value != null) {
          let valueFixed = String(value);
          if (valueFixed.split(",").length > 1) {
            valueFixed = valueFixed.split(",")[0];
          }
          valueFixed = Number(valueFixed).toFixed(1);
          return valueFixed;
        }else {
          return "-"
        }
        
      },
      /**
       * 表格数据格式化
       */
      toFixedFormatter(row, column, value, index) {
        if (value != null) {
          return value;
        }else {
          return "-"
        }
      },
      /**
       * 行政区划
       */
      async getArea() {
        let obj = {
          areaCode: 3303,
          level: 3,
        };
        await disasterPreventionApi.getArea(obj).then((res) => {
          if (res.code == 0) {
            this.administration = res.data;
          }
        });
      },
      /**
       * 水库详情数据
       */
      async rsvrProjList(val) {
        this.loading = true;
        let obj = {
          resAdcd: this.administrationValue, // 行政区划编码
          resBas: this.administrativeValue, // 流域 飞云江、瓯江、鳌江
          engScales: this.projectScaleValue,
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc,
          drainage24H: this.mediumSized == '大中型水库' ? true : false,
          drainage48H: this.mediumSized == '大中型水库' ? true : false,
          drainage72H: this.mediumSized == '大中型水库' ? true : false,
        }
        await disasterPreventionApi.rsvrProjList(obj).then((res) => {
          if (res.code == 0) {
            this.WaterloggingData = res.data.list;
            this.statisticsData = res.data;
          }
          this.loading = false;
        }).catch(error => {
          this.loading = false;
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .primary-dialog {
    ::v-deep .el-dialog {
      .el-dialog__header {
        padding: 2px;
        .primary-dialog__title {
          .primary-dialog__title--label {
            width: 80%;
            height: 32px;
            color: #fff;
            font-size: 16px;
            letter-spacing: 0;
            margin-left: 15px;
            font-weight: bold;
            line-height: 35px;
            padding-left: 56px;
          }
        }
      }
      .el-dialog__body {
        padding: 2px;
        padding-top: 0;
        border-bottom: 0.0625rem solid #00c1ff;
        .primary-dialog__content {
          height: 620px;
          border-top: 0;
          padding: 0 16px;
          box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
          background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
          
          // 统计样式
          .statistics-sty {
            padding: 10px 0;
            width: 100%;
            display: flex;
            justify-content: left;
            border-bottom: 1px solid rgba($color: #409eff, $alpha: 0.4);
            > div {
              margin-right:50px;
              padding: 10px 0;
              display: flex;
              justify-content: space-between;
              > p {
                padding-top: 5px;
                margin-right:15px;
              }
              > div{
                >p:nth-child(1) {
                  font-family: PingFangSC-Regular;
                  font-size: 15px;
                  color: #FFFFFF;
                  letter-spacing: 0;
                  line-height: 24px;
                  font-weight: 400;
                  margin-bottom: 5px;
                }
                >p:nth-child(2) {
                  font-family: PingFangSC-Medium;
                  font-size: 14px;
                  color: #fff;
                  letter-spacing: 0;
                  line-height: 24px;
                  font-weight: 500;
                  >span {
                    font-size: 24px;
                  color: #56FEFE;
                  }
                }
              }
            }
            
          }
          // 查询样式
          .query-sty {
            padding: 15px 0;
            color: #fff;
            .derive-sty {
              padding: 0 10px;
              text-align: center;
              border-radius: 4px;
              line-height: 30px;
              float: right;
              color: #fff;
              border-color: linear-gradient(180deg , #36c0e4 20%, #0639ff 100%) !important;
              background: linear-gradient(180deg , #36c0e4 20%, #0639ff 100%);
            }
            .aa {
              margin-right: 20px;
            }
            .demo-input-suffix {
              display: flex;
              justify-content: left;
              align-items: center;
            }
          }
        }
      }
    }
    ::v-deep.el-input-number {
      width: 5.25rem;
    }
    ::v-deep.el-input__inner:hover,
    .el-select:hover .el-input__inner {
      border: 1px solid #fff;
    }
    ::v-deep.el-input__inner {
      height: 2rem;
      background: rgba(24, 47, 90, 0.1);
      color: #fff;
      border: 1px solid #fff;
    }
    ::v-deep.el-select .el-input .el-select__caret {
      color: #fff;
    }
    ::v-deep input::-webkit-input-placeholder {
      color: #fff;
    }
    .dialog-footer {
      .el-button--primary.is-plain {
        color: #fff;
        border-color: #0bbbff;
        background-color: rgba($color: #0bbbff, $alpha: 0.1);
      }
    }
    ::v-deep.el-table .warm-row .cell  {
      color: #EEC80B !important;
    }
    .abc ::v-deep .el-table__body tr:hover {
      background-color: rgba($color: #56FEFE, $alpha: 0.4);
    } 
    .gates-open-sty {
      background: url(~assets/images/WaterDisastersImage/zhamen-kaiqi.png) no-repeat center center;
      background-size: 100% 100%;
    }
    .water-gate-sty {
      > .area-style {
        border-left: 1px solid rgba($color: #0bbbff, $alpha: 0.5);
        background: none;
        display: flex;
        justify-content: flex-start;
        > p:nth-child(1) {
          padding: 15px 15px 0 40px;
          > span {
            font-family: PingFangSC-Regular;
            font-size: 16px;
            color: #FFFFFF;
            font-weight: 400;
            margin-right: 10px;
          }
          > i {
            line-height: 30px;
            margin: 0 10px 0 0;
            color: #fff;
          }
          ::v-deep.el-input__inner {
            background: none;
            border: 0.0625rem solid #fff;
          }
          ::v-deep.el-input-number__decrease {
            border-left: 0.0625rem solid #fff;
            background: none;
            > i {
              color: #fff;
            }
          }
          ::v-deep.el-input-number__increase {
            height: 16px;
            border-left: 0.0625rem solid #fff;
            background: none;
            > i {
              color: #fff;
            }
          }
        }
        > div:nth-child(2) {
          padding: 15px 15px 0 40px;
          font-size: 16px;
          color: #fff;
          > span {
            font-family: PingFangSC-Medium;
            font-size: 24px;
            color: #1CFFF5;
            letter-spacing: 0;
            text-align: right;
            line-height: 24px;
            font-weight: 500;
            padding: 0;
            margin: 0;
          }
        }
      }
    }
  }
  // 弹窗底部按钮样式
  .dialog-footer {
    position: absolute;
    right: 20px;
    bottom: 15px;
    .el-button--primary.is-plain {
      color: #fff;
      border-color: #0bbbff;
      background-color: rgba($color: #0bbbff, $alpha: 0.1);
    }
  }
  ::v-deep.primary-dialog .el-dialog .el-table th {
    color: #1cfff5;
  }
  ::v-deep.el-table .ascending .sort-caret.ascending {
    border-bottom-color: #1CFFF5;
  }
  ::v-deep.el-table .descending .sort-caret.descending {
    border-top-color: #1CFFF5;
  }
  ::v-deep.myClass .el-input__suffix {
    margin-top: -3px;
    padding: 0;
  }
  ::v-deep.el-date-editor .el-range-input {
    background: none;
    color: #fff;
  }
  ::v-deep.el-date-editor .el-range-separator {
    color: #fff;
    line-height: 1.5rem;
  }
  ::v-deep.el-date-editor .el-range__icon {
    color: #fff;
    line-height: 1.4rem;
  }
  ::v-deep.el-date-editor .el-range__close-icon {
    color: #fff;
    line-height: 1.4rem;
  }
</style>
