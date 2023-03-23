
<template>
    <el-dialog :visible.sync="dialogVisible" width="1200px" :before-close="handleClose" top="15vh"  title="内涝模型" class="model-dialog" :close-on-click-modal="false" append-to-body>
        <!-- 中间内容 -->
        <div class="dialog-content">
            <el-row>
                <!-- 列表展示 -->
                <el-col :span="8">
                    <el-table ref="interfaceTable" :header-cell-style="{'text-align':'left'}" :cell-style="{'text-align':'left'}" :data="waterPointsData" height="500" highlight-current-row @row-click="rowClick">
                        <el-table-column type="index" label="序号" width="55">
                            <template slot-scope="scope">
                                <span class="table-order">{{ scope.$index + 1 }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column :show-overflow-tooltip="true" prop="name" label="名称">
                            <template slot-scope="scope">
                                <span v-if="scope.row.ponding" class="hot-sty">有积水</span>
                                <span class="table-order">{{scope.row.name}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="waterLevel" label="水深 (cm)" width="100px" />
                    </el-table>
                </el-col>
                <!-- 图标容器 -->
                <el-col :span="16">
                    <div id="echart0" style="height: 500px; width: 750px;border: 1px solid #229DEF;"></div>
                </el-col>
            </el-row>
        </div>
    </el-dialog>
</template>

<script>
    //接口路径
    import { dispatchApi } from "@/api/dataScreen/jointDispatch/JointDispatch";
    export default {
        props: {
            dialogVisible: { type: Boolean, default: false }
        },
        data() {
            return {
                waterPointsData: [],
                yListData: [],
                xListData: [],
                name: ''
            }
        },
        mounted() {
            this.$nextTick(()=> {
                this.floodPoint();
            })
        },
        watch: {
            waterPointsData: function() {
                this.$nextTick(function() {
                    this.$refs.interfaceTable.setCurrentRow(this.waterPointsData[0]);
                    this.name = this.waterPointsData[0].name;
                    this.floodPointForecastCurve();
                    this.drawChart0();
                })
            }
        },
        methods: {
            //积水点接口获取
            async floodPoint() {
                await dispatchApi.floodPoint().then((res) => {
                    if (res.code == 0) {
                        this.waterPointsData = res.data;
                    }
                })
            },
            //tab行点击事件
            rowClick(row) {
                this.name = row.name;
                this.floodPointForecastCurve();
            },
            //关闭按钮
            handleClose() {
                this.$emit("handleClose");
            },
            //图表
            drawChart0() {
                this.$echarts.dispose(document.getElementById("echart0"));
                let myChart = this.$echarts.init(document.getElementById("echart0"));
                let option = {
                    title: {
                        subtext: this.name + ' (cm)',
                        left: '20',
                        align: 'right'
                    },
                    grid: {
                        left: '1%',
                        right: '3%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        data: this.xListData,
                        axisLabel: {
                            show: true,
                            textStyle: {
                            color: '#2672B7'   //这里用参数代替了
                            }
                        },
                    },
                    yAxis: {
                        type: 'value',
                        splitLine:{
                            show:true,
                            lineStyle: {
                            color: '#2672B7'  //这里用参数代替了
                            }
                        },
                        //  去掉背景的网格线
                        axisLabel: {
                            show: true,
                            textStyle: {
                            color: '#2672B7'  //这里用参数代替了
                            }
                        },
                    },
                    series: [{
                        data: this.yListData,
                        type: 'line',
                        smooth: true,
                        itemStyle : {
                            normal : {
                                color:'#23E3FB',
                                lineStyle:{
                                    color:'#23E3FB'
                                }
                            }
                        }
                    }]
                };
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            },
            //图表请求接口
            async floodPointForecastCurve() {
                this.yListData = [];
                this.xListData = [];
                await dispatchApi.floodPointForecastCurve({name:this.name}).then((res) => {
                    if (res.code == 0) {
                        res.data.forEach(val => {
                            this.yListData.push(val.value);
                            this.xListData.push(val.key.slice(5,10));
                        });
                        this.$nextTick(()=> {
                            this.drawChart0();
                        })
                    }
                })
            }
        }
    }
</script>

<style lang="scss">
    @import "@/style/blueDialog.scss";
    .model-dialog {
        margin: 0 auto;
        user-select: none;
        .el-dialog {
            height: 600px;
            background: rgba(0, 0, 0, 0) url(../../../../../assets/images/bs-dialog-bg.png)
            no-repeat center center;
            background-size: 100% 100%;
            .el-dialog__header {
                padding: 10px 50px;
            }
            .el-dialog__title {
                line-height: 1.5rem;
                font-size: 1.125rem;
                color: #50BCEA;
            }
            .el-dialog__footer .form-btn {
                background: rgba(0, 0, 0, 0);
                color: #2c72be;
                border: none;
                border-radius: 0;
                &.btn-save {
                    color: #fff;
                    background: #239def;
                }
            }
            .dialog-content {
                padding: 10px 15px;
                .el-table th, .el-table tr {
                    background-color: none;
                    color: #2c72be;
                }
                .el-table th {
                    color: #728691;
                    background: none !important; 
                }
                .hot-sty {
                    background: red;
                    color: #FFFFFF;
                    border: 1px solid red;
                    font-size: 12px;
                    padding: 2px 3px;
                    margin-right: 5px;
                    border-radius: 12px;
                }
                .el-table__body-wrapper::-webkit-scrollbar {
                    width: 0px;
                }
            }
        }
    }
</style>