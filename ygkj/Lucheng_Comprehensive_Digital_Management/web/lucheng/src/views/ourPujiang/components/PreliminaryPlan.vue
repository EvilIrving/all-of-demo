<!--
 * @Date: 2022-05-06 16:10:16
 * @Author: 
 * @Description: 戍浦江防洪调度-预案
 * @LastEditors: dyn
 * @LastEditTime: 2022-05-13 17:14:56
 * @FilePath: \lucheng\src\views\ourPujiang\components\PreliminaryPlan.vue
-->
<template>
  <div class="preliminary-plan-card">
    <panel-item title="预案">
      <ul class="bar_box">
        <li v-for="(item, index) in tabList" :key="index" @click="tabChange(index)">
          <i :class="item.className"></i>
          <p :class="item.className" :title="item.text">{{item.text}}</p>
          <i class="has_pic"></i>
        </li>
      </ul>
    </panel-item>
    <PreliminaryPlanDialog v-if="visible" :visible="visible" @close="visible = false" />
  </div>
</template>

<script>
  import { PanelItem } from '@/components';
  import PreliminaryPlanDialog from "../dialog/PreliminaryPlanDialog"
  import { getReservePlan } from "@/api/reservePlan"
  export default {
    name: 'PreliminaryPlan',
    props: {

    },
    components: {
      PanelItem,
      PreliminaryPlanDialog
    },
    data() {
      return {
        visible: false,
        selectedTab: 0,
        tabList: [],
        timer: null,
      };
    },
    mounted() {
      this._getReservePlan();
      this.timer = setTimeout(() => {
        this._getReservePlan();
      }, 1000 * 60 * 5)
    },
    beforeDestroy() {
      if (this.timer) clearTimeout(this.timer)
    },
    methods: {
      tabChange(index) {
        this.selectedTab = index
        this.visible = true;
      },
      async _getReservePlan() {
        const res = await getReservePlan();
        if (res.code == 0) {
          const { historyPlanList, planList, compareToDealLevel } = res.data;
          let cPlanList = this.formatText(planList, compareToDealLevel, true);
          let cHistoryPlanList = this.formatText(historyPlanList);
          this.tabList = cPlanList.concat(cHistoryPlanList)
          if (this.tabList.length > 0) this.tabList[0].className = 'active';
        }
      },
      /**
       * @description: 处理预案显示文字
       * @param {Array} arr 预案列表
       * @param {Boolean} compareToDealLevel 判断当前时间内河水位是否低于死水位，false是小于，true是大于
       * @param {Boolean} flag 是否为新数据列表
       * @return {*}
       */
      formatText(arr, compareToDealLevel, flag = false) {
        arr.map((item) => {
          const time = this.formatTime(item.overtime);
          // type 1.戍浦江暴雨 2.泽雅防水 3.内河超过控制水位 4.预计内河超过控制水位 5.预计外河水位超过内河水位
          if (item.type == 1 || item.type == 2 || item.type == 3) {
            // type(1|2|3)内河水位降到死水位以下，醒目的颜色变为正常颜色；
            if (flag && compareToDealLevel) item.className = 'active'
            if (item.type == 1) {
              item.text = `${time}，戍浦江流域，24小时暴雨预警超过150mm，请大闸管理人员及时预泄`
            } else if (item.type == 2) {
              item.text = `${time}，泽雅水库放水，放水量**，请大闸管理人员及时预泄`
            } else if (item.type == 3) {
              item.text = `${time}，内河水位超过控制水位，请大闸管理人员及时开闸泄水`
            }
          } else {
            // type(4|5)24h后，醒目的颜色变为正常颜色
            const now = this.$moment(new Date());
            const overtime = this.$moment(new Date(item.overtime));
            const diffMinutes = now.diff(overtime, 'minutes');//计算相差的分钟数
            const diffHour = Math.floor(diffMinutes / 60);//相差的小时数
            if (diffHour < 24) item.className = 'active'
            if (item.type == 4) {
              item.text = `${time}，内河水位超过控制水位，请大闸管理人员及时关注水位变化`
            } else if (item.type == 5) {
              item.text = `${time}，外江水位超过内河水位，请大闸管理人员及时关注水位变化`
            }
          }
        })
        return arr
      },
      formatTime(time) {
        return this.$moment(new Date(time)).format(time.length > 10 ? "yyyy年MM月DD日 HH时mm分" : "yyyy年MM月DD日")
      }
    },
  };
</script>

<style scoped lang="scss">
.preliminary-plan-card {
  height: 100%;
}
.bar_box {
  width: 100%;
  height: calc(100% - 12px);
  @include flexbox();
  flex-direction: column;
  overflow-y: auto;
  // 隐藏滚动条  overflow显示设置为auto或者scroll保证内容是可滚动的。
  // scrollbar-width: none; /* firefox */
  // -ms-overflow-style: none; /* IE 10+ */
  // &::-webkit-scrollbar {
  //   display: none; /* Chrome Safari */
  // }
  li {
    flex-shrink: 0;
    flex-grow: 0;
    width: 100%;
    height: 36px;
    background: linear-gradient(90deg, rgba(0, 193, 255, 0.1) 0%, rgba(249, 249, 249, 0) 100%);
    @include flexbox();
    @include flexJC(flex-start);
    @include flexAI(center);
    cursor: pointer;
    border-bottom: 1px solid rgba($color: #326876, $alpha: 0.6);
    margin-bottom: 5px;
    color: #ffffff;
    &:nth-of-type(1) {
      margin-top: 15px;
    }
    &:last-child {
      margin-bottom: 0;
    }
    p {
      width: 80%;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      &.active {
        color: #fcee27;
      }
    }
    i {
      width: 20px;
      height: 20px;
      margin: 0 10px;
      background: url(~assets/images/ourPujiang/electric-light.png) no-repeat;
      background-size: 100% 100%;
      &.active {
        margin: 0 10px;
        background: url(~assets/images/ourPujiang/active-electric-light.png) no-repeat;
        background-size: 100% 100%;
      }
      &.has_pic {
        width: 26px;
        height: 26px;
        margin-left: 30px;
        background: url(~assets/images/ourPujiang/plan-supervision.png) no-repeat;
        background-size: 100% 100%;
      }
    }
  }
}
</style>
