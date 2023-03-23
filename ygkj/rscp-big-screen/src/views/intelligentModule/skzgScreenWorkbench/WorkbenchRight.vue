<!--
 * @Author: hanyu
 * @LastEditTime: 2022-02-28 11:31:21
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenWorkbench\WorkbenchRight.vue
-->
<template>
  <div class='wrap'>
    <ul class="tab_wrapper">
      <li class="tab_item" :class="['tab_item',detailNavIndex === index ? 'avitve' : '']" v-for="(item, index) in navList" :key="index" @click="chooseDetailNav(index)">
        {{ item.name }}({{item.num}})
      </li>
    </ul>
    <div class="query_wrapper work_query">
      <!-- 查询区域 -->
      <template>
        <el-select popper-class="query_select" clearable v-model="taskTypeValue" placeholder="任务类型" @change="getSelectEntity">
          <el-option v-for="item in taskTypeOption" :key="item.label" :label="item.label" :value="item.label">
          </el-option>
        </el-select>
        <el-date-picker @change="getSelectEntity" value-format="yyyy-MM-dd" format="yyyy-MM-dd" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" popper-class="sk_date_picker" v-model="datetime" type="datetimerange">
        </el-date-picker>
        <el-input v-model="inputName" placeholder="水库名称" clearable @input="getSelectEntity" suffix-icon="el-icon-search"></el-input>
        <el-button plain @click="getSelectEntity">查询</el-button>
      </template>
    </div>
    <div class="sk_table_wrapper">
      <el-table v-loading='loading' @row-click="lookStatus" :height='tableHeight' :data="siteList" style="width: 100%">
        <el-table-column prop="task_type" label="任务类型" align="center"></el-table-column>
        <el-table-column prop="name" :show-overflow-tooltip="true" label="所属水库" align="center"></el-table-column>
        <el-table-column prop="scale" label="规模" align="center"></el-table-column>
        <el-table-column prop="describe" :show-overflow-tooltip="true" label="任务描述" align="center"></el-table-column>
        <el-table-column prop="tm" label="发送时间" min-width="100" align="center">
          <template slot-scope="scope">
            <div>
              {{ scope.row.tm.substring(0,10) }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="发送人" align="center"></el-table-column>
        <el-table-column prop="status" :label="'处理状态'" align="center" min-width="120px">
          <template slot-scope="scope">
            <div class="handle_status">
              <img v-if="scope.row.status ==='1' " :src="$config.imgUrl+ 'lv.png'" alt="">
              <!-- <p class="line"></p> -->
              <img v-if="scope.row.status ==='2' " :src="$config.imgUrl+ 'huang.png'" alt="">
              <!-- <p class="line"></p> -->
              <img v-if="scope.row.status ==='3' " :src="$config.imgUrl+ 'hui.png'" alt="">
              <!-- <i class="green"></i>
              <i class="orange"></i>
              <i class="gray"></i> -->
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="scale" align="center">
          <template slot="header">
            <div>操作</div>
          </template>
          <template slot-scope="scope">
            <span style="color: #03E6F0;">查看</span>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- <transition name="el-fade-in"> -->
    <div v-show="drawer" tabindex="0" @focus='focusDiv' @blur='blurDiv' outline="none" hidefocus="false" class="drawer">
      <!-- <div v-show="drawer" class="drawer"> -->
      <p class="third_title" @click="chooseMapType('除险加固')">
        <img class="point" :src="$config.imgUrl+ 'point.png'" alt="">
        <span>事项流程</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl+ 'arr.gif'" alt="">
      </p>
      <ul class="timeline">
        <li class="timeline_item">
          <p class="time">01-18 10:20</p>
          <i class="icon_report"></i>
          <span class="status">上报</span>
          <div class="lines"></div>
          <div class="matter_process_item">
            <div class="top">
              <img :src="$config.imgUrl+ 'avatar.png'" alt="">
              <div class="right">
                <div class="name">
                  <span>郑志光</span>
                  <span class="charge_title">县级</span>
                </div>
                <div class="bottom">巡查责任人</div>
              </div>
            </div>
            <div class="describe">
              发现水库大坝、启闭设施等损坏。
            </div>
          </div>
        </li>
        <li class="timeline_item">
          <p class="time">01-18 10:20</p>
          <i class="icon_refresh"></i>
          <span class="status">上报</span>
          <div class="lines"></div>
          <div class="matter_process_item">
            <div class="top">
              <img :src="$config.imgUrl+ 'avatar.png'" alt="">
              <div class="right">
                <div class="name">
                  <span>郑志光</span>
                  <span class="charge_title">县级</span>
                </div>
                <div class="bottom">巡查责任人</div>
              </div>
            </div>
            <div class="describe">
              发现水库大坝、启闭设施等损坏。
            </div>
          </div>
        </li>
        <li class="timeline_item">
          <p class="time">01-18 10:20</p>
          <i class="icon_refresh"></i>
          <span class="status">上报</span>
          <div class="lines"></div>
          <div class="matter_process_item">
            <div class="top">
              <img :src="$config.imgUrl+ 'avatar.png'" alt="">
              <div class="right">
                <div class="name">
                  <span>郑志光</span>
                  <span class="charge_title">县级</span>
                </div>
                <div class="bottom">巡查责任人</div>
              </div>
            </div>
            <div class="describe">
              发现水库大坝、启闭设施等损坏。
            </div>
          </div>
        </li>
        <li class="timeline_item">
          <p class="time">01-18 10:20</p>
          <i class="icon_seal"></i>
          <span class="status">上报</span>
          <div class="lines"></div>
          <div class="matter_process_item">
            <div class="top">
              <img :src="$config.imgUrl+ 'avatar.png'" alt="">
              <div class="right">
                <div class="name">
                  <span>郑志光</span>
                  <span class="charge_title">县级</span>
                </div>
                <div class="bottom">巡查责任人</div>
              </div>
            </div>
            <div class="describe">
              发现水库大坝、启闭设施等损坏。
            </div>
          </div>
        </li>
      </ul>
    </div>
    <!-- </transition> -->
  </div>
</template>

<script>
import { searchEntity, searchCount, selectGzt, statusTypeCount } from "@/api/api_reservoir";
export default {
  name: 'WorkbenchRight',
  props: {

  },
  components: {

  },
  data() {
    return {
      navList: [{ name: '我的待办', num: 0, type: 1, }, { name: '我的已办', num: 0, type: 1, }, { name: '与我相关', num: 0, type: 1, },],
      detailNavIndex: 0,
      taskTypeOption: [{ label: '工程检查' }],
      taskTypeValue: '',
      inputName: '',
      datetime: [],
      tableHeight: 300,
      loading: false,
      siteList: [],
      drawer: false,
      direction: 'rtl',

    };
  },
  mounted() {
    this.getStatusTypeCount()
    this.getSelectEntity(0)
  },
  methods: {
    async getStatusTypeCount() {
      let res = await statusTypeCount()
      if (res.success) {
        this.navList[0].num = res.data.db
        this.navList[1].num = res.data.yb
        this.navList[2].num = res.data.ywxg
      }
    },
    async getSelectEntity(index) {
      console.log(this.datetime);
      let opt = {
        statusType: index + 1,
        taskType: this.taskTypeValue,
        startTime: this.datetime[0],
        endTime: this.datetime[1],
      }
      let res = await searchEntity(opt)
      if (res.success) {
        this.siteList = res.rows
      }
    },
    lookStatus(row) {
      this.drawer = true
      this.$nextTick(() => {
        // document.querySelector('.drawer').tabIndex = -1
        document.querySelector('.drawer').focus()
      })
    },
    blurDiv() {
      this.drawer = false
    },
    focusDiv() {
      this.drawer = true
    },
    chooseDetailNav(index) {
      this.taskTypeValue = ''
      this.datetime = ''
      this.detailNavIndex = index
      this.getSelectEntity(index)
    },
  },
  watch: {
    drawer: function (newVal, oldVal) {
      if (newVal) {
        console.log(newVal, 'newVal');
        // document.querySelector('.drawer').focus()
      }
    }
  },
}
</script>

<style lang='scss' scoped>
.wrap {
  width: 100%;
  position: relative;
  color: #fff;
  .tab_wrapper {
    margin: 10px auto;
    width: 100%;
    @include flexbox();
    @include flexflow(row wrap);
    .tab_item {
      width: 16%;
      height: 40px;
      background: rgba(6, 63, 178, 0.3);
      border: 1px solid #1990ff;
      text-align: center;
      font-size: 16px;
      font-weight: 500;
      color: #ffffff;
      line-height: 40px;
      transition: all 0.2s linear;
      cursor: pointer;
      &.avitve {
        background: rgba(105, 227, 238, 0.2);
        box-shadow: inset 0px 2px 20px 0px #69e3ee;
        border: 1px solid #69e3ee;
      }
      &:not(:last-child) {
        border-right: 0px;
      }
      // &:not(:first-child) {
      //   border-right: 0px;
      // }
    }
  }
  .drawer {
    width: 45%;
    height: 550px;
    position: absolute;
    top: 0%;
    right: 0%;
    background-color: #052176;
    color: #ffffff;
    font-size: 16px;
    // overflow: auto;
    overflow-y: auto;
    outline: none;
    padding: 20px;
  }
  .third_title {
    @include flexbox();
    @include flexAI(center);
    @include flexJC(center);
    font-weight: bold;
    color: #fff;
    margin-bottom: 16px;
    .point {
      width: 20px;
      margin-right: 5px;
    }
    span {
      font-family: "youshe";
      font-size: 20px;
      cursor: pointer;
      user-select: none;
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
  // 进度线样式
  .timeline {
    padding: 10px 10px 10px 70px;
    border: 2px solid #448ef7;
    border-radius: 4px;
    position: relative;
    .timeline_item {
      position: relative;
      .icon_report {
        position: absolute;
        top: 0px;
        left: -16px;
        width: 16px;
        height: 16px;
        background: center/100% url("../../../assets/images/skzg/report.png")
          no-repeat;
      }
      .icon_refresh {
        position: absolute;
        top: 0px;
        left: -16px;
        width: 16px;
        height: 16px;
        background: center/100% url("../../../assets/images/skzg/refresh.png")
          no-repeat;
      }
      .icon_seal {
        position: absolute;
        top: 0px;
        left: -16px;
        width: 16px;
        height: 16px;
        background: center/100% url("../../../assets/images/skzg/seal.png")
          no-repeat;
      }
      .time {
        width: 18%;
        position: absolute;
        top: 0px;
        left: -60px;
        word-break: break-all;
      }
      .status {
        padding-left: 6px;
      }
      .lines {
        position: absolute;
        top: 20px;
        left: -10px;
        width: 1px;
        height: 80%;
        border-left: 2px dotted #448ef7;
      }
    }
  }
  .matter_process_item {
    background: #03338d;
    border-radius: 8px;
    padding: 8px;
    margin: 8px 0;
    color: #fff;
    div.top {
      @include flexbox();
      @include flexAI(center);
      @include flexJC(flex-start);
      img {
        width: 20%;
      }
      .right {
        font-size: 18px;
        margin-left: 10px;
        .name {
          @include flexbox();
          @include flexAI(center);
          @include flexJC(center);
          font-size: 16px;
          line-height: 24px;
          .charge_title {
            margin-left: 8px;
            font-size: 16px;
            padding: 0px 8px;
            background: rgba(6, 63, 178, 0.3);
            border: 1px solid #1990ff;
            color: #1990ff;
          }
        }
        .bottom {
          font-size: 16px;
          margin-top: 6px;
          color: rgba(255, 255, 255, 0.548);
        }
      }
    }
    div.describe {
      margin-top: 6px;
      font-size: 16px;
    }
  }
}
</style>

<style lang='scss'>
.work_query {
  margin: 0;
  @include flexbox();
  @include flexAI(center);
  @include flexJC(space-between);
  .el-select {
    width: 16%;
  }
  .el-input {
    width: 20%;
  }
  .el-date-editor {
    width: 40%;
    margin-left: 10px;
  }
  .sk_date_picker {
    background: #063fb29c;
    border: 2px solid #1990ff9d;
    .el-date-range-picker__time-header {
      background: #063fb29c;
      border: 2px solid #1990ff9d;
    }
  }
}
.sk_table_wrapper {
  .handle_status {
    @include flexbox();
    @include flexAI(center);
    @include flexJC(center);
    p.line {
      display: inline-block;
      width: 15px;
      height: 2px;
      background: #b4cbe0;
    }
    i {
      // width: 20px;
      // height: 20px;
      display: inline-block;
      &.green {
        background: center/100% url("../../../assets/images/skzg/lv.png")
          no-repeat;
      }
      &.orange {
        background: center/100% url("../../../assets/images/skzg/huang.png")
          no-repeat;
      }
      &.gray {
        background: center/100% url("../../../assets/images/skzg/hui.png")
          no-repeat;
      }
    }
  }
  .el-table__row {
    cursor: pointer;
  }
}

// .v-modal {
//   position: fixed;
//   left: 0;
//   top: 0;
//   width: 100%;
//   height: 100%;
//   opacity: 0.5;
//   background: transparent;
// }
</style>