
<template>
  <div class="warp">
    <div class="chart_title chart_title-ht" style="height: 25px">
      <img src="../../../../assets/images/chart_item_icon.png" alt="" />
      海塘概况
    </div>
    <section class="top_box">
      <ul class="aqjd_box">
        <li class="aqjd_item" v-for="(item, index) in htStatistics" :key="index">
          <img :src="item.src" alt="" />
          <div>
            <!-- <p>{{ item.title }}</p> -->
            <p>
              <span class="num">{{ item.num }}</span>
              <span class="unit">{{ item.unit }}</span>
            </p>
          </div>
        </li>
      </ul>
      <ul class="select_square">
        <li :class="['square_item', index === htTabsActive ? 'active' : '']" v-for="(item, index) in htTabs"
          :key="index" @click="clickSquare(index)">
          <p>{{ item.name }}</p>
        </li>
      </ul>
      <!-- 分布 -->
      <bar-chart v-if="htTabsActive === 0" v-loading="htfbLoading" class="htfbChart" :chartData="htfbData"
        @barClick="barClickAdcd1"></bar-chart>
      <!-- 长度 -->
      <bar-chart v-if="htTabsActive === 1" v-loading="htglsLoading" class="htfbChart" :chartData="htglsData"
        @barClick="barClickAdcd2"></bar-chart>
      <!-- 级别 -->
      <pie-chart v-if="htTabsActive === 2" v-loading="htjbtjLoading" class="htfbChart" :chartData="htjbtjData"
        @pieClick="pieClick2"></pie-chart>
    </section>
    <div class="chart_title chart_title-ht" style="height: 25px">
      <img src="../../../../assets/images/chart_item_icon.png" alt="" />
      沿塘设施
    </div>
    <section class="middle_box">
      <ul class="select_square">
        <li :class="[
            'square_item',
            index === htFacilityOneActive ? 'active' : '',
            index === 2 ? 'not-allow' : '',
          ]" v-for="(item, index) in htFacilityOneTabs" :key="index" @click="clickSquareThree(index)">
          <p>{{ item.name }}</p>
        </li>
      </ul>
      <div class="middle_box_list">
        <div class="control-one-list" v-show="htFacilityOneActive === 0">
          <div class="control-one-item" :class="{}" v-for="(item, index) in oneList" :key="index">
            <img :src="item.imgUrl" alt="" />
            <p>{{ item.name }}</p>
            <div>
              <p>
                <span>{{ item.num }}</span>座
              </p>
            </div>
          </div>
        </div>
        <div class="control-one-list" v-show="htFacilityOneActive === 1">
          <div class="control-one-item" :class="{}" v-for="(item, index) in twoList" :key="index">
            <img :src="item.imgUrl" alt="" />
            <p>{{ item.name }}</p>
            <div>
              <p>
                <span>{{ item.num }}</span>座
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>
    <div class="chart_title chart_title-ht" style="height: 25px">
      <img src="../../../../assets/images/chart_item_icon.png" alt="" />
      工程施工
    </div>
    <section class="third_box">
      <ul class="select_square between">
        <li :class="[
            'square_item between',
            index === htFacilityTwoActive ? 'active' : '',
          ]" v-for="(item, index) in htFacilityTwoTabs" :key="index" @click="clickSquareTwo(index)">
          <p>{{ item.name }}</p>
        </li>
      </ul>
      <bar-chart v-loading="htchartDataLoading" class="htfbChart" :chartData="chartData"></bar-chart>
    </section>
    <div class="chart_title chart_title-ht" style="height: 25px">
      <img src="../../../../assets/images/chart_item_icon.png" alt="" />
      安全鉴定
    </div>
    <section class="fourth_box">
      <ul class="tab_box">
        <li>
          <p>总数</p>
          <div class="tab_box_item">
            <span class="ba_text">二类塘</span>
            <span class="ba_num">
              {{ cxjgStaticData.elwkg }}
              <span class="ba_unit">条</span>
            </span>
          </div>
          <div class="tab_box_item">
            <span class="ba_text">三类塘</span>
            <span class="ba_num">
              {{ cxjgStaticData.slwkg }}
              <span class="ba_unit">条</span>
            </span>
          </div>
        </li>
        <li>
          <p>未加固</p>
          <div class="tab_box_item">
            <span class="ba_text">二类塘</span>
            <span class="ba_num">
              {{ cxjgStaticData.elykg }}
              <span class="ba_unit">条</span>
            </span>
          </div>
          <div class="tab_box_item">
            <span class="ba_text">三类塘</span>
            <span class="ba_num">
              {{ cxjgStaticData.slywg }}
              <span class="ba_unit">条</span>
            </span>
          </div>
        </li>
        <li>
          <p>加固中</p>
          <div class="tab_box_item">
            <span class="ba_text">二类塘</span>
            <span class="ba_num">
              {{ cxjgStaticData.eldnwg }}
              <span class="ba_unit">条</span>
            </span>
          </div>
          <div class="tab_box_item">
            <span class="ba_text">三类塘</span>
            <span class="ba_num">
              {{ cxjgStaticData.sldnwg }}
              <span class="ba_unit">条</span>
            </span>
          </div>
        </li>
      </ul>
    </section>
  </div>
