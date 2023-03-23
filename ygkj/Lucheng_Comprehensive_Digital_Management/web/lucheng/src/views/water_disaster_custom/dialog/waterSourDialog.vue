<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2021-08-13 10:41:44
 * @LastEditors: ysl
 * @Description: 水源供水落点弹窗
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/dialog/waterSourDialog.vue
-->
<template>
  <div class="detail-table">
    <el-dialog
      :width="obj.type == '水源保障' ? '619px' : '1200px'"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label"> {{obj.type == '水源保障' ? '水源地概况' : obj.type == '水源保障' ? '水厂运行状态' : obj.adnm}}</p>
      </div>
      <!-- 水源保障 内容区域 -->
      <div v-if="obj.type == '水源保障'" class="primary-dialog__content">
        <StatisticsTabs
					v-if="obj.type == '水源保障'"
					:active.sync="activeTabBeauty"
					:tabs="['基本信息', '标识标牌']"
					@change="changeActiveTabBeauty"
				/>
        <div v-if="activeTabBeauty == '基本信息'">
          <div class="img-panel">
            <img src="@/assets/images/shuiku-linshi.png" alt="" />
          </div>
          <div class="info-panel">
            <el-row :gutter="20">
              <el-col :span="12">
                <span class="name">水源地名称</span>
                <span class="value">{{ obj.name }}</span>
              </el-col>
              <el-col :span="12">
                <span class="name">类型</span>
                <span class="value">{{ obj._type }}</span>
              </el-col>
              <el-col :span="12">
                <span class="name">规模</span>
                <span class="value">{{ obj.scale }}</span>
              </el-col>
              <el-col :span="12">
                <span class="name">所在市县</span>
                <span class="value">{{ obj.area }}</span>
              </el-col>
              <el-col :span="12">
                <span class="name">是否划定保护区</span>
                <span class="value">{{ obj.isP }}</span>
              </el-col>
              <el-col :span="12">
                <span class="name">目标水质</span>
                <span class="value">{{ obj.target }}</span>
              </el-col>
              <el-col :span="12">
                <span class="name">水量预警</span>
                <span class="value" :style="{ color: setWaterWarnColor }">{{
                  obj.warn
                }}</span>
              </el-col>
            </el-row>
          </div>
        </div>
        <div v-else>
          <el-table ref="multipleTable" :data="tableList" height="350px" stripe width="100%">
              <el-table-column prop="index" label="序号" width="80">
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="a" label="名称" />
              <el-table-column :show-overflow-tooltip="true" prop="b" label="类型" />
              <el-table-column :show-overflow-tooltip="true" prop="c" label="负责人" />
              <el-table-column label="相关图片">
                <template slot-scope="scope">
                  <i class="el-icon-paperclip" style="color:#00c1ff;cursor: pointer;" @click="pictureUrl(scope.row.d)" />
                </template>
              </el-table-column>
            </el-table>
        </div>
      </div>
      <!-- 水厂 内容区域 -->
      <div v-if="obj.type == '水厂'" class="primary-dialog__content">
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- 表头 -->
            <div style="display: flex; justify-content: space-between;">
              <p>
                <i class="icon-diamonds" style="margin-right: 10px; cursor: pointer;vertical-align: middle;"/>
                <span style="line-height:20px;height:20px;color:#fff;">工程图片</span>
              </p>
              <p>
                <span class="panorama-sty" style="margin-right:10px;font-size: 18px;">全景图</span>
                <span class="panorama-style">全景图</span>
              </p>
            </div>
            <i class="icon-card-title-divide" />
            <!-- 图片展示 -->
            <div class="img-panel" style=" margin-top: 10px; height: 400px;">
              <img :src="obj.src" alt="" />
            </div>
          </el-col>
          <el-col :span="12">
            <!-- 基本数据 -->
            <el-row :gutter="20">
              <!-- 表头 -->
              <div>
                <p>
                  <i class="icon-diamonds" style="margin-right: 10px; cursor: pointer;vertical-align: middle;"/>
                  <span style="line-height:20px;height:20px;color:#fff;font-size: 18px;">基本信息</span>
                </p>
                <i class="icon-card-title-divide" />
              </div>
              <el-col :span="8" style="margin: 10px 0;">
                <span class="name-sty">水厂（站）名称</span>
                <span class="value-sty">{{ obj.cwsName || '-' }}</span>
              </el-col>
              <el-col :span="8" style="margin: 10px 0;">
                <span class="name-sty">所在市县</span>
                <span class="value-sty">{{ obj.adnm || '-'  }}</span>
              </el-col>
              <el-col :span="8" style="margin: 10px 0;">
                <span class="name-sty">水厂（站）位置</span>
                <span class="value-sty">{{ obj.cwsLoc || '-'  }}</span>
              </el-col>
              <el-col :span="8" style="margin: 10px 0;">
                <span class="name-sty">完工时间（年）</span>
                <span class="value-sty">{{ obj.compDate || '-'  }}</span>
              </el-col>
              <el-col :span="8" style="margin: 10px 0;">
                <span class="name-sty">消毒工艺</span>
                <span class="value-sty">{{ obj.dfcProc || '-'  }}</span>
              </el-col>
              <el-col :span="8" style="margin: 10px 0;">
                <span class="name-sty">覆盖人口（人）</span>
                <span class="value-sty">{{ obj.desWasuPop || '-' }}</span>
              </el-col>
              <el-col :span="8" style="margin: 10px 0;">
                <span class="name-sty">实际供水规模（吨/天）</span>
                <span class="value-sty">{{ obj.actWasuScal || '-'  }}</span>
              </el-col>
              <el-col :span="8" style="margin: 10px 0;">
                <span class="name-sty">处理工艺</span>
                <span class="value-sty">{{ obj.treProc || '-' }}</span>
              </el-col>
              <el-col :span="8" style="margin: 10px 0;">
                <span class="name-sty">设计供水规模（吨/天）</span>
                <span class="value-sty">{{ obj.desWasuScal || '-' }}</span>
              </el-col>
              
            </el-row>
            <!-- 水源地信息 -->
            <el-row :gutter="20">
              <!-- 表头 -->
              <div>
                <p>
                  <i class="icon-diamonds" style="margin-right: 10px; cursor: pointer;vertical-align: middle;"/>
                  <span style="line-height:20px;height:20px;color:#fff;font-size: 18px;">水源地信息</span>
                </p>
                <i class="icon-card-title-divide" />
              </div>
              <el-col :span="8" style="margin: 10px 0;">
                <span class="name-sty">主水源名称</span>
                <span class="value-sty">{{ '-' }}</span>
              </el-col>
              <el-col :span="8" style="margin: 10px 0;">
                <span class="name-sty">水源类型</span>
                <span class="value-sty">{{ '-' }}</span>
              </el-col>
              <el-col :span="8" style="margin: 10px 0;">
                <span class="name-sty">水源地规模</span>
                <span class="value-sty">{{ '-' }}</span>
              </el-col>
            </el-row>
            <!-- 运行管理单位 -->
            <el-row :gutter="20">
              <!-- 表头 -->
              <div>
                <p>
                  <i class="icon-diamonds" style="margin-right: 10px; cursor: pointer;vertical-align: middle;"/>
                  <span style="line-height:20px;height:20px;color:#fff;font-size: 18px;">运行管理单位</span>
                </p>
                <i class="icon-card-title-divide" />
              </div>
              <el-col :span="12" style="margin: 10px 0;">
                <span class="name-sty">名称</span>
                <span class="value-sty">{{ '-' }}</span>
              </el-col>
              <el-col :span="12" style="margin: 10px 0;">
                <span class="name-sty">地址</span>
                <span class="value-sty">{{ '-' }}</span>
              </el-col>
              <el-col :span="12" style="margin: 10px 0;">
                <span class="name-sty">责任人</span>
                <span class="value-sty">{{ '-' }}</span>
              </el-col>
              <el-col :span="12" style="margin: 10px 0;">
                <span class="name-sty">责任人联系电话</span>
                <span class="value-sty">{{ '-' }}</span>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>
      <!-- 水厂运行状态 -->
      <div v-if="obj.type == '水厂运行状态'" class="primary-dialog__content">
        <!-- tab切换 -->
        <div style="margin-bottom:20px;">
          <span :class=" waterState == '0' ? 'real-time-sty' : 'real-time-style'" @click="waterQuality('0')" style="margin-right:10px;">实时水质</span>
          <span :class=" waterState == '1' ? 'real-time-sty' : 'real-time-style'" @click="waterQuality('1')">实时水量</span>
        </div>
        <!-- 表头 -->
        <div>
          <p>
            <i class="icon-diamonds" style="margin-right: 10px; cursor: pointer;vertical-align: middle;"/>
            <span style="line-height:20px;height:20px;color:#fff;font-size: 18px;">
              {{ waterState == '0' ? '水质实时监测指标' : '供水情况' }}
            </span>
          </p>
          <i class="icon-card-title-divide" />
        </div>
        <!-- 水质实时监测指标 内容区域 -->
        <div class="monitoring-sty" v-if="waterState == '0'">
          <el-row>
            <el-col :span="8" class="ph-sty">
              <p></p>
              <p>
                <span>pH</span>
                <span>7.417</span>
              </p>
            </el-col>
            <el-col :span="8" class="ph-sty">
              <p></p>
              <p>
                <span>浑浊度</span>
                <span>7.417</span>
              </p>
            </el-col>
            <el-col :span="8" class="ph-sty">
              <p></p>
              <p>
                <span>余氧</span>
                <span>7.417</span>
              </p>
            </el-col>
          </el-row>
        </div>
        <!-- 供水情况 内容区域 -->
        <div v-if="waterState == '1'">
          <el-row>
            <el-col :span="5" class="ph-sty" style="padding: 2% 0 0 5%;">
              <i v-if="realInfo.supplyStatus == 1" class="icon-zheng-chang-gongshui" />
              <i v-else class="icon-yi-chang-gongshui" />
            </el-col>
            <el-col :span="19" class="water-supply-sty">
              <p>
                数据更新时间
                <span>{{realInfo.updateTime || '-'}}</span>
              </p>
              <p>
                实时供水流量
                <span>{{realInfo.supplyFlow || '-'}}</span>
              </p>
              <p>
                今日累计供水量
                <span>{{realInfo.totalSupplyQuantity || '-'}}</span>
              </p>
            </el-col>
          </el-row>
        </div>
        <!-- 表头 -->
        <div style="display: flex; justify-content: space-between;">
          <p style="line-height: 44px;">
            <i class="icon-diamonds" style="margin-right: 10px; cursor: pointer;vertical-align: middle;"/>
            <span style="line-height:20px;height:20px;color:#fff;font-size: 18px;">
              {{ waterState == '0' ? '水质实时监测变化趋势(水厂管理单位)' : '实时供水流量变化曲线' }}
            </span>
          </p>
          <p style="line-height: 44px;">
            <span :class=" graphData == '过去24小时' ? 'panorama-sty' : 'panorama-style'" style="margin-right:10px;" @click="graph('过去24小时')">过去24小时</span>
            <span :class=" graphData == '近7日' ? 'panorama-sty' : 'panorama-style'" style="margin-right:10px;" @click="graph('近7日')">近7日</span>
            <span :class=" graphData == '近1月' ? 'panorama-sty' : 'panorama-style'" style="margin-right:10px;" @click="graph('近1月')">近1月</span>
            <el-select v-model="value" placeholder="请选择" v-if="waterState == '0'" @change="earlyWarning(value)">
              <el-option label="pH" value="pH" />
              <el-option label="浑浊度" value="浑浊度" />
              <el-option label="余氯" value="余氯" />
            </el-select>
          </p>
        </div>
        <i class="icon-card-title-divide" />
        <!-- 图表展示 -->
        <div id="diagram" style="width:100%;height:340px;" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as echarts from 'echarts';
  import { StatisticsTabs } from "./DetailsCommonDialog/components";
  import { waterResourceApi } from "@/api/waterResourcesGuarantee";
  export default {
    name: "waterSourDialog",
    components: { StatisticsTabs },
    data() {
      return {
        tableList: [{
          a: '石坑底珊溪水水源警示标志',
          b: '警示类',
          c: '林晓磊',
          d: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
        },{
          a: '石坑底山溪水界标',
          b: '公告类',
          c: '熊强',
          d: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
        }],
        obj: {},
        img: "",
        name: "",
        scale: "",
        type: "",
        area: "",
        isP: "",
        target: "",
        warn: "",
        waterState: '0',
        value: 'pH',
        graphData: '过去24小时',
        WarningData: 'pH',
        xAxisData: [],
        seriesData: [],
        upperData: '',
        floorData: '',
        realInfo: {},
        seriesList: [],
        activeTabBeauty: "基本信息",
      };
    },
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
    },
    computed: {
      setWaterWarnColor() {
        switch (this.obj.warn) {
          case "暂无风险":
            return "#19AF47";
          case "黄色预警":
            return "#e89100";
          case "橙色预警":
            return "#ff551f";
          case "红色预警":
            return "#e80000";
        }
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.runningOpt();
      });
    },
    methods: {
      // 查看图片
      pictureUrl(val) {
        console.log(98,val)
      },
      init(val) {
        this.obj = val;
        if (val.type == '水厂运行状态') {
          this.waterWorksStateCurve();
        }
      },
      // 水源保障tab切换
      changeActiveTabBeauty(val) {
        this.activeTabBeauty = val;
      },
      // 
      earlyWarning(val) {
        this.WarningData = val;
      },
      // 水厂运行状态
      waterQuality(val) {
        this.waterState = val;
        this.waterWorksStateCurve();
      },
      // 图表数据
      graph(val) {
        this.graphData = val;
        this.waterWorksStateCurve();
      },
      // 
      async waterWorksStateCurve() {
        this.xAxisData = [];
        this.seriesData = [];
        let opt = {
          cwsCode: this.obj.cwsCode,
          days: this.graphData == '过去24小时' ? '1' : this.graphData == '近7日' ? '7' : '30' ,
          type: this.waterState == '0' ? '2' : '1'
        }
        let res = await waterResourceApi.waterWorksStateCurve(opt);
        if(res.code == 0) {
          if (this.waterState == '0') {
            this.seriesList = [{
              data: this.seriesData,
              type: 'line',
              symbol: "none",
              markLine : {   //添加警戒线
                symbol:"none",               //去掉警戒线最后面的箭头
                silent:true,
                data : [{
                  label:{
                    position:"middle",         //将警示值放在哪个位置，三个值“start”,"middle","end"  开始  中点 结束
                    formatter: "预警值上限",
                    color:"red",
                    fontSize:14
                  },
                  silent:true,             //鼠标悬停事件  true没有，false有
                  lineStyle:{               //警戒线的样式  ，虚实  颜色
                    type:"dotted",
                    color:"red"
                  },
                  name: '预警值上限',
                  yAxis: '300'
                },{
                  label:{
                    position:"middle",         //将警示值放在哪个位置，三个值“start”,"middle","end"  开始  中点 结束
                    formatter: "预警值下限",
                    color:"#19AF47",
                    fontSize:14
                  },
                    silent:true,             //鼠标悬停事件  true没有，false有
                    lineStyle:{               //警戒线的样式  ，虚实  颜色
                      type:"dotted",
                      color:"#19AF47"
                    },
                    name: '预警值下限',
                    yAxis: '100'
                }]
              }
            }]
            if(this.WarningData == 'pH') {
              res.data.list.forEach((val)=> {
                this.xAxisData.push(val.timeRecord);
                this.seriesData.push(val.phValue);
              });
              this.upperData = res.data.phUpperLimit;
              this.floorData = res.data.phLowerLimit;
              this.$nextTick(() => {
                this.runningOpt();
              });
            }else if(this.WarningData == '浑浊度') {
              res.data.list.forEach((val)=> {
                this.xAxisData.push(val.timeRecord);
                this.seriesData.push(val.ntuValue);
              });
              this.upperData = res.data.ntuLowerLimit;
              this.floorData = res.data.ntuUpperLimit;
              this.$nextTick(() => {
                this.runningOpt();
              });
            }else if(this.WarningData == '余氯') {
              res.data.list.forEach((val)=> {
                this.xAxisData.push(val.timeRecord);
                this.seriesData.push(val.clValue);
              });
              this.upperData = res.data.clLowerLimit;
              this.floorData = res.data.clUpperLimit;
              this.$nextTick(() => {
                this.runningOpt();
              });
            }
          } else {
            this.seriesList = [{
              data: this.seriesData,
              type: 'line',
              symbol: "none",
            }];
            this.realInfo = res.data.realInfo;
            res.data.list.forEach((val)=> {
              this.xAxisData.push(val.updateTime);
              this.seriesData.push(val.totalSupplyQuantity);
            });
          }
          
        }
      },
      // 水厂运行状态 叠加柱状图
      runningOpt() {
        var chartDom = document.getElementById('diagram');
        var myChart = echarts.init(chartDom);
        var option;
        option = {
          xAxis: {
            type: 'category',
            axisLabel: {
              interval: 0,
              fontSize: "12",
              color: '#fff'
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            data: this.xAxisData
          },
          yAxis: {
            type: 'value',
            nameTextStyle: {
              color: "#ffffff99",
              fontSize: "12",
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              color: "#fff",
              fontSize: "14",
            },
            splitLine: {
              lineStyle: {
                color: "#fff",
                opacity: 0.5,
              },
            },
          },
          series: this.seriesList
        };
        option && myChart.setOption(option);
      },
      handleClose() {
        this.$emit("closeDialog");
      },
    },
  };
