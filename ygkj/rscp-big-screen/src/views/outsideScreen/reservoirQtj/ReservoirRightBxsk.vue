<!--
 * @Author: hanyu
 * @LastEditTime: 2021-08-13 17:59:11
 * @Description: 地图右侧-病险水库
 * @FilePath: /rscp-big-screen/src/views/bigScreen/reservoirQtj/ReservoirRightBxsk.vue
-->
<template>
  <div class="wrap">
    <div class="jiao_border bottomRight"></div>
    <div class="jiao_border topLeft"></div>
    <div class="jiao_border topRight"></div>
    <div class="jiao_border bottomLeft"></div>
    <section class="xsgs_box">
      <div class="qtj_title">
        <div class="qtj_title_name">
          形势概述
        </div>
        <img src="../../../assets/images/qtj-arr.png" alt="" />
      </div>
      <div class="xsgs_words" v-loading="loading_xsgs">
        截至今日病险水库<span class="red">{{ topData.total }}</span
        >座。
        <p>
          <span class="weight">控制运行：</span>
          空库运行{{ squareList.length ? squareList[0].value : 0 }}座；
          超限制水位<span class="red">{{
            squareList.length ? squareList[1].value : 0
          }}</span
          >座， 超正常水位<span class="red">{{
            squareList.length ? squareList[2].value : 0
          }}</span
          >座。
        </p>
        <p>
          <span class="weight">加固处理：</span>
          未加固{{ topData.status1 }}，加固中{{ topData.status2 }}座，已完工{{
            topData.status3
          }}座，拟降等报废{{ topData.status4 }}座。
        </p>
      </div>
    </section>
    <section class="fbcyj_box">
      <div class="qtj_title">
        <div class="qtj_title_name">
          运行情况
        </div>
        <img src="../../../assets/images/qtj-arr.png" alt="" />
      </div>
      <div class="select_square" v-loading="loading_square">
        <div
          :class="[
            'square_item',
            item.type,
            index === squareIndex ? 'active' : '',
          ]"
          v-for="(item, index) in squareList"
          :key="index"
          @click="clickSquare(index)"
        >
          <p>{{ item.name }}</p>
          <span>{{ item.value }}<i>座</i></span>
        </div>
      </div>
    </section>
    <div class="qtj_title">
      <div class="qtj_title_name">
        除险加固情况
      </div>
      <img src="../../../assets/images/qtj-arr.png" alt="" />
    </div>
    <pie-chart
      v-loading="loading_status"
      class="statusChart"
      :chartData="statusData"
      @pieClick="pieClick"
    ></pie-chart>
    <div class="prg_box">
      <div class="qtj_title">
        <div class="qtj_title_name">
          水库数量
        </div>
        <img src="../../../assets/images/qtj-arr.png" alt="" />
      </div>
      <progress-list
        v-loading="progressLoading"
        title="水库数量"
        class="progress"
        :progressData="progressData"
        @selectAdcd="selectAdcd"
        :noTitle="true"
      ></progress-list>
    </div>
  </div>
</template>

