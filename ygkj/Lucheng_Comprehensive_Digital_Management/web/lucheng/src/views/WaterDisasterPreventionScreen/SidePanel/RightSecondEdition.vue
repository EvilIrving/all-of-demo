<template>
  <div class="collapse-panel__content">
    <!-- 大中型水库 -->
    <panel-item title="大中型水库" hasSwitch>
      <template #title-switch>
        <i title="地图落点" class="icon-yinzhang-active icon-sty" />
        <i title="查看详表" class="icon-liebiao icon-sty" />
        <i title="一张图" class="icon-yizhangtu icon-sty" />
        <i title="洪水预报弹窗" class="icon-hongshui-yubao icon-sty" @click="FloodForecasting"/>
      </template>
      <!-- 当前泄洪水库 -->
      <div class="reservoir-sty">
        <p> 当前泄洪 <span> 15 </span> 座 </p>
        <p> 累计泄洪量 <span> 23 </span> 万m³ </p>
      </div>
      <!-- 详情泄洪量 -->
      <div class="flood-discharge">
        <el-row>
          <el-col :span="12" class="right-border-sty">
            <div class="flood-discharge-sty">
              <p>珊溪水库</p>
              <div>
                <i class="icon-xie-hong"></i>
                <p>泄洪 <span>24.6</span>m³</p>
              </div>
            </div>
          </el-col>
          <el-col :span="12"></el-col>
        </el-row>
      </div>
    </panel-item>
    <!-- 水闸 -->
    <panel-item title="水闸" hasSwitch>
      <template #title-switch>
        <i title="地图落点" class="icon-yinzhang-active icon-sty" />
        <i title="查看详表" class="icon-liebiao icon-sty" />
        <i title="一张图" class="icon-yizhangtu icon-sty" />
      </template>
      <div class="rainfall-style">
        <p>
          总数 <span> {{ wagaData.wagaStNum }} </span> 个
        </p>
      </div>
      <!-- 水闸详情 -->
      <div class="water-gate-sty">
        <!-- 大中型水库 -->
        <div>
          <p>温瑞平</p>
          <p> 总数 <span> {{ wagaData.wrpNum }} </span> 个 </p>
          <p>
            <i style="margin-right: 10px" class="icon-kai-qi-shu"></i>已开启 <span> {{ wagaData.wrpOpenNum }} </span> 个
          </p>
        </div>
        <div>
          <p>
            <i class="el-icon-time"></i><span>累计:</span>
            <el-input-number
              v-model="num"
              size="small"
              controls-position="right"
              :min="1"
              :max="72"
              @change="changeHours"
            />
          </p>
          <p style="font-size:20px;">
            泄洪总量 <span> {{ wagaData.drainage }} </span> 万m³
          </p>
        </div>
      </div>
    </panel-item>
    <!-- 河网 -->
    <panel-item title="河网" :action-tabs="['平均纳洪能力', '累计纳洪量']" hasSwitch>
      <template #title-switch>
        <i title="地图落点" class="icon-yinzhang-active icon-sty" />
        <i title="查看详表" class="icon-liebiao icon-sty" />
        <i title="一张图" class="icon-yizhangtu icon-sty" />
      </template>
      <!-- 河网雷达图 -->
      <div id="riverEchart" style="width: 100%; height: 250px; margin-top: 20px;"></div>
    </panel-item>
  </div>
</template>

<script>
// echart插件
import * as echarts from "echarts";
// 标题组件
import PanelItem from "../components/PanelItem";
// api接口调用
import { disasterPreventionApi } from "@/api/RightSildePanel";

