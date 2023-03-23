<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-27 10:24:27
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\projectPlan\ProjectPlan.vue
-->
<template>
  <div >
    <!---列表--->
    <div class="wrap" v-if="detailType">
      <div class="item_box_title">
        <p>预案管理</p>
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
          <span style="font-size: 14px">预案类型：</span>
          <el-select
            style="width: 180px; margin: 0 20px 0 15px"
            v-model="processWay"
            placeholder="请选择"
            clearable
            @change="search"
          >
            <el-option label="应急预案" value="应急预案"> </el-option>
            <el-option label="控运计划" value="控运计划"> </el-option>
            <el-option label="调度方案" value="调度方案"> </el-option>
            <el-option label="管理手册" value="管理手册"> </el-option>
            <el-option label="放水预警方案" value="放水预警方案"> </el-option>
          </el-select>
          <span style="font-size: 14px">审批状态：</span>
          <el-select
            style="width: 180px; margin: 0 20px 0 15px"
            v-model="source"
            clearable
            @change="search"
          >
            <el-option label="全部" value="全部"> </el-option>
            <el-option label="待审批" value="待审批"> </el-option>
            <el-option label="已通过" value="已通过"> </el-option>
            <el-option label="已驳回" value="已驳回"> </el-option>
          </el-select>
        </div>
        <div class="search_right">
          <el-button type="primary" @click="editTable('新增')">新增</el-button>
        </div>
      </div>
      <div class="statistics-box">
        <span class="el-icon-warning"></span>
        <span>申请总数</span>
        <span class="blue">51</span>
        <span>待审批</span>
        <span class="orange">51</span>
        <span>已通过</span>
        <span class="blue">51</span>
        <span>已驳回</span>
        <span class="red">51</span>
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
            prop="principal"
            label="时间"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="issueMan"
            label="工程名称"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="issueDate"
            label="预案类型"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="issueDate"
            label="编制单位"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="issueDate"
            label="审批单位"
            align="center"
          ></el-table-column>
          <el-table-column prop="file" label="详情" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.fileIds">
                <div
                  v-for="item in scope.row.files"
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
            prop="issueDate"
            label="发起时间"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="issueDate"
            label="审核状态"
            align="center"
          ></el-table-column>
          <el-table-column align="center" label="操作" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <span
                  class="deal_btn deal_btn_look"
                  @click="editTable('编辑', scope.row.id)"
                  >查看</span
                >
                <span
                  class="deal_btn deal_btn_del"
                  @click="removeTable(scope.row.id)"
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
        />
      </div>
    </div>
    <ProjectPlanDeatil v-else />
    <el-dialog
      :title="title"
      width="900px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
    >
      <project-plan-from
        :options="options"
        @close="closeDialog"
        :id="id"
        v-if="visibleDialog"
      />
    </el-dialog>
    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <look-file v-if="fileDialog" :url="url"></look-file>
    </el-dialog>
  </div>
</template>

<script>
import { operation, usersApi } from "@/api/projectUnitDetail.js";
import ProjectPlanFrom from "./ProjectPlanFrom";
import LookFile from "@/components/LookFile.vue";
import { exportOverhaul } from "@/utils/export.js";
import ProjectPlanDeatil from './ProjectPlanDeatil.vue'
export default {
  data() {
    return {
      dispatchYear: "",
      dispatchName: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      title: "新增",
      visibleDialog: false,
      options: [],
      fileDialog: false,
      url: "",
      detailType: false
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  components: { ProjectPlanFrom, LookFile, ProjectPlanDeatil },
  mounted() {
    this.loadPageOverhaul();
    this.getUserList();
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
        name: this.dispatchName,
        pid: this.projectDetail.prcd,
        type: 2,
        date: this.dispatchYear,
      };
      exportOverhaul(obj);
    },
    removeTable(id) {
      this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          operation
            .delOverhaul({
              id,
              type: 2,
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
      let res = await operation.loadPageOverhaul({
        pid: this.projectDetail.prcd,
        type: 2,
        date: this.dispatchYear,
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        name: this.dispatchName,
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
    async getUserList() {
      let res = await usersApi({ fetchAll: true });
      this.options = res.list.map((item) => {
        return {
          label: item.username,
          value: item.id,
        };
      });
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
  .statistics-box {
    margin: 0px 16px 0px 24px;
    height: 32px;
    background-color: rgba(20, 146, 255, 0.08);
    padding-left: 8px;
    font-size: 14px;
    color: #666666;
    display: flex;
    align-items: center;
    > span:nth-child(1) {
      color: #1492ff;
      font-size: 20px;
    }
    > span:nth-child(2) {
      padding-left: 8px;
    }
    .blue {
      color: #1492ff;
      font-size: 16px;
      margin: 0 48px 0 15px;
    }
    .orange {
      color: #feaf37;
      font-size: 16px;
      margin: 0 48px 0 15px;
    }
    .red {
      color: #ff0000;
      font-size: 16px;
      margin: 0 48px 0 15px;
    }
  }
}
</style>