<template>
    <el-dialog :visible.sync="dialogVisible" width="700px" :before-close="handleClose" top="10vh" title="一键研判" class="dispatch-dialog" :close-on-click-modal="false">
        <div class="dialog-content">
            <!-- 雨情 -->
            <p class="rainTitle">【雨情】</p>
            <div style="padding: 15px 30px;">
                <!-- 一小时雨情 -->
                <el-collapse>
                    <el-collapse-item>
                        <template slot="title">
                            <span style="color:#fff;">1小时累计雨量，超警的有 
                                <span class="redSty"> {{rainText.oneHourWarningSum}} </span>个
                            </span>
                        </template>
                        <div class="titleContentSty">
                            <p class="stripSty" v-for="(item,index) in rainText.oneHourWarningInfo" :key="index">
                                <span> {{index + 1}} </span>. <span> {{ item.name }}</span> - <span> {{ item.time }} </span> ,
                                1h累计降雨量 <span> {{ item.value }} </span> mm 
                                ( 警戒雨量 <span> {{ item.threshold }} </span> mm ) ，
                                超警戒雨量 <span> {{ item.overThreshold }} </span> mm
                            </p>
                        </div>
                    </el-collapse-item>
                </el-collapse>
                <!-- 三小时雨情 -->
                <el-collapse>
                    <el-collapse-item>
                        <template slot="title">
                            <span style="color:#fff;">3小时累计雨量，超警的有
                                <span class="redSty"> {{rainText.threeHourWarningSum}} </span>个
                            </span>
                        </template>
                        <div class="titleContentSty">
                            <p class="stripSty" v-for="(item,index) in rainText.threeHourWarningInfo" :key="index">
                                <span> {{index + 1}} </span>. <span>{{ item.name }}</span> - <span> {{ item.time }} </span>, 
                                3h累计降雨量 <span> {{ item.value }} </span> mm 
                                ( 警戒雨量 <span> {{ item.threshold }} </span> mm ) ，
                                超警戒雨量 <span> {{ item.overThreshold }} </span> mm
                            </p>
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </div>
            <!-- 江河水情 -->
            <p class="rainTitle">【江河水情】</p>
            <div style="padding: 15px 30px;">
                <el-collapse>
                    <el-collapse-item>
                        <template slot="title">
                            <span style="color:#fff;">全省主要江河控制站，超警的有
                                <span class="redSty"> {{rainText.riverWarnSum}} </span>个
                            </span>
                        </template>
                        <div class="titleContentSty">
                            <p class="stripSty" v-for="(item,index) in rainText.riverWarnInfo" :key="index">
                                <span> {{index + 1}} </span>. <span> {{ item.name }} </span> - <span> {{ item.time }} </span>，水位
                                <span> {{ item.value }} </span> m ( 警戒水位 <span> {{ item.threshold }} </span> m )， 超汛限
                                <span> {{ item.overThreshold }}  </span> m
                            </p>
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </div>
            <!-- 大中型水库水情 -->
            <p class="rainTitle">【大中型水库水情】</p>
            <div style="padding: 15px 30px;">
                <el-collapse>
                    <el-collapse-item>
                        <template slot="title">
                            <span style="color:#fff;">全省大中型水客，超警的有
                                <span class="redSty"> {{rainText.largeMidResWarnSum}} </span>个
                            </span>
                        </template>
                        <div class="titleContentSty">
                            <p class="stripSty" v-for="(item,index) in rainText.largeMidResWarnInfo" :key="index">
                                <span> {{index + 1}} </span>. <span>{{ item.name }}</span> - <span> {{ item.time }} </span>，水位
                                <span> {{ item.value }} </span> m ( 警戒水位 <span> {{ item.threshold }} </span> m )， 超汛限
                                <span> {{ item.overThreshold }}  </span> m
                            </p>
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </div>
            <!-- 积水 -->
            <p class="rainTitle">【积水】</p>
            <div style="padding: 15px 30px;">
                <el-collapse>
                    <el-collapse-item>
                        <template slot="title">
                            <span  style="color:#fff;">全市积水点,出现积水的有
                                <span class="redSty"> {{rainText.floodPointWarnSum}} </span>个
                            </span>
                        </template>
                        <div class="titleContentSty">
                            <p class="stripSty" v-for="(item,index) in rainText.floodPointWarnInfo" :key="index">
                                <span> {{index + 1}} </span>. <span>{{ item.name }}</span> - <span> {{ item.time }} </span>，积水深 <span> {{ item.value }} </span> m 
                            </p>
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </div>
            <!-- 山洪 -->
            <p class="rainTitle">【山洪】</p>
            <div style="padding: 15px 30px;">
                <el-collapse>
                    <el-collapse-item>
                        <template slot="title">
                            <span style="color:#fff;">全市受影响乡镇的有
                                <span class="redSty"> {{rainText.oneHourWarningSum}} </span>个
                            </span>
                        </template>
                        <div class="titleContentSty">
                            <p class="stripSty" v-for="(item,index) in rainText.largeMidResWarnInfo" :key="index">
                                <span> {{index + 1}} </span>. <span>{{ item.resName }}</span> - 受影响可能性较大的村落有 <span> {{ item.time }} </span>个，
                                可能性较小的村落有 <span> {{ item.waterLevel }} </span> 个，
                                影响人口 <span> {{ item.waterLevel }} </span> 人。
                            </p>
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </div>
            <!-- 海塘 -->
            <p class="rainTitle">【海塘】</p>
            <div style="padding: 15px 30px;">
                <el-collapse>
                    <el-collapse-item>
                        <template slot="title">
                            <span style="color:#fff;">全市受影响的高风险海塘的有
                                <span class="redSty"> {{rainText.oneHourWarningSum}} </span>km
                            </span>
                        </template>
                        <div class="titleContentSty">
                            <p class="stripSty" v-for="(item,index) in rainText.largeMidResWarnInfo" :key="index">
                                <span> {{index + 1}} </span>. <span>{{ item.resName }}</span> - 
                                <span> {{ item.tm }} </span>海塘坝顶高程 <span> {{ item.time }} </span>m，
                                潮位 <span> {{ item.waterLevel }} </span> m，
                                坝顶潮位高差小于 <span> {{ item.waterLevel }} </span> m ;
                            </p>
                        </div>
                    </el-collapse-item>
                </el-collapse>
                <el-collapse>
                    <el-collapse-item>
                        <template slot="title">
                            <span style="color:#fff;">全市受影响的高风险海塘的有
                                <span class="redSty"> {{rainText.oneHourWarningSum}} </span>km
                            </span>
                        </template>
                        <div class="titleContentSty">
                            <p class="stripSty" v-for="(item,index) in rainText.largeMidResWarnInfo" :key="index">
                                <span> {{index + 1}} </span>. <span>{{ item.resName }}</span> - 
                                <span> {{ item.tm }} </span>海塘坝顶高程 <span> {{ item.time }} </span>m，
                                潮位 <span> {{ item.waterLevel }} </span> m，
                                坝顶潮位高差小于 <span> {{ item.waterLevel }} </span> m ;
                            </p>
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </div>
        </div> 
        <!-- 尾部取消 -->
        <div slot="footer">
            <div class="form-footer">
                <el-button class="form-btn btn-save" @click="handleClose">取消</el-button>
            </div>
        </div>
    </el-dialog>
</template>

<script>
    import { dispatchApi } from "@/api/dataScreen/rescueSupport/dispatch.js";
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
                contentShow: false,
                rainText: [],
            }
        },
        mounted() {
            this.$nextTick(()=> {
                this.warnStatsAll();
            })
        },
        methods: {
            //取消按钮
            handleClose() {
            this.$emit("handleClose");
            },
            //标题事件
            async warnStatsAll() {
                await dispatchApi.warnStatsAll().then((res) => {
                    if (res.code == 0) {
                        this.rainText = res.data;
                    }
                })
            }
        }
    }
</script>

<style lang="scss">
    .titleContentSty {
        width: 100%;
        color:#fff; 
        padding: 10px; 
        opacity: 0.7;
        height: 150px;
        overflow: auto;
    }
    .stripSty {
        width:700px;
        overflow-x: auto;
        padding:5px 0;
    }
    .rainTitle {
        color: #239def; 
        margin-left: 20px; 
        font-size: 16px;
    }
    .dispatch-dialog {
        user-select: none;
        .el-dialog {
            .dialog-content {
                height: 630px;
                overflow: auto;
            }
        }
    }
    .redSty {
        color: red; 
        font-size: 18px;
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
</style>