<!--
 * @Author: hanyu
 * @LastEditTime: 2022-02-28 11:30:59
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenWorkbench\WorkbenchLeft.vue
-->
<template>
  <div class='wrap'>
    <div class="header">
      <div class="header_left">
        <img :src="$config.imgUrl+ 'personnal-avatar.png'" alt="" class="avatar">
        <div class="name">
          <p class="name_name">{{responsiblePersonName}}</p>
          <p class="name_title" style="font-size:14px;">{{responsibleTitle || '-'}}</p>
        </div>
      </div>
      <div class="header_right">
        <div class="name">
          <p class="name_name" style="color:#69E3EE">{{manageReservoirs}} <span class="unit">座</span> </p>
          <p class="name_title">管理水库</p>
        </div>
        <div class="lines"></div>
        <div class="name">
          <p class="name_name" style="color:#F7B500">{{upcomingTasks}} <span class="unit">件</span></p>
          <p class="name_title">待办任务</p>
        </div>
      </div>
    </div>
    <div class="duty_wrapper">
      <div class="left">
        <i class="duty_icon"></i><span>我的职责</span>
      </div>
      <div class="right" @click="showPerformDuties = true">
        <i class="list_icon"></i><span>责任清单</span>
      </div>
    </div>
    <div class="query_wrapper work_left_box">
      <!-- 查询区域 -->
      <template>
        <el-select popper-class="query_select" clearable v-model="scaleSelect" placeholder="水库规模" @change="getSelectCount">
          <el-option v-for="item in scaleList" :key="item.value" :label="item.scale" :value="item.value">
          </el-option>
        </el-select>
        <el-input v-model="inputName" placeholder="水库名称" clearable @input="getSelectCount" suffix-icon="el-icon-search"></el-input>
        <el-button plain @click="getSelectCount">查询</el-button>
      </template>
    </div>
    <div class="main_list" v-for="(item,index) in chargeReservoirList" :key="index">
      <p class="third_title">
        <img class="point" :src="$config.imgUrl+ 'point.png'" alt="">
        <span>{{item.typeName}}</span>
        <span class="unit">{{item.skList.length}}座</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl+ 'arr.gif'" alt="">
      </p>
      <ul class="list_box">
        <!-- threeYards 三色码(红色，黄色，绿色) -->
        <li :class="['list_item', skItem.selected == true ? 'avitve' : '']" v-for="(skItem, skIndex) in item.skList" :key="skIndex" @click="chooseDetailNav(index, skIndex);chooseReservoir(skItem, item.typeName)">
          <span v-show='skItem.num !== 0' class="badge">{{skItem.num}}</span>
          <i :class="[skItem.threeYards === '红色'?'red':skItem.threeYards === '黄色'? 'yellow':'green']"></i>
          <span class="reservire_name">{{skItem.name?skItem.name:'-'}}</span>
        </li>
      </ul>
    </div>
    <div v-show="chargeReservoirList.length === 0" style="text-align:center;margin-top:40px;"> 暂无水库 </div>

    <!-- 职责履行 perform duties -->
    <primary-dialog mainTitle="责任人履职" :visible="showPerformDuties" @handleClose="closePerformDuties" width="75%" top="10vh">
      <div class="dialog-content">
        <perform-duties v-if="showPerformDuties" />
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import { searchEntity, searchCount, selectGzt, statusTypeCount } from "@/api/api_reservoir";
import PrimaryDialog from '../components/PrimaryDialog.vue'
import PerformDuties from './projectDetail/PerformDuties.vue'
export default {
  name: 'WorkBenchLeft',
  props: {

  },
  components: {
    PrimaryDialog, PerformDuties
  },
  computed: {
    tasklistChange() {
      return this.$store.state.tasklistChange
    },
  },
  watch: {
    tasklistChange(val) {
      this.getSelectGzt()
      this.getSelectEntity()
      // this.getSelectCount()
    },
  },
  data() {
    return {
      chargeReservoirList: [],
      reservoirIndex: '',
      scaleList: [{ scale: '大(1)型', value: 1 }, { scale: '大(2)型', value: 2 }, { scale: '中型', value: 3 }, { scale: '小(1)型', value: 4 }, { scale: '小(2)型', value: 5 }],
      scaleSelect: '',
      inputName: '',
      loading: false,
      showPerformDuties: false,
      responsiblePersonName: '',
      responsibleTitle: '',
      upcomingTasks: 0,
      manageReservoirs: 0,
    };
  },
  mounted() {
    this.getSelectGzt()
    this.getSelectEntity()
    this.getSelectCount()
  },
  methods: {
    async getSelectGzt() {
      let res = await selectGzt()
      if (res.success) {
        this.responsiblePersonName = res.data.psnm
        this.upcomingTasks = res.data.rwCount
        this.manageReservoirs = res.data.skCount
        this.responsibleTitle = res.data.zc
      }
    },
    async getSelectEntity() {
      let opt = {
        statusType: 2
      }
      let res = await searchEntity(opt)
      if (res.success) {

      }
    },
    async getSelectCount() {
      let opt = {
        projectScale: this.scaleSelect,
        proName: this.inputName
      }
      let res = await searchCount(opt)
      if (res.success && res.data.length !== 0) {
        this.chargeReservoirList = res.data
        this.chargeReservoirList[0].skList[0].selected = true
        this.$parent.showReservoirDetail(res.data[0].skList[0], res.data[0].typeName)
      } else {
        this.chargeReservoirList = []
      }
    },
    chooseReservoir(reservoirDetail, dutyType) {
      // console.log(reservoirDetail, 'reservoirDetail');
      this.$parent.showReservoirDetail(reservoirDetail, dutyType)
    },
    chooseDetailNav(index, skIndex) {
      for (let i = 0; i < this.chargeReservoirList.length; i++) {
        for (let k = 0; k < this.chargeReservoirList[i].skList.length; k++) {
          this.chargeReservoirList[i].skList[k].selected = false
        }
        this.$set(this.chargeReservoirList, i, this.chargeReservoirList[i])
      }
      this.chargeReservoirList[index].skList[skIndex].selected = true
      this.$set(this.chargeReservoirList, index, this.chargeReservoirList[index])
    },
    closePerformDuties() {
      this.showPerformDuties = false
    },
  }
}
</script>

