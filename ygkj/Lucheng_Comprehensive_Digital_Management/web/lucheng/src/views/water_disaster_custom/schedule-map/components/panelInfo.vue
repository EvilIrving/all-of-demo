<!--
 * @Autor: ysl
 * @Date: 2022-03-30 09:44:11
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-27 23:32:53
 * @Description: 侧边栏信息
 * @FilePath: \lucheng\src\views\water_disaster_custom\schedule-map\components\panelInfo.vue
-->
<template>
  <div class="panel-info">
    <div style="color: #fff; display: flex; align-items: center; font-size: 14px">
      显示：
      <el-checkbox-group v-model="activeArr" @change="handleCheckedCitiesChange">
        <el-checkbox v-for="(item, index) in checkedList" :label="item" :key="index">{{ item }}</el-checkbox>
      </el-checkbox-group>
    </div>
    <div class="count-box">
      <div @click="showNumber(1)">
        水库报警<span>{{ count.skWarn.length }}</span
        >座
      </div>
      <div @click="showNumber(2)">
        水闸开启<span>{{ count.szWarn.length }}</span
        >个
      </div>
    </div>
    <ScheduleLegend class="panel-legend" :showTitle="false" />
  </div>
</template>
<script>
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index.js';
import ScheduleLegend from './legends.vue';
export default {
  name: 'panelInfo',
  components: { ScheduleLegend },
  model: { prop: 'checked', event: 'changeChecked' },
  props: {
    checked: Array,
    count: {
      type: Object,
      default: () => {
        return {
          skWarn: [],
          szWarn: []
        };
      }
    }
  },
  data() {
    return {
      iconMap: {
        名称: 'icon-general-name',
        水位: 'icon-general-waterlevel',
        流量: 'icon-general-flow'
      },
      checkedList: ['名称', '水位', '流量'],
      activeArr: [],
      skStatusType: false,
      szStatusType: false
    };
  },
  watch: {
    checked: {
      handler: function (val) {
        this.activeArr = val || [];
      },
      deep: true
    }
  },
  mounted() {
    this.activeArr = this.checked || [];
  },
  methods: {
    handleCheckedCitiesChange(val) {
      this.$emit('changeChecked', val);
    },
    showNumber(num) {
      if (num == 0) {
        if (this.count.skWarn.length > 0) {
          this.skStatusType = !this.skStatusType;
          this.$emit('showNumber', num, this.skStatusType);
        }
      } else {
        if (this.count.szWarn.length > 0) {
          this.szStatusType = !this.szStatusType;
          this.$emit('showNumber', num, this.szStatusType);
        }
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.panel-info {
  width: 372px;
  color: #fff;
  ::v-deep .el-checkbox-group {
    .el-checkbox {
      color: #fff;
    }
    .el-checkbox__input {
      .el-checkbox__inner {
        background-color: transparent;
        border-color: #fff;
      }
      &.is-checked {
        .el-checkbox__inner {
          background-color: transparent;
          border-color: #23e3fb;
          &::after {
            border-color: #23e3fb;
          }
        }
      }
    }
    .el-checkbox__input.is-checked + .el-checkbox__label {
      color: #23e3fb;
    }
  }
  .count-box {
    display: flex;
    align-items: center;
    margin: 24px 0 20px;
    padding-bottom: 20px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.5);
    > div {
      font-size: 16px;
      margin-right: 50px;
      cursor: pointer;
      span {
        font-size: 32px;
        padding: 0 6px 0 12px;
        color: #eec80b;
      }
      &:last-child {
        margin-right: 0;
        span {
          color: #23e3fb;
        }
      }
    }
    p {
      margin-top: 8px;
      text-align: center;
    }
    .fs32 {
      font-size: 32px;
      margin-right: 4px;
    }
    .blue {
      color: #1bf6ee;
    }
    .yellow {
      color: #eec80b;
    }
  }
  .setting {
    height: 100%;
    margin: 0 16px 0 8px;
    display: flex;
    align-items: center;
    // padding-left: 16px;
    > span {
      margin: 0 8px;
      i {
        margin-right: 4px;
      }
    }
    ::v-deep.el-input-number {
      width: 72px;

      .el-input-number__decrease,
      .el-input-number__increase {
        background: transparent;
        width: 32px;
        > i {
          color: #fff;
        }
      }
      .el-input {
        .el-input__inner {
          border: 1px solid #fff;
          padding-left: 5px;
          padding-right: 36px;
        }
      }
    }
  }
  .h-count {
    height: 100%;
    cursor: pointer;
    .warn-item_content {
      height: 100%;
    }
    &.active {
      .warn-item_content {
        background: #1bf6ee65;
      }
      $color: #1bf6ee;
      background: linear-gradient(to bottom, $color, $color) left top no-repeat,
        linear-gradient(to bottom, $color, $color) left top no-repeat,
        linear-gradient(to bottom, $color, $color) right top no-repeat,
        linear-gradient(to bottom, $color, $color) right top no-repeat,
        linear-gradient(to bottom, $color, $color) left bottom no-repeat,
        linear-gradient(to bottom, $color, $color) left bottom no-repeat,
        linear-gradient(to bottom, $color, $color) right bottom no-repeat,
        linear-gradient(to bottom, $color, $color) right bottom no-repeat;
      background-size: 12px 2px, 2px 12px;
      border-radius: 3px;
    }
  }
  .panel-legend {
    position: relative;
    width: 100%;
    bottom: 0;
    right: 0;
    ::v-deep .schedule-legend__wrap {
      &::after {
        background: none;
      }
      .schedule-legend__content {
        border: none;
        background: transparent;
        padding: 0;
        .schedule-legend__item {
          padding-right: 0;
        }
      }
    }
  }
}
.panel-tab {
  span {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 32px;
  }
  .icon-basin {
    display: inline-block;
    width: 18px;
    height: 18px;
    background: url(~assets/images/icon-basin.png) no-repeat;
    background-size: 18px 18px;
    margin-right: 8px;
    &.active {
      background-image: url(~assets/images/icon-basin-active.png);
    }
  }
  ::v-deep &.el-tabs .el-tabs__header .el-tabs__nav {
    border: none;
    .el-tabs__item {
      color: #fff;
      opacity: 0.8;
      border: 1px solid #ffffff;
      line-height: 36px;
      height: 36px;
      font-size: 16px;
      &.is-active {
        opacity: 1;
        color: #56fefe;
        box-shadow: inset 0 0 5px 0 #56fefe;
        background: #4898fb1a;
        border: 1px solid #56fefe;
      }
    }
  }
}
</style>