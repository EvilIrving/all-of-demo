<template>
  <div
    :class="['primary-dialog', leftSide == true ? 'project-dialog-wrap' : '']"
  >
    <el-dialog
      :visible.sync="visible"
      :width="width"
      :before-close="handleClose"
      destroy-on-close
      :modal-append-to-body="true"
      :close-on-click-modal="false"
    >
      <div class="primary-dialog__title" slot="title" :style="minimizeStatus ? 'justify-content: space-between;' : ''" >
        <p class="primary-dialog__title--label" style="color: #fff;">{{ mainTitle }} 
          <a target="blank" href="http://10.36.160.145:8888?token=eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.gcGB1bClPatg2GZv2RR66iwdG6R5UsX0kmgkMHEf3wWVrKQIFmIA1Yg16xJgmeGfNbD3A9CTHXiRkVr8gXLtoRtlspBrhwL8OfhK_yAHnsCdaBTJDctWP_Ia7y9UnzmRMYMuTFzredeMIzQikxFSzAMWAyGN0WlHE2bMze_gNOQ.b0ffbanUZTskgR_2.eYyCDUOGbfsBThAJwASO6_wT2OC7vMYIWGaSGCuaNVJlAR0EXX0NDSZvGR8rfH-rUZyJyQgfqZTb80BRjRKX5N67su-1SVyVtu2-lvcfyyyVDtBiqtMKN7z9jDlL2Jvs-pQFBv5QbQpevUZaoSecDReuHovAleQ.ZMO_T18465VDneqtm4K1vw" :underline="false">
          <el-tooltip class="item" effect="dark" content="跳转洪水预报" placement="top-start">
            <i class="icon-jump-to" style="margin: 7px 0 0 10px;"></i>
          </el-tooltip>
        </a>
        </p>
        
      </div>
      <div class="primary-dialog__content">
        <el-row>
            <el-col :span="12" style="margin: 10px 0;" v-for="(item,index) in lookDetailsData" :key="index">
                <!-- 查询条件 -->
                <div class="first-line">
                    <div class="left-side">
                        <span>开始日期: </span>
                        <el-date-picker v-model="lookDetailsData[index].startTime" type="datetime" value-format="yyyy-MM-dd hh:mm:ss" placeholder="选择日期" />
                        <b>至</b>
                        <el-date-picker v-model="lookDetailsData[index].endTime" type="datetime" value-format="yyyy-MM-dd hh:mm:ss" placeholder="选择日期" />
                        <div style="display:inline-block; padding: 0 10px;">
                            <span>当前汛限: </span>
                            <el-input style="width:100px;border-radius:5px;" v-model="lookDetailsData[index].limitLevel"></el-input>
                            <span style="padding-left:4px"> 米</span>
                        </div>
                        <span class="active" @click="doCalc(item,index)">重新计算</span>
                    </div>
                </div>
                <!-- 图表 -->
                <div :id="'chart'+ index" style="padding:20px 0 0 0; width:100%;height:550px;"></div>
            </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
