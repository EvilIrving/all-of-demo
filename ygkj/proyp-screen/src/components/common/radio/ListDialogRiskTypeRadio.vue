<!--
 * @Author: wqn
 * @Date: 2023-02-15 14:26:42
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-15 14:38:07
 * @Description: 管控清单弹窗内的风险类型单选框
-->
<template>
  <ul class='list-dialog-risk-type-radio'>
    <li v-for="item in riskType" :key="'risk-type-radio-' + item.value" @click="changeRiskType(item.value)" :class="{active: value === item.value}">
      {{item.label}}
    </li>
  </ul>
</template>

<script>
export default {
  name: "ListDialogRiskTypeRadio",
  props: {
    value: {
      type: String,
      default: "",
    },
  },
  components: {},
  data() {
    return {
      riskType: [
        { value: "", label: "全部" },
        { value: "1", label: "高坝" },
        { value: "2", label: "屋顶" },
        { value: "3", label: "普通" },
      ],
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  destroyed() {},
  methods: {
    changeRiskType(value) {
      this.$emit("input", value);
    },
  },
};
</script>
<style lang='scss' scoped>
ul.list-dialog-risk-type-radio {
  display: flex;
  align-items: center;
  li {
    width: 78px;
    height: 32px;
    border-radius: 6px;
    border: 1px solid #486e95;
    line-height: 30px;
    text-align: center;
    color: #b0d9e8;
    box-shadow: 0 0 15px 0 inset #1c415b;
    cursor: pointer;
    + li {
      margin-left: 23px;
    }
    &.active {
      border-color: #8ccbff;
    }
  }
}
</style>