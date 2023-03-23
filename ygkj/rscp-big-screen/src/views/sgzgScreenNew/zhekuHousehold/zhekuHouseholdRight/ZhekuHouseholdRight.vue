<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-29 18:04:58
 * @Description: 
 * @FilePath: /rscp-big-screen/src/views/sgzgScreenNew/zhekuHousehold/zhekuHouseholdRight/ZhekuHouseholdRight.vue
-->
<template>
  <div class='map_right'>
    <div class="cqgl_box">
      <div class="second_title">
        <div class="name" style="width: 95px;">
          <p>产权属性</p>
        </div>
        <!-- <div class="solid"></div>
        <div class="dashed"><div></div><div></div></div> -->
      </div>
      <div class="status_list">
        <div class="status_item">
          <img src="../../../../assets/images/cqgl.png" alt="" />
          <div class="status_right">
            <p>水利部门</p>
            <span>296<i>座</i></span>
          </div>
        </div>
        <div class="status_item">
          <img src="../../../../assets/images/cqgl.png" alt="" />
          <div class="status_right">
            <p>国资部门</p>
            <span>46<i>座</i></span>
          </div>
        </div>
        <div class="status_item">
          <img src="../../../../assets/images/cqgl.png" alt="" />
          <div class="status_right">
            <p>国家电网</p>
            <span>17<i>座</i></span>
          </div>
        </div>
        <div class="status_item">
          <img src="../../../../assets/images/cqgl.png" alt="" />
          <div class="status_right">
            <p>集体经济组织</p>
            <span>3551<i>座</i></span>
          </div>
        </div>
        <div class="status_item">
          <img src="../../../../assets/images/cqgl.png" alt="" />
          <div class="status_right">
            <p>民营股份制</p>
            <span>348<i>座</i></span>
          </div>
        </div>
        <div class="status_item">
          <img src="../../../../assets/images/cqgl.png" alt="" />
          <div class="status_right">
            <p>其他部门</p>
            <span>38<i>座</i></span>
          </div>
        </div>
      </div>
    </div>
    <div class="qqhj_box">
      <div class="second_title">
        <div class="name" style="width: 95px;">
          <p>确权划界</p>
        </div>
        <!-- <div class="solid"></div>
        <div class="dashed"><div></div><div></div></div> -->
      </div>
      <div class="qqhj_top">
        <div class="qqhj_top_item">
          <p>产权已登记</p>
          <span>2533<i>座</i></span>
        </div>
        <div class="qqhj_top_item">
          <p>管保范围已划定</p>
          <span>4248<i>座</i></span>
        </div>
      </div>
      <div class="qqhj_tabs">
        <div :class="['qqhj_tabs_item', tabIndex == index ? 'active' : '']" v-for="(item, index) in tabList" :key="index" @click="clickTab(index)">{{item}}</div>
      </div>
      <div class="qqhj_chart">
        <bar-chart-no-click
          :chartData="chartData"
        ></bar-chart-no-click>
      </div>
    </div>
    <div class="zrr_box">
      <div class="second_title">
        <div class="name" style="width: 95px;">
          <p>责任人</p>
        </div>
        <!-- <div class="solid"></div>
        <div class="dashed"><div></div><div></div></div> -->
      </div>
      <div class="status_list">
        <div class="status_item">
          <img src="../../../../assets/images/zrr.png" alt="" />
          <div class="status_right">
            <p>政府责任人</p>
            <span>951<i>人</i></span>
          </div>
        </div>
        <div class="status_item">
          <img src="../../../../assets/images/zrr.png" alt="" />
          <div class="status_right">
            <p>水行政主管部门责任人</p>
            <span>161<i>人</i></span>
          </div>
        </div>
        <div class="status_item">
          <img src="../../../../assets/images/zrr.png" alt="" />
          <div class="status_right">
            <p>产权单位责任人</p>
            <span>1625<i>人</i></span>
          </div>
        </div>
        <div class="status_item">
          <img src="../../../../assets/images/zrr.png" alt="" />
          <div class="status_right">
            <p>管理单位责任人</p>
            <span>671<i>人</i></span>
          </div>
        </div>
        <div class="status_item">
          <img src="../../../../assets/images/zrr.png" alt="" />
          <div class="status_right">
            <p>技术责任人</p>
            <span>1506<i>人</i></span>
          </div>
        </div>
        <div class="status_item">
          <img src="../../../../assets/images/zrr.png" alt="" />
          <div class="status_right">
            <p>巡查责任人</p>
            <span>3999<i>人</i></span>
          </div>
        </div>
      </div>
    </div>
  </div>
  
</template>

