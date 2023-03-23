<!--
 * @Author: wqn
 * @Date: 2023-02-14 11:42:01
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-20 17:43:23
 * @Description: 海塘风险管控清单
-->
<template>
  <ModuleLayout title="风险管控清单" disabled>
    <div slot="extra">
      <span class="extra-time">{{startTime}}</span>
      <el-button type="primary" size="mini" @click="openDialog">生成</el-button>
    </div>
    <div class='seawall-detailed-list' ref="listBox">
      <el-table class="detailed-list" :data="tableData" style="width: 100%" :height="tableHeight">
        <el-table-column prop="proname" label="名称"></el-table-column>
        <el-table-column label="行政区划" align="center">
          <template slot-scope="scope">
            <div>
              {{ scope.row.cityadnm }}-{{ scope.row.countryadnm }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="规模">
          <template slot-scope="scope">
            <span style="color: #29def0;font-weight: bold;">
              {{ scope.row.projectScale }}级海塘
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="safeState" label="管控要求"></el-table-column>
        <el-table-column label="落实情况">
          <template slot-scope="scope">
            <span :style="{color: scope.row.content === '已落实' ? '#36da5b' : '#d96c39', fontWeight: 'bold', fontFamily: 'youshebiaotihei'}">
              {{ scope.row.content }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <list-details-dialog v-model="dialogVisible">
      <DialogContent />
    </list-details-dialog>
  </ModuleLayout>
</template>

<script>
import DialogContent from "./SeawallDetailedListDialog.vue";
import { getRiskList } from "api/api_utils";
export default {
  name: "SeawallRiskList",
  props: {},
  components: {
    DialogContent,
  },
  data() {
    return {
      tableData: [],
      tableHeight: 0,
      startTime: "",
      dialogVisible: false,
    };
  },
  computed: {
    selectAdcds() {
      return this.$store.state.selectAdcd;
    },
  },
  watch: {
    selectAdcd() {
      this.getRiskList();
    },
    dialogVisible(value) {
      if (!value) {
        this.getRiskList();
      }
    },
  },
  created() {
    this.getRiskList();
  },
  mounted() {
    this.tableHeightResize();
    window.addEventListener("resize", this.tableHeightResize);
  },
  destroyed() {
    window.removeEventListener("resize", this.tableHeightResize);
  },
  methods: {
    tableHeightResize() {
      this.$nextTick(() => {
        this.tableHeight = this.$refs.listBox.clientHeight;
      });
    },
    openDialog() {
      this.dialogVisible = true;
    },
    async getRiskList() {
      this.startTime = "";
      this.tableData = [];
      let res = await getRiskList({
        adcd: this.selectAdcds,
        projectCategory: "海塘",
      });
      if (res.success && res.rows.length) this.tableData = res.rows;
      // if (this.tableData.length > 0)
      //   this.startTime = this.tableData[0].createTime ?? "-";
      this.startTime =
        new Date().format("hh") < 20
          ? new Date().format("yyyy-MM-dd ") + "08:00"
          : new Date().format("yyyy-MM-dd ") + "20:00";
    },
  },
};
</script>

<style lang='scss' scoped>
.extra-time {
  font-size: 14px;
  color: #b0dbfe;
  opacity: 0.9;
  margin-right: 10px;
}
.seawall-detailed-list {
  width: 100%;
  height: 100%;
  padding: 20px 10px 0 20px;
}
</style>