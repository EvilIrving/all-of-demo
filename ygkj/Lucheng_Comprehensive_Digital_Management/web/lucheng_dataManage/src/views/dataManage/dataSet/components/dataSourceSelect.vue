<!--
 * @Author: zg
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: zg
 * @Description: 详情
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div class="column_details">
    <primary-table
      searchText="名称"
      @searchAction="searchAction"
      :hideAdd="true"
    >
      <div ref="tableBox" class="table-box">
        <!-- 表格 -->
        <table-page
          ref="tablePage"
          dataLoaded
          :url="tableUrl"
          :columns="columns"
          :query-params="queryParams"
          :pageIngType="false"
          :checkBox="true"
          :height="tableHeight"
          :headerCellClassName="cellClass"
          @tableLoaded="tableLoaded"
          @handle-select="handleSelect"
        >
          <template slot-scope="{ index }" slot="order">{{
            index + 1
          }}</template>
          <template slot-scope="{ row }" slot="dataBaseType">
            <span v-if="row.dataBaseType == '1'">MySQL</span>
            <span v-else-if="row.dataBaseType == '2'">ORACLE</span>
            <span v-else-if="row.dataBaseType == '3'">PostgreSQL</span>
            <span v-else-if="row.dataBaseType == '4'">SQLServer</span>
            <span v-else-if="row.dataBaseType == '5'">DM</span>
          </template>
        </table-page>
      </div>
    </primary-table>
    <!-- 自定义按钮组 -->
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" @click="sure">确 定</el-button>
    </div>
  </div>
</template>

<script>
import { ConfirmCustom, MessageError, MessageSuccess } from "_l/message";
import PrimaryTable from "_c/primary_table/index";
import TablePage from "_c/CommonForm/table-page";
export default {
  name: "dataSourceSelect",
  props: {
    dataSourceId: {
      type: String || Number,
    },
    dataSourceShow: {
      type: Boolean,
    },
  },
  components: { PrimaryTable, TablePage },
  data() {
    return {
      tableUrl: "/data-manage/dataSource/page",
      columns: [
        { label: "序号", slot: "order", width: 50 },
        { label: "数据库名称", prop: "dataBaseName" },
        { label: "数据库地址", prop: "dataBaseAddress" },
        { label: "数据库类型", slot: "dataBaseType" },
        { label: "创建人", prop: "createId" },
        { label: "创建时间", prop: "createTime" },
      ],
      queryParams: {},
      tableHeight: 0,
      checkItems: [],
      checkId: null,
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    if (this.dataSourceId && this.dataSourceId != "") {
      this.checkId = this.dataSourceId;
    }
    this.$nextTick(() => {
      this.tableHeight = this.$refs.tableBox.offsetHeight;
    });
  },
  methods: {
    // 查询
    searchAction(name) {
      this.queryParams.dataBaseName = name;
      this.refresh();
    },
    /**
     * 刷新
     */
    refresh() {
      this.$refs.tablePage.refresh();
    },
    /**
     * 关闭数据源弹框
     */
    closeDialog() {
      this.$emit("close");
    },

    cellClass(row) {
      if (row.columnIndex === 0) {
        //你需要判断的条件
        return "disabledCheck";
      }
    },

    /**
     * 表格加载完成后默认选中
     */
    tableLoaded(data) {
      this.$nextTick(() => {
        if (this.checkId && this.checkId != "") {
          data.forEach((row) => {
            if (this.checkId == row.id) {
              this.$refs.tablePage.toggleRowSelection(row, true);
              let index;
              index = this.isSelectedGoods(row, this.checkItems);
              if (index == -1) {
                this.checkItems.push(row);
              }
            }
          });
        }
      });
    },

    //单选
    handleSelect(selection, row) {
      if (this.checkItems.length && this.checkItems.length > 0) {
        let index;
        index = this.isSelectedGoods(row, this.checkItems);
        if (index > -1) {
          this.checkItems.splice(index, 1);
          this.checkId = null;
        } else {
          this.$refs.tablePage.clearSelection();
          this.checkItems = [];
          this.$refs.tablePage.toggleRowSelection(row, true);
          this.checkItems.push(row);
          this.checkId = row.id;
        }
      } else {
        this.checkItems.push(row);
        this.checkId = row.id;
      }
      //console.log(11, this.checkId);
    },
    //判断是否存在的方法
    isSelectedGoods(row, list) {
      let rows = JSON.parse(JSON.stringify(row));
      //是否选取 -1表示没选中
      return list.findIndex((item) => {
        return item.id == rows.id;
      });
    },

    sure() {
      this.closeDialog();
      this.$emit("setDataSource", this.checkItems);
    },
  },
};
</script> 

<style lang='scss' scoped>
.column_details {
  height: 650px;
  .wrap {
    width: 100%;
    height: calc(100% - 52px);
    ::v-deep .table_content {
      width: 100%;
      height: calc(100% - 72px);
      padding-bottom: 16px;
      .table-box {
        height: 100%;
      }
    }
  }
}
.dialog-footer {
  padding: 0 20px;
  text-align: right;
}
</style>
