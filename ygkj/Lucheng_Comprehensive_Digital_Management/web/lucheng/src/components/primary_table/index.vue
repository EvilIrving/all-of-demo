<!--
 * @Author: wupengfei
 * @LastEditTime: 2021-01-13 11:56:48
 * @LastEditors: lsh
 * @Descripttion: 
 * @FilePath: \hydrology\src\components\primary_table\index.vue
-->
<template>
  <div class="wrap">
    <div class="table-view-head clearfix" v-if="searchShow">
      <div v-if="isOpen">
        <p v-if="dropTitle" class="title">{{ dropTitle }}</p>
        <slot name="drop-down"></slot>
      </div>
      <div class="drop-down" v-if="dropTitle" @click="handleDropDown">
        <i :class="isOpen ? 'icon-list-drop-up' : 'icon-list-drop-down'"></i>
      </div>
      <p v-if="title" class="title">{{ title }}</p>
      <span class="tips" v-if="tips">{{ tipsText }}</span>
      <slot name="tab-slot"></slot>
      <div class="search-area">
        <div class="left-operate">
          <slot name="type-slot"> </slot>
        </div>
        <div class="right-operate">
          <slot name="date-slot"></slot>
          <div class="input-search" v-if="!hideInput">
            <el-input
              :placeholder="placeholder"
              v-model="searchName"
              @input="doSearch"
              ><i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
          </div>
          <slot name="btn-slot">
            <span class="btn" v-if="!hideAdd" @click="addAction()">新增</span>
          </slot>
        </div>
      </div>
    </div>
    <div
      class="table-content"
      ref="tableContent"
      :style="{ height: tabeleHeight }"
    >
      <slot></slot>
    </div>
  </div>
