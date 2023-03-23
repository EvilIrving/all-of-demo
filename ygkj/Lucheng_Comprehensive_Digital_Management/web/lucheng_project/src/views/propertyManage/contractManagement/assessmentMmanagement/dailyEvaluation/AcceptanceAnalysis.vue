<!--
 * @Date: 2022-04-27 14:45:04
 * @Author: 
 * @Description: 日常考评-考勤分析
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-08-16 13:58:37
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\assessmentMmanagement\dailyEvaluation\AcceptanceAnalysis.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>考勤分析</p>
    </div>
    <el-tabs class="el-tabs-box" v-model="activeName">
      <el-tab-pane label="考勤列表" name="first"></el-tab-pane>
      <el-tab-pane label="曲线分析" name="second"></el-tab-pane>
    </el-tabs>
    <div v-show="activeName == 'first'">
      <div
        class="info_option"
        style="
          width: 100%;
          padding: 25px 16px 16px 16px;
          box-sizing: border-box;
        "
      >
        <span style="font-size: 14px; margin-left: 15px">姓名：</span>
        <el-input
          size="mini"
          placeholder="请输入"
          prefix-icon="el-icon-search"
          @change="getList"
          v-model="name"
          style="width: 180px; margin-right: 15px"
          clearable
        />
        <span style="font-size: 14px; margin-left: 15px">年份：</span>
        <el-date-picker
          size="mini"
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="year"
          value-format="yyyy-MM"
          format="yyyy-MM"
          type="month"
          placeholder="请选择"
          clearable
          @change="getList"
        >
        </el-date-picker>
        <!-- <el-button size="mini" type="primary" class="right" @click="editTable('新增', '')">新增</el-button>
      <el-button size="mini" style="margin-right: 10px;" class="right" @click="exportTable()">导出</el-button> -->
      </div>
      <div class="tb_wrapper">
        <el-table :data="tableData" style="width: 100%">
          <el-table-column
            type="index"
            align="center"
            width="50"
            label="序号"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="姓名"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="post"
            label="岗位"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="phone"
            label="联系电话"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="needNum"
            label="应出勤天数"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="attendanceNum"
            label="实际出勤天数"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="missNum"
            label="漏签"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="leaveNum"
            label="请假"
            align="center"
          ></el-table-column>
          <el-table-column prop="problems" label="考勤排名" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <span
                  class="deal_btn deal_btn_look"
                  @click="editTable('查看', scope.row)"
                  >查看</span
                >
                <!-- <span class="deal_btn deal_btn_del" @click="delTable(scope.row.id)">删除</span> -->
              </div>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="pageTotal"
          :page.sync="pageNum"
          :limit.sync="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          @pagination="getList"
        />
      </div>
    </div>
    <div class="main-echarts-box" v-show="activeName == 'second'">
      <div class="info_option">
        <span style="font-size: 14px; margin-left: 15px">年份：</span>
        <el-date-picker
          size="mini"
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="yearMonth"
          value-format="yyyy-MM"
          format="yyyy-MM"
          type="month"
          placeholder="请选择"
          clearable
          @change="curveAnalysis"
        >
        </el-date-picker>
        <!-- <el-button size="mini" type="primary" class="right" @click="editTable('新增', '')">新增</el-button>
      <el-button size="mini" style="margin-right: 10px;" class="right" @click="exportTable()">导出</el-button> -->
      </div>
      <div class="main-echarts" id="main" ref="main"></div>
    </div>
    <el-dialog :title="title" :visible.sync="showEditDialog" width="65%">
      <acceptance-analysis-detail
        v-if="editDialog"
        :detail="detail"
        @closeEditDialog="closeEditDialog"
      ></acceptance-analysis-detail>
    </el-dialog>
  </div>
</template>

<script>
import { supervisedCheck } from "@/api/propertyManage";
import AcceptanceAnalysisDetail from "./AcceptanceAnalysisDetail.vue";
export default {
  name: "AcceptanceAnalysis",
  props: {},
  components: {
    AcceptanceAnalysisDetail,
  },
  data() {
    return {
      name: "",
      year: "",
      yearMonth: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      detail: {
        name: "",
        year: "",
      },
      activeName: "first",
      myChart: null
    };
  },
  mounted() {
    this.getList();
    this.curveAnalysis();
  },
  watch:{
    activeName(val) {
    
      if(val == 'second'){
        this.$nextTick(() => {
          this.myChart.resize()
        });
      }
    }
  },
  methods: {
    async getList() {
      let res = await supervisedCheck.attendanceList({
        name: this.name,
        yearMonth: this.year,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    async curveAnalysis() {
      let res = await supervisedCheck.curveAnalysis({
        yearMonth: this.yearMonth,
      });
      if (res.code == 0) {
        let normalNum = [];
        let abnormalNum = [];
        let attendanceTime = [];
        if (res.data.length > 0) {
          res.data.forEach((item, index) => {
            normalNum.push(item.normalNum);
            abnormalNum.push(item.abnormalNum);
            attendanceTime.push(item.attendanceTime);
          });
        }
        this.$nextTick(() => {
          this.init(normalNum, abnormalNum, attendanceTime);
        });
      }
    },

    init(normalNum, abnormalNum, attendanceTime) {
      var chartDom = document.getElementById("main");
      this.myChart = this.$echarts.init(chartDom);
      var option;
      option = {
        legend: {
          show: true,
          bottom: 0,
        },
        xAxis: {
          type: "category",
          data: attendanceTime,
        },
        yAxis: {
          type: "value",
          minInterval: 1,
        },
        tooltip: {
          trigger: "axis",
        },
        series: [
          {
            name: "已打卡",
            data: normalNum,
            type: "line",
          },
          {
            name: "未打卡",
            data: abnormalNum,
            type: "line",
          },
        ],
      };
      option && this.myChart.setOption(option);
    },
    editTable(title, detail) {
      this.detail.name = detail.name;
      this.detail.year = this.year;
      this.title = title;
      this.showEditDialog = true;
      this.editDialog = false;
      this.$nextTick(() => {
        this.editDialog = true;
      });
    },
    closeEditDialog() {
      this.showEditDialog = false;
      this.editDialog = false;
      this.getList();
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  @include contentBox();
  height: 100%;
}
.el-tabs-box {
  padding: 0 16px;
}

.main-echarts-box {
  padding: 0 16px 16px;
  width: 100%;
  box-sizing: border-box;
  .main-echarts {
    height: 53vh;
    width: 100%;
  }
}
</style>