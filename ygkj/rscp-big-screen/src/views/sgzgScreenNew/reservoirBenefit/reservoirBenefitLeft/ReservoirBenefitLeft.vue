<!--
 * @Date: 2021-12-24 09:31:12
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2021-12-27 09:07:58
 * @FilePath: \rscp-big-screen\src\views\sgzgScreenNew\reservoirBenefit\reservoirBenefitLeft\ReservoirBenefitLeft.vue
-->
<template>
  <div class="wrap">
    <div class="left_title">
      <span>社会效益</span>
    </div>
    <section class="xsl_box">
      <div class="second_title">
        <div class="name">
          <p>防洪效益</p>
        </div>
        <!-- <div class="solid"></div>
        <div class="dashed">
          <div></div>
          <div></div>
        </div> -->
      </div>
      <div class="benefit_box">
        <ul class="date_box">
         
          <li>
            <p>保护人口<i>(万人)</i> </p>
            <div class="green_text">3,650</div>
          </li>
          <li>
            <p>保护面积<i>(平方公里)</i> </p>
            <div class="green_text">7,015</div>
          </li>
           <li>
            <p>拦蓄洪水量<i>(万m³)</i> </p>
            <div>1,660,000</div>
          </li>
        </ul>
      </div>
    </section>
    <section class="xsl_box">
      <div class="second_title">
        <div class="name">
          <p>生态效益</p>
        </div>
        <!-- <div class="solid"></div>
        <div class="dashed">
          <div></div>
          <div></div>
        </div> -->
      </div>
      <div class="benefit_box">
        <ul class="date_box">
          <li>
            <p>生态放水量<i>(万m³)</i></p>
            <div class="blue_text">63,400 </div>
          </li>
          <li>
            <p>生态价值GEP<i>(万元)</i> </p>
            <div>5,680,000</div>
          </li>
        </ul>
      </div>
    </section>
  </div>
</template>

