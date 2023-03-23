<template>
  <count-wrap title="堤防" class="dike-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>堤防总数</p>
        <p>
          <span class="num" style="cursor: pointer"> {{totalNum}} </span>
          <span class="unit">条</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>总长度</p>
        <p>
          <span class="num" style="cursor: pointer"> {{dikeLen}} </span>
          <span class="unit">km</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>最多数量</p>
        <p>
          <span class="num" style="cursor: pointer"> {{highestNumber}} </span>
        </p>
      </div>
      <div class="has-count-item">
        <p>最少数量</p>
        <p>
          <span class="num" style="cursor: pointer"> {{miniNumber}} </span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
import { mapGetters } from "vuex";

export default {
  name: "dikeCount",
  data() {
    return {
      abc: "",
      totalNum:"",
      dikeLen:"",
      highestNumber:"",
      miniNumber:"",
      timer: null,
    };
  },
  computed: {
    ...mapGetters(["dikeCount"]),
  },
  components: { CountWrap },
  mounted() {
    this.getcAStatisticalProject();
    this.timer = setInterval(() => {
      this.getcAStatisticalProject();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },

  // getcAStatisticalProject
  methods: {
    async getcAStatisticalProject() {
      await disasterPreventionApi
        .getcAStatisticalProject({ type: "堤防" })
        .then((res) => {
          if (res.code == 0) {
            this.totalNum = res.data.totalNum;
            this.dikeLen = res.data.dikeLen;
            this.highestNumber = res.data.highestNumber;
            this.miniNumber = res.data.miniNumber;
          }
          // console.log(this.abc, "哎哟woc");
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.dike-count {
  .has-count {
    display: flex;
    justify-content: space-around;
    align-items: center;
    width: 100%;
    height: 100%;
    .has-count-item {
      & > p {
        font-size: 20px;
        text-align: center;
        margin-bottom: 3px;
      }
      .num {
        font-size: 24px;
        color: #1df8f3;
      }
      .unit {
        font-size: 16px;
      }
    }
  }
}
</style>
