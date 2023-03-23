<!--
 * @Date: 2021-12-24 09:31:12
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2021-12-26 22:51:33
 * @FilePath: \rscp-big-screen\src\views\sgzgScreenNew\reservoirStorage\reservoirStorageRight\ReservoirStorageRight.vue
-->
<template>
  <div class="wrap">
    <div class="left_title" ref="titleHeight">
      <span>供水能力</span>
    </div>
    <section class="xsl_box" ref="oneHeight">
      <div class="second_title">
        <div class="name">
          <p>可供水量</p>
        </div>
        <!-- <div class="solid"></div>
        <div class="dashed">
          <div></div>
          <div></div>
        </div> -->
      </div>
      <div class="box_water_list">
        <div class="box_water_item" v-for="(item, index) in waterListOne" :key="index">
          <div class="status_item">
            <img :src="item.srcs" alt="" />
            <div class="status_right">
              <div class="top">
                <span>{{ item.nam }}</span>
              </div>
            </div>
          </div>
          <div class="box_wave_wrap">
            <!-- <div class="wave_center_wrapper"> -->
            <div class="wave_center" :style="{ top:`${item.Cent}%`}"></div>
            <!-- </div> -->
            <p>可供水量(万m³)</p>
            <span>{{ item.shuiwei }}</span>
          </div>
        </div>
      </div>
    </section>
    <section class="xsl_box" ref="twoHeight">
      <div class="second_title">
        <div class="name">
          <p>按规模类型分</p>
        </div>
        <!-- <div class="solid"></div>
        <div class="dashed">
          <div></div>
          <div></div>
        </div> -->
      </div>
      <div class="water_list">
        <!-- <div :class="[
            'water_item',
            index === waterIndex ? 'active' : '',
            index === 0 || index === 1 ? 'width50' : 'width33',
          ]" v-for="(item, index) in waterList" :key="index">
          <div class="wave_box" slot="reference">
            <div class="wave_wrap">
              <div class="kgs">
                <p>
                  {{ item.kgsPin }}
                </p>
                <i>{{ item.kgsUnit }}</i>
              </div>
              <div class="line"></div>
              <div class="knx">
                <span>可供水</span>
                <p>
                  {{ item.kgs }} <i>{{ item.kgsUnit }}</i>
                </p>
              </div>
            </div>
            <div class="wave_word">{{ item.name }}</div>
          </div>
        </div> -->
        <div class="water_item width50">
          <div class="wave_box" slot="reference">
            <div class="wave_wrap">
              <div class="kgs one">
                <!-- <p>
                  8960
                </p>
                <i>(万m³)</i> -->
              </div>
              <div class="line"></div>
              <div class="knx one">
                <p>
                  8960
                </p>
                <i>(万m³)</i>
              </div>
            </div>
            <div class="wave_word">大(1)型</div>
          </div>
        </div>
        <div class="water_item width50">
          <div class="wave_box" slot="reference">
            <div class="wave_wrap">
              <div class="kgs two">
                <!-- <p>
                  2140
                </p>
                <i>(万m³)</i> -->
              </div>
              <div class="line"></div>
              <div class="knx two">
                <p>
                  2140
                </p>
                <i>(万m³)</i>
              </div>
            </div>
            <div class="wave_word">大(1)型</div>
          </div>
        </div>
        <div class="water_item width33">
          <div class="wave_box" slot="reference">
            <div class="wave_wrap">
              <div class="kgs three">
                <!-- <p>
                  180
                </p>
                <i>(万m³)</i> -->
              </div>
              <div class="line"></div>
              <div class="knx three">
                <p>
                  180
                </p>
                <i>(万m³)</i>
              </div>
            </div>
            <div class="wave_word">大(1)型</div>
          </div>
        </div>
        <div class="water_item width33">
          <div class="wave_box" slot="reference">
            <div class="wave_wrap">
              <div class="kgs four">
                <!-- <p>
                  700
                </p>
                <i>(万m³)</i> -->
              </div>
              <div class="line"></div>
              <div class="knx four">
                <p>
                  700
                </p>
                <i>(万m³)</i>
              </div>
            </div>
            <div class="wave_word">大(1)型</div>
          </div>
        </div>
        <div class="water_item width33">
          <div class="wave_box" slot="reference">
            <div class="wave_wrap">
              <div class="kgs five">
                <p>
                  310
                </p>
                <i>(万m³)</i>
              </div>
              <div class="line"></div>
              <div class="knx five">
              </div>
            </div>
            <div class="wave_word">大(1)型</div>
          </div>
        </div>
      </div>
    </section>
    <!-- <section class="xsl_box">
      <div class="second_title">
        <div class="name">
          <p>按行政区划分</p>
        </div>
      </div>
      <bar-chart-double-click  :style="{ height: barHeight }"  v-loading="sghtfbLoading" class="htfbChart" :chartData="skfbData" @barClick="barClickAdcd"></bar-chart-double-click>
    </section> -->
    <section class="xsl_box">
      <div class="second_title">
        <div class="name">
          <p>重点水库</p>
        </div>
        <!-- <div class="solid"></div>
        <div class="dashed">
          <div></div>
          <div></div>
        </div> -->
      </div>
      <div class="point_title">
        <span>至供水死水位</span><span>至灌溉死水位</span><span>至发电死水位</span>
      </div>
      <p class="point_name">新安江水库</p>
      <div class="progress">
        <section class="parent_box">
          <section :style="{width:'80%'}" class="child_box">
          <span>629070(万m³)</span>
          </section>
        </section>
        <section class="parent_box">
          <section :style="{width:'80%'}" class="child_box">
          <span>629070(万m³)</span>
          </section>
        </section>
        <section class="parent_box">
          <section :style="{width:'80%'}" class="child_box">
          <span>629070(万m³)</span>
          </section>
        </section>
      </div>
      <p class="point_name">珊溪水库</p>
      <!-- <bar-chart-one :style="{ height: barHeight }" v-loading="sghtfbLoading" class="htfbChart" :chartData="skfbData" @barClick="barClickAdcd"></bar-chart-one> -->
      <div class="progress">
        <section class="parent_box">
          <section :style="{width:'80%'}" class="child_box">
          <span>43477(万m³)</span>
          </section>
        </section>
        <section class="parent_box">
          <section :style="{width:'80%'}" class="child_box">
          <span>43477(万m³)</span>
          </section>
        </section>
        <section class="parent_box">
          <section :style="{width:'80%'}" class="child_box">
          <span>43477(万m³)</span>
          </section>
        </section>
      </div>
    </section>
  </div>
