<template>
  <div class="flood-dialog">
    <div style="padding: 10px 20px; height: 50px">
      <span
        :class="num == i ? 'switchover-sty' : 'type-sty'"
        v-for="(item, i) in alarmList"
        :key="i"
        @click="switchData(i, item.value)"
        >{{ item.label }}</span
      >
      <div class="derive-sty" @click="batchSendWarnSms()">批量报警</div>
    </div>
    <!-- 列表 -->
    <div style="padding: 10px 20px">
      <el-table ref="multipleTable" :data="dataList" height="450px" stripe width="100%">
        <el-table-column prop="index" label="序号" width="80">
          <template slot-scope="scope">
            <span class="table-order">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <!-- 可能性很大，有可能发生山洪 -->
        <el-table-column :show-overflow-tooltip="true" prop="nvi" label="村庄名称" />
        <el-table-column :show-overflow-tooltip="true" prop="warnTm" label="预警时间" />
        <el-table-column :show-overflow-tooltip="true" prop="overThresholdVal" label="超预警指标" />
        <el-table-column :show-overflow-tooltip="true" prop="rainfall" label="实时降雨量(mm)" />
      </el-table>
    </div>
  </div>
</template>

<script>
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
export default {
  name: 'FloodDialog',
  data() {
    return {
      num: 0,
      alarmList: [
        {
          label: '当前影响村落',
          value: 2
        }, {
          label: '预报影响村落',
          value: 3
        }, {
          label: '24小时以来影响',
          value: 1
        }
      ],
      type: 2,
      dataList: [],
      typeWarnValue: {
        1: 'pastMonitorWarnValue',
        2: 'monitorWarnValue',
        3: 'predictWarnValue'
      }
    }
  },
  mounted() {
    this.getMountainTorrentCWindowTable();
  },
  methods: {
    switchData(val, data) {
      this.num = val;
      this.type = data;
      this.getMountainTorrentCWindowTable();
    },
    async getMountainTorrentCWindowTable() {
      let opt = {
        warnType: this.type,
      }
      let res = await disasterPreventionApi.getMountainTorrentCWindowTable(opt);
      this.dataList = res.data;
      let currentTime = new Date().format("yyyy-MM-dd hh:mm:ss");
      this.dataList.map(item => {
        //当前预警 时间取当前时间
        item.warnTm = this.type == '2' ? currentTime : item[this.typeWarnValue[this.type]].warnTm;

        item.overThresholdVal = item[this.typeWarnValue[this.type]].overThresholdVal;
        item.rainfall = item[this.typeWarnValue[this.type]].rainfall;

      })
    },
    async batchSendWarnSms() {
      const opt = {
        warnType: this.type,
        warnList: this.dataList
      }
      let res = await disasterPreventionApi.batchSendWarnSms(opt);
      if (res.code == 0) {
        this.$message({
          type: 'success',
          message: "报警成功"
        });
      } else {
        this.$message({
          type: 'error',
          message: res.message
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.flood-dialog {
  width: 100%;
  height: 100%;
  .derive-sty {
    padding: 0 15px;
    text-align: center;
    border-radius: 4px;
    line-height: 30px;
    float: right;
    color: #fff;
    cursor: pointer;
    border-color: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%) !important;
    background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
  }

  .type-sty {
    padding: 5px 10px;
    margin-right: 10px;
    width: 140px;
    text-align: center;
    display: inline-block;
    opacity: 0.6;
    color: #fff;
    border: 1px solid #ffffff;
  }

  .switchover-sty {
    padding: 5px 10px;
    margin-right: 10px;
    width: 140px;
    text-align: center;
    display: inline-block;
    color: #fff;
    background-color: rgba($color: #0bbbff, $alpha: 0.4);
    border: 1px solid #56fefe;
  }
}
</style>