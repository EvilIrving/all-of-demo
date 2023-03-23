<!--
 * @Author: hanyu
 * @LastEditTime: 2022-04-21 15:37:01
 * @Description: 地图左侧-统计
 * @FilePath: /rscp-big-screen/src/views/sgzgScreenNew/reservoirControl/reservoirControlLeft/ReservoirControlLeft.vue
-->
<template>
  <div class="map_left" >
    <div class="sgzg_title">
      <div class="sgzg_title_name">
        非常运行
      </div>
      <img src="@/assets/images/qtj-arr.png" alt="" />
    </div>
    <div class="flexbox">


      <div class="left_box">
        <div class="second_title_2">
          <div class="name">
            <p>正常坝<span>(一类坝)</span></p>
          </div>
          <div class="solid"></div>
          <div class="dashed"><div></div><div></div></div>
        </div>
        <div class="select_square" v-loading="loading_square1">
          <div
            :class="[
              'square_item',
              index === squareIndex1 ? 'active' : '',
            ]"
            v-for="(item, index) in squareList1"
            :key="index"
            @click="clickSquare1(index)"
          > 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img v-if="index == 0 && index !== squareIndex1" class="square_img_center" src="../../../../assets/images/csj.png" alt=""/>
                  <img v-if="index == 1 && index !== squareIndex1" class="square_img_center" src="../../../../assets/images/czc.png" alt=""/>
                  <img v-if="index == 2 && index !== squareIndex1" class="square_img_center" src="../../../../assets/images/cxz.png" alt=""/>
                  <img v-if="index === squareIndex1" class="square_img_center" src="../../../../assets/images/c-active.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <p>{{ item.name }}</p>
                <span v-if="index == 0" style="color: #FF3E09">{{ item.value ? item.value : 0 }}<i>座</i></span>
                <span v-if="index == 1" style="color: #FF7509">{{ item.value ? item.value : 0 }}<i>座</i></span>
                <span v-if="index == 2" style="color: #FF9D09">{{ item.value ? item.value : 0 }}<i>座</i></span>
              </div>
            </div>
            
          </div>
        </div>
      </div>


      <div class="left_box">
        <div class="second_title_2">
          <div class="name">
            <p>病害坝<span>(二类坝)</span></p>
          </div>
          <div class="solid"></div>
          <div class="dashed"><div></div><div></div></div>
        </div>
        <div class="select_square" v-loading="loading_square2">
          <div
            :class="[
              'square_item',
              index === squareIndex2 ? 'active' : '',
            ]"
            v-for="(item, index) in squareList2"
            :key="index"
            @click="clickSquare2(index)"
          > 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img v-if="index == 0 && index !== squareIndex2" class="square_img_center" src="../../../../assets/images/csj.png" alt=""/>
                  <img v-if="index == 1 && index !== squareIndex2" class="square_img_center" src="../../../../assets/images/czc.png" alt=""/>
                  <img v-if="index == 2 && index !== squareIndex2" class="square_img_center" src="../../../../assets/images/cxz.png" alt=""/>
                  <img v-if="index === squareIndex2" class="square_img_center" src="../../../../assets/images/c-active.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <p>{{ item.name }}</p>
                <span v-if="index == 0" style="color: #FF3E09">{{ item.value ? item.value : 0 }}<i>座</i></span>
                <span v-if="index == 1" style="color: #FF7509">{{ item.value ? item.value : 0 }}<i>座</i></span>
                <span v-if="index == 2" style="color: #FF9D09">{{ item.value ? item.value : 0 }}<i>座</i></span>
              </div>
            </div>
            
          </div>
        </div>
      </div>


      <div class="left_box">
        <div class="second_title_2">
          <div class="name">
            <p>危险坝<span>(三类坝)</span></p>
          </div>
          <div class="solid"></div>
          <div class="dashed"><div></div><div></div></div>
        </div>
        <div class="select_square" v-loading="loading_square3">
          <div
            :class="[
              'square_item',
              index === squareIndex3 ? 'active' : '',
            ]"
            v-for="(item, index) in squareList3"
            :key="index"
            @click="clickSquare3(index)"
          > 
            <div class="square_center_box">
              <div class="square_img_box">
                <div class="square_img">
                  <img v-if="index == 0 && index !== squareIndex3" class="square_img_center" src="../../../../assets/images/csj.png" alt=""/>
                  <img v-if="index == 1 && index !== squareIndex3" class="square_img_center" src="../../../../assets/images/czc.png" alt=""/>
                  <img v-if="index == 2 && index !== squareIndex3" class="square_img_center" src="../../../../assets/images/cxz.png" alt=""/>
                  <img v-if="index === squareIndex3" class="square_img_center" src="../../../../assets/images/c-active.png" alt=""/>
                </div>
              </div>
              <div class="square_bottom">
                <p>{{ item.name }}</p>
                <span v-if="index == 0" style="color: #FF3E09">{{ item.value ? item.value : 0 }}<i>座</i></span>
                <span v-if="index == 1" style="color: #FF7509">{{ item.value ? item.value : 0 }}<i>座</i></span>
                <span v-if="index == 2" style="color: #FF9D09">{{ item.value ? item.value : 0 }}<i>座</i></span>
              </div>
            </div>
            
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {
  },
  data() {
    return {
      squareList1: [],
      squareIndex1: 0,
      loading_square1: true,
      squareList2: [],
      squareIndex2: "",
      loading_square2: true,
      squareList3: [],
      squareIndex3: "",
      loading_square3: true,
      squareValue: "DSFLZ",
      opt: {
        statistic: "mapList",
        itemCategory: "",
        itemValue: "",
        adcd: this.$localData("get", "userInfo").adcd,
      },
    };
  },
  
  mounted() {
    this.getSquareData1()
    this.getSquareData2()
    this.getSquareData3()
    this.opt.itemCategory = "firstClass," + this.squareValue;
    this.opt.itemValue = "一类坝,1"
    this.$nextTick(()=>{
      this.getReservoirList()
    })
  },
  methods: {
    // 运行情况
    async getSquareData1() {
      this.loading_square1 = true;
      this.squareList1 = [];
      let res = await reservoirLeftStatisticApi({
        statistic: "overCnt",
        adcd: this.$localData("get", "userInfo").adcd,
        itemCategory: "firstClass",
        itemValue: "一类坝"
      });
      if (res.success) {
        this.squareList1.push(
          {
            type: "DSFLZ",
            name: "超设计水位",
            value: res.data[0]["DSFLZ"],
          },
          {
            type: "NWL",
            name: "超正常水位",
            value: res.data[0]["NWL"],
          },
          {
            type: "limitWaterLevel",
            name: "超限制水位",
            value: res.data[0]["limitWaterLevel"],
          }
        );
      }
      this.loading_square1 = false;
    },
    async getSquareData2() {
      this.loading_square2 = true;
      this.squareList2 = [];
      let res = await reservoirLeftStatisticApi({
        statistic: "overCnt",
        adcd: this.$localData("get", "userInfo").adcd,
        itemCategory: "secondClass",
        itemValue: "二类坝"
      });
      if (res.success) {
        this.squareList2.push(
          {
            type: "DSFLZ",
            name: "超设计水位",
            value: res.data[0]["DSFLZ"],
          },
          {
            type: "NWL",
            name: "超正常水位",
            value: res.data[0]["NWL"],
          },
          {
            type: "limitWaterLevel",
            name: "超限制水位",
            value: res.data[0]["limitWaterLevel"],
          }
        );
      }
      this.loading_square2 = false;
    },
    async getSquareData3() {
      this.loading_square3 = true;
      this.squareList3 = [];
      let res = await reservoirLeftStatisticApi({
        statistic: "overCnt",
        adcd: this.$localData("get", "userInfo").adcd,
        itemCategory: "thirdClass",
        itemValue: "三类坝"
      });
      if (res.success) {
        this.squareList3.push(
          {
            type: "DSFLZ",
            name: "超设计水位",
            value: res.data[0]["DSFLZ"],
          },
          {
            type: "NWL",
            name: "超正常水位",
            value: res.data[0]["NWL"],
          },
          {
            type: "limitWaterLevel",
            name: "超限制水位",
            value: res.data[0]["limitWaterLevel"],
          }
        );
      }
      this.loading_square3 = false;
    },
    clickSquare1(index) {
      this.squareIndex1 = index;
      this.squareIndex2 = "";
      this.squareIndex3 = "";
      this.squareValue = this.squareList1[index].type;
      this.opt.itemCategory = "firstClass," + this.squareValue;
      this.opt.itemValue = "一类坝,1"
      this.getReservoirList();
    },
    clickSquare2(index) {
      this.squareIndex1 = "";
      this.squareIndex2 = index;
      this.squareIndex3 = "";
      this.squareValue = this.squareList2[index].type;
      this.opt.itemCategory = "secondClass," + this.squareValue;
      this.opt.itemValue = "二类坝,1"
      this.getReservoirList();
    },
    clickSquare3(index) {
      this.squareIndex1 = "";
      this.squareIndex2 = "";
      this.squareIndex3 = index;
      this.squareValue = this.squareList3[index].type;
      this.opt.itemCategory = "thirdClass," + this.squareValue;
      this.opt.itemValue = "三类坝,1"
      this.getReservoirList();
    },
    async getReservoirList() {
      this.$parent.$refs.reservoirMap.removeAllLayer();
      this.$parent.$refs.reservoirMap.closePopup();
      this.$parent.$refs.reservoirMap.initView();
      let res = await reservoirLeftStatisticApi(this.opt);
      if (res.success) {
        let pointArr = [];
        let pointObj = {
          yellow: [],
          orange: [],
          red: []
        };
        if (res.data.length) {
          let colorType = "";
          if (this.squareValue === 'DSFLZ') {
            colorType = "red";
          } else if (this.squareValue === 'NWL') {
            colorType = "orange";
          } else if (this.squareValue === 'limitWaterLevel') {
            colorType = "yellow";
          }
          res.data.map((v) => {
            v.lat = v.LTTD;
            v.lng = v.LGTD;
            v.rightType = "1";
            v.src = require("@/assets/images/map/reservoir.png");
            v.colorType = colorType;
            console.log(this.squareValue,'====squareValue===');
            if (this.squareIndex === "") {
                v.limitWaterLevel !== null &&
                v.waterLevel !== null &&
                v.NWL != null &&
                v.limitWaterLevel < v.waterLevel &&
                v.waterLevel <= v.NWL
                  ? (v.colorType = "blue")
                  : "";

                v.DSFLZ !== null &&
                v.waterLevel !== null &&
                v.NWL != null &&
                v.NWL < v.waterLevel &&
                v.waterLevel <= v.DSFLZ
                  ? (v.colorType = "yellow")
                  : "";

                v.DSFLZ !== null &&
                v.waterLevel !== null &&
                v.waterLevel > v.DSFLZ
                  ? (v.colorType = "orange")
                  : "";
            }
            if (v.colorType) {
              pointObj[v.colorType].push(v);
            }
          });
          
          pointArr = [
            ...pointObj["red"],
            ...pointObj["orange"],
            ...pointObj["yellow"],
          ];
        }
        console.log(pointArr,'--pointArr--');
        this.$parent.showMapPoints(pointArr);
      }
    },
  }
};
</script>

