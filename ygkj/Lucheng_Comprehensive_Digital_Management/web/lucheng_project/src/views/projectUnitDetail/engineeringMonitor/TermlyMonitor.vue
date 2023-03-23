<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-07-06 16:15:02
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\engineeringMonitor\TermlyMonitor.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>定期监测</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <span style="font-size: 14px">名称：</span>
        <el-input
          placeholder="请输入"
          v-model="dispatchName"
          @change="search"
          clearable
          style="width: 180px; margin: 0 20px 0 15px"
        />
      </div>
      <div class="search_right">
        <el-button @click="exportsData" v-permission="'termlyMonitor:export'">导出</el-button>
        <el-button type="primary" v-permission="'termlyMonitor:add'" @click="editTable('新增')">新增</el-button>
      </div>
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <!-- <el-table-column type="selection" width="50"> </el-table-column> -->
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column
          prop="year"
          label="年份"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="monitorDate"
          label="监测日期"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="monitorUnit"
          label="监测单位"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="monitorType"
          label="监测类型"
          align="center"
        ></el-table-column>
        <el-table-column prop="file" label="安全监测分析报告" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.analyzeReport">
              <div
                v-for="item in scope.row.analyzeReportList"
                :key="item.id"
                @click="preview(item.fileUrl)"
              >
                <i
                  class="el-icon-link"
                  style="color: #409eff; cursor: pointer"
                ></i>
              </div>
            </div>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column prop="file" label="安全监测整编报告" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.reorganizationReport">
              <div
                v-for="item in scope.row.reorganizationReportList"
                :key="item.id"
                @click="preview(item.fileUrl)"
              >
                <i
                  class="el-icon-link"
                  style="color: #409eff; cursor: pointer"
                ></i>
              </div>
            </div>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="compared"
          label="与前期对比是否异常"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="manager"
          label="水库管理单位负责人"
          align="center"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'termlyMonitor:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="removeTable(scope.row.id)"
                v-permission="'termlyMonitor:del'"
                >删除</span
              >
            </div>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="pageTotal"
        :page.sync="pageNum"
        :limit.sync="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        @pagination="loadPageOverhaul"
      />
    </div>
    <el-dialog
      :title="title"
      width="980px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
    >
      <termly-monitor-from @close="closeDialog" :id="id" v-if="visibleDialog" />
    </el-dialog>
    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <look-file v-if="fileDialog" :url="url"></look-file>
    </el-dialog>
  </div>
</template>

<script>
import { regularMonitor } from "@/api/propertyManage.js";
import TermlyMonitorFrom from "./TermlyMonitorFrom";
import LookFile from "@/components/LookFile.vue";
import { regularMonitorExport } from "@/utils/export.js";
export default {
  data() {
    return {
      dispatchName: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      title: "新增",
      visibleDialog: false,
      fileDialog: false,
      url: "",
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  components: { TermlyMonitorFrom, LookFile },
  mounted() {
    this.loadPageOverhaul();
  },
  methods: {
    preview(url) {
      this.url = url;
      this.fileDialog = true;
    },
    handleClose() {
      this.visibleDialog = false;
    },
    search() {
      this.pageNum = 1;
      this.loadPageOverhaul();
    },
    exportsData() {
      let obj = {
        pid: this.projectDetail.prcd,
        name: this.dispatchName,
      };
      regularMonitorExport(obj);
    },
    removeTable(id) {
      this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          regularMonitor
            .del({
              id,
            })
            .then((res) => {
              if (res.code == 0) {
                this.$message.success("删除成功");
                this.loadPageOverhaul();
              } else {
                this.$message.error(res.message);
              }
            })
            .catch(() => {
              this.$message.error(res.message);
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    async loadPageOverhaul() {
      let res = await regularMonitor.list({
        pid: this.projectDetail.prcd,
        name: this.dispatchName,
        pageSize: this.pageSize,
        pageNum: this.pageNum,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    editTable(title, id = "") {
      this.title = title;
      this.id = id;
      this.visibleDialog = true;
    },
    closeDialog(data) {
      if (data) {
        this.pageNum = 1;
        this.loadPageOverhaul();
      }
      this.handleClose();
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .list_search_box {
    @include flexbox;
    @include flexAI(center);
    @include flexJC(space-between);
    padding: 25px 16px 16px;
  }
}
</style>