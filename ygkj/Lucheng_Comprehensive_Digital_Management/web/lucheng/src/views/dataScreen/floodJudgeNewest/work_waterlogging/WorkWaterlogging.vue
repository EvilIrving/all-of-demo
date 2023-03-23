<template>
    <div class="work-waterlogging">
        <!-- 地图区域 -->
        <screen-map ref="map" class="screen_map"></screen-map>
        <!-- 左侧菜单 -->
        <div class="bigScreen-left">
            <transition name="fade" v-on:after-leave="transitionDoneLeft">
                <div class="sidebar" :style="{ width: '380px' }" v-show="barshowLeft">
                    <!-- 左侧标题1 -->
                    <div class="screen-title">
                        <div class="title-left">
                            <i class="icon-judge-icon"></i> <span>积水点</span>
                        </div>
                        <!-- 左侧开关 -->
                        <el-switch v-model="reservoirVal" active-color="#106CE0" inactive-color="#ff4949" class="sidebar-switch" />
                    </div>
                    <!-- 左侧tab1 -->
                    <div class="sidebar-list">
                        <el-table ref="table1" :header-cell-style="{'text-align':'left'}" :cell-style="{'text-align':'left'}" :data="waterPointsData" style="width: 100%" height="145" highlight-current-row >
                            <el-table-column type="index" width="55" label="序号">
                                <template slot-scope="scope">
                                    <span class="table-order">{{ scope.$index + 1 }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column :show-overflow-tooltip="true" prop="name" label="名称" width="220">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.ponding" class="hot-sty">有积水</span>
                                    <span class="table-order">{{scope.row.name}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="waterLevel" label="水深 (cm)" />
                        </el-table>
                    </div>
                    <!-- 左侧标题2 -->
                    <div class="screen-title">
                        <div class="title-left">
                            <i class="icon-judge-icon"></i> <span>河网水位</span>
                        </div>
                        <!-- 左侧开关 -->
                        <el-switch v-model="reservoirVal" active-color="#106CE0" inactive-color="#ff4949" class="sidebar-switch" />
                    </div>
                    <!-- 左侧tab2 -->
                    <div class="sidebar-list">
                        <el-table ref="table2" :header-cell-style="{'text-align':'left'}" :cell-style="{'text-align':'left'}" :data="riverNetworkData" style="width: 100%" height="145" highlight-current-row >
                            <el-table-column type="index" label="序号" width="55">
                                <template slot-scope="scope">
                                    <span class="table-order">{{ scope.$index + 1 }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column :show-overflow-tooltip="true" prop="stName" label="名称" width="160">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.warning" class="hot-sty">超</span>
                                    <span class="table-order">{{scope.row.stName}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="waterLevel" label="水位 (m)" />
                            <el-table-column prop="warningLevel" label="警戒水位 (m)" />
                        </el-table>
                    </div>
                    <!-- 左侧标题3 -->
                    <div class="screen-title">
                        <div class="title-left">
                            <i class="icon-judge-icon"></i> <span>潮位</span>
                        </div>
                        <!-- 左侧开关 -->
                        <el-switch v-model="reservoirVal" active-color="#106CE0" inactive-color="#ff4949" class="sidebar-switch" />
                    </div>
                    <!-- 左侧tab3 -->
                    <div class="sidebar-list">
                        <el-table ref="table3" :header-cell-style="{'text-align':'left'}" :cell-style="{'text-align':'left'}" :data="waterStationsData" style="width: 100%" height="145" highlight-current-row >
                            <el-table-column type="index" label="序号" width="55">
                                <template slot-scope="scope">
                                    <span class="table-order">{{ scope.$index + 1 }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column :show-overflow-tooltip="true" prop="stName" label="名称" />
                            <el-table-column prop="tdz" label="潮位 (m)" />
                        </el-table>
                    </div>
                    <!-- 左侧标题4 -->
                    <div class="screen-title">
                        <div class="title-left">
                            <i class="icon-judge-icon"></i> <span>闸门</span>
                        </div>
                        <!-- 左侧开关 -->
                        <el-switch v-model="reservoirVal" active-color="#106CE0" inactive-color="#ff4949" class="sidebar-switch" />
                    </div>
                    <!-- 左侧tab4 -->
                    <div class="sidebar-list">
                        <el-table ref="table4" :header-cell-style="{'text-align':'left'}" :cell-style="{'text-align':'left'}" :data="strobeData" style="width: 100%" height="145" highlight-current-row>
                            <el-table-column type="index" label="序号" width="55">
                                <template slot-scope="scope">
                                    <span class="table-order">{{ scope.$index + 1 }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column :show-overflow-tooltip="true" prop="stName" label="名称" width="150">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.warning" class="hot-sty">超</span>
                                    <span class="table-order">{{scope.row.stName}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="upz" label="闸上水位 (m)" width="120" />
                            <el-table-column prop="dwz" label="闸下水位 (m)" width="120" />
                            <el-table-column prop="openGate" label="开度 ( °)" width="120" />
                        </el-table>
                    </div>
                    <!-- 左侧边栏切换按钮 -->
                    <div class="switch-btn" @click="toggleSideBarLeft()"></div>
                </div>
            </transition>
            <!-- 左侧边栏切换按钮 -->
            <div class="switch-btn show-btn" v-show="showBtnLeft" @click="toggleSideBarLeft()" ></div>
        </div>
        <!-- 右侧内涝模型 -->
        <div class="waterlogging-sty" @click="waterlogging">
            <i class="icon-waterlogging"></i>
        </div>
        <i class="icon-waterlogging"></i>
        <!-- 右侧菜单 -->
        <div class="bigScreen-right">
            <transition name="fade" v-on:after-leave="transitionDone">
                <div class="sidebar" v-show="barshow">
                    <!-- 右侧标题 -->
                    <div class="screen-title">
                        <div class="title-left"> 
                            <i class="icon-judge-icon"></i> 
                            <span :style="{marginRight:'15px',paddingBottom:'10px',borderBottom:(switchoverShow=='3'?'2px solid #23E3FB':'2px'),color:(switchoverShow=='3'?'#23E3FB':'#728691')}" @click="switchover('3')">洪水风险</span>
                            <span :style="{marginRight:'15px',paddingBottom:'10px',borderBottom:(switchoverShow=='2'?'2px solid #23E3FB':'2px'),color:(switchoverShow == '2'?'#23E3FB':'#728691')}" @click="switchover('2')">泵站</span> 
                            <span :style="{paddingBottom:'10px',borderBottom:(switchoverShow=='1'?'2px solid #23E3FB':'2px'),color:(switchoverShow=='1'?'#23E3FB':'#728691')}" @click="switchover('1')">内涝模型</span> 
                        </div>
                        <!-- 右侧开关 -->
                        <el-switch v-model="reservoirVal" active-color="#106CE0" inactive-color="#ff4949" class="sidebar-switch" />
                    </div>
                    <!-- 洪水风险 -->
                    <div class="sidebar-list"  v-if="switchoverShow == '3'">
                        <flood-risk-dialog v-if="true"></flood-risk-dialog>
                    </div>
                    <!-- 内涝模型 积水点曲线展示 -->
                    <div class="sidebar-list" v-show="switchoverShow == '1'">
                        <p style="padding: 0 0 10px 10px; font-size:14px;color:#3F9BFC;">{{floodPointLsit[0]}} (cm)</p>
                        <div id="echart" style="height: 195px; width: 410px;"></div>
                        <p style="padding:10px; font-size:14px;color:#3F9BFC;">{{floodPointLsit[1]}} (cm)</p>
                        <div id="echart1" style="height: 195px; width: 410px;"></div>
                        <p style="padding:10px; font-size:14px;color:#3F9BFC;">{{floodPointLsit[2]}} (cm)</p>
                        <div id="echart2" style="height: 195px; width: 410px;"></div>
                    </div>
                    <!-- 泵站 -->
                    <div class="sidebar-list" v-if="switchoverShow == '2'">
                        <el-table ref="table3" :data="pumpStationData" style="width: 100%" height="670" highlight-current-row >
                            <el-table-column type="index" label="序号" width="55">
                                <template slot-scope="scope">
                                    <span class="table-order">{{ scope.$index + 1 }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="pustName" label="名称" />
                            <el-table-column prop="insFlow" label="装机流量 (m³/s)" />
                        </el-table>
                    </div>
                    <!-- 右侧边栏切换按钮 -->
                    <div class="switch-btn" @click="toggleSideBar()"></div>
                </div>
            </transition>
            <!-- 右侧边栏切换按钮 -->
            <div class="switch-btn show-btn" v-show="showBtn" @click="toggleSideBar()" ></div>
        </div>
        <!-- 浮框 -->
        <div class="bigScreen_count">
            <div class="box">
                <i class="icon-eau_stagnante icon-sty"></i>
                <div>
                    <p>有积水点</p>
                    <p class="num" :style="{ color:'#ff3525' }">{{waterLoggingData.pondingNum}}</p>
                </div>
            </div>
            <div class="box">
                <i class="icon-avertir icon-sty"></i>
                <div>
                    <p>超警戒内河水位</p>
                    <p class="num" :style="{ color:'#ff3525' }">{{waterLoggingData.riverWarnNum}}</p>
                </div>
            </div>
            <div class="box">
                <i class="icon-vanne icon-sty"></i>
                <div>
                    <p>超警戒闸门</p>
                    <p class="num" :style="{ color:'#ff3525' }">{{waterLoggingData.wagaWarnNum}}</p>
                </div>
            </div>
        </div>
        <!-- 右下侧标识 地图图例 -->
        <div class="map-legend">
            <div class="map-legend-item" >
                <div class="legend-title">闸门</div>
                <div class="legend-detail" :key="index" v-for="(item,index) in strobeLegendList">
                    <i :class="item.type"></i> <span>{{item.label}}</span>
                </div>
            </div>
            <div class="map-legend-item" >
                <div class="legend-title">潮位</div>
                <div class="legend-detail" :key="index" v-for="(item,index) in seaLevelLegendList">
                    <i :class="item.type"></i> <span>{{item.label}}</span>
                </div>
            </div>
            <div class="map-legend-item" >
                <div class="legend-title">河网水位</div>
                <div class="legend-detail" :key="index" v-for="(item,index) in waterwayLegendList">
                    <i :class="item.type"></i> <span>{{item.label}}</span>
                </div>
            </div>
            <div class="map-legend-item" >
                <div class="legend-title">积水点</div>
                <div class="legend-detail" :key="index" v-for="(item,index) in WaterPointsLegendList">
                    <i :class="item.type"></i> <span>{{item.label}}</span>
                </div>
            </div>
        </div>
        <!-- 内涝模型弹窗 -->
        <waterlogging-model-dialog :dialogVisible="waterloggingDialogShow" v-if="waterloggingDialogShow" @handleClose="closeWaterloggingDialog" />
    </div>
</template>

<script>
    //地图组件引用
    import ScreenMap from "../components/ScreenMap";
    //右侧侧边栏组件
    import floodRiskDialog from "./components/floodRiskDialog";
    //内涝模型
    import WaterloggingModelDialog from "./dialog/WaterloggingModelDialog";
    //接口路径
    import { dispatchApi } from "@/api/dataScreen/jointDispatch/JointDispatch";

    export default {
        name: 'WorkWaterlogging',
        components:{
            ScreenMap,
            floodRiskDialog,
            WaterloggingModelDialog
        },
        data() { 
            return {
                showBtnLeft:false,
                barshowLeft:true,
                showBtn:false,
                barshow:true,
                //闸门图列
                strobeLegendList:[{
                    label:'正常',
                    type:'icon-sluice-gate'
                },{
                    label:'有积水',
                    type:'icon-sluice-gate1'
                }],
                //潮位图列
                seaLevelLegendList:[{
                    label:'潮位',
                    type:'icon-sea-level'
                }],
                //内河水位图列
                waterwayLegendList:[{
                    label:'正常河网水位',
                    type:'icon-shuiwei2'
                },{
                    label:'超警河网水位',
                    type:'icon-shuiwei'
                }],
                //积水点图列
                WaterPointsLegendList:[{
                    label:'无积水',
                    type:'icon-stagnante-g'
                },{
                    label:'有积水',
                    type:'icon-stagnante-red'
                }],
                reservoirVal:true,//大中型水库地图点位显隐标记
                waterloggingDialogShow: false,//内涝弹窗显示
                waterPointsData: [],    //积水点接口获取
                riverNetworkData: [],   //河网水位
                waterStationsData:[],   //潮位
                waterLoggingData: {},   //报警统计
                strobeData: [],         //闸门
                switchoverShow: '',
                pumpStationData: [],
                yListData1: [],
                yListData2: [],
                yListData3: [],
                xListData1: [],
                xListData2: [],
                xListData3: [],
                floodPointLsit: [],
            }
        },
        watch: {
            switchoverShow(val) {
                this.drawChart();
                this.drawChart1();
                this.drawChart2();
            },
            //积水点
            waterPointsData(val) {
                this.$refs.map.waterloggingPoint(val);
            },
            //河网水位
            riverNetworkData(val) {
                this.$refs.map.drainagePoint(val);
            },
            //潮位
            waterStationsData(val) {
                this.$refs.map.seaLevelPoint(val);
            },
            //闸门
            strobeData(val) {
                this.$refs.map.sluicePoint(val);
            },
        },
        mounted() {
            this.switchoverShow = '3',
            this.$nextTick(()=> {
                // 拿到表格中承载数据的div元素
                const divData1 = this.$refs.table1.bodyWrapper;
                const divData2 = this.$refs.table2.bodyWrapper;
                const divData3 = this.$refs.table3.bodyWrapper;
                const divData4 = this.$refs.table4.bodyWrapper;
                
                this.initRollTable(divData1);
                this.initRollTable(divData2);
                this.initRollTable(divData3);
                this.initRollTable(divData4);
               
                this.floodPoint();
                this.riverWaterLevel();
                this.waterStations();
                this.waterLoggingWarnNum();
                this.wagaList();
                this.listPustProj();
                this.drawChart();
                this.drawChart1();
                this.drawChart2();
                this.floodPointCurveList();
            })
        },
        methods: {
            // 拿到元素后，对元素进行定时增加距离顶部距离，实现滚动效果(此配置为每100毫秒移动1像素)
            initRollTable(dom){
                setInterval(() => {
                    // 元素自增距离顶部1像素
                    dom.scrollTop += 1
                    // 判断元素是否滚动到底部(可视高度+距离顶部=整个高度)
                    if (dom.clientHeight + dom.scrollTop == dom.scrollHeight) { dom.scrollTop = 0 }// 重置table距离顶部距离
                }, 100);
            },
            //右侧tab切换事件
            switchover(val) {
                this.switchoverShow = val;
            },
            //积水点接口获取
            async floodPoint() {
                await dispatchApi.floodPoint().then((res) => {
                    if (res.code == 0) {
                        this.waterPointsData = res.data;
                        this.$refs.map.waterloggingPoint(res.data);
                    }
                })
            },
            //河网水位
            async riverWaterLevel() {
                await dispatchApi.riverWaterLevel().then((res) => {
                    if (res.code == 0) {
                        this.riverNetworkData = res.data;
                        this.$refs.map.drainagePoint(res.data);
                    }
                })
            },
            //潮位
            async waterStations() {
                let opt = {
                    stationType: 'TT'
                }
                await dispatchApi.waterStations(opt).then((res) => {
                    if (res.code == 0) {
                        this.waterStationsData = res.data;
                        this.$refs.map.seaLevelPoint(res.data);
                    }
                })
            },
            //报警统计
            async waterLoggingWarnNum() {
                await dispatchApi.waterLoggingWarnNum().then((res) => {
                    if (res.code == 0) {
                        this.waterLoggingData = res.data;
                    }
                })
            },
            //闸门
            async wagaList() {
                await dispatchApi.wagaList().then((res) => {
                    if (res.code == 0) {
                        this.strobeData = res.data;
                        this.$refs.map.sluicePoint(res.data);
                    }
                })
            },
            //泵站排水量
            async listPustProj() {
                let opt = {
                    pageNum: 1,
                    pageSize: 99999
                }
                await dispatchApi.listPustProj(opt).then((res) => {
                    if (res.code == 0) {
                        this.pumpStationData = res.data.list;
                    }
                })
            },
            //查询最深的几个积水点曲线
            async floodPointCurveList() {
                await dispatchApi.floodPointCurveList().then((res) => {
                    if (res.code == 0) {
                        res.data.forEach((val)=> {
                            this.floodPointLsit.push(val.key);
                        })
                        res.data[0].value.forEach(val => {
                            this.yListData1.push(val.value);
                            this.xListData1.push(val.key.slice(5,10));
                        });
                        res.data[1].value.forEach(val => {
                           this.yListData2.push(val.value);
                            this.xListData2.push(val.key.slice(5,10));
                        });
                        res.data[2].value.forEach(val => {
                            this.yListData3.push(val.value);
                            this.xListData3.push(val.key.slice(5,10));
                        });
                    }
                })
            },
            //内涝模型 积水点 图表
            drawChart() {
                this.$echarts.dispose(document.getElementById("echart"));
                let myChart = this.$echarts.init(document.getElementById("echart"));
                let option = {
                    grid: {
                        top: '3%',
                        left: '1%',
                        right: '1%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        data: this.xListData1,
                        axisLabel: {
                            show: true,
                            textStyle: {
                            color: '#2672B7'   //这里用参数代替了
                            }
                        }
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
                        }
                    },
                    series: [{
                        data: this.yListData1,
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
            drawChart1() {
                this.$echarts.dispose(document.getElementById("echart1"));
                let myChart = this.$echarts.init(document.getElementById("echart1"));
                let option = {
                    grid: {
                        top: '5%',
                        left: '1%',
                        right: '1%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        data: this.xListData2,
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
                        data: this.yListData2,
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
            drawChart2() {
                this.$echarts.dispose(document.getElementById("echart2"));
                let myChart = this.$echarts.init(document.getElementById("echart2"));
                let option = {
                    grid: {
                        top: '5%',
                        left: '1%',
                        right: '1%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        data: this.xListData2,
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
                        data: this.yListData3,
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
            //左侧打开按钮
            transitionDoneLeft() {
                this.showBtnLeft = true;
            },
            // 左侧关闭按钮
            toggleSideBarLeft(){
                this.showBtnLeft = false;
                this.barshowLeft = !this.barshowLeft;
            },
            //右侧打开按钮
            transitionDone(){
                this.showBtn = true;
            },
            // 右侧关闭按钮
            toggleSideBar(){
                this.showBtn = false;
                this.barshow = !this.barshow;
            },
            //内涝弹窗打开
            waterlogging() {
                this.waterloggingDialogShow = true;
            },
            //内涝弹窗关闭
            closeWaterloggingDialog() {
                this.waterloggingDialogShow = false;
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "@/style/bigScreen.scss";
    .work-waterlogging{
        position: relative;
        width: 100%;
        height: 100%;
        .weather-forecast{
            width: 380px;
            height: 126px;
            position: absolute;
            // top: 75px;
            left: 25px;
            @include flexbox;
            @include flexflow(row,nowrap);
            .weather-item{
                @include flex(1);
                font-size: 14px;
                color: #FFFFFF;
                div{
                    text-align: center;
                    &.weather-type{
                        margin: 8px 0 16px;
                        i{
                            display: inline-block;
                            width: 36px;
                            height: 36px;
                        }
                    }
                }
            }
        }
        ::v-deep.sidebar-list .el-table .cell {
            font-size: 14px;
            opacity: 0.8;
        }
        //左侧导航栏样式 
        .bigScreen-left{
            position: absolute;
            top:80px;
            .switch-btn {
                height: 862px !important;
            }
            ::v-deep.el-table__body-wrapper::-webkit-scrollbar {
                width: 0px;
            }
        }
        .hot-sty {
            background: red;
            color: #FFFFFF;
            border: 1px solid red;
            padding: 2px 3px;
            margin-right: 5px;
            font-size: 12px;
            border-radius: 12px;
        }
        //右侧导航栏样式 
        .bigScreen-right {
            .sidebar .sidebar-box {
                width: 104% !important;
                .tab-sty {
                    border-bottom: 2px solid red !important;
                }
            }
            .switch-btn {
                height: 750px !important;
            }
            
        }
        //内涝弹窗样式
        .waterlogging-sty{
            position: absolute;
            top: 135px;
            right: 40px;
        }
        //浮框样式
        .bigScreen_count {
            position: absolute;
            top: 85px;
            left: 460px;
            text-align: left;
            color: #a6dff6;
            font-size: 14px;
            font-weight: 500;
            width: 207px;
            background: transparent;
            .box {
                border: 1px solid #001D3D;
                box-shadow: inset 0 0 40px 0 rgba(63,150,251,0.24);
                width: 207px;
                height: 66px;
                background: url("../../../../assets/images/legend/background_img.png") no-repeat center;
                background-size: 100% 100%;
                padding-left: 16px;
                margin-bottom: 20px;
                .icon-sty {
                    float: left; 
                    margin-top:10px; 
                    margin-right:30px;
                }
                & > div {
                    width: 110px;
                    height: 100%;
                    display: flex;
                    flex-direction: column;
                    justify-content: space-around;
                    padding: 5px 0;
                    p {
                        font-size: 14px;
                        color: #FFFFFF;
                        font-weight: 400;
                    }
                    .num {
                        font-size: 22px;
                        color: #bafcfe;
                    }
                }
            }
        }
    }
</style>