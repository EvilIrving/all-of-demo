<!--
 * @Author: hanyu
 * @LastEditTime: 2022-02-28 11:30:23
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenWorkbench\projectDetail\TaskInformation.vue
-->
<template>
  <div class='wrap'>
    <section class="page_top">
      <p class="third_title">
        <img class="point" :src="$config.imgUrl+ 'point.png'" alt="">
        <span>与我相关</span>
        <span style="margin-left: 5px; color: #69E3EE">{{taskNumber}}件</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl+ 'arr.gif'" alt="">
      </p>
      <div class="task_list">
        <div class="task_item" v-for="(item, index) in taskList" :key="index">
          <div class="task_title">
            <p>
              <img :src="$config.imgUrl+ 'point.png'" alt="">
              <span>{{item.title}}</span>
            </p>
            <img :src="$config.imgUrl+ 'arr.gif'" alt="">
          </div>
          <div class="task_text">{{item.describe}}</div>
          <div class="task_btn" :class="item.status == 0 ? 'no' : 'yes'" @click="dealTask(item.status, item.taskId)">{{item.status == 0 ? '立即办理' : '已办理'}}</div>
        </div>
      </div>
    </section>
    <!-- Object.keys(obj).length === 0 ? '空' : '不为空'-->
    <section class="page_bottom">
      <p class="third_title">
        <img class="point" :src="$config.imgUrl+ 'point.png'" alt="">
        <span>风险隐患</span>
        <span v-if="riskListNumber" style="margin-left: 5px; color: #FF5D5D">{{ riskListNumber }}件</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl+ 'arr.gif'" alt="">
      </p>
      <div class="yh_list" v-if="riskListNumber">
        <div class="yh_item" v-if="oneIs">
          <h3>
            <img :src="$config.imgUrl+ 'status-red.png'" alt="">
            <span class="red">防洪预警</span>
          </h3>
          <p>当前水库水位：<span>{{ oneIs ?riskHiddenData.list[0].waterLevel:'-'}}</span>({{oneIs ?riskHiddenData.list[0].waterLevelTM:'-'}})；正常蓄水位：<span>{{oneIs?riskHiddenData.list[0].normalWaterLevel:'-'}}</span>；超汛限(正常)水位：<span>{{ oneIs ?riskHiddenData.list[0].overLimitwaterlevelTime:'-'}}</span>；持续时间<span>{{ oneIs ? riskHiddenData.list[0].lastTime:"-"}}</span>；</p>
        </div>
        <div class="yh_item" v-show="twoIs">
          <h3>
            <img :src="$config.imgUrl+ 'status-yellow.png'"  alt="">
            <span class="yellow">安全鉴定</span>
          </h3>
          <p>最近一次安全鉴定结论：{{twoIs ?riskHiddenData.list[1].safetyIdentification:"-"}}；{{twoIs && riskHiddenData.list[1].rRefStSm !== null ?'已开展':"未开展"}}除险加固工作，预计完工时间：{{twoIs && riskHiddenData.list[1].rRefStSm?riskHiddenData.list[1].rRefStSm:" -"}}</p>
        </div>
        <div class="yh_item" v-show="threeIs">
          <h3>
            <img :src="$config.imgUrl+ 'status-yellow.png'"  alt="">
            <span class="yellow">工程检查</span>
          </h3>
          <p>现场检查中发现轻微隐患，目前正在处置中</p>
        </div>
      </div>
      <div class="nodata" v-if="!riskListNumber">
        <img :src="$config.imgUrl+ 'norisk.png'" alt="">
      </div>

    </section>

    <!-- 顶部标题点击弹窗 -->
    <primary-dialog mainTitle="" :visible="showDealTaskDialog" @handleClose="handleCloseTaskDialog" width="600px">
      <div class="dialog-content">
        
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import { taskApi, riskHiddenDanger } from "@/api/api_reservoir";
import PrimaryDialog from '../../components/PrimaryDialog.vue';
export default {
  name: 'TaskInformation',
  props: {
    reservoirDetail: {
      type: Object
    },
    dutyType: String
  },
  components: {
    PrimaryDialog
  },
  computed: {
    riskListNumber() {
      let one = ['R4', 'R5', 'R6',] //1条
      let two = ['Y2', 'Y3',] //2条
      let three = ['Y4'] //3条
      one.includes(this.riskHiddenData.tyCause) ? this.oneIs = true : this.oneIs = false

      two.includes(this.riskHiddenData.tyCause) ? this.twoIs = true : this.twoIs = false

      three.includes(this.riskHiddenData.tyCause) ? this.threeIs = true : this.threeIs = false

      let tempArr = [this.oneIs, this.twoIs, this.threeIs]
      let num = 0
      for (let index = 0; index < tempArr.length; index++) {
        tempArr[index] ? num++ : ''
      }
      return num
    },
  },
  data() {
    return {
      taskList: [],
      riskHiddenData: {},
      taskNumber: null,
      flag: true,
      isShowRisk: false,
      twoIs: false,
      twoIs: false,
      threeIs: false,
      showDealTaskDialog: false,
    };
  },
  mounted() {
    this.getTaskList()
    this.riskHIddenData()
  },
  methods: {
    async getTaskList() {
      let dutyType = ''
      switch (this.dutyType) {
        case '政府责任人':
          dutyType = 1
          break;
        case '水行政主管部门责任人':
          dutyType = 2
          break;
        case '主管部门(产权人)责任人':
          dutyType = 3
          break;
        case '管理单位责任人':
          dutyType = 4
          break;
        case '技术负责人':
          dutyType = 5
          break;
        case '巡查负责人':
          dutyType = 6
          break;
      }
      let res = await taskApi.list({
        prcd: this.reservoirDetail.prcd,
        dutyType: dutyType
      })
      if (res.success) {
        this.taskNumber = res.rows.filter((i) => i.status === 0).length
        this.$store.commit('SET_TASK_LIST_CHANGE', this.taskNumber)
        this.taskList = res.rows
      }
    },
    async riskHIddenData() {
      let res = await riskHiddenDanger({
        prcd: this.reservoirDetail.prcd,
      })
      if (res.success) {
        this.riskHiddenData = res.data
      }
    },
    async dealTask(status, taskId,flag = false) {
      if (status == 0) {
        // 打开弹窗
        let res = await taskApi.deal({
          prcd: this.reservoirDetail.prcd,
          taskId: taskId
        })
        if (res.success) {
          this.$message.success('办理成功！')
          this.getTaskList()
        } else {
          return false;
        }
      } else {
        this.showDealTaskDialog = true
        return false;
      }
    },
    handleCloseTaskDialog() {
      this.showDealTaskDialog = false
    },
  }
}
</script>

