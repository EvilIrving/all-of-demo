<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-12 15:19:12
 * @Description: 地图右侧-病险水库
 * @FilePath: /rscp-big-screen/src/views/bigScreen/reservoir/reservoirRight/ReservoirRightKgkn.vue
-->
<template>
  <div class="wrap">
    <div class="chart_line"></div>
    <section class="xsl_box">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        规模分布
      </div>
      <div class="water_list">
        <div
          :class="[
            'water_item',
            index === waterIndex ? 'active' : '',
            index === 0 || index === 1 ? 'width50' : 'width33',
          ]"
          v-for="(item, index) in waterList"
          :key="index"
          @click="clickWater(index)"
        >
          <!-- <el-popover
            placement="left"
            width="40"
            trigger="hover"> -->
          <!-- <div>
              <p>可纳蓄：{{item.knx}}{{item.knxUnit}}</p>
              <p>可供水：{{item.kgs}}{{item.kgsUnit}}</p>
            </div> -->
          <div class="wave_box" slot="reference">
            <div class="wave_wrap">
              <div class="kgs">
                <span>可纳洪</span>
                <p>
                  {{ item.knx }} <i>{{ item.knxUnit }}</i>
                </p>
              </div>
              <div class="line"></div>
              <div class="knx">
                <span>可供水</span>
                <p>
                  {{ item.kgs }} <i>{{ item.kgsUnit }}</i>
                  <!-- {{ item.knx }} <i>{{ item.knxUnit }}</i> -->
                </p>
              </div>
            </div>
            <div class="wave_word">{{ item.name }}</div>
          </div>
          <!-- </el-popover> -->
        </div>
      </div>
    </section>
    <div class="chart_line"></div>
    <!-- <bar-chart v-loading="sghtfbLoading" class="htfbChart" :chartData="skfbData" @barClick="barClickAdcd"></bar-chart> -->
    <bar-chart-double-click
      v-if="showSkfbChart"
      v-loading="sghtfbLoading"
      class="htfbChart"
      :chartData="skfbData"
      @barClick="barClickAdcd"
    ></bar-chart-double-click>

    <div class="chart_line"></div>
    <section class="reservoirList_box">
      <div class="chart_title" style="height:25px">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        水库列表
        <el-button
          v-if="canExport"
          style="float:right"
          plain
          size="small"
          @click="exportList()"
          >导出</el-button
        >
      </div>
      <div class="table_list" ref="tableContainer">
        <el-table
          :height="tableHeight"
          v-loading="tableLoading"
          :data="tableData"
          stripe
          style="width: 100%"
          @row-click="clickReservoir"
        >
          <template slot="empty">
            <div class="table_nodata">
              <img src="../../../../assets/images/noData.png" alt="" />
            </div>
          </template>
          <el-table-column prop="NAME" label="水库名称"></el-table-column>
          <!-- <el-table-column label="安全等级" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.status ? scope.row.status : '-'}}</div>
            </template>
          </el-table-column> -->
          <el-table-column label="行政区划" width="60" align="center">
            <template slot-scope="scope">
              <div>{{ scope.row.cityADNM ? scope.row.cityADNM : "-" }}</div>
            </template>
          </el-table-column>
          <el-table-column label="规模" width="60" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.project_scale == '1'">大(1)型</div>
              <div v-if="scope.row.project_scale == '2'">大(2)型</div>
              <div v-if="scope.row.project_scale == '3'">中型</div>
              <div v-if="scope.row.project_scale == '4'">小(1)型</div>
              <div v-if="scope.row.project_scale == '5'">小(2)型</div>
            </template>
          </el-table-column>
          <el-table-column label="可纳蓄" align="center">
            <template slot-scope="scope">
              <div>
                {{ scope.row.knx ? scope.row.knx + scope.row.knxUnit : "0" }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="可供水" align="center">
            <template slot-scope="scope">
              <div>
                {{ scope.row.kgs ? scope.row.kgs + scope.row.kgsUnit : "0" }}
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </section>
  </div>
</template>

<script>
import BarChart from "@/components/BarChart";
import PieChart from "@/components/PieChart";
import ProgressList from "@/components/ProgressList";
import BarChartDoubleClick from "@/components/BarChartDoubleClick";
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {
    BarChart,
    PieChart,
    ProgressList,
    BarChartDoubleClick,
  },
  data() {
    return {
      skfbData: {
        title: "地区分布（亿方）",
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
    }
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
              break;
            case "2":
              obj.name = "大(2)型";
              break;
            case "3":
              obj.name = "中型";
              break;
            case "4":
              obj.name = "小(1)型";
              break;
            case "5":
              obj.name = "小(2)型";
              break;
          }
          obj.scale = res.data[i].project_scale;
          if (res.data[i].capable < 10000) {
            obj.kgs = res.data[i].capable.toFixed(0);
            obj.kgsUnit = "万m³";
          } else {
            obj.kgs = (res.data[i].capable / 10000).toFixed(0);
            obj.kgsUnit = "亿m³";
          }
          if (res.data[i].addtion < 10000) {
            obj.knx = res.data[i].addtion.toFixed(0);
            obj.knxUnit = "万m³";
          } else {
            obj.knx = (res.data[i].addtion / 10000).toFixed(0);
            obj.knxUnit = "亿m³";
          }
          // if(res.data[i].FCLWL){
          //   if(res.data[i].FCLWL > res.data[i].currenty){
          //     obj.knx = (res.data[i].FCLWL - res.data[i].currenty).toFixed(0)
          //     if(obj.knx > 10000 || obj.knx == 10000){
          //       obj.knx = (obj.knx / 10000).toFixed(0)
          //       obj.knxUnit = '亿m³'
          //     }else{
          //       obj.knxUnit = '万m³'
          //     }
          //   }else{
          //     obj.knx = 0
          //   }
          // }else{
          //   obj.knx = 0
          // }

          // if(res.data[i].currenty){
          //   if(res.data[i].currenty > res.data[i].DDCP){
          //     obj.kgs = (res.data[i].currenty - res.data[i].DDCP).toFixed(0)
          //     if(obj.kgs > 10000 || obj.kgs == 10000){
          //       obj.kgs = (obj.kgs / 10000).toFixed(0)
          //       obj.kgsUnit = '亿m³'
          //     }else{
          //       obj.kgsUnit = '万m³'
          //     }
          //   }else{
          //     obj.kgs = 0
          //   }
          // }else{
          //   obj.kgs = 0
          // }
          arr.push(obj);
        }
        this.waterList = arr;
        this.getReservoirList();
      }
      this.loading_square = false;
    },

    // // 病险水库分布
    // async getSghtfbData(){
    //   this.sghtfbLoading = true
    //   let opt = {}
    //     opt = {
    //       statistic: 'waterQT',
    //       itemCategory: 'scale,keyStr',
    //       itemValue: this.scale + ',FCLWL',
    //       adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd,
    //       superviseLevel: this.superviseLevel
    //     }

    //   let res = await reservoirLeftStatisticApi(opt)
    //   if(res.success){
    //     let xData = []
    //     let yData = []
    //     let adcdList = []
    //     let adnmList = []
    //     if(res.data.length){
    //       for (let i = 0; i < res.data.length; i++) {
    //         let number = 0
    //         if(res.data[i].adnm.length > 2){
    //           xData.push(res.data[i].adnm.substring(0,res.data[i].adnm.length - 1))
    //         }else{
    //           xData.push(res.data[i].adnm)
    //         }

    //         if(res.data[i].currenty > res.data[i].capable){
    //           number = 100
    //         }else{
    //           number = ( res.data[i].currenty / res.data[i].capable * 100).toFixed(0)
    //         }
    //         yData.push(number)
    //         adcdList.push(res.data[i].adcd)
    //         adnmList.push(res.data[i].adnm)
    //       }
    //     }
    //     this.skfbData.xData = xData
    //     this.skfbData.yData = yData
    //     this.skfbData.adcdList = adcdList
    //     this.skfbData.adnmList = adnmList
    //   } else {
    //     this.$message.error(res.msg ? res.msg : '请求失败')
    //   }
    //   this.sghtfbLoading = false
    // },

    // 水库库容
    async getSghtfbData() {
      this.sghtfbLoading = true;
      this.showSkfbChart = false;
      this.$nextTick(() => {
        this.showSkfbChart = true;
      });
      let opt = {
        statistic: "capacity",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        basin: this.leftSelectBasin,
        itemCategory: "scale",
        itemValue: this.scale,
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
            yData.push((res.data[i].supply / 10000).toFixed(1));
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
        // let res = await reservoirLeftStatisticApi(opt)
        let res = await reservoirLeftStatisticApi(this.opt);
        if (res.success) {
          let pointArr = [];
          if (res.data.length) {
            this.tableData = res.data;
            res.data.map((v) => {
              let knx = 0;
              let kgs = 0;
              let knxUnit = "万m³";
              let kgsUnit = "万m³";
              if (v.FCLWL) {
                if (v.FCLWL > v.currenty) {
                  knx = (v.FCLWL - v.currenty).toFixed(1);
                  if (knx > 10000 || knx == 10000) {
                    knx = (knx / 10000).toFixed(1);
                    knxUnit = "亿m³";
                  } else {
                    knxUnit = "万m³";
                  }
                } else {
                  knx = 0;
                }
              } else {
                knx = 0;
              }

              if (v.currenty) {
                if (v.currenty > v.DDCP) {
                  kgs = (v.currenty - v.DDCP).toFixed(1);
                  if (kgs > 10000 || kgs == 10000) {
                    kgs = (kgs / 10000).toFixed(1);
                    kgsUnit = "亿m³";
                  } else {
                    kgsUnit = "万m³";
                  }
                } else {
                  kgs = 0;
                }
              } else {
                kgs = 0;
              }
              v.rightPopType = "kgkn";
              v.kgs = kgs;
              v.knx = knx;
              v.knxUnit = knxUnit;
              v.kgsUnit = kgsUnit;
              v.lat = v.LTTD;
              v.lng = v.LGTD;
              v.rightType = "1";
              v.colorType = "blue";
              v.src = require("@/assets/images/map/reservoir.png");
              pointArr.push(v);
            });
          }
          // this.$parent.showMapPointJh(pointArr, "kgkn");
          this.$parent.showMapPoints(pointArr);
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
      row.rightPopType = "kgkn";
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
.wrap {
  height: 100%;
  .htfbChart {
    width: 100%;
    height: 280px;
  }
  .xsl_box {
    .water_list {
      // position: relative;
      @include flexbox();
      @include flexJC(center);
      @include flexflow(row wrap);
      padding: 16px;
      .water_item {
        margin-bottom: 10px;
        &.width50 {
          width: 34%;
        }
        &.width33 {
          width: 33%;
        }
        &.active {
          .wave_wrap {
            @include transition(all, 0.5s, ease);
            background-image: linear-gradient(#fab6aa, #f37e69) !important;
          }
        }
        .wave_box {
          .wave_wrap {
            margin: 0 auto;
            margin-bottom: 10px;
            cursor: pointer;
            position: relative;
            padding: 1px;
            box-sizing: border-box;
            width: 100px;
            height: 110px;
            line-height: 50px;
            font-size: 14px;
            text-align: center;
            overflow: hidden;
            animation: water-wave linear infinite;
            // background-image: linear-gradient(#95ceff, #4997f7);
            // border-radius: 2px;
            // border: none;
            .kgs {
              background: #eef4ff;
              color: #000;
              height: 50%;
              box-sizing: border-box;
              padding-top: 5px;
              border: 1px dashed #4997f7;
              & > p {
                font-size: 22px;
                line-height: 22px;
                i {
                  font-size: 12px;
                }
              }
              & > span {
                display: block;
                line-height: 14px;
                padding-bottom: 5px;
              }
            }
            .line {
            }
            .knx {
              background-color: #4997f7;
              color: #fff;
              height: 50%;
              box-sizing: border-box;
              padding-top: 10px;
              // border: 1px solid #95ceff;
              & > p {
                font-size: 22px;
                line-height: 22px;
                i {
                  font-size: 12px;
                }
              }
              & > span {
                display: block;
                line-height: 14px;
                padding-bottom: 5px;
              }
            }
          }
          .wave_word {
            text-align: center;
          }
        }
      }
    }
  }
  .reservoirList_box {
    height: calc(100% - 640px);
    .table_list {
      height: calc(100% - 42px);
      box-sizing: border-box;
      padding-bottom: 16px;
      // height: 500px;
      overflow: auto;
    }
  }
}

@-webkit-keyframes imgCircle {
  from {
    -webkit-transform: rotate(0deg);
  }
  to {
    -webkit-transform: rotate(360deg);
  }
}

@keyframes imgCircle {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
