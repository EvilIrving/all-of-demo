<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-26 20:08:21
 * @Description: 地图右侧-统计
 * @FilePath: /rscp-big-screen/src/views/sgzgScreenNew/repairProtect/repairProtectRight/RepairProtectRight.vue
-->
<template>
  <div class="map_right" >
    <div class="sgzg_title">
      <div class="sgzg_title_name">
        维养设施
      </div>
      <img src="@/assets/images/qtj-arr.png" alt="" />
    </div>
    <div class="yhqy_box">
      <div class="yhqy_wrap">
        <div class="yhqy_left" >
          <img src="@/assets/images/wcx-icon.png" alt="" />
          <div class="scale_item_name">
            <p>完成项</p>
            <div class="scale_item_num">6105<span>座</span></div>
          </div>
        </div>
        <div class="yhqy_right">
          <div class="yhqy_right_title">
            <p>完成率</p>
            <span>95%</span>
          </div>
          <el-progress :show-text="false" :stroke-width="22" color="#fff" :percentage="86"></el-progress>
        </div>
      </div>
    </div>
    
    <!-- <div class="xzqh_box">
      <div class="xzqh_chart">
        <bar-chart-double
          :chartData="skkrData"
        ></bar-chart-double>
      </div>
    </div> -->

    <div class="aqzt_box">
      <div class="second_title">
        <div class="name">
          <p>按安全状态分</p>
        </div>
        <div class="solid"></div>
        <div class="dashed"><div></div><div></div></div>
      </div>
      <div class="aqzt_wrap">
        <div class="aqzt_left" style="background: rgba(111, 171, 65, 0.1);color: #6FAB41;">
          <img src="@/assets/images/1b.png" alt="">
          <div>
            <p>正常坝</p>
            <span>(一类坝)</span>
          </div>
        </div>
        <div class="aqzt_right">
          <div class="scale_item" >
            <div class="scale_item_name">
              <p>完成项</p>
            </div>
            <div class="scale_item_num">5359<span>项</span></div>
          </div>
          <div class="scale_line"></div>
          <div class="scale_item" >
            <div class="scale_item_name">
              <p>完成率</p>
            </div>
            <div class="scale_item_num">95<span>%</span></div>
          </div>
        </div>
      </div>
      <div class="aqzt_wrap">
        <div class="aqzt_left" style="background: rgba(224, 135, 0, 0.1);;color: #d58a31;">
          <img src="@/assets/images/2b.png" alt="">
          <div>
            <p>病害坝</p>
            <span>(二类坝)</span>
          </div>
        </div>
        <div class="aqzt_right">
          <div class="scale_item" >
            <div class="scale_item_name">
              <p>完成项</p>
            </div>
            <div class="scale_item_num">540<span>项</span></div>
          </div>
          <div class="scale_line"></div>
          <div class="scale_item" >
            <div class="scale_item_name">
              <p>完成率</p>
            </div>
            <div class="scale_item_num">95<span>%</span></div>
          </div>
        </div>
      </div>
      <div class="aqzt_wrap">
        <div class="aqzt_left" style="background: rgba(222, 92, 10, 0.1);;color: #ce632a;">
          <img src="@/assets/images/3b.png" alt="">
          <div>
            <p>危险坝</p>
            <span>(三类坝)</span>
          </div>
        </div>
        <div class="aqzt_right">
          <div class="scale_item" >
            <div class="scale_item_name">
              <p>完成项</p>
            </div>
            <div class="scale_item_num">206<span>项</span></div>
          </div>
          <div class="scale_line"></div>
          <div class="scale_item" >
            <div class="scale_item_name">
              <p>完成率</p>
            </div>
            <div class="scale_item_num">95<span>%</span></div>
          </div>
        </div>
      </div>
    </div>

    <div class="sgzg_title">
      <div class="sgzg_title_name">
        维养服务
      </div>
      <img src="@/assets/images/qtj-arr.png" alt="" />
    </div>
    
    <div class="wyfw_box">
      <div class="wyfw_item">
        <img src="../../../../assets/images/wyqy.png" alt="">
        <p>物业企业</p>
        <div>168<span>家</span></div>
      </div>
      <div class="wyfw_item">
        <img src="../../../../assets/images/wyqy.png" alt="">
        <p>维养人数</p>
        <div>3287<span>人</span></div>
      </div>
    </div>
    
      
  </div>
</template>

