<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-07-06 15:59:46
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\operation\recondition\Mechanical.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>机械操作</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <!-- <span style="font-size: 14px">名称：</span>
        <el-input
          placeholder="请输入"
          v-model="dispatchName"
          @change="search"
          clearable
          style="width: 180px; margin: 0 20px 0 15px"
        />
        <span style="font-size: 14px">试运行时间：</span>
        <el-date-picker
          @change="search"
          v-model="dispatchYear"
          value-format="yyyy-MM-dd HH:mm:ss"
          clearable
          type="datetime"
          placeholder="请选择"
          style="width: 200px; margin: 0 20px 0 15px"
        >
        </el-date-picker> -->
      </div>
      <div class="search_right">
        <el-button @click="exportsData" v-permission="'mechanical:export'">导出</el-button>
        <el-button type="primary" @click="editTable('新增')" v-permission="'mechanical:add'">新增</el-button>
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
          prop="principal"
          label="工作负责人"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="issueMan"
          label="签发负责人"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="issueDate"
          label="签发日期"
          align="center"
        ></el-table-column>
        <el-table-column prop="file" label="上传附件" align="center">
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
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                v-permission="'mechanical:edit'"
                @click="editTable('编辑', scope.row.id)"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                v-permission="'mechanical:del'"
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
        @pagination="loadPageOverhaul"
      />
    </div>
    <el-dialog
      :title="title"
      width="900px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
    >
      <mechanical-from
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
import mechanicalFrom from "./mechanicalFrom";
import LookFile from "@/components/LookFile.vue";
import { exportOverhaul } from "@/utils/export.js";
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
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  components: { mechanicalFrom, LookFile },
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
}
</style>