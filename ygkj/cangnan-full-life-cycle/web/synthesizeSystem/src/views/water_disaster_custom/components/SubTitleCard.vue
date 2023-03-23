<!--
 * @Author: dyn
 * @Date: 2021-07-13 09:23:38
 * @LastEditTime: 2021-07-14 13:56:29
 * @LastEditors: Please set LastEditors
 * @Description: 带有子标题的卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\components\SubTitleCard.vue
-->
<template>
  <div class="sub-title-card">
    <div class="title-box" v-if="title">
      <div>
        <span class="title-label">
          <i class="icon-diamonds"></i>{{ title }}
        </span>
        <slot name="title-operate"> </slot>
      </div>
      <i class="icon-card-title-divide"></i>
    </div>
    <div class="operate-bar">
      <div v-if="tabs && tabs.length > 0" class="operate-tab">
        <span
          v-for="(item, index) in tabs"
          :key="index"
          :class="[index == activeTab && 'active', 'tab-button']"
          @click="handleTabChange(index)"
        >
          {{ item }}
        </span>
      </div>
      <div v-if="dropdown && dropdown.length > 0" class="operate-dropdown">
        <el-select
          v-model="selected"
          placeholder="请选择"
          @change="handleDropdownChange"
        >
          <el-option
            v-for="item in dropdown"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
      </div>
    </div>
    <div class="card-content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "",
  props: {
    title: {
      type: String,
      default: "",
    },
    tabs: {
      type: Array,
      default: () => {
        return [];
      },
    },
    dropdown: {
      type: Array,
      default: () => {
        return [];
      },
    },
  },
  data() {
    return {
      activeTab: 0,
      selected: "",
    };
  },
  components: {},
  created() {},
  mounted() {},
  methods: {
    handleTabChange(index) {
      this.activeTab = index;
      this.$emit("handleTabChange", index);
    },
    handleDropdownChange(val) {
      this.$emit("handleDropdownChange", val);
    },
  },
};
</script>
<style lang="scss" scoped>
.sub-title-card {
  width: 100%;
  height: 100%;
  .title-box {
    > div {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 18px;
      color: #ffffff;
      margin-top: 12px;
    }
    i {
      cursor: pointer;
    }
    .icon-card-title-divide {
      cursor: unset;
      margin-bottom: 4px;
    }
    .title-label i {
      margin-right: 8px;
      vertical-align: middle;
      cursor: unset;
    }
  }
  .operate-bar {
    display: flex;
    justify-content: space-between;
    ::v-deep .el-input__inner {
      height: 24px;
      font-size: 16px;
      background: transparent;
      text-align: right;
    }
    ::v-deep .el-input__icon {
      line-height: unset;
    }
    .el-select {
      width: 120px;
    }
    .operate-tab {
      > span {
        display: inline-block;
        padding: 4px 10px;
        cursor: pointer;
        &.active {
          background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
          border-radius: 12px;
          font-size: 16px;
          color: #ffffff;
        }
      }
    }
  }
  .card-content {
    width: 100%;
    height: calc(100% - 46px - 24px);
  }
}
</style>