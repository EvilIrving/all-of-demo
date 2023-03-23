<!--
 * @Author: wqn
 * @Date: 2023-02-14 11:42:01
 * @LastEditors: dtb
 * @LastEditTime: 2023-03-07 17:24:05
 * @Description: 风险管控清单
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
        <el-table-column prop="countryadnm" label="行政区划"></el-table-column>
        <el-table-column label="规模">
          <template slot-scope="scope">
            <span style="color: #29def0;font-weight: bold;">
              {{ scope.row.safeState }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="controlRequire" label="管控要求"></el-table-column>
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
import DialogContent from "./ReservoirDetailedListDialog.vue";
import { getRiskList } from "api/api_utils.js";
export default {
  name: "ReservoirRiskList",
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
    // 行政区划code
    selectAdcd() {
      return this.$store.state.selectAdcd;
    },
  },
  watch: {
    selectAdcd() {
      // 行政区划code改变时，重新获取表格数据
      this.getTableData();
    },
    dialogVisible(value) {
      if (!value) {
        this.getRiskList();
      }
    },
  },
  created() {
    this.getTableData();
  },
  mounted() {
    this.tableHeightResize();
    window.addEventListener("resize", this.tableHeightResize);
  },
  destroyed() {
    window.removeEventListener("resize", this.tableHeightResize);
  },
  methods: {
    /**
     * @description 动态计算表格高度
     */
    tableHeightResize() {
      this.$nextTick(() => {
        this.tableHeight = this.$refs.listBox.clientHeight;
      });
    },
    /**
     * @description 点击生成按钮打开弹窗
     */
    openDialog() {
      this.dialogVisible = true;
    },
    /**
     * @description 获取列表清单数据
     */
    getTableData() {
      this.startTime = "";
      this.tableData = [];
      getRiskList({
        adcd: this.selectAdcd,
        projectCategory: "水库",
        fetchAll: true,
      })
        .then(({ rows }) => {
          this.tableData = rows || [];
        })
        .finally(() => {
          // if (this.tableData.length > 0)
          //   this.startTime = this.tableData[0].createTime ?? "-";
          this.startTime =
            new Date().format("hh") < 20
              ? new Date().format("yyyy-MM-dd ") + "08:00"
              : new Date().format("yyyy-MM-dd ") + "20:00";
        });
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