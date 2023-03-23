<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-09 16:51:31
 * @Description: 海塘工程名录
 * @FilePath: \lucheng_project\src\views\seawallPrevent\projectList\ProjectList.vue
-->
<template>
  <div class='wrap'>
    <!-- <div class="wrap_top" v-if="userType != '3'">
      <div class="top_item_box">
        <div class="item_box_title">
          <p>海塘预警</p>
        </div>
        <bar-chart class="chart_wrap" :chartData="chartData1"></bar-chart>
      </div>
      <div class="top_item_box">
        <div class="item_box_title">
          <p>海塘报警</p>
        </div>
        <bar-chart class="chart_wrap" :chartData="chartData2"></bar-chart>
      </div>
      <div class="top_item_box">
        <div class="item_box_title">
          <p>安全鉴定</p>
        </div>
        <pie-chart class="chart_wrap" :chartData="chartData3"></pie-chart>
      </div>
      <div class="top_item_box">
        <div class="item_box_title">
          <p>海塘等级</p>
        </div>
        <pie-chart class="chart_wrap" :chartData="chartData4"></pie-chart>
      </div>
    </div> -->
    <div class="wrap_bottom">
      <div class="list_search_box">
        <div class="search_left">
          <span style="font-size:14px;">工程名称</span>
          <el-input
            placeholder="请输入"
            @change="getList"
            v-model="seawallName"
            clearable
            style="width: 180px; margin: 0 20px 0 15px"
          />
          <span style="font-size:14px;">工程等级</span>
          <el-select v-model="grade" placeholder="请选择" @change="getList" clearable style="width: 180px; margin: 0 20px 0 15px">
            <el-option
                v-for="item in gradeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
          <span style="font-size:14px;">安全鉴定</span>
          <el-select v-model="safetyConclusion" placeholder="请选择" @change="getList" clearable style="width: 180px; margin: 0 20px 0 15px">
            <el-option
                v-for="item in safetyConclusionOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
          
        </div>
        <div class="search_right">
          <div :class="['change_model_btn', listModel === 1 ? 'active' : '']" @click="changeListModel(1)">
            <img v-show="listModel === 1" src="../../../assets/images/listmodel1.png" alt="">
            <img v-show="listModel !== 1" src="../../../assets/images/listmodel0.png" alt="">
            <span>清单模式</span>
          </div>
          <div :class="['change_model_btn', listModel === 2 ? 'active' : '']" @click="changeListModel(2)">
            <img v-show="listModel === 2" src="../../../assets/images/mapmodel1.png" alt="">
            <img v-show="listModel !== 2" src="../../../assets/images/mapmodel0.png" alt="">
            <span>地图模式</span>
          </div>
        </div>
      </div>
      <div class="list_model" v-if="listModel === 1">
        <el-table :data="projectList" border style="width: 100%" @row-click="goProjectDetail">
          <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
          <el-table-column prop="seawallName" label="工程名称" align="center">
            <template slot-scope="scope">
              <div style="color: #1492FF; cursor:pointer">{{scope.row.seawallName}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="areaName" label="行政区划" align="center"></el-table-column>
          <el-table-column label="海塘等级" align="center">
            <template slot-scope="scope">
             <div>
               <span v-if="scope.row.seawallLevel === '1'">1级海塘</span>
               <span v-if="scope.row.seawallLevel === '2'">2级海塘</span>
               <span v-if="scope.row.seawallLevel === '3'">3级海塘</span>
               <span v-if="scope.row.seawallLevel === '4'">4级海塘</span>
               <span v-if="scope.row.seawallLevel === '5'">5级海塘</span>
               <span v-if="scope.row.seawallLevel === '6'">规模以下</span>
             </div>
            </template>
          </el-table-column>
          <el-table-column prop="seawallLength" label="海塘长度（米）" align="center"></el-table-column>
          <el-table-column prop="designTideStandard" label="设计防潮标准（年）" align="center"></el-table-column>
          <el-table-column prop="safetyConclusion" label="安全鉴定" align="center"></el-table-column>
        </el-table>
      </div>
      <div class="map_model" v-if="listModel === 2">
        <map-model ref="mapModelDom"></map-model>
        <div class="map_model_list" ref="tableContainer" :class="{ showList: showList }">
          <img v-show="!showList" src="../../../assets/images/map_left_open.png" alt="" @click="clickShowList(true)">
          <img v-show="showList" src="../../../assets/images/map_left_close.png" alt="" @click="clickShowList(false)">
          <el-table :data="projectList" border style="width: 100%" @row-click="goProjectDetail" :height="tableHeight">
            <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
            <el-table-column prop="seawallName" label="工程名称" align="center">
              <template slot-scope="scope">
                <div style="color: #1492FF; cursor:pointer">{{scope.row.seawallName}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="areaName" label="行政区划" align="center"></el-table-column>
             <el-table-column label="海塘等级" align="center">
            <template slot-scope="scope">
             <div>
               <span v-if="scope.row.seawallLevel === '1'">1级海塘</span>
               <span v-if="scope.row.seawallLevel === '2'">2级海塘</span>
               <span v-if="scope.row.seawallLevel === '3'">3级海塘</span>
               <span v-if="scope.row.seawallLevel === '4'">4级海塘</span>
               <span v-if="scope.row.seawallLevel === '5'">5级海塘</span>
               <span v-if="scope.row.seawallLevel === '6'">规模以下</span>
             </div>
            </template>
          </el-table-column>
          </el-table>
        </div>
        
      </div>
    </div>
  </div>
</template>

<script>
import { getOutsideToken } from '@/api/index'
import { seawallListApi } from '@/api/seawallPrevent'
import BarChart from '@/components/BarChart'
import PieChart from '@/components/PieChart'
import MapModel from './components/MapModel'
export default {
  name: 'ProjectList',
  props: {
    
  },
  components: {
    BarChart,
    PieChart,
    MapModel
  },
  data () {
    return {
      projectList: [],
      chartData1: {
        id: 'barchart1',
        unit: '座',
        xData: ['正常', '超蓝色', '超黄色', '超橙色', '超红色'],
        yData: []
      },
      chartData2: {
        id: 'barchart2',
        unit: '座',
        xData: ['正常', '超蓝色', '超黄色', '超橙色', '超红色'],
        yData: []
      },
      chartData3: {
        id: 'piechart1',
        unit: '座',
        legend: {
          orient: 'vertical',
          left: '60%',
          height: '70%'
        },
        color: ['#F6D200', '#50C95F', '#4FA0FF', '#6236FF', '#B520E0'],
        series: [],
      },
      chartData4: {
        id: 'piechart2',
        unit: '座',
        legend: {
          orient: 'vertical',
          left: '60%',
          height: '70%'
        },
        color: ['#F6D200', '#50C95F', '#4FA0FF', '#6236FF', '#B520E0', '#FF8126'],
        series: [],
      },
      listModel: 1,
      tableHeight: 500,
      showList: true,
      seawallName: '',
      grade: '',
      gradeOptions: [
        {
          label: '1级海塘',
          value: '1'
        },
        {
          label: '2级海塘',
          value: '2'
        },
        {
          label: '3级海塘',
          value: '3'
        },
        {
          label: '4级海塘',
          value: '4'
        },
        {
          label: '5级海塘',
          value: '5'
        }
      ],
      safetyConclusion: '',
      safetyConclusionOptions: [
        {
          label: '一类塘',
          value: '一类塘'
        },
        {
          label: '二类塘',
          value: '二类塘'
        },
        {
          label: '三类塘',
          value: '三类塘'
        },
        {
          label: '待鉴定',
          value: '待鉴定'
        }
      ],
      userType: '1'
    };
  },
  
  mounted() {
    this.userType = this.$sessionData('get', 'userInfo').userType
    this.getList()
    // this.getHtyj()
    // this.getHtbj()
    // this.getAqjd()
    // this.getHtdj()
  },
  methods: {
    async getList(){
      let res = await seawallListApi.list({
        seawallName: this.seawallName,
        grade: this.grade,
        safetyConclusion: this.safetyConclusion
      })
      if(res.code === 0){
        this.projectList = res.data.list
        if(this.listModel === 2 ){
          this.listPoints()
        }
      }
    },

    async getHtyj(){
      this.chartData1.yData = []
      let res = await seawallListApi.htyj()
      if(res.code === 0){
        this.chartData1.yData = [
          res.data.zc,
          res.data.blue,
          res.data.yellow,
          res.data.orange,
          res.data.red,
        ]
      }
    },
    async getHtbj(){
      this.chartData2.yData = []
      let res = await seawallListApi.htbj()
      if(res.code === 0){
        this.chartData2.yData = [
          res.data.zc,
          res.data.blue,
          res.data.yellow,
          res.data.orange,
          res.data.red,
        ]
      }
    },
    async getAqjd(){
      let res = await seawallListApi.aqjd()
      if(res.code === 0){
        this.chartData3.series = [
          {
            name: '一类塘',
            value: res.data.ylt
          },
          {
            name: '二类塘',
            value: res.data.elt
          },
          {
            name: '三类塘',
            value: res.data.slt
          },
          {
            name: '待鉴定',
            value: res.data.djd
          }
        ]
      }
    },
    async getHtdj(){
      let res = await seawallListApi.htdj()
      if(res.code === 0){
        this.chartData4.series = [
          {
            name: '1级海塘',
            value: res.data.one
          },
          {
            name: '2级海塘',
            value: res.data.two
          },
          {
            name: '3级海塘',
            value: res.data.three
          },
          {
            name: '4级海塘',
            value: res.data.four
          },
          {
            name: '5级海塘',
            value: res.data.five
          }
        ]
      }
    },

    async goProjectDetail(item){
      let seawallDetail = item
      seawallDetail.prcd = item.seawallCode
      let res = await getOutsideToken({
        prcd: item.seawallCode
      })
      if(res.code === 0){
        seawallDetail.outsideToken = res.data
        this.$store.commit('SET_SEAWALL_DETAIL', seawallDetail)
        this.$router.push('/projectMenu')
      }
    },
    changeListModel(num){
      this.listModel = num
      if(num === 2){
        this.$nextTick(function() {
          const height = this.$refs.tableContainer.clientHeight;
          this.tableHeight = height;
          let _this = this;
          window.onresize = function() {
            const height = this.$refs.tableContainer.clientHeight;
            this.tableHeight = height;
          };
          this.listPoints()
        });
      }
    },
    listPoints(){
      let pointArr = []
      let lineArr = []
      this.projectList.map(v=>{
        v.lat = v.latitude
        v.lng = v.longitude
        lineArr.push({
          line: v.coorpot ? JSON.parse(v.coorpot.replace(new RegExp('&quot;',"gm"),'"'))[0] : '',
          level: v.seawallLevel
        })
      })
      this.$refs.mapModelDom._drawPoints(this.projectList);
      this.$refs.mapModelDom._drawLines(lineArr)
    },
    clickShowList(flag){
      this.showList = flag
    }
  }
}
</script>

<style lang='scss' scoped>
  .wrap{
    padding-bottom: 16px;
    .wrap_top{
      @include flexbox();
      @include flexJC(space-between);
      margin-bottom: 16px;
      .top_item_box{
        @include contentBox();
        width: calc(25% - 12px);
        height: 320px;
        .chart_wrap{
          width: 100%;
          height: calc(100% - 56px);
        }
      }
    }
    .wrap_bottom{
      @include contentBox();
      box-sizing: border-box;
      padding: 8px 16px 16px 16px;
      .list_search_box{
        @include flexbox;
        @include flexAI(center);
        @include flexJC(space-between);
        padding: 16px 0;
        .search_left{

        }
        .search_right{
          @include flexbox;
          .change_model_btn{
            width: 110px;
            height: 32px;
            background: #f8f8f8;
            border: 1px solid #D6D6D6;
            border-radius: 4px;
            box-sizing: border-box;
            cursor: pointer;
            @include flexbox;
            @include flexAI(center);
            @include flexJC(center);
            img{
              margin-right: 8px;
              vertical-align: middle;
            }
            span{
              color: #333;
              font-size: 14px;
              vertical-align: middle;
            }
            &.active{
              border: 1px solid #1492FF;
              background: #1492FF;
              span{
                color: #fff;
              }
            }
            &:first-child{
              margin-right: 8px;
            }
          }
        }
      }
      .map_model{
        position: relative;
        width: 100%;
        height: 500px;
        overflow: hidden;
        .map_model_list{
          height: 100%;
          width: 40%;
          position: absolute;
          top: 0;
          right: -40%;
          @include transition(all, 0.3s, ease);
          &>img{
            position: absolute;
            left: -20px;
            top: 50%;
            margin-top: -33px;
            cursor: pointer;
          }
        }
        .showList{
          right: 0
        }
      }
    }
  }
</style>