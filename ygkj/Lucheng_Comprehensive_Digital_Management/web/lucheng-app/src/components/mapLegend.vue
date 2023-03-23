<template>
  <div
    class="map-legend-box"
    :class="{ 'old-wrap-legend': addRouteType === 'older' }"
  >
    <img
      class="legend-icon"
      v-show="handerChangeLegendType"
      @click="handerChangeLegendType = !handerChangeLegendType"
      src="../assets/images/legend-icon.png"
      alt=""
    />
    <div
      class="legend-box"
      v-show="!handerChangeLegendType"
      @click="handerChangeLegendType = !handerChangeLegendType"
    >
      <div class="legend-title">
        <img src="../assets/images/legend-title-icon.png" alt="" />
        <span>图例</span>
      </div>
      <div class="legent-list">
        <div class="legent-item" v-for="item in legendList" :key="item.color">
          <span
            class="span-icon"
            :style="`background-color: ${item.color}`"
          ></span>
          <span class="span-text">{{ item.label }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  props: {
    legendList: Array,
  },
  computed: {
    ...mapGetters(["addRouteType"]),
  },
  data() {
    return {
      handerChangeLegendType: false,
    };
  },
};
</script>

<style lang="scss" scoped>
.map-legend-box {
  .legend-icon {
    width: 44px;
  }
  .legend-box {
    background: #ffffff;
    box-shadow: 0px 2px 8px 0px rgba(0, 0, 0, 0.15);
    border-radius: 4px;
    min-width: 78px;
    .legend-title {
      height: 20px;
      background: #f9f9f9;
      display: flex;
      align-items: center;
      padding-left: 8px;
      position: relative;
      ::after {
        position: absolute;
        top: 50%;
        right: 11px;
        margin-top: -1.5px;
        border: 3px solid;
        border-color: transparent transparent #818181 #818181;
        -webkit-transform: rotate(135deg);
        transform: rotate(135deg);
        // opacity: 0.8;
        content: "";
      }
      img {
        width: 14px;
        margin-right: 3px;
      }
      span {
        font-size: 12px;
        font-weight: 500;
        color: #666666;
      }
    }
    .legent-list {
      padding: 4px 0 4px 8px;
      .legent-item {
        padding-bottom: 4px;
        display: flex;
        align-items: center;
        &:last-child {
          padding-bottom: 0;
        }
        .span-icon {
          width: 7px;
          height: 7px;
          border-radius: 50%;
          margin-right: 6px;
          position: relative;
          top: -1px;
        }
        .span-text {
          color: #666666;
          line-height: 22px;
          font-size: 12px;
        }
      }
    }
  }
  &.old-wrap-legend {
    .legend-icon {
      width: 52px;
    }
    .legend-box {
      background: #ffffff;
      box-shadow: 0px 2px 8px 0px rgba(0, 0, 0, 0.15);
      border-radius: 4px;
      min-width: 94px;
      .legend-title {
        background: #f9f9f9;
        display: flex;
        align-items: center;
        padding: 3px 0 3px 8px;
        position: relative;
        ::after {
          position: absolute;
          top: 50%;
          right: 7px;
          margin-top: -1.5px;
          border: 5px solid;
          border-color: transparent transparent #818181 #818181;
          -webkit-transform: rotate(135deg);
          transform: rotate(135deg);
          content: "";
        }
        img {
          width: 20px;
          margin-right: 3px;
        }
        span {
          font-size: 20px;
          font-weight: 500;
          color: #666666;
        }
      }
      .legent-list {
        padding: 4px 0 4px 8px;
        .legent-item {
          padding-bottom: 4px;
          display: flex;
          align-items: center;
          &:last-child {
            padding-bottom: 0;
          }
          .span-icon {
            width: 7px;
            height: 7px;
            border-radius: 50%;
            margin-right: 6px;
            position: relative;
            top: -1px;
          }
          .span-text {
            color: #666666;
            line-height: 22px;
            font-size: 18px;
          }
        }
      }
    }
  }
}
</style>