</script>
<style lang="scss" scoped>
.primary-dialog {
  .img-panel {
    height: 300px;
    margin-bottom: 20px;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .info-panel {
    color: #fff;
    font-size: 16px;
    line-height: 32px;
    span.name {
      font-size: 15px;
    }
    span.value {
      margin-left: 10px;
      color: #33ffff;
    }
  }

  ::v-deep .el-radio__input.is-checked .el-radio__inner {
    border-color: #1ef8f2;
    background-color: #1ef8f2;
  }
  ::v-deep .el-dialog {
    .el-dialog__header {
      padding: 2px;
      .primary-dialog__title {
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
      }
    }
    .el-dialog__body {
      height: 600px;
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        height: 100%;
        padding: 20px;
      }
    }
    .name-sty {
      font-family: PingFangSC-Semibold;
      font-size: 16px;
      color: #FFFFFF;
      font-weight: 600;
    }
    .value-sty {
      font-family: PingFangSC-Semibold;
      margin-left: 10px;
      font-size: 18px;
      color: #33FFFF;
      letter-spacing: 0;
      line-height: 24px;
      font-weight: 600;
    }
    .panorama-sty {
      background-image: linear-gradient(180deg, #36C0E4 20%, #0639FF 100%);
      border-radius: 11px;
      color: #fff;
      padding: 5px 15px;
    }
    .panorama-style {
      border-radius: 11px;
      color: #fff;
      padding: 5px 15px;
    }
    .real-time-sty {
      padding: 6px 12px;
      font-size: 14px;
      color: #56FEFE;
      text-align: center;
      line-height: 16px;
      font-weight: 400;
      border: 1 solid #56FEFE;
      box-shadow: inset 0 0 5px 0 #56FEFE;
    }
    .real-time-style {
      padding: 5px 10px;
      font-size: 14px;
      color: #FFFFFF;
      text-align: center;
      line-height: 16px;
      font-weight: 400;
      opacity: 0.6;
      border: 1px solid #FFFFFF;
    }
    .monitoring-sty {
      height: 40px;
      width: 100%;
      margin: 10px 0;
      padding-left: 10px;
      background-image: linear-gradient(90deg, rgba(255,255,255,0.20) 0%, rgba(249,249,249,0.00) 100%);
    }
    .water-supply-sty {
      margin: 10px 0;
      padding-left: 10px;
      background-image: linear-gradient(90deg, rgba(255,255,255,0.20) 0%, rgba(249,249,249,0.00) 100%);
      > p {
        line-height: 40px;
        height: 40px;
        color: #fff;
        > span {
          padding-left: 10px;
          color: #00c1ff;
        }
      }
    }
    .ph-sty {
      height: 40px;
      line-height: 40px;
      display: flex;
      justify-content: left;
      >p:nth-child(1) {
        display:inline-block;
        width: 12px;
        height: 12px;
        margin-top: 1rem;
        margin-right: 10px;
        background:#33FFFF;
      }
      >p:nth-child(2) {
        font-size: 16px;
        font-weight: 600;
        color: #fff;
        >span:nth-child(2) {
          padding-left:15px;
          color: #33FFFF;
        }
      }
    }
  }
}
</style>