<style lang="scss" scoped>
.map_left {
  height: 100%;
  z-index: 1002;
  .flexbox{
    height: calc(100% - 48px);
    @include flexbox();
    flex-direction: column;
    box-sizing: border-box;
    &>div{
      @include flex(1);
      &.left_box{
        height: 100%;
        .select_square {
          height: calc(100% - 42px);
          box-sizing: border-box;
          padding: 0 16px 16px 16px;
          @include flexbox();
          @include flexAI(center);
          @include flexJC(space-between);
          .square_item {
            @include flexbox();
            @include flexAI(center);
            @include flexJC(center);
            box-sizing: border-box;
            width: 140px;
            height: 80%;
            text-align: center;
            background-color: #F8F8F8;
            background-image: url('../../../../assets/images/c-bg.png');
            background-repeat: no-repeat;
            background-position: right bottom;
            background-size: 40% 40%;
            border: 1px solid #FF9D09;
            cursor: pointer;
            border-radius: 4px;
            // margin-bottom: 8px;
            &:nth-child(2){
              &.active {
                background: #FF3E09 !important;
                border: 1px solid #FF3E09 !important;
              }
            }
            &:nth-child(3){
              &.active {
                background: #FF7509 !important;
                border: 1px solid #FF7509 !important;
              }
            }
            &:nth-child(4){
              &.active {
                background: #FF9D09 !important;
                border: 1px solid #FF9D09 !important;
              }
            }
            &.active {
              .square_bottom {
                & > p {
                  color: #fff !important;
                }
                & > span {
                  color: #fff !important;
                }
                i {
                  color: #fff !important;
                }
              }
            }
            .square_center_box{
              .square_img_box {
                padding-top: 5px;
                .square_img {
                  margin: 0 auto;
                  width: 55px;
                  height: 55px;
                  position: relative;
                  .square_img_circle {
                    width: 100%;
                    height: 100%;
                    // -webkit-animation: imgCircle 2s linear 2s 5 alternate;
                    // animation: imgCircle 2s linear infinite;
                  }
                  .square_img_center {
                    width: 64px;
                    height: 64px;
                  }
                }
              }
              .square_bottom {
                & > p {
                  line-height: 24px;
                  font-size: 18px;
                  color: #000;
                  padding: 24px 0 12px 0;
                }
                i {
                  padding-left: 4px;
                  font-size: 16px;
                  font-weight: 400;
                  color: #333333;
                  line-height: 24px;
                }
                & > span{
                  font-size: 34px;
                  font-weight: 500;
                  color: #FF7509;
                  line-height: 24px;
                }
              
              }
            }
            
          }
        }
      }
    }
  }
  .second_title_2{
    @include flexbox();
    @include flexAI(center);
    padding: 16px;
    .name{
      width: 150px;
      box-sizing: border-box;
      padding-left: 6px;
      border-left: 4px solid #2D58FF;
      border-right: 4px solid #2D58FF;
      p{
        font-size: 18px;
        font-weight: 600;
        color: #2D58FF;
        span{
          font-size: 18px;
          color: #999;
          font-weight: 600;
        }
      }
    }
    .solid{
      width: calc(100% - 150px);
      height: 2px;
      background: #2D58FF;
    }
    .dashed{
      width: 22px;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(space-between);
      &>div{
        height: 2px;
        width: 8px;
        background: #2D58FF;
        margin-left: 4px;
      }
    }
  }
}
</style>
