<template>
  <div class="table-wrap" ref="tableDom">
    <u-table
      ref="plTable"
      :data="dataList"
      :max-height="tableHeight"
      use-virtual
      showBodyOverflow="title"
      showHeaderOverflow="title"
      :row-height="30"
      @row-click="rowClick"
      border
    >
      <u-table-column type="index" width="50" />
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
    tableHeight: {
      type: [Number, String],
      defaults: 420,
    },
  },
  data() {
    return {};
  },
  mounted() {
    // this.tableHeight = this.$refs.tableDom.offsetHeight;
    // console.log(this.tableHeight);
  },
  methods: {
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
    .el-table {
      overflow-y: auto;
      background: rgba(0, 0, 0, 0);
      border: 0;
      tr {
        background: rgba(0, 0, 0, 0);
        th {
          background: rgba(0, 0, 0, 0);
          border: 0;
        }
        td {
          background: rgba(0, 0, 0, 0);
          border: 0;
          padding:0;
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