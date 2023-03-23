<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>水域占用</p>
    </div>

    <div class="map_box">
      <map-model ref="mapModelDom"></map-model>
      <div class="box_info">
        <div class="info_title">
          <img src="../../../assets/images/info_title.png" alt="" />
          <span>水域占用信息</span>
        </div>
        <img src="../../../assets/images/title_line.png" alt=""/>
        <div class="box_content">
          <el-button-group style="margin-left: 16px;margin-top: 10px" >
            <el-button type="primary" style="width: 183px;height: 32px" size="small">类型占比</el-button>
            <el-button style="width: 183px;height: 32px" size="small">面积占比</el-button>
          </el-button-group>
          <div class="chart_box">
            <div id="typeChart" style="height: 153px;width: 161px"></div>
            <div class="chart_legend">
              <div class="legend_item">
                <i class="green"></i>
                <span style="font-size: 14px">河道</span>
                <span style="font-size: 20px;color: #0CD3B0;margin-left: 80px">318</span>
                <span style="font-size: 14px;color: #0CD3B0;margin-top: 4px;margin-left: 2px">个</span>
              </div>
              <div class="legend_item">
                <i class="yellow"></i>
                <span style="font-size: 14px">山塘</span>
                <span style="font-size: 20px;color: #CDB139;margin-left: 80px">11</span>
                <span style="font-size: 14px;color: #CDB139;margin-top: 4px;margin-left: 2px">个</span>
              </div>
              <div class="legend_item">
                <i class="blue"></i>
                <span style="font-size: 14px">其他水域</span>
                <span style="font-size: 20px;color: #4FA0FF;margin-left: 50px">16</span>
                <span style="font-size: 14px;color: #4FA0FF;margin-top: 4px;margin-left: 2px">个</span>
              </div>
            </div>
          </div>

        </div>

        <div class="info_title">
          <img src="../../../assets/images/info_title.png" alt="" />
          <span>水域占用列表</span>
        </div>
        <img src="../../../assets/images/title_line.png" alt=""/>
        <div class="table_box">
          <el-table :data="tableData" ref="chooseTable" stripe style="width: 100%" height="480">
            <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
            <el-table-column prop="name" label="水域名称" align="center"></el-table-column>
            <el-table-column prop="area" label="占用面积(m)" align="center"></el-table-column>

          </el-table>
        </div>


      </div>
    </div>

  </div>

</template>

<script>
import * as echarts from 'echarts';
import MapModel from "@/views/occupyApprove/hydrograph/components/MapModel";
import {occupyList} from "@/api/projectUnitDetail";
import {hydrographApi} from "@/api/occupyApprove";
export default {
  name: "waterOccupy",
  props: {},
  components: {
    MapModel

  },
  data(){
    return{
      tableData:[],
      projectList:[
      //     {
      //   latitude: "28.031788",
      //   levelDatum: null,
      //   longitude: "120.646276",
      //   seawallName: "江心屿景区防洪堤塘",
      //   seawallPattern: "河道",
      //   styleObj:{
      //     src: require("@/assets/images/riverway.png"),
      //   },
      //   type:'riverWay'
      // },
      //   {
      //     latitude: "28.021617",
      //     levelDatum: null,
      //     longitude: "120.749273",
      //     seawallName: "七都标准堤西北堤",
      //     seawallPattern: "山塘",
      //     styleObj: {
      //       src: require("@/assets/images/pool.png"),
      //     },
      //     type:'pool'
      //   },{
      //     latitude: "28.005417",
      //     levelDatum: null,
      //     longitude: "120.782778",
      //     seawallName: "七都标准堤东堤南堤吟州堤",
      //     seawallPattern: "其他水域",
      //     styleObj: {
      //       src: require("@/assets/images/otherWater.png"),
      //     },
      //     type:'otherWater'
      //   },

      ],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,

    }
  },
  mounted() {
    this.getList()
    this.chartInit()
    this.listPoints()
  },
  methods:{
    chartInit(){
      var chartDom = document.getElementById('typeChart');
      var myChart = this.$echarts.init(chartDom);
      var option;
      option={
        tooltip:{},
        series:[
          {
            name: '类型占比',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            label:{
              show:false
            },
            labelLine: {
              show: false
            },
            data: [
              {
                value: 318,
                name: '河道',
                itemStyle:{
                  color:'rgba(65, 187, 90, 1)'
                }

              },
              {
                value: 11,
                name: '山塘',
                itemStyle: {
                  color:'rgba(255, 203, 39, 1)'
                }

              },
              {
                value: 16,
                name: '其他水域',
                itemStyle: {
                  color:'rgba(39, 106, 240, 1)'
                }

              },

            ]
          }
        ]
      }
      myChart.setOption(option)
    },

    async getList(){

      let res = await hydrographApi.occupyList({
       // pageNum: this.pageNum,
        //pageSize: this.pageSize,
      })
      console.log(res,1111)
      if(res.code==0){
          this.tableData = res.data.list
      }
      this.listPoints()
    },
    listPoints(){

      this.projectList.map(v=>{
        v.lat = v.latitude
        v.lng = v.longitude
        switch (v.seawallPattern){
          case "山塘":{
            v.styleObj.src = require("@/assets/images/pool.png")
          }
          case "河道":{
            v.styleObj.src = require("@/assets/images/riverway.png")
          }
          case "其他水域":{
            v.styleObj.src = require("@/assets/images/otherWater.png")
          }
        }
      })
      this.$refs.mapModelDom.drawMapPoints(this.projectList);
    },

  }
}
</script>

<style scoped lang="scss">
.wrap {
  @include contentBox();
  height: 100%;
.item_box_title{
  display: block;
  padding-top: 16px;
  height: 40px;
}
  .map_box{
    height: 834px;
    .box_info{
      position: absolute;
      top: 22.8%;
      left:77.8%;
      @include contentBox();
      height: 75%;
      width: 400px;
      img{
        width: 366px;
        margin-left: 16px;
      }
      .info_title{
        margin-top: 16px;
        display: flex;
        flex-direction: row;
        img{
          width: 18px;
          height: 18px;
          margin-right: 9px;
        }
        span{
          font-size: 18px;
        }
      }
      .chart_box{
        display: flex;
        flex-direction: row;
        .chart_legend{
          .legend_item{
            display: flex;
            flex-direction: row;
            margin-top: 25px;
            .green{
                width: 16px;
                height: 16px;
                background-color: #41BB5A;
                margin-right: 10px;
              }
            .blue{
              width: 16px;
              height: 16px;
              background-color:#276AF0;
              margin-right: 10px;
            }
            .yellow{
              width: 16px;
              height: 16px;
              background-color:#FFCB27;
              margin-right: 10px;
            }
            span{
              font-family: PingFang SC;
              font-weight: 400;
            }


          }
        }
      }
      .table_box{
        width: 366px;
        margin-left: 16px;
      }
    }
  }
}
</style>
