<!--
 * @Autor: ysl
 * @Date: 2021-08-09 19:58:25
 * @LastEditors: ysl
 * @LastEditTime: 2021-09-16 15:40:53
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
        <i @click="changeFullscreen" :class="['el-icon-full-screen','full-screen-icon']"></i>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <el-row v-if="code == '资产总览' || code == '报废'">
          <div class="count-panel">
            <div class="count-item">
              <i class="icon-count icon-sybh-count-1"></i>
              <div class="text">
                <p>资产总数</p>
                <p class="num">{{ assetsObj.totalNum || 0 }} <span>件</span></p>
              </div>
            </div>
            <div class="count-item" v-if="code == '资产总览'">
              <i class="icon-count icon-sybh-count-2"></i>
              <div class="text">
                <p>到期件数</p>
                <p class="num outNum">{{ assetsObj.expireNum || 0 }} <span>件</span></p>
              </div>
            </div>
            <div class="count-item" v-if="code == '资产总览'">
              <i class="icon-count icon-sybh-count-3"></i>
              <div class="text">
                <p>资产总额</p>
                <p class="num">{{ assetsObj.totalAmount || 0 }} <span>万元</span></p>
              </div>
            </div>
            <div class="count-item" v-if="code == '报废'">
              <i class="icon-count icon-sybh-count-4"></i>
              <div class="text">
                <p>报废总数</p>
                <p class="num outNum">{{ assetsObj.expireNum || 0 }} <span>件</span></p>
              </div>
            </div>
          </div>
        </el-row>
        <el-row>
          <el-col :span="24" class="water-supply-select-panel" v-if="code == '用水效率'">
            <div
              :class="[
                activeTabCarry == item ? 'active' : '',
                'water-supply-select-btn',
              ]"
              @click="changeActiveTabCarry(item)"
              v-for="(item, index) in waterEfficiencyTabList" :key="index"
            >
              {{ item }}
            </div>
          </el-col>
          <el-col :span="24" class="water-supply-select-panel" v-if="code == '节水指标'">
            <div
              :class="[
                handleSavingActionTab == index ? 'active' : '',
                'water-supply-select-btn',
              ]"
              @click="changeSavingTabCarry(index)"
              v-for="(item, index) in savingActionTabList" :key="index"
            >
              {{ item.label }}
            </div>
          </el-col>
          <el-col :span="24" class="water-supply-select-panel" v-if="code == '三条红线'">
            <div
              :class="[
                handleLineActionTab == index ? 'active' : '',
                'water-supply-select-btn',
              ]"
              @click="changeLineTabCarry(index)"
              v-for="(item, index) in lineActionTabList" :key="index"
            >
              {{ item.label }}
            </div>
          </el-col>
        </el-row>
				<el-row :gutter="20" class="query-sty">
          <!-- 年份筛选 -->
          <el-col :span="6" v-if="code == '节水指标' || code == '三条红线' || code == '合同管理' || code == '领用'">
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
          <el-col :span="6" v-if="code == '出差管理' || code == '公务接待' || code == '合同管理' || code == '报废' || code == '政务内部职能管理' || code == '市内出租车' || code == '公务用车' || code == '其他报销' || code == '会议培训'">
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
              <el-select
                v-model="deptName"
                size="small"
                @change="changeDept"
              >
                <el-option
                  v-for="item in deptOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                />
              </el-select>
            </div>
          </el-col>
          <!-- 时间筛选 -->
          <el-col :span="6" v-if="code == '前期影响雨量'">
            <div class="demo-input-suffix">
              <p style="width: 80px">时间筛选：</p>
               <el-date-picker
                v-model="checkTime"
                size="small"
                clearable
                :picker-options="pickerOptions1"
                @change="handleDate"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                placeholder="选择日期">
              </el-date-picker>
            </div>
          </el-col>
          <!-- 关键词筛选 -->
          <el-col :span="6" v-if="code == '水源保障' || code == '领用' || code == '报废' || code == '资产总览'">
            <div class="demo-input-suffix">
              <p style="width: 40%">关键词：</p>
              <el-input
                placeholder="请输入关键词"
                v-model="keyWords"
                size="small"
                class="myClass"
                clearable
                @blur="queryVal"
              />
              <div
                class="derive-sty mg-l16"
                style="width: 80px"
                @click="queryVal"
              >
               查 询
              </div>
            </div>
          </el-col>
          <!-- 行政区划筛选 -->
          <el-col :span="6" v-if="false">
              <div class="demo-input-suffix">
                <p style="width: 80px">行政区划：</p>
                <el-select
                  v-model="administrationValue"
                  size="small"
                  clearable
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
              <el-select
                v-model="waterType"
                size="small"
                @change="getWptList"
              >
                <el-option
                  v-for="item in waterTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                />
              </el-select>
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
              prop="areaInNum"
              label="市内出差次数"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="areaOutNum"
              label="市外出差次数"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="peopleNum"
              label="出差人次"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="totalCost"
              width="160px"
              label="出差报销金额(元)"
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
              prop="count"
              label="接待次数"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="actualAmount"
              width="160px"
              label="报销金额(元)"
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
         <!-- 水源保障 -->
        <div class="tab-list-sty" v-if="code == '水源保障'">
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
              prop="name"
              label="水源地名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="supportWater"
              label="水源可用水量"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="dayWatSupply"
              label="日平均供水量"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="supplyAbility"
              width="160px"
              label="供水能力(天)"
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
              v-for="(item, index) in savingActionTabList[handleSavingActionTab].columnsList"
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
              v-for="(item, index) in lineActionTabList[handleLineActionTab].columnsList"
              :key="index"
              :prop="item.props"
              :label="item.label"
            />
          </el-table>
        </div>
        <!-- 资产管理 -->
        <div class="tab-list-sty" v-if="code == '资产总览' || code == '报废' || code == '领用'">
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
        <!-- 市内出租车&公务用车&其他报销 -->
        <div class="tab-list-sty" v-if="code == '市内出租车' || code == '公务用车' || code == '其他报销'">
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
              :sortable="'custom'"
              :formatter="toFixedFormatter"
              prop="drp"
              label="前期影响雨量(mm)"
            />
          </el-table>
        </div>
      </div>
		</el-dialog>
	</div>
