<template>
    <div class="sidebar-box">
        <div class="sidebar-top">
            <!-- 水库水位列表 -->
            <div v-if="num == '2'" style="margin-top: 10px;">
                <el-row style="margin-bottom:10px;">
                    <el-col :span="8">
                        <div class="forecast-sty forecast">
                            <p class="font-sty">预报总库容</p>
                            <p style="color:#23E3FB;font-size:16px;">{{modelInfo.capacity}} 万m³</p>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="forecast-sty forecast">
                            <p class="font-sty">超汛限水位时间</p>
                            <p style="color:#23E3FB;font-size:16px;">{{modelInfo.overWaterTime}}</p>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="forecast-sty forecast">
                            <p class="font-sty">富余库容</p>
                            <p style="color:#23E3FB;font-size:16px;">{{modelInfo.overCapacity}} 万m³</p>
                        </div>
                    </el-col>
                </el-row>
                <el-row style="margin-bottom:10px;">
                    <el-col :span="8">
                        <div class="warning-sty forecast">
                            <p class="font-sty">入库洪量</p>
                            <p style="color:#F7994A;font-size:16px;">{{modelInfo.inFlowVolume}} 万m³</p>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="warning-sty forecast">
                            <p class="font-sty">出库洪量</p>
                            <p style="color:#F7994A;font-size:16px;">{{modelInfo.outFlowVolume}} 万m³</p>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="warning-sty forecast">
                            <p class="font-sty">流域降雨总量</p>
                            <p style="color:#F7994A;font-size:16px;">{{modelInfo.rainCnt}} 万m³</p>
                        </div>
                    </el-col>
                </el-row>
                <el-row style="margin-bottom:20px;">
                    <el-col :span="8">
                        <div class="flood-peak-sty forecast">
                            <p class="font-sty">入库洪峰</p>
                            <p style="color:#4898FB;font-size:16px;">{{modelInfo.highFlow}} 万m³/s</p>
                            <p class="bottom-sty">{{modelInfo.highWaterTime.slice(5,19)}} </p>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="flood-peak-sty forecast">
                            <p class="font-sty">预报最高库水位</p>
                            <p style="color:#4898FB;font-size:16px;">{{modelInfo.highWater}} m</p>
                            <p class="bottom-sty">{{modelInfo.highWaterTime.slice(5,19)}} </p>
                        </div>
                    </el-col>
                </el-row>
                <!-- tab切换 -->
                <div class="right">
                    <template>
                        <span v-for="(name, index) in sidebarTabList" :key="index" @click="sidebarChange(name)" :class="activeSidebarName == name ? 'active' : ''">{{ name }}</span>
                    </template>
                </div>
            </div>
            <!-- 水闸水位列表 -->
            <div v-if="num == '1'">
                <p style="padding: 15px 0;">闸门开度</p>
                <el-row style="margin-bottom:10px;">
                    <el-col :span="8" v-for="(item,index) in ovs" :key="index">
                        <div :class="[ item == '0.00' ? 'aperture-stys' :'aperture-sty', 'aperture']">
                            <i :class="item == '0.00' ? 'icon-kaidu1' :'icon-kaidu'"></i>
                            <span :style="{color:item == '0.00' ? 'rgba(228, 236, 238)' :' rgba(25, 214, 247)', fontSize:'16px', marginLeft:'10px'}">{{item}}</span>
                        </div>
                    </el-col>
                </el-row>
                <!-- tab切换 -->
                <div class="right">
                    <template>
                        <span v-for="(name, index) in sidebarTabArr" :key="index" @click="sidebarTabChange(name)" :class="activeSidebar == name ? 'active' : ''">{{ name }}</span>
                    </template>
                </div>
            </div>
            <!-- 时间 -->
            <div style=" padding:0 0 0 5px; margin: 15px 0 20px 0;">
                <span style="font-size:16px;color:#3f9bfc;">时间: </span>
                <el-select v-model="time" filterable placeholder="请选择" style="border:1px solid #4898fb;border-radius:5px;" @input="changeDate">
                    <el-option  v-for="item in timeOptions" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
            </div>
            <!-- 图标容器 -->
            <div id="echart-g" style="height: 300px; width: 380px;"></div>
        </div>
    </div>
</template>

