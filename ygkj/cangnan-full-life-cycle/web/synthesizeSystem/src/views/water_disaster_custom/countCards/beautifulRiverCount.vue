<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: 美丽河湖 C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="美丽河湖" class="stagnant-water-count">
    <el-carousel
      trigger="click"
      arrow="never"
      :autoplay="false"
      :interval="interval"
    >
      <el-carousel-item class="carousel-item-content">
        <div class="has-count">
          <div class="has-count-item">
            <p>美丽河湖建设总体形象进度</p>
            <p>
              <span class="num">
                {{ count.beautLakeProgress }}
              </span>
              <span class="unit">%</span>
            </p>
          </div>
          <div class="has-count-item">
            <p>中小河流治理总体形象进度</p>
            <p>
              <span class="num">
                {{ count.smallMediumRiverNumProgress }}
              </span>
              <span class="unit">%</span>
            </p>
          </div>
        </div>
      </el-carousel-item>

      <el-carousel-item class="carousel-item-content">
        <div class="has-count">
          <div class="has-count-item">
            <p>水美乡镇建设总体形象进度</p>
            <p>
              <span class="num">
                {{ count.beautVillageNumProgress }}
              </span>
              <span class="unit">%</span>
            </p>
          </div>
          <div class="has-count-item">
            <p>农村池塘整治总体形象进度</p>
            <p>
              <span class="num">
                {{ count.ruralPondNumProgress }}
              </span>
              <span class="unit">%</span>
            </p>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

export default {
  name: "FourChaosCount",
  data() {
    return {
      interval: 5000,
      count: {},
    };
  },
  components: { CountWrap },
  computed: {},
  created() {},
  mounted() {
    this.projectStatic();
  },
  methods: {
    async projectStatic() {
      await waterResourceApi.projectStatic().then((res) => {
        this.count = res.data;
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.stagnant-water-count {
  .el-carousel {
    height: 100%;
  }
  ::v-deep .el-carousel__container {
    height: 100%;
  }
  ::v-deep .el-carousel__button {
    width: 0.5625rem;
    height: 0.5625rem;
    border-radius: 50%;
  }
  ::v-deep .el-carousel__indicator.is-active button {
    background-color: #1ef8f2;
  }
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
      p:nth-child(2) {
        font-size: 32px;
        margin-top: 10px;
      }
      .num {
        font-size: 26px;
        // font-weight: bold;
        color: #1ef8f2;
      }
      .unit {
        font-size: 20px;
      }
    }
  }
}
</style>
