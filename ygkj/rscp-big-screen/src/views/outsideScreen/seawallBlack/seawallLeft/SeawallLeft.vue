<!--
 * @Author: hanyu
 * @LastEditTime: 2021-09-12 11:40:18
 * @Description: 地图左侧-统计
 * @FilePath: /rscp-big-screen/src/views/bigScreen/seawallSimple/seawallLeft/SeawallLeft.vue
-->
<template>
  <div>
    <bar-chart v-loading="htfbLoading" :style="{ height: barHeight }" class="htfbChart" :chartData="htfbData" @barClick="barClickAdcd1"></bar-chart>
    <!-- <pie-chart v-loading="htsjfhbzLoading" :style="{ height: pieHeight }" class="htfbChart" :chartData="htsjfhbzData" @pieClick="pieClick1"></pie-chart>
    <div class="chart_line"></div> -->
    <pie-chart v-loading="htjbtjLoading" :style="{ height: barHeight }" class="htfbChart" :chartData="htjbtjData" @pieClick="pieClick2"></pie-chart>
    <bar-chart v-loading="htglsLoading" :style="{ height: barHeight }" class="htfbChart" :chartData="htglsData" @barClick="barClickAdcd2"></bar-chart>
    <div class="border"></div>
  </div>
</template>

<script>
import BarChart from '../components/BarChart.vue'
import PieChart from '../components/PieChart.vue'
import { seawallLeftStatisticApi } from '@/api/api_seawall'
export default {
  name: '',
  props: {
    
  },
  components: {
    BarChart, PieChart
  },
  data () {
    return {
      htfbData: {
        title: "海塘数量(条)",
        id: 'htfbChart',
        xData: [],
        yData: [],
        adcdList: [],
        unit: '条'
      },
      htfbLoading: true,

      htsjfhbzData: {
        title: "海塘设计防洪标准统计",
        id: 'htsjfhbzChart',
        unit: '',
        legend: {
          orient: 'vertical',
          left: '52%',
          height: '80%'
        },
        color: ['#E02020', '#EB7E65', '#F7C739', '#6F9DF2', '#DAB6A0', '#8BBC89', '#9270CA', '#E9DDB1', '269A99', '#FF99C3', '#1BC57E'],
        series: [],
      },
      htsjfhbzLoading: true,

      htjbtjData: {
        title: "海塘等级",
        id: 'htjbtjChart',
        unit: '条',
        legend: {
          orient: 'vertical',
          left: '65%',
          height: '80%'
        },
        color: ['#E02020', '#EB7E65', '#F7C739', '#6F9DF2', '#DAB6A0', '#8BBC89'],
        series: [],
      },
      htjbtjLoading: true,

      htglsData: {
        title: "海塘长度(km)",
        id: 'htglsChart',
        xData: [],
        yData: [],
        adcdList: [],
        unit: '公里',
        noWords: true
      },
      htglsLoading: true,

      barHeight: '',
      pieHeight: '',
    };
  },
  mounted() {
    // Promise.all([
    // ]).then(res=>{
    // })
    this.barHeight = (window.innerHeight - 145) /3  + 'px'
    // this.barHeight = window.innerHeight / 2 * 15 / (15 + 19) + 'px'
    // this.pieHeight = window.innerHeight / 2 * 19 / (15 + 19) + 'px'
    // this.barHeight = window.innerHeight * 7 / 24 + 'px'
    // this.pieHeight = window.innerHeight * 5 / 12 + 'px'

    this.getHtfbData();
    this.getHtglsData();
    this.getHtsjfhbzData();
    this.getHtjbtjData();

  },
  methods: {
    // 海塘分布
    async getHtfbData(){
      let res = await seawallLeftStatisticApi({
        statistic: 'adcd',
        adcd: this.$localData('get', 'userInfo').adcd
      })
      if(res.success){
        if(res.data.length){
          for (let i = 0; i < res.data.length; i++) {
            if(res.data[i].adnm.length > 2){
              this.htfbData.xData.push(res.data[i].adnm.substring(0,res.data[i].adnm.length - 1))
            }else{
              this.htfbData.xData.push(res.data[i].adnm)
            }
            this.htfbData.yData.push(res.data[i].num)
            this.htfbData.adcdList.push(res.data[i].adcd)
          }
        }
      } else {
        this.$message.error(res.msg ? res.msg : '请求失败')
      }
      this.htfbLoading = false
    },

    // 海塘公里数统计
    async getHtglsData(){
      let res = await seawallLeftStatisticApi({
        statistic: 'length',
        adcd: this.$localData('get', 'userInfo').adcd
      })
      if(res.success){
        let num = 0
        if(res.data.length){
          for (let i = 0; i < res.data.length; i++) {
            if(res.data[i].adnm.length > 2){
              this.htglsData.xData.push(res.data[i].adnm.substring(0,res.data[i].adnm.length - 1))
            }else{
              this.htglsData.xData.push(res.data[i].adnm)
            }
            this.htglsData.yData.push((res.data[i].length / 1000).toFixed(1))
            this.htglsData.adcdList.push(res.data[i].adcd)
            num += res.data[i].length
          }
          // this.htglsData.title = `海塘长度（${(num / 1000).toFixed(1)}公里）`
          this.htglsData.title = `海塘长度(km)`
        }
      } else {
        this.$message.error(res.msg ? res.msg : '请求失败')
      }
      this.htglsLoading = false
    },

    // 海塘设计防洪标准统计
    async getHtsjfhbzData(){
      let res = await seawallLeftStatisticApi({
        statistic: 'preventLevel',
        adcd: this.$localData('get', 'userInfo').adcd
      })
      if(res.success){
        if(res.data.length){
          for (let i = 0; i < res.data.length; i++) {
            this.htsjfhbzData.series.push({
              name: res.data[i].preventLevel + '年一遇',
              value: res.data[i].num,
              preventLevel: res.data[i].preventLevel
            })
          }
        }
      } else {
        this.$message.error(res.msg ? res.msg : '请求失败')
      }
      this.htsjfhbzLoading = false
    },

    // 海塘级别统计
    async getHtjbtjData(){
      let res = await seawallLeftStatisticApi({
        statistic: 'scale',
        adcd: this.$localData('get', 'userInfo').adcd
      })
      if(res.success){
        if(res.data.length){
          for (let i = 0; i < res.data.length; i++) {
            let htName = res.data[i].project_scale + '级海塘'
            if(res.data[i].project_scale != 6){
              this.htjbtjData.series.push({
                name: htName,
                value: res.data[i].num,
                project_scale: res.data[i].project_scale
              })
            }
          }
        }
      } else {
        this.$message.error(res.msg ? res.msg : '请求失败')
      }
      this.htjbtjLoading = false
    },

    // 海塘分布柱状图点击事件
    barClickAdcd1(index){
      if(this.htfbData.xData[index] == '省级'){
        this.$store.commit('SET_LEFT_SELECT_ADCD', '33')
      }else{
        this.$store.commit('SET_LEFT_SELECT_ADCD', this.htfbData.adcdList[index])
      }
    },

    // 海滩公里数统计柱状图点击事件
    barClickAdcd2(index){
      if(this.htglsData.xData[index] == '省级'){
        this.$store.commit('SET_LEFT_SELECT_ADCD', '33')
      }else{
        this.$store.commit('SET_LEFT_SELECT_ADCD', this.htglsData.adcdList[index])
      }
    },

    // 海塘设计防洪标准统计点击事件
    pieClick1(data){
      this.$store.commit('SET_LEFT_SELECT_PREVENT_LEVEL', data.preventLevel)
    },

    // 海塘级别统计点击事件
    pieClick2(data){
      this.$store.commit('SET_LEFT_SELECT_PROJECT_SCALE', data.project_scale)
    }
  }
}
</script>

<style lang='scss' scoped>
  .htfbChart{
    width: 100%;
    // height: var (--barChartHeight);
  }
</style>