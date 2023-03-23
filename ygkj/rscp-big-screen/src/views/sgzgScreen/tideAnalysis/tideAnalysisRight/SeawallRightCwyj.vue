<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-19 16:47:53
 * @Description: 地图右侧-潮位预警
 * @FilePath: \rscp-big-screen\src\views\sgzgScreen\tideAnalysis\tideAnalysisRight\SeawallRightCwyj.vue
-->
<template>
  <div class='wrap'>
    <div class="select_title">
      <!-- <div>
        <img src="../../../../assets/images/chart_item_icon.png" alt="">
        数据来源
      </div> -->
      <div>
        <!-- <el-radio :disabled="!listFlag" v-model="cwFlag" label="1">实时潮位</el-radio> -->
        <el-radio :disabled="!listFlag" v-model="cwFlag" label="1">预报潮位</el-radio>
        <el-radio :disabled="!listFlag" v-model="cwFlag" label="2">潮位站分布</el-radio>
      </div>
    </div>
    
    <section class="fangan_box">
      <div class="chart_line"></div>
      <div class="fangan_title">
        <p>方案选择</p>
        <el-button plain size="small" @click="fanganManage()">方案管理</el-button>
      </div>
      <div id="fangan_select" v-if="fanganList.length">
        <el-button class="planItem" v-for="(item, index) in fanganList" :key="index" :type="index == fanganIndex ? 'primary' : ''" @click="fanganClick(index)">{{item.planName}}</el-button>
      </div>
      <div v-else class="noFangan">暂无方案</div>
    </section>
    <div class="chart_line"></div>
    <div class="seawallList_wrap hei2" v-show="cwFlag == '1'">
      <section class="fbcyj_box">
        <div class="select_title">
          <div>
            <img src="../../../../assets/images/chart_item_icon.png" alt="">
            <!-- 潮位预报预警（{{yjNum}}条） -->
            潮位预报
          </div>
          <!-- <div>
            <el-select v-model="seawallAd" :disabled="!listFlag" clearable placeholder="请选择" @change="adChange">
              <el-option v-for="item in adOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </div> -->
        </div>
        <div class="select_square" v-loading="loading_square">
          <div :class="['square_item', item.type, index === squareIndex ? 'active' : '']" v-for="(item, index) in squareList" :key="index" @click="clickSquare(index)">
            <p>{{item.name}}</p>
            <span>{{item.value}}<i>条</i></span>
            <h3>{{item.length}}<i>公里</i></h3>
          </div>
        </div>
      </section>
      <div class="chart_line"></div>
      <section class="htfb_box">
        <bar-chart v-if="showChart" v-loading="htfbLoading" class="htfbChart" :chartData="yjhtfbData" @barClick="barClickAdcd"></bar-chart>
      </section>
      <div class="chart_line"></div>
      <section class="seawallList_box">
        <div class="chart_title" style="height:25px">
          <img src="../../../../assets/images/chart_item_icon.png" alt="">
          海塘列表
          <el-button v-if="canExport" style="float:right" plain size="small" @click="exportList()">导出</el-button>
        </div>
        <div class="table_list" ref="tableContainer">
          <el-table :height="tableHeight" v-if="showTable" v-loading="tableLoading" :data="tableData" stripe style="width: 100%" @row-click="clickSeawall">
            <template slot="empty">
              <div class="table_nodata">
                <img src="../../../../assets/images/noData.png" alt="" />
              </div>
            </template>
            <el-table-column :width=" squareIndex === 0 || squareIndex === 1 ? '60px' : ''" prop="NAME" label="海塘名称">
              <template slot-scope="scope">
                <div :class="scope.row.colorType">{{scope.row.NAME ? scope.row.NAME : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column v-if="squareIndex === 2" label="塘顶最低高程" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.LOWEST_ELEVATION ? scope.row.LOWEST_ELEVATION + 'm' : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column v-if="squareIndex === 0 || squareIndex === 1" label="安全状态" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.status ? scope.row.status : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column v-if="squareIndex === '' || squareIndex === 0 || squareIndex === 1" label="设计潮位" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.DESIGN_TIDE_LEVEL ? scope.row.DESIGN_TIDE_LEVEL + 'm' : '-'}}</div>
              </template>
            </el-table-column>


            
            
            
            <!-- <el-table-column v-if="squareIndex === 6" prop="DESIGN_TIDE_LEVEL" label="临设" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.DESIGN_TIDE_LEVEL ? scope.row.DESIGN_TIDE_LEVEL : '-'}}</div>
              </template>
            </el-table-column>
            <el-table-column v-if="squareIndex === 7" prop="DESIGN_TIDE_LEVEL" label="近设" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.DESIGN_TIDE_LEVEL ? scope.row.DESIGN_TIDE_LEVEL : '-'}}</div>
              </template>
            </el-table-column> -->

            <!-- <el-table-column v-if="squareIndex === 8" label="防潮标准" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.design ? scope.row.design + 'm' : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column v-if="squareIndex === 8" label="设计标准" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.DESIGN_TIDE_STANDARD ? scope.row.DESIGN_TIDE_STANDARD + '年' : '-'}}</div>
              </template>
            </el-table-column> -->
            


            <!-- <el-table-column v-if="squareIndex === 0" prop="TIDE_ALERT_BLUE" label="蓝色" align="center">
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
            </el-table-column> -->

            <el-table-column width="60px" label="潮位" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.forecast ? scope.row.forecast + 'm' : '-'}}</div>
              </template>
            </el-table-column>
            <el-table-column :width=" squareIndex === 2 ? '120px' : ''" label="时间" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.forecast_time ? scope.row.forecast_time.slice(0, 16) : '-'}}</div>
              </template>
            </el-table-column>
          </el-table> 
        </div>
      </section>
    </div>
    <div class="cwzfblist_wrap" v-show="cwFlag == '2'">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="">
        潮位站分布
      </div>
      <div class="table_list" ref="tableContainer2">
        <el-table :height="cwztableHeight" v-loading="cwzfbTableLoading" :data="cwzfbTableData" stripe style="width: 100%" @row-click="clickCwzfb" :span-method="objectSpanMethod">
          <template slot="empty">
            <div class="table_nodata">
              <img src="../../../../assets/images/noData.png" alt="" />
            </div>
          </template>
          <el-table-column label="潮位代表站" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.stnm ? scope.row.stnm : '-'}}</div>
            </template>
          </el-table-column>
          <el-table-column label="预报潮位" align="center">
            <el-table-column label="时间" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.forecastTime ? scope.row.forecastTime : '-'}}</div>
              </template>
            </el-table-column>
            <el-table-column label="潮位/m" align="center">
              <template slot-scope="scope">
                <div :class="['bold', scope.row.colorType]">{{scope.row.forecast ? scope.row.forecast : '-'}}</div>
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column prop="coastalName" label="关联岸段" align="center"></el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import { seawallLeftStatisticApi, adListApi, cwzListApi, getPlanApi } from '@/api/api_seawall'
import BarChart from '@/components/BarChart'
export default {
  name: '',
  props: {
    rightSelectList1: {
      type: Array,
      required: true
    }
  },
  components: {
    BarChart
  },
  data () {
    return {
      cwFlag: '1',
      seawallList: [],
      seawallAd: '',
      adOptions: [],
      yjhtfbData: {
        title: "预警分布",
        // yInterval: 2,
        id: 'yjhtfbChart',
        xData: [],
        yData: [],
        adcdList: [],
        unit: '条'
      },
      htfbLoading: true,
      tableData: [],
      tableLoading: true,
      squareList: [],
      squareIndex: 0,
      squareValue: 'overCnt',
      loading_square: true,
      seawallFangan: '',
      fanganOptions: [{
        label: '暂无方案',
        value: ''
      }],
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
      planId: '',
      showTable: true,
      canExport: false
    };
  },

  computed: {
    leftSelectAdcd(){
      return this.$store.state.leftSelectAdcd
    },
    leftSelectProjectScale(){
      return this.$store.state.leftSelectProjectScale
    },
  },
  watch: {
    leftSelectAdcd(val){
      this.getYbcwData()
    },
    // leftSelectProjectScale(val){
    //   this.setOptions('scale', val)
    //   this.getSeawallList()
    // }
    cwFlag(val){
      this.squareIndex = 0
      this.squareValue = 'overCnt'
      // this.fanganIndex = 0
      // this.planId = this.fanganList[0].id
      if(val == '1'){
        this.resizeTable()
        this.getYbcwData()
      }else{
        this.resizeTable2()
        this.getCwzList()
      }
    }
  },
 
  mounted() {
    // this.resizeTable2()
    // this.getCwzList()
    this.resizeTable()
    this.getPlanList()
  },
  
  methods: {
    getYbcwData(){
      this.updateValue()
      this.getTideAlertData('forecast')
      this.getSeawallList()
      this.getLengthData()
    },
    // 预警海塘分布
    async getLengthData(){
      this.htfbLoading = true
      this.yjhtfbData = {
        title: "预警分布",
        // yInterval: 2,
        id: 'yjhtfbChart',
        xData: [],
        yData: [],
        adcdList: [],
        unit: '条'
      }
      let opt = {}
      if(this.planId != ''){
        opt = {
          statistic: 'adcd',
          adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd,
          itemCategory: 'forecast,planId',
          itemValue: this.squareValue + ',' + this.planId
        }
      }else{
        opt = {
          statistic: 'adcd',
          adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd,
          itemCategory: 'forecast',
          itemValue: this.squareValue
        }
      }
      let res = await seawallLeftStatisticApi(opt)
      if(res.success){
        if(res.data.length){
          for (let i = 0; i < res.data.length; i++) {
            if(res.data[i].adnm.length > 2){
              this.yjhtfbData.xData.push(res.data[i].adnm.substring(0,res.data[i].adnm.length - 1))
            }else{
              this.yjhtfbData.xData.push(res.data[i].adnm)
            }
            this.yjhtfbData.yData.push(res.data[i].num)
            this.yjhtfbData.adcdList.push(res.data[i].adcd)
          }
          // this.showChart = false
          // this.$nextTick(()=>{
          //   this.showChart = true
          // })
        }else{
          this.showChart = false
          this.$nextTick(()=>{
            this.showChart = true
          })
        }
      } else {
        this.$message.error(res.msg ? res.msg : '请求失败')
      }
      
      
      this.htfbLoading = false
    },
    // adChange(val){
    //   this.getSeawallList()
    // },
    clickSeawall(row){
      this.$parent.setView(row.LGTD, row.LTTD);
      row.lat = row.LTTD
      row.lng = row.LGTD
      row.rightType = '3'
      this.$parent.showOneSelectPoint([row], false, 'cwyj');
      setTimeout(()=>{
        this.$parent.setPopup(row)
      },500)
    },
    clickCwzfb(row){
      // this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.setView(row.stLgtd, row.stLttd);
      row.lat = row.stLttd
      row.lng = row.stLgtd
      row.LGTD = row.stLgtd
      row.LTTD = row.stLttd
      row.rightType = '6'
      this.$parent.showOneSelectPoint([row], true);
      setTimeout(()=>{
        this.$parent.setPopup(row)
      },500)
    },
    clickSquare(index){
      if(this.listFlag){
        this.showTable = false
        this.$nextTick(()=>{
          this.showTable = true
        })
        // if(this.squareIndex === index){
        //   this.squareIndex = ''
        // }else{
          this.squareIndex = index
        // }
        this.squareValue = this.squareList[index].type
        this.getSeawallList()
        this.getLengthData()
      }
    },

    // 海塘分布柱状图点击事件
    barClickAdcd(index){
      if(this.yjhtfbData.xData[index] == '省级'){
        this.$store.commit('SET_LEFT_SELECT_ADCD', '33')
      }else{
        this.$store.commit('SET_LEFT_SELECT_ADCD', this.yjhtfbData.adcdList[index])
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
    async getSeawallList(){
      if(this.listFlag){
        this.$parent.$refs.seawallMap.removeAllLayer();
        this.$parent.$refs.seawallMap.closePopup();
        this.$parent.$refs.seawallMap.initView();
        this.$store.commit('SET_SEAWALL_RIGTH_SELECT_FLAG', false)
        this.listFlag = false
        this.tableData = []
        this.tableLoading = true
        this.canExport = false
        let opt = {}
        if(this.seawallAd){
          opt = {
            statistic: 'mapList',
            itemCategory: 'costalId,forecast',
            itemValue: this.seawallAd + ',' + this.squareValue,
            adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd
          }
        }else{
          if(this.planId != ''){
            opt = {
              statistic: 'mapList',
              itemCategory: 'forecast,planId',
              itemValue: this.squareValue + ',' + this.planId,
              adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd
            }
          }else{
            opt = { 
              statistic: 'mapList',
              itemCategory: 'forecast',
              itemValue: this.squareValue,
              adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd
            }
          }
        }
        let res = await seawallLeftStatisticApi(opt)
        if(res.success){
          let pointArr = []
          let lineArr = []
          if(res.data.length){
            this.canExport = true
            this.tableData = res.data
            res.data.map((v,i) => {
              let colorType = ''
              if(v.forecast > v.TIDE_ALERT_BLUE){
                if(v.forecast < v.TIDE_ALERT_YELLOW || v.forecast == v.TIDE_ALERT_YELLOW){
                  colorType = 'blue'
                }else if(v.forecast < v.TIDE_ALERT_ORANGE || v.forecast == v.TIDE_ALERT_ORANGE){
                  colorType = 'yellow'
                }else if(v.forecast < v.TIDE_ALERT_RED || v.forecast == v.TIDE_ALERT_RED){
                  colorType = 'orange'
                }else{
                  colorType = 'red'
                }
              }else{
                colorType = 'default'
              }
              this.tableData[i].colorType = colorType
              pointArr.push({
                lat: v.LTTD,
                lng: v.LGTD,
                NAME: v.NAME,
                PRCD: v.PRCD,
                rightType: '3',
                forecast_time: v.forecast_time,
                colorType: colorType,
                forecast: v.forecast,
                status: v.status,
                DESIGN_TIDE_STANDARD: v.DESIGN_TIDE_STANDARD,
                DESIGN_TIDE_LEVEL: v.DESIGN_TIDE_LEVEL,
                LOWEST_ELEVATION: v.LOWEST_ELEVATION,
                src: require("@/assets/images/map/seawall.png")
              })
              lineArr.push({
                line: v.COORPOT ? JSON.parse(v.COORPOT.replace(new RegExp('&quot;',"gm"),'"'))[0] : '',
                level: v.project_scale
              })
              v.lineArr = []
              v.lineArr.push({
                line: v.COORPOT ? JSON.parse(v.COORPOT.replace(new RegExp('&quot;',"gm"),'"'))[0] : '',
                level: v.project_scale
              })
            });
          }
          // this.$parent.showMapPointJh(pointArr, 'cwyj')
          this.$parent.showMapPoints(pointArr,'cwyj')
          this.$parent.showMapLines(lineArr)
        }
        this.listFlag = true
        this.tableLoading = false
        this.$store.commit('SET_SEAWALL_RIGTH_SELECT_FLAG', true)
      }
      
    },
    
    async getTideAlertData(type){
      this.loading_square = true
      this.squareList = []
      let res = await seawallLeftStatisticApi({
        statistic: type,
        adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd,
        itemCategory: 'planId',
        itemValue: this.planId
      })
      if(res.success){
        this.yjNum = res.data[0]['total']
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
            type: 'overCnt',
            name: '超设计潮位',
            value: res.data[0]['overCnt'],
            length: res.data[0]['overLength'] ? (res.data[0]['overLength'] / 1000).toFixed(1) : 0
          },
          {
            type: 'sickCnt',
            name: '超病险塘能力',
            value: res.data[0]['sickCnt'],
            length: res.data[0]['sickLength'] ? (res.data[0]['sickLength'] / 1000).toFixed(1) : 0
          },
          {
            type: 'overTop',
            name: '超塘顶高程',
            value: res.data[0]['overTop'],
            length: res.data[0]['overTopLength'] ? (res.data[0]['overTopLength'] / 1000).toFixed(1) : 0
          },
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
        )
      }
      this.loading_square = false
    },
    resizeTable(){
      this.$nextTick(function () {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 80
        let _this = this;
        // window.onresize = function() {
        //   const height = this.$refs.tableContainer.clientHeight;
        //   this.tableHeight = height - 80
        // }
      })
    },
    resizeTable2(){
      this.$nextTick(function () {
        const height = this.$refs.tableContainer2.clientHeight;
        console.log(height,'height');
        this.cwztableHeight = height - 60
        let _this = this;
        // window.onresize = function() {
        //   const height = this.$refs.tableContainer2.clientHeight;
        //   this.cwztableHeight = height - 60
        // }
      })
    },
    async getCwzList(){
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      this.$parent.$refs.seawallMap.initView();
      this.$store.commit('SET_SEAWALL_RIGTH_SELECT_FLAG', false)
      this.cwzfbTableData = []
      this.cwzfbTableLoading = true
      let res = await cwzListApi({
        planId: this.planId
      })
      let pointArr = []
      if(res.success){
        // this.cwzfbTableData = res.rows
        res.rows.map((v,i) => {
          if(v.forecast){
            let colorType = ''
            if(v.forecast > v.tideAlertBlue){
              if(v.forecast < v.tideAlertYellow || v.forecast == v.tideAlertYellow){
                colorType = 'blue'
              }else if(v.forecast < v.tideAlertOrange || v.forecast == v.tideAlertOrange){
                colorType = 'yellow'
              }else if(v.forecast < v.tideAlertRed || v.forecast == v.tideAlertRed){
                colorType = 'orange'
              }else{
                colorType = 'red'
              }
            }else{
              colorType = 'default'
            }
            v.colorType = colorType
            this.cwzfbTableData.push(v)
            pointArr.push({
              lat: v.stLttd,
              lng: v.stLgtd,
              coastalName: v.coastalName,
              forecastTime: v.forecastTime,
              forecast: v.forecast,
              colorType: colorType,
              rightType: '6',
              stnm: v.stnm,
              src: require("@/assets/images/map/seawall.png")
            })
          }
        });
        this.$parent.showMapPoints(pointArr)
        // this.$parent.showOverlays(pointArr)
        this.spanArr = []
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
      this.cwzfbTableLoading = false
      this.$store.commit('SET_SEAWALL_RIGTH_SELECT_FLAG', true)
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) {
        const _row = this.spanArr[rowIndex]
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col
        }
      }
    },
    async getPlanList(){
      let res = await getPlanApi({
        isMatch:1
        // adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd,
      })
      if(res.success){
        if(res.rows.length){
          this.fanganList = res.rows.slice(0, 4)
          this.planId = this.fanganList[0].id
        }
        this.getYbcwData()
      }
      // console.log(fanganList);
    },
    fanganClick(index){
      this.squareIndex = 0
      this.squareValue = 'overCnt'
      this.fanganIndex = index
      this.planId = this.fanganList[index].id
      if(this.cwFlag == '1'){
        this.getYbcwData()
      }else{
        this.getCwzList()
      }
    },
    fanganManage(){
      let url = 'https://sk.zjwater.com/mgt/coastalTideAlert/forecastList' +
      // let url = 'http://192.168.2.97:8082/mgt/coastalTideAlert/forecastList' +
        '?from=bigscreen&token=' + this.$localData('get', 'token')
      this.$parent.openFanganManage(url)
    },
    exportList(){
      let adcd = this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd
      let token = this.$localData('get', 'token')
      window.open(
        this.$config.host + 
        `/mgt/bm/seawallei/toExcel` +
        `?adcd=${adcd}` +
        `&itemCategory=forecast,planId` +
        `&itemValue=${this.squareValue},${this.planId}` + 
        `&token=${token}`
      )
    },
    async updateValue(){
      let adcd = this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd
      let superviseLevel = ''
      if(adcd == '33'){
        superviseLevel = 1
      }
      let itemCategory = 'planId'
      let itemValue = this.planId
      if(this.leftSelectProjectScale){
        itemCategory = 'planId,scale'
        itemValue = this.planId + ',' + this.leftSelectProjectScale
      }
      let res = await seawallLeftStatisticApi({
        adcd: adcd,
        statistic: 'mapTabs',
        superviseLevel: superviseLevel,
        itemCategory: itemCategory,
        itemValue: itemValue
      })
      if(res.success){
        this.rightSelectList1[1].num = res.data[0].forecast
        this.$emit('update.rightSelectList1',rightSelectList1)
      }
    }
  }
}
</script>

