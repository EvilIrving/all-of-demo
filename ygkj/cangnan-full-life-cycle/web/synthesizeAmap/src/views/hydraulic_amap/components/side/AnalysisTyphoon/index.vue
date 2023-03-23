<template>
  <div class="analysis-typhoon">
    <el-tabs v-model="activeTab" type="card" stretch class="analysis-typhoon">
      <el-tab-pane name="comprehensive" lazy label="综合查询">
        <compre-query />
      </el-tab-pane>
      <el-tab-pane name="similar" lazy label="相似分析">
        <similar-analysis />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import CompreQuery from "./CompreQuery/index.vue";
import SimilarAnalysis from "./SimilarAnalysis/index.vue";
export default {
  name: "AnalysisTypoon",
  components: { CompreQuery, SimilarAnalysis },
  provide() {
    return {
      analysisPage: this,
    };
  },
  data() {
    return {
      activeTab: "comprehensive",
    };
  },
};
</script>
<style lang="scss" scoped>
.analysis-typhoon {
  // margin-top: 16px;
  height: 100%;
  @include flexbox;
  @include flexflow(column,nowrap);
  ::v-deep .el-tabs__nav {
    border: none;
    .el-tabs__item{
      color: #fff;
      border: 1px solid #fff;
      &.is-active{
        color: #56fefe;
        box-shadow: inset 0 0 0.3125rem 0 #56fefe;
        background: rgba(72,152,251,.10196078431372549);
        border: 1px solid #56fefe;
      }
    }
  }
  ::v-deep .el-tabs__content{
    height: 100%;
    color: #fff;
    .el-tab-pane{
      height: 100%;
    }
  }
}
</style>
