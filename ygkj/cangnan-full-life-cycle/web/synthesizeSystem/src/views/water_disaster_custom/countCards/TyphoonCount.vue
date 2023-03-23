<template>
  <count-wrap :title="typhoonTitle" class="typhoon-count">
    <el-row class="has-count">
      <el-col :span="12" class="fs24">
        <p>
          距离温州<span class="orange ml12 mr12 fs36">{{ typhoonData.distance }}</span
          >km
        </p>
      </el-col>
      <el-col :span="12" class="fs24">
        <p>
          受影响工程<span class="yellow fs36 ml12 mr12">{{ typhoonData.projectNum }}</span
          >个
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import { getTyphoonStat4C } from "@/api/typhoon.js";
import CountWrap from "../components/CountWrap.vue";
import { mapGetters } from "vuex";
import moment from "moment";

export default {
  name: "TyphoonCount",
  components: { CountWrap },
  data() {
    return { typhoonData: {
    } };
  },
  computed: {
    ...mapGetters(["TyphoonPointData"]),
    typhoonTitle() {
      let typhoonInfo = this.TyphoonPointData?.typhoonInfo || {};
      return `${typhoonInfo.name || this.typhoonData.name || "-"} ${
        typhoonInfo.englishName || this.typhoonData.englishName || "-"
      } ${typhoonInfo.landPower || this.typhoonData.power || "-"}级`;
    },
  },
  watch: {
    TyphoonPointData: {
      immediate: true,
      handler(val) {
        let opt = {};
        if (Object.keys(val).length > 0) {
          let { move_speed, pressure, speed, point, ...rest } = {
            ...val,
            ...val.point,
          };
          opt = { ...rest, time: moment(top.time).format("yyyy-MM-DD HH:mm:ss") };
        }
        getTyphoonStat4C(opt).then((res) => {
          if (res.code == 0) {
            this.typhoonData = res.data;
          }
        });
      },
    },
  },
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.typhoon-count .has-count {
  height: 100%;
  .el-col {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
}
</style>
