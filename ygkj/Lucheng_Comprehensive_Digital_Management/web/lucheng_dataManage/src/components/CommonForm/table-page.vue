<template>
  <div>
    <el-table
      ref="tableMain"
      v-loading="loading"
      :data="data"
      :height="height"
      :max-height="maxHeight"
      :stripe="stripe"
      :border="border"
      :size="size"
      :fit="fit"
      :show-header="showHeader"
      :highlight-current-row="highlightCurrentRow"
      :current-row-key="currentRowKey"
      :row-class-name="rowClassName"
      :row-style="rowStyle"
      :cell-class-name="cellClassName"
      :cell-style="cellStyle"
      :header-row-class-name="headerRowClassName"
      :header-row-style="headerRowStyle"
      :header-cell-class-name="headerCellClassName"
      :header-cell-style="headerCellStyle"
      :row-key="rowKey"
      :empty-text="emptyText"
      :default-expand-all="defaultExpandAll"
      :expand-row-keys="expandRowKeys"
      :default-sort="defaultSort"
      :tooltip-effect="tooltipEffect"
      :show-summary="showSummary"
      :sum-text="sumText"
      :summary-method="summaryMethod"
      :span-method="spanMethod"
      :select-on-indeterminate="selectOnIndeterminate"
      :indent="indent"
      :lazy="lazy"
      :load="load"
      :tree-props="treeProps"
      @selection-change="handleSelectionChange"
      @current-change="handleCurrentRowChange"
      @sort-change="handleSortChange"
      @select="handleSelect"
    >
      <template v-for="(item, index) in columns">
        <el-table-column
          v-if="item.hasOwnProperty('slot')"
          :key="index"
          :type="item.type"
          :index="item.index"
          :column-key="item.columnKey"
          :label="item.label"
          :prop="item.slot"
          :width="item.width"
          :min-width="item.minWidth"
          :fixed="item.fixed"
          :render-header="item.renderHeader"
          :sortable="item.sortable"
          :sort-method="item.sortMethod"
          :sort-by="item.sortBy"
          :sort-orders="item.sortOrders"
          :resizable="item.resizable"
          :formatter="item.formatter"
          :show-overflow-tooltip="item.showOverflowTooltip"
          :align="item.align ? item.align : 'center'"
          :header-align="item.headerAlign"
          :class-name="item.className"
          :label-class-name="item.labelClassName"
          :selectable="item.selectable"
          :reserve-selection="item.reserveSelection"
          :filters="item.filters"
          :filter-placement="item.filterPlacement"
          :filter-multiple="item.filterMultiple"
          :filter-method="item.filterMethod"
          :filtered-value="item.filteredValue"
        >
          <template slot-scope="scope">
            <slot
              :row="scope.row"
              :index="scope.$index"
              :column="scope.column"
              :name="item.slot"
            ></slot>
          </template>
        </el-table-column>
        <el-table-column
          v-else
          :key="index"
          :type="item.type"
          :index="item.index"
          :column-key="item.columnKey"
          :label="item.label"
          :prop="item.prop"
          :width="item.width"
          :min-width="item.minWidth"
          :fixed="item.fixed"
          :render-header="item.renderHeader"
          :sortable="item.sortable"
          :sort-method="item.sortMethod"
          :sort-by="item.sortBy"
          :sort-orders="item.sortOrders"
          :resizable="item.resizable"
          :formatter="item.formatter"
          :show-overflow-tooltip="item.showOverflowTooltip"
          :align="item.align ? item.align : 'center'"
          :header-align="item.headerAlign"
          :class-name="item.className"
          :label-class-name="item.labelClassName"
          :selectable="item.selectable"
          :reserve-selection="item.reserveSelection"
          :filters="item.filters"
          :filter-placement="item.filterPlacement"
          :filter-multiple="item.filterMultiple"
          :filter-method="item.filterMethod"
          :filtered-value="item.filteredValue"
        />
      </template>
    </el-table>

    <div style="float: right; padding-top: 10px">
      <el-pagination
        v-if="pageIng"
        :small="small"
        :background="background"
        :page-size="pageSize"
        :total="total"
        :page-count="pageCount"
        :pager-count="pagerCount"
        :current-page.sync="current"
        :layout="layout"
        :page-sizes="pageSizes"
        :popper-class="popperClass"
        :prev-text="prevText"
        :next-text="nextText"
        :disabled="pageDisabled"
        :hide-on-single-page="hideOnSinglePage"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { getModulesTables } from "@/api/common";

