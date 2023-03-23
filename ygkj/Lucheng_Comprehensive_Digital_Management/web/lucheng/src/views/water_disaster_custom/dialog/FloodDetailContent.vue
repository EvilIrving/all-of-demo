<template>
  <div class="flood-detail-content map-dialog">
    <div class="btn-box clearfix" v-if="this.pointData.type == '山洪预警'">
      <div v-if="monitorWarnValue" class="derive-sty" @click="batchSendWarnSms()">一键报警</div>
    </div>
    <div class="base-table">
      <table width="100%">
        <tr>
          <td class="prop-td">总户数</td>
          <td>{{ detailInfo.tho | EmptyFormat }}户</td>
          <td class="prop-td">总人口</td>
          <td>{{ detailInfo.tpo | EmptyFormat }}人</td>
        </tr>
        <tr>
          <td class="prop-td">防汛责任人</td>
          <td>{{ chargeManInfo }}</td>
          <td class="prop-td">代表站</td>
          <td>{{ detailInfo.stationName | EmptyFormat }}</td>
        </tr>
      </table>
    </div>
    <div class="warn-cards">
      <template v-if="this.pointData.type == '山洪'">
        <div class="warn-card-item" v-for="(item, index) in detailInfo.list" :key="index">
          <table width="100%">
            <tr>
              <td class="prop-td" colspan="2">{{ item.period }}小时报警</td>
            </tr>
            <tr>
              <td class="prop-td">实时雨量</td>
              <td>{{ item.realTimeRainfall | EmptyFormat }}mm</td>
            </tr>
            <tr>
              <td class="prop-td">准备转移</td>
              <td style="color: #e89100">{{ item.readyMove | EmptyFormat }}mm</td>
            </tr>
            <tr>
              <td class="prop-td">立即转移</td>
              <td style="color: #ff551f">{{ item.immediateTransfer | EmptyFormat }}mm</td>
            </tr>
          </table>
        </div>
      </template>
      <template v-if="this.pointData.type == '山洪预警'">
        <!-- 过去 -->
        <div class="warn-card-item" v-if="this.pastMonitorWarnValue">
          <table width="100%">
            <tr>
              <td class="prop-td" colspan="2">过去24小时预警</td>
            </tr>
            <tr>
              <td class="prop-td">实时雨量</td>
              <td>{{ this.pastMonitorWarnValue.rainfall | EmptyFormat }}mm</td>
            </tr>
            <tr>
              <td class="prop-td">
                {{ this.pastMonitorWarnValue.warnGradeId == 5 ? '立即转移' : '准备转移' }}
              </td>
              <td style="color: #e89100">{{ this.pastMonitorWarnValue.warnTm | EmptyFormat }}</td>
            </tr>
          </table>
        </div>
        <!-- 现在 -->
        <div class="warn-card-item" v-if="this.monitorWarnValue">
          <table width="100%">
            <tr>
              <td class="prop-td" colspan="2">正在预警</td>
            </tr>
            <tr>
              <td class="prop-td">实时雨量</td>
              <td>{{ this.monitorWarnValue.rainfall | EmptyFormat }}mm</td>
            </tr>
            <tr>
              <td class="prop-td">{{ this.monitorWarnValue.warnGradeId == 5 ? '立即转移' : '准备转移' }}</td>
              <td style="color: #e89100">{{ this.monitorWarnValue.warnTm | EmptyFormat }}</td>
            </tr>
          </table>
        </div>
        <!-- 未来 -->
        <div class="warn-card-item" v-if="this.predictWarnValue">
          <table width="100%">
            <tr>
              <td class="prop-td" colspan="2">预测预警</td>
            </tr>
            <tr>
              <td class="prop-td">实时雨量</td>
              <td>{{ this.predictWarnValue.rainfall | EmptyFormat }}mm</td>
            </tr>
            <tr>
              <td class="prop-td">{{ this.predictWarnValue.warnGradeId == 5 ? '立即转移' : '准备转移' }}</td>
              <td style="color: #e89100">{{ this.predictWarnValue.warnTm | EmptyFormat }}</td>
            </tr>
          </table>
        </div>
      </template>
    </div>
    <FloodWarnMessage :visible="visible" :pointData="this.pointData" @closeDialog="visible = false"></FloodWarnMessage>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { disasterPreventionApi } from "@/api/RightSildePanel/index";
import FloodWarnMessage from "./FloodWarnMessage.vue";
export default {
  name: "FloodDetailContent",
  props: {
    pointData: {
      type: Object
    }
  },
  data() {
    return {
      detailInfo: {},
      visible: false,

    };
  },
  computed: {
    ...mapGetters([
      "previewModule",
      "formatterCurrentTime",
      "AffairId"
    ]),
    chargeManInfo() {
      let info = "";
      if (this.detailInfo.chargerName) {
        info += this.detailInfo.chargerName + ' ';
      }
      if (this.detailInfo.chargerPhone) {
        info += this.detailInfo.chargerPhone;
      }
      return info;
    },
    pastMonitorWarnValue() {
      return this.pointData.data.pastMonitorWarnValue;
    },
    monitorWarnValue() {
      return this.pointData.data.monitorWarnValue;
    },
    predictWarnValue() {
      return this.pointData.data.predictWarnValue;
    }
  },
  created() {
    if (this.pointData.type == '山洪预警') {
      this.detailInfo = this.pointData.data;
    } else {
      this.flashFloodDetails();
    }
  },
  components: {
    FloodWarnMessage
  },
  watch: {},
  methods: {
    async flashFloodDetails() {
      let opt = {
        nvi: this.pointData.nvi,
        avi: this.pointData.avi
      }
      if (this.previewModule) {
        opt.endTime = new Date(this.formatterCurrentTime()).format("yyyy-MM-dd hh:mm:ss");
        opt.drillAffairId = this.AffairId;
      }
      let res = await disasterPreventionApi.flashFloodDetails(opt);
      if (res.code == 0) {
        this.detailInfo = res.data;
      }
    },
    batchSendWarnSms() {
      this.visible = true;
    }
  },
};
</script>

<style lang="scss" scoped>
.flood-detail-content {
  height: 100%;
  width: 100%;
  height: 600px;
  overflow-y: auto;
  .btn-box {
    position: relative;
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
    margin-bottom: 12px;
  }

  table {
    tr {
      td {
        font-size: 16px;
        padding: 12px;
        color: #fff;
        line-height: 20px;
        border: 1px solid rgba(0, 193, 255, 0.4);
        &.prop-td {
          text-align: center;
          width: 160px;
          color: #56fefe;
          background-image: linear-gradient(90deg, rgba(44, 158, 191, 0.3) 3%, rgba(41, 147, 180, 0.1) 100%);
          vertical-align: middle;
        }
        a {
          display: inline-block;
          vertical-align: middle;
          font-size: 16px;
          color: #56fefe;
          margin-right: 10px;
        }
      }
    }
  }
  .warn-cards {
    @include flexbox;
    @include flexflow(row, wrap);
    @include flexAC;
    margin-top: 16px;
    .warn-card-item {
      width: 31%;
      margin-bottom: 16px;
      margin-right: 16px;
    }
  }
}
</style>
