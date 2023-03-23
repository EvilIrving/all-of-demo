<template>
  <div class="layer-wrap">
    <p class="layer-title" @click="handleActive">
      <span>图层控制</span>
      <i :class="active ? 'el-icon-arrow-down' : 'el-icon-arrow-up'"></i>
    </p>
    <div v-show="active">
      <slot></slot>
    </div>
  </div>
</template>
<script>
export default {
  name: "PrimaryLayerControl",
  props: {
    title: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      active: true,
    };
  },
  watch: {},
  components: {},
  mounted() {},
  methods: {
    handleActive() {
      this.active = !this.active;
    },
  },
};
</script>

<style lang="scss" scoped>
$titleHeight: 52px;
$itemHeight: 30px;
.layer-wrap {
  width: 100%;
  ul {
    width: 100%;
    padding: 8px 20px;
    background: rgba(38, 118, 113, 0.24);
    border: 1px solid #3ab378;
    border-top: 0;
    box-shadow: inset 0 0 24px 0 #267671;
    color: #fff;
    line-height: $itemHeight;
    overflow-y: auto;
    border-radius: 0 0 4px 4px;
    li {
      font-size: 16px;
    }
  }
  .layer-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    height: $titleHeight;
    padding: 0 20px;
    color: rgba(255, 255, 255, 0.88);
    font-size: 20px;
    padding: 0 18px;
    background: #267671;
    box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.5);
    border-radius: 4px 4px 0 0;
    cursor: pointer;
  }
}
</style>
<style lang="scss">
.el-checkbox__input.is-checked .el-checkbox__inner,
.el-checkbox__input.is-indeterminate .el-checkbox__inner {
  background-color: transparent;
  border-color: #fff;
  color: #fff;
}
.el-checkbox__inner:hover {
  border-color: #fff;
}
.el-checkbox__input.is-focus .el-checkbox__inner,
.el-checkbox__input.is-checked + .el-checkbox__label {
  color: #fff;
}
.el-checkbox,
.el-checkbox__label {
  color: #fff;
  font-size: 16px;
}
.layer-wrap {
  ul {
    li {
      display: flex;
      position: relative;
      align-items: center;
      .icon-wrap {
        width: 30px;
        height: 30px;
        position: absolute;
        left: 16px;
        display: flex;
        justify-content: center;
        align-items: center;
      }
      .el-checkbox__label {
        margin-left: 24px;
      }
    }
  }
}
</style>