<script>
    import { waterApi, rainApi } from "@/api/dataScreen/floodJudge/index";
    export default {
        name: "floodRiskDialog",
        props:[
            'num',
            'yListData1',
            'xListData1',
            'yListData2',
            'YechartDataList',
            'XechartDataList',
            'LevelsDataList',
            'echartName',
            'modelInfo',
            'strobeOvs'
        ],
        data() {
            return {
                timeOptions: [
                    {
                    value: 1,
                    label: "前一天",
                    },
                    {
                    value: 3,
                    label: "前二天",
                    },
                    {
                    value: 5,
                    label: "前三天",
                    },
                    {
                    value: 4,
                    label: "前四天",
                    },
                    {
                    value: 7,
                    label: "前一周",
                    },
                    {
                    value: 14,
                    label: "前二周",
                    },
                ],
                time: 14,
                sidebarTabArr: ["闸上水位", "闸下水位", "过闸流量"],
                activeSidebar: "闸上水位",
                sidebarTabList: ["实时水位", "入库流量", "出口流量"],
                activeSidebarName: '实时水位',
                titleData: '',
                listDetails: false,
                input: '',
                ovs: [],
                YechartList: [],
                XechartList: [],
                LevelsList: []
               
            }
        },
        watch:{   
            XechartDataList(val) {
                this.drawChart();
            },
            strobeOvs(val) {
                if (val.length != 0) {
                    this.ovs = val[0].split(',');
                }
            }    
        },
        mounted() {
            this.$nextTick(()=> {
                this.drawChart();
            });
        },
        methods: {
            //右侧列表
            listData(val) {
                this.titleData = val;
                this.listDetails = true;
            },
            //返回按钮
            getBack() {
                this.titleData = '';
                this.listDetails = false;
            },
            //tab切换
            sidebarTabChange(name) {
                this.activeSidebar = name;
            },
            //时间获取
            changeDate() {
                this.$parent.waterStation();
            },
            //tab切换
            sidebarTabChange(name) {
                this.activeSidebar = name;
            },
            //水库水位
            sidebarChange(name) {
                this.activeSidebarName = name;
            },
            //内涝模型 积水点 图表
            drawChart() {
                this.$echarts.dispose(document.getElementById("echart-g"));
                let myChart = this.$echarts.init(document.getElementById("echart-g"));
                let option = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: this.echartName,
                        textStyle:{
                            color: '#fff'
                        }
                    },
                    grid: {
                        top: '10%',
                        left: '2%',
                        right: '1%',
                        bottom: '3%',
                        containLabel: true
                    },
                     axisLine: {
                        lineStyle: {
                            color: '#2672B7',
                            width: 1, //这里是为了突出显示加上的  
                        }
                    },
                    xAxis: {
                        type: 'category',
                        data: this.XechartDataList,
                        axisLabel: {
                            show: true,
                            textStyle: {
                                color: '#2672B7'   //这里用参数代替了
                            }
                        },
                        axisLine:{
                            lineStyle:{
                                color:'#2672B7'
                            }
                        } 
                    },
                    yAxis: {
                        type: 'value',
                        name: "m",
                        nameTextStyle:{
                            color:"#2672B7", 
                            fontSize:14,  
                        },
                        axisLine:{
                            show:false
                        },
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
                        }
                    },
                    series: [{
                        data: this.YechartDataList,
                        type: 'line',
                        name: this.echartName[0],
                        smooth: true,
                        itemStyle : {
                            normal : {
                                color:'#23E3FB',
                                lineStyle:{
                                    color:'#23E3FB'
                                }
                            }
                        }
                    },{
                        data: this.LevelsDataList,
                        type: 'line',
                        name: this.echartName[1],
                        smooth: false,
                        itemStyle : {
                            normal : {
                                color:'red',
                                lineStyle:{
                                    color:'red',
                                    width:2,
                                    type:'dotted'  //'dotted'虚线 'solid'实线
                                }
                            }
                        }
                    }]
                }
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }
        }
    }
</script>

<style lang='scss' scoped>
    @import "@/style/bigScreenSidebar.scss";
