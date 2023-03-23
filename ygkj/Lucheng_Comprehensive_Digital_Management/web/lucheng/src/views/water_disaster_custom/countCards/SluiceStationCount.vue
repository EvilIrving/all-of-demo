<template>
  <count-wrap title="闸站" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>闸站总数</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ statisticObj.gateNum }}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>总装机容量</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{ statisticObj.totalInsPow }}
          </span>
          <span class="unit">m³/s</span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { hydraulicApi } from "@/api/hydraulic_amap";
import { mapGetters } from "vuex";

export default {
  name: "SluiceStationCount",
  data() {
    return {
      statisticObj: {
        gateNum: "",
        totalInsPow: "",
      },
      timer: null,

    };
  },
  components: { CountWrap },
  computed: {
    ...mapGetters(["WaterSupplyCount"]),
  },
  created() { },
  mounted() {
    this.getStats();
    this.timer = setInterval(() => {
      this.getStats();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    async getStats() {
      const opt = {
        type: '闸站'
      }
      await hydraulicApi.stats4ProjectList(opt).then(res => {
        if (res.code == 0) {
          this.statisticObj = res.data;
        }
      })
    },
  },
};
</script>
<style lang="scss" scoped>
.stagnant-water-count {
  .has-count {
    display: flex;
    justify-content: space-around;
    align-items: center;
    width: 100%;
    height: 100%;
    .has-count-item {
      display: flex;
      align-items: baseline;
      & > p {
        font-size: 24px;
        text-align: center;
        margin-right: 10px;
      }
      .num {
        font-size: 32px;
        color: #1df8f3;
      }
    }
  }
}
</style>
