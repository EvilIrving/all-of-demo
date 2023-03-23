<template>
  <count-wrap title="山塘" class="mountain-pond-count">
    <div class="has-count">   
      <div class="has-count-item">
        <p>山塘总数</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{totalNum}}
          </span>
          <span class="unit">个</span>
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
        <p>主要用途</p>
        <p>
          <span class="num" style="cursor: pointer">
            {{mainPurpose}}
          </span>
         
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
  name: "MountainPondcount",
 data() {
    return {
      abc: "",
      totalNum:"",
      mainPurpose:"",
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
        .getcAStatisticalProject({ type: "山塘" })
        .then((res) => {
          if (res.code == 0) {
            this.totalNum = res.data.totalNum;
            // this.dikeLen = res.data.dikeLen;
            this.highestNumber = res.data.highestNumber;
            this.mainPurpose = res.data.mainPurpose;
          }
          // console.log(this.abc, "哎哟woc");
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.mountain-pond-count {
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
        padding: 0.25rem 0;
      }
      .num {
        font-size: 24px;
        color: #1df8f3;
      }
    }
  }
}
</style>