<script>
import BarChartNoClick from "../../components/BarChartNoClick";
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: '',
  props: {
    
  },
  components: {
    BarChartNoClick
  },
  data () {
    return {
      tabIndex: 0,
      tabList: ['产权已登记','管保范围已划定'],
      chartData: {
        title: "",
        xShupai: true,
        id: "qqhjChart",
        xData: ['杭州','宁波','温州','湖州','绍兴','金华','衢州','舟山','台州','丽水'],
        yData: [381,174,217,112,406,549,370,0,157,167],
        unit: "座",
      },
      skfbLoading: true,
      opt: {
        statistic: "mapList",
        itemCategory: "keyStr",
        itemValue: "FCLWL_PREC",
        basin: "",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectAdnm() {
      return this.$store.state.leftSelectAdnm;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.opt.adcd = val ? val : this.$localData("get", "userInfo").adcd;
      if (val == "33") {
        this.opt.superviseLevel = 1;
      } else {
        this.opt.superviseLevel = "";
      }
      this.getReservoirList();
    }
  },
  mounted() {
    this.getReservoirList()
  },
  methods: {
    clickTab(index){
      this.tabIndex = index
      if(index == 1){
        this.chartData.yData = [631,401,324,157,555,795,466,209,346,364]
      }else{
        this.chartData.yData = [424,217,260,155,449,597,413,0,200,210]
      }
    },
    // 水库列表
    async getReservoirList() {
        this.$parent.$refs.reservoirMap.removeAllLayer();
        this.$parent.$refs.reservoirMap.closePopup();
        this.$parent.$refs.reservoirMap.initView();
        let res = await reservoirLeftStatisticApi(this.opt);
        if (res.success) {
          let pointArr = [];
          if (res.data.length) {
            res.data.map((v,i) => {
              v.lat = v.LTTD;
              v.lng = v.LGTD;
              v.rightType = "1";
              v.colorType = "blue";
              v.src = require("@/assets/images/map/reservoir.png");
              // if(v.project_scale == '1' || v.project_scale == '2' || v.project_scale == '3'){
              //   if(i == 0){
              //     v.colorType = "red"
              //   }
              //   if(i == 3 || i == 20 || i == 99){
              //     v.colorType = "yellow"
              //   }
              //   pointArr.push(v);
              // }
              pointArr.push(v);
            });
          }
          // this.$parent.showMapPointJh(pointArr, "kgkn");
          this.$parent.showMapPoints(pointArr);
        }
        this.tableLoading = false;
        this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", true);
    },
  }
}
</script>

<style lang='scss' scoped>
  .map_right{
    height: 100%;
    z-index: 1002;
    .status_list {
      width: 100%;
      @include flexbox();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      padding: 0 12px;
      box-sizing: border-box;
      .status_item {
        width: calc(50% - 5px);
        @include flexbox();
        @include flexAC();
        box-sizing: border-box;
        padding: 6px 0;
        background: #F8F8F8;
        border-radius: 4px;
        border: 1px solid #D6D6D6;
        margin-bottom: 5px;
        img {
          width: 52px;
          height: 52px;
          margin-right: 8px;
          margin-left: 16px;
        }
        .status_right {
          & > p {
            line-height: 18px;
            font-size: 14px;
            color: #333;
            font-weight: bold;
            padding: 8px 0 4px 0;
          }
          
          & > span {
            // line-height: 24px;
            font-size: 24px;
            color: #193BC4;
            font-weight: 500;
            i {
              font-size: 14px;
              padding-left: 4px;
              color: #000;
            }
          }
        }
      }
    }
    
    .qqhj_box{
      height: calc(100% - 540px);
      .qqhj_top{
        @include flexbox();
        @include flexJC(space-between);
        width: 100%;
        box-sizing: border-box;
        padding: 0 16px;
        .qqhj_top_item {
          width: calc(50% - 5px);
          border: 1px solid #D6D6D6;
          border-radius: 4px;
          box-sizing: border-box;
          padding-left: 21px;
          &:first-child{
            background: #f0f4fd;
            border-left: 6px solid #193BC4;
          }
          &:last-child{
            background: #f4ffff;
            border-left: 6px solid #04BE9A;
            & > span {
              color: #16BB9B;
            }
          }
          & > p {
            line-height: 24px;
            font-size: 14px;
            color: #333;
            font-weight: bold;
            padding: 8px 0 4px 0;
          }
          
          & > span {
            line-height: 24px;
            font-size: 26px;
            color: #193BC4;
            font-weight: 500;
            i {
              font-size: 14px;
              padding-left: 4px;
              color: #000;
            }
          }
        }
      }
      .qqhj_tabs{
        padding: 0 21px;
        margin: 16px 0;
        @include flexbox();
        .qqhj_tabs_item{
          cursor: pointer;
          color: #333;
          font-size: 14px;
          border-bottom: 3px solid #fff;
          line-height: 24px;
          margin-right: 20px;
          &.active{
            color: #000;
            border-color: #193BC4;
          }
        }
      }
      .qqhj_chart{
        height: calc(100% - 150px);
        // height: 100%;
      }
    }
    .cqgl_box{
      height: 265px;
    }
    .zrr_box{
      height: 265px;
    }
  }
</style>