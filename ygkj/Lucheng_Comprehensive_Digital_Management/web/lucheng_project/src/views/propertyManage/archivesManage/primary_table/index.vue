<template>
  <div class="wrap">
    <div class="table-view-head clearfix" v-if="searchShow">
      <span v-if="title">{{ title }}</span>
      <span class="tips" v-if="tips">{{ tipsText }}</span>
      <div class="right search-area">
        <slot name="btn-slot">
          <i
            class="btn icon-btn-add right"
            v-if="!hideAdd"
            @click="addAction()"
          ></i>
        </slot>
        <div class="input-search right" v-if="!hideInput">
          <el-input
            placeholder="请输入内容"
            v-model="searchName"
            @change="doSearch"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="doSearch"
            ></el-button>
          </el-input>
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
  },
  data() {
    return {
      searchName: "",
      tipsText: "",
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
    doSearch() {
      this.$emit("doSearch", this.searchName);
    },
  },
};
</script>

<style lang="scss" scoped>
$tableHeadHeight: 36px;
.wrap {
  padding: 10px 10px 0 10px;
  .table-view-head {
    height: $tableHeadHeight;
    .icon-btn-add {
      width: 80px;
      height: 36px;
    }
    span {
      font-weight: bold;
    }
    .tips {
      font-weight: normal;
      font-size: 14px;
      line-height: $tableHeadHeight;
    }
    .search-area {
      width: fit-content;
    }
  }
  .table-content {
    height: calc(100% - #{$tableHeadHeight});
    .pagination-area {
      padding-top: 20px;
    }
  }
  ::v-deep .el-input__inner {
    -webkit-appearance: none;
    background-color: #fff;
    background-image: none;
    border-radius: 0.208vw;
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
    border: 1px solid #dcdfe6;
    box-sizing: border-box;
    color: #606266;
    display: inline-block;
    font-size: inherit;
    outline: 0;
    transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
    width: 100%;
  }
}
</style>
<style lang="scss">
$inputHeightHeight: 36px;
.table-view-head .el-input__inner {
  height: $inputHeightHeight;
  line-height: $inputHeightHeight;
}
</style>
