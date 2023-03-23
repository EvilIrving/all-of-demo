<template>
  <div class="detail-table">
    <div class="danger-tap">
      <div @click="checkTab(item,index)" :class="{ active: index == tabAction }" v-for="(item, index) in tabList" :key="item.name">
        {{ item.name }}
      </div>
    </div>
    <div class="new_box">
      <component :is="componentValue" :info="info" @close="dialogClose">
      </component>
    </div>
  </div>
</template>
<script>
import CodeSetting from "./CodeSetting";
import PowerSetting from "./PowerSetting";
export default {
  name: "baseForm",
  props: {
    info: Object,
  },
  components: {
    CodeSetting,
    PowerSetting,
  },
  data() {
    return {
      tabList: [
        {
          name: "权重配置",
          value: "PowerSetting",
        },
        {
          name: "二维码配置",
          value: "CodeSetting",
        },
      ],
      tabAction: 0,
      componentValue: "PowerSetting",
    };
  },
  mounted() {
    this.$nextTick(() => { });
  },
  methods: {
    // 关闭
    dialogClose() {
      this.$emit("close");
    },
    checkTab(tab, index) {
      this.tabAction = index;
      this.componentValue = tab.value;
    },
  },
};
</script>
<style lang="scss" scoped>
@import './style.scss';
.danger-tap {
  display: flex;
  height: 32px;
  margin: 15px 0px;
  > div {
    width: 114px;
    height: 32px;
    border: 1px solid rgba(255, 255, 255, 0.6);
    line-height: 32px;
    text-align: center;
    margin-right: 16px;
    color: rgba(255, 255, 255, 0.8);
    cursor: pointer;
    &.active {
      color: #00baff;
      background: rgba(72, 152, 251, 0.1);
      box-shadow: 0px 0px 5px 0px #00baff inset;
      border-color: rgba(0, 186, 255, 0.6);
    }
  }
}
</style>
