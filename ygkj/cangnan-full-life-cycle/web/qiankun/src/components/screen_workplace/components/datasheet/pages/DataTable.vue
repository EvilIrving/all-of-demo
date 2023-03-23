<template>
  <div class="collect-table">
    <div class="collect-table__content">
      <div class="main-search-box">
        <template v-for="(item, index) in querySelectList">
          {{ item.colDesc }}
          <el-input
            style="width: 150px; margin: 0 8px"
            :placeholder="item.placeholder"
            :key="index"
            v-if="item.type == 'text'"
            v-model="queryModel[index]"
            @change="getList"
          ></el-input>
          <el-select
            v-if="item.type == 'select'"
            v-model="queryModel[index]"
            :key="index"
            :placeholder="item.placeholder"
            style="margin: 0 8px"
            clearable
            @change="getList"
          >
            <el-option
              v-for="(option, optionIndex) in item.options"
              :key="optionIndex"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
          <el-radio-group
            :key="index"
            v-if="item.type == 'radio'"
            style="margin: 0 8px"
            v-model="queryModel[index]"
            @change="getList"
          >
            <el-radio
              v-for="(option, optionIndex) in item.options"
              :key="optionIndex"
              :label="option.value"
              >{{ option.label }}</el-radio
            >
          </el-radio-group>
          <el-checkbox-group
            :key="index"
            style="margin: 0 8px; display: inline-block"
            v-if="item.type == 'checkbox'"
            v-model="queryModel[index]"
            @change="getList"
          >
            <el-checkbox
              v-for="(option, optionIndex) in item.options"
              :key="optionIndex"
              :label="option.value"
              >{{ option.label }}</el-checkbox
            >
          </el-checkbox-group>
        </template>
      </div>
      <div class="table-wrap">
        <vxe-table
          stripe
          :loading="loading"
          :data="tableData"
          :height="480"
          :sort-config="{ remote: true }"
          highlight-hover-row
          show-header-overflow="tooltip"
          @sort-change="sortChange"
        >
          <vxe-table-column
            type="seq"
            :seq-config="seqMethod"
            title="序号"
            width="55"
          />
          <vxe-table-column
            v-for="(item, index) in tableAsyncHeader"
            :key="index"
            :field="item.prop"
            :title="item.label"
            :formatter="getFormatter"
            show-header-overflow="tooltip"
            show-overflow
            min-width="20%"
            sortable
          >
            <template #default="{ row }">
              <template v-if="isFile(item.prop)">
                <vxe-button
                  type="text"
                  status="primary"
                  icon="vxe-icon-paperclip"
                  content="查看"
                  @click="onFilePreview(row[item.prop])"
                />
              </template>
              <template v-else>{{ row[item.prop] }}</template>
            </template>
          </vxe-table-column>
          <!-- <template v-if="loading || tableAsyncHeader.length == 0">
            <vxe-table-column title="" />
            <vxe-table-column title="" />
            <vxe-table-column title="" />
          </template> -->
          <vxe-table-column
            title="操作"
            fixed="right"
            width="160"
            v-if="ManageBtnFlag"
          >
            <template #default="{ row }">
              <vxe-button
                @click="manageData(row)"
                type="text"
                status="primary"
                content="管理数据"
              />
            </template>
          </vxe-table-column>
        </vxe-table>
      </div>
    </div>
    <div class="main-pagination-area">
      <el-pagination
        class="right"
        @current-change="getList"
        :current-page.sync="pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>
  </div>
</template>

<script>
import { isDropdown, getDropdownOptions } from "../utils.js";
import { exportBusDataTable } from "@/api/dataTable";
import { dutyManage, dataManage } from "@/api/dataMaintenance";
import { mapGetters } from 'vuex';