</template>

<script>
  import BarChart from "../components/BarChart";
  import PieChart from "../components/PieChartLabel.vue";
  import { seawallLeftStatisticApi, zhSeawall, seawallProIndo } from "@/api/api_seawall";
  export default {
    name: "",
    props: {},
    components: {
      BarChart,
      PieChart,
    },
    computed: {
      leftSelectAdcd() {
        return this.$store.state.leftSelectAdcd;
      },
      leftSelectPreventLevel() {
        return this.$store.state.leftSelectPreventLevel;
      },
      leftSelectProjectScale() {
        return this.$store.state.leftSelectProjectScale;
      },
    },
    watch: {
      leftSelectAdcd(val) {
        this.getColorSeawallList();
      },
      leftSelectPreventLevel(val) {
        this.getColorSeawallList();
      },
      leftSelectProjectScale(val) {
        this.getColorSeawallList();
      },
    },
    data() {
      return {
        htStatistics: [
          {
            name: "",
            num: 815,
            unit: "条",
            title: "条数",
            src: require("../../../../assets/images/sl_number.png"),
          },
          {
            name: "",
            num: 2014,
            unit: "km",
            title: "长度",
            src: require("../../../../assets/images/sl_length.png"),
          },
        ], //海塘概况 统计
        htTabsActive: 0,
        htTabs: [
          { name: "分布", sort: 0 },
          { name: "长度", sort: 1 },
          { name: "等级", sort: 2 },
        ], //海塘概况 tab栏
        htFacilityOneActive: 0,
        htFacilityOneTabs: [
          { name: "沿塘闸站", sort: 0 },
          { name: "其他口门", sort: 1 },
          { name: "交叉管线", sort: 1 },
        ], // 沿塘设施 第一个
        htFacilityTwoActive: 0,
        htFacilityTwoTabs: [
          { name: "安澜工程", sort: 0 },
          { name: "涉塘工程", sort: 1 },
        ], // 沿塘设施 第二个
        oneActiveItem: 0,
        oneList: [
          // {
          //   imgUrl: require("@/assets/images/control-one-a.png"),
          //   name: "大中型闸站",
          //   num: 68,
          // },
          {
            imgUrl: require("@/assets/images/control-one-b.png"),
            name: "水闸",
            num: 1370,
          },
          {
            imgUrl: require("@/assets/images/control-one-c.png"),
            name: "泵站",
            num: 78,
          },
          {
            imgUrl: require("@/assets/images/control-one-d.png"),
            name: "闸站",
            num: 80,
          },
        ],
        twoList: [
          {
            name: "旱闸",
            num: 451,
            imgUrl: require("@/assets/images/control-two-a.png"),
          },
          {
            name: "缺口",
            num: 251,
            imgUrl: require("@/assets/images/control-two-b.png"),
          },
          {
            name: "道口",
            num: 462,
            imgUrl: require("@/assets/images/control-two-c.png"),
          },
          // {
          //   name: "涵闸",
          //   num: 229,
          //   imgUrl: require("@/assets/images/control-two-d.png"),
          // },
        ],


        htfbData: {
          noTitle: true,
          title: "海塘数量(条)",
          id: "htfbChart",
          xData: [],
          yData: [],
          adcdList: [],
          unit: "条",
        },
        htfbLoading: true,

        htjbtjData: {
          noTitle: true,
          title: "海塘等级",
          id: "htjbtjChart",
          unit: "条",
          legend: {
            orient: "vertical",
            left: "65%",
            height: "80%",
          },
          color: [
            "#193CC5",
            "#4C71FF",
            "#7692FFFF",
            "#ACB5FFFF",
            "#E3E4FFFF",
            "#E3E4FFFF",
          ],
          series: [],
        },
        htjbtjLoading: true,

        htglsData: {
          noTitle: true,
          title: "海塘长度(km)",
          id: "htglsChart",
          xData: [],
          yData: [],
          adcdList: [],
          unit: "公里",
          noWords: true,
        },
        htglsLoading: true,
        cxjgStaticData: {},

        chartData: {
          noTitle: true,
          title: "安澜工程",
          id: "htAnLanChart",
          xData: ["杭州", "宁波", "温州", "嘉兴", "绍兴", "舟山", "台州"],
          yData: [2, 24, 9, 3, 1, 24, 27],
          adcdList: [],
          unit: "条",
        },
        htchartDataLoading: false,
        htAnLanData: {
          noTitle: true,
          title: "安澜工程",
          id: "htAnLanChart",
          xData: ["杭州", "宁波", "温州", "嘉兴", "绍兴", "舟山", "台州"],
          yData: [2, 24, 9, 3, 1, 24, 27],
          adcdList: [],
          unit: "条",
        },
        htSheTData: {
          noTitle: true,
          title: "涉塘工程",
          id: "htSheTChart",
          xData: ["杭州", "宁波", "嘉兴", "绍兴"],
          yData: [3, 1, 1, 1],
          adcdList: [],
          unit: "条",
        },

        barHeight: "",
        SecondbarHeight: "",
        pieHeight: "",

        pointArr: [],
      };
    },
    mounted() {
      // this.barHeight = window.innerHeight / 2 * 15 / (15 + 19) + 'px'
      // this.pieHeight = window.innerHeight / 2 * 19 / (15 + 19) + 'px'
      // this.barHeight = window.innerHeight * 7 / 24 + 'px'
      // this.pieHeight = window.innerHeight * 5 / 12 + 'px'
      this.getHtfbData();
      this.getHtglsData();
      this.getHtjbtjData();
      this.clickSquareTwo(0);
      this.getColorSeawallList();
      this.getXzyyData();
    },
    beforeDestroy() {
      window.removeEventListener("resize");
    },
    methods: {
      resizeHeight() {
        if (window.innerHeight > 1000) {
          this.barHeight = window.innerHeight / 3.5 + "px";
          this.SecondbarHeight = window.innerHeight / 3.5 + "px";
        } else {
          this.barHeight = window.innerHeight / 4 + "px";
          this.SecondbarHeight = window.innerHeight / 4 + "px";
        }
      },
      clickSquare(index) {
        this.htTabsActive = index;
        switch (index) {
          case 1:
            this.getHtglsData();
            break;
          case 2:
            this.getHtjbtjData();
            break;
          default:
            this.getHtfbData();
            break;
        }
      },
      clickSquareThree(index) {
        if (index === 2) {
          return;
        }
        this.htFacilityOneActive = index;
      },
      clickSquareTwo(index) {
        if (index === 2) {
          return;
        }
        this.htchartDataLoading = true;
        this.htFacilityTwoActive = index;
        switch (index) {
          case 1:
            this.chartData = this.htSheTData;
            break;
          default:
            this.chartData = this.htAnLanData;
            break;
        }
        this.htchartDataLoading = false;
      },
      //  三色码 柱状图
      async getHtfbData() {
        this.htfbLoading = true;
        let res = await zhSeawall.seawallthreeYardBarChart({
          adcd: this.leftSelectAdcd
            ? this.leftSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          userAdcd: this.leftSelectAdcd
            ? this.leftSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          projectScale: this.$store.state.leftSelectProjectScale,
        });
        if (res.success) {
          let xData = [];
          let yData = [];
          let adcdList = [];
          if (res.rows.length > 0) {
            res.rows.forEach((element) => {
              if (element.red + element.yellow + element.green !== 0) {
                if (element.adnm.length > 2) {
                  xData.push(element.adnm.replace(/市/, ""));
                } else {
                  xData.push(element.adnm);
                }
                yData.push(element.total);
                adcdList.push(element.adcd);
              }
            });
          }
          this.htfbData.xData = xData;
          this.htfbData.yData = yData;
          this.htfbData.adcdList = adcdList;
        } else {
          this.$message.error(res.msg ? res.msg : "请求失败");
        }
        this.htfbLoading = false;
      },

      // 除险加固
      async getXzyyData() {
        let opt = {
          adcd: this.leftSelectAdcd,
        };
        let res = await zhSeawall.reinforcementSta(opt);
        if (res.success) {
          this.cxjgStaticData = res.data;
        }
      },

      // 海塘公里数统计
      async getHtglsData() {
        this.htglsData = {
          noTitle: true,
          title: "海塘长度(km)",
          id: "htglsChart",
          xData: [],
          yData: [],
          adcdList: [],
          unit: "公里",
          noWords: true,
        };
        let res = await seawallLeftStatisticApi({
          statistic: "length",
          adcd: this.$localData("get", "userInfo").adcd,
        });
        if (res.success) {
          let num = 0;
          if (res.data.length) {
            for (let i = 0; i < res.data.length; i++) {
              if (res.data[i].adnm.length > 2) {
                this.htglsData.xData.push(
                  res.data[i].adnm.substring(0, res.data[i].adnm.length - 1)
                );
              } else {
                this.htglsData.xData.push(res.data[i].adnm);
              }
              this.htglsData.yData.push((res.data[i].length / 1000).toFixed(0));
              this.htglsData.adcdList.push(res.data[i].adcd);
              num += res.data[i].length;
            }
            // this.htglsData.title = `海塘长度（${(num / 1000).toFixed(1)}公里）`
            this.htglsData.title = `海塘长度(km)`;
          }
        } else {
          this.$message.error(res.msg ? res.msg : "请求失败");
        }
        this.htglsLoading = false;
      },

      // 海塘级别统计
      async getHtjbtjData() {
        this.htjbtjData = {
          noTitle: true,
          title: "海塘等级",
          id: "htjbtjChart",
          unit: "条",
          legend: {
            orient: "vertical",
            left: "65%",
            height: "80%",
          },
          color: [
            "#193CC5",
            "#4C71FF",
            "#7692FFFF",
            "#ACB5FFFF",
            "#E3E4FFFF",
            "#E3E4FFFF",
          ],
          series: [],
        };
        let res = await seawallLeftStatisticApi({
          statistic: "scale",
          adcd: this.$localData("get", "userInfo").adcd,
        });
        if (res.success) {
          if (res.data.length) {
            for (let i = 0; i < res.data.length; i++) {
              let htName = res.data[i].project_scale + "级海塘";
              if (res.data[i].project_scale == 5) {
                htName = "非标海塘";
              }
              if (res.data[i].project_scale != 6) {
                this.htjbtjData.series.push({
                  name: htName,
                  value: res.data[i].num,
                  project_scale: res.data[i].project_scale,
                });
              }
            }
          }
        } else {
          this.$message.error(res.msg ? res.msg : "请求失败");
        }
        this.htjbtjLoading = false;
      },

      // 海塘分布柱状图点击事件
      barClickAdcd1(index) {
        if (this.htfbData.xData[index] == "省级") {
          this.$store.commit("SET_LEFT_SELECT_ADCD", "33");
        } else {
          this.$store.commit(
            "SET_LEFT_SELECT_ADCD",
            this.htfbData.adcdList[index]
          );
        }
        this.getColorSeawallList()
      },

      // 海滩公里数统计柱状图点击事件
      barClickAdcd2(index) {
        if (this.htglsData.xData[index] == "省级") {
          this.$store.commit("SET_LEFT_SELECT_ADCD", "33");
        } else {
          this.$store.commit(
            "SET_LEFT_SELECT_ADCD",
            this.htglsData.adcdList[index]
          );
        }
        this.getColorSeawallList()
      },

      /**
   * @function:
   * @description: 三色码请求接口
   * @param {*} type 颜色类型 red yellow green 切换图层用
   * @param {*} name 颜色名字 红黄绿 请求接口用
   * @return {*}
   */
      async getColorSeawallList(type = "", name = "") {
        this.pointArr = [];
        let opt = {
          adcd: this.leftSelectAdcd
            ? this.leftSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          projectScale: this.leftSelectProjectScale,
        };
        this.$parent.$refs.seawallMap.removeAllLayer();
        this.$parent.$refs.seawallMap.closePopup();
        this.$parent.$refs.seawallMap.initView();
        let res = await seawallProIndo(opt);
        if (res.success) {
          let lineArr = [];
          this.tableData = [];
          if (res.data.length) {
            this.tableData = res.data;
            res.data.map((v) => {
              v.lat = v.lttd;
              v.lng = v.lgtd;
              v.rightType = 4;
              v.styleObj = {
                scale: 1,
              };

              v.mapType = "seawallPopup";
              lineArr.push({
                line: v.coorpot
                  ? JSON.parse(
                    v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                  )[0]
                  : "",
                level: v.project_scale,

                type: "seawallPopup",
              });
              v.lineArr = [];
              this.pointArr.push(v);
              v.lineArr.push({
                line: v.coorpot
                  ? JSON.parse(
                    v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                  )[0]
                  : "",
                level: v.seawallPopup,
                type: "seawallPopup",
              });
            });
          }

          this.$parent.showMapPointJh(this.pointArr, "ptSeawallPointLayer");
          this.$parent.showMapLines(lineArr);
        }
      },

      // 海塘设计防洪标准统计点击事件
      pieClick1(data) {
        this.$store.commit("SET_LEFT_SELECT_PREVENT_LEVEL", data.preventLevel);
      },

      // 海塘级别统计点击事件
      pieClick2(data) {
        this.$store.commit("SET_LEFT_SELECT_PROJECT_SCALE", data.project_scale);
        this.getColorSeawallList()
      },
    },
  };
