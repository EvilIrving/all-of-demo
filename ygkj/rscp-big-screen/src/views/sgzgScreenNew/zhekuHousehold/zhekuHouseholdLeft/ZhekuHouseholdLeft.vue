<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-26 20:06:50
 * @Description: 地图左侧-统计
 * @FilePath: /rscp-big-screen/src/views/sgzgScreenNew/zhekuHousehold/zhekuHouseholdLeft/ZhekuHouseholdLeft.vue
-->
<template>
  <div class="map_left"  ref="mapLeftHeight">
    <div class="sgzg_title"  ref="titleHeight">
      <div class="sgzg_title_name">
        水库数量
      </div>
      <img src="@/assets/images/qtj-arr.png" alt="" />
    </div>
    <div class="scale_box" ref="scaleHeight">
      <div class="scale_top">
        <div class="scale_top_item1">
          <p>全部水库</p>
          <span>4296</span>
        </div>
        <div class="scale_top_item2">
          <p>水利部门注册</p>
          <span>4279</span>
        </div>
        <div class="scale_top_item3">
          <p>能源部门注册</p>
          <span>17</span>
        </div>
      </div>
      <div class="second_title">
        <div class="name">
          <p>按规模分</p>
        </div>
        <!-- <div class="solid"></div>
        <div class="dashed"><div></div><div></div></div> -->
      </div>
      <div class="scale_list">
        <div
          class="scale_item"
          v-for="(item, index) in skgmList"
          :key="index"
        >
          <div class="scale_item_name">
            <img :src="item.img" alt="" />
            <p>{{ item.name }}</p>
          </div>
          <div class="scale_item_num">{{ item.value }}<span>座</span></div>
        </div>
      </div>
    </div>
    <div class="sktj_box" ref="sktjHeight">
      <div class="second_title">
        <div class="name">
          <p>按功能坝型分</p>
        </div>
        <!-- <div class="solid"></div>
        <div class="dashed"><div></div><div></div></div> -->
      </div>
      <div class="sktj_wrap">
        <div class="sktj_left">
          <h3>功能</h3>
          <div class="sktj_left_list">
            <div class="sktj_left_list_item">
              <p>
                <img src="../../../../assets/images/fh-icon.png" alt="">
                <span>防洪</span>
              </p>
              <p>3074<span>座</span></p>
            </div>
            <div class="sktj_left_list_item">
              <p>
                <img src="../../../../assets/images/gg-icon.png" alt="">
                <span>灌溉</span>
              </p>
              <p>1996<span>座</span></p>
            </div>
            <div class="sktj_left_list_item">
              <p>
                <img src="../../../../assets/images/gs-icon.png" alt="">
                <span>供水</span>
              </p>
              <p>2138<span>座</span></p>
            </div>
            <div class="sktj_left_list_item">
              <p>
                <img src="../../../../assets/images/fd-icon.png" alt="">
                <span>发电</span>
              </p>
              <p>2511<span>座</span></p>
            </div>
            <div class="sktj_left_list_item">
              <p>
                <img src="../../../../assets/images/qt-icon.png" alt="">
                <span>其他</span>
              </p>
              <p>573<span>座</span></p>
            </div>
          </div>
        </div>
        <div class="sktj_right">
          <h3>坝型</h3>
          <div class="sktj_right_list">
            <div class="sktj_right_list_item">
              <p>
                <img src="../../../../assets/images/jzb-icon.png" alt="">
                <span>土石坝</span>
              </p>
              <p>3533<span>座</span></p>
            </div>
            <div class="sktj_right_list_item">
              <p>
                <img src="../../../../assets/images/gb-icon.png" alt="">
                <span>拱坝</span>
              </p>
              <p>432<span>座</span></p>
            </div>
            <div class="sktj_right_list_item">
              <p>
                <img src="../../../../assets/images/zlb-icon.png" alt="">
                <span>重力坝</span>
              </p>
              <p>236<span>座</span></p>
            </div>
            <div class="sktj_right_list_item">
              <p>
                <img src="../../../../assets/images/qt-icon.png" alt="">
                <span>其他</span>
              </p>
              <p>95<span>座</span></p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <bar-chart
      v-loading="skfbLoading"
      :style="{ height: barHeight }"
      class="skLeftChart"
      :chartData="skfbData"
      @barClick="barClickAdcd1"
    ></bar-chart>
  </div>
</template>

