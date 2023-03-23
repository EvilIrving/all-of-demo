<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-26 21:44:40
 * @Description: 地图右侧-统计
 * @FilePath: /rscp-big-screen/src/views/sgzgScreenNew/reservoirControl/reservoirControlRight/ReservoirControlRight.vue
-->
<template>
  <div class="map_right" >
    <!-- <img class="lyqh_btn" src="../../../../assets/images/lyqh.png" alt=""> -->
    <div class="sgzg_title">
      <div class="sgzg_title_name">
        防洪控运
      </div>
      <img src="@/assets/images/qtj-arr.png" alt="" />
    </div>
    <div class="top_box">
      <div class="top_tab">
        <div :class="['top_item', topIndex == index ? 'active' : '']" v-for="(item, index) in topList" :key="index" @click="clickTop(index)">
          <p>{{item.name}}<span>{{item.num}}</span>座</p>
          <div v-if="topIndex == index" class="bottom_line"></div>
        </div>
      </div>
      <div class="top_number">
        <div class="top_number_item" v-for="(item, index) in numberList" :key="index">
          <p>{{item.name}}</p>
          <span>{{item.num}}<i>座</i></span>
        </div>
      </div>
    </div>
    
    <div class="middle_box">
      <bar-chart-two :chartData="skkrData"></bar-chart-two>
    </div>
    <div class="sgzg_title">
      <div class="sgzg_title_name">
        兴利控运
      </div>
      <img src="@/assets/images/qtj-arr.png" alt="" />
    </div>
    <div class="bottom_box box1">
      <!-- <div class="bottom_item">
        <div class="item_left" >
          <img src="../../../../assets/images/gsyx-big.png" alt="" />
          <div class="scale_item_name">
            <p>供水运行</p>
            <div class="scale_item_num">2138<span>座</span></div>
          </div>
        </div>
        <div class="item_right">
          <div class="first_item">
            <p>有闸门(开闸)</p>
            <span>283<i>座</i></span>
          </div>
          <div class="first_item">
            <p>无闸门(溢洪)</p>
            <span>26<i>座</i></span>
          </div>
        </div>
      </div> -->
      <div class="bottom_item">
        <div class="item_left" >
          <img src="../../../../assets/images/gs-big.png" alt="" />
          <div class="scale_item_name">
            <p>供水</p>
            <div class="scale_item_num">2138<span>座</span></div>
          </div>
        </div>
        <div class="item_right">
          <h3>流量</h3>
          <p>-<i>m³/s</i></p>
        </div>
      </div>
      <div class="bottom_item">
        <div class="item_left" >
          <img src="../../../../assets/images/gg-big.png" alt="" />
          <div class="scale_item_name">
            <p>灌溉</p>
            <div class="scale_item_num">806<span>座</span></div>
          </div>
        </div>
        <div class="item_right">
          <h3>流量</h3>
          <p>-<i>m³/s</i></p>
        </div>
      </div>
      <div class="bottom_item">
        <div class="item_left" >
          <img src="../../../../assets/images/fd-big.png" alt="" />
          <div class="scale_item_name">
            <p>发电</p>
            <div class="scale_item_num">2511<span>座</span></div>
          </div>
        </div>
        <div class="item_right">
          <h3>流量</h3>
          <p>-<i>m³/s</i></p>
        </div>
      </div>
    </div>
    <div class="sgzg_title" style="margin-top:5px;">
      <div class="sgzg_title_name">
        生态放水
      </div>
      <img src="@/assets/images/qtj-arr.png" alt="" />
    </div>
    <div class="bottom_box box2">
      <div class="bottom_item">
        <div class="item_left" >
          <img src="../../../../assets/images/st-big.png" style="width:47px;height:53px" alt="" />
          <div class="scale_item_name">
            <p>生态</p>
            <div class="scale_item_num">2511<span>座</span></div>
          </div>
        </div>
        <div class="item_right">
          <h3>流量</h3>
          <p>-<i>m³/s</i></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BarChartTwo from "../../components/BarChartTwo";
