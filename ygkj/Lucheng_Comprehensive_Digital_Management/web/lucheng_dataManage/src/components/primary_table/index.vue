<!--
 * @Author: zg
 * @LastEditTime: 2022-03-24 16:13:52
 * @LastEditors: Please set LastEditors
 * @Descripttion: 
 * @FilePath: \web-data-center\src\components\primary_table\index.vue
-->
<template>
  <div class="wrap">
    <div class="table_title" v-if="title">
      <p>{{ title }}</p>
    </div>
    <div class="table_head clearfix" v-if="searchShow">
      <slot name="condition"></slot>
      <div class="search_item" v-if="!hideInput">
        <span>{{ searchText }}：</span>
        <el-input
          placeholder="请输入..."
          v-model="searchName"
          clearable
          @change="searchAction()"
        />
      </div>
      <span class="_btn" v-if="!hideInput" @click="searchAction()">搜 索</span>
      <span class="_btn" v-if="!hideAdd" @click="addAction()">新 增</span>
      <slot name="operateBtn"> </slot>
    </div>
    <div class="table_content">
      <slot></slot>
    </div>
  </div>
</template>
<script>
export default {
  name: "PrimaryTable",
  props: {
    searchShow: {
      type: Boolean,
      default: true,
    },
    hideInput: {
      type: Boolean,
      default: false,
    },
    hideAdd: {
      type: Boolean,
      default: false,
    },
    searchText: {
      // 查询输入框label
      type: String,
      defaulr: "",
    },
    title: {
      // 表格标题
      type: String,
      defaulr: "",
    },
  },
  data() {
    return {
      searchName: "",
      tipsText: "",
      activeType: 0,
    };
  },
  watch: {
    tips: function (val) {
      this.tipsText = val;
    },
  },
  components: {},
  mounted() {
    if (this.tips) this.tipsText = this.tips;
  },
  methods: {
    addAction() {
      this.$emit("addAction");
    },
    searchAction() {
      this.$emit("searchAction", this.searchName);
    },
    handleActiveType(val) {
      this.activeType = val;
    },
    reset() {
      this.searchName = "";
    },
  },
};
</script>

<style lang="scss" scoped>
$operateHeight: 32px;
$operateWidth: 160px;
$operateFs: 14px;
$borderColor: #d9d9d9;
$borderRadius: 4px;
$operateBtnColor: #1492ff;
$operateBtnWidth: 65px;
$operateDelBtnColor: #f1924e;
.wrap {
  color: #333333;
  font-size: $operateFs;
  background: #ffffff;
  // box-shadow: 0 0 2px 0 rgba(14, 31, 53, 0.12),
  //   0 1px 4px 0 rgba(14, 31, 53, 0.06);
  // border-radius: 4px;
  padding: 0;
  overflow-y: auto;
  .table_title {
    width: 100%;
    height: 56px;
    padding: 16px 10px;
    border-bottom: 1px solid #eeeeee;
    p {
      font-size: 18px;
      font-weight: 600;
      color: #333;
      line-height: 20px;
      padding-left: 10px;
      border-left: 4px solid #1492ff;
    }
  }
  .table_head {
    @include flexbox();
    @include flexJC(flex-start);
    color: #666666;
    padding: 0 24px;
    margin: 16px 0;
    .search_item {
      @include flexbox();
      @include flexAC();
      margin-right: 24px;
      ::v-deep .el-select,
      ::v-deep .el-input {
        width: unset;
        height: $operateHeight;
        .el-input__inner {
          width: $operateWidth;
          height: 100%;
          background: transparent;
          border: 1px solid $borderColor;
          border-radius: $borderRadius;
          font-size: $operateFs;
          color: #333;
        }
        .el-input__icon {
          line-height: $operateHeight;
        }
      }
      ::v-deep .el-date-editor.el-input__inner {
        width: unset;
        height: $operateHeight;
        background: transparent;
        border: 1px solid $borderColor;
        border-radius: $borderRadius;
        font-size: $operateFs;
        color: #333;
      }
      ::v-deep .el-date-editor .el-input__icon {
        line-height: 24px;
      }
      ::v-deep .el-date-editor .el-range-separator {
        line-height: 24px;
      }
    }
    ._btn {
      height: $operateHeight;
      background: $operateBtnColor;
      color: #ffffff;
      border-radius: $borderRadius;
      text-align: center;
      line-height: $operateHeight;
      cursor: pointer;
      padding: 0 16px;
      margin-right: 24px;
      border: 1px solid $operateBtnColor;
      &.btn_export {
        border: 1px solid $operateBtnColor;
        color: $operateBtnColor;
        background: transparent;
      }
      &.btn_del {
        border: 1px solid $operateDelBtnColor;
        color: $operateDelBtnColor;
        background: transparent;
      }
    }
  }
  .table_content {
    // height: calc(100% - #{$tableHeadHeight});
    padding: 0 24px;
    .pagination_area {
      float: right;
      padding-top: 10px;
    }
    ::v-deep .list_operate {
      font-size: 14px;
      color: #1492ff;
      cursor: pointer;
    }
    ::v-deep .divide {
      display: inline-block;
      height: 16px;
      width: 1px;
      background: rgba(0, 0, 0, 0.1);
      vertical-align: middle;
      margin: 0 4px;
    }
  }
}
.wrap .table_head .end-box {
  position: absolute;
  right: 0;
  > span {
    display: inline-block;
    border-radius: 0;
    &:first-child {
      margin-right: 0;
    }
  }
}
</style>