<script>
import BarChartDouble from "../../components/BarChartDouble";
export default {
  name: "",
  props: {},
  components: {
    BarChartDouble
  },
  data() {
    return {
      skkrData: {
        title: "按行政区划分",
        id: "skkrChart",
        xData: ['杭州'],
        yData: [10],
        yData2: [20],
        unit: "亿m³",
      },
      skgmList: [
        {
          name: "大(1)型",
          value: 5,
          project_scale: 1,
          img: require("../../../../assets/images/dxsk.png"),
        },
        {
          name: "大(2)型",
          value: 29,
          project_scale: 2,
          img: require("../../../../assets/images/dxsk.png"),
        },
        {
          name: "中型",
          value: 159,
          project_scale: 3,
          img: require("../../../../assets/images/zxsk.png"),
        },
        {
          name: "小(1)型",
          value: 727,
          project_scale: 4,
          img: require("../../../../assets/images/xxsk.png"),
        },
        {
          name: "小(2)型",
          value: 3376,
          project_scale: 5,
          img: require("../../../../assets/images/xxsk.png"),
        }
      ],
      
    };
  },
  
  mounted() {
    
  },
  methods: {
    
  },
};
</script>

<style lang="scss" scoped>
.map_right {
  height: 100%;
  z-index: 1002;
  .yhqy_box{
    height: 115px;
    box-sizing: border-box;
    padding-top: 16px;
    .yhqy_wrap{
      padding: 0 13px;
      @include flexbox();
      @include flexJC(space-between);
      .yhqy_left {
        @include flexbox();
        @include flexAC();
        color: #fff;
        height: 93px;
        cursor: pointer;
        padding: 14px 0;
        box-sizing: border-box;
        margin-right: 8px;
        width: 182px;
        background: url('../../../../assets/images/wy-left-bg.png') no-repeat center;
        background-size: 100% 100%;
        img {
          margin:0 10px;
          width: 58px;
          height: 49px;
        }
        .scale_item_name {
          p{
            font-size: 18px;
          }
          .scale_item_num {
            font-size: 30px;
            font-weight: 500;
            // text-align: center;
            margin-top: 13px;
            span {
              font-size: 16px;
              padding-left: 5px;
            }
          }
        }
        
      }
      .yhqy_right{
        width: calc(100% - 190px);
        height: 93px;
        padding: 14px 16px;
        box-sizing: border-box;
        background: url('../../../../assets/images/wy-right-bg.png') no-repeat center;
        background-size: 100% 100%;
        .yhqy_right_title{
          @include flexbox();
          @include flexAI(center);
          @include flexJC(space-between);
          margin-bottom: 14px;
          p{
            color: #fff;
            font-size: 18px;
          }
          span{
            font-size: 24px;
            font-weight: 500;
            color: #fff;
          }
        }
      }
    }
  }
  // .xzqh_box{
  //   height: calc(100% - 630px);
  //   .xzqh_chart{
  //     height: 100%;
  //   }
  // }
  .aqzt_box{
    height: 440px;
    .aqzt_wrap{
      padding: 0 13px;
      @include flexbox();
      @include flexJC(space-between);
      margin-top: 8px;
      .aqzt_left{
        width: 172px;
        height: 116px;
        @include flexbox();
        @include flexJC(center);
        @include flexAI(center);
        img{
          width: 72px;
          height: 72px;
          margin-right: 8px;
        }
        &>div{
          text-align: center;
          p{
            font-size: 20px;
            margin-bottom: 5px;
          }
          span{
            font-size: 12px;
          }
        }
        
      }
      .aqzt_right{
        width: calc(100% - 180px);
        height: 116px;
        @include flexbox();
        @include flexJC(space-between);
        @include flexAI(center);
        box-sizing: border-box;
        background: #E7EEFF;
        .scale_item {
          color: #333;
          cursor: pointer;
          box-sizing: border-box;
          width: 50%;
          height: 50%;
          .scale_item_name {
            @include flexbox();
            @include flexAC();
            padding-left: 28px;
            font-size: 18px;
          }
          .scale_item_num {
            font-size: 34px;
            font-weight: 600;
            color: #000000;
            line-height: 48px;
            margin-top: 7px;
            padding-left: 30px;
            span {
              font-size: 16px;
              padding-left: 5px;
            }
          }
        }
        .scale_line{
          width: 1px;
          height: 24px;
          background: #FFFFFF;
        }
      }
    }
  }

  .wyfw_box{
    @include flexbox();
    @include flexJC(space-between);
    padding: 16px;
    height: calc(100% - 670px);
    overflow: hidden;
    .wyfw_item{
      width: 216px;
      height: 95%;
      border-radius: 4px;
      border: 1px solid #4A6BEE;
      background: url('../../../../assets/images/wyqy-bg.png') no-repeat right bottom;
      background-size: 40% 40%;
      text-align: center;
      img{
        width: 64px;
        height: 64px;
        margin: 13% 0 23px 0;

      }
      p{
        font-size: 20px;
        color: #000000;
        line-height: 24px;
        margin-bottom: 18px;
      }
      div{
        font-size: 34px;
        font-weight: 500;
        color: #4A6BEE;
        line-height: 24px;
        span{
          font-size: 16px;
          font-weight: 400;
          color: #333333;
          line-height: 24px;
          vertical-align: top;
          margin-left: 12px;
        }
      }
    }
  }
}
</style>

<style lang="scss">
  .el-progress-bar__outer{
    background: #95a6ee;
  }
</style>