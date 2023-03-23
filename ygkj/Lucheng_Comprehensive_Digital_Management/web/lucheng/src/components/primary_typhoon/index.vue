<template>
  <div class="typhoon-box">
    <div class="typhoon-symbol">
      <div v-for="(item, index) in typhoonList" :key="index">
        <span
          class="typhoon-circle"
          :class="activeCard == index ? 'active' : ''"
        />
        <span v-if="index != typhoonList.length - 1" class="typhoon-line" />
      </div>
    </div>
    <ul class="typhoon-card">
      <li
        :class="['card-item', activeCard == index ? 'active' : '']"
        v-for="(item, index) in typhoonList"
        :key="index"
        @click="typhoonCardClick(index)"
      >
        <div class="card-item__time">
          {{ item.time }}
        </div>
        <div class="card-item__info">
          <div class="card-item__info-item">
            <span class="text">气压:</span>
            <span class="num">{{ item.pressure }} 百帕</span>
          </div>
          <div class="card-item__info-item">
            <span class="text">风力:</span>
            <span class="num">{{ item.power }} 级</span>
          </div>
          <div class="card-item__info-item">
            <span class="text">移速:</span>
            <span class="num">{{ item.speed }} m/s</span>
          </div>
        </div>
        <div class="card-item__distance">
          距离温州:
          <span class="distance-label">{{ item.distance }}</span>
          km
        </div>
      </li>
    </ul>
  </div>
</template>
<script>
export default {
  name: "Typhoon",
  components: {},
  props: {
    typhoonList: Array,
  },
  data() {
    return {
      activeCard: 0,
    };
  },
  methods: {
    typhoonCardClick(val) {
      this.activeCard = val;
      this.$emit("typhoonCardClick", val);
    },
  },
};
</script>
<style lang="scss" scoped>
.typhoon-box {
  position: relative;
  .typhoon-symbol {
    width: 26px;
    position: absolute;
    top: 35px;
    > div {
      width: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }
    .typhoon-circle {
      display: inline-block;
      width: 8px;
      height: 8px;
      border-radius: 50%;
      border: 1px solid #3e92f8;
      &.active {
        height: 16px;
        width: 16px;
        &::before {
          content: "";
          position: absolute;
          width: 10px;
          height: 10px;
          background-color: #3e92f8;
          border-radius: 50%;
          transform: translate(2px, 2px);
        }
      }
    }
    .typhoon-line {
      display: inline-block;
      height: 108px;
      width: 2px;
      opacity: 0.32;
      background: #000000;
    }
  }

  .typhoon-card {
    width: calc(100% - 26px);
    margin-left: 26px;
    .card-item {
      cursor: pointer;

      display: flex;
      justify-content: space-between;
      flex-direction: column;

      width: 100%;
      height: 100px;
      color: #fff;
      background: #00172c4f;
      border: 1px solid #4898fb;
      border-radius: 4px;
      margin-bottom: 16px;
      padding: 16px 8px;

      font-size: 14px;
      &:last-child {
        margin-bottom: 0;
      }
      &.active {
        background: rgba(71, 230, 244, 0.2);
        border: 1px solid #47e6f4;
      }

      .card-item__info {
        padding-top: 3px;
        margin: 8px 0;
        display: flex;
        justify-content: space-between;
        .card-item__info-item {
          &:last-child {
            margin-right: 16px;
          }
        }
      }
      .card-item__distance {
        border-top: 1px solid #ffffff55;
        padding-top: 8px;
      }
      .distance-label {
        font-size: 16px;
        margin: 0 8px;
      }
    }
  }
}
</style>