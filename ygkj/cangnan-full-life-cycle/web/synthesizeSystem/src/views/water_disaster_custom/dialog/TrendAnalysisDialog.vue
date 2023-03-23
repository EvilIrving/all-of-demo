<template>
  <div :class="['primary-dialog', leftSide == true ? 'project-dialog-wrap' : '']" >
    <el-dialog :visible.sync="visible" :width="width" :before-close="handleClose" destroy-on-close style="z-index:3000;" :modal-append-to-body="true" :close-on-click-modal="false">
      <!-- 标题 -->
      <div class="primary-dialog__title" slot="title" :style="minimizeStatus ? 'justify-content: space-between;' : ''" >
        <p class="primary-dialog__title--label" style="color: #fff;">趋势分析图</p>
      </div>
      <!-- 内容 -->
      <div class="primary-dialog__content">
        <el-row>
          <el-col :span="24">
            <div class="results-show-sty">
              <div class="card-sty">
                <p style="margin-right:15px;">
                  <i class="icon-cheng-guo-shu" />
                </p>
                <div style="margin-right:20px;">
                  <p>研究成果总数</p>
                  <p style="margin-top:10px;">
                    <span style="font-size: 24px;color: #A80BEE;"> {{ overviewList.achievementNums }} </span> 个
                  </p>
                </div>
                <p style="margin-right:15px;">
                  <i class="icon-yajiu-jingfei" />
                </p>
                <div>
                  <p>研究经费总计</p>
                  <p style="margin-top:10px;">
                    <span style="font-size: 24px;color: #56FEFE;"> {{ overviewList.totalResearchFund }} </span> 万元
                  </p>
                </div>
              </div>
            </div>
            <!-- 中间卡片 -->
            <el-row style="margin:15px 0;">
              <el-col :span="5">
                <span style="color: #FFFFFF;">研究类型：</span>
                <el-select v-model="researchTypeId" multiple collapse-tags placeholder="请选择" @change="researchChange(researchTypeId)">
                  <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-col>
            </el-row>
            <!-- 列表 -->
             <el-table
              ref="multipleTable"
              :data="realTimeData"
              height="100%" stripe
              v-loading="loading" width="100%"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
            >
              <el-table-column prop="index" label="序号" width="55">
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="researchName" label="研究名称" />
              <el-table-column prop="particularYear" label="年份" width="100" />
              <el-table-column :show-overflow-tooltip="true" prop="personLiable" label="负责人" width="100" />
              <el-table-column :show-overflow-tooltip="true" prop="undertakUnit" label="承担单位" width="160" />
              <el-table-column prop="photoUrl" label="成果图片">
                <template slot-scope="scope">
                  <i class="el-icon-picture-outline" style="color:#328173;font-size: 20px;" @click="photograph(scope.row)" />
                </template>
              </el-table-column>
              <el-table-column prop="fileUrl" label="研究报告">
                <template slot-scope="scope">
                  <i class="el-icon-paperclip" style="color:#328173;font-size: 20px;" @click="fileDialogShow(scope.row.fileUrl)" />
                </template>
              </el-table-column>
              <el-table-column prop="researchResult" label="研究成果转化">
                <template slot-scope="scope">
                  <el-popover placement="top-start" width="270" trigger="hover">
                    <template>
                      <div>
                        <div style="padding: 10px 5px; background: #d3dddb;display: flex;justify-content: left;">
                          <p style="margin-right:10px;width: 30px;">序号</p>
                          <p style="margin-right:10px;width: 30px;">类型</p>
                          <p style="margin-right:10px;width: 120px;">名称</p>
                          <span>附件</span>
                        </div>
                        <div>
                          <div style="padding: 10px;display: flex;justify-content: left;" v-for="(item, index) in scope.row.achRespVoList" :key="index">
                            <p style="margin-right:10px;width: 30px;">{{index + 1}}</p>
                            <p style="margin-right:10px;width: 30px;">{{item.type}}</p>
                            <p style="margin-right:10px;width: 120px;">{{item.name}}</p>
                            <p>
                              <i class="el-icon-paperclip" style="color:#328173;font-size: 20px;" @click="fileDialog(item.url)" />
                            </p>
                          </div>
                        </div>
                      </div>
                    </template>
                    <span slot="reference" style="color:#328173;">转化列表</span>
                  </el-popover>
                </template>
              </el-table-column>
            </el-table>
            <!-- 分页 -->
            <div class="pagination-area">
              <el-pagination class="right" @current-change="pageChange" background layout="total, prev, pager, next" :total="total" />
            </div>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  // import * as echarts from "echarts";
  // import { TableTitle } from "../cards/CardPages/components.js";
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
        default: "1600px",
      },
      leftSide: {
        type: Boolean,
        default: false,
      },
      minimizeStatus: {
        type: Boolean,
        default: false,
      }
    },
    data() {
      return {
        total: 0,
        pageNum: 1,
        pageSize: 10,




        // colorList: ['#1EF8F2','#19AF47','#008FE8','#008FE8','#FF7144','#8A8AE1','#A80BEE'],
        researchTypeId: ['-1'],
        // endYear: '2021',
        // startYear: '2019',
        // switchNum: 1,
        // options: [],
        // overviewList: {},
        // yearData: [],
        // overviewData: [],
        // resultList: [],
        // titleName: '个',
        // legendName: [],
        // typeList: [
        //   {
        //     type: 'value',
        //     name: this.titleName,
        //     interval: this.titleName == '个' ? '2' : '500',
        //     axisLine: {
        //       show: false,
        //     },
        //     axisTick: {
        //       show: false
        //     },
        //     axisLabel: {
        //       formatter: '{value}',
        //       textStyle: {
        //         color: "#fff"
        //       }
        //     }
        //   },
        //   {
        //     type: 'value',
        //     name: this.titleName == '个' ? '%' : '',
        //     min: 0,
        //     max: 100,
        //     interval: 20,
        //     axisLine: {
        //       show: false,
        //     },
        //     axisTick: {
        //       show: false
        //     },
        //     axisLabel: {
        //       formatter: '{value}',
        //       textStyle: {
        //         color: "#fff"
        //       }
        //     },
        //   }
        // ]
      }
    },
    // components: {TableTitle},
    mounted() {
      this.$nextTick(()=> {
        this.busSubLoadPage();
        // this.histogram();
        // this.loadPage();
        // this.trendAchievementAnalysis();      // 成果分析
        // this.trendOverviewAchievements();     //成果总览
      })
    },
    methods: {
      // 研究类别
      researchChange(val) {
        console.log(5555,val);
        // if (this.switchNum == 1) {
        //   this.trendAchievementAnalysis();      // 成果分析
        // }else {
        //   this.trendExpenditureAnalysis();
        // }
        // this.trendOverviewAchievements();     //成果总览
      },
      // 分页
      pageChange(page) {
        this.pageNum = page;
        this.busSubLoadPage();
      },
      //项目列表
      async busSubLoadPage() {
        let obj = {
          researchType: this.projecList.id,
          pageNum: this.pageNum,              
          pageSize: this.pageSize,            
          researchName: this.planName,            //规划信息名称
        }
        await programmeApi.busSubLoadPage(obj).then((res) => {
          if (res.code == 0) {
            this.total = res.data.totalRows;
            this.tableData = res.data.list;
          }
        })
      },





      // // 
      // dateList() {
      //   if (this.endYear == null || this.endYear == '') {
      //     this.$message({ message: '请选择终止年份！', type: 'warning' });
      //   }else if (this.startYear == null || this.startYear == '') {
      //     this.$message({ message: '请选择起始年份！', type: 'warning' });
      //   }else {
      //     this.trendOverviewAchievements();     //成果总览
      //     if (this.switchNum == 1) {
      //       this.trendAchievementAnalysis();      // 成果分析
      //     }else {
      //       this.trendExpenditureAnalysis();
      //     }
      //   }
      // },
      // // 趋势分析 -- 成果分析
      // async trendOverviewAchievements() {
      //   this.overviewList = {};
      //   let obj = {
      //     endYear: this.endYear,
      //     startYear: this.startYear,
      //     researchTypeId: this.researchTypeId.join(','),
      //   }
      //   await programmeApi.trendOverviewAchievements(obj).then((res) => {
      //     if (res.code == 0) {
      //       this.overviewList = res.data;
      //     }
      //   })
      // },
      // // 经费分析
      //  async trendExpenditureAnalysis() {
      //    this.overviewData = [];
      //   this.overviewData = [];
      //   this.histogram();
      //   this.resultList = [];
      //   let obj = {
      //     endYear: this.endYear,
      //     startYear: this.startYear,
      //     researchTypeId: this.researchTypeId.join(','),
      //   }
      //   await programmeApi.trendExpenditureAnalysis(obj).then((res) => {
      //     if (res.code == 0) {
      //       this.yearData = res.data.years;
      //       let arr = res.data.data.filter((val) => val.type == 'bar');
      //       for (let index = 0; index < arr.length; index++) {
      //         arr[index].barWidth = 25;
      //         arr[index].name = arr[index].name == '全部' ? '全部研究成果数' : arr[index].name + '研究成果数';
      //         arr[index].color = this.colorList[index];
      //       }
      //       this.overviewData.push(...arr);
      //        this.overviewData.forEach((val)=>{
      //         this.legendName.push(val.name);
      //       })
      //       this.resultList = arr;
      //       this.$nextTick(() => {
      //         this.histogram();
      //       })
      //     }
      //   })
      // },
      // // 成果分析
      // async trendAchievementAnalysis() {
      //   this.legendName = [];
      //   this.overviewData = [];
      //   this.resultList = [];
      //   let obj = {
      //     endYear: this.endYear,
      //     startYear: this.startYear,
      //     researchTypeId: this.researchTypeId.join(','),
      //   }
      //   await programmeApi.trendAchievementAnalysis(obj).then((res) => {
      //     if (res.code == 0) {
      //       this.yearData = res.data.years;
      //       let arr = res.data.data.filter((val) => val.type == 'bar');
      //       let str = res.data.data.filter((val) => val.type == 'line');
      //       for (let index = 0; index < arr.length; index++) {
      //         arr[index].barWidth = 25;
      //         arr[index].name = arr[index].name == '全部' ? '全部研究成果数' : arr[index].name + '研究成果数';
      //         arr[index].color = this.colorList[index];
      //       }
      //       for (let index = 0; index < str.length; index++) {
      //         str[index].color = this.colorList[index];
      //         str[index].name = str[index].name == '全部' ? '全部研究成果转化率(%)' : str[index].name + '研究成果转化率(%)';
      //         str[index].yAxisIndex = 1;
      //       }
      //       this.overviewData.push(...arr,...str);
      //       this.overviewData.forEach((val)=>{
      //         this.legendName.push(val.name);
      //       })
      //       this.resultList = arr;
      //       this.$nextTick(() => {
      //         this.histogram();
      //       })
      //     }
      //   })
      // },
      // // 研究类别
      // async loadPage() {
      //   await programmeApi.loadPage().then((res) => {
      //     if (res.code == 0) {
      //       let str = {
      //         id: '-1',
      //         name: '全部'
      //       }
      //       res.data.list.unshift(str);
      //       this.options = res.data.list;
      //     }
      //   })
      // },
    
      // // 成果
      // achievement() {
      //   this.titleName = '个';
      //   this.switchNum = 1;
      //   this.typeList = [
      //       {
      //         type: 'value',
      //         name: this.titleName,
      //         // min: 0,
      //         // max: 250,
      //         nameTextStyle:{
      //             color:"#fff", 
      //         },

      //         interval: this.titleName == '个' ? '2' : '500',
      //         axisLine: {
      //           show: false,
      //         },
      //         axisTick: {
      //           show: false
      //         },
      //         axisLabel: {
      //           formatter: '{value}',
      //           textStyle:{
      //             // fontSize: 18,//字体大小
      //             color: '#ffffff'//字体颜色
      //           },
      //         }
      //       },
      //       {
      //         type: 'value',
      //         name: this.titleName == '个' ? '%' : '',
      //         min: 0,
      //         max: 100,
      //         nameTextStyle:{
      //             color:"#fff", 
      //         },
      //         interval: 20,
      //         axisLine: {
      //           show: false,
      //         },
      //         axisTick: {
      //           show: false
      //         },
      //         axisLabel: {
      //           formatter: '{value}',
      //            textStyle:{
      //             // fontSize: 18,//字体大小
      //             color: '#ffffff'//字体颜色
      //           },
      //         }
      //       }
      //     ],
      //   this.trendAchievementAnalysis();
      // },
      // // 经费
      // expenditure() {
      //   this.titleName = '万元';
      //   this.switchNum = 2;
      //   this.typeList = [
      //       {
      //         type: 'value',
      //         color: '#fff',
      //         name: this.titleName,
      //         // min: 0,
      //         // max: 250,
      //         nameTextStyle:{
      //             color:"#fff", 
      //         },
      //         interval: this.titleName == '个' ? '2' : '500',
      //         axisLine: {
      //           show: false,
      //         },
      //         axisTick: {
      //           show: false
      //         },
      //         axisLabel: {
      //           formatter: '{value}',
      //            textStyle:{
      //             // fontSize: 18,//字体大小
      //             color: '#ffffff'//字体颜色
      //           },
      //         }
      //       }
      //     ],
      //   this.trendExpenditureAnalysis();
      // },
      // // 柱状，折线图
      // histogram() {
      //   var chartDom = document.getElementById('histogramChart');
      //   var myChart = echarts.init(chartDom);
      //   var option;
      //   option = {
      //     // title: {
      //     //   text: this.titleName,
      //     //   top: '-5px'
      //     // },
      //     tooltip: {
      //       trigger: 'axis',
      //       axisPointer: {
      //         type: 'cross',
      //         crossStyle: {
      //           color: '#fff'
      //         }
      //       }
      //     },
      //     legend: {
      //         top: '5px',
      //         right: '0',
      //         textStyle:{
      //             // fontSize: 18,//字体大小
      //             color: '#ffffff'//字体颜色
      //         },
      //         data: this.legendName

      //     },
      //     grid: {
      //       top: '25%',
      //       left: '0',
      //       right: '0',
      //       bottom: '0',
      //       containLabel: true
      //     },
      //     xAxis: [
      //       {
      //         type: 'category',
      //         color: '#fff',
      //         data: this.yearData,
      //         axisPointer: {
      //           type: 'shadow'
      //         },
      //         axisLine: {
      //           show: false,
      //         },
      //         axisTick: {
      //           show: false
      //         },
      //         axisLabel: {
      //           show: true,
      //           textStyle: {
      //             color: '#fff'
      //           }
      //         },
      //       }
      //     ],
      //     yAxis: this.typeList,
      //     series: this.overviewData
      //   };
      //   option && myChart.setOption(option, true);
      // },





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
        padding:0 15px;
        height: 630px;
        overflow-y: auto;
        overflow-x: hidden;
        // border: 1px solid #00c1ff;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
        border-top: 0;
        .results-show-sty {
          margin-top: 10px;
          display: flex;
          padding-bottom: 10px;
          border-bottom: 1px solid rgba($color: #00C1FF, $alpha: 0.2);
          justify-content: space-between;
          >p:nth-child(1) {
            margin-top: 10px;
            width: 200px;
            height: 26px;
            font-family: PingFangSC-Semibold;
            font-size: 24px;
            color: #328173;
            letter-spacing: 0;
            font-weight: 600;
            padding-left: 10px;
            border-left: 4px solid #328173;
          }
          > div {
            display: flex;
            justify-content: space-between;
            > p{
              display: flex;
              margin-left: 10px;
              justify-content: space-between;
              > span {
                width: 100px;
                height: 44px;
                line-height: 44px;
              }
            }
            .el-input__inner {
              color: #333333;
              border: 0;
              height: 40px;
              background: #DEE3E4;
              border-radius: 4px;
            }
          }
        }
        .orientation {
          position: absolute;
          right:20px;
          top: 145px;
        }
        .card-sty {
          > div {
            color:#fff;
          }
          // padding: 20px 10px 0 10px;
          // display: flex;
          // justify-content: space-between;
          // >div:nth-child(1) {
          //   // width: 475px;
          //   // height: 151.56px;
          //   // background: url(../../../../assets/images/research-result.png) no-repeat;
          //   background-size: 100% 100%;
          //   padding: 45px 0 0 150px;
          //   > p {
          //     height: 28px;
          //     font-family: PingFangSC-Regular;
          //     font-size: 20px;
          //     color: #FFFFFF;
          //     letter-spacing: 0;
          //     font-weight: 400;
          //   }
          //   > p:nth-child(2) {
          //     > span {
          //       font-size: 40px;
          //     }
          //   }
          // }
          // >div:nth-child(2) {
          //   width: 475px;
          //   height: 151.56px;
          //   // background: url(../../../../assets/images/the-document.png) no-repeat;
          //   background-size: 100% 100%;
          //   padding: 45px 0 0 150px;
          //   > p {
          //     height: 28px;
          //     font-family: PingFangSC-Regular;
          //     font-size: 20px;
          //     color: #FFFFFF;
          //     letter-spacing: 0;
          //     font-weight: 400;
          //   }
          //   > p:nth-child(2) {
          //     > span {
          //       font-size: 40px;
          //     }
          //   }
          // }
        }
        .tab-sty {
          color: #fff;
          height: 26px;
          font-weight: 500;
          margin-right: 20px;
          padding-bottom: 5px;
        }
        .switch-sty {
          height: 26px;
          font-weight: 500;
          margin-right: 20px;
          padding-bottom: 5px;
          color: #56FEFE;
          border-bottom: 2px solid #56FEFE;
        }
        .test-sty {
          width: 20px;
          height: 10px;
          display: inline-block;
          margin-right: 10px;
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
  // background: url(~assets/images/peripheral_analysis.png) no-repeat;
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