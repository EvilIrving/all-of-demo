<!--
 * @Author: wupengfei
 * @LastEditTime: 2022-05-23 11:12:22
 * @LastEditors: Please set LastEditors
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\components\primary_table\index.vue
-->
<template>
  <div class="wrap primary-table">
    <div ref="tableHead" class="table-view-head clearfix" v-if="searchShow">
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
    <div class="table-content">
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
      type: [String, Number],
      default: "请输入内容",
    },
  },
  data() {
    return {
      searchName: "",
      tipsText: "",
      isOpen: false,
      activeType: 0,
      tableHead: 0,
    };
  },
  computed: {
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
.primary-table {
  height: 100%;
  @include flexbox;
  @include flexflows(column, nowrap);
  .table-content {
    @include flex(1);
    overflow: auto;
  }
}
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
              background-color: #328173;
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
            background-color: #e44c27;
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