export default {
  name: "",
  props: {},
  components: {
    BarChartTwo
  },
  data() {
    return {
      topIndex: 0,
      topList: [
        {
          name: '溢洪',
          num: 0,
        },
        {
          name: '泄洪',
          num: 0,
        },
      ],
      skkrData: {
        title: "按行政区划分",
        id: "skkrChart",
        xData: ['杭州','宁波','温州','湖州','绍兴','金华','衢州','舟山','台州','丽水'],
        yData: [0,0,0,0,0,0,0,0,0,0],
        yData2: [0,0,0,0,0,0,0,0,0,0],
        unit: "座",
      },
      numberList: [
        {
          name: '大(1)型',
          num: 0
        },
        {
          name: '大(2)型',
          num: 0
        },
        {
          name: '中型',
          num: 0
        },
        {
          name: '小(1)型',
          num: 0
        },
        {
          name: '小(2)型',
          num: 0
        }
      ]
    };
  },
  
  mounted() {
    
  },
  methods: {
    clickTop(index){
      this.topIndex = index
      if(index == 0){
        this.numberList = [
          {
            name: '大(1)型',
            num: 0
          },
          {
            name: '大(2)型',
            num: 0
          },
          {
            name: '中型',
            num: 0
          },
          {
            name: '小(1)型',
            num: 0
          },
          {
            name: '小(2)型',
            num: 0
          }
        ]
      }else{
        this.numberList = [
          {
            name: '大(1)型',
            num: 0
          },
          {
            name: '大(2)型',
            num: 0
          },
          {
            name: '中型',
            num: 0
          },
          {
            name: '小(1)型',
            num: 0
          },
          {
            name: '小(2)型',
            num: 0
          }
        ]
      }
    }
  },
};
</script>

<style lang="scss" scoped>
.map_right {
  height: 100%;
  z-index: 1002;
  position: relative;
  .lyqh_btn{
    position: absolute;
    top: -42px;
    right: 0;
    width: 121px;
    height: 40px;
    cursor: pointer;
  }
  .top_box{
    height: 170px;
    padding: 16px;
    box-sizing: border-box;
    .top_tab{
      @include flexbox();
      @include flexAI(center);
      .top_item{
        padding: 4px 8px;
        margin-right: 30px;
        cursor: pointer;
        position: relative;
        .bottom_line{
          position: absolute;
          bottom: -5px;
          left: 0;
          height: 5px;
          width: 100%;
          background: url('../../../../assets/images/bottom_line.png') no-repeat bottom;
          background-size: 100% auto;
        }
        p{
          font-size: 18px;
          color: #000;
          &>span{
            font-size: 28px;
            color: #000;
            font-weight: 500;
            padding: 0 5px;
          }
        }
        &.active{
          background: #9ECAFA;
          color: #000;
          p{
            font-size: 18px;
            &>span{
              font-size: 28px;
              // color: #577FEC;
              font-weight: 500;
            }
          }
        }
      }
    }
    .top_number{
      width: 100%;
      height: 92px;
      margin-top: 10px;
      background: #F8F8F8;
      border-radius: 4px;
      border: 1px solid #a8b5e5;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(space-between);
      .top_number_item{
        width: 20%;
        text-align: center;
        p{
          font-size: 16px;
          color: #000000;
          line-height: 24px;
          padding-bottom: 10px;
        }
        span{
          font-size: 28px;
          font-weight: 500;
          color: #577FEC;
          line-height: 24px;
          i{
            font-size: 16px;
            color: #333333;
            line-height: 24px;
            margin-left: 5px;
          }
        }
      }
    }
  }
  .middle_box{
    height: calc(100% - 700px);
  }
  .bottom_box{
    padding: 0 16px;
    // overflow: auto;
    &.box1{
      height: 270px;
    }
    &.box2{
      height: 100px;
    }
    .bottom_item{
      @include flexbox();
      background: #F8F8F8;
      border-radius: 4px;
      border: 1px solid #a8b5e5;
      margin-top: 8px;
      .item_left {
        @include flexbox();
        @include flexAC();
        color: #333;
        height: 82px;
        cursor: pointer;
        padding: 14px 0;
        box-sizing: border-box;
        margin-right: 8px;
        width: 181px;
        border-right: 1px solid #a8b5e5;
        img {
          margin:0 14px 0 16px;
          width: 53px;
          height: 56px;
        }
        .scale_item_name {
          p{
            font-size: 16px;
          }
          .scale_item_num {
            font-size: 28px;
            font-weight: 600;
            // text-align: center;
            margin-top: 10px;
            span {
              font-size: 16px;
              padding-left: 5px;
            }
          }
        }
      }
      .item_right{
        width: calc(100% - 181px);
        @include flexbox();
        @include flexAI(center);
        @include flexJC(space-between);
        .first_item{
          width: 50%;
          text-align: center;
          p{
            font-size: 16px;
            font-weight: 400;
            color: #000000;
            padding-bottom: 10px;
          }
          span{
            font-size: 28px;
            font-weight: 500;
            color: #333333;
            i{
              font-size: 16px;
              font-weight: 400;
              color: #333333;
            }
          }
        }
        &>h3{
          font-size: 18px;
          color: #000000;
          line-height: 24px;
          margin-left: 48px;
        }
        &>p{
          font-size: 28px;
          font-weight: 500;
          color: #333333;
          line-height: 24px;
          margin-right: 20px;
          i{
            margin-left: 6px;
            font-size: 16px;
            color: #333333;
            line-height: 24px;
          }
        }
      }
    }
  }
}
</style>
