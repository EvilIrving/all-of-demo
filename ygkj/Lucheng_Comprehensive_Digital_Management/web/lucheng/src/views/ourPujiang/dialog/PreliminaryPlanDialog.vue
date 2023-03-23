<!--
 * @Author: dyn
 * @Date: 2022-05-12 17:57:48
 * @LastEditTime: 2022-05-13 17:35:43
 * @LastEditors: dyn
 * @Description: 戍浦江防洪调度-预案弹框
 * @FilePath: \lucheng\src\views\ourPujiang\dialog\PreliminaryPlanDialog.vue
-->

<template>
  <DataDetailDialog :visible.sync="visible" title="预案监管" :before-close="handleClose"
    class="detail-table preliminary_plan_dialog">
    <div class="main_box">
      <div class="left_box">
        <BoxTitle title="监测统计"></BoxTitle>
        <div class="chart_box" id="lineChart" ref="lineChart">

        </div>
      </div>
      <div class="right_box">
        <BoxTitle title="监控视频"></BoxTitle>
        <el-select class="video_sel" v-model="value" popper-class="video_sel_popper" @visible-change="selChange"
          @change="videoChange">
          <el-option v-for="item in options" :key="item.cameraIndexCode" :label="item.cameraName"
            :value="item.cameraIndexCode" />
        </el-select>
        <div class="video_box">
          <PrimaryVideo ref="primaryVideo" :code="value"></PrimaryVideo>
        </div>
      </div>
    </div>
  </DataDetailDialog>
</template>

<script>
  import { PrimaryVideo } from "@/components"
  import * as echarts from 'echarts';
  import { planSupervision } from "@/api/reservePlan"
  let BoxTitle = {
    props: { title: String },
    render() {
      return (
        <div class="project-item">
          <div class="project-item-title">
            <i class="icon-send-dian"></i>
            <span>
              {this.title}
            </span>
          </div>
          <span class="split-line"></span>
        </div>
      );
      // {<img class="split-line" style="width: 100%" src="../../../assets/images/short_line.png" alt="" />
    }
  }
  export default {
    name: "PreliminaryPlanDialog",
    data() {
      return {
        chart: null,
        options: [],
        value: null,
      };
    },
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
    },
    components: {
      PrimaryVideo,
      BoxTitle,
      DataDetailDialog: () => import("../../water_disaster_custom/dialog/DetailsCommonDialog/components/DataDetailDialog")
    },
    created() { },
    mounted() {
      this._planSupervision();
    },
    beforeDestroy() {
      if (this.$refs.primaryVideo) {
        this.$refs.primaryVideo.hideCamera();
        this.$refs.primaryVideo.destroyCamera();
      }
    },
    computed: {},
    methods: {
      // 下拉框显示/隐藏
      selChange(val) {
        if (val) {
          this.$refs.primaryVideo.cuttingPartWindow(0, 0, 202, 252)
        } else {
          this.$refs.primaryVideo.repairPartWindow(0, 0, 202, 252)
        }
      },
      // 视频切换
      videoChange(val) {
        this.value = val;
        this.$refs.primaryVideo.init();
      },
      // 折线图数据
      async _planSupervision() {
        const res = await planSupervision();
        if (res.code == 0) {
          this.options = res.data.jksp;
          if (this.options.length > 0) {
            this.value = this.options[0].cameraIndexCode;
          } else {
            this.value = null;
          }
          // riverData内河实时水位  tideData江心屿实时潮位  gateData闸上闸下开度数据(gateUp gateDown gateOpen)  forecastRiverData内河预报水位  forecastTideData江心屿预报潮位
          let riverData = [], tideData = [], gateUp = [], gateDown = [], gateOpen = [], forecastRiverData = [], forecastTideData = [];
          res.data.riverData.map((item) => {
            riverData.push([item.tm, item.z]);
          })
          res.data.forecastRiverData.map((item) => {
            forecastRiverData.push([item.tm, item.z]);
          })
          res.data.tideData.map((item) => {
            tideData.push([item.tm, item.tdz]);
          })
          res.data.forecastTideData.map((item) => {
            forecastTideData.push([item.tm, item.tdz]);
          })
          res.data.gateData.map((item) => {
            gateUp.push([item.tm, item.upz]);
            gateDown.push([item.tm, item.dwz]);
            gateOpen.push([item.tm, item.ovs]);
          })
          this.initChart(riverData, tideData, gateUp, gateDown, gateOpen, forecastRiverData, forecastTideData)
        }

      },
      initChart(riverData, tideData, gateUp, gateDown, gateOpen, forecastRiverData, forecastTideData) {
        const el = document.querySelector('#lineChart');
        if (this.chart) this.chart.dispose();
        this.chart = echarts.init(el);
        let option = {
          color: ["#00BAFF", "#47F5A7", "#E47E00", "#0074FF", "#1CFFF5", "#FCEE27", "#FF00BE"],
          title: {
            text: 'm',
            textStyle: {
              color: '#fff',
              fontSize: '16',
              fontWeight: 'normal',
            },
            top: 94,
            left: 10,
          },
          tooltip: {
            trigger: 'axis',
            backgroundColor: "rgba(3, 26, 43, 0.9)",
            shadowStyle: {
              color: "rgba(35 81 239,0.35)"//背景色设置
            },
            textStyle: {
              color: '#fff'
            },
            borderColor: '#00c1ff;'
          },
          legend: {
            left: '0',
            top: '20',
            data: ['内河实时水位', '闸上水位', '江心屿实时潮位', '闸门开度', '内河预报水位', '闸下水位', '江心屿预报潮位'],
            itemGap: 30,
            textStyle: {
              color: '#fff',
              padding: [0, 0, 0, 4]
            },
            padding: [10, 40]
          },
          grid: {
            left: '3%',
            right: '2%',
            bottom: '0',
            top: '22%',
            containLabel: true
          },
          xAxis: {
            type: 'time',
            boundaryGap: false,
            axisLabel: {
              color: '#FFFFFF'
            },
            axisTick: {
              show: false,
            }
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              color: '#FFFFFF'
            },
            splitLine: {
              lineStyle: {
                color: 'rgba(255,255,255,.3)'
              }
            }
          },
          series: [
            {
              name: '内河实时水位',
              type: 'line',
              symbol: 'none',
              data: riverData
            },
            {
              name: '闸上水位',
              type: 'line',
              symbol: 'none',
              data: gateUp
            },
            {
              name: '江心屿实时潮位',
              type: 'line',
              symbol: 'none',
              data: tideData
            },
            {
              name: '闸门开度',
              type: 'line',
              symbol: 'none',
              data: gateOpen
            },
            {
              name: '内河预报水位',
              type: 'line',
              symbol: 'none',
              data: forecastRiverData
            },
            {
              name: '闸下水位',
              type: 'line',
              symbol: 'none',
              data: gateDown
            },
            {
              name: '江心屿预报潮位',
              type: 'line',
              symbol: 'none',
              data: forecastTideData
            },
          ]
        };
        this.chart.setOption(option)
      },
      handleClose() {
        this.$emit("close");
      },
    },
  };
