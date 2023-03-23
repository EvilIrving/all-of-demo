<template>
  <div ref="dragCard" class="drag-card collapse-panel__border drag-box">
    <div class="drag-crad__content">
      <panel-item title="预测">
        <el-select v-model='value' style='margin-left: 10px;width: 117px;height:24px' @change='getChartData()'>
          <el-option
            v-for="item in tableList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
            >
          </el-option>
        </el-select>
        <div class='table_box' style='height: 500px'>
          <div v-show='value==1' >
              <EchartLine ref="lineChart" :extraOption="extraOption"></EchartLine>
          </div>
          <div v-show='value==2'>
              <EchartLine ref="lineChart" :extraOption="extraOption"></EchartLine>
          </div>
          <div v-show='value==3'>
              <EchartLine ref="lineChart" :extraOption="extraOption"></EchartLine>
          </div>
          <div v-show='value==4'>
              <EchartLine ref="lineChart" :extraOption="extraOption4"></EchartLine>
          </div>
        </div>
      </panel-item>
    </div>

  </div>
</template>

<script>
import EchartLine from '@/views/water_disaster_custom/charts/echart_line/EchartLine';
import PanelItem from '@/views/ourPujiang/components/PanelItem';
import { forecastedApi } from '@/api/ourPujiang';
export default {
  name: 'forecastedChart',
  props:{

  },
  components:{
    PanelItem,
    EchartLine
  },
  computed: {
    isBigScreen() {
      return this.$route.query.large == "true";
		},
  },
  data(){
    return{
      riskIcon: false,
      tableList:[
        {
          label:'泽雅水库',
          value:'1'
        },
        {
          label:'江心屿',
          value:'2'
        },
        {
          label:'戍浦江大闸',
          value: '3'
        },
        {
          label:'断面',
          value:'4'
        }
      ],
      value:'1',
      extraOption:{},
      extraOption4:{},

      yData:''
    }
  },
  mounted() {
    this.getChartData()
  },
  methods:{
    getOption(){

      if(this.value==1){
        let ydata1=[],ydata2=[]
        for(var i=0;i<8;i++){
          ydata1.push(this.yData.base.limitLevel)
          ydata2.push(this.yData.base.floodLevel)
        }
        this.extraOption = {
          legend:{
            textStyle:{
              color:'rgba(255, 255, 255, 1)',
              fontSize:14
            },

          },
          grid: {
            left: '2%',
            right: '5%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            boundaryGap: false,
            data: ['01:00', '02:00', '03:00', '04:00', '05:00','06:00','07:00','08:00'],
            axisLabel:{
              textStyle: {
                color:'rgba(83, 114, 121, 1)',
                fontSize: 14,
              }
            }
          },
          yAxis: {
            name:'mm',
            type: 'value',
            nameTextStyle:{
              fontSize:14,
              color:'rgba(83, 114, 121, 1)',
            },
            axisLabel:{
              color:'rgba(83, 114, 121, 1)',
              fontSize: 14,
            }
          },
          series:[
            {
              name:'水位曲线',
              type:'line',
              smooth:true,
              data:[105,100,120,115,130],
              areaStyle: {},
              color:'rgba(0, 186, 255, 0.5)'

            },
            {
              name:'汛限水位',
              type:'line',
              smooth:true,
              data:ydata1,
            },
            {
              name:'防洪高水位',
              type:'line',
              smooth:true,
              data:ydata2,
              color:'rgba(252, 238, 39, 1)'
            },


          ]
        }
        console.log(this.extraOption)
      }
      if(this.value==2){
        this.extraOption = {
          legend:{
            textStyle:{
              color:'rgba(255, 255, 255, 1)',
              fontSize:14

            }
          },
          xAxis: {
            boundaryGap: false,
            data: ['01:00', '02:00', '03:00', '04:00', '05:00'],
            axisLabel:{
              textStyle: {
                color:'rgba(83, 114, 121, 1)',
                fontSize: 14,
              }
            }
          },
          yAxis: {
            name:'mm',
            type: 'value',
            nameTextStyle:{
              fontSize:14,
              color:'rgba(83, 114, 121, 1)',
            },
            axisLabel:{
              color:'rgba(83, 114, 121, 1)',
              fontSize: 14,
            }
          },
          series:[
            {
              name:'预报潮位',
              type:'line',
              smooth:true,
              data:[25,10,20,15,30],
              areaStyle: {},
              color:'rgba(0, 186, 255, 0.5)'
            },
            {
              name:'实施潮位',
              type:'line',
              smooth:true,
              data:[25,25,25,25,25],
            },


          ]
        }
      }
      if(this.value==3){
        let ydata1=[],ydata2=[]
        for(var j=0;j<8;j++){
          ydata2.push(this.yData.deal_level)
          ydata1.push(this.yData.control_transport_level)
        }
        this.extraOption = {
          legend:{
            textStyle:{
              color:'rgba(255, 255, 255, 1)',
              fontSize:14

            }
          },
          xAxis: {
            boundaryGap: false,
            data: ['01:00', '02:00', '03:00', '04:00', '05:00'],
            axisLabel:{
              textStyle: {
                color:'rgba(83, 114, 121, 1)',
                fontSize: 14,
              }
            }
          },
          yAxis: {
            name:'mm',
            type: 'value',
            nameTextStyle:{
              fontSize:14,
              color:'rgba(83, 114, 121, 1)',
            },
            axisLabel:{
              color:'rgba(83, 114, 121, 1)',
              fontSize: 14,
            }
          },
          series:[
            {
              name:'闸上水位',
              type:'line',
              smooth:true,
              areaStyle: {},
              data:[45,35,45,30,20],
              color:'rgba(0, 186, 255, 0.5)'

            },
            {
              name:'闸下水位',
              type:'line',
              smooth:true,
              areaStyle: {},
              data:[25,10,20,15,30],
              color:'rgba(28, 255, 245, 0.5)'
            },
            {
              name:'控制水位',
              type:'line',
              smooth:true,
              data:ydata1,
              color:'rgba(252, 238, 39, 1)'
            },
            {
              name:'死水位',
              type:'line',
              smooth:true,
              data:ydata2,
              color:'rgba(228, 126, 0, 1)'
            },


          ]
        }
      }

      this.extraOption4 = {
        legend:{
          textStyle:{
            color:'rgba(255, 255, 255, 1)',
            fontSize:14

          }
        },
        xAxis: {
          boundaryGap: false,
          data: ['01:00', '02:00', '03:00', '04:00', '05:00'],
          axisLabel:{
            textStyle: {
              color:'rgba(83, 114, 121, 1)',
              fontSize: 14,
            }
          }
        },
        yAxis: {
          name:'mm',
          type: 'value',
          nameTextStyle:{
            fontSize:14,
            color:'rgba(83, 114, 121, 1)',
          },
          axisLabel:{
            color:'rgba(83, 114, 121, 1)',
            fontSize: 14,
          }
        },
        series:[
          {
            name:'预报潮位',
            type:'line',
            smooth:true,
            areaStyle: {},
            data:[25,10,20,15,30],
            color:'rgba(0, 186, 255, 0.5)'

          },
          {
            name:'实施潮位',
            type:'line',
            smooth:true,
            areaStyle: {},
            data:[45,35,45,30,20],
            color:'rgba(28, 255, 245, 0.5)'
          },
          {
            name:'警戒水位',
            type:'line',
            smooth:true,
            data:[15,15,15,15,15],
            color:'rgba(255, 0, 0, 1)'
          },
          {
            name:'保证水位',
            type:'line',
            smooth:true,
            data:[5,5,5,5,5],
            color:'rgba(252, 238, 39, 1)'
          },
        ]
      }
    },

    async getChartData(){
      console.log(this.value)
      //let yData
      await forecastedApi.forecastChart({
        index:this.value,
        hours:24
      }).then((res)=>{
        if(res.code==0){
          console.log(res,111111)
          this.yData = res.data
          //console.log(this.yData,11123123)

        }
      })
      this.getOption()
    }
  }
};
</script>