<script>
import BarChart from "@/components/BarChart";
import PieChart from "@/components/PieChart";
import ProgressList from "@/components/ProgressList";
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {
    BarChart,
    PieChart,
    ProgressList,
  },
  data() {
    return {
      sghtfbData: {
        title: "病险水库分布",
        id: "sghtfbChart",
        unit: "",
        legend: {
          orient: "vertical",
          left: "60%",
          height: "70%",
        },
        color: [
          "#00c87b",
          "#539ff5",
          "#6e86a3",
          "#ffc425",
          "#ff7663",
          "#4fd3f0",
          "#aa84d4",
          "#ffa662",
          "#269A99",
        ],
        series: [],
      },

      skfbData: {
        title: "水库分布",
        id: "bxskfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "座",
      },

      sghtfbLoading: true,
      tableData: [],
      tableLoading: true,
      tableHeight: 0,
      opt: {
        statistic: "mapList",
        itemCategory: "thirdType,status",
        itemValue: "存量,三类坝",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
      canExport: false,
      squareList: [],
      squareIndex: "",
      loading_square: true,
      squareValue: "",
      listFlag: true,
      adcd: this.leftSelectAdcd
        ? this.leftSelectAdcd
        : this.$localData("get", "userInfo").adcd,
      superviseLevel: "",
      loading_xsgs: true,

      topData: {},
      loading_status: true,
      statusData: {
        noTitle: true,
        title: "除险加固情况",
        id: "statusData",
        unit: "座",
        legend: {
          orient: "vertical",
          left: "65%",
          height: "80%",
        },
        color: ["#5B8FF9", "#5AD8A6", "#F7C63A", "#779fec", "#8BBC89"],
        series: [],
      },

      progressLoading: true,
      progressData: [],
    };
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
      this.opt.adcd = val ? val : this.$localData("get", "userInfo").adcd;
      if (val == "33") {
        this.opt.superviseLevel = 1;
        this.superviseLevel = 1;
      }
      this.adcd = val;
      this.getSquareData();
    },
    leftSelectPreventLevel(val) {
      this.setOptions("preventLevel", val);
      this.getSquareData();
    },
    leftSelectProjectScale(val) {
      this.setOptions("scale", val);
      this.getSquareData();
    },
  },
  mounted() {
    // this.resizeTable()
    if (this.leftSelectAdcd == "33") {
      this.opt.superviseLevel = 1;
    }
    if (this.leftSelectProjectScale) {
      this.setOptions("scale", this.leftSelectProjectScale);
    }
    this.getTopData();
    this.getSquareData();
    this.getStatusData();
    this.getAdcdData();

    this.loading_xsgs = false;
  },
  methods: {
    resizeTable() {
      this.$nextTick(function() {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 20;
        let _this = this;
        window.onresize = function() {
          const height = this.$refs.tableContainer.clientHeight;
          this.tableHeight = height - 20;
        };
      });
    },
    setOptions(type, val) {
      if (val) {
        // val不为空
        if (this.opt.itemCategory) {
          if (this.opt.itemCategory.indexOf(type) == -1) {
            // 不存在该参数
            this.opt.itemCategory = this.opt.itemCategory + "," + type;
            this.opt.itemValue = this.opt.itemValue + "," + val;
          } else {
            // 已存在该参数 更新参数
            let itemArr = this.opt.itemCategory.split(",");
            let index = itemArr.indexValue(type);
            let valueArr = this.opt.itemValue.toString().split(",");
            valueArr[index] = val;
            this.opt.itemValue = valueArr.join(",");
          }
        } else {
          // 没有参数
          this.opt.itemCategory = type;
          this.opt.itemValue = val;
        }
      } else {
        // 为空
        if (this.opt.itemCategory) {
          if (this.opt.itemCategory.indexOf(type) != -1) {
            // 存在该参数 清空参数
            let itemArr = this.opt.itemCategory.split(",");
            let valueArr = this.opt.itemValue.toString().split(",");
            let index = itemArr.indexValue(type);
            itemArr.splice(index, 1);
            valueArr.splice(index, 1);
            this.opt.itemCategory = itemArr.join(",");
            this.opt.itemValue = valueArr.join(",");
          }
        }
      }
    },

    async getTopData() {
      let res = await reservoirLeftStatisticApi({
        statistic: "problemCnt",
        adcd: this.adcd,
        itemCategory: "thirdType,status," + this.squareValue,
        itemValue: "存量,三类坝,1",
        superviseLevel: this.superviseLevel,
      });
      if (res.success) {
        this.topData = res.data[0];
      }
    },

    // 运行情况
    async getSquareData() {
      this.loading_square = true;
      this.squareList = [];
      let res = await reservoirLeftStatisticApi({
        statistic: "overCnt",
        adcd: this.adcd,
        itemCategory: "thirdType,status",
        itemValue: "存量,三类坝",
        superviseLevel: this.superviseLevel,
      });
      if (res.success) {
        this.squareList.push(
          {
            type: "NWL",
            name: "超正常水位",
            value: res.data[0]["NWL"],
          },
          {
            type: "limitWaterLevel",
            name: "超限制水位",
            value: res.data[0]["limitWaterLevel"],
          },
          {
            type: "normal",
            name: "空库运行",
            value: res.data[0]["normal"],
          }
        );
        this.getReservoirList();
      }
      this.loading_square = false;
    },

    // 除险加固情况
    async getStatusData() {
      this.loading_status = true;
      this.statusData.series = [];
      let res = await reservoirLeftStatisticApi({
        statistic: "problemCnt",
        adcd: this.adcd,
        itemCategory: "thirdType,status," + this.squareValue,
        itemValue: "存量,三类坝,1",
        superviseLevel: this.superviseLevel,
      });
      if (res.success) {
        this.statusData.series = [
          {
            name: "未加固",
            value: res.data[0].status1 ? res.data[0].status1 : 0,
          },
          {
            name: "加固中",
            value: res.data[0].status2 ? res.data[0].status2 : 0,
          },
          {
            name: "已完工",
            value: res.data[0].status3 ? res.data[0].status3 : 0,
          },
          {
            name: "拟降等报废",
            value: res.data[0].status4 ? res.data[0].status4 : 0,
          },
        ];
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.loading_status = false;
    },

    async getAdcdData() {
      this.progressLoading = true;
      this.progressData = [];
      let res = await reservoirLeftStatisticApi({
        statistic: "adcd",
        itemCategory: "thirdType,status," + this.squareValue,
        itemValue: "存量,三类坝,1",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        let maxarr = [];
        for (let i = 0; i < res.data.length; i++) {
          maxarr.push(res.data[i].num);
        }
        let max = Math.max(...maxarr);
        for (let i = 0; i < res.data.length; i++) {
          let percentage = ((res.data[i].num / max) * 100).toFixed(0);
          this.progressData.push({
            adcd: res.data[i].adcd,
            title: res.data[i].adnm,
            nowNum: res.data[i].num.toFixed(0),
            // allNum: (fz / 100).toFixed(0),
            percentage: Number(percentage),
          });
        }
      }
      this.progressLoading = false;
    },

    // 病险水库分布
    async getSghtfbData() {
      this.sghtfbLoading = true;
      let res = await reservoirLeftStatisticApi({
        statistic: "adcd",
        itemCategory: "thirdType,status," + this.squareValue,
        itemValue: "存量,三类坝,1",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      // if(res.success){
      //   if(res.data.length){
      //     for (let i = 0; i < res.data.length; i++) {
      //       skfbData
      //       this.sghtfbData.series.push({
      //         name: res.data[i].adnm,
      //         value: res.data[i].num
      //       })
      //     }
      //   }
      // } else {
      //   this.$message.error(res.msg ? res.msg : '请求失败')
      // }
      // this.sghtfbLoading = false

      if (res.success) {
        let xData = [];
        let yData = [];
        let adcdList = [];
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            if (res.data[i].adnm.length > 2) {
              xData.push(
                res.data[i].adnm.substring(0, res.data[i].adnm.length - 1)
              );
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
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.sghtfbLoading = false;
    },

    // 病险水库列表
    async getReservoirList() {
      if (this.listFlag) {
        // this.setOptions('overCnt', this.squareValue)
        this.$parent.$refs.reservoirMap.removeAllLayer();
        this.$parent.$refs.reservoirMap.closePopup();
        this.$parent.$refs.reservoirMap.initView();
        this.tableLoading = true;
        this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", false);
        this.tableData = [];
        this.canExport = false;
        this.listFlag = false;
        // let res = await reservoirLeftStatisticApi(opt)
        let res = await reservoirLeftStatisticApi(this.opt);
        if (res.success) {
          let pointArr = [];
          let pointObj = {
            blue: [],
            yellow: [],
            orange: [],
            red: [],
          };
          if (res.data.length) {
            this.canExport = true;
            this.tableData = res.data;
            let colorType = "";
            if (this.squareIndex === 2) {
              colorType = "yellow";
            } else if (this.squareIndex === 1) {
              colorType = "orange";
            } else if (this.squareIndex === 0) {
              colorType = "red";
            }
            res.data.map((v) => {
              v.lat = v.LTTD;
              v.lng = v.LGTD;
              v.rightType = "1";
              v.src = require("@/assets/images/map/reservoir.png");
              v.colorType = colorType;
              if (this.squareIndex === "") {
                if (v.waterLevel < v.DDZ + 1) {
                  v.colorType = "yellow";
                } else if (v.DDZ + 1 < v.waterLevel && v.waterLevel < v.NWL) {
                  v.colorType = "orange";
                } else if (v.waterLevel > v.NWL) {
                  v.colorType = "red";
                }
              }
              if (v.colorType) {
                pointObj[v.colorType].push(v);
              }
            });
            pointArr = [
              ...pointObj["blue"],
              ...pointObj["yellow"],
              ...pointObj["orange"],
              ...pointObj["red"],
            ];
          }
          this.$parent.showMapPoints(pointArr);
        }
        this.listFlag = true;
        this.tableLoading = false;
        this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", true);
      }
    },

    clickReservoir(row) {
      this.$parent.setView(row.LGTD, row.LTTD);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightType = "1";
      this.$parent.showOneSelectPoint([row]);
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },

    clickSquare(index) {
      if (this.listFlag) {
        if (this.squareIndex === index) {
          this.squareIndex = "";
          this.squareValue = "";
          this.opt.itemCategory = "thirdType,status";
          this.opt.itemValue = "存量,三类坝";
        } else {
          this.squareIndex = index;
          this.squareValue = this.squareList[index].type;
          this.opt.itemCategory = "thirdType,status," + this.squareValue;
          this.opt.itemValue = "存量,三类坝,1";
        }
        this.opt.adcd = this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd;
        this.$parent.$refs.reservoirMap.removePolygon();
        this.getAdcdData();
        this.getStatusData();
        this.getReservoirList();
      }
    },

    exportList() {
      let adcd = this.leftSelectAdcd
        ? this.leftSelectAdcd
        : this.$localData("get", "userInfo").adcd;
      let token = this.$localData("get", "token");
      window.open(
        this.$config.host +
          `/mgt/bm/reservoirei/toExcel` +
          `?adcd=${this.opt.adcd}` +
          `&statistic=mapList` +
          `&superviseLevel=${this.opt.superviseLevel}` +
          `&itemCategory=${this.opt.itemCategory}` +
          `&itemValue=${this.opt.itemValue}` +
          `&token=${token}`
      );
    },

    // 水库分布点击事件
    selectAdcd(item) {
      if (item.title == "省级") {
        this.opt.superviseLevel = 1;
        // this.$store.commit('SET_LEFT_SELECT_ADCD', '33')
        this.$store.commit("SET_LEFT_SELECT_ADNM", "浙江省");
      } else {
        this.opt.adcd = item.adcd;
        this.$store.commit("SET_LEFT_SELECT_ADNM", item.title);
        // this.$store.commit('SET_LEFT_SELECT_ADCD', item.adcd)
      }
      this.getReservoirList();
    },
    pieClick(index) {
      console.log(index, "pieClick");
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  height: 100%;
  position: relative;
  .qtj_title {
    @include flexbox();
    @include flexAC();
    padding: 20px 16px;
    .qtj_title_name {
      height: 36px;
      line-height: 36px;
      padding-left: 12px;
      background: -webkit-gradient(
        linear,
        left top,
        right top,
        from(rgba(77, 0, 247, 0.1)),
        to(rgba(20, 20, 20, 0))
      );
      background: linear-gradient(
        90deg,
        rgba(77, 0, 247, 0.1),
        rgba(20, 20, 20, 0)
      );
      border-left: 3.5px solid #4d81f7;
      font-size: 28px;
      font-weight: bold;
      letter-spacing: 1.6px;
      color: #333;
    }
    img {
      height: 24px;
      width: 22px;
      margin: 0 8px;
    }
  }
  .jiao_border {
    width: 14px;
    height: 14px;
    position: absolute;
    &.bottomRight {
      bottom: 0;
      right: 0;
      border-bottom: 3px solid #4d81f7 !important;
      border-right: 3px solid #4d81f7 !important;
    }
    &.bottomLeft {
      bottom: 0;
      left: 0;
      border-bottom: 3px solid #4d81f7 !important;
      border-left: 3px solid #4d81f7 !important;
    }
    &.topLeft {
      top: 0;
      left: 0;
      border-top: 3px solid #4d81f7 !important;
      border-left: 3px solid #4d81f7 !important;
    }
    &.topRight {
      top: 0;
      right: 0;
      border-top: 3px solid #4d81f7 !important;
      border-right: 3px solid #4d81f7 !important;
    }
  }
  .htfbChart {
    width: 100%;
    height: 220px;
  }
  .statusChart {
    width: 100%;
    height: 230px;
  }
  .prg_box {
    height: calc(100% - 680px);
    // overflow: hidden;
    position: relative;
    top: -40px;
    .export_btn {
      position: absolute;
      right: 16px;
      top: 5px;
    }
    .progress {
      height: 100%;
    }
  }

  .xsgs_box {
    .xsgs_words {
      color: #333;
      padding: 0 16px;
      font-size: 18px;
      line-height: 26px;
      span {
        font-weight: 500;
        &.weight {
          font-weight: bold;
        }
        &.red {
          color: #e02020;
        }
        &.orange {
          color: #fa6400;
        }
        &.yellow {
          color: #f7b500;
        }
      }
    }
  }
  .fbcyj_box {
    .select_square {
      height: 80px;
      box-sizing: border-box;
      @include flexbox();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      padding: 0 16px 16px 16px;
      .square_item {
        box-sizing: border-box;
        width: 32%;
        height: 70px;
        text-align: center;
        // width: 32%;
        background: #f8f8f8;
        border: 1px solid #d6d6d6;
        cursor: pointer;
        border-radius: 2px;
        margin-bottom: 8px;
        &.active {
          background: #193cc5 !important;
          border: 1px solid #193cc5 !important;
          & > p {
            color: #fff !important;
          }
          & > span {
            color: #fff !important;
          }
          & > h3 {
            color: #fff !important;
          }
        }
        & > p {
          line-height: 24px;
          font-size: 14px;
          color: #000;
          padding: 8px 0 4px 0;
        }
        i {
          font-size: 12px;
          padding-left: 4px;
        }
        & > span,
        & > h3 {
          line-height: 24px;
          font-size: 20px;
          color: #000;
          font-weight: 500;
        }
        &.overCnt,
        &.nearCnt,
        &.overTop,
        &.closeCnt,
        &.overDesign,
        &.sickCnt {
          width: 32%;
          background: #f8f8f8;
          border: 1px solid #d6d6d6;
        }
        &.blue {
          background: #eef5fe;
          border: 1px solid #2478f0;
        }
        &.yellow {
          background: #fffbf0;
          border: 1px solid #f7c739;
        }
        &.orange {
          background: #fef5f3;
          border: 1px solid #eb7e65;
        }
        &.red {
          background: #fdeeee;
          border: 1px solid #e02020;
        }
      }
    }
  }
  .reservoirList_box {
    height: calc(100% - 380px);
    .table_list {
      height: calc(100% - 42px);
      box-sizing: border-box;
      padding-bottom: 16px;
      // height: 500px;
      overflow: auto;
    }
  }
}
</style>