</script>
<style lang="scss" scoped>
.main_box {
  height: 100%;
  width: 100%;
  display: flex;
  .left_box {
    width: calc(100% - 612px - 20px);
    height: 100%;
  }
  .right_box {
    width: 612px;
    height: 100%;
    margin-left: 20px;
  }
  .chart_box {
    width: 100%;
    height: calc(100% - 88px);
  }
  .video_box {
    width: 100%;
    height: calc(100% - 150px);
    margin-top: 14px;
  }
}
</style>
<style lang="scss">
.preliminary_plan_dialog .project-item {
  .project-item-title {
    font-size: 18px;
    display: flex;
    align-items: center;
    margin-bottom: 12px;
    margin-top: 16px;
    span {
      color: #fff;
      font-weight: 600;
      margin-left: 8px;
    }
  }
  .split-line {
    display: inline-block;
    height: 4px;
    width: 100%;
    margin-bottom: 16px;
    background-image: url(~assets/images/short_line.png);
    background-repeat: no-repeat;
    background-size: 100%;
    background-color: unset;
  }
}
.preliminary_plan_dialog.data-detail-dialog .el-dialog .el-dialog__body .data-detail-dialog__content {
  padding: 0 !important;
}
.preliminary_plan_dialog.data-detail-dialog .el-dialog .el-dialog__body {
  .video_sel {
    .el-input__inner,
    .el-input__inner:hover,
    .el-select:hover .el-input__inner {
      border: 1px solid rgba(0, 186, 255, 0.54);
      background: rgba(0, 186, 255, 0.1);
      border-radius: 2px;
    }
  }
}
.video_sel_popper.el-select-dropdown.chart_box {
  z-index: 99999;
}
</style>