<script>
import BarChart from "../../components/BarChart";
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {
    BarChart,
  },
  data() {
    return {
      skfbData: {
        title: "按行政区划分",
        xShupai: true,
        id: "skfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        adnmList: [],
        unit: "座",
      },
      skfbLoading: true,

      skgmData: {
        title: "水库规模",
        id: "skgmChart",
        unit: "座",
        legend: {
          orient: "vertical",
          left: "65%",
          height: "80%",
        },
        // color: ['#E02020', '#EB7E65', '#F7C739', '#6F9DF2', '#8BBC89'],
        color: ["#F7C739", "#EB7E65", "#9dc87e", "#779fec"],
        series: [],
      },
      skgmLoading: true,
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
      scaleIndex: "",

      barHeight: "",
      pieHeight: "",
    };
  },
  
  mounted() {
    this.$nextTick(()=>{
      this.barHeight = 
        this.$parent.$refs.screenLeft.clientHeight 
        - this.$refs.scaleHeight.clientHeight 
        - this.$refs.sktjHeight.clientHeight  
        - this.$refs.titleHeight.clientHeight  
        + "px";
    })
    // this.getSkNumber();
    this.getSkfbData();
  },
  methods: {
    // 水库分布
    async getSkfbData() {
      let opt = {
        statistic: "adcd",
        adcd: this.$localData("get", "userInfo").adcd,
      };
      if (this.leftSelectProjectScale) {
        opt = {
          statistic: "adcd",
          adcd: this.$localData("get", "userInfo").adcd,
          itemCategory: "scale",
          itemValue: this.leftSelectProjectScale,
        };
      }
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        let xData = [];
        let yData = [];
        let adcdList = [];
        let adnmList = [];
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            adnmList.push(res.data[i].adnm);
            if (res.data[i].adnm.length > 2) {
              // xData.push(res.data[i].adnm.substring(0,res.data[i].adnm.length - 1))
              xData.push(res.data[i].adnm.replace(/市/, ""));
            } else {
              xData.push(res.data[i].adnm);
            }
            yData.push(res.data[i].num);
            adcdList.push(res.data[i].adcd);
          }
        }
        this.skfbData.xData = xData;
        this.skfbData.yData = yData;
        this.skfbData.adcdList = adcdList;
        this.skfbData.adnmList = adnmList;
        this.$store.commit("SET_RESERVOIR_LEFT_ADCD_DATA", res.data);
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.skfbLoading = false;
    },

    // 水库规模数量
    async getSkNumber() {
      let res = await reservoirLeftStatisticApi({
        statistic: "scale",
        adcd: this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        if (res.data.length) {
          let bigArr = [];
          let otherArr = [];
          for (let i = 0; i < res.data.length; i++) {
            switch (res.data[i].project_scale) {
              case "1":
                otherArr.push({
                  name: "大(1)型",
                  value: res.data[i].num,
                  project_scale: res.data[i].project_scale,
                  img: require("../../../../assets/images/dxsk.png"),
                });
                break;
              case "2":
                otherArr.push({
                  name: "大(2)型",
                  value: res.data[i].num,
                  project_scale: res.data[i].project_scale,
                  img: require("../../../../assets/images/dxsk.png"),
                });
                break;
              case "3":
                otherArr.push({
                  name: "中型",
                  value: res.data[i].num,
                  project_scale: res.data[i].project_scale,
                  img: require("../../../../assets/images/zxsk.png"),
                });
                break;
              case "4":
                otherArr.push({
                  name: "小(1)型",
                  value: res.data[i].num,
                  project_scale: res.data[i].project_scale,
                  img: require("../../../../assets/images/xxsk.png"),
                });
                break;
              case "5":
                otherArr.push({
                  name: "小(2)型",
                  value: res.data[i].num,
                  project_scale: res.data[i].project_scale,
                  img: require("../../../../assets/images/xxsk.png"),
                });
                break;
            }
          }
          // let bigVal = 0
          // for (let i = 0; i < bigArr.length; i++) {
          //   bigVal += bigArr[i]
          // }
          // this.skgmData.series = [{
          //   name: '大型',
          //   value: bigVal,
          //   project_scale: '-1',
          //   img: require('../../../../assets/images/sk-scale1.png')
          // },...otherArr]
          this.skgmData.series = otherArr;
          this.skgmList = this.skgmData.series;
          this.$store.commit(
            "SET_RESERVOIR_LEFT_SCALE_DATA",
            this.skgmData.series
          );
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.skgmLoading = false;
    },

    // 水库分布柱状图点击事件
    barClickAdcd1(index) {
      if (this.skfbData.xData[index] == "省级") {
        this.$store.commit("SET_LEFT_SELECT_ADCD", "33");
        this.$store.commit("SET_LEFT_SELECT_ADNM", "浙江省");
      } else {
        this.$store.commit(
          "SET_LEFT_SELECT_ADNM",
          this.skfbData.adnmList[index]
        );
        this.$store.commit(
          "SET_LEFT_SELECT_ADCD",
          this.skfbData.adcdList[index]
        );
      }
    },

    // 水库规模点击事件
    pieClick2(data) {
      this.$store.commit("SET_LEFT_SELECT_PROJECT_SCALE", data.project_scale);
    },

    clickScale(index) {
      if (this.scaleIndex === index) {
        this.scaleIndex = "";
        this.$store.commit("SET_LEFT_SELECT_PROJECT_SCALE", "all");
      } else {
        this.scaleIndex = index;
        this.$store.commit(
          "SET_LEFT_SELECT_PROJECT_SCALE",
          this.skgmList[index].project_scale
        );
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.map_left {
  height: 100%;
  z-index: 1002;
  .scale_box {
    padding-top: 16px;
    .scale_top{
      @include flexbox();
      @include flexJC(space-between);
      padding: 0 16px;
      &>div{
        height: 70px;
        background: #f0f4fd;
        border-top: 6px solid #193BC4;
        box-sizing: border-box;
        padding-left: 16px;
        &>p{
          color: #333;
          line-height: 24px;
          padding-top: 5px;
          padding-bottom: 5px;
        }
        &>span{
          font-size: 26px;
          font-weight: 500;
          color: #193BC4;
          line-height: 20px;
        }
        &.scale_top_item1{
          @include flex(1);
          margin-right: 8px;
        }
        &.scale_top_item2{
          @include flex(1.3);
          margin-right: 8px;
        }
        &.scale_top_item3{
          @include flex(1.3);
        }
      }
    }
    .scale_list {
      @include flexbox();
      @include flexAC();
      @include flexflow(row wrap);
      padding: 0 16px;
      .scale_item {
        background: #f8f8f8;
        border: 1px solid #d6d6d6;
        border-radius: 4px;
        height: 70px;
        margin-bottom: 5px;
        padding: 8px 0;
        box-sizing: border-box;
        margin-right: 5px;
        width: calc(33.33% - 5px);
        .scale_item_name {
          @include flexbox();
          @include flexAC();
          @include flexJC(center);
          // padding-left: 32px;
          font-size: 16px;
          img {
            margin-right: 10px;
            width: 22px;
            height: 22px;
          }
        }
        .scale_item_num {
          font-size: 26px;
          font-weight: 500;
          text-align: center;
          margin-top: 8px;
          span {
            font-size: 16px;
            padding-left: 5px;
          }
        }
        &.active {
          color: #fff;
          border: none;
          background: url("../../../../assets/images/scale_select.png")
            no-repeat center;
          background-size: cover;
        }
      }
    }
  }
  .sktj_box{
    .sktj_wrap{
      @include flexbox();
      @include flexJC(space-between);
      padding: 0 16px;
      .sktj_left{
        width: 218px;
        h3{
          height: 34px;
          text-align: center;
          background: #4B67D8;
          line-height: 34px;
          color: #fff;
          font-size: 16px;
        }
        .sktj_left_list{
          background: #f8f8f8;
          border: 1px solid #EBEBEB;
          border-top: none;
          padding: 10px 9px 4px 10px;
          .sktj_left_list_item{
            @include flexbox();
            margin-bottom: 6px;
            &>p:first-child{
              width: 53%;
              height: 42px;
              text-align: center;
              font-size: 18px;
              line-height: 42px;
              border-right: 1px solid #fff;
              box-sizing: border-box;
              border-top-left-radius: 21px;
              border-bottom-left-radius: 21px;
              @include flexbox();
              @include flexAI(center);
              background: #E2EFFF;
              color: #193BC4;
              &>img{
                width: 30px;
                height: 30px;
                margin: 0 10px 0 5px;
              }
              &>span{
                font-weight: 600;
              }
            }
            &>p:last-child{
              width: 47%;
              height: 42px;
              text-align: center;
              border-left: 1px solid #fff;
              box-sizing: border-box;
              font-size: 18px;
              line-height: 42px;
              font-weight: 500;
              color: #000;
              border-top-right-radius: 21px;
              border-bottom-right-radius: 21px;
              background: #E2EFFF;
              &>span{
                font-size: 15px;
                font-weight: 400;
                margin-left: 3px;
              }
            }
          }
        }
      }
      .sktj_right{
        width: 218px;
        h3{
          height: 34px;
          text-align: center;
          background: #4B67D8;
          line-height: 34px;
          color: #fff;
          font-size: 16px;
        }
        .sktj_right_list{
          background: #f8f8f8;
          border: 1px solid #EBEBEB;
          border-top: none;
          padding: 10px 9px 4px 9px;
          .sktj_right_list_item{
            @include flexbox();
            margin-bottom: 12px;
            &>p:first-child{
              width: 53%;
              height: 48px;
              text-align: center;
              font-size: 18px;
              line-height: 48px;
              border-right: 1px solid #fff;
              box-sizing: border-box;
              border-top-left-radius: 24px;
              border-bottom-left-radius: 24px;
              @include flexbox();
              @include flexAI(center);
              background: #E2EFFF;
              color: #193BC4;
              &>img{
                width: 37px;
                height: 37px;
                margin: 0 5px 0 5px;
              }
              &>span{
                font-weight: 600;
              }
            }
            &>p:last-child{
              width: 47%;
              height: 48px;
              text-align: center;
              border-left: 1px solid #fff;
              box-sizing: border-box;
              font-size: 18px;
              line-height: 48px;
              font-weight: 500;
              color: #000;
              border-top-right-radius: 24px;
              border-bottom-right-radius: 24px;
              background: #E2EFFF;
              &>span{
                font-size: 15px;
                font-weight: 400;
                margin-left: 3px;
              }
            }
          }
        }
      }
    }
  }
}
.skLeftChart {
  width: 100%;
  // height: var (--barChartHeight);
}
</style>