// echart插件
import * as echarts from "echarts";
import { dispatchApi } from "@/api/dataScreen/jointDispatch/JointDispatch";
export default {
  name: "PrimaryDialog",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    width: {
      type: String,
      default: "1600px",
    },
    mainTitle: {
      type: String,
      default: "水库洪水调度图",
    },
    leftSide: {
      type: Boolean,
      default: false,
    },
    minimizeStatus: {
      type: Boolean,
      default: false,
    },
    lookDetailsData: {
      type: Array,
      default: [],
    }
  },
  data() {
    return {
        tokenStr: 'eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.gcGB1bClPatg2GZv2RR66iwdG6R5UsX0kmgkMHEf3wWVrKQIFmIA1Yg16xJgmeGfNbD3A9CTHXiRkVr8gXLtoRtlspBrhwL8OfhK_yAHnsCdaBTJDctWP_Ia7y9UnzmRMYMuTFzredeMIzQikxFSzAMWAyGN0WlHE2bMze_gNOQ.b0ffbanUZTskgR_2.eYyCDUOGbfsBThAJwASO6_wT2OC7vMYIWGaSGCuaNVJlAR0EXX0NDSZvGR8rfH-rUZyJyQgfqZTb80BRjRKX5N67su-1SVyVtu2-lvcfyyyVDtBiqtMKN7z9jDlL2Jvs-pQFBv5QbQpevUZaoSecDReuHovAleQ.ZMO_T18465VDneqtm4K1vw',
    };
  },
  components: {},
  mounted() {
    this.$nextTick(()=> {
        let sTime = this.$moment().subtract(3, "days").format("YYYY-MM-DD HH") + ":00:00";
        let eTime = this.$moment().subtract(-2, "days").format("YYYY-MM-DD HH") + ":00:00";
        this.lookDetailsData.forEach((val,index)=> {
            this.getDetails(sTime,eTime,1.00,val.limitLevel,1.00,val.floodId,index,val.projectName);
        })
    })
  },
  methods: {
    //图标数据获取
    async getDetails(sTime,eTime,flowZoom,limitWater,outflowZoom,projectId,index,stName) {
      await axios({
          url: `http://10.36.160.145:8888/mgt/bus/floodDispatch/dispatch?token=${this.tokenStr}&startTime=${sTime}&endTime=${eTime}&flowZoom=${1}&limitWater=${limitWater}&jsonFloodArray=[]&jsonWaveArray=[]&jsonSplitWaveArray=[]&outflowZoom=${1.00}&outflowArray=[]&projectId=${projectId}`,
          method: "get",
      }).then((res) => {
          if (res.data.success) {
              let xArr = [],
                  rainArr = [],
                  flowArr = [],
                  outFlowArr = [],
                  waterArr = [],
                  actualWaterArr = [],
                  capacityArr = [];
              for (let i in res.data.data) {
                  xArr.push(res.data.data[i].forecastTime);
                  rainArr.push(res.data.data[i].rain);
                  flowArr.push(res.data.data[i].flow);
                  outFlowArr.push(res.data.data[i].outFlow);
                  waterArr.push(res.data.data[i].water);
                  actualWaterArr.push(res.data.data[i].actualWater);
                  capacityArr.push(res.data.data[i].capacity);
              }
              let str = 'chart' + index;
              this.drawChart( xArr, rainArr, flowArr, outFlowArr, waterArr, actualWaterArr, capacityArr,str,stName);
          }
      })
    },
    //重新计算
    doCalc(val,idx) {
        if(!val.startTime && !val.endTime) {
            this.$message({ type: 'warning', message:"请选择起止时间" });
        }else{
            if(val.startTime < val.endTime) {
                this.$nextTick(()=> {
                    this.getDetails(val.startTime,val.endTime,this.inMagnifyNum,val.limitLevel,this.outMagnifyNum,val.floodId,idx);
                })
            }else {
                this.$message({ type: 'warning', message:"结束时间不允大于开始时间" });
            }
        }
    },
    /**
     * 洪水预报图表
     */
    drawChart( xArr, rainArr, flowArr, outFlowArr, waterArr, actualWaterArr, capacityArr,str,stName ) {
        var chartDom = document.getElementById(str);
        var myChart = echarts.init(chartDom);
        var option;
        let months = xArr;
        var colors = ['#5398f6', '#FFA500', '#91CC75','red'];
        option = {
            color: colors,
            grid: {
                left: '20%'
            },
            title: {
                text: stName,
                textStyle: { color: "#4898fb" }
                // subtext: '纯属虚构' 
            },    
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross'
                }
            },
            legend: {
                bottom:'10',
                right: '35',
                textStyle:{
                    // fontSize: 18,//字体大小
                    color: '#3283FF'//字体颜色
                },
                data: ['雨量', '预报水位', '入库流量']
            },
            xAxis: [
                {
                    type: 'category',
                    axisTick: {
                        alignWithLabel: true
                    },
                    axisTick: {
                        show: false
                    },
                    axisLine: {
                        show: false,
                        position: "top",
                        lineStyle: {
                            color: "rgba(255,255,255,0.60)",
                        },
                    },
                    axisLabel: {
                        color: '#00C1FF',
                        formatter: function(value){
                            return new Date(value).format("MM-dd hh:mm:ss");
                        },
                    },
                    splitLine: {
                        lineStyle: {
                            color: "#00C1FF",
                        }
                    },
                    name: 'm',
                    nameTextStyle: {
                        color: "#00C1FF",
                    },
                    axisLabel: {
                        color: "#00C1FF",
                    },
                    data: months
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '预报水位',
                    min: 0,
                    max: 250,
                    offset: 80,
                    position: 'left',
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: colors[0]
                        }
                    },
                    axisLabel: {
                        formatter: '{value} m'
                    }
                },
                {
                    type: 'value',
                    name: '入库流量',
                    min: 0,
                    max: 250,
                    position: 'right',
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: colors[1]
                        }
                    },
                    axisLabel: {
                        formatter: '{value} m³/s'
                    }
                },
                {
                    type: 'value',
                    name: '雨量',
                    min: 0,
                    max: 25,
                    inverse: true,
                    position: 'left',
                    nameLocation: "start",
                    axisLine: {
                        show: true,
                        position: "top",
                        lineStyle: {
                            color: colors[2]
                        }
                    },
                    axisLabel: {
                        formatter: '{value} mm'
                    }
                },
            ],
            series: [
                {
                    name: '预报水位',
                    type: 'line',
                    smooth: true,
                    itemStyle: {
                    normal: {
                        lineStyle: {
                        width: 2,
                        type: "dashed",
                        },
                    },
                    },
                    data: waterArr
                },
                {
                    name: '入库流量',
                    type: 'line',
                    yAxisIndex: 1,
                    itemStyle: {
                    normal: {
                        lineStyle: {
                            width: 2,
                            type: "dashed",
                        },
                    },
                    },
                    data: flowArr
                },
                {
                    name: '雨量',
                    type: 'bar',
                    yAxisIndex: 2,
                    position: 'top',
                    barWidth:'20',              //---柱形宽度
                    barCategoryGap:'20%',
                    data: rainArr
                },
            ]
        };
        option && myChart.setOption(option);
    },
    handleClose() {
      this.$emit("closeDialog");
    },
    // peripheralAnalysis() {
    //   this.$emit("analysis");
    // },
    // minimize() {
    //   this.$emit("minimize");
    // },
  },
};
</script>

