<template>
  <div class="legend-box">
    <i v-for="i in 4" :key="i"></i>
    <div class="legend-title" @click="legendShowType = !legendShowType">
      <span>图例</span>
      <span class="legend-arrow" :class="{'rotate-arrow': legendShowType}"></span>
    </div>
    <div class="legend-list" v-show="legendShowType">
      <div class="legend-list-item" v-for="item in legendList" :key="item.title">
        <div class="item-name">{{ item.title }}</div>
        <div class="item-status">
          <div class="item-status-div" v-for="(ite, index) in item.status" :key="index">
            <div :style="{ 'background-color': ite.color }"></div>
            <div>{{ ite.name }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
export default {
  computed: {
    ...mapGetters(['legendList'])
  },
  data() {
    return {
      legendShowType: false
    };
  },
  mounted() {
    console.log(this.legendList);
  }
};
</script>

<style lang="scss" scoped>
.legend-box {
  position: absolute;
  left: 41px;
  bottom: 40px;
  width: 284px;
  z-index: 88;
  border: 1px solid #00c1ff;
  background: linear-gradient(180deg, rgba(6, 29, 54, 0.7) 0%, #051832 100%);
  box-shadow: inset 0px 4px 30px 0px rgba(35, 81, 239, 0.25);
  z-index: 3;
  > i {
    position: absolute;
    width: 18px;
    height: 16px;
    border-image: linear-gradient(180deg, rgba(53, 185, 227, 1), rgba(96, 138, 255, 1)) 3 3;
    &:nth-child(1) {
      left: -3px;
      top: -3px;
      border-left: 3px solid;
      border-top: 3px solid;
    }
    &:nth-child(2) {
      top: -3px;
      right: -3px;
      border-top: 3px solid;
      border-right: 3px solid;
    }
    &:nth-child(3) {
      right: -3px;
      bottom: -3px;
      border-right: 3px solid;
      border-bottom: 3px solid;
    }
    &:nth-child(4) {
      bottom: -3px;
      left: -3px;
      border-bottom: 3px solid;
      border-left: 3px solid;
    }
  }
  .legend-title {
    width: 100%;
    height: 46px;
    background: linear-gradient(270deg, rgba(12, 61, 54, 0.02) 0%, rgba(7, 157, 254, 0.4) 100%);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16px;
    color: #fff;
    font-size: 16px;
    .legend-arrow {
      width: 13px;
      height: 9px;
      background-image: url(~assets/new_images/projectScheduling/legend-arrow.png);
      background-size: 100% 100%;
      background-repeat: no-repeat;
      &.rotate-arrow{
        transform: rotate(180deg);
      }
    }
  }
  .legend-list {
    padding: 0 16px 14px;
    .legend-list-item {
      padding-top: 14px;
      .item-name {
        font-weight: 500;
        color: #00baff;
        font-size: 14px;
      }
      .item-status {
        padding-top: 8px;
        display: flex;
        flex-wrap: wrap;
        .item-status-div {
          display: flex;
          align-items: center;
          margin-right: 35px;
          padding-bottom: 8px;
          &:last-child {
            margin-right: 0px;
          }
          &:nth-child(3n) {
            margin-right: 0px;
          }
          > div:nth-child(2) {
            color: #ffffff;
            font-size: 14px;
            padding-left: 8px;
          }
          > div:nth-child(1) {
            width: 12px;
            height: 5px;
          }
        }
      }
    }
  }
}
</style>