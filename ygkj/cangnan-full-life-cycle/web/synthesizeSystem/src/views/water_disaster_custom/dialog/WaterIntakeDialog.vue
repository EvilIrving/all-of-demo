<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2021-07-15 18:12:26
 * @LastEditors: Please set LastEditors
 * @Description: 取水户落点弹窗
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCard.vue
-->
<template>
  <div class="detail-table">
    <el-dialog
      width="60%"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">{{ name }}</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div class="content-panel">
          <div class="left-content">
            <div class="top-panel">
              <div class="title">
                <i class="icon-diamonds"></i>
                <span>取水量统计</span>
              </div>
              <el-select
                v-model="time"
                class="primary-select"
                @change="timeChange"
                placeholder="请选择时间"
                style="width: 126px"
              >
                <el-option
                  v-for="item in timeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </div>

            <div class="chart-panel">
              <div id="barChart" style="width: 100%; height: 100%"></div>
            </div>
          </div>
          <div class="right-content">
            <div class="tab-panel">
              <div
                :class="['tab-item', activeTab == '取水量列表' ? 'active' : '']"
                @click="changeActiveTab('取水量列表')"
              >
                取水量列表
              </div>
              <div
                :class="['tab-item', activeTab == '基本信息' ? 'active' : '']"
                @click="changeActiveTab('基本信息')"
              >
                基本信息
              </div>
              <div
                :class="['tab-item', activeTab == '取水许可' ? 'active' : '']"
                @click="changeActiveTab('取水许可')"
              >
                取水许可
              </div>
              <div
                :class="['tab-item', activeTab == '档案' ? 'active' : '']"
                @click="changeActiveTab('档案')"
              >
                档案
              </div>
            </div>
            <div class="list-panel" v-if="activeTab == '基本信息'">
              <div
                class="list-item"
                v-for="(item, ind) in listColumn"
                :key="ind"
              >
                <p class="label">{{ item.name }}</p>
                <p class="value">{{ listData[item.prop] }}</p>
              </div>
            </div>
            <el-table
              v-if="activeTab == '取水量列表'"
              ref="multipleTable"
              :data="tbList1"
              height="90%"
              width="100%"
              stripe
              class="abc"
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
                prop="updateTime"
                label="时间"
                width="160"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="watUse"
                label="取水量(万m³)"
              />
            </el-table>
            <el-table
              v-if="activeTab == '取水许可'"
              ref="multipleTable"
              :data="tbList2"
              height="90%"
              width="100%"
              stripe
              class="abc"
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
                prop="applSn"
                label="许可证编号"
                width="200"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="wrOrgCode"
                label="关联取水户组织机构编码"
                width="200"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="orgAddr"
                label="单位地址"
                width="200"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="applDt"
                label="申请日期"
                width="150"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="intBd"
                label="取水起始日期"
                width="150"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="typeSylx"
                label="水源类型"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="typeQslx"
                label="取水类型"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="wpermitsize"
                label="取水许可量"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="spsj"
                label="审批时间"
                width="150"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="spdw"
                label="审批单位"
                width="150"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="jdgldw"
                label="监督管理单位"
                width="150"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="adcd"
                label="行政区划"
              />
            </el-table>
            <el-table
              v-if="activeTab == '档案'"
              ref="multipleTable"
              :data="tbList3"
              height="90%"
              width="100%"
              stripe
              class="abc"
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
                v-if="activeTab == '档案'"
                :show-overflow-tooltip="true"
                prop="name"
                label="名称"
              />
              <el-table-column
                v-if="activeTab == '档案'"
                :show-overflow-tooltip="true"
                prop="fileIds"
                label="附件"
              />
            </el-table>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { generateDataZoom } from "../cards/utils/chart.js";
import * as echarts from "echarts"; // echart插件
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