<style lang="scss" scoped>
.primary-dialog {
  ::v-deep .el-dialog {
    background: transparent;
    .el-dialog__header {
      // background: linear-gradient(to bottom, #35b9e3, #5398f6) left top
      //     no-repeat,
      //   linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
      //   linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
      //   linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat;
      background-size: 18px 2px, 2px 18px;
      padding: 2px;
      // padding-bottom: 0;
      height: 54px;
      .primary-dialog__title {
        // border: 1px solid #00c1ff;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1f375f 0%, #1b3047 100%);
        // border-bottom: 0;
        height: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 40px;
        .primary-dialog__title--label {
          background: url(~assets/images/WaterDisastersImage/title-bg.png)
            no-repeat;
          // background-size: 100% 100%;
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
      // background: linear-gradient(to bottom, #5398f6, #35b9e3) left bottom
      //     no-repeat,
      //   linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
      //   linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
      //   linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
      background-size: 18px 2px, 2px 18px;
      border-bottom: 0.0625rem solid #00c1ff;
      padding: 2px;
      padding-top: 0;
      .primary-dialog__content {
        height: 600px;
        overflow-y: auto;
        overflow-x: hidden;
        // border: 1px solid #00c1ff;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
        border-top: 0;
        #floodEchart {
            width: 100%;
            height: 500px;
        }
        .first-line {
            .left-side {
                padding-left:20px;
                color: #4898fb;
                .el-input {
                    width: 195px;
                    border: 1px solid #4898fb;
                    ::v-deep.el-input__icon {
                        line-height: 1.5rem;
                    }
                    input::-webkit-input-placeholder {
                        color: #4898fb;
                    }
                    .el-input__prefix, .el-input__suffix {
                        position: absolute;
                        top: -6px;
                        height: 100%;
                        color: #4898fb;
                        text-align: center;
                    }
                    .el-input__inner {
                        height: 28px;
                        color: #4898fb;
                        background: rgba(0,0,0,0);
                    }
                    .el-input__prefix {
                        left: 0.3125rem;
                        color: #4898fb;
                        top: -6px;
                        -webkit-transition: all .3s;
                        transition: all .3s;
                    }
                }
                b {
                    padding: 0 5px;
                    font-weight: normal;
                }
                .active {
                    color: #fff;
                    padding:7px 10px;
                    border-radius: 5px;
                    background: #3283FF;
                }
            }
        }
      }
    }
  }
  &.project-dialog-wrap{
    ::v-deep .el-dialog{
      margin-left: 20px;
      margin-top: 10vh!important;
    } 
  }
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