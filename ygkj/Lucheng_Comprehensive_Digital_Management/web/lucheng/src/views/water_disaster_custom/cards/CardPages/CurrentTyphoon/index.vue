<template>
  <div class="current-typhoon">
    <div class="simple-info">
      <div class="simple-info__title">
        <span class="typhoon-name"
          >{{ TyphoonPointData.typhoonInfo.name || "" }}
          {{ TyphoonPointData.typhoonInfo.englishName || "" }}</span
        >
        <span v-if="TyphoonPointData.power" class="typhoon-level">{{
          TyphoonPointData.power
        }}</span>
      </div>
      <div class="simple-info__effect">
        <span
          >预计登陆地点：{{
            TyphoonPointData.land ? eval(TyphoonPointData.land).join(",") : "-"
          }}</span
        >
        <!-- <span class="unit">6</span> -->
      </div>
      <div class="simple-info__action" @click="inPlay = !inPlay">
        <i :class="inPlay ? 'icon-typhoon-pause' : 'icon-typhoon-play'" />
      </div>
    </div>
    <el-tabs v-model="activeTab" type="card" stretch class="current-typhoon">
      <el-tab-pane name="information" label="台风信息">
        <TyphoonInformation
          v-if="activeTab == 'information'"
          :typhoon-data="{ points: currentTyphoon.points, ...TyphoonPointData }"
        />
      </el-tab-pane>
      <el-tab-pane name="effect" label="台风影响">
        <TyphoonEffect
          v-if="activeTab == 'effect'"
          :point-data="TyphoonPointData.point"
          :typhoonName="TyphoonPointData.typhoonInfo.name"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import TyphoonInformation from "./TyphoonInformation/index.vue";
import TyphoonEffect from "./TyphoonEffect/index.vue";
import { mapGetters, mapActions } from "vuex";
export default {
  name: "CurrentTyphoon",
  components: { TyphoonInformation, TyphoonEffect },
  data() {
    return {
      currentTyphoon: {},
      activeTab: "information",
      inPlay: false,
    };
  },
  computed: {
    ...mapGetters(["TyphoonPointData"]),
  },
  methods: {
    ...mapActions([]),
    initTyphoon(data) {
      this.currentTyphoon = data;
    },
  },
};
</script>
<style lang="scss">
.current-typhoon {
  height: calc(100% - 48px);
  .simple-info {
    height: 100px;
    position: relative;
    padding: 24px 3px 0;
    .simple-info__title {
      color: #33ffff;
      .typhoon-name {
        color: #33ffff;
        font-size: 24px;
        line-height: 24px;
      }
      .typhoon-level {
        margin-left: 6px;
        font-size: 24px;
        &::after {
          content: "级";
          font-size: 14px;
        }
      }
    }
    .simple-info__effect {
      margin-top: 3px;
      .unit {
        font-size: 20px;
        color: #00f5ff;
        &::after {
          content: "h";
          font-size: 14px;
          color: #ffffff;
        }
      }
    }
    .simple-info__action {
      cursor: pointer;
      position: absolute;
      left: 295px;
      top: 19px;
    }
  }
}
</style>
