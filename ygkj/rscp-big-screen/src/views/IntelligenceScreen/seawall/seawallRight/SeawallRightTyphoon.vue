<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-20 10:48:02
 * @Description: 地图右侧-潮位预警
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawall\seawallRight\SeawallRightTyphoon.vue
-->
<template>
  <div class="wrap">
    <section class="typhoon-box">
      <div class="select_title">
        <div>
          <img src="../../../../assets/images/chart_item_icon.png" alt="" />
          梅花
        </div>
      </div>
      <div class="typhoon-detail">
        <!--留着备用 start-->
        <!-- <div class="typhoon-tap-detail">
          <div class="fangan_select_type">
            <div
              v-for="(item, index) in typhoonTypeList"
              :key="index"
              :class="{ active_type_btn: index === typhoonTypeIndex }"
              @click="checkTyphoonClick(index)"
              class="select_type_btn"
            >
              {{ item }}
            </div>
          </div>
          <div class="date-picker-box" v-if="typhoonTypeIndex === 1">
            <el-date-picker
              @change="handleChangeYear"
              :clearable="false"
              :picker-options="pickerOptions"
              class="date-picker"
              size="small"
              value-format="yyyy"
              v-model="typhoonYear"
              type="year"
            >
            </el-date-picker>
          </div>
        </div> -->
        <!-- <div class="typhoon-list" v-loading="loading_typhoon">
          <div class="current-typhoon" v-show="typhoonTypeIndex == 0">
            <div class="typhoon" v-if="currentTyphoon">
              <img
                @click="showTyphoon(currentTyphoon.tfid, true)"
                src="../../../../assets/images/typhoon-icon.png"
                alt=""
              />
              <div>
                <h3>
                  {{ currentTyphoon.tfid + currentTyphoon.name }}({{
                    currentTyphoon.enname
                  }})
                </h3>
                <p>
                  当前位置<span
                    >{{ currentTyphoon.lng }}°/{{ currentTyphoon.lat }}°</span
                  >
                </p>
                <p>
                  风速风力<span>{{ currentTyphoon.speed }}米/秒</span
                  ><span class="typhoon-red"
                    >({{ currentTyphoon.power }}级)</span
                  >
                </p>
                <p>
                  移速移向<span
                    >{{ currentTyphoon.movespeed }}公里/小时，{{
                      currentTyphoon.movedirection
                    }}</span
                  >
                </p>
              </div>
            </div>
            <div class="no-typhoon" v-if="currentTyphoon == 1">
              <img src="../../../../assets/images/no-typhoon.png" alt="" />
            </div>
          </div>
          <div class="history-Typhoon" v-if="typhoonTypeIndex == 1">
            <div v-if="carouselShow">
              <el-carousel
                class="carousel-Typhoon"
                trigger="click"
                height="100%"
                :autoplay="false"
                indicator-position="none"
              >
                <el-carousel-item
                  v-for="itemIndex in carouselTyphoonPage"
                  :key="itemIndex"
                  class="carousel-Typhoon-item"
                >
                  <div
                    @click="checkTyphoonItem(item)"
                    :class="{ 'active-Typhoon-item': activeTfid === item.tfid }"
                    v-for="item in typhoonPageList(itemIndex)"
                    :key="item.tfid"
                  >
                    <p>{{ item.tfid }}</p>
                    <h3>{{ item.name }}</h3>
                    <p>{{ item.enname }}</p>
                  </div>
                </el-carousel-item>
              </el-carousel>
            </div>
          </div>
        </div> -->
        <!--留着备用 end-->
        <div class="typhoon-list">
          <div class="current-typhoon">
            <div class="typhoon">
              <img src="../../../../assets/images/typhoon-icon.png" alt="" />
              <div>
                <h3>202212梅花(Muifa)</h3>
                <p>当前位置<span>122.00°/30.30°</span></p>
                <p>
                  风速风力<span>40米/秒</span
                  ><span class="typhoon-red">(13级)</span>
                </p>
                <p>移速移向<span>30公里/小时，北西</span></p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="fangan_box">
      <div class="select_title">
        <div>
          <img src="../../../../assets/images/chart_item_icon.png" alt="" />
          预报潮位
        </div>
      </div>
      <div class="fangan_list">
        <div
          class="fangan_item"
          v-for="(item) in planTypeList"
          :key="item"
        >
          <div class="fangan_item_title">
            <span>{{ item }}预报</span>
            <img src="../../../../assets/images/tip-icon.png" alt="" />
          </div>
          <div class="fangan_item_table" ref="tableContainer">
            <div class="scroll_item_title">
              <div>站点名称</div>
              <div>预报潮位(m)</div>
              <div>预报时间</div>
            </div>
            <div class="scroll_item_box">
              <div
                v-for="(item, idx) in cwzfbTableData"
                :key="idx + 'n'"
                class="scroll_item_ervice"
                @click="rowClick(item)"
              >
                <div>{{ item.stnm }}</div>
                <div>{{ item.realTimeTide || "-" }}</div>
                <div>{{ item.realTime || "-" }}</div>
              </div>
            </div>
            <!-- <el-table
              :height="tableHeight"
              v-loading="tableLoading"
              :data="tableData"
              stripe
              style="width: 100%"
              class="fangan_table_box"
              @row-click="clickSeawall"
            >
              <template slot="empty">
                <div class="table_nodata">
                  <img src="../../../../assets/images/noData.png" alt="" />
                </div>
              </template>
              <el-table-column prop="NAME" label="站点名称">
                <template slot-scope="scope">
                  <div :class="scope.row.colorType">
                    {{ scope.row.NAME ? scope.row.NAME : "-" }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="预报潮位" align="center">
                <template slot-scope="scope">
                  <div>
                    {{
                      scope.row.LOWEST_ELEVATION
                        ? scope.row.LOWEST_ELEVATION + "m"
                        : "-"
                    }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="预报时间" align="center">
                <template slot-scope="scope">
                  <div>
                    {{ scope.row.tm ? scope.row.tm.slice(0, 16) : "-" }}
                  </div>
                </template>
              </el-table-column>
            </el-table> -->
          </div>
        </div>
      </div>
    </section>

    <PrimaryDialog
      mainTitle="管控措施"
      custom-class="fangan_dialog"
      :visible="showFanganDialog"
      width="1200px"
      :showFooter="false"
      top="3.5vh"
      :fullscreen="fullscreen"
      @changeFullscreen="changeFullscreen"
      @handleClose="showFanganDialog = false"
    >
      <div class="img-control">
        <img src="../../../../assets/images/control-measures.png" alt="" />
      </div>
    </PrimaryDialog>
  </div>
</template>

<script>
import {
  seawallLeftStatisticApi,
  adListApi,
  cwzListApi,
  getPlanApi,
} from "@/api/api_seawall";
import BarChart from "@/components/BarChart";
import { drawPoint } from "@/utils/mapUtil";
import PrimaryDialog from "../../../skzgScreen/components/PrimaryDialog.vue";
import jquery from "@/plugins/jquery-1.11.3";
let currentTimes = new Date();
export default {
  name: "",
  components: {
    BarChart,
    PrimaryDialog,
  },
  data() {
    return {
      showFanganDialog: false,
      fullscreen: false,
      cwFlag: "1",
      seawallList: [],
      seawallAd: "",
      adOptions: [],
      yjhtfbData: {
        title: "预警分布",
        // yInterval: 2,
        id: "yjhtfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "条",
        noTitle: true,
      },
      htfbLoading: true,
      tableData: [],
      tableLoading: true,
      squareList: [],
      squareIndex: 0,
      squareValue: "overCnt",
      loading_square: true,
      seawallFangan: "",
      fanganOptions: [
        {
          label: "暂无方案",
          value: "",
        },
      ],
      listFlag: true,
      tableHeight: 0,
      cwztableHeight: 0,
      showChart: true,
      cwzfbTableLoading: true,
      cwzfbTableData: [],
      spanArr: [],
      yjNum: 0,
      fanganList: [],
      planName: "",
      planTypeIndex: 0,
      planTypeList: ["海洋", "综合", "水文", "研究院"],
      fanganIndex: 0,
      planId: "",
      showTable: true,
      canExport: false,
      currentTyphoon: null,
      typhoonTypeList: ["当前台风", "历史台风"],
      typhoonTypeIndex: 0,
      typhoonList: [],
      loading_typhoon: true,
      carouselTyphoonPage: 1,
      carouselShow: false,
      activeTfid: "",
      pickerOptions: {
        disabledDate(time) {
          let currentTime = new Date();
          return time.getFullYear() > currentTime.getFullYear();
        },
      },
      typhoonYear: String(currentTimes.getFullYear()),
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
    leftSelectAdcd(val) {
      this.getYbcwData();
    },

    cwFlag(val) {
      this.squareIndex = 0;
      this.squareValue = "overCnt";
      // this.fanganIndex = 0
      // this.planId = this.fanganList[0].id
      if (val == "1") {
        this.resizeTable();
        this.getYbcwData();
      } else {
        this.resizeTable2();
        this.getCwzList();
      }
    },
  },

  mounted() {
    // this.resizeTable2()
    this.getCwzList("F2DB010A1E87451685EAC761D76FEBFD");
    this.resizeTable();
    this.getPlanList();
    this.getCurrentTyphoon();
    this.getTyphoonList();
    let _this = this;
    window.addEventListener("resize", function () {
      _this.resizeTable();
    });
  },
  beforeDestroy() {
    this.$parent.removeTyphoonLayer();
    window.removeEventListener("resize");
  },
  methods: {
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
    },
    checkTyphoonClick(index) {
      if (this.typhoonTypeIndex === index) {
        return false;
      }
      this.typhoonTypeIndex = index;
      if (this.typhoonTypeIndex == 1) {
        this.typhoonYear = String(currentTimes.getFullYear());
        this.getTyphoonList(true);
      }
    },
    getCurrentTyphoon() {
      let that = this;
      jquery.ajax({
        url: "https://sk.zjwater.com/typhoonServer/Api/TyhoonActivity",
        type: "get",
        dataType: "jsonp",
        data: {},
        success: function (res) {
          if (res && res.length > 0 && res[0]) {
            that.activeTfid = res[0].tfid;
            that.showTyphoon(res[0].tfid);
            that.currentTyphoon = res[0];
          } else {
            that.currentTyphoon = 1;
          }
          that.$nextTick(() => {
            that.loading_typhoon = false;
          });
        },
      });
    },
    handleChangeYear() {
      this.loading_typhoon = true;
      this.carouselShow = false;
      this.getTyphoonList();
    },
    getTyphoonList(type) {
      let that = this;
      jquery.ajax({
        url:
          "https://sk.zjwater.com/typhoonServer/Api/TyphoonList/" +
          that.typhoonYear,
        type: "get",
        dataType: "jsonp",
        data: {},
        success: function (res) {
          that.typhoonList = res.reverse();
          if (that.typhoonList.length > 0) {
            that.carouselTyphoonPage = Math.ceil(that.typhoonList.length / 3);
            that.carouselShow = true;
            if (type) {
              that.activeTfid = that.typhoonList[0].tfid;
              that.showTyphoon(res[0].tfid, true);
            }
          }
          that.$nextTick(() => {
            that.loading_typhoon = false;
          });
        },
      });
    },
    checkTyphoonItem(item) {
      if (this.activeTfid === item.tfid) {
        return false;
      }
      this.activeTfid = item.tfid;
      this.showTyphoon(item.tfid, true);
    },
    typhoonPageList(index) {
      let startNum = (index - 1) * 3;
      let endNum = startNum + 3;
      return this.typhoonList.slice(startNum, endNum);
    },
    async showTyphoon(tfid, type) {
      await this.$parent.showTyphoon(tfid, type);
      // jquery.ajax({
      //   url: "http://typhoon.zjwater.gov.cn/Api/TyphoonInfo/" + res[0].tfid,
      //   type: "get",
      //   dataType: "jsonp",
      //   data: {},
      //   success: function (res) {
      //     if (res.length) {
      //       let mydata = {
      //         name: res[0].name,
      //         code: res[0].tfid,
      //         points: [],
      //       };
      //       // let nowDate = new Date('2021-07-25 13:00:00')
      //       res[0].points.map((item, i) => {
      //         item.lat = Number(item.lat);
      //         item.lng = Number(item.lng);
      //         item.index = i;
      //         item.type = "live";
      //         mydata.points.push(item);
      //         // let typhoonTime = new Date(item.time)
      //         // if(typhoonTime < nowDate){
      //         //   mydata.points.push(item);
      //         // }
      //       });
      //       that.tfLng = res[0].points[res[0].points.length - 1].lng;
      //       that.tfLat = res[0].points[res[0].points.length - 1].lat;
      //       const typhoon = new newTyphoon(
      //         that.map,
      //         mydata,
      //         that.typhoonsMap,
      //         that
      //       );
      //       typhoon.init();
      //     }
      //   },
      // });
    },
    getYbcwData() {
      this.getTideAlertData("forecast");
      this.getSeawallList();
      this.getLengthData();
    },
    // 预警海塘分布
    async getLengthData() {
      this.htfbLoading = true;
      this.yjhtfbData = {
        title: "预警分布",
        // yInterval: 2,
        id: "yjhtfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "条",
        noTitle: true,
      };
      let opt = {};
      if (this.planId != "") {
        opt = {
          statistic: "adcd",
          adcd: this.leftSelectAdcd
            ? this.leftSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          itemCategory: "forecast,planId",
          itemValue: this.squareValue + "," + this.planId,
        };
      } else {
        opt = {
          statistic: "adcd",
          adcd: this.leftSelectAdcd
            ? this.leftSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          itemCategory: "forecast",
          itemValue: this.squareValue,
        };
      }
      let res = await seawallLeftStatisticApi(opt);
      if (res.success) {
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            if (res.data[i].adnm.length > 2) {
              this.yjhtfbData.xData.push(
                res.data[i].adnm.substring(0, res.data[i].adnm.length - 1)
              );
            } else {
              this.yjhtfbData.xData.push(res.data[i].adnm);
            }
            this.yjhtfbData.yData.push(res.data[i].num);
            this.yjhtfbData.adcdList.push(res.data[i].adcd);
          }
          // this.showChart = false
          // this.$nextTick(()=>{
          //   this.showChart = true
          // })
        } else {
          this.showChart = false;
          this.$nextTick(() => {
            this.showChart = true;
          });
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }

      this.htfbLoading = false;
    },
    // adChange(val){
    //   this.getSeawallList()
    // },
    clickSeawall(row) {
      this.$parent.setView(row.LGTD, row.LTTD);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightType = "3";
      this.$parent.showOneSelectPoint([row], false, "cwyj");
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },
    clickCwzfb(row) {
      // this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.setView(row.stLgtd, row.stLttd);
      row.lat = row.stLttd;
      row.lng = row.stLgtd;
      row.LGTD = row.stLgtd;
      row.LTTD = row.stLttd;
      row.rightType = "6";
      this.$parent.showOneSelectPoint([row], true);
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },
    clickSquare(index) {
      if (this.listFlag) {
        this.showTable = false;
        this.$nextTick(() => {
          this.showTable = true;
        });
        // if(this.squareIndex === index){
        //   this.squareIndex = ''
        // }else{
        this.squareIndex = index;
        // }
        this.squareValue = this.squareList[index].type;
        this.getSeawallList();
        this.getLengthData();
      }
    },

    // 海塘分布柱状图点击事件
    barClickAdcd(index) {
      if (this.yjhtfbData.xData[index] == "省级") {
        this.$store.commit("SET_LEFT_SELECT_ADCD", "33");
      } else {
        this.$store.commit(
          "SET_LEFT_SELECT_ADCD",
          this.yjhtfbData.adcdList[index]
        );
      }
    },
    // async getAdOptions(){
    //   this.adOptions = []
    //   let res = await adListApi()
    //   if(res.success){
    //     if(res.rows.length){
    //       for (let i = 0; i < res.rows.length; i++) {
    //         this.adOptions.push({
    //           label: res.rows[i].coastalName,
    //           value: res.rows[i].id
    //         })
    //       }
    //     }
    //     this.adOptions.unshift({
    //       label: '全部岸段',
    //       value: ''
    //     })
    //   }
    // },
    async getSeawallList() {
      if (this.listFlag) {
        this.$parent.$refs.seawallMap.removeAllLayer();
        this.$parent.$refs.seawallMap.closePopup();
        this.$parent.$refs.seawallMap.initView();
        this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", false);
        this.listFlag = false;
        this.tableData = [];
        this.tableLoading = true;
        this.canExport = false;
        let opt = {};
        if (this.seawallAd) {
          opt = {
            statistic: "mapList",
            itemCategory: "costalId,forecast",
            itemValue: this.seawallAd + "," + this.squareValue,
            adcd: this.leftSelectAdcd
              ? this.leftSelectAdcd
              : this.$localData("get", "userInfo").adcd,
          };
        } else {
          if (this.planId != "") {
            opt = {
              statistic: "mapList",
              itemCategory: "forecast,planId",
              itemValue: this.squareValue + "," + this.planId,
              adcd: this.leftSelectAdcd
                ? this.leftSelectAdcd
                : this.$localData("get", "userInfo").adcd,
            };
          } else {
            opt = {
              statistic: "mapList",
              itemCategory: "forecast",
              itemValue: this.squareValue,
              adcd: this.leftSelectAdcd
                ? this.leftSelectAdcd
                : this.$localData("get", "userInfo").adcd,
            };
          }
        }
        let res = await seawallLeftStatisticApi(opt);
        if (res.success) {
          let pointArr = [];
          let lineArr = [];
          if (res.data.length) {
            this.canExport = true;
            this.tableData = res.data;
            res.data.map((v, i) => {
              let colorType = "";
              if (v.forecast > v.TIDE_ALERT_BLUE) {
                if (
                  v.forecast < v.TIDE_ALERT_YELLOW ||
                  v.forecast == v.TIDE_ALERT_YELLOW
                ) {
                  colorType = "blue";
                } else if (
                  v.forecast < v.TIDE_ALERT_ORANGE ||
                  v.forecast == v.TIDE_ALERT_ORANGE
                ) {
                  colorType = "yellow";
                } else if (
                  v.forecast < v.TIDE_ALERT_RED ||
                  v.forecast == v.TIDE_ALERT_RED
                ) {
                  colorType = "orange";
                } else {
                  colorType = "red";
                }
              } else {
                colorType = "default";
              }
              this.tableData[i].colorType = colorType;
              pointArr.push({
                lat: v.LTTD,
                lng: v.LGTD,
                NAME: v.NAME,
                PRCD: v.PRCD,
                rightType: "3",
                forecast_time: v.forecast_time,
                colorType: colorType,
                forecast: v.forecast,
                status: v.status,
                DESIGN_TIDE_STANDARD: v.DESIGN_TIDE_STANDARD,
                DESIGN_TIDE_LEVEL: v.DESIGN_TIDE_LEVEL,
                LOWEST_ELEVATION: v.LOWEST_ELEVATION,
                src: require("@/assets/images/map/gao_risk.png"),
              });
              lineArr.push({
                line: v.COORPOT
                  ? JSON.parse(
                      v.COORPOT.replace(new RegExp("&quot;", "gm"), '"')
                    )[0]
                  : "",
                level: v.project_scale,
              });
              v.lineArr = [];
              v.lineArr.push({
                line: v.COORPOT
                  ? JSON.parse(
                      v.COORPOT.replace(new RegExp("&quot;", "gm"), '"')
                    )[0]
                  : "",
                level: v.project_scale,
              });
            });
          }
          this.$parent.showMapPoints(pointArr, "cwyj");
          this.$parent.showMapLines(lineArr);
        }
        this.listFlag = true;
        this.tableLoading = false;
        this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", true);
      }
    },

    async getTideAlertData(type) {
      this.loading_square = true;
      this.squareList = [];
      let res = await seawallLeftStatisticApi({
        statistic: type,
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        itemCategory: "planId",
        itemValue: this.planId,
      });
      if (res.success) {
        this.yjNum = res.data[0]["total"];
        this.squareList.push(
          // {
          //   type: 'blue',
          //   name: '蓝色',
          //   value: res.data[0]['blue']
          // },
          // {
          //   type: 'yellow',
          //   name: '黄色',
          //   value: res.data[0]['yellow']
          // },
          // {
          //   type: 'orange',
          //   name: '橙色',
          //   value: res.data[0]['orange']
          // },
          // {
          //   type: 'red',
          //   name: '红色',
          //   value: res.data[0]['red']
          // },
          {
            type: "overCnt",
            name: "超设计潮位",
            value: res.data[0]["overCnt"],
            length: res.data[0]["overLength"]
              ? (res.data[0]["overLength"] / 1000).toFixed(1)
              : 0,
          },
          {
            type: "sickCnt",
            name: "超病险塘能力",
            value: res.data[0]["sickCnt"],
            length: res.data[0]["sickLength"]
              ? (res.data[0]["sickLength"] / 1000).toFixed(1)
              : 0,
          },
          {
            type: "overTop",
            name: "超塘顶高程",
            value: res.data[0]["overTop"],
            length: res.data[0]["overTopLength"]
              ? (res.data[0]["overTopLength"] / 1000).toFixed(1)
              : 0,
          }
          // {
          //   type: 'nearCnt',
          //   name: '临设',
          //   value: res.data[0]['nearCnt']
          // },
          // {
          //   type: 'closeCnt',
          //   name: '近设',
          //   value: res.data[0]['closeCnt']
          // },
          // {
          //   type: 'overDesign',
          //   name: '超标准',
          //   value: res.data[0]['overDesign']
          // },
        );
      }
      this.loading_square = false;
    },
    resizeTable() {
      this.$nextTick(function () {
        const height = this.$refs.tableContainer[0].clientHeight;
        this.tableHeight = height;
        // window.onresize = function() {
        //   const height = this.$refs.tableContainer.clientHeight;
        //   this.tableHeight = height - 80
        // }
      });
    },
    resizeTable2() {
      this.$nextTick(function () {
        const height = this.$refs.tableContainer2.clientHeight;
        console.log(height, "height");
        this.cwztableHeight = height - 60;
        let _this = this;
        // window.onresize = function() {
        //   const height = this.$refs.tableContainer2.clientHeight;
        //   this.cwztableHeight = height - 60
        // }
      });
    },
    async getCwzList(planId) {
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      this.$parent.$refs.seawallMap.initView();
      this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", false);
      this.cwzfbTableData = [];
      this.cwzfbTableLoading = true;
      let res = await cwzListApi({
        planId: planId,
      });
      let pointArr = [];
      if (res.success) {
        this.cwzfbTableData = res.rows;
        console.log(this.cwzfbTableData);
        return false;
        res.rows.map((v, i) => {
          if (v.forecast) {
            let colorType = "";
            if (v.forecast > v.tideAlertBlue) {
              if (
                v.forecast < v.tideAlertYellow ||
                v.forecast == v.tideAlertYellow
              ) {
                colorType = "blue";
              } else if (
                v.forecast < v.tideAlertOrange ||
                v.forecast == v.tideAlertOrange
              ) {
                colorType = "yellow";
              } else if (
                v.forecast < v.tideAlertRed ||
                v.forecast == v.tideAlertRed
              ) {
                colorType = "orange";
              } else {
                colorType = "red";
              }
            } else {
              colorType = "default";
            }
            v.colorType = colorType;
            this.cwzfbTableData.push(v);
            pointArr.push({
              lat: v.stLttd,
              lng: v.stLgtd,
              coastalName: v.coastalName,
              forecastTime: v.forecastTime,
              forecast: v.forecast,
              colorType: colorType,
              rightType: "6",
              stnm: v.stnm,
              src: require("@/assets/images/map/seawall.png"),
            });
          }
        });
        this.$parent.showMapPoints(pointArr);
        // this.$parent.showOverlays(pointArr)
        this.spanArr = [];
        let contactDot = 0;
        this.cwzfbTableData.forEach((item, index) => {
          item.index = index;
          if (index === 0) {
            this.spanArr.push(1);
          } else {
            if (item.stnm === this.cwzfbTableData[index - 1].stnm) {
              this.spanArr[contactDot] += 1;
              this.spanArr.push(0);
            } else {
              this.spanArr.push(1);
              contactDot = index;
            }
          }
        });
      }
      this.cwzfbTableLoading = false;
      this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", true);
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col,
        };
      }
    },
    checkPlanClick(index) {
      if (this.planTypeIndex === index) {
        return false;
      }
      this.planTypeIndex = index;
      this.getPlanList();
    },
    async getPlanList() {
      let res = await getPlanApi({
        isMatch: 1,
        fetchAll: true,
        planType: this.planTypeList[this.planTypeIndex],
      });
      if (res.success) {
        this.fanganList = res.rows;
        if (this.fanganList.length > 0) {
          this.planId = this.fanganList[0].id;
        } else {
          this.planId = "";
        }
        this.getYbcwData();
      }
    },
    fanganClick() {
      this.squareIndex = 0;
      this.squareValue = "overCnt";
      // this.fanganIndex = index;
      if (this.cwFlag == "1") {
        this.getYbcwData();
      } else {
        this.getCwzList();
      }
    },
    fanganManage() {
      let url =
        "https://sk.zjwater.com/mgt/coastalTideAlert/forecastList" +
        // let url = 'http://192.168.2.97:8082/mgt/coastalTideAlert/forecastList' +
        "?from=bigscreen&token=" +
        this.$localData("get", "token");
      this.$parent.openFanganManage(url);
    },
    exportList() {
      let adcd = this.leftSelectAdcd
        ? this.leftSelectAdcd
        : this.$localData("get", "userInfo").adcd;
      let token = this.$localData("get", "token");
      window.open(
        this.$config.host +
          `/mgt/bm/seawallei/toExcel` +
          `?adcd=${adcd}` +
          `&itemCategory=forecast,planId` +
          `&itemValue=${this.squareValue},${this.planId}` +
          `&token=${token}`
      );
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  height: 100%;
  .select_title {
    padding: 16px 16px 10px 16px;
    @include flexbox();
    @include flexAC();
    @include flexJC(space-between);
    & > div {
      @include flexbox();
      @include flexAC();
      color: #000;
      font-weight: 500;
      font-size: 18px;
      img {
        margin-right: 5px;
      }
      &:first-child {
        font-weight: bold;
      }
    }
  }
  .typhoon-box {
    height: 17%;
    .typhoon-detail {
      height: calc(100% - 42px);
      .typhoon-tap-detail {
        display: flex;
        justify-content: space-between;
        padding-right: 15px;
        .fangan_select_type {
          padding-top: 0;
          padding-bottom: 8px;
          justify-content: flex-start;
          > div {
            margin-right: 8px;
          }
        }
        .date-picker-box {
          width: 150px;
          .date-picker {
            width: 100%;
            position: relative;
            top: -2px;
          }
        }
      }
      .typhoon-list {
        // height: calc(100% - 38px);
        height: 100%;
        .current-typhoon {
          height: 100%;
          display: flex;
          flex-direction: column;
          justify-content: center;
          .typhoon {
            display: flex;
            padding: 0 0 0 25px;
            width: 100%;
            box-sizing: border-box;
            img {
              width: 79px;
              height: 81px;
              margin-top: 10px;
              cursor: pointer;
            }
            div {
              padding-left: 18px;
              h3 {
                font-family: "youshe";
                font-size: 20px;
                color: #000000;
                padding-bottom: 6px;
                font-weight: 700;
              }
              p {
                font-size: 16px;
                font-weight: 600;
                color: #333333;
                padding-bottom: 6px;
                span {
                  font-size: 18px;
                  padding-left: 16px;
                  color: rgba(25, 60, 197, 1);
                  &.typhoon-red {
                    padding-left: 0px;
                    color: rgba(255, 0, 0, 1);
                  }
                }
              }
            }
          }
          .no-typhoon {
            padding: 0 16px;
            height: 100%;
            img {
              width: 100%;
              height: 100%;
            }
          }
        }
        .history-Typhoon {
          height: 100%;
          > div {
            height: 100%;
            .carousel-Typhoon {
              height: 100%;
              .carousel-Typhoon-item {
                display: flex;
                padding: 0 15px;
                > div {
                  width: 117px;
                  height: calc(100% - 2px);
                  background: #f8f8f8;
                  border-radius: 2px;
                  border: 1px solid #d6d6d6;
                  margin-right: 6px;
                  display: flex;
                  flex-direction: column;
                  justify-content: space-between;
                  align-items: center;
                  padding: 12px 0;
                  box-sizing: border-box;
                  cursor: pointer;
                  p {
                    font-size: 14px;
                    color: rgba(0, 0, 0, 0.88);
                  }
                  h3 {
                    font-size: 20px;
                    font-weight: bold;
                    color: #000000;
                  }
                  &:last-child {
                    margin-right: 0;
                  }
                  &.active-Typhoon-item {
                    background: #193cc5;
                    border-color: #193cc5;
                    h3 {
                      color: #fff;
                    }
                    p {
                      color: #ffffff;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  .fangan_box {
    height: 82.8%;
    .fangan_list {
      display: flex;
      justify-content: space-between;
      flex-direction: column;
      height: calc(100% - 55px);
      padding: 0 15px;
      .fangan_item {
        height: 25%;
        box-sizing: border-box;
        .fangan_item_title {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 8px 0 7px;
          height: 32px;
          span {
            color: #000000;
            font-size: 16px;
          }
          img {
            width: 19px;
            height: 20px;
            cursor: pointer;
          }
        }
        .fangan_item_table {
          height: calc(100% - 47px);
          .scroll_item_title {
            height: 48px;
            background: #f9f9f9;
            padding: 0 16px;
            align-items: center;
            display: flex;
            > div {
              color: rgba(0, 0, 0, 0.48);
              font-size: 14px;
              text-align: center;
              &:first-child {
                text-align: left;
                width: 33%;
              }
              &:nth-child(2) {
                width: 25%;
              }
              &:last-child {
                // text-align: right;
                width: 42%;
              }
            }
          }
          .scroll_item_box {
            height: calc(100% - 48px);
            width: 100%;
            .scroll_item {
              width: 100%;
              .scroll_item_ervice {
                height: 48px;
                padding: 0 16px;
                align-items: center;
                display: flex;
                &:nth-child(2n) {
                  background-color: #f9f9f9;
                }
                > div {
                  color: rgba(0, 0, 0, 0.88);
                  font-size: 14px;
                  text-align: center;
                  &:first-child {
                    text-align: left;
                    width: 33%;
                  }
                  &:nth-child(2) {
                    width: 27%;
                  }
                  &:last-child {
                    // text-align: right;
                    width: 40%;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  .fbcyj_box {
    height: 30%;
    .select_square {
      height: calc(100% - 80px);
      box-sizing: border-box;
      @include flexbox();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      align-items: center;
      padding: 8px 16px 16px;
      .square_item {
        box-sizing: border-box;
        width: 23.5%;
        height: 116px;
        text-align: center;
        cursor: pointer;
        border-radius: 2px;
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
          padding: 12px 0 6px 0;
        }
        i {
          font-size: 14px;
          padding-left: 4px;
        }

        & > span,
        & > h3 {
          line-height: 24px;
          font-size: 24px;
          color: #000;
          font-weight: 500;
        }
        & > h3 {
          padding-top: 8px;
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
    .fangan_select_type {
      padding-bottom: 4px;
      padding-top: 4px;
      .select_type_btn {
        padding: 8px 0;
        width: 31.5%;
        text-align: center;
        cursor: not-allowed;
        &:first-child {
          cursor: default;
        }
      }
    }
  }
  .htfb_box {
    height: 43%;
    .htfbChart {
      margin-top: 3px;
      width: 100%;
      height: calc(100% - 55px);
    }
    .control-measures-box {
      padding: 2px 16px 0;
      .control-measures {
        width: 100%;
        height: 48px;
        background: #f8f8f8;
        border-radius: 4px;
        border: 1px solid #d6d6d6;
        font-size: 16px;
        font-weight: bold;
        color: #2e3d53;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        img {
          width: 24px;
          margin-left: 15px;
        }
      }
    }
  }
  .seawallList_wrap {
    &.hei2 {
      height: calc(100% - 160px);
    }
  }
  .seawallList_box {
    height: calc(100% - 356px);
  }
  .cwzfblist_wrap {
    height: calc(100% - 105px);
  }
  .table_list {
    height: calc(100% - 42px);
    box-sizing: border-box;
    padding-bottom: 16px;
    overflow: auto;
    .el-table td div {
      & > .bold {
        font-weight: bold;
      }
      & > .blue {
        color: #2478f0;
      }
      & > .yellow {
        color: #f7c739;
      }
      & > .orange {
        color: #eb7e65;
      }
      & > .red {
        color: #e02020;
      }
    }
  }
}
.fangan_select_type {
  padding: 13px 16px 12px 16px;
  @include flexbox();
  @include flexJC(space-between);
  @include flexflow(row wrap);
  // overflow-x: auto;
  // overflow-y: hidden;
  .select_type_btn {
    padding: 8px 15px;
    cursor: pointer;
    background-color: #f8f8f8;
    font-size: 14px;
    color: rgba(0, 0, 0, 0.48);
    &.active_type_btn {
      background-color: #193cc5;
      color: #ffffff;
    }
  }
}

.img-control {
  text-align: center;
  height: 100%;
  overflow: auto;
  padding-bottom: 25px;
  box-sizing: border-box;
  img {
    width: 65%;
    vertical-align: bottom;
  }
}
</style>
<style lang="scss">
.select_title {
  .el-radio {
    color: #000;
    font-size: 16px;
    margin-right: 8px;
  }
  .el-radio__input.is-checked + .el-radio__label {
    color: #000;
  }
  .el-radio__label {
    padding-left: 4px;
  }
  .el-radio__input.is-checked .el-radio__inner {
    border-color: #1341c5;
    background: #1341c5;
  }
  .el-select .el-input__inner {
    height: 32px;
  }
  .el-input__icon {
    line-height: unset;
  }
}

#fangan_select {
  padding: 0 16px 12px 16px;
  // overflow-x: auto;
  // overflow-y: hidden;
  .el-select {
    width: 100%;
    padding: 8px 0;
    background-color: #f8f8f8;
    // height: 32px;
    .el-input__inner {
      border: none;
      background-color: #f8f8f8;
      &::-webkit-input-placeholder {
        /* Chrome/Opera/Safari */
        color: #606266;
      }
      &::-moz-placeholder {
        /* Firefox 19+ */
        color: #606266;
      }
      &:-ms-input-placeholder {
        /* IE 10+ */
        color: #606266;
      }
      &:-moz-placeholder {
        /* Firefox 18- */
        color: #606266;
      }
    }
    .el-input__suffix {
      position: absolute;
      right: 5px;
      &::after {
        content: "";
        position: absolute;
        left: -5px;
        top: 50%;
        margin-top: -8px;
        width: 1px;
        height: 16px;
        background-color: #e5e5e5;
      }
      .el-input__suffix-inner {
        .el-select__caret {
          color: rgba(0, 0, 0, 0.48);
        }
        .el-icon-arrow-up:before {
          content: "\e78f";
        }
      }
    }
  }
}

// #fangan_select::-webkit-scrollbar-track {
//   /*滚动条里面轨道*/
//   // -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
//   background: #fff;
//   // background-color: #fff;
//   // display: none

// }
.noFangan {
  text-align: center;
  padding-top: 10px;
}

.fangan_item_table {
  height: calc(100% - 47px);
  .fangan_table_box {
    &.el-table th {
      background-color: #f9f9f9;
    }
  }
}
</style>