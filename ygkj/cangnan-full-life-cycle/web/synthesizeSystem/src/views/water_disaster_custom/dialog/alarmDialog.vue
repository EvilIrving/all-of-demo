<template>
  <div
    :class="['primary-dialog', leftSide == true ? 'project-dialog-wrap' : '']"
  >
    <el-dialog
      top="10vh"
      width="1500px"
      :visible.sync="visible"
      v-loading="exportLoading"
			element-loading-text="拼命导出中。。。"
			element-loading-spinner="el-icon-loading"
			element-loading-background="rgba(0, 0, 0, 0.8)"
      destroy-on-close
      :modal-append-to-body="true"
      :before-close="close"
      :close-on-click-modal="false"
    >
      <div
        class="primary-dialog__title"
        slot="title"
        :style="minimizeStatus ? 'justify-content: space-between;' : ''"
      >
        <p class="primary-dialog__title--label" style="color: #fff">
          {{ mainTitle }}
        </p>
      </div>
        <div class="primary-dialog__content" style="height:34rem">
          <div style="padding: 10px 20px;height:50px;">
            <span :class="num == i ? 'switchover-sty' : 'type-sty'" v-for="(item, i) in alarmList" :key="i" @click="switchData(i, item)">{{item.label}}({{statsData[item.value]}})</span>
            <div class="derive-sty" style="width: 65px" @click="exportExcel('风险隐患')"> 导 出 </div>
          </div>
          <!-- 列表 -->
          <div style="padding: 10px 20px;">
            <el-table ref="multipleTable" :data="realTimeData" height="450px" stripe width="100%">
            <el-table-column prop="index" label="序号" width="80">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <!-- 1小时降雨  ，三小时降雨 -->
            <el-table-column v-if="num == '0' || num == '1'" :show-overflow-tooltip="true" prop="stationName" label="站点名称" />
            <el-table-column v-if="num == '0' || num == '1'" :show-overflow-tooltip="true" prop="tm" label="报警时间" />
            <el-table-column v-if="num == '0'" :show-overflow-tooltip="true" prop="rainfallOneData" label="1小时累计降雨量（mm）" />
            <el-table-column v-if="num == '1'" :show-overflow-tooltip="true" prop="rainfallThreeData" label="3小时累计降雨量（mm）" />
            <!-- 水库 -->
            <el-table-column v-if="num == '2'" :show-overflow-tooltip="true" prop="resName" label="站点名称" />
            <el-table-column v-if="num == '2'" :show-overflow-tooltip="true" prop="dataTime" label="水位测量时间" />
            <el-table-column v-if="num == '2'" :show-overflow-tooltip="true" prop="waterLevel" label="实时水位（m）" />
            <el-table-column v-if="num == '2'" :show-overflow-tooltip="true" prop="currentLimitLevel" label="汛限水位（m）" />
            <el-table-column v-if="num == '2'" :show-overflow-tooltip="true" prop="overLimitVal" label="超汛限水位（m）" />
            <!-- 河网 -->
            <el-table-column v-if="num == '3'" :show-overflow-tooltip="true" prop="stName" label="站点名称" />
            <el-table-column v-if="num == '3'" :show-overflow-tooltip="true" prop="tm" label="报警时间" />
            <el-table-column v-if="num == '3'" :show-overflow-tooltip="true" prop="waterLevel" label="实时水位（m）" />
            <el-table-column v-if="num == '3'" :show-overflow-tooltip="true" prop="warningLevel" label="警戒水位（m）" />
            <el-table-column v-if="num == '3'" :show-overflow-tooltip="true" prop="overWarnLevel" label="超警戒值（m）" />
            <!-- 潮位 -->
            <el-table-column v-if="num == '4'" :show-overflow-tooltip="true" prop="stName" label="站点名称" />
            <el-table-column v-if="num == '4'" :show-overflow-tooltip="true" prop="tm" label="报警时间" />
            <el-table-column v-if="num == '4'" :show-overflow-tooltip="true" prop="tdz" label="实时潮位（m）" />
            <el-table-column v-if="num == '4'" :show-overflow-tooltip="true" prop="warningLevel" label="警戒水位（m）" />
            <el-table-column v-if="num == '4'" :show-overflow-tooltip="true" prop="overWarnLevel" label="超警戒值（m）" />
            <!-- 可能性很大，有可能发生山洪 -->
            <el-table-column v-if="num == '5' || num == '6'" :show-overflow-tooltip="true" prop="adnm" label="村庄名称" />
            <el-table-column v-if="num == '5' || num == '6'" :show-overflow-tooltip="true" prop="dataTime" label="预警时间" />
            <el-table-column v-if="num == '5' || num == '6'" :show-overflow-tooltip="true" prop="overWarnIndex" label="超预警指标" />
            <el-table-column v-if="num == '5' || num == '6'" :show-overflow-tooltip="true" prop="forecastRainfall" label="预报降雨量" />
            <el-table-column v-if="num == '5' || num == '6'" :show-overflow-tooltip="true" prop="forecastTime" label="预报时间" />
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
import { programmeApi } from "@/api/programme_api/index.js";
export default {
  name: "PrimaryDialog",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    width: {
      type: String,
      default: "600px",
    },
    mainTitle: {
      type: String,
      default: "报警详情",
    },
    leftSide: {
      type: Boolean,
      default: false,
    },
    minimizeStatus: {
      type: Boolean,
      default: false,
    },
    fileList:{
      type: String,
      default: "",
    },
  },
  data() {
    return {
      // 导出Loading
			exportLoading: false,
      num: '0',
      statsData: {},
      realTimeData: [],
      alarmList: [
        {
          label: '1小时降雨',
          value: 'rainfall1Warn'
        },
        {
          label: '3小时降雨',
          value: 'rainfall3Warn'
        },
        {
          label: '水库',
          value: 'rsvrWarn'
        },
        {
          label: '河网',
          value: 'riverNetWarn'
        },
        {
          label: '潮位',
          value: 'tideWarn'
        },
        {
          label: '有可能发生山洪',
          value: 'mountainTorrentGrad4'
        },
        {
          label: '可能性很大',
          value: 'mountainTorrentGrad5'
        }
      ],
      type: '1小时降雨'
    }
  },
  mounted() {
    this.$nextTick(()=> {
      this.warnMapPointStats();
    })
  },
  methods: {
    // 导出
    exportExcel() {
      this.exportLoading = true;
      let userInfo = localData("get", "userInfo");
      let { accessToken } = userInfo ? userInfo : { accessToken: "" };
      axios({
        url: `${config.baseUrl}/big-screen/waterDisasterPrevention/exportWarnMapPoint`,
        method: "post",
        data: {},
        responseType: "blob",
        headers: {
          authorization: accessToken,
        },
      }).then((res) => {
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
      }).catch(error => {
				this.exportLoading = false;
			});
    },
    //类型切换
    switchData(val, data) {
      this.num = val;
      this.type = data.label;
      this.warnMapPointStats();
    },
    // 列表数据接口
    async warnMapPointStats() {
      await programmeApi.warnMapPointStats().then((res) => {
        if (res.code == 0) {
          this.statsData = res.data.stats;
          switch (this.type) {
            case '1小时降雨':
              this.realTimeData = res.data.list.rainfall1Warn;
              break;
            case '3小时降雨':
              this.realTimeData = res.data.list.rainfall3Warn;
              break;
            case '水库':
              this.realTimeData = res.data.list.rsvrWarn;
              break;
            case '河网':
              this.realTimeData = res.data.list.riverNetWarn;
              break;
            case '潮位':
              this.realTimeData = res.data.list.tideWarn;
              break;
            case '有可能发生山洪':
              this.realTimeData = res.data.list.mountainTorrentGrad4;
              break;
            case '可能性很大':
              this.realTimeData = res.data.list.mountainTorrentGrad5;
              break;
            default:
              this.realTimeData = res.data.list.rainfall1Warn;
              break;
          }
        }
      })
    },
    // 关闭
    close() {
      this.$emit("close");
    }
  }
}
</script>

