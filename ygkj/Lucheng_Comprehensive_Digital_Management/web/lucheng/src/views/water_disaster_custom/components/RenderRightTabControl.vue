<template>
  <div class="tab-box">
    <div class="tab-control tab-control-00" v-permission="'bigScreen:note'" @click="this.sendMessage">短信发送</div>
    <div
      :class="[
        'tab-control',
        'tab-control-01',
        index == 1 ? 'active1' : index == 2 ? 'active2' : index == 3 ? 'active3' : index == 4 ? 'active4' : 'active5'
      ]"
      @click="this.openResponseLevel"
    >
      {{ responseLevelName }}
      <!-- <div class="responseLevel" v-show="isShowResponseLevel">
        <div v-for="item in responseLevelOptions" :key="item.id" @click.stop="selectResponseLevel(item.name,item.id)">
          {{ item.name }}
        </div>
      </div> -->
    </div>
    <div class="responseLevel" v-show="isShowResponseLevel">
      <div v-for="item in responseLevelOptions" :key="item.id" @click.stop="selectResponseLevel(item.name, item.id)">
        {{ item.name }}
      </div>
    </div>
    <div class="tab-control tab-control-02" @click="openBriefing(1)">防汛简报</div>
    <div class="tab-control tab-control-02 water-rain-control" @click="openBriefing(2)">水雨情简报</div>
    <div class="tab-control tab-control-02" @click="visibleRainDialog = true">雨量报表</div>
    <PrimaryDialog
      :visible="visibleDialog"
      :appendToBody="true"
      :main-title="waterRainType == 1 ? '防汛防台简报' : '水雨情简报'"
      top="10vh"
      width="1296px"
      @handleClose="handleClose"
    >
      <div class="briefing_top">
        <div>
          <span>日期：</span>
          <el-date-picker
            v-if="waterRainType == 1"
            v-model="time"
            type="date"
            size="small"
            placeholder="选择日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            @change="changeTime"
          >
          </el-date-picker>
          <el-date-picker
            v-if="waterRainType == 2"
            v-model="time"
            type="month"
            placeholder="选择日期"
            format="yyyy-MM"
            size="small"
            value-format="yyyy-MM"
            :clearable="false"
            @change="getAllMonthByDate"
          >
          </el-date-picker>
        </div>
        <div class="briefing_btn">
          <div style="cursor: pointer" @click="floodPreventionBulletin">生成简报</div>
          <div class="export" style="cursor: pointer" @click="exporBriefing">导出</div>
        </div>
      </div>
      <div class="briefing_content">
        <div class="briefing_content_left">
          <div class="briefing_times">
            <div
              :class="['briefing_time', timeIndex == i ? 'selectActive' : '']"
              v-for="(item, i) in times"
              :key="item.id"
              @click="selectTime(item.file_id, i)"
            >
              {{ item.tm }}
            </div>
          </div>
        </div>
        <div class="briefing_content_right" :class="{ briefing_content_right_two: waterRainType == 2 }">
          <div class="wordDom" v-if="fileUrl && waterRainType == 1" ref="file"></div>
          <iframe
            class="iframe-box"
            v-if="fileUrl && waterRainType == 2"
            :src="fileUrl"
            frameborder="0"
            style=""
          ></iframe>
        </div>
      </div>
    </PrimaryDialog>
    <PrimaryDialog
      :visible="visibleRainDialog"
      :appendToBody="true"
      main-title="雨量报表"
      top="10vh"
      width="1296px"
      @handleClose="handleRainClose"
    >
      <div class="rain-report-forms">
        <div class="rain-report-forms-top">
          <div class="rain-report-forms-left">
            <span>时间段</span>
            <el-date-picker
              prefix-icon="el-icon-date"
              v-model="rainTime"
              type="datetimerange"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              format="yyyy-MM-dd HH:00:00"
              value-format="yyyy-MM-dd HH:00:00"
              size="small"
              class="rain-report-forms-date"
            >
            </el-date-picker>
            <span>雨量范围</span>
            <span>大于</span>
            <el-input size="small" class="rain-report-forms-input first-input" v-model="minRain"></el-input>
            <span>小于</span>
            <el-input size="small" class="rain-report-forms-input" v-model="maxRain"></el-input>
            <span>（mm）</span>
          </div>
          <div class="rain-report-forms-right">
            <div class="rain-report-forms-btn" @click="rainfallReport">生成</div>
            <div class="rain-report-forms-btn" @click="exportRain">导出</div>
          </div>
        </div>
        <div class="rain-report-forms-table">
          <el-table
            class="report-forms-table"
            :data="rainTableData"
            border
            :height="500"
            :span-method="objectSpanMethod"
          >
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
    </PrimaryDialog>
  </div>
