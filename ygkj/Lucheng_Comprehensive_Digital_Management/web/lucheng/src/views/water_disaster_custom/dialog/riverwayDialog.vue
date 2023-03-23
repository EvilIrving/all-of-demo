<template>
  <div
    :class="['primary-dialog', leftSide == true ? 'project-dialog-wrap' : '']"
  >
    <el-dialog
      :visible.sync="visible"
      :width="width"
      :before-close="handleClose"
      :destroy-on-close="true"
      style="z-index: 3000"
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      :fullscreen="fullscreen"
    >
      <!-- 标题 -->
      <div
        class="primary-dialog__title"
        slot="title"
        :style="minimizeStatus ? 'justify-content: space-between;' : ''"
      >
        <p class="primary-dialog__title--label" style="color: #fff">
          {{ riverwayName }}
        </p>
        <i
          @click="changeFullscreen"
          :class="['el-icon-full-screen', 'full-screen-icon']"
        ></i>
      </div>
      <!-- 内容 -->
      <div class="primary-dialog__content">
        <div v-if="riverwayName != '河道详情列表'">
          <!-- tab切换 -->
          <div>
            <!-- 河道 -->
            <span
              :class="tabSwitch == '基础信息' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('基础信息')"
              >基础信息</span
            >
            <span
              :class="tabSwitch == '健康信息' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('健康信息')"
              >健康信息</span
            >
            <span
              :class="tabSwitch == '巡查信息' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('巡查信息')"
              >巡查信息</span
            >
            <i
              v-if="backData"
              style="float: right"
              class="icon-get-back"
              @click="backtrack"
            />
          </div>
          <!-- 河道基础信息 -->
          <div v-if="!backData" style="margin-top: 15px">
            <!-- 基础信息 -->
            <el-row v-if="tabSwitch == '基础信息'">
              <el-col :span="24">
                <ProjectItem title="河道基础信息" />
                <el-col
                  :span="11"
                  class="riverway-sty"
                  v-for="(item, i) in channelLnformationList"
                  :key="i"
                >
                  <span>{{ item.label }}</span>
                  <span>{{ rivSegInfoList[item.prop] }}</span>
                </el-col>
              </el-col>
              <el-col :span="24">
                <ProjectItem title="河长基础信息">
                  <span
                    v-for="(item, i) in riverChiefName"
                    :key="i"
                    v-show="riverChief"
                    :class="
                      riverChief == item ? 'tab-switch' : 'tab-switch-sty'
                    "
                    @click="riverChiefClick(item)"
                    >{{ item }}</span
                  >
                </ProjectItem>
                <el-col
                  v-show="riverChief"
                  style="margin: 10px 38px 10px 0"
                  :span="11"
                  class="riverway-sty"
                  v-for="(item, i) in riverChiefList"
                  :key="i"
                >
                  <span>{{ item.label }}</span>
                  <span>{{ riverChiefData[item.prop] }}</span>
                </el-col>
              </el-col>
            </el-row>
            <!-- 健康信息 -->
            <el-row v-if="tabSwitch == '健康信息'" style="margin-top: 10px">
              <ProjectItem title="健康信息" />
              <el-col
                :span="13"
                style="
                  margin-top: 20px;
                  display: flex;
                  justify-content: space-between;
                "
              >
                <div style="width: 95%">
                  <div class="health-code-sty">
                    <i
                      v-if="rivSegInfoList.healthState == 1"
                      class="icon-the-green-code"
                    />
                    <i
                      v-if="rivSegInfoList.healthState == 2"
                      class="icon-the-yellow-code"
                    />
                    <i
                      v-if="rivSegInfoList.healthState == 3"
                      class="icon-the-red-code"
                    />
                  </div>
                  <div class="health-sty">
                    <p>
                      河湖“四乱”清零<span>{{
                        rivSegInfoList.noneFourChaos == 1 ? "是" : "否"
                      }}</span>
                    </p>
                    <p>
                      河湖管护到位
                      <span>{{
                        rivSegInfoList.riverManageWell == 1 ? "是" : "否"
                      }}</span>
                    </p>
                  </div>
                  <div class="health-sty">
                    <p>
                      无乱点河道创建
                      <span>{{
                        rivSegInfoList.noneIllegalRiverCreate == 1 ? "是" : "否"
                      }}</span>
                    </p>
                    <p>
                      水域岸线占补平衡
                      <span>{{
                        rivSegInfoList.shorelineOccupyBalance == 1 ? "是" : "否"
                      }}</span>
                    </p>
                  </div>
                  <div class="health-sty">
                    <p>
                      涉河建设项目批建相符
                      <span>{{
                        rivSegInfoList.riverProjTallied == 1 ? "是" : "否"
                      }}</span>
                    </p>
                    <p>
                      公众满意
                      <span>{{
                        rivSegInfoList.publicSatisfy == 1 ? "是" : "否"
                      }}</span>
                    </p>
                  </div>
                </div>
                <div class="boundary-sty"></div>
              </el-col>
              <el-col :span="11">
                <div id="radarMap" style="width: 450px; height: 500px"></div>
              </el-col>
            </el-row>
            <!-- 巡查信息 -->
            <div v-if="tabSwitch == '巡查信息'" style="margin-top: 10px">
              <el-row class="under-frame-sty">
                <el-col :span="6" class="inspect-number-sty">
                  <p><i class="icon-amount" /></p>
                  <div>
                    <p>本月巡查总次数</p>
                    <p>
                      <span>{{ statsLoadPageList.times }}</span
                      >次
                    </p>
                  </div>
                </el-col>
                <el-col :span="6" class="inspect-number-sty">
                  <p><i class="icon-amount" /></p>
                  <div>
                    <p>本月巡查总长度</p>
                    <p>
                      <span>{{ statsLoadPageList.inspectLength }}</span
                      >km
                    </p>
                  </div>
                </el-col>
                <el-col :span="6" class="inspect-number-sty">
                  <p><i class="icon-amount" /></p>
                  <div>
                    <p>本月巡查总时长</p>
                    <p>
                      <span>{{ statsLoadPageList.inspectTime }}</span
                      >h
                    </p>
                  </div>
                </el-col>
                <el-col :span="6" class="inspect-number-sty">
                  <p><i class="icon-hong-ma" /></p>
                  <div>
                    <p>本月巡查问题总数</p>
                    <p>
                      <span style="color: red">{{
                        statsLoadPageList.problemNum
                      }}</span
                      >个
                    </p>
                  </div>
                </el-col>
              </el-row>
              <el-row style="margin: 15px 0">
                <el-col :span="5">
                  <p style="font-size: 16px; color: #fff; margin-bottom: 10px">
                    所属单位
                  </p>
                  <el-select v-model="affiliatedUnitData" placeholder="请选择">
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-col>
                <el-col :span="5">
                  <p style="font-size: 16px; color: #fff; margin-bottom: 10px">
                    巡查开始时间
                  </p>
                  <el-date-picker
                    value-format="yyyy-MM-dd HH:mm"
                    format="yyyy-MM-dd HH:mm"
                    v-model="startTime"
                    type="datetime"
                    placeholder="巡查时间"
                  />
                </el-col>
                <el-col :span="5">
                  <p style="font-size: 16px; color: #fff; margin-bottom: 10px">
                    巡查结束时间
                  </p>
                  <el-date-picker
                    value-format="yyyy-MM-dd HH:mm"
                    format="yyyy-MM-dd HH:mm"
                    v-model="endTime"
                    type="datetime"
                    placeholder="巡查时间"
                  />
                </el-col>
                <el-col :span="5">
                  <p style="font-size: 16px; color: #fff; margin-bottom: 10px">
                    巡查人员
                  </p>
                  <el-input v-model="inspectorsData" placeholder="请输入" />
                </el-col>
                <el-col :span="4">
                  <span class="btn-sty" @click="inquire">查询</span>
                  <span class="btn-sty" @click="empty">清空</span>
                </el-col>
              </el-row>
              <!-- 巡查列表 -->
              <div class="table-box" style="height: 380px">
                <primary-map-table
                  :columnList="columnLists"
                  @rowClick="rowClick"
                  :dataList="loadPageList"
                />
              </div>
            </div>
          </div>
          <!-- 巡查路劲 -->
          <div
            v-if="backData"
            style="margin-top: 15px; height: 780px; overflow-y: auto"
          >
            <ProjectItem title="巡查路径" />
            <div style="padding-bottom: 10px; height: 470px">
              <ComparativeHistoryMap
                v-if="visible"
                :mapShowsList="mapShowsList"
              />
            </div>
            <!-- 巡查情况 -->
            <ProjectItem title="巡查情况" />
            <el-row>
              <el-col
                :span="11"
                class="riverway-sty"
                v-for="(item, i) in columnLists"
                :key="i"
                v-show="item.label != '巡查问题(个)'"
              >
                <span>{{ item.label }}</span>
                <span>{{ inspectorsList[item.prop] }}</span>
              </el-col>
            </el-row>
            <!-- 问题记录 -->
            <div>
              <ProjectItem title="问题记录" />
              <el-collapse
                v-model="activeName"
                accordion
                v-if="inspectorsList.patrolProblemRespVos"
              >
                <el-collapse-item
                  v-for="(item, i) in inspectorsList.patrolProblemRespVos"
                  :key="i"
                  :title="'问题点' + `${i + 1}` + ':' + item.problemDescription"
                  :name="i"
                >
                  <el-row style="padding: 20px 0 0 20px">
                    <el-col :span="11" class="riverway-sty">
                      <span>详细地址</span>
                      <span>{{ item.address }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>所在乡镇</span>
                      <span>{{ item.town }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>所在街道</span>
                      <span>{{ item.street }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>地理坐标</span>
                      <span>{{ item.lng }}-{{ item.lat }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>问题来源</span>
                      <span>{{ item.problemSource }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>所在河湖</span>
                      <span>{{ item.riverName }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>河道等级</span>
                      <span>{{ item.riverLevel }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>河长姓名</span>
                      <span>{{ item.riverManagerName }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>四乱类型</span>
                      <span>{{ item.fourType }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>严重程度</span>
                      <span>{{ item.severity }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>备注说明</span>
                      <span>{{ item.remark }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>占地面积</span>
                      <span>{{ item.occupySpace }}</span>
                    </el-col>
                    <el-col :span="23" class="riverway-sty">
                      <span>整改要求</span>
                      <span>{{ item.rectificationRequirement }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>整改前图像</span>
                      <span>{{ item.rectImgbeforeList }}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>整改后图像</span>
                      <span>{{ item.rectImgAfter }}</span>
                    </el-col>
                  </el-row>
                </el-collapse-item>
              </el-collapse>
              <div v-else style="text-align: center">
                <i class="icon-no-record"></i>
              </div>
            </div>
          </div>
        </div>
        <!-- 河道统计 -->
        <div class="collect-sty" v-if="riverwayName == '河道详情列表'">
          <el-row>
            <el-col
              :span="4"
              style="display: flex; justify-content: left; margin-right: 15px"
            >
              <i class="icon-amount"></i>
              <div style="margin-left: 20px">
                <p style="margin-bottom: 10px">河道总数</p>
                <span style="font-size: 24px; color: #56fefe">{{
                  collectList.rivGreenNum
                }}</span>
                个
              </div>
            </el-col>
            <el-col
              :span="4"
              style="display: flex; justify-content: left; margin-right: 15px"
            >
              <i class="icon-green-ma"></i>
              <div style="margin-left: 20px">
                <p style="margin-bottom: 10px">绿码</p>
                <span style="font-size: 24px; color: #56fefe">{{
                  collectList.rivSegNum
                }}</span>
                个
              </div>
            </el-col>
            <el-col
              :span="4"
              style="display: flex; justify-content: left; margin-right: 15px"
            >
              <i class="icon-huang-ma"></i>
              <div style="margin-left: 20px">
                <p style="margin-bottom: 10px">黄码</p>
                <span style="font-size: 24px; color: #56fefe">{{
                  collectList.rivYellowNum
                }}</span>
                个
              </div>
            </el-col>
            <el-col :span="4" style="display: flex; justify-content: left">
              <i class="icon-hong-ma"></i>
              <div style="margin-left: 20px">
                <p style="margin-bottom: 10px">红码</p>
                <span style="font-size: 24px; color: #56fefe">{{
                  collectList.rivRedNum
                }}</span>
                个
              </div>
            </el-col>
          </el-row>
        </div>
        <!-- 筛选项 -->
        <div class="select-group" v-if="riverwayName == '河道详情列表'">
          <div class="select-box">
            河道等级:
            <el-select
              v-model="level"
              placeholder="河道等级"
              @change="pageRivSegBaseInfo"
            >
              <el-option
                v-for="item in levelList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </div>
          <div class="select-box">
            健康分析:
            <el-select
              v-model="healthState"
              placeholder="健康分析"
              @change="pageRivSegBaseInfo"
            >
              <el-option
                v-for="item in healthStateList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </div>
          <div class="select-box">
            河道名称:
            <el-input
              style="width: 270px"
              v-model="projName"
              placeholder="请输入"
              @change="pageRivSegBaseInfo"
            />
          </div>
          <!-- 运行状态 -->
          <div class="select-box">
            运行状态:
            <el-select
              v-model="runningStatusData"
              placeholder="请选择"
              @change="pageRivSegBaseInfo"
            >
              <el-option
                v-for="item in runningStatusList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </div>
        </div>
        <!-- 列表 -->
        <div v-if="riverwayName == '河道详情列表'">
          <el-table
            ref="multipleTable"
            :data="dataList"
            stripe
            v-loading="loading"
            width="100%"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
          >
            <el-table-column type="index" width="80">
              <template slot-scope="scope">
                <i
                  :class="
                    scope.row.healthState == '1'
                      ? 'icon-green-code'
                      : scope.row.healthState == '2'
                      ? 'icon-Yellow-code'
                      : 'icon-red-code'
                  "
                />
              </template>
            </el-table-column>
            <el-table-column prop="index" label="序号" width="80">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="riverName"
              label="河道名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="areaName"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="levelName"
              label="河道等级"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="startName"
              label="起点名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="endName"
              label="终点名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="length"
              label="河道长度(km)"
            />
          </el-table>
          <!-- 分页 -->
          <div class="pagination-area" style="margin-top: 15px">
            <el-pagination
              class="right"
              @current-change="pageChange"
              background
              layout="total, prev, pager, next"
              :total="total"
            />
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import * as echarts from "echarts";
import PrimaryMapTable from "@/components/primary_map_table/";
import ComparativeHistoryMap from "@/components/comparative_history_map/";
import ProjectItem from "@/components/screen_project_dialog/components/ProjectItem";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
export default {
  name: "riverwayDialog",
  components: {
    ProjectItem,
    PrimaryMapTable,
    ComparativeHistoryMap
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    width: {
      type: String,
      default: "1600px",
    },
    leftSide: {
      type: Boolean,
      default: false,
    },
    minimizeStatus: {
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      // 巡查人员表字段
      inspectorsList: {},
      mapShowsList: [],
      loadPageList: [],
      // 巡查列表表头
      columnLists: [
        {
          width: 110,
          prop: "patrolUserName",
          label: "巡查人员",
        },
        {
          // width: 160,
          prop: "deptName",
          label: "所属单位",
        },
        {
          // width: 160,
          prop: "startTime",
          label: "开始时间",
        },
        {
          // width: 160,
          prop: "endTime",
          label: "结束时间",
        },
        {
          width: 160,
          prop: "inspectTime",
          label: "巡查时长(h)",
          sortable: true,
        },
        {
          width: 160,
          prop: "inspectLength",
          label: "巡查长度(km)",
          sortable: true,
        },
        {
          width: 160,
          prop: "problemNum",
          label: "巡查问题(个)",
          sortable: true,
        }
      ],
      inspectorsData: null,
      endTime: null,
      startTime: null,
      options: [],
      affiliatedUnitData: null,
      statsLoadPageList: {},
      riverChief: null,
      riverChiefName: [],
      riverChiefData: {},
      rivSegInfoList: {},
      riverChiefList: [
        {
          label: "河长等级",
          prop: "levelName",
        },
        {
          label: "河长身份",
          prop: "rpostName",
        },
        {
          label: "河长姓名",
          prop: "realName",
        },
        {
          label: "河长职务",
          prop: "post",
        },
        {
          label: "联系方式",
          prop: "phone",
        }
      ],
      channelLnformationList: [
        {
          label: "行政区划",
          prop: "areaName",
        },
        {
          label: "所属流域",
          prop: "watershedName",
        },
        {
          label: "河道等级",
          prop: "levelName",
        },
        {
          label: "起点名称",
          prop: "startName",
        },
        {
          label: "终点名称",
          prop: "endName",
        },
        {
          label: "河道长度",
          prop: "length",
        }
      ],
      backData: false,
      tabSwitch: "基础信息",
      riverwayName: '河道详情列表',
      // 河道汇总
      collectList: {},
      // 河道等级
      level: null,
      // 河道等级数据
      levelList: [
        {
          label: "全部",
          value: null
        }, {
          label: "省级",
          value: "2"
        }, {
          label: "市级",
          value: "3"
        }, {
          label: "县级",
          value: "4"
        }, {
          label: "镇级",
          value: "5"
        }, {
          label: "村级",
          value: "6"
        }
      ],
      // 健康等级
      healthState: null,
      // 健康等级数据
      healthStateList: [
        {
          label: "全部",
          value: null
        }, {
          label: "绿码",
          value: "1"
        }, {
          label: "黄码",
          value: "2"
        }, {
          label: "红码",
          value: "3"
        }
      ],
      // 河道名称
      projName: null,
      // 运行状态选中值
      runningStatusData: null,
      //运行状态
      runningStatusList: [
        {
          label: "全部",
          value: null
        }, {
          label: "正在取水",
          value: "正在取水"
        }, {
          label: "停止取水",
          value: "停止取水"
        }, {
          label: "设备掉线",
          value: "设备掉线"
        }
      ],
      dataList: [],
      loading: false,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      fullscreen: false,
    }
  },
  mounted() {
    this.$nextTick(() => {
      if (Object.keys(this.$store.state.riverway).length != 0) {
        this.riverwayName = this.$store.state.riverway.name;
        this.rivSegInfoById(this.$store.state.riverway.id);
        this.stats4LoadPage();
        this.loadPage();
      } else {
        this.riverwayName = '河道详情列表';
      }
      this.pageRivSegStats();
      this.pageRivSegBaseInfo();
    })
  },
  methods: {
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      this.$refs.multipleTable.doLayout();
    },
    // tab切换
    tabSwitchClick(val) {
      this.tabSwitch = val;
      if (val == '健康信息') {
        this.radarMapApi();
      }
    },
    // 巡查路径返回
    backtrack() {
      this.backData = false;
    },
    // 巡查列表点击某一行事件
    rowClick(val) {
      this.inspectorsList = val;
      this.backData = true;
    },
    // 河长选择
    riverChiefClick(val) {
      this.riverChief = val;
      let str = this.rivSegInfoList.riverChiefList.find((item) => {
        if (item.realName == val) {
          return item;
        }
      });
      this.riverChiefData = str;
    },
    // 河道健康码，基础信息，健康信息接口
    async rivSegInfoById(val) {
      this.riverChiefName = [];
      await waterResourceApi.rivSegInfoById({ id: val, withChiefInfo: true }).then((res) => {
        if (res.code == 0) {
          this.rivSegInfoList = res.data;
          this.riverChiefData = res.data.riverChiefList[0];
          this.riverChief = res.data.riverChiefList[0].realName
          res.data.riverChiefList.forEach((item) => {
            this.riverChiefName.push(item.realName);
          })
        }
      })
    },
    // 分页查找上面的统计
    async stats4LoadPage() {
      let opt = {
        patrolUserName: this.inspectorsData, //巡查人员姓名
        deptName: this.affiliatedUnitData,   //所属单位
        startTime: this.startTime,           //巡查开始时间
        endTime: this.endTime,               //巡查开始时间
        id: this.$store.state.riverway.id
      }
      await waterResourceApi.stats4LoadPage(opt).then((res) => {
        if (res.code == 0) {
          this.statsLoadPageList = res.data;
        }
      });
    },
    // 巡查查询按钮
    inquire() {
      this.loadPage();
      this.stats4LoadPage();
    },
    // 巡查筛选清楚按钮
    empty() {
      this.affiliatedUnitData = null;
      this.inspectorsData = null;
      this.startTime = null;             //巡查开始时间
      this.endTime = null;               //巡查开始时间
    },
    // 巡查列表接口
    async loadPage() {
      let opt = {
        patrolUserName: this.inspectorsData, //巡查人员姓名
        deptName: this.affiliatedUnitData,   //所属单位
        startTime: this.startTime,           //巡查开始时间
        endTime: this.endTime,               //巡查开始时间
      }
      await waterResourceApi.WsPatrolInfo(opt).then((res) => {
        if (res.code == 0) {
          this.loadPageList = res.data.list;
        }
      });
    },
    // 健康信息雷达图
    radarMapApi() {
      var chartDom = document.getElementById('radarMap');
      var myChart = echarts.init(chartDom);
      var option = {
        tooltip: {
          trigger: 'axis'
        },
        radar: {
          indicator: [
            { name: '河湖“四乱”清零' },
            { name: '无乱点河道创建' },
            { name: '水域岸线占补平衡' },
            { name: '涉河建设项目批建相符' },
            { name: '公众满意' },
            { name: '河湖管护到位' }
          ],
          name: {
            textStyle: { color: '#fff' }
          },
          splitArea: {
            show: true,
            areaStyle: {
              opacity: 0.2,
              color: ["#050E30"]  // 图表背景网格的颜色
            }
          },
          splitLine: {
            show: true,
            lineStyle: {
              width: 1,
              color: '#fff' // 图表背景网格线的颜色
            }
          },
          radius: 130,
        },
        series: [{
          symbol: "none", // 去掉图表中各个图区域的边框线拐点
          type: 'radar',
          itemStyle: {
            normal: {
              color: "#33FFFF", // 图表中各个图区域的边框线拐点颜色
              areaStyle: {
                type: 'default'
              }
            }
          },
          data: [
            {
              value: [1, 1, 0, 0, 1, 1],
              itemStyle: {
                normal: {
                  areaStyle: {
                    type: 'default',
                    opacity: 0.8,
                    color: "#33FFFF" // 图表中各个图区域的颜色
                  }
                }
              },
              name: '无乱点河道创建'
            },
            {
              value: [1, 1, 0, 0, 1, 1],
              itemStyle: {
                normal: {
                  areaStyle: {
                    type: 'default',
                    opacity: 0.8,
                    color: "red" // 图表中各个图区域的颜色
                  }
                }
              },
              name: '河湖管护到位'
            }
          ]
        }]
      };
      option && myChart.setOption(option);
    },
    // 河道统计
    async pageRivSegStats() {
      let obj = {
        riverName: this.projName,           //河道名称
        areaCode: this.areaData,            //行政区划
        healthState: this.healthState,      //健康等级
        level: this.level                   //河道等级
      };
      await waterResourceApi.pageRivSegStats(obj).then((res) => {
        if (res.code == 0) {
          this.collectList = res.data;
        }
      })
    },
    // 河道详情统计接口
    async pageRivSegBaseInfo() {
      this.loading = true;
      let obj = {
        riverName: this.projName,           //河道名称
        areaCode: this.areaData,            //行政区划
        healthState: this.healthState,      //健康等级
        level: this.level,                   //河道等级
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        withPathStr: true
      };
      await waterResourceApi.pageRivSegBaseInfo(obj).then((res) => {
        if (res.code == 0) {
          this.dataList = res.data.list;
          this.total = res.data.totalRows;
          this.loading = false;
        }
      }).catch((err) => {
        this.$message.error(err);
        this.loading = false;
      })
    },
    // 分页
    pageChange(page) {
      this.pageNum = page;
      this.pageRivSegBaseInfo();
    },
    handleClose() {
      this.$store.commit("riverwayData", {});
      this.$emit("closeDialog");
    }
  }
}
</script>

<style lang="scss" scoped>
.primary-dialog {
  ::v-deep .el-dialog {
    background: transparent;
    @include flexbox;
    @include flexflow(column, nowrap);
    height: 680px;
    // overflow: auto;
    &.is-fullscreen {
      height: 100%;
      overflow: hidden;
    }
    .el-dialog__header {
      background-size: 18px 2px, 2px 18px;
      padding: 2px;
      height: 54px;
      .primary-dialog__title {
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1f375f 0%, #1b3047 100%);
        height: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 40px;
        position: relative;
        .primary-dialog__title--label {
          background: url(~assets/images/WaterDisastersImage/title-bg.png)
            no-repeat;
          font-size: 16px;
          color: #00c1ff;
          letter-spacing: 0;
          height: 32px;
          width: 80%;
          margin-left: 16px;
          padding-left: 56px;
          font-weight: bold;
          line-height: 32px;
        }
        .full-screen-icon {
          position: absolute;
          right: 40px;
          color: #00c1ff;
          cursor: pointer;
          top: 15px;
        }
        .primary-dialog__title--minimize {
          cursor: pointer;
          margin-right: 48px;
        }
      }
      .el-dialog__headerbtn {
        .el-dialog__close {
          color: #00c1ff;
          font-weight: bold;
        }
      }
    }
    .el-dialog__body {
      background-size: 18px 2px, 2px 18px;
      border-bottom: 0.0625rem solid #00c1ff;
      padding: 2px;
      padding-top: 0;
      @include flex(1);
      .primary-dialog__content {
        @include flexbox;
        @include flexflow(column, nowrap);
        padding: 0 15px;
        // height: 630px;
        height: 100%;
        overflow-y: auto;
        overflow-x: hidden;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
        border-top: 0;
        .collect-sty {
          padding-right: 20px;
          padding-bottom: 15px;
          color: #fff;
          border-bottom: 1px solid rgba($color: #00c1ff, $alpha: 0.2);
        }
        .select-group {
          padding: 16px 0 24px;
          display: flex;
          .select-box {
            color: #fff;
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
    }
  }
  &.project-dialog-wrap {
    ::v-deep .el-dialog {
      margin-left: 20px;
      margin-top: 10vh !important;
    }
  }
}
.pagination-area {
  padding: 0 0 16px 0;
  color: #fff;
  ::v-deep .el-pagination {
    .el-pagination__total {
      color: #fff;
    }
    .el-pagination__jump {
      color: #fff;
    }
    .el-pager {
      > li {
        background: #0000;
        color: #fff;
        &.active {
          background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
          border-radius: 2px;
        }
      }
    }
    .btn-prev,
    .btn-next {
      background: rgba(0, 0, 0, 0);
      color: #fff;
    }
  }
}
.peripheral-analysis {
  display: inline-block;
  margin-left: 10px;
  cursor: pointer;
  background-size: 100% 100%;
  width: 15px;
  height: 15px;
}
.minimize {
  color: #00c1ff;
  font-weight: bold;
  cursor: pointer;
}
.tab-switch {
  padding: 5px 10px;
  margin-right: 15px;
  color: #56fefe;
  width: 150px;
  height: 30px;
  line-height: 30px;
  border: 1px solid #56fefe;
  box-shadow: inset 0 0 5px 0 #56fefe;
}
.under-frame-sty {
  border-bottom: 1px solid rgba($color: #00c1ff, $alpha: 0.2);
}
.inspect-number-sty {
  padding-bottom: 15px;
  padding-right: 50px;
  font-size: 16px;
  display: flex;
  justify-content: space-between;
  > div {
    color: #fff;
    > p:nth-child(2) {
      margin-top: 10px;
      > span {
        margin-right: 10px;
        font-size: 22px;
        color: #33ffff;
      }
    }
  }
}
.tab-switch-sty {
  padding: 5px 10px;
  margin-right: 15px;
  color: #fff;
  width: 160px;
  height: 30px;
  line-height: 30px;
  border: 1px solid #fff;
}
.health-code-sty {
  text-align: center;
  margin-bottom: 50px;
}
.btn-sty {
  background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
  border-radius: 4px;
  padding: 13px 15px;
  float: right;
  line-height: 10px;
  font-size: 16px;
  display: inline-block;
  color: #fff;
  margin-right: 10px;
  margin-top: 25px;
}
.health-sty {
  color: #fff;
  display: flex;
  justify-content: space-between;
  padding: 15px 0;
  > p {
    font-size: 16px;
    font-weight: 600;
    > span {
      color: #33ffff;
      font-size: 18px;
      padding-left: 20px;
    }
  }
}
.boundary-sty {
  width: 2px;
  height: 460px;
  float: right;
  background-image: linear-gradient(
    0deg,
    rgba(12, 61, 54, 0.02) 0%,
    rgba(28, 255, 245, 0.5) 54%,
    rgba(12, 61, 54, 0.5) 100%
  );
}
.riverway-sty {
  height: 50px;
  margin: 0 38px 20px 0;
  border: 1px solid #00c1ff;
  > span:nth-child(1) {
    color: #56fefe;
    text-align: center;
    padding: 0 15px;
    height: 48px;
    line-height: 48px;
    display: inline-block;
    border-right: 1px solid #00c1ff;
    background-image: linear-gradient(
      180deg,
      rgba(8, 42, 53, 0.9) 2%,
      rgba(5, 14, 48, 0.9) 100%
    );
    box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
  }
  > span:nth-child(2) {
    color: #fff;
    padding-left: 15px;
  }
}
</style>