<!--
 * @Date: 2023-02-16 10:51:13
 * @Author: 
 * @Description: 海塘风险管控清单详情弹窗的内容
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-20 15:52:52
 * @FilePath: \proyp-screen\src\views\seawall\cpns\SeawallDetailedListDialog.vue
-->
<template>
  <div class='dialog-content'>
    <div class="filter-box">
      <list-dialog-admn-select v-model="adcd" @change="changeAdcd" />
      <!-- <div class="risk-select-box">
        <div class="label">存在风险：</div>
        <list-dialog-risk-type-radio v-model="risk" />
      </div> -->
      <list-details-export-button @click="oneClickExport" />
    </div>
    <div class="title">海塘重点风险管控清单</div>
    <div class="table-box" ref="tableBox">
      <el-table v-loading="loading" class="dialog-detailed-list" :data="tableData" style="width: 100%" :height="tableHeight" border :empty-text="emptyText">
        <el-table-column type="index" label="序号" width="80" align="center"></el-table-column>
        <el-table-column prop="cityadnm" label="设区市" align="center"></el-table-column>
        <el-table-column prop="countryadnm" label="县(区、市)" align="center"></el-table-column>
        <el-table-column prop="proname" label="工程名称" align="center"></el-table-column>
        <!-- <el-table-column label="风险底数(项目名称)" header-align="center">
          <template slot-scope="scope">
            <div>
              {{ scope.row.riskCont +  scope.row.risk + '座' }}
            </div>
          </template>
        </el-table-column> -->
        <el-table-column prop="riskType" label="风险类型" align="center"></el-table-column>
        <el-table-column prop="controlRequire" label="管控要求" align="center"></el-table-column>
        <el-table-column prop="unit" label="责任部门" align="center"></el-table-column>
        <el-table-column prop="state" label="落实情况" header-align="center"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { getNewGerneRiskList } from "api/api_utils";
export default {
  name: "SeawallDetailedListDialog",
  props: {},
  components: {},
  data() {
    return {
      adcd: this.$localData("get", "userInfo").adcd,
      risk: "",
      tableData: [],
      tableHeight: 0,
      loading: false,
      emptyText: "加载中...",
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getGerneRiskList();
    this.tableHeightResize();
    window.addEventListener("resize", this.tableHeightResize);
  },
  destroyed() {
    window.removeEventListener("resize", this.tableHeightResize);
  },
  methods: {
    changeAdcd(adcd) {
      this.adcd = adcd;
      this.getGerneRiskList(adcd);
    },
    async getGerneRiskList(adcd = "") {
      this.emptyText = "加载中...";
      this.loading = true;
      let res = await getNewGerneRiskList({
        adcd: adcd || this.adcd,
        // startTime: new Date().format('yyyy-MM-dd'),
        projectCategory: "海塘",
        fetchAll: true,
      });
      this.loading = false;
      this.emptyText = "暂无数据";
      if (res.success && res.rows.length) return (this.tableData = res.rows);
      return (this.tableData = []);
    },
    oneClickExport() {
      let token = this.$localData("get", "token");
      window.open(
        this.$config.host +
          `/mgt/temp/proriskcolse/toExcel` +
          `?startTime=${this.startTime}` +
          `&projectCategory=海塘` +
          `&adcd=${this.adcd || ""}` +
          `&token=${token}`
      );
    },
    tableHeightResize() {
      this.$nextTick(() => {
        this.tableHeight = this.$refs.tableBox.clientHeight;
      });
    },
  },
};
</script>
<style lang='scss' scoped>
.dialog-content {
  width: 100%;
  height: 100%;
  padding: 14px 38px 58px 31px;
  .filter-box {
    display: flex;
    justify-content: space-between;
    .risk-select-box {
      display: flex;
      align-items: center;
      flex: 1;
      height: 32px;
      padding-left: 61px;
      .label {
        color: #5cdbf2;
        font-size: 14px;
        margin-right: 6px;
      }
    }
  }
  .title {
    width: 100%;
    height: 66px;
    margin: 24px 0 15px;
    background-image: linear-gradient(to bottom, #083465, #091e49);
    line-height: 62px;
    text-align: center;
    font-size: 30px;
    font-weight: bolder;
    letter-spacing: 1px;
  }
  .table-box {
    width: 100%;
    height: calc(100% - 32px - 24px - 66px - 15px);
  }
}
</style>