<style lang='scss' scoped>
  .wrap{
    height: 100%;
    .select_title{
      padding: 16px 16px 10px 16px;
      @include flexbox();
      @include flexAC();
      @include flexJC(space-between);
      &>div{
        @include flexbox();
        @include flexAC();
        color: #000;
        font-weight: 500;
        font-size: 16px;
        img{
          margin-right: 5px;
        }
      }
    }
    .fangan_box{
      height: 150px;
      .fangan_title{
        padding: 8px 16px;
        @include flexbox();
        @include flexJC(space-between);
        &>p{
          font-size: 16px;
          line-height: 32px;
          color: #000000;
          font-weight: 500;
        }
      }
    }
    .fbcyj_box{
      .select_square{
        height: 120px;
        box-sizing: border-box;
        @include flexbox();
        @include flexJC(space-between);
        @include flexflow(row wrap);
        padding: 8px 16px 16px 16px;
        .square_item{
          box-sizing: border-box;
          width: 23.5%;
          height: 100px;
          text-align: center;
          cursor: pointer;
          border-radius: 2px;
          margin-bottom: 8px;
          &.active{
            background: #193CC5!important;
            border: 1px solid #193CC5!important;
            &>p{
              color: #fff!important;
            }
            &>span{
              color: #fff!important;
            }
            &>h3{
              color: #fff!important;
            }
          }
          &>p{
            line-height: 24px;
            font-size: 14px;
            color: #000;
            padding: 8px 0 4px 0;
          }
          i{
            font-size: 12px;
            padding-left: 4px;
          }
          &>span, &>h3{
            line-height: 24px;
            font-size: 20px;
            color: #000;
            font-weight: 500;
          }
          &.overCnt, &.nearCnt, &.overTop, &.closeCnt, &.overDesign, &.sickCnt{
            width: 32%;
            background: #F8F8F8;
            border: 1px solid #D6D6D6;
          }
          &.blue{
            background: #eef5fe;
            border: 1px solid #2478F0;
          }
          &.yellow{
            background: #fffbf0;
            border: 1px solid #F7C739;
          }
          &.orange{
            background: #fef5f3;
            border: 1px solid #EB7E65;
          }
          &.red{
            background: #fdeeee;
            border: 1px solid #E02020;
          }
        }
      }
    }
    .seawallList_wrap{
      &.hei2{
        height: calc(100% - 160px);
      }
    }
    .seawallList_box{
      height: calc(100% - 356px);
    }
    .cwzfblist_wrap{
      height: calc(100% - 105px);
    }
    .table_list{
      height: calc(100% - 42px);
      box-sizing: border-box;
      padding-bottom: 16px;
      overflow: auto;
      .el-table td div{
        & > .bold{
          font-weight: bold;
        }
        & >.blue{
          color: #2478F0;
        }
        & >.yellow{
          color: #F7C739;
        }
        & >.orange{
          color: #EB7E65;
        }
        & >.red{
          color: #E02020;
        }
      }
    }
  }
  .htfbChart{
    width: 100%;
    height: 210px;
  }
