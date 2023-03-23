<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-06 14:47:55
 * @Description: 知识库管理
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\knowledgeBaseManage\KnowledgeBaseManage.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>知识库管理</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <el-input
        placeholder="请输入名称"
        prefix-icon="el-icon-search"
        @change="getList()"
        v-model="canonicalName"
        style="width: 240px; margin-right: 15px"
        clearable
      />
      <el-button type="primary" v-permission="'knowledgeBaseManage:add'" class="right" @click="editTable('新增', '')"
        >新增</el-button
      >
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column
          prop="canonicalName"
          label="规范名称"
          align="center"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="lookFiles(scope.row.appendixUrl)"
                v-if="scope.row.appendixUrl"
                v-permission="'knowledgeBaseManage:look'"
                >查看</span
              >
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'knowledgeBaseManage:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="delTable(scope.row.id)"
                v-permission="'knowledgeBaseManage:del'"
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
        @pagination="getList"
      />
    </div>
    <el-dialog :title="title" :visible.sync="showEditDialog" width="35%">
      <knowledge-base-manage-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></knowledge-base-manage-form>
    </el-dialog>
    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <look-file
        v-if="fileDialog"
        :url="url"
        @closeEditDialog="closeEditDialog"
      ></look-file>
    </el-dialog>
  </div>
</template>

<script>
import { basMsgZskApi } from "@/api/seawallPrevent";
import LookFile from "@/components/LookFile.vue";
import KnowledgeBaseManageForm from "./KnowledgeBaseManageForm";

export default {
  name: "KnowledgeBaseManage",
  props: {},
  components: {
    KnowledgeBaseManageForm,
    LookFile,
  },
  data() {
    return {
      canonicalName: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      fileDialog: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    lookFiles(url) {
      this.url = url;
      this.fileDialog = true;
    },
    async getList() {
      let res = await basMsgZskApi.list({
        canonicalName: this.canonicalName,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    editTable(title, id) {
      this.id = id;
      this.title = title;
      this.showEditDialog = true;
      this.editDialog = false;
      this.$nextTick(() => {
        this.editDialog = true;
      });
    },
    delTable(id) {
      this.$confirm("确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        basMsgZskApi.del(id).then((res) => {
          if (res.code == 0) {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            this.getList();
          } else {
            this.$message({
              type: "error",
              message: "删除失败！",
            });
          }
        });
      });
    },
    closeEditDialog() {
      this.showEditDialog = false;
      this.editDialog = false;
      this.getList();
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  @include contentBox();
  height: 100%;
}
</style>