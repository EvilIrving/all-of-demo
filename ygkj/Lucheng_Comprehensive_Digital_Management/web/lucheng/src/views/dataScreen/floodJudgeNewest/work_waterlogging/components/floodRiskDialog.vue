<template>
    <div class="sidebar-box">
        <div class="sidebar-top">
            <div class="right-tab">
                <template>
                    <span v-for="(name, index) in sidebarTabArr" :key="index" @click="sidebarTabChange(name)" :class="activeSidebar == name ? 'active' : ''">{{ name }}</span>
                </template>
            </div>
            <p class="titleSty" @click="getBack">设计洪水<span class="sizeColor"> 4 </span>场 <span class="right-arrows"></span> <span>{{titleData}}</span></p>
            <div class="sidebar-list">
                <!-- 列表数据 -->
                <div v-if="!listDetails">
                    <el-input class="ipnSty" placeholder="输入关键字进行查询" v-model="input" clearable>
                        <i slot="prefix" class="el-input__icon el-icon-search"></i>
                    </el-input>
                    <p v-for="(val,index) in tableDataList" :key="index" class="listSty" @click="listData(val.name)">
                        <span class="table-order">{{ val.id }}</span>
                        <span style="padding-left: 10px;">{{val.name}}</span>
                        <span style="float: right;" class="right-arrows"></span>
                    </p>
                </div>
                <!-- 详情 -->
                <div v-else>
                    <div class="right-tab">
                        <template>
                            <span class="active">灾情统计</span>
                            <el-button type="primary" size="mini" style="float: right;" @click="getBack">返回</el-button>
                        </template>
                    </div>
                    <div style="padding: 20px 0;">
                        <p><span style="margin-right: 10px;" class="beijing"></span>说明</p>
                        <p class="contentSty">暂无说明</p>
                    </div>
                    <div style="padding: 20px 0;">
                        <p><span style="margin-right: 10px;" class="beijing"></span>风险信息统计</p>
                        <p class="contentSty"> 淹没面积：<span class="sizeColor"> 351.33 </span> k㎡ </p>
                        <p class="contentSty"> 影响人口：<span class="sizeColor"> 60.12 </span> 万人 </p>
                        <p class="contentSty"> 淹没区洪水损失：<span class="sizeColor"> 793365.5 </span> 万元 </p>
                    </div>
                    <!-- 按钮 -->
                    <div style="padding:10px 0 0 43px;">
                        <el-button type="primary" size="small" style="width: 300px;">淹没水深图</el-button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { waterApi, rainApi } from "@/api/dataScreen/floodJudge/index";
    export default {
        name: "floodRiskDialog",
        data() {
            return {
                sidebarTabArr: ["全市一张图管理", "流域一张图管理", "片区风险图管理"],
                activeSidebar: "全市一张图管理",
                titleData: '',
                listDetails: false,
                input: '',
                tableDataList: [
                    {
                        id: "1",
                        name: "温州市5年一遇设计洪水",
                    }, {
                        id: "1",
                        name: "温州市10年一遇设计洪水",
                    }, {
                        id: "2",
                        name: "温州市15年一遇设计洪水",
                    }, {
                        id: "3",
                        name: "温州市20年一遇设计洪水",
                    }, {
                        id: "4",
                        name: "温州市25年一遇设计洪水",
                    }, {
                        id: "5",
                        name: "温州市30年一遇设计洪水",
                    }, {
                        id: "6",
                        name: "温州市35年一遇设计洪水",
                    }, {
                        id: "7",
                        name: "温州市40年一遇设计洪水",
                    }, {
                        id: "1",
                        name: "温州市45年一遇设计洪水",
                    }, {
                        id: "8",
                        name: "温州市5年一遇设计洪水",
                    }, {
                        id: "9",
                        name: "温州市50年一遇设计洪水",
                    }, {
                        id: "10",
                        name: "温州市55年一遇设计洪水",
                    }
                ]
            }
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
        .right-tab {
            > span {
                display: inline-block;
                width: 120px;
                height: 40px;
                line-height: 40px;
                text-align: center;
                cursor: pointer;
                color: #4898fb;
                background: url(../../../../../assets/images/bigScreen-big-tab.png) no-repeat;
                background-size: 100% 100%;
                margin-right: 6px;
                &.active {
                    color: #a6e9fb;
                    background: url(../../../../../assets/images/bigScreen-big-tab-check.png) no-repeat;
                    background-size: 100% 100%;
                }
            }
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
</style>