</template>
<script>
import { oaApi } from "@/api/governmentOaApi";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { disasterPreventionApi } from "@/api/RightSildePanel";
export default {
  name: "governmentDialog",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    code: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      pickerOptions1: {
        disabledDate(time) {
          return time.getTime() < 1627747200000;
        },
      },
      monthOption: [],
      checkMonth: "",
      yearOption: [],
      checkYear: 2020,
      checkTime: "",
			tableData: [],
      //表格loading
      loading: false,
      keyWords: "",
      activeTabCarry: "万元GDP用水量",
      administrationValue: "", // 选中行政区划
      administration: [], // 行政区划
      compareType: "省内", // 选中对比对象
      compareTypeOptions: [ "省内", "国内", "国际" ], // 对比对象
      waterType: "城市生活", // 取水用途
      waterTypeOptions: [
        { label: "城市生活", value: 1 },
        { label: "农村生活", value: 2 },
        { label: "工业", value: 3 },
        { label: "农业", value: 4 },
        { label: "水力发电", value: 5 },
        { label: "其他", value: 6 },
        { label: "非水力发电", value: 7 },
      ],
      waterEfficiencyProp: "wpg",
      waterEfficiencyColumn: "万元GDP用水量(m³/万元)", 
      waterEfficiencyTabList: [ "万元GDP用水量", "万元工业增加值用水量" ],
      handleSavingActionTab: 0,
      savingActionTabList: [
        { 
          label: "年用水总量", 
          columnsList: [
            { label: "用水量(亿m³)", props: "totalWatCsm" },
            { label: "十四五控制指标(亿m³)", props: "totalWatCsm5Norm" },
          ],
          value: 0
        },{ 
          label: "万元GDP用水量", 
          columnsList: [
            { label: "万元GDP用水量(m³/万元)", props: "wpg" },
            { label: "万元GDP用水量下降率(%)", props: "wpgDecrRate" },
            { label: "十四五控制指标(%)", props: "wpgDecrRate5Norm" },
          ],
          value: 1
        },{ 
          label: "万元工业增加值用水量", 
          columnsList: [
            { label: "万元工业增加值用水量(m³/万元)", props: "induWpg" },
            { label: "万元工业增加值用水量下降率(%)", props: "induWpgDecrRate" },
            { label: "十四五控制指标(%)", props: "induWpgDecrRate5Norm" },
          ],
          value: 2
        },{ 
          label: "农业灌溉水有效利用系数", 
          columnsList: [
            { label: "农业灌溉水有效利用系数", props: "agIrrEffectFactor" },
            { label: "十四五控制指标", props: "agIrrEffectFactor5Norm" },
          ],
          value: 3
        },{ 
          label: "县域节水型社会创建达标率", 
          columnsList: [
            { label: "县域节水型社会创建达标率", props: "watSavRate" },
            { label: "十四五控制指标", props: "watSavRate5Norm" },
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
            { label: "控制指标(亿m³)", props: "controlWatCsm" },
          ],
          value: 0
        },{ 
          label: "用水效率", 
          columnsList: [
            { label: "万元GDP用水量下降率(%)", props: "wpgDecrRate" },
            { label: "控制指标(%)", props: "wpgDecrRateNorm" },
          ],
          value: 1
        },{ 
          label: "限制纳污", 
          columnsList: [
            { label: "水功能区水质达标率(%)", props: "wpgDecrRate" },
            { label: "控制指标(%)", props: "wpgDecrRateNorm" },
          ],
          value: 2
        }
      ], // 三条红线
      deptName: "", // 使用部门
      deptOptions: [], // 使用部门列表
      assetsObj: {},
      assetsTableList: {
        "资产总览": [
          { label: "资产总额(万元)", props: "totalWatCsm" },
          { label: "资产件数", props: "controlWatCsm" },
          { label: "到期件数", props: "controlWatCsm" },
        ],
        "报废": [
          { label: "资产件数", props: "totalWatCsm" },
          { label: "报废件数", props: "controlWatCsm" },
        ],
        "领用": [
          { label: "领用件数", props: "totalWatCsm" },
        ],
      },
      insideTableList: [
        { label: "主办件个数(个)", props: "hostPartNum" },
        { label: "会办件个数(个)", props: "handlePartNum" },
        { label: "领导批示文件个数(个)", props: "instruFileNum" },
        { label: "重要文件个数(个)", props: "importFileNum" },
      ],
      conferenceList: [
        { label: "实际报销金额(万元)", props: "totalCost" },
        { label: "培训次数", props: "trainNumber" },
        { label: "预计住宿费支出(万元)", props: "hotelCost" },
        // { label: "预计伙食费支出(万元)", props: "importFileNum" },
        { label: "预计其他费用(万元)", props: "otherCost" },
      ],
      fullscreen:false,
		}
	},
	created() {
    this.getmonthOption();
    this.getYearOption();
    this.getArea();
	},
	methods: {
    changeFullscreen(){
      this.fullscreen=!this.fullscreen;
      this.$refs.travelTable.doLayout();
    },
		init() {
      switch (this.code) {
        case "出差管理":
			    this.getOaOverView()
          break;
        case "合同管理":
          this.checkYear = 2021
			    this.getOaContractOverView()
          break;
        case "公务接待":
          this.getOaOfficialOverView();
          break;
        case "水源保障":
          this.waterGuaranteeRsvrWatSrcTable()
          break;
        case "用水效率":
          this.getOverView()
          break;
        case "三条红线":
          this.checkYear = 2020
          this.getRedLineData()
          break;
        case "节水指标":
          this.watSavNormByYear()
          break;
        case "取水监管":
          this.getWptList()
          break;
        case "资产总览":
        case "报废":
        case "领用":
          this.getAssetsList()
          break;
        case "政务内部职能管理":
          this.getInsideList()
          break;
        case "市内出租车":
          this.getTaxiExpenseDetailedTable()
          break;
        case "公务用车":
          this.getStatisDetailedTable()
          break;
        case "其他报销":
          this.getOtherExpenseDetailedTable()
          break;
        case "会议培训":
          this.getMeetTrainDetailedTable()
          break;
        case "前期影响雨量":
          this.getPrecedeAffectRainfall()
          break;
      }
		},
    // 搜索
    queryVal() {
      switch (this.code) {
        case "水源保障":
          this.waterGuaranteeRsvrWatSrcTable()
          break;
        case "资产总览":
        case "报废":
        case "领用":
          this.getAssetsList()
          break;
      }
    },
    // 用水效率详表tab切换
    changeActiveTabCarry(val) {
      this.activeTabCarry = val;
      switch (val) {
        case "万元GDP用水量":
          this.waterEfficiencyProp = "wpg"
          this.waterEfficiencyColumn = "万元GDP用水量(m³/万元)"
          break;
        case "万元工业增加值用水量":
          this.waterEfficiencyProp = "induWpg"
          this.waterEfficiencyColumn = "万元工业增加值用水量(m³/万元)"
          break;
      }
    },
    // 节水指标详表tab切换
    changeSavingTabCarry(val) {
      this.handleSavingActionTab = val
      this.watSavNormByYear()
      switch (val) {
        case "年用水总量":

          break;
        case "万元GDP用水量":

          break;
      }
    },
    // 三条红线
    changeLineTabCarry(val) {
      this.handleLineActionTab = val
      this.getRedLineData()
    },
    // 获取年份选择下拉内容
    getYearOption() {
      let date = new Date();
      let y = date.getFullYear();
      this.yearOption = [{ label: "全部", value: "" }];
      for (let i = y; i >= 1990; i--) {
        this.yearOption.push({
          label: i + " 年",
          value: i,
        });
      }
    },
    // 下拉年份选择后
    handleYear() {
      switch (this.code) {
        case "合同管理":
			    this.getOaContractOverView()
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
      let m = date.getMonth();
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
          this.getTaxiExpenseDetailedTable()
          break;
        case "公务用车":
          this.getStatisDetailedTable()
          break;
        case "其他报销":
          this.getOtherExpenseDetailedTable()
          break;
        case "会议培训":
          this.getMeetTrainDetailedTable()
          break;
      }
    },
    // 时间筛选
    handleDate() {
      this.getPrecedeAffectRainfall()
    },
    // 变更部门
    changeDept() {

    },
    // 获取部门
    getDeptList() {

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
    // 获取出差管理数据
    async getOaOverView() {
      this.loading = true;
      let opt = {
        type: 5,
        moon: this.checkMonth,
      };
      await oaApi.getOaOverView(opt).then((res) => {
				res.code == 0 ? this.tableData = res.data.list : []
      	this.loading = false;
			})
    },
    // 合同管理列表
    async getOaContractOverView() {
      this.loading = true;
      let month = this.checkMonth < 10 ? "0" + this.checkMonth : this.checkMonth
      let opt = {
        year: this.checkYear + "-" + month,
      };
      await oaApi.contractLoadPage(opt).then((res) => {
				res.code == 0 ? this.tableData = res.data.list : []
      	this.loading = false;
			})
    },
    // 公务接待列表
    async getOaOfficialOverView() {
      this.loading = true;
      let month = this.checkMonth < 10 ? "0" + this.checkMonth : this.checkMonth
      let opt = {
        month: month,
      };
      await oaApi.detailedTableComponents(opt).then((res) => {
				res.code == 0 ? this.tableData = res.data : []
      	this.loading = false;
			})
    },
    // 获取水源保障统计数据
    async waterGuaranteeRsvrWatSrcTable() {
      this.loading = true;
      let opt = {
        watSrcName: this.keyWords
      };
      await waterResourceApi.waterGuaranteeRsvrWatSrcTable(opt).then((res) => {
				res.code == 0 ? this.tableData = res.data : []
      	this.loading = false;
      });
    },
    // 获取用水效率数据
    async getOverView() {
      this.loading = true;
      let opt = {
        areaLevel: this.compareType,
      };
      this.barDataList = []
      this.firstData = []
      this.maxValue = 0
      await waterResourceApi.wpgEffect(opt).then((res) => {
				res.code == 0 ? this.tableData = res.data.map(item => {
          item.wpg = Number(item.wpg).toFixed(2) 
          item.induWpg = Number(item.induWpg).toFixed(2)
          return item
        }) : []
      	this.loading = false;
      });
    },
    // 获取三条红线数据
    async getRedLineData(type) {
      this.loading = true;
      let opt = { year: this.checkYear};
      let str = this.handleLineActionTab == 0 ? "redLineWatCsm" : "redLineWpgEffect"
      this.tableData = [];
      await waterResourceApi[str](opt).then((res) => {
        this.loading = false;
				res.code == 0 ? this.tableData = res.data.list.map(item => {
            item.controlWatCsm = item.controlWatCsm ? item.controlWatCsm.toFixed(2) : 0
            item.totalWatCsm = item.totalWatCsm ? item.totalWatCsm.toFixed(2) : 0
            item.wpgDecrRateNorm = item.wpgDecrRateNorm ? item.wpgDecrRateNorm.toFixed(2) : 0
            item.wpgDecrRate = item.wpgDecrRate ? item.wpgDecrRate.toFixed(2) : 0
          return item
        }) : []
      	this.loading = false;
      });
    },
    // 获取节水指标数据
    async watSavNormByYear() {
      this.loading = true;
      let opt = { 
        compareType: this.handleSavingActionTab + 1,
        year: this.checkYear, 
      };
      await waterResourceApi.watSavNormByYear(opt).then((res) => {
          this.loading = false;
          res.code == 0 ? this.tableData = res.data.map(item => {
            item.totalWatCsm = item.totalWatCsm ? Number(item.totalWatCsm).toFixed(2) : 0
            item.induWpg = item.induWpg ? Number(item.induWpg).toFixed(2) : 0
            item.wpg = item.wpg ? Number(item.wpg).toFixed(2) : 0
            return item
          }) : ""
      })
    },
    // 获取取水监管数据
    async getWptList() {
      this.loading = true;
      let opt = { 
        typeQslx: this.waterType
      };
      await waterResourceApi.getWptList(opt).then((res) => {
          this.loading = false;
          res.code == 0 ? this.tableData = res.data.map(item => {
            item.intDl = item.intDl && item.intDl.substring(0, 10)
            return item
          }) : ""
      })
    },
    // 获取资产管理数据
    async getAssetsList() {
      // let opt = { 
      //   year,
      //   month,
      //   keyWords: this.keyWords,

      // };
      // await oaApi.getWptList(opt).then((res) => {
      //     res.code == 0 ? this.tableData = res.data.map(item => {
      //       item.intDl = item.intDl && item.intDl.substring(0, 10)
      //       return item
      //     }) : ""
      // })
    },
    // 获取政务内部职能管理数据
    async getInsideList() {
      this.loading = true;
      let month = this.checkMonth < 10 ? "0" + this.checkMonth : this.checkMonth
      let opt = { 
        month,
      };
      await oaApi.proposalDetailedTable(opt).then((res) => {
          this.loading = false;
          res.code == 0 ? this.tableData = res.data : ""
      })
    },
    // 获取市内出租车数据
    async getTaxiExpenseDetailedTable() {
      this.loading = true;
      let month = this.checkMonth < 10 ? "0" + this.checkMonth : this.checkMonth
      let opt = { 
        month,
      };
      await oaApi.taxiExpenseDetailedTable(opt).then((res) => {
          this.loading = false;
          res.code == 0 ? this.tableData = res.data : ""
      })
    },
    // 获取公务用车数据
    async getStatisDetailedTable() {
      this.loading = true;
      let month = this.checkMonth < 10 ? "0" + this.checkMonth : this.checkMonth
      let opt = { 
        month,
      };
      await oaApi.statisDetailedTable(opt).then((res) => {
          this.loading = false;
          res.code == 0 ? this.tableData = res.data : ""
      })
    },
    // 获取其他报销数据
    async getOtherExpenseDetailedTable() {
      this.loading = true;
      let month = this.checkMonth < 10 ? "0" + this.checkMonth : this.checkMonth
      let opt = { 
        month,
      };
      await oaApi.otherExpenseDetailedTable(opt).then((res) => {
          this.loading = false;
          res.code == 0 ? this.tableData = res.data : ""
      })
    },
    // 获取会议培训数据
    async getMeetTrainDetailedTable() {
      this.loading = true;
      let month = this.checkMonth < 10 ? "0" + this.checkMonth : this.checkMonth
      let opt = { 
        month,
      };
      await oaApi.meetTrainDetailedTable(opt).then((res) => {
          this.loading = false;
          res.code == 0 ? this.tableData = res.data : ""
      })
    },
    // 获取前期影响雨量数据
    async getPrecedeAffectRainfall() {
      this.loading = true;
      let theDay = this.checkTime && this.checkTime.substring(0, 10)
      let opt = { 
        theDay,
      };
      await waterResourceApi.precedeAffectRainfall(opt).then((res) => {
          this.loading = false;
          res.code == 0 ? this.tableData = res.data.map( item => {
            item.date = item.date.substring(0, 10)
            item.drp = item.drp && Number(item.drp).toFixed(2)
            return item
          }) : ""
      })
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
					this.tableData.sort(this.sortBy(val.prop, false))
        } else {
					this.tableData.sort(this.sortBy(val.prop, true))
        }
      } else {
				this.tableData.sort(this.sortBy(val.prop, false))
      }
		},
		sortBy(attr,rev) {
			rev = rev ? 1 : -1
      return function(a,b){
        a = a[attr];
        b = b[attr];
        if(a < b){
          return rev * -1;
        }
        if(a > b){
          return rev * 1;
        }
        return 0;
      }
    },
	}
}
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
    padding: 0;
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
      width: 15%;
      display: flex;
      align-items: center;
      margin: 5px 0 0;
      .text {
        font-size: 14px;
        margin-left: 10px;
        line-height: 25px;
        color: #ffffff;
        .num {
          font-size: 20px;
          color: #56fefe;
        }
        .outNum {
          color: #E89100;
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
		@include flexflow(column,nowrap);
    height: 680px;
    &.is-fullscreen{
			height:100%;
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
        .full-screen-icon{
					position:absolute;
					right:40px;
					color:#00c1ff;
          cursor: pointer;
          top:15px;
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
        height:100%;
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
</style>
