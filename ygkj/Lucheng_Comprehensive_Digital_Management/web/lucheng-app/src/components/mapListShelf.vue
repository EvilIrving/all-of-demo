<!--
 * @Date: 2022-06-01 09:26:38
 * @Author: 
 * @Description: 进入{{type}}工程的首页
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-12-19 15:36:39
 * @FilePath: \lucheng-app\src\components\mapListShelf.vue
-->
<template>
  <div
    class="map-list-wrap"
    :class="{ 'old-wrap-list': addRouteType === 'older' }"
  >
    <div class="base-map">
      <slot name="map"></slot>
    </div>
    <div class="list-map" :class="{ 'top-list-map': !upDownType }">
      <div class="bottm_info">
        <div class="slot-info">
          <slot></slot>
        </div>
        <div class="bottm_icon" @click="checkUpDownType">
          <i class="map_mode"></i>
          <i class="up_flod" v-if="upDownType"></i>
          <i class="down_flod" v-else></i>
        </div>
      </div>
      <div class="table-list-map" ref="table">
        <slot name="table"></slot>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "mapListShelf",
  data() {
    return {
      upDownType: true,
      tableListType: true,
    };
  },
  computed: {
    ...mapGetters(["addRouteType"]),
  },
  methods: {
    checkUpDownType() {
      if (!this.upDownType) {
        setTimeout(() => {
          this.$refs.table.scrollTop = 0;
        }, 600);
      }
      this.upDownType = !this.upDownType;
    },
  },
};
</script>

<style scoped lang="scss">
.map-list-wrap {
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: relative;
  .base-map {
    height: calc(100% - 120px);
    width: 100%;
    position: relative;
    .map-legend {
      z-index: 133;
      position: absolute;
      left: 12px;
      bottom: 12px;
    }
  }
  .list-map {
    width: 100%;
    height: 100%;
    position: absolute;
    top: calc(100% - 120px);
    transition: all 0.5s linear;
    z-index: 233;
    background-color: #ffffff;
    &.top-list-map {
      top: 0;
      transition: all 0.5s linear;
    }
    .bottm_info {
      height: 55px;
      width: 100%;
      box-sizing: border-box;
      padding: 0px 10px;
      @include flexbox();
      @include flexJC(space-between);
      @include flexAI(center);
      .slot-info {
        width: calc(100% - 54px);
      }
      .bottm_icon {
        width: 54px;
        @include flexbox();
        @include flexJC(space-around);
        .map_mode {
          justify-self: end;
          width: 16px;
          height: 16px;
          background: url("../assets/map_mode.png") no-repeat center;
          background-size: 80%;
        }
        .up_flod {
          justify-self: end;
          width: 16px;
          height: 16px;
          background: url("../assets/up_flod.png") no-repeat center;
          background-size: 80%;
        }
        .down_flod {
          justify-self: end;
          width: 16px;
          height: 16px;
          background: url("../assets/down_flod.png") no-repeat center;
          background-size: 80%;
        }
      }
    }
    .table-list-map {
      height: calc(100% - 120px);
      overflow: auto;
    }
  }
  &.old-wrap-list {
    .base-map {
      height: calc(100% - 55px);
      width: 100%;
      position: relative;
      .map-legend {
        z-index: 133;
        position: absolute;
        left: 12px;
        bottom: 12px;
      }
    }
    .list-map {
      width: 100%;
      height: 100%;
      position: absolute;
      top: calc(100% - 55px);
      transition: all 0.5s linear;
      z-index: 233;
      background-color: #ffffff;
      &.top-list-map {
        top: 0;
        transition: all 0.5s linear;
      }
      .bottm_info {
        height: 55px;
        width: 100%;
        box-sizing: border-box;
        padding: 0px 10px;
        @include flexbox();
        @include flexJC(space-between);
        @include flexAI(center);
        .slot-info {
          width: calc(100% - 54px);
        }
        .bottm_icon {
          width: 54px;
          @include flexbox();
          @include flexJC(space-around);
          .map_mode {
            justify-self: end;
            width: 16px;
            height: 16px;
            background: url("../assets/map_mode.png") no-repeat center;
            background-size: 80%;
          }
          .up_flod {
            justify-self: end;
            width: 16px;
            height: 16px;
            background: url("../assets/up_flod.png") no-repeat center;
            background-size: 80%;
          }
          .down_flod {
            justify-self: end;
            width: 16px;
            height: 16px;
            background: url("../assets/down_flod.png") no-repeat center;
            background-size: 80%;
          }
        }
      }
      .table-list-map {
        height: calc(100% - 80px);
        overflow: auto;
      }
    }
  }
}
</style>