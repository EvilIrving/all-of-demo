<!-- 可蓄水量-->
<template>
  <div class="sidebar-box" v-loading="loading">
    <div
      class="sidebar-progress-box sidebar-progress-box-yellow"
      v-for="(item, index) in progressBar"
      :key="index"
    >
      <p class="progress-name">{{ item.resName }}</p>
      <p class="progress-num">{{ item.amount }}</p>
      <el-progress type="line" :percentage="item.total" :stroke-width="10" class="progress-bar" :format="format"></el-progress>
    </div>
  </div>
</template>

<script>
import { workApi } from "@/api/dataScreen/floodJudge/index";

export default {
  name: "Sidebar",
  props: {},
  data() {
    return {
      progressBar: [],
      loading: true
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      let res = await workApi.count({
        type: 1
      });
      if (res) {
        let maxNum = 0;
        res.data.map(item => {
          if (item.amount == null) {
            item.amount = 0;
          }
          if (maxNum < item.amount) {
            maxNum = item.amount.toFixed(0);
          }
        });
        res.data.map(item => {
          if (item.amount == 0) {
            item.total = 0;
          } else {
            item.amount = item.amount.toFixed(1);
            item.total = Number(
              ((item.amount / (maxNum * 1.5)) * 100).toFixed(0)
            );
          }
        });
        this.progressBar = res.data;
        this.loading = false;
      }
    },
    format() {
      return "";
    }
  }
};
</script>

<style lang='scss' scoped>
@import "@/style/bigScreenSidebar.scss";
</style>
<style lang="scss">
.sidebar-progress-box.sidebar-progress-box-yellow {
  position: relative;
  .el-progress-bar__inner {
    background-image: linear-gradient(180deg, #f9f200 0%, #e0970a 100%);
    box-shadow: 0 0 8px 0 #289dfb;
    border-radius: 5px;
  }
  .el-progress-bar__outer {
    background: rgba(62, 146, 248, 0.16);
  }
  .el-progress__text {
    font-size: 16px;
    margin-left: 16px;
    color: #12a3df;
  }
}
.progress-num {
  font-size: 0.875rem;
  color: #106ce0;
  position: absolute;
  right: 5px;
  top: 21px;
}
</style>