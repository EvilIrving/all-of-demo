<template>
  <div class="wrap_box">
    <div class="wrap_left">
      <div class="item_box_title">
        <p>潮位曲线</p>
      </div>

      <div style="display: flex;flex-direction: row;margin-bottom: 5px">
        <p style="margin-left: 10px">堤顶高程：<span>{{crestElevatio}}</span></p>
        <p style="margin-left: 25px">潮位：<span>{{tdz}}</span></p>
      </div>
      <div id="sectionChart" style="width: 370px;height: 400px"></div>

    </div>
    <div class="wrap_right">
      <div class="item_box_title">
        <p>列表</p>
      </div>
      <div class="table_box">
        <el-table :data="tableData" ref="chooseTable" stripe border style="width: 95%" height="400">
          <el-table-column type="index" align="center" width="40" ></el-table-column>
          <el-table-column prop="tm" label="时间" align="center"></el-table-column>
          <el-table-column prop="h1" label="H1%(m)" align="center"></el-table-column>
          <el-table-column prop="h2" label="H2%(m)" align="center"></el-table-column>
          <el-table-column prop="h13" label="H13%(m)" align="center" width="83px"></el-table-column>
          <el-table-column prop="crestElevatio" label="堤顶高程(m)" align="center" width="98px"></el-table-column>
        </el-table>
      </div>
    </div>
  </div>


</template>

<script>
import echarts from "echarts";
import {stormSurgeWarningApi} from "@/api/seawallPrevent";
export default {
  name: "sectionDetail",
  props:{
    id:{
      type:String,
      default:'',
    }
  },
  components:{},
  data(){
    return{
      tableData: [''],
      h1data:[],
      h2data:[],
      h13data:[],
      crestElevatio:'',
      tdz:''

    }
  },
  mounted() {
    if(this.id){
      this.getDetail()
    }


  },
  methods:{
    getchart(){
      var myChart = echarts.init(document.getElementById('sectionChart'));
      var option={
        tooltip: {
          trigger: 'axis',
          backgroundColor:'rgba(20, 146, 255, 1)',
          extraCssText:'opacity:0.67'
        },
        legend: {
          data: ['潮位', 'H1%', 'H2%', 'H13%', '堤顶高程'],
          textStyle:{
            fontSize: 12
          }
        },
        grid: {
          left: '2%',
          right: '3%',
          bottom: '5%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['0', '2', '4', '6', '8', '10', '12','14','16','18','20','22','24'],
          axisTick:{
            show:false
          },
          axisLine:{
            show:false
          },
          axisLabel:{
            color:'rgba(153, 153, 153, 1)'
          }
        },
        yAxis: {
          type: 'value',
          name:'m',
          nameTextStyle:{
            color:'rgba(153, 153, 153, 1)'
          },
          axisLine:{
            onZero:false,
            show:false
          },
          axisLabel:{
            color:'rgba(153, 153, 153, 1)',
          },
          axisTick:{
            show:false
          },
        },
        series:[
          {
            name: 'H1%',
            type: 'line',
            smooth:'true',
            data: this.h1data,
            itemStyle:{
              color:'rgba(1, 165, 156, 1)'
            }
          },
          {
            name: 'H2%',
            type: 'line',
            smooth:'true',
            data: this.h2data,
            itemStyle:{
              color:'rgba(255, 183, 11, 1)'
            }
          },
          {
            name: 'H13%',
            type: 'line',
            smooth:'true',
            data:this.h13data,
            itemStyle:{
              color:'rgba(146, 7, 255, 1)'
            }
          },

        ]


      }
      myChart.setOption(option)
    },

    async getDetail(){
      //console.log(this.id)
      let res = await stormSurgeWarningApi.forecastSectionInfo({
        sectionId:this.id,
      })
      if(res.code==0){
        this.tableData = res.data
        this.crestElevatio = res.data[0].crestElevatio
        this.tdz = res.data[0].tdz

      }
      for(var item of this.tableData){
        this.h1data.push(item.h1)
        this.h2data.push(item.h2)
        this.h13data.push(item.h13)
      }
      console.log(this.h1data,123132)
      this.getchart()
    }

  }
}
</script>

<style scoped lang="scss">
.wrap_box{
  @include flexbox();
  flex-direction: row;
  .wrap_left {
    @include contentBox();
    width: 40%;
    height: 100%;
    box-shadow: none;
    border: none;
    .item_box_title{
      display: block;
      padding-top: 16px;
      height: 40px;
    }

  }
  .wrap_right{
    @include contentBox();
    box-shadow: none;
    border: none;
    width: 60%;
    height: 100%;
    .item_box_title{
      display: block;
      padding-top: 16px;
      height: 40px;
    }
  }
}

</style>