</template>

<script>
import axios from 'axios';
const docx = require('docx-preview');
window.JSZip = require('jszip');
import { PrimaryDialog } from '@/components';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index.js';
import { exportRainfallReport } from '@/api/micro-service/project-manage/export';
export default {
  components: { PrimaryDialog },
  data() {
    return {
      index: '',
      responseLevelName: '',
      responseLevelOptions: [],
      time: new Date().format('yyyy-MM-dd'),
      times: [],
      timeIndex: 0,
      fileName: '',
      fileUrl: '',
      isShowResponseLevel: false,
      visibleDialog: false,
      waterRainType: 1,
      visibleRainDialog: false,
      rainTime: '',
      maxRain: '',
      minRain: '',
      rainTableData: [],
      firstArr: [],
      firstIndex: 0
    };
  },
  mounted() {
    this.getResponseLevel();
    // this.rainfallReport();
  },
  methods: {
    sendMessage() {
      this.$parent.$refs.mapsche.openMessage();
    },
    openResponseLevel() {
      this.isShowResponseLevel = !this.isShowResponseLevel;
    },
    selectResponseLevel(name, id) {
      this.responseLevelName = name;
      this.isShowResponseLevel = !this.isShowResponseLevel;
      this.index = id;
      this.updateResponseLevel(id);
    },
    openBriefing(type) {
      if (type == 1) {
        this.time = new Date().format('yyyy-MM-dd');
        this.getAllHoursByDate();
      } else {
        this.time = new Date().format('yyyy-MM');
        this.getAllMonthByDate();
      }
      this.waterRainType = type;
      this.visibleDialog = true;
    },
    handleClose() {
      this.visibleDialog = false;
      this.$nextTick(() => {
        this.fileName = '';
        this.fileUrl = '';
      });
    },
    changeTime() {
      this.getAllHoursByDate();
    },
    selectTime(id, i) {
      this.timeIndex = i;
      this.getBriefing(id);
    },
    exporBriefing() {
      let fileUrl = this.fileUrl;
      if (this.waterRainType == 2) {
        fileUrl = fileUrl.split('src=')[1];
      }
      axios({
        method: 'get',
        responseType: 'blob', // 因为是流文件，所以要指定blob类型
        url: fileUrl // 自己的服务器，提供的一个word下载文件接口
      }).then(({ data }) => {
        const blob = new Blob([data]); // 把得到的结果用流对象转一下
        var a = document.createElement('a'); //创建一个<a></a>标签
        a.href = URL.createObjectURL(blob); // 将流文件写入a标签的href属性值
        a.download = this.fileName; //设置文件名
        a.style.display = 'none'; // 障眼法藏起来a标签
        document.body.appendChild(a); // 将a标签追加到文档对象中
        a.click(); // 模拟点击了a标签，会触发a标签的href的读取，浏览器就会自动下载了
        a.remove(); // 一次性的，用完就删除a标签
      });
    },
    async getResponseLevel() {
      let res = await disasterPreventionApi.responseLevel();
      this.responseLevelOptions = res.data;
      this.responseLevelOptions.find((item) => {
        if (item.whetherOpen == 1) {
          this.responseLevelName = item.name;
          this.index = item.id;
        }
      });
    },
    async updateResponseLevel(id) {
      let res = await disasterPreventionApi.updateResponseLevel({
        id: id
      });
    },
    async floodPreventionBulletin() {
      if (this.waterRainType == 1) {
        await disasterPreventionApi.floodPreventionBulletin();
        await this.getAllHoursByDate();
      } else {
        await disasterPreventionApi.waterAndRainBriefing();
        await this.getAllMonthByDate();
      }
    },
    async getAllMonthByDate() {
      this.fileUrl = '';
      let res = await disasterPreventionApi.getAllMonthByDate({ date: this.time });
      this.times = res.data;
      res.data.length > 0 && this.getBriefing(res.data[0].file_id);
    },
    async getAllHoursByDate() {
      let res = await disasterPreventionApi.getAllHoursByDate({ date: this.time });
      this.times = res.data;
      res.data.length > 0 && this.getBriefing(res.data[0].file_id);
    },
    async getBriefing(id) {
      let res = await disasterPreventionApi.getBriefing({ ids: id });
      this.fileName = res.data[0].fileName;
      if (this.waterRainType == 1) {
        this.fileUrl = res.data[0].fileUrl;
        this.setDocx();
      } else {
        let src = 'https://view.officeapps.live.com/op/view.aspx?src=';
        this.fileUrl = src + res.data[0].fileUrl;
      }
    },
    setDocx() {
      axios({
        method: 'get',
        responseType: 'blob', // 设置响应文件格式
        url: this.fileUrl
      }).then(({ data }) => {
        docx.renderAsync(data, this.$refs.file); // 渲染到页面预览
      });
    },
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
    //雨量报表列表
    async rainfallReport() {
      let flag = this.rulesVerify();
      if (!flag) {
        return false;
      }
      let res = await disasterPreventionApi.rainfallReport({
        startTime: this.rainTime.length > 0 ? this.rainTime[0] : '',
        endTime: this.rainTime.length > 0 ? this.rainTime[1] : '',
        minRain: this.minRain,
        maxRain: this.maxRain
      });
      if (res.code == 0) {
        this.rainTableData = res.data;
        this.setRowspan();
      }
    },
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
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0 && this.firstArr.length !== 0) {
        const _row = this.firstArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col
        };
      }
    },
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
    handleRainClose(){
      this.visibleRainDialog = false;
      this.$nextTick(() => {
        this.rainTime = ''
        this.minRain = ''
        this.maxRain = ''
        this.rainTableData = []
      })
    }
  }
};
</script>