<style lang='scss' scoped>
.wrap {
  width: 100%;
  height: 100%;
  .third_title {
    @include flexbox();
    @include flexAI(center);
    // @include flexJC();
    font-weight: bold;
    color: #fff;
    .point {
      width: 20px;
      margin-right: 5px;
    }
    span {
      font-family: "youshe";
      color: #fff;
      font-size: 20px;
      cursor: pointer;
      user-select: none;
      margin-right: 10px;
      &.active {
        font-size: 24px;
        color: #fff;
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
  .page_top {
    .task_list {
      @include flexbox();
      @include flexAI(start);
      @include flexJC(flex-start);
      @include flexflow(row wrap);
      // @include flexJC(space-between);
      padding: 16px 0 0 0;
      height: 175px;
      overflow: auto;
      margin-bottom: 16px;
      .task_item {
        text-align: center;
        box-sizing: border-box;
        padding: 16px 7px 16px 16px;
        width: 250px;
        height: 156px;
        background: url($imageUrl + "task_bg.png") no-repeat
          center;
        background-size: 100% 100%;
        margin-bottom: 16px;
        margin-right: 8px;
        .task_title {
          @include flexbox();
          @include flexAI(center);
          @include flexJC(space-between);
          p {
            @include flexbox();
            @include flexAI(center);
            img {
              width: 20px;
              height: 20px;
              margin-right: 8px;
            }
            span {
              font-size: 20px;
              font-family: youshe;
              color: #fff;
            }
          }
          & > img {
            width: 28px;
          }
        }
        .task_text {
          text-align: left;
          font-size: 16px;
          font-weight: 500;
          color: #ffffff;
          line-height: 22px;
          padding-left: 40px;
          height: 60px;
          margin: 5px 0;
          overflow: auto;
        }
        .task_btn {
          display: inline-block;
          width: 134px;
          height: 35px;
          background: #000;
          font-size: 16px;
          text-align: center;
          line-height: 35px;
          &.no {
            color: #69e3ee;
            background: url($imageUrl + "do-task.png")
              no-repeat center;
            background-size: 100% 100%;
            cursor: pointer;
          }
          &.yes {
            color: #999;
            border: 1px #999 dashed;
            border-radius: 3px;
            background: transparent;
          }
        }
      }
    }
  }
  .page_bottom {
    height: calc(100% - 300px);
    .yh_list {
      height: calc(100% - 20px);
      overflow: auto;
      border: 1px solid #448ef7;
      border-radius: 4px;
      border-top: none;
      padding: 0 16px;
      .yh_item {
        @include flexbox();
        padding: 16px 0;
        border-bottom: 1px solid #448ef7;
        &:last-child {
          border-bottom: none;
        }
        h3 {
          width: 150px;
          img {
            width: 20px;
          }
          span {
            font-size: 20px;
            font-family: youshe;
            line-height: 24px;
            &.red {
              color: #fc4d4d;
            }
            &.yellow {
              color: #f7b500;
            }
          }
        }
        p {
          line-height: 24px;
          span {
            color: #fc4d4d;
          }
        }
      }
    }
    .nodata {
      height:200px;
      // display: flex;
      // justify-content: center;
      // align-items: center;
      position: relative;
      img{
        width: 170px;
        position: absolute;
        left: 0;
        top: 0;
        right: 0;
        bottom: 0;
        margin: auto;
      }
      span{
        width: 70px;
        position: absolute;
        left: 0;
        right: 0;
        bottom: 0;
        margin: auto;
      }
    }
  }
  .dialog-content {
    height: 300px;
    ::v-deep .primary-dialog {
      .el-dialog__body {
        padding: 0;
      }
    }
  }
}
</style>