<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-20 09:12:29
 * @Description: 地图左侧-统计
 * @FilePath: /rscp-big-screen/src/views/sgzgScreen/intelligenceUse/intelligenceUseLeft/IntelligenceUseLeft.vue
-->
<template>
  <div class="map_left">
    <div class="prty_box" ref="projectHeight">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        工程类型
      </div>
      <div class="prty_list">
        <div
          :class="['prty_item', index == prtyIndex ? 'active' : '']"
          v-for="(item, index) in prtyList"
          :key="index"
          @click="clickPrty(index)"
        >
          {{item}}
        </div>
      </div>
    </div>
    <div class="scale_box" v-loading="skgmLoading" ref="reservoirHeight">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        工程规模
      </div>
      <div class="scale_list">
        <div
          :class="[
            'scale_item',
            index === scaleIndex ? 'active' : '',
            index == 0 || index == 1 ? 'width50' : 'width33',
          ]"
          v-for="(item, index) in skgmList"
          :key="index"
          @click="clickScale(index)"
        >
          <div class="scale_item_name">
            <img :src="item.img" alt="" />
            <p>{{ item.name }}</p>
          </div>
          <div class="scale_item_num">{{ item.value }}<span>{{leftProjectType == '堤防' ? '条' : '座'}}</span></div>
        </div>
      </div>
    </div>
    <div class="chart_line"></div>
    <bar-chart
      v-loading="skfbLoading"
      :style="{ height: barHeight }"
      class="skLeftChart"
      :chartData="skfbData"
      @barClick="barClickAdcd1"
    ></bar-chart>
    <div class="chart_line"></div>
    <bar-chart-no-click
      v-loading="basinLoading"
      :style="{ height: barHeight }"
      class="skLeftChart"
      :chartData="basinData"
    ></bar-chart-no-click>
  </div>
</template>

