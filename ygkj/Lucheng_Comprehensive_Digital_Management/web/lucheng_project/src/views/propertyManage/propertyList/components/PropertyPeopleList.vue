<!--
 * @Author: hanyu
 * @LastEditTime: 2022-08-16 11:19:18
 * @Description: 物业管理-人员列表
 * @FilePath: \lucheng_project\src\views\propertyManage\propertyList\components\PropertyPeopleList.vue
-->
<template>
  <div class="dialog_wrap">
    <div
      class="info_option"
      style="
        width: 100%;
        padding: 16px 16px 0 0;
        box-sizing: border-box;
        text-align: right;
      "
    >
      <el-button type="primary" @click="editTable('新增', '')">新增</el-button>
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
          prop="post"
          label="岗位"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="username"
          label="姓名"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="phone"
          label="手机"
          align="center"
        ></el-table-column>
        <!-- <el-table-column
          prop="password"
          label="登录密码"
          align="center"
        ></el-table-column> -->
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                >修改密码</span
              >
              <span class="deal_btn deal_btn_del" @click="delCard(scope.row.id)"
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
        :page-sizes="[10, 20, 30, 40]"
        @pagination="getList"
      />
    </div>
    <el-dialog
      :title="title"
      :visible.sync="showEditDialog"
      width="550px"
      append-to-body
    >
      <property-people-form
        v-if="editDialog"
        :id="id"
        :deptId="deptId"
        @closeEditDialog="closeEditDialog"
      ></property-people-form>
    </el-dialog>
  </div>
</template>

<script>
import { propertyPeopleApi } from "@/api/propertyManage";
import PropertyPeopleForm from "./PropertyPeopleForm";
export default {
  name: "PropertyPeopleList",
  props: {
    deptId: [String, Number],
  },
  components: {
    PropertyPeopleForm,
  },
  data() {
    return {
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await propertyPeopleApi.list({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        deptId: this.deptId,
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
    delCard(id) {
      this.$confirm("此操作会删除该物业公司下所有人员信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        propertyPeopleApi.del({ id: id }).then((res) => {
          if (res.code == 0) {
            if (this.tableData.length == 1 && this.pageNum > 1) {
              this.pageNum--;
            }
            this.getList();
            this.$message({
              type: "success",
              message: "删除成功!",
            });
          } else {
            this.$message({
              type: "error",
              message: "删除失败！",
            });
          }
        });
      });
    },
    closeEditDialog(num) {
      if (num) {
        this.getList();
      }
      this.editDialog = false;
      this.showEditDialog = false;
    },
  },
};
</script>

<style lang='scss' scoped>
.dialog_wrap {
}
</style>