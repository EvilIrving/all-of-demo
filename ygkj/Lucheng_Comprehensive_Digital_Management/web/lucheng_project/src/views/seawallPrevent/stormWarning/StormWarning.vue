<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-06 14:11:04
 * @Description: 风暴潮预报
 * @FilePath: \lucheng_project\src\views\seawallPrevent\stormWarning\StormWarning.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>风暴潮预报</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <span style="font-size: 14px; margin-left: 15px">时间：</span>
      <el-date-picker
        v-model="searchTime"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="yyyy-MM-dd HH:mm:ss"
        @change="handerChangeTime"
        clearable
      >
      </el-date-picker>
      <span style="font-size: 14px; margin-left: 15px">上报方式：</span>
      <el-select
        v-model="releaseType"
        placeholder="请选择"
        @change="getList"
        clearable
        style="width: 180px; margin: 0 20px 0 15px"
      >
        <el-option
          v-for="item in releaseTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <el-button type="primary" class="right" v-permission="'stormForecast:add'" @click="operateAdd"
        >新增</el-button
      >
    </div>
    <div class="tb_wrapper">
      <el-table :data="projectList" border style="width: 100%">
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column prop="releaseType" label="上报类型" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.releaseType == '1'">手动上报</span>
            <span v-if="scope.row.releaseType == '2'">自动上报</span>
          </template></el-table-column
        >
        <el-table-column
          prop="releaseTime"
          label="上报时间"
          align="center"
        ></el-table-column>
        <el-table-column align="center" label="附件">
          <template slot-scope="scope">
            <img
              class="deal_img"
              v-if="scope.row.attachUrl"
              @click="lookFile(scope.row.attachUrl)"
              src="../../../assets/images/address.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                v-permission="'stormForecast:look'" 
                @click="lookTable(scope.row.id, scope.row.releaseTime)"
                >查看</span
              >
              <!-- <span class="deal_btn deal_btn_del" @click="del(scope.row.id)"
                >删除</span
              > -->
            </div>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="pageTotal"
        :page.sync="pageNum"
        :limit.sync="pageSize"
        :page-sizes="[10, 20, 30, 40]"
        @pagination="getList"
      />
    </div>
    <el-dialog
      title="详情"
      :visible.sync="showLookDialog"
      width="960px"
      @close="closeLookDialog"
    >
      <div class="details_top" style="width: 100%; box-sizing: border-box">
        <span style="font-size: 14px">站点选择：</span>
        <el-select
          v-model="station"
          placeholder="请选择"
          @change="getStormSurgeList"
          clearable
          style="width: 180px; margin: 0 20px 0 15px"
        >
          <el-option
            v-for="item in stationOptions"
            :key="item.value"
            :label="item.stName"
            :value="item.stCode"
          >
          </el-option>
        </el-select>
        <span
          style="
            font-size: 14px;
            color: #1492ff;
            position: relative;
            left: 418px;
          "
          >预报时间：{{ releaseTime }}</span
        >
      </div>
      <div class="details">
        <div class="top_item_box">
          <div class="item_box_title">
            <p>潮位曲线</p>
          </div>
          <div class="myChart" id="myChart"></div>
        </div>
        <div class="top_item_box">
          <div class="item_box_title">
            <p>详表</p>
          </div>
          <div class="tb_wrapper">
            <el-table :data="stationList" border style="width: 100%">
              <el-table-column
                type="index"
                align="center"
                width="50"
                label="序号"
              ></el-table-column>
              <el-table-column
                prop="tm"
                label="时间"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="tdz"
                label="潮位(m)"
                align="center"
              ></el-table-column>
              <!-- <el-table-column align="center" label="操作" width="150">
                <template slot-scope="">
                  <div class="deal_box">
                    <span
                      class="deal_btn deal_btn_look"
                      @click="lookTable('查看', scope.row.id)"
                      >解除</span
                    >
                  </div>
                </template>
              </el-table-column> -->
            </el-table>
          </div>
        </div>
      </div>
    </el-dialog>
    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <look-file
        v-if="fileDialog"
        :url="url"
        @closeLookDialog="closeLookDialog"
      ></look-file>
    </el-dialog>
    <base-form
      v-if="dialogVisible"
      :visible="dialogVisible"
      :isEdit="isEdit"
      :isNew="isNew"
      :info="dialogInfo"
      @close="close"
      @getList="getList"
    ></base-form>
  </div>
</template>

<script>
import { stormSurgeWarningApi } from "@/api/seawallPrevent";
import BaseForm from "./BaseForm.vue";

