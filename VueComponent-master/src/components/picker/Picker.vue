<template>
  <popup v-model="visible" :close="close" :popup-height="popupHeight">
    <div id="picker">
      <div
        class="picker-top"
        :style="{ height: `${topHeight}px`, lineHeight: `${topHeight}px` }"
      >
        <div class="top-cancel" @click.self="close">取消</div>
        <div v-if="showTitle" class="top-title">{{ title }}</div>
        <div class="top-comfirm" @click.self="onConfirm">确定</div>
      </div>
      <div class="picker-content">
        <picker-slot
          v-for="(sItem, index) in currentList"
          :key="index"
          v-model="valueList[index]"
          :slotMap="sItem"
          :itemHeight="itemHeight"
          :rows="rows"
          :isLink="isLink"
          @input="inputFn"
        ></picker-slot>
        <div class="mask"></div>
      </div>
    </div>
  </popup>
</template>
<script>
import Popup from "../Popup.vue";
import PickerSlot from "./PickerSlot.vue";

import { PICKER } from "../../assets/js/const";
const { TITLE, P, C, R, ZERO, PROVINCES, CITYS, REGIONS } = PICKER;
export default {
  name: "Picker",
  props: {
    value: {
      type: Array,
      default: () => {},
    },
    close: {
      type: Function,
      default: () => {},
    },
    visible: {
      type: Boolean,
      default: () => false,
    },
    showTitle: {
      type: Boolean,
      default: () => false,
    },
  },
  data: () => {
    return {
      isLink: true,
      rows: 7,
      itemHeight: 34,
      topHeight: 44,
      currentList: [],
      valueList: [],
    };
  },
  computed: {
    // 弹框高度
    popupHeight() {
      return this.rows * this.itemHeight + this.topHeight;
    },
    // 标题
    title() {
      return TITLE.slice(0, this.valueLen).join("/");
    },
    valueLen() {
      return this.value.length;
    },
  },
  created() {
    // valueLen非整数且小于等于0
    if (!Number.isInteger(this.valueLen) && this.valueLen <= 0) return;

    // 处理省份，城市，地区数据
    const proviceMap = new Map();
    const cityMap = new Map();
    const regionMap = new Map();
    this.handleProvinces(proviceMap, cityMap, regionMap);
    proviceMap.size && this.currentList.push(proviceMap);
    cityMap.size && this.currentList.push(cityMap);
    regionMap.size && this.currentList.push(regionMap);

    // 获取valueList
    for (let i = 0; i < this.valueLen; i++) {
      const parent = i === 0 ? ZERO : this.value[i - 1];
      const valuetem = this.currentList[i]
        .get(parent)
        .find((item) => item.value === this.value[i]);
      this.valueList.push(valuetem);
    }
  },
  methods: {
    // input事件
    inputFn(val) {
      // 计算startIndex值
      let startIndex = 0;
      const parent = val.parent;
      startIndex = this.valueList.findIndex((item) => item.parent === parent);
      if (startIndex === -1) return;
      for (let i = startIndex; i < this.valueList.length; i++) {
        // 替换对应索引的valueList值
        if (i === startIndex) {
          this.valueList[startIndex] = val;
        } else {
          // 根据parent获取余下valueList
          const parent = this.valueList[i - 1].value;
          this.valueList[i] = this.currentList[i].get(parent)[0];
        }
      }
    },
    // 保存
    onConfirm() {
      this.$emit("confirm", this.valueList);
      this.close();
    },
    // 处理省份
    handleProvinces(proviceMap, cityMap, regionMap) {
      const provicesList = [];
      for (let i = 0; i < PROVINCES.length; i++) {
        provicesList.push({
          name: PROVINCES[i],
          parent: ZERO,
          value: `${PROVINCES[i]}${P}`,
        });
        this.handleCitys(cityMap, regionMap, i);
      }
      proviceMap.set(ZERO, provicesList);
    },
    // 处理城市
    handleCitys(cityMap, regionMap, i) {
      if (this.valueLen < 2) return;
      const cityList = [];
      for (let j = 0; j < CITYS[i].length; j++) {
        cityList.push({
          name: CITYS[i][j],
          parent: `${PROVINCES[i]}${P}`,
          value: `${CITYS[i][j]}${C}`,
        });
        this.handleRegions(regionMap, i, j);
      }
      cityMap.set(`${PROVINCES[i]}${P}`, cityList);
    },
    // 处理地区
    handleRegions(regionMap, i, j) {
      if (this.value < 3) return;
      const regionsList = [];
      for (let k = 0; k < REGIONS[i][j].length; k++) {
        regionsList.push({
          name: REGIONS[i][j][k],
          parent: `${CITYS[i][j]}${C}`,
          value: `${REGIONS[i][j][k]}${R}`,
        });
      }
      regionMap.set(`${CITYS[i][j]}${C}`, regionsList);
    },
  },
  components: {
    Popup,
    PickerSlot,
  },
};
</script>

<style lang="scss" scoped>
#picker {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  border-radius: $px-6 $px-6 0 0;
  .picker-top {
    display: flex;
    justify-content: space-between;
    height: $px-40;
    font-size: $px-14;
    line-height: $px-40;
    color: $c-primary;
    background-color: $c-body;
    padding: 0 $px-10;
    .top-title {
      flex: 1;
      color: $c-title;
      font-size: $px-16;
      text-align: center;
    }
  }
  .picker-content {
    position: relative;
    display: flex;
    flex: 1;
    height: inherit;
    padding: 0 $px-10;

    .mask {
      position: absolute;
      top: 0;
      right: auto;
      bottom: auto;
      left: 0;
      width: 100%;
      height: 100%;
      padding: 0 $px-10;
      box-sizing: border-box;
      background-clip: content-box;
      background-image: linear-gradient(
          to top,
          rgba(255, 255, 255, 0.95),
          rgba(255, 255, 255, 0.6) calc(50% - 17px),
          $c-division calc(50% - 17px) calc(50% - 16px),
          transparent calc(50% - 16px)
        ),
        linear-gradient(
          to bottom,
          rgba(255, 255, 255, 0.95),
          rgba(255, 255, 255, 0.6) calc(50% - 17px),
          $c-division calc(50% - 17px) calc(50% - 16px),
          transparent calc(50% - 16px)
        );
    }
  }
}
</style>