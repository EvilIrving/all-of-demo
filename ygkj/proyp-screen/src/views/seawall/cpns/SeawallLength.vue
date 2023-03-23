<!--
 * @Author: wqn
 * @Date: 2023-02-14 11:42:01
 * @LastEditors: dtb
 * @LastEditTime: 2023-02-15 14:26:53
 * @Description: 海塘长度
-->
<template>
  <ModuleLayout title="海塘长度" unit="km" disabled>
    <div class='seawall-length'>
      <single-date-bar-chart v-loading="htglsLoading" class="leftChart" :chartData="htglsData" />
    </div>
  </ModuleLayout>
</template>

<script>
import SingleDateBarChart from '../components/SingleDateBarChart.vue'
import { zhSeawall } from "@/api/api_seawall";
export default {
  name: "SeawallLength",
  props: {},
  components: {
    SingleDateBarChart
  },
  data() {
    return {
      htglsData: {
        title: "海塘长度",
        id: "htglsChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "公里",
        allowClick: false,
        isReset: false,
      },
      htglsLoading: true,
    };
  },
  computed: {
    selectAdcds() {
      return this.$store.state.selectAdcd;
    },
  },
  watch: {
    selectAdcds() {
      this.getHtglsData()
    },
  },
  created() { },
  mounted() {
    this.getHtglsData()
  },
  destroyed() { },
  methods: {
    // 海塘公里数统计
    async getHtglsData() {
      let xData = [];
      let yData = [];
      let adcdList = [];
      let res = await zhSeawall.adcdLocation({
        adcd: this.selectAdcds,
        userAdcd: this.$localData("get", "userInfo").adcd,
      });
      if (res.success && res.data.length) {
        for (let i = 0; i < res.data.length; i++) {
          if (res.data[i].adnm.length > 2) {
            xData.push(
              res.data[i].adnm.substring(0, res.data[i].adnm.length - 1)
            );
          } else {
            xData.push(res.data[i].adnm);
          }
          yData.push((res.data[i].length / 1000).toFixed(0));
          adcdList.push(res.data[i].adcd);
        }
        this.htglsData.isReset = !this.selectAdcds.endsWith("0000000000")
        this.htglsData.xData = xData;
        this.htglsData.yData = yData;
        this.htglsData.adcdList = adcdList;
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.htglsLoading = false;
    },

  },
};
</script>
<style lang='scss' scoped>
.seawall-length {
  width: 96%;
  margin: 0 auto;
  height: 100%;
}
</style>