</template>

<script>
import BarChartDoubleClick from "../components/BarChartDoubleClick";
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {
    BarChartDoubleClick,
  },
  data() {
    return {
      barHeight: '',
      waterListOne: [],
      allWater: '1394030',
      supplyDays: '00000400',
      forecastDays: '0002000',
      skfbData: {
        title: "按区域分",
        id: "skfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        adnmList: [],
        unit: "亿立方",
      },
      showSkfbChart: true,
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
          // 大1： 611380.99   大2： 200823.35 中： 160836.21       小1： 66932.89      小2： 31485.88
          // 1  3109974.000
          // 2  615810.000
          // 3  459722.500
          // 4  188480.002
          // 5  95063.612
          obj.scale = res.data[i].project_scale;
          if (res.data[i].capable < 10000) {
            obj.kgs = res.data[i].capable.toFixed(0);
            obj.kgsUnit = "万m³";
          } else {
            obj.kgs = (res.data[i].capable / 10000).toFixed(0);
            obj.kgsUnit = "(万m³)";
          }
          if (res.data[i].addtion < 10000) {
            obj.knx = res.data[i].addtion.toFixed(0);
            obj.knxUnit = "万m³";
          } else {
            obj.knx = (res.data[i].addtion / 10000).toFixed(0);
            obj.knxUnit = "(万m³)";
          }
          switch (res.data[i].project_scale) {
            case "1":
              obj.name = "大(1)型";
              obj.nam = "[至供水死水位]";
              obj.nams = '[到正常蓄水位]';
              obj.shuiwei = '1,390,000'
              obj.Cent = 60
              obj.srcs = require("@/assets/images/normal-water.png");
              obj.kgsPin = 8960
              break;
            case "2":
              obj.name = "大(2)型";
              obj.nam = "[至灌溉死水位]";
              obj.nams = '[到设计水位]';
              obj.shuiwei = '1,090,000'
              obj.Cent = 80
              obj.srcs = require("@/assets/images/design-water.png");
              obj.kgsPin = 2140
              break;
            case "3":
              obj.srcs = require("@/assets/images/high-water.png");
              obj.name = "中型";
              obj.nam = "[至发电死水位]";
              obj.nams = '[到防洪高水位]';
              obj.shuiwei = '890,000'
              obj.Cent = 90
              obj.kgsPin = 180
              break;
            case "4":
              obj.name = "小(1)型";
              obj.kgsPin = 700
              break;
            case "5":
              obj.name = "小(2)型";
              obj.kgsPin = 310
              break;
          }
          obj.scale = res.data[i].project_scale;

          // obj.number = res.data[i].storagePerc
          //   ? res.data[i].storagePerc > 100
          //     ? "100%"
          //     : res.data[i].storagePerc + "%"
          //   : "0%";
          obj.number = res.data[i].addtion.toFixed(0)
          obj.viewHeightOne =
            res.data[i].shuiwei / res.data[i].addtion * 100 > 100
              ? "0%"
              : (100 - res.data[i].storagePerc).toFixed(0) + "%";
          // obj.viewHeight = res.data[i].storagePerc && res.data[i].storagePerc > 100
          //   ? "0%"
          //   : (100 - res.data[i].storagePerc).toFixed(0) + "%";
          arr.push(obj);
        }
        this.waterList = arr;
        this.waterListOne = arr.slice(0, 3);
        this.getReservoirList();
      }
      this.$nextTick(() => {
        this.barHeight =
          this.$parent.$refs.screenLeft.clientHeight
          - this.$refs.oneHeight.clientHeight
          - this.$refs.twoHeight.clientHeight
          - this.$refs.titleHeight.clientHeight
          + "px";
      })
      this.loading_square = false;
    },



    // 水库库容
    async getSghtfbData() {
      this.sghtfbLoading = true;
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
        // let yData2 = [];
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
            //             杭州市	330100000000	670806.38
            // 宁波市	330200000000	99158.12
            // 温州市	330300000000	70633.44
            // 湖州市	330500000000	20618.89
            // 绍兴市	330600000000	62663.91
            // 金华市	330700000000	86815.02
            // 衢州市	330800000000	101525.27
            // 舟山市	330900000000	4914.62
            // 台州市	331000000000	88354.21
            // 丽水市	331100000000	188102.52
            switch (res.data[i].adnm) {
              case '杭州市':
                res.data[i].capableNum = 670806.38
                break;
              case '宁波市':
                res.data[i].capableNum = 99158.12
                break;
              case '温州市':
                res.data[i].capableNum = 70633.44
                break;
              case '湖州市':
                res.data[i].capableNum = 20618.89
                break;
              case '绍兴市':
                res.data[i].capableNum = 62663.91
                break;
              case '金华市':
                res.data[i].capableNum = 86815.02
                break;
              case '衢州市':
                res.data[i].capableNum = 101525.27
                break;
              case '舟山市':
                res.data[i].capableNum = 4914.62
                break;
              case '台州市':
                res.data[i].capableNum = 88354.21
                break;
              case '丽水市':
                res.data[i].capableNum = 188102.52
                break;
              default:
                break;
            }
            yData.push(
              // res.data[i].storagePerc ? res.data[i].storagePerc.toFixed(1) : 0
              res.data[i].capableNum
            );
            // yData.push((res.data[i].supply / 10000).toFixed(1));
            // yData2.push(
            //   res.data[i].addtion ? (res.data[i].addtion / 10000).toFixed(1) : 0
            // );
            adcdList.push(res.data[i].adcd);
            adnmList.push(res.data[i].adnm);
          }
        }
        this.skfbData.xData = xData;
        this.skfbData.yData = yData;
        // this.skfbData.yData2 = yData2;
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
                    kgsUnit = " (亿方)";
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
  .left_title {
    width: 100%;
    height: 35px;
    span {
      font: bold 20px italic;
      vertical-align: center;
      margin-left: 30px;
      line-height: 35px;
    }
    background: left/96% no-repeat url("../../../../assets/images/menu-bg.png");
  }
  .water_supplys > ul {
    margin: 0px 16px;
    width: calc(100% - 32px);
    @include flexbox();
    @include flexJC(space-around);
    @include flexflow(row nowrap);
    user-select: none;
    li {
      width: 32px;
      height: 36px;
      border: 1px solid #c1daf5;
      background: center/100% no-repeat
        url("../../../../assets/images/numbers_box.png");
      @include flexbox();
      @include flexJC(center);
      @include flexAI(center);
      margin: 0px 5px;
      span {
        text-align: center;
        font-size: 32px;
        font-weight: bold;
        color: #8c8c8c;
        line-height: 32px;
        &.blue {
          color: #193cc5;
        }
      }
    }
  }
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
      .water_item {
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
              border: 1px dashed #4997f7;
              @include flexbox();
              @include flexJC(center);
              @include flexflow(column wrap);
              &.one {
                height: 30%;
              }
              &.two {
                height: 30%;
              }
              &.three {
                height: 0%;
              }
              &.four {
                height: 15%;
              }
              &.five {
                height: 80%;
              }
              & > p {
                width: 100%;
                height: 30%;
                font-size: 24px;
                line-height: 24px;
              }
              & > i {
                width: 100%;
                height: 30%;
                font-size: 16px;
              }
              & > span {
                display: block;
                line-height: 14px;
                padding-bottom: 5px;
              }
            }
            .knx {
              background-color: #4997f7;
              color: #fff;
              height: 50%;
              box-sizing: border-box;
              padding-top: 10px;
              // border: 1px solid #95ceff;
              &.one {
                height: 70%;
              }
              &.two {
                height: 70%;
              }
              &.three {
                height: 100%;
              }
              &.four {
                height: 85%;
              }
              &.five {
                height: 20%;
              }
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
            // .kgs:nth-child(1) {
            //   height: 20%;
            // }
            // .knx:nth-child(1) {
            //   height: 80%;
            // }
          }
          .wave_word {
            text-align: center;
          }
        }
      }
    }
    .box_water_list {
      width: 100%;
      @include flexbox();
      @include flexJC(space-around);
      margin: 10px 5px;
      .box_water_item {
        flex: 1;
        height: 250px;
        // background: #f8f8f8;
        border-radius: 4px;
        border: 1px solid #f7b500af;
        // box-sizing: border-box;
        // box-shadow: 1px 0px 2px 0px rgba(0, 0, 0, 0.2);
        margin: 0px 10px;
        @include flexbox();
        @include flexJC(space-around);
        @include flexAI(center);
        @include flexflow(column nowrap);
        .status_item {
          @include flexbox();
          @include flexAC();
          @include flexflow(column nowrap);
          border: 1px solid #fff;
          box-sizing: border-box;
          padding: 2px 0;
          img {
            width: 50%;
          }
          .status_right {
            @include flexbox();
            @include flexAC();
            @include flexflow(column nowrap);
            .top {
              text-align: center;
              margin: 10px 0;
              color: #000000;
              font-size: 16px;
              font-weight: 600;
            }
          }
        }
        .box_wave_wrap {
          position: relative;
          border: 1px solid #4c9ef0;
          padding: 1px;
          box-sizing: border-box;
          margin: 0px auto;
          width: 90%;
          height: 60%;
          // width: 100px;
          // height: 100px;
          // border-radius: 50%;
          background: #e2ecff;
          // border-radius: 2px;
          border: 1px solid #b9d6f3;
          line-height: 50px;
          font-size: 14px;
          text-align: center;
          overflow: hidden;
          animation: box-water-wave linear infinite;
          margin-bottom: 5px;
          & > p {
            padding-top: 23px;
            font-size: 16px;
            font-weight: 600;
            line-height: 14px;
          }
          & > span {
            font-size: 20px;
            line-height: 24px;
            font-weight: bold;
          }
          .wave_center {
            z-index: 3;
            position: absolute;
            top: 50%;
            left: -50%;
            background: #68ADF8;
            opacity: 1;
            width: 250px;
            height: 250px;
            border-radius: 45%;
            animation: inherit;
            animation-duration: 10s;
          }
        }
        @keyframes box-water-wave {
          0% {
            transform: rotate(0deg);
          }
          100% {
            transform: rotate(360deg);
          }
        }
      }
    }
    .nurturing {
      padding: 16px;
      @include flexbox();
      @include flexJC(flex-start);
      @include flexflow(row nowrap);
      .nurturing_left {
        width: 100px;
        height: 100px;
        background: #edeff8;
        margin-right: 2px;
        @include flexbox();
        @include flexJC(center);
        @include flexAI(center);
        @include flexflow(column nowrap);
        img {
          width: 30%;
        }
        span {
          margin-top: 5px;
          width: 80px;
          height: 16px;
          font-size: 16px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #193cc5;
          line-height: 16px;
        }
      }
      .nurturing_right {
        flex: 3;
        @include flexbox();
        @include flexJC(flex-start);
        @include flexAI(center);
        @include flexflow(column nowrap);
        li {
          padding: 0px 10px;
          @include flexbox();
          @include flexJC(space-between);
          @include flexAI(center);
          width: calc(100% - 20px);
          height: 40%;
          background: center/100% no-repeat
            url("../../../../assets/images/list_bg.png");
          .bottom {
            text-align: center;
            .num {
              font-size: 16px;
              font-weight: 600;
            }
            .text {
              margin-left: 5px;
              font-size: 12px;
            }
          }
        }
        // &.width33 {
        //   width: 33%;
        // }
      }
    }
    .point_title {
      @include flexbox();
      @include flexJC(space-between);
      @include flexAI(center);
      @include flexflow(row wrap);
      width: calc(100% - 32px);
      height: 33px;
      background: #eff4fe;
      border-radius: 4px;
      border: 1px solid #edeef2;
      margin: 16px;
      span {
        padding: 0 10px;
        height: 20px;
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #000000;
        line-height: 20px;
      }
    }
    p.point_name {
      margin: 5px 0 0 16px;
      width: 90px;
      height: 25px;
      font-size: 18px;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
      line-height: 25px;
    }
    .progress {
      display: flex;
      @include flexJC(space-around);
      @include flexAI(center);
      @include flexflow(row wrap);
      width: calc(100% - 32px);
      margin: 10px 16px;
      .parent_box {
        flex: 1;
        height: 30px;
        border: 1px solid #e2efff;
        background: #e2efff;
        position: relative;
        margin-right: 5px;
        .child_box {
          width: 100%;
          height: 100%;
          background: #68adf8;
          border: 1px solid #68adf8;
          display: flex;
          @include flexJC(space-around);
          @include flexAI(center);
          @include flexflow(row wrap);
          position: relative;
        }
        span {
          display: contents;
          position: absolute;
          top: 18%;
          left: 5%;
          transform: translate(-50% 0);
          height: 24px;
          font-size: 16px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 600;
          color: rgba(0, 0, 0, 0.88);
          line-height: 16px;
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
