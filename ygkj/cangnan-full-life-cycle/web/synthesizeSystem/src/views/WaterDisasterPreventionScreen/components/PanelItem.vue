<template>
  <div class="panel-item">
    <div class="panel-item__title">
      <div class="panel-item__title--light" />
      <div v-if="title.icon" class="panel-item__title--icon">
        <i :class="title.icon" />
      </div>
      <div class="panel-item__title--label">
        <span
          class="label-text"
          @click="handleTitleClick"
          :style="{ cursor: to ? 'pointer' : 'unset' }"
        >
          <slot name="title-label">{{ title || title.text }}</slot>
        </span>
        <span v-if="inSimulation" class="in-simulation">演练中</span>
        <span class="title-description">
          <slot name="label-description"> </slot>
        </span>
      </div>
      <div v-if="tabs" class="panel-item__title--tabs">
        <span
          v-for="(item, index) in tabs"
          :key="index"
          :class="[index == activeTab && 'active', 'tab-button']"
          @click="handleTabChange(index)"
        >
          {{ item }}
        </span>
      </div>
      <div v-if="dropdown" class="panel-item__title--select">
        <el-select v-model="selected" placeholder="请选择">
          <el-option
            v-for="(item, index) in dropdown"
            :key="index"
            :label="item"
            :value="item"
          />
        </el-select>
      </div>
      <div v-if="hasSwitch || doubleSwitch" class="panel-item__title--switch">
        <slot name="title-switch">
          <el-switch v-model="value1" @change="handleSwitchChange(1)" />
          <el-switch
            v-if="doubleSwitch"
            v-model="value2"
            @change="handleSwitchChange(2)"
          />
        </slot>
      </div>
      <div v-if="count != null" class="panel-item__title--info">
        总数
        <span class="count-num">{{ count }}</span>
        个
      </div>
      <div v-if="custom" class="panel-item__title--custom">
        <slot name="custom"></slot>
      </div>
    </div>
    <div class="panel-item__content">
      <slot name="content-tabs">
        <div v-if="actionTabs" class="panel-item__content--tabs">
          <span
            v-for="(item, index) in actionTabs"
            :key="index"
            :class="[index == activeActionTab && 'active', 'tab-button']"
            @click="handleActionTabChange(index)"
          >
            {{ item }}
          </span>
        </div>
      </slot>
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "PanelItem",
  props: {
    title: {
      type: [Object, String],
      default: () => {
        return { text: "标题" };
      },
    },
    to: { type: [String, Object] },
    dropdown: { type: Array },
    tabs: {
      type: Array,
    },
    hasSwitch: {
      type: Boolean,
      default: false,
    },
    doubleSwitch: {
      type: Boolean,
      default: false,
    },
    count: {
      type: Number,
    },
    actionTabs: {
      type: Array,
    },
    custom: { type: Boolean, default: false },
    inSimulation: { type: Boolean, default: false },
  },
  data() {
    return {
      activeTab: 0,
      activeActionTab: 0,
      value1: false,
      value2: false,
      selected: "",
    };
  },
  methods: {
    /**
     * 处理tab切换并将事件冒泡
     */
    handleTabChange(index) {
      this.activeTab = index;
      this.$emit("change", index, this.tabs[index]);
    },
    handleSwitchChange(index) {
      this.$emit("change", index, this[`value${index}`]);
    },
    handleActionTabChange(index) {
      this.activeActionTab = index;
      this.$emit("change", index, this.actionTabs[index]);
    },
    /**
     * 标题部分点击事件
     */
    handleTitleClick() {
      if (this.to && this.to.link) {
        window.open(this.to.link);
      } else if (this.to) {
        this.$router.push(this.to);
      }
      this.$emit("tclick");
    },
  },
};
</script>

<style lang="scss">
.panel-item {
  display: flex;
  flex-direction: column;
  padding-bottom: 16px;
  .panel-item__title {
    // width: 370px;
    height: 35px;
    background: url(~assets/images/WaterDisastersImage/title-bg_lightless.png)
      no-repeat;
    background-size: 100% 100%;
    background-position-x: 18px;
    display: flex;
    align-items: center;
    color: #fff;
    .panel-item__title--light {
      height: 32px;
      width: 40px;
      background: url(~assets/images/WaterDisastersImage/title-bg_light.gif)
        no-repeat;
      background-size: 100% 100%;
      margin-right: 18px;
    }
    .panel-item__title--label {
      .label-text {
        user-select: none;
        font-size: 24px;
        font-weight: 600;
        margin-right: 10px;
      }
      .title-description {
        font-size: 12px;
        color: #fff;
      }
      .in-simulation {
        font-size: 20px;
        color: #1cfff5;
        background-color: #08516c;
        // padding: 4px;
        border-radius: 2px;
      }
    }
    .panel-item__title--select {
      ::v-deep .el-select {
        width: 50%;
        .el-input__inner {
          font-size: 12px;
          background: transparent;
        }
      }
    }
    .panel-item__title--switch {
      margin-left: auto;
      // margin-right: 16px;
      > i {
        cursor: pointer;
      }
    }
    .panel-item__title--info {
      margin-left: auto;
      margin-right: 8px;
      color: #00c1ff;
    }
  }
  .panel-item__content--tabs,
  .panel-item__title--tabs {
    .tab-button {
      cursor: pointer;
      user-select: none;
      height: 24px;
      line-height: 24px;
      padding: 0 5px;
      border-radius: 13px;
      color: #fff;
      font-size: 20px;
      &.active {
        padding: 2px 7px;
        // background: #00c1ff;
        background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
      }
    }
  }
  .panel-item__content--tabs {
    margin-top: 10px;
  }
  .panel-item__content {
    flex: 2;
    .el-carousel__indicators {
      .el-carousel__indicator {
        &.is-active button {
          background-color: #1ef8f2;
        }
      }
    }
    .el-carousel__button {
      padding: 0;
      width: 9px;
      height: 9px;
      border-radius: 50%;
    }
  }
  .count-num {
    color: #00f5ff;
    font-size: 20px;
    font-weight: normal;
  }
  .panel-item-subtitle {
    color: #fff;
    font-size: 16px;
    line-height: 32px;
    position: relative;
    display: flex;
    align-items: center;
    > span {
      flex: 2;
    }
    &:before {
      content: "";
      display: inline-block;
      height: 24px;
      width: 24px;
      background-image: url(~assets/images/WaterDisastersImage/diamond.gif);
    }
  }
}
</style>
