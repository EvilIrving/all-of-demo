<!--
 * @Author: wanxin
 * @LastEditTime: 2022-07-25 14:44:17
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\defenseDispatch\personLiable\PersonLiable.vue
-->
<template>
  <div class="wrap">
    <!-- <div class="item_box_title">
      <p>山洪灾害责任人表</p>
    </div> -->
    <div
      class="info_option"
      style="width: 100%; padding: 16px 16px 16px 16px; box-sizing: border-box"
    >
      <el-input
        placeholder="请输入搜索内容"
        prefix-icon="el-icon-search"
        @change="getList"
        v-model="search"
        style="width: 240px; margin-right: 15px"
        clearable
      />
      <el-button
        type="primary"
        v-permission="'personLiable:add'"
        class="right"
        @click="editTable('新增', '')"
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
          prop="adName"
          label="镇（街道）"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="adminAdnm"
          label="行政村"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="naturalAdnm"
          label="自然村"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="chargeType"
          label="责任人类型"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="chargeLevel"
          label="责任人级别"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="pchargeName"
          label="责任人姓名"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="mobile"
          label="手机号码"
          align="center"
        ></el-table-column>
        <el-table-column prop="unit" label="所在单位" align="center">
        </el-table-column>
        <el-table-column prop="position" label="职务" align="center">
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.pchargeCode)"
                v-permission="'personLiable:edit'"
                >编辑</span
              >
              <span
                v-permission="'personLiable:del'"
                class="deal_btn deal_btn_del"
                @click="del(scope.row.pchargeCode)"
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
    <el-dialog :title="title" :visible.sync="showEditDialog" width="960px">
      <person-liable-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></person-liable-form>
    </el-dialog>
  </div>
</template>

<script>
import { PersonLiableApi } from "@/api/defenseDispatch.js";
import PersonLiableForm from "./PersonLiableForm";
export default {
  name: "personLiable",
  props: {},
  components: {
    PersonLiableForm,
  },
  data() {
    return {
      search: "",
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
      let res = await PersonLiableApi.pvPchargeList({
        key: this.search,
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
    del(id) {
      this.$confirm("此操作会删除该条信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        PersonLiableApi.delete({ code: id }).then((res) => {
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

    closeEditDialog() {
      this.getList();
      this.editDialog = false;
      this.showEditDialog = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .look {
    cursor: pointer;
  }
}
</style>
