<template>
  <div class="table-wrap" ref="tableDom">
    <u-table
      ref="plTable"
      :data="dataList"
      :max-height="tableHeight"
      use-virtual
      showBodyOverflow="title"
      showHeaderOverflow="title"
      :row-height="40"
      @row-click="rowClick"
      border
      stripe
      v-loading="loading"
      element-loading-text="数据加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
    >
      <u-table-column :width="projectType == '河道' ? '50' : '1'">
        <template slot-scope="scope" v-if="projectType == '河道'">
          <i :class="scope.row.healthState == '1' ? 'icon-green-code' : scope.row.healthState == '2' ? 'icon-Yellow-code' : 'icon-red-code'" />
        </template>
      </u-table-column>
      <u-table-column v-if="pageNum" type="index" label="序号" width="50" :index="indexMethod" />
      <u-table-column v-else type="index" label="序号" width="50" />
      <u-table-column
        v-for="item in columnList"
        :key="item.id"
        :show-overflow-tooltip="true"
        :prop="item.prop"
        :label="item.label"
        :sortable="item.sortable ? item.sortable : false"
        :min-width="item.width ? item.width : ''"
      />
    </u-table>
  </div>
</template>
<script>
export default {
  name: "TyphoonTable",
  props: {
    columnList: Array,
    dataList: Array,
    projectType: {
      type: String,
      default: null,
    },
    pageNum: {
      type: [Number, String],
      defaults: 1,
    },
    pageSize: {
      type: [Number, String],
      defaults: 10,
    },
    tableHeight: {
      type: [Number, String],
      defaults: 420,
    },
    loading: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
    };
  },
  mounted() {
    // this.tableHeight = this.$refs.tableDom.offsetHeight;
    // console.log(this.tableHeight);
  },
  methods: {
    // 序号处理
    indexMethod(index) {
      return (this.pageNum - 1) * this.pageSize + index + 1;
    },
    rowClick(row) {
      this.$emit("rowClick", row);
    },
    handleSelectionChange(val) {
      this.$emit("handleSelectionChange", val);
    },
    dataLoad(data) {
      // console.log(data, 666);
      this.$emit("dataLoad", data);
    },
  },
};
</script>
<style lang="scss">
.table-wrap {
  @include flexbox;
  @include flexflow(column, wrap);
  height: 100%;
  width: 100%;
  .plTableBox .el-table__fixed-right::before,
  .plTableBox .el-table__fixed::before {
    background: rgba(0, 0, 0, 0);
  }
  .plTableBox .el-table--border::after,
  .plTableBox .el-table--group::after,
  .plTableBox .el-table::before {
    background: rgba(0, 0, 0, 0);
  }
  .plTableBox {
    margin-top: 16px;
    .el-table {
      overflow-y: auto;
      background: rgba(0, 0, 0, 0);
      border: 0;
      .el-table__row.el-table__row--striped {
        background-image: linear-gradient(
          90deg,
          rgba(255, 255, 255, 0.2) 0%,
          rgba(249, 249, 249, 0) 100%
        );
      }
      thead tr {
        background-image: linear-gradient(
          90deg,
          rgba(44, 158, 191, 0.3) 3%,
          rgba(41, 147, 180, 0.1) 100%
        );
      }
      tr {
        background: rgba(0, 0, 0, 0);
        th {
          // background: rgba(0, 0, 0, 0);
          border: 0;
          color: #56fefe;
          min-height: 44px;
        }
        td {
          background: rgba(0, 0, 0, 0);
          border: 0;
          padding: 0;
        }
        &.hover-row {
          td {
            background: rgba(0, 0, 0, 0);
          }
        }
      }
    }
  }
  .pagination-area {
    margin-top: 10px;
  }
}

.el-loading-spinner .el-loading-text,
.el-loading-spinner i {
  color: #32bcb4;
}
</style>