export default {
  name: "RightSecondEdition",
  components:{
    PanelItem,
  },
  data() {
    return {
      //累计泄洪时间
      num: 1,
      //水闸统计数据
      wagaData: {},
    }
  },
  mounted() {
    this.$nextTick(()=> {
      this.riverEchart();
      this.wagaStaStats(this.num);
      this.rsvrProjDrainageStats();
    })
  },
  methods:{
    /**
     * 大中型水库
     */
    async rsvrProjDrainageStats() {
      await disasterPreventionApi.rsvrProjDrainageStats().then((res) => {
        if (res.code == 0) {
          // console.log(456,res.data);

          // this.wagaData = res.data;
        }
      });
    },
    /**
     * 洪水预报
     */
    FloodForecasting() {
      this.$emit('FloodForecasting');
    },
    /**
     * 水闸统计数据
     */
    async wagaStaStats(val) {
      let obj = {
        hours: val,
      };
      await disasterPreventionApi.wagaProjStats(obj).then((res) => {
        if (res.code == 0) {
          this.wagaData = res.data;
        }
      });
    },
    /**
     * 河网雷达图
     */
    riverEchart() {
      var chartDom = document.getElementById('riverEchart');
      var myChart = echarts.init(chartDom);
      var option;
      option = {
        radar: [{
          indicator: [
            { name: '乐成', max: 65},
            { name: '江南', max: 60, axisLabel:{show:false}},
            { name: '温瑞', max: 30, axisLabel:{show:false}},
            { name: '瑞平', max: 38, axisLabel:{show:false}},
            { name: '鳌江', max: 52, axisLabel:{show:false}},
            { name: '永强', max: 25, axisLabel:{show:false}}
          ],
          center: ['50%', '50%'],
          radius: 95,
          name: {
            textStyle: {
              color: '#fff',
            }
          },
          splitArea: {
            areaStyle: {
              color: 'rgba(114, 172, 209, 0)', //圆环颜色
              shadowColor: '#fff', // 圆颜色
              shadowBlur: 10
            }
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(114, 172, 209, 0.7)' // 分割线
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(114, 172, 209, 0.7)' //圆线
            }
          },
          axisLabel:{
            show:true,
          }
        }],
        series: [{
          // name: '预算 vs 开销（Budget vs spending）',
          type: 'radar',
          data: [
            {
              value: [42, 30, 20, 35, 50, 18],
              name: '预算分配（Allocated Budget）',
              label: {
                normal: {
                  show: true
                }
              },
              areaStyle: {
                color: new echarts.graphic.RadialGradient(0.1, 0.6, 1, [
                  {
                    color: 'rgba(238, 200, 11, 0.5)',
                    offset: 0
                  },
                  {
                    color: 'rgba(232, 0, 0, 1)',
                    offset: 1
                  }
                ])
              }
            }
          ]
        }]
      };
      option && myChart.setOption(option);
    },
    /**
     * 累计泄洪时间
     */
    changeHours() {
      this.wagaStaStats(this.num);
    },
  }
}
</script>

<style lang="scss" scoped>
  @import "../style/SildePanel.scss";
  .collapse-panel__content {
    .icon-sty {
      margin-right: 0.3125rem;
    }
    .reservoir-sty {
      font-size:20px;
      margin: 10px 0;
      > p:nth-child(1) {
        margin-right: 16px;
        > span {
          color: #1cfff5;
        }
      }
      > p:nth-child(2) {
        background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png) no-repeat
          top center;
        background-size: 100% 100%;
      }
    }
    .flood-discharge {
      margin: 10px 0;
      color: #fff;
      .right-border-sty {
        border-right: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
        .flood-discharge-sty {
          > p {
            font-size: 24px;
            text-align: center;
          }
          > div {
            display: flex;
            padding: 10px 0;
            font-size: 18px;
            justify-content: center;
            > i {
              margin: 5px 10px 0 0;
            }
            > p {
              > span {
                margin: 0 10px;
                font-size: 24px;
                color: #1cfff5;
              }
            }
          }
        }
      }
    }
    .rainfall-style {
      margin: 20px 0;
      display: flex;
      justify-content: center;
      > p {
        font-size: 20px;
        > span {
          margin: 0 10px;
          color: #1cfff5;
          font-size: 24px;
        }
      }
    }
    .water-gate-sty {
      padding: 10px;
      margin-bottom: 10px;
      background: url(~assets/images/WaterDisastersImage/shui-zha.png) no-repeat top center;
      background-size: 100% 100%;
      > div:nth-child(1) {
        display: flex;
        background: none;
        padding: 10px 0 15px 0;
        justify-content: space-between;
        border-bottom: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
        > p:nth-child(1) {
          color: #1cfff5;
          font-size: 24px;
        }
        >p:nth-child(2) {
          color: #fff;
          font-size: 20px;
          > span {
            color: #1cfff5;
            font-size: 24px;
          }
        }
        >p:nth-child(3) {
          font-size: 20px;
          color: #fff;
          > i {
            margin: 2px;
            vertical-align: unset;
          }
          > span {
            color: #1cfff5;
            font-size: 24px;
          }
        }
      }
      > div:nth-child(2) {
        display: flex;
        background: none;
        padding: 15px 0 10px 0;
        justify-content: space-between;
        > p:nth-child(1) {
          > i {
            font-size: 17px;
            margin: 9px 10px 0 0;
            color: #fff;
          }
          > span {
            font-size:20px;
            line-height: 34px;
            margin-right: 10px;
          }
          font-size: 16px;
          color: #fff;
          display: flex;
          justify-content: left;
          ::v-deep.el-input {
            width: 80px;
          }
          ::v-deep.el-input-number {
            position: relative;
            display: inline-block;
            width: 5.25rem;
            border: 1px solid #ffffff;
            border-radius: 4px;
          }
          ::v-deep.el-input__inner {
            background: none;
          }
          ::v-deep.el-input-number__decrease {
            border-left: 0.0625rem solid #fff;
            background: none;
            > i {
              color: #fff;
            }
          }
          ::v-deep.el-input-number__increase {
            height: 1.05rem;
            border-left: 0.0625rem solid #fff;
            background: none;
            > i {
              color: #fff;
            }
          }
        }
        > p:nth-child(2) {
          font-size:20px;
          line-height: 34px;
          color: #fff;
          font-size: 16px;
          > span {
            font-size:28px;
            color: #1cfff5;
            padding: 0;
            margin: 0;
          }
        }
      }
    }
  }
</style>