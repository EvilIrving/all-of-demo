<!--
 * @Author: wqn
 * @Date: 2022-11-14 13:46:26
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-14 11:27:12
 * @Description: 功能模块组件的容器
-->
<template>
  <div class='module_layout'>
    <div class="title">
      <slot name="title">
        <span @click="() => {!disabled ? $emit('titleClick', title) : ''}" :style="{cursor: disabled ? 'default' : 'pointer'}">
          {{title}}<span class="unit" v-if="unit"> ({{unit}}) </span>
        </span>
      </slot>
      <div class="extra">
        <slot name="extra"></slot>
      </div>
    </div>
    <div class="content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "ModuleLayout",
  props: {
    title: {
      type: String,
      default: "",
    },
    unit: {
      type: String,
      default: "",
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  components: {},
  data() {
    return {};
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  destroyed() {},
  methods: {},
};
</script>
<style lang='scss' scoped>
.module_layout {
  position: relative;
  min-width: 460px;
  width: 100%;
  height: 100%;
  .title {
    position: absolute;
    display: flex;
    justify-content: space-between;
    align-items: center;
    left: 0;
    top: 0;
    right: 0;
    height: 40px;
    background-image: url("./img/title-bg.png");
    background-repeat: no-repeat;
    background-size: 100% 18px;
    background-position: bottom;
    > span {
      margin-left: 40px;
      font-size: 20px;
      font-family: fzzyjw;
      line-height: 48px;
      letter-spacing: 1px;
      .unit {
        font-size: 15px;
        opacity: 0.75;
      }
    }
    .extra {
      display: flex;
      margin-right: 10px;
    }
  }
  .content {
    position: absolute;
    left: 0;
    top: 40px;
    right: 0;
    bottom: 0;
  }
}
</style>