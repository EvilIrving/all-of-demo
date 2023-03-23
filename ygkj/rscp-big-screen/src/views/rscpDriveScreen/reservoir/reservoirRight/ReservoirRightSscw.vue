<!--
 * @Author: hanyu
 * @LastEditTime: 2021-08-17 20:34:52
 * @Description: 地图右侧-水位预警
 * @FilePath: /rscp-big-screen/src/views/bigScreen/reservoir/reservoirRight/ReservoirRightSscw.vue
-->
<template>
  <div class='wrap'>
    <div class="chart_line"></div>
    <div class="reservoirList_wrap hei1">
      <section class="fbcyj_box">
        <div class="select_title">
          <div>
            <img src="../../../../assets/images/chart_item_icon.png" alt="">
            实时水位
          </div>
        </div>
        <div class="select_square" v-loading="loading_square">
          <div :class="['square_item', item.type, index === squareIndex ? 'active' : '']" v-for="(item, index) in squareList" :key="index" @click="clickSquare(index)">
            <p>{{item.name}}</p>
            <span>{{item.value ? item.value : 0}}<i>座</i></span>
          </div>
        </div>
      </section>
      <section class="fbcyj_box">
        <div class="select_title">
          <div>
            <img src="../../../../assets/images/chart_item_icon.png" alt="">
            病险水库
          </div>
        </div>
        <div class="select_square" v-loading="loading_bxsk_square">
          <div :class="['square_item', 'square2', item.type, index === bxskSquareIndex ? 'active' : '']" v-for="(item, index) in bxskSquareList" :key="index" @click="clickBxskSquare(index)">
            <p>{{item.name}}</p>
            <span>{{item.value ? item.value : 0}}<i>座</i></span>
          </div>
        </div>
      </section>
      <section class="htfb_box">
        <bar-chart v-if="showChart" v-loading="htfbLoading" class="htfbChart" :chartData="yjhtfbData" @barClick="barClickAdcd"></bar-chart>
      </section>
      <div class="chart_line"></div>
      <section class="reservoirList_box">
        <div class="chart_title" style="height:25px">
          <img src="../../../../assets/images/chart_item_icon.png" alt="">
          水库列表
          <el-button v-if="canExport" style="float:right" plain size="small" @click="exportList()">导出</el-button>
        </div>
        <div class="table_list" ref="tableContainer">
          <el-table :height="tableHeight" v-if="showTable" v-loading="tableLoading" :data="tableData" stripe style="width: 100%" @row-click="clickReservoir">
            <template slot="empty">
              <div class="table_nodata">
                <img src="../../../../assets/images/noData.png" alt="" />
              </div>
            </template>
            <el-table-column width="90px" label="水库名称">
              <template slot-scope="scope">
                <div>{{scope.row.NAME ? scope.row.NAME : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column v-if="squareIndex === 0" width="70px" label="汛限水位" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.limitWaterLevel ? scope.row.limitWaterLevel.toFixed(2) + 'm' : '-'}}</div>
              </template>
            </el-table-column>
            
            <el-table-column v-if="squareIndex === 1" width="70px" label="正常水位" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.NWL ? scope.row.NWL.toFixed(2) + 'm' : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column v-if="squareIndex === 2" width="70px" label="设计水位" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.DSFLZ ? scope.row.DSFLZ.toFixed(2) + 'm' : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column v-if="squareIndex === 3" width="70px" label="校核水位" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.CKFLV ? scope.row.CKFLV.toFixed(2) + 'm' : '-'}}</div>
              </template>
            </el-table-column>


            <el-table-column width="70px" label="实时水位" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.waterLevel ? scope.row.waterLevel.toFixed(2) + 'm' : '-'}}</div>
              </template>
            </el-table-column>

            <el-table-column label="时间" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.waterleveltm ? scope.row.waterleveltm.slice(0, 16) : '-'}}</div>
              </template>
            </el-table-column>
          </el-table> 
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import { reservoirLeftStatisticApi } from '@/api/api_reservoir'
import BarChart from '@/components/BarChart'
import { drawPoint } from '@/utils/mapUtil'

