<!--
 * @Author: wanxin
 * @LastEditTime: 2022-10-24 14:49:17
 * @Descripttion: 审批项目管理
 * @FilePath: \lucheng_project\src\views\occupyApprove\approval-project\ApprovalProject.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>审批项目管理</p>
    </div>
    <div class="tb_wrapper">
      <el-tabs v-model="archive" @tab-click="handleClick">
        <el-tab-pane label="待归档" name="0"></el-tab-pane>
        <el-tab-pane label="已归档" name="1"></el-tab-pane>
      </el-tabs>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <span style="font-size: 14px">名称：</span>
        <el-input
          placeholder="请输入"
          v-model="name"
          @change="getList"
          clearable
          style="width: 180px; margin: 0 20px 0 0"
        />
        <span style="font-size: 14px">年份：</span>
        <!-- <el-date-picker
          @change="getList"
          v-model="year"
          value-format="yyyy"
          type="year"
          placeholder="请选择年份"
        >
        </el-date-picker> -->
        <el-date-picker
          v-model="year"
          @change="getList"
          value-format="yyyy"
          type="year"
          placeholder="请选择年份"
        >
        </el-date-picker>
      </div>
      <div class="search_right">
        <el-button
          type="primary"
          @click="exportTable"
          v-permission="'approval-project:export'"
          >导出</el-button
        >
        <!-- <el-button type="primary" @click="editTable('新增')">新增</el-button> -->
      </div>
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <!-- <el-table-column type="selection" width="50"> </el-table-column> -->
        <el-table-column
          prop="projectCode"
          align="center"
          label="项目编码"
        ></el-table-column>
        <el-table-column
          prop="projectName"
          label="项目名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="enterpriseName"
          label="单位名称"
          align="center"
        ></el-table-column>
        <el-table-column label="涉河水域" prop="wadingWaters" align="center">
        </el-table-column>
        <el-table-column
          prop="occupiedWaterArea"
          label="占用水域面积㎡"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="approvalNumber" label="批复文号" align="center">
        </el-table-column>
        <el-table-column prop="wadingTime" label="涉河时长" align="center">
        </el-table-column>
        <el-table-column prop="principalName" label="联系人" align="center">
        </el-table-column>
        <el-table-column prop="principalTel" label="联系电话" align="center">
        </el-table-column>
        <el-table-column
          prop="applyDate"
          label="受理日期"
          align="center"
          width="160"
        >
        </el-table-column>
        <el-table-column label="项目建设" align="center">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('查看', scope.row.projectCode, 2)"
                v-permission="'approval-project:look'"
                >查看</span
              >
              <span
                class="deal_btn deal_btn_edit"
                @click="editTable('编辑', scope.row.projectCode, 2)"
                v-permission="'approval-project:edit'"
                >编辑</span
              >
              <!-- <span class="deal_btn deal_btn_del" @click="del(scope.row.id)">删除</span> -->
            </div>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="140">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('查看', scope.row.projectCode, 1)"
                v-permission="'approval-project:look'"
                >查看</span
              >
              <span
                class="deal_btn deal_btn_edit"
                @click="editTable('编辑', scope.row.projectCode, 1)"
                v-permission="'approval-project:edit'"
                >编辑</span
              >
              <!-- <span class="deal_btn deal_btn_del" @click="del(scope.row.id)">删除</span> -->
            </div>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="pageTotal"
        :page.sync="pageNum"
        :limit.sync="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        @pagination="getList"
      />
    </div>
    <el-dialog
      :title="title"
      width="900px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <approval-project-from
        @close="closeDialog"
        :id="id"
        :isLook="isLook"
        v-if="approvalVisibleDialog"
      />
      <project-construction-form
        v-if="projectVisibleDialog"
        @close="closeDialog"
        :id="id"
        :isLook="isLook"
      ></project-construction-form>
    </el-dialog>
  </div>
</template>

<script>
import ApprovalProjectFrom from "./components/ApprovalProjectFrom.vue";
import ProjectConstructionForm from "./components/ProjectConstructionForm.vue";
import { approvalProject } from "@/api/projectSupervision";
import { approvalProjectExport } from "@/utils/export.js";
export default {
  name: "ApprovalProject",
  data() {
    return {
      name: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      visibleDialog: false,
      id: "",
      title: "新增",
      isLook: false,
      year: "",
      archive: "0",
      projectVisibleDialog: false,
      approvalVisibleDialog: false,
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  components: {
    ApprovalProjectFrom,
    ProjectConstructionForm,
  },
  mounted() {
    this.getList();
  },
  methods: {
    exportTable() {
      let obj = {
        key: this.name,
        archive: this.archive,
        year: this.year,
      };
      approvalProjectExport(obj);
    },
    handleClick() {
      this.name = "";
      this.year = "";
      this.pageNum = 1;
      this.getList();
    },
    async getList() {
      await approvalProject
        .list({
          key: this.name,
          archive: this.archive,
          year: this.year,
          pageSize: this.pageSize,
          pageNum: this.pageNum,
        })
        .then((res) => {
          if (res.code === 0) {
            this.tableData = res.data.list;
            this.pageTotal = res.data.totalRows;
          }
        });
    },
    handleClose() {
      this.visibleDialog = false;
      this.projectVisibleDialog = false;
      this.approvalVisibleDialog = false;
    },
    closeDialog() {
      this.visibleDialog = false;
      this.projectVisibleDialog = false;
      this.approvalVisibleDialog = false;
      this.getList();
    },
    editTable(title, id, type) {
      this.id = id;
      this.title = title;
      this.isLook = title == "查看";
      this.visibleDialog = true;
      this.$nextTick(() => {
        if (type == 1) {
          this.approvalVisibleDialog = true;
        } else {
          this.projectVisibleDialog = true;
        }
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
  .point {
    display: inline-block;
    border-radius: 50%;
    width: 6px;
    height: 6px;
    background: #01a59c;
    vertical-align: middle;
    margin-right: 7px;
    &.blue {
      background: #1492ff;
    }
    &.red {
      background: #ff0000;
    }
  }
  .lookPic {
    display: inline-block;
  }
  .search-right {
    text-align: right;
    padding: 25px 16px 16px;
  }
}
.popover_title {
  span {
    float: right;
  }
}
.popover_content {
  background: rgba(216, 216, 216, 0.19);
  border-radius: 2px;
}
::v-deep .el-dialog__body {
  //   padding: 0;
}
</style>