export default {
  name: 'DataTable',
  components: {
  },
  data() {
    return {
      loading: false,
      searchName: "",
      tableHeight: 0,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableAsyncHeader: [],
      tableData: [],
      fieldConfig: [],
      ascending: null,
      sortColumn: "",
      tableId: "",
      tableName: "",
      queryConfig: [],
      queryModel: [],
      primaryKey: "",
      rowId: "",
    }
  },
  computed: {
    ...mapGetters(["DataType", "ObjectType", "ManageBtnFlag"]),
    querySelectList() {
      return this.queryConfig.map((item) => {
        switch (item.searchType) {
          case 'text': {//文本
            return {
              type: 'text',
              placeholder: `请输入${item.colDesc}`,
              colDesc: `${item.colDesc}`
            }
          }
          case 'radio': {//单选
            let options = JSON.parse(item.dropDownItems);
            return {
              type: 'radio',
              placeholder: `请输入${item.colDesc}`,
              colDesc: `${item.colDesc}`,
              options: Object.keys(options).map((key) => ({
                label: options[key],
                value: key,
              })),
            }
          }
          case 'select': {
            let options = JSON.parse(item.dropDownItems);
            return {
              type: 'select',
              colDesc: `${item.colDesc}`,
              placeholder: `请选择${item.colDesc}`,
              options: Object.keys(options).map((key) => ({
                label: options[key],
                value: key,
              })),
            };
          }
          case 'checkbox': {
            let options = JSON.parse(item.dropDownItems);
            return {
              type: 'checkbox',
              colDesc: `${item.colDesc}`,
              placeholder: `请选择${item.colDesc}`,
              options: Object.keys(options).map((key) => ({
                label: options[key],
                value: key,
              })),
            };
          }
        }

      });
    },
  },
  watch: {
    tableId: {
      immediate: true,
      handler(val) {
        this.$emit("changeTableId", val);
        if (val != "")
          this.getTableColumn().then(() => {
            this.getList();
          });
      },
    },
  },
  mounted() {
  },
  methods: {
    manageData(row) {
      window.open(`http://192.168.0.119:8093/resources/base/manage?rowId=${row[this.primaryKey]}&tableId=${this.tableId}&dataType=${this.DataType}&objectType=${this.ObjectType}`);
      // window.open(`https://117.149.228.207:8081/dataManagement/resources/base/manage?rowId=${row[this.primaryKey]}&tableId=${this.tableId}&dataType=${this.DataType}&objectType=${this.ObjectType}`);
    },
    getFormatter({ cellValue, column }) {
      let { property } = column;
      if (this.isDropdown(property)) {
        const options = this.getDropdownOptions(property);
        let option = options.find((item) => item.value == cellValue);
        return option.label;
      } else return cellValue;
    },
    isDropdown(colName) {
      return isDropdown(colName, this.fieldConfig);
    },
    getDropdownOptions(colName) {
      return getDropdownOptions(colName, this.fieldConfig);
    },
    isFile(colName) {
      let find = this.fieldConfig.find((item) => item.colName == colName);
      if (find != undefined) {
        return find.fileDesc != "" && find.fileDesc != null;
      } else return false;
    },
    /**
     * 左侧树叶子结点点击事件
     */
    nodeClick(data) {
      this.tableId = data.id;
      this.tableName = data.categoryName;
      // console.log(data,23333)
    },
    doApplySuccess() {
      this.getTableColumn().then(() => {
        this.getList();
      });
    },
    async getTableColumn() {
      this.loading = true;
      this.tableAsyncHeader = [];
      this.tableData = [];
      let res = await dataManage.getTableColumnConfig(this.tableId);
      if (res.code === 0) {
        this.noAuthority = false;
        this.dept = {
          id: res.data.dutyDepartmentId,
          name: res.data.dutyDepartment
        }
        this.fieldConfig = res.data.formConfig.filter((item) => item.isHidden == 1);
        this.queryConfig = res.data.queryConfig;
        this.queryModel = this.queryConfig.map((item) => {
          if (item.searchType == 'checkbox') {
            return [];
          } else {
            return '';
          }
        });
        this.primaryKey = res.data.primaryKey;
        this.tableAsyncHeader = res.data.listConfig.map((item) => ({
          label: item.colDesc,
          prop: item.colName,
        }));
      }
      this.loading = false;
    },
    async getList(ascending = null, sortColumn = "") {
      this.loading = true;
      let paramObj = {};
      this.queryConfig
        .map((item) => item.colName)
        .forEach((item, index) => {
          if (this.queryModel[index] instanceof Array) {
            paramObj[item] = this.queryModel[index].join(',')
          } else if (this.queryModel[index] != "") {
            paramObj[item] = this.queryModel[index];
          }
        });
      let paramJson = JSON.stringify(paramObj);
      let opt = {
        tableId: this.tableId,
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        paramJson,
        queryValue: this.searchName,
      };
      if (this.ascending != null && this.sortColumn != "") {
        opt.ascending = this.ascending;
        opt.sortColumn = this.sortColumn;
      }
      let res = await dataManage.getTableAllDataCommon(opt);
      this.loading = false;
      if (res.code === 214) {
        //没有权限时显示申请
        this.noAuthority = true;
        this.noAuthoritying = false;
        this.warnMessage = res.message;
      } else if (res.code === 215) {
        //已提交申请
        this.noAuthority = true;
        this.noAuthoritying = true;
        this.warnMessage = res.message;
      } else {
        this.noAuthoritying = false;
        this.noAuthority = false;
        this.tableData = res.data.list;
        this.total = res.data.totalRows;
      }
    },
    /**
     * 历史
     */
    onHistoryClick(row) {
      this.rowId = row[this.primaryKey];
      this.historyDialog = true;
    },
    /**
     * 导出
     */
    onExportClick() {
      if (this.loading) return;
      let opt = {
        tableId: this.tableId,
      };
      exportBusDataTable(opt);
    },
    /** 排序事件 */
    sortChange({ property, order }) {
      if (order == "asc") {
        this.ascending = true;
        this.sortColumn = property;
      } else if (order == "desc") {
        this.ascending = false;
        this.sortColumn = property;
      }
      this.getList();
    },
    onDetailClick(row) {
      this.rowId = row[this.primaryKey];
      this.$emit("goToDetail", this.rowId, this.fieldConfig);
    },
  }
}
</script>