export default {
  name: '',
  props: {
    
  },
  components: {
    BarChart
  },
  data () {
    return {
      reservoirList: [],
      reservoirAd: '',
      adOptions: [],
      yjhtfbData: {
        title: "预警分布",
        yInterval: 1,
        id: 'bxskfbChart',
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
      squareValue: 'limitWaterLevel',
      bxskSquareList: [],
      bxskSquareIndex: '',
      bxskSquareValue: '',
      loading_bxsk_square: true,
      loading_square: true,
      reservoirFangan: '',
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
      fanganList: [],
      fanganIndex: 0,
      showTable: true,
      canExport: false,
      superviseLevel: '',
      adcd: '',
      scale: 'all'
    };
  },

  computed: {
    leftSelectAdcd(){
      return this.$store.state.leftSelectAdcd
    },
    leftSelectProjectScale(){
      return this.$store.state.leftSelectProjectScale
    }
  },
  watch: {
    leftSelectAdcd(val){
      if(val == '33'){
        this.superviseLevel = 1
      }else{
        this.superviseLevel = ''
      }
      this.adcd = val
      this.getTideAlertData()
      this.getSquareData()
      this.getLengthData()
      this.getReservoirList()
    },
    leftSelectProjectScale(val){
      this.scale = val
      this.getTideAlertData()
      this.getSquareData()
      this.getLengthData()
      this.getReservoirList()
    }
  },
 
  mounted() {
    this.resizeTable()
    if(this.leftSelectAdcd == '33'){
      this.superviseLevel = 1
    }
    if(this.leftSelectAdcd){
      this.adcd = this.leftSelectAdcd
    }
    if(this.leftSelectProjectScale){
      this.scale = this.leftSelectProjectScale
    }
    this.getSscwData()
  },
  
  methods: {
    getSscwData(){
      this.getTideAlertData()
      this.getSquareData()
      this.getLengthData()
      this.getReservoirList()
    },
    // 预警水库分布
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
    // 预警水库分布
    async getLengthData(){
      this.htfbLoading = true
      this.yjhtfbData = {
        title: "预警分布",
        // yInterval: 2,
        id: 'bxskfbChart',
        xData: [],
        yData: [],
        adcdList: [],
        unit: '条'
      }
      let res = await reservoirLeftStatisticApi({
        statistic: 'adcd',
        adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd,
        itemCategory: 'scale,' + this.squareValue,
        itemValue: this.scale + ',' + this.squareValue,
        superviseLevel: this.superviseLevel
      })
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

  
    clickReservoir(row){
      this.$parent.setView(row.LGTD, row.LTTD);
      row.lat = row.LTTD
      row.lng = row.LGTD
      row.rightType = '8'
      // this.$parent.showOneSelectPoint([row]);
      setTimeout(()=>{
        this.$parent.setPopup(row)
      },500)
    },
  
    clickSquare(index){
      this.showTable = false
      this.$nextTick(()=>{
        this.showTable = true
      })
      if(this.listFlag){
        // if(this.squareIndex === index){
        //   this.squareIndex = ''
        // }else{
          this.squareIndex = index
        // }
        this.squareValue = this.squareList[index].type
        this.getReservoirList()
        this.getLengthData()
        this.getSquareData()
      }
    },
    clickBxskSquare(index){
      this.showTable = false
      this.$nextTick(()=>{
        this.showTable = true
      })
      if(this.listFlag){
        if(this.bxskSquareIndex === index){
          this.bxskSquareIndex = ''
          this.bxskSquareValue = ''
        }else{
          this.bxskSquareIndex = index
          this.bxskSquareValue = this.bxskSquareList[index].name
        }
        this.getReservoirList()
        this.getLengthData()
      }
    },

    // 水库分布柱状图点击事件
    barClickAdcd(index){
      if(this.yjhtfbData.xData[index] == '省级'){
        this.$store.commit('SET_LEFT_SELECT_ADCD', '33')
      }else{
        this.$store.commit('SET_LEFT_SELECT_ADCD', this.yjhtfbData.adcdList[index])
      }
    },
    
    
    async getTideAlertData(){
      this.loading_square = true
      this.squareList = []
      let res = await reservoirLeftStatisticApi({
        statistic: 'overCnt',
        itemCategory: 'scale',
        itemValue: this.scale,
        adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd,
        superviseLevel: this.superviseLevel
      })
      if(res.success){
        this.squareList.push(
          {
            type: 'limitWaterLevel',
            name: '超汛限水位',
            value: res.data[0]['limitWaterLevel'],
          },
          {
            type: 'NWL',
            name: '超正常水位',
            value: res.data[0]['NWL'],
          },
          {
            type: 'DSFLZ',
            name: '超设计水位',
            value: res.data[0]['DSFLZ'],
          },
          // {
          //   type: 'CKFLV',
          //   name: '超校核水位',
          //   value: res.data[0]['CKFLV'],
          // }
        )
      }
      this.loading_square = false
    },

    async getSquareData(){
      this.loading_bxsk_square = true
      this.bxskSquareList = []
      let res = await reservoirLeftStatisticApi({
        statistic: 'problemCnt',
        itemCategory: 'scale,' + this.squareValue,
        itemValue: this.scale + ',1',
        adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd,
        superviseLevel: this.superviseLevel
      })
      if(res.success){
        this.bxskSquareList.push(
          {
            type: 'processing',
            name: '加固中',
            value: res.data[0]['processing'],
          },
          {
            type: 'thridClass',
            name: '三类坝',
            value: res.data[0]['thridClass'],
          },
          // {
          //   type: 'secondClass',
          //   name: '二类坝',
          //   value: res.data[0]['secondClass'],
          // }
        )
        this.getReservoirList()
      }
      this.loading_bxsk_square = false
    },

    async getReservoirList(){
      if(this.listFlag){
        this.$parent.$refs.reservoirMap.removeAllLayer();
        this.$parent.$refs.reservoirMap.closePopup();
        this.$parent.$refs.reservoirMap.initView();
        this.$store.commit('SET_RESERVOIR_RIGTH_SELECT_FLAG', false)
        this.listFlag = false
        this.tableData = []
        this.tableLoading = true
        this.canExport = false
        let opt = {
          statistic: 'mapList',
          itemCategory: this.squareValue + (this.bxskSquareValue ? ',status' : '') + ',scale',
          // itemCategory: 'overCnt',
          itemValue: ',' + (this.bxskSquareValue ? '1,' : '') + this.scale,
          adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd,
          superviseLevel: this.superviseLevel
        }
        let res = await reservoirLeftStatisticApi(opt)
        if(res.success){
          let pointArr = []
          let colorType = 'blue'
          if(this.squareIndex === 0){
              colorType = 'blue'
            }else if(this.squareIndex === 1){
              colorType = 'yellow'
            }else if(this.squareIndex === 2){
              colorType = 'orange'
            }
          if(res.data.length){
            res.data.map((v,i) => {
              v.lat = v.LTTD
              v.lng = v.LGTD
              v.rightType = '8'
              pointArr.push(v)
              v.colorType = colorType
            });
            this.tableData = res.data
            this.canExport = true
          }
  
          this.$parent.showMapPoints(pointArr)
        }
        this.listFlag = true
        this.tableLoading = false
        this.$store.commit('SET_RESERVOIR_RIGTH_SELECT_FLAG', true)
      }
      
    },


    resizeTable(){
      this.$nextTick(function () {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 80
        let _this = this;
        window.onresize = function() {
          const height = this.$refs.tableContainer.clientHeight;
          this.tableHeight = height - 80
        }
      })
    },

    exportList(){
      let adcd = this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd
      let token = this.$localData('get', 'token')
      window.open(
        this.$config.host + 
        `/mgt/bm/reservoirei/toExcel` +
        `?adcd=${adcd}` +
        `&statistic=mapList` + 
        `&itemCategory=${this.squareValue}` +
        `&token=${token}`
      )
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
      height: 104px;
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
        height: 80px;
        box-sizing: border-box;
        @include flexbox();
        @include flexJC(space-between);
        @include flexflow(row wrap);
        padding: 8px 16px 16px 16px;
        
        .square_item{
          box-sizing: border-box;
          width: 32%;
          height: 70px;
          text-align: center;
          // width: 32%;
          background: #F8F8F8;
            border: 1px solid #D6D6D6;
          cursor: pointer;
          border-radius: 2px;
          margin-bottom: 8px;
          &.square2{
            width: 48%;
          }
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
    .reservoirList_wrap{
      &.hei1{
        height: calc(100% - 40px);
      }
      &.hei2{
        height: calc(100% - 160px);
      }
    }
    .reservoirList_box{
      height: calc(100% - 350px);
    }
    .table_list{
      height: calc(100% - 42px);
      box-sizing: border-box;
      padding-bottom: 16px;
      overflow: auto;
      .el-table td div{
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
  .fangan_select{
    padding: 0 16px 16px 16px;
    .el-select{
      width: 100%;
      // height: 32px;
    }
  }
  .noFangan{
    text-align: center;
  }
</style>