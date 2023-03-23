<!--
 * @Date: 2022-06-13 13:48:29
 * @Author: 
 * @Description: 工作台
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-08-12 17:30:50
 * @FilePath: \lucheng-app\src\views\appView\workbench\Workbench.vue
-->
<template>
  <!--工作台-->
  <div class="workbench_wrap">
    <van-nav-bar
      fixed
      placeholder
      safe-area-inset-top
      :title="$route.meta.title"
      :left-arrow="$store.state.viewType !== 'irs'"
      @click-left="$router.go(-1)"
    />
    <img class="bannner" src="../../../assets/Personalworkbench.png" alt="" />
    <div class="content">
      <van-grid :column-num="4" :border="false" style="width: 100%">
        <!-- 1 报警 2 提醒 -->
        <van-grid-item @click="goLink('/callpolice?type=1')">
          <span v-show="policeNum.alarmNum" class="badge">{{
            policeNum.alarmNum
          }}</span>
          <img :src="imgsrc.callPolice" />
          <span>报警</span>
        </van-grid-item>
        <van-grid-item @click="goLink('/callpolice?type=2')">
          <span v-show="policeNum.remindNum" class="badge">{{
            policeNum.remindNum
          }}</span>
          <img :src="imgsrc.caution" />
          <span>提醒</span>
        </van-grid-item>
        <van-grid-item @click="goLink('/propertySupervision')">
          <span v-show="policeNum.overseeNum" class="badge">{{
            policeNum.overseeNum
          }}</span>
          <img :src="imgsrc.supvivse" />
          <span>督办</span>
        </van-grid-item>
        <van-grid-item @click="goLink('/approve')">
          <span v-show="policeNum.approvalNum" class="badge">{{
            policeNum.approvalNum
          }}</span>
          <img :src="imgsrc.approve" />
          <span>审批</span>
        </van-grid-item>
      </van-grid>
    </div>
  </div>
</template>

<script>
import { localData } from "@/plugins/storage";
import { policeMessage } from "@/api/projectDetail.js";
export default {
  name: "Workbench",
  props: {},
  components: {},
  data() {
    return {
      imgsrc: {
        approve: require("@/assets/approve.png"),
        supvivse: require("@/assets/supvivse.png"),
        callPolice: require("@/assets/callPolice.png"),
        caution: require("@/assets/caution.png"),
      },
      form: {},
      policeNum: {},
    };
  },
  mounted() {
    this.addZwlog(this)
    this.getList();
  },
  methods: {
    goLink(path) {
      this.$router.push(path);
    },
    async getList() {
      let res = await policeMessage.AlarmNum();
      if (res.code === 0) {
        this.policeNum = res.data;
      }
    },
  },
};
</script>

<style scoped lang="scss">
.workbench_wrap {
  width: 100%;
  height: calc(100% - 30px);
  margin: 10px auto;
  background-color: #fff;
  .bannner {
    display: block;
    width: 94%;
    margin: 0 auto 20px;
  }
  .content {
    width: 94%;
    height: calc(100% - 250px);
    margin: 10px auto;
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 200;
    color: #666666;
    line-height: 16px;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    background: #ffffff;
    box-shadow: 0px 0px 20px 0px rgba(39, 106, 240, 0.1);
    border-radius: 4px;
    img {
      width: 30px;
      height: 30px;
      margin-bottom: 8px;
    }
  }
}
</style>
<style lang="scss">
.workbench_wrap {
  .content {
    .van-grid {
      justify-content: space-between;
      .van-grid-item {
        height: 20%;
        padding-right: 0 !important;
      }
    }
    .van-grid-item__content {
      width: 80px;
      background: #fff;
      border-radius: 5px;
    }
  }
}
</style>
