<template>
  <div class="similar-analysis">
    <span>请选择要分析的台风</span>
    <div style="margin: 10px 0">
      <el-select v-model="year" style="width: 150px" @change="onYearChange">
        <el-option
          v-for="(item, index) in yearList"
          :value="item.value"
          :label="item.value"
          :key="index"
        />
      </el-select>
      <el-select v-model="code" style="width: 150px; margin-left: 20px">
        <el-option
          v-for="(item, index) in codeList"
          :value="item.value"
          :label="item.label"
          :key="index"
        />
      </el-select>
    </div>
    <span>请选择分析类型</span>
    <div style="margin: 10px 0">
      <el-radio-group v-model="type">
        <el-radio label="path">路径相似</el-radio>
        <el-radio label="born_land">季节相似</el-radio>
        <el-radio label="season">生成地相似</el-radio>
      </el-radio-group>
    </div>
    <div class="action-slot">
      <div class="query-btn" @click="querySimilarTyhpoon">
        <span>分析</span>
      </div>
    </div>
    <table-title title="相似台风列表">
      <template #append>
        <el-button type="text" style="color: #fff; padding: 0" @click="clearSelection">
          <i class="icon-delete" style="vertical-align: bottom" />
          删除
        </el-button>
      </template>
    </table-title>
    <div ref="typhoonTable" style="height: 100%">
      <u-table
        v-loading="typhoonListLoading"
        ref="uTable"
        use-virtual
        stripe
        element-loading-background="rgba(0, 0, 0, 0.5)"
        element-loading-text="数据正在加载中"
        element-loading-spinner="el-icon-loading"
        :data="typhoonList"
        :row-height="35"
        :height="typhoonListHeight"
        showBodyOverflow="title"
        showHeaderOverflow="title"
        @selection-change="handleSelectionChange"
        @row-click="rowClick"
      >
        <u-table-column type="selection" />
        <u-table-column label="编号" prop="tfbh" />
        <u-table-column label="中文名" prop="name" />
        <u-table-column label="英文名" prop="ename" />
      </u-table>
    </div>
  </div>
</template>
<script>
import { getHistoryTyphoons, getSimilarTyphoons } from "@/api/typhoon.js";
import { TableTitle } from "../../components.js";
export default {
  name: "SimilarAnalysis",
  components: { TableTitle },
  // inject from ../index.vue
  inject: ["analysisPage"],
  data() {
    return {
      multipleSelection: [],

      year: "",
      yearList: new Array(100).fill(0).map((item, index) => {
        let curYear = new Date().getFullYear();
        return { value: curYear - index + "" };
      }),

      code: "",
      codeList: "",
      type: "path",

      codeListLoading: false,

      typhoonListLoading: false,
      typhoonList: [],
      typhoonListHeight: 0,
    };
  },
  mounted() {
    this.fitTableHeight();
  },
  updated() {
    this.fitTableHeight();
  },
  watch: {
    multipleSelection: {
      handler(val, oldVal) {
        let newChecked = val
          .filter((item) => !oldVal.includes(item))
          .map((item) => ({ code: item.tfbh, name: item.name }));
        let unChecked = oldVal
          .filter((item) => !val.includes(item))
          .map((item) => ({ code: item.tfbh, name: item.name }));
        this.analysisPage.$emit("selectTyphoon", { newChecked, unChecked });
      },
    },
  },
  methods: {
    clearSelection() {
      this.$refs.uTable.clearSelection();
    },
    fitTableHeight() {
      this.$nextTick(() => {
        this.typhoonListHeight = this.$refs.typhoonTable.offsetHeight - 10;
      });
    },
    rowClick(row, column) {
      if (column.type !== "selection") {
        this.analysisPage.$emit("rowClick", { code: row.tfbh, name: row.name });
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    onYearChange(year) {
      this.codeListLoading = true;
      getHistoryTyphoons({ affectedWz:1,begin: `${year}-01`, end: `${year}-12` }).then((res) => {
        if (res.code == 0) {
          this.codeList = res.data.map((item) => ({
            value: item.code,
            label: item.name,
          }));
          this.code = this.codeList[0]?.value || "";
        }
        this.codeListLoading = false;
      });
    },
    querySimilarTyhpoon() {
      this.typhoonListLoading = true;
      let { code, type } = this;
      getSimilarTyphoons({ code, type }).then((res) => {
        if (res.code == 0) {
          this.typhoonList = res.data.data;
          this.typhoonListLoading = false;
        }
      });
    },
  },
};
</script>
<style lang="scss">
.similar-analysis {
  height: 100%;
  display: flex;
  flex-direction: column;
  .action-slot {
    width: 100%;
    margin-top: 16px;
    margin-bottom: 18px;
  }
  .query-btn {
    margin: 0 auto;
    cursor: pointer;
    width: 84px;
    height: 34px;
    background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
    border-radius: 4px;
    text-align: center;
    line-height: 34px;
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
