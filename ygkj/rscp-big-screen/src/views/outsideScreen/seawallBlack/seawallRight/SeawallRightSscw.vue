<!--
 * @Author: hanyu
 * @LastEditTime: 2021-07-24 10:31:48
 * @Description: 地图右侧-潮位预警
 * @FilePath: /rscp-big-screen/src/views/bigScreen/seawall/seawallRight/SeawallRightSscw.vue
-->
<template>
  <div class="wrap">
    <div class="select_title">
      <!-- <div>
        <img src="../../../../assets/images/chart_item_icon.png" alt="">
        数据来源
      </div> -->
      <div>
        <el-radio
          :disabled="!listFlag"
          v-model="cwFlag"
          label="1"
          class="chart_title_black"
          >实时潮位</el-radio
        >
        <el-radio
          :disabled="!listFlag"
          v-model="cwFlag"
          label="2"
          class="chart_title_black"
          >潮位站分布</el-radio
        >
      </div>
    </div>
    <div class="seawallList_wrap hei1" v-show="cwFlag == '1'">
      <section class="fbcyj_box">
        <div>
          <div class="chart_title_black">
            <!-- <img src="../../../../assets/images/chart_item_icon.png" alt=""> -->
            实时潮位
          </div>
          <!-- <div>
            <el-select v-model="seawallAd" :disabled="!listFlag" clearable placeholder="请选择" @change="adChange">
              <el-option v-for="item in adOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </div> -->
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
            <span>{{ item.value }}<i>条</i></span>
            <h3>{{ item.length }}<i>公里</i></h3>
          </div>
        </div>
      </section>
      <section class="htfb_box">
        <bar-chart
          v-if="showChart"
          v-loading="htfbLoading"
          class="htfbChart"
          :chartData="yjhtfbData"
          @barClick="barClickAdcd"
        ></bar-chart>
      </section>
      <section class="seawallList_box">
        <div class="chart_title_black">
          <!-- <img src="../../../../assets/images/chart_item_icon.png" alt=""> -->
          海塘列表
        </div>
        <div class="table_list_diy">
          <table v-if="showTable" v-loading="tableLoading" :height="tableHeight">
            <thead>
              <tr>
                <td style="width:20%">海塘名称</td>
                <td style="width:20%" v-if="squareIndex === 0">
                  塘顶最低高程
                </td>
                <td style="width:20%" v-if="squareIndex === 1">安全状态</td>
                <td
                  style="width:20%"
                  v-if="squareIndex === '' || squareIndex === 1"
                >
                  设计潮位
                </td>
                <td
                  style="width:20%"
                  v-if="squareIndex === '' || squareIndex === 1"
                >
                  实时潮位
                </td>
                <td style="width:20%">
                  时间
                </td>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(item, index) in tableData"
                :key="index"
                @click="clickSeawall(item)"
              >
                <td style="width:20%">{{ item.NAME }}</td>
                <td style="width:20%" v-if="squareIndex === 0">
                  {{ item.LOWEST_ELEVATION ? item.LOWEST_ELEVATION : "-" }}
                </td>
                <td style="width:20%" v-if="squareIndex === 1">
                  {{ item.status ? item.status : "-" }}
                </td>
                <td
                  style="width:20%"
                  v-if="squareIndex === '' || squareIndex === 1"
                >
                  {{
                    item.DESIGN_TIDE_LEVEL ? item.staDESIGN_TIDE_LEVELtus : "-"
                  }}
                </td>
                <td
                  style="width:20%"
                  v-if="squareIndex === '' || squareIndex === 1"
                >
                  {{ item.tide ? item.tide + "m" : "-" }}
                </td>
                <td style="width:20%">
                  {{ item.tm ? item.tm.slice(0, 16) : "-" }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- <div class="table_list" ref="tableContainer">
          <el-table :height="tableHeight" v-if="showTable" v-loading="tableLoading" :data="tableData" stripe style="width: 100%" @row-click="clickSeawall">
            <template slot="empty">
              <div class="table_nodata">
                <img src="../../../../assets/images/noData.png" alt="" />
              </div>
            </template>
            <el-table-column :width=" squareIndex === 1 ? '60px' : ''" prop="NAME" label="海塘名称">
              <template slot-scope="scope">
                <div :class="scope.row.colorType">{{scope.row.NAME ? scope.row.NAME : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column v-if="squareIndex === 0" label="塘顶最低高程" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.LOWEST_ELEVATION ? scope.row.LOWEST_ELEVATION + 'm' : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column v-if="squareIndex === 1" label="安全状态" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.status ? scope.row.status : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column v-if="squareIndex === '' || squareIndex === 1" label="设计潮位" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.DESIGN_TIDE_LEVEL ? scope.row.DESIGN_TIDE_LEVEL + 'm' : '-'}}</div>
              </template>
            </el-table-column>


            
            
            
            <el-table-column v-if="squareIndex === 6" prop="DESIGN_TIDE_LEVEL" label="临设" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.DESIGN_TIDE_LEVEL ? scope.row.DESIGN_TIDE_LEVEL : '-'}}</div>
              </template>
            </el-table-column>
            <el-table-column v-if="squareIndex === 7" prop="DESIGN_TIDE_LEVEL" label="近设" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.DESIGN_TIDE_LEVEL ? scope.row.DESIGN_TIDE_LEVEL : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column v-if="squareIndex === 8" label="防潮标准" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.design ? scope.row.design + 'm' : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column v-if="squareIndex === 8" label="设计标准" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.DESIGN_TIDE_STANDARD ? scope.row.DESIGN_TIDE_STANDARD + '年' : '-'}}</div>
              </template>
            </el-table-column>
            


            <el-table-column v-if="squareIndex === 0" prop="TIDE_ALERT_BLUE" label="蓝色" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.TIDE_ALERT_BLUE ? scope.row.TIDE_ALERT_BLUE + 'm' : '-'}}</div>
              </template>
            </el-table-column>
            <el-table-column v-if="squareIndex === 0 || squareIndex === 1" prop="TIDE_ALERT_YELLOW" label="黄色" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.TIDE_ALERT_YELLOW ? scope.row.TIDE_ALERT_YELLOW + 'm' : '-'}}</div>
              </template>
            </el-table-column>
            <el-table-column v-if="squareIndex === 1 || squareIndex === 2" prop="TIDE_ALERT_ORANGE" label="橙色" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.TIDE_ALERT_ORANGE ? scope.row.TIDE_ALERT_ORANGE + 'm' : '-'}}</div>
              </template>
            </el-table-column>
            <el-table-column v-if="squareIndex === 2 || squareIndex === 3" prop="TIDE_ALERT_RED" label="红色" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.TIDE_ALERT_RED ? scope.row.TIDE_ALERT_RED + 'm' : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column width="60px" label="实时潮位" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.tide ? scope.row.tide + 'm' : '-'}}</div>
              </template>
            </el-table-column>
            <el-table-column :width=" squareIndex === 0 || squareIndex === 1 || squareIndex === 2 || squareIndex === 3 ? '60px' : ''" label="时间" align="center">
            <el-table-column label="时间" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.tm ? scope.row.tm.slice(0, 16) : '-'}}</div>
              </template>
            </el-table-column>
          </el-table> 
        </div> -->
      </section>
    </div>
    <div style="height:100%" v-show="cwFlag == '2'">
      <div class="chart_title_black">
        <!-- <img src="../../../../assets/images/chart_item_icon.png" alt="" /> -->
        潮位站分布
      </div>
      <div class="table_list_diy">
        <table v-loading="cwzfbTableLoading" :height="cwztableHeight">
          <thead>
            <tr>
              <td style="width:25%" rowspan="2">潮位代表站</td>
              <td style="width:50%" colspan="2">实时潮位</td>
              <td style="width:25%" rowspan="2">潮位/m</td>
            </tr>
            <tr>
              <td style="width:25%">时间</td>
              <td style="width:25%">潮位/m</td>
            </tr>
          </thead>
          <tbody >
            <tr
              v-for="(item, index) in cwzfbTableData"
              :key="index"
              @click="clickCwzfb(item)"
            >
              <td style="width:25%">{{ item.stnm }}</td>
              <td style="width:25%">
                {{ item.tm ? item.tm : "-" }}
              </td>
              <td style="width:25%">
               {{ item.tide ? item.tide : "-" }}
              </td>
               <td style="width:25%">
               {{ item.coastalName ? item.coastalName : "-" }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <!-- <div class="table_list" ref="tableContainer2">
        <el-table
          :height="cwztableHeight"
          v-loading="cwzfbTableLoading"
          :data="cwzfbTableData"
          stripe
          style="width: 100%"
          @row-click="clickCwzfb"
          :span-method="objectSpanMethod"
        >
          <template slot="empty">
            <div class="table_nodata">
              <img src="../../../../assets/images/noData.png" alt="" />
            </div>
          </template>
          <el-table-column label="潮位代表站" align="center">
            <template slot-scope="scope">
              <div>{{ scope.row.stnm ? scope.row.stnm : "-" }}</div>
            </template>
          </el-table-column>
          <el-table-column label="实时潮位" align="center">
            <el-table-column label="时间" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.tm ? scope.row.tm : "-" }}</div>
              </template>
            </el-table-column>
            <el-table-column label="潮位/m" align="center">
              <template slot-scope="scope">
                <div :class="scope.row.colorType">
                  {{ scope.row.tide ? scope.row.tide : "-" }}
                </div>
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column
            prop="coastalName"
            label="关联岸段"
            align="center"
          ></el-table-column>
        </el-table>
      </div> -->
    </div>
  </div>
