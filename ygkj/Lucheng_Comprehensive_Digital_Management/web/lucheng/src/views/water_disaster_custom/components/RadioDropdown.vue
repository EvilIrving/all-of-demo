<template>
  <el-dropdown placement="bottom-start" trigger="click" @visible-change="visibleChange">
    <span :class="['el-dropdown-link', isOpen && 'dropdown-active']">
      <span>{{ calcDisplayLabel }}{{ labelSuffix }}</span>
      <i class="el-icon-caret-bottom" />
    </span>
    <template #dropdown>
      <el-dropdown-menu class="radio-dropdown">
        <el-dropdown-item v-for="item in options" :key="calcValue(item)">
          <el-radio v-model="modelValue" :label="calcValue(item)" @change="onRadioClick"
            >{{ calcLabel(item) }}{{ labelSuffix }}</el-radio
          >
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>
<script>
import { defineComponent } from '@vue/composition-api';
export default defineComponent({
  name: 'RadioDropdown',
  model: { prop: 'value', event: 'input', default: '天气实况' },
  props: {
    value: { require: true, type: [String, Number] },
    options: {
      require: true,
      type: /** @type {import('@vue/composition-api').PropType<any[]>}*/ (Array),
      default: ['天气实况', '气象预警', '气象监测', '风力分布', '降雨1小时预报', '降雨3小时预报', '降雨6小时预报']
    },
    keyOption: {
      type: /** @type {import('@vue/composition-api').PropType<{valueProp:string;labelProp:string}>}*/ (Object)
    },
    labelSuffix: { type: String, default: '' }
  },
  data() {
    return { isOpen: false, modelValue: this.value };
  },
  computed: {
    calcDisplayLabel() {
      if (this.keyOption && Object.keys(this.keyOption).length > 0) {
        const find = this.options.find((item) => item[this.keyOption.valueProp] == this.value);
        return find ? find[this.keyOption.labelProp] : this.value;
      }
      return this.value;
    }
  },
  methods: {
    calcValue(item) {
      if (!this.keyOption || Object.keys(this.keyOption).length == 0) return item;
      return item[this.keyOption.valueProp];
    },
    calcLabel(item) {
      if (!this.keyOption || Object.keys(this.keyOption).length == 0) return item;
      return item[this.keyOption.labelProp];
    },
    visibleChange(v) {
      this.isOpen = v;
    },
    onRadioClick(value) {
      this.$emit('input', value);
      this.$emit('change', value);
    }
  }
});
</script>
<style lang="scss">
.el-dropdown-link {
  display: flex;
  align-items: center;
  i {
    transition: transform 0.1s ease-out;
  }
  &.dropdown-active {
    color: #1cfff5;
    i {
      transform: rotate(-180deg);
    }
  }
}
</style>
<style lang="scss">
.radio-dropdown {
  max-height: 200px;
  overflow: auto;
  background: rgba(2, 10, 28, 0.7);
  border: 1px solid #00BAFF;
  .el-dropdown-menu__item {
    line-height: 26px;
    .el-radio {
      color: #fff;
      .el-radio__input.is-checked .el-radio__inner {
        border-color: #00BAFF;
        background: transparent;
        &::after {
          width: 8px;
          height: 8px;
          background: #00BAFF;
        }
      }
      .el-radio__input.is-checked + .el-radio__label {
        color: #00BAFF;
      }
      .el-radio__inner {
        background: transparent;
        border: 1px solid #fff;
      }
    }
  }

  .el-popper[x-placement^='bottom'] .popper__arrow::after {
    border-bottom-color: rgba(2, 10, 28, 0.7);
  }
}
</style>