export default {
  name: "table-page",
  data() {
    return {
      // 表格配置相关
      loading: false, // 表格是否加载中
      // 分页相关
      // sortTable: this.sort, // 排序字段
      sortTable: this.defaultSort.prop, // 排序字段
      // orderTable: this.order, // 排序类型
      orderTable: this.defaultSort.order === "ascending" ? "asc" : "desc", // 排序类型
      // checkBox:true,// 是否显示checkBox
      current: 1, // 当前页面
      pageSizeTable: this.pageSize, // 每页显示多少条
      total: 0, // 一共多少页
      initPage: 1, // 初始页面
      // 分页相关
      pageIng: this.pageIngType,
      // showTotal: true,
      // showSizer: true,
      // showElevator: true,
      // 数据相关
      data: [], // 返回数据
    };
  },
  props: {
    /*Table Attributes*/
    pageIngType: {
      type: Boolean,
      default: true,
    },
    // 查询参数配置
    queryParams: {
      // 查询参数
      type: Object,
    },
    checkBox: {
      // 是否显示checkBox
      type: Boolean,
      default: true,
    },
    methodType: {
      type: String,
      default: "post",
    },
    url: {
      // url 地址
      type: String,
    },
    height: {
      // 高度
      type: [String, Number],
    },
    maxHeight: {
      // 高度
      type: [String, Number],
    },
    stripe: {
      //是否为斑马纹 table
      type: Boolean,
      default: false,
    },
    border: {
      //是否带有纵向边框
      type: Boolean,
      default: true,
    },
    size: {
      //Table 的尺寸
      type: String,
    },
    fit: {
      //列的宽度是否自撑开
      type: Boolean,
      default: true,
    },
    showHeader: {
      //是否显示表头
      type: Boolean,
      default: true,
    },
    highlightCurrentRow: {
      //是否要高亮当前行
      type: Boolean,
      default: false,
    },
    currentRowKey: {
      //当前行的 key，只写属性
      type: [String, Number],
    },
    rowClassName: {
      //行的 className 的回调方法，也可以使用字符串为所有行设置一个固定的 className。
      type: [String, Function],
    },
    rowStyle: {
      //行的 style 的回调方法，也可以使用一个固定的 Object 为所有行设置一样的 Style。
      type: [Object, Function],
    },
    cellClassName: {
      //单元格的 className 的回调方法，也可以使用字符串为所有单元格设置一个固定的 className。
      type: [String, Function],
    },
    cellStyle: {
      //单元格的 style 的回调方法，也可以使用一个固定的 Object 为所有单元格设置一样的 Style。
      type: [Object, Function],
    },
    headerRowClassName: {
      //表头行的 className 的回调方法，也可以使用字符串为所有表头行设置一个固定的 className。
      type: [String, Function],
    },
    headerRowStyle: {
      //表头行的 style 的回调方法，也可以使用一个固定的 Object 为所有表头行设置一样的 Style。
      type: [Object, Function],
    },
    headerCellClassName: {
      //表头单元格的 className 的回调方法，也可以使用字符串为所有表头单元格设置一个固定的 className。
      type: [String, Function],
    },
    headerCellStyle: {
      //表头单元格的 style 的回调方法，也可以使用一个固定的 Object 为所有表头单元格设置一样的 Style。
      type: [Object, Function],
    },
    rowKey: {
      //	行数据的 Key，用来优化 Table 的渲染；在使用 reserve-selection 功能与显示树形数据时，该属性是必填的。类型为 String 时，支持多层访问：user.info.id，但不支持 user.info[0].id，此种情况请使用 Function。
      type: [String, Function],
    },
    emptyText: {
      //空数据时显示的文本内容，也可以通过 slot="empty" 设置
      type: String,
      default: "暂无数据",
    },
    defaultExpandAll: {
      //是否默认展开所有行，当 Table 包含展开行存在或者为树形表格时有效
      type: Boolean,
      default: false,
    },
    expandRowKeys: {
      //可以通过该属性设置 Table 目前的展开行，需要设置 row-key 属性才能使用，该属性为展开行的 keys 数组。
      type: Array,
    },
    defaultSort: {
      //默认的排序列的 prop 和顺序。它的prop属性指定默认的排序的列，order指定默认排序的顺序
      type: Object,
      default() {
        return { prop: null, order: null };
      },
    },
    tooltipEffect: {
      //tooltip effect 属性
      type: String,
    },
    showSummary: {
      //是否在表尾显示合计行
      type: Boolean,
      default: false,
    },
    sumText: {
      //合计行第一列的文本
      type: String,
    },
    summaryMethod: {
      //自定义的合计计算方法
      type: Function,
    },
    spanMethod: {
      //合并行或列的计算方法
      type: Function,
    },
    selectOnIndeterminate: {
      //在多选表格中，当仅有部分行被选中时，点击表头的多选框时的行为。若为 true，则选中所有行；若为 false，则取消选择所有行
      type: Boolean,
      default: true,
    },
    indent: {
      //	展示树形数据时，树节点的缩进
      type: Number,
      default: 16,
    },
    lazy: {
      //是否懒加载子节点数据
      type: Boolean,
      default: false,
    },
    load: {
      //加载子节点数据的函数，lazy 为 true 时生效，函数第二个参数包含了节点的层级信息
      type: Function,
    },
    treeProps: {
      //渲染嵌套数据的配置选项
      type: Object,
      default() {
        return {
          hasChildren: "hasChildren",
          children: "children",
        };
      },
    },
    dataLoaded: {
      // 操作表格数据加载完成
      type: Boolean,
      default: false,
    },
    manualLoad: {
      // 手动加载
      type: Boolean,
      default: false,
    },

    /*Table-column Attributes*/
    columns: {
      type: Array,
      require: true,
      default() {
        /*return [{
              slot: null,
              type: null,
              index: null,
              columnKey: null,
              label: null,
              prop: null,
              width: null,
              minWidth: null,
              fixed: null,
              renderHeader: null,
              sortable: false,
              sortMethod: null,
              sortBy: null,
              sortOrders: null,
              resizable: null,
              formatter: null,
              showOverflowTooltip: null,
              align: 'center',
              headerAlign: null,
              className: null,
              labelClassName: null,
              selectable: null,
              reserveSelection: null,
              filters: null,
              filterPlacement: null,
              filterMultiple: null,
              filterMethod: null,
              filteredValue: null
          }]*/
        return [];
      },
    },

    /*Pagination Attributes*/
    small: {
      //是否使用小型分页样式
      type: Boolean,
      default: false,
    },
    background: {
      //是否为分页按钮添加背景色
      type: Boolean,
      default: false,
    },
    pageSize: {
      //每页显示条目个数，支持 .sync 修饰符
      type: Number,
      default: 10,
    },
    // total: {
    //     //总条目数
    //     type: Number
    // },
    pageCount: {
      //总页数，total 和 page-count 设置任意一个就可以达到显示页码的功能；如果要支持 page-sizes 的更改，则需要使用 total 属性
      type: Number,
    },
    pagerCount: {
      //页码按钮的数量，当总页数超过该值时会折叠
      type: Number,
      default: 7,
    },
    currentPage: {
      //当前页数，支持 .sync 修饰符
      type: Number,
      default: 1,
    },
    layout: {
      //组件布局，子组件名用逗号分隔
      type: String,
      default() {
        return "total, sizes, prev, pager, next, jumper";
      },
    },
    pageSizes: {
      //每页显示个数选择器的选项设置
      type: Array,
      default() {
        return [10, 20, 30, 40, 50, 100];
      },
    },
    popperClass: {
      //每页显示个数选择器的下拉框类名
      type: String,
    },
    prevText: {
      //替代图标显示的上一页文字
      type: String,
    },
    nextText: {
      //替代图标显示的下一页文字
      type: String,
    },
    pageDisabled: {
      //是否禁用
      type: Boolean,
      default: false,
    },
    hideOnSinglePage: {
      //只有一页时是否隐藏
      type: Boolean,
    },
  },
  methods: {
    /**
     * pageSize 改变时会触发
     * @param val 每页条数
     */
    handleSizeChange(val) {
      this.pageSizeTable = val;
      this.getData(this.initPage);
    },
    /**
     * currentPage 改变时会触发
     * @param val 当前页
     */
    handleCurrentChange(val) {
      this.getData(val);
    },
    /**
     * 获取数据
     * @param pageNum
     */
    getData(pageNum) {
      // 定义分页对象
      let pageObj = {};
      if (this.pageIngType) {
        pageObj = {
          current: pageNum,
          size: this.pageSizeTable,
          sort: this.sortTable,
          order: this.orderTable,
        };
      }
      if (this.queryParams != null) {
        Object.assign(pageObj, this.queryParams);
      }
      this.loading = true;
      getModulesTables(this.url, this.methodType, pageObj).then((response) => {
        let dataObj = response.data;
        // 是否包含这个key
        if (dataObj.hasOwnProperty("records")) {
          this.current = pageNum;
          this.data = dataObj.records;
          this.total = dataObj.total;
        } else {
          this.data = dataObj;
          // 分页相关
          this.pageIng = false;
        }
        this.loading = false;
        if (this.dataLoaded) this.$emit("tableLoaded", this.data);
      });
    },
    //-----------------用于多选------------------
    /**
     *用于多选表格，清空用户的选择
     */
    clearSelection() {
      this.$refs.tableMain.clearSelection();
    },
    toggleRowSelection(row, selected) {
      this.$refs.tableMain.toggleRowSelection(row, selected);
    },
    /**
     * 当选择项发生变化时会触发该事件
     * @param selection
     */
    handleSelectionChange(selection) {
      this.$emit("table-select-val", selection);
    },

    handleSelect(selection, row) {
      this.$emit("handle-select", selection, row);
    },
    //-----------------用于单选------------------
    /**
     * 当表格的当前行发生变化的时候会触发该事件，如果要高亮当前行，请打开表格的 highlight-current-row 属性
     * @param currentRow
     * @param oldCurrentRow
     */
    handleCurrentRowChange(currentRow, oldCurrentRow) {
      this.$emit("table-select-row", currentRow, oldCurrentRow);
    },
    /**
     * 用于单选表格，设定某一行为选中行，如果调用时不加参数，则会取消目前高亮行的选中状态。
     * @param row
     */
    setCurrentRow(row) {
      this.$refs.tableMain.setCurrentRow(row);
    },
    /**
     *    当表格的排序条件发生变化的时候会触发该事件
     * @param sortData
     */
    handleSortChange(sortData) {
      this.sortTable = sortData.prop; // 排序字段
      this.orderTable = sortData.order === "ascending" ? "asc" : "desc"; // 排序类型
      this.getData(this.current);
    },
    /**
     * 刷新
     */
    refresh() {
      this.current = 1;
      this.getData(this.initPage);
    },

    /**
     * 从父页面获取data
     */
    getTableData(data) {
      this.data = data;
    },

    /**
     * 重新布局
     */
    doLayout() {
      this.$refs.tableMain.doLayout();
    },
  },
  created() {
    if (this.checkBox) {
      this.columns.unshift({
        type: "selection",
        width: 60,
        align: "center",
      });
    }
  },
  mounted() {
    if (!this.manualLoad) this.getData(this.initPage);
  },
};
</script>

<style scoped lang="scss">
/deep/ .el-table {
  .el-table__header th {
    height: 44px;
  }
  .el-table__body-wrapper.is-scrolling-none {
    height: calc(100% - 44px) !important;
  }
}

.el-table ::v-deep.disabledCheck .cell .el-checkbox__inner {
  display: none !important;
}
.el-table ::v-deep.disabledCheck .cell:before {
  content: "选择";
  position: absolute;
  right: 11px;
}
</style>