export default {
  name: "WaterIntakeDialog",
  data() {
    return {
      name: "",
      wiuCode: "",
      time: 12,
      chartData: {
        name: [],
        watUse: [],
        watUseSum: [],
        wpermitSize: [],
      },
      tbList1: [],
      tbList2: [],
      tbList3: [],
      loading: false,

      activeTab: "取水量列表",
      baseInfo: {},
      listData: {},

      listColumn: [],

      timeOptions: [
        {
          value: 1,
          label: "近1个月",
        },
        {
          value: 3,
          label: "近3个月",
        },
        {
          value: 6,
          label: "近6个月",
        },
        {
          value: 9,
          label: "近9个月",
        },
        {
          value: 12,
          label: "近12个月",
        },
      ],
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  components: {},
  created() {},
  mounted() {
    window.addEventListener("resize", this.handleWindowResize);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  computed: {},
  methods: {
    init(val) {
      this.name = val.name;
      this.baseInfo = val.obj;
      this.wiuCode = val.obj.wiuCode;
      this.$nextTick(() => {
        this.listUseByMonth();
      });
    },
    // 取水量列表
    async listUseByMonth() {
      this.chartData = {
        name: [],
        watUse: [],
        watUseSum: [],
        wpermitSize: [],
      };
      let opt = {
        month: this.time,
        wiuCode: this.wiuCode,
      };
      await waterResourceApi.listUseByMonth(opt).then((res) => {
        this.tbList1 = res.data;
        res.data.forEach((item) => {
          this.chartData.name.push(item.updateTime);
          this.chartData.watUse.push(item.watUse);
          this.chartData.watUseSum.push(item.watUseSum);
          this.chartData.wpermitSize.push(item.wpermitSize);
        });
        this.$nextTick(() => {
          this.chartOpt();
        });
      });
    },
    // 取水许可
    async currentWptOfWiu() {
      let opt = {
        wiuCode: this.wiuCode,
      };
      await waterResourceApi.currentWptOfWiu(opt).then((res) => {
        this.tbList2 = res.data;
      });
    },
    // 档案
    async wiuDocList4Config() {
      let opt = {
        wiuCode: this.wiuCode,
      };
      await waterResourceApi.wiuDocList4Config(opt).then((res) => {
        this.tbList3 = res.data;
      });
    },
    // 基本信息
    setBaseInfoList() {
      this.listColumn = [
        { name: "代码", prop: "wiuCode" },
        { name: "名称", prop: "wiuName" },
        { name: "经度", prop: "wiuLong" },
        { name: "纬度", prop: "wiuLat" },
        { name: "所在位置", prop: "wiuLoc" },
        { name: "法人代表", prop: "legRepr" },
        { name: "联系电话", prop: "legPhone" },
        { name: "取水户类型", prop: "wiuType" },
        { name: "取水用量(万m³)", prop: "watCons" },
      ];
      this.listData = this.baseInfo;
    },
    changeActiveTab(val) {
      this.activeTab = val;
      switch (val) {
        case "取水量列表":
          this.listUseByMonth();
          break;
        case "基本信息":
          this.setBaseInfoList();
          break;
        case "取水许可":
          this.currentWptOfWiu();
          break;
        case "档案":
          this.wiuDocList4Config();
          break;
      }
    },
    timeChange(val) {
      this.listUseByMonth();
    },
    handleClose() {
      this.$emit("closeDialog");
    },
    chartOpt() {
      const _this = this;
      var chartDom = document.getElementById("barChart");
      var myChart = echarts.init(chartDom);

      var value = [];
      let options = {
        dataZoom: generateDataZoom(6, this.chartData.name.length),
        grid: {
          top: "10%",
          bottom: "5%",
          left: "0%",
          right: "0%",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          data: this.chartData.name,
          axisLabel: {
            show: true,
            textStyle: {
              color: "#fff",
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            lineStyle: {
              color: "#fff",
              fontSize: "14",
            },
          },
        },
        yAxis: {
          type: "value",
          name: "",
          nameTextStyle: {
            color: "#ffffff99",
            fontSize: "14",
          },
          axisLine: {
            show: false,
          },
          axisLabel: {
            color: "#fff",
            fontSize: "14",
          },
          splitLine: {
            lineStyle: {
              show: false,
              color: "#fff",
              opacity: 0.2,
            },
          },
        },
        legend: {
          show: true,
          selectedMode: false,
          textStyle: {
            color: "#fff",
          },
          // itemStyle: {
          //   color: "none",
          //   borderColor: "#1CFFF5",
          //   borderWidth: 1,
          //   opacity: 1,
          // },
          itemGap: 20,
        },
        tooltip: {
          trigger: "axis",
          // formatter: function (data) {
          //   return (
          //     data[0].name +
          //     "<br/><span>COD：</span>" +
          //     data[0].value +
          //     "<br/><span>氨氮：</span>" +
          //     data[1].value +
          //     "<br/><span>总磷：</span>" +
          //     data[2].value
          //   );
          // },
          confine: true,
        },
        series: [
          {
            name: "取水量",
            type: "bar",
            data: this.chartData.watUse,
            smooth: true,
            symbol: "none",
          },
          {
            name: "取水量累加",
            type: "line",
            data: this.chartData.watUseSum,
            smooth: true,
            symbol: "none",
          },
          {
            name: "许可取水量",
            type: "line",
            data: this.chartData.wpermitSize,
            smooth: true,
            symbol: "none",
          },
        ],
        color: ["#1CFFF5", "#008FE8", "#E89100"],
      };

      options && myChart.setOption(options);
    },
    // 当窗口缩放时，echart动态调整自身大小
    handleWindowResize() {
      this.$nextTick(() => {});
    },
  },
};
</script>
<style lang="scss" scoped>
.primary-dialog {
  .top-panel {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .content-panel {
    height: 500px;
    display: flex;
    justify-content: space-between;
    color: #fff;
    .left-content {
      width: 60%;
      margin-right: 20px;
      .title {
        margin-bottom: 15px;
        span {
          margin-left: 10px;
          font-size: 18px;
          vertical-align: top;
        }
      }
    }
    .chart-panel {
      width: 100%;
      height: calc(100% - 40px);
    }
    .right-content {
      width: 40%;
    }
  }

  .tab-panel {
    display: flex;
    margin-bottom: 20px;
  }
  .tab-item {
    padding: 8px 10px;
    margin-right: 20px;
    color: #fff;
    border: 1px solid rgba(255, 255, 255, 0.6);
    cursor: pointer;
    &.active {
      color: #56fefe;
      background: rgba(72, 152, 251, 0.1);
      border: 1px solid #56fefe;
      box-shadow: inset 0 0 5px 0 #56fefe;
    }
  }
  .list-panel {
    height: calc(100% - 42px);
    overflow: auto;
    .list-item {
      color: #fff;
      font-size: 14px;
      margin-bottom: 20px;
      .value {
        padding: 10px 10px;
        margin-top: 8px;
        border-radius: 5px;
        background-color: rgba(0, 0, 0, 0.2);
      }
    }
  }
  .abc ::v-deep .el-table__body tr:hover {
    background-color: rgba($color: #56fefe, $alpha: 0.4);
  }
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
      // height: 300px;
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        height: 100%;
        padding: 20px;
      }
    }
  }
}
</style>