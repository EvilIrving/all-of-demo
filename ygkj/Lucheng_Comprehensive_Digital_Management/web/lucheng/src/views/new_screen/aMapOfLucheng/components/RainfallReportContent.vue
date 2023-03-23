<!--
 * @Author: wqn
 * @Date: 2022-09-23 14:08:50
 * @LastEditors: wqn
 * @LastEditTime: 2022-09-23 14:08:50
 * @Description: 雨量报表弹窗内容
-->
<template>
  <div class="rain-report-forms">
    <div class="rain-report-forms-top">
      <div class="rain-report-forms-left">
        <span>时间段</span>
        <el-date-picker prefix-icon="el-icon-date" v-model="rainTime" type="datetimerange" start-placeholder="开始时间" end-placeholder="结束时间" format="yyyy-MM-dd HH:00:00" value-format="yyyy-MM-dd HH:00:00" size="small"
          class="rain-report-forms-date"></el-date-picker>
        <span>雨量范围</span>
        <span>大于</span>
        <el-input size="small" class="rain-report-forms-input first-input" v-model="minRain"></el-input>
        <span>小于</span>
        <el-input size="small" class="rain-report-forms-input" v-model="maxRain"></el-input>
        <span> (mm) </span>
      </div>
      <div class="rain-report-forms-right">
        <div class="rain-report-forms-btn" @click="rainfallReport">生成</div>
        <div class="rain-report-forms-btn" @click="exportRain">导出</div>
      </div>
    </div>
    <div class="rain-report-forms-table">
      <el-table class="report-forms-table" :data="rainTableData" border :height="500" :span-method="objectSpanMethod">
        <el-table-column prop="adName" label="所属区域"></el-table-column>
        <el-table-column prop="bas" label="小流域">
          <template slot-scope="scope">
            <span>{{ scope.row.bas || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stName" label="测站名称">
          <template slot-scope="scope">
            <span>{{ scope.row.stName || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="drp" label="雨量（mm）">
          <template slot-scope="scope">
            <span>{{ scope.row.drp || '-' }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index.js';
import { exportRainfallReport } from '@/api/micro-service/project-manage/export';
export default {
  name: 'rainfallReportContent',
  props: {},
  components: {},
  data() {
    return {
      rainTime: '',
      maxRain: '',
      minRain: '',
      rainTableData: [],
      firstArr: [],
      firstIndex: 0
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  destroyed() {},
  methods: {
    /**
     * @description 生成雨量报表数据
     */
    rainfallReport() {
      let flag = this.rulesVerify();
      if (!flag) {
        return false;
      }
      disasterPreventionApi
        .rainfallReport({
          startTime: this.rainTime.length > 0 ? this.rainTime[0] : '',
          endTime: this.rainTime.length > 0 ? this.rainTime[1] : '',
          minRain: this.minRain,
          maxRain: this.maxRain
        })
        .then((res) => {
          if (res.code == 0) {
            this.rainTableData = res.data;
            this.setRowspan();
          }
        });
    },
    /**
     * @description 导出雨量报表数据
     */
    exportRain() {
      let flag = this.rulesVerify();
      if (!flag) {
        return false;
      }
      let obj = {
        startTime: this.rainTime.length > 0 ? this.rainTime[0] : '',
        endTime: this.rainTime.length > 0 ? this.rainTime[1] : '',
        minRain: this.minRain,
        maxRain: this.maxRain
      };
      exportRainfallReport(obj);
    },
    /**
     * @description 雨量报表查询条件校验
     */
    rulesVerify() {
      if (!this.rainTime) {
        this.$message.error('请选择时间范围！');
        return false;
      }
      if (this.minRain) {
        if (isNaN(Number(this.minRain)) || Number(this.minRain) < 0) {
          this.$message.error('雨量范围请输入大于0的数字或者小数~');
          return false;
        }
      }
      if (this.maxRain) {
        if (isNaN(Number(this.maxRain)) || Number(this.maxRain) < 0) {
          this.$message.error('雨量范围请输入大于0的数字或者小数~');
          return false;
        }
      }
      if (this.maxRain && this.minRain && this.minRain >= this.maxRain) {
        this.$message.error('请输入正确的雨量范围值~');
        return false;
      }
      return true;
    },
    /**
     * @description
     */
    setRowspan() {
      this.firstArr = [];
      this.firstIndex = 0;
      for (let i = 0; i < this.rainTableData.length; i++) {
        if (i == 0) {
          this.firstArr.push(1);
          this.firstIndex = 0;
        } else {
          if (this.rainTableData[i].adCode === this.rainTableData[i - 1].adCode) {
            this.firstArr[this.firstIndex] += 1;
            this.firstArr.push(0);
          } else {
            this.firstArr.push(1);
            this.firstIndex = i;
          }
        }
      }
    },
    /**
     * @description
     */
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0 && this.firstArr.length !== 0) {
        const _row = this.firstArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col
        };
      }
    }
  }
};
</script>
<style lang='scss' scoped>
.rain-report-forms {
  .rain-report-forms-top {
    padding-top: 17px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .rain-report-forms-left {
      color: #fff;
      span {
        padding-right: 12px;
      }
      .rain-report-forms-date {
        margin-right: 49px;
      }
      .rain-report-forms-input {
        width: 52px;
        border: 1px solid #155bd4;
        border-radius: 2px;
        &.first-input {
          margin-right: 15px;
        }
      }
    }
    .rain-report-forms-right {
      .rain-report-forms-btn {
        display: inline-block;
        cursor: pointer;
        width: 67px;
        height: 35px;
        background: linear-gradient(180deg, #36c0e4 0%, #0639ff 100%);
        border-radius: 4px;
        text-align: center;
        line-height: 35px;
        font-weight: 500;
        color: #ffffff;
        font-size: 16px;
        &:first-child {
          margin-right: 32px;
        }
      }
    }
  }
  .rain-report-forms-table {
    padding-top: 16px;
    .report-forms-table {
      width: 100%;
    }
  }
  .rain-report-forms-left {
    .rain-report-forms-date {
      border: 1px solid #155bd4;
      ::v-deep .el-range-separator {
        color: #ffffff;
      }
      ::v-deep .el-range-input {
        background-color: transparent;
        color: #ffffff;
      }
      ::v-deep .el-range__icon {
        color: #c8c9cc;
      }
    }
    .rain-report-forms-input {
      ::v-deep .el-input__inner {
        padding: 0 6px;
      }
    }
  }
  .rain-report-forms-table {
    ::v-deep .el-table--border,
    .el-table--group,
    .el-table--border th.gutter:last-of-type,
    .el-table--border th,
    .el-table__fixed-right-patch {
      border: 1px solid #004965;
    }
    ::v-deep .el-table .el-table__row:hover {
      background-color: transparent;
    }
    ::v-deep .el-table td {
      border: 1px solid #004965;
    }
    ::v-deep .el-table th {
      border-right: none;
      border-left: none;
      &:first-child {
        border-right: 1px solid #004965;
      }
    }
    ::v-deep .el-table td.el-table_1_column_2,
    .el-table td.el-table_1_column_3,
    .el-table td.el-table_1_column_4 {
      border-right: none;
    }
    ::v-deep .el-table--border th,
    .el-table--border td {
      border-left: none;
    }
    ::v-deep .el-table th.is-leaf,
    .el-table td {
      border-top: none;
    }
  }
}
</style>