<style lang='scss' scoped>
.wrap {
  margin: 0 3%;
  .header {
    width: 100%;
    height: 100px;
    background: linear-gradient(
      319deg,
      #0e74d962 0%,
      rgba(6, 63, 178, 0.137) 100%
    );
    // opacity: 0.5;
    color: #ffffff;
    @include flexbox();
    @include flexAI(center);
    @include flexJC(space-around);
    .header_left {
      width: 50%;
      height: 100%;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);
    }
    .header_right {
      width: 40%;
      height: 100%;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);
    }
    .header_left {
      .avatar {
        width: 32%;
        border-radius: 50%;
        margin-right: 5%;
      }
      .name {
        height: 100%;
        @include flexbox();
        @include flexflow(column nowrap);
        @include flexJC(space-around);
        .name_name {
          font-size: 40px;
          font-family: "youshe";
          color: #ffffff;
          line-height: 40px;
        }
        .name_title {
          font-size: 20px;
          font-weight: 400;
          color: #ffffff;
          line-height: 20px;
        }
      }
    }
    .header_right {
      @include flexbox();
      @include flexAI(center);
      @include flexJC(space-around);
      .name {
        font-size: 26px;
        .name_name {
          color: #69e3ee;
        }
        .unit,
        .name_title {
          font-size: 16px;
          font-weight: 400;
          color: #ffffff;
          margin-top: 5%;
        }
      }
    }
  }
  .duty_wrapper {
    width: 100%;
    margin-top: 5%;
    height: 70px;
    background: linear-gradient(180deg, #0e74d962 0%, rgba(6, 63, 178, 0) 100%);
    border-top: 1px solid #69e3ee;
    .left {
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);
      width: 30%;
      .duty_icon {
        width: 30px;
        height: 30px;
        background: center/80% url($imageUrl + "wodezhize.png")
          no-repeat;
        margin-left: 10%;
        margin-right: 3%;
        display: inline-block;
      }
      span {
        font-size: 26px;
        font-family: "youshe";
        color: #fff;
        line-height: 70px;
      }
    }
    .right {
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);
      width: 30%;
      cursor: pointer;
      .list_icon {
        width: 30px;
        height: 30px;
        background: center/50%
          url($imageUrl + "zerenqingdan.png") no-repeat;
        margin-right: 5%;
        margin-left: 3%;
        display: inline-block;
      }
      span {
        font-size: 24px;
        font-family: "youshe";
        color: #69e3ee;
        line-height: 70px;
      }
    }
  }
  .main_list {
    width: 100%;
    .list_box {
      width: 98%;
      margin: 10px auto;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(flex-start);
      @include flexflow(row wrap);
      // .el-badge {
      //   width: 28%;
      // }
      .list_item {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(flex-start);
        width: 28%;
        height: 40px;
        margin: 6px 3% 6px 0;
        background: #1990ff44;
        box-shadow: inset 0px -10px 5px 0px #063fb2;
        // background: rgba(105, 227, 238, 0.2);
        // box-shadow: inset 0px 2px 20px 0px #69e3ee;
        border: 1px solid #1990ff;
        position: relative;
        transition: all 0.2s linear;
        cursor: pointer;
        &.avitve {
          background: rgba(105, 227, 238, 0.2);
          box-shadow: inset 0px 2px 20px 0px #69e3ee;
          border: 1px solid #69e3ee;
        }
        .badge {
          position: absolute;
          top: -14px;
          right: -12px;
          display: block;
          width: 24px;
          height: 24px;
          background: #e02020;
          font-size: 14px;
          text-align: center;
          line-height: 24px;
          border-radius: 12px;
          color: #fff;
          font-family: none;
        }
        i {
          width: 20px;
          height: 20px;
          margin: 0 10px;
          &.red {
            background: center/100%
              url($imageUrl + "status-red.png") no-repeat;
          }
          &.green {
            background: center/100%
              url($imageUrl + "status-green.png") no-repeat;
          }
          &.yellow {
            background: center/100%
              url($imageUrl + "huang.png") no-repeat;
          }
          span {
            font-size: 26px;
            font-weight: 500;
            color: #ffffff;
            line-height: 24px;
          }
        }
      }
    }
  }
}
.third_title {
  @include flexbox();
  @include flexAI(center);
  @include flexJC(center);
  font-weight: bold;
  color: #fff;
  margin-top: 20px;
  .point {
    width: 20px;
    margin-right: 5px;
  }
  span {
    font-family: "youshe";
    font-size: 24px;
    cursor: pointer;
    user-select: none;
    &.unit {
      margin-left: 2%;
      font-family: "youshe";
      color: #69e3ee;
      font-size: 20px;
    }
  }
  i {
    display: block;
    height: 1px;
    @include flex(1);
    border-bottom: 1px solid #4c70a3;
    margin: 0 10px;
  }
  .arr {
    width: 30px;
  }
}
.dialog-content {
  height: 600px;
  ::v-deep .primary-dialog {
    .el-dialog__body {
      padding: 0;
    }
  }
}
</style>

<style lang='scss'>
.work_left_box {
  @include flexbox();
  @include flexAI(center);
  @include flexJC(space-between);
  .query_select {
    flex: 1;
  }
  .el-input {
    width: 46%;
  }
}
</style>