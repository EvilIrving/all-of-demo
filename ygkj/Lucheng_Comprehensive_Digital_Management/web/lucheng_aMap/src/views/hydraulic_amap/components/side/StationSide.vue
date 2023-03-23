<!--
 * @Author: hanyu
 * @LastEditTime: 2022-09-14 10:21:19
 * @Description: 
 * @FilePath: \lucheng_aMap\src\views\hydraulic_amap\components\side\StationSide.vue
-->
<template>
  <div class="station-side" ref="stationSide">
    <!-- 统计分析 -->
    <div class="statistics-chart" ref="statisticsChart">
      <div v-if="stationType == '视频'" style="padding: 0 0 10px; color: #fff">
        <el-row>
          <el-col :span="6" style="opacity: 0.8">
            <div>
              <p style="text-align: center">总数</p>
              <p style="padding-top: 10px; text-align: center">
                <span style="color: #56fefe; font-size: 18px">{{
                  cameraList.totalNum
                }}</span>
                个
              </p>
            </div>
          </el-col>
          <el-col :span="6" style="border-left: 1px solid #fff; opacity: 0.8">
            <div style="text-align: center">
              <p style="text-align: center">在线</p>
              <p style="padding-top: 10px; text-align: center">
                <span style="color: #56fefe; font-size: 20px">{{
                  cameraList.onlineNum
                }}</span>
                个
              </p>
            </div>
          </el-col>
          <el-col :span="6" style="border-left: 1px solid #fff; opacity: 0.8">
            <div style="text-align: center">
              <p style="text-align: center">离线</p>
              <p style="padding-top: 10px; text-align: center">
                <span style="color: #56fefe; font-size: 18px">{{
                  cameraList.offlineNum
                }}</span>
                个
              </p>
            </div>
          </el-col>
          <el-col :span="6" style="border-left: 1px solid #fff; opacity: 0.8">
            <div style="text-align: center">
              <p style="text-align: center">在线率</p>
              <p style="padding-top: 10px; text-align: center">
                <span style="color: #56fefe; font-size: 18px">{{
                  cameraList.onlineRate
                }}</span>
                %
              </p>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="chart-tabs">
        <span
          v-if="
            stationType == '雨情' ||
            stationType == '水库水位' ||
            stationType == '河道水位' ||
            stationType == '堰闸水位' ||
            stationType == '潮位' ||
            stationType == '水闸工情' ||
            stationType == '墒情' ||
            stationType == '水质监测' ||
            stationType == '积水' ||
            stationType == '泵站工情' ||
            stationType == '流量'
          "
          :class="[typeTab == '条件选择' ? 'active' : '']"
          @click="typeTabChange('条件选择')"
          >条件选择
        </span>
        <!-- <span
          v-if="stationType != '墒情' || stationType != '水质监测'"
          :class="[typeTab == '行政区域' ? 'active' : '']"
          @click="typeTabChange('行政区域')"
          >行政区划</span
        >
        <span
          v-if="stationType == '水质监测'"
          :class="[typeTab == '站点级别' ? 'active' : '']"
          @click="typeTabChange('站点级别')"
          >站点级别</span
        >
        <span
          v-if="stationType == '水质监测'"
          :class="[typeTab == '水质类别' ? 'active' : '']"
          @click="typeTabChange('水质类别')"
          >水质类别</span
        >
        <span
          v-if="stationType == '水库水位'"
          :class="[typeTab == '类别' ? 'active' : '']"
          @click="typeTabChange('类别')"
          >按类型</span
        > -->

        <!-- <span
          v-if="
            stationType != '流量' &&
            stationType != '墒情' &&
            stationType != '泵站工情' &&
            stationType != '水库水位'
          "
          :class="[typeTab == '行政区域' ? 'active' : '']"
          @click="typeTabChange('行政区域')"
          >区域</span
        > -->
        <span
          v-if="stationType == '视频'"
          :class="[typeTab == '在线率' ? 'active' : '']"
          @click="typeTabChange('在线率')"
          >在线率</span
        >
        <!-- <span
          v-if="stationType == '水库水位'"
          :class="[typeTab == '流域' ? 'active' : '']"
          @click="typeTabChange('流域')"
          >流域统计</span
        > -->
        <div class="title-right" v-if="stationType != '水质监测'">
          <i @click="resetParam()" class="icon-btn-reset"></i>
        </div>
      </div>
      <div class="chart-box" :style="chartStyle">
        <echart-capsule
          v-loading="chartLoading"
          element-loading-text="数据加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          v-if="
            typeTab == '行政区域' ||
            typeTab == '在线率' ||
            typeTab == '站点级别' ||
            typeTab == '水质类别'
          "
          ref="chartCapsule"
          :series-data="statisticData"
          :extra-option="extraOption"
          @clickItem="getCountryData"
        />
        <div class="type-chart" v-if="typeTab == '流域'">
          <!-- 总数 -->
          <div class="type-chart-item">
            <div class="chart-item-title cursor" @click="showBas('瓯江流域')">
              瓯<br /><br />江
            </div>
            <div class="chart-item-content">
              <div
                :class="[
                  selectItem == '瓯江正常' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.ojNormalRemark, '瓯江正常')
                "
              >
                <div class="param-item-icon icon-type-two"></div>
                <div class="param-item-data">
                  <p>
                    <strong>{{ normalText }}</strong>
                  </p>
                  <p>
                    <span>{{ statisticObj.ojNormalNum }}</span
                    >个
                  </p>
                </div>
              </div>
              <div
                :class="[
                  selectItem == '瓯江超汛限' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.ojOverRemark, '瓯江超汛限')
                "
              >
                <div class="param-item-icon icon-type-three"></div>
                <div class="param-item-data">
                  <p>
                    <strong>{{ abnormalText }}</strong>
                  </p>
                  <p>
                    <span
                      :style="{
                        color:
                          statisticObj.ojOverNum > 0 ? '#FBE400' : '#54FEF4',
                      }"
                      >{{ statisticObj.ojOverNum }}</span
                    >个
                  </p>
                </div>
              </div>
            </div>
          </div>
          <!-- 飞云江 -->
          <div class="type-chart-item">
            <div class="chart-item-title cursor" @click="showBas('飞云江流域')">
              飞云江
            </div>
            <div class="chart-item-content">
              <div
                :class="[
                  selectItem == '飞云江正常' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(
                    statisticObj.fyjNormalRemark,
                    '飞云江正常'
                  )
                "
              >
                <div class="param-item-icon icon-type-two"></div>
                <div class="param-item-data">
                  <p>
                    <strong>{{ normalText }}</strong>
                  </p>
                  <p>
                    <span>{{ statisticObj.fyjNormalNum }}</span
                    >个
                  </p>
                </div>
              </div>
              <div
                :class="[
                  selectItem == '飞云江超汛限' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(
                    statisticObj.fyjOverRemark,
                    '飞云江超汛限'
                  )
                "
              >
                <div class="param-item-icon icon-type-three"></div>
                <div class="param-item-data">
                  <p>
                    <strong>{{ abnormalText }}</strong>
                  </p>
                  <p>
                    <span
                      :style="{
                        color:
                          statisticObj.fyjOverNum > 0 ? '#FBE400' : '#54FEF4',
                      }"
                      >{{ statisticObj.fyjOverNum }}</span
                    >个
                  </p>
                </div>
              </div>
            </div>
          </div>
          <!-- 鳌江 -->
          <div class="type-chart-item">
            <div class="chart-item-title cursor" @click="showBas('鳌江流域')">
              鳌<br /><br />江
            </div>
            <div class="chart-item-content">
              <div
                :class="[
                  selectItem == '鳌江正常' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.ajNormalRemark, '鳌江正常')
                "
              >
                <div class="param-item-icon icon-type-two"></div>
                <div class="param-item-data">
                  <p>
                    <strong>{{ normalText }}</strong>
                  </p>
                  <p>
                    <span>{{ statisticObj.ajNormalNum }}</span
                    >个
                  </p>
                </div>
              </div>
              <div
                :class="[
                  selectItem == '鳌江超汛限' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.ajOverRemark, '鳌江超汛限')
                "
              >
                <div class="param-item-icon icon-type-three"></div>
                <div class="param-item-data">
                  <p>
                    <strong>{{ abnormalText }}</strong>
                  </p>
                  <p>
                    <span
                      :style="{
                        color:
                          statisticObj.ajOverNum > 0 ? '#FBE400' : '#54FEF4',
                      }"
                      >{{ statisticObj.ajOverNum }}</span
                    >个
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="type-chart" v-if="typeTab == '类别'">
          <!-- 总数 -->
          <div class="type-chart-item">
            <div class="chart-item-title">总<br /><br />数</div>
            <div class="chart-item-content">
              <div
                :class="[
                  selectItem == '水位站总数' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  waterStationsBody(
                    statisticObj.stationRemark,
                    'RR',
                    '水位站总数'
                  )
                "
              >
                <div class="param-item-icon icon-type-two"></div>
                <div class="param-item-data">
                  <p><strong>水位站</strong></p>
                  <p>
                    <span>{{ statisticObj.stationNum }}</span
                    >个
                  </p>
                </div>
              </div>
              <div
                :class="[
                  selectItem == '水库总数' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.rsvrNumRemark, '水库总数')
                "
              >
                <div class="param-item-icon icon-type-one"></div>
                <div class="param-item-data">
                  <p><strong>水库</strong></p>
                  <p>
                    <span>{{ statisticObj.rsvrNumNum }}</span
                    >个
                  </p>
                </div>
              </div>
            </div>
          </div>
          <!-- 大中型 -->
          <div class="type-chart-item">
            <div class="chart-item-title">大中型</div>
            <div class="chart-item-content">
              <div
                :class="[
                  selectItem == '大中型正常' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(
                    statisticObj.lmNormalRemark,
                    '大中型正常'
                  )
                "
              >
                <div class="param-item-icon icon-type-four"></div>
                <div class="param-item-data">
                  <p><strong>正常</strong></p>
                  <p>
                    <span>{{ statisticObj.lmNormalNum }}</span
                    >个
                  </p>
                </div>
              </div>
              <div
                :class="[
                  selectItem == '大中型超汛限' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(
                    statisticObj.lmOverRemark,
                    '大中型超汛限'
                  )
                "
              >
                <div class="param-item-icon icon-type-three"></div>
                <div class="param-item-data">
                  <p><strong>超汛限数</strong></p>
                  <p>
                    <span
                      :style="{
                        color:
                          statisticObj.lmOverNum > 0 ? '#FBE400' : '#54FEF4',
                      }"
                      >{{ statisticObj.lmOverNum }}</span
                    >个
                  </p>
                </div>
              </div>
            </div>
          </div>
          <!-- 小型 -->
          <div class="type-chart-item">
            <div class="chart-item-title">小<br /><br />型</div>
            <div class="chart-item-content">
              <div
                :class="[
                  selectItem == '小型正常' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.smNormalRemark, '小型正常')
                "
              >
                <div class="param-item-icon icon-type-four"></div>
                <div class="param-item-data">
                  <p><strong>正常</strong></p>
                  <p>
                    <span>{{ statisticObj.smNormalNum }}</span
                    >个
                  </p>
                </div>
              </div>
              <div
                :class="[
                  selectItem == '小型超汛限' ? 'active' : '',
                  'param-item',
                ]"
                @click="
                  rsvrNewestWaterLevel(statisticObj.smOverRemark, '小型超汛限')
                "
              >
                <div class="param-item-icon icon-type-three"></div>
                <div class="param-item-data">
                  <p><strong>超汛限数</strong></p>
                  <p>
                    <span
                      :style="{
                        color:
                          statisticObj.smOverNum > 0 ? '#FBE400' : '#54FEF4',
                      }"
                      >{{ statisticObj.smOverNum }}</span
                    >个
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 雨情条件筛选 -->
        <div
          class="type-chart"
          v-if="typeTab == '条件选择' && stationType == '雨情'"
        >
          <div class="custom-filter">
            <div class="filter-item">
              <span class="filter-label">影响温州</span>
              <div>
                <el-checkbox-group
                  @change="getRainStations"
                  v-model="influenceCheck"
                >
                  <el-checkbox label="水文遥测"></el-checkbox>
                  <el-checkbox label="市县自建"></el-checkbox>
                </el-checkbox-group>
              </div>
            </div>
            <div class="filter-item">
              <span class="filter-label">降雨历时</span>
              <div>
                <el-radio-group v-model="radioVal" @change="getRainStations">
                  <el-radio :label="60">前1小时</el-radio>
                  <el-radio :label="180">前3小时</el-radio>
                  <el-radio :label="360">前6小时</el-radio>
                  <el-radio :label="720">前12小时</el-radio>
                  <el-radio :label="1440">前24小时</el-radio>
                  <el-radio :label="2880">前48小时</el-radio>
                </el-radio-group>
              </div>
            </div>
            <div class="filter-item">
              <span class="filter-label">时间范围</span>
              <div>
                <span style="color: #fff; margin-right: 8px">从</span>
                <el-date-picker
                  v-model="customFilter.beginDate"
                  type="date"
                  style="margin-right: 12px"
                  placeholder="日期"
                  :clearable="true"
                  @change="getRainStations"
                />
                <el-time-select
                  v-model="customFilter.beginTime"
                  :picker-options="{
                    start: '00:00',
                    step: '01:00',
                    end: '23:00',
                  }"
                  value-format="HH[时]"
                  placeholder="时间"
                  :clearable="true"
                  @change="getRainStations"
                />
                <br />
                <span style="color: #fff; margin-right: 8px">到</span>
                <el-date-picker
                  v-model="customFilter.endDate"
                  type="date"
                  style="margin-right: 12px"
                  placeholder="日期"
                  :clearable="true"
                  @change="getRainStations"
                />
                <el-time-select
                  v-model="customFilter.endTime"
                  :picker-options="{
                    start: '00:00',
                    step: '01:00',
                    end: '23:00',
                  }"
                  value-format="HH[时]"
                  @change="getRainStations"
                  placeholder="时间"
                  :clearable="true"
                />
              </div>
            </div>
            <div class="filter-item">
              <span class="filter-label">降雨分布</span>
              <div>
                <el-checkbox
                  @change="handleCheckRain"
                  v-model="rainDistribution"
                  >加载对应时段降雨等值面</el-checkbox
                >
              </div>
            </div>
            <div class="filter-item">
              <span class="filter-label">防汛等级</span>
              <div>
                <el-radio-group v-model="floodLevel" @change="getRainStations">
                  <el-radio label="">全部</el-radio>
                  <el-radio label="true">重点</el-radio>
                  <el-radio label="false">一般</el-radio>
                </el-radio-group>
              </div>
            </div>
            <div class="filter-item">
              <span class="filter-label">雨量范围</span>
              <div>
                <el-row class="rainfall-range">
                  <el-select
                    v-model="rainfallRange"
                    placeholder="请选择"
                    @change="getRainStations"
                  >
                    <el-option label="<10" :value="[0, 10]"></el-option>
                    <el-option label="10-25" :value="[10, 25]"></el-option>
                    <el-option label="25-50" :value="[25, 50]"></el-option>
                    <el-option label="50-100" :value="[50, 100]"></el-option>
                    <el-option label="100-250" :value="[100, 250]"></el-option>
                    <el-option label=" > 250" :value="[250, 999]"></el-option>
                    <el-option label="自定义" :value="'0'"></el-option>
                  </el-select>
                </el-row>
              </div>
            </div>
            <!-- <div class="filter-item" style="width: 100%">
              <div style="width: 100%; margin-top: 8px">
                <PrimarySelect @selectHandle="selectHandle"></PrimarySelect>
              </div>
            </div> -->
          </div>
        </div>

        <!-- 水情 工情 条件筛选 -->
        <div
          class="type-chart"
          v-if="
            typeTab == '条件选择' &&
            (stationType == '水库水位' ||
              stationType == '河道水位' ||
              stationType == '堰闸水位' ||
              stationType == '潮位' ||
              stationType == '水闸工情' ||
              stationType == '积水' ||
              stationType == '墒情')
          "
        >
          <div class="filter-item">
            <span class="filter-label">时间：</span>
            <div>
              <el-date-picker
                v-model="waterRegimeradioValDate"
                type="date"
                placeholder="选择日期"
                value-format="yyyy-MM-dd"
                @change="dateTimeChange"
                clearable
              >
              </el-date-picker>
              <el-select
                v-model="waterRegimeradioValTime"
                class="select-time"
                clearable
                @change="dateTimeChange"
              >
                <el-option
                  v-for="item in timeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div class="filter-item" v-if="stationType == '水库水位'">
            <span class="filter-label">是否绑定工程：</span>
            <div>
              <el-radio-group
                v-model="bindProj"
                @change="getWaterStations('水库水位')"
              >
                <el-row>
                  <el-col :span="8">
                    <el-radio :label="null">全部</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="true">是</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="false">否</el-radio>
                  </el-col>
                </el-row>
              </el-radio-group>
            </div>
          </div>
          <div class="filter-item" v-if="stationType == '水库水位'">
            <span class="filter-label">是否主测站：</span>
            <div>
              <el-radio-group
                v-model="main"
                @change="getWaterStations('水库水位')"
              >
                <el-row>
                  <el-col :span="8">
                    <el-radio :label="null">全部</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="true">是</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="false">否</el-radio>
                  </el-col>
                </el-row>
              </el-radio-group>
            </div>
          </div>
          <div class="filter-item" v-if="stationType == '水库水位'">
            <span class="filter-label">水库规模：</span>
            <div>
              <el-radio-group
                v-model="waterRegimeradioVal1"
                @change="getWaterStations('水库水位')"
              >
                <el-row>
                  <el-col :span="8">
                    <el-radio :label="''">全部</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="'大中型'">大中型</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="'小（Ⅰ）型'">小(1)型</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="'小（Ⅱ）型'">小(2)型</el-radio>
                  </el-col>
                </el-row>
              </el-radio-group>
            </div>
          </div>
          <div class="filter-item" v-if="stationType == '河道水位'">
            <span class="filter-label">河道类型：</span>
            <div>
              <el-radio-group
                v-model="waterRegimeradioVal2"
                @change="getWaterStations('河道水位')"
              >
                <el-row>
                  <el-col :span="8">
                    <el-radio :label="''">全部</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="'平原河网'">平原河网</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="'山区河道'">山区河道</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="'其他'">其他</el-radio>
                  </el-col>
                </el-row>
              </el-radio-group>
            </div>
          </div>
          <div
            class="filter-item"
            v-if="
              stationType == '水库水位' ||
              stationType == '河道水位' ||
              stationType == '堰闸水位' ||
              stationType == '潮位'
            "
          >
            <span class="filter-label">防汛等级：</span>
            <div>
              <el-radio-group
                v-model="waterRegimeradioVal3"
                @change="getWaterStations(stationType)"
              >
                <el-row>
                  <el-col :span="8">
                    <el-radio :label="''">全部</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="true">重点</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="false">一般</el-radio>
                  </el-col>
                </el-row>
              </el-radio-group>
            </div>
          </div>
          <div class="filter-item" v-if="stationType == '水闸工情'">
            <span class="filter-label">水闸规模：</span>
            <div>
              <el-radio-group
                v-model="workingConditionRadioVal1"
                @change="getWorkCondition"
              >
                <el-row>
                  <el-col :span="8">
                    <el-radio :label="''">全部</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="'中型'">中型</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="'小（Ⅰ）型'">小(1)型</el-radio>
                  </el-col>
                  <el-col :span="8">
                    <el-radio :label="'小（Ⅱ）型'">小(2)型</el-radio>
                  </el-col>
                </el-row>
              </el-radio-group>
            </div>
          </div>
        </div>

        <!-- 水质条件筛选 -->
        <div
          class="type-chart"
          v-if="typeTab == '条件选择' && stationType == '水质监测'"
        >
          <div class="filter-item">
            <span class="filter-label">站点级别：</span>
            <div>
              <el-select
                v-model="level"
                class="primary-select"
                @change="administrativeDivision()"
              >
                <el-option
                  v-for="item in SiteLevelList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
          </div>
          <!-- 水质类别 -->
          <div class="filter-item" v-if="false">
            <span class="filter-label">水质类别：</span>
            <div>
              <el-select
                v-model="qualityLevel"
                class="primary-select"
                @change="administrativeDivision()"
              >
                <el-option
                  v-for="item in WaterQualityList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
          </div>
        </div>
        <!-- 水质统计 -->
        <div
          class="type-chart"
          v-if="typeTab == '水质统计' && stationType == '水质监测'"
        >
          <div class="filter-item">
            <span class="filter-label">站点级别：</span>
            <div>
              <el-select
                v-model="level"
                class="primary-select"
                @change="administrativeDivision()"
              >
                <el-option
                  v-for="item in SiteLevelList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
          </div>
          <!-- 水质类别 -->
          <div class="filter-item">
            <span class="filter-label">水质类别：</span>
            <div>
              <el-select
                v-model="qualityLevel"
                class="primary-select"
                @change="administrativeDivision()"
              >
                <el-option
                  v-for="item in WaterQualityList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
          </div>
        </div>

        <!-- 流量条件筛选 -->
        <div
          class="type-chart"
          v-if="typeTab == '条件选择' && stationType == '流量'"
        >
          <div class="filter-item">
            <span class="filter-label">时间：</span>
            <div>
              <el-date-picker
                v-model="waterRegimeradioValDate"
                type="date"
                placeholder="选择日期"
                value-format="yyyy-MM-dd"
                @change="dateTimeChange"
                clearable
              >
              </el-date-picker>
              <el-select
                v-model="waterRegimeradioValTime"
                class="select-time"
                clearable
                @change="dateTimeChange"
              >
                <el-option
                  v-for="item in timeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div class="filter-item" style="width: 100%">
            <div style="width: 100%; margin-top: 8px">
              <PrimarySelect @selectHandle="selectHandle"></PrimarySelect>
            </div>
          </div>
        </div>
      </div>
      <i class="split-line icon-split-line1"></i>
    </div>

    <!-- 统计列表 -->
    <div class="statistics-list" ref="statisticsList" :style="listStyle">
      <div class="qualityLevelList" v-if="stationType == '水质监测'">
        <div
          :class="[
            'qualityLevel_li',
            'level_li_' + index,
            { active: WaterQualityActiveIndex == item.index },
          ]"
          v-for="(item, index) in WaterQualityState"
          :key="index"
          @click="changeQualityLevel(item.value, item.index)"
        >
          <div>
            <p>{{ item.label }}</p>
            <p>{{ item.num }}</p>
          </div>
        </div>
      </div>
      <div class="statistics-list-top" ref="statisticsListTop">
        <!-- 全部/超警 -->
        <div
          class="type-area"
          v-if="
            stationType == '水库水位' ||
            stationType == '河道水位' ||
            stationType == '堰闸水位' ||
            stationType == '潮位' ||
            stationType == '积水' ||
            stationType == '雨情' ||
            stationType == '水闸工情'
          "
        >
          <div
            :class="countTypeTab == '全部' ? 'active' : ''"
            class="tabList"
            @click="countTypeTabChange('全部')"
          >
            <div>全部 {{ allCount }}</div>
          </div>
          <div
            v-if="stationType == '雨情'"
            :class="countTypeTab == '降雨' ? 'active' : ''"
            class="tabList"
            @click="countTypeTabChange('降雨')"
          >
            <div>
              降雨
              {{ warningCount }}
            </div>
          </div>
          <div
            v-if="stationType != '雨情' && stationType != '水闸工情'"
            :class="countTypeTab == '超警' ? 'active' : ''"
            class="tabList"
            @click="countTypeTabChange('超警')"
          >
            <div>
              {{ stationType == "积水" ? "积水" : "超警" }} {{ warningCount }}
            </div>
          </div>
          <div
            v-if="stationType == '水闸工情'"
            :class="countTypeTab == '开启' ? 'active' : ''"
            class="tabList"
            @click="countTypeTabChange('开启')"
          >
            <div>开启 {{ warningCount }}</div>
          </div>
        </div>
        <!-- 泵站工情 -->
        <div class="type-area" v-if="stationType == '泵站工情'">
          <div class="tabList active" @click="countTypeTabChange('全部')">
            <div style="padding: 0 5px">
              全部 <br />
              {{ pumpAllCount }}
            </div>
          </div>
        </div>
        <!-- 搜索 -->
        <div class="search-area">
          <div class="left-area">
            <el-input
              @input="getStationListByType"
              v-model="searchVal"
              placeholder="请输入关键字查询"
            >
              <template slot="prepend">
                <el-button slot="append" icon="el-icon-search"></el-button>
              </template>
            </el-input>
          </div>
        </div>
        <!-- 详表按钮 -->
        <div
          class="right-area"
          @click="lookDetail"
          v-if="
            stationType == '水库水位' ||
            stationType == '河道水位' ||
            stationType == '堰闸水位' ||
            stationType == '潮位' ||
            stationType == '水闸工情' ||
            stationType == '雨情' ||
            stationType == '流量'
          "
        >
          <i class="icon-liebiao-detail"></i>
        </div>
      </div>
      <div class="table-list" :style="tableListStyle">
        <screen-table
          :columnList="tableColumns"
          :dataList="dataList"
          :pageNum="pageNum"
          :pageSize="pageSize"
          @rowClick="rowClick"
          :tableHeight="tableHeight"
          :loading="tbLoading"
        ></screen-table>
        <!-- <primary-map-table 
          :columnList="tableColumns"
          :dataList="dataList"
          @rowClick="rowClick"
          :paginationFlag="true"
        ></primary-map-table> -->
      </div>
      <el-pagination
        small
        v-if="stationType == '视频'"
        class="el-pagination-sty"
        style="margin-top: 20px; color: #fff"
        @current-change="pageChange"
        background
        layout="total, prev, pager, next"
        :total="total"
      />
    </div>

    <!-- 详情弹框 -->
    <primary-dialog
      mainTitle="详表"
      :visible="detailDialog"
      @handleClose="handleDetailClose"
      width="50%"
      :hasAnalysis="false"
      :appendToBody="true"
    >
      <div class="dialog-content scope-content">
        <station-detail-dialog
          v-if="detailDialog"
          :stationType="stationType"
        ></station-detail-dialog>
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import { dispatchApi } from "@/api/dataScreen/jointDispatch/JointDispatch";
import { ScreenTable } from "@/components/";
import StationDetailDialog from "../dialog/StationDetailDialog";
import DetailTable from "../dialog/DetailTable";
import { getEquivalentSurface } from "./equivalentSurface";
import { PrimaryDialog, PrimarySelect } from "@/components/";
import EchartCapsule from "../charts/echart_capsule/EchartCapsule";
import { mapActions, mapGetters } from "vuex";
export default {
  name: "station-side",
  data() {
    return {
      // 站点级别
      SiteLevelList: [
        {
          label: "国控",
          value: "1",
        },
        {
          label: "省控",
          value: "2",
        },
        {
          label: "市控",
          value: "3",
        },
        {
          label: "县控",
          value: "4",
        },
        {
          label: "乡镇控",
          value: "5",
        },
      ],
      // 水质类别统计
      WaterQualityState: [],
      WaterQualityActiveIndex: null,
      // 水质类别
      WaterQualityList: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "Ⅰ类",
          value: "1",
        },
        {
          label: "Ⅱ类",
          value: "2",
        },
        {
          label: "Ⅲ类",
          value: "3",
        },
        {
          label: "Ⅳ类",
          value: "4",
        },
        {
          label: "Ⅴ类",
          value: "5",
        },
        {
          label: "劣Ⅴ类",
          value: "6",
        },
      ],
      qualityLevel: "",
      level: "",
      //条件选择
      radioVal: 60,
      pumpAllCount: "",
      customFilter: {
        beginDate: new Date(new Date().getTime() - 24 * 60 * 60 * 1000),
        endDate: "",
        beginTime: "08:00",
        endTime: "",
      },
      rainfallRange: [0, 10],
      rainfallRangeMin: "",
      rainfallRangMax: "",
      statisticData: [], //统计图数据
      extraOption: {},
      searchVal: "",
      tableHeight: 0,
      tableColumns: [
        {
          prop: "stName",
          label: "名称",
        },
      ],
      dataList: [],
      tableListStyle: {}, //表格样式
      listStyle: {}, //表格父容器样式
      typeTab: "条件选择",
      stationType: "水库水位",
      stationObj: {
        水库水位: "RR",
        河道水位: "ZZ",
        水闸工情: "DD",
        堰闸水位: "DD",
        潮位: "TT",
        雨情: "PP",
      },
      basinName: "", //选中流域字段
      areaCode: "", //选中行政区划
      statisticObj: {},
      selectItem: "水位站总数",
      areaCodeObj: {}, //行政区划和编码对应对象 用在按区域查询水库
      normalText: "正常",
      abnormalText: "超汛限",

      timeOptions: [], // 时间选择
      waterRegimeradioValDate: "", // 日期
      waterRegimeradioValTime: "", // 时间
      waterRegimeradioVal1: "", // 水库规模
      bindProj: null, // 是否绑定工程
      main: null, // 是否为主测站
      waterRegimeradioVal2: "", // 河道类型
      waterRegimeradioVal3: "", // 防汛等级
      workingConditionRadioVal1: "", // 水闸规模
      areaVal: "", // 行政区划
      areaoOptions: [
        {
          value: "",
          label: "全部",
        },
      ],
      cameraList: {},
      pageNum: 1,
      pageSize: 10,
      total: 0,
      cameraData: [],
      checkedShow: false,
      countTypeTab: "全部", // 简表 全部 超警 切换
      allCount: 0, // 全部数量
      warningCount: 0, // 超警数量
      detailDialog: false,
      influenceCheck: ["水文遥测", "市县自建"],
      floodLevel: "",
      rainDistribution: "",
      areaType: "",
      areaTypeVal: "",
      tbLoading: false,
      chartLoading: false,
      pumpStationDate: "",
    };
  },
  components: {
    ScreenTable,
    // PrimaryMapTable,
    EchartCapsule,
    StationDetailDialog,
    DetailTable,
    PrimarySelect,
    PrimaryDialog,
  },
  computed: {
    ...mapGetters(["MapComponent"]),
    customFilterStartTime() {
      if (this.customFilter.beginDate && this.customFilter.beginTime) {
        return (
          new Date(this.customFilter.beginDate).format("yyyy-MM-dd") +
          " " +
          this.customFilter.beginTime
        );
      } else {
        return "";
      }
    },
    customFilterEndTime() {
      if (this.customFilter.endDate && this.customFilter.endTime) {
        return (
          new Date(this.customFilter.endDate).format("yyyy-MM-dd") +
          " " +
          this.customFilter.endTime
        );
      } else {
        return "";
      }
    },
    chartStyle() {
      let obj = {
        height: "auto",
      };
      if (this.typeTab == "行政区域") {
        obj = {
          height: "280px",
        };
      }
      return obj;
    },
    listTitle() {
      let text = "统计列表";
      if (this.areaCode) {
        text = text + "(" + this.areaCode + ")";
      } else {
        text = "统计列表";
      }
      return text;
    },
    mapObj() {
      return this.$store.state.hydraulicAmap.mapComponent;
    },
    connectDate() {
      let dateTime = "";
      if (!this.waterRegimeradioValDate || !this.waterRegimeradioValTime) {
        dateTime = "";
      } else {
        dateTime =
          this.waterRegimeradioValDate + " " + this.waterRegimeradioValTime;
      }
      return dateTime;
    },
  },
  watch: {
    $route: {
      handler: function (val, oldVal) {
        this.calcDomHeight();
        this.calcTableHeight();
        let type = val.query.type ? val.query.type : "";
        // console.log(556,type);
        this.stationType = type;
        this.initSideStation(type);
        this.typeTab = "条件选择";

        // this.waterRegimeradioVal1 = "";
        // this.waterRegimeradioVal2 = "";
        // this.waterRegimeradioVal3 = "";
        // this.countTypeTab = "全部";
        // this.waterRegimeradioValDate = "";
        // this.waterRegimeradioValTime = "";
        this.resetParam();
      },
      // 深度观察监听
      deep: true,
    },
    "$store.state.checkedShow": function (val) {
      this.checkedShow = val;
      this.getCameraOpsStations();
    },
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  mounted() {
    this.calcDomHeight();
    this.calcTableHeight();
    this.setTime();
    this.getAdcdList();
    this.cameraStatusByAdcd4Amap();
    if (this.$route.query.type == "泵站工情") {
      this.typeTab = "条件选择";
    }
    this.stationType = this.$route.query.type;
    this.initSideStation(this.stationType);
    window.addEventListener("resize", this.handleWindowResize);
  },
  methods: {
    /**
     * 等值面切换
     */
    handleCheckRain(val) {
      if (val) {
        this.sideLoading = true;
        this.showRealTimeRainfull();
      } else {
        this.MapComponent.removeLayerByName("isolineLayer");
      }
    },
    //加载等值面
    showRealTimeRainfull() {
      let startTime = "",
        endTime = "";
      console.log(this.customFilterEndTime, "5456545645");
      if (this.customFilterStartTime && this.customFilterEndTime) {
        startTime =
          this.customFilterStartTime == ""
            ? ""
            : this.customFilterStartTime + ":00";
        endTime =
          this.customFilterEndTime == ""
            ? ""
            : this.customFilterEndTime + ":00";
      } else {
        endTime = this.$moment().format("yyyy-MM-DD HH:00:00");
        startTime = this.$moment()
          .subtract(this.radioVal, "minutes")
          .format("yyyy-MM-DD HH:00:00");
      }

      getEquivalentSurface({ startTime, endTime }).then((layer) => {
        this.MapComponent.map.addLayer(layer);
        this.sideLoading = false;
      });
    },
    // 视频统计接口
    async cameraStatusByAdcd4Amap() {
      let res = await hydraulicApi.cameraStatusByAdcd4Amap({ sortType: "4" });
      if (res.code == 0) {
        this.cameraList = res.data;
        this.cameraData = res.data;
      }
    },
    // 设置时间筛选的 时 部分
    setTime() {
      for (let i = 0; i < 24; i++) {
        this.timeOptions.push({
          label: i < 10 ? "0" + i + " 时" : i + " 时",
          value: i < 10 ? "0" + i + ":00:00" : i + ":00:00",
        });
      }
    },
    // 获取行政区
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
    //计算表格父容器高度
    calcDomHeight() {
      this.$nextTick(() => {
        const height =
          this.$refs.stationSide.offsetHeight -
          this.$refs.statisticsChart.offsetHeight -
          16 +
          "px";
        // console.log(this.$refs.statisticsChart.offsetHeight, 666);

        this.listStyle = {
          height,
        };
      });
    },
    //计算表格高度
    calcTableHeight() {
      this.$nextTick(() => {
        setTimeout(() => {
          this.tableHeight =
            this.$refs.statisticsList.offsetHeight -
            this.$refs.statisticsListTop.offsetHeight -
            50;
          this.stationType == "水质监测" ? (this.tableHeight -= 148) : "";
          this.tableListStyle = {
            height: this.tableHeight + "px",
          };
        }, 100);
      });
    },
    handleWindowResize() {
      this.calcDomHeight();
      this.calcTableHeight();
    },
    //重置筛选条件
    resetParam() {
      this.basinName = "";
      this.areaCode = "";
      this.mapObj.resetMap();
      this.mapObj.clearAreaLayer();
      this.mapObj.clearBasinLayer();

      this.waterRegimeradioVal1 = "";
      this.waterRegimeradioVal2 = "";
      this.waterRegimeradioVal3 = "";
      this.countTypeTab = "全部";
      // this.getProjectList();
      this.waterRegimeradioValDate = "";
      this.waterRegimeradioValTime = "";
      this.workingConditionRadioVal1 = "";
      this.areaVal = "";
      this.searchVal = "";
      this.bindProj = null;
      this.main = null;
      this.reloadTabList();
    },
    rowClick(data) {
      let lng =
        data.longitude ||
        data.lowLeftLong ||
        data.dtMidLong ||
        data.startLong ||
        data.pustLong ||
        data.startLong ||
        data.hystLong ||
        data.stLong;
      let lat =
        data.latitude ||
        data.lowLeftLat ||
        data.dtMidLat ||
        data.startLat ||
        data.pustLat ||
        data.startLat ||
        data.hystLat ||
        data.stLat;
      if (lng && lat) {
        this.mapObj.setView(lng, lat);
      }
      this.$parent.$parent.$parent.$refs.map.$emit("showDialog", data);
    },
    //获取水库工程
    async getProjectList(areaCode) {
      const opt = {
        type: "水库",
        stationName: this.searchVal,
        areaCode: areaCode,
      };
      this.tableColumns = [
        {
          prop: "resName",
          label: "名称",
        },
      ];
      let arr = [
        {
          prop: "engScal",
          label: "工程规模",
          sortable: true,
        },
        {
          prop: "totCap",
          label: "总库容(万m³)",
          sortable: true,
        },
      ];
      arr.forEach((item) => {
        this.tableColumns.push(item);
      });
      this.tbLoading = true;
      let res = await hydraulicApi.getProjects(opt);
      res.data.map((item) => {
        item.totalVol = item.totalVol || "-";
      });
      this.dataList = res.data;
      this.tbLoading = false;
      // console.log( this.$parent.showProjectPoint)
      // if(!unloadPoint){
      //   this.mapObj.showProjectPoint(this.dataList,this.projectType);
      // }
    },
    //统计分析tab改变 -------------分类统计
    typeTabChange(type) {
      if (
        this.stationType == "水质监测" &&
        (type == "行政区域" || type == "水质类别" || type == "站点级别")
      ) {
        if (type == "行政区域") {
          this.waterQualityStatistic("1");
        } else if (type == "站点级别") {
          this.waterQualityStatistic("2");
        } else if (type == "水质类别") {
          this.waterQualityStatistic("3");
        }
      } else if (this.stationType == "视频") {
        if (type == "行政区域") {
          this.statsCameraByAdcd();
        } else if (type == "在线率") {
          this._statsCameraByAdcd();
        }
      }
      this.resetParam();
      this.typeTab = type;
      this.abnormalText = "超汛限";
      this.normalText = "正常";
      this.calcDomHeight();
      this.calcTableHeight();
      // this.reloadTabList();
    },
    // 根据stationType和typeTab重新获取内容
    reloadTabList() {
      this.getStationListByType(this.stationType);
      this.cameraStatusByAdcd4Amap();
      // if (this.stationType == "视频") {
      //   if (this.typeTab == "行政区域") {
      //     this.statsCameraByAdcd();
      //   } else if (this.typeTab == "在线率") {
      //     this._statsCameraByAdcd();
      //   }
      //   {
      //     this.abnormalText = "离线";
      //     this.normalText = "在线";
      //     this.statsCameraByBasin();
      //   }
      // }
      if (this.stationType == "视频") {
        // if (this.typeTab == "行政区域") {
        //   this.statsCameraByAdcd();
        // } else if (this.typeTab == "在线率") {
        //   this._statsCameraByAdcd();
        // }
        {
          this.abnormalText = "离线";
          this.normalText = "在线";
          // this.statsCameraByBasin();
        }
      }
      // if (this.stationType == "视频") {
      //   if (this.typeTab == "在线率") {
      //     this._statsCameraByAdcd();
      //   } else {
      //     this.statsCameraByBasin();
      //   }
      // }
    },
    initSideStation(type) {
      this.tbLoading = true;
      switch (type) {
        case "水库水位":
          {
            this.stationType = type;
            this.getWaterStations(type);
            if (this.typeTab == "行政区域")
              this.projStatStatistic("水库", "区域");
          }
          break;
        case "河道水位":
        case "堰闸水位":
        case "潮位":
          {
            this.stationType = type;
            // this.typeTab = "行政区域";
            this.getWaterStations(type);
            // if (this.typeTab == "行政区域")
            //   this.attStBaseCountByType("行政区域", this.stationObj[type]);
          }
          break;
        case "水闸工情":
          {
            this.stationType = type;
            // this.typeTab = "行政区域";
            this.getWorkCondition();
            // if (this.typeTab == "行政区域")
            //   this.attStBaseCountByType("行政区域", this.stationObj[type]);
          }
          break;
        case "国控断面":
        case "省控断面":
        case "水质监测":
          {
            this.stationType = type;
            this.typeTab = "条件选择";
            this.waterQualityStatistic("1");
            this.getWaterQualityStations();
          }
          break;
        case "雨情":
          {
            this.getRainStations(type);
            this.attStBaseCountByType("行政区域", "PP");
          }
          break;
        case "积水":
          {
            this.floodPointStatistic();
            this.getWaterLoggingStations();
          }
          break;
        case "视频":
          {
            this.getCameraOpsStations(type);
            this.statsCameraByAdcd();
          }
          break;
        case "墒情":
          {
            this.stationType = type;
            this.typeTab = "条件选择";
            this.getSoilMoistureContent(type);
          }
          break;
        case "流量":
          {
            this.typeTab = "条件选择";
            this.mountainRiverWaterLevel();
          }
          break;
        case "泵站工情":
          {
            this.pumpStationLevel();
          }
          break;
      }
    },
    // 泵站工情
    async pumpStationLevel() {
      this.tbLoading = true;
      let opt = {
        stationType: "PT",
        stationName: this.searchVal,
        dataTime: this.pumpStationDate,
      };
      this.tableColumns = [
        {
          prop: "stName",
          label: "名称",
        },
        {
          prop: "time",
          label: "更新时间",
        },
        {
          prop: "jskZ",
          label: "进水口水位(m)",
        },
        {
          prop: "jscZ",
          label: "出水口水位(m)",
        },
        {
          prop: "cscZ",
          label: "出水池水位(m)",
        },
        {
          prop: "ll",
          label: "流量(m³/s)",
        },
      ];
      let res = await dispatchApi.waterStations(opt);
      if (res.code == 0) {
        this.dataList = res.data;
        this.pumpAllCount = res.data.length;
        this.tbLoading = false;
      }
      this.tbLoading = false;
    },
    // 分页
    pageChange(page) {
      this.pageNum = page;
      this.getStationListByType();
    },
    //根据类型获取测站列表 搜索
    getStationListByType() {
      switch (this.stationType) {
        case "水库水位":
          {
            if (this.typeTab == "类别") {
              this.waterStationsBody(
                this.statisticObj.stationRemark,
                "RR",
                "水位站总数",
                "isSearch"
              );
            } else {
              this.getWaterStations(this.stationType, "isSearch");
            }
          }
          break;
        case "河道水位":
        case "堰闸水位":
        case "潮位":
          {
            this.getWaterStations(this.stationType, "isSearch");
          }
          break;
        case "水闸工情":
          this.getWorkCondition("isSearch");
          break;
        case "泵站工情":
          {
            this.pumpStationLevel();
          }
          break;
        case "水质监测":
          {
            this.getWaterQualityStations();
          }
          break;
        case "雨情":
          {
            this.getRainStations(this.stationType);
          }
          break;
        case "积水":
          {
            this.getWaterLoggingStations("isSearch");
          }
          break;
        case "视频":
          {
            this.getCameraOpsStations(this.stationType);
          }
          break;
        case "墒情":
          {
            this.getSoilMoistureContent(this.stationType);
          }
          break;
      }
    },
    //水库测站统计
    async projStatStatistic(type, dimension) {
      let opt = {
        type: type,
        dimension: dimension,
      };
      this.chartLoading = true;
      if (dimension == "类型") {
        let res = await hydraulicApi.projStatStatistic(opt);
        this.statisticObj = res.data;
        this.waterStationsBody(
          this.statisticObj.stationRemark,
          "RR",
          "水位站总数"
        );
      } else if (dimension == "流域") {
        let res = await hydraulicApi.projStatStatistic(opt);
        this.statisticObj = res.data;
        this.rsvrNewestWaterLevel(this.statisticObj.ojNormalRemark, "瓯江正常");
      } else {
        let opt2 = {
          type: "行政区域",
          stType: "RR",
        };
        let res2 = await hydraulicApi.attStBaseCountByType1(opt2);
        this.statisticData = [];
        let xArr = [];
        let seriesArr = [];
        let overNumArr = []; //超汛限数目
        let normalNumArr = []; //正常数目
        res2.data.forEach((item) => {
          const x = item.countKey || item.key;
          xArr.push(x);
          const y = item.countNum || item.value;
          this.statisticData.unshift(y);
          //暂存totalRemark  点击柱子查列表时要用到
          // this.areaCodeObj[x] = item.value.totalRemark;
          // overNumArr.push(item.value.overNum);
          // normalNumArr.push(item.value.normalNum);
        });
        xArr = xArr.reverse();
        overNumArr = overNumArr.reverse();
        normalNumArr = normalNumArr.reverse();
        seriesArr = [
          {
            name: "超汛限",
            type: "bar",
            stack: "total",
            label: {
              show: false,
            },
            emphasis: {
              focus: "series",
            },
            data: overNumArr,
            itemStyle: {
              barBorderRadius: [10, 10, 10, 10],
              borderWidth: 2,
              borderColor: "#ccc",
            },
          },
          {
            name: "正常",
            type: "bar",
            stack: "total",
            label: {
              show: false,
            },
            emphasis: {
              focus: "series",
            },
            data: normalNumArr,
            itemStyle: {
              barBorderRadius: [10, 10, 10, 10],
              borderWidth: 2,
              borderColor: "#ccc",
            },
          },
        ];
        let obj = {
          yAxis: {
            data: xArr,
          },
          // series: seriesArr,
          // color: ["#F53526", "#37A2DA"],
        };
        this.extraOption = { ...obj };
        this.$refs.chartCapsule.updateChartView();
        this.rsvrNewestWaterLevel("", "");
        this.$nextTick(() => {
          this.calcTableHeight();
        });
      }
      this.chartLoading = false;
    },
    //监测站分类 统计
    async attStBaseCountByType(type, stType) {
      let opt = {
        type,
        stType,
      };
      if (stType == "DD") {
        if (this.stationType == "堰闸水位") {
          // opt.isWrp = false;
        } else {
          opt.isWrp = true;
        }
      }
      let res = await hydraulicApi.attStBaseCountByType(opt);
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

        this.$refs.chartCapsule.updateChartView();
        this.$nextTick(() => {
          this.calcTableHeight();
        });
      }
      // console.log(res);
    },
    //按类型水库列表
    async rsvrNewestWaterLevel(resCodes, activeType) {
      if (resCodes) {
        this.selectItem = activeType;
        this.tableColumns = [];
        const arr = [
          {
            prop: "resName",
            label: "名称",
          },
          {
            prop: "waterLevel",
            label: "水位(m)",
            sortable: true,
          },
          {
            prop: "meiFloodLimitWaterLevel",
            label: "汛限水位(m)",
            sortable: true,
          },
        ];
        arr.map((item) => {
          this.tableColumns.push(item);
        });
        if (resCodes == "") {
          this.dataList = [];
        } else {
          let opt = {
            resCodes: resCodes,
            warning: false,
          };

          this.tbLoading = true;
          let res1 = await hydraulicApi.rsvrNewestWaterLevel(opt);
          if (this.countTypeTab == "全部") this.dataList = res1.data;
          this.tbLoading = false;
          // this.allCount = res1.data.length;

          // opt.warning = true;
          // let res2 = await hydraulicApi.rsvrNewestWaterLevel(opt);
          // if (this.countTypeTab == "超警") this.dataList = res2.data;
          // this.warningCount = res2.data.length;
        }
      }
      // console.log(res.data);
    },
    //获取水位站列表
    async waterStationsBody(
      stCodes,
      stationType,
      activeType,
      isSearch = false
    ) {
      this.selectItem = activeType;
      this.tableColumns = [];
      const arr = [
        {
          prop: "stName",
          label: "名称",
        },
        {
          prop: "waterLevel",
          label: "水位(m)",
          sortable: true,
        },
        {
          prop: "time",
          label: "更新时间",
          sortable: true,
          width: "220",
        },
      ];
      arr.map((item) => {
        this.tableColumns.push(item);
      });
      let opt = {
        stCodes: stCodes,
        stationType: stationType,
        warning: false,
      };

      this.tbLoading = true;
      let res1 = await hydraulicApi.waterStationsBody(opt);
      if (this.countTypeTab == "全部") this.dataList = res1.data;
      if (!isSearch) this.allCount = res1.data.length;
      // this.tbLoading = false

      // this.tbLoading = true
      opt.warning = true;
      let res2 = await hydraulicApi.waterStationsBody(opt);
      if (this.countTypeTab == "超警") this.dataList = res2.data;
      if (!isSearch) this.warningCount = res2.data.length;
      this.tbLoading = false;
    },
    //水情  isSearch: 是否是输入搜索时获取，输入搜索时不进行总数/超警赋值
    async getWaterStations(type, isSearch = false) {
      this.tbLoading = true;
      const opt = {
        stationType: "RR",
        dataTime: this.connectDate,
        isFocus: this.waterRegimeradioVal3, // 防汛等级
        riverStationType: this.waterRegimeradioVal2, // 河道类型
        resGrade: "",
        stationName: this.searchVal,
        basinCode: this.basinName,
        areaCode: this.areaCode,
        main: this.main,
        bindProj: this.bindProj,
      };

      if (this.waterRegimeradioVal1) opt.engScal = this.waterRegimeradioVal1; // 水库规模为‘全部’时不传engScal

      if (type == "堰闸水位") {
        this.tableColumns = [
          {
            prop: "stName",
            label: "名称",
            width: 90,
          },
          {
            prop: "upz",
            label: "闸上水位(m)",
            sortable: true,
            width: "125",
          },
          {
            prop: "dwz",
            label: "闸下水位(m)",
            sortable: true,
            width: "125",
          },
          {
            prop: "time",
            label: "更新时间",
            sortable: true,
            width: "180",
          },
        ];
      } else if (type == "潮位") {
        this.tableColumns = [
          {
            prop: "stName",
            label: "名称",
            width: 120,
          },
          {
            prop: "tdz",
            label: "潮位(m)",
            sortable: true,
            width: "100",
          },
          {
            prop: "time",
            label: "更新时间",
            sortable: true,
            width: "220",
          },
        ];
      } else {
        this.tableColumns = [
          {
            prop: "stName",
            label: "名称",
            width: 120,
          },
          {
            prop: "waterLevel",
            label: "水位(m)",
            sortable: true,
            width: "100",
          },
          {
            prop: "time",
            label: "更新时间",
            sortable: true,
            width: "220",
          },
        ];
      }
      switch (type) {
        case "水库水位":
          {
            opt.stationType = "RR";
            opt.resGrade = "";
          }
          break;
        case "河道水位":
          {
            opt.stationType = "ZZ";
          }
          break;
        case "堰闸水位":
          {
            opt.stationType = "DD";
          }
          break;
        case "潮位":
          {
            opt.stationType = "TT";
          }
          break;
      }

      let res1 = await hydraulicApi.waterStations(opt);
      if (this.countTypeTab == "全部") this.dataList = res1.data;
      if (!isSearch) this.allCount = res1.data.length;

      opt.warning = true;
      let res2 = await hydraulicApi.waterStations(opt);
      if (this.countTypeTab == "超警") this.dataList = res2.data;
      if (!isSearch) this.warningCount = res2.data.length;
      this.tbLoading = false;
    },
    //工情
    async getWorkCondition(isSearch = false) {
      const opt = {
        stationType: "DD",
        dataTime: this.connectDate,
        resGrade: "",
        stationName: this.searchVal,
        basinCode: this.basinName,
        areaCode: this.areaCode,
        isWrp: true,
        openGate: false, // 是否开启，false-全部 true-开启
      };

      if (this.workingConditionRadioVal1)
        opt.engScal = this.workingConditionRadioVal1; // 规模为‘全部’时不传engScal

      this.tableColumns = [
        {
          prop: "stName",
          label: "名称",
        },
        {
          prop: "time",
          label: "更新时间",
          sortable: true,
          width: "220",
        },
        {
          prop: "upz",
          label: "闸上水位(m)",
          sortable: true,
        },
        {
          prop: "dwz",
          label: "闸下水位(m)",
          sortable: true,
        },
        {
          prop: "tdz",
          label: "闸门开度",
          sortable: true,
        },
        {
          prop: "flow",
          label: "过闸流量(m³/s))",
          sortable: true,
        },
      ];

      this.tbLoading = true;

      let res = await hydraulicApi.waterStations(opt);
      if (this.countTypeTab == "全部") this.dataList = res.data;
      if (!isSearch) this.allCount = res.data.length;

      opt.openGate = true;
      let res2 = await hydraulicApi.waterStations(opt);
      if (this.countTypeTab == "开启") this.dataList = res2.data;
      if (!isSearch) this.warningCount = res2.data.length;

      this.tbLoading = false;
    },
    //按流域统计摄像头数量
    async statsCameraByBasin() {
      let res = await hydraulicApi.statsCameraByBasin();
      if (res.code == 0) {
        const data = res.data;
        this.statisticObj = {
          ojNormalNum:
            data.onlineNum[0].value >= 0 ? data.onlineNum[0].value : "-",
          ojOverNum:
            data.offlineNum[0].value >= 0 ? data.offlineNum[0].value : "-",
          fyjNormalNum:
            data.onlineNum[2].value >= 0 ? data.onlineNum[2].value : "-",
          fyjOverNum:
            data.offlineNum[2].value >= 0 ? data.offlineNum[2].value : "-",
          ajNormalNum:
            data.onlineNum[1].value >= 0 ? data.onlineNum[1].value : "-",
          ajOverNum:
            data.offlineNum[1].value >= 0 ? data.offlineNum[1].value : "-",
        };
      } else {
        this.statisticObj = {
          ojNormalNum: "-",
          ojOverNum: "-",
          fyjNormalNum: "-",
          fyjOverNum: "-",
          ajNormalNum: "-",
          ajOverNum: "-",
        };
      }
    },
    //按在线率统计摄像头数量
    async _statsCameraByAdcd() {
      let res = await hydraulicApi.cameraStatusByAdcd4Amap({ sortType: "4" });
      this.statisticData = [];
      let xArr = [];
      let seriesArr = [];
      let overNumArr = []; //离线数目
      res.data.statsList.forEach((item) => {
        const x = item.areaName || item.key;
        xArr.push(x);
        overNumArr.push(item.onlineRate);
      });
      xArr = xArr.reverse();
      overNumArr = overNumArr.reverse();
      seriesArr = [
        {
          name: "在线率",
          type: "bar",
          stack: "total",
          label: {
            show: false,
          },
          emphasis: {
            focus: "series",
          },
          data: overNumArr,
          itemStyle: {
            barBorderRadius: [10, 10, 10, 10],
            borderWidth: 2,
            borderColor: "#ccc",
          },
        },
      ];
      let obj = {
        yAxis: {
          data: xArr,
        },
        xAxis: {
          name: "%",
        },
        series: seriesArr,
        color: ["#F53526"],
      };
      this.extraOption = { ...obj };
      this.$nextTick(() => {
        this.calcTableHeight();
        this.$refs.chartCapsule.updateChartView();
      });
    },
    //按行政区划统计摄像头数量
    async statsCameraByAdcd() {
      let res = await hydraulicApi.statsCameraByAdcd();
      this.statisticData = [];
      let xArr = [];
      let seriesArr = [];
      let overNumArr = []; //离线数目
      let normalNumArr = []; //在线数目
      res.data.offlineNum.forEach((item) => {
        const x = item.countKey || item.key;
        xArr.push(x);
        overNumArr.push(item.value);
      });
      res.data.onlineNum.forEach((item) => {
        normalNumArr.push(item.value);
      });
      xArr = xArr.reverse();
      overNumArr = overNumArr.reverse();
      normalNumArr = normalNumArr.reverse();
      seriesArr = [
        {
          name: "离线",
          type: "bar",
          stack: "total",
          label: {
            show: false,
          },
          emphasis: {
            focus: "series",
          },
          data: overNumArr,
          itemStyle: {
            barBorderRadius: [10, 10, 10, 10],
            borderWidth: 2,
            borderColor: "#ccc",
          },
        },
        {
          name: "在线",
          type: "bar",
          stack: "total",
          label: {
            show: false,
          },
          emphasis: {
            focus: "series",
          },
          data: normalNumArr,
          itemStyle: {
            barBorderRadius: [10, 10, 10, 10],
            borderWidth: 2,
            borderColor: "#ccc",
          },
        },
      ];
      let obj = {
        yAxis: {
          data: xArr,
        },
        series: seriesArr,
        color: ["#F53526", "#37A2DA"],
      };
      this.extraOption = { ...obj };
      this.$nextTick(() => {
        this.calcTableHeight();
        this.$refs.chartCapsule.updateChartView();
      });
    },
    //雨情
    async getRainStations(type) {
      if (this.rainfallRange != "0") {
        this.rainfallRangeMin = "";
        this.rainfallRangMax = "";
      }
      let dataSource = null;
      if (this.influenceCheck.length == 2) {
        dataSource = null;
      } else {
        dataSource = this.influenceCheck.includes("水文遥测") ? "1" : "2";
      }
      const areaCode = this.areaType == "area" ? this.areaTypeVal : "";
      const bas = this.areaType == "basin" ? this.areaTypeVal : "";
      const opt = {
        min:
          this.rainfallRange == "0"
            ? this.rainfallRangeMin
            : this.rainfallRange[0],
        max:
          this.rainfallRange == "0"
            ? this.rainfallRangMax
            : this.rainfallRange[1],
        minutes: this.radioVal,
        stationName: this.searchVal,
        dataSource,
        start:
          this.customFilterStartTime == ""
            ? ""
            : this.customFilterStartTime + ":00",
        end:
          this.customFilterEndTime == ""
            ? ""
            : this.customFilterEndTime + ":00",
        isFocus: this.floodLevel,
        areaCode,
        bas,
        warning: this.countTypeTab == "超警" ? true : false,
        isDisplay: true,
      };
      this.tableColumns = [
        {
          prop: "stationName",
          label: "名称",
        },
        {
          prop: "drp",
          label: "雨量(mm)",
          sortable: true,
        },
      ];
      this.tbLoading = true;
      let res = await hydraulicApi.rainStations(opt);
      this.allCount = res.data.length;
      let arr = [];
      res.data.forEach((val) => {
        if (val.drp > 0) {
          arr.push(val);
        }
      });
      if (this.countTypeTab == "降雨") {
        this.dataList = arr;
      } else {
        this.dataList = res.data.map((item) => {
          let data = item.drp;
          if (item.drp && Number(item.drp) < 0) {
            data = "-";
          }
          item.drp = data;
          return item;
        });
      }
      this.warningCount = arr.length;
      this.tbLoading = false;
    },
    //水质统计
    async waterQualityStatistic(type) {
      let res = await hydraulicApi.qualityStats({ type: type });
      if (res.code == 0) {
        this.statisticData = [];
        let xArr = [];
        res.data.forEach((item) => {
          const x = item.countKey || item.key;
          xArr.push(x);
          const y = item.countNum || item.value;
          this.statisticData.unshift(y);
        });
        xArr = xArr.reverse();
        let obj = {
          yAxis: {
            data: xArr,
          },
          xAxis: {
            name: "",
          },
        };
        this.extraOption = { ...obj };

        this.$refs.chartCapsule.updateChartView();
        this.$nextTick(() => {
          this.calcTableHeight();
        });
      }
    },
    //积水统计
    async floodPointStatistic() {
      let res = await hydraulicApi.floodPointStatistic();
      if (res.code == 0) {
        this.statisticData = [];
        let xArr = [];
        res.data.forEach((item) => {
          if (item.countNum > 0 || item.value > 0) {
            const x = item.countKey || item.key;
            xArr.push(x);
            const y = item.countNum || item.value;
            this.statisticData.unshift(y);
          }
        });
        xArr = xArr.reverse();
        let obj = {
          yAxis: {
            data: xArr,
          },
        };
        this.extraOption = { ...obj };

        this.$refs.chartCapsule.updateChartView();
        this.$nextTick(() => {
          this.calcTableHeight();
        });
      }
    },
    changeQualityLevel(val, index) {
      this.WaterQualityActiveIndex == index
        ? (this.qualityLevel = null)
        : (this.qualityLevel = val);
      this.WaterQualityActiveIndex == index
        ? (this.WaterQualityActiveIndex = null)
        : (this.WaterQualityActiveIndex = index);
      this.getWaterQualityStations(true);
    },
    //水质
    async getWaterQualityStations(val) {
      this.tableColumns = [
        {
          prop: "siteName",
          label: "名称",
          width: "140",
        },
        {
          prop: "areaName",
          label: "行政区划",
          width: "140",
        },
        {
          prop: "codmn",
          label: "高锰酸盐指数(mg/L)",
          width: "200",
        },
        {
          prop: "nh3N",
          label: "氨氮(mg/L)",
          width: "120",
        },
        {
          prop: "tp",
          label: "总磷(mg/L)",
          width: "120",
        },
        {
          prop: "cod",
          label: "溶解氧",
          width: "100",
        },
        {
          prop: "qualityLevel",
          label: "水质类别",
          width: "120",
        },
        {
          prop: "tm",
          label: "更新时间",
          width: "120",
        },
      ];
      let opt = {
        areaCode: this.areaCode,
        siteName: this.searchVal,
        qualityLevel: this.qualityLevel,
        level: this.level,
      };
      this.tbLoading = true;
      let res = await hydraulicApi.waterQualityV2(opt);
      this.dataList = res.data.map((item) => {
        item.tm = item.tm && item.tm.substring(0, 10);
        this.WaterQualityList.forEach((sItem) => {
          item.qualityLevel == sItem.value
            ? (item.qualityLevel = sItem.label)
            : "";
        });
        return item;
      });
      if (!val) {
        this.WaterQualityState = [];
        let ress = await hydraulicApi.qualityStats({ type: 3 });
        this.WaterQualityList.forEach((item, index) => {
          ress.data.forEach((sItem) => {
            if (sItem.key == item.label) {
              item.index = index - 1;
              item.num = sItem.value;
              this.WaterQualityState.push(item);
            }
          });
        });
      }
      this.tbLoading = false;
    },
    //积水
    async getWaterLoggingStations(isSearch = false) {
      this.tbLoading = true;
      let opt = {
        name: this.searchVal,
        // name4Search: this.searchVal,
        areaCode: this.areaVal,
        dataTime: this.connectDate, // 数据时间 格式为yyyy-MM-dd HH:mm:ss
        ponding: "", // 是否又积水，为空查询全部，true表示有积水，false标是没有积水
      };
      this.tableColumns = [
        {
          prop: "name",
          label: "名称",
        },
        {
          prop: "waterLevel",
          label: "水深（cm）",
          sortable: true,
        },
      ];

      let res1 = await hydraulicApi.getLogging(opt);
      if (this.countTypeTab == "全部") this.dataList = res1.data;
      if (!isSearch) this.allCount = res1.data.length;

      opt.ponding = true;
      let res2 = await hydraulicApi.getLogging(opt);
      if (this.countTypeTab == "超警") this.total = res2.data.totalRows;
      this.dataList = res2.data;
      if (!isSearch) this.warningCount = res2.data.length;
      this.tbLoading = false;
    },
    //视频
    async getCameraOpsStations(type) {
      let opt = {
        cameraName: this.searchVal,
        adcd: this.areaCode,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      this.tableColumns = [
        {
          prop: "cameraName",
          label: "名称",
        },
        {
          prop: "status",
          label: "状态",
          sortable: true,
        },
      ];
      this.tbLoading = true;
      let res = await hydraulicApi.getCameraOps(opt);
      this.total = res.data.totalRows;
      this.dataList = res.data.list.map((item) => {
        item.status = item.status == 0 ? "离线" : "在线";
        item.stType = "camera";
        return item;
      });
      this.tbLoading = false;
    },
    //统计图单项点击
    async getCountryData(data) {
      let str = this.cameraData.statsList.find((val) => {
        if (val.areaName == data.name) {
          return val;
        }
      });
      let area = this.areaoOptions.find((val) => {
        if (val.label == data.name) {
          return val;
        }
      });
      // this.searchVal = data.name;
      this.cameraList = str;
      this.areaCode = area.value;
      this.mapObj.showAreaLine(data.name);
      switch (this.stationType) {
        case "水库水位":
          {
            // let remark = this.areaCodeObj[this.areaCode];
            // this.rsvrNewestWaterLevel(remark, "");
            this.getWaterStations(this.stationType);
          }
          break;
        case "河道水位":
        case "堰闸水位":
        case "潮位":
        case "水闸工情":
          {
            this.getWorkCondition();
          }
          break;
        case "国控断面":
        case "省控断面":
        case "水质监测":
          {
            // 水质监测列表
            this.getWaterQualityStations();
          }
          break;
        case "雨情":
          {
            this.getRainStations(this.stationType);
          }
          break;
        case "积水":
          {
            this.getWaterLoggingStations();
          }
          break;
        case "视频":
          {
            this.getCameraOpsStations(this.stationType);
          }
          break;
        case "墒情":
          {
            this.getSoilMoistureContent(this.stationType);
          }
          break;
      }
    },
    // 墒情
    async getSoilMoistureContent() {
      let opt = {
        cameraName: this.searchVal,
        adcd: this.areaVal,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        stationType: "SS",
        dataTime: this.connectDate,
      };
      this.tableColumns = [
        {
          prop: "stName",
          label: "名称",
        },
      ];
      let arr = [
        {
          prop: "slm10",
          label: "10cm土壤含水率",
          sortable: true,
        },
        {
          prop: "slm20",
          label: "20cm土壤含水率",
          sortable: true,
        },
        {
          prop: "slm30",
          label: "30cm土壤含水率",
          sortable: true,
        },
      ];
      arr.forEach((item) => {
        this.tableColumns.push(item);
      });
      this.tbLoading = true;
      let res = await hydraulicApi.soilMoistureMonitorStations(opt);
      this.total = res.data.totalRows;
      res.data.map((val) => {
        if (!val.stSoliR) return;
        val.slm10 = val.stSoliR.slm10;
        val.slm20 = val.stSoliR.slm20;
        val.slm30 = val.stSoliR.slm30;
      });
      this.dataList = res.data;
      this.tbLoading = false;
    },
    //展示流域
    async showBas(basName) {
      this.basinName = basName;
      this.mapObj.basinCommand(this.basinName);
    },

    // 时间改变时
    dateTimeChange() {
      if (
        this.stationType == "水库水位" ||
        this.stationType == "河道水位" ||
        this.stationType == "堰闸水位" ||
        this.stationType == "潮位"
      ) {
        this.getWaterStations(this.stationType);
      } else if (this.stationType == "水闸工情") {
        this.getWorkCondition();
      } else if (this.stationType == "积水") {
        this.getWaterLoggingStations();
      } else if (this.stationType == "墒情") {
        this.getSoilMoistureContent();
      } else if (this.stationType == "流量") {
        this.mountainRiverWaterLevel();
      } else if (this.stationType == "泵站工情") {
        this.pumpStationLevel();
      }
    },
    // 行政区划
    administrativeDivision() {
      if (this.stationType == "积水") {
        this.getWaterLoggingStations();
      } else if (this.stationType == "墒情") {
        this.getSoilMoistureContent();
      } else if (this.stationType == "水质监测") {
        this.getWaterQualityStations();
      }
    },
    // 简表 全部/超警 切换
    countTypeTabChange(type) {
      this.countTypeTab = type;
      // this.tbLoading = true;

      switch (this.stationType) {
        case "水库水位":
        case "河道水位":
        case "堰闸水位":
        case "潮位":
          this.getWaterStations(this.stationType);
          break;
        case "积水":
          this.getWaterLoggingStations();
          break;
        case "雨情":
          this.getRainStations(this.stationType);
          break;
        case "水闸工情":
          this.getWorkCondition();
          break;
      }
    },

    selectHandle(val, type, label) {
      this.areaType = type;
      this.areaTypeVal = val;
      if (this.stationType == "流量") {
        this.mountainRiverWaterLevel();
      } else if (this.stationType == "雨情") {
        this.getRainStations();
      }
    },

    //实时流量信息
    async mountainRiverWaterLevel() {
      const arr = [
        {
          prop: "stName",
          label: "名称",
        },
        {
          prop: "flow",
          label: "流量m³/s",
          sortable: true,
        },
        {
          prop: "time",
          label: "更新时间",
          sortable: true,
          width: "220",
        },
      ];
      this.getTableColumn(arr);

      const areaCode = this.areaType == "area" ? this.areaTypeVal : "";
      const bas = this.areaType == "basin" ? this.areaTypeVal : "";
      let opt = {
        dataTime: this.connectDate
          ? this.connectDate
          : new Date().format("yyyy-MM-dd hh:mm:ss"),
        areaCode,
        bas: bas == "全部" ? "" : bas,
      };
      this.tbLoading = true;
      let res = await hydraulicApi.mountainRiverWaterLevel(opt);
      this.dataList = res.data;
      this.tbLoading = false;
    },
    getTableColumn(arr) {
      this.tableColumns = [];
      arr.map((item) => {
        this.tableColumns.push(item);
      });
    },
    //详表弹窗关闭
    handleDetailClose() {
      this.detailDialog = false;
    },
    //查看详表
    lookDetail() {
      this.detailDialog = true;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
.station-side {
  width: 100%;
  height: 100%;
  @include flexbox;
  @include flexflow(column, nowrap);
  .statistics-chart {
    position: relative;
    // flex: 1;
    .title-right {
      position: absolute;
      right: 0;
      .icon-btn-reset {
        cursor: pointer;
        height: 30px;
      }
    }
    .chart-tabs {
      @include flexbox;
      @include flexAC;
      span {
        display: inline-block;
        padding: 4px 8px;
        width: 80px;
        color: #fff;
        border: 1px solid rgba(255, 255, 255, 0.6);
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
    .chart-box {
      padding-top: 8px;
      .split-line {
        display: inline-block;
        width: 100%;
        height: 4px;
      }
      ::v-deep .dv-capsule-chart {
        height: 100%;
      }
      .type-chart {
        padding: 16px 0;
        .type-chart-item {
          @include flexbox;
          @include flexflow(row, nowrap);
          color: #00c1ff;
          margin-bottom: 20px;
          &:last-child {
            margin-bottom: 0;
          }
          .chart-item-title {
            width: 25px;
            letter-spacing: 10px;
            border-right: 1px solid #00c1ff;
            &.cursor {
              cursor: pointer;
            }
          }
          .chart-item-content {
            @include flex(1);
            @include flexbox;
            @include flexflow(row, nowrap);
            padding-left: 18px;
            .param-item {
              width: 49%;
              @include flexbox;
              @include flexAC;
              @include flexflow(row, nowrap);
              cursor: pointer;
              padding: 6px 12px;
              &.active {
                // border: 1px dashed #4CE3FB;
                background: url(~assets/images/param-item-bg.png) no-repeat
                  center center;
                background-size: 100% 100%;
              }
              .param-item-icon {
                width: 43px;
                height: 43px;
              }
              .param-item-data {
                margin-left: 10px;
                p {
                  line-height: 24px;
                  span {
                    font-weight: bold;
                    color: #1cfef4;
                    margin-right: 6px;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  .statistics-list {
    @include flex(2);
    .table-list {
      height: 100%;
      width: 100%;
      ::v-deep .table-wrap .plTableBox {
        margin-top: 0;
      }
    }

    .statistics-list-top {
      display: flex;
      align-items: center;
      margin: 10px 0;
      .type-area {
        display: flex;
        margin-right: 10px;
        .tabList {
          display: flex;
          align-items: center;
          color: #fff;
          border: 1px solid #fff;
          cursor: pointer;
          padding: 0 5px;
          font-size: 14px;
          & > div {
            text-align: center;
            line-height: 20px;
            height: 50px;
            display: flex;
            align-items: center;
          }
        }
        .active {
          color: #56fefe;
          background: rgba(72, 152, 251, 0.1);
          border: 1px solid #56fefe;
          box-shadow: inset 0 0 5px 0 #56fefe;
        }
      }
      .right-area {
        margin-left: 10px;
        cursor: pointer;
      }
    }
  }
}
</style>
<style lang="scss">
.custom-filter {
  .sub-title {
    width: 64px;
    height: 20px;
    font-size: 14px;
    color: #23e3fb;
    position: relative;
    padding-left: 8px;
    &:before {
      content: "";
      position: absolute;
      left: 0;
      width: 3px;
      height: 14px;
      background: #23e3fb;
      border-radius: 15px;
    }
  }
  .filter-time {
    height: 48px;
    display: flex;
    align-items: center;
    > * {
      margin: 0 3px;
    }
    .el-button {
      margin-left: 16px;
    }
  }
  // .el-radio {
  //   margin: 8px 0;
  //   width: 90px;
  //   .el-radio__inner {
  //     background-color: transparent;
  //     border: 1px solid #4694f6;
  //   }
  //   .el-radio__input.is-checked {
  //     .el-radio__inner {
  //       background: transparent;
  //       &:after {
  //         background-color: #1ef8f2;
  //         border-color: #1ef8f2;
  //         transform: translate(-50%, -50%) scale(1.8);
  //       }
  //     }
  //   }
  //   .el-radio__label {
  //     color: #fff;
  //   }
  // }
  .el-date-editor {
    width: 139px;
    .el-input__prefix {
      color: #56fefe;
      left: unset;
      right: 8px;
      .el-input__icon {
        line-height: 1;
      }
    }
    .el-input__suffix {
      .el-input__icon {
        line-height: 1;
      }
    }
    .el-input__inner {
      padding-left: 8px;
      width: 139px;
      height: 32px;
      border: 1px solid #56fefe;
      border-radius: 2px;
    }
    &.el-date-editor--time-select {
      width: 88px;
      .el-input__inner {
        width: 88px;
      }
    }
  }
  .rainfall-range-label {
    color: #fff;
    font-size: 14px;
    height: 30px;
    @include flexbox;
    @include flexAC;
  }
  .rainfall-range {
    display: flex;
    color: #fff;
    justify-content: left;
    // align-items: center;
    > span {
      font-size: 14px;
    }
    .btn-sty {
      padding: 5px 10px;
      font-size: 14px;
      margin-left: 10px;
      line-height: 20px;
      color: #c0c4cc;
      background: rgba(0, 0, 0, 0.4);
      border: 1px solid #56fefe;
    }
    .el-select {
      width: auto;
      .el-input__inner {
        // width: 88px;
        height: 32px;
        border: 1px solid #56fefe;
        border-radius: 2px;
      }
      .el-input__suffix {
        .el-input__icon {
          line-height: 1;
        }
      }
    }
    > .el-input {
      width: auto;
      .el-input__inner {
        width: 88px;
        height: 32px;
        border: 1px solid #56fefe;
        border-radius: 2px;
      }
    }
  }
  .screen-button {
    width: 72px;
    height: 32px;
    border: 1px solid #56fefe;
    border-radius: 2px;
    background: #0000;
    color: #56fefe;
    padding: 0;
  }
  .el-checkbox {
    &.is-checked {
      .el-checkbox__inner {
        border-color: #56fefe;
        &::after {
          border: 1px solid #56fefe;
          border-left: 0;
          border-top: 0;
        }
      }
      .el-checkbox__label {
        color: #56fefe;
      }
    }
  }
  .el-checkbox__label {
    font-size: 14px;
  }
}
.el-pagination__total {
  color: #fff;
}
.el-pagination.is-background .btn-next,
.el-pagination.is-background .btn-prev,
.el-pagination.is-background .el-pager li {
  background-color: rgba($color: #000000, $alpha: 0);
}
.filter-item {
  display: flex;
  font-size: 14px;
  color: #fff;
  margin-top: 5px;
  > div {
    width: calc(100% - 70px);
    line-height: 24px;
  }
  .filter-label {
    width: 70px;
    text-align: left;
    line-height: 28px;
    flex-shrink: 0;
  }
  .el-date-editor {
    width: 150px;
    border: none;
    border-radius: 3px;
    margin-bottom: 8px;
    .el-input__inner {
      height: 32px;
    }
    .el-input__icon {
      line-height: 1;
    }
  }
  .select-time {
    width: 100px;
    margin-left: 10px;
  }
}

.el-select {
  border: 1px solid rgba(255, 255, 255, 0.6);
  border-radius: 3px;
  height: 32px;
  & > .el-input,
  .el-input__inner {
    height: 100%;
  }
  .el-select__caret {
    line-height: 1;
  }
}

.el-radio {
  margin: 8px 8px 8px 0;
  width: 90px;
  &.is-checked {
    .el-radio__label {
      color: #56fefe;
    }
    .el-radio__inner {
      background: transparent;
      &:after {
        background-color: #1ef8f2;
        border-color: #1ef8f2;
        transform: translate(-50%, -50%) scale(1.8);
      }
    }
  }
  .el-radio__inner {
    background-color: transparent;
    border: 1px solid #56fefe;
  }
  .el-radio__label {
    color: #fff;
  }
}
.qualityLevelList {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  .qualityLevel_li {
    width: 120px;
    height: 72px;
    font-size: 16px;
    text-align: center;
    margin-top: 16px;
    border-radius: 2px;
    border: 1px solid transparent;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: space-around;
    p {
      margin-top: 8px;
      font-size: 24px;
      &:first-child {
        font-size: 16px;
      }
    }
    &.level_li_0 {
      background: rgba(25, 175, 71, 0.1);
      border-color: #19af47;
    }
    &.level_li_1 {
      background: rgba(10, 143, 236, 0.1);
      border-color: rgba(0, 144, 234, 0.5);
    }
    &.level_li_2 {
      background: rgba(232, 145, 0, 0.1);
      border-color: rgba(232, 145, 0, 0.5);
    }
    &.level_li_3 {
      margin-top: 8px;
      background: rgba(250, 100, 0, 0.1);
      border-color: rgba(250, 100, 0, 0.5);
    }
    &.level_li_4 {
      margin-top: 8px;
      background: rgba(224, 32, 32, 0.1);
      border-color: rgba(224, 32, 32, 0.5);
    }
    &.level_li_5 {
      margin-top: 8px;
      background: rgba(182, 32, 224, 0.1);
      border-color: rgba(182, 32, 224, 0.5);
    }
    &.active {
      background: #005656;
      border-color: #56fefe;
    }
  }
}
</style>