</template>

<script>
import { seawallLeftStatisticApi, cwzListApi } from "@/api/api_seawall";
import BarChart from "../components/BarChart.vue";

export default {
  name: "",
  props: {},
  components: {
    BarChart,
  },
  data() {
    return {
      cwFlag: "1",
      seawallList: [],
      seawallAd: "",
      adOptions: [],
      yjhtfbData: {
        title: "预警分布",
        yInterval: 1,
        id: "yjhtfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "条",
      },
      htfbLoading: true,
      tableData: [],
      tableLoading: true,
      squareList: [],
      squareIndex: 0,
      squareValue: "overTop",
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
      fanganIndex: 0,
      showTable: true,
    };
  },

  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.getSeawallList();
    },
    cwFlag(val) {
      this.squareIndex = 0;
      this.squareValue = "overTop";
      if (val == "1") {
        this.resizeTable();
        this.getSscwData();
      } else {
        this.resizeTable2();
        this.getCwzList();
      }
    },
  },

  mounted() {
    // this.resizeTable2()
    // this.getCwzList()
    this.resizeTable();
    this.getSscwData();
  },

  methods: {
    getSscwData() {
      this.getTideAlertData();
      this.getSeawallList();
      this.getLengthData();
    },
    // 预警海塘分布
    // async getLengthData(){
    //   this.htfbLoading = true
    //   this.yjhtfbData = {
    //     title: "预警分布",
    //     yInterval: 1,
    //     id: 'yjhtfbChart',
    //     xData: [],
    //     yData: [],
    //     adcdList: [],
    //     unit: '条'
    //   }
    //   this.showChart = false
    //   this.$nextTick(()=>{
    //     this.showChart = true
    //   })
    //   this.htfbLoading = false
    // },
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
      };
      let res = await seawallLeftStatisticApi({
        statistic: "adcd",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        itemCategory: "tideAlert",
        itemValue: this.squareValue,
      });
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
      row.rightType = "8";
      this.$parent.showOneSelectPoint([row]);
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
      row.rightType = "7";
      this.$parent.showOneSelectPoint([row], true);
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },
    clickSquare(index) {
      this.showTable = false;
      this.$nextTick(() => {
        this.showTable = true;
      });
      if (this.listFlag) {
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
        let opt = {};
        if (this.seawallAd) {
          opt = {
            statistic: "mapList",
            itemCategory: "costalId,tideAlert",
            itemValue: this.seawallAd + "," + this.squareValue,
            adcd: this.leftSelectAdcd
              ? this.leftSelectAdcd
              : this.$localData("get", "userInfo").adcd,
          };
        } else {
          // if(this.squareIndex !== ''){
          opt = {
            statistic: "mapList",
            itemCategory: "tideAlert",
            itemValue: this.squareValue,
            adcd: this.leftSelectAdcd
              ? this.leftSelectAdcd
              : this.$localData("get", "userInfo").adcd,
          };
          // }else{
          //   opt = {
          //     statistic: 'mapList' ,
          //     adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd
          //   }
          // }
        }
        let res = await seawallLeftStatisticApi(opt);
        if (res.success) {
          let pointArr = [];
          let lineArr = [];
          if (res.data.length) {
            this.tableData = res.data;
            res.data.map((v, i) => {
              let colorType = "";
              if (v.tide > v.TIDE_ALERT_BLUE) {
                if (
                  v.tide < v.TIDE_ALERT_YELLOW ||
                  v.tide == v.TIDE_ALERT_YELLOW
                ) {
                  colorType = "blue";
                } else if (
                  v.tide < v.TIDE_ALERT_ORANGE ||
                  v.tide == v.TIDE_ALERT_ORANGE
                ) {
                  colorType = "yellow";
                } else if (
                  v.tide < v.TIDE_ALERT_RED ||
                  v.tide == v.TIDE_ALERT_RED
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
                rightType: "8",
                tm: v.tm,
                tide: v.tide,
                colorType: colorType,
                DESIGN_TIDE_STANDARD: v.DESIGN_TIDE_STANDARD,
                DESIGN_TIDE_LEVEL: v.DESIGN_TIDE_LEVEL,
                LOWEST_ELEVATION: v.LOWEST_ELEVATION,
                src: require("@/assets/images/map/seawall.png"),
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
          this.$parent.showMapPointJh(pointArr, "cwyj");
          this.$parent.showMapLines(lineArr);
        }
        this.listFlag = true;
        this.tableLoading = false;
        this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", true);
      }
    },

    async getTideAlertData() {
      this.loading_square = true;
      this.squareList = [];
      let res = await seawallLeftStatisticApi({
        statistic: "tideAlert",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
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
          // }
        );
      }
      this.loading_square = false;
    },
    resizeTable() {
      this.$nextTick(function() {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 80;
        let _this = this;
        window.onresize = function() {
          const height = this.$refs.tableContainer.clientHeight;
          this.tableHeight = height - 80;
        };
      });
    },
    resizeTable2() {
      this.$nextTick(function() {
        const height = this.$refs.tableContainer2.clientHeight;
        console.log(height, "height");
        this.cwztableHeight = height - 60;
        let _this = this;
        window.onresize = function() {
          const height = this.$refs.tableContainer2.clientHeight;
          this.cwztableHeight = height - 60;
        };
      });
    },
    async getCwzList() {
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      this.$parent.$refs.seawallMap.initView();
      this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", false);
      this.cwzfbTableData = [];
      this.cwzfbTableLoading = true;
      let res = await cwzListApi();
      let pointArr = [];
      if (res.success) {
        this.cwzfbTableData = res.rows;
        res.rows.map((v, i) => {
          let colorType = "";
          if (v.tide > v.tideAlertBlue) {
            if (v.tide < v.tideAlertYellow || v.tide == v.tideAlertYellow) {
              colorType = "blue";
            } else if (
              v.tide < v.tideAlertOrange ||
              v.tide == v.tideAlertOrange
            ) {
              colorType = "yellow";
            } else if (v.tide < v.tideAlertRed || v.tide == v.tideAlertRed) {
              colorType = "orange";
            } else {
              colorType = "red";
            }
          } else {
            colorType = "default";
          }
          this.cwzfbTableData[i].colorType = colorType;
          pointArr.push({
            lat: v.stLttd,
            lng: v.stLgtd,
            coastalName: v.coastalName,
            tm: v.tm,
            tide: v.tide,
            colorType: colorType,
            rightType: "7",
            stnm: v.stnm,
            src: require("@/assets/images/map/seawall.png"),
          });
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
    fanganManage() {
      let url =
        "https://sk.zjwater.com/mgt/coastalTideAlert/forecastList" +
        // let url = 'http://192.168.2.97:8082/mgt/coastalTideAlert/forecastList' +
        "?from=bigscreen&token=" +
        this.$localData("get", "token");
      this.$parent.openFanganManage(url);
    },
  },
};
</script>

<style lang="scss" scoped>
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
      color: #fff;
      font-weight: 500;
      font-size: 16px;
      img {
        margin-right: 5px;
      }
    }
  }
  .fangan_box {
    height: 104px;
    .fangan_title {
      padding: 8px 16px;
      @include flexbox();
      @include flexJC(space-between);
      & > p {
        font-size: 16px;
        line-height: 32px;
        color: #000000;
        font-weight: 500;
      }
    }
  }
  .fbcyj_box {
    .select_square {
      height: 120px;
      box-sizing: border-box;
      @include flexbox();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      padding: 8px 16px 16px 16px;
      .square_item {
        box-sizing: border-box;
        width: 23.5%;
        height: 100px;
        text-align: center;
        cursor: pointer;
        border-radius: 2px;
        margin-bottom: 8px;
        &.active {
          background: #284e53;
          border: 1px solid rgba(84, 249, 252, 0.8);
          box-shadow: inset 0 0 16px 0 rgba(84, 249, 252, 0.6);
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
          color: #fff;
          font-weight: bold;
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
          color: #fff;
          font-weight: bold;
        }
        &.overCnt,
        &.nearCnt,
        &.overTop,
        &.closeCnt,
        &.overDesign,
        &.sickCnt {
          width: 32%;
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
  .seawallList_wrap {
    &.hei1 {
      height: calc(100% - 40px);
    }
    &.hei2 {
      height: calc(100% - 160px);
    }
  }
  .seawallList_box {
    height: calc(100% - 310px);
  }
  .table_list {
    height: calc(100% - 42px);
    box-sizing: border-box;
    padding-bottom: 16px;
    overflow: auto;
    .el-table td div {
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
.htfbChart {
  width: 100%;
  height: 210px;
}
</style>
<style lang="scss">
.select_title {
  .el-radio {
    color: #fff;
    font-size: 16px;
    margin-right: 8px;
  }
  .el-radio__input.is-checked + .el-radio__label {
    color: #fff;
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
.fangan_select {
  padding: 0 16px 16px 16px;
  .el-select {
    width: 100%;
    // height: 32px;
  }
}
.noFangan {
  text-align: center;
}
</style>