<style lang="scss" scoped>
.tab-box {
  position: absolute;
  right: 20%;
  top: 16%;
  z-index: 100;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  .tab-control {
    width: 128px;
    height: 43px;
    background: url(~assets/images/ScreenAsset/tab-control-icon.png) no-repeat;
    background-size: 100% 100%;
    padding-left: 16px;
    box-sizing: border-box;
    line-height: 43px;
    position: relative;
    font-size: 16px;
    color: #ffffff;
    margin-bottom: 24px;
    cursor: pointer;
    &::after {
      content: '';
      position: absolute;
      width: 60px;
      height: 60px;
      right: -10px;
      top: 50%;
      margin-top: -30px;
    }
    &.tab-control-00 {
      &::after {
        background: url(~assets/images/scheduling/shortMessage.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    &.tab-control-01 {
      &::after {
        background: url(~assets/images/scheduling/no_response.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    &.active1 {
      background: url(~assets/images/ScreenAsset/tab-control-icon-red.png) no-repeat;
      background-size: 100% 100%;
      &::after {
        background: url(~assets/images/scheduling/1_response.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    &.active2 {
      background: url(~assets/images/ScreenAsset/tab-control-icon-orange.png) no-repeat;
      background-size: 100% 100%;
      &::after {
        background: url(~assets/images/scheduling/2_response.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    &.active3 {
      background: url(~assets/images/ScreenAsset/tab-control-icon-yellow.png) no-repeat;
      background-size: 100% 100%;
      &::after {
        background: url(~assets/images/scheduling/3_response.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    &.active4 {
      background: url(~assets/images/ScreenAsset/tab-control-icon.png) no-repeat;
      background-size: 100% 100%;
      &::after {
        background: url(~assets/images/scheduling/4_response.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    &.active5 {
      background: url(~assets/images/ScreenAsset/tab-control-icon.png) no-repeat;
      background-size: 100% 100%;
      &::after {
        background: url(~assets/images/scheduling/no_response.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    &.tab-control-02 {
      &::after {
        background: url(~assets/images/scheduling/briefing.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    &.water-rain-control {
      width: 145px;
    }
  }
  .responseLevel {
    background: #0f1b2590;
    // margin-top: 5px;
    width: 128px;
    position: relative;
    bottom: 15px;
    text-align: center;
    font-size: 16px;
    color: #ffffff;
    margin-bottom: 5px;
    > div {
      height: 40px;
      line-height: 40px;
    }
  }
}
.briefing_top {
  margin-top: 16px;
  display: flex;
  justify-content: space-between;
  color: #ffffff;
  ::v-deep .el-input__inner {
    border: 1px solid #e5e5e5;
  }
  .briefing_btn {
    display: flex;
    > div {
      height: 35px;
      width: 100px;
      line-height: 35px;
      background: linear-gradient(180deg, #36c0e4 0%, #0639ff 100%);
      border-radius: 4px;
      text-align: center;
    }
    .export {
      width: 67px;
      margin-left: 38px;
    }
  }
}
.briefing_content {
  margin-top: 20px;
  height: 657px;
  background: #ffffff;
  border-radius: 4px;
  display: flex;
  .briefing_content_left {
    width: 290px;
    height: 100%;
    border-right: 1px solid #e6e6e6;
  }
  .briefing_content_right {
    width: calc(100% - 290px);
    position: relative;
    &.briefing_content_right_two {
      overflow: hidden;
    }
    .iframe-box {
      width: 100%;
      height: 115%;
      position: absolute;
      left: 0;
      top: -95px;
    }
  }
}
.briefing_times {
  padding: 10px;
  height: 100%;
  .briefing_time {
    width: 270px;
    height: 32px;
    border: 1px solid #e6e6e6;
    text-align: center;
    line-height: 30px;
    margin-bottom: 8px;
    &.selectActive {
      box-shadow: 0px 0px 4px 0px rgba(80, 166, 255, 0.65);
      border: 1px solid #026fe0;
    }
  }
}
.wordDom {
  height: 100%;
  //   overflow: auto;
  ::v-deep .docx-wrapper {
    height: 100%;
    overflow: auto;
    background: #fff;
  }
  ::v-deep .docx {
    //   overflow: auto;
    min-height: 0;
  }
  //   ::v-deep section.docx {
  //     overflow: auto;
  //   }
}
::v-deep section.docx {
  overflow: auto;
}
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
}
</style>

<style lang="scss">
.rain-report-forms {
  .rain-report-forms-left {
    .rain-report-forms-date {
      border: 1px solid #155bd4;
      .el-range-separator {
        color: #ffffff;
      }
      .el-range-input {
        background-color: transparent;
        color: #ffffff;
      }
      .el-range__icon {
        color: #c8c9cc;
      }
    }
    .rain-report-forms-input {
      .el-input__inner {
        padding: 0 6px;
      }
    }
  }
  .rain-report-forms-table {
    .el-table--border,
    .el-table--group,
    .el-table--border th.gutter:last-of-type,
    .el-table--border th,
    .el-table__fixed-right-patch {
      border: 1px solid #004965;
    }
    .el-table .el-table__row:hover {
      background-color: transparent;
    }
    .el-table td {
      border: 1px solid #004965;
    }
    .el-table th {
      border-right: none;
      border-left: none;
      &:first-child {
        border-right: 1px solid #004965;
      }
    }
    .el-table td.el-table_1_column_2,
    .el-table td.el-table_1_column_3,
    .el-table td.el-table_1_column_4 {
      border-right: none;
    }
    .el-table--border th,
    .el-table--border td {
      border-left: none;
    }
    .el-table th.is-leaf,
    .el-table td {
      border-top: none;
    }
  }
}
</style>