<style lang="scss" scoped>
.primary-dialog {
  ::v-deep .el-dialog {
    background: transparent;
    .el-dialog__header {
      background-size: 18px 2px, 2px 18px;
      padding: 2px;
      height: 54px;
      .primary-dialog__title {
        height: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 40px;
        .primary-dialog__title--label {
          background: url(~assets/images/WaterDisastersImage/title-bg.png) no-repeat;
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
      .primary-dialog__content {
        height: 43.375rem;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
        border-top: 0;
        #floodEchart {
          width: 100%;
          height: 500px;
        }
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
        .first {
          font-family: PingFangSC-Semibold;
          font-size: 18px;
          color: #ffffff;
          line-height: 16px;
          font-weight: 600;
          padding-left: 25px;
        }
        .second,
        .third {
          font-family: PingFangSC-Regular;
          font-size: 16px;
          color: #ffffff;
          line-height: 16px;
          font-weight: 400;
          padding-left: 25px;
          margin-top: 25px;
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
.dialog-footer {
  position: absolute;
  right: 20px;
  bottom: 20px;
  .el-button--primary.is-plain {
    color: #fff;
    border-color: #0bbbff;
    background-color: rgba($color: #0bbbff, $alpha: 0.1);
  }
}
.switchover-sty {
  padding: 5px 10px;
  margin-right: 10px;
  width:140px;
  text-align: center;
  display: inline-block;
  color: #fff;
  background-color: rgba($color: #0bbbff, $alpha: 0.4);
  border: 1px solid #56FEFE;
}
.el-range-editor.el-input__inner {
  height: 40px;
}
::v-deep.el-range-editor .el-range-input {
  color: #fff;
  background: none;
}
::v-deep.el-date-editor .el-range-separator {
  opacity: 0.6;
  color: #fff;
}
.type-sty {
  padding: 5px 10px;
  margin-right: 10px;
  width:140px;
  text-align: center;
  display: inline-block;
  opacity: 0.6;
  color: #fff;
  border: 1px solid #FFFFFF;
}
.peripheral-analysis {
  display: inline-block;
  margin-left: 10px;
  cursor: pointer;
  background: url(~assets/images/peripheral_analysis.png) no-repeat;
  background-size: 100% 100%;
  width: 15px;
  height: 15px;
}
.minimize {
  color: #00c1ff;
  font-weight: bold;
  cursor: pointer;
}
</style>