<!-- 实时通讯 -->
<template>
  <div class="sidebar-box">
    <div class="person-box">
      <person-card :list="personList"></person-card>
    </div>
  </div>
</template>

<script>
import { rainApi } from "@/api/dataScreen/floodJudge/index";
import PersonCard from "./PersonCard";

export default {
  name: "Sidebar",
  props: {},
  components: { PersonCard },
  data() {
    return {
      name: "",
      personList: [
        // {
        //   realName: "王晓辉",
        //   job: "办事处主任",
        //   phone: "132-4567-8952",
        //   dept: "温州市水利局",
        // },
      ],
    };
  },
  mounted() {},
  methods: {
    async getList() {
      let res = await rainApi.realRainFall({
        adnm: this.name,
      });
      if (res) {
        res.data.map((item) => {
          if (item.drpSum == null) {
            item.highestLevel = "-";
          }
        });
      }
    },
  },
};
</script>

<style lang='scss' scoped>
@import "@/style/bigScreenSidebar.scss";
.person-box {
  height: calc(100% - 90px);
  overflow-y: auto;
}
</style>
<style lang="scss">
.sidebar-box .el-loading-mask {
  background-color: transparent;
}
</style>