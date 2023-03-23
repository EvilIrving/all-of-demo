<template>
  <div class="typhoon-search" ref="typhoonSearchSide">
    <!-- 台风查询 -->
    <div class="statistics-chart" ref="statisticsChart">
      <el-input :value="typhoonName" placeholder="请输入关键字查询"></el-input>
      <el-row class="search-row">
        <el-col :span="4">
          <span>日期:</span>
        </el-col>
        <el-col :span="20">
          <el-date-picker
            v-model="searchData"
            type="daterange"
            value-format="yyyy-MM-dd"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          >
          </el-date-picker>
        </el-col>
      </el-row>
      <el-row class="search-row">
        <el-col :span="4">
          <span>等级:</span>
        </el-col>
        <el-col :span="20">
          <el-date-picker
            v-model="searchLevel"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          >
          </el-date-picker>
        </el-col>
      </el-row>
      <div class="search-btn">
        <el-button @click="getTyphoons" type="primary">查询</el-button>
      </div>
    </div>
    <!-- 查询结果 -->
    <div class="statistics-list" ref="statisticsList" :style="listStyle">
      <div class="catalog-title" ref="catalogTitle">
        <div class="title-left">
          <i class="icon-judge-icon"></i>
          <span>查询结果</span>
        </div>
      </div>
      <div class="search-area" ref="searchArea">
        <div class="left-area">
          <el-input
            @input="getTyphoons"
            v-model="searchVal"
            placeholder="请输入关键字查询"
          >
            <template #prepend>
              <el-button slot="append" icon="el-icon-search"></el-button>
            </template>
          </el-input>
        </div>
        <div class="right-area">
          <span>查看详表</span>
        </div>
      </div>
      <div
        v-loading="loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        class="table-list"
        :style="tableListStyle"
      >
        <screen-table
          :columnList="tableColumns"
          :dataList="dataList"
          @rowClick="rowClick"
          :tableHeight="tableHeight"
        ></screen-table>
        <!-- <primary-map-table 
          :columnList="tableColumns"
          :dataList="dataList"
          @rowClick="rowClick"
          :paginationFlag="true"
        ></primary-map-table> -->
      </div>
    </div>
  </div>
</template>

<script>
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import { ScreenTable, PrimaryMapTable } from "@/components/";
export default {
  name: "typhoon-search",
  data() {
    return {
      searchVal: "",
      tableHeight: 0,
      tableColumns: [
        {
          prop: "name",
          label: "名称",
        },
        {
          prop: "beginTime",
          label: "登录时间",
          sortable: true,
        },
      ],
      dataList: [],
      tableListStyle: {}, //表格样式
      listStyle: {}, //表格父容器样式
      typhoonName: "",
      searchData: "",
      searchLevel: "",
      loading: false,
    };
  },
  components: {
    ScreenTable,
    PrimaryMapTable,
  },
  mounted() {
    this.calcDomHeight();
    this.calcTableHeight();
    this.getTyphoons();
  },
  computed: {
    mapObj() {
      return this.$store.state.hydraulicAmap.mapComponent;
    },
  },
  methods: {
    //计算表格父容器高度
    calcDomHeight() {
      this.$nextTick(() => {
        const height =
          this.$refs.typhoonSearchSide.offsetHeight -
          this.$refs.statisticsChart.offsetHeight +
          "px";
        this.listStyle = {
          height,
        };
      });
    },
    //计算表格高度
    calcTableHeight() {
      this.$nextTick(() => {
        this.tableHeight =
          this.$refs.statisticsList.offsetHeight -
          this.$refs.catalogTitle.offsetHeight -
          this.$refs.searchArea.offsetHeight;
        this.tableListStyle = {
          height: this.tableHeight + "px",
        };
      });
    },
    rowClick(data) {
      this.$parent.showTyphoonDetail(data.code);
      this.getTyphoonDetail(data.code);
    },
    //获取台风列表
    async getTyphoons() {
      let begin = "",
        end = "";
      let minLandingLevel = "",
        maxLandingLevel = "";
      if (this.searchData.length > 0) {
        begin = this.searchData[0];
        end = this.searchData[1];
      }
      this.loading = true;
      let opt = {
        name: this.searchVal,
        begin,
        end,
        minLandingLevel,
        maxLandingLevel,
      };
      let res = await hydraulicApi.getTyphoons(opt);
      this.loading = false;
      this.dataList = res.data;
    },
    //根据code查台风详情
    async getTyphoonDetail(code) {
      let res = await hydraulicApi.getTyphoonDetail(code);
      if (res.code == 0) {
        this.mapObj.showTyphoon(res.data);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
.typhoon-search {
  width: 100%;
  height: 100%;
  @include flexbox;
  @include flexflow(column, nowrap);
  .statistics-chart {
    .search-row {
      @include flexbox;
      @include flexAC;
      @include flexflow(row, nowrap);
      color: #fff;
      margin-bottom: 10px;
      ::v-deep .el-date-editor {
        border: 1px solid #3f9bfc;
        width: 100%;
        border-radius: 2px;
        height: 32px;
        line-height: 32px;
        .el-range__icon {
          line-height: 16px;
        }
        .el-range-input {
          background: rgba(0, 0, 0, 0);
          color: #fff;
        }
      }
    }
    .search-btn {
      padding: 8px 0 16px 0;
      ::v-deep .el-button {
        width: 100%;
      }
    }
  }
  .statistics-list {
    @include flex(1);
    .table-list {
      height: 100%;
      width: 100%;
    }
  }
}
</style>