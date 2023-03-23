<template>
  <div class="comprehensive">
    <el-row>
      <el-col :span="advanced ? 24 : 12.5" :class="{ 'justify-none': advanced }">
        <span style="white-space: nowrap">影响温州：</span>
        <el-select style="width: 120px" v-model="affectedWz" @change="baseQuery">
          <el-option label="全部" :value="1" />
          <el-option label="是" :value="2" />
          <el-option label="否" :value="3" />
        </el-select>
      </el-col>
      <el-col v-if="!advanced" :span="11.5">
        <span style="margin-left: 0.375rem">年份:</span>
        <el-select style="width: 120px" v-model="year" clearable @change="baseQuery">
          <el-option
            v-for="(item, index) in yearList"
            :key="index"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
      </el-col>
      <el-col :span="12">
        <span
          @click="advanced = !advanced"
          :class="[advanced ? 'advanced' : 'base', 'query']"
          >{{ advanced ? "高级查询" : "基本查询" }} <i class="el-icon-caret-bottom"
        /></span>
      </el-col>
    </el-row>
    <template v-if="advanced">
      <advanced-form @query="advancedQuery" />
    </template>
    <table-title title="台风列表">
      <template slot="append">
        <el-button type="text" style="color: #fff; padding: 0" @click="clearSelection">
          <i class="icon-delete" style="vertical-align: bottom" />
          删除
        </el-button>
      </template>
    </table-title>
    <div ref="typhoonTable" style="height: 100%">
      <u-table
        v-loading="loading"
        ref="uTable"
        use-virtual
        stripe
        element-loading-background="rgba(0, 0, 0, 0.5)"
        element-loading-text="数据正在加载中"
        element-loading-spinner="el-icon-loading"
        :data="typhoonList"
        :height="typhoonListHeight"
        :row-height="35"
        @selection-change="handleSelectionChange"
        @row-click="rowClick"
      >
        <u-table-column type="selection" />
        <u-table-column label="编号" prop="code">
          <template slot-scope="{ row }">
            <span>{{ row.code }}</span>
            <i
              v-if="row.current"
              class="icon-current-typhoon"
              style="vertical-align: middle"
            />
          </template>
        </u-table-column>
        <u-table-column label="中文名" prop="name" />
        <u-table-column label="英文名" prop="englishName" />
      </u-table>
    </div>
  </div>
</template>
<script>
import { TableTitle } from "../../components.js";
import AdvancedForm from "./AdvancedForm.vue";
import { getTyhoonList } from "@/api/typhoon.js";
import moment from "moment";
export default {
  name: "CompreQuery",
  components: { AdvancedForm, TableTitle },
  // inject from ../index.vue
  inject: ["analysisPage"],
  data() {
    return {
      multipleSelection: [],

      affectedWz: 1,
      year: "",
      yearList: [
        { value: "", label: "全部" },
        ...new Array(100).fill(0).map((item, index) => {
          let value = new Date().getFullYear() - index + "";
          return { value, label: value };
        }),
      ],

      advanced: false,

      // 台风表格
      loading: false,
      typhoonList: [],
      typhoonListHeight: 0,
      currentRow: "",
    };
  },
  watch: {
    multipleSelection: {
      handler(val, oldVal) {
        let newChecked = val
          .filter((item) => !oldVal.includes(item))
          .map((item) => ({ code: item.code, name: item.name }));
        let unChecked = oldVal
          .filter((item) => !val.includes(item))
          .map((item) => ({ code: item.code, name: item.name }));
        this.analysisPage.$emit("selectTyphoon", { newChecked, unChecked });
      },
    },
  },
  mounted() {
    this.fitTableHeight();
    this.baseQuery();
  },
  updated() {
    this.fitTableHeight();
  },
  methods: {
    clearSelection() {
      this.$refs.uTable.clearSelection();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    rowClick(row, column) {
      if (column.type !== "selection") {
        this.analysisPage.$emit("rowClick", { code: row.code, name: row.name });
      }
    },
    fitTableHeight() {
      this.$nextTick(() => {
        this.typhoonListHeight = this.$refs.typhoonTable.offsetHeight - 10;
      });
    },
    baseQuery() {
      this.loading = true;
      let opt = { affectedWz: this.affectedWz };
      if (this.year != "") {
        opt.begin = moment(`${this.year}-01-01`).format("yyyy-MM");
        opt.end = moment(`${this.year}-12-31`).format("yyyy-MM");
      }
      getTyhoonList(opt).then((res) => {
        this.typhoonList = res.data;
        this.loading = false;
      });
    },
    advancedQuery(opt) {
      this.loading = true;
      getTyhoonList({ affectedWz: this.affectedWz, ...opt }).then((res) => {
        this.typhoonList = res.data;
        this.loading = false;
      });
    },
  },
};
</script>
<style lang="scss">
.comprehensive {
  height: 100%;
  font-size: 14px;
  display: flex;
  flex-direction: column;
  color: #fff;
  .plTableBox .el-table__fixed-right::before,
  .plTableBox .el-table__fixed::before {
    background: rgba(0, 0, 0, 0);
  }
  .plTableBox .el-table--border::after,
  .plTableBox .el-table--group::after,
  .plTableBox .el-table::before {
    background: rgba(0, 0, 0, 0);
  }
  .plTableBox .el-table--striped .el-table__body tr.el-table__row--striped td{
    background: rgba(0, 0, 0, 0.2);
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
  .el-col {
    display: flex;
    justify-content: space-between;
    align-items: center;
    &.justify-none {
      justify-content: unset;
    }
  }
  .query {
    cursor: pointer;
    line-height: 32px;
    height: 32px;

    margin-right: 16px;
    > i {
      transition: all 0.3s ease;
    }
    &.base {
      /* margin-left: auto; */
      color: #fff;
    }
    &.advanced {
      color: #56fefe;
      > i {
        transform: rotate(-180deg);
      }
    }
  }
}
.el-table--border,
.el-table--group,
.el-table--border th.gutter:last-of-type,
.el-table--border th,
.el-table__fixed-right-patch {
  border: 0;
}
.el-table--border::after {
  background-color: transparent;
}
</style>
