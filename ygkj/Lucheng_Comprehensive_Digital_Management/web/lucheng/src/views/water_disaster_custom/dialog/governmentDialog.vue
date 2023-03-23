<!--
 * @Autor: ysl
 * @Date: 2021-08-09 19:58:25
 * @LastEditors: ysl
 * @LastEditTime: 2021-10-13 16:23:09
 * @Description: 
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/dialog/governmentDialog.vue
-->
<template>
  <div class="primary-dialog detail-table">
    <el-dialog
      top="10vh"
      width="1200px"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      :fullscreen="fullscreen"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">
          {{ code }}
        </p>
        <i
          @click="changeFullscreen"
          :class="['el-icon-full-screen', 'full-screen-icon']"
        ></i>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <el-row v-if="code == '资产总览' || code == '报废'">
          <div class="count-panel">
            <div class="count-item">
              <i class="icon-count icon-sybh-count-1"></i>
              <div class="text">
                <p>资产总数</p>
                <p class="num">
                  {{ assetsObj.totalNum || assetsObj.assetNum || 0 }}
                  <span>件</span>
                </p>
              </div>
            </div>
            <div class="count-item" v-if="code == '资产总览'">
              <i class="icon-count icon-sybh-count-2"></i>
              <div class="text">
                <p>到期件数</p>
                <p class="num outNum">
                  {{ assetsObj.expireNum || 0 }} <span>件</span>
                </p>
              </div>
            </div>
            <div class="count-item" v-if="code == '资产总览'">
              <i class="icon-count icon-sybh-count-3"></i>
              <div class="text">
                <p>资产总额</p>
                <p class="num">
                  {{ assetsObj.totalAmount || 0 }} <span>万元</span>
                </p>
              </div>
            </div>
            <div class="count-item" v-if="code == '报废'">
              <i class="icon-count icon-sybh-count-4"></i>
              <div class="text">
                <p>报废总数</p>
                <p class="num outNum">
                  {{ assetsObj.collectNum || 0 }} <span>件</span>
                </p>
              </div>
            </div>
          </div>
        </el-row>
        <el-row v-if="code == '出差管理' && threeYearsData.length > 0">
          <div class="count-panel">
            <div class="count-item">
              <i class="icon-count icon-sybh-count-people"></i>
              <div class="text">
                <p>{{ threeYearsData[0].year }}年出差人次</p>
                <p class="num">
                  {{ threeYearsData[0].peopleNum || 0 }} <span>人</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-people"></i>
              <div class="text">
                <p>{{ threeYearsData[1].year }}年出差人次</p>
                <p class="num">
                  {{ threeYearsData[1].peopleNum || 0 }} <span>人</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-people"></i>
              <div class="text">
                <p>{{ threeYearsData[2].year }}年出差人次</p>
                <p class="num">
                  {{ threeYearsData[2].peopleNum || 0 }} <span>人</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-money"></i>
              <div class="text">
                <p>{{ threeYearsData[0].year }}总报销金额</p>
                <p class="num">
                  {{ threeYearsData[0].totalCost || 0 }} <span>元</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-money"></i>
              <div class="text">
                <p>{{ threeYearsData[1].year }}总报销金额</p>
                <p class="num">
                  {{ threeYearsData[1].totalCost || 0 }} <span>元</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-money"></i>
              <div class="text">
                <p>{{ threeYearsData[2].year }}总报销金额</p>
                <p class="num">
                  {{ threeYearsData[2].totalCost || 0 }} <span>元</span>
                </p>
              </div>
            </div>
          </div>
        </el-row>
        <el-row v-if="code == '公务用车' && threeYearsData.length > 0">
          <div class="count-panel">
            <div class="count-item">
              <i class="icon-count icon-sybh-count-people"></i>
              <div class="text">
                <p>{{ threeYearsData[0].year }}年用车次数</p>
                <p class="num">
                  {{ threeYearsData[0].num || 0 }} <span>次</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-people"></i>
              <div class="text">
                <p>{{ threeYearsData[1].year }}年用车次数</p>
                <p class="num">
                  {{ threeYearsData[1].num || 0 }} <span>次</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-people"></i>
              <div class="text">
                <p>{{ threeYearsData[2].year }}年用车次数</p>
                <p class="num">
                  {{ threeYearsData[2].num || 0 }} <span>次</span>
                </p>
              </div>
            </div>
          </div>
        </el-row>
        <el-row v-if="code == '公务接待' && threeYearsData.length > 0">
          <div class="count-panel">
            <div class="count-item">
              <i class="icon-count icon-sybh-count-people"></i>
              <div class="text">
                <p>{{ threeYearsData[0].year }}年接待次数</p>
                <p class="num">
                  {{ threeYearsData[0].num || 0 }} <span>次</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-people"></i>
              <div class="text">
                <p>{{ threeYearsData[1].year }}年接待次数</p>
                <p class="num">
                  {{ threeYearsData[1].num || 0 }} <span>次</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-people"></i>
              <div class="text">
                <p>{{ threeYearsData[2].year }}年接待次数</p>
                <p class="num">
                  {{ threeYearsData[2].num || 0 }} <span>次</span>
                </p>
              </div>
            </div>
          </div>
        </el-row>
        <el-row v-if="code == '其他报销' && threeYearsData.length > 0">
          <div class="count-panel">
            <div class="count-item">
              <i class="icon-count icon-sybh-count-people"></i>
              <div class="text">
                <p>{{ threeYearsData[0].year }}年报销次数</p>
                <p class="num">
                  {{ threeYearsData[0].peopleNum || 0 }} <span>次</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-people"></i>
              <div class="text">
                <p>{{ threeYearsData[1].year }}年报销次数</p>
                <p class="num">
                  {{ threeYearsData[1].peopleNum || 0 }} <span>次</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-people"></i>
              <div class="text">
                <p>{{ threeYearsData[2].year }}年报销次数</p>
                <p class="num">
                  {{ threeYearsData[2].peopleNum || 0 }} <span>次</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-money"></i>
              <div class="text">
                <p>{{ threeYearsData[0].year }}总报销金额</p>
                <p class="num">
                  {{ threeYearsData[0].totalCost || 0 }} <span>元</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-money"></i>
              <div class="text">
                <p>{{ threeYearsData[1].year }}总报销金额</p>
                <p class="num">
                  {{ threeYearsData[1].totalCost || 0 }} <span>元</span>
                </p>
              </div>
            </div>
            <div class="count-item">
              <i class="icon-count icon-sybh-count-money"></i>
              <div class="text">
                <p>{{ threeYearsData[2].year }}总报销金额</p>
                <p class="num">
                  {{ threeYearsData[2].totalCost || 0 }} <span>元</span>
                </p>
              </div>
            </div>
          </div>
        </el-row>
        <el-row v-if="code == '水质监控' && qualityStatsData.length > 0">
          <div style="display: flex">
            <div v-for="(item, index) in qualityStatsData" :key="index" :class="['quality_box', 'quality_icon_' + ( index + 1 )]">
              <div></div>
              <div>
                <p>{{ item.label }}</p>
                <p><span>{{ item.value }}</span> 个</p>
              </div>
            </div>
          </div>
        </el-row>
        <el-row>
          <el-col
            :span="24"
            class="water-supply-select-panel"
            v-if="code == '用水效率'"
          >
            <div
              :class="[
                activeTabCarry == item ? 'active' : '',
                'water-supply-select-btn',
              ]"
              @click="changeActiveTabCarry(item)"
              v-for="(item, index) in waterEfficiencyTabList"
              :key="index"
            >
              {{ item }}
            </div>
          </el-col>
          <el-col
            :span="24"
            class="water-supply-select-panel"
            v-if="code == '节水指标'"
          >
            <div
              :class="[
                handleSavingActionTab == index ? 'active' : '',
                'water-supply-select-btn',
              ]"
              @click="changeSavingTabCarry(index)"
              v-for="(item, index) in savingActionTabList"
              :key="index"
            >
              {{ item.label }}
            </div>
          </el-col>
          <el-col
            :span="24"
            class="water-supply-select-panel"
            v-if="code == '三条红线'"
          >
            <div
              :class="[
                handleLineActionTab == index ? 'active' : '',
                'water-supply-select-btn',
              ]"
              @click="changeLineTabCarry(index)"
              v-for="(item, index) in lineActionTabList"
              :key="index"
            >
              {{ item.label }}
            </div>
          </el-col>
        </el-row>
        <el-row v-if="code == '前期影响雨量'">
          <div class="count-panel">
            <div class="count-item" style="width: 35%">
              <i class="icon-maximum icon-sybh-count-1"></i>
              <div class="text">
                <p>最大</p>
                <p class="num" v-if="prophaseRainfallData.moistest != {}">
                  {{ prophaseRainfallData.moistest.date.slice(0, 10) }}
                  {{ prophaseRainfallData.moistest.areaName }}
                  {{ prophaseRainfallData.moistest.drp }}
                </p>
              </div>
            </div>
            <div class="count-item" style="width: 35%">
              <i class="icon-minimum icon-sybh-count-2"></i>
              <div class="text">
                <p>最小</p>
                <p class="num outNum" v-if="prophaseRainfallData.aridest != {}">
                  {{ prophaseRainfallData.aridest.date.slice(0, 10) }}
                  {{ prophaseRainfallData.aridest.areaName }}
                  {{ prophaseRainfallData.aridest.drp }}
                </p>
              </div>
            </div>
          </div>
        </el-row>
        <el-row :gutter="20" class="query-sty">
          <!-- 年份筛选 -->
          <el-col
            :span="6"
            v-if="
              code == '节水指标' ||
              code == '三条红线' ||
              code == '合同管理' ||
              code == '领用'
            "
          >
            <div class="demo-input-suffix">
              <p style="width: 80px">年份筛选：</p>
              <el-select
                v-model="checkYear"
                size="small"
                clearable
                @change="handleYear"
              >
                <el-option
                  v-for="(item, index) in yearOption"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
          </el-col>
          <!-- 月份筛选 -->
          <el-col
            :span="6"
            v-if="
              code == '合同管理' ||
              code == '报废' ||
              code == '政务内部职能管理' ||
              code == '市内出租车' ||
              code == '会议培训'
            "
          >
            <div class="demo-input-suffix">
              <p style="width: 80px">月份筛选：</p>
              <el-select
                v-model="checkMonth"
                size="small"
                clearable
                @change="handleMonth"
              >
                <el-option
                  v-for="(item, index) in monthOption"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
          </el-col>
          <!-- 使用部门 -->
          <el-col :span="6" v-if="code == '资产总览'">
            <div class="demo-input-suffix">
              <p style="width: 80px">使用部门：</p>
              <el-select v-model="deptName" size="small" @change="changeDept">
                <el-option
                  v-for="item in deptOptions"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </div>
          </el-col>
          <!-- 使用部门 -->
          <el-col :span="5" v-if="code == '出差管理' || code == '公务用车' || code == '公务接待' || code == '其他报销'">
            <div class="demo-input-suffix">
              <p style="width: 80px">部门筛选：</p>
              <el-select v-model="deptId" size="small" @change="changeDeptId">
                <el-option
                  v-for="item in departments"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </div>
          </el-col>
          <!-- 行政区划筛选 -->
          <el-col :span="6" v-if="code == '前期影响雨量'">
            <div class="demo-input-suffix">
              <p style="width: 80px">行政区划：</p>
              <el-select
                v-model="administrationValue"
                size="small"
                clearable
                @change="handleDate"
                placeholder="全部"
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
          <!-- 时间筛选 -->
          <el-col :span="7" v-if="code == '前期影响雨量' || code == '出差管理' || code == '公务用车' || code == '公务接待' || code == '其他报销'">
            <div class="demo-input-suffix">
              <p style="width: 75px">时间筛选：</p>
              <el-date-picker
                v-model="checkTime"
                size="small"
                clearable
                :picker-options="pickerOptions1"
                @change="handleDate"
                type="daterange"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
              >
              </el-date-picker>
            </div>
          </el-col>
          <!-- 人员筛选 -->
          <el-col :span="5" v-if="code == '出差管理'">
            <div class="demo-input-suffix">
              <p style="width: 40%">出差人员：</p>
              <el-input
                placeholder="请输入出差人员"
                v-model="sponsor"
                size="small"
                class="myClass select_input"
                clearable
                @input="queryVal"
              />
            </div>
          </el-col>
          <!-- 地点筛选 -->
          <el-col :span="5" v-if="code == '出差管理'">
            <div class="demo-input-suffix">
              <p style="width: 40%">出差地点：</p>
              <el-input
                placeholder="请输入出差地点"
                v-model="address"
                size="small"
                class="myClass select_input"
                clearable
                @input="queryVal"
              />
            </div>
          </el-col>
          <!-- 联系人筛选 -->
          <el-col :span="5" v-if="code == '公务用车'">
            <div class="demo-input-suffix">
              <p style="width: 40%">联系人：</p>
              <el-input
                placeholder="请输入联系人"
                v-model="sponsor"
                size="small"
                class="myClass select_input"
                clearable
                @input="queryVal"
              />
            </div>
          </el-col>
          <!-- 用车人筛选 -->
          <el-col :span="5" v-if="code == '公务用车'">
            <div class="demo-input-suffix">
              <p style="width: 40%">用车人：</p>
              <el-input
                placeholder="请输入用车人"
                v-model="escortPeople"
                size="small"
                class="myClass select_input"
                clearable
                @input="queryVal"
              />
            </div>
          </el-col>
          <!-- 经办人筛选 -->
          <el-col :span="5" v-if="code == '公务接待' || code == '其他报销'">
            <div class="demo-input-suffix">
              <p style="width: 65px">经办人：</p>
              <el-input
                placeholder="请输入经办人"
                v-model="sponsor"
                size="small"
                class="myClass select_input"
                clearable
                @input="queryVal"
              />
            </div>
          </el-col>
          <!-- 资金来源筛选 -->
          <el-col :span="5" v-if="code == '其他报销'">
            <div class="demo-input-suffix">
              <p style="width: 85px">资金来源：</p>
              <el-input
                placeholder="请输入经办人"
                v-model="sponsor"
                size="small"
                class="myClass select_input"
                clearable
                @input="queryVal"
              />
            </div>
          </el-col>
          <!-- 类型筛选 -->
          <el-col :span="5" v-if="code == '公务接待'">
            <div class="demo-input-suffix">
              <p style="width: 50px">类型：</p>
              <el-select
                v-model="remarkType"
                size="small"
                clearable
                placeholder="全部"
                @change="queryVal"
              >
                <el-option label="全部" value=""/>
                <el-option label="市内" value="1"/>
                <el-option label="市外" value="2"/>
              </el-select>
            </div>
          </el-col>
          <!-- 水质类型筛选 -->
          <el-col :span="5" v-if="code == '水质监控'">
            <div class="demo-input-suffix">
              <p style="width: 80px">水质类别：</p>
              <el-select
                v-model="qualityLevel"
                size="small"
                clearable
                placeholder="全部"
                @change="getWaterQualityV2"
              >
                <el-option label="全部" value=""/>
                <el-option label="Ⅰ类" value="1"/>
                <el-option label="Ⅱ类" value="2"/>
                <el-option label="Ⅲ类" value="3"/>
                <el-option label="Ⅳ类" value="4"/>
                <el-option label="Ⅴ类" value="5"/>
                <el-option label="劣Ⅴ类" value="6"/>
              </el-select>
            </div>
          </el-col>
          <!-- 行政区划筛选 -->
          <el-col :span="6" v-if="code == '水质监控'">
            <div class="demo-input-suffix">
              <p style="width: 80px">行政区划：</p>
              <el-select
                v-model="administrationValue"
                size="small"
                clearable
                placeholder="全部"
                @change="getWaterQualityV2"
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
          <!-- 站点级别类型筛选 -->
          <el-col :span="5" v-if="code == '水质监控'">
            <div class="demo-input-suffix">
              <p style="width: 80px">站点级别：</p>
              <el-select
                v-model="level"
                size="small"
                clearable
                placeholder="全部"
                @change="getWaterQualityV2"
              >
                <el-option label="全部" value=""/>
                <el-option label="国控" value="1"/>
                <el-option label="省控" value="2"/>
                <el-option label="市控" value="3"/>
                <el-option label="县控" value="4"/>
                <el-option label="乡镇控" value="5"/>
              </el-select>
            </div>
          </el-col>
          <!-- 资金来源筛选 -->
          <el-col :span="5" v-if="code == '水质监控'">
            <div class="demo-input-suffix">
              <p style="width: 85px">关键字：</p>
              <el-input
                placeholder="请输入站点名称"
                v-model="searchVal"
                size="small"
                class="myClass select_input"
                clearable
                @input="getWaterQualityV2"
              />
            </div>
          </el-col>
          <!-- 对比对象筛选 -->
          <el-col :span="6" v-if="code == '用水效率'">
            <div class="demo-input-suffix">
              <p style="width: 80px">对比对象：</p>
              <el-select
                v-model="compareType"
                size="small"
                clearable
                @change="getOverView"
              >
                <el-option
                  v-for="(item, index) in compareTypeOptions"
                  :key="index"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </div>
          </el-col>
          <!-- 取水用途筛选 -->
          <el-col :span="6" v-if="code == '取水监管'">
            <div class="demo-input-suffix">
              <p style="width: 80px">取水用途：</p>
              <el-select v-model="waterType" size="small" @change="getWptList">
                <el-option
                  v-for="item in waterTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                />
              </el-select>
            </div>
          </el-col>
          <el-col
            :span="1"
            :offset="1"
            v-if="code == '前期影响雨量' || code == '出差管理' || code == '公务用车' || code == '公务接待' || code == '其他报销' || code == '水质监控'"
          >
            <div class="derive-sty" style="width: 65px" @click="exportExcel">
              导 出
            </div>
          </el-col>
        </el-row>

        <!-- tab展示区域 -->
        <!-- 出差管理 -->
        <div class="tab-list-sty" v-if="code == '出差管理'">
          <el-table
            ref="travelTable"
            :data="tableData"
            height="99%"
            show-summary
            :summary-method="getSummaries"
            stripe
            class="abc government_table"
            :span-method="_objectSpanMethod"
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
              :formatter="toFixedFormatter"
              prop="deptName"
              label="部门"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="period"
              label="时间"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="sumPeopleNum"
              label="总人次"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="sumAmount"
              label="总金额"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="businessMan"
              label="出差人"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="startTime"
              label="时间"
            />
            <el-table-column :formatter="toFixedFormatter" label="出差地点">
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="toFixedFormatter"
                prop="businessAddress"
                label="市"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                :formatter="toFixedFormatter"
                prop="businessArea"
                label="区县"
              />
            </el-table-column>
            <el-table-column
              :formatter="toFixedFormatter"
              prop="averageAmount"
              label="报销金额(人均)"
            />
          </el-table>
        </div>
        <!-- 公务接待 -->
        <div class="tab-list-sty" v-if="code == '公务接待'">
          <el-table
            ref="travelTable"
            :data="tableData"
            height="99%"
            show-summary
            :summary-method="getSummaries"
            stripe
            class="abc government_table"
            :span-method="_objectSpanMethod"
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
              :formatter="toFixedFormatter"
              prop="deptName"
              label="部门"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="period"
              label="时间"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="count"
              label="总次数"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="handle"
              label="经办人"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="visitTrip"
              label="来访事由"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="receiveTime"
              label="日期"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="receptionType"
              label="类型"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="visitNum"
              label="来访人数"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="escortsNumber"
              label="陪同人数"
            />
          </el-table>
        </div>
        <!-- 合同管理 -->
        <div class="tab-list-sty" v-if="code == '合同管理'">
          <el-table
            ref="travelTable"
            :data="tableData"
            height="99%"
            show-summary
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
              prop="deptName"
              label="部门"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="contractAmount"
              label="合同总额(万元)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="expenseAmount"
              width="160px"
              label="合同总报销(万元)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="remainExpenseAmount"
              width="160px"
              label="剩余报销金额(万元)"
            />
          </el-table>
        </div>
        
        <!-- 用水效率 -->
        <div class="tab-list-sty" v-if="code == '用水效率'">
          <el-table
            ref="travelTable"
            :data="tableData"
            height="100%"
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
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              :prop="waterEfficiencyProp"
              :label="waterEfficiencyColumn"
            />
          </el-table>
        </div>
        <!-- 节水指标 -->
        <div class="tab-list-sty" v-if="code == '节水指标'">
          <el-table
            ref="travelTable"
            :data="tableData"
            height="100%"
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
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="year"
              label="年份"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              v-for="(item, index) in savingActionTabList[handleSavingActionTab]
                .columnsList"
              :key="index"
              :prop="item.props"
              :label="item.label"
            />
          </el-table>
        </div>
        <!--取水监管 -->
        <div class="tab-list-sty" v-if="code == '取水监管'">
          <el-table
            ref="travelTable"
            :data="tableData"
            height="100%"
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
              prop="applSn"
              label="许可证编码"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="typeQslx"
              label="取水用途"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="wpermitsize"
              label="批准取水量"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="intDl"
              label="许可有限期限"
            />
          </el-table>
        </div>
        <!-- 三条红线 -->
        <div class="tab-list-sty" v-if="code == '三条红线'">
          <el-table
            ref="travelTable"
            :data="tableData"
            height="100%"
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
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              v-for="(item, index) in lineActionTabList[handleLineActionTab]
                .columnsList"
              :key="index"
              :prop="item.props"
              :label="item.label"
            />
          </el-table>
        </div>
        <!-- 资产管理 -->
        <div
          class="tab-list-sty"
          v-if="code == '资产总览' || code == '报废' || code == '领用'"
        >
          <el-table
            ref="travelTable"
            :data="tableData"
            height="99%"
            show-summary
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
              prop="deptName"
              label="部门"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              v-for="(item, index) in assetsTableList[code]"
              :key="index"
              :prop="item.props"
              :label="item.label"
            />
          </el-table>
        </div>
        <!-- 政务内部职能管理 -->
        <div class="tab-list-sty" v-if="code == '政务内部职能管理'">
          <el-table
            ref="travelTable"
            :data="tableData"
            height="99%"
            show-summary
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
              prop="deptName"
              label="部门"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              v-for="(item, index) in insideTableList"
              :key="index"
              :prop="item.props"
              :label="item.label"
            />
          </el-table>
        </div>

        <!-- 公务用车 -->
        <div class="tab-list-sty" v-if="code == '公务用车'">
          <el-table
            ref="travelTable"
            :data="tableData"
            height="100%"
            show-summary
            :summary-method="getSummaries"
            stripe
            class="abc government_table"
            :span-method="_objectSpanMethod"
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
              :formatter="toFixedFormatter"
              prop="deptName"
              label="部门"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="period"
              label="时间"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="count"
              label="总次数"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="sponsor"
              label="联系人"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="carCause"
              label="事由"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="carType"
              label="用车类型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="carUser"
              label="用车人"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="useModel"
              label="车型"
            />
          </el-table>
        </div>
        <!-- 市内出租车&其他报销 -->
        <div
          class="tab-list-sty"
          v-if="
            code == '市内出租车' || code == '其他报销'
          "
        >
          <el-table
            ref="travelTable"
            :data="tableData"
            height="99%"
            show-summary
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
              prop="deptName"
              label="部门"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="expenseMoney"
              label="报销金额（万元）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="expenseNumber"
              label="报销次数"
            />
          </el-table>
        </div>
        <!-- 会议培训 -->
        <div class="tab-list-sty" v-if="code == '会议培训'">
          <el-table
            ref="travelTable"
            :data="tableData"
            height="99%"
            show-summary
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
              prop="deptName"
              label="部门"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              v-for="(item, index) in conferenceList"
              :key="index"
              :prop="item.props"
              :label="item.label"
            />
          </el-table>
        </div>
        <!-- 前期影响雨量 -->
        <div class="tab-list-sty" v-if="code == '前期影响雨量'">
          <el-table
            ref="travelTable"
            :data="tableData"
            height="100%"
            stripe
            border
            class="abc"
            v-loading="loading"
            :span-method="objectSpanMethod"
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
              prop="areaName"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="date"
              label="时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="drp"
              label="前期影响雨量(mm)"
            />
          </el-table>
        </div>
        <!-- 水质监控 -->
        <div class="tab-list-sty" v-if="code == '水质监控'">
          <el-table
            ref="travelTable"
            :data="tableData"
            height="100%"
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
              prop="siteName"
              label="站点名称"
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
              prop="codmn"
              label="高锰酸钾盐指数(mg/L)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="nh3N"
              label="氨氮(mg/L)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="tp"
              label="总磷(mg/L)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="cod"
              label="溶解氧"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="qualityLevel"
              label="水质等级"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="tm"
              label="更新时间"
            />
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import axios from "axios";
import config from "@/config/config";
import { localData } from "@/plugins/storage";
import { oaApi } from "@/api/governmentOaApi";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { hydraulicApi } from "@/api/hydraulic_amap/index";
import {exportWaterQualityList} from "@/api/micro-service/project-manage/export";
export default {
  name: "governmentDialog",
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    code: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      pickerOptions1: {
        disabledDate(time) {
          return time.getTime() < 1627747200000;
        }
      },
      spanArr: [],
      pos: 0,
      prophaseRainfallData: {},
      monthOption: [],
      checkMonth: "",
      yearOption: [],
      checkYear: 2020,
      checkTime: [],
      tableData: [],
      threeYearsData: [],
      governmentInfoData: {},
      //表格loading
      loading: false,
      keyWords: "",
      activeTabCarry: "万元GDP用水量",
      administrationValue: "", // 选中行政区划
      administration: [], // 行政区划
      compareType: "省内", // 选中对比对象
      compareTypeOptions: ["省内", "国内", "国际"], // 对比对象
      waterType: "城市生活", // 取水用途
      waterTypeOptions: [
        { label: "城市生活", value: 1 },
        { label: "农村生活", value: 2 },
        { label: "工业", value: 3 },
        { label: "农业", value: 4 },
        { label: "水力发电", value: 5 },
        { label: "其他", value: 6 },
        { label: "非水力发电", value: 7 }
      ],
      waterEfficiencyProp: "wpg",
      waterEfficiencyColumn: "万元GDP用水量(m³/万元)",
      waterEfficiencyTabList: ["万元GDP用水量", "万元工业增加值用水量"],
      handleSavingActionTab: 0,
      savingActionTabList: [
        {
          label: "年用水总量",
          columnsList: [
            { label: "用水量(亿m³)", props: "totalWatCsm" },
            { label: "十四五控制指标(亿m³)", props: "totalWatCsm5Norm" }
          ],
          value: 0
        },
        {
          label: "万元GDP用水量",
          columnsList: [
            { label: "万元GDP用水量(m³/万元)", props: "wpg" },
            { label: "万元GDP用水量下降率(%)", props: "wpgDecrRate" },
            { label: "十四五控制指标(%)", props: "wpgDecrRate5Norm" }
          ],
          value: 1
        },
        {
          label: "万元工业增加值用水量",
          columnsList: [
            { label: "万元工业增加值用水量(m³/万元)", props: "induWpg" },
            { label: "万元工业增加值用水量下降率(%)", props: "induWpgDecrRate" },
            { label: "十四五控制指标(%)", props: "induWpgDecrRate5Norm" }
          ],
          value: 2
        },
        {
          label: "农业灌溉水有效利用系数",
          columnsList: [
            { label: "农业灌溉水有效利用系数", props: "agIrrEffectFactor" },
            { label: "十四五控制指标", props: "agIrrEffectFactor5Norm" }
          ],
          value: 3
        },
        {
          label: "县域节水型社会创建达标率",
          columnsList: [
            { label: "县域节水型社会创建达标率", props: "watSavRate" },
            { label: "十四五控制指标", props: "watSavRate5Norm" }
          ],
          value: 4
        }
      ], // 节水指标tab
      handleLineActionTab: 0,
      lineActionTabList: [
        {
          label: "用水总量",
          columnsList: [
            { label: "实际用水量(亿m³)", props: "totalWatCsm" },
            { label: "控制指标(亿m³)", props: "controlWatCsm" }
          ],
          value: 0
        },
        {
          label: "用水效率",
          columnsList: [
            { label: "万元GDP用水量下降率(%)", props: "wpgDecrRate" },
            { label: "控制指标(%)", props: "wpgDecrRateNorm" }
          ],
          value: 1
        },
        {
          label: "限制纳污",
          columnsList: [
            { label: "水功能区水质达标率(%)", props: "wpgDecrRate" },
            { label: "控制指标(%)", props: "wpgDecrRateNorm" }
          ],
          value: 2
        }
      ], // 三条红线
      deptName: "", // 使用部门
      deptId: null, // 部门Id
      deptOptions: [], // 使用部门列表
      departments: [], // 顶级部门
      assetsObj: {},
      assetsTableList: {
        资产总览: [
          { label: "资产总额(万元)", props: "amountSum" },
          { label: "资产件数", props: "assetNum" },
          { label: "到期件数", props: "maturityNum" }
        ],
        报废: [
          { label: "资产件数", props: "assetNum" },
          { label: "报废件数", props: "controlWatCsm" }
        ],
        领用: [{ label: "领用件数", props: "collectNum" }]
      },
      insideTableList: [
        { label: "主办件个数(个)", props: "hostPartNum" },
        { label: "会办件个数(个)", props: "handlePartNum" },
        { label: "领导批示文件个数(个)", props: "instruFileNum" },
        { label: "重要文件个数(个)", props: "importFileNum" }
      ],
      conferenceList: [
        { label: "实际报销金额(万元)", props: "totalCost" },
        { label: "培训次数", props: "trainNumber" },
        { label: "预计住宿费支出(万元)", props: "hotelCost" },
        // { label: "预计伙食费支出(万元)", props: "importFileNum" },
        { label: "预计其他费用(万元)", props: "otherCost" }
      ],
      fullscreen: false,

      // 出差筛选信息
      address: null, // 地点模糊筛选
      sponsor: null, // 申请人模糊筛选
      // 公务用车筛选信息
      escortPeople: null, // 用车人筛选
      // 公务接待筛选信息
      remarkType: null, // 类型筛选
      // 合计信息
      columnsIndexList: {
        "出差管理": [ 
          { index: 1, prop: "deptNum" }, 
          { index: 3, prop: "sumPeopleNum" }, 
          { index: 4, prop: "sumAmount" }, 
          { index: 5, prop: "subtotalPeople" }, 
          { index: 7, prop: "" },  // sumAreaNum
          { index: 8, prop: "" },  // sumAddressNum
          { index: 9, prop: "subtotalAverageAmount" }
        ],
        "公务接待": [ 
          { index: 1, prop: "deptNum" }, 
          { index: 3, prop: "totalNum" }, 
          { index: 4, prop: "sponsorNum" },
          { index: 8, prop: "visitNum" },
          { index: 9, prop: "escortNum" }
        ],
        "公务用车": [ 
          { index: 1, prop: "deptNum" }, 
          { index: 3, prop: "totalNum" }, 
          { index: 4, prop: "sponsorNum" },
          { index: 9, prop: "" }
        ],
      },
      qualityStatsData: [
        { label: "Ⅰ类", value: 0, index: 1 },
        { label: "Ⅱ类", value: 0, index: 2 },
        { label: "Ⅲ类", value: 0, index: 3 },
        { label: "Ⅳ类", value: 0, index: 4 },
        { label: "Ⅴ类", value: 0, index: 5 },
        { label: "劣Ⅴ类", value: 0, index: 6 }
      ], // 水质监控数据
      qualityLevel: null, // 水质类别
      level: null, // 站点级别
      searchVal: null, // 站点名称
    };
  },
  created() {
    this.getmonthOption();
    this.getYearOption();
    this.getArea();
    this.getDepartMentList();
    this.getDeptOption()
  },
  methods: {
    // 查询部门
    getDeptOption() {
      oaApi.readDepartments({}).then(res => {
        if (res.code != 0) return this.$message.error(res.message || "系统错误");
        let data = JSON.parse(JSON.stringify(res.data));
        data.unshift("全部");
        this.deptOptions = data;
      });
    },
    /**
     * methods: getDepartMentList
     * desc: 获取所有顶级部门
     */
    getDepartMentList() {
      let data = {};
      oaApi.getTopDept(data).then(res => {
        if (res.code != 0) return this.$message.error(res.message || "系统错误");
        let data = JSON.parse(JSON.stringify(res.data));
        data.unshift({
          createId: null,
          createTime: null,
          delFlag: 0,
          id: "",
          name: "全部",
          remark: null,
          updateId: null,
          updateTime: null
        });
        this.departments = data;
      });
    },
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      this.$refs.travelTable.doLayout();
    },
    init() {
      switch (this.code) {
        case "出差管理":
          this.getOaOverView();
          this.businessApplyThreeYearsData();
          break;
        case "合同管理":
          this.checkYear = 2021;
          this.getOaContractOverView();
          break;
        case "公务接待":
          this.getOaOfficialOverView();
          this.receptionApplyThreeYearsData();
          break;
        case "资产总览":
        case "报废":
        case "领用":
          this.getAssetsList();
          break;
        case "政务内部职能管理":
          this.getInsideList();
          break;
        case "市内出租车":
          this.getTaxiExpenseDetailedTable();
          break;
        case "公务用车":
          this.getStatisDetailedTable();
          this.carApplyThreeYearsData()
          break;
        case "其他报销":
          this.getOtherExpenseDetailedTable();
          this.otherApplyThreeYearsData()
          break;
        case "会议培训":
          this.getMeetTrainDetailedTable();
          break;
        case "用水效率":
          this.getOverView();
          break;
        case "三条红线":
          this.checkYear = 2020;
          this.getRedLineData();
          break;
        case "节水指标":
          this.watSavNormByYear();
          break;
        case "取水监管":
          this.getWptList();
          break;
        case "前期影响雨量":
          this.getPrecedeAffectRainfall();
          this.stats4PrecedeAffectRainfall();
          break;
        case "水质监控":
          this.getWaterQualityV2()
          this.getQualityStatsData()
          break;
      }
    },
    // 搜索
    queryVal() {
      switch (this.code) {
        case "资产总览":
        case "报废":
        case "领用":
          this.getAssetsList();
          break;
        case "出差管理":
          this.getOaOverView();
          break;
        case "公务用车":
          this.getStatisDetailedTable();
          break;
        case "公务接待":
          this.getOaOfficialOverView();
          break;
      }
    },
    getSpanArr(data, key) {
      this.spanArr = [];
      for (var i = 0; i < data.length; i++) {
        if (i == 0) {
          this.spanArr.push(1);
          this.pos = 0;
        } else {
          // 判断当前元素与上一个元素是否相同  inAccessCode（批次字段）
          if (data[i][key] == data[i - 1][key]) {
            this.spanArr[this.pos] += 1;
            this.spanArr.push(0);
          } else {
            this.spanArr.push(1);
            this.pos = i;
          }
        }
      }
    },
    //进行表格合并
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex == 1) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col
        };
      }
    },
    // 导出
    exportExcel() {
      if (this.code == "水质监控") {
        let opt = {
          areaCode: this.administrationValue,
          siteName: this.searchVal,
          qualityLevel: this.qualityLevel,
          level: this.level,
        }
        exportWaterQualityList(opt)
        return false
      }
      if (this.code != "前期影响雨量") {
        this.governmentExportExcel();
        return false;
      }
      let obj = {
        exportStrategy: 0,
        endDate: this.checkTime[1],
        startDate: this.checkTime[0],
        areaCode: this.administrationValue
      };
      let userInfo = localData("get", "userInfo");
      let { accessToken } = userInfo ? userInfo : { accessToken: "" };
      axios({
        url: `${config.baseUrl}/graph/waterAndRain/exportPrecedeAffectRainfall`,
        method: "post",
        data: obj,
        responseType: "blob",
        headers: {
          authorization: accessToken
        }
      })
        .then(res => {
          const content = res.data;
          const blob = new Blob([content], { type: "application/msword" });
          const link = document.createElement("a"); // 创建a标签
          let filename = res.headers["content-disposition"];
          filename = filename ? filename.substr(filename.indexOf("=") + 1) : "";
          filename = decodeURIComponent(filename);
          link.download = filename;
          link.style.display = "none";
          link.href = URL.createObjectURL(blob);
          document.body.appendChild(link);
          link.click(); // 执行下载
          URL.revokeObjectURL(link.href); // 释放url
          document.body.removeChild(link); // 释放标签
          this.exportLoading = false;
        })
        .catch(error => {
          this.exportLoading = false;
        });
    },
    // 用水效率详表tab切换
    changeActiveTabCarry(val) {
      this.activeTabCarry = val;
      switch (val) {
        case "万元GDP用水量":
          this.waterEfficiencyProp = "wpg";
          this.waterEfficiencyColumn = "万元GDP用水量(m³/万元)";
          break;
        case "万元工业增加值用水量":
          this.waterEfficiencyProp = "induWpg";
          this.waterEfficiencyColumn = "万元工业增加值用水量(m³/万元)";
          break;
      }
    },
    // 节水指标详表tab切换
    changeSavingTabCarry(val) {
      this.handleSavingActionTab = val;
      this.watSavNormByYear();
      switch (val) {
        case "年用水总量":
          break;
        case "万元GDP用水量":
          break;
      }
    },
    // 三条红线
    changeLineTabCarry(val) {
      this.handleLineActionTab = val;
      this.getRedLineData();
    },
    // 获取年份选择下拉内容
    getYearOption() {
      let date = new Date();
      let y = date.getFullYear();
      this.yearOption = [{ label: "全部", value: "" }];
      for (let i = y; i >= 1990; i--) {
        this.yearOption.push({
          label: i + " 年",
          value: i
        });
      }
    },
    // 下拉年份选择后
    handleYear() {
      switch (this.code) {
        case "合同管理":
          this.getOaContractOverView();
          break;
        case "三条红线":
          this.getRedLineData();
          break;
        case "领用":
          this.getAssetsList();
          break;
      }
    },
    // 获取月份选择下拉内容
    getmonthOption() {
      let date = new Date();
      let m = date.getMonth() + 1;
      for (let i = 1; i <= 12; i++) {
        this.monthOption.push({
          label: i + " 月",
          value: i
        });
      }
      this.checkMonth = m;
    },
    // 下拉月份选择后
    handleMonth() {
      switch (this.code) {
        case "出差管理":
          this.getOaOverView();
          break;
        case "合同管理":
          this.getOaContractOverView();
          break;
        case "公务接待":
          this.getOaOfficialOverView();
          break;
        case "报废":
          this.getAssetsList();
          break;
        case "政务内部职能管理":
          this.getInsideList();
          break;
        case "市内出租车":
          this.getTaxiExpenseDetailedTable();
          break;
        case "公务用车":
          this.getStatisDetailedTable();
          break;
        case "其他报销":
          this.getOtherExpenseDetailedTable();
          break;
        case "会议培训":
          this.getMeetTrainDetailedTable();
          break;
      }
    },
    // 时间筛选
    handleDate() {
      switch(this.code) {
        case "前期影响雨量": 
          this.getPrecedeAffectRainfall();
          this.stats4PrecedeAffectRainfall();
          break;
        case "出差管理":
          this.getOaOverView();
          break;
        case "公务用车":
          this.getStatisDetailedTable();
          break;
        case "公务接待":
          this.getOaOfficialOverView();
          break;
      }
    },
    // 变更部门
    changeDept() {
      this.statsTable4ScreenUnit();
    },
    // 变更部门ID
    changeDeptId() {
      switch (this.code) {
        case "出差管理":
          this.getOaOverView();
          break;
        case "公务用车":
          this.getStatisDetailedTable();
          break;
        case "公务接待":
          this.getOaOfficialOverView();
          break;
      }
    },
    /**
     * 行政区划
     */
    async getArea() {
      let obj = {
        areaCode: 3303,
        level: 3
      };
      await disasterPreventionApi.getArea(obj).then(res => {
        if (res.code == 0) {
          this.administration = res.data;
        }
      });
    },
    governmentExportExcel() {
      let obj = {},
        str = "";

      switch (this.code) {
        case "出差管理":
          obj = {
            type: 5,
            startDate: this.checkTime[0],
            endDate: this.checkTime[1]
          };
          this.escortPeople ? (obj.escortPeople = this.escortPeople) : "";
          this.address ? (obj.address = this.address) : "";
          this.deptId ? (obj.deptId = this.deptId) : "";
          str = "/businessApply/exportDetailTableData";
          break;
        case "公务用车":
          obj = {
            type: 5,
            startDate: this.checkTime[0],
            endDate: this.checkTime[1]
          };
          this.sponsor ? (obj.sponsor = this.sponsor) : "";
          this.escortPeople ? (obj.escortPeople = this.escortPeople) : ""
          this.deptId ? (obj.deptId = this.deptId) : "";
          str = "/carApply/exportDetailTable";
          break;
        case "公务接待":
          obj = {
            startDate: this.checkTime[0],
            endDate: this.checkTime[1],
          };
          this.sponsor ? (obj.sponsor = this.sponsor) : "";
          this.remarkType ? (obj.remarkType = this.remarkType) : "";
          this.deptId ? (obj.deptId = this.deptId) : "";
          str = "/receptionApply/exportDetailTable";
          break;
        case "其他报销":
          obj = {
            startDate: this.checkTime[0],
            endDate: this.checkTime[1],
          };
          this.sponsor ? (obj.sponsor = this.sponsor) : "";
          this.remarkType ? (obj.remarkType = this.remarkType) : "";
          this.deptId ? (obj.deptId = this.deptId) : "";
          str = "/other/exportDetailTable";
          break;
      }
      this.commonExportExcel(obj, str);
    },
    /*
     * 公共导出
     */
    async commonExportExcel(obj, url) {
      let userInfo = localData("get", "userInfo");
      let { accessToken } = userInfo ? userInfo : { accessToken: "" };
      axios({
        url: config.baseUrl + "office-automation" + url,
        method: "post",
        data: obj,
        responseType: "blob",
        headers: {
          authorization: accessToken
        }
      })
        .then(res => {
          const content = res.data;
          const blob = new Blob([content], { type: "application/msword" });
          const link = document.createElement("a"); // 创建a标签
          // let filename = res.headers["content-disposition"];
          let filename = this.code + ".xlsx";
          // filename = filename ? filename.substr(filename.indexOf("=") + 1) : "";
          filename = decodeURIComponent(filename);
          link.download = filename;
          link.style.display = "none";
          link.href = URL.createObjectURL(blob);
          document.body.appendChild(link);
          link.click(); // 执行下载
          URL.revokeObjectURL(link.href); // 释放url
          document.body.removeChild(link); // 释放标签
          this.exportLoading = false;
        })
        .catch(error => {
          this.exportLoading = false;
        });
    },
    // OA表格统一合并
    _objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      let num = this.code == '出差管理' ? 4 : 3
      if (columnIndex <= num) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col
        };
      }
    },
    // 合计
    getSummaries(param) {
        const { columns, data } = param;
        const sums = [];
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '小计';
            return;
          }
          sums[index] = "-"
          this.columnsIndexList[this.code].forEach( item => {
            sums[item.index] = this.governmentInfoData[item.prop] || '-'
          })
        });
        return sums;
    },
    // 获取水质监测详表数据
    getWaterQualityV2() {
      this.loading = true
      let opt = {
        areaCode: this.administrationValue,
        siteName: this.searchVal,
        qualityLevel: this.qualityLevel,
        level: this.level,
      }
      hydraulicApi.waterQualityV2(opt).then(res => {
        this.loading = false
        this.tableData = res.data.map(item => {
          item.tm = item.tm && item.tm.substring(0, 10)
          this.qualityStatsData.forEach(sItem => {
            item.qualityLevel == sItem.index ? item.qualityLevel = sItem.label : ""
          })
          return item
        });
      })
    },
     // 获取水质监控统计数据
    async getQualityStatsData() {
      await hydraulicApi.qualityStats({ type: 3 }).then(res => {
        this.qualityStatsData.map((item, index) => {
          res.data.forEach(sItem => {
            if( sItem.key == item.label ) {
              // item.index = index - 1
              item.value = sItem.value
            }
          })
          return item
        })
      })
    },
    // 获取近3年数据
    businessApplyThreeYearsData() {
      oaApi.businessApplyThreeYearsData({}).then(res => {
        this.threeYearsData = res.data;
      });
    },
    // 获取近3年数据
    carApplyThreeYearsData() {
      oaApi.carApplyThreeYearsData({}).then(res => {
        this.threeYearsData = res.data;
      });
    },
    // 获取近3年数据
    receptionApplyThreeYearsData() {
      oaApi.receptionApplyThreeYearsData({}).then(res => {
        this.threeYearsData = res.data;
      });
    },
    // 获取近3年数据
    otherApplyThreeYearsData(){
      oaApi.otherApplyThreeYearsData({}).then(res => {
        this.threeYearsData = res.data
      })
    },
    // 获取出差管理数据
    async getOaOverView() {
      this.loading = true;
      let opt = {
        type: 5,
        deptId: this.deptId,
        escortPeople: this.sponsor,
        address: this.address
      };
      this.checkTime ? opt.startDate = this.checkTime[0] : ""
      this.checkTime ? opt.endDate = this.checkTime[1] : ""
      this.tableData = [];
      await oaApi.getOaOverView(opt).then(res => {
        res.code == 0
          ? res.data.list.forEach(item => {
            item.list.forEach(sItem => {
              sItem.deptName = item.deptName;
              sItem.period = item.period;
              sItem.sumAmount = item.sumAmount;
              sItem.sumPeopleNum = item.sumPeopleNum;
              sItem.startTime = sItem.startTime && sItem.startTime.substring(0, 10);
              this.tableData.push(sItem);
            });
          })
          : [];
        this.governmentInfoData = res.data;
        this.loading = false;
      });
      this.$nextTick(() => {
        this.getSpanArr(this.tableData, "deptName");
      });
    },
    // 合同管理列表
    async getOaContractOverView() {
      this.loading = true;
      let month = this.checkMonth < 10 ? "0" + this.checkMonth : this.checkMonth;
      let opt = {
        year: this.checkYear + "-" + month
      };
      await oaApi.contractLoadPage(opt).then(res => {
        res.code == 0 ? (this.tableData = res.data.list) : [];
        this.loading = false;
      });
    },
    // 公务接待列表
    async getOaOfficialOverView() {
      this.loading = true;
      let opt = {
        deptId: this.deptId,
        sponsor: this.sponsor,
        remarkType: this.remarkType,
      };
      this.checkTime ? opt.startDate = this.checkTime[0] : ""
      this.checkTime ? opt.endDate = this.checkTime[1] : ""
      this.tableData = []
      await oaApi.detailedTableComponents(opt).then(res => {
        res.code == 0
          ? res.data.list.forEach(item => {
            item.list.forEach(sItem => {
              sItem.deptName = item.deptName;
              sItem.period = item.period;
              sItem.count = item.count;
              sItem.receptionType = sItem.receptionType == 2 ? "市外" : "市内"
              sItem.receiveTime = sItem.receiveTime && sItem.receiveTime.substring(0, 10)
              this.tableData.push(sItem);
            });
          })
          : [];
        this.governmentInfoData = res.data;
        this.loading = false;
      });
      this.$nextTick(() => {
        this.getSpanArr(this.tableData, "deptName");
      });
    },
    // 获取资产管理数据
    async getAssetsList() {
      this.code == "资产总览"
        ? this.statsTable4ScreenUnit()
        : this.code == "报废"
          ? this.scrapApplyStatsTable4ScreenUnit()
          : this.collectApplyStatsTable4ScreenUnit();
    },
    // 获取资产统计数据
    async statsTable4ScreenUnit() {
      let opt = {
        dept: this.deptName == "全部" ? null : this.deptName
      };
      await oaApi.listTable4ScreenUnit(opt).then(res => {
        res.code == 0
          ? (this.tableData = res.data.map(item => {
            return item;
          }))
          : "";
      });
      await oaApi.statsTable4ScreenUnit({}).then(res => {
        res.data.totalAmount = Number(res.data.totalAmount).toFixed(2);
        this.assetsObj = res.data;
      });
    },
    // 获取报废统计数据
    async scrapApplyStatsTable4ScreenUnit() {
      let month = this.checkMonth < 10 ? "0" + this.checkMonth : this.checkMonth;
      let opt = {
        month: month
      };
      await oaApi.scrapApplyListTable4ScreenUnit(opt).then(res => {
        res.code == 0
          ? (this.tableData = res.data.map(item => {
            item.deptName = item.key;
            item.assetNum = item.value;
            item.controlWatCsm = item.remark;
            return item;
          }))
          : "";
      });

      await oaApi.scrapApplyStatsTable4ScreenUnit(opt).then(res => {
        res.data.totalAmount = Number(res.data.totalAmount).toFixed(2);
        this.assetsObj = res.data;
      });
    },
    // 获取领用统计数据
    async collectApplyStatsTable4ScreenUnit() {
      let opt = {
        year: this.checkYear
      };
      await oaApi.collectApplyListTable4ScreenUnit(opt).then(res => {
        res.code == 0 ? (this.tableData = res.data) : "";
      });
    },
    // 获取政务内部职能管理数据
    async getInsideList() {
      this.loading = true;
      let month = this.checkMonth < 10 ? "0" + this.checkMonth : this.checkMonth;
      let opt = {
        month
      };
      await oaApi.proposalDetailedTable(opt).then(res => {
        this.loading = false;
        res.code == 0 ? (this.tableData = res.data) : "";
      });
    },
    // 获取市内出租车数据
    async getTaxiExpenseDetailedTable() {
      this.loading = true;
      let month = this.checkMonth < 10 ? "0" + this.checkMonth : this.checkMonth;
      let opt = {
        month
      };
      await oaApi.taxiExpenseDetailedTable(opt).then(res => {
        this.loading = false;
        res.code == 0 ? (this.tableData = res.data) : "";
      });
    },
    // 获取公务用车数据
    async getStatisDetailedTable() {
      this.loading = true;
      let opt = {
        deptId: this.deptId,
        sponsor: this.sponsor,
        escortPeople: this.escortPeople
      };
      this.checkTime ? opt.startDate = this.checkTime[0] : ""
      this.checkTime ? opt.endDate = this.checkTime[1] : ""
      this.tableData = []
      await oaApi.statisDetailedTable(opt).then(res => {
        res.code == 0
          ? res.data.list.forEach(item => {
            item.list.forEach(sItem => {
              sItem.deptName = item.deptName;
              sItem.period = item.period;
              sItem.count = item.count;
              sItem.carType = sItem.carType == '1' ? '公务用车' : sItem.carType == '2' ? '接待用车' : sItem.carType == '3' ? '抗台抢险' : '-'
              sItem.useModel = sItem.useModel == '1' ? '轿车' : sItem.useModel == '2' ? '商务车' : sItem.useModel == '3' ? '越野车' : sItem.useModel == '4' ? '面包车' : '大客车'
              sItem.startTime = sItem.startTime && sItem.startTime.substring(0, 10);
              this.tableData.push(sItem);
            });
          })
          : [];
        this.governmentInfoData = res.data;
        this.loading = false;
      });

      this.$nextTick(() => {
        this.getSpanArr(this.tableData, "deptName");
      });
    },
    // 获取其他报销数据
    async getOtherExpenseDetailedTable() {
      this.loading = true;
      let opt = {
        deptId: this.deptId,
        sponsor: this.sponsor,
        escortPeople: this.escortPeople
      };
      await oaApi.otherExpenseDetailedTable(opt).then(res => {
        res.code == 0
          ? res.data.list.forEach(item => {
            item.list.forEach(sItem => {
              sItem.deptName = item.deptName;
              sItem.period = item.period;
              sItem.count = item.count;
              // sItem.receptionType = sItem.receptionType == 2 ? "市外" : "市内"
              // sItem.receiveTime = sItem.receiveTime && sItem.receiveTime.substring(0, 10)
              this.tableData.push(sItem);
            });
          })
          : [];
        this.governmentInfoData = res.data;
        this.loading = false;
      });

      this.$nextTick(() => {
        this.getSpanArr(this.tableData, "deptName");
      });
    },
    // 获取会议培训数据
    async getMeetTrainDetailedTable() {
      this.loading = true;
      let month = this.checkMonth < 10 ? "0" + this.checkMonth : this.checkMonth;
      let opt = {
        month
      };
      await oaApi.meetTrainDetailedTable(opt).then(res => {
        this.loading = false;
        res.code == 0 ? (this.tableData = res.data) : "";
      });
    },
    // 获取用水效率数据
    async getOverView() {
      this.loading = true;
      let opt = {
        areaLevel: this.compareType
      };
      this.barDataList = [];
      this.firstData = [];
      this.maxValue = 0;
      await waterResourceApi.wpgEffect(opt).then(res => {
        res.code == 0
          ? (this.tableData = res.data.map(item => {
            item.wpg = Number(item.wpg).toFixed(2);
            item.induWpg = Number(item.induWpg).toFixed(2);
            return item;
          }))
          : [];
        this.loading = false;
      });
    },
    // 获取三条红线数据
    async getRedLineData(type) {
      this.loading = true;
      let opt = { year: this.checkYear };
      let str = this.handleLineActionTab == 0 ? "redLineWatCsm" : "redLineWpgEffect";
      this.tableData = [];
      await waterResourceApi[str](opt).then(res => {
        this.loading = false;
        res.code == 0
          ? (this.tableData = res.data.list.map(item => {
            item.controlWatCsm = item.controlWatCsm ? item.controlWatCsm.toFixed(2) : 0;
            item.totalWatCsm = item.totalWatCsm ? item.totalWatCsm.toFixed(2) : 0;
            item.wpgDecrRateNorm = item.wpgDecrRateNorm ? item.wpgDecrRateNorm.toFixed(2) : 0;
            item.wpgDecrRate = item.wpgDecrRate ? item.wpgDecrRate.toFixed(2) : 0;
            return item;
          }))
          : [];
        this.loading = false;
      });
    },
    // 获取节水指标数据
    async watSavNormByYear() {
      this.loading = true;
      let opt = {
        compareType: this.handleSavingActionTab + 1,
        year: this.checkYear
      };
      await waterResourceApi.watSavNormByYear(opt).then(res => {
        this.loading = false;
        res.code == 0
          ? (this.tableData = res.data.map(item => {
            item.totalWatCsm = item.totalWatCsm ? Number(item.totalWatCsm).toFixed(2) : 0;
            item.induWpg = item.induWpg ? Number(item.induWpg).toFixed(2) : 0;
            item.wpg = item.wpg ? Number(item.wpg).toFixed(2) : 0;
            return item;
          }))
          : "";
      });
    },
    // 获取取水监管数据
    async getWptList() {
      this.loading = true;
      let opt = {
        typeQslx: this.waterType
      };
      await waterResourceApi.getWptList(opt).then(res => {
        this.loading = false;
        res.code == 0
          ? (this.tableData = res.data.map(item => {
            item.intDl = item.intDl && item.intDl.substring(0, 10);
            return item;
          }))
          : "";
      });
    },
    // 前期雨量 统计接口
    async stats4PrecedeAffectRainfall() {
      let opt = {
        endDate: this.checkTime[1],
        startDate: this.checkTime[0],
        areaCode: this.administrationValue
      };
      await waterResourceApi.stats4PrecedeAffectRainfall(opt).then(res => {
        if (res.code == 0) {
          this.prophaseRainfallData = res.data;
        }
      });
    },
    // 获取前期影响雨量数据
    async getPrecedeAffectRainfall() {
      this.loading = true;
      let opt = {
        endDate: this.checkTime[1],
        startDate: this.checkTime[0],
        areaCode: this.administrationValue
      };
      await waterResourceApi.pagePrecedeAffectRainfall(opt).then(res => {
        this.loading = false;
        res.code == 0
          ? (this.tableData = res.data.map(item => {
            item.date = item.date.substring(0, 10);
            item.drp = item.drp && Number(item.drp).toFixed(2);
            return item;
          }))
          : "";
        this.$nextTick(() => {
          this.getSpanArr(this.tableData, "areaName");
        });
      });
    },
    /**
     * 弹窗关闭
     */
    handleClose() {
      this.$emit("closeDialog");
    },
    /**
     * 表格数据格式化
     */
    toFixedFormatter(row, column, value, index) {
      if (value != null) {
        return value;
      } else {
        return "-";
      }
    },
    sortChange(val) {
      if (val.order) {
        if (val.order == "ascending") {
          this.tableData.sort(this.sortBy(val.prop, false));
        } else {
          this.tableData.sort(this.sortBy(val.prop, true));
        }
      } else {
        this.tableData.sort(this.sortBy(val.prop, false));
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
    }
  }
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
@import "@/style/sidebarStyle.scss";
// 去掉table表格纵向滚动条
::v-deep .el-table__body-wrapper::-webkit-scrollbar {
  height: 6px; // 纵向滚动条 必写
  width: 0px;
}
//设置table表格字体样式
::v-deep.sidebar-list .el-table .cell {
  font-size: 12px;
  opacity: 0.8;
}
//设置table表格 列宽，字间距样式
::v-deep .el-table {
  .cell {
    height: $tdHeight;
    line-height: $tdHeight;
  }
  // 设置勾选框样式
  .el-checkbox__inner {
    border: 0.0625rem solid #00c1ff;
    background-color: #00c1ff;
    background: rgba($color: #000000, $alpha: 0);
  }
  // 设置选中行样式颜色
  .el-table__body tr.current-row > td {
    background-color: rgba(41, 147, 180, 0.3);
  }
  th,
  td {
    color: #fff;
    font-size: 12px;
    background-color: transparent;
  }
  th {
    font-size: 14px;
    color: #00c1ff;
    background-color: rgba(41, 147, 180, 0.1);
  }
}
</style>
<style lang="scss">
.realtime-250 {
  background-color: #f32fdf;
  @include icon();
}
.realtime-100 {
  background-color: #fe323a;
  @include icon();
}
.realtime-50 {
  background-color: #ff8a21;
  @include icon();
}
.realtime-25 {
  background-color: #fecb6e;
  @include icon();
}
.realtime-10 {
  background-color: #4bc704;
  @include icon();
}
.realtime-0_1 {
  background-color: #00a0ff;
  @include icon();
}
.forecast-100 {
  background-color: #7f0140;
  @include icon();
}
.forecast-70 {
  background-color: #f800fb;
  @include icon();
}
.forecast-10 {
  background-color: #3cba3e;
  @include icon();
}
.forecast-0 {
  background-color: #fff;
  @include icon();
}
.forecast-50 {
  background-color: #0001e1;
  @include icon();
}
.forecast-25 {
  background-color: #60b8ff;
  @include icon();
}
.forecast-10 {
  background-color: #48b940;
  @include icon();
}
.forecast-1 {
  background-color: #92ff74;
  @include icon();
}

// 供水量图例图标
.legend-icon-xushui {
  background-image: url(~assets/images/gongshui.png);
  background-repeat: no-repeat;
}
.legend-icon-tishui {
  background-image: url(~assets/images/tishui.png);
  background-repeat: no-repeat;
}
.legend-icon-yinshui {
  background-image: url(~assets/images/yinshui.png);
  background-repeat: no-repeat;
}

// 水资源承载能力 水量水质评价图例
.water-buchaozai {
  background-color: #19af47;
  @include icon();
}
.water-linjie {
  background-color: #008fe8;
  @include icon();
}
.water-chaozai {
  background-color: #e89100;
  @include icon();
}
.water-yanzhong {
  background-color: #e80000;
  @include icon();
}

// 水资源承载能力 经济人口评价图例
.water-max {
  background-color: #00c1ff;
  @include icon();
}
.water-min {
  background-color: #19af47;
  @include icon();
}
.water-normal {
  background-color: #33ffff;
  @include icon();
}
</style>

<style lang="scss" scoped>
.count-panel {
  display: flex;
  .count-item {
    // width: 20%;
    display: flex;
    align-items: center;
    margin: 5px 20px 0 0;
    .text {
      font-size: 14px;
      margin-left: 10px;
      line-height: 25px;
      color: #ffffff;
      .num {
        font-size: 20px;
        font-weight: bold;
        color: #56fefe;
      }
      .outNum {
        color: #e89100;
      }
      span {
        color: #fff;
        font-size: 12px;
      }
    }
  }
}

.icon-count {
  display: inline-block;
  width: 48px;
  height: 48px;
  background-size: 100% 100%;
  &.icon-sybh-count-1 {
    background-image: url(~assets/images/assets_count_1.png);
  }
  &.icon-sybh-count-2 {
    background-image: url(~assets/images/assets_count_2.png);
  }
  &.icon-sybh-count-3 {
    background-image: url(~assets/images/assets_count_3.png);
  }
  &.icon-sybh-count-4 {
    background-image: url(~assets/images/assets_count_4.png);
  }
  &.icon-sybh-count-people {
    background-image: url(~assets/images/icon-sybh-count-people.png);
  }
  &.icon-sybh-count-money {
    background-image: url(~assets/images/icon-sybh-count-money.png);
  }
}
.mg-l16 {
  margin-left: 16px;
}
.fr {
  float: right;
}
.primary-dialog {
  ::v-deep .el-dialog {
    @include flexbox;
    @include flexflow(column, nowrap);
    height: 680px;
    &.is-fullscreen {
      height: 100%;
      overflow: hidden;
    }
    .el-dialog__header {
      padding: 2px;
      .primary-dialog__title {
        position: relative;
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
        .full-screen-icon {
          position: absolute;
          right: 40px;
          color: #00c1ff;
          cursor: pointer;
          top: 15px;
        }
      }
    }
    .el-dialog__body {
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      @include flex(1);
      .primary-dialog__content {
        @include flexbox;
        @include flexflow(column, nowrap);
        height: 100%;
        // height: 680px;
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
            // margin-right: 50px;
            padding: 10px 0;
            margin-right: 20px;
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
            cursor: pointer;
            border-color: linear-gradient(
              180deg,
              #36c0e4 20%,
              #0639ff 100%
            ) !important;
            background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
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

        .tab-list-sty {
          @include flex(1);
          padding-bottom: 20px;
          overflow: auto;
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
  ::v-deep.el-table .warm-row .cell {
    color: #eec80b !important;
  }
  .abc ::v-deep .el-table__body tr:hover {
    background-color: rgba($color: #56fefe, $alpha: 0.4);
  }
  .gates-open-sty {
    background: url(~assets/images/WaterDisastersImage/zhamen-kaiqi.png)
      no-repeat center center;
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
          color: #ffffff;
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
          color: #1cfff5;
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
  border-bottom-color: #1cfff5;
}
::v-deep.el-table .descending .sort-caret.descending {
  border-top-color: #1cfff5;
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
::v-deep.el-date-editor .el-icon-date {
  line-height: 1;
}
.detail-table {
  ::v-deep .el-table__body-wrapper::-webkit-scrollbar {
    height: 6px; // 纵向滚动条 必写
    width: 6px;
  }
}
.tb-top-panel {
  margin-top: 10px;
  color: #fff;
  display: flex;
  align-items: center;
  i {
    float: left;
  }
  p {
    flex: 1;
    font-size: 15px;
    margin-left: 10px;
    background-color: rgba(255, 255, 255, 0.1);
    padding: 8px 10px;
    border-radius: 4px;
  }
  span {
    color: #7ccfff;
    font-size: 20px;
  }
  .laba {
    width: 24px;
    height: 24px;
    background: url(~assets/images/laba.png) no-repeat;
    background-size: 100%;
  }
}

::v-deep .is-checked .el-checkbox__label {
  color: #70adf8;
}
::v-deep .el-radio {
  margin-right: 15px;
}
::v-deep .el-radio__label {
  color: #fff;
}
::v-deep .is-checked .el-checkbox__inner {
  border-color: #70adf8;
  color: #70adf8;
  &::after {
    border-color: #70adf8;
  }
}
</style>

<style lang="scss" scoped>
.water-supply-select-panel {
  display: flex;
  margin-bottom: 20px;
}
.water-supply-select-btn {
  padding: 10px 20px;
  margin-right: 20px;
  color: #c9c8c8;
  border: 1px solid rgba(255, 255, 255, 0.6);
  cursor: pointer;
  &.active {
    color: #56fefe;
    background: rgba(72, 152, 251, 0.1);
    border: 1px solid #56fefe;
    box-shadow: inset 0 0 5px 0 #56fefe;
  }
}
::v-deep .el-table__empty-text,
::v-deep .el-loading-text {
  text-align: center;
}
::v-deep .el-table {
  &.government_table {
    tr {
      td {
        &:nth-child(2), &:nth-child(3) {
          .cell {
            overflow: initial;
            height: 2.9125rem;
            line-height: 1.5rem;
            display: flex;
            align-items: center;
            justify-content: center;
          }
        } 
      }
    }
    .el-table__body-wrapper.is-scrolling-none {
      height: calc(100% - 124px) !important;
    }
  }
}
.select_input {
  width: 80%;
}
::v-deep .el-date-editor {
  width: 75%;
}
::v-deep .el-select {
  width: 75%;
}
.quality_box {
  font-size: 16px;
  display: flex;
  align-items: center;
  color: #fff;
  > div {
    &:first-child {
      width: 48px;
      height: 48px;
      background-size: 48px 48px;
    }
    &:last-child {
      margin-left: 16px;
      margin-right: 48px;
      p {
        &:last-child {
          margin-top: 8px;
        }
      }
    }
  }
  span {
    font-size: 24px;
    font-weight: 500;
  }
  &.quality_icon_1 {
    > div {
      &:first-child {
        background-image: url(~assets/images/quality_1.png);
      }
    }
    span {
      color: #49EB79;
    }
  }
  &.quality_icon_2 {
    > div {
      &:first-child {
        background-image: url(~assets/images/quality_2.png);
      }
    }
    span {
      color: #56FEFE;
    }
  }
  &.quality_icon_3 {
    > div {
      &:first-child {
        background-image: url(~assets/images/quality_3.png);
      }
    }
    span {
      color: #EEC80B;
    }
  }
  &.quality_icon_4 {
    > div {
      &:first-child {
        background-image: url(~assets/images/quality_4.png);
      }
    }
    span {
      color: #EE6E0B;
    }
  }
  &.quality_icon_5 {
    > div {
      &:first-child {
        background-image: url(~assets/images/quality_5.png);
      }
    }
    span {
      color: #FF1111;
    }
  }
  &.quality_icon_6 {
    > div {
      &:first-child {
        background-image: url(~assets/images/quality_6.png);
      }
    }
    span {
      color: #B336F3;
    }
  }
}
</style>
