<template>
    <el-dialog title="洪水预报" class="dispatch-dialog" width="100%" top="2vh" :before-close="handleClose" :visible.sync="dialogVisible" :close-on-click-modal="false">
        <div class="dialog-content" style="height: 100%;padding: 10px 40px 0 40px;">
            <div class="configuration-header">
                <el-row>
                    <el-col :span="12" v-for="(item,index) in lookDetailsData" :key="index">
                        <div class="first-line">
                            <div class="left-side">
                                <span>开始日期 </span>
                                <el-date-picker v-model="lookDetailsData[index].startTime" type="datetime" value-format="yyyy-MM-dd hh:mm:ss" placeholder="选择日期" />
                                <b>至</b>
                                <el-date-picker v-model="lookDetailsData[index].endTime" type="datetime" value-format="yyyy-MM-dd hh:mm:ss" placeholder="选择日期" />
                                <div style="display:inline-block; padding: 0 10px;">
                                    <span>当前汛限: </span>
                                    <el-input style="width:120px;border-radius:5px;" v-model="lookDetailsData[index].limitLevel"></el-input>
                                    <span style="padding-left:4px"> 米</span>
                                </div>
                                <span class="active" @click="doCalc(item,index)">重新计算</span>
                            </div>
                        </div>
                        <!-- 图表展示 -->
                        <div :id="'chart'+ index" style="padding:20px 0 0 0; height: 600px; width: 100%;"></div>
                    </el-col>
                </el-row>
            </div>
        </div>
        <!-- 尾部取消 -->
        <div slot="footer">
            <div class="form-footer" style="margin-right:45px;">
                <el-button class="form-btn btn-save" @click="handleClose">取消</el-button>
            </div>
        </div>
    </el-dialog>
</template>

