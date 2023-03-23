<!--
 * @Author: dyn
 * @Date: 2020-11-30 13:41:51
 * @LastEditTime: 2020-11-30 13:53:08
 * @LastEditors: Please set LastEditors
 * @Description: 地图右侧信息中tab
 * @FilePath: \pingHuWaterPlatform\src\components\primary_map_tab\index.vue
-->
<template>
  <div class="primary-map-tab">
    <p v-if="tabName">{{ tabName }}</p>
    <div class="tab-box" :style="{ 'margin-top': tabName ? '16px' : 0 }">
      <!-- 单选 -->
      <template v-if="type == 'single'">
        <span
          v-for="(item, index) in tabArr"
          :key="index"
          @click="tabChange(index)"
          :class="[selectedTabIndex === index ? 'active' : '', 'tab-item']"
        >
          {{ item.label }}
        </span>
      </template>
      <!-- 多选 -->
      <template v-if="type == 'multiple'">
        <span
          v-for="(item, index) in tabArr"
          :key="index"
          @click="multabChange(index)"
          :class="[
            multiSelectedList.indexOf(index) != -1 ? 'active' : '',
            'tab-item',
          ]"
        >
          {{ item.label }}
        </span>
      </template>
    </div>
  </div>
</template>

<script>
export default {
  name: "PrimaryMapTab",
  props: {
    tabArr: Array,
    tabName: String,
    type: {
      type: String,
      default: "single",
    },
  },
  data() {
    return {
      selectedTabIndex: 0,
      multiSelectedList: [],
    };
  },
  components: {},
  computed: {},
  mounted() {},
  watch: {},
  methods: {
    tabChange(index) {
      this.selectedTabIndex = index;
      this.$emit("tabChange", index);
    },
    multabChange(index) {
      const idx = this.multiSelectedList.indexOf(index);
      if (idx == -1) {
        this.multiSelectedList.push(index);
      } else {
        this.multiSelectedList.splice(idx, 1);
      }
      this.$emit("tabChange", this.multiSelectedList);
    },
  },
};
</script>

<style lang='scss' scoped>
$tabHeight: 24px;
.primary-map-tab {
  margin: 16px 0;
  .tab-box {
    display: flex;
    height: $tabHeight;
    line-height: $tabHeight;
    flex-wrap: wrap;
  }
  > p {
    font-size: 14px;
    color: #ffffff;
  }
  .tab-item {
    display: inline-block;
    height: 100%;
    line-height: $tabHeight;
    text-align: center;
    padding: 0 16px;
    font-size: 14px;
    color: #fff;
    cursor: pointer;
    margin-right: 10px;
    &:last-child {
      margin-right: 0;
    }
    &.active {
      background: #2C6FB7;
      // background-image: linear-gradient(180deg, #71c1a9 1%, #07bbb9 100%);
      border-radius: 4px;
    }
  }
}
</style>