</style>
<style lang="scss">
    $searchInpHeight: 25px;
    $mainColor: #3f9bfc;
    $tdHeight: 37px;
    .sidebar-box {
        margin-top:100px;
        .sidebar-top, .sidebar-bottom {
            width: 100%;
            height: 50%;
        }
        .titleSty {
            padding: 20px 0;
            font-size: 14px;
            border-bottom: 2px solid #073b5b;
        }
        .listSty {
            padding: 7px 0;
            opacity: 0.6;
            font-size: 13px;
        }
        .ipnSty {
            font-size:12px;
            border:1px solid #4898fb;
            border-radius: 20px;
            opacity: 0.8;
            margin-bottom: 10px;
        }
        .contentSty {
            font-size: 14px;
            opacity: 0.8;
            padding:10px 0 0 43px;
        }
        .sizeColor {
            font-size: 18px;
            color:#4898fb;
        }
        .beijing {
            display: inline-block;
            width: 20px;
            height: 20px;
            vertical-align:middle;
            text-align: center;
            cursor: pointer;
            color: #4898fb;
            background: url(../../../../../assets/images/explain-icon.png) no-repeat;
            background-size: 100% 100%;
            margin-right: 6px;
        }
        .right-arrows {
            display: inline-block;
            width: 20px;
            height: 14px;
            vertical-align:middle;
            text-align: center;
            cursor: pointer;
            color: #4898fb;
            background: url(../../../../../assets/images/right-arrows.png) no-repeat;
            background-size: 100% 100%;
            margin-right: 6px;
        }
        .sidebar-title {
            display: flex;
            line-height: 50px;
            align-items: center;
            justify-content: space-between;
            &-tab {
                display: flex;
                > div {
                    font-size: 12px;
                    height: 24px;
                    line-height: 24px;
                    width: 44px;
                    border-radius: 12px;
                    text-align: center;
                    cursor: pointer;
                    background: #073b5b;
                    margin-left: 6px;
                    &.active {
                        background-color: #129cdc;
                    }
                }
            }
        }
        .el-input__inner, .el-input__icon {
            color: $mainColor;
            height: $searchInpHeight;
            line-height: $searchInpHeight;
            &::-webkit-input-placeholder {
                color: $mainColor;
            }
            &::-moz-placeholder {
                color: $mainColor;
            }
            &:-ms-input-placeholder {
                color: $mainColor;
            }
        }
        .el-select__caret.el-input__icon.el-icon-arrow-up {
            color: $mainColor;
        }
        .sidebar-list {
            .el-table .cell {
              height: $tdHeight;
              line-height: $tdHeight;
            }
            .el-table th,
            .el-table td {
              padding: 0;
              font-size: 16px;
              color: #fff;
              text-align: left;
            }
            .el-table th {
              padding: 3px 0;
              background-color: transparent;
              font-weight: bold;
            }
            .el-table__body tr.current-row > td {
              color: #fff;
              background-color: transparent;
            }
            .type-icon {
                display: inline-block;
                width: 12px;
                height: 12px;
                vertical-align: middle;
                border-radius: 50%;
                margin-right: 8px;
                &.type-0-10 {
                    background: #39a0ff;
                }
                &.type-10-25 {
                    background: #43dc00;
                }
                &.type-25-50 {
                    background: #fcee4e;
                }
                &.type-50-100 {
                    background: #f7994a;
                }
                &.type-100-150 {
                    background: #9303bc;
                }
                &.type-150 {
                    background: #f40221;
                }
            }
            .type-label {
                vertical-align: middle;
            }
        }
    }
    .bigScreen-sel {
        .el-select-dropdown__item.selected {
            color: $mainColor;
        }
        .el-select-dropdown__item:hover {
            background-color: transparent;
        }
    }
    //水库水位样式
    .forecast {
            width: 97%;
            height: 75px;
            margin: 0 5px;
            padding: 10px 7px;
        }
        .forecast-sty {
            border-top: 2px solid #23E3FB;
            background-color: rgba(25, 214, 247, 0.1);
        }
        .warning-sty {
            border-top: 2px solid  #F7994A;
            background-color: rgba(240, 186, 86, 0.1);
        }
        .flood-peak-sty {
            border-top: 2px solid  #4898FB;
            background-color: rgba(17, 158, 240, 0.1);
        }
        .aperture {
            width: 97%;
            height: 85px;
            margin: 5px;
            padding: 20px 0 0 20px;
        }
        .aperture-sty {
            background-color: rgba(25, 214, 247, 0.1);
        }
        .aperture-stys {
            background-color: rgba(228, 236, 238, 0.1);
        }
        .font-sty {
            color:#fff; 
            opacity: 0.7;
            font-size:14px;
            margin-bottom: 7px;
        }
        .bottom-sty {
            color:#fff; 
            opacity: 0.7;
            font-size:12px;
            margin-top:7px;
        }
        //tab切换样式
        .right {
            > span {
                display: inline-block;
                width: 113px;
                height: 40px;
                line-height: 40px;
                text-align: center;
                cursor: pointer;
                color: #4898fb;
                background: url(../../../../../assets/images/bigScreen-big-tab.png) no-repeat;
                background-size: 100% 100%;
                margin-right: 10px;
                margin-bottom: 20px;
                &.active {
                    color: #a6e9fb;
                    background: url(../../../../../assets/images/bigScreen-big-tab-check.png) no-repeat;
                    background-size: 100% 100%;
                }
            }
        }
</style>