<script>
    import axios from "axios";
    export default {
        name: "FloodPredictionDialog",
        props: {
            dialogVisible: { type: Boolean, default: false },
            lookDetailsData: { type: Array },
        },
        data() {
            return {
                tokenStr: 'eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.gcGB1bClPatg2GZv2RR66iwdG6R5UsX0kmgkMHEf3wWVrKQIFmIA1Yg16xJgmeGfNbD3A9CTHXiRkVr8gXLtoRtlspBrhwL8OfhK_yAHnsCdaBTJDctWP_Ia7y9UnzmRMYMuTFzredeMIzQikxFSzAMWAyGN0WlHE2bMze_gNOQ.b0ffbanUZTskgR_2.eYyCDUOGbfsBThAJwASO6_wT2OC7vMYIWGaSGCuaNVJlAR0EXX0NDSZvGR8rfH-rUZyJyQgfqZTb80BRjRKX5N67su-1SVyVtu2-lvcfyyyVDtBiqtMKN7z9jDlL2Jvs-pQFBv5QbQpevUZaoSecDReuHovAleQ.ZMO_T18465VDneqtm4K1vw',
            }
        },
        mounted() {
            this.$nextTick(()=> {
                let sTime = this.$moment().subtract(3, "days").format("YYYY-MM-DD HH") + ":00:00";
                let eTime = this.$moment().subtract(-2, "days").format("YYYY-MM-DD HH") + ":00:00";
                this.lookDetailsData.forEach((val,index)=> {
                    this.getDetails(sTime,eTime,1.00,val.limitLevel,1.00,val.floodId,index,val.stName);
                })
            })
        },
        methods: {
            //取消按钮
            handleClose() {
                this.$emit("handleClose");
            },
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
            //图表容器
            drawChart( xArr, rainArr, flowArr, outFlowArr, waterArr, actualWaterArr, capacityArr,str,stName ) {
                // 基于准备好的dom，初始化echarts实例
                this.$echarts.dispose(document.getElementById(str));
                let myChart = this.$echarts.init(document.getElementById(str));
                let colors = [ "#51FEBF", "#01C9FE", "#301FFF", "#9F1CFF", "#FFC31C", "#FF4A1A" ];
                let months = xArr;
                // 指定图表的配置项和数据
                let option = {
                    color: colors,
                    title: {
                        text: stName,
                        textStyle: { color: "#4898fb" }
                        // subtext: '纯属虚构' 
                    },       //标题，可以自定义标题的位置和样式
                    grid: {           // 间距是 根据x、y轴计算的；假如都是0，x、y轴的label汉字就隐藏掉了。
                        left: 115,    // 默认10%，给24就挺合适的。
                        right: 30,   // 默认10%
                    },
                    legend: {         //图例，每一个系列独有一个图例，注意：图例的名字必须跟下面series数组里面的name一致。
                        data: [ "雨量", "入库流量", "预测水位" ],
                        textStyle: { color: "#4898fb" }
                    },
                    tooltip: {                //鼠标悬浮时的样式，可自定义
                        trigger: "axis",
                        axisPointer: {        // 坐标轴指示器，坐标轴触发有效
                            type: "cross"     // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    xAxis: {
                        //x轴的配置
                        data: months,
                        axisLine: {
                            lineStyle: { color: "#4898fb" },
                        },
                        axisLabel: {
                            formatter: function(value){
                                return new Date(value).format("MM-dd hh:mm:ss");
                            },
                            textStyle: { color: "#4898fb" }
                        }
                    },
                    yAxis: [
                        {
                            position: "left",
                            type: "value",
                            name: "雨量",
                            inverse:true,
                            splitLine: {
                                show: true,
                                lineStyle: { color: "rgba(0,0,0,.6)" },
                            },
                            axisLine: {
                                lineStyle: { color: colors[0] },
                            },
                            axisLabel: {
                                formatter: "{value} m",
                                textStyle: { color: "#4898fb" }
                            }
                        }, {
                            position: "left",
                            offset: 60,
                            type: "value",
                            splitLine: {
                                show: true,
                                lineStyle: { color: "rgba(0,0,0,.6)" }
                            },
                            name: "入库流量",
                            axisLine: {
                                lineStyle: { color: colors[1] }
                            },
                            axisLabel: {
                                formatter: "{value} m³/s",
                                textStyle: { color: "#4898fb" }
                            }
                        }, {
                            position: "left",
                            offset: 140,
                            type: "value",
                            splitLine: {
                                show: true,
                                lineStyle: { color: "rgba(0,0,0,.6)" }
                            },
                            name: "出库流量",
                            // axisLine: {
                            //     lineStyle: { color: colors[2] }
                            // },
                            axisLabel: {
                                formatter: "{value} m³/s",
                                textStyle: { color: "#4898fb" }
                            }
                        }, {
                            position: "right",
                            type: "value",
                            name: "预测水位",
                            splitLine: {
                                show: true,
                                lineStyle: { color: "rgba(0,0,0,.6)" }
                            },
                            axisLine: {
                                lineStyle: { color: colors[3] },
                            },
                            axisLabel: {
                                formatter: "{value} m",
                                textStyle: { color: "#4898fb" }
                            }
                        }, {
                            position: "right",
                            offset: 60,
                            type: "value",
                            name: "实测水位",
                            splitLine: {
                                show: true,
                                lineStyle: { color: "rgba(0,0,0,.6)" }
                            },
                            // axisLine: {
                            //     lineStyle: { color: colors[4] }
                            // },
                            axisLabel: {
                                formatter: "{value} m",
                                textStyle: { color: "#4898fb" }
                            }
                        }, {
                            position: "right",
                            offset: 120,
                            type: "value",
                            name: "库容",
                            splitLine: {
                                show: true,
                                lineStyle: { color: "rgba(0,0,0,.6)" }
                            },
                            // axisLine: {
                            //   lineStyle: { color: colors[5] }
                            // },
                            axisLabel: {
                              formatter: "{value} 万m³",
                              textStyle: { color: "#4898fb" }
                            }
                        }
                    ],
                    toolbox: { show: false },
                    series: [
                        {
                            name: "雨量",
                            type: "bar",
                            barMaxWidth: "20%",
                            label: {
                                normal: {
                                    show: false,
                                    position: "top"
                                }
                            },
                            yAxisIndex: "0", //使用第一个y轴，序号从0开始
                            data: rainArr,
                        }, {
                            name: "入库流量",
                            type: "line",
                            symbol: "circle", //设定为实心点
                            symbolSize: 6, //设定实心点的大小
                            barMaxWidth: "20%",
                            smooth: true,
                            label: {
                                normal: {
                                    show: false,
                                    position: "top"
                                },
                            },
                            yAxisIndex: "1", //使用第二个y轴
                            data: flowArr,
                        }, {
                            name: "预测水位",
                            type: "line",
                            symbol: "circle", //设定为实心点
                            symbolSize: 6, //设定实心点的大小
                            barMaxWidth: "20%",
                            smooth: true,
                            label: {
                                normal: {
                                    show: false,
                                    position: "top"
                                }
                            },
                            yAxisIndex: "4", //使用第三个y轴
                            data: waterArr
                        }
                    ]
                }
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
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
            }
        }
    }
</script>

<style lang="scss" scoped>
    .dispatch-dialog {
        user-select: none;
        .el-dialog {
            ::v-deep.el-dialog__header {
                margin: 0 70px !important;
                padding: 20px 60px !important;
            }
            .dialog-content {
                width: 100%;
                overflow: auto;
            }
        }
    }
    .dialog-content {
        .configuration-header {
            width: 100%;
            height: 100px;
            padding: 0;
            .first-line {
                .left-side {
                    padding-left:20px;
                    color: #4898fb;
                    .el-input {
                        width: 210px;
                        border: 1px solid #4898fb;
                        ::v-deep.el-input__icon {
                            line-height: 1.5rem;
                        }
                        .el-input__inner {
                            height: 28px;
                            color: #4898fb;
                            background: rgba(0,0,0,0);
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
    .form-btn {
        background: rgba(0, 0, 0, 0);
        color: #2c72be;
        border: none;
        border-radius: 0;
        &.btn-save {
            color: #fff;
            background: #239def;
        }
    }
    .el-form-item__label {
        color: #fff;
    }
</style>