export default {
  name: "StormWarning",
  props: {},
  components: { BaseForm },
  data() {
    return {
      releaseType: "",
      searchTime: "",
      releaseStartTm: "",
      releaseEndTm: "",
      fcstId: "",
      releaseTime: "",
      xTimes: [],
      yDatas: [],
      releaseTypeOptions: [
        {
          label: "手动上报",
          value: "1",
        },
        {
          label: "自动上报",
          value: "2",
        },
      ],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      projectList: [
        {
          seawallName: "11",
        },
      ],
      showLookDialog: false,
      fileDialog: false,
      station: "",
      stationOptions: [
        {
          label: "x站点",
          value: "1",
        },
        {
          label: "xx站点",
          value: "2",
        },
      ],
      stationList: [
        {
          seawallName: "11",
        },
      ],
      dialogVisible: false,
      isEdit: false,
      isNew: false,
      dialogInfo: {},
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    //关闭弹窗
    close() {
      this.dialogVisible = false;
    },
    //新增
    operateAdd() {
      this.isEdit = false;
      this.isNew = true;
      this.dialogVisible = true;
      this.dialogInfo = {};
    },
    lookFile(url) {
      this.url = url;
      this.fileDialog = true;
    },
    lookTable(id, time) {
      this.fcstId = id;
      this.releaseTime = time;
      this.showLookDialog = true;
      this.getStationList();
      // this.$nextTick(() => {
      //   this.initChart()
      // })
    },
    closeEditDialog() {
      this.showEditDialog = false;
    },
    async getList() {
      let res = await stormSurgeWarningApi.list({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        releaseType: this.releaseType,
        releaseStartTm: this.releaseStartTm,
        releaseEndTm: this.releaseEndTm,
      });
      if (res.code == 0) {
        this.projectList = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    async getStationList() {
      let res = await stormSurgeWarningApi.stationList(this.fcstId);
      if (res.code == 0) {
        this.stationOptions = res.data;
        if (res.data.length > 0) {
          this.station = this.stationOptions[0].stCode;
          this.getStormSurgeList();
        }
      }
    },
    async getStormSurgeList() {
      let res = await stormSurgeWarningApi.stormSurgeList({
        fcstId: this.fcstId,
        stCode: this.station,
      });
      if (res.code == 0) {
        this.stationList = res.data;
        let xData = [];
        let yData = [];
        this.stationList.map((item) => {
          xData.push(item.tm);
          yData.push(item.tdz);
        });
        this.xTimes = xData;
        this.yDatas = yData;
        this.initChart();
      }
    },
    handerChangeTime(times) {
      if (times) {
        this.releaseStartTm = times[0];
        this.releaseEndTm = times[1];
      } else {
        this.releaseStartTm = "";
        this.releaseEndTm = "";
      }
      this.getList();
    },
    closeLookDialog(num) {
      if (num) {
        this.getList();
      }
      this.station = "";
      this.showLookDialog = false;
    },
    initChart() {
      let chartDom = document.getElementById("myChart");
      let myChart = this.$echarts.init(chartDom);
      let option;

      option = {
        tooltip: {
          trigger: "axis",
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          axisLabel: {
            textStyle: {
              color: "#848e98",
            },
          },
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          data: this.xTimes,
        },
        yAxis: {
          type: "value",
          name: "m",
          nameTextStyle: {
            color: "#848e98",
            padding: [0, 20, 0, 0],
          },
          axisLabel: {
            textStyle: {
              color: "#848e98",
            },
          },
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
        },
        series: [
          {
            data: this.yDatas,
            itemStyle: {
              normal: {
                color: "#66b1ff", //折线点的颜色
                lineStyle: {
                  color: "#66b1ff", //折线的颜色
                },
              },
            },
            markPoint: {
              symbol: "round",
              label: {
                color: "#fff",
                formatter: "{b}{c}",
              },
              itemStyle: {
                color: "#66b1ff90",
              },
              data: [
                {
                  type: "max", //最大值
                  name: "最大潮位：",
                  symbolSize: [120, 30], // 容器大小
                  symbolOffset: [0, -20], //位置偏移
                },
              ],
            },
            type: "line",
            smooth: true,
          },
        ],
      };

      option && myChart.setOption(option);
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  .details {
    display: flex;
    border-bottom: 1px solid #eee;
    .top_item_box {
      width: 50% !important;
      ::v-deep .el-table__body-wrapper {
        height: 250px;
        overflow-y: auto;
      }
      .item_box_title {
        border-bottom: 0;
      }
      .myChart {
        width: 460px;
        min-height: 302px;
      }
    }
  }
}
</style>
