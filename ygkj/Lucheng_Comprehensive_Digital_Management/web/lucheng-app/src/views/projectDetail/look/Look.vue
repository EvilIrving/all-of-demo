<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-09 10:05:43
 * @Description: 督
 * @FilePath: /cangnan-app/src/views/projectDetail/governor/Governor.vue
-->
<template>
  <div class="wrap">
    <div class="detail-menu">
      <div
        :class="['detail-menu-item', menuIndex === index ? 'active' : '']"
        v-for="(item, index) in tabs"
        :key="item.name"
        @click="clickMenu(index)"
      >
        {{ item.title }}
      </div>
    </div>
    <tide v-if="menuIndex === 0" :type="type"></tide>
    <water v-if="menuIndex === 1" :type="type"></water>
    <displacement v-if="menuIndex === 2" :type="type"></displacement>
    <settlement v-if="menuIndex === 3" :type="type"></settlement>
  </div>
</template>

<script>
import Tide from "./components/Tide.vue";
import Water from "./components/Water.vue";
import Displacement from "./components/Displacement.vue";
import Settlement from "./components/Settlement.vue";
export default {
  name: "",
  props: {},
  components: { Water, Tide, Displacement, Settlement },
  data() {
    return {
      menuIndex: 0,
      tabs: [
        {
          title: "潮位",
          name: "tide",
        },
        {
          title: "雨量",
          name: "rain",
        },
        {
          title: "位移",
          name: "displacement",
        },
        {
          title: "沉降",
          name: "settlement",
        },
      ],
      type: 1,
    };
  },
  mounted() {},
  methods: {
    clickMenu(index) {
      this.menuIndex = index;
      this.type = index + 1;
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  width: 100%;
  .detail-menu {
    @include flexbox();
    overflow-x: auto;
    overflow-y: hidden;
    padding: 12px;
    box-sizing: border-box;
    &::-webkit-scrollbar {
      display: none;
    }
    .detail-menu-item {
      white-space: nowrap;
      min-width: 80px;
      height: 30px;
      font-size: 16px;
      color: #74767a;
      text-align: center;
      line-height: 30px;
      font-weight: 500;
      background: #eff3fe;
      border-radius: 4px;
      margin-right: 10px;
      &.active {
        background: #eff3fe;
        color: #036eff;
      }

      .detail-menu-item {
        white-space: nowrap;
        min-width: 96px;
        height: 34px;
        font-size: 16px;
        color: #74767a;
        text-align: center;
        line-height: 32px;
        font-weight: 500;
        background: #f6f6f6;
        border-radius: 4px;
        margin-right: 16px;

        &.active {
          background: #eff3fe;
          color: #036eff;
        }

        &:last-child {
          margin-right: 0;
        }
      }
    }
  }
}
</style>
