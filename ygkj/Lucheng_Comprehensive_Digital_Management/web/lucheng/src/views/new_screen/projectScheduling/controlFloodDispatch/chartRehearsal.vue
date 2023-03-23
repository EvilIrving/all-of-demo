<!--
 * @Author: wqn
 * @Date: 2022-09-05 16:41:28
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-23 09:42:55
 * @Description: 预演
-->
<template>
  <div class='chart_rehearsal'>
    <el-input v-model="value" placeholder="请输入" suffix-icon="el-icon-search"></el-input>
    <div class="btn_box">
      <div class="btn" @click="simulationCalculation">模拟预演</div>
      <div class="btn" @click="schemeComparison">方案对比</div>
    </div>
    <div class="rehearsal">
      <template v-if="list.length > 0">
        <div v-for="(item, index) in list" :key="'历史预演方案' + (index + 1)" class="item wrapper">
          <div class="line"></div>
          <div class="inner">
            <img class="ico" src="@/assets/images/ourPujiang/history-rehearsal-icon.png" />
            <div class="con">
              <div class="name">{{item.name}}</div>
              <div class="ext">
                <div class="time">{{item.time}}</div>
                <div class="warn">异常站点：{{item.abnormalSite}}</div>
              </div>
            </div>
            <img class="btn" src="@/assets/images/ourPujiang/rehearsal-button.png" @click="rehearsal(item)" />
          </div>
        </div>
      </template>
      <template v-else>
        <div class="empty">
          <span>{{promptText}}</span>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
export default {
  name: 'controlFloodDispatchChartRehearsal',
  props: {},
  components: {},
  data() {
    return {
      value: '',
      list: [],
      promptText: ''
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getList();
  },
  destroyed() {},
  methods: {
    /**
     * @description 获取历史预演方案
     */
    getList() {
      this.promptText = '加载中...'
      setTimeout(() => {
        this.promptText = '暂无数据'
        this.list = [
          { name: 'XXXXXXX演练名称', time: '2023-02-03 10:00', abnormalSite: 5 },
          { name: 'XXXXXXX演练名称', time: '2023-02-03 10:00', abnormalSite: 5 },
          { name: 'XXXXXXX演练名称', time: '2023-02-03 10:00', abnormalSite: 5 },
        ]
      }, 1000);
    },
    /**
     * @description 模拟预演
     */
    simulationCalculation() {
      this.$emit('simulationRehearsal')
    },
    /**
     * @description 方案对比
     */
    schemeComparison() {
      this.$emit('schemeComparison')
    },
    /**
     * @description 历史预演
     */
    rehearsal(info) {
      this.$emit('historicalRehearsal', info)
    }
  }
};
</script>
<style lang='scss' scoped>
.chart_rehearsal {
  width: 100%;
  height: 100%;
  ::v-deep .el-input {
    width: 186px;
    .el-input__inner {
      height: 28px;
      background: rgba(0,186,255,0.1);
      border-radius: 2px;
      border: 1px solid rgba(0,186,255,0.54);
      text-align: left;
      padding-left: 5px;
      padding-right: 22px;
    }
    .el-input__icon {
      line-height: 28px;
    }
  }
  .btn_box {
    float: right;
    display: flex;
    padding-top: 3px;
    .btn {
      width: 68px;
      height: 22px;
      background: linear-gradient(180deg, #36c0e4 0%, #0639ff 100%);
      border-radius: 2px;
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #ffffff;
      line-height: 22px;
      text-align: center;
      cursor: pointer;
      & + .btn {
        margin-left: 16px;
      }
    }
  }
  .rehearsal {
    position: relative;
    overflow: auto;
    margin-top: 5px;
    height: calc(100% - 30px);
    .item.wrapper{
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      width: 100%;
      height: 55px;
      + .wrapper {
        margin-top: 10px;
      }
      .line {
        width: 100%;
        height: 2px;
        background-image: linear-gradient(to right, #026FE0 63px, rgba(2,111,224,0.43) 63px)
      }
      .inner {
        display: flex;
        align-items: center;
        width: 100%;
        height: 48px;
        background: #03212D;
        border-radius: 2px;
        border: 1px solid #214B9A;
        .ico {
          width: 43px;
          height: 42px;
          margin-left: 3px;
          margin-right: 3px;
        }
        .con {
          display: flex;
          flex-direction: column;
          justify-content: space-evenly;
          flex: 1;
          height: 100%;
          padding-top: 2px;
          .name {
            font-size: 16px;
            font-weight: 400;
            color: #FFFFFF;
          }
          .ext {
            display: flex;
            .time {
              width: 160px;
              font-size: 12px;
              font-weight: 400;
              color: #FFFFFF;
            }
            .warn {
              font-size: 12px;
              font-weight: 400;
              color: #FCEE27;
            }
          }
        }
        .btn {
          width: 70px;
          height: 22px;
          margin-right: 10px;
        }
      }
    }
    .empty {
      position: absolute;
      left: 0;
      right: 0;
      margin: auto;
      width: 226px;
      height: 197px;
      background-image: url(~@/assets/images/ourPujiang/empty-icon.png);
      background-size: 100% 100%;
      text-align: center;
      > span {
        display: inline-block;
        font-size: 14px;
        font-weight: 400;
        color: rgba(255, 255, 255, 0.8);
        margin-top: 18px;
      }
    }
  }
}
</style>