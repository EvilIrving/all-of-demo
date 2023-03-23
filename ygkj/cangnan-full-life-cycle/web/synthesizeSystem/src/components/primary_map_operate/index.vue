<template>
  <div class="operate-wrap">
    <!-- 工具 -->
    <div class="tool">
      <div>
        <i class="icon-lywg" style="vertical-align: middle"></i>
        <el-dropdown trigger="click">
          <span class="el-dropdown-link">
            流域网格<i class="el-icon-caret-top el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>流域网格</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <!-- <div>
        <el-dropdown trigger="click">
          <span class="el-dropdown-link">
            温州市<i class="el-icon-caret-top el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>温州市</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div> -->
      <div>
        <i class="icon-operate-box" style="vertical-align: middle"></i>
        <el-dropdown trigger="click" @command="mapToolBar">
          <span class="el-dropdown-link">
            工具箱<i class="el-icon-caret-top el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="length">测距</el-dropdown-item>
            <el-dropdown-item command="area">测面</el-dropdown-item>
            <el-dropdown-item command="none">清除测量</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!-- 缩放 -->
    <div class="enlarge zoom-change" @click="zoomChange('add')">+</div>
    <div class="narrow zoom-change" @click="zoomChange('reduce')">-</div>

    <!-- 底图切换 -->
    <!-- <div class="switch">
      <i class="icon-change" style="vertical-align: middle"></i>
      底图切换
    </div> -->
  </div>
</template>
<script>
import Measure from "../../utils/measure";
export default {
  name: "PrimaryMapOperate",
  props: {},
  data() {
    return {};
  },
  mounted() { },
  methods: {
    mapToolBar(command) {
      // this.$message('click on item ' + command);
      // alert(command)
      this.$emit("measure", command);
    },
    zoomChange(type) {
      this.$emit("zoomChange", type);
    },
  },
};
</script>
<style lang="scss">
.operate-wrap {
  display: flex;
  > div {
    height: 100%;
    background: rgba(0, 0, 0, 0.4);
    margin-left: 16px;
    cursor: pointer;
    text-align: center;
    i {
      margin-right: 6px;
    }
  }
  .tool {
    display: flex;
    padding: 8px 24px;
    line-height: 24px;
    margin: 0;
    > div:first-child {
      border-right: 1px solid #fff;
      padding-right: 12px;
      // margin-right: 18px;
    }
    > div:last-child {
      border-left: 1px solid #fff;
      padding-left: 18px;
      // margin-left: 12px;
    }
  }
  .zoom-change {
    width: 40px;
    font-size: 28px;
  }
  .switch {
    padding: 0 22px;
  }
}
</style>
<style lang="scss">
.el-dropdown {
  font-size: 16px;
  color: #fff;
  vertical-align: middle;
}
.el-dropdown-menu__item:focus,
.el-dropdown-menu__item:not(.is-disabled):hover {
  background-color: rgba(38, 118, 113, 0.26);
  color: #267671;
}
</style>