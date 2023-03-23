<template>
  <count-wrap title="泵站" class="pump-station-count">
    <div class="has-count">   
      <div class="has-count-item">
        <p>泵站总数</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{totalNum}}
          </span>
          <span class="unit">个</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>总装机</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{totalInstalled}}
          </span>
          <span class="unit">m³/s</span>
        </p>
      </div>
      <div class="has-count-item">
        <p>最多数量</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{highestNumber}}
          </span>
          
        </p>
      </div>
      <div class="has-count-item">
        <p>最大装机</p>
        <p>
          <span class="num" style="cursor: pointer">
           {{pustName}}{{insFlow}}(m³/s)
          </span>
         <span class="unit"></span>
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
  name: "pumpStationcount",
  data() {
    return {
      abc: "",
      totalNum:"",
      totalInstalled:"",
      pustName:"",
      insFlow:"",
      highestNumber:"",
      largestInstalledEntity:"",
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
        .getcAStatisticalProject({ type: "泵站" })
        .then((res) => {
          if (res.code == 0) {
            console.log(res.data,2222113333)
            this.totalNum = res.data.totalNum;
            this.totalInstalled = res.data.totalInstalled;
            this.highestNumber = res.data.highestNumber;
            this.pustName = res.data.largestInstalledEntity.pustName;
            this.insFlow = res.data.largestInstalledEntity.insFlow;
          }
          // console.log(this.abc, "哎哟woc");
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.pump-station-count {
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