<script>
import BarChart from "@/components/BarChart";
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {
    BarChart,
  },
  data() {
    return {
      waterListOne: [], benefitDate: '',
      skfbData: {
        title: "地区分布 (%)",
        id: "kgnslChart",
        xData: [],
        yData: [],
        yData2: [],
        adcdList: [],
        adnmList: [],
        unit: "%",
      },
      showSkfbChart: false,
      sghtfbLoading: true,

      tableData: [],
      tableLoading: true,
      tableHeight: 0,
      opt: {
        statistic: "mapList",
        itemCategory: "keyStr",
        itemValue: "FCLWL_PREC",
        basin: "",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
      canExport: false,
      listFlag: true,
      waterList: [],
      waterIndex: "",
      scale: "all",
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectAdnm() {
      return this.$store.state.leftSelectAdnm;
    },
    leftSelectPreventLevel() {
      return this.$store.state.leftSelectPreventLevel;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    },
    leftSelectBasin() {
      return this.$store.state.leftSelectBasin;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.opt.adcd = val ? val : this.$localData("get", "userInfo").adcd;
      if (val == "33") {
        this.opt.superviseLevel = 1;
        this.superviseLevel = 1;
      } else {
        this.opt.superviseLevel = "";
        this.superviseLevel = "";
      }
      this.adcd = val;
      this.getSghtfbData();
      this.getTopData();
    },
    leftSelectProjectScale(val) {
      this.setOptions("scale", val);
      this.scale = val;
      this.getSghtfbData();
      this.getReservoirList();
    },
    leftSelectAdnm(val) {
      if (val == this.$localData("get", "userInfo").adnm) {
        this.opt.superviseLevel = "";
        this.opt.adcd = this.leftSelectAdcd;
        this.getReservoirList();
      }
    },
    leftSelectBasin(val) {
      this.getSghtfbData();
      this.getTopData();
    },
  },
  mounted() {
    this.resizeTable();
    if (this.leftSelectAdcd == "33") {
      this.opt.superviseLevel = 1;
      this.superviseLevel = 1;
    }
    if (this.leftSelectAdcd) {
      this.opt.adcd = this.leftSelectAdcd;
      this.adcd = this.leftSelectAdcd;
    }
    if (this.leftSelectProjectScale) {
      this.setOptions("scale", this.leftSelectProjectScale);
      this.scale = this.leftSelectProjectScale;
    }
    this.getTopData();
    this.getSghtfbData();

    this.loading_xsgs = false;
  },
  methods: {
    PrefixInteger(num, length) {
      return (Array(length).join('0') + num).slice(-length);
    },
    resizeTable() {
      this.$nextTick(function () {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 20;
        let _this = this;
        window.onresize = function () {
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

    // 运行情况
    async getTopData() {
      this.loading_square = true;
      this.squareList = [];
      let res = await reservoirLeftStatisticApi({
        statistic: "scale",
        adcd: this.adcd,
        basin: this.leftSelectBasin,
        itemCategory: "",
        itemValue: "",
        superviseLevel: this.superviseLevel,
      });
      if (res.success) {
        let arr = [];
        for (let i = 0; i < res.data.length; i++) {
          let obj = {};
          switch (res.data[i].project_scale) {
            case "1":
              obj.name = "大(1)型";
              obj.nam = "可蓄水量";
              obj.srcs = require("@/assets/images/normal-water.png");
              obj.nams = '[到正常蓄水位]';
              break;
            case "2":
              obj.name = "大(2)型";
              obj.nam = "可蓄水量";
              obj.nams = '[到设计水位]';
              obj.srcs = require("@/assets/images/design-water.png");
              break;
            case "3":
              obj.srcs = require("@/assets/images/high-water.png");
              obj.name = "中型";
              obj.nam = "可蓄水量";
              obj.nams = '[到防洪高水位]';
              break;
            case "4":
              obj.name = "小(1)型";
              break;
            case "5":
              obj.name = "小(2)型";
              break;
          }
          obj.scale = res.data[i].project_scale;

          // obj.number = res.data[i].storagePerc
          //   ? res.data[i].storagePerc > 100
          //     ? "100%"
          //     : res.data[i].storagePerc + "%"
          //   : "0%";
          obj.number = 100000
          obj.viewHeight =
            res.data[i].storagePerc && res.data[i].storagePerc > 100
              ? "0%"
              : (100 - res.data[i].storagePerc).toFixed(0) + "%";
          arr.push(obj);
        }
        this.waterList = arr;
        this.waterListOne = arr.slice(0, 3);
        this.getReservoirList();
      }
      this.loading_square = false;
    },



    // 水库库容
    async getSghtfbData() {
      this.sghtfbLoading = true;
      this.showSkfbChart = false;
      this.$nextTick(() => {
        this.showSkfbChart = true;
      });
      let opt = {
        statistic: "waterQT",
        itemCategory: "keyStr,scale",
        itemValue: "CSCNWL," + this.scale,
        basin: this.leftSelectBasin,
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        superviseLevel: this.superviseLevel,
      };
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        let xData = [];
        let yData = [];
        let yData2 = [];
        let adcdList = [];
        let adnmList = [];
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            if (res.data[i].adnm.length > 2) {
              // xData.push(res.data[i].adnm.substring(0,res.data[i].adnm.length - 1))
              xData.push(res.data[i].adnm.replace(/市/, ""));
            } else {
              xData.push(res.data[i].adnm);
            }

            // 原本按老字段显示push进去的内容
            //  ((res.data[i].currenty / res.data[i].capable) * 100 > 100
            //                 ? 100
            //                 : (res.data[i].currenty / res.data[i].capable) * 100
            //               ).toFixed(0)
            yData.push(
              res.data[i].storagePerc ? res.data[i].storagePerc.toFixed(1) : 0
            );

            yData2.push(
              res.data[i].addtion ? (res.data[i].addtion / 10000).toFixed(1) : 0
            );
            adcdList.push(res.data[i].adcd);
            adnmList.push(res.data[i].adnm);
          }
        }
        this.skfbData.xData = xData;
        this.skfbData.yData = yData;
        this.skfbData.yData2 = yData2;
        this.skfbData.adcdList = adcdList;
        this.skfbData.adnmList = adnmList;
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.sghtfbLoading = false;
    },

    // 水库列表
    async getReservoirList() {
      if (this.listFlag) {
        this.opt.basin = this.leftSelectBasin,
          this.$parent.$refs.reservoirMap.removeAllLayer();
        this.$parent.$refs.reservoirMap.closePopup();
        this.$parent.$refs.reservoirMap.initView();
        this.tableLoading = true;
        this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", false);
        this.tableData = [];
        this.listFlag = false;
        let res = await reservoirLeftStatisticApi(this.opt);
        if (res.success) {
          let pointArr = [];
          if (res.data.length) {
            this.tableData = res.data;
            res.data.map((v) => {
              let xsl = 0;
              if (v.currenty) {
                if (v.CSCNWL) {
                  if (v.currenty > v.CSCNWL) {
                    xsl = 100;
                  } else {
                    xsl = ((v.currenty / v.CSCNWL) * 100).toFixed(0);
                  }
                } else {
                  xsl = 100;
                }
              } else {
                xsl = 0;
              }
              v.rightPopType = "xsl";
              v.xsl = xsl + "%";
              v.lat = v.LTTD;
              v.lng = v.LGTD;
              v.colorType = "blue";
              v.rightType = "1";
              v.src = require("@/assets/images/map/reservoir.png");
              pointArr.push(v);
            });
          }
          // this.$parent.showMapPoints(pointArr);
        }
        this.listFlag = true;
        this.tableLoading = false;
        this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", true);
      }
    },

    clickReservoir(row) {
      this.$parent.setView(Number(row.LGTD) + 0.004, Number(row.LTTD) + 0.013);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightPopType = "xsl";
      row.rightType = "1";
      // this.$parent.showOneSelectPoint([row]);
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },

    clickWater(index) {
      if (this.listFlag) {
        if (this.waterIndex === index) {
          this.waterIndex = "";
          this.scale = "all";
          this.opt.itemCategory = "keyStr";
          this.opt.itemValue = "FCLWL_PREC";
        } else {
          this.waterIndex = index;
          this.scale = this.waterList[index].scale;
          this.opt.itemCategory = "keyStr,scale";
          this.opt.itemValue = "FCLWL_PREC," + this.scale;
        }
        this.opt.adcd = this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd;
        this.getSghtfbData();
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

    barClickAdcd(index) {
      if (this.skfbData.xData[index] == "省级") {
        this.opt.superviseLevel = 1;
        // this.$store.commit('SET_LEFT_SELECT_ADCD', '33')
        this.$store.commit("SET_LEFT_SELECT_ADNM", "浙江省");
      } else {
        this.opt.adcd = this.skfbData.adcdList[index];
        this.$store.commit(
          "SET_LEFT_SELECT_ADNM",
          this.skfbData.adnmList[index]
        );
        // this.$store.commit('SET_LEFT_SELECT_ADCD', this.skfbData.adcdList[index])
      }
      this.getReservoirList();
    },
  },
};
</script>

<style lang="scss" scoped>
@font-face {
  font-family: "UnidreamLCD";
  src: url("../../../../assets/data/UnidreamLCD.woff"); /***兼容ie9***/
  src: url("../../../../assets/data/UnidreamLCD.woff?#iefix")
      format("embedded-opentype"),
    /***兼容ie6-ie8***/ url("../../../../assets/data/UnidreamLCD.woff")
      format("woff"),
    local("UnidreamLCD"), url("../../../../assets/data/UnidreamLCD.woff"); /***默认使用本地的***/
}
.date-icon {
  width: 10px;
  height: 10px;
  background: url("../../../../assets/images/calendar.png");
}
.wrap {
  height: 100%;
  .second_title {
    .name {
      width: 90px;
    }
  }
  .xsl_box {
    .benefit_box {
      box-sizing: border-box;
      width: 100%;
      padding: 0px 16px;
      .cal_btn {
        margin-top: 20px;
        width: calc(100% - 16px);
        background: #193cc5;
      }
      .date_box {
        margin-top: 10px;
        width: calc(100% - 16px);
        height: 50%;
        min-height: 250px;
        // background: #f0f6ff;
        // border: 1px solid #f0f6ff;
        @include flexbox();
        @include flexflow(column nowrap);
        & > li {
          margin: 5px auto;
          width: 90%;
          @include flexbox();
          @include flexJC(center);
          @include flexAI(center);
          @include flexflow(column nowrap);
          p {
            width: 100%;
            text-align: left;
            height: 24px;
            font-size: 22px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 600;
            color: #333333;
            line-height: 24px;
            i {
              color: #33333388;
            }
          }
          div {
            width: 100%;
            height: 90px;
            line-height: 90px;
            font-size: 70px;
            font-family: "UnidreamLCD";
            font-weight: 600;
            letter-spacing: 15px;
            color: #fba040;
            &.green_text {
              color: #63b225;
            }
            &.blue_text {
              color: #4d7df7;
            }
            margin: 10px auto;
            background: #ffffff;
            box-shadow: 0px 3px 6px 0px rgba(59, 89, 205, 0.08);
            border-radius: 4px;
            border: 1px solid rgba(59, 89, 205, 0.4);
            @include flexbox();
            @include flexJC(flex-start);
            @include flexAI(center);
            padding-left: 20px;
          }
        }
      }
    }
  }
}
</style>
