<!--
 * @Author: hanyu
 * @LastEditTime: 2022-02-28 10:50:15
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenWorkbench\SkzgScreenWorkbench.vue
-->
<template>
  <div class='work_wrap'>
    <section class="work_left">
      <WorkbenchLeft/>
    </section>
    <div class="line"></div>
    <section class="work_right">
      <div class="right_top">
        <p class="active">{{reservoirDetail.name ? reservoirDetail.name : '水库'}}</p>
      </div>
      <!-- <div class="right_content" v-loading="showLoading"> -->
      <div class="right_content">
        <!-- <WorkbenchRight v-if="!detailTabFlag"/> -->
        <div class="right_detail">
          <div class="right_detail_nav">
            <div :class="['nav_item',detailNavIndex == index ? 'active' : '']" v-for="(item, index) in navList" :key="index" @click="chooseDetailNav(index)">
              {{ item }}
            </div>
          </div>
          <TaskInformation v-if="showDetail && detailNavIndex == 0" :reservoirDetail="reservoirDetail" :dutyType="dutyType"/>
          <BaseMessage v-if="showDetail && detailNavIndex == 1" :reservoirDetail="reservoirDetail"/>
          <OperationPlan v-if="showDetail && detailNavIndex == 2" :reservoirDetail="reservoirDetail"/>
          <ReservoirStorage v-if="showDetail && detailNavIndex == 3" :reservoirDetail="reservoirDetail"/>
          <SolveDangerous v-if="showDetail && detailNavIndex == 4" :reservoirDetail="reservoirDetail"/>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import WorkbenchLeft from './WorkbenchLeft'
// import WorkbenchRight from './WorkbenchRight'
import TaskInformation from './projectDetail/TaskInformation'
import BaseMessage from './projectDetail/BaseMessage'
import OperationPlan from './projectDetail/OperationPlan'
import ReservoirStorage from './projectDetail/ReservoirStorage'
import SolveDangerous from './projectDetail/SolveDangerous'
import { statusTypeCount } from "@/api/api_reservoir";
export default {
  name: 'SkzgScreenWorkbench',
  props: {
    
  },
  components: {
    WorkbenchLeft,
    // WorkbenchRight,
    TaskInformation,
    BaseMessage,
    OperationPlan,
    ReservoirStorage,
    SolveDangerous
  },
  data () {
    return {
      navList: ['综合信息', '基本信息', '控运计划', '水库纳蓄', '除险加固'],
      detailTabFlag: false,
      showDetail: false,
      reservoirDetail: {},
      detailNavIndex: 0,
      upcoming:0,
      showLoading: true
    };
  },
  mounted() {
    this.getStatusTypeCount()
  },
  methods: {
    async getStatusTypeCount() {
      let res = await statusTypeCount()
      if (res.success) {
        this.upcoming = res.data.db
      }
    },
    // goBackList(){
    //   this.showDetail = false
    //   this.detailTabFlag = false
    // },
    showReservoirDetail(reservoirDetail, dutyType){
      this.showLoading = false
      // this.detailTabFlag = true
      this.showDetail = false
      this.$nextTick(()=>{
        this.showDetail = true
      })
      this.reservoirDetail = reservoirDetail
      this.dutyType = dutyType
    },
    chooseDetailNav(index){
      this.detailNavIndex = index
    }
  },
}
</script>

<style lang='scss' scoped>
  .work_wrap{
    width: 100%;
    height: 100%;
    @include flexbox();
    color: #fff;
    .line{
      width: 4px;
      height: 100%;
      background: url($imageUrl + 'work_line.png') no-repeat center;
      background-size: auto 100%;
    }
    .work_left{
      width: 40%;
    }
    .work_right{
      width: calc(60% - 4px);
      box-sizing: border-box;
      padding: 0 10px 0 25px;
      .right_top{
        width: 100%;
        height: 40px;
        background: url($imageUrl + "work_title_bg.png") no-repeat bottom;
        background-size: 100% auto;
        box-sizing: border-box;
        padding-left: 5px;
        padding-top: 5px;
        margin-bottom: 20px;
        &>img{
          width: 31px;
          height: 24px;
          vertical-align: bottom;
          cursor: pointer;
        }
        &>p{
          display: inline-block;
          font-size: 26px;
          font-family: 'youshe';
          color: #8491b7;
          line-height: 24px;
          position: relative;
          margin-left: 25px;
          cursor: pointer;
          &.active{
            font-size: 30px;
            font-family: 'youshe';
            color: #fff;
            line-height: 24px;
            text-shadow: 0px 0px 10px #69E3EE;
          }
          span{
            position: absolute;
            top: -16px;
            right: -16px;
            display: block;
            width: 24px;
            height: 24px;
            background: #E02020;
            font-size: 14px;
            text-align: center;
            line-height: 24px;
            border-radius: 12px;
            color: #fff;
            font-family: none;
          }
        }
      }
      .right_content{
        height: 580px;
        .right_detail{
          height: 100%;
          .right_detail_nav{
            @include flexbox();
            margin-bottom: 16px;
            .nav_item{
              width: 96px;
              height: 40px;
              background: rgba(6, 63, 178, 0.3);
              border: 1px solid #1990FF;
              font-size: 16px;
              font-weight: 500;
              color: #FFFFFF;
              line-height: 40px;
              text-align: center;
              box-sizing: border-box;
              border-right: none;
              cursor: pointer;
              &:last-child{
                border-right: 1px solid #1990FF;
              }
              &.active{
                background: rgba(105, 227, 238, 0.2);
                box-shadow: inset 0px 1px 10px 0px #69E3EE;
                border: 1px solid #69E3EE;
              }
            }
          }
        }
        
      }
    }
  }
</style>