<style lang="scss" scoped>
.collect-table {
  height: 100%;
  .table-wrap {
    width: 100%;
    // overflow: auto;
    ::v-deep .vxe-table {
      width: 100%;
      background: transparent;
      .vxe-table--body-wrappe {
        background: transparent;
        .vxe-header--row {
          background: transparent;
          color: #fff;
          border: none;
          .th {
            background: transparent;
            color: #56fefe;
          }
        }
      }
    }
  }
  .collect-table__content {
    .main-search-box {
      padding-bottom: 16px;
      ::v-deep .el-input {
        .el-input__inner {
          width: 150px;
        }
      }
      ::v-deep .el-select {
        .el-input__icon {
          line-height: inherit;
        }
      }
      ::v-deep .el-checkbox {
        .el-checkbox__label {
          font-size: 14px;
        }
      }
    }
  }
}
</style>
<style lang="scss">
.collect-table__content {
  .vxe-table--render-default.border--default .vxe-table--header-wrapper,
  .vxe-table--render-default.border--full .vxe-table--header-wrapper,
  .vxe-table--render-default.border--outer .vxe-table--header-wrapper {
    background: rgba(0, 0, 0, 0);
  }
  .vxe-table--render-default.border--default .vxe-header--column {
    background: rgba(0, 0, 0, 0.2);
  }
  .vxe-table--body {
    background: #172d52;
    .vxe-button.type--text {
      color: #56fefe;
    }
  }
  .fixed-right--wrapper {
    .vxe-header--row {
      background: #172d52;
    }
  }
  .vxe-table--render-default .vxe-table--body-wrapper {
    background: transparent;
    color: #fff;
    .vxe-body--row {
      background: transparent;
      color: #fff;
      &.row--hover {
        background-color: rgba(0, 0, 0, 0);
        color: #fff;
        &.row--stripe {
          background-color: rgba(0, 0, 0, 0);
          color: #fff;
        }
      }
    }
  }
  .vxe-table--render-default .vxe-body--row.row--stripe {
    background: transparent;
    color: #fff;
  }
  .vxe-table--render-default.border--default .vxe-table--header-wrapper,
  .vxe-table--render-default.border--full .vxe-table--header-wrapper,
  .vxe-table--render-default.border--outer .vxe-table--header-wrapper,
  .vxe-table--render-default.border--inner .vxe-table--header-wrapper,
  .vxe-table--render-default.border--none .vxe-table--header-wrapper {
    background-image: linear-gradient(90deg, #2c9ebf4d 3%, #2993b41a 100%);
    color: #56fefe;
  }
  .vxe-table .vxe-sort--asc-btn,
  .vxe-table .vxe-sort--desc-btn {
    color: #56fefe;
  }
  .vxe-body--row {
    position: relative;
    &::after {
      content: "";
      position: absolute;
      left: 0;
      bottom: 0;
      width: 100%;
      border-bottom: none;
    }
  }
  .vxe-table ::-webkit-scrollbar-track {
    background-color: rgba(0, 0, 0, 0.2);
  }
  .vxe-table ::-webkit-scrollbar-corner {
    background-color: rgba(0, 0, 0, 0.2);
  }
  .vxe-table ::-webkit-scrollbar-thumb {
    background-color: rgba(125, 125, 125, 0.7);
    border-radius: 5px;
    border: none;
  }
  .vxe-table ::-webkit-scrollbar {
    width: 5px;
    height: 8px;
  }
  .el-pager li.btn-quicknext,
  .el-pager li.btn-quickprev {
    background: transparent;
    color: #fff;
  }
  .el-radio__input.is-checked + .el-radio__label,
  .el-radio__label {
    color: #fff;
  }
}
</style>
