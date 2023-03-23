<!--
 * @Author: hanyu
 * @LastEditTime: 2022-04-26 10:02:45
 * @Description: 
 * @FilePath: /lucheng_project/src/views/propertyManage/contractManagement/assessmentMmanagement/dailyEvaluation/StaffAttendance.vue
-->
<template>
  <div class='wrap'>
<!--    <section class="calendar_left">
      <div class="calendar_left_msg">
        <div class="msg">
          <img src="@/assets/images/head.png" alt="">
        </div>
        <div class="name">
          <h3>{{userInfo.username}}</h3>
          <p>{{userInfo.department}}</p>
        </div>
      </div>
      <div class="calendar_left_tj">
        <div class="item_box_title_small">
          <i></i>
          <p>出勤统计</p>
        </div>
        <div id="myChart"></div>
        <div class="legend">
          <p><i></i>实际出勤<span>23天</span></p>
          <p><i></i>未出勤<span>8天</span></p>
        </div>
      </div>
    </section>
    <section class="calendar_right">
      <div class="item_box_title_small">
        <i></i>
        <p>考勤日历</p>
      </div>
      <el-calendar>
        <template
          slot="dateCell"
          slot-scope="{date, data}">
          <p :class="data.isSelected ? 'is-selected' : ''">
            {{ data.day.split('-').slice(1).join('-') }} {{ data.isSelected ? '✔️' : ''}}
          </p>
        </template>
      </el-calendar>
    </section>-->
    <div class="item_box_title">
      <p>人员考勤</p>
    </div>
    <div class="info_option" style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box">
      <span style="font-size: 14px; margin-left: 15px">所属公司：</span>
      <el-input size="mini" placeholder="请输入" prefix-icon="el-icon-search" @change="getList" v-model="company" style="width: 180px; margin-right: 15px" clearable />
      <span style="font-size: 14px; margin-left: 15px">所属部门：</span>
      <el-input size="mini" placeholder="请输入" prefix-icon="el-icon-search" @change="getList" v-model="department" style="width: 180px; margin-right: 15px" clearable />
      <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input size="mini" placeholder="请输入" prefix-icon="el-icon-search" @change="getList" v-model="name" style="width: 180px; margin-right: 15px" clearable />
      <span style="font-size: 14px; margin-left: 15px">日期：</span>
      <el-date-picker
          size="mini"
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="year"
          value-format="yyyy-MM"
          format="yyyy-MM"
          type="month"
          placeholder="请选择"
          clearable
          @change="getList">
      </el-date-picker>
      <el-button type="primary" size="mini" style="margin-left: 450px" @click="exportTable">导出</el-button>
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column type="index" align="center" width="50" label="序号" ></el-table-column>
        <el-table-column prop="name" label="姓名" align="center"  ></el-table-column>
        <el-table-column prop="corporationName" label="公司" align="center"></el-table-column>
        <el-table-column prop="departmentName" label="部门" align="center"></el-table-column>
        <el-table-column prop="deviceName" label="设备名称" align="center"></el-table-column>
        <el-table-column prop="checkingType" label="考勤方式" align="center"></el-table-column>
        <el-table-column prop="temperature" label="温度" align="center"></el-table-column>
        <el-table-column prop="attendanceTime" label="考勤时间" align="center"></el-table-column>
        <el-table-column prop="checkingDesc" label="描述" align="center"></el-table-column>

      </el-table>
      <pagination :total="pageTotal" :page.sync="pageNum" :limit.sync="pageSize" :page-sizes="[10, 20, 50, 100]" @pagination="getList" />
    </div>
  </div>
</template>

<script>
import {staffAttendance} from "@/api/propertyManage";
import {exportStaffAttendance} from "@/utils/export";

export default {
  name: 'StaffAttendance',
  props: {
    
  },
  components: {
    
  },
  data () {
    return {
      userInfo: {},
      pageTotal:0,
      pageNum:1,
      pageSize:10,
      year:'',
      tableData:[],
      company:'',
      department:'',
      name:'',
    };
  },
  mounted() {
  /*  this.userInfo = this.$sessionData('get', 'userInfo')
    this.initDayChart()*/
    this.getList()
  },
  methods: {
  /*  initDayChart(days, undays){
      this.$echarts.init(document.getElementById('myChart')).setOption({
        title: {
          text: '31天',
          subtext: '应出勤',
          left: 'center',
          top: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        color: ['#1492FE', '#F81C68'],
        series: [
          {
            name: '出勤统计',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderColor: '#fff',
              borderWidth: 1
            },
            label: {
              show: false
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 23, name: '实际出勤' },
              { value: 8, name: '未出勤' }
            ]
          }
        ]
      })
    }*/
    async getList(){
      let res = await staffAttendance.list({
        yearMonth:this.year,
        pageNum:this.pageNum,
        pageSize:this.pageSize,
        name:this.name,
        corporationName:this.company,
        departmentName:this.department
      })
      if(res.code==0){
        this.pageTotal = parseInt(res.data.data.total)
        this.tableData = res.data.data.records
      }
      console.log(res,111)
    },
    sortForm(obj1,obj2){
      let val1 = obj1.jobNumber
      let val2 = obj2.jobNumber
      return  obj2 - obj1

    },
    exportTable(){
      console.log(111)
      let obj = {
        corporationName:this.company,
        departmentName:this.department,
        name:this.name,
        yearMonth:this.year,
      }
      console.log(obj)
      exportStaffAttendance(obj)
    }
  }
}
</script>

<style lang='scss' scoped>
  /*.wrap{
    @include flexbox();
    @include flexJC(space-between);
    .calendar_left{
      width: 312px;
      .calendar_left_msg{
        @include contentBox();
        overflow: hidden;
        .msg{
          background: url('../../../../../assets/images/qd_bg.png') no-repeat center;
          width: 100%;
          height: 122px;
          text-align: center;
          img{
            position: relative;
            bottom: -82px;
          }
        }
        .name{
          text-align: center;
          padding-top: 50px;
          h3{
            font-size: 18px;
            font-weight: 500;
            color: rgba(0, 0, 0, 0.85);
            line-height: 22px;
            margin-bottom: 10px;
          }
          p{
            font-size: 14px;
            font-weight: 400;
            color: #777777;
            line-height: 12px;
            margin-bottom: 20px;
          }
        }
      }
      .calendar_left_tj{
        @include contentBox();
        margin-top: 16px;
        height: calc(100% - 252px);
        #myChart{
          width: 100%;
          height: 250px;
        }
        .legend{
          @include flexbox();
          @include flexAI(center);
          @include flexJC(space-between);
          padding: 0 30px;
          p{
            font-size: 14px;
            font-weight: 400;
            color: #777777;
            line-height: 24px;
            i{
              display: inline-block;
              background: #F81C68;
              width: 12px;
              height: 12px;
              border-radius: 2px;
              margin-right: 6px;
            }
            span{
              font-size: 14px;
              font-weight: 400;
              color: #F81C68;
              line-height: 24px;
              margin-left: 12px;
            }
            &:first-child{
              i{
                background: #1492FE;
              }
              span{
                color: #2C5AF4;
              }
            }
          }
        }
      }
    }
    .calendar_right{
      @include contentBox();
      width: calc(100% - 328px);
    }
  }*/
  .wrap {
    @include contentBox();
    height: 100%;
  }
</style>