</template>
<script>
export default {
  name: "PrimaryTable",
  props: {
    title: String,
    dropTitle: String,
    tips: String,
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
    dropDown: {
      type: Boolean,
      default: true,
    },
    listTab: {
      type: Boolean,
      default: true,
    },
    placeholder: {
      type: [String,Number],
      default: "请输入内容",
    },
  },
  data() {
    return {
      searchName: "",
      tipsText: "",
      isOpen: false,
      activeType: 0,
      headHeight: 0,
    };
  },
  watch: {
    tips: function (val) {
      this.tipsText = val;
    },
  },
  computed: {
    tabeleHeight() {
      return `calc(100% - ${this.headHeight + 170}px)`;
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
    doSearch() {
      this.$emit("doSearch", this.searchName);
    },
    handleDropDown() {
      this.isOpen = !this.isOpen;
      // this.$emit("handleDropDown", this.isOpen);
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
@import "@/style/theme.scss";
// $tableHeadHeight: 52px;
$operateHeight: 44px;
.wrap {
  color: #333333;
  .table-view-head {
    // height: $tableHeadHeight;
    .icon-btn-add {
      width: 80px;
      height: 36px;
    }
    .title {
      font-weight: bold;
      font-size: 22px;
      color: $primaryListColor;
      border-left: 4px solid $primaryListColor;
      padding-left: 10px;
      margin-bottom: 24px;
    }
    .tips {
      font-weight: normal;
      font-size: 14px;
      // line-height: $tableHeadHeight;
    }
    .search-area {
      // width: fit-content;
      width: 100%;
      height: $operateHeight;
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;
      .condition-lable {
        font-size: 14px;
        color: #95969b;
        line-height: $operateHeight;
        margin-right: 16px;
      }
      .input-search {
        margin-left: 20px;
      }
      .left-operate {
        .type-box {
          display: flex;
          > div {
            height: 30px;
            line-height: 30px;
            width: 50px;
            text-align: center;
            cursor: pointer;
            background: #fff;
            font-size: 14px;
            color: #333333;
            &:first-child {
              border-radius: 6px 0 0 6px;
            }
            &:last-child {
              border-radius: 0 6px 6px 0;
            }
            &.active {
              background-color:#328173;
              color: #fff;
            }
          }
        }
      }
      .right-operate {
        display: flex;
        > div {
          display: flex;
        }
        .btn {
          background-color: #328173;
          color: #fff;
          border-radius: 4px;
          &.del-btn {
            background-color: #E44C27;
          }
        }
      }
    }
    .drop-down {
      width: 100%;
      text-align: center;
      cursor: pointer;
      margin-bottom: 24px;
    }
  }
  .table-content {
    // height: calc(100% - #{$tableHeadHeight});
    .pagination-area {
      padding-top: 20px;
    }
  }
}
</style>
<style lang="scss">
@import "@/style/theme.scss";
$inputHeightHeight: 40px;
$inputHeightWidth: 280px;
$inputBackground: rgba(255, 255, 255, 0.4);
$inputBorderColor: #e5e5e5;
$listItemOpeateColor: #3ebeb6;
$listItemOpeateDelColor: #e99b11;
.table-view-head {
  .el-input__inner {
    height: $inputHeightHeight;
    line-height: $inputHeightHeight;
    background: $inputBackground;
    border: 1px solid $inputBorderColor;
    border-radius: 3px;
    color: #333;
  }

  .el-input.is-active .el-input__inner,
  .el-input__inner:focus,
  .el-range-editor.is-active,
  .el-range-editor.is-active:hover {
    border-color: $inputBorderColor;
  }
}
.table-view-head .input-search .el-input__inner {
  width: $inputHeightWidth;
  border-radius: 20px;
}
.el-date-table td.current:not(.disabled) span,
.el-pagination.is-background .el-pager li:not(.disabled).active,
.el-date-table td.end-date span,
.el-date-table td.start-date span {
  background-color: #2f7466;
}
.el-date-table td.available:hover,
.el-date-table td.today span {
  color: $primaryListColor;
}
.table-content {
  .el-table tr {
    color: #333;
  }
  .list-item-opearte {
    color: $listItemOpeateColor;
    cursor: pointer;
    margin-right: 10px;
    &-del {
      color: $listItemOpeateDelColor;
    }
  }
  .el-table--striped .el-table__body tr.el-table__row--striped td,
  .el-table--striped .el-table__body tr.el-table__row--striped td:hover {
    background: #d5ddda;
  }
  .el-checkbox__inner {
    border-color: #979797;
  }

  .el-checkbox__input.is-checked .el-checkbox__inner,
  .el-checkbox__input.is-indeterminate .el-checkbox__inner {
    background-color: $primaryDialog;
    border-color: $primaryDialog;
    color: #fff;
  }
  .el-checkbox__inner:hover {
    border-color: $primaryDialog;
  }
}

.chart-content {
  height: 274px;
  width: 100%;
  display: flex;
  .chart22 {
    display: flex;
    width: 22%;
    height: 100%;
    margin-right: 50px;
  }
  .chart56 {
    display: flex;
    width: calc(56% - 100px);
    height: 100%;
  }
  .pie-box,
  .ring-box {
    width: calc(100% - 106px);
  }
  .bar-box {
    width: 100%;
    height: 100%;
  }
  .legend-box.ring-legend {
    width: 90px;
    max-height: 100%;
    height: auto;
    margin-left: 16px;
    overflow-y: auto;
  }
  .legend-box.pie-legend {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 90px;
    margin-left: 16px;
  }
  .sub-legend-box {
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
  .pie-legend {
    &-item {
      margin-bottom: 10px;
      i {
        display: inline-block;
        width: 20px;
        height: 4px;
        border-radius: 2px;
      }
      p {
        display: flex;
        justify-content: space-between;
        font-size: 12px;
        color: #333333;
        font-weight: normal;
        line-height: 24px;
      }
    }
  }
  .ring-legend {
    &-item {
      margin-bottom: 24px;
      i {
        display: inline-block;
        width: 8px;
        height: 8px;
        border-radius: 50%;
        border: 2px solid #ffb946;
      }
      span {
        font-size: 14px;
        color: #192a3e;
        font-weight: normal;
        margin-left: 9px;
      }
    }
  }
}
</style>
