<template>
  <div class="table-wrap" ref="tableDom">
    <el-table
      :data="dataList"
      stripe
      :key="tableIndex"
      style="width: 100%"
      :height="tableHeight"
      header-row-class-name="table-header"
      @row-click="rowClick"
      v-loading="tableLoading"
      element-loading-background="rgba(0, 0, 0, 0.5)"
      element-loading-text="数据正在加载中"
      element-loading-spinner="el-icon-loading"
    >
      <el-table-column type="index" label="序号" width="60"> </el-table-column>
      <el-table-column
        :key="index"
        v-for="(item, index) in columnList"
        :prop="item.prop"
        :label="item.label"
        :load="dataLoad"
        :sortable="item.sortable"
        :width="item.width ? item.width : ''"
      >
        <slot v-if="item.slot"></slot>
        <template slot-scope="scope">
          <span v-if="item.lessZeroEmpty">{{
            scope.row[item.prop] < 0 ? " - " : scope.row[item.prop]
          }}</span>
          <span v-else>{{
            scope.row[item.prop] || scope.row[item.prop] == 0
              ? scope.row[item.prop]
              : "-"
          }}</span>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-area" v-if="paginationFlag">
      <el-pagination
        :page-size="pageSize"
        :pager-count="5"
        layout="total, prev, pager, next"
        :total="total"
        @current-change="pageChange"
      >
      </el-pagination>
    </div>
  </div>
</template>
<script>
export default {
  name: "PrimaryMapTable",
  props: {
    columnList: Array,
    dataList: Array,
    tableIndex:{
      type:[String,Number],
      default:'1'
    },
    tableLoading: {
      type: Boolean,
      default: false,
    },
    paginationFlag: {
      type: Boolean,
      default: false,
    },
    pageSize: {
      type: Number,
      default: 15,
    },
    total: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      tableHeight: 0,
    };
  },
  mounted() {
    this.tableHeight = this.$refs.tableDom.offsetHeight;
  },
  methods: {
    rowClick(row) {
      this.$emit("rowClick", row);
    },
    dataLoad(data) {
      // console.log(data, 666);
      this.$emit("dataLoad", data);
    },
    pageChange(page) {
      this.$emit("pageChange", page);
    },
  },
};
</script>
<style scoped lang="scss">
  .table-wrap {
    @include flexbox;
    @include flexflow(column, wrap);
    @include flex(1);
    height: 100%;
    width: 100%;
    ::v-deep .el-table {
      overflow-y: auto;
      th{
        background: rgba(0,0,0,0);
        padding: 0;
      }
    }
    .pagination-area {
      margin-top: 10px;
    }
    ::v-deep .el-pagination{
      .el-pagination__total{
        color: #fff;
      }
      .el-pager{
        li{
          background: rgba(0,0,0,0);
        }
      }
    }
  }
</style>
<style lang="scss">
.el-loading-spinner .el-loading-text,
.el-loading-spinner i {
  color: #32bcb4;
}
</style>