<style scoped lang='scss'>
.drag-crad__content {
  > div {
    height: 100%;
  }
  width: 100%;
  display: flex;
  flex-direction: column;
  overflow: scroll;
 // padding: 0 16px;
  ::v-deep .panel-item__content {
    display: flex;
    flex-direction: column;
    &::-webkit-scrollbar {
      display: none;
    }

  }
  &::-webkit-scrollbar {
    display: none;
  }

}

.drag-card {
  width: 500px;
  color: #fff;
  position: relative;
  background: url(~assets/images/ScreenAsset/drag-big-card-icon.png) no-repeat;
  background-size: 100% 100%;
  background-color: rgba($color: #031a2b, $alpha: 0.9);
  height: 100%;
  .risk-left-icon {
    position: absolute;
    width: 37px;
    height: 69px;
    background: url(~assets/images/ScreenAsset/risk-left-icon.png) no-repeat;
    background-size: 100% 100%;
    right: -37px;
    top: 50%;
    margin-top: -35px;
    cursor: pointer;
    &::after {
      content: '';
      width: 10px;
      height: 18px;
      background: url(~assets/images/ScreenAsset/risk-left-array-icon.png) no-repeat;
      background-size: 100% 100%;
      position: absolute;
      top: 50%;
      left: 50%;
      margin: -9px 0 0 -8px;
    }
    &.risk-icon {
      &::after {
        content: '';
        background: url(~assets/images/ScreenAsset/risk-right-array-icon.png) no-repeat;
        background-size: 100% 100%;
      }
    }
  }
}

.table_box{
  margin-top: 20px;
  margin-left: 10px;
  height: 500px;
  width: 100%;
  > div{
    height: 100%;
    .chart{
      width: 95%;
      height: 220px;
    }
  }
}
</style>