<script>
import BarChart from "@/components/BarChart";
import BarChartDouble from "@/components/BarChartDouble";
import BarChartNoClick from "@/components/BarChartNoClick";
import PieChart from "@/components/PieChart";
import { reservoirLeftStatisticApi, projectBasinDataApi } from "@/api/api_reservoir";
import { basinStatisticApi } from "@/api/api_basin";
export default {
  name: "",
  props: {},
  components: {
    BarChart,
    PieChart,
    BarChartDouble,
    BarChartNoClick
  },
  data() {
    return {
      prtyIndex: 0,
      prtyList: ['水库', '堤防', '泵站', '水闸', '闸站'],
      leftProjectType: '水库',
      skfbData: {
        title: "工程数量（座）",
        xShupai: false,
        id: "skfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        adnmList: [],
        unit: "座",
      },
      skfbLoading: true,

      basinData: {
        title: "流域水系（座/条）",
        xShupai: false,
        id: "basinChart",
        xData: [],
        yData: [],
        unit: "座",
      },
      basinLoading: true,

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
      skgmList: [],
      scaleIndex: "",

      barHeight: "",
      pieHeight: "",
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    },
  },
  watch: {
    leftSelectAdcd(val){
      this.getProjectBasinData()
    },
    leftSelectProjectScale(val) {
      this.getSkfbData();
      this.getProjectBasinData();
    },
  },
  mounted() {
    this.barHeight = (window.innerHeight - this.$refs.reservoirHeight.clientHeight - this.$refs.projectHeight.clientHeight) / 2 + "px";
    this.getSkjbtjData();
    this.getSkfbData();
    this.getProjectBasinData();
  },
  methods: {
    // 水库规模
    async getSkjbtjData() {
      // let res = await reservoirLeftStatisticApi({
      //   statistic: "scale",
      //   adcd: this.$localData("get", "userInfo").adcd,
      // });
      this.skgmLoading = true
      let res = await basinStatisticApi({
        statistic: "typeScale",
        adcd: this.$localData("get", "userInfo").adcd,
        projectCategory: this.leftProjectType,
      })
      if (res.success) {
        if (res.data.length) {
          let bigArr = [];
          let otherArr = [];
          for (let i = 0; i < res.data.length; i++) {
            switch (res.data[i].projectScale) {
              case "1":
                otherArr.push({
                  name: this.leftProjectType == "堤防" ? "一级" : "大(1)型",
                  value: res.data[i].num,
                  project_scale: res.data[i].projectScale,
                  img: require("../../../../assets/images/sk-scale1.png"),
                });
                break;
              case "2":
                otherArr.push({
                  name: this.leftProjectType == "堤防" ? "二级" : "大(2)型",
                  value: res.data[i].num,
                  project_scale: res.data[i].projectScale,
                  img: require("../../../../assets/images/sk-scale1.png"),
                });
                break;
              case "3":
                otherArr.push({
                  name: this.leftProjectType == "堤防" ? "三级" : "中型",
                  value: res.data[i].num,
                  project_scale: res.data[i].projectScale,
                  img: require("../../../../assets/images/sk-scale2.png"),
                });
                break;
              case "4":
                otherArr.push({
                  name: this.leftProjectType == "堤防" ? "四级" : "小(1)型",
                  value: res.data[i].num,
                  project_scale: res.data[i].projectScale,
                  img: require("../../../../assets/images/sk-scale3.png"),
                });
                break;
              case "5":
                otherArr.push({
                  name: this.leftProjectType == "堤防" ? "五级" : "小(2)型",
                  value: res.data[i].num,
                  project_scale: res.data[i].projectScale,
                  img: require("../../../../assets/images/sk-scale3.png"),
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

    // 水库分布
    async getSkfbData() {
      this.skfbLoading = true;
      // let opt = {
      //   statistic: "adcd",
      //   adcd: this.$localData("get", "userInfo").adcd,
      // };
      // if (this.leftSelectProjectScale) {
      //   opt = {
      //     statistic: "adcd",
      //     adcd: this.$localData("get", "userInfo").adcd,
      //     itemCategory: "scale",
      //     itemValue: this.leftSelectProjectScale,
      //   };
      // }
      // let res = await reservoirLeftStatisticApi(opt);

      let res = await basinStatisticApi({
        statistic: "adcd",
        adcd: this.$localData("get", "userInfo").adcd,
        projectCategory: this.leftProjectType,
        projectScale: this.leftSelectProjectScale
      })
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
            yData.push(res.data[i].total);
            adcdList.push(res.data[i].adcd);
          }
        }
        this.skfbData.xData = xData;
        this.skfbData.yData = yData;
        this.skfbData.adcdList = adcdList;
        this.skfbData.adnmList = adnmList;
        this.$store.commit("SET_RESERVOIR_LEFT_ADCD_DATA", res.data);
      }
      this.skfbLoading = false;
    },

    // 流域水系（座/条）
    async getProjectBasinData(){
      this.basinLoading = true;
      let res = await projectBasinDataApi({
        statistic: "adcd",
        adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData("get", "userInfo").adcd,
        projectCategory: this.leftProjectType,
        projectScale: this.leftSelectProjectScale
      })
      if(res.success){
        let xData = [];
        let yData = [];
        if (res.rows.length) {
          for (let i = 0; i < res.rows.length; i++) {
            xData.push(res.rows[i].basinName);
            yData.push(res.rows[i].proNum);
          }
        }
        this.basinData.xData = xData;
        this.basinData.yData = yData;
      }
      this.basinLoading = false;
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

    clickPrty(index){
      this.prtyIndex = index;
      this.leftProjectType = this.prtyList[index]
      //重置
      this.scaleIndex = ''
      this.$store.commit("SET_LEFT_SELECT_PROJECT_SCALE", "all");
      this.$store.commit("SET_LEFT_SELECT_ADCD", '');
      this.$store.commit("SET_LEFT_SELECT_ADNM", "浙江省");
      //end
      this.$emit('selectProjectType', this.leftProjectType)
      this.getSkjbtjData()
      this.getSkfbData()
      this.getProjectBasinData()
    }
  },
};
</script>

<style lang="scss" scoped>
.map_left {
  z-index: 1002;
  .prty_box{
    height: 80px;
    box-sizing: border-box;
    .prty_list{
      @include flexbox();
      @include flexAC();
      @include flexJC(space-between);
      padding: 0 16px;
      .prty_item{
        width: 64px;
        height: 32px;
        box-sizing: border-box;
        text-align: center;
        line-height: 32px;
        background: #F8F8F8;
        border: 1px solid #D6D6D6;
        border-radius: 4px;
        cursor: pointer;
        &.active{
          color: #fff;
          border: none;
          background: url("../../../../assets/images/scale_select.png")
            no-repeat center;
          background-size: cover;
        }
      }
    }
  }
  .scale_box {
    height: 260px;
    .scale_list {
      @include flexbox();
      @include flexAC();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      padding: 0 16px;
      .scale_item {
        background: #f8f8f8;
        border: 1px solid #d6d6d6;
        border-radius: 4px;
        height: 96px;
        margin-bottom: 10px;
        cursor: pointer;
        padding: 14px 0;
        box-sizing: border-box;
        &.width50 {
          width: 48.8%;
        }
        &.width33 {
          width: 32%;
          .scale_item_name {
            font-size: 16px;
            img {
              width: 22px;
              height: 22px;
            }
          }
          .scale_item_num {
            font-size: 26px;
            margin-top: 13px;
          }
        }
        .scale_item_name {
          @include flexbox();
          @include flexAC();
          @include flexJC(center);
          // padding-left: 32px;
          font-size: 18px;
          img {
            margin-right: 10px;
            width: 25px;
            height: 25px;
          }
        }
        .scale_item_num {
          font-size: 32px;
          font-weight: 500;
          text-align: center;
          margin-top: 7px;
          span {
            font-size: 22px;
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
}
.skLeftChart {
  width: 100%;
  // height: var (--barChartHeight);
}
</style>