</script>

<style lang='scss' scoped>
.warp {
  height: 100%;
}

.top_box {
  width: 100%;
  height: calc(28% + 20px);
  .aqjd_box {
    width: 82%;
    height: 30px;
    @include flexbox();
    @include flexAI(center);
    @include flexJC(space-around);
    margin: 0 auto;
    li.aqjd_item {
      user-select: none;
      margin: 0px 0px;
      width: 100%;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(flex-start);
      flex-direction: row;
      color: #000;
      padding: 5px 0 5px 0;
      &:nth-child(2) {
        margin-left: 32px;
      }
      img {
        width: 10%;
      }
      div {
        @include flexbox();
        @include flexAI(start);
        @include flexJC(flex-start);
        flex-direction: column;
        margin-left: 10px;
        p {
          font-size: 14px;
          font-weight: 600;
          padding-top: 5px;
          span {
            &.num {
              font-size: 26px;
              font-weight: 600;
            }
            &.unit {
              padding-left: 5px;
              font-size: 14px;
            }
          }
        }
      }
    }
  }
  .htfbChart {
    width: 100%;
    height: calc(100% - 86px);
  }
}
.middle_box {
  width: 100%;
  height: 15%;
  // height: calc(20% - 24px);
  .control-one-list {
    display: flex;
    justify-content: space-around;
    .control-one-item {
      width: 130px;
      height: 100px;
      padding: 6px 0 6px 10px;
      box-sizing: border-box;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);
      flex-direction: column;
      cursor: not-allowed;
      img {
        width: 36px;
        height: 36px;
      }
      > p {
        margin-top: 6px;
        font-size: 16px;
        color: #000000;
      }
      div {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(center);
        p {
          font-size: 14px;
          color: #000000;
          span {
            font-size: 20px;
            padding-right: 5px;
            font-weight: 600;
            line-height: 32px;
          }
        }
      }
      &.avtive-one-item {
        width: 180px;
        height: 76px;
        background: rgba(25, 59, 196, 0.08);
        box-shadow: 0px 2px 4px 1px rgba(0, 0, 0, 0.02),
          0px 0px 1px 0px rgba(0, 0, 0, 0.06),
          0px 1px 1px 0px rgba(0, 0, 0, 0.06);
        border: 1px solid #193bc4;
        padding: 12px 0 12px 23px;
        div {
          p {
            color: #193cc5;
          }
        }
      }
    }
  }
  .middle_box_list {
    padding-top: 0px;
    box-sizing: border-box;
    height: calc(100% - 40px);
    display: flex;
    align-items: center;
    margin-top: 20px;
  }
}
.third_box {
  width: 100%;
  height: 25%;
  .htfbChart {
    width: 100%;
    height: calc(100% - 30px);
    margin-top: 18px;
    // height: var (--barChartHeight);
  }
}
.fourth_box {
  width: 100%;
  height: 16%;
  margin: 0;
  .tab_box {
    width: 100%;
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    gap: 5px 0px;
    font-size: 18px;
    text-align: left;
    margin: 0px auto;
    li {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      align-items: start;
      p {
        width: 80%;
        font-weight: 600;
        text-align: center;
        margin: 0 0 0 5px;
      }
      .tab_box_item {
        @include flexbox;
        @include flexAI(center);
      }
      div {
        margin: 6px 0px 0px 10px;
        span {
          font-size: 16px;
          color: "#000";
          margin: 0 2px;
        }
        .ba_text {
          font-size: 16px;
        }
        .ba_num {
          display: flex;
          font-size: 18px;
          flex: 0 1 0%;
          align-items: center;
          color: "#000";
          font-weight: 600;
          .ba_unit {
            color: "#000";
            font-size: 16px;
            margin: 0 2px;
            font-weight: 500;
          }
        }
      }
    }
  }
}
.select_square {
  width: 96%;
  height: 30px;
  box-sizing: border-box;
  @include flexbox();
  @include flexJC(space-around);
  @include flexflow(row nowrap);
  margin: 10px auto;
  &.between {
    margin: 0px auto;
    @include flexJC(flex-start);
  }
  .square_item {
    &.between {
      margin: 0px 6px 16px 9px;
    }

    height: 100%;
    width: 27%;
    cursor: pointer;
    border-radius: 2px;
    padding: 4px;
    background: #f8f8f8;
    border: 1px solid #d6d6d6;
    p {
      line-height: 30px;
      font-size: 16px;
      text-align: center;
      color: #000;
    }
    &.active {
      background: #193cc5 !important;
      border: 1px solid #193cc5 !important;
      & > p {
        color: #fff !important;
      }
    }
  }
}
</style>