</style>
<style lang="scss">
  .select_title{
    .el-radio{
      color: #000;
      font-size: 16px;
      margin-right: 8px;
    }
    .el-radio__input.is-checked+.el-radio__label{
      color: #000;
    }
    .el-radio__label{
      padding-left: 4px;
    }
    .el-radio__input.is-checked .el-radio__inner{
      border-color: #1341c5;
      background: #1341c5;
    }
    .el-select .el-input__inner{
      height: 32px;
    }
    .el-input__icon{
      line-height: unset;
    }
  }
  #fangan_select .el-button+.el-button{
    margin: 0;
  }
  #fangan_select{
    padding: 0 16px 12px 16px;
    @include flexbox();
    @include flexJC(space-between);
    @include flexflow(row wrap);
    // overflow-x: auto;
    // overflow-y: hidden;
    .el-select{
      width: 100%;
      // height: 32px;
    }
    .el-button{
      width: 49%;
      // margin-right: 5px!important;
      margin-bottom: 10px!important;
      padding-left: 4px;
      padding-right: 4px;
    }
  }
  #fangan_select::-webkit-scrollbar{
    // display: none;
  }
  #fangan_select::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 10px;
    background-color: rgb(168, 168, 168);
  }
    
  // #fangan_select::-webkit-scrollbar-track {
  //   /*滚动条里面轨道*/
  //   // -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  //   background: #fff;
  //   // background-color: #fff;
  //   // display: none

  // }
  .noFangan{
    text-align: center;
  }
</style>