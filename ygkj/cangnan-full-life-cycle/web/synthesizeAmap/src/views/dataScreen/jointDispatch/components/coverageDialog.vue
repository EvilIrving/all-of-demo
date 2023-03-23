<template>
    <el-dialog :visible.sync="dialogVisible" width="700px" :before-close="handleClose" top="20vh" title="泰顺县内河水位" class="dispatch-dialog" :close-on-click-modal="false">
        <div class="dialog-content" style="height:400px;">
            <div class="dateSty">
                <el-time-select v-model="value" :picker-options="{ start: '00:00', step: '00:30', end: '24:00' }" placeholder="选择时间" />
            </div>
            <div id="radarChart"></div>
        </div> 
    </el-dialog>
</template>

<script>
   
    import echarts from "echarts";
    export default {
        name: "JudgedDialog",
        props: {
            dialogVisible: {
                type: Boolean,
                default: false,
            }
        },
        data() {
            return {
                value: ''
            }
        },
        mounted() {
            this.$nextTick(()=>{
                this.initRadarChart();
            });
        },
        methods: {
            //取消按钮
            handleClose() {
                this.$emit("handleClose");
            },
            //echarts图表
            initRadarChart() {
                console.log(document.getElementById("radarChart"));
                let radarChart = this.$echarts.init(document.getElementById("radarChart"));
                radarChart.setOption({
                    title: {
                        text: '水位曲线',
                        textStyle: {
                            color: '#4db6e4',
                            
                        },
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross',
                            label: {
                                backgroundColor: '#fff',
                            }
                        }
                    },
                    legend: {
                        textStyle: {
                            color: '#fff'
                        },
                        left: 100,
                        data: ['闸上水位','超汛期水位']
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '10%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            axisLine: {
                                lineStyle: {
                                    color: "#fff"
                                }
                            },
                            boundaryGap: false,
                            data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00', '24:00']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisLine: {
                                lineStyle: {
                                    color: "#fff",
                                }
                            }
                        }
                    ],
                    series: [
                        {
                            name: '闸上水位',
                            type: 'line',
                            stack: '总量',
                            areaStyle: {
                                color: '#4db6e4',
                                opacity: 0.1,
                            },
                            itemStyle:{ 
                                normal:{
                                    color:'#4db6e4'
                                } 
                            },
                            data: [20, 13, 10, 14, 50, 20, 10]
                        },
                        {
                            name: '超汛期水位',
                            type: 'line',
                            stack: '总量',
                            itemStyle:{ 
                                normal:{
                                    color:'red'
                                } 
                            },
                            data: [12, 32, 11, 34, 44, 30, 21]
                        }
                    ]
                })
            }
        }
    }
</script>

<style lang="scss">
    .dispatch-dialog {
        user-select: none;
        .el-dialog {
            .dialog-content {
                overflow: auto;
                padding: 10px 30px;
                position: relative;
                #radarChart {
                    height: 100%;
                    width: 100%;
                    
                }
                .dateSty {
                    position: absolute;
                    right: 55px;
                    top: 0;
                    
                    z-index: 9999;
                }
            }
        }
    }
</style>