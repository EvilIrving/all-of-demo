<!--
 * @Author: wqn
 * @Date: 2022-09-05 16:41:28
 * @LastEditors: wqn
 * @LastEditTime: 2022-09-05 16:41:28
 * @Description: 预案
-->
<template>
  <div class='chart_reserve_plan'>
    <ul class="list">
      <li v-for="item in reservePlanList" :key="'chart_reserve_plan_' + item.id">
        <img v-if="item.className === 'active'" class="pre_img" src="@/assets/new_images/control_flood_dispatch_chart_reserve_plan_warn_icon.png" alt="">
        <img v-else class="pre_img" src="@/assets/new_images/control_flood_dispatch_chart_reserve_plan_icon.png" alt="">
        <p :class="item.className">{{ item.text }}</p>
        <img class="tail_img" src="@/assets/new_images/control_flood_dispatch_chart_reserve_plan_tail_icon.png" alt="" @click="visible=true">
      </li>
    </ul>
    <PreliminaryPlanDialog v-if="visible" :visible="visible" @close="visible = false" />
  </div>
</template>

<script>
import { getReservePlan } from '@/api/reservePlan';
import PreliminaryPlanDialog from '@/views/ourPujiang/dialog/PreliminaryPlanDialog';
export default {
  name: 'controlFloodDispatchChartReservePlan',
  props: {},
  components: {
    PreliminaryPlanDialog
  },
  data() {
    return {
      reservePlanList: [],
      visible: false
    };
  },
  computed: {},
  watch: {},
  created() {
    this.getReservePlanList();
  },
  mounted() {},
  destroyed() {},
  methods: {
    /**
     * @description 获取预案列表数据
     */
    getReservePlanList() {
      getReservePlan().then((res) => {
        if (res.code == 0) {
          const { historyPlanList, planList, compareToDealLevel } = res.data;
          let cPlanList = this.formatText(planList, compareToDealLevel, true);
          let cHistoryPlanList = this.formatText(historyPlanList);
          this.reservePlanList = cPlanList.concat(cHistoryPlanList);
          if (this.reservePlanList.length > 0) this.reservePlanList[0].className = 'active';
        }
      });
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
          if (flag && compareToDealLevel) item.className = 'active';
          if (item.type == 1) {
            item.text = `${time}，戍浦江流域，24小时暴雨预警超过150mm，请大闸管理人员及时预泄`;
          } else if (item.type == 2) {
            item.text = `${time}，泽雅水库放水，放水量**，请大闸管理人员及时预泄`;
          } else if (item.type == 3) {
            item.text = `${time}，内河水位超过控制水位，请大闸管理人员及时开闸泄水`;
          }
        } else {
          // type(4|5)24h后，醒目的颜色变为正常颜色
          const now = this.$moment(new Date());
          const overtime = this.$moment(new Date(item.overtime));
          const diffMinutes = now.diff(overtime, 'minutes'); //计算相差的分钟数
          const diffHour = Math.floor(diffMinutes / 60); //相差的小时数
          if (diffHour < 24) item.className = 'active';
          if (item.type == 4) {
            item.text = `${time}，内河水位超过控制水位，请大闸管理人员及时关注水位变化`;
          } else if (item.type == 5) {
            item.text = `${time}，外江水位超过内河水位，请大闸管理人员及时关注水位变化`;
          }
        }
      });
      return arr;
    },
    formatTime(time) {
      return this.$moment(new Date(time)).format(time.length > 10 ? 'yyyy年MM月DD日 HH时mm分' : 'yyyy年MM月DD日');
    }
  }
};
</script>
<style lang='scss' scoped>
.chart_reserve_plan {
  width: 100%;
  height: 100%;
  ul.list {
    overflow: auto;
    height: 100%;
    li {
      box-sizing: border-box;
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 33px;
      padding: 0 12px 0 15px;
      border-bottom: 1px solid rgba(50, 104, 118, 0.5);
      background: linear-gradient(90deg, rgba(0, 193, 255, 0.2) 0%, rgba(249, 249, 249, 0) 100%);
      & + li {
        margin-top: 8px;
      }
      .pre_img {
        margin-right: 7px;
      }
      p {
        height: 22px;
        font-size: 15px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #ffffff;
        line-height: 22px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        &.active {
          color: #fcee27;
        }
      }
      .tail_img {
        margin-left: 10px;
      }
    }
  }
}
</style>